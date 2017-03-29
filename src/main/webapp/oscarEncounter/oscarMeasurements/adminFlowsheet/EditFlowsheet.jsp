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
<% long startTime = System.currentTimeMillis(); %>
<%@ page import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*,oscar.oscarEncounter.oscarMeasurements.*,oscar.oscarEncounter.oscarMeasurements.bean.*,java.net.*"%>
<%@ page import="org.jdom.Element,oscar.oscarEncounter.oscarMeasurements.data.*,org.jdom.output.Format,org.jdom.output.XMLOutputter,oscar.oscarEncounter.oscarMeasurements.util.*,java.io.*" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page import="org.oscarehr.common.dao.*,org.oscarehr.common.model.FlowSheetCustomization"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.MeasurementTemplateFlowSheetConfig"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.FlowSheetItem"%>

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Demographic" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed2=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_flowsheet" rights="w" reverse="<%=true%>">
	<%authed2=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_flowsheet");%>
</security:oscarSec>
<%
if(!authed2) {
	return;
}
%>

<%
    long startTimeToGetP = System.currentTimeMillis();
    String module="";
    String htQueryString = "";
    if(request.getParameter("htracker")!=null){
    	module="htracker";
    	htQueryString="&"+module;	
    }
    
    if(request.getParameter("htracker")!=null && request.getParameter("htracker").equals("slim")){
    	module="slim";
    	htQueryString=htQueryString+"=slim";
    }
    
    String temp = "";
    if(request.getParameter("flowsheet") != null){
    	temp = request.getParameter("flowsheet");
    }else{
		temp = "tracker";
    }

    String flowsheet = temp;
    String demographic = request.getParameter("demographic");
    MeasurementTemplateFlowSheetConfig templateConfig = MeasurementTemplateFlowSheetConfig.getInstance();
    Hashtable<String, String> flowsheetNames = templateConfig.getFlowsheetDisplayNames();

    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    FlowSheetCustomizationDao flowSheetCustomizationDao = (FlowSheetCustomizationDao) ctx.getBean("flowSheetCustomizationDao");
    List<FlowSheetCustomization> custList = null;
    if(demographic == null || demographic.isEmpty()) {
    	custList = flowSheetCustomizationDao.getFlowSheetCustomizations( flowsheet,(String) session.getAttribute("user"));
    } else {
    	custList = flowSheetCustomizationDao.getFlowSheetCustomizations( flowsheet,(String) session.getAttribute("user"),Integer.parseInt(demographic));
    }
    Enumeration en = flowsheetNames.keys();

    EctMeasurementTypesBeanHandler hd = new EctMeasurementTypesBeanHandler();
    Vector<EctMeasurementTypesBean> vec = hd.getMeasurementTypeVector();
    String demographicStr = "";
    if (demographic != null){
        demographicStr = "&demographic="+demographic;
    }

    XMLOutputter outp = new XMLOutputter();
    outp.setFormat(Format.getPrettyFormat());

    DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class); 
    Demographic demo = demographicDao.getDemographic(demographic);
%>
<!DOCTYPE html>
<html lang="en">

<head>
<title>Edit Flowsheet</title><!--I18n-->

<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet">


<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
  <script src="<%=request.getContextPath() %>/js/html5.js"></script>
<![endif]-->

<!-- Fav and touch icons -->
<link rel="apple-touch-icon-precomposed" sizes="144x144" href="ico/apple-touch-icon-144-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="ico/apple-touch-icon-114-precomposed.png">
  <link rel="apple-touch-icon-precomposed" sizes="72x72" href="ico/apple-touch-icon-72-precomposed.png">
                <link rel="apple-touch-icon-precomposed" href="ico/apple-touch-icon-57-precomposed.png">
                               <link rel="shortcut icon" href="ico/favicon.png">
                                   
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/DT_bootstrap.css">

<%
if( request.getParameter("htracker")!=null && request.getParameter("htracker").equals("slim") ){
%>
<style type="text/css">
#container-main{
width:720px !important;
}
</style>
<%}%>

<style type="text/css">

.table tbody tr:hover td, .table tbody tr:hover th {
    background-color: #FFFFAA;
}

.action-head{width:60px !important;}
.action-icon{
padding-right:10px;
opacity:0.6;
filter:alpha(opacity=60); /* For IE8 and earlier */
}

.action-icon:hover{
opacity:1;
filter:alpha(opacity=100); /* For IE8 and earlier */
}

.mode-toggle{
font-size: 14px;
padding-left:10px;
font-weight:normal;
}

#scrollToTop{
Position:fixed;
display:none;
bottom:30px;
right:15px;
}


