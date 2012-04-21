
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

<title>Caisi Editor</title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<button onclick="location.href='CaisiEditor.do?method=edit'">Add</button>
<table border="1" cellpadding="1" cellspacing="1" bgcolor="#C0C0C0">
	<thead>
		<tr class="title">
			<th>&nbsp;&nbsp;&nbsp;&nbsp;</th>
			<th>Category &nbsp;&nbsp;</th>
			<th>Label &nbsp;&nbsp;</th>
			<th>Type &nbsp; &nbsp;</th>
			<th>Label Value &nbsp;&nbsp;</th>
			<th>Label Code &nbsp;&nbsp;</th>
			<th>Horizontal &nbsp;&nbsp;</th>
			<th>Active &nbsp;&nbsp;</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="caisiEditor" items="${caisiEditors}"
			varStatus="status">
			<c:choose>
				<c:when test="${status.count % 2 == 0}">
					<tr class="even">
				</c:when>
				<c:otherwise>
					<tr class="odd">
				</c:otherwise>
			</c:choose>

			<td><a
				href="CaisiEditor.do?method=edit&amp;id=<c:out value="${caisiEditor.id}"/>">Edit</a></td>

			<td><c:out value="${caisiEditor.category}" /></a></td>
			<td><c:out value="${caisiEditor.label}" /></td>
			<td><c:out value="${caisiEditor.type}" /></td>
			<td><c:out value="${caisiEditor.labelValue}" /></td>
			<td><c:out value="${caisiEditor.labelCode}" /></td>
			<td><c:out value="${caisiEditor.horizontal}" /></td>
			<td><c:out value="${caisiEditor.isActive}" /></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
