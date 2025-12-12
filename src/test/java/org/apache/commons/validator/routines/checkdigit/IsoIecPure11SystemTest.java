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
 * Test ISO/IEC 7064, MOD 11-2 pure recursive/iterative and polynomial routines.
 *
 * @author EUG https://github.com/homebeaver
 * @since 1.10.0
 */
public class IsoIecPure11SystemTest extends AbstractIsoIec7064Test {

    /**
     * Sets up routine & valid codes.
     */
    @BeforeEach
    protected void setUp() {
        routine = IsoIecPure11System.getInstance();
        routin2 = IsoIecPolynomial11System.getInstance();
        valid = new String[] {"1", "01", "001"
            , "079X"
            , "07940"
            , "112518008" // ISNI-Id
            , "000000150000007"
            , "0000000150000007" // ORCID-Id
            , "0000000167986212" // dto
            };
        invalid = new String[] {"08940", "089X", "X3", "X1"};
    }

}
