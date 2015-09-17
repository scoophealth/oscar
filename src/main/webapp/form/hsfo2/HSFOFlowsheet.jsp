<%--

    Copyright (C) 2007  Heart & Stroke Foundation
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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.oscarehr.common.model.Hsfo2Patient"%>
<%@page import="org.oscarehr.common.model.Hsfo2Visit"%>
<html:html>
<head>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/HSFO2.js"></script>
  
  <title>HMP Follow Up Assessment</title>
  
  <link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css" />
  <link rel="stylesheet" href="../form/hsfo2/hsfo2.css">
</head>
<%
	Hsfo2Patient patientData = (Hsfo2Patient) request.getAttribute("Hsfo2Patient");
	Hsfo2Visit visitData = (Hsfo2Visit) request.getAttribute("Hsfo2Visit");
	
%>	
<body onLoad="initialize()">

<html:form action="/form/SaveHSFORegistrationForm2.do" onsubmit="return checkform(false)" styleId="form1" >
<DIV id="page_1">

<DIV id="id_1">

<!-- head table -->
<TABLE cellpadding=0 cellspacing=0 class="t0">
<TR class="tr0">
	<TD class="td0"><img src="../images/HSF_HMP_LogoENG.jpg" width="150" height="50" border="0" style="border:none;" alt="HSF HMP Logo"></img></TD>
  
	<TD class="td1">
    HYPERTENSION FLOWSHEET - Follow Up Assessment<br />
    Hypertension Management Program<br />
  </TD>
	
	<TD class="td2">Registration ID<br />
    <input name="Patient_Id" type="text" size="8" value="<%= patientData.getPatient_Id()%>" />
    <input name="visit_form_id" type="hidden" size="10" value="<%= visitData.getId()%>" />
    <input name="form_id" type="hidden" size="10" value="<%= visitData.getId()%>" />
  </TD>
  <TD class="td3">Site ID<br />
    <input name="Site_Id" type="text" size="8" value="<%= (String)request.getAttribute("siteId") %>" />
  </TD>
  
</TR>
</TABLE>
<!-- end head table -->

<!-- middle table -->
<TABLE cellpadding=0 cellspacing=0 class="t-m">
<TR>
  <!-- first column of middle table -->
	<TD class="td-m-fc">
  <TABLE>

    <!-- patient other information -->
    <TR><TD>
    </TD></TR>
    
    <TR><TD>
      <!-- Date of visit table -->
      <table width="100%" cellpadding=0 cellspacing=0>
        <tr><td colspan="3><font size="2" face="Arial, Helvetica, sans-serif"><strong>Date of visit</strong> </font></td></tr>
        <tr><td colspan="3"><font size="2" face="Arial, Helvetica, sans-serif"> 
          <select name="VisitDate_Id_year">
            <option value="" <%=visitData.getVisitDate_Id()== null || visitData.getVisitDate_Id().getYear()==0 ? "SELECTED" :""%>></option>
            <option value="2007" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getYear()+1900==2007 ) ? "SELECTED" :""%> >2007</option>
            <option value="2008" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getYear()+1900==2008 ) ? "SELECTED" :""%> >2008</option>
            <option value="2009" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getYear()+1900==2009 ) ? "SELECTED" :""%> >2009</option>
            <option value="2010" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getYear()+1900==2010 ) ? "SELECTED" :""%> >2010</option>
            <option value="2011" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getYear()+1900==2011 ) ? "SELECTED" :""%> >2011</option>
            <option value="2012" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getYear()+1900==2012 ) ? "SELECTED" :""%> >2012</option>
            <option value="2013" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getYear()+1900==2013 ) ? "SELECTED" :""%> >2013</option>
            <option value="2014" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getYear()+1900==2014 ) ? "SELECTED" :""%> >2014</option>
            <option value="2015" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getYear()+1900==2015 ) ? "SELECTED" :""%> >2015</option>
            <option value="2016" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getYear()+1900==2016 ) ? "SELECTED" :""%> >2016</option>
          </select> - 
          <SELECT name="VisitDate_Id_month">
            <option value="" <%=visitData.getVisitDate_Id()== null ? "SELECTED" :""%>></option>
            <option value="1" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getMonth()==0 ) ? "SELECTED" :""%>>Jan</option>
            <option value="2" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getMonth()==1 ) ? "SELECTED" :""%>>Feb</option>
            <option value="3" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getMonth()==2 ) ? "SELECTED" :""%>>Mar</option>
            <option value="4" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getMonth()==3 ) ? "SELECTED" :""%>>Apr</option>
            <option value="5" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getMonth()==4 ) ? "SELECTED" :""%>>May</option>
            <option value="6" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getMonth()==5 ) ? "SELECTED" :""%>>Jun</option>
            <option value="7" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getMonth()==6 ) ? "SELECTED" :""%>>Jul</option>
            <option value="8" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getMonth()==7 ) ? "SELECTED" :""%>>Aug</option>
            <option value="9" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getMonth()==8 ) ? "SELECTED" :""%>>Sep</option>
            <option value="10" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getMonth()==9 ) ? "SELECTED" :""%>>Oct</option>
            <option value="11" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getMonth()==10 ) ? "SELECTED" :""%>>Nov</option>
            <option value="12" <%=(visitData.getVisitDate_Id()!= null && visitData.getVisitDate_Id().getMonth()==11 ) ? "SELECTED" :""%>>Dec</option>
          </SELECT> - <SELECT name="VisitDate_Id_day">
            <option value="" <%=visitData.getVisitDate_Id()==null ? "SELECTED" :""%>></option>
            <option value="1" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==1 ) ? "SELECTED" :""%>>1</option>
            <option value="2" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==2 ) ? "SELECTED" :""%>>2</option>
            <option value="3" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==3 ) ? "SELECTED" :""%>>3</option>
            <option value="4" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==4 ) ? "SELECTED" :""%>>4</option>
            <option value="5" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==5 ) ? "SELECTED" :""%>>5</option>
            <option value="6" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==6 ) ? "SELECTED" :""%>>6</option>
            <option value="7" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==7 ) ? "SELECTED" :""%>>7</option>
            <option value="8" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==8 ) ? "SELECTED" :""%>>8</option>
            <option value="9" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==9 ) ? "SELECTED" :""%>>9</option>
            <option value="10" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==10 ) ? "SELECTED" :""%>>10</option>
            <option value="11" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==11 ) ? "SELECTED" :""%>>11</option>
            <option value="12" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==12 ) ? "SELECTED" :""%>>12</option>
            <option value="13" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==13 ) ? "SELECTED" :""%>>13</option>
            <option value="14" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==14 ) ? "SELECTED" :""%>>14</option>
            <option value="15" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==15 ) ? "SELECTED" :""%>>15</option>
            <option value="16" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==16 ) ? "SELECTED" :""%>>16</option>
            <option value="17" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==17 ) ? "SELECTED" :""%>>17</option>
            <option value="18" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==18 ) ? "SELECTED" :""%>>18</option>
            <option value="19" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==19 ) ? "SELECTED" :""%>>19</option>
            <option value="20" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==20 ) ? "SELECTED" :""%>>20</option>
            <option value="21" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==21 ) ? "SELECTED" :""%>>21</option>
            <option value="22" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==22 ) ? "SELECTED" :""%>>22</option>
            <option value="23" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==23 ) ? "SELECTED" :""%>>23</option>
            <option value="24" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==24 ) ? "SELECTED" :""%>>24</option>
            <option value="25" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==25 ) ? "SELECTED" :""%>>25</option>
            <option value="26" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==26 ) ? "SELECTED" :""%>>26</option>
            <option value="27" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==27 ) ? "SELECTED" :""%>>27</option>
            <option value="28" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==28 ) ? "SELECTED" :""%>>28</option>
            <option value="29" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==29 ) ? "SELECTED" :""%>>29</option>
            <option value="30" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==30 ) ? "SELECTED" :""%>>30</option>
            <option value="31" <%=(visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate()==31 ) ? "SELECTED" :""%>>31</option>
          </SELECT>  <!-- end date of vist -->
        </font></td></tr>
        <tr>
          <td width="56"><font size="2" face="Arial, Helvetica, sans-serif">Year</font></td>
          <td width="50"><font size="2" face="Arial, Helvetica, sans-serif">month</font></td>
          <td width="100"><font size="2" face="Arial, Helvetica, sans-serif">day</font></td>
        </tr>
      </table>
    </TD></TR>
    
    <!-- HMP -->
    <TR><TD style="border-bottom: medium solid">
      <table>
        <tr>
          <td>Status in HMP: </td>
          <td><font size="2" face="Arial, Helvetica, sans-serif"> 
            <select name="statusInHmp">
              <option value="" <%=(patientData.getStatusInHmp()== null || patientData.getStatusInHmp().equals("") ) ? "SELECTED" :""%>></option>
              <option value="enrolled" <%=(patientData.getStatusInHmp()!= null && patientData.getStatusInHmp().equals("enrolled") ) ? "SELECTED" :""%>>Enrolled</option>
              <option value="notEnrolled" <%=(patientData.getStatusInHmp()!= null && patientData.getStatusInHmp().equals("notEnrolled") ) ? "SELECTED" :""%>>Not enrolled</option>
            </select>
          </td>
        </tr>
        
        <tr><td>Date of HMP Status: </td></tr>
        <!-- date -->
        <tr><td colspan=3><FONT class="ft24">
          <input name="dateOfHmpStatus_year" type="text" size="4" maxlength=4
                 value="<%=(patientData.getDateOfHmpStatus() != null) ? ""+(patientData.getDateOfHmpStatus().getYear()+1900) : ""%>" />
          - <SELECT name="dateOfHmpStatus_month">
            <option value="" <%=patientData.getDateOfHmpStatus()== null ? "SELECTED" :""%>></option>
            <option value="1" <%=(patientData.getDateOfHmpStatus()!= null && patientData.getDateOfHmpStatus().getMonth()==0 ) ? "SELECTED" :""%>>Jan</option>
            <option value="2" <%=(patientData.getDateOfHmpStatus()!= null && patientData.getDateOfHmpStatus().getMonth()==1 ) ? "SELECTED" :""%>>Feb</option>
            <option value="3" <%=(patientData.getDateOfHmpStatus()!= null && patientData.getDateOfHmpStatus().getMonth()==2 ) ? "SELECTED" :""%>>Mar</option>
            <option value="4" <%=(patientData.getDateOfHmpStatus()!= null && patientData.getDateOfHmpStatus().getMonth()==3 ) ? "SELECTED" :""%>>Apr</option>
            <option value="5" <%=(patientData.getDateOfHmpStatus()!= null && patientData.getDateOfHmpStatus().getMonth()==4 ) ? "SELECTED" :""%>>May</option>
            <option value="6" <%=(patientData.getDateOfHmpStatus()!= null && patientData.getDateOfHmpStatus().getMonth()==5 ) ? "SELECTED" :""%>>Jun</option>
            <option value="7" <%=(patientData.getDateOfHmpStatus()!= null && patientData.getDateOfHmpStatus().getMonth()==6 ) ? "SELECTED" :""%>>Jul</option>
            <option value="8" <%=(patientData.getDateOfHmpStatus()!= null && patientData.getDateOfHmpStatus().getMonth()==7 ) ? "SELECTED" :""%>>Aug</option>
            <option value="9" <%=(patientData.getDateOfHmpStatus()!= null && patientData.getDateOfHmpStatus().getMonth()==8 ) ? "SELECTED" :""%>>Sep</option>
            <option value="10" <%=(patientData.getDateOfHmpStatus()!= null && patientData.getDateOfHmpStatus().getMonth()==9 ) ? "SELECTED" :""%>>Oct</option>
            <option value="11" <%=(patientData.getDateOfHmpStatus()!= null && patientData.getDateOfHmpStatus().getMonth()==10 ) ? "SELECTED" :""%>>Nov</option>
            <option value="12" <%=(patientData.getDateOfHmpStatus()!= null && patientData.getDateOfHmpStatus().getMonth()==11 ) ? "SELECTED" :""%>>Dec</option>
          </SELECT> - <SELECT name="dateOfHmpStatus_day">
            <option value="" <%=patientData.getDateOfHmpStatus()==null ? "SELECTED" :""%>></option>
            <option value="1" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==1 ) ? "SELECTED" :""%>>1</option>
            <option value="2" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==2 ) ? "SELECTED" :""%>>2</option>
            <option value="3" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==3 ) ? "SELECTED" :""%>>3</option>
            <option value="4" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==4 ) ? "SELECTED" :""%>>4</option>
            <option value="5" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==5 ) ? "SELECTED" :""%>>5</option>
            <option value="6" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==6 ) ? "SELECTED" :""%>>6</option>
            <option value="7" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==7 ) ? "SELECTED" :""%>>7</option>
            <option value="8" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==8 ) ? "SELECTED" :""%>>8</option>
            <option value="9" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==9 ) ? "SELECTED" :""%>>9</option>
            <option value="10" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==10 ) ? "SELECTED" :""%>>10</option>
            <option value="11" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==11 ) ? "SELECTED" :""%>>11</option>
            <option value="12" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==12 ) ? "SELECTED" :""%>>12</option>
            <option value="13" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==13 ) ? "SELECTED" :""%>>13</option>
            <option value="14" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==14 ) ? "SELECTED" :""%>>14</option>
            <option value="15" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==15 ) ? "SELECTED" :""%>>15</option>
            <option value="16" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==16 ) ? "SELECTED" :""%>>16</option>
            <option value="17" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==17 ) ? "SELECTED" :""%>>17</option>
            <option value="18" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==18 ) ? "SELECTED" :""%>>18</option>
            <option value="19" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==19 ) ? "SELECTED" :""%>>19</option>
            <option value="20" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==20 ) ? "SELECTED" :""%>>20</option>
            <option value="21" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==21 ) ? "SELECTED" :""%>>21</option>
            <option value="22" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==22 ) ? "SELECTED" :""%>>22</option>
            <option value="23" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==23 ) ? "SELECTED" :""%>>23</option>
            <option value="24" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==24 ) ? "SELECTED" :""%>>24</option>
            <option value="25" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==25 ) ? "SELECTED" :""%>>25</option>
            <option value="26" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==26 ) ? "SELECTED" :""%>>26</option>
            <option value="27" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==27 ) ? "SELECTED" :""%>>27</option>
            <option value="28" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==28 ) ? "SELECTED" :""%>>28</option>
            <option value="29" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==29 ) ? "SELECTED" :""%>>29</option>
            <option value="30" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==30 ) ? "SELECTED" :""%>>30</option>
            <option value="31" <%=(patientData.getDateOfHmpStatus()!=null && patientData.getDateOfHmpStatus().getDate()==31 ) ? "SELECTED" :""%>>31</option>
          </SELECT>  
        </FONT></td></tr>
        <!-- end of date -->
        
      </table>
    </TD></TR>
    <!-- end of HMP -->
    
    
    <!-- graphies link -->
    <tr><td><Strong>
      <A HREF="#Graphs" onClick="window.open('<html:rewrite action="/form/HSFOForm2.do" />?operation=displayGraphs&demographic_no=<%=patientData.getPatient_Id()%>', 'Graphs', 'location=1,status=1,resizable=1,scrollbars=1,width=960,height=1024');">Display graphs</A> 
    </Strong></td></tr>
    <!-- end of graphies link -->
  </TABLE>  
  </TD>
  <!-- end first column of middle table-->

