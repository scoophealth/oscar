/**
 * Copyright (c) 2014-2015. KAI Innovations Inc. All Rights Reserved.
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
package org.oscarehr.integration.mcedt.web;

import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.oscarehr.util.MiscUtils;

public class UploadForm extends ActionForm {

	private static final long serialVersionUID = 1L;
    
    private static final Logger logger = MiscUtils.getLogger();
    
	private String description;
	private String resourceType;
	private String fileName;
	private BigInteger resourceId;
	private FormFile addUploadFile;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getFileName() {
	    return fileName;
    }

	public void setFileName(String fileName) {
	    this.fileName = fileName;
    }

	public BigInteger getResourceId() {
		return resourceId;
	}

	public void setResourceId(BigInteger resourceId) {
		this.resourceId = resourceId;
	}
	@Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors result = new ActionErrors();
		if (getResourceType() == null || getResourceType().isEmpty()) {
			result.add("resourceType", new ActionMessage("error.mcedt.uploadAddForm.resourceType.required"));
		}
		
		if (getDescription() == null || getDescription().isEmpty()) {
			result.add("description", new ActionMessage("error.mcedt.uploadAddForm.description.required"));
		}
		
		if (getFileName() == null || getFileName().isEmpty()) {
			result.add("content", new ActionMessage("error.mcedt.uploadAddForm.content.required"));
		}
		
		if (getResourceId() == null || getResourceId().equals(0)) {
			result.add("resourceType", new ActionMessage("error.mcedt.uploadAddForm.resourceType.required"));
		}
				
	    return result;
    }

	public FormFile getAddUploadFile() {
	    return addUploadFile;
    }

	public void setAddUploadFile(FormFile addUpload) {
	    this.addUploadFile = addUpload;
    }

}
