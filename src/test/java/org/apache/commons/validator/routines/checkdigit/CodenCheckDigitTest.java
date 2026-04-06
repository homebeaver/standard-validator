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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * CodenCheckDigit Tests.
 */
public class CodenCheckDigitTest extends AbstractCheckDigitTest {

    // see https://cassi.cas.org/publication.jsp?P=DXaT8ZV7yNcyz133K_ll3zLPXfcr-WXfDOBEZZNqw0Ayz133K_ll3zLPXfcr-WXfQhoy4wwfUfwyz133K_ll3zLPXfcr-WXflWO_eNaIwo7E5UmtIMVTpg
    private static final String NATUAS = "NATUAS"; // the journal Nature
    private static final String TEREAU  = "TEREAU"; // Technology Review

    /**
     * Sets up routine & valid codes.
     */
    @BeforeEach
    protected void setUp() {
        routine = CodenCheckDigit.getInstance();
        valid = new String[] { "CYSTE3"
            , "48THAM"
            , "JPERFA", "53AKAE" // Bsp. von Teppo
            , "60WQAW" // mit '0'
            , "60W0AL" // mit '0' anstelle Buchstaben
            , "ACMCEI", "BIEDDX", "BJPCBM", "CHEIDI", "DJSCES", "GCKGEI"
            , "JPPCEJ", "JMSLD5", "LANGD5", "MBADEI", "MSICF5", "PLSCE4"
            ,  NATUAS, TEREAU
            };
        invalid = new String[] { "6OWQAW" , "ACMCEL", "BIEDDK" };
    }

    @Test
    public void testZeroSum() {
    }

}
