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

<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page language="java"%>
<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
    String formClass = "Rourke2009";
    String formLink = "formrourke2009p2.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(demoNo, formId);    
    String []growthCharts = new String[2];
    
    if( ((FrmRourke2009Record)rec).isFemale(demoNo) ) {
        growthCharts[0] = new String("GrowthChartRourke2009Girls&__cfgGraphicFile=GrowthChartRourke2009GirlGraphic&__cfgGraphicFile=GrowthChartRourke2009GirlGraphic2&__template=GrowthChartRourke2009Girls");
        growthCharts[1] = new String("GrowthChartRourke2009Girls2&__cfgGraphicFile=GrowthChartRourke2009GirlGraphic3&__cfgGraphicFile=GrowthChartRourke2009GirlGraphic4&__template=GrowthChartRourke2009Girlspg2");
    }
    else {
        growthCharts[0] = new String("GrowthChartRourke2009Boys&__cfgGraphicFile=GrowthChartRourke2009BoyGraphic&__cfgGraphicFile=GrowthChartRourke2009BoyGraphic2&__template=GrowthChartRourke2009Boys");
        growthCharts[1] = new String("GrowthChartRourke2009Boys2&__cfgGraphicFile=GrowthChartRourke2009BoyGraphic3&__cfgGraphicFile=GrowthChartRourke2009BoyGraphic4&__template=GrowthChartRourke2009Boyspg2");
    }
    
    FrmData fd = new FrmData();
    String resource = fd.getResource();    
    props.setProperty("c_lastVisited", "p2");

    //get project_home
    String project_home = request.getContextPath().substring(1);
    
%>
<%
  boolean bView = false;
  if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 
%>

