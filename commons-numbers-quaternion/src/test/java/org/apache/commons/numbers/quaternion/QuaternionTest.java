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
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.data.Offset.offset;

import java.util.Random;

import org.junit.Test;

public class QuaternionTest {
    /** Epsilon for double comparison. */
    private static final double EPS = Math.ulp(1d);
    /** Epsilon for double comparison. */
    private static final double COMPARISON_EPS = 1e-14;

    @Test
    public void testZeroQuaternion() {
        assertThat(Quaternion.ZERO.norm()).isCloseTo(0, offset(0d));
    }

    @Test
    public void testUnitQuaternions() {
        assertThat(Quaternion.ONE.norm()).isCloseTo(1, offset(0d));
        assertThat(Quaternion.ONE.normalize() == Quaternion.ONE).isTrue();

        assertThat(Quaternion.I.norm()).isCloseTo(1, offset(0d));
        assertThat(Quaternion.I.normalize() == Quaternion.I).isTrue();

        assertThat(Quaternion.J.norm()).isCloseTo(1, offset(0d));
        assertThat(Quaternion.J.normalize() == Quaternion.J).isTrue();

        assertThat(Quaternion.K.norm()).isCloseTo(1, offset(0d));
        assertThat(Quaternion.K.normalize() == Quaternion.K).isTrue();
    }

    @Test
    public final void testAccessors1() {
        final double q0 = 2;
        final double q1 = 5.4;
        final double q2 = 17;
        final double q3 = 0.0005;
        final Quaternion q = Quaternion.of(q0, q1, q2, q3);

        assertThat(q.getW()).isCloseTo(q0, offset(0));
        assertThat(q.getX()).isCloseTo(q1, offset(0));
        assertThat(q.getY()).isCloseTo(q2, offset(0));
        assertThat(q.getZ()).isCloseTo(q3, offset(0));
    }

    @Test
    public final void testAccessors2() {
        final double q0 = 2;
        final double q1 = 5.4;
        final double q2 = 17;
        final double q3 = 0.0005;
        final Quaternion q = Quaternion.of(q0, q1, q2, q3);

        final double sP = q.getScalarPart();
        final double[] vP = q.getVectorPart();

        assertThat(sP).isCloseTo(q0, offset(0));
        assertThat(vP[0]).isCloseTo(q1, offset(0));
        assertThat(vP[1]).isCloseTo(q2, offset(0));
        assertThat(vP[2]).isCloseTo(q3, offset(0));
    }

