<!DOCTYPE html>
<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting,_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_report&type=_admin.reporting&type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="oscar.OscarProperties" %>
<%@page import="java.text.NumberFormat" %>
<%@page import="java.text.DecimalFormat" %>

<%! boolean bMultisites = org.oscarehr.common.IsPropertiesOn.isMultisitesEnable(); %>
<%@ page import="java.math.*,java.util.*, java.sql.*, oscar.*, java.net.*,oscar.util.*,oscar.oscarBilling.ca.on.pageUtil.*,oscar.oscarBilling.ca.on.data.*,org.apache.struts.util.LabelValueBean" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>


<%
     boolean isTeamBillingOnly=false;
    boolean isSiteAccessPrivacy=false;
    boolean isTeamAccessPrivacy=false; 
    OscarProperties props = OscarProperties.getInstance();
    
    boolean hideName = Boolean.valueOf(props.getProperty("invoice_reports.print.hide_name","false"));
    
%>
<security:oscarSec objectName="_team_billing_only" roleName="<%=roleName$ %>" rights="r" reverse="false">
<% isTeamBillingOnly=true; %>
</security:oscarSec>
<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%isSiteAccessPrivacy=true; %>
</security:oscarSec>
<security:oscarSec objectName="_team_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%isTeamAccessPrivacy=true; %>
</security:oscarSec>

<%
	//multi-site office , save all bgcolor to Hashmap
	HashMap<String,String> siteBgColor = new HashMap<String,String>();
	HashMap<String,String> siteShortName = new HashMap<String,String>();
	int patientCount = 0;
	if (bMultisites) {
    	SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");
    	
    	List<Site> sites = siteDao.getAllSites();
    	for (Site st : sites) {
    		siteBgColor.put(st.getName(),st.getBgColor());
    		siteShortName.put(st.getName(),st.getShortName());
    	}
	}
%>

<%//
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setDateHeader("Expires", 0); //prevents caching at the proxy server
response.setHeader("Cache-Control", "private"); // HTTP 1.1 
response.setHeader("Cache-Control", "no-store"); // HTTP 1.1 
response.setHeader("Cache-Control", "max-stale=0"); // HTTP 1.1 


boolean bSearch = true;
String[] billType = request.getParameterValues("billType");
String[] strBillType = new String[] {""};
if (billType == null || billType.length == 0) { // no boxes checked
	bSearch = false;
	strBillType = new String[] {"HCP","WCB","RMB","NOT","PAT","OCF","ODS","CPP","STD","IFH"};
} else { 
	// at least on box checked
	strBillType = billType;
}

String statusType = request.getParameter("statusType");
String providerNo = request.getParameter("providerview");
String startDate  = request.getParameter("xml_vdate"); 
String endDate    = request.getParameter("xml_appointment_date");
String demoNo     = request.getParameter("demographicNo");
String serviceCode     = request.getParameter("serviceCode");
String raCode     = request.getParameter("raCode");
String dx = request.getParameter("dx");
String visitType = request.getParameter("visitType");
String filename = request.getParameter("demographicNo");

String selectedSite = request.getParameter("site");
String billingForm = request.getParameter("billing_form");

String visitLocation = request.getParameter("xml_location");

String sortName = request.getParameter("sortName");
String sortOrder = request.getParameter("sortOrder");

String paymentStartDate  = request.getParameter("paymentStartDate"); 
String paymentEndDate    = request.getParameter("paymentEndDate");

if ( statusType == null ) { statusType = "O"; } 
if ( "_".equals(statusType) ) { demoNo = "";}
if ( startDate == null ) { startDate = ""; } 
if ( endDate == null ) { endDate = ""; } 
if ( demoNo == null ) { demoNo = ""; filename = "";} 
if ( providerNo == null ) { providerNo = "" ; } 
if ( raCode == null ) { raCode = "" ; } 
if ( dx == null ) { dx = "" ; } 
if ( visitType == null ) { visitType = "-" ; } 
if ( serviceCode == null || serviceCode.equals("")) serviceCode = "%";
if ( billingForm == null ) { billingForm = "-" ; }
if ( visitLocation == null) { visitLocation = "";}
if ( sortName == null) { sortName = "ServiceDate";}
if ( sortOrder == null) { sortOrder = "asc";}
if ( paymentStartDate == null ) { paymentStartDate = ""; } 
if ( paymentEndDate == null ) { paymentEndDate = ""; } 

List<String> pList = isTeamBillingOnly
		? (new JdbcBillingPageUtil()).getCurTeamProviderStr((String) session.getAttribute("user"))
		: (new JdbcBillingPageUtil()).getCurProviderStr();

BillingStatusPrep sObj = new BillingStatusPrep();
List<BillingClaimHeader1Data> bList = null;
if((serviceCode == null || billingForm == null) && dx.length()<2 && visitType.length()<2) {
	bList = bSearch ? sObj.getBills(strBillType, statusType, providerNo, startDate, endDate, demoNo, visitLocation,paymentStartDate, paymentEndDate) : new ArrayList<BillingClaimHeader1Data>();
	//serviceCode = "-";
	serviceCode = "%";
} else {
	serviceCode = (serviceCode == null || serviceCode.length()<2)? "%" : serviceCode; 
	bList = bSearch ? sObj.getBillsWithSorting(strBillType, statusType,  providerNo, startDate,  endDate,  demoNo, serviceCode, dx, visitType, billingForm, visitLocation,sortName,sortOrder,paymentStartDate, paymentEndDate) : new ArrayList<BillingClaimHeader1Data>();
}

