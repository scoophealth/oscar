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

import org.oscarehr.common.model.MeasurementMap;
import org.oscarehr.common.model.MeasurementType;
import org.springframework.stereotype.Repository;

@Repository
public class MeasurementMapDao extends AbstractDao<MeasurementMap> {

	public MeasurementMapDao() {
		super(MeasurementMap.class);
	}
	
	public void addMeasurementMap(MeasurementMap measurementMap) {
		if(measurementMap.getId()==null)
			persist(measurementMap);
		else 
			merge(measurementMap);
	}

        
	public List<MeasurementMap> getAllMaps() {    
		String queryStr = "select m FROM MeasurementMap m";
		Query q = entityManager.createQuery(queryStr);
            
		@SuppressWarnings("unchecked")
		List<MeasurementMap> rs = q.getResultList();

		return rs;    
	}

       
	public List<MeasurementMap> getMapsByIdent(String identCode) {   
		String queryStr = "select m FROM MeasurementMap m WHERE m.identCode=? ORDER BY m.id";
		Query q = entityManager.createQuery(queryStr);
		q.setParameter(1, identCode);
            
		@SuppressWarnings("unchecked")    
		List<MeasurementMap> rs  = q.getResultList();

		return rs;    
	}
	
	public List<MeasurementMap> findByLoincCode(String loincCode) {   
		return getMapsByLoinc(loincCode);
	}

	public List<MeasurementMap> getMapsByLoinc(String loinc) {   
		String queryStr = "select m FROM MeasurementMap m WHERE m.loincCode=?";
		Query q = entityManager.createQuery(queryStr);
		q.setParameter(1, loinc);
            
		@SuppressWarnings("unchecked")    
		List<MeasurementMap> rs  = q.getResultList();

		return rs;    
	}
	
	public List<MeasurementMap> findByLoincCodeAndLabType(String loincCode,String labType) {   
		String queryStr = "select m FROM MeasurementMap m WHERE m.loincCode=? and m.labType=?";
		Query q = entityManager.createQuery(queryStr);
		q.setParameter(1, loincCode);
		q.setParameter(2, labType);
            
		@SuppressWarnings("unchecked")    
		List<MeasurementMap> rs  = q.getResultList();

		return rs;    
	}
	
	public List<String> findDistinctLabTypes() {    
		String queryStr = "select distinct(m.labType) FROM MeasurementMap m";
		Query q = entityManager.createQuery(queryStr);
            
		@SuppressWarnings("unchecked")
		List<String> rs = q.getResultList();

		return rs;    
	}
	
	public List<String> findDistinctLoincCodes() {    
		String queryStr = "select distinct(m.loincCode) FROM MeasurementMap m";
		Query q = entityManager.createQuery(queryStr);
            
		@SuppressWarnings("unchecked")
		List<String> rs = q.getResultList();

		return rs;    
	}

	/**
	 * Finds measurements for the specified lab type and ident code
	 * 
	 * @param labType
	 * 		Lab type to find
	 * @param idCode
	 * 		Ident code to find
	 * @return
	 * 		Returns a list of triples holding {@link MeasurementMap}, {@link MeasurementMap}, {@link MeasurementType}
	 */
	@SuppressWarnings("unchecked")
    public List<Object[]> findMeasurements(String labType, String idCode) {
		String sql = "FROM MeasurementMap a, MeasurementMap b, " + MeasurementType.class.getSimpleName() + " type " +
				"WHERE b.labType = :labType " +
				"AND a.identCode = :idCode " +
				"AND a.loincCode = b.loincCode " +
				"AND type.type = b.identCode";
		Query q = entityManager.createQuery(sql);
		q.setParameter("labType", labType);
		q.setParameter("idCode", idCode);
		return q.getResultList();
    }

}
