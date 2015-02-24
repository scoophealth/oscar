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
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.common.model.BillingService;

import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

public class JdbcBillingCodeImpl {
	
	private BillingServiceDao dao = (BillingServiceDao)SpringUtils.getBean(BillingServiceDao.class);
	
	public List getBillingCodeAttr(String serviceCode) {
		List ret = new Vector();
		List<BillingService> bs = dao.getBillingCodeAttr(serviceCode);
		for(BillingService b:bs) {
			ret.add(serviceCode);
			ret.add(b.getDescription());
			ret.add(b.getValue());
			ret.add(b.getPercentage());
			ret.add(ConversionUtils.toDateString(b.getBillingserviceDate()));
           ret.add(b.getGstFlag());
        }
		
		return ret;
	}

	public Properties getCodeDescByNames(List serviceCodeNames) {
		Properties ret = new Properties();
		
		List<String> serviceCodeList = new ArrayList<String>();
		serviceCodeList.addAll(serviceCodeNames);
		List<BillingService> bs = dao.findByServiceCodes(serviceCodeList);
		for(BillingService b:bs) {
			ret.setProperty(b.getServiceCode(), b.getDescription());
		}
		
		return ret;
	}

	public boolean updateCodeByName(String serviceCode, String description, String value, String percentage, String billingservice_date, String gstFlag) {
		
		List<BillingService> bs = dao.findByServiceCode(serviceCode);
		for(BillingService b:bs) {
			b.setDescription(description);
			b.setValue(value);
			b.setPercentage(percentage);
			b.setGstFlag(Boolean.valueOf(gstFlag));
			b.setBillingserviceDate(ConversionUtils.fromDateString(billingservice_date));
			dao.merge(b);
			
		}
		return true;
	}

	public int addCodeByStr(String serviceCode, String description, String value, String percentage, String billingservice_date, String gstFlag) {
		BillingService b = new BillingService();
		b.setServiceCompositecode("");
		b.setServiceCode(serviceCode);
		b.setDescription(description);
		b.setValue(value);
		b.setPercentage(percentage);
		b.setGstFlag(Boolean.valueOf(gstFlag));
		b.setBillingserviceDate(ConversionUtils.fromDateString(billingservice_date));
		b.setSliFlag(false);		
		b.setTerminationDate(ConversionUtils.fromDateString("9999-12-31"));
		
		dao.persist(b);
		
		return b.getId();
	}

	public boolean deletePrivateCode(String serviceCode) {
		List<BillingService> bs = dao.findByServiceCode(serviceCode);
		for(BillingService b:bs) {
			dao.remove(b.getId());
		}
		return true;
	}

	public List<String> getPrivateBillingCodeDesc() {
		List<String> ret = new ArrayList<String>();
		
		List<BillingService> bs = dao.finAllPrivateCodes();
		for(BillingService b:bs) {
			ret.add(b.getServiceCode());
			ret.add(b.getDescription());
		}
		return ret;
	}

}
