<%--

    Copyright (c) 2001-2002. Andromedia. All Rights Reserved.
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
    Andromedia, to be provided as
    part of the OSCAR McMaster
    EMR System

--%>
<%@page import="java.math.*, java.util.*,  oscar.*, java.net.*,oscar.oscarBilling.ca.bc.data.*,org.oscarehr.common.model.*,oscar.util.*" %>
<%@page import="org.springframework.web.context.WebApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils, oscar.entities.*" %><%
        if (session.getAttribute("user") == null) {
            response.sendRedirect("../../logout.jsp");
        }


        String form = request.getParameter("form");
        String field = request.getParameter("field");
        String info = request.getParameter("info");
        String feeField = request.getParameter("feeField");
        String searchStr = request.getParameter("searchStr");
        String serviceDate = request.getParameter("serviceDate");
        Date serDate = UtilDateUtilities.StringToDate(serviceDate,"yyyyMMdd");
        
        if(serDate == null){
           serDate = new Date();
        }

        if (searchStr == null) {
            searchStr = "%";
        } else {
            searchStr = "%" + searchStr + "%";
        }
        searchStr = oscar.Misc.mysqlEscape(searchStr);
        BillingCodeData bcd = new BillingCodeData();
        List<BillingService> billServiceList = bcd.search(searchStr,serDate);
        boolean color = false;
%>

<html:html locale="true">
    <head>
    <html:base/>
    <title>OSCAR Billing Fee Item</title>
    <link rel="stylesheet" href="../../../../share/css/oscar.css">
    </head>
    

    <script language="JavaScript">
        function posttoText(index){
            self.close();
            opener.document.<%=form%>.<%=field%>.value = index;
            opener.document.focus();
        }
        <%if (request.getParameter("corrections") != null) {%>
            function updateFeeCodeValues(code,description,fee){
                self.close();
                opener.document.<%=form%>.<%=field%>.value = code;
                opener.document.<%=form%>.<%=feeField%>.value = fee;

                var valueEle = opener.document.getElementById('billValue');
                if (valueEle){
                    valueEle.value = fee;
                }

                var valDisp = opener.document.getElementById('valueDisplay');
                if (valDisp){
                    valDisp.innerHTML = fee;
                }

                opener.document.focus();
            }
        <%}%>

    </script>
    <body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#D3D3D3">
            <tr>
                <td height="40" width="25"></td>
                <td width="90%" align="left">
                    <p><font face="Verdana" color="#4D4D4D"><b><font size="4">oscar<font size="3">Billing Fee Items</font></font></b></font>
                    </p>
                </td>
            </tr>
        </table>
        <br>
        <table width="100%" border="0" cellspacing="5" cellpadding="0">
            <tr bgcolor="#D4D4D4">
                <td>Link</td>
                <td>Description</td>
            </tr>
            
            <%for(BillingService code : billServiceList){%>
            <tr <%=((color) ? "bgcolor=\"#F6F6F6\"" : "")%> align="left" valign="top">
                <td class="SmallerText">
                    <%if (request.getParameter("corrections") == null) {%>
                    <a href=# onClick="posttoText('<%=code.getServiceCode()%>');"><%=code.getServiceCode()%></a>
                    <%} else {%>
                    <a href=# onClick="updateFeeCodeValues('<%=code.getServiceCode()%>',' ','<%=code.getValue()%>');"><%=code.getServiceCode()%></a>
                    <%}%>
                </td>
                <td class="SmallerText"><%=code.getDescription()%> (<%=code.getValue()%>)  </td>
            </tr>
            <%
              color = !(color);
              }
            %>
            <tr bgcolor="#D4D4D4">
                <td colspan="5">&nbsp</td>
            </tr>

        </table>
    </body>
</html>