<!-- the second column of middle table -->
<td class="td-m-sc" >
<table>
  <!-- medical Dx & Hx-->
  <tr><td>
    <table class="t-medicalDx" cellpadding=0 cellspacing=0 style="border-bottom: medium solid;">
      <tr><th><FONT class="ft4">Medical Dx & Hx</FONT></th>
          <th><FONT class="ft4">Family</FONT></th>
      </tr>
      <tr>
        <td>
          <input type="radio" name="HtnDxType" value="ElevatedBpReadings"
							<%=(visitData.getHtnDxType() != null && visitData.getHtnDxType().equals("ElevatedBpReadings")) ? "checked" :"" %>>
							<label>Elevated BP readings</label> </input><br />
							<FONT class="ft21">or</FONT>
        </td>
        <td valign="top">
          Hx        
        </td>
      </tr>
      <tr>
        <td>
          <input type="radio" name="HtnDxType" value="PrimaryHtn"
								<%=(visitData.getHtnDxType() != null && visitData.getHtnDxType().equals("PrimaryHtn")) ? "checked" :"" %>>
                1&deg; Hypertension</input>
        </td>
        <td>
          <input type="checkbox" name="FamHx_Htn" value="FamHx_Htn" <%=(visitData.isFamHx_Htn() ) ? "checked" :"" %>> </input>
        </td>
      </tr>
							
      <tr>
        <td>
          <input type="checkbox" name="Dyslipid" value="Dyslipid"	<%=(visitData.isDyslipid()) ? "checked" :"" %>>Dyslipidemia</input>
        </td>
        <td>
          <input type="checkbox" name="FamHx_Dyslipid" value="FamHx_Dyslipid" <%=(visitData.isFamHx_Dyslipid()) ? "checked" :"" %>> </input>
        </td>
      </tr>
      <tr>
        <td>
          <input type="checkbox" name="Diabetes" value="Diabetes" <%=(visitData.isDiabetes()) ? "checked" :"" %>>Diabetes</input>
        </td>
        <td>
          <input type="checkbox" name="FamHx_Diabetes" value="FamHx_Diabetes" <%=(visitData.isFamHx_Diabetes()) ? "checked" :"" %>> </input>
        </td>
      </tr>

      <tr>
        <td>
          <input type="checkbox" name="KidneyDis" value="KidneyDis" <%=(visitData.isKidneyDis()) ? "checked" :"" %>> Kidney disease</input>
        </td>
        <td>
          <input type="checkbox" name="FamHx_KidneyDis" value="FamHx_KidneyDis" <%=(visitData.isFamHx_KidneyDis()) ? "checked" :"" %>> </input>
        </td>
      </tr>

      <tr>
        <td>
          <input type="checkbox" name="Obesity" value="Obesity" <%=(visitData.isObesity()) ? "checked" :"" %>> Obesity</input>
        </td>
        <td>
          <input type="checkbox" name="FamHx_Obesity" value="FamHx_Obesity" <%=(visitData.isFamHx_Obesity()) ? "checked" :"" %>> </input>
        </td>
      </tr>
      <tr>
        <td>
          <input type="checkbox" name="CHD" value="CHD" <%=(visitData.isCHD()) ? "checked" :"" %>> Coronary heart disease</input>
        </td>
        <td>
          <input type="checkbox" name="FamHx_CHD" value="FamHx_CHD" <%=(visitData.isFamHx_CHD()) ? "checked" :"" %>> </input>
        </td>
      </tr>

      <tr>
        <td>
          <input type="checkbox" name="Stroke_TIA" value="Stroke_TIA" <%=(visitData.isStroke_TIA()) ? "checked" :"" %>> Stroke or TIA</input>
        </td>
        <td>
          <input type="checkbox" name="FamHx_Stroke_TIA" value="FamHx_Stroke_TIA" <%=(visitData.isFamHx_Stroke_TIA()) ? "checked" :"" %>></input>
        </td>
      </tr>
            
      <tr>
        <td>
          <input type="checkbox" name="Depression" value="Depression" <%=(visitData.isDepression()) ? "checked" :"" %>> Depression</input>
        </td>
        <td>
          <input type="checkbox" name="FamHx_Depression" value="FamHx_Depression" <%=(visitData.isFamHx_Depression()) ? "checked" :"" %>></input>
        </td>
      </tr>
      
    </table>
  </td></tr>
