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

<%@page import="org.oscarehr.PMmodule.model.Program"%>
<%@page import="org.oscarehr.PMmodule.dao.ProgramDao"%>
<%@page import="org.oscarehr.common.model.ContactType"%>
<%@page import="org.oscarehr.common.dao.ContactTypeDao"%>
<%@page import="oscar.OscarProperties"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="org.oscarehr.PMmodule.model.ProgramProvider"%>
<%@page import="org.oscarehr.managers.ProgramManager2"%>
<%@page import="org.oscarehr.managers.ContactManager"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.common.dao.ProgramContactTypeDao"%>
<%@ page import="org.oscarehr.common.model.ProgramContactType" %>
<%@ page import="java.util.List, org.apache.commons.lang.StringUtils" %>
<%@ page import="org.oscarehr.common.web.ContactAction" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.model.DemographicContact" %>
<%@ page import="org.oscarehr.common.model.Demographic" %>
<%@ page import="org.oscarehr.common.dao.DemographicDao" %>
<%@ page import="org.oscarehr.common.dao.ContactSpecialtyDao" %>
<%@ page import="org.oscarehr.common.model.ContactSpecialty" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<security:oscarSec roleName="${ sessionScope.userrole }" objectName="_demographic" rights="r" reverse="${ false }">

<% 
	List<DemographicContact> demographicContacts = null;
	List<Provider> providerList = null;
	ProviderDao providerDao = null;
	DemographicDao demographicDao = null;
	Demographic demographic = null;
	ContactSpecialtyDao contactSpecialtyDao = null;
	List<ContactSpecialty> specialty = null;
	String demographicNoString = request.getParameter("demographicNo");
	String type = request.getParameter("type");
	if(type == null) {
		type = DemographicContact.CATEGORY_PROFESSIONAL;	
	}
	if ( ! StringUtils.isBlank( demographicNoString ) ) {		
		providerDao = SpringUtils.getBean(ProviderDao.class);
		providerList = providerDao.getActiveProviders();
		demographicDao = SpringUtils.getBean(DemographicDao.class);
		demographic = demographicDao.getClientByDemographicNo( Integer.parseInt(demographicNoString) );
		demographicContacts = ContactAction.getDemographicContacts(demographic,type);
		contactSpecialtyDao = SpringUtils.getBean(ContactSpecialtyDao.class);
		specialty = contactSpecialtyDao.findAll();
	}	
	
	String headerTitle = "";
	if(type.equals(DemographicContact.CATEGORY_PROFESSIONAL)) {
		headerTitle = "Health Care Team";
	}
	if(type.equals(DemographicContact.CATEGORY_PERSONAL)) {
		headerTitle = "Personal Contacts";
	}
	if(type.equals(DemographicContact.CATEGORY_OTHER)) {
		headerTitle = "Other Contacts";
	}
	
	ContactManager contactManager = SpringUtils.getBean(ContactManager.class);
	ProgramManager2 programManager2 = SpringUtils.getBean(ProgramManager2.class);
	
	List<ProgramContactType> pctList = contactManager.getContactTypesForCurrentProgramAndCategory(LoggedInInfo.getLoggedInInfoFromSession(request),"Health Care Provider");
	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
	List<ProgramProvider> ppList = programManager2.getProgramDomain(loggedInInfo, loggedInInfo.getLoggedInProviderNo());
	
	
	pageContext.setAttribute("headerTitle", headerTitle);
	pageContext.setAttribute("professionalSpecialistType", DemographicContact.TYPE_PROFESSIONALSPECIALIST);
	pageContext.setAttribute("providerType", DemographicContact.TYPE_PROVIDER);
	pageContext.setAttribute("professionalContactType", DemographicContact.TYPE_CONTACT);
	pageContext.setAttribute("demographicNoString", demographicNoString);
	pageContext.setAttribute("demographic", demographic);
	pageContext.setAttribute("demographicContacts", demographicContacts);
	pageContext.setAttribute("specialty", specialty);
	pageContext.setAttribute("providerList", providerList);
	
	OscarProperties op = OscarProperties.getInstance();
	
	ContactTypeDao contactTypeDao = SpringUtils.getBean(ContactTypeDao.class);
	ProgramDao programDao = SpringUtils.getBean(ProgramDao.class);
	
