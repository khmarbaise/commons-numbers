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

package org.apache.commons.numbers.complex;

import java.util.List;

import org.apache.commons.numbers.complex.Complex;
import org.junit.Assert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.data.Offset.offset;

import org.assertj.core.data.Offset;
import org.junit.Ignore;
import org.junit.Test;


/**
 */
public class ComplexTest {

    private static final double inf = Double.POSITIVE_INFINITY;
    private static final double neginf = Double.NEGATIVE_INFINITY;
    private static final double nan = Double.NaN;
    private static final double pi = Math.PI;
    private static final Complex oneInf = Complex.ofCartesian(1, inf);
    private static final Complex oneNegInf = Complex.ofCartesian(1, neginf);
    private static final Complex infOne = Complex.ofCartesian(inf, 1);
    private static final Complex infZero = Complex.ofCartesian(inf, 0);
    private static final Complex infNaN = Complex.ofCartesian(inf, nan);
    private static final Complex infNegInf = Complex.ofCartesian(inf, neginf);
    private static final Complex infInf = Complex.ofCartesian(inf, inf);
    private static final Complex negInfInf = Complex.ofCartesian(neginf, inf);
    private static final Complex negInfZero = Complex.ofCartesian(neginf, 0);
    private static final Complex negInfOne = Complex.ofCartesian(neginf, 1);
    private static final Complex negInfNaN = Complex.ofCartesian(neginf, nan);
    private static final Complex negInfNegInf = Complex.ofCartesian(neginf, neginf);
    private static final Complex oneNaN = Complex.ofCartesian(1, nan);
    private static final Complex zeroInf = Complex.ofCartesian(0, inf);
    private static final Complex zeroNaN = Complex.ofCartesian(0, nan);
    private static final Complex nanInf = Complex.ofCartesian(nan, inf);
    private static final Complex nanNegInf = Complex.ofCartesian(nan, neginf);
    private static final Complex nanZero = Complex.ofCartesian(nan, 0);
    private static final Complex NAN = Complex.ofCartesian(nan, nan);

    @Test
    public void testConstructor() {
        Complex z = Complex.ofCartesian(3.0, 4.0);
        assertThat(z.getReal()).isCloseTo(3.0, offset(1.0e-5));
        assertThat(z.getImaginary()).isCloseTo(4.0, offset(1.0e-5));
    }

    @Test
    public void testConstructorNaN() {
        Complex z = Complex.ofCartesian(3.0, Double.NaN);
        assertThat(z.isNaN()).isTrue();

        z = Complex.ofCartesian(nan, 4.0);
        assertThat(z.isNaN()).isTrue();

        z = Complex.ofCartesian(3.0, 4.0);
        assertThat(z.isNaN()).isFalse();
    }

    @Test
    public void testAbs() {
        Complex z = Complex.ofCartesian(3.0, 4.0);
        
				Assert.assertEquals(5.0, z.abs(), 1.0e-5);
        assertThat(z.abs()).isCloseTo(5.0, offset(1.0e-5));

        assertThat(z.abs()).isEqualTo(5.0, Offset.offset(1.0e-5));
    }

    @Test
    public void testAbsNaN() {
        assertThat(Double.isNaN(NAN.abs())).isTrue();
        Complex z = Complex.ofCartesian(inf, nan);
        Assert.assertTrue(Double.isNaN(z.abs()));
        assertThat(Double.isNaN(z.abs())).isTrue();

        assertThat(z.abs()).isNaN();
    }

    @Test
    public void testAdd() {
        Complex x = Complex.ofCartesian(3.0, 4.0);
        Complex y = Complex.ofCartesian(5.0, 6.0);
        Complex z = x.add(y);
        Assert.assertEquals(8.0, z.getReal(), 1.0e-5);
        Assert.assertEquals(10.0, z.getImaginary(), 1.0e-5);
        assertThat(z.getReal()).isCloseTo(8.0, offset(1.0e-5));
        assertThat(z.getImaginary()).isCloseTo(10.0, offset(1.0e-5));

        assertThat(z.getReal()).isEqualTo(8.0, Offset.offset(1.0e-19));
        assertThat(z.getImaginary()).isEqualTo(10.0, Offset.offset(1.0e-19));
    }

    @Test
    public void testAddInf() {
        Complex x = Complex.ofCartesian(1, 1);
        Complex z = Complex.ofCartesian(inf, 0);
        Complex w = x.add(z);
        assertThat(w.getImaginary()).isCloseTo(1d, offset(0d));
        assertThat(w.getReal()).isCloseTo(inf, offset(0d));

        x = Complex.ofCartesian(neginf, 0);
        assertThat(Double.isNaN(x.add(z).getReal())).isTrue();
    }


    @Test
    public void testScalarAdd() {
        Complex x = Complex.ofCartesian(3.0, 4.0);
        double yDouble = 2.0;
        Complex yComplex = Complex.ofReal(yDouble);
        assertThat(x.add(yDouble)).isEqualTo(x.add(yComplex));
    }

