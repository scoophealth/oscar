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
 * programExclusiveViewTag.java
 *
 * Created on May 24, 2007, 12:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.caisi.core.web.tld;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.dao.ProviderDefaultProgramDao;
import org.oscarehr.util.SpringUtils;

/**
 *
 * @author cronnie
 */
public class programExclusiveViewTag extends TagSupport {

    private static final long serialVersionUID = 1L;
    
	/**
	 * Creates a new instance of programExclusiveViewTag
	 */
	public programExclusiveViewTag() {
		exclusiveView = "no";
	}

	public void setProviderNo(String providerNo1) {
		providerNo = providerNo1;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setValue(String value1) {
		value = value1;
	}

	public String getValue() {
		return value;
	}

	public int doStartTag() throws JspException {
		ProviderDefaultProgramDao dao = SpringUtils.getBean(ProviderDefaultProgramDao.class);
		for (Program p : dao.findProgramsByProvider(providerNo)) {
			exclusiveView = p.getExclusiveView();
			if (exclusiveView.equals("")) {
				exclusiveView = "no";
			}
		}

		/* For the time being, only the Appointment/Oscar view can be set exclusive.
		 * If necessary, modify the following code and relating .jsp to enable other view(s) exclusive.
		 *    exclusiveView = "no" -> no exclusive view set, user can switch between views
		 *    exclusiveView = "appointment" -> Appointment/Oscar view exclusive
		 *    exclusiveView = "case-management" -> Case-management view exclusive
		 */
		if (exclusiveView.equalsIgnoreCase(value)) {
			return (EVAL_BODY_INCLUDE);
		} else {
			return (SKIP_BODY);
		}
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	private String providerNo;
	private String value;
	private String exclusiveView;
}
