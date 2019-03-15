<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.olis.model.OLISResultNomenclature"%>
<%@page import="org.oscarehr.olis.dao.OLISResultNomenclatureDao"%>
<%@page import="org.oscarehr.common.model.OLISResults"%>
<%@page import="org.oscarehr.common.dao.OLISResultsDao"%>
<%@page import="oscar.log.LogAction"%>
<%@page import="org.oscarehr.common.model.OscarLog"%>
<%@page import="java.io.File"%>
<%@page import="org.oscarehr.olis.OLISUtils"%>
<%@page import="oscar.oscarLab.FileUploadCheck"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="oscar.oscarLab.ca.all.upload.MessageUploader"%>
<%@page import="org.oscarehr.olis.model.OLISRequestNomenclature"%>
<%@page import="org.oscarehr.olis.dao.OLISRequestNomenclatureDao"%>
<%@ page language="java" contentType="text/html;" %>
<%@page import="com.indivica.olis.queries.*,org.oscarehr.olis.OLISSearchAction,java.util.*,oscar.oscarLab.ca.all.parsers.Factory, oscar.oscarLab.ca.all.parsers.OLISHL7Handler, oscar.oscarLab.ca.all.parsers.OLISHL7Handler.OLISError, org.oscarehr.olis.OLISResultsAction, org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.util.MiscUtils" %>
	
<%
 OLISResultsDao olisResultsDao = SpringUtils.getBean(OLISResultsDao.class);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
    jQuery.noConflict();
</script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/share/css/OscarStandardLayout.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/share/javascript/Oscar.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/share/javascript/oscarMDSIndex.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/share/javascript/sortable.js"></script>


<script type="text/javascript">
image_path = '<%=request.getContextPath()%>/images/';
image_up = 'arrow_up.png';
image_down = 'arrow_down.png';
image_none = 'arrow_off.png';


function bulkAddToInbox() {
	var checkedItems='';
	var reqData = {};
	var items = [];
	reqData.items = items;
	
	
	jQuery("input[name='result_checked']:checked").each(function() {
		if(checkedItems.length>0) {checkedItems+=",";}
		checkedItems += jQuery(this).val();
		var item = {};
		item.uuid = jQuery(this).val();
		item.addToInbox = jQuery("input[name='addToInbox_"+item.uuid+"']").is(":checked");
		item.acknowledge= jQuery("input[name='acknowledge_"+item.uuid+"']").is(":checked");
		items.push(item);
	});	
	if(checkedItems.length==0) {
		alert('You must check ALL labs you would like to add to inbox');
		return false;
	}
	
	jQuery.ajax({
		url: "<%=request.getContextPath() %>/olis/AddToInbox.do",
		method: "POST",
		data: "method=bulkAddToInbox&data=" + btoa(JSON.stringify(reqData)),
		dataType: 'json',
		success: function(data) {
			console.log(JSON.stringify(data));
			if(data.successIds) {
				for(var x=0;x<data.successIds.length;x++) {
					console.log(data.successIds[x]);
					jQuery("#resultsTable tr[uuid='"+data.successIds[x]+"']").remove();
					
				}
				alternate(document.getElementById("resultsTable"));
			}
		}
	});
}

function bulkAcknowledge() {
	var checkedItems='';
	jQuery("input[name='result_checked']:checked").each(function() {
		if(checkedItems.length>0) {checkedItems+=",";}
		checkedItems += jQuery(this).val();
	});	
	if(checkedItems.length==0) {
		alert('You must check ALL labs you would like to add to inbox');
		return false;
	}
	jQuery.ajax({
		url: "<%=request.getContextPath() %>/olis/AddToInbox.do",
		data: "uuids=" + checkedItems + "&method=bulkAddToInbox&ack=true",
		dataType: 'json',
		success: function(data) {
			console.log(JSON.stringify(data));
			if(data.successIds) {
				for(var x=0;x<data.successIds.length;x++) {
					console.log(data.successIds[x]);
					jQuery("#resultsTable tr[uuid='"+data.successIds[x]+"']").remove();
					
				}
				alternate(document.getElementById("resultsTable"));
			}
		}
	});
}

