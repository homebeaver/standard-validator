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
 * Bahncard number validation.
 *
 * <p>
 * BahnCard is a discount subscription program offered by Deutsche Bahn, the German national railway company.
 * </p>
 *
 * <p>
 * The card number is a 16-digit number. For example, it may start with 7081.
 * </p>
 *
 * <p>
 * For further information see
 *  <a href="https://en.wikipedia.org/wiki/BahnCard">Wikipedia</a>.
 * </p>
 *
 * @since 2.10.5
 */
public final class BahnCardValidator implements Serializable {

    private static final long serialVersionUID = 2598787327598177288L;

    /** Singleton instance */
    private static final BahnCardValidator INSTANCE = new BahnCardValidator();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the card validator.
     */
    public static BahnCardValidator getInstance() {
        return INSTANCE;
    }

    private static final String FORMAT = "(7081)(\\d{12})";
    private static final int LEN = 16;

    static RegexValidator FORMAT_VALIDATOR = new RegexValidator(FORMAT);
    static final CodeValidator VALIDATOR = new CodeValidator(FORMAT_VALIDATOR, LEN, LuhnCheckDigit.getInstance());

    /**
     * Constructs a validator.
     */
    private BahnCardValidator() { }

    /**
     * Tests whether the code is a valid card number.
     *
     * @param code The card number to validate.
     * @return {@code true} if a card number, otherwise {@code false}.
     */
    /* <code>code.length()==LEN</code> sorgt dafür, dass FORMAT pur angewendet wird
     * whitespaces (TAB, NewLine) als Präfix oder Suffix der nummer liefern invalid.
     * Der &&-Operand darf nicht vorne stehen, bei code==null führt es zu NPE!
     */
    public boolean isValid(final String code) {
    	/**
    	 * <code>code.length()==LEN</code> sorgt
    	 */
        return VALIDATOR.isValid(code) && code.length()==LEN;
    }

}
