<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite" %>
<%@page import="oscar.oscarBilling.data.*,oscar.*"%>
<%@page import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*,oscar.oscarBilling.MSP.*" %>

<%@ include file="../../../admin/dbconnection.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" /> 



<%@ include file="dbBilling.jsp" %>

<%  
  if(session.getValue("user") == null)
    response.sendRedirect("../../../logout.htm");




  
  String curUser_no,userfirstname,userlastname;
  curUser_no = (String) session.getAttribute("user");
//  mygroupno = (String) session.getAttribute("groupno");  
 String UpdateDate = "";
 String DemoNo = "";
 String DemoName = "";
 String DemoAddress = "";
 String DemoCity="";
 String DemoProvince="";
 String DemoPostal="";
 String DemoDOB="";
 String DemoSex="";
 String hin=""; 
 String location="";
 String BillLocation="";
 String BillLocationNo="";
 String BillDate="";
 String Provider="";
 String BillType="";
 String BillTotal="";
 String visitdate="";
 String visittype="";
 String BillDTNo="";
 String HCTYPE="";
 String HCSex="";
 String r_doctor_ohip="";
 String r_doctor="";
 String m_review="";
  String specialty="";
 String r_status="";
  String roster_status="";
  String billRegion = OscarProperties.getInstance().getProperty("billRegion","BC");
 int rowCount = 0; 
 int rowReCount = 0;
  ResultSet rslocation = null;  
 ResultSet rsPatient = null; 
 ////
 BillingFormData billform = new BillingFormData();
BillingFormData.BillingPhysician[] billphysician = billform.getProviderList();
BillingFormData.BillingVisit[] billvisit = billform.getVisitType(billRegion);
BillingFormData.Location[] billlocation = billform.getLocationList(billRegion);
BillingFormData.BillingForm[] billformlist = billform.getFormList();
   int bFlag = 0;	
   String billNo = request.getParameter("billing_no");  
   if (billNo == null){
    billNo = (String) request.getAttribute("billing_no");  
   }   
   MSPReconcile msp = new MSPReconcile();
   Properties allFields = msp.getBillingMasterRecord(billNo);

  GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);  
%>

<!--  
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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */ 
-->

<html>
<head>
<title>oscarBillingBC Correction</title>
      <meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
      <meta http-equiv="Pragma" content="no-cache">
        <script language="JavaScript">

		function setfocus() {
		  document.form1.billing_no.focus();
		  document.form1.billing_no.select();
		}
		function rs(n,u,w,h,x) {
		  args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
		  remote=window.open(u,n,args);
		  if (remote != null) {
		    if (remote.opener == null)
		      remote.opener = self;
		  }
	 	  if (x == 1) { return remote; }
		}
	 	
		function popupPage2(varpage) {
                   var page = "" + varpage;
                   windowprops = "height=700,width=800,location=no,"
                   + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";
                   window.open(page, "<bean:message key="oscarEncounter.Index.popupPage2Window"/>", windowprops);
                }
		
	 	var awnd=null;
		
                function ScriptAttach(elementName ) {               
                    var d = elementName;
                    t0 = escape(document.ReProcessBilling.elements[d].value);                  
                    t1 = escape("");
                    t2 = escape("");
                    awnd=rs('att','<rewrite:reWrite jspPage="billingDigNewSearch.jsp"/>?name='+t0 + '&name1=' + t1 + '&name2=' + t2 + '&search=&formElement=' +d+ '&formName=ReProcessBilling',600,600,1);
                    awnd.focus();
                }

                function OtherScriptAttach() {
                      t0 = escape(document.ReProcessBilling.service_code.value);
                      t1 = escape("");
                      t2 = escape("");
                      awnd=rs('att','<rewrite:reWrite jspPage="billingCodeNewSearch.jsp"/>?name='+t0 + '&name1=' + t1 + '&name2=' + t2 + '&search=&formName=ReProcessBilling&formElement=service_code',600,600,1);                      
                      awnd.focus();
                }

                function ReferralScriptAttach(elementName) {                   
                     var d = elementName;                     
                     t0 = escape(document.ReProcessBilling.elements[d].value);
                     t1 = escape("");    
                     awnd=rs('att','<rewrite:reWrite jspPage="billingReferCodeSearch.jsp"/>?name='+t0 + '&name1=' + t1 + '&name2=&search=&formElement=' +d+ '&formName=ReProcessBilling',600,600,1);
                     awnd.focus();
                }




    </script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<style type="text/css">	
        td.bCellData{ font-weight:bold; font-family: Arial,Helvetica,sans-serif; }
        th.bHeaderData{ font-weight:bold; font-family: Arial,Helvetica,sans-serif; }
</style> 
</head>

         

<body bgcolor="#FFFFFF" text="#000000" topmargin="5"  leftmargin="0" rightmargin="0" onLoad="setfocus()">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr bgcolor="#000000"> 
    <td height="40" width="10%"> </td>
    <td width="90%" align="left"> 
      <p><font face="Verdana, Arial, Helvetica, sans-serif" color="#FFFFFF"><b><font face="Arial, Helvetica, sans-serif" size="4">oscar<font size="3">Billing - Correction</font></font></b></font> 
      </p>
    </td>
  </tr>
