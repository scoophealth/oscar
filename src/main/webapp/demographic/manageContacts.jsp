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

<%@page import="org.oscarehr.PMmodule.dao.ProgramDao"%>
<%@page import="org.oscarehr.PMmodule.model.Program"%>
<%@page import="oscar.OscarProperties"%>
<%@page import="org.oscarehr.PMmodule.model.ProgramProvider"%>
<%@page import="org.oscarehr.managers.ProgramManager2"%>
<%@page import="org.oscarehr.managers.ContactManager"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.common.dao.ProgramContactTypeDao"%>
<%@ page import="org.oscarehr.common.model.ProgramContactType" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List, org.apache.commons.lang.StringUtils" %>
<%@ page import="org.oscarehr.common.web.ContactAction" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.model.DemographicContact" %>
<%@ page import="org.oscarehr.common.model.Demographic" %>
<%@ page import="org.oscarehr.common.dao.DemographicDao" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<link rel="stylesheet" href="../css/cupertino/jquery-ui-1.8.18.custom.css" type="text/css" />
<link rel="stylesheet" href="../css/jquery.ui.autocomplete.css" type="text/css" />
<style type="text/css">
	.ui-autocomplete-loading { background: white url('../images/ui-anim_basic_16x16.gif') right center no-repeat; }
</style>
<script type="text/javascript" src="../js/jquery-ui-1.8.18.custom.min.js"></script>


<security:oscarSec roleName="${ sessionScope.userrole }" objectName="_demographic" rights="r" reverse="${ false }">

<% 
	List<DemographicContact> demographicContacts = null;
	List<Demographic> demographicList = null;
	ProviderDao providerDao = null;
	DemographicDao demographicDao = null;
	Demographic demographic = null;
	String demographicNoString = request.getParameter("demographicNo");
	String type = request.getParameter("type");
	if(type == null) {
		type = DemographicContact.CATEGORY_PERSONAL;	
	}
	if ( ! StringUtils.isBlank( demographicNoString ) ) {		
		providerDao = SpringUtils.getBean(ProviderDao.class);
		demographicDao = SpringUtils.getBean(DemographicDao.class);
		demographic = demographicDao.getClientByDemographicNo( Integer.parseInt(demographicNoString) );
		demographicContacts = ContactAction.getDemographicContacts(demographic,type);
		demographicList = new java.util.ArrayList<Demographic>();
		
	}	
	
	String headerTitle = "";
	if(type.equals(DemographicContact.CATEGORY_PERSONAL)) {
		headerTitle = "Personal Contacts";
	}
	else if(type.equals(DemographicContact.CATEGORY_OTHER)) {
		headerTitle = "Other Contacts";
	}
	
	pageContext.setAttribute("headerTitle", headerTitle);
	pageContext.setAttribute("demographicType", DemographicContact.TYPE_DEMOGRAPHIC);
	pageContext.setAttribute("otherContactType", DemographicContact.TYPE_CONTACT);
	pageContext.setAttribute("demographicNoString", demographicNoString);
	pageContext.setAttribute("demographic", demographic);
	pageContext.setAttribute("demographicContacts", demographicContacts);
	pageContext.setAttribute("demographicList", demographicList);
	
	
	ProgramManager2 programManager2 = SpringUtils.getBean(ProgramManager2.class);
	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
	List<ProgramProvider> ppList = programManager2.getProgramDomain(loggedInInfo, loggedInInfo.getLoggedInProviderNo());
	
	OscarProperties op = OscarProperties.getInstance();
	
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
var popupWindow<%=type%>;	
//--> Add contact from popup selection or internal method.
function popUpData<%=type%>( data ){	

	if( data != null ) {

		jQuery('#search<%=type%>TeamInput').attr('value', null);
		var path = "${ oscar_context_path }/demographic/Contact.do";
		var target = '#<%=type%>TeamDiv';
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
			procontact_num = 0; // total number of procontacts to add.				
			//id = 0; // Id of a demographicContact object - set as zero when adding new.
			contact_num = 1; // total number of demographic contacts to add.
			contactCategory = "contact"; // prefix is always "procontact" but could change later.
			contactObject = contactCategory + '_' + contact_num + '.';
		}
		
		if( 'edit<%=type%>Team' == method ) {
			target = 'popup';
		}
		
		
		if( ! type ) {
			type = jQuery('#searchInternalExternal<%=type%>').val().trim();
		}
		
		
		var t = 'External';
		if(type == 1 || type == 0) {
			t = 'Internal';
		}
		if( ! role.trim() ) {
			role = jQuery('#select'+t+'<%=type%>TeamRoleType option:selected').val(); 
		}

		
		var programId = json.programId;
		
		var param = 'postMethod=ajax2&method=' + method + 
					'&demographic_no=' + demographic_no +
					'&demographicNo=' + demographic_no +
					'&procontact_num=' + procontact_num +
					'&contact_num=' + contact_num +
					'&type=<%=type%>' +
					'&' + contactObject + 'id=' + id +
					'&' + contactObject + 'contactId=' + contactId +
					'&' + contactObject + 'contactName=' + contactName +
					'&' + contactObject + 'type=' + type + 
					'&' + contactObject + 'role=' + jQuery('#select'+t+'<%=type%>TeamRoleType option:selected').text() +
					'&' + contactObject + 'active=' + active +
					'&' + contactObject + 'category=<%=type%>' +
					'&' + contactObject + 'contactTypeId=' + jQuery('#select'+t+'<%=type%>TeamRoleType option:selected').val() +
					'&' + contactObject + "programId=" + programId +
					'&' + contactObject + 'consentToContact=' + consentToContact;
		
		//this.window.focus();
		return sendData<%=type%>(path, param, target);	
	}	
}

