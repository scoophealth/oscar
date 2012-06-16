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

<tr align="center"  id="obxhx_<%=n%>">
	<td><a dl="delete_link" href="javascript:void(0)" onclick="deleteObxHx('<%=n%>'); return false;">[x]</a>&nbsp;<%=n %></td>
	<td>
		<input type="text" name="pg1_year<%=n %>" size="5" maxlength="4" style="width: 90%"  />
	</td>
	<td>
		<input type="text" name="pg1_sex<%=n %>" size="2"	maxlength="1" style="width: 50%"  />
	</td>
	<td>
		<input type="text" name="pg1_oh_gest<%=n %>" size="3" maxlength="5" style="width: 80%"  />
	</td>
	<td>
		<input type="text" name="pg1_weight<%=n %>" size="5" maxlength="6" style="width: 80%"  />
	</td>
	<td>
		<input type="text" name="pg1_length<%=n %>" size="5" maxlength="6" style="width: 80%"  />
	</td>
	<td>
		<input type="text" name="pg1_place<%=n %>" size="8" maxlength="20" style="width: 80%"  />
	</td>
	<td>
		<input type="checkbox" name="pg1_svb<%=n %>" /> 
		<input type="checkbox" name="pg1_cs<%=n %>" /> 
		<input type="checkbox" name="pg1_ass<%=n %>" />
	</td>
	<td align="left">
		<input type="text" name="pg1_oh_comments<%=n %>" size="20" maxlength="80" style="width: 100%"  />
	</td>
 </tr>