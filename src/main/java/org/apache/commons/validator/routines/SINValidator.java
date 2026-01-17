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

import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;

/**
 * SIN (Canadian social insurance number) Validator.
 * <p>
 * The social insurance number (SIN) is a unique identifier and has become a national identification number,
 * see <A HREF="https://en.wikipedia.org/wiki/Social_insurance_number">Wikipedia</A>.
 */
public class SINValidator {

    /*
     * NOTE: the optional space groups are non-capturing (?: ... )
     */
    private static final String FORMAT = "(\\d{3})(?:\\s)?(\\d{3})(?:\\s)?(\\d{3})";

    private static final int LEN_MIN =  9;
    private static final int LEN_MAX = 11;

    private static final CodeValidator VALIDATOR =
        new CodeValidator(FORMAT, LEN_MIN, LEN_MAX, LuhnCheckDigit.getInstance());
    private static final RegexValidator NUM_VALIDATOR = new RegexValidator("\\d");

    /** The singleton instance which uses the default formats */
    private static final SINValidator DEFAULT_VALIDATOR = new SINValidator();

    /**
     * Return a singleton instance of the validator using the default formats
     *
     * @return A singleton instance of the validator
     */
    public static SINValidator getInstance() {
        return DEFAULT_VALIDATOR;
    }

    /**
     * Validate a SIN
     *
     * @param id The value validation is being performed on
     * @return <code>true</code> if the value is valid
     */
    public boolean isValid(String code) {
        /* <code>code.length()==LEN</code> sorgt dafür, dass FORMAT pur angewendet wird,
         * whitespaces (TAB, NewLine) als Präfix oder Suffix der nummer liefern invalid.
         * Der &&-Operand darf nicht vorne stehen, bei code==null führt es zu NPE!
         */
        if (VALIDATOR.isValid(code) && code.length()>=LEN_MIN && code.length()<=LEN_MAX) {
            return NUM_VALIDATOR.isValid(code.substring(code.length()-1))
                && NUM_VALIDATOR.isValid(code.substring(0, 1));
        }
        return false;
    }

}
