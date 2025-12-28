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
 * Tests {@link BahnCardValidator}.
 */
public class MilesAndMoreCardValidatorTest {

    private static final MilesAndMoreCardValidator VALIDATOR = MilesAndMoreCardValidator.getInstance();

//    private static final String VALID_MASTERLHMM = "5310000314865928"; // Kreditkarte_(MasterCard_Gold)_mit_Miles_%26_More,_um_2022.jpg
    private static final String LHMM = "992001870623749";
    private static final String MATTHIAS = "992009452655443"; // Miles_%26_More-Karte_(Lufthansa)_%E2%80%94_Vorderseite.jpg

    private final String[] validFormat = { LHMM, MATTHIAS
        , "999956789012347"
        , "992056789012343"
        , "992256789012349"
        , "222000000000002"
        , "333000000000008"
    };

    private final String[] invalidFormat = { null, "" // empty
            , "\t333000000000008\n"
            , "\t333000000000008"
            , "333000000000008\n"
            , "378282246310005" // AMEX
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