function bulkRemove() {
	var checkedItems='';
	jQuery("input[name='result_checked']:checked").each(function() {
		if(checkedItems.length>0) {checkedItems+=",";}
		checkedItems += jQuery(this).val();
	});	
	if(checkedItems.length==0) {
		alert('You must check ALL labs you would like to add to inbox');
		return false;
	}
	jQuery.ajax({
		url: "<%=request.getContextPath() %>/olis/AddToInbox.do",
		data: "uuids=" + checkedItems + "&method=bulkRemove",
		dataType: 'json',
		success: function(data) {
			console.log(JSON.stringify(data));
			if(data.successIds) {
				for(var x=0;x<data.successIds.length;x++) {
					console.log(data.successIds[x]);
					jQuery("#resultsTable tr[uuid='"+data.successIds[x]+"']").remove();
					
				}
				alternate(document.getElementById("resultsTable"));
			}
		}
	});
}

function showMatch(name,uuid) {
	popupPage(800,1000,'<%=request.getContextPath()%>/oscarMDS/SearchPatient.do?labType=HL7&from=olis&segmentID='+uuid+'&name=' + encodeURIComponent(name) );
}

function updateLabDemoStatus2(uuid,demo) {
	
	jQuery.ajax({
		url: "<%=request.getContextPath() %>/olis/AddToInbox.do?method=saveMatch",
		data: "uuid=" + uuid + "&demographicNo=" + demo,
		success: function(data) {
			var ptName = jQuery("#resultsTable tbody tr[uuid='"+uuid+"']").attr('patientName');
			jQuery("#name_" + uuid).html("<a href='javascript:void(0)' onClick='openPatient("+demo+")'>"+ptName+"</a>");
		}
	});
	
	
}
function remove(uuid) {
	jQuery(uuid).attr("disabled", "disabled");
	jQuery.ajax({
		url: "<%=request.getContextPath() %>/olis/AddToInbox.do?method=remove",
		data: "uuid=" + uuid,
		success: function(data) {
			jQuery("#action_result").html(data);
			jQuery("#resultsTable tr[uuid='"+uuid+"']").remove();
			alternate(document.getElementById("resultsTable"));
		}
	});
}

function addToInbox(uuid) {
	jQuery(uuid).attr("disabled", "disabled");
	jQuery.ajax({
		url: "<%=request.getContextPath() %>/olis/AddToInbox.do",
		data: "uuid=" + uuid,
		success: function(data) {
			jQuery("#action_result").html(data);
			jQuery("#resultsTable tr[uuid='"+uuid+"']").remove();
			alternate(document.getElementById("resultsTable"));
		}
	});
}
function preview(uuid) {
	reportWindow('<%=request.getContextPath()%>/lab/CA/ALL/labDisplayOLIS.jsp?segmentID=0&preview=true&uuid=' + uuid);
}

function save(uuid) {
	jQuery(uuid).attr("disabled", "disabled");
	jQuery.ajax({
		url: "<%=request.getContextPath() %>/olis/AddToInbox.do",
		data: "uuid=" + uuid + "&addToMyInbox=false",
		success: function(data) {
			jQuery("#action_result").html(data);
			jQuery("#resultsTable tr[uuid='"+uuid+"']").remove();
			alternate(document.getElementById("resultsTable"));
		}
	});
}

function ack(uuid) {
	jQuery(uuid).attr("disabled", "disabled");
	jQuery.ajax({
		url: "<%=request.getContextPath() %>/olis/AddToInbox.do?ack=true",
		data: "uuid=" + uuid + "&ack=true",
		success: function(data) {
			jQuery("#" + uuid + "_result").html(data);
		}
	});
}

var patientFilter = "";
var labFilter = "";
var hcnFilter = "";
var categoryFilter = "";
var performingLabFilter = "";
var abnormalFilter = "";
var testRequestCodeFilter = "";
var testRequestStatusFilter = "";
var resultStatusFilter = "";

