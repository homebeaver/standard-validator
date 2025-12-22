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

import org.apache.commons.validator.GenericValidator;

/**
 * <strong>Creditor Reference</strong> Check Digit calculation/validation.
 * <p>
 * RFCreditorReferenceCheckDigit is very equal to IBAN Check Digit with Prefix RF,
 * which is not an alpha-2 country code.
 * The only difference is that it accepts lower case letter, but handles them as upper case.
 * Implemented as <A HREF="https://en.wikipedia.org/wiki/Delegation_pattern">Delegation</A>.
 * <p>
 * For further information see ISO 11649
 *
 * @since 2.10.4
 */
public final class RFCreditorReferenceCheckDigit extends AbstractCheckDigit {

    public static final String RF = "RF";
    private static final int MAX_CODE_LEN = 25; // with "RF" and check digits

    /** Singleton Check Digit instance */
    private static final RFCreditorReferenceCheckDigit INSTANCE = new RFCreditorReferenceCheckDigit();

    /**
     * Gets the singleton instance of this class.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }

    private CheckDigit delegate = null;

    private RFCreditorReferenceCheckDigit() {
        delegate = IBANCheckDigit.getInstance();
    }

    @Override
    public String calculate(String code) throws CheckDigitException {
        if (GenericValidator.isBlankOrNull(code)) {
            throw new CheckDigitException(CheckDigitException.MISSING_CODE);
        }
        if (code.length() > MAX_CODE_LEN) {
            throw new CheckDigitException(CheckDigitException.invalidCode(code, "Code length=" + code.length()));
        }
        return delegate.calculate(code.toUpperCase());
    }

    @Override
    public boolean isValid(String code) {
        if (code == null || code.length() > MAX_CODE_LEN) {
            return false;
        }
        return delegate.isValid(code.toUpperCase());
    }

}
