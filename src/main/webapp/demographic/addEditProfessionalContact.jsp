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

<%@page import="java.util.ArrayList"%>
<%@page import="org.oscarehr.common.model.ProfessionalContact"%>
<%@page import="org.oscarehr.common.dao.ProgramContactTypeDao"%>
<%@page import="org.oscarehr.common.model.ProgramContactType"%>
<%@page import="oscar.OscarProperties"%>
<%@ include file="/taglibs.jsp"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.List, org.oscarehr.util.SpringUtils" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.oscarehr.common.dao.ContactSpecialtyDao" %>
<%@ page import="org.oscarehr.common.model.ContactSpecialty" %>
<%@page import="org.oscarehr.PMmodule.model.ProgramProvider"%>
<%@page import="org.oscarehr.managers.ProgramManager2"%>
<%@page import="org.oscarehr.managers.ContactManager"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>


<%
ProgramContactTypeDao pcTypeDao = SpringUtils.getBean(ProgramContactTypeDao.class);
ProgramManager2 programManager2 = SpringUtils.getBean(ProgramManager2.class);
LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
List<ProgramProvider> ppList = programManager2.getProgramDomain(loggedInInfo, loggedInInfo.getLoggedInProviderNo());
List<ProgramProvider> ppList2 = new ArrayList<ProgramProvider>();

//only programs with contact types set
for(ProgramProvider p: ppList) {
	 List<ProgramContactType> tmp = pcTypeDao.findByProgram(p.getProgramId().intValue());
	 if(!tmp.isEmpty()) {
		 ppList2.add(p);
	 }
}
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.12.3.js"></script>
<title>Add/Edit Professional Contact</title>
<script type="text/javascript">

<% if("saveProContact".equals(request.getParameter("method"))) { %>
	$(document).ready(function(){
		window.close();
	});
<% } %>
//<!--
		function setfocus() {
			this.window.focus();
			captureParameters(this);
    		forwardToParent();		  			  	
			// document.forms[0].referral_no.focus();
			// document.forms[0].referral_no.select();		
		}
		
		function captureParameters(id) {
			
			var keyword = '${ param.keyword }';
			var keywordLastName = null;
			var keywordFirstName = null;
			var firstName = '${ pcontact.firstName }';
			var lastName = '${ pcontact.lastName }';
			
			if( keyword && keyword.contains(",") ) {
				keywordLastName = keyword.split(",")[0].trim();
				keywordFirstName = keyword.split(",")[1].trim();
			} else if( keyword ) {
				keywordLastName = keyword;
			}

			if( ! lastName ) {
				document.getElementById("pcontact.lastName").value = keywordLastName;
			}
			if( ! firstName ) {
				document.getElementById("pcontact.firstName").value = keywordFirstName;
			}
		}

		function forwardToParent() {
			
			var contactId = '${ requestScope.contactId }'; // server returns the id that was saved.
			var demographicContactId = '${ requestScope.demographicContactId }';
			var contactRole = '${ requestScope.contactRole }';
			var contactName = '${ requestScope.contactName }';
			var contactType = '${ requestScope.contactType }';
			
			if( contactId ) {
				
				var data = new Object();
				data.contactId = contactId;
				data.contactName = contactName;
				data.contactRole = contactRole;
				data.demographicContactId = demographicContactId;
				data.method = "saveManage";
				data.contactType = contactType;

				try {
					if( opener.popUpData( JSON.stringify(data) ) ) {
						this.window.close()
					}
				} catch(error) {
					// do nothing
				}
					
			} 
		}
		
	    function onSearch() {
	        //document.forms[0].submit.value="Search";
	        var ret = checkreferral_no();
	        return ret;
	    }
	    
	    function onSave() {
	    	
	    	if( checkAllFields() ) {
	    		document.contactForm.submit();
	    	}
  	
	        //document.forms[0].submit.value="Save";
	        /*
	        var ret = true;
	        if(ret==true) {
				ret = checkAllFields();
			}
	        if(ret==true) {
	            ret = confirm("Are you sure you want to save?");
	        }
	        */
	        // return true;
	    }
		
		function checkAllFields() {
			
			var verified = true;
			var fields = document.forms[0].elements;			
			var fieldname;
			var fieldvalue;
			var fieldobject;
			
			for( var i = 0; i < fields.length; i++ ) {
				
				fieldobject = fields[i];
				fieldname = fieldobject.id;
				fieldvalue = fieldobject.value.trim();
				
				
				if( fieldname == "pcontact.lastName" && fieldvalue.length == 0 ) {
					verified = false;
					paintErrorField(fieldobject);	
				}
				
				if( fieldname == "pcontact.firstName" && fieldvalue.length == 0 ) {
					verified = false;
					paintErrorField(fieldobject);
				} 

				<%if("true".equals(OscarProperties.getInstance().getProperty("professionalContact.required.workPhone","true"))) {%>
				if( fieldname == "pcontact.workPhone" && fieldvalue.length == 0 ) {
					verified = false;
					paintErrorField(fieldobject);
				}
				<% } %>
				
				<%if("true".equals(OscarProperties.getInstance().getProperty("professionalContact.required.program","false"))) {%>
				if( fieldname == "pcontact.programNo") {
					
					if(fieldobject.options[fieldobject.selectedIndex].value == '0') {
						verified = false;
						paintErrorField(fieldobject);
					}
				}
				<% } %>
			}
			
			return verified;
	    }
		
		function paintErrorField( fieldobject ) {
			fieldobject.style.border = "medium solid red";
		}
		
	    function isNumber(s){
	        var i;
	        for (i = 0; i < s.length; i++){
	            // Check that current character is number.
	            var c = s.charAt(i);
	            if (c == ".") continue;
	            if (((c < "0") || (c > "9"))) return false;
	        }
	        // All characters are numbers.
	        return true;
	    }

