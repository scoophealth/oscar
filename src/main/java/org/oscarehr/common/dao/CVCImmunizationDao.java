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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.CVCImmunization;
import org.springframework.stereotype.Repository;

@Repository
public class CVCImmunizationDao extends AbstractDao<CVCImmunization> {

	public CVCImmunizationDao() {
		super(CVCImmunization.class);
	}

	public void removeAll() {
		Query query = entityManager.createQuery("DELETE FROM CVCImmunization");
		query.executeUpdate();
	}
	
	public List<CVCImmunization> findAllGeneric() {
		Query query = entityManager.createQuery("SELECT x FROM CVCImmunization x WHERE x.generic = :generic");
		query.setParameter("generic", true);
		List<CVCImmunization> result = query.getResultList();
		return result;
	}
	
	public List<CVCImmunization> findByParent(String conceptCodeId) {
		Query query = entityManager.createQuery("SELECT x FROM CVCImmunization x WHERE x.parentConceptId = :parentConceptId");
		query.setParameter("parentConceptId", conceptCodeId);
		List<CVCImmunization> result = query.getResultList();
		return result;
	}
	
	public CVCImmunization findBySnomedConceptId(String conceptCodeId) {
		Query query = entityManager.createQuery("SELECT x FROM CVCImmunization x WHERE x.snomedConceptId = :snomedConceptId");
		query.setParameter("snomedConceptId", conceptCodeId);
		query.setMaxResults(1);
		List<CVCImmunization> result = query.getResultList();
		if(!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}
	
	public List<CVCImmunization> query(String term, boolean includeGenerics, boolean includeBrands) {
		if(!includeBrands && !includeGenerics) {
			return new ArrayList<CVCImmunization>();
		}
		
		String segment = "";
		
		if(includeBrands && !includeGenerics) {
			segment = " AND generic=0 ";
		}
		if(!includeBrands && includeGenerics) {
			segment = " AND generic=1 ";
		}
		
		Query query = entityManager.createQuery("SELECT x FROM CVCImmunization x WHERE x.displayName like :term OR x.picklistName like :term" + segment);
		query.setParameter("term", "%" +  term  + "%");
		List<CVCImmunization> results = query.getResultList();
		return results;
	}
}
