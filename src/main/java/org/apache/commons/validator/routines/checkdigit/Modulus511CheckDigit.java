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

import org.apache.commons.validator.GenericValidator;

/**
 * Implements MOD 511 check digit simple procedure.
 * <p>
 * MOD 511 applies to numeric strings, the check digit is numeric and has the length of 3 digits.
 * It is used for french Numéro d'immatriculation fiscale (NIF).
 * See <a href="https://fr.wikipedia.org/wiki/Num%C3%A9ro_d%27immatriculation_fiscale#France">Wikipedia - NIF (fr)</a>
 * for more details.
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 2.10.6
 */
public class Modulus511CheckDigit extends IsoIec7064PureSystem implements IsoIecConstants {

    private static final long serialVersionUID = 8609862408916124805L;

    /** Singleton Check Digit instance */
    private static final Modulus511CheckDigit INSTANCE = new Modulus511CheckDigit();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }
    Modulus511CheckDigit() {
        super(511, 3);
    }

    @Override
    protected int getRadix() {
        return RADIX_10;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overrides with simplified procedure. CheckDigits are code MOD 511, the modulus,
     * </p>
     */
    @Override
    public String calculate(final String code) throws CheckDigitException {
        if (code == null) {
            throw new CheckDigitException(CheckDigitException.MISSING_CODE);
        }
        if (code.isEmpty()) {
            return toCheckDigit(0);
        }
        try {
            long l = Long.parseLong(code); // throws NumberFormatException
            int r = (int) (l % getModulus());
            return toCheckDigit(r);
        } catch (final NumberFormatException ex) {
//            System.out.println("NumberFormatException: "+ex);
            // Expected exception for high codes f.i. 999999999999999999
            // fall back to recursive/iterative method in super
            return super.calculate(code);
        }
    }

    @Override
    protected String getCharacterSet() {
        return NUMERIC;
    }
    @Override
    protected String toCheckDigit(final int checksum) throws CheckDigitException {
        String chars = getCharacterSet();
        if (checksum > 99) {
            // dreistellig
            int _23 = checksum % (getRadix()*getRadix());
            int first = (checksum - _23) / (getRadix()*getRadix());
            int third =  _23 % getRadix();
            int second = (_23 - third) / getRadix();
            return "" + chars.charAt(first) + chars.charAt(second) + chars.charAt(third);
        } else if (checksum > 9) {
            // zweistellig
            int third = checksum % getRadix();
            int second = (checksum - third) / getRadix();
            return "0" + chars.charAt(second) + chars.charAt(third);
        }
        return "00" + chars.charAt(checksum);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(final String code) {
        if (GenericValidator.isBlankOrNull(code)) {
            return false;
        }
        if (code.length() < getCheckdigitLength()) {
            return false;
        }
        String checkDigit = code.substring(code.length() - getCheckdigitLength());
        try {
            String cd = calculate(code.substring(0, code.length() - getCheckdigitLength())); // throws CheckDigitException
            return cd.equals(checkDigit);
        } catch (final CheckDigitException ex) {
            return false;
        }
    }

}
