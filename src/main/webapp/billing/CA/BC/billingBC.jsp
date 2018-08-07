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

<%@page import="java.net.URLEncoder"%>
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

<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="oscar.oscarDemographic.data.*"%>
<%@page import="java.text.*, java.util.*, oscar.oscarBilling.ca.bc.data.*,oscar.oscarBilling.ca.bc.pageUtil.*,oscar.*,oscar.entities.*"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.BillingreferralDao" %>
<%!
  public void fillDxcodeList(BillingFormData.BillingService[] servicelist, Map dxcodeList) {
    for (int i = 0; i < servicelist.length; i++) {
      BillingAssociationPersistence per = new BillingAssociationPersistence();
      ServiceCodeAssociation assoc = per.getServiceCodeAssocByCode(servicelist[i].getServiceCode());
      List dxcodes = assoc.getDxCodes();
      if (!dxcodes.isEmpty()) {
        dxcodeList.put(servicelist[i].getServiceCode(), (String) dxcodes.get(0));
      }
    }
  }
  public String createAssociationJS(Map assocCodeMap,String assocArrayName) {

    Set e = assocCodeMap.keySet();
    StringBuffer out = new StringBuffer();
    out.append("var " + assocArrayName + "  = new Array();");
    out.append("\n");
    int index = 0;
    for (Iterator iter = e.iterator(); iter.hasNext(); ) {
      String key = (String) iter.next();
      String value = (String) assocCodeMap.get(key);
      String rowName = assocArrayName + "row";
      out.append("var " + rowName + index + " = new Array(2);\n");
      out.append(rowName + index + "[0]='" + key + "'; ");
      out.append(rowName + index + "[1]='" + value + "';\n");
      out.append(assocArrayName + "[" + index + "]=" + rowName  + index + ";\n");
      index++;
    }
    return out.toString();
  }
%>
<%
  int year = 0; //Integer.parseInt(request.getParameter("year"));
  int month = 0; //Integer.parseInt(request.getParameter("month"));
  //int day = now.get(Calendar.DATE);
  int delta = 0; //request.getParameter("delta")==null?0:Integer.parseInt(request.getParameter("delta")); //add or minus month
  GregorianCalendar now = new GregorianCalendar();
  year = now.get(Calendar.YEAR);
  month = now.get(Calendar.MONTH) + 1;
  String sxml_location = "", sxml_provider = "", sxml_visittype = "";
  String color = "", colorflag = "";
  BillingSessionBean bean = (BillingSessionBean) pageContext.findAttribute("billingSessionBean");
  oscar.oscarDemographic.data.DemographicData demoData = new oscar.oscarDemographic.data.DemographicData();
  org.oscarehr.common.model.Demographic demo = demoData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), bean.getPatientNo());
  oscar.oscarBilling.ca.bc.MSP.ServiceCodeValidationLogic lgc = new oscar.oscarBilling.ca.bc.MSP.ServiceCodeValidationLogic();
  BillingFormData billform = new BillingFormData();
  BillingFormData.BillingService[] billlist1 = lgc.filterServiceCodeList(billform.getServiceList("Group1", bean.getBillForm(), bean.getBillRegion(),new Date()), demo);
  BillingFormData.BillingService[] billlist2 = lgc.filterServiceCodeList(billform.getServiceList("Group2", bean.getBillForm(), bean.getBillRegion(),new Date()), demo);
  BillingFormData.BillingService[] billlist3 = lgc.filterServiceCodeList(billform.getServiceList("Group3", bean.getBillForm(), bean.getBillRegion(),new Date()), demo);
  String group1Header = billform.getServiceGroupName("Group1", bean.getBillForm());
  String group2Header = billform.getServiceGroupName("Group2", bean.getBillForm());
  String group3Header = billform.getServiceGroupName("Group3", bean.getBillForm());
  BillingFormData.BillingPhysician[] billphysician = billform.getProviderList();
  BillingFormData.BillingVisit[] billvisit = billform.getVisitType(bean.getBillRegion());
  BillingFormData.Location[] billlocation = billform.getLocationList(bean.getBillRegion());
  BillingFormData.Diagnostic[] diaglist = billform.getDiagnosticList(bean.getBillForm(), bean.getBillRegion());
  BillingFormData.BillingForm[] billformlist = billform.getFormList();
  SupServiceCodeAssocDAO supDao = SpringUtils.getBean(SupServiceCodeAssocDAO.class);
  HashMap assocCodeMap = new HashMap();
  fillDxcodeList(billlist1, assocCodeMap);
  fillDxcodeList(billlist2, assocCodeMap);
  fillDxcodeList(billlist3, assocCodeMap);
  String frmType = request.getParameter("billType");
  if (frmType != null && frmType.equals("Pri")) {
    billform.setPrivateFees(billlist1);
    billform.setPrivateFees(billlist2);
    billform.setPrivateFees(billlist3);
  }
  String loadFromSession = request.getParameter("loadFromSession");
  if (request.getAttribute("loadFromSession") != null) {
    loadFromSession = "y";
  }
  if (loadFromSession == null) {
    request.getSession().removeAttribute("BillingCreateBillingForm");
  }

  BillingreferralDao billingReferralDao = (BillingreferralDao)SpringUtils.getBean("BillingreferralDAO");
  
  String newWCBClaim = (String)request.getAttribute("newWCBClaim");
  
  String mRecRefDoctor = "";
  String mRecRefDoctorNum = "";

  if(!demo.getFamilyDoctorNumber().equals("")){
   mRecRefDoctor = demo.getFamilyDoctorLastName() + ", " + demo.getFamilyDoctorFirstName();
   mRecRefDoctorNum = demo.getFamilyDoctorNumber();
  }else{
   mRecRefDoctor = "none";
  }

  ArrayList<String> recentList = billform.getRecentReferralDoctorsList(demo.getDemographicNo());
%>
<html>
<head>
<title>
<bean:message key="billing.bc.title"/>
</title>
<html:base/>
<link rel="stylesheet" type="text/css" media="all" href="../../../share/calendar/calendar.css" title="win2k-cold-1"/>
<link rel="stylesheet" type="text/css" media="all" href="../../../library/bootstrap/3.0.0/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" media="all" href="../../../css/bootstrap-datetimepicker-standalone.css" />
<link rel="stylesheet" type="text/css" media="all" href="../../../css/bootstrap-datetimepicker.min.css" />

<script type="text/javascript" src="../../../library/moment.js"></script>
<script type="text/javascript" src="../../../js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="../../../library/bootstrap/3.0.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="../../../library/bootstrap-datetimepicker.min.js" ></script>