//--> AJAX the data to the server.
function sendData<%=type%>(path, param, target) {
	
	var success = false;
 		jQuery.ajax({
 		    url: path,
 		    type: 'POST',
 		    data: param,
 		  	dataType: 'html',
 		    success: function(data) {
 		    	if( target == 'popup' ) {
	  		    	popupWindow<%=type%> = window.open("", "_blank", "scrollbars=yes, resizable=yes, width=600, height=600");
					with(popupWindow<%=type%>.document) {
				      open();
				      write(data);
				      close();
			    	}
 		    	} else {
 		    		renderResponse<%=type%>(data, target);
 		    	}
 		    	success = true;
 		    }
 		});
 		return success;
}

//--> Refresh containing elements 
function renderResponse<%=type%>(html, id) {
	if( id instanceof Array ) {
		jQuery.each(id, function(i, val){
			jQuery(val).replaceWith( jQuery(val, html) );
		});			
	} else {			
		//alert(html);
		jQuery(id).replaceWith( html );
	}
	
	jQuery().bindFunctions();
	
	// bounce back to parent
//	try {
//		opener.popUpData<%=type%>(html);
//	} catch(error) {
		// do nothing
//	}
	
	try{
		jQuery("input:submit").eq(1).focus();
		jQuery().resetFields();
	} catch(error) {
		// do nothing
	}
	 

}

//--> reset all list input fields
jQuery.fn.resetFields = function() {
	// clean search fields and re-focus
	jQuery('#search<%=type%>TeamInput').val("Last Name, First Name").css('color', 'grey');
}

//--> Remove/Edit contact action. Wrapped in a function to re-bind after postback
jQuery.fn.bindFunctions = function() {
	
	
}
		
