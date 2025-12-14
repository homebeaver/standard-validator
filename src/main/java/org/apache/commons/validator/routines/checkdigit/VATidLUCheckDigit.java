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

import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;

/**
 * Luxemburg VAT identification number (VATIN) Check Digit calculation/validation.
 * <p>
 * Numéro d'identification à la taxe sur la valeur ajoutée {@code 123456pp}.
 * </p>
 * <p>
 * The check digits are calculated as MOD 89
 * </p>
 * <p>
 * See <a href="https://en.wikipedia.org/wiki/VAT_identification_number">Wikipedia - VAT IN</a>
 * for more details.
 * </p>
 *
 * @since 1.10.0
 */
public final class VATidLUCheckDigit extends ModulusCheckDigit implements IsoIecConstants {

    private static final long serialVersionUID = 6690723004719444647L;

    /** Singleton Check Digit instance */
    private static final VATidLUCheckDigit INSTANCE = new VATidLUCheckDigit();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }

    static final int CHECKDIGIT_LEN = 2;
    static final int MODULUS_89 = 89;

    /**
     * Constructs a Check Digit routine.
     */
    private VATidLUCheckDigit() {
        super(MODULUS_89);
    }

    private final int checkdigitLength = CHECKDIGIT_LEN;
    public int getCheckdigitLength() {
        return checkdigitLength;
    }

    protected int getRadix() {
        return RADIX_10;
    }

    protected String getCharacterSet() {
        return NUMERIC;
    }

    @Override
    protected int weightedValue(final int charValue, final int leftPos, final int rightPos) {
        if (rightPos <= CHECKDIGIT_LEN) {
            return 0;
        }
        int res = charValue;
        for ( int i = 1; i < rightPos - CHECKDIGIT_LEN; i++) {
            res = res * 10;
        }
        return res;
    }

    @Override
    protected String toCheckDigit(final int checksum) throws CheckDigitException {
        String chars = getCharacterSet();
        if (getCheckdigitLength() == 2) {
            int second = checksum % getRadix();
            int first = (checksum - second) / getRadix();
            return "" + chars.charAt(first) + chars.charAt(second);
        } else {
            return "" + chars.charAt(checksum);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String calculate(final String code) throws CheckDigitException {
        if (GenericValidator.isBlankOrNull(code)) {
            throw new CheckDigitException(CheckDigitException.MISSING_CODE);
        }
        if (code.length() < CHECKDIGIT_LEN) {
            throw new CheckDigitException("Invalid Code length=" + code.length());
        }
        final int mr = calculateModulus(code, false);
        // Modulus result mr can be 0 - the CheckDigit is "00"
        return toCheckDigit(mr);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(final String code) {
        if (GenericValidator.isBlankOrNull(code)) {
            return false;
        }
        if (code.length() < CHECKDIGIT_LEN) {
            return false;
        }

        final String check = code.substring(code.length() - CHECKDIGIT_LEN);
        final Integer icheck = GenericTypeValidator.formatInt(check);
        // formatInt accepts "+0" as 0, avoid this
        if (icheck == null || !Character.isDigit(check.charAt(0))) {
            return false;
        }
        try {
            final int mr = calculateModulus(code, true);
            return icheck.intValue() == mr;
        } catch (final CheckDigitException ex) {
            return false;
        }
    }

}
