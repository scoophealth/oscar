<!--  
/*
 * 
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
 */
-->
 <%
  if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");
%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="oscar.oscarEncounter.pageUtil.*"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.pageUtil.*"%>
<%@ page import="java.util.Vector;"%>
<%
    response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
    response.setHeader("Pragma","no-cache"); //HTTP 1.0
    response.setDateHeader ("Expires", 0); //prevents caching at the proxy   
    System.out.println(session.getServletContext().getRealPath("../form/VTForm.xml"));
%>

<html:html locale="true">

<head>
<title>
Vascular Tracker
</title>
<style type="text/css">
        a:link{
            text-decoration: none;
            color:#000000;
        }

        a:active{
            text-decoration: none;
            color:#000000;
        }

        a:visited{
            text-decoration: none;
            color:#000000;
        }

        a:hover{
            text-decoration: none;
            color:#000000;
        }

	.Head {
            background-color:#BBBBBB;
            padding-top:3px;
            padding-bottom:3px;           
            height: 30px;
            font-size:9pt;
        }

        .Head INPUT {
            width: 100px;
        }

        .Head A {
            font-size:9pt;
        }

        BODY {
            font-family: Arial, Verdana, Tahoma, Helvetica, sans-serif;             
            background-color: #F2F2F2;            
        }

        TABLE {
            font-family: Arial, Verdana, Tahoma, Helvetica, sans-serif;
        }
        
        TD{
            font-size:9pt;
            empty-cells:show;
        }

        TH{
            font-size:9pt;
            font-weight: normal;
            text-align:left;
        }
        

        .subject {
            background-color: #000000;
            color: #FFFFFF;  
            font-size: 15pt;
            font-weight: bold;
            text-align: left;
        }

        .title {            
            text-align: left;
            vertical-align: bottom;
            font-weight: bold;
            border-bottom: 3px groove #000000;
        }
        .soap{
            text-align: left;
            vertical-align: bottom;
            font-weight: bold;
        }
        .subTitle {            
            text-align: center;
            font-weight: bold;
            vertical-align: bottom;
            border-bottom: 2px groove #000000;
        }
        .note {
            font-size:80%;             
        }
        .data {
            background-color:#F7F7F7;
            font-size:80%;
        }
        .eightyPercent{
            font-size:80%;
        }
        .ninetyPercent{
            font-size:90%;
        }
        .dataEntryTable{
            border-style: solid;
            border-width: 1px;
            border-color: #BBBBBB;
            border-collapse: collapse
            empty-cells: show;            
        }
        

    </style>

<html:base/>

</head>

<script type="text/javascript">

    function onPrint() {
//        document.forms[0].submit.value="print";
//        var ret = checkAllDates();
//        if(ret==true)
//        {
//            ret = confirm("Do you wish to save this form and view the print preview?");
//        }
//        return ret;
        window.print();
    }
    function onSave() {
        document.forms[0].submit.value="save"; 
        storeSMKSupportData();
        storeFTExamSupportData();
        storeEyeExamSupportData();
            var ret = confirm("Are you sure you want to save this form?");
        
        return ret;
    }
    function onExit() {
        if(confirm("Are you sure you wish to exit without saving your changes?")==true)
        {
            window.close();
        }
        return(false);
    }
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        storeSMKSupportData();
        storeFTExamSupportData();
        storeEyeExamSupportData();
            var ret = confirm("Are you sure you wish to save and close this window?");
        
        return ret;
    }

function popupDecisiontSupport(){
    var varpage = "<bean:write name="decisionSupportURL"/>";
    var vheight = 500;
    var vwidth = 400;
    var posX = 400;
    var posY = 0;
    windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=600";
    var popup=window.open(varpage, "Decision Support", windowprops);  
    
 }
  
function showHideItem(id){ 
    if(document.getElementById(id).style.display == 'none')
        document.getElementById(id).style.display = 'block'; 
    else
        document.getElementById(id).style.display = 'none'; 
}

function storeSMKSupportData(){
    //getElementbyId function doesn't work on these elements
    //here is the work around
    var e = document.forms[0].elements;
    var smkSDate, smkHDate, smkCDate;
    var smkSCmt, smkHCmt, smkCCmt;
    for(i=0; i< 20; i++){
        switch(e[i].name){
            case 'value(SmkSDate)': smkSDate = i;  break;
            case 'value(SmkHDate)': smkHDate = i;  break;
            case 'value(SmkCDate)': smkCDate = i;  break;
            
            case 'value(SmkSComments)': smkSCmt = i;  break;
            case 'value(SmkHComments)': smkHCmt = i;  break;
            case 'value(SmkCComments)': smkCCmt = i;  break;
        }
    }
    
    document.forms[0].elements[smkSDate].value = document.getElementById('SmkDate').value;
    document.forms[0].elements[smkHDate].value = document.getElementById('SmkDate').value;
    document.forms[0].elements[smkCDate].value = document.getElementById('SmkDate').value;
    
    document.forms[0].elements[smkSCmt].value = document.getElementById('SmkComments').value;
    document.forms[0].elements[smkHCmt].value = document.getElementById('SmkComments').value;
    document.forms[0].elements[smkCCmt].value = document.getElementById('SmkComments').value;
}


function storeFTExamSupportData(){
    //getElementbyId function doesn't work on these elements
    //here is the work around
    var e = document.forms[0].elements;
    var ftNoId, ftNeId, ftIsId, ftInId, ftUlId, ftOtId, ftReId;
    var ftNoCmt, ftNeCmt, ftIsCmt, ftInCmt, ftUlCmt, ftOtCmt, ftReCmt;
    for(i=50; i< e.length; i++){
        switch(e[i].name){
            case 'value(FTNoDate)': ftNoId = i;  break;
            case 'value(FTNeDate)': ftNeId = i;  break;
            case 'value(FTIsDate)': ftIsId = i;  break;
            case 'value(FTUlDate)': ftUlId = i;  break;
            case 'value(FTInDate)': ftInId = i;  break;
            case 'value(FTOtDate)': ftOtId = i;  break;
            case 'value(FTReDate)': ftReId = i;  break;
            
            case 'value(FTNoComments)': ftNoCmt = i;  break;
            case 'value(FTNeComments)': ftNeCmt = i;  break;
            case 'value(FTIsComments)': ftIsCmt = i;  break;
            case 'value(FTUlComments)': ftUlCmt = i;  break;
            case 'value(FTInComments)': ftInCmt = i;  break;
            case 'value(FTOtComments)': ftOtCmt = i;  break;
            case 'value(FTReComments)': ftReCmt = i;  break;
        }
    }

    document.forms[0].elements[ftNoId].value = document.getElementById('FTDate').value;
    document.forms[0].elements[ftNeId].value = document.getElementById('FTDate').value;
    document.forms[0].elements[ftIsId].value = document.getElementById('FTDate').value;
    document.forms[0].elements[ftUlId].value = document.getElementById('FTDate').value;
    document.forms[0].elements[ftInId].value = document.getElementById('FTDate').value;
    document.forms[0].elements[ftOtId].value = document.getElementById('FTDate').value;
    document.forms[0].elements[ftReId].value = document.getElementById('FTDate').value;
    
    document.forms[0].elements[ftNoCmt].value = document.forms[0].FTComments.value;
    document.forms[0].elements[ftNeCmt].value = document.forms[0].FTComments.value;
    document.forms[0].elements[ftIsCmt].value = document.forms[0].FTComments.value;
    document.forms[0].elements[ftUlCmt].value = document.forms[0].FTComments.value;
    document.forms[0].elements[ftInCmt].value = document.forms[0].FTComments.value;
    document.forms[0].elements[ftOtCmt].value = document.forms[0].FTComments.value;
    document.forms[0].elements[ftReCmt].value = document.forms[0].FTComments.value;
}

