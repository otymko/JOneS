package com.github.otymko.jos.example;

public class CheckLauncher {

  public static void main(String[] args) {
    var documentContext = "";
    var extCheck = new ExternalCheck(documentContext);
    extCheck.init();
    extCheck.visitNode("");
    extCheck.destroy();
  }

}
