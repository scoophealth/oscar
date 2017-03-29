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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="java.util.Calendar"%>
<%@page import="org.oscarehr.PMmodule.model.Program"%>
<%@page import="org.oscarehr.PMmodule.service.ProgramManager"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.managers.ProviderManager2"%>
<%@page import="org.oscarehr.common.dao.FunctionalCentreDao"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.common.model.FunctionalCentre"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.text.DateFormatSymbols"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>


<%@ include file="/taglibs.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />

<%
	FunctionalCentreDao functionalCentreDao = (FunctionalCentreDao) SpringUtils.getBean("functionalCentreDao");
	ProviderManager2 providerManager = (ProviderManager2) SpringUtils.getBean("providerManager2");
    ProgramManager programManager = (ProgramManager) SpringUtils.getBean("programManager");

    LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	List<FunctionalCentre> functionalCentres=functionalCentreDao.findInUseByFacility(loggedInInfo.getCurrentFacility().getId());
	
	Calendar cal = Calendar.getInstance();
	String today = DateFormatUtils.ISO_DATE_FORMAT.format(cal);
	cal.add(Calendar.MONTH,-1);
	String lastMonth = DateFormatUtils.ISO_DATE_FORMAT.format(cal);

	List<Program> programs=programManager.getPrograms(loggedInInfo.getCurrentFacility().getId());
	
%>

<%@include file="/layouts/html_top.jspf"%>

<script>
function chooseProgramProviderFilter() {
	var pId = jQuery("#pIds").val();
    jQuery.getJSON("../PMmodule/ProviderSearch.do?method=search",
            {
    			programNo: pId
            },
            function(response){
        		$("#providerIds").find('option').remove();
        		
        		for(var x=0;x<response.length;x++) {
        			$('#providerIds').append($('<option>', {
        			    value: response[x].id,
        			    text: response[x].name
        			}));

        		}
        		
      });

}

</script>

<script type="text/javascript">
	function validate(form)
	{
		var fields = form.elements;

		if (fields.functionalCentreId.value==null||fields.functionalCentreId.value=="")
		{
			alert('Please select a functional centre.');
			return(false);
		}
	}
</script>

<div class="page-header">
	<h1>CDS Reports</h1>
</div>

