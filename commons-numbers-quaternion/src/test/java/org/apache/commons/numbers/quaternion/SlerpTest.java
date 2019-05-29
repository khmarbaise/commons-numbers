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
package org.apache.commons.numbers.quaternion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

import org.apache.commons.numbers.core.Precision;
import org.junit.Test;

public class SlerpTest {

    private static final double EPS = 1e-7;

    private static final double SQRT_2 = Math.sqrt(2.0);
    private static final double INV_SQRT_2 = 1.0 / SQRT_2;

    @Test
    public void testSlerp_sphericalAlgorithm() {
        // arrange
        Quaternion q1 = createZRotation(0.75 * Math.PI);
        Quaternion q2 = createZRotation(-0.75 * Math.PI);

        Slerp slerp = new Slerp(q1, q2);

        // act/assert
        // the algorithm should follow the path around the pi coordinate of the circle rather than
        // the one through the zero coordinate
        assertQuaternion(q1.positivePolarForm(), slerp.apply(0));
        assertQuaternion(createZRotation(0.875 * Math.PI), slerp.apply(0.25));
        assertQuaternion(createZRotation(Math.PI), slerp.apply(0.5));
        assertQuaternion(createZRotation(-0.875 * Math.PI), slerp.apply(0.75));
        assertQuaternion(q2.positivePolarForm(), slerp.apply(1));
    }

    @Test
    public void testSlerp_sphericalAlgorithm_allOutputsAreInPositivePolarForm() {
        // arrange
        Quaternion q1 = createZRotation(0.75 * Math.PI);
        Quaternion q2 = createZRotation(-0.75 * Math.PI);

        Slerp slerp = new Slerp(q1, q2);

        final int numSteps = 200;
        final double delta = 1d / numSteps;
        for (int step = 0; step <= numSteps; step++) {
            final double t = -10 + step * delta;

            // act
            Quaternion result = slerp.apply(t);

            // assert
            assertThat(result.norm()).isCloseTo(1.0, offset(EPS));
            assertThat(result.getW() >= 0.0).isTrue();
        }
    }

    @Test
    public void testSlerp_nonNormalizedInputs() {
        // arrange
        Quaternion q1 = createZRotation(0).multiply(10.0);
        Quaternion q2 = createZRotation(Math.PI).multiply(0.2);

        Slerp slerp = new Slerp(q1, q2);

        // act/assert
        assertQuaternion(q1.positivePolarForm(), slerp.apply(0));
        assertQuaternion(createZRotation(0.25 * Math.PI), slerp.apply(0.25));
        assertQuaternion(createZRotation(0.5 * Math.PI), slerp.apply(0.5));
        assertQuaternion(createZRotation(0.75 * Math.PI), slerp.apply(0.75));
        assertQuaternion(q2.positivePolarForm(), slerp.apply(1));
    }

    @Test
    public void testSlerp_linearAlgorithm() {
        // arrange
        Quaternion q1 = createZRotation(0.75 * Math.PI);
        Quaternion q2 = createZRotation(0.76 * Math.PI);

        Slerp slerp = new Slerp(q1, q2);

        // act/assert
        assertQuaternion(q1.positivePolarForm(), slerp.apply(0));
        assertQuaternion(createZRotation(0.7525 * Math.PI), slerp.apply(0.25));
        assertQuaternion(createZRotation(0.755 * Math.PI), slerp.apply(0.5));
        assertQuaternion(createZRotation(0.7575 * Math.PI), slerp.apply(0.75));
        assertQuaternion(q2.positivePolarForm(), slerp.apply(1));
    }

    @Test
    public void testSlerp_linearAlgorithm_allOutputsAreInPositivePolarForm() {
        // arrange
        Quaternion q1 = createZRotation(0.75 * Math.PI);
        Quaternion q2 = createZRotation(0.76 * Math.PI);

        Slerp slerp = new Slerp(q1, q2);

        final int numSteps = 200;
        final double delta = 1d / numSteps;
        for (int step = 0; step <= numSteps; step++) {
            final double t = -10 + step * delta;

            // act
            Quaternion result = slerp.apply(t);

            // assert
            assertThat(result.norm()).isCloseTo(1.0, offset(EPS));
            assertThat(result.getW() >= 0.0).isTrue();
        }
    }

    @Test
    public void testSlerp_identicalInputs() {
        // arrange
        Quaternion q1 = createZRotation(0);
        Quaternion q2 = createZRotation(0);

        Slerp slerp = new Slerp(q1, q2);

        // act/assert
        Quaternion expected = q1.positivePolarForm();

        assertQuaternion(expected, slerp.apply(0));
        assertQuaternion(expected, slerp.apply(0.5));
        assertQuaternion(expected, slerp.apply(1));
    }

