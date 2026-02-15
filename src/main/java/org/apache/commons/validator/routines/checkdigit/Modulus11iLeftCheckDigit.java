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
 * Check digit calculation based on <em>modulus 11</em> and weighs based on the digit position.
 * <p>
 * Digits are weighted based by their position, from left to right with the 
 * first digit being weighted 1, the second 2 and so on. Check digit cannot be 10.
 * </p>
 * <p>
 * This module is used to calculate Hungarian TIN check digits „adóazonosító jel“. 
 * See <a href="https://hu.wikipedia.org/wiki/Ad%C3%B3azonos%C3%ADt%C3%B3_jel">Wikipedia (hu)</a>
 * for more details.
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 2.10.6
 */
public class Modulus11iLeftCheckDigit extends Modulus11iBSNCheckDigit {

    private static final long serialVersionUID = -6055173352252600811L;

    /** Singleton Check Digit instance */
    private static final Modulus11iLeftCheckDigit INSTANCE = new Modulus11iLeftCheckDigit();

    /**
     * Gets the singleton instance of this class.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }
    Modulus11iLeftCheckDigit() {
        super();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override to handle weights 1 2 3 4 5 6 7 8 9, then 1 2 3 ... as left position.
     * </p>
     */
    @Override
    protected int weightedValue(int charValue, int leftPos, int rightPos) throws CheckDigitException {
        if (leftPos > 18) {
            throw new CheckDigitException("Code is longer than 18 chars");
        }
        int weight = leftPos > 9 ? leftPos - 9 : leftPos;
//	    System.out.println("weightedValue use leftPos weight "+weight+" charValue="+charValue + " leftPos="+leftPos + " rightPos="+rightPos);
        return rightPos == 1 ? 0 : charValue * weight;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override to handle charValue 10 as invalid check digit value.
     * </p>
     */
    @Override
    protected String toCheckDigit(final int charValue) throws CheckDigitException {
        if (charValue == 10) {
            throw new CheckDigitException(CheckDigitException.invalidCheckDigitValue(charValue));
        }
        return super.toCheckDigit(charValue);
    }

}
