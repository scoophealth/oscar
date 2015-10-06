<!DOCTYPE html>
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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
if(session.getAttribute("user") == null) response.sendRedirect("../../../logout.jsp");
String user_no = (String) session.getAttribute("user");
%>

<%@ page import="java.util.*, java.sql.*, oscar.util.*,oscar.oscarProvider.data.ProviderData,oscar.oscarBilling.ca.bc.data.*,oscar.entities.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html-el" prefix="html-el"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
GregorianCalendar now=new GregorianCalendar();
int curYear = now.get(Calendar.YEAR);
now.add(Calendar.DAY_OF_YEAR, -1);
pageContext.setAttribute("yesterday",now.getTime());


String[] yearArray = new String[5];
String thisyear = (request.getParameter("year")==null || request.getParameter("year").compareTo("")==0) ? String.valueOf(curYear) : request.getParameter("year");

pageContext.setAttribute("thisYear",thisyear);


String yearColor = "";
yearArray[0] = String.valueOf(curYear);
yearArray[1] = String.valueOf(curYear-1);
yearArray[2] = String.valueOf(curYear-2);
yearArray[3] = String.valueOf(curYear-3);
yearArray[4] = String.valueOf(curYear-4);
pageContext.setAttribute("yearArray",yearArray);

if (thisyear.compareTo(yearArray[0])==0) yearColor="#B1D3EF";
if (thisyear.compareTo(yearArray[1])==0) yearColor="#BBBBBB";
if (thisyear.compareTo(yearArray[2])==0) yearColor="#CCCCCC";
if (thisyear.compareTo(yearArray[3])==0) yearColor="#DDDDDD";
if (thisyear.compareTo(yearArray[4])==0) yearColor="#EEEEEE";

BillActivityDAO billActDAO = new BillActivityDAO();
List billList = billActDAO.getBillactivityByYear(Integer.parseInt(thisyear));
pageContext.setAttribute("billActivityList",billList);

%>
<html>
<head>
<title>Billing Report</title>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
<script language="JavaScript">
var checkSubmitFlg = false;
function checkSubmit() {
	if (checkSubmitFlg == true) {
		return false;
	}
	checkSubmitFlg = true;
	document.forms[0].Submit.disabled = true;
	return true;
}

function findObj(n, d) { //v4.0
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=findObj(n,d.layers[i].document);
  if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function showHideLayers() { //v3.0
    var i,p,v,obj,args=showHideLayers.arguments;
    for (i=0; i<(args.length-2); i+=3){
    if ((obj=findObj(args[i]))!=null) {
        v=args[i+2];
        if (obj.style) {
            obj=obj.style;
            v=(v=='show')?'visible':(v='hide')?'hidden':v;
        }
        obj.visibility=v;
    }
  }
}
//-->
</script>
</head>

<body>
<h3><bean:message key="admin.admin.genTeleplanFile2"/></h3>

<div class="container-fluid well">
	<div id="Layer2" style="position: absolute; left: 76px; top: 190px; width: 129px; height: 123px; z-index: 2; border: 1px none #000000; visibility: hidden;">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="background-color:white">
		<tr bgcolor="#DDDDEE">
			<td align='CENTER'>Last5 Years</td>
		</tr>
		<% for (String year:yearArray) { %>
		<tr>
			<td align='CENTER'><a
				href="TeleplanSubmission.jsp?year=<%=year%>">YEAR <%=year%></a></td>
		</tr>
		<% } %>
	
		<c:forEach var="year" items="${yearArray}">
			<tr>
				<td align='CENTER'><a
					href="TeleplanSubmission.jsp?year=<c:out value="${year}"/>">YEAR
				<c:out value="${year}" /></a></td>
			</tr>
		</c:forEach>
	
	
	</table>
	</div>
	
	
	<h4>Teleplan Group Report - <%=thisyear%></h4>
	<c:if test="${!empty error}"><c:out value="${error}"/></c:if>
	
	<html:form action="/billing/CA/BC/GenerateTeleplanFile.do" onsubmit="return checkSubmit();" styleClass="form-inline">
		
		Select provider
		<select name="provider">
			<option value="all">All Providers</option>
			<%ProviderData pd = new ProviderData();
	                  List<String> list = pd.getProviderListWithInsuranceNo();
	                  for(String provNo: list){
	                     ProviderData provider = new ProviderData(provNo);%>
			<option value="<%=provider.getOhip_no()%>"><%=provider.getLast_name()%>,<%=provider.getFirst_name()%></option>
			<%}%>
		</select>
		<input class="btn btn-primary" type="submit" name="Submit" value="Create Report">
	</html:form>
	
	Activity List | <a href="#" onClick="showHideLayers('Layer2','','show');">Show Archive</a> 
	<button class="btn pull-right" type="button" value="Print" onclick="window.print()"/><i class="icon-print"></i> Print</button>
	

	<table class="table table-striped  table-condensed">
	<thead>
		<tr style="background-color:<%=yearColor%>">
			<th width="12%">Provider</th>
			<th width="14%">Group Number</th>
			<th width="20%">Creation Date</th>
			<th width="15%">Claims/Records</th>
			<th>Teleplan</th>
			<th width="15%">MSP Filename</th>
			<th>HTML Filename</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="billAct" items="${billActivityList}">
			<c:choose>
				<c:when test="${billAct.updatedatetime > yesterday}">
					<tr bgcolor="#E6F0F7" align="center">
				</c:when>
				<c:otherwise>
					<tr bgcolor="<%=yearColor%>" align="center">
				</c:otherwise>
			</c:choose>
	
			<td><c:out value="${billAct.providerohipno}" />&nbsp;</td>
			<td><c:out value="${billAct.groupno}" />&nbsp;</td>
			<td><c:out value="${billAct.updatedatetime}" />&nbsp;</td>
			<td><c:out value="${billAct.claimrecord}" />&nbsp;</td>
			<td><c:choose>
				<c:when test="${billAct.status == 'A'}">
					<html-el:link action="/billing/CA/BC/ManageTeleplan?id=${billAct.id}&method=sendFile">
		            	Send
		            </html-el:link>
				</c:when>
				<c:otherwise>
	            	Sent
	        	</c:otherwise>
			</c:choose></td>
	
			<td><html-el:link action="/billing/CA/BC/DownloadBilling?filename=${billAct.ohipfilename}">
				<c:out value="${billAct.ohipfilename}" />
			</html-el:link></td>
			<td><html-el:link action="/billing/CA/BC/DownloadBilling?filename=${billAct.htmlfilename}">
				<c:out value="${billAct.htmlfilename}" />
			</html-el:link></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</div>
</body>
</html>
