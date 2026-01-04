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
package org.apache.commons.validator.routines;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * DriverLicenseDEValidator Test.
 */
public class DriverLicenseDEValidatorTest {

    private final String[] validLeitwegFormat = new String[] {
        "E021AB37X11", // File:DE Licence 2021 Front.jpg mit prefix E und sufix 1
        "B072RRE2I55", // aus https://de.wikipedia.org/wiki/F%C3%BChrerscheinnummer
    };

    private final String[] invalidLeitwegFormat = new String[] {
        "Z021AB37X11", // invalid "Z" prefix
        "E021AB37X1*", // invalid suffix "*"
        "B072RRE2I55\n", // plus new line
        "",
        null
    };

    private static final DriverLicenseDEValidator VALIDATOR = DriverLicenseDEValidator.getInstance();

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
