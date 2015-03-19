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

package org.oscarehr.ws;

import java.util.List;

import javax.jws.WebService;

import org.apache.cxf.annotations.GZIP;
import org.oscarehr.common.model.Property;
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.ProviderManager2;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.transfer_objects.ProviderPropertyTransfer;
import org.oscarehr.ws.transfer_objects.ProviderTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
@GZIP(threshold = AbstractWs.GZIP_THRESHOLD)
public class ProviderWs extends AbstractWs {
	@Autowired
	private ProviderManager2 providerManager;

	public ProviderTransfer getLoggedInProviderTransfer() {
		LoggedInInfo loggedInInfo = getLoggedInInfo();
		return (ProviderTransfer.toTransfer(loggedInInfo.getLoggedInProvider()));
	}

	/**
	 * @deprecated 2013-03-27 parameter should have been an object to allow nulls
	 */
	public ProviderTransfer[] getProviders(boolean active) {
		return (getProviders2(active));
	}

	public ProviderTransfer[] getProviders2(Boolean active) {
		List<Provider> tempResults = providerManager.getProviders(getLoggedInInfo(), active);
		ProviderTransfer[] results = ProviderTransfer.toTransfers(tempResults);
		return (results);
	}

	public ProviderPropertyTransfer[] getProviderProperties(String providerNo, String propertyName) {
		List<Property> tempResults = providerManager.getProviderProperties(getLoggedInInfo(), providerNo, propertyName);
		ProviderPropertyTransfer[] results = ProviderPropertyTransfer.toTransfers(tempResults);
		return (results);
	}
}