<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<meta content="text/html;  charset=UTF-8" http-equiv="Content-Type">
<title>Rourke2009 Record 2</title>
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
    
        var txtBirthRem = new ylib.widget.TextCounter("c_riskFactors", 90,3);
        var txtriskFact = new ylib.widget.TextCounter("c_famHistory", 84,3);
        var txtConcern1w = new ylib.widget.TextCounter("p2_pConcern2m", 195,5);
        var txtConcern2w = new ylib.widget.TextCounter("p2_pConcern4m", 220,5);
        var txtConcern1m = new ylib.widget.TextCounter("p2_pConcern6m", 235,5);
        var txtNutrition2m = new ylib.widget.TextCounter("p2_nutrition2m", 234,6);
        var txtNutrition4m = new ylib.widget.TextCounter("p2_nutrition4m", 264,6);
        var txtDevelopment1w = new ylib.widget.TextCounter("p2_development2m", 123,3);
        var txtDevelopment2w = new ylib.widget.TextCounter("p2_development4m", 205,5);
        var txtDevelopment1m = new ylib.widget.TextCounter("p2_development6m", 140,3);                
        var txtProblems2w = new ylib.widget.TextCounter("p2_problems2m", 246,6);
        var txtProblems1m = new ylib.widget.TextCounter("p2_problems4m", 246,6);        
        var txtProblems1w = new ylib.widget.TextCounter("p2_problems6m", 230,5);
    }            
    
    function del( names ) {        
        var arrElem = names.split(",");
        var elem;
    
        for( var idx = 0; idx < arrElem.length; ++idx ) {
            elem = document.getElementById(arrElem[idx]);
            elem.checked = false;
        }
    
    }
    
    function showNotes() {
        var frm = document.getElementById("frmPopUp");
        frm.action = "Rourke2009_Notes.pdf";
        frm.submit();
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
    
    function $F(elem) {
        return document.forms[0].elements[elem].value;
    
    }
    
    /*We have to check that measurements are numeric and an observation date has 
     *been entered for that measurment
     */
    function checkMeasures() {
        var measurements = new Array(3);
        measurements[0] = new Array("p2_ht2m", "p2_wt2m", "p2_hc2m");
        measurements[1] = new Array("p2_ht4m", "p2_wt4m", "p2_hc4m");
        measurements[2] = new Array("p2_ht6m", "p2_wt6m", "p2_hc6m");
        var dates = new Array("p2_date2m", "p2_date4m", "p2_date6m");
                    
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
    
    function onGraph(url) {    
        if( checkMeasures() ) {
            document.forms["graph"].action = url;
            document.forms["graph"].submit();
        }          
    }
    
    function onPrint() {
        document.forms[0].submit.value="print"; 
                
        document.forms[0].action = "../form/createpdf?__title=Rourke+Baby+Report+Pg2&__cfgfile=rourke2009printCfgPg2&__template=rourke2009p2";
        document.forms[0].target="_blank";            
        
        return true;
    }
    
    function onPrintAll() {
        document.forms[0].submit.value="printAll"; 
                
        document.forms[0].action = "../form/formname.do?__title=Rourke+Baby+Report&__cfgfile=rourke2009printCfgPg1&__cfgfile=rourke2009printCfgPg2&__cfgfile=rourke2009printCfgPg3&__cfgfile=rourke2009printCfgPg4&__template=rourke2009";
        document.forms[0].target="_blank";            
        
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
    <div style="display:block; width:100%; text-align:center; background-color: #FFFFFF;"><img alt="copyright" src="graphics/banner.png" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2009.formCopyRight" />')"
                                                   onMouseOut="hideLayer()">
    </div>
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
		value=<%=props.getProperty("c_lastVisited", "p2")%> />
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
				onclick="javascript:return popPage('http://rourkebabyrecord.ca','About Rourke');" />
			</td>
			<td align="center" width="100%">
			<% if(formId > 0)
           { %> <a name="length" href="#"
				onclick="onGraph('<%=request.getContextPath()%>/form/formname.do?submit=graph&form_class=Rourke2009&__title=Baby+Growth+Graph1&__cfgfile=<%=growthCharts[0]%>&demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');return false;">
			<bean:message key="oscarEncounter.formRourke1.btnGraphLenghtWeight" /></a><br>
			<a name="headCirc" href="#"
				onclick="onGraph('<%=request.getContextPath()%>/form/formname.do?submit=graph&form_class=Rourke2009&__title=Baby+Head+Circumference&__cfgfile=<%=growthCharts[1]%>&demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');return false;">
			<bean:message key="oscarEncounter.formRourke1.btnGraphHead" /></a> <% }else { %>
			&nbsp; <% } %>
			</td>
			<td nowrap="true"><a
				href="formrourke2009p1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg1" /></a>&nbsp;|&nbsp; <a><bean:message
				key="oscarEncounter.formRourke2006.Pg2" /></a>&nbsp;|&nbsp; <a
				href="formrourke2009p3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg3" /></a>&nbsp;|&nbsp; <a
				href="formrourke2009p4.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg4" /></a></td>
		</tr>
	</table>

	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr class="titleBar">
			<th><bean:message
				key="oscarEncounter.formRourke2006_2.msgRourkeBabyRecord" /></th>
		</tr>
	</table>

	<table cellpadding="0" cellspacing="0" width="100%" border="0">
		<tr valign="top">
			<td align="center"><bean:message
				key="oscarEncounter.formRourke2006_2.formRiskFactors" /><br>
			<textarea wrap="physical" id="c_riskFactors" name="c_riskFactors"
				rows="3" cols="17"><%= props.getProperty("c_riskFactors", "") %></textarea>
			</td>
			<td nowrap align="center"><bean:message
				key="oscarEncounter.formRourke2006_2.formFamHistory" /><br>
			<textarea id="c_famHistory" name="c_famHistory" rows="3" cols="17"><%= props.getProperty("c_famHistory", "") %></textarea>
			</td>
			<td width="65%" nowrap align="center">
			<p><bean:message key="oscarEncounter.formRourke1.msgName" />: <input
				type="text" name="c_pName" maxlength="60" size="30"
				value="<%= props.getProperty("c_pName", "") %>" readonly="true" />
			&nbsp;&nbsp; <bean:message
				key="oscarEncounter.formRourke1.msgBirthDate" /> (d/m/yyyy): <input
				type="text" name="c_birthDate" size="10" maxlength="10"
				value="<%= props.getProperty("c_birthDate", "") %>" readonly="true">
			&nbsp;&nbsp; <% if(! ((FrmRourke2009Record)rec).isFemale(demoNo))
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
			</td>
		</tr>
	</table>

	<table cellpadding="0" cellspacing="0" width="100%" border="1">
		<tr align="center">
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke2006_1.visitDate" /></a></td>
			<td colspan="3" class="row"><a><bean:message
				key="oscarEncounter.formRourke2006_2.msg2mos" /></a></td>
			<td colspan="3" class="row"><a><bean:message
				key="oscarEncounter.formRourke2006_2.msg4mos" /></a></td>
			<td colspan="3" class="row"><a><bean:message
				key="oscarEncounter.formRourke2006_2.msg6mos" /></a></td>
		</tr>
		<tr align="center">
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgDate" /></a></td>
			<td colspan="3"><input readonly type="text" id="p2_date2m"
				ondblclick="resetDate(this)" name="p2_date2m" size="10"
				value="<%=UtilMisc.htmlEscape(props.getProperty("p2_date2m", ""))%>" />
			<img src="../images/cal.gif" id="p2_date2m_cal"></td>
			<td colspan="3"><input readonly type="text" id="p2_date4m"
				ondblclick="resetDate(this)" name="p2_date4m" size="10"
				value="<%=UtilMisc.htmlEscape(props.getProperty("p2_date4m", ""))%>" />
			<img src="../images/cal.gif" id="p2_date4m_cal"></td>
			<td colspan="3"><input readonly type="text" id="p2_date6m"
				ondblclick="resetDate(this)" name="p2_date6m" size="10"
				value="<%=UtilMisc.htmlEscape(props.getProperty("p2_date6m", ""))%>" />
			<img src="../images/cal.gif" id="p2_date6m_cal"></td>
		</tr>
		<tr align="center">
			<td class="column" rowspan="2"><a><bean:message
                            key="oscarEncounter.formRourke1.btnGrowth" />*</a><br>
                            <bean:message
                            key="oscarEncounter.formRourke2009_1.btnGrowthmsg" />
                        </td>
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formWt" /></td>
			<td><bean:message
				key="oscarEncounter.formRourke2006_3.formHdCirc" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formWt" /></td>
			<td><bean:message
				key="oscarEncounter.formRourke2006_3.formHdCirc" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke2006_2.formWt6m" /></td>
			<td><bean:message
				key="oscarEncounter.formRourke2006_3.formHdCirc" /></td>
		</tr>
		<tr align="center">
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p2_ht2m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_ht2m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="wtEnglish2Metric(this);" name="p2_wt2m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_wt2m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p2_hc2m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_hc2m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p2_ht4m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_ht4m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="wtEnglish2Metric(this);" name="p2_wt4m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_wt4m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p2_hc4m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_hc4m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p2_ht6m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_ht6m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="wtEnglish2Metric(this);" name="p2_wt6m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_wt6m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p2_hc6m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_hc6m", "") %>"></td>
		</tr>
		<tr align="center">
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.formParentalConcerns" /></a></td>
			<td colspan="3"><textarea id="p2_pConcern2m"
				name="p2_pConcern2m" class="wide" cols="10" rows="5"><%= props.getProperty("p2_pConcern2m", "") %></textarea>
			</td>
			<td colspan="3"><textarea id="p2_pConcern4m"
				name="p2_pConcern4m" class="wide" cols="10" rows="5"><%= props.getProperty("p2_pConcern4m", "") %></textarea>
			</td>
			<td colspan="3"><textarea id="p2_pConcern6m"
				name="p2_pConcern6m" class="wide" cols="10" rows="5"><%= props.getProperty("p2_pConcern6m", "") %></textarea>
			</td>
		</tr>
		<tr align="center">

			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgNutrition" />*:</a></td>

			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="3"><textarea id="p2_nutrition2m"
						name="p2_nutrition2m" class="wide" rows="5" cols="25"><%= props.getProperty("p2_nutrition2m", "") %></textarea></td>
				</tr>
				<tr>
                                <tr>
                                            <td style="padding-right: 5pt" valign="top"><img height="15"
                                                        width="20" src="graphics/Checkmark_L.gif"></td>
                                            <td class="edcol" valign="top">X</td>
                                            <td>&nbsp;</td>
                                </tr>
                                <tr>
                                        <td valign="top"><input type="radio" id="p2_breastFeeding2mOk"
						name="p2_breastFeeding2mOk" onclick="onCheck(this,'p2_breastFeeding2m')"
						<%= props.getProperty("p2_breastFeeding2mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_breastFeeding2mNo"
						name="p2_breastFeeding2mNo" onclick="onCheck(this,'p2_breastFeeding2m')"
						<%= props.getProperty("p2_breastFeeding2mNo", "") %>></td>
					<td><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"
						onclick="popPage('<%=resource%>n_breastFeeding');return false"><bean:message
						key="oscarEncounter.formRourke2006_1.btnBreastFeeding" /><br />
					</a><span
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.msgBreastFeedingDescr" /></span></b></td>
				</tr>
				<tr>
                                        <td valign="top"><input type="radio" id="p2_formulaFeeding2mOk"
						name="p2_formulaFeeding2mOk" onclick="onCheck(this,'p2_formulaFeeding2m')"
						<%= props.getProperty("p2_formulaFeeding2mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_formulaFeeding2mNo"
						name="p2_formulaFeeding2mNo" onclick="onCheck(this,'p2_formulaFeeding2m')"
						<%= props.getProperty("p2_formulaFeeding2mNo", "") %>></td>
					<td><a  href="javascript:showNotes()"
                                                onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()" href="javascript:showNotes()"><i><bean:message
						key="oscarEncounter.formRourke2009_2.msgFormulaFeeding2m" /></i></a></td>
				</tr>
                                <tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_breastFeeding2mOk,p2_breastFeeding2mNo,p2_formulaFeeding2mOk,p2_formulaFeeding2mNo');" /></td>
                                </tr>
			</table>

			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="3"><textarea id="p2_nutrition4m"
						name="p2_nutrition4m" class="wide" rows="5" cols="25"><%= props.getProperty("p2_nutrition4m", "") %></textarea></td>
				</tr>
                                <tr>
                                            <td style="padding-right: 5pt" valign="top"><img height="15"
                                                        width="20" src="graphics/Checkmark_L.gif"></td>
                                            <td class="edcol" valign="top">X</td>
                                            <td>&nbsp;</td>
                                </tr>
                                <tr>
                                        <td valign="top"><input type="radio" id="p2_breastFeeding4mOk"
						name="p2_breastFeeding4mOk" onclick="onCheck(this,'p2_breastFeeding4m')"
						<%= props.getProperty("p2_breastFeeding4mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_breastFeeding4mNo"
						name="p2_breastFeeding4mNo" onclick="onCheck(this,'p2_breastFeeding4m')"
						<%= props.getProperty("p2_breastFeeding4mNo", "") %>></td>
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
                                        <td valign="top"><input type="radio" id="p2_formulaFeeding4mOk"
						name="p2_formulaFeeding4mOk" onclick="onCheck(this,'p2_formulaFeeding4m')"
						<%= props.getProperty("p2_formulaFeeding4mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_formulaFeeding4mNo"
						name="p2_formulaFeeding4mNo" onclick="onCheck(this,'p2_formulaFeeding4m')"
						<%= props.getProperty("p2_formulaFeeding4mNo", "") %>></td>
					<td><a  href="javascript:showNotes()"
                                                onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()" href="javascript:showNotes()"><i><bean:message
						key="oscarEncounter.formRourke2009_2.msgFormulaFeeding4m" /></i></a></td>
				</tr>
                                <tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_breastFeeding4mOk,p2_breastFeeding4mNo,p2_formulaFeeding4mOk,p2_formulaFeeding4mNo');" /></td>
                                </tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%" height="100%">
                                <tr>
                                            <td style="padding-right: 5pt" valign="top"><img height="15"
                                                        width="20" src="graphics/Checkmark_L.gif"></td>
                                            <td class="edcol" valign="top">X</td>
                                            <td>&nbsp;</td>
                                </tr>
                                <tr>
                                        <td valign="top"><input type="radio" id="p2_breastFeeding6mOk"
						name="p2_breastFeeding6mOk" onclick="onCheck(this,'p2_breastFeeding6m')"
						<%= props.getProperty("p2_breastFeeding6mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_breastFeeding6mNo"
						name="p2_breastFeeding6mNo" onclick="onCheck(this,'p2_breastFeeding6m')"
						<%= props.getProperty("p2_breastFeeding6mNo", "") %>></td>
					<td><b><a
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()" href="javascript:showNotes()"><bean:message
						key="oscarEncounter.formRourke2006_1.btnBreastFeeding" /></a><br />
					<span
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_2.msgBreastFeedingDescr6m" /></span></b></td>
				</tr>
				<tr>
                                        <td valign="top"><input type="radio" id="p2_formulaFeeding6mOk"
						name="p2_formulaFeeding6mOk" onclick="onCheck(this,'p2_formulaFeeding6m')"
						<%= props.getProperty("p2_formulaFeeding6mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_formulaFeeding6mNo"
						name="p2_formulaFeeding6mNo" onclick="onCheck(this,'p2_formulaFeeding6m')"
						<%= props.getProperty("p2_formulaFeeding6mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_2.msgFormulaFeedingLong6m" /></td>
				</tr>
				<tr>
                                        <td valign="top"><input type="radio" id="p2_bottle6mOk"
						name="p2_bottle6mOk" onclick="onCheck(this,'p2_bottle6m')"
						<%= props.getProperty("p2_bottle6mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_bottle6mNo"
						name="p2_bottle6mNo" onclick="onCheck(this,'p2_bottle6m')"
						<%= props.getProperty("p2_bottle6mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_2.msgBottle" /></td>
				</tr>
				<tr>
                                        <td valign="top"><input type="radio" id="p2_liquids6mOk"
						name="p2_liquids6mOk" onclick="onCheck(this,'p2_liquids6m')"
						<%= props.getProperty("p2_liquids6mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_liquids6mNo"
						name="p2_liquids6mNo" onclick="onCheck(this,'p2_liquids6m')"
						<%= props.getProperty("p2_liquids6mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_2.msgLiquids" /></td>
				</tr>
				<tr>
                                        <td valign="top"><input type="radio" id="p2_iron6mOk"
						name="p2_iron6mOk" onclick="onCheck(this,'p2_iron6m')"
						<%= props.getProperty("p2_iron6mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_iron6mNo"
						name="p2_iron6mNo" onclick="onCheck(this,'p2_iron6m')"
						<%= props.getProperty("p2_iron6mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_2.msgIronFoods" /></td>
				</tr>
				<tr>
                                        <td valign="top"><input type="radio" id="p2_vegFruit6mOk"
						name="p2_vegFruit6mOk" onclick="onCheck(this,'p2_vegFruit6m')"
						<%= props.getProperty("p2_vegFruit6mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_vegFruit6mNo"
						name="p2_vegFruit6mNo" onclick="onCheck(this,'p2_vegFruit6m')"
						<%= props.getProperty("p2_vegFruit6mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_2.msgVegFruits" /></td>
				</tr>
				<tr>
                                        <td valign="top"><input type="radio" id="p2_egg6mOk"
						name="p2_egg6mOk" onclick="onCheck(this,'p2_egg6m')"
						<%= props.getProperty("p2_egg6mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_egg6mNo"
						name="p2_egg6mNo" onclick="onCheck(this,'p2_egg6m')"
						<%= props.getProperty("p2_egg6mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_2.msgEggWhites" /></td>
				</tr>
				<tr>
                                        <td valign="top"><input type="radio" id="p2_choking6mOk"
						name="p2_choking6mOk" onclick="onCheck(this,'p2_choking6m')"
						<%= props.getProperty("p2_choking6mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_choking6mNo"
						name="p2_choking6mNo" onclick="onCheck(this,'p2_choking6m')"
						<%= props.getProperty("p2_choking6mNo", "") %>></td>
					<td><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.msgChoking" />*</a></td>
				</tr>
                                <tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_breastFeeding6mOk,p2_breastFeeding6mNo,p2_formulaFeeding6mOk,p2_formulaFeeding6mNo,p2_bottle6mOk,p2_bottle6mNo,p2_liquids6mOk,p2_liquids6mNo,p2_iron6mOk,p2_iron6mNo,p2_vegFruit6mOk,p2_vegFruit6mNo,p2_egg6mOk,p2_egg6mNo,p2_choking6mOk,p2_choking6mNo');" /></td>
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
			<table style="font-size: 8pt;" cellpadding="0" cellspacing="0"
				width="100%">
				<tr>
					<td colspan="15">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="15"><bean:message
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
					<td colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_carSeatOk"
						name="p2_carSeatOk" onclick="onCheck(this,'p2_carSeat')"
						<%= props.getProperty("p2_carSeatOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_carSeatNo"
						name="p2_carSeatNo" onclick="onCheck(this,'p2_carSeat')"
						<%= props.getProperty("p2_carSeatNo", "") %>></td>
					<td valign="top"><b><a
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()" href="javascript:showNotes()"><bean:message
						key="oscarEncounter.formRourke1.formCarSeat" /></a>*</b></td>
					<td valign="top"><input type="radio" id="p2_sleepPosOk"
						name="p2_sleepPosOk" onclick="onCheck(this,'p2_sleepPos')"
						<%= props.getProperty("p2_sleepPosOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_sleepPosNo"
						name="p2_sleepPosNo" onclick="onCheck(this,'p2_sleepPos')"
						<%= props.getProperty("p2_sleepPosNo", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formSleepPos" /></a></b></td>
					<td valign="top"><input type="radio" id="p2_poisonsOk"
						name="p2_poisonsOk" onclick="onCheck(this,'p2_poisons')"
						<%= props.getProperty("p2_poisonsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_poisonsNo"
						name="p2_poisonsNo" onclick="onCheck(this,'p2_poisons')"
						<%= props.getProperty("p2_poisonsNo", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formPoisons" /></a></b></td>
					<td valign="top"><input type="radio" id="p2_firearmSafetyOk"
						name="p2_firearmSafetyOk"
						onclick="onCheck(this,'p2_firearmSafety')"
						<%= props.getProperty("p2_firearmSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_firearmSafetyNo"
						name="p2_firearmSafetyNo"
						onclick="onCheck(this,'p2_firearmSafety')"
						<%= props.getProperty("p2_firearmSafetyNo", "") %>></td>
					<td colspan="4" valign="top"><b><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formFireArm" />*</a></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_electricOk"
						name="p2_electricOk" onclick="onCheck(this,'p2_electric')"
						<%= props.getProperty("p2_electricOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_electricNo"
						name="p2_electricNo" onclick="onCheck(this,'p2_electric')"
						<%= props.getProperty("p2_electricNo", "") %>></td>
					<td valign="top"><i><bean:message
						key="oscarEncounter.formRourke2006_2.formElectric" /></i></td>
					<td valign="top"><input type="radio" id="p2_smokeSafetyOk"
						name="p2_smokeSafetyOk" onclick="onCheck(this,'p2_smokeSafety')"
						<%= props.getProperty("p2_smokeSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_smokeSafetyNo"
						name="p2_smokeSafetyNo" onclick="onCheck(this,'p2_smokeSafety')"
						<%= props.getProperty("p2_smokeSafetyNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSmokeSafety" />*</a></td>
					<td valign="top"><input type="radio" id="p2_hotWaterOk"
						name="p2_hotWaterOk" onclick="onCheck(this,'p2_hotWater')"
						<%= props.getProperty("p2_hotWaterOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_hotWaterNo"
						name="p2_hotWaterNo" onclick="onCheck(this,'p2_hotWater')"
						<%= props.getProperty("p2_hotWaterNo", "") %>></td>
					<td valign="top"><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_2.formHotWater" />*</a></i></td>
					<td colspan="6">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_fallsOk"
						name="p2_fallsOk" onclick="onCheck(this,'p2_falls')"
						<%= props.getProperty("p2_fallsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_fallsNo"
						name="p2_fallsNo" onclick="onCheck(this,'p2_falls')"
						<%= props.getProperty("p2_fallsNo", "") %>></td>
					<td colspan="4" valign="top"><i><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formFalls" />*</a></i></td>
					<td valign="top"><input type="radio" id="p2_safeToysOk"
						name="p2_safeToysOk" onclick="onCheck(this,'p2_safeToys')"
						<%= props.getProperty("p2_safeToysOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_safeToysNo"
						name="p2_safeToysNo" onclick="onCheck(this,'p2_safeToys')"
						<%= props.getProperty("p2_safeToysNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSafeToys" />*</a></td>
					<td colspan="6">&nbsp;</td>
				</tr>
				<tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_carSeatOk,p2_carSeatNo,p2_electricOk,p2_electricNo,p2_fallsOk,p2_fallsNo');" /></td>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_sleepPosOk,p2_sleepPosNo,p2_smokeSafetyOk,p2_smokeSafetyNo');" /></td>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_poisonsOk,p2_poisonsNo,p2_hotWaterOk,p2_hotWaterNo,p2_safeToysOk,p2_safeToysNo');" /></td>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_firearmSafetyOk,p2_firearmSafetyNo');" /></td>
					<td colspan="3">&nbsp;</td>
				</tr>
				<!-- </table>
          <br/>
          <table style="font-size:9pt;" cellpadding="0" cellspacing="0" width="100%"  >                    
          -->
				<tr>
					<td colspan="15">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="15"><bean:message
						key="oscarEncounter.formRourke2006_1.formBehaviour" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_sleepCryOk"
						name="p2_sleepCryOk" onclick="onCheck(this,'p2_sleepCry')"
						<%= props.getProperty("p2_sleepCryOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_sleepCryNo"
						name="p2_sleepCryNo" onclick="onCheck(this,'p2_sleepCry')"
						<%= props.getProperty("p2_sleepCryNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formsleepCry" />**</a></td>
					<td valign="top"><input type="radio" id="p2_soothabilityOk"
						name="p2_soothabilityOk" onclick="onCheck(this,'p2_soothability')"
						<%= props.getProperty("p2_soothabilityOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_soothabilityNo"
						name="p2_soothabilityNo" onclick="onCheck(this,'p2_soothability')"
						<%= props.getProperty("p2_soothabilityNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formSoothability" /></td>
					<td valign="top"><input type="radio" id="p2_homeVisitOk"
						name="p2_homeVisitOk" onclick="onCheck(this,'p2_homeVisit')"
						<%= props.getProperty("p2_homeVisitOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_homeVisitNo"
						name="p2_homeVisitNo" onclick="onCheck(this,'p2_homeVisit')"
						<%= props.getProperty("p2_homeVisitNo", "") %>></td>
					<td colspan="7" valign="top"><b><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_2.formHomeVisit" />**</a></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_bondingOk"
						name="p2_bondingOk" onclick="onCheck(this,'p2_bonding')"
						<%= props.getProperty("p2_bondingOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_bondingNo"
						name="p2_bondingNo" onclick="onCheck(this,'p2_bonding')"
						<%= props.getProperty("p2_bondingNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formBonding" /></td>
					<td valign="top"><input type="radio" id="p2_pFatigueOk"
						name="p2_pFatigueOk" onclick="onCheck(this,'p2_pFatigue')"
						<%= props.getProperty("p2_pFatigueOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_pFatigueNo"
						name="p2_pFatigueNo" onclick="onCheck(this,'p2_pFatigue')"
						<%= props.getProperty("p2_pFatigueNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formParentFatigue" />**</a></td>
					<td valign="top"><input type="radio" id="p2_famConflictOk"
						name="p2_famConflictOk" onclick="onCheck(this,'p2_famConflict')"
						<%= props.getProperty("p2_famConflictOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_famConflictNo"
						name="p2_famConflictNo" onclick="onCheck(this,'p2_famConflict')"
						<%= props.getProperty("p2_famConflictNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formFamConflict" /></td>
					<td valign="top"><input type="radio" id="p2_siblingsOk"
						name="p2_siblingsOk" onclick="onCheck(this,'p2_siblings')"
						<%= props.getProperty("p2_siblingsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_siblingsNo"
						name="p2_siblingsNo" onclick="onCheck(this,'p2_siblings')"
						<%= props.getProperty("p2_siblingsNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formSiblings" /></td>
					<td valign="top"><input type="radio" id="p2_childCareOk"
						name="p2_childCareOk" onclick="onCheck(this,'p2_childCare')"
						<%= props.getProperty("p2_childCareOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_childCareNo"
						name="p2_childCareNo" onclick="onCheck(this,'p2_childCare')"
						<%= props.getProperty("p2_childCareNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
                                                onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><i><bean:message
						key="oscarEncounter.formRourke2009_2.formChildCare" /></i></a></td>
				</tr>
				<tr>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_sleepCryOk,p2_sleepCryNo,p2_bondingOk,p2_bondingNo');" /></td>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_soothabilityOk,p2_soothabilityNo,p2_pFatigueOk,p2_pFatigueNo');" /></td>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_homeVisitOk,p2_homeVisitNo,p2_famConflictOk,p2_famConflictNo');" /></td>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_siblingsOk,p2_siblingsNo');" /></td>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_childCareOk,p2_childCareNo');" /></td>
				</tr>
				<!-- </table>
          <br/>
          <table style="font-size:9pt;" cellpadding="0" cellspacing="0" width="100%"  >
             -->
				<tr>
					<td colspan="15">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="15"><bean:message
						key="oscarEncounter.formRourke2006_1.formOtherIssues" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_2ndSmokeOk"
						name="p2_2ndSmokeOk" onclick="onCheck(this,'p2_2ndSmoke')"
						<%= props.getProperty("p2_2ndSmokeOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_2ndSmokeNo"
						name="p2_2ndSmokeNo" onclick="onCheck(this,'p2_2ndSmoke')"
						<%= props.getProperty("p2_2ndSmokeNo", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.formSecondHandSmoke" />*</a></b></td>
					<td valign="top"><input type="radio" id="p2_teethingOk"
						name="p2_teethingOk" onclick="onCheck(this,'p2_teething')"
						<%= props.getProperty("p2_teethingOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_teethingNo"
						name="p2_teethingNo" onclick="onCheck(this,'p2_teething')"
						<%= props.getProperty("p2_teethingNo", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formTeething" />*</a></b></td>
					<td valign="top"><input type="radio" id="p2_altMedOk"
						name="p2_altMedOk" onclick="onCheck(this,'p2_altMed')"
						<%= props.getProperty("p2_altMedOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_altMedNo"
						name="p2_altMedNo" onclick="onCheck(this,'p2_altMed')"
						<%= props.getProperty("p2_altMedNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_2.formAltMed" />*</a></td>
					<td valign="top"><input type="radio" id="p2_pacifierOk"
						name="p2_pacifierOk" onclick="onCheck(this,'p2_pacifier')"
						<%= props.getProperty("p2_pacifierOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_pacifierNo"
						name="p2_pacifierNo" onclick="onCheck(this,'p2_pacifier')"
						<%= props.getProperty("p2_pacifierNo", "") %>></td>
					<td colspan="4" valign="top"><i><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formPacifierUse" />*</a></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_tmpControlOk"
						name="p2_tmpControlOk" onclick="onCheck(this,'p2_tmpControl')"
						<%= props.getProperty("p2_tmpControlOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_tmpControlNo"
						name="p2_tmpControlNo" onclick="onCheck(this,'p2_tmpControl')"
						<%= props.getProperty("p2_tmpControlNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formTempCtrl" />*</a></td>
					<td valign="top"><input type="radio" id="p2_feverOk"
						name="p2_feverOk" onclick="onCheck(this,'p2_fever')"
						<%= props.getProperty("p2_feverOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_feverNo"
						name="p2_feverNo" onclick="onCheck(this,'p2_fever')"
						<%= props.getProperty("p2_feverNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formFever" />*</a></td>
					<td valign="top"><input type="radio" id="p2_sunExposureOk"
						name="p2_sunExposureOk" onclick="onCheck(this,'p2_sunExposure')"
						<%= props.getProperty("p2_sunExposureOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_sunExposureNo"
						name="p2_sunExposureNo" onclick="onCheck(this,'p2_sunExposure')"
						<%= props.getProperty("p2_sunExposureNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSunExposure" />*</a></td>
					<td valign="top"><input type="radio" id="p2_pesticidesOk"
						name="p2_pesticidesOk" onclick="onCheck(this,'p2_pesticides')"
						<%= props.getProperty("p2_pesticidesOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_pesticidesNo"
						name="p2_pesticidesNo" onclick="onCheck(this,'p2_pesticides')"
						<%= props.getProperty("p2_pesticidesNo", "") %>></td>
					<td colspan="4" valign="top"><i><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formPesticides" />*</a></i></td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p2_readingOk"
						name="p2_readingOk" onclick="onCheck(this,'p2_reading')"
						<%= props.getProperty("p2_readingOk", "") %>></td>
                                        <td valign="top"><input type="radio" id="p2_readingNo"
						name="p2_readingNo" onclick="onCheck(this,'p2_reading')"
						<%= props.getProperty("p2_readingNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_2.formReading" />**</a></td>
                                        <td valign="top"><input type="radio" id="p2_noCoughMedOk"
						name="p2_noCoughMedOk" onclick="onCheck(this,'p2_noCoughMed')"
						<%= props.getProperty("p2_noCoughMedOk", "") %>></td>
                                        <td valign="top"><input type="radio" id="p2_noCoughMedNo"
						name="p2_noCoughMedNo" onclick="onCheck(this,'p2_noCoughMed')"
						<%= props.getProperty("p2_noCoughMedNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_1.formCough" />*</a></td>
                                        <td colspan="9">&nbsp;</td>
				<tr>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_2ndSmokeOk,p2_2ndSmokeNo,p2_tmpControlOk,p2_tmpControlNo,p2_readingOk,p2_readingNo');" /></td>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_teethingOk,p2_teethingNo,p2_feverOk,p2_feverNo,p2_noCoughMedOk,p2_noCoughMedNo');" /></td>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_altMedOk,p2_altMedNo,p2_sunExposureOk,p2_sunExposureNo');" /></td>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_pacifierOk,p2_pacifierNo,p2_pesticidesOk,p2_pesticidesNo');" /></td>
					<td colspan="3">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="15">&nbsp;</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgDevelopment" />**</a><br>
			<bean:message
				key="oscarEncounter.formRourke2009_1.msgDevelopmentDesc" /><br />
			<img height="15" width="20" src="graphics/Checkmark_Lwhite.gif"><bean:message
				key="oscarEncounter.formRourke2006_1.msgDevelopmentLegend" /></td>
			<td colspan="3" valign="top" align="center">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="3"><textarea id="p2_development2m"
						name="p2_development2m" rows="5" cols="25" class="wide"><%= props.getProperty("p2_development2m", "") %></textarea></td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_eyesOk"
						name="p2_eyesOk" onclick="onCheck(this,'p2_eyes')"
						<%= props.getProperty("p2_eyesOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_eyesNo"
						name="p2_eyesNo" onclick="onCheck(this,'p2_eyes')"
						<%= props.getProperty("p2_eyesNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_2.formEyesMove" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_eyesOk,p2_eyesNo');" /></td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p2_coosOk"
						name="p2_coosOk" onclick="onCheck(this,'p2_coos')"
						<%= props.getProperty("p2_coosOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_coosNo"
						name="p2_coosNo" onclick="onCheck(this,'p2_coos')"
						<%= props.getProperty("p2_coosNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_2.formCoos" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_coosOk,p2_coosNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_headUpTummyOk"
						name="p2_headUpTummyOk" onclick="onCheck(this,'p2_headUpTummy')"
						<%= props.getProperty("p2_headUpTummyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_headUpTummyNo"
						name="p2_headUpTummyNo" onclick="onCheck(this,'p2_headUpTummy')"
						<%= props.getProperty("p2_headUpTummyNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_2.formHeadUp" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_headUpOk,p2_headUpNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_cuddledOk"
						name="p2_cuddledOk" onclick="onCheck(this,'p2_cuddled')"
						<%= props.getProperty("p2_cuddledOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_cuddledNo"
						name="p2_cuddledNo" onclick="onCheck(this,'p2_cuddled')"
						<%= props.getProperty("p2_cuddledNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_2.formCuddled" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_cuddledOk,p2_cuddledNo');" /></td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p2_2sucksOk"
						name="p2_2sucksOk" onclick="onCheck(this,'p2_2sucks')"
						<%= props.getProperty("p2_2sucksOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_2sucksNo"
						name="p2_2sucksNo" onclick="onCheck(this,'p2_2sucks')"
						<%= props.getProperty("p2_2sucksNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_2.form2sucks" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_2sucksOk,p2_2sucksNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_smilesOk"
						name="p2_smilesOk" onclick="onCheck(this,'p2_smiles')"
						<%= props.getProperty("p2_smilesOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_smilesNo"
						name="p2_smilesNo" onclick="onCheck(this,'p2_smiles')"
						<%= props.getProperty("p2_smilesNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_2.formSmiles" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_smilesOk,p2_smilesNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio"
						id="p2_noParentsConcerns2mOk" name="p2_noParentsConcerns2mOk"
						onclick="onCheck(this,'p2_noParentsConcerns2m')"
						<%= props.getProperty("p2_noParentsConcerns2mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_noParentsConcerns2mNo" name="p2_noParentsConcerns2mNo"
						onclick="onCheck(this,'p2_noParentsConcerns2m')"
						<%= props.getProperty("p2_noParentsConcerns2mNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009.formNoparentConcerns" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_noParentsConcerns2mOk,p2_noParentsConcerns2mNo');" /></td>
					<td>&nbsp;</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top" align="center">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="3"><textarea id="p2_development4m"
						name="p2_development4m" rows="5" cols="25" class="wide"><%= props.getProperty("p2_development4m", "") %></textarea></td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p2_movingObjOk"
						name="p2_movingObjOk" onclick="onCheck(this,'p2_movingObj')"
						<%= props.getProperty("p2_movingObjOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_movingObjNo"
						name="p2_movingObjNo" onclick="onCheck(this,'p2_movingObj')"
						<%= props.getProperty("p2_movingObjNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_2.formMovingObj" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_movingObjOk,p2_movingObjNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_respondsOk"
						name="p2_respondsOk" onclick="onCheck(this,'p2_responds')"
						<%= props.getProperty("p2_respondsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_respondsNo"
						name="p2_respondsNo" onclick="onCheck(this,'p2_responds')"
						<%= props.getProperty("p2_respondsNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_2.formResponds" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_respondsOk,p2_respondsNo');" /></td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p2_headSteadyOk"
						name="p2_headSteadyOk" onclick="onCheck(this,'p2_headSteady')"
						<%= props.getProperty("p2_headSteadyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_headSteadyNo"
						name="p2_headSteadyNo" onclick="onCheck(this,'p2_headSteady')"
						<%= props.getProperty("p2_headSteadyNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_2.formHeadSteady" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_headSteadyOk,p2_headSteadyNo');" /></td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p2_holdsObjOk"
						name="p2_holdsObjOk" onclick="onCheck(this,'p2_holdsObj')"
						<%= props.getProperty("p2_holdsObjOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_holdsObjNo"
						name="p2_holdsObjNo" onclick="onCheck(this,'p2_holdsObj')"
						<%= props.getProperty("p2_holdsObjNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_2.formholdsObj" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_holdsObjOk,p2_holdsObjNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_laughsOk"
						name="p2_laughsOk" onclick="onCheck(this,'p2_laughs')"
						<%= props.getProperty("p2_laughsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_laughsNo"
						name="p2_laughsNo" onclick="onCheck(this,'p2_laughs')"
						<%= props.getProperty("p2_laughsNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_2.formLaughs" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_laughsOk,p2_laughsNo');" /></td>
					<td>&nbsp;</td>
				</tr>							
				<tr>
					<td valign="top"><input type="radio"
						id="p2_noParentsConcerns4mOk" name="p2_noParentsConcerns4mOk"
						onclick="onCheck(this,'p2_noParentsConcerns4m')"
						<%= props.getProperty("p2_noParentsConcerns4mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_noParentsConcerns4mNo" name="p2_noParentsConcerns4mNo"
						onclick="onCheck(this,'p2_noParentsConcerns4m')"
						<%= props.getProperty("p2_noParentsConcerns4mNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009.formNoparentConcerns" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_noParentsConcerns4mOk,p2_noParentsConcerns4mNo');" /></td>
					<td>&nbsp;</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="3"><textarea id="p2_development6m"
						name="p2_development6m" rows="5" cols="25" class="wide"><%= props.getProperty("p2_development6m", "") %></textarea></td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p2_turnsHeadOk"
						name="p2_turnsHeadOk" onclick="onCheck(this,'p2_turnsHead')"
						<%= props.getProperty("p2_turnsHeadOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_turnsHeadNo"
						name="p2_turnsHeadNo" onclick="onCheck(this,'p2_turnsHead')"
						<%= props.getProperty("p2_turnsHeadNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_2.formTurnsHead" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_turnsHeadOk,p2_turnsHeadNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_makesSoundOk"
						name="p2_makesSoundOk" onclick="onCheck(this,'p2_makesSound')"
						<%= props.getProperty("p2_makesSoundOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_makesSoundNo"
						name="p2_makesSoundNo" onclick="onCheck(this,'p2_makesSound')"
						<%= props.getProperty("p2_makesSoundNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_2.formmakesSound" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_makesSoundOk,p2_makesSoundNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_vocalizesOk"
						name="p2_vocalizesOk" onclick="onCheck(this,'p2_vocalizes')"
						<%= props.getProperty("p2_vocalizesOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_vocalizesNo"
						name="p2_vocalizesNo" onclick="onCheck(this,'p2_vocalizes')"
						<%= props.getProperty("p2_vocalizesNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_2.formVocalizes" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_vocalizesOk,p2_vocalizesNo');" /></td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p2_rollsOk"
						name="p2_rollsOk" onclick="onCheck(this,'p2_rolls')"
						<%= props.getProperty("p2_rollsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_rollsNo"
						name="p2_rollsNo" onclick="onCheck(this,'p2_rolls')"
						<%= props.getProperty("p2_rollsNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_2.formRolls" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_rollsOk,p2_rollsNo');" /></td>
				</tr>								
				<tr>
					<td valign="top"><input type="radio" id="p2_sitsOk"
						name="p2_sitsOk" onclick="onCheck(this,'p2_sits')"
						<%= props.getProperty("p2_sitsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_sitsNo"
						name="p2_sitsNo" onclick="onCheck(this,'p2_sits')"
						<%= props.getProperty("p2_sitsNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_2.formSits" /></td>
				</tr>
				<tr>
					<td valign="bottom" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_sitsOk,p2_sitsNo');" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p2_reachesGraspsOk"
						name="p2_reachesGraspsOk" onclick="onCheck(this,'p2_reachesGrasps')"
						<%= props.getProperty("p2_reachesGraspsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p2_reachesGraspsNo"
						name="p2_reachesGraspsNo" onclick="onCheck(this,'p2_reachesGrasps')"
						<%= props.getProperty("p2_reachesGraspsNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_2.formreachesGrasps" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_reachesGraspsOk,p2_reachesGraspsNo');" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio"
						id="p2_noParentsConcerns6mOk" name="p2_noParentsConcerns6mOk"
						onclick="onCheck(this,'p2_noParentsConcerns6m')"
						<%= props.getProperty("p2_noParentsConcerns6mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_noParentsConcerns6mNo" name="p2_noParentsConcerns6mNo"
						onclick="onCheck(this,'p2_noParentsConcerns6m')"
						<%= props.getProperty("p2_noParentsConcerns6mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009.formNoparentConcerns" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_noParentsConcerns6mOk,p2_noParentsConcerns6mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgPhysicalExamination" /></a><br>
			<bean:message
				key="oscarEncounter.formRourke1.msgPhysicalExaminationDesc" /><br>
			<img height="15" width="20" src="graphics/Checkmark_Lwhite.gif"><bean:message
				key="oscarEncounter.formRourke2009.msgPhysicalExaminationLegend" />
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td colspan="3">&nbsp;</td>
				</tr>
                                <tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p2_fontanelles2mOk" name="p2_fontanelles2mOk"
						onclick="onCheck(this,'p2_fontanelles2m')"
						<%= props.getProperty("p2_fontanelles2mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_fontanelles2mNo" name="p2_fontanelles2mNo"
						onclick="onCheck(this,'p2_fontanelles2m')"
						<%= props.getProperty("p2_fontanelles2mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_fontanelles2mOk,p2_fontanelles2mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p2_eyes2mOk" name="p2_eyes2mOk"
						onclick="onCheck(this,'p2_eyes2m')"
						<%= props.getProperty("p2_eyes2mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_eyes2mNo" name="p2_eyes2mNo"
						onclick="onCheck(this,'p2_eyes2m')"
						<%= props.getProperty("p2_eyes2mNo", "") %>></td>
					<td><b><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke1.formRedReflex"/>*</a></b></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_eyes2mOk,p2_eyes2mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p2_corneal2mOk" name="p2_corneal2mOk"
						onclick="onCheck(this,'p2_corneal2m')"
						<%= props.getProperty("p2_corneal2mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_corneal2mNo" name="p2_corneal2mNo"
						onclick="onCheck(this,'p2_corneal2m')"
						<%= props.getProperty("p2_corneal2mNo", "") %>></td>
					<td><b><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formCornealReflex"/>*</a></b></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_corneal2mOk,p2_corneal2mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p2_hearing2mOk" name="p2_hearing2mOk"
						onclick="onCheck(this,'p2_hearing2m')"
						<%= props.getProperty("p2_hearing2mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_hearing2mNo" name="p2_hearing2mNo"
						onclick="onCheck(this,'p2_hearing2m')"
						<%= props.getProperty("p2_hearing2mNo", "") %>></td>
					<td><i><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formHearingInquiry"/>*</a></i></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_hearing2mOk,p2_hearing2mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p2_heart2mOk" name="p2_heart2mOk"
						onclick="onCheck(this,'p2_heart2m')"
						<%= props.getProperty("p2_heart2mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_heart2mNo" name="p2_heart2mNo"
						onclick="onCheck(this,'p2_heart2m')"
						<%= props.getProperty("p2_heart2mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_2.formHeart" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_heart2mOk,p2_heart2mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p2_hips2mOk" name="p2_hips2mOk"
						onclick="onCheck(this,'p2_hips2m')"
						<%= props.getProperty("p2_hips2mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_hips2mNo" name="p2_hips2mNo"
						onclick="onCheck(this,'p2_hips2m')"
						<%= props.getProperty("p2_hips2mNo", "") %>></td>
					<td><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_2.formHips"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_hips2mOk,p2_hips2mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio"
						id="p2_muscleTone2mOk" name="p2_muscleTone2mOk"
						onclick="onCheck(this,'p2_muscleTone2m')"
						<%= props.getProperty("p2_muscleTone2mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_muscleTone2mNo" name="p2_muscleTone2mNo"
						onclick="onCheck(this,'p2_muscleTone2m')"
						<%= props.getProperty("p2_muscleTone2mNo", "") %>></td>
					<td><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formMuscleTone"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_muscleTone2mOk,p2_muscleTone2mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>				
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td colspan="3">&nbsp;</td>
				</tr>
                                 <tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p2_fontanelles4mOk" name="p2_fontanelles4mOk"
						onclick="onCheck(this,'p2_fontanelles4m')"
						<%= props.getProperty("p2_fontanelles4mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_fontanelles4mNo" name="p2_fontanelles4mNo"
						onclick="onCheck(this,'p2_fontanelles4m')"
						<%= props.getProperty("p2_fontanelles4mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_fontanelles4mOk,p2_fontanelles4mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p2_eyes4mOk" name="p2_eyes4mOk"
						onclick="onCheck(this,'p2_eyes4m')"
						<%= props.getProperty("p2_eyes4mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_eyes4mNo" name="p2_eyes4mNo"
						onclick="onCheck(this,'p2_eyes4m')"
						<%= props.getProperty("p2_eyes4mNo", "") %>></td>
					<td><b><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke1.formRedReflex"/>*</a></b></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_eyes4mOk,p2_eyes4mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p2_corneal4mOk" name="p2_corneal4mOk"
						onclick="onCheck(this,'p2_corneal4m')"
						<%= props.getProperty("p2_corneal4mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_corneal4mNo" name="p2_corneal4mNo"
						onclick="onCheck(this,'p2_corneal4m')"
						<%= props.getProperty("p2_corneal4mNo", "") %>></td>
					<td><b><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formCornealReflex"/>*</a></b></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_corneal4mOk,p2_corneal4mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p2_hearing4mOk" name="p2_hearing4mOk"
						onclick="onCheck(this,'p2_hearing4m')"
						<%= props.getProperty("p2_hearing4mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_hearing4mNo" name="p2_hearing4mNo"
						onclick="onCheck(this,'p2_hearing4m')"
						<%= props.getProperty("p2_hearing4mNo", "") %>></td>
					<td><i><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formHearingInquiry"/>*</a></i></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_hearing4mOk,p2_hearing4mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio"
						id="p2_hips4mOk" name="p2_hips4mOk"
						onclick="onCheck(this,'p2_hips4m')"
						<%= props.getProperty("p2_hips4mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_hips4mNo" name="p2_hips4mNo"
						onclick="onCheck(this,'p2_hips4m')"
						<%= props.getProperty("p2_hips4mNo", "") %>></td>
					<td><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_2.formHips"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_hips4mOk,p2_hips4mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio"
						id="p2_muscleTone4mOk" name="p2_muscleTone4mOk"
						onclick="onCheck(this,'p2_muscleTone4m')"
						<%= props.getProperty("p2_muscleTone4mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_muscleTone4mNo" name="p2_muscleTone4mNo"
						onclick="onCheck(this,'p2_muscleTone4m')"
						<%= props.getProperty("p2_muscleTone4mNo", "") %>></td>
					<td><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formMuscleTone"/>*</a></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_muscleTone4mOk,p2_muscleTone4mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td colspan="3">&nbsp;</td>
				</tr>
                                 <tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p2_fontanelles6mOk" name="p2_fontanelles6mOk"
						onclick="onCheck(this,'p2_fontanelles6m')"
						<%= props.getProperty("p2_fontanelles6mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_fontanelles6mNo" name="p2_fontanelles6mNo"
						onclick="onCheck(this,'p2_fontanelles6m')"
						<%= props.getProperty("p2_fontanelles6mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_fontanelles6mOk,p2_fontanelles6mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p2_eyes6mOk" name="p2_eyes6mOk"
						onclick="onCheck(this,'p2_eyes6m')"
						<%= props.getProperty("p2_eyes6mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_eyes6mNo" name="p2_eyes6mNo"
						onclick="onCheck(this,'p2_eyes6m')"
						<%= props.getProperty("p2_eyes6mNo", "") %>></td>
					<td><b><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke1.formRedReflex"/>*</a></b></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_eyes6mOk,p2_eyes6mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p2_corneal6mOk" name="p2_corneal6mOk"
						onclick="onCheck(this,'p2_corneal6m')"
						<%= props.getProperty("p2_corneal6mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_corneal6mNo" name="p2_corneal6mNo"
						onclick="onCheck(this,'p2_corneal6m')"
						<%= props.getProperty("p2_corneal6mNo", "") %>></td>
					<td><b><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formCornealReflex"/>*</a></b></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_corneal6mOk,p2_corneal6mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p2_hearing6mOk" name="p2_hearing6mOk"
						onclick="onCheck(this,'p2_hearing6m')"
						<%= props.getProperty("p2_hearing6mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_hearing6mNo" name="p2_hearing6mNo"
						onclick="onCheck(this,'p2_hearing6m')"
						<%= props.getProperty("p2_hearing6mNo", "") %>></td>
					<td><i><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formHearingInquiry"/>*</a></i></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_hearing6mOk,p2_hearing6mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio"
						id="p2_hips6mOk" name="p2_hips6mOk"
						onclick="onCheck(this,'p2_hips6m')"
						<%= props.getProperty("p2_hips6mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_hips6mNo" name="p2_hips6mNo"
						onclick="onCheck(this,'p2_hips6m')"
						<%= props.getProperty("p2_hips6mNo", "") %>></td>
					<td><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_2.formHips"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_hips6mOk,p2_hips6mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio"
						id="p2_muscleTone6mOk" name="p2_muscleTone6mOk"
						onclick="onCheck(this,'p2_muscleTone6m')"
						<%= props.getProperty("p2_muscleTone6mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p2_muscleTone6mNo" name="p2_muscleTone6mNo"
						onclick="onCheck(this,'p2_muscleTone6m')"
						<%= props.getProperty("p2_muscleTone6mNo", "") %>></td>
					<td><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                                  onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formMuscleTone"/>*</a></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p2_muscleTone6mOk,p2_muscleTone6mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgProblemsAndPlans" /></a></td>
			<td colspan="3" valign="top"><textarea id="p2_problems2m"
				name="p2_problems2m" rows="5" cols="25" class="wide"><%= props.getProperty("p2_problems2m", "") %></textarea></td>
			
			<td colspan="3" valign="top"><textarea id="p2_problems4m"
				name="p2_problems4m" rows="5" cols="25" class="wide"><%= props.getProperty("p2_problems4m", "") %></textarea></td>
			
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea id="p2_problems6m"
						name="p2_problems6m" rows="5" cols="25" class="wide"><%= props.getProperty("p2_problems6m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_tb6m" <%= props.getProperty("p2_tb6m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke2006_2.formTB" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgImmunization" /></a><br>
			<bean:message key="oscarEncounter.formRourke1.msgImmunizationDesc" />
			</td>
			<td style="text-align: center" colspan="3" valign="top"><b><bean:message
				key="oscarEncounter.formRourke2006_1.msgImmunizationColTitle" /></b></td>
			<td style="text-align: center" colspan="3" valign="top"><b><bean:message
				key="oscarEncounter.formRourke2006_1.msgImmunizationColTitle" /></b></td>
			
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
						name="p2_hepatitisVaccine6m"
						<%= props.getProperty("p2_hepatitisVaccine6m", "") %>></td>
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
				style="width: 100%" name="p2_signature2m"
				value="<%= props.getProperty("p2_signature2m", "") %>" /></td>
			<td colspan="3"><input type="text" class="wide" maxlength="42"
				style="width: 100%" name="p2_signature4m"
				value="<%= props.getProperty("p2_signature4m", "") %>" /></td>
			<td colspan="3"><input type="text" class="wide"
				style="width: 100%" name="p2_signature6m"
				value="<%= props.getProperty("p2_signature6m", "") %>" /></td>
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
				onclick="javascript:return popPage('http://rourkebabyrecord.ca','About Rourke');" />
			</td>
			<td align="center" width="100%">
			<% if(formId > 0)
           { %> <a name="length" href="#"
				onclick="onGraph('<%=request.getContextPath()%>/form/formname.do?submit=graph&form_class=Rourke2009&__title=Baby+Growth+Graph1&__cfgfile=<%=growthCharts[0]%>&demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');return false;">
			<bean:message key="oscarEncounter.formRourke1.btnGraphLenghtWeight" /></a><br>
			<a name="headCirc" href="#"
				onclick="onGraph('<%=request.getContextPath()%>/form/formname.do?submit=graph&form_class=Rourke2009&__title=Baby+Head+Circumference&__cfgfile=<%=growthCharts[1]%>&demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');return false;">
			<bean:message key="oscarEncounter.formRourke1.btnGraphHead" /></a> <% }else { %>
			&nbsp; <% } %>
			</td>
			<td nowrap="true"><a
				href="formrourke2009p1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg1" /></a>&nbsp;|&nbsp; <a><bean:message
				key="oscarEncounter.formRourke2006.Pg2" /></a>&nbsp;|&nbsp; <a
				href="formrourke2009p3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg3" /></a>&nbsp;|&nbsp; <a
				href="formrourke2009p4.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
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
    Calendar.setup({ inputField : "p2_date2m", ifFormat : "%d/%m/%Y", showsTime :false, button : "p2_date2m_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "p2_date4m", ifFormat : "%d/%m/%Y", showsTime :false, button : "p2_date4m_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "p2_date6m", ifFormat : "%d/%m/%Y", showsTime :false, button : "p2_date6m_cal", singleClick : true, step : 1 });
</script>
</html:html>
