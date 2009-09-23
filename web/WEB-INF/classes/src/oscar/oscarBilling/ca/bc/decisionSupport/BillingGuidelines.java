/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada   
 *
 * MeasurementTemplateFlowSheetConfig.java
 *
 * Created on January 28, 2006, 10:45 PM
 *
 */

package oscar.oscarBilling.ca.bc.decisionSupport;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.oscarehr.decisionSupport.model.DSConsequence;
import org.oscarehr.decisionSupport.model.DSGuideline;
import org.oscarehr.decisionSupport.model.DSGuidelineFactory;
import org.oscarehr.decisionSupport.model.DecisionSupportException;

/**
 * Class used to Manage BillingGuidelines.
 * Temporary and will be refactored to include the other billing systems. And probably more of a centralized rule repository.
 * @author jay
 */
public class BillingGuidelines  {

    private static Log log = LogFactory.getLog(BillingGuidelines.class);

    private List<DSGuideline> billingGuideLines = null ;
    
   
    static BillingGuidelines measurementTemplateFlowSheetConfig = new BillingGuidelines();

   
   
    /**
     * Creates a new instance of MeasurementTemplateFlowSheetConfig
     */
    private BillingGuidelines() {
    }


    static public BillingGuidelines getInstance() {
        if (measurementTemplateFlowSheetConfig.billingGuideLines == null) {
            try {
                measurementTemplateFlowSheetConfig.loadGuidelines();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return measurementTemplateFlowSheetConfig;
    }

    String[] filenames= {"250.xml","401a.xml","401b.xml","428.xml"};

    /**
     * Loads all the guidelines from preset files in this package.  This will probably change to load them from a table in the database.
     */
    void loadGuidelines() throws FileNotFoundException {
        log.debug("LOADING FLOWSSHEETS");
        billingGuideLines = new ArrayList();
        for (String filename : filenames) {
            DSGuideline guideline = null;
            StringBuffer sb = new StringBuffer();
            try{
                String streamToGet = "oscar/oscarBilling/ca/bc/decisionSupport/"+filename;
                System.out.println("Trying to get "+streamToGet);
                BufferedReader in = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(streamToGet)));
                String str;
                while ((str = in.readLine()) != null) {
                    sb.append(str+"\n");
                }
                in.close();
                DSGuidelineFactory dsFactory = new DSGuidelineFactory();
                System.out.println("xml "+sb.toString());
                guideline = dsFactory.createGuidelineFromXml(sb.toString());
                billingGuideLines.add(guideline);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }


    public List<DSConsequence> evaluateAndGetConsequences(String demographicNo, String providerNo) {
        System.out.println("passed in provider: " + providerNo + " demographicNo" + demographicNo);
        log.info("Decision Support 'evaluateAndGetConsequences' has been called, reading " + billingGuideLines.size() + " for this provider");
        ArrayList<DSConsequence> allResultingConsequences = new ArrayList();
        for (DSGuideline dsGuideline: billingGuideLines) {
            try {
                List<DSConsequence> newConsequences = dsGuideline.evaluate(demographicNo);
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

