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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.oscarehr.billing.CA.ON.model.Billing3rdPartyAddress;
import org.oscarehr.common.dao.Billing3rdPartyAddressDao;
import org.oscarehr.common.dao.BillingONExtDao;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.model.BillingONExt;
import org.oscarehr.util.SpringUtils;

public class JdbcBilling3rdPartImpl {
	private static final Logger _logger = Logger
			.getLogger(JdbcBilling3rdPartImpl.class);
	private ClinicDAO clinicDao = (ClinicDAO)SpringUtils.getBean("clinicDAO");

	public static final String ACTIVE = "1";
	public static final String INACTIVE = "0";

	private Billing3rdPartyAddressDao dao = SpringUtils.getBean(Billing3rdPartyAddressDao.class);
	private BillingONExtDao extDao = (BillingONExtDao)SpringUtils.getBean(BillingONExtDao.class);
	
	

	// 3rd bill ins. address
	public int addOne3rdAddrRecord(Properties val) {
		Billing3rdPartyAddress b = new Billing3rdPartyAddress();
		b.setAttention(val.getProperty("attention", ""));
		b.setCompanyName(val.getProperty("company_name", ""));
		b.setAddress(val.getProperty("address", ""));
		b.setCity(val.getProperty("city", ""));
		b.setProvince(val.getProperty("province", ""));
		b.setPostalCode(val.getProperty("postcode", ""));
		b.setTelephone(val.getProperty("telephone", ""));
		b.setFax(val.getProperty("fax", ""));
		
		dao.persist(b);
		
		return b.getId();
	}

	public boolean update3rdAddr(String id, Properties val) {
		Billing3rdPartyAddress b = dao.find(Integer.parseInt(id));
		if(b != null) {
			b.setAttention(val.getProperty("attention", ""));
			b.setCompanyName(val.getProperty("company_name", ""));
			b.setAddress(val.getProperty("address", ""));
			b.setCity(val.getProperty("city", ""));
			b.setProvince(val.getProperty("province", ""));
			b.setPostalCode(val.getProperty("postcode", ""));
			b.setTelephone(val.getProperty("telephone", ""));
			b.setFax(val.getProperty("fax", ""));
			dao.merge(b);
			return true;
		}
		return false;
	}
    
	public boolean add3rdBillExt(String billingNo, String demoNo, String key, String value) {
		BillingONExt b = new BillingONExt();
		b.setBillingNo(Integer.parseInt(billingNo));
		b.setDemographicNo(Integer.parseInt(demoNo));
		b.setKeyVal(key);
		b.setDateTime(new Date());
		b.setStatus(ACTIVE.toCharArray()[0]);
		b.setValue(value);
		
		extDao.persist(b);
		
		return true;
	}

        public boolean keyExists(String billingNo, String key) {
        	List<BillingONExt> results = extDao.findByBillingNoAndKey(Integer.parseInt(billingNo),key);
        	if(results.isEmpty())
        		return false;
        	return true;
        }
        
        
    public boolean updateKeyStatus(String billingNo, String key, String status) {
    	List<BillingONExt> results = extDao.findByBillingNoAndKey(Integer.parseInt(billingNo),key);
    	for(BillingONExt result:results) {
    		result.setStatus(status.toCharArray()[0]);
    		extDao.merge(result);
    	}
		return true;
	}

    /*
     * We're updating a key--make sure it is active as well
     */
	public boolean updateKeyValue(String billingNo, String key, String value) {
		List<BillingONExt> results = extDao.findByBillingNoAndKey(Integer.parseInt(billingNo),key);
    	for(BillingONExt result:results) {
    		result.setValue(value);
    		result.setStatus('1');
    		extDao.merge(result);
    	}
		return true;
	}
	

	public List<Properties> get3rdAddrNameList() {
		List<Properties> ret = new ArrayList<Properties>();
		
		List<Billing3rdPartyAddress> results = dao.findAll();
		Collections.sort(results, Billing3rdPartyAddress.COMPANY_NAME_COMPARATOR);
		for(Billing3rdPartyAddress result:results) {
			Properties prop = new Properties();
			prop.setProperty("id", result.getId().toString());
			prop.setProperty("company_name", result.getCompanyName());
			ret.add(prop);
		}
		return ret;
	}


	public Properties get3rdAddr(String id) {
		Properties prop = new Properties();
		Billing3rdPartyAddress b = dao.find(Integer.parseInt(id));
		if(b != null) {
			prop.setProperty("id", id);
			prop.setProperty("attention", b.getAttention());
			prop.setProperty("company_name", b.getCompanyName());
			prop.setProperty("address", b.getAddress());
			prop.setProperty("city", b.getCity());
			prop.setProperty("province", b.getProvince());
			prop.setProperty("postcode", b.getPostalCode());
			prop.setProperty("telephone", b.getTelephone());
			prop.setProperty("fax", b.getFax());
		}
		
		return prop;
	}

	public Properties get3rdAddrProp(String name) {
		Properties prop = new Properties();
		List<Billing3rdPartyAddress> results = dao.findByCompanyName(name);
		for(Billing3rdPartyAddress b:results) {
			prop.setProperty("id", b.getId().toString());
			prop.setProperty("attention", b.getAttention());
			prop.setProperty("company_name", b.getCompanyName());
			prop.setProperty("address", b.getAddress());
			prop.setProperty("city", b.getCity());
			prop.setProperty("province", b.getProvince());
			prop.setProperty("postcode", b.getPostalCode());
			prop.setProperty("telephone", b.getTelephone());
			prop.setProperty("fax", b.getFax());
		}
		return prop;
	}
}
