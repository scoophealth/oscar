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
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.managers.SecurityInfoManager"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%	
	SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	boolean isHrmAdmin = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_hrm.administrator", "r", null);
	boolean isAdmin = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin.hrm", "r", null);
	boolean isHrm = securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_hrm", "r", null);
	
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	List<Provider> providers = providerDao.getActiveProviders();
	String myProviderNo = LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo();
	
%>

<!DOCTYPE html > 
<html:html locale="true" >
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>HRM Inbox - OSCAR EMR</title>

	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/library/bootstrap/3.0.0/css/bootstrap.min.css" />
 	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/library/DataTables-1.10.12/media/css/jquery.dataTables.min.css" /> 
	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/hospitalReportManager/inbox.css" />
	<script>var ctx = "${pageContext.request.contextPath}"</script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jquery-1.9.1.min.js"></script>	
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/bootstrap/3.0.0/js/bootstrap.min.js" ></script>	
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/DataTables-1.10.12/media/js/dataTables.bootstrap.min.js" ></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/library/DataTables-1.10.12/media/js/jquery.dataTables.min.js" ></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/hospitalReportManager/inbox.js?<%=(int)(Math.random()*100000)%>"></script>
	
	<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jquery.ui.widget.js" ></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jquery.fileupload.js" ></script>
	
</head>
<body>
<div>
<div class="col-sm-12">
	
    <!-- Fixed navbar -->
    <nav class="navbar navbar-default navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <b>
          <a class="navbar-brand" href="#">Health Report Manager</a>
          </b>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            
          </ul>
          <ul class="nav navbar-nav navbar-right">
          <li><a href="javascript:void(0)"><b>Status:<span id="hrm_status"></span></b></a></li>
          <li><a href="javascript:void(0)" onClick="fetchNewData()">Fetch New Data</a></li>
          <li><a id="uploadHRM" href="javascript:void(0);">Upload</a></li>
          <li><a href="log.jsp">Log</a></li>
          <li><a href="prefs.jsp">Prefs</a></li>
          <%if(isAdmin) { %>
            <li><a href="configure.jsp">Configure</a></li>
          <% } %>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>
	
	<div class="table-responsive" id="libraryTableContainer">
	
	<div class="col-sm-12">
		<table style="width:50%;align:center">
			<tr>
				<td><b>Provider Inbox</b>:</td>
				<%if(isHrmAdmin || isAdmin) { %>
				  <td>
					<select id="providerNo" name="providerNo" class="form-control">
						<option value="ALL">ALL</option>
						<%for(Provider p : providers) {
							String selected="";
							if(p.getProviderNo().equals(myProviderNo)) {
								selected=" selected=\"selected\" ";
							}
						%>
							<option value="<%=p.getProviderNo()%>" <%=selected %>><%=p.getFormattedName()%></option>
						<% } %>
					</select>
				</td>
				<td style="padding-left:10px">
				<b>OR Unmatched only</b>:
				</td>
				<td>
					<input type="checkbox" name="providerUnmatched" value="true" id="providerUnmatched"/>
				</td>
				<% } else { %>
						<td colspan="3"><%=LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProvider().getFormattedName() %></td>
					<% } %>
			</tr>
			<tr>
				<td><b>Limit reports to</b>:</td>
				<td colspan="3">
					<input type="checkbox" checked="checked" id="noSignOff" name="noSignOff"/>No Sign-off &nbsp;&nbsp;
					<input type="checkbox" id="demographicUnmatched" name="demographicUnmatched"/>Unmatched Patient &nbsp;&nbsp;
				</td>
			</tr>
				<tr>
				<td><b>Display</b>:</td>
				<td colspan="3">
					<input type="checkbox" name="showAddlPatientInfo" id="showAddlPatientInfo"/>Additional Patient Information &nbsp;&nbsp;
					<input type="checkbox" name="showCategoryInfo" id="showCategoryInfo"/>Categorization &nbsp;&nbsp;
				</td>
			</tr>
		</table>
	</div>
	
	<br/>
	<br/>
	
	<div class="col-sm-12">
		<table class="table table-striped table-condensed" id="libraryTable" style="width:100%">
			<thead>
				<tr>
					<th></th>
					<th>Recipient</th>
					<th>Patient Name</th>
					<th>DOB</th>
					<th>Health Card #</th>
					<th>Gender</th>
					<th>Report Date</th>
					<th>Received Date</th>
					<th>Sending Facility</th>
					<th>Class/Sub-class</th>
					<th>Category</th>
					<th>Description</th>
				</tr>
			</thead>
		
			<tbody>
			</tbody>
		</table>
		<hr />
		<h3> 
			&nbsp;
		</h3>
	</div>
	</div>


<div id="uploadHRMDialog" class="modal fade" role="dialog">
	<div class="modal-dialog">

		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">
					Upload HRM File
				</h4>
			</div>
			
			<form id="uploadHRMForm" >
						
			<div class="modal-body">
			
				<div class="row">
					<label for="hrm_file">HRM XML File: </label>
					<input id="hrm_file" type="file" name="hrm_file" data-url="../hospitalReportManager/hrm.do?method=uploadReport" >	
				</div>
				

			</div>
			<div class="modal-footer">
				
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Close
				</button>
			</div>
			</form>
		</div>

	</div>
</div> <!-- end modal window -->
</div>	
</div> <!-- end container -->
</body>
</html:html>
