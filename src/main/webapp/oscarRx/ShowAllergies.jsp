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
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.myoscar.utils.MyOscarLoggedInInfo"%>
<%@page import="org.oscarehr.util.LocaleUtils"%>
<%@page import="org.oscarehr.phr.util.MyOscarUtils"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="oscar.oscarRx.pageUtil.AllergyHelperBean"%>
<%@page import="oscar.oscarRx.pageUtil.AllergyDisplay"%>
<%@page import="java.util.List"%>
<%@page import="oscar.OscarProperties"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
   String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
   boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_allergy" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_allergy");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%
	OscarProperties props = OscarProperties.getInstance();
   	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
%>

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

com.quatro.service.security.SecurityManager securityManager = new com.quatro.service.security.SecurityManager();
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="EditAllergies.title" /></title>
<link rel="stylesheet" type="text/css" href="styles.css">

<style type="text/css">
.view_menu{
font-style:normal;
font-size:12;
font-weight:normal;
padding-right:12px;
}

.view_selected{
font-style:normal;
font-size:12;
font-weight:normal;
padding-right:12px;

}

table.allergy_legend{
border:0;
padding-left:20px;
}

table.allergy_legend td{
font-size:8;
padding-right:6;
}

.at_border{
border-top: 1px solid black;
border-bottom: 1px solid black;
}


table.colour_codes{
width:8px;
height:10px;
border:1px solid #999999;
}


table.allergy_table td{
	border-collapse: collapse;
	border-bottom: 1px #8F8F8F solid thin;
	border-top: none;
	border-left: none;
	border-right: none;
	
	empty-cells: show;	
}

</style>

<!--[if IE]>
<style type="text/css">

table.allergy_legend td{
font-size:10;
padding-right:6;
}

