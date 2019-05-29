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
package org.apache.commons.numbers.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Arrays;
import java.math.BigInteger;
import java.util.Collections;

import org.junit.Test;

/**
 * Test cases for the {@link ArithmeticUtils} class.
 *
 */
public class ArithmeticUtilsTest {

    @Test
    public void testAddAndCheck() {
        int big = Integer.MAX_VALUE;
        int bigNeg = Integer.MIN_VALUE;
        assertThat(ArithmeticUtils.addAndCheck(big, 0)).isEqualTo(big);
        try {
            ArithmeticUtils.addAndCheck(big, 1);
            fail("Expecting ArithmeticException");
        } catch (ArithmeticException ex) {
        }
        try {
            ArithmeticUtils.addAndCheck(bigNeg, -1);
            fail("Expecting ArithmeticException");
        } catch (ArithmeticException ex) {
        }
    }

    @Test
    public void testAddAndCheckLong() {
        long max = Long.MAX_VALUE;
        long min = Long.MIN_VALUE;
        assertThat(ArithmeticUtils.addAndCheck(max, 0L)).isEqualTo(max);
        assertThat(ArithmeticUtils.addAndCheck(min, 0L)).isEqualTo(min);
        assertThat(ArithmeticUtils.addAndCheck(0L, max)).isEqualTo(max);
        assertThat(ArithmeticUtils.addAndCheck(0L, min)).isEqualTo(min);
        assertThat(ArithmeticUtils.addAndCheck(-1L, 2L)).isEqualTo(1);
        assertThat(ArithmeticUtils.addAndCheck(2L, -1L)).isEqualTo(1);
        assertThat(ArithmeticUtils.addAndCheck(-2L, -1L)).isEqualTo(-3);
        assertThat(ArithmeticUtils.addAndCheck(min + 1, -1L)).isEqualTo(min);
        assertThat(ArithmeticUtils.addAndCheck(min, max)).isEqualTo(-1);
        testAddAndCheckLongFailure(max, 1L);
        testAddAndCheckLongFailure(min, -1L);
        testAddAndCheckLongFailure(1L, max);
        testAddAndCheckLongFailure(-1L, min);
        testAddAndCheckLongFailure(max, max);
        testAddAndCheckLongFailure(min, min);
    }

    @Test
    public void testGcd() {
        int a = 30;
        int b = 50;
        int c = 77;

        assertThat(ArithmeticUtils.gcd(0, 0)).isEqualTo(0);

        assertThat(ArithmeticUtils.gcd(0, b)).isEqualTo(b);
        assertThat(ArithmeticUtils.gcd(a, 0)).isEqualTo(a);
        assertThat(ArithmeticUtils.gcd(0, -b)).isEqualTo(b);
        assertThat(ArithmeticUtils.gcd(-a, 0)).isEqualTo(a);

        assertThat(ArithmeticUtils.gcd(a, b)).isEqualTo(10);
        assertThat(ArithmeticUtils.gcd(-a, b)).isEqualTo(10);
        assertThat(ArithmeticUtils.gcd(a, -b)).isEqualTo(10);
        assertThat(ArithmeticUtils.gcd(-a, -b)).isEqualTo(10);

        assertThat(ArithmeticUtils.gcd(a, c)).isEqualTo(1);
        assertThat(ArithmeticUtils.gcd(-a, c)).isEqualTo(1);
        assertThat(ArithmeticUtils.gcd(a, -c)).isEqualTo(1);
        assertThat(ArithmeticUtils.gcd(-a, -c)).isEqualTo(1);

        assertThat(ArithmeticUtils.gcd(3 * (1 << 20), 9 * (1 << 15))).isEqualTo(3 * (1 << 15));

        assertThat(ArithmeticUtils.gcd(Integer.MAX_VALUE, 0)).isEqualTo(Integer.MAX_VALUE);
        assertThat(ArithmeticUtils.gcd(-Integer.MAX_VALUE, 0)).isEqualTo(Integer.MAX_VALUE);
        assertThat(ArithmeticUtils.gcd(1 << 30, -Integer.MIN_VALUE)).isEqualTo(1 << 30);
        try {
            // gcd(Integer.MIN_VALUE, 0) > Integer.MAX_VALUE
            ArithmeticUtils.gcd(Integer.MIN_VALUE, 0);
            fail("expecting ArithmeticException");
        } catch (ArithmeticException expected) {
            // expected
        }
        try {
            // gcd(0, Integer.MIN_VALUE) > Integer.MAX_VALUE
            ArithmeticUtils.gcd(0, Integer.MIN_VALUE);
            fail("expecting ArithmeticException");
        } catch (ArithmeticException expected) {
            // expected
        }
        try {
            // gcd(Integer.MIN_VALUE, Integer.MIN_VALUE) > Integer.MAX_VALUE
            ArithmeticUtils.gcd(Integer.MIN_VALUE, Integer.MIN_VALUE);
            fail("expecting ArithmeticException");
        } catch (ArithmeticException expected) {
            // expected
        }
    }

