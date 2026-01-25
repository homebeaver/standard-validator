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
 * Implements Moduli-31 check digit calculation/validation.
 * <p>
 * Moduli-31 applies to finish TIN (HETU) number, the check digit has one alphanumeric char.
 * See <a href="https://en.wikipedia.org/wiki/National_identification_number#Finland">Wikipedia</a>
 * for more details.
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 2.10.6
 */
public class Mudulus31CheckDigit extends IsoIec7064PureSystem implements IsoIecConstants {

    private static final long serialVersionUID = -6810195028611194540L;

    /** Singleton Check Digit instance */
    private static final Mudulus31CheckDigit INSTANCE = new Mudulus31CheckDigit();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }
    Mudulus31CheckDigit() {
        super(MODULUS_31, 1);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overridden because not used for moduli-31 calculation
     */
    @Override
    protected int getRadix() {
        return MODULUS_31;
    }

    /**
     * Check characters are “0” to “9” plus 21 ALPHABETIC chars.
     * @return a String containing characters the check digit is build from.
     * This is {@link IsoIecConstants#ALPHANUMERIC31}
     */
    @Override
    protected String getCharacterSet() {
        return ALPHANUMERIC31;
    }

    // XXX ähnlich zu VATIN_BE
    @Override
    public String calculate(final String code) throws CheckDigitException {
        if (GenericValidator.isBlankOrNull(code)) {
            throw new CheckDigitException(CheckDigitException.MISSING_CODE);
        }
        try {
            long l = Long.parseLong(code); // throws NumberFormatException
            if (l == 0) {
                throw new CheckDigitException(CheckDigitException.ZERO_SUM);
            }
            int r = (int) (l % getModulus()); // MODULUS reminder
            return toCheckDigit(r);
        } catch (final NumberFormatException ex) {
            System.out.println("Expected exception for invalid high codes. " + ex.getMessage());
            // Expected exception for high codes f.i. 99999999999999999999999
            throw new CheckDigitException(CheckDigitException.invalidCode(code));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(final String code) {
        if (GenericValidator.isBlankOrNull(code)) {
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
