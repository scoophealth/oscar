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
<%-- @ taglib uri="../WEB-INF/taglibs-log.tld" prefix="log" --%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    String demographic$ = request.getParameter("demographic_no") ;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic"
	rights="r" reverse="<%=true%>">
	<% response.sendRedirect("../noRights.html"); %>
</security:oscarSec>

<security:oscarSec roleName="<%=roleName$%>"
	objectName='<%="_demographic$"+demographic$%>' rights="o"
	reverse="<%=false%>">
You have no rights to access the data!
<% response.sendRedirect("../noRights.html"); %>
</security:oscarSec>

<%@ page
	import="java.util.*, java.sql.*, java.net.*,java.text.DecimalFormat, oscar.*, oscar.oscarDemographic.data.ProvinceNames, oscar.oscarWaitingList.WaitingList"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.ProfessionalSpecialist" %>
<%@page import="org.oscarehr.common.dao.ProfessionalSpecialistDao" %>
<%@page import="org.oscarehr.common.model.DemographicCust" %>
<%@page import="org.oscarehr.common.dao.DemographicCustDao" %>
<%
	ProfessionalSpecialistDao professionalSpecialistDao = (ProfessionalSpecialistDao) SpringUtils.getBean("professionalSpecialistDao");
	DemographicCustDao demographicCustDao = (DemographicCustDao)SpringUtils.getBean("demographicCustDao");
%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="providerBean" class="java.util.Properties"
	scope="session" />
<% java.util.Properties oscarVariables = OscarProperties.getInstance(); %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%
	if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");

	String curProvider_no = (String) session.getAttribute("user");
	String demographic_no = request.getParameter("demographic_no") ;
	String userfirstname = (String) session.getAttribute("userfirstname");
	String userlastname = (String) session.getAttribute("userlastname");
	String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
	String str = null;
	int nStrShowLen = 20;
        String prov= (oscarVariables.getProperty("billregion","")).trim().toUpperCase();

        OscarProperties oscarProps = OscarProperties.getInstance();

        ProvinceNames pNames = ProvinceNames.getInstance();

%>



<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="demographic.demographiceditdemographic.title" /></title>
<link rel="stylesheet" type="text/css"
	href="../oscarEncounter/encounterStyles.css">

</head>
<%
java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);
//----------------------------REFERRAL DOCTOR------------------------------
String rdohip="", rd="", fd="", family_doc = "";

String resident="", nurse="", alert="", notes="", midwife="";
ResultSet rs = null;

DemographicCust demographicCust = demographicCustDao.find(Integer.parseInt(demographic_no));
if(demographicCust != null) {
	resident = demographicCust.getResident();
	nurse = demographicCust.getNurse();
	alert = demographicCust.getAlert();
	midwife = demographicCust.getMidwife();
	notes = SxmlMisc.getXmlContent(demographicCust.getNotes(),"unotes") ;
	notes = notes==null?"":notes;
}

GregorianCalendar now=new GregorianCalendar();
int curYear = now.get(Calendar.YEAR);
int curMonth = (now.get(Calendar.MONTH)+1);
int curDay = now.get(Calendar.DAY_OF_MONTH);
String dateString = curYear+"-"+curMonth+"-"+curDay;
int age=0, dob_year=0, dob_month=0, dob_date=0;

int param = Integer.parseInt(demographic_no);

