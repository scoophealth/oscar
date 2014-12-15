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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@ page
	import="java.sql.*, java.util.*, oscar.SxmlMisc, oscar.oscarProvider.data.ProviderBillCenter"
	errorPage="errorpage.jsp"%>
<%@ page import="oscar.log.LogAction,oscar.log.LogConst"%>
<%@ page import="org.oscarehr.common.model.ClinicNbr"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="org.oscarehr.common.dao.ClinicNbrDao"%>
<%@ page import="org.oscarehr.common.model.ProviderData"%>
<%@ page import="org.oscarehr.common.dao.ProviderDataDao"%>
<%@page import="org.oscarehr.common.dao.UserPropertyDAO"%>
<%@page import="org.oscarehr.common.model.UserProperty,org.apache.commons.lang.StringUtils"%>
<%@ page import="oscar.OscarProperties"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />

<%
  java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);
  ProviderDataDao providerDao = SpringUtils.getBean(ProviderDataDao.class);
%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.oscarehr.common.model.Site"%>
<%@page import="oscar.login.*"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%><html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.providerupdateprovider.title" /></title>
<link rel="stylesheet" href="../web.css">
<script LANGUAGE="JavaScript">
<!--
function setfocus() {
  document.updatearecord.last_name.focus();
  document.updatearecord.last_name.select();
}
//-->
</script>
</head>

<%
    if(session.getAttribute("user") == null ) response.sendRedirect("../logout.jsp");
    String curProvider_no = (String) session.getAttribute("user");

    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	List<Integer> siteIDs = new ArrayList<Integer>();
    boolean isSiteAccessPrivacy=false;
%>

<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%
	isSiteAccessPrivacy=true;
	DBHelp dbObj = new DBHelp();
	String sqlString = "SELECT site_id from providersite where provider_no=" + curProvider_no;
	ResultSet siters = DBHelp.searchDBRecord(sqlString);

	while (siters.next()) {
		siteIDs.add(siters.getInt("site_id"));
	}

	siters.close();
	%>
</security:oscarSec>

<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th><font face="Helvetica" color="#FFFFFF"><bean:message
			key="admin.providerupdateprovider.description" /></font></th>
	</tr>
</table>