    @Test
    public void testScalarAddNaN() {
        Complex x = Complex.ofCartesian(3.0, 4.0);
        double yDouble = Double.NaN;
        Complex yComplex = Complex.ofReal(yDouble);
        assertThat(x.add(yDouble)).isEqualTo(x.add(yComplex));
    }

    @Test
    public void testScalarAddInf() {
        Complex x = Complex.ofCartesian(1, 1);
        double yDouble = Double.POSITIVE_INFINITY;

        Complex yComplex = Complex.ofReal(yDouble);
        assertThat(x.add(yDouble)).isEqualTo(x.add(yComplex));

        x = Complex.ofCartesian(neginf, 0);
        assertThat(x.add(yDouble)).isEqualTo(x.add(yComplex));
    }

    @Test
    public void testConjugate() {
        Complex x = Complex.ofCartesian(3.0, 4.0);
        Complex z = x.conjugate();
        assertThat(z.getReal()).isCloseTo(3.0, offset(1.0e-5));
        assertThat(z.getImaginary()).isCloseTo(-4.0, offset(1.0e-5));
    }

    @Test
    public void testConjugateNaN() {
        Complex z = NAN.conjugate();
        assertThat(z.isNaN()).isTrue();
    }

    @Test
    public void testConjugateInfiinite() {
        Complex z = Complex.ofCartesian(0, inf);
        assertThat(z.conjugate().getImaginary()).isCloseTo(neginf, offset(0d));
        z = Complex.ofCartesian(0, neginf);
        assertThat(z.conjugate().getImaginary()).isCloseTo(inf, offset(0d));
    }

    @Test
    public void testDivide() {
        Complex x = Complex.ofCartesian(3.0, 4.0);
        Complex y = Complex.ofCartesian(5.0, 6.0);
        Complex z = x.divide(y);
        assertThat(z.getReal()).isCloseTo(39.0 / 61.0, offset(1.0e-5));
        assertThat(z.getImaginary()).isCloseTo(2.0 / 61.0, offset(1.0e-5));
    }

    @Test
    public void testDivideReal() {
        Complex x = Complex.ofCartesian(2d, 3d);
        Complex y = Complex.ofCartesian(2d, 0d);
        assertThat(x.divide(y)).isEqualTo(Complex.ofCartesian(1d, 1.5));

    }

    @Test
    public void testDivideImaginary() {
        Complex x = Complex.ofCartesian(2d, 3d);
        Complex y = Complex.ofCartesian(0d, 2d);
        assertThat(x.divide(y)).isEqualTo(Complex.ofCartesian(1.5d, -1d));
    }

    @Test
    public void testDivideZero() {
        Complex x = Complex.ofCartesian(3.0, 4.0);
        Complex z = x.divide(Complex.ZERO);
        assertThat(Complex.INF).isEqualTo(z);
    }

    @Test
    public void testDivideZeroZero() {
        Complex x = Complex.ofCartesian(0.0, 0.0);
        Complex z = x.divide(Complex.ZERO);
        assertThat(NAN).isEqualTo(z);
    }

    @Test
    public void testDivideNaN() {
        Complex x = Complex.ofCartesian(3.0, 4.0);
        Complex z = x.divide(NAN);
        assertThat(z.isNaN()).isTrue();
    }

    @Test
    public void testDivideNaNInf() {
       Complex z = oneInf.divide(Complex.ONE);
       assertThat(Double.isNaN(z.getReal())).isTrue();
       assertThat(z.getImaginary()).isCloseTo(inf, offset(0d));

       z = negInfNegInf.divide(oneNaN);
       assertThat(Double.isNaN(z.getReal())).isTrue();
       assertThat(Double.isNaN(z.getImaginary())).isTrue();

       z = negInfInf.divide(Complex.ONE);
       assertThat(Double.isInfinite(z.getReal())).isTrue();
       assertThat(Double.isInfinite(z.getImaginary())).isTrue();
    }

    @Test
    public void testScalarDivide() {
        Complex x = Complex.ofCartesian(3.0, 4.0);
        double yDouble = 2.0;
        Complex yComplex = Complex.ofReal(yDouble);
        assertThat(x.divide(yDouble)).isEqualTo(x.divide(yComplex));
    }

    @Test
    public void testScalarDivideNaN() {
        Complex x = Complex.ofCartesian(3.0, 4.0);
        double yDouble = Double.NaN;
        Complex yComplex = Complex.ofReal(yDouble);
        assertThat(x.divide(yDouble)).isEqualTo(x.divide(yComplex));
    }

    @Test
    public void testScalarDivideZero() {
        Complex x = Complex.ofCartesian(1,1);
        TestUtils.assertEquals(x.divide(Complex.ZERO), x.divide(0), 0);
    }

    @Test
    public void testReciprocal() {
        Complex z = Complex.ofCartesian(5.0, 6.0);
        Complex act = z.reciprocal();
        double expRe = 5.0 / 61.0;
        double expIm = -6.0 / 61.0;
        assertThat(act.getReal()).isCloseTo(expRe, offset(Math.ulp(expRe)));
        assertThat(act.getImaginary()).isCloseTo(expIm, offset(Math.ulp(expIm)));
    }

