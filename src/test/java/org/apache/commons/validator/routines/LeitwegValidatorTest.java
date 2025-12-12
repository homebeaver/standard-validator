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
package org.apache.commons.validator.routines;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * LeitwegValidator Test.
 */
public class LeitwegValidatorTest {

    private final String[] validLeitwegFormat = new String[] {
        "05916-31001-97",         // Stadt Herne (PZ 97 und nicht 00)
        "05111-12012-98",         // Rechenzentrum der Finanzverwaltung NRW (PZ 98 und nicht 01)
        "992-90009-96",           // Deutsche Bahn AG
        "051700052052-31001-35",  // Xanten, Stadt
        "053340002002-33004-23",  // Aachener Parkhaus GmbH
        "05711-06001-79",         // UNI BI Lkr:Bielefeld, Stadt
        "05913-99001-25",         // Kassenärztliche Vereinigung Westfalen-Lippe, 44141 Dortmund, x-rechnung@kvwl.de
        "11-2000001000-30",       // ITDZ, Berlin
        "08125065-A9359-66",      // Stadt Neckarsulm
        "09564000-NUE0001-49",    // Flughafen Nürnberg GmbH siehe https://verzeichnis.leitweg-id.de/
        "04011000-900X999-08",    // Test-Leitweg-ID https://www.e-rechnung.bremen.de/testen-11767
    };

    private final String[] invalidLeitwegFormat = new String[] {
        "9992",                   // "99-92" without '-'
        "00-290009-64",           // invalid region "00"
    };

    private static final LeitwegValidator VALIDATOR = LeitwegValidator.getInstance();

    @Test
    public void testValid() {
        for (String f : validLeitwegFormat) {
            System.out.println("   Testing Valid Code=[" + f + "]");
            assertTrue(VALIDATOR.isValid(f), f);
        }
    }

    @Test
    public void testInValid() {
        for (String f : invalidLeitwegFormat) {
            assertFalse(VALIDATOR.isValid(f), f);
        }
    }

}