function filterResults(select) {
	if (select.name == "labFilter") {
		labFilter = select.value;
	} else if(select.name == "patientFilter") {
		patientFilter = select.value;
	} else if(select.name == "hcnFilter") {
		hcnFilter = select.value;
	} else if(select.name == "categoryFilter") {
		categoryFilter = select.value;
	} else if(select.name == "performingLabFilter") {
		performingLabFilter = select.value;
	} else if(select.name == "abnormalFilter") {
		abnormalFilter = select.value;
	}  else if(select.name == "testRequestCodeFilter") {
		testRequestCodeFilter = select.value;
	} else if(select.name == "testRequestStatusFilter") {
		testRequestStatusFilter = select.value;
	} else if(select.name == "resultStatusFilter") {
		resultStatusFilter = select.value;
	}
	
	var performFilter = function() {
		var visible = (patientFilter == "" || jQuery(this).attr("patientName") == patientFilter)
				   && (labFilter == "" || jQuery(this).attr("reportingLaboratory") == labFilter)
				    && (hcnFilter == "" || jQuery(this).attr("hcn") == hcnFilter)
				    && (categoryFilter == "" || jQuery(this).attr("category").indexOf(categoryFilter) != -1) 
				    && (performingLabFilter == "" || jQuery(this).attr("performingLaboratory") == performingLabFilter)
				    && (abnormalFilter == "" || jQuery(this).attr("abnormal") == abnormalFilter) 
				    && (testRequestCodeFilter == "" || jQuery(this).attr("testRequestCode") == testRequestCodeFilter)
				     && (testRequestStatusFilter == "" || jQuery(this).attr("testRequestStatus") == testRequestStatusFilter)
				     && (resultStatusFilter == "" || jQuery(this).attr("resultStatus").indexOf(resultStatusFilter) != -1);
		
		
		if (visible) { 
			jQuery(this).show(); 
		} else { 
			jQuery(this).hide(); 
		}
	};
	
	jQuery(".evenLine").each(performFilter);
	jQuery(".oddLine").each(performFilter);
	
}

function popupPage(vheight,vwidth,varpage) { //open a new popup window
    var page = "" + varpage;
    windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";//360,680
    var popup=window.open(page, "groupno", windowprops);
    if (popup != null) {
      if (popup.opener == null) {
        popup.opener = self;
      }
      popup.focus();
    }
}

function openPatient(demographicNo) {
	popupPage(700,1200,'../demographic/demographiccontrol.jsp?demographic_no='+demographicNo+'&displaymode=edit&dboperation=search_detail');
}

jQuery(document).ready(function(){
	alternate(document.getElementById("resultsTable"));
	
	jQuery("input[name^='remove_']").bind('change',function(){
		var uuid = (jQuery(this).attr('uuid'));
		if(jQuery(this).is(":checked")) {
			jQuery("input[name^='addToInbox_"+uuid+"']").attr("checked","");
			jQuery("input[name^='acknowledge_"+uuid+"']").attr("checked","");
		}
	});
	
	jQuery("input[name^='addToInbox_']").bind('change',function(){
		var uuid = (jQuery(this).attr('uuid'));
		if(jQuery(this).is(":checked")) {
			jQuery("input[name^='remove_"+uuid+"']").attr("checked","");
			jQuery("input[name^='acknowledge_"+uuid+"']").attr("checked","");
		}
	});
	
	jQuery("input[name^='acknowledge_']").bind('change',function(){
		var uuid = (jQuery(this).attr('uuid'));
		if(jQuery(this).is(":checked")) {
			jQuery("input[name^='remove_"+uuid+"']").attr("checked","");
			jQuery("input[name^='addToInbox_"+uuid+"']").attr("checked","");
		}
	});
});

function checkAllRemoved() {
	jQuery("input[name^='remove_']").each(function() {
		jQuery(this).attr("checked","checked");
		var uuid = (jQuery(this).attr('uuid'));
		jQuery("input[name^='addToInbox_"+uuid+"']").attr("checked","");
		jQuery("input[name^='acknowledge_"+uuid+"']").attr("checked","");
	});	
}

function checkAllAddToInbox() {
	jQuery("input[name^='addToInbox_']").each(function() {
		jQuery(this).attr("checked","checked");
		var uuid = (jQuery(this).attr('uuid'));
		jQuery("input[name^='remove_"+uuid+"']").attr("checked","");
		jQuery("input[name^='acknowledge_"+uuid+"']").attr("checked","");
	});	
}

