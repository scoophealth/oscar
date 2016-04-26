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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="oscar.oscarBilling.ca.bc.data.*,oscar.*,org.oscarehr.common.model.*"%>
<%@page import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*,oscar.oscarBilling.ca.bc.MSP.*" %>
<%@page import="org.springframework.web.context.WebApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils, oscar.entities.*" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Demographic" %>
<%@page import="org.oscarehr.common.dao.DemographicDao" %>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />


<%
	BillingmasterDAO billingMasterDao =  SpringUtils.getBean(BillingmasterDAO.class);
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
%>
<%

  String curUser_no = (String) session.getAttribute("user");
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

  ////
  BillingFormData billform = new BillingFormData();
  BillingFormData.BillingPhysician[] billphysician = billform.getProviderList();
  BillingFormData.BillingVisit[] billvisit = billform.getVisitType(billRegion);
  request.setAttribute("billvisit",billvisit);
  BillingFormData.Location[] billlocation = billform.getLocationList(billRegion);
  BillingFormData.BillingForm[] billformlist = billform.getFormList();
  int bFlag = 0;
  String billNo = request.getParameter("billing_no");
  if (billNo == null){
    billNo = (String) request.getAttribute("billing_no");
  }
  MSPReconcile msp = new MSPReconcile();
  Properties allFields = msp.getBillingMasterRecord(billNo);
  MSPBillingNote billingNote = new MSPBillingNote();
  String corrNote = billingNote.getNote(billNo);
  BillingNote  bNote = new BillingNote();
  String messageNotes = bNote.getNote(billNo);
  //TODO get note for this record and put it on screen and then be able to save a new note

  GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);

  String codes[] = {"W","O","P","N","X","T","D"};
  request.setAttribute("codes",codes);
  String serviceLocation = allFields.getProperty("serviceLocation");


  BillingmasterDAO billingmasterDAO = (BillingmasterDAO) SpringUtils.getBean("BillingmasterDAO");
  Billingmaster billingmaster = billingmasterDAO.getBillingMasterByBillingMasterNo(billNo);
  Billing bill = billingmasterDAO.getBilling(billingmaster.getBillingNo());
  BillingCodeData bcd =  new BillingCodeData();

  //fixes bug where invoice number is null when
  //bill changed to Private
  Billingmaster bm = billingMasterDao.getBillingmaster(Integer.parseInt(billNo));
  if(bm != null) {
	  request.setAttribute("invoiceNo",String.valueOf(bm.getBillingNo()));
  }

%>
<html>
<head>
   <title>oscarBillingBC Correction</title>
   <link rel="stylesheet" type="text/css" media="all" href="../../../share/calendar/calendar.css" title="win2k-cold-1" />
   <script type="text/javascript" src="../../../share/calendar/calendar.js"></script>
   <script type="text/javascript" src="../../../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
   <script type="text/javascript" src="../../../share/calendar/calendar-setup.js"></script>
   <script type="text/javascript" src="../../../share/javascript/prototype.js"></script>
   <script type="text/javascript" src="../../../share/javascript/Oscar.js"></script>
        <script language="JavaScript">
        if('<%=request.getAttribute("close")%>' == 'true'){
          window.close();
        }
		function setfocus() {
			this.focus();
		  //document.form1.billing_no.focus();
		  //document.form1.billing_no.select();
		}

		function checkTextLimit(textField, maximumlength) {
         if (textField.value.length > maximumlength + 1){
            alert("Maximun "+maximumlength+" characters");
         }
         if (textField.value.length > maximumlength){
            textField.value = textField.value.substring(0, maximumlength);
         }
      }

      function correspondenceNote(){
         if (document.ReProcessBilling.correspondenceCode.value == "0" ){
            HideElementById('CORRESPONDENCENOTE');
         }else if (document.ReProcessBilling.correspondenceCode.value == "C" ){
            HideElementById('CORRESPONDENCENOTE');
         }else if (document.ReProcessBilling.correspondenceCode.value == "N" ){
            ShowElementById('CORRESPONDENCENOTE');
         }else {(document.ReProcessBilling.correspondenceCode.value == "B" )
            ShowElementById('CORRESPONDENCENOTE');
         }
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
                    awnd=rs('att','<rewrite:reWrite jspPage="billingDigNewSearch.jsp"/>?name='+t0 + '&name1=' + t1 + '&name2=' + t2 + '&search=&formElement=' +d+ '&formName=ReProcessBilling',820,660,1);
                    awnd.focus();
                }

                function GetPriceOfCode(formName,codeElementName,priceElementName ) {
                    var code = codeElementName;
                    var form = formName;
                    var price = priceElementName;
                    t0 = escape(document.forms[form].elements[code].value);
                    t1 = escape("");
                    t2 = escape("");
                    awnd=rs('att','<rewrite:reWrite jspPage="billingGetPriceCode.jsp"/>?name='+t0 + '&name1=' + t1 + '&name2=' + t2 + '&search=&formElementCode=' +t0+ '&formName=' +form+ '&formElementPrice=' +price+ '&formNothing=blank' ,820,660,1);
                    awnd.focus();
                }

                function OtherScriptAttach() {
                      t0 = escape(document.ReProcessBilling.service_code.value);
                      t1 = escape("");
                      t2 = escape("");
                      awnd=rs('att','<rewrite:reWrite jspPage="billingCodeNewSearch.jsp"/>?name='+t0 + '&name1=' + t1 + '&name2=' + t2 + '&search=&formName=ReProcessBilling&formElement=service_code',820,660,1);
                      awnd.focus();
                }

                function ReferralScriptAttach(elementName) {
                     var d = elementName;
                     t0 = escape(document.ReProcessBilling.elements[d].value);
                     t1 = escape("");
                     awnd=rs('att','<rewrite:reWrite jspPage="billingReferCodeSearch.jsp"/>?name='+t0 + '&name1=' + t1 + '&name2=&search=&formElement=' +d+ '&formName=ReProcessBilling',600,600,1);
                     awnd.focus();
                }