<script type="text/javascript" src="../../../share/calendar/calendar.js"></script>
<script type="text/javascript" src="../../../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript" src="../../../share/calendar/calendar-setup.js"></script>
<script type="text/javascript" src="../../../share/javascript/prototype.js"></script>
<script type="text/javascript" src="../../../share/javascript/Oscar.js"></script>
<script type="text/javascript" src="../../../share/javascript/boxover.js"></script>
<style type="text/css">
  <!--
    A, BODY, INPUT, OPTION ,SELECT , TABLE, TEXTAREA, TD, TR {font-family:tahoma,Verdana, Arial, Helvetica,sans-serif; font-size:10px;}
  -->

body {
	margin:0px;
	padding:0px;
	border:none;
}
  .popUp{
  visibility: hidden;
  background-color:#ffffcc;
  }

  .odd{
  background-color:#EEEEFF;
  }
  .even{
  background-color:#FFFFFF;
  }

  .popupHeader{
  background-color:#ccccff;
  font-size:10pt;
  }
  
   div.container{
  	padding: 0 !important;
  }
  
  .form-control {
	  font-size: 10px !important;
	}
	
	#patientIdRow span, #patientIdRow a {
	  margin:10px;
	}
	
	h3 {
	  font-size:small;
	  width:100%;
	  border-top: red thin solid;
	  border-bottom: red thin solid;
	  margin:0px;
	  margin-top:5px;
	  padding-left:20px;
	}
	
  #billingFormTable table {
	  border:black thin solid;
		width:100%;
		margin-bottom: 5px;
		background-color: #f5f5f5;
		border: 1px solid #ccc;
	}

	.serviceCodesTable tr:nth-child(even) {background: #f5f5f5}
	.serviceCodesTable tr:nth-child(odd) {background: #FFF}

	* table td {
	  padding:0px;
	  margin:0px;
	}

	  #billingFormTable table table {
		border:grey thin solid;
		background-color:black;
		margin:3px auto;
	  }
	
	#billingFormTable table tr td {
	  padding:1px 5px !important;
	}
	
	tr#buttonRow td {
		border-bottom: #ccc thin solid;
		padding-bottom: 5px !important;
	}
	
</style>
<script type="text/javascript" >

//creates a javaspt array of associated dx codes
<%=createAssociationJS(assocCodeMap,"jsAssocCodes")%>
<%=createAssociationJS(supDao.getAssociationKeyValues(),"trayAssocCodes")%>



function codeEntered(svcCode){
	myform = document.forms[0];
	return((myform.xml_other1.value == svcCode)||(myform.xml_other2.value == svcCode)||(myform.xml_other3.value == svcCode))
}
function addSvcCode(svcCode) {
    myform = document.forms[0];
    for (var i = 0; i < myform.service.length; i++) {
      if (myform.service[i].value == svcCode) {
        if (myform.service[i].checked) {
          if (myform.xml_other1.value == "") {
            myform.xml_other1.value = svcCode;
            var trayCode =  getAssocCode(svcCode,trayAssocCodes);
            if(trayCode!=''){
              myform.xml_other2.value = trayCode;
            }
            myform.xml_diagnostic_detail1.value = getAssocCode(svcCode,jsAssocCodes);
          }
          else if (myform.xml_other2.value == "") {
            myform.xml_other2.value = svcCode;
            var trayCode =  getAssocCode(svcCode,trayAssocCodes);
            if(trayCode!=''){
              myform.xml_other3.value = trayCode;
            }
            myform.xml_diagnostic_detail2.value = getAssocCode(svcCode,jsAssocCodes);
          }
          else if (myform.xml_other3.value == "") {
            myform.xml_other3.value = svcCode;
            myform.xml_diagnostic_detail3.value = getAssocCode(svcCode,jsAssocCodes);
          }
          else {
            alert("There are already three service codes entered");
            myform.service[i].checked = false;
            return;
          }
        }
        else {
          if (myform.xml_other1.value == svcCode) {
            myform.xml_other1.value = "";
            myform.xml_other2.value = "";
            myform.xml_diagnostic_detail1.value = "";
          }
          else if (myform.xml_other2.value == svcCode) {
            myform.xml_other2.value = "";
            myform.xml_diagnostic_detail2.value = "";
          }
          else if (myform.xml_other3.value == svcCode) {
            myform.xml_other3.value = "";
            myform.xml_diagnostic_detail3.value = "";
          }
        }return;
      }
    }
  }
function getAssocCode(svcCode,assocCodes){
  var retcode = ""
  for (var i = 0; i < assocCodes.length; i++) {
    var row = assocCodes[i];

    if(row[0] == svcCode){
      return row[1];
    }
  }
  return retcode;
}
function checkSelectedCodes(){
    myform = document.forms[0];
    for (var i = 0; i < myform.service.length; i++) {
        if (myform.service[i].checked) {
            if(!codeEntered(myform.service[i].value)){
                myform.service[i].checked = false;
            }
        }
    }
}






function HideElementById(ele){
	document.getElementById(ele).style.display='none';
}

function ShowElementById(ele){
	document.getElementById(ele).style.display='';
}
function CheckType(){
	if (document.BillingCreateBillingForm.xml_billtype.value == "ICBC"){
		ShowElementById('ICBC');
		document.BillingCreateBillingForm.mva_claim_code.options[1].selected = true;
	}else{
		HideElementById('ICBC');
		document.BillingCreateBillingForm.mva_claim_code.options[0].selected = true;
	}
         toggleWCB();




}

function callReplacementWebService(url,id){
               var ran_number=Math.round(Math.random()*1000000);
               var params = "demographicNo=<%=bean.getPatientNo()%>&wcb=&rand="+ran_number;  //hack to get around ie caching the page
               new Ajax.Updater(id,url, {method:'get',parameters:params,asynchronous:true}); 
         } 
          <%
          String wcb = "";
          Integer wcbid = (Integer) request.getAttribute("WCBFormId");
          if (wcbid != null){
              wcb = "?wcbid="+wcbid;
          }
          %>


function toggleWCB(){
        <%
        if(!"1".equals(newWCBClaim)){
        %>
       if (document.BillingCreateBillingForm.xml_billtype.value == "WCB"){
        document.BillingCreateBillingForm.fromBilling.value = "true";
       }
       else{
          document.BillingCreateBillingForm.fromBilling.value = "false";
       }
        <%}
        %>

       if (document.BillingCreateBillingForm.xml_billtype.value == "WCB"){
         callReplacementWebService("wcbForms.jsp<%=wcb%>",'wcbForms');
       }

}

function replaceWCB(id){
    oscarLog("In replaceWCB");
  var ur = "wcbForms.jsp?wcbid="+id;
  callReplacementWebService(ur,'wcbForms');
  oscarLog("replaceWCB out == "+ur);
}

