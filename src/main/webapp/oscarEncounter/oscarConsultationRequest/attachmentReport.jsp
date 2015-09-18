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
<security:oscarSec roleName="<%=roleName$%>" objectName="_con" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_con");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page
	import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, oscar.util.*, java.net.*, oscar.oscarLab.ca.on.*, oscar.MyDateFormat, oscar.dms.*, oscar.dms.data.*"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.util.SessionConstants"%>

<%
//preliminary JSP code

LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
   		 
// "Module" and "function" is the same thing (old dms module)
String module = "demographic";
String demoNo= request.getParameter("demographicNo");
String reqId = request.getParameter("reqId");
String provNo = request.getParameter("providerNo");
String demoName = EDocUtil.getDemographicName(loggedInInfo, demoNo);

ArrayList doctypes = EDocUtil.getDoctypes(module);

%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="dms.documentReport.title" /></title>
<link rel="stylesheet" type="text/css"
	href="../../share/css/OscarStandardLayout.css" />
<script type="text/javascript" src="../../share/javascript/Oscar.js"></script>
<script type="text/javascript" src="../../share/javascript/prototype.js"></script>

<link rel="stylesheet" type="text/css"
	href="../../share/css/niftyCorners.css" />
<link rel="stylesheet" type="text/css" href="report.css" />
<link rel="stylesheet" type="text/css"
	href="../../share/css/niftyPrint.css" media="print" />
<script type="text/javascript" src="../../share/javascript/nifty.js"></script>
<script type="text/javascript">
window.onload=function(){
if(!NiftyCheck())
    return;

Rounded("div.doclist","top","transparent", "#ccccd7", "small border #ccccd7");
Rounded("div.doclist","bottom","transparent", "#e0ecff", "small border #ccccd7");
Rounded("div.leftplane","top", "transparent", "#CCCCFF","small border #ccccff");
Rounded("div.leftplane","bottom","transparent","#EEEEFF","small border #ccccff");

}

function checkAll(master){
   var frmList = document.forms[0].elements;

   for (i =0; i < frmList.length; i++){
      if( frmList[i].type == "checkbox" && frmList[i] != master )
        frmList[i].checked = !frmList[i].checked;
   }

}

function verifyChecks(t){
   var ret = false;

   if( t.docNo != undefined ) {

        if( t.docNo.length == undefined )
            ret = t.docNo.checked;
        else {
            for( var idx = 0; idx < t.docNo.length; ++idx ) {
                if( t.docNo[idx].checked ) {
                    ret = true;
                    break;
                }
            } //end for
        } //else if
   }

   if( !ret )
        alert("<bean:message key="oscarEncounter.oscarConsultationRequest.PrintReport.pdfPrintErr"/>");
   return ret;
}

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

<script src="../../share/javascript/prototype.js" type="text/javascript"></script>
<body class="bodystyle">

