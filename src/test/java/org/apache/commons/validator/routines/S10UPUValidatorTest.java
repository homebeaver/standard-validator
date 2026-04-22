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

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class S10UPUValidatorTest {
    /**
     * Examples see https://raw.githubusercontent.com/wiki/homebeaver/ungueltig/pdf/S10-12.pdf
     */
    private final List<String> validFormat = Arrays.asList(new String[] {
        "EE123456785KR", "EE 123 456 785 KR",
        "RR876543216ER", "RR 876543216 ER",
        "VA456789015KG",
        "CP654321092GM",
        "CV010000155UA",
        "EE876543216CA",
        "RR012808877PH",
        "RR072705659PL",
        "RT011231107HK",
        "RT395449360DE",
        "RT395449360AX",      // valid country Åland
        "RT395449360XK",      // valid country Kosovo
    });

    private final List<String> invalidFormat = Arrays.asList(new String[] {
        "RT395449360D",       // invalid length, to short
        "RT395449360DD",      // invalid country DDR
        "EE000000000KR",      // invalid check digit
        "QN123456785KR",      // valid only QA–QM
        "GB123456785KR",      // valid only GA, GD
    });

    private static final S10UPUValidator VALIDATOR = S10UPUValidator.getInstance();

    @Test
    public void testValid() {
        validFormat.forEach(e -> {
            assertTrue(VALIDATOR.isValid(e), e);
        });
    }

    @Test
    public void testInValid() {
        invalidFormat.forEach(e -> {
            assertFalse(VALIDATOR.isValid(e), e);
        });
    }

}
