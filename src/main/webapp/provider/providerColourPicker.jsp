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
if(session.getValue("user") == null)
    response.sendRedirect("../logout.htm");
  String curUser_no = (String) session.getAttribute("user");
  boolean bFirstLoad = request.getAttribute("status") == null;

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<title><bean:message key="provider.setColour.title" /></title>

<link rel="stylesheet" type="text/css"
	href="../oscarEncounter/encounterStyles.css">
<script type="text/javascript" src="../share/javascript/picker.js"></script>
<script type="text/javascript">
            function update() {
                var elem = document.getElementById('cdisp');
                elem.style.backgroundColor=document.forms[0].elements['colour'].value;
                setTimeout('update()', 1000);
            }
        </script>

</head>

<body class="BodyStyle" vlink="#0000FF"
	<%=bFirstLoad?"onload='update()'":""%>>

<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message
			key="provider.setColour.msgPrefs" /></td>
		<td style="color: white" class="MainTableTopRowRightColumn"><bean:message
			key="provider.setColour.msgProviderColour" /></td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn">&nbsp;</td>
		<td class="MainTableRightColumn">
		<%
              ProviderColourUpdater colourUpdater = new ProviderColourUpdater(curUser_no);
              String colour = colourUpdater.getColour();
               
               if( request.getAttribute("status") == null )
               {
      
            %> <html:form action="/setProviderColour.do">
			<html:hidden property="colour" value="<%=colour%>" />
			<bean:message key="provider.setColour.msgEdit" />
			<a href="javascript:TCP.popup(document.forms[0].elements['colour'])"><img
				width="15" height="13" border="0" src="../images/sel.gif"></a>
			<p><bean:message key="provider.setColour.msgSatus" />
			<div id='cdisp' style='width: 33%'>&nbsp;</div>
			</p>
			<p><input type="submit" onclick="return validate();"
				value="<bean:message key="provider.setColour.btnSubmit"/>" />
		</html:form> <%
               }
               else if( ((String)request.getAttribute("status")).equals("complete") ) {
            %> <bean:message key="provider.setColour.msgSuccess" /> <br>

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
</body>
</html:html>