</table>
    <%if (allFields == null){
    /////////////////////////////////////////////////////////////////////////
    %>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr > 
                <td height="40" width="10%">No Record Found </td>                                        
            </tr>
        </table>    
    </body>
    </html>

    <%///////////////////////////////////////////////////////////////////////
    return;
    }%>
  <table width="100%" border="0" bgcolor="#FFFFFF">  
    <tr> 
      <td width="20%" align="left"  class="bCellData"><font color="#000000">
         Office Claim No
      </td>
      <td width="20%"  class="bCellData">
         <%=billNo%>
      </td>
      <td width="60%" align="left"  class="bCellData">
         <font color="#000000">Last update: <%=UpdateDate%></font>
      </td>
    </tr>
  
   </table>

<SCRIPT Language="Javascript">
function printBill(){
if (window.print) {
    window.print() ;
} else {
    var WebBrowser = '<OBJECT ID="WebBrowser1" WIDTH=0 HEIGHT=0 CLASSID="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2"></OBJECT>';
document.body.insertAdjacentHTML('beforeEnd', WebBrowser);
    WebBrowser1.ExecWB(6, 2);//Use a 1 vs. a 2 for a prompting dialog box    WebBrowser1.outerHTML = "";
}
}
</script>
    <table>    
    <%
        ArrayList li = msp.getAllC12Records(billNo);        
        if (li.size() == 0 ){
            li = msp.getAllS00Records(billNo);        
        }
        for ( int i = 0; i < li.size(); i++){ 
        String rejReason = (String) li.get(i);
    %>
        <tr>
            <td><%=rejReason%></td>
        </tr>
        
    <% }%>            
    </table>

<%


 rslocation = null;
 rslocation = apptMainBean.queryResults(billNo, "search_teleplanbill");
 while(rslocation.next()){ 
    DemoNo = rslocation.getString("demographic_no");
    DemoName = rslocation.getString("demographic_name");
    UpdateDate = rslocation.getString("update_date");
    BillDate = rslocation.getString("billing_date");
    BillType = rslocation.getString("status");
    Provider = rslocation.getString("provider_no");
    visitdate = rslocation.getString("visitdate");
    visittype = rslocation.getString("visittype");
 }
 BillType = allFields.getProperty("billingstatus");
 rsPatient = null;
 rsPatient = apptMainBean.queryResults(DemoNo, "search_demographic_details");
 while(rsPatient.next()){
     
     DemoSex = rsPatient.getString("sex");
     DemoAddress = rsPatient.getString("address");
     DemoCity = rsPatient.getString("city");
     DemoProvince = rsPatient.getString("province");
     DemoPostal = rsPatient.getString("postal");
     DemoDOB = MyDateFormat.getStandardDate(Integer.parseInt(rsPatient.getString("year_of_birth")),Integer.parseInt(rsPatient.getString("month_of_birth")),Integer.parseInt(rsPatient.getString("date_of_birth")));
     hin = rsPatient.getString("hin") + rsPatient.getString("ver");
     if (rsPatient.getString("family_doctor") == null){ r_doctor = "N/A"; r_doctor_ohip="000000";}else{
        r_doctor=SxmlMisc.getXmlContent(rsPatient.getString("family_doctor"),"rd")==null?"":SxmlMisc.getXmlContent(rsPatient.getString("family_doctor"),"rd");
        r_doctor_ohip=SxmlMisc.getXmlContent(rsPatient.getString("family_doctor"),"rdohip")==null?"":SxmlMisc.getXmlContent(rsPatient.getString("family_doctor"),"rdohip");
     }
     
     HCTYPE = rsPatient.getString("hc_type")==null?"":rsPatient.getString("hc_type");
     if (DemoSex.equals("M")) HCSex = "1";
     if (DemoSex.equals("F")) HCSex = "2";
     roster_status = rsPatient.getString("roster_status");
 }

%>

  <br><html:form  action="/billing/CA/BC/reprocessBill">
<input type="hidden" name="update_date" value="<%=UpdateDate%>"/>
<input type="hidden" name="demoNo" value="<%=DemoNo%>"/>
<input type="hidden" name="billNumber" value="<%=allFields.getProperty("billing_no")%>"/>
<table width="600" border="0">
  <tr bgcolor="#CCCCFF"> 
     <td height="21" colspan="2" class="bCellData">Patient Information<input type="hidden" name ="billingmasterNo" value="<%=billNo%>" /></td>
    
  </tr>
  <tr> 
    <td width="54%"  class="bCellData">
        Patient Name:
        <a href=# onClick="popupPage2('../../../demographic/demographiccontrol.jsp?demographic_no=<%=DemoNo%>&displaymode=edit&dboperation=search_detail');return false;" title="<bean:message key="provider.appointmentProviderAdminDay.msgMasterFile"/>">
        <%=DemoName%> 
        </a>
        <input type="hidden" name="demo_name" value="<%=DemoName%>">
    </td>
    <td width="46%"  class="bCellData">Health# : 
      <%=allFields.getProperty("phn")%>
      Type
      <%=HCTYPE%>
    </td>
  </tr>
  <tr bgcolor="#EEEEFF">  
    <td  class="bCellData">
      Sex: <%=DemoSex%> 
      <input type="hidden" name="demo_sex" value="<%=DemoSex%>">
      <input type="hidden" name="hc_sex" value="<%=HCSex%>">
    </td>
    <td  class="bCellData">
      D.O.B. : <%=DemoDOB%>
      <input type="hidden" name="xml_dob" value="<%=DemoDOB%>">
      
    </td>  
  </tr>
  <tr> 
    <td  class="bCellData">
       Address: <%=DemoAddress%> 
       <input type="hidden" name="demo_address" value="<%=DemoAddress%>">
    </td>
    <td  class="bCellData">
       City: <%=DemoCity%> 
       <input type="hidden" name="demo_city" value="<%=DemoCity%>">
    </td>
  </tr>
  <tr bgcolor="#EEEEFF"> 
    <td  class="bCellData">
       Province: <%=DemoProvince%> 
       <input type="hidden" name="demo_province" value="<%=DemoProvince%>">
    </td>
    <td  class="bCellData">
       Postal Code: <%=DemoPostal%> 
       <input type="hidden" name="demo_postal" value="<%=DemoPostal%>">
    </td>
  </tr>