function checkAllAcknowledge() {
	jQuery("input[name^='acknowledge_']").each(function() {
		jQuery(this).attr("checked","checked");
		var uuid = (jQuery(this).attr('uuid'));
		jQuery("input[name^='addToInbox_"+uuid+"']").attr("checked","");
		jQuery("input[name^='remove_"+uuid+"']").attr("checked","");
	});	
}

function bulkProcess() {
	var reqData = {};
	var items = [];
	reqData.items = items;
	
	jQuery("input[name^='addToInbox_']:checked").each(function() {
		var item = {};
		item.uuid = jQuery(this).attr('uuid');
		item.type='addToInbox';
		items.push(item);
	});
	
	jQuery("input[name^='acknowledge_']:checked").each(function() {
		var item = {};
		item.uuid = jQuery(this).attr('uuid');
		item.type='acknowledge';
		items.push(item);
	});	
	
	jQuery("input[name^='remove_']:checked").each(function() {
		var item = {};
		item.uuid = jQuery(this).attr('uuid');
		item.type='remove';
		items.push(item);
	});	
	
	jQuery.ajax({
		url: "<%=request.getContextPath() %>/olis/AddToInbox.do",
		method: "POST",
		data: "method=bulkProcess&data=" + btoa(JSON.stringify(reqData)),
		dataType: 'json',
		success: function(data) {
			console.log(JSON.stringify(data));
			if(data.successIds) {
				for(var x=0;x<data.successIds.length;x++) {
					console.log(data.successIds[x]);
					jQuery("#resultsTable tr[uuid='"+data.successIds[x]+"']").remove();
					
				}
				alternate(document.getElementById("resultsTable"));
			}
		}
	});
	
}

</script>

