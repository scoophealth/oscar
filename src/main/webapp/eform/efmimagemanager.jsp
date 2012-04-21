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
<%@ page
	import="oscar.eform.data.*, oscar.OscarProperties, oscar.eform.*, java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="eform.uploadhtml.title" /></title>
<link rel="stylesheet" href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" href="../share/css/eforms.css">
</head>
<script type="text/javascript" language="javascript">
  function checkFormAndDisable(){
    if(document.forms[0].image.value==""){ 
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
  function showImage(url, id) {
        Popup = window.open(url,id,'toolbar=no,location=no,status=yes,menubar=no, scrollbars=yes,resizable=yes,width=700,height=600,left=200,top=0');
  }
  function deleteImg(image) {
    if (confirm("<bean:message key="eform.uploadimages.imgDelete"/>")) {
        document.location = "../eform/deleteImage.do?filename=" + image;
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
		<center><html:errors />
		<table cellspacing="2" cellpadding="2" width="80%" border="0"
			BGCOLOR="<%=weakColor%>">
			<html:form action="/eform/imageUpload" enctype="multipart/form-data"
				method="post">
				<tr>
					<td align='right' nowrap><b><bean:message
						key="eform.uploadimages.msgFileName" /> </b></td>
					<td><input type="file" name="image" size="40"></td>
				</tr>
				<tr>
					<td></td>
					<td><input type="button" name="subm"
						value="<bean:message key="eform.uploadimages.btnUpload"/>"
						onclick="javascript:checkFormAndDisable();"></td>
				</tr>
			</html:form>
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
				<td align='left'><a href="../eform/efmimagemanager.jsp"
					class="current"><bean:message key="admin.admin.btnUploadImage" />
				</a></td>
			</tr>
			<tr>
				<td align='left'><a href="../eform/efmmanageformgroups.jsp"><bean:message
					key="eform.groups.name" /> </a></td>
			</tr>
		</table>
		</td>
	<tr>
		<td>
		<center>
		<table class="elements" width="78%">
			<tr>
				<td style="border: none;"><bean:message
					key="eform.uploadhtml.msgLibrary" /></td>
			</tr>
			<tr bgcolor="#CCCCFF">
				<th><bean:message key="eform.uploadimages.msgimgName" /></th>
				<th><bean:message key="eform.uploadimages.msgImgAction" /></th>
			</tr>
			<%
          //OscarProperties op = OscarProperties.getInstance();
          //String project_home = op.getProperty("project_home");
          ArrayList images = EFormUtil.listImages();
          request.setAttribute("images", images);
          for (int i=0; i<images.size(); i++) {
              String fileURL="../eform/displayImage.do?imagefile="+images.get(i);
              //String fileURL="/OscarDocument/" + project_home + "/eform/images/"+images.get(i);
              String curimage = (String) images.get(i);
        %>
			<tr style="background-color: <%= ((i%2) == 1)?"#F2F2F2":"white"%>;">
				<td width="90%" style="padding-left: 4px;"><a href="#"
					onclick="showImage('<%=fileURL%>', '<%="image" + i%>'); return false;"><%=curimage%></a></td>
				<td nowrap align='center'><a href="#"
					onclick="javascript: deleteImg('<%= curimage%>'); return false;"><bean:message
					key="eform.uploadimages.btnDelete" /></a></td>
			</tr>
			<% } %>
		</table>
		</center>
		</td>
		<td>&nbsp;</td>
	</tr>
</table>
</body>
</html:html>
