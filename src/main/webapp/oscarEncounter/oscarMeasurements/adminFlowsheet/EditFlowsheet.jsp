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


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite" %>


<%
    long startTimeToGetP = System.currentTimeMillis();
    //int demographic_no = Integer.parseInt(request.getParameter("demographic_no"));
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");


    String temp = "diab2";//"physFunction";  //
    if (request.getParameter("flowsheet") != null) {
        temp = request.getParameter("flowsheet");
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

%>
<!DOCTYPE html>
<html lang="en">

<head>
<title>Edit Flowsheet</title><!--I18n-->

<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet">
<link href="<%=request.getContextPath() %>/css/bootstrap-responsive.css" rel="stylesheet">

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

<style type="text/css">

.main-container{
position:absolute;
top:60px;
}

.help-about{
position:absolute;
top:50px;
right:10px;
}

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

#about-oscar:hover{cursor: hand; cursor: pointer;}



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


</style>


<style type="text/css" media="print">
.DoNotPrint {
	display: none;
}
</style>

</head>

<body id="editFlowsheetBody">

<%if(request.getParameter("demographic")==null){ %>
<div class="well well-small"></div>
<%}else{ %>

<%@ include file="/share/templates/patient.jspf"%>
<%} %>

<!-- help and about -->
<div class="help-about"> <i class="icon-question-sign"></i> <oscar:help keywords="flowsheet" key="app.top1"/>  <i class="icon-info-sign" style="margin-left:10px;"></i> <a id="about-oscar" ><bean:message key="global.about" /></a></div>

<div class="container-fluid main-container">
<div class="row-fluid">
<h3 style="display:inline;">Edit Flowsheet: <span style="font-weight:normal"><%=flowsheet.toUpperCase()%></span> </h3>

		  <span class="mode-toggle">
		            <% if (demographic!=null) { %>
		             Individual Patient | <a href="EditFlowsheet.jsp?flowsheet=<%=flowsheet%>">All Patients</a> 
		            <%}else{%>
		                All Patients
		            <%}%>
		  </span>
</div>

	<div class="row-fluid">
		<div class="span8">

		<!-- Flowsheet Measurement List -->
		<table class="table table-striped table-bordered table-condensed" id="measurementTbl">
		<thead>
		<tr>
		<th style="width:80px"></th>
		<th style="width:80px">Position</th>
		<th style="width:110px">Measurement</th>
		<th>Display Name</th>
		<th>Guideline</th>
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
		         		<a href="UpdateFlowsheet.jsp?flowsheet=<%=temp%>&measurement=<%=mstring%><%=demographicStr%>" title="Edit" class="action-icon"><i class="icon-pencil"></i></a>
		                <a href="FlowSheetCustomAction.do?method=delete&flowsheet=<%=temp%>&measurement=<%=mstring%><%=demographicStr%>" title="Delete" class="action-icon"><i class="icon-trash"></i></a>
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
		</div>
		
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
		       <tr><td><a href="FlowSheetCustomAction.do?method=archiveMod&id=<%=cust.getId()%>&flowsheet=<%=flowsheet%><%=demographicStr%>" class="action-icon"><i class="icon-trash"></i></a> </td> 
		       
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
		       <td><%=cust.getProviderNo()%> </td> <td> <a href="<%=request.getContextPath() %>/demographic/demographiccontrol.jsp?demographic=<%=cust.getDemographicNo()%>&displaymode=edit&dboperation=search_detail&appointment=" target="_blank"><%=cust.getDemographicNo()%></a></td></tr>
		    <%
		    	}
		    %>
		    </tbody>
		    </table><!-- Custom List END-->		
		    <%} %>
			</div>
			
		</div>
	</div>
</div>
            