    @Test
    public void testReciprocalReciprocal() {
        Complex z = Complex.ofCartesian(5.0, 6.0);
        Complex zRR = z.reciprocal().reciprocal();
        final double tol = 1e-14;
        assertThat(z.getReal()).isCloseTo(zRR.getReal(), offset(tol));
        assertThat(z.getImaginary()).isCloseTo(zRR.getImaginary(), offset(tol));
    }

    @Test
    public void testReciprocalReal() {
        Complex z = Complex.ofCartesian(-2.0, 0.0);
        assertThat(Complex.equals(Complex.ofCartesian(-0.5, 0.0), z.reciprocal())).isTrue();
    }

    @Test
    public void testReciprocalImaginary() {
        Complex z = Complex.ofCartesian(0.0, -2.0);
        assertThat(z.reciprocal()).isEqualTo(Complex.ofCartesian(0.0, 0.5));
    }

    @Test
    public void testReciprocalNaN() {
        assertThat(NAN.reciprocal().isNaN()).isTrue();
    }

    @Test
    public void testMultiply() {
        Complex x = Complex.ofCartesian(3.0, 4.0);
        Complex y = Complex.ofCartesian(5.0, 6.0);
        Complex z = x.multiply(y);
        assertThat(z.getReal()).isCloseTo(-9.0, offset(1.0e-5));
        assertThat(z.getImaginary()).isCloseTo(38.0, offset(1.0e-5));
    }

    @Test
    public void testMultiplyInfInf() {
        // Assert.assertTrue(infInf.multiply(infInf).isNaN()); // MATH-620
        assertThat(infInf.multiply(infInf).isInfinite()).isTrue();
    }

    @Test
    public void testScalarMultiply() {
        Complex x = Complex.ofCartesian(3.0, 4.0);
        double yDouble = 2.0;
        Complex yComplex = Complex.ofReal(yDouble);
        assertThat(x.multiply(yDouble)).isEqualTo(x.multiply(yComplex));
        int zInt = -5;
        Complex zComplex = Complex.ofReal(zInt);
        assertThat(x.multiply(zInt)).isEqualTo(x.multiply(zComplex));
    }

    @Test
    public void testScalarMultiplyNaN() {
        Complex x = Complex.ofCartesian(3.0, 4.0);
        double yDouble = Double.NaN;
        Complex yComplex = Complex.ofReal(yDouble);
        assertThat(x.multiply(yDouble)).isEqualTo(x.multiply(yComplex));
    }

    @Test
    public void testScalarMultiplyInf() {
        Complex x = Complex.ofCartesian(1, 1);
        double yDouble = Double.POSITIVE_INFINITY;
        Complex yComplex = Complex.ofReal(yDouble);
        assertThat(x.multiply(yDouble)).isEqualTo(x.multiply(yComplex));

        yDouble = Double.NEGATIVE_INFINITY;
        yComplex = Complex.ofReal(yDouble);
        assertThat(x.multiply(yDouble)).isEqualTo(x.multiply(yComplex));
    }

    @Test
    public void testNegate() {
        Complex x = Complex.ofCartesian(3.0, 4.0);
        Complex z = x.negate();
        assertThat(z.getReal()).isCloseTo(-3.0, offset(1.0e-5));
        assertThat(z.getImaginary()).isCloseTo(-4.0, offset(1.0e-5));
    }

    @Test
    public void testNegateNaN() {
        Complex z = NAN.negate();
        assertThat(z.isNaN()).isTrue();
    }

    @Test
    public void testSubtract() {
        Complex x = Complex.ofCartesian(3.0, 4.0);
        Complex y = Complex.ofCartesian(5.0, 6.0);
        Complex z = x.subtract(y);
        assertThat(z.getReal()).isCloseTo(-2.0, offset(1.0e-5));
        assertThat(z.getImaginary()).isCloseTo(-2.0, offset(1.0e-5));
    }

    @Test
    public void testSubtractInf() {
        Complex x = Complex.ofCartesian(1, 1);
        Complex z = Complex.ofCartesian(neginf, 0);
        Complex w = x.subtract(z);
        assertThat(w.getImaginary()).isCloseTo(1, offset(0d));
        assertThat(w.getReal()).isCloseTo(inf, offset(0d));

        x = Complex.ofCartesian(neginf, 0);
        assertThat(Double.isNaN(x.subtract(z).getReal())).isTrue();
    }

    @Test
    public void testScalarSubtract() {
        Complex x = Complex.ofCartesian(3.0, 4.0);
        double yDouble = 2.0;
        Complex yComplex = Complex.ofReal(yDouble);
        assertThat(x.subtract(yDouble)).isEqualTo(x.subtract(yComplex));
    }

    @Test
    public void testScalarSubtractNaN() {
        Complex x = Complex.ofCartesian(3.0, 4.0);
        double yDouble = Double.NaN;
        Complex yComplex = Complex.ofReal(yDouble);
        assertThat(x.subtract(yDouble)).isEqualTo(x.subtract(yComplex));
    }

