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
import org.springframework.stereotype.Repository;

@Repository
public class DemographicPharmacyDao extends AbstractDao<DemographicPharmacy>{

	public DemographicPharmacyDao() {
		super(DemographicPharmacy.class);
	}

	   public void addPharmacyToDemographic(String pharmacyId,String demographicNo){
		   DemographicPharmacy dp = new DemographicPharmacy();
		   dp.setAddDate(new Date());
		   dp.setStatus("1");
		   dp.setDemographicNo(Integer.parseInt(demographicNo));
		   dp.setPharmacyId(Integer.parseInt(pharmacyId));
		   persist(dp);
	   }

	   public DemographicPharmacy findByDemographicId(String demographicNo){
		      DemographicPharmacy record = null;
		      String sql = "select x from DemographicPharmacy x where x.status=? and x.demographicNo=? order by x.addDate desc";
		      Query query = entityManager.createQuery(sql);
		      query.setParameter(1,"1");
		      query.setParameter(2,Integer.parseInt(demographicNo));
		      @SuppressWarnings("unchecked")
		      List<DemographicPharmacy> results = query.getResultList();
		      if(results.size()>0) {
		    	  return results.get(0);
		      }
		      return record;
	   }
}
