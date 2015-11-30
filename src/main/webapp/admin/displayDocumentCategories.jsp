<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@ page
	import="java.util.*, oscar.dms.EDocUtil"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.document" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin,_admin.document");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />

<% 
ArrayList<String> doctypesD = EDocUtil.getDoctypes("demographic");
ArrayList<String> doctypesP = EDocUtil.getDoctypes("provider");
%>
<script>


function popupPage(vheight,vwidth,varpage) { //open a new popup window
	  var page = "" + varpage;
	  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";//360,680
	  var popup=window.open(page, "groupno", windowprops);
	  if (popup != null) {
	    if (popup.opener == null) {
	      popup.opener = self;
	    }
	    popup.focus();
	  }
}

function submitUpload(object) {
    object.Submit.disabled = true;
    
    return true;
}
</script>
    

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<c:out value="${ctx}/share/javascript/prototype.js"/>"></script>
<script type="text/javascript" src="<c:out value="${ctx}/share/javascript/Oscar.js"/>"></script>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css" />
<title> Document Categories</title>
</head>
<body class="mainbody">

<table class="MainTable" id="scrollNumber1" name="documentCategoryTable" style="margin: 0px;">
            <tr class="topbar">
                <td class="MainTableTopRowLeftColumn" width="60px">Document Categories</td>
                <td class="MainTableTopRowRightColumn">
                    <table class="TopStatusBar">
                        <tr>
                            <td>Display Document Categories</td>
                            <td style="text-align: right;"  >
                                    <a href="javascript: popupStart(300, 400, 'Help.jsp')">Help</a> |
                                    <a href="javascript: popupStart(300, 400, 'About.jsp')">About</a> |
                                    <a href="javascript: popupStart(300, 400, 'License.jsp')">License</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
 
	           
            <table class="docTypeTable" width="60px">
            <br>
            <br>
            <tr>
            <tr class="DocTableTopRowLeftColumn"  width="60px">Demographic Document Categories</tr>
            
            	<table width="50%" cellspacing="2" cellpadding="2" border="1" id="demographicDocType" style="margin: 0px;" >
					<tr bgcolor="#ccccff">
						<td width="10%" height="25"><b>Document Type</b></td>
						<td width="10%" height="25"><b>Status</b></td>
					</tr>
					<% for (String doctypeD : doctypesD) { %>
					<tr>
					    
						<!--<td align="center" width="40%" height="25">Demographic</td>-->
						<td width="10%" height="25"><%=doctypeD%></td>
						
						<td width="10%" height="25"><%=EDocUtil.getDocStatus("demographic",doctypeD)%></td>
						
						
						
					</tr>
					<% }%>
					</table>
			</tr>
           <br><br>
           <tr>
            <td class="DocTableTopRowRightColumn"  width="60px">Provider Document Categories</td>
            <table width="50%" cellspacing="2" cellpadding="2" border="1" id="demographicDocType" style="margin: 0px;" >
					<tr bgcolor="#ccccff">
						<td  width="10%" height="25"><b>Document Type</b></td>
						<td  width="10%" height="25"><b>Status</b></td>
					</tr>
					<% for (String doctypeP : doctypesP) { %>
						
						<tr>
						<td  width="10%" height="25"><%=doctypeP%></td>
						<td  width="10%" height="25"><%=EDocUtil.getDocStatus("provider",doctypeP)%></td>
					   </tr>
					<% }%>
						
			</table>
			</tr>
		
		</table>
		<br>
		<br>
          <tr>
          <td> <input type="button" value="Add New" onclick='popupPage(550,800,&quot;<html:rewrite page="/dms/addNewDocumentCategories.jsp"/>&quot;);return false;' /> 
           </td>
           <td><input type="button" value="Update Status"  onclick='popupPage(550,800,&quot;<html:rewrite page="/dms/changeStatus.jsp"/>&quot;);return false;' /> </td>
          </tr>
</table>

</body>
</html>
