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
 * Implements ISO/IEC 7064, MOD 37-2 check digit calculation/validation.
 * <p>
 * MOD 37-2 applies to alphanumeric strings, the check digit is alphanumeric or '*'.
 * See <a href="https://de.wikipedia.org/wiki/ISO/IEC_7064">Wikipedia - ISO/IEC_7064 (de)</a>
 * for more details.
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 1.10.0
 */
public class IsoIecPure37System extends IsoIec7064PureSystem implements IsoIecConstants {

    private static final long serialVersionUID = -4873682097291042782L;

    /** Singleton Check Digit instance */
    private static final IsoIecPure37System INSTANCE = new IsoIecPure37System();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }
    IsoIecPure37System() {
        super(MODULUS_37, 1);
    }

    @Override
    protected int getRadix() {
        return RADIX_2;
    }

    @Override
    protected String getCharacterSet() {
        return ALPHANUMERIC_PLUS_STAR;
    }

    @Override
    protected int toInt(final char character, final int leftPos, final int rightPos) throws CheckDigitException {
        if (character == '*' ) {
            throw new CheckDigitException(CheckDigitException.invalidCharacter(character, leftPos));
        }
        return super.toInt(character, leftPos, rightPos);
    }

}
