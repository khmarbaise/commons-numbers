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

import java.util.Iterator;
import java.util.Comparator;

import org.junit.Test;

/**
 * Tests for the {@link Combinations} class.
 */
public class CombinationsTest {
    @Test
    public void testAccessor1() {
        final int n = 5;
        final int k = 3;
        assertThat(new Combinations(n, k).getN()).isEqualTo(n);
    }
    @Test
    public void testAccessor2() {
        final int n = 5;
        final int k = 3;
        assertThat(new Combinations(n, k).getK()).isEqualTo(k);
    }

    @Test
    public void testLexicographicIterator() {
        checkLexicographicIterator(new Combinations.LexicographicComparator(5, 3));
        checkLexicographicIterator(new Combinations.LexicographicComparator(6, 4));
        checkLexicographicIterator(new Combinations.LexicographicComparator(8, 2));
        checkLexicographicIterator(new Combinations.LexicographicComparator(6, 1));
        checkLexicographicIterator(new Combinations.LexicographicComparator(3, 3));
        checkLexicographicIterator(new Combinations.LexicographicComparator(1, 1));
        checkLexicographicIterator(new Combinations.LexicographicComparator(1, 0));
        checkLexicographicIterator(new Combinations.LexicographicComparator(0, 0));
        checkLexicographicIterator(new Combinations.LexicographicComparator(4, 2));
        checkLexicographicIterator(new Combinations.LexicographicComparator(123, 2));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testLexicographicComparatorWrongIterate1() {
        final int n = 5;
        final int k = 3;
        final Comparator<int[]> comp = new Combinations.LexicographicComparator(n, k);
        comp.compare(new int[] {1}, new int[] {0, 1, 2});
    }

    @Test(expected=IllegalArgumentException.class)
    public void testLexicographicComparatorWrongIterate2() {
        final int n = 5;
        final int k = 3;
        final Comparator<int[]> comp = new Combinations.LexicographicComparator(n, k);
        comp.compare(new int[] {0, 1, 2}, new int[] {0, 1, 2, 3});
    }

    @Test(expected=IllegalArgumentException.class)
    public void testLexicographicComparatorWrongIterate3() {
        final int n = 5;
        final int k = 3;
        final Comparator<int[]> comp = new Combinations.LexicographicComparator(n, k);
        comp.compare(new int[] {1, 2, 5}, new int[] {0, 1, 2});
    }

    @Test(expected=IllegalArgumentException.class)
    public void testLexicographicComparatorWrongIterate4() {
        final int n = 5;
        final int k = 3;
        final Comparator<int[]> comp = new Combinations.LexicographicComparator(n, k);
        comp.compare(new int[] {1, 2, 4}, new int[] {-1, 1, 2});
    }

    @Test
    public void testLexicographicComparatorAccessors() {
        final int n = 9;
        final int k = 6;
        final Combinations.LexicographicComparator comp =
            new Combinations.LexicographicComparator(n, k);
        assertThat(comp.getN()).isEqualTo(n);
        assertThat(comp.getK()).isEqualTo(k);
    }

    @Test
    public void testLexicographicComparator() {
        final int n = 5;
        final int k = 3;
        final Comparator<int[]> comp = new Combinations.LexicographicComparator(n, k);
        assertThat(comp.compare(new int[]{1, 2, 4},
            new int[]{1, 2, 3})).isEqualTo(1);
        assertThat(comp.compare(new int[]{0, 1, 4},
            new int[]{0, 2, 4})).isEqualTo(-1);
        assertThat(comp.compare(new int[]{1, 3, 4},
            new int[]{1, 3, 4})).isEqualTo(0);
    }

    /**
     * Check that iterates can be passed unsorted.
     */
    @Test
    public void testLexicographicComparatorUnsorted() {
        final int n = 5;
        final int k = 3;
        final Comparator<int[]> comp = new Combinations.LexicographicComparator(n, k);
        assertThat(comp.compare(new int[]{1, 4, 2},
            new int[]{1, 3, 2})).isEqualTo(1);
        assertThat(comp.compare(new int[]{0, 4, 1},
            new int[]{0, 4, 2})).isEqualTo(-1);
        assertThat(comp.compare(new int[]{1, 4, 3},
            new int[]{1, 3, 4})).isEqualTo(0);
    }

    @Test
    public void testEmptyCombination() {
        final Iterator<int[]> iter = new Combinations(12345, 0).iterator();
        assertThat(iter.hasNext()).isTrue();
        final int[] c = iter.next();
        assertThat(c.length).isEqualTo(0);
        assertThat(iter.hasNext()).isFalse();
    }

    @Test
    public void testFullSetCombination() {
        final int n = 67;
        final Iterator<int[]> iter = new Combinations(n, n).iterator();
        assertThat(iter.hasNext()).isTrue();
        final int[] c = iter.next();
        assertThat(c.length).isEqualTo(n);

        for (int i = 0; i < n; i++) {
            assertThat(c[i]).isEqualTo(i);
        }

        assertThat(iter.hasNext()).isFalse();
    }

    /**
     * Verifies that the iterator generates a lexicographically
     * increasing sequence of b(n,k) arrays, each having length k
     * and each array itself increasing.
     *
     * @param comp Comparator.
     */
    private void checkLexicographicIterator(Combinations.LexicographicComparator comp) {
        final int n = comp.getN();
        final int k = comp.getK();

        int[] lastIterate = null;

        long numIterates = 0;
        for (int[] iterate : new Combinations(n, k)) {
            assertThat(iterate.length).isEqualTo(k);

            // Check that the sequence of iterates is ordered.
            if (lastIterate != null) {
                assertThat(comp.compare(iterate, lastIterate) == 1).isTrue();
            }

            // Check that each iterate is ordered.
            for (int i = 1; i < iterate.length; i++) {
                assertThat(iterate[i] > iterate[i - 1]).isTrue();
            }

            lastIterate = iterate;
            ++numIterates;
        }

        // Check the number of iterates.
        assertThat(numIterates).isEqualTo(BinomialCoefficient.value(n, k));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCombinationsIteratorFail1() {
        new Combinations(4, 5).iterator();
    }
    @Test(expected=IllegalArgumentException.class)
    public void testCombinationsIteratorFail2() {
        new Combinations(-1, -2).iterator();
    }
}