    @Test
    public void testScalarSubtractInf() {
        Complex x = Complex.ofCartesian(1, 1);
        double yDouble = Double.POSITIVE_INFINITY;
        Complex yComplex = Complex.ofReal(yDouble);
        assertThat(x.subtract(yDouble)).isEqualTo(x.subtract(yComplex));

        x = Complex.ofCartesian(neginf, 0);
        assertThat(x.subtract(yDouble)).isEqualTo(x.subtract(yComplex));
    }


    @Test
    public void testEqualsNull() {
        Complex x = Complex.ofCartesian(3.0, 4.0);
        assertThat(x.equals(null)).isFalse();
    }

    @Test(expected=NullPointerException.class)
    public void testFloatingPointEqualsPrecondition1() {
        Complex.equals(Complex.ofCartesian(3.0, 4.0), null, 3);
    }
    @Test(expected=NullPointerException.class)
    public void testFloatingPointEqualsPrecondition2() {
        Complex.equals(null, Complex.ofCartesian(3.0, 4.0), 3);
    }

    @Test
    public void testEqualsClass() {
        Complex x = Complex.ofCartesian(3.0, 4.0);
        assertThat(x.equals(this)).isFalse();
    }

    @Test
    public void testEqualsSame() {
        Complex x = Complex.ofCartesian(3.0, 4.0);
        assertThat(x.equals(x)).isTrue();
    }

    @Test
    public void testFloatingPointEquals() {
        double re = -3.21;
        double im = 456789e10;

        final Complex x = Complex.ofCartesian(re, im);
        Complex y = Complex.ofCartesian(re, im);

        assertThat(x.equals(y)).isTrue();
        assertThat(Complex.equals(x, y)).isTrue();

        final int maxUlps = 5;
        for (int i = 0; i < maxUlps; i++) {
            re = Math.nextUp(re);
            im = Math.nextUp(im);
        }
        y = Complex.ofCartesian(re, im);
        assertThat(Complex.equals(x, y, maxUlps)).isTrue();

        re = Math.nextUp(re);
        im = Math.nextUp(im);
        y = Complex.ofCartesian(re, im);
        assertThat(Complex.equals(x, y, maxUlps)).isFalse();
    }

    @Test
    public void testFloatingPointEqualsNaN() {
        Complex c = Complex.ofCartesian(Double.NaN, 1);
        assertThat(Complex.equals(c, c)).isFalse();

        c = Complex.ofCartesian(1, Double.NaN);
        assertThat(Complex.equals(c, c)).isFalse();
    }

    @Test
    public void testFloatingPointEqualsWithAllowedDelta() {
        final double re = 153.0000;
        final double im = 152.9375;
        final double tol1 = 0.0625;
        final Complex x = Complex.ofCartesian(re, im);
        final Complex y = Complex.ofCartesian(re + tol1, im + tol1);
        assertThat(Complex.equals(x, y, tol1)).isTrue();

        final double tol2 = 0.0624;
        assertThat(Complex.equals(x, y, tol2)).isFalse();
    }

    @Test
    public void testFloatingPointEqualsWithAllowedDeltaNaN() {
        final Complex x = Complex.ofCartesian(0, Double.NaN);
        final Complex y = Complex.ofCartesian(Double.NaN, 0);
        assertThat(Complex.equals(x, Complex.ZERO, 0.1)).isFalse();
        assertThat(Complex.equals(x, x, 0.1)).isFalse();
        assertThat(Complex.equals(x, y, 0.1)).isFalse();
    }

    @Test
    public void testFloatingPointEqualsWithRelativeTolerance() {
        final double tol = 1e-4;
        final double re = 1;
        final double im = 1e10;

        final double f = 1 + tol;
        final Complex x = Complex.ofCartesian(re, im);
        final Complex y = Complex.ofCartesian(re * f, im * f);
        assertThat(Complex.equalsWithRelativeTolerance(x, y, tol)).isTrue();
    }

    @Test
    public void testFloatingPointEqualsWithRelativeToleranceNaN() {
        final Complex x = Complex.ofCartesian(0, Double.NaN);
        final Complex y = Complex.ofCartesian(Double.NaN, 0);
        assertThat(Complex.equalsWithRelativeTolerance(x, Complex.ZERO, 0.1)).isFalse();
        assertThat(Complex.equalsWithRelativeTolerance(x, x, 0.1)).isFalse();
        assertThat(Complex.equalsWithRelativeTolerance(x, y, 0.1)).isFalse();
    }

    @Test
    public void testEqualsTrue() {
        Complex x = Complex.ofCartesian(3.0, 4.0);
        Complex y = Complex.ofCartesian(3.0, 4.0);
        assertThat(x.equals(y)).isTrue();
    }

    @Test
    public void testEqualsRealDifference() {
        Complex x = Complex.ofCartesian(0.0, 0.0);
        Complex y = Complex.ofCartesian(0.0 + Double.MIN_VALUE, 0.0);
        assertThat(x.equals(y)).isFalse();
    }

    @Test
    public void testEqualsImaginaryDifference() {
        Complex x = Complex.ofCartesian(0.0, 0.0);
        Complex y = Complex.ofCartesian(0.0, 0.0 + Double.MIN_VALUE);
        assertThat(x.equals(y)).isFalse();
    }

