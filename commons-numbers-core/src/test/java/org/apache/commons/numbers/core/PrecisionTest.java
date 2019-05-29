/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.apache.commons.numbers.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.data.Offset.offset;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

/**
 * Test cases for the {@link Precision} class.
 *
 */
public class PrecisionTest {
    @Test
    public void testEqualsWithRelativeTolerance() {
        assertThat(Precision.equalsWithRelativeTolerance(0d, 0d, 0d)).isTrue();
        assertThat(Precision.equalsWithRelativeTolerance(0d, 1 / Double.NEGATIVE_INFINITY, 0d))
            .isTrue();

        final double eps = 1e-14;
        assertThat(Precision.equalsWithRelativeTolerance(1.987654687654968, 1.987654687654988, eps))
            .isFalse();
        assertThat(Precision.equalsWithRelativeTolerance(1.987654687654968, 1.987654687654987, eps))
            .isTrue();
        assertThat(Precision.equalsWithRelativeTolerance(1.987654687654968, 1.987654687654948, eps))
            .isFalse();
        assertThat(Precision.equalsWithRelativeTolerance(1.987654687654968, 1.987654687654949, eps))
            .isTrue();

        assertThat(Precision.equalsWithRelativeTolerance(Precision.SAFE_MIN, 0.0, eps)).isFalse();

        assertThat(Precision.equalsWithRelativeTolerance(1.0000000000001e-300, 1e-300, eps))
            .isFalse();
        assertThat(Precision.equalsWithRelativeTolerance(1.00000000000001e-300, 1e-300, eps))
            .isTrue();

        assertThat(Precision.equalsWithRelativeTolerance(Double.NEGATIVE_INFINITY, 1.23, eps))
            .isFalse();
        assertThat(Precision.equalsWithRelativeTolerance(Double.POSITIVE_INFINITY, 1.23, eps))
            .isFalse();

        assertThat(Precision
            .equalsWithRelativeTolerance(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, eps))
            .isTrue();
        assertThat(Precision
            .equalsWithRelativeTolerance(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, eps))
            .isTrue();
        assertThat(Precision
            .equalsWithRelativeTolerance(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, eps))
            .isFalse();

        assertThat(Precision.equalsWithRelativeTolerance(Double.NaN, 1.23, eps)).isFalse();
        assertThat(Precision.equalsWithRelativeTolerance(Double.NaN, Double.NaN, eps)).isFalse();
    }

    @Test
    public void testEqualsIncludingNaN() {
        double[] testArray = {
            Double.NaN,
            Double.POSITIVE_INFINITY,
            Double.NEGATIVE_INFINITY,
            1d,
            0d };
        for (int i = 0; i < testArray.length; i++) {
            for (int j = 0; j < testArray.length; j++) {
                if (i == j) {
                    assertThat(Precision.equalsIncludingNaN(testArray[i], testArray[j])).isTrue();
                    assertThat(Precision.equalsIncludingNaN(testArray[j], testArray[i])).isTrue();
                } else {
                    assertThat(!Precision.equalsIncludingNaN(testArray[i], testArray[j])).isTrue();
                    assertThat(!Precision.equalsIncludingNaN(testArray[j], testArray[i])).isTrue();
                }
            }
        }
    }

    @Test
    public void testEqualsWithAllowedDelta() {
        assertThat(Precision.equals(153.0000, 153.0000, .0625)).isTrue();
        assertThat(Precision.equals(153.0000, 153.0625, .0625)).isTrue();
        assertThat(Precision.equals(152.9375, 153.0000, .0625)).isTrue();
        assertThat(Precision.equals(153.0000, 153.0625, .0624)).isFalse();
        assertThat(Precision.equals(152.9374, 153.0000, .0625)).isFalse();
        assertThat(Precision.equals(Double.NaN, Double.NaN, 1.0)).isFalse();
        assertThat(Precision.equals(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 1.0))
            .isTrue();
        assertThat(Precision.equals(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 1.0))
            .isTrue();
        assertThat(Precision.equals(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1.0))
            .isFalse();
    }

