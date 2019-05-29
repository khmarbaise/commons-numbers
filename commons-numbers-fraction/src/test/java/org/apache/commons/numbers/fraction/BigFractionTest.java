/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.numbers.fraction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.data.Offset.offset;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import org.apache.commons.numbers.core.TestUtils;

import org.assertj.core.data.Offset;
import org.junit.Test;


public class BigFractionTest {

    private void assertFraction(int expectedNumerator, int expectedDenominator, BigFraction actual) {
        assertThat(actual.getNumeratorAsInt()).isEqualTo(expectedNumerator);
        assertThat(actual.getDenominatorAsInt()).isEqualTo(expectedDenominator);
    }

    private void assertFraction(long expectedNumerator, long expectedDenominator, BigFraction actual) {
        assertThat(actual.getNumeratorAsLong()).isEqualTo(expectedNumerator);
        assertThat(actual.getDenominatorAsLong()).isEqualTo(expectedDenominator);
    }

    @Test
    public void testConstructor() {
        assertFraction(0, 1, new BigFraction(0, 1));
        assertFraction(0, 1, new BigFraction(0l, 2l));
        assertFraction(0, 1, new BigFraction(0, -1));
        assertFraction(1, 2, new BigFraction(1, 2));
        assertFraction(1, 2, new BigFraction(2, 4));
        assertFraction(-1, 2, new BigFraction(-1, 2));
        assertFraction(-1, 2, new BigFraction(1, -2));
        assertFraction(-1, 2, new BigFraction(-2, 4));
        assertFraction(-1, 2, new BigFraction(2, -4));
        assertFraction(11, 1, new BigFraction(11));
        assertFraction(11, 1, new BigFraction(11l));
        assertFraction(11, 1, new BigFraction(new BigInteger("11")));

        assertFraction(0, 1, new BigFraction(0.00000000000001, 1.0e-5, 100));
        assertFraction(2, 5, new BigFraction(0.40000000000001, 1.0e-5, 100));
        assertFraction(15, 1, new BigFraction(15.0000000000001, 1.0e-5, 100));

        assertThat(new BigFraction(0.00000000000001).doubleValue())
            .isCloseTo(0.00000000000001, offset(0.0));
        assertThat(new BigFraction(0.40000000000001).doubleValue())
            .isCloseTo(0.40000000000001, offset(0.0));
        assertThat(new BigFraction(15.0000000000001).doubleValue())
            .isCloseTo(15.0000000000001, offset(0.0));
        assertFraction(3602879701896487l, 9007199254740992l, new BigFraction(0.40000000000001));
        assertFraction(1055531162664967l, 70368744177664l, new BigFraction(15.0000000000001));
        try {
            new BigFraction(null, BigInteger.ONE);
            fail("Expecting NullPointerException");
        } catch (NullPointerException npe) {
            // expected
        }
        try {
            new BigFraction(BigInteger.ONE, null);
            fail("Expecting NullPointerException");
        } catch (NullPointerException npe) {
            // expected
        }
        try {
            new BigFraction(BigInteger.ONE, BigInteger.ZERO);
            fail("Expecting ArithmeticException");
        } catch (ArithmeticException ignored) {
            // expected
        }
        try {
            new BigFraction(2.0 * Integer.MAX_VALUE, 1.0e-5, 100000);
            fail("Expecting ArithmeticException");
        } catch (ArithmeticException ignored) {
            // expected
        }
    }

    @Test(expected=FractionException.class)
    public void testGoldenRatio() {
        // the golden ratio is notoriously a difficult number for continuous fraction
        new BigFraction((1 + Math.sqrt(5)) / 2, 1.0e-12, 25);
    }

