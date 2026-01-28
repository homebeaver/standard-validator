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
package org.apache.commons.validator.routines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link TINValidator}.
 */
public class TINValidatorTest {

    private static final Log LOG = LogFactory.getLog(TINValidatorTest.class);
    private static final TINValidator VALIDATOR = TINValidator.getInstance();

    public static class Tin {
        final String countryCode;
        final String code;
        public Tin(final String cc, final String code) {
            this.countryCode = cc;
            this.code = code;
        }
        public String toString() {
            return countryCode.toString() + ":" + code.toString();
        }
    }

    // Eclipse 3.6 allows you to turn off formatting by placing a special comment, like
    // @formatter:off
    private static final Tin[] VALID_TIN_FIXTURES = {
            new Tin("AT", "98-123/4560"), // FA-NNN/NNNP
            new Tin("AT", "98 123/4560"), // FA NNN/NNNP
            new Tin("AT", "98 1234560"), // FA NNNNNNP
            new Tin("AT", "981234560"), // FANNNNNNP
            new Tin("AT", "90-123/4567"),
            new Tin("AT", "46-376/5321"),
            new Tin("AT", "03-826/1574"),
            new Tin("AT", "54-267/9451"),
            new Tin("AT", "35-353/5354"), // invalid until 2020 : 35 ist kein AT Finanzamt

            new Tin("BE", "95.02.28-998.74"),
            new Tin("BE", "85 07 30 033 28"),
            new Tin("BE", "17 07 30 033 84"), // born 2017
            new Tin("BE", "40 00 00 955-79"),
            new Tin("BE", "00 00 01 003-64"),

            new Tin("BG", "0011113966"), // born 11.11.1900, male
            new Tin("BG", "0404271406"), // born 27.04.1904, male
            new Tin("BG", "1012191435"), // born 19.12.1910, female
            new Tin("BG", "1302203993"), // born 20.02.1913, female
            new Tin("BG", "2502191464"), // born 19.02.1925, male
            new Tin("BG", "0649034487"), // born 03.09.2006, male
            new Tin("BG", "0847038794"), // born 03.07.2008, female
            new Tin("BG", "7901050017"),
            new Tin("BG", "0847038794"), // Пример bg.eikipedia

            new Tin("DE", "86095742719"), // doppelte Ziffer : 7
            new Tin("DE", "47036892816"), // doppelte Ziffer : 8
            new Tin("DE", "65929970489"), // keine doppelte Ziffer, dreifache Ziffer : 9
            new Tin("DE", "57549285017"), // keine doppelte Ziffer, dreifache Ziffer : 5
            new Tin("DE", "25768131411"), // keine doppelte Ziffer, dreifache Ziffer : 1

            new Tin("DK", "211062-5629"), // wikipedia
            new Tin("DK", "1111111118"),
            new Tin("DK", "111111-1118"),

            new Tin("ES", "A60195278"), // LIDL SUPERMERCADOS, S.A.U.
            new Tin("ES", "54362315K"), // Españoles con DNI
            new Tin("ES", "B58378431"), // Sociedades de responsabilidad limitada
            new Tin("ES", "X2482300W"), // Extranjeros residentes
            new Tin("ES", "W8265365J"), // Establecimientos permanentes de entidades no residentes en España

            new Tin("FI", "131052-308T"), // wikipedia
            new Tin("FI", "120464-126J"), // https://tarkistusmerkit.teppovuori.fi/tarkmerk.htm#moduli-31
            new Tin("FI", "120464U126J"), // same as 120464-126J (born 12.04.1964, female)
            new Tin("FI", "120464Y126J"),
            new Tin("FI", "120464+126J"), // born 1864 (!)
            new Tin("FI", "280264-051U"), // gleiche Quelle
            new Tin("FI", "140457-107D"),

            new Tin("HR", "33392005961"), // NASTAVNI ZAVOD ZA JAVNO ZDRAVSTVO DR. ANDRIJA ŠTA, Zagreb

            new Tin("LT", "33309240064"), // wikipedia
            new Tin("LT", "46411231034"),
            new Tin("EE", "47101010033"), // EE wie LT
            new Tin("EE", "47302200234"),
            new Tin("EE", "37605030299"), // wikipedia
            new Tin("EE", "49403136515"),
            new Tin("EE", "49403136526"),
            new Tin("EE", "37107290014"), // teppo

            new Tin("PL", "44051401458"), // wikipedia
            new Tin("PL", "26083006995"),
            new Tin("PL", "55030101193"), // wikipedia
            new Tin("PL", "55030101230"),

            new Tin("RO", "1800101221144"),
            new Tin("RO", "1541115221233"),
            new Tin("RO", "9000123456785"),

            new Tin("SE", "720310-1212"), // https://tarkistusmerkit.teppovuori.fi/tarkmerk.htm#pnr
            new Tin("SE", "720310+1212"), // +
            new Tin("SE", "820821-2384"),
            new Tin("SE", "870314-2391"),
    };
    // @formatter:on

