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
 * Test ISO/IEC 7064, MOD 97-10 pure recursive/iterative and polynomial routines.
 *
 * @author EUG https://github.com/homebeaver
 * @since 1.10.0
 */
public class IsoIecPure97SystemTest extends AbstractIsoIec7064Test {

    private static final String MIN = "000195"; // theoretical minimum
    private static final String MAX = "99999999999999999928"; // theoretical
    private static final String LONG = "999999999999999999999999999916"; // theoretical

    public IsoIecPure97SystemTest() {
        checkDigitLth = 2;
    }

    /**
     * Sets up routine & valid codes.
     */
    @BeforeEach
    protected void setUp() {
        routine = IsoIecPure97System.getInstance();
        routin2 = IsoIecPolynomial97System.getInstance();
        valid = new String[] {"01", "001", "0001"
            , MIN, MAX
            , "999999999999999999950"
            , "9999999999999999999976"
            , "99999999999999999999945"
            , "999999999999999999999926"
            , "9999999999999999999999930" // calculation of high power results not with Math.pow(10, 23) % 97
            , "99999999999999999999999970"
            , LONG
            , "04011000123451234506" // from Leitweg spec: 04011000-1234512345-06
            , "9929000996" // Leitweg
            , "0517000520523100135"
            , "0533400020023300423"
            , "057110600179"
            , "059139900125"
            , "11200000100030"
            };
        invalid = new String[] {"08940", "089X", "X3"};
    }

}
