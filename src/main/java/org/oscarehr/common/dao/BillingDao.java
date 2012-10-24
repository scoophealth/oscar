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

package org.oscarehr.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Query;

import org.oscarehr.common.model.Billing;
import org.springframework.stereotype.Repository;

@Repository
public class BillingDao extends AbstractDao<Billing> {

	public BillingDao() {
		super(Billing.class);
	}

	@SuppressWarnings("unchecked")
    public List<Object[]> findBillings(Integer demoNo, List<String> serviceCodes) {    	
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder serviceCodeValues = new StringBuilder();
		for (int i = 0; i < serviceCodes.size(); i++) {
			if (serviceCodeValues.length() != 0) {
				serviceCodeValues.append(" OR ");
			}

			String param = "serviceCode" + (i + 1);
			serviceCodeValues.append("bd.serviceCode = :").append(param);
			params.put(param, serviceCodes.get(i));
		}

		StringBuilder buf = new StringBuilder("FROM Billing b, BillingDetail bd where b.demographicNo = :demoNo and bd.billingNo = b.id");
		params.put("demoNo", demoNo);

		if (serviceCodeValues.length() != 0) {
			buf.append(" AND ( ").append(serviceCodeValues).append(" ) ");
		}

		buf.append(" AND bd.status != :deletedFlag and b.status != :deletedFlag order by b.billingDate desc");
		params.put("deletedFlag", "D");
		
		Query query = entityManager.createQuery(buf.toString());
		query.setMaxResults(1);

		for (Entry<String, Object> e : params.entrySet()) {
			query.setParameter(e.getKey(), e.getValue());
		}		
		return query.getResultList();
	}

}
