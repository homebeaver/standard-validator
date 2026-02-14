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
 * LU TAX Id (TIN) Check Digit Tests.
 */
public class TidLUCheckDigitTest extends AbstractCheckDigitTest {

    /**
     * Sets up routine & valid codes.
     */
    @BeforeEach
    protected void setUp() {
        routine = TidLUCheckDigit.getInstance();
        checkDigitLth = 2;
        valid = new String[] { "1980122200100" // erfunden Datum 1980.12.22
            , "1900010150631" // Prüfziffern OK, Jahr plausibel, TIN gültig
            , "1800010150643" // TIN gültig
            , "2026021550630" // TIN gültig
            };
        invalid = new String[] { "1020304050668" // Prüfziffern OK, TIN nicht valide wg. Datum
            , "1020123150693" // Prüfziffern OK, Jahr nicht plausibel, TIN nicht valide
            , "1799123150688" // Prüfziffern OK, Jahr nicht plausibel, TIN nicht valide
            };
    }

}