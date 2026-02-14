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
 * Check digit calculation based on <em>modulus 11</em> for Luxembourg TIN numbers for non-natural persons.
 * <p>
 * Digits are weighted based by their position, from right to left.
 * </p>
 * 
 * @since 2.10.5
 */
public class TidLUCheckDigit extends Modulus11iWeightCheckDigit {

    private static final long serialVersionUID = 7056068269876852557L;

    /** Singleton Check Digit instance */
    private static final TidLUCheckDigit INSTANCE = new TidLUCheckDigit();
    // used for TIN numbers for natural persons
    private static final ModulusCheckDigit LUHN = new LuhnCheckDigit();
    private static final VerhoeffCheckDigit VERHOEFF = new VerhoeffCheckDigit();

    private static final int LEN = 11; // non-natural persons incl. one check digit
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
    TidLUCheckDigit() {
        super();
    }

/*
Structure and syntax of Luxembourg TIN-like numbers for non-natural persons: 
The identifier has 11 digits (99999999999), the last digit is a check digit.

The 11th digit corresponds to the difference between 11 and the remainder of the division by 11 of the 
sum of the products obtained by multiplying each of the first 10 digits of the ID number by the 
respective factors of 5, 4, 3, 2, 7, 6, 5, 4, 3 and 2, 
XXX das entspricht von rechts 2 3 4 5 6 7 und 2 3 4 5 , also i aka right position

being understood that of the numbers generated, during the abovementioned division, 
a remainder of 1 is not allocated. XXX charValue == 10
A remainder of zero during that division is the check digit.
------------
Structure and syntax of Luxembourg TIN-like numbers for natural persons:  
The identification number has 13 digits (9999999999999), the 2 last digits are check digits. 

The 12 th digit is a check digit calculated on the basis of the algorithm “de Luhn 10”, 
calculated on the 11 first digits.  
The 13th digit is a check digit calculated on the basis of the algorithm “de Verhoeff”, 
calculated on the 11 first digits.
 */
    /**
     * {@inheritDoc}
     * <p>
     * Override to handle weights 2 3 4 5 6 7  as right position, then 2 3 ....
     * </p>
     */
    @Override
    protected int weightedValue(int charValue, int leftPos, int rightPos) throws CheckDigitException {
        int weight = rightPos > 7 ? rightPos - 6 : rightPos;
	    System.out.println("Modulus11iLUCheckDigit::weightedValue "+weight+" use useRightPos >>>>>> charValue="+charValue + " leftPos="+leftPos + " rightPos="+rightPos);
        return rightPos == 1 ? 0 : charValue * weight;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override to handle charValue 10.
     * </p>
     */
    @Override
    protected String toCheckDigit(final int charValue) throws CheckDigitException {
//	    if(charValue == 0 || charValue == 10) System.out.println("toCheckDigit >>>>>> charValue="+charValue); // XXX
        if (charValue == 10) {
            throw new CheckDigitException(CheckDigitException.invalidCheckDigitValue(charValue));
        }
        return super.toCheckDigit(charValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String calculate(final String code) throws CheckDigitException {
        if (GenericValidator.isBlankOrNull(code)) {
            throw new CheckDigitException(CheckDigitException.MISSING_CODE);
        }
        if (code.length() > LEN - 1) {
            // natural persons with two check digits
            String luhnCd = LUHN.calculate(code);
            String verhoeffCd = VERHOEFF.calculate(code);
            return luhnCd + verhoeffCd;
        }
        // non natural persons with one check
        return super.calculate(code);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override to handle code for natural persons (length 13 with two check digits)
     * and code for non natural persons (lenght 11 with one check digit).
     */
    @Override
    public boolean isValid(final String code) {
        if (GenericValidator.isBlankOrNull(code)) {
            return false;
        }
        try {
            if (code.length() > LEN) {
                // natural persons with two check digits
                final String cd = calculate(code.substring(0, code.length() - 2));
                return code.endsWith(cd);
            }
            final String cd = calculate(code.substring(0, code.length() - 1));
            return code.endsWith(cd);
        } catch (final CheckDigitException ex) {
            return false;
        }
    }

}
