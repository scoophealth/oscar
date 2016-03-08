<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@ page language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="oscar.oscarMDS.data.*,oscar.oscarLab.ca.on.*,oscar.util.StringUtils,oscar.util.UtilDateUtilities, oscar.OscarProperties" %>
<%@ page import="org.apache.commons.collections.MultiHashMap" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@page import="org.oscarehr.common.hl7.v2.oscar_to_oscar.OscarToOscarUtils"%>
<%@page import="org.oscarehr.util.MiscUtils,org.apache.commons.lang.StringEscapeUtils"%>

<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_lab" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_lab");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
@SuppressWarnings("unchecked")
ArrayList<PatientInfo> patients = (ArrayList<PatientInfo>) request.getAttribute("patients");
if (patients!=null) {
	Collections.sort(patients);
}
Integer unmatchedDocs   = (Integer) request.getAttribute("unmatchedDocs");
Integer unmatchedLabs   = (Integer) request.getAttribute("unmatchedLabs");
Integer totalDocs		= (Integer) request.getAttribute("totalDocs");
Integer totalLabs 		= (Integer) request.getAttribute("totalLabs");
if (totalLabs==null) { totalLabs = 0;}
Integer abnormalCount 	= (Integer) request.getAttribute("abnormalCount");
Integer normalCount 	= (Integer) request.getAttribute("normalCount");
Integer totalNumDocs    = (Integer) request.getAttribute("totalNumDocs");
Long categoryHash       = (Long) request.getAttribute("categoryHash");
String  providerNo		= (String) request.getAttribute("providerNo");
String searchProviderNo = (String) request.getAttribute("searchProviderNo");
String demographicNo	= (String) request.getAttribute("demographicNo");
String ackStatus 		= (String) request.getAttribute("ackStatus");

String selectedCategory        = request.getParameter("selectedCategory");
String selectedCategoryPatient = request.getParameter("selectedCategoryPatient");
String selectedCategoryType    = request.getParameter("selectedCategoryType");

String patientFirstName    = (String) request.getAttribute("patientFirstName");
String patientLastName     = (String) request.getAttribute("patientLastName");
String patientHealthNumber = (String) request.getAttribute("patientHealthNumber");

String startDate = (String) request.getAttribute("startDate");
String endDate = (String) request.getAttribute("endDate");

boolean ajax = "true".equals(request.getParameter("ajax"));
%>