</style>
<![endif]-->


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
    
    function moveAllergyDown(allergyId) {
    	window.location="showAllergy.do?method=reorder&direction=down&demographicNo=" + <%=bean.getDemographicNo()%> + "&allergyId="+allergyId;
    }
    
	function moveAllergyUp(allergyId) {
		window.location="showAllergy.do?method=reorder&direction=up&demographicNo=" + <%=bean.getDemographicNo()%> + "&allergyId="+allergyId;
    }
	function show_Search_Criteria(){
			var tbl_as = document.getElementById("advancedSearch");
			
			if (tbl_as.style.display == '') {
				tbl_as.style.display = 'none';
			}else{ 
				tbl_as.style.display = '';
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
				<div class="DivContentSectionHead"><bean:message
					key="EditAllergies.section1Title" /></div>
				</td>
			</tr>
			<tr>
				<td>
				<table>
					<tr>
						<td><b><bean:message key="SearchDrug.nameText" /></b> <jsp:getProperty name="patient" property="surname" />, <jsp:getProperty name="patient" property="firstName" /></td>
						<td>&nbsp;</td>
						<td><b>Age:</b> <jsp:getProperty name="patient" property="age" /></td>
					</tr>
				</table>
				</td>
			</tr>

			<tr>
				<td>
				<div class="DivContentSectionHead"><bean:message
					key="EditAllergies.section2Title" />
				| <span class="view_menu">View: 
				
				<%
					 					 
					 String demoNo=request.getParameter("demographicNo");
					 
					 if(demoNo==null) {
						 demoNo = (String)session.getAttribute("demographicNo");
					 }else{
						 session.setAttribute("demographicNo", demoNo);
					 }
					 
				     String strView=request.getParameter("view");
					
					 String[] navArray={"Active","All","Inactive"};	
				
					 int i=0;
					 for(i=0;i<navArray.length;i++)
					 { 
						
						if( (strView!=null && strView.equals(navArray[i]) || ( strView==null && i==0 ) )){

							out.print(" <span class='view_selected'>"+navArray[i]+"</span>");		
						
							
						}else{
							out.print("<span class='view_menu'><a href='showAllergy.do?demographicNo="+demoNo+"&view="+navArray[i]+"'>");
								out.print(navArray[i]);
							out.print("</a></span>");	
						}
					 }
					 //1 mild 2 moderate 3 severe 4 unknown
					 
					 String[] ColourCodesArray=new String[5];
					 ColourCodesArray[1]="#F5F5F5"; // Mild Was set to yellow (#FFFF33) SJHH requested not to flag mild
					 ColourCodesArray[2]="#FF6600"; // Moderate
					 ColourCodesArray[3]="#CC0000"; // Severe
					 ColourCodesArray[4]="#E0E0E0"; // unknown 
					 
					
					 String allergy_colour_codes = "<table class='allergy_legend' cellspacing='0'><tr><td><b>Legend:</b></td> <td > <table class='colour_codes' bgcolor='"+ColourCodesArray[1]+"'><td> </td></table></td> <td >Mild</td> <td > <table class='colour_codes' bgcolor='"+ColourCodesArray[2]+"'><td> </td></table></td> <td >Moderate</td><td > <table class='colour_codes' bgcolor='"+ColourCodesArray[3]+"'><td> </td></table></td> <td >Severe</td> </tr></table>";
				%>
				</span>
				<%
                  	  if (MyOscarUtils.isMyOscarEnabled((String) session.getAttribute("user")))
                  	  {
                  		  MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(session);
                  		  	boolean enabledMyOscarButton=MyOscarUtils.isMyOscarSendButtonEnabled(myOscarLoggedInInfo, Integer.valueOf(demoNo));
							if (enabledMyOscarButton) 
							{
								%>
									| <a href="send_allergies_to_myoscar_action.jsp?demographicId=<%=demoNo%>"><%=LocaleUtils.getMessage(request, "SendToPHR")%></a>
								<%
							}
							else
							{
								%>
									| <span style="color:grey;text-decoration:underline"><%=LocaleUtils.getMessage(request, "SendToPHR")%></span>
								<%
							}
                  	  }
             	%> 
				</div>
				</td>
			</tr>
			<tr>
				<td>
				<table border="0">
					<tr>
						<td width="100%">
						<%=allergy_colour_codes%>
						<div class="Step1Text" style="width: 830px;">
						                                                                          <!-- frame="below" and rules="all" 
						                                                                          are here so the browser displays 
						                                                                          cells that are empty so the bottom 
						                                                                          border appears -->                   
						<table width="100%" cellpadding="3" cellspacing="0" class="allergy_table" frame="below" rules="all">
							<thead>
								<td>&nbsp;</td>
								<td><b>Status</b></td>
								<td><b>Entry Date</b></td>
								<td><b>Description</b></td>
								<td><b>Allergy Type</b></td>
								<td><b>Severity</b></td>
								<td><b>Onset of Reaction</b></td>
								<td><b>Reaction</b></td>
								<td><b>Start Date</b></td>
								<td><img src="../images/notes.gif" border="0" width="10" height="12" alt="Annotation"></td>
								<td><b>Action</b></td>
								<td><b>&nbsp;</b></td>
							</thead>
							<%
							String strArchived;
							int intArchived;
							String labelStatus;
							String labelAction;
							String actionPath;
							String trColour;
							String labelConfirmAction;
							String strSOR;
							int intSOR;

								Integer demographicId=bean.getDemographicNo();
								List<AllergyDisplay> displayAllergies=AllergyHelperBean.getAllergiesToDisplay(loggedInInfo, demographicId, request.getLocale());
								for (AllergyDisplay displayAllergy : displayAllergies)
								{

boolean filterOut=false;
								
								strArchived=displayAllergy.getArchived();
								intArchived = Integer.parseInt(strArchived);
								
								if(bean.getView().equals("Active") && intArchived == 1) {
									filterOut=true;
								}
								
								if(bean.getView().equals("Inactive") && intArchived == 0) {
									filterOut=true;
								}
								
								strSOR=displayAllergy.getSeverityCode();
							    intSOR = Integer.parseInt(strSOR);
							    String sevColour;
							    								
									if(intArchived==1){
										//if allergy is set as archived
										labelStatus="Inactive";
										labelAction="Activate";
										labelConfirmAction="Active";
										actionPath="activate";
										trColour="#C0C0C0";
										
										sevColour="#C0C0C0"; //clearing severity bgcolor
									}else{
										labelStatus="Active";
										labelAction="Inactivate";
										labelConfirmAction="Inactive";
										actionPath="delete";

										trColour="#E0E0E0";
										sevColour=ColourCodesArray[intSOR];
									}

									if(!filterOut) {
									%>
										<tr bgcolor="<%=trColour%>">
											<td>												
												<%if(!(displayAllergies.get(displayAllergies.size()-1) == displayAllergy)) {%>
												<img border="0" align="bottom" src="<%=request.getContextPath()%>/images/icon_down_sort_arrow.png" onclick="moveAllergyDown(<%=displayAllergy.getId() %>);return false;"/>
												<% } %>
												<%if(!(displayAllergies.get(0) == displayAllergy)) {%>
												<img border="0" align="top" src="<%=request.getContextPath()%>/images/icon_up_sort_arrow.png" onclick="moveAllergyUp(<%=displayAllergy.getId() %>);return false;"/>
												<%} %>
											</td>
											<td><%=labelStatus%></td>
											<td><%=StringEscapeUtils.escapeHtml(displayAllergy.getEntryDate())%></td>
											<td><%=StringEscapeUtils.escapeHtml(displayAllergy.getDescription())%></td>
											<td><%=StringEscapeUtils.escapeHtml(displayAllergy.getTypeDesc())%></td>
											<td bgcolor="<%=sevColour%>"><%=StringEscapeUtils.escapeHtml(displayAllergy.getSeverityDesc())%></td>
											<td><%=StringEscapeUtils.escapeHtml(displayAllergy.getOnSetDesc())%></td>
											<td><%=StringEscapeUtils.escapeHtml(displayAllergy.getReaction())%></td>
											<td><%=StringEscapeUtils.escapeHtml(displayAllergy.getStartDate())%></td>
											<td>
												<%
													// annotations only avaiable for local allergies
													if (displayAllergy.getRemoteFacilityId()==null)
													{
														%>
															<a href="#" title="Annotation" onclick="window.open('../annotation/annotation.jsp?display=<%=annotation_display%>&table_id=<%=displayAllergy.getId()%>&demo=<jsp:getProperty name="patient" property="demographicNo"/>','anwin','width=400,height=500');"><img src="../images/notes.gif" border="0"></a>
														<%
													}
												%>
											</td>
										<td>
									<%									
									if (displayAllergy.getRemoteFacilityId()==null && securityManager.hasDeleteAccess("_allergies",roleName2$)) {
									%>
									<a href="deleteAllergy.do?ID=<%= String.valueOf(displayAllergy.getId()) %>&demographicNo=<%=demoNo %>&action=<%=actionPath %>" onClick="return confirm('Are you sure you want to set the allergy <%=displayAllergy.getDescription() %> to <%=labelConfirmAction%>?');"><%=labelAction%></a>
									<% } %>
									</td>
										</tr>
									<%
								}//filterout
								}
							%>
						</table>
						</div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<%if(securityManager.hasWriteAccess("_allergies",roleName2$)) {%>
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
						</tr>
						<tr>
                        	<td><html:text property="searchString" size="50" styleId="searchString" maxlength="50" /></td>
						</tr>
						<tr>
							<td>
								<html:submit property="submit" value="Search" styleClass="ControlPushButton" />                            
								<input type=button class="ControlPushButton" onclick="javascript:document.forms.RxSearchAllergyForm.searchString.value='';document.forms.RxSearchAllergyForm.searchString.focus();"	value="Reset" />
								<input type=button class="ControlPushButton" onclick="javascript:addCustomAllergy();" value="Custom Allergy" />
<% 
                           String shPref;
                           String showClose;
                           if (props.getProperty("ALLERGIES_SIMPLE_SEARCH", "").equals("1")) { %>
                           		<a href="#" onclick="show_Search_Criteria();">Advanced Search</a>
                           <%
                            
                            shPref="display:none";	 
                            showClose="<a href='#' onclick='show_Search_Criteria();'>Close [x]</a>";
                            }else{
                            	
                            shPref="";	
                            showClose="";
                            }
                           %>                                                            
                                                             
                           </td>
						</tr>
					</table>
                      &nbsp;
                      <table bgcolor="#F5F5F5" cellpadding="3" id="advancedSearch" style="<%=shPref%>">
						<tr>
							<td colspan="3">Search the following categories: <i>(Listed
							general to specific)</i></td>
							<td align="right"><%=showClose%></td>
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
                                    
                                    function initialTypeSelect(){
                                        var frm = document.forms.RxSearchAllergyForm;

                                        frm.type1.checked = true;
                                        frm.type2.checked = true;
                                        frm.type3.checked = false;
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

                                    initialTypeSelect();
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
				
						if("yes".equalsIgnoreCase(OscarProperties.getInstance().getProperty("RX3"))) {
							sBack="SearchDrug3.jsp";
						}
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
	<% } %>
</table>
</body>
</html:html>
