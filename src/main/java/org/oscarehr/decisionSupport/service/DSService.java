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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DSGuidelineDao;
import org.oscarehr.common.dao.DSGuidelineProviderMappingDao;
import org.oscarehr.decisionSupport.model.DSConsequence;
import org.oscarehr.decisionSupport.model.DSGuideline;
import org.oscarehr.decisionSupport.model.DecisionSupportException;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author apavel
 */
public abstract class DSService {
    private static final Logger logger = MiscUtils.getLogger();
    @Autowired
    protected DSGuidelineDao dSGuidelineDao;
    @Autowired
    protected DSGuidelineProviderMappingDao dSGuidelineProviderMappingDao;

    public DSService() {
  
    }

    public List<DSConsequence> evaluateAndGetConsequences(LoggedInInfo loggedInInfo, String demographicNo, String providerNo) {
        logger.debug("passed in provider: " + providerNo + " demographicNo" + demographicNo);
        List<DSGuideline> dsGuidelines = this.dSGuidelineDao.getDSGuidelinesByProvider(providerNo);
        logger.info("Decision Support 'evaluateAndGetConsequences' has been called, reading " + dsGuidelines.size() + " for this provider");
        ArrayList<DSConsequence> allResultingConsequences = new ArrayList<DSConsequence>();
        for (DSGuideline dsGuideline: dsGuidelines) {
            try {
                List<DSConsequence> newConsequences = dsGuideline.evaluate(loggedInInfo, demographicNo);
                if (newConsequences != null) {
                    allResultingConsequences.addAll(newConsequences);
                }
            } catch (DecisionSupportException dse) {
                logger.error("Failed to evaluate the patient against guideline, skipping guideline uuid: " + dsGuideline.getUuid(), dse);
            }
        }
        logger.info("Decision Support 'evaluateAndGetConsequences' finished, returing " + allResultingConsequences.size() + " consequences");
        return allResultingConsequences;
    }

    public void fetchGuidelinesFromServiceInBackground(LoggedInInfo loggedInInfo) {
        DSServiceThread dsServiceThread = new DSServiceThread(this, loggedInInfo);
        dsServiceThread.start();
    }
    public abstract void fetchGuidelinesFromService(LoggedInInfo loggedInInfo);

    public List<DSGuideline> getDsGuidelinesByProvider(String provider) {
        return dSGuidelineDao.getDSGuidelinesByProvider(provider);
    }

    public DSGuideline findGuideline(Integer guidelineId) {
    	return dSGuidelineDao.find(guidelineId);
    }
}