    @Test
    public void testMath475() {
        final double a = 1.7976931348623182E16;
        final double b = Math.nextUp(a);

        double diff = Math.abs(a - b);
        // Because they are adjacent floating point numbers, "a" and "b" are
        // considered equal even though the allowed error is smaller than
        // their difference.
        assertThat(Precision.equals(a, b, 0.5 * diff)).isTrue();

        final double c = Math.nextUp(b);
        diff = Math.abs(a - c);
        // Because "a" and "c" are not adjacent, the tolerance is taken into
        // account for assessing equality.
        assertThat(Precision.equals(a, c, diff)).isTrue();
        assertThat(Precision.equals(a, c, (1 - 1e-16) * diff)).isFalse();
    }

    @Test
    public void testEqualsIncludingNaNWithAllowedDelta() {
        assertThat(Precision.equalsIncludingNaN(153.0000, 153.0000, .0625)).isTrue();
        assertThat(Precision.equalsIncludingNaN(153.0000, 153.0625, .0625)).isTrue();
        assertThat(Precision.equalsIncludingNaN(152.9375, 153.0000, .0625)).isTrue();
        assertThat(Precision.equalsIncludingNaN(Double.NaN, Double.NaN, 1.0)).isTrue();
        assertThat(
            Precision.equalsIncludingNaN(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 1.0))
            .isTrue();
        assertThat(
            Precision.equalsIncludingNaN(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 1.0))
            .isTrue();
        assertThat(
            Precision.equalsIncludingNaN(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1.0))
            .isFalse();
        assertThat(Precision.equalsIncludingNaN(153.0000, 153.0625, .0624)).isFalse();
        assertThat(Precision.equalsIncludingNaN(152.9374, 153.0000, .0625)).isFalse();
    }

    // Tests for floating point equality
    @Test
    public void testFloatEqualsWithAllowedUlps() {
        assertThat(Precision.equals(0.0f, -0.0f)).as("+0.0f == -0.0f").isTrue();
        assertThat(Precision.equals(0.0f, -0.0f, 1)).as("+0.0f == -0.0f (1 ulp)").isTrue();
        float oneFloat = 1.0f;
        assertThat(
            Precision.equals(oneFloat, Float.intBitsToFloat(1 + Float.floatToIntBits(oneFloat))))
            .as("1.0f == 1.0f + 1 ulp").isTrue();
        assertThat(
            Precision.equals(oneFloat, Float.intBitsToFloat(1 + Float.floatToIntBits(oneFloat)), 1))
            .as("1.0f == 1.0f + 1 ulp (1 ulp)").isTrue();
        assertThat(
            Precision.equals(oneFloat, Float.intBitsToFloat(2 + Float.floatToIntBits(oneFloat)), 1))
            .as("1.0f != 1.0f + 2 ulp (1 ulp)").isFalse();

        assertThat(Precision.equals(153.0f, 153.0f, 1)).isTrue();

        // These tests need adjusting for floating point precision
//        Assert.assertTrue(Precision.equals(153.0f, 153.00000000000003f, 1));
//        Assert.assertFalse(Precision.equals(153.0f, 153.00000000000006f, 1));
//        Assert.assertTrue(Precision.equals(153.0f, 152.99999999999997f, 1));
//        Assert.assertFalse(Precision.equals(153f, 152.99999999999994f, 1));
//
//        Assert.assertTrue(Precision.equals(-128.0f, -127.99999999999999f, 1));
//        Assert.assertFalse(Precision.equals(-128.0f, -127.99999999999997f, 1));
//        Assert.assertTrue(Precision.equals(-128.0f, -128.00000000000003f, 1));
//        Assert.assertFalse(Precision.equals(-128.0f, -128.00000000000006f, 1));

        assertThat(Precision.equals(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, 1)).isTrue();
        assertThat(Precision.equals(Double.MAX_VALUE, Float.POSITIVE_INFINITY, 1)).isTrue();

        assertThat(Precision.equals(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, 1)).isTrue();
        assertThat(Precision.equals(-Float.MAX_VALUE, Float.NEGATIVE_INFINITY, 1)).isTrue();

        assertThat(Precision.equals(Float.NaN, Float.NaN, 1)).isFalse();
        assertThat(Precision.equals(Float.NaN, Float.NaN, 0)).isFalse();
        assertThat(Precision.equals(Float.NaN, 0, 0)).isFalse();
        assertThat(Precision.equals(Float.NaN, Float.POSITIVE_INFINITY, 0)).isFalse();
        assertThat(Precision.equals(Float.NaN, Float.NEGATIVE_INFINITY, 0)).isFalse();

        assertThat(Precision.equals(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, 100000))
            .isFalse();
    }

