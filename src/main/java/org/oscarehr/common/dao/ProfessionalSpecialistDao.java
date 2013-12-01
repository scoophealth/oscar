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

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.model.ProfessionalSpecialist;
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
}
