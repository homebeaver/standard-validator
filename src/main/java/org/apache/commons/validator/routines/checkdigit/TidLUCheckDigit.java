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
package org.apache.commons.validator.routines.checkdigit;

import org.apache.commons.validator.routines.DateValidator;

/**
 * Check digit calculation based on <em>modulus 11</em> for Luxembourg TIN numbers.
 * <p>
 * The TID number has 13 digits (9999999999999), the 2 last digits are check digits.
 * The 12th digit is a check digit calculated on the basis of the algorithm “de Luhn 10”,
 * calculated on the 11 first digits.
 * The 13th digit is a check digit calculated on the basis of the algorithm “de Verhoeff”,
 * calculated on the 11 first digits.
 * </p>
 * 
 * @author EUG https://github.com/homebeaver
 * @since 2.10.6
 */
public class TidLUCheckDigit extends Modulus11iWeightCheckDigit {

    private static final long serialVersionUID = 7056068269876852557L;

    /** Singleton Check Digit instance */
    private static final TidLUCheckDigit INSTANCE = new TidLUCheckDigit();
    // used for TIN checkdigits
    private static final CheckDigit LUHN = LuhnCheckDigit.getInstance();
    private static final CheckDigit VERHOEFF = VerhoeffCheckDigit.VERHOEFF_CHECK_DIGIT;
    private static final String YMD_PATTERN = "yyyyMMdd";

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }

    /**
     * Constructs a new instance.
     */
    TidLUCheckDigit() {
        super();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override because there are two checkdigits.
     * </p>
     */
    @Override
    protected int getCheckdigitLength() {
        return 2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String calculate(final String code) throws CheckDigitException {
        String luhnCd = LUHN.calculate(code);
        String verhoeffCd = VERHOEFF.calculate(code);
        // check valid date:
        if (code.length() < 8) {
            throw new CheckDigitException(CheckDigitException.invalidCode(code, "too short"));
        }
        final String date = code.substring(0, 8); // CHECKSTYLE IGNORE MagicNumber
        final DateValidator dateValidator = new DateValidator();
        if (dateValidator.validate(date, YMD_PATTERN) == null) {
            throw new CheckDigitException(CheckDigitException.invalidCode(code, "Date " + date + " is not valid pattern "+YMD_PATTERN));
        }
        final String year = code.substring(0, 4); // CHECKSTYLE IGNORE MagicNumber
        int y = Integer.parseInt(year);
        if (y < 1800 || y > 2030) {
            throw new CheckDigitException(CheckDigitException.invalidCode(code, "Date " + date + " is not valid year."));
        }
        return luhnCd + verhoeffCd;
    }

}
