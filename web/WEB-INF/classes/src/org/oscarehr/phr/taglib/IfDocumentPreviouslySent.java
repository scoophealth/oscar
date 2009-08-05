/*
 * IfTimeToTalk.java
 *
 * Created on August 20, 2007, 10:45 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.oscarehr.phr.taglib;


import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.oscarehr.phr.PHRConstants;
import org.oscarehr.phr.service.PHRService;

import org.oscarehr.util.SpringUtils;



/**
 *
 * @author Paul
 */
public class IfDocumentPreviouslySent extends TagSupport {
    Logger log = Logger.getLogger(this.getClass());
    private String documentOscarId;
    private String recipientDemographicNo;
    private boolean invertResult = false;

    @Override
    public int doStartTag() {
        PHRService service = (PHRService) SpringUtils.getBean("phrService");
        PHRConstants constants = (PHRConstants) SpringUtils.getBean("phrConstants");
        if (service.isIndivoRegistered(constants.DOCTYPE_BINARYDATA(), documentOscarId)) {
            if (invertResult) return SKIP_BODY;
            else return EVAL_BODY_INCLUDE;
        }
        if (invertResult) return EVAL_BODY_INCLUDE;
        else return SKIP_BODY;
    }

    /**
     * @return the oscarId
     */
    public String getDocumentOscarId() {
        return documentOscarId;
    }

    /**
     * @param oscarId the oscarId to set
     */
    public void setDocumentOscarId(String documentOscarId) {
        this.documentOscarId = documentOscarId;
    }

    /**
     * @return the recipientDemographicNo
     */
    public String getRecipientDemographicNo() {
        return recipientDemographicNo;
    }

    /**
     * @param recipientDemographicNo the recipientDemographicNo to set
     */
    public void setRecipientDemographicNo(String recipientDemographicNo) {
        this.recipientDemographicNo = recipientDemographicNo;
    }

    /**
     * @return the invertResult
     */
    public boolean isInvertResult() {
        return invertResult;
    }

    /**
     * @param invertResult the invertResult to set
     */
    public void setInvertResult(boolean invertResult) {
        this.invertResult = invertResult;
    }
}
