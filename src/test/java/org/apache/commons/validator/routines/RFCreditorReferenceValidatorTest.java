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
 * Creditor Reference Test.
 * There is an example in
 *  <A HREF="https://en.wikipedia.org/wiki/Creditor_Reference">wikipedia</A>.
 */
public class RFCreditorReferenceValidatorTest {

    private final String[] validRFCreditorReference = new String[] {
        "RF45ABC",
        "RF45abc",
        "RF18539007547034",
        "RF18000000000539007547034",
    };

    private final String[] invalidRFCreditorReference = new String[] {
        "RF45",                       // too short
        "RF28000000000539007547034X", // too long
        "RF18000000000539007547034\t", // with trailing TAB
        "RF18539007547034\t", // with trailing TAB
    };

    private static final RFCreditorReferenceValidator VALIDATOR = RFCreditorReferenceValidator.getInstance();

    @Test
    public void testValid() {
        for (String f : validRFCreditorReference) {
            System.out.println("   Testing Valid Code=[" + f + "]");
            assertTrue(VALIDATOR.isValid(f), f);
        }
    }

    @Test
    public void testInValid() {
        for (String f : invalidRFCreditorReference) {
            assertFalse(VALIDATOR.isValid(f), f);
        }
    }

}
