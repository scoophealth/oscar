<%      
  if(session.getValue("user") == null)
    response.sendRedirect("../../../logout.jsp");
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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->




 


<%@ page language="java" contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite" %>
<%@ page import="oscar.oscarDemographic.data.*" %>
<%@ page import="java.text.*, java.util.*, oscar.oscarBilling.data.*,oscar.oscarBilling.pageUtil.*,oscar.*" %>
<%

 int year = 0;//Integer.parseInt(request.getParameter("year"));
  int month = 0; //Integer.parseInt(request.getParameter("month"));
  //int day = now.get(Calendar.DATE);
  int delta = 0; //request.getParameter("delta")==null?0:Integer.parseInt(request.getParameter("delta")); //add or minus month
  GregorianCalendar now = new GregorianCalendar();

 
  year = now.get(Calendar.YEAR);
  month = now.get(Calendar.MONTH)+1;
String sxml_location="", sxml_provider="", sxml_visittype="";
String color = "", colorflag ="";
BillingSessionBean bean = (BillingSessionBean)pageContext.findAttribute("billingSessionBean");
oscar.oscarDemographic.data.DemographicData demoData = new oscar.oscarDemographic.data.DemographicData();
oscar.oscarDemographic.data.DemographicData.Demographic demo = demoData.getDemographic(bean.getPatientNo());
BillingFormData billform = new BillingFormData();
BillingFormData.BillingService[] billlist1 = billform.getServiceList("Group1", bean.getBillForm(), bean.getBillRegion());
BillingFormData.BillingService[] billlist2 = billform.getServiceList("Group2", bean.getBillForm(), bean.getBillRegion());
BillingFormData.BillingService[] billlist3 = billform.getServiceList("Group3", bean.getBillForm(), bean.getBillRegion());
String group1Header = billform.getServiceGroupName("Group1");
String group2Header = billform.getServiceGroupName("Group2");
String group3Header = billform.getServiceGroupName("Group3");

BillingFormData.BillingPhysician[] billphysician = billform.getProviderList();
BillingFormData.BillingVisit[] billvisit = billform.getVisitType(bean.getBillRegion());
BillingFormData.Location[] billlocation = billform.getLocationList(bean.getBillRegion());
BillingFormData.Diagnostic[] diaglist = billform.getDiagnosticList( bean.getBillForm(), bean.getBillRegion());
BillingFormData.BillingForm[] billformlist = billform.getFormList();





if (request.getParameter("loadFromSession") == null ){
   System.out.println("RemovingAttribute from Session");
   request.getSession().removeAttribute("BillingCreateBillingForm");	
}
%>



<html>
<head>
<title><bean:message key="billing.bc.title"/></title>
<html:base/>
<style type="text/css">
	<!--
	A, BODY, INPUT, OPTION ,SELECT , TABLE, TEXTAREA, TD, TR {font-family:tahoma,sans-serif; font-size:10px;}
	
	-->
</style>  
<script language="JavaScript">
<!--
function quickPickDiagnostic(diagnos){

	if (document.BillingCreateBillingForm.xml_diagnostic_detail1.value == ""){
		document.BillingCreateBillingForm.xml_diagnostic_detail1.value = diagnos;
        }else if ( document.BillingCreateBillingForm.xml_diagnostic_detail2.value == ""){
		document.BillingCreateBillingForm.xml_diagnostic_detail2.value= diagnos;
	}else if ( document.BillingCreateBillingForm.xml_diagnostic_detail3.value == "" ){
		document.BillingCreateBillingForm.xml_diagnostic_detail3.value = diagnos;
	}else{
		alert("All of the Diagnostic Coding Boxes are full");
	}
}

function isNumeric(strString){
        var validNums = "0123456789.";
        var strChar;
        var retval = true;

        for (i = 0; i < strString.length && retval == true; i++){
           strChar = strString.charAt(i);
           if (validNums.indexOf(strChar) == -1){
              retval = false;
           }
        }
         return retval;
   }

