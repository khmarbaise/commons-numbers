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

package org.apache.commons.numbers.fraction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.data.Offset.offset;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;


public class BigFractionFormatTest {

    BigFractionFormat properFormat = null;
    BigFractionFormat improperFormat = null;

    protected Locale getLocale() {
        return Locale.getDefault();
    }

    @Before
    public void setUp() {
        properFormat = BigFractionFormat.getProperInstance(getLocale());
        improperFormat = BigFractionFormat.getImproperInstance(getLocale());
    }

    @Test
    public void testFormat() {
        BigFraction c = new BigFraction(1, 2);
        String expected = "1 / 2";

        String actual = properFormat.format(c);
        assertThat(actual).isEqualTo(expected);

        actual = improperFormat.format(c);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testFormatNegative() {
        BigFraction c = new BigFraction(-1, 2);
        String expected = "-1 / 2";

        String actual = properFormat.format(c);
        assertThat(actual).isEqualTo(expected);

        actual = improperFormat.format(c);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testFormatZero() {
        BigFraction c = new BigFraction(0, 1);
        String expected = "0 / 1";

        String actual = properFormat.format(c);
        assertThat(actual).isEqualTo(expected);

        actual = improperFormat.format(c);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testFormatImproper() {
        BigFraction c = new BigFraction(5, 3);

        String actual = properFormat.format(c);
        assertThat(actual).isEqualTo("1 2 / 3");

        actual = improperFormat.format(c);
        assertThat(actual).isEqualTo("5 / 3");
    }

    @Test
    public void testFormatImproperNegative() {
        BigFraction c = new BigFraction(-5, 3);

        String actual = properFormat.format(c);
        assertThat(actual).isEqualTo("-1 2 / 3");

        actual = improperFormat.format(c);
        assertThat(actual).isEqualTo("-5 / 3");
    }

    @Test
    public void testParse() throws Exception {
        String source = "1 / 2";

        {
            BigFraction c = properFormat.parse(source);
            assertThat(c).isNotNull();
            assertThat(c.getNumerator()).isEqualTo(BigInteger.ONE);
            assertThat(c.getDenominator()).isEqualTo(BigInteger.valueOf(2l));

            c = improperFormat.parse(source);
            assertThat(c).isNotNull();
            assertThat(c.getNumerator()).isEqualTo(BigInteger.ONE);
            assertThat(c.getDenominator()).isEqualTo(BigInteger.valueOf(2l));
        }
    }

    @Test
    public void testParseInteger() throws Exception {
        String source = "10";
        {
            BigFraction c = properFormat.parse(source);
            assertThat(c).isNotNull();
            assertThat(c.getNumerator()).isEqualTo(BigInteger.TEN);
            assertThat(c.getDenominator()).isEqualTo(BigInteger.ONE);
        }
        {
            BigFraction c = improperFormat.parse(source);
            assertThat(c).isNotNull();
            assertThat(c.getNumerator()).isEqualTo(BigInteger.TEN);
            assertThat(c.getDenominator()).isEqualTo(BigInteger.ONE);
        }
    }

    @Test
    public void testParseInvalid() {
        String source = "a";
        String msg = "should not be able to parse '10 / a'.";
        try {
            properFormat.parse(source);
            fail(msg);
        } catch (ParseException ex) {
            // success
        }
        try {
            improperFormat.parse(source);
            fail(msg);
        } catch (ParseException ex) {
            // success
        }
    }

    @Test
    public void testParseInvalidDenominator() {
        String source = "10 / a";
        String msg = "should not be able to parse '10 / a'.";
        try {
            properFormat.parse(source);
            fail(msg);
        } catch (ParseException ex) {
            // success
        }
        try {
            improperFormat.parse(source);
            fail(msg);
        } catch (ParseException ex) {
            // success
        }
    }

    @Test
    public void testParseNegative() throws Exception {
        String source = "-1 / 2";
        BigFraction c = properFormat.parse(source);
        BigFractionAssert.assertThat(c)
            .hasNumeratorAsInt(-1)
            .and()
            .hasDenominatorAsInt(2);

        c = improperFormat.parse(source);
        BigFractionAssert.assertThat(c)
            .hasNumeratorAsInt(-1)
            .and()
            .hasDenominatorAsInt(2);

        source = "1 / -2";
        c = properFormat.parse(source);
        BigFractionAssert.assertThat(c).hasNumeratorAsInt(-1).and().hasDenominatorAsInt(2);

        c = improperFormat.parse(source);
        BigFractionAssert.assertThat(c).hasNumeratorAsInt(-1).and().hasDenominatorAsInt(2);
    }

    @Test
    public void testParseProper() throws Exception {
        String source = "1 2 / 3";

        {
            BigFraction c = properFormat.parse(source);
            assertThat(c).isNotNull();
            assertThat(c.getNumeratorAsInt()).isEqualTo(5);
            assertThat(c.getDenominatorAsInt()).isEqualTo(3);
        }

        try {
            improperFormat.parse(source);
            fail("invalid improper fraction.");
        } catch (ParseException ex) {
            // success
        }
    }

    @Test
    public void testParseImproperNegative() {
        String source = "-1 2 / 3";
        assertThatExceptionOfType(ParseException.class)
            .isThrownBy(() -> improperFormat.parse(source))
            .withMessage(
                "string \"-1 2 / 3\" unparseable (from position 2) as an object of type class org.apache.commons.numbers.fraction.BigFraction");
    }

    @Test
    public void testParseProperNegative() throws Exception {
        String source = "-1 2 / 3";
        {
            BigFraction c = properFormat.parse(source);
            BigFractionAssert.assertThat(c)
                .hasNumeratorAsInt(-5)
                .and()
                .hasDenominatorAsInt(3);
        }

        try {
            improperFormat.parse(source);
            fail("invalid improper fraction.");
        } catch (ParseException ex) {
            // success
        }
    }

    @Test
    public void testParseProperInvalidMinus() {
        String source = "2 -2 / 3";
        try {
            properFormat.parse(source);
            fail("invalid minus in improper fraction.");
        } catch (ParseException ex) {
            // expected
        }
        source = "2 2 / -3";
        try {
            properFormat.parse(source);
            fail("invalid minus in improper fraction.");
        } catch (ParseException ex) {
            // expected
        }
    }

    @Test
    public void testParseBig() throws Exception {
        BigFraction f1 =
            improperFormat.parse("167213075789791382630275400487886041651764456874403" +
                                 " / " +
                                 "53225575123090058458126718248444563466137046489291");
        assertThat(f1.doubleValue()).isCloseTo(Math.PI, offset(0.0));
        BigFraction f2 =
            properFormat.parse("3 " +
                               "7536350420521207255895245742552351253353317406530" +
                               " / " +
                               "53225575123090058458126718248444563466137046489291");
        assertThat(f2.doubleValue()).isCloseTo(Math.PI, offset(0.0));
        assertThat(f2).isEqualTo(f1);
        BigDecimal pi =
            new BigDecimal("3.141592653589793238462643383279502884197169399375105820974944592307816406286208998628034825342117068");
        assertThat(f1.bigDecimalValue(99, RoundingMode.HALF_EVEN)).isEqualTo(pi);
    }

    @Test
    public void testNumeratorFormat() {
        NumberFormat old = properFormat.getNumeratorFormat();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setParseIntegerOnly(true);
        properFormat.setNumeratorFormat(nf);
        assertThat(properFormat.getNumeratorFormat()).isEqualTo(nf);
        properFormat.setNumeratorFormat(old);

        old = improperFormat.getNumeratorFormat();
        nf = NumberFormat.getInstance();
        nf.setParseIntegerOnly(true);
        improperFormat.setNumeratorFormat(nf);
        assertThat(improperFormat.getNumeratorFormat()).isEqualTo(nf);
        improperFormat.setNumeratorFormat(old);
    }

    @Test
    public void testDenominatorFormat() {
        NumberFormat old = properFormat.getDenominatorFormat();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setParseIntegerOnly(true);
        properFormat.setDenominatorFormat(nf);
        assertThat(properFormat.getDenominatorFormat()).isEqualTo(nf);
        properFormat.setDenominatorFormat(old);

        old = improperFormat.getDenominatorFormat();
        nf = NumberFormat.getInstance();
        nf.setParseIntegerOnly(true);
        improperFormat.setDenominatorFormat(nf);
        assertThat(improperFormat.getDenominatorFormat()).isEqualTo(nf);
        improperFormat.setDenominatorFormat(old);
    }

    @Test
    public void testWholeFormat() {
        ProperBigFractionFormat format = (ProperBigFractionFormat)properFormat;

        NumberFormat old = format.getWholeFormat();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setParseIntegerOnly(true);
        format.setWholeFormat(nf);
        assertThat(format.getWholeFormat()).isEqualTo(nf);
        format.setWholeFormat(old);
    }

    @Test
    public void testLongFormat() {
        assertThat(improperFormat.format(10l)).isEqualTo("10 / 1");
    }

    @Test
    public void testDoubleFormat() {
        assertThat(improperFormat.format(0.0625)).isEqualTo("1 / 16");
    }
}
