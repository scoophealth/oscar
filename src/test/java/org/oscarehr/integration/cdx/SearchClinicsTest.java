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
import ca.uvic.leadlab.obibconnector.facades.registry.ISearchClinic;
import ca.uvic.leadlab.obibconnector.impl.registry.SearchClinic;
import org.junit.Assert;
import org.junit.Test;
import org.oscarehr.util.MiscUtils;

import java.util.List;

public class SearchClinicsTest {
    private final CDXConfiguration config = new CDXConfiguration();
    private final String clinicId = config.getClinicId();
    private String receivedMessage = null;

    @Test
    public void testFindByName() {
        ISearchClinic searchClinic = new SearchClinic(config);
        List<IClinic> clinics = null;

        try {
            clinics = searchClinic.findByName("gandalf");
        } catch (OBIBException e) {
            receivedMessage = e.getMessage();
            MiscUtils.getLogger().info(receivedMessage);
        } catch (Exception e) {
            MiscUtils.getLogger().info(e.getStackTrace());
        }
        if (clinics != null) {
            MiscUtils.getLogger().info("num of CDX clinics by name : " + clinics.size());
        } else {
            MiscUtils.getLogger().info("cdx clinics is null by name");
        }

        String expectedErrorMsg = "Error finding clinics by name.";
        Assert.assertEquals(expectedErrorMsg, receivedMessage);
        //Assert.assertNotNull(clinics);
    }

    @Test
    public void testFindByAddress() {
        ISearchClinic searchClinic = new SearchClinic(config);
        List<IClinic> clinics = null;

        try {
            clinics = searchClinic.findByAddress("the address");
        } catch (OBIBException e) {
            receivedMessage = e.getMessage();
            MiscUtils.getLogger().info(receivedMessage);
        } catch (Exception e) {
            MiscUtils.getLogger().info(e.getStackTrace());
        }
        if (clinics != null) {
            MiscUtils.getLogger().info("num of CDX clinics by address : " + clinics.size());
        } else {
            MiscUtils.getLogger().info("cdx clinics is null by address");
        }

        String expectedErrorMsg = "Error finding clinics by address.";
        Assert.assertEquals(expectedErrorMsg, receivedMessage);
        //Assert.assertNotNull(clinics);
    }

    @Test
    public void testFindById() {
        ISearchClinic searchClinic = new SearchClinic(config);
        List<IClinic> clinics = null;

        try {
            clinics = searchClinic.findByID(clinicId);
        } catch (OBIBException e) {
            receivedMessage = e.getMessage();
            MiscUtils.getLogger().info(receivedMessage);
        } catch (Exception e) {
            MiscUtils.getLogger().info(e.getStackTrace());
        }
        if (clinics != null) {
            MiscUtils.getLogger().info("num of CDX clinics by id: " + clinics.size());
        } else {
            MiscUtils.getLogger().info("cdx clinics is null by id");
        }

        String expectedErrorMsg = "Error finding clinics by id.";
        Assert.assertEquals(expectedErrorMsg, receivedMessage);
        //Assert.assertNotNull(clinics);
    }

    @Test(expected = OBIBException.class)
    public void testFindByIdError() throws Exception {
        ISearchClinic searchClinic = new SearchClinic(config);

        List<IClinic> clinics = searchClinic.findByID("__Wrong_ID");

        Assert.assertNull(clinics);
    }
}