//
function HideElementById(ele){
	document.getElementById(ele).style.display='none';
}

function ShowElementById(ele){
	document.getElementById(ele).style.display='';
}


function checkDebitRequest(){
   if (document.ReProcessBilling.submissionCode.value == "E" ){
      ShowElementById('DEBITREQUEST');
      ShowElementById('submitButton');
   }else{
      HideElementById('DEBITREQUEST');
      HideElementById('submitButton');
   }
}

function showRecord(){
   if (document.getElementById('SENDRECORD').style.display == 'none'){
      ShowElementById('SENDRECORD');
   }else{
      HideElementById('SENDRECORD');
   }
}


function validateNum(el){
   var val = el.value;
   var tval = ""+val;
   if (isNaN(val)){
      alert("Item value must be numeric.");
      el.select();
      el.focus();
      return false;
   }
   if ( val >= 99999.99 ){
     alert("Item value must be below $100000");
     el.select();
     el.focus();
     return false;
   }
   decLen = tval.indexOf(".");
   if (decLen != -1  &&   ( tval.length - decLen ) > 3  ){
      alert("Item value has a maximum of 2 decimal places");
      el.select();
      el.focus();
      return false;
   }
   return true;
}


function checkSubmitType(){
	var billtype = document.getElementById('status').value;
	if (billtype == 'W'){
		if(document.forms[0].WCBid == null){
	       alert("Please select a WCB form");
	       return false;
	    }
	}

    if(document.forms[0].submit.value=="Reprocess and Resubmit Bill"){
       window.close()
    }
}
function popup( height, width, url, windowName){
  var page = url;
  windowprops = "height="+height+",width="+width+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(url, windowName, windowprops);
  if (popup != null){
    if (popup.opener == null){
      popup.opener = self;
    }
  }
  popup.focus();
  return false;
}
function popFeeItemList(form,field){
     var width = 575;
     var height = 400;
     var serviceDate = document.getElementById('serviceDate').value;
     var str = document.forms[form].elements[field].value;
     var url = '<rewrite:reWrite jspPage="support/billingfeeitem.jsp"/>'+'?form=' +form+ '&field='+field+'&feeField=billingAmount&corrections=1&searchStr=' +str+'&serviceDate='+serviceDate;
     var windowName = field;
     popup(height,width,url,windowName);
}

function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
  var popup=window.open(page, "attachment", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
   popup.focus();
  }
}

//Rounding function seems to use the right rules for MSP
function calculateFee(){
 var billValue = document.getElementById("billValue").value;
 var billUnit  = document.getElementById("billingUnit").value;
 var roundedValue = Math.round(billValue * billUnit * 100)/100;
 document.getElementById("billingAmount").value = roundedValue.toFixed(2);
}

</script>
<style type="text/css">
        td.bCellData{ font-weight:bold; font-family: Arial,Helvetica,sans-serif; }
        th.bHeaderData{ font-weight:bold; font-family: Arial,Helvetica,sans-serif; }
</style>
</head>



<body bgcolor="#FFFFFF" text="#000000" topmargin="5"  leftmargin="0" rightmargin="0" onLoad="setfocus();checkDebitRequest();correspondenceNote();">
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
      <td  align="left"  class="bCellData"><font color="#000000">
         Office Claim No
      </td>
      <td   class="bCellData">
         <%=billNo%>
      </td>
      <td  align="left"  class="bCellData">
         <font color="#000000">Last update: <%=UpdateDate%></font>
      </td>
      <td align="right" class="bCellData">
          Creator:  <%=providerBean.getProperty(bill.getCreator(),bill.getCreator() )%>
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
        li.addAll(msp.getAllS00Records(billNo));

        for ( int i = 0; i < li.size(); i++){
        String rejReason = (String) li.get(i);
    %>
        <tr>
            <td><%=rejReason%></td>
        </tr>

    <% }%>
    </table>

<%


    DemoNo =  ""+bill.getDemographicNo();
    UpdateDate = MyDateFormat.getMyStandardDate(bill.getUpdateDate());//  rslocation.getString("update_date");
    BillDate = MyDateFormat.getMyStandardDate(bill.getBillingDate());//  rslocation.getString("billing_date");
    BillType = bill.getStatus();
    Provider = bill.getProviderNo();
    visitdate = MyDateFormat.getMyStandardDate(bill.getVisitDate());  //rslocation.getString("visitdate");
    visittype = bill.getVisitType();

 BillType = allFields.getProperty("billingstatus");
 Demographic d = demographicDao.getDemographic(DemoNo);
 if(d != null){
     DemoName = d.getFormattedName();
     DemoSex = d.getSex();
     DemoAddress = d.getAddress();
     DemoCity = d.getCity();
     DemoProvince = d.getProvince();
     DemoPostal = d.getPostal();
     DemoDOB = MyDateFormat.getStandardDate(Integer.parseInt(d.getYearOfBirth()),Integer.parseInt(d.getMonthOfBirth()),Integer.parseInt(d.getDateOfBirth()));
     hin = d.getHin() + d.getVer();
     if (d.getFamilyDoctor() == null){ r_doctor = "N/A"; r_doctor_ohip="000000";}else{
        r_doctor=SxmlMisc.getXmlContent(d.getFamilyDoctor(),"rd")==null?"":SxmlMisc.getXmlContent(d.getFamilyDoctor(),"rd");
        r_doctor_ohip=SxmlMisc.getXmlContent(d.getFamilyDoctor(),"rdohip")==null?"":SxmlMisc.getXmlContent(d.getFamilyDoctor(),"rdohip");
     }

     HCTYPE = d.getHcType()==null?"":d.getHcType();
     if (DemoSex.equals("M")) HCSex = "1";
     if (DemoSex.equals("F")) HCSex = "2";
     roster_status = d.getRosterStatus();
 }

