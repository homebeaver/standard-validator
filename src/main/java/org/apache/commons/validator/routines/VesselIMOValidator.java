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

import java.io.Serializable;

import org.apache.commons.validator.routines.checkdigit.VesselIMOCheckDigit;

/**
 * <b>Vessel IMO Number</b> validation.
 *
 * <p>
 * IMO Numbers are unique identification numbers used to identify vessels.
 * </p>
 *
 * <p>
 * Check digit calculation is based on <i>modulus 10</i> with digits being weighted
 * based on their position (from right to left).
 * </p>
 *
 * <p>
 * For further information see
 *  <a href="https://en.wikipedia.org/wiki/IMO_number">Wikipedia - IMO Number</a>.
 * </p>
 *
 * @since 1.9.0
 */
public final class VesselIMOValidator implements Serializable {

    private static final long serialVersionUID = 23388782916114154L;

    /** Singleton instance */
    private static final VesselIMOValidator INSTANCE = new VesselIMOValidator();

    /**
     * Gets the singleton instance of this validator.
     * @return A singleton instance of the IMO Number validator.
     */
    public static VesselIMOValidator getInstance() {
        return INSTANCE;
    }

    /**
     * IMO number consists of 7 numbers prefixed by "IMO" and optional space.
     * Example: IMO9074729
     */
    /*
     * NOTE: the first two groups are non-capturing (?: ... )
     */
    static final String IMO_REGEX = "^(?:IMO)(?:\\s)?(\\d{7})$";

    static final CodeValidator VALIDATOR = new CodeValidator(IMO_REGEX,
        VesselIMOCheckDigit.MIN_LEN, VesselIMOCheckDigit.MAX_LEN,
        VesselIMOCheckDigit.getInstance());

    /**
     * Constructs a validator.
     */
    private VesselIMOValidator() { }

    /**
     * Tests whether the code is a valid IMO number.
     *
     * @param code The code to validate.
     * @return {@code true} if a IMO number, otherwise {@code false}.
     */
    public boolean isValid(final String code) {
        return VALIDATOR.isValid(code);
    }

    /**
     * Checks the code is valid IMO number.
     *
     * @param code The code to validate.
     * @return A IMO number code with prefix removed if valid, otherwise {@code null}.
     */
    public Object validate(final String code) {
        final Object validate = VALIDATOR.validate(code);
        if (validate != null) {
            return VALIDATOR.isValid(code) ? validate : null;
        }
        return validate;
    }

}
