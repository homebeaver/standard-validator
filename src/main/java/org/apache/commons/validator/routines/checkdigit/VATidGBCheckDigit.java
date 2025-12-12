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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;

/**
 * United Kingdom value added tax registration number
 * <p>
 * The VAT number can either be a 9-digit standard number,
 * a 12-digit standard number followed by a 3-digit branch identifier,
 * a 5-digit number for government departments (first two digits are GD) or
 * a 5-digit number for health authorities (first two digits are HA).
 * The 9-digit variants use a weighted checksum.
 * </p>
 * <p>
 * See <a href="https://en.wikipedia.org/wiki/VAT_identification_number">Wikipedia - VAT IN</a>
 * for more details.
 * </p>
 *
 * @since 1.10.0
 */
public final class VATidGBCheckDigit extends ModulusCheckDigit implements IsoIecConstants {

    private static final long serialVersionUID = -1297016213259512985L;
    private static final Log LOG = LogFactory.getLog(VATidGBCheckDigit.class);

    /** Singleton Check Digit instance */
    private static final VATidGBCheckDigit INSTANCE = new VATidGBCheckDigit();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }

    static final int LEN = 9; // with Check Digit

    private static final int MODULUS97_55 = 55; // the modifier for Modulus 9755 algorithm

    private final int checkdigitLength = 2;
    public int getCheckdigitLength() {
        return checkdigitLength;
    }

    /**
     * Constructs a Check Digit routine.
     */
    private VATidGBCheckDigit() {
        super(MODULUS_97);
    }

//    @Override
    protected int getRadix() {
        return RADIX_10;
    }

    protected String getCharacterSet() {
        return NUMERIC;
    }

    /**
     * Calculates the <i>weighted</i> value of a character in the
     * code at a specified position.
     *
     * <p>For VATID digits are weighted by their position from left to right.</p>
     *
     * @param charValue The numeric value of the character.
     * @param leftPos The position of the character in the code, counting from left to right
     * @param rightPos The position of the character in the code, counting from right to left
     * @return The weighted value of the character.
     */
    @Override
    protected int weightedValue(final int charValue, final int leftPos, final int rightPos) {
        return charValue * rightPos;

    }

    @Override
    protected String toCheckDigit(final int checksum) throws CheckDigitException {
        String chars = getCharacterSet();
        if (getCheckdigitLength() == 2) {
            int second = checksum % getRadix();
            int first = (checksum - second) / getRadix();
            return "" + chars.charAt(first) + chars.charAt(second);
        } else {
            return "" + chars.charAt(checksum);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overridden to handle two digits as <em>Check Digit</em> and MOD 9755
     * </p>
     */
    @Override
    public String calculate(final String code) throws CheckDigitException {
        if (GenericValidator.isBlankOrNull(code)) {
            throw new CheckDigitException(CheckDigitException.MISSING_CODE);
        }
        try {
            long l = Long.parseLong(code); // throws NumberFormatException
            if (l == 0) {
                throw new CheckDigitException(CheckDigitException.ZERO_SUM);
            }
        } catch (final NumberFormatException ex) {
            System.out.println("Expected exception for invalid high codes. " + ex.getMessage());
            // Expected exception for high codes f.i. 99999999999999999999999
            throw new CheckDigitException(CheckDigitException.invalidCode(code));
        }

        int modulusResult = calculateModulus(code, false);
        if (LOG.isDebugEnabled()) {
            int mr55 = (MODULUS97_55 + modulusResult) % MODULUS_97;
            int newStyle = MODULUS_97 - mr55;
            LOG.debug(code + " modulusResult=" + modulusResult + " - old style cd = " + (MODULUS_97 - modulusResult)
                + " and MOD9755-style " + newStyle);
        }
        // There are more than one possible VATIN check digits for a given code,
        // one old style MOD 97 and one new style MOD 9755
        // thus, it isn't possible to compute the right one.
        // here I return old style MOD 97 check digit
        return ((Modulus97CheckDigit)Modulus97CheckDigit.getInstance()).toCheckDigit(modulusResult == 0 ? 0 : MODULUS_97 - modulusResult);
        // this retuens MOD 9755
        //return Modulus97CheckDigit.toCheckDigit(newStyle);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overridden to handle two digits as <em>Check Digit</em>, ignore tree branch digits and MOD 9755
     * </p>
     */
    @Override
    public boolean isValid(final String ocode) {
        String code = ocode;
        if (GenericValidator.isBlankOrNull(code)) {
            return false;
        }
        if (code.length() > LEN) {
            if (code.length() != LEN + 3) {  // CHECKSTYLE IGNORE MagicNumber
                return false;
            }
            // ignore three branch digits
            code = code.substring(0, LEN);
        }

        try {
            if (code.length() < getCheckdigitLength()) {
              throw new CheckDigitException(CheckDigitException.invalidCode(code, "too short"));
          }
            // without the leading "0" the following would be valid 8888502+4
            Integer cd = GenericTypeValidator.formatInt("0" + code.substring(code.length() - getCheckdigitLength()));
            if (cd == null) {
                throw new CheckDigitException(CheckDigitException.invalidCode(code));
            }
            if (cd >= MODULUS_97) {
                throw new CheckDigitException(CheckDigitException.invalidCode(code, "check digit >= " + MODULUS_97));
            }
            int modulusResult = calculateModulus(code.substring(0, code.length() - getCheckdigitLength()), false);
            if (0 == (modulusResult + cd) % MODULUS_97) {
                return true; // old style MOD 97
            }
            if (0 == (modulusResult + cd + MODULUS97_55) % MODULUS_97) {
                return true; // new style MOD 9755
            }
            return false;
        } catch (final CheckDigitException ex) {
            return false;
        }

    }

}