<!-- end of medical Dx & Hx-->

<!-- CV risk Factors -->
  <tr><td>
  <table>
    <tr><td><table> 
      <tr><td><FONT class="ft11">CV Risk Factors</FONT><br /><font class="ft1">(fill Y or N per row)<br />&nbsp;&nbsp;&nbsp;Y&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;N</font></td>
          <td><FONT class="ft11">Patient-Selected<br />Lifestyle Goal</FONT><br /><FONT class="ft8">(fill ONE)</FONT></td></tr>
    </table></td></tr>
    <tr><td><table style="border-bottom: thin solid;" >
      <tr class="tr-hight12">
        <td><input type="radio" name="Risk_weight" value="Yes" <%=(visitData.isRisk_weight()!=null && visitData.isRisk_weight().booleanValue()) ? "checked" :"" %> /></td>
        <td><input type="radio" name="Risk_weight" value="No" <%=(visitData.isRisk_weight()!=null && !visitData.isRisk_weight().booleanValue()) ? "checked" :"" %> />Weight</input></td>
        <td><input type="radio" name="LifeGoal" value="Goal_weight" <%=(visitData.getLifeGoal() != null && visitData.getLifeGoal().equals("Goal_weight")) ? "checked" :"" %> /></td>
      </tr>
      <tr class="tr-hight12">
        <td><input type="radio" name="Risk_activity" value="Yes" <%=(visitData.isRisk_activity()!=null && visitData.isRisk_activity().booleanValue()) ? "checked" :"" %> /></td>
        <td><input type="radio" name="Risk_activity" value="No" <%=(visitData.isRisk_activity()!=null && !visitData.isRisk_activity().booleanValue()) ? "checked" :"" %> > Physical activity</input></td>
        <td><input type="radio" name="LifeGoal" value="Goal_activity" <%=(visitData.getLifeGoal() != null && visitData.getLifeGoal().equals("Goal_activity")) ? "checked" :"" %> /></td>
      </tr>
      <tr>
        <td><input type="radio" name="Risk_diet" value="Yes" <%=(visitData.isRisk_diet()!=null && visitData.isRisk_diet().booleanValue()) ? "checked" : "" %> /></td>
        <td><input type="radio" name="Risk_diet" value="No" <%=(visitData.isRisk_diet()!=null && !visitData.isRisk_diet().booleanValue()) ? "checked" :"" %>> Diet/Nutrition - Salt</input></td>           
        <td><input type="radio" name="LifeGoal" value="Goal_dietSalt" <%=(visitData.getLifeGoal() != null && visitData.getLifeGoal().equals("Goal_dietSalt")) ? "checked" :"" %> /></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Dash</td>
        <td><input type="radio" name="LifeGoal" value="Goal_dietDash" <%=(visitData.getLifeGoal() != null && visitData.getLifeGoal().equals("Goal_dietDash")) ? "checked" :"" %> /></td>
      </tr>

      <tr>
        <td><input type="radio" name="Risk_smoking" value="Yes" <%=(visitData.isRisk_smoking()!=null && visitData.isRisk_smoking().booleanValue()) ? "checked" :"" %> /></td>
        <td><input type="radio" name="Risk_smoking" value="No" <%=(visitData.isRisk_smoking()!=null && !visitData.isRisk_smoking().booleanValue()) ? "checked" :"" %> >Smoking</input></td>
        <td><input type="radio" name="LifeGoal" value="Goal_smoking" <%=(visitData.getLifeGoal() != null && visitData.getLifeGoal().equals("Goal_smoking")) ? "checked" :"" %> /></td>
      </tr>
      <tr>
        <td><input type="radio" name="Risk_alcohol" value="Yes" <%=(visitData.isRisk_alcohol()!=null && visitData.isRisk_alcohol().booleanValue()) ? "checked" :"" %> /></td>
        <td><input type="radio" name="Risk_alcohol" value="No" <%=(visitData.isRisk_alcohol()!=null && !visitData.isRisk_alcohol().booleanValue()) ? "checked" :"" %> > Alcohol intake</input></td>
        <td><input type="radio" name="LifeGoal" value="Goal_alcohol" <%=(visitData.getLifeGoal() != null && visitData.getLifeGoal().equals("Goal_alcohol")) ? "checked" :"" %> /></td>
      </tr>
      <tr>
        <td><input type="radio" name="Risk_stress" value="Yes" <%=(visitData.isRisk_stress()!=null && visitData.isRisk_stress().booleanValue()) ? "checked" :"" %> /></td>
        <td><input type="radio" name="Risk_stress" value="No" <%=(visitData.isRisk_stress()!=null && !visitData.isRisk_stress().booleanValue()) ? "checked" :"" %> >Stress</input></td>
        <td><input type="radio" name="LifeGoal" value="Goal_stress" <%=(visitData.getLifeGoal() != null && visitData.getLifeGoal().equals("Goal_stress")) ? "checked" :"" %> /></td>
      </tr>
    </table></td></tr>
  </table>
  </td></tr>
  <!-- end CV risk Factors -->

  <!-- assessment of CV risk -->
  <tr><td>
    <table style="border-bottom: thin solid;">
      <tr><td colspan=3><FONT class="ft11">Current assessment of CV risk<br />factors</FONT></td></tr>
      <tr>
        <td>Physical<br />activity</td>
        <td><input name="assessActivity" type="text" size="3" maxlength="3"	value="<%=visitData.getAssessActivity()==null ? "" : visitData.getAssessActivity().intValue()%>" /></td>
        <td>min/wk<br />(0 or more)</td>
      </tr>
      <tr>
        <td>Smoking</td>
        <td><input name="assessSmoking" type="text" size="3" maxlength="3"	value="<%=visitData.getAssessSmoking()==null ? "" : visitData.getAssessSmoking().intValue()%>" /></td>
        <td>cigs/day<br />(0 or more)</td>
      </tr>
      <tr>
        <td>Alcohol</td>
        <td><input name="assessAlcohol" type="text" size="3" maxlength="3"	value="<%=visitData.getAssessAlcohol()==null ? "" : visitData.getAssessAlcohol().intValue()%>" /></td>
        <td>drinks/wk<br />(0 or more)</td>
      </tr>
    </table>
  </td></tr>
  <!-- end assessment of CV risk -->
  
  <!-- assessment of CV risk part 2-->
  <tr><td>
    <table style="border-bottom: thin solid;"><FONT class="ft6">
    
      <tr>
        <td>&nbsp;</td>
        <td>Always</td>
        <td>Often</td>
        <td>Some-<br />times</td>
        <td>Never</td>
      </tr>
      <tr>
        <td>High salt<br />foods</td>
        <td><input type="radio" name="assessSalt" value="Always" <%=(visitData.getSel_HighSaltFood() != null && visitData.getSel_HighSaltFood().equals("Always")) ? "checked" :"" %> /></td>
        <td><input type="radio" name="assessSalt" value="Often" <%=(visitData.getSel_HighSaltFood() != null && visitData.getSel_HighSaltFood().equals("Often")) ? "checked" :"" %> /></td>
        <td><input type="radio" name="assessSalt" value="Sometimes" <%=(visitData.getSel_HighSaltFood() != null && visitData.getSel_HighSaltFood().equals("Sometimes")) ? "checked" :"" %> /></td>
        <td><input type="radio" name="assessSalt" value="Never" <%=(visitData.getSel_HighSaltFood() != null && visitData.getSel_HighSaltFood().equals("Never")) ? "checked" :"" %> /></td>
      </tr>
      <tr>
        <td>DASH<br />diet</td>
        <td><input type="radio" name="assessDash" value="Always" <%=(visitData.getSel_DashDiet() != null && visitData.getSel_DashDiet().equals("Always")) ? "checked" :"" %> /></td>
        <td><input type="radio" name="assessDash" value="Often" <%=(visitData.getSel_DashDiet() != null && visitData.getSel_DashDiet().equals("Often")) ? "checked" :"" %> /></td>
        <td><input type="radio" name="assessDash" value="Sometimes" <%=(visitData.getSel_DashDiet() != null && visitData.getSel_DashDiet().equals("Sometimes")) ? "checked" :"" %> /></td>
        <td><input type="radio" name="assessDash" value="Never" <%=(visitData.getSel_DashDiet() != null && visitData.getSel_DashDiet().equals("Never")) ? "checked" :"" %> /></td>
      </tr>
      <tr>
        <td>Stressed</td>
        <td><input type="radio" name="assessStressed" value="Always" <%=(visitData.getSel_Stressed() != null && visitData.getSel_Stressed().equals("Always")) ? "checked" :"" %> /></td>
        <td><input type="radio" name="assessStressed" value="Often" <%=(visitData.getSel_Stressed() != null && visitData.getSel_Stressed().equals("Often")) ? "checked" :"" %> /></td>
        <td><input type="radio" name="assessStressed" value="Sometimes" <%=(visitData.getSel_Stressed() != null && visitData.getSel_Stressed().equals("Sometimes")) ? "checked" :"" %> /></td>
        <td><input type="radio" name="assessStressed" value="Never" <%=(visitData.getSel_Stressed() != null && visitData.getSel_Stressed().equals("Never")) ? "checked" :"" %> /></td>
      </tr>
    </FONT>
    </table>
  </td></tr>
  <!-- end assessment of CV risk part 2-->
  
  <!-- patient view of selected lifestyle goal-->
  <tr><td>
    <table class="t-lifestyleGoal">
      <tr><td colspan=2><FONT class="ft37">Patient view of selected lifestyle goal</FONT></td></tr>
      <tr>
        <td><input type="radio" name="PtView" value="Uninterested" <%="Uninterested".equals( visitData.getPtView() ) ? "checked" :"" %>>Uninterested</input></td>
        <td><input type="radio" name="PtView" value="TakingAction" <%="TakingAction".equals( visitData.getPtView() ) ? "checked" :"" %>>Taking action</input></td>
      </tr>
      <tr>
        <td><input type="radio" name="PtView" value="Thinking" <%="Thinking".equals( visitData.getPtView() ) ? "checked" :"" %>>Thinking</input></td>
        <td><input type="radio" name="PtView" value="Maintaining" <%="Maintaining".equals( visitData.getPtView() ) ? "checked" :"" %>>Maintaining</input></td>
      </tr>
      <tr>
        <td><input type="radio" name="PtView" value="Deciding" <%="Deciding".equals( visitData.getPtView() ) ? "checked" :"" %>>Deciding</input></td>
        <td><input type="radio" name="PtView" value="Relapsing" <%="Relapsing".equals( visitData.getPtView() ) ? "checked" :"" %>>Relapsing</input></td>
      </tr>
    </table>
  </td></tr>
  <!-- end patient view of selected lifestyle goal-->
  
  <!-- current assessment of selected lifesytle goal -->
  <tr><td>
    <table>
      <tr><td colspan=2><strong>Current	assessment of selected<br/>lifestyle goal</strong>(complete both)</td></tr>
      <tr><td style="width: 157px">How important is this<br />lifestyle change to patient?<br />(1-10; 10 = most)</td>
          <td align="right" style="width: 36px"><input name="Change_importance" type="text" size="2" maxlength=2
                value="<%=visitData.getChange_importance() != 0? ""+ visitData.getChange_importance(): ""%>" />
          </td>
      </tr>
      <tr>
        <td>How confident is patient<br />in carrying out the lifestyle<br />change? (10 = most)</td>
        <td align="right" style="width: 36px"><input name="Change_confidence" type="text" size="2" maxlength=2
                              value="<%=visitData.getChange_confidence() != 0? ""+ visitData.getChange_confidence(): ""%>" />
				</td>
			</tr>
    </table>
  </td></tr>
  <!-- end of current assessment of selected lifesytle goal -->
  
