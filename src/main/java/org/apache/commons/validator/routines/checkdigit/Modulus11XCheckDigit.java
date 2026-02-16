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

/**
 * Modulus 11-X module for Check Digit calculation/validation of 11-X Numbers.
 * <p>
 * 11-X Numbers are a numeric code except for the last (check) digit
 * which can have a value of "X".
 * </p>
 * <p>
 * Check digit calculation is based on <em>modulus 11</em> with digits being weighted
 * based by their position, from right to left with the right most digit being weighted 1,
 * the second 2 and so on. If the check digit is calculated as "10" it is converted to "X".
 * </p>
 * <p>
 * A prominent usage are the ISBN-10 and the ISSN Number. ISBN-10 Numbers are a numeric codes 
 * except for the last (check) digit which can have a value of "X".
 * </p>
 * <p>
 * <b>N.B.</b> From 1st January 2007 the book industry will start to use a new 13 digit
 * ISBN number (rather than this 10 digit ISBN number) which uses the EAN-13 / UPC
 * (see {@link EAN13CheckDigit}) standard.
 * <p>
 * For further information see:
 * <ul>
 *   <li><a href="https://en.wikipedia.org/wiki/ISBN">Wikipedia - International
 *       Standard Book Number (ISBN)</a>.</li>
 *   <li><a href="http://www.isbn.org/standards/home/isbn/transition.asp">ISBN-13
 *       Transition details</a>.</li>
 * </ul>
 *
 * @see Modulus11iWeightCheckDigit#weightedValue(int, int, int)
 * 
 * @since 1.10.0
 */
public class Modulus11XCheckDigit extends Modulus11iWeightCheckDigit {

    private static final long serialVersionUID = 5214797259628194566L;

    /**
     * The ALPHABET for the check digit is a number or X which indicates ten.
     */
    static final int X = 10;

    /** Singleton Check Digit instance */
    private static final Modulus11XCheckDigit INSTANCE = new Modulus11XCheckDigit();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }

    Modulus11XCheckDigit() {
        super();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override to handle charValue X.
     * </p>
     */
    @Override
    protected String toCheckDigit(final int charValue) throws CheckDigitException {
        return charValue == X ? "X" : super.toCheckDigit(charValue);
    }

    /**
     * <p>Convert a character at a specified position to an integer value.</p>
     *
     * <p>Character 'X' check digit converted to 10.</p>
     *
     * @param character The character to convert.
     * @param leftPos The position of the character in the code, counting from left to right
     * @param rightPos The position of the character in the code, counting from right to left
     * @return The integer value of the character.
     * @throws CheckDigitException if an error occurs.
     */
    @Override
    protected int toInt(final char character, final int leftPos, final int rightPos) throws CheckDigitException {
        if (rightPos == 1 && character == 'X') {
            return X;
        }
        return super.toInt(character, leftPos, rightPos);
    }

}
