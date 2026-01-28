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
package org.apache.commons.validator.routines;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.routines.checkdigit.CheckDigit;
import org.apache.commons.validator.routines.checkdigit.IsoIecHybrid1110System;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;
import org.apache.commons.validator.routines.checkdigit.ModulusTenCheckDigit;
import org.apache.commons.validator.routines.checkdigit.Mudulus31CheckDigit;
import org.apache.commons.validator.routines.checkdigit.TidDECheckDigit;
import org.apache.commons.validator.routines.checkdigit.TidROCheckDigit;
import org.apache.commons.validator.routines.checkdigit.Modulus11DKCheckDigit;
import org.apache.commons.validator.routines.checkdigit.VATidBECheckDigit;
import org.apache.commons.validator.routines.checkdigit.VATidBGCheckDigit;
import org.apache.commons.validator.routines.checkdigit.VATidESCheckDigit;

/**
 * Tax identification number (TIN) Validator.
 * <p>
 * The validator includes a default set of formats and check routines for countries
 * </p>
 * <p>
 * This can be adjusted f.i. adding a new country routines by creating a validator and using the
 * {@link #setValidator(String, int, String, CheckDigit)}
 * method to add (or remove) an entry.
 * </p>
 * <p>
 * For example:
 * </p>
 * <pre>
 * TINValidator v = new TINValidator();
 * v.setValidator("AT", 9, "\\d{9}", TidATCheckDigit.getInstance());
 * </pre>
 * <p>
 * The singleton default instance cannot be modified in this way.
 * </p>
 * @since 1.10.0
 */
public class TINValidator {

    private static final Log LOG = LogFactory.getLog(TINValidator.class);

    /**
     * The validation class
     */
    public static class Validator {
        /*
         * The minimum length does not appear to be defined.
         * Denmark, Finnland are currently the shortest (including countryCode).
         */
        private static final int MIN_LEN = 10;
        private static final int MAX_LEN = 16;

        final String countryCode;
        final RegexValidator regexValidator;
        final int tinLength; // used to avoid unnecessary regex matching
        final CheckDigit routine;

        /**
         * Creates the validator.
         * @param cc the country code
         * @param maxLength the max length of the TIN for the country code
         * @param regex the regex to use to check the format.
         * @param routine the Check Digit routine
         */
        public Validator(final String cc, final int maxLength, final String regex, final CheckDigit routine) {
            if (!(cc.length() == 2 && Character.isUpperCase(cc.charAt(0)) && Character.isUpperCase(cc.charAt(1)))) {
                throw new IllegalArgumentException("Invalid country Code; must be exactly 2 upper-case characters");
            }
            if (maxLength > MAX_LEN || maxLength < MIN_LEN) {
                throw new IllegalArgumentException("Invalid length parameter, must be in range " + MIN_LEN + " to " + MAX_LEN + " inclusive: " + maxLength);
            }
//            if (!regex.startsWith(cc)) {
//                throw new IllegalArgumentException("countryCode '" + cc + "' does not agree with format: " + regex);
//            }
            this.countryCode = cc;
            this.tinLength = maxLength;
            this.regexValidator = new RegexValidator(regex);
            this.routine = routine;
        }
        /**
         * A convinient ctor to create a validator.
         * @param cc the country code
         * @param routine the Check Digit routine
         * @param maxLength the max length of the TIN for the country code
         * @param regex the regex to use to check the format without country code.
         */
        private Validator(final String cc, final CheckDigit routine, final int maxLength, final String regex) {
            this(cc, maxLength, regex, routine);
        }

        /**
         * Gets the RegexValidator.
         *
         * @return the RegexValidator.
         */
        public RegexValidator getRegexValidator() {
            return regexValidator;
        }
    }

    private static final int COUNTRY_CODE_LEN = 2;
    private static final String INVALID_COUNTRY_CODE = "No CheckDigit routine or invalid country, code=";
    private static final String CANNOT_MODIFY_SINGLETON = "The singleton validator cannot be modified";

    private static final String REGEX_NON_DIGITS = "[^\\d]";

