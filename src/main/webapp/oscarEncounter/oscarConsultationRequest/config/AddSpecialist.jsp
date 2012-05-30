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

<%@ page import="java.util.ResourceBundle"%>
<% java.util.Properties oscarVariables = oscar.OscarProperties.getInstance(); %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConAddSpecialistForm"%><html:html locale="true">

<%
  ResourceBundle oscarR = ResourceBundle.getBundle("oscarResources",request.getLocale());

  String transactionType = new String(oscarR.getString("oscarEncounter.oscarConsultationRequest.config.AddSpecialist.addOperation"));
  int whichType = 1;
  if ( request.getAttribute("upd") != null){
      transactionType = new String(oscarR.getString("oscarEncounter.oscarConsultationRequest.config.AddSpecialist.updateOperation"));
      whichType=2;
  }
%>

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><%=transactionType%></title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>
<script language="javascript">
function BackToOscar() {
       window.close();
}
</script>

<link rel="stylesheet" type="text/css" href="../../encounterStyles.css">
<body class="BodyStyle" vlink="#0000FF">

<html:errors />
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">Consultation</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td class="Header"><%=transactionType%></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr style="vertical-align: top">
		<td class="MainTableLeftColumn">
		<%oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConTitlebar titlebar = new oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConTitlebar(request);
                  out.print(titlebar.estBar(request));
                  %>
		</td>
		<td class="MainTableRightColumn">
		<table cellpadding="0" cellspacing="2"
			style="border-collapse: collapse" bordercolor="#111111" width="100%"
			height="100%">

			<!----Start new rows here-->
			<%
                   String added = (String) request.getAttribute("Added");
                   if (added != null){  %>
			<tr>
				<td><font color="red"> <bean:message
					key="oscarEncounter.oscarConsultationRequest.config.AddSpecialist.msgSpecialistAdded"
					arg0="<%=added%>" /> </font></td>
			</tr>
			<%}%>
			<tr>
				<td>

				<html:form action="/oscarEncounter/AddSpecialist">
					<table>
						<%
                           if (request.getAttribute("specId") != null ){
                           EctConAddSpecialistForm thisForm;
                           thisForm = (EctConAddSpecialistForm) request.getAttribute("EctConAddSpecialistForm");
                           thisForm.setFirstName( (String) request.getAttribute("fName"));
                           thisForm.setLastName( (String) request.getAttribute("lName"));
                           thisForm.setProLetters( (String) request.getAttribute("proLetters"));
                           thisForm.setAddress( (String) request.getAttribute("address"));
                           thisForm.setPhone( (String) request.getAttribute("phone"));
                           thisForm.setFax( (String) request.getAttribute("fax"));
                           thisForm.setWebsite( (String) request.getAttribute("website"));
                           thisForm.setEmail( (String) request.getAttribute("email"));
                           thisForm.setSpecType( (String) request.getAttribute("specType"));
                           thisForm.setSpecId( (String) request.getAttribute("specId"));
                           thisForm.seteDataUrl( (String) request.getAttribute("eDataUrl"));
                           thisForm.seteDataOscarKey( (String) request.getAttribute("eDataOscarKey"));
                           thisForm.seteDataServiceKey( (String) request.getAttribute("eDataServiceKey"));
                           thisForm.seteDataServiceName( (String) request.getAttribute("eDataServiceName"));
                           thisForm.setAnnotation((String)request.getAttribute("annotation"));
                           thisForm.setReferralNo((String)request.getAttribute("referralNo"));
                           }
                        %>
						<html:hidden name="EctConAddSpecialistForm" property="specId" />
						<tr>
							<td><bean:message key="oscarEncounter.oscarConsultationRequest.config.AddSpecialist.firstName" /></td>
							<td><html:text name="EctConAddSpecialistForm" property="firstName" /></td>
							<td><bean:message key="oscarEncounter.oscarConsultationRequest.config.AddSpecialist.lastName" /></td>
							<td><html:text name="EctConAddSpecialistForm" property="lastName" /></td>
							<td><bean:message key="oscarEncounter.oscarConsultationRequest.config.AddSpecialist.professionalLetters" />
							</td>
							<td><html:text name="EctConAddSpecialistForm" property="proLetters" /></td>
						</tr>
						<tr>
							<td><bean:message key="oscarEncounter.oscarConsultationRequest.config.AddSpecialist.address" />
							</td>
							<td><html:textarea name="EctConAddSpecialistForm" property="address" cols="30" rows="3" /> <%=oscarVariables.getProperty("consultation_comments","") %>
							</td>
                                                        <td><bean:message key="oscarEncounter.oscarConsultationRequest.config.EditSpecialists.Annotation" />
							</td>
							<td colspan="4"><html:textarea name="EctConAddSpecialistForm" property="annotation" cols="30" rows="3" />
							</td>
						</tr>
						<tr>
							<td><bean:message key="oscarEncounter.oscarConsultationRequest.config.AddSpecialist.phone" />
							</td>
							<td><html:text name="EctConAddSpecialistForm" property="phone" /></td>
							<td><bean:message key="oscarEncounter.oscarConsultationRequest.config.AddSpecialist.fax" />
							</td>
							<td colspan="4"><html:text name="EctConAddSpecialistForm" property="fax" /></td>
						</tr>
						<tr>
							<td><bean:message key="oscarEncounter.oscarConsultationRequest.config.AddSpecialist.website" /></td>
							<td><html:text name="EctConAddSpecialistForm" property="website" /></td>
							<td><bean:message key="oscarEncounter.oscarConsultationRequest.config.AddSpecialist.email" /></td>
							<td colspan="4"><html:text name="EctConAddSpecialistForm" property="email" /></td>
						</tr>
						<tr>
							<td><bean:message key="oscarEncounter.oscarConsultationRequest.config.AddSpecialist.specialistType" /></td>
							<td><html:text name="EctConAddSpecialistForm" property="specType" /></td>
							<td><bean:message key="oscarEncounter.oscarConsultationRequest.config.AddSpecialist.referralNo" /></td>
							<td colspan="4">
								<% if (request.getAttribute("refnoinuse") != null) { %>
									<span style="color: red;"><bean:message
										key="oscarEncounter.oscarConsultationRequest.config.AddSpecialist.referralNoInUse" /></span><br />
								<% } else if (request.getAttribute("refnoinvalid") != null) { %>
									<span style="color: red;"><bean:message
										key="oscarEncounter.oscarConsultationRequest.config.AddSpecialist.referralNoInvalid" /></span><br />
								<% } %>
								<html:text name="EctConAddSpecialistForm" property="referralNo" maxlength="6" />
							</td>
						</tr>
						<tr>
							<td colspan="7"><hr /></td>
						</tr>
						<tr>
							<td><bean:message key="oscarEncounter.oscarConsultationRequest.config.AddSpecialist.eDataUrl" /></td>
							<td colspan="5"><html:text style="width:100%" name="EctConAddSpecialistForm" property="eDataUrl" /></td>
						</tr>
						<tr>
							<td><bean:message key="oscarEncounter.oscarConsultationRequest.config.AddSpecialist.eDataOscarKey" /></td>
							<td colspan="5"><html:text style="width:100%" name="EctConAddSpecialistForm" property="eDataOscarKey" /></td>
						</tr>
						<tr>
							<td><bean:message key="oscarEncounter.oscarConsultationRequest.config.AddSpecialist.eDataServiceKey" /></td>
							<td colspan="5"><html:text style="width:100%" name="EctConAddSpecialistForm" property="eDataServiceKey" /></td>
						</tr>
						<tr>
							<td><bean:message key="oscarEncounter.oscarConsultationRequest.config.AddSpecialist.eDataServiceName" /></td>
							<td colspan="5"><html:text style="width:100%" name="EctConAddSpecialistForm" property="eDataServiceName" /></td>
						</tr>
						<tr>
							<td colspan="6">
								<input type="hidden" name="whichType" value="<%=whichType%>" />
								<input type="submit" name="transType" value="<%=transactionType%>" />
							</td>
						</tr>
					</table>
				</html:form>
				</td>
			</tr>
			<!----End new rows here-->

			<tr height="100%">
				<td></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