.select-measurement{
font-size:16px;
width:250px;
}

.month-range{
width:100px !important;
}

.rule-text{
width:100px !important;
}

.list-title {
   padding-top:10px;
   padding-right: 12px;
}

#myTab{
margin-top:10px;
}
.measurement-select{
width:450px;
}
</style>

<style type="text/css" media="print">
.DoNotPrint {
	display: none;
}
</style>

</head>

<body id="editFlowsheetBody">

<%
if( request.getParameter("tracker")!=null && request.getParameter("tracker").equals("slim") ){ 
	
}else{
if(request.getParameter("demographic")==null){ %>
<div class="well well-small" id="demoHeader"></div>
<%}else{%>
<%@ include file="/share/templates/patient.jspf"%>
<div style="height:60px;"></div>
<%
}
}
%>

<div class="container" id="container-main">

<div class="row-fluid">

<h4 style="display:inline;">

<%if(demographic!=null){

String tracker = "";
if( request.getParameter("tracker")!=null && request.getParameter("tracker").equals("slim") ){ 
tracker="&tracker=slim";
}

String flowsheetPath = "";

if ( request.getParameter("htracker")!=null ){
	flowsheetPath = "HealthTrackerPage.jspf";
}else{
	flowsheetPath = "TemplateFlowSheet.jsp";
}%>

<a href="../<%=flowsheetPath%>?demographic_no=<%=demographic%>&template=<%=flowsheet%><%=tracker%>" class="btn btn-small" title="go back to <%=flowsheet%> flowsheet"><i class="icon-backward"></i></a>

<%}%> 

Flowsheet: <span style="font-weight:normal"><%=flowsheet.toUpperCase()%></span>
</h4>
		  <span class="mode-toggle">
		            <% if (demographic!=null) { %>
		             Patient 
					<security:oscarSec roleName="<%=roleName2$%>" objectName="_flowsheet" rights="w">
						| <a href="EditFlowsheet.jsp?flowsheet=<%=flowsheet%>">All Patients</a> 
					</security:oscarSec>

		            <%}else{%>
		               <i>for</i> All Patients
		            <%}%>
		  </span>
</div><!-- row -->

<div class="row-fluid">
		<div class="span12">
		
		<ul class="nav nav-tabs" id="myTab">
		<li class="list-title">Measurements:</li>
		<li class="active"><a href="#home" data-toggle="tab">All</a></li>
		<li><a href="#custom" data-toggle="tab">Custom</a></li>
		<li><a href="#add" data-toggle="tab"><i class="icon-plus-sign"></i> Add</a></li>
		</ul>

	<%if (demographic!=null) { %>
		<div class="alert alert-info">
			Any changes made to this flowsheet will be applied to this patient <strong><%=demo.getLastName()%>, <%=demo.getFirstName()%></strong> for you only.
		</div>
	 <%}else{%>
		<div class="alert">
			Any changes made to this flowsheet will be applied to all of <u>your</u> patients.
		</div>
	 <%}%>
 
