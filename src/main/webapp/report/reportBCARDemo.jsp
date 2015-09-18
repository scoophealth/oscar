<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="java.util.*, oscar.oscarDemographic.data.ProvinceNames"
	errorPage="errorpage.jsp"%>
<% java.util.Properties oscarVariables = oscar.OscarProperties.getInstance(); %>
<% 
  ProvinceNames pNames = ProvinceNames.getInstance();
  String prov= ((String ) oscarVariables.getProperty("billregion","")).trim().toUpperCase();

  String billingCentre = ((String ) oscarVariables.getProperty("billcenter","")).trim().toUpperCase();
%>

<html lang="en">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="oscarReport.oscarReportscpbDemo.title" /></title>
<LINK REL="StyleSheet" HREF="../web.css" TYPE="text/css">
<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />
<!-- main calendar program -->
<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript"
	src="../share/calendar/lang/calendar-en.js"></script>
<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
<script language="JavaScript">

		<!--
		function setfocus() {
		  this.focus();
		  //document.forms[0].service_code.focus();
		}
	    function onAdd() {
			if(document.baseurl.name.value.length < 2) {
				alert("Please type in a valid name!");
				return false;
			} else {
	        	return true;
	        }
	    }

	    function goPage(id) {
			self.location.href = "reportFilter.jsp?id=" + id;
	    }
		//-->

            
            function OnSubmitForm(){
                var cForm = document.getElementById("clientform");
                var newAR = document.getElementById("bcartype");
                //console.log("dd" +cForm.action + " "+newAR.checked);
                if(newAR.checked == true){
                    cForm.action ="reportBCARDemo3.jsp";
                }else{
                    cForm.action ="reportBCARDemo2.jsp";
                }
                return true;
            }

      </script>
</head>
<body bgcolor="ivory" onLoad="setfocus()" topmargin="0" leftmargin="0"
	rightmargin="0">
<center>
<form method="post" id="clientform" name="baseurl"
	action="reportBCARDemo2.jsp">
<table BORDER="1" CELLPADDING="0" CELLSPACING="0" WIDTH="80%">
	<tr BGCOLOR="#CCFFFF">
		<th><bean:message key="oscarReport.oscarReportscpbDemo.msgHeader"/></th>
	</tr>
</table>
</center>
<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr bgcolor="silver">
		<td><input type="hidden" name="demoReport" value="1"> <input
			type="submit" name="submit" value="<bean:message key="oscarReport.oscarReportscpbDemo.btnHTML"/>"> | <input
			type="submit" name="submit" value="<bean:message key="oscarReport.oscarReportscpbDemo.btnCSV"/>"> BCAR: <input
			type="radio" name="bcartype" value="BCAR" /> BCAR2007: <input
			type="radio" id="bcartype" checked name="bcartype" value="BCAR2007" />
</table>

