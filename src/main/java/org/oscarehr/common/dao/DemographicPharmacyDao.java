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

package org.oscarehr.common.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.DemographicPharmacy;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class DemographicPharmacyDao extends AbstractDao<DemographicPharmacy> {

	public DemographicPharmacyDao() {
		super(DemographicPharmacy.class);
	}

	public DemographicPharmacy addPharmacyToDemographic(Integer pharmacyId, Integer demographicNo, Integer preferredOrder) {

		String sql = "select x from DemographicPharmacy x where x.status = ? and x.demographicNo = ? and x.pharmacyId = ?";
		Query query = entityManager.createQuery(sql);		
		query.setParameter(1, DemographicPharmacy.ACTIVE);
		query.setParameter(2, demographicNo);
		query.setParameter(3, pharmacyId);
		DemographicPharmacy demographicPharmacy = getSingleResultOrNull(query);
		int currentOrder;
		if( demographicPharmacy != null ) {
			
			int min,max;
			currentOrder = demographicPharmacy.getPreferredOrder();
			min = currentOrder > preferredOrder ? preferredOrder : currentOrder;
			max = currentOrder > preferredOrder ? currentOrder : preferredOrder;
			
			sql = "select x from DemographicPharmacy x where x.status = ? and x.demographicNo = ? and x.preferredOrder >= ? and x.preferredOrder < ?";
			query = entityManager.createQuery(sql);
			query.setParameter(1, DemographicPharmacy.ACTIVE);
			query.setParameter(2, demographicNo);
			query.setParameter(3, min);
			query.setParameter(4, max);			
		}
		else {
			sql = "select x from DemographicPharmacy x where x.status = ? and x.demographicNo = ? and x.preferredOrder >= ?";
			query = entityManager.createQuery(sql);
			query.setParameter(1, DemographicPharmacy.ACTIVE);
			query.setParameter(2, demographicNo);
			query.setParameter(3, preferredOrder);
		}
		 		
		@SuppressWarnings("unchecked")
		List<DemographicPharmacy> results = query.getResultList();
		
		for( DemographicPharmacy demographicPharmacy2 : results ) {
			currentOrder = demographicPharmacy2.getPreferredOrder();
			++currentOrder;
			if( currentOrder > 10 ) {
				demographicPharmacy2.setStatus(DemographicPharmacy.INACTIVE);
			}
			
			demographicPharmacy2.setPreferredOrder(currentOrder);
			merge(demographicPharmacy2);
		}
		
		
		if( demographicPharmacy == null ) {
		
			DemographicPharmacy dp = new DemographicPharmacy();
			dp.setAddDate(new Date());
			dp.setStatus(DemographicPharmacy.ACTIVE);
			dp.setDemographicNo(demographicNo);
			dp.setPharmacyId(pharmacyId);
			dp.setPreferredOrder(preferredOrder);
			persist(dp);
			return dp;
		}
		else {
			
			demographicPharmacy.setPreferredOrder(preferredOrder);
			merge(demographicPharmacy);
			return demographicPharmacy;
		}
		
		
	}
	
	public void unlinkPharmacy(Integer pharmacyId, Integer demographicNo ) {
		
		String sql = "select x from DemographicPharmacy x where x.status = ? and x.demographicNo = ? and x.pharmacyId = ?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, DemographicPharmacy.ACTIVE);
		query.setParameter(2, demographicNo);
		query.setParameter(3, pharmacyId);
		DemographicPharmacy demographicPharmacy = getSingleResultOrNull(query);
		
		if( demographicPharmacy != null ) {
			
			demographicPharmacy.setStatus("0");
		    merge(demographicPharmacy);
		    
			sql = "select x from DemographicPharmacy x where x.status = ? and x.demographicNo = ? and x.preferredOrder > ?";
			query = entityManager.createQuery(sql);
			query.setParameter(1, DemographicPharmacy.ACTIVE);
			query.setParameter(2, demographicNo);
			query.setParameter(3, demographicPharmacy.getPreferredOrder());
						
			@SuppressWarnings("unchecked")
            List<DemographicPharmacy> demographicPharmacies = query.getResultList();
			
			int preferredOrder;
			for( DemographicPharmacy demographicPharmacy2 : demographicPharmacies ) {
				preferredOrder = demographicPharmacy2.getPreferredOrder();
				--preferredOrder;				
				demographicPharmacy2.setPreferredOrder(preferredOrder);
				merge(demographicPharmacy2);
			}
			
		}
		else {
			MiscUtils.getLogger().error("UNKNOWN PHARMACY TO UNLINK");
		}
	}

	public List<DemographicPharmacy> findByDemographicId(Integer demographicNo) {
		String sql = "select x from DemographicPharmacy x where x.status=? and x.demographicNo=? order by x.preferredOrder";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, DemographicPharmacy.ACTIVE);
		query.setParameter(2, demographicNo);
		@SuppressWarnings("unchecked")
		List<DemographicPharmacy> results = query.getResultList();
		return results;
	}

	@SuppressWarnings("unchecked")
    public List<DemographicPharmacy> findAllByDemographicId(Integer demographicNo) {
		Query query = createQuery("dp", "dp.demographicNo = :demoNo AND dp.status = 1");
		query.setParameter("demoNo", demographicNo);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
    public List<DemographicPharmacy> findAllByPharmacyId(Integer pharmacyId) {
		
		String sql = "select x from DemographicPharmacy x where x.pharmacyId = ?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, pharmacyId);
		
		return query.getResultList();
		
	}
}
