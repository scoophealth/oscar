<!--
/*
*
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License.
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version. *
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
*
* <OSCAR TEAM>
*
* This software was written for
* Centre for Research on Inner City Health, St. Michael's Hospital,
* Toronto, Ontario, Canada
*/
-->

<%@ include file="/taglibs.jsp"%>

<%@ page import="org.oscarehr.PMmodule.web.formbean.*"%>
<html:form action="/PMmodule/AgencyManager">
    <div class="tabs" id="tabs">
        <table cellpadding="3" cellspacing="0" border="0">
            <tr>
                <th title="Programs">General Information</th>
            </tr>
        </table>
    </div>
    <table width="100%" border="1" cellspacing="2" cellpadding="3">
        <tr class="b">
            <td width="20%">Name:</td>
            <td><c:out value="${agency.name}" /></td>
        </tr>
        <tr class="b">
            <td width="20%">Description:</td>
            <td><c:out value="${agency.description}" /></td>
        </tr>
        <tr class="b">
            <td width="20%">HIC:</td>
            <td><c:out value="${agency.hic}" /></td>
        </tr>
        <tr class="b">
            <td width="20%">Primary Contact Name:</td>
            <td><c:out value="${agency.contactName}" /></td>
        </tr>
        <tr class="b">
            <td width="20%">Primary Contact Email:</td>
            <td><c:out value="${agency.contactEmail}" /></td>
        </tr>
        <tr class="b">
            <td width="20%">Primary Contact Phone:</td>
            <td><c:out value="${agency.contactPhone}" /></td>
        </tr>
    </table>

    <div>
        <p><a href="<html:rewrite action="/PMmodule/AgencyManager.do"/>?method=edit"> Update agency information </a></p>
    </div>

    <%@include file="/common/messages.jsp"%>

</html:form>