%>

<%-- DETACHED VIEW ENABLED  --%>
<c:if test="${ param.view eq 'detached' }">

	<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
	<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
	
	<!DOCTYPE html>
	<html>
	<head>
	
	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/css/healthCareTeam.css" />
	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/share/css/OscarStandardLayout.css" />
	<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jquery.js" ></script>
	
</c:if>
<%-- END DETACHED VIEW ENABLED  --%> 

<script type="text/javascript" >
var popupWindow;	
//--> Add contact from popup selection or internal method.
function popUpData( data ){	

	if( data != null ) {

		jQuery('#searchHealthCareTeamInput').attr('value', null);
		var path = "${ oscar_context_path }/demographic/Contact.do";
		var target = '#fullHealthCareTeam';
		var json = JSON.parse(data);
		
		var role = json.contactRole.toUpperCase(); // type of doctor - user determined
		var type = json.contactType.trim(); // type of contact - pre-determined as 0 = internal | 3 = external
		var contactId = json.contactId;// id number of any contact.
		var contactName = json.contactName.trim(); // name of the contact being added.
		var method = json.method.trim(); 
		var id = json.demographicContactId;
		
		// Hoping for Action servlets that consume JSON in the future.			
		var demographic_no = "${ demographicNoString }"; 								
		var active = 1;
		var consentToContact = 1;
		var procontact_num = "";
		//var id = 0;
		var contact_num = "";
		var contactCategory = "";
		var contactObject = "";
		
		// force parameters required by the saveManage action method.
		if(method == "saveManage") {			
			procontact_num = 1; // total number of procontacts to add.				
			//id = 0; // Id of a demographicContact object - set as zero when adding new.
			contact_num = 0; // total number of demographic contacts to add.
			contactCategory = "procontact"; // prefix is always "procontact" but could change later.
			contactObject = contactCategory + '_' + procontact_num + '.';
		}
		
		if( 'editHealthCareTeam' == method ) {
			target = 'popup';
		}
		
		if( ! type ) {
			type = jQuery('#searchInternalExternal').val().trim();
		}
		
		if( ! role.trim() ) {
			role = jQuery('#selectHealthCareTeamRoleType option:selected').val(); 
		}
		
		var programId = jQuery('#selectTeamProgramId option:selected').val();
		

		var param = 'postMethod=ajax&method=' + method + 
					'&demographic_no=' + demographic_no +
					'&demographicNo=' + demographic_no +
					'&procontact_num=' + procontact_num +
					'&contact_num=' + contact_num +
					'&type=<%=type%>' + 
					'&' + contactObject + 'id=' + id +
					'&' + contactObject + 'contactId=' + contactId +
					'&' + contactObject + 'contactName=' + contactName +
					'&' + contactObject + 'type=' + type + 
					'&' + contactObject + 'role=' + role +
					'&' + contactObject + 'active=' + active +
					'&' + contactObject + 'contactTypeId=' + jQuery('#selectTeamRoleType option:selected').val() +
					'&' + contactObject + "programId=" + programId +
					'&' + contactObject + 'consentToContact=' + consentToContact;
		
		//alert(param);
		//this.window.focus();
		return sendData(path, param, target);	
	}	
}

//--> AJAX the data to the server.
function sendData(path, param, target) {
	var success = false;
 		jQuery.ajax({
 		    url: path,
 		    type: 'POST',
 		    data: param,
 		  	dataType: 'html',
 		    success: function(data) {
 		    	if( target == 'popup' ) {
	  		    	popupWindow = window.open("", "_blank", "scrollbars=yes, resizable=yes, width=600, height=600");
					with(popupWindow.document) {
				      open();
				      write(data);
				      close();
			    	}
 		    	} else {
 		    		renderResponse(data, target);
 		    	}
 		    	success = true;
 		    }
 		});
 		return success;
}

