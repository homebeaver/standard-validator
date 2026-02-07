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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test MOD 511 which applies to alphanumeric Strings.
 * Check digits can be from 000 to 510
 *
 * @author EUG https://github.com/homebeaver
 * @since 2.10.6
 */
public class Modulus511CheckDigitTest extends AbstractIsoIec7064Test {

    private static final String MIN = "1001"; // theoretical minimum
    private static final String MAX = "99999999999999999228"; // theoretical
    private static final String LONG = "999999999999999999999999999999999203"; // theoretical

    public Modulus511CheckDigitTest() {
        checkDigitLth = 3;
    }

    /**
     * Sets up routine & valid codes.
     */
    @BeforeEach
    protected void setUp() {
        routine = Modulus511CheckDigit.getInstance();
        valid = new String[] { "000", "0000" // empty or zero string with check digit
          , MIN, MAX
          , "999999999999999999245"
          , "9999999999999999999450"
          , "99999999999999999999313"
          , "999999999999999999999476"
          , "9999999999999999999999999999167"
          , "99999999999999999999999999999038"
          , "999999999999999999999999999999281"
          , "9999999999999999999999999999999156"
          , "99999999999999999999999999999999439"
          , LONG
          , "3023217600053"
          , "3999999999331" // max TIN_FR
        };
        invalid = new String[] {"511511", "001", "0001"};
    }

    @Test
    public void testZeroSum() {
        assertTrue(routine.isValid(zeroSum), "isValid() Zero Sum"); // valide, siehe oben
        if (routin2 != null) {
            assertFalse(routin2.isValid(zeroSum), "isValid() Zero Sum");
        }
        // nothing is thrown because check digit is calculated for "0000000000"
//      Exception e = assertThrows(Exception.class, () -> routine.calculate(zeroSum), "Zero Sum");
//      assertEquals("Invalid code, sum is zero", e.getMessage(), "isValid() Zero Sum");
        try {
            final String actual = routine.calculate(zeroSum);
            System.out.println("EUG testZeroSum for code \"" + zeroSum + "\" returns " + actual);
            assertNotNull(actual);
            if (routin2 != null) {
                final String actual2 = routine.calculate(zeroSum);
                System.out.println("EUG testZeroSum for code \"" + zeroSum + "\" returns " + actual2);
                assertNotNull(actual2);
            }
        } catch (CheckDigitException e) {
            fail("testZeroSum for code \"" + zeroSum + "\" threw " + e);
        }
    }

}
