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
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.data.Offset.offset;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;


public class FractionFormatTest {

    FractionFormat properFormat = null;
    FractionFormat improperFormat = null;

    protected Locale getLocale() {
        return Locale.getDefault();
    }

    @Before
    public void setUp() {
        properFormat = FractionFormat.getProperInstance(getLocale());
        improperFormat = FractionFormat.getImproperInstance(getLocale());
    }

    @Test
    public void testFormat() {
        Fraction c = new Fraction(1, 2);
        String expected = "1 / 2";

        String actual = properFormat.format(c);
        assertThat(actual).isEqualTo(expected);

        actual = improperFormat.format(c);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testFormatNegative() {
        Fraction c = new Fraction(-1, 2);
        String expected = "-1 / 2";

        String actual = properFormat.format(c);
        assertThat(actual).isEqualTo(expected);

        actual = improperFormat.format(c);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testFormatZero() {
        Fraction c = new Fraction(0, 1);
        String expected = "0 / 1";

        String actual = properFormat.format(c);
        assertThat(actual).isEqualTo(expected);

        actual = improperFormat.format(c);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testFormatImproper() {
        Fraction c = new Fraction(5, 3);

        String actual = properFormat.format(c);
        assertThat(actual).isEqualTo("1 2 / 3");

        actual = improperFormat.format(c);
        assertThat(actual).isEqualTo("5 / 3");
    }

    @Test
    public void testFormatImproperNegative() {
        Fraction c = new Fraction(-5, 3);

        String actual = properFormat.format(c);
        assertThat(actual).isEqualTo("-1 2 / 3");

        actual = improperFormat.format(c);
        assertThat(actual).isEqualTo("-5 / 3");
    }

    @Test
    public void testParse() {
        String source = "1 / 2";

        try {
            Fraction c = properFormat.parse(source);
            assertThat(c).isNotNull();
            assertThat(c.getNumerator()).isEqualTo(1);
            assertThat(c.getDenominator()).isEqualTo(2);

            c = improperFormat.parse(source);
            assertThat(c).isNotNull();
            assertThat(c.getNumerator()).isEqualTo(1);
            assertThat(c.getDenominator()).isEqualTo(2);
        } catch (ParseException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testParseInteger() throws Exception {
        String source = "10";
        {
            Fraction c = properFormat.parse(source);
            assertThat(c).isNotNull();
            assertThat(c.getNumerator()).isEqualTo(10);
            assertThat(c.getDenominator()).isEqualTo(1);
        }
        {
            Fraction c = improperFormat.parse(source);
            assertThat(c).isNotNull();
            assertThat(c.getNumerator()).isEqualTo(10);
            assertThat(c.getDenominator()).isEqualTo(1);
        }
    }

    @Test
    public void testParseOne1() throws Exception {
        String source = "1 / 1";
        Fraction c = properFormat.parse(source);
        assertThat(c).isNotNull();
        assertThat(c.getNumerator()).isEqualTo(1);
        assertThat(c.getDenominator()).isEqualTo(1);
    }

    @Test
    public void testParseOne2() throws Exception {
        String source = "10 / 10";
        Fraction c = properFormat.parse(source);
        assertThat(c).isNotNull();
        assertThat(c.getNumerator()).isEqualTo(1);
        assertThat(c.getDenominator()).isEqualTo(1);
    }

    @Test
    public void testParseZero1() throws Exception {
        String source = "0 / 1";
        Fraction c = properFormat.parse(source);
        assertThat(c).isNotNull();
        assertThat(c.getNumerator()).isEqualTo(0);
        assertThat(c.getDenominator()).isEqualTo(1);
    }

    @Test
    public void testParseZero2() throws Exception {
        String source = "-0 / 1";
        Fraction c = properFormat.parse(source);
        assertThat(c).isNotNull();
        assertThat(c.getNumerator()).isEqualTo(0);
        assertThat(c.getDenominator()).isEqualTo(1);
        // This test shows that the sign is not preserved.
        assertThat(1d / c.doubleValue()).isCloseTo(Double.POSITIVE_INFINITY, offset(0d));
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

        {
            String source = "-1 / 2";
            Fraction c = properFormat.parse(source);
            assertThat(c).isNotNull();
            assertThat(c.getNumerator()).isEqualTo(-1);
            assertThat(c.getDenominator()).isEqualTo(2);

            c = improperFormat.parse(source);
            assertThat(c).isNotNull();
            assertThat(c.getNumerator()).isEqualTo(-1);
            assertThat(c.getDenominator()).isEqualTo(2);

            source = "1 / -2";
            c = properFormat.parse(source);
            assertThat(c).isNotNull();
            assertThat(c.getNumerator()).isEqualTo(-1);
            assertThat(c.getDenominator()).isEqualTo(2);

            c = improperFormat.parse(source);
            assertThat(c).isNotNull();
            assertThat(c.getNumerator()).isEqualTo(-1);
            assertThat(c.getDenominator()).isEqualTo(2);
        }
    }

    @Test
    public void testParseProper() throws Exception {
        String source = "1 2 / 3";

        {
            Fraction c = properFormat.parse(source);
            assertThat(c).isNotNull();
            assertThat(c.getNumerator()).isEqualTo(5);
            assertThat(c.getDenominator()).isEqualTo(3);
        }

        try {
            improperFormat.parse(source);
            fail("invalid improper fraction.");
        } catch (ParseException ex) {
            // success
        }
    }

    @Test
    public void testParseProperNegative() throws Exception {
        String source = "-1 2 / 3";
        {
            Fraction c = properFormat.parse(source);
            assertThat(c).isNotNull();
            assertThat(c.getNumerator()).isEqualTo(-5);
            assertThat(c.getDenominator()).isEqualTo(3);
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
        ProperFractionFormat format = (ProperFractionFormat)properFormat;

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
        assertThat(improperFormat.format(Math.PI)).isEqualTo("355 / 113");
    }
}
