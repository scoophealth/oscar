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
import java.util.Map;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.decisionSupport.model.DSConsequence;
import org.oscarehr.decisionSupport.model.DSGuideline;
import org.oscarehr.decisionSupport.model.DSGuidelineFactory;
import org.oscarehr.decisionSupport.model.DecisionSupportException;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

import oscar.OscarProperties;

/**
 * Class used to Manage BillingGuidelines.
 * Temporary and will be refactored to include the other billing systems. And probably more of a centralized rule repository.
 * @author jay
 */
public class BillingGuidelines  {

    private static Logger log = MiscUtils.getLogger();
    private List<DSGuideline> billingGuideLines;

    private static BillingGuidelines measurementTemplateFlowSheetConfig = new BillingGuidelines();
    private static String region;

    private static final String[] filenamesBC= {"BC250.xml","BC401a.xml","BC401b.xml","BC428.xml"};
    private static final String[] filenamesON= {"ON250.xml","ON428.xml","ONA003A.xml","ONK017A.xml","ONK130A.xml","ONK131A.xml","ONK132A.xml"};

    private static final Boolean DEFAULT_LOCATION = true;

    /**
     * Creates a new instance of MeasurementTemplateFlowSheetConfig
     */
    private BillingGuidelines() {}

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

    private Map<String,Boolean> populateFileList(String region) {

        Map<String,Boolean>filenamesList = new HashMap<String,Boolean>();

        //load internal billing rule files
        String defaultFileLocation = "oscar/oscarBilling/ca/decisionSupport/";

        if (region.equals("BC")) {
            for (String filename : filenamesBC) {
                filenamesList.put(defaultFileLocation + filename,DEFAULT_LOCATION);
            }
        } else if (region.equals("ON")) {
            for (String filename : filenamesON) {
                filenamesList.put(defaultFileLocation + filename,DEFAULT_LOCATION);
            }
        }

        //load external billing rule files
        String fileLocation = OscarProperties.getInstance().getProperty("decision_support_dir");
        if (fileLocation != null && !fileLocation.isEmpty()) {

            if (!fileLocation.endsWith("/")){
                fileLocation = fileLocation + "/";
            }

            String[] filenamesLocal = OscarProperties.getInstance().getProperty("decision_support_files").split(",");
            if (filenamesLocal != null) {
                for (String filename : filenamesLocal) {
                        filenamesList.put(fileLocation + filename, !DEFAULT_LOCATION);
                }
            }
        }

        return filenamesList;
    }

    /**
     * Loads all the guidelines from preset files in this package.  This will probably change to load them from a table in the database.
     */
    void loadGuidelines(String regionCode) {
        log.debug("LOADING GUIDELINES");
        billingGuideLines = new ArrayList<DSGuideline>();

        Map<String,Boolean> files = populateFileList(regionCode);
        if( files != null ) {
            for(String streamToGet : files.keySet()) {

                Boolean isDefaultFileLocation = files.get(streamToGet);
                StringBuilder sb = new StringBuilder();
                InputStream is = null;
                BufferedReader in = null;

                try{
                    log.debug("Trying to get "+ streamToGet);

                    if (isDefaultFileLocation){
                        is = this.getClass().getClassLoader().getResourceAsStream(streamToGet);
                    } else {
                        is = new FileInputStream(streamToGet);
                    }
                    in = new BufferedReader(new InputStreamReader(is));
                    String str;
                    while ((str = in.readLine()) != null) {
                        sb.append(str+"\n");
                    }
                    in.close();
                    DSGuidelineFactory dsFactory = new DSGuidelineFactory();
                    log.debug("xml "+sb.toString());
                    DSGuideline guideline = dsFactory.createGuidelineFromXml(sb.toString());
                    billingGuideLines.add(guideline);
                }catch(Exception e){
                    MiscUtils.getLogger().error("Error", e);
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {}
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {}
                    }
                }
            }
        } else {
            throw new RuntimeException("bill code not found");
        }
    }


    public List<DSConsequence> evaluateAndGetConsequences(LoggedInInfo loggedInInfo, String demographicNo, String providerNo) {
    	if(demographicNo == null) {
    		return new ArrayList<DSConsequence>();
    	}
        log.debug("passed in provider: " + providerNo + " demographicNo" + demographicNo);
        log.debug("Decision Support 'evaluateAndGetConsequences' has been called, reading " + billingGuideLines.size() + " for this provider");
        ArrayList<DSConsequence> allResultingConsequences = new ArrayList<DSConsequence>();
        for (DSGuideline dsGuideline: billingGuideLines) {
            try {
                List<DSConsequence> newConsequences = dsGuideline.evaluate(loggedInInfo, demographicNo, providerNo);
                if (newConsequences != null) {
                    allResultingConsequences.addAll(newConsequences);
                }
            } catch (DecisionSupportException dse) {
                log.warn("Failed to evaluate the patient against guideline, skipping guideline uuid " + dsGuideline.getUuid() + " (" + dsGuideline.getTitle() + ")");
            }
        }
        log.debug("Decision Support 'evaluateAndGetConsequences' finished, returing " + allResultingConsequences.size() + " consequences");
        return allResultingConsequences;
    }




}
