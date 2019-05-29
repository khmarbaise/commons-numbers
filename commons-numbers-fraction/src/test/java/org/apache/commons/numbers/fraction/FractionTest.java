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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.data.Offset.offset;

import org.apache.commons.numbers.core.TestUtils;
import org.junit.Test;


/**
 */
public class FractionTest {

    private void assertFraction(int expectedNumerator, int expectedDenominator, Fraction actual) {
        assertThat(actual.getNumerator()).isEqualTo(expectedNumerator);
        assertThat(actual.getDenominator()).isEqualTo(expectedDenominator);
    }

    @Test
    public void testConstructor() {
        FractionAssert.assertThat(new Fraction(0, 1)).hasNumerator(0).and().hasDenominator(1);

        assertFraction(0, 1, new Fraction(0, 1));
        assertFraction(0, 1, new Fraction(0, 2));
        assertFraction(0, 1, new Fraction(0, -1));
        assertFraction(1, 2, new Fraction(1, 2));
        assertFraction(1, 2, new Fraction(2, 4));
        assertFraction(-1, 2, new Fraction(-1, 2));
        assertFraction(-1, 2, new Fraction(1, -2));
        assertFraction(-1, 2, new Fraction(-2, 4));
        assertFraction(-1, 2, new Fraction(2, -4));

        // overflow
        try {
            new Fraction(Integer.MIN_VALUE, -1);
            fail("XXXX");
        } catch (ArithmeticException ex) {
            // success
        }
        try {
            new Fraction(1, Integer.MIN_VALUE);
            fail("XXXX");
        } catch (ArithmeticException ex) {
            // success
        }

        assertFraction(0, 1, new Fraction(0.00000000000001));
        assertFraction(2, 5, new Fraction(0.40000000000001));
        assertFraction(15, 1, new Fraction(15.0000000000001));
    }

    @Test
    public void testGoldenRatio() {
        // the golden ratio is notoriously a difficult number for continuous fraction
        assertThatExceptionOfType(ArithmeticException.class)
            .isThrownBy(() -> new Fraction((1 + Math.sqrt(5)) / 2, 1.0e-12, 25))
            .withMessage("Unable to convert 1.618 to fraction after 25 iterations");
    }

    // MATH-179
    @Test
    public void testDoubleConstructor() throws Exception  {
        assertFraction(1, 2, new Fraction((double)1 / (double)2));
        assertFraction(1, 3, new Fraction((double)1 / (double)3));
        assertFraction(2, 3, new Fraction((double)2 / (double)3));
        assertFraction(1, 4, new Fraction((double)1 / (double)4));
        assertFraction(3, 4, new Fraction((double)3 / (double)4));
        assertFraction(1, 5, new Fraction((double)1 / (double)5));
        assertFraction(2, 5, new Fraction((double)2 / (double)5));
        assertFraction(3, 5, new Fraction((double)3 / (double)5));
        assertFraction(4, 5, new Fraction((double)4 / (double)5));
        assertFraction(1, 6, new Fraction((double)1 / (double)6));
        assertFraction(5, 6, new Fraction((double)5 / (double)6));
        assertFraction(1, 7, new Fraction((double)1 / (double)7));
        assertFraction(2, 7, new Fraction((double)2 / (double)7));
        assertFraction(3, 7, new Fraction((double)3 / (double)7));
        assertFraction(4, 7, new Fraction((double)4 / (double)7));
        assertFraction(5, 7, new Fraction((double)5 / (double)7));
        assertFraction(6, 7, new Fraction((double)6 / (double)7));
        assertFraction(1, 8, new Fraction((double)1 / (double)8));
        assertFraction(3, 8, new Fraction((double)3 / (double)8));
        assertFraction(5, 8, new Fraction((double)5 / (double)8));
        assertFraction(7, 8, new Fraction((double)7 / (double)8));
        assertFraction(1, 9, new Fraction((double)1 / (double)9));
        assertFraction(2, 9, new Fraction((double)2 / (double)9));
        assertFraction(4, 9, new Fraction((double)4 / (double)9));
        assertFraction(5, 9, new Fraction((double)5 / (double)9));
        assertFraction(7, 9, new Fraction((double)7 / (double)9));
        assertFraction(8, 9, new Fraction((double)8 / (double)9));
        assertFraction(1, 10, new Fraction((double)1 / (double)10));
        assertFraction(3, 10, new Fraction((double)3 / (double)10));
        assertFraction(7, 10, new Fraction((double)7 / (double)10));
        assertFraction(9, 10, new Fraction((double)9 / (double)10));
        assertFraction(1, 11, new Fraction((double)1 / (double)11));
        assertFraction(2, 11, new Fraction((double)2 / (double)11));
        assertFraction(3, 11, new Fraction((double)3 / (double)11));
        assertFraction(4, 11, new Fraction((double)4 / (double)11));
        assertFraction(5, 11, new Fraction((double)5 / (double)11));
        assertFraction(6, 11, new Fraction((double)6 / (double)11));
        assertFraction(7, 11, new Fraction((double)7 / (double)11));
        assertFraction(8, 11, new Fraction((double)8 / (double)11));
        assertFraction(9, 11, new Fraction((double)9 / (double)11));
        assertFraction(10, 11, new Fraction((double)10 / (double)11));
    }

