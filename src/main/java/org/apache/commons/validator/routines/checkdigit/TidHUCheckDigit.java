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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Check digit calculation based on <em>modulus 11</em> and weighs based on the digit position.
 * <p>
 * Digits are weighted based by their position, from left to right with the 
 * first digit being weighted 1, the second 2 and so on. Check digit cannot be 10.
 * </p>
 * <p>
 * This module is used to calculate Hungarian TIN check digits „adóazonosító jel“ for natural persons 
 * and "Adószám" for companies and organisations. 
 * See <a href="https://hu.wikipedia.org/wiki/Ad%C3%B3azonos%C3%ADt%C3%B3_jel">Wikipedia (hu)</a>
 * and <a href="https://hu.wikipedia.org/wiki/Ad%C3%B3sz%C3%A1m">Adószám (hu)</a>
 * for more details.
 * </p>
 *
 * @author EUG https://github.com/homebeaver
 * @since 2.10.6
 */
public class TidHUCheckDigit extends Modulus11iLeftCheckDigit {

    private static final long serialVersionUID = 2699080857569548906L;

    /** Singleton Check Digit instance */
    private static final TidHUCheckDigit INSTANCE = new TidHUCheckDigit();
    // used for Adószám:
    private static final ModulusCheckDigit MOD10CD = new ModulusTenCheckDigit(new int[] { 1, 3, 7, 9 }, true);

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
    TidHUCheckDigit() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String calculate(final String code) throws CheckDigitException {
        if (code.length() > 7) {
            // natural person TIN : `8gebtt999p` - gebtt - gebtt days after 1.Jan.1867
            boolean validBirthday = false;
            try {
                validBirthday = validBirthday(code.substring(1, 6));
            } catch (final NumberFormatException ex) {
                throw new CheckDigitException(CheckDigitException.invalidCode(code));
            } catch (final ParseException ex) {
                System.out.println("Exception in validBirthday: "+ex.getMessage());
                throw new CheckDigitException("internal Error");
            }
            if (!validBirthday) {
                throw new CheckDigitException(CheckDigitException.invalidCode(code, "invalid Birthday"));
            }
            return super.calculate(code);
        }
        return MOD10CD.calculate(code);
    }

    private boolean validBirthday(String gebtt) throws ParseException, NumberFormatException {
//	    System.out.println("validBirthday gebtt "+gebtt);
        int days = Integer.parseInt(gebtt); // throws NumberFormatException
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse("1867-01-01")); // throws ParseException
        c.add(Calendar.DATE, days);  // number of days to add
        Calendar today = Calendar.getInstance();
//	    System.out.println("validBirthday days "+days + " after 1867-01-01 => "+sdf.format(c.getTime()));
        if (c.compareTo(today) >= 0) {
            System.out.println("Not valid Birthday "+sdf.format(c.getTime()) + " == 1867-01-01 + "+days+ " days.");
            return false;
        } else {
//	        System.out.println("validBirthday "+sdf.format(c.getTime()) + " compareTo(today) < 0");
            return true;
        }
    }

}
