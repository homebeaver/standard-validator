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
        checkDigitLth = 2; // oder 1
        valid = new String[] { "12345678903" // erfunden 11-stellig
            , "1020304050668" // Prüfziffern OK, TIN nicht valide TODO Datum prüfen
            , "1020123150693" // Prüfziffern OK, Jahr nicht plausibel, TIN nicht valide
            , "2026021550630" // TIN gültig
            , "1980122200100" // TIN gültig
            };
    }

    @Override
    protected String removeCheckDigit(final String code) {
        if (code == null || code.length() <= checkDigitLth) {
            return null;
        }
        return code.substring(0, code.length() - (code.length() > 11 ? 2 : 1));
    }

    @Override
    protected String checkDigit(final String code) {
        if (code == null || code.length() <= checkDigitLth) {
            return "";
        }
        final int start = code.length() - (code.length() > 11 ? 2 : 1);
        return code.substring(start);
    }

    private static final String POSSIBLE_CHECK_CIPHERS = "0123456789X";
    @Override
    protected String[] createInvalidCodes(final String[] codes) {
        final List<String> list = new ArrayList<>();
        if (checkDigitLth == 0) {
            return list.toArray(new String[0]);
        }

        // create invalid check digit values
        for (final String fullCode : codes) {
            final String code = removeCheckDigit(fullCode);
            final String check = checkDigit(fullCode);
            for (int i = 0; i < POSSIBLE_CHECK_CIPHERS.length(); i++) {
                String c = fullCode.length() > 11 ? POSSIBLE_CHECK_CIPHERS.substring(i, i + 1) : "";
                for (int j = 0; j < POSSIBLE_CHECK_CIPHERS.length(); j++) {
                    final String curr = POSSIBLE_CHECK_CIPHERS.substring(j, j + 1) + c;
                    if (!curr.equals(check)) {
                        list.add(createCode(code, curr));
                    }
                }
            }
        }

        System.out.println("EUG " + list.size() + " createInvalidCodes created.");
        return list.toArray(new String[0]);
    }

}
