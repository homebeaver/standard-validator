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
 * Romanian Tax identification number (TIN) and Civil Number CNP Check Digit calculation/validation.
 * <p>
 * Codul de înregistrare fiscală (CIF) or Număr de identificare fiscală (NIF)
 * <br>
 * Cod Numeric Personal (CNP)
 * </p>
 * <p>
 * See <a href="https://ro.wikipedia.org/wiki/Num%C4%83r_de_identificare_fiscal%C4%83">Wikipedia - NIF (ro)r</a>
 * and <a href="https://en.wikipedia.org/wiki/National_identification_number#Romania">Wikipedia - CNP (en)</a>
 * for more details.
 * </p>
 *
 * @since 2.10.6
 */
public final class TidROCheckDigit extends Modulus11XCheckDigit {

    private static final long serialVersionUID = 8937961504560395464L;

	/** Singleton Check Digit instance */
    private static final TidROCheckDigit INSTANCE = new TidROCheckDigit();

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
    private TidROCheckDigit() {
    }

    /** Weighting given to digits depending on their left position */
    private static final int[] POSITION_WEIGHT = { 2,7,9,1,4,6,3,5,8,2,7,9 };
    /**
     * Calculates the <i>weighted</i> value of a character in the
     * code at a specified position.
     *
     * <p>CNP digits are weighted by their position from left to right.</p>
     *
     * @param charValue The numeric value of the character.
     * @param leftPos The position of the character in the code, counting from left to right
     * @param rightPos The positionof the character in the code, counting from right to left
     * @return The weighted value of the character.
     */
    @Override
    protected int weightedValue(int charValue, int leftPos, int rightPos) throws CheckDigitException {
        final int weight = POSITION_WEIGHT[(leftPos - 1) % POSITION_WEIGHT.length];
        return charValue * weight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String calculate(final String code) throws CheckDigitException {
        if (GenericValidator.isBlankOrNull(code)) {
            throw new CheckDigitException(CheckDigitException.MISSING_CODE);
        }
        return toCheckDigit(INSTANCE.calculateModulus(code, false));
    }

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

    /**
     * {@inheritDoc}
     * <p>
     * Override to handle charValue X.
     * </p>
     */
    @Override
    protected String toCheckDigit(final int charValue) throws CheckDigitException {
        return charValue == X ? "0" : super.toCheckDigit(charValue);
    }

}
