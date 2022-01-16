/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
import com.github.otymko.jos.runtime.context.ContextInitializer;
import com.github.otymko.jos.runtime.context.type.collection.V8KeyAndValue;
//import com.github.otymko.jos.runtime.context.type.enumeration.SymbolsContext;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

class StressTest {

  public static void main(String[] args) {

    var pattern = Pattern.compile("(\\d)(\\d\\d)");
    var value = "s456";

    var matcher = pattern.matcher(value);

    var a = 1;

  }

//  @Test
//  void test() {
//
//    var myClass = V8KeyAndValue.class;
//    var result = ContextInitializer.getProperties(myClass);
//
//
//    var a = 1;
//  }

}
