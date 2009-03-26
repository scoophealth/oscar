<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>
<%@page
	import="java.util.*,org.oscarehr.PMmodule.service.*,org.oscarehr.util.SpringUtils,org.oscarehr.common.model.Facility, org.springframework.web.context.support.*,org.springframework.web.context.*"%>
<%@page import="org.oscarehr.common.dao.FacilityDao"%>
<%
WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
GenericIntakeManager  genericIntakeManager =  (GenericIntakeManager) ctx.getBean("genericIntakeManager");
FacilityDao facilityDao = (FacilityDao) SpringUtils.getBean("facilityDao");
List<Integer> facilityIds =  genericIntakeManager.getIntakeFacilityIds();
List<Facility> facilities = new ArrayList<Facility>();
if(facilityIds != null && !facilityIds.isEmpty()) {
	for(Integer i : facilityIds) {
		facilities.add(facilityDao.find(i));
	}
}
request.setAttribute("facilities", facilities);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	function callAction() {
//		document.getElementById("exportButton").value = "Exporting...";
//		document.getElementById("exportButton").disabled = true;
		if(document.getElementById("submitted").value == 'off') {
			document.getElementById("submitted").value = 'on';
			return true;
		}
		return false;
	}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
<META HTTP-EQUIV="EXPIRES" CONTENT="-1">
<title>DATIS Export</title>
</head>
<body>
	
	<form name="datisform" method="post" action="../exportfiles">
		<input type="hidden" id="submitted" name="submitted" value="off">
		<table cellpadding="0" cellspacing="0" width="40%" style="text-indent: 2px;">
			<tr style="background-color: #342D7E">
				<td style="color: #FFFFFF"><strong>DATIS EXPORT</strong></td>
			</tr>
			<tr style="background-color: #C2DFFF">
				<td>
					<table cellpadding="0" cellspacing="0" style="text-indent: 2px;">
						<tr><td>&nbsp;</td></tr>
						<tr><td>Facility(All clients in facility will be exported):&nbsp;</td>
							<td>
								<select name="facilityId">
									<c:forEach items="${facilities}" var="fac">
										<option value=<c:out value="${fac.id }"/>><c:out value="${fac.name }"/></option>
									</c:forEach>
								</select>
							</td>
						
					</table>
				</td>
			</tr>
			<tr style="background-color: #C2DFFF">
				<td>
					<table cellpadding="0" cellspacing="0" style="text-indent: 2px;">
						<tr><td>Files to export:</td></tr>
						<tr><td><input name="ai" type="checkbox" checked="checked">Agency Information</td></tr>
						<tr><td><input name="lp" type="checkbox" checked="checked">List Of Programs</td></tr>
						<tr><td><input name="mn" type="checkbox" checked="checked">Main</td></tr>
						<tr><td><input name="pi" type="checkbox" checked="checked">Program Information</td></tr>
						<tr><td><input name="gf" type="checkbox" checked="checked">Gambling Form</td></tr>
						<tr><td><input name="nc" type="checkbox" checked="checked">Non Client Services</td></tr>
					</table>
				</td>
			</tr>
			<tr style="background-color: #C2DFFF"><td>&nbsp;</td></tr>
			<tr style="background-color: #C2DFFF">
				<td>
					<input id="exportButton" type="submit" value="Export Files" />&nbsp;(This operation may take few minutes)
				</td>
			</tr>
			<tr style="background-color: #C2DFFF">
				<td><em>If this operation is taking long time, try exporting one file at a time.</em></td>
			</tr>
		</table>
	</form>
</body>
</html>