    // MATH-179
    @Test
    public void testDoubleConstructor() throws Exception {
        assertFraction(1, 2, new BigFraction((double) 1 / (double) 2, 1.0e-5, 100));
        assertFraction(1, 3, new BigFraction((double) 1 / (double) 3, 1.0e-5, 100));
        assertFraction(2, 3, new BigFraction((double) 2 / (double) 3, 1.0e-5, 100));
        assertFraction(1, 4, new BigFraction((double) 1 / (double) 4, 1.0e-5, 100));
        assertFraction(3, 4, new BigFraction((double) 3 / (double) 4, 1.0e-5, 100));
        assertFraction(1, 5, new BigFraction((double) 1 / (double) 5, 1.0e-5, 100));
        assertFraction(2, 5, new BigFraction((double) 2 / (double) 5, 1.0e-5, 100));
        assertFraction(3, 5, new BigFraction((double) 3 / (double) 5, 1.0e-5, 100));
        assertFraction(4, 5, new BigFraction((double) 4 / (double) 5, 1.0e-5, 100));
        assertFraction(1, 6, new BigFraction((double) 1 / (double) 6, 1.0e-5, 100));
        assertFraction(5, 6, new BigFraction((double) 5 / (double) 6, 1.0e-5, 100));
        assertFraction(1, 7, new BigFraction((double) 1 / (double) 7, 1.0e-5, 100));
        assertFraction(2, 7, new BigFraction((double) 2 / (double) 7, 1.0e-5, 100));
        assertFraction(3, 7, new BigFraction((double) 3 / (double) 7, 1.0e-5, 100));
        assertFraction(4, 7, new BigFraction((double) 4 / (double) 7, 1.0e-5, 100));
        assertFraction(5, 7, new BigFraction((double) 5 / (double) 7, 1.0e-5, 100));
        assertFraction(6, 7, new BigFraction((double) 6 / (double) 7, 1.0e-5, 100));
        assertFraction(1, 8, new BigFraction((double) 1 / (double) 8, 1.0e-5, 100));
        assertFraction(3, 8, new BigFraction((double) 3 / (double) 8, 1.0e-5, 100));
        assertFraction(5, 8, new BigFraction((double) 5 / (double) 8, 1.0e-5, 100));
        assertFraction(7, 8, new BigFraction((double) 7 / (double) 8, 1.0e-5, 100));
        assertFraction(1, 9, new BigFraction((double) 1 / (double) 9, 1.0e-5, 100));
        assertFraction(2, 9, new BigFraction((double) 2 / (double) 9, 1.0e-5, 100));
        assertFraction(4, 9, new BigFraction((double) 4 / (double) 9, 1.0e-5, 100));
        assertFraction(5, 9, new BigFraction((double) 5 / (double) 9, 1.0e-5, 100));
        assertFraction(7, 9, new BigFraction((double) 7 / (double) 9, 1.0e-5, 100));
        assertFraction(8, 9, new BigFraction((double) 8 / (double) 9, 1.0e-5, 100));
        assertFraction(1, 10, new BigFraction((double) 1 / (double) 10, 1.0e-5, 100));
        assertFraction(3, 10, new BigFraction((double) 3 / (double) 10, 1.0e-5, 100));
        assertFraction(7, 10, new BigFraction((double) 7 / (double) 10, 1.0e-5, 100));
        assertFraction(9, 10, new BigFraction((double) 9 / (double) 10, 1.0e-5, 100));
        assertFraction(1, 11, new BigFraction((double) 1 / (double) 11, 1.0e-5, 100));
        assertFraction(2, 11, new BigFraction((double) 2 / (double) 11, 1.0e-5, 100));
        assertFraction(3, 11, new BigFraction((double) 3 / (double) 11, 1.0e-5, 100));
        assertFraction(4, 11, new BigFraction((double) 4 / (double) 11, 1.0e-5, 100));
        assertFraction(5, 11, new BigFraction((double) 5 / (double) 11, 1.0e-5, 100));
        assertFraction(6, 11, new BigFraction((double) 6 / (double) 11, 1.0e-5, 100));
        assertFraction(7, 11, new BigFraction((double) 7 / (double) 11, 1.0e-5, 100));
        assertFraction(8, 11, new BigFraction((double) 8 / (double) 11, 1.0e-5, 100));
        assertFraction(9, 11, new BigFraction((double) 9 / (double) 11, 1.0e-5, 100));
        assertFraction(10, 11, new BigFraction((double) 10 / (double) 11, 1.0e-5, 100));
    }

