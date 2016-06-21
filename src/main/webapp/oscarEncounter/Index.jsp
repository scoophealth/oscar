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

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="oscar.oscarRx.data.RxPatientData"%>
<%@ taglib uri="http://www.caisi.ca/plugin-tag" prefix="plugin" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    String demographic$ = request.getParameter("demographicNo") ;
    boolean bPrincipalControl = false;
    boolean bPrincipalDisplay = false;
    
    LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
%>

<%
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_eChart");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<security:oscarSec roleName="<%=roleName$%>"
	objectName='<%="_eChart$"+demographic$%>' rights="o"
	reverse="<%=false%>">
You have no rights to access the data!
<% response.sendRedirect("../noRights.html"); %>
</security:oscarSec>

<%-- only principal has the save rights --%>
<security:oscarSec roleName='<%="_principal"%>'
	objectName='<%="_eChart"%>' rights="ow" reverse="<%=false%>">
	<% 	bPrincipalControl = true;
	if(EctPatientData.getProviderNo(loggedInInfo, demographic$).equals((String) session.getAttribute("user")) ) {
		bPrincipalDisplay = true;
	}
%>
</security:oscarSec>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/special_tag.tld" prefix="special" %>

<%@page
	import="oscar.log.*,oscar.util.UtilMisc,oscar.oscarEncounter.data.*, java.net.*,java.util.*,oscar.util.UtilDateUtilities"%>
<%@page
	import="oscar.oscarMDS.data.MDSResultsData,oscar.oscarLab.ca.on.*, oscar.oscarMessenger.util.MsgDemoMap, oscar.oscarMessenger.data.MsgMessageData"%>
<%@page
	import="oscar.oscarEncounter.oscarMeasurements.*,oscar.oscarResearch.oscarDxResearch.bean.*"%>
<% java.util.Properties oscarVariables = oscar.OscarProperties.getInstance(); %>

<%
	String ip = request.getRemoteAddr();
 	LogAction.addLog((String) session.getAttribute("user"), LogConst.READ, LogConst.CON_ECHART, demographic$, ip);
%>
<%
  //The oscarEncounter session manager, if the session bean is not in the context it looks for a session cookie with the appropriate name and value, if the required cookie is not available
  //it dumps you out to an erros page.

  oscar.oscarEncounter.pageUtil.EctSessionBean bean = null;
  if((bean=(oscar.oscarEncounter.pageUtil.EctSessionBean)request.getSession().getAttribute("EctSessionBean"))==null) {
    response.sendRedirect("error.jsp");
    return;
  }
%>
<!--add for special encounter-->
<plugin:hideWhenCompExists componentName="specialencounterComp" reverse="true">
<special:SpecialEncounterTag moduleName="eyeform">
<%
session.setAttribute("encounter_oscar_bean", bean);
session.setAttribute("encounter_bean_flag", "true");
String encounterurl=request.getContextPath()+"/mod/specialencounterComp/forward.jsp?method=view&demographicNo="+bean.demographicNo+"&providerNo="+bean.providerNo+"&providerName="+URLEncoder.encode(bean.userName);
session.setAttribute("encounter_oscar_baseurl",request.getContextPath());
if (request.getParameter("specalEncounter")==null)
{
	response.sendRedirect(encounterurl);
    return;
}
%>
</special:SpecialEncounterTag>
</plugin:hideWhenCompExists>
<!-- add by caisi  -->
<caisi:isModuleLoad moduleName="caisi">
	<%
session.setAttribute("casemgmt_oscar_baseurl",request.getContextPath());
session.setAttribute("casemgmt_oscar_bean", bean);
session.setAttribute("casemgmt_bean_flag", "true");
String hrefurl=request.getContextPath()+"/casemgmt/forward.jsp?action=view&demographicNo="+bean.demographicNo+"&providerNo="+bean.providerNo+"&providerName="+URLEncoder.encode(bean.userName);
if (request.getParameter("casetoEncounter")==null)
{
	response.sendRedirect(hrefurl);
    return;
}
%>
</caisi:isModuleLoad>
<!-- add by caisi end-->
<%
  //need these variables for the forms
  oscar.util.UtilDateUtilities dateConvert = new oscar.util.UtilDateUtilities();
  String demoNo = bean.demographicNo;
  String provNo = bean.providerNo;
  EctFormData.Form[] forms = EctFormData.getForms();
  EctPatientData.Patient pd = new EctPatientData().getPatient(loggedInInfo, demoNo);
  EctProviderData.Provider prov = new EctProviderData().getProvider(provNo);
  String patientName = pd.getFirstName()+" "+pd.getSurname();
  String patientAge = pd.getAge();
  String patientSex = pd.getSex();
  String providerName = bean.userName;
  String pAge = Integer.toString(dateConvert.calcAge(bean.yearOfBirth,bean.monthOfBirth,bean.dateOfBirth));
  java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);

  CommonLabResultData comLab = new CommonLabResultData();
  ArrayList labs = comLab.populateLabResultsData(loggedInInfo,"",demoNo, "", "","","U");
  Collections.sort(labs);

  String province = ((String ) oscarVariables.getProperty("billregion","")).trim().toUpperCase();
  Properties windowSizes = oscar.oscarEncounter.pageUtil.EctWindowSizes.getWindowSizes(provNo);

  MsgDemoMap msgDemoMap = new MsgDemoMap();
  List<String> msgVector = msgDemoMap.getMsgVector(demoNo);
  MsgMessageData msgData;

  EctSplitChart ectSplitChart = new EctSplitChart();
  Vector splitChart = ectSplitChart.getSplitCharts(demoNo);

  boolean sChart = true;
  if (splitChart == null || splitChart.size() == 0){
     sChart = false;
  }
%>




<%@page import="org.oscarehr.util.MiscUtils"%><html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="oscarEncounter.Index.title" /> - <oscar:nameage
	demographicNo="<%=demoNo%>" /></title>
<html:base />
<script language="javascript" type="text/javascript"
	src="../share/javascript/Oscar.js"></script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<!-- This is from OscarMessenger to get the top and left borders on -->
<link rel="stylesheet" type="text/css" href="encounterStyles.css">
<!--<script type="application/x-javascript" language="javascript" src="/javascript/sizing.js"></script>-->
<script type="text/javascript" language=javascript>
    var X       = 10;
    var pBSmall = 30;
    var small   = 60;
    var normal  = 166;
    var medium  = 272;
    var large   = 378;
    var full    = 649;
