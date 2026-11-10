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
 * Modulus 7 module for Check Digit calculation/validation of DHL tracking number.
 * <p>
 * The 10-digit DHL Express tracking number is numeric.
 * </p>
 * <p>
 * For further information see
 *  <a href="https://franken-magazin.de/blog/00340-paket/">franken-magazin.de/blog</a>.
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 2.11.1
 */
public class Modulus7CheckDigit extends ModulusCheckDigit implements IsoIecConstants {

    private static final long serialVersionUID = 7044713341985316121L;

    /** Singleton Check Digit instance */
    private static final Modulus7CheckDigit INSTANCE = new Modulus7CheckDigit();

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
    Modulus7CheckDigit(final int modulus) {
        super(modulus);
    }
    private Modulus7CheckDigit() {
        this(MODULUS_7);
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
        // Satisfy testZeroSum
        final Long l = GenericTypeValidator.formatLong(code);
        // formatLong returns null when NumberFormatException occurs
        if (l == null) {
            //System.out.println("Expected exception for invalid high codes. code="+code);
            throw new CheckDigitException(CheckDigitException.invalidCode(code));
        }
        if (l == 0) {
            throw new CheckDigitException(CheckDigitException.ZERO_SUM);
        }
        return (int) (l % getModulus()); // MODULUS reminder
    }

    @Override
    public String calculate(final String code) throws CheckDigitException {
        if (GenericValidator.isBlankOrNull(code)) {
            throw new CheckDigitException(CheckDigitException.MISSING_CODE);
        }
        final int modulusResult = calculateModulus(code, false);
        return toCheckDigit(modulusResult);
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
