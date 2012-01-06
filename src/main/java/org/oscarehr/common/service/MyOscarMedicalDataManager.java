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

/**
 * @author Ronnie Cheng
 */
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

		// set date of medical data
		GregorianCalendar cal = new GregorianCalendar();
		if (dateOfData != null) {
			cal.setTime(dateOfData);
		}
		medicalDataTransfer.setDateOfData(cal);

		medicalDataTransfer.setData(medicalData);
		medicalDataTransfer.setOriginalSourceId(loggedInInfo.currentFacility.getName() + ":" + dataType + ":" + objectId);
		medicalDataWs.addMedicalData2(medicalDataTransfer);

		logging(dataType, objectId, "content=" + medicalData);
	}

	public static Integer getLastTrackingId(Integer demographicNo, String dataType) {
		SentToPHRTracking lastTracking = sentToPHRTrackingDao.findByDemographicObjectServer(demographicNo, dataType, MyOscarServerWebServicesManager.getMyOscarServerBaseUrl());
		if (lastTracking != null) return lastTracking.getLastObjectId();
		return 0;
	}

	public static void addTracking(Integer demographicNo, String dataType, Integer objectId) {
		SentToPHRTracking tracking = new SentToPHRTracking();
		tracking.setDemographicNo(demographicNo);
		tracking.setObjectName(dataType);
		tracking.setLastObjectId(objectId);
		tracking.setSentDatetime(new Date());
		tracking.setSentToServer(MyOscarServerWebServicesManager.getMyOscarServerBaseUrl());
		sentToPHRTrackingDao.persist(tracking);
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
}