<% if (!ajax) { %>
<html>

<head>
    <!-- main calendar program -->
<script type="text/javascript" src="<%= request.getContextPath() %>/share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript" src="<%= request.getContextPath() %>/share/calendar/lang/<bean:message key='global.javascript.calendar'/>"></script>
<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="<%= request.getContextPath() %>/share/calendar/calendar-setup.js"></script>
<!-- calendar style sheet -->
<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/share/calendar/calendar.css" title="win2k-cold-1" />


<script type="text/javascript" src="<%=request.getContextPath()%>/share/javascript/prototype.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/scriptaculous.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/oscarMDSIndex.js"></script>

<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/jquery/jquery.form.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/share/yui/js/yahoo-dom-event.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/share/yui/js/connection-min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/share/yui/js/animation-min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/share/yui/js/datasource-min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/share/yui/js/autocomplete-min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/demographicProviderAutocomplete.js"></script>        

        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/share/yui/css/fonts-min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/share/yui/css/autocomplete.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/share/css/demographicProviderAutocomplete.css"  />
        
        <link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/share/css/oscarMDSIndex.css"  />

  <script type="text/javascript" src="<%=request.getContextPath()%>/dms/showDocument.js"></script>        



<title>
<bean:message key="oscarMDS.index.title"/>
</title>
<html:base/>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/oscarMDS/encounterStyles.css">

<script type="text/javascript" >
	jQuery.noConflict();
	
	jQuery(window).on("scroll",handleScroll());
	
	function renderCalendar(id,inputFieldId){
    	Calendar.setup({ inputField : inputFieldId, ifFormat : "%Y-%m-%d", showsTime :false, button : id });
        
	}
	
	function split(id) {
		var loc = "<%= request.getContextPath()%>/oscarMDS/Split.jsp?document=" + id;
		popupStart(1100, 1100, loc, "Splitter");
	}

	var page = 1;
	var pageSize = 20;
	var selected_category = <%=(selectedCategory == null ? "1" : selectedCategory)%>;
	var selected_category_patient = <%=(selectedCategoryPatient == null ? "\"\"" : selectedCategoryPatient)%>;
	var selected_category_type = <%=(selectedCategoryType == null ? "\"\"" : selectedCategoryType)%>;
	var searchProviderNo = "<%=(searchProviderNo == null ? "" : searchProviderNo)%>";
	var firstName = "<%=(patientFirstName == null ? "" : patientFirstName)%>";
	var lastName = "<%=(patientLastName == null ? "" : patientLastName)%>";
	var hin = "<%=(patientHealthNumber == null ? "" : patientHealthNumber)%>";
	var providerNo = "<%=(providerNo == null ? "" : providerNo)%>";
	var searchStatus = "<%=(ackStatus == null ? "": ackStatus)%>";
	var url = "<%=request.getContextPath()%>/dms/inboxManage.do?";
	var contextpath = "<%=request.getContextPath()%>";
	var startDate = "<%= startDate %>";
	var endDate = "<%= endDate %>";
	var request = null;
	var canLoad = true;
	var isListView = false;
	var loadingDocs = false;
	var currentBold = false;
	var oldestDate = null;

	window.changePage = function (p) {
		if (p == "Next") { page++; }
		else if (p == "Previous") { page--; }
		else { page = p; }
		if (request != null) { request.transport.abort(); }
		request = updateListView();
	};

	function handleScroll(e) {
		if (!canLoad || loadingDocs) { return false; }
		var evt = e || window.event;
		var loadMore = false;
	    if (isListView && evt.scrollHeight > $("listViewDocs").clientHeight && evt.scrollTop > 0 && evt.scrollTop + evt.offsetHeight >= evt.scrollHeight) {
	    	loadMore = true;
	    }
	    else if (isListView && evt.scrollHeight <= $("listViewDocs").clientHeight) {
	    	loadMore = true;
	    }
	    else if (!isListView && evt.scrollTop + evt.offsetHeight >= evt.scrollHeight) {
	    	loadMore = true;
	    }
	    if (loadMore) {
	    	loadingDocs = true;
        	changePage("Next");
	    }
	}

	function fakeScroll() {
		var scroller;
		if (isListView) {
			scroller = document.getElementById("summaryView");
		}
		else {
			scroller = document.getElementById("docViews");
		}
 		handleScroll(scroller);
	}

	function updateListView() {
		var query = getQuery();
		if (page == 1) {
			document.getElementById("docViews").innerHTML = "";
			canLoad = true;
		}

		if (isListView && page == 1) {
			document.getElementById("docViews").innerHTML =	"<div id='tempLoader'><img src='<%=request.getContextPath()%>/images/DMSLoader.gif'> Loading reports...</div>"
		}
		else if (isListView) {
			document.getElementById("loader").style.display = "block";
		}
		else {
			jQuery("#docViews").append(jQuery("<div id='tempLoader'><img src='<%=request.getContextPath()%>/images/DMSLoader.gif'> Loading reports...</div>"));
		}
		var div;
		if (!isListView || page == 1) {
			div = document.getElementById("docViews");
		}
		else {
			div = document.getElementById("summaryView");
		}	
		jQuery("#readerSwitcher").prop("disabled",true);
		jQuery("#listSwitcher").prop("disabled",true);
		return new Ajax.Updater(div,url,{method:'get',parameters:query,insertion:Insertion.Bottom,evalScripts:true,onSuccess:function(transport){
			loadingDocs = false;
			var tmp = jQuery("#tempLoader");
			if (tmp != null) { tmp.remove(); }
			if (isListView) {
				if (page == 1) { jQuery("#tempLoader").remove(); }
				else { document.getElementById("loader").style.display = "none"; }
			}

			if (page == 1) {
				if (isListView) {
					document.getElementById("docViews").style.overflow = "hidden";
				}
				else {
					document.getElementById("docViews").style.overflow = "auto";
				}
			}
			if (transport.responseText.indexOf("<input type=\"hidden\" name=\"NoMoreItems\" value=\"true\" />") >= 0) {
				canLoad = false;
			}
			else {
				// It is possible that the current amount of loaded items has not filled up the page enough
				// to create a scroll bar. So we fake a scroll (since no scroll bar is equivalent to reaching the bottom).
				setTimeout("fakeScroll();", 1000);
			}
			
			jQuery("#readerSwitcher").prop("disabled",false);
			jQuery("#listSwitcher").prop("disabled",false);
		}});
	}

	function getQuery() {
		var CATEGORY_ALL = 1,CATEGORY_DOCUMENTS = 2,CATEGORY_HL7 = 3,CATEGORY_NORMAL = 4,CATEGORY_ABNORMAL = 5,CATEGORY_PATIENT = 6,CATEGORY_PATIENT_SUB = 7,CATEGORY_TYPE_DOC = 'DOC',CATEGORY_TYPE_HL7 = 'HL7';
		var query = "method=prepareForContentPage";
		query +="&searchProviderNo="+searchProviderNo+"&providerNo="+providerNo+"&status="+searchStatus+"&page="+page
			   +"&pageSize="+pageSize+"&isListView="+(isListView?"true":"false") 
			   +"&startDate=" + startDate + "&endDate=" + endDate;
		switch (selected_category) {
		case CATEGORY_ALL:
			query  += "&view=all";
			query  += "&fname=" + firstName + "&lname=" + lastName + "&hnum=" + hin;
			break;
		case CATEGORY_DOCUMENTS:
			query  += "&view=documents";
			query  += "&fname=" + firstName + "&lname=" + lastName + "&hnum=" + hin;
			break;
		case CATEGORY_HL7:
			query  += "&view=labs";
			query  += "&fname=" + firstName + "&lname=" + lastName + "&hnum=" + hin;
			break;
		case CATEGORY_NORMAL:
			query  += "&view=normal";
			query  += "&fname=" + firstName + "&lname=" + lastName + "&hnum=" + hin;
			break;
		case CATEGORY_ABNORMAL:
			query  += "&view=abnormal";
			query  += "&fname=" + firstName + "&lname=" + lastName + "&hnum=" + hin;
			break;
		case CATEGORY_PATIENT:
			query  += "&view=all&demographicNo=" + selected_category_patient;
			break;
	    case CATEGORY_PATIENT_SUB:
	    	query  += "&demographicNo=" + selected_category_patient;
	    	switch (selected_category_type) {
		    	case CATEGORY_TYPE_DOC:
		    		query  += "&view=documents";
					break;
		    	case CATEGORY_TYPE_HL7:
		    		query  += "&view=labs";
					break;
	    	}
	    	break;
	    }

		if (oldestLab != null)
			query += "&newestDate=" + encodeURIComponent(oldestLab);

		return query;
	}

	function changeView(type,patientId,subtype) {
		loadingDocs = true;
		selected_category = type;
		selected_category_patient = patientId;
		selected_category_type = subtype;
		document.getElementById("docViews").innerHTML = "";
		changePage(1);
	}

	function switchView() {
		loadingDocs = true;
		isListView = !isListView;
		jQuery("input[name=isListView]").val(isListView);
		document.getElementById("docViews").innerHTML = "";
		var list = document.getElementById("listSwitcher");
		var view = document.getElementById("readerSwitcher");
		var active, passive;
		if (isListView) {
			pageSize = 20;
			active = view;
			passive = list;
		}
		else {
			pageSize = 5;
			active = list;
			passive = view;
		}
		active.style.display = "inline";
		passive.style.display = "none";

		changePage(1);
	}

	jQuery(document).ready(function() {
		isListView = <%= (selectedCategoryPatient != null) %>;
		jQuery('input[name=isListView]').val(isListView);
		switchView();
		//un_bold($("totalAll"));
		currentBold = "totalAll";
		refreshCategoryList();
	});
	function ForwardSelectedRows() {
		var query = jQuery(document.reassignForm).formSerialize();
		var labs = jQuery("input[name='flaggedLabs']:checked");
		for (var i = 0; i < labs.length; i++) {
			query += "&flaggedLabs=" + labs[i].value;
			query += "&" + jQuery(labs[i]).next().name + "=" + jQuery(labs[i]).next().value;
		}
		jQuery.ajax({
			type: "POST",
			url:  "<%= request.getContextPath()%>/oscarMDS/ReportReassign.do",
			data: query,
			success: function (data) {
				jQuery("input[name='flaggedLabs']:checked").each(function () {
					this.checked = false;
				});
			}
		});
	}
	window.FileSelectedRows = function () {
		var query = jQuery(document.reassignForm).formSerialize();
		var labs = jQuery("input[name='flaggedLabs']:checked");
		for (var i = 0; i < labs.length; i++) {
			query += "&flaggedLabs=" + labs[i].value;
			query += "&" + labs[i].next().name + "=" + labs[i].next().value;
		}
		jQuery.ajax({
			type: "POST",
			url:  "<%= request.getContextPath()%>/oscarMDS/FileLabs.do",
			data: query,
			success: function (data) {
				updateCategoryList();

				jQuery("input[name='flaggedLabs']:checked").each(function () {
					jQuery(this).parent().parent().remove();
				});

				// We may have removed enough items that the scroll bar is missing so we need to
				// check and retrieve more items if so.
				fakeScroll();
			}
		});
	}

	function refreshCategoryList() {
		jQuery("#categoryHash").val("-1");
		updateCategoryList();
	}

	function updateCategoryList() {
		jQuery.ajax({
			type: "GET",
			url: "<%=request.getContextPath()%>/dms/inboxManage.do",
			data: window.location.search.substr(1) + "&ajax=true",
			success: function (data) {
				if (jQuery("#categoryHash").length == 0 || jQuery(data)[2].value != jQuery("#categoryHash").val()) {
					jQuery("#categoryList").html(data);
					re_bold(currentBold);
				}
			}
		});
	}

	window.removeReport = function (reportId) {
		var el = jQuery("#labdoc_" + reportId);
		if (el != null) {
			el.remove();
		}
	}
</script>


<style type="text/css">
	.multiPage {
		background-color: RED;
		color: WHITE;
		font-weight:bold;
		padding: 0px 5px;
		font-size: medium;
	}
	.singlePage {

	}
	
	

	.TDISRes	{font-weight: bold; font-size: 10pt; color: black; font-family:
               Verdana, Arial, Helvetica}
</style>
</head>

<body oldclass="BodyStyle" vlink="#0000FF"  >
    <form name="reassignForm" method="post" action="ReportReassign.do" id="lab_form">
        <table  oldclass="MainTable" id="scrollNumber1" border="0" name="encounterTable" cellspacing="0" cellpadding="3" width="100%">
            <tr oldclass="MainTableTopRow">
                <td class="MainTableTopRowRightColumn" colspan="10" align="left">
                 <table width="100%">

                        <tr>
                            <td align="left" valign="center" > <%-- width="30%" --%>
                                <input type="hidden" name="providerNo" value="<%= providerNo %>" />
                                <input type="hidden" name="searchProviderNo" value="<%= searchProviderNo %>" />
                                <%= (request.getParameter("lname") == null ? "" : "<input type=\"hidden\" name=\"lname\" value=\""+request.getParameter("lname")+"\">") %>
                                <%= (request.getParameter("fname") == null ? "" : "<input type=\"hidden\" name=\"fname\" value=\""+request.getParameter("fname")+"\">") %>
                                <%= (request.getParameter("hnum") == null ? "" : "<input type=\"hidden\" name=\"hnum\" value=\""+request.getParameter("hnum")+"\">") %>
                                <input type="hidden" name="status" value="<%= ackStatus %>" />
                                <input type="hidden" name="selectedProviders" />
                                <input type="hidden" name="favorites" value="" />
                                <input type="hidden" name="isListView" value="" />
                                <input id="listSwitcher" type="button" style="display:none;" class="smallButton" value="<bean:message key="inboxmanager.document.listView"/>" onClick="switchView();" />
                                <input id="readerSwitcher" type="button" class="smallButton" value="<bean:message key="inboxmanager.document.readerView"/>" onClick="switchView();" />
                                <% if (demographicNo == null) { %>
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnSearch"/>" onClick="window.location='<%=request.getContextPath()%>/oscarMDS/Search.jsp?providerNo=<%= providerNo %>'" />
                                <% } %>
                                <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnClose"/>" onClick="wrapUp()" />
                      		</td>

                            <td align="right" valign="center" width="35%">
								<span class="HelpAboutLogout">
									<oscar:help keywords="&Title=Inbox&portal_type%3Alist=Document" key="app.top1" style="color: #FFFFFF"/>
                                	| <a href="javascript:popupStart(300,400,'<%=request.getContextPath()%>/oscarEncounter/About.jsp')" style="color: #FFFFFF;" ><bean:message key="global.about"/></a>
								</span>
                                | <a href="javascript:parent.reportWindow('<%=request.getContextPath()%>/oscarMDS/ForwardingRules.jsp?providerNo=<%= providerNo %>');" style="color: #FFFFFF;" >Forwarding Rules</a>
                                | <a href="javascript:popupStart(800,1000,'<%=request.getContextPath()%>/lab/CA/ALL/testUploader.jsp')" style="color: #FFFFFF; "><bean:message key="admin.admin.hl7LabUpload"/></a>
                                <% if (OscarProperties.getInstance().getBooleanProperty("legacy_document_upload_enabled", "true")) { %>
                                | <a href="javascript:popupStart(600,500,'<%=request.getContextPath()%>/dms/html5AddDocuments.jsp')" style="color: #FFFFFF; "><bean:message key="inboxmanager.document.uploadDoc"/></a>
                                <% } else { %>
                                | <a href="javascript:popupStart(800,1000,'<%=request.getContextPath()%>/dms/documentUploader.jsp')" style="color: #FFFFFF; "><bean:message key="inboxmanager.document.uploadDoc"/></a>
                                <% } %>
								<br />
								<a href="javascript:popupStart(700,1100,'../dms/inboxManage.do?method=getDocumentsInQueues')" style="color: #FFFFFF;"><bean:message key="inboxmanager.document.pendingDocs"/></a>
                                                                | <a href="javascript:popupStart(800,1200,'<%=request.getContextPath() %>/dms/incomingDocs.jsp')" style="color: #FFFFFF;" ><bean:message key="inboxmanager.document.incomingDocs"/></a>
								| <a href="javascript:popupStart(800,1000, '<%=request.getContextPath() %>/oscarMDS/CreateLab.jsp')" style="color: #FFFFFF;"><bean:message key="global.createLab" /></a>
                                | <a href="javascript:popupStart(800,1000, '<%=request.getContextPath() %>/olis/Search.jsp')" style="color: #FFFFFF;"><bean:message key="olis.olisSearch" /></a>
                                | <a href="javascript:popupPage(400, 400,'<html:rewrite page="/hospitalReportManager/hospitalReportManager.jsp"/>')" style="color: #FFFFFF;">HRM Status/Upload</a>

                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>

        <table id="readerViewTable" style="table-layout: fixed;border-color: blue;border-width: thin;border-spacing: 0px;background-color: #E0E1FF" width="100%" border="1">
                                                     <col width="120">
                                                     <col width="100%">
          <tr height="600px">
              <td id="categoryList" valign="top" style="overflow:hidden;border-color: blue;border-width: thin;background-color: #E0E1FF" >
<% } // end if(!ajax)
   else {
%>
					<input type="hidden" id="categoryHash" value="<%=categoryHash%>" />
                    <div style="height:600px; overflow:auto;">
                    <%
                    	//Enumeration en=patientIdNames.keys();
                        if((totalNumDocs) > 0){
                    %>
                        <div>
                        	<a id="totalAll" href="javascript:void(0);" onclick="un_bold(this);changeView(CATEGORY_ALL);">
                        		All (<span id="totalNumDocs"><%=totalNumDocs%></span>)</a>
                        </div>
                        <br>

    				<% }

                       if((totalDocs)>0){
					%>
						<div>
							<a id="totalDocs" href="javascript:void(0);" onclick="un_bold(this);changeView(CATEGORY_DOCUMENTS);"
							   title="Documents">Documents (<span id="totalDocsNum"><%=totalDocs%></span>)
						   </a>
					   </div>
                     <%} if((totalLabs)>0){%>
                       <div>
                            <a id="totalHL7s" href="javascript:void(0);" onclick="un_bold(this);changeView(CATEGORY_HL7);" title="HL7">
                           		HL7 (<span id="totalHL7Num"><%=totalLabs%></span>)
                           	</a>
                       </div>

					<%}
					  if(totalLabs>0){%><br><%}

					%>
					    <div>
					    	<a id="totalNormals" href="javascript:void(0);" onclick="un_bold(this);changeView(CATEGORY_NORMAL);" title="Normal">
					    		Normal
				    		</a>
		    			</div>

						<div>
    						<a id="totalAbnormals" href="javascript:void(0);" onclick="un_bold(this);changeView(CATEGORY_ABNORMAL);" title="Abnormal">
    							Abnormal
   							</a>
						</div>
						<dl id="patientsdoclabs">
				    <%
				         for (PatientInfo info : patients) {
				                        String patientId= info.id + "";
				                        String patientName= info.toString();
				                        int numDocs= info.getDocCount() + info.getLabCount();
				   %>

   					   <dt> <img id="plus<%=patientId%>" alt="plus" src="<%=request.getContextPath()%>/images/plus.png" onclick="showhideSubCat('plus','<%=patientId%>');"/>
       					    <img id="minus<%=patientId%>" alt="minus" style="display:none;" src="<%=request.getContextPath()%>/images/minus.png" onclick="showhideSubCat('minus','<%=patientId%>');"/>
       						<a id="patient<%=patientId%>all" href="javascript:void(0);"  onclick="un_bold(this);changeView(CATEGORY_PATIENT,<%=patientId%>);" title="<%=patientName%>"><%=patientName%> (<span id="patientNumDocs<%=patientId%>"><%=numDocs%></span>)</a>
                    		<dl id="labdoc<%=patientId%>showSublist" style="display:none" >
                   <%if (info.getDocCount() > 0) {%>
                        		<dt>
                        			<a id="patient<%=patientId%>docs" href="javascript:void(0);" onclick="un_bold(this);changeView(CATEGORY_PATIENT_SUB,<%=patientId%>,CATEGORY_TYPE_DOC);" title="Documents">
                        				Documents (<span id="pDocNum_<%=patientId%>"><%=info.getDocCount()%></span>)
                       				</a>
                        		</dt>
                   <%} if (info.getLabCount() > 0) {%>
                     			<dt>
                     				<a id="patient<%=patientId%>hl7s" href="javascript:void(0);" onclick="un_bold(this);changeView(CATEGORY_PATIENT_SUB,<%=patientId%>,CATEGORY_TYPE_HL7);" title="HL7">
                     					HL7 (<span id="pLabNum_<%=patientId%>"><%=info.getLabCount()%></span>)
                   					</a>
                        		</dt>
                   <%}%>
                    		</dl>
                    	</dt>

			<% if (selectedCategoryPatient != null) { if (selectedCategoryPatient.equals(Integer.toString(info.id))) { %>
			<script>
				showhideSubCat('plus','<%=info.id%>');
				un_bold($('patient<%=info.id%><%=(selectedCategoryType.equals("CATEGORY_TYPE_HL7"))?"hl7s":(selectedCategoryType.equals("CATEGORY_TYPE_DOC")?"docs":"all")%>'));
			</script>
			<% } } %>
                   <%}%>

				   <% if (unmatchedDocs > 0 || unmatchedLabs > 0) { %>
						<dt> <img id="plus0" alt="plus" src="<%=request.getContextPath()%>/images/plus.png" onclick="showhideSubCat('plus','0');"/>
       					    <img id="minus0" alt="minus" style="display:none;" src="<%=request.getContextPath()%>/images/minus.png" onclick="showhideSubCat('minus','0');"/>
       						<a id="patient0all" href="javascript:void(0);"  onclick="un_bold(this);changeView(CATEGORY_PATIENT,0)" title="Unmatched">Unmatched (<span id="patientNumDocs0"><%=unmatchedDocs + unmatchedLabs%></span>)</a>
                    		<dl id="labdoc0showSublist" style="display:none" >
                   <%if (unmatchedDocs > 0) {%>
                        		<dt>
                        			<a id="patient0docs" href="javascript:void(0);" onclick="un_bold(this);changeView(CATEGORY_PATIENT_SUB,0,CATEGORY_TYPE_DOC);" title="Documents">
                        				Documents (<span id="pDocNum_0"><%=unmatchedDocs%></span>)
                       				</a>
                        		</dt>
                   <%} if (unmatchedLabs > 0) {%>
                     			<dt>
                     				<a id="patient0hl7s" href="javascript:void(0);" onclick="un_bold(this);changeView(CATEGORY_PATIENT_SUB,0,CATEGORY_TYPE_HL7);" title="HL7">
                     					HL7 (<span id="pLabNum_0"><%=unmatchedLabs%></span>)
                   					</a>
                        		</dt>
                   <%}%>
                    		</dl>
                    	</dt>

					<% } %>

                  	</dl>
                  	</div>
                  	
<%  } //end else
	if (!ajax) {
%>
             </td>
             <td style="width:100%;height:600px;background-color: #E0E1FF">
                 <div id="docViews" style="width:100%;height:600px;overflow:auto;" onscroll="handleScroll(this)">

                 </div>
             </td>
          </tr>
     </table>
     </form>
</body>
</html>
<% } // end if(!ajax) %>