function controlFTExam(){
    
    //getElementbyId function doesn't work on these elements
    //here is the work around
    var e = document.forms[0].elements;
    var ftNoId, ftNeId, ftIsId, ftInId, ftUlId, ftOtId, ftReId;
    for(i=50; i< e.length; i++){
        switch(e[i].name){
            case 'value(FTNoValue)': ftNoId = i;  break;
            case 'value(FTNeValue)': ftNeId = i;  break;
            case 'value(FTIsValue)': ftIsId = i;  break;
            case 'value(FTUlValue)': ftUlId = i;  break;
            case 'value(FTInValue)': ftInId = i;  break;
            case 'value(FTOtValue)': ftOtId = i;  break;
            case 'value(FTReValue)': ftReId = i;  break;
        }
    }
    
    if(document.forms[0].elements[ftNoId].checked == false){
        //enable all foot exam checkbox        
        //alert("enable all foot exam checkboxes");
        document.forms[0].elements[ftNeId].disabled= false;
        document.forms[0].elements[ftIsId].disabled= false;
        document.forms[0].elements[ftUlId].disabled= false;
        document.forms[0].elements[ftInId].disabled= false;
        document.forms[0].elements[ftOtId].disabled= false;
        document.forms[0].elements[ftReId].disabled= false;
    }
    else{
        //uncheck and disable all foot exam checkbox        
        //alert("uncheck all foot exam checkboxes");
        document.forms[0].elements[ftNeId].checked= false;
        document.forms[0].elements[ftIsId].checked= false;
        document.forms[0].elements[ftUlId].checked= false;
        document.forms[0].elements[ftInId].checked= false;
        document.forms[0].elements[ftOtId].checked= false;
        document.forms[0].elements[ftReId].checked= false;
        
        //alert("disable all foot exam checkboxes");
        document.forms[0].elements[ftNeId].disabled= true;
        document.forms[0].elements[ftIsId].disabled= true;
        document.forms[0].elements[ftUlId].disabled= true;
        document.forms[0].elements[ftInId].disabled= true;
        document.forms[0].elements[ftOtId].disabled= true;
        document.forms[0].elements[ftReId].disabled= true;
    }
}

function storeEyeExamSupportData(){
    //getElementbyId function doesn't work on these elements
    //here is the work around
    var e = document.forms[0].elements;
    var eyeNoId, eyeHypId, eyeDiaId, eyeOthId, eyeRefId;
    var eyeNoCmt, eyeHypCmt, eyeDiaCmt, eyeOthCmt, eyeRefCmt;
    for(i=50; i< e.length; i++){
        switch(e[i].name){
            case 'value(iNoDate)': eyeNoId = i;  break;
            case 'value(iHypDate)': eyeHypId = i;  break;
            case 'value(iDiaDate)': eyeDiaId = i;  break;
            case 'value(iOthDate)': eyeOthId = i;  break;
            case 'value(iRefDate)': eyeRefId = i;  break;
            
            case 'value(iNoComments)': eyeNoCmt = i;  break;
            case 'value(iHypComments)': eyeHypCmt = i;  break;
            case 'value(iDiaComments)': eyeDiaCmt = i;  break;
            case 'value(iOthComments)': eyeOthCmt = i;  break;
            case 'value(iRefComments)': eyeRefCmt = i;  break;
        }
    }

    document.forms[0].elements[eyeNoId].value = document.forms[0].iDate.value;
    document.forms[0].elements[eyeHypId].value = document.forms[0].iDate.value;
    document.forms[0].elements[eyeDiaId].value = document.forms[0].iDate.value;
    document.forms[0].elements[eyeOthId].value = document.forms[0].iDate.value;
    document.forms[0].elements[eyeRefId].value = document.forms[0].iDate.value;
    
    document.forms[0].elements[eyeNoCmt].value = document.forms[0].iComments.value;
    document.forms[0].elements[eyeHypCmt].value = document.forms[0].iComments.value;
    document.forms[0].elements[eyeDiaCmt].value = document.forms[0].iComments.value;
    document.forms[0].elements[eyeOthCmt].value = document.forms[0].iComments.value;
    document.forms[0].elements[eyeRefCmt].value = document.forms[0].iComments.value;    
}


