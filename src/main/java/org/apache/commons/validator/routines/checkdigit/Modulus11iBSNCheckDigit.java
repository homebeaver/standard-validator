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
 * Check digit calculation based on <em>modulus 11</em> and weighs based on the digit position.
 * <p>
 * Digits are weighted based by their position, from right to left with the 
 * first digit being weighted 1, the second 2 and so on.
 * </p>
 * <p>
 * This module is used to calculate the TIN_NL Burgerservicenummer (BSN) check digits.
 * See <a href="https://nl.wikipedia.org/wiki/Burgerservicenummer">Wikipedia</a>
 * for more details.
 * </p>
 * @since 2.10.5
 */
public final class Modulus11iBSNCheckDigit extends ModulusCheckDigit {

    private static final long serialVersionUID = 6574749379620087578L;

    /** Singleton Check Digit instance */
    private static final Modulus11iBSNCheckDigit INSTANCE = new Modulus11iBSNCheckDigit();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }

    /**
     * Constructs a new instance.
     */
    Modulus11iBSNCheckDigit() {
        super(MODULUS_11);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implement to handle weights as right position.
     * </p>
     */
    @Override
    protected int weightedValue(int charValue, int leftPos, int rightPos) throws CheckDigitException {
        return charValue * rightPos;
    }

    /**
     * {@inheritDoc}
     */
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
     * <p>
     * Override to handle charValue 10.
     * </p>
     */
    @Override
    protected String toCheckDigit(final int charValue) throws CheckDigitException {
        return charValue == 10 ? "0" : super.toCheckDigit(charValue);
    }

    /*
     * Valide Prüfziffern "0" ergeben sich aus check charValue 0 oder 10.
     * Die Methode der Oberklasse erkennt nur die einstellige 0, nicht die 10.
     * Daher muss sie überschrieben werden
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(final String code) {
        if (GenericValidator.isBlankOrNull(code)) {
            return false;
        }
        try {
            final String cd = calculate(code.substring(0, code.length() - 1));
            return code.endsWith(cd);
        } catch (final CheckDigitException ex) {
            return false;
        }
    }

}
