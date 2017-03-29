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

<%@page import="org.oscarehr.common.service.AcceptableUseAgreementManager"%>
<%@page import="oscar.OscarProperties, javax.servlet.http.Cookie, oscar.oscarSecurity.CookieSecurity, oscar.login.UAgentInfo" %>
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

// clear old cookies - this cookie has something to do with old OscarDocument setup
Cookie prvCookie = new Cookie(CookieSecurity.providerCookie, "");
prvCookie.setPath("/");
prvCookie.setSecure(true);
response.addCookie(prvCookie);


// Initialize browser info variables
String userAgent = request.getHeader("User-Agent");
String httpAccept = request.getHeader("Accept");
UAgentInfo detector = new UAgentInfo(userAgent, httpAccept);

// This parameter exists only if the user clicks the "Full Site" link on a mobile device
if (request.getParameter("full") != null) {
    session.setAttribute("fullSite","true");
}

// If a user is accessing through a smartphone (currently only supports mobile browsers with webkit),
// and if they haven't already clicked to see the full site, then we set a property which is
// used to bring up iPhone-optimized stylesheets, and add or remove functionality in certain pages.
if (detector.detectSmartphone() && detector.detectWebkit()  && session.getAttribute("fullSite") == null) {
    session.setAttribute("mobileOptimized", "true");
} else {
    session.removeAttribute("mobileOptimized");
}
Boolean isMobileOptimized = session.getAttribute("mobileOptimized") != null;


//Input field styles
String login_input_style="login_txt_fields";
%>

