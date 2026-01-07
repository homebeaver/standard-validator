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
import org.apache.commons.validator.routines.checkdigit.Modulus10_731CheckDigit;

/**
 * Travel Document Validator.
 * <p>
 * The validator includes a default set of formats to check numbers of passports, ID-cards and visas.
 * </p>
 * <p>
 * For further information see
 *  <a href="https://en.wikipedia.org/wiki/Travel_document">Wikipedia</a>.
 * See International Civil Aviation Organization, Doc 9303: 
 * Machine Readable Travel Documents, Part 3: Specifications Common to all MRTDs
 * for more details. 
 * </p>
 * <p>
 * This can be adjusted f.i. adding a new country routines by creating a validator and using the
 * {@link #setValidator(Type, String, int, String, CheckDigit)}
 * method to add (or remove) an entry.
 * </p>
 * <p>
 * For example:
 * </p>
 * <pre>
 * TravelDocumentValidator v = new TravelDocumentValidator();
 * v.setValidator(P, "TWN", 10, "\\d{10}", Modulus10_731CheckDigit.getInstance());
 * </pre>
 * <p>
 * The singleton default instance cannot be modified in this way.
 * </p>
 * @since 2.10.5
 */
public class TravelDocumentValidator {

    private static final Log LOG = LogFactory.getLog(TravelDocumentValidator.class);

    public enum Type 
    { ID // Personalausweis
    , IR // Residence permit
    , VF // Visum F==>???
    , PD // Diplomatenpass
    , PO // Dienstpass
    , PT // Reiseausweis für Ausländer
    , PR // Reiseausweis Konvention 1951
    , PS // Reiseausweis Konvention 1954
    , PP // Reisepass (ab November 2025)
    , P {
           @Override
           public String toString() {
               return "P<";
           }
       }
    ;
    }

    /**
     * The validation class
     */
    public static class Validator {
        /*
         * The minimum length does not appear to be defined.
         * Bosnia and Herzegovina are currently the shortest (including checkdigit).
         */
        private static final int MIN_LEN = 8;
        private static final int MAX_LEN = 10;

        final Type type;
        final String countryCode;
        final RegexValidator regexValidator;
        final int docLength; // used to avoid unnecessary regex matching
        final CheckDigit routine;

