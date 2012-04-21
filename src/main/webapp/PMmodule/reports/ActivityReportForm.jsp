
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

<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/checkDate.js"></script>
<script type="text/javascript">
	function validateDates() {
		var sdate = document.getElementById('sdate').value;
		var edate = document.getElementById('edate').value;
		if(checkAndValidateDate(sdate)) {
			if(checkAndValidateDate(edate)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
</script>
<title>Program Activity Report Generator</title>
</head>

<body>
<Br />
<br />
<html:form action="/PMmodule/Reports/ProgramActivityReport" onsubmit="return validateDates();">
	<input type="hidden" name="method" value="generate" />
	<div id="projecthome" class="app"><br />
	<div class="h4">
	<h5>Enter Report Criteria</h5>
	</div>
	<div class="axial">
	<table border="0" cellspacing="2" cellpadding="3">
		<tr>
			<th>Start Date</th>
			<td><html:text styleId="sdate" property="form.startDate" size="15" />
			(yyyy-mm-dd)</td>
		</tr>
		<tr>
			<th>End Date</th>
			<td><html:text styleId="edate" property="form.endDate" size="15" />
			(yyyy-mm-dd)</td>
		</tr>
		<!-- 
			<tr>
				<th>Program</th>
				<td>
					<html:select property="form.programId">
						<html:options collection="programs"  property="id" labelProperty="name"/>
					</html:select>
				</td>
			</tr>
			-->
		<tr>
			<td align="center" colspan="2"><html:submit	value="Generate Report"/></td>
		</tr>
	</table>
	</div>
	</div>
</html:form>

</body>

</html:html>
