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
 * Implements ISO/IEC 7064, MOD 97-10 check digit calculation/validation.
 * <p>
 * MOD 97-10 applies to numeric strings, the check digit is numeric.
 * See <a href="https://de.wikipedia.org/wiki/ISO/IEC_7064">Wikipedia - ISO/IEC_7064 (de)</a>
 * for more details.
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 1.10.0
 */
public class IsoIecPure97System extends IsoIec7064PureSystem implements IsoIecConstants {

    private static final long serialVersionUID = 7929594261265681161L;

    /** Singleton Check Digit instance */
    private static final IsoIecPure97System INSTANCE = new IsoIecPure97System();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }
    IsoIecPure97System() {
        super(MODULUS_97, 2);
    }

    @Override
    protected int getRadix() {
        return RADIX_10;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overrides with simplified procedure described in ISO/IEC 7064:2003(E).
     * </p>
     */
    @Override
    public String calculate(final String code) throws CheckDigitException {
        if (code == null) {
            throw new CheckDigitException(CheckDigitException.MISSING_CODE);
        }
        try {
            long l = Long.parseLong(code + "00"); // throws NumberFormatException
            int r = (int) (l % getModulus());
            return toCheckDigit((getModulus() + 1 - r) % getModulus());
        } catch (final NumberFormatException ex) {
//            System.out.println("NumberFormatException: "+ex);
            // Expected exception for high codes f.i. 999999999999999999
            // fall back to recursive/iterative method in super
            return super.calculate(code);
        }
    }

    @Override
    protected String getCharacterSet() {
        return NUMERIC;
    }

}