rs = apptMainBean.queryResults(param, request.getParameter("dboperation"));
if(rs==null) {
out.println("failed!!!");
} else {
while (rs.next()) {
        //----------------------------REFERRAL DOCTOR------------------------------
        fd=apptMainBean.getString(rs,"family_doctor");
        if (fd==null) {
                rd = "";
                rdohip="";
                family_doc = "";
        }else{
                rd = SxmlMisc.getXmlContent(apptMainBean.getString(rs,"family_doctor"),"rd")    ;
                rd = rd !=null ? rd : "" ;
                rdohip = SxmlMisc.getXmlContent(apptMainBean.getString(rs,"family_doctor"),"rdohip");
                rdohip = rdohip !=null ? rdohip : "" ;
                family_doc = SxmlMisc.getXmlContent(apptMainBean.getString(rs,"family_doctor"),"family_doc");
                family_doc = family_doc !=null ? family_doc : "" ;
        }
        //----------------------------REFERRAL DOCTOR --------------end-----------

        dob_year = Integer.parseInt(apptMainBean.getString(rs,"year_of_birth"));
        dob_month = Integer.parseInt(apptMainBean.getString(rs,"month_of_birth"));
        dob_date = Integer.parseInt(apptMainBean.getString(rs,"date_of_birth"));
        if(dob_year!=0) age=MyDateFormat.getAge(dob_year,dob_month,dob_date);
                    WaitingList wL = WaitingList.getInstance();
%>


<body onLoad="setfocus(); checkONReferralNo(); formatPhoneNum();"
	topmargin="0" leftmargin="0" rightmargin="0">
<form>
<table width="100%" class="MainTableLeftColumn">
	<tr>
		<td class="RowTop" colspan="3" align="center" bgcolor="#EEEEFF">
		<b>Record</b> (<%=apptMainBean.getString(rs,"demographic_no")%>) <%=apptMainBean.getString(rs,"last_name")%>,
		<%=apptMainBean.getString(rs,"first_name")%> <%=apptMainBean.getString(rs,"sex")%>
		<%=age%> years</td>
	</tr>
	<tr>
		<td align="left"
			title='<%=apptMainBean.getString(rs,"demographic_no")%>'><b><bean:message
			key="demographic.demographiceditdemographic.formLastName" />: </b><%=apptMainBean.getString(rs,"last_name")%></td>
		<td align="left"><b><bean:message
			key="demographic.demographiceditdemographic.formFirstName" />: </b></td>
		<td align="left"><%=apptMainBean.getString(rs,"first_name")%></td>
	</tr>

	<%
    if (vLocale.getCountry().equals("BR")) { %>
	<tr>
		<td align="left"><b><bean:message
			key="demographic.demographicaddrecordhtm.formRG" />:</b> <%=apptMainBean.getString(rs,"rg")==null?"":apptMainBean.getString(rs,"rg")%>
		</td>
		<td align="left"><b><bean:message
			key="demographic.demographicaddrecordhtm.formCPF" />:</b></td>
		<td align="left"><%=apptMainBean.getString(rs,"cpf")==null?"":apptMainBean.getString(rs,"cpf")%>
		</td>
	</tr>
	<tr>
		<td align="left"><b><bean:message
			key="demographic.demographicaddrecordhtm.formMaritalState" />:</b> <select
			name="marital_state">
			<option value="-">-</option>
			<option value="S"
				<%if (apptMainBean.getString(rs,"marital_state").trim().equals("S")){%>
				selected <%}%>><bean:message
				key="demographic.demographicaddrecordhtm.formMaritalState.optSingle" /></option>
			<option value="M"
				<%if (apptMainBean.getString(rs,"marital_state").trim().equals("M")){%>
				selected <%}%>><bean:message
				key="demographic.demographicaddrecordhtm.formMaritalState.optMarried" /></option>
			<option value="R"
				<%if (apptMainBean.getString(rs,"marital_state").trim().equals("R")){%>
				selected <%}%>><bean:message
				key="demographic.demographicaddrecordhtm.formMaritalState.optSeparated" /></option>
			<option value="D"
				<%if (apptMainBean.getString(rs,"marital_state").trim().equals("D")){%>
				selected <%}%>><bean:message
				key="demographic.demographicaddrecordhtm.formMaritalState.optDivorced" /></option>
			<option value="W"
				<%if (apptMainBean.getString(rs,"marital_state").trim().equals("W")){%>
				selected <%}%>><bean:message
				key="demographic.demographicaddrecordhtm.formMaritalState.optWidower" /></option>
		</select></td>
		<td align="left"><b><bean:message
			key="demographic.demographicaddrecordhtm.formBirthCertificate" />:</b></td>
		<td align="left"><%=apptMainBean.getString(rs,"birth_certificate")==null?"":apptMainBean.getString(rs,"birth_certificate")%>
		</td>
	</tr>
	<tr>
		<td align="left"><b><bean:message
			key="demographic.demographicaddrecordhtm.formMarriageCertificate" />:</b>
		<%=apptMainBean.getString(rs,"marriage_certificate")==null?"":apptMainBean.getString(rs,"marriage_certificate")%>
		</td>
		<td align="left"><b><bean:message
			key="demographic.demographicaddrecordhtm.formPartnerName" />:</b></td>
		<td align="left"><%=apptMainBean.getString(rs,"partner_name")==null?"":apptMainBean.getString(rs,"partner_name")%>
		</td>
	</tr>
	<tr>
		<td align="left"><b><bean:message
			key="demographic.demographicaddrecordhtm.formFatherName" />:</b> <%=apptMainBean.getString(rs,"father_name")==null?"":apptMainBean.getString(rs,"father_name")%>
		</td>
		<td align="left"><b><bean:message
			key="demographic.demographicaddrecordhtm.formMotherName" />:</b></td>
		<td align="left"><%=apptMainBean.getString(rs,"mother_name")==null?"":apptMainBean.getString(rs,"mother_name")%>
		</td>
	</tr>
	<%}%>
	<tr valign="top">
		<td align="left"><b><bean:message
			key="demographic.demographiceditdemographic.formAddr" />: </b> <%=apptMainBean.getString(rs,"address")%>
		<% if (vLocale.getCountry().equals("BR")) { %> <b><bean:message
			key="demographic.demographicaddrecordhtm.formAddressNo" />:</b> <%=apptMainBean.getString(rs,"address_no")==null?"":apptMainBean.getString(rs,"address_no")%>
		<%}%>
		</td>
		<td align="left"><b><bean:message
			key="demographic.demographiceditdemographic.formCity" />: </b></td>
		<td align="left"><%=apptMainBean.getString(rs,"city")%></td>
	</tr>
	<% if (vLocale.getCountry().equals("BR")) { %>
	<tr valign="top">
		<td align="left"><b><bean:message
			key="demographic.demographicaddrecordhtm.formComplementaryAddress" />:
		</b><%=apptMainBean.getString(rs,"complementary_address")==null?"":apptMainBean.getString(rs,"complementary_address")%></td>
		<td align="left"><b><bean:message
			key="demographic.demographicaddrecordhtm.formDistrict" />: </b></td>
		<td align="left"><%=apptMainBean.getString(rs,"district")==null?"":apptMainBean.getString(rs,"district")%></td>
	</tr>
	<%}%>
	<tr valign="top">
		<td align="left"><b><bean:message
			key="demographic.demographiceditdemographic.formProcvince" />: </b><%=apptMainBean.getString(rs,"province")%></td>
		<td align="left"><b><bean:message
			key="demographic.demographiceditdemographic.formPostal" />: </b></td>
		<td align="left"><%=apptMainBean.getString(rs,"postal")%></td>
	</tr>
	<tr valign="top">
		<td align="left"><b><bean:message
			key="demographic.demographiceditdemographic.formPhoneH" />: </b><%=apptMainBean.getString(rs,"phone")%></td>
		<td align="left"><b><bean:message
			key="demographic.demographiceditdemographic.formPhoneW" />:</b></td>
		<td align="left"><%=apptMainBean.getString(rs,"phone2")%></td>
	</tr>
	<tr valign="top">
		<td align="left"><b><bean:message
			key="demographic.demographiceditdemographic.formEmail" />: </b><%=apptMainBean.getString(rs,"email")!=null? apptMainBean.getString(rs,"email") : ""%></td>
		<td align="left"><b><bean:message
			key="demographic.demographiceditdemographic.formPIN" />: </b></td>
		<td align="left"><%=apptMainBean.getString(rs,"myOscarUserName")!=null? apptMainBean.getString(rs,"myOscarUserName") : ""%></td>
	</tr>
	<tr valign="top">
		<td align="left"><b><bean:message
			key="demographic.demographiceditdemographic.formDOB" /></b><bean:message
			key="demographic.demographiceditdemographic.formDOBDetais" /><b>:
		</b> <%=apptMainBean.getString(rs,"year_of_birth")%>/ <%=apptMainBean.getString(rs,"month_of_birth")%>/
		<%=apptMainBean.getString(rs,"date_of_birth")%> <b>Age: </b> <%=age%>
		</td>
		<td align="left" nowrap><b><bean:message
			key="demographic.demographiceditdemographic.formSex" />:</b></td>
		<td align="left"><%=apptMainBean.getString(rs,"sex")%></td>
	</tr>
	<tr valign="top">
		<td align="left"><b><bean:message
			key="demographic.demographiceditdemographic.formHin" />: </b><%=apptMainBean.getString(rs,"hin")%>
		<b><bean:message
			key="demographic.demographiceditdemographic.formVer" /></b> <%=apptMainBean.getString(rs,"ver")%>
		</td>
		<td align="left"><b><bean:message
			key="demographic.demographiceditdemographic.formEFFDate" />:</b></td>
		<td align="left">
		<%
             // Put 0 on the left on dates
             DecimalFormat decF = new DecimalFormat();
             // Year
             decF.applyPattern("0000");
             String effDateYear = decF.format(MyDateFormat.getYearFromStandardDate(apptMainBean.getString(rs,"eff_date")));
             
             if(!effDateYear.equals("0000")) {
            	 // Month and Day
             	decF.applyPattern("00");
             	String effDateMonth = decF.format(MyDateFormat.getMonthFromStandardDate(apptMainBean.getString(rs,"eff_date")));
             	String effDateDay = decF.format(MyDateFormat.getDayFromStandardDate(apptMainBean.getString(rs,"eff_date")));
        	 %>
         		<%= effDateYear%>/ <%= effDateMonth%>/ <%= effDateDay%></td>
         	<%}%>
	</tr>
	<tr valign="top">
		<td align="left"><b><bean:message
			key="demographic.demographiceditdemographic.formHCType" />:</b> <% String hctype = apptMainBean.getString(rs,"hc_type")==null?"":apptMainBean.getString(rs,"hc_type"); %>
		<%=hctype%></td>
		<td></td>
		<td></td>
	</tr>
	<tr valign="top">
		<td align="left" nowrap><b><bean:message
			key="demographic.demographiceditdemographic.formDoctor" />: </b> <%
      ResultSet rsdemo = apptMainBean.queryResults("search_provider_doc");
      while (rsdemo.next()) {
        if ( rsdemo.getString("provider_no").equals(apptMainBean.getString(rs,"provider_no")) ) {%>
		<%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",nStrShowLen)%>
		<%  }
      } %>
		</td>
		<td align="left" nowrap><b><bean:message
			key="demographic.demographiceditdemographic.formNurse" />: </b></td>
		<td align="left">
		<%
      rsdemo.beforeFirst();
      while (rsdemo.next()) {
          if ( rsdemo.getString("provider_no").equals(resident) )  {  %>
		<%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",nStrShowLen)%>
		<% }  } %>
		</td>
	</tr>
	<tr valign="top">
		<td align="left" nowrap><b><bean:message
			key="demographic.demographiceditdemographic.formMidwife" />: </b> <%
      rsdemo.beforeFirst();
      while (rsdemo.next()) {
          if ( rsdemo.getString("provider_no").equals(midwife) ) {                         %>
		<%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",nStrShowLen)%>
		<%    }
      } %>
		</td>
		<td align="left"><b><bean:message
			key="demographic.demographiceditdemographic.formResident" />:</b></td>
		<td align="left">
		<%
      rsdemo.beforeFirst();
      while (rsdemo.next()) {
          if ( rsdemo.getString("provider_no").equals(nurse) ) {
    %> <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",nStrShowLen)%>
		<% } } %>
		</td>
	</tr>

	<tr valign="top">
		<td align="left" nowrap><b><bean:message
			key="demographic.demographiceditdemographic.formRefDoc" />: </b> <% if(oscarProps.getProperty("isMRefDocSelectList", "").equals("true") ) {
                            // drop down list
                                                      Properties prop = null;
                                                      Vector vecRef = new Vector();

                                                      List<ProfessionalSpecialist> specialists = professionalSpecialistDao.findAll();
                                                      for(ProfessionalSpecialist specialist : specialists) {
                                                    	  if (specialist != null && specialist.getReferralNo() != null && ! specialist.getReferralNo().equals("")) {
	                                                    	  prop = new Properties();
	                                                          prop.setProperty("referral_no", specialist.getReferralNo());
	                                                          prop.setProperty("last_name", specialist.getLastName());
	                                                          prop.setProperty("first_name", specialist.getFirstName());
	                                                          vecRef.add(prop);
                                                    	  }
                                                      }

              %> <select name="r_doctor" onChange="changeRefDoc()"
			style="width: 200px">
			<option value=""></option>
			<% for(int k=0; k<vecRef.size(); k++) {
                            prop= (Properties) vecRef.get(k);
                    %>
			<option
				value="<%=prop.getProperty("last_name")+","+prop.getProperty("first_name")%>"
				<%=prop.getProperty("referral_no").equals(rdohip)?"selected":""%>>
			<%=Misc.getShortStr( (prop.getProperty("last_name")+","+prop.getProperty("first_name")),"",nStrShowLen)%></option>
			<% } %>
		</select> <script language="Javascript">
    <!--
    function changeRefDoc() {
    //alert(document.updatedelete.r_doctor.value);
    var refName = document.updatedelete.r_doctor.options[document.updatedelete.r_doctor.selectedIndex].value;
    var refNo = "";
            <% for(int k=0; k<vecRef.size(); k++) {
                    prop= (Properties) vecRef.get(k);
            %>
    if(refName=="<%=prop.getProperty("last_name")+","+prop.getProperty("first_name")%>") {
      refNo = '<%=prop.getProperty("referral_no", "")%>';
    }
    <% } %>
    document.updatedelete.r_doctor_ohip.value = refNo;
    }
    //-->
    </script> <% } else {%> <%=rd%> <% } %>
		</td>
		<td align="left" nowrap><b><bean:message
			key="demographic.demographiceditdemographic.formRefDocNo" />: </b></td>
		<td align="left"><%=rdohip%></td>
	</tr>

	<tr valign="top">
		<td align="left" nowrap><b><bean:message
			key="demographic.demographiceditdemographic.formRosterStatus" />: </b> <%String rosterStatus = apptMainBean.getString(rs,"roster_status");
              if (rosterStatus == null) {
                 rosterStatus = "";
              }
              %> <%=rosterStatus%></td>
		<td align="left" nowrap><b><bean:message
			key="demographic.demographiceditdemographic.DateJoined" />: </b></td>
		<td align="left">
		<%
             // Format year
             decF.applyPattern("0000");
             String hcRenewYear = decF.format(MyDateFormat.getYearFromStandardDate(apptMainBean.getString(rs,"hc_renew_date")));
          
          	if(!hcRenewYear.equals("0000")) {
            	 // Month and Day
             	decF.applyPattern("00");
             	String hcRenewMonth = decF.format(MyDateFormat.getMonthFromStandardDate(apptMainBean.getString(rs,"hc_renew_date")));
             	String hcRenewDay = decF.format(MyDateFormat.getDayFromStandardDate(apptMainBean.getString(rs,"hc_renew_date")));
        	 %>
        	 <%= hcRenewYear %>/ <%= hcRenewMonth %>/ <%= hcRenewDay %></td>
         	<%}%>
	</tr>
	<tr valign="top">
		<td align="left"><b><bean:message
			key="demographic.demographiceditdemographic.formPatientStatus" />:</b> <% String pacStatus = apptMainBean.getString(rs,"patient_status"); %>
		<% if (vLocale.getCountry().equals("BR")) { %> <%=pacStatus%> <% } else {
               boolean nextStatus = true;
               ResultSet rsstatus = apptMainBean.queryResults("search_ptstatus");
                 while (rsstatus.next()) {
                    if ( pacStatus.equals(rsstatus.getString("patient_status")) ) { %>
		<%=rsstatus.getString("patient_status")%> <% nextStatus = false;
                    }
                } // end while
           %> <% if ( nextStatus )  {

                %> <%=pacStatus%> <% } %> <% } // end if...then...else %>
		</td>
		<td align="left"><b><bean:message
			key="demographic.demographiceditdemographic.formChartNo" />:</b></td>
		<td align="left"><%=apptMainBean.getString(rs,"chart_no")%></td>
	</tr>
	<%
        if (vLocale.getCountry().equals("BR")) { %>
	<tr valign="top">
		<td align="left"></td>
		<td align="left"><b><bean:message
			key="demographic.demographicaddrecordhtm.formChartAddress" />: </b></td>
		<td align="left"><%=apptMainBean.getString(rs,"chart_address")==null?"":apptMainBean.getString(rs,"chart_address")%>
		</td>
	</tr>
	<%}%>
	<%if(wL.getFound()){%>
	<tr valign="top">
		<td align="left" nowrap><b>Waiting List: </b> <%
            ResultSet rsWLStatus = apptMainBean.queryResults(demographic_no,"search_wlstatus");
            String listID = "", wlnote="";
            if (rsWLStatus.next()){
                listID = rsWLStatus.getString("listID");
                wlnote = rsWLStatus.getString("note");
            }
           %> <%  ResultSet rsWL = apptMainBean.queryResults("search_waiting_list");
                  while (rsWL.next()) { %> <% if ( rsWL.getString("ID").equals(listID) ) { %>
		<%=rsWL.getString("name")%> <% }
                }
              %>
		</td>
		<td align="left" nowrap><b>Waiting List Note: </b></td>
		<td align="left"><%=wlnote%></td>
	</tr>
	<%}%>
	<tr valign="top">
		<td align="left" nowrap><b><bean:message
			key="demographic.demographiceditdemographic.formDateJoined1" />: </b> <%
             // Format year
             decF.applyPattern("0000");
             String dateJoinedYear = decF.format(MyDateFormat.getYearFromStandardDate(apptMainBean.getString(rs,"date_joined")));
             decF.applyPattern("00");
             String dateJoinedMonth = decF.format(MyDateFormat.getMonthFromStandardDate(apptMainBean.getString(rs,"date_joined")));
             String dateJoinedDay = decF.format(MyDateFormat.getDayFromStandardDate(apptMainBean.getString(rs,"date_joined")));
          %> <%= dateJoinedYear %> <%= dateJoinedMonth %> <%= dateJoinedDay %>
		</td>
		<td align="left"><b><bean:message
			key="demographic.demographiceditdemographic.formEndDate" />: </b></td>
		<td align="left">
		<%
             // Format year
             decF.applyPattern("0000");
             String endYear = decF.format(MyDateFormat.getYearFromStandardDate(apptMainBean.getString(rs,"end_date")));
             if(!endYear.equals("0000")) {
            	 // Month and Day
             	decF.applyPattern("00");
             	String endMonth = decF.format(MyDateFormat.getMonthFromStandardDate(apptMainBean.getString(rs,"end_date")));
             	String endDay = decF.format(MyDateFormat.getDayFromStandardDate(apptMainBean.getString(rs,"end_date")));
        	 %>
        	 <%= endYear %>/ <%= endMonth %>/ <%= endDay %></td>
         	<%}%>
             
	<tr valign="top">
		<td nowrap colspan="3">
		<table width="100%" bgcolor="#EEEEFF">
			<tr>
				<td width="7%" align="left"><font color="#FF0000"><b><bean:message
					key="demographic.demographiceditdemographic.formAlert" />: </b></font></td>
				<td><%=alert%></td>
			</tr>
			<tr>
				<td align="left"><b><bean:message
					key="demographic.demographiceditdemographic.formNotes" />: </b></td>
				<td><%=notes%></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>


<%
    }
  }
%>

</body>
</html:html>
