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

package oscar.oscarBilling.ca.on.bean;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.BatchEligibilityDao;
import org.oscarehr.common.model.BatchEligibility;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class BillingEDTOBECOutputSpecificationBeanHandler {

	private BatchEligibilityDao batchEligibilityDao = (BatchEligibilityDao) SpringUtils.getBean("batchEligibilityDao");

	Vector<BillingEDTOBECOutputSpecificationBean> EDTOBECOutputSecifiationBeanVector = new Vector<BillingEDTOBECOutputSpecificationBean>();
	public boolean verdict = true;

	public BillingEDTOBECOutputSpecificationBeanHandler(LoggedInInfo loggedInInfo, FileInputStream file) {
		init(loggedInInfo, file);
	}

	public boolean init(LoggedInInfo loggedInInfo, FileInputStream file) {

		InputStreamReader reader = new InputStreamReader(file);
		BufferedReader input = new BufferedReader(reader);
		String nextline;

		try {

			while ((nextline = input.readLine()) != null) {

				if (nextline.length() > 2) {

					String obecHIN = nextline.substring(0, 10);
					String obecVer = nextline.substring(10, 12);
					String obecResponse = nextline.substring(12, 14);
					BillingEDTOBECOutputSpecificationBean osBean = new BillingEDTOBECOutputSpecificationBean(obecHIN, obecVer, obecResponse);

					DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
					List<Demographic> demos = demographicManager.searchByHealthCard(loggedInInfo, obecHIN);
					if (!demos.isEmpty()) {
						Demographic demo = demos.get(0);
						osBean.setLastName(demo.getLastName());
						osBean.setFirstName(demo.getFirstName());
						osBean.setDOB(demo.getDateOfBirth());
						osBean.setSex(demo.getSex());

						ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
						Provider provider = providerDao.getProvider(StringUtils.trimToNull(demo.getProviderNo()));

						if (provider != null) {
							osBean.setIdentifier(provider.getLastName());
						}
					}
					BatchEligibility batchEligibility = batchEligibilityDao.find(Integer.parseInt(obecResponse));

					if (batchEligibility != null) {
						osBean.setMOH(batchEligibility.getMOHResponse());
					}

					if (nextline.length() > 14) {
						//osBean.setIdentifier(nextline.substring(14,18));
						//osBean.setSex(nextline.substring(18,19));
						//osBean.setDOB(nextline.substring(19,27));
						osBean.setExpiry(nextline.substring(27, 35));
						//osBean.setLastName(nextline.substring(35,65));
						//osBean.setFirstName(nextline.substring(65,85));
						osBean.setSecondName(nextline.substring(85, 105));
						//osBean.setMOH(nextline.substring(105,207));
					}

					EDTOBECOutputSecifiationBeanVector.add(osBean);
				}
			}
		} catch (IOException ioe) {
			MiscUtils.getLogger().error("Error", ioe);
		} catch (StringIndexOutOfBoundsException ioe) {
			verdict = false;
		}
		return verdict;
	}

	public Vector<BillingEDTOBECOutputSpecificationBean> getEDTOBECOutputSecifiationBeanVector() {
		return EDTOBECOutputSecifiationBeanVector;
	}

}
