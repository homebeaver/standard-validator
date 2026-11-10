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
 * Modulus 10 module for Check Digit calculation/validation of DHL tracking number.
 * <p>
 * The 20-digit DHL tracking number is numeric.
 * </p>
 * <p>
 * Check digit calculation is based on <em>modulus 10</em> with digits being weighted
 * based by their position, from left to right.
 * </p>
 * <p>
 * For further information see
 *  <a href="https://franken-magazin.de/blog/00340-paket/">franken-magazin.de/blog</a>.
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 2.11.1
 */
public class Modulus10_31CheckDigit extends ModulusCheckDigit implements IsoIecConstants {

    private static final long serialVersionUID = 4323034939957138801L;

	/** Singleton Check Digit instance */
    private static final Modulus10_31CheckDigit INSTANCE = new Modulus10_31CheckDigit();

    /**
     * Gets the singleton instance of this class.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }
    Modulus10_31CheckDigit() {
        super(MODULUS_10);
    }

    /** Weighting given to digits depending on their right position */
    private static final int[] POSITION_WEIGHT = {3, 1};

    /**
     * Calculates the <em>weighted</em> value of a character in the
     * code at a specified position.
     * <p>
     * DHL numbers are weighted in the following manner:
     * <pre>{@code
     *     left position: 1  2  3  4  5  6  ...
     *            weight: 3  1  3  1  3  1
     * }</pre>
     *
     * @param charValue The numeric value of the character.
     * @param leftPos The position of the character in the code, counting from left to right
     * @param rightPos The position of the character in the code, counting from right to left
     * @return The weighted value of the character.
     */
    @Override
    protected int weightedValue(final int charValue, final int leftPos, final int rightPos) {
        final int weight = POSITION_WEIGHT[(leftPos-1) % 2]; // CHECKSTYLE IGNORE MagicNumber
//        System.out.println(" <<< charValue="+charValue + " weight="+weight);
        return charValue * weight;
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
