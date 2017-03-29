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


/*
 * MessageUploader.java
 *
 * Created on June 18, 2007, 1:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarLab.ca.all.upload;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.OtherIdManager;
import org.oscarehr.common.dao.FileUploadCheckDao;
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.dao.Hl7TextMessageDao;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.MeasurementsExtDao;
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.dao.ProviderLabRoutingDao;
import org.oscarehr.common.dao.RecycleBinDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.FileUploadCheck;
import org.oscarehr.common.model.Hl7TextInfo;
import org.oscarehr.common.model.Hl7TextMessage;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.MeasurementsExt;
import org.oscarehr.common.model.OtherId;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ProviderLabRoutingModel;
import org.oscarehr.common.model.RecycleBin;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.olis.dao.OLISSystemPreferencesDao;
import org.oscarehr.olis.model.OLISSystemPreferences;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarDemographic.data.DemographicMerged;
import oscar.oscarLab.ca.all.Hl7textResultsData;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.HHSEmrDownloadHandler;
import oscar.oscarLab.ca.all.parsers.MEDITECHHandler;
import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.oscarLab.ca.all.parsers.SpireHandler;
import oscar.util.UtilDateUtilities;

public final class MessageUploader {

	private static final Logger logger = MiscUtils.getLogger();
	private static PatientLabRoutingDao patientLabRoutingDao = SpringUtils.getBean(PatientLabRoutingDao.class);
	private static ProviderLabRoutingDao providerLabRoutingDao = SpringUtils.getBean(ProviderLabRoutingDao.class);
	private static RecycleBinDao recycleBinDao = SpringUtils.getBean(RecycleBinDao.class);
	private static Hl7TextInfoDao hl7TextInfoDao = (Hl7TextInfoDao) SpringUtils.getBean("hl7TextInfoDao");
	private static Hl7TextMessageDao hl7TextMessageDao = (Hl7TextMessageDao) SpringUtils.getBean("hl7TextMessageDao");
	private static MeasurementsExtDao measurementsExtDao = SpringUtils.getBean(MeasurementsExtDao.class);
	private static MeasurementDao measurementDao = SpringUtils.getBean(MeasurementDao.class);
	private static FileUploadCheckDao fileUploadCheckDao = SpringUtils.getBean(FileUploadCheckDao.class);
	private static DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);



	private MessageUploader() {
		// there's no reason to instantiate a class with no fields.
	}

	public static String routeReport(LoggedInInfo loggedInInfo, String serviceName, String type, String hl7Body, int fileId) throws Exception {
		return routeReport(loggedInInfo, serviceName, type, hl7Body, fileId, null);
	}

	/**
	 * Insert the lab into the proper tables of the database
	 */
	public static String routeReport(LoggedInInfo loggedInInfo, String serviceName, String type, String hl7Body, int fileId, RouteReportResults results) throws Exception {

		
		String retVal = "";
		try {
			MessageHandler h = Factory.getHandler(type, hl7Body);

			String firstName = h.getFirstName();
			String lastName = h.getLastName();
			String dob = h.getDOB();
			String sex = h.getSex();
			String hin = h.getHealthNum();
			String resultStatus = "";
			String priority = h.getMsgPriority();
			String requestingClient = h.getDocName();
			String reportStatus = h.getOrderStatus();
			String accessionNum = h.getAccessionNum();
			String fillerOrderNum = h.getFillerOrderNumber();
			String sendingFacility = h.getPatientLocation();
			ArrayList docNums = h.getDocNums();
			int finalResultCount = h.getOBXFinalResultCount();
			String obrDate = h.getMsgDate();

			if(h instanceof HHSEmrDownloadHandler) {
				try{
	            	String chartNo = ((HHSEmrDownloadHandler)h).getPatientIdByType("MR");
	            	if(chartNo != null) {
	            		//let's get the hin
	            		List<Demographic> clients = demographicManager.getDemosByChartNo(loggedInInfo, chartNo);
	            		if(clients!=null && clients.size()>0) {
	            			hin = clients.get(0).getHin();
	            		}
	            	}
				}catch(Exception e){
					logger.error("HHS ERROR",e);
				}
            }
            
            // get actual ohip numbers based on doctor first and last name for spire lab
            if(h instanceof SpireHandler) {
				List<String> docNames = ((SpireHandler)h).getDocNames();
				//logger.debug("docNames:");
	            for (int i=0; i < docNames.size(); i++) {
					logger.info(i + " " + docNames.get(i));
				}
            	if (docNames != null) {
					docNums = findProvidersForSpireLab(docNames);
				}
            }
            //logger.debug("docNums:");
            //for (int i=0; i < docNums.size(); i++) {
				//logger.debug(i + " " + docNums.get(i));
			//}

			try {
				// reformat date
				String format = "yyyy-MM-dd HH:mm:ss".substring(0, obrDate.length() - 1);
				obrDate = UtilDateUtilities.DateToString(UtilDateUtilities.StringToDate(obrDate, format), "yyyy-MM-dd HH:mm:ss");
			} catch (Exception e) {				
				logger.error("Error parsing obr date : ", e);
				throw e;
			}

			int i = 0;
			int j = 0;
			while (resultStatus.equals("") && i < h.getOBRCount()) {
				j = 0;
				while (resultStatus.equals("") && j < h.getOBXCount(i)) {
					if (h.isOBXAbnormal(i, j)) resultStatus = "A";
					j++;
				}
				i++;
			}

			ArrayList<String> disciplineArray = h.getHeaders();
			String next = "";
			
			if (disciplineArray != null && disciplineArray.size() > 0) { 
				next = disciplineArray.get(0);
			}
			
			int sepMark;
			if ((sepMark = next.indexOf("<br />")) < 0) {
				if ((sepMark = next.indexOf(" ")) < 0) sepMark = next.length();
			}
			
			String discipline = next.substring(0, sepMark).trim();

			for (i = 1; i < disciplineArray.size(); i++) {

				next = disciplineArray.get(i);
				if ((sepMark = next.indexOf("<br />")) < 0) {
					if ((sepMark = next.indexOf(" ")) < 0) sepMark = next.length();
				}

				if (!next.trim().equals("")) { 
					discipline = discipline + "/" + next.substring(0, sepMark);
				}
			}

			boolean isTDIS = type.equals("TDIS");
			boolean hasBeenUpdated = false;
			Hl7TextMessage hl7TextMessage = new Hl7TextMessage();
			Hl7TextInfo hl7TextInfo = new Hl7TextInfo();

			
			if( h instanceof MEDITECHHandler ) {				
				discipline = ( (MEDITECHHandler) h ).getDiscipline();
			}

			if (isTDIS) {
				List<Hl7TextInfo> matchingTdisLab =  hl7TextInfoDao.searchByFillerOrderNumber(fillerOrderNum, sendingFacility);
				if (matchingTdisLab.size()>0) {

					hl7TextMessageDao.updateIfFillerOrderNumberMatches(new String(Base64.encodeBase64(hl7Body.getBytes(MiscUtils.DEFAULT_UTF8_ENCODING)), MiscUtils.DEFAULT_UTF8_ENCODING),fileId,matchingTdisLab.get(0).getLabNumber());

					hl7TextInfoDao.updateReportStatusByLabId(reportStatus,matchingTdisLab.get(0).getLabNumber());
					hasBeenUpdated = true;
				}
			}
			int insertID = 0;
			if (!isTDIS || !hasBeenUpdated) {
				hl7TextMessage.setFileUploadCheckId(fileId);
				hl7TextMessage.setType(type);
				hl7TextMessage.setBase64EncodedeMessage(new String(Base64.encodeBase64(hl7Body.getBytes(MiscUtils.DEFAULT_UTF8_ENCODING)), MiscUtils.DEFAULT_UTF8_ENCODING));
				hl7TextMessage.setServiceName(serviceName);
				hl7TextMessageDao.persist(hl7TextMessage);

				insertID = hl7TextMessage.getId();
				hl7TextInfo.setLabNumber(insertID);
				hl7TextInfo.setLastName(lastName);
				hl7TextInfo.setFirstName(firstName);
				hl7TextInfo.setSex(sex);
				hl7TextInfo.setHealthNumber(hin);
				hl7TextInfo.setResultStatus(resultStatus);
				hl7TextInfo.setFinalResultCount(finalResultCount);
				hl7TextInfo.setObrDate(obrDate);
				hl7TextInfo.setPriority(priority);
				hl7TextInfo.setRequestingProvider(requestingClient);
				hl7TextInfo.setDiscipline(discipline);
				hl7TextInfo.setReportStatus(reportStatus);
				hl7TextInfo.setAccessionNumber(accessionNum);
				hl7TextInfo.setFillerOrderNum(fillerOrderNum);
				hl7TextInfoDao.persist(hl7TextInfo);
			}

			String demProviderNo = patientRouteReport(loggedInInfo, insertID, lastName, firstName, sex, dob, hin, DbConnectionFilter.getThreadLocalDbConnection());
			if(type.equals("OLIS_HL7") && demProviderNo.equals("0")) {
				OLISSystemPreferencesDao olisPrefDao = (OLISSystemPreferencesDao)SpringUtils.getBean("OLISSystemPreferencesDao");
			    OLISSystemPreferences olisPreferences =  olisPrefDao.getPreferences();
			    if(olisPreferences.isFilterPatients()) {
			    	//set as unclaimed
			    	providerRouteReport(String.valueOf(insertID), null, DbConnectionFilter.getThreadLocalDbConnection(), String.valueOf(0), type);
			    } else {
			    	providerRouteReport(String.valueOf(insertID), docNums, DbConnectionFilter.getThreadLocalDbConnection(), demProviderNo, type);
			    }
			} else {
				Integer limit = null;
				boolean orderByLength = false;
				String search = null;
				if (type.equals("Spire")) {
					limit = new Integer(1);
					orderByLength = true;
					search = "provider_no";
				}
				
				if( "MEDITECH".equals(type) ) {
					search = "practitionerNo";
				}
				
				providerRouteReport(String.valueOf(insertID), docNums, DbConnectionFilter.getThreadLocalDbConnection(), demProviderNo, type, search, limit, orderByLength);
			}
			retVal = h.audit();
			if(results != null) {
				results.segmentId = insertID;
			}
		} catch (Exception e) {
			logger.error("Error uploading lab to database");
			throw e;
		}

		return (retVal);

	}
	
	/**
	 * Method findProvidersForSpireLab
	 * Finds the providers that are associated with a spire lab.  (need to do this using doctor names, as
	 * spire labs don't have a valid ohip number associated with them).
	 */ 
	private static ArrayList<String> findProvidersForSpireLab(List<String> docNames) {
		List<String> docNums = new ArrayList<String>();
		ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
		
		for (int i=0; i < docNames.size(); i++) {
			String[] firstLastName = docNames.get(i).split("\\s");
			if (firstLastName != null && firstLastName.length >= 2) {
				//logger.debug("Searching for provider with first and last name: " + firstLastName[0] + " " + firstLastName[firstLastName.length-1]);
				List<Provider> provList = providerDao.getProviderLikeFirstLastName("%"+firstLastName[0]+"%", firstLastName[firstLastName.length-1]);
				if (provList != null) {
					int provIndex = findProviderWithShortestFirstName(provList);
					if (provIndex != -1 && provList.size() >= 1 && !provList.get(provIndex).getProviderNo().equals("0")) {
						docNums.add( provList.get(provIndex).getProviderNo() );
						//logger.debug("ADDED1: " + provList.get(provIndex).getProviderNo());
					} else {
						// prepend 'dr ' to first name and try again
						provList = providerDao.getProviderLikeFirstLastName("dr " + firstLastName[0], firstLastName[1]);
						if (provList != null) {
							provIndex = findProviderWithShortestFirstName(provList);
							if (provIndex != -1 && provList.size() == 1 && !provList.get(provIndex).getProviderNo().equals("0")) {
								//logger.debug("ADDED2: " + provList.get(provIndex).getProviderNo());
								docNums.add( provList.get(provIndex).getProviderNo() );
							}
						}
					}
				}
			}
		}
		
		return (ArrayList<String>)docNums;
	}
	
	/**
	 * Method findProviderWithShortestFirstName
	 * Finds the provider with the shortest first name in a list of providers.
	 */ 
	private static int findProviderWithShortestFirstName(List<Provider> provList) {
		if (provList == null || provList.isEmpty())
			return -1;
			
		int index = 0;
		int shortestLength = provList.get(0).getFirstName().length();
		for (int i=1; i < provList.size(); i++) {
			int curLength = provList.get(i).getFirstName().length();
			if (curLength < shortestLength) {
				index = i;
				shortestLength = curLength;
			}
		}
		
		return index;
	}
	
	/**
	 * Attempt to match the doctors from the lab to a provider
	 */ 
	private static void providerRouteReport(String labId, ArrayList docNums, Connection conn, String altProviderNo, String labType, String search_on, Integer limit, boolean orderByLength) throws Exception {
		ArrayList<String> providerNums = new ArrayList<String>();
		PreparedStatement pstmt;
		String sql = "";
		String sqlLimit = "";
		String sqlOrderByLength = "";
		String sqlSearchOn = "ohip_no";
		
		if (search_on != null && search_on.length() > 0) {
			sqlSearchOn = search_on;
		}
		
		if (limit != null && limit.intValue() > 0) {
			sqlLimit = " limit " + limit.toString();
		}	
		
		if (orderByLength) {
			sqlOrderByLength = " order by length(first_name)";
		}		
		
		if (docNums != null) {
			for (int i = 0; i < docNums.size(); i++) {

				if (docNums.get(i) != null && !((String) docNums.get(i)).trim().equals("")) {
					sql = "select provider_no from provider where "+ sqlSearchOn +" = '" + ((String) docNums.get(i)) + "'" + sqlOrderByLength + sqlLimit;
					pstmt = conn.prepareStatement(sql);
					ResultSet rs = pstmt.executeQuery();
					while (rs.next()) {
						providerNums.add(oscar.Misc.getString(rs, "provider_no"));
					}
					rs.close();
					pstmt.close();

					String otherIdMatchKey = OscarProperties.getInstance().getProperty("lab.other_id_matching", "");
					if(otherIdMatchKey.length()>0) {
						OtherId otherId = OtherIdManager.searchTable(OtherIdManager.PROVIDER, otherIdMatchKey, (String)docNums.get(i));
						if(otherId != null) {
							providerNums.add(otherId.getTableId());
						}
					}

				}
			}
		}
		
		//if (!labType.equals("Spire"))
		//	labType = "HL7";
		
		
		ProviderLabRouting routing = new ProviderLabRouting();
		if (providerNums.size() > 0) {
			for (int i = 0; i < providerNums.size(); i++) {
				String provider_no = providerNums.get(i);
				routing.route(labId, provider_no, conn, "HL7");
			}
		} else {
			routing.route(labId, "0", conn, "HL7");
			routing.route(labId, altProviderNo, conn, "HL7");
		}
	}

	/**
	 * Attempt to match the doctors from the lab to a provider
	 */
	private static void providerRouteReport(String labId, ArrayList docNums, Connection conn, String altProviderNo, String labType) throws Exception {
		providerRouteReport(labId, docNums, conn, altProviderNo, labType, null, null, false);
	}

	/**
	 * Attempt to match the patient from the lab to a demographic, return the patients provider which is to be used then no other provider can be found to match the patient to.
	 */
	private static String patientRouteReport(LoggedInInfo loggedInInfo, int labId, String lastName, String firstName, String sex, String dob, String hin, Connection conn) throws SQLException {
		PatientLabRoutingResult result = null;
		
			String sql;
			String demo = "0";
			String provider_no = "0";
			// 19481015
			String dobYear = "%";
			String dobMonth = "%";
			String dobDay = "%";
			String hinMod = "%";
	
			
			try {
	
				if (hin != null) {
					hinMod = new String(hin);
					if (hinMod.length() == 12) {
						hinMod = hinMod.substring(0, 10);
					}
				}
	
				if (dob != null && !dob.equals("")) {
					String[] dobArray = dob.trim().split("-");
					dobYear = dobArray[0];
					dobMonth = dobArray[1];
					dobDay = dobArray[2];
				}
	
				if (!firstName.equals("")) firstName = firstName.substring(0, 1);
				if (!lastName.equals("")) lastName = lastName.substring(0, 1);
	
				if (hinMod.equals("%")) {
					sql = "select demographic_no, provider_no from demographic where" + " last_name like '" + lastName + "%' and " + " first_name like '" + firstName + "%' and " + " year_of_birth like '" + dobYear + "' and " + " month_of_birth like '" + dobMonth + "' and " + " date_of_birth like '" + dobDay + "' and " + " sex like '" + sex + "%' ";
				} else if (OscarProperties.getInstance().getBooleanProperty("LAB_NOMATCH_NAMES", "yes")) {
					sql = "select demographic_no, provider_no from demographic where hin='" + hinMod + "' and " + " year_of_birth like '" + dobYear + "' and " + " month_of_birth like '" + dobMonth + "' and " + " date_of_birth like '" + dobDay + "' and " + " sex like '" + sex + "%' ";
				} else {
					sql = "select demographic_no, provider_no from demographic where hin='" + hinMod + "' and " + " last_name like '" + lastName + "%' and " + " first_name like '" + firstName + "%' and " + " year_of_birth like '" + dobYear + "' and " + " month_of_birth like '" + dobMonth + "' and " + " date_of_birth like '" + dobDay + "' and " + " sex like '" + sex + "%' ";
				}
	
				logger.debug(sql);
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();
				int count = 0;
				
				while (rs.next()) {
					result = new PatientLabRoutingResult();
					demo = oscar.Misc.getString(rs, "demographic_no");
					provider_no = oscar.Misc.getString(rs, "provider_no");
					result.setDemographicNo(Integer.parseInt(demo));
					result.setProviderNo(provider_no);
					count++;
				}
				rs.close();
				pstmt.close();
				if(count > 1) {
					result = null;
				}
			} catch (SQLException sqlE) {
				throw sqlE;
			}

		
		try {
			//did this link a merged patient? if so, we need to make sure we are the head record, or update
			//result to be the head record.
			if(result != null) {
				DemographicMerged dm = new DemographicMerged();
				Integer headDemo = dm.getHead(result.getDemographicNo());
				if(headDemo != null && headDemo.intValue() != result.getDemographicNo()) {
					Demographic demoTmp = demographicManager.getDemographic(loggedInInfo, headDemo);
					if(demoTmp != null) {
						result.setDemographicNo(demoTmp.getDemographicNo());
						result.setProviderNo(demoTmp.getProviderNo());
					} else {
						logger.info("Unable to load the head record of this patient record. (" + result.getDemographicNo()  + ")");
						result = null;
					}
				}
			}
			
			
			if (result == null) {
				logger.info("Could not find patient for lab: " + labId);
			} else {
				Hl7textResultsData.populateMeasurementsTable("" + labId, result.getDemographicNo().toString());
			}

			if(result != null) {
				sql = "insert into patientLabRouting (demographic_no, lab_no,lab_type,dateModified,created) values ('" + ((result != null && result.getDemographicNo()!=null)?result.getDemographicNo().toString():"0") + "', '" + labId + "','HL7',now(),now())";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();
				pstmt.close();
			}
		} catch (SQLException sqlE) {
			logger.info("NO MATCHING PATIENT FOR LAB id =" + labId);
			throw sqlE;
		}

		return (result != null)?result.getProviderNo():"0";
	}

	/**
	 * Used when errors occur to clean the database of labs that have not been inserted into all of the necessary tables
	 */
	public static void clean(int fileId) {
		
		List<Hl7TextMessage> results = hl7TextMessageDao.findByFileUploadCheckId(fileId);
		

		for (Hl7TextMessage result:results) {
			int lab_id = result.getId();
			
			Hl7TextInfo hti = hl7TextInfoDao.findLabId(lab_id);
			if(hti != null) {
				RecycleBin rb = new RecycleBin();
				rb.setProviderNo("0");
				rb.setUpdateDateTime(new Date());
				rb.setTableName("hl7TextInfo");
				rb.setKeyword(String.valueOf(lab_id));
				rb.setTableContent("<id>" + hti.getId() + "</id>" + "<lab_no>" + lab_id + "</lab_no>" + "<sex>" + hti.getSex() + "</sex>" + "<health_no>" +hti.getHealthNumber() + "</health_no>" + "<result_status>"
				        + hti.getResultStatus() + "</result_status>" + "<final_result_count>" + hti.getFinalResultCount() + "</final_result_count>" + "<obr_date>" + hti.getObrDate() + "</obr_date>" + "<priority>" + hti.getPriority() + "</priority>" + "<requesting_client>" + hti.getRequestingProvider() + "</requesting_client>" + "<discipline>" + hti.getDiscipline() + "</discipline>"
				        + "<last_name>" + hti.getLastName() + "</last_name>" + "<first_name>" + hti.getFirstName() + "</first_name>" + "<report_status>" + hti.getReportStatus() + "</report_status>" + "<accessionNum>" + hti.getAccessionNumber() + "</accessionNum>'");
				recycleBinDao.persist(rb);
				
				hl7TextInfoDao.remove(hl7TextInfoDao.findLabId(lab_id).getId());
			}
				

			Hl7TextMessage htm = hl7TextMessageDao.find(lab_id);
			if(htm != null) {
				RecycleBin rb = new RecycleBin();
				rb.setProviderNo("0");
				rb.setUpdateDateTime(new Date());
				rb.setTableName("hl7TextMessage");
				rb.setKeyword(String.valueOf(lab_id));
				rb.setTableContent("<lab_id>" + htm.getId() + "</lab_id>" + "<message>" + htm.getBase64EncodedeMessage() + "</message>" + "<type>" + htm.getType() + "</type>" + "<fileUploadCheck_id>" + htm.getFileUploadCheckId() + "</fileUploadCheck_id>");
				recycleBinDao.persist(rb);
				
				hl7TextMessageDao.remove(lab_id);
			}
			

			ProviderLabRoutingModel plr = providerLabRoutingDao.findByLabNo(lab_id);
			if(plr != null) {
				RecycleBin rb = new RecycleBin();
				rb.setProviderNo("0");
				rb.setUpdateDateTime(new Date());
				rb.setTableName("providerLabRouting");
				rb.setKeyword(String.valueOf(lab_id));
				rb.setTableContent("<provider_no>" + plr.getProviderNo() + "</provider_no>" + "<lab_no>" + plr.getLabNo() + "</lab_no>" + "<status>" +plr.getStatus() + "</status>" + "<comment>" + plr.getComment()
				        + "</comment>" + "<timestamp>" + plr.getTimestamp() + "</timestamp>" + "<lab_type>" + plr.getLabType() + "</lab_type>" + "<id>" + plr.getId() + "</id>");
				recycleBinDao.persist(rb);
				
				providerLabRoutingDao.remove(plr.getId());
			}
				
			PatientLabRouting lr = patientLabRoutingDao.findByLabNo(lab_id);
			if(lr != null) {
				RecycleBin rb = new RecycleBin();
				rb.setProviderNo("0");
				rb.setUpdateDateTime(new Date());
				rb.setTableName("patientLabRouting");
				rb.setKeyword(String.valueOf(lab_id));
				rb.setTableContent("<demographic_no>" + lr.getDemographicNo() + "</demographic_no>" + "<lab_no>" + lr.getLabNo() + "</lab_no>" + "<lab_type>" + lr.getLabType() + "</lab_type>" + "<id>" + lr.getId() + "</id>");
				recycleBinDao.persist(rb);

				patientLabRoutingDao.remove(lr.getId());
			}

			List<MeasurementsExt> measurementExts = measurementsExtDao.findByKeyValue("lab_no", String.valueOf(lab_id));
			for(MeasurementsExt me:measurementExts) {
				Measurement m = measurementDao.find(me.getMeasurementId());
				if(m != null) {
					RecycleBin rb = new RecycleBin();
					rb.setProviderNo("0");
					rb.setUpdateDateTime(new Date());
					rb.setTableName("measurements");
					rb.setKeyword(String.valueOf(me.getMeasurementId()));
					rb.setTableContent("<id>" +m.getId() + "</id>" + "<type>" + m.getType() + "</type>" + "<demographicNo>" +m.getDemographicId() + "</demographicNo>" + "<providerNo>" + m.getProviderNo() + "</providerNo>" + "<dataField>"
						        + m.getDataField() + "</dataField>" + "<measuringInstruction>" + m.getMeasuringInstruction() + "</measuringInstruction>" + "<comments>" + m.getComments() + "</comments>" + "<dateObserved>" + m.getDateObserved() + "</dateObserved>" + "<dateEntered>" + m.getCreateDate()+ "</dateEntered>");
					recycleBinDao.persist(rb);
					
					measurementDao.remove(m.getId());
				}
				
				List<MeasurementsExt> mes = measurementsExtDao.getMeasurementsExtByMeasurementId(me.getMeasurementId());
				for(MeasurementsExt me1:mes) {
					RecycleBin rb = new RecycleBin();
					rb.setProviderNo("0");
					rb.setUpdateDateTime(new Date());
					rb.setTableName("measurementsExt");
					rb.setKeyword(String.valueOf(me.getMeasurementId()));
					rb.setTableContent("<id>" + me1.getId() + "</id>" + "<measurement_id>" + me1.getMeasurementId() + "</measurement_id>" + "<keyval>" + me1.getKeyVal() + "</keyval>" + "<val>" + me1.getVal() + "</val>");
					recycleBinDao.persist(rb);
					
					measurementsExtDao.remove(me1.getId());
				}
				
			}
			
			

		}
		
		
		FileUploadCheck fuc = fileUploadCheckDao.find(fileId);
		if(fuc != null) {
			RecycleBin rb = new RecycleBin();
			rb.setProviderNo("0");
			rb.setUpdateDateTime(new Date());
			rb.setTableName("fileUploadCheck");
			rb.setKeyword(String.valueOf(fileId));
			rb.setTableContent("<id>" + fuc.getId() + "</id>" + "<provider_no>" + fuc.getProviderNo() + "</provider_no>" + "<filename>" + fuc.getFilename() + "</filename>" + "<md5sum>" + fuc.getMd5sum() + "</md5sum>" + "<datetime>" +fuc.getDateTime() + "</datetime>");
			recycleBinDao.persist(rb);	
			
			fileUploadCheckDao.remove(fuc.getId());
		}

			
			
	}
}
