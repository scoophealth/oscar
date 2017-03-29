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

<%@ include file="/taglibs.jsp"%>
<%@page import="java.util.List" %>
<%@page import="org.oscarehr.eyeform.web.MacroAction" %>
<%@page import="org.apache.struts.util.LabelValueBean" %>
<%@page import="org.oscarehr.common.dao.ClinicLocationDao" %>
<%@page import="org.oscarehr.common.model.ClinicLocation" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%
	List<LabelValueBean> sliCodes = MacroAction.sliCodeList;
	request.setAttribute("sliCodes", sliCodes);
    ClinicLocationDao clinicLocationDao = SpringUtils.getBean(ClinicLocationDao.class);
    List<ClinicLocation> locations = clinicLocationDao.findByClinicNo(1); //default clinic no. is 1.
    
%>

<html lang="en">
<head>
    <title>Eyeform Macro Details</title>


<link rel="stylesheet" href="css/displaytag.css" type="text/css">
<style type="text/css">
.boldRow {
	color:red;
}
.commonRow{
	color:black;
}
span.h5 {
  margin-top: 1px;
  border-bottom: 1px solid #000;
  width: 90%;
  font-weight: bold;
  list-style-type: none;
  padding: 2px 2px 2px 2px;
  color: black;
  background-color: #69c;
  font-family: Trebuchet MS, Lucida Sans Unicode, Arial, sans-serif;
  font-size: 10pt;
  text-decoration: none;
  display: block;
  clear: both;
  white-space: nowrap;

}
</style>
<script type="text/javascript">
function selectAllCheckBox(){
	var form=document.inputForm;
	for (i=0;i<form.elements.length;i++){
		if (form.elements[i].name.indexOf("cbox")==0){
			form.elements[i].checked=true;
			form.elements[i].value="1";
		}
	}
}
function clearAllCheckBox(){
	var form=document.inputForm;
	for (i=0;i<form.elements.length;i++){
		if (form.elements[i].name.indexOf("cbox")==0){
			form.elements[i].checked=false;
			form.elements[i].value="0";
		}
	}
}
function turnCheckBox(el){
	if (el.checked==true) {
		el.value="1";
	}else {
		el.value="0";
	}
}

function fixCheckboxes(form) {
	var cbs = form.getElementsByTagName("input");
	form.followupFlags.value = '';
	for (var i=0; i<cbs.length; i++) {
		if (cbs[i].type=="checkbox" && cbs[i].name.indexOf("Flag")>0 && cbs[i].checked) {
			form.followupFlags.value += cbs[i].name + "|";
		}
	}
}
</script>

</head><body>

<html:form action="/eyeform/Macro.do" onsubmit="fixCheckboxes(this); return true;">
	<input name="method" value="save" type="hidden">
	<table style="border: 0px none;">
	<html:hidden property="macro.id"/>
	<tbody><tr>
	<td>
	<input value="Save" type="submit">
	<input value="Cancel" onclick="this.form.method.value='list'" type="submit">
	<input value="Close" onclick="window.opener.location.reload();window.close();return false;" type="button">
	</td></tr>
	</tbody></table>

	<c:if test="${not empty errors }">
		<span style="color:red;"><c:out value="${errors}" escapeXml="false"/></span>
	</c:if>