//--> Refresh containing elements 
function renderResponse(html, id) {
	
	if( id instanceof Array ) {
		jQuery.each(id, function(i, val){
			jQuery(val).replaceWith( jQuery(val, html) );
		});			
	} else {			
		jQuery(id).replaceWith( html );
	}
	
	jQuery().bindFunctions();
	
	// bounce back to parent
	try {
		opener.popUpData(html);
	} catch(error) {
		// do nothing
	}
	
	try{
		jQuery("input:submit").eq(1).focus();
		jQuery().resetFields();
	} catch(error) {
		// do nothing
	}
	 
	/* var lastrow = jQuery( id + " tr:last" );		
	lastrow.css( { backgroundColor: "yellow" } );

	setTimeout(function(){
		lastrow.css( { backgroundColor:"inherit" }); 
    },3000); */
}

//--> reset all list input fields
jQuery.fn.resetFields = function() {
	// clean search fields and re-focus
	jQuery('#searchHealthCareTeamInput').val("Last Name, First Name").css('color', 'grey');
}

//--> Remove/Edit contact action. Wrapped in a function to re-bind after postback
jQuery.fn.bindFunctions = function() {
	
	
}
		
//--> Search external providers	function
function searchExternalProviders(action) {
	
	var contactRole = jQuery('#selectHealthCareTeamRoleType option:selected').val();
	var searchfield = jQuery('#searchHealthCareTeamInput').val();
	var windowspecs = "width=500,height=600,left=100,top=100, scrollbars=yes, resizable=yes";
	var programId = jQuery('#selectTeamProgramId option:selected').val();
	
	
	popupWindow = window.open(
		'procontactSearch.jsp?form=updatedelete' +
		'&elementName=contactName' +
		'&elementId=contactId' +
		'&programNo=' + programId + 
		'&keyword='+ searchfield +
		'&programId=' + programId + 
		'&contactRole=' + contactRole + 
		'&relatedTo=<%=demographicNoString%>' + 
		'&submit=' + action +
		'&list=all',
		'ManageContacts',
		windowspecs			
	);	

}

window.onunload = function() {
    if (popupWindow && ! popupWindow.closed) {
    	popupWindow.close();
    }
};

//--> Document Ready Methods 
jQuery(document).ready( function($) {

	//console.log('binding');
	jQuery('.actionlink').bind("click", function(event){			
		 var id = this.id.split("_")[1].trim();
		 var param = '{"contactId":"' + id + 
			'","contactName":"' +  
			'","contactRole":"' +
			'","demographicContactId":"0' +
			'","contactType":"';				
		 
		 if( this.value == "remove") {	 
			param += '","method":"removeContact"}'; 		 
		 } else if( this.value == "edit") {	 
			 param += '","method":"editHealthCareTeam"}'; 				 
		 }
	
		//alert(param);
		 
		 popUpData(param);
	});
	
	//--> Change MRP Status
	jQuery("input[id*='mostResponsibleProviderCheckbox']").bind("change", function(){
		var contactId = jQuery("#" + this.id).val();
		var path = "${ oscar_context_path }/demographic/Contact.do"; 
		var param = "method=setMRP&contactId=" + contactId;
		var target = '#fullHealthCareTeam';
		sendData(path, param, target);
	})

	//--> Add internal provider
	jQuery('#addHealthCareTeamButton').bind("click", function(){
		// get the selected value
		var selected = jQuery('#internalProviderList option:selected');
		var selectedtext = selected.text();
		var roledata = ( (selectedtext.split("(")[1]).trim() ).split(")")[0].trim()
		var contactId = selected.val();
		var contactName = (selectedtext.split("(")[0]).trim();
		var mrp = jQuery('#mostResponsibleProviderCheckbox').is(":checked");

		var param = '{"contactId":"' + contactId + 
			'","contactName":"' + contactName + 
			'","contactRole":"' + roledata +
			'","demographicContactId":"0'+
			'","method":"saveManage' +
			'","contactType":""}';

		popUpData(param);
	})	

	//--> Search external providers events
	jQuery('#searchHealthCareTeamInput').bind('keydown', function(event) {		
	    if (event.keyCode == 13) {
	    	event.preventDefault();
	    	searchExternalProviders("Search");
	    }     
	});
	
	jQuery('#searchHealthCareTeamInput').val("Last Name, First Name").css('color','grey')
	.focus(function(){
	    if(this.value == "Last Name, First Name"){
	         this.value = "";
	         jQuery('#searchHealthCareTeamInput').css('color','black')
	    }		
	}).blur(function(){
	    if(this.value==""){
	         this.value = "Last Name, First Name";
	         jQuery('#searchHealthCareTeamInput').css('color','grey')
	    }
	});
			
	jQuery('#searchHealthCareTeamButton').bind("click", function(){
		searchExternalProviders(this.value);
	});		

	//--> Toggle search options
	jQuery(".external").hide();	// internal = on, external = off	
	jQuery('#searchInternalExternal').bind("change", function(){
		var selected = jQuery('#' + this.id + ' option:selected').text();
		jQuery(this).find('option').each(function(){
			if( selected == $(this).text() )	{
				jQuery( "." + selected ).toggle();
			} else {
				jQuery( "." + $(this).text() ).toggle();
			}

		});			
	})
	
	
	//--> do on page ready
	jQuery().resetFields();	
	jQuery().bindFunctions();
	
	//jQuery('#searchInternalExternal').trigger("change");
	
	<%
		if("false".equals(op.getProperty("DEMOGRAPHIC_CONTACT.AllowInternalHCP", "true"))) {
	%>
	jQuery(".internal").hide();	
	jQuery(".external").show();	
	
	jQuery("#searchInternalExternal option:contains('internal')").attr("disabled","disabled");
	jQuery("#searchInternalExternal").val("external");
	<% } %>
	

	
})
				
