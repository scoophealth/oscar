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

package oscar.oscarReport.data;

import java.util.Arrays;
import java.util.List;

import org.oscarehr.common.dao.BillingDao;
import org.oscarehr.common.dao.BillingONCHeader1Dao;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.util.ConversionUtils;

public class VisitReportData {

	private String dateBegin = null;
	private String dateEnd = null;
	private String providerNo = null;

	public VisitReportData() {
	}

	public void setDateBegin(String value) {
		dateBegin = value;
	}

	public void setDateEnd(String value) {
		dateEnd = value;
	}

	public void setProviderNo(String value) {
		providerNo = value;
	}

	public String[] getCreatorCount() {
		String retval, retcount;
		String[] retVisit = new String[6];
		Arrays.fill(retVisit, "0");

		List<Object[]> billingCounts;
		boolean isNewBilling = OscarProperties.getInstance().getBooleanProperty("isNewONbilling", "true");
		if (isNewBilling) {
			BillingONCHeader1Dao dao = SpringUtils.getBean(BillingONCHeader1Dao.class);
			billingCounts = dao.countBillingVisitsByCreator(providerNo, ConversionUtils.fromDateString(dateBegin), ConversionUtils.fromDateString(dateEnd));
		} else {
			BillingDao dao = SpringUtils.getBean(BillingDao.class);
			billingCounts = dao.countBillingVisitsByCreator(providerNo, ConversionUtils.fromDateString(dateBegin), ConversionUtils.fromDateString(dateEnd));
		}

		for (Object[] o : billingCounts) {
			String visitType = String.valueOf(o[0]);
			if (visitType != null && !visitType.equals("")) {
				// get last character
				visitType = visitType.substring(visitType.length() - 1);
			} else {
				visitType = "";
			}

			retval = visitType;
			retcount = String.valueOf(o[1]);

			int retvalAsInt = Integer.parseInt(retval);
			if ((retvalAsInt >= 0) && (retvalAsInt < retVisit.length)) {
				retVisit[retvalAsInt] = retcount;
			}
		}

		return retVisit;
	}
	
	public String[] getApptProviderCount() {
		String retval, retcount;
		String[] retVisit = new String[6];
		Arrays.fill(retVisit, "0");

		List<Object[]> billingCounts;
		boolean isNewBilling = OscarProperties.getInstance().getBooleanProperty("isNewONbilling", "true");
		if (isNewBilling) {
			BillingONCHeader1Dao dao = SpringUtils.getBean(BillingONCHeader1Dao.class);
			billingCounts = dao.countBillingVisitsByProvider(providerNo, ConversionUtils.fromDateString(dateBegin), ConversionUtils.fromDateString(dateEnd));
		} else {
			BillingDao dao = SpringUtils.getBean(BillingDao.class);
			billingCounts = dao.countBillingVisitsByProvider(providerNo, ConversionUtils.fromDateString(dateBegin), ConversionUtils.fromDateString(dateEnd));
		}

		for (Object[] o : billingCounts) {
			String visitType = String.valueOf(o[0]);
			if (visitType != null && !visitType.equals("")) {
				// get last character
				visitType = visitType.substring(visitType.length() - 1);
			} else {
				visitType = "";
			}

			retval = visitType;
			retcount = String.valueOf(o[1]);

			int retvalAsInt = Integer.parseInt(retval);
			if ((retvalAsInt >= 0) && (retvalAsInt < retVisit.length)) {
				retVisit[retvalAsInt] = retcount;
			}
		}

		return retVisit;
	}

}
