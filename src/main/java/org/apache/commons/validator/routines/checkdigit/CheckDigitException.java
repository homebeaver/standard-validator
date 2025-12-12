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
package org.apache.commons.validator.routines.checkdigit;

/**
 * Check Digit calculation/validation error.
 *
 * @since 1.4
 */
public class CheckDigitException extends Exception {

    private static final long serialVersionUID = -3519894732624685477L;

    /**
     * Common prefix for messages, f.i. "Invalid Code ...", "Invalid Character ..."
     */
    public static final String START_WITH_INVALID = "Invalid ";

    /**
     * Common message text when code is Null or empty
     */
    public static final String MISSING_CODE = "Code is missing";

    /**
     * Common message text when code sum is zero
     */
    public static final String ZERO_SUM = START_WITH_INVALID + "code, sum is zero";

    /**
     * Convenient message text "Invalid code [invalidCode]." with no additional information
     * @param code the invalid code
     * @return the message text
     */
    public static final String invalidCode(final String code) {
        return invalidCode(code, null);
    }
    /**
     * Convenient message text "Invalid code [invalidCode], additional information",
     *  f.i. Invalid code "", too short
     * @param code the invalid code
     * @param detail optional additional information
     * @return the message text
     */
    public static final String invalidCode(final String code, final String detail) {
        return START_WITH_INVALID + "code \"" + code + "\"" + (detail == null ? "." : ", " + detail);
    }

    /**
     * Convenient message text "Invalid Character [character]"
     * @param character the invalid character string
     * @return the message text
     */
    public static final String invalidCharacter(final String character) {
        return START_WITH_INVALID + "Character \"" + character + "\"";
    }
    /**
     * Convenient message text "Invalid Character [character]" without pos
     * @param character the invalid character
     * @return the message text
     */
    public static final String invalidCharacter(final char character) {
        return START_WITH_INVALID + "Character '" + character + "'";
    }
    /**
     * Convenient message text "Invalid Character [character] at pos [atPos]"
     * @param character the invalid character
     * @param atPos the position of the character in code
     * @return the message text
     */
    public static final String invalidCharacter(final char character, final int atPos) {
        return START_WITH_INVALID + "Character '" + character + "' at pos " + atPos;
    }

    /**
     * Constructs an Exception with no message.
     */
    public CheckDigitException() {
    }

    /**
     * Constructs an Exception with a message.
     *
     * @param msg The error message.
     */
    public CheckDigitException(final String msg) {
        super(msg);
    }

    /**
     * Constructs an Exception with a message and
     * the underlying cause.
     *
     * @param msg The error message.
     * @param cause The underlying cause of the error
     */
    public CheckDigitException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}