    @Test
    public void testEqualsWithAllowedUlps() {
        assertThat(Precision.equals(0.0, -0.0, 1)).isTrue();

        assertThat(Precision.equals(1.0, 1 + Math.ulp(1d), 1)).isTrue();
        assertThat(Precision.equals(1.0, 1 + 2 * Math.ulp(1d), 1)).isFalse();

        final double nUp1 = Math.nextAfter(1d, Double.POSITIVE_INFINITY);
        final double nnUp1 = Math.nextAfter(nUp1, Double.POSITIVE_INFINITY);
        assertThat(Precision.equals(1.0, nUp1, 1)).isTrue();
        assertThat(Precision.equals(nUp1, nnUp1, 1)).isTrue();
        assertThat(Precision.equals(1.0, nnUp1, 1)).isFalse();

        assertThat(Precision.equals(0.0, Math.ulp(0d), 1)).isTrue();
        assertThat(Precision.equals(0.0, -Math.ulp(0d), 1)).isTrue();

        assertThat(Precision.equals(153.0, 153.0, 1)).isTrue();

        assertThat(Precision.equals(153.0, 153.00000000000003, 1)).isTrue();
        assertThat(Precision.equals(153.0, 153.00000000000006, 1)).isFalse();
        assertThat(Precision.equals(153.0, 152.99999999999997, 1)).isTrue();
        assertThat(Precision.equals(153, 152.99999999999994, 1)).isFalse();

        assertThat(Precision.equals(-128.0, -127.99999999999999, 1)).isTrue();
        assertThat(Precision.equals(-128.0, -127.99999999999997, 1)).isFalse();
        assertThat(Precision.equals(-128.0, -128.00000000000003, 1)).isTrue();
        assertThat(Precision.equals(-128.0, -128.00000000000006, 1)).isFalse();

        assertThat(Precision.equals(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 1))
            .isTrue();
        assertThat(Precision.equals(Double.MAX_VALUE, Double.POSITIVE_INFINITY, 1)).isTrue();

        assertThat(Precision.equals(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 1))
            .isTrue();
        assertThat(Precision.equals(-Double.MAX_VALUE, Double.NEGATIVE_INFINITY, 1)).isTrue();

        assertThat(Precision.equals(Double.NaN, Double.NaN, 1)).isFalse();
        assertThat(Precision.equals(Double.NaN, Double.NaN, 0)).isFalse();
        assertThat(Precision.equals(Double.NaN, 0, 0)).isFalse();
        assertThat(Precision.equals(Double.NaN, Double.POSITIVE_INFINITY, 0)).isFalse();
        assertThat(Precision.equals(Double.NaN, Double.NEGATIVE_INFINITY, 0)).isFalse();

        assertThat(Precision.equals(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 100000))
            .isFalse();
    }