        /**
         * Creates the validator.
         * @param t the ICAO document type
         * @param cc the ICAO country code
         * @param maxLength the max length of the codument number for the country
         * @param regex the regex to use to check the format.
         * @param routine the Check Digit routine
         */
        public Validator(final Type t, final String cc, final int maxLength, final String regex, final CheckDigit routine) {
            if (!(cc.length() == 3 && Character.isUpperCase(cc.charAt(0)) 
                                   && Character.isUpperCase(cc.charAt(1))
                                   && Character.isUpperCase(cc.charAt(2))
                  || cc.equals("D")
                 )) {
                throw new IllegalArgumentException("Invalid country Code; must be 3 upper-case characters");
            }
            if (maxLength > MAX_LEN || maxLength < MIN_LEN) {
                throw new IllegalArgumentException("Invalid length parameter, must be in range " + MIN_LEN + " to " + MAX_LEN + " inclusive: " + maxLength);
            }
            this.type = t;
            this.countryCode = cc;
            this.docLength = maxLength;
            this.regexValidator = new RegexValidator(regex);
            this.routine = routine;
        }
        /**
         * A convinient ctor to create a validator.
         * @param t the ICAO document type
         * @param cc the ICAO country code
         * @param routine the Check Digit routine
         * @param maxLength the max length of the TIN for the country code
         * @param regex the regex to use to check the format without country code.
         */
        private Validator(final Type t, final String cc, final CheckDigit routine, final int maxLength, final String regex) {
            this(t, cc, maxLength, regex, routine);
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

    private static final String INVALID_COUNTRY_CODE = "No CheckDigit routine or invalid country, code=";
    private static final String CANNOT_MODIFY_SINGLETON = "The singleton validator cannot be modified";
    static final String REGEX_ICAO9303 = "[A-Z0-9]{9}\\d";
    private static final String REGEX_ALLNUMERIC = "\\d{9}\\d";

    private static final Validator[] DEFAULT_VALIDATORS = {
        // ohne Beleg: sieht so aus als ob der neue Pass PP eine Stelle  mehr hat
        new Validator(Type.P , "AUT", Modulus10_731CheckDigit.getInstance(),  9, "[A-Z0-9]{1}\\d{7}\\d"),
        new Validator(Type.PP, "AUT", Modulus10_731CheckDigit.getInstance(), 10, "[A-Z0-9]{2}\\d{7}\\d"),

        new Validator(Type.P , "BEL", Modulus10_731CheckDigit.getInstance(), 10, "[A-Z0-9]{2}\\d{7}\\d"),
        // https://en.wikipedia.org/wiki/Belgian_identity_card ==> 12 digits in the form xxx-xxxxxxx-yy
//        new Validator(Type.ID, "BEL", Modulus10_731CheckDigit.getInstance(), 10, "[A-Z0-9]{2}\\d{7}\\d"),

        new Validator(Type.P , "D", Modulus10_731CheckDigit.getInstance(),  10, "[C-HJ-NP-RT-Z0-9]{9}\\d"),
        new Validator(Type.ID, "D", Modulus10_731CheckDigit.getInstance(),  10, "[C-HJ-NP-RT-Z0-9]{9}\\d"),
        
        new Validator(Type.P , "CZE", Modulus10_731CheckDigit.getInstance(),  9, "\\d{8}\\d"),
        new Validator(Type.P , "EST", Modulus10_731CheckDigit.getInstance(), 10, "[A-Z0-9]{2}\\d{7}\\d"),
        new Validator(Type.ID, "EST", Modulus10_731CheckDigit.getInstance(), 10, "[A-Z0-9]{2}\\d{7}\\d"),
        new Validator(Type.P , "FIN", Modulus10_731CheckDigit.getInstance(), 10, "[A-Z0-9]{2}\\d{7}\\d"),
        new Validator(Type.P , "FRA", Modulus10_731CheckDigit.getInstance(), 10, "\\d{2}[A-Z]{2}\\d{5}\\d"),
        new Validator(Type.P , "HRV", Modulus10_731CheckDigit.getInstance(), 10, REGEX_ALLNUMERIC),
        new Validator(Type.P , "HUN", Modulus10_731CheckDigit.getInstance(), 10, "[A-Z0-9]{2}\\d{7}\\d"),
        new Validator(Type.P , "LUX", Modulus10_731CheckDigit.getInstance(),  9, "[A-Z0-9]{8}\\d"),
        new Validator(Type.P , "NLD", Modulus10_731CheckDigit.getInstance(), 10, REGEX_ICAO9303),
        new Validator(Type.PS, "POL", Modulus10_731CheckDigit.getInstance(), 10, "[A-Z0-9]{2}\\d{7}\\d"),

        // im Specimen model ist Type.PE (?), bei ID passen die VIS und MRT nicht zueinander
        new Validator(Type.P , "ROU", Modulus10_731CheckDigit.getInstance(), 10, REGEX_ALLNUMERIC),
        new Validator(Type.ID, "ROU", Modulus10_731CheckDigit.getInstance(), 10, "[A-Z0-9]{2}\\d{7}\\d"),

        new Validator(Type.P , "SWE", Modulus10_731CheckDigit.getInstance(), 10, "[A-Z0-9]{2}\\d{7}\\d"),
        new Validator(Type.ID, "SWE", Modulus10_731CheckDigit.getInstance(), 10, "[A-Z0-9]{2}\\d{7}\\d"),

        new Validator(Type.P , "ARG", Modulus10_731CheckDigit.getInstance(), 10, REGEX_ICAO9303),
        new Validator(Type.PS, "BIH", Modulus10_731CheckDigit.getInstance(),  8, "[A-Z0-9]{7}\\d"),
        new Validator(Type.P , "RKS", Modulus10_731CheckDigit.getInstance(), 10, "[A-Z0-9]\\d{8}\\d"),

        // Die Buchstaben O (Oscar) und I (India) werden bei der Nummer der Identitätskarte oder des Schweizer Passes nicht verwendet
        new Validator(Type.P , "CHE", Modulus10_731CheckDigit.getInstance(), 10, "[A-HJ-NP-Z0-9]{8}\\d"),
        new Validator(Type.ID, "CHE", Modulus10_731CheckDigit.getInstance(),  9, "[A-HJ-NP-Z0-9]{8}\\d"),

        // im Specimen model ist Type.PN (?)
        new Validator(Type.P , "SYR", Modulus10_731CheckDigit.getInstance(), 10, "[A-Z0-9]\\d{8}\\d"),

        // bis 2021 nur Ziffern; NGP Next Generation Passport: Alpha am Anfang, meist A, C für PassCard
        new Validator(Type.P , "USA", Modulus10_731CheckDigit.getInstance(), 10, "[A-Z0-9]\\d{8}\\d"),

        new Validator(Type.P , "TWN", Modulus10_731CheckDigit.getInstance(), 10, REGEX_ALLNUMERIC),
    };

    /** The singleton instance which uses the default formats */
    public static final TravelDocumentValidator DEFAULT_VALIDATOR = new TravelDocumentValidator();

    /**
     * Gets the singleton instance of the validator using the default formats
     *
     * @return A singleton instance of the validator
     */
    public static TravelDocumentValidator getInstance() {
        return DEFAULT_VALIDATOR;
    }

    private final ConcurrentMap<String, Validator> validatorMap;

    /**
     * Create a default validator.
     */
    public TravelDocumentValidator() {
        this(DEFAULT_VALIDATORS);
    }

    /**
     * Create an IN validator from the specified map of TIN formats.
     *
     * @param validators map of TIN formats
     */
    public TravelDocumentValidator(final Validator[] validators) {
        this.validatorMap = createValidators(validators);
    }

    private ConcurrentMap<String, Validator> createValidators(final Validator[] validators) {
        final ConcurrentMap<String, Validator> map = new ConcurrentHashMap<>();
        for (final Validator validator : validators) {
            map.put(validator.type.toString()+validator.countryCode, validator);
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
     * Gets the Validator for a given type and country
     *
     * @param t the ICAO document type
     * @param cc the ICAO country code
     *
     * @return the validator or {@code null} if there is no one registered.
     */
    public Validator getValidator(final Type t, final String cc) {
        if (t == null || cc == null || cc.isEmpty()) { // ensure we can extract the key
            return null;
        }
        return validatorMap.get(t.toString()+cc);
    }

    /**
     * Does the class have the required validator?
     *
     * @param t the ICAO document type
     * @param cc the ICAO country code
     * @return true if there is a validator for the country
     */
    public boolean hasValidator(final Type t, final String cc) {
        return getValidator(t, cc) != null;
    }

    /**
     * Validate a travel document
     *
     * @param t the ICAO document type
     * @param cc the ICAO country code
     * @param code The value validation is being performed on (e.g. "9102392482" an USA Passport)
     * @return {@code true} if the value is valid
     */
    public boolean isValid(final Type t, final String cc, final String code) {
        final Validator validator = getValidator(t, cc);
        if (validator == null || code.length() > validator.docLength || !validator.regexValidator.isValid(code)) {
            return false;
        }
        if (validator.routine == null) {
            LOG.warn(INVALID_COUNTRY_CODE + code);
            return false;
        }
        return validator.routine.isValid(code);
    }

    /**
     * Installs a validator.
     * Will replace any existing entry which has the same countryCode.
     *
     * @param t the ICAO document type
     * @param cc the ICAO country code
     * @param length the length of the TIN. Must be &ge; 8 and &le; 32.
     *  If the length is &lt; 0, the validator is removed, and the format is not used.
     * @param format the format of the TIN for the country (as a regular expression)
     * @param routine the CheckDigit module
     * @return the previous Validator, or {@code null} if there was none
     * @throws IllegalArgumentException if there is a problem
     * @throws IllegalStateException if an attempt is made to modify the singleton validator
     */
    public Validator setValidator(final Type t, final String cc, final int length, final String format, final CheckDigit routine) {
        if (this == DEFAULT_VALIDATOR) {
            throw new IllegalStateException(CANNOT_MODIFY_SINGLETON);
        }
        if (length < 0) {
            return validatorMap.remove(t.toString()+cc);
        }
        return setValidator(new Validator(t, cc, length, format, routine));
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
        if (this == DEFAULT_VALIDATOR) {
            throw new IllegalStateException(CANNOT_MODIFY_SINGLETON);
        }
        return validatorMap.put(validator.type.toString()+validator.countryCode, validator);
    }
}
