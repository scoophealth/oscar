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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<div class="page-header">
        <h1><a href="#">Oscar Sharing Center</a></h1>
      </div>
      <nav class="navbar navbar-default" role="navigation">
                <div class="navbar-collapse">
                    <ul class="nav navbar-nav">
                      <li><a href="${ctx}/administration">Administration</a></li>
                      <li><a href="${ctx}/sharingcenter/affinitydomain/manage.jsp">Manage Affinity Domains</a></li>
                      <li><a href="${ctx}/sharingcenter/security/infrastructure.jsp">Security</a></li>
                      <li><a href="${ctx}/sharingcenter/affinitydomain/clinic.jsp">Clinic Info</a></li>
                    </ul>
                </div>
            </nav>