    @Test
    public void testEqualsIncludingNaNWithAllowedUlps() {
        assertThat(Precision.equalsIncludingNaN(0.0, -0.0, 1)).isTrue();

        assertThat(Precision.equalsIncludingNaN(1.0, 1 + Math.ulp(1d), 1)).isTrue();
        assertThat(Precision.equalsIncludingNaN(1.0, 1 + 2 * Math.ulp(1d), 1)).isFalse();

        final double nUp1 = Math.nextAfter(1d, Double.POSITIVE_INFINITY);
        final double nnUp1 = Math.nextAfter(nUp1, Double.POSITIVE_INFINITY);
        assertThat(Precision.equalsIncludingNaN(1.0, nUp1, 1)).isTrue();
        assertThat(Precision.equalsIncludingNaN(nUp1, nnUp1, 1)).isTrue();
        assertThat(Precision.equalsIncludingNaN(1.0, nnUp1, 1)).isFalse();

        assertThat(Precision.equalsIncludingNaN(0.0, Math.ulp(0d), 1)).isTrue();
        assertThat(Precision.equalsIncludingNaN(0.0, -Math.ulp(0d), 1)).isTrue();

        assertThat(Precision.equalsIncludingNaN(153.0, 153.0, 1)).isTrue();

        assertThat(Precision.equalsIncludingNaN(153.0, 153.00000000000003, 1)).isTrue();
        assertThat(Precision.equalsIncludingNaN(153.0, 153.00000000000006, 1)).isFalse();
        assertThat(Precision.equalsIncludingNaN(153.0, 152.99999999999997, 1)).isTrue();
        assertThat(Precision.equalsIncludingNaN(153, 152.99999999999994, 1)).isFalse();

        assertThat(Precision.equalsIncludingNaN(-128.0, -127.99999999999999, 1)).isTrue();
        assertThat(Precision.equalsIncludingNaN(-128.0, -127.99999999999997, 1)).isFalse();
        assertThat(Precision.equalsIncludingNaN(-128.0, -128.00000000000003, 1)).isTrue();
        assertThat(Precision.equalsIncludingNaN(-128.0, -128.00000000000006, 1)).isFalse();

        assertThat(
            Precision.equalsIncludingNaN(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 1))
            .isTrue();
        assertThat(Precision.equalsIncludingNaN(Double.MAX_VALUE, Double.POSITIVE_INFINITY, 1))
            .isTrue();

        assertThat(
            Precision.equalsIncludingNaN(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 1))
            .isTrue();
        assertThat(Precision.equalsIncludingNaN(-Double.MAX_VALUE, Double.NEGATIVE_INFINITY, 1))
            .isTrue();

        assertThat(Precision.equalsIncludingNaN(Double.NaN, Double.NaN, 1)).isTrue();

        assertThat(Precision
            .equalsIncludingNaN(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 100000))
            .isFalse();
    }

    @Test
    public void testCompareToEpsilon() {
        assertThat(Precision.compareTo(152.33, 152.32, .011)).isEqualTo(0);
        assertThat(Precision.compareTo(152.308, 152.32, .011) < 0).isTrue();
        assertThat(Precision.compareTo(152.33, 152.318, .011) > 0).isTrue();
        assertThat(Precision.compareTo(Double.MIN_VALUE, +0.0, Double.MIN_VALUE)).isEqualTo(0);
        assertThat(Precision.compareTo(Double.MIN_VALUE, -0.0, Double.MIN_VALUE)).isEqualTo(0);
    }

    @Test
    public void testCompareToMaxUlps() {
        double a     = 152.32;
        double delta = Math.ulp(a);
        for (int i = 0; i <= 10; ++i) {
            if (i <= 5) {
                assertThat(Precision.compareTo(a, a + i * delta, 5)).isEqualTo(0);
                assertThat(Precision.compareTo(a, a - i * delta, 5)).isEqualTo(0);
            } else {
                assertThat(Precision.compareTo(a, a + i * delta, 5)).isEqualTo(-1);
                assertThat(Precision.compareTo(a, a - i * delta, 5)).isEqualTo(+1);
            }
        }

        assertThat(Precision.compareTo(-0.0, 0.0, 0)).isEqualTo(0);

        assertThat(Precision.compareTo(-Double.MIN_VALUE, -0.0, 0)).isEqualTo(-1);
        assertThat(Precision.compareTo(-Double.MIN_VALUE, -0.0, 1)).isEqualTo(0);
        assertThat(Precision.compareTo(-Double.MIN_VALUE, +0.0, 0)).isEqualTo(-1);
        assertThat(Precision.compareTo(-Double.MIN_VALUE, +0.0, 1)).isEqualTo(0);

        assertThat(Precision.compareTo(Double.MIN_VALUE, -0.0, 0)).isEqualTo(+1);
        assertThat(Precision.compareTo(Double.MIN_VALUE, -0.0, 1)).isEqualTo(0);
        assertThat(Precision.compareTo(Double.MIN_VALUE, +0.0, 0)).isEqualTo(+1);
        assertThat(Precision.compareTo(Double.MIN_VALUE, +0.0, 1)).isEqualTo(0);

        assertThat(Precision.compareTo(-Double.MIN_VALUE, Double.MIN_VALUE, 0)).isEqualTo(-1);
        assertThat(Precision.compareTo(-Double.MIN_VALUE, Double.MIN_VALUE, 1)).isEqualTo(-1);
        assertThat(Precision.compareTo(-Double.MIN_VALUE, Double.MIN_VALUE, 2)).isEqualTo(0);

        assertThat(Precision.compareTo(Double.MAX_VALUE, Double.POSITIVE_INFINITY, 1)).isEqualTo(0);
        assertThat(Precision.compareTo(Double.MAX_VALUE, Double.POSITIVE_INFINITY, 0))
            .isEqualTo(-1);

        assertThat(Precision.compareTo(Double.MAX_VALUE, Double.NaN, Integer.MAX_VALUE))
            .isEqualTo(+1);
        assertThat(Precision.compareTo(Double.NaN, Double.MAX_VALUE, Integer.MAX_VALUE))
            .isEqualTo(+1);
    }