</script>

<%-- DETACHED VIEW ENABLED  --%>
<c:if test="${ param.view eq 'detached' }" >
	</head>
	<body id="${ param.view }View" >
	<table class="MainTable" >

	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" width="20%">Manage Health Care Team</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>&nbsp;</td>
				<td>
					<c:out value="${ demographic.lastName }" />,&nbsp;
					<c:out value="${ demographic.firstName }" />&nbsp;
					<c:out value="${ demographic.age }" />&nbsp;years
				</td>
				<td style="text-align: right">
					<oscar:help keywords="contact" key="app.top1"/> | 
					<a href="javascript:popupStart(300,400,'About.jsp')">
					<bean:message key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')">
					<bean:message key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr><td colspan="2">
</c:if>

<%-- END DETACHED VIEW ENABLED  --%>

<div id="fullHealthCareTeam">


<table id="listHealthCareTeam" class="${ param.view }View" >
	<%-- MANAGE PATIENTS HEALTH CARE TEAM  --%>
		<%-- LIST CURRENT HEALTH CARE TEAM --%>
		
		<c:set value="${ demographicContacts }" var="demographicContactList" scope="page" />

		<tr id="tableTitle" >
			<th colspan="8" class="alignLeft" >${headerTitle}</th>
		</tr>

		<c:if test="${ not empty demographicContactList }" >
			<tr id="healthCareTeamSubHeading" >
				<td></td><td></td><td>Name</td><td>Phone</td><td>Fax</td><td></td><td></td>
			</tr>
		</c:if>
		<c:forEach items="${ demographicContactList }" var="demographicContact" >
			<c:set value="internal" var="internal" scope="page" />
			<c:set value="${ demographicContact.details.workPhone }" var="workPhone" scope="page" />
			
			<tr>					
			
			
				<td class="alignLeft" >	
					<c:out value="${ demographicContact.role }" />				 					
				</td>
				<td class="alignLeft" >	
					<%
						pageContext.setAttribute("contactTypeName", "");
						DemographicContact dc = (DemographicContact) pageContext.getAttribute("demographicContact");
						Integer ctId = dc.getContactTypeId();
						
						String contactTypeName = "";
						if(ctId != null && ctId > 0) {
							ContactType ct = contactTypeDao.find(ctId);
							if(ct != null) {
								contactTypeName = ct.getName();
								pageContext.setAttribute("contactTypeName", contactTypeName);
							}
						}
						
					%>
					<c:out value="${ contactTypeName }" />				 					
				</td>
                <td class="alignLeft" >
                		<c:out value="${ demographicContact.contactName }" />
                </td>
                
                 	<c:if test="${ workPhone eq internal }" > 
						<td>&#40;<c:out value="${ internal }" />&#41;</td>
						<td>&nbsp;</td>
					</c:if>	
					                 	
               		<c:if test="${ workPhone ne internal }" >	                 		
                 		<td><c:out value="${ workPhone }" /></td>
                 		<td><c:out value="${ demographicContact.details.fax }" /></td>
               		</c:if>
               	 	
	            <td class="alignRight">
	            	<input type="button" 
	            		id="remove<c:out value="${ demographicContact.type }" />_<c:out value="${ demographicContact.id }" />" 
	            		class="actionlink" value="remove" />
				</td>
				<td class="alignLeft">
					<c:if test="${ demographicContact.type gt 0 }">
						<input type="button" 
							id="edit<c:out value="${ demographicContact.type }" />_<c:out value="${ demographicContact.id }" />" 
							class="actionlink" value="edit" />
					</c:if>
					<c:if test="${ demographicContact.type eq 0 }" >					  
					 	<input type="radio" name="mostResponsibleProviderCheckbox"
					 		value="${ demographicContact.id }" 
					 		id="mostResponsibleProviderCheckbox_${ demographicContact.id }" 
					 		${ demographicContact.mrp ? 'checked' : '' } />
					 		
					 	<label for="mostResponsibleProviderCheckbox_${ demographicContact.id }" 
					 		title="Most Responsible Provider" >MRP</label>
					</c:if>
				</td>
            </tr>	
		</c:forEach>
