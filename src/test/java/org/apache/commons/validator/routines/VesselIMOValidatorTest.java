/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link VesselIMOValidator}.
 */
public class VesselIMOValidatorTest {

    private static final VesselIMOValidator VALIDATOR = VesselIMOValidator.getInstance();

    private static final String KAVITA = "9074729";

    private final String[] validFormat = {
            " IMO0000012 ", // theoretical minimum with spaces
            "IMO "+KAVITA,
            "\tIMO9999993\n", }; // theoretical maximum with white spaces (TAB and NL)

    private final String[] invalidFormat = { null, "", // empty
            "   ", // empty
            "IMO9074720", // proper check digit is '9', see above
            "imo9074729", // lower case
            "IMO-9074729" };

    @Test
    public void testInvalidFalse() {
        for (final String f : invalidFormat) {
            assertFalse(VALIDATOR.isValid(f), f);
        }
    }

    @Test
    public void testIsValidTrue() {
        for (final String f : validFormat) {
            assertTrue(VALIDATOR.isValid(f), f);
        }
    }

    @Test
    public void testValidate() {
        for (final String f : validFormat) {
            assertEquals(VALIDATOR.validate(f), f.replaceAll("IMO(\\s)?", "").trim());
        }
        for (final String f : invalidFormat) {
            assertNull(VALIDATOR.validate(f));
        }
    }

}
