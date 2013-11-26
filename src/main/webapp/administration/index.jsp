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

<%@ page import="org.oscarehr.phr.util.MyOscarUtils"%>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.common.model.UserProperty" %>
<%@ page import="org.oscarehr.common.dao.UserPropertyDAO" %>
<%@ page import="oscar.OscarProperties" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.util.*" %>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="myoscar" %>
<%@ taglib uri="http://www.caisi.ca/plugin-tag" prefix="plugin" %>

<%
if (session.getAttribute("userrole") == null) response.sendRedirect(request.getContextPath()+"/logout.jsp");

UserPropertyDAO userPropertyDao = SpringUtils.getBean(UserPropertyDAO.class);
    		
Properties oscarVariables = OscarProperties.getInstance();

String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
String curUser_no = (String) session.getAttribute("user");
String userfirstname = (String) session.getAttribute("userfirstname");
String userlastname = (String) session.getAttribute("userlastname");
String prov= (oscarVariables.getProperty("billregion","")).trim().toUpperCase();

String resourcebaseurl =  oscarVariables.getProperty("resource_base_url");

 	    UserProperty rbu = userPropertyDao.getProp("resource_baseurl");
 	    if(rbu != null) {
 	    	resourcebaseurl = rbu.getValue();
 	    }
    		
GregorianCalendar cal = new GregorianCalendar();
int curYear = cal.get(Calendar.YEAR);
int curMonth = (cal.get(Calendar.MONTH)+1);
int curDay = cal.get(Calendar.DAY_OF_MONTH);
%>

<!doctype html>
<html lang="en">

<head>
<title>Administration Panel</title>
<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/DT_bootstrap.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/bootstrap-responsive.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">

<style>
body{background-color: #fff /*#f3f3f3*/;}

sup{
color:#000;
font-weight:bold;
}  

#main-wrapper{margin-top:70px;}

div.navbar div.dropdown:hover ul.dropdown-menu{
    display: block;  
    margin:0px;  
}

.navbar .dropdown-menu {
 margin-top: 0px;
}

.navbar .nav > li > a {
padding:10px 10px;
}

#caret-loggedIn{
vertical-align: top; 
opacity: 0.3;
margin-top:18px;
}

.accordion-heading{background-color:#fff;border-bottom:thin solid #C6C6C6;}

.accordion-heading:hover{background-color:#e6e6e6;}
.accordion-heading a:hover{text-decoration:none;}

.icon-chevron-right{opacity:0.3;}

.accordion-heading a:hover > .icon-chevron-right{opacity: 1;}

.selected-heading{background-color:#e6e6e6;}

.accordion-inner{background-color:#fff;border-bottom:thin solid #C6C6C6;}

.accordion-inner a{color:#424242;}
.accordion-inner a:hover{text-decoration:none;color:#000; cursor: pointer; cursor: hand;}

.accordion-inner li{border-bottom: thin solid #c6c6c6;padding:2px 0px 2px 0px;}


#side{
position:absolute;
/* border-right:thin solid #C6C6C6; */
padding-right:10px;
padding-bottom:20px
}

#side a{
color:#333;
text-decoration: none;
}

#side a:hover{
color:#0088cc;
}


.icon-chevron-right{float:right;}  

#adminNav{
-webkit-box-shadow: 0 1px 3px rgba(0, 0, 0, 0.065);
-moz-box-shadow: 0 1px 3px rgba(0, 0, 0, 0.065);
box-shadow: 0 1px 3px rgba(0, 0, 0, 0.065);
}

#adminNav ul{
	padding: 0px;
	margin: 0px;
	list-style-type: none;
}

label.valid {
	width: 24px;
	height: 24px;
	background: url(<%=request.getContextPath() %>/images/icons/valid.png) center center no-repeat;
	display: inline-block;
	text-indent: -9999px;
}

label.error {
	font-weight: bold;
	color: red;
	padding: 2px 8px;
	margin-top: 2px;
	font-size: 13px;
	display: inline;
}

.table tbody tr:hover td, .table tbody tr:hover th {
    background-color: #FFFFAA;
}

.quick-links{
display:inline-block;
width:160px;
height:100px;
margin:10px;
text-align:center;
vertical-align:top;
}

.quick-links a{
text-decoration:none;
color:#333;
}

.quick-links a:hover{
color:#0088cc;
}

.used-heading{
padding-bottom:0px;
margin-bottom:0px;
}

@media (max-width: 767px) {
     #side {
         width:98%;
         position:static;
     }
     
     #main-wrapper{margin-top:30px;}
 }
  
@media (min-width: 768px) and (max-width: 1430px) { 
	
	 #side{
	 	width: 240px;
	 	margin-right:15px;
	  } 
	  
	  #dynamic-content{margin-left:260px;}
}