</table>

<table id="addEditHealthCareTeam" class="${ param.view }View" >	
		<%-- ADD NEW MEMBER TO HEALTH CARE TEAM --%>

		<tr>
			<td class="alignLeft"><strong>add a provider:</strong></td>		
			<td class="alignLeft">
				<select name="searchInternalExternal" id="searchInternalExternal" >
					
					<option value="${ providerType }" >internal</option>
					
		            <option value="${ professionalContactType }" >external</option>
				</select>
			</td>
			
			<!-- If Internal list, then display the Internal Demographic options
			 External, then display the external search options -->

			 <td class="internal" >
			 	<select name="internalProviderList" id="internalProviderList">
			 		<c:forEach items="${ providerList }" var="providerDetail" >
			 			<option value="${ providerDetail.providerNo }" >
			 				<c:out value="${ providerDetail.formattedName }" />
			 				&#40;<c:out value="${ providerDetail.specialty }" />&#41;
			 			</option>
			 		</c:forEach>		 
				</select>
			 </td>
			 
			 <td class="internal">
				<input type="button" name="addHealthCareTeamButton" 
					id="addHealthCareTeamButton" value="Add" />						
			</td>
			
			<td class="external" >
				<select id="selectHealthCareTeamRoleType" name="selectHealthCareTeamRoleType" >					
					<c:forEach items="${ specialty }" var="specialtyType">						
						<option value="${ specialtyType.id }" ${ specialtyType.specialty eq 'UNKNOWN' ? 'selected' : '' } > 						 
							<c:out value="${ specialtyType.specialty }" />			
						</option>
					</c:forEach>
				</select>
			</td>
			
			<td class="external" >
				<select id="selectTeamRoleType" name="selectTeamRoleType" >					
					<%
					for(ProgramContactType pct:pctList) {
					%>
							<option value="<%=pct.getContactType().getId()%>"><%= pct.getContactType().getName() %></option>
					<% } %>
				</select>
			</td>
			
			
			
			 
			<td class="external" >
				<input type="text" id="searchHealthCareTeamInput" 
					name="searchHealthCareTeamInput" 
					value="" />
			</td>
			
			<td class="external">
				<input type="button" name="searchHealthCareTeamButton" id="searchHealthCareTeamButton" value="Search" />						
			</td>
				
		</tr>
		
	<%-- END MANAGE PATIENTS HEALTH CARE TEAM  --%>
</table>

</div>

<%-- DETACHED VIEW ENABLED  --%>
<c:if test="${ param.view eq 'detached' }">
	</td></tr>
	</table>
	</body>
	</html>
</c:if>
<%-- END DETACHED VIEW ENABLED  --%>


</security:oscarSec>
