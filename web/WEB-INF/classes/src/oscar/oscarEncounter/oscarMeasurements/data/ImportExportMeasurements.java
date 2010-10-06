/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada   Creates a new instance of Prevention
 *
 * ImportExportMeasurements.java
 *
 * Created on Feb 10, 2009
 *
 */

package oscar.oscarEncounter.oscarMeasurements.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.oscarehr.util.DbConnectionFilter;

import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.oscarMeasurements.model.MeasurementsExt;

public class ImportExportMeasurements {
    
    public static List getMeasurements(String demoNo) throws SQLException {
	List measList = new ArrayList();
	if (filled(demoNo)) {
	    
	    String sql = "SELECT * FROM measurements WHERE demographicNo=" + demoNo;
	    ResultSet rs = DBHandler.GetSQL(sql);

	    while (rs.next()) {
		Measurements meas = new Measurements(Long.valueOf(demoNo));
		meas.setId(rs.getLong("id"));
		meas.setType(rs.getString("type"));
		meas.setProviderNo(rs.getString("providerNo"));
		meas.setDataField(rs.getString("dataField"));
		meas.setMeasuringInstruction(rs.getString("measuringInstruction"));
		meas.setComments(rs.getString("comments"));
		meas.setDateObserved(rs.getDate("dateObserved"));
		meas.setDateEntered(rs.getDate("dateEntered"));
		measList.add(meas);
	    }
	}
	return measList;
    }
    
    public static List getLabMeasurements(String demoNo) throws SQLException {
	List labmList = new ArrayList();
	
	List<Measurements> measList = getMeasurements(demoNo);
	for (Measurements ms : measList) {
	    List mExt = getMeasurementsExt(ms.getId());
	    if (!mExt.isEmpty()) {
		LabMeasurements labm = new LabMeasurements();
		labm.setMeasure(ms);
		labm.setExts(mExt);
		labmList.add(labm);
	    }
	}
	return labmList;
    }
    
    public static Long saveMeasurements(String type, String demoNo, String providerNo, String dataField, Date dateObserved) throws SQLException {
	String sql = "SELECT measuringInstruction FROM measurementType WHERE type='"+type+"' LIMIT 1";
	
	ResultSet rs = DBHandler.GetSQL(sql);
	String mi = rs.next() ? rs.getString("measuringInstruction") : "";
	return saveMeasurements(type, demoNo, providerNo, dataField, mi, dateObserved);
    }
    
    public static Long saveMeasurements(String type, String demoNo, String providerNo, String dataField, String measuringInstruction, Date dateObserved) throws SQLException {
	Long id = null;
	if (dateObserved==null) dateObserved = new Date();
	String sql = "INSERT INTO measurements (demographicNo, type, providerNo, dataField, measuringInstruction, dateObserved, dateEntered)" +
				      " VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
	PreparedStatement pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, demoNo);
	pstmt.setString(2, type.toUpperCase());
	pstmt.setString(3, providerNo);
	pstmt.setString(4, dataField);
	pstmt.setString(5, measuringInstruction);
	pstmt.setDate(6, new java.sql.Date(dateObserved.getTime()));
	pstmt.setDate(7, new java.sql.Date(new Date().getTime()));
	pstmt.executeUpdate();
	ResultSet rs = pstmt.getGeneratedKeys();
	if (rs.next()) id = rs.getLong(1);
	pstmt.close();
	conn.close();
	return id;
    }
    
    public static void saveMeasurements(Measurements meas) throws SQLException {
	
        String sql=null, mi=meas.getMeasuringInstruction();
        if (!filled(mi)) {
            sql = "SELECT measuringInstruction FROM measurementType WHERE type='"+meas.getType()+"' LIMIT 1";
            ResultSet rs = DBHandler.GetSQL(sql);
            mi = rs.next() ? rs.getString("measuringInstruction") : "";
        }
	sql = "INSERT INTO measurements (demographicNo, type, providerNo, dataField, measuringInstruction, dateObserved, dateEntered)" +
			       " VALUES (?, ?, ?, ?, ?, ?, ?)";
	if (meas.getDateObserved()==null) meas.setDateObserved(new Date());
	if (meas.getDateEntered()==null) meas.setDateEntered(new Date());
	if (meas.getType()==null) meas.setType("");
	Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
	PreparedStatement pstmt = conn.prepareStatement(sql);
	pstmt.setLong(1, meas.getDemographicNo());
	pstmt.setString(2, meas.getType());
	pstmt.setString(3, meas.getProviderNo());
	pstmt.setString(4, meas.getDataField());
	pstmt.setString(5, mi);
	pstmt.setDate(6, new java.sql.Date(meas.getDateObserved().getTime()));
	pstmt.setDate(7, new java.sql.Date(meas.getDateEntered().getTime()));
	pstmt.executeUpdate();
	ResultSet rs = pstmt.getGeneratedKeys();
	if (rs.next()) meas.setId(rs.getLong(1));
	pstmt.close();
	conn.close();
    }
    
    public static void saveMeasurementsExt(MeasurementsExt mExt) throws SQLException {
        String sql = "INSERT INTO measurementsExt (measurement_id,keyval,val) VALUES (?,?,?)";
        
	Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
	PreparedStatement pstmt = conn.prepareStatement(sql);
	pstmt.setLong(1, mExt.getMeasurementId());
	pstmt.setString(2, mExt.getKeyVal());
	pstmt.setString(3, mExt.getVal());
	pstmt.executeUpdate();
	ResultSet rs = pstmt.getGeneratedKeys();
	if (rs.next()) mExt.setId(rs.getInt(1));
	pstmt.close();
	conn.close();
    }
    
    public static List getMeasurementsExt(Long measurementId) throws SQLException {
	List extsList = new ArrayList();
	if (measurementId!=null) {
	    
	    String sql = "SELECT * FROM measurementsExt WHERE measurement_id=" + measurementId;
	    ResultSet rs = DBHandler.GetSQL(sql);

	    while (rs.next()) {
		MeasurementsExt exts = new MeasurementsExt(measurementId.intValue());
		exts.setId(rs.getInt("id"));
		exts.setMeasurementId(rs.getInt("measurement_id"));
		exts.setKeyVal(rs.getString("keyval"));
		exts.setVal(rs.getString("val"));
		extsList.add(exts);
                
	    }
	}
	return extsList;
    }
    
    public static MeasurementsExt getMeasurementsExtByKeyval(Long measurementId, String keyval) throws SQLException {
	MeasurementsExt measurementsExt = null;
	if (measurementId!=null) {
	    
	    String sql = "SELECT * FROM measurementsExt WHERE measurement_id=" + measurementId + " AND keyval='" + keyval + "'";
	    ResultSet rs = DBHandler.GetSQL(sql);

	    if (rs.next()) {
		measurementsExt = new MeasurementsExt(measurementId.intValue());
		measurementsExt.setId(rs.getInt("id"));
		measurementsExt.setMeasurementId(rs.getInt("measurement_id"));
		measurementsExt.setKeyVal(rs.getString("keyval"));
		measurementsExt.setVal(rs.getString("val"));
	    }
	}
	return measurementsExt;
    }
    
    private static boolean filled(String s) {
	return (s!=null && s.trim().length()>0);
    }
}