    // MATH-181
    @Test
    public void testDigitLimitConstructor() throws Exception {
        assertFraction(2, 5, new BigFraction(0.4, 9));
        assertFraction(2, 5, new BigFraction(0.4, 99));
        assertFraction(2, 5, new BigFraction(0.4, 999));

        assertFraction(3, 5, new BigFraction(0.6152, 9));
        assertFraction(8, 13, new BigFraction(0.6152, 99));
        assertFraction(510, 829, new BigFraction(0.6152, 999));
        assertFraction(769, 1250, new BigFraction(0.6152, 9999));

        // MATH-996
        assertFraction(1, 2, new BigFraction(0.5000000001, 10));
    }

    // MATH-1029
    @Test(expected=ArithmeticException.class)
    public void testPositiveValueOverflow() {
        assertFraction((long) 1e10, 1, new BigFraction(1e10, 1000));
    }

    // MATH-1029
    @Test(expected=ArithmeticException.class)
    public void testNegativeValueOverflow() {
        assertFraction((long) -1e10, 1, new BigFraction(-1e10, 1000));
    }

    @Test
    public void testEpsilonLimitConstructor() throws Exception {
        assertFraction(2, 5, new BigFraction(0.4, 1.0e-5, 100));

        assertFraction(3, 5, new BigFraction(0.6152, 0.02, 100));
        assertFraction(8, 13, new BigFraction(0.6152, 1.0e-3, 100));
        assertFraction(251, 408, new BigFraction(0.6152, 1.0e-4, 100));
        assertFraction(251, 408, new BigFraction(0.6152, 1.0e-5, 100));
        assertFraction(510, 829, new BigFraction(0.6152, 1.0e-6, 100));
        assertFraction(769, 1250, new BigFraction(0.6152, 1.0e-7, 100));
    }

    @Test
    public void testCompareTo() {
        BigFraction first = new BigFraction(1, 2);
        BigFraction second = new BigFraction(1, 3);
        BigFraction third = new BigFraction(1, 2);

        assertThat(first.compareTo(first)).isEqualTo(0);
        assertThat(first.compareTo(third)).isEqualTo(0);
        assertThat(first.compareTo(second)).isEqualTo(1);
        assertThat(second.compareTo(first)).isEqualTo(-1);

        // these two values are different approximations of PI
        // the first  one is approximately PI - 3.07e-18
        // the second one is approximately PI + 1.936e-17
        BigFraction pi1 = new BigFraction(1068966896, 340262731);
        BigFraction pi2 = new BigFraction( 411557987, 131002976);
        assertThat(pi1.compareTo(pi2)).isEqualTo(-1);
        assertThat(pi2.compareTo(pi1)).isEqualTo(1);
        assertThat(pi1.doubleValue() - pi2.doubleValue()).isCloseTo(0.0, offset(1.0e-20));

    }

    @Test
    public void testDoubleValue() {
        BigFraction first = new BigFraction(1, 2);
        BigFraction second = new BigFraction(1, 3);

        assertThat(first.doubleValue()).isCloseTo(0.5, offset(0.0));
        assertThat(second.doubleValue()).isCloseTo(1.0 / 3.0, offset(0.0));
    }

    // MATH-744
    @Test
    public void testDoubleValueForLargeNumeratorAndDenominator() {
        final BigInteger pow400 = BigInteger.TEN.pow(400);
        final BigInteger pow401 = BigInteger.TEN.pow(401);
        final BigInteger two = new BigInteger("2");
        final BigFraction large = new BigFraction(pow401.add(BigInteger.ONE),
                                                  pow400.multiply(two));

        assertThat(large.doubleValue()).isCloseTo(5, offset(1e-15));
    }

