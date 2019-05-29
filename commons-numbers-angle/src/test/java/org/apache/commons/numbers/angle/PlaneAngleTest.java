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
package org.apache.commons.numbers.angle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

import org.junit.Test;

/**
 * Test cases for the {@link PlaneAngle} class.
 */
public class PlaneAngleTest {
    @Test
    public void testConversionTurns() {
        final double value = 12.3456;
        final PlaneAngle a = PlaneAngle.ofTurns(value);
        assertThat(a.toTurns()).isCloseTo(value, offset(0d));
    }

    @Test
    public void testConversionRadians() {
        final double one = 2 * Math.PI;
        final double value = 12.3456 * one;
        final PlaneAngle a = PlaneAngle.ofRadians(value);
        assertThat(a.toRadians()).isCloseTo(value, offset(0d));
    }

    @Test
    public void testConversionDegrees() {
        final double one = 360;
        final double value = 12.3456 * one;
        final PlaneAngle a = PlaneAngle.ofDegrees(value);
        assertThat(a.toDegrees()).isCloseTo(value, offset(0d));
    }

    @Test
    public void testNormalizeRadians() {
        for (double a = -15.0; a <= 15.0; a += 0.1) {
            for (double b = -15.0; b <= 15.0; b += 0.2) {
                final PlaneAngle aA = PlaneAngle.ofRadians(a);
                final PlaneAngle aB = PlaneAngle.ofRadians(b);
                final double c = aA.normalize(aB).toRadians();
                assertThat((b - Math.PI) <= c).isTrue();
                assertThat(c <= (b + Math.PI)).isTrue();
                double twoK = Math.rint((a - c) / Math.PI);
                assertThat(a - twoK * Math.PI).isCloseTo(c, offset(1e-14));
            }
        }
    }

    @Test
    public void testNormalizeMixed() {
        for (double a = -15.0; a <= 15.0; a += 0.1) {
            for (double b = -15.0; b <= 15.0; b += 0.2) {
                final PlaneAngle aA = PlaneAngle.ofDegrees(a);
                final PlaneAngle aB = PlaneAngle.ofRadians(b);
                final double c = aA.normalize(aB).toTurns();
                assertThat((aB.toTurns() - 0.5) <= c).isTrue();
                assertThat(c <= (aB.toTurns() + 0.5)).isTrue();
                double twoK = Math.rint((aA.toTurns() - c));
                assertThat(aA.toTurns() - twoK).isCloseTo(c, offset(1e-14));
            }
        }
    }

    @Test
    public void testNormalizeAroundZero1() {
        final double value = 1.25;
        final double expected = 0.25;
        final double actual = PlaneAngle.ofTurns(value).normalize(PlaneAngle.ZERO).toTurns();
        final double tol = Math.ulp(expected);
        assertThat(actual).isCloseTo(expected, offset(tol));
    }
    @Test
    public void testNormalizeAroundZero2() {
        final double value = 0.75;
        final double expected = -0.25;
        final double actual = PlaneAngle.ofTurns(value).normalize(PlaneAngle.ZERO).toTurns();
        final double tol = Math.ulp(expected);
        assertThat(actual).isCloseTo(expected, offset(tol));
    }
    @Test
    public void testNormalizeAroundZero3() {
        final double value = 0.5 + 1e-10;
        final double expected = -0.5 + 1e-10;
        final double actual = PlaneAngle.ofTurns(value).normalize(PlaneAngle.ZERO).toTurns();
        final double tol = Math.ulp(expected);
        assertThat(actual).isCloseTo(expected, offset(tol));
    }
    @Test
    public void testNormalizeAroundZero4() {
        final double value = 5 * Math.PI / 4;
        final double expected = Math.PI * (1d / 4 - 1);
        final double actual = PlaneAngle.ofRadians(value).normalize(PlaneAngle.ZERO).toRadians();
        final double tol = Math.ulp(expected);
        assertThat(actual).isCloseTo(expected, offset(tol));
    }