%>
  <br><html:form  action="/billing/CA/BC/reprocessBill" onsubmit="return checkSubmitType()">
<input type="hidden" name="update_date" value="<%=UpdateDate%>"/>
<input type="hidden" name="demoNo" value="<%=DemoNo%>"/>
<input type="hidden" name="billNumber" value="<%=allFields.getProperty("billingNo")%>"/>
<table width="100%" border="0">
  <tr bgcolor="#CCCCFF">
     <td height="21" colspan="2" class="bCellData">Patient Information<input type="hidden" name ="billingmasterNo" value="<%=billNo%>" />

	 <%if(BillType.equals("A")||BillType.equals("P")){%>
	 <a href="#" onClick="popupPage(800,800, '../../../billing/CA/BC/billingView.do?billing_no=<%=request.getAttribute("invoiceNo")%>&receipt=yes')">View Invoice</a>
         <%}%>
	 </td>

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
      <% if (HCTYPE != null && HCTYPE.equals("BC")){ %>
      <%=allFields.getProperty("phn")%>
      <%}else{%>
      <%=allFields.getProperty("oinRegistrationNo")%>
      <%}%>
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


<table width="100%" border="0">
  <tr bgcolor="#CCCCFF">
    <td colspan="2"  class="bCellData">
       Billing Information  Data Center <%=allFields.getProperty("datacenter")%> Payee Number: <%=allFields.getProperty("payeeNo")%> Practitioner Number: <%=allFields.getProperty("practitionerNo")%>
       Bill Type: <%=bill.getBillingtype()%>
     </td>
  </tr>

  <tr>
    <td   class="bCellData">
    <!-- includes the Billing Type Drop Down List -->
      <jsp:include flush="false" page="billType_frag.jsp">
        <jsp:param name="BillType" value="<%=BillType%>"/>
      </jsp:include>
    </td>
    <td   class="bCellData">
        <table>
            <tr>
                <td>
              <!--<a href="#" onClick='rs("billingcalendar","<rewrite:reWrite jspPage="billingCalendarPopup.jsp"/>?year=<%=curYear%>&month=<%=curMonth%>&type=&returnForm=ReProcessBilling&returnItem=serviceDate","380","300","0")'>-->
              <a href="javascript: function myFunction() {return false; }" id="hlSDate">
              Billing Date:
              </a>
                </td>
                <td>
                    To:
                </td>
            </tr>
            <tr>
                <td>
                <input type="text" style="font-size:80%;" id="serviceDate" name="serviceDate" value="<%=allFields.getProperty("serviceDate")%>"><!--<%=allFields.getProperty("serviceDate")%>"/><%=BillDate%>-->
                </td>
                <td>
                <input type="text" name="serviceToDay" value="<%=allFields.getProperty("serviceToDay")%>" size="2" maxlength="2"/>
                </td>
            </tr>
        </table>
    </td>
  </tr>
  <tr bgcolor="#EEEEFF">
    <td width="54%"  class="bCellData">
        Clarification Code:
          <input type="text" name="locationVisit" value="<%=allFields.getProperty("clarificationCode")%>" maxlength="2" size="2"/>
    </td>
    <td width="46%"  class="bCellData">
        Billing Physician#:
        <select style="font-size:80%;" name="providerNo">
            <option value="">--- Select Provider ---</option>
            <% 
               // Retrieving Provider
               String proFirst="", proLast="", proOHIP="", proNo="";
               int Count = 0;
               for(Provider p:providerDao.getActiveProviders()) {
            	 if(p.getOhipNo() != null && !p.getOhipNo().isEmpty()) {
                    proFirst = p.getFirstName();
                    proLast = p.getLastName();
                    proOHIP = p.getProviderNo();

            %>
            <option value="<%=proOHIP%>" <%=Provider.equals(proOHIP)?"selected":""%>> <%=proOHIP%> | <%=proLast%>, <%=proFirst%></option>
            <% } }%>
            <input type="hidden" name="xml_provider_no" value="<%=Provider%>">
    </td>
  </tr>
  <tr>
   <%visittype = allFields.getProperty("serviceLocation");%>
    <td width="54%"  class="bCellData">
        Visit Type:
        <input type="hidden" name="xml_visittype" value="<%=visittype%>">
              <select name="serviceLocation" style="font-size:80%;">
              <%
              for (int i = 0; i < billvisit.length; i++) {
                oscar.oscarBilling.ca.bc.data.BillingFormData.BillingVisit visit = billvisit[i];
                String selected = serviceLocation.equals(visit.getVisitType())?"selected":"";
              %>
              <option value="<%=visit.getVisitType()%>" <%=selected%>><%=visit.getDescription()%> </option>
              <%
              }
              %>

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
            <option value="00" <%=allFields.getProperty("dependentNum").equals("00")?"selected":""%>>00</option>
            <option value="66" <%=allFields.getProperty("dependentNum").equals("66")?"selected":""%>>66</option>
        </select>
    </td>
    <td class="bCellData">New Program Ind:
        <input type="text" name="newProgram" value="<%=allFields.getProperty("newProgram")%>" size="2" maxlength="2" />
    </td>
  </tr>
  <tr>
   <td class="bCellData">After Hours:
        <select name="afterHours" >
            <option value="0" <%=allFields.getProperty("afterHour").equals("0")?"selected":""%>>NO</option>
            <option value="E" <%=allFields.getProperty("afterHour").equals("E")?"selected":""%>>Evening Call</option>
            <option value="N" <%=allFields.getProperty("afterHour").equals("N")?"selected":""%>>Night Call</option>
            <option value="W" <%=allFields.getProperty("afterHour").equals("W")?"selected":""%>>Weekend Call</option>
        </select>
    </td>
    <td class="bCellData">Time Call Recieved<!--TIME-CALL-RECVD-SRV-->
       <input type="text" name="timeCallRec" value="<%=allFields.getProperty("timeCall")%>" size="4" maxlength="4"/>
       <input type="hidden" name="anatomicalArea" value="<%=allFields.getProperty("anatomicalArea")%>" />
    </td>
  </tr>

       <tr>

            <td class="bCellData">Service Time Start<%! /*SERVICE-TIME-START*/ %>
            <input type="text" name="startTime" value="<%=allFields.getProperty("serviceStartTime")%>" size="4" maxlength="4"/></td>

            <td class="bCellData">Service Time Finish <%! /*SERVICE-TIME-FINISH*/ %>
            <input type="text" name="finishTime" value="<%=allFields.getProperty("serviceEndTime")%>" size="4" maxlength="4"/></td>

       </tr>
       <tr>
           <td class="bCellData">MVA
                <select name="mvaClaim" >
                    <option value="N" <%=allFields.getProperty("mvaClaimCode").equals("N")?"selected":""%>>No</option>
                    <option value="Y" <%=allFields.getProperty("mvaClaimCode").equals("Y")?"selected":""%>>Yes</option>
                </select>
              <!--<input type="text" name="mvaClaim" value="<%=allFields.getProperty("mvaClaimCode")%>" size="1"/>-->
            </td>
            <td class="bCellData">ICBC Claim Num:
            <input type="text" name="icbcClaim" value="<%=allFields.getProperty("icbcClaimNo")%>"size="8" maxlength="8"/></td>
       </tr>
       <tr>



            <td class="bCellData">Facility Number
            <input type="text" name="facilityNum" value="<%=allFields.getProperty("facilityNo")%>" size="5" maxlength="5"/></td>

            <td class="bCellData">Facility Sub Number
            <input type="text" name="facilitySubNum" value="<%=allFields.getProperty("facilitySubNo")%>" size="5" maxlength="5"/></td>
       </tr>
