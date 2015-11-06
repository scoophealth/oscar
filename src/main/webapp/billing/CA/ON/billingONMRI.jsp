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

<%@ page import="oscar.util.ConversionUtils" %>
<%@ page import="java.util.*,java.sql.*,oscar.*,oscar.util.*,java.net.*" errorPage="errorpage.jsp"%>
<%@ page import="oscar.oscarBilling.ca.on.pageUtil.*"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*"%>

<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>

<%@ page import="org.oscarehr.billing.CA.model.BillActivity" %>
<%@ page import="org.oscarehr.billing.CA.dao.BillActivityDao" %>

<%@ page import="org.oscarehr.common.model.ProviderData"%>
<%@ page import="org.oscarehr.common.dao.ProviderDataDao"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%@ include file="../../../admin/dbconnection.jsp"%>


<%@page import="org.oscarehr.common.model.ProviderBillCenter" %>
<%@page import="org.oscarehr.common.dao.ProviderBillCenterDao" %>
<%@page import="org.oscarehr.util.SpringUtils" %>

<%
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	BillActivityDao billActivityDao = SpringUtils.getBean(BillActivityDao.class);
	ProviderDataDao providerDataDao = SpringUtils.getBean(ProviderDataDao.class);

	String curProvider_no = (String) session.getAttribute("user");
    
    boolean isTeamBillingOnly=false;
    boolean isSiteAccessPrivacy=false;
    boolean isTeamAccessPrivacy=false; 
%>

<security:oscarSec objectName="_team_billing_only" roleName="<%=roleName$ %>" rights="r" reverse="false"><% isTeamBillingOnly=true; %></security:oscarSec>
<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false"><%isSiteAccessPrivacy=true; %></security:oscarSec>
<security:oscarSec objectName="_team_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false"><%isTeamAccessPrivacy=true; %></security:oscarSec>

<% 
List<ProviderData> pdList = null;
HashMap<String,String> providerMap = new HashMap<String,String>();

//multisites function
if (isSiteAccessPrivacy || isTeamAccessPrivacy) {

	if (isSiteAccessPrivacy) 
		pdList = providerDataDao.findByProviderSite(curProvider_no);
	
	if (isTeamAccessPrivacy) 
		pdList = providerDataDao.findByProviderTeam(curProvider_no);

	for(ProviderData providerData : pdList) {
		providerMap.put(providerData.getId(), "true");
	}
}
%>
<html>
<head>
<title><bean:message key="admin.admin.btnGenerateOHIPDiskette" /></title>

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
<script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>

<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">

<%
			GregorianCalendar now = new GregorianCalendar();
			int curYear = now.get(Calendar.YEAR);
			int curMonth = (now.get(Calendar.MONTH) + 1);
			int curDay = now.get(Calendar.DAY_OF_MONTH);

			String nowDate = UtilDateUtilities.DateToString(new java.util.Date(), "yyyy-MM-dd HH:mm:ss"); //String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);
			String thisyear = (request.getParameter("year") == null || request.getParameter("year").equals("")) ? ("" + curYear)
					: request.getParameter("year");

			String[] yearArray = new String[5];
			for (int i = 0; i < yearArray.length; i++) {
				yearArray[i] = "" + (curYear - i);
			}

			String yearColor = "";
			String[] yearColorArray = new String[] { "#CCFFCC", "#BBBBBB", "#CCCCCC", "#DDDDDD", "#EEEEEE" };
			for (int i = 0; i < yearArray.length; i++) {
				if (yearArray[i].equals(thisyear)) {
					yearColor = yearColorArray[i];
					break;
				}
			}

			String monthCode = BillingDataHlp.getPropMonthCode().getProperty("" + curMonth);
			String ohipdownload = oscarVariables.getProperty("HOME_DIR");
			session.setAttribute("ohipdownload", ohipdownload);

			//			 get the current year's billing disk filenames
			BillingReviewPrep prep = new BillingReviewPrep();
			List mriList = prep.getMRIList(thisyear + "-01-01 00:00:01", thisyear + "-12-31 23:59:59","U");
			
			String xml_vdate=request.getParameter("xml_vdate") == null?"":request.getParameter("xml_vdate");
			String xml_appointment_date = request.getParameter("xml_appointment_date")==null? UtilDateUtilities.DateToString(new java.util.Date(), "yyyy-MM-dd") : request.getParameter("xml_appointment_date");
			%>

