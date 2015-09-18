<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_edoc" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_edoc");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page contentType="text/html" %>
<%@page import="oscar.dms.data.*,java.util.*,oscar.oscarLab.ca.on.CommonLabResultData,org.oscarehr.util.SpringUtils,org.oscarehr.common.dao.QueueDao, oscar.oscarMDS.data.ProviderData" %>
<%@page import="oscar.OscarProperties"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao, org.oscarehr.common.model.Provider" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%
ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
ArrayList<Provider> providers = new ArrayList<Provider>(providerDao.getActiveProviders());
String provider = CommonLabResultData.NOT_ASSIGNED_PROVIDER_NO;

QueueDao queueDao = (QueueDao) SpringUtils.getBean("queueDao");
HashMap queues = queueDao.getHashMapOfQueues();

String queueIdStr = (String) request.getSession().getAttribute("preferredQueue");
int queueId = 1;
if (queueIdStr != null) {
    queueIdStr = queueIdStr.trim();
    queueId = Integer.parseInt(queueIdStr);
}

String context = request.getContextPath();
String resourcePath = context + "/share/documentUploader/";
%>
<!DOCTYPE HTML>
<html lang="en" class="no-js">
<head>
	<meta charset="utf-8">
	<title><bean:message key="inboxmanager.document.title" /></title>
	<link rel="stylesheet" href="<%=context%>/css/cupertino/jquery-ui-1.8.18.custom.css" id="theme">
	<link rel="stylesheet" href="<%=resourcePath%>jquery.fileupload-ui.css">
	<link rel="stylesheet" href="<%=resourcePath%>style.css">
	<link rel="stylesheet" type="text/css" href="<%=context%>/share/css/OscarStandardLayout.css" />
	<script type="text/javascript">
	function setProvider(select){
		jQuery("#provider").val(select.options[select.selectedIndex].value);
	}
	</script>
	<style type="text/css">
	body {
		background-color: #c0c0c0;
	}
	form.file_upload { float:left; clear:none; }
	</style>
</head>
<body>
<div class="ui-widget">
<div id="fileupload">

    <div class="file_upload_buttons fileupload-buttonbar ui-widget-header ui-corner-top">
	    <form action="<%=context%>/dms/documentUpload.do?method=executeUpload" method="POST" enctype="multipart/form-data">
	        <input type="hidden" id="provider" name="provider" value="<%=provider%>" />
			<input type="hidden" name="queue" value="<%=queueId%>"/>
	        <input type="file" name="filedata" multiple>
	        <button type="submit">Upload</button>
	        <div id="add" class="file_upload_label">Add files...</div>
	    </form>
        <button id="start" class="file_upload_start">Start upload</button>
        <button id="cancel" class="file_upload_cancel">Cancel upload</button>
        <span>
			<label style="font-family:Arial; font-weight:normal; font-size:12px" id="provLabel" for="providerDrop" class="fields">Send to Provider:</label>
			<select onchange="javascript:setProvider(this);" id="providerDrop" name="providerDrop">
				<option value="0" <%=("0".equals(provider) ? " selected" : "")%>>None</option>
				<%
				for (int i = 0; i < providers.size(); i++) {
			             	Provider p = providers.get(i);
			    %>
				<option value="<%= p.getProviderNo()%>" <%= (p.getProviderNo().equals(provider) ? " selected" : "")%>><%= p.getLastName()%> <%= p.getFirstName()%></option>
				<%
				}
				%>
			</select>
		</span>
    </div>
    <div class="fileupload-content ui-widget-content ui-corner-bottom">
	    <table class="files">
	        <tr class="file_upload_template" style="display:none;">
	            <td class="file_upload_preview"></td>
	            <td class="file_name"></td>
	            <td class="file_size"></td>
	            <td class="file_upload_progress"><div></div></td>
	            <td class="file_upload_start"><button>Start</button></td>
	            <td class="file_upload_cancel"><button>Cancel</button></td>
	        </tr>
	        <tr class="file_download_template" style="display:none;">
	            <td class="file_download_preview"></td>
	            <td class="file_name"><a></a></td>
	            <td class="file_size"></td>
	            <td> Uploaded successfully</td>
	        </tr>
	    </table>
	    <div class="file_upload_overall_progress"><div style="display:none;"></div></div>
    </div>
</div>
</div>
<script src="<%=context%>/js/jquery-1.7.1.min.js"></script>
<script src="<%=context%>/js/jquery-ui-1.8.18.custom.min.js"></script>
<script src="<%=resourcePath%>jquery.fileupload.js"></script>
<script src="<%=resourcePath%>jquery.fileupload-ui.js"></script>
<script src="<%=resourcePath%>jquery.fileupload-uix.js"></script>
<script type="text/javascript">
jQuery(function () {
	jQuery('#fileupload').fileUploadUIX({ sequentialUploads: true });
	jQuery(".file_upload_label").button({icons: {primary: 'ui-icon-plusthick'}})
});
</script>
<style type="text/css">
body .file_upload .ui-widget input, body .file_upload .ui-widget select, body .file_upload .ui-widget textarea, body .file_upload .ui-widget button {
    font-family: Arial;
    font-size: 12px;
}

</style>
</body>
</html>