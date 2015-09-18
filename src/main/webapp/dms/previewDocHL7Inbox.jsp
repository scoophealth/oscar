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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_edoc" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_edoc");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="/WEB-INF/oscarProperties-tag.tld" prefix="oscarProp"%>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="indivo"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, oscar.util.*, java.net.*,oscar.MyDateFormat, oscar.dms.*, oscar.dms.data.*, oscar.oscarProvider.data.ProviderMyOscarIdData, oscar.oscarDemographic.data.DemographicData"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="oscar.oscarMDS.data.*,oscar.oscarLab.ca.on.*"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="dms.documentReport.title" /></title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css" />
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
<script type="text/javascript" src="../share/javascript/prototype.js"></script>

<link rel="stylesheet" type="text/css"
	href="../share/css/niftyCorners.css" />
<link rel="stylesheet" type="text/css" href="dms.css" />
<link rel="stylesheet" type="text/css"
	href="../share/css/niftyPrint.css" media="print" />
<script type="text/javascript" src="../share/javascript/nifty.js"></script>
<script type="text/javascript" src="../phr/phr.js"></script>
<script type="text/javascript">
function popup1(height, width, url, windowName){
  var page = url;
  windowprops = "height="+height+",width="+width+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(url, windowName, windowprops);
  if (popup != null){
    if (popup.opener == null){
      popup.opener = self;
    }
  }
  popup.focus();

}
</script>

<script src="../share/javascript/prototype.js" type="text/javascript"></script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />


<script type="text/javascript" language=javascript>

function popupStart(vheight,vwidth,varpage) {
    popupStart(vheight,vwidth,varpage,"helpwindow");
}

function popupStart(vheight,vwidth,varpage,windowname) {
    var page = varpage;
    windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
    var popup=window.open(varpage, windowname, windowprops);
}

function reportWindow(page) {
    windowprops="height=660, width=960, location=no, scrollbars=yes, menubars=no, toolbars=no, resizable=yes, top=0, left=0";
    var popup = window.open(page, "labreport", windowprops);
    popup.focus();
}

function checkSelected() {
    aBoxIsChecked = false;
    if (document.reassignForm.flaggedLabs.length == undefined) {
        if (document.reassignForm.flaggedLabs.checked == true) {
            aBoxIsChecked = true;
        }
    } else {
        for (i=0; i < document.reassignForm.flaggedLabs.length; i++) {
            if (document.reassignForm.flaggedLabs[i].checked == true) {
                aBoxIsChecked = true;
            }
        }
    }
    if (aBoxIsChecked) {
        popupStart(300, 400, 'SelectProvider.jsp', 'providerselect');
    } else {
        alert("<bean:message key="oscarMDS.index.msgSelectOneLab"/>");
    }
}

function submitFile(){
   aBoxIsChecked = false;
    if (document.reassignForm.flaggedLabs.length == undefined) {
        if (document.reassignForm.flaggedLabs.checked == true) {
            aBoxIsChecked = true;
        }
    } else {
        for (i=0; i < document.reassignForm.flaggedLabs.length; i++) {
            if (document.reassignForm.flaggedLabs[i].checked == true) {
                aBoxIsChecked = true;
            }
        }
    }
    if (aBoxIsChecked) {
       document.reassignForm.action = '../oscarLab/FileLabs.do';
       document.reassignForm.submit();
    }
}

function checkAll(formId){
   var f = document.getElementById(formId);
   var val = f.checkA.checked;
   for (i =0; i < f.flaggedLabs.length; i++){
      f.flaggedLabs[i].checked = val;
   }
}
</script>
</head>
<body class="bodystyle">
<%
                ArrayList categories = new ArrayList();
                ArrayList categoryKeys = new ArrayList();
                ArrayList privatedocs = new ArrayList();
                privatedocs = (ArrayList)request.getAttribute("docPreview");//new
                String demoName=(String)request.getAttribute("demoName");//new
                String providerNo=(String)request.getAttribute("providerNo");
                String searchProviderNo=(String)request.getAttribute("searchProviderNo");
                String ackStatus=(String)request.getAttribute("ackStatus");
                
                if ( ackStatus == null ) { ackStatus = "N"; } // default to only new lab reports
                if ( providerNo == null ) { providerNo = ""; }
                if ( searchProviderNo == null ) { searchProviderNo = providerNo; }
                categories.add(privatedocs);
                categoryKeys.add(demoName + "'s Private Documents");
                ArrayList labs = (ArrayList)request.getAttribute("labPreview");

