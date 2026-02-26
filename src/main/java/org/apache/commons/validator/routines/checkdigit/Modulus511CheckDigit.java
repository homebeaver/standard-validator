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

import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;

/**
 * Implements MOD 511 check digit simple procedure.
 * <p>
 * MOD 511 applies to numeric strings, the check digit is numeric and has the length of 3 digits.
 * It is used for French Numéro d'immatriculation fiscale (NIF).
 * See <a href="https://fr.wikipedia.org/wiki/Num%C3%A9ro_d%27immatriculation_fiscale#France">Wikipedia - NIF (fr)</a>
 * for more details.
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 2.10.6
 */
public final class Modulus511CheckDigit extends ModulusCheckDigit implements IsoIecConstants {

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
        super(MODULUS_511);
    }

    @Override
    protected int getCheckdigitLength() {
        return 3;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implement not used abstract method.
     * </p>
     */
    @Override
    protected int weightedValue(int charValue, int leftPos, int rightPos) throws CheckDigitException {
        return charValue;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override to handle numeric value of code.
     * </p>
     */
    @Override
    protected int calculateModulus(final String code, final boolean includesCheckDigit) throws CheckDigitException {
        try {
            // Satisfy testZeroSum
            final Long l = GenericTypeValidator.formatLong(code);
            if (l == null) {
                throw new CheckDigitException(CheckDigitException.invalidCode(code));
            }
            if (l == 0) {
                throw new CheckDigitException(CheckDigitException.ZERO_SUM);
            }
            return (int) (l % getModulus()); // MODULUS reminder
        } catch (final NumberFormatException ex) {
            System.out.println("Expected exception for invalid high codes. " + ex.getMessage());
            // Expected exception for high codes f.i. 99999999999999999999999
            throw new CheckDigitException(CheckDigitException.invalidCode(code));
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override to handle charValues with three digits.
     * </p>
     */
    @Override
    protected String toCheckDigit(final int charValue) throws CheckDigitException {
        int cdv = charValue == 0 ? 0 : getModulus() - charValue;
        if (cdv > 99) {
            // dreistellig
            int _23 = cdv % RADIX_100;
            int first = (cdv - _23) / RADIX_100;
            int third =  _23 % RADIX_10;
            int second = (_23 - third) / RADIX_10;
            return "" + first + second + third;
        } else if (cdv > 9) {
            // zweistellig
            int third = cdv % RADIX_10;
            int second = (cdv - third) / RADIX_10;
            return "0" + second + third;
        }
        return "00" + cdv;
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
