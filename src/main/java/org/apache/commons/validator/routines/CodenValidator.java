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

import org.apache.commons.validator.routines.checkdigit.CodenCheckDigit;

/**
 * Coden Validator.
 * <p>
 * Coden is a six character identifier given by Chemical Abstracts Service (CAS) to publications. 
 * There are two main types of identifiers: Serial and Nonserial publications. 
 * see <A HREF="https://en.wikipedia.org/wiki/CODEN">Wikipedia</A>.
 *
 * @author EUG https://github.com/homebeaver
 * @since 2.10.8
 */
public class CodenValidator {

    /*
     * Serial publications: AAAADC, where AAAA is a mnemonic code derived from the publication's name, 
     * D distinguishes publications which otherwise would have identical codes and C is the check character.
     * 
     * Nonserial publications: NNAAHC, where NNAA is the publication's identifier 
     * and the last two characters are as above.
     * 
     * N represents a decimal digit 0..9, 
     * A and H represent a capital letter A..Z, 
     * D represents a capital letter usually from the beginning of the alphabet (A..G), 
     * C represents a decimal digit 2..9 or a capital letter A..Z.
     */
    private static final String FORMAT = "([A-Z0-9]{2})([A-Z]{3})([A-Z2-9])";

    private static final int LEN =  6;

    private static final CodeValidator VALIDATOR =
        new CodeValidator(new RegexValidator(FORMAT), LEN, CodenCheckDigit.getInstance());

    /** The singleton instance which uses the default formats */
    private static final CodenValidator DEFAULT_VALIDATOR = new CodenValidator();

    /**
     * Return a singleton instance of the validator using the default formats
     *
     * @return A singleton instance of the validator
     */
    public static CodenValidator getInstance() {
        return DEFAULT_VALIDATOR;
    }

    /**
     * Validate a Coden
     *
     * @param coden The value validation is being performed on
     * @return <code>true</code> if the coden is valid
     */
    public boolean isValid(String coden) {
        /* <code>code.length()==LEN</code> sorgt dafür, dass FORMAT pur angewendet wird,
         * whitespaces (TAB, NewLine) als Präfix oder Suffix der nummer liefern invalid.
         * Der &&-Operand darf nicht vorne stehen, bei code==null führt es zu NPE!
         */
        return VALIDATOR.isValid(coden) && coden.length()==LEN;
    }

}
