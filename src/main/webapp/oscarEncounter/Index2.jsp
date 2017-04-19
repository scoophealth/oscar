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
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />
<%
    long startTime = System.currentTimeMillis();
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    String demographic$ = request.getParameter("demographicNo") ;
    boolean bPrincipalControl = false;
    boolean bPrincipalDisplay = false;

    LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
    
    String eChart$ = "_eChart$"+demographic$;

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


<security:oscarSec roleName="<%=roleName$%>" objectName="<%=eChart$%>"
	rights="o" reverse="<%=false%>">
You have no rights to access the data!
<% response.sendRedirect("../acctLocked.html");  %>
</security:oscarSec>

<%-- only principal has the save rights --%>

<security:oscarSec roleName="_principal" objectName="_eChart"
	rights="ow" reverse="<%=false%>">
	<% 	bPrincipalControl = true;
	if(EctPatientData.getProviderNo(loggedInInfo, demographic$).equals((String) session.getAttribute("user")) ) {
		bPrincipalDisplay = true;
	}
%>
</security:oscarSec>

<%-- if this patients eChart is read only remove the save rights --%>
<security:oscarSec roleName="_all" objectName="<%=eChart$%>" rights="or"
	reverse="<%=false%>">
	<%
    bPrincipalControl = true;
    bPrincipalDisplay = false;
%>
</security:oscarSec>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

<%@page
	import="oscar.log.*,oscar.util.UtilMisc,oscar.oscarEncounter.data.*, java.net.*,java.util.*,oscar.util.UtilDateUtilities"%>
<%@page
	import="oscar.oscarMDS.data.MDSResultsData,oscar.oscarLab.ca.on.*, oscar.oscarMessenger.util.MsgDemoMap, oscar.oscarMessenger.data.MsgMessageData"%>
<%@page
	import="oscar.oscarEncounter.oscarMeasurements.*,oscar.oscarResearch.oscarDxResearch.bean.*,oscar.util.*"%>
<%@page
	import="oscar.eform.*, oscar.dms.*, org.apache.commons.lang.StringEscapeUtils"%>

<% java.util.Properties oscarVariables = oscar.OscarProperties.getInstance(); %>

<%
	String ip = request.getRemoteAddr();
	LogAction.addLog((String) session.getAttribute("user"), LogConst.READ, LogConst.CON_ECHART, demographic$, ip,demographic$);
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

<!-- check to see if new case management is request -->
<%
    ArrayList<String> users = (ArrayList<String>)session.getServletContext().getAttribute("CaseMgmtUsers");
    String userNo = (String) request.getSession().getAttribute("user"); 
    Boolean useNewEchart = (Boolean) request.getSession().getServletContext().getAttribute("useNewEchart");
    if( userNo != null && (useNewEchart != null && useNewEchart.equals(Boolean.TRUE)) ) {
        session.setAttribute("newCaseManagement", "true");
%>
<caisi:isModuleLoad moduleName="caisi" reverse="true">
	<%
            EctProgram prgrmMgr = new EctProgram(session);
            session.setAttribute("case_program_id", prgrmMgr.getProgram(bean.providerNo));
            session.setAttribute("casemgmt_oscar_baseurl",request.getContextPath());
            String strBeanName = "casemgmt_oscar_bean" + bean.getDemographicNo();
            session.setAttribute(strBeanName, bean);
            session.setAttribute("casemgmt_bean_flag", "true");
            String hrefurl=request.getContextPath()+"/casemgmt/forward.jsp?action=view&demographicNo="+bean.demographicNo+"&providerNo="+bean.providerNo+"&providerName="+URLEncoder.encode(bean.userName)+"&appointmentNo="+request.getParameter("appointmentNo")+"&reason=" + URLEncoder.encode(request.getParameter("reason")==null?"":request.getParameter("reason")) + "&appointmentDate="+request.getParameter("appointmentDate")+"&start_time="+request.getParameter("startTime")+ "&apptProvider=" + request.getParameter("apptProvider_no")+"&providerview="+ request.getParameter("providerview") + "&msgType="+request.getParameter("msgType")+"&OscarMsgTypeLink="+request.getParameter("OscarMsgTypeLink")+"&noteId="+request.getParameter("noteId")+(request.getParameter("noteId") != null ? "&forceNote=true" :"");

            if( request.getParameter("noteBody") != null )
                hrefurl += "&noteBody=" + request.getParameter("noteBody");

            if( !response.isCommitted()) {
                response.sendRedirect(hrefurl);
                return;
            }
        %>

</caisi:isModuleLoad>

<%  }
    else
        session.setAttribute("newCaseManagement", "false");
%>
<!-- add by caisi  -->

<caisi:isModuleLoad moduleName="caisi">
	<%
