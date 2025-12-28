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
 * Tests {@link BahnCardValidator}.
 */
public class BahnCardValidatorTest {

    private static final BahnCardValidator VALIDATOR = BahnCardValidator.getInstance();

    private static final String RR = "7081411114324805";

    private final String[] validFormat = { RR
        , "7081410043891603" // BahnCard_50_1994.jpg  ISO-Prefix 7081 4100
        , "7081410162182750" // BahnCard_25_2009_Jugend.png      7081 4101
        , "7081411114324805" // Bahncard_50_2002.JPG  ISO-Prefix 7081 4111
        , "7081420000000007" // Bahncard_100_2017.jpg "Business" 7081 4200
        , "7081411000000006"
    };

    private final String[] invalidFormat = { null, "" // empty
            , "\t7081411000000006\n"
            , "\t7081411000000006"
            , "7081411000000006\n"
            , "5047000000000004" // ÖBB (Österreich) – Vorteilscard
            };

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