</table>
</td>
<!-- end of the second column of middle table -->

<!-- third column of middle table -->
<td class="td-m-tc">
<table>

<!-- Physical exam -->
<tr><td>
<table style="border-bottom: medium solid">
  <tr><th colspan=4>Physical exam</th></tr>
  <tr>
    <td>SBP</td>
    <td><input type="text" name="SBP" size=3 maxlength=3 value="<%=visitData.getSBP() != 0? ""+visitData.getSBP(): ""%>"/></td>
    <td colspan=2>Automated office<br />Blood pressure</td>
  </tr>
  <tr>
    <td>DBP</td>
    <td><input type="text" name="DBP" size=3 maxlength=3 value="<%=visitData.getDBP() != 0? ""+visitData.getDBP(): ""%>"/></td>
    <td>Monitor?<br />&nbsp;</td>
    <td>
      <input type="radio" name="monitor" value="yes" <%=(visitData.isMonitor()!=null && visitData.isMonitor().booleanValue()) ? "checked" :"" %> class="i-no-vmargin">Y</input><br />
      <input type="radio" name="monitor" value="no" <%=(visitData.isMonitor()!=null && !visitData.isMonitor().booleanValue()) ? "checked" :"" %> class="i-no-vmargin">N</input>
    </td>
  </tr>
  <tr>
    <td>Height</td>
    <td><input type="text" name="HeightP1" size=3 maxlength=3 value="<%=visitData.getHeightP1()!= 0? ""+visitData.getHeightP1(): "" %>"/></td>
    <td>.<input type="text" name="HeightP2" size=1 maxlength=1 value="<%=visitData.getHeightP2()!= 0? ""+visitData.getHeightP2(): ""  %>"/></td>
    <td>
      <input type="radio" name="Height_unit" class="i-no-vmargin" value="cm" <%=(visitData.getHeight_unit()!=null)  && visitData.getHeight_unit().equals("cm")? "checked" :"" %> >cm</input><br />
      <input type="radio" name="Height_unit" class="i-no-vmargin" value="inch" <%=(visitData.getHeight_unit()!=null)  && visitData.getHeight_unit().equals("inch")? "checked" :"" %>>in.</input><br />
    </td>
  </tr>
  <tr>
    <td>Weight</td>
    <td><input type="text" name="WeightP1" size=3 maxlength=3 value="<%=visitData.getWeightP1() != 0? ""+visitData.getWeightP1(): ""%>" /></td>
    <td>.<input type="text" name="WeightP2" size=1 maxlength=1 value="<%=visitData.getWeightP2() != 0? ""+visitData.getWeightP2(): ""%>"/></td>
    <td>
      <input type="radio" name="Weight_unit" class="i-no-vmargin" value="kg" <%="kg".equals( visitData.getWeight_unit() ) ? "checked" :"" %> >kg</input><br />
      <input type="radio" name="Weight_unit" class="i-no-vmargin" value="lb" <%="lb".equals( visitData.getWeight_unit() ) ? "checked" :"" %> >lb.</input><br />
    </td>
  </tr>
  <tr>
    <td>Waist</td>
    <td><input type="text" name="WaistP1" size=3 maxlength=3 value="<%=visitData.getWaistP1() != 0? ""+visitData.getWaistP1(): ""%>" /></td>
    <td>.<input type="text" name="WaistP2" size=1 maxlength=1 value="<%=visitData.getWaistP2() != 0? ""+visitData.getWaistP2(): ""%>" /></td>
    <td>
      <input type="radio" name="Waist_unit" class="i-no-vmargin" value="cm" <%="cm".equals( visitData.getWaist_unit() ) ? "checked" :"" %> >cm</input><br />
      <input type="radio" name="Waist_unit" class="i-no-vmargin" value="inch" <%="inch".equals( visitData.getWaist_unit() ) ? "checked" :"" %> >in.</input><br />
    </td>
  </tr>
</table>
</td></tr>
<!-- end of Physical exam -->

