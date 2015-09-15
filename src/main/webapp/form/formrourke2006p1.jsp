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
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
    String formClass = "Rourke2006";
    String formLink = "formrourke2006p1.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request) ,demoNo, formId);
    String []growthCharts = new String[2];
    
    if( ((FrmRourke2006Record)rec).isFemale(demoNo) ) {
        growthCharts[0] = new String("GrowthChartRourke2006Girls&__cfgGraphicFile=GrowthChartRourke2006GirlGraphic&__cfgGraphicFile=GrowthChartRourke2006GirlGraphic2&__template=GrowthChartRourke2006Girls");
        growthCharts[1] = new String("GrowthChartRourke2006Girls2&__cfgGraphicFile=GrowthChartRourke2006GirlGraphic3&__cfgGraphicFile=GrowthChartRourke2006GirlGraphic4&__template=GrowthChartRourke2006Girlspg2");
    }
    else {
        growthCharts[0] = new String("GrowthChartRourke2006Boys&__cfgGraphicFile=GrowthChartRourke2006BoyGraphic&__cfgGraphicFile=GrowthChartRourke2006BoyGraphic2&__template=GrowthChartRourke2006Boys");
        growthCharts[1] = new String("GrowthChartRourke2006Boys2&__cfgGraphicFile=GrowthChartRourke2006BoyGraphic3&__cfgGraphicFile=GrowthChartRourke2006BoyGraphic4&__template=GrowthChartRourke2006Boyspg2");
    }

    FrmData fd = new FrmData();
    String resource = fd.getResource();    
    props.setProperty("c_lastVisited", "p1");

    //get project_home
    String project_home = request.getContextPath().substring(1);
    
%>
<%
  boolean bView = false;
  if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Rourke2006 Record 1</title>
<link rel="stylesheet" type="text/css" href="rourkeStyle.css">
<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />

<!-- main calendar program -->
<script type="text/javascript" src="../share/calendar/calendar.js"></script>

<!-- language for the calendar -->
<script type="text/javascript"
	src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>

<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>

<!-- popup mouseover js code -->
<script type="text/javascript" src="../share/javascript/mouseover.js"></script>

<!--Text Area text max limit code -->
<script type="text/javascript"
	src="../share/javascript/txtCounter/x_core.js"></script>
<script type="text/javascript"
	src="../share/javascript/txtCounter/x_dom.js"></script>
<script type="text/javascript"
	src="../share/javascript/txtCounter/x_event.js"></script>
<script type="text/javascript"
	src="../share/javascript/txtCounter/ylib.js"></script>
<script type="text/javascript"
	src="../share/javascript/txtCounter/y_TextCounter.js"></script>
<script type="text/javascript"
	src="../share/javascript/txtCounter/y_util.js"></script>
<html:base />
</head>