    @Test
    public void testSlerp_inputQuaternionsHaveMinusOneDotProduct() {
        // arrange
        Quaternion q1 = createZRotation(0.5 * Math.PI);
        Quaternion q2 = createZRotation(1.5 * Math.PI).conjugate(); // 3pi/2 around -z

        Slerp slerp = new Slerp(q1, q2);

        // act/assert
        assertThat(q1.dot(q2)).isCloseTo(-1.0, offset(EPS));

        Quaternion expected = q1.positivePolarForm();

        assertQuaternion(expected, slerp.apply(0));
        assertQuaternion(expected, slerp.apply(0.5));
        assertQuaternion(expected, slerp.apply(1));
    }

    @Test
    public void testSlerp_tOutsideOfZeroToOne() {
        // arrange
        Quaternion q1 = createZRotation(0);
        Quaternion q2 = createZRotation(0.25 * Math.PI);

        Slerp slerp = new Slerp(q1, q2);

        // act/assert
        assertQuaternion(createZRotation(-0.5 * Math.PI).positivePolarForm(), slerp.apply(-2));
        assertQuaternion(createZRotation(-0.25 * Math.PI).positivePolarForm(), slerp.apply(-1));

        assertQuaternion(createZRotation(0).positivePolarForm(), slerp.apply(0));

        assertQuaternion(createZRotation(0.25 * Math.PI), slerp.apply(1));
        assertQuaternion(createZRotation(0.5 * Math.PI), slerp.apply(2));
    }

    @Test
    public void testVectorTransform_simple() {
        // arrange
        Quaternion q0 = Quaternion.of(1, 0, 0, 0); // rotation of zero
        Quaternion q1 = Quaternion.of(0, 0, 0, 1); // pi rotation around +z

        Slerp slerp = new Slerp(q0, q1);

        double[] vec = { 2, 0, 1 };

        // act/assert
        assertThat(transformVector(slerp.apply(0), vec))
            .contains(new double[]{2, 0, 1}, offset(EPS));

        assertThat(transformVector(slerp.apply(0.25), vec))
            .contains(new double[]{SQRT_2, SQRT_2, 1}, offset(EPS));

        assertThat(transformVector(slerp.apply(0.5), vec))
            .contains(new double[]{0, 2, 1}, offset(EPS));

        assertThat(transformVector(slerp.apply(0.75), vec))
            .contains(new double[]{-SQRT_2, SQRT_2, 1}, offset(EPS));

        assertThat(transformVector(slerp.apply(1), vec))
            .contains(new double[]{-2, 0, 1}, offset(EPS));
    }

    @Test
    public void testVectorTransform_multipleCombinations() {
        // arrange
        Quaternion[] quaternions = {
                // +x axis
                Quaternion.of(1, 0, 0, 0), // 0 pi
                Quaternion.of(INV_SQRT_2, INV_SQRT_2, 0, 0), // pi/2
                Quaternion.of(0, 1, 0, 0), // pi

                // -x axis
                Quaternion.of(1, 0, 0, 0), // 0 pi
                Quaternion.of(INV_SQRT_2, -INV_SQRT_2, 0, 0), // pi/2
                Quaternion.of(0, -1, 0, 0), // pi

                // +y axis
                Quaternion.of(1, 0, 0, 0), // 0 pi
                Quaternion.of(INV_SQRT_2, 0, INV_SQRT_2, 0), // pi/2
                Quaternion.of(0, 0, 1, 0), // pi

                // -y axis
                Quaternion.of(1, 0, 0, 0), // 0 pi
                Quaternion.of(INV_SQRT_2, 0, -INV_SQRT_2, 0), // pi/2
                Quaternion.of(0, 0, -1, 0), // pi

                // +z axis
                Quaternion.of(1, 0, 0, 0), // 0 pi
                Quaternion.of(INV_SQRT_2, 0, 0, INV_SQRT_2), // pi/2
                Quaternion.of(0, 0, 0, 1), // pi

                // -z axis
                Quaternion.of(1, 0, 0, 0), // 0 pi
                Quaternion.of(INV_SQRT_2, 0, 0, -INV_SQRT_2), // pi/2
                Quaternion.of(0, 0, 0, -1) // pi
        };

        // act/assert
        // test each quaternion against all of the others (including itself)
        for (int i=0; i<quaternions.length; ++i) {
            for (int j=0; j<quaternions.length; ++j) {
                checkSlerpCombination(quaternions[i], quaternions[j]);
            }
        }
    }

    private void checkSlerpCombination(Quaternion start, Quaternion end) {
        Slerp slerp = new Slerp(start, end);

        double[] vec = { 1, 2, 3 };
        double vecNorm = norm(vec);

        double[] startVec = transformVector(start, vec);
        double[] endVec = transformVector(end, vec);

        // check start and end values
        assertThat(transformVector(slerp.apply(0), vec)).contains(startVec, offset(EPS));
        assertThat(transformVector(slerp.apply(1), vec)).contains(endVec, offset(EPS));

        // check intermediate values
        double prevAngle = -1;
        final int numSteps = 100;
        final double delta = 1.0 / numSteps;
        for (int step = 0; step <= numSteps; ++step) {
            final double t= step * delta;
            Quaternion result = slerp.apply(t);

            double[] slerpVec = transformVector(result, vec);

            // the transformation should not effect the vector magnitude
            assertThat(norm(slerpVec)).isCloseTo(vecNorm, offset(EPS));

            // make sure that we're steadily progressing to the end angle
            double angle = angle(slerpVec, startVec);
            assertThat(Precision.compareTo(angle, prevAngle, EPS) >= 0)
                .as("Expected slerp angle to continuously increase; previous angle was " +
                    prevAngle + " and new angle is " + angle).isTrue();
        }
    }

