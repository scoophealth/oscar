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


package oscar.oscarLab;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.oscarehr.util.SpringUtils;
import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.oscarMeasurements.dao.MeasurementsExtDao;
import oscar.oscarEncounter.oscarMeasurements.model.MeasurementsExt;
import oscar.util.StringUtils;
import oscar.util.UtilDateUtilities;

public class LabRequestReportLink {
	private static MeasurementsExtDao measurementsExtDao = null;
	
	static {
		measurementsExtDao = (MeasurementsExtDao) SpringUtils.getBean("measurementsExtDao");
	}
    
	public static HashMap<String,Object> getLinkByReport(String reportTable, Long reportId) throws SQLException {
		HashMap<String,Object> link = new HashMap<String,Object>();

		String sql = "SELECT * FROM labRequestReportLink WHERE report_table='" + reportTable+"'" +
					 " AND report_id="  + reportId;
		ResultSet rs = DBHandler.GetSQL(sql);
		if (rs.next()) {
			link.put("id", rs.getLong("id"));
			link.put("request_table", rs.getString("request_table"));
			link.put("request_id", rs.getLong("request_id"));
			link.put("request_date", rs.getTimestamp("request_date"));
			link.put("report_table", reportTable);
			link.put("report_id", reportId);
		}
		return link;
	}

	public static HashMap<String,Object> getLinkByRequestId(String requestTable, Long reqId) throws SQLException {
		HashMap<String,Object> link = new HashMap<String,Object>();

		String sql = "SELECT * FROM labRequestReportLink WHERE request_table='" + requestTable+"'" +
					 " AND request_id="  + reqId;
		ResultSet rs = DBHandler.GetSQL(sql);
		if (rs.next()) {
			link.put("id", rs.getLong("id"));
			link.put("request_table", rs.getString("request_table"));
			link.put("request_id", rs.getLong("request_id"));
			link.put("request_date", rs.getTimestamp("request_date"));
			link.put("report_table", rs.getString("report_table"));
			link.put("report_id", rs.getLong("report_id"));
		}
		return link;
	}

	public static String getRequestDate(String id) throws SQLException {
	
		String sql = "SELECT request_date FROM labRequestReportLink WHERE id=" + id;
		ResultSet rs = DBHandler.GetSQL(sql);
		Date requestDate = null;
		if (rs.next()) {
			requestDate = rs.getTimestamp(1);
		}
		return UtilDateUtilities.DateToString(requestDate,"yyyy-MM-dd HH:mm:ss");
	}

	public static Long getIdByReport(String reportTable, Long reportId) throws SQLException {
		HashMap<String,Object> link = getLinkByReport(reportTable, reportId);
		return (Long)link.get("id");
	}

	public static Long getRequestTableIdByReport(String reportTable, Long reportId) throws SQLException {
		HashMap<String,Object> link = getLinkByReport(reportTable, reportId);
		Long requestId = (Long)link.get("request_id");
		if (requestId==null || requestId==0) requestId = null;
		return requestId;
	}
	
	public static void save(String requestTable, Long requestId, String requestDate, String reportTable, Long reportId) throws SQLException {
		if (StringUtils.empty(reportTable) || reportId==null) return;
		
		if (StringUtils.empty(requestDate)) requestDate=null;
		String requestDateString="null";
		if (requestDate!=null) requestDateString="'"+requestDate+"'";

		String sql = "INSERT INTO labRequestReportLink (request_table,request_id,request_date,report_table,report_id) VALUES ('" +
					 requestTable+"',"+requestId+","+requestDateString+",'"+reportTable+"',"+reportId+")";
		DBHandler.RunSQL(sql);
		
		Integer measurementId = getMeasurementIdFromExt(reportTable, reportId.toString());
		MeasurementsExt mExt = getRequestDate_MeasurementsExt(measurementId);
		if (mExt==null && measurementId!=null) {
			saveRequestDate_MeasurementsExt(requestDate, measurementId);
		}
	}

	public static void update(Long id, String requestTable, Long requestId, String requestDate) throws SQLException {
		if (id==null) return;
		
		String sql = "UPDATE labRequestReportLink SET request_table='" + requestTable + "'" +
					 " AND request_id=" + requestId +
					 " AND request_date='" + requestDate + "'" +
					 " WHERE id=" + id;
		DBHandler.RunSQL(sql);
		
		//update request_datetime in measurementsExt
		HashMap<String,Object> link = getLinkByRequestId(requestTable, requestId);
		String reportTbl = (String) link.get("report_table");
		String reportId = (String) link.get("report_id");
		
		Integer measurementId = getMeasurementIdFromExt(reportTbl, reportId);
		MeasurementsExt mExt = getRequestDate_MeasurementsExt(measurementId);
		
		if (mExt!=null && getRequestDate(id.toString()).equals(mExt.getVal())) {
			saveRequestDate_MeasurementsExt(requestDate, measurementId);
		}
	}

    private static void saveRequestDate_MeasurementsExt(String requestDate, Integer measurementId) {
		if (requestDate==null) return;
		
		Date dRequestDate = UtilDateUtilities.StringToDate(requestDate, "yyyy-MM-dd HH:mm:ss");
		if (dRequestDate==null) requestDate += " 00:00:00";

		MeasurementsExt mExt = getRequestDate_MeasurementsExt(measurementId);
		if (mExt==null) {
			mExt = new MeasurementsExt(measurementId);
			mExt.setKeyVal("request_datetime");
		}
		mExt.setVal(requestDate);
		measurementsExtDao.getHibernateTemplate().saveOrUpdate(mExt);
	}
	
	private static MeasurementsExt getRequestDate_MeasurementsExt(Integer measurementId) {
		List<MeasurementsExt> l_mExt = measurementsExtDao.getMeasurementsExtByMeasurementId(measurementId);
		for (MeasurementsExt mExt : l_mExt) {
			if (mExt.getKeyVal().equals("request_datetime")) {
				return mExt;
			}
		}
		return null;
	}
	
	private static String getRequestDate_MeasurementsExt(String reportTable, String reportId) {
		Integer measurementId = getMeasurementIdFromExt(reportTable, reportId);
		MeasurementsExt mExt = getRequestDate_MeasurementsExt(measurementId);
		
		if (mExt!=null) return mExt.getVal();
		return null; 
	}
	
	private static Integer getMeasurementIdFromExt(String reportTable, String reportId) {
		String key = "lab_no";
		if ("labPatientPhysicianInfo".equalsIgnoreCase(reportTable)) key = "lab_ppid";
		
		return measurementsExtDao.getMeasurementIdByKeyValue(key, reportId);
	}
}
