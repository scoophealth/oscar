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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%      
  if(session.getValue("user") == null)
      response.sendRedirect("../logout.jsp");
  String user_no; 
  user_no = (String) session.getAttribute("user");
  String asstProvider_no = "";
  String color ="";
  String premiumFlag="";
String service_form="", service_name="";
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>oscarBilling :: Clip board ::</title>
<link rel="stylesheet" href="billing.css">

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

</head>

<body leftmargin="0" topmargin="5" rightmargin="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#000000">
		<td height="40" width="20%">
		<form><input class=mbttn type=button name=print value=PRINT
			onClick=window.print()><input class=mbttn type=button
			name=back value=BACK onClick="history.go(-1);return false;"></form>
		</td>
		<td width="80%" align="left" bgcolor="#000000">
		<p><font face="Verdana, Arial, Helvetica, sans-serif"
			color="#FFFFFF"><b><font
			face="Arial, Helvetica, sans-serif" size="4">oscar<font
			size="3">Billing</font></font></b></font> <font color="#CCCCCC">Ciipboard </font></p>
		</td>
	</tr>
</table>


<pre>
<%=request.getParameter("textfield")==null?"":request.getParameter("textfield").replaceAll("\r[^\n]|[^\r]\n]", "\r\n")%>
</pre>

<pre>
<% 
String tmp1 = "";
String tmp =request.getParameter("textfield1")==null?"":request.getParameter("textfield1");
tmp1 = tmp;
while (tmp1.length() > 80){
%>
<%=tmp1.substring(0,80)%>
<%
tmp1 = tmp1.substring(80);

}


%>
<%=tmp1%>
</pre>


</body>
</html>
