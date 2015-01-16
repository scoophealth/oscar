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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean isTeamBillingOnly=false;
    boolean isSiteAccessPrivacy=false;
    boolean isTeamAccessPrivacy=false; 
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
String strBillType = "";
if (billType == null || billType.length == 0) { // no boxes checked
	bSearch = false;
	strBillType = "'HCP','WCB','RMB','NOT','PAT','OCF','ODS','CPP','STD','IFH',";
} else { //at least on box checked
	for(int i=0; i<billType.length; i++) {
		strBillType += "'" + billType[i] + "'" + ",";
	}
}

strBillType = strBillType.endsWith(",")? strBillType.substring(0,strBillType.length()-1): strBillType;
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

List pList = isTeamBillingOnly
		? (Vector)(new JdbcBillingPageUtil()).getCurTeamProviderStr((String) session.getAttribute("user"))
		: (Vector)(new JdbcBillingPageUtil()).getCurProviderStr();

BillingStatusPrep sObj = new BillingStatusPrep();
List bList = null;
if((serviceCode == null || billingForm == null) && dx.length()<2 && visitType.length()<2) {
	bList = bSearch ? sObj.getBills(strBillType, statusType, providerNo, startDate, endDate, demoNo) : new Vector();
	//serviceCode = "-";
	serviceCode = "%";
} else {
	serviceCode = (serviceCode == null || serviceCode.length()<2)? "%" : serviceCode; 
	bList = bSearch ? sObj.getBills(strBillType, statusType,  providerNo, startDate,  endDate,  demoNo, serviceCode, dx, visitType, billingForm) : new Vector();
}


RAData raData = new RAData();


BigDecimal total = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP); 
BigDecimal paidTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal adjTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);

%>

<%
	String ohipNo= "";
	if(request.getParameter("provider_ohipNo")!=null)
		ohipNo = request.getParameter("provider_ohipNo");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.oscarehr.common.model.Site"%>
