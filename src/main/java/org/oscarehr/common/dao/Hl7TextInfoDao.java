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

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.codec.binary.Base64;
import org.oscarehr.common.NativeSql;
import org.oscarehr.common.model.Hl7TextInfo;
import org.oscarehr.common.model.Hl7TextMessageInfo;
import org.oscarehr.common.model.Hl7TextMessageInfo2;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class Hl7TextInfoDao extends AbstractDao<Hl7TextInfo> {

	public Hl7TextInfoDao() {
		super(Hl7TextInfo.class);
	}
	
	@SuppressWarnings("unchecked")
	@Deprecated
	public List<Hl7TextInfo> findAll() {
		Query query = createQuery("x", null);
		return query.getResultList();
	}

	/**
	 * LabId is also refereed to as Lab_no, and segmentId.
	 */
    public Hl7TextInfo findLabId(int labId) {

    	String sqlCommand="select x from Hl7TextInfo x where x.labNumber=?1";

    	Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, labId);

		return(getSingleResultOrNull(query));
    }

    @SuppressWarnings("unchecked")
    public List<Hl7TextInfo> findByHealthCardNo(String hin) {
    	String sql = "select hl7 from Hl7TextInfo hl7 where hl7.healthNumber = :hin";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter("hin", hin);
    	List<Hl7TextInfo> list = query.getResultList();
    	return list;
    }

    public List<Hl7TextInfo> searchByAccessionNumber(String acc) {

    	String sqlCommand="select x from Hl7TextInfo x where x.accessionNumber = ?1";

    	Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1,acc);

		@SuppressWarnings("unchecked")
		List<Hl7TextInfo> results = query.getResultList();

		return results;
    }
                                                                                
    // Calgary labs are associated by Accession number usually. Glucose labs are not, but can be 
    // found by filler number                                                   
    public Hl7TextInfo findLatestVersionByAccessionNumberOrFillerNumber(
		String acc, String fillerNumber) {

		String sqlCommand="select x from Hl7TextInfo x where x.accessionNumber = ?1 " +
			"OR x.fillerOrderNum = ?2 order by lab_no DESC";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, acc);
		query.setParameter(2, fillerNumber);

		return (getSingleResultOrNull(query));
    }

    public List<Hl7TextInfo> searchByFillerOrderNumber(String fon, String sending_facility){
    	String sql = "select x from Hl7TextInfo x where x.fillerOrderNum=?1 and sendingFacility=?2";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1, fon);
    	query.setParameter(2, sending_facility);

    	@SuppressWarnings("unchecked")
		List<Hl7TextInfo> lab =  query.getResultList();

		return lab;


    }

    public void updateReportStatusByLabId(String reportStatus, int labNumber){
    	Query query = entityManager.createQuery("update " + modelClass.getName() + " x set x.reportStatus=? where x.labNumber=?");
		query.setParameter(1, reportStatus);
		query.setParameter(2, labNumber);

		query.executeUpdate();

    }

    public List<Hl7TextMessageInfo> getMatchingLabs(String hl7msg) {
    	String sql = "SELECT a.lab_no as id, m2.message, a.lab_no AS lab_no_A, b.lab_no AS lab_no_B, a.obr_date as labDate_A, b.obr_date as labDate_B FROM hl7TextInfo a, hl7TextInfo b, hl7TextMessage m2 WHERE m2.lab_id = a.lab_no AND a.accessionNum !='' AND a.accessionNum=b.accessionNum AND b.lab_no IN ( SELECT lab_id FROM hl7TextMessage WHERE message=?1 ) ORDER BY a.obr_date, a.lab_no";   	
    	Query query = entityManager.createNativeQuery(sql, Hl7TextMessageInfo.class);
    	try {
	        query.setParameter(1, (new String(Base64.encodeBase64(hl7msg.getBytes(MiscUtils.DEFAULT_UTF8_ENCODING)), MiscUtils.DEFAULT_UTF8_ENCODING)));
        } catch (UnsupportedEncodingException e) {

	        MiscUtils.getLogger().error("Error setting query parameter hl7msg ",e);
        }

    	@SuppressWarnings("unchecked")
		List<Hl7TextMessageInfo> labs =  query.getResultList();
    	return labs;

    }

    public List<Hl7TextMessageInfo2> getMatchingLabsByAccessionNo(String accession) {
    	if(accession != null){
    		String sql = "SELECT a.lab_no as id,  m2.message,  a.lab_no AS lab_no_A,  a.obr_date as labDate_A  FROM hl7TextInfo a, hl7TextMessage m2  WHERE  m2.lab_id = a.lab_no AND  a.accessionNum = ? ORDER BY a.obr_date, a.lab_no";
    		Query query = entityManager.createNativeQuery(sql, Hl7TextMessageInfo2.class);
    	
    		query.setParameter(1, accession);
        
    		@SuppressWarnings("unchecked")
    		List<Hl7TextMessageInfo2> labs =  query.getResultList();
    		return labs;
    	}
    	return null;
    }

    
    public List<Hl7TextInfo> getAllLabsByLabNumberResultStatus() {
    	String sql = "SELECT x FROM Hl7TextInfo x";
    	Query query = entityManager.createQuery(sql);

    	@SuppressWarnings("unchecked")
		List<Hl7TextInfo> labs = query.getResultList();

    	return labs;
    }

    public void updateResultStatusByLabId(String resultStatus, int labNumber){
    	Query query = entityManager.createQuery("update " + modelClass.getName() + " x set x.resultStatus=? where x.labNumber=?");
		query.setParameter(1, resultStatus);
		query.setParameter(2, labNumber);

		query.executeUpdate();

    }

    public void createUpdateLabelByLabNumber(String label, int lab_no) {
    	String sql = "update Hl7TextInfo x set x.label=?1 where x.labNumber=?2";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1, label);
    	query.setParameter(2, lab_no);
    	query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<Hl7TextInfo> findByLabId(Integer labNo) {
    	 Query query = createQuery("h", "h.labNumber = :labNo ORDER BY h.obrDate DESC");
    	 query.setParameter("labNo", labNo);
    	 return query.getResultList();
    }
    
	@SuppressWarnings("unchecked")
    public List<Object[]> findByLabIdViaMagic(Integer labNo) {
		String sql = "FROM Hl7TextInfo a, Hl7TextInfo b " +
				"WHERE a.accessionNumber <> '' " +
				"AND a.accessionNumber = b.accessionNumber " +
				"AND b.labNumber = :labNo " +
				"ORDER BY a.obrDate, a.finalResultCount, a.labNumber";
	    Query q = entityManager.createQuery(sql);
	    q.setParameter("labNo", labNo);
	    return q.getResultList();
    }

	@SuppressWarnings("unchecked")
    public List<Object[]> findByDemographicId(Integer demographicNo) {
		String sql = 
				"FROM Hl7TextInfo hl7, PatientLabRouting p " +
				"WHERE p.labNo = hl7.labNumber "+
				"AND p.labType = 'HL7' " +
				"AND p.demographicNo = :dNo " +
				"GROUP BY hl7.labNumber";
		Query q = entityManager.createQuery(sql);
		q.setParameter("dNo", demographicNo);
		return q.getResultList();
    }

	public List<Object[]> findLabsViaMagic(String status, String providerNo, String patientFirstName, String patientLastName, String patientHealthNumber) {
		String sql = "FROM Hl7TextInfo info, ProviderLabRoutingModel p " +
			"WHERE info.labNumber = p.labNo "+
			"AND p.status like :status " +
			"AND p.providerNo like :providerNo " +
			"AND p.labType = 'HL7' " +
			"AND info.firstName like :fName " +
			"AND info.lastName like :lName";
		if (patientHealthNumber!=null) { 
			sql = sql + " AND info.healthNumber like :hNum";
		}
		sql = sql+" ORDER BY info.labNumber DESC";
		
		Query q = entityManager.createQuery(sql);
		q.setParameter("status", "%" + status + "%" );
		q.setParameter("providerNo", providerNo.equals("") ? "%" : providerNo);
		q.setParameter("fName", patientFirstName + "%");
		q.setParameter("lName", patientLastName + "%");
		if (patientHealthNumber != null) {
			q.setParameter("hNum", "%" + patientHealthNumber + "%");
		}
	    return null;
    }

	@SuppressWarnings("unchecked")
    @NativeSql({"hl7TextInfo", "providerLabRouting", "ctl_document", "demographic"})
	public List<Object[]> findLabAndDocsViaMagic(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status, boolean isPaged, Integer page, Integer pageSize, boolean mixLabsAndDocs, Boolean isAbnormal, boolean searchProvider, boolean patientSearch) {
	    String sql;
	    if (mixLabsAndDocs) {
	    	if ("0".equals(demographicNo)  || "0".equals(providerNo)) {
	    		sql = " SELECT info.label, info.lab_no, info.sex, info.health_no, info.result_status, info.obr_date, info.priority, info.requesting_client, info.discipline, info.last_name, info.first_name, info.report_status, info.accessionNum, info.final_result_count, X.status "
	    			+ " FROM hl7TextInfo info, "
	    			+ " (SELECT plr.id, plr.lab_type, plr.lab_no, plr.status "
	    			+ "  FROM patientLabRouting plr2, providerLabRouting plr, hl7TextInfo info "
	    			+ "  WHERE plr.lab_no = plr2.lab_no "
	    			+ (searchProvider ? " AND plr.provider_no = '"+providerNo+"' " : "")
	    			+ "    AND plr.status like '%"+status+"%' "
	    			+ "    AND plr.lab_type = 'HL7' "
	    			+ "    AND plr2.lab_type = 'HL7' "
	    			+ "    AND info.lab_no = plr.lab_no "
	    			+ (isAbnormal != null && isAbnormal ? " AND info.result_status = 'A' " :
	    				isAbnormal != null && !isAbnormal ? " AND (info.result_status IS NULL OR info.result_status != 'A') " : "")
	    				+ " UNION "
	    				+ " SELECT plr.id, plr.lab_type, plr.lab_no, plr.status "
	    				+ " FROM ctl_document cd, providerLabRouting plr  "
	    				+ " WHERE plr.lab_type = 'DOC' AND plr.status like '%"+status+"%' "
	    				+ (searchProvider ? " AND plr.provider_no = '"+providerNo+"' " : "")
	    				+ " AND plr.lab_no = cd.document_no "
	    				+ " AND 	cd.module_id = -1 "
	    				+ " ORDER BY id DESC "
	    				+ " ) AS X "
	    				+ " WHERE X.lab_type = 'HL7' AND X.lab_no = info.lab_no "
	    				+ (isPaged ? "	LIMIT " + (page * pageSize) + "," + pageSize : "");
	    	}

	    	else if (demographicNo != null && !"".equals(demographicNo)) {
	    		sql = " SELECT info.label, info.lab_no, info.sex, info.health_no, info.result_status, info.obr_date, info.priority, info.requesting_client, info.discipline, info.last_name, info.first_name, info.report_status,  info.accessionNum, info.final_result_count, X.status "
	    			+ " FROM hl7TextInfo info, "
	    			+" (SELECT * FROM "
	    			+" (SELECT DISTINCT plr.id, plr.lab_type, plr.lab_no, plr.status FROM providerLabRouting plr, ctl_document cd "
	    			+" WHERE 	"
	    			+" (cd.module_id = '"+demographicNo+"' "
	    			+ "	AND cd.document_no = plr.lab_no"
	    			+ "	AND plr.lab_type = 'DOC'  	"
	    			+ "	AND plr.status like '%"+status+"%' " + (searchProvider ? " AND plr.provider_no = '"+providerNo+"' )" : " )")
	    			+ " ORDER BY id DESC) AS Y"
	    			+ " UNION"
	    			+ " SELECT * FROM"
	    			+ " (SELECT DISTINCT plr.id, plr.lab_type, plr.lab_no, plr.status  FROM providerLabRouting plr, patientLabRouting plr2"
	    			+ " WHERE"
	    			+ "	plr.lab_type = 'HL7' AND plr2.lab_type = 'HL7'"
	    			+ "	AND plr.status like '%"+status+"%' " + (searchProvider ? " AND plr.provider_no = '"+providerNo+"' " : " ")
	    			+ " 	AND plr.lab_no = plr2.lab_no AND plr2.demographic_no = '"+demographicNo+"'"
	    			+ " ORDER BY id DESC) AS Z"
	    			+ " ORDER BY id DESC"
	    			+ " ) AS X "
	    			+ " WHERE X.lab_type = 'HL7' and X.lab_no = info.lab_no "
	    			+ (isPaged ? "	LIMIT " + (page * pageSize) + "," + pageSize : "");
	    	}
	    	else if (patientSearch) { // N
	    		sql = " SELECT info.label, info.lab_no, info.sex, info.health_no, info.result_status, info.obr_date, info.priority, info.requesting_client, info.discipline, info.last_name, info.first_name, info.report_status, info.accessionNum, info.final_result_count, Z.status "
	    				+ " FROM hl7TextInfo info, "
	    				+ " 	(SELECT * FROM "
						+ "			(SELECT DISTINCT plr.id, plr.lab_type, plr.lab_no, plr.status, d.demographic_no "
						+ "				FROM providerLabRouting plr, ctl_document cd, demographic d "
						+ "				WHERE "
						+ "					d.first_name like '%"+patientFirstName+"%' AND d.last_name like '%"+patientLastName+"%' AND d.hin like '%"+patientHealthNumber+"%' "
						+ "					AND cd.module_id = d.demographic_no 	AND cd.document_no = plr.lab_no	AND plr.lab_type = 'DOC' "
						+ "					AND plr.status like '%"+status+"%' " + (searchProvider ? " AND plr.provider_no = '"+providerNo+"' " : " ")
						+ " 		) AS X "
						+ " 		UNION "
						+ "			(SELECT DISTINCT plr.id, plr.lab_type, plr.lab_no, plr.status, d.demographic_no "
						+ "				FROM providerLabRouting plr, patientLabRouting plr2, demographic d" + (isAbnormal != null ? ", hl7TextInfo info " : " ")
						+ "				WHERE d.first_name like '%"+patientFirstName+"%' AND d.last_name like '%"+patientLastName+"%' AND d.hin like '%"+patientHealthNumber+"%' "
						+ "					AND	plr.lab_type = 'HL7' AND plr2.lab_type = 'HL7' "
						+ 					(isAbnormal != null ? " AND plr.lab_no = info.lab_no AND (info.result_status IS NULL OR info.result_status != 'A') " : " " )
						+ "					AND plr.status like '%"+status+"%' " + (searchProvider ? " AND plr.provider_no = '"+providerNo+"' " : " ")
						+ " 				AND plr.lab_no = plr2.lab_no AND plr2.demographic_no = d.demographic_no "
						+ " 		) "
						+ " 		UNION "
						+ " 		(SELECT DISTINCT plr.id, plr.lab_type, plr.lab_no, plr.status, NULL AS demographic_no "
						+ " 			FROM providerLabRouting plr, hl7TextInfo info "
						+ " 			WHERE info.first_name like '%"+patientFirstName+"%' AND info.last_name like '%"+patientLastName+"%' AND info.health_no like '%"+patientHealthNumber+"%' "
						+ " 				AND plr.lab_type = 'HL7' AND plr.lab_no = info.lab_no "
						+					(isAbnormal != null ? " AND (info.result_status IS NULL OR info.result_status != 'A') " : " ")
						+ " 				AND plr.status like '%"+status+"%' " + (searchProvider ? " AND plr.provider_no = '"+providerNo+"' " : " ")
						+ " 				AND plr.lab_no NOT IN (SELECT DISTINCT lab_no FROM patientLabRouting WHERE lab_type = 'HL7' AND demographic_no != 0) "
						+ " 		) "
						+ " 		ORDER BY id DESC " 
						+ " 	) AS Z "
						+ " WHERE Z.lab_type = 'HL7' and Z.lab_no = info.lab_no "
						+ (isPaged ? "	LIMIT " + (page * pageSize) + "," + pageSize : "");
	    	}
	    	else { // N
	    		sql = " SELECT info.label, info.lab_no, info.sex, info.health_no, info.result_status, info.obr_date, info.priority, info.requesting_client, info.discipline, info.last_name, info.first_name, info.report_status,  info.accessionNum, info.final_result_count, X.status "
	    			+ " FROM hl7TextInfo info, "
	    			+ " (SELECT DISTINCT plr.id, plr.lab_type, plr.lab_no, plr.status "
	    			+ " FROM providerLabRouting plr"  + (isAbnormal != null ? ", hl7TextInfo info " : " ")
	    			+ " WHERE ("
	    			+ "       plr.status like '%"+status+"%' " + (searchProvider ? " AND plr.provider_no = '"+providerNo+"' " : "")
	    			+ (isAbnormal != null ? "     AND (plr.lab_type = 'DOC' OR (plr.lab_no = info.lab_no AND ("+(!isAbnormal ? "info.result_status IS NULL OR" : "") + " info.result_status "+(isAbnormal ? "" : "!")+"= 'A'))) " : " ")
	    			+ "       ) "
	    			+ " ORDER BY id DESC "
	    			+ " ) AS X "
	    			+ " WHERE X.lab_type = 'HL7' and X.lab_no = info.lab_no "
	    			+ (isPaged ? "	LIMIT " + (page * pageSize) + "," + pageSize : "");
	    	}
	    }
	    else {
	    	if ("0".equals(demographicNo) || "0".equals(providerNo)) { // Unmatched labs
	    		sql = " SELECT info.label, info.lab_no, info.sex, info.health_no, info.result_status, info.obr_date, info.priority, info.requesting_client, info.discipline, info.last_name, info.first_name, info.report_status,  info.accessionNum, info.final_result_count, plr.status "
	    			+ " FROM patientLabRouting plr2, providerLabRouting plr, hl7TextInfo info "
	    			+ " WHERE plr.lab_no = plr2.lab_no "
	    			+ (searchProvider ? " AND plr.provider_no = '"+providerNo+"' " : "")
	    			+ " AND plr.lab_type = 'HL7' "
	    			+ " AND plr.status like '%"+status+"%' "
	    			+ " AND plr2.lab_type = 'HL7' "
	    			+ " AND plr.lab_no = info.lab_no "
	    			+ (isAbnormal != null && isAbnormal ? "AND info.result_status = 'A'" :
	    				isAbnormal != null && !isAbnormal ? "AND (info.result_status IS NULL OR info.result_status != 'A')" : "")
	    				+ " ORDER BY plr.id DESC "
	    				+ (isPaged ? "	LIMIT " + (page * pageSize) + "," + pageSize : "");
	    	}
	    	else if (demographicNo != null && !"".equals(demographicNo)) {
	    		sql = " SELECT info.label, info.lab_no, info.sex, info.health_no, info.result_status, info.obr_date, info.priority, info.requesting_client, info.discipline, info.last_name, info.first_name, info.report_status,  info.accessionNum, info.final_result_count, X.status "
	    			+ " FROM hl7TextInfo info, "
	    			+ " (SELECT DISTINCT plr.id,plr.lab_no, plr.lab_type,  plr.status, d.demographic_no "
	    			+ " FROM providerLabRouting plr, patientLabRouting plr2, demographic d "
	    			+ " WHERE 	(d.demographic_no = '"+demographicNo+"' "
	    			+ " 		AND plr.lab_no = plr2.lab_no AND plr2.demographic_no = d.demographic_no "
	    			+ " 		AND plr.lab_type = 'HL7' AND plr2.lab_type = 'HL7' "
	    			+ " 		AND plr.status like '%"+status+"%' " + (searchProvider ? " AND plr.provider_no = '"+providerNo+"' " : "")
	    			+ " 		) "
	    			+ " ORDER BY plr.id DESC "
	    			+ " ) AS X "
	    			+ " WHERE X.lab_type = 'HL7' and X.lab_no = info.lab_no "
	    			+ (isPaged ? "	LIMIT " + (page * pageSize) + "," + pageSize : "");
	    	}
	    	else if (patientSearch) { // A
				sql = " SELECT info.label, info.lab_no, info.sex, info.health_no, info.result_status, info.obr_date, info.priority, info.requesting_client, info.discipline, info.last_name, info.first_name, info.report_status, info.accessionNum, info.final_result_count, Z.status "
						+ " FROM hl7TextInfo info, "
						+ " 	(SELECT * FROM "
						+ " 		(SELECT DISTINCT plr.id, plr.lab_type, plr.status, plr.lab_no, d.demographic_no "
						+ " 			FROM providerLabRouting plr, patientLabRouting plr2, demographic d "
						+ " 			WHERE d.first_name like '%"+patientFirstName+"%' AND d.last_name like '%"+patientLastName+"%' AND d.hin like '%"+patientHealthNumber+"%' "
						+ " 				AND plr.lab_no = plr2.lab_no AND plr2.demographic_no = d.demographic_no "
						+ " 				AND plr.lab_type = 'HL7' AND plr2.lab_type = 'HL7' "
						+ " 				AND plr.status like '%"+status+"%' " + (searchProvider ? " AND plr.provider_no = '"+providerNo+"' " : "")
						+ " 		) AS X "
						+ " 		UNION "
						+ " 		(SELECT DISTINCT plr.id, plr.lab_type, plr.status, plr.lab_no, NULL AS demographic_no "
						+ " 			FROM providerLabRouting plr, hl7TextInfo info "
						+ " 			WHERE info.first_name like '%"+patientFirstName+"%' AND info.last_name like '%"+patientLastName+"%' AND info.health_no like '%"+patientHealthNumber+"%' "
						+ " 				AND plr.lab_type = 'HL7' AND plr.lab_no = info.lab_no "
						+ " 				AND plr.status like '%"+status+"%' " + (searchProvider ? " AND plr.provider_no = '"+providerNo+"' " : " ")
						+ " 				AND plr.lab_no NOT IN (SELECT DISTINCT lab_no FROM patientLabRouting WHERE lab_type = 'HL7' AND demographic_no != 0) "
						+ " 		) "
						+ " 		ORDER BY id DESC "
						+ " 	) AS Z "
						+ " WHERE Z.lab_type = 'HL7' and Z.lab_no = info.lab_no "
						+ (isAbnormal != null ? " AND (" + (!isAbnormal ? "info.result_status IS NULL OR" : "") + " info.result_status " + (isAbnormal ? "" : "!") + "= 'A') " : " ")
						+ (isPaged ? "	LIMIT " + (page * pageSize) + "," + pageSize : "");
	    	}
	    	else { // A
	    		sql = " SELECT info.label, info.lab_no, info.sex, info.health_no, info.result_status, info.obr_date, info.priority, info.requesting_client, info.discipline, info.last_name, info.first_name, info.report_status,  info.accessionNum, info.final_result_count, plr.status "
	    			+ " FROM providerLabRouting plr, hl7TextInfo info "
	    			+ " WHERE plr.status like '%"+status+"%' " + (searchProvider ? " AND plr.provider_no = '"+providerNo+"' " : "")
	    			+ "   AND lab_type = 'HL7' and info.lab_no = plr.lab_no "
	    			+ (isAbnormal != null ? " AND (" + (!isAbnormal ? "info.result_status IS NULL OR" : "") + " info.result_status " + (isAbnormal ? "" : "!") + "= 'A') " : " ")
	    			+ " ORDER BY plr.id DESC "
	    			+ (isPaged ? "	LIMIT " + (page * pageSize) + "," + pageSize : "");
	    	}
	    }
	    Query query = entityManager.createNativeQuery(sql);
	    return query.getResultList();
    }

	public List<Object> findDisciplines(Integer labid) {
	    String sql = "SELECT DISTINCT i.discipline FROM " + modelClass.getName() + " i WHERE i.discipline <> '' AND i.labNumber = :labid";
		Query query = entityManager.createQuery(sql);
		query.setParameter("labid", labid);
		return query.getResultList();
		
    }
	
	public List<Hl7TextInfo> findByFillerOrderNumber(String fillerOrderNum) {
	    String sql = "SELECT h FROM " + modelClass.getName() + " h WHERE h.fillerOrderNum = ?1 ORDER BY id";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, fillerOrderNum);
		return query.getResultList();
		
    }
}
