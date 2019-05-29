package org.apache.commons.numbers.complex.streams;

import org.apache.commons.numbers.complex.Complex;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class ComplexAssert extends AbstractAssert<ComplexAssert, Complex> {

  public ComplexAssert(Complex complex) {
    super(complex, ComplexAssert.class);
  }

  public static ComplexAssert assertThat(Complex actual) {
    return new ComplexAssert(actual);
  }

  public ComplexAssert isNaN() {
    isNotNull();
    Assertions.assertThat(myself.actual.getReal()).isNaN();
    Assertions.assertThat(myself.actual.getImaginary()).isNaN();
    return myself;
  }
}
