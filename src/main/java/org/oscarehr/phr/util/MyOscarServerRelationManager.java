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
import org.oscarehr.myoscar_server.ws.NoSuchItemException_Exception;
import org.oscarehr.myoscar_server.ws.Relation;
import org.oscarehr.myoscar_server.ws.RelationshipTransfer2;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.QueueCache;

import oscar.oscarDemographic.data.DemographicData;

public class MyOscarServerRelationManager {
	private static final Logger logger=MiscUtils.getLogger();

	private static final int MAX_OBJECTS_TO_CACHE = 1024;

	private static QueueCache<String, List<RelationshipTransfer2>> relationDataCache = new QueueCache<String, List <RelationshipTransfer2>>(4, MAX_OBJECTS_TO_CACHE, DateUtils.MILLIS_PER_HOUR*2);

	private static List<RelationshipTransfer2> getRelationShipTransferFromServer(PHRAuthentication auth,String myoscarUsername) throws NoSuchItemException_Exception{
		AccountWs accountWs=MyOscarServerWebServicesManager.getAccountWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());
		List<RelationshipTransfer2> relationList = accountWs.getRelationshipsByPrimaryPersonIdRelatedPersonId(auth.getMyOscarUserName(),myoscarUsername);
		return relationList;
	}

	private static String getCacheKey(String primaryUsername, String relatedUsername){
		return(primaryUsername + ":" + relatedUsername);
	}

	public static List<RelationshipTransfer2> getRelationData(PHRAuthentication auth,String myoscarUsername) throws NoSuchItemException_Exception{

		String cacheKey = getCacheKey(auth.getMyOscarUserName(), myoscarUsername);
		List<RelationshipTransfer2> relationList = relationDataCache.get(cacheKey);

		if(relationList == null){
			relationList = getRelationShipTransferFromServer(auth, myoscarUsername);
			relationDataCache.put(cacheKey, relationList);
		}
		return relationList;
	}

	public static boolean hasPatientRelationship(PHRAuthentication auth,String myoscarUsername) throws NoSuchItemException_Exception{

		boolean patientRelationshipExists= false;
		List<RelationshipTransfer2> relationList = getRelationData( auth, myoscarUsername);
		if(relationList != null){
		    for(RelationshipTransfer2 rt:relationList){
		    	if (Relation.PATIENT == rt.getRelation()){
		    		patientRelationshipExists = true;
		    		break;
		    	}
		    }
		}
		return patientRelationshipExists;
	}

	/**
	 *
	 * @param auth
	 * @param demoNo
	 * @return True if relation was created
	 * @throws Exception
	 */
	public static boolean addPatientRelationship(PHRAuthentication auth, String demoNo ) throws Exception {
		boolean relationCreated = false;
		AccountWs accountWs=MyOscarServerWebServicesManager.getAccountWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());

		org.oscarehr.common.model.Demographic demo = new DemographicData().getDemographic(demoNo);
        String myOscarUserName = demo.getMyOscarUserName();
        boolean patientRelationshipExists= MyOscarServerRelationManager.hasPatientRelationship(auth,myOscarUserName);

        if (!patientRelationshipExists){
		   accountWs.createRelationshipByUserName(auth.getMyOscarUserName(), myOscarUserName, Relation.PATIENT);
		   relationCreated = true;
		   relationDataCache.remove(getCacheKey(auth.getMyOscarUserName(), myOscarUserName));
        }else{
        	logger.error("Patient Relation was not added already exists");
        }
        return relationCreated;
    }




}