<!-- lab work -->
<tr><td>
<table class="t-12px" cellpadding=0 style="border-bottom: thin solid">
  <tr><td colspan=3><FONT class="ft11">Lab work<br />Lipids</FONT><FONT class="ft1">(date and results of most recent)</FONT></td><tr>
  <!-- date -->
  <tr><td colspan=3><FONT class="ft24">
    <input name="TC_HDL_LabresultsDate_year" type="text" size="4" maxlength=4
           value="<%=(visitData.getTC_HDL_LabresultsDate() != null) ? ""+(visitData.getTC_HDL_LabresultsDate().getYear()+1900) : ""%>" />
    - <SELECT name="TC_HDL_LabresultsDate_month">
      <option value="" <%=visitData.getTC_HDL_LabresultsDate()==null ? "SELECTED" :""%>></option>
      <option value="1" <%=(visitData.getTC_HDL_LabresultsDate()!= null && visitData.getTC_HDL_LabresultsDate().getMonth()==0 ) ? "SELECTED" :""%>>Jan</option>
      <option value="2" <%=(visitData.getTC_HDL_LabresultsDate()!= null && visitData.getTC_HDL_LabresultsDate().getMonth()==1 ) ? "SELECTED" :""%>>Feb</option>
      <option value="3" <%=(visitData.getTC_HDL_LabresultsDate()!= null && visitData.getTC_HDL_LabresultsDate().getMonth()==2 ) ? "SELECTED" :""%>>Mar</option>
      <option value="4" <%=(visitData.getTC_HDL_LabresultsDate()!= null && visitData.getTC_HDL_LabresultsDate().getMonth()==3 ) ? "SELECTED" :""%>>Apr</option>
      <option value="5" <%=(visitData.getTC_HDL_LabresultsDate()!= null && visitData.getTC_HDL_LabresultsDate().getMonth()==4 ) ? "SELECTED" :""%>>May</option>
      <option value="6" <%=(visitData.getTC_HDL_LabresultsDate()!= null && visitData.getTC_HDL_LabresultsDate().getMonth()==5 ) ? "SELECTED" :""%>>Jun</option>
      <option value="7" <%=(visitData.getTC_HDL_LabresultsDate()!= null && visitData.getTC_HDL_LabresultsDate().getMonth()==6 ) ? "SELECTED" :""%>>Jul</option>
      <option value="8" <%=(visitData.getTC_HDL_LabresultsDate()!= null && visitData.getTC_HDL_LabresultsDate().getMonth()==7 ) ? "SELECTED" :""%>>Aug</option>
      <option value="9" <%=(visitData.getTC_HDL_LabresultsDate()!= null && visitData.getTC_HDL_LabresultsDate().getMonth()==8 ) ? "SELECTED" :""%>>Sep</option>
      <option value="10" <%=(visitData.getTC_HDL_LabresultsDate()!= null && visitData.getTC_HDL_LabresultsDate().getMonth()==9 ) ? "SELECTED" :""%>>Oct</option>
      <option value="11" <%=(visitData.getTC_HDL_LabresultsDate()!= null && visitData.getTC_HDL_LabresultsDate().getMonth()==10 ) ? "SELECTED" :""%>>Nov</option>
      <option value="12" <%=(visitData.getTC_HDL_LabresultsDate()!= null && visitData.getTC_HDL_LabresultsDate().getMonth()==11 ) ? "SELECTED" :""%>>Dec</option>
    </SELECT> - <SELECT name="TC_HDL_LabresultsDate_day">
      <option value="" <%=visitData.getTC_HDL_LabresultsDate()==null ? "SELECTED" :""%>></option>
      <option value="1" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==1 ) ? "SELECTED" :""%>>1</option>
      <option value="2" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==2 ) ? "SELECTED" :""%>>2</option>
      <option value="3" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==3 ) ? "SELECTED" :""%>>3</option>
      <option value="4" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==4 ) ? "SELECTED" :""%>>4</option>
      <option value="5" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==5 ) ? "SELECTED" :""%>>5</option>
      <option value="6" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==6 ) ? "SELECTED" :""%>>6</option>
      <option value="7" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==7 ) ? "SELECTED" :""%>>7</option>
      <option value="8" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==8 ) ? "SELECTED" :""%>>8</option>
      <option value="9" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==9 ) ? "SELECTED" :""%>>9</option>
      <option value="10" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==10 ) ? "SELECTED" :""%>>10</option>
      <option value="11" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==11 ) ? "SELECTED" :""%>>11</option>
      <option value="12" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==12 ) ? "SELECTED" :""%>>12</option>
      <option value="13" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==13 ) ? "SELECTED" :""%>>13</option>
      <option value="14" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==14 ) ? "SELECTED" :""%>>14</option>
      <option value="15" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==15 ) ? "SELECTED" :""%>>15</option>
      <option value="16" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==16 ) ? "SELECTED" :""%>>16</option>
      <option value="17" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==17 ) ? "SELECTED" :""%>>17</option>
      <option value="18" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==18 ) ? "SELECTED" :""%>>18</option>
      <option value="19" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==19 ) ? "SELECTED" :""%>>19</option>
      <option value="20" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==20 ) ? "SELECTED" :""%>>20</option>
      <option value="21" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==21 ) ? "SELECTED" :""%>>21</option>
      <option value="22" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==22 ) ? "SELECTED" :""%>>22</option>
      <option value="23" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==23 ) ? "SELECTED" :""%>>23</option>
      <option value="24" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==24 ) ? "SELECTED" :""%>>24</option>
      <option value="25" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==25 ) ? "SELECTED" :""%>>25</option>
      <option value="26" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==26 ) ? "SELECTED" :""%>>26</option>
      <option value="27" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==27 ) ? "SELECTED" :""%>>27</option>
      <option value="28" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==28 ) ? "SELECTED" :""%>>28</option>
      <option value="29" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==29 ) ? "SELECTED" :""%>>29</option>
      <option value="30" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==30 ) ? "SELECTED" :""%>>30</option>
      <option value="31" <%=(visitData.getTC_HDL_LabresultsDate()!=null && visitData.getTC_HDL_LabresultsDate().getDate()==31 ) ? "SELECTED" :""%>>31</option>
    </SELECT>  
  </FONT></td></tr>
  <!-- end of date -->
  <tr>
    <td>LDL</td>
    <td><input type="text" style="width:30px" maxlength=2 name="LDLP1" value="<%=visitData.getLDLP1() != 0? ""+visitData.getLDLP1(): ""%>" />
        .<input type="text" style="width:16px" maxlength=1 name="LDLP2" value="<%=visitData.getLDLP2() != 0? ""+visitData.getLDLP2(): ""%>"/></td>
    <td>mmol/L(goal &lt; 2.0)</td>
  </tr>
    <tr>
    <td>TC/HDL</td>
    <td><input type="text" style="width:30px" maxlength=2 name="TC_HDLP1" value="<%=visitData.getTC_HDLP1() != 0? ""+visitData.getTC_HDLP1(): ""%>" />
        .<input type="text" style="width:16px" maxlength=1 name="TC_HDLP2" value="<%=visitData.getTC_HDLP2() != 0? ""+visitData.getTC_HDLP2(): ""%>" /></td>
    <td>ratio(goal &lt; 4.0)</td>
  </tr>
  <tr>
    <td>HDL</td>
    <td align="right"><input type="text" style="width:16px" maxlength=1 name="HDLP1" value="<%=visitData.getHDLP1() != 0? ""+visitData.getHDLP1(): ""%>" />
        .<input type="text" style="width:16px" maxlength=1 name="HDLP2" value="<%=visitData.getHDLP2() != 0? ""+visitData.getHDLP2(): ""%>" /></td>
    <td>mmol/L(goal &gt; 1.0)</td>
  </tr>
  <tr>
    <td>Trigly<br />-cerides</td>
    <td><input type="text" style="width:30px" maxlength=2 name="TriglyceridesP1" value="<%=visitData.getTriglyceridesP1() != 0? ""+visitData.getTriglyceridesP1(): ""%>" />
        .<input type="text" style="width:16px" maxlength=1 name="TriglyceridesP2" value="<%=visitData.getTriglyceridesP2() != 0? ""+visitData.getTriglyceridesP2(): ""%>"/></td>
    <td>mmol/L(goal &lt; 1.7)</td>
  </tr>
</table>
</td></tr>
<!-- end of lab work -->
  

<!-- A1C and FBS -->
<tr><td>
<table class="t-12px" cellpadding=0 style="border-bottom: thin solid">
  <tr><td colspan=3>
  AIC and FBS (data and results of most recent)
  </td></tr>
  <!-- date -->
  <tr><td colspan=3><FONT class="ft24">
    <input name="A1C_LabresultsDate_year" type="text" size="4" maxlength=4
           value="<%=(visitData.getA1C_LabresultsDate() != null) ? ""+(visitData.getA1C_LabresultsDate().getYear()+1900) : ""%>" />
    - <SELECT name="A1C_LabresultsDate_month">
      <option value="" <%=visitData.getA1C_LabresultsDate()==null ? "SELECTED" :""%>></option>
      <option value="1" <%=(visitData.getA1C_LabresultsDate()!= null && visitData.getA1C_LabresultsDate().getMonth()==0 ) ? "SELECTED" :""%>>Jan</option>
      <option value="2" <%=(visitData.getA1C_LabresultsDate()!= null && visitData.getA1C_LabresultsDate().getMonth()==1 ) ? "SELECTED" :""%>>Feb</option>
      <option value="3" <%=(visitData.getA1C_LabresultsDate()!= null && visitData.getA1C_LabresultsDate().getMonth()==2 ) ? "SELECTED" :""%>>Mar</option>
      <option value="4" <%=(visitData.getA1C_LabresultsDate()!= null && visitData.getA1C_LabresultsDate().getMonth()==3 ) ? "SELECTED" :""%>>Apr</option>
      <option value="5" <%=(visitData.getA1C_LabresultsDate()!= null && visitData.getA1C_LabresultsDate().getMonth()==4 ) ? "SELECTED" :""%>>May</option>
      <option value="6" <%=(visitData.getA1C_LabresultsDate()!= null && visitData.getA1C_LabresultsDate().getMonth()==5 ) ? "SELECTED" :""%>>Jun</option>
      <option value="7" <%=(visitData.getA1C_LabresultsDate()!= null && visitData.getA1C_LabresultsDate().getMonth()==6 ) ? "SELECTED" :""%>>Jul</option>
      <option value="8" <%=(visitData.getA1C_LabresultsDate()!= null && visitData.getA1C_LabresultsDate().getMonth()==7 ) ? "SELECTED" :""%>>Aug</option>
      <option value="9" <%=(visitData.getA1C_LabresultsDate()!= null && visitData.getA1C_LabresultsDate().getMonth()==8 ) ? "SELECTED" :""%>>Sep</option>
      <option value="10" <%=(visitData.getA1C_LabresultsDate()!= null && visitData.getA1C_LabresultsDate().getMonth()==9 ) ? "SELECTED" :""%>>Oct</option>
      <option value="11" <%=(visitData.getA1C_LabresultsDate()!= null && visitData.getA1C_LabresultsDate().getMonth()==10 ) ? "SELECTED" :""%>>Nov</option>
      <option value="12" <%=(visitData.getA1C_LabresultsDate()!= null && visitData.getA1C_LabresultsDate().getMonth()==11 ) ? "SELECTED" :""%>>Dec</option>
    </SELECT> - <SELECT name="A1C_LabresultsDate_day">
      <option value="" <%=visitData.getA1C_LabresultsDate()==null ? "SELECTED" :""%>></option>
      <option value="1" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==1 ) ? "SELECTED" :""%>>1</option>
      <option value="2" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==2 ) ? "SELECTED" :""%>>2</option>
      <option value="3" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==3 ) ? "SELECTED" :""%>>3</option>
      <option value="4" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==4 ) ? "SELECTED" :""%>>4</option>
      <option value="5" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==5 ) ? "SELECTED" :""%>>5</option>
      <option value="6" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==6 ) ? "SELECTED" :""%>>6</option>
      <option value="7" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==7 ) ? "SELECTED" :""%>>7</option>
      <option value="8" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==8 ) ? "SELECTED" :""%>>8</option>
      <option value="9" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==9 ) ? "SELECTED" :""%>>9</option>
      <option value="10" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==10 ) ? "SELECTED" :""%>>10</option>
      <option value="11" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==11 ) ? "SELECTED" :""%>>11</option>
      <option value="12" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==12 ) ? "SELECTED" :""%>>12</option>
      <option value="13" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==13 ) ? "SELECTED" :""%>>13</option>
      <option value="14" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==14 ) ? "SELECTED" :""%>>14</option>
      <option value="15" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==15 ) ? "SELECTED" :""%>>15</option>
      <option value="16" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==16 ) ? "SELECTED" :""%>>16</option>
      <option value="17" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==17 ) ? "SELECTED" :""%>>17</option>
      <option value="18" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==18 ) ? "SELECTED" :""%>>18</option>
      <option value="19" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==19 ) ? "SELECTED" :""%>>19</option>
      <option value="20" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==20 ) ? "SELECTED" :""%>>20</option>
      <option value="21" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==21 ) ? "SELECTED" :""%>>21</option>
      <option value="22" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==22 ) ? "SELECTED" :""%>>22</option>
      <option value="23" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==23 ) ? "SELECTED" :""%>>23</option>
      <option value="24" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==24 ) ? "SELECTED" :""%>>24</option>
      <option value="25" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==25 ) ? "SELECTED" :""%>>25</option>
      <option value="26" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==26 ) ? "SELECTED" :""%>>26</option>
      <option value="27" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==27 ) ? "SELECTED" :""%>>27</option>
      <option value="28" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==28 ) ? "SELECTED" :""%>>28</option>
      <option value="29" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==29 ) ? "SELECTED" :""%>>29</option>
      <option value="30" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==30 ) ? "SELECTED" :""%>>30</option>
      <option value="31" <%=(visitData.getA1C_LabresultsDate()!=null && visitData.getA1C_LabresultsDate().getDate()==31 ) ? "SELECTED" :""%>>31</option>
    </SELECT>  
  </FONT></td></tr>
  <tr>
    <td>A1C</td>
    <td align="right"><input type="text" style="width:30px" maxlength=2 name="A1CP1" value="<%=visitData.getA1CP1() != 0? ""+ visitData.getA1CP1(): ""%>" />
        .<input type="text" style="width:16px" maxlength=1 name="A1CP2" value="<%=visitData.getA1CP2() != 0 ? ""+ visitData.getA1CP2(): ""%>"/>%</td>
    <td>(goal &lt; 7.0%)</td>
  </tr>
  <tr>
    <td>FBS</td>
    <td align="right"><input type="text" style="width:32px" maxlength=2 name="FBSP1" value="<%=visitData.getFBSP1() != 0 ? ""+ visitData.getFBSP1(): ""%>" />
        .<input type="text" style="width:16px" maxlength=1 name="FBSP2" value="<%=visitData.getFBSP2() != 0 ? ""+ visitData.getFBSP2(): ""%>" /></td>
    <td>mmol/L (goal 4-7)</td>
  </tr>