</table>
<%
BillingService billService = bcd.getBillingCodeByCode(allFields.getProperty("billingCode"),billingmaster.getServiceDateAsDate());
String billValue= "0.00";
if(billService != null){
    billValue = billService.getValue();
}
%>
<table width="100%" border=1>
  <tr bgcolor="#CCCCFF">
    <td class="bCellData">Service Code</td>
    <td width="50%"  class="bCellData">Description</td>
    <td class="bCellData">Unit</td>
    <td class="bCellData">
      <div align="right">$ Fee</div>
    </td>
	<td class="bCellData">Internal Adj.</td>
  </tr>

    <tr>
      <td  class="bCellData">

        <input type="text" style="font-size:80%;" name="service_code" value="<%=allFields.getProperty("billingCode")%>" size="10" >
        <input type="button" onClick="javascript:popFeeItemList('ReProcessBilling','service_code');return false;" value="Search/Update"/>
      </td>
      <td width="50%"  class="bCellData">
        <%=billform.getServiceDesc(allFields.getProperty("billingCode"),billRegion)%>   ($<span id="valueDisplay"><%=billValue%></span>)
        <input type="hidden" value="<%=billValue%>" id="billValue"/>
        <input type="button" value="Recalculate" onclick="calculateFee()"/>
      </td>
      <td  class="bCellData">
        <input type="hidden" name="billing_unit" value="<%=allFields.getProperty("billingUnit")%>">
        <input type="text" style="font-size:80%;" name="billingUnit" value="<%=allFields.getProperty("billingUnit")%>" size="6" maxlength="6" id="billingUnit">
      </td>
      <td   class="bCellData" nowrap>
        <div align="right">
           <input type="hidden" name="billing_amount" value="<%=allFields.getProperty("bilAmount")%>">
           <input type="text" style="font-size:80%;" size="8" maxlength="8" name="billingAmount" value="<%=allFields.getProperty("billAmount")%>" onChange="javascript:validateNum(this)" id="billingAmount">
        </div>
      </td>
	  <td>
			<label>
				Amount:
			    <input name="adjAmount" type="text" size="7" maxlength="7">
			</label>
                        <label>
                          <input type="checkbox"  name="adjType" value="1"/>
                          debit
                        </label>
	  </td>
    </tr>
 </table>
 <table width="100%" border=1>
  <%
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
                    <a href="javascript:ScriptAttach('dx1')">DX 1</a><input type="text" name="dx1" onClick="checkSubmitType()" value="<%=allFields.getProperty("dxCode1")%>" size="10">
                  </td>
                  <td><%=billform.getDiagDesc(allFields.getProperty("dxCode1"),billRegion)%></td>
                </tr>
                <tr>
                  <td   class="bCellData">
                    <a href="javascript:ScriptAttach('dx2')">DX 2</a><input type="text" name="dx2" onClick="checkSubmitType()" value="<%=allFields.getProperty("dxCode2")%>" size="10">
                  </td>
                  <td><%=billform.getDiagDesc(allFields.getProperty("dxCode2"),billRegion)%></td>
                </tr>
                <tr>
                  <td class="bCellData">
                    <a href="javascript:ScriptAttach('dx3')">DX 3</a><input type="text" name="dx3" onClick="checkSubmitType()" value="<%=allFields.getProperty("dxCode3")%>" size="10">
                  </td>
                  <td><%=billform.getDiagDesc(allFields.getProperty("dxCode3"),billRegion)%></td>
                </tr>
              </table>
       </td>
       <td colspan=2 valign="top">
              <table width="100%">
                <tr bgcolor="#CCCCFF">
                  <td colspan=2 class="bCellData">
                    Referrals
                  <% String  refCD1 = allFields.getProperty("referralFlag1");
                     String  refCD2 = allFields.getProperty("referralFlag2");
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
                        <input type="button" onClick="javascript:ReferralScriptAttach('referalPrac1')" value="Search"/>
                        <input type="text" name="referalPrac1" value="<%=allFields.getProperty("referralNo1")%>" size="5" maxlength="5"/>
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
                        <input type="button" onClick="javascript:ReferralScriptAttach('referalPrac2')" value="Search"/>
                        <input type="text" name="referalPrac2" value="<%=allFields.getProperty("referralNo2")%>" size="5" maxlength="5"/>
                    </td>
                </tr>
              </table>
       </td>
    </tr>
        <tr>
            <td class="bCellData">Payment Mode</td><!--PAYMENT MODE-->
            <td class="bCellData">
                <select name="paymentMode" >
                    <option value="0" <%=allFields.getProperty("paymentMode").equals("0")?"selected":""%>>Fee For Service</option>
                    <option value="E" <%=allFields.getProperty("paymentMode").equals("E")?"selected":""%>>Alternate Funding</option>
                </select>
            </td>
            <td class="bCellData">Submission Code</td><!--SUBMISSION-CODE-->
            <td class="bCellData">
                <select name="submissionCode" onChange="checkDebitRequest();" >
                    <option value="0" <%=allFields.getProperty("submissionCode").equals("0")?"selected":""%>>0|Normal Submission</option>
                    <option value="D" <%=allFields.getProperty("submissionCode").equals("D")?"selected":""%>>D|Duplicate</option>
                    <option value="E" <%=allFields.getProperty("submissionCode").equals("E")?"selected":""%>>E|Debit Request</option>
                    <option value="I" <%=allFields.getProperty("submissionCode").equals("I")?"selected":""%>>I|ICBC Claim</option>
                    <option value="W" <%=allFields.getProperty("submissionCode").equals("W")?"selected":""%>>W|Claim not accepted by WCB</option>
                    <option value="C" <%=allFields.getProperty("submissionCode").equals("C")?"selected":""%>>C|Subscriber Coverage Problem</option>
                    <option value="R" <%=allFields.getProperty("submissionCode").equals("R")?"selected":""%>>R|Resubmit Claim</option>
                    <option value="A" <%=allFields.getProperty("submissionCode").equals("A")?"selected":""%>>A|Pre-approved claim</option>
                    <option value="X" <%=allFields.getProperty("submissionCode").equals("X")?"selected":""%>>X|Resubmitting refused or part paid</option>
                </select>
            </td>
       </tr>
       <!--<tr>
            <td>Service Date</td><%/*SERVICE-DATE*/%>
            <td><input type="text" name="serviceDate" value="<%=allFields.getProperty("serviceDate")%>"/></td>
            <td>Service to Day</td><%/*SERVICE-TO-DAY*/%>
            <td<input type="text" name="serviceToDay" value="<%=allFields.getProperty("serviceToDay")%>"/></td>
       </tr>-->
       <!--
       <tr>
            <td>Time Call Recieved</td><%!/*TIME-CALL-RECVD-SRV*/%>
            <td><input type="text" name="timeCallRec" value="<%=allFields.getProperty("timeCall")%>" size="4"/></td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
       </tr>
       <tr>

            <td>Service Time Start</td><%! /*SERVICE-TIME-START*/ %>
            <td><input type="text" name="startTime" value="<%=allFields.getProperty("serviceStartTime")%>" size="4"/></td>

            <td>Service Time Finish</td><%! /*SERVICE-TIME-FINISH*/ %>
            <td><input type="text" name="finishTime" value="<%=allFields.getProperty("serviceEndTime")%>" size="4"</td>

       </tr>
       -->

       <tr>
            <td class="bCellData" colspan="4">
            <div id="DEBITREQUEST">
                Select sequence number you would like to debit <input name="debitRequestSeqNum" type="text" maxlength="7" size="7" value="<%=getDebitRequestSeqNum(allFields.getProperty("originalClaim"))%>"/>
                </br>Select date MSP received claim (if not known, fill with zeros) (YYYYMMDD): <input id="debitRequestDate" name="debitRequestDate" type="text" maxlength="8" size="8" value="<%=getDebitRequestDate(allFields.getProperty("originalClaim"))%>"/>
                <a id="hlADate"><img title="Calendar" src="../../../images/cal.gif" alt="Calendar" border="0" /></a>
                <!--Date Received MSP <input type="text" />-->
            </div>
            </td>
       </tr>


       <tr>

            <td class="bCellData">Correspondence Code</td><!--CORRESPONDENCE-CODE-->
            <td class="bCellData">
                <select name="correspondenceCode" onChange="correspondenceNote();" >
                    <option value="0" <%=allFields.getProperty("correspondenceCode").equals("0")?"selected":""%>>None</option>
                    <option value="C" <%=allFields.getProperty("correspondenceCode").equals("C")?"selected":""%>>Paper Note</option>
                    <option value="N" <%=allFields.getProperty("correspondenceCode").equals("N")?"selected":""%>>Elec Note</option>
                    <option value="B" <%=allFields.getProperty("correspondenceCode").equals("B")?"selected":""%>>Both</option>
                </select>
                <!--<input type="text" name="correspondenceCode" value="<%=allFields.getProperty("correspondenceCode")%>" size="1"/>-->
            </td>
            <td class="bCellData">Insurer Code</td><!--OIN-INSURER-C0DE-->
            <td class="bCellData">
                <select name="insurerCode" >
                    <option value="" <%=allFields.getProperty("oinInsurerCode").equals("0")?"selected":""%>>None</option>
                    <option value="IN" <%=allFields.getProperty("oinInsurerCode").equals("IN")?"selected":""%>>Institutional Claim</option>
                    <option value="PP" <%=allFields.getProperty("oinInsurerCode").equals("PP")?"selected":""%>>Pay Patient</option>
                    <option value="WC" <%=allFields.getProperty("oinInsurerCode").equals("WC")?"selected":""%>>WCB</option>
                </select>
            </td>
       </tr>
       <tr>
            <td class="bCellData">Claim Short Comment</td><!--CLAIM-SHORT-COMMENT-->
            <td><input type="text" name="shortComment" value="<%=allFields.getProperty("claimComment")%>"size="20" maxlength="20"/></td>
            <td class="bCellData">Note</td>
            <td>
               <div id="CORRESPONDENCENOTE">
                  <textarea cols="60" rows="5"name="notes" onKeyUp="checkTextLimit(this.form.notes,400);"><%=corrNote%></textarea>
               </div>
            </td>

       </tr>
       <tr valign="top">
      <td>
        <table width="100%">
          <tr bgcolor="#CCCCFF"><td class="bCellData">Billing Notes</td>
            </tr>
          <tr>

            <!--CLAIM-SHORT-COMMENT-->
            <td colspan="3">
              <textarea cols="60" rows="5" name="messageNotes"><%=messageNotes%>              </textarea>
            </td>
            <td>            </td>

        </table>

      <td colspan="3">
      <jsp:include flush="false" page="billTransactions.jsp">
        <jsp:param name="billMasterNo" value="<%=billNo%>"/>
      </jsp:include>
      </td>

  </table>

  <script type="text/javascript">
  function callReplacementWebService(url,id){
           var ran_number=Math.round(Math.random()*1000000);
           var params = "demographicNo=<%=bill.getDemographicNo()%>&wcb=&billingcode=<%=allFields.getProperty("billingCode")%>&rand="+ran_number;  //hack to get around ie caching the page
           new Ajax.Updater(id,url, {method:'get',parameters:params,asynchronous:true});
  }

  function replaceWCB(id){
        oscarLog("In replaceWCB");
        var ur = "wcbForms.jsp?wcbid="+id;
        callReplacementWebService(ur,'wcbForms');
        oscarLog("replaceWCB out == "+ur);
  }

  function toggleWCB(){
       var statusType = document.getElementById('status');
       //alert(statusType.value);

       if(statusType.value == "W"){
          oscarLog("Replacing WCB element");
           replaceWCB('0');
       }else{
           document.getElementById('wcbForms').innerHTML = "";
       }

}


  <%if(bill.getBillingtype().equals("WCB")){ %>
         oscarLog("DOES THIS LOG");
      replaceWCB('<%=billingmaster.getWcbId()%>');

  <%}%>
  </script>
  <div id="wcbForms"></div>





       <!--<tr>
            <td>Facility Num</td><%! /*FACILITY-NUM*/ %>
            <td><input type="text" name="facilityNum" value="<%=allFields.getProperty("facilityNo")%>" size="5"/></td>
            <td>Facility Sub Num</td><%! /*FACILITY-SUB-NUM*/%>
            <td><input type="text" name="facilitySubNum" value="<%=allFields.getProperty("facilitySubNo")%>" size="5"/></td>
       </tr>-->

       <!--<tr>


            <td>Registration Num</td><%!/*OIN-REGISTRATION-NUM*/%>
            <td><input type="text" name="registrationNum" value="<%=allFields.getProperty("oinRegistrationNo")%>" size="12"/></td>
       </tr>-->
       <!--
       <tr>
            <td>First Name</td><%/*OIN-FIRST-NAME*/%>
            <td><input type="text" name="firstName" value="<%=allFields.getProperty("oinFirstName")%>" size="12"/></td>

            <td>Surname</td><%/*OIN-SURNAME*/%>
            <td><input type="text" name="surname" value="<%=allFields.getProperty("oinSurname")%>" size="18"/></td>
       </tr>
       <tr>
            <td>SEX</td>
            <td><input type="text" name="sex" value="<%=allFields.getProperty("oinSexCode")%>" size="1"/></td>
            <td>Birth date</td><%/*OIN-BIRTHDATE*/%>
            <td><input type="text" name="birthdate" value="<%=allFields.getProperty("oinBirthdate")%>" size="8"/></td>
       </tr>
       <tr>

            <td>Address 1 WCB Date Of Injury</td><%/*OIN-ADDRESS-1		WCB DATE OF INJURY*/%>
            <td colspan="3"><input type="text" name="address1" value="<%=allFields.getProperty("oinAddress")%>" size="25"/></td>
       </tr>
       <tr>
            <td>Address 2 WCB AREA OF INJURY</td><%/*OIN-ADDRESS-2 WCB AREA OF INJURY ANATOMICAL-POSITION*/%>
            <td colspan="3"><input type="text" name="address2" value="<%=allFields.getProperty("oinAddress2")%>" size="25"/></td>
       </tr>
       <tr>
            <Td>Address 3 WCB NATURE OF INJURY</td><%/*OIN-ADDRESS-3 WCB NATURE OF INJURY*/%>
            <td colspan="3"><input type="text" name="address3" value="<%=allFields.getProperty("oinAddress3")%>" size="25" /></td>
       </tr>
       <tr>
            <td>Address 4 WCB Claim Number</td><%/*OIN-ADDRESS-4 WCB CLAIM NUMBER*/%>
            <td colspan="3"><input type="text" name="address4" value="<%=allFields.getProperty("oinAddress4")%>" size="25" /></td>
       </tr>
       <tr>
            <td>Postal Code</td><%/*OIN-POSTAL-CODE*/%>
            <td colspan="3"><input type="text" name="postalCode" value="<%=allFields.getProperty("oinPostalcode")%>" size="6"/></td>
        </tr>