.visible-print {
  display: none !important;
}  

@media print {
  .visible-print {
    display: inherit !important;
  }
  .hidden-print {
    display: none !important;
  }
}
</style>

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.js"></script>

<oscar:customInterface section="main"/> <!--needs to be in header-->
</head>

<body>


<!--<oscar:help keywords="admin" key="app.top1"/> --> 

<div class="container-fluid">
<div class="row-fluid" style="text-align:right"><i class=" icon-question-sign"></i> <a href="#" ONCLICK ="popupPage2('<%=resourcebaseurl%>');return false;" title="" onmouseover="window.status='';return true">Help</a> <i class=" icon-info-sign" style="margin-left:10px;"></i> <a href="javascript:void(0)"  onClick="window.open('<%=request.getContextPath()%>/oscarEncounter/About.jsp','About OSCAR','scrollbars=1,resizable=1,width=800,height=600,left=0,top=0')" ><bean:message key="global.about" /></a></div>
<div class="row-fluid">

<%@ include file="leftNav.jspf"%>

  <div class="span9 offset2" id="dynamic-content">
    
    <!-- ****DYNAMIC CONTENT**** -->

<div class="row-fluid">	
      <div class="well quick-links">
	<a href='javascript:void(0);' class="xlink" rel="${ctx}/admin/unLock.jsp"><i class="icon-user icon-4x"></i>
      
      <h5><bean:message key="admin.admin.unlockAcct"/></h5></a>
      </div>
      
      <div class="well quick-links">
	<a href='javascript:void(0);' class="xlink" rel="${ctx}/admin/provideraddarecordhtm.jsp"><i class="icon-user icon-4x"></i>	
      
        <h5><bean:message key="admin.admin.btnAddProvider" /></h5></a>
      </div>
      
      <div class="well quick-links">
        <a href='javascript:void(0);' class="xlink" rel="${ctx}/lab/CA/ALL/testUploader.jsp" ><i class="icon-inbox icon-4x"></i>
      
        <h5><bean:message key="admin.admin.hl7LabUpload" /></h5></a>
      </div>
      
      <div class="well quick-links">
        <a href="javascript:void(0);" class="xlink" rel="${ctx}/schedule/scheduletemplatesetting.jsp" title="<bean:message key="admin.admin.scheduleSettingTitle"/>"><i class="icon-calendar icon-4x"></i>
      
        <h5><bean:message key="admin.admin.scheduleSetting" /></h5></a>
      </div>


       <div class="well quick-links">
         <a href='javascript:void(0);' class="xlink" rel="${ctx}/admin/securityaddarecord.jsp"><i class="icon-user icon-4x"></i>
      
         <h5><bean:message key="admin.admin.btnAddLogin" /></h5></a>
      </div>
      
      <div class="well quick-links">
        <a href='javascript:void(0);' class="xlink" rel="${ctx}/admin/providertemplate.jsp"><i class="icon-medkit icon-4x"></i>
      
        <h5><bean:message key="admin.admin.btnInsertTemplate" /></h5></a>
      </div>
      
      <div class="well quick-links">
        <a href="javascript:void(0);" class="xlink" rel="${ctx}/admin/admindisplaymygroup.jsp"><i class="icon-calendar icon-4x"></i>
      
        <h5><bean:message key="admin.admin.btnSearchGroupNoRecords" /></h5></a>     
      </div>
      
      <div class="well quick-links">
        <a href='javascript:void(0);' class="xlink" rel="${ctx}/admin/providerPrivilege.jsp"><i class="icon-wrench icon-4x"></i>
      
        <h5><bean:message key="admin.admin.assignRightsObject"/></h5></a>
      </div>     
 </div>     
              
             
          
       
    <!-- ****DYNAMIC CONTENT END**** -->
    
    </div><!-- span8 end -->

