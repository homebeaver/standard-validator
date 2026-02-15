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

            new Tin("CY", "00010823U"),
            new Tin("CY", "60010823I"),
            new Tin("CY", "61234567I"),
            new Tin("CY", "91234567O"),

            new Tin("CZ", "930112/5207"), // Vzor_OP_2021_1_revers.jpg
            new Tin("CZ", "710319/2745"),
            new Tin("CZ", "685229/4449"),
            new Tin("CZ", "695622/0612"),
            new Tin("CZ", "723124/0181"), // male, born=1972/11/24 (serial numbers get depleted for a day)

            new Tin("DE", "86095742719"), // doppelte Ziffer : 7
            new Tin("DE", "47036892816"), // doppelte Ziffer : 8
            new Tin("DE", "65929970489"), // keine doppelte Ziffer, dreifache Ziffer : 9
            new Tin("DE", "57549285017"), // keine doppelte Ziffer, dreifache Ziffer : 5
            new Tin("DE", "25768131411"), // keine doppelte Ziffer, dreifache Ziffer : 1

            new Tin("DK", "211062-5629"), // wikipedia
            new Tin("DK", "1111111118"),
            new Tin("DK", "111111-1118"),

            new Tin("EL", "123456783"),
            new Tin("EL", "040127797"),
            new Tin("EL", "023456780"),
            new Tin("GR", "123456783"),
            new Tin("GR", "040127797"),
            new Tin("GR", "023456780"),

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

            new Tin("FR", "3023217600053"),
            new Tin("FR", "30 23 217 600 053"),
            new Tin("FR", "12 34 567 890 066"), // aus TIN_-_country_sheet_FR_de.pdf PZ:066
            new Tin("FR", "00 01 123 456 278"), // aus TIN_-_country_sheet_FR_de.pdf PZ:278
            new Tin("FR", "07 01 987 765 493"), // aus TIN_-_country_sheet_FR_de.pdf PZ:493
            new Tin("FR", "0000000001 001"),
            new Tin("FR", "3999999999 331"),

            new Tin("HR", "33392005961"), // NASTAVNI ZAVOD ZA JAVNO ZDRAVSTVO DR. ANDRIJA ŠTA, Zagreb
            new Tin("HR", "05781305459"),

            new Tin("HU", "8234560018"),
            new Tin("HU", "8400000021"),
            new Tin("HU", "10663103-2-18"), // jur.Person
            new Tin("HU", "12188224-2-43"),
            new Tin("HU", "21588017-2-44"),

            new Tin("IE", "1234567FA"),
            new Tin("IE", "8473625E"),
            new Tin("IE", "8473625EW"), // "W" at pos 9 (in numbers assigned before 1 January 2013)

            new Tin("IT", "MRTMTT91D08F205J"), // Matteo Moretti (male), born in Milan on 8 Apr.1991
            new Tin("IT", "MLLSNT82P65Z404U"), // Samantha Miller (female), born in the USA on 25 Sep.1982
            new Tin("IT", "MRYWLM80A01H501H"),
            new Tin("IT", "MRNLCU00A01H501J"),
            new Tin("IT", "MRNLCU00A01H50MB"), // Omocodia zu MRNLCU00A01H501
            new Tin("IT", "MRWYLM80AL1H501S"), // Omocodia zu MRWYLM80AL1H501

            new Tin("LT", "33309240064"), // wikipedia
            new Tin("LT", "46411231034"),
            new Tin("EE", "47101010033"), // EE wie LT
            new Tin("EE", "47302200234"),
            new Tin("EE", "37605030299"), // wikipedia
            new Tin("EE", "49403136515"),
            new Tin("EE", "49403136526"),
            new Tin("EE", "37107290014"), // teppo

            new Tin("LU", "1980122200100"),
            new Tin("LU", "1900010150631"),
            new Tin("LU", "1800010150643"),
            new Tin("LU", "2026021550630"),

            new Tin("LV", "32132113936"),
            new Tin("LV", "32503511782"),
            new Tin("LV", "29075810403"),
            new Tin("LV", "11111111410"),

            new Tin("NL", "999999990"),
            new Tin("NL", "111222333"),
            new Tin("NL", "123456782"),

            new Tin("PL", "44051401458"), // wikipedia
            new Tin("PL", "26083006995"),
            new Tin("PL", "55030101193"), // wikipedia
            new Tin("PL", "55030101230"),

            new Tin("PT", "502757191"),
            new Tin("PT", "136695973"),
            new Tin("PT", "501964843"),
            new Tin("PT", "504308548"),

            new Tin("RO", "1800101221144"),
            new Tin("RO", "1541115221233"),
            new Tin("RO", "9000123456785"),

            new Tin("SE", "720310-1212"), // https://tarkistusmerkit.teppovuori.fi/tarkmerk.htm#pnr
            new Tin("SE", "720310+1212"), // +
            new Tin("SE", "820821-2384"),
            new Tin("SE", "870314-2391"),

            new Tin("SI", "15012557"),
            new Tin("SI", "59082437"),

            new Tin("SK", "600101/8760"),
            new Tin("SK", "2021896096"),
            new Tin("SK", "4030000007"),
            new Tin("SK", "2022749619"),
            new Tin("SK", "911101/1250"),
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
            new Tin("CY", "30010823A"),             // VATIN
            new Tin("CY", "10259033P"),             // VATIN
            new Tin("CY", "12000139V"),             // starts with 12 : not TIN nor VATIN
            new Tin("CZ", "795229/0292"), // invalid date 29.Feb.1979 (female: 02+50)
            new Tin("EE", "75001010007"),           // invalid 7 (sex+century)
            new Tin("FI", "120464T126J"),           // T is not valid
            new Tin("FI", "120464Z126J"),           // Z is not valid
            new Tin("FI", "120464G126J"),           // G is not valid
            new Tin("FI", "131052A308T"), // born 2052 (!)
            new Tin("FI", "120464F126J"), // born 2064 (!)
            new Tin("FI", "211271-426U"),           // specimen invalid CD
            new Tin("IT", "MRWYLM80AU1H501B"),      // Omocodia 80AU1 == 81.Jan.1980 invalid
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

    private static final String INVALID_LENGTH = "Invalid length parameter, must be in range 8 to 17 inclusive:";

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
