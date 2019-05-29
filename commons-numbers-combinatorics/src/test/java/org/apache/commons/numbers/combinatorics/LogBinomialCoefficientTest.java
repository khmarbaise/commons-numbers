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
import static org.assertj.core.data.Offset.offset;

import org.junit.Test;

/**
 * Test cases for the {@link LogBinomialCoefficient} class.
 */
public class LogBinomialCoefficientTest {
    /** Verify that b(0,0) = 1 */
    @Test
    public void test0Choose0() {
        assertThat(0d).isCloseTo(LogBinomialCoefficient.value(0, 0), offset(0));
    }

    @Test
    public void testBinomialCoefficient() {
        final long[] bcoef5 = { 1, 5, 10, 10, 5, 1 };
        final long[] bcoef6 = { 1, 6, 15, 20, 15, 6, 1 };

        for (int n = 1; n < 10; n++) {
            for (int k = 0; k <= n; k++) {
                assertThat(LogBinomialCoefficient.value(n, k)).as(n + " choose " + k)
                    .isCloseTo(Math.log(BinomialCoefficientTest.binomialCoefficient(n, k)),
                        offset(1e-12));
            }
        }

        final int[] n = { 34, 66, 100, 1500, 1500 };
        final int[] k = { 17, 33, 10, 1500 - 4, 4 };
        for (int i = 0; i < n.length; i++) {
            final long expected = BinomialCoefficientTest.binomialCoefficient(n[i], k[i]);
            assertThat(LogBinomialCoefficient.value(n[i], k[i]))
                .as("log(" + n[i] + " choose " + k[i] + ")")
                .isCloseTo(Math.log(expected), offset(0d));
        }
    }

    @Test(expected=CombinatoricsException.class)
    public void testBinomialCoefficientFail1() {
        LogBinomialCoefficient.value(4, 5);
    }

    @Test(expected=CombinatoricsException.class)
    public void testBinomialCoefficientFail2() {
        LogBinomialCoefficient.value(-1, -2);
    }

    /**
     * Tests correctness for large n and sharpness of upper bound in API doc
     * JIRA: MATH-241
     */
    @Test
    public void testBinomialCoefficientLarge() throws Exception {
        // This tests all legal and illegal values for n <= 200.
        for (int n = 0; n <= 200; n++) {
            for (int k = 0; k <= n; k++) {
                long exactResult = -1;
                boolean shouldThrow = false;
                boolean didThrow = false;
                try {
                    BinomialCoefficient.value(n, k);
                } catch (ArithmeticException ex) {
                    didThrow = true;
                }
                try {
                    exactResult = BinomialCoefficientTest.binomialCoefficient(n, k);
                } catch (ArithmeticException ex) {
                    shouldThrow = true;
                }

                if (!shouldThrow && exactResult > 1) {
                    assertThat(LogBinomialCoefficient.value(n, k) / Math.log(exactResult))
                        .as(n + " choose " + k).isCloseTo(1, offset(1e-10));
                }
            }
        }

        final int n = 10000;
        final double actualOverExpected = LogBinomialCoefficient.value(n, 3) /
            Math.log(BinomialCoefficientTest.binomialCoefficient(n, 3));
        assertThat(actualOverExpected).isCloseTo(1, offset(1e-10));
    }
}