</table>
</td></tr>
<!-- end of A1C and FBS -->

<!-- eGFR and ACR -->
<tr><td>
<table class="t-12px" cellpadding=0 style="border-bottom: medium solid">
  <tr><td colspan=3>
    <FONT class="ft11">eGFR and ACR </FONT><FONT class="ft1">(data and results of most recent)</FONT>
  </td></tr>
  <!-- date -->
  <tr><td colspan=3><FONT class="ft24">
    <input name="egfrYear" type="text" size="4" maxlength=4
           value="<%=(visitData.getEgfrDate() != null) ? ""+(visitData.getEgfrDate().getYear()+1900) : ""%>" />
    - <SELECT name="egfrMonth">
      <option value="" <%=visitData.getEgfrDate()==null ? "SELECTED" :""%>></option>
      <option value="1" <%=(visitData.getEgfrDate()!= null && visitData.getEgfrDate().getMonth()==0 ) ? "SELECTED" :""%>>Jan</option>
      <option value="2" <%=(visitData.getEgfrDate()!= null && visitData.getEgfrDate().getMonth()==1 ) ? "SELECTED" :""%>>Feb</option>
      <option value="3" <%=(visitData.getEgfrDate()!= null && visitData.getEgfrDate().getMonth()==2 ) ? "SELECTED" :""%>>Mar</option>
      <option value="4" <%=(visitData.getEgfrDate()!= null && visitData.getEgfrDate().getMonth()==3 ) ? "SELECTED" :""%>>Apr</option>
      <option value="5" <%=(visitData.getEgfrDate()!= null && visitData.getEgfrDate().getMonth()==4 ) ? "SELECTED" :""%>>May</option>
      <option value="6" <%=(visitData.getEgfrDate()!= null && visitData.getEgfrDate().getMonth()==5 ) ? "SELECTED" :""%>>Jun</option>
      <option value="7" <%=(visitData.getEgfrDate()!= null && visitData.getEgfrDate().getMonth()==6 ) ? "SELECTED" :""%>>Jul</option>
      <option value="8" <%=(visitData.getEgfrDate()!= null && visitData.getEgfrDate().getMonth()==7 ) ? "SELECTED" :""%>>Aug</option>
      <option value="9" <%=(visitData.getEgfrDate()!= null && visitData.getEgfrDate().getMonth()==8 ) ? "SELECTED" :""%>>Sep</option>
      <option value="10" <%=(visitData.getEgfrDate()!= null && visitData.getEgfrDate().getMonth()==9 ) ? "SELECTED" :""%>>Oct</option>
      <option value="11" <%=(visitData.getEgfrDate()!= null && visitData.getEgfrDate().getMonth()==10 ) ? "SELECTED" :""%>>Nov</option>
      <option value="12" <%=(visitData.getEgfrDate()!= null && visitData.getEgfrDate().getMonth()==11 ) ? "SELECTED" :""%>>Dec</option>
    </SELECT> - <SELECT name="egfrDay">
      <option value="" <%=visitData.getEgfrDate()==null ? "SELECTED" :""%>></option>
      <option value="1" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==1 ) ? "SELECTED" :""%>>1</option>
      <option value="2" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==2 ) ? "SELECTED" :""%>>2</option>
      <option value="3" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==3 ) ? "SELECTED" :""%>>3</option>
      <option value="4" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==4 ) ? "SELECTED" :""%>>4</option>
      <option value="5" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==5 ) ? "SELECTED" :""%>>5</option>
      <option value="6" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==6 ) ? "SELECTED" :""%>>6</option>
      <option value="7" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==7 ) ? "SELECTED" :""%>>7</option>
      <option value="8" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==8 ) ? "SELECTED" :""%>>8</option>
      <option value="9" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==9 ) ? "SELECTED" :""%>>9</option>
      <option value="10" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==10 ) ? "SELECTED" :""%>>10</option>
      <option value="11" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==11 ) ? "SELECTED" :""%>>11</option>
      <option value="12" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==12 ) ? "SELECTED" :""%>>12</option>
      <option value="13" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==13 ) ? "SELECTED" :""%>>13</option>
      <option value="14" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==14 ) ? "SELECTED" :""%>>14</option>
      <option value="15" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==15 ) ? "SELECTED" :""%>>15</option>
      <option value="16" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==16 ) ? "SELECTED" :""%>>16</option>
      <option value="17" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==17 ) ? "SELECTED" :""%>>17</option>
      <option value="18" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==18 ) ? "SELECTED" :""%>>18</option>
      <option value="19" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==19 ) ? "SELECTED" :""%>>19</option>
      <option value="20" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==20 ) ? "SELECTED" :""%>>20</option>
      <option value="21" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==21 ) ? "SELECTED" :""%>>21</option>
      <option value="22" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==22 ) ? "SELECTED" :""%>>22</option>
      <option value="23" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==23 ) ? "SELECTED" :""%>>23</option>
      <option value="24" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==24 ) ? "SELECTED" :""%>>24</option>
      <option value="25" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==25 ) ? "SELECTED" :""%>>25</option>
      <option value="26" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==26 ) ? "SELECTED" :""%>>26</option>
      <option value="27" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==27 ) ? "SELECTED" :""%>>27</option>
      <option value="28" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==28 ) ? "SELECTED" :""%>>28</option>
      <option value="29" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==29 ) ? "SELECTED" :""%>>29</option>
      <option value="30" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==30 ) ? "SELECTED" :""%>>30</option>
      <option value="31" <%=(visitData.getEgfrDate()!=null && visitData.getEgfrDate().getDate()==31 ) ? "SELECTED" :""%>>31</option>
    </SELECT>  
  </FONT></td></tr>
  <tr>
    <td>eGFR</td>
    <td align="left"><input type="text" style="width:45px" maxlength=3 name="egfr" value="<%=visitData.getEgfr() != 0.0? ""+ visitData.getEgfr(): ""%>" />
    </td>
    <td>mL/min</td>
  </tr>
  <tr>
    <td>ACR</td>
    <td align="right"><input type="text" style="width:45px" maxlength=4 name="acrP1" value="<%=visitData.getAcrP1() != 0 ? ""+ visitData.getAcrP1(): ""%>" />
        .<input type="text" style="width:16px" maxlength=1 name="acrP2" value="<%=visitData.getAcrP2() != 0 ? ""+ visitData.getAcrP2(): ""%>" /></td>
    <td>mg/mmol</td>
  </tr>
</table>
</td></tr>
<!-- end of eGFR and ACR -->

<!-- meds -->
<tr><td>
<table style="border-bottom: medium solid">
  <tr>
    <td>How often does patient miss<br />taking his/her meds?</td>
    <td><input name="Often_miss" type="text" size="2" maxlength="2" value="<%=visitData.getOften_miss() != 0? ""+ visitData.getOften_miss(): ""%>" >/wk</input></td>
  </tr>
  <tr>
    <td>Does patient take any<br /> herbal remedies</td>
    <td>
      <input type="radio" name="Herbal" value="yes" <%=(visitData.getHerbal()!=null && visitData.getHerbal().equals("yes"))? "checked" :"" %>>Y</input> 
      <input type="radio" name="Herbal" value="no" <%=(visitData.getHerbal()!=null && visitData.getHerbal().equals("no"))? "checked" :"" %>>N</inut>
    </td>
  </tr>
  <tr>
    <td>Adequate drug coverage?</td>
      <td>
        <input type="radio" name="Drugcoverage" value="yes" <%=(visitData.getDrugcoverage() != null && visitData.getDrugcoverage().equals("yes")) ? "checked" :"" %>>Y</input>
        <input type="radio" name="Drugcoverage" value="no" <%=(visitData.getDrugcoverage() != null && visitData.getDrugcoverage().equals("no")) ? "checked" :"" %>>N</input>
      </td>
  </tr>
</table>
</td></tr>
<!-- end meds -->

<!-- next visit -->
<tr><td style="border-bottom: medium solid">
<table>
  <tr><td>
    Next visit in
    <input name="nextVisitInMonths" type="text" size=2 maxlength=2 value=<%=visitData.getNextVisitInMonths()==0 ? "" : visitData.getNextVisitInMonths()%> >mths or</input>
    <input name="nextVisitInWeeks" type="text" size=2 maxlength=2 value=<%=visitData.getNextVisitInWeeks()==0 ? "" : visitData.getNextVisitInWeeks()%> >wks</input>
  </td></tr>
</table>
</td></tr>
<!-- end of next visit -->

