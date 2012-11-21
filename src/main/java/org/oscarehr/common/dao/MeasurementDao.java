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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.oscarehr.common.model.Measurement;
import org.springframework.stereotype.Repository;

@Repository
public class MeasurementDao extends AbstractDao<Measurement> {

	public MeasurementDao() {
		super(Measurement.class);
	}

	public List<Measurement> findByDemographicIdUpdatedAfterDate(Integer demographicId, Date updatedAfterThisDate) {

		// using create date since this object is not updateable
		String sqlCommand = "select x from Measurement x where x.demographicId=?1 and x.createDate>?2";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);
		query.setParameter(2, updatedAfterThisDate);

		@SuppressWarnings("unchecked")
		List<Measurement> results = query.getResultList();

		return (results);
	}

	public List<Measurement> findMatching(Measurement measurement) {

		String sqlCommand = "select x from Measurement x where x.demographicId=?1 and x.dataField=?2 and x.measuringInstruction=?3 and x.comments=?4 and x.dateObserved=?5 and x.type=?6";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, measurement.getDemographicId());
		query.setParameter(2, measurement.getDataField());
		query.setParameter(3, measurement.getMeasuringInstruction());
		query.setParameter(4, measurement.getComments());
		query.setParameter(5, measurement.getDateObserved());
		query.setParameter(6, measurement.getType());

		@SuppressWarnings("unchecked")
		List<Measurement> results = query.getResultList();

		return results;
	}

	public List<Measurement> findByType(Integer demographicId, String type) {
		String sqlCommand = "select x from Measurement x where x.demographicId = ?1 and x.type = ?2 order by x.dateObserved desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);
		query.setParameter(2, type);

		@SuppressWarnings("unchecked")
		List<Measurement> results = query.getResultList();

		return results;
	}
	
	public List<Measurement> findByDemographicNo(Integer demographicNo) {
		String sqlCommand = "select x from Measurement x where x.demographicId = ?1";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicNo);
		
		@SuppressWarnings("unchecked")
		List<Measurement> results = query.getResultList();

		return results;
	}
	
	public List<Measurement> findByDemographicNoAndType(Integer demographicNo, String type) {
		String sqlCommand = "select x from Measurement x where x.demographicId = ?1 and x.type=?2";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicNo);
		query.setParameter(2, type);
		
		@SuppressWarnings("unchecked")
		List<Measurement> results = query.getResultList();

		return results;
	}
	
	public Measurement findLatestByDemographicNoAndType(int demographicNo, String type) {
		List<Measurement> ms = findByDemographicNoAndType(demographicNo,type);
		if(ms.size()==0)
			return null;
		Collections.sort(ms, Measurement.DateObservedComparator);
		return ms.get(ms.size()-1);
		
	}
	
	
	public List<Measurement> findByAppointmentNo(Integer appointmentNo) {
		String sqlCommand = "select x from Measurement x where x.appointmentNo = ?1";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, appointmentNo);
		
		@SuppressWarnings("unchecked")
		List<Measurement> results = query.getResultList();

		return results;
	}
	
	public List<Measurement> findByAppointmentNoAndType(Integer appointmentNo, String type) {
		String sqlCommand = "select x from Measurement x where x.appointmentNo = ?1 and x.type = ?2";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, appointmentNo);
		query.setParameter(2, type);
		
		@SuppressWarnings("unchecked")
		List<Measurement> results = query.getResultList();

		return results;
	}
	
	public Measurement findLatestByAppointmentNoAndType(int appointmentNo, String type) {
		List<Measurement> ms = findByAppointmentNoAndType(appointmentNo,type);
		if(ms.size()==0)
			return null;
		Collections.sort(ms, Measurement.DateObservedComparator);
		return ms.get(ms.size()-1);
		
	}
	

	public List<Measurement> findByDemographicIdObservedDate(Integer demographicId, Date startDate, Date endDate) {
		String sqlCommand = "select x from Measurement x where x.demographicId=? and x.type!='' and x.dateObserved >? and x.dateObserved <? order by x.dateObserved desc";
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);
		query.setParameter(2, startDate);
		query.setParameter(3, endDate);

		@SuppressWarnings("unchecked")
		List<Measurement> results = query.getResultList();

		return (results);
	}

	public List<Measurement> findByDemographicId(Integer demographicId) {
		String sqlCommand = "select x from Measurement x where x.demographicId=? and x.type!='' order by x.dateObserved desc";
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);

		@SuppressWarnings("unchecked")
		List<Measurement> results = query.getResultList();

		return (results);
	}

	/**
	 * Finds be
	 * 
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Measurement> find(SearchCriteria criteria) {
		Query query = entityManager.createQuery("select m FROM Measurement m WHERE m.demographicId = :demographicNo " + "AND m.type= :type " + "AND m.dataField = :dataField " + "AND m.measuringInstruction = :measuringInstrc " + "AND m.comments = :comments " + "AND m.dateObserved = :dateObserved");
		query.setParameter("demographicNo", criteria.getDemographicNo());
		query.setParameter("type", criteria.getType());
		query.setParameter("dataField", criteria.getDataField());
		query.setParameter("measuringInstrc", criteria.getMeasuringInstrc());
		query.setParameter("comments", criteria.getComments());
		query.setParameter("dateObserved", criteria.getDateObserved());
		return query.getResultList();
	}

	/**
	 * Criteria for measurement search.
	 */
	public static class SearchCriteria {

		private Integer demographicNo;
		private String type;
		private String dataField;
		private String measuringInstrc;
		private String comments;
		private Date dateObserved;

		public Integer getDemographicNo() {
			return demographicNo;
		}

		public void setDemographicNo(String demographicNo) {
			setDemographicNo(Integer.parseInt(demographicNo));
		}

		public void setDemographicNo(Integer demographicNo) {
			this.demographicNo = demographicNo;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDataField() {
			return dataField;
		}

		public void setDataField(String dataField) {
			this.dataField = dataField;
		}

		public String getMeasuringInstrc() {
			return measuringInstrc;
		}

		public void setMeasuringInstrc(String measuringInstrc) {
			this.measuringInstrc = measuringInstrc;
		}

		public String getComments() {
			return comments;
		}

		public void setComments(String comments) {
			this.comments = comments;
		}

		public Date getDateObserved() {
			return dateObserved;
		}

		public void setDateObserved(Date dateObserved) {
			this.dateObserved = dateObserved;
		}
	}

	/**
	 * Looks up measurement information based on the demographic id, type and instructions.
	 * 
	 * @param demographicId
	 * 		ID of the demographic record
	 * @param type
	 * 		Type of the measurement
	 * @param instructions
	 * 		Measurement instructions
	 * @return
	 * 		Returns the measurements found
	 */
	@SuppressWarnings("unchecked")
    public List<Measurement> findByIdTypeAndInstruction(Integer demographicId, String type, String instructions) {
		Query query = entityManager.createQuery("FROM " + modelClass.getSimpleName() + " m WHERE m.demographicId = :demographicNo " + "AND m.type = :type " + "AND m.measuringInstruction = :measuringInstruction ORDER BY m.createDate DESC");
		query.setParameter("demographicNo", demographicId);
		query.setParameter("type", type);
		query.setParameter("measuringInstruction", instructions);
		query.setMaxResults(1);
		return query.getResultList();
	}
	
	
	public HashMap<String, Measurement> getMeasurements(Integer demographicNo, String[] types) {
		HashMap<String, Measurement> map = new HashMap<String, Measurement>();
		String queryStr = "select m from Measurement m WHERE m.demographicId = :demographicNo AND m.type IN (:types) ORDER BY m.type,m.dateObserved";
		Query query = entityManager.createQuery(queryStr);
		query.setParameter("demographicId", demographicNo);
		List<String> lst = new ArrayList<String>();
		for(int x=0;x<types.length;x++) {
			lst.add(types[x]);
		}
		query.setParameter("types", lst);
		
		
		@SuppressWarnings("unchecked")
		List<Measurement> results = query.getResultList();

		for (Measurement m : results) {
			map.put(m.getType(), m);
		}
		return map;
	}
	
	public Set<Integer> getAppointmentNosByDemographicNoAndType(int demographicNo, String type, Date startDate, Date endDate) {
		Map<Integer, Boolean> results = new HashMap<Integer, Boolean>();

		String queryStr = "select m from  Measurement m WHERE m.demographicId = " + demographicNo + " and m.type=? and m.dateObserved>=? and m.dateObserved<=? ORDER BY m.dateObserved DESC";
		Query query = entityManager.createQuery(queryStr);
		query.setParameter(1, demographicNo);
		query.setParameter(2, type);
		query.setParameter(3, startDate);
		query.setParameter(4, endDate);
		
		@SuppressWarnings("unchecked")
		List<Measurement> rs = query.getResultList();
		for (Measurement m : rs) {
			results.put(m.getAppointmentNo(), true);
		}

		return results.keySet();
	}
	
	public HashMap<String, Measurement> getMeasurementsPriorToDate(String demographicNo, Date d) {
		String queryStr = "select m From Measurement m WHERE m.demographicId = " + demographicNo + " AND m.dateObserved <= ?";
		Query query = entityManager.createQuery(queryStr);
		query.setParameter(1, demographicNo);
		query.setParameter(2, d);
		
		@SuppressWarnings("unchecked")
    	List<Measurement> rs = query.getResultList();
		
    	HashMap<String,Measurement> map = new HashMap<String,Measurement>();

    	for(Measurement m:rs) {
    		map.put(m.getType(), m);
    	}

    	return map;
	}
	
	public List<Date> getDatesForMeasurements(String demographicNo, String[] types) {
		List<String> lst = new ArrayList<String>();
		
    	for(String type:types) {
    		lst.add(type);
    	}

		String queryStr = "SELECT DISTINCT m.dateObserved FROM Measurement m WHERE m.demographicId = :demographicNo AND m.type IN (:types) ORDER BY m.dateObserved DESC";
		
		Query query = entityManager.createQuery(queryStr);
		query.setParameter("demographicNo",demographicNo);
		query.setParameter("types", lst);
		
		@SuppressWarnings("unchecked")
		List<Date> results = query.getResultList();

		return results;
	}
	
	/**
	 * Finds abnormal measurements for the specified patient
	 * 
	 * @param demoNo
	 * 		Patient ID
	 * @param loincCode
	 * 		LOINC Code
	 * @return
	 * 		Returns a list of tuples containing record data, observation date, lab no, abnormal value.
	 */
	@SuppressWarnings("unchecked")
    public List<Object[]> findMeasurementsByDemographicIdAndLocationCode(Integer demoNo, String loincCode) {
		String sql = "SELECT m.dataField, m.dateObserved, e1.val, e3.val " +
				"FROM Measurement m, MeasurementsExt e1, MeasurementsExt e2, MeasurementsExt e3, MeasurementMap mm " +
				"WHERE m.id = e1.measurementId " +
				"AND e1.keyVal = 'lab_no' " +
				"AND m.id = e2.measurementId " +
				"AND e2.keyVal = 'identifier' " +
				"AND m.id = e3.measurementId " +
				"AND e3.keyVal = 'abnormal' " +
				"AND e2.val = mm.identCode " +
				"AND mm.loincCode = :loincCode " +
				"AND m.demographicId = :demoNo " +
				"ORDER BY m.dateObserved DESC";
		Query query = entityManager.createQuery(sql);
		query.setParameter("demoNo", demoNo);
		query.setParameter("loincCode", loincCode);
		return query.getResultList();	    
    }
    
	@SuppressWarnings("unchecked")
    public List<Object[]> findMeasurementsWithIdentifiersByDemographicIdAndLocationCode(Integer demoNo, String loincCode) {
		String sql = "SELECT m.dataField, m.dateObserved, e1.val, e3.val, e4.val " + 
				"FROM Measurement m, MeasurementsExt e1, MeasurementsExt e2, MeasurementsExt e3, MeasurementsExt e4, MeasurementMap mm " +
				"WHERE m.id = e1.measurementId " +
				"AND e1.keyVal = 'lab_no' " +
				"AND m.id = e2.measurementId " +
				"AND e2.keyVal='identifier'" +
				"AND m.id = e4.measurementId " +
				"AND e4.keyVal='identifier' " +
				"AND m.id = e3.measurementId " +
				"AND e3.keyVal='abnormal' " + 
				"AND e2.val = mm.identCode " +
				"AND mm.loincCode = :loincCode " +
				"AND m.demographicId = :demoNo " +
				"ORDER BY m.dateObserved DESC";
		
		Query query = entityManager.createQuery(sql);
		query.setParameter("demoNo", demoNo);
		query.setParameter("loincCode", loincCode);
		return query.getResultList();
	    
    }

	@SuppressWarnings("unchecked")
    public List<Object> findLabNumbers(Integer demoNo, String identCode) {
		String sql = "SELECT DISTINCT e2.val FROM Measurement m, MeasurementsExt e1, MeasurementsExt e2 " +
				"WHERE m.id = e1.measurementId " +
				"AND e1.keyVal = 'identifier' " +
				"AND m.id = e2.measurementId " +
				"AND e2.keyVal = 'lab_no' " +
				"AND e1.val= :identCode " +
				"AND m.demographicId = :demoNo";
		Query query = entityManager.createQuery(sql);
		query.setParameter("identCode", identCode);
		query.setParameter("demoNo", demoNo);
		return query.getResultList();
    }

	public Measurement findLastEntered(Integer demo, String type) {
		Query query = createQuery("ms", "ms.demographicId = :demoNo AND ms.type = :type ORDER BY ms.createDate DESC");
		query.setParameter("demoNo", demo);
		query.setParameter("type", type);
		return getSingleResultOrNull(query);
	}
	
}