function gotoPrivate(){
   if (document.BillingCreateBillingForm.xml_billtype.value == "Pri"){
      document.location.href = "<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/" %>billing.do?billRegion=<%=bean.getBillRegion()%>&billForm=Pri&hotclick=&appointment_no=<%=bean.getApptNo()%>&demographic_name=<%=URLEncoder.encode(bean.getPatientName(),"UTF-8")%>&demographic_no=<%=bean.getPatientNo()%>&user_no=<%=bean.getCreator()%>&apptProvider_no=<%=bean.getApptProviderNo()%>&providerview=<%=bean.getProviderView()%>&appointment_date=<%=bean.getApptDate()%>&status=<%=bean.getApptStatus()%>&start_time=<%=bean.getApptStart()%>&bNewForm=1&billType=Pri";
  }
   if (document.BillingCreateBillingForm.xml_billtype.value == "MSP"){
      document.location.href = "<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/" %>billing.do?billRegion=<%=bean.getBillRegion()%>&billForm=<%=OscarProperties.getInstance().getProperty("default_view")%>&hotclick=&appointment_no=<%=bean.getApptNo()%>&demographic_name=<%=URLEncoder.encode(bean.getPatientName(),"UTF-8")%>&demographic_no=<%=bean.getPatientNo()%>&user_no=<%=bean.getCreator()%>&apptProvider_no=<%=bean.getApptProviderNo()%>&providerview=<%=bean.getProviderView()%>&appointment_date=<%=bean.getApptDate()%>&status=<%=bean.getApptStatus()%>&start_time=<%=bean.getApptStart()%>&bNewForm=1&billType=MSP";
   }
}

function correspondenceNote(){
	if (document.BillingCreateBillingForm.correspondenceCode.value == "0" ){
		HideElementById('CORRESPONDENCENOTE');
	}else if (document.BillingCreateBillingForm.correspondenceCode.value == "C" ){
		HideElementById('CORRESPONDENCENOTE');
	}else if (document.BillingCreateBillingForm.correspondenceCode.value == "N" ){
	  ShowElementById('CORRESPONDENCENOTE');
	}else {(document.BillingCreateBillingForm.correspondenceCode.value == "B" )
     ShowElementById('CORRESPONDENCENOTE');
	}
}

function checkFACILITY(){
	if (document.getElementById('FACILITY').style.display == 'none'){
		ShowElementById('FACILITY');
	}else{
		HideElementById('FACILITY');
	}
}




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
	}else if (document.BillingCreateBillingForm.xml_provider.value == "000000"){
	   alert("Please select a Billing Physician");
	   document.BillingCreateBillingForm.xml_provider.focus();
	   return false;
	}
	return true;

}

function checkTextLimit(textField, maximumlength) {
   if (textField.value.length > maximumlength + 1){
      alert("Maximun "+maximumlength+" characters");
   }
   if (textField.value.length > maximumlength){
      textField.value = textField.value.substring(0, maximumlength);
   }
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
  awnd=rs('att','<rewrite:reWrite jspPage="billingDigNewSearch.jsp"/>?name='+t0 + '&name1=' + t1 + '&name2=' + t2 + '&search=',850,740,1);
  awnd.focus();



}



function OtherScriptAttach() {
  t0 = escape(document.BillingCreateBillingForm.xml_other1.value);
  t1 = escape(document.BillingCreateBillingForm.xml_other2.value);
  t2 = escape(document.BillingCreateBillingForm.xml_other3.value);
 // f1 = document.serviceform.xml_dig_search1.value;
 // f2 = escape(document.serviceform.elements["File2Data"].value);
 // fname = escape(document.Compose.elements["FName"].value);
  awnd=rs('att','<rewrite:reWrite jspPage="billingCodeNewSearch.jsp"/>?name='+t0 + '&name1=' + t1 + '&name2=' + t2 + '&search=',820,740,1);
  awnd.focus();
}

function ReferralScriptAttach1(){
    ReferralScriptAttach('xml_refer1');
}

function ReferralScriptAttach2(){
    ReferralScriptAttach('xml_refer2');
}


