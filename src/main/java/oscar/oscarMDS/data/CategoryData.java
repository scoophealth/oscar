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


package oscar.oscarMDS.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.oscarehr.util.DbConnectionFilter;

public class CategoryData {

	private int totalDocs = 0;
	private int totalLabs = 0;
	private int unmatchedLabs = 0;
	private int unmatchedDocs = 0;
    private int totalNumDocs = 0;
    private int abnormalCount = 0;
    private int normalCount = 0;
    private HashMap<Integer,PatientInfo> patients;

	public int getTotalDocs() {
		return totalDocs;
	}

	public int getTotalLabs() {
		return totalLabs;
	}

	public int getUnmatchedLabs() {
		return unmatchedLabs;
	}

	public int getUnmatchedDocs() {
		return unmatchedDocs;
	}

	public int getTotalNumDocs() {
		return totalNumDocs;
	}

	public int getAbnormalCount() {
		return abnormalCount;
	}

	public int getNormalCount() {
		return normalCount;
	}

	public HashMap<Integer, PatientInfo> getPatients() {
		return patients;
	}

	private String patientLastName;
	private String searchProviderNo;
	private String status;
	private String patientFirstName;
	private String patientHealthNumber;
	private boolean patientSearch;
	private boolean providerSearch;

	public CategoryData(String patientLastName, String patientFirstName, String patientHealthNumber, boolean patientSearch,
					    boolean providerSearch, String searchProviderNo, String status)  {

		this.patientLastName = patientLastName;
		this.searchProviderNo = searchProviderNo;
		this.status = status;
		this.patientFirstName = patientFirstName;
		this.patientHealthNumber = patientHealthNumber;
		this.patientSearch = patientSearch;
		this.providerSearch = providerSearch;

    	totalDocs = 0;
		totalLabs = 0;
		unmatchedLabs = 0;
	    unmatchedDocs = 0;
	    totalNumDocs = 0;
	    abnormalCount = 0;
	    normalCount = 0;

        patients = new HashMap<Integer,PatientInfo>();

	}
	public void populateCountsAndPatients() throws SQLException {

		// Retrieving documents and labs.
		totalDocs += getDocumentCountForPatientSearch();
        totalLabs += getLabCountForPatientSearch();

        // If this is not a patient search, then we need to find the unmatched documents.
        if (!patientSearch) {
            unmatchedDocs += getDocumentCountForUnmatched();
            unmatchedLabs += getLabCountForUnmatched();
            totalDocs += unmatchedDocs;
            totalLabs += unmatchedLabs;
        }

        // The total overall items is the sum of docs and labs.
        totalNumDocs = totalDocs + totalLabs;

        // Retrieving abnormal labs.
        abnormalCount = getAbnormalCount(true);

        // Cheaper to subtract abnormal from total to find the number of normal docs.
        normalCount = totalNumDocs - abnormalCount;
	}

	public int getLabCountForUnmatched()
			throws SQLException {
		String sql;
		              sql = " SELECT HIGH_PRIORITY COUNT(1) as count "
				                       + " FROM patientLabRouting plr2, providerLabRouting plr  "
				                       + " WHERE plr.lab_no = plr2.lab_no "
				                       + (providerSearch ? " AND plr.provider_no = '"+searchProviderNo+"' " : "")
				
				                        + "   AND plr.lab_type = 'HL7' "
				                        + "   AND plr.status like '%"+status+"%' "
				                       + "   AND plr2.lab_type = 'HL7'"
				                       + "   AND plr2.demographic_no = '0' ";
				

			
		Connection c  = DbConnectionFilter.getThreadLocalDbConnection();
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs= ps.executeQuery(sql);

		return (rs.next() ? rs.getInt("count") : 0);
	}

	public int getAbnormalCount(boolean isAbnormal) throws SQLException {
		ResultSet rs;
		String sql;
		if (patientSearch) {
        	sql = " SELECT HIGH_PRIORITY COUNT(1) as count "
        		+ " FROM patientLabRouting cd, demographic d, providerLabRouting plr, hl7TextInfo info "
        		+ " WHERE d.last_name like '%"+patientLastName+"%'  "
        		+ " 	AND d.first_name like '%"+patientFirstName+"%' "
        		+ " 	AND d.hin like '%"+patientHealthNumber+"%' "
        		+ " 	AND plr.status like '%"+status+"%' "
        		+ (providerSearch ? "AND plr.provider_no = '"+searchProviderNo+"' " : "")
        		+ " 	AND plr.lab_type = 'HL7' "
        		+ " 	AND cd.lab_type = 'HL7' "
        		+ " 	AND cd.lab_no = plr.lab_no "
        		+ " 	AND cd.demographic_no = d.demographic_no "
        		+ " 	AND info.lab_no = plr.lab_no "
        		+ " 	AND result_status "+(isAbnormal ? "" : "!")+"= 'A' ";
        }
        else if (providerSearch || !"".equals(status)){ // providerSearch
        	sql = "SELECT HIGH_PRIORITY COUNT(1) as count "
				+ " FROM providerLabRouting plr, hl7TextInfo info "
				+ " WHERE plr.status like '%"+status+"%' "
				+ (providerSearch ? " AND plr.provider_no = '"+searchProviderNo+"' " : " ")
				+ " AND plr.lab_type = 'HL7'  "
				+ " AND info.lab_no = plr.lab_no"
				+ " AND result_status "+(isAbnormal ? "" : "!")+"= 'A' ";
        }
        else {
        	sql = " SELECT HIGH_PRIORITY COUNT(1) as count "
            	+ " FROM hl7TextInfo info "
            	+ " WHERE result_status "+(isAbnormal ? "" : "!")+"= 'A' ";
        }
		Connection c  = DbConnectionFilter.getThreadLocalDbConnection();
		PreparedStatement ps = c.prepareStatement(sql);
		rs= ps.executeQuery(sql);
        return (rs.next() ? rs.getInt("count") : 0);
	}