<html:html locale="true">
    <head>
    <link rel="shortcut icon" href="images/Oscar.ico" />
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
        <html:base/>
        <% if (isMobileOptimized) { %><meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, width=device-width"/><% } %>
        <title>
            <% if (props.getProperty("logintitle", "").equals("")) { %>
            <bean:message key="loginApplication.title"/>
            <% } else { %>
            <%= props.getProperty("logintitle", "")%>
            <% } %>
        </title>
        <!--LINK REL="StyleSheet" HREF="web.css" TYPE="text/css"-->

        <script language="JavaScript">
        function showHideItem(id){
            if(document.getElementById(id).style.display == 'none')
                document.getElementById(id).style.display = 'block';
            else
                document.getElementById(id).style.display = 'none';
        }
        
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
            
            .login_txt_fields {
		
			 
				border: 1px solid #999;
				height: 25px;
				-webkit-box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.3);
				-moz-box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.3);
				box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.3);
			}
			 
			.login_txt_fields_error {
			  border: 1px solid #F78181;
				height: 25px;
				-webkit-box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.3);
				-moz-box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.3);
				box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.3);
			}
            
            td.topbar{
               background-color: gold;
            }
            td.leftbar{
                background-color:  #106B3A; /*#009966; */
                color: white;
            }
            td.leftinput{
                background-color: #f5fffa;
            }
            td#loginText{
                width:200px;
                font-size: small;
                }
            span#buildInfo{
                float: right; color:#FFFFFF; font-size: xx-small; text-align: right;
            }
                span.extrasmall{
                    font-size: x-small;
                }
            #mobileMsg { display: none; }
        </style>
        <% if (isMobileOptimized) { %>
        <!-- Small adjustments are made to the mobile stylesheet -->
        <style type="text/css">
            html { -webkit-text-size-adjust: none; }
            td.topbar{ width: 75%; }
            td.leftbar{ width: 25%; }
            span.extrasmall{ font-size: small; }
            #browserInfo, #logoImg, #buildInfo { display: none; }
            #mobileMsg { display: inline; }
        </style>
        <% } %>
    </head>
    
    <body onLoad="setfocus()" bgcolor="#ffffff">
        
        <table border=0 width="100%">
            <tr>
                <td align="center" class="leftbar" height="20px" width="200px"><%
                    String key = "loginApplication.formLabel" ;
                    if(request.getParameter("login")!=null && request.getParameter("login").equals("failed") ){
                    key = "loginApplication.formFailedLabel" ;
                    login_input_style="login_txt_fields_error";                    
                    }
                    %><bean:message key="<%=key%>"/>        
                </td>
                <td  class="topbar" align="center" >
                    <span id="buildInfo">build date: <%= OscarProperties.getBuildDate() %> <br/> build tag: <%=OscarProperties.getBuildTag()%> </span>
                    <%=props.getProperty("logintitle", "")%>
                    <% if (props.getProperty("logintitle", "").equals("")) { %>
                    <bean:message key="loginApplication.alert"/>
                    <% } %>                    
                </td>
            </tr>
        </table>
        <table class="leftinput" border="0" width="100%">
            <tr>
                <td id="loginText" valign="top">
                    <!--- left side -->
                        
                            <html:form action="login" >
                            <bean:message key="loginApplication.formUserName"/><%
                            if(oscar.oscarSecurity.CRHelper.isCRFrameworkEnabled() && !net.sf.cookierevolver.CRFactory.getManager().isMachineIdentified(request)){
                            %><img src="gatekeeper/appid/?act=image&/empty<%=System.currentTimeMillis() %>.gif" width='1' height='1'><%
                            }
                            %>
                        
                        <br/>            
                        <input type="text" name="username" value="" size="15" maxlength="15" autocomplete="off" class="<%=login_input_style %>"/>
                        <br/>                
                        <bean:message key="loginApplication.formPwd"/><br/>
                        <input type="password" name="password" value="" size="15" maxlength="32" autocomplete="off" class="<%=login_input_style %>"/><br/>
                                <input type="submit" value="<bean:message key="index.btnSignIn"/>" />
                        <br/>
                        <bean:message key="index.formPIN"/>: 
                        <br/>
                        <input type="password" name="pin" value="" size="15" maxlength="15" autocomplete="off" class="<%=login_input_style %>"/><br/>
                       
                        <span class="extrasmall">
                            <bean:message key="loginApplication.formCmt"/>
                        </span>
                        <input type=hidden name='propname' value='<bean:message key="loginApplication.propertyFile"/>' />
                        </html:form>
                        
                        <%if (AcceptableUseAgreementManager.hasAUA()){ %>
                        <span class="extrasmall">
                        	<bean:message key="global.aua" /> &nbsp; <a href="javascript:void(0);" onclick="showHideItem('auaText');"><bean:message key="global.showhide"/></a>
                        </span>
                        <%} %>
                        <hr width="100%" color="navy">
                        
                        <span class="extrasmall">
                            <div id="mobileMsg"><bean:message key="loginApplication.mobileMsg"/>
                                <a href="index.jsp?full=true"><bean:message key="loginApplication.fullSite"/></a>
                                <br/><br/>
                            </div>
                            <div id="browserInfo"><bean:message key="loginApplication.leftRmk1"/></div>
                            <bean:message key="loginApplication.leftRmk2" />
                            <a href=# onClick='popupPage(500,700,"http://www.gnu.org/licenses/gpl-2.0.txt")'><bean:message key="loginApplication.gplLink"/></a>
                            <br/>
                            <img style="width: 26px; height: 18px;" alt="<bean:message key="loginApplication.image.i18nAlt"/>"
                            title="<bean:message key="loginApplication.image.i18nTitle"/>"
                            src="<bean:message key="loginApplication.image.i18n"/>">
                            <bean:message key="loginApplication.i18nText"/>

                        </span>
                    <!-- left side end-->
                </td>
                <td id="logoImg" align="center" valign="top">
                	<%if (AcceptableUseAgreementManager.hasAUA()){ %>
                	<div style="float:right;text-align:center;z-index:3;display:none;" id="auaText">
            				<h3><bean:message key="provider.login.title.confidentiality"/></h3>
        					<div style="margin-left:auto; margin-right:auto; text-align:left; width:70%; padding:5px; border:2px groove black;"><%=AcceptableUseAgreementManager.getAUAText()%></div>
        			</div>
                	<%}%>
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
