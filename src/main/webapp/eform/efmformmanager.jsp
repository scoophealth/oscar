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


<style>
.uploadEformTitle{display:inline-block;}
</style>
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
        Popup = window.open(url,id,'toolbar=no,location=no,status=yes,menubar=no, scrollbars=yes,resizable=yes,width=900,height=600,left=200,top=0');
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

  function doOnLoad() {
    <%String input = request.getParameter("input");
    if (input == null) input = (String) request.getAttribute("input");
    if (input != null && input.equals("import")) {%>
    openImport();
    <%}%>
  }

$(function ()  { 

	  $("[rel=popover]").popover(); 

});

</script>

<body>


<%@ include file="efmTopNav.jspf"%>
    
<h3 style='display:inline;padding-right:10px'><bean:message key="eform.uploadhtml.msgLibrary" /></h3> <a href="<%= request.getContextPath() %>/eform/efmformmanagerdeleted.jsp" class="contentLink">View Deleted<!--<bean:message key="eform.uploadhtml.btnDeleted" />--> </a> 

    
<ul class="nav nav-pills" id="eformOptions">
<li ><a href="#upload" >Upload</a></li>
<li><a href="#import">Import</a></li>
<li><a href="#download" onclick="newWindow('<%= request.getContextPath() %>/eform/efmformmanagerdownload.jsp', 'eformDownload'); return false;">Download</a></li>
</ul>
 
<div class="tab-content">
<div class="tab-pane" id="upload">
<div class="well">

<html:form action="/eform/uploadHtml" method="POST" onsubmit="return checkFormAndDisable()" enctype="multipart/form-data">
<div class="alert alert-error" style="display:none"> <html:errors /> </div>
                                       
                                        <div class='uploadEformTitle'>
                                        <bean:message key="eform.uploadhtml.formName" /><br>
                                        <input type="text" name="formName" size="30">
                                        </div>
                                        
                                        <div class='uploadEformTitle'>
                                        <bean:message key="eform.uploadhtml.formSubject" /><br>
                                        <input type="text" name="formSubject" size="30">
                                        </div>
                                        
                                        <div class='uploadEformTitle'>
                                        <bean:message key="eform.uploadhtml.btnRoleType"/><br>
                                        <select name="roleType">
                                        <option value="" >- select one -</option>
                                       <%  ArrayList roleList = EFormUtil.listSecRole();
  											for (int i=0; i<roleList.size(); i++) {    
  										%>  											
                                        	
                                        		<option value="<%=roleList.get(i) %>"><%=roleList.get(i) %></option>
                                        	
                                        <%} %>
                                        </select>
                                        </div>
                                        
                                        <div class='uploadEformTitle'>
                                        <input type="checkbox" name="showLatestFormOnly" value="true"/><bean:message key="eform.uploadhtml.showLatestFormOnly"/><br />
                                        <input type="checkbox" name="patientIndependent" value="true"/><bean:message key="eform.uploadhtml.patientIndependent"/>
                                        </div>
                                        
                                        <input type="file" name="formHtml" size="50">
                                        <input type="submit" name="subm" class="btn btn-primary" value="<bean:message key="eform.uploadhtml.btnUpload"/>">
               
</html:form>
</div>
</div>

<div class="tab-pane" id="import">
<div class="well">
                        
                                <form action="<%=request.getContextPath()%>/eform/manageEForm.do" method="POST" enctype="multipart/form-data" id="eformImportForm">
                                    <input type="hidden" name="method" value="importEForm">
                                        <font color="red" size="1"> <html:errors /> </font>
                                        <%List<String> importErrors = (List<String>) request.getAttribute("importErrors");
                                        if (importErrors != null && importErrors.size() > 0) {%>
                                        
                                                <%for (String importError: importErrors) {%>
                                                - <%=importError%><br>
                                                <%}%>
                                           
                                        <%}%>
                                        Import eForm: <br>
                                        
                                        
                                        <input type="file" name="zippedForm" size="50">
                                        <input type="submit" name="subm" value="Import" class="btn" onclick="this.value = 'Importing...'; this.disabled = true;"><br>
                                        <span class="label label-info">Info: </span> <strong>When importing the file format is required to be a zip file.</strong>
                                </form>

</div>
</div>

<div class="tab-pane" id="download">

</div>

</div><!-- tab content eformOptions -->
    
