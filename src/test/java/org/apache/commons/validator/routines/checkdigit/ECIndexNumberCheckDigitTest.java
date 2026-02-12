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
package org.apache.commons.validator.routines.checkdigit;

import org.junit.jupiter.api.BeforeEach;

/**
 * EC Index Number Check Digit Tests.
 */
public class ECIndexNumberCheckDigitTest extends AbstractCheckDigitTest {

    private static final String HYDROGEN = "001001009"; // the first entry
    private static final String LITHIUM  = "003001004";
    private static final String KRESOXIM = "607310000"; // kresoxim-methyl
    private static final String ASBESTOS = "650013006";
    // with X:
    private static final String HCL     = "01700201X"; // Hydrochloric acid, Salzsäure
    private static final String ARSENIC = "03300100X";
    private static final String MAX     = "999999995"; // theoretical

    /**
     * Sets up routine & valid codes.
     */
    @BeforeEach
    protected void setUp() {
        routine = Modulus11XiLeftCheckDigit.getInstance();
        valid = new String[] { "8234560018" // valid TIN_HU
            , "11" // theoretical min , check digit is weight
            , "012" // check digit is weight
            , "0013" // check digit is weight
            , "00014" // check digit is weight
            , "000015" // check digit is weight
            , "0000016" // check digit is weight
            , "00000017" // check digit is weight
            , "000000018" // check digit is weight
            , "0000000019" // check digit is weight
            , "0000000001X" // check digit is weight 10=X
            , HYDROGEN, LITHIUM, HCL, ARSENIC, KRESOXIM, ASBESTOS, MAX
            };
    }

}
