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
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.decisionSupport.model.DSGuideline;
import org.oscarehr.decisionSupport.model.DSGuidelineFactory;
import org.oscarehr.decisionSupport.model.DSGuidelineProviderMapping;
import org.oscarehr.decisionSupport.model.DecisionSupportException;
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

    public void fetchGuidelinesFromService(String providerNo) {

        Vector<String> params = new Vector<String>();
        params.addElement(this.getMyDrugrefId(providerNo));
        RxMyDrugrefInfoAction myDrugrefAction = new RxMyDrugrefInfoAction();
        try {
            logger.debug("CALLING MYDRUGREF");
            @SuppressWarnings("unchecked")
            Vector<Hashtable<String,String>> providerGuidelines = (Vector<Hashtable<String,String>>) myDrugrefAction.callWebserviceLite("GetGuidelineIds", params);
            if (providerGuidelines == null) {
                logger.error("Could not get provider decision support guidelines from MyDrugref.");
                return;
            }
            logger.debug("MyDrugref call returned: " + providerGuidelines.size() + " guidelines");
            ArrayList<String> guidelinesToFetch = new ArrayList<String>();
            for (Hashtable<String,String> providerGuideline: providerGuidelines) {

                String uuid =  providerGuideline.get("uuid");
                String versionNumberStr =  providerGuideline.get("version");
                Integer versionNumber = Integer.parseInt(versionNumberStr);

                logger.debug("uuid: " + uuid);
                logger.debug("version: " + versionNumber);

                DSGuideline matchedGuideline = dSGuidelineDao.findByUUID(uuid);
                if (matchedGuideline == null) {
                    guidelinesToFetch.add(uuid);
                } else if (matchedGuideline.getVersion() < versionNumber) {
                    matchedGuideline.setStatus('I');
                    matchedGuideline.setDateDecomissioned(new Date());
                    dSGuidelineDao.merge(matchedGuideline);
                    guidelinesToFetch.add(uuid);
                }
            }
            //fetch the new ones
            List<DSGuideline> newGuidelines = this.fetchGuidelines(guidelinesToFetch);
            for (DSGuideline newGuideline: newGuidelines) {
                dSGuidelineDao.persist(newGuideline);
            }
            //Do mappings-guideline mappings;
            List<DSGuidelineProviderMapping> uuidsMapped = dSGuidelineProviderMappingDao.getMappingsByProvider(providerNo);
            for (Hashtable<String,String> newMapping: providerGuidelines) {
                String newUuid = newMapping.get("uuid");
                DSGuidelineProviderMapping newUuidObj = new DSGuidelineProviderMapping(newUuid, providerNo);
                if (uuidsMapped.contains(newUuidObj)) {
                    uuidsMapped.remove(newUuidObj);
                } else {
                	dSGuidelineProviderMappingDao.persist(newUuidObj);
                }
            }
            //remove ones left over
            for (DSGuidelineProviderMapping uuidLeft: uuidsMapped) {
            	dSGuidelineProviderMappingDao.remove(uuidLeft);
            }
        } catch (Exception e) {
            logger.error("Unable to fetch guidelines from MyDrugref", e);
        }

    }

    public List<DSGuideline> fetchGuidelines(List<String> uuids)  {

        RxMyDrugrefInfoAction myDrugrefAction = new RxMyDrugrefInfoAction();
        Vector params = new Vector();
        params.addElement(new Vector(uuids));

        Vector<Hashtable> fetchedGuidelines = (Vector<Hashtable>) myDrugrefAction.callWebserviceLite("GetGuidelines", params);
        ArrayList<DSGuideline> newGuidelines = new ArrayList<DSGuideline>();
        for (Hashtable<String,Serializable> fetchedGuideline: fetchedGuidelines) {
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
                logger.error("Error", newException);
                newGuideline.setStatus('F');
                newGuideline.setDateDecomissioned(new Date());
            }
        }
        return newGuidelines;
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

}