    @Test
    public void testGcdConsistency() {
        // Use Integer to prevent varargs vs array issue with Arrays.asList
        Integer[] primeList = {19, 23, 53, 67, 73, 79, 101, 103, 111, 131};

        for (int i = 0; i < 20; i++) {
            Collections.shuffle(Arrays.asList(primeList));
            int p1 = primeList[0];
            int p2 = primeList[1];
            int p3 = primeList[2];
            int p4 = primeList[3];
            int i1 = p1 * p2 * p3;
            int i2 = p1 * p2 * p4;
            int gcd = p1 * p2;
            assertThat(ArithmeticUtils.gcd(i1, i2)).isEqualTo(gcd);
            long l1 = i1;
            long l2 = i2;
            assertThat(ArithmeticUtils.gcd(l1, l2)).isEqualTo(gcd);
        }
    }

    @Test
    public void  testGcdLong(){
        long a = 30;
        long b = 50;
        long c = 77;

        assertThat(ArithmeticUtils.gcd(0L, 0)).isEqualTo(0);

        assertThat(ArithmeticUtils.gcd(0, b)).isEqualTo(b);
        assertThat(ArithmeticUtils.gcd(a, 0)).isEqualTo(a);
        assertThat(ArithmeticUtils.gcd(0, -b)).isEqualTo(b);
        assertThat(ArithmeticUtils.gcd(-a, 0)).isEqualTo(a);

        assertThat(ArithmeticUtils.gcd(a, b)).isEqualTo(10);
        assertThat(ArithmeticUtils.gcd(-a, b)).isEqualTo(10);
        assertThat(ArithmeticUtils.gcd(a, -b)).isEqualTo(10);
        assertThat(ArithmeticUtils.gcd(-a, -b)).isEqualTo(10);

        assertThat(ArithmeticUtils.gcd(a, c)).isEqualTo(1);
        assertThat(ArithmeticUtils.gcd(-a, c)).isEqualTo(1);
        assertThat(ArithmeticUtils.gcd(a, -c)).isEqualTo(1);
        assertThat(ArithmeticUtils.gcd(-a, -c)).isEqualTo(1);

        assertThat(ArithmeticUtils.gcd(3L * (1L << 50), 9L * (1L << 45)))
            .isEqualTo(3L * (1L << 45));

        assertThat(ArithmeticUtils.gcd(1L << 45, Long.MIN_VALUE)).isEqualTo(1L << 45);

        assertThat(ArithmeticUtils.gcd(Long.MAX_VALUE, 0L)).isEqualTo(Long.MAX_VALUE);
        assertThat(ArithmeticUtils.gcd(-Long.MAX_VALUE, 0L)).isEqualTo(Long.MAX_VALUE);
        assertThat(ArithmeticUtils.gcd(60247241209L, 153092023L)).isEqualTo(1);
        try {
            // gcd(Long.MIN_VALUE, 0) > Long.MAX_VALUE
            ArithmeticUtils.gcd(Long.MIN_VALUE, 0);
            fail("expecting ArithmeticException");
        } catch (ArithmeticException expected) {
            // expected
        }
        try {
            // gcd(0, Long.MIN_VALUE) > Long.MAX_VALUE
            ArithmeticUtils.gcd(0, Long.MIN_VALUE);
            fail("expecting ArithmeticException");
        } catch (ArithmeticException expected) {
            // expected
        }
        try {
            // gcd(Long.MIN_VALUE, Long.MIN_VALUE) > Long.MAX_VALUE
            ArithmeticUtils.gcd(Long.MIN_VALUE, Long.MIN_VALUE);
            fail("expecting ArithmeticException");
        } catch (ArithmeticException expected) {
            // expected
        }
    }


