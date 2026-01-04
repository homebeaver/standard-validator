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

import java.io.Serializable;

import org.apache.commons.validator.routines.checkdigit.Modulus11FINCheckDigit;

/**
 * German <b>Driver License Number</b> validation.
 *
 * <p>
 * The alphanumeric code is a unique identification used on Driver License Cards.
 * </p>
 *
 * <p>
 * Check digit calculation is based on <i>modulus 11</i> and can have a value of "X".
 * </p>
 *
 * <p>
 * For further information see
 *  <a href="https://de.wikipedia.org/wiki/F%C3%BChrerscheinnummer">Wikipedia (de)</a>.
 * </p>
 *
 * @since 2.10.5
 */
public final class DriverLicenseDEValidator implements Serializable {

    private static final long serialVersionUID = 7027147848347009616L;

	/** Singleton instance */
    private static final DriverLicenseDEValidator INSTANCE = new DriverLicenseDEValidator();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the Card Number validator.
     */
    public static DriverLicenseDEValidator getInstance() {
        return INSTANCE;
    }

    /**
     * The card number consists of 10 + 1 characters
     * - a letter A-P for the state of issue
     * - tree numbers for the issuer dept
     * - 5 alphanumeric characters
     * - a check character numeric or "X"
     * - a sequence number or alpha A for 10 ... (not used in check digit calculation)
     * Example with spaces for the parts: B 072 RRE2I 5 5
     */
    /* the prefix designate the individual German state, here with ISO 3166-2 country subdivision code
A 08 DE-BW Baden-Württemberg 
B 09 DE-BY Bayern 
C 11 DE-BE Berlin 
D 12 DE-BB Brandenburg 
E 04 DE-HB Bremen 
F 02 DE-HH Hamburg 
G 06 DE-HE Hessen 
H 13 DE-MV Mecklenburg-Vorpommern 
I 03 DE-NI Niedersachsen 
J 05 DE-NW Nordrhein-Westfalen 
K 07 DE-RP Rheinland-Pfalz 
L 10 DE-SL Saarland 
M 14 DE-SN Sachsen 
N 15 DE-ST Sachsen-Anhalt 
O 01 DE-SH Schleswig-Holstein 
P 16 DE-TH Thüringen 
     */
    /*
     * NOTE: the last group is non-capturing (?: ... )
     */
    private static final String FORMAT = "([A-P])(\\d{3})([A-Z0-9]{5})([0-9X])(?:[A-Z0-9])";
    private static final int LEN = 10; // without the non-capturing group

    static RegexValidator FORMAT_VALIDATOR = new RegexValidator(FORMAT);
    static final CodeValidator VALIDATOR = new CodeValidator(FORMAT_VALIDATOR, LEN, Modulus11FINCheckDigit.getInstance());

    /**
     * Constructs a validator.
     */
    private DriverLicenseDEValidator() { }

    /**
     * Tests whether the code is a valid driver card number.
     *
     * @param code The code to validate.
     * @return {@code true} if a valid number, otherwise {@code false}.
     */
    public boolean isValid(final String code) {
        /* <code>code.length()==LEN</code> sorgt dafür, dass FORMAT pur angewendet wird,
         * whitespaces (TAB, NewLine) als Präfix oder Suffix der nummer liefern invalid.
         * Der &&-Operand darf nicht vorne stehen, bei code==null führt es zu NPE!
         */
        return VALIDATOR.isValid(code) && code.length()==LEN+1;
    }

    /**
     * Checks the code is valid IMO number.
     *
     * @param code The code to validate.
     * @return A IMO number code with prefix removed if valid, otherwise {@code null}.
     */
    public Object validate(final String code) {
        final Object validate = VALIDATOR.validate(code); // validate trims the input
        if (validate != null) {
            return VALIDATOR.getCheckDigit().isValid(code.substring(0, code.length()-1));
        }
        return validate;
    }

}