</div>
</div>

<!-- jquery-1.9.1.js - in nonPatientContextHeader.jspf --> 
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/DT_bootstrap.js"></script>   


<script type="text/javascript">
$( document ).ready(function( $ ) {
	$("a.contentLink").click(function(e) {
		//alert('link click');
		e.preventDefault();
		//alert("You clicked the link");
		$("#dynamic-content").load($(this).attr("href"), 
			function(response, status, xhr) {
		  		if (status == "error") {
			    	var msg = "Sorry but there was an error: ";
			    	$("#dynamic-content").html(msg + xhr.status + " " + xhr.statusText);
				}
			}
		);
	});
		
});

function registerFormSubmit(formId, divId) {
	$('#'+formId).submit(function() {
		if(!$('#'+formId).valid()){
			return false;
		}
		// gather the form data
		var data = $(this).serialize();
		// post data
		$.post($('#'+formId).attr('action'), data, function(returnData) {
			// insert returned html 
			$('#'+divId).html(returnData)
		})

		return false; // stops browser from doing default submit process
	});
}

function submitForm(formId, divId){
	// gather the form data
	var data = $(this).serialize();
	// post data
	$.post($('#'+formId).attr('action'), data, function(returnData) {
		// insert returned html 
		$('#'+divId).html(returnData)
	})
}

function parseDate(date, format, separator) {
	if(!date){
		date = '';
	}
	var parts = date.split(separator), formatParts = format.split(separator),
		date1 = new Date(),
		val;
	date1.setHours(0);
	date1.setMinutes(0);
	date1.setSeconds(0);
	date1.setMilliseconds(0);
	if (parts.length === formatParts.length) {
		var year = date1.getFullYear(), day = date1.getDate(), month = date1.getMonth();
		for (var i=0, cnt = formatParts.length; i < cnt; i++) {
			val = parseInt(parts[i], 10)||1;
			switch(formatParts[i]) {
				case 'dd':
				case 'd':
					day = val;
					date1.setDate(val);
					break;
				case 'mm':
				case 'm':
					month = val - 1;
					date1.setMonth(val - 1);
					break;
				case 'yy':
					year = 2000 + val;
					date1.setFullYear(2000 + val);
					break;
				case 'yyyy':
					year = val;
					date1.setFullYear(val);
					break;
				default:
					if(!val)
						return null;
			}
		}
		date1 = new Date(year, month, day, 0 ,0 ,0);
		return date1;
	}
	return null;
}

function validDate(value, format, separator){
	try{
		var d = parseDate(value, format, separator);
		
		return d!=null;
	} catch(e){
		return false;
	}			
}

$(document).ready(function() {
	
	// set validation defaults
	jQuery.validator.setDefaults({ 
		debug: true,
		highlight: function(element) {
		    $(element).closest('.control-group').removeClass('success').addClass('error');
		},
		success: function(element) {
		    element.closest('.control-group').removeClass('error').addClass('success');
		} 
	});
	
	
	jQuery.validator.addMethod("oscarDate", function(value, element) { 
	    return validDate(value, "yyyy-mm-dd", "-");
	},
    "Date format should be yyyy-mm-dd.");
	
	jQuery.validator.addMethod("oscarMonth", function(value, element) { 
	    return validDate(value, "mm/yyyy", "/");
	},
    "Date format should be mm/yyyy.");
	
	
	// initialiaze toolstips
	$('[rel=tooltip]').tooltip();
});

</script>

</body>
</html>
