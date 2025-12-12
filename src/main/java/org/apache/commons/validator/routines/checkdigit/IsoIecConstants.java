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

/**
 * A collection of constants generally used for ISO/IEC 7064 check digit calculation/validation.
 *
 * @author EUG https://github.com/homebeaver
 * @since 1.10.0
 */
// Fields in interfaces and annotations are automatically public static final
public interface IsoIecConstants {  // CHECKSTYLE IGNORE InterfaceIsType

    /**
     * Numeric character set used for hybrid ISO/IEC 7064, MOD 11,10
     * and for pure ISO/IEC 7064, MOD 97-10 check digit.
     */
    String NUMERIC = "0123456789";

    /**
     * Alphabetic character set used for hybrid ISO/IEC 7064, MOD 27,26
     * and for pure ISO/IEC 7064, MOD 661-26 check digit.
     */
    String ALPHABETIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Alphanumeric character set used for hybrid ISO/IEC 7064, MOD 37,36
     * and for pure ISO/IEC 7064, MOD 1271-36 check digit.
     */
    String ALPHANUMERIC = NUMERIC + ALPHABETIC;

    /**
     * Character set used for pure ISO/IEC 7064, MOD 11-2 check digit is numeric or 'X'.
     */
    String NUMERIC_PLUS_X = NUMERIC + 'X';

    /**
     * Character set used for pure ISO/IEC 7064, MOD 37-2 check digit is alphanumeric or '*'.
     */
    String ALPHANUMERIC_PLUS_STAR = ALPHANUMERIC + '*';

    /**
     * The modulus used for pure ISO/IEC 7064, MOD 37-2.
     */
    int MODULUS_37 = 37;

    /**
     * The modulus used for pure ISO/IEC 7064, MOD 97-10.
     */
    int MODULUS_97 = 97;

    /**
     * The modulus used for pure ISO/IEC 7064, MOD 661-26.
     */
    int MODULUS_661 = 661;

    /**
     * The modulus used for pure ISO/IEC 7064, MOD 1271-36.
     */
    int MODULUS_1271 = 1271;

    /**
     * The radix used for pure ISO/IEC 7064, MOD 11-2 and ISO/IEC 7064, MOD 37-2.
     */
    int RADIX_2 = 2;

    /**
     * The radix used for pure ISO/IEC 7064, MOD 97-10.
     */
    int RADIX_10 = NUMERIC.length();

    /**
     * The radix used for pure ISO/IEC 7064, MOD 661-26.
     */
    int RADIX_26 = ALPHABETIC.length();

    /**
     * The radix used for pure ISO/IEC 7064, MOD 1271-36.
     */
    int RADIX_36 = ALPHANUMERIC.length();
}
