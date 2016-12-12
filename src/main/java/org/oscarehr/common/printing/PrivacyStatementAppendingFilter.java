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
package org.oscarehr.common.printing;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import oscar.OscarProperties;

/**
 * Filter class for handling confidentiality note printing. This class works by appending a confidentiality note
 * retrieved from the global OSCAR properties and appending it to the end of an HTML page.
 * 
 * <p></p>
 * 
 * When outputting the statement, the filter uses the following conventions:
 * 
 * <ul>
 * 
 * 	<li>The statement is outputted for HTML requests that result in generation of HTML content. This is done by checking the response type being created.</li>
 * 	<li>The statement is outputted only once per requests. This is ensure through setting a temporary session attribute
 * if session is available, or request attribute otherwise.</li>
 * 	<li>The statement is not outputted for AJAX-based requests, which is detected by checking {@link PrivacyStatementAppendingFilter#HTTP_HEADER_VALUE_AJAX_REQUESTED_WITH}
 * request header.</li>
 * 
 * </ul>
 * 
 * <p></p>  
 * 
 * Please note that this filter results in not well-formed HTML content being outputted to the browser, which in turn 
 * results in page rendered in Quirks mode.
 */
public class PrivacyStatementAppendingFilter implements Filter {

	public static final String HTTP_HEADER_VALUE_AJAX_REQUESTED_WITH = "XMLHttpRequest";
	public static final String HTTP_HEADER_NAME_AJAX_REQUESTED_WITH = "X-Requested-With";
	public static final String ATTRIBUTE_NAME_CONFIDENTIALITY_NOTE_PRINTED = "CONFIDENTIALITY_NOTE_PRINTED";

	private Set<String> exclusions = Collections.synchronizedSet(new HashSet<String>());
	
	private String getPrivacyStatement() { 
		if (OscarProperties.getConfidentialityStatement() == null || OscarProperties.getConfidentialityStatement().trim().isEmpty()) {
			return "";
		}
			return "<style type=\"text/css\"><!--\n" +
			".yesprint {\n" + 
			"	display: none;        \n" + 
			"}\n" +  
			"@media print {\n" + 
			"	.yesprint {\n" + 
			"		display:block;\n" + 
			"	}\n" + 
			"}\n" +
			"--></style>" +
			"<p class=\"yesprint\"><b>\n" + 
			OscarProperties.getConfidentialityStatement() +
			"</b><br/>" +
			"<b>END OF PRINTED DOCUMENT</b>" +
			"</p>";
	}
	
