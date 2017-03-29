<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.common.model.Admission"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.web.OcanForm"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));	
	int prepopulationLevel = OcanForm.PRE_POPULATION_LEVEL_ALL;
	String ocanType = request.getParameter("ocanType");
	int ocanStaffFormId =0;
	if(request.getParameter("ocanStaffFormId")!=null && request.getParameter("ocanStaffFormId")!="") {
		ocanStaffFormId = Integer.parseInt(request.getParameter("ocanStaffFormId"));
	}
	int centerNumber = Integer.parseInt(request.getParameter("center_num"));
	String LHIN_code = request.getParameter("LHIN_code");
	
	int prepopulate = 0;
	prepopulate = Integer.parseInt(request.getParameter("prepopulate")==null?"0":request.getParameter("prepopulate"));
	
	OcanStaffForm ocanStaffForm = null;
	if(ocanStaffFormId != 0) {
		ocanStaffForm=OcanForm.getOcanStaffForm(Integer.parseInt(request.getParameter("ocanStaffFormId")));
	}else {
		ocanStaffForm=OcanForm.getOcanStaffForm(loggedInInfo.getCurrentFacility().getId(),currentDemographicId,prepopulationLevel,ocanType);	
		
		if(ocanStaffForm.getAssessmentId()==null) {
			
			OcanStaffForm lastCompletedForm = OcanForm.getLastCompletedOcanFormByOcanType(loggedInInfo.getCurrentFacility().getId(),currentDemographicId,ocanType);
			if(lastCompletedForm!=null) {
				
				// prepopulate the form from last completed assessment
				if(prepopulate==1) {			
						
					lastCompletedForm.setAssessmentId(null);
					lastCompletedForm.setAssessmentStatus("In Progress");
						
					ocanStaffForm = lastCompletedForm;
					
				}
			}
		}
		if(ocanStaffForm!=null) {
			ocanStaffFormId = ocanStaffForm.getId()==null?0:ocanStaffForm.getId().intValue();
		}
	}
%>


<script type="text/javascript">
$('document').ready(function() {
	//load mental health centres orgnaization name
	var demographicId='<%=currentDemographicId%>';
	var ocanType='<%=ocanType%>';
	var ocanStaffFormId = '<%=ocanStaffFormId%>';
	var LHIN_code= $("#serviceUseRecord_orgLHIN<%=centerNumber%>").val(); 
	var orgName = $("#serviceUseRecord_orgName<%=centerNumber%>").val();
	var item = $("#center_block_orgName<%=centerNumber%>");
	var prepopulate = '<%=prepopulate%>';
	if(orgName!=null && orgName!="") {
		$.get('ocan_form_getProgramName.jsp?prepopulate='+prepopulate+'&ocanStaffFormId='+ocanStaffFormId+'&ocanType='+ocanType+'&demographicId='+demographicId+'&center_num=<%=centerNumber%>'+'&LHIN_code='+LHIN_code+'&orgName='+orgName, function(data) {
			  item.append(data);					 
			});				
	}
});
</script>

<div id="center_orgName<%=centerNumber%>">
	<table>
	
		<tr>
			<td class="genericTableHeader">Organization Name</td>
			<td class="genericTableData">
			
			<select name="serviceUseRecord_orgName<%=centerNumber %>" id="serviceUseRecord_orgName<%=centerNumber %>" onchange="changeOrgName(this);" class="{validate: {required:true}}">					
				<%=OcanForm.renderAsConnexOrgNameSelectOptions(ocanStaffForm.getId(), "serviceUseRecord_orgName"+centerNumber, OcanForm.getOcanConnexOrgOptions(LHIN_code), prepopulationLevel)%>
			</select>	
			</td>
		</tr>
		
	</table>
</div>
