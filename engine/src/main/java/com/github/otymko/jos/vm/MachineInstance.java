package com.github.otymko.jos.vm;

import com.github.otymko.jos.compiler.MethodDescriptor;
import com.github.otymko.jos.compiler.image.ModuleImage;
import com.github.otymko.jos.context.ContextInitializer;
import com.github.otymko.jos.context.RuntimeContextInstance;
import com.github.otymko.jos.context.ScriptDrivenObject;
import com.github.otymko.jos.context.value.Arithmetic;
import com.github.otymko.jos.context.value.Value;
import com.github.otymko.jos.context.value.ValueFactory;
import com.github.otymko.jos.context.value.Variable;
import com.github.otymko.jos.vm.info.MethodInfo;
import com.github.otymko.jos.vm.info.VariableInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * Экземпляр стековой машины
 */
public class MachineInstance {
  private final Map<OperationCode, Consumer<Integer>> commands = createMachineCommands();

  @Getter
  private final List<Scope> scopes = new ArrayList<>();

  private final Stack<Value> operationStack = new Stack<>();

  private final Stack<ExecutionFrame> callStack = new Stack<>();
  private ExecutionFrame currentFrame;

  private ModuleImage currentImage;


  public void implementContext(RuntimeContextInstance context) {
    var methods = ContextInitializer.getContextMethods(context.getClass());
    // FIXME: this
    var variables = new Variable[0];
    var scope = new Scope(context, variables, methods);
    scopes.add(scope);
  }

  public void executeModuleBody(ScriptDrivenObject sdo) {
    currentImage = sdo.getModuleImage();
    createModuleScope(sdo);
    executeModuleBody(currentImage);
  }

  private void createModuleScope(ScriptDrivenObject sdo) {
    Variable[] variables = createVariables(currentImage.getVariables());

    // + из sdo
    MethodInfo[] methods = new MethodInfo[currentImage.getMethods().size()];
    var index = 0;
    for (var method : currentImage.getMethods()) {
      methods[index] = method.getSignature();
    }

    var scope = new Scope(sdo, variables, methods);
    scopes.add(scope);
  }

  private Variable[] createVariables(List<VariableInfo> variableInfos) {
    Variable[] variables = new Variable[variableInfos.size()];
    int index = 0;
    for (var variableInfo : variableInfos) {
      var variable = new Variable();
      variable.setName(variableInfo.getName());
      variable.setValue(ValueFactory.create());
      variables[index] = variable;
      index++;
    }
    return variables;
  }

  // ??
  public void executeModuleBody(ModuleImage image) {
    if (image.getEntry() >= 0) {
      var methodDescriptor = image.getMethods().get(image.getEntry());
      prepareFrame(image, methodDescriptor);
      executeCode();
    }
  }

  // ???
  private void prepareFrame(ModuleImage image, MethodDescriptor methodDescriptor) {
    var frame = new ExecutionFrame();
    frame.setImage(currentImage);
    frame.setInstructionPointer(methodDescriptor.getEntry());
    var variables = createVariables(methodDescriptor.getVariables());
    frame.setLocalVariables(variables);

    frame.setModuleLoadIndex(scopes.size() - 1);
    frame.setModuleScope(scopes.get(frame.getModuleLoadIndex()));

    pushFrame(frame);
  }

  private void executeCode() {
    startExecuting();
  }

  private void startExecuting() {

    while (currentFrame.getInstructionPointer() >= 0
      && currentFrame.getInstructionPointer() < currentImage.getCode().size()) {

      var command = currentImage.getCode().get(currentFrame.getInstructionPointer());
      commands.get(command.getCode()).accept(command.getArgument());

    }

  }

  private Map<OperationCode, Consumer<Integer>> createMachineCommands() {
    Map<OperationCode, Consumer<Integer>> map = new EnumMap<>(OperationCode.class);
    map.put(OperationCode.LineNum, this::lineNum);
    map.put(OperationCode.PushConst, this::pushConst);
    map.put(OperationCode.ArgNum, this::argNum);
    map.put(OperationCode.CallProc, this::callProc);
    map.put(OperationCode.LoadLoc, this::loadLoc);
    map.put(OperationCode.PushLoc, this::pushLoc);
    map.put(OperationCode.PushVar, this::pushVar);
    map.put(OperationCode.LoadVar, this::loadVar);
    map.put(OperationCode.Return, this::_return);
    map.put(OperationCode.Add, this::add);
    map.put(OperationCode.Sub, this::sub);
    map.put(OperationCode.Mul, this::mul);
    map.put(OperationCode.Div, this::div);
    return map;
  }

