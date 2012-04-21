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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page
	import="oscar.oscarMDS.data.ProviderData, java.util.ArrayList, oscar.oscarLab.ForwardingRules, oscar.OscarProperties"%>

<%

ForwardingRules fr = new ForwardingRules();
String providerNo = request.getParameter("providerNo");
ArrayList frwdProviders = fr.getProviders(providerNo);
%>

<link rel="stylesheet" type="text/css" href="encounterStyles.css">
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Lab Report Forwarding Rules</title>

<script type="text/javascript" language=javascript>
            
            function removeProvider(remProviderNo, providerName){
                var answer = confirm ("Are you sure you would like to stop forwarding labs to "+providerName)
                if (answer){
                    document.RULES.operation.value="remove";
                    document.RULES.remProviderNum.value = remProviderNo;
                    document.RULES.submit();
                    return true;
                }else{
                    return false;
                }
                
            }
            
            function setActionClear(){
                var answer = confirm ("Are you sure you would like to clear the forwarding rules?")
                if (answer){
                    document.RULES.operation.value="clear";
                    return true;
                }else{
                    return false;
                }
            }
            
            function confirmUpdate(){
                <%
                OscarProperties props = OscarProperties.getInstance();
                String autoFileLabs = props.getProperty("AUTO_FILE_LABS");
                if (autoFileLabs != null && autoFileLabs.equalsIgnoreCase("yes")){%>
                    return confirm ("Are you sure you would like to update the forwarding rules?")
                <%}else{%>
                    if (document.RULES.providerNums.value == '' && document.RULES.status[1].checked && <%= (frwdProviders.size() == 0)%>){
                        alert("You must select a physician to forward your incoming labs to if you wish to automatically file them");
                        return false;
                    }else{
                        return confirm ("Are you sure you would like to update the forwarding rules?")
                    }
                <%}%>
            }
        </script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body>
<form method="post" name="RULES" action="ForwardingRules.do"><input
	type="hidden" name="providerNo" value="<%= providerNo %>"> <input
	type="hidden" name="operation" value="update"> <input
	type="hidden" name="remProviderNum" value="">
<table width="100%" height="100%" border="0">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRow" colspan="9" align="left">
		<table width="100%">
			<tr>
				<td align="left"><input type="button"
					value=" <bean:message key="global.btnClose"/> "
					onClick="window.close()"></td>
				<td align="right"><oscar:help keywords="inbox forwarding" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td valign="middle">
		<center>
		<table>
			<tr>
				<td colspan="2" valign="bottom" class="Header">Current
				Forwarding Rules</td>
			</tr>
			<tr>
				<%
                                    
                                    String status = "N";
                                    if (!fr.isSet(providerNo)){%>
				<td colspan="2" align="center"><font color="red">There
				are no forwarding rules set</font></td>
				<%}else{
                                        status = fr.getStatus(providerNo);
                                        %>
				<td class="Cell">Incoming lab status:</td>
				<td class="Cell"><%= status.equals("N") ? "New" : "Filed" %></td>
			</tr>
			<%if (frwdProviders != null && frwdProviders.size() > 0) {%>
			<tr>
				<td valign="top" class="Cell">Labs are currently forwarded to:
				</td>
				<td class="Cell">
				<%for (int i=0; i < frwdProviders.size(); i++){%> <%= (String) ((ArrayList) frwdProviders.get(i)).get(1) %>
				<%= (String) ((ArrayList) frwdProviders.get(i)).get(2) %> <a
					href="#"
					onclick="return removeProvider('<%= (String) ((ArrayList) frwdProviders.get(i)).get(0) %>', '<%= (String) ((ArrayList) frwdProviders.get(i)).get(1) %> <%= (String) ((ArrayList) frwdProviders.get(i)).get(2) %>')">REMOVE</a>
				<br />
				<%}%>
				</td>
			</tr>
			<%}else{%>
			<tr>
				<td class="Cell" colspan="2"><font color="red">The
				incoming labs are not being forwarded</font>
				<td>
			</tr>
			<%}%>
			<tr>
				<td colspan="2" class="Cell"><input type="submit"
					value=" Clear Forwarding Rules " onclick="return setActionClear()">
				</td>
				<%}%>
			</tr>
			<tr height="10"></tr>
			<tr>
				<td colspan="2" class="Header">Update Forwarding Rules</td>
			</tr>
			<tr>
				<td class="Cell">Set incoming report status:</td>
				<td class="Cell"><input type="radio" name="status" value="N"
					<%= status.equals("F") ? "" : "checked" %>><bean:message
					key="oscarMDS.search.formReportStatusNew" /> <input type="radio"
					name="status" value="F" <%= status.equals("F") ? "checked" : "" %>>Filed
				</td>
			</tr>
			<tr>
				<td valign="top" class="Cell">Forward incoming reports to the
				following physicians:<br />
				(Hold 'Ctrl' to select multiple physicians)</td>
				<td class="Cell"><select multiple name="providerNums" size="10">
					<optgroup
						label="&#160&#160Doctors&#160&#160&#160&#160&#160&#160&#160&#160">
						<% ArrayList providers = ProviderData.getProviderList();
                                                for (int i=0; i < providers.size(); i++) { 
                                                String prov_no = (String) ((ArrayList) providers.get(i)).get(0);
                                                if ( !providerNo.equals(prov_no) && !frwdProviders.contains(providers.get(i))){%>
						<option value="<%= prov_no %>"><%= (String) ((ArrayList) providers.get(i)).get(1) %>
						<%= (String) ((ArrayList) providers.get(i)).get(2) %></option>
						<% }
                                                } %>
					</optgroup>
				</select></td>
			</tr>

			<tr>
				<td colspan="2" class="Cell"><input type="submit"
					value=" Update Forwarding Rules " onclick="return confirmUpdate()">
				</td>
			</tr>
		</table>
		</center>
		</td>
	</tr>
</table>
</form>
</body>
</html>
