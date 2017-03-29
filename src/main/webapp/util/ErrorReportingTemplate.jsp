<%--

    Copyright (c) 2013-2015. Leverage Analytics. All Rights Reserved.
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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
	<c:when test="${pageContext.errorData != null}">
		<c:choose>
			<c:when test="${pageContext.errorData.throwable != null}">
				<c:choose>
					<c:when test="${pageContext.errorData.throwable.cause != null}">
						<h1><bean:message key="error.description" /></h1>
						<h2><bean:message key="error.msgException" /></h2>
						<font color=red> <c:out value="${pageContext.errorData.throwable.cause.message} }"/> </font>
					</c:when>
					<c:otherwise>
						<h1><bean:message key="error.description" /></h1>
						<h2><bean:message key="error.msgException" /></h2>
						<font color=red> <c:out value="${pageContext.errorData.throwable}"/> </font>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<h1><bean:message key="error.description" /></h1>
				<h2><bean:message key="error.msgException" /></h2>
				<font color=red> <c:out value="${pageContext.errorData}"/> </font>
			</c:otherwise>
		</c:choose>
	</c:when>
</c:choose>