    // MATH-744
    @Test
    public void testFloatValueForLargeNumeratorAndDenominator() {
        final BigInteger pow400 = BigInteger.TEN.pow(400);
        final BigInteger pow401 = BigInteger.TEN.pow(401);
        final BigInteger two = new BigInteger("2");
        final BigFraction large = new BigFraction(pow401.add(BigInteger.ONE),
                                                  pow400.multiply(two));

        assertThat(large.floatValue()).isCloseTo(5f, offset(1e-15f));
    }

    // NUMBERS-15
    @Test
    public void testDoubleValueForLargeNumeratorAndSmallDenominator() {
        final BigInteger pow300 = BigInteger.TEN.pow(300);
        final BigInteger pow330 = BigInteger.TEN.pow(330);
        final BigFraction large = new BigFraction(pow330.add(BigInteger.ONE),
                                                  pow300);

        assertThat(large.doubleValue()).isCloseTo(1e30, offset(1e-15));
    }

    // NUMBERS-15
    @Test
    public void testFloatValueForLargeNumeratorAndSmallDenominator() {
        final BigInteger pow30 = BigInteger.TEN.pow(30);
        final BigInteger pow40 = BigInteger.TEN.pow(40);
        final BigFraction large = new BigFraction(pow40.add(BigInteger.ONE),
                pow30);

        assertThat(large.floatValue()).isCloseTo(1e10f, offset(1e-15f));
    }

    @Test
    public void testFloatValue() {
        BigFraction first = new BigFraction(1, 2);
        BigFraction second = new BigFraction(1, 3);

        assertThat(first.floatValue()).isCloseTo(0.5f, offset(0.0f));
        assertThat(second.floatValue()).isCloseTo((float) (1.0 / 3.0), offset(0.0f));
    }

    @Test
    public void testIntValue() {
        BigFraction first = new BigFraction(1, 2);
        BigFraction second = new BigFraction(3, 2);

        assertThat(first.intValue()).isEqualTo(0);
        assertThat(second.intValue()).isEqualTo(1);
    }

    @Test
    public void testLongValue() {
        BigFraction first = new BigFraction(1, 2);
        BigFraction second = new BigFraction(3, 2);

        assertThat(first.longValue()).isEqualTo(0L);
        assertThat(second.longValue()).isEqualTo(1L);
    }

