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


package org.oscarehr.study.decisionSupport;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.decisionSupport.model.DSConsequence;
import org.oscarehr.decisionSupport.model.DSGuideline;
import org.oscarehr.decisionSupport.model.DSGuidelineFactory;
import org.oscarehr.decisionSupport.model.DecisionSupportException;
import org.oscarehr.study.Study;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

/**
 * Class used to Manage BillingGuidelines.
 * Temporary and will be refactored to include the other billing systems. And probably more of a centralized rule repository.
 * @author jay
 */
public class StudyGuidelines  {

    private static Logger log = MiscUtils.getLogger();

    private List<DSGuideline> studyGuideLines = null ;


    static StudyGuidelines studyGuideLine = new StudyGuidelines();

    private HashMap<String, String> studyFilesMap;

    /**
     * Creates a new instance of MeasurementTemplateFlowSheetConfig
     */
    private StudyGuidelines() {
    	studyFilesMap = new HashMap<String, String>();
    	studyFilesMap.put(Study.MYMEDS, "myMEDS.xml");
    }

    static public StudyGuidelines getInstance(String study) {
        if (studyGuideLine.studyGuideLines == null) {                
               studyGuideLine.loadGuidelines(study);
        }
        
        return studyGuideLine;
    }


    /**
     * Loads all the guidelines from preset files in this package.  This will probably change to load them from a table in the database.
     */
    void loadGuidelines(String study) {
        log.debug("LOADING STUDY GUIDELINES");
        studyGuideLines = new ArrayList<DSGuideline>();

        DSGuideline guideline = null;
        StringBuilder sb = new StringBuilder();
        String studyFileName = studyFilesMap.get(study);
        try{
        	String streamToGet = "org/oscarehr/study/decisionSupport/"+studyFileName;
            log.debug("Trying to get "+streamToGet);
            BufferedReader in = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(streamToGet)));
            String str;
            while ((str = in.readLine()) != null) {
            	sb.append(str+"\n");
            }
            in.close();
            DSGuidelineFactory dsFactory = new DSGuidelineFactory();
            log.debug("xml "+sb.toString());
            guideline = dsFactory.createGuidelineFromXml(sb.toString());
            studyGuideLines.add(guideline);
        }catch(Exception e){
        	MiscUtils.getLogger().error("Error", e);
        }
    }


    public List<DSConsequence> evaluateAndGetConsequences(LoggedInInfo loggedInInfo, String demographicNo, String providerNo, List<Object> dynamicArgs) {
        log.debug("passed in provider: " + providerNo + " demographicNo " + demographicNo + " dynamicArgs size " + dynamicArgs.size());
        log.info("Decision Support 'evaluateAndGetConsequences' has been called, reading " + studyGuideLines.size() + " for this provider");
        ArrayList<DSConsequence> allResultingConsequences = new ArrayList<DSConsequence>();
        for (DSGuideline dsGuideline: studyGuideLines) {
            try {
                List<DSConsequence> newConsequences = dsGuideline.evaluate(loggedInInfo, demographicNo, providerNo, dynamicArgs);
                if (newConsequences != null) {
                    allResultingConsequences.addAll(newConsequences);
                }
            } catch (DecisionSupportException dse) {
                log.error("Failed to evaluate the patient against guideline, skipping guideline uuid: " , dse);
            }
        }
        log.info("Decision Support 'evaluateAndGetConsequences' finished, returing " + allResultingConsequences.size() + " consequences");
        return allResultingConsequences;
    }




}