<form method="post" action="admincontrol.jsp" name="updatearecord">
<%
  ResultSet rs = apptMainBean.queryResults(request.getParameter("keyword"), request.getParameter("dboperation"));
  if(rs==null) {
    out.println("failed");
  } else {
	LogAction.addLog((String)session.getAttribute("user"), LogConst.UPDATE, "adminUpdateUser",
			request.getParameter("keyword"), request.getRemoteAddr());
    while (rs.next()) {
%>

<table cellspacing="0" cellpadding="2" width="100%" border="0"
	datasrc='#xml_list'>

	<tr>
		<td width="50%" align="right"><bean:message
			key="admin.provider.formProviderNo" />:</td>
		<td>
		<% String provider_no = apptMainBean.getString(rs,"provider_no"); %><%= provider_no %>
		<input type="hidden" name="provider_no" value="<%= provider_no %>">
		<input type="hidden" name="dboperation" value="provider_update_record"></td>
	</tr>
	<tr>
		<td>
		<div align="right"><bean:message
			key="admin.provider.formLastName" />:</div>
		</td>
		<td><input type="text" index="3" name="last_name"
			value="<%= apptMainBean.getString(rs,"last_name") %>" maxlength="30"></td>
	</tr>
	<tr>
		<td>
		<div align="right"><bean:message
			key="admin.provider.formFirstName" />:</div>
		</td>
		<td><input type="text" index="4" name="first_name"
			value="<%= apptMainBean.getString(rs,"first_name") %>" maxlength="30"></td>
	</tr>


<% if (org.oscarehr.common.IsPropertiesOn.isMultisitesEnable()) { %>
	<tr>
		<td>
		<div align="right"><bean:message key="admin.provider.sitesAssigned" /><font color="red">:</font></div>
		</td>
		<td>
<%
ProviderDao pDao = (ProviderDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("providerDao");

SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");
List<Site> psites = siteDao.getActiveSitesByProviderNo(provider_no);
List<Site> sites = siteDao.getAllActiveSites();
for (int i=0; i<sites.size(); i++) {
%>
	<input type="checkbox" name="sites" value="<%= sites.get(i).getSiteId() %>" <%= psites.contains(sites.get(i))?"checked='checked'":"" %> <%=((!isSiteAccessPrivacy) || siteIDs.contains(sites.get(i).getSiteId()) ? "" : " disabled ") %>>
	<%= sites.get(i).getName() %><br />
<%
}
%>
		</td>
	</tr>
<% } %>



	<tr>
		<td align="right"><bean:message key="admin.provider.formType" />:
		</td>
		<td>
			<select name="provider_type">
			<option value="receptionist"
				<% if (apptMainBean.getString(rs,"provider_type").equals("receptionist")) { %>
				SELECTED <%}%>><bean:message
				key="admin.provider.formType.optionReceptionist" /></option>
			<option value="doctor"
				<% if (apptMainBean.getString(rs,"provider_type").equals("doctor")) { %>
				SELECTED <%}%>><bean:message
				key="admin.provider.formType.optionDoctor" /></option>
			<option value="nurse"
				<% if (apptMainBean.getString(rs,"provider_type").equals("nurse")) { %>
				SELECTED <%}%>><bean:message
				key="admin.provider.formType.optionNurse" /></option>
			<option value="resident"
				<% if (apptMainBean.getString(rs,"provider_type").equals("resident")) { %>
				SELECTED <%}%>><bean:message
				key="admin.provider.formType.optionResident" /></option>
			<option value="midwife"
				<% if (apptMainBean.getString(rs,"provider_type").equals("midwife")) { %>
				SELECTED <%}%>><bean:message
				key="admin.provider.formType.optionMidwife" /></option>
			<option value="admin"
				<% if (apptMainBean.getString(rs,"provider_type").equals("admin")) { %>
				SELECTED <%}%>><bean:message
				key="admin.provider.formType.optionAdmin" /></option>
			<caisi:isModuleLoad moduleName="survey">
				<option value="er_clerk"
					<% if (apptMainBean.getString(rs,"provider_type").equals("er_clerk")) { %>
					SELECTED <%}%>><bean:message
					key="admin.provider.formType.optionErClerk" /></option>
			</caisi:isModuleLoad>
		</select> <!--input type="text" name="provider_type" value="<%= apptMainBean.getString(rs,"provider_type") %>" maxlength="15" -->
		</td>
	</tr>
	<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
		<tr>
			<td align="right"><bean:message
				key="admin.provider.formSpecialty" />:</td>
			<td><input type="text" name="specialty"
				value="<%= apptMainBean.getString(rs,"specialty") %>" maxlength="40"></td>
		</tr>
		<tr>
			<td align="right"><bean:message key="admin.provider.formTeam" />:
			</td>
			<td><input type="text" name="team"
				value="<%= apptMainBean.getString(rs,"team") %>" maxlength="20"></td>
		</tr>
		<tr>
			<td align="right"><bean:message key="admin.provider.formSex" />:
			</td>
			<td><input type="text" name="sex"
				value="<%= apptMainBean.getString(rs,"sex") %>" maxlength="1"></td>
		</tr>
		<tr>
			<td align="right"><bean:message key="admin.provider.formDOB" />:
			</td>
			<td><input type="text" name="dob"
				value="<%= oscar.MyDateFormat.getMyStandardDate(apptMainBean.getString(rs,"dob")) %>"
				maxlength="11"></td>
		</tr>
		<tr>
			<td align="right"><bean:message key="admin.provider.formAddress" />:
			</td>
			<td><input type="text" name="address"
				value="<%= apptMainBean.getString(rs,"address") %>" size="40"
				maxlength="40"></td>
		</tr>
		<tr>
			<td align="right"><bean:message
				key="admin.provider.formHomePhone" />:</td>
			<td><input type="text" name="phone"
				value="<%= apptMainBean.getString(rs,"phone") %>"></td>
		</tr>
		<tr>
			<td align="right"><bean:message
				key="admin.provider.formWorkPhone" />:</td>
			<td><input type="text" name="workphone"
				value="<%= apptMainBean.getString(rs,"work_phone") == null?"":(apptMainBean.getString(rs,"work_phone")) %>"
				maxlength="50"></td>
		</tr>
		<tr>
			<td align="right"><bean:message key="admin.provider.formEmail" />:</td>
			<td><input type="text" name="email"
				value="<%= apptMainBean.getString(rs,"email") == null?"":(apptMainBean.getString(rs,"email")) %>"
				maxlength="50"></td>
		</tr>
		<tr>
			<td align="right"><bean:message key="admin.provider.formPager" />:
			</td>
			<td><input type="text" name="xml_p_pager"
				value="<%= SxmlMisc.getXmlContent(apptMainBean.getString(rs,"comments"),"xml_p_pager")==null?"":SxmlMisc.getXmlContent(apptMainBean.getString(rs,"comments"),"xml_p_pager") %>"
				datafld='xml_p_pager'></td>
		</tr>
		<tr>
			<td align="right"><bean:message key="admin.provider.formCell" />:
			</td>
			<td><input type="text" name="xml_p_cell"
				value="<%= SxmlMisc.getXmlContent(apptMainBean.getString(rs,"comments"),"xml_p_cell")==null?"": SxmlMisc.getXmlContent(apptMainBean.getString(rs,"comments"),"xml_p_cell")%>"
				datafld='xml_p_cell'></td>
		</tr>
		<tr>
			<td align="right"><bean:message
				key="admin.provider.formOtherPhone" />:</td>
			<td><input type="text" name="xml_p_phone2"
				value="<%= SxmlMisc.getXmlContent(apptMainBean.getString(rs,"comments"),"xml_p_phone2")==null?"":SxmlMisc.getXmlContent(apptMainBean.getString(rs,"comments"),"xml_p_phone2") %>"
				datafld='xml_p_phone2'></td>
		</tr>
		<tr>
			<td align="right"><bean:message key="admin.provider.formFax" />:
			</td>
			<td><input type="text" name="xml_p_fax"
				value="<%= SxmlMisc.getXmlContent(apptMainBean.getString(rs,"comments"),"xml_p_fax")==null?"": SxmlMisc.getXmlContent(apptMainBean.getString(rs,"comments"),"xml_p_fax")%>"
				datafld='xml_p_fax'></td>
		</tr>
		<tr>
			<td align="right"><bean:message key="admin.provider.formOhipNo" />:
			</td>
			<td><input type="text" name="ohip_no"
				value="<%= apptMainBean.getString(rs,"ohip_no") %>" maxlength="20"></td>
		</tr>
		<tr>
			<td align="right"><bean:message key="admin.provider.formRmaNo" />:
			</td>
			<td><input type="text" name="rma_no"
				value="<%= apptMainBean.getString(rs,"rma_no") %>" maxlength="20"></td>
		</tr>
		<tr>
			<td align="right"><bean:message
				key="admin.provider.formBillingNo" />:</td>
			<td><input type="text" name="billing_no"
				value="<%= apptMainBean.getString(rs,"billing_no") %>"
				maxlength="20"></td>
		</tr>
		<tr>
			<td align="right"><bean:message key="admin.provider.formHsoNo" />:
			</td>
			<td><input type="text" name="hso_no"
				value="<%= apptMainBean.getString(rs,"hso_no") %>" maxlength="10"></td>
		</tr>
		<tr>
			<td align="right"><bean:message key="admin.provider.formStatus" />:
			</td>
			<td><input type="text" name="status"
				value="<%= apptMainBean.getString(rs,"status") %>" maxlength="1"></td>
		</tr>
		<tr>
			<td align="right"><bean:message
				key="admin.provider.formSpecialtyCode" />:</td>
			<td><input type="text" name="xml_p_specialty_code"
				value="<%= SxmlMisc.getXmlContent(apptMainBean.getString(rs,"comments"),"xml_p_specialty_code")==null?"":SxmlMisc.getXmlContent(apptMainBean.getString(rs,"comments"),"xml_p_specialty_code") %>"
				datafld='xml_p_specialty_code'></td>
		</tr>
		<tr>
			<td align="right"><bean:message
				key="admin.provider.formBillingGroupNo" />:</td>
			<td><input type="text" name="xml_p_billinggroup_no"
				value="<%= SxmlMisc.getXmlContent(apptMainBean.getString(rs,"comments"),"xml_p_billinggroup_no") %>"
				datafld='xml_p_billinggroup_no'></td>
		</tr>
		<tr>
			<td align="right"><bean:message key="admin.provider.formCPSID" />:
			</td>
			<td><input type="text" name="practitionerNo"
				value="<%= apptMainBean.getString(rs,"practitionerNo") %>"
				maxlength="10"></td>
		</tr>

		<%
		UserPropertyDAO userPropertyDAO = (UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
		%>
		<tr>
			<td align="right"><bean:message key="admin.provider.formOfficialFirstName" />:</td>
			<td><input type="text" name="officialFirstName" value="<%=StringUtils.trimToEmpty(userPropertyDAO.getStringValue(provider_no, UserProperty.OFFICIAL_FIRST_NAME))%>" maxlength="255"></td>
		</tr>
		<tr>
			<td align="right"><bean:message key="admin.provider.formOfficialSecondName" />:</td>
			<td><input type="text" name="officialSecondName" value="<%=StringUtils.trimToEmpty(userPropertyDAO.getStringValue(provider_no, UserProperty.OFFICIAL_SECOND_NAME))%>" maxlength="255"></td>
		</tr><tr>
			<td align="right"><bean:message key="admin.provider.formOfficialLastName" />:</td>
			<td><input type="text" name="officialLastName" value="<%=StringUtils.trimToEmpty(userPropertyDAO.getStringValue(provider_no, UserProperty.OFFICIAL_LAST_NAME))%>" maxlength="255"></td>
		</tr>


		<% if (OscarProperties.getInstance().getBooleanProperty("rma_enabled", "true")) { %>
			<tr>
				<td align="right">Default Clinic NBR:</td>
				<td colspan="3">
				<select name="xml_p_nbr">
				<%
				ClinicNbrDao clinicNbrDAO = (ClinicNbrDao)SpringUtils.getBean("clinicNbrDao");
				List<ClinicNbr> nbrList = clinicNbrDAO.findAll();
				Iterator<ClinicNbr> nbrIter = nbrList.iterator();
				while (nbrIter.hasNext()) {
					ClinicNbr tempNbr = nbrIter.next();
					String valueString = tempNbr.getNbrValue() + " | " + tempNbr.getNbrString();
				%>
					<option value="<%=tempNbr.getNbrValue()%>" <%=SxmlMisc.getXmlContent(rs.getString("comments"),"xml_p_nbr").startsWith(tempNbr.getNbrValue())?"selected":""%>><%=valueString%></option>
				<%}%>

				</select>
				</td>
			</tr>
		<%} %>
		<tr>
			<td align="right">Bill Center:</td>
			<td><select name="billcenter">
				<option value=""></option>
				<%
              ProviderBillCenter billCenter = new ProviderBillCenter();
              String billCode = "";
              String codeDesc = "";
              Enumeration<?> keys = billCenter.getAllBillCenter().propertyNames();
              String currentBillCode = billCenter.getBillCenter(provider_no);
              for(int i=0;i<billCenter.getAllBillCenter().size();i++){
                  billCode=(String)keys.nextElement();
                  codeDesc=billCenter.getAllBillCenter().getProperty(billCode);
              %>
				<option value=<%= billCode %>
					<%=currentBillCode.compareTo(billCode)==0?"selected":""%>><%= codeDesc%></option>
				<%
              }
              %>
			</select></td>
		</tr>

		<input type="hidden" name="provider_activity" value="">


	</caisi:isModuleLoad>
	<tr>
		<td align="right"><bean:message
			key="admin.provider.formSlpUsername" />:</td>
		<td><input type="text" name="xml_p_slpusername"
			value="<%= SxmlMisc.getXmlContent(apptMainBean.getString(rs,"comments"),"xml_p_slpusername")==null?"":SxmlMisc.getXmlContent(apptMainBean.getString(rs,"comments"),"xml_p_slpusername") %>"
			datafld='xml_p_slpusername'></td>
	</tr>
	<tr>
		<td align="right"><bean:message
			key="admin.provider.formSlpPassword" />:</td>
		<td><input type="text" name="xml_p_slppassword"
			value="<%= SxmlMisc.getXmlContent(apptMainBean.getString(rs,"comments"),"xml_p_slppassword")==null?"": SxmlMisc.getXmlContent(apptMainBean.getString(rs,"comments"),"xml_p_slppassword")%>"
			datafld='xml_p_slppassword'></td>
	</tr>
    <tr>
		<td align="right"><bean:message
			key="provider.login.title.confidentiality" />:</td>
		<td><input type="text" readonly name="signed_confidentiality"
			value="<%=apptMainBean.getString(rs,"signed_confidentiality")%>">
        </td>
	</tr>
	<tr>
		<td colspan="2">
		<div align="center"><input type="hidden" name="displaymode"
			value="Provider_Update_Record"> <input type="submit"
			name="subbutton"
			value="<bean:message key="admin.providerupdateprovider.btnSubmit"/>">
		</div>
		</td>
	</tr>

</table>
<%
  }}
%>
</form>

<p></p>
<%@ include file="footerhtm.jsp"%></center>
</body>
</html:html>
