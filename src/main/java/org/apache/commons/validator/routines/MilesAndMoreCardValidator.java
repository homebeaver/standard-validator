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

import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;

/**
 * Perform MilesAndMore service card number validations.
 *
 * <p>
 * MilesAndMore is a frequent flyer program from Austrian Airlines, Brussels Airlines, Lufthansa and SWISS.
 * </p>
 *
 * <p>
 * The service card number is a 15-digit number. For example, it may start with 
 * 9999 (without advertising consent), 
 * 9920 (without frequent flyer status), 
 * 9922 (Frequent Traveller), 
 * 222 (Senator) or 
 * 333 (HON Circle Member).
 * </p>
 *
 * <p>
 * For further information see
 *  <a href="https://en.wikipedia.org/wiki/Miles_%26_More">Wikipedia</a>.
 * </p>
 *
 * @since 2.10.5
 */
public final class MilesAndMoreCardValidator implements Serializable {

    private static final long serialVersionUID = 3757993674231804673L;

    /** Singleton instance */
    private static final MilesAndMoreCardValidator INSTANCE = new MilesAndMoreCardValidator();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the card validator.
     */
    public static MilesAndMoreCardValidator getInstance() {
        return INSTANCE;
    }

    /**
     * The service card number is a 15-digit number. It may start with Major industry identifier (MII = 9):
     * 9999 (without advertising consent), 
     * 9920 (without frequent flyer status), 
     * 9922 (Frequent Traveller), 
     * <p> and MII = 2 : Airlines,  MII = 3 : Travel :
     * <p>
     * 222 (Senator) or 
     * 333 (HON Circle Member).
     * <p>
     * For further information see
     *  <a href="https://en.wikipedia.org/wiki/ISO%2FIEC_7812">Wikipedia</a>.
     * </p>
     */
    private static final String FORMAT1 = "(9999|9920|9922)(\\d{11})";
    private static final String FORMAT2 = "(222|333)(\\d{12})";
    private static final String[] FORMAT = new String[] {FORMAT1, FORMAT2};

    static RegexValidator FORMAT_VALIDATOR = new RegexValidator(FORMAT);
    static final CodeValidator VALIDATOR = new CodeValidator(FORMAT_VALIDATOR, 15, LuhnCheckDigit.getInstance());

    /**
     * Constructs a validator.
     */
    private MilesAndMoreCardValidator() { }

    /**
     * Tests whether the code is a valid service card number.
     *
     * @param code The code to validate.
     * @return {@code true} if a service card number, otherwise {@code false}.
     */
    public boolean isValid(final String code) {
        return VALIDATOR.isValid(code);
    }

    /**
     * Checks the code is valid IMO number.
     *
     * @param code The code to validate.
     * @return A IMO number code with prefix removed if valid, otherwise {@code null}.
     */
    public Object validate(final String code) {
        final Object validate = VALIDATOR.validate(code);
        if (validate != null) {
            return VALIDATOR.isValid(code) ? validate : null;
        }
        return validate;
    }

}
