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
<%@page import="java.io.StringWriter"%>
<%@page import="org.codehaus.jackson.map.ObjectMapper"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ page import="oscar.oscarRx.data.*,java.util.*"%>
<%@ page import="org.oscarehr.common.model.PharmacyInfo" %>

<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_rx" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_rx");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<html:html locale="true">
<head>

<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>

<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js">"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="SelectPharmacy.title" /></title>
<html:base />

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
%>

<bean:define id="patient"
	type="oscar.oscarRx.data.RxPatientData.Patient" name="Patient" />

<link rel="stylesheet" type="text/css" href="styles.css">

<style type="text/css">

.ui-autocomplete {
	background-color: #CEF6CE;
	border: 3px outset #2EFE2E;
	width:300px;
}

.ui-menu-item:hover {
		background-color:#426FD9;
		color:#FFFFFF;
}

</style>

<script type="text/javascript">

$(function() {
	 
    $( "#autocompletepharmacy" ).autocomplete({
      source: function( request, response ) {
    	  
    	  var city = $("#autocompletepharmacyCity").val();
    	  var searchTerm = request.term;
    	  
    	  if( city != null && city != "") {
    		searchTerm += "," + city;  
    	  }
    	  
    	  $.ajax({
    		  url: "<%= request.getContextPath() %>/oscarRx/managePharmacy.do?method=search",
    		  type: "GET",
    		  dataType: "json",
    		  data: {
    			  term: searchTerm   			  
    		  },
    		  contentType: "application/json",
    		  success: function( data ) {
    			  response($.map(data, function( item ) {
    				  return {
    					  label: item.name + " " + item.address + " " + item.city,
    					  value: item.name,
    					  pharmacy: item
    				  }
    			  }));
    		  }
    	  });
      },
      minLength: 2,  
      focus: function( event, ui ) {
    	  $( "#autocompletepharmacy" ).val( ui.item.value );
          return false;
      },
      select: function(event, ui) {    	  
    	  $('#pharmacyId').val(ui.item.pharmacy.id);
    	  $('#pharmacyName').val(ui.item.pharmacy.name);
    	  $('#pharmacyAddress').val(ui.item.pharmacy.address);
    	  $('#pharmacyCity').val(ui.item.pharmacy.city);
    	  $('#pharmacyProvince').val(ui.item.pharmacy.province);
    	  $('#pharmacyPostalCode').val(ui.item.pharmacy.postalCode);
    	  $('#pharmacyPhone1').val(ui.item.pharmacy.phone1);
    	  $('#pharmacyPhone2').val(ui.item.pharmacy.phone2);
    	  $('#pharmacyFax').val(ui.item.pharmacy.fax);
    	  $('#pharmacyEmail').val(ui.item.pharmacy.email);
    	  $('#pharmacyServiceLocationId').val(ui.item.pharmacy.serviceLocationIdentifier);
    	  $('#pharmacyNotes').val(ui.item.pharmacy.notes);
    	  return false;
      }      
    });
    
    $( "#autocompletepharmacyCity" ).autocomplete({
        source: function( request, response ) {
        	
      	  $.ajax({
      		  url: "<%= request.getContextPath() %>/oscarRx/managePharmacy.do?method=searchCity",
      		  type: "GET",
      		  dataType: "json",
      		  data: {
      			  term: request.term    			  
      		  },
      		  contentType: "application/json",
      		  success: function( data ) {
      			  response($.map(data, function( item ) {
      				  
      				  return {
      					  label: item,
      					  value: item      					  
      				  }
      			  }));
      		  }
      	  });
        },
        minLength: 2,  
        focus: function( event, ui ) {
      	  $( "#autocompletepharmacyCity" ).val( ui.item.value );
            return false;
        },
        select: function(event, ui) {    	  
      	  return false;
        }      
      });
    
    var length = $('#preferedPharmacy>option').length;
    
    if(  length > 0 ) {
    	    
    	var select = $('#preferedPharmacy>option:selected');
		editPharmacy($(select).val());
		
    }
    
  });
  
  function isFaxNumberCorrect() {
	  
	  var faxNumber = $("#pharmacyFax").val().trim();
	  var isCorrect = faxNumber.match(/^1?\s?\(?[0-9]{3}\)?[\-\s]?[0-9]{3}[\-\s]?[0-9]{4}$/);
  	
	  if( !isCorrect  ) {
	  		
	  	alert("Please enter the fax number in the format 9051234567");
	  	setTimeout( function() {
	  			$("#pharmacyFax").focus();	
	  	},1);
	  		
 	  }
	  
	  return isCorrect;
  	
  }
  
  function editPharmacy(data) {
	  resetForm();
	  var json = JSON.parse(data);
	  
	  $('#pharmacyId').val(json["id"]);
	  $('#pharmacyName').val(json["name"]);
	  $('#pharmacyAddress').val(json["address"]);
	  $('#pharmacyCity').val(json["city"]);
	  $('#pharmacyProvince').val(json["province"]);
	  $('#pharmacyPostalCode').val(json["postalCode"]);
	  $('#pharmacyPhone1').val(json["phone1"]);
	  $('#pharmacyPhone2').val(json["phone2"]);
	  $('#pharmacyFax').val(json["fax"]);
	  $('#pharmacyServiceLocationId').val(json["serviceLocationIdentifier"]);
	  $('#pharmacyNotes').val(json["notes"]);
	  
  }
  
  function resetForm() {
	  $('#pharmacyId').val("");
	  $('#pharmacyName').val("");
	  $('#pharmacyAddress').val("");
	  $('#pharmacyCity').val("");
	  $('#pharmacyProvince').val("");
	  $('#pharmacyPostalCode').val("");
	  $('#pharmacyPhone1').val("");
	  $('#pharmacyPhone2').val("");
	  $('#pharmacyFax').val("");
	  $('#pharmacyEmail').val("");
	  $('#pharmacyServiceLocationId').val("");
	  $('#pharmacyNotes').val("");
	  
	  return false;
  }
  
  function savePharmacy() {
	  
	  if( !confirm("You are about to edit/add a pharmacy to the database.  Is this what you want?\nSelect set Preferred Pharmacy to add pharmacy to the patient.")) {
		  return false;
	  }
	  
	  if( !isFaxNumberCorrect() ) {
		  return false;
	  }
	  
	  
	  if( $("#pharmacyId").val() != null && $("#pharmacyId").val() != "" ) {
	  	  
		  var data = $("#pharmacyForm").serialize();
		  $.post("<%=request.getContextPath() + "/oscarRx/managePharmacy.do?method=save"%>",
			  data, function( data ) {
		      	if( data.id ) {
		      		
		      		if( $('#preferedPharmacy option').length > 0 ) {
		      			var select = $('#preferedPharmacy>option:selected');
		      			var json = JSON.parse($(select).val());
		      			if( data.id = json.id ) {	      			
		      				$(select).val(JSON.stringify(data));
		      			}
		      		}
		      	    alert("Record saved!");
		      	    resetForm();
		      	    
		      	}
		      	else {
		      	    alert("There was a problem saving your record");
		      	}
		  },
		  "json"	  
		  );
	  }
	  else {
		  addPharmacy();
	  }
	  
	  return false;
  }
  
  function setPreferredPharmacy() {
	  
	  if( $('#preferredOrder>option:selected').val() == -1 ) {
		  alert("You have not set the preference order for this pharmacy");
		  return false;
	  }
	  
	  if( $("#pharmacyId").val() == "" ) {
		  alert("You must save or add the pharmacy first before setting it as a preferred pharmacy");
		  return false;
	  }
	  	  
	  if( $('#preferredOrder > option:selected').val() > $('#preferedPharmacy > option').length) {
		  
		  $('#preferredOrder').val($('#preferedPharmacy > option').length+1);
		  
	  }
	  
	  var data = $("#pharmacyForm").serialize();
	  $.post("<%=request.getContextPath() + "/oscarRx/managePharmacy.do?method=setPreferred"%>",
		  data, function( data ) {
		  
		  	if( data.id ) {
			  
		  		populatePreferredPharmacy();	
		  		
		  		setTimeout( function() {
		  			$("#preferedPharmacy > option").each( function() {
			  			
			  			var val = JSON.parse($(this).val());
			  			
			  			if( val.id == data.id ) {
			  				$(this).prop("selected",true);
			  			}
			  		});
			  		
		  			alert("Preferred Pharmacy set");	
		  		}, 1000);
			  	
		  		
	  			
	  		}
	  		else {
	  			
	  			alert("There was an error setting your preferred Pharmacy");
	  			
	  		}
		},
		"json"
	);

	return false;
  }
  
  
  function addPharmacy() {
	  
	  if( $("#pharmacyName").val() == "" ) {
		  alert("Please fill in at least the name of a pharmacy");
		  return false;
	  } 
	  
	  var data = $("#pharmacyForm").serialize();
	  
	  $.post("<%=request.getContextPath() + "/oscarRx/managePharmacy.do?method=add"%>",
			  data, function( data ) {
		  		if( data.success ) {
		  			alert("Pharmacy was added!");
		  			resetForm();
		  		}
		  		else {
		  			alert("There was an error saving your Pharmacy");
		  		}
	  		},
	  		"json"
	  	);
	  
	  return false;
  }
  
  function unlinkPharmacy(data) {
	  
	  if( $('#preferedPharmacy > option').length == 0 ) {
		  alert("There are no Pharmacies to unlink!");
		  return false;
	  }
	  
	  
	  var data = $("#pharmacyForm").serialize();
	  $.post("<%=request.getContextPath() + "/oscarRx/managePharmacy.do?method=unlink"%>",
		  data, function( data ) {
		  	
		  	if( data.id ) {
			  
		  		populatePreferredPharmacy();			  	
			  	
			  	alert("Pharmacy was successfully removed from the preferred pharmacy list");
	  			
	  		}
	  		else {
	  			
	  			alert("There was an error setting your preferred Pharmacy");
	  			
	  		}
		},
		"json"
	);
	  

	return false;
	  
  }
  
  function deletePharmacy(data) {
	  
	  if( $("#pharmacyId").val() == "" ) {
		  alert("Please select a pharmacy to delete!");
		  return false;
	  }
	  
	  
	  if( confirm("You are about to remove this pharmacy from EVERY patient's preferred list.  Are you sure you want to continue?")) {
		  
		  var data = $("#pharmacyForm").serialize();
		  $.post("<%=request.getContextPath() + "/oscarRx/managePharmacy.do?method=delete"%>",
				  data, function( data ) {
				  
				  	if( data.success ) {
					  
				  		populatePreferredPharmacy();			  	
					  	
					  	alert("Pharmacy was successfully removed from the pharmacy list");
			  			resetForm();
			  		}
			  		else {
			  			
			  			alert("There was an error deleting the Pharmacy");
			  			
			  		}
				},
				"json"
			);
	  }
	  
	  return false;
  }
  
  function populatePreferredPharmacy() {
	  
	  var demo = $("#demographicNo").val();
	  	$.get("<%=request.getContextPath() + "/oscarRx/managePharmacy.do?method=getPharmacyFromDemographic&demographicNo="%>"+demo,
	  			function( data ) {
	  				$("#preferedPharmacy").find("option").remove();
	  				
	  				var json;
	  				var preferredPharmacyInfo;
	  				for( var idx = 0; idx < data.length; ++idx  ) {
	  					preferredPharmacyInfo = data[idx];
	  					json = JSON.stringify(preferredPharmacyInfo);
	  					$("#preferedPharmacy").append($("<option></option>").val(json).text(preferredPharmacyInfo.name + " " + preferredPharmacyInfo.city));
	  				}			
	  	},
	  	"json"
	  	);
  }

