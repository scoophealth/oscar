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

package oscar.oscarBilling.ca.bc.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.oscarehr.billing.CA.BC.model.BillingTrayFee;
import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.common.model.BillingService;
import org.springframework.stereotype.Repository;

import oscar.util.ConversionUtils;

/**
 * Performing CRUD operations on Billing Procedure/Tray Fee Associations. Some Procedures are automatically associated with certain tray fees
 */
@Repository
public class SupServiceCodeAssocDAO extends AbstractDao<BillingTrayFee> {

	public static final String KEY_ASSOCIATION_STATUS = "associationStatus";
	public static final String KEY_BILLING_SERVICE_TRAY_NO = "billingServiceTrayNo";
	public static final String KEY_BILLING_SERVICE_NO = "billingServiceNo";
	public static final String KEY_ID = "id";

	public static final int VALUE_BY_CODE = 1;
	public static final int VALUE_BY_ID = 2;

	public SupServiceCodeAssocDAO() {
		super(BillingTrayFee.class);
	}

	/**
	 * Returns available procedure code/tray fee code associations. Map uses KEY_* contains available in this DAO for storing the values.
	 * 
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getServiceCodeAssociactions() {
		// TODO test me
		List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
		// TODO replace with #findAll
		List<BillingTrayFee> btfs = entityManager.createQuery(getBaseQuery()).getResultList();
		for (BillingTrayFee btf : btfs) {
			Map<String, String> map = new HashMap<String, String>();
			map.put(KEY_ID, btf.getId().toString());
			map.put(KEY_BILLING_SERVICE_NO, this.getBillingServiceValue(btf.getBillingServiceNo(), SupServiceCodeAssocDAO.VALUE_BY_ID));
			map.put(KEY_BILLING_SERVICE_TRAY_NO, this.getBillingServiceValue(btf.getBillingServiceTrayNo(), SupServiceCodeAssocDAO.VALUE_BY_ID));
			map.put(KEY_ASSOCIATION_STATUS, "");
			ret.add(map);
		}
		return ret;
	}
	
	/**
	 * Returns a Map of ServiceCode associations
	 * Key = Service Code
	 * Value = Tray Fee
	 * 
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getAssociationKeyValues() {
		// TODO test me
		// TODO replace with find all
		List<BillingTrayFee> btfs = entityManager.createQuery("SELECT x FROM BillingTrayFee x").getResultList();
		Map<String, String> ret = new HashMap<String, String>();
		for (BillingTrayFee btf : btfs) {
			String key = this.getBillingServiceValue(btf.getBillingServiceNo(), SupServiceCodeAssocDAO.VALUE_BY_ID);
			String value = this.getBillingServiceValue(btf.getBillingServiceTrayNo(), SupServiceCodeAssocDAO.VALUE_BY_ID);
			ret.put(key, value);
		}
		return ret;
	}

	/**
	 * Saves or updates(if exists) a procedure/tray fee association
	 * @param primaryCode String
	 * @param secondaryCode String
	 */
	@SuppressWarnings("unchecked")
	public void saveOrUpdateServiceCodeAssociation(String primaryCode, String secondaryCode) {
		String primaryCodeId = getBillingServiceValue(primaryCode, SupServiceCodeAssocDAO.VALUE_BY_CODE);
		String secondaryCodeId = getBillingServiceValue(secondaryCode, SupServiceCodeAssocDAO.VALUE_BY_CODE);

		Query query = createQuery("btf", "btf.billingServiceNo = :billingServiceNo");
		query.setParameter("billingServiceNo", primaryCodeId);

		BillingTrayFee btf = null;
		for (BillingTrayFee b : (List<BillingTrayFee>) query.getResultList()) {
			btf = b;
			break;
		}

		if (btf == null) {
			btf = new BillingTrayFee();
			btf.setBillingServiceNo(primaryCodeId);
		}
		btf.setBillingServiceTrayNo(secondaryCodeId);
		saveEntity(btf);
	}

	/**
	 * Returns the monetary value of the specified service code
	 *
	 * @param primaryCode String - The Service Code
	 * @param type int - The type of query to perform using either the billingservice_no or service_code fields
	 * as criterion for searching the code value
	 * @return String
	 */
	@SuppressWarnings("unchecked")
    private String getBillingServiceValue(String code, int type) {
		String queryString = "";
		Object queryParam = null;

		if (type == SupServiceCodeAssocDAO.VALUE_BY_CODE) {
			queryString = "FROM BillingService bs WHERE bs.serviceCode = :param";
			queryParam = code;
		} else if (type == SupServiceCodeAssocDAO.VALUE_BY_ID) {
			queryString = "FROM BillingService bs WHERE bs.billingserviceNo = :param";
			queryParam = ConversionUtils.fromIntString(code);
		} else {
			throw new IllegalArgumentException("Unsupported type" + type);
		}

		Query query = entityManager.createQuery(queryString);
		query.setParameter("param", queryParam);

		List<BillingService> billingServices = query.getResultList();
		if (billingServices.isEmpty()) return "";

		BillingService firstAvailableBillingService = billingServices.get(0);
		if (type == SupServiceCodeAssocDAO.VALUE_BY_CODE) {
			return String.valueOf(firstAvailableBillingService.getBillingserviceNo());
		} else {
			return firstAvailableBillingService.getServiceCode();
		}
	}

	/**
	 * Removes a service code association
	 * 
	 * @param id String - The id of the service code
	 */
	public void deleteServiceCodeAssociation(String id) {
		BillingTrayFee deletionCandidate = find(ConversionUtils.fromIntString(id));
		if (deletionCandidate != null) entityManager.remove(deletionCandidate);
	}
}
