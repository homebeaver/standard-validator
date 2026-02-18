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
 * Iceland Tax identification number (TIN) Check Digit calculation/validation (kennitala).
 * <p>
 * It uses weights calculated from right position <em>i started from 2 to 7</em> and repeated
 * when necessary.
 * </p>
 * <p>
 * See <a href="https://is.wikipedia.org/wiki/Kennitala">Wikipedia (is)r</a>
 * for more details.
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 2.10.7
 */
public class Modulus11i2to7CheckDigit extends Modulus11iWeightCheckDigit {

    private static final long serialVersionUID = -8388821796959216094L;

    /** Singleton Check Digit instance */
    private static final Modulus11i2to7CheckDigit INSTANCE = new Modulus11i2to7CheckDigit();

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
    private Modulus11i2to7CheckDigit() {
        super();
    }

    /**
     * Calculates the <i>weighted</i> value of a character in the code at a specified position.
     *
     * <p>TIN digits are weighted by their position from right to left.
     * The weights are 2, 3, .. to 7, Then we repeat the weight 2, 3, ...
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
//	    System.out.println("Modulus11ISCheckDigit::weight="+weight + " charValue="+charValue + " leftPos="+leftPos + " rightPos="+rightPos);
        return charValue * weight;
    }

}
