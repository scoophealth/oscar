<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.model.ProviderPreference"%>

<%@ page
	import="java.util.*,oscar.util.*, org.apache.struts.action.*, oscar.oscarWaitingList.bean.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<link rel="stylesheet" type="text/css"
	href="../oscarEncounter/encounterStyles.css">
<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Change Waiting List Name</title>
</head>
<script language="JavaScript">

function resetFields(actionType){

	if(actionType == "create"){
		document.forms[0].wlChangedName.value="";
	}else if(actionType == "change"){
		document.forms[0].wlNewName.value="";
	}else if(actionType == "remove"){
		document.forms[0].wlChangedName.value="";
		document.forms[0].wlNewName.value="";
	}
}

</script>
<%
	ProviderPreference providerPreference=(ProviderPreference)session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE);
	
	String groupNo = "";
	if(providerPreference.getMyGroupNo() != null){
		groupNo = providerPreference.getMyGroupNo();
	}
	WLWaitingListNameBeanHandler wlNameHd = new WLWaitingListNameBeanHandler(groupNo, (String)session.getAttribute("user"));

   	List allWaitingListName = wlNameHd.getWaitingListNameList();
%>
<body bgproperties="fixed" " topmargin="0" leftmargin="0"
	rightmargin="0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr border="#CCCCFF">
		<th><font face="Helvetica">Create/Edit Waiting List Name</font></th>
	</tr>
</table>

<%
	String message = "";
	if(request.getAttribute("message") != null){
		message = (String)request.getAttribute("message");
	}
%>

<html:form
	action="/oscarWaitingList/WLEditWaitingListNameAction.do?edit=Y">
	<html:hidden property="actionChosen" />
	<table border="0" width="100%">
		<tr bgcolor="#EEEEFF">
			<td width="25%" align="right" class="data4">Please select a
			Waiting List name to be changed:</td>
			<td align="right"><html:select property="selectedWL">
				<option value=""></option>
				<%
                             for(int i=0; i<allWaitingListName.size(); i++){
                                 WLWaitingListNameBean wLBean = (WLWaitingListNameBean) allWaitingListName.get(i);
                                 String id = wLBean.getId();
                                 String name = wLBean.getWaitingListName();                                       
                                 String selected = id.compareTo((String) request.getAttribute("WLId")==null?"0":(String) request.getAttribute("WLId"))==0?"SELECTED":"";                                        
                         %>
				<option value="<%=id%>" <%=selected%>><%=name%></option>
				<%}%>
			</html:select></td>
			<td align="left"><html:text property="wlChangedName" size="45"
				maxlength="255" /></td>
			<td width="30%" align="left"><input type="submit"
				value="Change Name"
				onclick="resetFields('change');document.forms[0].actionChosen.value='change'">
			</td>
		</tr>
		<tr bgcolor="#EEEEFF">
			<td colspan="2" align="right">
			<div align="right"><span class="data4">Create New Name:</span></div>
			</td>

			<td align="left"><html:text property="wlNewName" size="45"
				maxlength="255" /></td>
			<td width="30%" align="left"><input type="submit"
				value="Create Name"
				onclick="resetFields('create');document.forms[0].actionChosen.value='create'">
			</td>
		</tr>
		<tr bgcolor="#EEEEFF">
			<td width="25%" align="right" class="data4">Please select a
			Waiting List name to be removed:</td>
			<td align="right"><html:select property="selectedWL2">
				<option value=""></option>
				<%
                             for(int i=0; i<allWaitingListName.size(); i++){
                                 WLWaitingListNameBean wLBean2 = (WLWaitingListNameBean) allWaitingListName.get(i);
                                 String id = wLBean2.getId();
                                 String name = wLBean2.getWaitingListName();                                       
                                 String selected = id.compareTo((String) request.getAttribute("WLId")==null?"0":(String) request.getAttribute("WLId"))==0?"SELECTED":"";                                        
                         %>
				<option value="<%=id%>" <%=selected%>><%=name%></option>
				<%}%>
			</html:select></td>
			<td colspan="2" align="left"><input type="submit"
				value="Remove Name"
				onclick="resetFields('remove');document.forms[0].actionChosen.value='remove'">
			</td>
		</tr>

	</table>

	<table width="100%">
		<tr bgcolor="#CCCCFF">
			<td align="center"><input type="reset" value='Close'
				onClick="window.close();"></td>
		</tr>
	</table>

</html:form>
<table width="100%">
	<tr>
		<td align="center"><span
			style="color: red; font-weight: bold; font-size: 14;"> <%-- 								
				<html:messages id="msg">
					<bean:write name="msg"/><br/>
				</html:messages>
--%> <%
	if(message != null  &&  !message.equals("")){
%> <bean:message key="<%=message%>" /> <%
	}
%> </span>
	</tr>
</table>


</body>
</html:html>
