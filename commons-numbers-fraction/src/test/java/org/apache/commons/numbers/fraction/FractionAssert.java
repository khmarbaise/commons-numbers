package org.apache.commons.numbers.fraction;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

/**
 * Custom Assertion for Handling of Fraction class.
 *
 * @author Karl Heinz Marbaise
 */
public class FractionAssert extends AbstractAssert<FractionAssert, Fraction> {

  private FractionAssert(Fraction complex) {
    super(complex, FractionAssert.class);
  }

  public static FractionAssert assertThat(Fraction actual) {
    return new FractionAssert(actual);
  }

  public FractionAssert hasNumerator(int expected) {
    isNotNull();
    Assertions.assertThat(myself.actual.getNumerator())
        .as("Expected getNumerator <%s> instead of <%s>", myself.actual.getNumerator(),
            expected).isEqualTo(expected);
    return myself;
  }

  public FractionAssert and() {
    return myself;
  }

  public FractionAssert hasDenominator(int expected) {
    isNotNull();

    Assertions.assertThat(myself.actual.getDenominator())
        .as("Expected getDenominator <%s> instead of <%s>", myself.actual.getDenominator(),
            expected).isEqualTo(expected);

    return myself;
  }
}