    private static final String AT = "AT";
    /**
     * AT Finanzamtsnummern bis 2020
     * See <a href="https://de.wikipedia.org/wiki/Abgabenkontonummer#Finanzamtsnummern">Wikipedia</a>
     * FA-NNN/NNNP with two non-capturing groups
     */
//    private List<String> atFA = Arrays.asList("03", "04", "06", "07", "08", "09", "10", "12", "15", "16"
//        , "18", "22", "23", "29", "33", "38", "41", "46", "51", "52", "53", "54", "57", "59", "61", "65"
//        , "67", "68", "69", "71", "72", "81", "82", "83", "84", "90", "91", "93", "97", "98");
    private static final String REGEX_AT = "(\\d{2})(?:-|\\s)?(\\d{3})(?:/?)(\\d{4})";

    private static final String BE = "BE";
    /**
     * BE Numéro National (NN)
     * See <a href="https://fr.wikipedia.org/wiki/Num%C3%A9ro_de_registre_national">Wikipedia (fr)</a>
     * YY.MM.DD-999.PP with non-capturing groups
     */
    private static final String REGEX_BE = "(\\d{2})(?:.|\\s)?([0-1]\\d)(?:.|\\s)?([0-3]\\d)(?:-|\\s)?(\\d{3})(?:.|\\s)?(\\d{2})";

    private static final String BG = "BG";
    /**
     * BG Edinen grazhdanski nomer EGN (ЕГН)
     * See <a href="https://en.wikipedia.org/wiki/Unique_citizenship_number">Wikipedia</a>
     * YYMMDDRRGP : RRP - Region+Gender
     */
/* Regions:
000 ... 043 : Blagoevgrad
044 ... 093 : Burgas
094 ... 139 : Varna
140 ... 169 : Veliko Tarnovo
170 ... 183 : Vidin
184 ... 217 : Vratsa
218 ... 233 : Gabrovo
234 ... 281 : Kardschali
282 ... 301 : Kyustendil
302 ... 319 : Lovech
320 ... 341 : Montana
342 ... 377 : Pazardzhik
378 ... 395 : Pernik
396 ... 435 : Pleven
436 ... 501 : Plowdwi
502 ... 527 : Razgrad
528 ... 555 : Russe
556 ... 575 : Silistra
576 ... 601 : Sliven
602 ... 623 : Smoljan
624 ... 721 : Sofia City
722 ... 751 : Sofia County
752 ... 789 : Stara Zagora
790 ... 821 : Dobrich
822 ... 843 : Targovischte
844 ... 871 : Haskovo
872 ... 903 : Schumen
904 ... 925 : Yambol
926 ... 999 : other
 */
    private static final String REGEX_BG = "\\d{4}[0-3]\\d{5}";

    private static final String DE = "DE";
    private static final String REGEX_DE = "[1-9]\\d{10}";

    private static final String DK = "DK";
    /**
     * DK CPR-nummer
     * See <a href="https://da.wikipedia.org/wiki/CPR-nummer">Wikipedia</a>
     * `DDMMYY-CZZG` : zehn Zeichen, der Bindestrich ist optional (non-capturing)
     */
    private static final String REGEX_DK = "(\\d{6})(?:-?)(\\d{4})";

    private static final String ES = "ES";
    private static final String REGEX_ES = "[A-Z0-9]\\d{7}[A-Z0-9]";

    private static final String FI = "FI";
    /**
     * FI Suomalainen henkilötunnus (HETU)
     * See <a href="https://en.wikipedia.org/wiki/National_identification_number#Finland">Wikipedia</a>
     * DDMMYYCZZZQ  : C - Century indicator
     */
    private static final String REGEX_FI = "(0[1-9]|[12]\\d|3[01])(0[1-9]|1[0-2])([5-9]\\d\\+|\\d\\d[-U-Y]|[0-2]\\d[A-F])(\\d{3}[A-Z0-9])";
//    "(\\d{6})(\\+|-|[A-FU-Y])?(\\d{3})([A-Z0-9])"; // simpler

    private static final String HR = "HR";
    private static final String REGEX_HR = "[1-9]\\d{10}";

