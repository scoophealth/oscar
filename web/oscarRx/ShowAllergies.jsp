<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_rx" rights="r"
	reverse="<%=true%>">
	<%response.sendRedirect("../noRights.html");%>
</security:oscarSec>

<% response.setHeader("Cache-Control","no-cache");%>
<logic:notPresent name="RxSessionBean" scope="session">
	<logic:redirect href="error.html" />
</logic:notPresent>
<logic:present name="RxSessionBean" scope="session">
	<bean:define id="bean" type="oscar.oscarRx.pageUtil.RxSessionBean"
		name="RxSessionBean" scope="session" />
	<logic:equal name="bean" property="valid" value="false">
		<logic:redirect href="error.html" />
	</logic:equal>
</logic:present>
<%
oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean)pageContext.findAttribute("bean");
String annotation_display = org.oscarehr.casemgmt.model.CaseManagementNoteLink.DISP_ALLERGY;
%>

<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->



<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="EditAllergies.title" /></title>
<link rel="stylesheet" type="text/css" href="styles.css">


<script type="text/javascript">



    function isEmpty(){  
        if (document.RxSearchAllergyForm.searchString.value.length == 0){
            alert("Search Field is Empty");
            document.RxSearchAllergyForm.searchString.focus();
            return false;
        }
        return true;
    }
    
    function addCustomAllergy(){
        var name = document.getElementById('searchString').value;
        if(isEmpty() == true){
            name = name.toUpperCase();
            window.location="addReaction.do?ID=0&type=0&name="+name;
        }
    }
</script>
</head>
<bean:define id="patient"
	type="oscar.oscarRx.data.RxPatientData.Patient" name="Patient" />