<script language="JavaScript" type="text/JavaScript">

var checkSubmitFlg = false;
function checkSubmit() {
	if (checkSubmitFlg == true) {
		return false;      
	}
	checkSubmitFlg = true;
	document.forms[0].Submit.disabled = true;
	return true;   
}

function recreate(si) {
    ret = confirm("Are you sure you want to regenerate the file? \n\nWARNING: This should only be performed in very specific circumstances. If you are unsure, consult your OSCAR administrator before using this feature.");
	if(ret) {
		ss=document.forms[0].billcenter[document.forms[0].billcenter.selectedIndex].value;
		var su = document.forms[0].useProviderMOH.checked;
		location.href="onregenreport.jsp?diskId="+si+"&billcenter="+ss+"&useProviderMOH="+su;		
	}
}

</script>


<script>
function recreate(si) {
    ret = confirm("Are you sure you want to regenerate the file? \n\nWARNING: This should only be performed in very specific circumstances. If you are unsure, consult your OSCAR administrator before using this feature.");
	if(ret) {
		ss=document.forms[0].billcenter[document.forms[0].billcenter.selectedIndex].value;
		var su = document.forms[0].useProviderMOH.checked;
		location.href="onregenreport.jsp?diskId="+si+"&billcenter="+ss+"&useProviderMOH="+su;		
	}
}

var providerBillCenterMap = new Object();
<%
ProviderBillCenterDao providerBillCenterDao = (ProviderBillCenterDao)SpringUtils.getBean("providerBillCenterDao");

for(Provider p : providerDao.getBillableProviders()) {
	String providerNo = p.getProviderNo();
	ProviderBillCenter pbc = providerBillCenterDao.find(providerNo);
	if(pbc != null) {
	%>
		providerBillCenterMap['<%=providerNo%>'] = '<%=pbc.getBillCenterCode()%>';
	<%
	}
}
%>

function setBillingCenter( providerNo ) {
	var bcDropdown = document.getElementById("billcenter");
	
	var textToFind = providerBillCenterMap[providerNo];
	
	if (bcDropdown) {
		for (var i = 0; i < bcDropdown.options.length; i++) {
	    if (bcDropdown.options[i].value === textToFind) {
	        bcDropdown.selectedIndex = i;
	        break;
	    }
	}
	}
}
</script>

<style type="text/css">
	input[name=useProviderMOH] {margin: 4px 4px 4px;}
	
@media print {
  .visible-print {
    display: inherit !important;
  }
  .hidden-print {
    display: none !important;
  }
  
  /*this is so the link locatons don't display*/
  a:link:after, a:visited:after {
    content: "";
  }
 }
</style>
</head>

<body>

<h3><bean:message key="admin.admin.btnGenerateOHIPDiskette" /></h3>

<div class="container-fluid">

<div id="Layer1" style="position: absolute; left: 90px; top: 35px; width: 0px; height: 12px; z-index: 1"></div>

<div class="row well hidden-print">

<button type='button' name='print' value='Print' class="btn hidden-print" onClick='window.print()' style="position:absolute;top:20px;right:20px;"> <i class="icon icon-print"></i> Print</button>

<div class="dropdown">
<!-- Link or button to toggle dropdown -->
<a href="#" class="dropdown-archive">Show Archive</a>
		<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
		<%for (int i = 0; i < 5; i++) { %>
		<li><a href="billingONMRI.jsp?year=<%=yearArray[i]%>">YEAR <%=yearArray[i]%></a></li>
		<%}%>
		</ul>
