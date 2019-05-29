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
package org.apache.commons.numbers.primes;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class SmallPrimesTest {

    // Primes larger than the small PRIMES array in SmallPrimes
    private static final int[] LARGE_PRIME = {3673, 3677};

    @Test
    public void smallTrialDivision_smallComposite() {
        final List<Integer> factors = new ArrayList<Integer>();
        final int result = SmallPrimes.smallTrialDivision(3*7*23, factors);
        assertThat(result).isEqualTo(1);
        assertThat(factors).isEqualTo(Arrays.asList(3, 7, 23));
    }

    @Test
    public void smallTrialDivision_repeatedFactors() {
        final List<Integer> factors = new ArrayList<Integer>();
        final int result = SmallPrimes.smallTrialDivision(2*2*3*3*3, factors);
        assertThat(result).isEqualTo(1);
        assertThat(factors).isEqualTo(Arrays.asList(2, 2, 3, 3, 3));
    }

    @Test
    public void smallTrialDivision_oneFactor() {
        final List<Integer> factors = new ArrayList<Integer>();
        final int result = SmallPrimes.smallTrialDivision(59, factors);
        assertThat(result).isEqualTo(1);
        assertThat(factors).isEqualTo(Collections.singletonList(59));
    }

    @Test
    public void smallTrialDivision_BoundaryPrimes() {
        final List<Integer> factors = new ArrayList<Integer>();
        final int penultimatePrime = SmallPrimes.PRIMES[SmallPrimes.PRIMES.length-2];
        final int result = SmallPrimes.smallTrialDivision(penultimatePrime*SmallPrimes.PRIMES_LAST, factors);
        assertThat(result).isEqualTo(1);
        assertThat(factors).isEqualTo(Arrays.asList(penultimatePrime, SmallPrimes.PRIMES_LAST));
    }

    @Test
    public void smallTrialDivision_largeComposite() {
        final List<Integer> factors = new ArrayList<Integer>();
        final int result = SmallPrimes.smallTrialDivision(2*5*LARGE_PRIME[0], factors);
        assertThat(result).isEqualTo(LARGE_PRIME[0]);
        assertThat(factors).isEqualTo(Arrays.asList(2, 5));
    }

    @Test
    public void smallTrialDivision_noSmallPrimeFactors() {
        final List<Integer> factors = new ArrayList<Integer>();
        final int result = SmallPrimes.smallTrialDivision(LARGE_PRIME[0]*LARGE_PRIME[1], factors);
        assertThat(result).isEqualTo(LARGE_PRIME[0] * LARGE_PRIME[1]);
        assertThat(factors).isEqualTo(Collections.<Integer>emptyList());
    }
    
    @Test
    public void boundedTrialDivision_twoDifferentFactors() {
        final List<Integer> factors = new ArrayList<Integer>();
        final int result = SmallPrimes.boundedTrialDivision(LARGE_PRIME[0]*LARGE_PRIME[1], Integer.MAX_VALUE, factors);
        assertThat(result).isEqualTo(LARGE_PRIME[1]);
        assertThat(factors).isEqualTo(Arrays.asList(LARGE_PRIME[0], LARGE_PRIME[1]));
    }

    @Test
    public void boundedTrialDivision_square() {
        final List<Integer> factors = new ArrayList<Integer>();
        final int result = SmallPrimes.boundedTrialDivision(LARGE_PRIME[0]*LARGE_PRIME[0], Integer.MAX_VALUE, factors);
        assertThat(result).isEqualTo(LARGE_PRIME[0]);
        assertThat(factors).isEqualTo(Arrays.asList(LARGE_PRIME[0], LARGE_PRIME[0]));
    }

    @Test
    public void trialDivision_smallComposite() {
        final List<Integer> factors = SmallPrimes.trialDivision(5*11*29*103);
        assertThat(factors).isEqualTo(Arrays.asList(5, 11, 29, 103));
    }

    @Test
    public void trialDivision_repeatedFactors() {
        final List<Integer> factors = SmallPrimes.trialDivision(2*2*2*2*5*5);
        assertThat(factors).isEqualTo(Arrays.asList(2, 2, 2, 2, 5, 5));
    }

    @Test
    public void trialDivision_oneSmallFactor() {
        final List<Integer> factors = SmallPrimes.trialDivision(101);
        assertThat(factors).isEqualTo(Collections.singletonList(101));
    }

    @Test
    public void trialDivision_largeComposite() {
        final List<Integer> factors = SmallPrimes.trialDivision(2*3*LARGE_PRIME[0]);
        assertThat(factors).isEqualTo(Arrays.asList(2, 3, LARGE_PRIME[0]));
    }

    @Test
    public void trialDivision_veryLargeComposite() {
        final List<Integer> factors = SmallPrimes.trialDivision(2*LARGE_PRIME[0]*LARGE_PRIME[1]);
        assertThat(factors).isEqualTo(Arrays.asList(2, LARGE_PRIME[0], LARGE_PRIME[1]));
    }

    @Test
    public void millerRabinPrimeTest_primes() {
        for (final int n : PrimesTest.PRIMES) {
            if (n % 2 == 1) {
                assertThat(SmallPrimes.millerRabinPrimeTest(n)).isTrue();
            }
        }
    }

    @Test
    public void millerRabinPrimeTest_composites() {
        for (final int n : PrimesTest.NOT_PRIMES) {
            if (n %2 == 1) {
                assertThat(SmallPrimes.millerRabinPrimeTest(n)).isFalse();
            }
        }
    }
}
