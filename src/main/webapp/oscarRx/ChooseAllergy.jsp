<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="java.util.*"%>
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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="ChooseAllergy.title" /></title>
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
            alert(name);
            window.location="addReaction.do?ID=0&type=0&name="+name;
        }
    }
    
</script>
</head>
<body topmargin="0" leftmargin="0" vlink="#0000FF">

<table border="0" cellpadding="0" cellspacing="0"
	style="border-collapse: collapse" bordercolor="#111111" width="100%"
	id="AutoNumber1" height="100%">
	<%@ include file="TopLinks.jsp"%><!-- Row One included here-->
	<tr>
		<%@ include file="SideLinksNoEditFavorites.jsp"%><!-- <td></td>Side Bar File --->
		<td width="100%" style="border-left: 2px solid #A9A9A9;" height="100%"
			valign="top">
		<table cellpadding="0" cellspacing="2"
			style="border-collapse: collapse" bordercolor="#111111" width="100%"
			height="100%">
			<tr>
				<td width="0%" valign="top">
				<div class="DivCCBreadCrumbs"><a href="SearchDrug.jsp"> <bean:message
					key="SearchDrug.title" /></a>&nbsp;&gt;&nbsp; <a
					href="ShowAllergies.jsp"> <bean:message
					key="EditAllergies.title" /></a>&nbsp;&gt;&nbsp; <b><bean:message
					key="ChooseAllergy.title" /></b></div>
				</td>
			</tr>
			<!----Start new rows here-->
			<tr>
				<td>
				<div class="DivContentTitle"><bean:message
					key="ChooseAllergy.title" /></div>
				</td>
			</tr>
			<tr>
				<td>
				<div class="DivContentSectionHead"><bean:message
					key="ChooseAllergy.section1Title" /></div>
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
								styleClass="ControlPushButton" /></td>
							<td><input type=button class="ControlPushButton"
								onclick="javascript:document.forms.RxSearchAllergyForm.searchString.value='';document.forms.RxSearchAllergyForm.searchString.focus();"
								value="Reset" />
                                                             <input type=button class="ControlPushButton" onclick="javascript:addCustomAllergy();" value="Custom Allergy" />
                                                            
                                                        </td>
						</tr>
					</table>
                      &nbsp;
                      <table bgcolor="#F5F5F5" cellspacing=2
						cellpadding=2>
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
                                    function typeSelect()
                                    {
                                        var frm = document.forms.RxSearchAllergyForm;

                                        frm.type1.checked = true;
                                        frm.type2.checked = true;
                                        frm.type3.checked = true;
                                        frm.type4.checked = true;
                                        
                                    }
                                    function typeClear()
                                    {
                                        var frm = document.forms.RxSearchAllergyForm;

                                        frm.type1.checked = false;
                                        frm.type2.checked = false;
                                        frm.type3.checked = false;
                                        frm.type4.checked = false;
                                        
                                    }
                                </script> <input type=button
								class="ControlPushButton" onclick="javascript:typeSelect();"
								value="Select All" /> <input type=button
								class="ControlPushButton" onclick="javascript:typeClear();"
								value="Clear All" /></td>
						</tr>
					</table>
				</html:form></td>
			</tr>
			<tr>
				<td>
				<div class="DivContentSectionHead"><bean:message
					key="ChooseAllergy.section2Title" /></div>
				</td>
			</tr>
			<tr>
				<td>
				<%
                        int newSection = 0;
                        Hashtable drugClassHash = (Hashtable) request.getAttribute("drugClasses");                            
                        %>
				<div class="LeftMargin"><logic:notPresent name="allergies">
                                    Search returned no results. Revise your search and try again.
                            </logic:notPresent> <logic:present name="allergies">
					<logic:iterate id="allergy"
						type="oscar.oscarRx.data.RxAllergyData.Allergy" name="allergies">
						<%
                                    if (allergy != null){
                                        if (newSection != allergy.getTYPECODE()) {
                                        %>
						<div class="DivContentSectionHead"><%=allergy.getTypeDesc() %></div>
						<%
                                        newSection = allergy.getTYPECODE();
                                        }
                                       
                                            /*|  8 | anatomical class
                                             *|  9 | chemical class
                                             *| 10 | therapeutic class
                                             *| 11 | generic
                                             *| 12 | composite generic
                                             *| 13 | branded product
                                             *| 14 | ingredient
                                             */

                                            String s = "";

                                            switch(allergy.getTYPECODE()) {
                                                case 11:  //Generic Name
                                                case 12:
                                    %>

						<a
							href="addReaction.do?ID=<%= allergy.getPickID() %>&name=<%=java.net.URLEncoder.encode(allergy.getDESCRIPTION())%>&type=<%=allergy.getTYPECODE()%>">
						<bean:write name="allergy" property="DESCRIPTION" /> </a>
						<%                                                
                                                    break;                                            
                                                case 13://Brand Name
                                                    %>
						<a
							href="addReaction.do?ID=<%= allergy.getPickID() %>&name=<%=java.net.URLEncoder.encode(allergy.getDESCRIPTION())%>&type=<%=allergy.getTYPECODE()%>">
						<bean:write name="allergy" property="DESCRIPTION" /> </a>
						<%
                                                    java.util.Vector vect = (Vector) drugClassHash.get(""+allergy.getPickID());
                                                    if (vect != null){
                                                        for (int k = 0; k < vect.size() ; k++){
                                                        String[] drugClassPair = (String[]) vect.get(k);
                                                    %>
                                                        &nbsp;&nbsp;&nbsp;
                                                        <a
							style="color: orange"
							href="addReaction.do?ID=<%=drugClassPair[0] %>&name=<%=java.net.URLEncoder.encode(drugClassPair[1])%>&type=10"><%=drugClassPair[1] %>
						</a>
						<%
                                                        }
                                                    }       

                                                    break;
                                                case 8://Drug Class
                                                case 10:
                                                    %>
						<a
							href="addReaction.do?ID=<%= allergy.getPickID() %>&name=<%=java.net.URLEncoder.encode(allergy.getDESCRIPTION())%>&type=<%=allergy.getTYPECODE()%>">
						<bean:write name="allergy" property="DESCRIPTION" /> </a>
						<%
                                                    break;                                                                                           
                                                case 14://Ingredient
                                                    %>
						<a
							href="addReaction.do?ID=<%= allergy.getPickID() %>&name=<%=java.net.URLEncoder.encode(allergy.getDESCRIPTION())%>&type=<%=allergy.getTYPECODE()%>">
						<bean:write name="allergy" property="DESCRIPTION" /> </a>
						<%                                                
                                                    break;
                                                default:

                                            }
                                    } else {
                                        %>
						<div class="LeftMargin">Search returned no results. Revise
						your search and try again.</div>
						<%
                                    }

                                    %>

						<br>
					</logic:iterate>
				</logic:present></div>

				<br>
				<br>
				<%
                        String sBack="ShowAllergies.jsp";
                      %> <input type=button class="ControlPushButton"
					onclick="javascript:window.location.href='<%=sBack%>';"
					value="Back to View Allergies" /></td>
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