</table>


<table width="600" border="0">
  <tr bgcolor="#CCCCFF"> 
    <td colspan="2"  class="bCellData">
       Billing Information  Data Center <%=allFields.getProperty("datacenter")%> Payee Number: <%=allFields.getProperty("payee_no")%>
     </td>
  </tr>

  <tr>    
    <td   class="bCellData">
       Billing Type: 
      <input type="hidden" name="xml_status" value="<%=BillType%>">
      <select style="font-size:80%;" name="status">
                <option value="">--- Select Bill Type ---</option>
      		<!--<option value="H" <%=BillType.equals("H")?"selected":""%>>H | Capitated</option>-->
                <option value="O" <%=BillType.equals("O")?"selected":""%>>O | Bill MSP</option>
                <option value="P" <%=BillType.equals("P")?"selected":""%>>P | Bill Patient</option>
                <option value="N" <%=BillType.equals("N")?"selected":""%>>N | Do Not Bill</option>
                <option value="W" <%=BillType.equals("W")?"selected":""%>>W | Bill Worker's Compensation Board</option>
                <option value="B" <%=BillType.equals("B")?"selected":""%>>B | Summitted MSP</option>
                <option value="S" <%=BillType.equals("S")?"selected":""%>>S | Settled/Paid by MSP</option>
                <option value="X" <%=BillType.equals("X")?"selected":""%>>X | Bad Debt</option>
                <option value="D" <%=BillType.equals("D")?"selected":""%>>D | Deleted Bill</option>          

                <option value="R" <%=BillType.equals("R")?"selected":""%>>R | Rejected Bill</option>          
                <option value="Z" <%=BillType.equals("Z")?"selected":""%>>Z | Held Bill</option>          
                <option value="C" <%=BillType.equals("C")?"selected":""%>>C | Data Center Changed</option>                            
                <option value="E" <%=BillType.equals("E")?"selected":""%>>E | Paid With Explanation</option>          
                <option value="F" <%=BillType.equals("F")?"selected":""%>>F | Refused Bill</option>          
    
    
    
    
    
    
      </select>
    </td>
    <td   class="bCellData">
        <table>
            <tr>
                <td>
              <a href="#" onClick='rs("billingcalendar","billingCalendarPopup.jsp?year=<%=curYear%>&month=<%=curMonth%>&type=&returnForm=serviceform&returnItem=xml_appointment_date","380","300","0")'>
              Billing Date: 
              </a>
                </td>
                <td>
                    To: 
                </td>
            </tr>
            <tr>
                <td>
                <input type="text" style="font-size:80%;"  name="serviceDate" value="<%=BillDate%>"><!--<%=allFields.getProperty("service_date")%>"/>-->
                </td>
                <td>
                <input type="text" name="serviceToDay" value="<%=allFields.getProperty("service_to_day")%>" size="2" maxlength="2"/>
                </td>
            </tr>
        </table>
    </td>
  </tr>
  <tr bgcolor="#EEEEFF"> 
    <td width="54%"  class="bCellData">
        Visit:          
        <select style="font-size:80%;" name="locationVisit">
           <option value="">--- Select Visit Location ---</option>
           <% for (int i=0; i< billlocation.length; i++){
              String locationDescription = billlocation[i].getBillingLocation()+"|"+billlocation[i].getDescription();
              String selection = "";
              if (allFields.getProperty("clarification_code").equals(billlocation[i].getBillingLocation())){ selection = "selected" ; }
           %>
           <option value="<%=locationDescription%>" <%=selection%> ><%=billlocation[i].getDescription()%></option>
           <% }%>
        </select>   
    </td>
    <td width="46%"  class="bCellData">
        Billing Physician#: 
        <select style="font-size:80%;" name="provider_no">
            <option value="">--- Select Provider ---</option>
            <% ResultSet rslocal = null;  
               // Retrieving Provider
               String proFirst="", proLast="", proOHIP="", proNo="";
               int Count = 0;
               rslocal = null;
               rslocal = apptMainBean.queryResults("%", "search_provider_dt");
               while(rslocal.next()){
                    proFirst = rslocal.getString("first_name");
                    proLast = rslocal.getString("last_name");
                    proOHIP = rslocal.getString("provider_no");

            %>
            <option value="<%=proOHIP%>" <%=Provider.equals(proOHIP)?"selected":""%>> <%=proOHIP%> | <%=proLast%>, <%=proFirst%></option>
            <% }%>
            <input type="hidden" name="xml_provider_no" value="<%=Provider%>">
    </td>
  </tr>
  <tr> 
   <%visittype = allFields.getProperty("service_location");%>
    <td width="54%"  class="bCellData">
        Visit Type: 
        <input type="hidden" name="xml_visittype" value="<%=visittype%>">    
            <select style="font-size:80%;" name="serviceLocation">
                 <option value="">--- Select Visit Type ---</option>
      		 <option value="O|Physician's office" <%=visittype.equals("O")?"selected":""%>>Physician's office</option>
		 <option value="C|Continuing Care facility" <%=visittype.equals("C")?"selected":""%>>Continuing Care facility</option>
		 <option value="H|Hospital" <%=visittype.equals("H")?"selected":""%>>Hospital</option>
		 <option value="I|Hospital Inpatient" <%=visittype.equals("I")?"selected":""%>>Hospital Inpatient</option>
		 <option value="E|Hospital Emergency Depart. or Diagnostic & Treatment Centre" <%=visittype.equals("E")?"selected":""%>>Hospital Emergency Depart. or Diagnostic & Treatment Centre</option>
		 <option value="P|Outpatient" <%=visittype.equals("P")?"selected":""%>>Outpatient</option>
		 <option value="D|Diagnostic Facility" <%=visittype.equals("D")?"selected":""%>>Diagnostic Facility</option>
		 <option value="S|Future Use" <%=visittype.equals("S")?"selected":""%>>Future Use</option>
		 <option value="Z|None of the above" <%=visittype.equals("Z")?"selected":""%>>None of the above</option>      		
      	    </select>
    </td>
    <td width="46%"  class="bCellData">
        <input type="hidden" name="xml_visitdate" value="<%=visitdate%>">
        <a href="#" onClick='rs("billingcalendar","billingCalendarPopup.jsp?year=<%=curYear%>&month=<%=curMonth%>&type=&returnForm=serviceform&returnItem=xml_vdate","380","300","0")'> 
            Admission Date:
        </a> 
        <input type="text" style="font-size:80%;" name="xml_vdate" value="<%=visitdate%>">
    </td>
  </tr> 
  <tr>
    <td class="bCellData">Dependent Number: 
        <select name="dependentNo" >
            <option value="00" <%=allFields.getProperty("dependent_num").equals("00")?"selected":""%>>00</option> 
            <option value="66" <%=allFields.getProperty("dependent_num").equals("66")?"selected":""%>>66</option>
        </select>        
    </td>    
    <td class="bCellData">New Program Ind:
        <input type="text" name="newProgram" value="<%=allFields.getProperty("new_program")%>" size="2" maxlength="2" />   
    </td>
  </tr>  
  <tr>
   <td class="bCellData">After Hours:
        <select name="afterHours" >
            <option value="0" <%=allFields.getProperty("after_hour").equals("0")?"selected":""%>>NO</option> 
            <option value="E" <%=allFields.getProperty("after_hour").equals("E")?"selected":""%>>Evening Call</option>
            <option value="N" <%=allFields.getProperty("after_hour").equals("N")?"selected":""%>>Night Call</option>
            <option value="W" <%=allFields.getProperty("after_hour").equals("W")?"selected":""%>>Weekend Call</option>
        </select>        
    </td>
    <td class="bCellData">Time Call Recieved<!--TIME-CALL-RECVD-SRV-->
       <input type="text" name="timeCallRec" value="<%=allFields.getProperty("time_call")%>" size="4" maxlength="4"/>
       <input type="hidden" name="anatomicalArea" value="<%=allFields.getProperty("anatomical_area")%>" />
    </td>
  </tr>  

       <tr>

            <td class="bCellData">Service Time Start<%! /*SERVICE-TIME-START*/ %>
            <input type="text" name="startTime" value="<%=allFields.getProperty("service_start_time")%>" size="4" maxlength="4"/></td>        

            <td class="bCellData">Service Time Finish <%! /*SERVICE-TIME-FINISH*/ %>
            <input type="text" name="finishTime" value="<%=allFields.getProperty("service_end_time")%>" size="4" maxlength="4"/></td>
        
       </tr>
       <tr>
           <td class="bCellData">MVA 
                <select name="mvaClaim" >
                    <option value="N" <%=allFields.getProperty("mva_claim_code").equals("N")?"selected":""%>>No</option> 
                    <option value="Y" <%=allFields.getProperty("mva_claim_code").equals("Y")?"selected":""%>>Yes</option>                                
                </select>        
              <!--<input type="text" name="mvaClaim" value="<%=allFields.getProperty("mva_claim_code")%>" size="1"/>-->
            </td>
            <td class="bCellData">ICBC Claim Num:
            <input type="text" name="icbcClaim" value="<%=allFields.getProperty("icbc_claim_no")%>"size="8" maxlength="8"/></td>                                         
       </tr>
