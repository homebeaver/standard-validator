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
 * Italian Tax identification number (TIN) Check Digit calculation/validation.
 * <p>
 * Codice fiscale
 * </p>
 * <p>
 * See <a href="https://it.wikipedia.org/wiki/Codice_fiscale">Wikipedia (it)</a>
 * for more details.
 * </p>
 *
 * @since 2.10.6
 */
public final class TidITCheckDigit extends ModulusCheckDigit implements IsoIecConstants {

    private static final long serialVersionUID = -4719922002893417631L;

	/** Singleton Check Digit instance */
    private static final TidITCheckDigit INSTANCE = new TidITCheckDigit();

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
    private TidITCheckDigit() {
        super(RADIX_26);
    }

/*
Si dividono in due gruppi i caratteri alfanumerici del codice fiscale: 
- in un gruppo vanno i caratteri la cui posizione all'interno del codice fiscale è dispari, 
- nell'altro quelli la cui posizione è pari;

fatto questo, i caratteri vengono convertiti in valori numerici secondo le seguenti tabelle;
- posizione pari
0:0 1:1 .. 9:9    01234 5  6  7  8  9  ABCDE F  G  H  I  J  K  L  M  N  O  P  Q  R  S  T  U  V  W  Y  X  Z
                  01234 5  6  7  8  9  01234 5  6  7  8  9  10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25
- posizione dispari
                  10579 13 15 17 19 21 10579 13 15 17 19 21 2  4  18 20 11 3  6  8  12 14 16 10 22 25 24 23

a questo punto, tutti i valori ricavati con le tabelle sopra vanno sommati tra di loro 
e il risultato va diviso per 26;     ==> summe MOD 26
il resto della divisione fornisce il codice identificativo, 
ottenuto dalla seguente tabella di conversione:
                                                                 10                           K
 */
    private static final int[] DISPARI = { 1,0,5,7,9,13,15,17,19,21,1,0,5,7,9,13,15,17,19,21, 2, 4,18,20,11, 3, 6, 8,12,14,16,10,22,25,24,23 };
    private static final int[]    PARI = { 0,1,2,3,4, 5, 6, 7, 8, 9,0,1,2,3,4, 5, 6, 7, 8, 9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25 };
    // XXX ableiten von abstract class IsoIec7064PureSystem
    // oder von IsoIecPure1271System - dort ist getCharacterSet ALPHANUMERIC
    protected String getCharacterSet() {
        return ALPHANUMERIC;
    }

    @Override
    protected int toInt(final char character, final int leftPos, final int rightPos) throws CheckDigitException {
//        if (getCharacterSet().indexOf(character) == -1) {
//            throw new CheckDigitException(CheckDigitException.invalidCharacter(character, leftPos));
//        }
        int i = ALPHANUMERIC.indexOf(character);
        if (i == -1) {
            throw new CheckDigitException(CheckDigitException.invalidCharacter(character, leftPos));
        }
        if (leftPos % 2 == 1) { // dispari
            return DISPARI[i];
        } else { // pari
            return PARI[i];
        }
    }
//    /** Weighting given to digits depending on their left position */
//    private static final int[] POSITION_WEIGHT = { 2,7,9,1,4,6,3,5,8,2,7,9 };
    /**
     * The <i>weighted</i> value of a character in the the value of the character.
     *
     * @param charValue The numeric value of the character.
     * @param leftPos The position of the character in the code, counting from left to right
     * @param rightPos The positionof the character in the code, counting from right to left
     * @return The value of the character.
     */
    @Override
    protected int weightedValue(int charValue, int leftPos, int rightPos) throws CheckDigitException {
//        final int weight = POSITION_WEIGHT[(leftPos - 1) % POSITION_WEIGHT.length];
//        return charValue * weight;
        return charValue;
    }

//    protected int calculateModulus(final String code, final boolean includesCheckDigit) throws CheckDigitException {
//        int total = 0;
//        for (int i = 0; i < code.length(); i++) {
//            final int lth = code.length() + (includesCheckDigit ? 0 : getCheckdigitLength());
//            final int leftPos = i + 1;
//            final int rightPos = lth - i;
//            final int charValue = toInt(code.charAt(i), leftPos, rightPos);
//            System.out.println("calculateModulus leftPos="+leftPos + "'"+code.charAt(i)+"' == "+charValue);
//            total += weightedValue(charValue, leftPos, rightPos);
//        }
//        if (total == 0) {
//            throw new CheckDigitException(CheckDigitException.ZERO_SUM);
//        }
//        return total % RADIX_26; // modulus; XXX
//    }

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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(final String code) {
        if (GenericValidator.isBlankOrNull(code)) {
            return false;
        }
        try {
            final String cd = calculate(code.substring(0, code.length() - 1));
            return code.endsWith(cd);
        } catch (final CheckDigitException ex) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override to get the non numeric check character
     * </p>
     */
    @Override
    protected String toCheckDigit(final int charValue) throws CheckDigitException {
        if (charValue >= 0 && charValue < ALPHABETIC.length()) {
            return "" + ALPHABETIC.charAt(charValue);
        }
        throw new CheckDigitException(CheckDigitException.invalidCheckDigitValue(charValue));
    }

}
