<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
<%@ page
	import="java.util.*,oscar.oscarReport.data.*, java.util.Properties, oscar.oscarBilling.ca.on.administration.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">

<%

Properties props = new Properties();
GstControlAction db = new GstControlAction();
props = db.readDatabase();
String percent = props.getProperty("gstPercent");

%>

<script type="text/javascript">
    function submitcheck(){
            document.getElementById("gstPercent").value = extractNums(document.getElementById("gstPercent").value);
    }
    function extractNums(str){
        return str.replace(/\D/g, "");
    }



</script>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Edit GST</title>
</head>
<body
	style="font-family: arial, helvetica, sans-serif; font-size: 13px;"
	onload="loadData()">
<html:form action="/admin/GstControl">
	<TABLE>
		<TR>
			<TD>GST :</TD>
			<TD align="right"><input type="text" size="3" maxlength="3"
				id="gstPercent" name="gstPercent" value="<%=percent%>" />%</TD>
		</TR>
	</TABLE>
	<input type="submit" value="Submit" onclick="submitcheck()" />
</html:form>
</body>
</html:html>
