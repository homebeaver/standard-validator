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
 * International postal items S10 UPU Check Digit calculation/validation Tests.
 */
public class S10UPUCheckDigitTest extends AbstractCheckDigitTest {

    /**
     * Sets up routine & valid codes.
     */
    @BeforeEach
    protected void setUp() {
        routine = S10UPUCheckDigit.getInstance();
        valid = new String[] { "473124829" // example in S10-12.pdf
            , "123456785" // EE123456785KR
            , "876543216" // RR876543216ER
            , "456789015" // VA456789015KG
            , "654321092" // CP654321092GM
            , "010000155" // CV010000155UA
            , "876543216" // EE876543216CA
            , "012808877" // RR012808877PH
            , "072705659" // RR072705659PL
            , "011231107" // RT011231107HK
            , "395449360" // Deutsche Post Einschreibebrief RT395449360DE
            };
    }

}