<style type="text/css">
.oddLine { 
	background-color: #eee;
}
.evenLine {background-color: #fff; } 

.error {
	border: 1px solid red;
	color: red;
	font-weight: bold;
	margin: 10px;
	padding: 10px;
}

</style>
	
<%
	OLISRequestNomenclatureDao olisRequestNomenclatureDao = SpringUtils.getBean(OLISRequestNomenclatureDao.class);
	OLISResultNomenclatureDao olisResultNomenclatureDao = SpringUtils.getBean(OLISResultNomenclatureDao.class);
%>
<title>OLIS Search Results</title>
</head>
<body>

<table style="width:100%;" class="MainTable" align="left">
	<tbody><tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" width="175">OLIS</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tbody><tr>
				<td>Results</td>
				<td>&nbsp;</td>
				<td style="text-align: right"><a href="javascript:void(0)" onClick="popupPage(800,1000,'log.jsp')">OLIS Log</a>
			</tr>
			</tbody>
		</table>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<%
			if (request.getAttribute("searchException") != null) {
			%>
				<div class="error">Could not perform the OLIS query due to the following exception:<br /><%=((Exception) request.getAttribute("searchException")).getLocalizedMessage() %></div>
			<%
			} %>
			
			<%
			if (request.getAttribute("errors") != null) {
				// Show the errors to the user				
				for (String error : (List<String>) request.getAttribute("errors")) { %>
					<div class="error"><%=error.replaceAll("\\n", "<br />") %></div>
				<% }
			}
			String resp = (String) request.getAttribute("olisResponseContent");
			if(resp == null) { resp = ""; }
			%>
			<!--  RAW HL7
				<%=resp%>
			-->
			<%
			boolean hasBlockedContent = false;
			try {
				if(resp != null && resp.length()>0) {
					OLISHL7Handler reportHandler = (OLISHL7Handler) Factory.getHandler("OLIS_HL7", resp);
					if(reportHandler != null) {
						List<OLISError> errors = reportHandler.getReportErrors();
						if (errors.size() > 0) {
							for (OLISError error : errors) {
							%>
								<div class="error"><%=error.getIndentifer()%>:<%=error.getText().replaceAll("\\n", "<br />")%></div>
							<%
							}
						}
						hasBlockedContent = reportHandler.isReportBlocked();
					}
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("error",e);
			}
			if (hasBlockedContent) { 
			%>
			<form  action="<%=request.getContextPath()%>/olis/Search.do" onsubmit="return confirm('Are you sure you want to resubmit this query with a patient consent override?')">
				<input type="hidden" name="redo" value="true" />
				<input type="hidden" name="uuid" value="<%=(String)request.getAttribute("searchUuid")%>" />
				<input type="hidden" name="force" value="true" />				
				<input type="submit" value="Submit Override Consent" /> 
				Authorized by: 
				<select id="blockedInformationIndividual" name="blockedInformationIndividual">
					<option value="patient">Patient</option>
					<option value="substitute">Substitute Decision Maker</option>					
				</select>
			</form>
			<%
			}
			List<String> resultList = (List<String>) request.getAttribute("resultList");
			
			
			if (resultList != null) {
			%>
			<table style="width:90%">
				
				<% if (resultList.size() > 0) { 
					List<String> names = new ArrayList<String>();
					List<String> hcns = new ArrayList<String>();
					List<String> labs = new ArrayList<String>();
					List<String> categories = new ArrayList<String>();
					
					List<String> performingLabs = new ArrayList<String>();
					List<String> resultStatuses = new ArrayList<String>();
					List<String> abnormals = new ArrayList<String>();
					List<String> testRequestCodes = new ArrayList<String>();
					List<String> testRequestStatuses = new ArrayList<String>();
					
					OLISHL7Handler result;
					
					Map<String,Boolean> duplicates = new HashMap<String,Boolean>();
					
					
					for (String resultUuid : resultList) {
						 result = OLISResultsAction.searchResultsMap.get(resultUuid);
						 
						 //is there an OLIS duplicate. Have we gotten this before?
						//boolean dup1 = FileUploadCheck.hasFileBeenUploadedByFileLocation(System.getProperty("java.io.tmpdir") + "/olis_" + resultUuid + ".response");
					//	boolean dup2 = OLISUtils.isDuplicate(LoggedInInfo.getLoggedInInfoFromSession(request), new File(System.getProperty("java.io.tmpdir") + "/olis_" + resultUuid + ".response"));
						
					//	if(dup2) {
					//		duplicates.put(resultUuid,true);
					//		continue;
					//	}
							
						String hcn = oscar.Misc.getStr(result.getHealthNum(), "").trim();
						if (!hcn.equals("")) { hcns.add(hcn);}
						String name = oscar.Misc.getStr(result.getPatientName(), "").trim();
						if (!name.equals("")) { names.add(name); }
						String reportingLab = oscar.Misc.getStr(result.getReportingFacilityName(), "").trim();
						if (!reportingLab.equals("")) { labs.add(reportingLab); }
						String[] category = result.getCategoryList(",").split(",");
						for(String c:category) {
							if (!c.equals("")) { categories.add(c); }
						}
						String performingLab = oscar.Misc.getStr(result.getPerformingFacilityNameOnly(), "").trim();
						if (!performingLab.equals("")) { performingLabs.add(performingLab); }
						String testRequestCode = oscar.Misc.getStr(result.getTestRequestCode(), "").trim();
						if (!testRequestCode.equals("")) { testRequestCodes.add(testRequestCode); }
						String abnormal = oscar.Misc.getStr(result.hasAbnormalResult() ? "true" : "false", "").trim();
						if (!abnormal.equals("")) { abnormals.add(abnormal); }
						
						String resultStatus = oscar.Misc.getStr(result.getTestResultStatuses(),"").trim();
						for(String rs:resultStatus.split(",")) {
							if (!rs.equals("")) { resultStatuses.add(rs); }
						}
						
						String orderStatus = oscar.Misc.getStr(result.getOrderStatus(), "").trim();
						for(String rs:orderStatus.split(",")) {
							if (!rs.equals("")) { testRequestStatuses.add(rs); }
						}
					//	if (!orderStatus.equals("")) { testRequestStatuses.add(orderStatus); }
						
						
						
					}
				
				%>
					<tr>
						<td style="text-align:right"><b>Patient:</b></td>
						<td>
							<select name="patientFilter" onChange="filterResults(this)">
								<option value="">All Patients</option>
								<%  
									for (String tmp: new HashSet<String>(names)) {
								%>
									<option value="<%=tmp%>"><%=tmp%></option>
								<% } %>
							</select>
						</td>
						<td style="text-align:right"><b>HCN:</b></td>
						<td>
							<select name="hcnFilter" onChange="filterResults(this)">
								<option value="">All HCNs</option>
								<% 
									for (String tmp: new HashSet<String>(hcns)) {
								%>
									<option value="<%=tmp%>"><%=tmp%></option>
								<% } %>
							</select>
						</td>
						<td style="text-align:right"><b>Category:</b></td>
						<td>
							<select name="categoryFilter" onChange="filterResults(this)">
								<option value="">All Categories</option>
								<% 
									for (String tmp: new HashSet<String>(categories)) {
								%>
									<option value="<%=tmp%>"><%=tmp%></option>
								<% } %>
							</select>
						</td>
					</tr>
					
					<tr>
						<td style="text-align:right"><b>Reporting Lab:</b></td>
						<td>	
							<select name="labFilter" onChange="filterResults(this)">
								<option value="">All Reporting Labs</option>
								<% 
									for (String tmp: new HashSet<String>(labs)) {
								%>
									<option value="<%=tmp%>"><%=tmp%></option>
								<% } %>
							</select>
						</td>
						<td style="text-align:right"><b>Performing Lab:</b></td>
						<td>
							<select name="performingLabFilter" onChange="filterResults(this)">
								<option value="">All Performing Labs</option>
								<% 
									for (String tmp: new HashSet<String>(performingLabs)) {
								%>
									<option value="<%=tmp%>"><%=tmp%></option>
								<% } %>
							</select>
						</td>
						<td style="text-align:right"><b>Result Status:</b></td>
						<td>
							<select name="resultStatusFilter" onChange="filterResults(this)">
								<option value="">All Result Statuses</option>
								<% 
									for (String tmp: new HashSet<String>(resultStatuses)) {
								%>
									<option value="<%=tmp%>">
									<%=	OLISHL7Handler.getTestResultStatusMessage(tmp.charAt(0))%>
									</option>
								<% } %>
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align:right"><b>Abnormal:</b></td>
						<td>
							
							<select name="abnormalFilter" onChange="filterResults(this)">
								<option value="">All Normal and Abnormal</option>
								<% 
									for (String tmp: new HashSet<String>(abnormals)) {
								%>
									<option value="<%=tmp%>">
									<%if("true".equals(tmp)) { out.print("Abnormal"); } if("false".equals(tmp)) { out.print("Normal"); } %>
									</option>
								<% } %>
							</select>
						</td>
						<td style="text-align:right"><b>Test Request Code:</b></td>
						<td>
							
							<select name="testRequestCodeFilter" onChange="filterResults(this)">
								<option value="">All Test Request Codes</option>
								<% 
									for (String tmp: new HashSet<String>(testRequestCodes)) {
										OLISRequestNomenclature item =  olisRequestNomenclatureDao.findByNameId(tmp);
										
										
								%>
									<option value="<%=tmp%>"><%=item!=null?item.getName():tmp%></option>
								<% } %>
							</select>
						</td>
						<td style="text-align:right"><b>Test Request Status:</b></td>
						<td>
							
							<select name="testRequestStatusFilter" onChange="filterResults(this)">
								<option value="">All Test Request Statuses</option>
								<% 
									for (String tmp: new HashSet<String>(testRequestStatuses)) {
								%>
									<option value="<%=tmp%>"><%=OLISHL7Handler.getTestRequestStatusMessageShort(tmp.charAt(0))%></option>
								<% } %>
							</select>
						</td>
					</tr>
					<tr>
					<td colspan="3"><div id="action_result"  style="font-size:12px;color:blue" /></td>
				
					<td colspan="3" style="text-align:right"><%=resultList.size()-duplicates.keySet().size() %> result(s) found</td>
				</tr>
				<tr>
					<td colspan="6"></td>
				</tr>
				<tr>
					<td colspan="6" style="height:10px"></td>
				</tr>
				
					<tr><td colspan="6">
					<table class="sortable" id="resultsTable" style="width:100%">
					<thead>
						<tr>
							<th class="unsortable" style="white-space: nowrap;" title="Add to inbox"><a href="javascript:void(0)" onClick="checkAllAddToInbox()"><img src="../images/icons/103.png" border="0"/></a></th>
							<th class="unsortable" style="white-space: nowrap;" title="Add to Inbox and Acknowledge"><a href="javascript:void(0)" onClick="checkAllAcknowledge()"><img src="../images/icons/114.png" border="0"/></a></th>
							<th class="unsortable" style="white-space: nowrap;" title="Remove/Reject"><a href="javascript:void(0)" onClick="checkAllRemoved()"><img src="../images/icons/104.png" border="0"/></a></th>
							
							<th class="unsortable" style="white-space: nowrap;"></th>
							
							<th class="unsortable" style="white-space: nowrap;"></th>
						<!-- 
							<th class="unsortable" style="white-space: nowrap;"></th>
							<th class="unsortable" style="white-space: nowrap;"></th>
						-->
							<th style="white-space: nowrap;">Patient Name</th>
							<th style="white-space: nowrap;">Health Number</th>
							<th style="white-space: nowrap;">DOB</th>
							<th style="white-space: nowrap;">Sex</th>
							<th style="white-space: nowrap;">Date of Test</th>
							<th style="white-space: nowrap;">Discipline</th>
							<th style="white-space: nowrap;">Tests</th> <!-- test request name -->
							<th style="white-space: nowrap;">Status</th><!-- test request status -->
							<th style="white-space: nowrap;width:30%">Results</th>
							<th style="white-space: nowrap;">Abnormal</th>
							<th style="white-space: nowrap;">Practitioners</th>
							
						</tr>
					</thead>
					<tbody>
					<%  int lineNum = 0;
						for (String resultUuid : resultList) {
						result = OLISResultsAction.searchResultsMap.get(resultUuid);
						if(duplicates.get(resultUuid) != null && duplicates.get(resultUuid) == true) {
							LogAction.addLog(LoggedInInfo.getLoggedInInfoFromSession(request), "OLIS","duplicate", resultUuid, "","");
							continue;
						}
					%>
					<tr class="oddLine" patientName="<%=result.getPatientName()%>" reportingLaboratory="<%=result.getReportingFacilityName()%>" 
						hcn="<%=result.getHealthNum()%>" category="<%=result.getCategoryList()%>" performingLaboratory="<%=result.getPerformingFacilityNameOnly()%>"
						abnormal="<%=result.hasAbnormalResult()%>" testRequestCode="<%=result.getTestRequestCode()%>" testRequestStatus="<%=result.getOrderStatus()%>"
						resultStatus="<%=result.getTestResultStatuses()%>" uuid="<%=resultUuid%>">
						
						<td><input title="Add to my inbox" type="checkbox" name="addToInbox_<%=resultUuid%>" uuid="<%=resultUuid%>"/></td>
						<td><input title="Sign-off" type="checkbox" name="acknowledge_<%=resultUuid%>" uuid="<%=resultUuid%>"/></td>
						<td><input title="Remove/Reject" type="checkbox" name="remove_<%=resultUuid%>" uuid="<%=resultUuid%>"/</td>
						
						<td>&nbsp;</td>
						
						<td>
							<div id="<%=resultUuid %>_result"></div>
							<a href="javascript:void(0)" onClick="preview('<%=resultUuid %>'); return false;" title="Preview full report"><img src="../images/icons/172.png" border="0"/></a>
						</td>
						
						<!-- 
						<td>	
							<a href="javascript:void(0)" onClick="remove('<%=resultUuid %>');return false;" title="Remove from results"><img src="../images/icons/101.png" border="0"/></a>	
						</td>
						
						<td>
							<a href="javascript:void(0)" onClick="save('<%=resultUuid %>'); return false;" title="Save"><img src="../images/Save16.png" border="0"/></a>
						</td>
						
						-->
						
						
						<td id="name_<%=resultUuid%>">
							<%
							OLISResults r = olisResultsDao.findByUUID(resultUuid);			
							Integer demId = r.getDemographicNo();
							if(demId != null) {%>
								<a href="javascript:void(0)" onClick="openPatient('<%=demId%>')"><%=result.getPatientName() %></a>
							<%} else {%>
								<%=result.getPatientName() %><a href="javascript:void(0)" onClick="showMatch('<%=result.getLastName() + "," + result.getFirstName() %>','<%=resultUuid%>')"><img src="../images/here.gif" border="0"/></a>
							<% } %>
						</td>
						<td><%=result.getHealthNum() %></td>
					
						<td align="center" style="white-space: nowrap;"><%=result.getDOB() %></td>
						<td align="center"><%=result.getSex() %></td>
						<td style="white-space: nowrap;"><%=result.getSpecimenReceivedDateTime() %></td>
						<td style="white-space: nowrap;">
							<ul>
							<%
								String[] categories1 = result.getCategoryList(",").split(",");
								if(categories1.length == 1) {
									%><%=categories1[0]%><%
								} else {
								for(String category: categories1) {
							%>
									<li><%=category %></li>
							<% } } %>
							</ul>
						</td>
						<td style="white-space: nowrap;">
							<ul>
								<%
									String[] tests = result.getTestList(",").split(",");
								if(tests.length == 1) {%>
									<%=tests[0]%>
									
								<%} else {for(String i :tests) {  %>
									<li><%=i %></li>
								<% } }%>
							</ul>
						</td>
						<td style="white-space: nowrap;">
							<%
							String[] testRequestStatuses1 =  result.getTestRequestStatuses().split(",");
							if(testRequestStatuses1.length==1) {
								%><%=!testRequestStatuses1[0].isEmpty() ? OLISHL7Handler.getTestRequestStatusMessageShort(testRequestStatuses1[0].charAt(0))  : ""%><%
							} else if(testRequestStatuses1.length>1) {
							%>
							<ul>
							<%for(String testRequestStatus: testRequestStatuses1) { %>
								<li><%=!testRequestStatus.isEmpty() ? OLISHL7Handler.getTestRequestStatusMessageShort(testRequestStatus.charAt(0)) : ""%></li>
							<% } } %>
							</ul>
						</td>
						<td>
							
							<%
							List<String[]> testResultInfo =  result.getTestResultInfo();
							%>
							<table width="100%">
								<%for(String[] item : testResultInfo) { 
									OLISResultNomenclature orn = olisResultNomenclatureDao.findByNameId(item[0]);
								%>
								<tr>
									<td><%=orn!=null?orn.getName() : item[0] %></td>
									<%if(!item[3].equals("N") && !item[3].isEmpty()) { %>
										<td style="color:red"><%=StringEscapeUtils.escapeHtml(item[1]) %>(<%=item[3] %>)</td>
									<% } else { %>
										<td><%=StringEscapeUtils.escapeHtml(item[1]) %></td>
									<% } %>
									
									<td><%=StringEscapeUtils.escapeHtml(item[2]) %></td>
									<td><%=item[4] != null && !item[4].isEmpty() ? OLISHL7Handler.getTestResultStatusMessage(item[4].charAt(0)) : item[4] %></td>
								</tr>
								<% } %>
							</table>
						</td>
						 <td><%=result.hasAbnormalResult() ?  "<span style='color:red'>Abnormal</span>" : "" %></td>
						<td> 
						
							<%
								List<String> docs = result.getAllPractitioners();
								if(docs.size() == 1) {
									%><%=docs.get(0) %><%
								} else {
									%><%
									for(String doc:docs) {
							%> 
									<%=doc %><br/>
							<% } %>
								
							<% }%>
						
						</td>
						
					</tr>	
									
					<% } %>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="18">
								
							<input type="button" value="Process Changes" onClick="bulkProcess()"/>
							</td>
						</tr>
					</tfoot>
					</table></td></tr>
				<% } else {
				%>
					<p>No results found</p>
				<%
			}%>
			</table>
			<%
			}
			%>
		</td>
	</tr></tbody>
</table>
<!-- RAW HL7 ERP
<%=request.getAttribute("unsignedResponse") %>
-->


</body>
</html>
