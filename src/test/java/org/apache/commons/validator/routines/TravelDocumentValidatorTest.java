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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.routines.TravelDocumentValidator.Type;
import org.apache.commons.validator.routines.checkdigit.Modulus10_731CheckDigit;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link TravelDocumentValidator}.
 */
public class TravelDocumentValidatorTest {

    private static final Log LOG = LogFactory.getLog(TravelDocumentValidatorTest.class);
    private static final TravelDocumentValidator VALIDATOR = TravelDocumentValidator.getInstance();

    public static class TreavelDocument {
    	final Type docType;
        final String countryCode;
        final String code;
        public TreavelDocument(final Type t, final String cc, final String code) {
            this.docType = t;
            this.countryCode = cc;
            this.code = code;
        }
        public String toString() {
            return (docType==null ? "null" :docType.toString()) + countryCode.toString() + ":" + code.toString();
        }
    }

    // Eclipse 3.6 allows you to turn off formatting by placing a special comment, like
    // @formatter:off
    private static final TreavelDocument[] VALID_FIXTURES = {
        new TreavelDocument(Type.P , "AUT", "U12345676"), // https://www.consilium.europa.eu/prado/en/AUT-AO-02002/image-240610.html
        new TreavelDocument(Type.PP, "AUT", "AP12345673"), // File:Passport_of_Austria_(2024)_data_page.jpg
        new TreavelDocument(Type.P , "BEL", "GA00078050"), // File:Belgian_passport_-_2022_update.png
        new TreavelDocument(Type.P , "HRV", "0070070071"), // File:Croatian_passport_data_page.jpg
        new TreavelDocument(Type.ID, "HRV", "1155018306"), // File:New_Croatian_ID_Card XXX mit Type IO ?
        new TreavelDocument(Type.P , "CZE", "990090544"), // File:Czech_passport_2006_MRZ_data.jpg
        new TreavelDocument(Type.ID, "CZE", "9980094762"), // File:Vzor_OP_2021_1_revers.jpg
        new TreavelDocument(Type.P , "ESP", "ZAB0002549"), // File:ESPpassportdatapage.png
        new TreavelDocument(Type.ID, "ESP", "CAA0000004"), // File:Spanish_ID_card_(back_side)_webp
        new TreavelDocument(Type.P , "EST", "KS12345672"), // File:Eesti_biodata_2021.jpg
        new TreavelDocument(Type.ID, "EST", "AS00022619"), // https://en.wikipedia.org/wiki/Estonian_identity_card
        new TreavelDocument(Type.P , "FIN", "FX12345672"), // File:Finland_biodata.png
        new TreavelDocument(Type.P , "D", "CZ6311T472"), // File:Deutscher_Reisepass_(2024)_-_Passkartendatenseite.jpg
        new TreavelDocument(Type.ID, "D", "L01X00T471"), // File:Personalausweis_(2021).png
        new TreavelDocument(Type.I , "D", "L01X00T471"),
        new TreavelDocument(Type.ID, "D", "LZ6311T475"), // File:Personalausweis_(2024).png
        new TreavelDocument(Type.ID, "D", "1220011933"), // File:Mustermann_nPA_Sicherheitsmerkmale.jpg
        new TreavelDocument(Type.PP, "GBR", "9992371442"), // File:British_passport_biographical_data.jpg
        new TreavelDocument(Type.P , "HUN", "BD00020282"), // File:Hungarian_passport_biodata_page.png
        new TreavelDocument(Type.P , "ITA", "KK60005333"), // File:Itpassportbiodata.jpg
        new TreavelDocument(Type.P , "LUX", "JC3L7T2H4"), // File:Luxembourgish_Passport_Biodata.jpg
        new TreavelDocument(Type.ID, "LUX", "SPEC073476"), // File:Luxembourg_identity_Card_2021_verso.jpg
        new TreavelDocument(Type.P , "NLD", "SPECI20142"), // File:Dutch_passport_specimen_issued_9_March_2014.jpg
        new TreavelDocument(Type.PS, "POL", "ZC39917886"), // File:PL_Passport_data_page_2018_series.jpg
        new TreavelDocument(Type.I , "POL", "ZZC1082012"), // www.gov.pl
        new TreavelDocument(Type.PE, "ROU", "0400210397"), // File:Romanian_passport_2024_model_datapage.jpg
        new TreavelDocument(Type.ID, "ROU", "SP12352370"), // File:CEI_Romania_2025.jpg
        new TreavelDocument(Type.P , "SWE", "XA00000012"), // File:Svensk_pass_biodatakort.png
        new TreavelDocument(Type.ID, "SWE", "XA00000023"), // File:Nya_svensk_ID_backsidan.png
        new TreavelDocument(Type.ID, "SVK", "XX0230582"), // File:Slovakian_identity_card_-_back_-_2022.jpg
        new TreavelDocument(Type.I , "SVK", "XX02305802"),
        new TreavelDocument(Type.ID, "CYP", "HT00000006"), // File:Cypriot_identity_card_-_2020_-_Back.jpg

        new TreavelDocument(Type.P , "ARG", "ZZZ0001105"), // File:Argentine_Passport_for_Argentinian-Foreigners_citizens.png
        new TreavelDocument(Type.PS, "BIH", "20000028"), // File:Bosnia_passport.jpg
        new TreavelDocument(Type.P , "RKS", "P000000005"), // File:Kosovo_passport_data_page.jpg
        new TreavelDocument(Type.P , "RKS", "P009542643"),
        new TreavelDocument(Type.P , "FRA", "01EY738902"), // File:Passeport_français_avant_2000_pp8-9_visa_Chine_F_2011_tampons_Chine.png
        new TreavelDocument(Type.P , "CHE", "S0A00A006"),
        new TreavelDocument(Type.ID, "CHE", "F08033995"),
        new TreavelDocument(Type.ID, "CHE", "S1A00A009"), // File:NIDK-back.jpg
        new TreavelDocument(Type.ID, "CHE", "S1A00A0009"),
        new TreavelDocument(Type.ID, "LIE", "ID12345673"), // File:Liechtenstein_IDt_card_back.png
        new TreavelDocument(Type.P , "SYR", "N000000001"), // File:Syria_passport_2022_data_page.jpg
        new TreavelDocument(Type.PP, "JPN", "ZZ10000012"), // File:2025_Passport_of_Japan.png
        new TreavelDocument(Type.P , "QAT", "001264997"), // File:Qatar_passport_data_page.jpg
        new TreavelDocument(Type.P , "IND", "T43541679"),
        new TreavelDocument(Type.P , "AUS", "RA01234564"),
        new TreavelDocument(Type.P , "CAN", "BA1518622"),
        new TreavelDocument(Type.P , "USA", "E000093499"), // File:United_States_Next_Generation_Passport_signature_and_biodata_page.jpg

        new TreavelDocument(Type.P , "TWN", "8888008505"), // File:ROC_National_Without_Registration_Passport_Datapage.jpg

        new TreavelDocument(Type.V , "EGY", "0203104802"), // File:Egypt_visa_in_Czech_passport.jpg
    };
    // @formatter:on

