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

<%-- 
    Document   : SendToPhrPreview
    Created on : 8-Jul-2009, 12:18:50 PM
    Author     : apavel
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_phr" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_phr");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page import="oscar.oscarDemographic.data.DemographicData"%>
<%@ page import="java.util.Enumeration,oscar.dms.EDoc,oscar.dms.EDocUtil"%>
<%@ page import="org.oscarehr.util.SpringUtils,org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="org.oscarehr.common.dao.Hl7TextInfoDao,org.oscarehr.common.model.Hl7TextInfo"%>




<%@ taglib uri="/WEB-INF/phr-tag.tld" prefix="phr"%>

<%
String demographicNo = request.getParameter("demographic_no");
if (demographicNo == null) demographicNo = request.getParameter("demographicNo");
DemographicData demographicData = new DemographicData();
String demographicName = demographicData.getDemographicFirstLastName(LoggedInInfo.getLoggedInInfoFromSession(request), demographicNo);

String documentName = "";

if(request.getParameter("labId") != null){
	Hl7TextInfoDao hl7TextInfoDao = (Hl7TextInfoDao) SpringUtils.getBean("hl7TextInfoDao");
	int lab_no = Integer.parseInt(request.getParameter("labId"));
	Hl7TextInfo hl7Lab = hl7TextInfoDao.findLabId(lab_no);
	documentName = hl7Lab.getLabelOrDiscipline();
}else if(request.getParameter("module") != null && request.getParameter("module").equals("document")) {
	String documentNo = request.getParameter("documentNo");
	EDoc eDoc = EDocUtil.getDoc(documentNo);
	documentName = eDoc.getDescription();
}
	
%>

<html>
    <head>
        <title>Preview</title>
		<link href="../library/bootstrap/3.0.0/css/bootstrap.css" rel="stylesheet">
        <script type="text/javascript" language="JavaScript" src="phr.js"></script>
        <script type="text/javascript" language="JavaScript">
            function send(obj) {
                if (document.getElementById('demographic_no').value == "") {
                    alert("Cannot find demographic number");
                    return false;
                }
                if (obj.form.subject.value == "") {
                    alert("Subject cannot be empty");
                    obj.form.subject.focus();
                    return false;
                }
                obj.disabled = true;
                obj.value = "Sending....";
                //phrActionPopup("google.ca");
                obj.form.method.value = 'send';
                obj.form.submit();
            }
            function onloadd() {
        <%--String labId = request.getParameter("labId");%>
        <phr:ifDocumentPreviouslySent documentOscarId="<%=labId%>" recipientDemographicNo="<%=demographicNo%>">
            alert("test");
        </phr:ifDocumentPreviouslySent>---%>
            }
        </script>

    </head>
    <body onload="onloadd()">
    	
        <div class="page-header" style="margin:5px"><h4>Send to <%=StringEscapeUtils.escapeHtml(demographicName)%>'s PHR - Preview</h4></div>
        <div class="container">
        <form action="<%=request.getContextPath()%>/SendToPhr.do" method="POST" >
            <input type="hidden" name="demographic_no" value="<%=demographicNo%>" id="demographic_no">
            <input type="hidden" name="method" value="send">
            <input type="Hidden" name="SendToPhrPreview" value="yes">
            <%Enumeration<String> parameterNames = request.getParameterNames();
              while (parameterNames.hasMoreElements()) {
              	String parameterName = parameterNames.nextElement();
                if (parameterName.equals("method")) continue;
                for (String parameterValue: request.getParameterValues(parameterName)) {%>
                	<input type="hidden" name="<%=parameterName%>" value="<%=parameterValue%>">
			<%} }%>
			
            <div class="form-group">
    			<label for="subject">Document Name</label>
    			<input type="text" name="subject" class="form-control" id="subject" placeholder="" value="<%=documentName%>">
  			</div>
  			<div class="form-group">
    			<label for="message">Annotation</label>
    			<textarea name="message"   class="form-control" rows="3" ></textarea>
  			</div>
  			<div class="form-group">
  				<input type="submit" onclick="this.form.method.value='documentPreview'" value="Document Preview" class="btn btn-default">
  			 	<input type="button" onclick="window.close()" value="Cancel"  class="btn btn-default">
                <input type="button" onclick="send(this)" value="Send" class="btn btn-primary">
  			</div>
           
        </form>
        <iframe name="iframe" id="iframe" style="border:0px; width: 0px; height: 0px;"/>
        </div>
    </body>
</html>
