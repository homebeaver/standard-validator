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
 * Implements ISO/IEC 7064, MOD 661-26 polynomial check digit calculation/validation.
 * <p>
 * MOD 661-26 applies to alphabetic strings, the check digit has two alphabetic chars.
 * See <a href="https://de.wikipedia.org/wiki/ISO/IEC_7064">Wikipedia - ISO/IEC_7064 (de)</a>
 * for more details.
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 1.10.0
 */
// derzeit keine mir bekannte Anwendung !!! TODO recherchieren
public class IsoIecPolynomial661System extends IsoIec7064PurePolynomialSystem implements IsoIecConstants {

    private static final long serialVersionUID = -2857595243713486002L;

    /** Singleton Check Digit instance */
    private static final IsoIecPolynomial661System INSTANCE = new IsoIecPolynomial661System();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }
    IsoIecPolynomial661System() {
        super(MODULUS_661, 2);
    }

    @Override
    protected int getRadix() {
        return RADIX_26;
    }

// TODO add precalculated POSITION_WEIGHTs (optional)

    @Override
    protected String getCharacterSet() {
        return ALPHABETIC;
    }

}