    // @formatter:off
    private static final TreavelDocument[] INVALID_FIXTURES = {
            new TreavelDocument(null, "", ""),                  // empty
            new TreavelDocument(null, "D", "   "),              // empty
            new TreavelDocument(Type.P, "D", "9"),              // too short
            new TreavelDocument(Type.P, "??", "abc"),           // non country
            new TreavelDocument(Type.P, "USA", "\n9102392482"), // nl
    };
    // @formatter:on

    @Test
    public void testCountryCodes() {
        assertTrue(TravelDocumentValidator.ICAO.isIcaoCountry("ABW"));
        assertTrue(TravelDocumentValidator.ICAO.isIcaoCountry("ZWE"));
        assertFalse(TravelDocumentValidator.ICAO.isIcaoCountry("DEU"));
        assertTrue(TravelDocumentValidator.ICAO.isIcaoCountry("D")); 
        assertTrue(TravelDocumentValidator.ICAO.isIcaoCountry("GBD"));
        assertTrue(TravelDocumentValidator.ICAO.isIcaoCountry("RKS"));
        assertTrue(TravelDocumentValidator.ICAO.isIcaoCountry("XXX"));
        assertTrue(TravelDocumentValidator.ICAO.isIcaoCountry("UTO"));
        assertFalse(TravelDocumentValidator.ICAO.isIcaoCountry("IAO"));
    }

