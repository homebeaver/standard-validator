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
 * CODEN is a six-character, alphanumeric bibliographic code that provides concise, unique and unambiguous 
 * identification of the titles of periodicals and non-serial publications from all subject areas
 * <p>
 * ASTM standard E250 - deprecated Standard "Practice for Use of CODEN"
 * </p>
 * <p>
 * See <a href="https://en.wikipedia.org/wiki/CODEN">Wikipedia</a>
 * for more details.
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 2.10.8
 */
public final class CodenCheckDigit extends ModulusCheckDigit implements IsoIecConstants {

    private static final long serialVersionUID = 5769735850899733407L;

    /** Singleton Check Digit instance */
    private static final CodenCheckDigit INSTANCE = new CodenCheckDigit();

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
    private CodenCheckDigit() {
        super(34);
    }

    private static final String CODEN = ALPHABETIC + NUMERIC;

    @Override
    protected int toInt(final char character, final int leftPos, final int rightPos) throws CheckDigitException {
        int i = CODEN.indexOf(character);
        if (i == -1) {
            throw new CheckDigitException(CheckDigitException.invalidCharacter(character, leftPos));
        }
        return i < ALPHABETIC.length() ? i + 1 : i == ALPHABETIC.length() ? 36 : i;
    }

    /** Weighting given to digits depending on their left position */
    private static final int[] POSITION_WEIGHT = {11, 7, 5, 3, 1};

    /**
     * Calculates the <em>weighted</em> value of a character in the
     * code at a specified position.
     *
     * @param charValue The numeric value of the character.
     * @param leftPos The position of the character in the code, counting from left to right
     * @param rightPos The positionof the character in the code, counting from right to left
     * @return The value of the character.
     */
    @Override
    protected int weightedValue(int charValue, int leftPos, int rightPos) throws CheckDigitException {
        if (leftPos > POSITION_WEIGHT.length) {
            throw new CheckDigitException(CheckDigitException.START_WITH_INVALID + "Code: too long");
        }
        final int weight = POSITION_WEIGHT[(leftPos-1)];
//      System.out.println(" <<< charValue="+charValue + " weight="+weight);
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
        return toCheckDigit(calculateModulus(code, false));
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
     * Override to get the non numeric check character
     * </p>
     */
    @Override
    protected String toCheckDigit(final int charValue) throws CheckDigitException {
//      System.out.println("toCheckDigit charValue="+charValue);
        if (charValue > 0 && charValue <= ALPHABETIC.length()) {
            return "" + ALPHABETIC.charAt(charValue - 1);
        }
        // 0 => 9
        if (charValue == 0) {
            return "9";
        }
        // 27 => 2 , 28 => 3 , ...
        if (charValue < getModulus()) {
            return "" + NUMERIC.charAt(charValue - 25);
        }
        throw new CheckDigitException(CheckDigitException.invalidCheckDigitValue(charValue));
    }

}
