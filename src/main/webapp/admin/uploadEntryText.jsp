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
if(session.getAttribute("user") == null) response.sendRedirect("../../logout.jsp");
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title><bean:message key="admin.admin.uploadEntryTxt"/></title>
        <link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">
    </head>
    <body>
        <table class="MainTable">
            <tr class="MainTableTopRow">
                <td class="MainTableTopRowLeftColumn" width="175">&nbsp;</td>
                <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                <tr>
                    <td><bean:message key="admin.admin.uploadEntryTxt"/></td>
                    <td>&nbsp;</td>
                    <td style="text-align: right"><oscar:help keywords="1.6.11" key="app.top1"/> | <a
                        href="javascript:popupStart(300,400,'About.jsp')"><bean:message
    					key="global.about" /></a> | <a
        				href="javascript:popupStart(300,400,'License.jsp')"><bean:message
            			key="global.license" /></a></td>
                </tr>
                </table>
                </td>
            </tr>
            <tr>
                <td class="MainTableLeftColumn" valign="top">&nbsp;</td>
                <td class="MainTableRightColumn">
                    <html:form action="/admin/uploadEntryText" method="POST" enctype="multipart/form-data">
                        <input type="file" name="importFile"><br>
                        <input type="submit" value="<bean:message key="admin.admin.uploadEntryTxt"/>">
                    </html:form>
                </td>
            </tr>
            <% 
            Boolean error = (Boolean)request.getAttribute("error");                                
            if( error != null ) {
            %>
            <tr>
                <td class="MainTableLeftColumn" valign="top">&nbsp;</td>
                <td class="MainTableRightColumn">
                    <% if( error == true ) { %>
                    <span style="color:red;"><bean:message key="admin.admin.ErrorUploadEntryTxt"/></span>
                    <%}else {%>
                     <span style="color:green;"><bean:message key="admin.admin.SuccessUploadEntryTxt"/></span>
                    <%}%>
                </td>
            </tr>
            <%}%>
            <tr>
                <td class="MainTableBottomRowLeftColumn">&nbsp;</td>
                <td class="MainTableBottomRowRightColumn" valign="top">&nbsp;</td>
            </tr>
        </table>
    </body>
</html>
