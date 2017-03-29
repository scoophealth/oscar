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

<%@ page import="oscar.eform.data.*, oscar.OscarProperties, oscar.eform.*, java.util.*"%>
<%@ page import="java.net.URLEncoder" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
</head>

<script type="text/javascript" language="javascript"> 
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

<iframe id="uploadFrame" name="uploadFrame" frameborder="0" width="800" height="100" src="<%=request.getContextPath()%>/eform/partials/upload_image.jsp"></iframe>	
	
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
				<td title="<%=curimage%>">
					<a href="#"	class="viewImage" onclick="showImage('<%=fileURL%>', '<%="image" + i%>'); return false;"><%=curimage%></a>
				</td>
					
				<td>
					<a href="<%= request.getContextPath() %>/eform/deleteImage.do?filename=<%=URLEncoder.encode(curimage,"UTF-8")%>" class="contentLink"><bean:message key="eform.uploadimages.btnDelete" /></a>
				</td>
			</tr>
			<% } %>
			</tbody>
		</table>


<%@ include file="efmFooter.jspf"%>

<script>
$('#tblImage').dataTable({
	"aaSorting" : [ [ 0, "asc" ] ],
	"bPaginate": false
});

</script>
</body>
</html:html>