    @Test
    public void testHashCode() {
        Complex x = Complex.ofCartesian(0.0, 0.0);
        Complex y = Complex.ofCartesian(0.0, 0.0 + Double.MIN_VALUE);
        assertThat(x.hashCode() == y.hashCode()).isFalse();
        y = Complex.ofCartesian(0.0 + Double.MIN_VALUE, 0.0);
        assertThat(x.hashCode() == y.hashCode()).isFalse();
        Complex realNaN = Complex.ofCartesian(Double.NaN, 0.0);
        Complex imaginaryNaN = Complex.ofCartesian(0.0, Double.NaN);
        assertThat(imaginaryNaN.hashCode()).isEqualTo(realNaN.hashCode());
        assertThat(NAN.hashCode()).isEqualTo(imaginaryNaN.hashCode());

        // MATH-1118
        // "equals" and "hashCode" must be compatible: if two objects have
        // different hash codes, "equals" must return false.
        final String msg = "'equals' not compatible with 'hashCode'";

        x = Complex.ofCartesian(0.0, 0.0);
        y = Complex.ofCartesian(0.0, -0.0);
        assertThat(x.hashCode() != y.hashCode()).isTrue();
        assertThat(x.equals(y)).as(msg).isFalse();

        x = Complex.ofCartesian(0.0, 0.0);
        y = Complex.ofCartesian(-0.0, 0.0);
        assertThat(x.hashCode() != y.hashCode()).isTrue();
        assertThat(x.equals(y)).as(msg).isFalse();
    }

    @Test
    @Ignore
    public void testJava() {// TODO more debug
        System.out.println(">>testJava()");
        // MathTest#testExpSpecialCases() checks the following:
        // Assert.assertEquals("exp of -infinity should be 0.0", 0.0, Math.exp(Double.NEGATIVE_INFINITY), Precision.EPSILON);
        // Let's check how well Math works:
        System.out.println("Math.exp="+Math.exp(Double.NEGATIVE_INFINITY));
        String props[] = {
        "java.version", //    Java Runtime Environment version
        "java.vendor", // Java Runtime Environment vendor
        "java.vm.specification.version", //   Java Virtual Machine specification version
        "java.vm.specification.vendor", //    Java Virtual Machine specification vendor
        "java.vm.specification.name", //  Java Virtual Machine specification name
        "java.vm.version", // Java Virtual Machine implementation version
        "java.vm.vendor", //  Java Virtual Machine implementation vendor
        "java.vm.name", //    Java Virtual Machine implementation name
        "java.specification.version", //  Java Runtime Environment specification version
        "java.specification.vendor", //   Java Runtime Environment specification vendor
        "java.specification.name", // Java Runtime Environment specification name
        "java.class.version", //  Java class format version number
        };
        for(String t : props) {
            System.out.println(t + "=" + System.getProperty(t));
        }
        System.out.println("<<testJava()");
    }


    @Test
    public void testScalarPow() {
        Complex x = Complex.ofCartesian(3, 4);
        double yDouble = 5.0;
        Complex yComplex = Complex.ofReal(yDouble);
        assertThat(x.pow(yDouble)).isEqualTo(x.pow(yComplex));
    }

    @Test
    public void testScalarPowNaNBase() {
        Complex x = NAN;
        double yDouble = 5.0;
        Complex yComplex = Complex.ofReal(yDouble);
        assertThat(x.pow(yDouble)).isEqualTo(x.pow(yComplex));
    }

    @Test
    public void testScalarPowNaNExponent() {
        Complex x = Complex.ofCartesian(3, 4);
        double yDouble = Double.NaN;
        Complex yComplex = Complex.ofReal(yDouble);
        assertThat(x.pow(yDouble)).isEqualTo(x.pow(yComplex));
    }
    @Test
    public void testSqrtPolar() {
        final double tol = 1e-12;
        double r = 1;
        for (int i = 0; i < 5; i++) {
            r += i;
            double theta = 0;
            for (int j = 0; j < 11; j++) {
                theta += pi / 12;
                Complex z = Complex.ofPolar(r, theta);
                Complex sqrtz = Complex.ofPolar(Math.sqrt(r), theta / 2);
                TestUtils.assertEquals(sqrtz, z.sqrt(), tol);
            }
        }
    }

    /**
     * Test: computing <b>third roots</b> of z.
     * <pre>
     * <code>
     * <b>z = -2 + 2 * i</b>
     *   => z_0 =  1      +          i
     *   => z_1 = -1.3660 + 0.3660 * i
     *   => z_2 =  0.3660 - 1.3660 * i
     * </code>
     * </pre>
     */
    @Test
    public void testNthRootNormalThirdRoot() {
        // The complex number we want to compute all third-roots for.
        Complex z = Complex.ofCartesian(-2,2);
        // The List holding all third roots
        Complex[] thirdRootsOfZ = z.nthRoot(3).toArray(new Complex[0]);
        // Returned Collection must not be empty!
        assertThat(thirdRootsOfZ.length).isEqualTo(3);
        // test z_0
        assertThat(thirdRootsOfZ[0].getReal()).isCloseTo(1.0, offset(1.0e-5));
        assertThat(thirdRootsOfZ[0].getImaginary()).isCloseTo(1.0, offset(1.0e-5));
        // test z_1
        assertThat(thirdRootsOfZ[1].getReal()).isCloseTo(-1.3660254037844386, offset(1.0e-5));
        assertThat(thirdRootsOfZ[1].getImaginary()).isCloseTo(0.36602540378443843, offset(1.0e-5));
        // test z_2
        assertThat(thirdRootsOfZ[2].getReal()).isCloseTo(0.366025403784439, offset(1.0e-5));
        assertThat(thirdRootsOfZ[2].getImaginary()).isCloseTo(-1.3660254037844384, offset(1.0e-5));
    }