    @Test
    public void testConstructorDouble() {
        assertFraction(1, 2, new BigFraction(0.5));
        assertFraction(6004799503160661l, 18014398509481984l, new BigFraction(1.0 / 3.0));
        assertFraction(6124895493223875l, 36028797018963968l, new BigFraction(17.0 / 100.0));
        assertFraction(1784551352345559l, 562949953421312l, new BigFraction(317.0 / 100.0));
        assertFraction(-1, 2, new BigFraction(-0.5));
        assertFraction(-6004799503160661l, 18014398509481984l, new BigFraction(-1.0 / 3.0));
        assertFraction(-6124895493223875l, 36028797018963968l, new BigFraction(17.0 / -100.0));
        assertFraction(-1784551352345559l, 562949953421312l, new BigFraction(-317.0 / 100.0));
        for (double v : new double[] { Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY}) {
            try {
                new BigFraction(v);
                fail("Expecting IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                // expected
            }
        }
        assertThat(new BigFraction(Double.MAX_VALUE).getDenominatorAsLong()).isEqualTo(1l);
        assertThat(
            new BigFraction(Double.longBitsToDouble(0x0010000000000000L)).getNumeratorAsLong())
            .isEqualTo(1l);
        assertThat(new BigFraction(Double.MIN_VALUE).getNumeratorAsLong()).isEqualTo(1l);
    }

    @Test
    public void testAbs() {
        BigFraction a = new BigFraction(10, 21);
        BigFraction b = new BigFraction(-10, 21);
        BigFraction c = new BigFraction(10, -21);

        assertFraction(10, 21, a.abs());
        assertFraction(10, 21, b.abs());
        assertFraction(10, 21, c.abs());
    }

    @Test
    public void testReciprocal() {
        BigFraction f = null;

        f = new BigFraction(50, 75);
        f = f.reciprocal();
        assertThat(f.getNumeratorAsInt()).isEqualTo(3);
        assertThat(f.getDenominatorAsInt()).isEqualTo(2);

        f = new BigFraction(4, 3);
        f = f.reciprocal();
        assertThat(f.getNumeratorAsInt()).isEqualTo(3);
        assertThat(f.getDenominatorAsInt()).isEqualTo(4);

        f = new BigFraction(-15, 47);
        f = f.reciprocal();
        assertThat(f.getNumeratorAsInt()).isEqualTo(-47);
        assertThat(f.getDenominatorAsInt()).isEqualTo(15);

        f = new BigFraction(0, 3);
        try {
            f = f.reciprocal();
            fail("expecting ArithmeticException");
        } catch (ArithmeticException ignored) {
        }

        // large values
        f = new BigFraction(Integer.MAX_VALUE, 1);
        f = f.reciprocal();
        assertThat(f.getNumeratorAsInt()).isEqualTo(1);
        assertThat(f.getDenominatorAsInt()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    public void testNegate() {
        BigFraction f = null;

        f = new BigFraction(50, 75);
        f = f.negate();
        assertThat(f.getNumeratorAsInt()).isEqualTo(-2);
        assertThat(f.getDenominatorAsInt()).isEqualTo(3);

        f = new BigFraction(-50, 75);
        f = f.negate();
        assertThat(f.getNumeratorAsInt()).isEqualTo(2);
        assertThat(f.getDenominatorAsInt()).isEqualTo(3);

        // large values
        f = new BigFraction(Integer.MAX_VALUE - 1, Integer.MAX_VALUE);
        f = f.negate();
        assertThat(f.getNumeratorAsInt()).isEqualTo(Integer.MIN_VALUE + 2);
        assertThat(f.getDenominatorAsInt()).isEqualTo(Integer.MAX_VALUE);

    }

    @Test
    public void testAdd() {
        BigFraction a = new BigFraction(1, 2);
        BigFraction b = new BigFraction(2, 3);

        assertFraction(1, 1, a.add(a));
        assertFraction(7, 6, a.add(b));
        assertFraction(7, 6, b.add(a));
        assertFraction(4, 3, b.add(b));

        BigFraction f1 = new BigFraction(Integer.MAX_VALUE - 1, 1);
        BigFraction f2 = BigFraction.ONE;
        BigFraction f = f1.add(f2);
        assertThat(f.getNumeratorAsInt()).isEqualTo(Integer.MAX_VALUE);
        assertThat(f.getDenominatorAsInt()).isEqualTo(1);

        f1 = new BigFraction(-1, 13 * 13 * 2 * 2);
        f2 = new BigFraction(-2, 13 * 17 * 2);
        f = f1.add(f2);
        assertThat(f.getDenominatorAsInt()).isEqualTo(13 * 13 * 17 * 2 * 2);
        assertThat(f.getNumeratorAsInt()).isEqualTo(-17 - 2 * 13 * 2);

        try {
            f.add((BigFraction) null);
            fail("expecting NullPointerException");
        } catch (NullPointerException ex) {
        }

        // if this fraction is added naively, it will overflow.
        // check that it doesn't.
        f1 = new BigFraction(1, 32768 * 3);
        f2 = new BigFraction(1, 59049);
        f = f1.add(f2);
        assertThat(f.getNumeratorAsInt()).isEqualTo(52451);
        assertThat(f.getDenominatorAsInt()).isEqualTo(1934917632);

        f1 = new BigFraction(Integer.MIN_VALUE, 3);
        f2 = new BigFraction(1, 3);
        f = f1.add(f2);
        assertThat(f.getNumeratorAsInt()).isEqualTo(Integer.MIN_VALUE + 1);
        assertThat(f.getDenominatorAsInt()).isEqualTo(3);

        f1 = new BigFraction(Integer.MAX_VALUE - 1, 1);
        f = f1.add(BigInteger.ONE);
        assertThat(f.getNumeratorAsInt()).isEqualTo(Integer.MAX_VALUE);
        assertThat(f.getDenominatorAsInt()).isEqualTo(1);

        f = f.add(BigInteger.ZERO);
        assertThat(f.getNumeratorAsInt()).isEqualTo(Integer.MAX_VALUE);
        assertThat(f.getDenominatorAsInt()).isEqualTo(1);

        f1 = new BigFraction(Integer.MAX_VALUE - 1, 1);
        f = f1.add(1);
        assertThat(f.getNumeratorAsInt()).isEqualTo(Integer.MAX_VALUE);
        assertThat(f.getDenominatorAsInt()).isEqualTo(1);

        f = f.add(0);
        assertThat(f.getNumeratorAsInt()).isEqualTo(Integer.MAX_VALUE);
        assertThat(f.getDenominatorAsInt()).isEqualTo(1);

        f1 = new BigFraction(Integer.MAX_VALUE - 1, 1);
        f = f1.add(1l);
        assertThat(f.getNumeratorAsInt()).isEqualTo(Integer.MAX_VALUE);
        assertThat(f.getDenominatorAsInt()).isEqualTo(1);

        f = f.add(0l);
        assertThat(f.getNumeratorAsInt()).isEqualTo(Integer.MAX_VALUE);
        assertThat(f.getDenominatorAsInt()).isEqualTo(1);

    }

    @Test
    public void testDivide() {
        BigFraction a = new BigFraction(1, 2);
        BigFraction b = new BigFraction(2, 3);

        assertFraction(1, 1, a.divide(a));
        assertFraction(3, 4, a.divide(b));
        assertFraction(4, 3, b.divide(a));
        assertFraction(1, 1, b.divide(b));

        BigFraction f1 = new BigFraction(3, 5);
        BigFraction f2 = BigFraction.ZERO;
        try {
            f1.divide(f2);
            fail("expecting ArithmeticException");
        } catch (ArithmeticException ex) {
        }

        f1 = new BigFraction(0, 5);
        f2 = new BigFraction(2, 7);
        BigFraction f = f1.divide(f2);
        assertThat(f).isSameAs(BigFraction.ZERO);

        f1 = new BigFraction(2, 7);
        f2 = BigFraction.ONE;
        f = f1.divide(f2);
        assertThat(f.getNumeratorAsInt()).isEqualTo(2);
        assertThat(f.getDenominatorAsInt()).isEqualTo(7);

        f1 = new BigFraction(1, Integer.MAX_VALUE);
        f = f1.divide(f1);
        assertThat(f.getNumeratorAsInt()).isEqualTo(1);
        assertThat(f.getDenominatorAsInt()).isEqualTo(1);

        f1 = new BigFraction(Integer.MIN_VALUE, Integer.MAX_VALUE);
        f2 = new BigFraction(1, Integer.MAX_VALUE);
        f = f1.divide(f2);
        assertThat(f.getNumeratorAsInt()).isEqualTo(Integer.MIN_VALUE);
        assertThat(f.getDenominatorAsInt()).isEqualTo(1);

        try {
            f.divide((BigFraction) null);
            fail("expecting NullPointerException");
        } catch (NullPointerException ex) {
        }

        f1 = new BigFraction(Integer.MIN_VALUE, Integer.MAX_VALUE);
        f = f1.divide(BigInteger.valueOf(Integer.MIN_VALUE));
        assertThat(f.getDenominatorAsInt()).isEqualTo(Integer.MAX_VALUE);
        assertThat(f.getNumeratorAsInt()).isEqualTo(1);

        f1 = new BigFraction(Integer.MIN_VALUE, Integer.MAX_VALUE);
        f = f1.divide(Integer.MIN_VALUE);
        assertThat(f.getDenominatorAsInt()).isEqualTo(Integer.MAX_VALUE);
        assertThat(f.getNumeratorAsInt()).isEqualTo(1);

        f1 = new BigFraction(Integer.MIN_VALUE, Integer.MAX_VALUE);
        f = f1.divide((long) Integer.MIN_VALUE);
        assertThat(f.getDenominatorAsInt()).isEqualTo(Integer.MAX_VALUE);
        assertThat(f.getNumeratorAsInt()).isEqualTo(1);

    }

    @Test
    public void testMultiply() {
        BigFraction a = new BigFraction(1, 2);
        BigFraction b = new BigFraction(2, 3);

        assertFraction(1, 4, a.multiply(a));
        assertFraction(1, 3, a.multiply(b));
        assertFraction(1, 3, b.multiply(a));
        assertFraction(4, 9, b.multiply(b));

        BigFraction f1 = new BigFraction(Integer.MAX_VALUE, 1);
        BigFraction f2 = new BigFraction(Integer.MIN_VALUE, Integer.MAX_VALUE);
        BigFraction f = f1.multiply(f2);
        assertThat(f.getNumeratorAsInt()).isEqualTo(Integer.MIN_VALUE);
        assertThat(f.getDenominatorAsInt()).isEqualTo(1);

        f = f2.multiply(Integer.MAX_VALUE);
        assertThat(f.getNumeratorAsInt()).isEqualTo(Integer.MIN_VALUE);
        assertThat(f.getDenominatorAsInt()).isEqualTo(1);

        f = f2.multiply((long) Integer.MAX_VALUE);
        assertThat(f.getNumeratorAsInt()).isEqualTo(Integer.MIN_VALUE);
        assertThat(f.getDenominatorAsInt()).isEqualTo(1);

        try {
            f.multiply((BigFraction) null);
            fail("expecting NullPointerException");
        } catch (NullPointerException ex) {
        }

    }

    @Test
    public void testSubtract() {
        BigFraction a = new BigFraction(1, 2);
        BigFraction b = new BigFraction(2, 3);

        assertFraction(0, 1, a.subtract(a));
        assertFraction(-1, 6, a.subtract(b));
        assertFraction(1, 6, b.subtract(a));
        assertFraction(0, 1, b.subtract(b));

        BigFraction f = new BigFraction(1, 1);
        try {
            f.subtract((BigFraction) null);
            fail("expecting NullPointerException");
        } catch (NullPointerException ex) {
        }

        // if this fraction is subtracted naively, it will overflow.
        // check that it doesn't.
        BigFraction f1 = new BigFraction(1, 32768 * 3);
        BigFraction f2 = new BigFraction(1, 59049);
        f = f1.subtract(f2);
        assertThat(f.getNumeratorAsInt()).isEqualTo(-13085);
        assertThat(f.getDenominatorAsInt()).isEqualTo(1934917632);

        f1 = new BigFraction(Integer.MIN_VALUE, 3);
        f2 = new BigFraction(1, 3).negate();
        f = f1.subtract(f2);
        assertThat(f.getNumeratorAsInt()).isEqualTo(Integer.MIN_VALUE + 1);
        assertThat(f.getDenominatorAsInt()).isEqualTo(3);

        f1 = new BigFraction(Integer.MAX_VALUE, 1);
        f2 = BigFraction.ONE;
        f = f1.subtract(f2);
        assertThat(f.getNumeratorAsInt()).isEqualTo(Integer.MAX_VALUE - 1);
        assertThat(f.getDenominatorAsInt()).isEqualTo(1);

    }

    @Test
    public void testBigDecimalValue() {
        assertThat(new BigFraction(1, 2).bigDecimalValue()).isEqualTo(new BigDecimal(0.5));
        assertThat(new BigFraction(3, 10000).bigDecimalValue()).isEqualTo(new BigDecimal("0.0003"));
        assertThat(new BigFraction(1, 3).bigDecimalValue(RoundingMode.DOWN))
            .isEqualTo(new BigDecimal("0"));
        assertThat(new BigFraction(1, 3).bigDecimalValue(3, RoundingMode.DOWN))
            .isEqualTo(new BigDecimal("0.333"));
    }

    @Test
    public void testEqualsAndHashCode() {
        BigFraction zero = new BigFraction(0, 1);
        BigFraction nullFraction = null;
        assertThat(zero.equals(zero)).isTrue();
        assertThat(zero.equals(nullFraction)).isFalse();
        assertThat(zero.equals(Double.valueOf(0))).isFalse();
        BigFraction zero2 = new BigFraction(0, 2);
        assertThat(zero.equals(zero2)).isTrue();
        assertThat(zero2.hashCode()).isEqualTo(zero.hashCode());
        BigFraction one = new BigFraction(1, 1);
        assertThat((one.equals(zero) || zero.equals(one))).isFalse();
        assertThat(one.equals(BigFraction.ONE)).isTrue();
    }

    @Test
    public void testGetReducedFraction() {
        BigFraction threeFourths = new BigFraction(3, 4);
        assertThat(threeFourths.equals(BigFraction.getReducedFraction(6, 8))).isTrue();
        assertThat(BigFraction.ZERO.equals(BigFraction.getReducedFraction(0, -1))).isTrue();
        try {
            BigFraction.getReducedFraction(1, 0);
            fail("expecting ArithmeticException");
        } catch (ArithmeticException ex) {
            // expected
        }
        assertThat(-1)
            .isEqualTo(BigFraction.getReducedFraction(2, Integer.MIN_VALUE).getNumeratorAsInt());
        assertThat(-1).isEqualTo(BigFraction.getReducedFraction(1, -1).getNumeratorAsInt());
    }

    @Test
    public void testPercentage() {
        assertThat(new BigFraction(1, 2).percentageValue()).isCloseTo(50.0, offset(1.0e-15));
    }

    @Test
    public void testPow() {
        assertThat(new BigFraction(2, 3).pow(13)).isEqualTo(new BigFraction(8192, 1594323));
        assertThat(new BigFraction(2, 3).pow(13l)).isEqualTo(new BigFraction(8192, 1594323));
        assertThat(new BigFraction(2, 3).pow(BigInteger.valueOf(13l)))
            .isEqualTo(new BigFraction(8192, 1594323));
        assertThat(new BigFraction(2, 3).pow(0)).isEqualTo(BigFraction.ONE);
        assertThat(new BigFraction(2, 3).pow(0l)).isEqualTo(BigFraction.ONE);
        assertThat(new BigFraction(2, 3).pow(BigInteger.valueOf(0l))).isEqualTo(BigFraction.ONE);
        assertThat(new BigFraction(2, 3).pow(-13)).isEqualTo(new BigFraction(1594323, 8192));
        assertThat(new BigFraction(2, 3).pow(-13l)).isEqualTo(new BigFraction(1594323, 8192));
        assertThat(new BigFraction(2, 3).pow(BigInteger.valueOf(-13l)))
            .isEqualTo(new BigFraction(1594323, 8192));
    }

    @Test
    public void testMath340() {
        BigFraction fractionA = new BigFraction(0.00131);
        BigFraction fractionB = new BigFraction(.37).reciprocal();
        BigFraction errorResult = fractionA.multiply(fractionB);
        BigFraction correctResult = new BigFraction(fractionA.getNumerator().multiply(fractionB.getNumerator()),
                                                    fractionA.getDenominator().multiply(fractionB.getDenominator()));
        assertThat(errorResult).isEqualTo(correctResult);
    }

    @Test
    public void testSerial() {
        BigFraction[] fractions = {
            new BigFraction(3, 4), BigFraction.ONE, BigFraction.ZERO,
            new BigFraction(17), new BigFraction(Math.PI, 1000),
            new BigFraction(-5, 2)
        };
        for (BigFraction fraction : fractions) {
            assertThat(TestUtils.serializeAndRecover(fraction)).isEqualTo(fraction);
        }
    }
}
