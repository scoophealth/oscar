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
package oscar.form.util;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.oscarehr.util.LoggedInInfo;
import oscar.dms.ConvertToEdoc.DocumentType;

/**
 * 
 * @author denniswarren
 * 
 * Use this class to transport the output of an Oscar from from any 
 * servlet into the ConvertToEdoc Utility.  
 */
public class FormTransportContainer {

	private HttpServletResponseWrapper responseWrapper;
	private final static DocumentType documentType = DocumentType.form;
	private final String HTML;
	private final String contextPath;
	private String realPath;
	private final LoggedInInfo loggedInInfo;
	private String subject;  
	private String providerNo; 
	private String demographicNo;
	private String formName;
	
	public FormTransportContainer( HttpServletResponse response, 
			HttpServletRequest request, final String formPath ) throws ServletException, IOException {

		responseWrapper = new HttpServletResponseWrapper(response) {
			private final StringWriter stringWriter = new StringWriter();

			@Override
			public PrintWriter getWriter() throws IOException {
			    return new PrintWriter(stringWriter);
			}

			@Override
			public String toString() {
			    return stringWriter.toString();
			}
		};	
		
		request.getRequestDispatcher( formPath ).include( request, responseWrapper );		
		this.HTML = responseWrapper.toString();
		
		this.loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession( request );		
	    this.contextPath = request.getContextPath();
	    
	}
	
	public final String getContextPath() {
		return this.contextPath;
	}

	public final String getRealPath() {
		return realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getHTML() {		
		return this.HTML;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(String demographicNo) {
		this.demographicNo = demographicNo;
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

	public LoggedInInfo getLoggedInInfo() {
		return loggedInInfo;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String toString() {
	    return ReflectionToStringBuilder.toString(this);
	}

}