<!-- ABOUT Modal -->
<div id="aboutModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="aboutModalLabel" aria-hidden="true" style="width:800px;margin-left:-400px">
<div class="modal-header">
<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
<h3 id="aboutModalLabel">About OSCAR</h3>
</div>
<div class="modal-body" id="aboutBody">
<!-- loading jsp with jquery -->          
</div>
<div class="modal-footer">

<input type="button" class="btn" data-dismiss="modal" aria-hidden="true" value="<bean:message key='global.close' />">
 
</div>
</div><!--ABOUT modal end -->  
  

<!--ADD NEW MEAS Modal -->
<form name="FlowSheetCustomActionForm" id="FlowSheetCustomActionForm" action="FlowSheetCustomAction.do" method="post">
<div id="addModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="addModalLabel" aria-hidden="true">
<div class="modal-header">
<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
<h3 id="addModalLabel">Add Measurement</h3>
</div>
<div class="modal-body">
<!-- Add measurement type -->
<div>
		       
            <input type="hidden" name="flowsheet" value="<%=temp%>"/>
            <input type="hidden" name="method" value="save"/>
            <%if (demographic !=null){%>
                    <input type="hidden" name="demographic" value="<%=demographic%>"/>
            <%}%>
          
		<div id="measurement-details">		
		<h4>Add Measurement</h4>
		<table>
		<tr>                
		<td>Select A Measurment: </td> <td><select name="measurement" class="measurement-select">
		
                	<option value="0">choose:</option>
                    <% for (EctMeasurementTypesBean measurementTypes : vec){ %>
                    <option value="<%=measurementTypes.getType()%>" ><%=measurementTypes.getTypeDisplayName()%> (<%=measurementTypes.getType()%>) </option>
                    <% } %>
                </select></td>
		</tr>

		<tr><td>Display Name:</td><td><input type="text" name="display_name" id="display_name" required/></td></tr>
                <tr><td>Guideline:    </td><td><input type="text" name="guideline"    /></td></tr>
                <tr><td>Graphable:</td><td> <select name="graphable"   >
                    <option  value="yes" >YES</option>
                    <option  value="no">NO</option>
                </select></td></tr>
                <tr><td>Value Name:</td><td><input type="text" name="value_name"       /></td></tr>
		</table>
                </div>
                
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

        
    </div> <!-- Add measurement type END-->       
</div>
<div class="modal-footer">

<input type="button" class="btn" data-dismiss="modal" aria-hidden="true" value="<bean:message key='global.close' />">
<input type="submit" class="btn btn-primary" value="Save" />
 
</div>
</div><!-- ADD NEW MEAS modal end -->
</form>     

<div id="scrollToTop"><a href="#editFlowsheetBody"><i class="icon-arrow-up"></i>Top</a></div>

<!-- flowsheet xml output -->
        <textarea style="display:none;" cols="200" rows="200">
            <%=outp.outputString(va)%>
        </textarea><!-- flowsheet xml output END-->


<script src="<%=request.getContextPath() %>/js/jquery-1.9.1.js"></script> 
<script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>	
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/DT_bootstrap.js"></script> 

<script src="<%=request.getContextPath() %>/js/jquery.validate.js"></script>

<script>
$(document).ready(function () {
	
	$(document).scroll(function () {
	    var y = $(this).scrollTop();
	    if (y > 60) {
	        $('#scrollToTop').fadeIn();
	    } else {
	        $('#scrollToTop').fadeOut();
	    }
	});
	
	//this needs work
	$("#about-oscar").click(function(){
		$('#aboutModal').modal('show');		
		$('#aboutBody').load('<%=request.getContextPath() %>/oscarEncounter/About.jsp');
	});

	$('<a href="#" class="btn" id="add-new" title="Add Measurement" style="margin-left:15px"><i class="icon-plus"></i> Add</a>').appendTo('div.dataTables_filter label');	

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

	"aoColumnDefs": [{ //remove sorting from batch column
	                  bSortable: false,
	                  aTargets: [ 0 ]
	               }]
});
</script>

</body>
</html>