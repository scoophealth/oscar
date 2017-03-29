<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@ include file="/taglibs.jsp"%>

<%@ include file="/common/messages.jsp"%>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/validation.js">
</script>
<script type="text/javascript">
	function validateForm()
	{
		if (bCancel == true)
			return confirm("Do you really want to Cancel?");
		var isOk = false;
		isOk = validateRequiredField('functionalCentreId', 'functionalCentreId', 32);
		if (isOk) isOk = validateRequiredField('functionalCentreName', 'functionalCentreName', 70);
		return isOk;
	}
</script>
<!-- don't close in 1 statement, will break IE7 -->


<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Functional Centre">Edit Functional Centre</th>
	</tr>
</table>
</div>

<html:form action="/PMmodule/FunctionalCentreManager.do"
	onsubmit="return validateForm();">
	<input type="hidden" name="method" value="save" />
	<table width="100%" border="1" cellspacing="2" cellpadding="3">
		<tr class="b">
			<td width="20%">Functional Centre Id:</td>
			<td><html:text property="functionalCentre.accountId" size="32" maxlength="32"
				styleId="functionalCentreId" /></td>

		</tr>
		<tr class="b">
			<td width="20%">Functional Centre Name:</td>
			<td><html:text property="functionalCentre.description" size="70" maxlength="70"
				styleId="functionalCentreName" /></td>
		</tr>		
		
		<tr class="b">
			<td width="20%">Enable CBI Form:</td>
			<td><html:checkbox property="functionalCentre.enableCbiForm" /></td>
		</tr>
		
		<tr>
			<td colspan="2"><html:submit property="submit.save" onclick="bCancel=false;">Save</html:submit>
			<html:cancel>Cancel</html:cancel></td>
		</tr>
	</table>
</html:form>
<div>
<p><a
	href="<html:rewrite action="/PMmodule/FunctionalCentreManager.do"/>?method=list" >Return
to Functional Centre List</a></p>
</div>