    @Test
    public void testLcm() {
        int a = 30;
        int b = 50;
        int c = 77;

        assertThat(ArithmeticUtils.lcm(0, b)).isEqualTo(0);
        assertThat(ArithmeticUtils.lcm(a, 0)).isEqualTo(0);
        assertThat(ArithmeticUtils.lcm(1, b)).isEqualTo(b);
        assertThat(ArithmeticUtils.lcm(a, 1)).isEqualTo(a);
        assertThat(ArithmeticUtils.lcm(a, b)).isEqualTo(150);
        assertThat(ArithmeticUtils.lcm(-a, b)).isEqualTo(150);
        assertThat(ArithmeticUtils.lcm(a, -b)).isEqualTo(150);
        assertThat(ArithmeticUtils.lcm(-a, -b)).isEqualTo(150);
        assertThat(ArithmeticUtils.lcm(a, c)).isEqualTo(2310);

        // Assert that no intermediate value overflows:
        // The naive implementation of lcm(a,b) would be (a*b)/gcd(a,b)
        assertThat(ArithmeticUtils.lcm((1 << 20) * 3, (1 << 20) * 5)).isEqualTo((1 << 20) * 15);

        // Special case
        assertThat(ArithmeticUtils.lcm(0, 0)).isEqualTo(0);

        try {
            // lcm == abs(MIN_VALUE) cannot be represented as a nonnegative int
            ArithmeticUtils.lcm(Integer.MIN_VALUE, 1);
            fail("Expecting ArithmeticException");
        } catch (ArithmeticException expected) {
            // expected
        }

        try {
            // lcm == abs(MIN_VALUE) cannot be represented as a nonnegative int
            ArithmeticUtils.lcm(Integer.MIN_VALUE, 1<<20);
            fail("Expecting ArithmeticException");
        } catch (ArithmeticException expected) {
            // expected
        }

        try {
            ArithmeticUtils.lcm(Integer.MAX_VALUE, Integer.MAX_VALUE - 1);
            fail("Expecting ArithmeticException");
        } catch (ArithmeticException expected) {
            // expected
        }
    }

    @Test
    public void testLcmLong() {
        long a = 30;
        long b = 50;
        long c = 77;

        assertThat(ArithmeticUtils.lcm(0, b)).isEqualTo(0);
        assertThat(ArithmeticUtils.lcm(a, 0)).isEqualTo(0);
        assertThat(ArithmeticUtils.lcm(1, b)).isEqualTo(b);
        assertThat(ArithmeticUtils.lcm(a, 1)).isEqualTo(a);
        assertThat(ArithmeticUtils.lcm(a, b)).isEqualTo(150);
        assertThat(ArithmeticUtils.lcm(-a, b)).isEqualTo(150);
        assertThat(ArithmeticUtils.lcm(a, -b)).isEqualTo(150);
        assertThat(ArithmeticUtils.lcm(-a, -b)).isEqualTo(150);
        assertThat(ArithmeticUtils.lcm(a, c)).isEqualTo(2310);

        assertThat(ArithmeticUtils.lcm(60247241209L, 153092023L)).isEqualTo(Long.MAX_VALUE);

        // Assert that no intermediate value overflows:
        // The naive implementation of lcm(a,b) would be (a*b)/gcd(a,b)
        assertThat(ArithmeticUtils.lcm((1L << 45) * 3, (1L << 50) * 5)).isEqualTo((1L << 50) * 15);

        // Special case
        assertThat(ArithmeticUtils.lcm(0L, 0L)).isEqualTo(0L);

        try {
            // lcm == abs(MIN_VALUE) cannot be represented as a nonnegative int
            ArithmeticUtils.lcm(Long.MIN_VALUE, 1);
            fail("Expecting ArithmeticException");
        } catch (ArithmeticException expected) {
            // expected
        }

        try {
            // lcm == abs(MIN_VALUE) cannot be represented as a nonnegative int
            ArithmeticUtils.lcm(Long.MIN_VALUE, 1<<20);
            fail("Expecting ArithmeticException");
        } catch (ArithmeticException expected) {
            // expected
        }

        assertThat(ArithmeticUtils.lcm((long) Integer.MAX_VALUE, Integer.MAX_VALUE - 1))
            .isEqualTo((long) Integer.MAX_VALUE * (Integer.MAX_VALUE - 1));
        try {
            ArithmeticUtils.lcm(Long.MAX_VALUE, Long.MAX_VALUE - 1);
            fail("Expecting ArithmeticException");
        } catch (ArithmeticException expected) {
            // expected
        }
    }

