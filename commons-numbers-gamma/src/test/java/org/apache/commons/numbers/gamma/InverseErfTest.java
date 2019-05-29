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
 * Tests for {@link InverseErf}.
 */
public class InverseErfTest {
    @Test
    public void testErfInvNaN() {
        assertThat(Double.isNaN(InverseErf.value(-1.001))).isTrue();
        assertThat(Double.isNaN(InverseErf.value(+1.001))).isTrue();
    }

    @Test
    public void testErfInvInfinite() {
        assertThat(Double.isInfinite(InverseErf.value(-1))).isTrue();
        assertThat(InverseErf.value(-1) < 0).isTrue();
        assertThat(Double.isInfinite(InverseErf.value(+1))).isTrue();
        assertThat(InverseErf.value(+1) > 0).isTrue();
    }

    @Test
    public void testErfInv() {
        for (double x = -5.9; x < 5.9; x += 0.01) {
            final double y = Erf.value(x);
            final double dydx = 2 * Math.exp(-x * x) / Math.sqrt(Math.PI);
            assertThat(InverseErf.value(y)).isCloseTo(x, offset(1.0e-15 / dydx));
        }
    }
}
