<%-- 
    Document   : CloneRx
    Created on : 28-Aug-2008, 10:51:45 AM
    Author     : toby
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<% response.setHeader("Cache-Control", "no-cache");%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta name='gwt:module' content='oscar.oscarRx.CloneRx=oscar.oscarRx.CloneRx'>
    <title>CloneRx</title>
    <logic:notPresent name="RxSessionBean" scope="session">
        <logic:redirect href="error.html" />
    </logic:notPresent>
    <logic:present name="RxSessionBean" scope="session">
        <bean:define id="bean" type="oscar.oscarRx.pageUtil.RxSessionBean" name="RxSessionBean" scope="session" />
        <logic:equal name="bean" property="valid" value="false">
            <logic:redirect href="error.html" />
        </logic:equal>
    </logic:present>
    
    <%
        oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean)pageContext.findAttribute("bean");
    %>
    
    <link rel="stylesheet" type="text/css" href="CloneFavorites.css">
</head>
<body>
<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber1" height="100%">
<%@ include file="TopLinks.jsp" %><!-- Row One included here-->

<tr>
    <td></td>
    <td width="100%" style="border-left: 2px solid #A9A9A9; " height="100%" valign="top">
    <table style="border-collapse: collapse" bordercolor="#111111" width="100%" height="100%">
        
        <tr>
            <td><div class="DivContentPadding">
                    <div class="DivContentTitle">Copy Favorites</div>
            </div></td>
        </tr>
        <tr>
            <td>
                <div class=DivContentPadding>
                    <input type=button value="Back to Search For Drug" class="ControlPushButton" onClick="javascript:window.location.href='SearchDrug.jsp';" />
                </div>
            </td>
        </tr>
        <tr>
            <td><div id="copyfavorites">
                <script language="javascript" src="../oscar.oscarRx.CloneRx/oscar.oscarRx.CloneRx.nocache.js"></script>
            </td></div>
        </tr>
        <tr>
            <td>
                <div class=DivContentPadding>
                    <input type=button value="Back to Search For Drug" class="ControlPushButton" onClick="javascript:window.location.href='SearchDrug.jsp';" />
                </div>
            </td>
        </tr>
        <tr height="100%"><td></td></tr>
    </table>
    </td>
</tr>

  <tr>
    <td height="0%" style="border-bottom:2px solid #A9A9A9; border-top:2px solid #A9A9A9; "></td>
    <td height="0%" style="border-bottom:2px solid #A9A9A9; border-top:2px solid #A9A9A9; "></td>
  </tr>
  <tr>
    <td width="100%" height="0%" colspan="2">&nbsp;</td>
  </tr>
  <tr>
    <td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC" colspan="2">
    </td>
  </tr>
</table>
</body>
</html>