    @Test
    public void testMulAndCheck() {
        int big = Integer.MAX_VALUE;
        int bigNeg = Integer.MIN_VALUE;
        assertThat(ArithmeticUtils.mulAndCheck(big, 1)).isEqualTo(big);
        try {
            ArithmeticUtils.mulAndCheck(big, 2);
            fail("Expecting ArithmeticException");
        } catch (ArithmeticException ex) {
        }
        try {
            ArithmeticUtils.mulAndCheck(bigNeg, 2);
            fail("Expecting ArithmeticException");
        } catch (ArithmeticException ex) {
        }
    }

    @Test
    public void testMulAndCheckLong() {
        long max = Long.MAX_VALUE;
        long min = Long.MIN_VALUE;
        assertThat(ArithmeticUtils.mulAndCheck(max, 1L)).isEqualTo(max);
        assertThat(ArithmeticUtils.mulAndCheck(min, 1L)).isEqualTo(min);
        assertThat(ArithmeticUtils.mulAndCheck(max, 0L)).isEqualTo(0L);
        assertThat(ArithmeticUtils.mulAndCheck(min, 0L)).isEqualTo(0L);
        assertThat(ArithmeticUtils.mulAndCheck(1L, max)).isEqualTo(max);
        assertThat(ArithmeticUtils.mulAndCheck(1L, min)).isEqualTo(min);
        assertThat(ArithmeticUtils.mulAndCheck(0L, max)).isEqualTo(0L);
        assertThat(ArithmeticUtils.mulAndCheck(0L, min)).isEqualTo(0L);
        assertThat(ArithmeticUtils.mulAndCheck(-1L, -1L)).isEqualTo(1L);
        assertThat(ArithmeticUtils.mulAndCheck(min / 2, 2)).isEqualTo(min);
        testMulAndCheckLongFailure(max, 2L);
        testMulAndCheckLongFailure(2L, max);
        testMulAndCheckLongFailure(min, 2L);
        testMulAndCheckLongFailure(2L, min);
        testMulAndCheckLongFailure(min, -1L);
        testMulAndCheckLongFailure(-1L, min);
    }

    @Test
    public void testSubAndCheck() {
        int big = Integer.MAX_VALUE;
        int bigNeg = Integer.MIN_VALUE;
        assertThat(ArithmeticUtils.subAndCheck(big, 0)).isEqualTo(big);
        assertThat(ArithmeticUtils.subAndCheck(bigNeg, -1)).isEqualTo(bigNeg + 1);
        assertThat(ArithmeticUtils.subAndCheck(bigNeg, -big)).isEqualTo(-1);
        try {
            ArithmeticUtils.subAndCheck(big, -1);
            fail("Expecting ArithmeticException");
        } catch (ArithmeticException ex) {
        }
        try {
            ArithmeticUtils.subAndCheck(bigNeg, 1);
            fail("Expecting ArithmeticException");
        } catch (ArithmeticException ex) {
        }
    }

