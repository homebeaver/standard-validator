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
 * RO TAX Id (TIN) and Civil Number CNP Check Digit Tests.
 */
public class TidROCheckDigitTest extends AbstractCheckDigitTest {

    /**
     * Sets up routine & valid codes.
     */
    @BeforeEach
    protected void setUp() {
        routine = TidROCheckDigit.getInstance();
        valid = new String[] { "12"  // theoretical minimum
            , "1800101221144" // https://en.wikipedia.org/wiki/National_identification_number#Romania
            , "2870212450080" , "8781001330012" , "4800101017005" // CNP
            , "9000123456785" , "2570120400241" // TIN
            , "1541115881234" // erfunden (ungültige Region)
            , "1541115221233" // erfunden (gültige Region)
            , "0541115221231" // erfunden (gender 0 ungültig)
            };
    }

}
