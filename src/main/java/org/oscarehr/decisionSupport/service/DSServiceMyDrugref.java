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

package org.oscarehr.decisionSupport.service;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.decisionSupport.model.DSGuideline;
import org.oscarehr.decisionSupport.model.DSGuidelineFactory;
import org.oscarehr.decisionSupport.model.DSGuidelineProviderMapping;
import org.oscarehr.decisionSupport.model.DecisionSupportException;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;

import oscar.oscarRx.pageUtil.RxMyDrugrefInfoAction;

/**
 *
 * @author apavel
 */
public class DSServiceMyDrugref extends DSService {
    private static final Logger logger = MiscUtils.getLogger();
    @Autowired
    private UserPropertyDAO  userPropertyDAO;

    public DSServiceMyDrugref() {

    }

    public void fetchGuidelinesFromService(LoggedInInfo loggedInInfo) {
        RxMyDrugrefInfoAction myDrugrefAction = new RxMyDrugrefInfoAction();
        try {
            logger.debug("CALLING MYDRUGREF");
            @SuppressWarnings("unchecked")
            Vector<Hashtable<String,Object>> providerGuidelines = myDrugrefAction.callOAuthService(loggedInInfo,"guidelines/getGuidelineIds", null, this.getMyDrugrefId(loggedInInfo.getLoggedInProviderNo()));
            if (providerGuidelines == null) {
                logger.error("Could not get provider decision support guidelines from MyDrugref.");
                return;
            }
            logger.debug("MyDrugref call returned: " + providerGuidelines.size() + " guidelines");
            ArrayList<String> guidelinesToFetch = new ArrayList<String>();
            for (Hashtable<String,Object> providerGuideline: providerGuidelines) {

                String uuid = (String) providerGuideline.get("uuid");
                String versionNumberStr = (String) providerGuideline.get("version");
                Integer versionNumber = Integer.parseInt(versionNumberStr);
                Date updatedAt = (Date) providerGuideline.get("updatedAt");

                logger.debug("uuid: " + uuid);
                logger.debug("version: " + versionNumber);

                DSGuideline matchedGuideline = dSGuidelineDao.findByUUID(uuid);
                if (matchedGuideline == null) {
                    guidelinesToFetch.add(uuid);
                } else if (matchedGuideline.getVersion()<versionNumber || matchedGuideline.getDateStart().before(updatedAt)) {
                    matchedGuideline.setStatus('I');
                    matchedGuideline.setDateDecomissioned(new Date());
                    dSGuidelineDao.merge(matchedGuideline);
                    guidelinesToFetch.add(uuid);
                }
            }
            
            //fetch the new ones
            List<DSGuideline> newGuidelines = this.fetchGuidelines(loggedInInfo,guidelinesToFetch);
            for (DSGuideline newGuideline: newGuidelines) {
                dSGuidelineDao.persist(newGuideline);
            }
            
            boolean hasInvalidXML = false;
            if (guidelinesToFetch.size()>newGuidelines.size()) hasInvalidXML = true;
            
            //Do mappings-guideline mappings
            List<DSGuidelineProviderMapping> uuidsMapped = dSGuidelineProviderMappingDao.getMappingsByProvider(loggedInInfo.getLoggedInProviderNo());
            for (Hashtable<String,Object> newMapping: providerGuidelines) {
                String newUuid = (String) newMapping.get("uuid");
                if (hasInvalidXML) {
                	DSGuideline checkingGuideline = dSGuidelineDao.findByUUID(newUuid);
                	if (checkingGuideline==null || checkingGuideline.getStatus()!='A') continue; //do not write invalid guideline
                }
                
                for (DSGuidelineProviderMapping uuidMapped : uuidsMapped) {
                	if (uuidMapped.getGuidelineUUID().equals(newUuid)) {
                		uuidsMapped.remove(uuidMapped);
                		newUuid = null;
                		break;
                	}
                }
                if (newUuid!=null) {
                	DSGuidelineProviderMapping newUuidObj = new DSGuidelineProviderMapping(newUuid, loggedInInfo.getLoggedInProviderNo());
                	dSGuidelineProviderMappingDao.persist(newUuidObj);
                }
            }
            //remove ones left over
            for (DSGuidelineProviderMapping uuidLeft: uuidsMapped) {
            	dSGuidelineProviderMappingDao.remove(uuidLeft.getId());
            }
        } catch (Exception e) {
            logger.error("Unable to fetch guidelines from MyDrugref", e);
        }

    }