//-->

</script>
</head>
<body bgcolor="ivory" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr>
		<td align="left">&nbsp;</td>
	</tr>
</table>

<table BORDER="1" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr BGCOLOR="#CCFFFF">
		<th style="text-align:center;"> 
			<c:out value="${ pcontact.id gt 0 ? 'Edit' : 'Add' }" />
			Professional Contact
		</th>
	</tr>
</table>

<html:form action="/demographic/Contact" styleId="addEditProfessionalForm">

	<c:if test="${ pcontact.id gt 0 }" >
		<input type="hidden" name="pcontact.id" value="${ pcontact.id }" />
	</c:if>
	
	<input type="hidden" name="method" value="saveProContact"/>
	<input type="hidden" name="demographicContactId" value="${ demographicContactId }"/>
	<input type="hidden" name="keywordFirstName" id="keywordFirstName" value=""/>
	<input type="hidden" name="keywordLastName" id="keywordLastName" value="" />
	
<table width="100%" border="0" cellspacing="2" cellpadding="2">
	<tr>
		<td>&nbsp;</td>
	</tr>	
	<tr>
		<td align="right"><b>Last Name</b></td>
		<td>
			<input type="text" name="pcontact.lastName" id="pcontact.lastName" 
				value="${ pcontact.lastName }" size="30">
		</td>
	</tr>
	<tr>
		<td align="right"><b>First Name</b></td>
		<td>
			<input type="text" name="pcontact.firstName" id="pcontact.firstName" value="<c:out value="${pcontact.firstName}"/>" size="30">
		</td>
	</tr>
	<tr>
		<td align="right"><b>Address</b></td>
		<td>
			<input type="text" name="pcontact.address" id="pcontact.address" value="<c:out value="${pcontact.address}"/>" size="50">
		</td>
	</tr>
	<tr>
		<td align="right"><b>Address2</b></td>
		<td>
			<input type="text" name="pcontact.address2" id="pcontact.address2" value="<c:out value="${pcontact.address2}"/>" size="50">
		</td>
	</tr>
	<tr>
		<td align="right"><b>City</b></td>
		<td>
			<input type="text" name="pcontact.city" id="pcontact.city" value="<c:out value="${pcontact.city}"/>" size="30">
		</td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td align="right"><b>Province</b></td>
		<td>	
		
		<c:set var="select" value="${ region }" scope="page" />
		<c:if test="${ not empty pcontact.province }" >
			<c:set var="select" value="${ pcontact.province }" />
		</c:if>

        <select name="pcontact.province" id="pcontact.province">
			<option value="AB" ${ pageScope.select eq 'AB' ? 'selected' : '' }>AB-Alberta</option>
			<option value="BC" ${ pageScope.select eq 'BC' ? 'selected' : '' }>BC-British Columbia</option>
			<option value="MB" ${ pageScope.select eq 'MB' ? 'selected' : '' }>MB-Manitoba</option>
			<option value="NB" ${ pageScope.select eq 'NB' ? 'selected' : '' }>NB-New Brunswick</option>
			<option value="NL" ${ pageScope.select eq 'NL' ? 'selected' : '' }>NL-Newfoundland & Labrador</option>
			<option value="NT" ${ pageScope.select eq 'NT' ? 'selected' : '' }>NT-Northwest Territory</option>
			<option value="NS" ${ pageScope.select eq 'NS' ? 'selected' : '' }>NS-Nova Scotia</option>
			<option value="NU" ${ pageScope.select eq 'NU' ? 'selected' : '' }>NU-Nunavut</option>
			<option value="ON" ${ pageScope.select eq 'ON' ? 'selected' : '' }>ON-Ontario</option>
			<option value="PE" ${ pageScope.select eq 'PE' ? 'selected' : '' }>PE-Prince Edward Island</option>
			<option value="QC" ${ pageScope.select eq 'QC' ? 'selected' : '' }>QC-Quebec</option>
			<option value="SK" ${ pageScope.select eq 'SK' ? 'selected' : '' }>SK-Saskatchewan</option>
			<option value="YT" ${ pageScope.select eq 'YT' ? 'selected' : '' }>YT-Yukon</option>
			<option value="US" ${ pageScope.select eq 'US' ? 'selected' : '' }>US resident</option>
		</select> 
		
		<label for="pcontact.country" >Country </label>
		<input type="text" name="pcontact.country"  id="pcontact.country" value="<c:out value="${pcontact.country}"/>" size="2" maxlength="2">
		</td>
	</tr>
	<tr>
		<td align="right"><b>Postal</b></td>
		<td>
			<input type="text" name="pcontact.postal" id="pcontact.postal" value="<c:out value="${pcontact.postal}"/>" size="30">
		</td>
	</tr>
	<tr>
		<td align="right"><b>Res. Phone</b></td>
		<td>
			<input type="text" name="pcontact.residencePhone" id="pcontact.residencePhone" value="<c:out value="${pcontact.residencePhone}"/>" size="30">
		</td>
	</tr>
	<tr>
		<td align="right"><b>Cell Phone</b></td>
		<td>
			<input type="text" name="pcontact.cellPhone" id="pcontact.cellPhone" value="<c:out value="${pcontact.cellPhone}"/>" size="30">
		</td>
	</tr>	
	<tr>
		<td align="right"><b>Work Phone</b></td>
		<td>
			<input type="text" name="pcontact.workPhone" id="pcontact.workPhone" value="<c:out value="${pcontact.workPhone}"/>" size="15"/>
		Ext: <input type="text" name="pcontact.workPhoneExtension" value="<c:out value="${pcontact.workPhoneExtension}"/>" size="10"/>
		</td>
	</tr>
	<tr>
		<td align="right"><b>Fax</b></td>
		<td>
			<input type="text" name="pcontact.fax" id="pcontact.fax" value="<c:out value="${pcontact.fax}"/>" size="30">
		</td>
	</tr>	
	<tr>
		<td align="right"><b>Email</b></td>
		<td>
			<input type="text" name="pcontact.email" id="pcontact.email" value="<c:out value="${pcontact.email}"/>" size="30">
		</td>
	</tr>
	<tr>
		<td align="right"><b>Specialty</b></td>
		<td>
