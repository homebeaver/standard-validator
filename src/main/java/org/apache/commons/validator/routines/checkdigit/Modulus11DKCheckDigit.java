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
 * @author EUG https://github.com/homebeaver
 * @since 2.10.6
 */
public final class Modulus11DKCheckDigit extends Modulus11iBSNCheckDigit {

    private static final long serialVersionUID = -2476335527498714738L;

    /** Singleton Check Digit instance */
    private static final Modulus11DKCheckDigit INSTANCE = new Modulus11DKCheckDigit();

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
    private Modulus11DKCheckDigit() {
        super();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override because there is no checkdigit.
     * </p>
     */
    @Override
    protected int getCheckdigitLength() {
        return 0;
    }

    /**
     * Calculates the <i>weighted</i> value of a character in the code at a specified position.
     *
     * <p>TIN, VATID digits are weighted by their position from right to left.
     * There is no check digit at the right most pos where the weight is 1.
     * The next weight is 2, 3, .. to 7, Then we repeat the weight 2, 3, ...
     * </p>
     *
     * @param charValue The numeric value of the character.
     * @param leftPos The position of the character in the code, counting from left to right
     * @param rightPos The positionof the character in the code, counting from right to left
     * @return The weighted value of the character.
     */
    @Override
    protected int weightedValue(int charValue, int leftPos, int rightPos) throws CheckDigitException {
        final int weight = rightPos < 8 ?  rightPos : 2 + (rightPos - 2) % 6;
//	    System.out.println("Modulus11DKCheckDigit::weight="+weight + " charValue="+charValue + " leftPos="+leftPos + " rightPos="+rightPos);
        return charValue * weight;
    }


    /**
     * {@inheritDoc}
     * <p>
     * Override because charValue 0 is the only valid check digit value and there is no checkdigit.
     * </p>
     */
    @Override
    protected String toCheckDigit(final int charValue) throws CheckDigitException {
        if (charValue == 0) {
            return ("");
        }
        throw new CheckDigitException(CheckDigitException.invalidCheckDigitValue(charValue));
    }

}
