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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%-- This JSP is the first page you see when you enter 'report by template' --%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.common.model.Provider" %>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	Provider provider = loggedInInfo.getLoggedInProvider();
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>OSCAR Jobs</title>
<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/DT_bootstrap.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/bootstrap-responsive.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/cupertino/jquery-ui-1.8.18.custom.css">

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/DT_bootstrap.js"></script>   
<script type="text/javascript" language="JavaScript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"></script>

<%
java.util.Map<Integer,java.util.concurrent.ScheduledFuture<Object>> futures = org.oscarehr.common.jobs.OscarJobExecutingManager.getFutures();
%>
<style>
.red{color:red}

</style>

<script>
	function cancelJob(jobId) {
		jQuery.getJSON("../ws/rs/jobs/cancelJob?jobId="+jobId, {async:true},
	       		 function(xml) {
				listJobs();
			});
	}
	
	function updateJobStatus(jobId,status) {
		if(status) {
			jQuery.getJSON("../ws/rs/jobs/enableJob?jobId="+jobId, {async:true},
		       		 function(xml) {
						listJobs();
			});
		} else {
			jQuery.getJSON("../ws/rs/jobs/disableJob?jobId="+jobId, {async:true},
		       		 function(xml) {
						listJobs();
			});
		}
	}

	function scheduleJob(jobId) {
		$('#scheduleJobId').val(jobId);
		
		$('input:radio[name=minute_chooser]')[0].checked = true;
		$('input:radio[name=hour_chooser]')[0].checked = true;
		$('input:radio[name=day_chooser]')[0].checked = true;
		$('input:radio[name=month_chooser]')[0].checked = true;
		$('input:radio[name=weekday_chooser]')[0].checked = true;
		
		$("#minute option:selected").removeAttr("selected");
		$("#hour option:selected").removeAttr("selected");
		$("#day option:selected").removeAttr("selected");
		$("#month option:selected").removeAttr("selected");
		$("#weekday option:selected").removeAttr("selected");

		$("#minute").attr('disabled','disabled');
		$("#hour").attr('disabled','disabled');
		$("#day").attr('disabled','disabled');
		$("#month").attr('disabled','disabled');
		$("#weekday").attr('disabled','disabled');
		
		//do we already have an existing cronExpression
		jQuery.getJSON("../ws/rs/jobs/job/"+jobId, {async:false},
       		 function(xml) {
				var existingCron = xml.jobs.cronExpression;
				if(existingCron != undefined && existingCron.length>0) {
					var parts = existingCron.split(' ');
					//ignore seconds parts[0]
					setCronPart(parts[1],'minute');
					setCronPart(parts[2],'hour');
					setCronPart(parts[3],'day');
					setCronPart(parts[4],'month');
					setCronPart(parts[5],'weekday');
					
					$('#scheduleDialog').dialog('open');
				} else { 
					$('#scheduleDialog').dialog('open');
				}
		});
	}

	function setCronPart(value,type) {
		if(value == '*') {
			$('input:radio[name='+type+'_chooser]')[0].checked = true;
		} else {
			$('input:radio[name='+type+'_chooser]')[1].checked = true;
			$("#"+type).removeAttr('disabled');
			var mins = value.split(',');					
			$('#'+type).val(mins);
			
		}
	}
	
	function editJob(jobId) {
		jQuery.getJSON("../ws/rs/jobs/job/"+jobId, {},
        function(xml) {
			if(xml.jobs) {
				var job;
				if(xml.jobs instanceof Array) {
					job = xml.jobs[0];
				} else {
					job = xml.jobs;
				}
				
				$('#jobName').val(job.name);
				$('#jobType').val(job.oscarJobTypeId);
				$('#jobDescription').val(job.description);
				$('#jobEnabled').prop('checked',job.enabled);
				$('#jobProvider').val(job.providerNo);
				$('#jobId').val(job.id);
			}
        });
		$('#new-job').dialog('open');
	}
	
	function addNewJob() {	
		$('#jobName').val('');
		$('#jobType').val('');
		$('#jobDescription').val('');
		$('#jobEnabled').prop('checked',true);
		$('#jobProvider').val('<%=provider.getProviderNo()%>');
		$('#jobId').val('0');
		$('#new-job').dialog('open');
	}
	
	function clearJobs() {
		$("#jobTable tbody tr").remove();
	}
	
	function listJobs() {
		jQuery.getJSON("../ws/rs/jobs/all", {},
        function(xml) {
			clearJobs();
			
			if(xml.jobs) {
				var arr = new Array();
				if(xml.jobs instanceof Array) {
					arr = xml.jobs;
				} else {
					arr[0] =xml.jobs;
				}
				
				for(var i=0;i<arr.length;i++) {
					var job = arr[i];
					var extraClass = (job.cronExpression != undefined)?"blue":"red";
					var html = '<tr>';
					html += '<td><a onclick="scheduleJob('+job.id+');"><i class="icon-calendar ' + extraClass + '"></i></a></td>';
					html += '<td><u><a href="javascript:void();" onclick="editJob('+job.id+');">'+job.name+'</a></u></td>';
					html += '<td><a onclick="cancelJob('+job.id+');">Cancel</a></td>';
					html += '<td>'+((job.enabled==true)?"Enabled (<a onclick='updateJobStatus("+job.id+",false)'>Disable</a>)":"<span color='red'>Disabled</span> (<a onclick='updateJobStatus("+job.id+",true)'>Enable</a>)") +'</td>';
					html += '<td>N/A</td>';
					html += '<td>'+((job.nextPlannedExecutionDate==null)?'N/A':new Date(job.nextPlannedExecutionDate)) +'</td>';		
					html += '</tr>';
				
					jQuery('#jobTable tbody').append(html);
				}
			} else {
        		alert('error retrieving jobs');
			}
        });
	}
	
	function getJobTypes() {
		jQuery.getJSON("../ws/rs/jobs/types/all",{async:false},
        function(xml) {
			if(xml.types) {
				var arr = new Array();
				if(xml.types instanceof Array) {
					arr = xml.types;
				} else {
					arr[0] = xml.types;
				}
				
				for(var i=0;i<arr.length;i++) {
					$('#jobType').append($('<option>', {
					    value: arr[i].id,
					    text: arr[i].name + ((arr[i].currentlyValid == true)?'':'(Not currently available)')
					}));
				}
				
			}
			
        });
	}
	
	function getProviders() {
		jQuery.getJSON("../ws/rs/providerService/providers_json",{async:false},
        function(xml) {
			if(xml.content instanceof Array) {
				for(var i=0;i<xml.content.length;i++) {
					$('#jobProvider').append($('<option>', {
					    value: xml.content[i].providerNo,
					    text: xml.content[i].lastName + ',' + xml.content[i].firstName
					}));
				}
			}
        });
	}
	
	$(document).ready(function(){
		getJobTypes();
		getProviders();	
		listJobs();
		
		$( "#new-job" ).dialog({
			autoOpen: false,
			height: 560,
			width: 620,
			modal: true,
			buttons: {
				"Save Job": function() {	
					if(validateSaveJob()) {
						$.post('../ws/rs/jobs/saveJob',$('#jobForm').serialize(),function(data){listJobs();});
						$( this ).dialog( "close" );	
					}
					
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				
			}
		});
		
		$( "#scheduleDialog" ).dialog({
			autoOpen: false,
			height: 400,
			width: 780,
			modal: true,
			buttons: {
				"Save": function() {	
					//TODO: validate the fields.
					//submit the crontab-form , close the dialog.
					$.post('../ws/rs/jobs/saveCrontabExpression',$('#crontab-form').serialize(),function(data){listJobs();});	
					$( this ).dialog( "close" );	
					
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				
			}
		});
		
		$(":radio").bind('change',function(){
			var chooser = $(this).attr('name');
			
			var checked = $("input:radio[name=" + chooser + "]:checked").val();
			var rootName = chooser.substring(0,chooser.indexOf("_"));
			
			if(checked==0) {
				$("#"+rootName).attr('disabled','disabled');
			} else {
				$("#"+rootName).removeAttr('disabled');
			}
		});
	});
	
	function validateSaveJob() {
		var errorMsg = '';
		
		if($('#jobName').val().length==0) {
			errorMsg += 'Please provide a name for the job\n';
		}
		if($('#jobType').val().length==0 || $('#jobType').val() == '0' ) {
			errorMsg += 'Please provide a valid job type\n';
		}
		if($('#jobProvider').val().length==0 || $('#jobProvider').val() == '0' ) {
			errorMsg += 'Please provide a valid job provider\n';
		}
		
		if(errorMsg.length>0) {
			alert(errorMsg);
			return false;
		}
		return true;
		
	}
</script>
</head>

<body vlink="#0000FF" class="BodyStyle">
<h4>Manage Jobs</h4>
<table id="jobTable" name="jobTable" class="table table-bordered table-striped table-hover table-condensed">
	<thead>
		<tr>
			<th></th>
			<th>Name</th>
			<th>Execution Status</th>
			<th>Job Status</th>
			<th>Last Run</th>
			<th>Next Planned Run</th>
		</tr>
	</thead>
	<tbody>
	</tbody>
</table>
<input type="button" class="btn btn-primary" value="Add New" onClick="addNewJob()"/>	


<div id="new-job" title="OSCAR Job Editor">
	<p class="validateTips"></p>
	
	<form id="jobForm">
		<input type="hidden" name="job.id" id="jobId" value="0"/>
		<fieldset>
			<div class="control-group">
				<label class="control-label" for="jobName">Name:*</label>
				<div class="controls">
					<input type="text" name="job.name" id="jobName" value=""/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="jobType">Type:*</label>
				<div class="controls">
					<select name="job.oscarJobTypeId" id="jobType">
						<option value=""></option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="jobDescription">Description:</label>
				<div class="controls">
					<textarea rows="5" name="job.description" id="jobDescription"></textarea>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="jobEnabled">Enabled: <input type="checkbox" name="job.enabled" id="jobEnabled" /></label>
				<div class="controls">
					
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="jobProvider">Run As Provider:</label>
				<div class="controls">
					<select name="job.provider" id="jobProvider">
						<option value=""></option>
						
					</select>
				</div>
			</div>
			
		</fieldset>
	</form>
</div>


<div id="scheduleDialog" title="Schedule Job">
	<p class="validateTips"></p>
	
	<form id="crontab-form">
		<input type="hidden" name="scheduleJobId" id="scheduleJobId" value="0"/>
	
	<table cellpadding="2" cellspacing="2">
		<tr>
			<td>
				<h4>Minute</h4>
				
				Every Minute
				<input type="radio" name="minute_chooser" id="minute_chooser_every"  value="0" checked="checked" /><br />
				Choose
				<input type="radio" name="minute_chooser" id="minute_chooser_choose"  value="1" /><br />
				
				<select name="minute" id="minute" multiple="multiple" disabled="disabled" style="width:120px">
				<%
					for(int x=0;x<59;x++) {
				%>
					<option value="<%=x%>"><%=x%></option>
				<% } %>
				</select>
			</td>
			<td>
				<h4>Hour</h4>
				Every Hour
				<input type="radio" name="hour_chooser" id="hour_chooser_every"  value="0" checked="checked" /><br />
				
				Choose
				<input type="radio" name="hour_chooser" id="hour_chooser_choose"  value="1" /><br />
				
				<select name="hour" id="hour" multiple="multiple" disabled="disabled" style="width:120px">
				<option value="0">12 Midnight</option>
				<option value="1">1 AM</option><option value="2">2 AM</option><option value="3">3 AM</option>
				<option value="4">4 AM</option><option value="5">5 AM</option><option value="6">6 AM</option>
				<option value="7">7 AM</option><option value="8">8 AM</option><option value="9">9 AM</option>
				<option value="10">10 AM</option><option value="11">11 AM</option><option value="12">12 Noon</option>
				<option value="13">1 PM</option><option value="14">2 PM</option><option value="15">3 PM</option>
				<option value="16">4 PM</option><option value="17">5 PM</option><option value="18">6 PM</option>
				<option value="19">7 PM</option><option value="20">8 PM</option><option value="21">9 PM</option>
				<option value="22">10 PM</option><option value="23">11 PM</option></select>			
			</td>	
						
			<td>
				<h4>Day</h4>
				Every Day
				<input type="radio" name="day_chooser" id="day_chooser_every"  value="0" checked="checked" /><br />
				
				Choose
				<input type="radio" name="day_chooser" id="day_chooser_choose"  value="1" /><br />
				
				<select name="day" id="day" multiple="multiple" disabled="disabled" style="width:120px">
				<%
					for(int x=1;x<30;x++) {
				%>
					<option value="<%=x%>"><%=x%></option>
				<% } %>
				</select>			
			</td>		
		<td>
			<h4>Month</h4>
			Every Month
			<input type="radio" name="month_chooser" id="month_chooser_every"  value="0" checked="checked" /><br />
			
			Choose
			<input type="radio" name="month_chooser" id="month_chooser_choose"  value="1" /><br />
			
			<select name="month" id="month" multiple="multiple" disabled="disabled" style="width:120px">
			<option value="1">January</option>
			<option value="2">February</option>
			<option value="3">March</option>
			<option value="4">April</option>
			<option value="5">May</option>
			<option value="6">June</option>
			<option value="7">July</option>
			<option value="8">Augest</option>
			<option value="9">September</option>
			<option value="10">October</option>
			<option value="11">November</option>
			<option value="12">December</option>
			</select>		
		</td>
		<td>
			<h4>Weekday</h4>
			Every Weekday
			<input type="radio" name="weekday_chooser" id="weekday_chooser_every"  value="0" checked="checked" /><br />
			
			Choose
			<input type="radio" name="weekday_chooser" id="weekday_chooser_choose"  value="1" /><br />
			
			<select name="weekday" id="weekday" multiple="multiple" disabled="disabled" style="width:120px">
			<option value="0">Sunday</option>
			<option value="1">Monday</option>
			<option value="2">Tuesday</option>
			<option value="3">Wednesday</option>
			<option value="4">Thursday</option>
			<option value="5">Friday</option>
			<option value="6">Saturday</option>
			</select>		
		</td>
		</tr>
	</table>
<!-- 
<br />
Result Crontab Line:<br />
<input type="text" name="cron" id="cron" size="100">
-->
</form>
</div>


</body>
</html:html>