    public String getMyDrugrefId(String providerNo) {
        UserProperty prop = userPropertyDAO.getProp(providerNo, UserProperty.MYDRUGREF_ID);
        String myDrugrefId = null;
        if (prop != null) {
            myDrugrefId = prop.getValue();
        }
        return myDrugrefId;
    }

    /**
     * @param userPropertyDAO the userPropertyDAO to set
     */
    public void setUserPropertyDAO(UserPropertyDAO userPropertyDAO) {
        this.userPropertyDAO = userPropertyDAO;
    }

    

    private List<DSGuideline> fetchGuidelines(LoggedInInfo loggedInInfo,List<String> uuids)  {
        RxMyDrugrefInfoAction myDrugrefAction = new RxMyDrugrefInfoAction();
        Vector params = new Vector();
        if(uuids.size() > 0) {
        	for(int i=0; i < uuids.size(); i++) {
        		params.add(uuids.get(i));
        	}
        }
        Vector<Hashtable> fetchedGuidelines = myDrugrefAction.callOAuthService(loggedInInfo,"guidelines/getGuidelines", params, null);
        
        ArrayList<DSGuideline> newGuidelines = new ArrayList<DSGuideline>();
        for (Hashtable<String,Serializable> fetchedGuideline: fetchedGuidelines) {
        	if (!isValidGuidelineXml((String) fetchedGuideline.get("body"))) continue;
        	
            logger.debug("Title: " + (String) fetchedGuideline.get("name"));
            logger.debug("Author: " + (String) fetchedGuideline.get("author"));
            logger.debug("UUID: " + (String) fetchedGuideline.get("uuid"));
            logger.debug("Version: " + (String) fetchedGuideline.get("version"));

            DSGuidelineFactory factory = new DSGuidelineFactory();
            DSGuideline newGuideline = factory.createBlankGuideline();
            try {
                newGuideline.setUuid((String) fetchedGuideline.get("uuid"));
                newGuideline.setTitle((String) fetchedGuideline.get("name"));
                newGuideline.setVersion(Integer.parseInt((String) fetchedGuideline.get("version")));
                newGuideline.setAuthor((String) fetchedGuideline.get("author"));
                newGuideline.setXml((String) fetchedGuideline.get("body"));
                newGuideline.setSource("mydrugref");
                newGuideline.setDateStart(new Date());
                newGuideline.setStatus('A');

                newGuideline.setXml((String) fetchedGuideline.get("body"));
                newGuidelines.add(newGuideline);

                newGuideline.parseFromXml();


            } catch (Exception e) {
                DecisionSupportException newException = new DecisionSupportException("Error parsing drug with with title: '" + (String) fetchedGuideline.get("name") + "' uuid: '" + (String) fetchedGuideline.get("uuid") + "'", e);
                logger.error(newException.getMessage());
                newGuideline.setStatus('F');
                newGuideline.setDateDecomissioned(new Date());
            }
        }
        return newGuidelines;
    }
    
    private boolean isValidGuidelineXml(String guidelineXml) {
    	StreamSource xsd = new StreamSource(this.getClass().getResourceAsStream("/k2a/dsGuideline.xsd"));
		StreamSource xml = new StreamSource(new StringReader(guidelineXml));
		
    	SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
	    	Schema schema = schemaFactory.newSchema(xsd);
	    	Validator validator = schema.newValidator();
			validator.validate(xml);
		} catch (Exception e) {
            DecisionSupportException newException = new DecisionSupportException("Error parsing guideline: "+guidelineXml, e);
            logger.error(newException.getMessage());
            return false;
		}
	
    	return true;
    }
}
