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

/**
 * Implements ISO/IEC 7064, MOD 11-2 polynomial check digit calculation/validation.
 * <p>
 * MOD 11-2 applies to numeric strings, the check digit is numeric or 'X'.
 * See <a href="https://de.wikipedia.org/wiki/ISO/IEC_7064">Wikipedia - ISO/IEC_7064 (de)</a>
 * for more details.
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 1.10.0
 */
public class IsoIecPolynomial11System extends IsoIec7064PurePolynomialSystem implements IsoIecConstants {

    private static final long serialVersionUID = -7329819731853774693L;

    /** Singleton Check Digit instance */
    private static final IsoIecPolynomial11System INSTANCE = new IsoIecPolynomial11System();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }
    IsoIecPolynomial11System() {
        super(MODULUS_11, 1);
    }

    @Override
    protected int getRadix() {
        return RADIX_2;
    }

    /** First fifteen weightings given to digits depending on their right position */
    private static final int[] POSITION_WEIGHT = { 1, 2, 4, 8, 5, 10, 9, 7, 3, 6, 1, 2, 4, 8, 5 };
    @Override
    protected int[] getWeightings() {
        return POSITION_WEIGHT;
    }

    @Override
    protected String getCharacterSet() {
        return NUMERIC_PLUS_X;
    }

    @Override
    protected int toInt(final char character, final int leftPos, final int rightPos) throws CheckDigitException {
        if (character == 'X' ) {
            throw new CheckDigitException(CheckDigitException.invalidCharacter(character, leftPos));
        }
        return super.toInt(character, leftPos, rightPos);
    }

}
