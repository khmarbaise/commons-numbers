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
package org.apache.commons.numbers.combinatorics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.data.Offset.offset;

import org.junit.Test;

/**
 * Test cases for the {@link FactorialDouble} class.
 */
public class FactorialDoubleTest {
    @Test
    public void testFactorialZero() {
        assertThat(FactorialDouble.create().value(0)).as("0!").isCloseTo(1, offset(0d));
    }

    @Test
    public void testFactorialDirect() {
        for (int i = 1; i < 21; i++) {
            assertThat(FactorialDouble.create().value(i)).as(i + "!")
                .isCloseTo(factorialDirect(i), offset(0d));
        }
    }
    
    @Test
    public void testLargestFactorialDouble() {
        final int n = 170;
        assertThat(Double.POSITIVE_INFINITY != FactorialDouble.create().value(n)).as(n + "!")
            .isTrue();
    }

    @Test
    public void testFactorialDoubleTooLarge() {
        final int n = 171;
        assertThat(FactorialDouble.create().value(n)).as(n + "!")
            .isCloseTo(Double.POSITIVE_INFINITY, offset(0d));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNonPositiveArgument() {
        FactorialDouble.create().value(-1);
    }

    @Test
    public void testCompareDirectWithoutCache() {
        // This test shows that delegating to the "Gamma" class will also lead to a
        // less accurate result.

        final int max = 100;
        final FactorialDouble f = FactorialDouble.create();

        for (int i = 0; i < max; i++) {
            final double expected = factorialDirect(i);
            assertThat(f.value(i)).as(i + "! ")
                .isCloseTo(expected, offset(100 * Math.ulp(expected)));
        }
    }

    @Test
    public void testCompareDirectWithCache() {
        final int max = 100;
        final FactorialDouble f = FactorialDouble.create().withCache(max);

        for (int i = 0; i < max; i++) {
            final double expected = factorialDirect(i);
            assertThat(f.value(i)).as(i + "! ")
                .isCloseTo(expected, offset(100 * Math.ulp(expected)));
        }
    }

    @Test
    public void testCacheIncrease() {
        final int max = 100;
        final FactorialDouble f1 = FactorialDouble.create().withCache(max);
        final FactorialDouble f2 = f1.withCache(2 * max);

        final int val = max + max / 2;
        assertThat(f2.value(val)).isCloseTo(f1.value(val), offset(0d));
    }

    @Test
    public void testZeroCache() {
        // Ensure that no exception is thrown.
        final FactorialDouble f = FactorialDouble.create().withCache(0);
        assertThat(f.value(0)).isCloseTo(1, offset(0d));
        assertThat(f.value(1)).isCloseTo(1, offset(0d));
    }

    @Test
    public void testUselessCache() {
        // Ensure that no exception is thrown.
        LogFactorial.create().withCache(1);
        assertThatCode(() -> LogFactorial.create().withCache(1)).doesNotThrowAnyException();

        LogFactorial.create().withCache(2);
        assertThatCode(() -> LogFactorial.create().withCache(2)).doesNotThrowAnyException();
    }

    @Test
    public void testCacheDecrease() {
        final int max = 100;
        final FactorialDouble f1 = FactorialDouble.create().withCache(max);
        final FactorialDouble f2 = f1.withCache(max / 2);

        final int val = max / 4;
        assertThat(f2.value(val)).isCloseTo(f1.value(val), offset(0d));
    }

    /**
     * Direct multiplication implementation.
     */
    private double factorialDirect(int n) {
        double result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}
