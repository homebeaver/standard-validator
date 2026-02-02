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
 * Digits are weighted based by their position, from right to left with the 
 * first digit being weighted 1, the second 2 and so on.
 * </p>
 * <p>
 * This module is used to calculate some VATIN and TIN check digits. For instance:
 * o número de identificacao para efeitos do imposto sobre o valor acrescentado (NIPC) in Portugal
 * or Burgerservicenummer (BSN) in the Netherlands.
 * See <a href="https://en.wikipedia.org/wiki/VAT_identification_number">Wikipedia - VAT IN for Portugal</a>
 * for more details.
 * </p>
 * <p>
 * A prominent possible subclass is {@link ISBN10CheckDigit}).
 * </p>
 *
 * @since 1.10.0
 */
//public final class VATidPTCheckDigit extends Modulus11XCheckDigit { TODO rename to Modulus11iWeightCheckDigit
public final class Modulus11iWeightCheckDigit extends ModulusCheckDigit {

    private static final long serialVersionUID = 3389131219768039368L;

    /** Singleton Check Digit instance */
    private static final Modulus11iWeightCheckDigit INSTANCE = new Modulus11iWeightCheckDigit();

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
    private Modulus11iWeightCheckDigit() {
        super(MODULUS_11);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implement to handle weights as right position.
     * </p>
     */
    @Override
    protected int weightedValue(int charValue, int leftPos, int rightPos) throws CheckDigitException {
        return charValue * rightPos;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override to handle charValue 10.
     * </p>
     */
    @Override
    protected String toCheckDigit(final int charValue) throws CheckDigitException {
        return charValue == 10 ? "0" : super.toCheckDigit(charValue);
    }

}