-->



<input type="hidden" value="0" name="saveandclose"/>

 <%
 if(!BillType.equals("S")){
 %>
        <tr>
          <td colspan="4"  class="bCellData">
            <input type="submit" name="submit" value="Reprocess Bill">
            <input type="submit" name="submit" value="Resubmit Bill">
            <input type="submit" name="submit" value="Reprocess and Resubmit Bill" onClick="checkSubmitType()">
            <input type="submit" name="submit" value="Settle Bill">

          </td>
       </tr>
<%}else{%>
       <tr>
          <td colspan="4"  class="bCellData">
            <input type="submit" name="submit" id="submitButton" style="display:none;" value="Reprocess and Resubmit Bill" onClick="checkSubmitType()">
          </td>
       </tr>

<%}%>


  </table>
  </html:form>
  <a href="javascript: function myFunction() {return false; }" onClick="javascript: showRecord();" >View Full Record</a>
  <div style="display: none;" id="SENDRECORD">
    <table border=1>
        <tr>
            <td>Name</td>
            <td>Value</td>
            <td>Size</td>
            <td>Shown</td>
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
            <td><%=allFields.getProperty("payeeNo")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Practitioner Number</td><!--PRACTITIONER-NUM-->
            <td><%=allFields.getProperty("practitionerNo")%></td><!--MSP PHN-->
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
            <td><%=allFields.getProperty("nameVerify")%></td>
            <td>4</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Dependent Number</td><!--DEPENDENT-NUM-->
            <td><%=allFields.getProperty("dependentNum")%></td>
            <td>2</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Billed Units</td><!--BILLED-SRV-UNITS-->
            <td><%=allFields.getProperty("billingUnit")%></td>
            <td>3</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Service Clarification code</td><!--SERVICE CLARIFICATION CODE-->
            <Td><%=allFields.getProperty("clarificationCode")%></td>
            <td>2</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Anatomical Area</td><!--MSP SERVICE ANATOMICAL AREA-->
            <td><%=allFields.getProperty("anatomicalArea")%></td>
            <td>2</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>After Hours</td><!--AFTER HOURS SERVICE IND-->
            <td><%=allFields.getProperty("afterHour")%></td>
            <td>1</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>New Program Ind</td><!--NEW PROGRAM IND-->
            <td><%=allFields.getProperty("newProgram")%></td>
            <td>2</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Billed Fee Item</td><!--BILLED-FEE-ITEM-->
            <td><%=allFields.getProperty("billingCode")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Billed Amount</td><!--BILLED-AMOUNT-->
            <td><%=allFields.getProperty("billAmount")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Payment Mode</td><!--PAYMENT MODE-->
            <td><%=allFields.getProperty("paymentMode")%></td>
            <td>1</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Service Date</td><!--SERVICE-DATE-->
            <td><%=allFields.getProperty("serviceDate")%></td>
            <td>8</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Service to Day</td><!--SERVICE-TO-DAY-->
            <td><%=allFields.getProperty("serviceToDay")%></td>
            <td>2</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Submission Code</td><!--SUBMISSION-CODE-->
            <td><%=allFields.getProperty("submissionCode")%></td>
            <td>1</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Ex Submission Code</td><!--EXTENDED SUBMISSION CODE-->
            <td><%=allFields.getProperty("extendedSubmissionCode")%></td>
            <td>1</td>
            <td> </td>
       </tr>
       <tr>

            <td>Diag Code 1</td><!--DIAGNOSTIC-CODE-1-->
            <td><%=allFields.getProperty("dxCode1")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Diag Code 2</td><!--DIAGNOSTIC-CODE-2-->
            <td><%=allFields.getProperty("dxCode2")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Diag Code 3</td><!--DIAGNOSTIC-CODE-3-->
            <td><%=allFields.getProperty("dxCode3")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Diag Expansion</td><!--DIAGNOSTIC EXPANSION-->
            <td><%=allFields.getProperty("dxExpansion")%></td>
            <td>15</td>
            <td> </td>
       </tr>
       <tr>

            <td>Service Location</td><!--SERVICE-LOCATION-CD-->
            <td><%=allFields.getProperty("serviceLocation")%></td>
            <td>1</td>
            <td> </td>
       </tr>
       <tr>

            <td>Referal Practitioner CD</td><!--REF-PRACT-1-CD-->
            <Td><%=allFields.getProperty("referralFlag1")%></td>
            <td>1</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Referal Practitioner</td><!--REF-PRACT-1-->
            <td><%=allFields.getProperty("referralNo1")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Referal Practitioner CD</td><!--REF-PRACT-2-CD-->
            <Td><%=allFields.getProperty("referralFlag2")%></td>
            <td>1</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Referal Practitioner</td><!--REF-PRACT-2-->
            <td><%=allFields.getProperty("referralNo2")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Time Call Recieved</td><!--TIME-CALL-RECVD-SRV-->
            <td><%=allFields.getProperty("timeCall")%></td>
            <td>4</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Service Time Start</td><!--SERVICE-TIME-START-->
            <td><%=allFields.getProperty("serviceStartTime")%></td>
            <td>4</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Service Time Finish</td><!--SERVICE-TIME-FINISH-->
            <td><%=allFields.getProperty("serviceEndTime")%></td>
            <td>4</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Birth Date</td><!--BIRTH-DATE-->
            <td><%=allFields.getProperty("birthDate")%></td>
            <td>8</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Office Number</td><!--OFFICE-FOLIO-NUMBER-->
            <td><%=allFields.getProperty("officeNumber")%></td>
            <td>7</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Correspondence Code</td><!--CORRESPONDENCE-CODE-->
            <td><%=allFields.getProperty("correspondenceCode")%></td>
            <td>1</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Claim Short Comment</td><!--CLAIM-SHORT-COMMENT-->
            <td><%=allFields.getProperty("claimComment")%></td>
            <td>20</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>MVA Claim Code</td><!--MVA-CLAIM-CODE-->
            <td><%=allFields.getProperty("mvaClaimCode")%></td>
            <td>1</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>ICBC Claim Num</td><!--ICBC-CLAIM-NUM-->
            <td><%=allFields.getProperty("icbcClaimNo")%></td>
            <td>8</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>MSP File Num</td><!--ORG-MSP-FILE-NUM-->
            <td><%=allFields.getProperty("originalClaim")%></td>
            <td>20</td>
            <td> </td>
       </tr>
       <tr>
            <td>Facility Num</td><!--FACILITY-NUM-->facility_no
            <td><%=allFields.getProperty("facilityNo")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Facility Sub Num</td><!--FACILITY-SUB-NUM-->
            <td><%=allFields.getProperty("facilitySubNo")%></td>
            <td>5</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Insurer Code</td><!--OIN-INSURER-C0DE-->
            <td><%=allFields.getProperty("oinInsurerCode")%></td>
            <td>2</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Registration Num</td><!--OIN-REGISTRATION-NUM-->
            <td><%=allFields.getProperty("oinRegistrationNo")%></td>
            <td>12</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Birth date</td><!--OIN-BIRTHDATE-->
            <td><%=allFields.getProperty("oinBirthdate")%></td>
            <td>8</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>First Name</td><!--OIN-FIRST-NAME-->
            <td><%=allFields.getProperty("oinFirstName")%></td>
            <td>12</td>
            <td>Y</td>
       </tr>
       <tr>
            <td>Second Name</td><!--OIN-SECOND-NAME-INITIAL-->
            <td><%=allFields.getProperty("oinSecondName")%></td>
            <td>1</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Surname</td><!--OIN-SURNAME-->
            <td><%=allFields.getProperty("oinSurname")%></td>
            <td>18</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>SEX</td>
            <td><%=allFields.getProperty("oinSexCode")%></td>
            <td>1</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Address 1 WCB Date Of Injury</td><!--OIN-ADDRESS-1		WCB DATE OF INJURY-->
            <td><%=allFields.getProperty("oinAddress")%></td>
            <td>25</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Address 2 WCB AREA OF INJURY</td><!--OIN-ADDRESS-2 WCB AREA OF INJURY ANATOMICAL-POSITION-->
            <td><%=allFields.getProperty("oinAddress2")%></td>
            <td>25</td>
            <td>Y</td>
       </tr>
       <tr>

            <Td>Address 3 WCB NATURE OF INJURY</td><!--OIN-ADDRESS-3 WCB NATURE OF INJURY-->
            <td><%=allFields.getProperty("oinAddress3")%></td>
            <td>25</td>
            <td>Y</td>
       </tr>
       <tr>

            <td>Address 4 WCB Claim Number</td><!--OIN-ADDRESS-4 WCB CLAIM NUMBER-->
            <td><%=allFields.getProperty("oinAddress4")%></td>
            <td>25</td>a
            <td>Y</td>
       </tr>
       <tr>

            <td>Postal Code</td><!--OIN-POSTAL-CODE-->
            <td><%=allFields.getProperty("oinPostalcode")%></td>
            <td>6</td>
            <td>Y</td>
        </tr>

    </table>
    </div>
    <script language='javascript'>
           Calendar.setup({inputField:"serviceDate",ifFormat:"%Y%m%d",showsTime:false,button:"hlSDate",singleClick:true,step:1});
           Calendar.setup({inputField:"debitRequestDate",ifFormat:"%Y%m%d",showsTime:false,button:"hlADate",singleClick:true,step:1});
     </script>
</body>
</html>
<%!
public String getDebitRequestSeqNum(String str){
    if(str == null || str.length() <12){
        return "";
    }
    String retval = str.substring(5,12);
    return retval;
}

public String getDebitRequestDate (String str){
    if(str == null || str.length() <12){
        return "";
    }
    String retval = str.substring(13);
    return retval;
}


%>