    @Test
    public void testSubAndCheckErrorMessage() {
        int big = Integer.MAX_VALUE;
        try {
            ArithmeticUtils.subAndCheck(big, -1);
            fail("Expecting ArithmeticException");
        } catch (ArithmeticException ex) {
            assertThat(ex.getMessage().length() > 1).isTrue();
        }
    }

    @Test
    public void testSubAndCheckLong() {
        long max = Long.MAX_VALUE;
        long min = Long.MIN_VALUE;
        assertThat(ArithmeticUtils.subAndCheck(max, 0)).isEqualTo(max);
        assertThat(ArithmeticUtils.subAndCheck(min, 0)).isEqualTo(min);
        assertThat(ArithmeticUtils.subAndCheck(0, max)).isEqualTo(-max);
        assertThat(ArithmeticUtils.subAndCheck(min, -1)).isEqualTo(min + 1);
        // min == -1-max
        assertThat(ArithmeticUtils.subAndCheck(-max - 1, -max)).isEqualTo(-1);
        assertThat(ArithmeticUtils.subAndCheck(-1, -1 - max)).isEqualTo(max);
        testSubAndCheckLongFailure(0L, min);
        testSubAndCheckLongFailure(max, -1L);
        testSubAndCheckLongFailure(min, 1L);
    }

    @Test
    public void testPow() {

        assertThat(ArithmeticUtils.pow(21, 7)).isEqualTo(1801088541);
        assertThat(ArithmeticUtils.pow(21, 0)).isEqualTo(1);
        try {
            ArithmeticUtils.pow(21, -7);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected behavior
        }

        assertThat(ArithmeticUtils.pow(21, 7)).isEqualTo(1801088541);
        assertThat(ArithmeticUtils.pow(21, 0)).isEqualTo(1);
        try {
            ArithmeticUtils.pow(21, -7);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected behavior
        }

        assertThat(ArithmeticUtils.pow(21l, 7)).isEqualTo(1801088541l);
        assertThat(ArithmeticUtils.pow(21l, 0)).isEqualTo(1l);
        try {
            ArithmeticUtils.pow(21l, -7);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected behavior
        }

        BigInteger twentyOne = BigInteger.valueOf(21l);
        assertThat(ArithmeticUtils.pow(twentyOne, 7)).isEqualTo(BigInteger.valueOf(1801088541l));
        assertThat(ArithmeticUtils.pow(twentyOne, 0)).isEqualTo(BigInteger.ONE);
        try {
            ArithmeticUtils.pow(twentyOne, -7);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected behavior
        }

        assertThat(ArithmeticUtils.pow(twentyOne, 7l)).isEqualTo(BigInteger.valueOf(1801088541l));
        assertThat(ArithmeticUtils.pow(twentyOne, 0l)).isEqualTo(BigInteger.ONE);
        try {
            ArithmeticUtils.pow(twentyOne, -7l);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected behavior
        }

        assertThat(ArithmeticUtils.pow(twentyOne, BigInteger.valueOf(7l)))
            .isEqualTo(BigInteger.valueOf(1801088541l));
        assertThat(ArithmeticUtils.pow(twentyOne, BigInteger.ZERO)).isEqualTo(BigInteger.ONE);
        try {
            ArithmeticUtils.pow(twentyOne, BigInteger.valueOf(-7l));
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected behavior
        }

        BigInteger bigOne =
            new BigInteger("1543786922199448028351389769265814882661837148" +
                           "4763915343722775611762713982220306372888519211" +
                           "560905579993523402015636025177602059044911261");
        assertThat(ArithmeticUtils.pow(twentyOne, 103)).isEqualTo(bigOne);
        assertThat(ArithmeticUtils.pow(twentyOne, 103l)).isEqualTo(bigOne);
        assertThat(ArithmeticUtils.pow(twentyOne, BigInteger.valueOf(103l))).isEqualTo(bigOne);

    }

