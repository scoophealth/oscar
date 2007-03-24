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

<%@ page language="java" contentType="text/html" import="oscar.OscarProperties, oscar.util.BuildInfo, javax.servlet.http.Cookie, oscar.oscarSecurity.CookieSecurity" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<caisi:isModuleLoad moduleName="ticklerplus"><%
if(session.getValue("user") != null) {
		response.sendRedirect("provider/providercontrol.jsp");
	}
%></caisi:isModuleLoad><%
    String propFile = request.getContextPath().substring(1)+".properties";
	session.setAttribute("oscarContextPath",request.getContextPath());
    String sep = System.getProperty("file.separator");
    String propFileName = System.getProperty("user.home")+sep+propFile;
    OscarProperties props = OscarProperties.getInstance();
    props.loader(propFileName);

    BuildInfo buildInfo = BuildInfo.getInstance();
    String buildDate = buildInfo.getBuildDate();

    // clear old cookies
    Cookie rcpCookie = new Cookie(CookieSecurity.receptionistCookie, "");
    Cookie prvCookie = new Cookie(CookieSecurity.providerCookie, "");
    Cookie admCookie = new Cookie(CookieSecurity.adminCookie, "");
    rcpCookie.setPath("/");
    prvCookie.setPath("/");
    admCookie.setPath("/");
    response.addCookie(rcpCookie);
    response.addCookie(prvCookie);
    response.addCookie(admCookie);
%>

<html:html locale="true">
<head>
  <html:base/>
<META HTTP-EQUIV="CONTENT-TYPE" CONTENT="text/html; charset=UTF-8">
  <title>
  <% if (props.getProperty("logintitle", "").equals("")) { %>
    <bean:message key="loginApplication.title"/>
  <% } else { %>
    <%= props.getProperty("logintitle", "")%>
  <% } %>
  </title>
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
<tr><td width="200" bgcolor="#f5fffa" valign="top">
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
            <td align="left">
              <bean:message key="loginApplication.formUserName"/><%
              if(oscar.oscarSecurity.CRHelper.isCRFrameworkEnabled() && !net.sf.cookierevolver.CRFactory.getManager().isMachineIdentified(request)){
				%><img src="gatekeeper/appid/?act=image&/empty<%=System.currentTimeMillis() %>.gif" width='1' height='1'><%
				}
			  %><br/>
              <input type="text" name="username" size="15" maxlength="15" autocomplete="off"/>
            </td>
	  </tr>
          <tr>
            <td align="left">
              <bean:message key="loginApplication.formPwd"/><br/>
              <input type="password" name="password" size="15" maxlength="15" autocomplete="off"/>
            </td>
          </tr>
          <tr>
            <td align="left"> <font size="+1">
              <input type="submit" value="<bean:message key="index.btnSignIn"/>" />
              </font> </td>
          </tr>
          <tr>
            <td align="center">
              <div align="left">&nbsp;</div>
            </td>
          </tr>
          <tr>
            <td align="left">
              <bean:message key="index.formPIN"/>: 
              <br/>
              <html:password property="pin" size="15" maxlength="15"/><br/>
              <font size="-2">
              <bean:message key="loginApplication.formCmt"/> </font> 
            </td>
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
        <a href=# onClick='popupPage(500,700,"<bean:message key="loginApplication.gpltext"/>")'><bean:message key="loginApplication.gplLink"/></a><br>
        </font> </p>
    </td></tr>
</table>

</td>
    <td align="center">
      <!--right column-->
      <table border=0 width='100%' cellpadding="3">
      <tr>
      <td align="right">
          <font size="-2" color="#C0C0C0">build date: <%= buildDate %></font>
      </td></tr>
      <tr bgcolor='gold'><td>
      <center><font size="+1"><b><%=props.getProperty("logintitle", "")%>
      <% if (props.getProperty("logintitle", "").equals("")) { %>
          <bean:message key="loginApplication.alert"/>
      <% } %>
      </b></font></center>
      </td></tr></table>
      <br>
      <center>
        <b>
        <% if (props.getProperty("loginlogo", "").equals("")) { %>
            <html:img srcKey="loginApplication.image.logo" width="450" height="274"/>
        <% } else { %>
            <img src="<%=props.getProperty("loginlogo", "")%>">
        <% } %>
        <p>
        <font face="Verdana, Arial, Helvetica, sans-serif" size="-1">
        <% if (props.getProperty("logintext", "").equals("")) { %>
            <bean:message key="loginApplication.image.logoText"/>
        <% } else { %>
            <%=props.getProperty("logintext", "")%>
        <% } %>
        </font></b>
      </center>
    </td>
  </tr>
</table>
</body>
</html:html>
