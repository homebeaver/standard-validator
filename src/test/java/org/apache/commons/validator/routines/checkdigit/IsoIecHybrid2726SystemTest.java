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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IsoIecHybrid2726SystemTest extends AbstractIsoIec7064Test {

    /**
     * Sets up routine & valid codes.
     */
    @BeforeEach
    protected void setUp() {
        routine = IsoIecHybrid2726System.getInstance();
        valid = new String[] {"B"
        };
    }

    @Test
    public void testZeroSum() {
        assertFalse(routine.isValid(zeroSum), "isValid() Zero Sum");
        // throw exception "Invalid Character ... when calculated for "0000000000" because '0' is not alphabetic
        Exception e = assertThrows(Exception.class, () -> routine.calculate(zeroSum), "Zero Sum");
        System.out.println("EUG testZeroSum calculate(\"" + zeroSum + "\") throws " + e.getMessage());
        assertTrue(e.getMessage().startsWith("Invalid "), "message is " + e.getMessage());
    }

}