    // MATH-181
    @Test
    public void testDigitLimitConstructor() throws Exception  {
        assertFraction(2, 5, new Fraction(0.4,   9));
        assertFraction(2, 5, new Fraction(0.4,  99));
        assertFraction(2, 5, new Fraction(0.4, 999));

        assertFraction(3, 5,      new Fraction(0.6152,    9));
        assertFraction(8, 13,     new Fraction(0.6152,   99));
        assertFraction(510, 829,  new Fraction(0.6152,  999));
        assertFraction(769, 1250, new Fraction(0.6152, 9999));

        // MATH-996
        assertFraction(1, 2, new Fraction(0.5000000001, 10));
    }

    @Test
    public void testIntegerOverflow() {
        checkIntegerOverflow(0.75000000001455192);
        checkIntegerOverflow(1.0e10);
        checkIntegerOverflow(-1.0e10);
        checkIntegerOverflow(-43979.60679604749);
    }

    private void checkIntegerOverflow(double a) {
        try {
            @SuppressWarnings("unused")
            Fraction f = new Fraction(a, 1.0e-12, 1000);
            //System.out.println(f.getNumerator() + "/" + f.getDenominator());
            fail("an exception should have been thrown");
        } catch (ArithmeticException ignored) {
            // expected behavior
        }
    }

    @Test
    public void testEpsilonLimitConstructor() throws Exception  {
        assertFraction(2, 5, new Fraction(0.4, 1.0e-5, 100));

        assertFraction(3, 5,      new Fraction(0.6152, 0.02, 100));
        assertFraction(8, 13,     new Fraction(0.6152, 1.0e-3, 100));
        assertFraction(251, 408,  new Fraction(0.6152, 1.0e-4, 100));
        assertFraction(251, 408,  new Fraction(0.6152, 1.0e-5, 100));
        assertFraction(510, 829,  new Fraction(0.6152, 1.0e-6, 100));
        assertFraction(769, 1250, new Fraction(0.6152, 1.0e-7, 100));
    }

    @Test
    public void testCompareTo() {
        Fraction first = new Fraction(1, 2);
        Fraction second = new Fraction(1, 3);
        Fraction third = new Fraction(1, 2);

        assertThat(first.compareTo(first)).isEqualTo(0);
        assertThat(first.compareTo(third)).isEqualTo(0);
        assertThat(first.compareTo(second)).isEqualTo(1);
        assertThat(second.compareTo(first)).isEqualTo(-1);

        // these two values are different approximations of PI
        // the first  one is approximately PI - 3.07e-18
        // the second one is approximately PI + 1.936e-17
        Fraction pi1 = new Fraction(1068966896, 340262731);
        Fraction pi2 = new Fraction( 411557987, 131002976);
        assertThat(pi1.compareTo(pi2)).isEqualTo(-1);
        assertThat(pi2.compareTo(pi1)).isEqualTo(1);
        assertThat(pi1.doubleValue() - pi2.doubleValue()).isCloseTo(0.0, offset(1.0e-20));
    }

    @Test
    public void testDoubleValue() {
        Fraction first = new Fraction(1, 2);
        Fraction second = new Fraction(1, 3);

        assertThat(first.doubleValue()).isCloseTo(0.5, offset(0.0));
        assertThat(second.doubleValue()).isCloseTo(1.0 / 3.0, offset(0.0));
    }