<script type="text/javascript" language="Javascript">
    //constrain text to number of rows and columns   
    window.onload = function() {
    
        var txtBirthRem = new ylib.widget.TextCounter("c_birthRemarks", 84,3);
        var txtriskFact = new ylib.widget.TextCounter("c_riskFactors", 90,3);
        var txtConcern1w = new ylib.widget.TextCounter("p1_pConcern1w", 200,5);
        var txtConcern2w = new ylib.widget.TextCounter("p1_pConcern2w", 220,5);
        var txtConcern1m = new ylib.widget.TextCounter("p1_pConcern1m", 230,5);
        var txtDevelopment1w = new ylib.widget.TextCounter("p1_development1w", 360,9);
        var txtDevelopment2w = new ylib.widget.TextCounter("p1_development2w", 396,9);
        var txtDevelopment1m = new ylib.widget.TextCounter("p1_development1m", 230,5);        
        var txtProblems1w = new ylib.widget.TextCounter("p1_problems1w", 120,3);
        var txtProblems2w = new ylib.widget.TextCounter("p1_problems2w", 220,5);
        var txtProblems1m = new ylib.widget.TextCounter("p1_problems1m", 230,5);        
        
        var updated = "<%=props.getProperty("updated","")%>";
        if( updated == "true" ) {
            alert("Synchronizing demographic information\nRemember to save changes");
        }
    }    
    
    function showNotes() {
        var frm = document.getElementById("frmPopUp");
        frm.action = "Rourke2006_Notes.pdf";
        frm.submit();
    }
    
    function del( names ) {        
        var arrElem = names.split(",");
        var elem;
    
        for( var idx = 0; idx < arrElem.length; ++idx ) {
            elem = document.getElementById(arrElem[idx]);
            elem.checked = false;
        }
    
    }
    
    function onCheck(a, groupName) {
        if (a.checked) {
		var s = groupName;
		unCheck(s);
		a.checked = true;                
        }
    }

    function unCheck(s) {   
        for (var i =0; i <document.forms[0].elements.length; i++) {
            if (document.forms[0].elements[i].id.indexOf(s) != -1 && document.forms[0].elements[i].id.indexOf(s) < 1) {
                document.forms[0].elements[i].checked = false;                
            }
        }
    }
    
    function reset() {        
        document.forms[0].target = "";
        document.forms[0].action = "/<%=project_home%>/form/formname.do" ;
    }
    
    function onGraph(url) {        
    
        if( checkMeasures() ) {
            document.forms["graph"].action = url;
            document.forms["graph"].submit();
        }       
            
    }
    
    function onPrint() {
        document.forms[0].submit.value="print"; 
                
        document.forms[0].action = "../form/createpdf?__title=Rourke+Baby+Report+Pg1&__cfgfile=rourke2006printCfgPg1&__template=rourke2006p1";
        document.forms[0].target="_blank";            
        
        return true;
    }
    function onPrintAll() {
        document.forms[0].submit.value="printAll"; 
                
        document.forms[0].action = "../form/formname.do?__title=Rourke+Baby+Report&__cfgfile=rourke2006printCfgPg1&__cfgfile=rourke2006printCfgPg2&__cfgfile=rourke2006printCfgPg3&__cfgfile=rourke2006printCfgPg4&__template=rourke2006";
        document.forms[0].target="_blank";            
        
        return true;
    }
    
    function $F(elem) {
        return document.forms[0].elements[elem].value;
    
    }
    
    /*We have to check that measurements are numeric and an observation date has 
     *been entered for that measurment
     */
    function checkMeasures() {
        var measurements = new Array(3);
        measurements[0] = new Array("p1_ht1w", "p1_wt1w", "p1_hc1w");
        measurements[1] = new Array("p1_ht2w", "p1_wt2w", "p1_hc2w");
        measurements[2] = new Array("p1_ht1m", "p1_wt1m", "p1_hc1m");
        var dates = new Array("p1_date1w", "p1_date2w", "p1_date1m");
                    
        for( var dateIdx = 0; dateIdx < dates.length; ++dateIdx ) {
            var date = dates[dateIdx];
            for( var elemIdx = 0; elemIdx < measurements[dateIdx].length; ++elemIdx ) {
                var elem = measurements[dateIdx][elemIdx];                
                if( $F(elem).length > 0 && (isNaN($F(elem)) || $F(date).length == 0 )) {
                    alert('<bean:message key="oscarEncounter.formRourke2006.frmError"/>');
                    return false;
                }
            }
           
        }
            
        return true;   
    }
    
    function onSave() {
    
        if( checkMeasures() ) {
            document.forms[0].submit.value="save";                
            reset();                
            return confirm("Are you sure you want to save this form?");
        }
        
        return false;  
    }
    
    function onSaveExit() {
    
        if( checkMeasures() ) {
            document.forms[0].submit.value="exit";
            reset();
            return confirm("Are you sure you wish to save and close this window?");
        }
        
        return false;
     }
    
    function popPage(varpage,pageName) {
        windowprops = "height=700,width=960"+
            ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=50,screenY=50,top=20,left=20";
        var popup = window.open(varpage,pageName, windowprops);
        //if (popup.opener == null) {
        //    popup.opener = self;
        //}
        popup.focus();
    }
    
    
    function isNumber(ss) {
		var s = ss.value;
        var i;
        for (i = 0; i < s.length; i++){
            // Check that current character is number.
            var c = s.charAt(i)
			if (c == '.') {
				continue;
			} else if (((c < "0") || (c > "9"))) {
                            alert('Invalid '+s+' in field ' + ss.name);
                            ss.focus();
                            return false;
			}
        }
        // All characters are numbers.
        return true;
    }
    
    function wtEnglish2Metric(obj) {
	//if(isNumber(document.forms[0].c_ppWt) ) {
	//	weight = document.forms[0].c_ppWt.value;
	if(isNumber(obj) ) {
		weight = obj.value;
		weightM = Math.round(weight * 10 * 0.4536) / 10 ;
		if(confirm("Are you sure you want to change " + weight + " pounds to " + weightM +"kg?") ) {
			//document.forms[0].c_ppWt.value = weightM;
			obj.value = weightM;
		}
	}
    }
    
    function htEnglish2Metric(obj) {
	height = obj.value;
	if(height.length > 1 && height.indexOf("'") > 0 ) {
		feet = height.substring(0, height.indexOf("'"));
		inch = height.substring(height.indexOf("'"));
		if(inch.length == 1) {
			inch = 0;
		} else {
			inch = inch.charAt(inch.length-1)=='"' ? inch.substring(0, inch.length-1) : inch;
			inch = inch.substring(1);
		}
		
		//if(isNumber(feet) && isNumber(inch) )
			height = Math.round((feet * 30.48 + inch * 2.54) * 10) / 10 ;
			if(confirm("Are you sure you want to change " + feet + " feet " + inch + " inch(es) to " + height +"cm?") ) {
				obj.value = height;
			}
		//}
	}
    }
    
    //Remove date from textbox
    function resetDate(textbox) {
        if( textbox.value.length > 0 )
            textbox.value = "";
        else {
            var date = new Date();
            var mth = date.getMonth() + 1;
            textbox.value = date.getDate() + "/" + mth + "/" + date.getFullYear();
        }
    }
    
    

</script>

<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
<div id="object1"
	style="position: absolute; background-color: FFFFDD; color: black; border-color: black; border-width: 20; left: 25px; top: -100px; z-index: +1"
	onmouseover="overdiv=1;"
	onmouseout="overdiv=0; setTimeout('hideLayer()',1000)">pop up