    /**
     * Test: computing <b>fourth roots</b> of z.
     * <pre>
     * <code>
     * <b>z = 5 - 2 * i</b>
     *   => z_0 =  1.5164 - 0.1446 * i
     *   => z_1 =  0.1446 + 1.5164 * i
     *   => z_2 = -1.5164 + 0.1446 * i
     *   => z_3 = -1.5164 - 0.1446 * i
     * </code>
     * </pre>
     */
    @Test
    public void testNthRootNormalFourthRoot() {
        // The complex number we want to compute all third-roots for.
        Complex z = Complex.ofCartesian(5,-2);
        // The List holding all fourth roots
        Complex[] fourthRootsOfZ = z.nthRoot(4).toArray(new Complex[0]);
        // Returned Collection must not be empty!
        assertThat(fourthRootsOfZ.length).isEqualTo(4);
        // test z_0
        assertThat(fourthRootsOfZ[0].getReal()).isCloseTo(1.5164629308487783, offset(1.0e-5));
        assertThat(fourthRootsOfZ[0].getImaginary())
            .isCloseTo(-0.14469266210702247, offset(1.0e-5));
        // test z_1
        assertThat(fourthRootsOfZ[1].getReal()).isCloseTo(0.14469266210702256, offset(1.0e-5));
        assertThat(fourthRootsOfZ[1].getImaginary()).isCloseTo(1.5164629308487783, offset(1.0e-5));
        // test z_2
        assertThat(fourthRootsOfZ[2].getReal()).isCloseTo(-1.5164629308487783, offset(1.0e-5));
        assertThat(fourthRootsOfZ[2].getImaginary()).isCloseTo(0.14469266210702267, offset(1.0e-5));
        // test z_3
        assertThat(fourthRootsOfZ[3].getReal()).isCloseTo(-0.14469266210702275, offset(1.0e-5));
        assertThat(fourthRootsOfZ[3].getImaginary()).isCloseTo(-1.5164629308487783, offset(1.0e-5));
    }

    /**
     * Test: computing <b>third roots</b> of z.
     * <pre>
     * <code>
     * <b>z = 8</b>
     *   => z_0 =  2
     *   => z_1 = -1 + 1.73205 * i
     *   => z_2 = -1 - 1.73205 * i
     * </code>
     * </pre>
     */
    @Test
    public void testNthRootCornercaseThirdRootImaginaryPartEmpty() {
        // The number 8 has three third roots. One we all already know is the number 2.
        // But there are two more complex roots.
        Complex z = Complex.ofCartesian(8,0);
        // The List holding all third roots
        Complex[] thirdRootsOfZ = z.nthRoot(3).toArray(new Complex[0]);
        // Returned Collection must not be empty!
        assertThat(thirdRootsOfZ.length).isEqualTo(3);
        // test z_0
        assertThat(thirdRootsOfZ[0].getReal()).isCloseTo(2.0, offset(1.0e-5));
        assertThat(thirdRootsOfZ[0].getImaginary()).isCloseTo(0.0, offset(1.0e-5));
        // test z_1
        assertThat(thirdRootsOfZ[1].getReal()).isCloseTo(-1.0, offset(1.0e-5));
        assertThat(thirdRootsOfZ[1].getImaginary()).isCloseTo(1.7320508075688774, offset(1.0e-5));
        // test z_2
        assertThat(thirdRootsOfZ[2].getReal()).isCloseTo(-1.0, offset(1.0e-5));
        assertThat(thirdRootsOfZ[2].getImaginary()).isCloseTo(-1.732050807568877, offset(1.0e-5));
    }


