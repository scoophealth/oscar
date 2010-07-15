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
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
-->

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
OscarProperties props = OscarProperties.getInstance();

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
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
        <html:base/>
        <META HTTP-EQUIV="CONTENT-TYPE" CONTENT="text/html; charset=ISO-8859-1">
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
        
        <style type="text/css">
            body { 
               font-family: Verdana, helvetica, sans-serif;
               margin: 0px;
               padding:0px;
               
            }
            
            td.topbar{
               background-color: gold;
               
               
            }
            td.leftbar{
                background-color: #009966;
                color: white;
            }
            td.leftinput{
                background-color: #f5fffa;
                //background-color: #ffffff;
                
                font-size: small;
                }
                
                span.extrasmall{
                    font-size: x-small;
                }
        </style>
    </head>
    
    <body onLoad="setfocus()" bgcolor="#ffffff">
        
        <table border=0 width="100%" height="100%">
            <tr>
                <td align="center" class="leftbar" height="20px"><%
                    String key = "loginApplication.formLabel" ;
                    if(request.getParameter("login")!=null && request.getParameter("login").equals("failed") )
                    key = "loginApplication.formFailedLabel" ;
                    %><bean:message key="<%=key%>"/>        
                </td>
                <td  class="topbar" align="center" >
                    <span style="float: right; color:#FFFFFF; font-size: xx-small;text-align: right;">build date: <%= BuildInfo.getBuildDate() %> <br/> build tag: <%=BuildInfo.getBuildTag()%> </span>
                    <%=props.getProperty("logintitle", "")%>
                    <% if (props.getProperty("logintitle", "").equals("")) { %>
                    <bean:message key="loginApplication.alert"/>
                    <% } %>                    
                </td>
            </tr>
            <tr>
                <td width="200  " class="leftinput" valign="top">
                    <!--- left side -->
                        
                            <html:form action="login" >
                            <bean:message key="loginApplication.formUserName"/><%
                            if(oscar.oscarSecurity.CRHelper.isCRFrameworkEnabled() && !net.sf.cookierevolver.CRFactory.getManager().isMachineIdentified(request)){
                            %><img src="gatekeeper/appid/?act=image&/empty<%=System.currentTimeMillis() %>.gif" width='1' height='1'><%
                            }
                            %>
                        
                        <br/>            
                        <input type="text" name="username" value="" size="15" maxlength="15" autocomplete="off"/>
                        <br/>                
                        <bean:message key="loginApplication.formPwd"/><br/>
                        <input type="password" name="password" value="" size="15" maxlength="15" autocomplete="off"/><br/>
                                <input type="submit" value="<bean:message key="index.btnSignIn"/>" />
                        </br></br>
                        <bean:message key="index.formPIN"/>: 
                        <br/>
                        <input type="password" name="pin" value="" size="15" maxlength="15" autocomplete="off"/><br/>
                       
                        <span class="extrasmall">
                            <bean:message key="loginApplication.formCmt"/>
                        </span>
                        <input type=hidden name='propname' value='<bean:message key="loginApplication.propertyFile"/>' />
                        </html:form>
                        <hr width="100%" color="navy">
                        
                        <span class="extrasmall">
                            <bean:message key="loginApplication.leftRmk"/>
                            <a href=# onClick='popupPage(500,700,"<bean:message key="loginApplication.gpltext"/>")'><bean:message key="loginApplication.gplLink"/></a>
                            <br><br>
                            <img style="width: 26px; height: 18px;" alt="<bean:message key="loginApplication.image.i18nAlt"/>"
                            title="<bean:message key="loginApplication.image.i18nTitle"/>"
                            src="<bean:message key="loginApplication.image.i18n"/>">
                            <bean:message key="loginApplication.i18nText"/>

                        </span>
                    <!-- left side end-->
                </td>
                <td align="center" valign="top">
                    <div style="margin-top:25px;"><% if (props.getProperty("loginlogo", "").equals("")) { %>
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
                            </font>
                    </div>
                    
                </td>
            </tr>     
        </table>
        
    </body>
</html:html>
