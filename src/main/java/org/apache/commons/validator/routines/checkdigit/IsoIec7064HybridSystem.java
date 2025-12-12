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

import org.apache.commons.validator.GenericValidator;

/*
 3 subklassen

    MOD 11,10 für numerische Zeichenketten == Modulus11TenCheckDigit TODO
    MOD 27,26 für alphabetische Zeichenketten
    MOD 37,36 für alphanumerische Zeichenketten

 */
/**
 * Abstract <b>Hybrid</b> check digit calculation/validation according to ISO/IEC 7064.
 * <p>
 * Hybrid system algorithms use two modulus values, M+1 and M, and generate a single check character
 * that will be the right most character of a valid input string.
 * </p>
 * <p>
 * There are three subclasses that implement
 * <ul>
 *   <li>ISO/IEC 7064, MOD 11,10 applies to numeric strings ({@link IsoIecHybrid1110System})</li>
 *   <li>ISO/IEC 7064, MOD 27,26 applies to alphabetic strings ({@link IsoIecHybrid2726System})</li>
 *   <li>ISO/IEC 7064, MOD 37,36 applies to alphanumeric strings ({@link IsoIecHybrid3736System})</li>
 * </ul>
 * <p>
 * The two numbers following “MOD” in the designation are the two modulus.
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 1.10.0
 */
public abstract class IsoIec7064HybridSystem extends ModulusCheckDigit {

    private static final long serialVersionUID = 1017150154578158635L;

    /**
     * The character set of the item to be protected.
     * @return a String of characters (numeric, alphabetic or alphanumeric)
     */
    protected abstract String getCharacterSet();

    /**
     * {@inheritDoc}
     * <p>
     * Overrides because there is no need for a weight
     * </p>
     */
    @Override
    protected int weightedValue(int charValue, int leftPos, int rightPos) throws CheckDigitException {
        return charValue;
    }

    /**
     * Constructs a modulus Check Digit routine.
     * @param modulus the first number following “MOD” in the ISO/IEC designation, f.i. 11 for "MOD 11,10"
     */
    IsoIec7064HybridSystem(final int modulus) {
        super(modulus);
    }

    @Override
    protected String toCheckDigit(final int checkdigit) throws CheckDigitException {
        return "" + getCharacterSet().charAt(checkdigit);
    }

    private int getOtherModulus() {
        return getModulus() - 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String calculate(final String code) throws CheckDigitException {
        if (code == null) {
            throw new CheckDigitException(CheckDigitException.MISSING_CODE);
        }
        return toCheckDigit(calculateModulus(code, false));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int calculateModulus(final String code, final boolean includesCheckDigit) throws CheckDigitException {
        int product = getOtherModulus();
        int sum = 0;
        for (int i = 0; i < code.length() - (includesCheckDigit ? 1 : 0); i++) {
            final int lth = code.length() + (includesCheckDigit ? 0 : 1);
            final int leftPos = i + 1;
            final int rightPos = lth - i;
            sum = toInt(code.charAt(i), leftPos, rightPos) + product;
            sum = sum % getOtherModulus();
            product = 2 * (sum == 0 ? getOtherModulus() : sum) % getModulus();
        }
        int pz = getModulus() - product;
        return pz == getOtherModulus() ? 0 : pz;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(final String code) {
        if (GenericValidator.isBlankOrNull(code)) {
            return false;
        }
        try {
            final int modulusResult = calculateModulus(code, true);
            return modulusResult == toInt(code.charAt(code.length() - 1), code.length() - 1, 1);
        } catch (final CheckDigitException ex) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overrides to handle numeric, alphabetic or alphanumeric values respectively.
     * </p>
     */
    @Override
    protected int toInt(final char character, final int leftPos, final int rightPos) throws CheckDigitException {
        int pos = getCharacterSet().indexOf(character);
        if (pos == -1 ) {
            throw new CheckDigitException(CheckDigitException.invalidCharacter(character, leftPos));
        }
        return pos;
    }

}
