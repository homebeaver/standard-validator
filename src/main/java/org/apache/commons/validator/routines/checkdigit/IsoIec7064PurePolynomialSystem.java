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

/**
 * TODO comment
 */
public abstract class IsoIec7064PurePolynomialSystem extends IsoIec7064PureSystem {

    private static final long serialVersionUID = -6198930160531058449L;

    /**
     * Constructs a modulus Check Digit routine.
     * @param modulus the first number following “MOD” in the ISO/IEC designation, f.i. 11 for "MOD 11-2"
     * @param checkdigitLength the check digit length is one or two
     * @see #getRadix()
     */
    IsoIec7064PurePolynomialSystem(final int modulus, final int checkdigitLength) {
        super(modulus, checkdigitLength);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The polynomial method for the pure ISO/IEC 7064 Systems is computed
     * by multiplying the value for each Character in the string by its weight.
     * </p>
     */
    @Override
    public String calculate(final String code) throws CheckDigitException {
        if (code == null) {
            throw new CheckDigitException(CheckDigitException.MISSING_CODE);
        }
        final int m = getModulus();
        int cp = calculateModulus(code, false);
        int r = (m - cp + 1) % m; // wie in super.calculate
        int checksum = r;
        if (checksum < 0) {
            checksum = m + checksum; // 10 ==> "X" / 36 ==> "*"
        } // bei 0 und 1 unverändert
//        System.out.println("calculate polynomial " + cp + " for '" + code + "' checksum=" + checksum + " r=" + r);
        return toCheckDigit(checksum);
    }
    /**
     * {@inheritDoc}
     * <p>
     * The polynomial method for the pure ISO/IEC 7064 Systems is computed
     * by multiplying the value for each Character in the string by its weight.
     * The weights can be precalculated.
     * </p>
     */
    protected int calculateModulus(final String code, final boolean includesCheckDigit) throws CheckDigitException {
        int p = 0;
        int l = includesCheckDigit ? code.length() - getCheckdigitLength() : code.length();
        for (int i = 0; i < l; i++) {
            final int leftPos = i + 1;
            final int rightPos = l - i;
            final int charValue = toInt(code.charAt(i), leftPos, rightPos);
            // multiply the character values by their weights and add the products:
            p += weightedValue(charValue, leftPos, rightPos);
        }
        // if we want a double check digit we perform one additional pass with charValue = 0
        if (getCheckdigitLength() == 2) {
            p = p * getRadix() % getModulus();
        }
        return p;
    }

    /** Weightings placeholder given to digits depending on their right position */
    private static final int[] POSITION_WEIGHT = {};
    /**
     * Gets precomputed weights. Override this in subclasses.
     * @return precalculated weights defined for the first positions.
     */
    protected int[] getWeightings() {
        return POSITION_WEIGHT;
    }
    @Override
    protected int weightedValue(int charValue, int leftPos, int rightPos) throws CheckDigitException {
        if (leftPos + rightPos > getWeightings().length && rightPos >= getWeightings().length) {
            // calculate the weighted value
            return super.weightedValue(charValue, leftPos, rightPos);
        }
        // use precalculated weight
        return charValue * getWeightings()[rightPos];
    }

}