<!-- Tools Provided section -->
</tr><td style="vertical-align: top;">
  <u>Tools provided</u><br />
  <input type="checkbox" name="PressureOff" value="PressureOff"
    <%=visitData.isPressureOff()? "checked" :"" %> >HSFO Take the<br />Pressure Off book</input><br />
  <input type="checkbox" name="Bpactionplan" value="Bpactionplan"
    <%=visitData.isBpactionplan()? "checked" :"" %>>My Heart &amp; Stroke<br />BP Action Plan e-tool</input><br />
    
  <u>BP monitoring</u><br />
  <input type="checkbox" name="Home" value="Home" <%=visitData.isHome()? "checked" :"" %>>Home</input>
  <input type="checkbox" name="ABPM" value="ABPM" <%=visitData.isABPM()? "checked" :"" %>>Ambulatory</input><br />
  
  <u>Referrals</u><br />
  <input type="checkbox" name="ProRefer" value="ProRefer" <%=visitData.isProRefer()? "checked" :"" %>>Healthcare Professional</input><br />
  <input type="checkbox" name="CommunityRes" value="CommunityRes" <%=visitData.isCommunityRes()? "checked" :"" %>>
    Community Resources</input>
</td></tr>
<!-- end of Tools Provided section -->

</table>
</td>
<!-- end of third column of middle table -->

</TR></TABLE>
<!-- end of middle table -->


<!-- bottom table -->
<table class="t-bottom">

<!-- first column of bottom table -->
<tr style="vertical-align: top;">