<oscar:oscarPropertiesCheck property="DEMOGRAPHIC_PATIENT_HEALTH_CARE_TEAM" value="true">	

			<select id="pcontact.specialty" name="pcontact.specialty" >					
				<c:forEach items="${ specialties }" var="specialtyType">			
					<option value="${ specialtyType.id }" ${ specialtyType.id eq requestScope.contactRole ? 'selected' : '' } >  
						<c:out value="${ specialtyType.specialty }" />
					</option>
				</c:forEach>
			</select>
</oscar:oscarPropertiesCheck>
<oscar:oscarPropertiesCheck property="DEMOGRAPHIC_PATIENT_HEALTH_CARE_TEAM" value="false">			
			<input type="text" name="pcontact.specialty" value="<c:out value="${ pcontact.specialty }"/>" size="30">
</oscar:oscarPropertiesCheck>
		</td>
	</tr>	
	<tr>
		<td align="right"><b>CPSO#</b></td>
		<td>
			<input type="text" name="pcontact.cpso" value="<c:out value="${pcontact.cpso}"/>" size="30">
		</td>
	</tr>		
	<tr>
		<td align="right"><b>System Id</b></td>
		<td>
			<input type="text" readonly="readonly" 
			name="pcontact.systemId" value="<c:out value="${pcontact.systemId}"/>" size="30">
		</td>
	</tr>	
	<tr>
		<td align="right"><b>Note</b></td>
		<td>
			<input type="text" name="pcontact.note" value="<c:out value="${pcontact.note}"/>" size="30">
		</td>
	</tr>	
	<tr>
		<td align="right"><b>Restrict to program</b></td>
			<td>
			 	<select name="pcontact.programNo" id="pcontact.programNo" title="Restrict to Program">
	            		<option value="0"></option>
	            		<%
	            			for(ProgramProvider pp:ppList2) {
	            				String selected = "";
	            				ProfessionalContact cc = (ProfessionalContact)request.getAttribute("pontact");
	            				if(pp.getProgramId() != null && cc != null && cc.getProgramNo() != null && pp.getProgramId().intValue() == cc.getProgramNo().intValue()) {
	            					selected = " selected=\"selected\" ";
	            				}
	            		%>
							<option value="<%=pp.getProgramId()%>" <%=selected %>><%=pp.getProgram().getName() %></option>
						<%
	            			}
						%>
	            	</select>
			 </td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td align="center" bgcolor="#CCCCFF" colspan="2">
			<input type="button" name="submitbtn" value="<bean:message key="admin.resourcebaseurl.btnSave"/>" onclick="javascript: onSave();"> 			
			<input type="button" name="cancelbtn" value="<bean:message key="admin.resourcebaseurl.btnExit"/>" onClick="window.close()">
		</td>
	</tr>	
</table>
</html:form>
</body>
</html:html>
