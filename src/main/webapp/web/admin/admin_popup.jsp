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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<script>
function newWindow(url) {
    newwindow=window.open(url,'administration','height=700,width=1000,scrollbars=1');
    if (window.focus) {newwindow.focus()}
    return false;
}

newWindow('<%=request.getContextPath()%>/administration/');
</script>
<p class="info"><a href="javascript:void()" onClick="newWindow('<%=request.getContextPath()%>/administration/');return false"><bean:message key="admin.panel" bundle="ui"/></a> <bean:message key="admin.popupMessage" bundle="ui"/></p>

<p><a href="#/dashboard"><bean:message key="global.goToDashboard" bundle="ui"/></a></p>


