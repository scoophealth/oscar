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

package oscar.oscarBilling.ca.on.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.BillingONCHeader1Dao;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

public class BillingStatusData {
	private static final Logger _logger = Logger.getLogger(BillingStatusData.class);

	public BillingStatusData() {
	}

	public ArrayList<Hashtable<String,Object>> getBills(String statusType, String providerNo, String startDate, String endDate, String demoNo) {
		ArrayList<Hashtable<String,Object>> list = new ArrayList<Hashtable<String,Object>>();
		
		String providerParam = "";
		Date startDateParam = null;
		Date endDateParam = null;
		Integer demoNoParam = null;
		
		if (providerNo != null && !providerNo.trim().equalsIgnoreCase("all")) {
			providerParam = providerNo;
		}

		if (startDate != null && !startDate.trim().equalsIgnoreCase("")) {
			startDateParam = ConversionUtils.fromDateString(startDate);
		}

		if (endDate != null && !endDate.trim().equalsIgnoreCase("")) {
			endDateParam = ConversionUtils.fromDateString(endDate);
		}
		if (demoNo != null && !demoNo.trim().equalsIgnoreCase("")) {
			demoNoParam = ConversionUtils.fromIntString(demoNo);
		}

		try {
			BillingONCHeader1Dao dao = SpringUtils.getBean(BillingONCHeader1Dao.class);
			for(BillingONCHeader1 hh : dao.findBillingsByManyThings(statusType, providerParam, startDateParam, endDateParam, demoNoParam)) {
				Hashtable<String,Object> h = new Hashtable<String,Object>();
				h.put("billing_no", "" + hh.getId());
				h.put("demographic_no", "" + hh.getDemographicNo());
				h.put("status", hh.getStatus());
				h.put("provider_no", hh.getProviderNo());
				h.put("demographic_name", hh.getDemographicName());
				h.put("billing_date", ConversionUtils.toTimeString(hh.getBillingDate()));
				h.put("billing_time", ConversionUtils.toTimeString(hh.getBillingTime()));
				h.put("total", hh.getTotal());
				h.put("clinic", hh.getClinic());
				list.add(h);
			}
		} catch (Exception e) {
			_logger.error("Error",e);
		}
		return list;
	}
}
