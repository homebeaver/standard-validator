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
public class Modulus31CheckDigit extends ModulusCheckDigit implements IsoIecConstants {

    private static final long serialVersionUID = -6810195028611194540L;

    /** Singleton Check Digit instance */
    private static final Modulus31CheckDigit INSTANCE = new Modulus31CheckDigit();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }

    /**
     * Constructs a Check Digit routine.
     */
    Modulus31CheckDigit(final int modulus) {
        super(modulus);
    }
    private Modulus31CheckDigit() {
        this(MODULUS_31);
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
            long l = Long.parseLong(code); // throws NumberFormatException
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
     * Override to map charValue to alphanumerics.
     * </p>
     */
    @Override
    protected String toCheckDigit(final int charValue) throws CheckDigitException {
        return "" + ALPHANUMERIC31.charAt(getModulus() - charValue);
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
