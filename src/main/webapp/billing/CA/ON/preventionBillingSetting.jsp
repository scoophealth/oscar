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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*"%>
<%@page import="org.oscarehr.common.dao.DemographicDao, org.oscarehr.common.model.Demographic" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.util.LocaleUtils"%>
<%@page import="org.oscarehr.util.WebUtils, java.util.Collections"%>
<%@page import="org.oscarehr.billing.CA.ON.dao.PreventionsBillingDao" %>
<%@page import="org.oscarehr.common.model.PreventionsBilling" %>
<%@page import="org.oscarehr.common.dao.*" %>
<%@page import="org.oscarehr.common.model.*" %>
<%@page import="oscar.oscarBilling.ca.on.data.*" %>
<%@page import="org.oscarehr.util.LoggedInInfo" %>
<%
String ctx = request.getContextPath();
LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request.getSession());
PreventionDisplayConfig pdc = PreventionDisplayConfig.getInstance(loggedInInfo);
ArrayList<HashMap<String,String>> prevList = pdc.getPreventions(loggedInInfo);

%>


<!DOCTYPE html>
<html>
<head lang="en">

	<title>Prevention Billing Setting</title>
	
	<!-- Latest compiled and minified CSS -->
	<!--
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css"> 
	 -->
	
	<!-- Optional theme -->
	<!-- 
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">
	 -->
	

