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
package org.apache.commons.validator.routines.checkdigit;

import org.junit.jupiter.api.BeforeEach;

/**
 * Modulus10_31GLSCheckDigit Check Digit Test.
 */
class Modulus10_31GLSCheckDigitTest extends AbstractCheckDigitTest {

    private static final String BEISPIEL1 = "291116590502";
    private static final String BEISPIEL2 = "641012345674";

    /**
     * Sets up routine & valid codes.
     */
    @BeforeEach
    protected void setUp() {
        routine = Modulus10_31GLSCheckDigit.getInstance();
        valid = new String[] { BEISPIEL1 
            , BEISPIEL2
            , "481537858033" // fast erfunden
            , "123456789011"
            , "901234567897"
            };
        invalid = new String[] { "0000", "0", "", null
            , "1234567890", "114"
            };
    }

}
