<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page errorPage="errorpage.jsp" import="java.util.*"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%
	int nS = 1;
	int nE = 7;
	int n = 1;
  if(request.getParameter("print")!=null) {
	if("1".equals(request.getParameter("print")) ) {
	} else if("2".equals(request.getParameter("print")) ) {
		n  = 2;
		nS = 1+(n-1)*nE;
		nE = nS + nE -1;
	} else if("3".equals(request.getParameter("print")) ) {
		n  = 3;
		nS = 1+(n-1)*nE;
		nE = nS + nE -1;
	}else if("4".equals(request.getParameter("print")) ) {
		n  = 4;
		nS = 1+(n-1)*nE;
		nE = nS + nE -1;
	}else if("5".equals(request.getParameter("print")) ) {
		n  = 5;
		nS = 1+(n-1)*nE;
		nE = nS + nE -1;
	}else if("6".equals(request.getParameter("print")) ) {
		n  = 6;
		nS = 1+(n-1)*nE;
		nE = nS + nE -1;
	}
  }

	String[] cfgGraphic = null;
	if(request.getParameterValues("__cfgGraphicFile")!=null) {
		cfgGraphic = request.getParameterValues("__cfgGraphicFile");
	}

  //response.sendRedirect("../form/createpdf");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>CDC US Growth Charts</title>
<script type="text/javascript" language="JavaScript">
    function go() {
		if (document.all){
		<% if(cfgGraphic.length>1) {%>
			document.all.growth.action="../form/createpdf?__cfgGraphicFile=<%=cfgGraphic[0]%>&__cfgGraphicFile=<%=cfgGraphic[1]%>";
		<% }else{%>
			document.all.growth.action="../form/createpdf?__cfgGraphicFile=<%=request.getParameter("__cfgGraphicFile")%>";
		<% }%>
			document.all.growth.submit();
		}else{
		<% if(cfgGraphic.length>1) {%>
			document.getElementById('growth').action="../form/createpdf?__cfgGraphicFile=<%=cfgGraphic[0]%>&__cfgGraphicFile=<%=cfgGraphic[1]%>";
		<% }else{%>
			document.getElementById('growth').action="../form/createpdf?__cfgGraphicFile=<%=request.getParameter("__cfgGraphicFile")%>";
		<% }%>
			document.getElementById('growth').submit();
		}
//	        document.growth.action="../form/createpdf";
//	        document.growth.submit();
	}
	</script>
</head>
<body onload='window.setTimeout("go()",1000);'>
<form id="growth" name="growth" action="../form/createpdf" method="post">
<%
	Properties prop = new Properties();
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		String temp = e.nextElement().toString();
		if("submit".equals(temp) || "__cfgGraphicFile".equals(temp) ) continue;
		if(temp.matches("date_\\d+|age_\\d+|stature_\\d+|weight_\\d+|comment_\\d+|bmi_\\d+") ) {
			prop.setProperty(temp, request.getParameter(temp));
			continue;
		}
%>
<input type="hidden" name="<%= temp %>"
	value="<%=StringEscapeUtils.escapeHtml(request.getParameter(temp))%>" />
<%
}
%>
<%
	for (Enumeration e = prop.propertyNames() ; e.hasMoreElements() ;) {
		String temp = e.nextElement().toString();
		String[] str = temp.split("date_|age_|stature_|weight_|comment_|bmi_");
		int nC = Integer.parseInt(str[1]);
		if(nC>=nS && nC<=nE) {
			// swap: set tempName = 1 - 7
			nC = nC - ((n-1)*(nE-nS+1));
			String newName = temp.substring(0, temp.indexOf("_")+1) + nC;
			String newValue = prop.getProperty(temp, "");
			// 1-7 change to nS-nE
			String baseName = temp;
			String baseValue = prop.getProperty(newName, "");
			prop.setProperty(newName,newValue);
			prop.setProperty(baseName,baseValue);
		}
	}

	for (Enumeration e = prop.propertyNames() ; e.hasMoreElements() ;) {
		String temp = e.nextElement().toString();
%>
<input type="hidden" name="<%= temp %>"
	value="<%=StringEscapeUtils.escapeHtml(prop.getProperty(temp, ""))%>" />
<%
}
%>
<!--input type="submit" value="Save" onclick="go()" style="width: 120px;"/-->
One second ......
</form>
</body>
</html>
