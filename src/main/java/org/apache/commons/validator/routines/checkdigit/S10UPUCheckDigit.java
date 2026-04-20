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

import org.apache.commons.validator.GenericValidator;

/**
 * S10 UPU (Union postale universelle) Check Digit calculation/validation to international postal items.
 * <p>
 * See <a href="https://en.wikipedia.org/wiki/S10_(UPU_standard)">Wikipedia</a>
 * for more details.
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 2.10.8
 */
public final class S10UPUCheckDigit extends ModulusCheckDigit {

    private static final long serialVersionUID = 2710041343986794246L;

	/** Singleton Check Digit instance */
    private static final S10UPUCheckDigit INSTANCE = new S10UPUCheckDigit();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the class.
     */
    public static CheckDigit getInstance() {
        return INSTANCE;
    }

    /**
     * Constructs a new instance.
     */
    private S10UPUCheckDigit() {
        super(MODULUS_11);
    }

    /** Weighting given to digits depending on their left position */
    private static final int[] POSITION_WEIGHT = { 8,6,4,2,3,5,9,7 };
    /**
     * Calculates the <i>weighted</i> value of a character in the
     * code at a specified position.
     *
     * <p>CNP digits are weighted by their position from left to right.</p>
     *
     * @param charValue The numeric value of the character.
     * @param leftPos The position of the character in the code, counting from left to right
     * @param rightPos The positionof the character in the code, counting from right to left
     * @return The weighted value of the character.
     */
    @Override
    protected int weightedValue(int charValue, int leftPos, int rightPos) throws CheckDigitException {
        final int weight = POSITION_WEIGHT[(leftPos - 1) % POSITION_WEIGHT.length];
        return charValue * weight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(final String code) {
        if (GenericValidator.isBlankOrNull(code)) {
            return false;
        }
        try {
            final String cd = calculate(code.substring(0, code.length() - 1));
            return code.endsWith(cd);
        } catch (final CheckDigitException ex) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override to handle charValue 10 and 0.
     * </p>
     */
    @Override
    protected String toCheckDigit(final int charValue) throws CheckDigitException {
        return charValue == 10 ? "0" : charValue == 0 ? "5" :super.toCheckDigit(charValue);
    }

}
