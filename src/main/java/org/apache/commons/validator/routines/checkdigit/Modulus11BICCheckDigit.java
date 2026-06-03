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
 * BIC‐Container Code (aka ISO 6346) is a eleven-character, alphanumeric code that provides concise, 
 * unique and unambiguous identification of containers.
 * <p>
 * This works also for european ILU Codes (Intermodal Loading Units), EN 13044.
 * </p>
 * <p>
 * See <a href="https://en.wikipedia.org/wiki/ISO_6346">Wikipedia</a>
 * for more details.
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 2.10.8
 */
public final class Modulus11BICCheckDigit extends Modulus11iBSNCheckDigit implements IsoIecConstants {

    private static final long serialVersionUID = 5993546412606972307L;

    /** Singleton Check Digit instance */
    private static final Modulus11BICCheckDigit INSTANCE = new Modulus11BICCheckDigit();

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
    private Modulus11BICCheckDigit() {
        super();
    }

    @Override
    protected int toInt(final char character, final int leftPos, final int rightPos) throws CheckDigitException {
        int i = ALPHANUMERIC.indexOf(character);
        if (i == -1) {
            throw new CheckDigitException(CheckDigitException.invalidCharacter(character, leftPos));
        }
        if (i >= 11) {
            // B is 12!
            i++;
        }
        if (i >= 22) {
            // L is 23!
            i++;
        }
        if (i >= 33) {
            // V is 34!
            i++;
        }
//	    System.out.println("toInt use leftPos character "+character+" res="+i + " leftPos="+leftPos + " rightPos="+rightPos);
        return i;
    }

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
        int weight = 1;
        int i = leftPos -1;
        while (i > 0) {
            weight = weight * 2;
            i--;
        }
//	    System.out.println("weightedValue use leftPos weight "+weight+" charValue="+charValue + " leftPos="+leftPos + " rightPos="+rightPos);
        return charValue * weight;
    }

}
