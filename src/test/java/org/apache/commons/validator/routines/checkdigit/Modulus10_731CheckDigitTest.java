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
 * Modulus10_731CheckDigit Check Digit Test.
 */
class Modulus10_731CheckDigitTest extends AbstractCheckDigitTest {

    // https://de.wikipedia.org/wiki/Ausweisnummer:
    private static final String BEISPIEL1 = "T220001293"; // File:Deutscher_Personalausweis_(2010_Version).jpg
    private static final String BEISPIEL2 = "1220001297"; 
    private static final String MUSTERMANN_1987 = "1220000430"; // File:Mustermann_1987.jpg
    private static final String MUSTERMANN_1997 = "1220000371"; // File:Personalausweis.gif
    private static final String MUSTERMANN_2001 = "1220011933"; // File:Mustermann_Reisepass_2001.jpg
    private static final String MUSTERMANN_2021 = "L01X00T471"; // File:Personalausweis_(2021).png
    private static final String MUSTERMANN_2024 = "LZ6311T475"; // File:Personalausweis_(2024).png
    private static final String MUSTERMANN_NPA = "1220011933"; // File:Mustermann_nPA_Sicherheitsmerkmale.jpg
    // https://www2023.icao.int/publications/Documents/9303_p3_cons_en.pdf :
    private static final String UTOPIA = "L898902C36"; // Figure 1. Example of a VIZ and MRZ from an MRTD
    private static final String EXAMPLE_1 = "5207273"; // APPENDIX A TO PART 3 — EXAMPLES
    private static final String EXAMPLE_2 = "AB21345";
    private static final String EXAMPLE_3A = "HA67224206";
    private static final String EXAMPLE_3B = "HA6722426";
    private static final String EXAMPLE_4A = "D231458907";
    private static final String EXAMPLE_5 = "HA67224206580225496010868";

    /**
     * Sets up routine & valid codes.
     */
    @BeforeEach
    protected void setUp() {
        routine = Modulus10_731CheckDigit.getInstance();
        valid = new String[] { BEISPIEL1 , BEISPIEL2, MUSTERMANN_1987, MUSTERMANN_1997, MUSTERMANN_2001
            , MUSTERMANN_2021, MUSTERMANN_2024, MUSTERMANN_NPA
            , UTOPIA, EXAMPLE_1, EXAMPLE_2, EXAMPLE_3A, EXAMPLE_3B, EXAMPLE_4A, EXAMPLE_5
            , "AP12345673" // AT-Pass
            , "GA00078050" // BE-Pass
            , "0070070071" // HR-Pass
            , "990090544"  // CZ-Pass
            , "KS12345672" // EE-Pass
            , "FX12345672" // FI-Pass
            , "CZ6311T472" // DE-Pass
            , "BD00020282" // HU-Pass
            , "JC3L7T2H4"  // LU-Pass
            , "SPECI20142" // NL-Pass
            , "ZC39917886" // PL-Pass
            , "0400210397" // RO-Pass
            , "XA00000012" // SE-Pass
            };
        invalid = new String[] { "0000", "0", "", null };
    }

}
