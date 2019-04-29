/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */

package org.oscarehr.integration.cdx;

import ca.uvic.leadlab.obibconnector.facades.exceptions.OBIBException;
import ca.uvic.leadlab.obibconnector.facades.registry.IClinic;
import ca.uvic.leadlab.obibconnector.facades.registry.IProvider;
import ca.uvic.leadlab.obibconnector.facades.registry.ISearchClinic;
import ca.uvic.leadlab.obibconnector.impl.registry.SearchClinic;
import org.junit.Assert;
import org.junit.Test;
import org.oscarehr.util.MiscUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchClinicsTest extends FacadesBaseTest {
    private String result = null;

    @Test
    public void testFindByName() {
        ISearchClinic searchClinic = new SearchClinic(configClinicC);
        List<IClinic> clinics = null;
        String expectedErrorMsg = "Error finding clinics by name.";
        String notNullClinics = "Providers not null";
        List<String> expectedResults = new ArrayList<String>(Arrays.asList(notNullClinics,expectedErrorMsg));
        result = null;
        try {
            clinics = searchClinic.findByName("oscar");
        } catch (OBIBException e) {
            result = e.getMessage();
            MiscUtils.getLogger().warn(result);
        } catch (Exception e) {
            result = e.getMessage(); //unexpected outcome
            MiscUtils.getLogger().error(e.getStackTrace());
        }
        if (clinics != null) {
            result = notNullClinics;
            MiscUtils.getLogger().debug("Num of CDX clinics by name : " + clinics.size());
            for (IClinic c: clinics) {
                MiscUtils.getLogger().debug("Found: " + c.getName()+" "+c.getCity()+" "+c.getID());
            }
        } else {
            MiscUtils.getLogger().debug("CDX clinics is null for search by name");
        }

        Assert.assertTrue("The list of expected outcomes does not contain the value " + result,
                expectedResults.contains(result));
    }

    @Test
    public void testFindByAddress() {
        ISearchClinic searchClinic = new SearchClinic(configClinicC);
        List<IClinic> clinics = null;
        String expectedErrorMsg = "Error finding clinics by address.";
        String notNullClinics = "Clinics not null";
        List<String> expectedResults = new ArrayList<String>(Arrays.asList(notNullClinics,expectedErrorMsg));
        result = null;
        try {
            clinics = searchClinic.findByAddress("Kelowna");
        } catch (OBIBException e) {
            result = e.getMessage();
            MiscUtils.getLogger().warn(result);
        } catch (Exception e) {
            result = e.getMessage(); //unexpected outcome
            MiscUtils.getLogger().error(e.getStackTrace());
        }
        if (clinics != null) {
            result = notNullClinics;
            MiscUtils.getLogger().debug("Num of CDX clinics found in search by address : " + clinics.size());
            for (IClinic c: clinics) {
                MiscUtils.getLogger().debug("Found: " + c.getName()+" "+c.getCity()+" "+c.getID());
            }
        } else {
            MiscUtils.getLogger().debug("CDX clinics is null in search by address");
        }
        Assert.assertTrue("The list of expected outcomes does not contain the value " + result, expectedResults.contains(result));
    }

    @Test
    public void testFindById() {
        ISearchClinic searchClinic = new SearchClinic(configClinicC);
        List<IClinic> clinics = null;
        String expectedErrorMsg = "Error finding clinics by id.";
        String notNullClinics = "Clinics not null";
        List<String> expectedResults = new ArrayList<String>(Arrays.asList(notNullClinics,expectedErrorMsg));
        result = null;
        try {
            clinics = searchClinic.findByID(clinicIdA);
        } catch (OBIBException e) {
            result = e.getMessage();
            MiscUtils.getLogger().warn(result);
        } catch (Exception e) {
            result = e.getMessage(); //unexpected outcome
            MiscUtils.getLogger().error(e.getStackTrace());
        }
        if (clinics != null) {
            result = notNullClinics;
            MiscUtils.getLogger().debug("Num of CDX clinics found in search by id: " + clinics.size());
            for (IClinic c: clinics) {
                MiscUtils.getLogger().debug("Found: " + c.getName()+" "+c.getCity()+" "+c.getID());
            }
        } else {
            MiscUtils.getLogger().debug("CDX clinics is null for search by id");
        }
        Assert.assertTrue("The list of expected outcomes does not contain the value " + result, expectedResults.contains(result));
    }

    @Test(expected = OBIBException.class)
    public void testFindByIdError() throws Exception {
        ISearchClinic searchClinic = new SearchClinic(configClinicA);

        List<IClinic> clinics = searchClinic.findByID("__Wrong_ID");

        Assert.assertNull("clinics expected to be null but is not null", clinics);
    }
}