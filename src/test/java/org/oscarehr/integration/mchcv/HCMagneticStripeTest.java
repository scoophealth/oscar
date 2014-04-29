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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class HCMagneticStripeTest {

    private HCMagneticStripe stripe;
    private final static String MAGNETIC_STRIPE_EXAMPLE = "%b6100549267294685^FOX/AMANDA                ^1501799219800407DKJACOB10010101?5";

    public HCMagneticStripeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        stripe = new HCMagneticStripe(MAGNETIC_STRIPE_EXAMPLE);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getHealthNumber method, of class HCMagneticStripe.
     */
    @Test
    public void testGetHealthNumber() {
        String expResult = "9267294685";
        String result = stripe.getHealthNumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFirstName method, of class HCMagneticStripe.
     */
    @Test
    public void testGetFirstName() {
        String expResult = "AMANDA";
        String result = stripe.getFirstName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLastName method, of class HCMagneticStripe.
     */
    @Test
    public void testGetLastName() {
        String expResult = "FOX";
        String result = stripe.getLastName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getExpiryDate method, of class HCMagneticStripe.
     */
    @Test
    public void testGetExpiryDate() {
        String expected = "20150107";
        String actual = stripe.getExpiryDate();
        assertEquals(expected.toString(), actual.toString());        
    }

    /**
     * Test of getSex method, of class HCMagneticStripe.
     */
    @Test
    public void testGetSex() {
        String expResult = "F";
        String result = stripe.getSex();
        assertEquals(expResult, result);
    }

    /**
     * Test of getBirthDate method, of class HCMagneticStripe.
     */
    @Test
    public void testGetBirthDate() {
        String expected = "19800407";
        String actual = stripe.getBirthDate();
        assertEquals(expected.toString(), actual.toString());
    }

    /**
     * Test of getCardVersion method, of class HCMagneticStripe.
     */
    @Test
    public void testGetCardVersion() {
        String expResult = "DK";
        String result = stripe.getCardVersion();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIssueDate method, of class HCMagneticStripe.
     */
    @Test
    public void testGetIssueDate() {
        String expected = "20100101";
        String actual = stripe.getIssueDate();
        assertEquals(expected.toString(), actual.toString()); 
    }
}