function checkUnits(){
	if  (!isNumeric(document.BillingCreateBillingForm.xml_other1_unit.value)){
		alert("Units have be of numeric value");
	        document.BillingCreateBillingForm.xml_other1_unit.focus();
		return false;
	}else if (!isNumeric(document.BillingCreateBillingForm.xml_other2_unit.value)){
		alert("Units have be of numeric value");
                document.BillingCreateBillingForm.xml_other2_unit.focus();
                return false;
	}else if (!isNumeric(document.BillingCreateBillingForm.xml_other3_unit.value)){
		alert("Units have be of numeric value");
                document.BillingCreateBillingForm.xml_other3_unit.focus();
                return false;
	}
	return true;

}

function setfocus() {
		  //document.serviceform.xml_diagnostic_code.focus();
		  //document.serviceform.xml_diagnostic_code.select();
		}
		
function RecordAttachments(Files, File0, File1, File2) {
  window.document.serviceform.elements["File0Data"].value = File0;
  window.document.serviceform.elements["File1Data"].value = File1;
  window.document.serviceform.elements["File2Data"].value = File2;
    window.document.all.Atts.innerText = Files;
  }

var remote=null;

function rs(n,u,w,h,x) {
  args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
  remote=window.open(u,n,args);
  if (remote != null) {
    if (remote.opener == null)
      remote.opener = self;
  }
  if (x == 1) { return remote; }
}


var awnd=null;
function ScriptAttach() {


  t0 = escape(document.BillingCreateBillingForm.xml_diagnostic_detail1.value);
  t1 = escape(document.BillingCreateBillingForm.xml_diagnostic_detail2.value);
  t2 = escape(document.BillingCreateBillingForm.xml_diagnostic_detail3.value);
  awnd=rs('att','<rewrite:reWrite jspPage="billingDigNewSearch.jsp"/>?name='+t0 + '&name1=' + t1 + '&name2=' + t2 + '&search=',600,600,1);
  awnd.focus();
  
  
 
}



function OtherScriptAttach() {
  t0 = escape(document.BillingCreateBillingForm.xml_other1.value);
  t1 = escape(document.BillingCreateBillingForm.xml_other2.value);
  t2 = escape(document.BillingCreateBillingForm.xml_other3.value);
 // f1 = document.serviceform.xml_dig_search1.value;
 // f2 = escape(document.serviceform.elements["File2Data"].value);
 // fname = escape(document.Compose.elements["FName"].value);
  awnd=rs('att','<rewrite:reWrite jspPage="billingCodeNewSearch.jsp"/>?name='+t0 + '&name1=' + t1 + '&name2=' + t2 + '&search=',600,600,1);
  awnd.focus();
}
function ReferralScriptAttach() {
  t0 = escape(document.BillingCreateBillingForm.xml_refer1.value);
  t1 = escape(document.BillingCreateBillingForm.xml_refer2.value);
 // t2 = escape(document.BillingCreateBillingForm.xml_refer3.value);
 // f1 = document.serviceform.xml_dig_search1.value;
 // f2 = escape(document.serviceform.elements["File2Data"].value);
 // fname = escape(document.Compose.elements["FName"].value);
  awnd=rs('att','<rewrite:reWrite jspPage="billingReferCodeSearch.jsp"/>?name='+t0 + '&name1=' + t1 + '&name2=&search=',600,600,1);
  awnd.focus();
}


function ResearchScriptAttach() {
  t0 = escape(document.serviceform.xml_referral1.value);
  t1 = escape(document.serviceform.xml_referral2.value);
  
  awnd=rs('att','../<rewrite:reWrite jspPage="billingReferralCodeSearch.jsp"/>?name='+t0 + '&name1=' + t1 +  '&search=',600,600,1);
  awnd.focus();
}

function POP(n,h,v) {
  window.open(n,'OSCAR','toolbar=no,location=no,directories=no,status=yes,menubar=no,resizable=yes,copyhistory=no,scrollbars=yes,width='+h+',height='+v+',top=100,left=200');
}
//-->
</SCRIPT>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="JavaScript">
<!--
<!--
function reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.pgW=innerWidth; document.pgH=innerHeight; onresize=reloadPage; }}
  else if (innerWidth!=document.pgW || innerHeight!=document.pgH) location.reload();
}
reloadPage(true);
// -->

