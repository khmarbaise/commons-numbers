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
 * Tests for {@link LanczosApproximation}.
 */
public class LanczosApproximationTest {
    @Test
    public void testG() {
        assertThat(LanczosApproximation.g()).isCloseTo(607d / 128d, offset(0d));
    }

    @Test
    public void testSomeValues() {
        // Given that few values are checked, this test would only serve
        // as early warning of unexpected changes to the current code.
        assertThat(LanczosApproximation.value(0.1d)).isCloseTo(29.020294557631818d, offset(0d));
        assertThat(LanczosApproximation.value(1.0d)).isCloseTo(13.14778027539684d, offset(0d));
        assertThat(LanczosApproximation.value(2.0d)).isCloseTo(7.897828855157814d, offset(0d));
    }
}