<table class="MainTable" id="scrollNumber1" name="encounterTable"
	style="margin: 0px;">
	<tr>
		<%--
         <td class="MainTableLeftColumn" valign="top">
             <div class="leftplane">
                  <h3>&nbsp; Tags</h3>
                  <div style="background-color: #EEEEFF;">
                      <ul>
                         <li>Tag 1</li>
                         <li>Tag 2</li>
                         <li>Tag 3</li>
                      </ul>
                  </div>
             </div>
         </td>
         --%>
		<td class="MainTableRightColumn" colspan="2" valign="top"><%--<jsp:include page="addDocument.jsp"/>--%>

		<html:form action="/oscarEncounter/oscarConsultation/printAttached"
			onsubmit="return verifyChecks(this);">

			<div class="documentLists"><%-- STUFF TO DISPLAY --%> <%
                ArrayList consultdocs = EDocUtil.listDocs(loggedInInfo, demoNo, reqId, EDocUtil.ATTACHED);
             %>
			<div class="doclist">
			<div class="headerline">
			<div class="docHeading"><%=demoName%> <bean:message
				key="oscarEncounter.oscarConsultationRequest.PrintReport.docs" /></div>
			</div>
			<div id="documentsInnerDiv1" style="background-color: #f2f7ff;">
			<table id="privateDocs" class="docTable">
				<tr>
					<td><input class="tightCheckbox" type="checkbox" id="pdfCheck"
						onclick="checkAll(this);" /></td>
					<td width="30%"><b><bean:message
						key="dms.documentReport.msgDocDesc" /></b></td>
					<td width="10%"><b><bean:message
						key="oscarEncounter.oscarConsultationRequest.PrintReport.ContentType" /></b></td>
					<td width="15%"><b><bean:message
						key="dms.documentReport.msgDocType" /></b></td>
					<td width="20%"><b><bean:message
						key="dms.documentReport.msgCreator" /></b></td>
					<td width="25%"><b><bean:message
						key="oscarEncounter.oscarConsultationRequest.PrintReport.Observation" /></b></td>
				</tr>

				<%
                for (int i2=0; i2<consultdocs.size(); i2++) {
                    EDoc curdoc = (EDoc) consultdocs.get(i2);
                    //content type (take everything following '/')
                    int slash = 0;
                    String contentType = "";
                    if ((slash = curdoc.getContentType().indexOf('/')) != -1) {
                        contentType = curdoc.getContentType().substring(slash+1);
                    }
                    String dStatus = "";
                    if ((curdoc.getStatus() + "").compareTo("A") == 0) dStatus="active";
                    else if ((curdoc.getStatus() + "").compareTo("H") == 0) dStatus="html";
            %>
				<tr>
					<td>
					<% if (curdoc.isPDF()){%> <input class="tightCheckbox1"
						type="checkbox" name="docNo" id="docNo<%=curdoc.getDocId()%>"
						value="<%=curdoc.getFileName()%>"
						style="margin: 0px; padding: 0px;" /> <%}else{%> &nbsp; <%}%>
					</td>
					<td>
					<%
                              String url = "documentGetFile.jsp?document=" + StringEscapeUtils.escapeJavaScript(curdoc.getFileName()) + "&type=" + dStatus + "&doc_no=" + curdoc.getDocId();
                              if (curdoc.getStatus() == 'H') { %> <a
						href="<%=url%>" target="_blank"> <% } else { %> <a
						href="javascript:popup1(480, 480, '<%=url%>', 'edoc<%=i2%>')">
					<% } %> <%=curdoc.getDescription()%></td>
					<td><%=contentType%></td>
					<td><%=curdoc.getType()%></td>
					<td><%=curdoc.getCreatorName()%></td>
					<td><%=curdoc.getObservationDate()%></td>
				</tr>

				<%}
                CommonLabResultData consultLabs = new CommonLabResultData();
                ArrayList attachedLabs = consultLabs.populateLabResultsData(loggedInInfo, demoNo, reqId, CommonLabResultData.ATTACHED);

                LabResultData result;
                String labURL;
                String labDisplayName;
                for(int idx = 0; idx < attachedLabs.size(); ++idx) {
                    result = (LabResultData) attachedLabs.get(idx);
                    if ( result.isMDS() ){
                        labURL ="../../oscarMDS/SegmentDisplay.jsp?providerNo="+provNo+"&segmentID="+result.segmentID+"&status="+result.getReportStatus();
                        labDisplayName = result.getDiscipline()+" "+result.getDateTime();
                    }else if (result.isCML()){
                        labURL ="../../lab/CA/ON/CMLDisplay.jsp?providerNo="+provNo+"&segmentID="+result.segmentID;
                        labDisplayName = result.getDiscipline()+" "+result.getDateTime();
                    }else if (result.isHL7TEXT()){
                        labDisplayName = result.getDiscipline();
                        labURL ="../../lab/CA/ALL/labDisplay.jsp?providerNo="+provNo+"&segmentID="+result.segmentID;
                    }else{
                        labURL ="../../lab/CA/BC/labDisplay.jsp?segmentID="+result.segmentID+"&providerNo="+provNo;
                        labDisplayName = result.getDiscipline()+" "+result.getDateTime();
                    }




            %>
				<tr>
					<td>&nbsp;</td>
					<td><a href="<%=labURL%>" target="_blank"><%=labDisplayName%></a></td>
					<td>html</td>
					<td>Lab Result</td>
					<td style="text-align: center">---</td>
					<td><%=result.getDateTime()%></td>
				</tr>

				<%
                }

            if (consultdocs.size() == 0 && attachedLabs.size() == 0 ) {%>
				<tr>
					<td colspan="6">No attached documents to display</td>
				</tr>
				<%}%>
			</table>

			</div>
			</div>
			</div>
			<div><input type="button" name="Button"
				value="<bean:message key="dms.documentReport.btnDoneClose"/>"
				onclick=self.close();> <input type="submit"
				value="Print PDFs" /> <input type="button" value="Back"
				onclick="window.history.back()" /></div>
		</html:form></td>
	</tr>
	<tr>
		<td colspan="2" class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>


</body>
</html:html>