description layer</div>
<html:form action="/form/formname">

	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="ID"
		value="<%= props.getProperty("ID", "0") %>" />
	<input type="hidden" name="provider_no"
		value=<%=request.getParameter("provNo")%> />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="formId" value="<%=formId%>" />
	<input type="hidden" name="c_lastVisited"
		value=<%=props.getProperty("c_lastVisited", "p1")%> />
	<input type="hidden" name="submit" value="exit" />

	<table cellpadding="0" cellspacing="0" class="Header" class="hidePrint">
		<tr>
			<td nowrap="true"><input type="submit"
				value="<bean:message key="oscarEncounter.formRourke1.btnSave"/>"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke1.btnSaveExit"/>"
				onclick="javascript:return onSaveExit();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke1.btnExit"/>"
				onclick="javascript:return onExit();"> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke1.btnPrint"/>"
				onclick="javascript:return onPrint();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke2006.btnPrintAll"/>"
				onclick="javascript:return onPrintAll();" /> <input type="button"
				value="About"
				onclick="javascript:return popPage('formRourke2006intro.html','About Rourke');" />
			</td>
			<td align="center" width="100%">
			<% if(formId > 0)
           { %> <a name="length" href="#"
				onclick="onGraph('<%=request.getContextPath()%>/form/formname.do?submit=graph&form_class=Rourke2006&__title=Baby+Growth+Graph1&__cfgfile=<%=growthCharts[0]%>&demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');return false;">
			<bean:message key="oscarEncounter.formRourke1.btnGraphLenghtWeight" /></a><br>
			<a name="headCirc" href="#"
				onclick="onGraph('<%=request.getContextPath()%>/form/formname.do?submit=graph&form_class=Rourke2006&__title=Baby+Head+Circumference&__cfgfile=<%=growthCharts[1]%>&demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');return false;">
			<bean:message key="oscarEncounter.formRourke1.btnGraphHead" /></a> <% }else { %>
			&nbsp; <% } %>
			</td>
			<td nowrap="true"><a><bean:message
				key="oscarEncounter.formRourke2006.Pg1" /></a>&nbsp;|&nbsp; <a
				href="formrourke2006p2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg2" /></a>&nbsp;|&nbsp; <a
				href="formrourke2006p3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg3" /></a>&nbsp;|&nbsp; <a
				href="formrourke2006p4.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg4" /></a></td>
		</tr>
	</table>

	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr class="titleBar">
			<th><bean:message
				key="oscarEncounter.formRourke1.msgRourkeBabyRecord" /></th>
		</tr>
	</table>

	<table cellpadding="0" cellspacing="0" width="100%" border="0">
		<tr valign="top">
			<td align="center"><bean:message
				key="oscarEncounter.formRourke1.formBirhtRemarks" /><br>
			<textarea wrap="physical" id="c_birthRemarks" name="c_birthRemarks"
				rows="2" cols="17"><%= props.getProperty("c_birthRemarks", "") %></textarea>
			</td>
			<td nowrap align="center"><bean:message
				key="oscarEncounter.formRourke1.formRiksFactors" /><br>
			<textarea id="c_riskFactors" name="c_riskFactors" rows="2" cols="17"><%= props.getProperty("c_riskFactors", "") %></textarea>
			</td>
			<td width="65%" nowrap align="center">
			<p><bean:message key="oscarEncounter.formRourke1.msgName" />: <input
				type="text" name="c_pName" maxlength="60" size="30"
				value="<%= props.getProperty("c_pName", "") %>" readonly="true" />
			&nbsp;&nbsp; <bean:message
				key="oscarEncounter.formRourke1.msgBirthDate" /> (d/m/yyyy): <input
				type="text" name="c_birthDate" size="10" maxlength="10"
				value="<%= props.getProperty("c_birthDate", "") %>" readonly="true">
			&nbsp;&nbsp; <% if(! ((FrmRourke2006Record)rec).isFemale(demoNo))
                {
                    %><bean:message
				key="oscarEncounter.formRourke1.msgMale" /> <input type="hidden"
				name="c_male" value="x"> <%
                }else
                {
                    %><bean:message
				key="oscarEncounter.formRourke1.msgFemale" /> <input type="hidden"
				name="c_female" value="x"> <%
                }
                %>
			</p>
			<p><bean:message key="oscarEncounter.formRourke1.msgLenght" />: <input
				type="text" ondblclick="htEnglish2Metric(this);" name="c_length"
				size="6" maxlength="6"
				value="<%= props.getProperty("c_length", "") %>" /> <bean:message
				key="oscarEncounter.formRourke1.msgLenghtUnit" /> &nbsp;&nbsp; <bean:message
				key="oscarEncounter.formRourke1.msgHeadCirc" />: <input type="text"
				ondblclick="htEnglish2Metric(this);" name="c_headCirc" size="6"
				maxlength="6" value="<%= props.getProperty("c_headCirc", "") %>" />
			<bean:message key="oscarEncounter.formRourke1.msgHeadCircUnit" />
			&nbsp;&nbsp; <bean:message
				key="oscarEncounter.formRourke1.msgBirthWt" />: <input type="text"
				ondblclick="wtEnglish2Metric(this);" name="c_birthWeight" size="6"
				maxlength="7" value="<%= props.getProperty("c_birthWeight", "") %>" />
			<bean:message key="oscarEncounter.formRourke1.msgBirthWtUnit" />
			&nbsp;&nbsp; <bean:message
				key="oscarEncounter.formRourke1.msgDischargeWt" />: <input
				type="text" ondblclick="wtEnglish2Metric(this);"
				name="c_dischargeWeight" size="6" maxlength="7"
				value="<%= props.getProperty("c_dischargeWeight", "") %>"> <bean:message
				key="oscarEncounter.formRourke1.msgDischargeWtUnit" /></p>
			</td>
		</tr>
	</table>

	<table cellpadding="0" cellspacing="0" width="100%" border="1">
		<tr align="center">
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke2006_1.visitDate" /></a></td>
			<td colspan="3" class="row"><bean:message
				key="oscarEncounter.formRourke1.msgWithin" /> <a><bean:message
				key="oscarEncounter.formRourke1.btn1Week" /></a></td>
			<td colspan="3" class="row"><a><bean:message
				key="oscarEncounter.formRourke1.btn2Weeks" /></a> <bean:message
				key="oscarEncounter.formRourke1.msgOptional" /></td>
			<td colspan="3" class="row"><a><bean:message
				key="oscarEncounter.formRourke1.btn1month" /></a> <bean:message
				key="oscarEncounter.formRourke1.msgOptional" /></td>
		</tr>
		<tr align="center">
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgDate" /></a></td>
			<td colspan="3"><input readonly type="text" id="p1_date1w"
				name="p1_date1w" size="10" ondblclick="resetDate(this)"
				value="<%=UtilMisc.htmlEscape(props.getProperty("p1_date1w", ""))%>" />
			<img src="../images/cal.gif" id="p1_date1w_cal"></td>
			<td colspan="3"><input readonly type="text" id="p1_date2w"
				name="p1_date2w" size="10" ondblclick="resetDate(this)"
				value="<%=UtilMisc.htmlEscape(props.getProperty("p1_date2w", ""))%>" />
			<img src="../images/cal.gif" id="p1_date2w_cal"></td>
			<td colspan="3"><input readonly type="text" id="p1_date1m"
				name="p1_date1m" size="10" ondblclick="resetDate(this)"
				value="<%=UtilMisc.htmlEscape(props.getProperty("p1_date1m", ""))%>" />
			<img src="../images/cal.gif" id="p1_date1m_cal"></td>
		</tr>
		<tr align="center">
			<td class="column" rowspan="2"><a><bean:message
				key="oscarEncounter.formRourke1.btnGrowth" />*<br />
			<bean:message key="oscarEncounter.formRourke2006_1.btnGrowthmsg" /></a></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formWt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHdCirc" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formWt" /></td>
			<td><bean:message
				key="oscarEncounter.formRourke2006_3.formHdCirc" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formWt" /></td>
			<td><bean:message
				key="oscarEncounter.formRourke2006_3.formHdCirc" /></td>
		</tr>
		<tr align="center">
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p1_ht1w" size="4"
				maxlength="5" value="<%= props.getProperty("p1_ht1w", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="wtEnglish2Metric(this);" name="p1_wt1w" size="4"
				maxlength="5" value="<%= props.getProperty("p1_wt1w", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p1_hc1w" size="4"
				maxlength="5" value="<%= props.getProperty("p1_hc1w", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p1_ht2w" size="4"
				maxlength="5" value="<%= props.getProperty("p1_ht2w", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="wtEnglish2Metric(this);" name="p1_wt2w" size="4"
				maxlength="5" value="<%= props.getProperty("p1_wt2w", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p1_hc2w" size="4"
				maxlength="5" value="<%= props.getProperty("p1_hc2w", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p1_ht1m" size="4"
				maxlength="5" value="<%= props.getProperty("p1_ht1m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="wtEnglish2Metric(this);" name="p1_wt1m" size="4"
				maxlength="5" value="<%= props.getProperty("p1_wt1m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p1_hc1m" size="4"
				maxlength="5" value="<%= props.getProperty("p1_hc1m", "") %>"></td>
		</tr>
		<tr align="center">
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.formParentalConcerns" /></a></td>
			<td colspan="3"><textarea id="p1_pConcern1w"
				name="p1_pConcern1w" style="width: 100%" cols="10" rows="5"><%= props.getProperty("p1_pConcern1w", "") %></textarea>
			</td>
			<td colspan="3"><textarea wrap="physical" id="p1_pConcern2w"
				name="p1_pConcern2w" style="width: 100%" cols="10" rows="5"><%= props.getProperty("p1_pConcern2w", "") %></textarea>
			</td>
			<td colspan="3"><textarea id="p1_pConcern1m"
				name="p1_pConcern1m" style="width: 100%" cols="10" rows="5"><%= props.getProperty("p1_pConcern1m", "") %></textarea>
			</td>
		</tr>
		<tr align="center">

			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgNutrition" />:</a></td>

			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_breastFeeding1w"
						<%= props.getProperty("p1_breastFeeding1w", "") %> /></td>
					<td><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.btnBreastFeeding" /><br />
					</a><span
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.msgBreastFeedingDescr" /></span></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_formulaFeeding1w"
						<%= props.getProperty("p1_formulaFeeding1w", "") %> /></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_1.msgFormulaFeeding" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_stoolUrine1w"
						<%= props.getProperty("p1_stoolUrine1w", "") %> /></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formStoolPatern" /></td>
				</tr>
			</table>

			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_breastFeeding2w"
						<%= props.getProperty("p1_breastFeeding2w", "") %>></td>
					<td><b><a
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()" href="javascript:showNotes()"><bean:message
						key="oscarEncounter.formRourke2006_1.btnBreastFeeding" /></a><br />
					<span
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.msgBreastFeedingDescr" /></span></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_formulaFeeding2w"
						<%= props.getProperty("p1_formulaFeeding2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_1.msgFormulaFeeding" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_stoolUrine2w"
						<%= props.getProperty("p1_stoolUrine2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formStoolPatern" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%" height="100%">
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_breastFeeding1m"
						<%= props.getProperty("p1_breastFeeding1m", "") %>></td>
					<td><b><a
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()" href="javascript:showNotes()"><bean:message
						key="oscarEncounter.formRourke2006_1.btnBreastFeeding" /></a><br />
					<span
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.msgBreastFeedingDescr" /></span></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_formulaFeeding1m"
						<%= props.getProperty("p1_formulaFeeding1m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_1.msgFormulaFeedingShort" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_stoolUrine1m"
						<%= props.getProperty("p1_stoolUrine1m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formStoolPatern" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgEducational" /></a><br />
			<br />
			<img height="15" width="20" src="graphics/Checkmark_Lwhite.gif"><bean:message
				key="oscarEncounter.formRourke2006.msgEducationalLegend" /></td>
			<td colspan="9" valign="top">
			<table style="font-size: 9pt;" cellpadding="0" cellspacing="0"
				width="100%">
				<tr>
					<td colspan="12">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="12"><bean:message
						key="oscarEncounter.formRourke2006_1.formInjuryPrev" /></td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td>&nbsp;</td>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td>&nbsp;</td>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td>&nbsp;</td>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_carSeatOk"
						name="p1_carSeatOk" onclick="onCheck(this,'p1_carSeat')"
						<%= props.getProperty("p1_carSeatOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_carSeatNo"
						name="p1_carSeatNo" onclick="onCheck(this,'p1_carSeat')"
						<%= props.getProperty("p1_carSeatNo", "") %>></td>
					<td valign="top"><b><a
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()" href="javascript:showNotes()"><bean:message
						key="oscarEncounter.formRourke1.formCarSeat" /></a>*</b></td>
					<td valign="top"><input type="radio" id="p1_sleepPosOk"
						name="p1_sleepPosOk" onclick="onCheck(this,'p1_sleepPos')"
						<%= props.getProperty("p1_sleepPosOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_sleepPosNo"
						name="p1_sleepPosNo" onclick="onCheck(this,'p1_sleepPos')"
						<%= props.getProperty("p1_sleepPosNo", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSleepPos" /></a></b></td>
					<td valign="top"><input type="radio" id="p1_cribSafetyOk"
						name="p1_cribSafetyOk" onclick="onCheck(this,'p1_cribSafety')"
						<%= props.getProperty("p1_cribSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_cribSafetyNo"
						name="p1_cribSafetyNo" onclick="onCheck(this,'p1_cribSafety')"
						<%= props.getProperty("p1_cribSafetyNo", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.formCribSafety" />*</a></b></td>
					<td valign="top"><input type="radio" id="p1_firearmSafetyOk"
						name="p1_firearmSafetyOk"
						onclick="onCheck(this,'p1_firearmSafety')"
						<%= props.getProperty("p1_firearmSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_firearmSafetyNo"
						name="p1_firearmSafetyNo"
						onclick="onCheck(this,'p1_firearmSafety')"
						<%= props.getProperty("p1_firearmSafetyNo", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formFireArm" />*</a></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_smokeSafetyOk"
						name="p1_smokeSafetyOk" onclick="onCheck(this,'p1_smokeSafety')"
						<%= props.getProperty("p1_smokeSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_smokeSafetyNo"
						name="p1_smokeSafetyNo" onclick="onCheck(this,'p1_smokeSafety')"
						<%= props.getProperty("p1_smokeSafetyNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSmokeSafety" />*</a></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p1_hotWaterOk"
						name="p1_hotWaterOk" onclick="onCheck(this,'p1_hotWater')"
						<%= props.getProperty("p1_hotWaterOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_hotWaterNo"
						name="p1_hotWaterNo" onclick="onCheck(this,'p1_hotWater')"
						<%= props.getProperty("p1_hotWaterNo", "") %>></td>
					<td valign="top"><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formHotWater" />*</a></i></td>
					<td valign="top"><input type="radio" id="p1_safeToysOk"
						name="p1_safeToysOk" onclick="onCheck(this,'p1_safeToys')"
						<%= props.getProperty("p1_safeToysOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_safeToysNo"
						name="p1_safeToysNo" onclick="onCheck(this,'p1_safeToys')"
						<%= props.getProperty("p1_safeToysNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSafeToys" />*</a></td>
				</tr>

				<tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_carSeatOk,p1_carSeatNo,p1_smokeSafetyOk,p1_smokeSafetyNo');" /></td>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_sleepPosOk,p1_sleepPosNo');" /></td>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_cribSafetyOk,p1_cribSafetyNo,p1_hotWaterOk,p1_hotWaterNo');" /></td>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_firearmSafetyOk,p1_firearmSafetyNo,p1_safeToysOk,p1_safeToysNo');" /></td>
				</tr>
				<!-- </table>
          <br/>
          <table style="font-size:9pt;" cellpadding="0" cellspacing="0" width="100%"  >                    
          -->
				<tr>
					<td colspan="12">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="12"><bean:message
						key="oscarEncounter.formRourke2006_1.formBehaviour" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_sleepCryOk"
						name="p1_sleepCryOk" onclick="onCheck(this,'p1_sleepCry')"
						<%= props.getProperty("p1_sleepCryOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_sleepCryNo"
						name="p1_sleepCryNo" onclick="onCheck(this,'p1_sleepCry')"
						<%= props.getProperty("p1_sleepCryNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formsleepCry" />**</a></td>
					<td valign="top"><input type="radio" id="p1_soothabilityOk"
						name="p1_soothabilityOk" onclick="onCheck(this,'p1_soothability')"
						<%= props.getProperty("p1_soothabilityOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_soothabilityNo"
						name="p1_soothabilityNo" onclick="onCheck(this,'p1_soothability')"
						<%= props.getProperty("p1_soothabilityNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formSoothability" /></td>
					<td valign="top"><input type="radio" id="p1_homeVisitOk"
						name="p1_homeVisitOk" onclick="onCheck(this,'p1_homeVisit')"
						<%= props.getProperty("p1_homeVisitOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_homeVisitNo"
						name="p1_homeVisitNo" onclick="onCheck(this,'p1_homeVisit')"
						<%= props.getProperty("p1_homeVisitNo", "") %>></td>
					<td colspan="4" valign="top"><b><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formHomeVisit" />**</a></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_bondingOk"
						name="p1_bondingOk" onclick="onCheck(this,'p1_bonding')"
						<%= props.getProperty("p1_bondingOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_bondingNo"
						name="p1_bondingNo" onclick="onCheck(this,'p1_bonding')"
						<%= props.getProperty("p1_bondingNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formBonding" /></td>
					<td valign="top"><input type="radio" id="p1_pFatigueOk"
						name="p1_pFatigueOk" onclick="onCheck(this,'p1_pFatigue')"
						<%= props.getProperty("p1_pFatigueOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_pFatigueNo"
						name="p1_pFatigueNo" onclick="onCheck(this,'p1_pFatigue')"
						<%= props.getProperty("p1_pFatigueNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formParentFatigue" />**</a></td>
					<td valign="top"><input type="radio" id="p1_famConflictOk"
						name="p1_famConflictOk" onclick="onCheck(this,'p1_famConflict')"
						<%= props.getProperty("p1_famConflictOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_famConflictNo"
						name="p1_famConflictNo" onclick="onCheck(this,'p1_famConflict')"
						<%= props.getProperty("p1_famConflictNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formFamConflict" /></td>
					<td valign="top"><input type="radio" id="p1_siblingsOk"
						name="p1_siblingsOk" onclick="onCheck(this,'p1_siblings')"
						<%= props.getProperty("p1_siblingsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_siblingsNo"
						name="p1_siblingsNo" onclick="onCheck(this,'p1_siblings')"
						<%= props.getProperty("p1_siblingsNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formSiblings" /></td>
				</tr>
				<tr>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_sleepCryOk,p1_sleepCryNo,p1_bondingOk,p1_bondingNo');" /></td>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_soothabilityOk,p1_soothabilityNo,p1_pFatigueOk,p1_pFatigueNo');" /></td>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_homeVisitOk,p1_homeVisitNo,p1_famConflictOk,p1_famConflictNo');" /></td>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_siblingsOk,p1_siblingsNo');" /></td>
				</tr>
				<!-- </table>
          <br/>
          <table style="font-size:9pt;" cellpadding="0" cellspacing="0" width="100%"  >
             -->
				<tr>
					<td colspan="12">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="12"><bean:message
						key="oscarEncounter.formRourke2006_1.formOtherIssues" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_2ndSmokeOk"
						name="p1_2ndSmokeOk" onclick="onCheck(this,'p1_2ndSmoke')"
						<%= props.getProperty("p1_2ndSmokeOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_2ndSmokeNo"
						name="p1_2ndSmokeNo" onclick="onCheck(this,'p1_2ndSmoke')"
						<%= props.getProperty("p1_2ndSmokeNo", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.formSecondHandSmoke" />*</a></b></td>
					<td valign="top"><input type="radio" id="p1_altMedOk"
						name="p1_altMedOk" onclick="onCheck(this,'p1_altMed')"
						<%= props.getProperty("p1_altMedOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_altMedNo"
						name="p1_altMedNo" onclick="onCheck(this,'p1_altMed')"
						<%= props.getProperty("p1_altMedNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formAltMed" />*</a></td>
					<td valign="top"><input type="radio" id="p1_pacifierOk"
						name="p1_pacifierOk" onclick="onCheck(this,'p1_pacifier')"
						<%= props.getProperty("p1_pacifierOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_pacifierNo"
						name="p1_pacifierNo" onclick="onCheck(this,'p1_pacifier')"
						<%= props.getProperty("p1_pacifierNo", "") %>></td>
					<td colspan="4" valign="top"><b><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formPacifierUse" />*</a></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_feverOk"
						name="p1_feverOk" onclick="onCheck(this,'p1_fever')"
						<%= props.getProperty("p1_feverOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_feverNo"
						name="p1_feverNo" onclick="onCheck(this,'p1_fever')"
						<%= props.getProperty("p1_feverNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formFever" />*</a></td>
					<td valign="top"><input type="radio" id="p1_tmpControlOk"
						name="p1_tmpControlOk" onclick="onCheck(this,'p1_tmpControl')"
						<%= props.getProperty("p1_tmpControlOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_tmpControlNo"
						name="p1_tmpControlNo" onclick="onCheck(this,'p1_tmpControl')"
						<%= props.getProperty("p1_tmpControlNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formTempCtrl" />*</a></td>
					<td valign="top"><input type="radio" id="p1_sunExposureOk"
						name="p1_sunExposureOk" onclick="onCheck(this,'p1_sunExposure')"
						<%= props.getProperty("p1_sunExposureOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_sunExposureNo"
						name="p1_sunExposureNo" onclick="onCheck(this,'p1_sunExposure')"
						<%= props.getProperty("p1_sunExposureNo", "") %>></td>
					<td colspan="4" valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSunExposure" />*</a></td>
				</tr>
				<tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_2ndSmokeOk,p1_2ndSmokeNo,p1_feverOk,p1_feverNo');" /></td>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_altMedOk,p1_altMedNo,p1_tmpControlOk,p1_tmpControlNo');" /></td>
					<td class="edcol" colspan="6" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_pacifierOk,p1_pacifierNo,p1_sunExposureOk,p1_sunExposureNo');" /></td>
				</tr>
				<tr>
					<td colspan="12">&nbsp;</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgDevelopment" />**</a><br>
			<bean:message
				key="oscarEncounter.formRourke2006_1.msgDevelopmentDesc" /><br />
			<img height="15" width="20" src="graphics/Checkmark_Lwhite.gif"><bean:message
				key="oscarEncounter.formRourke2006_1.msgDevelopmentLegend" /></td>
			<td colspan="3" valign="top" align="center">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea id="p1_development1w"
						name="p1_development1w" rows="10" cols="25" class="wide"><%= props.getProperty("p1_development1w", "") %></textarea></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top" align="center">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea id="p1_development2w"
						name="p1_development2w" rows="10" cols="25" class="wide"><%= props.getProperty("p1_development2w", "") %></textarea></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea id="p1_development1m"
						name="p1_development1m" rows="5" cols="25" class="wide"><%= props.getProperty("p1_development1m", "") %></textarea></td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_focusGaze1mOk"
						name="p1_focusGaze1mOk" onclick="onCheck(this,'p1_focusGaze1m')"
						<%= props.getProperty("p1_focusGaze1mOk", "") %>></td>
					<td><input type="radio" id="p1_focusGaze1mNo"
						name="p1_focusGaze1mNo" onclick="onCheck(this,'p1_focusGaze1m')"
						<%= props.getProperty("p1_focusGaze1mNo", "") %>><bean:message
						key="oscarEncounter.formRourke1.formFocusesGaze" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_focusGaze1mOk,p1_focusGaze1mNo');" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_startles1mOk"
						name="p1_startles1mOk" onclick="onCheck(this,'p1_startles1m')"
						<%= props.getProperty("p1_startles1mOk", "") %>></td>
					<td><input type="radio" id="p1_startles1mNo"
						name="p1_startles1mNo" onclick="onCheck(this,'p1_startles1m')"
						<%= props.getProperty("p1_startles1mNo", "") %>><bean:message
						key="oscarEncounter.formRourke1.formSuddenNoise" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_startles1mOk,p1_startles1mNo');" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_sucks1mOk"
						name="p1_sucks1mOk" onclick="onCheck(this,'p1_sucks1m')"
						<%= props.getProperty("p1_sucks1mOk", "") %>></td>
					<td><input type="radio" id="p1_sucks1mNo" name="p1_sucks1mNo"
						onclick="onCheck(this,'p1_sucks1m')"
						<%= props.getProperty("p1_sucks1mNo", "") %>><bean:message
						key="oscarEncounter.formRourke1.formSucksWell" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_sucks1mOk,p1_sucks1mNo');" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio"
						id="p1_noParentsConcerns1mOk" name="p1_noParentsConcerns1mOk"
						onclick="onCheck(this,'p1_noParentsConcerns1m')"
						<%= props.getProperty("p1_noParentsConcerns1mOk", "") %>></td>
					<td><input type="radio" id="p1_noParentsConcerns1mNo"
						name="p1_noParentsConcerns1mNo"
						onclick="onCheck(this,'p1_noParentsConcerns1m')"
						<%= props.getProperty("p1_noParentsConcerns1mNo", "") %>><bean:message
						key="oscarEncounter.formRourke1.formNoparentConcerns" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_noParentsConcerns1mOk,p1_noParentsConcerns1mNo');" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgPhysicalExamination" /></a><br>
			<bean:message
				key="oscarEncounter.formRourke1.msgPhysicalExaminationDesc" />
			</div>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_skin1w" <%= props.getProperty("p1_skin1w", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke1.formDrySkin" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_fontanelles1w"
						<%= props.getProperty("p1_fontanelles1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_eyes1w" <%= props.getProperty("p1_eyes1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formRedReflex" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_ears1w" <%= props.getProperty("p1_ears1w", "") %>></td>
					<td><i><bean:message
						key="oscarEncounter.formRourke2006_1.formEarDrums" /></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_heartLungs1w"
						<%= props.getProperty("p1_heartLungs1w", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke1.formHeart" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_umbilicus1w"
						<%= props.getProperty("p1_umbilicus1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formUmbilicus" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_femoralPulses1w"
						<%= props.getProperty("p1_femoralPulses1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFemoralPulses" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_hips1w" <%= props.getProperty("p1_hips1w", "") %>></td>
					<td><i><bean:message
						key="oscarEncounter.formRourke1.formHips" /></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_muscleTone1w"
						<%= props.getProperty("p1_muscleTone1w", "") %>></td>
					<td><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formMuscleTone" />*</a></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_testicles1w"
						<%= props.getProperty("p1_testicles1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formTescicles" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_maleUrinary1w"
						<%= props.getProperty("p1_maleUrinary1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formMaleUrinaryStream" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_skin2w" <%= props.getProperty("p1_skin2w", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke1.formDrySkin" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_fontanelles2w"
						<%= props.getProperty("p1_fontanelles2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_eyes2w" <%= props.getProperty("p1_eyes2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formRedReflex" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_ears2w" <%= props.getProperty("p1_ears2w", "") %>></td>
					<td><i><bean:message
						key="oscarEncounter.formRourke2006_1.formEarDrums" /></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_heartLungs2w"
						<%= props.getProperty("p1_heartLungs2w", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke1.formHeart" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_umbilicus2w"
						<%= props.getProperty("p1_umbilicus2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formUmbilicus" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_femoralPulses2w"
						<%= props.getProperty("p1_femoralPulses2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFemoralPulses" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_hips2w" <%= props.getProperty("p1_hips2w", "") %>></td>
					<td><i><bean:message
						key="oscarEncounter.formRourke2006_1.formHips" /></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_muscleTone2w"
						<%= props.getProperty("p1_muscleTone2w", "") %>></td>
					<td><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formMuscleTone" />*</a></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_testicles2w"
						<%= props.getProperty("p1_testicles2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formTescicles" /><br>
					</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_maleUrinary2w"
						<%= props.getProperty("p1_maleUrinary2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formMaleUrinaryStream" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_fontanelles1m"
						<%= props.getProperty("p1_fontanelles1m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_eyes1m" <%= props.getProperty("p1_eyes1m", "") %>></td>
					<td><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.formRedReflex" />*</a></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_corneal1m" <%= props.getProperty("p1_corneal1m", "") %>></td>
					<td><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formCornealReflex" />*</a></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_hearing1m" <%= props.getProperty("p1_hearing1m", "") %>></td>
					<td><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formHearingInquiry" />*</a></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_heart1m" <%= props.getProperty("p1_heart1m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke1.formHeart1" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_hips1m" <%= props.getProperty("p1_hips1m", "") %>></td>
					<td><i><bean:message
						key="oscarEncounter.formRourke2006_1.formHips" /></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_muscleTone1m"
						<%= props.getProperty("p1_muscleTone1m", "") %>></td>
					<td><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formMuscleTone" />*</a></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgProblemsAndPlans" /></a></td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea id="p1_problems1w"
						name="p1_problems1w" rows="5" cols="25" class="wide"><%= props.getProperty("p1_problems1w", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_pkuThyroid1w"
						<%= props.getProperty("p1_pkuThyroid1w", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke1.formThyroid" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_hemoScreen1w"
						<%= props.getProperty("p1_hemoScreen1w", "") %>></td>
					<td><b><a href="javascript:showNotes()"><bean:message
						key="oscarEncounter.formRourke1.formHemoglobinopathy" /></a> (if at
					risk)*</b></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea id="p1_problems2w"
						name="p1_problems2w" rows="5" cols="25" class="wide"><%= props.getProperty("p1_problems2w", "") %></textarea></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea id="p1_problems1m"
						name="p1_problems1m" rows="5" cols="25" class="wide"><%= props.getProperty("p1_problems1m", "") %></textarea></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgImmunization" /></a><br>
			<bean:message key="oscarEncounter.formRourke1.msgImmunizationDesc" />
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td style="text-align: center" colspan="2"><b><bean:message
						key="oscarEncounter.formRourke2006_1.msgImmunizationColTitle" /></b><br />
					<bean:message
						key="oscarEncounter.formRourke2006_1.msgImmunizationHepatitis" />
					</td>
				</tr>
				<tr>
					<td><input type="checkbox" class="chk"
						name="p1_hepatitisVaccine1w"
						<%= props.getProperty("p1_hepatitisVaccine1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_1.msgImmunizationHepatitisVaccine" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td style="text-align: center"><b><bean:message
						key="oscarEncounter.formRourke2006_1.msgImmunizationColTitle" /></b></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td style="text-align: center" colspan="2"><b><bean:message
						key="oscarEncounter.formRourke2006_1.msgImmunizationColTitle" /></b><br />
					<bean:message
						key="oscarEncounter.formRourke2006_1.msgImmunizationHepatitis" />
					</td>
				</tr>
				<tr>
					<td><input type="checkbox" class="chk"
						name="p1_hepatitisVaccine1m"
						<%= props.getProperty("p1_hepatitisVaccine1m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_1.msgImmunizationHepatitisVaccine" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.formSignature" /></a></td>
			<td colspan="3"><input type="text" class="wide"
				style="width: 100%" name="p1_signature1w"
				value="<%= props.getProperty("p1_signature1w", "") %>" /></td>
			<td colspan="3"><input type="text" class="wide" maxlength="42"
				style="width: 100%" name="p1_signature2w"
				value="<%= props.getProperty("p1_signature2w", "") %>" /></td>
			<td colspan="3"><input type="text" class="wide"
				style="width: 100%" name="p1_signature1m"
				value="<%= props.getProperty("p1_signature1m", "") %>" /></td>
		</tr>

	</table>

	<table cellpadding="0" cellspacing="0" class="Header" class="hidePrint">
		<tr>
			<td nowrap="true"><input type="submit"
				value="<bean:message key="oscarEncounter.formRourke1.btnSave"/>"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke1.btnSaveExit"/>"
				onclick="javascript:return onSaveExit();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke1.btnExit"/>"
				onclick="javascript:return onExit();"> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke1.btnPrint"/>"
				onclick="javascript:return onPrint();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke2006.btnPrintAll"/>"
				onclick="javascript:return onPrintAll();" /> <input type="button"
				value="About"
				onclick="javascript:return popPage('formRourke2006intro.html','About Rourke');" />
			</td>
			<td align="center" width="100%">
			<% if(formId > 0)
           { %> <a name="length" href="#"
				onclick="onGraph('<%=request.getContextPath()%>/form/formname.do?submit=graph&form_class=Rourke2006&__title=Baby+Growth+Graph1&__cfgfile=<%=growthCharts[0]%>&demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');return false;">
			<bean:message key="oscarEncounter.formRourke1.btnGraphLenghtWeight" /></a><br>
			<a name="headCirc" href="#"
				onclick="onGraph('<%=request.getContextPath()%>/form/formname.do?submit=graph&form_class=Rourke2006&__title=Baby+Head+Circumference&__cfgfile=<%=growthCharts[1]%>&demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');return false;">
			<bean:message key="oscarEncounter.formRourke1.btnGraphHead" /></a> <% }else { %>
			&nbsp; <% } %>
			</td>
			<td nowrap="true"><a><bean:message
				key="oscarEncounter.formRourke2006.Pg1" /></a>&nbsp;|&nbsp; <a
				href="formrourke2006p2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg2" /></a>&nbsp;|&nbsp; <a
				href="formrourke2006p3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg3" /></a>&nbsp;|&nbsp; <a
				href="formrourke2006p4.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg4" /></a></td>
		</tr>
	</table>
	<p style="font-size: 8pt;"><bean:message
		key="oscarEncounter.formRourke2006.footer" /><br />
	</p>

</html:form>
<form id="frmPopUp" method="get" action=""></form>
<form id="graph" method="post" action=""></form>
</body>
<script type="text/javascript">
    Calendar.setup({ inputField : "p1_date1w", ifFormat : "%d/%m/%Y", showsTime :false, button : "p1_date1w_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "p1_date2w", ifFormat : "%d/%m/%Y", showsTime :false, button : "p1_date2w_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "p1_date1m", ifFormat : "%d/%m/%Y", showsTime :false, button : "p1_date1m_cal", singleClick : true, step : 1 });
</script>
</html:html>