    @Test(expected=ArithmeticException.class)
    public void testPowIntOverflow() {
        ArithmeticUtils.pow(21, 8);
    }

    @Test
    public void testPowInt() {
        final int base = 21;

        assertThat(ArithmeticUtils.pow(base, 6)).isEqualTo(85766121L);
        assertThat(ArithmeticUtils.pow(base, 7)).isEqualTo(1801088541L);
    }

    @Test(expected=ArithmeticException.class)
    public void testPowNegativeIntOverflow() {
        ArithmeticUtils.pow(-21, 8);
    }

    @Test
    public void testPowNegativeInt() {
        final int base = -21;

        assertThat(ArithmeticUtils.pow(base, 6)).isEqualTo(85766121);
        assertThat(ArithmeticUtils.pow(base, 7)).isEqualTo(-1801088541);
    }

    @Test
    public void testPowMinusOneInt() {
        final int base = -1;
        for (int i = 0; i < 100; i++) {
            final int pow = ArithmeticUtils.pow(base, i);
            assertThat(pow).as("i: " + i).isEqualTo(i % 2 == 0 ? 1 : -1);
        }
    }

    @Test
    public void testPowOneInt() {
        final int base = 1;
        for (int i = 0; i < 100; i++) {
            final int pow = ArithmeticUtils.pow(base, i);
            assertThat(pow).as("i: " + i).isEqualTo(1);
        }
    }

    @Test(expected=ArithmeticException.class)
    public void testPowLongOverflow() {
        ArithmeticUtils.pow(21, 15);
    }

    @Test
    public void testPowLong() {
        final long base = 21;

        assertThat(ArithmeticUtils.pow(base, 13)).isEqualTo(154472377739119461L);
        assertThat(ArithmeticUtils.pow(base, 14)).isEqualTo(3243919932521508681L);
    }

    @Test(expected=ArithmeticException.class)
    public void testPowNegativeLongOverflow() {
        ArithmeticUtils.pow(-21L, 15);
    }

    @Test
    public void testPowNegativeLong() {
        final long base = -21;

        assertThat(ArithmeticUtils.pow(base, 13)).isEqualTo(-154472377739119461L);
        assertThat(ArithmeticUtils.pow(base, 14)).isEqualTo(3243919932521508681L);
    }

    @Test
    public void testPowMinusOneLong() {
        final long base = -1;
        for (int i = 0; i < 100; i++) {
            final long pow = ArithmeticUtils.pow(base, i);
            assertThat(pow).as("i: " + i).isEqualTo(i % 2 == 0 ? 1 : -1);
        }
    }

    @Test
    public void testPowOneLong() {
        final long base = 1;
        for (int i = 0; i < 100; i++) {
            final long pow = ArithmeticUtils.pow(base, i);
            assertThat(pow).as("i: " + i).isEqualTo(1);
        }
    }

    @Test
    public void testIsPowerOfTwo() {
        final int n = 1025;
        final boolean[] expected = new boolean[n];
        Arrays.fill(expected, false);
        for (int i = 1; i < expected.length; i *= 2) {
            expected[i] = true;
        }
        for (int i = 0; i < expected.length; i++) {
            final boolean actual = ArithmeticUtils.isPowerOfTwo(i);
            assertThat(actual == expected[i]).as(Integer.toString(i)).isTrue();
        }
    }

    private void testAddAndCheckLongFailure(long a, long b) {
        try {
            ArithmeticUtils.addAndCheck(a, b);
            fail("Expecting ArithmeticException");
        } catch (ArithmeticException ex) {
            // success
        }
    }

    private void testMulAndCheckLongFailure(long a, long b) {
        try {
            ArithmeticUtils.mulAndCheck(a, b);
            fail("Expecting ArithmeticException");
        } catch (ArithmeticException ex) {
            // success
        }
    }

    private void testSubAndCheckLongFailure(long a, long b) {
        try {
            ArithmeticUtils.subAndCheck(a, b);
            fail("Expecting ArithmeticException");
        } catch (ArithmeticException ex) {
            // success
        }
    }

