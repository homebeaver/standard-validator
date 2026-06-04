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

import org.apache.commons.validator.routines.checkdigit.Modulus11BICCheckDigit;

/**
 * Container BIC (aka ISO 6346 Code) is a eleven-character, alphanumeric code that provides concise, 
 * unique and unambiguous identification of containers.
 * <p>
 * BIC starts with three capital letters which indicates the owner of the container.
 * The equipment category identifier is U, J or Z.
 * The serial number consists of 6 numeric digits.
 * At the end a numeric check digit follows 
 * see <A HREF="https://en.wikipedia.org/wiki/ISO_6346">Wikipedia</A>.
 * <p>
 * The European ILU Codes (Intermodal Loading Units), EN 13044, differs to BIC 
 * by the equipment category identifier which can be A, B, D, E or K
 *
 * @author EUG https://github.com/homebeaver
 * @since 2.10.8
 */
public class ContainerBICValidator {

    private static final String FORMAT = "([A-Z]{3})([ABDEJKUZ])(\\d{7})";

    private static final int LEN =  11;

    private static final CodeValidator VALIDATOR =
        new CodeValidator(new RegexValidator(FORMAT), LEN, Modulus11BICCheckDigit.getInstance());

    /** The singleton instance which uses the default formats */
    private static final ContainerBICValidator DEFAULT_VALIDATOR = new ContainerBICValidator();

    /**
     * Return a singleton instance of the validator using the default formats
     *
     * @return A singleton instance of the validator
     */
    public static ContainerBICValidator getInstance() {
        return DEFAULT_VALIDATOR;
    }

    /**
     * Validate a BIC or ILU-code
     *
     * @param code The value validation is being performed on
     * @return <code>true</code> if the coden is valid
     */
    public boolean isValid(String code) {
        /* <code>code.length()==LEN</code> sorgt dafür, dass FORMAT pur angewendet wird,
         * whitespaces (TAB, NewLine) als Präfix oder Suffix der nummer liefern invalid.
         * Der &&-Operand darf nicht vorne stehen, bei code==null führt es zu NPE!
         */
        return VALIDATOR.isValid(code) && code.length()==LEN;
    }

}