</div>





	<form name="form1" method="post" action="ongenreport.jsp" onsubmit="return checkSubmit();">

		<div class="span4">
		Select Provider<br>
		<select name="provider" onchange = "setBillingCenter(this.value);">
			<%
			List providerStr; 
			
			if (isTeamBillingOnly || isTeamAccessPrivacy) {
				providerStr = prep.getTeamProviderBillingStr(curProvider_no);
			}
			else if (isSiteAccessPrivacy) {
				providerStr = prep.getSiteProviderBillingStr(curProvider_no);
			}
			else {
				providerStr = prep.getProviderBillingStr();
			}
			
			
			if(providerStr.size() == 1) {
				String temp[] = ((String) providerStr.get(0)).split("\\|");
			%>
			<option value="<%=temp[0]%>"><%=temp[1]%>, <%=temp[2]%></option>
			<%
			} else {
			%>
			<option value="all">All Providers</option>

			<%
			for (int i = 0; i < providerStr.size(); i++) {
				String temp[] = ((String) providerStr.get(i)).split("\\|");
			%>
			<option value="<%=temp[0]%>"><%=temp[1]%>, <%=temp[2]%></option>
			<%}
			}
			%>
		</select>
		</div>

		<div class="span4">
		Billing Center<br>
		<select name="billcenter" id="billcenter">
			<%for (Enumeration e = BillingDataHlp.getPropBillingCenter().propertyNames(); e.hasMoreElements();) {
				String centerCode = (String) e.nextElement();
%>
			<option value="<%=centerCode%>"
				<%=oscarVariables.getProperty("billcenter").compareTo(centerCode)==0?"selected":""%>><%=BillingDataHlp.getPropBillingCenter().getProperty(centerCode)%></option>
			<%}
			%>
		</select>
		</div>
		
		<input type="hidden" name="monthCode" value="<%=monthCode%>">
		<input type="hidden" name="verCode" value="V03"> 
		<input type="hidden" name="curUser" value="<%=curProvider_no%>"> 
		<input type="hidden" name="curDate" value="<%=nowDate%>">


		<div class="span4">		
		<label>Service Date Start:</label>
		<div class="input-append">
			<input type="text" name="xml_vdate" id="xml_vdate" value="<%=xml_vdate%>" pattern="^\d{4}-((0\d)|(1[012]))-(([012]\d)|3[01])$" autocomplete="off" />
			<span class="add-on"><i class="icon-calendar"></i></span>
		</div>
		</div>
		
		<div class="span4">		
		<label>Service Date End:</label>
		<div class="input-append">
			<input type="text" name="xml_appointment_date" id="xml_appointment_date" value="<%=xml_appointment_date%>" pattern="^\d{4}-((0\d)|(1[012]))-(([012]\d)|3[01])$" autocomplete="off" />
			<span class="add-on"><i class="icon-calendar"></i></span>
		</div>
		</div>
		
		<div class="span10">
		<input type="checkbox" name="useProviderMOH" id="useProviderMOH" <%=("true".equals(request.getParameter("useProviderMOH")) ? "checked" : "") %>>Use individual provider's bill center setting (will use above bill center if provider does not have one set.)
		<br><br>
		<input class="btn btn-primary" type="submit" name="Submit" value="Create Report">
		</div>
	</form>



</div><!--row well-->



<table class="table ">
<thead>
	<tr>
		<th>Provider</th>
		<th>Creation Date</th>
		<th>Clm/Rec</th>
		<th>Total</th>
		<th colspan=2>to OHIP</th>
		<th>HTML</th>
	</tr>
</thead>

