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
<%@ page language="java" contentType="text/html" %>  
  
<%-- 
	Author: Dennis Warren 
	Company: Colcamex Resources
	Date: November 2014 
	Comment: written for the Pharmacy Clinic of UBC Pharmaceutical Science's
--%>  
   
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>
  
<html:html>
		
<head>
	<title><bean:message key="colcamex.formBPMH.title" /></title>
	<link rel="stylesheet" type="text/css" media="screen" href="${ pageContext.request.contextPath }/form/pharmaForms/index.css" />
 	<link rel="stylesheet" type="text/css" media="screen" href="${ pageContext.request.contextPath }/css/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/css/healthCareTeam.css" />
	<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jquery.js" ></script>
 	<script type="text/javascript" >
		
 		function popUpData(data) {
			if(data) {
				location.reload();
			}
		}

 		jQuery(document).ready( function($) { 			
			$("#editFamilyDr").click(function(){
				var familyDrContactId = $("#familyDrContactId").val();
				var demographicNo = '${ bpmh.demographic.demographicNo }';
				var source = "${ pageContext.request.contextPath }/demographic/manageHealthCareTeam.jsp?" +
						"demographicNo=" + demographicNo + 
						"&view=detached" + 
						"&element=providerId";
				var windowspecs = "width=700,height=400,left=-1000,top=-1000, scrollbars=yes, resizable=yes";
				window.open(source, "manageHealthCareTeam", windowspecs);
			})
 		})
	</script>

</head>

