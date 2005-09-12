<%-- @ taglib uri="../WEB-INF/taglibs-log.tld" prefix="log" --%>
<%--
/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster Unviersity
 * Hamilton
 * Ontario, Canada
 */
--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    String demographic$ = request.getParameter("demographic_no") ;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="r" reverse="<%=true%>" >
<% response.sendRedirect("../noRights.html"); %>
</security:oscarSec>

<security:oscarSec roleName="<%=roleName$%>" objectName="<%="_demographic$"+demographic$%>" rights="o" reverse="<%=false%>" >
You have no rights to access the data!
<% response.sendRedirect("../noRights.html"); %>
</security:oscarSec>

<%@ page import="java.util.*, java.sql.*, java.net.*,java.text.DecimalFormat, oscar.*, oscar.oscarDemographic.data.ProvinceNames, oscar.oscarWaitingList.WaitingList" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%
	if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");

	String curProvider_no = (String) session.getAttribute("user");
	String demographic_no = request.getParameter("demographic_no") ;
	String userfirstname = (String) session.getAttribute("userfirstname");
	String userlastname = (String) session.getAttribute("userlastname");
	String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
	String str = null;
	int nStrShowLen = 20;
        String prov= ((String ) oscarVariables.getProperty("billregion","")).trim().toUpperCase();

        OscarProperties oscarProps = OscarProperties.getInstance();

        ProvinceNames pNames = ProvinceNames.getInstance();

%>



<html:html locale="true">
<head>
<title><bean:message key="demographic.demographiceditdemographic.title"/></title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">



</head>
<%
java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);
//----------------------------REFERRAL DOCTOR------------------------------
String rdohip="", rd="", fd="", family_doc = "";