session.setAttribute("casemgmt_oscar_baseurl",request.getContextPath());
String strBeanName = "casemgmt_oscar_bean" + bean.getDemographicNo();
session.setAttribute(strBeanName, bean);
session.setAttribute("casemgmt_oscar_bean", bean);
session.setAttribute("casemgmt_bean_flag", "true");
String hrefurl=request.getContextPath()+"/casemgmt/forward.jsp?action=view&demographicNo="+bean.demographicNo+"&providerNo="+bean.providerNo+"&providerName="+URLEncoder.encode(bean.userName);
if (request.getParameter("casetoEncounter")==null)
{
        if( !response.isCommitted())
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
  String famDocName, famDocSurname;
  if(bean.familyDoctorNo.equals("")) {
    famDocName = "";
    famDocSurname = "";
  }
  else {
    EctProviderData.Provider prov = new EctProviderData().getProvider(bean.familyDoctorNo);
    famDocName = prov.getFirstName();
    famDocSurname = prov.getSurname();

  }

  String patientName = pd.getFirstName()+" "+pd.getSurname();
  String patientAge = pd.getAge();
  String patientSex = pd.getSex();
  String providerName = bean.userName;

  String pAge = Integer.toString(dateConvert.calcAge(bean.yearOfBirth,bean.monthOfBirth,bean.dateOfBirth));
  java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);

  String province = (oscarVariables.getProperty("billregion","")).trim().toUpperCase();
  Properties windowSizes = oscar.oscarEncounter.pageUtil.EctWindowSizes.getWindowSizes(provNo);

  MsgDemoMap msgDemoMap = new MsgDemoMap();

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

<script src="../share/javascript/prototype.js" type="text/javascript"></script>
<script src="../share/javascript/scriptaculous.js"
	type="text/javascript"></script>

