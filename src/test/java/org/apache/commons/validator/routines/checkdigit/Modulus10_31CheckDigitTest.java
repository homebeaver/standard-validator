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
 * Modulus10_31CheckDigit Check Digit Test.
 */
class Modulus10_31CheckDigitTest extends AbstractCheckDigitTest {

    private static final String BEISPIEL1 = "00340433836214454077"; // https://franken-magazin.de/blog/00340-paket/
    private static final String BEISPIEL2 = "340434639972584579"; // ohne zwei führende Nullen

    /**
     * Sets up routine & valid codes.
     */
    @BeforeEach
    protected void setUp() {
        routine = Modulus10_31CheckDigit.getInstance();
        valid = new String[] { BEISPIEL1 , BEISPIEL2
            , "00340434639972584579" // fast erfunden
            , "34000434639972584579" // 34 und 00 vertauscht
            , "116" // 10 - (3+1)
            , "310"
            };
        invalid = new String[] { "0000", "0", "", null
            , "1234567890", "114"
            };
    }

}
