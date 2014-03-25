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

import java.math.BigInteger;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

import oscar.util.ConversionUtils;
import ca.ontario.health.edt.UpdateRequest;

public class UpdateForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    
    private String resourceId;
	private FormFile content;

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public FormFile getContent() {
		return content;
	}

	public void setContent(FormFile content) {
		this.content = content;
	}

	public UpdateRequest toUpdateRequest() {
		UpdateRequest result = new UpdateRequest();
		result.setResourceID(BigInteger.valueOf(ConversionUtils.fromIntString(resourceId)));
		try {
	        result.setContent(content.getFileData());
        } catch (Exception e) {
        	throw new RuntimeException("Unable to read upload data", e);
        }
	    return result;
    }

}
