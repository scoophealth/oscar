<%--

    Copyright (c) 2014-2015. KAI Innovations Inc. All Rights Reserved.
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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
  
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.oscar-emr.com/tags/integration" prefix="i"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.billing&type=_billing");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="java.util.*,org.oscarehr.integration.mcedt.web.ActionUtils" %>


<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
    <title>MCEDT</title>
    <link href="web/css/kai_mcedt.css" rel="stylesheet" type="text/css">
    <link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700,900" rel="stylesheet" type="text/css">
	
	<% String tab = ((request.getParameter("tab")==null ||request.getParameter("tab").equals(""))?"first":request.getParameter("tab"));
		String tabChange = ((request.getParameter("tabChange")==null)?"":request.getParameter("tabChange"));
		List<String> serviceIds=ActionUtils.getServiceIds();
		request.setAttribute("serviceIds", serviceIds);
		String defaultId=ActionUtils.getDefaultServiceId();
		request.setAttribute("defaultId", defaultId);
		%>
	
	<script src="<%= request.getContextPath() %>/js/jquery-1.7.1.min.js"></script> 
	<script type="text/javascript" charset="utf-8">
		$(function () {
			var tabContainers = $('div.tabs > div');
			var tab = '<%=tab %>';
			//if (tab == 'download') tabContainers.hide().filter('#download').show();
			//else if (tab == 'upload') tabContainers.hide().filter('#upload').show();
			//else if (tab == 'sent') tabContainers.hide().filter('#sent').show();
			//else tabContainers.hide().filter('#first').show();
			
			$('div.tabs ul.tabNavigation a').click(function () {
				tabContainers.hide();
				tabContainers.filter(this.hash).show();
				$('div.tabs ul.tabNavigation a').removeClass('selected');
				$(this).addClass('selected');
				
				if(this.hash=="#download")
		           {					  
					ShowSpin(true);
					return userDownload();								
					  
		           }
				else if(this.hash=="#sent")
		           {
					ShowSpin(true);
					return userSent();
		              
		           }
				else if(this.hash=="#upload")
		           {
					ShowSpin(true);
					return userUpload();
		              
		           }
					
				
				return false;
			});
			
			// setting the service Id:
			var tabChange = '<%=tabChange%>';
			if (tabChange == "true") {
				var val = "<%=defaultId%>";
			    var sel = document.getElementById('serviceIdSent');
			    var opts = sel.options;
			    for(var opt, j = 0; opt = opts[j]; j++) {
			        if(opt.value == val) {
			            sel.selectedIndex = j;
			            break;
			        }
			    }
			    var sel = document.getElementById('serviceId');
			    var opts = sel.options;
			    for(var opt, j = 0; opt = opts[j]; j++) {
			        if(opt.value == val) {
			            sel.selectedIndex = j;
			            break;
			        }
			    }
			}
		});
		
		function userDownload() {			
			var method = jQuery("#method");
			method.val('loadDownloadList');
			
			var form = jQuery("#form");
			form.submit();			
			return false;
		}
		
		function userSent() {			
			var method = jQuery("#method");
			method.val('loadSentList');
			
			var form = jQuery("#form");
			form.submit();			
			return false;
		}
		function userUpload() {
			window.location.href = "upload.do";
			return false;
		}
		
		function changePass() {
			window.location.href = "kaichpass.do";
			return false;
		}
		function autoDownload(control) {
			window.location.href = "kaiautodl.do";
			return false;
		}
		
		function downloadSelected(control) {
			return submitForm('download', control);
		}
		
		 function submitSelected(control) {
			window.location.href = "autoUpload.do";
			return false;
		} 

				
		/* function getInfo(resourceId, control) {
			if (control) {
				control.disabled = true;
			}
			window.location.href = "info.do?resourceId=" + resourceId;
			return false;
		} */		
		
		function submitForm(methodType, control){
			if (control) {
				control.disabled = true;
			}
			
			var method = jQuery("#method");
			method.val(methodType);
			
			var form = jQuery("#form");
			form.submit();
			return true;
		}
	</script>
</head>
<body id="page">
<jsp:include page="spinner.jsp" flush="true"/>
    <div class="tabs">
        <div id="tabWrapper" class="show tabWrapper">
	        <ul class="tabNavigation">
	            <li><a href='#first' <%=((tab==null ||tab.equals("first"))?"class='selected'":"") %> >Menu</a></li>
            <li><a href='#upload' <%=((tab.equals("upload"))?"class='selected'":"") %> >Upload</a></li>
            <li><a href='#sent' <%=((tab.equals("sent"))?"class='selected'":"") %> >Sent</a></li>
            <li><a href='#download' <%=((tab.equals("download"))?"class='selected'":"") %>>Download</a></li>
        </ul>
	    </div>
	    <!-- <div class="show">
	    	<img class="logo" src="web/img/kai.png"/>
	    </div> -->
        <div id="first" class="greyBox" <%=((tab==null ||tab.equals("first"))?"style='display:block;'":"style='display:none;'") %> >    
	        <div class="center">    
	            <html:form action="/mcedt/kaimcedt" method="post" styleId="form">
					<table>
						<tr>
							<input id="method" name="method" type="hidden" value="" />
							<div class="row bottomPadding20">
								<h1>Welcome To MCEDT</h1>
								<div style="width: 65%; float: left; padding-right:5%;">
						            <h2>Use the buttons below to automate the upload and download process, or use the tabs to handle it manually.</h2>
						        </div>
						        
						        <div class="navbar" style="width: 30%; float: right;">
						        	<p class="greenText bold capital font14" style="margin-top:5px;">Other Tools</p>
									<div style="vertical-align: middle !important;">
										<button type="button" class="flatLink black font12 bottomMargin20" onclick="return changePass();">Change Password</button>
									</div>
									<div style="vertical-align: middle !important;">	
										<button class="flatLink black font12" onclick="return deleteSelected(this);" disabled>Check Connection</button>
									</div>
								</div>
							</div>
						</tr>
						
						<tr>
							<div class="navbar">
								<div style="vertical-align: middle !important; width: 65%; float: left;">
									<button class="green flatLink font14" style="width:45%; padding:20px; margin-right:2%;"onclick="this.disabled=true;ShowSpin(true); return autoDownload();">
										<img src="web/img/download.png" style="float:left;"/>
										Download new files (EDT  >>  Oscar)
									</button>	
									<button class="green flatLink font14" style="width:45%; padding:20px;" onclick="return submitSelected(this);">
										<img src="web/img/upload.png" style="float:left;"/>
										Upload new files (Oscar  >>  EDT)
									</button>
								</div>
							</div>
						</tr>
					</table>
				</html:form>
			</div>
        </div>
        <div id="upload" class="greyBox" <%=((tab.equals("upload"))?"style='display:block;'":"style='display:none;'") %> >
			<div class="center">
	            <h1>Upload</h1>
	            <p><jsp:include page="upload.jsp" flush="true"/></p>
	    	</div>
        </div>
        <div id="sent" class="greyBox" <%=((tab.equals("sent"))?"style='display:block;'":"style='display:none;'") %> >
			<div class="center">
	            <h1>Sent Items</h1>
	            <p><jsp:include page="sent.jsp" flush="true"/></p>
        	</div>
        </div>
        <div id="download" class="greyBox" <%=((tab.equals("download"))?"style='display:block;'":"style='display:none;'") %> >
			<div class="center">
	            <h1>Download</h1>
	            <p><jsp:include page="download.jsp" flush="true"/></p>
        	</div>
        </div>
    </div>
	<!-- <div class="waste"></div> -->
  </body>
</html>