<fieldset>
	<legend>Macro Details</legend>
	<table style="border: 0px none;">

	<tbody><tr>
	<td width="150">Label</td>
	<td><html:text property="macro.label"/></td>
	</tr>
	<tr>
	<td>Display Order</td>
	<td><html:text property="macro.displayOrder" /></td>
	</tr>

	</tbody></table>

	<fieldset>
	<legend>Impression &amp; Followup</legend>
	<table style="border: 0px none;">
	<tbody><tr>
	<td width="150">Impression Text<br> <font size="-1">(to be appended)</font></td>

	<td><html:textarea property="macro.impression" cols="40" rows="10"></html:textarea></td>
	</tr>
	<tr>
	<td>Followup in</td>
	<td nowrap="nowrap"><html:text property="macro.followupNo" style="width: 40px;"/>
             <html:select property="macro.followupUnit" style="width: 80px;">
             	<html:option value="days">days</html:option>
            	<html:option value="weeks">weeks</html:option>
	            <html:option value="months">months</html:option>
	         </html:select>
    &nbsp;
    with doctor
    <html:select property="macro.followupDoctorId">
    	<html:options collection="providers" property="providerNo" labelProperty="formattedName" />
	</html:select>
	</td>
	</tr>
	<tr><td></td>

	<td nowrap="nowrap">

	<html:checkbox property="macro.statFlag" value="statFlag" /> STAT/PRN
	<html:checkbox property="macro.optFlag" value="optFlag" /> Optom Routine
	<html:checkbox property="macro.dischargeFlag" value="dischargeFlag"/> Discharge
	</td></tr>
	<tr>
	<td>Book Tests</td>

	<td><html:textarea property="macro.testRecords" cols="40" rows="4"></html:textarea></td>
	<td><font size="-1">Add a test booking per line with the following format:<br>
	&lt;test_name&gt;|&lt;OU,OD,OS&gt;|&lt;routine,ASAP,urgent&gt;|&lt;comment&gt;<br>
	e.g. OCT disc|OU|routine|book for Fridays</font></td>

	</tr>
	<tr><td>send tickler to </td><td>
			<html:select property="macro.ticklerRecipient">
				<html:option value="">Nobody</html:option>
				<html:options collection="providers" property="providerNo" labelProperty="formattedName" />
			</html:select>
    </td></tr>
	</tbody></table>
	</fieldset>


	<fieldset>
	<legend>Billing</legend>
	<table style="border: 0px none;">

	<tbody>
	<tr>
		<td>Billing Physician
		</td>
		<td>
			<html:select property="macro.billingBillto">
				<html:option value=""> -- </html:option>
				<html:options collection="providers" property="providerNo" labelProperty="formattedName" />
			</html:select>
		</td>	
	</tr>
	<tr>
		<td>
		SLI Code<br/>(Required for some billing codes)
		</td>
		<td>
			<html:select property="macro.sliCode">
				<html:options collection="sliCodes" property="value" labelProperty="label"/>
			</html:select>
		</td>
	</tr>
	<tr>
	<td>Billing Codes<br>(won't bill if empty)<br></td>
	<td><html:textarea property="macro.billingCodes" rows="4"></html:textarea></td>
	<td><font size="-1">Add a billing code per line in the following format:
	<br>&lt;unit_code&gt;|&lt;unit_count&gt;|&lt;sli_code&gt;
	<br>e.g. A001A|2|NA</font></td>

	</tr>
	<tr>
	<td>DX Code<br></td>
	<td><html:text property="macro.billingDxcode" /></td>
	</tr>
	<tr><td width="150">Visit Type</td>
	<td>
					<html:select property="macro.billingVisitType" styleId="xml_visittype">						
					    <html:option value="00| Clinic Visit">00 | Clinic Visit</html:option>
					    <html:option value="01| Outpatient Visit">01 | Outpatient Visit</html:option>
					    <html:option value="02| Hospital Visit">02 | Hospital Visit</html:option>
					    <html:option value="03| ER">03 | ER</html:option>
					    <html:option value="04| Nursing Home">04 | Nursing Home</html:option>
					    <html:option value="05| Home Visit">05 | Home Visit</html:option>

					</html:select>
					<script>document.getElementById("xml_visittype").value=''</script>
	</td></tr>
	<tr><td>Visit Location</td>
	<td>
			<html:select property="macro.billingVisitLocation">
			<%
				for(ClinicLocation fl : locations) {					
					String visitLocation = fl.getClinicLocationNo() + "|" + fl.getClinicLocationName();
			%>
			<html:option value="<%=visitLocation%>" ><%=fl.getClinicLocationName()%></html:option>
			<%} %>
			
			</html:select>

	</td></tr>
	<tr>
	<td>Billing Type<br></td>
	<td>
					<html:select property="macro.billingBilltype">
						<html:option value="ODP | Bill OHIP">Bill OHIP</html:option>
					    <html:option value="WCB | Worker's Compensation Board">WSIB</html:option>
					    <!--
					    <option value="NOT | Do Not Bill" >Do Not Bill</option>
					    <option value="IFH | Interm Federal Health" >IFH</option>
					    <option value="PAT | Bill Patient" >3rd Party</option>
					    <option value="OCF | " > -OCF</option>
					    <option value="ODS | "> -ODSP</option>
					    <option value="CPP | Canada Pension Plan" > -CPP</option>
					    <option value="STD | Short Term Disability / Long Term Disability" >-STD/LTD</option>
					    -->

                        <html:option value="BON | Bonus Codes">Bonus Codes</html:option>
                      </html:select>
	</td>
	</tr>
	<tr>
	<td>Comment<br></td>
	<td><html:textarea property="macro.billingComment"></html:textarea></td>
	</tr>

	</tbody></table>
	</fieldset>


</fieldset>
</html:form>
</body></html>
