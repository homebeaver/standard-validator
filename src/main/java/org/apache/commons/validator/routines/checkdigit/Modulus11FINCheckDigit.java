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

import java.util.AbstractMap;
import java.util.Map;

import org.apache.commons.validator.GenericValidator;

/**
 * Modulus 11-X module for FIN Check Digit calculation/validation of 11-X Numbers.
 * <p>
 * 11-X FIN Numbers are a alphanumeric code. The last (check) digit can have a value of "X".
 * </p>
 * <p>
 * Check digit calculation is based on <em>modulus 11</em> with digits being weighted
 * based by their position, from right to left.
 * If the check digit is calculated as "10" it is converted to "X".
 * </p>
 * <p>
 * See <a href="https://de.wikipedia.org/wiki/F%C3%BChrerscheinnummer">Wikipedia (de)</a>
 * for more details. And Berechnung der Prüfziffer zur FIN nach dem Modulo-11-Verfahren in
 * <a href="https://www.kba.de/DE/Themen/ZentraleRegister/ZFZR/Info_behoerden/Regelungen_ZulBescheinigungen/anlage_2_Berechnung_Pruefziffer_FIN_Modulo_11_Verfahren.pdf">kba.de</a>.
 * </p>
 *
 * @since 2.10.5
 */
public class Modulus11FINCheckDigit extends Modulus11XCheckDigit {

    private static final long serialVersionUID = 2264537301438667011L;

    /** Singleton Check Digit instance */
    private static final Modulus11FINCheckDigit INSTANCE = new Modulus11FINCheckDigit();

    /**
     * Gets the singleton instance of this class.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }
    Modulus11FINCheckDigit() {
        super();
    }

    /**
     * Transliterating the numbers in VIN (Vehicle identification number)
     */
    /*
     * Die Version der Anlage 2 stammt aus dem Jahr 2018.
     * Gegenüber VIN werden in FIN die Buchstaben I, O, und Q abgebildet (also sind erlaubt).
     * Zusässig sind auch Umlaute "ÄÖÜ" die wie "AOU" behandelt werden:
     */
    static final Map<Character, Integer> FINMAP = Map.ofEntries(
        new AbstractMap.SimpleEntry<Character, Integer>('A', 1),
        new AbstractMap.SimpleEntry<Character, Integer>('B', 2),
        new AbstractMap.SimpleEntry<Character, Integer>('C', 3),
        new AbstractMap.SimpleEntry<Character, Integer>('D', 4),
        new AbstractMap.SimpleEntry<Character, Integer>('E', 5),
        new AbstractMap.SimpleEntry<Character, Integer>('F', 6),
        new AbstractMap.SimpleEntry<Character, Integer>('G', 7),
        new AbstractMap.SimpleEntry<Character, Integer>('H', 8),
        new AbstractMap.SimpleEntry<Character, Integer>('I', 9), // not in VIN
        new AbstractMap.SimpleEntry<Character, Integer>('J', 1),
        new AbstractMap.SimpleEntry<Character, Integer>('K', 2),
        new AbstractMap.SimpleEntry<Character, Integer>('L', 3),
        new AbstractMap.SimpleEntry<Character, Integer>('M', 4),
        new AbstractMap.SimpleEntry<Character, Integer>('N', 5),
        new AbstractMap.SimpleEntry<Character, Integer>('O', 6), // not in VIN
        new AbstractMap.SimpleEntry<Character, Integer>('P', 7),
        new AbstractMap.SimpleEntry<Character, Integer>('Q', 8), // not in VIN
        new AbstractMap.SimpleEntry<Character, Integer>('R', 9),
        new AbstractMap.SimpleEntry<Character, Integer>('S', 2),
        new AbstractMap.SimpleEntry<Character, Integer>('T', 3),
        new AbstractMap.SimpleEntry<Character, Integer>('U', 4),
        new AbstractMap.SimpleEntry<Character, Integer>('V', 5),
        new AbstractMap.SimpleEntry<Character, Integer>('W', 6),
        new AbstractMap.SimpleEntry<Character, Integer>('X', 7),
        new AbstractMap.SimpleEntry<Character, Integer>('Y', 8),
        new AbstractMap.SimpleEntry<Character, Integer>('Z', 9)
        );

    /**
     * {@inheritDoc}
     * <p>
     * Override to handle character FIN mapping.
     * </p>
     */
    protected int toInt(final char character, final int leftPos, final int rightPos) throws CheckDigitException {
        if (Character.isDigit(character)) {
            return Character.getNumericValue(character);
        }
        Integer i =  FINMAP.get(character);
        if (i == null) {
            throw new CheckDigitException(CheckDigitException.invalidCharacter(character, leftPos));
        }
        return i;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override to handle weights as right position.
     * </p>
     */
    @Override
    protected int weightedValue(int charValue, int leftPos, int rightPos) throws CheckDigitException {
        int w = (rightPos+1) % 10 + 1;
        return charValue * w;
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

}
