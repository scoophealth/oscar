/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.common.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ProfessionalSpecialistDao extends AbstractDao<ProfessionalSpecialist> {

	public ProfessionalSpecialistDao() {
		super(ProfessionalSpecialist.class);
	}

	/**
	 * Sorted by lastname,firstname
	 */
	public List<ProfessionalSpecialist> findAll()
	{
		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x order by x.lastName,x.firstName");

		@SuppressWarnings("unchecked")
		List<ProfessionalSpecialist> results=query.getResultList();

		return(results);
	}

	/**
	 * Sorted by lastname,firstname
	 */
	public List<ProfessionalSpecialist> findByEDataUrlNotNull()
	{
		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x where x.eDataUrl is not null order by x.lastName,x.firstName");

		@SuppressWarnings("unchecked")
		List<ProfessionalSpecialist> results=query.getResultList();

		return(results);
	}

	public List<ProfessionalSpecialist> findByFullName(String lastName, String firstName) {
		Query query = entityManager.createQuery("select x from " + modelClass.getName() + " x WHERE x.lastName like ? and x.firstName like ? order by x.lastName");
		query.setParameter(1, "%"+lastName+"%");
		query.setParameter(2, "%"+firstName+"%");

		@SuppressWarnings("unchecked")
		List<ProfessionalSpecialist> cList = query.getResultList();

		if (cList != null && cList.size() > 0) {
			return cList;
		}

		return null;
	}

	public List<ProfessionalSpecialist> findByLastName(String lastName) {
		return findByFullName(lastName, "");
	}


	public List<ProfessionalSpecialist> findBySpecialty(String specialty) {
		Query query = entityManager.createQuery("select x from " + modelClass.getName() + " x WHERE x.specialtyType like ? order by x.lastName");
		query.setParameter(1, "%"+specialty+"%");

		@SuppressWarnings("unchecked")
		List<ProfessionalSpecialist> cList = query.getResultList();

		if (cList != null && cList.size() > 0) {
			return cList;
		}

		return null;

	}

	public List<ProfessionalSpecialist> findByReferralNo(String referralNo) {
		if (StringUtils.isBlank(referralNo)) {
			return null;
		}
		Query query = entityManager.createQuery("select x from " + modelClass.getName() + " x WHERE x.referralNo=? order by x.lastName");
		query.setParameter(1, referralNo);

		@SuppressWarnings("unchecked")
		List<ProfessionalSpecialist> cList = query.getResultList();

		if (cList != null && cList.size() > 0) {
			return cList;
		}

		return null;

	}

	public ProfessionalSpecialist getByReferralNo(String referralNo) {
		List<ProfessionalSpecialist> cList = findByReferralNo(referralNo);

		if (cList != null && cList.size() > 0) {
			return cList.get(0);
		}

		return null;

	}

	public boolean hasRemoteCapableProfessionalSpecialists()
	{
		return(findByEDataUrlNotNull().size()>0);
	}
	

	public List<ProfessionalSpecialist> search(String keyword) {
		StringBuilder where = new StringBuilder();
		List<String> paramList = new ArrayList<String>();
		
		String searchMode = "search_name";
		String orderBy = "c.lastName,c.firstName";
	    
		if(searchMode.equals("search_name")) {
			String[] temp = keyword.split("\\,\\p{Space}*");
			if(temp.length>1) {
		      where.append("c.lastName like ?1 and c.firstName like ?2");
		      paramList.add(temp[0].trim()+"%");
		      paramList.add(temp[1].trim()+"%");
		    } else {
		      where.append("c.lastName like ?1");
		      paramList.add(temp[0].trim()+"%");
		    }
		}		
		String sql = "SELECT c from ProfessionalSpecialist c where " + where.toString() + " order by " + orderBy;
		MiscUtils.getLogger().info(sql);
		Query query = entityManager.createQuery(sql);
		for(int x=0;x<paramList.size();x++) {
			query.setParameter(x+1,paramList.get(x));
		}		
		
		@SuppressWarnings("unchecked")
		List<ProfessionalSpecialist> contacts = query.getResultList();
		return contacts;
	}
	
	public List<ProfessionalSpecialist> findByFullNameAndSpecialtyAndAddress(String lastName, String firstName, String specialty, String address, Boolean showHidden) {
		String sql = "select x from " + modelClass.getName() + " x WHERE (x.lastName like ? and x.firstName like ?) ";
		
		if(!StringUtils.isEmpty(specialty)) {
			sql += " AND x.specialtyType LIKE ? ";
		}
		
		if(!StringUtils.isEmpty(address)) {
			sql += " AND x.streetAddress LIKE ? ";
		}
		
		if(showHidden == null || !showHidden) {
			sql += " AND x.hideFromView=false ";
		}
		sql += " order by x.lastName";
		
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, "%"+lastName+"%");
		query.setParameter(2, "%"+firstName+"%");

		int index=3;
		if(!StringUtils.isEmpty(specialty)) {
			query.setParameter(index++, "%" + specialty +"%");
		}
		if(!StringUtils.isEmpty(address)) {
			query.setParameter(index++, "%" + address +"%");
		}
		
		@SuppressWarnings("unchecked")
		List<ProfessionalSpecialist> cList = query.getResultList();

		return cList;
	}
	
	public List<ProfessionalSpecialist> findByService(String serviceName) {
		Query query = entityManager.createQuery("select x from " + modelClass.getName() + " x, ConsultationServices cs, ServiceSpecialists ss WHERE x.id = ss.id.specId and ss.id.serviceId = cs.serviceId and cs.serviceDesc = ?");
		query.setParameter(1, serviceName);

		@SuppressWarnings("unchecked")
		List<ProfessionalSpecialist> cList = query.getResultList();

		
		return cList;
	}
	
	public List<ProfessionalSpecialist> findByServiceId(Integer serviceId) {
		Query query = entityManager.createQuery("select x from " + modelClass.getName() + " x, ServiceSpecialists ss WHERE x.id = ss.id.specId and ss.id.serviceId = ?");
		query.setParameter(1, serviceId);

		@SuppressWarnings("unchecked")
		List<ProfessionalSpecialist> cList = query.getResultList();

		
		return cList;
	}
	
	
}