	@Override
    public void init(FilterConfig filterConfig) throws ServletException {
		String exclusionsParam = filterConfig.getInitParameter("exclusions");
		if (exclusionsParam == null || exclusionsParam.trim().isEmpty()) {
			return;
		}
		
		for(String ex : exclusionsParam.split(",")) {
			exclusions.add(ex.toLowerCase().trim());
		}
    }

	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		boolean isConfidentialityNotePrinted = false;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		try {
			// check if we are the first to print the request here
			isConfidentialityNotePrinted = isConfidentialtyNotePrinted(httpRequest);
			
			DelegatingServletResponse delegatingServletResponse = new DelegatingServletResponse(httpResponse);
			chain.doFilter(request, delegatingServletResponse);
			
			if (isExcluded(httpRequest)) {
				return;
			}
			
			if (isConfidentialityNotePrinted)
				return;
			
		    // ignore this stuff for non-html responses and AJAX queries
		    String contentType = delegatingServletResponse.getContentType();
		    if (contentType == null)
		    	return;
			boolean isHtmlResponse = contentType.toLowerCase().startsWith("text/html");
		    if (!isHtmlResponse)
		    	return; 
		    
		    // don't append for AJAX queries as well
		    String requestedWithHeader = httpRequest.getHeader(HTTP_HEADER_NAME_AJAX_REQUESTED_WITH);
		    boolean isAjaxRequest = requestedWithHeader != null && HTTP_HEADER_VALUE_AJAX_REQUESTED_WITH.equalsIgnoreCase(requestedWithHeader); 
		    if (isAjaxRequest) 
		    	return;
			
			printConfidentialityStatement(response, delegatingServletResponse);
		} finally {
			if (!isConfidentialityNotePrinted)
				resetConfidentialityNote((HttpServletRequest) request);
		}
    }
	
	private boolean isExcluded(HttpServletRequest request) {
		String servletPath = request.getServletPath();
		if (servletPath == null) {
			return false;
		}
		
		servletPath = servletPath.toLowerCase().trim();
		
		for(String ex : exclusions) {
			if (servletPath.startsWith(ex)) {
				return true;
			}
		}
	    return false;
    }

	private void printConfidentialityStatement(ServletResponse response, DelegatingServletResponse delegatingServletResponse) throws IOException {
		if (delegatingServletResponse.isResponseOutputStreamObtained()) {
			response.getOutputStream().write(getPrivacyStatement().getBytes());
		} else if (delegatingServletResponse.isResponseWriterObtained()) {
			response.getWriter().print(getPrivacyStatement());
		}
		
		response.flushBuffer();
    }

	private void resetConfidentialityNote(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute(ATTRIBUTE_NAME_CONFIDENTIALITY_NOTE_PRINTED) != null)
			session.removeAttribute(ATTRIBUTE_NAME_CONFIDENTIALITY_NOTE_PRINTED);
		request.removeAttribute(ATTRIBUTE_NAME_CONFIDENTIALITY_NOTE_PRINTED);
    }

	
	private boolean isConfidentialtyNotePrinted(HttpServletRequest request) {
		boolean isConfidentialtyNotePrinted = false;
		
		HttpSession session = request.getSession(false);
		if (session == null) {
			// fall back onto request
		    isConfidentialtyNotePrinted = request.getAttribute(ATTRIBUTE_NAME_CONFIDENTIALITY_NOTE_PRINTED) != null;
		    if (!isConfidentialtyNotePrinted)
		    	request.setAttribute(ATTRIBUTE_NAME_CONFIDENTIALITY_NOTE_PRINTED, Boolean.TRUE);
		    return isConfidentialtyNotePrinted;
		}
		
		isConfidentialtyNotePrinted = session.getAttribute(ATTRIBUTE_NAME_CONFIDENTIALITY_NOTE_PRINTED) != null;
		if (!isConfidentialtyNotePrinted)
			session.setAttribute(ATTRIBUTE_NAME_CONFIDENTIALITY_NOTE_PRINTED, Boolean.TRUE);
		return isConfidentialtyNotePrinted;
    }

	@Override
    public void destroy() {
    }

	
	/**
	 * Writer that prevents flushing and closing. Please be sure to close the underlying writer.
	 */
	private static class DelegatingWriter extends PrintWriter {

		public DelegatingWriter(Writer out) {
	        super(out);
        }

		@Override
        public void flush() {
			// avoid
        }

		@Override
        public void close() {
			// avoid
        }
	}
	
	/**
	 * Wrapper class for delegation of flushing and response closing. Please be careful with large responses,
	 * as it may cause memory problems.
	 */
	private static class DelegatingServletResponse extends HttpServletResponseWrapper {

		private boolean responseWriterObtained;
		private boolean responseOutputStreamObtained;
		
		private DelegatingWriter writer;
		
		public DelegatingServletResponse(HttpServletResponse response) {
			super(response);
			response.setBufferSize(1024 * 1024 * 1); // 1 Megs
        }

		@Override
        public ServletResponse getResponse() {
			responseWriterObtained = true;
	        return super.getResponse();
        }

		@Override
        public ServletOutputStream getOutputStream() throws IOException {
			responseOutputStreamObtained = true;
			return super.getOutputStream();
        }
		
		@Override
        public PrintWriter getWriter() throws IOException {
	        responseWriterObtained = true;
	        if (writer == null)
	        	writer = new DelegatingWriter(super.getWriter());
	        return writer;
        }

		public boolean isResponseWriterObtained() {
        	return responseWriterObtained;
        }

		public boolean isResponseOutputStreamObtained() {
        	return responseOutputStreamObtained;
        }

		@Override
        public void flushBuffer() throws IOException {
			// avoid flushing buffer
	        // super.flushBuffer();
        }		
	}
	
}