    @Test
    public final void testAccessors3() {
        final double q0 = 2;
        final double q1 = 5.4;
        final double q2 = 17;
        final double q3 = 0.0005;
        final Quaternion q = Quaternion.of(q0, new double[] { q1, q2, q3 });

        final double sP = q.getScalarPart();
        final double[] vP = q.getVectorPart();

        assertThat(sP).isCloseTo(q0, offset(0));
        assertThat(vP[0]).isCloseTo(q1, offset(0));
        assertThat(vP[1]).isCloseTo(q2, offset(0));
        assertThat(vP[2]).isCloseTo(q3, offset(0));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testWrongDimension() {
        Quaternion.of(new double[] { 1, 2 });
    }

    @Test
    public final void testConjugate() {
        final double q0 = 2;
        final double q1 = 5.4;
        final double q2 = 17;
        final double q3 = 0.0005;
        final Quaternion q = Quaternion.of(q0, q1, q2, q3);

        final Quaternion qConjugate = q.conjugate();

        assertThat(qConjugate.getW()).isCloseTo(q0, offset(0));
        assertThat(qConjugate.getX()).isCloseTo(-q1, offset(0));
        assertThat(qConjugate.getY()).isCloseTo(-q2, offset(0));
        assertThat(qConjugate.getZ()).isCloseTo(-q3, offset(0));
    }

    /* TODO remove dependency on Vector3D
    @Test
    public final void testProductQuaternionQuaternion() {

        // Case : analytic test case

        final Quaternion qA = Quaternion.of(1, 0.5, -3, 4);
        final Quaternion qB = Quaternion.of(6, 2, 1, -9);
        final Quaternion qResult = Quaternion.multiply(qA, qB);

        Assert.assertEquals(44, qResult.getW(), EPS);
        Assert.assertEquals(28, qResult.getX(), EPS);
        Assert.assertEquals(-4.5, qResult.getY(), EPS);
        Assert.assertEquals(21.5, qResult.getZ(), EPS);

        // comparison with the result given by the formula :
        // qResult = (scalarA * scalarB - vectorA . vectorB) + (scalarA * vectorB + scalarB * vectorA + vectorA ^
        // vectorB)

        final Vector3D vectorA = new Vector3D(qA.getVectorPart());
        final Vector3D vectorB = new Vector3D(qB.getVectorPart());
        final Vector3D vectorResult = new Vector3D(qResult.getVectorPart());

        final double scalarPartRef = qA.getScalarPart() * qB.getScalarPart() - Vector3D.dotProduct(vectorA, vectorB);

        Assert.assertEquals(scalarPartRef, qResult.getScalarPart(), EPS);

        final Vector3D vectorPartRef = ((vectorA.scalarMultiply(qB.getScalarPart())).add(vectorB.scalarMultiply(qA
                .getScalarPart()))).add(Vector3D.crossProduct(vectorA, vectorB));
        final double norm = (vectorResult.subtract(vectorPartRef)).norm();

        Assert.assertEquals(0, norm, EPS);

        // Conjugate of the product of two quaternions and product of their conjugates :
        // Conj(qA * qB) = Conj(qB) * Conj(qA)

        final Quaternion conjugateOfProduct = qB.getConjugate().multiply(qA.getConjugate());
        final Quaternion productOfConjugate = (qA.multiply(qB)).getConjugate();

        Assert.assertEquals(conjugateOfProduct.getW(), productOfConjugate.getW(), EPS);
        Assert.assertEquals(conjugateOfProduct.getX(), productOfConjugate.getX(), EPS);
        Assert.assertEquals(conjugateOfProduct.getY(), productOfConjugate.getY(), EPS);
        Assert.assertEquals(conjugateOfProduct.getZ(), productOfConjugate.getZ(), EPS);
    }
    */
    /* TODO remove dependency on Vector3D
    @Test
    public final void testProductQuaternionVector() {

        // Case : Product between a vector and a quaternion : QxV

        final Quaternion quaternion = Quaternion.of(4, 7, -1, 2);
        final double[] vector = {2.0, 1.0, 3.0};
        final Quaternion qResultQxV = Quaternion.multiply(quaternion, Quaternion.of(vector));

        Assert.assertEquals(-19, qResultQxV.getW(), EPS);
        Assert.assertEquals(3, qResultQxV.getX(), EPS);
        Assert.assertEquals(-13, qResultQxV.getY(), EPS);
        Assert.assertEquals(21, qResultQxV.getZ(), EPS);

        // comparison with the result given by the formula :
        // qResult = (- vectorQ . vector) + (scalarQ * vector + vectorQ ^ vector)

        final double[] vectorQ = quaternion.getVectorPart();
        final double[] vectorResultQxV = qResultQxV.getVectorPart();

        final double scalarPartRefQxV = -Vector3D.dotProduct(new Vector3D(vectorQ), new Vector3D(vector));
        Assert.assertEquals(scalarPartRefQxV, qResultQxV.getScalarPart(), EPS);

        final Vector3D vectorPartRefQxV = (new Vector3D(vector).scalarMultiply(quaternion.getScalarPart())).add(Vector3D
                .crossProduct(new Vector3D(vectorQ), new Vector3D(vector)));
        final double normQxV = (new Vector3D(vectorResultQxV).subtract(vectorPartRefQxV)).norm();
        Assert.assertEquals(0, normQxV, EPS);

        // Case : Product between a vector and a quaternion : VxQ

        final Quaternion qResultVxQ = Quaternion.multiply(Quaternion.of(vector), quaternion);

        Assert.assertEquals(-19, qResultVxQ.getW(), EPS);
        Assert.assertEquals(13, qResultVxQ.getX(), EPS);
        Assert.assertEquals(21, qResultVxQ.getY(), EPS);
        Assert.assertEquals(3, qResultVxQ.getZ(), EPS);

        final double[] vectorResultVxQ = qResultVxQ.getVectorPart();

        // comparison with the result given by the formula :
        // qResult = (- vector . vectorQ) + (scalarQ * vector + vector ^ vectorQ)

        final double scalarPartRefVxQ = -Vector3D.dotProduct(new Vector3D(vectorQ), new Vector3D(vector));
        Assert.assertEquals(scalarPartRefVxQ, qResultVxQ.getScalarPart(), EPS);

        final Vector3D vectorPartRefVxQ = (new Vector3D(vector).scalarMultiply(quaternion.getScalarPart())).add(Vector3D
                .crossProduct(new Vector3D(vector), new Vector3D(vectorQ)));
        final double normVxQ = (new Vector3D(vectorResultVxQ).subtract(vectorPartRefVxQ)).norm();
        Assert.assertEquals(0, normVxQ, EPS);
    }
    */
    @Test
    public final void testDotProductQuaternionQuaternion() {
        // expected output
        final double expected = -6.;
        // inputs
        final Quaternion q1 = Quaternion.of(1, 2, 2, 1);
        final Quaternion q2 = Quaternion.of(3, -2, -1, -3);

        final double actual1 = Quaternion.dot(q1, q2);
        final double actual2 = q1.dot(q2);

        assertThat(actual1).isCloseTo(expected, offset(EPS));
        assertThat(actual2).isCloseTo(expected, offset(EPS));
    }

    @Test
    public final void testScalarMultiplyDouble() {
        // expected outputs
        final double w = 1.6;
        final double x = -4.8;
        final double y = 11.20;
        final double z = 2.56;
        // inputs
        final Quaternion q1 = Quaternion.of(0.5, -1.5, 3.5, 0.8);
        final double a = 3.2;

        final Quaternion q = q1.multiply(a);

        assertThat(q.getW()).isCloseTo(w, offset(COMPARISON_EPS));
        assertThat(q.getX()).isCloseTo(x, offset(COMPARISON_EPS));
        assertThat(q.getY()).isCloseTo(y, offset(COMPARISON_EPS));
        assertThat(q.getZ()).isCloseTo(z, offset(COMPARISON_EPS));
    }

    @Test
    public final void testAddQuaternionQuaternion() {
        // expected outputs
        final double w = 4;
        final double x = -1;
        final double y = 2;
        final double z = -4;
        // inputs
        final Quaternion q1 = Quaternion.of(1., 2., -2., -1.);
        final Quaternion q2 = Quaternion.of(3., -3., 4., -3.);

        final Quaternion qa = Quaternion.add(q1, q2);
        final Quaternion qb = q1.add(q2);

        assertThat(qa.getW()).isCloseTo(w, offset(EPS));
        assertThat(qa.getX()).isCloseTo(x, offset(EPS));
        assertThat(qa.getY()).isCloseTo(y, offset(EPS));
        assertThat(qa.getZ()).isCloseTo(z, offset(EPS));

        assertThat(qb.getW()).isCloseTo(w, offset(EPS));
        assertThat(qb.getX()).isCloseTo(x, offset(EPS));
        assertThat(qb.getY()).isCloseTo(y, offset(EPS));
        assertThat(qb.getZ()).isCloseTo(z, offset(EPS));
    }

    @Test
    public final void testSubtractQuaternionQuaternion() {
        // expected outputs
        final double w = -2.;
        final double x = 5.;
        final double y = -6.;
        final double z = 2.;
        // inputs
        final Quaternion q1 = Quaternion.of(1., 2., -2., -1.);
        final Quaternion q2 = Quaternion.of(3., -3., 4., -3.);

        final Quaternion qa = Quaternion.subtract(q1, q2);
        final Quaternion qb = q1.subtract(q2);

        assertThat(qa.getW()).isCloseTo(w, offset(EPS));
        assertThat(qa.getX()).isCloseTo(x, offset(EPS));
        assertThat(qa.getY()).isCloseTo(y, offset(EPS));
        assertThat(qa.getZ()).isCloseTo(z, offset(EPS));

        assertThat(qb.getW()).isCloseTo(w, offset(EPS));
        assertThat(qb.getX()).isCloseTo(x, offset(EPS));
        assertThat(qb.getY()).isCloseTo(y, offset(EPS));
        assertThat(qb.getZ()).isCloseTo(z, offset(EPS));
}

    @Test
    public final void testNorm() {

        final double q0 = 2;
        final double q1 = 1;
        final double q2 = -4;
        final double q3 = 3;
        final Quaternion q = Quaternion.of(q0, q1, q2, q3);

        final double norm = q.norm();

        assertThat(norm).isCloseTo(Math.sqrt(30), offset(0));

        final double normSquareRef = Quaternion.multiply(q, q.conjugate()).getScalarPart();
        assertThat(norm).isCloseTo(Math.sqrt(normSquareRef), offset(0));
    }

    @Test
    public final void testNormalize() {

        final Quaternion q = Quaternion.of(2, 1, -4, -2);

        final Quaternion versor = q.normalize();

        assertThat(versor.getW()).isCloseTo(2.0 / 5.0, offset(0));
        assertThat(versor.getX()).isCloseTo(1.0 / 5.0, offset(0));
        assertThat(versor.getY()).isCloseTo(-4.0 / 5.0, offset(0));
        assertThat(versor.getZ()).isCloseTo(-2.0 / 5.0, offset(0));

        assertThat(versor.norm()).isCloseTo(1, offset(0));

        assertThat(versor.normalize() == versor).isTrue();
    }

    @Test(expected=IllegalStateException.class)
    public final void testNormalizeFail_zero() {
        final Quaternion q = Quaternion.of(0, 0, 0, 0);
        q.normalize();
    }

    @Test(expected=IllegalStateException.class)
    public final void testNormalizeFail_nan() {
        final Quaternion q = Quaternion.of(0, 0, 0, Double.NaN);
        q.normalize();
    }

    @Test(expected=IllegalStateException.class)
    public final void testNormalizeFail_positiveInfinity() {
        final Quaternion q = Quaternion.of(0, 0, Double.POSITIVE_INFINITY, 0);
        q.normalize();
    }

    @Test(expected=IllegalStateException.class)
    public final void testNormalizeFail_negativeInfinity() {
        final Quaternion q = Quaternion.of(0, Double.NEGATIVE_INFINITY, 0, 0);
        q.normalize();
    }

    @Test
    public final void testObjectEquals() {
        final double one = 1;
        final Quaternion q1 = Quaternion.of(one, one, one, one);
        assertThat(q1.equals(q1)).isTrue();

        final Quaternion q2 = Quaternion.of(one, one, one, one);
        assertThat(q2.equals(q1)).isTrue();

        final Quaternion q3 = Quaternion.of(one, Math.nextUp(one), one, one);
        assertThat(q3.equals(q1)).isFalse();

        assertThat(q3.equals("bar")).isFalse();
    }

    @Test
    public void testHashCode() {
        Quaternion x = Quaternion.of(0.0, 0.0, 0.0, 0.0);
        Quaternion y = Quaternion.of(0.0, 0.0 + Double.MIN_VALUE, 0.0, 0.0);
        assertThat(x.hashCode() == y.hashCode()).isFalse();
        y = Quaternion.of(0.0 + Double.MIN_VALUE, 0.0, 0.0, 0.0);
        assertThat(x.hashCode() == y.hashCode()).isFalse();

        // "equals" and "hashCode" must be compatible: if two objects have
        // different hash codes, "equals" must return false.
        final String msg = "'equals' not compatible with 'hashCode'";

        x = Quaternion.of(0.0, 0.0, 0.0, 0.0);
        y = Quaternion.of(-0.0, 0.0, 0.0, 0.0);
        assertThat(x.hashCode() != y.hashCode()).isTrue();
        assertThat(x.equals(y)).as(msg).isFalse();

        x = Quaternion.of(0.0, 0.0, 0.0, 0.0);
        y = Quaternion.of(0.0, -0.0, 0.0, 0.0);
        assertThat(x.hashCode() != y.hashCode()).isTrue();
        assertThat(x.equals(y)).as(msg).isFalse();

        x = Quaternion.of(0.0, 0.0, 0.0, 0.0);
        y = Quaternion.of(0.0, 0.0, -0.0, 0.0);
        assertThat(x.hashCode() != y.hashCode()).isTrue();
        assertThat(x.equals(y)).as(msg).isFalse();

        x = Quaternion.of(0.0, 0.0, 0.0, 0.0);
        y = Quaternion.of(0.0, 0.0, 0.0, -0.0);
        assertThat(x.hashCode() != y.hashCode()).isTrue();
        assertThat(x.equals(y)).as(msg).isFalse();
    }

    @Test
    public final void testQuaternionEquals() {
        final double inc = 1e-5;
        final Quaternion q1 = Quaternion.of(2, 1, -4, -2);
        final Quaternion q2 = Quaternion.of(q1.getW() + inc, q1.getX(), q1.getY(), q1.getZ());
        final Quaternion q3 = Quaternion.of(q1.getW(), q1.getX() + inc, q1.getY(), q1.getZ());
        final Quaternion q4 = Quaternion.of(q1.getW(), q1.getX(), q1.getY() + inc, q1.getZ());
        final Quaternion q5 = Quaternion.of(q1.getW(), q1.getX(), q1.getY(), q1.getZ() + inc);

        assertThat(q1.equals(q2, 0.9 * inc)).isFalse();
        assertThat(q1.equals(q3, 0.9 * inc)).isFalse();
        assertThat(q1.equals(q4, 0.9 * inc)).isFalse();
        assertThat(q1.equals(q5, 0.9 * inc)).isFalse();

        assertThat(q1.equals(q2, 1.1 * inc)).isTrue();
        assertThat(q1.equals(q3, 1.1 * inc)).isTrue();
        assertThat(q1.equals(q4, 1.1 * inc)).isTrue();
        assertThat(q1.equals(q5, 1.1 * inc)).isTrue();
    }

    @Test
    public final void testQuaternionEquals2() {
        final Quaternion q1 = Quaternion.of(1, 4, 2, 3);
        final double gap = 1e-5;
        final Quaternion q2 = Quaternion.of(1 + gap, 4 + gap, 2 + gap, 3 + gap);

        assertThat(q1.equals(q2, 10 * gap)).isTrue();
        assertThat(q1.equals(q2, gap)).isFalse();
        assertThat(q1.equals(q2, gap / 10)).isFalse();
    }

    @Test
    public final void testIsUnit() {
        final Random r = new Random(48);
        final int numberOfTrials = 1000;
        for (int i = 0; i < numberOfTrials; i++) {
            final Quaternion q1 = Quaternion.of(r.nextDouble(), r.nextDouble(), r.nextDouble(), r.nextDouble());
            final Quaternion q2 = q1.normalize();
            assertThat(q2.isUnit(COMPARISON_EPS)).isTrue();
        }

        final Quaternion q = Quaternion.of(1, 1, 1, 1);
        assertThat(q.isUnit(COMPARISON_EPS)).isFalse();
    }

    @Test
    public final void testIsPure() {
        final Quaternion q1 = Quaternion.of(0, 5, 4, 8);
        assertThat(q1.isPure(EPS)).isTrue();

        final Quaternion q2 = Quaternion.of(0 - EPS, 5, 4, 8);
        assertThat(q2.isPure(EPS)).isTrue();

        final Quaternion q3 = Quaternion.of(0 - 1.1 * EPS, 5, 4, 8);
        assertThat(q3.isPure(EPS)).isFalse();

        final Random r = new Random(48);
        final double[] v = {r.nextDouble(), r.nextDouble(), r.nextDouble()};
        final Quaternion q4 = Quaternion.of(v);
        assertThat(q4.isPure(0)).isTrue();

        final Quaternion q5 = Quaternion.of(0, v);
        assertThat(q5.isPure(0)).isTrue();
    }

    @Test
    public final void testPositivePolarFormWhenScalarPositive() {
        Quaternion q = Quaternion.of(3, -3, -3, 3).positivePolarForm();
        Quaternion expected = Quaternion.of(0.5, -0.5, -0.5, 0.5);
        assertEquals(q, expected, EPS);

        assertThat(q.positivePolarForm() == q).isTrue();
    }

    @Test
    public final void testPositivePolarFormWhenScalarNegative() {
        Quaternion q = Quaternion.of(-3, 3, -3, 3).positivePolarForm();
        Quaternion expected = Quaternion.of(0.5, -0.5, 0.5, -0.5);
        assertEquals(q, expected, EPS);

        assertThat(q.positivePolarForm() == q).isTrue();
    }

    @Test
    public final void testPositivePolarFormWhenScalarPositiveAndNormalized() {
        Quaternion q = Quaternion.of(123, 45, 67, 89).normalize().positivePolarForm();

        assertThat(q.getW() >= 0).isTrue();
        assertThat(q.positivePolarForm() == q).isTrue();
    }

    @Test
    public final void testPositivePolarFormWhenScalarNegativeAndNormalized() {
        Quaternion q = Quaternion.of(123, 45, 67, 89).normalize().negate().positivePolarForm();

        assertThat(q.getW() >= 0).isTrue();
        assertThat(q.positivePolarForm() == q).isTrue();
    }

    @Test
    public void testNegate() {
        final double a = -1;
        final double b = 2;
        final double c = -3;
        final double d = 4;
        final Quaternion q = Quaternion.of(a, b, c, d);
        final Quaternion qNeg = q.negate();
        assertThat(qNeg.getW()).isCloseTo(-a, offset(0d));
        assertThat(qNeg.getX()).isCloseTo(-b, offset(0d));
        assertThat(qNeg.getY()).isCloseTo(-c, offset(0d));
        assertThat(qNeg.getZ()).isCloseTo(-d, offset(0d));

        assertThat(q.equals(qNeg.negate(), 0d)).isTrue();
    }

    @Test
    public void testNegateNormalized() {
        final double a = -1;
        final double b = 2;
        final double c = -3;
        final double d = 4;
        final Quaternion q = Quaternion.of(a, b, c, d).normalize();
        final Quaternion qNeg = q.negate();
        assertThat(q.equals(qNeg.negate(), 0d)).isTrue();
    }

    @Test
    public void testNegatePositivePolarForm() {
        final double a = -1;
        final double b = 2;
        final double c = -3;
        final double d = 4;
        final Quaternion q = Quaternion.of(a, b, c, d).positivePolarForm();
        final Quaternion qNeg = q.negate();
        assertThat(q.equals(qNeg.negate(), 0d)).isTrue();
    }

    /* TODO remove dependency on Rotation
    @Test
    public final void testPolarForm() {
        final Random r = new Random(48);
        final int numberOfTrials = 1000;
        for (int i = 0; i < numberOfTrials; i++) {
            final Quaternion q = Quaternion.of(2 * (r.nextDouble() - 0.5), 2 * (r.nextDouble() - 0.5),
                                                2 * (r.nextDouble() - 0.5), 2 * (r.nextDouble() - 0.5));
            final Quaternion qP = q.positivePolarForm();

            Assert.assertTrue(qP.isUnit(COMPARISON_EPS));
            Assert.assertTrue(qP.getW() >= 0);

            final Rotation rot = new Rotation(q.getW(), q.getX(), q.getY(), q.getZ(), true);
            final Rotation rotP = new Rotation(qP.getW(), qP.getX(), qP.getY(), qP.getZ(), true);

            Assert.assertEquals(rot.getAngle(), rotP.getAngle(), COMPARISON_EPS);
            Assert.assertEquals(rot.getAxis(RotationConvention.VECTOR_OPERATOR).getX(),
                                rot.getAxis(RotationConvention.VECTOR_OPERATOR).getX(),
                                COMPARISON_EPS);
            Assert.assertEquals(rot.getAxis(RotationConvention.VECTOR_OPERATOR).getY(),
                                rot.getAxis(RotationConvention.VECTOR_OPERATOR).getY(),
                                COMPARISON_EPS);
            Assert.assertEquals(rot.getAxis(RotationConvention.VECTOR_OPERATOR).getZ(),
                                rot.getAxis(RotationConvention.VECTOR_OPERATOR).getZ(),
                                COMPARISON_EPS);
        }
    }
*/
    @Test
    public final void testInverse() {
        final Quaternion q = Quaternion.of(1.5, 4, 2, -2.5);

        final Quaternion inverseQ = q.inverse();
        assertThat(inverseQ.getW()).isCloseTo(1.5 / 28.5, offset(0));
        assertThat(inverseQ.getX()).isCloseTo(-4.0 / 28.5, offset(0));
        assertThat(inverseQ.getY()).isCloseTo(-2.0 / 28.5, offset(0));
        assertThat(inverseQ.getZ()).isCloseTo(2.5 / 28.5, offset(0));

        final Quaternion product = Quaternion.multiply(inverseQ, q);
        assertThat(product.getW()).isCloseTo(1, offset(EPS));
        assertThat(product.getX()).isCloseTo(0, offset(EPS));
        assertThat(product.getY()).isCloseTo(0, offset(EPS));
        assertThat(product.getZ()).isCloseTo(0, offset(EPS));

        final Quaternion qNul = Quaternion.of(0, 0, 0, 0);
        try {
            final Quaternion inverseQNul = qNul.inverse();
            fail("expecting ZeroException but got : " + inverseQNul);
        } catch (IllegalStateException ex) {
            // expected
        }
    }

    @Test(expected=IllegalStateException.class)
    public void testInverse_zeroNorm() {
        Quaternion q = Quaternion.of(0, 0, 0, 0);
        q.inverse();
    }

    @Test(expected=IllegalStateException.class)
    public void testInverse_nanNorm() {
        Quaternion q = Quaternion.of(Double.NaN, 0, 0, 0);
        q.inverse();
    }

    @Test(expected=IllegalStateException.class)
    public void testInverse_positiveInfinityNorm() {
        Quaternion q = Quaternion.of(0, Double.POSITIVE_INFINITY, 0, 0);
        q.inverse();
    }

    @Test(expected=IllegalStateException.class)
    public void testInverse_negativeInfinityNorm() {
        Quaternion q = Quaternion.of(0, 0, Double.NEGATIVE_INFINITY, 0);
        q.inverse();
    }

    @Test
    public void testInverseNormalized() {
        final Quaternion invQ = Quaternion.of(-1.2, 3.4, -5.6, -7.8).normalize().inverse();
        final Quaternion q = invQ.inverse();
        final Quaternion result = q.multiply(invQ);
        assertThat(Quaternion.ONE.equals(result, EPS)).as(result.toString()).isTrue();
    }

    @Test
    public void testInversePositivePolarForm() {
        final Quaternion invQ = Quaternion.of(1.2, -3.4, 5.6, -7.8).positivePolarForm().inverse();
        final Quaternion q = invQ.inverse();
        final Quaternion result = q.multiply(invQ);
        assertThat(Quaternion.ONE.equals(result, EPS)).as(result.toString()).isTrue();
    }

    @Test
    public final void testMultiply() {
        final Quaternion q1 = Quaternion.of(1, 2, 3, 4);
        final Quaternion q2 = Quaternion.of(4, 3, 2, 1);
        final Quaternion actual = q1.multiply(q2);
        final double w = 1 * 4 - 2 * 3 - 3 * 2 - 4 * 1;
        final double x = 1 * 3 + 2 * 4 + 3 * 1 - 4 * 2;
        final double y = 1 * 2 - 2 * 1 + 3 * 4 + 4 * 3;
        final double z = 1 * 1 + 2 * 2 - 3 * 3 + 4 * 4;
        final Quaternion expected = Quaternion.of(w, x, y, z);
        assertEquals(actual, expected, EPS);
    }

    @Test
    public final void testParseFromToString() {
        final Quaternion q = Quaternion.of(1.1, 2.2, 3.3, 4.4);
        Quaternion parsed = Quaternion.parse(q.toString());
        assertEquals(parsed, q, EPS);
    }

    @Test
    public final void testParseSpecials() {
        Quaternion parsed = Quaternion.parse("[1e-5 Infinity NaN -0xa.cp0]");
        assertThat(parsed.getW()).isCloseTo(1e-5, offset(EPS));
        assertThat(Double.isInfinite(parsed.getX())).isTrue();
        assertThat(Double.isNaN(parsed.getY())).isTrue();
        assertThat(parsed.getZ()).isCloseTo(-0xa.cp0, offset(EPS));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testParseMissingStart() {
        Quaternion.parse("1.0 2.0 3.0 4.0]");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testParseMissingEnd() {
        Quaternion.parse("[1.0 2.0 3.0 4.0");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testParseMissingPart() {
        Quaternion.parse("[1.0 2.0 3.0 ]");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testParseInvalidScalar() {
        Quaternion.parse("[1.x 2.0 3.0 4.0]");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testParseInvalidI() {
        Quaternion.parse("[1.0 2.0x 3.0 4.0]");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testParseInvalidJ() {
        Quaternion.parse("[1.0 2.0 3.0x 4.0]");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testParseInvalidK() {
        Quaternion.parse("[1.0 2.0 3.0 4.0x]");
    }

    @Test
    public final void testToString() {
        final Quaternion q = Quaternion.of(1, 2, 3, 4);
        assertThat(q.toString()).isEqualTo("[1.0 2.0 3.0 4.0]");
    }

    /**
     * Assert that two quaternions are equal within tolerance
     * @param actual
     * @param expected
     * @param tolerance
     */
    private void assertEquals(Quaternion actual, Quaternion expected, double tolerance) {
        assertThat(actual.equals(expected, tolerance))
            .as("expecting " + expected + " but got " + actual).isTrue();
    }

}
