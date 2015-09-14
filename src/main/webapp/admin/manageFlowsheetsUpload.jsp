<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%-- This JSP is the first page you see when you enter 'report by template' --%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.misc");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@ page import="java.util.*,oscar.oscarReport.reportByTemplate.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="org.apache.commons.fileupload.DiskFileUpload" %>
<%@ page import="org.apache.commons.fileupload.FileUpload" %>
<%@ page import="org.apache.commons.fileupload.FileItem" %>
<%@ page import="org.apache.commons.fileupload.FileUploadException" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.StringWriter" %>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.MeasurementTemplateFlowSheetConfig" %>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.MeasurementFlowSheet" %>
<%@ page import="org.oscarehr.common.model.Flowsheet" %>
<%@ page import="org.oscarehr.common.dao.FlowsheetDao" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>

<%
boolean isMultipart = FileUpload.isMultipartContent(request);
DiskFileUpload upload = new DiskFileUpload();

try {
    //           Parse the request
    @SuppressWarnings("unchecked")
    List<FileItem> items = upload.parseRequest(request);
    //Process the uploaded items
    Iterator<FileItem> iter = items.iterator();
    while (iter.hasNext()) {
        FileItem item = iter.next();

        if (item.isFormField()) {           
        } else {
            String contents = item.getString();
            
            //validate the data
            //TODO: make sure no duplicates
            MeasurementFlowSheet fs = null;
            fs = MeasurementTemplateFlowSheetConfig.getInstance().validateFlowsheet(contents);
            if(fs != null) {
            	//save to db
            	Flowsheet f = new Flowsheet();
            	f.setContent(contents);
            	f.setCreatedDate(new java.util.Date());
            	f.setEnabled(true);
            	f.setExternal(false);
            	f.setName(fs.getName());
            	
            	FlowsheetDao flowsheetDao = (FlowsheetDao)SpringUtils.getBean("flowsheetDao");
            	flowsheetDao.persist(f);
            	MeasurementTemplateFlowSheetConfig.getInstance().reloadFlowsheets();            	
            } else {
            	//error            	
            }                                   
        }
    }
} catch (FileUploadException e) {
    
} catch (Exception e) {
    
}   

response.sendRedirect("manageFlowsheets.jsp");
%>