    /**
     * Test: computing <b>third roots</b> of z with real part 0.
     * <pre>
     * <code>
     * <b>z = 2 * i</b>
     *   => z_0 =  1.0911 + 0.6299 * i
     *   => z_1 = -1.0911 + 0.6299 * i
     *   => z_2 = -2.3144 - 1.2599 * i
     * </code>
     * </pre>
     */
    @Test
    public void testNthRootCornercaseThirdRootRealPartZero() {
        // complex number with only imaginary part
        Complex z = Complex.ofCartesian(0,2);
        // The List holding all third roots
        Complex[] thirdRootsOfZ = z.nthRoot(3).toArray(new Complex[0]);
        // Returned Collection must not be empty!
        assertThat(thirdRootsOfZ.length).isEqualTo(3);
        // test z_0
        assertThat(thirdRootsOfZ[0].getReal()).isCloseTo(1.0911236359717216, offset(1.0e-5));
        assertThat(thirdRootsOfZ[0].getImaginary()).isCloseTo(0.6299605249474365, offset(1.0e-5));
        // test z_1
        assertThat(thirdRootsOfZ[1].getReal()).isCloseTo(-1.0911236359717216, offset(1.0e-5));
        assertThat(thirdRootsOfZ[1].getImaginary()).isCloseTo(0.6299605249474365, offset(1.0e-5));
        // test z_2
        assertThat(thirdRootsOfZ[2].getReal()).isCloseTo(-2.3144374213981936E-16, offset(1.0e-5));
        assertThat(thirdRootsOfZ[2].getImaginary()).isCloseTo(-1.2599210498948732, offset(1.0e-5));
    }

    /**
     * Test: compute <b>third roots</b> using a negative argument
     * to go clockwise around the unit circle. Fourth roots of one
     * are taken in both directions around the circle using
     * positive and negative arguments.
     * <pre>
     * <code>
     * <b>z = 1</b>
     *   => z_0 = Positive: 1,0 ; Negative: 1,0
     *   => z_1 = Positive: 0,1 ; Negative: 0,-1
     *   => z_2 = Positive: -1,0 ; Negative: -1,0
     *   => z_3 = Positive: 0,-1 ; Negative: 0,1
     * </code>
     * </pre>
     */
    @Test
    public void testNthRootNegativeArg() {
        // The complex number we want to compute all third-roots for.
        Complex z = Complex.ofCartesian(1, 0);
        // The List holding all fourth roots
        Complex[] fourthRootsOfZ = z.nthRoot(4).toArray(new Complex[0]);
        // test z_0
        assertThat(fourthRootsOfZ[0].getReal()).isCloseTo(1, offset(1.0e-5));
        assertThat(fourthRootsOfZ[0].getImaginary()).isCloseTo(0, offset(1.0e-5));
//         test z_1
        assertThat(fourthRootsOfZ[1].getReal()).isCloseTo(0, offset(1.0e-5));
        assertThat(fourthRootsOfZ[1].getImaginary()).isCloseTo(1, offset(1.0e-5));
        // test z_2
        assertThat(fourthRootsOfZ[2].getReal()).isCloseTo(-1, offset(1.0e-5));
        assertThat(fourthRootsOfZ[2].getImaginary()).isCloseTo(0, offset(1.0e-5));
        // test z_3
        assertThat(fourthRootsOfZ[3].getReal()).isCloseTo(0, offset(1.0e-5));
        assertThat(fourthRootsOfZ[3].getImaginary()).isCloseTo(-1, offset(1.0e-5));
        // go clockwise around the unit circle using negative argument
        fourthRootsOfZ = z.nthRoot(-4).toArray(new Complex[0]);
        // test z_0
        assertThat(fourthRootsOfZ[0].getReal()).isCloseTo(1, offset(1.0e-5));
        assertThat(fourthRootsOfZ[0].getImaginary()).isCloseTo(0, offset(1.0e-5));
//         test z_1
        assertThat(fourthRootsOfZ[1].getReal()).isCloseTo(0, offset(1.0e-5));
        assertThat(fourthRootsOfZ[1].getImaginary()).isCloseTo(-1, offset(1.0e-5));
        // test z_2
        assertThat(fourthRootsOfZ[2].getReal()).isCloseTo(-1, offset(1.0e-5));
        assertThat(fourthRootsOfZ[2].getImaginary()).isCloseTo(0, offset(1.0e-5));
        // test z_3
        assertThat(fourthRootsOfZ[3].getReal()).isCloseTo(0, offset(1.0e-5));
        assertThat(fourthRootsOfZ[3].getImaginary()).isCloseTo(1, offset(1.0e-5));
    }
    /**
     * Test standard values
     */
    @Test
    public void testGetArgument() {
        Complex z = Complex.ofCartesian(1, 0);
        assertThat(z.getArgument()).isCloseTo(0.0, offset(1.0e-12));

        z = Complex.ofCartesian(1, 1);
        assertThat(z.getArgument()).isCloseTo(Math.PI / 4, offset(1.0e-12));

        z = Complex.ofCartesian(0, 1);
        assertThat(z.getArgument()).isCloseTo(Math.PI / 2, offset(1.0e-12));

        z = Complex.ofCartesian(-1, 1);
        assertThat(z.getArgument()).isCloseTo(3 * Math.PI / 4, offset(1.0e-12));

        z = Complex.ofCartesian(-1, 0);
        assertThat(z.getArgument()).isCloseTo(Math.PI, offset(1.0e-12));

        z = Complex.ofCartesian(-1, -1);
        assertThat(z.getArgument()).isCloseTo(-3 * Math.PI / 4, offset(1.0e-12));

        z = Complex.ofCartesian(0, -1);
        assertThat(z.getArgument()).isCloseTo(-Math.PI / 2, offset(1.0e-12));

        z = Complex.ofCartesian(1, -1);
        assertThat(z.getArgument()).isCloseTo(-Math.PI / 4, offset(1.0e-12));

    }