//tilde operator function variables
    var handlePressState = 0;
    var handlePressFocus;
    var handlePressFocus2 = "none";
    var keyPressed;
    var textValue1;
    var textValue2;
    var textValue3;
    var measurementWindows = "";

    function closeEncounterWindow() {
        return window.confirm("<bean:message key="oscarEncounter.Index.closeEncounterWindowConfirm"/>");
    }
    //function saveAndCloseEncounterWindow() {
    //    var x = window.confirm("<bean:message key="oscarEncounter.Index.confirmExit"/>");
    //    if(x) {window.close();}
    //}
    //get another encounter from the select list
    function onSplit() {
        document.forms['encForm'].btnPressed.value = 'Split Chart';
        var ret = confirm("<bean:message key="oscarEncounter.Index.confirmSplit"/>");
        return ret;
    }
    function popUpImmunizations(vheight,vwidth,varpage) {
        var page = varpage;
        windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
        var popup=window.open(varpage, "<bean:message key="global.immunizations"/>", windowprops);
    }

    function popUpMeasurements(vheight,vwidth,varpage) { //open a new popup window
      if(varpage!= 'null'){
          document.measurementGroupForm.measurementGroupSelect.options[0].selected = true;
          var page = "<rewrite:reWrite jspPage="oscarMeasurements/SetupMeasurements.do"/>?groupName=" + varpage;
          windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=600,screenY=200,top=0,left=0";
          measurementWindows = window.open(page, "<bean:message key="oscarEncounter.Index.popupPageWindow"/>", windowprops);
          if (measurementWindows != null) {
            if (measurementWindows.opener == null) {
              measurementWindows.opener = self;
              alert("<bean:message key="oscarEncounter.Index.popupPageAlert"/>");
            }
          }
      }
    }
    function popUpInsertTemplate(vheight,vwidth,varpage) { //open a new popup window
        //var x = window.confirm("<bean:message key="oscarEncounter.Index.insertTemplateConfirm"/>");
        //if(x) {
              if(varpage!= 'null'){
                  document.insertTemplateForm.templateSelect.options[0].selected=true;
                  var page = "<rewrite:reWrite jspPage="InsertTemplate.do"/>?templateName=" + varpage;
                  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=600,screenY=200,top=0,left=0";
                  var popup=window.open(page, "<bean:message key="oscarEncounter.Index.popupPageWindow"/>", windowprops);
                  if (popup != null) {
                    if (popup.opener == null) {
                      popup.opener = self;
                      alert("<bean:message key="oscarEncounter.Index.popupPageAlert"/>");
                    }
                  }
              }
          //}
    }

    function popupStart1(vheight,vwidth,varpage) {
        var page = varpage;
        windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
        var popup=window.open(varpage, "<bean:message key="oscarEncounter.Index.title"/>", windowprops);
    }
    function getAnotherEncounter(newAppointmentNo){
            varpage = "./IncomingEncounter.do?appointmentList=true&appointmentNo="+newAppointmentNo;
            location = varpage;
    }
    function insertTemplate(text){
        // var x = window.confirm("<bean:message key="oscarEncounter.Index.insertTemplateConfirm"/>");
        // if(x) {
            document.encForm.enTextarea.value = document.encForm.enTextarea.value + "\n\n" + text;
            document.encForm.enTextarea.value = document.encForm.enTextarea.value.replace(/\\u003E/g, "\u003E");
            document.encForm.enTextarea.value = document.encForm.enTextarea.value.replace(/\\u003C/g, "\u003C");
            document.encForm.enTextarea.value = document.encForm.enTextarea.value.replace(/\\u005C/g, "\u005C");
            document.encForm.enTextarea.value = document.encForm.enTextarea.value.replace(/\\u0022/g, "\u0022");
            document.encForm.enTextarea.value = document.encForm.enTextarea.value.replace(/\\u0027/g, "\u0027");
            window.setTimeout("document.encForm.enTextarea.scrollTop=2147483647", 0);  // setTimeout is needed to allow browser to realize that text field has been updated
            document.encForm.enTextarea.focus();
        // }
    }

    function refresh() {
        var u = self.location.href;
        if(u.lastIndexOf("&status=B") > 0) {
            self.location.href = u.substring(0,u.lastIndexOf("&status=B")) + "&status=P"  ;
        } else if (u.lastIndexOf("&status=") > 0) {
            self.location.href = u.substring(0,u.lastIndexOf("&status=")) + "&status=B"  ;
        } else {
            history.go(0);
        }
     	  self.opener.refresh();
    }
    function onUnbilled(url) {
        if(confirm("<bean:message key="oscarEncounter.Index.onUnbilledConfirm"/>")) {
            popupPage(700,720, url);
        }
    }
    function popupPage2(varpage) {
        var page = "" + varpage;
        windowprops = "height=600,width=700,location=no,"
          + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";
        window.open(page, "<bean:message key="oscarEncounter.Index.popupPage2Window"/>", windowprops);
    }

    function urlencode(str) {
        var ns = (navigator.appName=="Netscape") ? 1 : 0;
        if (ns) { return escape(str); }
        var ms = "%25#23 20+2B?3F<3C>3E{7B}7D[5B]5D|7C^5E~7E`60";
        var msi = 0;
        var i,c,rs,ts ;
        while (msi < ms.length) {
            c = ms.charAt(msi);
            rs = ms.substring(++msi, msi +2);
            msi += 2;
            i = 0;
            while (true)	{
                i = str.indexOf(c, i);
                if (i == -1) break;
                ts = str.substring(0, i);
                str = ts + "%" + rs + str.substring(++i, str.length);
            }
        }
        return str;
    }
    function popupStart(vheight,vwidth,varpage) {
      var page = varpage;
      windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
      var popup=window.open(varpage, "", windowprops);
      if (popup != null) {
        if (popup.opener == null) {
          popup.opener = self;
        }
      }
    }

function reset() {
    document.encForm.shTextarea.style.overflow="auto";
    document.encForm.fhTextarea.style.overflow="auto";
    document.encForm.mhTextarea.style.overflow="auto";
    document.encForm.shTextarea.style.height=small;
    document.encForm.fhTextarea.style.height=small;
    document.encForm.mhTextarea.style.height=small;
    document.encForm.rowOneSize.value=small;
    document.encForm.ocTextarea.style.overflow="auto";
    document.encForm.reTextarea.style.overflow="auto";
    document.encForm.ocTextarea.style.height=small;
    document.encForm.reTextarea.style.height=small;
    document.encForm.rowTwoSize.value=small;
    document.encForm.enTextarea.style.overflow="auto";
    document.encForm.enTextarea.style.height=large;
    document.encForm.rowThreeSize.value=large;
    document.getElementById("presBox").style.height=pBSmall;
    document.getElementById("allergyBox").style.height=pBSmall;
    document.encForm.presBoxSize.value=pBSmall;
}
function rowOneX(){
    document.encForm.shTextarea.style.overflow="auto";
    document.encForm.fhTextarea.style.overflow="auto";
    document.encForm.mhTextarea.style.overflow="auto";
    document.encForm.shTextarea.style.height=X;
    document.encForm.fhTextarea.style.height=X;
    document.encForm.mhTextarea.style.height=X;
    document.encForm.rowOneSize.value=X;
}
function rowOneSmall(){
    document.encForm.shTextarea.style.overflow="auto";
    document.encForm.fhTextarea.style.overflow="auto";
    document.encForm.mhTextarea.style.overflow="auto";
    document.encForm.shTextarea.style.height=small;
    document.encForm.fhTextarea.style.height=small;
    document.encForm.mhTextarea.style.height=small;
    document.encForm.rowOneSize.value=small;
}
function rowOneNormal(){
    document.encForm.shTextarea.style.overflow="auto";
    document.encForm.fhTextarea.style.overflow="auto";
    document.encForm.mhTextarea.style.overflow="auto";
    document.encForm.shTextarea.style.height=normal;
    document.encForm.fhTextarea.style.height=normal;
    document.encForm.mhTextarea.style.height=normal;
    document.encForm.rowOneSize.value=normal;
}
function rowOneLarge(){
    document.encForm.shTextarea.style.overflow="auto";
    document.encForm.fhTextarea.style.overflow="auto";
    document.encForm.mhTextarea.style.overflow="auto";
    document.encForm.shTextarea.style.height=large;
    document.encForm.fhTextarea.style.height=large;
    document.encForm.mhTextarea.style.height=large;
    document.encForm.rowOneSize.value=large;
}
function rowOneFull(){
    document.encForm.shTextarea.style.overflow="auto";
    document.encForm.fhTextarea.style.overflow="auto";
    document.encForm.mhTextarea.style.overflow="auto";
    document.encForm.shTextarea.style.height=full;
    document.encForm.fhTextarea.style.height=full;
    document.encForm.mhTextarea.style.height=full;
    document.encForm.shInput.scrollIntoView(top);
    document.encForm.rowOneSize.value=full;
}
function rowTwoX(){
    document.encForm.ocTextarea.style.overflow="auto";
    document.encForm.reTextarea.style.overflow="auto";
    document.encForm.ocTextarea.style.height=X;
    document.encForm.reTextarea.style.height=X;
    document.encForm.rowTwoSize.value=X;
}
function rowTwoSmall(){
    document.encForm.ocTextarea.style.overflow="auto";
    document.encForm.reTextarea.style.overflow="auto";
    document.encForm.ocTextarea.style.height=small;
    document.encForm.reTextarea.style.height=small;
    document.encForm.rowTwoSize.value=small;
}
function rowTwoNormal(){
    document.encForm.ocTextarea.style.overflow="auto";
    document.encForm.reTextarea.style.overflow="auto";
    document.encForm.ocTextarea.style.height=normal;
    document.encForm.reTextarea.style.height=normal;
    document.encForm.rowTwoSize.value=normal;
}
function rowTwoLarge(){
    document.encForm.ocTextarea.style.overflow="auto";
    document.encForm.reTextarea.style.overflow="auto";
    document.encForm.ocTextarea.style.height=large;
    document.encForm.reTextarea.style.height=large;
    document.encForm.rowTwoSize.value=large;
}
function rowTwoFull(){
    document.encForm.ocTextarea.style.overflow="auto";
    document.encForm.reTextarea.style.overflow="auto";
    document.encForm.ocTextarea.style.height=full;
    document.encForm.reTextarea.style.height=full;
    document.encForm.ocInput.scrollIntoView(top);
    document.encForm.rowTwoSize.value=full;
}

function rowThreeX(){
    document.encForm.enTextarea.style.overflow="auto";
    document.encForm.enTextarea.style.height=X;
    document.encForm.rowThreeSize.value=X;
}
function rowThreeSmall(){
    document.encForm.enTextarea.style.overflow="auto";
    document.encForm.enTextarea.style.height=small;
    document.encForm.rowThreeSize.value=small;
}
function rowThreeNormal(){
    document.encForm.enTextarea.style.overflow="auto";
    document.encForm.enTextarea.style.height=normal;
    document.encForm.rowThreeSize.value=normal;
}
function rowThreeMedium(){
    document.encForm.enTextarea.style.overflow="auto";
    document.encForm.enTextarea.style.height=medium;
    document.encForm.rowThreeSize.value=medium;
}
function rowThreeLarge(){
    document.encForm.enTextarea.style.overflow="auto";
    document.encForm.enTextarea.style.height=large;
    document.encForm.rowThreeSize.value=large;
}
function rowThreeFull(){
    document.encForm.enTextarea.style.overflow="auto";
    document.encForm.enTextarea.style.height=full;
    document.encForm.enInput.scrollIntoView(top);
    document.encForm.rowThreeSize.value=full;
}