<table class="table table-condensed table-striped">
<thead>
    <tr>
        <th><!--<a href="<%= request.getContextPath() %>/eform/efmformmanager.jsp?orderby=file_name" class="contentLink"class="contentLink"><bean:message key="eform.uploadhtml.btnFile" />--></a></th>

        <th style="width:15%"><a href="<%= request.getContextPath() %>/eform/efmformmanager.jsp?orderby=form_name" class="contentLink"><bean:message key="eform.uploadhtml.btnFormName" /></a></th>
        <th style="width:20%"><a href="<%= request.getContextPath() %>/eform/efmformmanager.jsp?orderby=form_subject" class="contentLink"><bean:message key="eform.uploadhtml.btnSubject" /></a></th>

        <th><a href="<%= request.getContextPath() %>/eform/efmformmanager.jsp?" class="contentLink"><bean:message key="eform.uploadhtml.btnDate" /></a></th>
        <th><bean:message key="eform.uploadhtml.btnTime" /></th>
        <th><bean:message key="eform.uploadhtml.btnRoleType"/></th>
        <th style="width:15%"><bean:message key="eform.uploadhtml.msgAction" /></th>
        <th style="width:10%">Send <i class="icon-question-sign" rel="popover" data-html="true" data-placement="bottom" data-animation="true" data-trigger="hover" data-content="This is an online utility designed for users to easily share eForms to the OSCAR community." data-original-title="Send eForm to Emporium"></i></th>
    </tr>
</thead>

<tbody>
    <%
    ArrayList<HashMap<String, ? extends Object>> eForms = EFormUtil.listEForms(orderBy, EFormUtil.CURRENT);
  for (int i=0; i<eForms.size(); i++) {
	  HashMap<String, ? extends Object> curForm = eForms.get(i);
%>
    <tr>
        <td><%if(curForm.get("formFileName").toString().length()!=0){%><i class="icon-file" title="<%=curForm.get("formFileName").toString()%>"></i><%}%></td>
        <td>
        <a href="#" onclick="newWindow('<%= request.getContextPath() %>/eform/efmshowform_data.jsp?fid=<%=curForm.get("fid")%>', '<%="Form"+i%>'); return false;"><%=curForm.get("formName")%></a>
        </td>
        <td><%=curForm.get("formSubject")%> </td>
        <td align='center' ><%=curForm.get("formDate")%></td>
        <td align='center' ><%=curForm.get("formTime")%></td>
        <td align='center' ><%=curForm.get("roleType")%></td>
        <td align='center'>
                    
                
<a href="<%= request.getContextPath() %>/eform/efmformmanageredit.jsp?fid=<%= curForm.get("fid")%>" class="contentLink" style="padding-right:6px" title='<bean:message key="eform.uploadhtml.editform" /><%=curForm.get("formName")%>'><i class="icon-pencil" title="<bean:message key="eform.uploadhtml.editform" />"></i></a>

                 
<a href='<%= request.getContextPath() %>/eform/manageEForm.do?method=exportEForm&fid=<%=curForm.get("fid")%>' style="padding-right:6px" title='<bean:message key="eform.uploadhtml.btnExport" /> <%=curForm.get("formName")%>' ><i class="icon-download-alt" title="<bean:message key="eform.uploadhtml.btnExport" />"></i></a>
                    

<a href='<%= request.getContextPath() %>/eform/delEForm.do?fid=<%=curForm.get("fid")%>' style="padding-right:6px" title='<bean:message key="eform.uploadhtml.btnDelete" /> <%=curForm.get("formName")%>' class="contentLink"><i class="icon-trash" title="<bean:message key="eform.uploadhtml.btnDelete" />"></i></a>
		</td>			
		
		<td align='center'><a href="<%= request.getContextPath() %>/eform/efmSendform.jsp?fid=<%=curForm.get("fid")%>" title='Send <%=curForm.get("formName")%> to Emporium' class="contentLink"><i class="icon-share" title="send"></i></a>
					
    </tr>
    <% } %>
</tbody>
</table>

<%@ include file="efmFooter.jspf"%>

<script>
    $('#eformOptions a').click(function (e) {
    e.preventDefault();
    $(this).tab('show');
    });


registerFormSubmit('eformImportForm', 'dynamic-content');

</script>
</body>
</html:html>