    @Test
    public void testFloatValue() {
        Fraction first = new Fraction(1, 2);
        Fraction second = new Fraction(1, 3);

        assertThat(first.floatValue()).isCloseTo(0.5f, offset(0.0f));
        assertThat(second.floatValue()).isCloseTo((float) (1.0 / 3.0), offset(0.0f));
    }

    @Test
    public void testIntValue() {
        Fraction first = new Fraction(1, 2);
        Fraction second = new Fraction(3, 2);

        assertThat(first.intValue()).isEqualTo(0);
        assertThat(second.intValue()).isEqualTo(1);
    }

    @Test
    public void testLongValue() {
        Fraction first = new Fraction(1, 2);
        Fraction second = new Fraction(3, 2);

        assertThat(first.longValue()).isEqualTo(0L);
        assertThat(second.longValue()).isEqualTo(1L);
    }

    @Test
    public void testConstructorDouble() {
        assertFraction(1, 2, new Fraction(0.5));
        assertFraction(1, 3, new Fraction(1.0 / 3.0));
        assertFraction(17, 100, new Fraction(17.0 / 100.0));
        assertFraction(317, 100, new Fraction(317.0 / 100.0));
        assertFraction(-1, 2, new Fraction(-0.5));
        assertFraction(-1, 3, new Fraction(-1.0 / 3.0));
        assertFraction(-17, 100, new Fraction(17.0 / -100.0));
        assertFraction(-317, 100, new Fraction(-317.0 / 100.0));
    }

    @Test
    public void testAbs() {
        Fraction a = new Fraction(10, 21);
        Fraction b = new Fraction(-10, 21);
        Fraction c = new Fraction(10, -21);

        assertFraction(10, 21, a.abs());
        assertFraction(10, 21, b.abs());
        assertFraction(10, 21, c.abs());
    }

    @Test
    public void testPercentage() {
        assertThat(new Fraction(1, 2).percentageValue()).isCloseTo(50.0, offset(1.0e-15));
    }

    @Test
    public void testMath835() {
        final int numer = Integer.MAX_VALUE / 99;
        final int denom = 1;
        final double percentage = 100 * ((double) numer) / denom;
        final Fraction frac = new Fraction(numer, denom);
        // With the implementation that preceded the fix suggested in MATH-835,
        // this test was failing, due to overflow.
        assertThat(frac.percentageValue()).isCloseTo(percentage, offset(Math.ulp(percentage)));
    }

    @Test
    public void testMath1261() {
        final Fraction a = new Fraction(Integer.MAX_VALUE, 2);
        final Fraction b = a.multiply(2);
        assertThat(b.equals(new Fraction(Integer.MAX_VALUE))).isTrue();

        final Fraction c = new Fraction(2, Integer.MAX_VALUE);
        final Fraction d = c.divide(2);
        assertThat(d.equals(new Fraction(1, Integer.MAX_VALUE))).isTrue();
    }