RAData raData = new RAData();

BigDecimal total = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP); 
BigDecimal paidTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal adjTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);


NumberFormat formatter = new DecimalFormat("#0.00");


%>

<%
	String ohipNo= "";
	if(request.getParameter("provider_ohipNo")!=null)
		ohipNo = request.getParameter("provider_ohipNo");
%>

<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.oscarehr.common.model.Site"%>
<%@page import="org.oscarehr.common.model.Provider"%><html>
    <head>
        <title><bean:message key="admin.admin.invoiceRpts"/></title>
        
		<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
		<script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/js/excellentexport.min.js"></script>
		<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
		<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet" type="text/css">
       
        <script type="text/javascript">

            function nav_colour_swap(navid, num) {               
                for(var i = 0; i < num; i++) {
                    var nav = document.getElementById("A" + i);
                    if(navid == nav.id) { //selected td
                        nav.style.color = "red";
                    }
                    else { //other td
                        nav.style.color = "#645FCD";
                    }
                }
            }  
            
            function submitForm(methodName) {
                if (methodName=="email"){
                    document.invoiceForm.method.value="sendListEmail";
                } else if (methodName=="print") {            
                    document.invoiceForm.method.value="getListPrintPDF";
                }
                document.invoiceForm.submit();
            }

        function fillEndDate(d){
           document.serviceform.xml_appointment_date.value= d;  
        }
        function setDemographic(demoNo){
           //alert(demoNo);
           document.serviceform.demographicNo.value = demoNo;
        }
		function popupPage(vheight,vwidth,varpage) {
		  var page = "" + varpage;
		  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
		  var popup=window.open(page, "billcorrection", windowprops);
		    if (popup != null) {
		    if (popup.opener == null) {
		      popup.opener = self;
		    }
		    popup.focus();
		  }
		}
        function check(stat){
			for (var x = 0; x < 10; x++) {
				document.serviceform.billType[x].checked= stat;  
			}
        }
        function changeStatus(){
        	//alert(document.serviceform.billTypeAll.checked);
           if(document.serviceform.billTypeAll.checked) {
			check(true);
           } else {
			check(false);
           }  
        }	
        
        function changeProvider(shouldSubmit) {
        
        	var index = document.serviceform.providerview.selectedIndex;
        	var provider_no = document.serviceform.providerview[index].value;
        	
        	<% for (int i = 0 ; i < pList.size(); i++) { 
				String temp[] = ( pList.get(i)).split("\\|");				
			%>
			
			var temp_provider_no = <%=temp[0]%> ;			
			if(provider_no==temp_provider_no) {				
				var provider_ohipNo="<%=temp[3]%>";
				document.serviceform.provider_ohipNo.value=provider_ohipNo;	
				if (shouldSubmit) {
					if(document.getElementById("xml_vdate").value.length>0 && document.getElementById("xml_appointment_date").value.length>0)
					document.serviceform.submit();
				}
				else return;				
        	} 
        	<%} %>
        	document.serviceform.provider_ohipNo.value="";
        	if (shouldSubmit) document.serviceform.submit();
        }	
        </script>