<body topmargin="0" leftmargin="0" vlink="#0000FF">
<table border="0" cellpadding="0" cellspacing="0"
	style="border-collapse: collapse" bordercolor="#111111" width="100%"
	id="AutoNumber1" height="100%">
	<%@ include file="TopLinks.jsp"%><!-- Row One included here-->
	<tr>
		<%@ include file="SideLinksEditFavorites.jsp"%><!-- <td></td>Side Bar File --->
		<td width="100%" style="border-left: 2px solid #A9A9A9;" height="100%"
			valign="top"><!--Column Two Row Two-->
		<table cellpadding="0" cellspacing="2"
			style="border-collapse: collapse" bordercolor="#111111" width="100%"
			height="100%">
			<tr>
				<td width="0%" valign="top">
				<div class="DivCCBreadCrumbs"><a href="SearchDrug.jsp"> <bean:message
					key="SearchDrug.title" /></a>&nbsp;&gt;&nbsp; <b><bean:message
					key="EditAllergies.title" /></b></div>
				</td>
			</tr>
			<!----Start new rows here-->
			<tr>
				<td>
				<div class="DivContentTitle"><bean:message
					key="EditAllergies.title" /></div>
				</td>
			</tr>
			<tr>
				<td>
				<div class="DivContentSectionHead"><bean:message
					key="EditAllergies.section1Title" /></div>
				</td>
			</tr>
			<tr>
				<td>
				<table>
					<tr>
						<td><b>Name:</b> <jsp:getProperty name="patient"
							property="surname" /></td>
						<td>&nbsp;</td>
						<td><b>Age:</b> <jsp:getProperty name="patient"
							property="age" /></td>
					</tr>
				</table>
				</td>
			</tr>

			<tr>
				<td>
				<div class="DivContentSectionHead"><bean:message
					key="EditAllergies.section2Title" /></div>
				</td>
			</tr>
			<tr>
				<td>
				<table border=0>
					<tr>
						<td width="100%">
						<div class="Step1Text" style="width: 800px;">
						<table width="100%" cellpadding="3">
							<thead>
								<td>&nbsp;</td>
								<td><b>Entry Date</b></td>
								<td><b>Description</b></td>
								<td><b>Allergy Type</b></td>
								<td><b>Severity</b></td>
								<td><b>Onset of Reaction</b></td>
								<td><b>Reaction</b></td>
								<td><b>Start Date</b></td>
								<td><b>&nbsp;</b></td>
							</thead>
							<logic:iterate id="allergy"
								type="oscar.oscarRx.data.RxPatientData.Patient.Allergy"
								name="patient" property="allergies">
								<tr>
									<td><a
										href="deleteAllergy.do?ID=<%= String.valueOf(allergy.getAllergyId()) %>">
									Delete </a></td>
									<td><bean:write name="allergy" property="entryDate" /></td>
									<td><bean:write name="allergy"
										property="allergy.DESCRIPTION" /></td>
									<td><bean:write name="allergy" property="allergy.typeDesc" />
									</td>
									<td><bean:write name="allergy"
										property="allergy.severityOfReactionDesc" /></td>
									<td><bean:write name="allergy"
										property="allergy.onSetOfReactionDesc" /></td>
									<td><bean:write name="allergy" property="allergy.reaction" />
									</td>
									<td><%=allergy.getAllergy().getStartDate()!=null?allergy.getAllergy().getStartDate():""%>
									</td>
									<td><a href="#" title="Annotation" onclick="window.open('../annotation/annotation.jsp?display=<%=annotation_display%>&table_id=<%=String.valueOf(allergy.getAllergyId())%>&demo=<jsp:getProperty name="patient" property="demographicNo"/>','anwin','width=400,height=250');"><img src="../images/notes.gif" border="0"></a>
									</td>
								</tr>

							</logic:iterate>
						</table>
						</div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr> 
				<td>
				<div class="DivContentSectionHead"><bean:message
					key="EditAllergies.section3Title" /></div>
				</td>
			</tr>
			<tr>
				<td><html:form action="/oscarRx/searchAllergy"
					focus="searchString" onsubmit="return isEmpty()">
					<table>
						<tr valign="center">
							<td>Search:</td>
                                                        <td><html:text property="searchString" size="16" styleId="searchString" maxlength="16" /></td>
						</tr>
						<tr>
							<td><html:submit property="submit" value="Search"
								styleClass="ControlPushButton" />
                                                        </td>
							<td><input type=button class="ControlPushButton"
								onclick="javascript:document.forms.RxSearchAllergyForm.searchString.value='';document.forms.RxSearchAllergyForm.searchString.focus();"
								value="Reset" />
                                                             <input type=button class="ControlPushButton" onclick="javascript:addCustomAllergy();" value="Custom Allergy" />
                                                            
                                                             
                                                        </td>
						</tr>
					</table>
                      &nbsp;
                      <table bgcolor="#F5F5F5" cellpadding=3>
						<tr>
							<td colspan=4>Search the following categories: <i>(Listed
							general to specific)</i></td>
						</tr>

						<tr>
							<td><html:checkbox property="type4" /> Drug Classes</td>
							<td><html:checkbox property="type3" /> Ingredients</td>
							<td><html:checkbox property="type2" /> Generic Names</td>
							<td><html:checkbox property="type1" /> Brand Names</td>
						</tr>
						<tr>
							<td colspan=4><script language=javascript>
                                    function typeSelect(){
                                        var frm = document.forms.RxSearchAllergyForm;

                                        frm.type1.checked = true;
                                        frm.type2.checked = true;
                                        frm.type3.checked = true;
                                        frm.type4.checked = true;
                                        //frm.type5.checked = true;
                                    }

                                    function typeClear(){
                                        var frm = document.forms.RxSearchAllergyForm;

                                        frm.type1.checked = false;
                                        frm.type2.checked = false;
                                        frm.type3.checked = false;
                                        frm.type4.checked = false;
                                        frm.type5.checked = false;
                                    }

                                    typeSelect();
                                </script> <input type=button
								class="ControlPushButton" onclick="javascript:typeSelect();"
								value="Select All" /> <input type=button
								class="ControlPushButton" onclick="javascript:typeClear();"
								value="Clear All" /></td>
						</tr>
					</table>
				</html:form> <br>
				<br>
				<%
                        String sBack="SearchDrug.jsp";
                      %> <input type=button class="ControlPushButton"
					onclick="javascript:window.location.href='<%=sBack%>';"
					value="Back to Search Drug" /></td>
			</tr>
			<!----End new rows here-->
			<tr height="100%">
				<td></td>
			</tr>
		</table>
		</td>
	</tr>

	<tr>
		<td height="0%"
			style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
		<td height="0%"
			style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
	</tr>

	<tr>
		<td width="100%" height="0%" colspan="2">&nbsp;</td>
	</tr>

	<tr>
		<td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC"
			colspan="2"></td>
	</tr>

</table>
</body>
</html:html>