<%@page import="org.oscarehr.common.model.Provider"%><html>
    <head>
        <title>Bill Status</title>
        <script type="text/javascript" src="../../../share/javascript/Oscar.js"></script>
		<link rel="stylesheet" type="text/css" href="billingON.css" />
        <link rel="stylesheet" type="text/css" media="all" href="../../../share/calendar/calendar.css" title="win2k-cold-1" /> 
        <script type="text/javascript" src="../../../share/calendar/calendar.js"></script>
        <script type="text/javascript" src="../../../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>                                                            
        <script type="text/javascript" src="../../../share/calendar/calendar-setup.js"></script>
       
        <script type="text/javascript">
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
				String temp[] = ((String) pList.get(i)).split("\\|");				
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
</script>
        <style type="text/css">
            body {
              margin:0;
              padding:0;
              font-family: verdana,arial,helvetica,sans-serif;
            }
            
            div.statusTypeList{
              margin-top:0px;
              padding-top:0px;
              margin-bottom:0px;
              padding-bottom:0px;
              background-color: #CCFFCC;
            }
            

            div.statusTypeList ul{
              list-style-type:none; 
              list-style-position:outside;       
              padding-left:1px;
              margin-left:1px;    
              margin-top:0px;
              padding-top:1px;
              margin-bottom:0px;
              padding-bottom:0px;
	      font-size: x-small;
            }

            div.statusTypeList ul li{
              display: inline;
            }
            
            
            div.wrapper{
              background-color: #eeeeff;
              margin-top:0px;
              padding-top:0px;
              margin-bottom:0px;
              padding-bottom:0px;	
            }

            div.wrapper br{
              clear: left;
            }

            div.wrapper ul{
              width: 80%;
              background-color: #eeeeff;
              list-style:none;
              list-style-type:none; 
              list-style-position:outside;       
              padding-left:1px;
              margin-left:1px;    
              margin-top:0px;
              padding-top:1px;
              margin-bottom:0px;
              padding-bottom:0px;
	      font-size: x-small;
            }

            div.wrapper ul li{
              display: inline;
              background-color: #eeeeff;
              vertical-align: middle;
            }
            
            div.tableListing table {
               width: 100%;
               margin-top:0px;
               border-width: 1px 1px 1px 1px;
	       border-spacing: 0px;
	       border-style: outset outset outset outset;
	       border-color: gray gray gray gray;
	       border-collapse: collapse;
	
        
            }
            
            div.tableListing table tr td{
               font-size: x-small;
               text-align: center;
               border-width: 1px 1px 1px 1px;
               padding: 1px 1px 1px 1px;
               border-style: inset inset inset inset;
               border-color: gray gray gray gray;
               //background-color: white;
               -moz-border-radius: 0px 0px 0px 0px;
            }
            
            div.tableListing table tr th{
               font-size: small;
               border-width: 1px 1px 1px 1px;
               padding: 1px 1px 1px 1px;
               border-style: inset inset inset inset;
               border-color: gray gray gray gray;
               background-color: #FFCC99;
               -moz-border-radius: 0px 0px 0px 0px;
            }
   
            div.selectionForm{
            background-color: #eeeeff;
            padding-bottom:3px;
            }
            div.selectionForm a{
             font-size: x-small;
            }
            
            form{
              margin: 0px;
              padding: 0px;
            }
        </style>
    </head>
    <body>

    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="myDarkGreen">
      <tr> 
        <td height="40" width="10%"></td>
        <td width="90%" align="left"> 
          <font face="Arial" color="#FFFFFF" size="4"><b>oscar<font size="3">Billing</font></b></font> 
        </td>
        <td nowrap valign="bottom">
        <div align="right"><a href="javascript: function myFunction() {return false; }" onClick="popupPage(700,720,'../../../oscarReport/manageProvider.jsp?action=billingreport')"><font face="Arial" color="white" size="1">Manage Provider List </font></a></div>
        <b><%=DateUtils.sumDate("yyyy-MM-dd","0")%></b>
		<input type='button' name='print' value='Print' onClick='window.print()'>
        </td>
      </tr>
    </table>

    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="myYellow">
    <form name="serviceform" method="get" action="billingONStatus.jsp">
    <tr><td width="30%" class="myIvory">
        <input type="checkbox" name="billType" value="HCP" <%=strBillType.indexOf("HCP")>=0?"checked":""%>><span class="smallFont">Bill OHIP</span></input>
        <input type="checkbox" name="billType" value="RMB" <%=strBillType.indexOf("RMB")>=0?"checked":""%>><span class="smallFont">RMB</span></input>
        <input type="checkbox" name="billType" value="WCB" <%=strBillType.indexOf("WCB")>=0?"checked":""%>><span class="smallFont">WCB</span></input>
        <input type="checkbox" name="billType" value="NOT" <%=strBillType.indexOf("NOT")>=0?"checked":""%>><span class="smallFont">Not Bill</span></input>
        <input type="checkbox" name="billType" value="PAT" <%=strBillType.indexOf("PAT")>=0?"checked":""%>><span class="smallFont">Bill Patient</span></input><br>
        <input type="checkbox" name="billType" value="OCF" <%=strBillType.indexOf("OCF")>=0?"checked":""%>><span class="smallFont">OCF</span></input>
        <input type="checkbox" name="billType" value="ODS" <%=strBillType.indexOf("ODS")>=0?"checked":""%>><span class="smallFont">ODSP</span></input>
        <input type="checkbox" name="billType" value="CPP" <%=strBillType.indexOf("CPP")>=0?"checked":""%>><span class="smallFont">CPP</span></input>
        <input type="checkbox" name="billType" value="STD" <%=strBillType.indexOf("STD")>=0?"checked":""%>><span class="smallFont">STD/LTD</span></input>
        <input type="checkbox" name="billType" value="IFH" <%=strBillType.indexOf("IFH")>=0?"checked":""%>><span class="smallFont">IFH</span></input>
        <input type="checkbox" name="billTypeAll" value="ALL" checked onclick="changeStatus();"><span class="smallFont">ALL</span></input>
    </td>
    <td align="center" class="myYellow">


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
      	<select id="providerview" name="providerview" onchange="changeProvider(true);" style="width:140px"></select>
