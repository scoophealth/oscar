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
<script type="text/javascript" src="<%= request.getContextPath()%>/share/javascript/boxover.js"></script>
<title><bean:message key="eform.uploadhtml.title" /></title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/share/css/OscarStandardLayout.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/share/css/eformStyle.css">
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
  function BackHtml(){
    top.location.href = "../admin/admin.jsp";
  }
  function newWindow(url, id) {
        Popup = window.open(url,id,'toolbar=no,location=no,status=yes,menubar=no, scrollbars=yes,resizable=yes,width=700,height=600,left=200,top=0');
  }
  function confirmNDelete(url) {
    if (confirm("<bean:message key="eform.uploadhtml.confirmDelete"/>")) {
        document.location = url;
    }
  }

  var normalStyle = "eformInputHeading"
  var activeStyle = "eformInputHeading eformInputHeadingActive"
  function closeInputs() {
      document.getElementById("uploadDiv").style.display = 'none';
      document.getElementById("importDiv").style.display = 'none';
      document.getElementById("uploadHeading").className = normalStyle;
      document.getElementById("importHeading").className = normalStyle;
  }

  function openUpload() {
      closeInputs();
      document.getElementById("uploadHeading").className = activeStyle;
      document.getElementById("uploadDiv").style.display = 'block';
  }

  function openImport() {
      closeInputs();
      document.getElementById("importHeading").className = activeStyle;
      document.getElementById("importDiv").style.display = 'block';
  }

  function openCreate(obj) {
      window.location='efmformmanageredit.jsp'
  }

  function openDownload(obj) {
      window.location='efmformmanagerdownload.jsp'
  }
  function eformGenerator(obj){
      window.location='eformGenerator.jsp'
  }

  function doOnLoad() {
    <%String input = request.getParameter("input");
    if (input == null) input = (String) request.getAttribute("input");
    if (input != null && input.equals("import")) {%>
    openImport();
    <%}%>
  }
</script>
<body topmargin="0" leftmargin="0" rightmargin="0" onload="doOnLoad()">
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
                        <table class="eformInputHeadingTable">
                            <tr>
                                <td class="eformInputHeading" style="width: 20px; background-color: white;"> </td>
                                <td class="eformInputHeading eformInputHeadingActive" onclick="openUpload(this)" id="uploadHeading">Upload New eForm</td>
                                <td class="eformInputHeading" onclick="openImport()" id="importHeading">&nbsp; Import eForm</td>
                                <td class="eformInputHeading" onclick="openCreate()" id="createHeading">&nbsp; Create In Editor</td>
                                <td class="eformInputHeading" onclick="openDownload()" id="createHeading">&nbsp; Download eForms</td>
                               <!-- call eform generator-->
                                <td class="eformInputHeading" onClick ="popupPage(400,960,'eformGenerator.jsp');return false;"id="createHeading"> &nbsp; eForm Generator</td>
                                <!--generator-->
                            </tr>
                        </table>
                    <div id="uploadDiv" class="inputDiv">
                        <center>
                        <table style="text-align: center; border-collapse: collapse; border: 0px;">
                                <html:form action="/eform/uploadHtml" method="POST"
                                        onsubmit="return checkFormAndDisable()"
                                        enctype="multipart/form-data">
                                        <span style="color: red; font-size: 10px;"> <html:errors /> </span>
                                        <tr><td class="fieldLabel"><bean:message key="eform.uploadhtml.formName" /></td><td class="fieldLabel"><bean:message key="eform.uploadhtml.formSubject" /></td></tr>
                                        <tr><td><input type="text" name="formName" size="25"></td><td><input type="text" name="formSubject" size="25"></td></tr>
                                        <tr><td colspan="2" style="text-align: left;"><input type="file" name="formHtml" size="50"></td></tr>
                                        
                                        <tr>
	                                        <td style="text-align: left">
	                                        	<input type="checkbox" name="showLatestFormOnly" value="true"/><bean:message key="eform.uploadhtml.showLatestFormOnly"/>
	                                        	&nbsp; &nbsp;
	                                        	<input type="checkbox" name="patientIndependent" value="true"/><bean:message key="eform.uploadhtml.patientIndependent"/>
	                                       	</td>
	                                       	<td style="text-algin: right">
	                                       		eForm role type :
	                                       		<select name="roleType">
	                                       			<option value="" >- select one -</option>
	                                       <%  ArrayList roleList = EFormUtil.listSecRole();
	  											for (int i=0; i<roleList.size(); i++) {    
	  										%>  											
	                                       			<option value="<%=roleList.get(i) %>"><%=roleList.get(i) %></option>
	                                        <%} %></select>
	                                        </td>
                                        </tr>
                                        <tr>
                                            <td colspan="2" style="text-align: right;"><input type="submit" name="subm" value="<bean:message key="eform.uploadhtml.btnUpload"/>"></td>
                                        </tr>
                                </html:form>
                        </table>
                        </center>
                    </div>

                    <div id="importDiv" class="inputDiv" style="display: none;">
                        <table style="text-align: center; border-collapse: collapse; border: 0px;">
                                <form action="../eform/manageEForm.do" method="POST" enctype="multipart/form-data">
                                    <input type="hidden" name="method" value="importEForm">
                                        <font color="red" size="1"> <html:errors /> </font>
                                        <%List<String> importErrors = (List<String>) request.getAttribute("importErrors");
                                        if (importErrors != null && importErrors.size() > 0) {%>
                                        <tr><td style="text-align: left; color: #d97373;">
                                                <%for (String importError: importErrors) {%>
                                                - <%=importError%><br>
                                                <%}%>
                                            </td></tr>
                                        <%}%>
                                        <tr><td class="fieldLabel"><bean:message key="eform.uploadhtml.zipFile" />:</td></tr>
                                        <tr><td colspan="2" style="text-align: left;"><input type="file" name="zippedForm" size="50"></td></tr>
                                        <tr><td colspan="2" style="text-align: left;"><input type="submit" name="subm" value="Import" onclick="this.value = 'Importing...'; this.disabled = true;"></td>
                                        </tr>
                                        <tr><td> </td></tr>
                                        </tr>
                                </form>
                        </table>
                    </div>


        </td>
        <td style="border-left: 2px solid #A6A6A6">
        <table border="0" cellspacing="2" cellpadding="2"
            style="margin-left: 10px" width="100%">
            <tr>
                <td align='left'><a href=# onclick="javascript:BackHtml()"><bean:message key="eform.uploadhtml.btnBack" /></a></td>
            </tr>
            <tr>
                <td align='left'><a href="../eform/efmformmanager.jsp" class="current"><bean:message key="admin.admin.btnUploadForm" />
                </a></td>
            </tr>
            <tr>
                <td align='left'><a href="../eform/efmformmanagerdeleted.jsp"><bean:message key="eform.uploadhtml.btnDeleted" /> </a></td>
            </tr>
            <tr>
                <td align='left'><a href="../eform/efmimagemanager.jsp"><bean:message key="admin.admin.btnUploadImage" /> </a></td>
            </tr>
            <tr>
                <td align='left'><a href="../eform/efmmanageformgroups.jsp"><bean:message key="eform.groups.name" /> </a></td>
            </tr>
        </table>
        </td>
