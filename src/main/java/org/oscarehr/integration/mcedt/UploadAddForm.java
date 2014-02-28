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
package org.oscarehr.integration.mcedt;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.oscarehr.util.MiscUtils;

import ca.ontario.health.edt.UploadData;

public class UploadAddForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = MiscUtils.getLogger();
    
	private String description;
	private String resourceType;
	private FormFile content;

	public UploadData toUpload() {
		UploadData result = new UploadData();
		result.setDescription(getDescription());
		result.setResourceType(getResourceType());
		try {
			result.setContent(getContent().getFileData());
		} catch (Exception e) {
			logger.error("Unable to read upload file", e);
			
			throw new RuntimeException("Unable to read upload file", e);
		}
		return result;
	}

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

	public FormFile getContent() {
		return content;
	}

	public void setContent(FormFile content) {
		this.content = content;
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
		
		if (getContent() == null || getContent().getFileName() == null || getContent().getFileName().isEmpty()) {
			result.add("content", new ActionMessage("error.mcedt.uploadAddForm.content.required"));
		}
				
	    return result;
    }

}