  private void add(int argument) {
    var valueOne = operationStack.pop();
    var valueTwo = operationStack.pop();
    operationStack.push(Arithmetic.add(valueOne, valueTwo));
    nextInstruction();
  }

  private void sub(int argument) {
    var valueOne = operationStack.pop();
    var valueTwo = operationStack.pop();
    operationStack.push(Arithmetic.sub(valueTwo, valueOne));
    nextInstruction();
  }

  private void mul(int argument) {
    var valueOne = operationStack.pop();
    var valueTwo = operationStack.pop();
    operationStack.push(Arithmetic.mul(valueOne, valueTwo));
    nextInstruction();
  }

  private void div(int argument) {
    var valueOne = operationStack.pop();
    var valueTwo = operationStack.pop();
    operationStack.push(Arithmetic.div(valueTwo, valueOne));
    nextInstruction();
  }

  private void lineNum(int argument) {
    currentFrame.setLineNumber(argument);
    nextInstruction();
  }

  private void pushConst(int argument) {
    operationStack.push(currentImage.getConstants().get(argument).getValue());
    nextInstruction();
  }

  // FIXME: ??
  private void argNum(int argument) {
    operationStack.push(ValueFactory.create(argument));
    nextInstruction();
  }

  private void callProc(int argument) {
    var address = currentImage.getMethodRefs().get(argument);
    if (address.getContextId() == scopes.size() - 1) {

      // уход из зацикливания
      nextInstruction();

      // FIXME: под общую гребенку: хранить в sdo сколько методов из модели, сколько из кода
      var methodDescriptor = currentImage.getMethods().get(address.getSymbolId());

      var frame = new ExecutionFrame();
      frame.setImage(currentImage);
      frame.setModuleLoadIndex(scopes.size() - 1);
      frame.setModuleScope(scopes.get(frame.getModuleLoadIndex()));
      frame.setMethodName(methodDescriptor.getSignature().getName());
      // здесь нужно учесть значения по умолчанию и т.п.
      var variables = createVariables(methodDescriptor.getVariables());
      frame.setLocalVariables(variables);
      frame.setInstructionPointer(methodDescriptor.getEntry());
      pushFrame(frame);
    } else {
      var scope = getScopes().get(address.getContextId());
      var method = scope.getMethods()[address.getSymbolId()];

      int argumentCount = (int) operationStack.pop().asNumber();
      Value[] argumentValues = new Value[argumentCount];

      for (var index = argumentCount - 1; index >= 0; index--) {
        var value = operationStack.pop();
        argumentValues[index] = value;
      }

      callContext(scope.getInstance(), address.getSymbolId(), method, argumentValues);
      nextInstruction();
    }
//    nextInstruction();
  }

  private void pushFrame(ExecutionFrame frame) {
    callStack.push(frame);
    setFrame(frame);
  }

  private void setFrame(ExecutionFrame frame) {
    currentImage = frame.getImage();
    scopes.set(frame.getModuleLoadIndex(), frame.getModuleScope());
    currentFrame = frame;
  }

  private void loadLoc(int argument) {
    currentFrame.getLocalVariables()[argument].setValue(operationStack.pop());
    nextInstruction();
  }

  private void loadVar(int argument) {
    var address = currentImage.getVariableRefs().get(argument);
    var scope = scopes.get(address.getContextId());
    scope.getVariables()[address.getSymbolId()].setValue(operationStack.pop());
    nextInstruction();
//    Scope scope;
//    if (address.getContextId() == scopes.size() - 1) {
//      scope = scopes.get(scopes.size() - 1);
//    } else {
//      scope = scopes.get(address.getContextId());
//    }
//    operationStack.push(scope.getVariables()[address.getSymbolId()].getValue());
  }

  private void pushLoc(int argument) {
    operationStack.push(currentFrame.getLocalVariables()[argument].getValue());
    nextInstruction();
  }

  private void pushVar(int argument) {
    var address = currentImage.getVariableRefs().get(argument);
    Scope scope;
    if (address.getContextId() == scopes.size() - 1) {
      scope = scopes.get(scopes.size() - 1);
    } else {
      scope = scopes.get(address.getContextId());
    }
    operationStack.push(scope.getVariables()[address.getSymbolId()].getValue());
    nextInstruction();
  }

  private void nextInstruction() {
    currentFrame.setInstructionPointer(currentFrame.getInstructionPointer() + 1);
  }

  private void callContext(RuntimeContextInstance drivenObject, int methodId, MethodInfo methodInfo, Value[] arguments) {
    drivenObject.callMethodScript(methodId, arguments);
  }

  private void _return(int argument) {

    popFrame();

  }

  private void popFrame() {
    callStack.pop();
    setFrame(callStack.peek());
  }

}