    @Test
    public void testGetRegexValidatortPatterns() {
        assertNotNull(VALIDATOR.getValidator(Type.P, "USA").getRegexValidator().getPatterns(), "USA");
    }

    @Test
    public void testGetValidator() {
        assertNotNull(VALIDATOR.getValidator(Type.P, "USA"), "USA");
        assertNull(VALIDATOR.getValidator(Type.P, "us"), "us");
    }

    @Test
    public void testHasValidator() {
        assertTrue(VALIDATOR.hasValidator(Type.P, "USA"), "USA");
        assertFalse(VALIDATOR.hasValidator(Type.P, "us"), "us");
    }

    @Test
    public void testInValid() {
        for (final TreavelDocument f : INVALID_FIXTURES) {
            assertFalse(VALIDATOR.isValid(f.docType, f.countryCode, f.code), f.toString());
        }
    }

    @Test
    public void testNull() {
        assertFalse(VALIDATOR.isValid(null, null, null), "isValid(null)");
    }

    @Test
    public void testSetNewValidator() {
        TravelDocumentValidator v = new TravelDocumentValidator();
        v.setValidator(Type.PP, "UTO", 10, TravelDocumentValidator.REGEX_ICAO9303, Modulus10_731CheckDigit.getInstance());
        TreavelDocument f = new TreavelDocument(Type.PP , "UTO", "L898902C36");
        LOG.info("testValid:" + f);
        assertTrue(v.isValid(f.docType, f.countryCode, f.code), "CheckDigit fail: " + f.toString());
        assertTrue(v.hasValidator(f.docType, f.countryCode), "Missing validator: " + f.toString());
    }

    @Test
    public void testSetDefaultValidator1() {
        final IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> VALIDATOR.setValidator(Type.ID, "GB", 15, "GB", null));
        assertEquals("The singleton validator cannot be modified", thrown.getMessage());
    }

    @Test // -1 ==> remove
    public void testSetDefaultValidator2() {
        final IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> VALIDATOR.setValidator(Type.ID, "GB", -1, "GB", null));
        assertEquals("The singleton validator cannot be modified", thrown.getMessage());
    }

    @Test
    public void testSetValidatorLC() {
        final TravelDocumentValidator validator = new TravelDocumentValidator();
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> validator.setValidator(Type.ID, "gb", 15, "GB", null));
        assertEquals("Invalid code \"gb\", Must be a valid ICAO alpha-3 country", thrown.getMessage());
    }

    @Test // -1 ==> remove
    public void testSetValidatorLen1() {
        final TravelDocumentValidator validator = new TravelDocumentValidator();
        assertNotNull(validator.setValidator(Type.P, "USA", -1, "", null), "should be present");
        assertNull(validator.setValidator(Type.P, "USA", -1, "", null), "no longer present");
    }

    private static final String INVALID_LENGTH = "Invalid length parameter, must be in range 8 to 10 inclusive:";

    @Test
    public void testSetValidatorLen35() {
        final TravelDocumentValidator validator = new TravelDocumentValidator();
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> validator.setValidator(Type.ID, "UTO", 35, "UTO", null));
        assertEquals(INVALID_LENGTH + " 35", thrown.getMessage());
    }

    @Test
    public void testSetValidatorLen7() {
        final TravelDocumentValidator validator = new TravelDocumentValidator();
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> validator.setValidator(Type.ID, "XXX", 7, "XXX", null));
        assertEquals(INVALID_LENGTH + " 7", thrown.getMessage());
    }

    @Test
    public void testValid() {
        for (final TreavelDocument f : VALID_FIXTURES) {
            LOG.info("testValid:" + f);
            assertTrue(VALIDATOR.isValid(f.docType, f.countryCode, f.code), "CheckDigit fail: " + f.toString());
            if (!VALIDATOR.hasValidator(f.docType, f.countryCode) 
                && (f.docType == Type.V || f.docType == Type.I || f.docType == Type.P)) {
                System.out.println("   " + f + " is a valid document number");
            } else {
                assertTrue(VALIDATOR.hasValidator(f.docType, f.countryCode), "Missing validator: " + f.toString());
            }
        }
    }

}
