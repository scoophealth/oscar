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
import ca.uvic.leadlab.obibconnector.facades.registry.IProvider;
import ca.uvic.leadlab.obibconnector.facades.registry.ISearchProviders;
import ca.uvic.leadlab.obibconnector.impl.registry.SearchProviders;
import org.junit.Assert;
import org.junit.Test;
import org.oscarehr.util.MiscUtils;

import java.util.List;

public class SearchProvidersTest {
    private final CDXConfiguration config = new CDXConfiguration();
    private String receivedMessage = null;

    @Test
    public void testFindByProviderId() {

        ISearchProviders searchProviders = new SearchProviders(config);
        List<IProvider> providers = null;
        try {
            providers = searchProviders.findByProviderID("93188");
        } catch (OBIBException e) {
            receivedMessage = e.getMessage();
            MiscUtils.getLogger().info(receivedMessage);
        } catch (Exception e) {
            MiscUtils.getLogger().info(e.getStackTrace());
        }
        if (providers != null) {
            MiscUtils.getLogger().info("num of CDX providers: " + providers.size());
        } else {
            MiscUtils.getLogger().info("cdx providers is null");
        }
        //Assert.assertNotNull(providers);
        String expectedErrorMsg = "Error finding providers by id.";
        Assert.assertEquals(expectedErrorMsg, receivedMessage);
    }

    @Test(expected = OBIBException.class)
    public void testFindByProviderIdError() throws Exception {

        ISearchProviders searchProviders = new SearchProviders(config);
        List<IProvider> providers = searchProviders.findByProviderID("__Wrong_ID");
        Assert.assertNull(providers);
    }
}

