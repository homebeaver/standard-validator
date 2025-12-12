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
 * Implements ISO/IEC 7064, MOD 97-10 polynomial check digit calculation/validation.
 * <p>
 * MOD 97-10 applies to anumeric strings, the check digit is numeric.
 * See <a href="https://de.wikipedia.org/wiki/ISO/IEC_7064">Wikipedia - ISO/IEC_7064 (de)</a>
 * for more details.
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 1.10.0
 */
public class IsoIecPolynomial97System extends IsoIec7064PurePolynomialSystem implements IsoIecConstants {

    private static final long serialVersionUID = -5317384830801575413L;

    /** Singleton Check Digit instance */
    private static final IsoIecPolynomial97System INSTANCE = new IsoIecPolynomial97System();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }
    IsoIecPolynomial97System() {
        super(MODULUS_97, 2);
    }

    @Override
    protected int getRadix() {
        return RADIX_10;
    }

    /** First fifteen weightings given to digits depending on their right position */
    private static final int[] POSITION_WEIGHT = { 1, 10, 3, 30, 9, 90, 27, 76, 81, 34, 49, 5, 50, 15, 53 };
    @Override
    protected int[] getWeightings() {
        return POSITION_WEIGHT;
    }

    @Override
    protected String getCharacterSet() {
        return NUMERIC;
    }

}