</table>
  
<table width="700" border=1>
  <tr bgcolor="#CCCCFF"> 
    <td width="25%"  class="bCellData">Service Code</td>      
    <td width="50%"  class="bCellData">Description</td>         
    <td width="12%"  class="bCellData">Unit</td>     
    <td width="13%"  class="bCellData"> 
      <div align="right">$ Fee</div>
    </td>
  </tr>
    
    <tr> 
      <td width="25%"  class="bCellData">
        <input type="button" onclick="javascript:OtherScriptAttach()" value="Search"/>
        <input type="text" style="font-size:80%;" name="service_code" value="<%=allFields.getProperty("billing_code")%>" size="10" >
      </td>
      <td width="50%"  class="bCellData">
        <%=billform.getServiceDesc(allFields.getProperty("billing_code"),billRegion)%>
      </td>
      <td width="12%" class="bCellData">
        <input type="hidden" name="billing_unit" value="<%=allFields.getProperty("billing_unit")%>">
        <input type="text" style="font-size:80%;" name="billingunit" value="<%=allFields.getProperty("billing_unit")%>" size="3" maxlength="3">
      </td>
      <td width="13%"  class="bCellData"> 
        <div align="right">
           <input type="hidden" name="billing_amount" value="<%=allFields.getProperty("bill_amount")%>">
           <input type="text" style="font-size:80%;" size="5" maxlength="5" name="billingamount" value="<%=allFields.getProperty("bill_amount")%>">
        </div>
      </td>
    </tr>
 </table>
 <table width="700" border=1>
  <%
 
   apptMainBean.closePstmtConn();
   %>
 
    <tr>
        <td colspan=2 width="75%">
            <table width="100%">
                <tr bgcolor="#CCCCFF"> 
                  <td  colspan=2 class="bCellData">
                    Diagnostic Code
                  </td>
                  

                </tr>
                <tr> 
                  <td class="bCellData">
                    <a href="javascript:ScriptAttach('dx1')">DX 1</a><input type="text" name="dx1" value="<%=allFields.getProperty("dx_code1")%>" size="10">                      
                  </td>
                  <td><%=billform.getDiagDesc(allFields.getProperty("dx_code1"),billRegion)%></td>
                </tr>
                <tr> 
                  <td   class="bCellData">
                    <a href="javascript:ScriptAttach('dx2')">DX 2</a><input type="text" name="dx2" value="<%=allFields.getProperty("dx_code2")%>" size="10">            
                  </td>
                  <td><%=billform.getDiagDesc(allFields.getProperty("dx_code2"),billRegion)%></td>
                </tr>
                <tr> 
                  <td class="bCellData">
                    <a href="javascript:ScriptAttach('dx3')">DX 3</a><input type="text" name="dx3" value="<%=allFields.getProperty("dx_code3")%>" size="10">                    
                  </td>
                  <td><%=billform.getDiagDesc(allFields.getProperty("dx_code3"),billRegion)%></td>
                </tr>
              </table>
       </td>
       <td colspan=2 valign="top">
              <table width="100%">
                <tr bgcolor="#CCCCFF"> 
                  <td colspan=2 class="bCellData">
                    Referrals
                    <% String  refCD1 = allFields.getProperty("referalPracCD1");
                     String  refCD2 = allFields.getProperty("referalPracCD2");
                     if (refCD1 == null || refCD1.equals("null")){ refCD1 = "0"; }
                     if (refCD2 == null || refCD2.equals("null")){ refCD2 = "0"; }
                  %>
                  </td>
                  
                </tr>
                <tr>
                    <td class="bCellData">1.
                    <select name="referalPracCD1" >
                        
                        <option value="0" <%=refCD1.equals("0")?"selected":""%>>None</option> 
                        <option value="T" <%=refCD1.equals("T")?"selected":""%>>TO</option>
                        <option value="B" <%=refCD1.equals("B")?"selected":""%>>BY</option>                        
                    </select>        
                    </td>
                    <td class="bCellData">
                        <input type="button" onclick="javascript:ReferralScriptAttach('referalPrac1')" value="Search"/>
                        <input type="text" name="referalPrac1" value="<%=allFields.getProperty("referral_no1")%>" size="5" maxlength="5"/>
                    </td>            
                </tr>
                <tr>
                    <td class="bCellData">2.
                    <select name="referalPracCD2" >
                        <option value="0" <%=refCD2.equals("0")?"selected":""%>>None</option> 
                        <option value="T" <%=refCD2.equals("T")?"selected":""%>>TO</option>
                        <option value="B" <%=refCD2.equals("B")?"selected":""%>>BY</option>                        
                    </select>        
                    </td>
                    <td class="bCellData">
                        <input type="button" onclick="javascript:ReferralScriptAttach('referalPrac2')" value="Search"/>
                        <input type="text" name="referalPrac2" value="<%=allFields.getProperty("referral_no2")%>" size="5" maxlength="5"/>
                    </td>            
                </tr>       
              </table>
       </td> 
    </tr>
        <tr>
            <td class="bCellData">Payment Mode</td><!--PAYMENT MODE-->
            <td class="bCellData">
                <select name="paymentMode" >
                    <option value="0" <%=allFields.getProperty("payment_mode").equals("0")?"selected":""%>>Fee For Service</option> 
                    <option value="E" <%=allFields.getProperty("payment_mode").equals("E")?"selected":""%>>Alternate Funding</option>            
                </select>        
            </td>
            <td class="bCellData">Submission Code</td><!--SUBMISSION-CODE-->
            <td class="bCellData">
                <select name="submissionCode" >
                    <option value="0" <%=allFields.getProperty("submission_code").equals("0")?"selected":""%>>Normal Submission</option> 
                    <option value="D" <%=allFields.getProperty("submission_code").equals("D")?"selected":""%>>Duplicate</option>            
                    <option value="E" <%=allFields.getProperty("submission_code").equals("E")?"selected":""%>>Debit Request</option>            
                    <option value="I" <%=allFields.getProperty("submission_code").equals("I")?"selected":""%>>ICBC Claim</option>            
                    <option value="R" <%=allFields.getProperty("submission_code").equals("I")?"selected":""%>>Resubmit Claim</option>            
                    <option value="A" <%=allFields.getProperty("submission_code").equals("I")?"selected":""%>>Pre-approved claim</option>            
                    <option value="X" <%=allFields.getProperty("submission_code").equals("I")?"selected":""%>>Resubmitting refused or part paid</option>            
                </select>                        
            </td>
       </tr> 
       <!--<tr>
            <td>Service Date</td><%/*SERVICE-DATE*/%>
            <td><input type="text" name="serviceDate" value="<%=allFields.getProperty("service_date")%>"/></td>
            <td>Service to Day</td><%/*SERVICE-TO-DAY*/%>
            <td<input type="text" name="serviceToDay" value="<%=allFields.getProperty("service_to_day")%>"/></td>
       </tr>-->
       <!--           
       <tr>
            <td>Time Call Recieved</td><%!/*TIME-CALL-RECVD-SRV*/%>
            <td><input type="text" name="timeCallRec" value="<%=allFields.getProperty("time_call")%>" size="4"/></td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
       </tr>
       <tr>

            <td>Service Time Start</td><%! /*SERVICE-TIME-START*/ %>
            <td><input type="text" name="startTime" value="<%=allFields.getProperty("service_start_time")%>" size="4"/></td>        

            <td>Service Time Finish</td><%! /*SERVICE-TIME-FINISH*/ %>
            <td><input type="text" name="finishTime" value="<%=allFields.getProperty("service_end_time")%>" size="4"</td>
        
       </tr>
       -->
       <tr>

       <tr>

            <td class="bCellData">Correspondence Code</td><!--CORRESPONDENCE-CODE-->                
            <td class="bCellData">
                <select name="correspondenceCode" >
                    <option value="0" <%=allFields.getProperty("correspondence_code").equals("0")?"selected":""%>>None</option> 
                    <option value="C" <%=allFields.getProperty("correspondence_code").equals("C")?"selected":""%>>Paper Note</option>            
                    <option value="N" <%=allFields.getProperty("correspondence_code").equals("C")?"selected":""%>>Elec Note</option>            
                    <option value="B" <%=allFields.getProperty("correspondence_code").equals("C")?"selected":""%>>Both</option>            
                </select>        
                <!--<input type="text" name="correspondenceCode" value="<%=allFields.getProperty("correspondence_code")%>" size="1"/>-->
            </td>
            <td class="bCellData">Insurer Code</td><!--OIN-INSURER-C0DE-->
            <td class="bCellData">
                <select name="insurerCode2" >
                    <option value="" <%=allFields.getProperty("oin_insurer_code").equals("0")?"selected":""%>>None</option>                     
                    <option value="IN" <%=allFields.getProperty("oin_insurer_code").equals("C")?"selected":""%>>Institutional Claim</option>            
                    <option value="PP" <%=allFields.getProperty("oin_insurer_code").equals("C")?"selected":""%>>Pay Patient</option>            
                    <option value="WC" <%=allFields.getProperty("oin_insurer_code").equals("C")?"selected":""%>>WCB</option>            
                </select>                    
            </td>            
       </tr>
       <tr>     
            <td class="bCellData">Claim Short Comment</td><!--CLAIM-SHORT-COMMENT-->
            <td><input type="text" name="shortComment" value="<%=allFields.getProperty("claim_comment")%>"size="20" maxlength="20"/></td>
            
       </tr>      
       <!--<tr>
            <td>Facility Num</td><%! /*FACILITY-NUM*/ %>
            <td><input type="text" name="facilityNum" value="<%=allFields.getProperty("facility_no")%>" size="5"/></td>            
            <td>Facility Sub Num</td><%! /*FACILITY-SUB-NUM*/%>
            <td><input type="text" name="facilitySubNum" value="<%=allFields.getProperty("facility_sub_no")%>" size="5"/></td>
       </tr>-->
       
       <!--<tr>
            
                 
            <td>Registration Num</td><%!/*OIN-REGISTRATION-NUM*/%>
            <td><input type="text" name="registrationNum" value="<%=allFields.getProperty("oin_registration_no")%>" size="12"/></td>
       </tr>-->
       <!--
       <tr>
            <td>First Name</td><%/*OIN-FIRST-NAME*/%>
            <td><input type="text" name="firstName" value="<%=allFields.getProperty("oin_first_name")%>" size="12"/></td>
      
            <td>Surname</td><%/*OIN-SURNAME*/%>
            <td><input type="text" name="surname" value="<%=allFields.getProperty("oin_surname")%>" size="18"/></td>            
       </tr>
       <tr>
            <td>SEX</td>
            <td><input type="text" name="sex" value="<%=allFields.getProperty("oin_sex_code")%>" size="1"/></td>            
            <td>Birth date</td><%/*OIN-BIRTHDATE*/%>
            <td><input type="text" name="birthdate" value="<%=allFields.getProperty("oin_birthdate")%>" size="8"/></td>
       </tr>
       <tr>
            
            <td>Address 1 WCB Date Of Injury</td><%/*OIN-ADDRESS-1		WCB DATE OF INJURY*/%>
            <td colspan="3"><input type="text" name="address1" value="<%=allFields.getProperty("oin_address")%>" size="25"/></td>
       </tr>
       <tr>
            <td>Address 2 WCB AREA OF INJURY</td><%/*OIN-ADDRESS-2 WCB AREA OF INJURY ANATOMICAL-POSITION*/%>
            <td colspan="3"><input type="text" name="address2" value="<%=allFields.getProperty("oin_address2")%>" size="25"/></td>
       </tr>
       <tr>
            <Td>Address 3 WCB NATURE OF INJURY</td><%/*OIN-ADDRESS-3 WCB NATURE OF INJURY*/%>
            <td colspan="3"><input type="text" name="address3" value="<%=allFields.getProperty("oin_address3")%>" size="25" /></td>
       </tr>
       <tr>            
            <td>Address 4 WCB Claim Number</td><%/*OIN-ADDRESS-4 WCB CLAIM NUMBER*/%>
            <td colspan="3"><input type="text" name="address4" value="<%=allFields.getProperty("oin_address4")%>" size="25" /></td>
       </tr>
       <tr>
            <td>Postal Code</td><%/*OIN-POSTAL-CODE*/%>
            <td colspan="3"><input type="text" name="postalCode" value="<%=allFields.getProperty("oin_postalcode")%>" size="6"/></td>
        </tr>
