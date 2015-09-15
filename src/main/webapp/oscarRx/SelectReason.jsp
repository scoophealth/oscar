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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ page import="oscar.oscarRx.data.*,java.util.*,org.oscarehr.common.dao.DrugReasonDao,org.oscarehr.common.model.DrugReason"%>
<%@page import="org.oscarehr.util.SpringUtils,oscar.util.StringUtils"%>
<%@ page import="org.oscarehr.common.dao.DxresearchDAO,org.oscarehr.common.model.Dxresearch,org.oscarehr.common.dao.Icd9Dao,org.oscarehr.common.model.Icd9" %>
<%@ page import="org.oscarehr.util.MiscUtils" %>
<%@page import="org.oscarehr.managers.CodingSystemManager" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%
	CodingSystemManager codingSystemManager = SpringUtils.getBean(CodingSystemManager.class);
%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_rx" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_rx");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<html:html locale="true">
<head>

<%-- <script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/prototype.js"></script> 
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script> --%>

<script type="text/javascript" src="${ oscar_context_path }/js/jquery-1.7.1.min.js" ></script>
<script type="text/javascript" src="${ oscar_context_path }/js/jquery-ui-1.8.18.custom.min.js" ></script>
<script type="text/javascript" >var ctx = '${ oscar_context_path }';</script>
<title>Drug Reason</title>
<html:base />

<logic:notPresent name="RxSessionBean" scope="session">
	<logic:redirect href="error.html" />
</logic:notPresent>
<logic:present name="RxSessionBean" scope="session">
	<bean:define id="bean" type="oscar.oscarRx.pageUtil.RxSessionBean"
		name="RxSessionBean" scope="session" />
	<logic:equal name="bean" property="valid" value="false">
		<logic:redirect href="error.html" />
	</logic:equal>
</logic:present>

<%
oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean)pageContext.findAttribute("bean");
SecurityManager sm = new SecurityManager();
String demoStr = request.getParameter("demographicNo");
String drugIdStr  = request.getParameter("drugId");
Integer drugId = null;
Integer demoNo = null;

if(drugIdStr !=null){
	drugId = Integer.parseInt(drugIdStr);
}

if(drugId ==null){
	drugId = (Integer) request.getAttribute("drugId");
	drugIdStr = drugId.toString();
}

if(demoStr != null){
	demoNo = Integer.parseInt(demoStr);
}

if(demoNo == null){
	demoNo = (Integer) request.getAttribute("demoNo");
	demoStr = demoNo.toString();
}

boolean showQuicklist=false;

/* if(sm.hasWriteAccess("_dx.quicklist", roleName$)) {
	showQuicklist=true;
} */

DxresearchDAO dxResearchDAO  = (DxresearchDAO) SpringUtils.getBean("dxresearchDAO");
List<Dxresearch> dxList = dxResearchDAO.getByDemographicNo(demoNo);
Icd9Dao icd9Dao = (Icd9Dao)  SpringUtils.getBean("Icd9DAO");

pageContext.setAttribute("showQuicklist", showQuicklist);

%>
 
<bean:define id="patient" type="oscar.oscarRx.data.RxPatientData.Patient" name="Patient" />

<style type="text/css">
	body {
		margin:0;
		padding:3;
	}
.label {
	width:33%;
}

	table td label {
		width:100%;
		display: table;
		text-align: right;

	}
	table {
		width:100%;
		border-collapse: collapse;
		float:left;
		clear:both;
		
	}
	table td {
		padding:3px;
	}
</style>
<link rel="stylesheet" type="text/css" href="styles.css">
<script type="text/javascript">

	function assignPatientDxLink(id, name) {
		$("#codeTxt").val(id);
		$(".codeTxt").val(name);
		$(".codeTxt").css('color','black')
	}
	
	function toggleArchiveMenu(id) {
		$('#' + id).toggle();
	}
	$(document).ready(function() {
		$("#saveRxReason").click(function(event){
			event.preventDefault();
			$("#rxReasonForm").submit();
			opener.location.reload();
			window.close();
		})
		
	})
</script>	
</head>

<body topmargin="0" leftmargin="0" vlink="#0000FF">

