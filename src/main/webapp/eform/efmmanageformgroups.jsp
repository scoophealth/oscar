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
<%
  
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>
<%@ page import="oscar.eform.data.*, oscar.eform.*, java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
  ArrayList<Hashtable<String,String>> groups = EFormUtil.getEFormGroups();
  ArrayList<HashMap<String, ? extends Object>> forms = EFormUtil.listEForms(EFormUtil.NAME, EFormUtil.CURRENT);
  String groupView = request.getParameter("group_view");
  if (groupView == null) {
      groupView = (String) request.getAttribute("group_view");
  }
  if (groupView == null) {
      if( groups.size() > 0 ) {
        Hashtable tmphash = (Hashtable) groups.get(0);
        groupView = (String) tmphash.get("groupName");
      }
      else {
        groupView = "";
      }
  }
  
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
<link rel="stylesheet" href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" href="../share/css/eforms.css">
</head>
<script language="javascript">
  function checkFormAndDisable(){
    if(document.forms[0].groupName.value==""){ 
      alert("<bean:message key="eform.groups.errors.blankGroupName"/>");
    } else {
      document.forms[0].subm.value = "<bean:message key="eform.uploadimages.processing"/>";
      document.forms[0].subm.disabled = true;
      document.forms[0].submit();
    } 
  }
  function BackHtml(){
    top.location.href = "../admin/admin.jsp";
  }
  function newWindow(url, id) {
        Popup = window.open(url,id,'toolbar=no,location=no,status=yes,menubar=no, scrollbars=yes,resizable=yes,width=700,height=600,left=200,top=0');
  }
  function submitAdd() {
        document.forms[2].subm.value = "<bean:message key="eform.uploadimages.processing"/>";
        document.forms[2].subm.disabled = true;
  }
  function delGroup() {
      if (confirm("<bean:message key="eform.groups.delGroupConfirm"/>")) {
        document.forms[1].del.value="<bean:message key="eform.uploadimages.processing"/>";
        document.forms[1].del.disabled = true;
        var groupName = document.forms[1].group_view.value;
        document.location = "../eforms/delGroup.do?group_name=" + groupName;
      }
  }
</script>
<body topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="98%">
	<tr bgcolor="#CCCCFF">
		<th><font face="Helvetica"><bean:message
			key="eform.uploadhtml.msgUploadEForm" /></font></th>
	</tr>
</table>
<table border="0" cellpadding="0" cellspacing="5" width="98%">
	<tr>
		<td>
		<center>
		<table cellspacing="2" cellpadding="2" width="90%" border="0"
			style="margin-top: 10px" BGCOLOR="#EEEEFF">


			<html:form action="/eform/addGroup" method="get"
				onsubmit="return checkFormAndDisable()">
				<tr>
					<td width="35%" align='right' nowrap><strong><bean:message
						key="eform.groups.addNewGroup" /></strong></td>
					<td nowrap><input type="text" name="groupName" size="30"></td>
					<td nowrap width="35%"><input type="submit" name="subm"
						value="<bean:message key="eform.groups.addGroup"/>"></td>
				</tr>
			</html:form>

			<form action="../eform/efmmanageformgroups.jsp" name="groupSelect"
				method="get">
			<tr>
				<td nowrap align='right' nowrap><strong><bean:message
					key="eform.groups.selectViewGroup" /></strong></td>
				<td nowrap><select name="group_view"
					onchange="this.form.submit()">
					<%                              for (int i=0; i<groups.size(); i++) { 
                                   Hashtable curhash = (Hashtable) groups.get(i);
                                   String selected = "";
                                      if (((String) curhash.get("groupName")).equals(groupView)) {
                                          selected = " selected";
                                }
%>
					<option value="<%= (String) curhash.get("groupName")%>"
						<%= selected%>><%= (String) curhash.get("groupName")%> (<%= (String) curhash.get("count") %>)</option>
					<% } %>
				</select></td>
				<td nowrap><input type="button" name="del"
					value="<bean:message key="eform.groups.delGroup"/>"
					onclick="delGroup()"></td>
			</tr>
			</form>


			<%            if (!groupView.equals("")) {                      %>
			<html:form action="/eform/addToGroup" method="get"
				onsubmit="submitAdd()">
				<tr>
					<td align="right" nowrap><strong><bean:message
						key="eform.groups.addToGroup" /></strong></td>
					<td nowrap><html:select property="fid">
						<%                             
							for (int i=0; i<forms.size(); i++) {
							HashMap<String, ? extends Object> curhash = forms.get(i);
%>
						<html:option value='<%= (String) curhash.get("fid")%>'><%= (String) curhash.get("formName")%> | <%= (String) curhash.get("formDate")%></html:option>
						<% } %>
					</html:select>
					<td nowrap><input type="hidden" name="groupName"
						value="<%= groupView%>"> <input type="submit" name="subm"
						value="<bean:message key="eform.groups.addToGroup"/>"></td>
				</tr>
			</html:form>
			<%            }     %>


		</table>
		</center>
		</td>
		<td style="border-left: 2px solid #A6A6A6">
		<table border="0" cellspacing="2" cellpadding="2"
			style="margin-left: 10px" width="100%">
			<tr>
				<td align='left'><a href=# onclick="javascript:BackHtml()"><bean:message
					key="eform.uploadhtml.btnBack" /></a></td>
			</tr>
			<tr>
				<td align='left'><a href="../eform/efmformmanager.jsp"><bean:message
					key="admin.admin.btnUploadForm" /> </a></td>
			</tr>
			<tr>
				<td align='left'><a href="../eform/efmformmanagerdeleted.jsp"><bean:message
					key="eform.uploadhtml.btnDeleted" /> </a></td>
			</tr>
			<tr>
				<td align='left'><a href="../eform/efmimagemanager.jsp"><bean:message
					key="admin.admin.btnUploadImage" /> </a></td>
			</tr>
			<tr>
				<td align='left'><a href="../eform/efmmanageformgroups.jsp"
					class="current"><bean:message key="eform.groups.name" /> </a></td>
			</tr>
		</table>
		</td>
