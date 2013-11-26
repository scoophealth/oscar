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
<%@ page
	import="oscar.eform.data.*, oscar.OscarProperties, oscar.eform.*, java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
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

<%@ include file="efmTopNav.jspf"%>

<h3>Image Library</h3>


		<html:errors />
		<html:form action="/eform/imageUpload" enctype="multipart/form-data" method="post">
		<bean:message key="eform.uploadimages.msgFileName" /><br>
	
		<input type="file" name="image" size="40" >
		<input type="button" class="btn" name="subm" value="<bean:message key="eform.uploadimages.btnUpload"/>" onclick="javascript:checkFormAndDisable();">

		</html:form>	
	
		<table class="table table-condensed table-striped table-hover" id="tblImage">
		<thead>
			<tr>
				<th><bean:message key="eform.uploadimages.msgimgName" /></th>
				<th><bean:message key="eform.uploadimages.msgImgAction" /></th>
			</tr>
		</thead>
		
		 <tbody>
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
       
			<tr>
				<td>
					<a href="#"	class="viewImage" onclick="showImage('<%=fileURL%>', '<%="image" + i%>'); return false;"><%=curimage%></a>
				</td>
					
				<td>
					<a href="#" onclick="javascript: deleteImg('<%= curimage%>'); return false;"><bean:message key="eform.uploadimages.btnDelete" /></a>
				</td>
			</tr>
			<% } %>
			</tbody>
		</table>





<%@ include file="efmFooter.jspf"%>

<script>
$('#tblImage').dataTable({
	"aaSorting" : [ [ 0, "asc" ] ],
	"fnDrawCallback": bindLinks
});

function bindLinks(oSettings){
	registerHref('click', 'a.viewImage', '#dynamic-content');
}
</script>
</body>
</html:html>
