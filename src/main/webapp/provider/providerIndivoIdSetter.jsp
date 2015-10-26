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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="oscar.oscarProvider.data.*"%>


<%

String curUser_no;
curUser_no = (String) session.getAttribute("user");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<title><bean:message key="provider.setPHRLogin.title" /></title>

<link rel="stylesheet" type="text/css"
	href="../oscarEncounter/encounterStyles.css">

<script type="text/javascript">
            function validate() {
                var ret = true;
                var login = document.forms["setMyOscarIdForm"].myOscarLoginId.value.replace(/^\s+|\s+$/g, '');
                if(login.length < 3) {
                    alert("Login must be at least 3 characters");
                    ret = false;
                }
                
                document.forms["setMyOscarIdForm"].myOscarLoginId.value = login;
                document.forms["setMyOscarIdForm"].myOscarLoginId.focus();
                return ret;
            }
        </script>
</head>

<body class="BodyStyle" vlink="#0000FF">

<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message
			key="provider.setColour.msgPrefs" /></td>
		<td style="color: white" class="MainTableTopRowRightColumn"><bean:message
			key="provider.setPHRLogin.msgMyOscarId" /></td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn">&nbsp;</td>
		<td class="MainTableRightColumn"><html:errors /> <%
              String login = ProviderMyOscarIdData.getMyOscarId(curUser_no);
               int atsign = login.indexOf("@");
               if( atsign > -1 )
                   login = login.substring(0,atsign);
                   
               if( request.getAttribute("status") == null )
               {
      
            %> <html:form action="/setMyOscarId.do">
			<bean:message key="provider.setPHRLogin.msgEdit" />&nbsp;&nbsp;
               <html:text property="myOscarLoginId" value="<%=login%>"
				size="20" />
			<br>
			<input type="submit" onclick="return validate();"
				value="<bean:message key="provider.setPHRLogin.btnSubmit"/>" />
		</html:form> <%
               }               
               else if( ((String)request.getAttribute("status")).equals("complete") ) {
            %> <bean:message key="provider.setPHRLogin.msgSuccess" />&nbsp;'<%=login%>'

		<%
               }
            %>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
<script type="text/javascript">
        if( document.forms.length > 0 )
            document.forms["setMyOscarIdForm"].myOscarLoginId.focus();
    </script>
</body>
</html:html>