    @Test
    public void testRoundDouble() {
        double x = 1.234567890;
        assertThat(Precision.round(x, 2)).isCloseTo(1.23, offset(0.0));
        assertThat(Precision.round(x, 3)).isCloseTo(1.235, offset(0.0));
        assertThat(Precision.round(x, 4)).isCloseTo(1.2346, offset(0.0));

        // JIRA MATH-151
        assertThat(Precision.round(39.245, 2)).isCloseTo(39.25, offset(0.0));
        assertThat(Precision.round(39.245, 2, RoundingMode.DOWN)).isCloseTo(39.24, offset(0.0));
        double xx = 39.0;
        xx += 245d / 1000d;
        assertThat(Precision.round(xx, 2)).isCloseTo(39.25, offset(0.0));

        // BZ 35904
        assertThat(Precision.round(30.095d, 2)).isCloseTo(30.1d, offset(0.0d));
        assertThat(Precision.round(30.095d, 1)).isCloseTo(30.1d, offset(0.0d));
        assertThat(Precision.round(33.095d, 1)).isCloseTo(33.1d, offset(0.0d));
        assertThat(Precision.round(33.095d, 2)).isCloseTo(33.1d, offset(0.0d));
        assertThat(Precision.round(50.085d, 2)).isCloseTo(50.09d, offset(0.0d));
        assertThat(Precision.round(50.185d, 2)).isCloseTo(50.19d, offset(0.0d));
        assertThat(Precision.round(50.005d, 2)).isCloseTo(50.01d, offset(0.0d));
        assertThat(Precision.round(30.005d, 2)).isCloseTo(30.01d, offset(0.0d));
        assertThat(Precision.round(30.645d, 2)).isCloseTo(30.65d, offset(0.0d));

        assertThat(Precision.round(x, 2, RoundingMode.CEILING)).isCloseTo(1.24, offset(0.0));
        assertThat(Precision.round(x, 3, RoundingMode.CEILING)).isCloseTo(1.235, offset(0.0));
        assertThat(Precision.round(x, 4, RoundingMode.CEILING)).isCloseTo(1.2346, offset(0.0));
        assertThat(Precision.round(-x, 2, RoundingMode.CEILING)).isCloseTo(-1.23, offset(0.0));
        assertThat(Precision.round(-x, 3, RoundingMode.CEILING)).isCloseTo(-1.234, offset(0.0));
        assertThat(Precision.round(-x, 4, RoundingMode.CEILING)).isCloseTo(-1.2345, offset(0.0));

        assertThat(Precision.round(x, 2, RoundingMode.DOWN)).isCloseTo(1.23, offset(0.0));
        assertThat(Precision.round(x, 3, RoundingMode.DOWN)).isCloseTo(1.234, offset(0.0));
        assertThat(Precision.round(x, 4, RoundingMode.DOWN)).isCloseTo(1.2345, offset(0.0));
        assertThat(Precision.round(-x, 2, RoundingMode.DOWN)).isCloseTo(-1.23, offset(0.0));
        assertThat(Precision.round(-x, 3, RoundingMode.DOWN)).isCloseTo(-1.234, offset(0.0));
        assertThat(Precision.round(-x, 4, RoundingMode.DOWN)).isCloseTo(-1.2345, offset(0.0));

        assertThat(Precision.round(x, 2, RoundingMode.FLOOR)).isCloseTo(1.23, offset(0.0));
        assertThat(Precision.round(x, 3, RoundingMode.FLOOR)).isCloseTo(1.234, offset(0.0));
        assertThat(Precision.round(x, 4, RoundingMode.FLOOR)).isCloseTo(1.2345, offset(0.0));
        assertThat(Precision.round(-x, 2, RoundingMode.FLOOR)).isCloseTo(-1.24, offset(0.0));
        assertThat(Precision.round(-x, 3, RoundingMode.FLOOR)).isCloseTo(-1.235, offset(0.0));
        assertThat(Precision.round(-x, 4, RoundingMode.FLOOR)).isCloseTo(-1.2346, offset(0.0));

        assertThat(Precision.round(x, 2, RoundingMode.HALF_DOWN)).isCloseTo(1.23, offset(0.0));
        assertThat(Precision.round(x, 3, RoundingMode.HALF_DOWN)).isCloseTo(1.235, offset(0.0));
        assertThat(Precision.round(x, 4, RoundingMode.HALF_DOWN)).isCloseTo(1.2346, offset(0.0));
        assertThat(Precision.round(-x, 2, RoundingMode.HALF_DOWN)).isCloseTo(-1.23, offset(0.0));
        assertThat(Precision.round(-x, 3, RoundingMode.HALF_DOWN)).isCloseTo(-1.235, offset(0.0));
        assertThat(Precision.round(-x, 4, RoundingMode.HALF_DOWN)).isCloseTo(-1.2346, offset(0.0));
        assertThat(Precision.round(1.2345, 3, RoundingMode.HALF_DOWN))
            .isCloseTo(1.234, offset(0.0));
        assertThat(Precision.round(-1.2345, 3, RoundingMode.HALF_DOWN))
            .isCloseTo(-1.234, offset(0.0));

        assertThat(Precision.round(x, 2, RoundingMode.HALF_EVEN)).isCloseTo(1.23, offset(0.0));
        assertThat(Precision.round(x, 3, RoundingMode.HALF_EVEN)).isCloseTo(1.235, offset(0.0));
        assertThat(Precision.round(x, 4, RoundingMode.HALF_EVEN)).isCloseTo(1.2346, offset(0.0));
        assertThat(Precision.round(-x, 2, RoundingMode.HALF_EVEN)).isCloseTo(-1.23, offset(0.0));
        assertThat(Precision.round(-x, 3, RoundingMode.HALF_EVEN)).isCloseTo(-1.235, offset(0.0));
        assertThat(Precision.round(-x, 4, RoundingMode.HALF_EVEN)).isCloseTo(-1.2346, offset(0.0));
        assertThat(Precision.round(1.2345, 3, RoundingMode.HALF_EVEN))
            .isCloseTo(1.234, offset(0.0));
        assertThat(Precision.round(-1.2345, 3, RoundingMode.HALF_EVEN))
            .isCloseTo(-1.234, offset(0.0));
        assertThat(Precision.round(1.2355, 3, RoundingMode.HALF_EVEN))
            .isCloseTo(1.236, offset(0.0));
        assertThat(Precision.round(-1.2355, 3, RoundingMode.HALF_EVEN))
            .isCloseTo(-1.236, offset(0.0));

        assertThat(Precision.round(x, 2, RoundingMode.HALF_UP)).isCloseTo(1.23, offset(0.0));
        assertThat(Precision.round(x, 3, RoundingMode.HALF_UP)).isCloseTo(1.235, offset(0.0));
        assertThat(Precision.round(x, 4, RoundingMode.HALF_UP)).isCloseTo(1.2346, offset(0.0));
        assertThat(Precision.round(-x, 2, RoundingMode.HALF_UP)).isCloseTo(-1.23, offset(0.0));
        assertThat(Precision.round(-x, 3, RoundingMode.HALF_UP)).isCloseTo(-1.235, offset(0.0));
        assertThat(Precision.round(-x, 4, RoundingMode.HALF_UP)).isCloseTo(-1.2346, offset(0.0));
        assertThat(Precision.round(1.2345, 3, RoundingMode.HALF_UP)).isCloseTo(1.235, offset(0.0));
        assertThat(Precision.round(-1.2345, 3, RoundingMode.HALF_UP))
            .isCloseTo(-1.235, offset(0.0));

        assertThat(Precision.round(-1.23, 2, RoundingMode.UNNECESSARY))
            .isCloseTo(-1.23, offset(0.0));
        assertThat(Precision.round(1.23, 2, RoundingMode.UNNECESSARY)).isCloseTo(1.23, offset(0.0));

        try {
            Precision.round(1.234, 2, RoundingMode.UNNECESSARY);
            fail("Unknown");
        } catch (ArithmeticException ex) {
            // expected
        }

        assertThat(Precision.round(x, 2, RoundingMode.UP)).isCloseTo(1.24, offset(0.0));
        assertThat(Precision.round(x, 3, RoundingMode.UP)).isCloseTo(1.235, offset(0.0));
        assertThat(Precision.round(x, 4, RoundingMode.UP)).isCloseTo(1.2346, offset(0.0));
        assertThat(Precision.round(-x, 2, RoundingMode.UP)).isCloseTo(-1.24, offset(0.0));
        assertThat(Precision.round(-x, 3, RoundingMode.UP)).isCloseTo(-1.235, offset(0.0));
        assertThat(Precision.round(-x, 4, RoundingMode.UP)).isCloseTo(-1.2346, offset(0.0));

        // MATH-151
        assertThat(Precision.round(39.245, 2, RoundingMode.HALF_UP)).isCloseTo(39.25, offset(0.0));

        // special values
//        TestUtils.assertEquals(Double.NaN, Precision.round(Double.NaN, 2), 0.0);
        assertThat(Precision.round(Double.NaN, 2)).isNaN();

        assertThat(Precision.round(0.0, 2)).isCloseTo(0.0, offset(0.0));
        assertThat(Precision.round(Double.POSITIVE_INFINITY, 2))
            .isCloseTo(Double.POSITIVE_INFINITY, offset(0.0));
        assertThat(Precision.round(Double.NEGATIVE_INFINITY, 2))
            .isCloseTo(Double.NEGATIVE_INFINITY, offset(0.0));
        // comparison of positive and negative zero is not possible -> always equal thus do string comparison
        assertThat(Double.toString(Precision.round(-0.0, 0))).isEqualTo("-0.0");
        assertThat(Double.toString(Precision.round(-1e-10, 0))).isEqualTo("-0.0");
    }