<table id="AutoNumber1">
	
	<%@ include file="TopLinks.jsp"%><!-- Row One included here-->
	<tr>
		<td style="width:25%;vertical-align:top;"> <!-- left column -->
	
			<fieldset>
				<legend>Patient Dx Registry</legend>
				<table>
		        <% for(Dxresearch dx:dxList){
		        	String idc9Desc = "N/A";
		        	try{
		        	   idc9Desc = icd9Dao.getIcd9Code(dx.getDxresearchCode()).get(0).getDescription();
		        	}catch(Exception dxException){
		        		MiscUtils.getLogger().error("ICD9 Code not found ",dxException );
		        	}
		        %>
		        	<tr><td>
		        		<a href="javascript:void(0);" onclick="assignPatientDxLink('<%=dx.getDxresearchCode()%>', '<%=idc9Desc%>')" 
		        			title="<%=dx.getDxresearchCode()%> - <%=idc9Desc%>"  >		        			
		        			<%=dx.getDxresearchCode()%> - <%=StringUtils.maxLenString(idc9Desc, 10, 6, StringUtils.ELLIPSIS)%>
		        		</a>
		        	</td></tr>
		        <%}%>
		        </table>
		     </fieldset>   
		     <fieldset> 
		     	<legend>Dx Quick List</legend> 
		     	
		        <%-- DX QUICK LIST - returns a table 
					<logic:equal name="showQuicklist" value="true" scope="page">
					<tr>
						<td>
						<jsp:include page="dxQuickList.jsp" >
							<jsp:param value="false" name="disable"/>
							<jsp:param value="${ param.quickList }" name="quickList" />
							<jsp:param value="${ demographicNo }" name="demographicNo"/>
							<jsp:param value="${ providerNo }" name="providerNo"/>
						</jsp:include>
						</td>
					</tr>
					</logic:equal>--%>
				<%-- DX QUICK LIST --%>
        	</fieldset>

		</td> <!--   Side Bar File --->
		
		
		
		<td style="border-left: 2px solid #A9A9A9;" >
		
		<%if (request.getAttribute("message") !=null){ %>
			<span style="color:red;"><%=request.getAttribute("message") %></span>
		<%} %>
		
		<form action="RxReason.do" method="post" id="rxReasonForm">
		
		<fieldset>
			<input type="hidden" name="method" value="addDrugReason"/>
			<input type="hidden" name="demographicNo" value="<%=demoStr%>"   />
	        <input type="hidden" name="drugId"  value="<%=drugIdStr%>"  />
	        
			<legend>Assign Indication</legend>
			
<%-- 	Replaced with Dx Code Search template.		

			<label for="codingSystem" >Disease Code System</label>
		 	<select name="codingSystem" id="codingSystem" >
				<option value="icd9">icd9</option>
				option value="limitUse">Limited Use</option
			</select>
			
			<label for="codeTxt" >Indication</label><input type="text" name="code" id="codeTxt" /> --%>
			
			<input type="hidden" name="code" id="codeTxt" />
			<jsp:include page="/oscarResearch/oscarDxResearch/dxJSONCodeSearch.jsp" >
		    	<jsp:param value="true" name="enableCodeSystemSelect"/>
		    </jsp:include>
			
			<table >
			<tr><td class="label"><label for="comments" >Additional Comments</label> </td> 
			<td><textarea name="comments" id="comments"></textarea></td></tr>	
	
			<tr>
				<td colspan="2">
					<input type="checkbox" name="primaryReasonFlag" id="primaryReasonFlag" value="true"/>
					Primary Indication
				</td> 
				
			</tr>

			<tr><td colspan="2"><input type="submit" id="saveRxReason" value="Save"/></td> </tr>
			
			</table>
		</fieldset>
		</form>
		
		<table >		
			<tr>
				<td style="padding:0px;margin:0px;" >
				<%
				DrugReasonDao drugReasonDao  = (DrugReasonDao) SpringUtils.getBean("drugReasonDao");

				List<DrugReason> drugReasons  = drugReasonDao.getReasonsForDrugID(drugId,true);
				%>

                <fieldset style="height:200px; overflow:auto;">
                <legend>Current Indications</legend>
				<table>
					<tr>
						<th><bean:message key="SelectReason.table.codingSystem" /></th>
						<th><bean:message key="SelectReason.table.code" /></th>
						<th><bean:message key="SelectReason.table.description" /></th>
						<th><bean:message key="SelectReason.table.comments" /></th>
						<th><bean:message key="SelectReason.table.primaryReasonFlag" /></th>
						<th><bean:message key="SelectReason.table.provider" /></th>
						<th><bean:message key="SelectReason.table.dateCoded" /><th>
						<th>&nbsp;</th>
					</tr>

					<%for(DrugReason drugReason:drugReasons){ %>
					<tr>
						<td><%=drugReason.getCodingSystem() %></td>
						<td><%=drugReason.getCode() %></td>
						<td>
							<%
							String descr = codingSystemManager.getCodeDescription(drugReason.getCodingSystem(), drugReason.getCode());
							descr = org.apache.commons.lang.StringUtils.trimToEmpty(descr);
							%>
							<%=StringEscapeUtils.escapeHtml(descr) %>
						</td>
						<td><%=drugReason.getComments() %></td>
						<td>
						<%if(drugReason.getPrimaryReasonFlag()){ %>
								True
						<%}%>
						</td>
						<td><%=drugReason.getProviderNo() %></td>
						<td><%=drugReason.getDateCoded() %></td>
						<td>
							<a onclick="toggleArchiveMenu('archive<%=drugReason.getId()%>')" 
							href="javascript:void(0);">archive</a>
						</td>
					</tr>
					<tr id="archive<%=drugReason.getId()%>" style="display:none;">
					    <td colspan="7">
					    <div >

							<form action="RxReason.do" method="post">
								<fieldset>
									<legend>Archive  Coding System: <%=drugReason.getCodingSystem() %> Code: <%=drugReason.getCode() %></legend>
									<input type="hidden" name="method" value="archiveReason"/>
									<input type="hidden" name="reasonId" value="<%=drugReason.getId()%>"/>
 									Reason: <input type="text" name="archiveReason"/>
									<input type="submit" value="Archive"/>
								</fieldset>
							</form>
							</div>
					    </td>
					</tr>
					<%}%>

				</table>
                </fieldset>

				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="0%" style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
		<td height="0%" style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
	</tr>
	<tr>
		<td width="100%" height="0%" colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC" colspan="2"></td>
	</tr>
</table>

</body>

</html:html>
