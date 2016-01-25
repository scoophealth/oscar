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
<security:oscarSec roleName="<%=roleName$%>" objectName="_con" rights="w" reverse="<%=true%>">
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
<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, oscar.util.*, java.net.*,oscar.MyDateFormat, oscar.dms.*, oscar.dms.data.*, oscar.oscarEncounter.oscarConsultationRequest.pageUtil.ConsultationAttachDocs"%>
<%@ page import="oscar.oscarLab.ca.on.*"%>
<%@ page import="oscar.oscarLab.ca.all.Hl7textResultsData"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.oscarehr.util.SessionConstants"%>
<%@ page import="org.oscarehr.managers.HRMManager"%>
<%@ page import="org.oscarehr.hospitalReportManager.model.HRMDocument"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
   		 
	String user_no = (String) session.getAttribute("user");
	String userfirstname = (String) session.getAttribute("userfirstname");
	String userlastname = (String) session.getAttribute("userlastname");

	// "Module" and "function" is the same thing (old dms module)
	String module = "demographic";
	String demoNo = request.getParameter("demo");
	String requestId = request.getParameter("requestId");
	String providerNo = request.getParameter("provNo");

	if (demoNo == null && requestId == null)
		response.sendRedirect("../error.jsp");

	if (demoNo == null || demoNo.equals("null")) {
		ConsultationAttachDocs docsUtil = new ConsultationAttachDocs(requestId);
		demoNo = docsUtil.getDemoNo();

	}

	String patientName = EDocUtil.getDemographicName(loggedInInfo, demoNo);
	String[] docType = {"D", "L","H"};
	
	HRMManager hrmManager = SpringUtils.getBean(HRMManager.class);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html:html locale="true">

<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
<title><bean:message key="oscarEncounter.oscarConsultationRequest.AttachDocPopup.title" /></title>
<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/share/css/extractedFromPages.css" />
<style type="text/css">
.doc {
    color:blue;
}

.lab {
    color: #CC0099;
}

.hrm {
    color: green;
}

</style>
<script type="text/javascript">
	//<!--
	function setEmpty(selectbox) {
		var emptyTxt = "<bean:message key="oscarEncounter.oscarConsultationRequest.AttachDocPopup.empty"/>";
		var emptyVal = "0";
		var op = document.createElement("option");
		try {
			selectbox.add(op);
		} catch (e) {
			selectbox.add(op, null);
		}
		selectbox.options[0].text = emptyTxt;
		selectbox.options[0].value = emptyVal;
	}

	function swap(srcName, dstName) {
		
		var srcElement = document.getElementsByName(srcName); 
		var src = srcElement[0];
		var dstElement = document.getElementsByName(dstName); 
		var dst = dstElement[0];
		var opt;

		//if nothing or dummy is being transfered do nothing
		if (src.selectedIndex == -1 || src.options[0].value == "0") {
			return;
		}

		//if dst has dummy clobber it with new options
		if (dst.options[0].value == "0") {
			dst.remove(0);
		}

		for ( var idx = src.options.length - 1; idx >= 0; --idx) {

			if (src.options[idx].selected) {
				opt = document.createElement("option");
				try { //ie method of adding option
					dst.add(opt);
					dst.options[dst.options.length - 1].text = src.options[idx].text;
					dst.options[dst.options.length - 1].value = src.options[idx].value;
					dst.options[dst.options.length - 1].className = src.options[idx].className;
					src.remove(idx);
				} catch (e) { //firefox method of adding option
					dst.add(src.options[idx], null);
					dst.options[dst.options.length - 1].selected = false;
				}

			}

		} //end for

		if (src.options.length == 0) {
			setEmpty(src);
		}

	}

	//if consultation has not been saved, load existing docs into proper select boxes
	function init() {
		var attached = document.getElementsByName("attachedDocs")[0];
		var available = document.getElementsByName("documents")[0];

		if (document.forms[0].requestId.value == "null") {
			var docs = window.opener.document.EctConsultationFormRequestForm.documents.value
					.split("|");
			var opt;

			for ( var idx = 0; idx < docs.length; ++idx) {
				for ( var i = available.options.length - 1; i >= 0; --i) {
					if (docs[idx] == available.options[i].value) {
						opt = document.createElement("option");
						try { //ie method of adding option
							attached.add(opt);
							attached.options[attached.options.length - 1].text = available.options[i].text;
							attached.options[attached.options.length - 1].value = available.options[i].value;
							attached.options[attached.options.length - 1].className = available.options[i].className;
							available.remove(i);
						} catch (e) { //firefox method of adding option
							attached.add(available.options[i], null);
						}

						break;
					}

				} //end for

			} //end for
		} //end if

		if (attached.options.length == 0) {
			setEmpty(attached);
		}

		if (available.options.length == 0) {
			setEmpty(available);
		}

	}

	function save() {
		var ret;
		var ops = document.getElementsByName("attachedDocs")[0];

		if (document.forms[0].requestId.value == "null") {
			var saved = "";

			//we don't want to initially save dummy
			if (ops.options.length == 1 && ops.options[0].value == "0") {
				ops.options.length = 0;
			}

			var list = window.opener.document.getElementById("attachedList");
			var paragraph = window.opener.document
					.getElementById("attachDefault");

			//if we are saving something we need to update list on parent form
			if (ops.options.length) {
				paragraph.innerHTML = "";
			}

			//delete what we have before adding new docs to list
			while (list.firstChild) {
				list.removeChild(list.firstChild);
			}

			for ( var idx = 0; idx < ops.options.length; ++idx) {
				saved += ops.options[idx].value;

				if (idx < ops.options.length - 1) {
					saved += "|";
				}

				listElem = window.opener.document.createElement("li");
				listElem.innerHTML = ops.options[idx].innerHTML;
				listElem.className = ops.options[idx].className;
				list.appendChild(listElem);

			}

			window.opener.document.EctConsultationFormRequestForm.documents.value = saved;

			if (list.childNodes.length == 0) {
				paragraph.innerHTML = "<bean:message key="oscarEncounter.oscarConsultationRequest.AttachDoc.Empty"/>______$tag________________________________________________________________________";
			}

			ret = false;
			window.close();
		} else {
			//but we will use dummy in updating an empty list
			for ( var idx = 0; idx < ops.options.length; ++idx) {
				ops.options[idx].selected = true;
			}
		}
	}
