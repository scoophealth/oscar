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
<%@ include file="/ticklerPlus/header.jsp"%>

<tr>
	<td class="searchTitle" colspan="4">Custom Filters</td>
</tr>
</table>

<br />
<table width="30%" border="0" cellpadding="0" cellspacing="1"
	bgcolor="#C0C0C0">
	<html:form action="/CustomFilter">
		<input type="hidden" name="method" value="custom_filter" />
		<tr class="title">
			<th width="10%"></th>
			<th width="10%"></th>
			<th width="80%">Name</th>
			<th>Shortcut</th>
		</tr>
		<%int index=0; 
	  String bgcolor;
	%>
		<c:forEach var="filter" items="${custom_filters}">
			<%
			if(index++%2!=0) {
				bgcolor="white";
			} else {
				bgcolor="#EEEEFF";
			}
		%>
			<tr bgcolor="<%=bgcolor %>" align="center">
				<td valign="middle"><!--  <input type="checkbox" name="checkbox" value="<c:out value="${filter.name}"/>"/>
				--> <input type="checkbox" name="checkbox"
					value="<c:out value="${filter.id}"/>" /></td>
				<td valign="middle"><!--  <a href="../CustomFilter.do?method=edit&name=<c:out value="${filter.name}"/>"><img border="0" src="images/edit.jpg"/></a>
				--> <a
					href="../CustomFilter.do?method=edit&id=<c:out value="${filter.id}"/>"><img
					border="0" src="images/edit.jpg" /></a></td>
				<td><c:out value="${filter.name}" /></td>
				<td><html-el:link
					action="/CustomFilter?method=changeShortCutStatus&id=${filter.id}">
					<c:choose>
						<c:when test="${filter.shortcut == true}">
                                    Remove
                                </c:when>
						<c:otherwise>
                                    Add
                                </c:otherwise>
					</c:choose>
				</html-el:link></td>
			</tr>
		</c:forEach>
</table>

<table>
	<!-- 
		<tr>
			<td colspan="2"><a href="#" onclick="CheckAll(document.customFilterForm);">Check All</a>&nbsp;<a href="#" onclick="ClearAll(document.customFilterForm);">Clear All</a></td>
		</tr>
	-->
	<tr>
		<td><input type="button" value="New"
			onclick="location.href='<html:rewrite action="/CustomFilter"/>?method=edit'" /></td>
		<td><input type="button" value="Delete"
			onclick="this.form.method.value='delete';this.form.submit();" /></td>
	</tr>
	</html:form>
</table>

<br />
<table width="100%">
	<tr>
		<td><html:link action="/Tickler.do?method=filter">Back to Ticklers</html:link></td>
	</tr>
</table>
</body>
</html>