<form class="well form-horizontal" action="${ctx}/oscarReport/cds_4_report_results.jsp"
	id="cdsForm">
	<fieldset>

		<!-- Form Name -->
		<legend>CDS-MH 4.05</legend>

		<div class="control-group">
			<label class="control-label">Functional Centre</label>
			<div class="controls">
				<select id="functionalCentreId" name="functionalCentreId" class="input-large">
					<%
						for (FunctionalCentre functionalCentre : functionalCentres)
						{
							%>
							<option value="<%=functionalCentre.getAccountId()%>"><%=functionalCentre.getAccountId()+", "+functionalCentre.getDescription()%></option>
							<%
						}
					%>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Date Start</label>
			<div class="controls">
				<input type="text" name="startDate" id="startDate" />
				<script type="text/javascript">
					jQuery('#startDate').datepicker({ dateFormat: 'yy-mm-dd' });
					
					var d=new Date();
					var month=d.getMonth();
					if (month>0)
					{
						d.setMonth(month-1);
					}
					else
					{
						d.setMonth(11);
						d.setYear(d.getYear()-1);
					}
					
					jQuery('#startDate').datepicker("setDate", d);
					jQuery('#startDate').attr("readonly", true);
				</script>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Date End (inclusive)</label>
			<div class="controls">
				<input type="text" name="endDate" id="endDate" />
				<script type="text/javascript">
					jQuery('#endDate').datepicker({ dateFormat: 'yy-mm-dd' });					
					jQuery('#endDate').datepicker("setDate", new Date());
					jQuery('#endDate').attr("readonly", true);
				</script>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Filter By</label>
			<div class="controls">
				<select id="filterCriteriaSelection" name="filterCriteriaSelection" onchange="showFilterCriteria()">
					<option value="">None</option>
					<option value="PROVIDER">Provider</option>
					<option value="PROGRAM">Program</option>
				</select>				
				<script type="text/javascript">
					function showFilterCriteria()
					{
						var selection=jQuery('#filterCriteriaSelection').val();
						
						if (selection == "PROVIDER")
						{
							jQuery('#providerText').show();
							jQuery('#providerOptions').show();
							jQuery('#providerFilterOptions').show();
							jQuery('#programText').hide();
							jQuery('#programOptions').hide();
						}
						else if (selection == "PROGRAM")
						{
							jQuery('#providerText').hide();
							jQuery('#providerOptions').hide();
							jQuery('#providerFilterOptions').hide();
							jQuery('#programText').show();
							jQuery('#programOptions').show();							
						}
						else
						{
							jQuery('#providerText').hide();
							jQuery('#providerOptions').hide();
							jQuery('#providerFilterOptions').hide();
							jQuery('#programText').hide();
							jQuery('#programOptions').hide();
						}
					}
					
					$(document).ready(function(){
						showFilterCriteria();
					});
				</script>
			</div>
		</div>
			
			
		<div id="providerFilterOptions" class="control-group">
		
			<label class="control-label">Filter provider list by program <small>(optional)</small>
			</label>
			<div class="controls">
				<select id="pIds" name="pIds" class="input-medium" style="width:225px" onClick="chooseProgramProviderFilter()">
					<option value=""></option>
					<%
						
						for (Program program : programs)
						{
							%>
								<option value="<%=program.getId()%>"><%=StringEscapeUtils.escapeHtml(program.getName()+" ("+program.getType()+")")%></option>
							<%
						}
					%>
				</select>
			</div>
		</div>
		
		<div id="providerOptions" class="control-group">
		
			<label class="control-label">Providers to include
				<small>
					(multi select is allowed)
				</small>
			</label>
			<div class="controls">
				<select name="providerIds" id="providerIds" class="input-medium" multiple="multiple"  style="width:225px" size="10">
					<%
						// null for both active and inactive because the report might be for a provider who's just left in the current reporting period.
						List<Provider> providers=providerManager.getProviders(loggedInInfo, null);

						for (Provider provider : providers)
						{
							// skip (system,system) user
							if (provider.getProviderNo().equals(Provider.SYSTEM_PROVIDER_NO)) continue;
							
							%>
								<option value="<%=provider.getProviderNo()%>"><%=StringEscapeUtils.escapeHtml(provider.getFormattedName())%></option>
							<%
						}
					%>
				</select>
			</div>
		</div>


		<div id="programOptions" class="control-group">
			<label class="control-label">Programs to include
				<small>
					(multi select is allowed)
				</small>
			</label>
			<div class="controls">
				<select name="programIds" class="input-medium" multiple="multiple" style="width:225px" size="10">
					<%
						
						for (Program program : programs)
						{
							%>
								<option value="<%=program.getId()%>"><%=StringEscapeUtils.escapeHtml(program.getName()+" ("+program.getType()+")")%></option>
							<%
						}
					%>
				</select>
			</div>
		</div>

		<div class="control-group">
			<div class="controls">
				<button type="submit" class="btn btn-primary">View Report</button>
			</div>
		</div>

	</fieldset>
</form>
<%@include file="/layouts/caisi_html_bottom.jspf"%>

<div id="cds-results"></div>
<script type="text/javascript">
	$(document).ready(function() {
		$('#cdsForm').validate({
			rules : {
				functionalCentreId : {
					required : true
				}
			}
		});
		
		$('#cdsForm').submit(function() {
			if(!$('#cdsForm').valid()){
				return false;
			}
			var data = $('#cdsForm').serialize();
			$.post($('#cdsForm').attr('action'), data, function(returnData) {
				$('#cds-results').html(returnData);
			})
			return false;
		});
		
	});
</script>
