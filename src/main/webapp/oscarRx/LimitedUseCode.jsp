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

<%-- 
    Document   : LimitedUseCode
    Created on : Apr 15, 2010, 1:02:38 PM
    Author     : jackson
--%>

<%@page %><%@page import="oscar.oscarDemographic.data.*,org.oscarehr.common.model.Demographic"%>
<%@page import="oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler,java.util.*,oscar.oscarRx.util.*" %>
<%@page import="oscar.oscarLab.ca.on.*,oscar.util.*,oscar.oscarLab.*" %>

           <%
           String din=request.getParameter("din");
           String randomId=request.getParameter("randomId");
            ArrayList<LimitedUseCode> luList = LimitedUseLookup.getLUInfoForDin(din);

            if (luList != null){
     %>
            <div style="float:left; margin-left:2px; margin-right: 2px;">
            <table width="570" style="border-width: 1px; border-spacing: 2px; border-style: outset; border-color: black;">
                        <tr>
                                <th colspan="2" align="left">Limited Use Codes</th>
                        </tr>

                        <%for (LimitedUseCode limitedUseCode : luList){%>
                        <tr>
                            <td valign="top">
                                <a onclick="javascript:addLuCode('instructions_<%=randomId%>','<%=limitedUseCode.getUseId()%>')" href="javascript: return void();"><%=limitedUseCode.getUseId()%></a>&nbsp;
                            </td>
                            <td><%=limitedUseCode.getTxt()%></td>
                        </tr>
                        <%}%>
            </table>
            </div>
            <%}%>
