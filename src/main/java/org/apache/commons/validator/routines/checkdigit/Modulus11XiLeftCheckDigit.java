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

import org.apache.commons.validator.routines.ECIndexNumberValidator;

/**
 * Check digit calculation based on <em>modulus 11</em> and weighs based on the digit position.
 * <p>
 * Digits are weighted based by their position, from left to right with the 
 * first digit being weighted 1, the second 2 and so on. Check digit can have a value of "X" (10).
 * </p>
 * <p>
 * This module is used to calculate <b>EC index numbers</b>. 
 * EC Index Numbers are a numeric code except for the last (check) digit
 * which can have a value of "X".
 * <br>
 * Note that these module <b>do not validate</b> the input for syntax.
 * Such validation is performed by the {@link ECIndexNumberValidator}
 * </p>
 *
 * @since 1.9.0
 */
public final class Modulus11XiLeftCheckDigit extends Modulus11iLeftCheckDigit {

    private static final long serialVersionUID = 2078815937513115949L;

    /**
     * The ALPHABET for the check digit is a number or X which indicates ten.
     */
    static final int X = 10;

    /** Singleton Check Digit instance */
    private static final Modulus11XiLeftCheckDigit INSTANCE = new Modulus11XiLeftCheckDigit();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }

    /**
     * Constructs a modulus 11 Check Digit routine.
     */
    private Modulus11XiLeftCheckDigit() {
        super(); // handle weights as left position
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override to handle weights as left position.
     * </p>
     */
    @Override
    protected int weightedValue(int charValue, int leftPos, int rightPos) throws CheckDigitException {
//        int weight = leftPos;
//	    System.out.println("weightedValue use leftPos weight "+weight+" charValue="+charValue + " leftPos="+leftPos + " rightPos="+rightPos);
        return rightPos == 1 ? 0 : charValue * leftPos;
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


}
