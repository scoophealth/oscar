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

package oscar.oscarBilling.ca.on.data;

import java.util.ArrayList;
import java.util.Hashtable;

import org.oscarehr.common.dao.BillingDao;
import org.oscarehr.common.model.Billing;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

/**
 *
 * @author jay
 */
public class BillingData {

	/** Creates a new instance of BillingData */
	public BillingData() {
	}

	public ArrayList<Hashtable<String, Object>> getBills(String statusType, String providerNo, String startDate, String endDate, String demoNo) {
		ArrayList<Hashtable<String, Object>> list = new ArrayList<Hashtable<String, Object>>();
		BillingDao dao = SpringUtils.getBean(BillingDao.class);		
		for (Billing b : dao.findBillings(ConversionUtils.fromIntString(demoNo), statusType, providerNo, ConversionUtils.fromDateString(startDate), ConversionUtils.fromDateString(endDate))) {
			Hashtable<String, Object> h = new Hashtable<String, Object>();
			h.put("billing_no", b.getId());
			h.put("demographic_no", ConversionUtils.toIntString(b.getDemographicNo()));
			h.put("status", b.getStatus());
			h.put("provider_no", b.getProviderNo());
			h.put("demographic_name", b.getDemographicName());
			h.put("billing_date", ConversionUtils.toDateString(b.getBillingDate()));
			h.put("billing_time", ConversionUtils.toTimeString(b.getBillingTime()));
			h.put("total", b.getTotal());
			list.add(h);
		}
		return list;
	}
}