    @Test
    public void testNormalizeUpperAndLowerBounds() {
        // arrange
        double eps = 1e-15;

        // act/assert
        assertThat(PlaneAngle.ofTurns(-0.5).normalize(PlaneAngle.ZERO).toTurns())
            .isCloseTo(-0.5, offset(eps));
        assertThat(PlaneAngle.ofTurns(0.5).normalize(PlaneAngle.ZERO).toTurns())
            .isCloseTo(-0.5, offset(eps));

        assertThat(PlaneAngle.ofTurns(-1.5).normalize(PlaneAngle.ZERO).toTurns())
            .isCloseTo(-0.5, offset(eps));
        assertThat(PlaneAngle.ofTurns(1.5).normalize(PlaneAngle.ZERO).toTurns())
            .isCloseTo(-0.5, offset(eps));

        assertThat(PlaneAngle.ofTurns(0).normalize(PlaneAngle.PI).toTurns())
            .isCloseTo(0.0, offset(eps));
        assertThat(PlaneAngle.ofTurns(1).normalize(PlaneAngle.PI).toTurns())
            .isCloseTo(0.0, offset(eps));

        assertThat(PlaneAngle.ofTurns(-1).normalize(PlaneAngle.PI).toTurns())
            .isCloseTo(0.0, offset(eps));
        assertThat(PlaneAngle.ofTurns(2).normalize(PlaneAngle.PI).toTurns())
            .isCloseTo(0.0, offset(eps));
    }

    @Test
    public void testNormalizeVeryCloseToBounds() {
        // arrange
        double eps = 1e-22;

        double small = 1e-16;
        double tiny = 1e-18; // 0.5 + tiny = 0.5 (the value is too small to add to 0.5)

        // act/assert
        assertThat(PlaneAngle.ofTurns(-small).normalize(PlaneAngle.PI).toTurns())
            .isCloseTo(1.0 - small, offset(eps));
        assertThat(PlaneAngle.ofTurns(small).normalize(PlaneAngle.PI).toTurns())
            .isCloseTo(small, offset(eps));

        assertThat(PlaneAngle.ofTurns(-0.5 - small).normalize(PlaneAngle.ZERO).toTurns())
            .isCloseTo(0.5 - small, offset(eps));
        assertThat(PlaneAngle.ofTurns(0.5 + small).normalize(PlaneAngle.ZERO).toTurns())
            .isCloseTo(-0.5 + small, offset(eps));

        assertThat(PlaneAngle.ofTurns(-tiny).normalize(PlaneAngle.PI).toTurns())
            .isCloseTo(0.0, offset(eps));
        assertThat(PlaneAngle.ofTurns(tiny).normalize(PlaneAngle.PI).toTurns())
            .isCloseTo(tiny, offset(eps));

        assertThat(PlaneAngle.ofTurns(-0.5 - tiny).normalize(PlaneAngle.ZERO).toTurns())
            .isCloseTo(-0.5, offset(eps));
        assertThat(PlaneAngle.ofTurns(0.5 + tiny).normalize(PlaneAngle.ZERO).toTurns())
            .isCloseTo(-0.5, offset(eps));
    }

    @Test
    public void testHashCode() {
        // Test assumes that the internal representation is in "turns".
        final double value = -123.456789;
        final int expected = Double.valueOf(value).hashCode();
        final int actual = PlaneAngle.ofTurns(value).hashCode();
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    public void testEquals1() {
        final double value = 12345.6789;
        final PlaneAngle a = PlaneAngle.ofRadians(value);
        final PlaneAngle b = PlaneAngle.ofRadians(value);
        assertThat(a.equals(b)).isTrue();
    }
    @Test
    public void testEquals2() {
        final PlaneAngle a = PlaneAngle.ofRadians(153768.373486587);
        final PlaneAngle b = null;
        assertThat(a.equals(b)).isFalse();
    }
    @Test
    public void testEquals3() {
        final double value = 0.987654321;
        final PlaneAngle a = PlaneAngle.ofRadians(value);
        final PlaneAngle b = PlaneAngle.ofRadians(value + 1e-16);
        assertThat(a.equals(b)).isFalse();
    }

    @Test
    public void testZero() {
        assertThat(PlaneAngle.ZERO.toRadians()).isCloseTo(0, offset(0d));
    }
    @Test
    public void testPi() {
        assertThat(PlaneAngle.PI.toRadians()).isCloseTo(Math.PI, offset(0d));
    }
}
