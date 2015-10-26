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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.eform" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.eform");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="oscar.eform.data.*, oscar.eform.*, java.util.*"%>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
String orderByRequest = request.getParameter("orderby");
String orderBy = "";
if (orderByRequest == null) orderBy = EFormUtil.DATE;
else if (orderByRequest.equals("form_subject")) orderBy = EFormUtil.SUBJECT;
else if (orderByRequest.equals("form_name")) orderBy = EFormUtil.NAME;
else if (orderByRequest.equals("file_name")) orderBy = EFormUtil.FILE_NAME;
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="eform.uploadhtml.title" /></title>

</head>
<script language="javascript">
  function checkFormAndDisable(){
    if(document.forms[0].formHtml.value==""){ 
      alert("<bean:message key="eform.uploadhtml.msgFileMissing"/>");
    } else {
      document.forms[0].subm.value = "<bean:message key="eform.uploadimages.processing"/>";
      document.forms[0].subm.disabled = true;
      document.forms[0].submit();
    } 
  }

  function newWindow(url, id) {
        Popup = window.open(url,id,'toolbar=no,location=no,status=yes,menubar=no, scrollbars=yes,resizable=yes,width=700,height=600,left=200,top=0');
  }
  
  function confirmNRestore(url) {
    if (confirm("<bean:message key="eform.calldeletedformdata.confirmRestore"/>")) {
        document.location = url;
    }
  }
</script>
<body>


<%@ include file="efmTopNav.jspf"%>

<h3><bean:message key="eform.calldeletedformdata.title" /></h3>


<table class="table table-condensed table-striped table-hover" id="tblDeletedEforms">
	<tr>
		<th><a href="<%= request.getContextPath() %>/eform/efmformmanagerdeleted.jsp?orderby=form_name" class="contentLink"><bean:message	key="eform.uploadhtml.btnFormName" /></a></th>
		<th><a href="<%= request.getContextPath() %>/eform/efmformmanagerdeleted.jsp?orderby=form_subject" class="contentLink"><bean:message key="eform.uploadhtml.btnSubject" /></a></th>
		<th><a href="<%= request.getContextPath() %>/eform/efmformmanagerdeleted.jsp?orderby=file_name" class="contentLink"><bean:message key="eform.uploadhtml.btnFile" /></a></th>
		<th><a href="<%= request.getContextPath() %>/eform/efmformmanagerdeleted.jsp?" class="contentLink"><bean:message key="eform.uploadhtml.btnDate" /></a></th>
		<th><bean:message key="eform.uploadhtml.btnTime" /></th>
		<th><bean:message key="eform.uploadhtml.msgAction" /></th>
	</tr>
	<%
	ArrayList<HashMap<String, ? extends Object>> eForms = EFormUtil.listEForms(LoggedInInfo.getLoggedInInfoFromSession(request),orderBy, EFormUtil.DELETED);
  for (int i=0; i<eForms.size(); i++) {
	  HashMap<String, ? extends Object> curForm =  eForms.get(i);
%>
	<tr>
		<td><a href="#" class="viewEform" onclick="newWindow('<%= request.getContextPath() %>/eform/efmshowform_data.jsp?fid=<%=curForm.get("fid")%>', '<%="FormD"+i%>'); return false;"><%=curForm.get("formName")%></a></td>
		<td><%=curForm.get("formSubject")%>&nbsp;</td>
		<td><%=curForm.get("formFileName")%></td>
		<td ><%=curForm.get("formDate")%></td>
		<td><%=curForm.get("formTime")%></td>
		<td><a href='<%= request.getContextPath() %>/eform/restoreEForm.do?fid=<%=curForm.get("fid")%>' class="contentLink">
			<bean:message key="eform.calldeletedformdata.btnRestore" />
	           </a>
		</td>
	</tr>
	<% } %>
</table>

<%@ include file="efmFooter.jspf"%>

<script>
$('#tblDeletedEforms').dataTable({
	"aaSorting" : [ [ 0, "asc" ] ],
	"fnDrawCallback": bindLinks
});

function bindLinks(oSettings){
	registerHref('click', 'a.viewEform', '#dynamic-content');
}
</script>
</body>
</html:html>