    @Test
    public void testIssue721() {
        assertThat(Math.getExponent(Precision.EPSILON)).isEqualTo(-53);
        assertThat(Math.getExponent(Precision.SAFE_MIN)).isEqualTo(-1022);
    }


    @Test
    public void testRepresentableDelta() {
        int nonRepresentableCount = 0;
        final double x = 100;
        final int numTrials = 10000;
        for (int i = 0; i < numTrials; i++) {
            final double originalDelta = Math.random();
            final double delta = Precision.representableDelta(x, originalDelta);
            if (delta != originalDelta) {
                ++nonRepresentableCount;
            }
        }

        assertThat(nonRepresentableCount / (double) numTrials > 0.9).isTrue();
    }

    @Test
    public void testMath843() {
        final double afterEpsilon = Math.nextAfter(Precision.EPSILON,
                                                   Double.POSITIVE_INFINITY);

        // a) 1 + EPSILON is equal to 1.
        assertThat(1 + Precision.EPSILON == 1).isTrue();

        // b) 1 + "the number after EPSILON" is not equal to 1.
        assertThat(1 + afterEpsilon == 1).isFalse();
    }

    @Test
    public void testMath1127() {
        assertThat(Precision.equals(2.0, -2.0, 1)).isFalse();
        assertThat(Precision.equals(0.0, -0.0, 0)).isTrue();
        assertThat(Precision.equals(2.0f, -2.0f, 1)).isFalse();
        assertThat(Precision.equals(0.0f, -0.0f, 0)).isTrue();
    }
}