<tbody>
	<%Properties proName = (new JdbcBillingPageUtil()).getPropProviderName();
			String pro_no = "", pro_ohip = "", pro_group = "", pro_name = "", updatedate = "", cr = "", oFile = "", hFile = "", total = "";

			int count = 0;
			for(int i=0; i<mriList.size(); i++) {
				BillingDiskNameData obj = (BillingDiskNameData) mriList.get(i);
				oFile = obj.getOhipfilename();
				pro_group = obj.getGroupno();
				updatedate = obj.getUpdatedatetime();
				String createdate = obj.getCreatedatetime();
				Vector vecProviderOhipNo = obj.getProviderohipno();
				Vector vecProviderNo = obj.getProviderno();
				for(int j=0; j<vecProviderNo.size(); j++){
					count++;
					pro_ohip = (String)vecProviderOhipNo.get(j);
					pro_no = (String)vecProviderNo.get(j);
					cr = (String)obj.getVecClaimrecord().get(j);
					hFile = (String)obj.getHtmlfilename().get(j);
					total = (String)obj.getVecTotal().get(j);
					pro_name = proName.getProperty(pro_no);
					String bgColor = count%2==0?yearColor:"ivory";
					if(!updatedate.equals(createdate)) bgColor = "silver";
					
				    //multisites. skip record if not belong to same site/team
				    if (isSiteAccessPrivacy || isTeamAccessPrivacy) {
				    	if(providerMap.get(pro_no)== null)  continue;
				    }

%>

	<tr onMouseOver="this.style.backgroundColor='pink';" onMouseout="this.style.backgroundColor='<%=bgColor%>';" bgcolor="<%=bgColor%>">
		<td><font size="2"><%=pro_name%></font></td>
		<td align="center"><font size="2"><%=updatedate.substring(0,16)%></font></td>
		<td align="center"><font size="2"><%=cr%></font></td>
		<td align="right"><font size="2"><%=total%></font></td>

		<td width="15%"><font size="2"> <a
			href="../../../servlet/OscarDownload?homepath=ohipdownload&filename=<%=oFile%>"
			target="_blank"><%=oFile%></a></font></td>
		<td width="3%"><input type="button" value="R" class="btn hidden-print" onclick="recreate(<%=obj.getId() %>)" /></td>
		<td><font size="2"> <a
			href="../../../servlet/OscarDownload?homepath=ohipdownload&filename=<%=hFile%>"
			target="_blank"><%=hFile%></a></font></td>
	</tr>

	<%}}
		%>

	<% // get old data records
proName = new Properties();
for(Provider p : providerDao.getActiveProviders()) {
	if(p.getOhipNo() != null && !p.getOhipNo().isEmpty()) {
		proName.setProperty(p.getOhipNo(), (p.getLastName() + ", " +p.getFirstName()));
	}
}


String[] paramYear = new String[2];
paramYear[0] = thisyear+"/01/01";
paramYear[1] = thisyear+"/12/31 23:59:59";
//String pro_ohip="", pro_group="", pro_name="", updatedate="", cr="", oFile="", hFile="", total="";

//int count = 0;
List<BillActivity> bas = billActivityDao.findCurrentByDateRange(ConversionUtils.fromDateString(thisyear+"-01-01 00:00:00"), ConversionUtils.fromDateString(thisyear+"-12-31 23:59:59"));
Collections.sort(bas,BillActivity.UpdateDateTimeComparator);
for(BillActivity ba:bas) {
	count++;
	pro_ohip = ba.getProviderOhipNo();
	pro_group = ba.getGroupNo();
	updatedate = ConversionUtils.toDateString(ba.getUpdateDateTime());
	cr = ba.getClaimRecord();
	oFile = ba.getOhipFilename();
	hFile = ba.getHtmlFilename();
	total = ba.getTotal()==null?"0.00":ba.getTotal();
	pro_name = proName.getProperty(pro_ohip);
%>

	<tr bgcolor="<%=count%2==0?yearColor:"white"%>">
		<td><%if(pro_name!=null){ %><font size="2"><%=pro_name%></font><%}%></td>
		<td align="center"><font size="2"><%=updatedate%></font></td>
		<td align="center"><font size="2"><%=cr%></td>
		<td align="right"><font size="2"><%=total.substring(0,total.indexOf(".")) + total.substring(total.indexOf("."), total.indexOf(".") + 3)%></font></td>

		<td colspan=2><font size="2"> <a
			href="../../../servlet/OscarDownload?homepath=ohipdownload&filename=<%=oFile%>"
			target="_blank"><%=oFile%></a></font></td>
		<td><font size="2"> <a
			href="../../../servlet/OscarDownload?homepath=ohipdownload&filename=<%=hFile%>"
			target="_blank"><%=hFile%></a></font></td>
	</tr>

	<%
}
%>
</tbody>
</table>
</div><!--container-->

<script>
$('.dropdown-archive').dropdown();

var startDate = $("#xml_vdate").datepicker({
	format : "yyyy-mm-dd"
});

var endDate = $("#xml_appointment_date").datepicker({
	format : "yyyy-mm-dd"
});

$( document ).ready(function() {
parent.parent.resizeIframe($('html').height());

});

</script>
</body>
</html>