<%-- for popup menu of forms --%>
<script src="../share/javascript/popupmenu.js" type="text/javascript"></script>
<script src="../share/javascript/menutility.js" type="text/javascript"></script>

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
    var calculatorColour = "9900CC";
    var measurementWindows = new Array();
    var openWindows = new Object();
    var autoCompleted = new Object();
    var autoCompList = new Array();
    var itemColours = new Object();

    //Add calculators to autocompleter menu
    autoCompleted["<bean:message key="oscarEncounter.Index.bodyMass"/>"] = "popupPage(650,775,'BodyMassIndex','http://www.intmed.mcw.edu/clincalc/body.html')";
    autoCompList.push("<bean:message key="oscarEncounter.Index.bodyMass"/>");
    itemColours["<bean:message key="oscarEncounter.Index.bodyMass"/>"] = calculatorColour;

    autoCompleted["<bean:message key="oscarEncounter.Index.coronary"/>"] = "popupPage(525,775,'CoronaryArteryDiseaseRisk','calculators/CoronaryArteryDiseaseRiskPrediction.jsp?sex=<%=bean.patientSex%>&age=<%=pAge%>')";
    autoCompList.push("<bean:message key="oscarEncounter.Index.coronary"/>");
    itemColours["<bean:message key="oscarEncounter.Index.coronary"/>"] = calculatorColour;

    autoCompleted["<bean:message key="oscarEncounter.Index.msgOsteoporotic"/>"] = "popupPage(525,775,'OsteoporoticFracture','calculators/OsteoporoticFracture.jsp?sex=<%=bean.patientSex%>&age=<%=pAge%>')";
    autoCompList.push("<bean:message key="oscarEncounter.Index.msgOsteoporotic"/>");
    itemColours["<bean:message key="oscarEncounter.Index.msgOsteoporotic"/>"] = calculatorColour;

    autoCompleted["<bean:message key="oscarEncounter.Index.pregnancy"/>"] = "popupPage(650,775,'PregancyCalculator','http://www.intmed.mcw.edu/clincalc/pregnancy.html')";
    autoCompList.push("<bean:message key="oscarEncounter.Index.pregnancy"/>");
    itemColours["<bean:message key="oscarEncounter.Index.pregnancy"/>"] = calculatorColour;

    autoCompleted["<bean:message key="oscarEncounter.Index.simpleCalculator"/>"] = "popupPage(400,500,'SimpleCalc','calculators/SimpleCalculator.jsp')";
    autoCompList.push("<bean:message key="oscarEncounter.Index.simpleCalculator"/>");
    itemColours["<bean:message key="oscarEncounter.Index.simpleCalculator"/>"] = calculatorColour;

    autoCompleted["<bean:message key="oscarEncounter.Index.generalConversions"/>"] = "popupPage(650,775,'GeneralConversions','calculators/GeneralCalculators.jsp')";
    autoCompList.push("<bean:message key="oscarEncounter.Index.generalConversions"/>");
    itemColours["<bean:message key="oscarEncounter.Index.generalConversions"/>"] = calculatorColour;

   <%
   int MaxLen = 25;
   int TruncLen = 22;
   String ellipses = "...";
  for(int j=0; j<bean.templateNames.size(); j++) {
     String encounterTmp = bean.templateNames.get(j);
     encounterTmp = StringUtils.maxLenString(encounterTmp, MaxLen, TruncLen, ellipses);
     encounterTmp = StringEscapeUtils.escapeJavaScript(encounterTmp);
   %>
     autoCompleted["<%=encounterTmp%>"] = "ajaxInsertTemplate('<%=encounterTmp%>')";
     autoCompList.push("<%=encounterTmp%>");
     itemColours["<%=encounterTmp%>"] = "99CCCC";
   <%
  }
   %>
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

    function setCaretPosition(inpu, pos){
	if(inpu.setSelectionRange){
		inpu.focus();
		inpu.setSelectionRange(pos,pos);
	}else if (inpu.createTextRange) {
		var range = inpu.createTextRange();
		range.collapse(true);
		range.moveEnd('character', pos);
		range.moveStart('character', pos);
		range.select();
	}
    }

    function writeToEncounterNote(request) {
        //$("templatejs").update(request.responseText);
        var text = request.responseText;
        text = text.replace(/\\u000A/g, "\u000A");
        text = text.replace(/\\u000D/g, "");
        text = text.replace(/\\u003E/g, ">");
        text = text.replace(/\\u003C/g, "<");
        text = text.replace(/\\u005C/g, "\\");
        text = text.replace(/\\u0022/g, "\"");
        text = text.replace(/\\u0027/g, "'");

        document.encForm.enTextarea.value += "\n\n";
        var curPos = document.encForm.enTextarea.value.length;

        //subtract \r chars from total length for IE
        if( document.all ) {
            var newLines = document.encForm.enTextarea.value.match(/.*\n.*/g);
            curPos -= newLines.length;
        }
        ++curPos;

        //if insert text begins with a new line char jump to second new line
        var newlinePos;
        if( (newlinePos = text.indexOf('\n')) == 0 ) {
            ++newlinePos;
            var subtxt = text.substr(newlinePos);
            curPos += subtxt.indexOf('\n');
        }

        document.encForm.enTextarea.value = document.encForm.enTextarea.value + text;

        setTimeout("document.encForm.enTextarea.scrollTop=document.encForm.enTextarea.scrollHeight", 0);  // setTimeout is needed to allow browser to realize that text field has been updated
        document.encForm.enTextarea.focus();
        setCaretPosition(document.encForm.enTextarea,curPos);
    }

    function ajaxInsertTemplate(varpage) { //fetch template
        if(varpage!= 'null'){
          var page = "<rewrite:reWrite jspPage="InsertTemplate.do"/>";
          var params = "templateName=" + varpage + "&version=2";
          new Ajax.Request( page, {
                                    method: 'post',
                                    postBody: params,
                                    evalScripts: true,
                                    onSuccess:writeToEncounterNote,
                                    onFailure: function() {
                                            alert("Inserting template " + varpage + " failed");
                                        }
                                  }
                            );
        }

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
            document.encForm.enTextarea.value = document.encForm.enTextarea.value.replace(/\\u003E/g, ">");
            document.encForm.enTextarea.value = document.encForm.enTextarea.value.replace(/\\u003C/g, "<");
            document.encForm.enTextarea.value = document.encForm.enTextarea.value.replace(/\\u005C/g, "\\");
            document.encForm.enTextarea.value = document.encForm.enTextarea.value.replace(/\\u0022/g, "\"");
            document.encForm.enTextarea.value = document.encForm.enTextarea.value.replace(/\\u0027/g, "'");
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
            popupPage(700,720,'unbilled', url);
        }
    }

    var curWin = 0;

    function popupPage(vheight,vwidth,name,varpage) { //open a new popup window
      var page = "" + varpage;
      name = name.replace(/\s+/g,"_");
      windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=600,screenY=200,top=0,left=0";
            //var popup =window.open(page, "<bean:message key="oscarEncounter.Index.popupPageWindow"/>", windowprops);
            openWindows[name] = window.open(page, name, windowprops);

            if (openWindows[name] != null) {
                if (openWindows[name].opener == null) {
                    openWindows[name].opener = self;
                    alert("<bean:message key="oscarEncounter.Index.popupPageAlert"/>");
                }
                openWindows[name].focus();
            }
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

if (!document.all) document.captureEvents(Event.MOUSEUP);
document.onmouseup = getActiveText;


function getActiveText(e) {

  //text = (document.all) ? document.selection.createRange().text : document.getSelection();
  //document.ksearch.keyword.value = text;
 if(document.all) {
    //alert("one");
    text = document.selection.createRange().text;
    if(text != "" && document.ksearch.keyword.value=="") {
      document.ksearch.keyword.value += text;
    }
    if(text != "" && document.ksearch.keyword.value!="") {
      document.ksearch.keyword.value = text;
    }
  } else {
    text = window.getSelection();

    if (text.toString().length == 0){  //for firefox
       var txtarea = document.encForm.enTextarea;
       var selLength = txtarea.textLength;
       var selStart = txtarea.selectionStart;
       var selEnd = txtarea.selectionEnd;
       if (selEnd==1 || selEnd==2) selEnd=selLength;
       text = (txtarea.value).substring(selStart, selEnd);
    }
    //
    document.ksearch.keyword.value = text;
  }
  return true;
}
<%--
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

function selectBox(name) {
    to = name.options[name.selectedIndex].value;
    name.selectedIndex =0;
    if(to!="null")
      popupPageK(to);
}

--%>

function measurementLoaded(name) {
    measurementWindows.push(openWindows[name]);
}

function onClosing() {
    for( var idx = 0; idx < measurementWindows.length; ++idx ) {
        if( !measurementWindows[idx].closed )
            measurementWindows[idx].parentChanged = true;
    }
}

function showMenu(menuNumber, eventObj) {
    //    alert(eventObj);
    /*hideAllMenus();
    var menuId = 'menu' + menuNumber;
    if(changeObjectVisibility(menuId, 'visible')) {
	var menuTitle = getStyleObject('menuTitle' + menuNumber);
	menuTitle.backgroundColor = '#ff9900';
	eventObj.cancelBubble = true;
	return true;
    } else {
	return false;
    }
    */
    var menuId = 'menu' + menuNumber;
    return showPopup(menuId, eventObj);
}

var numMenus = 3;

function hideAllMenus() {
    for(counter = 1; counter <= numMenus; counter++) {
	changeObjectVisibility('menu' + counter, 'hidden');
	var menuTitle = getStyleObject('menuTitle' + counter);
	menuTitle.backgroundColor = '#000000';
    }
}

document.onclick = hideAllMenus;

    //This object stores the key -> cmd value passed to action class and the id of the created div
    // and the value -> URL of the action class
    var URLs = {
                  preventions:  "<rewrite:reWrite jspPage="displayPrevention.do?hC=009999"/>",
                  tickler:      "<rewrite:reWrite jspPage="displayTickler.do?hC=FF6600"/>",
                  Dx:           "<rewrite:reWrite jspPage="displayDisease.do?hC=5A5A5A"/>",
                  forms:        "<rewrite:reWrite jspPage="displayForms.do?hC=917611"/>",
                  eforms:       "<rewrite:reWrite jspPage="displayEForms.do?hC=11CC00"/>",<%/*  88E900 */%>
                  docs:         "<rewrite:reWrite jspPage="displayDocuments.do?hC=476BB3"/>",
                  labs:         "<rewrite:reWrite jspPage="displayLabs.do?hC=A0509C"/>", <%/* 550066   */%>
                  msgs:         "<rewrite:reWrite jspPage="displayMessages.do?hC=DDDD00"/>", <% /* FF33CC */ %>
                  measurements: "<rewrite:reWrite jspPage="displayMeasurements.do?hC=344887"/>",
                  consultation: "<rewrite:reWrite jspPage="displayConsultation.do"/>"
              };

    var params = new Array("cmd=forms", "cmd=eforms", "cmd=docs", "cmd=labs", "cmd=msgs", "cmd=measurements", "cmd=tickler",
                            "cmd=Dx", "cmd=preventions", "cmd=consultation", "cmd=msgs+eforms+forms+docs+labs+measurements+tickler");

function loader(){
    window.focus();
    var tmp;

    document.encForm.enTextarea.focus();
    document.encForm.enTextarea.value = document.encForm.enTextarea.value + "";
    document.encForm.enTextarea.scrollTop = document.encForm.enTextarea.scrollHeight;

    <%String popUrl = request.getParameter("popupUrl");
      if (popUrl != null){           %>
      window.setTimeout("popupPage(700,900,'<%=popUrl%>')", 2);
    <%}%>

    //new navbar loader
    var navBars = new navBarLoader();
    navBars.load();
}

var updateNeeded = false;

function updateDiv() {

    if( updateNeeded ) {
        var div = $F("reloadDiv");
        popLeftColumn(URLs[div], div, div);
        updateNeeded = false;
    }

    setTimeout("updateDiv();", 1000);
}

function clickLoadDiv(e) {
    var data = $A(arguments);
    Event.stop(e);
    data.shift();
    loadDiv(data[0],data[1],0);
}

function loadDiv(div,url,limit) {

    var objAjax = new Ajax.Request (
                            url,
                            {
                                method: 'post',
                                evalScripts: true,
                                /*onLoading: function() {
                                                $(div).update("<p>Loading ...<\/p>");
                                            },*/
                                onSuccess: function(request) {
                                                /*while( $(div).firstChild )
                                                    $(div).removeChild($(div).firstChild);
                                                */

                                                $(div).update(request.responseText);
                                                //listDisplay(div,100);

                                           },
                                onFailure: function(request) {
                                                $(div).innerHTML = "<h3>" + div + "<\/h3>Error: " + request.status;
                                            }
                            }

                      );
    return false;

}

<%--function popLeftColumn(url,div,params) {

    params = "cmd=" + params;
    var objAjax = new Ajax.Updater(div,url, {method:'post',
                                          postBody:params,
                                          asynchronous:true,
                                          evalScripts:true,
                                          onFailure: function(request) {

                                                    $(div).innerHTML = "<h3>Error:</h3>" + request.status;
                                          }
                                  });

}--%>

function popLeftColumn(url,div,params) {
    params = "cmd=" + params;
    var objAjax = new Ajax.Request (
                        url,
                        {
                            method: 'post',
                            postBody: params,
                            evalScripts: true,
                            /*onLoading: function() {
                                            $(div).update("<p>Loading ...</p>");
                                        }, */
                            onSuccess: function(request) {
                                            while( $(div).firstChild )
                                                $(div).removeChild($(div).firstChild);


                                            $(div).update(request.responseText);

                                            listDisplay(params);
                                       },
                            onFailure: function(request) {
                                            $(div).innerHTML = "<h3>Error:</h3>" + request.status;
                                        }
                        }

                  );
}

   /*
    *Set expand and collapse images for navbar divs and show/hide lines above threshold
    *Store function event listeners so we start/stop listening
    */
   var imgfunc = new Object();
   var obj = {};
   function listDisplay(Id, threshold) {
            if( threshold == 0 )
                return;

            var listId = Id + "list";
            var list = $(listId);
            var items = list.getElementsByTagName('li');
            items = $A(items);

            var topName = "img"+Id+"0";
            var midName = "img"+Id+(threshold-1);
            var lastName = "img"+Id+(items.length-1);
            var topImage = $(topName);
            var midImage = $(midName);
            var lastImage = $(lastName);
            var expand;
            var expandPath = "<c:out value="${ctx}"/>/oscarEncounter/graphics/expand.gif";
            var collapsePath = "<c:out value="${ctx}"/>/oscarMessenger/img/collapse.gif";
            var transparentPath = "<c:out value="${ctx}"/>/images/clear.gif";

            for( var idx = threshold; idx < items.length; ++idx ) {
                if( items[idx].style.display == 'block' ) {
                    items[idx].style.display = 'none';
                    expand = true;
                }
                else {
                    items[idx].style.display = 'block';
                    expand = false;
                }
            }

            if( expand ) {
                topImage.src = transparentPath;
                lastImage.src = transparentPath;
                midImage.src = expandPath;
                midImage.title = (items.length - threshold) + " items more";

                Element.stopObserving(topImage, "click", imgfunc[topName]);
                Element.stopObserving(lastImage, "click", imgfunc[lastName]);

                imgfunc[midName] = clickListDisplay.bindAsEventListener(obj,Id,threshold);
                Element.observe(midImage, "click", imgfunc[midName]);

            }
            else {
                topImage.src = collapsePath;
                lastImage.src = collapsePath;
                midImage.src = transparentPath;
                midImage.title = "";

               Element.stopObserving(midImage, "click", imgfunc[midName]);

                imgfunc[topName] = clickListDisplay.bindAsEventListener(obj,Id,threshold);
                Element.observe(topImage, "click", imgfunc[topName]);

                imgfunc[lastName] = clickListDisplay.bindAsEventListener(obj,Id,threshold);
                Element.observe(lastImage, "click", imgfunc[lastName]);

            }

    }

 function clickListDisplay(e) {
	var data = $A(arguments);
	data.shift();
	listDisplay(data[0],data[1]);
 }

function navBarLoader() {
    //$("leftNavbar").style.height = $("content").getHeight();

    /*
     *is right navbar present?
     *if so work with it
     *if not, set max lines to 0
     */
    if( $("rightNavBar") != undefined ) {
        $("rightNavBar").style.height = $("notCPP").getHeight();
        this.maxRightNumLines = Math.floor($("rightNavBar").getHeight() / 14);
    }
    else
        this.rightNumLines = 0;

    $("leftNavbar").style.height = "660px";
    this.maxLeftNumLines = Math.floor($("leftNavbar").getHeight() / 14);
    this.arrLeftDivs = new Array();
    this.arrRightDivs = new Array();
    this.rightTotal = 0;
    this.leftTotal = 0;
    this.leftDivs = 10;
    this.rightDivs = 3;
    this.leftReported = 0;
    this.rightReported = 0;

    //init ajax calls for all sections of the navbars and create a div for each ajax request
    this.load = function() {

            var leftNavbar = {
                  preventions:  "<c:out value="${ctx}"/>/oscarEncounter/displayPrevention.do?hC=009999",
                  tickler:      "<c:out value="${ctx}"/>/oscarEncounter/displayTickler.do?hC=FF6600",
                  Dx:           "<c:out value="${ctx}"/>/oscarEncounter/displayDisease.do?hC=5A5A5A",
                  forms:        "<c:out value="${ctx}"/>/oscarEncounter/displayForms.do?hC=917611",
                  eforms:       "<c:out value="${ctx}"/>/oscarEncounter/displayEForms.do?hC=11CC00",<%/*  88E900 */%>
                  docs:         "<c:out value="${ctx}"/>/oscarEncounter/displayDocuments.do?hC=476BB3",
                  labs:         "<c:out value="${ctx}"/>/oscarEncounter/displayLabs.do?hC=A0509C", <%/* 550066   */%>
                  msgs:         "<c:out value="${ctx}"/>/oscarEncounter/displayMessages.do?hC=DDDD00", <% /* FF33CC */ %>
                  measurements: "<c:out value="${ctx}"/>/oscarEncounter/displayMeasurements.do?hC=344887",
                  consultation: "<c:out value="${ctx}"/>/oscarEncounter/displayConsultation.do?hC="
              };

          var URLs = new Array();
          URLs.push(leftNavbar);


        for( var j = 0; j < URLs.length; ++j ) {

            var navbar;
            if( j == 0 )
                navbar = "leftNavbar";
            else if( j == 1)
                navbar = "rightNavBar";

            for( idx in URLs[j] ) {
                var div = document.createElement("div");
                div.className = "leftBox";
                div.style.display = "block";
                div.id = idx;
                $(navbar).appendChild(div);

                if( navbar == "leftNavbar" )
                    this.arrLeftDivs.push(div);
                if( navbar == "rightNavBar" )
                    this.arrRightDivs.push(div);

                this.popColumn(URLs[j][idx],idx,idx, navbar, this);
            }

        }


    };

    //update each ajax div with info from request
    this.popColumn = function (url,div,params, navBar, navBarObj) {
        params = "reloadURL=" + url + "&numToDisplay=6&cmd=" + params;

        var objAjax = new Ajax.Request (
                            url,
                            {
                                method: 'post',
                                postBody: params,
                                evalScripts: true,
                                /*onLoading: function() {
                                                $(div).update("<p>Loading ...<\/p>");
                                            }, */
                                onSuccess: function(request) {
                                                while( $(div).firstChild )
                                                    $(div).removeChild($(div).firstChild);

                                                $(div).update(request.responseText);

                                                //track ajax completions and display divs when last ajax call completes
                                                //navBarObj.display(navBar,div);
                                           },
                                onFailure: function(request) {
                                                $(div).innerHTML = "<h3>Error:<\/h3>" + request.status;
                                            }
                            }

                      );
        };

        //format display and show divs in navbars
        this.display = function(navBar,div) {

            //add number of items plus header to total
            var reported = 0;
            var numDivs = 0;
            var arrDivs;
            if( navBar == "leftNavbar" ) {
                this.leftTotal += parseInt($F(div+"num")) + 1;
                reported = ++this.leftReported;
                numDivs = this.leftDivs;
                arrDivs = this.arrLeftDivs;
            }
            else if( navBar == "rightNavBar" ) {
                this.rightTotal += parseInt($F(div+"num")) + 1;
                reported = ++this.rightReported;
                numDivs = this.rightDivs;
                arrDivs = this.arrRightDivs;
            }

            if( reported == numDivs ) {

                /*
                 * do we have more lines than permitted?
                 * if so we need to reduce display
                 */
                var overflow = this.leftTotal - this.maxLeftNumLines;
                if( navBar == "leftNavbar" && overflow > 0 ) {
                    this.adjust(this.arrLeftDivs, this.leftTotal, overflow);                                    	      }

                overflow = this.rightTotal - this.maxRightNumLines;
                if( navBar == "rightNavBar" && overflow > 0 )
                    this.adjust(this.arrRightDivs, this.rightTotal, overflow);

            } //end if
        };

        this.adjust = function(divs, total, overflow) {
            //spread reduction across all divs weighted according to number of lines each div has
            var num2reduce;
            var numLines;
            var threshold;
            for( var idx = 0; idx < divs.length; ++idx ) {
                numLines = parseInt($F(divs[idx].id + "num"));
                num2reduce = Math.ceil(overflow * (numLines/total));
                if( num2reduce == numLines && num2reduce > 0 )
                    --num2reduce;

                threshold = numLines - num2reduce;
                listDisplay(divs[idx].id, threshold);
            }
        };

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

function grabEnter(event){
  if(window.event && window.event.keyCode == 13){
      popupPage(600,800,'<bean:message key="oscarEncounter.Index.popupSearchPageWindow"/>', document.forms['ksearch'].channel.options[document.forms['ksearch'].channel.selectedIndex].value+urlencode(document.forms['ksearch'].keyword.value));
      return false;
  }else if (event && event.which == 13){
      popupPage(600,800,'<bean:message key="oscarEncounter.Index.popupSearchPageWindow"/>', document.forms['ksearch'].channel.options[document.forms['ksearch'].channel.selectedIndex].value+urlencode(document.forms['ksearch'].keyword.value));
      return false;
  }
}

function grabEnterGetTemplate(event){


  if(window.event && window.event.keyCode == 13){
      return false;
  }else if (event && event.which == 13){
      return false;
  }
}


</script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body onload="javascript:loader();" onunload="javascript:onClosing();"
	topmargin="0" leftmargin="0" bottommargin="0" rightmargin="0"
	vlink="#0000FF">

<html:errors />
<div id="templatejs" style="display: none"></div>
<table border="0" cellpadding="0" cellspacing="0"
	style="border-collapse: collapse; width: 100%; height: 680;"
	bordercolor="#111111" id="scrollNumber1"
	name="<bean:message key="oscarEncounter.Index.encounterTable"/>">
	<tr>
		<td class="hidePrint" bgcolor="#003399"
			style="width: auto; border-right: 2px solid #A9A9A9; height: 34px;">

		<div class="Title">&nbsp;<bean:message
			key="oscarEncounter.Index.msgEncounter" />&nbsp;&nbsp; <%=famDocName%>&nbsp;<%=famDocSurname%>
		</div>

		<div class="Title" style="margin: 0 auto; text-align: center"><a
			href="javascript: function myFunction() {return false; }"
			title="<bean:message key="oscarEncounter.Index.calculators"/>"
			onClick="popupPage(150,200,'calculatorWin','calculators.jsp?sex=<%=bean.patientSex%>&age=<%=pAge%>'); return false;"><img
			alt="<bean:message key="oscarEncounter.Index.calculators"/>"
			src="graphics/calculator.gif"></a></div>
		</td>

		<td bgcolor="#003399"
			style="text-align: right; height: 34px; padding-left: 2px;">
		<table name="tileTable"
			style="width: 100%; vertical-align: middle; border-collapse: collapse;">
			<tr>
				<td class="Header"
					style="width: 100%; height: 30px; padding-left: 2px; padding-right: 2px; border-right: 2px solid #003399; text-align: left; font-weight: bold;"
					NOWRAP>
				<%
                            String winName = "Master" + bean.demographicNo;
                            String url = "../demographic/demographiccontrol.jsp?demographic_no=" + bean.demographicNo + "&displaymode=edit&dboperation=search_detail";
                        %> <a href="#"
					style="font-size: 11px; text-decoration: none"
					onClick="popupPage(700,1000,'<%=winName%>','<%=url%>'); return false;"
					title="<bean:message key="provider.appointmentProviderAdminDay.msgMasterFile"/>"><%=bean.patientLastName %>,
				<%=bean.patientFirstName%></a>&nbsp;<%=bean.patientSex%> <%=bean.patientAge%>

				<a
					href="javascript:popupPage(400,850,'ApptHist','../demographic/demographiccontrol.jsp?demographic_no=<%=bean.demographicNo%>&last_name=<%=bean.patientLastName%>&first_name=<%=bean.patientFirstName%>&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=25')"
					style="font-size: 11px; text-decoration: none;"
					title="Click to see appointment history"><span
					style="margin-left: 20px;">Next Appt: <oscar:nextAppt
					demographicNo="<%=bean.demographicNo%>" /></span></a>

				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

				<form style="display: inline;" name="ksearch"><select
					name="channel">
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
				</select> <input type="text" name="keyword" value=""
					onkeypress="return grabEnter(event)" /> <input type="button"
					name="button" value="Search"
					onClick="popupPage(600,800,'<bean:message key="oscarEncounter.Index.popupSearchPageWindow"/>',forms['ksearch'].channel.options[forms['ksearch'].channel.selectedIndex].value+urlencode(forms['ksearch'].keyword.value) ); return false;">

				</form>

				</td>
				<%-- <td class="Header" style="text-align:center;border-right: 3px solid #003399" NOWRAP>
                                <a href="javascript:popupPage(300,400,'utility','Help.jsp')"><bean:message key="global.help"/></a> | <a href="javascript:popupPage(300,400,'utility','About.jsp')"><bean:message key="global.about"/></a>
                        </td>--%>
			</tr>

		</table>

		</td>
	</tr>
	<tr style="height: 100%">
		<td
			style="width: 22%; border-top: 2px solid #A9A9A9; border-right: 2px solid #A9A9A9; vertical-align: top">
		<div id="leftNavbar" style="height: 100%; width: 100%;"><caisi:isModuleLoad
			moduleName="caisi">
			<%String hrefurl2=request.getContextPath()+"/casemgmt/forward.jsp?action=view&demographicNo="+bean.demographicNo+"&providerNo="+bean.providerNo+"&providerName="+URLEncoder.encode(bean.userName);%>
			<a href="<%=hrefurl2%>">Case Management Encounter</a>
		</caisi:isModuleLoad></div>
		</td>
		<td style="background-color: #CCCCFF;" width="78%" valign="top">
		<form name="encForm" action="SaveEncounter2.do" method="POST">
		<input type="hidden" id="reloadDiv" name="reloadDiv" value="none"
			onchange="updateDiv();"> <caisi:isModuleLoad
			moduleName="caisi">
			<input type="hidden" name="casetoEncounter" value="true">
		</caisi:isModuleLoad>
		<table width="100%" name="encounterTableRightCol">
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
						<div
							style="display: inline; float: right; font-size: 8pt; text-align: right; vertical-align: bottom">
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
						<div class="RowTop">
						<% if(oscarVariables.getProperty("medicalHistory", "").length() > 1) {
                                        out.print(oscarVariables.getProperty("medicalHistory", ""));
                                    %> <% } else { %> <bean:message
							key="oscarEncounter.Index.medHist" />: <% } %>
						</div>
						</td>
					</tr>

					<tr width="100%">
						<!-- This is the Social History cell ...sh...-->
						<td valign="top"><!-- Creating the table tag within the script allows you to adjust all table sizes at once, by changing the value of leftCol -->
						<textarea name="shTextarea" tabindex="1" wrap="hard" cols="28"
							style="height:<%=windowSizes.getProperty("rowOneSize")%>;overflow:auto"><%=bean.socialHistory%></textarea>
						</td>
						<!-- This is the Family History cell ...fh...-->
						<td valign="top"><textarea name="fhTextarea" tabindex="2"
							wrap="hard" cols="28"
							style="height:<%=windowSizes.getProperty("rowOneSize")%>;overflow:auto"><%=bean.familyHistory%></textarea>
						</td>
						<!-- This is the Medical History cell ...mh...-->
						<td valign="top" colspan="2"><textarea name="mhTextarea"
							tabindex="3" wrap="hard" cols="28"
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
						<div
							style="display: inline; float: right; font-size: 8pt; text-align: right; vertical-align: bottom">
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
						<div class="RowTop"><bean:message
							key="oscarEncounter.Index.msgReminders" />:</div>
						</td>
					</tr>
					<tr width="100%">
						<td valign="top" style="border-right: 2px solid #ccccff"><textarea
							name='ocTextarea' tabindex="4" wrap="hard" cols="44"
							style="height:<%=windowSizes.getProperty("rowTwoSize")%>;overflow:auto"><%=bean.ongoingConcerns%></textarea>
						</td>
						<td colspan="2" valign="top"><textarea name='reTextarea'
							tabindex="5" wrap="hard" cols="44"
							style="height:<%=windowSizes.getProperty("rowTwoSize")%>;overflow:auto"><%=bean.reminders%></textarea>
						</td>
					</tr>
				</table>
				</td>
			</tr>

			<!-- prescription row -->
			<tr>
				<td>
				<table bgcolor="#ccccff" id="presTopTable" border="0" width="100%">
					<tr>
						<!--hr style="border-bottom: 0pt solid #888888; background-color: #888888;"-->
						<td valign="top">
						<div class="RowTop"><a href=#
							onClick="popupPage(700,960,'allergy','../oscarRx/showAllergy.do?demographicNo=<%=bean.demographicNo%>');return false;"><bean:message
							key="global.allergies" /></a>:&nbsp;&nbsp;&nbsp&nbsp;&nbsp;&nbsp&nbsp;&nbsp;</div>
						<div class="presBox" id="allergyBox">
						<ul>
							<%
								org.oscarehr.common.model.Allergy[] allergies =RxPatientData.getPatient(loggedInInfo, Integer.parseInt(demoNo)).getAllergies(loggedInInfo);

                                            for (int j=0; j<allergies.length; j++){%>
							<li><a
								title="<%= allergies[j].getDescription() %>">
							<%=allergies[j].getShortDesc(13,8,"...")%> </a></li>
							<%}%>
						</ul>
						</div>

						</td>
						<td width="85%" style="font-size: 10px;">
						<table width="100%" cellpadding=0 cellspacing=0 border="0">
							<tr>
								<td width=60>
								<div class="RowTop">Rx Date
								</td>
								<td>
								<div class="RowTop">
								<div class="RowTop"><a href=#
									onClick="popupPage(700,1027,'Rx','../oscarRx/choosePatient.do?providerNo=<%=bean.providerNo%>&demographicNo=<%=bean.demographicNo%>');return false;"><bean:message
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
						<div class="presBox" style="width: 100%;" id="presBox">
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
								<td <%=styleColor%>
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
									onClick="popupPage(600,700,'<bean:message key="oscarEncounter.Index.popupPage2Window"/>','../report/reportecharthistory.jsp?demographic_no=<%=bean.demographicNo%>');return false;">
								<bean:message key="global.encounter" />: <%=bean.patientLastName %>,
								<%=bean.patientFirstName%> </a> <%if (sChart) {%> &nbsp; &nbsp; &nbsp;<!--http://localhost:8084/oscar/oscarEncounter/echarthistoryprint.jsp?echartid=7491&demographic_no=10090-->
								<a href="javascript: function myFunction() {return false; }"
									onClick="showpic('splitChartLayer','splitChartId');"
									id="splitChartId"> Split Chart </a> <%}%> <!-- encounter template -->

								<input id="enTemplate" tabindex="6" autocomplete="off" size="25"
									type="text" value=""
									onkeypress="return grabEnterGetTemplate(event)" />
								<div class="enTemplate_name_auto_complete" id="enTemplate_list"
									style="display: none"></div>

								<script type="text/javascript">
                                    function menuAction(){
                                       var name = document.getElementById('enTemplate').value;
                                       var func = autoCompleted[name];
                                       eval(func);
                                    }
                                new Autocompleter.Local('enTemplate', 'enTemplate_list', autoCompList, { colours: itemColours, afterUpdateElement: menuAction }  );
                                </script> <!-- end template --></div>



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

                            }catch(Exception eee){MiscUtils.getLogger().error("Error", eee); }
                            %>
					<tr>
						<td colspan="2" valign="top" style="text-align: left"><textarea
							name='enTextarea' tabindex="7" wrap="hard" cols="90"
							style="height:<%=windowSizes.getProperty("rowThreeSize")%>;overflow:auto"><%=encounterText%></textarea>
						</td>
					</tr>
				</table>
				<table border=0 bgcolor="#CCCCFF" width=100%>
					<tr nowrap>
						<td><input type="hidden" name="subject"
							value="<%=UtilMisc.htmlEscape(bean.reason)%>"> <% if (consumption>=50) {%>
						<input type="submit" style="height: 20px" name="buttonPressed"
							value="Split Chart" class="ControlPushButton2"
							onClick="return (onSplit());"> <% } %>
						</td>
						<td style="text-align: right" nowrap><oscar:oscarPropertiesCheck
							property="CPP" value="yes">
							<input type="button" style="height: 20px;"
								class="ControlPushButton2" value="CPP"
								onClick="document.forms['encForm'].btnPressed.value='Save'; popupPage(700, 960, 'cpp', 'encounterCPP.jsp'); document.forms['encForm'].submit();" />
						</oscar:oscarPropertiesCheck> <input type="button" style="height: 20px;"
							class="ControlPushButton2"
							value="<bean:message key="global.btnPrint"/>"
							onClick="document.forms['encForm'].btnPressed.value='Save'; popupPage(700, 960, 'print', 'encounterPrint.jsp');" />
						<input type="hidden" name="btnPressed" value=""> <!-- security code block -->
						<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart"
							rights="w">
							<% if(!bPrincipalControl || (bPrincipalControl && bPrincipalDisplay) ) { %>
							<input type="button" style="height: 20px"
								value="<bean:message key="oscarEncounter.Index.btnSave"/>"
								class="ControlPushButton2"
								onclick="document.forms['encForm'].btnPressed.value='Save'; document.forms['encForm'].submit();">
							<input type="button" style="height: 20px"
								value="<bean:message key="oscarEncounter.Index.btnSignSave"/>"
								class="ControlPushButton2"
								onclick="document.forms['encForm'].btnPressed.value='Sign,Save and Exit'; document.forms['encForm'].submit();">
							<oscar:oscarPropertiesCheck property="billregion" value="BC">
								<input type="button" style="height: 20px"
									value="<bean:message key="oscarEncounter.Index.btnSignSaveBill"/>"
									class="ControlPushButton2"
									onclick="document.forms['encForm'].btnPressed.value='Sign,Save and Bill'; document.forms['encForm'].submit();">
							</oscar:oscarPropertiesCheck>
							<security:oscarSec roleName="<%=roleName$%>"
								objectName="_eChart.verifyButton" rights="w">
								<input type="button" style="height: 20px"
									value="<bean:message key="oscarEncounter.Index.btnSign"/>"
									class="ControlPushButton2"
									onclick="document.forms['encForm'].btnPressed.value='Verify and Sign'; document.forms['encForm'].submit();">
							</security:oscarSec>
							<% } %>
						</security:oscarSec> <!-- security code block --> <input type="button"
							style="height: 20px" name="buttonPressed"
							value="<bean:message key="global.btnExit"/>"
							class="ControlPushButton2"
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
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>
		<td class="MainTableBottomRowRightColumn" valign="top">&nbsp;</td>
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
			onClick="hidepic('splitChartLayer');popupPage(600,700,'<bean:message key="oscarEncounter.Index.popupPage2Window"/>','echarthistoryprint.jsp?echartid=<%=s[0]%>&demographic_no=<%=bean.demographicNo%>');return false;">
		<%=s[1]%> </a></td>
		<td class="wcblayerItem">&nbsp;</td>
	</tr>
	<%}%>
</table>
</div>
<%}%>
<script type="text/javascript">
    setTimeout("updateDiv();", 1000);
</script>

</body>
</html:html>
