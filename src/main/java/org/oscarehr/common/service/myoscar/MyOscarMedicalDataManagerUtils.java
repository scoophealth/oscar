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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.common.service.myoscar;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.RemoteDataLogDao;
import org.oscarehr.common.dao.SentToPHRTrackingDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.RemoteDataLog;
import org.oscarehr.common.model.SentToPHRTracking;
import org.oscarehr.myoscar.client.ws_manager.MedicalDataManager;
import org.oscarehr.myoscar.client.ws_manager.MyOscarServerWebServicesManager;
import org.oscarehr.myoscar.utils.MyOscarLoggedInInfo;
import org.oscarehr.myoscar_server.ws.InvalidRequestException_Exception;
import org.oscarehr.myoscar_server.ws.ItemCompletedException_Exception;
import org.oscarehr.myoscar_server.ws.MedicalDataTransfer4;
import org.oscarehr.myoscar_server.ws.MedicalDataWs;
import org.oscarehr.myoscar_server.ws.NoSuchItemException_Exception;
import org.oscarehr.myoscar_server.ws.NotAuthorisedException_Exception;
import org.oscarehr.myoscar_server.ws.UnsupportedEncodingException_Exception;
import org.oscarehr.phr.util.MyOscarUtils;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public final class MyOscarMedicalDataManagerUtils {
	private static final Logger logger=MiscUtils.getLogger();
	
	private static final RemoteDataLogDao remoteDataLogDao = (RemoteDataLogDao) SpringUtils.getBean("remoteDataLogDao");
	private static final SentToPHRTrackingDao sentToPHRTrackingDao = (SentToPHRTrackingDao) SpringUtils.getBean("sentToPHRTrackingDao");
	private static final ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
	
	
	private MyOscarMedicalDataManagerUtils() {
		// utility class, no instantiation allowed.
	}

	/**
	 * @return a MedicalDataTransfer with default data, but missing MedicalDataType, Data fields, orignalSourceId, set those after yourself.
	 */
	public static MedicalDataTransfer4 getEmptyMedicalDataTransfer(MyOscarLoggedInInfo myOscarLoggedInInfo, Date dateOfData, String providerNo, Integer demographicId)
	{
		MedicalDataTransfer4 medicalDataTransfer=new MedicalDataTransfer4();
		
		medicalDataTransfer.setActive(true);
		medicalDataTransfer.setCompleted(true);

		GregorianCalendar cal=new GregorianCalendar();
		cal.setTime(dateOfData);
		medicalDataTransfer.setDateOfData(cal);
		
		Long providerMyOscarUserId = MyOscarUtils.getMyOscarUserIdFromOscarProviderNo(myOscarLoggedInInfo, providerNo);
		medicalDataTransfer.setObserverOfDataPersonId(providerMyOscarUserId);

		medicalDataTransfer.setObserverOfDataPersonName(getObserverOfDataPersonName(providerNo));

		Long patientMyOscarUserId = MyOscarUtils.getMyOscarUserIdFromOscarDemographicId(myOscarLoggedInInfo, demographicId);
		medicalDataTransfer.setOwningPersonId(patientMyOscarUserId);
		
		return(medicalDataTransfer);
	}
	
	public static Long addMedicalData(String providerNo, MyOscarLoggedInInfo myOscarLoggedInInfo, MedicalDataTransfer4 medicalDataTransfer, String oscarDataType, Object localOscarObjectId, boolean completed, boolean active) throws NotAuthorisedException_Exception, UnsupportedEncodingException_Exception, InvalidRequestException_Exception {
		Long resultId=MedicalDataManager.addMedicalData(myOscarLoggedInInfo, medicalDataTransfer, completed, active);
		logger.debug("addMedicalData success : resultId="+resultId);

		addSendRemoteDataLog(providerNo, oscarDataType, localOscarObjectId, medicalDataTransfer.getData());
		
		return(resultId);
	}
	
	public static void updateMedicalData(String providerNo, MyOscarLoggedInInfo myOscarLoggedInInfo, MedicalDataTransfer4 medicalDataTransfer, String oscarDataType, Object localOscarObjectId) throws NotAuthorisedException_Exception, NoSuchItemException_Exception, ItemCompletedException_Exception, UnsupportedEncodingException_Exception, InvalidRequestException_Exception {
		MedicalDataManager.updateMedicalData(myOscarLoggedInInfo, medicalDataTransfer);
		logger.debug("updateMedicalData success : objectId="+medicalDataTransfer.getId());

		addSendRemoteDataLog(providerNo, oscarDataType, localOscarObjectId, medicalDataTransfer.getData());
	}
	
	public static Long addMedicalDataRelationship(MyOscarLoggedInInfo myOscarLoggedInInfo, Long ownerId, Long primaryMedicalDataId, Long relatedMedicalDataId, String relationship) throws NoSuchItemException_Exception, NotAuthorisedException_Exception
	{
		MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(myOscarLoggedInInfo);
		Long resultId=medicalDataWs.addMedicalDataRelationship2(ownerId, primaryMedicalDataId, relatedMedicalDataId, relationship);
		return(resultId);
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

	public static String generateSourceId(String facilityName, String dataType, Object objectId)
	{
		return(facilityName + ':' + dataType + ':' + objectId);
	}

	private static void addSendRemoteDataLog(String providerNo,String oscarDataType, Object oscarObjectId, String dataContentsDescription) {
		RemoteDataLog remoteDataLog = new RemoteDataLog();
		remoteDataLog.setProviderNo(providerNo);
		remoteDataLog.setDocumentId(MyOscarLoggedInInfo.getMyOscarServerBaseUrl(), oscarDataType, oscarObjectId);
		remoteDataLog.setAction(RemoteDataLog.Action.SEND);
		remoteDataLog.setDocumentContents(dataContentsDescription);
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
			lastTracking.setObjectName(dataType);
			lastTracking.setSentDatetime(new Date(0)); // set to beginning of time to not confuse any algorithms trying to figure out if it's an update or create.
			lastTracking.setSentToServer(server);
			
			sentToPHRTrackingDao.persist(lastTracking);
		}
		
		return(lastTracking);
	}
	
	public static List<MedicalDataTransfer4> getMedicalData(MyOscarLoggedInInfo myOscarLoggedInInfo, Long ownerId, String medicalDataType, Boolean active, int startIndex, int itemsToReturn)
	{
		MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(myOscarLoggedInInfo);
		List<MedicalDataTransfer4> results=medicalDataWs.getMedicalDataByType2(ownerId, medicalDataType, active, startIndex, itemsToReturn);
		return(results);
	}

	public static MedicalDataTransfer4 getMedicalData(MyOscarLoggedInInfo myOscarLoggedInInfo, Long ownerId, Long medicalDataId) throws NoSuchItemException_Exception, NotAuthorisedException_Exception
	{
		MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(myOscarLoggedInInfo);
		MedicalDataTransfer4 result=medicalDataWs.getMedicalData5(ownerId, medicalDataId);
		return(result);
	}
	
	public static MedicalDataTransfer4 materialiseDataIfRequired(MyOscarLoggedInInfo myOscarLoggedInInfo, MedicalDataTransfer4 medicalDataTransfer) throws NotAuthorisedException_Exception, NoSuchItemException_Exception
	{
		if (medicalDataTransfer.getData() == null)
		{
			medicalDataTransfer = getMedicalData(myOscarLoggedInInfo, medicalDataTransfer.getOwningPersonId(), medicalDataTransfer.getId());
		}

		return(medicalDataTransfer);
	}
}
