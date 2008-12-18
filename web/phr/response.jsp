<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="/WEB-INF/phr-tag.tld" prefix="phr"%>

<%@ page import="org.oscarehr.phr.PHRAuthentication"%>
<%@ page import="oscar.oscarProvider.data.ProviderData"%>

<%
String errorMsg = (String) request.getAttribute("error_msg");
boolean errors = false;
String cssPrefix = "objectGreen";
if (errorMsg != null) errors = true;
if (errors) {
    cssPrefix = "objectRed";
}
%>

<%
String providerName = request.getSession().getAttribute("userfirstname") + " " + 
        request.getSession().getAttribute("userlastname");
String providerNo = (String) request.getSession().getAttribute("user");
ProviderData providerData = new ProviderData();
providerData.setProviderNo(providerNo);
String providerPhrId = providerData.getMyOscarId();
PHRAuthentication phrAuth = (PHRAuthentication) session.getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<link rel="stylesheet" type="text/css" href="../phr/phr.css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>PHR Call</title>
<script type="text/javascript" language="JavaScript"
	src="../share/javascript/prototype.js"></script>
<script type="text/javascript" language="JavaScript" src="../phr/phr.js"></script>
<script type="text/javascript" language="JavaScript">
            function startWindowTimeout() {
                var sec=1500;
                setTimeout("closeWindow()",sec);
            }

            function closeWindow() {
                window.opener=self;
                window.close();
            }
        </script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>
<body>
<div class="<%=cssPrefix%>">
<div class="<%=cssPrefix%>Header">Personal Health Record Action</div>
<%if (errors) {%> <font class="announcementRed"><%=errorMsg%></font> <%} else {%>
<center><font class="announcementGreen">SUCCESS, added
to send queue.</font></center>
<center><font style="font-size: 10px;"><sup>(It
will be sent next time you log in)</sup></font></center>
<br />
<logic:present name="<%=PHRAuthentication.SESSION_PHR_AUTH%>">
	<div class="myoscarLoginElementAuth">Status: <b>Logged in as
	<%=providerName%></b> (<%=phrAuth.getUserId()%>)<br />
	<center>Closing Window... <a href="javascript:;"
		onclick="closeWindow()">close</a></center>
	<%-- if no errors and logged in, close window--%> <%if (!errors) {%> <script
		type="text/javascript" language="JavaScript">startWindowTimeout()</script>
	<%}%>
	</div>
	<!--<p style="background-color: #E00000"  title="fade=[on] requireclick=[on] header=[Diabetes Med Changes] body=[<span style='color:red'>no DM Med changes have been recorded</span> </br>]">dsfsdfsdfsdfgsdgsdg</p>-->
</logic:present> <logic:notPresent name="<%=PHRAuthentication.SESSION_PHR_AUTH%>">
	<div class="myoscarLoginElementNoAuth">
	<form action="../phr/Login.do" name="phrLogin" method="POST"
		style="margin-bottom: 0px;">
	<%request.setAttribute("phrUserLoginErrorMsg", request.getParameter("phrUserLoginErrorMsg"));
                                request.setAttribute("phrTechLoginErrorMsg", request.getParameter("phrTechLoginErrorMsg"));%>
	<logic:present name="phrUserLoginErrorMsg">
		<div class="phrLoginErrorMsg"><font color="red"><bean:write
			name="phrUserLoginErrorMsg" /></font> <logic:present
			name="phrTechLoginErrorMsg">
			<a href="javascript:;"
				title="fade=[on] requireclick=[off] cssheader=[moreInfoBoxoverHeader] cssbody=[moreInfoBoxoverBody] singleclickstop=[on] header=[MyOSCAR Server Response:] body=[<bean:write name="phrTechLoginErrorMsg"/> </br>]">More
			Info</a></div>
	</logic:present> </logic:present> Status: <b>Not logged in</b><br />
	<%=providerName%> password: <input type="password" id="phrPassword"
		name="phrPassword" style="font-size: 8px; width: 40px;"><br />
	<center><a
		href="javascript: document.forms['phrLogin'].submit()">Login &
	Send Now</a> &nbsp;&nbsp; <a href="javascript:;" onclick="closeWindow()">Send
	Later</a></center>
	<input type="hidden" name="forwardto"
		value="<%=request.getServletPath()%>"></form>
	</div>
</logic:notPresent> <%}%>
</div>
<script type="text/javascript" src="../share/javascript/boxover.js"></script>
<phr:IfTimeToExchange>
	<script type="text/javascript">
            phrExchangeGo('../phrExchange.do');
            </script>
</phr:IfTimeToExchange>
</body>
</html>
