<!--  
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
-->

<%@ page language="java" contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html>
<head>
  <html:base/>
  <title><bean:message key="loginApplication.title"/></title>
  <!--LINK REL="StyleSheet" HREF="web.css" TYPE="text/css"-->

  <script language="JavaScript">
  <!-- hide
  function setfocus() {
    document.loginForm.username.focus();
    document.loginForm.username.select();
  }
  function popupPage(vheight,vwidth,varpage) {
    var page = "" + varpage;
    windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
    var popup=window.open(page, "gpl", windowprops);
  }
  -->
  </script>
</head>

<body onLoad="setfocus()" bgcolor="#ffffff">
<table border="0" width="100%">
<tr><td width="200" bgcolor="#f5fffa">
<!--left column-->
  <table border="0" cellspacing="0" cellpadding="0" width="100%" >
    <tr bgcolor="#486ebd"> 
          <th align="CENTER" bgcolor="#009966"><font face="Helvetica" color="#FFFFFF">
<%
  String key = "loginApplication.formLabel" ;
  if(request.getParameter("login")!=null && request.getParameter("login").equals("failed") )
    key = "loginApplication.formFailedLabel" ;
%>
            <bean:message key="<%=key%>"/></font></th>
    </tr>
  </table>
      <table border="0" cellpadding="2" cellspacing="2"  width="100%" >
        <html:form action="login" >
          <tr> 
            <td align="center"> 
              <div align="left">&nbsp;</div>
            </td>
          </tr>
          <tr> 
            <td><bean:message key="loginApplication.formUserName"/></td>
          </tr>
		  <tr><td align="center">
              <html:text property="username" size="15" maxlength="15"/>
            </td></tr>
          <tr> 
            <td><bean:message key="loginApplication.formPwd"/></td>
          </tr>
          <tr>
            <td  align="center">
              <html:password property="password" size="15" maxlength="10"/>
            </td>
          </tr>
          <tr> 
            <td  align="center"> <font size="+1"> 
              <html:submit value="Sign in" />
              </font> </td>
          </tr>
          <tr> 
            <td  align="center" nowrap><font size="-1">PIN: </font> 
              <html:password property="pin" size="6" maxlength="6"/>
              <font size="-2"><bean:message key="loginApplication.formCmt"/> </font> </td>
          </tr>
		  <input type=hidden name='propname' value='<bean:message key="loginApplication.propertyFile"/>' />
        </html:form>
      </table>
  <p>
      <hr width="100%" color="navy">
  <table border="0" width="100%">
  <tr>
    <td><p><font size="-2"><bean:message key="loginApplication.leftRmk"/>
		<br><br>
        <a href=# onClick='popupPage(500,700,"gpl.htm")'><bean:message key="loginApplication.gplLink"/></a><br>
        </font> </p>
    </td></tr>
</table>

</td>
    <td align="center"> 
      <!--right column-->
      <table border=0 width='100%'>
      <tr bgcolor='gold'><td>
      <h2><bean:message key="loginApplication.alert"/></h2>
      </td></tr></table>
      <center>
        <b>
        <html:img srcKey="loginApplication.image.logo" width="450" height="274"/><br>
        <font face="Verdana, Arial, Helvetica, sans-serif" size="-1">
        <bean:message key="loginApplication.image.logoText"/>
        </font></b>
      </center>
    </td>
  </tr>
</table>
</body>
</html:html>