<script type="text/javascript">
var xmlHttp;
function createXMLHttpRequest() {
	if (window.ActiveXObject) {
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	else if (window.XMLHttpRequest) {
		xmlHttp = new XMLHttpRequest();
	}
}
var ajaxFieldId;
function startRequest(idNum) {
	ajaxFieldId = idNum;
	createXMLHttpRequest();
	xmlHttp.onreadystatechange = handleStateChange;
	var val = 'N';
	if(document.getElementById('status'+idNum).checked) {
	//alert(('status'+idNum) + document.getElementById('status'+idNum).checked);
		val = 'Y';
	}
	xmlHttp.open("GET", "billingONStatusERUpdateStatus.jsp?id="+idNum+"&val="+val , true);
	xmlHttp.send(null);
}

function handleStateChange() {
	if(xmlHttp.readyState == 4) {
//alert(xmlHttp.status + "0 :go 2" + xmlHttp.responseText);
			//document.getElementById(ajaxFieldId).innerHTML = xmlHttp.responseText;
		if(xmlHttp.status == 200) {
//alert("go 3" + xmlHttp.responseText);
			document.getElementById(ajaxFieldId).innerHTML = xmlHttp.responseText;
		}
	}
}

var isChecked = false;
function checkAll(group) {
    for (i = 0; i < group.length; i++) 
        group[i].checked = !isChecked;
    isChecked = !isChecked;
}


updateSort = function(name) {
	var sortName =$("#sortName").val();
	var sortOrder = $("#sortOrder").val();
	
	if(sortName != name) {
		sortName = name;
		sortOrder = 'asc';
	} else {
		if(sortOrder == 'asc') {
			sortOrder = 'desc';
		} else if(sortOrder == 'desc') {
			sortOrder = 'asc';
		} else {
			//this shouldn't happen..but just in case
			sortOrder='asc';
		}
	}
	
	$("#sortName").val(sortName);
	$("#sortOrder").val(sortOrder);
	
	document.serviceform.submit();
}

</script>

<style>

table td,th{font-size:12px;}

@media print {
  .hidden-print {
    display: none !important;
  }
}
</style>
  
    </head>
    <body>
    <h3><bean:message key="admin.admin.invoiceRpts"/></h3>
  	<div class="container-fluid">
 <div class="row">
<%=DateUtils.sumDate("yyyy-MM-dd","0")%>

<div class="pull-right hidden-print">
<!--Hiding for now since this does not seem to manage the providers in the select 
<a href="javascript: function myFunction() {return false; }" onClick="popupPage(700,720,'../../../oscarReport/manageProvider.jsp?action=billingreport')">Manage Provider List</a>-->
<button class="btn" type='button' name='print' value='Print' onClick='window.print()'><i class="icon icon-print"></i> Print</i></button>
</div>
</div>

<form name="serviceform" method="get" action="billingONStatus.jsp">

<input type="hidden" id="sortName" name="sortName" value="<%=sortName%>"/>
<input type="hidden" id="sortOrder" name="sortOrder" value="<%=sortOrder%>"/>

<div class="row well well-small hidden-print">    
  
    	<%
    		String tmpStrBillType = Arrays.toString(strBillType);
    	%>


<div class="row">
<div class="span10">
<small>    
<input type="checkbox" name="billTypeAll" value="ALL" checked onclick="changeStatus();"><span style="padding-right:4px">ALL</span></input> 
        <input type="checkbox" name="billType" value="HCP" <%=tmpStrBillType.indexOf("HCP")>=0?"checked":""%>><span style="padding-right:4px">Bill OHIP </span></input> 
        <input type="checkbox" name="billType" value="RMB" <%=tmpStrBillType.indexOf("RMB")>=0?"checked":""%>><span style="padding-right:4px">RMB </span></input> 
        <input type="checkbox" name="billType" value="WCB" <%=tmpStrBillType.indexOf("WCB")>=0?"checked":""%>><span style="padding-right:4px">WCB</span></input> 
        <input type="checkbox" name="billType" value="NOT" <%=tmpStrBillType.indexOf("NOT")>=0?"checked":""%>><span style="padding-right:4px">Not Bill</span></input> 
        <input type="checkbox" name="billType" value="PAT" <%=tmpStrBillType.indexOf("PAT")>=0?"checked":""%>><span style="padding-right:4px">Bill Patient</span></input> 
        <input type="checkbox" name="billType" value="OCF" <%=tmpStrBillType.indexOf("OCF")>=0?"checked":""%>><span style="padding-right:4px">OCF</span></input> 
        <input type="checkbox" name="billType" value="ODS" <%=tmpStrBillType.indexOf("ODS")>=0?"checked":""%>><span style="padding-right:4px">ODSP</span></input> 
        <input type="checkbox" name="billType" value="CPP" <%=tmpStrBillType.indexOf("CPP")>=0?"checked":""%>><span style="padding-right:4px">CPP</span></input> 
        <input type="checkbox" name="billType" value="STD" <%=tmpStrBillType.indexOf("STD")>=0?"checked":""%>><span style="padding-right:4px">STD/LTD</span></input> 
        <input type="checkbox" name="billType" value="IFH" <%=tmpStrBillType.indexOf("IFH")>=0?"checked":""%>><span style="padding-right:4px">IFH</span></input> 
</small>
</div>
</div><!-- row -->


<div class="row">
<div class="span3">  
<br>
<% // multisite start ==========================================
String curSite = request.getParameter("site");
if (bMultisites) 
{ 
        	SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");
          	List<Site> sites = siteDao.getActiveSitesByProviderNo((String) session.getAttribute("user"));
          	// now get all providers eligible         	
          	HashSet<String> pros=new HashSet<String>();
          	for (Object s:pList) {
          		pros.add(((String)s).substring(0, ((String)s).indexOf("|")));
          	}
      %> 
      <script>
var _providers = [];
<%	for (int i=0; i<sites.size(); i++) {  
	Set<Provider> siteProviders = sites.get(i).getProviders();
	List<Provider>  siteProvidersList = new ArrayList<Provider> (siteProviders);
	Collections.sort(siteProvidersList,(new Provider()).ComparatorName());%>
	_providers["<%= sites.get(i).getName() %>"]="<% Iterator<Provider> iter = siteProvidersList.iterator();
	while (iter.hasNext()) {
		Provider p=iter.next();
		if (pros.contains(p.getProviderNo())) {
	%><option value='<%= p.getProviderNo() %>'><%= p.getLastName() %>, <%= p.getFirstName() %></option><% }} %>";
<% } %>
function changeSite(sel) {
	sel.form.providerview.innerHTML=sel.value=="none"?"":"<option value='none'>---select provider---</option>"+_providers[sel.value];
	sel.style.backgroundColor=sel.options[sel.selectedIndex].style.backgroundColor;
	if (sel.value=='<%=request.getParameter("site")%>') {
		if (document.serviceform.provider_ohipNo.value!='')
			sel.form.providerview.value='<%=request.getParameter("providerview")%>';
	}
	changeProvider(false);
}
      </script>


      	<select id="site" name="site" onchange="changeSite(this)">
      		<option value="none" style="background-color:white">---select clinic---</option>
      	<%
      	for (int i=0; i<sites.size(); i++) {
      	%>
      		<option value="<%= sites.get(i).getName() %>" style="background-color:<%= sites.get(i).getBgColor() %>"
      			 <%=sites.get(i).getName().toString().equals(curSite)?"selected":"" %>><%= sites.get(i).getName() %></option>
      	<% } %>
      	</select>
</div>


      	<select id="providerview" name="providerview" onchange="changeProvider(true);" style="width:140px"></select>
<% if (request.getParameter("providerview")!=null) { %>
      	<script>
     	window.onload=function(){changeSite(document.getElementById("site"));}
      	</script>
<% } // multisite end ==========================================
} else {
%>	 
    <select name="providerview" onchange="changeProvider(false);">
			<%
			if(pList.size() == 1) {
				String temp[] = ( pList.get(0)).split("\\|");
			%>
			<option value="<%=temp[0]%>"> <%=temp[1]%>, <%=temp[2]%></option>
			<%
			} else {
			%>
       <option value="all">All Providers</option>
    <% for (int i = 0 ; i < pList.size(); i++) { 
		String temp[] = ( pList.get(i)).split("\\|");
	%>
       <option value="<%=temp[0]%>" <%=providerNo.equals(temp[0])?"selected":""%>><%=temp[1]%>, <%=temp[2]%></option>
         
    <% } 
    } %>
    </select>
<% } %>
</div>

<div class="span2">      
OHIP No.: <br>
<input type="text" class="span2" name="provider_ohipNo" readonly value="<%=ohipNo%>">
</div>
 

	<div class="span2">		
	Start:
		<div class="input-append">
			<input type="text" name="xml_vdate" id="xml_vdate" style="width:90px" value="<%=startDate%>" pattern="^\d{4}-((0\d)|(1[012]))-(([012]\d)|3[01])$" autocomplete="off" />
			<span class="add-on"><i class="icon-calendar"></i></span>
		</div>
	</div>


	<div class="span2">		
	End: 
		<small>
		<a href="javascript: function myFunction() {return false; }" onClick="fillEndDate('<%=DateUtils.sumDate("yyyy-MM-dd","-30")%>')" >30</a>
		<a href="javascript: function myFunction() {return false; }" onClick="fillEndDate('<%=DateUtils.sumDate("yyyy-MM-dd","-60")%>')" >60</a>
		<a href="javascript: function myFunction() {return false; }" onClick="fillEndDate('<%=DateUtils.sumDate("yyyy-MM-dd","-90")%>')" >90</a>     
		days back                    
		</small>
	
		<div class="input-append">
			<input type="text" name="xml_appointment_date" style="width:90px" id="xml_appointment_date" value="<%=endDate%>" pattern="^\d{4}-((0\d)|(1[012]))-(([012]\d)|3[01])$" autocomplete="off" />
			<span class="add-on"><i class="icon-calendar"></i></span>
		</div>
	</div>
</div><!-- row -->    
    
<div class="row">		
<div class="span2">	        
Dx:<br>
<input type="text" name="dx" class="span2" value="<%=dx%>"/>
</div>

<div class="span2">	
Serv. Code:<br>
<input type="text" name="serviceCode" class="span2" value="<%=serviceCode%>"/>
</div>

<div class="span2">	
Demographic:<br>
<input type="text" name="demographicNo" class="span2" value="<%=demoNo%>"/>
</div>

<div class="span2">	
RA Code:<br>
<input type="text" name="raCode" class="span2" value="<%=raCode%>"/>
</div>
</div> <!-- row -->

<div class="row">
<div class="span2">	
Visit Type:<br>
	<select class="span2" name="visitType" style="background-color:none;">
		<option value="-" <%=visitType.startsWith("-")?"selected":""%>> </option>
		<option value="00" <%=visitType.startsWith("00")?"selected":""%>>Clinic Visit</option>
		<option value="01" <%=visitType.startsWith("01")?"selected":""%>>Outpatient Visit</option>
		<option value="02" <%=visitType.startsWith("02")?"selected":""%>>Hospital Visit</option>
		<option value="03" <%=visitType.startsWith("03")?"selected":""%>>ER</option>
		<option value="04" <%=visitType.startsWith("04")?"selected":""%>>Nursing Home</option>
		<option value="05" <%=visitType.startsWith("05")?"selected":""%>>Home Visit</option>
	</select>

</div>

<div class="span5">		
Billing Form:<br>
<select name="billing_form" class="span5">
 <option value="---" selected="selected"> --- </option>
 <%
        List<LabelValueBean> forms = sObj.listBillingForms();
        String selected = "";
        for (LabelValueBean form : forms) {
            if (billingForm != null) {
                if (billingForm.equals(form.getValue())) {
                    selected = "selected";
                } else {
                    selected = "";
                }
            }
%>
<option value="<%= form.getValue()%>" <%= selected%> ><%= form.getLabel()%></option>
<%
        }
%>

</select>
</div>
</div><!-- row -->

<div class="row">
<div class="span5">		
Visit Location:<br>
<select name="xml_location" class="span5">
 												<% //
		JdbcBillingPageUtil tdbObj = new JdbcBillingPageUtil();
 											    
	    String billLocationNo="", billLocation="";
	    List lLocation = tdbObj.getFacilty_num();
	    for (int i = 0; i < lLocation.size(); i = i + 2) {
		billLocationNo = (String) lLocation.get(i);
		billLocation = (String) lLocation.get(i + 1);
		String locationSelected = visitLocation.equals(billLocationNo)? " selected=\"selected\" ":"";
%>
	<option value="<%=billLocationNo%>" <%=locationSelected %>>
	<%=billLocation%>
	</option>
	<%	    } %>
												

</select>
</div>
<div class="span7">
	<div class="span2">		
	Payment Start:
		<div class="input-append">
			<input type="text" name="paymentStartDate" id="paymentStartDate" style="width:90px" value="<%=paymentStartDate%>" pattern="^\d{4}-((0\d)|(1[012]))-(([012]\d)|3[01])$" autocomplete="off" />
			<span class="add-on"><i class="icon-calendar"></i></span>
		</div>
	</div>
	<div class="span2">		
	Payment End:
		<div class="input-append">
			<input type="text" name="paymentEndDate" id="paymentEndDate" style="width:90px" value="<%=paymentEndDate%>" pattern="^\d{4}-((0\d)|(1[012]))-(([012]\d)|3[01])$" autocomplete="off" />
			<span class="add-on"><i class="icon-calendar"></i></span>
		</div>
	</div>

</div>
</div>

<div class="row" >
<div class="span10">
<small>	
       <input type="radio" name="statusType" value="%" <%=statusType.equals("%")?"checked":""%>>All</input>
	   <input type="radio" name="statusType" value="_" <%=statusType.equals("_")?"checked":""%>>Rejected</input>
       <input type="radio" name="statusType" value="H" <%=statusType.equals("H")?"checked":""%>>Capitated</input>
       <input type="radio" name="statusType" value="O" <%=statusType.equals("O")?"checked":""%>>Invoiced</input>
       <input type="radio" name="statusType" value="P" <%=statusType.equals("P")?"checked":""%>>Bill Patient</input>
        <!--li><input type="radio" name="statusType" value="N" <%=statusType.equals("N")?"checked":""%>>Do Not Bill</input>
       <input type="radio" name="statusType" value="W" <%=statusType.equals("W")?"checked":""%>>WCB</input>-->
       <input type="radio" name="statusType" value="B" <%=statusType.equals("B")?"checked":""%>>Submmitted OHIP</input>
       <input type="radio" name="statusType" value="S" <%=statusType.equals("S")?"checked":""%>>Settled/Paid</input>
       <input type="radio" name="statusType" value="X" <%=statusType.equals("X")?"checked":""%>>Bad Debt</input>
       <input type="radio" name="statusType" value="D" <%=statusType.equals("D")?"checked":""%>>Deleted Bill</input>
</small>
</div>
</div>

<div class="row">
<div class="span2" style="padding-top:10px;">
<input class="btn btn-primary" type="submit" name="Submit" value="Create Report">
</div>   
</div><!-- row -->

    </form>
</div>

<div class="row">
    <form name="invoiceForm" action="<%=request.getContextPath()%>/BillingInvoice.do">     
         <input type="hidden" name="method" value=""/>
<% //
if(statusType.equals("_")) { %>
    <!--  div class="rejected list"-->
       <table class="table">
          <tr class="warning"> 
             <th>Health#</th>
             <th>D.O.B</th>
             <th>Invoice #</th>
             <!--th>Type</th-->
             <th>Ref #</th>
             <th>Hosp #</th>
             <th title="admission date">Admitted</th>
             <th>Claim Error</th>
             <th>Code</th>
             <th>Fee</th>
             <th>Unit</th>
             <th>Date</th>
             <th>Dx</th>
             <th>Exp.</th>
             <th>Code Error</th>
             <th>Status</th>
             <th>Filename</th>
          </tr>
	<% //
        ArrayList<String> aLProviders;
        if( providerNo == null || providerNo.equals(""))  {
            aLProviders = new ArrayList<String>(pList);
        }
        else {
            aLProviders = new ArrayList<String>();
            aLProviders.add(providerNo);
        }
        String[] provInfo;
        for( int idx = 0; idx < aLProviders.size(); ++idx ) {
            provInfo = aLProviders.get(idx).split("\\|");
            providerNo = provInfo[0].trim();

	List lPat = null;
        if(providerNo.equals("all")) {
            List<BillingProviderData> providerObj = (new JdbcBillingPageUtil()).getProviderObjList(providerNo);
            lPat = (new JdbcBillingErrorRepImpl()).getErrorRecords(providerObj, startDate, endDate, filename);
        } else {
            BillingProviderData providerObj = (new JdbcBillingPageUtil()).getProviderObj(providerNo);
            lPat = (new JdbcBillingErrorRepImpl()).getErrorRecords(providerObj, startDate, endDate, filename);
            }
    boolean nC = false;
	String invoiceNo = "";
	

	for(int i=0; i<lPat.size(); i++) {
		BillingErrorRepData bObj = (BillingErrorRepData) lPat.get(i);
		String color = "";
		if(!invoiceNo.equals(bObj.getBilling_no())) {
			invoiceNo = bObj.getBilling_no(); 
			nC = nC ? false : true;
		} 
	    color = nC ? "class='success'" : "";
	%>
    		<tr <%=color %>>
    			<td><small><%=bObj.getHin() %> <%=bObj.getVer() %></small></td>
    			<td><font size="-1"><%=bObj.getDob() %></font></td>
    			<td align="right"><a href=# onclick="popupPage(800,700,'billingONCorrection.jsp?billing_no=<%=bObj.getBilling_no()%>','BillCorrection<%=bObj.getBilling_no()%>');return false;"><%=bObj.getBilling_no() %></a></td>
    			<td><%=bObj.getRef_no() %></td>
    			<td><%=bObj.getFacility() %></td>
    			<td><%=bObj.getAdmitted_date() %></td>
    			<td><%=bObj.getClaim_error() %></td>
    			<td><%=bObj.getCode() %></td>
    			<%
    				String formattedFee = null;
    				try {
    				    formattedFee = String.valueOf(Integer.parseInt(bObj.getFee()));
    				    
    				}
    				catch( NumberFormatException e ) {
    				    formattedFee = "N/A";
    				}
    			%>
    			<td align="right"><%=ch2StdCurrFromNoDot(formattedFee)%></td>
    			<td align="right"><%=bObj.getUnit() %></td>
    			<td><font size="-1"><%=bObj.getCode_date() %></font></td>
    			<td><%=bObj.getDx() %></td>
    			<td><%=bObj.getExp() %></td>
    			<td><%=bObj.getCode_error() %></td>
    			<td align="center">
    			<input type="checkbox" id="status<%=bObj.getId() %>" name="status<%=bObj.getId() %>" 
    			value="Y" <%="N".equals(bObj.getStatus())? "":"checked" %> onclick="startRequest('<%=bObj.getId() %>');" />
    			</td>
    			<td id="<%=bObj.getId() %>"><%=bObj.getReport_name() %></td>
    		</tr>
<% }}} else { %>
    <!--  div class="tableListing"-->
       <table class="table" id="bListTable">
          <thead>
		<tr> 
             <th><a href="javascript:void();" onClick="updateSort('ServiceDate');return false;">SERVICE DATE</a></th>
             <th> <a href="javascript:void();" onClick="updateSort('DemographicNo');return false;">PATIENT</a></th>
             <th class="<%=hideName?"hidden-print":""%>">PATIENT NAME</th>
             <th>GENDER</th>
             <th> <a href="javascript:void();" onClick="updateSort('VisitLocation');return false;">LOCATION</a></th>
             <th title="Status">STAT</th>
             <th>SETTLED</th>
             <th title="Code Billed">CODE</th>
             <th title="Amount Billed">BILLED</th>
             <th title="Amount Paid"  >PAID</th>
             <th title="Adjustments">ADJ</th>
             <th>DX</th>
             <!--th>DX1</th-->
             <th>TYPE</th>
             <th>INVOICE #</th>
             <th>MESSAGES</th>
             <th>CASH</th>
             <th>DEBIT</th>
             <th>Quantity</th>
              <th>Provider</th>
		<% if (bMultisites) {%>
			 <th>SITE</th>             
        <% }%>  
            <th class="hidden-print"><a href="#" onClick="checkAll(document.invoiceForm.invoiceAction)"><bean:message key="billing.billingStatus.action"/></a></th>
          </tr>
       </thead>

<tbody>
          
       <% //
       String invoiceNo = ""; 
       boolean nC = false;
       boolean newInvoice = true;
       
       double totalCash=0;
       double totalDebit=0;
       
       for (int i = 0 ; i < bList.size(); i++) { 
    	   BillingClaimHeader1Data ch1Obj = bList.get(i);
    	   
    	   if (bMultisites && ch1Obj.getClinic()!=null && curSite!=null 
    			   && !ch1Obj.getClinic().equals(curSite) && isSiteAccessPrivacy) // only applies on user have siteAccessPrivacy (SiteManager)
				continue; // multisite: skip if the line doesn't belong to the selected clinic    		   
    		   
	       if (bMultisites && selectedSite != null && (!selectedSite.equals(ch1Obj.getClinic())))
	    	   continue;
	       
	       patientCount ++;
			       
    	   // ra code
    	   if(raCode.trim().length() == 2) {
    		   if(!raData.isErrorCode(ch1Obj.getId(), raCode)) {
    			   continue;
    		   }
    	   }
    	   
           String ohip_no = ch1Obj.getProvider_ohip_no();
	       ArrayList raList = raData.getRADataIntern(ch1Obj.getId(), ch1Obj.getBilling_date().replaceAll("\\D", ""), ohip_no);
	       boolean incorrectVal = false;
	       
	       BigDecimal valueToAdd = new BigDecimal("0.00");
	       try{
	          valueToAdd = new BigDecimal(ch1Obj.getTotal()).setScale(2, BigDecimal.ROUND_HALF_UP);  
	       }catch(Exception badValueException){ 
	          incorrectVal = true;
	       }
	       total = total.add(valueToAdd);
	       String amountPaid = "0.00";
	       String errorCode = "";
	       if(serviceCode.equals("-") && raList.size() > 0){
	    	   amountPaid = raData.getAmountPaid(raList);
	    	   errorCode = raData.getErrorCodes(raList);
	       } else if(raList.size() > 0) {
	    	   amountPaid = raData.getAmountPaid(raList,ch1Obj.getId(),ch1Obj.getTransc_id());
	    	   errorCode = raData.getErrorCodes(raList); 
	       }
	       // 3rd party billing
	       if(ch1Obj.getPay_program().matches("PAT|OCF|ODS|CPP|STD|IFH")) {
	    	   amountPaid = ch1Obj.getPaid();
	       }
	       
	       int qty = ch1Obj.getNumItems();
	       
    	   amountPaid = (amountPaid==null||amountPaid.equals("")||amountPaid.equals("null"))? "0.00" : amountPaid;
	       
	       BigDecimal bTemp;
	       BigDecimal adj;
	       try {
		   		bTemp = (new BigDecimal(amountPaid.trim())).setScale(2,BigDecimal.ROUND_HALF_UP);
			    adj = (new BigDecimal(ch1Obj.getTotal())).setScale(2,BigDecimal.ROUND_HALF_UP);               
	       }
	       catch(NumberFormatException e ) {		   		
		   		MiscUtils.getLogger().error("Could not parse amount paid for invoice " + ch1Obj.getId(), e);
		   		throw e;
	       }
	       
	       paidTotal = paidTotal.add(bTemp);
           adj = adj.subtract(bTemp);
           adjTotal = adjTotal.add(adj);
	       
           String color = "";
               
               if (invoiceNo.equals(ch1Obj.getId())){
                   newInvoice = false;
               }
               else {
                   newInvoice = true; 
               }
	       if(!invoiceNo.equals(ch1Obj.getId())) {
	    	   invoiceNo = ch1Obj.getId(); 
	    	   nC = nC ? false : true;
	       } 
	       color = nC ? "class='success'" : "";
               String settleDate = ch1Obj.getSettle_date();
               if( settleDate == null || !ch1Obj.getStatus().equals("S")) {
                   settleDate = "N/A";
               }
               else {
                   settleDate = settleDate.substring(0, settleDate.indexOf(" "));
               }
               
               String payProgram = ch1Obj.getPay_program();
               boolean b3rdParty = false;
               if(payProgram.equals("PAT") || payProgram.equals("OCF") || payProgram.equals("ODS") || payProgram.equals("CPP") || payProgram.equals("STD")) {
                   b3rdParty = true;
               }
	      
               String cash = formatter.format(ch1Obj.getCashTotal());
			   String debit = formatter.format(ch1Obj.getDebitTotal());
			   
			   totalCash += ch1Obj.getCashTotal();
			   totalDebit += ch1Obj.getDebitTotal();
			   
				String gender = "";
				if(ch1Obj.getSex() != null && "1".equals(ch1Obj.getSex())) {
					gender = "M";
				}
				if(ch1Obj.getSex() != null && "2".equals(ch1Obj.getSex())) {
					gender = "F";
				}
				
				
       %>       
          <tr <%=color %>> 
             <td align="center"><%= ch1Obj.getBilling_date()%>  <%--=ch1Obj.getBilling_time()--%></td>  <!--SERVICE DATE-->
             <td align="center"><%=ch1Obj.getDemographic_no()%></td> <!--PATIENT-->
             <td align="center" class="<%=hideName?"hidden-print":""%>"><a href=# onclick="popupPage(800,740,'../../../demographic/demographiccontrol.jsp?demographic_no=<%=ch1Obj.getDemographic_no()%>&displaymode=edit&dboperation=search_detail');return false;"><%= ch1Obj.getDemographic_name()%></a></td> 
             <td align="center"><%= gender%></td> 
             <td align="center"><%=ch1Obj.getFacilty_num()!=null?ch1Obj.getFacilty_num():"" %></td>
             <td align="center"><%=ch1Obj.getStatus()%></td> <!--STAT-->
             <td align="center"><%=settleDate%></td> <!--SETTLE DATE-->
             <td align="center"><%=getHtmlSpace(ch1Obj.getTransc_id())%></td><!--CODE-->
             <td align="right"><%=getStdCurr(ch1Obj.getTotal())%></td><!--BILLED-->
             <td align="right"><%=amountPaid%></td><!--PAID-->
             <td align="center"><%=adj.toString()%></td> <!--SETTLE DATE-->
             <td align="center"><%=getHtmlSpace(ch1Obj.getRec_id())%></td><!--DX1-->
             <!--td>&nbsp;</td--><!--DX2-->
             <td align="center"><%=payProgram%></td>
             <td align="center"><a href=#  onclick="popupPage(800,700,'billingONCorrection.jsp?billing_no=<%=ch1Obj.getId()%>','BillCorrection<%=ch1Obj.getId()%>');nav_colour_swap(this.id, <%=bList.size()%>);return false;"><%=ch1Obj.getId()%></a></td><!--ACCOUNT-->
             <td class="highlightBox"><a id="A<%=i%>" href=#  onclick="popupPage(800,700,'billingONCorrection.jsp?billing_no=<%=ch1Obj.getId()%>','BillCorrection<%=ch1Obj.getId()%>');nav_colour_swap(this.id, <%=bList.size()%>);return false;">Edit</a> <%=errorCode%></td><!--MESSAGES-->
             <td align="center">$<%=cash%></td>
             <td align="center">$<%=debit%></td>
             <td align="center"><%=qty %></td>
             <td align="center"><%=ch1Obj.getProviderName() %></td>
             <% if (bMultisites) {%>
				 <td "<%=(ch1Obj.getClinic()== null || ch1Obj.getClinic().equalsIgnoreCase("null") ? "" : "bgcolor='" + siteBgColor.get(ch1Obj.getClinic()) + "'")%>">
				 	<%=(ch1Obj.getClinic()== null || ch1Obj.getClinic().equalsIgnoreCase("null") ? "" : siteShortName.get(ch1Obj.getClinic()))%>
				 </td>     <!--SITE-->          
        	<% }%>   
             <td align="center" class="hidden-print">
                 <% if (newInvoice && b3rdParty) { %>
                 <input type="checkbox" name="invoiceAction" id="invoiceAction<%=invoiceNo%>" value="<%=invoiceNo%>"/>
                 <% }%>
             </td><!--ACTION-->
          </tr>
       <% } %>  
       
          <tr class="warning"> 
             <td>Count:</td>  
             <td align="center"><%=patientCount%></td> 
             <td align="center" class="<%=hideName?"hidden-print":""%>">&nbsp;</td> 
             <td>&nbsp;</td> <!--LOCATION-->
             <td>&nbsp;</td> <!--STAT-->
             <td>&nbsp;</td>
             <td>&nbsp;</td>
             <td>Total:</td><!--CODE-->
             <td align="right"><%=total.toString()%></td><!--BILLED-->
             <td align="right"><%=paidTotal.toString()%></td><!--PAID-->
             <td align="right"><%=adjTotal.toString()%></td><!--ADJUSTMENTS-->
             <td>&nbsp;</td><!--DX-->
             <td>&nbsp;</td><!--TYPE-->
             <td>&nbsp;</td><!--ACCOUNT-->
             <td>&nbsp;</td><!--MESSAGES-->
             <td align="center">$<%=formatter.format(totalCash)%></td>
             <td align="center">$<%=formatter.format(totalDebit) %></td>
             <td align="center">&nbsp;</td>
             <td>&nbsp;</td><!--PROVIDER-->
             <% if (bMultisites) {%>
				 <td>&nbsp;</td><!--SITE-->          
        	<% }%>  
             <td align="center" class="hidden-print"><a href="#" onClick="submitForm('print')"><bean:message key="billing.billingStatus.print"/></a> 
                                <a href="#" onClick="submitForm('email')"><bean:message key="billing.billingStatus.email"/></a>
             </td>
          </tr>
	</tobdy>
       </table>
       <%if(bList != null && !bList.isEmpty()) {%> 
     	  <a download="oscar_invoices.xls" href="#" onclick="return ExcellentExport.excel(this, 'bListTable', 'OSCAR Invoices');">Export to Excel</a>
		  <a download="oscar_invoices.csv" href="#" onclick="return ExcellentExport.csv(this, 'bListTable');">Export to CSV</a>
     	  
     	  
       <%} %>
<% } %>
    </form>    
    </table>
    </form>
    </div>
</div>
<script language='javascript'>
    var startDate = $("#xml_vdate").datepicker({format : "yyyy-mm-dd"});
	var endDate = $("#xml_appointment_date").datepicker({format : "yyyy-mm-dd"});
	
	var paymentStartDate = $("#paymentStartDate").datepicker({format : "yyyy-mm-dd"});
	var paymentEndDate = $("#paymentEndDate").datepicker({format : "yyyy-mm-dd"});
	
    $( document ).ready(function() {
    	parent.parent.resizeIframe($('html').height());
    });
</script>
    </body>
    <%! String getStdCurr(String s) {
    		if(s != null) {
    			if(s.indexOf(".") >= 0) {
    				s += "00".substring(0, 3-s.length()+s.indexOf("."));
    			} else {
    				s = s + ".00";
    			}
    		}
    		return s;
    }
    String getHtmlSpace(String s) {
    	String ret = s==null? "&nbsp;" : s;
    	return ret;
    }
    String ch2StdCurrFromNoDot(String s) {
		if(s != null) {
			if(s.indexOf(".") <= 0) {
				if(s.length()>2) {
					s = s.substring(0, (s.length()-2)) + "." + s.substring((s.length()-2)) ;
				} else if(s.length()==1) {
					s = "0.0" + s;
				} else {
					s = "0." + s;
				}
			}
		}
		return s;
	}
    %>
</html>
