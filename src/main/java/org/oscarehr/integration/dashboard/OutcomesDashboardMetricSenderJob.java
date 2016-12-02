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
package org.oscarehr.integration.dashboard;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.jobs.OscarRunnable;
import org.oscarehr.common.model.IndicatorTemplate;
import org.oscarehr.common.model.Property;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.managers.DashboardManager;
import org.oscarehr.managers.ProviderManager2;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class OutcomesDashboardMetricSenderJob implements OscarRunnable {

	private Logger logger = MiscUtils.getLogger();

	private Provider provider;
	private Security security;

	private DashboardManager dashboardManager = SpringUtils.getBean(DashboardManager.class);
	private ProviderManager2 providerManager = SpringUtils.getBean(ProviderManager2.class);
	

	@Override
	public void run() {

		try {
			LoggedInInfo x = new LoggedInInfo();
			x.setLoggedInProvider(provider);
			x.setLoggedInSecurity(security);

			logger.info("OutcomesDashboardMetricSenderJob started and running as " + x.getLoggedInProvider().getFormattedName());

			List<Provider> providers = providerManager.getProviders(x, true);
			providers = filterProvidersByShareStatus(providers);

			//get the shared indicators
			List<IndicatorTemplate> sharedIndicatorTemplates = dashboardManager.getIndicatorLibrary(x);

			for (IndicatorTemplate indicatorTemplate : sharedIndicatorTemplates) {

				if (!indicatorTemplate.isShared()) {
					continue;
				}

				for (Provider provider : providers) {
					OutcomesDashboardUtils.sendProviderIndicatorData(x,provider,indicatorTemplate);	
				}
			}
		} catch (Exception e) {
			logger.error("Error", e);

		} finally {
			
		}

	}


	List<Provider> filterProvidersByShareStatus(List<Provider> pList) {
		LoggedInInfo x = new LoggedInInfo();
		x.setLoggedInProvider(provider);
		x.setLoggedInSecurity(security);

		List<Provider> results = new ArrayList<Provider>();

		for (Provider p : pList) {

			List<Property> tmp = providerManager.getProviderProperties(x, p.getProviderNo(), UserProperty.DASHBOARD_SHARE);
			if (tmp.size() > 0) {
				String val = tmp.get(0).getValue();
				if ("true".equals(val)) {
					logger.info("Adding provider " + p.getFormattedName() + " to the shared list");
					results.add(p);
				}
			}
		}
		return results;
	}


	@Override
	public void setLoggedInProvider(Provider provider) {
		this.provider = provider;

	}

	@Override
	public void setLoggedInSecurity(Security security) {
		this.security = security;
	}

}
