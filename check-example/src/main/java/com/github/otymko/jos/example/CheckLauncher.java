/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
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
