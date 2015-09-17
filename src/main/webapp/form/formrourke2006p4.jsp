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
    String formLink = "formrourke2006p4.jsp";

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
    props.setProperty("c_lastVisited", "p4");

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
<title>Rourke2006 Record 4</title>
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
        var txtConcern18m = new ylib.widget.TextCounter("p4_pConcern18m", 235,5);
        var txtConcern24m = new ylib.widget.TextCounter("p4_pConcern24m", 250,5);
        var txtConcern48m = new ylib.widget.TextCounter("p4_pConcern48m", 155,5);
        var txtProblems18m = new ylib.widget.TextCounter("p4_problems18m", 235,5);
        var txtProblems24m = new ylib.widget.TextCounter("p4_problems24m", 205,5);        
        var txtProblems48m = new ylib.widget.TextCounter("p4_problems48m", 200,5);
        
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
    
    function $F(elem) {
        return document.forms[0].elements[elem].value;
    
    }
    
    /*We have to check that measurements are numeric and an observation date has 
     *been entered for that measurment
     */
    function checkMeasures() {
        var measurements = new Array(3);
        measurements[0] = new Array("p4_ht18m", "p4_wt18m", "p4_hc18m");
        measurements[1] = new Array("p4_ht24m", "p4_wt24m", "p4_hc24m");
        measurements[2] = new Array("p4_ht48m", "p4_wt48m");
        var dates = new Array("p4_date18m", "p4_date24m", "p4_date48m");
                    
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
                
        document.forms[0].action = "../form/createpdf?__title=Rourke+Baby+Report+Pg4&__cfgfile=rourke2006printCfgPg4&__template=rourke2006p4";
        document.forms[0].target="_blank";            
        
        return true;
    }
    
    function onPrintAll() {
        document.forms[0].submit.value="printAll"; 
                
        document.forms[0].action = "../form/formname.do?__title=Rourke+Baby+Report&__cfgfile=rourke2006printCfgPg1&__cfgfile=rourke2006printCfgPg2&__cfgfile=rourke2006printCfgPg3&__cfgfile=rourke2006printCfgPg4&__template=rourke2006";
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
		value=<%=props.getProperty("c_lastVisited", "p4")%> />
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
			<td nowrap="true"><a
				href="formrourke2006p1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg1" /></a>&nbsp;|&nbsp; <a
				href="formrourke2006p2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg2" /></a>&nbsp;|&nbsp; <a
				href="formrourke2006p3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg3" /></a>&nbsp;|&nbsp; <a><bean:message
				key="oscarEncounter.formRourke2006.Pg4" /></a></td>
		</tr>
	</table>

	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr class="titleBar">
			<th><bean:message
				key="oscarEncounter.formRourke2006_4.msgRourkeBabyRecord" /></th>
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
			</td>
		</tr>
	</table>

	<table cellpadding="0" cellspacing="0" width="100%" border="1">
		<tr align="center">
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke2006_1.visitDate" /></a></td>
			<td colspan="3" class="row"><a><bean:message
				key="oscarEncounter.formRourke2006_4.msg18mos" /></a></td>
			<td colspan="3" class="row"><a><bean:message
				key="oscarEncounter.formRourke2006_4.msg2yrs" /></a></td>
			<td colspan="3" class="row"><a><bean:message
				key="oscarEncounter.formRourke2006_4.msg4yrs" /></a></td>
		</tr>
		<tr align="center">
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgDate" /></a></td>
			<td colspan="3"><input readonly type="text" id="p4_date18m"
				name="p4_date18m" ondblclick="resetDate(this)" size="10"
				value="<%=UtilMisc.htmlEscape(props.getProperty("p4_date18m", ""))%>" />
			<img src="../images/cal.gif" id="p4_date18m_cal"></td>
			<td colspan="3"><input readonly type="text" id="p4_date24m"
				name="p4_date24m" ondblclick="resetDate(this)" size="10"
				value="<%=UtilMisc.htmlEscape(props.getProperty("p4_date24m", ""))%>" />
			<img src="../images/cal.gif" id="p4_date24m_cal"></td>
			<td colspan="2"><input readonly type="text" id="p4_date48m"
				name="p4_date48m" ondblclick="resetDate(this)" size="10"
				value="<%=UtilMisc.htmlEscape(props.getProperty("p4_date48m", ""))%>" />
			<img src="../images/cal.gif" id="p4_date48m_cal"></td>
		</tr>
		<tr align="center">
			<td class="column" rowspan="2"><a><bean:message
				key="oscarEncounter.formRourke1.btnGrowth" />*</td>
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formWt" /></td>
			<td><bean:message
				key="oscarEncounter.formRourke2006_3.formHdCirc" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formWt" /></td>
			<td><bean:message
				key="oscarEncounter.formRourke2006_4.formHdCirc24m" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formWt" /></td>
		</tr>
		<tr align="center">
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p4_ht18m" size="4"
				maxlength="5" value="<%= props.getProperty("p4_ht18m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="wtEnglish2Metric(this);" name="p4_wt18m" size="4"
				maxlength="5" value="<%= props.getProperty("p4_wt18m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p4_hc18m" size="4"
				maxlength="5" value="<%= props.getProperty("p4_hc18m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p4_ht24m" size="4"
				maxlength="5" value="<%= props.getProperty("p4_ht24m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="wtEnglish2Metric(this);" name="p4_wt24m" size="4"
				maxlength="5" value="<%= props.getProperty("p4_wt24m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p4_hc24m" size="4"
				maxlength="5" value="<%= props.getProperty("p4_hc24m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="htEnglish2Metric(this);" name="p4_ht48m" size="4"
				maxlength="5" value="<%= props.getProperty("p4_ht48m", "") %>"></td>
			<td><input type="text" class="wide"
				ondblclick="wtEnglish2Metric(this);" name="p4_wt48m" size="4"
				maxlength="5" value="<%= props.getProperty("p4_wt48m", "") %>"></td>
		</tr>
		<tr align="center">
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.formParentalConcerns" /></a></td>
			<td colspan="3"><textarea id="p4_pConcern18m"
				name="p4_pConcern18m" class="wide" cols="10" rows="5"><%= props.getProperty("p4_pConcern18m", "") %></textarea>
			</td>
			<td colspan="3"><textarea id="p4_pConcern24m"
				name="p4_pConcern24m" class="wide" cols="10" rows="5"><%= props.getProperty("p4_pConcern24m", "") %></textarea>
			</td>
			<td colspan="2"><textarea id="p4_pConcern48m"
				name="p4_pConcern48m" class="wide" cols="10" rows="5"><%= props.getProperty("p4_pConcern48m", "") %></textarea>
			</td>
		</tr>
		<tr align="center">

			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgNutrition" />:</a></td>

			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p4_breastFeeding18m"
						<%= props.getProperty("p4_breastFeeding18m", "") %> /></td>
					<td><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.btnBreastFeeding" /></a></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p4_homoMilk" <%= props.getProperty("p4_homoMilk", "") %> /></td>
					<td><i><bean:message
						key="oscarEncounter.formRourke2006_4.formHomoMilk" /></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p4_bottle18m" <%= props.getProperty("p4_bottle18m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_4.formNoBottle" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p4_homo2percent24m"
						<%= props.getProperty("p4_homo2percent24m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_4.Homo2percent" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p4_lowerfatdiet24m"
						<%= props.getProperty("p4_lowerfatdiet24m", "") %>></td>
					<td><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formLowerFatDiet" />*</a></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p4_foodguide24m"
						<%= props.getProperty("p4_foodguide24m", "") %>></td>
					<td><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formFoodGuide" />*</a></td>
				</tr>
			</table>
			</td>
			<td colspan="2" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%" height="100%">
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p4_2pMilk48m" <%= props.getProperty("p4_2pMilk48m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_4.form2percentMilk" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p4_foodguide48m"
						<%= props.getProperty("p4_foodguide48m", "") %>></td>
					<td><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formFoodGuide" />*</a></td>
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
			<td colspan="3" valign="top">
			<table style="font-size: 8pt;" cellpadding="0" cellspacing="0"
				width="100%">
				<tr>
					<td colspan="3">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="3"><bean:message
						key="oscarEncounter.formRourke2006_1.formInjuryPrev" /></td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_carSeat18mOk"
						name="p4_carSeat18mOk" onclick="onCheck(this,'p4_carSeat18m')"
						<%= props.getProperty("p4_carSeat18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_carSeat18mNo"
						name="p4_carSeat18mNo" onclick="onCheck(this,'p4_carSeat18m')"
						<%= props.getProperty("p4_carSeat18mNo", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formCarSeatChild" /></a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_bathSafetyOk"
						name="p4_bathSafetyOk" onclick="onCheck(this,'p4_bathSafety')"
						<%= props.getProperty("p4_bathSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_bathSafetyNo"
						name="p4_bathSafetyNo" onclick="onCheck(this,'p4_bathSafety')"
						<%= props.getProperty("p4_bathSafetyNo", "") %>></td>
					<td valign="top"><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formBathSafety" />*</a></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_safeToysOk"
						name="p4_safeToysOk" onclick="onCheck(this,'p4_safeToys')"
						<%= props.getProperty("p4_safeToysOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_safeToysNo"
						name="p4_safeToysNo" onclick="onCheck(this,'p4_safeToys')"
						<%= props.getProperty("p4_safeToysNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSafeToys" />*</a></td>
				</tr>
				<tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p4_carSeat18mOk,p4_carSeat18mNo,p4_bathSafetyOk,p4_bathSafetyNo,p4_safeToysOk,p4_safeToysNo');" /></td>
				</tr>
				<tr>
					<td colspan="3">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="3"><bean:message
						key="oscarEncounter.formRourke2006_4.formBehaviour" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_parentChild18mOk"
						name="p4_parentChild18mOk"
						onclick="onCheck(this,'p4_parentChild18m')"
						<%= props.getProperty("p4_parentChild18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_parentChild18mNo"
						name="p4_parentChild18mNo"
						onclick="onCheck(this,'p4_parentChild18m')"
						<%= props.getProperty("p4_parentChild18mNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formParentChild" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_discipline18mOk"
						name="p4_discipline18mOk"
						onclick="onCheck(this,'p4_discipline18m')"
						<%= props.getProperty("p4_discipline18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_discipline18mNo"
						name="p4_discipline18mNo"
						onclick="onCheck(this,'p4_discipline18m')"
						<%= props.getProperty("p4_discipline18mNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formDiscipline" />**</a></td>
				</tr>
				<tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p4_parentChild18mOk,p4_parentChild18mNo,p4_discipline18mOk,p4_discipline18mNo');" /></td>
				</tr>
				<tr>
					<td colspan="3">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="3"><bean:message
						key="oscarEncounter.formRourke2006_4.formFamily" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_pFatigue18mOk"
						name="p4_pFatigue18mOk" onclick="onCheck(this,'p4_pFatigue18m')"
						<%= props.getProperty("p4_pFatigue18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_pFatigue18mNo"
						name="p4_pFatigue18mNo" onclick="onCheck(this,'p4_pFatigue18m')"
						<%= props.getProperty("p4_pFatigue18mNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formParentFatigue" />**</a></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_highRisk18mOk"
						name="p4_highRisk18mOk" onclick="onCheck(this,'p4_highRisk18m')"
						<%= props.getProperty("p4_highRisk18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_highRisk18mNo"
						name="p4_highRisk18mNo" onclick="onCheck(this,'p4_highRisk18m')"
						<%= props.getProperty("p4_highRisk18mNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formHighRisk" />**</a></td>
				</tr>
				<tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p4_pFatigue18mOk,p4_pFatigue18mNo,p4_highRisk18mOk,p4_highRisk18mNo');" /></td>
				</tr>
				<tr>
					<td colspan="3">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3" valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formOther" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_socializing18mOk"
						name="p4_socializing18mOk"
						onclick="onCheck(this,'p4_socializing18m')"
						<%= props.getProperty("p4_socializing18mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_socializing18mNo"
						name="p4_socializing18mNo"
						onclick="onCheck(this,'p4_socializing18m')"
						<%= props.getProperty("p4_socializing18mNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formSocPeerPlay" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_dentalCareOk"
						name="p4_dentalCareOk" onclick="onCheck(this,'p4_dentalCare')"
						<%= props.getProperty("p4_dentalCareOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_dentalCareNo"
						name="p4_dentalCareNo" onclick="onCheck(this,'p4_dentalCare')"
						<%= props.getProperty("p4_dentalCareNo", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formDentalCleaning" />*</a></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio"
						id="p4_toiletLearning18mOk" name="p4_toiletLearning18mOk"
						onclick="onCheck(this,'p4_toiletLearning18m')"
						<%= props.getProperty("p4_toiletLearning18mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p4_toiletLearning18mNo" name="p4_toiletLearning18mNo"
						onclick="onCheck(this,'p4_toiletLearning18m')"
						<%= props.getProperty("p4_toiletLearning18mNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formToiletLearning" />**</a></td>
				</tr>
				<tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p4_socializing18mOk,p4_socializing18mNo,p4_dentalCareOk,p4_dentalCareNo,p4_toiletLearning18mOk,p4_toiletLearning18mNo');" /></td>
				</tr>
			</table>
			</td>
			<td colspan="5" valign="top">
			<table style="font-size: 8pt;" cellpadding="0" cellspacing="0"
				width="100%">
				<tr>
					<td colspan="9">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="9">&nbsp;</td>
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
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_carSeat24mOk"
						name="p4_carSeat24mOk" onclick="onCheck(this,'p4_carSeat24m')"
						<%= props.getProperty("p4_carSeat24mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_carSeat24mNo"
						name="p4_carSeat24mNo" onclick="onCheck(this,'p4_carSeat24m')"
						<%= props.getProperty("p4_carSeat24mNo", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formCarSeatChildBooster" /></a>*</b></td>
					<td valign="top"><input type="radio" id="p4_bikeHelmetsOk"
						name="p4_bikeHelmetsOk" onclick="onCheck(this,'p4_bikeHelmets')"
						<%= props.getProperty("p4_bikeHelmetsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_bikeHelmetsNo"
						name="p4_bikeHelmetsNo" onclick="onCheck(this,'p4_bikeHelmets')"
						<%= props.getProperty("p4_bikeHelmetsNo", "") %>></td>
					<td valign="top"><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formBikeHelmet" />*</a></i></td>
					<td valign="top"><input type="radio" id="p4_firearmSafetyOk"
						name="p4_firearmSafetyOk"
						onclick="onCheck(this,'p4_firearmSafety')"
						<%= props.getProperty("p4_firearmSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_firearmSafetyNo"
						name="p4_firearmSafetyNo"
						onclick="onCheck(this,'p4_firearmSafety')"
						<%= props.getProperty("p4_firearmSafetyNo", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formFireArm" />*</a></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_smokeSafetyOk"
						name="p4_smokeSafetyOk" onclick="onCheck(this,'p4_smokeSafety')"
						<%= props.getProperty("p4_smokeSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_smokeSafetyNo"
						name="p4_smokeSafetyNo" onclick="onCheck(this,'p4_smokeSafety')"
						<%= props.getProperty("p4_smokeSafetyNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSmokeSafety" />*</a></td>
					<td valign="top"><input type="radio" id="p4_matchesOk"
						name="p4_matchesOk" onclick="onCheck(this,'p4_matches')"
						<%= props.getProperty("p4_matchesOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_matchesNo"
						name="p4_matchesNo" onclick="onCheck(this,'p4_matches')"
						<%= props.getProperty("p4_matchesNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formMatches" /></td>
					<td valign="top"><input type="radio" id="p4_waterSafetyOk"
						name="p4_waterSafetyOk" onclick="onCheck(this,'p4_waterSafety')"
						<%= props.getProperty("p4_waterSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_waterSafetyNo"
						name="p4_waterSafetyNo" onclick="onCheck(this,'p4_waterSafety')"
						<%= props.getProperty("p4_waterSafetyNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formWaterSafety" /></td>
				</tr>
				<tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p4_carSeat24mOk,p4_carSeat24mNo,p4_smokeSafetyOk,p4_smokeSafetyNo');" /></td>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p4_bikeHelmetsOk,p4_bikeHelmetsNo,p4_matchesOk,p4_matchesNo');" /></td>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p4_firearmSafetyOk,p4_firearmSafetyNo,p4_waterSafetyOk,p4_waterSafetyNo');" /></td>
				</tr>
				<tr>
					<td colspan="9">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_parentChild24mOk"
						name="p4_parentChild24mOk"
						onclick="onCheck(this,'p4_parentChild24m')"
						<%= props.getProperty("p4_parentChild24mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_parentChild24mNo"
						name="p4_parentChild24mNo"
						onclick="onCheck(this,'p4_parentChild24m')"
						<%= props.getProperty("p4_parentChild24mNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formParentChild" /></td>
					<td valign="top"><input type="radio" id="p4_discipline24mOk"
						name="p4_discipline24mOk"
						onclick="onCheck(this,'p4_discipline24m')"
						<%= props.getProperty("p4_discipline24mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_discipline24mNo"
						name="p4_discipline24mNo"
						onclick="onCheck(this,'p4_discipline24m')"
						<%= props.getProperty("p4_discipline24mNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formDiscipline" />**</a></td>
					<td valign="top"><input type="radio" id="p4_highRisk24mOk"
						name="p4_highRisk24mOk" onclick="onCheck(this,'p4_highRisk24m')"
						<%= props.getProperty("p4_highRisk24mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_highRisk24mNo"
						name="p4_highRisk24mNo" onclick="onCheck(this,'p4_highRisk24m')"
						<%= props.getProperty("p4_highRisk24mNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formHighRisk" />**</a></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_pFatigue24mOk"
						name="p4_pFatigue24mOk" onclick="onCheck(this,'p4_pFatigue24m')"
						<%= props.getProperty("p4_pFatigue24mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_pFatigue24mNo"
						name="p4_pFatigue24mNo" onclick="onCheck(this,'p4_pFatigue24m')"
						<%= props.getProperty("p4_pFatigue24mNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formParentFatigue" />**</a></td>
					<td valign="top"><input type="radio" id="p4_famConflictOk"
						name="p4_famConflictOk" onclick="onCheck(this,'p4_famConflict')"
						<%= props.getProperty("p4_famConflictOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_famConflictNo"
						name="p4_famConflictNo" onclick="onCheck(this,'p4_famConflict')"
						<%= props.getProperty("p4_famConflictNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formFamConflict" /></td>
					<td valign="top"><input type="radio" id="p4_siblingsOk"
						name="p4_siblingsOk" onclick="onCheck(this,'p4_siblings')"
						<%= props.getProperty("p4_siblingsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_siblingsNo"
						name="p4_siblingsNo" onclick="onCheck(this,'p4_siblings')"
						<%= props.getProperty("p4_siblingsNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formSiblings" /></td>
				</tr>
				<tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p4_parentChild24mOk,p4_parentChild24mNo,p4_pFatigue24mOk,p4_pFatigue24mNo');" /></td>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p4_discipline24mOk,p4_discipline24mNo,p4_famConflictOk,p4_famConflictNo');" /></td>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p4_highRisk24mOk,p4_highRisk24mNo,p4_siblingsOk,p4_siblingsNo');" /></td>
				</tr>
				<tr>
					<td colspan="9">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_2ndSmokeOk"
						name="p4_2ndSmokeOk" onclick="onCheck(this,'p4_2ndSmoke')"
						<%= props.getProperty("p4_2ndSmokeOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_2ndSmokeNo"
						name="p4_2ndSmokeNo" onclick="onCheck(this,'p4_2ndSmoke')"
						<%= props.getProperty("p4_2ndSmokeNo", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.formSecondHandSmoke" />*</a></b></td>
					<td valign="top"><input type="radio" id="p4_dentalCleaningOk"
						name="p4_dentalCleaningOk"
						onclick="onCheck(this,'p4_dentalCleaning')"
						<%= props.getProperty("p4_dentalCleaningOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_dentalCleaningNo"
						name="p4_dentalCleaningNo"
						onclick="onCheck(this,'p4_dentalCleaning')"
						<%= props.getProperty("p4_dentalCleaningNo", "") %>></td>
					<td colspan="4" valign="top"><b><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formDentalCleaning" />*</a></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_altMedOk"
						name="p4_altMedOk" onclick="onCheck(this,'p4_altMed')"
						<%= props.getProperty("p4_altMedOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_altMedNo"
						name="p4_altMedNo" onclick="onCheck(this,'p4_altMed')"
						<%= props.getProperty("p4_altMedNo", "") %>></td>
					<td valign="top"><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formAltMed" />*</a></i></td>
					<td valign="top"><input type="radio"
						id="p4_toiletLearning24mOk" name="p4_toiletLearning24mOk"
						onclick="onCheck(this,'p4_toiletLearning24m')"
						<%= props.getProperty("p4_toiletLearning24mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p4_toiletLearning24mNo" name="p4_toiletLearning24mNo"
						onclick="onCheck(this,'p4_toiletLearning24m')"
						<%= props.getProperty("p4_toiletLearning24mNo", "") %>></td>
					<td colspan="4" valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formToiletLearning" />**</a></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_activeOk"
						name="p4_activeOk" onclick="onCheck(this,'p4_active')"
						<%= props.getProperty("p4_activeOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_activeNo"
						name="p4_activeNo" onclick="onCheck(this,'p4_active')"
						<%= props.getProperty("p4_activeNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formactiveLife" />*</a></td>
					<td valign="top"><input type="radio" id="p4_socializing24mOk"
						name="p4_socializing24mOk"
						onclick="onCheck(this,'p4_socializing24m')"
						<%= props.getProperty("p4_socializing24mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_socializing24mNo"
						name="p4_socializing24mNo"
						onclick="onCheck(this,'p4_socializing24m')"
						<%= props.getProperty("p4_socializing24mNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formSocializing" /></td>
					<td valign="top"><input type="radio" id="p4_readingOk"
						name="p4_readingOk" onclick="onCheck(this,'p4_reading')"
						<%= props.getProperty("p4_readingOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_readingNo"
						name="p4_readingNo" onclick="onCheck(this,'p4_reading')"
						<%= props.getProperty("p4_readingNo", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formEncourageReading" />**</a></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_dayCareOk"
						name="p4_dayCareOk" onclick="onCheck(this,'p4_dayCare')"
						<%= props.getProperty("p4_dayCareOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_dayCareNo"
						name="p4_dayCareNo" onclick="onCheck(this,'p4_dayCare')"
						<%= props.getProperty("p4_dayCareNo", "") %>></td>
					<td colspan="7" valign="top"><b><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formDayCare" />**</a></b></td>
				</tr>
				<tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p4_2ndSmokeOk,p4_2ndSmokeNo,p4_altMedOk,p4_altMedNo,p4_activeOk,p4_activeNo,p4_dayCareOk,p4_dayCareNo');" /></td>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p4_dentalCleaningOk,p4_dentalCleaningNo,p4_toiletLearning24mOk,p4_toiletLearning24mNo,p4_socializing24mOk,p4_socializing24mNo');" /></td>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p4_readingOk,p4_readingNo');" /></td>
				</tr>
				<tr>
					<td colspan="9">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="9" valign="top"><bean:message
						key="oscarEncounter.formRourke2006_3.formEnvHealth" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_sunExposureOk"
						name="p4_sunExposureOk" onclick="onCheck(this,'p4_sunExposure')"
						<%= props.getProperty("p4_sunExposureOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_sunExposureNo"
						name="p4_sunExposureNo" onclick="onCheck(this,'p4_sunExposure')"
						<%= props.getProperty("p4_sunExposureNo", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSunExposure" />*</a></td>
					<td valign="top"><input type="radio" id="p4_pesticidesOk"
						name="p4_pesticidesOk" onclick="onCheck(this,'p4_pesticides')"
						<%= props.getProperty("p4_pesticidesOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_pesticidesNo"
						name="p4_pesticidesNo" onclick="onCheck(this,'p4_pesticides')"
						<%= props.getProperty("p4_pesticidesNo", "") %>></td>
					<td colspan="4" valign="top"><i><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formPesticides" />*</a></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_checkSerumOk"
						name="p4_checkSerumOk" onclick="onCheck(this,'p4_checkSerum')"
						<%= props.getProperty("p4_checkSerumOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_checkSerumNo"
						name="p4_checkSerumNo" onclick="onCheck(this,'p4_checkSerum')"
						<%= props.getProperty("p4_checkSerumNo", "") %>></td>
					<td colspan="7" valign="top"><i><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formCheckSerum" />*</a></i></td>
				</tr>
				<tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p4_sunExposureOk,p4_sunExposureNo,p4_checkSerumOk,p4_checkSerumNo');" /></td>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p4_pesticidesOk,p4_pesticidesNo');" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgDevelopment" />**</a><br>
			<img height="15" width="20" src="graphics/Checkmark_Lwhite.gif"><bean:message
				key="oscarEncounter.formRourke2006_1.msgDevelopmentLegend" /></td>
			<td colspan="3" valign="top" align="center">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td colspan="3">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3"><bean:message
						key="oscarEncounter.formRourke2006_4.formSocialEmotion" /></td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_manageableOk"
						name="p4_manageableOk" onclick="onCheck(this,'p4_manageable')"
						<%= props.getProperty("p4_manageableOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_manageableNo"
						name="p4_manageableNo" onclick="onCheck(this,'p4_manageable')"
						<%= props.getProperty("p4_manageableNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formManageable" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_soothabilityOk"
						name="p4_soothabilityOk" onclick="onCheck(this,'p4_soothability')"
						<%= props.getProperty("p4_soothabilityOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_soothabilityNo"
						name="p4_soothabilityNo" onclick="onCheck(this,'p4_soothability')"
						<%= props.getProperty("p4_soothabilityNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formSoothability" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_comfortOk"
						name="p4_comfortOk" onclick="onCheck(this,'p4_comfort')"
						<%= props.getProperty("p4_comfortOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_comfortNo"
						name="p4_comfortNo" onclick="onCheck(this,'p4_comfort')"
						<%= props.getProperty("p4_comfortNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formComfort" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p4_manageableOk,p4_manageableNo,p4_soothabilityOk,p4_soothability,p4_comfortOk,p4_comfortNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3"><bean:message
						key="oscarEncounter.formRourke2006_4.formCommSkills" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_pointsOk"
						name="p4_pointsOk" onclick="onCheck(this,'p4_points')"
						<%= props.getProperty("p4_pointsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_pointsNo"
						name="p4_pointsNo" onclick="onCheck(this,'p4_points')"
						<%= props.getProperty("p4_pointsNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formPoints" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_getAttnOk"
						name="p4_getAttnOk" onclick="onCheck(this,'p4_getAttn')"
						<%= props.getProperty("p4_getAttnOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_getAttnNo"
						name="p4_getAttnNo" onclick="onCheck(this,'p4_getAttn')"
						<%= props.getProperty("p4_getAttnNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formGetAttn" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_pretendPlayOk"
						name="p4_pretendPlayOk" onclick="onCheck(this,'p4_pretendPlay')"
						<%= props.getProperty("p4_pretendPlayOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_pretendPlayNo"
						name="p4_pretendPlayNo" onclick="onCheck(this,'p4_pretendPlay')"
						<%= props.getProperty("p4_pretendPlayNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formPretendPlay" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_recsNameOk"
						name="p4_recsNameOk" onclick="onCheck(this,'p4_recsName')"
						<%= props.getProperty("p4_recsNameOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_recsNameNo"
						name="p4_recsNameNo" onclick="onCheck(this,'p4_recsName')"
						<%= props.getProperty("p4_recsNameNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formRecsName" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_initSpeechOk"
						name="p4_initSpeechOk" onclick="onCheck(this,'p4_initSpeech')"
						<%= props.getProperty("p4_initSpeechOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_initSpeechNo"
						name="p4_initSpeechNo" onclick="onCheck(this,'p4_initSpeech')"
						<%= props.getProperty("p4_initSpeechNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formInitSpeech" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_3consonantsOk"
						name="p4_3consonantsOk" onclick="onCheck(this,'p4_3consonants')"
						<%= props.getProperty("p4_3consonantsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_3consonantsNo"
						name="p4_3consonantsNo" onclick="onCheck(this,'p4_3consonants')"
						<%= props.getProperty("p4_3consonantsNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.form3consonants" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p4_pointsOk,p4_pointsNo,p4_getAttnOk,p4_getAttnNo,p4_pretendPlayOk,p4_pretendPlayNo,p4_recsNameOk,p4_recsNameNo,p4_initSpeechOk,p4_initSpeechNo,p4_3consonantsOk,p4_3consonantsNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3"><bean:message
						key="oscarEncounter.formRourke2006_4.formMotorSkills" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_walksbackOk"
						name="p4_walksbackOk" onclick="onCheck(this,'p4_walksback')"
						<%= props.getProperty("p4_walksbackOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_walksbackNo"
						name="p4_walksbackNo" onclick="onCheck(this,'p4_walksback')"
						<%= props.getProperty("p4_walksbackNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formWalksBack" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_feedsSelfOk"
						name="p4_feedsSelfOk" onclick="onCheck(this,'p4_feedsSelf')"
						<%= props.getProperty("p4_feedsSelfOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_feedsSelfNo"
						name="p4_feedsSelfNo" onclick="onCheck(this,'p4_feedsSelf')"
						<%= props.getProperty("p4_feedsSelfNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formFeedsSelf" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p4_walksbackOk,p4_walksbackNo,p4_feedsSelfOk,p4_feedsSelfNo');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3"><bean:message
						key="oscarEncounter.formRourke2006_4.formAdaptiv" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p4_removesHatOk"
						name="p4_removesHatOk" onclick="onCheck(this,'p4_removesHat')"
						<%= props.getProperty("p4_removesHatOk", "") %>></td>
					<td valign="top"><input type="radio" id="p4_removesHatNo"
						name="p4_removesHatNo" onclick="onCheck(this,'p4_removesHat')"
						<%= props.getProperty("p4_removesHatNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_4.formRemovesHat" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio"
						id="p4_noParentsConcerns18mOk" name="p4_noParentsConcerns18mOk"
						onclick="onCheck(this,'p4_noParentsConcerns18m')"
						<%= props.getProperty("p4_noParentsConcerns18mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p4_noParentsConcerns18mNo" name="p4_noParentsConcerns18mNo"
						onclick="onCheck(this,'p4_noParentsConcerns18m')"
						<%= props.getProperty("p4_noParentsConcerns18mNo", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke1.formNoparentConcerns" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p4_removesHatOk,p4_removesHatNo,p4_noParentsConcerns18mOk,p4_noParentsConcerns18mNo');" /></td>
					<td>&nbsp;</td>
				</tr>
			</table>
			</td>
			<td colspan="5" valign="top" align="center">
			<table cellpadding="0" cellspacing="2" border="1" width="100%">
				<tr>
					<td>
					<table cellpadding="0" cellspacing="0" width="100%">
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="3"><bean:message
								key="oscarEncounter.formRourke2006_4.form2yrs" /></td>
						</tr>
						<tr>
							<td style="padding-right: 5pt" valign="top"><img height="15"
								width="20" src="graphics/Checkmark_L.gif"></td>
							<td class="edcol" valign="top">X</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_newWordsOk"
								name="p4_newWordsOk" onclick="onCheck(this,'p4_newWords')"
								<%= props.getProperty("p4_newWordsOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_newWordsNo"
								name="p4_newWordsNo" onclick="onCheck(this,'p4_newWords')"
								<%= props.getProperty("p4_newWordsNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formNewWords" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_2wSentenceOk"
								name="p4_2wSentenceOk" onclick="onCheck(this,'p4_2wSentence')"
								<%= props.getProperty("p4_2wSentenceOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_2wSentenceNo"
								name="p4_2wSentenceNo" onclick="onCheck(this,'p4_2wSentence')"
								<%= props.getProperty("p4_2wSentenceNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.form2wordSentence" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_runsOk"
								name="p4_runsOk" onclick="onCheck(this,'p4_runs')"
								<%= props.getProperty("p4_runsOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_runsNo"
								name="p4_runsNo" onclick="onCheck(this,'p4_runs')"
								<%= props.getProperty("p4_runsNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formTriestoRun" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio"
								id="p4_smallContainerOk" name="p4_smallContainerOk"
								onclick="onCheck(this,'p4_smallContainer')"
								<%= props.getProperty("p4_smallContainerOk", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_smallContainerNo" name="p4_smallContainerNo"
								onclick="onCheck(this,'p4_smallContainer')"
								<%= props.getProperty("p4_smallContainerNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formSmallContainer" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_copiesActionsOk"
								name="p4_copiesActionsOk"
								onclick="onCheck(this,'p4_copiesActions')"
								<%= props.getProperty("p4_copiesActionsOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_copiesActionsNo"
								name="p4_copiesActionsNo"
								onclick="onCheck(this,'p4_copiesActions')"
								<%= props.getProperty("p4_copiesActionsNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formcopiesActions" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_newSkillsOk"
								name="p4_newSkillsOk" onclick="onCheck(this,'p4_newSkills')"
								<%= props.getProperty("p4_newSkillsOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_newSkillsNo"
								name="p4_newSkillsNo" onclick="onCheck(this,'p4_newSkills')"
								<%= props.getProperty("p4_newSkillsNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formNewSkills" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio"
								id="p4_noParentsConcerns24mOk" name="p4_noParentsConcerns24mOk"
								onclick="onCheck(this,'p4_noParentsConcerns24m')"
								<%= props.getProperty("p4_noParentsConcerns24mOk", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_noParentsConcerns24mNo" name="p4_noParentsConcerns24mNo"
								onclick="onCheck(this,'p4_noParentsConcerns24m')"
								<%= props.getProperty("p4_noParentsConcerns24mNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke1.formNoparentConcerns" /></td>
						</tr>
						<tr>
							<td valign="top" class="edcol" colspan="2"><input
								class="delete" type="button" value="Del"
								onclick="del('p4_newWordsOk,p4_newWordsNo,p4_2wSentenceOk,p4_2wSentenceNo,p4_runsOk,p4_runsNo,p4_smallContainerOk,p4_smallContainerNo,p4_copiesActionsOk,p4_copiesActionsNo,p4_newSkillsOk,p4_newSkillsNo,p4_noParentsConcerns24mOk,p4_noParentsConcerns24mNo');" /></td>
							<td>&nbsp;</td>
						</tr>
					</table>
					</td>
					<td>
					<table cellpadding="0" cellspacing="0" width="100%">
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="3"><bean:message
								key="oscarEncounter.formRourke2006_4.form4yrs" /></td>
						</tr>
						<tr>
							<td style="padding-right: 5pt" valign="top"><img height="15"
								width="20" src="graphics/Checkmark_L.gif"></td>
							<td class="edcol" valign="top">X</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_3directionsOk"
								name="p4_3directionsOk" onclick="onCheck(this,'p4_3directions')"
								<%= props.getProperty("p4_3directionsOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_3directionsNo"
								name="p4_3directionsNo" onclick="onCheck(this,'p4_3directions')"
								<%= props.getProperty("p4_3directionsNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.form3Directions" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_asksQuestionsOk"
								name="p4_asksQuestionsOk"
								onclick="onCheck(this,'p4_asksQuestions')"
								<%= props.getProperty("p4_asksQuestionsOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_asksQuestionsNo"
								name="p4_asksQuestionsNo"
								onclick="onCheck(this,'p4_asksQuestions')"
								<%= props.getProperty("p4_asksQuestionsNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formAsksQuestions" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_stands1footOk"
								name="p4_stands1footOk" onclick="onCheck(this,'p4_stands1foot')"
								<%= props.getProperty("p4_stands1footOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_stands1footNo"
								name="p4_stands1footNo" onclick="onCheck(this,'p4_stands1foot')"
								<%= props.getProperty("p4_stands1footNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formStands1Foot" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_drawsOk"
								name="p4_drawsOk" onclick="onCheck(this,'p4_draws')"
								<%= props.getProperty("p4_drawsOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_drawsNo"
								name="p4_drawsNo" onclick="onCheck(this,'p4_draws')"
								<%= props.getProperty("p4_drawsNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formDraws" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_toiletTrainedOk"
								name="p4_toiletTrainedOk"
								onclick="onCheck(this,'p4_toiletTrained')"
								<%= props.getProperty("p4_toiletTrainedOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_toiletTrainedNo"
								name="p4_toiletTrainedNo"
								onclick="onCheck(this,'p4_toiletTrained')"
								<%= props.getProperty("p4_toiletTrainedNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formToiletTrained" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_tries2comfortOk"
								name="p4_tries2comfortOk"
								onclick="onCheck(this,'p4_tries2comfort')"
								<%= props.getProperty("p4_tries2comfortOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_tries2comfortNo"
								name="p4_tries2comfortNo"
								onclick="onCheck(this,'p4_tries2comfort')"
								<%= props.getProperty("p4_tries2comfortNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formTries2comfort" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio"
								id="p4_noParentsConcerns48mOk" name="p4_noParentsConcerns48mOk"
								onclick="onCheck(this,'p4_noParentsConcerns48m')"
								<%= props.getProperty("p4_noParentsConcerns48mOk", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_noParentsConcerns48mNo" name="p4_noParentsConcerns48mNo"
								onclick="onCheck(this,'p4_noParentsConcerns48m')"
								<%= props.getProperty("p4_noParentsConcerns48mNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke1.formNoparentConcerns" /></td>
						</tr>
						<tr>
							<td valign="top" class="edcol" colspan="2"><input
								class="delete" type="button" value="Del"
								onclick="del('p4_3directionsOk,p4_3directionsNo,p4_asksQuestionsOk,p4_asksQuestionsNo,p4_stands1footOk,p4_stands1footNo,p4_drawsOk,p4_drawsNo,p4_toiletTrainedOk,p4_toiletTrainedNo,p4_tries2comfortOk,p4_tries2comfortNo,p4_noParentsConcerns48mOk,p4_noParentsConcerns48mNo');" /></td>
							<td>&nbsp;</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td>
					<table cellpadding="0" cellspacing="0" width="100%">
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="3"><bean:message
								key="oscarEncounter.formRourke2006_4.form3yrs" /></td>
						</tr>
						<tr>
							<td style="padding-right: 5pt" valign="top"><img height="15"
								width="20" src="graphics/Checkmark_L.gif"></td>
							<td class="edcol" valign="top">X</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_2directionsOk"
								name="p4_2directionsOk" onclick="onCheck(this,'p4_2directions')"
								<%= props.getProperty("p4_2directionsOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_2directionsNo"
								name="p4_2directionsNo" onclick="onCheck(this,'p4_2directions')"
								<%= props.getProperty("p4_2directionsNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.form2Directions" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_twistslidsOk"
								name="p4_twistslidsOk" onclick="onCheck(this,'p4_twistslids')"
								<%= props.getProperty("p4_twistslidsOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_twistslidsNo"
								name="p4_twistslidsNo" onclick="onCheck(this,'p4_twistslids')"
								<%= props.getProperty("p4_twistslidsNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formTwistsLids" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_turnsPagesOk"
								name="p4_turnsPagesOk" onclick="onCheck(this,'p4_turnsPages')"
								<%= props.getProperty("p4_turnsPagesOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_turnsPagesNo"
								name="p4_turnsPagesNo" onclick="onCheck(this,'p4_turnsPages')"
								<%= props.getProperty("p4_turnsPagesNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formTurnsPages" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio"
								id="p4_sharesSometimeOk" name="p4_sharesSometimeOk"
								onclick="onCheck(this,'p4_sharesSometime')"
								<%= props.getProperty("p4_sharesSometimeOk", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_sharesSometimeNo" name="p4_sharesSometimeNo"
								onclick="onCheck(this,'p4_sharesSometime')"
								<%= props.getProperty("p4_sharesSometimeNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formSharesSometimes" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_listenMusikOk"
								name="p4_listenMusikOk" onclick="onCheck(this,'p4_listenMusik')"
								<%= props.getProperty("p4_listenMusikOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_listenMusikNo"
								name="p4_listenMusikNo" onclick="onCheck(this,'p4_listenMusik')"
								<%= props.getProperty("p4_listenMusikNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formListensMusik" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio"
								id="p4_noParentsConcerns36mOk" name="p4_noParentsConcerns36mOk"
								onclick="onCheck(this,'p4_noParentsConcerns36m')"
								<%= props.getProperty("p4_noParentsConcerns36mOk", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_noParentsConcerns36mNo" name="p4_noParentsConcerns36mNo"
								onclick="onCheck(this,'p4_noParentsConcerns36m')"
								<%= props.getProperty("p4_noParentsConcerns36mNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke1.formNoparentConcerns" /></td>
						</tr>
						<tr>
							<td valign="top" class="edcol" colspan="2"><input
								class="delete" type="button" value="Del"
								onclick="del('p4_2directionsOk,p4_2directionsNo,p4_twistslidsOk,p4_twistslidsNo,p4_turnsPagesOk,p4_turnsPagesNo,p4_sharesSometimeOk,p4_sharesSometimeNo,p4_listenMusikOk,p4_listenMusikNo,p4_noParentsConcerns36mOk,p4_noParentsConcerns36mNo');" /></td>
							<td>&nbsp;</td>
						</tr>
					</table>
					</td>
					<td>
					<table cellpadding="0" cellspacing="0" width="100%">
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="3"><bean:message
								key="oscarEncounter.formRourke2006_4.form5yrs" /></td>
						</tr>
						<tr>
							<td style="padding-right: 5pt" valign="top"><img height="15"
								width="20" src="graphics/Checkmark_L.gif"></td>
							<td class="edcol" valign="top">X</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_counts2tenOk"
								name="p4_counts2tenOk" onclick="onCheck(this,'p4_counts2ten')"
								<%= props.getProperty("p4_counts2tenOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_counts2tenNo"
								name="p4_counts2tenNo" onclick="onCheck(this,'p4_counts2ten')"
								<%= props.getProperty("p4_counts2tenNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formCounts10" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_speaksClearlyOk"
								name="p4_speaksClearlyOk"
								onclick="onCheck(this,'p4_speaksClearly')"
								<%= props.getProperty("p4_speaksClearlyOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_speaksClearlyNo"
								name="p4_speaksClearlyNo"
								onclick="onCheck(this,'p4_speaksClearly')"
								<%= props.getProperty("p4_speaksClearlyNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formSpeaksClearly" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_throwsCatchesOk"
								name="p4_throwsCatchesOk"
								onclick="onCheck(this,'p4_throwsCatches')"
								<%= props.getProperty("p4_throwsCatchesOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_throwsCatchesNo"
								name="p4_throwsCatchesNo"
								onclick="onCheck(this,'p4_throwsCatches')"
								<%= props.getProperty("p4_throwsCatchesNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formThrowsCatches" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_hops1footOk"
								name="p4_hops1footOk" onclick="onCheck(this,'p4_hops1foot')"
								<%= props.getProperty("p4_hops1footOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_hops1footNo"
								name="p4_hops1footNo" onclick="onCheck(this,'p4_hops1foot')"
								<%= props.getProperty("p4_hops1footNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formHops1Foot" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio"
								id="p4_sharesWillinglyOk" name="p4_sharesWillinglyOk"
								onclick="onCheck(this,'p4_sharesWillingly')"
								<%= props.getProperty("p4_sharesWillinglyOk", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_sharesWillinglyNo" name="p4_sharesWillinglyNo"
								onclick="onCheck(this,'p4_sharesWillingly')"
								<%= props.getProperty("p4_sharesWillinglyNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formSharesWillingly" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_worksAloneOk"
								name="p4_worksAloneOk" onclick="onCheck(this,'p4_worksAlone')"
								<%= props.getProperty("p4_worksAloneOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_worksAloneNo"
								name="p4_worksAloneNo" onclick="onCheck(this,'p4_worksAlone')"
								<%= props.getProperty("p4_worksAloneNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formWorksAlone" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio" id="p4_separatesOk"
								name="p4_separatesOk" onclick="onCheck(this,'p4_separates')"
								<%= props.getProperty("p4_separatesOk", "") %>></td>
							<td valign="top"><input type="radio" id="p4_separatesNo"
								name="p4_separatesNo" onclick="onCheck(this,'p4_separates')"
								<%= props.getProperty("p4_separatesNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke2006_4.formSeparates" /></td>
						</tr>
						<tr>
							<td valign="top"><input type="radio"
								id="p4_noParentsConcerns60mOk" name="p4_noParentsConcerns60mOk"
								onclick="onCheck(this,'p4_noParentsConcerns60m')"
								<%= props.getProperty("p4_noParentsConcerns60mOk", "") %>></td>
							<td valign="top"><input type="radio"
								id="p4_noParentsConcerns60mNo" name="p4_noParentsConcerns60mNo"
								onclick="onCheck(this,'p4_noParentsConcerns60m')"
								<%= props.getProperty("p4_noParentsConcerns60mNo", "") %>></td>
							<td valign="top"><bean:message
								key="oscarEncounter.formRourke1.formNoparentConcerns" /></td>
						</tr>
						<tr>
							<td valign="top" class="edcol" colspan="2"><input
								class="delete" type="button" value="Del"
								onclick="del('p4_counts2tenOk,p4_counts2tenNo,p4_speaksClearlyOk,p4_speaksClearlyNo,p4_throwsCatchesOk,p4_throwsCatchesNo,p4_hops1footOk,p4_hops1footNo,p4_sharesWillinglyOk,p4_sharesWillinglyNo,p4_worksAloneOk,p4_worksAloneNo,p4_separatesOk,p4_separatesNo,p4_noParentsConcerns60mOk,p4_noParentsConcerns60mNo');" /></td>
							<td>&nbsp;</td>
						</tr>
					</table>
					</td>
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
						name="p4_eyes18m" <%= props.getProperty("p4_eyes18m", "") %>></td>
					<td
						<i><a href="javascript:showNotes()" onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')" onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke1.formRedReflex"/>*</a></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p4_corneal18m" <%= props.getProperty("p4_corneal18m", "") %>></td>
					<td><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formCornealReflex" />*</a></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p4_hearing18m" <%= props.getProperty("p4_hearing18m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_4.formHearingInquiry" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p4_tonsil18m" <%= props.getProperty("p4_tonsil18m", "") %>></td>
					<td><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formTonsilSize" />*</a></td>
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
						name="p4_bloodpressure24m"
						<%= props.getProperty("p4_bloodpressure24m", "") %>></td>
					<td><i><bean:message
						key="oscarEncounter.formRourke2006_4.formBloodPressure" /></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p4_eyes24m" <%= props.getProperty("p4_eyes24m", "") %>></td>
					<td><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formRedReflex" />*</a></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p4_corneal24m" <%= props.getProperty("p4_corneal24m", "") %>></td>
					<td><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formCornealReflex" />*</a></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p4_hearing24m" <%= props.getProperty("p4_hearing24m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_4.formHearingInquiry" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p4_tonsil24m" <%= props.getProperty("p4_tonsil24m", "") %>></td>
					<td><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formTonsilSize" />*</a></td>
				</tr>
			</table>
			</td>
			<td colspan="2" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p4_bloodpressure48m"
						<%= props.getProperty("p4_bloodpressure48m", "") %>></td>
					<td><i><bean:message
						key="oscarEncounter.formRourke2006_4.formBloodPressure" /></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p4_eyes48m" <%= props.getProperty("p4_eyes48m", "") %>></td>
					<td><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_4.formRedReflex" />*</a></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p4_corneal48m" <%= props.getProperty("p4_corneal48m", "") %>></td>
					<td><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formCornealReflex" />*</a></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p4_hearing48m" <%= props.getProperty("p4_hearing48m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_4.formHearingInquiry" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p4_tonsil48m" <%= props.getProperty("p4_tonsil48m", "") %>></td>
					<td><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formTonsilSize" />*</a></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgProblemsAndPlans" /></a></td>
			<td colspan="3" valign="top"><textarea id="p4_problems18m"
				name="p4_problems18m" rows="5" cols="25" class="wide"><%= props.getProperty("p4_problems18m", "") %></textarea>
			</td>
			<td colspan="3" valign="top"><textarea id="p4_problems24m"
				name="p4_problems24m" rows="5" cols="25" class="wide"><%= props.getProperty("p4_problems24m", "") %></textarea>
			</td>
			<td colspan="2" valign="top"><textarea id="p4_problems48m"
				name="p4_problems48m" rows="5" cols="25" class="wide"><%= props.getProperty("p4_problems48m", "") %></textarea>
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
			<td style="text-align: center" colspan="2" valign="top"><b><bean:message
				key="oscarEncounter.formRourke2006_1.msgImmunizationColTitle" /></b></td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.formSignature" /></a></td>
			<td colspan="3"><input type="text" class="wide"
				style="width: 100%" name="p4_signature18m"
				value="<%= props.getProperty("p4_signature18m", "") %>" /></td>
			<td colspan="3"><input type="text" class="wide" maxlength="42"
				style="width: 100%" name="p4_signature24m"
				value="<%= props.getProperty("p4_signature24m", "") %>" /></td>
			<td colspan="2"><input type="text" class="wide"
				style="width: 100%" name="p4_signature48m"
				value="<%= props.getProperty("p4_signature48m", "") %>" /></td>
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
			<td nowrap="true"><a
				href="formrourke2006p1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg1" /></a>&nbsp;|&nbsp; <a
				href="formrourke2006p2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg2" /></a>&nbsp;|&nbsp; <a
				href="formrourke2006p3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2006.Pg3" /></a>&nbsp;|&nbsp; <a><bean:message
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
    Calendar.setup({ inputField : "p4_date18m", ifFormat : "%d/%m/%Y", showsTime :false, button : "p4_date18m_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "p4_date24m", ifFormat : "%d/%m/%Y", showsTime :false, button : "p4_date24m_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "p4_date48m", ifFormat : "%d/%m/%Y", showsTime :false, button : "p4_date48m_cal", singleClick : true, step : 1 });
</script>
</html:html>
