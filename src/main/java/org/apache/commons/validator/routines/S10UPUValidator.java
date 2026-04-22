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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.routines.checkdigit.S10UPUCheckDigit;

/**
 * International postal items Validator defined in S10 UPU (Union postale universelle)
 * <p>
 * For further information see
 *  <a href="https://en.wikipedia.org/wiki/S10_(UPU_standard)">Wikipedia</a>.
 * See UPU 2018: S10-12.pdf: 
 * Identification of postal items – 13-character identifier
 * for more details. 
 * </p>
 * @author EUG https://github.com/homebeaver
 * @since 2.10.8
 */
public class S10UPUValidator {

    private static final Log LOG = LogFactory.getLog(S10UPUValidator.class);

    /**
     * S10 identifiers have four components: 
     * <br>- alpha Service indicator
     * <br>- numeric Serial number
     * <br>- numeric Check digit
     * <br>- alpha Country code
     */
    /* Service indicator : two alpha, the first indicates Type of product
     * E : Express Mail Service , Example EE 123 456 785 KR
     * L,M,Q,R,U,V : Letter post (QA-QM:nternational Business Reply Service, R:registered, V:insured)
     * C,H : Parcel post
     * A,B,D,G,N,P,Z : domestic/bilateral/multilateral use
     */
    private static final String SI_FORMAT = "([ABCDEHLMNPRUVZ][A-Z]|Q[A-M]|GA|GD)";
//    private static final String SI_FORMAT = "([ABCDEGHLMNPQRUVZ][A-Z])";
    private static final String CC_FORMAT = "([A-Z]{2})";
    /*
     * NOTE: the optional space groups are non-capturing (?: ... )
     */
    private static final String FORMAT = SI_FORMAT + "(?:\\s)?(\\d{3})(?:\\s)?(\\d{3})(?:\\s)?(\\d{3})(?:\\s)?" + CC_FORMAT;

    private static final int LEN_MIN = 13;
    private static final int LEN_MAX = 17;

    private static final CodeValidator VALIDATOR =
            new CodeValidator(FORMAT, LEN_MIN, LEN_MAX, S10UPUCheckDigit.getInstance());

    /** The singleton instance which uses the default formats */
    private static final S10UPUValidator DEFAULT_VALIDATOR = new S10UPUValidator();

    /**
     * Return a singleton instance of the validator using the default formats
     *
     * @return A singleton instance of the validator
     */
    public static S10UPUValidator getInstance() {
        return DEFAULT_VALIDATOR;
    }

    /**
     * Validate a S10 identifier
     *
     * @param code The value validation is being performed on
     * @return <code>true</code> if the value is valid
     */
    public boolean isValid(String code) {
        final RegexValidator regexValidator = VALIDATOR.getRegexValidator();
        // eliminate non-capturing groups
        String cde = regexValidator.validate(code);
        if (cde!=null && code.length()>=LEN_MIN && code.length()<=LEN_MAX) {
            String cc = cde.substring(LEN_MIN - 2);
    	    LOG.debug(cde + ", Country Code="+cc);
            return CC.isS10Country(cc) && VALIDATOR.getCheckDigit().isValid(cde.substring(2, LEN_MIN - 2));
        }
        return false;
    }

    static class S10Country implements Iso3166_1 {
        private final String COUNTRY_CODES_REMOVE = "";
        private final String COUNTRY_CODES_ADD = "XK,";
        public boolean isS10Country(final String code) {
            if (code == null || code.length() != 2) return false;
            return COUNTRY_CODES_REMOVE.indexOf(code+",") > -1 ? false 
                    : COUNTRY_CODES_ADD.indexOf(code+",") > -1 ? true : isAlpha2(code);
        }
    }
    static S10Country CC = new S10Country();

}