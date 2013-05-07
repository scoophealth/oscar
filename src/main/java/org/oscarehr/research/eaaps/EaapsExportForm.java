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
package org.oscarehr.research.eaaps;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.oscarehr.common.dao.StudyDao;
import org.oscarehr.common.model.Study;
import org.oscarehr.util.SpringUtils;

/**
 * Form class for the {@link EaapsExportAction}
 */
public class EaapsExportForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    
    protected int studyId;
    
    protected boolean acknowledged = false;
    
    protected String acknowledgedString;
    
    public List<Study> getStudies() {
    	StudyDao dao = SpringUtils.getBean(StudyDao.class);
    	return dao.findAll();
    }

    /**
     * Validates the form by verifying that the study is selected and that 
     * the export terms and conditions have been acknowledged. 
     * 
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    	ActionErrors errors = new ActionErrors();
    	if (getStudyId() <= 0) {
    		errors.add("studyId", new ActionMessage("Please select a study", false));
    	}
    	if(!isAcknowledged()) {
    		errors.add("acknowledged", new ActionMessage("Please acknowledge export requirements", false));
    	}
    	return errors;
    }
    
    public EaapsExportForm() {
    }
        
	public int getStudyId() {
		return studyId;
	}

	public void setStudyId(int studyId) {
		this.studyId = studyId;
	}

	public boolean isAcknowledged() {
		return acknowledged;
	}
	
	public boolean getAcknowledged() {
		return acknowledged;
	}

	public void setAcknowledged(boolean acknowledged) {
		this.acknowledged = acknowledged;
	}

	public String getAcknowledgedString() {
		return acknowledgedString;
	}

	public void setAcknowledgedString(String acknowledgedString) {
		this.acknowledgedString = acknowledgedString;
	}
    
}
