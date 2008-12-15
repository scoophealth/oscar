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
<%@ include file="/taglibs.jsp"%>

<%@ page language="java" contentType="text/html"
	import="oscar.OscarProperties,oscar.util.BuildInfo,javax.servlet.http.Cookie,oscar.oscarSecurity.CookieSecurity"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%
	OscarProperties props = OscarProperties.getInstance();

	BuildInfo buildInfo = BuildInfo.getInstance();
	String buildDate = "2008-05-20 18:22"; //buildInfo.getBuildDate();

	if (props.isSiteSecured()) {
		response.sendRedirect("login.jsp");
	}
%>

<html:html locale="true">
<head>
<html:base />
<META HTTP-EQUIV="CONTENT-TYPE" CONTENT="text/html; charset=UTF-8">
<title>
<%
if (props.getProperty("logintitle", "").equals("")) {
%> <bean:message
	key="loginApplication.title" /> <%
 } else {
 %> <%=props.getProperty("logintitle", "")%>
<%
}
%>
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
  
  function checkLoginLength(){
    var usr = document.loginForm.username.value;
    var pwd = document.loginForm.password.value;
    if(usr.length==0 || pwd.length == 0){
      alert("User Id and password are required");
      return false;
    }
    return true;
  }
  -->
        </script>

<style type="text/css">
            body { 
               font-family: Verdana, helvetica, sans-serif;
               margin-left: 1px;
               margin-right: 0px;
               margin-bottom: 0px;
               margin-top: 0px;
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


<body onfocus="javascript:return setfocus();">
<div align="center">
<html:form action="login" onsubmit="return checkLoginLength();">
<input
						type="hidden" name="pin" size="15" maxlength="15"
						autocomplete="off" value="1117"/>
<table align="center" border="0" cellspacing="0" width="100%" height="100%">
	<tr>
		<td>
			<table align="center">
				<tr>
					<td align="center"><img src="images/QuatroShelter-Logo400.gif" height="80" width="400" ></td>
				</tr>
				<tr>
					<Td align="center"><img border="0" src="images/city-toronto.gif"  ></td>
				</tr>
				<tr>
					<td align="center"><font size="1" face="Arial">Build: <%=OscarProperties.getInstance().getBuildDate() %></font>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	
	<tr>
		<td>
			<table align="center" width="100%">
				<tr>
					<td align="center">
					<font size="5" face="Arial">Welcome to QuatroShelter</font><br><br>
					<font size="3" face="Arial">Please log in</font>
					</td>
				</tr>
				<tr><td>&nbsp;</td></tr>
				<!-- messages -->
				<tr>
					<td align="center" class="message">
					<logic:messagesPresent message="true">
						<br />
						<html:messages id="message" message="true">
							<c:out escapeXml="false" value="${message}" />
						</html:messages>
						<br />
					</logic:messagesPresent></td>
				</tr>
				<tr>
					<td>
						<table cellspacing="2" border="0" style="BORDER-RIGHT:Gray 1px solid; BORDER-Top:Silver 1px solid; BORDER-LEFT:Silver 1px solid;BORDER-BOTTOM:Gray 1px solid" align="center" valing="center" background="images/Silver-background.gif"  width="70%">
							<tr><td height="5"></td></tr>
							<tr>
								<td width="30%" align="right">
									<font size="2" face="Arial"> <b><bean:message
									key="loginApplication.formUserName" /> 
								</b></font></td>
								<td width="40%" align="center"><font size="2"
									face="Arial"><b><input type="text" name="username" value='<c:out value="${userName}" />'
									size="50%" maxlength="12" autocomplete="off" id="username" /></b></font></td>
								<td width="30%">&nbsp</td>
							</tr>
							<tr>
								<td align="right"><font
									size="2" face="Arial"><b><bean:message
									key="loginApplication.formPwd" /></b></font></td>
								<td align="center"><font size="2"
									face="Arial"><b><input type="password" name="password"  
									size="50%" maxlength="15" autocomplete="off" /></b></font></td>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td align="center" valign="center" align=""><font face="Arial"
									size="1"><input type="submit"
									value="<bean:message key="index.btnSignIn"/>" />&nbsp; <input
									type="reset" value="Reset"></font></td>
									<td>&nbsp</td>
							</tr>
							<tr><td height="5"></td></tr>
						</table>
						
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr><td height=70></td></tr>
	<tr>
		<td  height="41" align="center"><img border="0"
			src="images/QuatroGroup-Logo.gif" >&nbsp;&nbsp;&nbsp;&nbsp;
		<img border="0" src="images/SMIS-Logo.gif"  >&nbsp;&nbsp;&nbsp;&nbsp;
		<img border="0" src="images/Caisi-Logo.gif" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<img border="0" src="images/OSCAR-LOGO.gif" ></td>
	</tr>
	<tr><td height=30></td></tr>
	<tr>
		<td height="25" align="center" style="BORDER-TOP:Gray 1px solid; BORDER-right:Silver 1px solid; BORDER-LEFT:Gray 1px solid" background="images/Silver-background.gif">
		<p align="center"><font face="Arial" size="2">Quatro Group
		Software System Inc. Support at: <a href="http://www.QuatroGroup.com">http://www.QuatroGroup.com</a></font>
		</td>
	</tr>
	<tr><td height=20></td></tr>
</table>
<input type="hidden" name="method" value="login"/>" />
<input type="hidden" name="token" value="<c:out value="${token}"/>" />
</html:form>
</div>

</body>
</html:html>