<body id="formBPMH">

	<!--  HEADING  -->
	<!-- FORM BEGIN -->
	<html:form action="/formBPMH.do" >
		<html:hidden property="demographicNo" />
		<html:hidden property="formId" />
		<header>	
				
			<h1><bean:message key="colcamex.formBPMH.title" /></h1>
			
			<c:set var="controls" value="on" scope="page" />
			
			<div id="bpmhId">	
								
					<logic:empty name="bpmh" property="provider">
	
				<span class="red" >	
						<bean:message key="colcamex.formBPMH.preparedby" />
						<bean:message key="colcamex.formBPMH.error.unknown" />
					</logic:empty>
					
					<logic:notEmpty name="bpmh" property="provider">
		
				<span>	
						<bean:message key="colcamex.formBPMH.preparedby" />
						<bean:write name="bpmh" property="provider.formattedName" />
						<logic:notEmpty name="bpmh" property="provider.ohipNo">	
							&#40;<bean:write name="bpmh" property="provider.ohipNo" />&#41;
						</logic:notEmpty>
						<logic:empty name="bpmh" property="provider.ohipNo">
							&#40;<bean:message key="colcamex.formBPMH.error.unknown" />&#41;
						</logic:empty>
								
					</logic:notEmpty>					
				</span>
					
				
				<logic:empty name="bpmh" property="formDateFormatted">
					<span class="red" >
				</logic:empty>
				<logic:notEmpty name="bpmh" property="formDateFormatted">
					<span>
				</logic:notEmpty>
					<bean:message key="colcamex.formBPMH.preparedon" />
					<bean:write name="bpmh" property="formDateFormatted" />	
				</span>
									
				<%-- <span>				
					<bean:message key="colcamex.formBPMH.printedon" />
					<bean:write name="bpmh" property="editDateFormatted" />	
				</span>	 --%>
				
			</div>		
		</header>
		
		<!--  SUB HEADING  -->
		<section id="subHeader">
		
			<!--  PATIENT  -->
			<table id="patientId" >
				<tr><th colspan="6"><bean:message key="colcamex.formBPMH.patient" /></th></tr>			
				<tr>	
					<td rowspan="2" class="columnTitle" ><bean:message key="colcamex.formBPMH.patient.name" /></td>
						<td rowspan="2">							
							<bean:write name="bpmh" property="demographic.fullName" />
						</td>	
					<td class="columnTitle"><bean:message key="colcamex.formBPMH.patient.insurance" /></td>
						<td>
							<bean:write name="bpmh" property="demographic.hin" />
						</td>
					<td class="columnTitle"><bean:message key="colcamex.formBPMH.patient.gender" /></td>
						<td>
							<bean:write name="bpmh" property="demographic.sex" />
						</td>
				</tr>
				
				<tr> 													
					<td class="columnTitle"><bean:message key="colcamex.formBPMH.patient.dob" /></td>
						<td>
							<bean:write name="bpmh" property="demographic.formattedDob" />
						</td>
					<td class="columnTitle"><bean:message key="colcamex.formBPMH.patient.phone" /></td>
						<td>
							<bean:write name="bpmh" property="demographic.phone" />
						</td>
				</tr>
			</table>
			<table id="allergies">
				<tr>
					<td class="columnTitle">
						<bean:message key="colcamex.formBPMH.patient.allergies" />
					</td>
					<td>	
						<logic:notEmpty name="bpmh" property="allergiesString">				
							<bean:write name="bpmh" property="allergiesString" />
						</logic:notEmpty>
					</td>
				</tr>		
			</table>
		</section>

		<!--  FAMILY PHYSICIAN  -->	
		<section id="familyPhysician" >	
			<html:hidden name="bpmh" property="familyDrContactId" styleId="familyDrContactId" />	
			<table id="providerId">
				<tr><th colspan="7"><bean:message key="colcamex.formBPMH.familyDr" /></th></tr>			
				<tr>
					<td class="columnTitle"><bean:message key="colcamex.formBPMH.familyDr.name" /></td>
					<td>
						<logic:empty name="bpmh" property="familyDrName" > 
							<span class="red" >	Unknown	</span>
						</logic:empty>
						<logic:notEmpty name="bpmh" property="familyDrName" > 	
							<bean:write name="bpmh" property="familyDrName" />
						</logic:notEmpty>
					</td>
					
					<td class="columnTitle"><bean:message key="colcamex.formBPMH.familyDr.phone" /></td>
					<td>
						<logic:empty name="bpmh" property="familyDrPhone" > 
							<span class="red" >	Unknown	</span>
						</logic:empty>
						<logic:notEmpty name="bpmh" property="familyDrPhone" >
							<bean:write name="bpmh" property="familyDrPhone" />
						</logic:notEmpty>
					</td>
					
					<td class="columnTitle"><bean:message key="colcamex.formBPMH.familyDr.fax" /></td>
					<td>
						<logic:empty name="bpmh" property="familyDrFax" > 
							<span class="red" >	Unknown	</span>
						</logic:empty>
						<logic:notEmpty name="bpmh" property="familyDrFax" >
							<bean:write name="bpmh" property="familyDrFax" />
						</logic:notEmpty>
					</td>
					<logic:equal name="bpmh" property="formId" value="0">
						<logic:notEmpty name="bpmh" property="familyDrContactId" >
							<td class="columnTitle" style="text-align:center;background-color:#CCC;border-top:#ccc thin solid;" >
								<input type="button" id="editFamilyDr" value="edit" />
							</td>
						</logic:notEmpty>
					</logic:equal>
					
				</tr>	
					
			</table>		
		</section>	
		
		<!-- DRUG DATA -->
		<section id="drugData">		
			<table id="drugtable">
			
				<tr><th colspan="4"><bean:message key="colcamex.formBPMH.drugs" /></th></tr>
				<tr id="drugtableSubheading">
					
					<td class="columnTitle" >
						<bean:message key="colcamex.formBPMH.drugs.what" /><br />
						<span class="smallText">
							<bean:message key="colcamex.formBPMH.drugs.what.sub" />
						</span>
					</td>
					<td class="columnTitle">
						<bean:message key="colcamex.formBPMH.drugs.how" /><br />
						<span class="smallText">
							<bean:message key="colcamex.formBPMH.drugs.how.sub" />
						</span>
					</td>
					<td class="columnTitle">
						<bean:message key="colcamex.formBPMH.drugs.why" /><br />
						<span class="smallText">
							<bean:message key="colcamex.formBPMH.drugs.why.sub" />
						</span>
					</td>
					<td class="columnTitle">
						<bean:message key="colcamex.formBPMH.drugs.instructions" /><br />
						<span class="smallText">
							<bean:message key="colcamex.formBPMH.drugs.instructions.sub" />
						</span>
					</td>
				</tr>
				<c:set value="missingDrugData" var="false" />
				<logic:notEmpty name="bpmh" property="drugs">
					<logic:iterate name="bpmh" property="drugs" id="drugs" indexId="status" >		
						<tr>
							<html:hidden name="drugs" property="id" indexed="true" />
	
							<td>
							<!-- WHAT -->
								<bean:write name="drugs" property="what" />
							</td>
							<logic:empty name="drugs" property="how">
								<c:set value="${ true }" var="missingDrugData" />
								<td style="border:red medium solid;" >
									<!-- HOW ERROR -->
									&nbsp;
								</td>
							</logic:empty>
							<logic:notEmpty  name="drugs" property="how" >
								<td>
								<!-- HOW -->
									<bean:write name="drugs" property="how" />
								</td>
							</logic:notEmpty>
							<logic:empty name="drugs" property="why">
								<c:set value="${ true }" var="missingDrugData" />
								<td style="border:red medium solid;" >
									<!-- WHY ERROR -->
									&nbsp;
								</td>
							</logic:empty>
							<logic:notEmpty  name="drugs" property="why">
								<td>
								<!-- WHY -->
									<bean:write name="drugs" property="why" />
								</td>
							</logic:notEmpty>
							<td>
							<!-- INSTRUCTION -->
								<logic:notEmpty name="drugs" property="instruction" >
									<bean:write name="drugs" property="instruction" />										
								</logic:notEmpty>
								
								<logic:empty name="drugs" property="instruction">								
									<html:textarea name="drugs" indexed="true" property="instruction" >&nbsp;</html:textarea>									
								</logic:empty>							
							</td>
						</tr>				
					</logic:iterate>
				</logic:notEmpty>

			</table>	
		</section>
		<section id="declaration">
			<table>
				<tr><td>
					<html:checkbox name="bpmh" property="confirm" value="checked"/>
					<label for="confirm"><bean:message key="colcamex.formBPMH.confirm" /></label>
				</td></tr>
			</table>
		</section>
		<section id="note">
			<table>
				<tr>
					<td>
					<bean:message key="colcamex.formBPMH.notes" />&nbsp; 
					<span class="smallText" >Lines > 10 or Characters > 1250 will carry over to a new page.</span>
					</td>
				</tr>
				<tr>
				<td>
					<logic:notEmpty name="bpmh" property="note" >
						<bean:write name="bpmh" property="note" />&nbsp;
					</logic:notEmpty>
					<logic:empty name="bpmh" property="note">
						<html:textarea property="note" > &nbsp;</html:textarea>
					</logic:empty>
				</td>
				</tr>
			</table>
		</section>
		
		<section id="controls" >
			<table>
			<tr>
				<td>
					<c:if test="${ missingDrugData }">
						<c:set var="controls" value="off" scope="page" />
						<span style="color:red;">Missing Medication Data</span>
					</c:if>
					<logic:empty name="bpmh" property="allergiesString">
								<c:set var="controls" value="off" scope="page" />
								<span style="color:red;">Allergy Notation is Required (ie: NKDA)</span>
							</logic:empty>
					<logic:empty name="bpmh" property="provider">
						<c:set var="controls" value="off" scope="page" />
						<span class="red" >
							<bean:message key="colcamex.formBPMH.error.missing.provider" />
						</span>
					</logic:empty>
					
					<c:if test="${ controls eq 'on' }" >
					
						<html:submit property="method" >
							<bean:message key="colcamex.formBPMH.print" />
						</html:submit>
						
						<html:submit property="method" >
							eDocument
						</html:submit>
		
						<logic:equal name="bpmh" property="formId" value="0">
							<html:submit property="method" >
								<bean:message key="colcamex.formBPMH.save" />
							</html:submit>
						</logic:equal>
					
					</c:if>

					<logic:messagesPresent message="true">
					    <html:messages id="saved" message="true">
					        <logic:present name="saved">
					            <div class="messages">
					                <bean:write name="saved" filter="false" />
					            </div>
					        </logic:present>
					    </html:messages>
					</logic:messagesPresent>
				</td>
			</tr>
			</table>
		</section>
	</html:form>
		
	<!-- FOOTER -->	
	<footer>
		<span><bean:message key="colcamex.formBPMH.formowner" /></span>
	</footer>
		
</body>
</html:html>