    private static final String SE = "SE";
    /**
     * SE personnummer
     * See <a href="https://sv.wikipedia.org/wiki/Personnummer_i_Sverige">Wikipedia</a>
     * YYMMDD-ZZGP  : das '-' ändert sich in '+', wenn die Person 100 Jahre alt wird
     * ( auf der sv-wiki Diskussionsseite gibt es Hinweise, dass statt '+' das Datum auf YYYY erweitert wird )
     */
    private static final String REGEX_SE = "(\\d{6})(?:-|\\+)?(\\d{4})";

    private static final String PL = "PL";
    private static final int[] PL_WEIGHTS = new int[] { 1, 3, 7, 9, 1, 3, 7, 9, 1, 3 };
    /**
     * PL Numer PESEL
     * See <a href="https://pl.wikipedia.org/wiki/PESEL">Wikipedia</a>
     * YYMMDDZZZGP : das Jahrhundert wird im Monat kodiert
     */
    private static final String REGEX_PL = "(\\d{6})(\\d{5})";

    private static final String RO = "RO";
    /**
     * RO Codul de Inregistrare Fiscală (CIF), Număr de Identificare Fiscală (NIF) and Cod Numeric Personal (CNP)
     * See <a href="https://ro.wikipedia.org/wiki/Cod_numeric_personal_(Rom%C3%A2nia)">Wikipedia</a>
     * GYYMMDDJJ999P : JJ ist Region 01..52 + 70
     */
    private static final String REGEX_RO = "[1-9]\\d{2}[0-1]\\d[0-3]\\d(70|51|52|[0-4]\\d)(\\d{4})";

    private static final Validator[] DEFAULT_VALIDATORS = {
            new Validator(AT, LuhnCheckDigit.getInstance(), 11, REGEX_AT),
            new Validator(BE, VATidBECheckDigit.getInstance(), 15, REGEX_BE),
            new Validator(BG, VATidBGCheckDigit.getInstance(), 10, REGEX_BG),
            new Validator(DE, TidDECheckDigit.getInstance(), 11, REGEX_DE),
            new Validator(DK, Modulus11DKCheckDigit.getInstance(), 11, REGEX_DK),
            new Validator(ES, VATidESCheckDigit.getInstance(), 11, REGEX_ES),
            new Validator(FI, Mudulus31CheckDigit.getInstance(), 11, REGEX_FI),
            new Validator(HR, IsoIecHybrid1110System.getInstance(), 11, REGEX_HR),
            new Validator(PL, new ModulusTenCheckDigit(PL_WEIGHTS, false), 11, REGEX_PL),
            new Validator(RO, TidROCheckDigit.getInstance(), 13, REGEX_RO),
            new Validator(SE, LuhnCheckDigit.getInstance(), 11, REGEX_SE),
    };

    /** The singleton instance which uses the default formats */
    public static final TINValidator DEFAULT_TIN_VALIDATOR = new TINValidator();

    /**
     * Gets the singleton instance of the validator using the default formats
     *
     * @return A singleton instance of the validator
     */
    public static TINValidator getInstance() {
        return DEFAULT_TIN_VALIDATOR;
    }

    private final ConcurrentMap<String, Validator> validatorMap;

    /**
     * Create a default validator.
     */
    public TINValidator() {
        this(DEFAULT_VALIDATORS);
    }

    /**
     * Create an IN validator from the specified map of TIN formats.
     *
     * @param validators map of TIN formats
     */
    public TINValidator(final Validator[] validators) {
        this.validatorMap = createValidators(validators);
    }

    private ConcurrentMap<String, Validator> createValidators(final Validator[] validators) {
        final ConcurrentMap<String, Validator> map = new ConcurrentHashMap<>();
        for (final Validator validator : validators) {
            map.put(validator.countryCode, validator);
        }
        return map;
    }

    /**
     * Gets a copy of the default Validators.
     *
     * @return a copy of the default Validator array
     */
    public Validator[] getDefaultValidators() {
        return Arrays.copyOf(DEFAULT_VALIDATORS, DEFAULT_VALIDATORS.length);
    }

