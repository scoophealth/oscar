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
package org.oscarehr.phr.util;

import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.oscarehr.myoscar_server.ws.AccountWs;
import org.oscarehr.myoscar_server.ws.Relation;
import org.oscarehr.myoscar_server.ws.RelationshipTransfer3;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.QueueCache;

import oscar.oscarDemographic.data.DemographicData;

public class MyOscarServerRelationManager {
	private static final Logger logger = MiscUtils.getLogger();

	private static final int MAX_OBJECTS_TO_CACHE = 1024;

	private static QueueCache<String, List<RelationshipTransfer3>> relationDataCache = new QueueCache<String, List<RelationshipTransfer3>>(4, MAX_OBJECTS_TO_CACHE, DateUtils.MILLIS_PER_HOUR * 2);

	private static List<RelationshipTransfer3> getRelationShipTransferFromServer(PHRAuthentication auth, Long targetMyOscarUserId) {
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());
		final int REASONABLE_RELATIONSHIP_LIMIT = 1024;
		List<RelationshipTransfer3> relationList = accountWs.getRelationshipsByPersonId(targetMyOscarUserId, 0, REASONABLE_RELATIONSHIP_LIMIT);
		if (relationList.size() >= REASONABLE_RELATIONSHIP_LIMIT) logger.error("Error, we hit a hard coded limit. targetMyOscarUserId=" + targetMyOscarUserId);
		return relationList;
	}

	private static String getCacheKey(PHRAuthentication auth, Long targetMyOscarUserId) {
		// this is to help ensure data in the cache isn't shared as permissions would be by passed if it were. 
		return (""+auth.getMyOscarUserId() + ':' + targetMyOscarUserId);
	}

	public static List<RelationshipTransfer3> getRelationData(PHRAuthentication auth, Long targetMyOscarUserId){

		String cacheKey = getCacheKey(auth, targetMyOscarUserId);
		List<RelationshipTransfer3> relationList = relationDataCache.get(cacheKey);

		if (relationList == null) {
			relationList = getRelationShipTransferFromServer(auth, targetMyOscarUserId);
			relationDataCache.put(cacheKey, relationList);
		}
		return relationList;
	}

	public static boolean hasPatientRelationship(PHRAuthentication auth, Long targetMyOscarUserId) {

		List<RelationshipTransfer3> relationList = getRelationData(auth, targetMyOscarUserId);
		if (relationList != null) {
			for (RelationshipTransfer3 rt : relationList) {
				if (rt.getRelation().equals("PatientPrimaryCareProvider"))
				{
					if (rt.getPerson2().getPersonId().equals(auth.getMyOscarUserId()) && rt.getPerson2VerificationDate()!=null) {
						return(true);
					}
				}
			}
		}
		return false;
	}

	/**
	 * @deprecated 2012-09-12, need to update to use createRelationship2 as soon as possible.
	 * @return True if relation was created
	 */
	public static boolean addPatientRelationship(PHRAuthentication auth, String demoNo) throws Exception {
		boolean relationCreated = false;
		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());

		org.oscarehr.common.model.Demographic demo = new DemographicData().getDemographic(demoNo);
		String myOscarUserName = demo.getMyOscarUserName();
		Long myOscarUserId=MyOscarUtils.getMyOscarUserId(auth, myOscarUserName);
		boolean patientRelationshipExists = MyOscarServerRelationManager.hasPatientRelationship(auth, myOscarUserId);

		if (!patientRelationshipExists) {
	        Long relatedPersonId=MyOscarUtils.getMyOscarUserId(auth, myOscarUserName);	        
			accountWs.createRelationship(auth.getMyOscarUserId(), relatedPersonId, Relation.PATIENT);
			relationCreated = true;
			relationDataCache.remove(getCacheKey(auth, myOscarUserId));
		} else {
			logger.error("Patient Relation was not added already exists");
		}
		return relationCreated;
	}

}
