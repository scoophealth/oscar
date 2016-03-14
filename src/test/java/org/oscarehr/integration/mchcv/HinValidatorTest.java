/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.integration.mchcv;

import static junit.framework.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class HinValidatorTest {

    private static HCValidator validator;
    
    @BeforeClass
    public static void init() {
        validator = HCValidationFactory.getSimpleValidator();
    }

    @Test
    public void testValidHin()  {
        HCValidationResult validationResult = validator.validate("9287170261", "DK");
        boolean isValid = validationResult.isValid();
        assertEquals(true, isValid);
    }

    @Test
    public void testNonValidHin()  {
        HCValidationResult validationResult = validator.validate("1286844022", "YX");
        boolean isValid = validationResult.isValid();
        assertEquals(true, isValid);
    }
    
    @Test
    public void testResponseCode()  {
        HCValidationResult validationResult = validator.validate("1262643149", "CT");
        String actual = validationResult.getResponseCode();
        String expected = "51";
        assertEquals(expected, actual);
    }
    
    @Test
    @Ignore("Valid only for online validation")
    public void testResponseDescription()  {
        HCValidationResult validationResult = validator.validate("1108809904", "CW");
        String actual = validationResult.getResponseDescription();
        String expected = "Health card cancelled or voided";
        assertEquals(expected, actual);
    }    
    
    @Test
    @Ignore("Valid only for online validation")
    public void testResponseAction()  {
        HCValidationResult validationResult = validator.validate("1357557162", "");
        String actual = validationResult.getResponseAction();
        String expected = "Ask the cardholder to visit the local ServiceOntario office to maintain his/her future health care coverage.";
        assertEquals(expected, actual);
    }    
    
    @Test
    @Ignore("Valid only for online validation")
    public void testFirstName()  {
        HCValidationResult validationResult = validator.validate("1023947722", "J");
        String actual = validationResult.getFirstName();
        String expected = "test first name4";
        assertEquals(expected, actual);
    }
    
    @Test
    @Ignore("Valid only for online validation")
    public void testLastName()  {
        HCValidationResult validationResult = validator.validate("1023947722", "J");
        String actual = validationResult.getLastName();
        String expected = "test last name4";
        assertEquals(expected, actual);
    }      

    @Test
    @Ignore("Valid only for online validation")
    public void testGender()  {
        HCValidationResult validationResult = validator.validate("1023947722", "J");
        String actual = validationResult.getGender();
        String expected = "M";
        assertEquals(expected, actual);
    }        

    @Test
    @Ignore("Valid only for online validation")
    public void testBirthDate() {
        HCValidationResult validationResult = validator.validate("1023947722", "J");
        String actual = validationResult.getBirthDate();
        String expected = "19881112";
        assertEquals(expected.toString(), actual.toString());
    }        
}