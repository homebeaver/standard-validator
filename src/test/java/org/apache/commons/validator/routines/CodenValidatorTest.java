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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link CodenValidator}.
 */
public class CodenValidatorTest {

    private static final CodenValidator VALIDATOR = CodenValidator.getInstance();

    private final String[] validFormat = {
            "NATUAS",
            "TEREAU",
            "60WQAW",
            "SEKEAI", "SENGA5", "TCYKE5", "ULTRD6", "WTHPDI", "YKXUD4", "ZXXUE5"};

    private final String[] invalidFormat = { null, "", // empty
            "   ", // empty
            "ßEKEAI", // ß
            "60W0AL", // mit '0' anstelle Buchstaben
            "SEKEAI\n", // nl + too long
            "SEKEA\n", // nl 
            "SEKEA0" };

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

}