//-->
</script>
</head>
<body onload="init()" onunload="window.close()"
	style="font-family: Verdana, Tahoma, Arial, sans-serif; background-color: #ddddff">

	<h3 style="text-align: center">
		<bean:message
			key="oscarEncounter.oscarConsultationRequest.AttachDocPopup.header" />
		<%=patientName%></h3>
	<html:form action="/oscarConsultationRequest/attachDoc">
		<table width="100%">
			<tr>
				<th style="text-align: center"><bean:message key="oscarEncounter.oscarConsultationRequest.AttachDocPopup.available" /></th>
				<th>&nbsp;</th>
				<th style="text-align: center"><bean:message key="oscarEncounter.oscarConsultationRequest.AttachDocPopup.attached" /></th>
			</tr>
			<tr>
				<td style="width: 45%; text-align: left" valign="top"><html:hidden
						property="requestId" value="<%=requestId%>" /> <html:hidden
						property="demoNo" value="<%=demoNo%>" /> <html:hidden
						property="providerNo" value="<%=providerNo%>" /> <html:select
						style="width: 100%;" property="documents" multiple="1" size="10">
						<%
							ArrayList privatedocs = new ArrayList();
							privatedocs = EDocUtil.listDocs(loggedInInfo, demoNo, requestId, EDocUtil.UNATTACHED);
							EDoc curDoc;
							for (int idx = 0; idx < privatedocs.size(); ++idx) {
								curDoc = (EDoc) privatedocs.get(idx);
						%>
						<html:option styleClass="doc"
							value="<%=docType[0] + curDoc.getDocId()%>"><%=curDoc.getDescription()%></html:option>
						<%
							}
							CommonLabResultData labData = new CommonLabResultData();

							ArrayList labs = labData.populateLabResultsData(loggedInInfo, demoNo, requestId, CommonLabResultData.UNATTACHED);
							LabResultData resData;
							
							for (int idx = 0; idx < labs.size(); ++idx) {
								resData = (LabResultData) labs.get(idx);
								boolean displayFlag = true;
								if (resData.labType.equals(LabResultData.HL7TEXT)) {
									if (!Hl7textResultsData.getMatchingLabs(resData.segmentID).endsWith(resData.segmentID))
										displayFlag = false;
								}
								if (displayFlag) {
						%>
						<html:option styleClass="lab"
							value="<%=docType[1] + resData.labPatientId%>"><%=resData.getDiscipline() + " "
											+ resData.getDateTime()%></html:option>
						<%
								}
							}
		
							Integer iRequestId = null;
			                if(requestId != null) {
			                	try {
			                		iRequestId = Integer.parseInt(requestId);
			                	}catch(NumberFormatException e) {
			                		iRequestId = null;
			                	}
			                }
			                
							List<HRMDocument> hrmDocuments =  hrmManager.findUnattached(loggedInInfo, Integer.parseInt(demoNo), iRequestId);
							
							for(HRMDocument hrmDoc:hrmDocuments) {
								String reportStatus = hrmDoc.getReportStatus();
								String t = StringUtils.isNullOrEmpty(hrmDoc.getDescription())?hrmDoc.getReportType():hrmDoc.getDescription();
								if (reportStatus != null && reportStatus.equalsIgnoreCase("C")) {
									t = "(Cancelled) " + t;
								}
								%>
								
								<html:option styleClass="hrm"
							value="<%=docType[2] + hrmDoc.getId().toString() %>"><%=t%></html:option>
											
								<%
							}
							
						%>
					</html:select></td>
				<td style="width: 10%; text-align: center"><input type="button"
					class="btn" onclick="swap('documents','attachedDocs');" value=">>" /><br />
					<input type="button" class="btn"
					onclick="swap('attachedDocs','documents');" value="<<" /></td>
				<td style="width: 45%; text-align: right"><html:select
						style="width: 100%;" property="attachedDocs" multiple="1"
						size="10">
						<%
							ArrayList privatedocs = new ArrayList();
							privatedocs = EDocUtil.listDocs(loggedInInfo, demoNo, requestId, EDocUtil.ATTACHED);
							EDoc curDoc;
							for (int idx = 0; idx < privatedocs.size(); ++idx) {
								curDoc = (EDoc) privatedocs.get(idx);
						%>
						<html:option styleClass="doc"
							value="<%=docType[0] + curDoc.getDocId()%>"><%=curDoc.getDescription()%></html:option>
						<%
							}

							CommonLabResultData labData = new CommonLabResultData();
							ArrayList labs = labData.populateLabResultsData(loggedInInfo, demoNo, requestId, CommonLabResultData.ATTACHED);
							LabResultData resData;
							for (int idx = 0; idx < labs.size(); ++idx) {
								resData = (LabResultData) labs.get(idx);
						%>
						<html:option styleClass="lab"
							value="<%=docType[1] + resData.labPatientId%>"><%=resData.getDiscipline() + " "
										+ resData.getDateTime()%></html:option>
						<%
							}
							
							Integer iRequestId = null;
			                if(requestId != null) {
			                	try {
			                		iRequestId = Integer.parseInt(requestId);
			                	}catch(NumberFormatException e) {
			                		iRequestId = null;
			                	}
			                }
			                
							List<HRMDocument> hrmDocuments = hrmManager.findAttached(loggedInInfo, Integer.parseInt(demoNo), iRequestId);
							for(HRMDocument hrmDoc: hrmDocuments) {
			                	String reportStatus = hrmDoc.getReportStatus();
								String t = StringUtils.isNullOrEmpty(hrmDoc.getDescription())?hrmDoc.getReportType():hrmDoc.getDescription();
								if (reportStatus != null && reportStatus.equalsIgnoreCase("C")) {
									t = "(Cancelled) " + t;
								}
			     %>
			   <html:option styleClass="hrm"
							value="<%=docType[2] + hrmDoc.getId().toString() %>"><%=t%></html:option>
			     <%
			                }
						%>
					</html:select></td>
			</tr>
		</table>
		<table width="100%">
			<tr>
				<td style="text-align: center"><input type="submit" class="btn"
					name="submit"
					value="<bean:message key="oscarEncounter.oscarConsultationRequest.AttachDocPopup.submit"/>"
					onclick="return save();" /></td>
			</tr>
			<tr>
				<td style="text-align: center"><span class="legend"><bean:message
							key="oscarEncounter.oscarConsultationRequest.AttachDoc.Legend" /></span><br />
				<span class="doc legend"><bean:message
							key="oscarEncounter.oscarConsultationRequest.AttachDoc.LegendDocs" /></span><br />
				<span class="lab legend"><bean:message
							key="oscarEncounter.oscarConsultationRequest.AttachDoc.LegendLabs" /></span><br/>
				<span class="hrm legend"><bean:message
							key="oscarEncounter.oscarConsultationRequest.AttachDoc.LegendHrm" /></span>
							</td>
			</tr>
		</table>
	</html:form>
</body>
</html:html>