    /**
     * Gets the Validator for a given country
     *
     * @param cc the ISO country code
     *
     * @return the validator or {@code null} if there is no one registered.
     */
    public Validator getValidator(final String cc) {
        if (cc == null || cc.length() < COUNTRY_CODE_LEN) { // ensure we can extract the key
            return null;
        }
        return validatorMap.get(cc);
    }

    /**
     * Does the class have the required validator?
     *
     * @param cc the ISO country code
     * @return true if there is a validator for the country
     */
    public boolean hasValidator(final String cc) {
        return getValidator(cc) != null;
    }

    /**
     * Validate a TIN Code
     *
     * @param cc the country code
     * @param code The value validation is being performed on (e.g. "02476291358" a DE TestID)
     * @return {@code true} if the value is valid
     */
    public boolean isValid(final String cc, final String code) {
        final Validator validator = getValidator(cc);
        if (validator == null || code.length() > validator.tinLength || !validator.regexValidator.isValid(code)) {
            return false;
        }
        if (validator.routine == null) {
            LOG.warn(INVALID_COUNTRY_CODE + code);
            return false;
        }
        final RegexValidator regexValidator = validator.getRegexValidator();
        if (AT.equals(cc)) {
            // eliminate non digits ( two non-capturing groups )
            String cde = regexValidator.validate(code);
            return validator.routine.isValid(cde);
        } else if (BE.equals(cc)) {
            String cde = regexValidator.validate(code);
            boolean res = validator.routine.isValid(cde);
            if (res) return res;
            // die ersten zwei Ziffern sind das Geburstjahr, 
            // bei >1999, also 00,01,..,25 muss für CheckDigit eine 2 vorangestellt werden
            char[] c = cde.toCharArray();
            if (c[0]<'3') { // born 2000 .. 2029
                res = validator.routine.isValid("2"+cde);
                if (res) {
                    LOG.info(code + " indicates a birthday after 1999 YYMMDD:"+cde.substring(0, 6));
                    return res;
                }
            }
            return false;
        } else if (DK.equals(cc)) {
            // eliminate non digits ( in non-capturing group )
            String cde = regexValidator.validate(code);
            return validator.routine.isValid(cde);
        } else if (FI.equals(cc)) {
            // eliminate non digit Century indicator ( regex without non-capturing group )
            return validator.routine.isValid(code.replaceAll(REGEX_NON_DIGITS, "")+code.substring(code.length()-1));
        } else if (SE.equals(cc)) {
            // eliminate non digits ( in non-capturing group )
            String cde = regexValidator.validate(code);
            return validator.routine.isValid(cde);
        }
        return validator.routine.isValid(code);
    }

    /**
     * Installs a validator.
     * Will replace any existing entry which has the same countryCode.
     *
     * @param countryCode the country code
     * @param length the length of the TIN. Must be &ge; 8 and &le; 32.
     *  If the length is &lt; 0, the validator is removed, and the format is not used.
     * @param format the format of the TIN for the country (as a regular expression)
     * @param routine the CheckDigit module
     * @return the previous Validator, or {@code null} if there was none
     * @throws IllegalArgumentException if there is a problem
     * @throws IllegalStateException if an attempt is made to modify the singleton validator
     */
    public Validator setValidator(final String countryCode, final int length, final String format, final CheckDigit routine) {
        if (this == DEFAULT_TIN_VALIDATOR) {
            throw new IllegalStateException(CANNOT_MODIFY_SINGLETON);
        }
        if (length < 0) {
            return validatorMap.remove(countryCode);
        }
        return setValidator(new Validator(countryCode, length, format, routine));
    }

    /**
     * Installs a validator.
     * Will replace any existing entry which has the same countryCode
     *
     * @param validator the instance to install.
     * @return the previous Validator, or {@code null} if there was none
     * @throws IllegalStateException if an attempt is made to modify the singleton validator
     */
    public Validator setValidator(final Validator validator) {
        if (this == DEFAULT_TIN_VALIDATOR) {
            throw new IllegalStateException(CANNOT_MODIFY_SINGLETON);
        }
        return validatorMap.put(validator.countryCode, validator);
    }
}
