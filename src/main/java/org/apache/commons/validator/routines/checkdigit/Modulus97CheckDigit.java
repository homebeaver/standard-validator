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
 * MOD 97-10 module similar to ISO/IEC 7064, MOD 97-10.
 * In difference to the standard this module applies to alphanumeric Strings.
 * Check digits can be from 02 to 98 (00 and 01 are not possible)

 * <p>
 * See <a href="https://de.wikipedia.org/wiki/ISO/IEC_7064">Wikipedia - ISO/IEC_7064 (de)</a>
 * for more details on ISO/IEC 7064, MOD 97-10
 * </p>
 *
 * <p>
 * This MOD 97-10 module can be used to validate the LEI (Legal Entity Identifier), ICD id 0199
 * </p>
 *
 * @see IsoIecPure97System
 * @since 1.10.0
 */
public class Modulus97CheckDigit extends IsoIec7064PureSystem implements IsoIecConstants {

    private static final long serialVersionUID = -5746038014913492394L;

    /** Singleton Check Digit instance */
    private static final Modulus97CheckDigit INSTANCE = new Modulus97CheckDigit();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }
    Modulus97CheckDigit() {
        super(MODULUS_97, 2);
    }

    @Override
    protected int getRadix() {
        return RADIX_10;
    }

    @Override
    protected String getCharacterSet() {
        return NUMERIC;
    }

    @Override
    public String calculate(final String code) throws CheckDigitException {
        if (code == null) {
            throw new CheckDigitException(CheckDigitException.MISSING_CODE);
        }
        final int m = getModulus();
        final int cm = calculateModulus(code, false);
        // now compute what checksum will be congruent to 1 mod M
        int checksum = (m - cm + 1) % m;
        // check digits can be from 02-98 (00 and 01 are not possible)
        return toCheckDigit(checksum > 1 ? checksum : checksum + m);
    }

    @Override
    protected int calculateModulus(final String code, final boolean includesCheckDigit) throws CheckDigitException {
        final int m = getModulus();
        final int r = getRadix();
        // process the code
        int p = 0;
        int l = includesCheckDigit ? code.length() - getCheckdigitLength() : code.length();
        for (int i = 0; i < l; i++) {
            final int leftPos = i + 1;
            final int rightPos = l - i;
            final int charValue = toInt(code.charAt(i), leftPos, rightPos);
            if (charValue >= NUMERIC.length()) {
                p = (p + charValue / r) * r % m;
                p = (p + charValue % r) * r % m;
            } else {
                p = (p + charValue) * r % m;
            }
        }
        // if we want a double check digit we perform one additional pass with charValue = 0
        if (getCheckdigitLength() == 2) {
            p = p * r % m;
        }
        return p;
    }
    @Override
    public boolean isValid(final String code) {
        if (code == null) {
            return false;
        }
//        String check = null;
        try {
            if (code.length() < getCheckdigitLength()) {
                throw new CheckDigitException(CheckDigitException.invalidCode(code, "too short"));
            }
//            check = code.substring(code.length() - getCheckdigitLength());
//         // without the leading "0" the following would be valid "+4"
//            final int cd = Integer.parseInt("0" + check);
//            if (cd==0 || cd==1 || cd==99) {
//                return false;
//            }
//            final int cm = calculateModulus(code, true);
//            return 1 == (cd + cm) % getModulus();
            // oder kurz:
            return code.endsWith(INSTANCE.calculate(code.substring(0, code.length() - getCheckdigitLength())));
//        } catch (final NumberFormatException ex) {
//            System.out.println(code + " check digit=" + check + " ==> " + ex);
//            return false;
        } catch (final CheckDigitException ex) {
            return false;
        }
    }

    @Override
    protected int toInt(final char character, final int leftPos, final int rightPos) throws CheckDigitException {
        if (ALPHANUMERIC.indexOf(character) == -1) {
            throw new CheckDigitException(CheckDigitException.invalidCharacter(character, leftPos));
        }
        return Character.getNumericValue(character);
    }
}
