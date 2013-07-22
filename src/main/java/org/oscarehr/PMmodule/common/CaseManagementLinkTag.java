/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.common;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class CaseManagementLinkTag extends TagSupport {
	
	private Integer demographicNo;
	private String providerNo;
	private String providerName;

	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
                
                Date date = new Date();
               
                Format formatterDate = new SimpleDateFormat("yyyy-M-d");
                Format formatterTime = new SimpleDateFormat("HH:mm");
    
                String placeDate = formatterDate.format(date);
                String placeTime = formatterTime.format(date);

                        
                        
		try {
			StringBuilder builder = new StringBuilder();

			builder.append(req.getScheme()).append("://");
			builder.append(req.getServerName()).append(":");
			builder.append(req.getServerPort());
			builder.append(req.getContextPath()).append("/");

			builder.append("oscarEncounter/IncomingEncounter.do").append("?");
			builder.append("providerNo=").append(providerNo).append("&");
			builder.append("appointmentNo=").append(0).append("&");
			builder.append("demographicNo=").append(demographicNo).append("&");
			builder.append("curProviderNo=").append(providerNo).append("&");
			builder.append("reason=").append("&");
			builder.append("userName=").append(providerName).append("&");
			builder.append("curDate=").append(placeDate).append("&");
			builder.append("appointmentDate=").append(placeDate).append("&");
			builder.append("startTime=").append(placeTime).append("&");
			builder.append("status=").append("T");
			
			pageContext.getOut().write(builder.toString());
		} catch (IOException e) {
			throw new JspTagException("An IOException occurred.");
		}

		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

}