function presBoxX(){
    document.getElementById("presBox").style.height=X;
    document.getElementById("allergyBox").style.height=X;
    document.encForm.presBoxSize.value=X;
}
function presBoxSmall(){
    document.getElementById("presBox").style.height=pBSmall;
    document.getElementById("allergyBox").style.height=pBSmall;
    document.encForm.presBoxSize.value=pBSmall;
}
function presBoxNormal(){
    document.getElementById("presBox").style.height=normal;
    document.getElementById("allergyBox").style.height=normal;
    document.encForm.presBoxSize.value=normal;
}
function presBoxLarge(){
    document.getElementById("presBox").style.height=large;
    document.getElementById("allergyBox").style.height=large;
    document.encForm.presBoxSize.value=large;
}
function presBoxFull(){
    document.getElementById("presBox").style.height=full;
    document.getElementById("allergyBox").style.height=full;
    document.getElementById("presTopTable").scrollIntoView(top);
    document.encForm.presBoxSize.value=full;
}
<!--new functions end from jay-->

function popupSearchPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprop = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=0,left=0";
  var popup=window.open(page, "<bean:message key="oscarEncounter.Index.popupSearchPageWindow"/>", windowprop);
}


if (!document.all) document.captureEvents(Event.MOUSEUP);
document.onmouseup = getActiveText;

var encTextareaFocus = false;

function getActiveText(e) {
  //text = (document.all) ? document.selection.createRange().text : document.getSelection();
  //document.ksearch.keyword.value = text;
 if(document.all) {
    //alert("one");
    text = document.selection.createRange().text;
    if(text != "" && document.forms['ksearch'].keyword.value=="") {
      document.forms['ksearch'].keyword.value += text;
    }
    if(text != "" && document.forms['ksearch'].keyword.value!="") {
      document.forms['ksearch'].keyword.value = text;
    }
  } else {
    text = window.getSelection();

    if ((text.toString().length == 0) && (encTextareaFocus == true)) {  //for firefox
       var txtarea = document.encForm.enTextarea;
       var selLength = txtarea.textLength;
       var selStart = txtarea.selectionStart;
       var selEnd = txtarea.selectionEnd;
       if (selEnd==1 || selEnd==2) selEnd=selLength;
       text = (txtarea.value).substring(selStart, selEnd);
    }
    if (text != '') {
        document.forms['ksearch'].keyword.value = text;
        text = '';
    }
  }
  return true;
}

function tryAnother(){
////
    var txt = "null";
    var foundIn = "null";
    if (window.getSelection){
		txt = window.getSelection();
		foundIn = 'window.getSelection()';
	 }else if (document.getSelection){
		txt = document.getSelection();
		foundIn = 'document.getSelection()';
	 }else if (document.selection){
		txt = document.selection.createRange().text;
		foundIn = 'document.selection.createRange()';
	 }
	 alert (txt+"\n"+foundIn);
    ////
}
function popupPageK(page) {
    windowprops = "height=700,width=1024,location=no,"
    + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";
    var popup = window.open(page, "<bean:message key="oscarEncounter.Index.popupPageKWindow"/>", windowprops);
    popup.focus();
}
function selectBox(name) {
    to = name.options[name.selectedIndex].value;
    name.selectedIndex =0;
    if(to!="null")
      popupPageK(to);
}
function popupOscarRx(vheight,vwidth,varpage) {
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "<bean:message key="global.oscarRx"/>", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
  popup.focus();
}
function popupOscarCon(vheight,vwidth,varpage) {
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "<bean:message key="oscarEncounter.Index.msgOscarConsultation"/>", windowprops);
  popup.focus();
}


function popupOscarComm(vheight,vwidth,varpage) {
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "<bean:message key="global.oscarComm"/>", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
  popup.focus();
}