<% if (request.getParameter("providerview")!=null) { %>
      	<script>
     	window.onload=function(){changeSite(document.getElementById("site"));}
      	</script>
<% } // multisite end ==========================================
} else {
%>	 
    <select name="providerview" onchange="changeProvider(true);">
			<%
			if(pList.size() == 1) {
				String temp[] = ((String) pList.get(0)).split("\\|");
			%>
			<option value="<%=temp[0]%>"> <%=temp[1]%>, <%=temp[2]%></option>
			<%
			} else {
			%>
       <option value="all">All Providers</option>
    <% for (int i = 0 ; i < pList.size(); i++) { 
		String temp[] = ((String) pList.get(i)).split("\\|");
	%>
       <option value="<%=temp[0]%>" <%=providerNo.equals(temp[0])?"selected":""%>><%=temp[1]%>, <%=temp[2]%></option>
         
    <% } 
    } %>
    </select>
<% } %>
    
    
    <font size="1">OHIP No.: <input type="text" size="7" name="provider_ohipNo" readonly value="<%=ohipNo%>"></font>
          <font size="1"><a href="javascript: function myFunction() {return false; }" id="hlSDate">From:</a></font> 
          <input type="text" name="xml_vdate" id="xml_vdate" value="<%=startDate%>" size=10  style="width:70px"/>          
        
          <font size="1" ><a href="javascript: function myFunction() {return false; }" id="hlADate" >To:</a></font> 
          <input type="text" name="xml_appointment_date" id="xml_appointment_date" value="<%=endDate%>" size=10 style="width:70px" /> 
          <br><a href="javascript: function myFunction() {return false; }" onClick="fillEndDate('<%=DateUtils.sumDate("yyyy-MM-dd","-30")%>')" >30</a>
          <a href="javascript: function myFunction() {return false; }" onClick="fillEndDate('<%=DateUtils.sumDate("yyyy-MM-dd","-60")%>')" >60</a>
          <a href="javascript: function myFunction() {return false; }" onClick="fillEndDate('<%=DateUtils.sumDate("yyyy-MM-dd","-90")%>')" >90</a>                         
		<input type="submit" name="Submit" value="Create Report">
        <div>         
          <font size="-1">Dx:</font><input type="text" name="dx" size="3" value="<%=dx%>"/>
          <font size="-1">Serv. Code:</font><input type="text" name="serviceCode" size="14" value="<%=serviceCode%>"/>
          <font size="-1">Demographic:</font><input type="text" name="demographicNo" size="5" value="<%=demoNo%>"/>
          <font size="-1">RA Code:</font><input type="text" name="raCode" size="2" value="<%=raCode%>"/>
			<select name="visitType">
				<option value="-" <%=visitType.startsWith("-")?"selected":""%>> </option>
				<option value="00" <%=visitType.startsWith("00")?"selected":""%>>Clinic Visit</option>
				<option value="01" <%=visitType.startsWith("01")?"selected":""%>>Outpatient Visit</option>
				<option value="02" <%=visitType.startsWith("02")?"selected":""%>>Hospital Visit</option>
				<option value="03" <%=visitType.startsWith("03")?"selected":""%>>ER</option>
				<option value="04" <%=visitType.startsWith("04")?"selected":""%>>Nursing Home</option>
				<option value="05" <%=visitType.startsWith("05")?"selected":""%>>Home Visit</option>
			</select>
            <font size="-1">Billing Form:&nbsp;&nbsp;
                <select name="billing_form">
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
        </td>                           
      </tr>
    </table>
    
    <div class="statusTypeList">
      <ul>
        <li><input type="radio" name="statusType" value="_" <%=statusType.equals("_")?"checked":""%>>Rejected</input></li>
        <li><input type="radio" name="statusType" value="H" <%=statusType.equals("H")?"checked":""%>>Capitated</input></li>
        <li><input type="radio" name="statusType" value="O" <%=statusType.equals("O")?"checked":""%>>Invoiced</input></li>
        <li><input type="radio" name="statusType" value="P" <%=statusType.equals("P")?"checked":""%>>Bill Patient</input></li>
        <!--li><input type="radio" name="statusType" value="N" <%=statusType.equals("N")?"checked":""%>>Do Not Bill</input></li>
        <li><input type="radio" name="statusType" value="W" <%=statusType.equals("W")?"checked":""%>>WCB</input></li>-->
        <li><input type="radio" name="statusType" value="B" <%=statusType.equals("B")?"checked":""%>>Submmitted OHIP</input></li>
        <li><input type="radio" name="statusType" value="S" <%=statusType.equals("S")?"checked":""%>>Settled/Paid</input></li>
        <li><input type="radio" name="statusType" value="X" <%=statusType.equals("X")?"checked":""%>>Bad Debt</input></li>
        <li><input type="radio" name="statusType" value="D" <%=statusType.equals("D")?"checked":""%>>Deleted Bill</input></li>
        <li><input type="radio" name="statusType" value="%" <%=statusType.equals("%")?"checked":""%>>All</input></li>
      </ul>
    </div>
    </form>