<table width="100%" border="0" cellspacing="1" cellpadding="0">
	<tr>
		<td>

		<table width="100%" border="0" cellspacing="2" cellpadding="0">
			<tr bgcolor="#EEdddd">
				<td colspan="2" align="center"><bean:message key="oscarReport.oscarReportscpbDemo.msgColHeader" /></td>
			</tr>
			<tr bgcolor="#EEEEFF">
				<td align="right" width="10%"><b><input type="checkbox"
					name="last_name"></b></td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgLastName"/></td>
			</tr>
			<tr bgcolor="#EEEEFF">
				<td align="right"><b><input type="checkbox"
					name="first_name"></b></td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgFirstName"/></td>
			</tr>
			<tr bgcolor="#EEEEFF">
				<td align="right"><b><input type="checkbox"
					name="date_joined"></b></td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgDateJoined"/></td>
			</tr>
			<tr bgcolor="#EEEEFF">
				<td align="right"><b><input type="checkbox" name="hin"></b>
				</td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgPHN"/></td>
			</tr>
			<tr bgcolor="#EEEEFF">
				<td align="right"><b><input type="checkbox" name="hc_type"></b>
				</td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgProvince"/></td>
			</tr>
			<tr bgcolor="#EEEEFF">
				<td align="right"><b><input type="checkbox" name="address"></b>
				</td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgAddress"/></td>
			</tr>
			<tr bgcolor="#EEEEFF">
				<td align="right"><b><input type="checkbox" name="city"></b>
				</td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgCity"/></td>
			</tr>
			<tr bgcolor="#EEEEFF">
				<td align="right"><b><input type="checkbox" name="postal"></b>
				</td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgPostalCode"/></td>
			</tr>
			<tr bgcolor="#EEEEFF">
				<td align="right"><b><input type="checkbox" name="phone"></b>
				</td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgHPhone"/></td>
			</tr>
			<tr bgcolor="#EEEEFF">
				<td align="right"><b><input type="checkbox" name="phone2"></b>
				</td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgWPhone"/></td>
			</tr>
			<tr bgcolor="#EEEEFF">
				<td align="right"><b><input type="checkbox" name="email"></b>
				</td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgEmail"/></td>
			</tr>
			<tr bgcolor="#EEEEFF">
				<td align="right"><b><input type="checkbox"
					name="prefer_language"></b></td>
				<td nowrap><bean:message key="oscarReport.oscarReportscpbDemo.msgPrefLang"/></td>
			</tr>

			<% if(oscarVariables.getProperty("demographicExt") != null) {
	String [] propDemoExt = oscarVariables.getProperty("demographicExt","").split("\\|");
	for(int i=0; i<propDemoExt.length; i++) {%>
			<tr bgcolor="#EEEEFF">
				<td align="right"><b><input type="checkbox"
					name="<%=propDemoExt[i].replace(' ', '_') %>"></b></td>
				<td nowrap><%=propDemoExt[i] %></td>
			</tr>
			<%} }%>

			<tr bgcolor="#EEEEFF">
				<td align="right"><b><input type="checkbox" name="c_EDD"></b>
				</td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgEDD"/></td>
			</tr>
			<!--  tr bgcolor="#EEEEFF">
            <td align="right">
              <b><input type="checkbox" name="ga" ></b>
            </td>
            <td>
              GA Today
            </td>
          </tr-->
			<tr bgcolor="#EEEEFF">
				<td align="right"><b><input type="checkbox"
					name="pg1_famPhy"></b></td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgFamPhys"/></td>
			</tr>
			<tr bgcolor="#EEEEFF">
				<td align="right"><b><input type="checkbox"
					name="pg1_partnerName"></b></td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgPartner"/></td>
			</tr>
		</table>

		</td>
		<td valign="top"><!-- right hand -->
		<table width="100%" border="0" cellspacing="2" cellpadding="2">
			<tr bgcolor="#EEdddd">
				<td colspan="3" align="center"><bean:message key="oscarReport.oscarReportscpbDemo.msgLimit"/></td>
			</tr>
			<tr bgcolor="#EEEEFF">
				<td align="right" width="6%"><b><input type="checkbox"
					name="filter_1" checked></b></td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgActiveClient"/></td>
				<td width="1%" align="right"><input type="hidden"
					name="value_1" value="demographic.patient_status='AC'"> <input
					type="hidden" name="position_1" value="sql"> <input
					type="hidden" name="dateFormat_1" value=""></td>
			</tr>

			<tr bgcolor="">
				<td align="right"><b><input type="checkbox" name="filter_2"></b>
				</td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgInActiveClient"/></td>
				<td align="right"><input type="hidden" name="value_2"
					value="demographic.patient_status='IN'"> <input
					type="hidden" name="position_2" value="sql"> <input
					type="hidden" name="dateFormat_2" value=""></td>
			</tr>

			<tr bgcolor="#EEEEFF">
				<td align="right"><b><input type="checkbox" name="filter_3"></b>
				</td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgDeliveredClient"/></td>
				<td align="right"><input type="hidden" name="value_3"
					value="formBCAR.demographic_no in (select distinct demographic_no from formBCBirthSumMo)">
				<input type="hidden" name="position_3" value="sql"> <input
					type="hidden" name="dateFormat_3" value=""></td>
			</tr>

			<tr bgcolor="">
				<td align="right"><b><input type="checkbox" name="filter_4"></b>
				</td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgDOB"/> <input type="text" name="startDate4"
					id="startDate4" value="" size="10" readonly><img
					src="../images/cal.gif" id="startDate4_cal"> <bean:message key="oscarReport.oscarReportscpbDemo.msgAnd"/> <input
					type="text" name="endDate4" id="endDate4" value="" size="10"
					readonly><img src="../images/cal.gif" id="endDate4_cal">
				</td>
				<td align="right"><input type="hidden" name="value_4"
					value="(concat(demographic.year_of_birth,'-',demographic.month_of_birth,'-',demographic.date_of_birth)) between '${startDate4}' and '${endDate4}'">
				<input type="hidden" name="position_4" value="sql"> <input
					type="hidden" name="dateFormat_4" value="dd/MM/yyyy"></td>
			</tr>

			<tr bgcolor="#EEEEFF">
				<td align="right"><b><input type="checkbox" name="filter_8"></b>
				</td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgEDDbetween"/> <input type="text" name="startDate8"
					id="startDate8" value="" size="10" readonly><img
					src="../images/cal.gif" id="startDate8_cal"> <bean:message key="oscarReport.oscarReportscpbDemo.msgAnd"/> <input
					type="text" name="endDate8" id="endDate8" value="" size="10"
					readonly><img src="../images/cal.gif" id="endDate8_cal">
				</td>
				<td align="right"><input type="hidden" name="value_8"
					value="formBCAR.c_EDD between '${startDate8}' and '${endDate8}'">
				<input type="hidden" name="position_8" value="sql"> <input
					type="hidden" name="dateFormat_8" value="dd/MM/yyyy"></td>
			</tr>

			<tr bgcolor="">
				<td align="right"><b><input type="checkbox"
					name="filter_12"></b></td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgIndyClientSearch"/>: <font size="-1"><bean:message key="oscarReport.oscarReportscpbDemo.msgLNameBracket"/>
				 <input type="text" name="pLastName12" value="" size="15">,
				<bean:message key="oscarReport.oscarReportscpbDemo.msgFNameBracket"/> <input type="text" name="pFirstName12" value=""
					size="15"></font></td>
				<td align="right"><input type="hidden" name="value_12"
					value="demographic.last_name like '${pLastName12}%' and demographic.first_name like '${pFirstName12}%'">
				<input type="hidden" name="position_12" value="sql"> <input
					type="hidden" name="dateFormat_12" value=""></td>
			</tr>

			<tr bgcolor="#EEEEFF">
				<td align="right"><b><input type="checkbox"
					name="filter_14"></b></td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgPrimiparous"/></td>
				<td align="right"><input type="hidden" name="value_14"
					value="formBCAR.pg1_term =0 and formBCAR.pg1_preterm =0"> <input
					type="hidden" name="position_14" value="sql"> <input
					type="hidden" name="dateFormat_14" value=""></td>
			</tr>

			<tr bgcolor="">
				<td align="right"><b><input type="checkbox"
					name="filter_15"></b></td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgMultiparous"/></td>
				<td align="right"><input type="hidden" name="value_15"
					value="(formBCAR.pg1_term >0 or formBCAR.pg1_preterm >0)">
				<input type="hidden" name="position_15" value="sql"> <input
					type="hidden" name="dateFormat_15" value=""></td>
			</tr>

			<tr bgcolor="#EEEEFF">
				<td align="right"><b><input type="checkbox"
					name="filter_16"></b></td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgEither"/></td>
				<td align="right"><input type="hidden" name="value_16"
					value="formBCAR.pg1_gravida >0"> <input type="hidden"
					name="position_16" value="sql"> <input type="hidden"
					name="dateFormat_16" value=""></td>
			</tr>

			<tr bgcolor="">
				<td align="right"><b><input type="checkbox"
					name="filter_100"></b></td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgApproxEDD"/> <input type="text"
					name="startDate100" id="startDate100" value="" size="10" readonly><img
					src="../images/cal.gif" id="startDate100_cal"> <bean:message key="oscarReport.oscarReportscpbDemo.msgAnd"/> <input
					type="text" name="endDate100" id="endDate100" value="" size="10"
					readonly><img src="../images/cal.gif" id="endDate100_cal">
				</td>
				<td align="right"><input type="hidden" name="value_100"
					value="demographicExt.key_val='Approximate_EDD' and demographicExt.value >= '${startDate100}' and demographicExt.value <= '${endDate100}'">
				<input type="hidden" name="position_100" value="sql"> <input
					type="hidden" name="dateFormat_100" value="dd/MM/yyyy"></td>
			</tr>

			<% if(oscarVariables.getProperty("demographicExt") != null) {
    boolean bExtForm = oscarVariables.getProperty("demographicExtForm") != null ? true : false;
    String [] propDemoExtForm = bExtForm ? (oscarVariables.getProperty("demographicExtForm","").split("\\|") ) : null;
	String [] propDemoExt = oscarVariables.getProperty("demographicExt","").split("\\|");
	for(int i=1; i<propDemoExt.length; i++) {%>

			<tr bgcolor="<%=i%2==0?"":"#EEEEFF" %>">
				<td align="right"><b><input type="checkbox"
					name="filter_<%=100+i %>"></b></td>
				<td>
				<% if(bExtForm) {  %> <%=propDemoExt[i] %><%=propDemoExtForm[i].replaceAll("name=\""+propDemoExt[i].replace(' ', '_')+"\"", "name=\"pSpec"+(100+i)+"\"") %>
				<% } else { %> <%=propDemoExt[i] %> <input type="text"
					name="pSpec<%=100+i %>" value="" size="15"> <% }  %>
				</td>
				<td align="right"><input type="hidden" name="value_<%=100+i %>"
					value="demographicExt.key_val='<%=propDemoExt[i].replace(' ', '_') %>' and demographicExt.value like '${pSpec<%=100+i %>}%' ">
				<input type="hidden" name="position_<%=100+i %>" value="sql">
				<input type="hidden" name="dateFormat_<%=100+i %>" value="">
				</td>
			</tr>
			<%} }%>

			<tr bgcolor="">
				<td align="right"><b><input type="checkbox"
					name="filter_200"></b></td>
				<td><bean:message key="oscarReport.oscarReportscpbDemo.msgProvince"/> <select name="province">
					<option value="OT"
						<%=prov.equals("") || prov.equals("OT")?" selected":""%>><bean:message key="global.other"/></option>
					<% if (pNames.isDefined()) {
	                   for (ListIterator li = pNames.listIterator(); li.hasNext(); ) {
	                       String province = (String) li.next(); %>
					<option value="<%=province%>"
						<%=province.equals(prov)?" selected":""%>><%=li.next()%></option>
					<% } %>
					<% } else { %>
					<option value="AB" <%=prov.equals("AB")?" selected":""%>><bean:message key="global.Alberta"/></option>
					<option value="BC" <%=prov.equals("BC")?" selected":""%>><bean:message key="global.BC"/>
					</option>
					<option value="MB" <%=prov.equals("MB")?" selected":""%>><bean:message key="global.Manitoba"/></option>
					<option value="NB" <%=prov.equals("NB")?" selected":""%>><bean:message key="global.NewBrun"/>
					</option>
					<option value="NL" <%=prov.equals("NL")?" selected":""%>><bean:message key="global.Nflnd"/>
					</option>
					<option value="NT" <%=prov.equals("NT")?" selected":""%>><bean:message key="global.NWTerr"/>
					</option>
					<option value="NS" <%=prov.equals("NS")?" selected":""%>><bean:message key="global.Nova"/>
					</option>
					<option value="NU" <%=prov.equals("NU")?" selected":""%>><bean:message key="global.Nunavut"/></option>
					<option value="ON" <%=prov.equals("ON")?" selected":""%>><bean:message key="global.Ontario"/></option>
					<option value="PE" <%=prov.equals("PE")?" selected":""%>><bean:message key="global.PEI"/>
					</option>
					<option value="QC" <%=prov.equals("QC")?" selected":""%>><bean:message key="global.Quebec"/></option>
					<option value="SK" <%=prov.equals("SK")?" selected":""%>><bean:message key="global.Sask"/></option>
					<option value="YT" <%=prov.equals("YT")?" selected":""%>><bean:message key="global.Yukon"/></option>
					<option value="US" <%=prov.equals("US")?" selected":""%>><bean:message key="global.US"/>
					</option>
					<% } %>
				</select></td>
				<td align="right"><input type="hidden" name="value_200"
					value="demographic.hc_type like '${province}'"> <input
					type="hidden" name="position_200" value="sql"> <input
					type="hidden" name="dateFormat_200" value=""></td>
			</tr>

		</table>


		</td>
	</tr>
