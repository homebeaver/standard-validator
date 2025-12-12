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

/**
 * IsoIec1110HybridSystem Check Digit Tests.
 * Zero Check: cd nicht eindeutig bei führenden nullen
     02 - valid
    004 - valid
   0008
   00005
   000000
   0000009
   00000007
   -- die pz ist jeweils die Gewichtung von 11-2 bei polynomial XXX
 */
public class IsoIecHybrid1110SystemTest extends AbstractIsoIec7064Test {

    // valid DE VAT Id without DE prefix
    private static final String MIN = "000000011"; // theoretical minimum
    private static final String MAX = "999999995"; // theoretical
    private static final String GEIGER = "128514248"; // https://www.geigergruppe.com/de-de/impressum/
    private static final String ITDZ = "205130669"; // https://www.itdz-berlin.de/allgemeines/impressum/

    /**
     * Sets up routine & valid codes.
     */
    @BeforeEach
    protected void setUp() {
//        routine = Modulus11TenCheckDigit.getInstance();
        routine = IsoIecHybrid1110System.getInstance();
        valid = new String[] {"1", "02", "004"
            , MIN, GEIGER, ITDZ, "136586130", "136695976"
            , "294776378", "811128135", "294776378", MAX
            // HR VAT Id:
            , "00000000010"
            , "33392005961" // NASTAVNI ZAVOD ZA JAVNO ZDRAVSTVO DR. ANDRIJA ŠTA, Zagreb
            , "99999999994"
            // TIN DE:
            , "81872495633" // aus http://www.pruefziffernberechnung.de/I/Identifikationsnummer.shtml
            , "02476291358"
            , "86095742719"
            , "47036892816"
            , "65929970489"
            , "57549285017"
            , "25768131411"
            , "11012234564" // 2 doppelt und 1 dreifach
            , "11234567890" // doppelte Ziffer : 1 direkt hintereinander
            , "11012345675" // dreifach Ziffer : 1 nicht direkt hintereinander
            , "12720320213" // 0 und 1 doppelt und 2 vierfach
        };
        invalid = new String[] {"11" // =/= "000000011"
        };
    }

}
