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

import org.apache.commons.validator.routines.checkdigit.RFCreditorReferenceCheckDigit;

/**
 * RF Creditor Reference Validator.
 * <p>
 * The Structured Creditor Reference is an international business standard based on ISO 11649.
 * Using Creditor Reference, a company can automatically match its remittance information to its A/R. 
 * The Creditor Reference is an alphanumeric string, up to 25 characters long, 
 * prefixed with the letters "RF" and two check digits.
 * <p>
 * For further information see ISO-11649 or 
 *  <A HREF="https://en.wikipedia.org/wiki/Creditor_Reference">wikipedia</A>.
 */
public class RFCreditorReferenceValidator {

    /**
     * The Format of the RF Creditor Reference is 2!a2!n21c :
     * - the first two letters shall always be "RF".
     * - the third and fourth characters shall be the check digits, 
     *   as calculated from the scheme defined on ISO-11649.
     * - the remaining part of the RF Creditor Reference (up to 21c), creditor reference, 
     *   shall only contain upper and lower case letters and numeric characters.
     */
    static final String FORMAT = "(RF)(\\d{2})([A-Za-z0-9]+)";

    static RegexValidator FORMAT_VALIDATOR = new RegexValidator(FORMAT);

    /**
     * in theory the shortest RF Creditor Reference has "RF", the two check digits and one char :
     *  <code>RF99x</code>
     */
    private static final int MIN_CODE_LEN = 5;

    private static final int MAX_CODE_LEN = 25;

    
    private static final CodeValidator VALIDATOR = new CodeValidator(new RegexValidator(FORMAT), 
        MIN_CODE_LEN, MAX_CODE_LEN, RFCreditorReferenceCheckDigit.getInstance());

    /** The singleton instance which uses the default formats */
    private static final RFCreditorReferenceValidator SINGLETON = new RFCreditorReferenceValidator();

    /**
     * Return a singleton instance of the validator using the default formats
     *
     * @return A singleton instance of the validator
     */
    public static RFCreditorReferenceValidator getInstance() {
        return SINGLETON;
    }

    /**
     * Validate a RF Creditor Reference
     *
     * @param id The value validation is being performed on
     * @return <code>true</code> if the value is valid
     */
    public boolean isValid(String id) {
        if (!FORMAT_VALIDATOR.isValid(id)) {
//            System.out.println("id="+id+">>> FORMAT_VALIDATOR is not valid");
            return false;
        }
        return VALIDATOR.isValid(id);
    }

}
