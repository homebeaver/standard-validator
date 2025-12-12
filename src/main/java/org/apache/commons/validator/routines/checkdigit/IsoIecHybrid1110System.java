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
 * Implements ISO/IEC 7064, MOD 11,10 check digit calculation/validation.
 * <p>
 * MOD 11,10 applies to numeric strings.
 * See <a href="https://de.wikipedia.org/wiki/ISO/IEC_7064">Wikipedia - ISO/IEC_7064 (de)</a>
 * for more details.
 * </p>
 *
 * <p>
 * This MOD 11,10 module can also be used to validate TIN_DE, TIN_HR, the VATIN_DE and VATIN_HR
 * used in VATINCheckDigit
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 1.10.0
 */
public class IsoIecHybrid1110System extends IsoIec7064HybridSystem implements IsoIecConstants {

    private static final long serialVersionUID = -3105352613748945891L;

    /** Singleton Check Digit instance */
    private static final IsoIecHybrid1110System INSTANCE = new IsoIecHybrid1110System();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }

    /**
     * Constructs a modulus Check Digit routine.
     * <p>
     * There are two modulus values, M+1 and M.
     * M is the number of characters in the character set of the string to be protected, aka NUMERIC.
     * M+1 is the parameter of the ctor.
     * </p>
     */
    IsoIecHybrid1110System() {
        super(NUMERIC.length() + 1);
    }

    @Override
    protected String getCharacterSet() {
        return NUMERIC;
    }

}
