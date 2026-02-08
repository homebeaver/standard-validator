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
 * IE TIN and VAT Id Check Digit Tests.
 * <pre>

    IE 8473625E : achtstellig, als VATIN ungültig aus pruefziffernberechnung.de
       8473625EW : "W" at pos 9 (in numbers assigned before 1 January 2013)
    IE 3628739L : achtstellig, als VATIN ungültig aus BMF_UID_Konstruktionsregeln.pdf bmf.gv.at
    IE 3628739UA : neunstellig, als VATIN ungültig aus BMF_UID_Konstruktionsregeln.pdf bmf.gv.at
    IE 6433435OA : valide, aber als VATIN ungültig, aus https://old.formvalidation.io/validators/vat/
    IE 9700053D : gültig APPLE DISTRIBUTION INTERNATIONAL LTD, HOLLYHILL INDUSTRIAL ESTATE, CORK
    IE 6388047V : gültig GOOGLE IRELAND LIMITED
    IE 6433435F : gültig EOBO LIMITED, SHANNON aus https://old.formvalidation.io/validators/vat/
    IE 9950958B : gültig HAUPPAUGE DIGITAL EUROPE SARL, DUBLIN aus adresslabor.de und
    IE 2251597K, 8Y93637V (old Style), 6693587J alle ungültig

 * </pre>
 */
public class VATidIECheckDigitTest extends AbstractCheckDigitTest {

    /**
     * Sets up routine & valid codes.
     */
    @BeforeEach
    protected void setUp() {
        routine = VATidIECheckDigit.getInstance();
        valid = new String[] { "1234567FA" // TIN aus https://en.wikipedia.org/wiki/Personal_Public_Service_Number
            , "8473625E", "8473625EW", "3628739L", "3628739UA", "6433435OA"
            , "9700053D", "6388047V"
            , "6433435F", "0936378V"
            };
        invalid = new String[] {"99509582" // check digit 2 instead B
            , "0000000IA" // sum is zero
            };
    }

    private static final int LENGTH9 = VATidIECheckDigit.LEN + 2;

    /**
     * {@inheritDoc}
     * <p>
     * Override for long code format to replace the check character with 0
     * </p>
     */
    @Override
    protected String removeCheckDigit(final String code) {
        if (code == null || code.length() <= checkDigitLth) {
            return null;
        }
        if (code.length() >= LENGTH9) {
            // set checkDigit to 0
            return code.substring(0, VATidIECheckDigit.LEN) + 0 + code.substring(VATidIECheckDigit.LEN + 1);
        }
        return super.removeCheckDigit(code);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override for long code format to get the check character which is not the last character
     * </p>
     */
    @Override
    protected String checkDigit(final String code) {
        if (code == null || code.length() <= checkDigitLth) {
            return "";
        }
        if (code.length() >= LENGTH9) {
            final int start = LENGTH9 - checkDigitLth;
            return code.substring(start - 1, start);
        }
        return super.checkDigit(code);
    }

}