<!-- second column of bottom table -->
<td style="vertical-align: top;">
  <table style="border-left: medium solid; border-bottom: medium solid; border-right: medium solid; ">
    <tr>
      <td><strong>Medications</strong></td>
      <td>Currently</td>
      <td class="td-solidRight">Side</td>
      <td colspan=2>Rx decision</td>
      <td colspan=3>&nbsp;</td>
      <td>In-class</td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>Rx'd</td>
      <td class="td-solidRight">effects</td>
      <td>Same</td>
      <td>Incr.</td>
      <td>Decr.</td>
      <td>Stop</td>
      <td>Start</td>
      <td>switch</td>
    </tr>
    <tr>
      <td>Diuretic<br />ACE inhibitor</td>
      <td><input type="checkbox" name="Diuret_rx" value="Diuret_rx"<%=visitData.isDiuret_rx()? "checked" :"" %> /> <br />
          <input type="checkbox" name="Ace_rx" value="Ace_rx"<%=visitData.isAce_rx()? "checked" :"" %> />
      </td>
      <td  class="td-solidRight"><input type="checkbox" name="Diuret_SideEffects" value="Diuret_SideEffects" <%=visitData.isDiuret_SideEffects()? "checked" :"" %> /> <br />
          <input type="checkbox" name="Ace_SideEffects" value="Ace_SideEffects"<%=visitData.isAce_SideEffects()? "checked" :"" %> />
      </td>
      <td>
        <input type="radio" name="Diuret_RxDecToday" value="Same" onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked"
          <%=(visitData.getDiuret_RxDecToday()!= null && visitData.getDiuret_RxDecToday().equals("Same")) ? "checked" :"" %> />
        <br />
        <input type="radio" name="Ace_RxDecToday" value="Same" onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked"
          <%=(visitData.getAce_RxDecToday()!=null && visitData.getAce_RxDecToday().equals("Same")) ? "checked" :"" %> />
      </td>
      <td>
        <input type="radio" name="Diuret_RxDecToday" value="Increase"
          <%=(visitData.getDiuret_RxDecToday()!=null && visitData.getDiuret_RxDecToday().equals("Increase")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Ace_RxDecToday" value="Increase"
          <%=(visitData.getAce_RxDecToday()!=null && visitData.getAce_RxDecToday().equals("Increase")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
      <td>
        <input type="radio" name="Diuret_RxDecToday" value="Decrease"
          <%=(visitData.getDiuret_RxDecToday()!=null && visitData.getDiuret_RxDecToday().equals("Decrease")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Ace_RxDecToday" value="Decrease"
          <%=(visitData.getDiuret_RxDecToday()!=null && visitData.getDiuret_RxDecToday().equals("Decrease")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
      <td>
        <input type="radio" name="Diuret_RxDecToday" value="Stop"
          <%=(visitData.getDiuret_RxDecToday()!= null && visitData.getDiuret_RxDecToday().equals("Stop")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Ace_RxDecToday" value="Stop"
          <%=(visitData.getAce_RxDecToday()!=null && visitData.getAce_RxDecToday().equals("Stop")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
      <td> 
        <input type="radio" name="Diuret_RxDecToday" value="Start"
          <%=(visitData.getDiuret_RxDecToday()!=null && visitData.getDiuret_RxDecToday().equals("Start")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Ace_RxDecToday" value="Start"
          <%=(visitData.getAce_RxDecToday()!=null && visitData.getAce_RxDecToday().equals("Start")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
      <td>
        <input type="radio" name="Diuret_RxDecToday" value="InClassSwitch"
          <%=(visitData.getDiuret_RxDecToday()!=null && visitData.getDiuret_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Ace_RxDecToday" value="InClassSwitch"
          <%=(visitData.getAce_RxDecToday()!=null && visitData.getAce_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
    </tr>
    <tr><td colspan=9 class="thinBottom"></td></tr>
    <tr >
      <td>A-II receptor antagonist<br />
          Beta blocker<br />
          Calcium channel blocker</td>
      <td>
        <input type="checkbox" name="Arecept_rx" value="Arecept_rx"
          <%=visitData.isArecept_rx()? "checked" :"" %> /> <br />
        <input type="checkbox" name="Beta_rx" value="Beta_rx"
          <%=visitData.isBeta_rx()? "checked" :"" %> /> <br />
        <input type="checkbox" name="Calc_rx" value="Calc_rx"
          <%=visitData.isCalc_rx()? "checked" :"" %> />
      </td>
      <td class="td-solidRight">
        <input type="checkbox" name="Arecept_SideEffects" value="Arecept_SideEffects"
          <%=visitData.isArecept_SideEffects()? "checked" :"" %> /> <br />
        <input type="checkbox" name="Beta_SideEffects" value="Beta_SideEffects"
          <%=visitData.isBeta_SideEffects()? "checked" :"" %> /> <br />
        <input type="checkbox" name="Calc_SideEffects" value="Calc_SideEffects"
          <%=visitData.isCalc_SideEffects()? "checked" :"" %> />
      </td>
      <td>
        <input type="radio" name="Arecept_RxDecToday" value="Same"
          <%=(visitData.getArecept_RxDecToday()!= null && visitData.getArecept_RxDecToday().equals("Same")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Beta_RxDecToday" value="Same"
          <%=(visitData.getBeta_RxDecToday()!=null && visitData.getBeta_RxDecToday().equals("Same")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Calc_RxDecToday" value="Same"
          <%=(visitData.getCalc_RxDecToday()!=null && visitData.getCalc_RxDecToday().equals("Same")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
      <td>
        <input type="radio" name="Arecept_RxDecToday" value="Increase"
          <%=(visitData.getArecept_RxDecToday()!=null && visitData.getArecept_RxDecToday().equals("Increase")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Beta_RxDecToday" value="Increase"
          <%=(visitData.getBeta_RxDecToday()!=null && visitData.getBeta_RxDecToday().equals("Increase")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Calc_RxDecToday" value="Increase"
          <%=(visitData.getCalc_RxDecToday()!=null && visitData.getCalc_RxDecToday().equals("Increase")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
      <td>
        <input type="radio" name="Arecept_RxDecToday" value="Decrease"
          <%=(visitData.getArecept_RxDecToday()!=null && visitData.getArecept_RxDecToday().equals("Decrease")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Beta_RxDecToday" value="Decrease"
          <%=(visitData.getBeta_RxDecToday()!=null && visitData.getBeta_RxDecToday().equals("Decrease")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Calc_RxDecToday" value="Decrease"
          <%=(visitData.getCalc_RxDecToday()!=null && visitData.getCalc_RxDecToday().equals("Decrease")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
      <td>
        <input type="radio" name="Arecept_RxDecToday" value="Stop"
          <%=(visitData.getArecept_RxDecToday()!=null && visitData.getArecept_RxDecToday().equals("Stop")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Beta_RxDecToday" value="Stop"
          <%=(visitData.getBeta_RxDecToday()!=null && visitData.getBeta_RxDecToday().equals("Stop")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Calc_RxDecToday" value="Stop"
          <%=(visitData.getCalc_RxDecToday()!=null && visitData.getCalc_RxDecToday().equals("Stop")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
      <td class="coldash">
        <input type="radio" name="Arecept_RxDecToday" value="Start"
          <%=(visitData.getArecept_RxDecToday()!=null && visitData.getArecept_RxDecToday().equals("Start")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Beta_RxDecToday" value="Start"
          <%=(visitData.getBeta_RxDecToday()!=null && visitData.getBeta_RxDecToday().equals("Start")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Calc_RxDecToday" value="Start"
          <%=(visitData.getCalc_RxDecToday()!=null && visitData.getCalc_RxDecToday().equals("Start")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
      <td>
        <input type="radio" name="Arecept_RxDecToday" value="InClassSwitch"
          <%=(visitData.getArecept_RxDecToday()!=null && visitData.getArecept_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Beta_RxDecToday" value="InClassSwitch"
          <%=(visitData.getBeta_RxDecToday()!=null && visitData.getBeta_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Calc_RxDecToday" value="InClassSwitch"
          <%=(visitData.getCalc_RxDecToday()!=null && visitData.getCalc_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
    </tr>
    <tr><td colspan=9 class="thinBottom"></td></tr>
    <tr>
      <td>
        Other antihypertensive <br />
        Statin<br />
        Other lipid-lowering</td>
      <td>
        <input type="checkbox" name="Anti_rx" value="Anti_rx"
          <%=visitData.isAnti_rx()? "checked" :"" %> /> <br />
        <input type="checkbox" name="Statin_rx" value="Statin_rx"
          <%=visitData.isStatin_rx()? "checked" :"" %> /> <br />
        <input type="checkbox" name="Lipid_rx" value="Lipid_rx"
          <%=visitData.isLipid_rx()? "checked" :"" %> /> 
      </td>
      <td class="td-solidRight">
        <input type="checkbox" name="Anti_SideEffects" value="Anti_SideEffects"
          <%=visitData.isAnti_SideEffects()? "checked" :"" %> /> <br />
        <input type="checkbox" name="Statin_SideEffects" value="Statin_SideEffects"
          <%=visitData.isStatin_SideEffects()? "checked" :"" %> /> <br />
        <input type="checkbox" name="Lipid_SideEffects" value="Lipid_SideEffects"
          <%=visitData.isLipid_SideEffects()? "checked" :"" %> />
      </td>
      <td>
        <input type="radio" name="Anti_RxDecToday" value="Same"
          <%=(visitData.getAnti_RxDecToday()!=null && visitData.getAnti_RxDecToday().equals("Same")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Statin_RxDecToday" value="Same"
          <%=(visitData.getStatin_RxDecToday()!=null && visitData.getStatin_RxDecToday().equals("Same")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Lipid_RxDecToday" value="Same"
          <%=(visitData.getLipid_RxDecToday()!=null && visitData.getLipid_RxDecToday().equals("Same")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
      <td>
        <input type="radio" name="Anti_RxDecToday" value="Increase"
          <%=(visitData.getAnti_RxDecToday()!=null && visitData.getAnti_RxDecToday().equals("Increase")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Statin_RxDecToday" value="Increase"
          <%=(visitData.getStatin_RxDecToday()!=null && visitData.getStatin_RxDecToday().equals("Increase")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Lipid_RxDecToday" value="Increase"
          <%=(visitData.getLipid_RxDecToday()!=null && visitData.getLipid_RxDecToday().equals("Increase")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
      <td>
        <input type="radio" name="Anti_RxDecToday" value="Decrease"
          <%=(visitData.getAnti_RxDecToday()!=null && visitData.getAnti_RxDecToday().equals("Decrease")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Statin_RxDecToday" value="Decrease"
          <%=(visitData.getStatin_RxDecToday()!=null && visitData.getStatin_RxDecToday().equals("Decrease")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Lipid_RxDecToday" value="Decrease"
          <%=(visitData.getLipid_RxDecToday()!=null && visitData.getLipid_RxDecToday().equals("Decrease")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
      <td>
        <input type="radio" name="Anti_RxDecToday" value="Stop"
          <%=(visitData.getAnti_RxDecToday()!=null && visitData.getAnti_RxDecToday().equals("Stop")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Statin_RxDecToday" value="Stop"
          <%=(visitData.getStatin_RxDecToday()!=null && visitData.getStatin_RxDecToday().equals("Stop")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Lipid_RxDecToday" value="Stop"
          <%=(visitData.getLipid_RxDecToday()!=null && visitData.getLipid_RxDecToday().equals("Stop")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
      <td>
        <input type="radio" name="Anti_RxDecToday" value="Start"
          <%=(visitData.getAnti_RxDecToday()!=null && visitData.getAnti_RxDecToday().equals("Start")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Statin_RxDecToday" value="Start"
          <%=(visitData.getStatin_RxDecToday()!=null && visitData.getStatin_RxDecToday().equals("Start")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Lipid_RxDecToday" value="Start"
          <%=(visitData.getLipid_RxDecToday()!=null && visitData.getLipid_RxDecToday().equals("Start")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
      <td>
        <input type="radio" name="Anti_RxDecToday" value="InClassSwitch"
          <%=(visitData.getAnti_RxDecToday()!=null && visitData.getAnti_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Statin_RxDecToday" value="InClassSwitch"
          <%=(visitData.getStatin_RxDecToday()!=null && visitData.getStatin_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Lipid_RxDecToday" value="InClassSwitch"
          <%=(visitData.getLipid_RxDecToday()!=null && visitData.getLipid_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
    </tr>
    <tr><td colspan=9 class="thinBottom"></td></tr>
    <tr>
      <td>
        Oral hypoglycemic<br />
        Insulin<br />
        ASA
      </td>
      <td>
        <input type="checkbox" name="Hypo_rx" value="Hypo_rx"
          <%=visitData.isHypo_rx()? "checked" :"" %> /> <br />
        <input type="checkbox" name="Insul_rx" value="Insul_rx"
          <%=visitData.isInsul_rx()? "checked" :"" %> /> <br />
        <input type="checkbox" name="ASA_rx" value="ASA_rx"
          <%=visitData.isASA_rx()? "checked" :"" %> />
      </td>
      <td class="td-solidRight">
        <input type="checkbox" name="Hypo_SideEffects" value="Hypo_SideEffects"
          <%=visitData.isHypo_SideEffects()? "checked" :"" %> /> <br />
        <input type="checkbox" name="Insul_SideEffects" value="Insul_SideEffects"
          <%=visitData.isInsul_SideEffects()? "checked" :"" %> />  <br />
        <input type="checkbox" name="ASA_SideEffects" value="ASA_SideEffects"
          <%=visitData.isASA_SideEffects()? "checked" :"" %> />
      </td>
      <td>
        <input type="radio" name="Hypo_RxDecToday" value="Same"
          <%=(visitData.getHypo_RxDecToday()!=null && visitData.getHypo_RxDecToday().equals("Same")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Insul_RxDecToday" value="Same"
          <%=(visitData.getInsul_RxDecToday()!=null && visitData.getInsul_RxDecToday().equals("Same")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="ASA_RxDecToday" value="Same"
          <%=(visitData.getASA_RxDecToday()!=null && visitData.getASA_RxDecToday().equals("Same")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
      <td>
        <input type="radio" name="Hypo_RxDecToday" value="Increase"
          <%=(visitData.getHypo_RxDecToday()!=null && visitData.getHypo_RxDecToday().equals("Increase")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Insul_RxDecToday" value="Increase"
          <%=(visitData.getInsul_RxDecToday()!=null && visitData.getInsul_RxDecToday().equals("Increase")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="ASA_RxDecToday" value="Increase"
          <%=(visitData.getASA_RxDecToday()!=null && visitData.getASA_RxDecToday().equals("Increase")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
      <td>
        <input type="radio" name="Hypo_RxDecToday" value="Decrease"
          <%=(visitData.getHypo_RxDecToday()!=null && visitData.getHypo_RxDecToday().equals("Decrease")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Insul_RxDecToday" value="Decrease"
          <%=(visitData.getInsul_RxDecToday()!=null && visitData.getInsul_RxDecToday().equals("Decrease")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="ASA_RxDecToday" value="Decrease"
          <%=(visitData.getASA_RxDecToday()!=null && visitData.getASA_RxDecToday().equals("Decrease")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
      <td>
        <input type="radio" name="Hypo_RxDecToday" value="Stop"
          <%=(visitData.getHypo_RxDecToday()!=null && visitData.getHypo_RxDecToday().equals("Stop")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Insul_RxDecToday" value="Stop"
          <%=(visitData.getInsul_RxDecToday()!=null && visitData.getInsul_RxDecToday().equals("Stop")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="ASA_RxDecToday" value="Stop"
          <%=(visitData.getASA_RxDecToday()!=null && visitData.getASA_RxDecToday().equals("Stop")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
      <td>
        <input type="radio" name="Hypo_RxDecToday" value="Start"
          <%=(visitData.getHypo_RxDecToday()!=null && visitData.getHypo_RxDecToday().equals("Start")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Insul_RxDecToday" value="Start"
          <%=(visitData.getInsul_RxDecToday()!=null && visitData.getInsul_RxDecToday().equals("Start")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="ASA_RxDecToday" value="Start"
          <%=(visitData.getASA_RxDecToday()!=null && visitData.getASA_RxDecToday().equals("Start")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
      <td>
        <input type="radio" name="Hypo_RxDecToday" value="InClassSwitch"
          <%=(visitData.getHypo_RxDecToday()!=null && visitData.getHypo_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="Insul_RxDecToday" value="InClassSwitch"
          <%=(visitData.getInsul_RxDecToday()!=null && visitData.getInsul_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
        <br />
        <input type="radio" name="ASA_RxDecToday" value="InClassSwitch"
          <%=(visitData.getASA_RxDecToday()!=null && visitData.getASA_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
          onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked" />
      </td>
    </tr>
  </table>
</td>
<!-- end of second column of bottom table -->


</tr>
</table>
<!-- end of bottom table -->

</DIV> <!--end of id_1 -->
       
      
<!-- control buttons -->
<table style="width: 766px">
  <tr>
    <td height="20" valign="top">
      <p align="right"><br></p>
      <p align="right">
        <input type="hidden" name="isFromRegistrationForm" value="false" /> 
        <input type="hidden" name="ID" value="<%=visitData.getId()%>" />        
        
        <input type="submit" name="Save" value="Save ">
        <!-- <input type="submit" name="Save" value="Save and Exit" />  -->
        
		<%String patient=patientData.getPatient_Id(); String user=(String)session.getAttribute("user");%> 
		<!--  
        <input type="button" onclick="popupXmlMsg('<html:rewrite action="/form/HSFOXmlTransfer2.do" />?xmlHsfoProviderNo=<%=user%>&xmlHsfoDemographicNo=<%=patient%>')"
                value="Assessment Complete" />
         -->       
        <input type="button" name="Reset" value="Reset" onClick="confirmReset(document.form1)" /> 
        <input  type="button" name="Close" value="Exit" onClick="confirmExit(document.form1)" />
        
        
      </p>
    </td>
  </tr>
</table>
</DIV>  <!--end of page_1 -->


</html:form>

</body>
</html:html>

