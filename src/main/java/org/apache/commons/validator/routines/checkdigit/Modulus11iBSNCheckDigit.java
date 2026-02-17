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
 * Check digit calculation based on <em>modulus 11</em> and weights based on the digit position.
 * <p>
 * Digits are weighted based by their position, from right to left like in the super class.
 * The difference to <em>i-weighted</em> super class is how the check digit is calculated.
 * Here the check digit is modulus 11 of the weighted sum. In super however the check digit is
 * <code>(11 - modulusResult) % 11</code>
 * </p>
 * <p>
 * This module is used to calculate the TIN_NL Burgerservicenummer (BSN) check digits.
 * See <a href="https://nl.wikipedia.org/wiki/Burgerservicenummer">Wikipedia</a>
 * for more details.
 * </p>
 * @since 2.10.5
 */
public class Modulus11iBSNCheckDigit extends Modulus11iWeightCheckDigit {

    private static final long serialVersionUID = 6574749379620087578L;

    /** Singleton Check Digit instance */
    private static final Modulus11iBSNCheckDigit INSTANCE = new Modulus11iBSNCheckDigit();

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
    Modulus11iBSNCheckDigit() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String calculate(final String code) throws CheckDigitException {
        if (GenericValidator.isBlankOrNull(code)) {
            throw new CheckDigitException(CheckDigitException.MISSING_CODE);
        }
        return toCheckDigit(calculateModulus(code, false));
    }

}
