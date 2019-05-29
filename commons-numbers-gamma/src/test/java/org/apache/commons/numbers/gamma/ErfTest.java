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
package org.apache.commons.numbers.gamma;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

import org.junit.Test;

/**
 * Tests for {@link Erf}.
 */
public class ErfTest {
    @Test
    public void testErf0() {
        double actual = Erf.value(0);
        double expected = 0;
        assertThat(actual).isCloseTo(expected, offset(1e-15));
        assertThat(Erfc.value(0)).isCloseTo(1 - expected, offset(1e-15));
    }

    @Test
    public void testErf1960() {
        double x = 1.960 / Math.sqrt(2);
        double actual = Erf.value(x);
        double expected = 0.95;
        assertThat(actual).isCloseTo(expected, offset(1e-5));
        assertThat(Erfc.value(x)).isCloseTo(1 - actual, offset(1e-15));

        actual = Erf.value(-x);
        expected = -expected;
        assertThat(actual).isCloseTo(expected, offset(1e-5));
        assertThat(Erfc.value(-x)).isCloseTo(1 - actual, offset(1e-15));
    }

    @Test
    public void testErf2576() {
        double x = 2.576 / Math.sqrt(2);
        double actual = Erf.value(x);
        double expected = 0.99;
        assertThat(actual).isCloseTo(expected, offset(1e-5));
        assertThat(Erfc.value(x)).isCloseTo(1 - actual, offset(1e-15));

        actual = Erf.value(-x);
        expected = -expected;
        assertThat(actual).isCloseTo(expected, offset(1e-5));
        assertThat(Erfc.value(-x)).isCloseTo(1 - actual, offset(1e-15));
    }

    @Test
    public void testErf2807() {
        double x = 2.807 / Math.sqrt(2);
        double actual = Erf.value(x);
        double expected = 0.995;
        assertThat(actual).isCloseTo(expected, offset(1e-5));
        assertThat(Erfc.value(x)).isCloseTo(1 - actual, offset(1e-15));

        actual = Erf.value(-x);
        expected = -expected;
        assertThat(actual).isCloseTo(expected, offset(1e-5));
        assertThat(Erfc.value(-x)).isCloseTo(1 - actual, offset(1e-15));
    }

    @Test
    public void testErf3291() {
        double x = 3.291 / Math.sqrt(2);
        double actual = Erf.value(x);
        double expected = 0.999;
        assertThat(actual).isCloseTo(expected, offset(1e-5));
        assertThat(Erfc.value(x)).isCloseTo(1 - expected, offset(1e-5));

        actual = Erf.value(-x);
        expected = -expected;
        assertThat(actual).isCloseTo(expected, offset(1e-5));
        assertThat(Erfc.value(-x)).isCloseTo(1 - expected, offset(1e-5));
    }

    /**
     * MATH-301, MATH-456
     */
    @Test
    public void testLargeValues() {
        for (int i = 1; i < 200; i *= 10) {
            double result = Erf.value(i);
            assertThat(Double.isNaN(result)).isFalse();
            assertThat(result > 0 && result <= 1).isTrue();
            result = Erf.value(-i);
            assertThat(Double.isNaN(result)).isFalse();
            assertThat(result >= -1 && result < 0).isTrue();
            result = Erfc.value(i);
            assertThat(Double.isNaN(result)).isFalse();
            assertThat(result >= 0 && result < 1).isTrue();
            result = Erfc.value(-i);
            assertThat(Double.isNaN(result)).isFalse();
            assertThat(result >= 1 && result <= 2).isTrue();
        }
        assertThat(Erf.value(Double.NEGATIVE_INFINITY)).isCloseTo(-1, offset(0));
        assertThat(Erf.value(Double.POSITIVE_INFINITY)).isCloseTo(1, offset(0));
        assertThat(Erfc.value(Double.NEGATIVE_INFINITY)).isCloseTo(2, offset(0));
        assertThat(Erfc.value(Double.POSITIVE_INFINITY)).isCloseTo(0, offset(0));
    }

    /**
     * Compare Erf.value against reference values computed using GCC 4.2.1
     * (Apple OSX packaged version) erfl (extended precision erf).
     */
    @Test
    public void testErfGnu() {
        final double tol = 1E-15;
        final double[] gnuValues = new double[] {
            -1, -1, -1, -1, -1,
            -1, -1, -1, -0.99999999999999997848,
            -0.99999999999999264217, -0.99999999999846254017, -0.99999999980338395581, -0.99999998458274209971,
            -0.9999992569016276586, -0.99997790950300141459, -0.99959304798255504108, -0.99532226501895273415,
            -0.96610514647531072711, -0.84270079294971486948, -0.52049987781304653809,  0,
            0.52049987781304653809, 0.84270079294971486948, 0.96610514647531072711, 0.99532226501895273415,
            0.99959304798255504108, 0.99997790950300141459, 0.9999992569016276586, 0.99999998458274209971,
            0.99999999980338395581, 0.99999999999846254017, 0.99999999999999264217, 0.99999999999999997848,
            1,  1,  1,  1,
            1,  1,  1,  1};
        
        double x = -10;
        for (int i = 0; i < 41; i++) {
            assertThat(Erf.value(x)).isCloseTo(gnuValues[i], offset(tol));
            x += 0.5d;
        }
    }
}