    @Test
    public void testReciprocal() {
        Fraction f = null;

        f = new Fraction(50, 75);
        f = f.reciprocal();
        assertThat(f.getNumerator()).isEqualTo(3);
        assertThat(f.getDenominator()).isEqualTo(2);

        f = new Fraction(4, 3);
        f = f.reciprocal();
        assertThat(f.getNumerator()).isEqualTo(3);
        assertThat(f.getDenominator()).isEqualTo(4);

        f = new Fraction(-15, 47);
        f = f.reciprocal();
        assertThat(f.getNumerator()).isEqualTo(-47);
        assertThat(f.getDenominator()).isEqualTo(15);

        f = new Fraction(0, 3);
        try {
            f = f.reciprocal();
            fail("expecting ArithmeticException");
        } catch (ArithmeticException ignored) {}

        // large values
        f = new Fraction(Integer.MAX_VALUE, 1);
        f = f.reciprocal();
        assertThat(f.getNumerator()).isEqualTo(1);
        assertThat(f.getDenominator()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    public void testNegate() {
        Fraction f = null;

        f = new Fraction(50, 75);
        f = f.negate();
        assertThat(f.getNumerator()).isEqualTo(-2);
        assertThat(f.getDenominator()).isEqualTo(3);

        f = new Fraction(-50, 75);
        f = f.negate();
        assertThat(f.getNumerator()).isEqualTo(2);
        assertThat(f.getDenominator()).isEqualTo(3);

        // large values
        f = new Fraction(Integer.MAX_VALUE-1, Integer.MAX_VALUE);
        f = f.negate();
        assertThat(f.getNumerator()).isEqualTo(Integer.MIN_VALUE + 2);
        assertThat(f.getDenominator()).isEqualTo(Integer.MAX_VALUE);

        f = new Fraction(Integer.MIN_VALUE, 1);
        try {
            f = f.negate();
            fail("expecting ArithmeticException");
        } catch (ArithmeticException ex) {}
    }

    @Test
    public void testAdd() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(2, 3);

        assertFraction(1, 1, a.add(a));
        assertFraction(7, 6, a.add(b));
        assertFraction(7, 6, b.add(a));
        assertFraction(4, 3, b.add(b));

        Fraction f1 = new Fraction(Integer.MAX_VALUE - 1, 1);
        Fraction f2 = Fraction.ONE;
        Fraction f = f1.add(f2);
        assertThat(f.getNumerator()).isEqualTo(Integer.MAX_VALUE);
        assertThat(f.getDenominator()).isEqualTo(1);
        f = f1.add(1);
        assertThat(f.getNumerator()).isEqualTo(Integer.MAX_VALUE);
        assertThat(f.getDenominator()).isEqualTo(1);

        f1 = new Fraction(-1, 13*13*2*2);
        f2 = new Fraction(-2, 13*17*2);
        f = f1.add(f2);
        assertThat(f.getDenominator()).isEqualTo(13 * 13 * 17 * 2 * 2);
        assertThat(f.getNumerator()).isEqualTo(-17 - 2 * 13 * 2);

        try {
            f.add(null);
            fail("expecting NullArgumentException");
        } catch (NullPointerException ex) {}

        // if this fraction is added naively, it will overflow.
        // check that it doesn't.
        f1 = new Fraction(1,32768*3);
        f2 = new Fraction(1,59049);
        f = f1.add(f2);
        assertThat(f.getNumerator()).isEqualTo(52451);
        assertThat(f.getDenominator()).isEqualTo(1934917632);

        f1 = new Fraction(Integer.MIN_VALUE, 3);
        f2 = new Fraction(1,3);
        f = f1.add(f2);
        assertThat(f.getNumerator()).isEqualTo(Integer.MIN_VALUE + 1);
        assertThat(f.getDenominator()).isEqualTo(3);

        f1 = new Fraction(Integer.MAX_VALUE - 1, 1);
        f2 = Fraction.ONE;
        f = f1.add(f2);
        assertThat(f.getNumerator()).isEqualTo(Integer.MAX_VALUE);
        assertThat(f.getDenominator()).isEqualTo(1);

        try {
            f = f.add(Fraction.ONE); // should overflow
            fail("expecting ArithmeticException but got: " + f.toString());
        } catch (ArithmeticException ex) {}

        // denominator should not be a multiple of 2 or 3 to trigger overflow
        f1 = new Fraction(Integer.MIN_VALUE, 5);
        f2 = new Fraction(-1,5);
        try {
            f = f1.add(f2); // should overflow
            fail("expecting ArithmeticException but got: " + f.toString());
        } catch (ArithmeticException ex) {}

        try {
            f= new Fraction(-Integer.MAX_VALUE, 1);
            f = f.add(f);
            fail("expecting ArithmeticException");
        } catch (ArithmeticException ex) {}

        try {
            f= new Fraction(-Integer.MAX_VALUE, 1);
            f = f.add(f);
            fail("expecting ArithmeticException");
        } catch (ArithmeticException ex) {}

        f1 = new Fraction(3,327680);
        f2 = new Fraction(2,59049);
        try {
            f = f1.add(f2); // should overflow
            fail("expecting ArithmeticException but got: " + f.toString());
        } catch (ArithmeticException ex) {}
    }

    @Test
    public void testDivide() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(2, 3);

        assertFraction(1, 1, a.divide(a));
        assertFraction(3, 4, a.divide(b));
        assertFraction(4, 3, b.divide(a));
        assertFraction(1, 1, b.divide(b));

        Fraction f1 = new Fraction(3, 5);
        Fraction f2 = Fraction.ZERO;
        try {
            f1.divide(f2);
            fail("expecting FractionException");
        } catch (FractionException ex) {}

        f1 = new Fraction(0, 5);
        f2 = new Fraction(2, 7);
        Fraction f = f1.divide(f2);
        assertThat(f).isSameAs(Fraction.ZERO);

        f1 = new Fraction(2, 7);
        f2 = Fraction.ONE;
        f = f1.divide(f2);
        assertThat(f.getNumerator()).isEqualTo(2);
        assertThat(f.getDenominator()).isEqualTo(7);

        f1 = new Fraction(1, Integer.MAX_VALUE);
        f = f1.divide(f1);
        assertThat(f.getNumerator()).isEqualTo(1);
        assertThat(f.getDenominator()).isEqualTo(1);

        f1 = new Fraction(Integer.MIN_VALUE, Integer.MAX_VALUE);
        f2 = new Fraction(1, Integer.MAX_VALUE);
        f = f1.divide(f2);
        assertThat(f.getNumerator()).isEqualTo(Integer.MIN_VALUE);
        assertThat(f.getDenominator()).isEqualTo(1);

        try {
            f.divide(null);
            fail("NullArgumentException");
        } catch (NullPointerException ex) {}

        try {
            f1 = new Fraction(1, Integer.MAX_VALUE);
            f = f1.divide(f1.reciprocal());  // should overflow
            fail("expecting ArithmeticException");
        } catch (ArithmeticException ex) {}
        try {
            f1 = new Fraction(1, -Integer.MAX_VALUE);
            f = f1.divide(f1.reciprocal());  // should overflow
            fail("expecting ArithmeticException");
        } catch (ArithmeticException ex) {}

        f1 = new Fraction(6, 35);
        f  = f1.divide(15);
        assertThat(f.getNumerator()).isEqualTo(2);
        assertThat(f.getDenominator()).isEqualTo(175);

    }