    /**
     * Testing helper method.
     * @return an array of int numbers containing corner cases:<ul>
     * <li>values near the beginning of int range,</li>
     * <li>values near the end of int range,</li>
     * <li>values near zero</li>
     * <li>and some randomly distributed values.</li>
     * </ul>
     */
    private static int[] getIntSpecialCases() {
        int ints[] = new int[100];
        int i = 0;
        ints[i++] = Integer.MAX_VALUE;
        ints[i++] = Integer.MAX_VALUE - 1;
        ints[i++] = 100;
        ints[i++] = 101;
        ints[i++] = 102;
        ints[i++] = 300;
        ints[i++] = 567;
        for (int j = 0; j < 20; j++) {
            ints[i++] = j;
        }
        for (int j = i - 1; j >= 0; j--) {
            ints[i++] = ints[j] > 0 ? -ints[j] : Integer.MIN_VALUE;
        }
        java.util.Random r = new java.util.Random(System.nanoTime());
        for (; i < ints.length;) {
            ints[i++] = r.nextInt();
        }
        return ints;
    }

    /**
     * Testing helper method.
     * @return an array of long numbers containing corner cases:<ul>
     * <li>values near the beginning of long range,</li>
     * <li>values near the end of long range,</li>
     * <li>values near the beginning of int range,</li>
     * <li>values near the end of int range,</li>
     * <li>values near zero</li>
     * <li>and some randomly distributed values.</li>
     * </ul>
     */
    private static long[] getLongSpecialCases() {
        long longs[] = new long[100];
        int i = 0;
        longs[i++] = Long.MAX_VALUE;
        longs[i++] = Long.MAX_VALUE - 1L;
        longs[i++] = (long) Integer.MAX_VALUE + 1L;
        longs[i++] = Integer.MAX_VALUE;
        longs[i++] = Integer.MAX_VALUE - 1;
        longs[i++] = 100L;
        longs[i++] = 101L;
        longs[i++] = 102L;
        longs[i++] = 300L;
        longs[i++] = 567L;
        for (int j = 0; j < 20; j++) {
            longs[i++] = j;
        }
        for (int j = i - 1; j >= 0; j--) {
            longs[i++] = longs[j] > 0L ? -longs[j] : Long.MIN_VALUE;
        }
        java.util.Random r = new java.util.Random(System.nanoTime());
        for (; i < longs.length;) {
            longs[i++] = r.nextLong();
        }
        return longs;
    }

    private static long toUnsignedLong(int number) {
        return number < 0 ? 0x100000000L + (long)number : (long)number;
    }

    private static int remainderUnsignedExpected(int dividend, int divisor) {
        return (int)remainderUnsignedExpected(toUnsignedLong(dividend), toUnsignedLong(divisor));
    }

    private static int divideUnsignedExpected(int dividend, int divisor) {
        return (int)divideUnsignedExpected(toUnsignedLong(dividend), toUnsignedLong(divisor));
    }

    private static BigInteger toUnsignedBigInteger(long number) {
        return number < 0L ? BigInteger.ONE.shiftLeft(64).add(BigInteger.valueOf(number)) : BigInteger.valueOf(number);
    }

    private static long remainderUnsignedExpected(long dividend, long divisor) {
        return toUnsignedBigInteger(dividend).remainder(toUnsignedBigInteger(divisor)).longValue();
    }

    private static long divideUnsignedExpected(long dividend, long divisor) {
        return toUnsignedBigInteger(dividend).divide(toUnsignedBigInteger(divisor)).longValue();
    }

    @Test(timeout=5000L)
    public void testRemainderUnsignedInt() {
        assertThat(ArithmeticUtils.remainderUnsigned(-2147479015, 63)).isEqualTo(36);
        assertThat(ArithmeticUtils.remainderUnsigned(-2147479015, 25)).isEqualTo(6);
    }

