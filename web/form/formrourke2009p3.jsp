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

<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
    String formClass = "Rourke2009";
    String formLink = "formrourke2009p3.jsp";

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
    props.setProperty("c_lastVisited", "p3");

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
<title>Rourke2009 Record 3</title>
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
        var txtConcern9m = new ylib.widget.TextCounter("p3_pConcern9m", 195,5);
        var txtConcern12m = new ylib.widget.TextCounter("p3_pConcern12m", 245,5);
        var txtConcern15m = new ylib.widget.TextCounter("p3_pConcern15m", 205,5);
        var txtNutrition12m = new ylib.widget.TextCounter("p3_nutrition12m", 245,5);
        var txtNutrition15m = new ylib.widget.TextCounter("p3_nutrition15m", 245,6);
        var txtDevelopment9m = new ylib.widget.TextCounter("p3_development9m", 90,2);
        var txtDevelopment12m = new ylib.widget.TextCounter("p3_development12m", 170,4);
        var txtDevelopment15m = new ylib.widget.TextCounter("p3_development15m", 208,4);
        var txtProblems9m = new ylib.widget.TextCounter("p3_problems9m", 117,3);
        var txtProblems12m = new ylib.widget.TextCounter("p3_problems12m", 208,4);        
        var txtProblems15m = new ylib.widget.TextCounter("p3_problems15m", 190,5);
        
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
    
    function showNotes() {
        var frm = document.getElementById("frmPopUp");
        frm.action = "Rourke2009_Notes.pdf";
        frm.submit();
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
        measurements[0] = new Array("p3_ht9m", "p3_wt9m", "p3_hc9m");
        measurements[1] = new Array("p3_ht12m", "p3_wt12m", "p3_hc12m");
        measurements[2] = new Array("p3_ht15m", "p3_wt15m", "p3_hc15m");
        var dates = new Array("p3_date9m", "p3_date12m", "p3_date15m");
                    
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
                
        document.forms[0].action = "../form/createpdf?__title=Rourke+Baby+Report+Pg3&__cfgfile=rourke2009printCfgPg3&__template=rourke2009p3";
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
		value=<%=props.getProperty("c_lastVisited", "p3")%> />
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
				onclick="onGraph('<%=request.getContextPath()%>/form/formname.do?submit=graph&form_class=Rourke2009&__title=Baby+Growth+Graph1&__cfgfile=<%=growthCharts[0]%>&demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>'); return false;">
			<bean:message key="oscarEncounter.formRourke1.btnGraphLenghtWeight" /></a><br>
			<a name="headCirc" href="#"
				onclick="onGraph('<%=request.getContextPath()%>/form/formname.do?submit=graph&form_class=Rourke2009&__title=Baby+Head+Circumference&__cfgfile=<%=growthCharts[1]%>&demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>'); return false;">
			<bean:message key="oscarEncounter.formRourke1.btnGraphHead" /></a> <% }else { %>
			&nbsp; <% } %>
			</td>
			<td nowrap="true"><a
				href="formrourke2009p1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg1" /></a>&nbsp;|&nbsp; <a
				href="formrourke2009p2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg2" /></a>&nbsp;|&nbsp; <a><bean:message
				key="oscarEncounter.formRourke2006.Pg3" /></a>&nbsp;|&nbsp; <a
				href="formrourke2009p4.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg4" /></a></td>
		</tr>
	</table>

	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr class="titleBar">
			<th><bean:message
				key="oscarEncounter.formRourke2006_3.msgRourkeBabyRecord" /></th>
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
				key="oscarEncounter.formRourke2006_3.msg9mos" /></a></td>
			<td colspan="3" class="row"><a><bean:message
				key="oscarEncounter.formRourke2006_3.msg12mos" /></a></td>
			<td colspan="3" class="row"><a><bean:message
				key="oscarEncounter.formRourke2006_3.msg15mos" /></a></td>
		</tr>
		<tr align="center">
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgDate" /></a></td>
			<td colspan="3"><input readonly type="text" id="p3_date9m"
				name="p3_date9m" ondblclick="resetDate(this)" size="10"
				value="<%=UtilMisc.htmlEscape(props.getProperty("p3_date9m", ""))%>" />
			<img src="../images/cal.gif" id="p3_date9m_cal"></td>
			<td colspan="3"><input readonly type="text" id="p3_date12m"
				name="p3_date12m" ondblclick="resetDate(this)" size="10"
				value="<%=UtilMisc.htmlEscape(props.getProperty("p3_date12m", ""))%>" />
			<img src="../images/cal.gif" id="p3_date12m_cal"></td>
			<td colspan="3"><input readonly type="text" id="p3_date15m"
				name="p3_date15m" ondblclick="resetDate(this)" size="10"
				value="<%=UtilMisc.htmlEscape(props.getProperty("p3_date15m", ""))%>" />
			<img src="../images/cal.gif" id="p3_date15m_cal"></td>
		</tr>
		<tr align="center">
			<td class="column" rowspan="2"><a><bean:message
				key="oscarEncounter.formRourke1.btnGrowth" />*</a><br>
                            <bean:message
				key="oscarEncounter.formRourke2009_1.btnGrowthmsg"/>
                        </td>
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formWt" /></td>
			<td><bean:message
				key="oscarEncounter.formRourke2006_3.formHdCirc" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message
				key="oscarEncounter.formRourke2006_3.formWt12m" /></td>
			<td><bean:message
				key="oscarEncounter.formRourke2006_3.formHdCirc12m" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formWt" /></td>
			<td><bean:message
				key="oscarEncounter.formRourke2006_3.formHdCirc" /></td>
		</tr>
		<tr align="center">
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p3_ht9m" size="4"
				maxlength="5" value="<%= props.getProperty("p3_ht9m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="wtEnglish2Metric(this);" name="p3_wt9m" size="4"
				maxlength="5" value="<%= props.getProperty("p3_wt9m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p3_hc9m" size="4"
				maxlength="5" value="<%= props.getProperty("p3_hc9m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p3_ht12m" size="4"
				maxlength="5" value="<%= props.getProperty("p3_ht12m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="wtEnglish2Metric(this);" name="p3_wt12m" size="4"
				maxlength="5" value="<%= props.getProperty("p3_wt12m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p3_hc12m" size="4"
				maxlength="5" value="<%= props.getProperty("p3_hc12m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p3_ht15m" size="4"
				maxlength="5" value="<%= props.getProperty("p3_ht15m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="wtEnglish2Metric(this);" name="p3_wt15m" size="4"
				maxlength="5" value="<%= props.getProperty("p3_wt15m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p3_hc15m" size="4"
				maxlength="5" value="<%= props.getProperty("p3_hc15m", "") %>"></td>
		</tr>
		<tr align="center">
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.formParentalConcerns" /></a></td>
			<td colspan="3"><textarea id="p3_pConcern9m"
				name="p3_pConcern9m" class="wide" cols="10" rows="5"><%= props.getProperty("p3_pConcern9m", "") %></textarea>
			</td>
			<td colspan="3"><textarea id="p3_pConcern12m"
				name="p3_pConcern12m" class="wide" cols="10" rows="5"><%= props.getProperty("p3_pConcern12m", "") %></textarea>
			</td>
			<td colspan="3"><textarea id="p3_pConcern15m"
				name="p3_pConcern15m" class="wide" cols="10" rows="5"><%= props.getProperty("p3_pConcern15m", "") %></textarea>
			</td>
		</tr>
		<tr align="center">

			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgNutrition" />*:</a></td>

			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
                                            <td style="padding-right: 5pt" valign="top"><img height="15"
                                                        width="20" src="graphics/Checkmark_L.gif"></td>
                                            <td class="edcol" valign="top">X</td>
                                            <td>&nbsp;</td>
                                </tr>
                                <tr>
                                        <td valign="top"><input type="radio" id="p3_breastFeeding9mOk"
						name="p3_breastFeeding9mOk" onclick="onCheck(this,'p3_breastFeeding9m')"
						<%= props.getProperty("p3_breastFeeding9mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_breastFeeding9mNo"
						name="p3_breastFeeding9mNo" onclick="onCheck(this,'p3_breastFeeding9m')"
						<%= props.getProperty("p3_breastFeeding9mNo", "") %>></td>
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
                                        <td valign="top"><input type="radio" id="p3_formulaFeeding9mOk"
						name="p3_formulaFeeding9mOk" onclick="onCheck(this,'p3_formulaFeeding9m')"
						<%= props.getProperty("p3_formulaFeeding9mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_formulaFeeding9mNo"
						name="p3_formulaFeeding9mNo" onclick="onCheck(this,'p3_formulaFeeding9m')"
						<%= props.getProperty("p3_formulaFeeding9mNo", "") %>></td>					
					<td><i><bean:message
						key="oscarEncounter.formRourke2009_3.msgFormulaFeeding" /></i></td>
				</tr>
				<tr>
                                        <td valign="top"><input type="radio" id="p3_bottle9mOk"
						name="p3_bottle9mOk" onclick="onCheck(this,'p3_bottle9m')"
						<%= props.getProperty("p3_bottle9mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_bottle9mNo"
						name="p3_bottle9mNo" onclick="onCheck(this,'p3_bottle9m')"
						<%= props.getProperty("p3_bottle9mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_2.msgBottle" /></td>
				</tr>
				<tr>
                                        <td valign="top"><input type="radio" id="p3_liquids9mOk"
						name="p3_liquids9mOk" onclick="onCheck(this,'p3_liquids9m')"
						<%= props.getProperty("p3_liquids9mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_liquids9mNo"
						name="p3_liquids9mNo" onclick="onCheck(this,'p3_liquids9m')"
						<%= props.getProperty("p3_liquids9mNo", "") %>></td>					
					<td><bean:message
						key="oscarEncounter.formRourke2009_2.msgLiquids" /></td>
				</tr>
				<tr>
                                        <td valign="top"><input type="radio" id="p3_cereal9mOk"
						name="p3_cereal9mOk" onclick="onCheck(this,'p3_cereal9m')"
						<%= props.getProperty("p3_cereal9mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_cereal9mNo"
						name="p3_cereal9mNo" onclick="onCheck(this,'p3_cereal9m')"
						<%= props.getProperty("p3_cereal9mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_3.msgCereal" /></td>
				</tr>
				<tr>
                                        <td valign="top"><input type="radio" id="p3_introCowMilk9mOk"
						name="p3_introCowMilk9mOk" onclick="onCheck(this,'p3_introCowMilk9m')"
						<%= props.getProperty("p3_cereal9mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_introCowMilk9mNo"
						name="p3_introCowMilk9mNo" onclick="onCheck(this,'p3_introCowMilk9m')"
						<%= props.getProperty("p3_introCowMilk9mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_3.msgIntroCowMilk" /></td>
				</tr>
				<tr>
                                        <td valign="top"><input type="radio" id="p3_egg9mOk"
						name="p3_egg9mOk" onclick="onCheck(this,'p3_egg9m')"
						<%= props.getProperty("p3_egg9mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_egg9mNo"
						name="p3_egg9mNo" onclick="onCheck(this,'p3_egg9m')"
						<%= props.getProperty("p3_egg9mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_2.msgEggWhites" /></td>
				</tr>
				<tr>    
                                    <td valign="top"><input type="radio" id="p3_choking9mOk"
						name="p3_choking9mOk" onclick="onCheck(this,'p3_choking9m')"
						<%= props.getProperty("p3_choking9mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_choking9mNo"
						name="p3_choking9mNo" onclick="onCheck(this,'p3_choking9m')"
						<%= props.getProperty("p3_choking9mNo", "") %>></td>
					<td><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.msgChoking" />*</a></td>
				</tr>
                                <tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_breastFeeding9mOk,p3_breastFeeding9mNo,p3_formulaFeeding9mOk,p3_formulaFeeding9mNo,p3_bottle9mOk,p3_bottle9mNo,p3_liquids9mOk,p3_liquids9mNo,p3_cereal9mOk,p3_cereal9mNo,p3_introCowMilk9mOk,p3_introCowMilk9mNo,p3_egg9mOk,p3_egg9mNo,p3_choking9mOk,p3_choking9mNo');" /></td>
                                </tr>
			</table>

			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="3"><textarea id="p3_nutrition12m"
						name="p3_nutrition12m" class="wide" rows="5" cols="25"><%= props.getProperty("p3_nutrition12m", "") %></textarea></td>
				</tr>
                                <tr>
                                            <td style="padding-right: 5pt" valign="top"><img height="15"
                                                        width="20" src="graphics/Checkmark_L.gif"></td>
                                            <td class="edcol" valign="top">X</td>
                                            <td>&nbsp;</td>
                                </tr>
                                <tr>
                                        <td valign="top"><input type="radio" id="p3_breastFeeding12mOk"
						name="p3_breastFeeding12mOk" onclick="onCheck(this,'p3_breastFeeding12m')"
						<%= props.getProperty("p3_breastFeeding12mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_breastFeeding12mNo"
						name="p3_breastFeeding12mNo" onclick="onCheck(this,'p3_breastFeeding12m')"
						<%= props.getProperty("p3_breastFeeding12mNo", "") %>></td>
					<td><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.btnBreastFeeding" />*</a></b></td>
				</tr>
				<tr>
                                        <td valign="top"><input type="radio" id="p3_homoMilk12mOk"
						name="p3_homoMilk12mOk" onclick="onCheck(this,'p3_homoMilk12m')"
						<%= props.getProperty("p3_homoMilk12mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_homoMilk12mNo"
						name="p3_homoMilk12mNo" onclick="onCheck(this,'p3_homoMilk12m')"
						<%= props.getProperty("p3_homoMilk12mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formHomogenizedMilk" /></td>
				</tr>
				<tr>
                                        <td valign="top"><input type="radio" id="p3_cup12mOk"
						name="p3_cup12mOk" onclick="onCheck(this,'p3_cup12m')"
						<%= props.getProperty("p3_cup12mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_cup12mNo"
						name="p3_cup12mNo" onclick="onCheck(this,'p3_cup12m')"
						<%= props.getProperty("p3_cup12mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_3.formEncourageCup" /></td>
				</tr>
				<tr>
                                        <td valign="top"><input type="radio" id="p3_appetite12mOk"
						name="p3_appetite12mOk" onclick="onCheck(this,'p3_appetite12m')"
						<%= props.getProperty("p3_appetite12mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_appetite12mNo"
						name="p3_appetite12mNo" onclick="onCheck(this,'p3_appetite12m')"
						<%= props.getProperty("p3_appetite12mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formAppetiteReduced" /></td>
				</tr>
				<tr>
                                        <td valign="top"><input type="radio" id="p3_choking12mOk"
						name="p3_choking12mOk" onclick="onCheck(this,'p3_choking12m')"
						<%= props.getProperty("p3_choking12mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_choking12mNo"
						name="p3_choking12mNo" onclick="onCheck(this,'p3_choking12m')"
						<%= props.getProperty("p3_choking12mNo", "") %>></td>
					<td><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.msgChoking" />*</a></td>
				</tr>
                                <tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_breastFeeding12mOk,p3_breastFeeding12mNo,p3_homoMilk12mOk,p3_homoMilk12mNo,p3_cup12mOk,p3_cup12mNo,p3_appetite12mOk,p3_appetite12mNo,p3_choking12mOk,p3_choking12mNo');" /></td>
                                </tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%" height="100%">
				<tr align="center">
					<td colspan="3"><textarea id="p3_nutrition15m"
						name="p3_nutrition15m" class="wide" rows="5" cols="25"><%= props.getProperty("p3_nutrition15m", "") %></textarea></td>
				</tr>
                                <tr>
                                            <td style="padding-right: 5pt" valign="top"><img height="15"
                                                        width="20" src="graphics/Checkmark_L.gif"></td>
                                            <td class="edcol" valign="top">X</td>
                                            <td>&nbsp;</td>
                                </tr>
                                <tr>
                                        <td valign="top"><input type="radio" id="p3_breastFeeding15mOk"
						name="p3_breastFeeding15mOk" onclick="onCheck(this,'p3_breastFeeding15m')"
						<%= props.getProperty("p3_breastFeeding15mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_breastFeeding15mNo"
						name="p3_breastFeeding15mNo" onclick="onCheck(this,'p3_breastFeeding15m')"
						<%= props.getProperty("p3_breastFeeding15mNo", "") %>></td>
					<td><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.btnBreastFeeding" />*</a></b></td>
				</tr>
				<tr>
                                        <td valign="top"><input type="radio" id="p3_homoMilk15mOk"
						name="p3_homoMilk15mOk" onclick="onCheck(this,'p3_homoMilk15m')"
						<%= props.getProperty("p3_homoMilk15mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_homoMilk15mNo"
						name="p3_homoMilk15mNo" onclick="onCheck(this,'p3_homoMilk15m')"
						<%= props.getProperty("p3_homoMilk15mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formHomogenizedMilk" /></td>
				</tr>
				<tr>
                                        <td valign="top"><input type="radio" id="p3_choking15mOk"
						name="p3_choking15mOk" onclick="onCheck(this,'p3_choking15m')"
						<%= props.getProperty("p3_choking15mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_choking15mNo"
						name="p3_choking15mNo" onclick="onCheck(this,'p3_choking15m')"
						<%= props.getProperty("p3_choking15mNo", "") %>></td>
					<td><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.msgChoking" />*</a></td>
				</tr>
				<tr>
                                        <td valign="top"><input type="radio" id="p3_cup15mOk"
						name="p3_cup15mOk" onclick="onCheck(this,'p3_cup15m')"
						<%= props.getProperty("p3_cup15mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_cup15mNo"
						name="p3_cup15mNo" onclick="onCheck(this,'p3_cup15m')"
						<%= props.getProperty("p3_cup15mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_3.formEncourageCup" /></td>
				</tr>
                                <tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_breastFeeding15mOk,p3_breastFeeding15mNo,p3_homoMilk15mOk,p3_homoMilk15mNo,p3_cup15mOk,p3_cup15mNo,p3_choking15mOk,p3_choking15mNo');" /></td>
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
					<td valign="top"><input type="radio" id="p3_carSeatOk"
						name="p3_carSeatOk" onclick="onCheck(this,'p3_carSeat')"
						<%= props.getProperty("p3_carSeatOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_carSeatNo"
						name="p3_carSeatNo" onclick="onCheck(this,'p3_carSeat')"
						<%= props.getProperty("p3_carSeatNo", "") %>></td>
					<td colspan="4" valign="top"><b><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_3.formCarSeat" /></a>*</b></td>
					<td valign="top"><input type="radio" id="p3_poisonsOk"
						name="p3_poisonsOk" onclick="onCheck(this,'p3_poisons')"
						<%= props.getProperty("p3_poisonsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_poisonsNo"
						name="p3_poisonsNo" onclick="onCheck(this,'p3_poisons')"
						<%= props.getProperty("p3_poisonsNo", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formPoisons" /></a></b></td>
					<td valign="top"><input type="radio" id="p3_firearmSafetyOk"
						name="p3_firearmSafetyOk"
						onclick="onCheck(this,'p3_firearmSafety')"
						<%= props.getProperty("p3_firearmSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_firearmSafetyNo"
						name="p3_firearmSafetyNo"
						onclick="onCheck(this,'p3_firearmSafety')"
						<%= props.getProperty("p3_firearmSafetyNo", "") %>></td>
					<td colspan="4" valign="top"><b><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formFireArm" />*</a></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_smokeSafetyOk"
						name="p3_smokeSafetyOk" onclick="onCheck(this,'p3_smokeSafety')"
						<%= props.getProperty("p3_smokeSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_smokeSafetyNo"
						name="p3_smokeSafetyNo" onclick="onCheck(this,'p3_smokeSafety')"
						<%= props.getProperty("p3_smokeSafetyNo", "") %>></td>
					<td colspan="4" valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSmokeSafety" />*</a></td>
					<td valign="top"><input type="radio" id="p3_hotWaterOk"
						name="p3_hotWaterOk" onclick="onCheck(this,'p3_hotWater')"
						<%= props.getProperty("p3_hotWaterOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_hotWaterNo"
						name="p3_hotWaterNo" onclick="onCheck(this,'p3_hotWater')"
						<%= props.getProperty("p3_hotWaterNo", "") %>></td>
					<td valign="top"><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formHotWater" />*</a></i></td>
					<td colspan="6">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3" valign="top"><bean:message
						key="oscarEncounter.formRourke2006_3.formChildProof" />
					<td valign="top"><input type="radio" id="p3_electricOk"
						name="p3_electricOk" onclick="onCheck(this,'p3_electric')"
						<%= props.getProperty("p3_electricOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_electricNo"
						name="p3_electricNo" onclick="onCheck(this,'p3_electric')"
						<%= props.getProperty("p3_electricNo", "") %>></td>
					<td valign="top"><i><bean:message
						key="oscarEncounter.formRourke2006_2.formElectric" /></i></td>
					<td valign="top"><input type="radio" id="p3_fallsOk"
						name="p3_fallsOk" onclick="onCheck(this,'p3_falls')"
						<%= props.getProperty("p3_fallsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_fallsNo"
						name="p3_fallsNo" onclick="onCheck(this,'p3_falls')"
						<%= props.getProperty("p3_fallsNo", "") %>></td>
					<td valign="top"><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formFalls" />*</a></i></td>
					<td valign="top"><input type="radio" id="p3_safeToysOk"
						name="p3_safeToysOk" onclick="onCheck(this,'p3_safeToys')"
						<%= props.getProperty("p3_safeToysOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_safeToysNo"
						name="p3_safeToysNo" onclick="onCheck(this,'p3_safeToys')"
						<%= props.getProperty("p3_safeToysNo", "") %>></td>
					<td colspan="4" valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSafeToys" />*</a></td>
				</tr>
				<tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_carSeatOk,p3_carSeatNo,p3_smokeSafetyOk,p3_smokeSafetyNo');" /></td>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_electricOk,p3_electricNo');" /></td>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_poisonsOk,p3_poisonsNo,p3_hotWaterOk,p3_hotWaterNo,p3_fallsOk,p3_fallsNo');" /></td>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_firearmSafetyOk,p3_firearmSafetyNo,p3_safeToysOk,p3_safeToysNo');" /></td>
				</tr>
				<tr>
					<td colspan="15">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="15"><bean:message
						key="oscarEncounter.formRourke2006_1.formBehaviour" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_sleepCryOk"
						name="p3_sleepCryOk" onclick="onCheck(this,'p3_sleepCry')"
						<%= props.getProperty("p3_sleepCryOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_sleepCryNo"
						name="p3_sleepCryNo" onclick="onCheck(this,'p3_sleepCry')"
						<%= props.getProperty("p3_sleepCryNo", "") %>></td>
					<td colspan="4" valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formsleepCry" />**</a></td>
					<td valign="top"><input type="radio" id="p3_soothabilityOk"
						name="p3_soothabilityOk" onclick="onCheck(this,'p3_soothability')"
						<%= props.getProperty("p3_soothabilityOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_soothabilityNo"
						name="p3_soothabilityNo" onclick="onCheck(this,'p3_soothability')"
						<%= props.getProperty("p3_soothabilityNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formSoothability" /></td>
					<td valign="top"><input type="radio" id="p3_homeVisitOk"
						name="p3_homeVisitOk" onclick="onCheck(this,'p3_homeVisit')"
						<%= props.getProperty("p3_homeVisitOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_homeVisitNo"
						name="p3_homeVisitNo" onclick="onCheck(this,'p3_homeVisit')"
						<%= props.getProperty("p3_homeVisitNo", "") %>></td>
					<td colspan="4" valign="top"><b><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_3.formHomeVisit" />**</a></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_parentingOk"
						name="p3_parentingOk" onclick="onCheck(this,'p3_parenting')"
						<%= props.getProperty("p3_parentingOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_parentingNo"
						name="p3_parentingNo" onclick="onCheck(this,'p3_parenting')"
						<%= props.getProperty("p3_parentingNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_3.formParenting" /></td>
					<td valign="top"><input type="radio" id="p3_pFatigueOk"
						name="p3_pFatigueOk" onclick="onCheck(this,'p3_pFatigue')"
						<%= props.getProperty("p3_pFatigueOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_pFatigueNo"
						name="p3_pFatigueNo" onclick="onCheck(this,'p3_pFatigue')"
						<%= props.getProperty("p3_pFatigueNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formParentFatigue" />**</a></td>
					<td valign="top"><input type="radio" id="p3_famConflictOk"
						name="p3_famConflictOk" onclick="onCheck(this,'p3_famConflict')"
						<%= props.getProperty("p3_famConflictOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_famConflictNo"
						name="p3_famConflictNo" onclick="onCheck(this,'p3_famConflict')"
						<%= props.getProperty("p3_famConflictNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formFamConflict" /></td>
					<td valign="top"><input type="radio" id="p3_siblingsOk"
						name="p3_siblingsOk" onclick="onCheck(this,'p3_siblings')"
						<%= props.getProperty("p3_siblingsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_siblingsNo"
						name="p3_siblingsNo" onclick="onCheck(this,'p3_siblings')"
						<%= props.getProperty("p3_siblingsNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formSiblings" /></td>
					<td valign="top"><input type="radio" id="p3_childCareOk"
						name="p3_childCareOk" onclick="onCheck(this,'p3_childCare')"
						<%= props.getProperty("p3_childCareOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_childCareNo"
						name="p3_childCareNo" onclick="onCheck(this,'p3_childCare')"
						<%= props.getProperty("p3_childCareNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_2.formChildCare" /></td>
				</tr>
				<tr>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_sleepCryOk,p3_sleepCryNo,p3_parentingOk,p3_parentingNo');" /></td>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_pFatigueOk,p3_pFatigueNo');" /></td>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_soothabilityOk,p3_soothabilityNo,p3_famConflictOk,p3_famConflictNo');" /></td>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_homeVisitOk,p3_homeVisitNo,p3_siblingsOk,p3_siblingsNo');" /></td>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_childCareOk,p3_childCareNo');" /></td>
				</tr>
				<tr>
					<td colspan="15">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="15"><bean:message
						key="oscarEncounter.formRourke2006_1.formOtherIssues" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_2ndSmokeOk"
						name="p3_2ndSmokeOk" onclick="onCheck(this,'p3_2ndSmoke')"
						<%= props.getProperty("p3_2ndSmokeOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_2ndSmokeNo"
						name="p3_2ndSmokeNo" onclick="onCheck(this,'p3_2ndSmoke')"
						<%= props.getProperty("p3_2ndSmokeNo", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.formSecondHandSmoke" />*</a></b></td>
					<td valign="top"><input type="radio" id="p3_teethingOk"
						name="p3_teethingOk" onclick="onCheck(this,'p3_teething')"
						<%= props.getProperty("p3_teethingOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_teethingNo"
						name="p3_teethingNo" onclick="onCheck(this,'p3_teething')"
						<%= props.getProperty("p3_teethingNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formTeething" />*</a></td>
					<td valign="top"><input type="radio" id="p3_altMedOk"
						name="p3_altMedOk" onclick="onCheck(this,'p3_altMed')"
						<%= props.getProperty("p3_altMedOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_altMedNo"
						name="p3_altMedNo" onclick="onCheck(this,'p3_altMed')"
						<%= props.getProperty("p3_altMedNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formAltMed" />*</a></td>
					<td valign="top"><input type="radio" id="p3_pacifierOk"
						name="p3_pacifierOk" onclick="onCheck(this,'p3_pacifier')"
						<%= props.getProperty("p3_pacifierOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_pacifierNo"
						name="p3_pacifierNo" onclick="onCheck(this,'p3_pacifier')"
						<%= props.getProperty("p3_pacifierNo", "") %>></td>
					<td colspan="4" valign="top"><i><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formPacifierUse" />*</a></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_feverOk"
						name="p3_feverOk" onclick="onCheck(this,'p3_fever')"
						<%= props.getProperty("p3_feverOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_feverNo"
						name="p3_feverNo" onclick="onCheck(this,'p3_fever')"
						<%= props.getProperty("p3_feverNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formFever" />*</a></td>
					<td valign="top"><input type="radio" id="p3_activeOk"
						name="p3_activeOk" onclick="onCheck(this,'p3_active')"
						<%= props.getProperty("p3_activeOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_activeNo"
						name="p3_activeNo" onclick="onCheck(this,'p3_active')"
						<%= props.getProperty("p3_activeNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_3.formactiveLife" />*</a></td>
					<td valign="top"><input type="radio" id="p3_readingOk"
						name="p3_readingOk" onclick="onCheck(this,'p3_reading')"
						<%= props.getProperty("p3_readingOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_readingNo"
						name="p3_readingNo" onclick="onCheck(this,'p3_reading')"
						<%= props.getProperty("p3_readingNo", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formEncourageReading" />**</a></b></td>
					<td valign="top"><input type="radio" id="p3_footwearOk"
						name="p3_footwearOk" onclick="onCheck(this,'p3_footwear')"
						<%= props.getProperty("p3_footwearOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_footwearNo"
						name="p3_footwearNo" onclick="onCheck(this,'p3_footwear')"
						<%= props.getProperty("p3_footwearNo", "") %>></td>
					<td colspan="4" valign="top"><bean:message
						key="oscarEncounter.formRourke2006_3.formFootwear" /></td>
				</tr>
                                <tr>
                                        <td valign="top"><input type="radio" id="p3_coughMedOk"
						name="p3_coughMedOk" onclick="onCheck(this,'p3_coughMed')"
						<%= props.getProperty("p3_coughMedOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_coughMedNo"
						name="p3_coughMedNo" onclick="onCheck(this,'p3_coughMed')"
						<%= props.getProperty("p3_coughMedNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_1.formCough" />*</a></td>
                                        <td colspan="12">&nbsp;</td>
                                </tr>
				<tr>
					<td colspan="3" valign="top"><bean:message
						key="oscarEncounter.formRourke2006_3.formEnvHealth" /></td>
					<td valign="top"><input type="radio" id="p3_sunExposureOk"
						name="p3_sunExposureOk" onclick="onCheck(this,'p3_sunExposure')"
						<%= props.getProperty("p3_sunExposureOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_sunExposureNo"
						name="p3_sunExposureNo" onclick="onCheck(this,'p3_sunExposure')"
						<%= props.getProperty("p3_sunExposureNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSunExposure" />*</a></td>
					<td valign="top"><input type="radio" id="p3_checkSerumOk"
						name="p3_checkSerumOk" onclick="onCheck(this,'p3_checkSerum')"
						<%= props.getProperty("p3_checkSerumOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_checkSerumNo"
						name="p3_checkSerumNo" onclick="onCheck(this,'p3_checkSerum')"
						<%= props.getProperty("p3_checkSerumNo", "") %>></td>
					<td valign="top"><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formCheckSerum" />*</a></i></td>
					<td valign="top"><input type="radio" id="p3_pesticidesOk"
						name="p3_pesticidesOk" onclick="onCheck(this,'p3_pesticides')"
						<%= props.getProperty("p3_pesticidesOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_pesticidesNo"
						name="p3_pesticidesNo" onclick="onCheck(this,'p3_pesticides')"
						<%= props.getProperty("p3_pesticidesNo", "") %>></td>
					<td colspan="4" valign="top"><i><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formPesticides" />*</a></i></td>

				</tr>
				<tr>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_2ndSmokeOk,p3_2ndSmokeNo,p3_feverOk,p3_feverNo,p3_coughMedOk,p3_coughMedNo');" /></td>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_teethingOk,p3_teethingNo,p3_activeOk,p3_activeNo,p3_sunExposureOk,p3_sunExposureNo');" /></td>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_altMedOk,p3_altMedNo,p3_readingOk,p3_readingNo,p3_checkSerumOk,p3_checkSerumNo');" /></td>
					<td colspan="3" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_pacifierOk,p3_pacifierNo,p3_footwearOk,p3_footwearNo,p3_pesticidesOk,p3_pesticidesNo');" /></td>
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
                            <bean:message key="oscarEncounter.formRourke2009_1.msgDevelopmentDesc"/><br>
			<img height="15" width="20" src="graphics/Checkmark_Lwhite.gif"><bean:message
				key="oscarEncounter.formRourke2006_1.msgDevelopmentLegend" />
                        </td>
			<td colspan="3" valign="top" align="center">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="3"><textarea id="p3_development9m"
						name="p3_development9m" rows="5" cols="25" class="wide"><%= props.getProperty("p3_development9m", "") %></textarea></td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_hiddenToyOk"
						name="p3_hiddenToyOk" onclick="onCheck(this,'p3_hiddenToy')"
						<%= props.getProperty("p3_hiddenToyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_hiddenToyNo"
						name="p3_hiddenToyNo" onclick="onCheck(this,'p3_hiddenToy')"
						<%= props.getProperty("p3_hiddenToyNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formhiddenToy" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_hiddenToyOk,p3_hiddenToyNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_soundsOk"
						name="p3_soundsOk" onclick="onCheck(this,'p3_sounds')"
						<%= props.getProperty("p3_soundsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_soundsNo"
						name="p3_soundsNo" onclick="onCheck(this,'p3_sounds')"
						<%= props.getProperty("p3_soundsNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formSounds" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_soundsOk,p3_soundsNo');" /></td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p3_responds2peopleOk"
						name="p3_responds2peopleOk" onclick="onCheck(this,'p3_responds2people')"
						<%= props.getProperty("p3_responds2peopleOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_responds2peopleNo"
						name="p3_responds2peopleNo" onclick="onCheck(this,'p3_responds2people')"
						<%= props.getProperty("p3_responds2peopleNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formResponds2people" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_responds2peopleOk,p3_responds2peopleNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_makeSoundsOk"
						name="p3_makeSoundsOk" onclick="onCheck(this,'p3_makeSounds')"
						<%= props.getProperty("p3_makeSoundsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_makeSoundsNo"
						name="p3_makeSoundsNo" onclick="onCheck(this,'p3_makeSounds')"
						<%= props.getProperty("p3_makeSoundsNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formMakeSounds" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_makeSoundsOk,p3_makeSoundsNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_sitsOk"
						name="p3_sitsOk" onclick="onCheck(this,'p3_sits')"
						<%= props.getProperty("p3_sitsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_sitsNo"
						name="p3_sitsNo" onclick="onCheck(this,'p3_sits')"
						<%= props.getProperty("p3_sitsNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_3.formSits" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_sitsOk,p3_sitsNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_standsOk"
						name="p3_standsOk" onclick="onCheck(this,'p3_stands')"
						<%= props.getProperty("p3_standsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_standsNo"
						name="p3_standsNo" onclick="onCheck(this,'p3_stands')"
						<%= props.getProperty("p3_standsNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formStands" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_standsOk,p3_standsNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_thumbOk"
						name="p3_thumbOk" onclick="onCheck(this,'p3_thumb')"
						<%= props.getProperty("p3_thumbOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_thumbNo"
						name="p3_thumbNo" onclick="onCheck(this,'p3_thumb')"
						<%= props.getProperty("p3_thumbNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formThumb&Index" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_thumbOk,p3_thumbNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_playGamesOk"
						name="p3_playGamesOk" onclick="onCheck(this,'p3_playGames')"
						<%= props.getProperty("p3_playGamesOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_playGamesNo"
						name="p3_playGamesNo" onclick="onCheck(this,'p3_playGames')"
						<%= props.getProperty("p3_playGamesNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formplayGames" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_playGamesOk,p3_playGamesNo');" /></td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_attention9mOk" name="p3_attention9mOk"
						onclick="onCheck(this,'p3_attention9m')"
						<%= props.getProperty("p3_attention9mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_attention9mNo" name="p3_attention9mNo"
						onclick="onCheck(this,'p3_attention9m')"
						<%= props.getProperty("p3_attention9mNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formAttention" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_attention9mOk,p3_attention9mNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio"
						id="p3_noParentsConcerns9mOk" name="p3_noParentsConcerns9mOk"
						onclick="onCheck(this,'p3_noParentsConcerns9m')"
						<%= props.getProperty("p3_noParentsConcerns9mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_noParentsConcerns9mNo" name="p3_noParentsConcerns9mNo"
						onclick="onCheck(this,'p3_noParentsConcerns9m')"
						<%= props.getProperty("p3_noParentsConcerns9mNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009.formNoparentConcerns" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_noParentsConcerns9mOk,p3_noParentsConcerns9mNo');" /></td>
					<td>&nbsp;</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top" align="center">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="3"><textarea id="p3_development12m"
						name="p3_development12m" rows="5" cols="25" class="wide"><%= props.getProperty("p3_development12m", "") %></textarea></td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_responds2nameOk"
						name="p3_respondsOk" onclick="onCheck(this,'p3_responds2name')"
						<%= props.getProperty("p3_respondsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_responds2nameNo"
						name="p3_respondsNo" onclick="onCheck(this,'p3_responds2name')"
						<%= props.getProperty("p3_respondsNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_3.formResponds" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_respondsOk,p3_respondsNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_simpleRequestsOk"
						name="p3_simpleRequestsOk"
						onclick="onCheck(this,'p3_simpleRequests')"
						<%= props.getProperty("p3_simpleRequestsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_simpleRequestsNo"
						name="p3_simpleRequestsNo"
						onclick="onCheck(this,'p3_simpleRequests')"
						<%= props.getProperty("p3_simpleRequestsNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formSimpleReq" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_simpleRequestsOk,p3_simpleRequestsNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_consonantOk"
						name="p3_consonantOk" onclick="onCheck(this,'p3_consonant')"
						<%= props.getProperty("p3_consonantOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_consonantNo"
						name="p3_consonantNo" onclick="onCheck(this,'p3_consonant')"
						<%= props.getProperty("p3_consonantNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formConsonant" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_consonantOk,p3_consonantNo');" /></td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p3_says3wordsOk"
						name="p3_says3wordsOk" onclick="onCheck(this,'p3_says3words')"
						<%= props.getProperty("p3_says3wordsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_says3wordsNo"
						name="p3_says3wordsNo" onclick="onCheck(this,'p3_says3words')"
						<%= props.getProperty("p3_says3wordsNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formsays3words" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_says3wordsOk,p3_says3wordsNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_shufflesOk"
						name="p3_shufflesOk" onclick="onCheck(this,'p3_shuffles')"
						<%= props.getProperty("p3_shufflesOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_shufflesNo"
						name="p3_shufflesNo" onclick="onCheck(this,'p3_shuffles')"
						<%= props.getProperty("p3_shufflesNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formCrawls" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_shufflesOk,p3_shufflesNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_pull2standOk"
						name="p3_pull2standOk" onclick="onCheck(this,'p3_pull2stand')"
						<%= props.getProperty("p3_pull2standOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_pull2standNo"
						name="p3_pull2standNo" onclick="onCheck(this,'p3_pull2stand')"
						<%= props.getProperty("p3_pull2standNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_3.formPulltoStand" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_pull2standOk,p3_pull2standNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_showDistressOk"
						name="p3_showDistressOk" onclick="onCheck(this,'p3_showDistress')"
						<%= props.getProperty("p3_showDistressOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_showDistressNo"
						name="p3_showDistressNo" onclick="onCheck(this,'p3_showDistress')"
						<%= props.getProperty("p3_showDistressNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formShowDistress" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_showDistressOk,p3_showDistressNo');" /></td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p3_followGazeOk"
						name="p3_followGazeOk" onclick="onCheck(this,'p3_followGaze')"
						<%= props.getProperty("p3_followGazeOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_followGazeNo"
						name="p3_followGazeNo" onclick="onCheck(this,'p3_followGaze')"
						<%= props.getProperty("p3_followGazeNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formfollowGaze" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_followGazeOk,p3_followGazeNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio"
						id="p3_noParentsConcerns12mOk" name="p3_noParentsConcerns12mOk"
						onclick="onCheck(this,'p3_noParentsConcerns12m')"
						<%= props.getProperty("p3_noParentsConcerns12mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_noParentsConcerns12mNo" name="p3_noParentsConcerns12mNo"
						onclick="onCheck(this,'p3_noParentsConcerns12m')"
						<%= props.getProperty("p3_noParentsConcerns12mNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009.formNoparentConcerns" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_noParentsConcerns12mOk,p3_noParentsConcerns12mNo');" /></td>
					<td>&nbsp;</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
                                <tr>
                                    <td colspan="3"><textarea id="p3_development15m"
						name="p3_development15m" rows="5" cols="25" class="wide"><%= props.getProperty("p3_development15m", "") %></textarea></td>
                                </tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_says5wordsOk"
						name="p3_says5wordsOk" onclick="onCheck(this,'p3_says5words')"
						<%= props.getProperty("p3_says5wordsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_says5wordsNo"
						name="p3_says5wordsNo" onclick="onCheck(this,'p3_says5words')"
						<%= props.getProperty("p3_says5wordsNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_3.formSays5words" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_says5wordsOk,p3_says5wordsNo');" /></td>
					<td>&nbsp;</td>
				</tr>				
				<tr>
					<td valign="top"><input type="radio" id="p3_fingerFoodsOk"
						name="p3_fingerFoodsOk" onclick="onCheck(this,'p3_fingerFoods')"
						<%= props.getProperty("p3_fingerFoodsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_fingerFoodsNo"
						name="p3_fingerFoodsNo" onclick="onCheck(this,'p3_fingerFoods')"
						<%= props.getProperty("p3_fingerFoodsNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_3.formFingerFoods" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_fingerFoodsOk,p3_fingerFoodsNo');" /></td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p3_walksSidewaysOk"
						name="p3_walksSidewaysOk" onclick="onCheck(this,'p3_walksSideways')"
						<%= props.getProperty("p3_walksSidewaysOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_walksSidewaysNo"
						name="p3_walksSidewaysNo" onclick="onCheck(this,'p3_walksSideways')"
						<%= props.getProperty("p3_walksSidewaysNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_3.formwalksSideways" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_walksSidewaysOk,p3_walksSidewaysNo');" /></td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p3_showsFearStrangersOk"
						name="p3_showsFearStrangersOk" onclick="onCheck(this,'p3_showsFearStrangers')"
						<%= props.getProperty("p3_showsFearStrangersOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_showsFearStrangersNo"
						name="p3_showsFearStrangersNo" onclick="onCheck(this,'p3_showsFearStrangers')"
						<%= props.getProperty("p3_showsFearStrangersNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_3.formshowsFearStrangers" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_showsFearStrangersOk,p3_showsFearStrangersNo');" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_crawlsStairsOk"
						name="p3_crawlsStairsOk" onclick="onCheck(this,'p3_crawlsStairs')"
						<%= props.getProperty("p3_crawlsStairsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_crawlsStairsNo"
						name="p3_crawlsStairsNo" onclick="onCheck(this,'p3_crawlsStairs')"
						<%= props.getProperty("p3_crawlsStairsNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_3.formCrawlsStairs" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_crawlsStairsOk,p3_crawlsStairsNo');" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_squatsOk"
						name="p3_squatsOk" onclick="onCheck(this,'p3_squats')"
						<%= props.getProperty("p3_squatsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_squatsNo"
						name="p3_squatsNo" onclick="onCheck(this,'p3_squats')"
						<%= props.getProperty("p3_squatsNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_3.formSquats" /></td>
				</tr>
				<tr>
					<td valign="bottom" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_squatsOk,p3_squatsNo');" /></td>
				</tr>				
				<tr>
					<td valign="top"><input type="radio"
						id="p3_noParentsConcerns15mOk" name="p3_noParentsConcerns15mOk"
						onclick="onCheck(this,'p3_noParentsConcerns15m')"
						<%= props.getProperty("p3_noParentsConcerns15mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_noParentsConcerns15mNo" name="p3_noParentsConcerns15mNo"
						onclick="onCheck(this,'p3_noParentsConcerns15m')"
						<%= props.getProperty("p3_noParentsConcerns15mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009.formNoparentConcerns" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_noParentsConcerns15mOk,p3_noParentsConcerns15mNo');" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgPhysicalExamination" /></a><br>
			<bean:message
				key="oscarEncounter.formRourke1.msgPhysicalExaminationDesc" /><br>
                        <img height="15" width="20" src="graphics/Checkmark_Lwhite.gif">
                        <bean:message key="oscarEncounter.formRourke2009.msgPhysicalExaminationLegend"/>
			
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
						id="p3_fontanelles9mOk" name="p3_fontanelles9mOk"
						onclick="onCheck(this,'p3_fontanelles9m')"
						<%= props.getProperty("p3_fontanelles9mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_fontanelles9mNo" name="p3_fontanelles9mNo"
						onclick="onCheck(this,'p3_fontanelles9m')"
						<%= props.getProperty("p3_fontanelles9mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_fontanelles9mOk,p3_fontanelles9mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_eyes9mOk" name="p3_eyes9mOk"
						onclick="onCheck(this,'p3_eyes9m')"
						<%= props.getProperty("p3_eyes9mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_eyes9mNo" name="p3_eyes9mNo"
						onclick="onCheck(this,'p3_eyes9m')"
						<%= props.getProperty("p3_eyes9mNo", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke1.formRedReflex"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_eyes9mOk,p3_eyes9mNo');" /></td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_corneal9mOk" name="p3_corneal9mOk"
						onclick="onCheck(this,'p3_corneal9m')"
						<%= props.getProperty("p3_corneal9mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_corneal9mNo" name="p3_corneal9mNo"
						onclick="onCheck(this,'p3_corneal9m')"
						<%= props.getProperty("p3_corneal9mNo", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_3.formCornealReflex"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_corneal9mOk,p3_corneal9mNo');" /></td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_hearing9mOk" name="p3_hearing9mOk"
						onclick="onCheck(this,'p3_hearing9m')"
						<%= props.getProperty("p3_hearing9mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_hearing9mNo" name="p3_hearing9mNo"
						onclick="onCheck(this,'p3_hearing9m')"
						<%= props.getProperty("p3_hearing9mNo", "") %>></td>
					<td><i><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formHearingInquiry"/>*</a></i></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_hearing9mOk,p3_hearing9mNo');" /></td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_hips9mOk" name="p3_hips9mOk"
						onclick="onCheck(this,'p3_hips9m')"
						<%= props.getProperty("p3_hips9mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_hips9mNo" name="p3_hips9mNo"
						onclick="onCheck(this,'p3_hips9m')"
						<%= props.getProperty("p3_hips9mNo", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_2.formHips"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_hips9mOk,p3_hips9mNo');" /></td>
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
						id="p3_fontanelles12mOk" name="p3_fontanelles12mOk"
						onclick="onCheck(this,'p3_fontanelles12m')"
						<%= props.getProperty("p3_fontanelles12mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_fontanelles12mNo" name="p3_fontanelles12mNo"
						onclick="onCheck(this,'p3_fontanelles12m')"
						<%= props.getProperty("p3_fontanelles12mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_fontanelles12mOk,p3_fontanelles12mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_eyes12mOk" name="p3_eyes12mOk"
						onclick="onCheck(this,'p3_eyes12m')"
						<%= props.getProperty("p3_eyes12mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_eyes12mNo" name="p3_eyes12mNo"
						onclick="onCheck(this,'p3_eyes12m')"
						<%= props.getProperty("p3_eyes12mNo", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke1.formRedReflex"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_eyes12mOk,p3_eyes12mNo');" /></td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_corneal12mOk" name="p3_corneal12mOk"
						onclick="onCheck(this,'p3_corneal12m')"
						<%= props.getProperty("p3_corneal12mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_corneal12mNo" name="p3_corneal12mNo"
						onclick="onCheck(this,'p3_corneal12m')"
						<%= props.getProperty("p3_corneal12mNo", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_3.formCornealReflex"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_corneal12mOk,p3_corneal12mNo');" /></td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_hearing12mOk" name="p3_hearing12mOk"
						onclick="onCheck(this,'p3_hearing12m')"
						<%= props.getProperty("p3_hearing12mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_hearing12mNo" name="p3_hearing12mNo"
						onclick="onCheck(this,'p3_hearing12m')"
						<%= props.getProperty("p3_hearing12mNo", "") %>></td>
					<td><i><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formHearingInquiry"/>*</a></i></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_hearing12mOk,p3_hearing12mNo');" /></td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_tonsil12mOk" name="p3_tonsil12mOk"
						onclick="onCheck(this,'p3_tonsil12m')"
						<%= props.getProperty("p3_tonsil12mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_tonsil12mNo" name="p3_tonsil12mNo"
						onclick="onCheck(this,'p3_tonsil12m')"
						<%= props.getProperty("p3_tonsil12mNo", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_3.formTonsilSize"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_tonsil12mOk,p3_tonsil12mNo');" /></td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_hips12mOk" name="p3_hips12mOk"
						onclick="onCheck(this,'p3_hips12m')"
						<%= props.getProperty("p3_hips12mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_hips12mNo" name="p3_hips12mNo"
						onclick="onCheck(this,'p3_hips12m')"
						<%= props.getProperty("p3_hips12mNo", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_2.formHips"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_hips12mOk,p3_hips12mNo');" /></td>
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
						id="p3_fontanelles15mOk" name="p3_fontanelles15mOk"
						onclick="onCheck(this,'p3_fontanelles15m')"
						<%= props.getProperty("p3_fontanelles15mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_fontanelles15mNo" name="p3_fontanelles15mNo"
						onclick="onCheck(this,'p3_fontanelles15m')"
						<%= props.getProperty("p3_fontanelles15mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_fontanelles15mOk,p3_fontanelles15mNo');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_eyes15mOk" name="p3_eyes15mOk"
						onclick="onCheck(this,'p3_eyes15m')"
						<%= props.getProperty("p3_eyes15mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_eyes15mNo" name="p3_eyes15mNo"
						onclick="onCheck(this,'p3_eyes15m')"
						<%= props.getProperty("p3_eyes15mNo", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke1.formRedReflex"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_eyes15mOk,p3_eyes15mNo');" /></td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_corneal15mOk" name="p3_corneal15mOk"
						onclick="onCheck(this,'p3_corneal15m')"
						<%= props.getProperty("p3_corneal15mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_corneal15mNo" name="p3_corneal15mNo"
						onclick="onCheck(this,'p3_corneal15m')"
						<%= props.getProperty("p3_corneal15mNo", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_3.formCornealReflex"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_corneal15mOk,p3_corneal15mNo');" /></td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_hearing15mOk" name="p3_hearing15mOk"
						onclick="onCheck(this,'p3_hearing15m')"
						<%= props.getProperty("p3_hearing15mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_hearing15mNo" name="p3_hearing15mNo"
						onclick="onCheck(this,'p3_hearing15m')"
						<%= props.getProperty("p3_hearing15mNo", "") %>></td>
					<td><i><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formHearingInquiry"/>*</a></i></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_hearing15mOk,p3_hearing15mNo');" /></td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_tonsil15mOk" name="p3_tonsil15mOk"
						onclick="onCheck(this,'p3_tonsil15m')"
						<%= props.getProperty("p3_tonsil15mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_tonsil15mNo" name="p3_tonsil15mNo"
						onclick="onCheck(this,'p3_tonsil15m')"
						<%= props.getProperty("p3_tonsil15mNo", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_3.formTonsilSize"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_tonsil15mOk,p3_tonsil15mNo');" /></td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_hips15mOk" name="p3_hips15mOk"
						onclick="onCheck(this,'p3_hips15m')"
						<%= props.getProperty("p3_hips15mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_hips15mNo" name="p3_hips15mNo"
						onclick="onCheck(this,'p3_hips15m')"
						<%= props.getProperty("p3_hips15mNo", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_2.formHips"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_hips15mOk,p3_hips15mNo');" /></td>
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
					<td colspan="2"><textarea id="p3_problems9m"
						name="p3_problems9m" rows="5" cols="25" class="wide"><%= props.getProperty("p3_problems9m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p3_antiHB9m" <%= props.getProperty("p3_antiHB9m", "") %>></td>
					<td><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formAntiHB" />*</a></b><br />
					<bean:message key="oscarEncounter.formRourke2006_3.formAntiHBcond" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p3_hemoglobin9m"
						<%= props.getProperty("p3_hemoglobin9m", "") %>></td>
					<td><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formHemoglobin" />*</a></i></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea id="p3_problems12m"
						name="p3_problems12m" rows="5" cols="25" class="wide"><%= props.getProperty("p3_problems12m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p3_hemoglobin12m"
						<%= props.getProperty("p3_hemoglobin12m", "") %>></td>
					<td><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formHemoglobin" />*</a></i></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top"><textarea id="p3_problems15m"
				name="p3_problems15m" rows="5" cols="25" class="wide"><%= props.getProperty("p3_problems15m", "") %></textarea>
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
			</td>
			<td style="text-align: center" colspan="3" valign="top"><b><bean:message
				key="oscarEncounter.formRourke2006_1.msgImmunizationColTitle" /></b></td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.formSignature" /></a></td>
			<td colspan="3"><input type="text" class="wide"
				style="width: 100%" name="p3_signature9m"
				value="<%= props.getProperty("p3_signature9m", "") %>" /></td>
			<td colspan="3"><input type="text" class="wide" maxlength="42"
				style="width: 100%" name="p3_signature12m"
				value="<%= props.getProperty("p3_signature12m", "") %>" /></td>
			<td colspan="3"><input type="text" class="wide"
				style="width: 100%" name="p3_signature15m"
				value="<%= props.getProperty("p3_signature15m", "") %>" /></td>
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
				key="oscarEncounter.formRourke2006.Pg1" /></a>&nbsp;|&nbsp; <a
				href="formrourke2009p2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg2" /></a>&nbsp;|&nbsp; <a><bean:message
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
    Calendar.setup({ inputField : "p3_date9m", ifFormat : "%d/%m/%Y", showsTime :false, button : "p3_date9m_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "p3_date12m", ifFormat : "%d/%m/%Y", showsTime :false, button : "p3_date12m_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "p3_date15m", ifFormat : "%d/%m/%Y", showsTime :false, button : "p3_date15m_cal", singleClick : true, step : 1 });
</script>
</html:html>
