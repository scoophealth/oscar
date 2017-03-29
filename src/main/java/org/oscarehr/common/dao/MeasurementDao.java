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
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.Query;

import org.oscarehr.common.NativeSql;
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

		List<Measurement> results = query.getResultList();

		return (results);
	}
	
	/**
	 * @return results ordered by createDate
	 */
	public List<Measurement> findByCreateDate(Date updatedAfterThisDateExclusive, int itemsToReturn) {
		String sqlCommand = "select x from "+modelClass.getSimpleName()+" x where x.createDate>?1 order by x.createDate";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, updatedAfterThisDateExclusive);
		setLimit(query, itemsToReturn);
		
		@SuppressWarnings("unchecked")
		List<Measurement> results = query.getResultList();
		return (results);
	}

	
	//for integrator
	public List<Integer> findDemographicIdsUpdatedAfterDate(Date updatedAfterThisDate) {
		
		String sqlCommand = "select x.demographicId from Measurement x where x.createDate>?1";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, updatedAfterThisDate);

		@SuppressWarnings("unchecked")
		List<Integer> results = query.getResultList();

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

		List<Measurement> results = query.getResultList();

		return results;
	}

	public List<Measurement> findByDemographicNo(Integer demographicNo) {
		String sqlCommand = "select x from Measurement x where x.demographicId = ?1";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicNo);

		List<Measurement> results = query.getResultList();

		return results;
	}

	public List<Measurement> findByDemographicNoAndType(Integer demographicNo, String type) {
		String sqlCommand = "select x from Measurement x where x.demographicId = ?1 and x.type=?2";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicNo);
		query.setParameter(2, type);

		List<Measurement> results = query.getResultList();

		return results;
	}

	public Measurement findLatestByDemographicNoAndType(int demographicNo, String type) {
		List<Measurement> ms = findByDemographicNoAndType(demographicNo, type);
		if (ms.size() == 0) return null;
		Collections.sort(ms, Measurement.DateObservedComparator);
		return ms.get(ms.size() - 1);

	}

	public List<Measurement> findByAppointmentNo(Integer appointmentNo) {
		String sqlCommand = "select x from Measurement x where x.appointmentNo = ?1";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, appointmentNo);

		List<Measurement> results = query.getResultList();

		return results;
	}

	public List<Measurement> findByAppointmentNoAndType(Integer appointmentNo, String type) {
		String sqlCommand = "select x from Measurement x where x.appointmentNo = ?1 and x.type = ?2";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, appointmentNo);
		query.setParameter(2, type);

		List<Measurement> results = query.getResultList();

		return results;
	}

	public Measurement findLatestByAppointmentNoAndType(int appointmentNo, String type) {
		List<Measurement> ms = findByAppointmentNoAndType(appointmentNo, type);
		if (ms.size() == 0) return null;
		Collections.sort(ms, Measurement.DateObservedComparator);
		return ms.get(ms.size() - 1);

	}

	public List<Measurement> findByDemographicIdObservedDate(Integer demographicId, Date startDate, Date endDate) {
		String sqlCommand = "select x from Measurement x where x.demographicId=? and x.type!='' and x.dateObserved >? and x.dateObserved <? order by x.dateObserved desc, x.createDate desc";
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);
		query.setParameter(2, startDate);
		query.setParameter(3, endDate);

		List<Measurement> results = query.getResultList();

		return (results);
	}

	public List<Measurement> findByDemographicId(Integer demographicId) {
		String sqlCommand = "select x from Measurement x where x.demographicId=? and x.type!='' order by x.dateObserved desc";
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);

		List<Measurement> results = query.getResultList();

		return (results);
	}

	/**
	 * Finds be
	 * 
	 * @param criteria
	 * @return list of measurements
	 */

	public List<Measurement> find(SearchCriteria criteria) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder buf = new StringBuilder();

		for (Object[] obj : new Object[][] { { "m.demographicId = :demographicNo", "demographicNo", criteria.getDemographicNo() }, { "m.type= :type", "type", criteria.getType() }, { "m.dataField = :dataField", "dataField", criteria.getDataField() }, { "m.measuringInstruction = :measuringInstrc", "measuringInstrc", criteria.getMeasuringInstrc() }, { "m.comments = :comments", "comments", criteria.getComments() }, { "m.dateObserved = :dateObserved", "dateObserved", criteria.getDateObserved() } }) {

			String queryClause = (String) obj[0];
			String paramName = (String) obj[1];
			Object paramValue = obj[2];

			if (paramValue == null) {
				continue;
			}

			if (buf.length() != 0) {
				buf.append("AND ");
			}

			buf.append(queryClause).append(" ");
			params.put(paramName, paramValue);
		}

		// make sure empty sc still results in a well-formed query
		if (buf.length() > 0) {
			buf.insert(0, " WHERE ");
		}
		buf.insert(0, "select m FROM Measurement m");

		Query query = entityManager.createQuery(buf.toString());
		for (Entry<String, Object> param : params.entrySet()) {
			query.setParameter(param.getKey(), param.getValue());
		}
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

		public SearchCriteria() {
		}

		public SearchCriteria(Integer demographicNo, String type, String dataField, String measuringInstrc, String comments, Date dateObserved) {
			super();
			this.demographicNo = demographicNo;
			this.type = type;
			this.dataField = dataField;
			this.measuringInstrc = measuringInstrc;
			this.comments = comments;
			this.dateObserved = dateObserved;
		}

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
		query.setParameter("demographicNo", demographicNo);
		List<String> lst = new ArrayList<String>();
		for (int x = 0; x < types.length; x++) {
			lst.add(types[x]);
		}
		query.setParameter("types", lst);

		List<Measurement> results = query.getResultList();

		for (Measurement m : results) {
			map.put(m.getType(), m);
		}
		return map;
	}	
	
	public List<Measurement> getMeasurementsByAppointment2(int appointmentNo) {
		String queryStr = "From Measurement m WHERE m.appointmentNo = ? ORDER BY m.type, m.dateObserved ASC";
		Query query = entityManager.createQuery(queryStr);
		query.setParameter(1, appointmentNo);
		return query.getResultList();
	}
	

	public Set<Integer> getAppointmentNosByDemographicNoAndType(int demographicNo, String type, Date startDate, Date endDate) {
		Map<Integer, Boolean> results = new HashMap<Integer, Boolean>();

		String queryStr = "select m from  Measurement m WHERE m.demographicId = ? and m.type=? and m.dateObserved>=? and m.dateObserved<=? ORDER BY m.dateObserved DESC";
		Query query = entityManager.createQuery(queryStr);
		query.setParameter(1, demographicNo);
		query.setParameter(2, type);
		query.setParameter(3, startDate);
		query.setParameter(4, endDate);

		List<Measurement> rs = query.getResultList();
		for (Measurement m : rs) {
			results.put(m.getAppointmentNo(), true);
		}

		return results.keySet();
	}

	public HashMap<String, Measurement> getMeasurementsPriorToDate(Integer demographicNo, Date d) {
		String queryStr = "select m From Measurement m WHERE m.demographicId = ? AND m.dateObserved <= ?";
		Query query = entityManager.createQuery(queryStr);
		query.setParameter(1, demographicNo);
		query.setParameter(2, d);

		List<Measurement> rs = query.getResultList();

		HashMap<String, Measurement> map = new HashMap<String, Measurement>();

		for (Measurement m : rs) {
			map.put(m.getType(), m);
		}

		return map;
	}

	public List<Date> getDatesForMeasurements(Integer demographicNo, String[] types) {
		List<String> lst = new ArrayList<String>();

		for (String type : types) {
			lst.add(type);
		}

		String queryStr = "SELECT DISTINCT m.dateObserved FROM Measurement m WHERE m.demographicId = :demographicNo AND m.type IN (:types) ORDER BY m.dateObserved DESC";

		Query query = entityManager.createQuery(queryStr);
		query.setParameter("demographicNo", demographicNo);
		query.setParameter("types", lst);

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

	public List<Object[]> findMeasurementsByDemographicIdAndLocationCode(Integer demoNo, String loincCode) {
		String sql = "SELECT m.dataField, m.dateObserved, e1.val, e3.val " + "FROM Measurement m, MeasurementsExt e1, MeasurementsExt e2, MeasurementsExt e3, MeasurementMap mm " + "WHERE m.id = e1.measurementId " + "AND e1.keyVal = 'lab_no' " + "AND m.id = e2.measurementId " + "AND e2.keyVal = 'identifier' " + "AND m.id = e3.measurementId " + "AND e3.keyVal = 'abnormal' " + "AND e2.val = mm.identCode " + "AND mm.loincCode = :loincCode " + "AND m.demographicId = :demoNo " + "ORDER BY m.dateObserved DESC";
		Query query = entityManager.createQuery(sql);
		query.setParameter("demoNo", demoNo);
		query.setParameter("loincCode", loincCode);
		return query.getResultList();
	}

	public List<Object[]> findMeasurementsWithIdentifiersByDemographicIdAndLocationCode(Integer demoNo, String loincCode) {
		String sql = "SELECT m.dataField, m.dateObserved, e1.val, e3.val, e4.val " + "FROM Measurement m, MeasurementsExt e1, MeasurementsExt e2, MeasurementsExt e3, MeasurementsExt e4, MeasurementMap mm " + "WHERE m.id = e1.measurementId " + "AND e1.keyVal = 'lab_no' " + "AND m.id = e2.measurementId " + "AND e2.keyVal='identifier'" + "AND m.id = e4.measurementId " + "AND e4.keyVal='identifier' " + "AND m.id = e3.measurementId " + "AND e3.keyVal='abnormal' " + "AND e2.val = mm.identCode "
		        + "AND mm.loincCode = :loincCode " + "AND m.demographicId = :demoNo " + "ORDER BY m.dateObserved DESC";

		Query query = entityManager.createQuery(sql);
		query.setParameter("demoNo", demoNo);
		query.setParameter("loincCode", loincCode);
		return query.getResultList();

	}

	public List<Object> findLabNumbers(Integer demoNo, String identCode) {
		String sql = "SELECT DISTINCT e2.val FROM Measurement m, MeasurementsExt e1, MeasurementsExt e2 " + "WHERE m.id = e1.measurementId " + "AND e1.keyVal = 'identifier' " + "AND m.id = e2.measurementId " + "AND e2.keyVal = 'lab_no' " + "AND e1.val= :identCode " + "AND m.demographicId = :demoNo";
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

	public List<Object[]> findMeasurementsAndTypes(Integer demoNo) {
		String sql = "FROM Measurement m, MeasurementType mt " + "WHERE m.demographicId = :demoNo " + "AND m.type = mt.type " + "GROUP BY mt.type " + "ORDER BY m.type ASC";
		Query query = entityManager.createQuery(sql);
		query.setParameter("demoNo", demoNo);
		return query.getResultList();
	}

	public List<Object[]> findMeasurementsAndProviders(Integer measurementId) {
		String sql = "FROM Measurement m, MeasurementType mt, Provider p " + "WHERE m.providerNo = p.ProviderNo " + "AND m.id = :mrId " + "AND m.type = mt.type";
		Query query = entityManager.createQuery(sql);
		query.setParameter("mrId", measurementId);
		return query.getResultList();
	}

	public List<Object[]> findMeasurementsAndProvidersByType(String type, Integer demographicNo) {
		String sql = "FROM Measurement m, Provider p, MeasurementType mt " + "WHERE m.providerNo = p.ProviderNo " + "AND m.type = mt.type " + "AND m.type = :type " + "AND m.demographicId = :demoNo";
		Query query = entityManager.createQuery(sql);
		query.setParameter("type", type);
		query.setParameter("demoNo", demographicNo);
		return query.getResultList();
	}

	public Object[] findMeasurementsAndProvidersByDemoAndType(Integer demographicNo, String type) {
		String sql = "FROM Measurement m, Provider p, MeasurementType mt " + "WHERE m.demographicId = :demoNo " + "AND m.type = :type " + "AND (" + "	m.providerNo = p.ProviderNo " + "	OR m.providerNo = '0'" + ") " + "AND m.type = mt.type " + "GROUP BY m.id " + "ORDER BY m.dateObserved DESC, m.createDate DESC";
		Query query = entityManager.createQuery(sql);
		query.setParameter("type", type);
		query.setParameter("demoNo", demographicNo);
		query.setMaxResults(1);

		List<Object[]> result = query.getResultList();
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}

	public List<Measurement> findByValue(String key, String value) {
		Query q = entityManager.createQuery("SELECT m FROM Measurement m, MeasurementsExt e " + "WHERE m.id = e.measurementId " + "AND e.keyVal = :key " + "AND e.val = :val");
		q.setParameter("key", key);
		q.setParameter("val", value);
		return q.getResultList();
	}

	public List<Object> findObservationDatesByDemographicNoTypeAndMeasuringInstruction(Integer demo, String type, String mInstrc) {
		String sql = "SELECT DISTINCT m.dateObserved FROM Measurement m " + "WHERE m.demographicId = :demo " + "AND m.type = :type " + "AND m.measuringInstruction = :mInstrc " + "ORDER BY m.dateObserved";
		Query query = entityManager.createQuery(sql);
		query.setParameter("demo", demo);
		query.setParameter("type", type);
		query.setParameter("mInstrc", mInstrc);
		return query.getResultList();
	}

	public List<Date> findByDemographicNoTypeAndMeasuringInstruction(Integer demo, String type, String mInstrc) {
		String sql = "SELECT m.dateObserved FROM Measurement m " + "WHERE m.demographicId = :demo " + "AND m.type = :type " + "AND m.measuringInstruction = :mInstrc " + "ORDER BY m.dateObserved";
		Query query = entityManager.createQuery(sql);
		query.setParameter("demo", demo);
		query.setParameter("type", type);
		query.setParameter("mInstrc", mInstrc);
		return query.getResultList();
	}

	public Measurement findByDemographicNoTypeAndDate(Integer demo, String type, java.util.Date date) {
		String sql = "FROM Measurement m WHERE m.demographicId = :demo " + "AND m.type = :type " + "AND m.dateObserved = :date " + "ORDER BY m.createDate DESC";
		Query query = entityManager.createQuery(sql);
		query.setMaxResults(1);
		query.setParameter("demo", demo);
		query.setParameter("type", type);
		query.setParameter("date", date);
		return getSingleResultOrNull(query);
	}

	public List<Measurement> findByDemoNoTypeDateAndMeasuringInstruction(Integer demoNo, Date from, Date to, String type, String instruction) {
		Query query = createQuery("m", "m.dateObserved >= :from AND m.dateObserved <= :to AND m.type = :type " + "AND m.measuringInstruction = :instruction AND m.demographicId = :demoNo");
		query.setParameter("demoNo", demoNo);
		query.setParameter("from", from);
		query.setParameter("to", to);
		query.setParameter("type", type);
		query.setParameter("instruction", instruction);
		return query.getResultList();
	}

	public List<Object[]> findLastEntered(Date from, Date to, String measurementType, String mInstrc) {
		Query query = createQuery("SELECT m.demographicId, max(m.createDate)", "m", "m.dateObserved >= :from AND m.dateObserved <= :to AND m.type = :measurementType AND m.measuringInstruction = :mInstrc group by m.demographicId");
		query.setParameter("from", from);
		query.setParameter("to", to);
		query.setParameter("measurementType", measurementType);
		query.setParameter("mInstrc", mInstrc);
		return query.getResultList();
	}

	public List<Measurement> findByDemographicNoTypeAndDate(Integer demographicNo, Date createDate, String measurementType, String mInstrc) {
		String sql = "FROM Measurement m " + "WHERE m.createDate = :createDate " + "AND m.demographicId = :demographicNo " + "AND m.type = :measurementType " + "AND m.measuringInstruction = :mInstrc";
		Query query = entityManager.createQuery(sql);
		query.setParameter("demographicNo", demographicNo);
		query.setParameter("createDate", createDate);
		query.setParameter("measurementType", measurementType);
		query.setParameter("mInstrc", mInstrc);
		return query.getResultList();
	}

	@NativeSql("measurements")
	public List<Object[]> findByDemoNoDateTypeMeasuringInstrAndDataField(Integer demographicNo, Date dateEntered, String measurementType, String mInstrc, String upper, String lower) {
		String sql = "SELECT dataField FROM measurements " + "WHERE dateEntered = :dateEntered " + "AND demographicNo = :demographicNo " + "AND type = :measurementType " + "AND measuringInstruction = :mInstrc " + "AND dataField < :upper " + "AND dataField > :lower";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("dateEntered", dateEntered);
		query.setParameter("demographicNo", demographicNo);
		query.setParameter("measurementType", measurementType);
		query.setParameter("mInstrc", mInstrc);
		query.setParameter("upper", upper);
		query.setParameter("lower", lower);
		return query.getResultList();
	}

	public List<Object[]> findLastEntered(Date from, Date to, String measurementType) {
		Query query = createQuery("SELECT m.demographicId, MAX(m.createDate)", "m", "m.dateObserved >= :from AND m.dateObserved <= :to AND m.type = :measurementType GROUP BY m.demographicId");
		query.setParameter("from", from);
		query.setParameter("to", to);
		query.setParameter("measurementType", measurementType);
		return query.getResultList();
	}

	public List<Measurement> findByDemoNoDateAndType(Integer demoNo, Date createDate, String type) {
		Query query = createQuery("m", "m.createDate = :createDate AND m.demographicId = :demoNo AND m.type = :type");
		query.setParameter("createDate", createDate);
		query.setParameter("demoNo", demoNo);
		query.setParameter("type", type);
		return query.getResultList();
	}

	@NativeSql("measurements")
	public List<Object[]> findByDemoNoDateTypeAndDataField(Integer demographicNo, Date dateEntered, String type, String upper, String lower) {
		String sql = "SELECT dataField FROM measurements WHERE dateEntered = :dateEntered " + "AND demographicNo = :demographicNo " + "AND type = :type " + "AND dataField < :upper " + "AND dataField > :lower";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("dateEntered", dateEntered);
		query.setParameter("demographicNo", demographicNo);
		query.setParameter("type", type);
		query.setParameter("upper", upper);
		query.setParameter("lower", lower);
		return query.getResultList();
	}

	public List<Object[]> findTypesAndMeasuringInstructionByDemographicId(Integer demoNo) {
		Query query = createQuery("SELECT DISTINCT m.type, m.measuringInstruction", "m", "m.demographicId = :demoNo");
		query.setParameter("demoNo", demoNo);
		return query.getResultList();
	}

	public List<Object[]> findByCreateDate(Date from, Date to) {
		Query query = createQuery("SELECT DISTINCT m.demographicId", "m", "m.createDate >= :from AND m.createDate <= :to");
		query.setParameter("from", from);
		query.setParameter("to", to);
		return query.getResultList();
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

	public List<Measurement> findByType(Integer demographicId, String type, Date after) {
		String sqlCommand = "select x from Measurement x where x.demographicId = ?1 and x.type = ?2 and x.dateObserved >= ?3 order by x.dateObserved desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);
		query.setParameter(2, type);
		query.setParameter(3, after);

		@SuppressWarnings("unchecked")
		List<Measurement> results = query.getResultList();

		return results;
	}

	public List<Measurement> findByType(Integer demographicId, List<String> types) {
		String sqlCommand = "select x from Measurement x where x.demographicId = :demographicNo and x.type IN (:types) order by x.dateObserved desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter("demographicNo", demographicId);
		query.setParameter("types", types);

		@SuppressWarnings("unchecked")
		List<Measurement> results = query.getResultList();

		return results;
	}

	public List<Measurement> findByType(Integer demographicId, List<String> types, Date after) {
		String sqlCommand = "select x from Measurement x where x.demographicId = :demographicNo and x.type IN (:types) and x.dateObserved >= :after order by x.dateObserved desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter("demographicNo", demographicId);
		query.setParameter("types", types);
		query.setParameter("after", after);

		@SuppressWarnings("unchecked")
		List<Measurement> results = query.getResultList();

		return results;
	}

	public List<Measurement> findByType(String type) {
		String sqlCommand = "select x from Measurement x where x.type = ?1 order by x.demographicId, x.dateObserved desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, type);

		@SuppressWarnings("unchecked")
		List<Measurement> results = query.getResultList();

		return results;
	}

	public List<Measurement> findByType(List<String> types) {
		String sqlCommand = "select x from Measurement x where x.type in (:type) order by x.demographicId, x.dateObserved desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter("type", types);

		@SuppressWarnings("unchecked")
		List<Measurement> results = query.getResultList();

		return results;
	}

	public List<Integer> findDemographicIdsByType(List<String> types) {
		String sqlCommand = "select distinct x.demographicId from Measurement x where x.type in (:types)";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter("types", types);

		@SuppressWarnings("unchecked")
		List<Integer> results = query.getResultList();

		return results;
	}

	public List<Measurement> findByTypeBefore(Integer demographicId, String type, Date date) {
		String sqlCommand = "select x from Measurement x where x.demographicId = ?1 and x.type = ?2 and x.dateObserved <= ?3 order by x.dateObserved desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);
		query.setParameter(2, type);
		query.setParameter(3, date);

		@SuppressWarnings("unchecked")
		List<Measurement> results = query.getResultList();

		return results;
	}

	public List<Measurement> findByProviderDemographicLastUpdateDate(String providerNo, Integer demographicId, Date updatedAfterThisDateExclusive, int itemsToReturn) {
		String sql = "select x from "+modelClass.getSimpleName()+" x where x.providerNo=:providerNo and x.demographicId=:demographicId and x.createDate>:updatedAfterThisDateExclusive order by x.createDate";

		Query query = entityManager.createQuery(sql);
		query.setParameter("providerNo", providerNo);
		query.setParameter("demographicId", demographicId);
		query.setParameter("updatedAfterThisDateExclusive", updatedAfterThisDateExclusive);

		setLimit(query, itemsToReturn);

		@SuppressWarnings("unchecked")
		List<Measurement> results = query.getResultList();
		return results;
	}
	
	@NativeSql("measurements")
	public List<Integer> findNewMeasurementsSinceDemoKey(String keyName) {
		
		String sql = "select distinct m.demographicNo from measurements m,demographic d,demographicExt e where m.demographicNo = d.demographic_no and d.demographic_no = e.demographic_no and e.key_val=? and m.type in ('HT','WT') and m.dateEntered > e.value";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1, keyName);
		
		return query.getResultList();
	}

	public Measurement getLatestMeasurementByAppointment(int appointmentNo, String type) {
		String sqlCommand = "From Measurement m WHERE m.appointmentNo = " + appointmentNo + " and m.type=?1 ORDER BY m.dateObserved DESC";

		@SuppressWarnings("unchecked")
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, type);
		
		return getSingleResultOrNull(query);
		
	}
	
	public Measurement getLatestMeasurementByDemographicNoAndType(int demographicNo, String type) {
		String sqlCommand = "From Measurement m WHERE m.demographicId = " + demographicNo + " and m.type=? ORDER BY m.dateObserved DESC";
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, type);
		
		return getSingleResultOrNull(query);		
		
	}
}
