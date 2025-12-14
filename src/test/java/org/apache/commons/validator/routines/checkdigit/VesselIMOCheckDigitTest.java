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
 * IMO Number Check Digit Tests.
 */
public class VesselIMOCheckDigitTest extends AbstractCheckDigitTest {

    // valid CAS Number with dashes removed
    private static final String MIN = "0000012"; // theoretical
    private static final String KAVITA = "9074729";
    private static final String EVANGELIA = "9176187";
    private static final String SEDOV = "7946356"; // https://en.wikipedia.org/wiki/STS_Sedov
    private static final String MAX = "9999993"; // theoretical

    /**
     * Sets up routine & valid codes.
     */
    @BeforeEach
    protected void setUp() {
        routine = VesselIMOCheckDigit.getInstance();
        valid = new String[] {MIN, EVANGELIA, KAVITA, SEDOV, MAX};
        invalid = new String[] { "0000011", // wrong check
                "7732-18-5", "IMO0000012", // formated chars
                " 9999993", "9999993 ", " 9999993 ", };
    }

}