%>
<table class="MainTable" id="scrollNumber1" name="encounterTable"
	style="margin: 0px;">
	<tr>
		<td class="MainTableRightColumn" colspan="2" valign="top">
                    <html:form action="/dms/combinePDFs">			
			<div class="documentLists"><%-- STUFF TO DISPLAY --%>
                             <%
                for (int i=0; i<categories.size();i++) {                   
                    ArrayList category = (ArrayList) categories.get(i);
             %>
			<div class="doclist">			
			<div id="documentsInnerDiv<%=i%>" style="background-color: #f2f7ff;">
			<table id="privateDocs" class="docTable" >
                            <tr>
                                <th align="center" colspan="7"><a style="color:black;"><%=demoName%>'s Documents</a></th>

                            </tr>
				<tr>					
					<th width="30%" ><b><a style="color:black;"><bean:message
						key="dms.documentReport.msgDocDesc" /></a></b></th>
					<th width="8%"><b><a style="color:black;">
						    <bean:message key="dms.documentReport.msgContent"/></a></b></th>
					<th width="8%"><b><a style="color:black;">
						    <bean:message key="dms.documentReport.msgType"/></a></b></th>
					<th width="12%"><b><a style="color:black;"><bean:message
						key="dms.documentReport.msgCreator" /></a></b></th>
					<th width="12%"><b><a style="color:black;">
						<bean:message key="dms.documentReport.msgResponsible"/></a></b></th>
					<th width="10%"><a style="color:black;" title="Observation Date"><b><bean:message key="dms.documentReport.msgDate"/></b></a></th>
					<th width="12%"><a style="color:black;"><b><bean:message key="dms.documentReport.msgReviewer"/></b></a></th>
				</tr>

				<%
                for (int i2=0; i2<category.size(); i2++) {
                    EDoc curdoc = (EDoc) category.get(i2);
                    //content type (take everything following '/')
                    int slash = 0;
                    String contentType = "";
                    if ((slash = curdoc.getContentType().indexOf('/')) != -1) {
                        contentType = curdoc.getContentType().substring(slash+1);
                    } else {
			contentType = curdoc.getContentType();
		    }
                    String dStatus = "";
                    if ((curdoc.getStatus() + "").compareTo("H") == 0)
                        dStatus="html";
                    else
                        dStatus="active";
		    String reviewerName = curdoc.getReviewerName();
		    if (reviewerName.equals("")) reviewerName = "- - -";
                    String docId=curdoc.getDocId();
            %>
				<tr>
					
					<td><a> 
                                               <%=curdoc.getDescription()%></a></td>
					<td><%=contentType%></td>
					<td><%=curdoc.getType()%></td>
					<td><%=curdoc.getCreatorName()%></td>
					<td><%=curdoc.getResponsibleName()%></td>
					<td><%=curdoc.getObservationDate()%></td>
					<td><%=reviewerName%></td>
				</tr>

				<%}
            if (category.size() == 0) {%>
				<tr>
					<td colspan="6"><bean:message key="dms.documentReport.msgNoDocumentsToDisplay"/></td>
				</tr>
				<%}%>
			</table>

			</div>
			</div>
			<%}%>
			</div>			
		</html:form></td>
	</tr>
</table>





<form name="reassignForm" method="post" action="ReportReassign.do"
	id="lab_form">
<table oldclass="MainTable" id="scrollNumber1" border="0"
	name="encounterTable" cellspacing="0" cellpadding="3" width="100%" style="background-color:#f2f7ff">
    <tr><th align="center" colspan="5"><%=demoName%>'s Labs</th>
</tr>
	<tr>
		<th align="left" valign="bottom" class="cell"><bean:message
			key="oscarMDS.index.msgDiscipline" /></th>
		<th align="left" valign="bottom" class="cell"><bean:message
			key="oscarMDS.index.msgDateTest" /></th>
		<th align="left" valign="bottom" class="cell"><bean:message
			key="oscarMDS.index.msgRequestingClient" /></th>
		<th align="left" valign="bottom" class="cell"><bean:message
			key="oscarMDS.index.msgResultStatus" /></th>
		<th align="left" valign="bottom" class="cell"><bean:message
			key="oscarMDS.index.msgReportStatus" /></th>
	</tr>

	<%
            int startIndex = 0;
            if ( request.getParameter("startIndex") != null ) {
                startIndex = Integer.parseInt(request.getParameter("startIndex"));
            }
            int endIndex = startIndex+20;
            if ( labs.size() < endIndex ) {
                endIndex = labs.size();
            }

            for (int i = startIndex; i < endIndex; i++) {


                LabResultData result =  (LabResultData) labs.get(i);

                String segmentID        = (String) result.segmentID;
                String status           = (String) result.acknowledgedStatus;
                String resultStatus     = (String) result.resultStatus;

                String bgcolor = i % 2 == 0 ? "#e0e0ff" : "#ccccff" ;
                if (!result.isMatchedToPatient()){
                   bgcolor = "#FFCC00";
                }
                %>

	<tr bgcolor="<%=bgcolor%>"
		class="<%= (result.isAbnormal() ? "AbnormalRes" : "NormalRes" ) %>">		
		<td nowrap>
		<% if (result.isHL7TEXT()) {%> <a>
			<!--href="javascript:reportWindow('../lab/CA/ALL/labDisplay.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>')"--><%=(String) result.getDiscipline()%></a>
		<% } else {%> <a>
			<!--href="javascript:reportWindow('../lab/CA/BC/labDisplay.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>')"--><%=(String) result.getDiscipline()%></a>
		<% }%>
		</td>
		<td nowrap><%= (String) result.getDateTime()%></td>
		<td nowrap><%= (String) result.getRequestingClient()%></td>
		<td nowrap><%= (result.isAbnormal() ? "Abnormal" : "" ) %></td>

		<td nowrap><%= ( (String) ( result.isFinal() ? "Final" : "Partial") )%>
		</td>
	</tr>
	<% }

            if ( endIndex == 0 ) { %>
	<tr>
		<td colspan="9" align="center"><i><bean:message
			key="oscarMDS.index.msgNoReports" /></i></td>
	</tr>
	<% } %>	
</table>
</form>

</body>
</html:html>
