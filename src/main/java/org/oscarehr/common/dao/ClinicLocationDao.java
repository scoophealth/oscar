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

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.ClinicLocation;
import org.springframework.stereotype.Repository;

@Repository
public class ClinicLocationDao extends AbstractDao<ClinicLocation> {

	public ClinicLocationDao() {
		super(ClinicLocation.class);
	}
	
	   @SuppressWarnings("unchecked")
		public List<ClinicLocation> findAll() {
			Query query = entityManager.createQuery("SELECT x FROM " + modelClass.getSimpleName() + " x");
			List<ClinicLocation> results = query.getResultList();
			return results;
		}

	public List<ClinicLocation> findByClinicNo(Integer clinicNo) {
	   	String sql = "select c from ClinicLocation c where c.clinicNo=?1 order by c.clinicLocationNo";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,clinicNo);

        @SuppressWarnings("unchecked")
        List<ClinicLocation> results = query.getResultList();

        return results;
	}

	public String searchVisitLocation(String clinicLocationNo) {
		String sql = "select c from ClinicLocation c where c.clinicLocationNo=?1";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,clinicLocationNo);

        @SuppressWarnings("unchecked")
        List<ClinicLocation> results = query.getResultList();
        if(!results.isEmpty()) {
        	return results.get(0).getClinicLocationName();
        }
        return null;
	}

	public ClinicLocation searchBillLocation(Integer clinicNo, String clinicLocationNo) {
		String sql = "select c from ClinicLocation c where c.clinicNo=?1 and c.clinicLocationNo=?2";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,clinicNo);
    	query.setParameter(2,clinicLocationNo);

        return this.getSingleResultOrNull(query);

	}

	/*
	 * @deprecated
	 */
	public void removeByClinicLocationNo(String clinicLocationNo) {
		String sql = "select c from ClinicLocation c where c.clinicLocationNo=?1";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,clinicLocationNo);

    	 @SuppressWarnings("unchecked")
         List<ClinicLocation> results = query.getResultList();
    	 for(ClinicLocation clinicLocation:results) {
    		 this.remove(clinicLocation);
    	 }

	}
}