-->
       
       


        <tr> 
          <td colspan="4"  class="bCellData">
            <input type="submit" name="submit" value="Reprocess Bill">
            <input type="submit" name="submit" value="Resubmit Bill">
            <input type="submit" name="submit" value="Settle Bill">
            
          </td>
       </tr>

       

  </table>
  </html:form>

  <div style="visibility: hidden;">
    <table border=1 >
        <tr>
            <td>Name</td>
            <td>VAlue</td>
            <td>size</td>   
            <td>shown</td>            
        </tr>
        <tr>
            <!--REC-CODE-IN-->
            <td>Data-Center</td><!--DATA-CENTRE-NUM-->
            <td><%=allFields.getProperty("datacenter")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>
	
            <!--DATA-CENTER-SEQNUM-->
            <td>PAYEE-NUM</td><!--PAYEE-NUM-->
            <td><%=allFields.getProperty("payee_no")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Practitioner Number</td><!--PRACTITIONER-NUM-->
            <td><%=allFields.getProperty("practitioner_no")%></td><!--MSP PHN-->
            <td>5</td>
            <td> </td>
       </tr>
       <tr>
            <td>PHN</td><!--PRACTITIONER-NUM-->
            <td><%=allFields.getProperty("phn")%></td><!--MSP PHN-->
            <td>10</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Name Verify</td><!--NAME-VERIFY-->
            <td><%=allFields.getProperty("name_verify")%></td>
            <td>4</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Dependent Number</td><!--DEPENDENT-NUM-->
            <td><%=allFields.getProperty("dependent_num")%></td>
            <td>2</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Billed Units</td><!--BILLED-SRV-UNITS-->
            <td><%=allFields.getProperty("billing_unit")%></td>
            <td>3</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Service Clarification code</td><!--SERVICE CLARIFICATION CODE-->
            <Td><%=allFields.getProperty("clarification_code")%></td>
            <td>2</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Anatomical Area</td><!--MSP SERVICE ANATOMICAL AREA-->
            <td><%=allFields.getProperty("anatomical_area")%></td>
            <td>2</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>After Hours</td><!--AFTER HOURS SERVICE IND-->
            <td><%=allFields.getProperty("after_hour")%></td>
            <td>1</td>
            <td>Y</td>
       </tr>
       <tr>
            
            <td>New Program Ind</td><!--NEW PROGRAM IND-->
            <td><%=allFields.getProperty("new_program")%></td>
            <td>2</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Billed Fee Item</td><!--BILLED-FEE-ITEM-->
            <td><%=allFields.getProperty("billing_code")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Billed Amount</td><!--BILLED-AMOUNT-->
            <td><%=allFields.getProperty("bill_amount")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Payment Mode</td><!--PAYMENT MODE-->
            <td><%=allFields.getProperty("payment_mode")%></td>
            <td>1</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Service Date</td><!--SERVICE-DATE-->
            <td><%=allFields.getProperty("service_date")%></td>
            <td>8</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Service to Day</td><!--SERVICE-TO-DAY-->
            <td><%=allFields.getProperty("service_to_day")%></td>
            <td>2</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Submission Code</td><!--SUBMISSION-CODE-->
            <td><%=allFields.getProperty("submission_code")%></td>
            <td>1</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Ex Submission Code</td><!--EXTENDED SUBMISSION CODE-->
            <td><%=allFields.getProperty("extended_submission_code")%></td>
            <td>1</td>
            <td> </td>
       </tr>
       <tr>

            <td>Diag Code 1</td><!--DIAGNOSTIC-CODE-1-->
            <td><%=allFields.getProperty("dx_code1")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Diag Code 2</td><!--DIAGNOSTIC-CODE-2-->
            <td><%=allFields.getProperty("dx_code2")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Diag Code 3</td><!--DIAGNOSTIC-CODE-3-->
            <td><%=allFields.getProperty("dx_code3")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Diag Expansion</td><!--DIAGNOSTIC EXPANSION-->
            <td><%=allFields.getProperty("dx_expansion")%></td>
            <td>15</td>
            <td> </td>
       </tr>
       <tr>

            <td>Service Location</td><!--SERVICE-LOCATION-CD-->
            <td><%=allFields.getProperty("service_location")%></td>
            <td>1</td>
            <td> </td>
       </tr>
       <tr>

            <td>Referal Practitioner CD</td><!--REF-PRACT-1-CD-->
            <Td><%=allFields.getProperty("referral_flag1")%></td>
            <td>1</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Referal Practitioner</td><!--REF-PRACT-1-->
            <td><%=allFields.getProperty("referral_no1")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Referal Practitioner CD</td><!--REF-PRACT-2-CD-->
            <Td><%=allFields.getProperty("referral_flag2")%></td>
            <td>1</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Referal Practitioner</td><!--REF-PRACT-2-->
            <td><%=allFields.getProperty("referral_no2")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Time Call Recieved</td><!--TIME-CALL-RECVD-SRV-->
            <td><%=allFields.getProperty("time_call")%></td>
            <td>4</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Service Time Start</td><!--SERVICE-TIME-START-->
            <td><%=allFields.getProperty("service_start_time")%></td>
            <td>4</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Service Time Finish</td><!--SERVICE-TIME-FINISH-->
            <td><%=allFields.getProperty("service_end_time")%></td>
            <td>4</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Birth Date</td><!--BIRTH-DATE-->
            <td><%=allFields.getProperty("birth_date")%></td>
            <td>8</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Office Number</td><!--OFFICE-FOLIO-NUMBER-->
            <td><%=allFields.getProperty("office_number")%></td>
            <td>7</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Correspondence Code</td><!--CORRESPONDENCE-CODE-->
            <td><%=allFields.getProperty("correspondence_code")%></td>
            <td>1</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Claim Short Comment</td><!--CLAIM-SHORT-COMMENT-->
            <td><%=allFields.getProperty("claim_comment")%></td>
            <td>20</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>MVA Claim Code</td><!--MVA-CLAIM-CODE-->
            <td><%=allFields.getProperty("mva_claim_code")%></td>
            <td>1</td>
            <td>Y</td>
       </tr>
       <tr>
            
            <td>ICBC Claim Num</td><!--ICBC-CLAIM-NUM-->
            <td><%=allFields.getProperty("icbc_claim_no")%></td>
            <td>8</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>MSP File Num</td><!--ORG-MSP-FILE-NUM-->
            <td><%=allFields.getProperty("original_claim")%></td>
            <td>20</td>
            <td> </td>
       </tr>
       <tr>
            <td>Facility Num</td><!--FACILITY-NUM-->facility_no
            <td><%=allFields.getProperty("facility_no")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Facility Sub Num</td><!--FACILITY-SUB-NUM-->
            <td><%=allFields.getProperty("facility_sub_no")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Insurer Code</td><!--OIN-INSURER-C0DE-->
            <td><%=allFields.getProperty("oin_insurer_code")%></td>
            <td>2</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Registration Num</td><!--OIN-REGISTRATION-NUM-->
            <td><%=allFields.getProperty("oin_registration_no")%></td>
            <td>12</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Birth date</td><!--OIN-BIRTHDATE-->
            <td><%=allFields.getProperty("oin_birthdate")%></td>
            <td>8</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>First Name</td><!--OIN-FIRST-NAME-->
            <td><%=allFields.getProperty("oin_first_name")%></td>
            <td>12</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Second Name</td><!--OIN-SECOND-NAME-INITIAL-->
            <td><%=allFields.getProperty("oin_second_name")%></td>
            <td>1</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Surname</td><!--OIN-SURNAME-->
            <td><%=allFields.getProperty("oin_surname")%></td>
            <td>18</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>SEX</td>
            <td><%=allFields.getProperty("oin_sex_code")%></td>
            <td>1</td>
            <td>Y</td>
       </tr>
       <tr>
            
            <td>Address 1 WCB Date Of Injury</td><!--OIN-ADDRESS-1		WCB DATE OF INJURY-->
            <td><%=allFields.getProperty("oin_address")%></td>
            <td>25</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Address 2 WCB AREA OF INJURY</td><!--OIN-ADDRESS-2 WCB AREA OF INJURY ANATOMICAL-POSITION-->
            <td><%=allFields.getProperty("oin_address2")%></td>
            <td>25</td>
            <td>Y</td>
       </tr>
       <tr>

            <Td>Address 3 WCB NATURE OF INJURY</td><!--OIN-ADDRESS-3 WCB NATURE OF INJURY-->
            <td><%=allFields.getProperty("oin_address3")%></td>
            <td>25</td>
            <td>Y</td>
       </tr>
       <tr>
            
            <td>Address 4 WCB Claim Number</td><!--OIN-ADDRESS-4 WCB CLAIM NUMBER-->
            <td><%=allFields.getProperty("oin_address4")%></td>
            <td>25</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Postal Code</td><!--OIN-POSTAL-CODE-->
            <td><%=allFields.getProperty("oin_postalcode")%></td>
            <td>6</td>
            <td>Y</td>
        </tr>

    </table>
    </div>
</body>
</html>
