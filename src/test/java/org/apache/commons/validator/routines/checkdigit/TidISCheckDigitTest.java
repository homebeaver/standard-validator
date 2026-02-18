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
 * IS TAX Id (TIN) Check Digit Tests.
 */
public class TidISCheckDigitTest extends AbstractCheckDigitTest {

    /**
     * Sets up routine & valid codes.
     */
    @BeforeEach
    protected void setUp() {
        routine = Modulus11i2to7CheckDigit.getInstance();
        valid = new String[] { "19" // 2*1 = 2 ; 11-2 = 9
            , "120160338" // 120160-3389
            , "121212129" // 121212-1239 ???
            , "051268148" // 051268-1449 ???
            , "120174339" // 120174-3389 ??? -339
            , "581113129" // 581113-1290 company aus https://github.com/HermannBjorgvin/Kennitala
            , "601010089" // 601010-0890 company
            , "310896209" // 310896-2099 person
            , "000000310" // 3*3 + 2*1 = 11 ; 11 mod 11 = 0
            , "000000400" // 3*4 + 2*0 = 12 ; 12 mod 11 = 1
            , "540269750" // 540269-7509
            , "09128517526" // TIN_NO
            };
        invalid = new String[] { "121212123"
            , "051268144"
            , "120174338"
            , "000002011" // 4*2 + 2*1 = 10 ; 10 mod 11 = 10 ==> PZ wird nicht vergeben
            };
    }

}