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
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.routines.checkdigit.CheckDigit;
import org.apache.commons.validator.routines.checkdigit.IsoIecHybrid1110System;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;
import org.apache.commons.validator.routines.checkdigit.TidDECheckDigit;

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
 * INValidator v = new TINValidator();
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

    private static final Validator[] DEFAULT_VALIDATORS = {
            new Validator("AT", LuhnCheckDigit.getInstance(), 11, "\\d{2}(-|\\s)?\\d{3}(/)?\\d{4}"),
            new Validator("DE", TidDECheckDigit.getInstance(), 11, "[1-9]\\d{10}"),
            new Validator("HR", IsoIecHybrid1110System.getInstance(), 11, "[1-9]\\d{10}"),
    };

    /** The singleton instance which uses the default formats */
    public static final TINValidator DEFAULT_TIN_VALIDATOR = new TINValidator();

    /**
     * Gets the singleton instance of the IBAN validator using the default formats
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

    /*
     * Finanzamtsnummern bis 2020
     * See <a href="https://de.wikipedia.org/wiki/Abgabenkontonummer#Finanzamtsnummern">Wikipedia</a>
     */
    private List<String> atFA = Arrays.asList("03", "04", "06", "07", "08", "09", "10", "12", "15", "16"
        , "18", "22", "23", "29", "33", "38", "41", "46", "51", "52", "53", "54", "57", "59", "61", "65"
        , "67", "68", "69", "71", "72", "81", "82", "83", "84", "90", "91", "93", "97", "98");

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
        if ("AT".equals(cc)) {
            String fa = code.substring(0, 2);
            return atFA.contains(fa) ? validator.routine.isValid(code.replaceAll("[^\\d]", "")) : false;
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
