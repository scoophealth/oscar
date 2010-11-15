/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.decisionSupport.dao.DSGuidelineDAO;
import org.oscarehr.decisionSupport.model.DSConsequence;
import org.oscarehr.decisionSupport.model.DSGuideline;
import org.oscarehr.decisionSupport.model.DecisionSupportException;
import org.oscarehr.util.MiscUtils;

/**
 *
 * @author apavel
 */
public abstract class DSService {
    private static final Logger logger = MiscUtils.getLogger();
    protected DSGuidelineDAO dsGuidelineDAO;

    public DSService() {
        this.setDsGuidelineDAO(new DSGuidelineDAO());
    }

    public List<DSConsequence> evaluateAndGetConsequences(String demographicNo, String providerNo) {
        logger.debug("passed in provider: " + providerNo + " demographicNo" + demographicNo);
        List<DSGuideline> dsGuidelines = this.dsGuidelineDAO.getDSGuidelinesByProvider(providerNo);
        logger.info("Decision Support 'evaluateAndGetConsequences' has been called, reading " + dsGuidelines.size() + " for this provider");
        ArrayList<DSConsequence> allResultingConsequences = new ArrayList();
        for (DSGuideline dsGuideline: dsGuidelines) {
            try {
                List<DSConsequence> newConsequences = dsGuideline.evaluate(demographicNo);
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

    public void fetchGuidelinesFromServiceInBackground(String providerNo) {
        DSServiceThread dsServiceThread = new DSServiceThread(this, providerNo);
        dsServiceThread.start();
    }
    public abstract void fetchGuidelinesFromService(String providerNo);

    public List<DSGuideline> getDsGuidelinesByProvider(String provider) {
        return dsGuidelineDAO.getDSGuidelinesByProvider(provider);
    }
    
    /**
     * @return the dsGuidelineDAO
     */
    public DSGuidelineDAO getDsGuidelineDAO() {
        return dsGuidelineDAO;
    }

    /**
     * @param dsGuidelineDAO the dsGuidelineDAO to set
     */
    public void setDsGuidelineDAO(DSGuidelineDAO dsGuidelineDAO) {
        this.dsGuidelineDAO = dsGuidelineDAO;
    }
}
