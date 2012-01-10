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
	private static final RemoteDataLogDao remoteDataLogDao = (RemoteDataLogDao) SpringUtils.getBean("remoteDataLogDao");
	private static final SentToPHRTrackingDao sentToPHRTrackingDao = (SentToPHRTrackingDao) SpringUtils.getBean("sentToPHRTrackingDao");
	private static final LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
	private static final ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");


	private MyOscarMedicalDataManager() {
		// utility class, no instantiation allowed.
	}

	/**
	 * @deprecated use sendMedicalData(PhrAuthentication auth, MedicalDataTransfer2 medicalDataTransfer) instead
	 */
	public static void sendMedicalData(PHRAuthentication auth, Integer demographicNo, String dataType, String medicalData, Object objectId, String providerNo, Date dateOfData) throws ItemAlreadyExistsException_Exception, NotAuthorisedException_Exception {
		MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());
		MedicalDataTransfer2 medicalDataTransfer = getEmptyMedicalDataTransfer2(auth, dateOfData, providerNo, demographicNo);

		medicalDataTransfer.setData(medicalData);
		medicalDataTransfer.setMedicalDataType(dataType);
		medicalDataTransfer.setOriginalSourceId(generateSourceId(loggedInInfo.currentFacility.getName(), dataType, objectId));

		medicalDataWs.addMedicalData2(medicalDataTransfer);

		addRemoteDataLog(dataType, objectId, "content=" + medicalData);
	}

	/**
	 * @return a MedicalDataTransfer2 with default data, but missing MedicalDataType, Data fields, orignalSourceId, set those after yourself.
	 */
	public static MedicalDataTransfer2 getEmptyMedicalDataTransfer2(PHRAuthentication auth, Date dateOfData, String providerNo, Integer demographicId)
	{
		MedicalDataTransfer2 medicalDataTransfer=new MedicalDataTransfer2();
		
		medicalDataTransfer.setActive(true);
		medicalDataTransfer.setCompleted(true);

		GregorianCalendar cal=new GregorianCalendar();
		cal.setTime(dateOfData);
		medicalDataTransfer.setDateOfData(cal);
		
		String myOscarUserName = getProviderMyOscarUserName(providerNo);
		if (myOscarUserName != null) {
			Long providerMyOscarUserId = MyOscarUtils.getMyOscarUserId(auth, myOscarUserName);
			medicalDataTransfer.setObserverOfDataPersonId(providerMyOscarUserId);
		}
		medicalDataTransfer.setObserverOfDataPersonName(getObserverOfDataPersonName(providerNo));

		Demographic demographic = demographicDao.getDemographicById(demographicId);
		Long patientMyOscarUserId = MyOscarUtils.getMyOscarUserId(auth, demographic.getMyOscarUserName());
		medicalDataTransfer.setOwningPersonId(patientMyOscarUserId);
		
		return(medicalDataTransfer);
	}
	
	public static void sendMedicalData(PHRAuthentication auth, MedicalDataTransfer2 medicalDataTransfer, String oscarDataType, String objectId) throws ItemAlreadyExistsException_Exception, NotAuthorisedException_Exception {
		MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());

		medicalDataWs.addMedicalData2(medicalDataTransfer);

		addRemoteDataLog(oscarDataType, objectId, medicalDataTransfer.getData());
	}
	
	private static String getObserverOfDataPersonName(Provider p)
	{
		if (p==null) return(null);
		return(p.getFormattedName());
	}
	
	public static String getObserverOfDataPersonName(String providerNo)
	{
		Provider p = providerDao.getProvider(providerNo);
		return getObserverOfDataPersonName(p);
	}
	
	private static String getProviderMyOscarUserName(String providerNo)
	{
		String myOscarUserName = ProviderMyOscarIdData.getMyOscarId(providerNo);
		return(myOscarUserName);
	}

	public static String generateSourceId(String facilityName, String dataType, Object objectId)
	{
		return(facilityName + ':' + dataType + ':' + objectId);
	}

	private static void addRemoteDataLog(String dataType, Object objectId, String dataContentsDescription) {
		RemoteDataLog remoteDataLog = new RemoteDataLog();
		remoteDataLog.setProviderNo(loggedInInfo.loggedInProvider.getProviderNo());
		remoteDataLog.setDocumentId(MyOscarServerWebServicesManager.getMyOscarServerBaseUrl(), dataType, objectId);
		remoteDataLog.setAction(RemoteDataLog.Action.SEND);
		remoteDataLog.setDocumentContents("id=" + objectId + ", contents=" + dataContentsDescription);
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
	
	public static void sendPrescriptionsMedicationsToMyOscar(PHRAuthentication auth, Integer demographicId)
	{
		// get the persons prescriptions
		// for each prescription
		//		send the prescription
		//		get the medications for the prescription
		//		for each medication
		//			send the medication
		//			link the medication with the prescription

// stubbed for now
	}
}
