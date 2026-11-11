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
 * Modulus 10 module for Check Digit calculation/validation of GLS tracking number.
 * <p>
 * Check digit calculation is based on <em>modulus 10</em> with digits being weighted
 * based by their position, from left to right.
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 2.11.1
 */
public class Modulus10_31GLSCheckDigit extends Modulus10_31CheckDigit {

    private static final long serialVersionUID = -4036222648839185325L;

	/** Singleton Check Digit instance */
    private static final Modulus10_31GLSCheckDigit INSTANCE = new Modulus10_31GLSCheckDigit();

    /**
     * Gets the singleton instance of this class.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }
    Modulus10_31GLSCheckDigit() {
        super();
    }

    /**
     * Calculates a modulus <em>Check Digit</em> for a code which does not yet have one.
     *
     * @param code The code for which to calculate the Check Digit; the check digit should not be included.
     * @return The calculated Check Digit.
     * @throws CheckDigitException if an error occurs calculating the check digit.
     */
    @Override
    public String calculate(final String code) throws CheckDigitException {
        if (GenericValidator.isBlankOrNull(code)) {
            throw new CheckDigitException(CheckDigitException.MISSING_CODE);
        }
        final int modulusResult = calculateModulus(code, false) + 1;
        final int charValue = (10 - modulusResult) % 10;
        return toCheckDigit(charValue);
    }

}
