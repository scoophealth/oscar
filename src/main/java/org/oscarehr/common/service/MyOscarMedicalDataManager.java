/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.common.service;

import java.util.Date;
import java.util.GregorianCalendar;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.RemoteDataLogDao;
import org.oscarehr.common.dao.SentToPHRTrackingDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.RemoteDataLog;
import org.oscarehr.common.model.SentToPHRTracking;
import org.oscarehr.myoscar_server.ws.ItemAlreadyExistsException_Exception;
import org.oscarehr.myoscar_server.ws.MedicalDataTransfer2;
import org.oscarehr.myoscar_server.ws.MedicalDataWs;
import org.oscarehr.myoscar_server.ws.NotAuthorisedException_Exception;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.util.MyOscarServerWebServicesManager;
import org.oscarehr.phr.util.MyOscarUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.oscarProvider.data.ProviderMyOscarIdData;

public final class MyOscarMedicalDataManager {
	private static final DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	private static final ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
	private static final RemoteDataLogDao remoteDataLogDao = (RemoteDataLogDao) SpringUtils.getBean("remoteDataLogDao");
	private static final SentToPHRTrackingDao sentToPHRTrackingDao = (SentToPHRTrackingDao) SpringUtils.getBean("sentToPHRTrackingDao");
	private static final LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();

	private MyOscarMedicalDataManager() {
		// utility class, no instantiation allowed.
	}

	public static void sendMedicalData(PHRAuthentication auth, Integer demographicNo, String dataType, String medicalData, Object objectId, String observerOscarId, Date dateOfData) throws ItemAlreadyExistsException_Exception, NotAuthorisedException_Exception {
		MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());
		MedicalDataTransfer2 medicalDataTransfer = createMedicalDataTransfer(dataType);

		// set patient PHR ID
		Demographic demographic = demographicDao.getDemographic(demographicNo.toString());
		Long patientMyOscarUserId = MyOscarUtils.getMyOscarUserId(auth, demographic.getMyOscarUserName());
		medicalDataTransfer.setOwningPersonId(patientMyOscarUserId);

		// set provider PHR ID and Name
		String observerMyOscarId = ProviderMyOscarIdData.getMyOscarId(observerOscarId);
		Long observerMyOscarUserId = null;
		if (observerMyOscarId != null) {
			observerMyOscarUserId = MyOscarUtils.getMyOscarUserId(auth, observerMyOscarId);
			medicalDataTransfer.setObserverOfDataPersonId(observerMyOscarUserId);
		}
		medicalDataTransfer.setObserverOfDataPersonName(getProviderName(observerOscarId));

		// set date of medical data, can not be null, how can we have data which we don't know when it's from?
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(dateOfData);
		medicalDataTransfer.setDateOfData(cal);

		medicalDataTransfer.setData(medicalData);
		medicalDataTransfer.setOriginalSourceId(generateSourceId(loggedInInfo.currentFacility.getName(), dataType, objectId));
		medicalDataWs.addMedicalData2(medicalDataTransfer);

		logging(dataType, objectId, "content=" + medicalData);
	}

	public static String generateSourceId(String facilityName, String dataType, Object objectId)
	{
		return(facilityName + ':' + dataType + ':' + objectId);
	}
	
	private static MedicalDataTransfer2 createMedicalDataTransfer(String dataType) {
		MedicalDataTransfer2 medicalDataTransfer = new MedicalDataTransfer2();
		medicalDataTransfer.setMedicalDataType(dataType);
		medicalDataTransfer.setActive(true);
		medicalDataTransfer.setCompleted(true);
		return medicalDataTransfer;
	}

	private static String getProviderName(String providerNo) {
		Provider p = providerDao.getProvider(providerNo);
		if (p != null) return p.getFormattedName();
		return null;
	}

	private static void logging(String dataType, Object objectId, String dataContentsDescription) {
		RemoteDataLog remoteDataLog = new RemoteDataLog();
		remoteDataLog.setProviderNo(loggedInInfo.loggedInProvider.getProviderNo());
		remoteDataLog.setDocumentId(MyOscarServerWebServicesManager.getMyOscarServerBaseUrl(), dataType, objectId);
		remoteDataLog.setAction(RemoteDataLog.Action.SEND);
		remoteDataLog.setDocumentContents("id=" + objectId + ", " + dataContentsDescription);
		remoteDataLogDao.persist(remoteDataLog);
	}
	
	/**
	 * This method will get the existing SentToPHRTracking entry or it will create a new one if no prior exists.
	 * If a new one is created the lastObjectId will be initialised to 0 and the sentDatetime will be set to time 0 as well.
	 */
	public static SentToPHRTracking getExistingOrCreateInitialSentToPHRTracking(Integer demographicNo, String dataType, String server)
	{
		SentToPHRTracking lastTracking = sentToPHRTrackingDao.findByDemographicObjectServer(demographicNo, dataType, server);
		
		if (lastTracking == null)
		{
			lastTracking=new SentToPHRTracking();
			lastTracking.setDemographicNo(demographicNo);
			lastTracking.setLastObjectId(0);
			lastTracking.setObjectName(dataType);
			lastTracking.setSentDatetime(new Date(0)); // set to beginning of time to not confuse any algorithms trying to figure out if it's an update or create.
			lastTracking.setSentToServer(server);
			
			sentToPHRTrackingDao.persist(lastTracking);
		}
		
		return(lastTracking);
	}
}