function ReferralScriptAttach(elementName) {
     var d = elementName;
     t0 = escape(document.BillingCreateBillingForm.elements[d].value);
     t1 = escape("");
     awnd=rs('att','<rewrite:reWrite jspPage="billingReferCodeSearch.jsp"/>?name='+t0 + '&name1=' + t1 + '&name2=&search=&formElement=' +d+ '&formName=BillingCreateBillingForm',600,600,1);
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


function grabEnter(event,callb){
  if( (window.event && window.event.keyCode == 13) || (event && event.which == 13) )  {
     eval(callb);
     return false;
  }
}

</script>
<script type="text/javascript">
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
function getOffsetLeft (el) {
	var ol=el.offsetLeft;
	while ((el=el.offsetParent) != null) { ol += el.offsetLeft; }
	return ol;
	}

function getOffsetTop (el) {
	var ot=el.offsetTop;
	while((el=el.offsetParent) != null) { ot += el.offsetTop; }
	return ot;
	}
var objPopup = null;
var shim = null;
function formPopup(event,objectId){
  objPopTrig = document.getElementById(event);
  objPopup = document.getElementById(objectId);
  shim = document.getElementById('DivShim');
 xPos = getOffsetLeft(objPopTrig);
  yPos = getOffsetTop(objPopTrig) + objPopTrig.offsetHeight;
//  objPopup.style.left = xPos + 'px' ;
//  objPopup.style.top = yPos + 'px' ;
  objPopup.style.zIndex = 9999;

  shim.style.width = objPopup.offsetWidth + 2;
  shim.style.height = objPopup.offsetHeight;
  shim.style.top = objPopup.style.top;
  shim.style.left = objPopup.style.left;
  shim.style.zIndex = objPopup.style.zIndex - 1;
  shim.style.display = "block";
  objPopup.style.display = "block";
  shim.style.visibility = 'visible';
  objPopup.style.visibility = 'visible';
}

function formPopupHide(){
  objPopup.style.visibility = 'hidden';
  shim.style.visibility = 'hidden';
  objPopup = null;
  shim = null;
}
//-->

function addCodeToList(svcCode){
    if (myform.xml_other1.value == "") {
        myform.xml_other1.value = svcCode;
        return true;
    }else if (myform.xml_other2.value == "") {
        myform.xml_other2.value = svcCode;
        return true;
    }else if (myform.xml_other3.value == "") {
        myform.xml_other3.value  = svcCode;
        return true;
    }
}

function setCodeToChecked(svcCode){
    myform = document.forms[0];
    var codeset = false;
    for (var i = 0; i < myform.service.length; i++) {
        if (myform.service[i].value == svcCode) {
            var wasAbleToAddCode = addCodeToList(svcCode);
            if(wasAbleToAddCode){
               myform.service[i].checked = true;
               codeset = true;
            }
            return;
        }
    }
    
    if(codeEntered(svcCode) == false){
        if (myform.xml_other1.value == "") {
            myform.xml_other1.value = svcCode;
            return;
            //myform.xml_diagnostic_detail1.value = "";
        }else if (myform.xml_other2.value == "") {
            myform.xml_other2.value = svcCode;
            //myform.xml_diagnostic_detail2.value = "";
        }else if (myform.xml_other3.value == "") {
            myform.xml_other3.value  = svcCode;
            //myform.xml_diagnostic_detail3.value = "";
        }
    }
    
    
    
}


function checkifSet(icd9,feeitem,extrafeeitem){
   myform = document.forms[0]; 
   oscarLog("icd9 "+icd9+" ,feeitem "+feeitem+" "+codeEntered(feeitem)+" extrafeeitem "+extrafeeitem+ " "+codeEntered(extrafeeitem));
   if (myform.xml_diagnostic_detail1.value == ""){
       myform.xml_diagnostic_detail1.value = icd9;
   }
   setCodeToChecked(feeitem);
   oscarLog("feeitem did put "+codeEntered(feeitem));
   setCodeToChecked(extrafeeitem);
   
   oscarLog("extra feeitem did put"+codeEntered(extrafeeitem));
}

jQuery(document).ready(function(){
	
	/* for setting times */
    jQuery(function () {
        jQuery('.datetimepicker').datetimepicker({
            format: 'HH:mm'
        });
    });
    
	/* New billing form selection method*/
    jQuery("#selectBillingForm").on('change',function() {
      	window.location.replace("../../../" + this.value);
    });
	
	/*  For dynamically setting the hours and minutes required for the start and end times.
	<input type='text' id="serviceStartTime" class="form-control" />
  	<input type=hidden id="xml_starttime_hr" name="xml_starttime_hr" />
    <input type=hidden id="xml_starttime_min" name="xml_starttime_min" /> */
    
	jQuery("#serviceStartTime").on('blur', function() {
	    var time = this.value;
	    if(time) {
	        var hour = time.split(":")[0];
	        var minute = time.split(":")[1];
	        var endtime = jQuery("#serviceEndTime").val();
	       if(endtime) {
	        	timeCompare( hour+minute, endtime.replace(":", "") );
	       }
			jQuery("#xml_starttime_hr").val(hour);
			jQuery("#xml_starttime_min").val(minute);
	    }
	 })
	 
	 
	 jQuery("#serviceEndTime").on('blur', function() {
	    var time = this.value;
	    
	    if(time) {    	
	        var hour = time.split(":")[0];
	        var minute = time.split(":")[1];
	        var starttime = jQuery("#serviceStartTime").val();
	        timeCompare( starttime.replace(":", ""), hour+minute );
			jQuery("#xml_endtime_hr").val(hour);
			jQuery("#xml_endtime_min").val(minute);
	    }
	 })
	 
	 function timeCompare( start, end ) {
	    if( !start || start > end ) {
	    	alert("Warning: the start time is greater than the end time.");
	    }	
	 }
  
 
 jQuery(".referral-doctor").on('click', function() {
  mRecordRefDocNum = jQuery(this).attr('data-num');  
  mRecordRefDoc= jQuery(this).attr('data-doc');  
  
  one = jQuery('[name="xml_refer1"]');
  two = jQuery('[name="xml_refer2"]');
  
  if(one.val().length>0){
	  two.val(mRecordRefDocNum);
	  two.attr("title", mRecordRefDoc );
  }else{
	  one.val(mRecordRefDocNum);
	  one.attr("title", mRecordRefDoc );
  }
 });
	 
	 
})
</script>
<link rel="stylesheet" href="../billing/billing.css" type="text/css" />
</head>
<%!
  /**
   Generates a string list of option tags in numeric order
   **/
  String generateNumericOptionList(int range,String selected) {
    selected = selected == null?"":selected;
    StringBuffer buff = new StringBuffer();
    buff.append("<option value=''></option>");
    for (int i = 0; i < range; i++) {
      String prefix = i < 10 ? "0" : "";
      String val = prefix + String.valueOf(i);
      String sel = "";
      if(val.equals(selected)){
        sel = "selected";
      }
      buff.append("<option value='" + val + "' " + sel + ">" + val + "</option>");
    }
    return buff.toString();
  }
%>
<body style="background-color:#FFFFFF;" onLoad="CheckType();correspondenceNote();">
<iframe id="DivShim" src="javascript:false;" scrolling="no" frameborder="0" style="position:absolute; top:0px; left:0px; display:none;"></iframe>
<div id="Layer2" class="popUp" style="position:absolute; left:298px; top:26px; width:332px; height:600px;">
  <table width="100%">
    <tr class="popupHeader">
      <th>Dx Code</th>
      <th>Description</th>
      <th>
        <a href="#" onClick="formPopupHide();return false; return false;">X</a>
      </th>
    </tr>
  <%
    boolean flag = false;
    for (int i = 0; i < diaglist.length; i++) {
      flag = !flag;
      String rowClass = flag ? "odd" : "even";
  %>
    <tr class="<%=rowClass%>">
      <td>
        <strong>
          <a href="#" onClick="quickPickDiagnostic('<%=diaglist[i].getDiagnosticCode()%>');formPopupHide();return false;return false;"><%=diaglist[i].getDiagnosticCode()%>          </a>
        </strong>
      </td>
      <td colspan="2"><%=diaglist[i].getDescription()%>      </td>
    </tr>
  <%}  %>
  </table>
</div>

<!--  end popout layer  -->


<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr style="background-color:#000000;">
    <td height="40" width="10%">    </td>
    <td width="90%" align="left">
      <p>
        <font color="#FFFFFF" size="4">
          <strong>oscar<bean:message key="billing.bc.title"/></strong>
        </font>
      </p>
    </td>
  </tr>
</table>


<html:errors/>

<!-- end error row -->

<%
List<String> wcbneeds = (List) request.getAttribute("WCBFormNeeds");
if(wcbneeds != null){%>
<div>
    <h3>WCB Form needs:</h3>
    <ul>
    <%for (String s: wcbneeds) { %>
    <li><bean:message key="<%=s%>"/></li>
    <%}%>
    </ul>
</div>
<%}%>

<div class="container">
<html:form action="/billing/CA/BC/CreateBilling" onsubmit="toggleWCB();return checkUnits();">
  <input type="hidden" name="fromBilling" value=""/>

<%
  BillingCreateBillingForm thisForm;
  thisForm = (BillingCreateBillingForm) request.getSession().getAttribute("BillingCreateBillingForm");
  if (thisForm != null) {
    sxml_provider = ((String) thisForm.getXml_provider());
    sxml_location = ((String) thisForm.getXml_location());
    sxml_visittype = ((String) thisForm.getXml_visittype());
    if (sxml_location.compareTo("") == 0) {
      sxml_location = OscarProperties.getInstance().getProperty("visitlocation");
      sxml_visittype = OscarProperties.getInstance().getProperty("visittype");
      sxml_provider = bean.getApptProviderNo();
      thisForm.setXml_location(sxml_location);
      thisForm.setXml_provider(sxml_provider);
      thisForm.setXml_visittype(sxml_visittype);
      if ( OscarProperties.getInstance().getProperty("BC_DEFAULT_ALT_BILLING") != null && OscarProperties.getInstance().getProperty("BC_DEFAULT_ALT_BILLING").equalsIgnoreCase("YES")){
         thisForm.setXml_encounter("8");
      }
    }
    String apDate = thisForm.getXml_appointment_date();
    if (apDate != null && apDate.trim().length() == 0) {
      thisForm.setXml_appointment_date(bean.getApptDate());
    }
    if (bean != null && bean.getBillType() != null) {
      thisForm.setXml_billtype(bean.getBillType());
    }
    else if (request.getParameter("billType") != null) {
      thisForm.setXml_billtype(request.getParameter("billType"));
    }
    if (demo != null && demo.getVer() != null && demo.getVer().equals("66")) {
      thisForm.setDependent("66");
    }
    thisForm.setCorrespondenceCode(bean.getCorrespondenceCode());
    oscar.oscarBilling.ca.bc.data.BillingPreferencesDAO dao = SpringUtils.getBean(oscar.oscarBilling.ca.bc.data.BillingPreferencesDAO.class);
    oscar.oscarBilling.ca.bc.data.BillingPreference pref = null;
    //checking for a bug where the passed in provider number is actually "none" rather than numeral 0
    if (oscar.util.StringUtils.isNumeric(thisForm.getXml_provider())) {
      pref = dao.getUserBillingPreference((String) thisForm.getXml_provider());
    }
    String userReferralPref = "";
    if (pref != null) {
      if (pref.getReferral() == 1) {
        userReferralPref = "T";
      }
      else if (pref.getReferral() == 2) {
        userReferralPref = "B";
      }
      thisForm.setRefertype1(userReferralPref);
      thisForm.setRefertype2(userReferralPref);
    }
  }
%>

<!-- end ??? row -->


  <table width="100%" >
    <tr>
      <td>
        <table width="100%" id="billingPatientInfo">
          <tr id="patientIdRow" >
            <td colspan="5">
          
                <span class="badge badge-primary"><bean:message key="billing.patient"/></span>
                <strong><%=demo.getLastName()%>, <%=demo.getFirstName()%></strong>
            	
            	<span class="badge badge-primary"><bean:message key="billing.patient.age"/></span>  
            	<strong><%=demo.getAge()%></strong>
            	
            	<a class="badge badge-primary" href="javascript: void();" onclick="popup(800, 1000, 'billStatus.jsp?lastName=<%=demo.getLastName()%>&firstName=<%=demo.getFirstName()%>&filterPatient=true&demographicNo=<%=demo.getDemographicNo()%>','InvoiceList');return false;">
				<bean:message key="demographic.demographiceditdemographic.msgInvoiceList"/>
				</a>
 			
				<span class="badge badge-primary"><bean:message key="billing.patient.status"/></span> 
				<strong><%=demo.getPatientStatus()%></strong>

                <span class="badge badge-primary"><bean:message key="billing.patient.roster"/></span> 
                <strong><%=demo.getRosterStatus()%></strong>
         
                <span class="badge badge-primary"><bean:message key="billing.provider.assignedProvider"/></span>
                <strong><%=billform.getProviderName(demo.getProviderNo())%></strong>  
	
          </tr>

          <tr>
          	<td>

	          	<div class="form-group" > 
			      <div class='input-group select'> 
			        <strong><bean:message key="billing.billingform"/></strong>
			        
          		    <select class="form-control" id="selectBillingForm">
          		      <% for (int i = 0; i < billformlist.length; i++) { %>
          		       <option <% if( bean.getBillForm().equalsIgnoreCase( billformlist[i].getFormCode() ) ) {%> 
          		       				selected 
          		       			<% } %> 
          		      		value="billing.do?billRegion=<%=bean.getBillRegion()%>&billForm=<%=billformlist[i].getFormCode()%>&hotclick=&appointment_no=<%=bean.getApptNo()%>&demographic_name=<%=bean.getPatientName()%>&demographic_no=<%=bean.getPatientNo()%>&user_no=<%=bean.getCreator()%>&apptProvider_no=<%=bean.getApptProviderNo()%>&providerview=<%=bean.getProviderView()%>&appointment_date=<%=bean.getApptDate()%>&status=<%=bean.getApptStatus()%>&start_time=<%=bean.getApptStart()%>&bNewForm=1&billType=<%=bean.getBillForm()%>" >
          		      		<%= billformlist[i].getDescription() %>
          		      	</option>          		      
          		      <%} %>
          		    </select>
          		    
          		   </div>
          		</div>
          	</td>
          
             <td>
              <div class="form-group" > 
		      <div class='input-group select'>
		      
		        <strong><bean:message key="billing.provider.billProvider"/></strong>
                <html:select styleClass="form-control" property="xml_provider" value="<%=sxml_provider%>">
                  <html:option value="000000">
                    Select Provider
                  </html:option>
                <%for (int j = 0; j < billphysician.length; j++) {                %>
                  <html:option value="<%=billphysician[j].getProviderNo()%>"><%=billphysician[j].getProviderName()%>                  </html:option>
                <%}                %>
                </html:select>
                
                </div>
                </div>
            </td>

            <td>
                         <div class="form-group" > 
		      <div class='input-group select'>
           		 <bean:message key="billing.billingtype"/>
                <html:select styleClass="form-control" property="xml_billtype" onchange="CheckType();gotoPrivate();">
                  <html:option value="MSP">Bill MSP</html:option>
                  <html:option value="WCB">Bill WCB</html:option>
                  <html:option value="ICBC">Bill ICBC</html:option>
                  <html:option value="Pri">Private</html:option>
                  <html:option value="DONOTBILL">Do Not Bill</html:option>
                </html:select>
                
                </div>
                </div>
            </td>
            <td>
               <div class="form-group" > 
		      <div class='input-group select'>
                <strong>Clarification Code</strong>
                <html:select styleClass="form-control" property="xml_location">
                <%
                  for (int i = 0; i < billlocation.length; i++) {
                    String locationDescription = billlocation[i].getBillingLocation() + "|" + billlocation[i].getDescription();
                %>
                  <html:option value="<%=locationDescription%>"><%=billlocation[i].getDescription()%>                  </html:option>
                <%}                %>
                </html:select> 
                </div>
                </div>           
            </td>

            <td>
             <div class="form-group" > 
		      <div class='input-group select'>
		      
		      <strong>Service Location</strong>
                <html:select styleClass="form-control" property="xml_visittype">
                <%
                  for (int i = 0; i < billvisit.length; i++) {
                    String visitTypeDescription = billvisit[i].getVisitType() + "|" + billvisit[i].getDescription();
                %>
                  <html:option value="<%=visitTypeDescription%>"><%=visitTypeDescription%>                  </html:option>
                <%}                %>
                </html:select>
                
                </div>
                </div>
            </td>
          </tr>
        </table>
</td>
</tr>
<tr>
<td>
        <table width="100%" >
          <tr>
            <td>
				<div class="form-group" > 
		      <div class='input-group select'>
              <a href="javascript: function myFunction() {return false; }" id="hlSDate">
                  <strong><bean:message key="billing.servicedate"/></strong>
              </a>
              <html:text styleClass="form-control" property="xml_appointment_date" size="10" readonly="true" styleId="xml_appointment_date"/>
              </div>
              </div>
            </td>
            <td nowrap>
			<div class="form-group" > 
		      <div class='input-group text'>
              <a href="javascript: function myFunction() {return false; }" id="serviceToDate">
                  <strong>Service to date</strong>
              </a>
              <br />
              <html:text styleClass="form-control" property="service_to_date" size="2" maxlength="2" styleId="service_to_date"/>
              </div>
              </div>
            </td>
            <td>              
            <div class="form-group" > 
		      <div class='input-group select'>
            After Hours
              <html:select styleClass="form-control" property="afterHours">
                <html:option value="0">No</html:option>
                <html:option value="E">Evening</html:option>
                <html:option value="N">Night</html:option>
                <html:option value="W">Weekend</html:option>
              </html:select>
              </div>
              </div>
            </td>
            <td title="(HHMM 24hr):">
            <div class="form-group">
		         <div class='input-group date datetimepicker'>
                <strong>Time Call</strong>
              <html:text styleClass="form-control" property="timeCall" size="4" maxlength="4"/>
              </div>
              </div>
            </td>
            
			<td>
			    <strong>
                  <bean:message key="billing.servicedate.starttime"/>
                </strong>
		            <div class="form-group">
		                <div class='input-group date datetimepicker'> 
		                    <input type='text' id="serviceStartTime" class="form-control" />
		                  	<input type=hidden id="xml_starttime_hr" name="xml_starttime_hr" />
		                    <input type=hidden id="xml_starttime_min" name="xml_starttime_min" />
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-time"></span>
		                    </span>
		                </div>
		            </div>
			</td>
			
			<td>			
			    <strong>
                  <bean:message key="billing.servicedate.endtime"/>
                </strong>
		            <div class="form-group">
		                <div class='input-group date datetimepicker'>
		                    <input type='text' id="serviceEndTime" class="form-control" />
		                    <input type=hidden id="xml_endtime_hr" name="xml_endtime_hr" />
		                    <input type=hidden id="xml_endtime_min" name="xml_endtime_min" />
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-time"></span>
		                    </span>
		                </div>
		            </div>

			</td>
                        
            <td>
               <div class="form-group" > 
		      <div class='input-group select'>            
                         Dependent
              <html:select styleClass="form-control"  property="dependent">
                <html:option value="00">No</html:option>
                <html:option value="66">Yes</html:option>
              </html:select>
              </div>
              </div>
            </td>
            <td title="Submission Code">              
             <div class="form-group" > 
		      <div class='input-group select'>
            Sub Code
              <html:select styleClass="form-control" property="submissionCode">
                <html:option value="0">O - Normal</html:option>
                <html:option value="D">D - Duplicate</html:option>
                <html:option value="E">E - Debit</html:option>
                <html:option value="C">C - Subscriber Coverage</html:option>
                <html:option value="R">R - Resubmitted</html:option>
                <html:option value="I">I - ICBC Claim > 90 Days</html:option>
                <html:option value="A">A - Requested Preapproval</html:option>
                <html:option value="W">W - WCB Rejected Claim</html:option>
                <html:option value="X">X - Resubmitting Refused / Partially Paid Claim</html:option>
              </html:select>
              </div>
              </div>
            </td>
            <td>
            <div class="form-group" > 
		      <div class='input-group select'>
                <strong>Payment Method</strong>
            <%
              ArrayList types = billform.getPaymentTypes();
              if ("Pri".equalsIgnoreCase(thisForm.getXml_billtype())) {
                for (int i = 0; i < types.size(); i++) {
                  PaymentType item = (PaymentType) types.get(i);
                  if (item.getId().equals("6") || item.getId().equals("8")) {
                    types.remove(i);
                    break;
                  }
                }
              }
              else {
                for (int i = 0; i < types.size(); i++) {
                  PaymentType item = (PaymentType) types.get(i);
                  if (!item.getId().equals("6") && !item.getId().equals("8")) {
                    types.remove(i);
                    i = i - 1;
                  }
                }
              }
              request.setAttribute("paymentMethodList", types);
            %>
              <html:select styleClass="form-control" property="xml_encounter">
                <html:options collection="paymentMethodList" property="id" labelProperty="paymentType"/>
              </html:select>
              </div>
              </div>
            </td>
            <td nowrap>
              <a href="javascript: function myFunction() {return false; }" onClick="checkFACILITY();">
                  <strong>Facility</strong>
              </a>
  			<div class="form-group" style="display: none;" id="FACILITY" > 
		      <div class='input-group text'>
              	<table>  
              		<tr>   
              		<td title="Facilty Num">  
              			Fac Num <html:text styleClass="form-control"  property="facilityNum" size="5" maxlength="5"/> 
              		</td>   
              		<td title="Facilty Sub Num">  
              			Fac Sub Num <html:text styleClass="form-control"  property="facilitySubNum" size="5" maxlength="5"/> 
              		</td>  
              	</tr>  
              	</table>
              	</div>
              	</div>  

            </td>
          </tr>
        </table>
</td>
</tr>
<tr>
<td>
        <div style="display: none">
          <table>
            <tr>
              <td>
                            <div class="form-group" > 
		      <div class='input-group select'>
                <bean:message key="billing.admissiondate"/>
                :
                <html:text property="xml_vdate" readonly="true" value="" size="10" styleId="xml_vdate"/>
                <a id="hlADate">
                  <img title="Calendar" src="../../../images/cal.gif" alt="Calendar" border="0"/>
                </a>
                </div>
                </div>
              </td>
            </tr>
          </table>
        </div>
		<script type="text/javascript">
           Calendar.setup({inputField:"xml_appointment_date",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlSDate",singleClick:true,step:1});
           //Calendar.setup({inputField:"xml_appointment_date", ifFormat:""%d/%m/%Y",",button:"hlSDate", align:"Bl", singleClick:true});
           Calendar.setup({inputField:"xml_vdate",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlADate",singleClick:true,step:1});
           Calendar.setup({inputField:"service_to_date", ifFormat:"%d",button:"serviceToDate", align:"Bl", singleClick:true});
        </script>

        <div id="ICBC">
          <table>
            <tr>
              <td>
              <div class="form-group" > 
		      <div class='input-group text'>
                              ICBC Claim No:
                <html:text styleClass="form-control" property="icbc_claim_no" maxlength="8"/>
                </div>
                </div>
              </td>
               <td>
                            <div class="form-group" > 
		      <div class='input-group select'>  
                MVA:
                <html:select styleClass="form-control" property="mva_claim_code">
                  <html:option value="N">No</html:option>
                  <html:option value="Y">Yes</html:option>
                </html:select>
                </div>
                </div>
              </td>
            </tr>
          </table>
        </div>
</td>
</tr>
<tr>
<td>
        <table width="100%" id="billingFormTable">
          <tr>
            <td valign="top" style="width:32%; padding-right:5px;" >
              <table width="100%" border="1" class="serviceCodesTable" >
                <tr style="background-color:#CCCCFF;">
                  <td width="25%">
                    <div align="left">
                        <strong>
                          <%=group1Header%>
                        </strong>
                    </div>
                  </td>
                  <td width="61%" style="background-color:#CCCCFF;">
                    <strong>
                        <bean:message key="billing.service.desc"/>
                    </strong>
                  </td>
                  <td width="14%">
                    <div align="right">
                      <strong>&dollar;<bean:message key="billing.service.fee"/></strong>
                    </div>
                  </td>
                </tr>
              <%for (int i = 0; i < billlist1.length; i++) {              %>
                <tr >
                <%String svcCall = "addSvcCode('" + billlist1[i].getServiceCode() + "')";                %>
                  <td width="25%" valign="middle">
                    <label>
                      <html:multibox property="service" value="<%=billlist1[i].getServiceCode()%>" onclick="<%=svcCall%>"/>
                      <%=billlist1[i].getServiceCode()%>
                    </label>
                  </td>
                  <td width="61%">
                    <%=billlist1[i].getDescription()%> 
                  </td>
                  <td width="14%">
                    <div align="right">
                      <%=billlist1[i].getPrice()%>                 
                    </div>
                  </td>
                </tr>
              <%}              %>
              </table>
              <table width="100%" style="background-color:#CC0000;">
                <tr>
                  <td width="91%" valign="top">
                    <table width="100%" style="background-color:#EEEEFF;">
                      <tr>
                        <td>
                          <strong>
                              <bean:message key="billing.referral.doctor"/>
                          </strong>
                        </td>
                        <td>
                          <strong>
                              <bean:message key="billing.referral.type"/>
                          </strong>
                        </td>
                      </tr>
                      <tr>
                        <td>
                            <html:text property="xml_refer1" size="40" onkeypress="return grabEnter(event,'ReferralScriptAttach1()')"/>
                        </td>
                        <td>
                            <html:select property="refertype1">
                              <html:option value="">Select Type</html:option>
                              <html:option value="T">Refer To</html:option>
                              <html:option value="B">Refer By</html:option>
                            </html:select>
                        </td>
                      </tr>
                      <tr>
                        <td colspan="2">
                          <a href="javascript:ReferralScriptAttach('xml_refer1')">
                            <img src="../../../images/search_code.jpg" border="0">
                          </a>
                        </td>
                      </tr>
                      <tr>
                        <td>
                            <html:text property="xml_refer2" size="40" onkeypress="return grabEnter(event,'ReferralScriptAttach2()')"/>
                        </td>
                        <td>
                            <html:select property="refertype2">
                              <html:option value="">Select Type</html:option>
                              <html:option value="T">Refer To</html:option>
                              <html:option value="B">Refer By</html:option>
                            </html:select>
                        </td>
                      </tr>
                      <tr>
                        <td colspan="2">
                          <a href="javascript:ReferralScriptAttach('xml_refer2')">
                            <img src="../../../images/search_code.jpg" border="0">
                          </a>
                        </td>
                      </tr>
                    </table>
                  </td>
                  <td width="9%">
                   &nbsp;
                  </td>
                </tr>
                <tr>
                <td colspan="3" valign="top" >

                <table style="background-color:#fff;width:97%;" align="left">
                <tr><td width="50%" valign="top">
                
                <table style="background-color:#fff;width:100%;border:0">
                <tr><th colspan="2">Recent Referral Doctors Used</th></tr>
                  <%
                  String bgColor="#fff";
                  String rProvider = "";

		  if(recentList.size()>0){
                  for (String r : recentList){ 
                  rProvider = billingReferralDao.getReferralDocName(r);
                  %>
                	  <tr bgcolor="<%=bgColor%>"><td width="20%"><a href="javascript:void(0)" class="referral-doctor" data-num="<%=r%>" data-doc="<%=rProvider%>"><%=r%></a></td><td><%=rProvider%></td></tr> 
                  <%
                  if(bgColor=="#fff"){bgColor="#ccc";}else{bgColor="#fff";}
                  
                  }
		  }else{
		  %>
                	  <tr><td width="20%"></td><td>none</td></tr> 
		  <%
		  }
                  %>
                 </table> 
                 
                 </td>
                 <td width="50%" valign="top">
                 
                <table style="background-color:#fff;width:100%;border:0">
                <tr><th colspan="2">Referral Doctor on Master Record</th></tr>
                <tr><td width="20%"><a href="javascript:void(0)" title="Populate referral doctor from master record" class="referral-doctor" data-num="<%=mRecRefDoctorNum%>" data-doc="<%=mRecRefDoctor%>"><%=mRecRefDoctorNum%></a></td><td><%=mRecRefDoctor%></td></tr> 
                </table>
                
                </td></tr>
                 </table>
                 
                </td>
                </tr>
              </table>
              
            </td>
            <td valign="top" style="width:32%; padding-right:5px;">
              <table width="100%" border="1" class="serviceCodesTable">
                <tr style="background-color:#CCCCFF;">
                  <td width="21%">
                        <strong>
                          <%=group2Header%>
                        </strong>
                  </td>
                  <td width="60%" style="background-color:#CCCCFF;">
                    <strong><bean:message key="billing.service.desc"/></strong>
                  </td>
                  <td width="19%" align="right" >
                      <strong>&dollar;<bean:message key="billing.service.fee"/></strong>
                  </td>
                </tr>
              <%for (int i = 0; i < billlist2.length; i++) {              %>
                <tr >
                <%String svcCall = "addSvcCode('" + billlist2[i].getServiceCode() + "')";                %>
                  <td width="21%">
                  <label>
                      <html:multibox property="service" value="<%=billlist2[i].getServiceCode()%>" onclick="<%=svcCall%>"/>
                      <%=billlist2[i].getServiceCode()%>
                  </label>
                  </td>
                  <td width="60%">
                   <%=billlist2[i].getDescription()%>
                  </td>
                  <td width="19%">
                    <div align="right">
                     <%=billlist2[i].getPrice()%> 
                    </div>
                  </td>
                </tr>
              <%}              %>
              </table>
              <table style="background-color:#999900;">
                <tr>
                  <td width="91%" valign="top">
                    <table width="100%" style="background-color:#EEEEFF;">
                      <tr>
                        <td width="85%">
                          <strong><bean:message key="billing.service.otherservice"/></strong>
                        </td>
                        <td width="15%">
                          <strong><bean:message key="billing.service.unit"/></strong>
                        </td>
                      </tr>
                      <tr>
                        <td nowrap>
                            <html:text property="xml_other1" onblur="checkSelectedCodes()" size="40" onkeypress="return grabEnter(event,'OtherScriptAttach()')"/>
                            <input type="button" value=".5" onClick="$('xml_other1_unit').value = '0.5'"/>
                        </td>
                        <td>
                            <html:text property="xml_other1_unit" size="6" maxlength="6" styleId="xml_other1_unit"/>
                        </td>
                      </tr>
                      <tr>
                        <td nowrap>
                            <html:text property="xml_other2" onblur="checkSelectedCodes()" size="40" onkeypress="return grabEnter(event,'OtherScriptAttach()')"/>
                            <input type="button" value=".5" onClick="$('xml_other2_unit').value = '0.5'"/>
                        </td>
                        <td>
                            <html:text property="xml_other2_unit" size="6" maxlength="6" styleId="xml_other2_unit"/>
                        </td>
                      </tr>
                      <tr>
                        <td nowrap>
                            <html:text property="xml_other3" onblur="checkSelectedCodes()" size="40" onkeypress="return grabEnter(event,'OtherScriptAttach()')"/>
                            <input type="button" value=".5" onClick="$('xml_other3_unit').value = '0.5'"/>
                        </td>
                        <td>
                            <html:text property="xml_other3_unit" size="6" maxlength="6" styleId="xml_other3_unit"/>
                        </td>
                      </tr>
                      <tr>
                        <td colspan="2">
                          <a href="javascript:OtherScriptAttach()">
                            <img src="../../../images/search_code.jpg" border="0">
                          </a>
                        </td>
                      </tr>
                    </table>
                  </td>
                  <td width="9%">
                 	&nbsp;
                  </td>
                </tr>
              </table>
            </td>
            <td valign="top" style="width:32%;" >
              <table width="100%" border="1" class="serviceCodesTable">
                <tr style="background-color:#CCCCFF;">
                  <td width="25%" align="left" valign="middle">
                        <strong><%=group3Header%></strong>
                  </td>
                  <td width="61%" style="background-color:#CCCCFF;">
                    <strong><bean:message key="billing.service.desc"/></strong>
                  </td>
                  <td width="14%" align="right">      
                      <strong>&dollar;<bean:message key="billing.service.fee"/></strong>
                  </td>
                </tr>
              <%for (int i = 0; i < billlist3.length; i++) {              %>
                <tr >
                <%String svcCall = "addSvcCode('" + billlist3[i].getServiceCode() + "')";                %>
                  <td width="25%" >
                  	<label>
                      <html:multibox property="service" value="<%=billlist3[i].getServiceCode()%>" onclick="<%=svcCall%>"/>
                      <%=billlist3[i].getServiceCode()%>
                      </label>
                  </td>
                  <td width="61%" >
                    <%=billlist3[i].getDescription()%>
                  </td>
                  <td width="14%" align="right">
                      <%=billlist3[i].getPrice()%>
                  </td>
                </tr>
              <%}              %>
              </table>
              <!-- ONSCREEN DX CODE DISPLAY -->
              <table width="100%" style="background-color:#CCCCFF;">
                <tr>
                  <td valign="top" width="10%">
                    <table style="background-color:#EEEEFF;">
                      <tr>
                        <th align="left">
                              <a href="#" id="pop2" onClick="formPopup(this.id,'Layer2');return false;">
                                <bean:message key="billing.diagnostic.code"/>
                              </a>   
                        </th>
                      </tr>
                      <tr>
                        <td>
                            <html:text property="xml_diagnostic_detail1" size="25" onkeypress="return grabEnter(event,'ScriptAttach()')"/>
                        </td>
                      </tr>
                      <tr>
                        <td>
                            <html:text property="xml_diagnostic_detail2" size="25" onkeypress="return grabEnter(event,'ScriptAttach()')"/>
                        </td>
                      </tr>
                      <tr>
                        <td>
                            <html:text property="xml_diagnostic_detail3" size="25" onkeypress="return grabEnter(event,'ScriptAttach()')"/>
                        </td>
                      </tr>
                      <tr>
                        <td>
                            <a href="javascript:ScriptAttach()"><img src="../../../images/search_dx_code.jpg" border="0"></a> 
                        </td>
                      </tr>
                    </table>
                  </td>
                  <td align="left" valign="top">
                      <div id="DX_REFERENCE"></div>
                       <oscar:oscarPropertiesCheck property="BILLING_DX_REFERENCE" value="yes">
                         <script type="text/javascript">
                         function getDxInformation(origRequest){
                               var url = "DxReference.jsp";
                               var ran_number=Math.round(Math.random()*1000000);
                               var params = "demographicNo=<%=bean.getPatientNo()%>&rand="+ran_number;  //hack to get around ie caching the page
                               //alert(params);
                               new Ajax.Updater('DX_REFERENCE',url, {method:'get',parameters:params,asynchronous:true}); 
                               //alert(origRequest.responseText);
                         }
                         getDxInformation();
                         </script>
                       </oscar:oscarPropertiesCheck>
                     
                  </td>
                </tr>
              </table>
              <!-- ONSCREEN DX CODE DISPLAY END-->
              
              <table width="100%">
                <tr>
                  <td>
                      <strong>Short Claim Note</strong>
                    <html:text property="shortClaimNote" size="23" maxlength="20"/>
                  </td>
                  <td align="left">
                  	<label>
                        <input type="checkbox" name="ignoreWarn" /> 
                        Ignore Warnings
                    </label>
                  </td>
                </tr>
                
                <tr>
                  <td align="left" colspan="2" >
                    <html:select property="correspondenceCode" onchange="correspondenceNote();">
                      <html:option value="0">No Correspondence</html:option>
                      <html:option value="N">Electronic Correspondence</html:option>
                      <html:option value="C">Paper Correspondence</html:option>
                      <html:option value="B">Both</html:option>
                    </html:select>
                  </td>
                </tr>
                <tr>
                  <td colspan="2" valign="top">
                    <div id="CORRESPONDENCENOTE">
                      <html:textarea cols="60" rows="5" property="notes" onkeyup="checkTextLimit(this.form.notes,400);"></html:textarea>
                      400 characters max.
                    </div>
                    <div style="background-color: #CCCCFF;">
                      <div style="background-color : #EEEEFF;">
                      <strong>Billing Notes</strong> 
                      <span style="font-size:smaller;">(Notes are for internal use and will not be sent to MSP)</span>
                      </div>
                      <html:textarea cols="60" rows="5" property="messageNotes"></html:textarea>
                    </div>
                  </td>
                </tr>
                
              </table>
            </td>
          </tr>
          <tr id="buttonRow" >
           <td align="right" colspan="3">
              <input class="btn btn-md btn-primary" type="submit" name="Submit" value="Continue">
              <input class="btn btn-md btn-danger" type="button" name="Button" value="Cancel" onClick="window.close();">                 
            </td>
          </tr>
          <tr valign="top">
              <td colspan="3">
              	<div id="wcbForms" style="float:left;"></div>
              </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>

</html:form>
 </div>
</body>
</html>
