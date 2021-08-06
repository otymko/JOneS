package com.github.otymko.jos.compiler;

import com.github.otymko.jos.runtime.context.type.ValueFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ConstantDefinitionTest {

  @Test
  void testEquals() {
    List<ConstantDefinition> list = new ArrayList<>();

    var value1 = new ConstantDefinition(ValueFactory.create(100));
    var value2 = new ConstantDefinition(ValueFactory.create(100));

    list.add(value1);

    assertThat(value1).isEqualTo(value2);
    assertThat(list.contains(value2)).isTrue();
  }

}