</head>
<body style="padding: 20px;">
	<form name="prevForm" id="prevForm" action="<%=ctx %>/billing/CA/ON/preventionBilling" method="post" >
	<input type="hidden" name="method" value="savePreventionBilling">
	<table class="table">
		<thead>
			<tr>
				<th><bean:message key="oscarEncounter.LeftNavBar.Prevent" /> </th>
				<th><bean:message key="admin.admin.prevention.title.billingServiceCodes" /></th>
				<th><bean:message key="billing.billingCorrection.msgBillingType"/></th>
				<th><bean:message key="report.reportdaysheet.msgDxCode"/></th>
				<th><bean:message key="billing.billingCorrection.formVisitType"/></th> 
				<th><bean:message key="billing.billingCorrection.msgVisitLocation"/></th>
				<th><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode"/></th>
			</tr>
		</thead>
		
		<tbody>
			<%
			PreventionsBillingDao pbDao = (PreventionsBillingDao)SpringUtils.getBean(PreventionsBillingDao.class);
			String clinicNo = oscar.OscarProperties.getInstance().getProperty("clinic_no", "").trim();
			for (int i=0;i<prevList.size();i++) {
				String prvType = prevList.get(i).get("name");
				if (prvType == null) {
					continue;
				}
				String servcieCodes = "", billingType = "", dxCode = "", visitType = "", visitLoc = "", sliCode = "";
				PreventionsBilling pb = pbDao.getPreventionBillingByType(prvType);
				if (pb != null) {
					servcieCodes = pb.getBillingServiceCodeAndUnit();
					billingType = pb.getBillingType();
					dxCode = pb.getBillingDxCode();
					visitType = pb.getVisitType();
					visitLoc = pb.getVisitLocation();
					sliCode = pb.getSliCode();
				}
				
			%>
			<tr>
				<td><%=prevList.get(i).get("name") %><input type="hidden" name="prevType_<%=i%>" value="<%=prevList.get(i).get("name")%>"></td>
				<td>
					<input type="hidden" name="oldserviceCodes_<%=i%>" maxlength="50" value="<%=servcieCodes%>">
					<input type="text" name="serviceCodes_<%=i%>" maxlength="50" value="<%=servcieCodes%>" onblur="checkServiceCode(this);">
				</td>
				
				<!-- billing type -->
				<td>
					<input type="hidden" name="oldbillingType_<%=i%>" value="<%=billingType %>">
					<select name="billingType_<%=i%>">
						<option value="ODP | Bill OHIP" <%=billingType.startsWith("ODP")?"selected" : ""%>>Bill OHIP</option>
					    <option value="WCB | Worker's Compensation Board" <%=billingType.startsWith("WCB")?"selected" : ""%>>WSIB</option>
					    <option value="NOT | Do Not Bill" <%=billingType.startsWith("NOT")?"selected" : ""%>>Do Not Bill</option>
					    <option value="IFH | Interm Federal Health" <%=billingType.startsWith("IFH")?"selected" : ""%>>IFH</option>
					    <option value="PAT | Bill Patient" <%=billingType.startsWith("PAT")?"selected" : ""%>>3rd Party</option>
					    <option value="OCF | " <%=billingType.startsWith("OCF")?"selected" : ""%>> -OCF</option>
					    <option value="ODS | " <%=billingType.startsWith("ODS")?"selected" : ""%>> -ODSP</option>
					    <option value="CPP | Canada Pension Plan" <%=billingType.startsWith("CPP")?"selected" : ""%>> -CPP</option>
					    <option value="STD | Short Term Disability / Long Term Disability" <%=billingType.startsWith("STD")?"selected" : ""%>>-STD/LTD</option>
                        <option value="BON | Bonus Codes" <%=billingType.startsWith("BON")?"selected" : ""%>>Bonus Codes</option>
					</select>
				</td>
				
				<!-- dx code -->
				<td>
					<input type="hidden" name="olddxCode_<%=i %>" value="<%=dxCode %>">
					<input type="text" name="dxCode_<%=i %>" value="<%=dxCode%>" maxlength="5">
				</td>
				
				<!-- visit type -->
				<td>
					<input type="hidden" name="oldvisitType_<%=i %>" value="<%=visitType%>">
					<select name="visitType_<%=i %>">
						<% if (oscar.OscarProperties.getInstance().getBooleanProperty("rma_enabled", "true")) { %>
						<%
						ClinicNbrDao cnDao = (ClinicNbrDao) SpringUtils.getBean("clinicNbrDao");
						ArrayList<ClinicNbr> nbrs = cnDao.findAll();
	                    for (ClinicNbr clinic : nbrs) {
							String valueString = String.format("%s | %s", clinic.getNbrValue(), clinic.getNbrString());
							%>
					    	<option value="<%=valueString%>" <%=visitType.startsWith(clinic.getNbrValue())?"selected":""%>><%=valueString%></option>
					    <%}%>
					    <% } else { %>
					    <option value="00" <%=visitType.startsWith("00")?"selected":""%>>00 | Clinic Visit</option>
					    <option value="01" <%=visitType.startsWith("01")?"selected":""%>>01 | Outpatient Visit</option>
					    <option value="02" <%=visitType.startsWith("02")?"selected":""%>>02 | Hospital Visit</option>
					    <option value="03" <%=visitType.startsWith("03")?"selected":""%>>03 | ER</option>
					    <option value="04" <%=visitType.startsWith("04")?"selected":""%>>04 | Nursing Home</option>
					    <option value="05" <%=visitType.startsWith("05")?"selected":""%>>05 | Home Visit</option>
					    <% } %>
					</select>
				
				</td>
				
				<!-- visit location -->
				<td>
					<input type="hidden" name="oldvisitLoc_<%=i%>" value="<%=visitLoc %>">
					<select name="visitLoc_<%=i%>">
					<%
					JdbcBillingPageUtil tdbObj = new JdbcBillingPageUtil();
					String billLocationNo="", billLocation="";
					List lLocation = tdbObj.getFacilty_num();
				    for (int j = 0; j < lLocation.size(); j = j + 2) {
				    	billLocationNo = (String) lLocation.get(j);
						billLocation = (String) lLocation.get(j + 1);%>
						<option value="<%=billLocationNo + "|" + billLocation%>" 
						<%=visitLoc.startsWith(billLocationNo)?"selected":""%>>
						<%=billLocation%>
						</option>
				    <%
				    }
					%>
					</select>
				</td>
				
				<!-- sli code -->
				<td>
					<input type="hidden" name="oldsliCode_<%=i %>" value="<%=sliCode %>">
					<select name="sliCode_<%=i %>">
						<option value="<%=clinicNo%>"><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.NA" /></option>
						<option value="HDS "  <%=sliCode.trim().startsWith("HDS")?"selected":""%>><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HDS" /></option>
						<option value="HED "  <%=sliCode.trim().startsWith("HED")?"selected":""%>><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HED" /></option>
						<option value="HIP " <%=sliCode.trim().startsWith("HIP")?"selected":""%>><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HIP" /></option>
						<option value="HOP " <%=sliCode.trim().startsWith("HOP")?"selected":""%>><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HOP" /></option>
						<option value="HRP " <%=sliCode.trim().startsWith("HRP")?"selected":""%>><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.HRP" /></option>
						<option value="IHF " <%=sliCode.trim().startsWith("IHF")?"selected":""%>><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.IHF" /></option>
						<option value="OFF " <%=sliCode.trim().startsWith("OFF")?"selected":""%>><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.OFF" /></option>
						<option value="OTN " <%=sliCode.trim().startsWith("OTN")?"selected":""%>><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.OTN" /></option>
						<option value="PDF " <%=sliCode.trim().startsWith("PDF")?"selected":""%>><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.PDF" /></option>
						<option value="RTF " <%=sliCode.trim().startsWith("RTF")?"selected":""%>><bean:message key="oscar.billing.CA.ON.billingON.OB.SLIcode.RTF" /></option>
					</select>
				</td>
			</tr>
			<%} %>
			
		</tbody>
	</table>
	<br>
	<div style="text-align:center; width: 50%; float: right;">
	  <input type="button" onclick="savePreventionBilling();" value="Save" name="saveBtn">
	  &nbsp;&nbsp;&nbsp;
	  <input type="button" onclick="window.close();" value="Cancel" name="cancelBtn">
	</div>
	<br>
	</form>
	
	<script type="text/javascript" src="<%=ctx%>/js/jquery-1.9.1.min.js"></script>
	<!-- Latest compiled and minified JavaScript -->
	<!-- 
	script <src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
	 -->
	
	<script type="text/javascript">
		function savePreventionBilling() {
			jQuery.ajax({
				url: "<%=ctx%>/billing/CA/ON/preventionBilling.do",
				type: "POST",
				data: jQuery("#prevForm").serialize(),
				dataType: "json",
				timeout: 5000,
				success: function(txt) {
					if (txt == null) {
						alert("Failed to save prevention billing, please try again!");
					}
					if (txt.ret) {
						alert("<bean:message key="admin.admin.successSavePrevention"/>");
						window.location.reload();
					}
				},
				error: function() {
					alert("Failed to save prevention billing, please try again!");
				}
			});
		}
	
		function checkServiceCode(which) {
			
			if (which.value.length == 0) {
				return;
			}
			
			// code validation
			jQuery.ajax({
				url: "<%=ctx%>/billing/CA/ON/preventionBilling.do",
				type: "POST",
				async: false,
				timeout: 3000,
				data: {codes:jQuery(which).val(),method:"validateServiceCodes"},
				dataType: "json",
				success: function(txt) {
					if (txt == null) {
						alert("Failed to validate servcie codes!");
						//jQuery(which).focus();
						setTimeout(function() {
							jQuery(which).focus();
						}, 0);
						return;
					}
					if (!txt.ret) {
						//alert("There're some service codes not exist, please check!");
						alert(txt.msg);
						setTimeout(function() {
							jQuery(which).focus();
						}, 0);
					}
				},
				error: function() {
					alert("Failed to validate servcie codes!");
					setTimeout(function() {
						jQuery(which).focus();
					}, 0);
				}
			});
		}
		
	</script>
</body>
</html>