    // @formatter:off
    private static final Tin[] INVALID_TIN_FIXTURES = {
            new Tin("", ""),                        // empty
            new Tin("DE", "   "),                   // empty
            new Tin("DE", "9"),                     // too short
            new Tin("DE", "AB768131411"),           // letters
            new Tin("DE", "11111111180"),           // cd 0
            new Tin("??", "abc"),                   // non ISO country
            new Tin("BG", "8508010133"),            // BG ID-Card Specimen
            new Tin("BG", "7608010133"),            // BG Passport Specimen
            new Tin("EE", "75001010007"),           // invalid 7 (sex+century)
            new Tin("FI", "120464T126J"),           // T is not valid
            new Tin("FI", "120464Z126J"),           // Z is not valid
            new Tin("FI", "120464G126J"),           // G is not valid
            new Tin("FI", "131052A308T"), // born 2052 (!)
            new Tin("FI", "120464F126J"), // born 2064 (!)
            new Tin("FI", "211271-426U"),           // specimen invalid CD
            new Tin("RO", "1541115881234"),         // invalid region1
            new Tin("RO", "0541115221231"),         // invalid gender: 0 
    };
    // @formatter:on


    @Test
    public void testGetRegexValidatortPatterns() {
        assertNotNull(VALIDATOR.getValidator("DE").getRegexValidator().getPatterns(), "DE");
    }

    @Test
    public void testGetValidator() {
        assertNotNull(VALIDATOR.getValidator("HR"), "HR");
        assertNull(VALIDATOR.getValidator("hr"), "hr");
    }

    @Test
    public void testHasValidator() {
        assertTrue(VALIDATOR.hasValidator("HR"), "HR");
        assertFalse(VALIDATOR.hasValidator("hr"), "hr");
    }

    @Test
    public void testInValid() {
        for (final Tin f : INVALID_TIN_FIXTURES) {
            assertFalse(VALIDATOR.isValid(f.countryCode, f.code), f.toString());
        }
    }

    @Test
    public void testNull() {
        assertFalse(VALIDATOR.isValid(null, null), "isValid(null)");
    }

    @Test
    public void testSetDefaultValidator1() {
        final IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> VALIDATOR.setValidator("GB", 15, "GB", null));
        assertEquals("The singleton validator cannot be modified", thrown.getMessage());
    }

    @Test
    public void testSetDefaultValidator2() {
        final IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> VALIDATOR.setValidator("GB", -1, "GB", null));
        assertEquals("The singleton validator cannot be modified", thrown.getMessage());
    }

    @Test
    public void testSetValidatorLC() {
        final TINValidator validator = new TINValidator();
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> validator.setValidator("gb", 15, "GB", null));
        assertEquals("Invalid country Code; must be exactly 2 upper-case characters", thrown.getMessage());
    }

    @Test
    public void testSetValidatorLen1() {
        final TINValidator validator = new TINValidator();
        assertNotNull(validator.setValidator("DE", -1, "", null), "should be present");
        assertNull(validator.setValidator("DE", -1, "", null), "no longer present");
    }

    private static final String INVALID_LENGTH = "Invalid length parameter, must be in range 10 to 16 inclusive:";

    @Test
    public void testSetValidatorLen35() {
        final TINValidator validator = new TINValidator();
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> validator.setValidator("DE", 35, "DE", null));
//        System.out.println("thrown.getMessage():" + thrown.getMessage());
        assertEquals(INVALID_LENGTH + " 35", thrown.getMessage());
    }

    @Test
    public void testSetValidatorLen7() {
        final TINValidator validator = new TINValidator();
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> validator.setValidator("GB", 7, "GB", null));
        assertEquals(INVALID_LENGTH + " 7", thrown.getMessage());
    }

    @Test
    public void testSorted() {
        final TINValidator validator = new TINValidator();
        final TINValidator.Validator[] vals = validator.getDefaultValidators();
        assertNotNull(vals);
        for (int i = 1; i < vals.length; i++) {
            if (vals[i].countryCode.compareTo(vals[i - 1].countryCode) <= 0) {
                fail("Not sorted: " + vals[i].countryCode + " <= " + vals[i - 1].countryCode);
            }
        }
    }

    @Test
    public void testValid() {
        for (final Tin f : VALID_TIN_FIXTURES) {
            LOG.info("testValid:" + f);
            assertTrue(VALIDATOR.isValid(f.countryCode, f.code), "CheckDigit fail: " + f.toString());
            assertTrue(VALIDATOR.hasValidator(f.countryCode), "Missing validator: " + f.toString());
        }
    }

}