<% //
if(statusType.equals("_")) { %>
    <!--  div class="rejected list"-->
       <table width="100%" border="1" cellspacing="0" cellpadding="1">
          <tr class="myYellow"> 
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
             <th>OHIP Claim Id</th>
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
	
	JdbcBillingRAImpl raObj = new JdbcBillingRAImpl();
	for(int i=0; i<lPat.size(); i++) {
		BillingErrorRepData bObj = (BillingErrorRepData) lPat.get(i);
		
		// get ohip claim number
		String claimNo = raObj.getRAClaimNo4BillingNo( bObj.getBilling_no() );
		
		String color = "";
		if(!invoiceNo.equals(bObj.getBilling_no())) {
			invoiceNo = bObj.getBilling_no(); 
			nC = nC ? false : true;
		} 
	    color = nC ? "class='myGreen'" : "";
	%>
    		<tr <%=color %>>
    			<td><span class="smallFont"><%=bObj.getHin() %> <%=bObj.getVer() %></span></td>
    			<td><font size="-1"><%=bObj.getDob() %></font></td>
    			<td align="right">
    			<a href="javascript: function myFunction() {return false; }"  onclick="javascript:popup(700,700,'billingONCorrection.jsp?billing_no=<%=bObj.getBilling_no()%>','BillCorrection<%=bObj.getBilling_no()%>');return false;">
    			<%=bObj.getBilling_no() %></a></td>
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
    			<td>
					<a href="javascript: function myFunction() {return false; }"  onclick="javascript:popup(700,700,'billingONCorrection.jsp?claim_no=<%=claimNo%>','BillCorrection<%=claimNo%>');return false;"><%=claimNo%></a>
    			</td>
    		</tr>
<% }}} else { %>
    <!--  div class="tableListing"-->
       <table width="100%" border="1" cellspacing="0" cellpadding="1">
          <tr class="myYellow"> 
             <th>SERVICE DATE</th>
             <th>PATIENT</th>
             <th>PATIENT NAME</th>
             <th title="Status">STAT</th>
             <th>SETTLED</th>
             <th title="Code Billed">CODE</th>
             <th title="Amount Billed">BILLED</th>
             <th title="Amount Paid"  >PAID</th>
             <th title="Adjustments">ADJ</th>
             <th>DX</th>
             <!--th>DX1</th-->
             <th>TYPE</th>
             <th>ACCOUNT</th>
             <th>MESSAGES</th>
             <th>OHIP Claim Id</td>
		<% if (bMultisites) {%>
			 <th>SITE</th>             
        <% }%>     
          </tr>
       
          
       <% //
       String invoiceNo = ""; 
       boolean nC = false;

		JdbcBillingRAImpl raObj = new JdbcBillingRAImpl();

       for (int i = 0 ; i < bList.size(); i++) { 
    	   BillingClaimHeader1Data ch1Obj = (BillingClaimHeader1Data) bList.get(i);
    	   
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
	    	   amountPaid = (amountPaid==null||amountPaid.equals("")||amountPaid.equals("null"))? "0.00" : amountPaid;
	       }
	       BigDecimal bTemp = (new BigDecimal(amountPaid.trim())).setScale(2,BigDecimal.ROUND_HALF_UP);
	       paidTotal = paidTotal.add(bTemp);
	       BigDecimal adj = (new BigDecimal(ch1Obj.getTotal())).setScale(2,BigDecimal.ROUND_HALF_UP);               
               adj = adj.subtract(bTemp);
               adjTotal = adjTotal.add(adj);
	       String color = "";
	       if(!invoiceNo.equals(ch1Obj.getId())) {
	    	   invoiceNo = ch1Obj.getId(); 
	    	   nC = nC ? false : true;
	       } 
	       color = nC ? "class='myGreen'" : "";
               String settleDate = ch1Obj.getSettle_date();
               if( settleDate == null || !ch1Obj.getStatus().equals("S")) {
                   settleDate = "N/A";
               }
               else {
                   settleDate = settleDate.substring(0, settleDate.indexOf(" "));
               }
               
           // get ohip claim number
			String claimNo = raObj.getRAClaimNo4BillingNo( ch1Obj.getId() );
	      
       %>       
          <tr <%=color %>> 
             <td align="center"><%= ch1Obj.getBilling_date()%>  <%--=ch1Obj.getBilling_time()--%></td>  <!--SERVICE DATE-->
             <td align="center"><a href="javascript: setDemographic('<%=ch1Obj.getDemographic_no()%>');"><%=ch1Obj.getDemographic_no()%></a></td> <!--PATIENT-->
             <td align="center"><a href=# onclick="popupPage(800,740,'../../../demographic/demographiccontrol.jsp?demographic_no=<%=ch1Obj.getDemographic_no()%>&displaymode=edit&dboperation=search_detail');return false;"><%= ch1Obj.getDemographic_name()%></a></td> 
             <td align="center"><%=ch1Obj.getStatus()%></td> <!--STAT-->
             <td align="center"><%=settleDate%></td> <!--SETTLE DATE-->
             <td align="center"><%=getHtmlSpace(ch1Obj.getTransc_id())%></td><!--CODE-->
             <td align="right"><%=getStdCurr(ch1Obj.getTotal())%></td><!--BILLED-->
             <td align="right">
                 <a href="javascript: function myFunction() {return false; }"  onclick="javascript:popup(700,700,'billingRAView.jsp?billing_no=<%=ch1Obj.getId()%>','RAView<%=ch1Obj.getId()%>');return false;">
                 <%=amountPaid%>
                 </a>
             </td><!--PAID-->
             <td align="center"><%=adj.toString()%></td> <!--SETTLE DATE-->
             <td align="center"><%=getHtmlSpace(ch1Obj.getRec_id())%></td><!--DX1-->
             <!--td>&nbsp;</td--><!--DX2-->
             <td align="center"><%=ch1Obj.getPay_program()%></td><!--DX3-->
             <td align="center">
                 <!--  a href="javascript: function myFunction() {return false; }"  onclick="javascript:popup(700,700,'../../../billing/CA/BC/billingView.do?billing_no=<%=ch1Obj.getId()%>','BillView<%=ch1Obj.getId()%>')">--><%=ch1Obj.getId()%>

             </td><!--ACCOUNT-->
             <td>
                 <a href="javascript: function myFunction() {return false; }"  onclick="javascript:popup(700,700,'billingONCorrection.jsp?billing_no=<%=ch1Obj.getId()%>','BillCorrection<%=ch1Obj.getId()%>');return false;">Edit</a>
                 <%=errorCode%>
             </td><!--MESSAGES-->
             <td>
				 <a href="javascript: function myFunction() {return false; }"  onclick="javascript:popup(700,700,'billingONCorrection.jsp?claim_no=<%=claimNo%>','BillCorrection<%=claimNo%>');return false;"><%=claimNo%></a>
             </td>
             <% if (bMultisites) {%>
				 <td "<%=(ch1Obj.getClinic()== null || ch1Obj.getClinic().equalsIgnoreCase("null") ? "" : "bgcolor='" + siteBgColor.get(ch1Obj.getClinic()) + "'")%>">
				 	<%=(ch1Obj.getClinic()== null || ch1Obj.getClinic().equalsIgnoreCase("null") ? "" : siteShortName.get(ch1Obj.getClinic()))%>
				 </td>     <!--SITE-->          
        	<% }%>     
          </tr>
       <% } %>  
       
          <tr class="myYellow"> 
             <td>Count:</td>  
             <td align="center"><%=patientCount%></td> 
             <td align="center"><%=patientCount%></td> 
             <td>&nbsp;</td> <!--STAT-->
             <td>&nbsp;</td>
             <td>Total:</td><!--CODE-->
             <td align="right"><%=total.toString()%></td><!--BILLED-->
             <td align="right"><%=paidTotal.toString()%></td><!--PAID-->
             <td align="right"><%=adjTotal.toString()%></td><!--ADJUSTMENTS-->
             <td>&nbsp;</td><!--DX1-->
             <td>&nbsp;</td><!--DX2-->
             <td>&nbsp;</td><!--DX3-->
             <td>&nbsp;</td><!--ACCOUNT-->
             <td>&nbsp;</td><!--MESSAGES-->
             <td>$nbsp;</td>
             <% if (bMultisites) {%>
				 <td>&nbsp;</td><!--SITE-->          
        	<% }%>    
          </tr>
       <table>
    </div>
<% } %>
    
    <script language='javascript'>
       Calendar.setup({inputField:"xml_vdate",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlSDate",singleClick:true,step:1});          
       Calendar.setup({inputField:"xml_appointment_date",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlADate",singleClick:true,step:1});                      
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
