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

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Creditor Reference Check Digit Test.
 */
public class RFCreditorReferenceCheckDigitTest extends AbstractCheckDigitTest {

    public RFCreditorReferenceCheckDigitTest() {
        checkDigitLth = 2;
    }

    /**
     * Returns the check digit (i.e. last character) for a code.
     *
     * @param code The code
     * @return The check digit
     */
    @Override
    protected String checkDigit(final String code) {
        if (code == null || code.length() <= checkDigitLth) {
            return "";
        }
        return code.substring(2, 4);
    }

    /**
     * Returns an array of codes with invalid check digits.
     *
     * @param codes Codes with valid check digits
     * @return Codes with invalid check digits
     */
    @Override
    protected String[] createInvalidCodes(final String[] codes) {
        final List<String> list = new ArrayList<>();

        // create invalid check digit values
        for (final String code2 : codes) {
            final String code = removeCheckDigit(code2);
            final String check = checkDigit(code2);
            for (int j = 2; j <= 98; j++) { // check digits can be from 02-98 (00 and 01 are not possible)
                final String curr = j > 9 ? "" + j : "0" + j;
                if (!curr.equals(check)) {
                    list.add(code.substring(0, 2) + curr + code.substring(4));
                }
            }
        }

        return list.toArray(new String[0]);
    }

    /**
     * Returns a code with the Check Digits (i.e. characters 3&4) set to "00".
     *
     * @param code The code
     * @return The code with the zeroed check digits
     */
    @Override
    protected String removeCheckDigit(final String code) {
        return code.substring(0, 2) + "00" + code.substring(4);
    }

    /**
     * Sets up routine & valid codes.
     */
    @BeforeEach
    protected void setUp() {
        routine = RFCreditorReferenceCheckDigit.getInstance();
        valid = new String[] { 

                // Creditor Reference looks like IBAN for RF, which is not a valid alpha-2 country code
                "RF18539007547034", // valid Creditor Reference
                "RF18000000000539007547034", // valid Creditor Reference with maximu lenght
                "RF45ABC",
                "RF45abc",

                };
        invalid = new String[] { "RF28000000000539007547034X" // invalid Creditor Reference (too long) with valid check digit
                , "RF04" // empty RFCreditorReference
                , "BEBE510007547061", "510007+47061BE63", "IE01AIBK93118702569045"
                , "AA0000000000089", "AA9900000000053", };
        zeroSum = null;
//        missingMessage = "Invalid Code length=0";

    }

    /**
     * Test zero sum
     */
    @Override
    @Test
    public void testZeroSum() {
        // ignore, don't run this test
    }
}
