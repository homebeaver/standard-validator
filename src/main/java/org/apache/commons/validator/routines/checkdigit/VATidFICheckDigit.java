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

import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;

/**
 * Finnish VAT identification number (VATIN) Check Digit calculation/validation.
 * <p>
 * Arvonlisâverorekisterointi-numero (ALV-NRO)
 * </p>
 * <p>
 * See <a href="https://tarkistusmerkit.teppovuori.fi/tarkmerk.htm#alv-numero">calculating checkdigits (fi)</a>
 * for more details.
 * </p>
 *
 * @since 1.10.0
 */
public final class VATidFICheckDigit extends IsoIecPure11System {

    private static final long serialVersionUID = -2733176347099754665L;

    /** Singleton Check Digit instance */
    private static final VATidFICheckDigit INSTANCE = new VATidFICheckDigit();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }

    @Override
    protected String getCharacterSet() {
        return NUMERIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String calculate(final String code) throws CheckDigitException {
        if (GenericValidator.isBlankOrNull(code)) {
            throw new CheckDigitException(CheckDigitException.MISSING_CODE);
        }
        Long l = GenericTypeValidator.formatLong(code);
        if (l == null) {
            throw new CheckDigitException(CheckDigitException.invalidCode(code));
        }
        if (l == 0) {
            throw new CheckDigitException(CheckDigitException.ZERO_SUM);
        }
        int m = getModulus();
        int cm = calculateModulus(code, false);
        // now compute what checksum will be congruent to 1 mod M
        int checksum = (m - cm) % m; // in super: (m - cm + 1) % m
//        System.out.println(code + " checksum="+checksum + " cm="+cm);
        if (checksum == 10) {  // CHECKSTYLE IGNORE MagicNumber
            // codes resulting to check digit X are not valid
            throw new CheckDigitException(CheckDigitException.invalidCode(code));
        }
        return toCheckDigit(checksum);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(final String code) {
        if (GenericValidator.isBlankOrNull(code)) {
            return false;
        }
        Long l = GenericTypeValidator.formatLong(code);
        if (l == null || l == 0) {
            return false;
        }
        try {
            final int mr = INSTANCE.calculateModulus(code, true);
            return (mr == 0 ? 0 : getModulus() - mr) == Character.getNumericValue(code.charAt(code.length() - 1));
        } catch (final CheckDigitException ex) {
            return false;
        }
    }

}
