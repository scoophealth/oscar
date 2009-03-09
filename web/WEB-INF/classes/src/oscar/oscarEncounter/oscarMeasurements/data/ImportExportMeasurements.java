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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import oscar.oscarDB.DBHandler;

public class ImportExportMeasurements {
    
    public static List getMeasurements(String demoNo) throws SQLException {
	List measList = new ArrayList();
	if (filled(demoNo)) {
	    DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	    String sql = "SELECT * FROM measurements WHERE demographicNo=" + demoNo;
	    ResultSet rs = db.GetSQL(sql);

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
    
    public static void saveMeasurements(String type, String demoNo, String providerNo, String dataField, Date dateObserved) throws SQLException {
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	String sql = "SELECT measuringInstruction FROM measurementType WHERE type='"+type+"' LIMIT 1";
	ResultSet rs = db.GetSQL(sql);
	String mi = rs.next() ? rs.getString("measuringInstruction") : "";
	saveMeasurements(type, demoNo, providerNo, dataField, mi, dateObserved);
    }
    
    public static void saveMeasurements(String type, String demoNo, String providerNo, String dataField, String measuringInstruction, Date dateObserved) throws SQLException {
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	String sql = "INSERT INTO measurements (demographicNo, type, providerNo, dataField, measuringInstruction, dateObserved, dateEntered) VALUES (" +
		    demoNo + ",'" + type.toUpperCase() + "','" + providerNo + "','" + dataField + "','" + measuringInstruction + "','" + dateObserved + "','" + new Date() + "')";
	db.RunSQL(sql);
    }
    
    private static List getMeasurementsExt(Long measurementId) throws SQLException {
	List extsList = new ArrayList();
	if (measurementId!=null) {
	    DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	    String sql = "SELECT * FROM measurementsExt WHERE measurement_id=" + measurementId;
	    ResultSet rs = db.GetSQL(sql);

	    while (rs.next()) {
		MeasurementsExt exts = new MeasurementsExt(measurementId);
		exts.setId(rs.getLong("id"));
		exts.setMeasurementId(rs.getLong("measurement_id"));
		exts.setKeyVal(rs.getString("keyval"));
		exts.setVal(rs.getString("val"));
		extsList.add(exts);
	    }
	}
	return extsList;
    }
    
    private static boolean filled(String s) {
	return (s!=null && s.trim().length()>0);
    }
}
