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
 * Digits are weighted based by their position, from right to left.
 * </p>
 * <p>
 * This module is used to calculate some VATIN and TIN check digits. For instance:
 * o número de identificacao para efeitos do imposto sobre o valor acrescentado (NIPC) in Portugal
 * or Burgerservicenummer (BSN) in the Netherlands.
 * See <a href="https://en.wikipedia.org/wiki/VAT_identification_number">Wikipedia - VAT IN for Portugal</a>
 * for more details.
 * </p>
 *
 * @since 1.10.0
 */
public class Modulus11iWeightCheckDigit extends ModulusCheckDigit {

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
    Modulus11iWeightCheckDigit() {
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
//	    System.out.println("Modulus11iWeightCheckDigit::weightedValue use useRightPos >>>>>> charValue="+charValue + " leftPos="+leftPos + " rightPos="+rightPos);
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
//	    if(charValue == 0 || charValue == 10) System.out.println("toCheckDigit >>>>>> charValue="+charValue); // XXX
        return charValue == 10 ? "0" : super.toCheckDigit(charValue);
    }

    /*
     * Valide Prüfziffern "0" ergeben sich aus check charValue 0 oder 10.
     * Die Methode der Oberklasse erkennt nur die einstellige 0, nicht die 10.
     * Daher muss sie überschrieben werden
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(final String code) {
        if (GenericValidator.isBlankOrNull(code)) {
            return false;
        }
        try {
            final String cd = calculate(code.substring(0, code.length() - getCheckdigitLength()));
            return code.endsWith(cd);
        } catch (final CheckDigitException ex) {
            return false;
        }
    }

}
