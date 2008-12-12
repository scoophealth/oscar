<%--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
--%>

<%@ page isErrorPage="true"%>
<html>
<body>
<script language="javascript">
function BackToOscar()
{
    if (opener.callRefreshTabAlerts) {
	opener.callRefreshTabAlerts("oscar_new_msg");
        setTimeout("window.close()", 100);
    } else {
        window.close();
    }
}
</script>

<h1>Error page createMessage</h1>

<br>
An error occured in the bean. Error Message is:
<%= exception.getMessage() %><br>
Stack Trace is :
<pre><font color="red">
<%
 java.io.CharArrayWriter cw = new java.io.CharArrayWriter();
 java.io.PrintWriter pw = new java.io.PrintWriter(cw,true);
 exception.printStackTrace(pw);
 out.println(cw.toString());
 %>
</font></pre>
<br>
&lt;
<a href="javascript:BackToOscar()"> <bean:message
	key="backToOscar.link" /> </a>
&gt;


</body>
</html>
