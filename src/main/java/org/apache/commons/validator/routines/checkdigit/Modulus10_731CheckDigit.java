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
 * Modulus 10 module for Check Digit calculation/validation of machine-readable travel document (MRTD).
 * <p>
 * MRTD has alphanumeric code. The last (check) digit is numeric.
 * </p>
 * <p>
 * Check digit calculation is based on <em>modulus 10</em> with digits being weighted
 * based by their position, to left to right.
 * </p>
 * <p>
 * See International Civil Aviation Organization, Doc 9303: 
 * Machine Readable Travel Documents, Part 3: Specifications Common to all MRTDs
 * for more details. 
 * </p>
 *
 * @since 2.10.5
 */
public class Modulus10_731CheckDigit extends ModulusCheckDigit implements IsoIecConstants {

    private static final long serialVersionUID = 5157318188312883502L;

    /** Singleton Check Digit instance */
    private static final Modulus10_731CheckDigit INSTANCE = new Modulus10_731CheckDigit();

    /**
     * Gets the singleton instance of this class.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }
    Modulus10_731CheckDigit() {
        super(MODULUS_10);
    }

    /** Weighting given to digits depending on their right position */
    private static final int[] POSITION_WEIGHT = {7, 3, 1};

    /**
     * Calculates the <em>weighted</em> value of a character in the
     * code at a specified position.
     * <p>
     * MRTD numbers are weighted in the following manner:
     * <pre>{@code
     *     left position: 1  2  3  4  5  6  7  8  9
     *            weight: 7  3  1  7  3  1  7  3  1
     * }</pre>
     *
     * @param charValue The numeric value of the character.
     * @param leftPos The position of the character in the code, counting from left to right
     * @param rightPos The position of the character in the code, counting from right to left
     * @return The weighted value of the character.
     */
    @Override
    protected int weightedValue(final int charValue, final int leftPos, final int rightPos) {
        final int weight = POSITION_WEIGHT[(leftPos-1) % 3]; // CHECKSTYLE IGNORE MagicNumber
//        System.out.println(" <<< charValue="+charValue + " weight="+weight);
        return charValue * weight;
    }

    protected String getCharacterSet() {
        return ALPHANUMERIC;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overrides to handle numeric, alphabetic or alphanumeric values respectively.
     * </p>
     */
    @Override
    protected int toInt(final char character, final int leftPos, final int rightPos) throws CheckDigitException {
        if (getCharacterSet().indexOf(character) == -1) {
            throw new CheckDigitException(CheckDigitException.invalidCharacter(character, leftPos));
        }
        return Character.getNumericValue(character);
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
        try {
            final String cd = calculate(code.substring(0, code.length() - 1));
            return code.endsWith(cd);
        } catch (final CheckDigitException ex) {
            return false;
        }
    }

}
