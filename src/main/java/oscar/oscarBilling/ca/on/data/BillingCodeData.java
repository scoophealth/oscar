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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.common.model.BillingService;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

/**
 *
 * @author Jay Gallagher
 */
public class BillingCodeData {

	private BillingServiceDao billingServiceDao = SpringUtils.getBean(BillingServiceDao.class);

	public List<Hashtable<String, String>> search(String str) {
		ArrayList<Hashtable<String, String>> list = new ArrayList<Hashtable<String, String>>();

		for (BillingService bs : billingServiceDao.findByServiceCodeOrDescription(str)) {
			list.add(fillCodeDataHashtable(bs));
		}
		return list;
	}

	public Hashtable<String, String> fillCodeDataHashtable(BillingService bs) {
		Hashtable<String, String> h = new Hashtable<String, String>();
		if (bs == null) {
			MiscUtils.getLogger().warn("Expected a billing service, but got null");
			return h;
		}

		h.put("service_compositecode", c(bs.getServiceCompositecode()));
		h.put("service_code", c(bs.getServiceCode()));
		h.put("description", c(bs.getDescription()));
		h.put("value", c(bs.getValue()));
		h.put("percentage", c(bs.getPercentage()));
		h.put("billingservice_date", c(ConversionUtils.toDateString(bs.getBillingserviceDate())));
		h.put("specialty", c(bs.getSpecialty()));
		h.put("region", c(bs.getRegion()));
		h.put("anaesthesia", c(bs.getAnaesthesia()));
		return h;
	}

	String c(String str) {
		return (str == null) ? "" : str;
	}

	public Hashtable<String, String> searchBillingCode(String str) {
		Hashtable<String, String> h = null;

		List<BillingService> bss = billingServiceDao.findMostRecentByServiceCode(str);

		for (BillingService bs : bss) {
			h = fillCodeDataHashtable(bs);
		}

		if (h != null) {
			h.put("count", "" + bss.size());
		}

		return h;
	}

	public int searchNumBillingCode(String str) {
		return billingServiceDao.findMostRecentByServiceCode(str).size();
	}

	public boolean editBillingCodeDesc(String desc, String val, String codeId) {
		BillingService bs = billingServiceDao.find(codeId);
		if (bs == null) {
			MiscUtils.getLogger().warn("Unable to find billing service for id " + codeId);
			return false;
		}

		if (desc != null) {
			bs.setDescription(desc);
		}

		if (val != null) {
			bs.setValue(val);
		}

		billingServiceDao.merge(bs);
		return true;
	}

	public boolean editBillingCode(String val, String codeId) {
		return editBillingCodeDesc(null, val, codeId);
	}

	public boolean editBillingCodeByServiceCode(String val, String codeId, String date) {
		for (BillingService bs : billingServiceDao.findByServiceCodeAndDate(codeId, ConversionUtils.fromDateString(date))) {
			bs.setValue(val);
			billingServiceDao.merge(bs);
		}
		return true;
	}

	public boolean insertBillingCode(String value, String code, String date, String description, String termDate) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		BillingService bs = new BillingService();
		bs.setServiceCompositecode("");
		bs.setServiceCode(code);
		bs.setDescription(description);
		bs.setValue(value);
		bs.setPercentage("");
		bs.setBillingserviceDate(formatter.parse(date));
		bs.setSpecialty("");
		bs.setRegion("ON");
		bs.setAnaesthesia("00");
		bs.setTerminationDate(formatter.parse(termDate));
		bs.setGstFlag(false);
		bs.setSliFlag(false);
		billingServiceDao.persist(bs);

		return true;
	}
}
