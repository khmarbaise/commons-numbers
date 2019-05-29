package org.apache.commons.numbers.fraction;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

/**
 * Custom Assertion for Handling of BigFraction class.
 *
 * @author Karl Heinz Marbaise
 */
public class BigFractionAssert extends AbstractAssert<BigFractionAssert, BigFraction> {

  private BigFractionAssert(BigFraction complex) {
    super(complex, BigFractionAssert.class);
  }

  public static BigFractionAssert assertThat(BigFraction actual) {
    return new BigFractionAssert(actual);
  }

  public BigFractionAssert hasNumeratorAsInt(int expected) {
    isNotNull();
    Assertions.assertThat(myself.actual.getNumeratorAsInt())
        .as("Expected getNumeratorAsInt <%s> instead of <%s>", myself.actual.getNumeratorAsInt(),
            expected).isEqualTo(expected);
    return myself;
  }

  public BigFractionAssert and() {
    return myself;
  }

  public BigFractionAssert hasDenominatorAsInt(int expected) {
    isNotNull();

    Assertions.assertThat(myself.actual.getDenominatorAsInt())
        .as("Expected getDenominatorAsInt <%s> instead of <%s>", myself.actual.getDenominatorAsInt(),
            expected).isEqualTo(expected);

    return myself;
  }
}