function popUpMsg(vheight,vwidth,msgPosition) {


  var page = "<rewrite:reWrite jspPage="../oscarMessenger/ViewMessageByPosition.do"/>?from=encounter&orderBy=!date&demographic_no=<%=demoNo%>&messagePosition="+msgPosition;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(page, "<bean:message key="global.oscarRx"/>", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
  popup.focus();
}

//function sign(){
//        document.encForm.enTextarea.value =document.encForm.enTextarea.value +"\n[<bean:message key="oscarEncounter.Index.signed"/> <%=dateConvert.DateToString(bean.currentDate)%> <bean:message key="oscarEncounter.Index.by"/> <%=bean.userName%>]";
//}

//function saveEncounter(){
//        location="SaveEncounter.do";
//}

//function saveSignEncounter(){
//        location="SaveEncounter.do";
//}

function loader(){
    window.focus();
    var tmp;

    document.encForm.enTextarea.focus();
    document.encForm.enTextarea.value = document.encForm.enTextarea.value + "";
    document.encForm.enTextarea.scrollTop = document.encForm.enTextarea.scrollHeight;

    <%String popUrl = request.getParameter("popupUrl");
      if (popUrl != null){           %>
      window.setTimeout("popupPageK('<%=popUrl%>')", 2);
    <%}%>

    //tmp = document.encForm.enTextarea.value; // these two lines cause the enTextarea to scroll to the bottom (only works in IE)
    //document.encForm.enTextarea.value = tmp; // another option is to use window.setTimeout("document.encForm.enTextarea.scrollTop=2147483647", 0);  (also only works in IE)
}

function notifyChildren() {
    if( !measurementWindows.closed )
        measurementWindows.parentChanged = true;
}
</script>
<script language="javascript">


/////
function showpic(picture,id){
     if (document.getElementById){ // Netscape 6 and IE 5+
        var targetElement = document.getElementById(picture);
        var bal = document.getElementById(id);

        var offsetTrail = document.getElementById(id);
        var offsetLeft = 0;
        var offsetTop = 0;
        while (offsetTrail) {
           offsetLeft += offsetTrail.offsetLeft;
           offsetTop += offsetTrail.offsetTop;
           offsetTrail = offsetTrail.offsetParent;
        }
        if (navigator.userAgent.indexOf("Mac") != -1 &&
           typeof document.body.leftMargin != "undefined") {
           offsetLeft += document.body.leftMargin;
           offsetTop += document.body.topMargin;
        }

        targetElement.style.left = offsetLeft +bal.offsetWidth;
        targetElement.style.top = offsetTop;
        targetElement.style.visibility = 'visible';
     }
  }

  function hidepic(picture){
     if (document.getElementById){ // Netscape 6 and IE 5+
        var targetElement = document.getElementById(picture);
        targetElement.style.visibility = 'hidden';
     }
  }


/////





function popperup(vheight,vwidth,varpage,pageName) { //open a new popup window
  hidepic('Layer1');
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",status=yes,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=100,left=100";
  var popup=window.open(varpage, pageName, windowprops);
      popup.opener = self;
  popup.focus();
}


function grabEnter(event){
  if(window.event && window.event.keyCode == 13){
      popupSearchPage(600,800,document.forms['ksearch'].channel.options[document.forms['ksearch'].channel.selectedIndex].value+urlencode(document.forms['ksearch'].keyword.value));
      return false;
  }else if (event && event.which == 13){
      popupSearchPage(600,800,document.forms['ksearch'].channel.options[document.forms['ksearch'].channel.selectedIndex].value+urlencode(document.forms['ksearch'].keyword.value));
      return false;
  }
}
</script>

<script language="javascript" type="text/javascript"
	src="../share/javascript/prototype.js"></script>
<script type="text/javascript" language=javascript>
// Written by wreby
// This code supports the autosave function
//autoSaveTimer is in milliseconds
var autoSaveTimerLength = <%= oscar.OscarProperties.getInstance().getProperty("ECT_AUTOSAVE_TIMER","-1") %>;
var SaveFeedbackTimerLength = <%=oscar.OscarProperties.getInstance().getProperty("ECT_SAVE_FEEDBACK_TIMER","2500") %>;
var request = null;
if (autoSaveTimerLength > 0)  {
    var autoSaveTimer = setTimeout("AutoSaveEncounter()", autoSaveTimerLength);
}
// disable all elements of type "button" on the passed in form
function disableButtons(formContainingButtons)  {
    for (i = 0; i < formContainingButtons.elements.length; i++)  {
        if (formContainingButtons.elements[i].type == "button")  {
            formContainingButtons.elements[i].disabled = true;
        }
    }
}

// enable all elements of type "button" on the passed in form
function enableButtons(formContainingButtons)  {
    for (i = 0; i < formContainingButtons.elements.length; i++)  {
        if (formContainingButtons.elements[i].type == "button")  {
            formContainingButtons.elements[i].disabled = false;
        }
    }
}

// This is an asynchronous function that automatically saves the encounter when
// the timer goes off.
function AutoSaveEncounter() {
    var form = document.forms['encForm'];
    disableButtons(form);
    form.btnPressed.value='AutoSave';
    form.submitMethod.value='ajax';
    var pars = Form.serialize(form);
    //send off the request
    request = new Ajax.Request('SaveEncounter.do', {method: 'post',
                                                  postBody: pars,
                                                  asynchronous:  true,
                                                  onSuccess: AutoSaveSuccess,
                                                  onFailure: AjaxSubmitFailure});
}

function AutoSaveSuccess(request)  {
    AjaxSubmitSuccess(request);
}

// This function will submit a form synchronously.
// The nice thing about using ajax to submit the form is
// that we can now programmatically decide what to do with the
// results
function AjaxSubmit(formToSubmit)  {
    if (autoSaveTimer)  {
        clearTimeout(autoSaveTimer);
    }
    disableButtons(formToSubmit);
    formToSubmit.submitMethod.value='ajax';
    var pars = Form.serialize(formToSubmit);
     //send off the request
    request = new Ajax.Request('SaveEncounter.do', {method: 'post',
                                                  postBody: pars,
                                                  asynchronous:  true,
                                                  onSuccess: AjaxSubmitSuccess,
                                                  onFailure: AjaxSubmitFailure});
}

// request was successful, so display the returned page
function AjaxSubmitSuccess(request)  {
    giveSaveFeedback();
    enableButtons(document.forms['encForm']);
    if (autoSaveTimerLength > 0)  {
        autoSaveTimer=setTimeout("AutoSaveEncounter()", autoSaveTimerLength);
    }
}

// The request was not successfully handled
function AjaxSubmitFailure(request)  {
    alert("<bean:message key="oscarEncounter.concurrencyError.errorMsg" />");
}

function giveSaveFeedback()  {
    document.getElementById('saveFeedbackRow').style.backgroundColor="yellow";
    document.getElementById('saveFeedbackText').style.display="inline";
    saveFeedbackTimer = setTimeout("removeSaveFeedback()", SaveFeedbackTimerLength);
}

function removeSaveFeedback()  {
    document.getElementById('saveFeedbackRow').style.backgroundColor="#CCCCFF";
    document.getElementById('saveFeedbackText').style.display="none";
}

</script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

</head>

<body onload="javascript:loader();" onUnload="notifyChildren();"
	topmargin="0" leftmargin="0" bottommargin="0" rightmargin="0"
	vlink="#0000FF">
<html:errors />

<table border="0" cellpadding="0" cellspacing="0"
	style="border-collapse: collapse; width: 100%; height: 680"
	bordercolor="#111111" id="scrollNumber1"
	name="<bean:message key="oscarEncounter.Index.encounterTable"/>">
	<tr>
		<td class="hidePrint" bgcolor="#003399"
			style="border-right: 2px solid #A9A9A9; height: 34px;">
		<div class="Title">&nbsp;<bean:message
			key="oscarEncounter.Index.msgEncounter" /></div>
		</td>

		<td bgcolor="#003399"
			style="text-align: right; height: 34px; padding-left: 3px;">
		<table name="tileTable"
			style="vertical-align: middle; border-collapse: collapse;">
			<form name="appointmentListForm"
				action="./oscarEncounter.Index/IncomingEncounter.do"><caisi:isModuleLoad
				moduleName="caisi">
				<input type="hidden" name="casetoEncounter" value="true">
			</caisi:isModuleLoad>
			<tr>
				<td width=70% class="Header"
					style="padding-left: 2px; padding-right: 2px; border-right: 2px solid #003399; text-align: left; font-size: 80%; font-weight: bold; width: 100%;"
					NOWRAP><%=bean.patientLastName %>, <%=bean.patientFirstName%>
				<%=bean.patientSex%> <%=bean.patientAge%> <span
					style="margin-left: 20px;"><i>Next Appointment: <oscar:nextAppt
					demographicNo="<%=bean.demographicNo%>" /></i></span></td>
				<td class="Header"
					style="text-align: center; border-right: 3px solid #003399" NOWRAP>
				<!--div class="FakeLink">
                        </div--> <!-- oscar:help keywords="echart" key="app.top1"/--> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a></td>
			</tr>
			</form>
		</table>

		</td>
	</tr>
	<tr style="height: 100%">
		<td
			style="font-size: 80%; border-top: 2px solid #A9A9A9; border-right: 2px solid #A9A9A9; vertical-align: top">
		<table class="LeftTable">
			<caisi:isModuleLoad moduleName="caisi">
				<%String hrefurl2=request.getContextPath()+"/casemgmt/forward.jsp?action=view&demographicNo="+bean.demographicNo+"&providerNo="+bean.providerNo+"&providerName="+URLEncoder.encode(bean.userName);
%>
				<tr>
					<td><a href="<%=hrefurl2%>">Case Management Encounter</a></td>
				</tr>
			</caisi:isModuleLoad>
			<tr class="Header">
				<td style="font-weight: bold"><bean:message
					key="oscarEncounter.Index.clinicalModules" /></td>
			</tr>
			<tr>
				<td>
				<a
					href="javascript: function myFunction() {return false; }"
					onClick="popup(700,1000,'../demographic/demographiccontrol.jsp?demographic_no=<%=bean.demographicNo%>&displaymode=edit&dboperation=search_detail','master')"
					title="<bean:message key="provider.appointmentProviderAdminDay.msgMasterFile"/>"><bean:message
					key="global.master" /></a>
				<br>
				<%
               if(bean.status.indexOf('B')==-1) { %>
				<a href=#
					onClick='popupPage(700,1000, "../billing.do?billRegion=<%=URLEncoder.encode(province)%>&billForm=<%=URLEncoder.encode(oscarVariables.getProperty("default_view"))%>&hotclick=<%=URLEncoder.encode("")%>&appointment_no=<%=bean.appointmentNo%>&demographic_name=<%=URLEncoder.encode(bean.patientLastName+","+bean.patientFirstName)%>&demographic_no=<%=bean.demographicNo%>&providerview=<%=bean.curProviderNo%>&user_no=<%=bean.providerNo%>&apptProvider_no=<%=bean.curProviderNo%>&appointment_date=<%=bean.appointmentDate%>&start_time=<%=bean.startTime%>&bNewForm=1&status=t");return false;'
					title="<bean:message key="global.billing"/>"><bean:message
					key="global.billing" /></a> <% } else {%> <!--a href=# onClick='onUnbilled("../billing/billingDeleteWithoutNo.jsp?appointment_no=<%=bean.appointmentNo%>");return false;' title="<bean:message key="global.unbil"/>">-<bean:message key="global.billing"/></a-->
				<a href=#
					onClick='onUnbilled("../billing/CA/<%=province%>/billingDeleteWithoutNo.jsp?status=<%=bean.status%>&appointment_no=<%=bean.appointmentNo%>");return false;'
					title="<bean:message key="global.unbil"/>">-<bean:message
					key="global.billing" /></a> <% } %> <br>
				<a href=#
					onClick="popupOscarRx(700,1027,'../oscarRx/choosePatient.do?providerNo=<%=bean.providerNo%>&demographicNo=<%=bean.demographicNo%>');return false;"><bean:message
					key="global.prescriptions" /></a><br>
				<a href=#
					onClick="popupOscarCon(700,960,'<rewrite:reWrite jspPage="oscarConsultationRequest/DisplayDemographicConsultationRequests.jsp"/>?de=<%=bean.demographicNo%>');return false;"><bean:message
					key="global.consultations" /></a><br>

				<oscar:oscarPropertiesCheck property="IMMUNIZATION" value="yes"
					defaultVal="true">
					<% if (oscar.oscarEncounter.immunization.data.EctImmImmunizationData.hasImmunizations(demoNo)) { %>
					<a style="color: red"
						href="javascript:popUpImmunizations(700,960,'<rewrite:reWrite jspPage="immunization/initSchedule.do"/>')"><bean:message
						key="global.immunizations" /></a>
					<br>
					<% } else {%>
					<a
						href="javascript:popUpImmunizations(700,960,'<rewrite:reWrite jspPage="immunization/initSchedule.do"/>')"><bean:message
						key="global.immunizations" /></a>
					<br>
					<% } %>
				</oscar:oscarPropertiesCheck> <oscar:oscarPropertiesCheck property="PREVENTION" value="yes">
					<a
						href="javascript:popUpImmunizations(700,960,'../oscarPrevention/index.jsp?demographic_no=<%=bean.demographicNo%>')">
					<oscar:preventionWarnings demographicNo="<%=bean.demographicNo%>">prevention</oscar:preventionWarnings></a>
					<br>
				</oscar:oscarPropertiesCheck> <%  if (oscar.OscarProperties.getInstance().getProperty("oscarcomm","").equals("on")) { %>
				<a href="javascript:popupOscarComm(700,960,'RemoteAttachments.jsp')"><bean:message
					key="global.oscarComm" /></a><br>
				<% } %> <a href=#
					onClick="popupOscarComm(580,900,'../oscarResearch/oscarDxResearch/setupDxResearch.do?demographicNo=<%=bean.demographicNo%>&providerNo=<%=provNo%>&quickList=');return false;"><bean:message
					key="global.disease" /></a><br>
				<a href=#
					onClick="popupOscarCon(580,800,'../appointment/appointmentcontrol.jsp?keyword=<%=URLEncoder.encode(bean.patientLastName+","+bean.patientFirstName)%>&displaymode=<%=URLEncoder.encode("Search ")%>&search_mode=search_name&originalpage=<%=URLEncoder.encode("../tickler/ticklerAdd.jsp")%>&orderby=last_name&appointment_date=2000-01-01&limit1=0&limit2=5&status=t&start_time=10:45&end_time=10:59&duration=15&dboperation=search_demorecord&type=&demographic_no=<%=bean.demographicNo%>');return false;"><bean:message
					key="oscarEncounter.Index.addTickler" /></a><br>
				</td>
			</tr>
			<!-- <tr><td>&nbsp;</td></tr> -->
		</table>

		<table class="LeftTable">
			<form name="leftColumnForm"><caisi:isModuleLoad
				moduleName="caisi">
				<input type="hidden" name="casetoEncounter" value="true">
			</caisi:isModuleLoad>
			<tr class="Header">
				<td style="font-weight: bold"><bean:message
					key="oscarEncounter.Index.msgForms" /></td>
			</tr>
			<tr>
				<td><select name="selectCurrentForms"
					onChange="javascript:selectBox(this)" class="ControlSelect"
					onMouseOver="javascript:window.status='View any of <%=patientName%>\'s current forms.'; return true;">
					<option value="null" selected>-<bean:message
						key="oscarEncounter.Index.currentForms" />- <%
                            for(int j=0; j<forms.length; j++) {
                                EctFormData.Form frm = forms[j];
                                String table = frm.getFormTable();
                                if(!table.equalsIgnoreCase("")){
                                    EctFormData.PatientForm[] pforms = EctFormData.getPatientForms(demoNo, table);
                                    if(pforms.length>0) {
                                        EctFormData.PatientForm pfrm = pforms[0];
                                %>

					<option
						value="<%="../form/forwardshortcutname.jsp?formname="+frm.getFormName()+"&demographic_no="+demoNo%>"><%=frm.getFormName()%>&nbsp;Cr:<%=pfrm.getCreated()%>&nbsp;Ed:<%=pfrm.getEdited()%>
					<%}}}

                            %>

				</select></td>
			</tr>
			<tr>
				<td><select name="selectForm"
					onChange="javascript:selectBox(this)" class="ControlSelect"
					onMouseOver="javascript:window.status='<bean:message key="oscarEncounter.Index.createForm"/> <%=patientName%>.'; return true;">
					<option value="null" selected>-<bean:message
						key="oscarEncounter.Index.addForm" />- <%
                        for(int j=0; j<forms.length; j++) {
                            EctFormData.Form frm = forms[j];
                            if (!frm.isHidden()) {
                        %>

					<option
						value="<%=frm.getFormPage()+demoNo+"&formId=0&provNo="+provNo%>"><%=frm.getFormName()%>
					<%

                            }
                        }

                        %>

				</select></td>
			</tr>
			<tr>
				<td><a href=#
					onClick='popupPage2("<rewrite:reWrite jspPage="formlist.jsp"/>?demographic_no=<%=demoNo%>"); return false;'>
				-<bean:message key="oscarEncounter.Index.msgOldForms" />-</a></td>
			</tr>

			<!-- <tr><td>&nbsp;</td></tr> -->
			</form>
		</table>
		<table class="LeftTable">
			<form name="msgForm"><caisi:isModuleLoad moduleName="caisi">
				<input type="hidden" name="casetoEncounter" value="true">
			</caisi:isModuleLoad>
			<tr class="Header">
				<td style="font-weight: bold">oscarMessenger <a
					href="javascript: function myFunction() {return false; }"
					onClick="popup(700,960,'../oscarMessenger/SendDemoMessage.do?demographic_no=<%=demoNo%>','msg')">Send
				Msg</a></td>
			</tr>
			<tr>
				<td><select name="msgSelect" class="ControlSelect"
					onChange="javascript:popUpMsg(600,900,document.msgForm.msgSelect.options[document.msgForm.msgSelect.selectedIndex].value)">
					<option value="null" selected>-Select Message- <%
                            String msgId;
                            String msgSubject;
                            String msgDate;
                            for(int j=0; j<10 && j<msgVector.size(); j++) {
                                msgId = (String) msgVector.get(j);
                                msgData = new MsgMessageData(msgId);
                                msgSubject = msgData.getSubject();
                                msgDate = msgData.getDate();
                         %>

					<option value="<%=j%>"><%=msgSubject + " - " + msgDate %></option>
					<% }%>
				</select></td>
				</td>
			</tr>
			<tr>
				<td><a href=#
					onClick='popupOscarRx(600,900,"<rewrite:reWrite jspPage="../oscarMessenger/DisplayDemographicMessages.do"/>?orderby=date&boxType=3&demographic_no=<%=demoNo%>&providerNo=<%=provNo%>&userName=<%=providerName%>"); return false;'>
				-All Messages-</a></td>
			</tr>
			<!-- <tr><td>&nbsp;</td></tr> -->
			</form>
		</table>
		<table class="LeftTable">
			<form name="insertTemplateForm"><caisi:isModuleLoad
				moduleName="caisi">
				<input type="hidden" name="casetoEncounter" value="true">
			</caisi:isModuleLoad>
			<tr class="Header">
				<td style="font-weight: bold"><bean:message
					key="oscarEncounter.Index.encounterTemplate" /></td>
			</tr>
			<tr>
				<td><select name="templateSelect" class="ControlSelect"
					onChange="javascript:popUpInsertTemplate(40,50,document.insertTemplateForm.templateSelect.options[document.insertTemplateForm.templateSelect.selectedIndex].value)">
					<option value="null" selected>-<bean:message
						key="oscarEncounter.Index.insertTemplate" />- <%
                            String encounterTmp ="NONE";
                            String encounterTmpValue="NONE";
                            for(int j=0; j<bean.templateNames.size(); j++) {
                                encounterTmp = (String)bean.templateNames.get(j);
                         %>

					<option value="<%=encounterTmp%>"><%=encounterTmp %></option>
					<% }%>
				</select></td>
			</tr>
			<!-- <tr><td>&nbsp;</td></tr> -->
			</form>
		</table>
		<table class="LeftTable">
			<tr class="Header">
				<td style="font-weight: bold" colspan="2"><bean:message
					key="oscarEncounter.Index.measurements" /></td>
			</tr>
			<tr>
				<td>
				<%
                        dxResearchBeanHandler dxRes = new dxResearchBeanHandler(bean.demographicNo);
                        Vector dxCodes = dxRes.getActiveCodeListWithCodingSystem();
                        ArrayList flowsheets = MeasurementTemplateFlowSheetConfig.getInstance().getFlowsheetsFromDxCodes(dxCodes);
                        for (int f = 0; f < flowsheets.size();f++){
                            String flowsheetName = (String) flowsheets.get(f);
                        %> <a
					href="javascript: function myFunction() {return false; }"
					onClick="popup(700,1000,'oscarMeasurements/TemplateFlowSheet.jsp?demographic_no=<%=bean.demographicNo%>&template=<%=flowsheetName%>','flowsheet')"><%=MeasurementTemplateFlowSheetConfig.getInstance().getDisplayName(flowsheetName)%></a>
				<%}%>

				<form name="measurementGroupForm"><caisi:isModuleLoad
					moduleName="caisi">
					<input type="hidden" name="casetoEncounter" value="true">
				</caisi:isModuleLoad> <select name="measurementGroupSelect" class="ControlSelect"
					onChange="popUpMeasurements(500,1000,document.measurementGroupForm.measurementGroupSelect.options[document.measurementGroupForm.measurementGroupSelect.selectedIndex].value);return false;">
					<option value="null" selected>-<bean:message
						key="oscarEncounter.Index.SelectGroup" />- <%
                            for(int j=0; j<bean.measurementGroupNames.size(); j++) {
                            String tmp = (String)bean.measurementGroupNames.get(j);
                         %>

					<option value="<%=tmp%>"><%=tmp %> <%}%>

				</select>
				</td>
			</tr>
			<tr>
				<td><a href=#
					onClick="popupPage(600,1000,'<rewrite:reWrite jspPage="oscarMeasurements/SetupHistoryIndex.do"/>'); return false;"><bean:message
					key="oscarEncounter.Index.oldMeasurements" /></a></td>
			</tr>
			</form>
		</table>
		<table class="LeftTable">
			<tr class="Header">
				<td style="font-weight: bold"><bean:message
					key="oscarEncounter.Index.clinicalResources" /></td>
			</tr>
			<tr>
				<td><a href="#"
					ONCLICK="popupPage2('http://resource.oscarmcmaster.org/oscarResource/');return false;"
					title="<bean:message key="oscarEncounter.Index.resource"/>"
					onmouseover="window.status='<bean:message key="oscarEncounter.Index.viewResource"/>';return true"><bean:message
					key="oscarEncounter.Index.resource" /></a><br>
				<a href="#"
					onClick="popupPage(710,970,'../dms/documentReport.jsp?function=demographic&doctype=lab&functionid=<%=bean.demographicNo%>&curUser=<%=bean.curProviderNo%>');return false;"><bean:message
					key="oscarEncounter.Index.msgDocuments" /></a><br>
				<a href="#"
					onClick="popupPage(500,950, '../eform/efmpatientformlist.jsp?demographic_no=<%=bean.demographicNo%>');return false;"><bean:message
					key="global.eForms" /></a><br>
				<a href="#"
					onClick="popupPage(700,1000, '../tickler/ticklerDemoMain.jsp?demoview=<%=bean.demographicNo%>');return false;"><bean:message
					key="global.viewTickler" /></a><br>
				<a href="javascript: function myFunction() {return false; }"
					onClick="popupPage(150,200,'calculators.jsp?sex=<%=bean.patientSex%>&age=<%=pAge%>'); return false;"><bean:message
					key="oscarEncounter.Index.calculators" /></a><br>
				<!--a href="#" onClick="popupPage(700,1000, '../lab/CumulativeLabValues.jsp?demographic_no=<%=bean.demographicNo%>');return false;">Cumulative Labs</a><br-->

				<select name="selectCurrentForms"
					onChange="javascript:selectBox(this)" class="ControlSelect"
					onMouseOver="javascript:window.status='View <%=patientName%>\'s lab results'; return true;">
					<option value="null" selected>-lab results-</option>
					<%
                                for(int j=0; j<labs.size(); j++) {
                                    LabResultData result =  (LabResultData) labs.get(j);
                                    if ( result.isMDS() ){ %>
					<option
						value="../oscarMDS/SegmentDisplay.jsp?providerNo=<%=provNo%>&segmentID=<%=result.segmentID%>&status=<%=result.getReportStatus()%>"><%=result.getDateTime()%>
					<%=result.getDiscipline()%></option>
					<% }else if (result.isCML()){ %>
					<option
						value="../lab/CA/ON/CMLDisplay.jsp?providerNo=<%=provNo%>&segmentID=<%=result.segmentID%>">
					<%=result.getDateTime()%> <%=result.getDiscipline()%></option>
					<% }else {%>
					<option
						value="../lab/CA/BC/labDisplay.jsp?segmentID=<%=result.segmentID%>&providerNo=<%=provNo%>"><%=result.getDateTime()%>
					<%=result.getDiscipline()%></option>
					<% }%>

					<% } %>
				</select></td>
			</tr>
			<!-- <tr><td>&nbsp;</td></tr> -->
		</table>
		<table class="LeftTable">
			<form name="ksearch"><caisi:isModuleLoad moduleName="caisi">
				<input type="hidden" name="casetoEncounter" value="true">
			</caisi:isModuleLoad>
			<tr class="Header">
				<td style="font-weight: bold"><bean:message
					key="oscarEncounter.Index.internetResources" /></td>
			</tr>
			<tr>
				<td><bean:message key="oscarEncounter.Index.searchFor" /><%= request.getParameter("userName")%></td>
			</tr>
			<tr>
				<td><input type="text" name="keyword" class="ControlSelect"
					onkeypress="return grabEnter(event)" /></td>
			</tr>
			<tr>
				<td><bean:message key="oscarEncounter.Index.using" /></td>
			</tr>
			<tr>
				<td><select class="ControlSelect" name="channel">
					<option
						value="http://resource.oscarmcmaster.org/oscarResource/OSCAR_search/OSCAR_search_results?title="><bean:message
						key="oscarEncounter.Index.oscarSearch" /></option>
					<option value="http://www.google.com/search?q="><bean:message
						key="global.google" /></option>
					<option
						value="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?SUBMIT=y&CDM=Search&DB=PubMed&term="><bean:message
						key="global.pubmed" /></option>
					<option
						value="http://search.nlm.nih.gov/medlineplus/query?DISAMBIGUATION=true&FUNCTION=search&SERVER2=server2&SERVER1=server1&PARAMETER="><bean:message
						key="global.medlineplus" /></option>
					<option
						value="http://www.bnf.org/bnf/bnf/current/noframes/search.htm?n=50&searchButton=Search&q="><bean:message
						key="global.BNF" /></option>
				</select></td>
			</tr>
			<tr>
				<td><input type="button" name="button"
					class="ControlPushButton"
					value="<bean:message key="oscarEncounter.Index.btnGo"/>"
					onClick="popupSearchPage(600,800,forms['ksearch'].channel.options[forms['ksearch'].channel.selectedIndex].value+urlencode(forms['ksearch'].keyword.value) ); return false;">
				</td>
			</tr>
			</form>
		</table>
		</td>
		<td valign="top">
		<form name="encForm" action="SaveEncounter.do" method="POST"><caisi:isModuleLoad
			moduleName="caisi">
			<input type="hidden" name="casetoEncounter" value="true">
		</caisi:isModuleLoad>
		<table name="encounterTableRightCol">
			<!-- social history row -->
			<!-- start new rows here -->
			<tr>
				<td>
				<table bgcolor="#CCCCFF" id="rowOne" width="100%">
					<tr>
						<td>
						<div class="RowTop"><bean:message
							key="oscarEncounter.Index.socialFamHist" />:</div>
						<input type="hidden" name="shInput" /></td>
						<td>
						<div class="RowTop">
						<% if(oscarVariables.getProperty("otherMedications", "").length() > 1) {
                                        out.print(oscarVariables.getProperty("otherMedications", ""));
                                    %> <% } else { %> <bean:message
							key="oscarEncounter.Index.otherMed" />: <% } %>
						</div>
						</td>
						<td>
						<div class="RowTop">
						<% if(oscarVariables.getProperty("medicalHistory", "").length() > 1) {
                                        out.print(oscarVariables.getProperty("medicalHistory", ""));
                                    %> <% } else { %> <bean:message
							key="oscarEncounter.Index.medHist" />: <% } %>
						</div>
						</td>
						<td>
						<div
							style="font-size: 8pt; text-align: right; vertical-align: bottom">
						<a onMouseOver="javascript:window.status='Minimize'; return true;"
							href="javascript:rowOneX();"
							title="<bean:message key="oscarEncounter.Index.tooltipClose"/>">
						<bean:message key="oscarEncounter.Index.x" /></a> | <a
							onMouseOver="javascript:window.status='Small Size'; return true;"
							href="javascript:rowOneSmall();"
							title="<bean:message key="oscarEncounter.Index.tooltipSmall"/>">
						<bean:message key="oscarEncounter.Index.s" /></a> | <a
							onMouseOver="javascript:window.status='Medium Size'; return true;"
							href="javascript:rowOneNormal();"
							title="<bean:message key="oscarEncounter.Index.tooltipNormal"/>">
						<bean:message key="oscarEncounter.Index.n" /></a> | <a
							onMouseOver="javascript:window.status='Large Size'; return true;"
							href="javascript:rowOneLarge();"
							title="<bean:message key="oscarEncounter.Index.tooltipLarge"/>">
						<bean:message key="oscarEncounter.Index.l" /></a> | <a
							onMouseOver="javascript:window.status='Full Size'; return true;"
							href="javascript:rowOneFull();"
							title="<bean:message key="oscarEncounter.Index.tooltipFull"/>">
						<bean:message key="oscarEncounter.Index.f" /></a> | <a
							onMouseOver="javascript:window.status='Full Size'; return true;"
							href="javascript:reset();"
							title="<bean:message key="oscarEncounter.Index.tooltipReset"/>">
						<bean:message key="oscarEncounter.Index.r" /></a></div>
						</td>
					</tr>
					<tr width="100%">
						<!-- This is the Social History cell ...sh...-->
						<td valign="top"><!-- Creating the table tag within the script allows you to adjust all table sizes at once, by changing the value of leftCol -->
						<textarea name="shTextarea" wrap="hard" cols="31"
							style="height:<%=windowSizes.getProperty("rowOneSize")%>;overflow:auto"><%=bean.socialHistory%></textarea>
						</td>
						<!-- This is the Family History cell ...fh...-->
						<td valign="top"><textarea name="fhTextarea" wrap="hard"
							cols="31"
							style="height:<%=windowSizes.getProperty("rowOneSize")%>;overflow:auto"><%=bean.familyHistory%></textarea>
						</td>
						<!-- This is the Medical History cell ...mh...-->
						<td valign="top" colspan="2"><textarea name="mhTextarea"
							wrap="hard" cols="31"
							style="height:<%=windowSizes.getProperty("rowOneSize")%>;overflow:auto"><%=bean.medicalHistory%></textarea>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<!-- ongoing concerns row -->
			<tr>
				<td>
				<table bgcolor="#CCCCFF" id="rowTwo" width="100%">
					<tr>
						<td>
						<div class="RowTop">
						<% if(oscarVariables.getProperty("ongoingConcerns", "").length() > 1) {
                                        out.print(oscarVariables.getProperty("ongoingConcerns", ""));
                                    %> <% } else { %> <bean:message
							key="oscarEncounter.Index.msgConcerns" />: <% } %>
						</div>
						<input type="hidden" name="ocInput" /></td>

						<td>
						<div class="RowTop"><bean:message
							key="oscarEncounter.Index.msgReminders" />:</div>
						</td>
						<td>
						<div
							style="font-size: 8pt; text-align: right; vertical-align: bottom">
						<a onMouseOver="javascript:window.status='Minimize'; return true;"
							href="javascript:rowTwoX();"
							title="<bean:message key="oscarEncounter.Index.tooltipClose"/>">
						<bean:message key="oscarEncounter.Index.x" /></a> | <a
							onMouseOver="javascript:window.status='Small Size'; return true;"
							href="javascript:rowTwoSmall();"
							title="<bean:message key="oscarEncounter.Index.tooltipSmall"/>">
						<bean:message key="oscarEncounter.Index.s" /></a> | <a
							onMouseOver="javascript:window.status='Medium Size'; return true;"
							href="javascript:rowTwoNormal();"
							title="<bean:message key="oscarEncounter.Index.tooltipNormal"/>">
						<bean:message key="oscarEncounter.Index.n" /></a> | <a
							onMouseOver="javascript:window.status='Large Size'; return true;"
							href="javascript:rowTwoLarge();"
							title="<bean:message key="oscarEncounter.Index.tooltipLarge"/>">
						<bean:message key="oscarEncounter.Index.l" /></a> | <a
							onMouseOver="javascript:window.status='Full Size'; return true;"
							href="javascript:rowTwoFull();"
							title="<bean:message key="oscarEncounter.Index.tooltipFull"/>">
						<bean:message key="oscarEncounter.Index.f" /></a> | <a
							onMouseOver="javascript:window.status='Full Size'; return true;"
							href="javascript:reset();"
							title="<bean:message key="oscarEncounter.Index.tooltipReset"/>">
						<bean:message key="oscarEncounter.Index.r" /></a></div>
						</td>
					</tr>
					<tr width="100%">
						<td valign="top" style="border-right: 2px solid #ccccff"><textarea
							name='ocTextarea' wrap="hard" cols="48"
							style="height:<%=windowSizes.getProperty("rowTwoSize")%>;overflow:auto"><%=bean.ongoingConcerns%></textarea>
						</td>
						<td colspan="2" valign="top"><textarea name='reTextarea'
							wrap="hard" cols="48"
							style="height:<%=windowSizes.getProperty("rowTwoSize")%>;overflow:auto"><%=bean.reminders%></textarea>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<!-- prescription row -->
			<tr>
				<td>
				<table bgcolor="#ccccff" id="presTopTable" border="0">
					<tr>
						<!--hr style="border-bottom: 0pt solid #888888; background-color: #888888;"-->
						<td valign="top">
						<div class="RowTop"><a href=#
							onClick="popupOscarRx(700,960,'../oscarRx/showAllergy.do?demographicNo=<%=bean.demographicNo%>');return false;"><bean:message
							key="global.allergies" /></a>:&nbsp;&nbsp;&nbsp&nbsp;&nbsp;&nbsp&nbsp;&nbsp;</div>
						<div class="presBox" id="allergyBox">
						<ul>
							<%
								org.oscarehr.common.model.Allergy[] allergies = RxPatientData.getPatient(loggedInInfo, Integer.parseInt(demoNo)).getAllergies(loggedInInfo);

                                            for (int j=0; j<allergies.length; j++){%>
							<li><a
								title="<%= allergies[j].getDescription() %>">
							<%=allergies[j].getShortDesc(13,8,"...")%> </a></li>
							<%}%>
						</ul>
						</div>

						</td>
						<td width="100%" style="font-size: 10px;">
						<table width="100%" cellpadding=0 cellspacing=0 border="0">
							<tr>
								<td width=60>
								<div class="RowTop">Rx Date
								</td>
								<td>
								<div class="RowTop">
								<div class="RowTop"><a href=#
									onClick="popupOscarRx(700,1027,'../oscarRx/choosePatient.do?providerNo=<%=bean.providerNo%>&demographicNo=<%=bean.demographicNo%>');return false;"><bean:message
									key="global.prescriptions" /></a></div>
								</div>
								</td>
								<td align=right>
								<div
									style="font-size: 8pt; text-align: right; vertical-align: bottom">
								<a
									onMouseOver="javascript:window.status='Minimize'; return true;"
									href="javascript:presBoxX();"
									title="<bean:message key="oscarEncounter.Index.tooltipClose"/>">
								<bean:message key="oscarEncounter.Index.x" /></a> | <a
									onMouseOver="javascript:window.status='Small Size'; return true;"
									href="javascript:presBoxSmall();"
									title="<bean:message key="oscarEncounter.Index.tooltipSmall"/>">
								<bean:message key="oscarEncounter.Index.s" /></a> | <a
									onMouseOver="javascript:window.status='Medium Size'; return true;"
									href="javascript:presBoxNormal();"
									title="<bean:message key="oscarEncounter.Index.tooltipNormal"/>">
								<bean:message key="oscarEncounter.Index.n" /></a> | <a
									onMouseOver="javascript:window.status='Large Size'; return true;"
									href="javascript:presBoxLarge();"
									title="<bean:message key="oscarEncounter.Index.tooltipLarge"/>">
								<bean:message key="oscarEncounter.Index.l" /></a> | <a
									onMouseOver="javascript:window.status='Full Size'; return true;"
									href="javascript:presBoxFull();"
									title="<bean:message key="oscarEncounter.Index.tooltipFull"/>">
								<bean:message key="oscarEncounter.Index.f" /></a> | <a
									onMouseOver="javascript:window.status='Full Size'; return true;"
									href="javascript:reset();"
									title="<bean:message key="oscarEncounter.Index.tooltipReset"/>">
								<bean:message key="oscarEncounter.Index.r" /></a></div>
								</td>
							</tr>
						</table>
						<div class="presBox" id="presBox">
						<%
                                    oscar.oscarRx.data.RxPrescriptionData prescriptData = new oscar.oscarRx.data.RxPrescriptionData();
                                    oscar.oscarRx.data.RxPrescriptionData.Prescription [] arr = {};
                                    arr = prescriptData.getUniquePrescriptionsByPatient(Integer.parseInt(bean.demographicNo));
                                    if (arr.length > 0){%>
						<table>
							<%for (int i = 0; i < arr.length; i++){
                                                String rxD = arr[i].getRxDate().toString();
                                                //String rxP = arr[i].getRxDisplay();
                                                String rxP = arr[i].getFullOutLine().replaceAll(";"," ");
                                                rxP = rxP + "   " + arr[i].getEndDate();
                                                String styleColor = "";
                                                if(arr[i].isCurrent() == true){  styleColor="style='color:red;'";  }
                                            %>
							<tr>
								<td <%=styleColor%> valign=top
									style="border-bottom: 1pt solid #888888; font-size: 10px;"><%=rxD%></td>
								<td width=600 <%=styleColor%>
									style="border-bottom: 1pt solid #888888; font-size: 10px;"><%=rxP%></td>
							</tr>
							<%}%>
						</table>
						<%}else{out.write("&nbsp;");}%>
						</div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<!-- encounter row -->
			<tr>
				<td>
				<table bgcolor="#CCCCFF" width="100%" id="rowThree">
					<tr>
						<td nowrap>
						<table border="0" cellpadding="0" cellspacing="0" width='100%'>
							<tr>
								<td width='75%'>
								<div class="RowTop"><a href=#
									onClick="popupPage2('../report/reportecharthistory.jsp?demographic_no=<%=bean.demographicNo%>');return false;">
								<bean:message key="global.encounter" />: <%=bean.patientLastName %>,
								<%=bean.patientFirstName%> </a> <%if (sChart) {%> &nbsp; &nbsp; &nbsp;<!--http://localhost:8084/oscar/oscarEncounter/echarthistoryprint.jsp?echartid=7491&demographic_no=10090-->
								<a href="javascript: function myFunction() {return false; }"
									onClick="showpic('splitChartLayer','splitChartId');"
									id="splitChartId"> Split Chart </a> <%}%>
								</div>
								<input type="hidden" name="enInput" /></td>
								<td width='5%'>
								<%
										boolean bSplit = request.getParameter("splitchart")!=null?true:false;
										int nEctLen = bean.encounter.length();
									    boolean bTruncate = bSplit &&  nEctLen > 5120?true:false;
										int consumption = (int) ((bTruncate?5120:nEctLen) / (10.24*32));
                    consumption = consumption == 0 ? 1 : consumption;
									    String ccolor = consumption>=70? "red":(consumption>=50?"orange":"green");
								%>
								<div class="RowTop"><%=consumption+"%"%></div>
								</td>
								<td align='right'>
								<table border="0" cellpadding="0" cellspacing="0"
									bgcolor="white" width="100%"%>
									<tr>
										<td width='<%=consumption+"%"%>' bgcolor='<%=ccolor%>'>
										<div class="RowTop">&nbsp;</div>
										</td>
										<td>
										<% if (consumption<100) { %><div class="RowTop">&nbsp;</div>
										<% } %>
										</td>
									</tr>
								</table>
								</td>
							</tr>
						</table>
						</td>
						<td nowrap>
						<div
							style="font-size: 8pt; text-align: right; vertical-align: bottom">
						<a onMouseOver="javascript:window.status='Minimize'; return true;"
							href="javascript:rowThreeX();"
							title="<bean:message key="oscarEncounter.Index.tooltipClose"/>">
						<bean:message key="oscarEncounter.Index.x" /></a> | <a
							onMouseOver="javascript:window.status='Small Size'; return true;"
							href="javascript:rowThreeSmall();"
							title="<bean:message key="oscarEncounter.Index.tooltipSmall"/>">
						<bean:message key="oscarEncounter.Index.s" /></a> | <a
							onMouseOver="javascript:window.status='Normal Size'; return true;"
							href="javascript:rowThreeNormal();"
							title="<bean:message key="oscarEncounter.Index.tooltipNormal"/>">
						<bean:message key="oscarEncounter.Index.n" /></a> | <a
							onMouseOver="javascript:window.status='Medium Size'; return true;"
							href="javascript:rowThreeMedium();"
							title="<bean:message key="oscarEncounter.Index.tooltipMedium"/>">
						<bean:message key="oscarEncounter.Index.m" /></a> | <a
							onMouseOver="javascript:window.status='Large Size'; return true;"
							href="javascript:rowThreeLarge();"
							title="<bean:message key="oscarEncounter.Index.tooltipLarge"/>">
						<bean:message key="oscarEncounter.Index.l" /></a> | <a
							onMouseOver="javascript:window.status='Full Size'; return true;"
							href="javascript:rowThreeFull();"
							title="<bean:message key="oscarEncounter.Index.tooltipFull"/>">
						<bean:message key="oscarEncounter.Index.f" /></a> | <a
							onMouseOver="javascript:window.status='Full Size'; return true;"
							href="javascript:reset();"
							title="<bean:message key="oscarEncounter.Index.tooltipReset"/>">
						<bean:message key="oscarEncounter.Index.r" /></a></div>
						</td>
					</tr>
					<%
                            String encounterText = "";
                            try{
                               if(!bSplit){
                                  encounterText = bean.encounter;
                               }else if(bTruncate){
                                  encounterText = bean.encounter.substring(nEctLen-5120)+"\n--------------------------------------------------\n$$SPLIT CHART$$\n";
                               }else{
                                  encounterText = bean.encounter+"\n--------------------------------------------------\n$$SPLIT CHART$$\n";
                               }

                               if(bean.eChartTimeStamp==null){
                                  encounterText +="\n["+dateConvert.DateToString(bean.currentDate)+" .: "+bean.reason+"] \n";
                               }else if(bean.currentDate.compareTo(bean.eChartTimeStamp)>0){
                                   encounterText +="\n__________________________________________________\n["+("".equals(bean.appointmentDate)?UtilDateUtilities.getToday("yyyy-MM-dd"):bean.appointmentDate)+" .: "+bean.reason+"]\n";
                               }else if((bean.currentDate.compareTo(bean.eChartTimeStamp) == 0) && (bean.reason != null || bean.subject != null ) && !bean.reason.equals(bean.subject) ){
                                   encounterText +="\n__________________________________________________\n["+bean.appointmentDate+" .: "+bean.reason+"]\n";
                               }
                               if(!bean.oscarMsg.equals("")){
                                  encounterText +="\n\n"+bean.oscarMsg;
                               }

                            }catch(Exception eee){MiscUtils.getLogger().error("Error", eee);}
                            %>
					<tr>
						<td colspan="2" valign="top" style="text-align: left"><textarea
							name='enTextarea' wrap="hard" cols="99"
							style="height:<%=windowSizes.getProperty("rowThreeSize")%>;overflow:auto"
							onfocus="javascript: encTextareaFocus=true;"
							onblur="javascript: encTextareaFocus=false;"><%=encounterText%></textarea>
						</td>
					</tr>
				</table>
				<table border=0 bgcolor="#CCCCFF" width=100%>
					<tr id="saveFeedbackRow" nowrap>
						<td><input type="hidden" name="subject"
							value="<%=UtilMisc.htmlEscape(bean.reason)%>"> <% if (consumption>=50) {%>
						<input type="submit" style="height: 20px" name="buttonPressed"
							value="Split Chart" class="ControlPushButton"
							onClick="return (onSplit());"> <% } %>
						</td>
						<td style="text-align: right" nowrap><span
							id="saveFeedbackText" style="display: none"> <bean:message
							key="oscarEncounter.Index.saveFeedbackText" /> </span> <oscar:oscarPropertiesCheck
							property="CPP" value="yes">
							<input type="button" style="height: 20px;"
								class="ControlPushButton" value="CPP"
								onClick="document.forms['encForm'].btnPressed.value='Save'; document.forms['encForm'].submit();javascript:popupPageK('encounterCPP.jsp');" />
						</oscar:oscarPropertiesCheck> <oscar:oscarPropertiesCheck property="encPrintOnly" value="yes">
							<input type="button" style="height: 20px;"
								class="ControlPushButton" value="Print Only"
								onClick="javascript:popupPageK('encounterPrint.jsp');" />
						</oscar:oscarPropertiesCheck> <input type="button" style="height: 20px;"
							class="ControlPushButton"
							value="<bean:message key="global.btnPrint"/>"
							onClick="document.forms['encForm'].btnPressed.value='Save'; document.forms['encForm'].submit();javascript:popupPageK('encounterPrint.jsp');" />
						<input type="hidden" name="btnPressed" value=""> <input
							type="hidden" name="submitMethod" value="synchronous" /> <!-- security code block -->
						<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart"
							rights="w">
							<% if(!bPrincipalControl || (bPrincipalControl && bPrincipalDisplay) ) { %>
							<input type="button" style="height: 20px"
								value="<bean:message key="oscarEncounter.Index.btnSave"/>"
								class="ControlPushButton"
								onclick="document.forms['encForm'].btnPressed.value='Save'; document.forms['encForm'].submit();">
							<input type="button" style="height: 20px"
								value="<bean:message key="oscarEncounter.Index.btnSignSave"/>"
								class="ControlPushButton"
								onclick="document.forms['encForm'].btnPressed.value='Sign,Save and Exit'; document.forms['encForm'].submit();">
							<oscar:oscarPropertiesCheck property="billregion" value="BC">
								<input type="button" style="height: 20px"
									value="<bean:message key="oscarEncounter.Index.btnSignSaveBill"/>"
									class="ControlPushButton"
									onclick="document.forms['encForm'].btnPressed.value='Sign,Save and Bill';document.forms['encForm'].status.value = 'BS'; document.forms['encForm'].submit(); ">
							</oscar:oscarPropertiesCheck>
							<security:oscarSec roleName="<%=roleName$%>"
								objectName="_eChart.verifyButton" rights="w">
								<input type="button" style="height: 20px"
									value="<bean:message key="oscarEncounter.Index.btnSign"/>"
									class="ControlPushButton"
									onclick="document.forms['encForm'].btnPressed.value='Verify and Sign'; document.forms['encForm'].submit();">
							</security:oscarSec>
							<% } %>
						</security:oscarSec> <!-- security code block --> <input type="button"
							style="height: 20px" name="buttonPressed"
							value="<bean:message key="global.btnExit"/>"
							class="ControlPushButton"
							onclick="document.forms['encForm'].btnPressed.value='Exit'; if (closeEncounterWindow()) {document.forms['encForm'].submit();}">
						<input type="hidden" name="rowOneSize"
							value="<%=windowSizes.getProperty("rowOneSize")%>"> <input
							type="hidden" name="rowTwoSize"
							value="<%=windowSizes.getProperty("rowTwoSize")%>"> <input
							type="hidden" name="presBoxSize"
							value="<%=windowSizes.getProperty("presBoxSize")%>"> <input
							type="hidden" name="rowThreeSize"
							value="<%=windowSizes.getProperty("rowThreeSize")%>"> <input
							type="hidden" name="status" value="t" /> <input type="hidden"
							name="appointment_no" value="<%=bean.appointmentNo%>" /></td>
					</tr>
				</table>
				<%
                            if(bSplit){%> <script>
                                document.forms['encForm'].btnPressed.value='Save';
                                document.forms['encForm'].submit();
                            </script> <%}
                        %>
				</td>
			</tr>
			<!-- end new rows here -->
		</table>
		</td>
	</tr>
</table>
</form>

<%if (sChart){ %>
<div id="splitChartLayer"
	style="position: absolute; left: 1px; top: 1px; width: 150px; height: 311px; visibility: hidden; z-index: 1">

<table width="98%" border="1" cellspacing="1" cellpadding="1"
	align=center bgcolor="#CCCCFF">
	<tr class="LightBG">
		<td class="wcblayerTitle">Date</td>
		<td class="wcblayerTitle" align="right"><a
			href="javascript: function myFunction() {return false; }"
			onClick="hidepic('splitChartLayer');" style="text-decoration: none;">X</a>
		</td>
	</tr>
	<% for (int i = 0 ; i < splitChart.size(); i++){
            String[] s = (String[]) splitChart.get(i);%>
	<tr class="background-color : #ccccff;">
		<td class="wcblayerTitle"><a href=#
			onClick="hidepic('splitChartLayer');popupPage2('echarthistoryprint.jsp?echartid=<%=s[0]%>&demographic_no=<%=bean.demographicNo%>');return false;">
		<%=s[1]%> </a></td>
		<td class="wcblayerItem">&nbsp;</td>
	</tr>
	<%}%>
</table>
</div>
<%}%>

</body>
</html:html>
