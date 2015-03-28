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
package org.oscarehr.integration.born;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;
import org.oscarehr.common.dao.BornTransmissionLogDao;
import org.oscarehr.common.model.BornTransmissionLog;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class ONAREnhancedBornConnector {

	Logger logger = MiscUtils.getLogger();
	
	public ONAREnhancedBornConnector() {
		
	}

	public void updateToSent(int formId) throws Exception {		
		Connection conn = org.oscarehr.util.DbConnectionFilter.getThreadLocalDbConnection();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select formEdited from formONAREnhancedRecord where id="+formId);
			Timestamp ts = null;
			if(rs.next()) {
				ts = rs.getTimestamp("formEdited");
			}
			if(ts == null) {
				MiscUtils.getLogger().warn("This shouldn't happen. Unabled to update flag as sent to born.");
				return;
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int res = st.executeUpdate("update formONAREnhancedRecord set sent_to_born=1,formEdited='"+formatter.format(ts)+"' where id="+formId);
			st.close();
		}finally {
			//conn.close();
		}
	}
	
	public String getFileSuffix()  {
		Connection conn=null;
		Statement st=null;
		int num = 0;
		try {
			conn = org.oscarehr.util.DbConnectionFilter.getThreadLocalDbConnection();
			
			st = conn.createStatement();
			String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			ResultSet rs = st.executeQuery("select count(*) as count from BornTransmissionLog where submitDateTime >= '" + today + " 00:00:00' and submitDateTime <= '" + today + " 23:59:59'");
			if(rs.next()) {
				num = rs.getInt("count");
			}
		}catch(Exception e) {
			MiscUtils.getLogger().error("Error",e);
		}finally {
			if(st != null)
				try {
					st.close();
				}catch(SQLException e){}
			if(conn != null)
				try {
					conn.close();
				}catch(SQLException e){}
		}
		num++;
		String tmp = String.valueOf(num);
		while(tmp.length() <3) {tmp = "0"+tmp;}
		return tmp;
	}
	
	public String updateBorn(LoggedInInfo loggedInInfo) throws Exception {
		String filename = generateFilename();
		
		Connection conn = org.oscarehr.util.DbConnectionFilter.getThreadLocalDbConnection();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select demographic_no,id,formEdited,c_finalEDB,sent_to_born,pg1_signature,pg2_signature,episodeId,c_postal from (select r.demographic_no,r.id,r.formEdited,r.c_finalEDB,r.sent_to_born,r.pg1_signature,r2.pg2_signature,r.episodeId,r.c_postal from formONAREnhancedRecord r,formONAREnhancedRecordExt2 r2 where r.ID = r2.ID and c_finalEDB!='' AND c_finalEDB IS NOT NULL ORDER BY formEdited DESC) AS x  GROUP BY demographic_no");
		
		ONAREnhancedFormToXML xml = new ONAREnhancedFormToXML();
		XmlOptions opts = getXmlOptions();
		
		String tmpPath = System.getProperty("java.io.tmpdir");
		int total = 0;		
		OutputStream os = null;
		PrintWriter pw = null;
		List<Integer> formIdsSent = new ArrayList<Integer>();
		
		try {
			os = new FileOutputStream(tmpPath + File.separator + filename);
			pw = new PrintWriter(os,true);
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<ARRecordSet xmlns=\"http://www.oscarmcmaster.org/AR2005\">");
			
			
			while(rs.next()) {
				try {
					int demographicNo = rs.getInt("demographic_no");
					int id = rs.getInt("id");
					boolean sent = rs.getBoolean("sent_to_born");
					String pg1Signature = rs.getString("pg1_signature");
					String pg2Signature = rs.getString("pg2_signature");
					int episodeId = rs.getInt("episodeId");
					String postalCode = rs.getString("c_postal");
					if(postalCode == null || postalCode.length() == 0) {
						continue;
					}
					//rs.getString("c_finalEDB");
					/*
					if(pg1Signature== null || pg2Signature==null || pg1Signature.length()==0 || pg2Signature.length()==0) {
						continue;
					}
					*/
					if(!sent) {
						MiscUtils.getLogger().info("Adding form "+ id +" for patient " + demographicNo);
						if(xml.addXmlToStream(loggedInInfo, pw,opts, null, String.valueOf(demographicNo), id, episodeId)) {				
							total++;
							formIdsSent.add(id);
						}
					}
				}catch(Exception e) {
					MiscUtils.getLogger().warn("Unable to add record",e);
				}
			}
			
			pw.println("</ARRecordSet>");			
		} finally {
			if(pw != null) {
				pw.flush();
				pw.close();
			}
		}
		
		if(total == 0) {
			MiscUtils.getLogger().info("No new forms found");
			return null;
		}
		
		
		File file = new File(tmpPath + File.separator + filename);
		
		
		String path = file.getPath();
		if(path.indexOf(File.separator)!=-1)
			path = path.substring(0,path.lastIndexOf(File.separator));
		else
			path="";
		
		BornFtpManager.uploadONAREnhancedDataToRepository(path, file.getName());

		for(Integer formId:formIdsSent) {
			updateToSent(formId);
		}
		//backup original file
		String documentDir = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		
		if(documentDir != null && new File(documentDir).exists()) {
			FileUtils.copyFileToDirectory(new File(tmpPath + File.separator + filename), new File(documentDir));
		} else {
			MiscUtils.getLogger().warn("Unabled to backup file to document dir");
		}
		
		//log it
		BornTransmissionLogDao logDao = SpringUtils.getBean(BornTransmissionLogDao.class); 
		BornTransmissionLog log = new BornTransmissionLog();
		log.setFilename(filename);
		log.setSubmitDateTime(new Date());
		log.setSuccess(true);
		logDao.persist(log);
		
		
		rs.close();
		st.close();
		conn.close();
		
		return filename;
	}
	
	public XmlOptions getXmlOptions() {
		HashMap<String,String> suggestedPrefixes = new HashMap<String,String>();
		suggestedPrefixes.put("http://www.oscarmcmaster.org/AR2005", "");
		suggestedPrefixes.put("http://www.w3.org/2001/XMLSchema-instance","xsi");
		XmlOptions opts = new XmlOptions();
		opts.setSaveSuggestedPrefixes(suggestedPrefixes);
		opts.setSavePrettyPrint();
		opts.setSaveNoXmlDecl();
		opts.setUseDefaultNamespace();
		Map<String,String> implicitNamespaces = new HashMap<String,String>();
		implicitNamespaces.put("","http://www.oscarmcmaster.org/AR2005");
		opts.setSaveImplicitNamespaces(implicitNamespaces);
		opts.setSaveNamespacesFirst();
		opts.setCharacterEncoding("UTF-16");
		
		return opts;
	}
	
	private String generateFilename() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dt = sdf.format(new Date());				
		String filename =  "/BORN_"+OscarProperties.getInstance().getProperty("born_orgid", "")+"_AR_" + OscarProperties.getInstance().getProperty("born_env", "T") + "_"+dt+"_"+getFileSuffix()+".xml";
		return filename;
	}
	
	public List<Map<String,Object>> getMetadata() throws Exception {
		List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
		
		Connection conn=null;
		Statement st=null;
		
		try {
			
			conn = org.oscarehr.util.DbConnectionFilter.getThreadLocalDbConnection();
			st = conn.createStatement();
			ResultSet rs = st.executeQuery("select provider_no,demographic_no,id,formEdited,c_finalEDB,sent_to_born,pg1_signature,pg2_signature,episodeId,c_postal from (select r.provider_no,r.demographic_no,r.id,r.formEdited,r.c_finalEDB,r.sent_to_born,r.pg1_signature,r2.pg2_signature,r.episodeId,r.c_postal from formONAREnhancedRecord r,formONAREnhancedRecordExt2 r2 where r.ID = r2.ID and c_finalEDB!='' AND c_finalEDB IS NOT NULL ORDER BY formEdited DESC) AS x  GROUP BY demographic_no");
			while(rs.next()) {
				try {
					int demographicNo = rs.getInt("demographic_no");
					int id = rs.getInt("id");
					boolean sent = rs.getBoolean("sent_to_born");
					int episodeId = rs.getInt("episodeId");
					String postalCode = rs.getString("c_postal");
					String providerNo = rs.getString("provider_no");
					Date formEdited = rs.getDate("formEdited");
					
					if(postalCode == null || postalCode.length() == 0) {
						continue;
					}
					if(!sent) {
						Map<String,Object> r = new HashMap<String,Object>();
						
						r.put("demographicNo", demographicNo);
						r.put("id",id);
						r.put("sent",sent);
						r.put("episodeId",episodeId);
						r.put("providerNo",providerNo);
						r.put("formEdited", formEdited);
						
						results.add(r);
					}
				}catch(Exception e) {
					MiscUtils.getLogger().warn("Unable to add record",e);
				}			
			}
			
		} finally {
			if(st != null)
				try {
					st.close();
				}catch(SQLException e){}
			if(conn != null)
				try {
					conn.close();
				}catch(SQLException e){}
		}
		return results;
	}
}
