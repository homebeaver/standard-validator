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

/**
 * Implements MOD 511 check digit simple procedure.
 * <p>
 * MOD 511 applies to numeric strings, the check digit is numeric and has the length of 3 digits.
 * It is used for French Numéro d'immatriculation fiscale (NIF).
 * See <a href="https://fr.wikipedia.org/wiki/Num%C3%A9ro_d%27immatriculation_fiscale#France">Wikipedia - NIF (fr)</a>
 * for more details.
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 2.10.6
 */
//Modulus511CheckDigit is Modulus511 and check digit length is 3 : can subclass Modulus31CheckDigit
public final class Modulus511CheckDigit extends Modulus31CheckDigit {

    private static final long serialVersionUID = 8609862408916124805L;

    /** Singleton Check Digit instance */
    private static final Modulus511CheckDigit INSTANCE = new Modulus511CheckDigit();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }

    /**
     * Constructs a Check Digit routine.
     */
    Modulus511CheckDigit() {
        super(MODULUS_511);
    }

    @Override
    protected int getCheckdigitLength() {
        return 3;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override to handle charValues with three digits.
     * </p>
     */
    @Override
    protected String toCheckDigit(final int charValue) throws CheckDigitException {
        int cdv = charValue == 0 ? 0 : getModulus() - charValue;
        return String.format("%03d", cdv);
    }

}