</table>

<table border="0" cellspacing="0" cellpadding="0" width="98%">
	<tr>
		<td><bean:message key="eform.groups.contents" /> '<%=groupView%>'
		</td>
	</tr>
</table>

<table class="elements" width="98%">
	<tr bgcolor="#CCCCFF">
		<th width="25%"><a
			href="../eform/efmmanageformgroups.jsp?orderby=form_name&group_view=<%=groupView%>"><bean:message
			key="eform.uploadhtml.btnFormName" /></a></th>
		<th width="30%"><a
			href="../eform/efmmanageformgroups.jsp?orderby=form_subject&group_view=<%=groupView%>"><bean:message
			key="eform.uploadhtml.btnSubject" /></a></th>
		<th width="25%"><a
			href="../eform/efmmanageformgroups.jsp?orderby=file_name&group_view=<%=groupView%>"><bean:message
			key="eform.uploadhtml.btnFile" /></a></th>
		<th width="10%"><a
			href="../eform/efmmanageformgroups.jsp?group_view=<%=groupView%>"><bean:message
			key="eform.uploadhtml.btnDate" /></a></th>
		<th width="10%"><bean:message key="eform.uploadhtml.msgAction" />
		</th>
	</tr>
	<%
  if (!groupView.equals("")) {
      ArrayList<HashMap<String, ? extends Object>> eForms = EFormUtil.listEForms(orderBy, EFormUtil.CURRENT, groupView,null);
      if (eForms.size() > 0) {
        for (int i=0; i<eForms.size(); i++) {
        	HashMap<String, ? extends Object> curForm = eForms.get(i);
%>
	<tr style="background-color: <%= ((i%2) == 1)?"#F2F2F2":"white"%>;">
		<td style="padding-left: 4px;"><a href="#"
			onclick="newWindow('efmshowform_data.jsp?fid=<%=curForm.get("fid")%>', '<%="FormG" + i%>'); return false;"><%=curForm.get("formName")%></a></td>
		<td style="padding-left: 4px"><%=curForm.get("formSubject")%>&nbsp;</td>
		<td style="padding-left: 4px"><%=curForm.get("formFileName")%></td>
		<td nowrap align='center'><%=curForm.get("formDate")%></td>
		<td nowrap align="center"><a
			href="../eforms/removeFromGroup.do?fid=<%=curForm.get("fid")%>&groupName=<%=groupView%>"><bean:message
			key="eform.groups.removeFromGroup" /></a></td>
	</tr>
	<%          } 
        } else { %>
	<tr align="center">
		<td colspan="5"><bean:message key="eform.groups.noFormsInGroup" /></td>
	</tr>
	<%      }
  } else {%>
	<tr align="center">
		<td colspan="5"><font color="red"><bean:message
			key="eform.groups.noGroupMsg" /></font></td>
	</tr>
	<% } %>
</table>
</center>

</body>
</html:html>
