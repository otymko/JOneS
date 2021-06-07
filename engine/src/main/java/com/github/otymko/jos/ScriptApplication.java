package com.github.otymko.jos;

public class ScriptApplication {

  public static void main(String[] args) {

//    if (args.length == 0) {
//      throw new RuntimeException("Должен быть передан хотя бы один аргумент");
//    }
//
//    final var pathToScriptFromArgument = args[0];
//    if (pathToScriptFromArgument.isEmpty()) {
//      throw new RuntimeException("Путь к скрипту не должен быть пустой");
//    }
//
//    Path pathToScript;
//    var pwd = Path.of("");
//    var file = new File(pathToScriptFromArgument);
//    if (file.exists()) {
//      pathToScript = file.toPath().toAbsolutePath();
//    } else {
//      var otherPathToScript = Path.of(pwd.toString(), pathToScriptFromArgument);
//      if (otherPathToScript.toFile().exists()) {
//        throw new RuntimeException("Путь к скрипту не должен быть пустой");
//      }
//      pathToScript = otherPathToScript.toAbsolutePath();
//    }
//
//    var engine = new ScriptEngine();
//    int exitCode = engine.execute(pathToScript);
//    System.exit(0);
  }

}