function findObj(n, d) { //v4.0
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=findObj(n,d.layers[i].document);
  if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function showHideLayers() { //v3.0
  var i,p,v,obj,args=showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v='hide')?'hidden':v; }
    obj.visibility=v; }
}
//-->
</script>
<link rel="stylesheet" href="../billing/billing.css" type="text/css">
</head>




<body bgcolor="#FFFFFF" text="#000000" rightmargin="0" leftmargin="0" topmargin="10" marginwidth="0" marginheight="0" onLoad="setfocus();showHideLayers('Layer1','','hide')">
<div id="Layer2" style="position:absolute; left:362px; top:26px; width:332px; height:600px; z-index:2; background-color: #FFCC00; layer-background-color: #FFCC00; border: 1px none #000000; visibility: hidden"> 
  <table width="98%" border="0" cellspacing="0" cellpadding="0" align=center>
    <tr> 
      <td width="18%"><b><font face="Verdana, Arial, Helvetica, sans-serif" size="-2">Dx 
        Code</font></b></td>
      <td width="76%"><b><font face="Verdana, Arial, Helvetica, sans-serif" size="-2">Description</font></b></td>
      <td width="6%"><a href="#" onClick="showHideLayers('Layer2','','hide'); return false;">X</a></td>
    </tr>
   <% for (int i=0; i< diaglist.length; i++){ 
	                         
         if (colorflag.compareTo("1")==0) {
            color = "#EEEEFF";
            colorflag = "0";
         }else{
	    color = "#FFFFFF";
	    colorflag = "1";	                         
	 }
    %>
       <tr bgcolor=<%=color%>> 
          <td width="18%"><b><font size="-2" face="Verdana, Arial, Helvetica, sans-serif" color="#7A388D">
               <a href="#" onClick="quickPickDiagnostic('<%=diaglist[i].getDiagnosticCode()%>');showHideLayers('Layer2','','hide');return false;">
                                <%=diaglist[i].getDiagnosticCode()%>
               </a></font></b>
          </td>
	  <td colspan="2"><font size="-2" face="Verdana, Arial, Helvetica, sans-serif" color="#7A388D"><%=diaglist[i].getDescription()%></font></td>
       </tr>                  
    <%}%>
	                          
	                          
                     
                                 
                                                            
   
  </table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr bgcolor="#000000"> 
    <td height="40" width="10%"> </td>
    <td width="90%" align="left"> 
      <p><font face="Verdana, Arial, Helvetica, sans-serif" color="#FFFFFF"><b><font face="Arial, Helvetica, sans-serif" size="4">oscar<font size="3"><bean:message key="billing.bc.title"/></font></font></b></font> 
      </p>
    </td>
  </tr>
</table>
<div id="Layer1" style="position:absolute; left:1px; top:159px; width:410px; height:300px; z-index:1; background-color: #FFCC00; layer-background-color: #FFCC00; border: 1px none #000000; visibility: hidden"> 
  <table width="98%" border="0" cellspacing="0" cellpadding="0" align=center>
    <tr bgcolor="#393764"> 
      <td width="96%" height="7" bgcolor="#FFCC00"><font size="-2" face="Geneva, Arial, Helvetica, san-serif"><b><font face="Verdana, Arial, Helvetica, sans-serif" color="#000000">Billing 
        Form</font></b></font></td>
      <td width="3%" bgcolor="#FFCC00" height="7"><font face="Verdana, Arial, Helvetica, sans-serif" ><b><a href="#" onClick="showHideLayers('Layer1','','hide');return false;">x</a></b></font></td>
    </tr>
     <% for (int i=0; i< billformlist.length; i++){ 
            if (colorflag.compareTo("1")==0) {    	                         
                color = "#EEEEFF";
    	        colorflag = "0";
    	    }else{
    	        color = "#FFFFFF";
    	      	colorflag = "1";    	                         
    	    }
     %>
            <tr bgcolor=<%=color%>> 
               <td colspan="2"><b><font size="-2" face="Verdana, Arial, Helvetica, sans-serif" color="#7A388D">
                  <a href="../../../billing.do?billRegion=<%=bean.getBillRegion()%>&billForm=<%=billformlist[i].getFormCode()%>&hotclick=&appointment_no=<%=bean.getApptNo()%>&demographic_name=<%=bean.getPatientName()%>&demographic_no=<%=bean.getPatientNo()%>&user_no=<%=bean.getCreator()%>&apptProvider_no=<%=bean.getApptProviderNo()%>&providerview=<%=bean.getProviderView()%>&appointment_date=<%=bean.getApptDate()%>&status=<%=bean.getApptStatus()%>&start_time=<%=bean.getApptStart()%>&bNewForm=1" onClick="showHideLayers('Layer1','','hide')"><%=billformlist[i].getDescription()%></a>
                  </font></b>
               </td>
            </tr>    
     <%}%>
	               
    
     </table>
