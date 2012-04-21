
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
<div class="tabs" id="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Templates">Vacancies</th>
		</tr>
	</table>
</div>
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td width="30%" class="beright">Requirement Template:</td>
		<td><select name="program.associatedProgram">
				<option selected="selected" value=" ">None Selected</option>
		</select></td>
	</tr>
</table>
........
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td width="30%" class="beright">Match Closed:</td>
		<td><input type="checkbox" value="on"
			name="program.matchClosed"></td>
	</tr>
	<tr class="b">
		<td class="beright">Date Closed:</td>
		<td><select name="program.dateClosed">
				<option selected="selected" value=" ">04/05/2010</option>
		</select></td>
	</tr>
	<tr class="b">
		<td class="beright">Reason Closed:</td>
		<td><select name="program.reasonClosed">
				<option selected="selected" value=" ">None Selected</option>
		</select></td>
	</tr>
</table>
