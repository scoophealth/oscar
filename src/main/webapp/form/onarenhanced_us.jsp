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
<tr id="us_<%=n%>" >
	<td>
		<a href="javascript:void(0)" onclick="deleteUltraSound('<%=n%>'); return false;">[x]</a>
	</td>

	<td nowrap><input type="text" name="ar2_uDate<%=n %>"
		id="ar2_uDate<%=n %>" class="spe ar2uDate" onDblClick="calToday(this)" size="10"
		maxlength="10">
	<img src="../images/cal.gif" id="ar2_uDate<%=n %>_cal"></td>
	<td><input type="text" id="ar2_uGA<%=n %>" name="ar2_uGA<%=n %>" class="spe"
		onDblClick="calcWeek(this)" size="5" maxlength="10"
		></td>
	<td><input type="text" name="ar2_uResults<%=n %>" size="50"
		maxlength="50"
		></td>							
</tr>
