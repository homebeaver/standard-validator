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
 * Norway Tax identification number (TIN) Check Digit calculation/validation (fødselsnummer).
 * <p>
 * There are two checkdigits, the first weighted modulus 11 calculated here, 
 * the second <code>Modulus11i2to7CheckDigit</code>.
 * </p>
 * <p>
 * See <a href="https://no.wikipedia.org/wiki/F%C3%B8dselsnummer">Wikipedia (no)r</a>
 * for more details.
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 2.10.7
 */
public final class TidNOCheckDigit extends Modulus11iWeightCheckDigit {

    private static final long serialVersionUID = -8388821796959216094L;

    /** Singleton Check Digit instance */
    private static final TidNOCheckDigit INSTANCE = new TidNOCheckDigit();
    // used for second TIN checkdigit
    private static final CheckDigit MOD11I2TO7 = Modulus11i2to7CheckDigit.getInstance();

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
    private TidNOCheckDigit() {
        super();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override because there are two checkdigits.
     * </p>
     */
    @Override
    protected int getCheckdigitLength() {
        return 2;
    }

    /** Weighting given to digits depending on their left position */
    private static final int[] POSITION_WEIGHT = {3, 7, 6, 1, 8, 9, 4, 5, 2};

    /**
     * Calculates the <i>weighted</i> value of a character in the code at a specified position.
     *
     * <p>TIN digits are weighted by their position from left to right.
     * </p>
     *
     * @param charValue The numeric value of the character.
     * @param leftPos The position of the character in the code, counting from left to right
     * @param rightPos The positionof the character in the code, counting from right to left
     * @return The weighted value of the character.
     */
    @Override
    protected int weightedValue(int charValue, int leftPos, int rightPos) throws CheckDigitException {
//	    System.out.println("Modulus11NOCheckDigit::weight="+POSITION_WEIGHT[leftPos - 1] + " charValue="+charValue + " leftPos="+leftPos + " rightPos="+rightPos);
        return charValue * POSITION_WEIGHT[leftPos - 1];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String calculate(final String code) throws CheckDigitException {
        if (GenericValidator.isBlankOrNull(code)) {
            throw new CheckDigitException(CheckDigitException.MISSING_CODE);
        }
        if (code.length() < POSITION_WEIGHT.length) {
            throw new CheckDigitException(CheckDigitException.invalidCode(code, "too short"));
        }
        final int modulusResult = calculateModulus(code, false);
        if (modulusResult == 10) {
            // bei 10 keine Prüfziffer vergeben
            throw new CheckDigitException(CheckDigitException.invalidCheckDigitValue(modulusResult));
        }
        final int charValue = (getModulus() - modulusResult) % getModulus();
        String cd1  = toCheckDigit(charValue);
        String cd2  = MOD11I2TO7.calculate(code + cd1);
//	    System.out.println("Modulus11NOCheckDigit::calculate code:"+code + " modulusResult="+modulusResult + " cd1="+charValue + " cd2="+cd2);
        return cd1 + cd2;
    }

}
