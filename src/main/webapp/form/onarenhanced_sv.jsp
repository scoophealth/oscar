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
<%
	int n = Integer.parseInt(request.getParameter("n"));
%>

<tr align="center" id="sv_<%=n%>">
	<td>
		<a href="javascript:void(0)" onclick="deleteSubsequentVisit('<%=n%>'); return false;">[x]</a>
	</td>
	<td nowrap><input type="text" name="pg2_date<%=n%>" id="pg2_date<%=n%>"
		class="spe" size="9" maxlength="10" onDblClick="calToday(this)"></td>
	<td><input type="text" name="pg2_gest<%=n%>" class="spe" size="6"
		maxlength="6" style="width: 90%"
		onDblClick="calcWeek(this)"></td>
	<td><input type="text" name="pg2_wt<%=n%>" size="6" maxlength="6"
		onDblClick="wtEnglish2Metric(this);" style="width: 90%"
		></td>
	<td><input type="text" name="pg2_BP<%=n%>" size="6" maxlength="8"
		style="width: 100%"
		></td>
	<td width="4%" colspan="2"><input type="text" name="pg2_urinePr<%=n%>" size="2"
		maxlength="3" style="width: 90%"
		></td>
		
		<!-- 
	<td width="4%"><input type="text" name="pg2_urineGl<=n>" size="2"
		maxlength="3" style="width: 90%"
		></td>
		-->
	<td><input type="text" name="pg2_ht<%=n%>" size="6" maxlength="6"
		onDblClick="htEnglish2Metric(this);" style="width: 90%"
		></td>
	<td><input type="text" name="pg2_presn<%=n%>" size="6" maxlength="6"
		style="width: 90%"
		></td>
	<td><input type="text" name="pg2_FHR<%=n%>" size="6" maxlength="6"
		style="width: 90%"
		></td>
	<td><input type="text" name="pg2_comments<%=n%>" size="20"
		maxlength="70" style="width: 100%"
		></td>
</tr>