//--> Search external providers	function
function searchExternalProviders<%=type%>(action) {
	
	var contactRole = jQuery('#selectExternal<%=type%>TeamRoleType option:selected').val();
	var searchfield = jQuery('#search<%=type%>TeamInput').val();
	var windowspecs = "width=500,height=600,left=100,top=100, scrollbars=yes, resizable=yes";
	//var programId = jQuery('#selectExternal<%=type%>TeamProgramId option:selected').val();
	
	
	popupWindow<%=type%> = window.open(
		'contactSearch.jsp?form=updatedelete' +
		'&elementName=search<%=type%>TeamInput' +
		'&elementId=search<%=type %>TeamInputId' +
		//'&programNo=' + programId + 
		'&keyword='+ searchfield +
		'&contactRole=' + contactRole +
		'&relatedTo=<%=demographicNoString%>' + 
		'&submit=' + action ,
		'ManageContacts',
		windowspecs			
	);	

}

window.onunload = function() {
    if (popupWindow<%=type%> && ! popupWindow<%=type%>.closed) {
    	popupWindow<%=type%>.close();
    }
};

//--> Document Ready Methods 
jQuery(document).ready( function($) {

	jQuery('.actionlink<%=type%>').bind("click", function(event){
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
	 
	 popUpData<%=type%>(param);
	});
	
	jQuery('.viewdemolink<%=type%>').bind("click", function(event){
		var id = jQuery(this).attr('contact-id');
		popupPage(800,800,'demographiccontrol.jsp?demographic_no=' + id + '&displaymode=edit&dboperation=search_detail');
	});
	
	jQuery('.viewcontactlink<%=type%>').bind("click", function(event){
		var id = jQuery(this).attr('contact-id');
		popupPage(500,800,'Contact.do?method=editContact&contact.id=' + id);
	});
	

	//--> Add internal provider
	jQuery('#add<%=type%>TeamButton').bind("click", function(){
		
		var category = jQuery(this).attr('id').substring(3,jQuery(this).attr('id').indexOf("Team"));
		
		var selectedtext = jQuery("#searchInternalExternal<%=type%> option:selected").text();
		
		var demographicNo;
		var demoName;
		var type = 2;
		if(selectedtext == 'Internal') {
			type = 1;
			demographicNo = jQuery("#ptNumber<%=type %>").val();
			demoName = jQuery("#ptName<%=type %>").val();
		}
		
		var role = jQuery('#selectInternal<%=type%>TeamRoleType option:selected').val();
		
		var programId = jQuery('#selectInternal<%=type%>TeamProgramId option:selected').val();
		
		
		var param = '{"contactId":'  + demographicNo + 
			',"contactName":"' + demoName + 
			'","contactRole":"' + role +  
			'","demographicContactId":"0'+
			'","programId":"' + programId + 
			'","method":"saveManage' +
			'","contactType":"'+type+'"}';
		
		popUpData<%=type%>(param);
		
	});
	
	//--> Add external provider
	jQuery('#addExternal<%=type%>TeamButton').bind("click", function(){
		
		var category = jQuery(this).attr('id').substring(3,jQuery(this).attr('id').indexOf("Team"));
		
		var selectedtext = jQuery("#searchInternalExternal<%=type%> option:selected").text();
		
		var demographicNo;
		var demoName;
		var type = 2;
		
		contactId = jQuery("#search<%=type %>TeamInputId").val();
		contactName = jQuery("#search<%=type %>TeamInput").val();
		
		
		var role = jQuery('#selectExternal<%=type%>TeamRoleType option:selected').val();
		
		//var programId = jQuery('#selectExternal<%=type%>TeamProgramId option:selected').val();
			
		var param = '{"contactId":'  + contactId + 
			',"contactName":"' + contactName + 
			'","contactRole":"' + role + 
	//		'","programId":"' + programId + 
			'","demographicContactId":"0'+
			'","method":"saveManage' +
			'","contactType":"'+type+'"}';
		
			
		popUpData<%=type%>(param);
		
	});
	
	

	//--> Search external providers events
	jQuery('#search<%=type%>TeamInput').bind('keydown', function(event) {		
	    if (event.keyCode == 13) {
	    	event.preventDefault();
	    	searchExternalProviders<%=type%>("Search");
	    }     
	});
	
	jQuery('#search<%=type%>TeamInput').val("Last Name, First Name").css('color','grey')
	.focus(function(){
	    if(this.value == "Last Name, First Name"){
	         this.value = "";
	         jQuery('#search<%=type%>TeamInput').css('color','black')
	    }		
	}).blur(function(){
	    if(this.value==""){
	         this.value = "Last Name, First Name";
	         jQuery('#search<%=type%>TeamInput').css('color','grey')
	    }
	});
			
	jQuery('#search<%=type%>TeamButton').bind("click", function(){
		searchExternalProviders<%=type%>(this.value);
	});		

	//--> Toggle search options
	jQuery(".External<%=type%>").hide();	// internal = on, external = off	
	jQuery('#searchInternalExternal<%=type%>').bind("change", function(){
		var selected = jQuery('#' + this.id + ' option:selected').text();
		jQuery(this).find('option').each(function(){
			if( selected == $(this).text() )	{
				jQuery( "." + selected + '<%=type%>' ).toggle();
			} else {
				jQuery( "." + $(this).text() + '<%=type%>' ).toggle();
			}

		});			
	})
	
	
	//--> do on page ready
	jQuery().resetFields();	
	jQuery().bindFunctions();
	
	// for incremental patient search
	jQuery( "#ptName<%=type %>" ).autocomplete({		
		source: function(request, response) {
		  $.ajax({
		   	url: "SearchDemographic.do?activeOnly=true&outofdomain=true&relatedTo=<%=demographicNoString%>&query=" + encodeURIComponent(request.term),
			method: "POST",
		   	dataType: "json",
		   	success: function(data) {
		   		response($.map(data.results, function(item) {
			    	
				  if(item.status == "AC") {  
					 
			    	return {
					      label: item.formattedName + " : " + item.fomattedDob,
					      value: item.formattedName,
					      id: item.demographicNo
				     }
				  } else {
					  return;
				  }
		    }))
		   }
		  })
		 },
		 minLength: 2,
		 select: function( event, ui ) {
				 jQuery(this).removeClass("ui-autocomplete-loading");
			 		ui.item.id ?
			 			jQuery("#ptNumber<%=type %>").val(ui.item.id)
			 		:
			 			alert("No demographic number!")
			 		;

		 },
		 open: function() {
		  	jQuery(this).removeClass("ui-corner-all").addClass("ui-corner-top");
		 },
		 close: function() {
		  	jQuery(this).removeClass("ui-corner-top").addClass("ui-corner-all");
		 }

	});	
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

<div id="<%=type%>TeamDiv">

<table id="list<%=type %>Team" class="${ param.view }View" >
	<%-- MANAGE CONTACTS  --%>
		<%-- LIST CURRENT CONTACTS --%>
		
		<c:set value="${ demographicContacts }" var="demographicContactList" scope="page" />

		<tr id="tableTitle" >
			<th colspan="5" class="alignLeft" >${headerTitle}</th>
		</tr>

		<c:if test="${ not empty demographicContactList }" >
			<tr id="healthCareTeamSubHeading" >
				<td></td><td>Name</td><td>Home Phone</td><td></td><td></td>
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
                		<c:out value="${ demographicContact.contactName }" />
                </td>
                
                 <td class="alignLeft" >
                 		<%
                 		String phone = "";
                 		DemographicContact dc = (DemographicContact) pageContext.getAttribute("demographicContact");
						if(dc.getType() == DemographicContact.TYPE_DEMOGRAPHIC) {
							Demographic d = demographicDao.getDemographic(dc.getContactId());
							if(d != null) {
								phone = d.getPhone();
							}
						} else if(dc.getType() == DemographicContact.TYPE_CONTACT)  {
							phone = dc.getDetails().getResidencePhone();
						}
						pageContext.setAttribute("phone", phone);
                 		%>
                		<c:out value="${ phone}" />
                </td>
    		            	
               	 	
	            <td class="alignRight">
	            	<input type="button" 
	            		id="remove<c:out value="${ demographicContact.type }" />_<c:out value="${ demographicContact.id }" />" 
	            		class="actionlink<%=type %>" value="remove" />
				</td>
				<td class="alignLeft">
					
				<c:choose>
					<c:when test="${ demographicContact.type eq 1 }">
						<input type="button" class="viewdemolink<%=type%>" value="view" contact-id="${demographicContact.contactId}" />
					</c:when>
					<c:when test="${ demographicContact.type eq 2 }">
						<input type="button" class="viewcontactlink<%=type%>" value="view" contact-id="${demographicContact.contactId}" />
					</c:when>
				</c:choose>
					
					
				</td>
            </tr>	
		</c:forEach>
</table>

<table id="addEdit<%=type %>Team" class="${ param.view }View" >	
		<%-- ADD NEW MEMBER TO CONTACTS --%>

		<tr>
			<td class="alignLeft"><strong>Add a contact:</strong></td>		
			<td class="alignLeft">
				<select name="searchInternalExternal<%=type %>" id="searchInternalExternal<%=type %>" >
					<option value="${ demographicType }" >Internal</option>
		            <option value="${ otherContactType }" >External</option>
				</select>
			</td>
			
			<%
			//get options for this program.
			ContactManager contactManager = SpringUtils.getBean(ContactManager.class);
			//List<ProgramContactType> pctList = contactManager.getContactTypesForCurrentProgramAndCategory(LoggedInInfo.getLoggedInInfoFromSession(request),type);
			
			List<ProgramContactType> pctList = contactManager.getContactTypesForBedProgramAndCategory(LoggedInInfo.getLoggedInInfoFromSession(request),type, demographicNoString);
			
			%>
			<!-- If Internal list, then display the Internal Demographic options
			 External, then display the external search options -->

			<td class="Internal<%=type %>" >
				<select id="selectInternal<%=type %>TeamRoleType" name="selectInternal<%=type %>TeamRoleType" >					
					<%
					for(ProgramContactType pct:pctList) {
					%>
							<option value="<%=pct.getContactType().getId()%>"><%= pct.getContactType().getName() %></option>
					<% } %>
				</select>
			</td>
			
			
			 
			 <td class="Internal<%=type %>" >
			 	<input id="ptName<%=type %>"  name="ptName<%=type %>" type="text" size="30" maxlength="60" />
				<input type="hidden" id="ptNumber<%=type %>" name="ptNumber<%=type %>" />
			 </td>
			 
			 <td class="Internal<%=type%>">
				<input type="button" name="add<%=type %>TeamButton" 
					id="add<%=type %>TeamButton" value="Add" />						
			</td>
			
			<td class="External<%=type%>" >
				<select id="selectExternal<%=type %>TeamRoleType" name="selectExternal<%=type %>TeamRoleType" >					
					<%
					for(ProgramContactType pct:pctList) {
					%>
							<option value="<%=pct.getContactType().getId()%>"><%= pct.getContactType().getName() %></option>
					<% } %>
				</select>
			</td>
			
			<td class="External<%=type %>" >
			
			 </td>
			 
			<td class="External<%=type%>" >
				<input type="hidden" id="search<%=type %>TeamInputId" name="search<%=type %>TeamInputId" />
				<input type="text" id="search<%=type %>TeamInput" 
					name="search<%=type %>TeamInput" 
					value="" />
			</td>
			
			<td class="External<%=type%>">
				<input type="button" name="search<%=type %>TeamButton" id="search<%=type %>TeamButton" value="Search" />						
			</td>
			
			 <td class="External<%=type%>">
				<input type="button" name="addExternal<%=type %>TeamButton" 
					id="addExternal<%=type %>TeamButton" value="Add" />						
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