</table>

<table border="0" cellspacing="0" cellpadding="0" width="98%">
    <tr>
        <td><bean:message key="eform.uploadhtml.msgLibrary" /></td>
    </tr>
</table>

<table class="elements" width="98%">
    <tr bgcolor="#CCCCFF">
        <th><a href="efmformmanager.jsp?orderby=form_name"><bean:message
            key="eform.uploadhtml.btnFormName" /></a></th>
        <th><a href="efmformmanager.jsp?orderby=form_subject"><bean:message
            key="eform.uploadhtml.btnSubject" /></a></th>
        <th><a href="efmformmanager.jsp?orderby=file_name"><bean:message
            key="eform.uploadhtml.btnFile" /></a></th>
        <th><a href="efmformmanager.jsp?"><bean:message
            key="eform.uploadhtml.btnDate" /></a></th>
        <th><bean:message key="eform.uploadhtml.btnTime" /></th>
        <th><bean:message key="eform.uploadhtml.btnRoleType"/></th>
        <th><bean:message key="eform.uploadhtml.msgAction" /></th>
        <th><bean:message key="eform.uploadhtml.editform" /></th>
    </tr>
    <%
    ArrayList<HashMap<String, ? extends Object>> eForms = EFormUtil.listEForms(orderBy, EFormUtil.CURRENT);
  for (int i=0; i<eForms.size(); i++) {
	  HashMap<String, ? extends Object> curForm = eForms.get(i);
%>
    <tr style="background-color: <%= ((i%2) == 1)?"#F2F2F2":"white"%>;">
        <td width="25%" style="padding-left: 4px;"><a href="#"
            onclick="newWindow('efmshowform_data.jsp?fid=<%=curForm.get("fid")%>', '<%="Form"+i%>'); return false;"><%=curForm.get("formName")%></a></td>
        <td width="30%" style="padding-left: 4px"><%=curForm.get("formSubject")%> </td>
        <td width="25%" style="padding-left: 4px"><%=curForm.get("formFileName")%></td>
        <td nowrap align='center' width="10%"><%=curForm.get("formDate")%></td>
        <td nowrap align='center' width="10%"><%=curForm.get("formTime")%></td>
        <td nowrap align='center' width="10%"><%=curForm.get("roleType")%></td>
        <td nowrap align='center'>
                    <a href="javascript:void();" onclick="document.location.href='efmSendform.jsp?fid=<%=curForm.get("fid")%>'">Upload</a>
                    <a href="#" onclick="document.location.href='../eform/manageEForm.do?method=exportEForm&fid=<%=curForm.get("fid")%>'"><bean:message key="eform.uploadhtml.btnExport" /></a>
                    <a href="#" onclick="confirmNDelete('../eform/delEForm.do?fid=<%=curForm.get("fid")%>')"><bean:message key="eform.uploadhtml.btnDelete" /></a>
                </td>
        <td nowrap align="center"><a
            href="efmformmanageredit.jsp?fid=<%= curForm.get("fid")%>"><bean:message key="eform.uploadhtml.editform" /></a></td>
    </tr>
    <% } %>
</table>
</center>

</body>
</html:html>