    /**
     * Verify atan2-style handling of infinite parts
     */
    @Test
    public void testGetArgumentInf() {
        assertThat(infInf.getArgument()).isCloseTo(Math.PI / 4, offset(1.0e-12));
        assertThat(oneInf.getArgument()).isCloseTo(Math.PI / 2, offset(1.0e-12));
        assertThat(infOne.getArgument()).isCloseTo(0.0, offset(1.0e-12));
        assertThat(zeroInf.getArgument()).isCloseTo(Math.PI / 2, offset(1.0e-12));
        assertThat(infZero.getArgument()).isCloseTo(0.0, offset(1.0e-12));
        assertThat(negInfOne.getArgument()).isCloseTo(Math.PI, offset(1.0e-12));
        assertThat(negInfNegInf.getArgument()).isCloseTo(-3.0 * Math.PI / 4, offset(1.0e-12));
        assertThat(oneNegInf.getArgument()).isCloseTo(-Math.PI / 2, offset(1.0e-12));
    }

    /**
     * Verify that either part NaN results in NaN
     */
    @Test
    public void testGetArgumentNaN() {
        Assert.assertTrue(Double.isNaN(nanZero.getArgument()));
        Assert.assertTrue(Double.isNaN(zeroNaN.getArgument()));
        Assert.assertTrue(Double.isNaN(NAN.getArgument()));
        
        assertThat(nanZero.getArgument()).isNaN();
        assertThat(zeroNaN.getArgument()).isNaN();
        assertThat(NAN.getArgument()).isNaN();
    }

    @Test
    public void testParse() {
        Assert.assertTrue(Complex.ZERO.equals(Complex.parse(Complex.ZERO.toString())));
        Assert.assertTrue(Complex.ONE.equals(Complex.parse(Complex.ONE.toString())));
        Assert.assertTrue(Complex.I.equals(Complex.parse(Complex.I.toString())));
        Assert.assertTrue(Complex.INF.equals(Complex.parse(Complex.INF.toString())));
        Assert.assertTrue(NAN.equals(Complex.parse(NAN.toString())));
        Assert.assertTrue(oneInf.equals(Complex.parse(oneInf.toString())));
        Assert.assertTrue(negInfZero.equals(Complex.parse(negInfZero.toString())));
        Assert.assertTrue(Complex.ofReal(pi).equals(Complex.parse(Complex.ofReal(pi).toString())));
        Assert.assertTrue(Complex.ofPolar(2, pi).equals(Complex.parse(Complex.ofPolar(2, pi).toString())));
        Assert.assertTrue(Complex.ofCis(pi).equals(Complex.parse(Complex.ofCis(pi).toString())));
        assertThat(Complex.ZERO.equals(Complex.parse(Complex.ZERO.toString()))).isTrue();

        assertThat(Complex.parse(Complex.ZERO.toString())).isEqualTo(Complex.ZERO);

        assertThat(Complex.ONE.equals(Complex.parse(Complex.ONE.toString()))).isTrue();
        assertThat(Complex.I.equals(Complex.parse(Complex.I.toString()))).isTrue();
        assertThat(Complex.INF.equals(Complex.parse(Complex.INF.toString()))).isTrue();
        assertThat(NAN.equals(Complex.parse(NAN.toString()))).isTrue();
        assertThat(oneInf.equals(Complex.parse(oneInf.toString()))).isTrue();
        assertThat(negInfZero.equals(Complex.parse(negInfZero.toString()))).isTrue();
        assertThat(Complex.ofReal(pi).equals(Complex.parse(Complex.ofReal(pi).toString())))
            .isTrue();
        assertThat(Complex.ofPolar(2, pi).equals(Complex.parse(Complex.ofPolar(2, pi).toString())))
            .isTrue();
        assertThat(Complex.ofCis(pi).equals(Complex.parse(Complex.ofCis(pi).toString()))).isTrue();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParseWrongStart() {
        final String re = "1.234";
        final String im = "5.678";
        Complex.parse(re + "," + im + ")");
    }
    @Test(expected=IllegalArgumentException.class)
    public void testParseWrongEnd() {
        final String re = "1.234";
        final String im = "5.678";
        Complex.parse("(" + re + "," + im);
    }
    @Test(expected=IllegalArgumentException.class)
    public void testParseMissingSeparator() {
        final String re = "1.234";
        final String im = "5.678";
        Complex.parse("(" + re + " " + im + ")");
    }
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidRe() {
        final String re = "I.234";
        final String im = "5.678";
        Complex.parse("(" + re + "," + im + ")");
    }
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidIm() {
        final String re = "1.234";
        final String im = "5.G78";
        Complex.parse("(" + re + "," + im + ")");
    }

    @Test
    public void testParseSpaceAllowedAroundNumbers() {
        final double re = 1.234;
        final double im = 5.678;
        final String str = "(  " + re + "  , " + im + "     )";
        Assert.assertTrue(Complex.ofCartesian(re, im).equals(Complex.parse(str)));
    }
}