    @Test
    public void testVectorTransform_tOutsideOfZeroToOne_() {
        // arrange
        double angle1 = Math.PI * 0.25;
        double angle2 = Math.PI * 0.75;

        double halfAngle1 = 0.5 * angle1;
        double halfAngle2 = 0.5 * angle2;

        Quaternion q1 = Quaternion.of(Math.cos(halfAngle1), 0, 0, Math.sin(halfAngle1)); // pi/4 around +z
        Quaternion q2 = Quaternion.of(Math.cos(halfAngle2), 0, 0, Math.sin(halfAngle2)); // 3pi/4 around +z

        double[] vec = new double[] { 1, 0, 0 };

        // act/assert
        Slerp slerp12 = new Slerp(q1, q2);
        assertThat(transformVector(slerp12.apply(-4.5), vec))
            .contains(new double[]{1, 0, 0}, offset(EPS));
        assertThat(transformVector(slerp12.apply(-0.5), vec))
            .contains(new double[]{1, 0, 0}, offset(EPS));
        assertThat(transformVector(slerp12.apply(1.5), vec))
            .contains(new double[]{-1, 0, 0}, offset(EPS));
        assertThat(transformVector(slerp12.apply(5.5), vec))
            .contains(new double[]{-1, 0, 0}, offset(EPS));

        Slerp slerp21 = new Slerp(q2, q1);
        assertThat(transformVector(slerp21.apply(-4.5), vec))
            .contains(new double[]{-1, 0, 0}, offset(EPS));
        assertThat(transformVector(slerp21.apply(-0.5), vec))
            .contains(new double[]{-1, 0, 0}, offset(EPS));
        assertThat(transformVector(slerp21.apply(1.5), vec))
            .contains(new double[]{1, 0, 0}, offset(EPS));
        assertThat(transformVector(slerp21.apply(5.5), vec))
            .contains(new double[]{1, 0, 0}, offset(EPS));
    }

    /**
     * Create a quaterion representing a rotation around the +z axis.
     * @param theta
     * @return
     */
    private static Quaternion createZRotation(final double theta) {
        double halfAngle = theta * 0.5;

        return Quaternion.of(Math.cos(halfAngle), 0, 0, Math.sin(halfAngle));
    }

    /**
     * Compute the norm of a vector.
     * @param vec
     * @return
     */
    private static double norm(double[] vec) {
        double sum = 0.0;
        for (int i=0; i<vec.length; ++i) {
            sum += vec[i] * vec[i];
        }
        return Math.sqrt(sum);
    }

    /**
     * Compute the angle between two vectors.
     * @param a
     * @param b
     * @return
     */
    private static double angle(double[] a, double[] b) {
        double cos = dot(a, b) / (norm(a) * norm(b));
        return Math.acos(cos);
    }

    /**
     * Compute the dot product of two vectors. The arrays are assumed to
     * have the same length.
     * @param a
     * @param b
     * @return
     */
    private static double dot(double[] a, double[] b) {
        double result = 0.0;
        for (int i=0; i<a.length; ++i) {
            result += a[i] * b[i];
        }
        return result;
    }

    /**
     * Tranform the vector by assigning its components to the vectorial part of a quaternion
     * and then multiplying it on the right by the quaternion and the left by the quaternion's
     * conjugate (inverse).
     * @param q the quaternion instance
     * @param vec the 3D vector to transform
     * @return the transformed 3D vector
     */
    private static double[] transformVector(Quaternion q, double[] vec) {
        Quaternion qVec = Quaternion.of(0, vec[0], vec[1], vec[2]);
        Quaternion qConj = q.conjugate();

        Quaternion result = q.multiply(qVec).multiply(qConj);

        return new double[] { result.getX(), result.getY(), result.getZ() };
    }

    /**
     * Assert that the given quaternions are equal.
     * @param expected
     * @param actual
     */
    private static void assertQuaternion(Quaternion expected, Quaternion actual) {
        String msg = "Expected quaternion to equal " + expected + " but was " + actual;

        assertThat(actual.getW()).as(msg).isCloseTo(expected.getW(), offset(EPS));
        assertThat(actual.getX()).as(msg).isCloseTo(expected.getX(), offset(EPS));
        assertThat(actual.getY()).as(msg).isCloseTo(expected.getY(), offset(EPS));
        assertThat(actual.getZ()).as(msg).isCloseTo(expected.getZ(), offset(EPS));
    }
}
