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
import ca.uvic.leadlab.obibconnector.impl.send.SubmitDoc;
import ca.uvic.leadlab.obibconnector.facades.datatypes.*;
import org.junit.Assert;
import org.junit.Test;
import org.oscarehr.util.MiscUtils;

import java.util.Date;

public class SubmitDocTest {

    @Test
    public void testSubmitDoc() {
        String response = null;
        try {
            response = new SubmitDoc("11111")
                    .patient()
                        .id("2222")
                        .name(NameType.LEGAL, "Joe", "Wine")
                        .  address(AddressType.HOME, "111 Main St", "Victoria", "BC", "V8V Z9Z", "CA")
                        .phone(TelcoType.HOME, "250-111-1234")
                    .and().author()
                        .id("3333")
                        .time(new Date())
                        .name(NameType.LEGAL, "Joseph", "Cloud")
                        .address(AddressType.HOME, "111 Main St", "Victoria", "BC", "V8V Z9Z", "CA")
                        .phone(TelcoType.HOME, "250-111-1234")
                    .and().recipient()
                        .id("4444")
                        .name(NameType.LEGAL, "Joseph", "Cloud")
                        .address(AddressType.HOME, "111 Main St", "Victoria", "BC", "V8V Z9Z", "CA")
                        .phone(TelcoType.HOME, "250-111-1234")
                    .and().participant()
                        .id("555")
                        .name(NameType.LEGAL, "Joseph", "Cloud")
                        .address(AddressType.HOME, "111 Main St", "Victoria", "BC", "V8V Z9Z", "CA")
                        .phone(TelcoType.HOME, "250-111-1234")
                    .submit();
        } catch (OBIBException e) {
            MiscUtils.getLogger().error(e.getMessage());
        }

        MiscUtils.getLogger().info("obibconnector response: "+ response);

        Assert.assertNotNull(response);
    }

}

