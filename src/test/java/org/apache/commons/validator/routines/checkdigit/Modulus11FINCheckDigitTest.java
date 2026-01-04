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
 * Modulus 11-X module for FIN Check Digit Test.
 */
class Modulus11FINCheckDigitTest extends AbstractCheckDigitTest {

    /*
     * In kba.de anlage_2_Berechnung_Pruefziffer_FIN_Modulo_11_Verfahren.pdf findet man zwei
     * Beispiele, BSP1 für invalid und BSP2 für valid, dass hier aber nicht als valide
     * erkannt wird weil es Kleinbuchstaben und Umlaute enthält
     */
    private static final String BSP1_ANLAGE2 = "A1BS31Z0430336179";
    private static final String BSP2_ANLAGE2 = "0Ly34Ü598110IX"; // mit "y" und "Ü"

    /*
     * Die Beispiele aus https://de.wikipedia.org/wiki/F%C3%BChrerscheinnummer liefern invalid
     * weil das Erste nur ein Layout mit falscher Nr ist. 
     * Ersetzt man das führende "Z" durch "E" so ist die Nummer valide.
     * Die zweite Nummer enthält eine Ausfertigungsziffer am Ende, 
     * die nicht in die Prüfzifferberechnung einfließt.
     */
    private static final String DL_DE_LAYOUT_2021 = "Z021AB37X1"; // File:DE Licence 2021 Front.jpg
    private static final String DRIVER_LICENSE_DE = "B072RRE2I55"; // mit Ausfertigungsziffer

    /**
     * Sets up routine & valid codes.
     */
    @BeforeEach
    protected void setUp() {
        routine = Modulus11FINCheckDigit.getInstance();

        valid = new String[] { "0LY341U59810IX" , "B072RRE2I5", "E021AB37X1" };
        invalid = new String[] { BSP1_ANLAGE2, BSP2_ANLAGE2, DL_DE_LAYOUT_2021, DRIVER_LICENSE_DE };
    }
}
