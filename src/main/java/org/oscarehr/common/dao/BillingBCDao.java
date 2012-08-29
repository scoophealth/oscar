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

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.NativeSql;
import org.springframework.stereotype.Repository;

/**
 * Billing DAO containing BC-specific extensions.
 *
 */
@Repository
public class BillingBCDao extends BillingDao {

	/**
	 * Selects service code, description, value and percentage from ctl_bilingservice and billingservice tables.
	 */
	@NativeSql({"ctl_billingservice", "billingservice"})
	@SuppressWarnings("unchecked")
	public List<Object[]> findBillingServices(String billRegion, String serviceGroup, String serviceType) {
		 Query query = entityManager.createNativeQuery("SELECT DISTINCT b.service_code, b.description, b.value, b.percentage "
	          + "FROM ctl_billingservice c left outer join billingservice b on b.service_code="
	          + "c.service_code where b.region = ? and c.service_group= ? and c.servicetype = ? order by c.service_order");
	          
	     query.setParameter(1, billRegion);
	     query.setParameter(2, serviceGroup);
	     query.setParameter(3, serviceType);
	     
	     return query.getResultList();
    }


	/**
	 * Selects service code, description, value and percentage from ctl_bilingservice and billingservice tables.
	 */
	@NativeSql({"ctl_billingservice", "billingservice"})
	@SuppressWarnings("unchecked")
	public List<Object[]> findBillingServices(String billRegion, String serviceGroup, String serviceType, String billReferenceDate) {
	      Query query = entityManager.createNativeQuery(
	              "SELECT DISTINCT b.service_code, b.description , b.value, b.percentage " +
	              "FROM ctl_billingservice c left outer join billingservice b on b.service_code="
	              + "c.service_code where b.region = ? and c.service_group = ? and c.servicetype = ?" +
	              " and b.billingservice_date in (select max(b2.billingservice_date) from billingservice b2 where b2.billingservice_date <= ? and b2.service_code = b.service_code) order by c.service_order");
		
	    query.setParameter(1, billRegion);
		query.setParameter(2, serviceGroup);
		query.setParameter(3, serviceType);
		query.setParameter(4, billReferenceDate);

	    return query.getResultList();
    }

	/**
	 * Selects billing location and description from the billinglocation table where region matches the one provided
	 */
	@SuppressWarnings("unchecked")
    @NativeSql("billinglocation")
	public List<Object[]> findBillingLocations(String billRegion) {
	      Query query = entityManager.createNativeQuery("SELECT billinglocation,billinglocation_desc FROM billinglocation WHERE region = ?");
	      query.setParameter(1, billRegion);
	      return query.getResultList();
	    
    }


	/**
	 * Selects visit type and description from billingvisit table for the specified region
	 */
	@SuppressWarnings("unchecked")
    @NativeSql("billingvisit")
	public List<Object[]> findBillingVisits(String billRegion) {
	    Query query = entityManager.createNativeQuery("SELECT visittype, visit_desc FROM billingvisit WHERE region = ?");
	    query.setParameter(1, billRegion);
	    return query.getResultList();
    }

	/**
	 * Gets all side types and descriptions from wcb_side table. 
	 */
	@SuppressWarnings("unchecked")
    @NativeSql("wcb_side")
	public List<Object[]> findInjuryLocations() {
	    Query query = entityManager.createNativeQuery("SELECT sidetype, sidedesc FROM wcb_side");
	    return query.getResultList();
	}
	
}
