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
package org.oscarehr.myoscar.utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.CustomHealthTrackerTransfer;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.InvalidRequestException_Exception;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.MedicalDataNoteTransfer;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.MedicalDataRelationshipTransfer2;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.MedicalDataTransfer5;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.MedicalDataWs;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.NoSuchItemException_Exception;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.NotAuthorisedException_Exception;
import org.oscarehr.myoscar_server.myoscar_server_client_stubs2.UnexpectedErrorException_Exception;
import org.oscarehr.util.ConfigXmlUtils;
import org.oscarehr.util.QueueCache;
import org.oscarehr.util.XmlUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public final class MedicalDataManager2 {
	private static final int MAX_OBJECTS_TO_CACHE = ConfigXmlUtils.getPropertyInt("myoscar_client", "medical_data_manager_data_cache_size");
	private static final int MAX_OBJECT_SIZE_TO_CACHE = ConfigXmlUtils.getPropertyInt("myoscar_client", "medical_data_manager_max_object_size_to_cache");
	private static final long MAX_TIME_TO_CACHE = ConfigXmlUtils.getPropertyInt("myoscar_client", "medical_data_manager_cache_time_ms");

	/**
	 * Cache key must be qualified so 2 people don't share a cached item, even if it's the same thing,
	 * the request must be made to the server so the access is logged and authentication is checked.
	 * 
	 * MAX_CACHE_ITEM_SIZE*items to queue = max memory used for cache, so 10,000 items at 10,240 bytes = 102,400,000 = 102 megs of rams
	 */
	private static QueueCache<String, MedicalDataTransfer5> medicalDataCache = new QueueCache<String, MedicalDataTransfer5>(4, MAX_OBJECTS_TO_CACHE, MAX_TIME_TO_CACHE, null);

	/**
	 * Borrowed from Hbase Bytes.java source
	 */
	public static String toStringBinary(final byte[] b, int off, int len) {
		StringBuilder result = new StringBuilder();
		// Just in case we are passed a 'len' that is > buffer length...
		if (off >= b.length) return result.toString();
		if (off + len > b.length) len = b.length - off;
		for (int i = off; i < off + len; ++i) {
			int ch = b[i] & 0xFF;
			if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || " `~!@#$%^&*()-_=+[]{}|;:'\",.<>/?".indexOf(ch) >= 0) {
				result.append((char) ch);
			} else {
				result.append(String.format("\\x%02X", ch));
			}
		}
		return result.toString();
	}

	private static String getCacheKey(MyOscarLoggedInInfo credentials, byte[] medicalDataId) {
		// we're qualifying session and person because
		// the "loggedInPersonId" is important because if some one manages to logs in with our clearing the session we might see old data from the last person logged in, in theory not possible as we clear the session on both logout and login but lets be double safe. 
		// the "loggedInSessionId" is desirable because if the same person logs in and logs out and logs back in, we want to ignore previously cached data, if effectively lets people clear the cache.  
		return (credentials.getLoggedInSessionId() + ':' + credentials.getLoggedInPersonId() + ':' + toStringBinary(medicalDataId,0,medicalDataId.length));
	}

	/**
	 * Put is defined as an add or replace depending on if the object already exists or not.
	 */
	public static byte[] putMedicalData(MyOscarLoggedInInfo credentials, MedicalDataTransfer5 medicalDataTransfer, boolean active) throws UnexpectedErrorException_Exception, NotAuthorisedException_Exception {
		medicalDataTransfer.setActive(active);

		MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(credentials);
		byte[] result = medicalDataWs.putMedicalData(medicalDataTransfer);
		String cacheKey = getCacheKey(credentials, medicalDataTransfer.getId());
		medicalDataCache.remove(cacheKey);

		return (result);
	}

	public static MedicalDataTransfer5 getMedicalData(MyOscarLoggedInInfo credentials, byte[] medicalDataId, boolean includeData, boolean includeNotes) throws NoSuchItemException_Exception, NotAuthorisedException_Exception, UnexpectedErrorException_Exception {
		// check cache for item
		String cacheKey = getCacheKey(credentials, medicalDataId);
		MedicalDataTransfer5 medicalDataTransfer = medicalDataCache.get(cacheKey);

		if (medicalDataTransfer == null) {
			MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(credentials);
			medicalDataTransfer = medicalDataWs.getMedicalData6(medicalDataId, includeData, includeNotes);

			// only cache full retrievals
			if (includeData && includeNotes && medicalDataTransfer.getData().length < MAX_OBJECT_SIZE_TO_CACHE) {
				medicalDataCache.put(cacheKey, medicalDataTransfer);
			}
		}

		return (medicalDataTransfer);
	}

	/**
	 * This method will convert the data block into an xml document. If the data block is not materialised it will
	 * fetch it from the server
	 */
	public static Document getMedicalDataAsXml(MyOscarLoggedInInfo credentials, MedicalDataTransfer5 medicalDataTransfer) throws ClientProtocolException, IllegalStateException, IOException, ParserConfigurationException, SAXException, NoSuchItemException_Exception, NotAuthorisedException_Exception, UnexpectedErrorException_Exception {
		byte[] dataBytes = medicalDataTransfer.getData();

		if (dataBytes == null && medicalDataTransfer.getId() != null) {
			medicalDataTransfer = getMedicalData(credentials, medicalDataTransfer.getId(), true, true);
		}

		if (dataBytes == null) return (null);

		return (XmlUtils.toDocument(dataBytes));
	}

	/**
	 * @return data ordered by dateOfData descending
	 */
	public static List<MedicalDataTransfer5> getMedicalDataList(MyOscarLoggedInInfo credentials, Long ownerPersonId, String medicalDataType, boolean matchExactType, Boolean active, boolean includeData, boolean includeNotes, byte[] startKey, int itemsToReturn, boolean excludeStartKey) throws NoSuchItemException_Exception, NotAuthorisedException_Exception {
		MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(credentials);
		List<MedicalDataTransfer5> results = medicalDataWs.getMedicalDataByTypeAndStartKey(ownerPersonId, medicalDataType, matchExactType, active, includeData, includeNotes, startKey, itemsToReturn, excludeStartKey);

		if (includeData && includeNotes) {
			for (MedicalDataTransfer5 temp : results) {
				if (temp.getData().length < MAX_OBJECT_SIZE_TO_CACHE) {
					String cacheKey = getCacheKey(credentials, temp.getId());
					medicalDataCache.put(cacheKey, temp);
				}
			}
		}

		return (results);
	}

	/**
	 * @return data ordered by dateOfData descending
	 */
	public static List<MedicalDataTransfer5> getMedicalDataList(MyOscarLoggedInInfo credentials, Long ownerPersonId, String medicalDataType, boolean matchExactType, Boolean active, boolean includeData, boolean includeNotes, int startIndex, int itemsToReturn) throws NoSuchItemException_Exception, NotAuthorisedException_Exception {
		MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(credentials);
		List<MedicalDataTransfer5> results = medicalDataWs.getMedicalDataByTypeAndStartIndex(ownerPersonId, medicalDataType, matchExactType, active, includeData, includeNotes, startIndex, itemsToReturn);

		if (includeData && includeNotes) {
			for (MedicalDataTransfer5 temp : results) {
				if (temp.getData().length < MAX_OBJECT_SIZE_TO_CACHE) {
					String cacheKey = getCacheKey(credentials, temp.getId());
					medicalDataCache.put(cacheKey, temp);
				}
			}
		}

		return (results);
	}

	public static List<MedicalDataTransfer5> getMedicalDataList(MyOscarLoggedInInfo credentials, Long ownerPersonId, String medicalDataType, boolean matchExactType, Boolean active, boolean includeData, boolean includeNotes, Calendar startDate, Calendar endDate, boolean excludeStartKey) throws NoSuchItemException_Exception, NotAuthorisedException_Exception {
		MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(credentials);
		List<MedicalDataTransfer5> results = medicalDataWs.getMedicalDataByTypeAndDateRange4(ownerPersonId, medicalDataType, matchExactType, active, includeData, includeNotes, startDate, endDate, excludeStartKey);

		if (includeData && includeNotes) {
			for (MedicalDataTransfer5 temp : results) {
				if (temp.getData().length < MAX_OBJECT_SIZE_TO_CACHE) {
					String cacheKey = getCacheKey(credentials, temp.getId());
					medicalDataCache.put(cacheKey, temp);
				}
			}
		}

		return (results);
	}

	public static List<String> getMedicalDataTypes(MyOscarLoggedInInfo credentials, Long ownerPersonId, String medicalDataType, Boolean active) throws NoSuchItemException_Exception, NotAuthorisedException_Exception {
		MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(credentials);
		List<String> results = medicalDataWs.getMedicalDataTypes(ownerPersonId, medicalDataType, active);
		return (results);
	}

	public static void addMedicalDataNote(MyOscarLoggedInInfo credentials, MedicalDataNoteTransfer noteTransfer) throws UnexpectedErrorException_Exception, NoSuchItemException_Exception, NotAuthorisedException_Exception {
		MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(credentials);
		medicalDataWs.addMedicalDataNote(noteTransfer);

		medicalDataCache.remove(getCacheKey(credentials, noteTransfer.getMedicalDataId()));
	}

	/**
	 * dataId can be null if you want to do a generic check on the datatype only.
	 */
	public static boolean isAllowed(MyOscarLoggedInInfo credentials, char permissionFlag, Long ownerPersonId, String dataType, Long dataId) {
		MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(credentials);
		boolean result = medicalDataWs.isAllowed(permissionFlag, ownerPersonId, dataType, dataId);
		return (result);
	}

	public static void addMedicalDataRelaionship(MyOscarLoggedInInfo credentials, MedicalDataRelationshipTransfer2 relationshipTransfer) throws NoSuchItemException_Exception, InvalidRequestException_Exception, NotAuthorisedException_Exception {
		MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(credentials);
		medicalDataWs.addMedicalDataRelationship3(relationshipTransfer);
	}

	public static void putCustomHealthTracker(MyOscarLoggedInInfo credentials, CustomHealthTrackerTransfer customHealthTrackerTransfer) throws UnexpectedErrorException_Exception, NotAuthorisedException_Exception {
		MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(credentials);
		medicalDataWs.putCustomHealthTracker(customHealthTrackerTransfer);
	}

	public static List<CustomHealthTrackerTransfer> getCustomHealthTrackers(MyOscarLoggedInInfo credentials, Long ownerPersonId, String medicalDataTypePrefix) throws NotAuthorisedException_Exception, UnexpectedErrorException_Exception {
		MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(credentials);
		return (medicalDataWs.getCustomHealthTrackers(ownerPersonId, medicalDataTypePrefix));
	}

	public static CustomHealthTrackerTransfer getCustomHealthTracker(MyOscarLoggedInInfo credentials, Long ownerPersonId, String medicalDataType) throws NotAuthorisedException_Exception, UnexpectedErrorException_Exception {
		List<CustomHealthTrackerTransfer> tempList = getCustomHealthTrackers(credentials, ownerPersonId, medicalDataType);
		for (CustomHealthTrackerTransfer temp : tempList) {
			if (medicalDataType.equals(temp.getMedicalDataType())) return (temp);
		}

		return (null);
	}

	public static void setMedicalDataInactive(MyOscarLoggedInInfo credentials, byte[] medicalDataId) throws NoSuchItemException_Exception, NotAuthorisedException_Exception, UnexpectedErrorException_Exception {
		MedicalDataTransfer5 medicalDataTransfer = getMedicalData(credentials, medicalDataId, false, false);
		putMedicalData(credentials, medicalDataTransfer, false);
	}
}