</script>
</head>
<body topmargin="0" leftmargin="0" vlink="#0000FF">
<form id="pharmacyForm">
<table border="0" cellpadding="0" cellspacing="0"
	style="border-collapse: collapse" bordercolor="#111111" width="100%"
	id="AutoNumber1" height="100%">
	<%@ include file="TopLinks.jsp"%><!-- Row One included here-->
	<tr>
		<td width="100%" style="border-left: 2px solid #A9A9A9;" height="100%"
			valign="top">
		<table cellpadding="0" cellspacing="2"
			style="border-collapse: collapse" bordercolor="#111111" width="100%"
			height="100%">
			<tr>
				<td width="0%" valign="top">
				<div class="DivCCBreadCrumbs"><a href="SearchDrug3.jsp"> <bean:message
					key="SearchDrug.title" /></a> >  <bean:message key="SelectPharmacy.title" /></div>
				</td>
			</tr>
			<!----Start new rows here-->
			<tr>
				<td>
				<div class="DivContentTitle"><b><bean:message
					key="SearchDrug.nameText" /></b> <jsp:getProperty name="patient"
					property="surname" />, <jsp:getProperty name="patient"
					property="firstName" /></div>
				<br />
				&nbsp; <bean:message key="SelectPharmacy.instructions" /></td>
			</tr>			
			<tr>
				<td>				
				Search Pharmacy&nbsp;&nbsp;<input type="text" id="autocompletepharmacy"/>&nbsp;&nbsp;
				Narrow Search By City&nbsp;&nbsp;<input type="text" id="autocompletepharmacyCity"/> 
				
				Preferred Pharmacies&nbsp;&nbsp;<select id="preferedPharmacy" name="preferedPharmacy">
				<%
					RxPharmacyData pharmacyData = new RxPharmacyData();
			        List<PharmacyInfo> pharmacyList;
			        pharmacyList = pharmacyData.getPharmacyFromDemographic(Integer.toString(bean.getDemographicNo()));
			        if( pharmacyList != null ) {
			            ObjectMapper mapper = new ObjectMapper();
			            StringWriter jsonObject;
			        	for( PharmacyInfo pharmacyInfo : pharmacyList ) {
			        		jsonObject = new StringWriter();
			        		mapper.writeValue(jsonObject, pharmacyInfo);
				%>
							<option value='<%=jsonObject.toString().replaceAll("'", "")%>'><%=pharmacyInfo.getName() + " " + pharmacyInfo.getCity()%></option>
				<%
			        	}
			        }
				%>
				</select>

				<input type="button" value="<bean:message key="SelectPharmacy.editLink" />" onclick="editPharmacy($('#preferedPharmacy>option:selected').val())"/>&nbsp;
				<input type="button" value="Unlink" onclick="unlinkPharmacy($('#preferedPharmacy>option:selected').val())"/>
				<input type="hidden" id="pharmacyId" name="pharmacyId"/>
				<input type="hidden" id="demographicNo" name="demographicNo" value="<%=bean.getDemographicNo()%>"/>
                <div style=" width:860px; height:460px; overflow:auto;">
				<table>
					<tr>
						<td><bean:message key="SelectPharmacy.table.pharmacyName" /></td>
						<td><bean:message key="SelectPharmacy.table.address" /></td>
						<td><bean:message key="SelectPharmacy.table.city" /></td>						
					</tr>
					<tr>
						<td>
						<input type="text" id="pharmacyName" name="pharmacyName"/>
						
						<!-- a
							href="LinkPharmacy2.do?ID=&DemoId=<jsp:getProperty name="patient" property="demographicNo"/>"></a--></td>
						<td><input type="text" id="pharmacyAddress" name="pharmacyAddress" size="32"/></td>
						<td><input type="text" id="pharmacyCity" name="pharmacyCity" size="32"/></td>							
					</tr>
					<tr>
						<td>Province</td>
						<td colspan="2">Postal Code</td>
					</tr>
					<tr>
						<td><input type="text" id="pharmacyProvince" name="pharmacyProvince" size="32"/></td>
						<td colspan="2"><input type="text" id="pharmacyPostalCode" name="pharmacyPostalCode" size="12"/></td>
					</tr>
					<tr>
						<td><bean:message key="SelectPharmacy.table.phone"/></td>
						<td>Phone 2</td>
						<td><bean:message key="SelectPharmacy.table.fax" /></td>
					</tr>
					<tr>											
						<td><input type="text" id="pharmacyPhone1" name="pharmacyPhone1" size="12"/></td>
						<td><input type="text" id="pharmacyPhone2" name="pharmacyPhone2" size="12"/></td>
						<td><input type="text" id="pharmacyFax" name="pharmacyFax" size="12"/></td>
					</tr>
					<tr>
						<td>Email</td>
						<td><bean:message key="ManagePharmacy.txtfld.label.serviceLocationIdentifier" />
					</tr>
					<tr>
						<td><input type="text" id="pharmacyEmail" name="pharmacyEmail" size="32"/></td>
						<td colspan="2"><input type="text" id="pharmacyServiceLocationId" name="pharmacyServiceLocationId" size="32"/></td>
					</tr>
					<tr>
						<td colspan="3">Notes</td>
					</tr>
					<tr>
						<td colspan="3"><textarea id="pharmacyNotes" name="pharmacyNotes" cols="32" rows="5"></textarea></td>
					</tr>
					<tr>
						<td>
						
						<input type="button" value="Set Preferred Pharmacy" onclick="return setPreferredPharmacy();"/> &nbsp;
						<select id="preferredOrder" name="preferredOrder">
						<%
							for( int idx = 1; idx <= 10; ++idx ) {
						%>
								<option value="<%=idx%>"><%=idx%></option>
						<%							    
							}
						
						%>
						
						</select>	
							</td>
						<td style="text-align:right;padding-right:250px;" colspan="2">
						<input type="button" value="Reset" onclick="return resetForm();"/>&nbsp;
						<security:oscarSec roleName="<%=roleName$%>" objectName="_rx" rights="w" reverse="<%=false%>">
						<input type="button" value="Save" onclick="return savePharmacy();"/>&nbsp;&nbsp;
<!-- 							<input type="button" value="<bean:message key="SelectPharmacy.deleteLink" />" onclick="return deletePharmacy($('#preferedPharmacy>option:selected').val())"/> -->
						</security:oscarSec>
						</td>
					</tr>
					
				</table>
                </div>
                
				</td>
			</tr>

			<tr>
				<td>
				<%
                        String sBack="SearchDrug3.jsp";
                      %> <input type=button class="ControlPushButton"
					onclick="javascript:window.location.href='<%=sBack%>';"
					value="Back to Rx" /></td>
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
</form>
</body>

</html:html>