</table>


<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr bgcolor="silver">
		<td><input type="hidden" name="id" value="1"> <input
			type="submit" name="submit" value="<bean:message key="oscarReport.oscarReportscpbDemo.btnHTML"/>"> | <input
			type="submit" name="submit" value="<bean:message key="oscarReport.oscarReportscpbDemo.btnCSV"/>"></td>
		</form>
</table>


</body>
<script type="text/javascript">
Calendar.setup({ inputField : "startDate4", ifFormat : "%d/%m/%Y", showsTime :false, button : "startDate4_cal", singleClick : true, step : 1 }); 
Calendar.setup({ inputField : "endDate4", ifFormat : "%d/%m/%Y", showsTime :false, button : "endDate4_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "startDate8", ifFormat : "%d/%m/%Y", showsTime :false, button : "startDate8_cal", singleClick : true, step : 1 }); 
Calendar.setup({ inputField : "endDate8", ifFormat : "%d/%m/%Y", showsTime :false, button : "endDate8_cal", singleClick : true, step : 1 });

Calendar.setup({ inputField : "startDate100", ifFormat : "%Y/%m/%d", showsTime :false, button : "startDate100_cal", singleClick : true, step : 1 }); 
Calendar.setup({ inputField : "endDate100", ifFormat : "%Y/%m/%d", showsTime :false, button : "endDate100_cal", singleClick : true, step : 1 });
      </script>
</html>