    @Test
    public void testMultiply() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(2, 3);

        assertFraction(1, 4, a.multiply(a));
        assertFraction(1, 3, a.multiply(b));
        assertFraction(1, 3, b.multiply(a));
        assertFraction(4, 9, b.multiply(b));

        Fraction f1 = new Fraction(Integer.MAX_VALUE, 1);
        Fraction f2 = new Fraction(Integer.MIN_VALUE, Integer.MAX_VALUE);
        Fraction f = f1.multiply(f2);
        assertThat(f.getNumerator()).isEqualTo(Integer.MIN_VALUE);
        assertThat(f.getDenominator()).isEqualTo(1);

        try {
            f.multiply(null);
            fail("expecting NullArgumentException");
        } catch (NullPointerException ex) {}

        f1 = new Fraction(6, 35);
        f  = f1.multiply(15);
        assertThat(f.getNumerator()).isEqualTo(18);
        assertThat(f.getDenominator()).isEqualTo(7);
    }

    @Test
    public void testPow() {
        Fraction a = new Fraction(3, 7);
        assertFraction(1, 1, a.pow(0));
        assertFraction(3, 7, a.pow(1));
        assertFraction(7, 3, a.pow(-1));
        assertFraction(9, 49, a.pow(2));
        assertFraction(49, 9, a.pow(-2));

        Fraction b = new Fraction(3, -7);
        assertFraction(1, 1, b.pow(0));
        assertFraction(-3, 7, b.pow(1));
        assertFraction(-7, 3, b.pow(-1));
        assertFraction(9, 49, a.pow(2));
        assertFraction(49, 9, a.pow(-2));

        Fraction c = new Fraction(0, -11);
        assertFraction(0, 1, c.pow(Integer.MAX_VALUE));
    }

    @Test
    public void testSubtract() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(2, 3);

        assertFraction(0, 1, a.subtract(a));
        assertFraction(-1, 6, a.subtract(b));
        assertFraction(1, 6, b.subtract(a));
        assertFraction(0, 1, b.subtract(b));

        Fraction f = new Fraction(1,1);
        try {
            f.subtract(null);
            fail("expecting NullArgumentException");
        } catch (NullPointerException ex) {}

        // if this fraction is subtracted naively, it will overflow.
        // check that it doesn't.
        Fraction f1 = new Fraction(1,32768*3);
        Fraction f2 = new Fraction(1,59049);
        f = f1.subtract(f2);
        assertThat(f.getNumerator()).isEqualTo(-13085);
        assertThat(f.getDenominator()).isEqualTo(1934917632);

        f1 = new Fraction(Integer.MIN_VALUE, 3);
        f2 = new Fraction(1,3).negate();
        f = f1.subtract(f2);
        assertThat(f.getNumerator()).isEqualTo(Integer.MIN_VALUE + 1);
        assertThat(f.getDenominator()).isEqualTo(3);

        f1 = new Fraction(Integer.MAX_VALUE, 1);
        f2 = Fraction.ONE;
        f = f1.subtract(f2);
        assertThat(f.getNumerator()).isEqualTo(Integer.MAX_VALUE - 1);
        assertThat(f.getDenominator()).isEqualTo(1);
        f = f1.subtract(1);
        assertThat(f.getNumerator()).isEqualTo(Integer.MAX_VALUE - 1);
        assertThat(f.getDenominator()).isEqualTo(1);

        try {
            f1 = new Fraction(1, Integer.MAX_VALUE);
            f2 = new Fraction(1, Integer.MAX_VALUE - 1);
            f = f1.subtract(f2);
            fail("expecting ArithmeticException");  //should overflow
        } catch (ArithmeticException ex) {}

        // denominator should not be a multiple of 2 or 3 to trigger overflow
        f1 = new Fraction(Integer.MIN_VALUE, 5);
        f2 = new Fraction(1,5);
        try {
            f = f1.subtract(f2); // should overflow
            fail("expecting ArithmeticException but got: " + f.toString());
        } catch (ArithmeticException ex) {}

        try {
            f= new Fraction(Integer.MIN_VALUE, 1);
            f = f.subtract(Fraction.ONE);
            fail("expecting ArithmeticException");
        } catch (ArithmeticException ex) {}

        try {
            f= new Fraction(Integer.MAX_VALUE, 1);
            f = f.subtract(Fraction.ONE.negate());
            fail("expecting ArithmeticException");
        } catch (ArithmeticException ex) {}

        f1 = new Fraction(3,327680);
        f2 = new Fraction(2,59049);
        try {
            f = f1.subtract(f2); // should overflow
            fail("expecting ArithmeticException but got: " + f.toString());
        } catch (ArithmeticException ex) {}
    }

    @Test
    public void testEqualsAndHashCode() {
        Fraction zero  = new Fraction(0,1);
        Fraction nullFraction = null;
        assertThat(zero.equals(zero)).isTrue();
        assertThat(zero.equals(nullFraction)).isFalse();
        assertThat(zero.equals(Double.valueOf(0))).isFalse();
        Fraction zero2 = new Fraction(0,2);
        assertThat(zero.equals(zero2)).isTrue();
        assertThat(zero2.hashCode()).isEqualTo(zero.hashCode());
        Fraction one = new Fraction(1,1);
        assertThat((one.equals(zero) || zero.equals(one))).isFalse();
    }

    @Test
    public void testGetReducedFraction() {
        Fraction threeFourths = new Fraction(3, 4);
        assertThat(threeFourths.equals(Fraction.getReducedFraction(6, 8))).isTrue();
        assertThat(Fraction.ZERO.equals(Fraction.getReducedFraction(0, -1))).isTrue();
        try {
            Fraction.getReducedFraction(1, 0);
            fail("expecting ArithmeticException");
        } catch (ArithmeticException ignored) {
            // expected
        }
        assertThat(-1).isEqualTo(Fraction.getReducedFraction
            (2, Integer.MIN_VALUE).getNumerator());
        assertThat(-1).isEqualTo(Fraction.getReducedFraction
            (1, -1).getNumerator());
    }

    @Test
    public void testToString() {
        assertThat(new Fraction(0, 3).toString()).isEqualTo("0");
        assertThat(new Fraction(6, 2).toString()).isEqualTo("3");
        assertThat(new Fraction(18, 27).toString()).isEqualTo("2 / 3");
    }

    @Test
    public void testSerial() {
        Fraction[] fractions = {
            new Fraction(3, 4), Fraction.ONE, Fraction.ZERO,
            new Fraction(17), new Fraction(Math.PI, 1000),
            new Fraction(-5, 2)
        };
        for (Fraction fraction : fractions) {
            assertThat(TestUtils.serializeAndRecover(fraction)).isEqualTo(fraction);
        }
    }
}