    @Test(timeout=5000L)
    public void testRemainderUnsignedIntSpecialCases() {
        int ints[] = getIntSpecialCases();
        for (int dividend : ints) {
            for (int divisor : ints) {
                if (divisor == 0) {
                    try {
                        ArithmeticUtils.remainderUnsigned(dividend, divisor);
                        fail("Should have failed with ArithmeticException: division by zero");
                    } catch (ArithmeticException e) {
                        // Success.
                    }
                } else {
                    assertThat(ArithmeticUtils.remainderUnsigned(dividend, divisor))
                        .isEqualTo(remainderUnsignedExpected(dividend, divisor));
                }
            }
        }
    }

    @Test(timeout=5000L)
    public void testRemainderUnsignedLong() {
        assertThat(ArithmeticUtils.remainderUnsigned(-2147479015L, 63L)).isEqualTo(48L);
    }

    @Test//(timeout=5000L)
    public void testRemainderUnsignedLongSpecialCases() {
        long longs[] = getLongSpecialCases();
        for (long dividend : longs) {
            for (long divisor : longs) {
                if (divisor == 0L) {
                    try {
                        ArithmeticUtils.remainderUnsigned(dividend, divisor);
                        fail("Should have failed with ArithmeticException: division by zero");
                    } catch (ArithmeticException e) {
                        // Success.
                    }
                } else {
                    assertThat(ArithmeticUtils.remainderUnsigned(dividend, divisor))
                        .isEqualTo(remainderUnsignedExpected(dividend, divisor));
                }
            }
        }
    }

    @Test(timeout=5000L)
    public void testDivideUnsignedInt() {
        assertThat(ArithmeticUtils.divideUnsigned(-2147479015, 63)).isEqualTo(34087115);
        assertThat(ArithmeticUtils.divideUnsigned(-2147479015, 25)).isEqualTo(85899531);
        assertThat(ArithmeticUtils.divideUnsigned(-3, 2)).isEqualTo(2147483646);
        assertThat(ArithmeticUtils.divideUnsigned(-16, 13)).isEqualTo(330382098);
        assertThat(ArithmeticUtils.divideUnsigned(-16, 14)).isEqualTo(306783377);
        assertThat(ArithmeticUtils.divideUnsigned(-1, 2147483647)).isEqualTo(2);
        assertThat(ArithmeticUtils.divideUnsigned(-2, 2147483647)).isEqualTo(2);
        assertThat(ArithmeticUtils.divideUnsigned(-3, 2147483647)).isEqualTo(1);
        assertThat(ArithmeticUtils.divideUnsigned(-16, 2147483647)).isEqualTo(1);
        assertThat(ArithmeticUtils.divideUnsigned(-16, 2147483646)).isEqualTo(1);
    }

    @Test(timeout=5000L)
    public void testDivideUnsignedIntSpecialCases() {
        int ints[] = getIntSpecialCases();
        for (int dividend : ints) {
            for (int divisor : ints) {
                if (divisor == 0) {
                    try {
                        ArithmeticUtils.divideUnsigned(dividend, divisor);
                        fail("Should have failed with ArithmeticException: division by zero");
                    } catch (ArithmeticException e) {
                        // Success.
                    }
                } else {
                    assertThat(ArithmeticUtils.divideUnsigned(dividend, divisor))
                        .isEqualTo(divideUnsignedExpected(dividend, divisor));
                }
            }
        }
    }

    @Test(timeout=5000L)
    public void testDivideUnsignedLong() {
        assertThat(ArithmeticUtils.divideUnsigned(-2147479015L, 63L))
            .isEqualTo(292805461453366231L);
    }

    @Test(timeout=5000L)
    public void testDivideUnsignedLongSpecialCases() {
        long longs[] = getLongSpecialCases();
        for (long dividend : longs) {
            for (long divisor : longs) {
                if (divisor == 0L) {
                    try {
                        ArithmeticUtils.divideUnsigned(dividend, divisor);
                        fail("Should have failed with ArithmeticException: division by zero");
                    } catch (ArithmeticException e) {
                        // Success.
                    }
                } else {
                    assertThat(ArithmeticUtils.divideUnsigned(dividend, divisor))
                        .isEqualTo(divideUnsignedExpected(dividend, divisor));
                }
            }
        }
    }
}
