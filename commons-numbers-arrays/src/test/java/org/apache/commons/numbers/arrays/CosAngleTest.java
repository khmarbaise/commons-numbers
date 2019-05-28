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
package org.apache.commons.numbers.arrays;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test cases for the {@link CosAngle} class.
 */
class CosAngleTest {

    private static final Offset<Double> ZERO_OFFSET = Offset.offset(0d);
    private static final Offset<Double> EPSILON_OFFSET = Offset.offset(1e-15);

    @Nested
    @DisplayName("CosAngle 2D Tests")
    class CosAngle2DTest {

        private final double[] VECTOR_1 = {1, 0};
        private final double[] VECTOR_2 = {0, 1};
        private final double[] VECTOR_3 = {7, 7};
        private final double[] VECTOR_4 = {-5, 0};
        private final double[] VECTOR_5 = {-100, 100};

        @Test
        @DisplayName("CosAngle of vector {1,0}")
        void valueVector1Vector1() {
            assertThat(CosAngle.value(VECTOR_1, VECTOR_1)).isEqualTo(1.0, ZERO_OFFSET);
        }

        @Test
        @DisplayName("CosAngle of vector {1,0} and vector {0,1}")
        void valueVector1Vector2() {
            assertThat(CosAngle.value(VECTOR_1, VECTOR_2)).isEqualTo(0.0, ZERO_OFFSET);
        }

        @Test
        @DisplayName("CosAngle of vector {1,0} and vector {7,7}")
        void valueVector1Vector3() {
            assertThat(CosAngle.value(VECTOR_1, VECTOR_3))
                .isEqualTo(Math.sqrt(2) / 2, EPSILON_OFFSET);
        }

        @Test
        @DisplayName("CosAngle of vector {7,7} and vector {0,1}")
        void valueVector3Vector2() {
            assertThat(CosAngle.value(VECTOR_3, VECTOR_2))
                .isEqualTo(Math.sqrt(2) / 2, EPSILON_OFFSET);
        }

        @Test
        @DisplayName("CosAngle of vector {1,0} and vector {-5,0}")
        void valueVector1Vector4() {
            assertThat(CosAngle.value(VECTOR_1, VECTOR_4))
                .isEqualTo(-1.0, ZERO_OFFSET);
        }

        @Test
        @DisplayName("CosAngle of vector {1,0} and vector {-5,0}")
        void valueVector3Vector5() {
            assertThat(CosAngle.value(VECTOR_3, VECTOR_5))
                .isEqualTo(0.0, ZERO_OFFSET);
        }
    }

    @Nested
    @DisplayName("CosAngle 3D Tests")
    class CosAngel3DTest {

        private final double[] VECTOR_1 = {1, 1, 0};
        private final double[] VECTOR_2 = {1, 1, 1};

        @Test
        @DisplayName("CosAngle of vector {1,1,0} and vector {1,1,0}")
        void valueVector1Vector1() {
            assertThat(CosAngle.value(VECTOR_1, VECTOR_1)).isEqualTo(1.0, EPSILON_OFFSET);
        }

        @Test
        @DisplayName("CosAngle of vector {1,1,0} and vector {1,1,1}")
        void valueVector1Vector2() {
            assertThat(CosAngle.value(VECTOR_1, VECTOR_2))
                .isEqualTo(Math.sqrt(2) / Math.sqrt(3), EPSILON_OFFSET);
        }

    }

    @Nested
    @DisplayName("CosAngle Extreme Test")
    class CosAngleExtremTest {

        private final double TINY = 1e-200;
        private final double BIG = 1e200;

        private final double[] VECTOR_1 = {TINY, TINY};
        private final double[] VECTOR_2 = { -BIG, -BIG};
        private final double[] VECTOR_3 = { BIG, -BIG };

        @Test
        @DisplayName("CosAngle of vector {1e-200,1e-200} and vector {-1e200, -1e200}")
        void valueVector1Vector2() {
            assertThat(CosAngle.value(VECTOR_1, VECTOR_2)).isEqualTo(-1.0, EPSILON_OFFSET);
        }

        @Test
        @DisplayName("CosAngle of vector {1e-200,1e-200} and vector {1e200, -1e200}")
        void valueVector1Vector3() {
            assertThat(CosAngle.value(VECTOR_1, VECTOR_3)).isEqualTo(0.0, EPSILON_OFFSET);
        }

    }

}
