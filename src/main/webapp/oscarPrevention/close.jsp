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
<%@page import="java.net.URLEncoder"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.*" %>
<%@page import="org.oscarehr.common.dao.*" %>
<%@page import="org.oscarehr.billing.CA.ON.dao.*" %>
<%@page import="org.oscarehr.PMmodule.dao.*" %>
<%@page import="org.apache.commons.codec.binary.Base64" %>
<%@page import="org.oscarehr.util.MiscUtils" %>
<%
java.util.Properties oscarVariables = oscar.OscarProperties.getInstance();
StringBuilder url = null;
if (org.oscarehr.common.IsPropertiesOn.propertiesOn("ENABLE_PREVENTION_BILLING")) {
	String prov = (oscarVariables.getProperty("billregion","")).trim().toUpperCase();
	String curProvider_no = (String) session.getAttribute("user");
	String billflag = request.getParameter("billflag");
	String prevId = request.getParameter("prevId");
	if ("1".equals(billflag) && prevId != null && !prevId.isEmpty()) {
		do {
			String demoNo = request.getParameter("demoNo");
			if (demoNo == null || demoNo.isEmpty()) {
				break;
			}
			DemographicDao demoDao = (DemographicDao)SpringUtils.getBean(DemographicDao.class);
			Demographic demo = demoDao.getDemographic(demoNo);
			if (demo == null) {
				break;
			}
			String prevType = request.getParameter("prevType");
			if (prevType == null || prevType.isEmpty()) {
				break;
			}
			url = new StringBuilder();
			String dateString = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
			DemographicCustDao demographicCustDao = (DemographicCustDao)SpringUtils.getBean("demographicCustDao");
			DemographicCust demographicCust = demographicCustDao.find(Integer.valueOf(demoNo));
			String demoAlert = "";
			if (demographicCust != null) {
				demoAlert = demographicCust.getAlert();
				if (demoAlert != null) {
					demoAlert = URLEncoder.encode(new String(Base64.encodeBase64(demoAlert.getBytes()), MiscUtils.DEFAULT_UTF8_ENCODING), "UTF-8");
				}
			}
			
			String proNo = request.getParameter("proNo");
			String provStr = "";
			if (proNo != null && !proNo.isEmpty()) {
				ProviderDao proDao = (ProviderDao)SpringUtils.getBean(ProviderDao.class);
				Provider provider = null;
				try {
					provider = proDao.getProvider(proNo);
				} catch (Exception e) {}
				if (provider != null) {
					provStr = provider.getProviderNo() + "|" + provider.getOhipNo();
				}
			}
			
			PreventionsBillingDao pbDao = (PreventionsBillingDao)SpringUtils.getBean(PreventionsBillingDao.class);
			PreventionsBilling pb = pbDao.getPreventionBillingByType(prevType);
			if (pb == null) {
				url.append(request.getContextPath()+"/billing.do?billRegion="+URLEncoder.encode(prov, "UTF-8")+"&billForm="
					+URLEncoder.encode(oscarVariables.getProperty("default_view"), "UTF-8")+"&hotclick=&appointment_no=0&demographic_name="
					+URLEncoder.encode(demo.getLastName(), "UTF-8")+"%2C"+URLEncoder.encode(demo.getFirstName(), "UTF-8")+"&demographic_no="
					+demo.getDemographicNo()+"&demoAlert="+demoAlert+"&providerview=none&xml_provider="+provStr
					+"&apptProvider_no=none&appointment_date="+dateString+"&start_time=0:00&bNewForm=1&status=t" + "&prevId="+prevId);
			} else {
				StringBuilder serviceCodeParams = new StringBuilder();
				if (!pb.getBillingServiceCodeAndUnit().isEmpty()) {
					String[] codes = pb.getBillingServiceCodeAndUnit().split(",");
					for (int i =0; i<codes.length; i++) {
						String[] codeUnit = codes[i].split("\\|");
						if (codeUnit.length < 2) {
							continue;
						}
						serviceCodeParams.append("&serviceCode"+i);
						serviceCodeParams.append("="+codeUnit[0]);
						serviceCodeParams.append("&serviceUnit"+i);
						serviceCodeParams.append("="+codeUnit[1]);
					}
				}
				
				url.append(request.getContextPath()+"/billing/CA/ON/billingON.jsp?bType="+URLEncoder.encode(pb.getBillingType().substring(0, pb.getBillingType().indexOf("|")).trim(), "UTF-8")
						+ "&&hotclick=&appointment_no=0&demographic_name=" + URLEncoder.encode(demo.getLastName(), "UTF-8")+"%2C"+URLEncoder.encode(demo.getFirstName(), "UTF-8")
						+ "&demographic_no="+demoNo+"&apptProvider_no=none&providerview=none&appointment_date="+dateString+"&status=t&start_time=0:00&bNewForm=1"
						+ serviceCodeParams.toString() +"&dxCode="+pb.getBillingDxCode()+"&dxCode1=&dxCode2=&referralCode=&referralSpet=&referralDocName=&xml_provider="
						+ provStr + "&xml_location=" + URLEncoder.encode(pb.getVisitLocation(), "UTF-8") + "&xml_slicode=" + URLEncoder.encode(pb.getSliCode(), "UTF-8")
						+ "&xml_visittype=" + URLEncoder.encode(pb.getVisitType(), "UTF-8") + "&xml_vdate=&cutlist=-%20SUPER%20CODES%20-&demoAlert=" + demoAlert + "&prevId="+prevId);
						
			}
		} while (false);
	}
}


%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>close</title>
<script LANGUAGE="JavaScript">

function popupPage(vheight,vwidth,varpage) { //open a new popup window
	var page = "" + varpage;
	windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=20,left=20";
	var popup=window.open(page, "demodetail", windowprops);
	if (popup != null) {
		if (popup.opener == null) {
			popup.opener = self;
		}
		popup.focus();
	}
}

function closeWin() {
	<% if (url != null) {%>
	popupPage(700, 1000, '<%=url.toString()%>');
	<%}%>
	
      if ( self.opener.refreshInfo){
         self.opener.refreshInfo();
         self.setTimeout('closeThisWindow()', 5000);
      }else{
         self.close();
         self.opener.location.reload();
      }
}
function closeThisWindow(){
   self.close();
}
</script>

</head>
<body onload="closeWin();">
Click to
<a href="javascript: function myFunction() {return false; }"
	onclick="window.close();">Close</a>
window
</body>
</html>
