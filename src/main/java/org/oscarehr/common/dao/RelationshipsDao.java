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

import org.oscarehr.common.model.Relationships;
import org.springframework.stereotype.Repository;

import oscar.util.ConversionUtils;

@Repository
public class RelationshipsDao extends AbstractDao<Relationships> {

	public RelationshipsDao() {
		super(Relationships.class);
	}

	//find all of them - for migration script
	public List<Relationships> findAll() {
		String sql = "select x from Relationships x order by x.demographicNo";
		Query query = entityManager.createQuery(sql);
		@SuppressWarnings("unchecked")
		List<Relationships> results = query.getResultList();
		return results;
	}

	/**
	 * Finds active relationship with the specified ID
	 * 
	 * @param id
	 * 		ID of the relationship to be loaded 
	 * @return
	 * 		Returns the non-deleted rel'p with the specified ID or null if it can't be found
	 */
	public Relationships findActive(Integer id) {
		// TODO replace by getBaseQuery(), when becomes available
		Query query = entityManager.createQuery("FROM " + modelClass.getSimpleName() + " r WHERE r.id = :id AND (r.deleted IS NULL OR r.deleted = '0')");
		query.setParameter("id", id);
		// query.setParameter("deleted", ConversionUtils.toBoolString(Boolean.TRUE));
		return getSingleResultOrNull(query);
	}

	/**
	 * Finds all active relationships for the specified demographic ID
	 * 
	 * @param demographicNumber
	 * 		demographic id
	 * @return
	 * 		Returns the non-deleted rel'p with the specified ID or null if it can't be found
	 */
	@SuppressWarnings("unchecked")
	public List<Relationships> findByDemographicNumber(Integer demographicNumber) {
		// TODO replace by getBaseQuery(), when becomes available
		Query query = entityManager.createQuery("FROM " + modelClass.getSimpleName() + " r WHERE r.demographicNo = :dN AND (r.deleted IS NULL OR r.deleted = '0')");
		query.setParameter("dN", demographicNumber);
		return query.getResultList();
	}

	/**
	 * Finds all active relationships that are marked as sub decision maker.
	 * 
	 * @param demographicNumber
	 * 		Demographic ID to find the relationships for
	 * @return
	 * 		Returns the non-deleted relationships with the sub decision maker flag set to true for the specified demographic ID   
	 */
	@SuppressWarnings("unchecked")
	public List<Relationships> findActiveSubDecisionMaker(Integer demographicNumber) {
		Query query = entityManager.createQuery("FROM " + modelClass.getSimpleName() + " r WHERE r.demographicNo = :dN AND r.subDecisionMaker = :sdm AND (r.deleted IS NULL OR r.deleted = '0')");
		query.setParameter("dN", demographicNumber);
		query.setParameter("sdm", ConversionUtils.toBoolString(Boolean.TRUE));
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
    public List<Relationships> findActiveByDemographicNumberAndFacility(Integer demographicNumber, Integer facilityId) {
		Query query = entityManager.createQuery("FROM " + modelClass.getSimpleName() + " r WHERE r.demographicNo = :dN AND r.facilityId = :facilityId AND (r.deleted IS NULL OR r.deleted = '0')");
		query.setParameter("dN", demographicNumber);
		query.setParameter("facilityId", facilityId);
		return query.getResultList();
	}
}
