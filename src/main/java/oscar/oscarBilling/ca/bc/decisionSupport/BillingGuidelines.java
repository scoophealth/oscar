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


package oscar.oscarBilling.ca.bc.decisionSupport;

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
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;

/**
 * Class used to Manage BillingGuidelines.
 * Temporary and will be refactored to include the other billing systems. And probably more of a centralized rule repository.
 * @author jay
 */
public class BillingGuidelines  {

    private static Logger log = MiscUtils.getLogger();

    private List<DSGuideline> billingGuideLines = null ;


    static BillingGuidelines measurementTemplateFlowSheetConfig = new BillingGuidelines();
    static String region = "";

    HashMap<String,String[]> filenameMap = new HashMap<String,String[]>();
    String[] filenamesBC= {"BC250.xml","BC401a.xml","BC401b.xml","BC428.xml"};
    String[] filenamesON= {"ON250.xml","ON428.xml"};
    String[] filenameON250 = {"ON250.xml"};
    String[] filenameON428 = {"ON428.xml"};


    /**
     * Creates a new instance of MeasurementTemplateFlowSheetConfig
     */
    private BillingGuidelines() {
        filenameMap.put("BC", filenamesBC);
        filenameMap.put("ON", filenamesON);
        filenameMap.put("250", filenameON250);
        filenameMap.put("428", filenameON428);
    }

    static public BillingGuidelines getInstance(String code) {
        if (measurementTemplateFlowSheetConfig.billingGuideLines == null || !code.equals(region)) {

                region = code;
                measurementTemplateFlowSheetConfig.loadGuidelines(region);


        }
        return measurementTemplateFlowSheetConfig;
    }

    static public BillingGuidelines getInstance() {
        String tmpRegion = OscarProperties.getInstance().getProperty("billregion","");
        if (measurementTemplateFlowSheetConfig.billingGuideLines == null || !tmpRegion.equals(region)) {

                region = tmpRegion;
                measurementTemplateFlowSheetConfig.loadGuidelines(region);


        }
        return measurementTemplateFlowSheetConfig;
    }

    /**
     * Loads all the guidelines from preset files in this package.  This will probably change to load them from a table in the database.
     */
    void loadGuidelines(String regionCode) {
        log.debug("LOADING FLOWSSHEETS");
        billingGuideLines = new ArrayList<DSGuideline>();

        String[] filenames = filenameMap.get(regionCode);
        if( filenames != null ) {
        for (String filename : filenames) {
            DSGuideline guideline = null;
            StringBuilder sb = new StringBuilder();
            try{
                    String streamToGet = "oscar/oscarBilling/ca/decisionSupport/"+filename;
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
                billingGuideLines.add(guideline);
            }catch(Exception e){
                MiscUtils.getLogger().error("Error", e);
            }
        }
    }
        else {
            throw new RuntimeException("bill code not found");
        }
    }


    public List<DSConsequence> evaluateAndGetConsequences(String demographicNo, String providerNo) {
        log.debug("passed in provider: " + providerNo + " demographicNo" + demographicNo);
        log.info("Decision Support 'evaluateAndGetConsequences' has been called, reading " + billingGuideLines.size() + " for this provider");
        ArrayList<DSConsequence> allResultingConsequences = new ArrayList<DSConsequence>();
        for (DSGuideline dsGuideline: billingGuideLines) {
            try {
                List<DSConsequence> newConsequences = dsGuideline.evaluate(demographicNo, providerNo);
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
