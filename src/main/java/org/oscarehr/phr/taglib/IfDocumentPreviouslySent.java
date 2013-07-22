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
import org.oscarehr.myoscar.commons.MedicalDataType;
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
        if (service.isIndivoRegistered(MedicalDataType.BINARY_DOCUMENT.name(), documentOscarId)) {
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