<div class="tab-content">
	<div class="tab-pane active" id="home">

		<!-- Flowsheet Measurement List -->
		<table class="table table-striped table-bordered table-condensed" id="measurementTbl">
		<thead>
		<tr>
		<th style="min-width:60px;max-width:80px;"></th>
		<th style="min-width:60px;max-width:80px;">Position</th>
		<th style="min-width:100px;max-width:120px">Measurement</th>
		<th style="min-width:100px;max-width:140px">Display Name</th>
		<th style="min-width:200px;max-width:500px">Guideline</th>
		</tr>
		</thead>
		
		<tbody>
		            <%
		            MeasurementFlowSheet mFlowsheet = templateConfig.getFlowSheet(temp,custList);
		            Element va = templateConfig.getExportFlowsheet(mFlowsheet);
		
		            List<String> measurements = mFlowsheet.getMeasurementList();
				
			    int counter = 1;
		            
		            if (measurements != null) {
		                for (String mstring : measurements) {
		               %>
		                <tr>
		         		<td>
		         		<%if(mFlowsheet.getFlowSheetItem(mstring).getPreventionType()!=null){ %>
		         		<i class="icon-pencil action-icon"  rel="popover" data-container="body"  data-toggle="popover" data-placement="right" data-content="unable to edit a prevention item" data-trigger="hover" title=""></i>
		                <%}else{%>
		                <a href="UpdateFlowsheet.jsp?flowsheet=<%=temp%>&measurement=<%=mstring%><%=demographicStr%><%=htQueryString%>" title="Edit" class="action-icon"><i class="icon-pencil"></i></a>
		                <%}%>
		                <a href="FlowSheetCustomAction.do?method=delete&flowsheet=<%=temp%>&measurement=<%=mstring%><%=demographicStr%><%=htQueryString%>" title="Delete" class="action-icon"><i class="icon-trash"></i></a>
		                </td>
		                <td><%=counter%></td>
		                <td><%=mstring%></td>
		                <td title="<%=mstring%>"><%=mFlowsheet.getFlowSheetItem(mstring).getDisplayName()%></td>
		                <td title="<%=mstring%>"><%=mFlowsheet.getFlowSheetItem(mstring).getGuideline()%></td>
						</tr>
				            
		            <%	
				counter++;
		                }
		
		            }
		            %>
		 </tbody>    
		</table><!-- Flowsheet Measurement List END-->
		
		
	</div><!-- main tab -->
	
	<div class="tab-pane" id="custom">
		
		<div class="span4">
		<!--right sidebar-->
		
			<!-- Custom List -->
		    <div class="well" style="min-width: 240px">
		    <h4>Custom List:</h4>
		    <%
		    if(custList.size()==0){
	    		%>    	    		
	    		<p class="muted">No custom measurements</p>
	    		<%
	    	}else{
	    	%>
		    <table class="table table-striped table-condensed">
		
			<tbody>

	    	<%	
		    String mtype="";
		     
		    for (FlowSheetCustomization cust :custList){
		    	
		    	
		    	MeasurementTemplateFlowSheetConfig mfc = MeasurementTemplateFlowSheetConfig.getInstance() ;
		    	
		    	FlowSheetItem item = mfc.getItemFromString(cust.getPayload());

		    	try{
		    	if(item.getMeasurementType()!=null){
		    	mtype=item.getMeasurementType();
		    	//out.print(mtype);
		    	}
		    	}catch(Exception e){
	             //do nothing   
	            }
		    %>
		       <tr><td><a href="FlowSheetCustomAction.do?method=archiveMod&id=<%=cust.getId()%>&flowsheet=<%=flowsheet%><%=demographicStr%><%=htQueryString%>" class="action-icon"><i class="icon-trash"></i></a> </td> 
		       
		       <td><%=cust.getAction()%></td>
		       
		       <%if(cust.getAction().equals("add")){ %>
		       <td><%if(mtype!=null){out.print(mtype);} %> 
		       
		       <%if(cust.getMeasurement()!=null){%>
		       after <em><%=cust.getMeasurement()%></em> 
		       <%}%>

		       </td> 
		       
		       <%}else{ %>
		       <td><%=cust.getMeasurement()%></td> 
		       <%} %>
		       <td><%=cust.getProviderNo()%> </td> <td> 
		       
		       <%if(cust.getDemographicNo().equals("0")){ %>
		       All Patients
		       <%}else{ %>
		       <a href="<%=request.getContextPath() %>/demographic/demographiccontrol.jsp?demographic_no=<%=cust.getDemographicNo()%>&displaymode=edit&dboperation=search_detail" target="_blank"><%=cust.getDemographicNo()%></a>
		       <%} %>
		       </td></tr>
		    <%
		    	}
		    %>
		    </tbody>
		    </table><!-- Custom List END-->		
		    <%} %>
			</div><!-- well -->
			
		 </div><!-- span4 -->
		
	</div><!-- custom tab -->
	
	<!-- ADD NEW MEAS -->
<div class="tab-pane" id="add">


