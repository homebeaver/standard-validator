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

import org.junit.jupiter.api.BeforeEach;

/**
 * Luhn Check Digit Test.
 */
class LuhnCheckDigitTest extends AbstractCheckDigitTest {

    private static final String VALID_VISA = "4417123456789113";
    private static final String VALID_SHORT_VISA = "4222222222222";
    private static final String VALID_AMEX = "378282246310005";
    private static final String VALID_MASTERCARD = "5105105105105100";
    private static final String VALID_DISCOVER = "6011000990139424";
    private static final String VALID_DINERS = "30569309025904";
    private static final String VALID_IT_IVA_BANCA_ITALIA = "950501007"; // without leading "00"
    private static final String VALID_SE_VATIN_OLLE_SVENSSONS = "5561888404"; // without Trailing "01"
    private static final String VALID_LUHN_FOR_AT_TIN = "353535354"; // ABER 35 ist kein AT Finanzamt!

    /**
     * Sets up routine & valid codes.
     */
    @BeforeEach
    protected void setUp() {

        routine = LuhnCheckDigit.getInstance();

        valid = new String[] { VALID_VISA, VALID_SHORT_VISA, VALID_AMEX, VALID_MASTERCARD, VALID_DISCOVER, VALID_DINERS
            , VALID_IT_IVA_BANCA_ITALIA
            // Miles & More:
            , "999956789012347", "992056789012343", "992256789012349", "222000000000002", "333000000000008"
            // VATIN_SE:
            , "0000000018", "9999999999", "5565102570" // theoretical minimum, maximum, checkdigit zero
            , "1366959755", "1234567897", "5560528514", "5566801444", "5565102471", VALID_SE_VATIN_OLLE_SVENSSONS
            // TIN_AT:
            , "981234560", "901234567", "463765321", "038261574", "542679451", VALID_LUHN_FOR_AT_TIN
            , "12345678903", "10215", "12345670017"};
    }
}
