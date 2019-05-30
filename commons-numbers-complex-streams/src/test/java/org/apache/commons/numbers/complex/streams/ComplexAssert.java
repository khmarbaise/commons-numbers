package org.apache.commons.numbers.complex.streams;

import org.apache.commons.numbers.complex.Complex;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractDateAssert;
import org.assertj.core.api.AbstractDoubleAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableTypeAssert;
import org.assertj.core.data.Offset;

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


  public ComplexAssert isCloseTo(Complex expected, float offset) {
    Assertions.assertThat(actual.getImaginary()).isCloseTo(expected.getImaginary(), Offset.offset(
        (double) offset));
    Assertions.assertThat(actual.getReal()).isCloseTo(expected.getReal(), Offset.offset(
        (double) offset));
    return myself;
  }

  public ComplexAssert withDelta(float delta) {
    myself.actual.getImaginary();
    return myself;
  }
}
