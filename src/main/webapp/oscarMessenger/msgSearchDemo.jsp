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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_msg" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_msg");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="java.lang.*" errorPage="errorpage.jsp"%>
<%@ page import="oscar.OscarProperties"%>
<%
	String demographic_no = request.getParameter("demographic_no") ;
        boolean firstSearch  = request.getParameter("firstSearch") == null ? false : (request.getParameter("firstSearch")).equalsIgnoreCase("true")?true:false;    

%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Demographic Search</title>
</head>
<body
	onload="<% if ( firstSearch) { %> document.forms[0].submit() <% } %>">
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>


<script language="JavaScript">
function searchInactive() {
    document.titlesearch.ptstatus.value="inactive";
    if (checkTypeIn()) document.titlesearch.submit();
}

function searchAll() {
    document.titlesearch.ptstatus.value="";
    if (checkTypeIn()) document.titlesearch.submit();
}

function checkTypeIn() {
	return true;
}


function write2Parent(keyword, demographic_no){
    
    opener.document.forms[0].demographic_no.value = demographic_no;
    opener.document.forms[0].selectedDemo.value = keyword;
    opener.document.forms[0].keyword.value = "";
    //opener.setTimeout("document.encForm.enTextarea.scrollTop=2147483647", 0);  // setTimeout is needed to allow browser to realize that text field has been updated 
    opener.document.forms[0].keyword.focus();
 }



</script>

<table BORDER="0" CELLPADDING="0" CELLSPACING="2" WIDTH="100%"
	bgcolor="#CCCCFF">
	<form method="post" name="titlesearch"
		action="../demographic/demographiccontrol.jsp"
		onsubmit="return checkTypeIn()">
	<tr>
		<td colspan="6" class="RowTop"><b><bean:message
			key="demographic.zdemographicfulltitlesearch.msgSearch" /></b></td>
	</tr>
	<% 
        String keyword = request.getParameter("keyword");

        if (keyword == null) {
         keyword = "";
        }
     %>
	<tr>
		<td>
		<table bgcolor="white" width="100%">
			<tr>
				<td width="10%" nowrap></td>
				<td nowrap></td>
				<td nowrap></td>
				<td valign="middle" rowspan="2" ALIGN="left"><input type="text"
					NAME="keyword" VALUE="<%=keyword%>" SIZE="17" MAXLENGTH="100">
<%
    String searchMode = request.getParameter("search_mode");
    if (searchMode == null || searchMode.isEmpty()) {
        searchMode = OscarProperties.getInstance().getProperty("default_search_mode","search_name");
    }
%>
				<input type="hidden" name="outofdomain" value="">
				<input type="hidden" name="search_mode" value="<%=searchMode%>">
				<INPUT TYPE="hidden" NAME="orderby" VALUE="last_name, first_name">
				<INPUT TYPE="hidden" NAME="dboperation" VALUE="search_titlename">
				<INPUT TYPE="hidden" NAME="limit1" VALUE="0"> <INPUT
					TYPE="hidden" NAME="limit2" VALUE="10"> <INPUT
					TYPE="hidden" NAME="displaymode" VALUE="Search"> <INPUT
					TYPE="hidden" NAME="ptstatus" VALUE="active"> <INPUT
					TYPE="hidden" NAME="fromMessenger" VALUE="true"> <INPUT
					TYPE="SUBMIT"
					VALUE="<bean:message key="demographic.zdemographicfulltitlesearch.msgSearch" />"
					SIZE="17"
					TITLE="<bean:message key="demographic.zdemographicfulltitlesearch.tooltips.searchActive"/>">
				&nbsp;&nbsp;&nbsp; <INPUT TYPE="button" onclick="searchInactive();"
					TITLE="<bean:message key="demographic.zdemographicfulltitlesearch.tooltips.searchInactive"/>"
					VALUE="<bean:message key="demographic.search.Inactive"/>">
				<INPUT TYPE="button" onclick="searchAll();"
					TITLE="<bean:message key="demographic.zdemographicfulltitlesearch.tooltips.searchAll"/>"
					VALUE="<bean:message key="demographic.search.All"/>"></td>
			</tr>
			<tr bgcolor="white">
				<td nowrap></td>
				<td nowrap></td>
				<td nowrap></td>
			</tr>
		</table>
		</td>
	</tr>
	</form>
</table>
<script>
    
    if ( <%=demographic_no%> != null ) {
        write2Parent("<%=keyword%>", "<%=demographic_no%>" );
        self.window.close();    
    }
    </script>

</body>
</html>
