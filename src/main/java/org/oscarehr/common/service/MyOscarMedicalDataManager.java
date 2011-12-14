/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.common.service;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.RemoteDataLogDao;
import org.oscarehr.common.dao.SentToPHRTrackingDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.RemoteDataLog;
import org.oscarehr.common.model.SentToPHRTracking;
import org.oscarehr.myoscar_server.ws.MedicalDataTransfer2;
import org.oscarehr.myoscar_server.ws.MedicalDataWs;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.util.MyOscarServerWebServicesManager;
import org.oscarehr.phr.util.MyOscarUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import oscar.oscarProvider.data.ProviderMyOscarIdData;

/**
 *
 * @author Ronnie Cheng
 */
public class MyOscarMedicalDataManager {
	public static final String SUCCESS = "success";
	
	private static final Logger logger = MiscUtils.getLogger();
	private static final DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	private static final ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
	private static final RemoteDataLogDao remoteDataLogDao = (RemoteDataLogDao) SpringUtils.getBean("remoteDataLogDao");
	private static final SentToPHRTrackingDao sentToPHRTrackingDao = (SentToPHRTrackingDao) SpringUtils.getBean("sentToPHRTrackingDao");
	private static final LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
	
	private PHRAuthentication phrAuthentication = null;


	//constructor
	public MyOscarMedicalDataManager(HttpSession session) {
		phrAuthentication = MyOscarUtils.getPHRAuthentication(session);
	}

	
	public String sendMedicalData(Integer demographicNo, String dataType, String medicalData, Object objectId, String observerOscarId, Date dateOfData) {
		String error_msg = "Cannot send Medical Data to MyOscar";
		if (phrAuthentication==null) {
			return error_msg+" - Not logged in";
		}
		
		try {
			MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(phrAuthentication.getMyOscarUserId(), phrAuthentication.getMyOscarPassword());
			MedicalDataTransfer2 medicalDataTransfer = createMedicalDataTransfer(dataType);
	
			//set patient PHR ID
			Demographic demographic = demographicDao.getDemographic(demographicNo.toString());
			Long patientMyOscarUserId = MyOscarUtils.getMyOscarUserId(phrAuthentication, demographic.getMyOscarUserName());
			medicalDataTransfer.setOwningPersonId(patientMyOscarUserId);
			
			//set provider PHR ID and Name
			String observerMyOscarId = ProviderMyOscarIdData.getMyOscarId(observerOscarId);
			Long observerMyOscarUserId = null;
			if (observerMyOscarId!=null) {
				observerMyOscarUserId = MyOscarUtils.getMyOscarUserId(phrAuthentication, observerMyOscarId);
				medicalDataTransfer.setObserverOfDataPersonId(observerMyOscarUserId);
			}
			medicalDataTransfer.setObserverOfDataPersonName(getProviderName(observerOscarId));
			
			//set date of medical data
			GregorianCalendar cal = new GregorianCalendar();
			if (dateOfData!=null) {
				cal.setTime(dateOfData);
			}
			medicalDataTransfer.setDateOfData(cal);
			
			medicalDataTransfer.setData(medicalData);
			medicalDataTransfer.setOriginalSourceId(loggedInInfo.currentFacility.getName()+":"+dataType+":"+objectId);
			medicalDataWs.addMedicalData2(medicalDataTransfer);
			
			logging(dataType, objectId, "content="+medicalData);
			return SUCCESS;
			
		} catch (Exception ex) {
			logger.error(error_msg, ex);
			return ex.getMessage()==null ? error_msg : ex.getMessage();
		}
	}
	
	public Integer getLastTrackingId(Integer demographicNo, String dataType) { 
		SentToPHRTracking lastTracking = sentToPHRTrackingDao.getLastTracking(demographicNo, dataType, MyOscarServerWebServicesManager.getMyOscarServerBaseUrl());
		if (lastTracking!=null) return lastTracking.getLastObjectId();
		return 0;
	}
	
	public void addTracking(Integer demographicNo, String dataType, Integer objectId) {
		SentToPHRTracking tracking = new SentToPHRTracking();
		tracking.setDemographicNo(demographicNo);
		tracking.setObjectName(dataType);
		tracking.setLastObjectId(objectId);
		tracking.setSentDatetime(new Date());
		tracking.setSentToServer(MyOscarServerWebServicesManager.getMyOscarServerBaseUrl());
		sentToPHRTrackingDao.persist(tracking);
	}
	
	

	private MedicalDataTransfer2 createMedicalDataTransfer(String dataType) {
		MedicalDataTransfer2 medicalDataTransfer = new MedicalDataTransfer2();
		medicalDataTransfer.setMedicalDataType(dataType);
		medicalDataTransfer.setActive(true);
		medicalDataTransfer.setCompleted(true);
		return medicalDataTransfer;
	}
	
	private String getProviderName(String providerNo) {
		Provider p = providerDao.getProvider(providerNo);
		if (p!=null) return p.getFormattedName();
		return null;
	}
	
	private void logging(String dataType, Object objectId, String dataContentsDescription) {
		RemoteDataLog remoteDataLog = new RemoteDataLog();
		remoteDataLog.setProviderNo(loggedInInfo.loggedInProvider.getProviderNo());
		remoteDataLog.setDocumentId(MyOscarServerWebServicesManager.getMyOscarServerBaseUrl(), dataType, objectId);
		remoteDataLog.setAction(RemoteDataLog.Action.SEND);
		remoteDataLog.setDocumentContents("id="+objectId+", "+dataContentsDescription);
		remoteDataLogDao.persist(remoteDataLog);
	}
}
