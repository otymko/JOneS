package com.github.otymko.jos.example;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExternalCheck {
  private final List<Check> checks;

  public ExternalCheck(String documentContext) {
    var files = getCheckFiles();
    checks = createChecks(files);
//    checks.forEach(check -> check.callMethodScript(Check.AT_CREATE_METHOD, new String[0]));
  }

  public void init() {
//    checks.forEach(check -> check.callMethodScript(Check.INIT_METHOD, new String[0]));
  }

  public void visitNode(String node) {
//    checks.forEach(check -> check.callMethodScript(Check.VISIT_NODE_METHOD, new String[1]));
  }

  public void destroy() {
//    checks.forEach(check -> check.callMethodScript(Check.DESTROY_METHOD, new String[0]));
  }

  private List<Check> createChecks(List<File> files) {
    List<Check> checks = new ArrayList<>();

//    files.forEach(file -> {
//
//      ModuleImage image;
//      try {
//        image = ScriptCompiler.compile(file.toPath());
//      } catch (Exception e) {
//        e.printStackTrace();
//        return;
//      }
//      var check = new Check(this, image);
//      checks.add(check);
//
//    });

    return checks;
  }

  private static List<File> getCheckFiles() {
    return null;
//    var directory = new File("C:\\Develop\\Projects\\OSJ\\OneScript4J\\src\\test\\resources\\checks");
//    return Arrays.stream(directory.listFiles())
//      .filter(file -> file.getName().endsWith(".os"))
//      .collect(Collectors.toList());
  }

}