String resident="", nurse="", alert="", notes="", midwife="";
ResultSet rs = null;
rs = apptMainBean.queryResults(demographic_no, "search_demographiccust");
while (rs.next()) {
resident = rs.getString("cust1");
nurse = rs.getString("cust2");
alert = rs.getString("cust3");
midwife = rs.getString("cust4");
notes = SxmlMisc.getXmlContent(rs.getString("content"),"unotes") ;
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
        fd=rs.getString("family_doctor");
        if (fd==null) {
                rd = "";
                rdohip="";
                family_doc = "";
        }else{
                rd = SxmlMisc.getXmlContent(rs.getString("family_doctor"),"rd")    ;
                rd = rd !=null ? rd : "" ;
                rdohip = SxmlMisc.getXmlContent(rs.getString("family_doctor"),"rdohip");
                rdohip = rdohip !=null ? rdohip : "" ;
                family_doc = SxmlMisc.getXmlContent(rs.getString("family_doctor"),"family_doc");
                family_doc = family_doc !=null ? family_doc : "" ;
        }
        //----------------------------REFERRAL DOCTOR --------------end-----------

        dob_year = Integer.parseInt(rs.getString("year_of_birth"));
        dob_month = Integer.parseInt(rs.getString("month_of_birth"));
        dob_date = Integer.parseInt(rs.getString("date_of_birth"));
        if(dob_year!=0) age=MyDateFormat.getAge(dob_year,dob_month,dob_date);
                    WaitingList wL = WaitingList.getInstance();                       
%>
 

<body onLoad="setfocus(); checkONReferralNo(); formatPhoneNum();" topmargin="0" leftmargin="0" rightmargin="0">

 <table width="100%" class="MainTableLeftColumn">
 <form> 
    <tr>
        <td class="RowTop" colspan="3" align="center"  bgcolor="#EEEEFF">
            <b>Record</b> (<%=rs.getString("demographic_no")%>)  <%=rs.getString("last_name")%>, <%=rs.getString("first_name")%> <%=rs.getString("sex")%> <%=age%> years
        </td>
    </tr>                                        
    <tr>
        <td align="left" title='<%=rs.getString("demographic_no")%>'> <b><bean:message key="demographic.demographiceditdemographic.formLastName"/>: </b><%=rs.getString("last_name")%></td>
        <td align="left"><b><bean:message key="demographic.demographiceditdemographic.formFirstName"/>: </b> </td>
        <td align="left"><%=rs.getString("first_name")%></td>
    </tr>

    <%
    if (vLocale.getCountry().equals("BR")) { %>
    <tr>
      <td align="left"> <b><bean:message key="demographic.demographicaddrecordhtm.formRG"/>:</b>
        <%=rs.getString("rg")==null?"":rs.getString("rg")%>
      </td>
      <td align="left"><b><bean:message key="demographic.demographicaddrecordhtm.formCPF"/>:</b> </td>
      <td align="left">
        <%=rs.getString("cpf")==null?"":rs.getString("cpf")%>
      </td>
    </tr>
    <tr>
      <td align="left"> <b><bean:message key="demographic.demographicaddrecordhtm.formMaritalState"/>:</b>
        <select name="marital_state">
            <option value="-">-</option>
                <option value="S" <%if (rs.getString("marital_state").trim().equals("S")){%> selected <%}%>><bean:message key="demographic.demographicaddrecordhtm.formMaritalState.optSingle"/></option>
                <option value="M" <%if (rs.getString("marital_state").trim().equals("M")){%> selected <%}%>><bean:message key="demographic.demographicaddrecordhtm.formMaritalState.optMarried"/></option>
                <option value="R" <%if (rs.getString("marital_state").trim().equals("R")){%> selected <%}%>><bean:message key="demographic.demographicaddrecordhtm.formMaritalState.optSeparated"/></option>
                <option value="D" <%if (rs.getString("marital_state").trim().equals("D")){%> selected <%}%>><bean:message key="demographic.demographicaddrecordhtm.formMaritalState.optDivorced"/></option>
                <option value="W" <%if (rs.getString("marital_state").trim().equals("W")){%> selected <%}%>><bean:message key="demographic.demographicaddrecordhtm.formMaritalState.optWidower"/></option>
        </select>
      </td>
      <td align="left"><b><bean:message key="demographic.demographicaddrecordhtm.formBirthCertificate"/>:</b> </td>
      <td align="left">
        <%=rs.getString("birth_certificate")==null?"":rs.getString("birth_certificate")%>
      </td>
    </tr>
    <tr>
      <td align="left"> <b><bean:message key="demographic.demographicaddrecordhtm.formMarriageCertificate"/>:</b>
        <%=rs.getString("marriage_certificate")==null?"":rs.getString("marriage_certificate")%>
      </td>
      <td align="left"><b><bean:message key="demographic.demographicaddrecordhtm.formPartnerName"/>:</b> </td>
      <td align="left">
        <%=rs.getString("partner_name")==null?"":rs.getString("partner_name")%>
      </td>
    </tr>
    <tr>
      <td align="left"> <b><bean:message key="demographic.demographicaddrecordhtm.formFatherName"/>:</b>
        <%=rs.getString("father_name")==null?"":rs.getString("father_name")%>
      </td>
      <td align="left"><b><bean:message key="demographic.demographicaddrecordhtm.formMotherName"/>:</b> </td>
      <td align="left">
        <%=rs.getString("mother_name")==null?"":rs.getString("mother_name")%>
      </td>
    </tr>
    <%}%>
    <tr valign="top">
          <td  align="left"> <b><bean:message key="demographic.demographiceditdemographic.formAddr"/>: </b> <%=rs.getString("address")%>
            <% if (vLocale.getCountry().equals("BR")) { %>
                <b><bean:message key="demographic.demographicaddrecordhtm.formAddressNo"/>:</b>
                <%=rs.getString("address_no")==null?"":rs.getString("address_no")%>
            <%}%>
          </td>
          <td align="left"><b><bean:message key="demographic.demographiceditdemographic.formCity"/>: </b></td>
          <td align="left"><%=rs.getString("city")%></td>
    </tr>
    <% if (vLocale.getCountry().equals("BR")) { %>
    <tr valign="top">
          <td align="left"><b><bean:message key="demographic.demographicaddrecordhtm.formComplementaryAddress"/>: </b><%=rs.getString("complementary_address")==null?"":rs.getString("complementary_address")%></td>
          <td  align="left"><b><bean:message key="demographic.demographicaddrecordhtm.formDistrict"/>: </b> </td>
          <td  align="left"><%=rs.getString("district")==null?"":rs.getString("district")%></td>
    </tr>
    <%}%>
        <tr valign="top">
          <td align="left"><b><bean:message key="demographic.demographiceditdemographic.formProcvince"/>: </b><%=rs.getString("province")%></td>
          <td  align="left"><b><bean:message key="demographic.demographiceditdemographic.formPostal"/>: </b> </td>
          <td  align="left"><%=rs.getString("postal")%></td>
        </tr>
        <tr valign="top">
          <td  align="left"><b><bean:message key="demographic.demographiceditdemographic.formPhoneH"/>: </b><%=rs.getString("phone")%></td>
          <td  align="left"><b><bean:message key="demographic.demographiceditdemographic.formPhoneW"/>:</b> </td>
          <td  align="left"><%=rs.getString("phone2")%></td>
        </tr>
        <tr valign="top">
          <td align="left"><b><bean:message key="demographic.demographiceditdemographic.formEmail"/>: </b><%=rs.getString("email")!=null? rs.getString("email") : ""%></td>
          <td  align="left"><b><bean:message key="demographic.demographiceditdemographic.formPIN"/>: </b> </td>
          <td  align="left"><%=rs.getString("pin")!=null? rs.getString("pin") : ""%></td>
        </tr>
        <tr valign="top">
          <td  align="left"><b><bean:message key="demographic.demographiceditdemographic.formDOB"/></b><bean:message key="demographic.demographiceditdemographic.formDOBDetais"/><b>: </b>
            <%=rs.getString("year_of_birth")%>/
            <%=rs.getString("month_of_birth")%>/
            <%=rs.getString("date_of_birth")%>
            <b>Age:  </b> <%=age%>
          </td>
          <td  align="left" nowrap><b><bean:message key="demographic.demographiceditdemographic.formSex"/>:</b> </td>
          <td align="left"><%=rs.getString("sex")%></td>
        </tr>
        <tr valign="top">
          <td align="left"><b><bean:message key="demographic.demographiceditdemographic.formHin"/>: </b><%=rs.getString("hin")%>
                                <b><bean:message key="demographic.demographiceditdemographic.formVer"/></b>
                                <%=rs.getString("ver")%>
          </td>
          <td align="left"><b><bean:message key="demographic.demographiceditdemographic.formEFFDate"/>:</b></td>
          <td align="left">
          <%
             // Put 0 on the left on dates
             DecimalFormat decF = new DecimalFormat();
             // Year
             decF.applyPattern("0000");
             String effDateYear = decF.format(MyDateFormat.getYearFromStandardDate(rs.getString("eff_date")));
             // Month and Day
             decF.applyPattern("00");
             String effDateMonth = decF.format(MyDateFormat.getMonthFromStandardDate(rs.getString("eff_date")));
             String effDateDay = decF.format(MyDateFormat.getDayFromStandardDate(rs.getString("eff_date")));
          %>
            <%= effDateYear%>/
            <%= effDateMonth%>/
            <%= effDateDay%>
          </td>
        </tr>
        <tr valign="top">
          <td align="left"><b><bean:message key="demographic.demographiceditdemographic.formHCType"/>:</b>
               <% String hctype = rs.getString("hc_type")==null?"":rs.getString("hc_type"); %>
               <%=hctype%>
          </td>
          <td></td>
          <td></td>
        </tr>
        <tr valign="top">
          <td align="left" nowrap><b><bean:message key="demographic.demographiceditdemographic.formDoctor"/>: </b>
    <%
      ResultSet rsdemo = apptMainBean.queryResults("search_provider_doc");
      while (rsdemo.next()) {
        if ( rsdemo.getString("provider_no").equals(rs.getString("provider_no")) ) {%>
            <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",nStrShowLen)%>
    <%  }
      } %>
          </td>
          <td align="left" nowrap><b><bean:message key="demographic.demographiceditdemographic.formNurse"/>: </b> </td>
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
          <td align="left" nowrap><b><bean:message key="demographic.demographiceditdemographic.formMidwife"/>: </b>
    <%
      rsdemo.beforeFirst();
      while (rsdemo.next()) {
          if ( rsdemo.getString("provider_no").equals(midwife) ) {                         %>                          
                <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",nStrShowLen)%>
    <%    } 
      } %>
          </td>
          <td align="left"><b><bean:message key="demographic.demographiceditdemographic.formResident"/>:</b> </td>
          <td align="left">
    <%
      rsdemo.beforeFirst();
      while (rsdemo.next()) {
          if ( rsdemo.getString("provider_no").equals(nurse) ) {
    %>
      <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",nStrShowLen)%>
    <% } } %>
          </td>
        </tr>

        <tr valign="top">
              <td align="left" nowrap><b><bean:message key="demographic.demographiceditdemographic.formRefDoc"/>: </b>
              <% if(oscarProps.getProperty("isMRefDocSelectList", "").equals("true") ) {
                            // drop down list
                                                      String	sql   = "select * from billingreferral order by last_name, first_name" ;
                                                      oscar.oscarBilling.ca.on.data.BillingONDataHelp dbObj = new oscar.oscarBilling.ca.on.data.BillingONDataHelp();
                                                      ResultSet rs1 = dbObj.searchDBRecord(sql);
                                                            // System.out.println(sql);
                                                      Properties prop = null;
                                                      Vector vecRef = new Vector();
                                                      while (rs1.next()) {
                                                            prop = new Properties();
                                                            prop.setProperty("referral_no",rs1.getString("referral_no"));
                                                            prop.setProperty("last_name",rs1.getString("last_name"));
                                                            prop.setProperty("first_name",rs1.getString("first_name"));
                                                            //prop.setProperty("specialty",rs1.getString("specialty"));
                                                            //prop.setProperty("phone",rs1.getString("phone"));
                                                            vecRef.add(prop);
                  }
              %>
                    <select name="r_doctor" onChange="changeRefDoc()" style="width:200px">
                    <option value="" ></option>
                    <% for(int k=0; k<vecRef.size(); k++) {
                            prop= (Properties) vecRef.get(k);
                    %>
      <option value="<%=prop.getProperty("last_name")+","+prop.getProperty("first_name")%>" <%=prop.getProperty("referral_no").equals(rdohip)?"selected":""%> >
      <%=Misc.getShortStr( (prop.getProperty("last_name")+","+prop.getProperty("first_name")),"",nStrShowLen)%></option>
          <% } %>         	</select>
    <script language="Javascript">
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
    </script>
              <% } else {%>
                <%=rd%>
              <% } %>
              </td>
              <td align="left" nowrap><b><bean:message key="demographic.demographiceditdemographic.formRefDocNo"/>: </b></td>
              <td align="left">
                <%=rdohip%>
              </td>
        </tr>

        <tr valign="top">
          <td align="left" nowrap><b><bean:message key="demographic.demographiceditdemographic.formRosterStatus"/>: </b>
            <%String rosterStatus = rs.getString("roster_status");
              if (rosterStatus == null) {
                 rosterStatus = "";
              }
              %>
            <%=rosterStatus%>
          </td>
          <td align="left" nowrap><b><bean:message key="demographic.demographiceditdemographic.DateJoined"/>: </b></td>
          <td align="left">
          <%
             // Format year
             decF.applyPattern("0000");
             String hcRenewYear = decF.format(MyDateFormat.getYearFromStandardDate(rs.getString("hc_renew_date")));
             decF.applyPattern("00");
             String hcRenewMonth = decF.format(MyDateFormat.getMonthFromStandardDate(rs.getString("hc_renew_date")));
             String hcRenewDay = decF.format(MyDateFormat.getDayFromStandardDate(rs.getString("hc_renew_date")));
          %>
            <%= hcRenewYear %>
            <%= hcRenewMonth %>
            <%= hcRenewDay %>
          </td>
        </tr>
        <tr valign="top">
          <td align="left"><b><bean:message key="demographic.demographiceditdemographic.formPatientStatus"/>:</b> 

          <% String pacStatus = rs.getString("patient_status"); %>
           <% if (vLocale.getCountry().equals("BR")) { %>
                 <%=pacStatus%>
           <% } else {
               boolean nextStatus = true;
               ResultSet rsstatus = apptMainBean.queryResults("search_ptstatus");
                 while (rsstatus.next()) {
                    if ( pacStatus.equals(rsstatus.getString("patient_status")) ) { %>
                        <%=rsstatus.getString("patient_status")%>
                    <% nextStatus = false;
                    } 
                } // end while
           %>

                <% if ( nextStatus )  { 

                %>
                <%=pacStatus%>
              <% } %>

           <% } // end if...then...else %>


          </td>
          <td align="left"><b><bean:message key="demographic.demographiceditdemographic.formChartNo"/>:</b> </td>
          <td align="left">
            <%=rs.getString("chart_no")%>
          </td>
        </tr>
        <%
        if (vLocale.getCountry().equals("BR")) { %>
        <tr valign="top">
          <td align="left">
          </td>
          <td align="left"><b><bean:message key="demographic.demographicaddrecordhtm.formChartAddress"/>: </b></td>
          <td align="left">
            <%=rs.getString("chart_address")==null?"":rs.getString("chart_address")%>
          </td>
        </tr>
        <%}%>
        <%if(wL.getFound()){%>
        <tr valign="top">
          <td align="left" nowrap><b>Waiting List: </b>
          <%
            ResultSet rsWLStatus = apptMainBean.queryResults(demographic_no,"search_wlstatus");
            String listID = "", wlnote="";
            if (rsWLStatus.next()){
                listID = rsWLStatus.getString("listID");
                wlnote = rsWLStatus.getString("note");
            }
           %>
              <%  ResultSet rsWL = apptMainBean.queryResults("search_waiting_list");
                  while (rsWL.next()) { %>
                <% if ( rsWL.getString("ID").equals(listID) ) { %> 
                          <%=rsWL.getString("name")%>
                <% }
                }
              %>
          </td>
          <td align="left" nowrap><b>Waiting List Note: </b></td>
          <td align="left">
            <%=wlnote%>
          </td>
        </tr>
        <%}%>
        <tr valign="top">
          <td align="left" nowrap><b><bean:message key="demographic.demographiceditdemographic.formDateJoined1"/>: </b>
          <%
             // Format year
             decF.applyPattern("0000");
             String dateJoinedYear = decF.format(MyDateFormat.getYearFromStandardDate(rs.getString("date_joined")));
             decF.applyPattern("00");
             String dateJoinedMonth = decF.format(MyDateFormat.getMonthFromStandardDate(rs.getString("date_joined")));
             String dateJoinedDay = decF.format(MyDateFormat.getDayFromStandardDate(rs.getString("date_joined")));
          %>
            <%= dateJoinedYear %>
            <%= dateJoinedMonth %>
            <%= dateJoinedDay %>
          </td>
          <td align="left"><b><bean:message key="demographic.demographiceditdemographic.formEndDate"/>: </b></td>
          <td align="left">
          <%
             // Format year
             decF.applyPattern("0000");
             String endYear = decF.format(MyDateFormat.getYearFromStandardDate(rs.getString("end_date")));
             decF.applyPattern("00");
             String endMonth = decF.format(MyDateFormat.getMonthFromStandardDate(rs.getString("end_date")));
             String endDay = decF.format(MyDateFormat.getDayFromStandardDate(rs.getString("end_date")));
          %>
            <%= endYear %>
            <%= endMonth %>
            <%= endDay %>
          </td>
        <tr valign="top">
          <td nowrap colspan="3">
             <table width="100%" bgcolor="#EEEEFF">
              <tr>
                <td width="7%" align="left"><font color="#FF0000"><b><bean:message key="demographic.demographiceditdemographic.formAlert"/>: </b></font></td>
                <td><%=alert%></td>
              </tr>
              <tr>
                <td align="left"><b><bean:message key="demographic.demographiceditdemographic.formNotes"/>: </b></td>
                <td><%=notes%></td>
              </tr>
            </table>
        </td>
       </tr>
</form>       
</table>


<%
    }
  }
  apptMainBean.closePstmtConn();
%>

</body>
</html:html>
