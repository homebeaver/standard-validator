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
package org.apache.commons.validator.routines;

import org.apache.commons.validator.routines.checkdigit.Modulus10_31CheckDigit;
import org.apache.commons.validator.routines.checkdigit.Modulus7CheckDigit;

/**
 * DHL tracking number Validator.
 * <p>
 * There are two formats, a 20-digit number and a 10-digit Express tracking number.
 * </p>
 * <p>
 * Example: <br>
 * <code>00340433836214454077</code> 
 *  see <a href="https://franken-magazin.de/blog/00340-paket/">franken-magazin.de/blog</a>
 * <br>
 * <code>6486005945</code> DHL Express
 * </p>
 */
public class DHLTrackingNumberValidator {

    private static final String DHL_EXPRESS_REGEX = "(\\d{10})";
    private static final String DHL_00340_REGEX = "(\\d{20})";

    private static final int DHL_EXPRESS_LEN = 10;
    private static final int DHL_00340_LEN = 20;

    /** The singleton instance which uses the default formats */
    private static final DHLTrackingNumberValidator DEFAULT_DHL_VALIDATOR = new DHLTrackingNumberValidator();

    private final CodeValidator expressValidator = new CodeValidator(DHL_EXPRESS_REGEX, DHL_EXPRESS_LEN, Modulus7CheckDigit.getInstance());
    private final CodeValidator dhl00340Validator = new CodeValidator(DHL_00340_REGEX, DHL_00340_LEN, Modulus10_31CheckDigit.getInstance());

    /**
     * Return a singleton instance of the validator using the default formats
     *
     * @return A singleton instance of the validator
     */
    public static DHLTrackingNumberValidator getInstance() {
        return DEFAULT_DHL_VALIDATOR;
    }

    /**
     * Check the code is either a valid DHL-Express or DHL-20 code.
     *
     * @param code The code to validate.
     * @return {@code true} if a valid ISBN-10 or ISBN-13 code, otherwise {@code false}.
     */
    public boolean isValid(final String code) {
        return isValidDHLExpress(code) || isValidDHL00340(code);
    }

    private boolean isValidDHLExpress(final String code) {
        /* <code>code.length()==LEN</code> sorgt dafür, dass FORMAT pur angewendet wird,
         * whitespaces (TAB, NewLine) als Präfix oder Suffix der nummer liefern invalid.
         * Der &&-Operand darf nicht vorne stehen, bei code==null führt es zu NPE!
         */
        return expressValidator.isValid(code) && code.length()==DHL_EXPRESS_LEN;
    }

    private boolean isValidDHL00340(final String code) {
        return dhl00340Validator.isValid(code) && code.length()==DHL_00340_LEN;
    }

}
