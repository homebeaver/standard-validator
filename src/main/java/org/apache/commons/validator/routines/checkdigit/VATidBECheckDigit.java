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
 * Belgian VAT identification number (VATIN) Check Digit calculation/validation.
 * <p>
 * Numéro T.V.A. BTW-nummer (Nº TVA BTW-nr.) old schema {@code 1234567pp}.
 * Note the check digit has two characters and that the old numbering schema only had 9 characters,
 * just adding a zero in front makes it a valid number in the new schema {@code 01234567pp}.
 * </p>
 * <p>
 * The check digits are calculated as 97 - MOD 97
 * </p>
 * <p>
 * See <a href="https://en.wikipedia.org/wiki/VAT_identification_number">Wikipedia - VAT IN</a>
 * for more details.
 * </p>
 *
 * @since 1.10.0
 */
// VATidBECheckDigit is Modulus97 and check digit length is 2 : can subclass Modulus31CheckDigit
public final class VATidBECheckDigit extends Modulus31CheckDigit {

    private static final long serialVersionUID = 4622288405648808179L;

    /** Singleton Check Digit instance */
    private static final VATidBECheckDigit INSTANCE = new VATidBECheckDigit();

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
    private VATidBECheckDigit() {
        super(MODULUS_97);
    }

    @Override
    protected int getCheckdigitLength() {
        return RADIX_2;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override to handle charValues between 0 and 96.
     * </p>
     */
    @Override
    protected String toCheckDigit(final int charValue) throws CheckDigitException {
        int cdv = charValue == 0 ? getModulus() : charValue;
        return "" + (cdv / RADIX_10) + (cdv % RADIX_10);
    }

}