</div>

<html:form action="/billing/CA/BC/CreateBilling" onsubmit="return checkUnits();" >   
        
        <%
        BillingCreateBillingForm thisForm;
        thisForm = (BillingCreateBillingForm)request.getSession().getAttribute("BillingCreateBillingForm");        
        if (thisForm != null){                
            sxml_provider = ((String) thisForm.getXml_provider());
            sxml_location =((String) thisForm.getXml_location());
            sxml_visittype = ((String) thisForm.getXml_visittype());
            if (sxml_location.compareTo("")==0){
                sxml_location  = OscarProperties.getInstance().getProperty("visitlocation");
                sxml_visittype = OscarProperties.getInstance().getProperty("visittype");
                sxml_provider  = bean.getApptProviderNo();

                thisForm.setXml_location(sxml_location);  
                thisForm.setXml_provider( sxml_provider);       
                thisForm.setXml_visittype(sxml_visittype);    
            }
            String apDate = thisForm.getXml_appointment_date();
            if ( apDate != null && apDate.trim().length() == 0 ){
                thisForm.setXml_appointment_date(bean.getApptDate());
            }
            System.out.println("app date "+thisForm.getXml_appointment_date());
            
        }
        %>
        
  <table width="100%" border="0" cellspacing="0" cellpadding="0" >
    <tr> 
      <td valign="top" height="221"> 
        <table width="107%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td ><font face="Verdana, Arial, Helvetica, sans-serif" size="-2"><b><bean:message key="billing.patient"/></b>:</font></td>
            <td><font face="Verdana, Arial, Helvetica, sans-serif" size="-2"><u><%=demo.getLastName()%>, <%=demo.getFirstName()%></u></font></td>
            <td><b><font face="Verdana, Arial, Helvetica, sans-serif" size="-2"><b><bean:message key="billing.patient.status"/></b>:</font><font face="Verdana, Arial, Helvetica, sans-serif" size="1"><%=demo.getPatientStatus()%> 
              &nbsp;&nbsp;&nbsp;&nbsp; <b><bean:message key="billing.patient.roster"/>: <%=demo.getRosterStatus()%></b></font></b></td>
            <td width="14%"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><strong><bean:message key="billing.provider.assignedProvider"/></strong></font></td>
            <td width="29%"><%=billform.getProviderName(demo.getProviderNo())%></td>
          </tr>
          <tr> 
            <td ><font face="Verdana, Arial, Helvetica, sans-serif" size="-2"><b><font face="Verdana, Arial, Helvetica, sans-serif" size="1"><bean:message key="billing.patient.age"/>:</font></b><br>
              </font></td>
            <td><b><font face="Verdana, Arial, Helvetica, sans-serif" size="1"><%=demo.getAge()%></font></b></td>
            <td><font size="1" face="Arial, Helvetica, sans-serif"><b><font face="Verdana, Arial, Helvetica, sans-serif"><a href="#" onClick="showHideLayers('Layer1','','show');return false;">
                    <bean:message key="billing.billingform"/></a>:</font></b>
                <%=billform.getBillingFormDesc(billformlist,bean.getBillForm())%>                        
              </font></td>
            <td width="14%"><font face="Verdana, Arial, Helvetica, sans-serif" size="-2"> 
              
              <!-- 2003-9-21 
              13:21 -->
              
              <b><bean:message key="billing.provider.billProvider"/></b> </font></td>
            <td width="29%"><font face="Verdana, Arial, Helvetica, sans-serif" size="-2"> 
              <html:select property="xml_provider"  value="<%=sxml_provider%>">
                <html:option value="000000" ><b>Select Provider</b></html:option>
                     <% for (int j=0; j< billphysician.length; j++){ %>
	             <html:option value="<%=billphysician[j].getProviderNo()%>"><%=billphysician[j].getProviderName()%></html:option>
               <%}%>
              </html:select>
              </font></td>
          </tr>
          <tr> 
            <td ><font face="Verdana, Arial, Helvetica, sans-serif" size="-2"><b><bean:message key="billing.billingtype"/></b> </font></td>
            <td width="12%"><font face="Verdana, Arial, Helvetica, sans-serif" size="-2"> 
              <html:select property="xml_billtype"  >
                <html:option value="MSP" >Bill MSP</html:option>
				<html:option value="MSP" >Bill WCB</html:option>
              </html:select>
              </font></td>
            <td width="33%"><font face="Verdana, Arial, Helvetica, sans-serif" size="-2"> 
              <b><bean:message key="billing.visitlocation"/></b> 
       
              <html:select property="xml_location" >
              
                  <% for (int i=0; i< billlocation.length; i++){
                     String locationDescription = billlocation[i].getBillingLocation()+"|"+billlocation[i].getDescription();
                  %>
                     
                    <html:option value="<%=locationDescription%>"><%=billlocation[i].getDescription()%></html:option>
               <%}%>
                    
                    
                   </html:select>
              </font></td>
            <td width="14%"><font size="-2" face="Verdana, Arial, Helvetica, sans-serif"><b><bean:message key="billing.visittype"/></b> </font></td>
            <td width="29%"> <font size="-2" face="Verdana, Arial, Helvetica, sans-serif"> 
              <html:select property="xml_visittype" >
                        <% for (int i=0; i< billvisit.length; i++){ 
                        String visitTypeDescription = billvisit[i].getVisitType()+"|"+billvisit[i].getDescription();
                        %>
                    <html:option value="<%=visitTypeDescription%>"><%=billvisit[i].getDescription()%></html:option>
                   <%}%>           
              </html:select>
              </font></td>
          </tr>
          <tr> 
            <td ><font face="Verdana, Arial, Helvetica, sans-serif" size="-2">
                <a href="javascript: function myFunction() {return false; }"  onClick='rs("billingcalendar","<rewrite:reWrite jspPage="billingCalendarPopup.jsp"/>?year=<%=year%>&month=<%=month%>&type=service","380","300","0")'><bean:message key="billing.servicedate"/></a> </font>                
            </td>
            <td colspan="2"><font face="Verdana, Arial, Helvetica, sans-serif" size="-2" nowrap>                                           
              <html:text property="xml_appointment_date"  size="12" />
              <strong><bean:message key="billing.servicedate.starttime"/></strong> 
              <html:text property="xml_starttime" size="12" maxlength="4" />
              <strong><bean:message key="billing.servicedate.endtime"/></strong> 
              <html:text property="xml_endtime" size="12" maxlength="4" />
              </font></td>
            <td width="14%"><font face="Verdana, Arial, Helvetica, sans-serif" size="-2">                                  
              <a href="javascript: function myFunction() {return false; }"  onClick='rs("billingcalendar","<rewrite:reWrite jspPage="billingCalendarPopup.jsp"/>?year=<%=year%>&month=<%=month%>&type=admission","380","300","0")'><bean:message key="billing.admissiondate"/></a> </font></td>
            <td width="29%"><font face="Verdana, Arial, Helvetica, sans-serif" size="-2"> 
              <html:text property="xml_vdate" />
              </font></td>  
          </tr>
        </table>
        <div align="left"></div>
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="137">
          
          <tr> 
            <td valign="top" width="33%"> 
              <table width="100%" border="1" cellspacing="0" cellpadding="0" height="0">
                
                <tr bgcolor="#CCCCFF"> 
                  <td width="25%"><b></b> 
                    <div align="left"><font face="Verdana, Arial, Helvetica, sans-serif"><b><font size="1" color="#000000"><%=group1Header%>
                      </font></b></font></div>
                  </td>
                  <td width="61%" bgcolor="#CCCCFF"><b><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000"><bean:message key="billing.service.desc"/></font></b></td>
                  <td width="14%"> 
                    <div align="right"><b><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000">$ 
                      <bean:message key="billing.service.fee"/></font></b></div>
                  </td>
                </tr>
                   <% for (int i=0; i< billlist1.length; i++){                    %>
                     <tr bgcolor=<%=i%2==0?"#EEEEFF":"white"%>> 
                                <td width="25%" height="14"><b></b> <font face="Verdana, Arial, Helvetica, sans-serif"> 
                                   <html:multibox property="service" value="<%=billlist1[i].getServiceCode()%>"/>
                                   <font size="1"><%=billlist1[i].getServiceCode()%></font></font></td>
                                <td width="61%" height="14"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><%=billlist1[i].getDescription()%></font>                              </font> 
                              </td>
                                <td width="14%" height="14"> 
                                  <div align="right"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><%=billlist1[i].getPrice()%></font> 
                               </div>
                                </td>
                </tr>
                    <%}%>
          
                
         
                
               </table>
              <table width="100%" border="0" cellpadding="2" cellspacing="2" bgcolor="#CC0000">
                <tr> 
                        
                  <td width="91%" valign="top"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0" height="67" bgcolor="#EEEEFF">
                            <tr> 
                              
                        <td><b><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><bean:message key="billing.referral.doctor"/></font></b></td>
                              
                        <td><b><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><bean:message key="billing.referral.type"/> </font></b></td>
                            </tr>
                            <tr> 
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                <html:text property="xml_refer1" size="40" />
                                </font></td>
                              
                        <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                          <html:select property="refertype1">
                            <html:option value="" >Select Type</html:option>
                            <html:option value="T">Refer To</html:option>
                            <html:option value="B">Refer By</html:option>
                          </html:select>
                          </font></td>
                            </tr>
                            <tr> 
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                <html:text property="xml_refer2" size="40" />
								             
                                </font></td>
                              
                        <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1">
                             <html:select property="refertype2">
                               <html:option value="" >Select Type</html:option>
                               <html:option value="T">Refer To</html:option>
                               <html:option value="B">Refer By</html:option>
                             </html:select>
                          </font></td>
                            </tr>
                            <tr> 
                              
                        <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1">&nbsp; 
                          </font></td>
                              
                        <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1">&nbsp; 
                          </font></td>
                            </tr>
                            <tr> 
                              <td colspan="2"><a href="javascript:ReferralScriptAttach()"><img src="../../../images/search_code.jpg" border="0"></a> 
                              </td>
                            </tr>
                          </table>
                        </td>
                        <td width="9%"> 
                          <!--
                          <table width="20%" border="0" cellspacing="0" cellpadding="0" height="67" bgcolor="#CEFFCE">
                            <tr> 
                              <td><b><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Research 
                                <font color="#FF0000">(optional)</font></font></b></td>
                              <td><b></b></td>
                            </tr>
                            <tr> 
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                <input type="hidden" name="xml_research1" size="10" datafld='xml_research1'>
                                </font></td>
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                </font></td>
                            </tr>
                            <tr> 
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                <input type="hidden" name="xml_research2" size="10" datafld='xml_research2'>
                                </font></td>
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                </font></td>
                            </tr>
                            <tr> 
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                <input type="hidden" name="xml_research3" size="10"  datafld='xml_research3'>
                                </font></td>
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                </font></td>
                            </tr>
                            <tr> 
                              <td colspan="2"><a href="javascript:ResearchScriptAttach()"><img src="../images/research_code.jpg" border="0"></a> 
                              </td>
                            </tr>
                          </table> -->
                        </td>
                      </tr>
                    </table>
              <p>&nbsp;</p>
            </td>
            
            <td valign="top" width="31%"> 
              <table width="100%" border="1" cellspacing="0" cellpadding="0">
                <tr bgcolor="#CCCCFF"> 
                  <td width="21%"><b></b> 
                    <div align="left"><font face="Verdana, Arial, Helvetica, sans-serif"><b><font size="1" color="#000000"><%=group2Header%></font></b></font></div>
                  </td>
                  <td width="60%" bgcolor="#CCCCFF"><b><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000"><bean:message key="billing.service.desc"/></font></b></td>
                  <td width="19%"> 
                    <div align="right"><b><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000">$ 
                      <bean:message key="billing.service.fee"/></font></b></div>
                  </td>
                </tr>
                
                 <% for (int i=0; i< billlist2.length; i++){	                         %>
	      		                         <tr bgcolor=<%=i%2==0?"#EEEEFF":"white"%>> 
	      				                    <td width="21%" height="14"><b></b> <font face="Verdana, Arial, Helvetica, sans-serif"> 
	      				                   <html:multibox property="service" value="<%=billlist2[i].getServiceCode()%>"/>
	      				                      <font size="1"><%=billlist2[i].getServiceCode()%></font></font></td>
	      				                    <td width="60%" height="14"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><%=billlist2[i].getDescription()%></font>                              </font> 
	      				                  </td>
	      				                    <td width="19%" height="14"> 
	      				                      <div align="right"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><%=billlist2[i].getPrice()%></font> 
	      				                   </div>
	      				                    </td>
	                      </tr>
	                          <%}%>
          
              </table>
              <table width="100%" height="105" border="0" cellpadding="2" cellspacing="2" bgcolor="#999900">
                <tr> 
                        
                  <td width="91%" valign="top"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0" height="67" bgcolor="#EEEEFF">
                            <tr> 
                              <td width="85%"><b><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><bean:message key="billing.service.otherservice"/></font></b></td>
                              <td width="15%"><b><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><bean:message key="billing.service.unit"/></font></b></td>
                            </tr>
                            <tr> 
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                <html:text  property="xml_other1" size="40"/>
                                </font></td>
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                
                        <html:text property="xml_other1_unit" size="5" maxlength="3" />
                                </font></td>
                            </tr>
                            <tr> 
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                <html:text property="xml_other2" size="40" />
                                </font></td>
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                
                        <html:text property="xml_other2_unit" size="5" maxlength="3" />
                                </font></td>
                            </tr>
                            <tr> 
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                <html:text property="xml_other3" size="40" />
                                </font></td>
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                
                        <html:text property="xml_other3_unit" size="5" maxlength="3" />
                                </font></td>
                            </tr>
                            <tr> 
                              <td colspan="2"><a href="javascript:OtherScriptAttach()"><img src="../../../images/search_code.jpg" border="0"></a> 
                              </td>
                            </tr>
                          </table>
                        </td>
                        <td width="9%"> 
                          <!--
                          <table width="20%" border="0" cellspacing="0" cellpadding="0" height="67" bgcolor="#CEFFCE">
                            <tr> 
                              <td><b><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Research 
                                <font color="#FF0000">(optional)</font></font></b></td>
                              <td><b></b></td>
                            </tr>
                            <tr> 
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                <input type="hidden" name="xml_research1" size="10" datafld='xml_research1'>
                                </font></td>
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                </font></td>
                            </tr>
                            <tr> 
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                <input type="hidden" name="xml_research2" size="10" datafld='xml_research2'>
                                </font></td>
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                </font></td>
                            </tr>
                            <tr> 
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                <input type="hidden" name="xml_research3" size="10"  datafld='xml_research3'>
                                </font></td>
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                </font></td>
                            </tr>
                            <tr> 
                              <td colspan="2"><a href="javascript:ResearchScriptAttach()"><img src="../images/research_code.jpg" border="0"></a> 
                              </td>
                            </tr>
                          </table> -->
                        </td>
                      </tr>
                    </table></td>
            <td valign="top" width="36%"> 
              
              <table width="100%" border="1" cellspacing="0" cellpadding="0" height="0">
                
                <tr bgcolor="#CCCCFF"> 
                  <td width="25%"><b></b> 
                    <div align="left"><font face="Verdana, Arial, Helvetica, sans-serif"><b><font size="1" color="#000000"><%=group3Header%></font></b></font></div>
                  </td>
                  <td width="61%" bgcolor="#CCCCFF"><b><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000"><bean:message key="billing.service.desc"/></font></b></td>
                  <td width="14%"> 
                    <div align="right"><b><font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="#000000">$<bean:message key="billing.service.fee"/></font></b></div>
                  </td>
                </tr>
                   <% for (int i=0; i< billlist3.length; i++){             %>
				                         <tr bgcolor=<%=i%2==0?"#EEEEFF":"white"%>> 
						                    <td width="25%" height="14"><b></b> <font face="Verdana, Arial, Helvetica, sans-serif"> 
						                   <html:multibox property="service" value="<%=billlist3[i].getServiceCode()%>"/>
						                      <font size="1"><%=billlist3[i].getServiceCode()%></font></font></td>
						                    <td width="61%" height="14"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><%=billlist3[i].getDescription()%></font>                              </font> 
						                  </td>
						                    <td width="14%" height="14"> 
						                      <div align="right"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><%=billlist3[i].getPrice()%></font> 
						                   </div>
						                    </td>
		                </tr>
		                    <%}%>
          
              </table>
              <table width="100%" border="0" cellpadding="2" cellspacing="2" bgcolor="#CCCCFF">
                <tr> 
                        <td width="91%" height="103" valign="top"> 
                          <table width="100%" border="0" cellspacing="0" cellpadding="0" height="67" bgcolor="#EEEEFF">
                            <tr> 
                              <td><b><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><a href="#" onClick="showHideLayers('Layer2','','show','Layer1','','hide');return false;"><bean:message key="billing.diagnostic.code"/></a></font></b></td>
                              <td><b></b></td>
                            </tr>
                            <tr> 
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                <html:text  property="xml_diagnostic_detail1"  size="25" />
                                </font></td>
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1">&nbsp; 
                                </font></td>
                            </tr>
                            <tr> 
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                <html:text property="xml_diagnostic_detail2"  size="25" />
                                </font></td>
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1">&nbsp; 
                                </font></td>
                            </tr>
                            <tr> 
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                <html:text property="xml_diagnostic_detail3"  size="25" />
                                </font></td>
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1">&nbsp; 
                                </font></td>
                            </tr>
                            <tr> 
                              <td colspan="2"><font face="Verdana, Arial, Helvetica, sans-serif" size="1"><a href="javascript:ScriptAttach()"><img src="../../../images/search_dx_code.jpg" border="0"></a></font> 
                                <font face="Verdana, Arial, Helvetica, sans-serif" size="1">&nbsp;
                                </font> </td>
                            </tr>
                          </table>
                        </td>
                        <td width="9%"> 
                          <!--
                          <table width="20%" border="0" cellspacing="0" cellpadding="0" height="67" bgcolor="#CEFFCE">
                            <tr> 
                              <td><b><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Research 
                                <font color="#FF0000">(optional)</font></font></b></td>
                              <td><b></b></td>
                            </tr>
                            <tr> 
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                <input type="hidden" name="xml_research1" size="10" datafld='xml_research1'>
                                </font></td>
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                </font></td>
                            </tr>
                            <tr> 
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                <input type="hidden" name="xml_research2" size="10" datafld='xml_research2'>
                                </font></td>
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                </font></td>
                            </tr>
                            <tr> 
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                <input type="hidden" name="xml_research3" size="10"  datafld='xml_research3'>
                                </font></td>
                              <td><font face="Verdana, Arial, Helvetica, sans-serif" size="1"> 
                                </font></td>
                            </tr>
                            <tr> 
                              <td colspan="2"><a href="javascript:ResearchScriptAttach()"><img src="../images/research_code.jpg" border="0"></a> 
                              </td>
                            </tr>
                          </table> -->
                        </td>
                      </tr>
                    </table>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td align="right"> 
            <input type="submit" name="Submit" value="Continue">
            <input type="button" name="Button" value="Cancel" onClick="window.close();">
          </td>
        </tr>
      </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</html:form>
<p>&nbsp; </p>
</body>
</html>