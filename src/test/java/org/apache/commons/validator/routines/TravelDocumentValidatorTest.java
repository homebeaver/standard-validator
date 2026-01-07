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
        
        new TreavelDocument(Type.P , "USA", "9102392482"), // old style
        new TreavelDocument(Type.P , "USA", "E000093499"), // File:United_States_Next_Generation_Passport_signature_and_biodata_page.jpg
    };
    // @formatter:on

    // @formatter:off
    private static final TreavelDocument[] INVALID_FIXTURES = {
            new TreavelDocument(null, "", ""),                  // empty
            new TreavelDocument(null, "D", "   "),              // empty
            new TreavelDocument(Type.P, "D", "9"),              // too short
            new TreavelDocument(Type.P, "??", "abc"),           // non country
    };
    // @formatter:on


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
//        final IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> VALIDATOR.setValidator(Type.ID, "GB", 15, "GB", null));
//        assertEquals("The singleton validator cannot be modified", thrown.getMessage());
        TravelDocumentValidator v = new TravelDocumentValidator();
        v.setValidator(Type.P, "TWN", 10, "\\d{10}", Modulus10_731CheckDigit.getInstance());
        TreavelDocument f = new TreavelDocument(Type.P , "TWN", "8888008505");
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
        assertEquals("Invalid country Code; must be 3 upper-case characters", thrown.getMessage());
    }

    @Test // -1 ==> remove
    public void testSetValidatorLen1() {
        final TravelDocumentValidator validator = new TravelDocumentValidator();
        assertNotNull(validator.setValidator(Type.P, "USA", -1, "", null), "should be present");
        assertNull(validator.setValidator(Type.P, "USA", -1, "", null), "no longer present");
    }

    private static final String INVALID_LENGTH = "Invalid length parameter, must be in range 9 to 10 inclusive:";

    @Test
    public void testSetValidatorLen35() {
        final TravelDocumentValidator validator = new TravelDocumentValidator();
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> validator.setValidator(Type.ID, "XYZ", 35, "XYZ", null));
//        System.out.println("thrown.getMessage():" + thrown.getMessage());
        assertEquals(INVALID_LENGTH + " 35", thrown.getMessage());
    }

    @Test
    public void testSetValidatorLen7() {
        final TravelDocumentValidator validator = new TravelDocumentValidator();
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> validator.setValidator(Type.ID, "XYZ", 7, "XYZ", null));
        assertEquals(INVALID_LENGTH + " 7", thrown.getMessage());
    }

    @Test
    public void testValid() {
        for (final TreavelDocument f : VALID_FIXTURES) {
            LOG.info("testValid:" + f);
            assertTrue(VALIDATOR.isValid(f.docType, f.countryCode, f.code), "CheckDigit fail: " + f.toString());
            assertTrue(VALIDATOR.hasValidator(f.docType, f.countryCode), "Missing validator: " + f.toString());
        }
    }

}
