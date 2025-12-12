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
 * Abstract implementation for five check digit calculation/validation defined in the ISO/IEC 7064 standard.
 * <ul>
 *   <li>ISO/IEC 7064, MOD 11-2 applies to numeric strings ({@link IsoIecPure11System})</li>
 *   <li>ISO/IEC 7064, MOD 37-2 applies to alphanumeric strings ({@link IsoIecPure37System})</li>
 *   <li>ISO/IEC 7064, MOD 97-10 applies to numeric strings ({@link IsoIecPure97System})</li>
 *   <li>ISO/IEC 7064, MOD 661-26 applies to alphabetic strings ({@link IsoIecPure661System})</li>
 *   <li>ISO/IEC 7064, MOD 1271_36 applies to alphanumeric strings ({@link IsoIecPure1271System})</li>
 * </ul>
 * <p>
 * <b>Pure</b> system algorithms use a single modulus value {@link #getModulus()} and a radix {@link #getRadix()},
 *  f.i. the modulus for MOD 11-2 is 11, the radix is 2.
 * There is also an alternative polynomial implementation for the pure systems,
 *  see {@link IsoIec7064PurePolynomialSystem} and subclasses.
 * </p>
 * <p>
 * The standard also defines hybrid systems with two modulus values (see {@link IsoIec7064HybridSystem}).
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 1.10.0
 */
public abstract class IsoIec7064PureSystem extends ModulusCheckDigit {

    private static final long serialVersionUID = 8956070914814659350L;

    /**
     * Radix is the second number following “MOD” in the ISO/IEC designation, f.i. 2 for "MOD 11-2"
     * @return the radix of the Check Digit routine
     */
    protected abstract int getRadix();
    /**
     * MOD 11-2 check characters are “0” to “9” plus “X” for example.
     * @return a String containing characters the check digit is build from.
     * This can be {@link IsoIecConstants#NUMERIC}, {@link IsoIecConstants#ALPHABETIC}, {@link IsoIecConstants#ALPHANUMERIC}
     * , {@link IsoIecConstants#NUMERIC_PLUS_X}, {@link IsoIecConstants#ALPHANUMERIC_PLUS_STAR}
     */
    protected abstract String getCharacterSet();

    private final int checkdigitLength;

    /**
     * Constructs a modulus Check Digit routine.
     * @param modulus the first number following “MOD” in the ISO/IEC designation, f.i. 11 for "MOD 11-2"
     * @param checkdigitLength the check digit length is one or two
     * @see #getRadix()
     */
    IsoIec7064PureSystem(final int modulus, final int checkdigitLength) {
        super(modulus);
        this.checkdigitLength = checkdigitLength;
    }

    /**
     * Returns the lentgth of the check digit for the system
     * @return checkdigitLength can be one ore two chars
     */
    public int getCheckdigitLength() {
        return checkdigitLength;
    }

    @Override
    public boolean isValid(final String code) {
        if (code == null) {
            return false;
        }
        try {
            if (code.length() < getCheckdigitLength()) {
                throw new CheckDigitException(CheckDigitException.invalidCode(code, "too short"));
            }
            final int cd = toInt(code.substring(code.length() - getCheckdigitLength()));
            if (cd >= getModulus()) {
                throw new CheckDigitException(CheckDigitException.invalidCode(code, "check digit = " + cd));
            }
            final int cm = calculateModulus(code, true);
            return 1 == (cd + cm) % getModulus();
        } catch (final CheckDigitException ex) {
            return false;
        }
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
        return toCheckDigit(checksum);
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
            p = (p + charValue) * r % m;
        }
        // if we want a double check digit we perform one additional pass with charValue = 0
        if (getCheckdigitLength() == 2) {
            p = p * r % m;
        }
        return p;
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
    private int toInt(final String character) throws CheckDigitException {
        //assert character.length() == checkdigitLength;
        if (character.length() == 1) {
            int pos = getCharacterSet().indexOf(character);
            if (pos == -1 ) {
                throw new CheckDigitException(CheckDigitException.invalidCharacter(character));
            }
            return pos;
        }
        // checkdigitLength == 2
        int p0 = getCharacterSet().indexOf(character.charAt(0));
        if (p0 == -1 ) {
            throw new CheckDigitException(CheckDigitException.invalidCharacter(character));
        }
        int p1 = getCharacterSet().indexOf(character.charAt(1));
        if (p1 == -1 ) {
            throw new CheckDigitException(CheckDigitException.invalidCharacter(character));
        }
        return p0 * getRadix() + p1;
    }
    /**
     * {@inheritDoc}
     * <p>
     * Overrides to handle numeric, alphabetic or alphanumeric values respectively.
     * </p>
     */
    @Override
    protected int toInt(final char character, final int leftPos, final int rightPos) throws CheckDigitException {
        if (getCharacterSet().indexOf(character) == -1) {
            throw new CheckDigitException(CheckDigitException.invalidCharacter(character, leftPos));
        }
        return Character.getNumericValue(character);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overrides because there is no need for a weight in ISO/IEC 7064 pure recursive/iterative computation.
     * However for polynomial calculation this method is used.
     * </p>
     * <p>
     * Weights are defined in subclasses for the first fifteen positions only.
     * The series can be extended indefinitely, using the formula
     * <pre>
     * wi = r^(i - 1) (mod M)
     * </pre>
     * where wi is the weight for Position i.
     * <p>
     * <b>NOTE</b>: do not use {@code Math.pow} for high results, f.i. {@code 10^23 (mod 97) = 56}.
     * Calculate it iteratively instead.
     * </p>
     */
    @Override
    protected int weightedValue(int charValue, int leftPos, int rightPos) throws CheckDigitException {
        final int r = getRadix();
        double pow = Math.pow(r, rightPos);
        if (pow > Long.MAX_VALUE) {
            long p = r; // r^1
            for (int i = 1; i < rightPos; i++) {
                p = p * r;
                if (p > Integer.MAX_VALUE) {
                    p = p % getModulus();
                }
            }
            p = p % getModulus();
//            System.out.println("weight for " + charValue + " at rightPos=" + rightPos + " is " + p + " =>weightedValue=" + (charValue * p));
            return charValue * (int) p;
        }
        double wi = Math.pow(r, rightPos) % getModulus();
//        System.out.println("weight for " + charValue + " at rightPos=" + rightPos + " is " + wi + " =>weightedValue=" + (charValue * wi));
        return charValue * (int) wi;
    }
}
