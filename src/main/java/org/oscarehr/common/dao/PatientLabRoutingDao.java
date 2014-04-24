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

import org.apache.commons.lang.StringEscapeUtils;
import org.oscarehr.common.model.LabPatientPhysicianInfo;
import org.oscarehr.common.model.LabTestResults;
import org.oscarehr.common.model.MdsMSH;
import org.oscarehr.common.model.MdsOBX;
import org.oscarehr.common.model.MdsZRG;
import org.oscarehr.common.model.PatientLabRouting;
import org.springframework.stereotype.Repository;

@Repository
public class PatientLabRoutingDao extends AbstractDao<PatientLabRouting> {

        public static final Integer UNMATCHED = 0;
	public static final String HL7 = "HL7";

	public PatientLabRoutingDao() {
		super(PatientLabRouting.class);
	}

	/**
	 * Finds routing record containing reference to the demographic record with the 
	 * specified lab results reference number of {@link #HL7} lab type. 
	 * 
	 * LabId is also refereed to as Lab_no, and segmentId.
	 */
	public PatientLabRouting findDemographicByLabId(Integer labId) {
		return findDemographics(HL7, labId);
	}

	/**
	 * Finds routing record containing reference to the demographic record with the 
	 * specified lab type and lab results reference number. 
	 * 
	 * @param labType
	 * 		Type of the lab record to look up
	 * @param labNo
	 * 		Number of the lab record to look up
	 * @return
	 * 		Returns the container pointing to the demographics or null of no matching container is found.
	 */
	public PatientLabRouting findDemographics(String labType, Integer labNo) {
    		String sqlCommand="select x from "+ this.modelClass.getName() +" x where x.labType=?1 and x.labNo=?2";
    		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, labType);
		query.setParameter(2, labNo);
		return(getSingleResultOrNull(query));
	}

    @SuppressWarnings("unchecked")
    public List<PatientLabRouting> findDocByDemographic(Integer docNum) {

    	String query = "select x from " + modelClass.getName() + " x where x.labNo=? and x.labType=?";
    	Query q = entityManager.createQuery(query);

    	q.setParameter(1, docNum);
    	q.setParameter(2, "DOC");

    	return q.getResultList();
    }
    
    public PatientLabRouting findByLabNo(int labNo) {
    	String query = "select x from " + modelClass.getName() + " x where x.labNo=?";
    	Query q = entityManager.createQuery(query);
    	q.setParameter(1, labNo);
    	return this.getSingleResultOrNull(q);
    }
    
    @SuppressWarnings("unchecked")
    public List<PatientLabRouting> findByLabNoAndLabType(int labNo, String labType) {

    	String query = "select x from " + modelClass.getName() + " x where x.labNo=? and x.labType=?";
    	Query q = entityManager.createQuery(query);

    	q.setParameter(1, labNo);
    	q.setParameter(2, labType);
    	
    	return q.getResultList();
    }

	/**
	 * Finds unique test names for a patient
	 * 
	 * @param demoId
	 * 		Demographic ID for the patient
	 * @param labType
	 * 		Lab type to find test for
	 * @return
	 * 		Returns a list of triples containing lab type, observation identifier and observation result status
	 */
	@SuppressWarnings("unchecked")
    public List<Object[]> findUniqueTestNames(Integer demoId, String labType) {
		String sql = "SELECT DISTINCT p.labType, x.observationIdentifier, x.observationResultStatus " +
                "FROM MdsOBX x, MdsMSH m, PatientLabRouting p " +
                "WHERE p.demographicNo = :demoNo " +
                "AND m.id = p.labNo " +
                "AND x.id = m.id " +
                "AND p.labType = :labType";
		Query query = entityManager.createQuery(sql);
		query.setParameter("demoNo", demoId);
		query.setParameter("labType", labType);
		return query.getResultList();
    }
    
	/**
	 * Finds unique test names for a patient
	 * 
	 * @param demoId
	 * 		Demographic ID for the patient
	 * @param labType
	 * 		Lab type to find test for
	 * @return
	 * 		Returns a list of triples containing {@link MdsOBX}, {@link MdsMSH}, {@link PatientLabRouting}
	 */
	@SuppressWarnings("unchecked")
    public List<Object[]> findTests(Integer demoId, String labType) {
		String sql = "FROM " + MdsOBX.class.getName() + " x, " +  MdsMSH.class.getName() + " m, PatientLabRouting p " +
                "WHERE p.demographicNo = :demoNo " +
                "AND m.id = p.labNo " +
                "AND x.id = m.id " +
                "AND p.labType = :labType";
		Query query = entityManager.createQuery(sql);
		query.setParameter("demoNo", demoId);
		query.setParameter("labType", labType);
		return query.getResultList();
    }

    /**
	 * Finds unique test names for a patient
	 * 
	 * @param demoNo
	 * 		Demographic ID for the patient
	 * @param labType
	 * 		Lab type to find test for
	 * @return
	 * 		Returns a list of pairs containing lab type and observation identifier
	 */
    @SuppressWarnings("unchecked")
    public List<Object[]> findUniqueTestNamesForPatientExcelleris(Integer demoNo, String labType) {
		String sql = "SELECT DISTINCT p.labType, x.observationIdentifier " +
				"FROM PatientLabRouting p, Hl7Msh m, Hl7Pid pi, Hl7Obr r, Hl7Obx x  " + 
				"WHERE p.demographicNo = :demoNo " + 
				"AND p.labNo = m.messageId " + 
				"AND pi.messageId = m.messageId " + 
				"AND r.id = pi.id " + 
				"AND r.id = x.obrId " +
				"AND p.labType = :labType";
		Query query = entityManager.createQuery(sql);
		query.setParameter("demoNo", demoNo);
		query.setParameter("labType", labType);
		return query.getResultList();
    }

	/**
	 * Finds lab routings for the specified demographic and lab type
	 * 
	 * @param demoNo
	 * 		Demographic to find labs for
	 * @param labType
	 * 		Type of the lab to get routings for
	 * @return
	 * 		Returns the routings found. 
	 */
	@SuppressWarnings("unchecked")
    public List<PatientLabRouting> findByDemographicAndLabType(Integer demoNo, String labType) {
		Query query = createQuery("r", "r.demographicNo = :demoNo AND r.labType = :labType");
		query.setParameter("demoNo", demoNo);
		query.setParameter("labType", labType);
		return query.getResultList();	    
    }
	
	/**
	 * Finds all routings and tests for the specified demographic and lab
	 * 
	 * @param demoNo
	 * 		Demographic to find tests for
	 * @param labType
	 * 		Lab type to find tests for
	 * @return
	 * 		Returns a list of triples containing {@link PatientLabRouting}, {@link LabTestResults}, {@link LabPatientPhysicianInfo}
	 */
	@SuppressWarnings("unchecked")
    public List<Object[]> findRoutingsAndTests(Integer demoNo, String labType, String testName) {
		String sql = "FROM PatientLabRouting p, " + LabTestResults.class.getSimpleName() + " ltr, " + LabPatientPhysicianInfo.class.getSimpleName() + " lpp WHERE " +
				"p.labType = :labType " +
				"AND p.demographicNo = :demoNo " +
				"AND p.labNo = ltr.labPatientPhysicianInfoId " +
				"AND ltr.testName = :testName " +
		        "AND ltr.labPatientPhysicianInfoId = lpp.id " +
		        "ORDER BY lpp.collectionDate";
	    Query query = entityManager.createQuery(sql);
	    query.setParameter("labType", labType);
	    query.setParameter("demoNo", demoNo);
	    query.setParameter("testName", testName);
	    return query.getResultList();
    }
    
	/**
	 * Finds all routings and tests for the specified demographic and lab
	 * 
	 * @param demoNo
	 * 		Demographic to find tests for
	 * @param labType
	 * 		Lab type to find tests for
	 * @return
	 * 		Returns a list of triples containing {@link PatientLabRouting}, {@link LabTestResults}, {@link LabPatientPhysicianInfo}
	 */
	@SuppressWarnings("unchecked")
    public List<Object[]> findRoutingsAndTests(Integer demoNo, String labType) {
		String sql = "FROM PatientLabRouting p, LabTestResults ltr, LabPatientPhysicianInfo lpp WHERE " +
				"p.labType = :labType " +
				"AND p.demographicNo = :demoNo " +
				"AND p.labNo = ltr.labPatientPhysicianInfoId " +
				"AND ltr.labPatientPhysicianInfoId = lpp.id " +
				"AND ltr.testName <> '' " +
		        "ORDER BY lpp.collectionDate";
		
	    Query query = entityManager.createQuery(sql);
	    query.setParameter("labType", labType);
	    query.setParameter("demoNo", demoNo);
	    return query.getResultList();
    }

	@SuppressWarnings("unchecked")
    public List<Object[]> findMdsRoutings(Integer demoNo, String testName, String labType) {
    	String sql = "FROM MdsOBX x, MdsMSH m, PatientLabRouting p " +
                "WHERE p.labType = :labType " +
                "AND p.demographicNo = :demoNo " +
                "AND x.observationIdentifier like :testName " +
                "AND x.id = m.id " +
                "AND m.id = p.labNo " +
                "ORDER BY m.dateTime";
		
		Query query = entityManager.createQuery(sql);
		query.setParameter("demoNo", demoNo);
		query.setParameter("testName", "%^" + testName + "%");
		query.setParameter("labType", labType);
		return query.getResultList();
    }

	@SuppressWarnings("unchecked")
    public List<Object[]> findHl7InfoForRoutingsAndTests(Integer demoNo, String labType, String testName) {
		String sql = "FROM PatientLabRouting p, Hl7Msh m, Hl7Pid pi, Hl7Obr r, Hl7Obx x, Hl7Orc c " +
				"WHERE p.labType = :labType " +
				"AND p.demographicNo = :demoNo " +
				"AND x.observationIdentifier like :testName " +
				"AND p.labNo = m.messageId " +
				"AND pi.messageId = m.messageId " +
				"AND r.pidId = pi.id " +
				"AND c.pidId = pi.id " +
				"AND r.id = x.id " +
				"ORDER BY r.oberservationDateTime";
		Query query = entityManager.createQuery(sql);
		query.setParameter("demoNo", demoNo);
		query.setParameter("labType", labType);
		query.setParameter("testName", testName);
		return query.getResultList();
    }

	@SuppressWarnings("unchecked")
    public List<Object[]> findRoutingsAndConsultDocsByRequestId(Integer reqId, String docType) {
        String sql = "FROM PatientLabRouting p, ConsultDocs c " +
        		"WHERE p.id = c.documentNo " +
        		"AND c.requestId = :reqId " +
        		"AND c.docType = :docType " +
        		"AND c.deleted IS NULL";
        Query query = entityManager.createQuery(sql);
        query.setParameter("reqId", reqId);
        query.setParameter("docType", docType);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Object[]> findResultsByDemographicAndLabType(Integer demographicNo, String labType) {
    	String sql = 
                "FROM " +
                "PatientLabRouting p, " + MdsMSH.class.getSimpleName() + " msh, " + MdsZRG.class.getSimpleName() + " zrg " +
                "WHERE p.labNo = msh.id "+
                "AND p.labNo = zrg.id " +
                "AND p.labType = :labType " +
                "AND p.demographicNo = :demographicNo";
		Query query = entityManager.createQuery(sql);
		query.setParameter("labType", labType);
		query.setParameter("demographicNo", demographicNo);
		return query.getResultList();
    }

	public List<Object[]> findRoutingAndPhysicianInfoByTypeAndDemoNo(String labType, Integer demographicNo) {
	    String sql = "FROM PatientLabRouting p , LabPatientPhysicianInfo l " +
	    		"WHERE p.labType = :labType " +
	    		"AND p.labNo = l.id " +
	    		"AND p.demographicNo = :demographicNo";
		Query query = entityManager.createQuery(sql);
		query.setParameter("labType", labType);
		query.setParameter("demographicNo", demographicNo);
		return query.getResultList();
    }

	public List<Object[]> findRoutingsAndMdsMshByDemoNo(Integer demographicNo) {
	    String sql = "FROM PatientLabRouting p, MdsMSH m " +
	    		"WHERE p.labType = 'MDS' " +
	    		"AND p.labNo = m.id " +
	    		"AND p.demographicNo = :demographicNo";
		Query query = entityManager.createQuery(sql);
		query.setParameter("demographicNo", demographicNo);
		return query.getResultList();
    }
	
    @SuppressWarnings("unchecked")
    public List<PatientLabRouting> findLabNosByDemographic(Integer demographicNo, String[] labTypes) {
    	
    	StringBuilder sb = new StringBuilder();
    	for(String t:labTypes) {
    		sb.append("'" + StringEscapeUtils.escapeSql(t) + "'");
    	}

    	String query = "select x from " + modelClass.getName() + " x where x.labNo=? and x.labType in ("+sb.toString()+")";
    	Query q = entityManager.createQuery(query);

    	q.setParameter(1, demographicNo);
    	
    	return q.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Integer> findDemographicIdsSince(Date date) {    	
    	String query = "select x.demographicNo from " + modelClass.getName() + " x where x.dateModified > ?1)";
    	Query q = entityManager.createQuery(query);

    	q.setParameter(1, date);
    	
    	return q.getResultList();
    }

    
}