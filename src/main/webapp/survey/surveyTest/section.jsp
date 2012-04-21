
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



<%@ include file="/survey/taglibs.jsp"%>
<td colspan="2">
<table border="1" width="100%">
	<tr>
		<td colspan="2"><c:if test="${section.bold eq 'true'}">
			<b>
		</c:if> <c:if test="${section.underline eq 'true'}">
			<u>
		</c:if> <c:if test="${section.italics eq 'true'}">
			<i>
		</c:if> <c:choose>
			<c:when test="${not empty section.color}">
				<span style="color:<c:out value="${section.color}"/>"><c:out
					value="${section.description}" /></span>
			</c:when>
			<c:otherwise>
				<c:out value="${section.description}" />
			</c:otherwise>
		</c:choose> <c:if test="${section.bold eq 'true'}">
			</b>
		</c:if> <c:if test="${section.underline eq 'true'}">
			</u>
		</c:if> <c:if test="${section.italics eq 'true'}">
			</i>
		</c:if></td>
	</tr>
	<c:forEach var="question" items="${section.questionArray}">
		<tr>
			<c:set var="question" value="${question}" scope="request" />
			<c:set var="sectionId" value="${section.id}" scope="request" />
			<jsp:include page="question.jsp" />
		</tr>
	</c:forEach>
</table>
</td>
