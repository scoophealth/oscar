/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

/*
 * GstReport.java
 *
 * Created on August 15, 2007, 5:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarBilling.ca.on.administration;

import java.util.Properties;
import java.util.Vector;

import org.oscarehr.common.dao.BillingONExtDao;
import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.common.model.BillingONExt;
import org.oscarehr.common.model.BillingService;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

public class GstReport {

	public Vector<Properties> getGST(LoggedInInfo loggedInInfo, String providerNo, String startDate, String endDate)  {
		Properties props;
		Vector<String> billno = new Vector<String>();
		Vector<Properties> list = new Vector<Properties>();
		// First find all the billing_no referring to the selected provider_no.
		BillingONExtDao dao = SpringUtils.getBean(BillingONExtDao.class);
		DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);

		for (BillingONExt e : dao.find("provider_no", providerNo)) {
			billno.add("" + e.getBillingNo());
		}

		// For every bill the provider is involved with, search the gst value, date, demo no within the chosen dates
		for (int i = 0; i < billno.size(); i++) {
			for (BillingONExt e : dao.find(ConversionUtils.fromIntString(billno.get(i)), "gst", ConversionUtils.fromDateString(startDate), ConversionUtils.fromDateString(endDate))) {
				props = new Properties();
				props.setProperty("gst", e.getValue());
				props.setProperty("date", ConversionUtils.toDateString(e.getDateTime()));
				props.setProperty("demographic_no", "" + e.getDemographicNo());

				Demographic demo = demographicManager.getDemographic(loggedInInfo,e.getDemographicNo());
				if (demo != null) {
					props.setProperty("name", demo.getFirstName() + " " + demo.getLastName());
				}

				for (BillingONExt ee : dao.find(ConversionUtils.fromIntString(billno.get(i)), "total", ConversionUtils.fromDateString(startDate), ConversionUtils.fromDateString(endDate))) {
					props.setProperty("total", ee.getValue());
				}
				list.add(props);
			}
		}
		return list;
	}

	public String getGstFlag(String code, String date) {
		BillingServiceDao dao = SpringUtils.getBean(BillingServiceDao.class);
		for (BillingService bs : dao.findGst(code, ConversionUtils.fromDateString(date))) {
			return ConversionUtils.toBoolString(bs.getGstFlag());
		}
		return "";
	}
}
