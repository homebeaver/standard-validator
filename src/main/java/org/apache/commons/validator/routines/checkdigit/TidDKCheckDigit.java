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
 * Danish Tax identification number (TIN) and VATIN Check Digit calculation/validation.
 * <p>
 * det Centrale PersonRegister - nummer (CPR-nummer)
 * <br>
 * Nmomsregistreringsnummer (SE-nr.)
 * </p>
 * <p>
 * See <a href="https://da.wikipedia.org/wiki/CPR-nummer">Wikipedia - CPR-numme (da)r</a>
 * and <a href="https://en.wikipedia.org/wiki/VAT_identification_number">Wikipedia - VAT IN (en)</a>
 * for more details.
 * </p>
 *
 * @since 2.10.6
 */
public final class TidDKCheckDigit extends Modulus11XCheckDigit {

    private static final long serialVersionUID = -2476335527498714738L;

    /** Singleton Check Digit instance */
    private static final TidDKCheckDigit INSTANCE = new TidDKCheckDigit();

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
    private TidDKCheckDigit() {
    }

    /** Weighting given to digits depending on their right position */
    private static final int[] POSITION_WEIGHT = { 2, 3, 4, 5, 6, 7 };

    /**
     * Calculates the <i>weighted</i> value of a character in the code at a specified position.
     *
     * <p>TID, VATID digits are weighted by their position from right to left.
     * There is no check digit on the right pos 1. 
     * And the lowest right pos is 2 and has the weight 1.
     * The next weight is 2 for right pos 3 and so on to right pos 8, Then we repeat the weight 2, 3, ...
     * </p>
     *
     * @param charValue The numeric value of the character.
     * @param leftPos The position of the character in the code, counting from left to right
     * @param rightPos The positionof the character in the code, counting from right to left
     * @return The weighted value of the character.
     */
    @Override
    protected int weightedValue(int charValue, int leftPos, int rightPos) throws CheckDigitException {
/*
   0 Testing calculate Valid Check Digit, Code=[2110625629] expected=[0]
>> charValue=2 weight=4 leftPos=1 rightPos=11 , summe bei TIN immer = 12
>> charValue=1 weight=3 leftPos=2 rightPos=10
>> charValue=1 weight=2 leftPos=3 rightPos=9
>> charValue=0 weight=7 leftPos=4 rightPos=8
>> charValue=6 weight=6 leftPos=5 rightPos=7
>> charValue=2 weight=5 leftPos=6 rightPos=6
>> charValue=5 weight=4 leftPos=7 rightPos=5
>> charValue=6 weight=3 leftPos=8 rightPos=4
>> charValue=2 weight=2 leftPos=9 rightPos=3
>> charValue=9 weight=1 leftPos=10 rightPos=2

 */
//        final int weight = POSITION_WEIGHT[(leftPos - 1)];
    	final int weight = rightPos < 3 ? 1 : POSITION_WEIGHT[(rightPos-3) % POSITION_WEIGHT.length];
    System.out.println(">> charValue="+charValue + " weight="+weight + " leftPos="+leftPos + " rightPos="+rightPos);
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
        // Satisfy testZeroSum
        final Long l = GenericTypeValidator.formatLong(code);
        if (l == null) {
            throw new CheckDigitException(CheckDigitException.invalidCode(code));
        }
        if (l == 0) {
            throw new CheckDigitException(CheckDigitException.ZERO_SUM);
        }
        return toCheckDigit(INSTANCE.calculateModulus(code, false));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override because valid codes has "0" calculated check digit
     * and hence Danish TIN/VATIN does not contain a check digit.
     * </p>
     */
    @Override
    public boolean isValid(final String code) {
        try {
            return "0".equals(calculate(code));
        } catch (final CheckDigitException ex) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override because charValue 0 is the only valid check digit value.
     * </p>
     */
    @Override
    protected String toCheckDigit(final int charValue) throws CheckDigitException {
        if (charValue == 0) {
            return super.toCheckDigit(charValue);
        }
        throw new CheckDigitException("Invalid Check Digit Value =" + +charValue);
    }

}