function controlEyeExam(){
    
    //getElementbyId function doesn't work on these elements
    //here is the work around
    var e = document.forms[0].elements;
    var eyeNoId, eyeHypId, eyeDiaId, eyeOthId, eyeRefId;
    for(i=50; i< e.length; i++){
        switch(e[i].name){
            case 'value(iNoValue)': eyeNoId = i;  break;
            case 'value(iHypValue)': eyeHypId = i;  break;
            case 'value(iDiaValue)': eyeDiaId = i;  break;
            case 'value(iOthValue)': eyeOthId = i;  break;
            case 'value(iRefValue)': eyeRefId = i;  break;
        }
    }
    
    if(document.forms[0].elements[eyeNoId][0].checked == false){
        //enable all foot exam checkbox        
        //alert("enable all foot exam checkboxes");
        document.forms[0].elements[eyeHypId][0].disabled= false;
        document.forms[0].elements[eyeDiaId][0].disabled= false;
        document.forms[0].elements[eyeOthId][0].disabled= false;
        document.forms[0].elements[eyeRefId][0].disabled= false;
        document.forms[0].elements[eyeHypId][1].disabled= false;
        document.forms[0].elements[eyeDiaId][1].disabled= false;
        document.forms[0].elements[eyeOthId][1].disabled= false;
        document.forms[0].elements[eyeRefId][1].disabled= false;
    }
    else if(document.forms[0].elements[eyeNoId][0].checked == true){
        //uncheck and disable all foot exam checkbox        
        //alert("uncheck all foot exam checkboxes");
        document.forms[0].elements[eyeHypId][0].checked= false;
        document.forms[0].elements[eyeDiaId][0].checked= false;
        document.forms[0].elements[eyeOthId][0].checked= false;
        document.forms[0].elements[eyeRefId][0].checked= false;
        document.forms[0].elements[eyeHypId][1].checked= true;
        document.forms[0].elements[eyeDiaId][1].checked= true;
        document.forms[0].elements[eyeOthId][1].checked= true;
        document.forms[0].elements[eyeRefId][1].checked= true;
        
        //alert("disable all foot exam checkboxes");
        document.forms[0].elements[eyeHypId][0].disabled= true;
        document.forms[0].elements[eyeDiaId][0].disabled= true;
        document.forms[0].elements[eyeOthId][0].disabled= true;
        document.forms[0].elements[eyeRefId][0].disabled= true;
    }
}
</script>
<body class="BodyStyle" vlink="#0000FF" onload="window.focus();window.resizeTo(670,710); popupDecisiontSupport();">
<!--  -->
    
    <html:form action="/form/SubmitForm" enctype="multipart/form-data">    
    <link rel="stylesheet" type="text/css" href="../oscarEncounter/oscarMeasurements/styles/measurementStyle.css">    
    <link rel="stylesheet" type="text/css" media="print" href="print.css"/>
    <input type="hidden" name="value(formName)" value="VTForm"/>
    <html:hidden property="value(formId)"/>
    <table class="Head" class="hidePrint" width="640px" cellpadding="0" cellspacing="0">
        <tr>
            <td align="left">                
                <input type="submit" value="Save" onclick="javascript:return onSave();" />
                <input type="submit" value="Save and Exit" onclick="javascript:return onSaveExit();"/>
                <input type="submit" value="Exit" onclick="javascript:return onExit();"/>
                <input type="button" value="Print" onclick="javascript:return onPrint();"/>
            </td>
        </tr>
    </table>
    <table width="640px">
        <tr>            
            <td class="subject">
                VASCULAR TRACKER
            </td>
        </tr>
        <tr>                     
            <td>
               <table border=0 cellspacing=0 >
                <tr>
                    <td>
                        <table width="640px">
                            <tr>
                                <td>
                                    <table width="100%">
                                    <html:errors/>                                                      
                                        <tr>
                                            <td colspan="2">
                                                <logic:present name="EctSessionBean"><bean:write name="EctSessionBean" property="patientLastName"/> <bean:write name="EctSessionBean" property="patientFirstName"/> <bean:write name="EctSessionBean" property="patientSex"/> <bean:write name="EctSessionBean" property="patientAge"/></logic:present>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th class="soap" width="2%" colspan="2">S</th>
                                        </tr>
                                        <tr>
                                            <th></th>
                                            <th class="title" width="98%">
                                            <a href="javascript: showHideItem('history');" >History >> </a>
                                            </th>
                                        </tr>                                        
                                         <tr>
                                            <td></td>
                                            <td ><table cellpadding='1' cellspacing='0' id="history">
                                            <tr><td>
                                            <table cellpadding='1' cellspacing='0'width="100%">   
                                                <tr>
                                                    <th class="subTitle" style="text-align:left">
                                                        On Going Concern
                                                    </th>
                                                </tr>
                                                <tr>
                                                    <td><bean:write name="ongoingConcerns"/></td>
                                                </tr>
                                            </table>
                                            </td></tr>
                                            <tr><td>
                                            <table cellpadding='1' cellspacing='0'width="100%">
                                                <tr>
                                                    <td class="subTitle"  style="text-align:left" colspan='6'>
                                                        Cigarette Smoking
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class="subTitle" width="12%">
                                                       &nbsp;
                                                    </td>
                                                    <td class="subTitle" width="15%">
                                                        Status<br>[cig/day 0-80]
                                                    </td>
                                                    <td class="subTitle" width="15%">
                                                        History<br>[pk yrs 0-150]
                                                    </td>
                                                    <td class="subTitle" width="15%">
                                                        Last Quit<br>(yyyy-MM-dd)
                                                    </td>
                                                    <td class="subTitle" width="15%">
                                                        Ob. Date<br>(yyyy-MM-dd)                                                        
                                                    </td>                       
                                                    <td class="subTitle" width="28%">
                                                        <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingComments"/>
                                                    </td>                                                    
                                                </tr>
                                                <tr class="dataEntryTable">
                                                    <td class="dataEntryTable">Last Data</td>
                                                    <td class="dataEntryTable"><font style="font-size:80%">
                                                        <logic:present name="SmkSLastData">
                                                            <bean:write name="SmkSLastData"/>
                                                            <html:hidden property="value(SmkSLastDataEnteredDate)"/>
                                                            <html:hidden property="value(SmkSLastData)"/>                                                        
                                                        </logic:present>
                                                        <logic:notPresent name="SmkSLastData">    
                                                            &nbsp;
                                                        </logic:notPresent></font>
                                                    </td>
                                                    <td class="dataEntryTable"><font style="font-size:80%">
                                                        <logic:present name="SmkHLastData">
                                                            <bean:write name="SmkHLastDataEnteredDate"/>
                                                            <html:hidden property="value(SmkHLastDataEnteredDate)"/>
                                                            <html:hidden property="value(SmkHLastData)"/>                                                        
                                                        </logic:present>
                                                        <logic:notPresent name="SmkHLastData">    
                                                            &nbsp;
                                                        </logic:notPresent></font>
                                                    </td>
                                                    <td class="dataEntryTable">
                                                        <logic:present name="SmkCLastData"><font style="font-size:80%">                                                       
                                                            <bean:write name="SmkCLastDataEnteredDate"/>
                                                            <html:hidden property="value(SmkCLastDataEnteredDate)"/>
                                                            <html:hidden property="value(SmkCLastData)"/>
                                                        </logic:present>
                                                        <logic:notPresent name="SmkCLastData">    
                                                            &nbsp;
                                                        </logic:notPresent></font>
                                                    </td>
                                                    <td class="dataEntryTable"><font style="font-size:80%">
                                                        <logic:present name="SmkCLastDataEnteredDate">
                                                            <bean:write name="SmkCLastDataEnteredDate"/>
                                                        </logic:present>
                                                        <logic:notPresent name="SmkCLastDataEnteredDate">    
                                                            &nbsp;
                                                        </logic:notPresent></font>                                                        
                                                    </td>
                                                    <td class="dataEntryTable">&nbsp;</td>
                                                </tr>
                                                <tr class="dataEntryTable">
                                                    <td class="dataEntryTable">New Data</td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(SmkSValue)" size="8%" /></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(SmkHValue)" size="8%" /></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(SmkCValue)" size="8%" /></td>
                                                    <td class="dataEntryTable" align="center"><input type="text" id="SmkDate" name="SmkDate" value="<bean:write name="SmkSDate"/>" size="8%" /></td>
                                                    <td class="dataEntryTable" align="center"><input type="text" id="SmkComments" name="SmkComments" value="<bean:write name="SmkSComments"/>" size="25%" tabindex="9999"/></td>
                                                    <html:hidden property="value(SmkSComments)"/>
                                                    <html:hidden property="value(SmkSDate)"/>
                                                    <html:hidden property="value(SmkHComments)"/>
                                                    <html:hidden property="value(SmkHDate)"/>
                                                    <html:hidden property="value(SmkCComments)"/>
                                                    <html:hidden property="value(SmkCDate)"/>
                                                </tr>                                                
                                            </table></td></tr>
                                            <tr><td>
                                             <table cellpadding='1' cellspacing='0'>
                                                <tr>
                                                    <td class="subTitle"  style="text-align:left" colspan='4'>
                                                        Lifestyle
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class="subTitle" width="39%">                                                        
                                                    </td>                                                    
                                                    <td class="subTitle" width="16%">
                                                        Last Data<br> Entered on
                                                    </td>
                                                    <td class="subTitle" width="10%">
                                                        New Data
                                                    </td>                                                                                                        
                                                    <td class="subTitle" width="35%">
                                                        <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingComments"/>
                                                    </td>                                                    
                                                </tr>                                                
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="ExerDesc"/> <font class="eightyPercent"><bean:write name="ExerMeasuringInstrc"/></font></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="ExerLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="ExerLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="ExerLastData"/></td></tr>
                                                            <html:hidden property="value(ExerLastDataEnteredDate)"/>
                                                            <html:hidden property="value(ExerLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="ExerLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(ExerValue)" size="4%" /></td>
                                                    <html:hidden property="value(ExerDate)"/>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(ExerComments)" size="30%" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="DietDesc"/> <font class="eightyPercent"><bean:write name="DietMeasuringInstrc"/></font></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="DietLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="DietLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="DietLastData"/></td></tr>
                                                            <html:hidden property="value(DietLastDataEnteredDate)"/>
                                                            <html:hidden property="value(DietLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="DietLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(DietValue)" size="4%" /></td>
                                                    <html:hidden property="value(DietDate)"/>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(DietComments)" size="30%" tabindex="9999"/></td>
                                                 </tr>
                                             </table></td></tr>
                                            <tr><td>
                                             <table cellpadding='1' cellspacing='0'>
                                                <tr>
                                                    <td class="subTitle"  style="text-align:left" colspan='4'>
                                                        Psychosocial Screen
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class="subTitle" width="36%">                                                        
                                                    </td>                                                    
                                                    <td class="subTitle" width="16%">
                                                        Last Data<br> Entered on
                                                    </td>
                                                    <td class="subTitle" width="18%">
                                                        New Data
                                                    </td>                                                                                                        
                                                    <td class="subTitle" width="30%">
                                                        <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingComments"/>
                                                    </td>                                                    
                                                </tr>                                                
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="DpScDisplay"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="DpScLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="DpScLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="DpScLastData"/></td></tr>
                                                            <html:hidden property="value(DpScLastDataEnteredDate)"/>
                                                            <html:hidden property="value(DpScLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="DpScLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center">
                                                        <html:radio property="value(DpScValue)" value="yes" />Yes
                                                        <html:radio property="value(DpScValue)" value="no" />No
                                                    </td>
                                                    <html:hidden property="value(DpScDate)"/>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(DpScComments)" size="30%" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="StScDisplay"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="StScLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="StScLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="StScLastData"/></td></tr>
                                                            <html:hidden property="value(StScLastDataEnteredDate)"/>
                                                            <html:hidden property="value(StScLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="StScLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center">
                                                        <html:radio property="value(StScValue)" value="Yes" />Yes 
                                                        <html:radio property="value(StScValue)" value="No" />No 
                                                    </td>
                                                    <html:hidden property="value(StScDate)"/>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(StScComments)" size="30%"tabindex="9999"/></td>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="LcCtDisplay"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="LcCtLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="LcCtLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="LcCtLastData"/></td></tr>
                                                            <html:hidden property="value(LcCtLastDataEnteredDate)"/>
                                                            <html:hidden property="value(LcCtLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="LcCtLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center">
                                                        <html:radio property="value(LcCtValue)" value="yes" />Yes 
                                                        <html:radio property="value(LcCtValue)" value="no" />No 
                                                    </td>
                                                    <html:hidden property="value(LcCtDate)"/>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(LcCtComments)" size="30%" tabindex="9999"/></td>
                                                 </tr>
                                             </table></td></tr>
                                            <tr><td>
                                             <table cellpadding='1' cellspacing='0'>
                                                <tr>
                                                    <td class="subTitle"  style="text-align:left" colspan='4'>
                                                        Medication Adherence screen
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class="subTitle" width="36%">                                                        
                                                    </td>                                                    
                                                    <td class="subTitle" width="16%">
                                                        Last Data<br> Entered on
                                                    </td>
                                                    <td class="subTitle" width="18%">
                                                        New Data
                                                    </td>                                                                                                        
                                                    <td class="subTitle" width="30%">
                                                        <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingComments"/>
                                                    </td>                                                    
                                                </tr>                                                
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="MedGDisplay"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="MedGLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="MedGLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="MedGLastData"/></td></tr>
                                                            <html:hidden property="value(MedGLastDataEnteredDate)"/>
                                                            <html:hidden property="value(MedGLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="MedGLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center">
                                                        <html:radio property="value(MedGValue)" value="yes" />Yes 
                                                        <html:radio property="value(MedGValue)" value="no" />No 
                                                    </td>
                                                    <html:hidden property="value(MedGDate)"/>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(MedGComments)" size="30%" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="MedNDisplay"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="MedNLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="MedNLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="MedNLastData"/></td></tr>
                                                            <html:hidden property="value(MedNLastDataEnteredDate)"/>
                                                            <html:hidden property="value(MedNLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="MedNLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center">
                                                        <html:radio property="value(MedNValue)" value="yes" />Yes 
                                                        <html:radio property="value(MedNValue)" value="no" />No
                                                    </td>
                                                    <html:hidden property="value(MedNDate)"/>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(MedNComments)" size="30%" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="MedRDisplay"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="MedRLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="MedRLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="MedRLastData"/></td></tr>
                                                            <html:hidden property="value(MedRLastDataEnteredDate)"/>
                                                            <html:hidden property="value(MedRLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="MedRLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center">
                                                        <html:radio property="value(MedRValue)" value="yes" />Yes 
                                                        <html:radio property="value(MedRValue)" value="no" />No 
                                                    </td>
                                                    <html:hidden property="value(MedRDate)"/>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(MedRComments)" size="30%" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="MedADisplay"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="MedALastData">
                                                        <table cellpadding='0' cellspacing='0'>
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="MedALastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="MedALastData"/></td></tr>                                                            
                                                            <html:hidden property="value(MedALastDataEnteredDate)"/>
                                                            <html:hidden property="value(MedALastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="MedALastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center">
                                                        <html:radio property="value(MedAValue)" value="yes" />Yes
                                                        <html:radio property="value(MedAValue)" value="no" />No
                                                    </td>
                                                    <html:hidden property="value(MedADate)"/>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(MedAComments)" size="30%" tabindex="9999"/></td>
                                                 </tr>
                                             </table></td></tr>
                                            </table></td>
                                        </tr>
                                        <tr>
                                            <th class="soap" width="2%">O</th>
                                            <th></th>
                                        </tr>
                                        <tr>
                                            <th></th>
                                            <th class="title">
                                            <a href="javascript: showHideItem('vital');">Vitals >> </a>
                                            </th>
                                        </tr>
                                         <tr>
                                            <td></td>                                            
                                            <td><table style="display:none" cellpadding='1' cellspacing='0' id="vital">
                                                <tr>
                                                    <td class="subTitle" width="34%">                                                        
                                                    </td>                                                    
                                                    <td class="subTitle" width="16%">
                                                        Last Data<br> Entered on
                                                    </td>
                                                    <td class="subTitle" width="7%">
                                                        New Data
                                                    </td>                                                    
                                                    <td class="subTitle" width="10%">
                                                        Ob. Date<br> (yyyy-MM-dd)
                                                    </td>
                                                    <td class="subTitle" width="33%">
                                                        <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingComments"/>
                                                    </td>                                                    
                                                </tr>
                                                <tr>
                                                    <td class="dataEntryTable"><bean:write name="BPDisplay"/><br><font class="eightyPercent"><bean:write name="BPMeasuringInstrc"/></font></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="BPLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="BPLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="BPLastData"/></td></tr>
                                                            <html:hidden property="value(BPLastDataEnteredDate)"/>
                                                            <html:hidden property="value(BPLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="BPLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(BPValue)" size="5%" /></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(BPDate)" size="10%" tabindex="9999"/></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(BPComments)" size="34%" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="WHRDisplay"/><br><font class="eightyPercent"><bean:write name="WHRMeasuringInstrc"/></font></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="WHRLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="WHRLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="WHRLastData"/></td></tr>
                                                            <html:hidden property="value(WHRLastDataEnteredDate)"/>
                                                            <html:hidden property="value(WHRLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="WHRLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(WHRValue)" size="5%"/></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(WHRDate)" size="10%" tabindex="9999"/></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(WHRComments)" size="34%" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="WCDisplay"/><br><font class="eightyPercent"><bean:write name="WCMeasuringInstrc"/></font></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="WCLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="WCLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="WCLastData"/></td></tr>
                                                            <html:hidden property="value(WCLastDataEnteredDate)"/>
                                                            <html:hidden property="value(WCLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="WCLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(WCValue)" size="5%"/></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(WCDate)" size="10%" tabindex="9999"/></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(WCComments)" size="34%" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="HCDisplay"/><br><font class="eightyPercent"><bean:write name="HCMeasuringInstrc"/></font></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="HCLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="HCLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="HCLastData"/></td></tr>
                                                            <html:hidden property="value(HCLastDataEnteredDate)"/>
                                                            <html:hidden property="value(HCLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="HCLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(HCValue)" size="5%"/></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(HCDate)" size="10%" tabindex="9999"/></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(HCComments)" size="34%" tabindex="9999"/></td>
                                                 </tr>                                                 
                                                 <tr class="dataEntryTable">
                                                    <td class="dataEntryTable"><bean:write name="WTDisplay"/><br><font class="eightyPercent"><bean:write name="WTMeasuringInstrc"/></font></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="WTLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="WTLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="WTLastData"/></td></tr>
                                                            <html:hidden property="value(WTLastDataEnteredDate)"/>
                                                            <html:hidden property="value(WTLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="WTLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(WTValue)" size="5%" /></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(WTDate)" size="10%" tabindex="9999"/></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(WTComments)" size="34%" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr class="dataEntryTable">                                                    
                                                    <td class="dataEntryTable"><bean:write name="HTDisplay"/><br><font class="eightyPercent"><bean:write name="HTMeasuringInstrc"/></font></td>   
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="HTLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="HTLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="HTLastData"/></td></tr>
                                                            <html:hidden property="value(HTLastDataEnteredDate)"/>
                                                            <html:hidden property="value(HTLastData)"/>
                                                        </table>
                                                        </logic:present>                                                        
                                                        <logic:notPresent name="HTLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(HTValue)" size="5%" /></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(HTDate)" size="10%" tabindex="9999"/></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(HTComments)" size="34%" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="HRDisplay"/><br><font class="eightyPercent"><bean:write name="HRMeasuringInstrc"/></font></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="HRLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="HRLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="HRLastData"/></td></tr>
                                                            <html:hidden property="value(HRLastDataEnteredDate)"/>
                                                            <html:hidden property="value(HRLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="HRLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(HRValue)" size="5%" /></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(HRDate)" size="10%" tabindex="9999"/></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(HRComments)" size="34%" tabindex="9999"/></td>
                                                 </tr>
                                            </table></td>
                                        </tr> 
                                       <tr>
                                            <th></th>
                                            <th class="title">
                                            <a href="javascript: showHideItem('examination');" >Examination >> </a>
                                            </th>
                                        </tr>
                                         <tr>
                                            <td></td>
                                            <td><table style="display:none" cellpadding='1' cellspacing='0' id="examination">
                                                <tr>
                                                    <td class="subTitle" width="30%">                                                        
                                                    </td>                                                    
                                                    <td class="subTitle" width="17%">
                                                        Last Data<br>Entered on
                                                    </td>
                                                    <td class="subTitle" width="20%">
                                                        New Data
                                                    </td>                                                    
                                                    <td class="subTitle" width="10%">
                                                        Ob. Date<br> (yyyy-MM-dd)
                                                    </td>
                                                    <td class="subTitle" width="24%">
                                                        <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingComments"/>
                                                    </td>                                                    
                                                </tr>
                                                <tr class="dataEntryTable">
                                                    <th class="dataEntryTable" colspan="3">Foot Exam</th>                                                    
                                                    <td class="dataEntryTable" valign="top" align="center"><input type="text" id="FTDate" name="FTDate" value="<bean:write name="FTNoDate"/>" size="10%" tabindex="9999"/></td>
                                                    <td class="dataEntryTable" rowspan="8" valign="top" align="center">
                                                        <textarea name="FTComments" wrap="hard" cols="24" style="height:180" tabindex="9999"><logic:present name="FTNoComments"><bean:write name="FTNoComments"/></logic:present></textarea>
                                                    </td>
                                                </tr>
                                                <tr class="dataEntryTable">
                                                    <td class="dataEntryTable"><bean:write name="FTNoDesc"/></td>   
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="FTNoLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="FTNoLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="FTNoLastData"/></td></tr>
                                                            <html:hidden property="value(FTNoLastDataEnteredDate)"/>
                                                            <html:hidden property="value(FTNoLastData)"/>
                                                        </table>
                                                        </logic:present>                                                
                                                        <logic:notPresent name="FTNoLastData">
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center">
                                                        <html:radio property="value(FTNoValue)" value="yes" />Yes
                                                        <html:radio property="value(FTNoValue)" value="no"/>No
                                                    </td>
                                                    <td class="dataEntryTable" rowspan="7" valign="top" align="center">&nbsp;</td>
                                                    <html:hidden property="value(FTNoDate)"/>
                                                    <html:hidden property="value(FTNoComments)"/>
                                                 </tr>
                                                 <tr class="dataEntryTable">
                                                    <td class="dataEntryTable"><bean:write name="FTNeDesc"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="FTNeLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="FTNeLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="FTNeLastData"/></td></tr>
                                                            <html:hidden property="value(FTNeLastDataEnteredDate)"/>
                                                            <html:hidden property="value(FTNeLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="FTNeLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center">
                                                        <html:radio property="value(FTNeValue)" value="yes" />Yes
                                                        <html:radio property="value(FTNeValue)" value="no" />No
                                                    </td>
                                                    <html:hidden property="value(FTNeDate)" />
                                                    <html:hidden property="value(FTNeComments)"/>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="FTIsDesc"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="FTIsLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="FTIsLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="FTIsLastData"/></td></tr>
                                                            <html:hidden property="value(FTIsLastDataEnteredDate)"/>
                                                            <html:hidden property="value(FTIsLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="FTIsLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center">
                                                        <html:radio property="value(FTIsValue)" value="yes" />Yes
                                                        <html:radio property="value(FTIsValue)" value="no" />No
                                                    </td>
                                                    <html:hidden property="value(FTIsDate)" />
                                                    <html:hidden property="value(FTIsComments)" />
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="FTUlDesc"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="FTUlLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="FTUlLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="FTUlLastData"/></td></tr>
                                                            <html:hidden property="value(FTUlLastDataEnteredDate)"/>
                                                            <html:hidden property="value(FTUlLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="FTUlLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center">
                                                        <html:radio property="value(FTUlValue)" value="yes" />Yes
                                                        <html:radio property="value(FTUlValue)" value="no" />No
                                                    </td>
                                                    <html:hidden property="value(FTUlDate)" />
                                                    <html:hidden property="value(FTUlComments)" />
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="FTInDesc"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="FTInLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="FTInLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="FTInLastData"/></td></tr>
                                                            <html:hidden property="value(FTInLastDataEnteredDate)"/>
                                                            <html:hidden property="value(FTInLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="FTInLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center">
                                                        <html:radio property="value(FTInValue)" value="yes" />Yes
                                                        <html:radio property="value(FTInValue)" value="no" />No
                                                    </td>
                                                    <html:hidden property="value(FTInDate)" />
                                                    <html:hidden property="value(FTInComments)" />
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="FTOtDesc"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="FTOtLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="FTOtLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="FTOtLastData"/></td></tr>
                                                            <html:hidden property="value(FTOtLastDataEnteredDate)"/>
                                                            <html:hidden property="value(FTOtLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="FTOtLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center">
                                                        <html:radio property="value(FTOtValue)" value="yes" />Yes
                                                        <html:radio property="value(FTOtValue)" value="no" />No
                                                    </td>
                                                    <html:hidden property="value(FTOtDate)" />
                                                    <html:hidden property="value(FTOtComments)" />
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="FTReDesc"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="FTReLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="FTReLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="FTReLastData"/></td></tr>
                                                            <html:hidden property="value(FTReLastDataEnteredDate)"/>
                                                            <html:hidden property="value(FTReLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="FTReLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>                                                  
                                                    <td class="dataEntryTable" align="center">
                                                        <html:radio property="value(FTReValue)" value="yes" />Yes
                                                        <html:radio property="value(FTReValue)" value="no" />No
                                                    </td>
                                                    <html:hidden property="value(FTReDate)" />
                                                    <html:hidden property="value(FTReComments)" />
                                                 </tr>
                                                 <tr class="dataEntryTable">
                                                    <th class="dataEntryTable" colspan="3">Eye Exam</th>                                                    
                                                    <td class="dataEntryTable" valign="top" align="center"><input type="text" name="iDate" value="<bean:write name="iNoDate"/>" size="10%"/></td>
                                                    <td class="dataEntryTable" rowspan="8" valign="top" align="center">
                                                        <textarea name="iComments" wrap="hard" cols="24" style="height:180"><logic:present name="iNoComments"><bean:write name="iNoComments"/></logic:present></textarea>
                                                    </td>
                                                </tr>
                                                <tr class="dataEntryTable">
                                                    <td class="dataEntryTable"><bean:write name="iNoDesc"/></td>   
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="iNoLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="iNoLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="iNoLastData"/></td></tr>
                                                            <html:hidden property="value(iNoLastDataEnteredDate)"/>
                                                            <html:hidden property="value(iNoLastData)"/>
                                                        </table>
                                                        </logic:present>                                                
                                                        <logic:notPresent name="iNoLastData">
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center">
                                                        <html:radio property="value(iNoValue)" value="yes"/>Yes
                                                        <html:radio property="value(iNoValue)" value="no"/>No
                                                    </td>
                                                    <td class="dataEntryTable" rowspan="7" valign="top" align="center">&nbsp;</td>
                                                    <html:hidden property="value(iNoDate)" />
                                                    <html:hidden property="value(iNoComments)" />
                                                 </tr>
                                                 <tr class="dataEntryTable">
                                                    <td class="dataEntryTable"><bean:write name="iHypDesc"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="iHypLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="iHypLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="iHypLastData"/></td></tr>
                                                            <html:hidden property="value(iHypLastDataEnteredDate)"/>
                                                            <html:hidden property="value(iHypLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="iHypLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center">
                                                        <html:radio property="value(iHypValue)" value="yes" />Yes
                                                        <html:radio property="value(iHypValue)" value="no" />No
                                                    </td>
                                                    <html:hidden property="value(iHypDate)" />
                                                    <html:hidden property="value(iHypComments)" />
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="iDiaDesc"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="iDiaLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="iDiaLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="iDiaLastData"/></td></tr>
                                                            <html:hidden property="value(iDiaLastDataEnteredDate)"/>
                                                            <html:hidden property="value(iDiaLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="FTIsLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center">
                                                        <html:radio property="value(iDiaValue)" value="yes" />Yes
                                                        <html:radio property="value(iDiaValue)" value="no" />No
                                                    </td>
                                                    <html:hidden property="value(iDiaDate)"/>
                                                    <html:hidden property="value(iDiaComments)"/>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="iOthDesc"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="iOthLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="iOthLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="iOthLastData"/></td></tr>
                                                            <html:hidden property="value(iOthLastDataEnteredDate)"/>
                                                            <html:hidden property="value(iOthLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="iOthLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center">
                                                        <html:radio property="value(iOthValue)" value="yes" />Yes
                                                        <html:radio property="value(iOthValue)" value="no" />No
                                                    </td>
                                                    <html:hidden property="value(iOthDate)"/>
                                                    <html:hidden property="value(iOthComments)"/>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="iRefDesc"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="iRefLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="iRefLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="iRefLastData"/></td></tr>
                                                            <html:hidden property="value(iRefLastDataEnteredDate)"/>
                                                            <html:hidden property="value(iRefLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="iRefLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center">
                                                        <html:radio property="value(iRefValue)" value="yes" />Yes
                                                        <html:radio property="value(iRefValue)" value="no" />No
                                                    </td>
                                                    <html:hidden property="value(iRefDate)"/>
                                                    <html:hidden property="value(iRefComments)"/>
                                                 </tr>                                                 
                                            </table></td>
                                        </tr>
                                        <tr>
                                            <th></th>
                                            <th class="title" widht="98%">
                                            <a href="javascript: showHideItem('labs');" >Labs >> </a>
                                            </th>
                                        </tr>
                                         <tr>
                                            <td></td>
                                            <td><table style="display:none" cellpadding='1' cellspacing='0' id="labs">
                                                <tr>
                                                    <td class="subTitle" width="34%">                                                        
                                                    </td>                                                    
                                                    <td class="subTitle" width="16%">
                                                        Last Data<br> Entered on
                                                    </td>
                                                    <td class="subTitle" width="5%">
                                                        New Data
                                                    </td>                                                    
                                                    <td class="subTitle" width="10%">
                                                        Ob. Date<br> (yyyy-MM-dd)
                                                    </td>
                                                    <td class="subTitle" width="35%">
                                                        <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingComments"/>
                                                    </td>                                                    
                                                </tr>
                                                <tr class="dataEntryTable">
                                                    <td class="dataEntryTable"><bean:write name="HbA1Desc"/><br><font class="eightyPercent"><bean:write name="HbA1MeasuringInstrc"/></font></td>   
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="HbA1LastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="HbA1LastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="HbA1LastData"/></td></tr>
                                                            <html:hidden property="value(HbA1LastDataEnteredDate)"/>
                                                            <html:hidden property="value(HbA1LastData)"/>
                                                        </table>
                                                        </logic:present>                                                
                                                        <logic:notPresent name="HBA1LastData">
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(HbA1Value)" size="4%" /></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(HbA1Date)" size="10%" tabindex="9999"/></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(HbA1Comments)" size="35%" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr class="dataEntryTable">
                                                    <td class="dataEntryTable"><bean:write name="BGDesc"/><br><font class="eightyPercent"><bean:write name="BGMeasuringInstrc"/></font></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="BGLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="BGLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="BGLastData"/></td></tr>
                                                            <html:hidden property="value(BGLastDataEnteredDate)"/>
                                                            <html:hidden property="value(BGLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="BGLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(BGValue)" size="4%" /></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(BGDate)" size="10%" tabindex="9999"/></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(BGComments)" size="35%" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="LDLDesc"/><br><font class="eightyPercent"><bean:write name="LDLMeasuringInstrc"/></font></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="LDLLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="LDLLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="LDLLastData"/></td></tr>
                                                            <html:hidden property="value(LDLLastDataEnteredDate)"/>
                                                            <html:hidden property="value(LDLLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="LDLLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(LDLValue)" size="4%" /></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(LDLDate)" size="10%" tabindex="9999"/></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(LDLComments)" size="35%" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="HDLDesc"/><br><font class="eightyPercent"><bean:write name="HDLMeasuringInstrc"/></font></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="HDLLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="HDLLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="HDLLastData"/></td></tr>
                                                            <html:hidden property="value(HDLLastDataEnteredDate)"/>
                                                            <html:hidden property="value(HDLLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="HDLLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(HDLValue)" size="4%" /></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(HDLDate)" size="10%" tabindex="9999"/></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(HDLComments)" size="35%" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="TCHLDesc"/><br><font class="eightyPercent"><bean:write name="TCHLMeasuringInstrc"/></font></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="TCHLLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="TCHLLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="TCHLLastData"/></td></tr>
                                                            <html:hidden property="value(TCHLLastDataEnteredDate)"/>
                                                            <html:hidden property="value(TCHLLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="TCHLLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(TCHLValue)" size="4%" /></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(TCHLDate)" size="10%" tabindex="9999"/></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(TCHLComments)" size="35%" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="TRIGDesc"/><br><font class="eightyPercent"><bean:write name="TRIGMeasuringInstrc"/></font></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="TRIGLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="TRIGLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="TRIGLastData"/></td></tr>
                                                            <html:hidden property="value(TRIGLastDataEnteredDate)"/>
                                                            <html:hidden property="value(TRIGLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="TRIGLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(TRIGValue)" size="4%" /></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(TRIGDate)" size="10%" tabindex="9999"/></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(TRIGComments)" size="35%" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="UALBDesc"/><br><font class="eightyPercent"><bean:write name="UALBMeasuringInstrc"/></font></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="UALBLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="UALBLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="UALBLastData"/></td></tr>
                                                            <html:hidden property="value(UALBLastDataEnteredDate)"/>
                                                            <html:hidden property="value(UALBLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="UALBLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(UALBValue)" size="4%" /></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(UALBDate)" size="10%" tabindex="9999"/></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(UALBComments)" size="35%" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="24UADesc"/><br><font class="eightyPercent"><bean:write name="24UAMeasuringInstrc"/></font></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="24UALastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="24UALastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="24UALastData"/></td></tr>
                                                            <html:hidden property="value(24UALastDataEnteredDate)"/>
                                                            <html:hidden property="value(24UALastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="24UALastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(24UAValue)" size="4%" /></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(24UADate)" size="10%" tabindex="9999"/></td>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(24UAComments)" size="35%" tabindex="9999"/></td>
                                                 </tr>
                                            </table></td>
                                        </tr>
                                        <tr>
                                            <th class="soap" width="2%">A</th>
                                            <th></th>
                                        </tr>
                                        <tr>
                                            <th></th>
                                            <th class="title">
                                            <a href="javascript: showHideItem('diagnosis');">Diagnosis >> </a>
                                            </th>
                                        </tr>
                                        <tr>
                                            <td></td>
                                            <td>
                                                <table style="display:none" cellpadding='1' cellspacing='0' id="diagnosis">
                                                <tr>
                                                    <td>
                                                        <html:textarea property="value(diagnosisVT)" cols="80" style="height:80"></html:textarea>
                                                    </td>
                                                </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th class="soap" width="2%">P</th>
                                            <th></th>
                                        </tr>
                                        <tr>
                                            <th></th>
                                            <th class="title" width="98%">
                                            <a href="javascript: showHideItem('medications');">Medications >> </a>
                                            </th>
                                        </tr>
                                         <tr>
                                            <td></td>
                                            <td><table style="display:none" cellpadding='1' cellspacing='0' id="medications">
                                            <tr>
                                                <td>
                                                    <logic:present name="drugs">
                                                        <logic:iterate id="drg" name="drugs">
                                                            <bean:write name="drg"/><br>
                                                        </logic:iterate>
                                                    </logic:present>
                                                    <logic:notPresent name="drugs">
                                                        No Drug in the current drug profile
                                                    </logic:notPresent>
                                                </td>
                                            </tr></table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th></th>
                                            <th class="title">
                                            <a href="javascript: showHideItem('counselling');" >Counselling >></a>
                                            </th>
                                        </tr>
                                         <tr>
                                            <td></td>
                                            <td><table style="display:none" cellpadding='1' cellspacing='0' id="counselling">
                                                <tr>
                                                    <td class="subTitle" width="29%">                                                        
                                                    </td>                                                    
                                                    <td class="subTitle" width="16%">
                                                        Last Data<br> Entered on
                                                    </td>
                                                    <td class="subTitle" width="10%">                                                        
                                                        &nbsp;
                                                    </td>                                                                                                        
                                                    <td class="subTitle" width="45%">
                                                        <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingComments"/>
                                                    </td>                                                    
                                                </tr>
                                                <tr class="dataEntryTable">
                                                    <td class="dataEntryTable"><bean:write name="NtrCDesc"/></td>   
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="NtrCLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="NtrCLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="NtrCLastData"/></td></tr>                                                            
                                                            <html:hidden property="value(NtrCLastDataEnteredDate)"/>
                                                            <html:hidden property="value(NtrCLastData)"/>
                                                        </table>
                                                        </logic:present>                                                        
                                                        <logic:notPresent name="NtrCLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:checkbox property="value(NtrCValue)" /></td>
                                                    <html:hidden property="value(NtrCDate)"/>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(NtrCComments)" size="45" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr class="dataEntryTable">
                                                    <td class="dataEntryTable"><bean:write name="ExeCDesc"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="ExeCLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="ExeCLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="ExeCLastData"/></td></tr>                                                           
                                                            <html:hidden property="value(ExeCLastDataEnteredDate)"/>
                                                            <html:hidden property="value(ExeCLastData)"/>                                                            
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="ExeCLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:checkbox property="value(ExeCValue)" /></td>
                                                    <html:hidden property="value(ExeCDate)"/>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(ExeCComments)" size="45" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="SmCCDesc"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="SmCCLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="SmCCLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="SmCCLastData"/></td></tr>                                                           
                                                            <html:hidden property="value(SmCCLastDataEnteredDate)"/>
                                                            <html:hidden property="value(SmCCLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="SmCCLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:checkbox property="value(SmCCValue)" /></td>
                                                    <html:hidden property="value(SmCCDate)"/>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(SmCCComments)" size="45"tabindex="9999"/></td>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="DiaCDesc"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="DiaCLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="DiaCLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="DiaCLastData"/></td></tr>                                                           
                                                            <html:hidden property="value(DiaCLastDataEnteredDate)"/>
                                                            <html:hidden property="value(DiaCLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="DiaCLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:checkbox property="value(DiaCValue)" /></td>
                                                    <html:hidden property="value(DiaCDate)"/>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(DiaCComments)" size="45" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="PsyCDesc"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="PsyCLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="PsyCLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="PsyCLastData"/></td></tr>                                                           
                                                            <html:hidden property="value(PsyCLastDataEnteredDate)"/>
                                                            <html:hidden property="value(PsyCLastData)"/>
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="PsyCLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:checkbox property="value(PsyCValue)" /></td>
                                                    <html:hidden property="value(PsyCDate)"/>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(PsyCComments)" size="45" tabindex="9999"/></td>
                                                 </tr>
                                                 <tr>
                                                    <td class="dataEntryTable"><bean:write name="OthCDesc"/></td>
                                                    <td class="dataEntryTable" align="center">
                                                        <logic:present name="OthCLastData">
                                                        <table cellpadding='0' cellspacing='0'>                                                            
                                                            <tr><td class="eightyPercent" align="left"><bean:write name="OthCLastDataEnteredDate"/></td></tr>
                                                            <tr><td class="eightyPercent" align="right"><bean:write name="OthCLastData"/></td></tr>                                                           
                                                            <html:hidden property="value(OthCLastDataEnteredDate)"/>
                                                            <html:hidden property="value(OthCLastData)"/>                                                            
                                                        </table>
                                                        </logic:present>
                                                        <logic:notPresent name="OthCLastData">    
                                                            &nbsp;
                                                        </logic:notPresent>
                                                    </td>
                                                    <td class="dataEntryTable" align="center"><html:checkbox property="value(OthCValue)" /></td>
                                                    <html:hidden property="value(OthCDate)"/>
                                                    <td class="dataEntryTable" align="center"><html:text property="value(OthCComments)" size="45" tabindex="9999"/></td>
                                                 </tr>
                                            </table></td>
                                        </tr>                                                                                
                                </td>   
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">

            </td>
            <td class="MainTableBottomRowRightColumn">

            </td>
        </tr>
    </table>
    <table class="Head" class="hidePrint" width="100%">
        <tr>
            <td align="left">
                <input type="hidden" name="submit" value=""/>
                <input type="submit" value="Save" onclick="javascript:return onSave();" />
                <input type="submit" value="Save and Exit" onclick="javascript:return onSaveExit();"/>
                <input type="submit" value="Exit" onclick="javascript:return onExit();"/>
                <input type="button" value="Print" onclick="javascript:return onPrint();"/>
            </td>
        </tr>
    </table>
</html:form>
</body>
</html:html>

