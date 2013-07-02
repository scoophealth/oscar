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

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.oscarehr.common.dao.LabRequestReportLinkDao;
import org.oscarehr.common.dao.MeasurementsExtDao;
import org.oscarehr.common.model.MeasurementsExt;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;
import oscar.util.StringUtils;
import oscar.util.UtilDateUtilities;

public class LabRequestReportLink {
	private static LabRequestReportLinkDao dao = SpringUtils.getBean(LabRequestReportLinkDao.class);
	private static MeasurementsExtDao measurementsExtDao = SpringUtils.getBean(MeasurementsExtDao.class);
    
	public static HashMap<String,Object> getLinkByReport(String reportTable, Long reportId) {
		HashMap<String,Object> link = new HashMap<String,Object>();

		List<org.oscarehr.common.model.LabRequestReportLink> results = dao.findByReportTableAndReportId(reportTable,reportId.intValue());
		for(org.oscarehr.common.model.LabRequestReportLink l:results) {
			link.put("id", l.getId().longValue());
			link.put("request_table", l.getRequestTable());
			link.put("request_id", new Long(l.getRequestId()));
			link.put("request_date", l.getRequestDate());
			link.put("report_table", reportTable);
			link.put("report_id", reportId);
		}
		
		
		return link;
	}

	public static HashMap<String,Object> getLinkByRequestId(String requestTable, Long reqId) {
		HashMap<String,Object> link = new HashMap<String,Object>();

		List<org.oscarehr.common.model.LabRequestReportLink> results = dao.findByRequestTableAndRequestId(requestTable,reqId.intValue());
		for(org.oscarehr.common.model.LabRequestReportLink l:results) {
			link.put("id", l.getId().longValue());
			link.put("request_table", l.getRequestTable());
			link.put("request_id", new Long(l.getRequestId()));
			link.put("request_date", l.getRequestDate());
			link.put("report_table", l.getReportTable());
			link.put("report_id", new Long(l.getReportId()));
		}
	
		return link;
	}

	public static String getRequestDate(String id) {
		Date requestDate = null;
		org.oscarehr.common.model.LabRequestReportLink l = dao.find(Integer.parseInt(id));
		if(l != null) {
			requestDate = l.getRequestDate();
		}
		
		return UtilDateUtilities.DateToString(requestDate,"yyyy-MM-dd HH:mm:ss");
	}

	public static Long getIdByReport(String reportTable, Long reportId)  {
		HashMap<String,Object> link = getLinkByReport(reportTable, reportId);
		return (Long)link.get("id");
	}

	public static Long getRequestTableIdByReport(String reportTable, Long reportId)  {
		HashMap<String,Object> link = getLinkByReport(reportTable, reportId);
		Long requestId = (Long)link.get("request_id");
		if (requestId==null || requestId==0) requestId = null;
		return requestId;
	}
	
	public static void save(String requestTable, Long requestId, String requestDate, String reportTable, Long reportId) {
		if (StringUtils.empty(reportTable) || reportId==null) return;
		if (StringUtils.empty(requestDate)) requestDate=null;
		
		org.oscarehr.common.model.LabRequestReportLink l = new org.oscarehr.common.model.LabRequestReportLink();
		l.setRequestTable(requestTable);
		l.setRequestId(requestId == null ? null : requestId.intValue());
		l.setRequestDate(ConversionUtils.fromTimestampString(requestDate));
		l.setReportTable(reportTable);
		l.setReportId(reportId.intValue());
		
		dao.persist(l);
		
		Integer measurementId = getMeasurementIdFromExt(reportTable, reportId.toString());
		MeasurementsExt mExt = getRequestDate_MeasurementsExt(measurementId);
		if (mExt==null && measurementId!=null) {
			saveRequestDate_MeasurementsExt(requestDate, measurementId);
		}
	}

	public static void update(Long id, String requestTable, Long requestId, String requestDate) {
		if (id==null) return;
		
		org.oscarehr.common.model.LabRequestReportLink l = dao.find(id.intValue());
		if(l != null) {
			l.setRequestTable(requestTable);
			l.setRequestId(requestId.intValue());
			l.setRequestDate(ConversionUtils.fromTimestampString(requestDate));
			dao.merge(l);
		}
		
		
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
			mExt.setVal(requestDate);
			measurementsExtDao.persist(mExt);
		} else {
			mExt.setVal(requestDate);
			measurementsExtDao.merge(mExt);
		}
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
	
	private static Integer getMeasurementIdFromExt(String reportTable, String reportId) {
		String key = "lab_no";
		if ("labPatientPhysicianInfo".equalsIgnoreCase(reportTable)) key = "lab_ppid";
		
		return measurementsExtDao.getMeasurementIdByKeyValue(key, reportId);
	}
}