	public int getDocumentCountForUnmatched()
			throws SQLException {
		String sql = " SELECT HIGH_PRIORITY COUNT(1) as count "
					+ " FROM ctl_document cd, providerLabRouting plr "
					+ " WHERE plr.lab_type = 'DOC' AND plr.status like '%"+status+"%' "
					+ (providerSearch ? " AND plr.provider_no ='"+searchProviderNo+"' " : "")
					+ "	AND plr.lab_no = cd.document_no"
					+ " AND 	cd.module_id = -1 ";
		Connection c  = DbConnectionFilter.getThreadLocalDbConnection();
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs= ps.executeQuery(sql);
		return (rs.next() ? rs.getInt("count") : 0);
	}

	public int getLabCountForPatientSearch() throws SQLException {
		PatientInfo info;
		String sql = " SELECT HIGH_PRIORITY d.demographic_no, last_name, first_name, COUNT(1) as count "
        	+ " FROM patientLabRouting cd,  demographic d, providerLabRouting plr "
        	+ " WHERE   d.last_name like '%"+patientLastName+"%' "
        	+ " 	AND d.first_name like '%"+patientFirstName+"%' "
        	+ " 	AND d.hin like '%"+patientHealthNumber+"%' "
        	+ " 	AND cd.demographic_no = d.demographic_no "
        	+ " 	AND cd.lab_no = plr.lab_no "
        	+ " 	AND plr.lab_type = 'HL7' "
        	+ " 	AND cd.lab_type = 'HL7' "
        	+ " 	AND plr.status like '%"+status+"%' "
        	+ (providerSearch ? "AND plr.provider_no = '"+searchProviderNo+"' " : "")
        	+ " GROUP BY demographic_no ";

		Connection c  = DbConnectionFilter.getThreadLocalDbConnection();
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs= ps.executeQuery(sql);
        int count = 0;
        while(rs.next()){
        	int id = rs.getInt("demographic_no");
        	// Updating patient info if it already exists.
        	if (patients.containsKey(id)) {
        		info = patients.get(id);
        		info.setLabCount(rs.getInt("count"));
        	}
        	// Otherwise adding a new patient record.
        	else {
        		info = new PatientInfo(id, rs.getString("first_name"), rs.getString("last_name"));
        		info.setLabCount(rs.getInt("count"));
        		patients.put(info.id, info);
        	}
        	count += info.getLabCount();
        }
        return count;
	}

	public int getLabCountForDemographic(String demographicNo) throws SQLException {
		String sql = " SELECT HIGH_PRIORITY d.demographic_no, last_name, first_name, COUNT(1) as count "
        	+ " FROM patientLabRouting cd,  demographic d, providerLabRouting plr "
        	+ " WHERE   d.demographic_no = " + demographicNo
        	+ " 	AND cd.demographic_no = d.demographic_no "
        	+ " 	AND cd.lab_no = plr.lab_no "
        	+ " 	AND plr.lab_type = 'HL7' "
        	+ " 	AND cd.lab_type = 'HL7' "
        	+ " 	AND plr.status like '%"+status+"%' "
        	+ (providerSearch ? "AND plr.provider_no = '"+searchProviderNo+"' " : "")
        	+ " GROUP BY demographic_no ";

		Connection c  = DbConnectionFilter.getThreadLocalDbConnection();
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs= ps.executeQuery(sql);
        return (rs.next() ? rs.getInt("count") : 0);
	}

	/*
	 * This will return the total documents found for this patients.
	 * it will also add to the patients map (demographicNo, PatientInfo) with a document count for the patient.
	 * 
	 */
	public int getDocumentCountForPatientSearch() throws SQLException {
		PatientInfo info;
		String sql = " SELECT HIGH_PRIORITY demographic_no, last_name, first_name, COUNT( distinct cd.document_no) as count "
					+ " FROM ctl_document cd, demographic d, providerLabRouting plr "
					+ " WHERE   d.last_name like '%"+patientLastName+"%'  "
					+ " 	AND d.hin like '%"+patientHealthNumber+"%' "
					+ " 	AND d.first_name like '%"+patientFirstName+"%' "
					+ " 	AND cd.module_id = d.demographic_no "
					+ " 	AND cd.document_no = plr.lab_no "
					+ " 	AND plr.lab_type = 'DOC' "
					+ " 	AND plr.status like '%"+status+"%' "
					+ (providerSearch ? "AND plr.provider_no = '"+searchProviderNo+"' " : "")
					+ " GROUP BY demographic_no ";
		Connection c  = DbConnectionFilter.getThreadLocalDbConnection();
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs= ps.executeQuery(sql);
        int count = 0;
        while(rs.next()){
        	info = new PatientInfo(rs.getInt("demographic_no"), rs.getString("first_name"), rs.getString("last_name"));
        	info.setDocCount(rs.getInt("count"));
        	patients.put(info.id, info);
        	count += info.getDocCount();
        }
        return count;
	}


	public Long getCategoryHash() {
		return Long.valueOf("" + (int)'A' + totalNumDocs)
			 + Long.valueOf("" + (int)'D' + totalDocs)
			 + Long.valueOf("" + (int)'L' + totalLabs);
	}
}
