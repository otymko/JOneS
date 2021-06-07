package com.github.otymko.jos.example;

import com.github.otymko.jos.compiler.image.ModuleImage;
import com.github.otymko.jos.context.ScriptDrivenObject;
import com.github.otymko.jos.context.value.Value;
import com.github.otymko.jos.label.ContextMethod;
import com.github.otymko.jos.label.ContextProperty;
import com.github.otymko.jos.label.ContextType;
import lombok.Setter;

@ContextType(name = "ВнешняяПроверка", alias = "ExternalCheck")
public class Check extends ScriptDrivenObject {
  public static final String AT_CREATE_METHOD = "ПриСозданииОбъекта";
  public static final String INIT_METHOD = "Инициализация";
  public static final String VISIT_NODE_METHOD = "ПосетитьУзел";
  public static final String DESTROY_METHOD = "Уничтожить";

  // пока просто заглушка
  private ExternalCheck externalCheck;

  @Setter
  @ContextProperty(name = "Ключ", alias = "Key")
  private String checkKey = "";

  protected Check(ModuleImage moduleImage) {
    super(moduleImage);
  }

  @ContextMethod(name = "ЗафиксироватьНарушение", alias = "CreateViolation")
  public void createViolation(String message) {
    // TODO: заглушка
    System.out.print(checkKey + ": " + message);
  }

  @Override
  public void callMethodScript(int methodId, Value[] arguments) {

  }

}