<form name="FlowSheetCustomActionForm" id="FlowSheetCustomActionForm" class="well" action="FlowSheetCustomAction.do" method="post">
		    <%if(request.getParameter("htracker")!=null){ %>
		    <input type="hidden" name="htracker" value="<%=module%>">
		    <%}%>   
            <input type="hidden" name="flowsheet" value="<%=temp%>"/>
            <input type="hidden" name="method" value="save"/>
            <%if (demographic !=null){%>
                    <input type="hidden" name="demographic" value="<%=demographic%>"/>
            <%}%>
          
	
		<h4>Select a Measurement</h4>
		<select name="measurement" class="measurement-select">
                	<option value="0">choose:</option>
                    <% for (EctMeasurementTypesBean measurementTypes : vec){ %>
                    <option value="<%=measurementTypes.getType()%>" ><%=measurementTypes.getTypeDisplayName()%> (<%=measurementTypes.getType()%>) </option>
                    <% } %>
        </select>
		
	    <h4>Customize Measurement</h4>
		<table>
		<tr><td>Display Name:</td><td><input type="text" name="display_name" id="display_name" required/></td></tr>
                <tr><td>Guideline:    </td><td><input type="text" name="guideline" /></td></tr>
                <tr><td>Graphable:</td><td> <select name="graphable"   >
                    <option  value="yes" >YES</option>
                    <option  value="no">NO</option>
                </select></td></tr>
                <tr><td>Value Name:</td><td><input type="text" name="value_name" id="value_name" /></td></tr>
		</table>
                
                
                <div>
                
                    <h4>Create Rule</h4>
                    
                    <table class="rule">
                    <tr>
                    <td>Month Range:</td> <td>Strength: </td> <td>Text: </td>
                    </tr>
              
                    <tr>
                    <td><input type="text" name="monthrange1" class="month-range"/></td> 
                    <td><select name="strength1">
                        <option value="recommendation">Recommendation</option>
                        <option value="warning">Warning</option>
                    </select> </td> 
                    <td><input type="text" name="text1" class="rule-text"/> </td>
                    </tr>

                    <tr>
                    <td><input type="text" name="monthrange2" class="month-range"/></td> 
                    <td><select name="strength2">
                        <option value="recommendation">Recommendation</option>
                        <option value="warning">Warning</option>
                    </select> </td> 
                    <td><input type="text" name="text2" class="rule-text"/> </td>
                    </tr>

                    <tr>
                    <td><input type="text" name="monthrange3" class="month-range"/></td> 
                    <td><select name="strength3">
                        <option value="recommendation">Recommendation</option>
                        <option value="warning">Warning</option>
                    </select> </td> 
                    <td><input type="text" name="text3" class="rule-text"/> </td>
                    </tr>
                    </table>
                    
                </div>

		<div>
		<h4>Display Position</h4>
                Position: <%int count = measurements.size()-custList.size();%>
		
		<select id="count" name="count" required>
		<%for(int i=2;i<count;i++){ %>
			<option value="<%=i%>"><%=i%></option>
		<%} %>
			<option value="0" selected>Last</option>
		</select>
        </div>

       <legend></legend>
       
	   <input type="submit" class="btn btn-large btn-primary" value="Save" />
 
</form>  

  
</div><!-- add pane -->
	
	
	</div><!-- tab-content -->

		
	</div><!-- row -->
	
</div><!-- container -->
            





<div id="scrollToTop"><a href="#editFlowsheetBody"><i class="icon-arrow-up"></i>Top</a></div>

<!-- flowsheet xml output -->
        <textarea style="display:none;" cols="200" rows="200">
            <%=outp.outputString(va)%>
        </textarea><!-- flowsheet xml output END-->

<script src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script> 
<script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>	
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/DT_bootstrap.js"></script> 
<script src="<%=request.getContextPath() %>/js/jquery.validate.js"></script>

<script>
$(function (){ 
	$("[rel=popover]").popover({});  
}); 


$(document).ready(function () {

		if ( self !== top ) {
		var h = $(document).height();
		parent.parent.document.getElementById('trackerSlim').style.height = h+"px";
	
        $("#demoHeader").hide();
	}else{

	}
	
	$('html, body', window.parent.document).animate({scrollTop:0}, 'slow');
	
$(".measurement-select").change(function(){
	$("#display_name").val($(".measurement-select").val());
	$("#value_name").val($(".measurement-select").val());
	
});
	
<%if(request.getParameter("add")!=null){%>
//$('#addModal').modal('show');
$('#myTab a[href="#add"]').tab('show');
<%}%>
	
	$(document).scroll(function () {
	    var y = $(this).scrollTop();
	    if (y > 60) {
	        $('#scrollToTop').fadeIn();
	    } else {
	        $('#scrollToTop').fadeOut();
	    }
	});
	
	//$('<a href="#" class="btn" id="add-new" title="Add Measurement" style="margin-left:15px"><i class="icon-plus"></i> Add</a>').appendTo('div.dataTables_filter label');	

	$("#add-new").click(function(){
		$('#addModal').modal('show');		
	});

	$("#measurement-select").click(function(){
		$("#measurement-details").show();
	});	


	// validate signup form on keyup and submit
	$("#FlowSheetCustomActionForm").validate( );

	$.validator.addMethod("maxlength", function (value, element, len) {
	   return value == "" || value.length <= len;
	});	
	
});

$('#measurementTbl').dataTable({
	"aaSorting" : [ [ 1, "asc" ] ],
	"iDisplayLength": 25,
	"aoColumnDefs": [{ //remove sorting from batch column
	                  bSortable: false,
	                  aTargets: [ 0 ]
	               }]
});


</script>

</body>
</html>
