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
    
    //set as default "not discussed" for all radio buttons if no value is set for a group
    //e.g. if no value is found for p3_formulaFeeding9m then set p3_formulaFeeding9mNotDiscussed to checked
    function setDefault() {
    	var currentElem;
    	var currentgroup;
    	var notDiscussed;
    	var isChecked = false;
    	var pattern = /\'.+\'/;
    	var clickHandler;
    	
    	for( var idx = 0; idx < document.forms[0].elements.length; ++idx ) {
    		if( document.forms[0].elements[idx].type == "radio") {
    			clickHandler = document.forms[0].elements[idx].getAttributeNode('onclick');
    			if( clickHandler != null ) {
    				//We need to capture the group name which is present in onclick call to onCheck(this,group)
    				currentElem = new String(pattern.exec(clickHandler.value));
    				currentElem = currentElem.substring(1,currentElem.length-1);
    				if( idx == 0 ) {
    					currentgroup = currentElem;
    				}
    				    				
    				if( currentgroup != currentElem ) {    					
    					if( !isChecked ) {
    						notDiscussed = document.getElementById(currentgroup+"NotDiscussed");
    						if( notDiscussed != null ) {
    							notDiscussed.checked = true;
    						}    						
    					}
    					currentgroup = currentElem;
						isChecked = false;
    				}
    				
    				if( document.forms[0].elements[idx].checked ) {    					
    					isChecked = true;
    				}
    			}    				
    		}
    	}
    	
    	//capture last element if necessary
    	if( !isChecked ) {
    		notDiscussed = document.getElementById(currentgroup+"NotDiscussed");
			if( notDiscussed != null ) {
				notDiscussed.checked = true;
			} 
    	}
    	
    }

</script>

<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0" onload="setDefault()">
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
				<tr align="center">
					<td colspan="5"><textarea id="p3_nutrition9m"
						name="p3_nutrition9m" class="wide" rows="5" cols="25"><%= props.getProperty("p3_nutrition9m", "") %></textarea></td>
				</tr>
				<tr>
                    <td style="padding-right: 5pt" valign="top"><img height="15"
                    	width="20" src="graphics/Checkmark_L.gif"></td>
                    <td class="edcol" valign="top">X</td>
                    <td class="edcol" valign="top"><bean:message key="oscarEncounter.formRourke2009.formNo" /></td>
                    <td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td> 
                </tr>
                <tr>
                	<td valign="top"><input type="radio" id="p3_breastFeeding9mOk"
						name="p3_breastFeeding9mOk" onclick="onCheck(this,'p3_breastFeeding9m')"
						<%= props.getProperty("p3_breastFeeding9mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_breastFeeding9mOkConcerns"
						name="p3_breastFeeding9mOkConcerns" onclick="onCheck(this,'p3_breastFeeding9m')"
						<%= props.getProperty("p3_breastFeeding9mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_breastFeeding9mNo"
						name="p3_breastFeeding9mNo" onclick="onCheck(this,'p3_breastFeeding9m')"
						<%= props.getProperty("p3_breastFeeding9mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p3_breastFeeding9mNotDiscussed"
						name="p3_breastFeeding9mNotDiscussed" onclick="onCheck(this,'p3_breastFeeding9m')"
						<%= props.getProperty("p3_breastFeeding9mNotDiscussed", "") %>></td>
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
					<td valign="top"><input type="radio" id="p3_formulaFeeding9mOkConcerns"
						name="p3_formulaFeeding9mOkConcerns" onclick="onCheck(this,'p3_formulaFeeding9m')"
						<%= props.getProperty("p3_formulaFeeding9mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_formulaFeeding9mNo"
						name="p3_formulaFeeding9mNo" onclick="onCheck(this,'p3_formulaFeeding9m')"
						<%= props.getProperty("p3_formulaFeeding9mNo", "") %>></td>					
					<td valign="top"><input type="radio" id="p3_formulaFeeding9mNotDiscussed"
						name="p3_formulaFeeding9mNotDiscussed" onclick="onCheck(this,'p3_formulaFeeding9m')"
						<%= props.getProperty("p3_formulaFeeding9mNotDiscussed", "") %>></td>
					<td><i><bean:message
						key="oscarEncounter.formRourke2009_3.msgFormulaFeeding" /></i></td>
				</tr>
				<tr>
                   	<td valign="top"><input type="radio" id="p3_bottle9mOk"
						name="p3_bottle9mOk" onclick="onCheck(this,'p3_bottle9m')"
						<%= props.getProperty("p3_bottle9mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_bottle9mOkConcerns"
						name="p3_bottle9mOkConcerns" onclick="onCheck(this,'p3_bottle9m')"
						<%= props.getProperty("p3_bottle9mOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p3_bottle9mNotDiscussed"
						name="p3_bottle9mNotDiscussed" onclick="onCheck(this,'p3_bottle9m')"
						<%= props.getProperty("p3_bottle9mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_2.msgBottle" /></td>
				</tr>
				<tr>
                    <td valign="top"><input type="radio" id="p3_liquids9mOk"
						name="p3_liquids9mOk" onclick="onCheck(this,'p3_liquids9m')"
						<%= props.getProperty("p3_liquids9mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_liquids9mOkConcerns"
						name="p3_liquids9mOkConcerns" onclick="onCheck(this,'p3_liquids9m')"
						<%= props.getProperty("p3_liquids9mOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p3_liquids9mNotDiscussed"
						name="p3_liquids9mNotDiscussed" onclick="onCheck(this,'p3_liquids9m')"
						<%= props.getProperty("p3_liquids9mNotDiscussed", "") %>></td>					
					<td><bean:message
						key="oscarEncounter.formRourke2009_2.msgLiquids" /></td>
				</tr>
				<tr>
                    <td valign="top"><input type="radio" id="p3_cereal9mOk"
						name="p3_cereal9mOk" onclick="onCheck(this,'p3_cereal9m')"
						<%= props.getProperty("p3_cereal9mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_cereal9mOkConcerns"
						name="p3_cereal9mOkConcerns" onclick="onCheck(this,'p3_cereal9m')"
						<%= props.getProperty("p3_cereal9mOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p3_cereal9mNotDiscussed"
						name="p3_cereal9mNotDiscussed" onclick="onCheck(this,'p3_cereal9m')"
						<%= props.getProperty("p3_cereal9mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_3.msgCereal" /></td>
				</tr>
				<tr>
                   	<td valign="top"><input type="radio" id="p3_introCowMilk9mOk"
						name="p3_introCowMilk9mOk" onclick="onCheck(this,'p3_introCowMilk9m')"
						<%= props.getProperty("p3_cereal9mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_introCowMilk9mOkConcerns"
						name="p3_introCowMilk9mOkConcerns" onclick="onCheck(this,'p3_introCowMilk9m')"
						<%= props.getProperty("p3_cereal9mOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p3_introCowMilk9mNotDiscussed"
						name="p3_introCowMilk9mNotDiscussed" onclick="onCheck(this,'p3_introCowMilk9m')"
						<%= props.getProperty("p3_introCowMilk9mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_3.msgIntroCowMilk" /></td>
				</tr>
				<tr>
                    <td valign="top"><input type="radio" id="p3_egg9mOk"
						name="p3_egg9mOk" onclick="onCheck(this,'p3_egg9m')"
						<%= props.getProperty("p3_egg9mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_egg9mOkConcerns"
						name="p3_egg9mOkConcerns" onclick="onCheck(this,'p3_egg9m')"
						<%= props.getProperty("p3_egg9mOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p3_egg9mNotDiscussed"
						name="p3_egg9mNotDiscussed" onclick="onCheck(this,'p3_egg9m')"
						<%= props.getProperty("p3_egg9mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_2.msgEggWhites" /></td>
				</tr>
				<tr>    
                   	<td valign="top"><input type="radio" id="p3_choking9mOk"
						name="p3_choking9mOk" onclick="onCheck(this,'p3_choking9m')"
						<%= props.getProperty("p3_choking9mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_choking9mOkConcerns"
						name="p3_choking9mOkConcerns" onclick="onCheck(this,'p3_choking9m')"
						<%= props.getProperty("p3_choking9mOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p3_choking9mNotDiscussed"
						name="p3_choking9mNotDiscussed" onclick="onCheck(this,'p3_choking9m')"
						<%= props.getProperty("p3_choking9mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.msgChoking" />*</a></td>
				</tr>
                <tr>
					<td class="edcol" colspan="5" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_breastFeeding9mOk,p3_breastFeeding9mOkConcerns,p3_breastFeeding9mNo,p3_breastFeeding9mNotDiscussed,p3_formulaFeeding9mOk,p3_formulaFeeding9mOkConcerns,p3_formulaFeeding9mNo,p3_formulaFeeding9mNotDiscussed,p3_bottle9mOk,p3_bottle9mOkConcerns,p3_bottle9mNotDiscussed,p3_liquids9mOk,p3_liquids9mOkConcerns,p3_liquids9mNotDiscussed,p3_cereal9mOk,p3_cereal9mOkConcerns,p3_cereal9mNotDiscussed,p3_introCowMilk9mOk,p3_introCowMilk9mOkConcerns,p3_introCowMilk9mNotDiscussed,p3_egg9mOk,p3_egg9mOkConcerns,p3_egg9mNotDiscussed,p3_choking9mOk,p3_choking9mOkConcerns,p3_choking9mNotDiscussed');" /></td>
                </tr>
			</table>

			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="5"><textarea id="p3_nutrition12m"
						name="p3_nutrition12m" class="wide" rows="5" cols="25"><%= props.getProperty("p3_nutrition12m", "") %></textarea></td>
				</tr>
                <tr>
                                    <td style="padding-right: 5pt" valign="top"><img height="15"
                                    	width="20" src="graphics/Checkmark_L.gif"></td>
                                    <td class="edcol" valign="top">X</td>
                                    <td class="edcol" valign="top"><bean:message key="oscarEncounter.formRourke2009.formNo" /></td>
                    				<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td> 
                </tr>
                <tr>
                	<td valign="top"><input type="radio" id="p3_breastFeeding12mOk"
						name="p3_breastFeeding12mOk" onclick="onCheck(this,'p3_breastFeeding12m')"
						<%= props.getProperty("p3_breastFeeding12mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_breastFeeding12mOkConcerns"
						name="p3_breastFeeding12mOkConcerns" onclick="onCheck(this,'p3_breastFeeding12m')"
						<%= props.getProperty("p3_breastFeeding12mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_breastFeeding12mNo"
						name="p3_breastFeeding12mNo" onclick="onCheck(this,'p3_breastFeeding12m')"
						<%= props.getProperty("p3_breastFeeding12mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p3_breastFeeding12mNotDiscussed"
						name="p3_breastFeeding12mNotDiscussed" onclick="onCheck(this,'p3_breastFeeding12m')"
						<%= props.getProperty("p3_breastFeeding12mNotDiscussed", "") %>></td>
					<td><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.btnBreastFeeding" />*</a></b></td>
				</tr>
				<tr>
                   	<td valign="top"><input type="radio" id="p3_homoMilk12mOk"
						name="p3_homoMilk12mOk" onclick="onCheck(this,'p3_homoMilk12m')"
						<%= props.getProperty("p3_homoMilk12mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_homoMilk12mOkConcerns"
						name="p3_homoMilk12mOkConcerns" onclick="onCheck(this,'p3_homoMilk12m')"
						<%= props.getProperty("p3_homoMilk12mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_homoMilk12mNo"
						name="p3_homoMilk12mNo" onclick="onCheck(this,'p3_homoMilk12m')"
						<%= props.getProperty("p3_homoMilk12mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p3_homoMilk12mNotDiscussed"
						name="p3_homoMilk12mNotDiscussed" onclick="onCheck(this,'p3_homoMilk12m')"
						<%= props.getProperty("p3_homoMilk12mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formHomogenizedMilk" /></td>
				</tr>
				<tr>
                  	<td valign="top"><input type="radio" id="p3_cup12mOk"
						name="p3_cup12mOk" onclick="onCheck(this,'p3_cup12m')"
						<%= props.getProperty("p3_cup12mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_cup12mOkConcerns"
						name="p3_cup12mOkConcerns" onclick="onCheck(this,'p3_cup12m')"
						<%= props.getProperty("p3_cup12mOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p3_cup12mNotDiscussed"
						name="p3_cup12mNotDiscussed" onclick="onCheck(this,'p3_cup12m')"
						<%= props.getProperty("p3_cup12mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_3.formEncourageCup" /></td>
				</tr>
				<tr>
                    <td valign="top"><input type="radio" id="p3_appetite12mOk"
						name="p3_appetite12mOk" onclick="onCheck(this,'p3_appetite12m')"
						<%= props.getProperty("p3_appetite12mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_appetite12mOkConcerns"
						name="p3_appetite12mOkConcerns" onclick="onCheck(this,'p3_appetite12m')"
						<%= props.getProperty("p3_appetite12mOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p3_appetite12mNotDiscussed"
						name="p3_appetite12mNotDiscussed" onclick="onCheck(this,'p3_appetite12m')"
						<%= props.getProperty("p3_appetite12mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formAppetiteReduced" /></td>
				</tr>
				<tr>
                    <td valign="top"><input type="radio" id="p3_choking12mOk"
						name="p3_choking12mOk" onclick="onCheck(this,'p3_choking12m')"
						<%= props.getProperty("p3_choking12mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_choking12mOkConcerns"
						name="p3_choking12mOkConcerns" onclick="onCheck(this,'p3_choking12m')"
						<%= props.getProperty("p3_choking12mOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p3_choking12mNotDiscussed"
						name="p3_choking12mNotDiscussed" onclick="onCheck(this,'p3_choking12m')"
						<%= props.getProperty("p3_choking12mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.msgChoking" />*</a></td>
				</tr>
                <tr>
					<td class="edcol" colspan="5" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_breastFeeding12mOk,p3_breastFeeding12mOkConcerns,p3_breastFeeding12mNo,p3_breastFeeding12mNotDiscussed,p3_homoMilk12mOk,p3_homoMilk12mOkConcerns,p3_homoMilk12mNo,p3_homoMilk12mNotDiscussed,p3_cup12mOk,p3_cup12mOkConcerns,p3_cup12mNotDiscussed,p3_appetite12mOk,p3_appetite12mOkConcerns,p3_appetite12mNotDiscussed,p3_choking12mOk,p3_choking12mOkConcerns,p3_choking12mNotDiscussed');" /></td>
               </tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%" height="100%">
				<tr align="center">
					<td colspan="5"><textarea id="p3_nutrition15m"
						name="p3_nutrition15m" class="wide" rows="5" cols="25"><%= props.getProperty("p3_nutrition15m", "") %></textarea></td>
				</tr>
                                <tr>
                                    <td style="padding-right: 5pt" valign="top"><img height="15"
                                    	width="20" src="graphics/Checkmark_L.gif"></td>
                                    <td class="edcol" valign="top">X</td>
                                    <td class="edcol" valign="top"><bean:message key="oscarEncounter.formRourke2009.formNo" /></td>
                    				<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td> 
                                </tr>
         		<tr>
                	<td valign="top"><input type="radio" id="p3_breastFeeding15mOk"
						name="p3_breastFeeding15mOk" onclick="onCheck(this,'p3_breastFeeding15m')"
						<%= props.getProperty("p3_breastFeeding15mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_breastFeeding15mOkConcerns"
						name="p3_breastFeeding15mOkConcerns" onclick="onCheck(this,'p3_breastFeeding15m')"
						<%= props.getProperty("p3_breastFeeding15mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_breastFeeding15mNo"
						name="p3_breastFeeding15mNo" onclick="onCheck(this,'p3_breastFeeding15m')"
						<%= props.getProperty("p3_breastFeeding15mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p3_breastFeeding15mNotDiscussed"
						name="p3_breastFeeding15mNotDiscussed" onclick="onCheck(this,'p3_breastFeeding15m')"
						<%= props.getProperty("p3_breastFeeding15mNotDiscussed", "") %>></td>
					<td><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.btnBreastFeeding" />*</a></b></td>
				</tr>
				<tr>
                   	<td valign="top"><input type="radio" id="p3_homoMilk15mOk"
						name="p3_homoMilk15mOk" onclick="onCheck(this,'p3_homoMilk15m')"
						<%= props.getProperty("p3_homoMilk15mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_homoMilk15mOkConcerns"
						name="p3_homoMilk15mOkConcerns" onclick="onCheck(this,'p3_homoMilk15m')"
						<%= props.getProperty("p3_homoMilk15mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_homoMilk15mNo"
						name="p3_homoMilk15mNo" onclick="onCheck(this,'p3_homoMilk15m')"
						<%= props.getProperty("p3_homoMilk15mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p3_homoMilk15mNotDiscussed"
						name="p3_homoMilk15mNotDiscussed" onclick="onCheck(this,'p3_homoMilk15m')"
						<%= props.getProperty("p3_homoMilk15mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formHomogenizedMilk" /></td>
				</tr>
				<tr>
                    <td valign="top"><input type="radio" id="p3_choking15mOk"
						name="p3_choking15mOk" onclick="onCheck(this,'p3_choking15m')"
						<%= props.getProperty("p3_choking15mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_choking15mOkConcerns"
						name="p3_choking15mOkConcerns" onclick="onCheck(this,'p3_choking15m')"
						<%= props.getProperty("p3_choking15mOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p3_choking15mNotDiscussed"
						name="p3_choking15mNotDiscussed" onclick="onCheck(this,'p3_choking15m')"
						<%= props.getProperty("p3_choking15mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.msgChoking" />*</a></td>
				</tr>
				<tr>
                    <td valign="top"><input type="radio" id="p3_cup15mOk"
						name="p3_cup15mOk" onclick="onCheck(this,'p3_cup15m')"
						<%= props.getProperty("p3_cup15mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_cup15mOkConcerns"
						name="p3_cup15mOkConcerns" onclick="onCheck(this,'p3_cup15m')"
						<%= props.getProperty("p3_cup15mOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p3_cup15mNotDiscussed"
						name="p3_cup15mNotDiscussed" onclick="onCheck(this,'p3_cup15m')"
						<%= props.getProperty("p3_cup15mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_3.formEncourageCup" /></td>
				</tr>
                <tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_breastFeeding15mOk,p3_breastFeeding15mOkConcerns,p3_breastFeeding15mNo,p3_breastFeeding15mNotDiscussed,p3_homoMilk15mOk,p3_homoMilk15mOkConcerns,p3_homoMilk15mNo,p3_homoMilk15mNotDiscussed,p3_cup15mOk,p3_cup15mOkConcerns,p3_cup15mNotDiscussed,p3_choking15mOk,p3_choking15mOkConcerns,p3_choking15mNotDiscussed');" /></td>
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
					<td colspan="20">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="20"><bean:message
						key="oscarEncounter.formRourke2006_1.formInjuryPrev" /></td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_carSeatOk"
						name="p3_carSeatOk" onclick="onCheck(this,'p3_carSeat')"
						<%= props.getProperty("p3_carSeatOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_carSeatOkConcerns"
						name="p3_carSeatOkConcerns" onclick="onCheck(this,'p3_carSeat')"
						<%= props.getProperty("p3_carSeatOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_carSeatNotDiscussed"
						name="p3_carSeatNotDiscussed" onclick="onCheck(this,'p3_carSeat')"
						<%= props.getProperty("p3_carSeatNotDiscussed", "") %>></td>
					<td valign="top"><b><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_3.formCarSeat" /></a>*</b></td>
					<td colspan="4">&nbsp;</td>
					<td valign="top"><input type="radio" id="p3_poisonsOk"
						name="p3_poisonsOk" onclick="onCheck(this,'p3_poisons')"
						<%= props.getProperty("p3_poisonsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_poisonsOkConcerns"
						name="p3_poisonsOkConcerns" onclick="onCheck(this,'p3_poisons')"
						<%= props.getProperty("p3_poisonsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_poisonsNotDiscussed"
						name="p3_poisonsNotDiscussed" onclick="onCheck(this,'p3_poisons')"
						<%= props.getProperty("p3_poisonsNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formPoisons" /></a></b></td>
					<td valign="top"><input type="radio" id="p3_firearmSafetyOk"
						name="p3_firearmSafetyOk"
						onclick="onCheck(this,'p3_firearmSafety')"
						<%= props.getProperty("p3_firearmSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_firearmSafetyOkConcerns"
						name="p3_firearmSafetyOkConcerns"
						onclick="onCheck(this,'p3_firearmSafety')"
						<%= props.getProperty("p3_firearmSafetyOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_firearmSafetyNotDiscussed"
						name="p3_firearmSafetyNotDiscussed"
						onclick="onCheck(this,'p3_firearmSafety')"
						<%= props.getProperty("p3_firearmSafetyNotDiscussed", "") %>></td>					
					<td valign="top"><b><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formFireArm" />*</a></b></td>
					<td  colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_smokeSafetyOk"
						name="p3_smokeSafetyOk" onclick="onCheck(this,'p3_smokeSafety')"
						<%= props.getProperty("p3_smokeSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_smokeSafetyOkConcerns"
						name="p3_smokeSafetyOkConcerns" onclick="onCheck(this,'p3_smokeSafety')"
						<%= props.getProperty("p3_smokeSafetyOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_smokeSafetyNotDiscussed"
						name="p3_smokeSafetyNotDiscussed" onclick="onCheck(this,'p3_smokeSafety')"
						<%= props.getProperty("p3_smokeSafetyNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSmokeSafety" />*</a></td>
					<td  colspan="4">&nbsp;</td>
					<td valign="top"><input type="radio" id="p3_hotWaterOk"
						name="p3_hotWaterOk" onclick="onCheck(this,'p3_hotWater')"
						<%= props.getProperty("p3_hotWaterOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_hotWaterOkConcerns"
						name="p3_hotWaterOkConcerns" onclick="onCheck(this,'p3_hotWater')"
						<%= props.getProperty("p3_hotWaterOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_hotWaterNotDiscussed"
						name="p3_hotWaterNotDiscussed" onclick="onCheck(this,'p3_hotWater')"
						<%= props.getProperty("p3_hotWaterNotDiscussed", "") %>></td>
					<td valign="top"><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formHotWater" />*</a></i></td>
					<td colspan="8">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="4" valign="top"><bean:message
						key="oscarEncounter.formRourke2006_3.formChildProof" />
					<td valign="top"><input type="radio" id="p3_electricOk"
						name="p3_electricOk" onclick="onCheck(this,'p3_electric')"
						<%= props.getProperty("p3_electricOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_electricOkConcerns"
						name="p3_electricOkConcerns" onclick="onCheck(this,'p3_electric')"
						<%= props.getProperty("p3_electricOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_electricNotDiscussed"
						name="p3_electricNotDiscussed" onclick="onCheck(this,'p3_electric')"
						<%= props.getProperty("p3_electricNotDiscussed", "") %>></td>
					<td valign="top"><i><bean:message
						key="oscarEncounter.formRourke2006_2.formElectric" /></i></td>
					<td valign="top"><input type="radio" id="p3_fallsOk"
						name="p3_fallsOk" onclick="onCheck(this,'p3_falls')"
						<%= props.getProperty("p3_fallsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_fallsOkConcerns"
						name="p3_fallsOkConcerns" onclick="onCheck(this,'p3_falls')"
						<%= props.getProperty("p3_fallsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_fallsNotDiscussed"
						name="p3_fallsNotDiscussed" onclick="onCheck(this,'p3_falls')"
						<%= props.getProperty("p3_fallsNotDiscussed", "") %>></td>
					<td valign="top"><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formFalls" />*</a></i></td>
					<td valign="top"><input type="radio" id="p3_safeToysOk"
						name="p3_safeToysOk" onclick="onCheck(this,'p3_safeToys')"
						<%= props.getProperty("p3_safeToysOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_safeToysOkConcerns"
						name="p3_safeToysOkConcerns" onclick="onCheck(this,'p3_safeToys')"
						<%= props.getProperty("p3_safeToysOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_safeToysNotDiscussed"
						name="p3_safeToysNotDiscussed" onclick="onCheck(this,'p3_safeToys')"
						<%= props.getProperty("p3_safeToysNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSafeToys" />*</a></td>
					<td  colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td class="edcol" colspan="4" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_carSeatOk,p3_carSeatOkConcerns,p3_carSeatNotDiscussed,p3_smokeSafetyOk,p3_smokeSafetyOkConcerns,p3_smokeSafetyNotDiscussed');" /></td>
					<td class="edcol" colspan="4" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_electricOk,p3_electricOkConcerns,p3_electricNotDiscussed');" /></td>
					<td class="edcol" colspan="4" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_poisonsOk,p3_poisonsOkConcerns,p3_poisonsNotDiscussed,p3_hotWaterOk,p3_hotWaterOkConcerns,p3_hotWaterNotDiscussed,p3_fallsOk,p3_fallsOkConcerns,p3_fallsNotDiscussed');" /></td>
					<td class="edcol" colspan="4" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_firearmSafetyOk,p3_firearmSafetyOkConcerns,p3_firearmSafetyNotDiscussed,p3_safeToysOk,p3_safeToysOkConcerns,p3_safeToysNotDiscussed');" /></td>
				</tr>
				<tr>
					<td colspan="20">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="20"><bean:message
						key="oscarEncounter.formRourke2006_1.formBehaviour" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_sleepCryOk"
						name="p3_sleepCryOk" onclick="onCheck(this,'p3_sleepCry')"
						<%= props.getProperty("p3_sleepCryOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_sleepCryOkConcerns"
						name="p3_sleepCryOkConcerns" onclick="onCheck(this,'p3_sleepCry')"
						<%= props.getProperty("p3_sleepCryOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_sleepCryNotDiscussed"
						name="p3_sleepCryNotDiscussed" onclick="onCheck(this,'p3_sleepCry')"
						<%= props.getProperty("p3_sleepCryNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formsleepCry" />**</a></td>
					<td  colspan="4">&nbsp;</td>
					<td valign="top"><input type="radio" id="p3_soothabilityOk"
						name="p3_soothabilityOk" onclick="onCheck(this,'p3_soothability')"
						<%= props.getProperty("p3_soothabilityOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_soothabilityOkConcerns"
						name="p3_soothabilityOkConcerns" onclick="onCheck(this,'p3_soothability')"
						<%= props.getProperty("p3_soothabilityOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_soothabilityNotDiscussed"
						name="p3_soothabilityNotDiscussed" onclick="onCheck(this,'p3_soothability')"
						<%= props.getProperty("p3_soothabilityNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formSoothability" /></td>
					<td valign="top"><input type="radio" id="p3_homeVisitOk"
						name="p3_homeVisitOk" onclick="onCheck(this,'p3_homeVisit')"
						<%= props.getProperty("p3_homeVisitOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_homeVisitOkConcerns"
						name="p3_homeVisitOkConcerns" onclick="onCheck(this,'p3_homeVisit')"
						<%= props.getProperty("p3_homeVisitOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_homeVisitNotDiscussed"
						name="p3_homeVisitNotDiscussed" onclick="onCheck(this,'p3_homeVisit')"
						<%= props.getProperty("p3_homeVisitNotDiscussed", "") %>></td>
					<td valign="top"><b><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_3.formHomeVisit" />**</a></b></td>
					<td  colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_parentingOk"
						name="p3_parentingOk" onclick="onCheck(this,'p3_parenting')"
						<%= props.getProperty("p3_parentingOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_parentingOkConcerns"
						name="p3_parentingOkConcerns" onclick="onCheck(this,'p3_parenting')"
						<%= props.getProperty("p3_parentingOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_parentingNotDiscussed"
						name="p3_parentingNotDiscussed" onclick="onCheck(this,'p3_parenting')"
						<%= props.getProperty("p3_parentingNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_3.formParenting" /></td>
					<td valign="top"><input type="radio" id="p3_pFatigueOk"
						name="p3_pFatigueOk" onclick="onCheck(this,'p3_pFatigue')"
						<%= props.getProperty("p3_pFatigueOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_pFatigueOkConcerns"
						name="p3_pFatigueOkConcerns" onclick="onCheck(this,'p3_pFatigue')"
						<%= props.getProperty("p3_pFatigueOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_pFatigueNotDiscussed"
						name="p3_pFatigueNotDiscussed" onclick="onCheck(this,'p3_pFatigue')"
						<%= props.getProperty("p3_pFatigueNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formParentFatigue" />**</a></td>
					<td valign="top"><input type="radio" id="p3_famConflictOk"
						name="p3_famConflictOk" onclick="onCheck(this,'p3_famConflict')"
						<%= props.getProperty("p3_famConflictOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_famConflictOkConcerns"
						name="p3_famConflictOkConcerns" onclick="onCheck(this,'p3_famConflict')"
						<%= props.getProperty("p3_famConflictOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_famConflictNotDiscussed"
						name="p3_famConflictNotDiscussed" onclick="onCheck(this,'p3_famConflict')"
						<%= props.getProperty("p3_famConflictNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formFamConflict" /></td>
					<td valign="top"><input type="radio" id="p3_siblingsOk"
						name="p3_siblingsOk" onclick="onCheck(this,'p3_siblings')"
						<%= props.getProperty("p3_siblingsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_siblingsOkConcerns"
						name="p3_siblingsOkConcerns" onclick="onCheck(this,'p3_siblings')"
						<%= props.getProperty("p3_siblingsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_siblingsNotDiscussed"
						name="p3_siblingsNotDiscussed" onclick="onCheck(this,'p3_siblings')"
						<%= props.getProperty("p3_siblingsNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formSiblings" /></td>
					<td valign="top"><input type="radio" id="p3_childCareOk"
						name="p3_childCareOk" onclick="onCheck(this,'p3_childCare')"
						<%= props.getProperty("p3_childCareOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_childCareOkConcerns"
						name="p3_childCareOkConcerns" onclick="onCheck(this,'p3_childCare')"
						<%= props.getProperty("p3_childCareOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_childCareNotDiscussed"
						name="p3_childCareNotDiscussed" onclick="onCheck(this,'p3_childCare')"
						<%= props.getProperty("p3_childCareNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_2.formChildCare" /></td>
				</tr>
				<tr>
					<td colspan="4" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_sleepCryOk,p3_sleepCryOkConcerns,p3_sleepCryNotDiscussed,p3_parentingOk,p3_parentingOkConcerns,p3_parentingNotDiscussed');" /></td>
					<td colspan="4" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_pFatigueOk,p3_pFatigueOkConcerns,p3_pFatigueNotDiscussed');" /></td>
					<td colspan="4" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_soothabilityOk,p3_soothabilityOkConcerns,p3_soothabilityNotDiscussed,p3_famConflictOk,p3_famConflictOkConcerns,p3_famConflictNotDiscussed');" /></td>
					<td colspan="4" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_homeVisitOk,p3_homeVisitOkConcerns,p3_homeVisitNotDiscussed,p3_siblingsOk,p3_siblingsOkConcerns,p3_siblingsNotDiscussed');" /></td>
					<td colspan="4" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_childCareOk,p3_childCareOkConcerns,p3_childCareNotDiscussed');" /></td>
				</tr>
				<tr>
					<td colspan="20">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="20"><bean:message
						key="oscarEncounter.formRourke2006_1.formOtherIssues" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_2ndSmokeOk"
						name="p3_2ndSmokeOk" onclick="onCheck(this,'p3_2ndSmoke')"
						<%= props.getProperty("p3_2ndSmokeOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_2ndSmokeOkConcerns"
						name="p3_2ndSmokeOkConcerns" onclick="onCheck(this,'p3_2ndSmoke')"
						<%= props.getProperty("p3_2ndSmokeOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_2ndSmokeNotDiscussed"
						name="p3_2ndSmokeNotDiscussed" onclick="onCheck(this,'p3_2ndSmoke')"
						<%= props.getProperty("p3_2ndSmokeNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.formSecondHandSmoke" />*</a></b></td>
					<td valign="top"><input type="radio" id="p3_teethingOk"
						name="p3_teethingOk" onclick="onCheck(this,'p3_teething')"
						<%= props.getProperty("p3_teethingOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_teethingOkConcerns"
						name="p3_teethingOkConcerns" onclick="onCheck(this,'p3_teething')"
						<%= props.getProperty("p3_teethingOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_teethingNotDiscussed"
						name="p3_teethingNotDiscussed" onclick="onCheck(this,'p3_teething')"
						<%= props.getProperty("p3_teethingNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formTeething" />*</a></td>
					<td valign="top"><input type="radio" id="p3_altMedOk"
						name="p3_altMedOk" onclick="onCheck(this,'p3_altMed')"
						<%= props.getProperty("p3_altMedOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_altMedOkConcerns"
						name="p3_altMedOkConcerns" onclick="onCheck(this,'p3_altMed')"
						<%= props.getProperty("p3_altMedOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_altMedNotDiscussed"
						name="p3_altMedNotDiscussed" onclick="onCheck(this,'p3_altMed')"
						<%= props.getProperty("p3_altMedNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formAltMed" />*</a></td>
					<td valign="top"><input type="radio" id="p3_pacifierOk"
						name="p3_pacifierOk" onclick="onCheck(this,'p3_pacifier')"
						<%= props.getProperty("p3_pacifierOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_pacifierOkConcerns"
						name="p3_pacifierOkConcerns" onclick="onCheck(this,'p3_pacifier')"
						<%= props.getProperty("p3_pacifierOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_pacifierNotDiscussed"
						name="p3_pacifierNotDiscussed" onclick="onCheck(this,'p3_pacifier')"
						<%= props.getProperty("p3_pacifierNotDiscussed", "") %>></td>
					<td colspan="5" valign="top"><i><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formPacifierUse" />*</a></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_feverOk"
						name="p3_feverOk" onclick="onCheck(this,'p3_fever')"
						<%= props.getProperty("p3_feverOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_feverOkConcerns"
						name="p3_feverOkConcerns" onclick="onCheck(this,'p3_fever')"
						<%= props.getProperty("p3_feverOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_feverNotDiscussed"
						name="p3_feverNotDiscussed" onclick="onCheck(this,'p3_fever')"
						<%= props.getProperty("p3_feverNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formFever" />*</a></td>
					<td valign="top"><input type="radio" id="p3_activeOk"
						name="p3_activeOk" onclick="onCheck(this,'p3_active')"
						<%= props.getProperty("p3_activeOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_activeOkConcerns"
						name="p3_activeOkConcerns" onclick="onCheck(this,'p3_active')"
						<%= props.getProperty("p3_activeOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_activeNotDiscussed"
						name="p3_activeNotDiscussed" onclick="onCheck(this,'p3_active')"
						<%= props.getProperty("p3_activeNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_3.formactiveLife" />*</a></td>
					<td valign="top"><input type="radio" id="p3_readingOk"
						name="p3_readingOk" onclick="onCheck(this,'p3_reading')"
						<%= props.getProperty("p3_readingOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_readingOkConcerns"
						name="p3_readingOkConcerns" onclick="onCheck(this,'p3_reading')"
						<%= props.getProperty("p3_readingOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_readingNotDiscussed"
						name="p3_readingNotDiscussed" onclick="onCheck(this,'p3_reading')"
						<%= props.getProperty("p3_readingNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formEncourageReading" />**</a></b></td>
					<td valign="top"><input type="radio" id="p3_footwearOk"
						name="p3_footwearOk" onclick="onCheck(this,'p3_footwear')"
						<%= props.getProperty("p3_footwearOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_footwearOkConcerns"
						name="p3_footwearOkConcerns" onclick="onCheck(this,'p3_footwear')"
						<%= props.getProperty("p3_footwearOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_footwearNotDiscussed"
						name="p3_footwearNotDiscussed" onclick="onCheck(this,'p3_footwear')"
						<%= props.getProperty("p3_footwearNotDiscussed", "") %>></td>
					<td colspan="5" valign="top"><bean:message
						key="oscarEncounter.formRourke2006_3.formFootwear" /></td>
				</tr>
                <tr>
                    <td valign="top"><input type="radio" id="p3_coughMedOk"
						name="p3_coughMedOk" onclick="onCheck(this,'p3_coughMed')"
						<%= props.getProperty("p3_coughMedOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_coughMedOkConcerns"
						name="p3_coughMedOkConcerns" onclick="onCheck(this,'p3_coughMed')"
						<%= props.getProperty("p3_coughMedOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_coughMedNotDiscussed"
						name="p3_coughMedNotDiscussed" onclick="onCheck(this,'p3_coughMed')"
						<%= props.getProperty("p3_coughMedNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_1.formCough" />*</a></td>
                	<td colspan="16">&nbsp;</td>
                </tr>
				<tr>
					<td colspan="4" valign="top"><bean:message
						key="oscarEncounter.formRourke2006_3.formEnvHealth" /></td>
					<td valign="top"><input type="radio" id="p3_sunExposureOk"
						name="p3_sunExposureOk" onclick="onCheck(this,'p3_sunExposure')"
						<%= props.getProperty("p3_sunExposureOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_sunExposureOkConcerns"
						name="p3_sunExposureOkConcerns" onclick="onCheck(this,'p3_sunExposure')"
						<%= props.getProperty("p3_sunExposureOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_sunExposureNotDiscussed"
						name="p3_sunExposureNotDiscussed" onclick="onCheck(this,'p3_sunExposure')"
						<%= props.getProperty("p3_sunExposureNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSunExposure" />*</a></td>
					<td valign="top"><input type="radio" id="p3_checkSerumOk"
						name="p3_checkSerumOk" onclick="onCheck(this,'p3_checkSerum')"
						<%= props.getProperty("p3_checkSerumOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_checkSerumOkConcerns"
						name="p3_checkSerumOkConcerns" onclick="onCheck(this,'p3_checkSerum')"
						<%= props.getProperty("p3_checkSerumOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_checkSerumNotDiscussed"
						name="p3_checkSerumNotDiscussed" onclick="onCheck(this,'p3_checkSerum')"
						<%= props.getProperty("p3_checkSerumNotDiscussed", "") %>></td>
					<td valign="top"><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formCheckSerum" />*</a></i></td>
					<td valign="top"><input type="radio" id="p3_pesticidesOk"
						name="p3_pesticidesOk" onclick="onCheck(this,'p3_pesticides')"
						<%= props.getProperty("p3_pesticidesOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_pesticidesOkConcerns"
						name="p3_pesticidesOkConcerns" onclick="onCheck(this,'p3_pesticides')"
						<%= props.getProperty("p3_pesticidesOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_pesticidesNotDiscussed"
						name="p3_pesticidesNotDiscussed" onclick="onCheck(this,'p3_pesticides')"
						<%= props.getProperty("p3_pesticidesNotDiscussed", "") %>></td>
					<td valign="top"><i><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_2.formPesticides" />*</a></i></td>
					<td  colspan="4">&nbsp;</td>

				</tr>
				<tr>
					<td colspan="4" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_2ndSmokeOk,p3_2ndSmokeOkConcerns,p3_2ndSmokeNotDiscussed,p3_feverOk,p3_feverOkConcerns,p3_feverNotDiscussed,p3_coughMedOk,p3_coughMedOkConcerns,p3_coughMedNotDiscussed');" /></td>
					<td colspan="4" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_teethingOk,p3_teethingOkConcerns,p3_teethingNotDiscussed,p3_activeOk,p3_activeOkConcerns,p3_activeNotDiscussed,p3_sunExposureOk,p3_sunExposureOkConcerns,p3_sunExposureNotDiscussed');" /></td>
					<td colspan="4" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_altMedOk,p3_altMedOkConcerns,p3_altMedNotDiscussed,p3_readingOk,p3_readingOkConcerns,p3_readingNotDiscussed,p3_checkSerumOk,p3_checkSerumOkConcerns,p3_checkSerumNotDiscussed');" /></td>
					<td colspan="4" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_pacifierOk,p3_pacifierOkConcerns,p3_pacifierNotDiscussed,p3_footwearOk,p3_footwearOkConcerns,p3_footwearNotDiscussed,p3_pesticidesOk,p3_pesticidesOkConcerns,p3_pesticidesNotDiscussed');" /></td>
					<td colspan="4">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="20">&nbsp;</td>
				</tr>
			</table>
			<textarea id="p3_education"
						name="p3_education" class="wide" rows="5" cols="25"><%= props.getProperty("p3_education", "") %></textarea>
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
					<td colspan="4"><textarea id="p3_development9m"
						name="p3_development9m" rows="5" cols="25" class="wide"><%= props.getProperty("p3_development9m", "") %></textarea></td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_hiddenToyOk"
						name="p3_hiddenToyOk" onclick="onCheck(this,'p3_hiddenToy')"
						<%= props.getProperty("p3_hiddenToyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_hiddenToyOkConcerns"
						name="p3_hiddenToyOkConcerns" onclick="onCheck(this,'p3_hiddenToy')"
						<%= props.getProperty("p3_hiddenToyOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_hiddenToyNotDiscussed"
						name="p3_hiddenToyNotDiscussed" onclick="onCheck(this,'p3_hiddenToy')"
						<%= props.getProperty("p3_hiddenToyNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formhiddenToy" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_hiddenToyOk,p3_hiddenToyOkConcerns,p3_hiddenToyNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_soundsOk"
						name="p3_soundsOk" onclick="onCheck(this,'p3_sounds')"
						<%= props.getProperty("p3_soundsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_soundsOkConcerns"
						name="p3_soundsOkConcerns" onclick="onCheck(this,'p3_sounds')"
						<%= props.getProperty("p3_soundsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_soundsNotDiscussed"
						name="p3_soundsNotDiscussed" onclick="onCheck(this,'p3_sounds')"
						<%= props.getProperty("p3_soundsNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formSounds" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_soundsOk,p3_soundsOkConcerns,p3_soundsNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
                <tr>
					<td valign="top"><input type="radio" id="p3_responds2peopleOk"
						name="p3_responds2peopleOk" onclick="onCheck(this,'p3_responds2people')"
						<%= props.getProperty("p3_responds2peopleOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_responds2peopleOkConcerns"
						name="p3_responds2peopleOkConcerns" onclick="onCheck(this,'p3_responds2people')"
						<%= props.getProperty("p3_responds2peopleOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_responds2peopleNotDiscussed"
						name="p3_responds2peopleNotDiscussed" onclick="onCheck(this,'p3_responds2people')"
						<%= props.getProperty("p3_responds2peopleNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formResponds2people" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_responds2peopleOk,p3_responds2peopleOkConcerns,p3_responds2peopleNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_makeSoundsOk"
						name="p3_makeSoundsOk" onclick="onCheck(this,'p3_makeSounds')"
						<%= props.getProperty("p3_makeSoundsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_makeSoundsOkConcerns"
						name="p3_makeSoundsOkConcerns" onclick="onCheck(this,'p3_makeSounds')"
						<%= props.getProperty("p3_makeSoundsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_makeSoundsNotDiscussed"
						name="p3_makeSoundsNotDiscussed" onclick="onCheck(this,'p3_makeSounds')"
						<%= props.getProperty("p3_makeSoundsNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formMakeSounds" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_makeSoundsOk,p3_makeSoundsOkConcerns,p3_makeSoundsNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_sitsOk"
						name="p3_sitsOk" onclick="onCheck(this,'p3_sits')"
						<%= props.getProperty("p3_sitsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_sitsOkConcerns"
						name="p3_sitsOkConcerns" onclick="onCheck(this,'p3_sits')"
						<%= props.getProperty("p3_sitsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_sitsNotDiscussed"
						name="p3_sitsNotDiscussed" onclick="onCheck(this,'p3_sits')"
						<%= props.getProperty("p3_sitsNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_3.formSits" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_sitsOk,p3_sitsOkConcerns,p3_sitsNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_standsOk"
						name="p3_standsOk" onclick="onCheck(this,'p3_stands')"
						<%= props.getProperty("p3_standsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_standsOkConcerns"
						name="p3_standsOkConcerns" onclick="onCheck(this,'p3_stands')"
						<%= props.getProperty("p3_standsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_standsNotDiscussed"
						name="p3_standsNotDiscussed" onclick="onCheck(this,'p3_stands')"
						<%= props.getProperty("p3_standsNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formStands" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_standsOk,p3_standsOkConcerns,p3_standsNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_thumbOk"
						name="p3_thumbOk" onclick="onCheck(this,'p3_thumb')"
						<%= props.getProperty("p3_thumbOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_thumbOkConcerns"
						name="p3_thumbOkConcerns" onclick="onCheck(this,'p3_thumb')"
						<%= props.getProperty("p3_thumbOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_thumbNotDiscussed"
						name="p3_thumbNotDiscussed" onclick="onCheck(this,'p3_thumb')"
						<%= props.getProperty("p3_thumbNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formThumb&Index" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_thumbOk,p3_thumbOkConcerns,p3_thumbNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_playGamesOk"
						name="p3_playGamesOk" onclick="onCheck(this,'p3_playGames')"
						<%= props.getProperty("p3_playGamesOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_playGamesOkConcerns"
						name="p3_playGamesOkConcerns" onclick="onCheck(this,'p3_playGames')"
						<%= props.getProperty("p3_playGamesOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_playGamesNotDiscussed"
						name="p3_playGamesNotDiscussed" onclick="onCheck(this,'p3_playGames')"
						<%= props.getProperty("p3_playGamesNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formplayGames" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_playGamesOk,p3_playGamesOkConcerns,p3_playGamesNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
               	<tr>
					<td valign="top"><input type="radio"
						id="p3_attention9mOk" name="p3_attention9mOk"
						onclick="onCheck(this,'p3_attention9m')"
						<%= props.getProperty("p3_attention9mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_attention9mOkConcerns" name="p3_attention9mOkConcerns"
						onclick="onCheck(this,'p3_attention9m')"
						<%= props.getProperty("p3_attention9mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_attention9mNotDiscussed" name="p3_attention9mNotDiscussed"
						onclick="onCheck(this,'p3_attention9m')"
						<%= props.getProperty("p3_attention9mNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formAttention" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_attention9mOk,p3_attention9mOkConcerns,p3_attention9mNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio"
						id="p3_noParentsConcerns9mOk" name="p3_noParentsConcerns9mOk"
						onclick="onCheck(this,'p3_noParentsConcerns9m')"
						<%= props.getProperty("p3_noParentsConcerns9mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_noParentsConcerns9mOkConcerns" name="p3_noParentsConcerns9mOkConcerns"
						onclick="onCheck(this,'p3_noParentsConcerns9m')"
						<%= props.getProperty("p3_noParentsConcerns9mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_noParentsConcerns9mNotDiscussed" name="p3_noParentsConcerns9mNotDiscussed"
						onclick="onCheck(this,'p3_noParentsConcerns9m')"
						<%= props.getProperty("p3_noParentsConcerns9mNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009.formNoparentConcerns" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_noParentsConcerns9mOk,p3_noParentsConcerns9mOkConcerns,p3_noParentsConcerns9mNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top" align="center">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="4"><textarea id="p3_development12m"
						name="p3_development12m" rows="5" cols="25" class="wide"><%= props.getProperty("p3_development12m", "") %></textarea></td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_responds2nameOk"
						name="p3_respondsOk" onclick="onCheck(this,'p3_responds2name')"
						<%= props.getProperty("p3_respondsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_responds2nameOkConcerns"
						name="p3_respondsOkConcerns" onclick="onCheck(this,'p3_responds2name')"
						<%= props.getProperty("p3_respondsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_responds2nameNotDiscussed"
						name="p3_respondsNotDiscussed" onclick="onCheck(this,'p3_responds2name')"
						<%= props.getProperty("p3_respondsNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_3.formResponds" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_responds2nameOk,p3_responds2nameOkConcerns,p3_responds2nameNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_simpleRequestsOk"
						name="p3_simpleRequestsOk"
						onclick="onCheck(this,'p3_simpleRequests')"
						<%= props.getProperty("p3_simpleRequestsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_simpleRequestsOkConcerns"
						name="p3_simpleRequestsOkConcerns"
						onclick="onCheck(this,'p3_simpleRequests')"
						<%= props.getProperty("p3_simpleRequestsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_simpleRequestsNotDiscussed"
						name="p3_simpleRequestsNotDiscussed"
						onclick="onCheck(this,'p3_simpleRequests')"
						<%= props.getProperty("p3_simpleRequestsNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formSimpleReq" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_simpleRequestsOk,p3_simpleRequestsOkConcerns,p3_simpleRequestsNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_consonantOk"
						name="p3_consonantOk" onclick="onCheck(this,'p3_consonant')"
						<%= props.getProperty("p3_consonantOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_consonantOkConcerns"
						name="p3_consonantOkConcerns" onclick="onCheck(this,'p3_consonant')"
						<%= props.getProperty("p3_consonantOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_consonantNotDiscussed"
						name="p3_consonantNotDiscussed" onclick="onCheck(this,'p3_consonant')"
						<%= props.getProperty("p3_consonantNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formConsonant" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_consonantOk,p3_consonantOkConcerns,p3_consonantNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
                <tr>
					<td valign="top"><input type="radio" id="p3_says3wordsOk"
						name="p3_says3wordsOk" onclick="onCheck(this,'p3_says3words')"
						<%= props.getProperty("p3_says3wordsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_says3wordsOkConcerns"
						name="p3_says3wordsOkConcerns" onclick="onCheck(this,'p3_says3words')"
						<%= props.getProperty("p3_says3wordsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_says3wordsNotDiscussed"
						name="p3_says3wordsNotDiscussed" onclick="onCheck(this,'p3_says3words')"
						<%= props.getProperty("p3_says3wordsNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formsays3words" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_says3wordsOk,p3_says3wordsOkConcerns,p3_says3wordsNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_shufflesOk"
						name="p3_shufflesOk" onclick="onCheck(this,'p3_shuffles')"
						<%= props.getProperty("p3_shufflesOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_shufflesOkConcerns"
						name="p3_shufflesOkConcerns" onclick="onCheck(this,'p3_shuffles')"
						<%= props.getProperty("p3_shufflesOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_shufflesNotDiscussed"
						name="p3_shufflesNotDiscussed" onclick="onCheck(this,'p3_shuffles')"
						<%= props.getProperty("p3_shufflesNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formCrawls" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_shufflesOk,p3_shufflesOkConcerns,p3_shufflesNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_pull2standOk"
						name="p3_pull2standOk" onclick="onCheck(this,'p3_pull2stand')"
						<%= props.getProperty("p3_pull2standOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_pull2standOkConcerns"
						name="p3_pull2standOkConcerns" onclick="onCheck(this,'p3_pull2stand')"
						<%= props.getProperty("p3_pull2standOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_pull2standNotDiscussed"
						name="p3_pull2standNotDiscussed" onclick="onCheck(this,'p3_pull2stand')"
						<%= props.getProperty("p3_pull2standNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_3.formPulltoStand" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_pull2standOk,p3_pull2standOkConcerns,p3_pull2standNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_showDistressOk"
						name="p3_showDistressOk" onclick="onCheck(this,'p3_showDistress')"
						<%= props.getProperty("p3_showDistressOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_showDistressOkConcerns"
						name="p3_showDistressOkConcerns" onclick="onCheck(this,'p3_showDistress')"
						<%= props.getProperty("p3_showDistressOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_showDistressNotDiscussed"
						name="p3_showDistressNotDiscussed" onclick="onCheck(this,'p3_showDistress')"
						<%= props.getProperty("p3_showDistressNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formShowDistress" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_showDistressOk,p3_showDistressOkConcerns,p3_showDistressNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
                <tr>
					<td valign="top"><input type="radio" id="p3_followGazeOk"
						name="p3_followGazeOk" onclick="onCheck(this,'p3_followGaze')"
						<%= props.getProperty("p3_followGazeOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_followGazeOkConcerns"
						name="p3_followGazeOkConcerns" onclick="onCheck(this,'p3_followGaze')"
						<%= props.getProperty("p3_followGazeOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_followGazeNotDiscussed"
						name="p3_followGazeNotDiscussed" onclick="onCheck(this,'p3_followGaze')"
						<%= props.getProperty("p3_followGazeNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_3.formfollowGaze" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_followGazeOk,p3_followGazeOkConcerns,p3_followGazeNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio"
						id="p3_noParentsConcerns12mOk" name="p3_noParentsConcerns12mOk"
						onclick="onCheck(this,'p3_noParentsConcerns12m')"
						<%= props.getProperty("p3_noParentsConcerns12mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_noParentsConcerns12mOkConcerns" name="p3_noParentsConcerns12mOkConcerns"
						onclick="onCheck(this,'p3_noParentsConcerns12m')"
						<%= props.getProperty("p3_noParentsConcerns12mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_noParentsConcerns12mNotDiscussed" name="p3_noParentsConcerns12mNotDiscussed"
						onclick="onCheck(this,'p3_noParentsConcerns12m')"
						<%= props.getProperty("p3_noParentsConcerns12mNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2009.formNoparentConcerns" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_noParentsConcerns12mOk,p3_noParentsConcerns12mOkConcerns,p3_noParentsConcerns12mNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
                                <tr>
                                    <td colspan="4"><textarea id="p3_development15m"
						name="p3_development15m" rows="5" cols="25" class="wide"><%= props.getProperty("p3_development15m", "") %></textarea></td>
                                </tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_says5wordsOk"
						name="p3_says5wordsOk" onclick="onCheck(this,'p3_says5words')"
						<%= props.getProperty("p3_says5wordsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_says5wordsOkConcerns"
						name="p3_says5wordsOkConcerns" onclick="onCheck(this,'p3_says5words')"
						<%= props.getProperty("p3_says5wordsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_says5wordsNotDiscussed"
						name="p3_says5wordsNotDiscussed" onclick="onCheck(this,'p3_says5words')"
						<%= props.getProperty("p3_says5wordsNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_3.formSays5words" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="4"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_says5wordsOk,p3_says5wordsOkConcerns,p3_says5wordsNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>				
				<tr>
					<td valign="top"><input type="radio" id="p3_fingerFoodsOk"
						name="p3_fingerFoodsOk" onclick="onCheck(this,'p3_fingerFoods')"
						<%= props.getProperty("p3_fingerFoodsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_fingerFoodsOkConcerns"
						name="p3_fingerFoodsOkConcerns" onclick="onCheck(this,'p3_fingerFoods')"
						<%= props.getProperty("p3_fingerFoodsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_fingerFoodsNotDiscussed"
						name="p3_fingerFoodsNotDiscussed" onclick="onCheck(this,'p3_fingerFoods')"
						<%= props.getProperty("p3_fingerFoodsNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_3.formFingerFoods" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="4"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_fingerFoodsOk,p3_fingerFoodsOkConcerns,p3_fingerFoodsNotDiscussed');" /></td>
				</tr>
                <tr>
					<td valign="top"><input type="radio" id="p3_walksSidewaysOk"
						name="p3_walksSidewaysOk" onclick="onCheck(this,'p3_walksSideways')"
						<%= props.getProperty("p3_walksSidewaysOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_walksSidewaysOkConcerns"
						name="p3_walksSidewaysOkConcerns" onclick="onCheck(this,'p3_walksSideways')"
						<%= props.getProperty("p3_walksSidewaysOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_walksSidewaysNotDiscussed"
						name="p3_walksSidewaysNotDiscussed" onclick="onCheck(this,'p3_walksSideways')"
						<%= props.getProperty("p3_walksSidewaysNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_3.formwalksSideways" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="4"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_walksSidewaysOk,p3_walksSidewaysOkConcerns,p3_walksSidewaysNotDiscussed');" /></td>
				</tr>
               	<tr>
					<td valign="top"><input type="radio" id="p3_showsFearStrangersOk"
						name="p3_showsFearStrangersOk" onclick="onCheck(this,'p3_showsFearStrangers')"
						<%= props.getProperty("p3_showsFearStrangersOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_showsFearStrangersOkConcerns"
						name="p3_showsFearStrangersOkConcerns" onclick="onCheck(this,'p3_showsFearStrangers')"
						<%= props.getProperty("p3_showsFearStrangersOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_showsFearStrangersNotDiscussed"
						name="p3_showsFearStrangersNotDiscussed" onclick="onCheck(this,'p3_showsFearStrangers')"
						<%= props.getProperty("p3_showsFearStrangersNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_3.formshowsFearStrangers" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="4"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_showsFearStrangersOk,p3_showsFearStrangersOkConcerns,p3_showsFearStrangersNotDiscussed');" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_crawlsStairsOk"
						name="p3_crawlsStairsOk" onclick="onCheck(this,'p3_crawlsStairs')"
						<%= props.getProperty("p3_crawlsStairsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_crawlsStairsOkConcerns"
						name="p3_crawlsStairsOkConcerns" onclick="onCheck(this,'p3_crawlsStairs')"
						<%= props.getProperty("p3_crawlsStairsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_crawlsStairsNotDiscussed"
						name="p3_crawlsStairsNotDiscussed" onclick="onCheck(this,'p3_crawlsStairs')"
						<%= props.getProperty("p3_crawlsStairsNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_3.formCrawlsStairs" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="4"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_crawlsStairsOk,p3_crawlsStairsOkConcerns,p3_crawlsStairsNotDiscussed');" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_squatsOk"
						name="p3_squatsOk" onclick="onCheck(this,'p3_squats')"
						<%= props.getProperty("p3_squatsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_squatsOkConcerns"
						name="p3_squatsOkConcerns" onclick="onCheck(this,'p3_squats')"
						<%= props.getProperty("p3_squatsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_squatsNotDiscussed"
						name="p3_squatsNotDiscussed" onclick="onCheck(this,'p3_squats')"
						<%= props.getProperty("p3_squatsNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_3.formSquats" /></td>
				</tr>
				<tr>
					<td valign="bottom" class="edcol" colspan="4"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_squatsOk,p3_squatsOkConcerns,p3_squatsNotDiscussed');" /></td>
				</tr>				
				<tr>
					<td valign="top"><input type="radio"
						id="p3_noParentsConcerns15mOk" name="p3_noParentsConcerns15mOk"
						onclick="onCheck(this,'p3_noParentsConcerns15m')"
						<%= props.getProperty("p3_noParentsConcerns15mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_noParentsConcerns15mOkConcerns" name="p3_noParentsConcerns15mOkConcerns"
						onclick="onCheck(this,'p3_noParentsConcerns15m')"
						<%= props.getProperty("p3_noParentsConcerns15mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_noParentsConcerns15mNotDiscussed" name="p3_noParentsConcerns15mNotDiscussed"
						onclick="onCheck(this,'p3_noParentsConcerns15m')"
						<%= props.getProperty("p3_noParentsConcerns15mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009.formNoparentConcerns" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="4"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_noParentsConcerns15mOk,p3_noParentsConcerns15mOkConcerns,p3_noParentsConcerns15mNotDiscussed');" /></td>
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
			<textarea id="p3_physical9m"
						name="p3_physical9m" class="wide" rows="5" cols="25"><%= props.getProperty("p3_physical9m", "") %></textarea>
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td colspan="4">&nbsp;</td>
				</tr>
                <tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
				</tr>
                <tr>
					<td valign="top"><input type="radio"
						id="p3_fontanelles9mOk" name="p3_fontanelles9mOk"
						onclick="onCheck(this,'p3_fontanelles9m')"
						<%= props.getProperty("p3_fontanelles9mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_fontanelles9mOkConcerns" name="p3_fontanelles9mOkConcerns"
						onclick="onCheck(this,'p3_fontanelles9m')"
						<%= props.getProperty("p3_fontanelles9mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_fontanelles9mNotDiscussed" name="p3_fontanelles9mNotDiscussed"
						onclick="onCheck(this,'p3_fontanelles9m')"
						<%= props.getProperty("p3_fontanelles9mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="4"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_fontanelles9mOk,p3_fontanelles9mOkConcerns,p3_fontanelles9mNotDiscussed');" /></td>
                    <td>&nbsp;</td>
				</tr>
              	<tr>
					<td valign="top"><input type="radio"
						id="p3_eyes9mOk" name="p3_eyes9mOk"
						onclick="onCheck(this,'p3_eyes9m')"
						<%= props.getProperty("p3_eyes9mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_eyes9mOkConcerns" name="p3_eyes9mOkConcerns"
						onclick="onCheck(this,'p3_eyes9m')"
						<%= props.getProperty("p3_eyes9mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_eyes9mNotDiscussed" name="p3_eyes9mNotDiscussed"
						onclick="onCheck(this,'p3_eyes9m')"
						<%= props.getProperty("p3_eyes9mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke1.formRedReflex"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="4"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_eyes9mOk,p3_eyes9mOkConcerns,p3_eyes9mNotDiscussed');" /></td>
				</tr>
                <tr>
					<td valign="top"><input type="radio"
						id="p3_corneal9mOk" name="p3_corneal9mOk"
						onclick="onCheck(this,'p3_corneal9m')"
						<%= props.getProperty("p3_corneal9mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_corneal9mOkConcerns" name="p3_corneal9mOkConcerns"
						onclick="onCheck(this,'p3_corneal9m')"
						<%= props.getProperty("p3_corneal9mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_corneal9mNotDiscussed" name="p3_corneal9mNotDiscussed"
						onclick="onCheck(this,'p3_corneal9m')"
						<%= props.getProperty("p3_corneal9mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_3.formCornealReflex"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="4"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_corneal9mOk,p3_corneal9mOkConcerns,p3_corneal9mNotDiscussed');" /></td>
				</tr>
                <tr>
					<td valign="top"><input type="radio"
						id="p3_hearing9mOk" name="p3_hearing9mOk"
						onclick="onCheck(this,'p3_hearing9m')"
						<%= props.getProperty("p3_hearing9mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_hearing9mOkConcerns" name="p3_hearing9mOkConcerns"
						onclick="onCheck(this,'p3_hearing9m')"
						<%= props.getProperty("p3_hearing9mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_hearing9mNotDiscussed" name="p3_hearing9mNotDiscussed"
						onclick="onCheck(this,'p3_hearing9m')"
						<%= props.getProperty("p3_hearing9mNotDiscussed", "") %>></td>
					<td><i><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formHearingInquiry"/>*</a></i></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="4"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_hearing9mOk,p3_hearing9mOkConcerns,p3_hearing9mNotDiscussed');" /></td>
				</tr>
               	<tr>
					<td valign="top"><input type="radio"
						id="p3_hips9mOk" name="p3_hips9mOk"
						onclick="onCheck(this,'p3_hips9m')"
						<%= props.getProperty("p3_hips9mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_hips9mOkConcerns" name="p3_hips9mOkConcerns"
						onclick="onCheck(this,'p3_hips9m')"
						<%= props.getProperty("p3_hips9mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_hips9mNotDiscussed" name="p3_hips9mNotDiscussed"
						onclick="onCheck(this,'p3_hips9m')"
						<%= props.getProperty("p3_hips9mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_2.formHips"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="4"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_hips9mOk,p3_hips9mOkConcerns,p3_hips9mNotDiscussed');" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<textarea id="p3_physical12m"
						name="p3_physical12m" class="wide" rows="5" cols="25"><%= props.getProperty("p3_physical12m", "") %></textarea>
			<table cellpadding="0" cellspacing="0" width="100%">
                                <tr>
					<td colspan="3">&nbsp;</td>
				</tr>
                                <tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_fontanelles12mOk" name="p3_fontanelles12mOk"
						onclick="onCheck(this,'p3_fontanelles12m')"
						<%= props.getProperty("p3_fontanelles12mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_fontanelles12mOkConcerns" name="p3_fontanelles12mOkConcerns"
						onclick="onCheck(this,'p3_fontanelles12m')"
						<%= props.getProperty("p3_fontanelles12mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_fontanelles12mNotDiscussed" name="p3_fontanelles12mNotDiscussed"
						onclick="onCheck(this,'p3_fontanelles12m')"
						<%= props.getProperty("p3_fontanelles12mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_fontanelles12mOk,p3_fontanelles12mOkConcerns,p3_fontanelles12mNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_eyes12mOk" name="p3_eyes12mOk"
						onclick="onCheck(this,'p3_eyes12m')"
						<%= props.getProperty("p3_eyes12mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_eyes12mOkConcerns" name="p3_eyes12mOkConcerns"
						onclick="onCheck(this,'p3_eyes12m')"
						<%= props.getProperty("p3_eyes12mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_eyes12mNotDiscussed" name="p3_eyes12mNotDiscussed"
						onclick="onCheck(this,'p3_eyes12m')"
						<%= props.getProperty("p3_eyes12mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke1.formRedReflex"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_eyes12mOk,p3_eyes12mOkConcerns,p3_eyes12mNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_corneal12mOk" name="p3_corneal12mOk"
						onclick="onCheck(this,'p3_corneal12m')"
						<%= props.getProperty("p3_corneal12mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_corneal12mOkConcerns" name="p3_corneal12mOkConcerns"
						onclick="onCheck(this,'p3_corneal12m')"
						<%= props.getProperty("p3_corneal12mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_corneal12mNotDiscussed" name="p3_corneal12mNotDiscussed"
						onclick="onCheck(this,'p3_corneal12m')"
						<%= props.getProperty("p3_corneal12mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_3.formCornealReflex"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_corneal12mOk,p3_corneal12mOkConcerns,p3_corneal12mNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_hearing12mOk" name="p3_hearing12mOk"
						onclick="onCheck(this,'p3_hearing12m')"
						<%= props.getProperty("p3_hearing12mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_hearing12mOkConcerns" name="p3_hearing12mOkConcerns"
						onclick="onCheck(this,'p3_hearing12m')"
						<%= props.getProperty("p3_hearing12mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_hearing12mNotDiscussed" name="p3_hearing12mNotDiscussed"
						onclick="onCheck(this,'p3_hearing12m')"
						<%= props.getProperty("p3_hearing12mNotDiscussed", "") %>></td>
					<td><i><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formHearingInquiry"/>*</a></i></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_hearing12mOk,p3_hearing12mOkConcerns,p3_hearing12mNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_tonsil12mOk" name="p3_tonsil12mOk"
						onclick="onCheck(this,'p3_tonsil12m')"
						<%= props.getProperty("p3_tonsil12mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_tonsil12mOkConcerns" name="p3_tonsil12mOkConcerns"
						onclick="onCheck(this,'p3_tonsil12m')"
						<%= props.getProperty("p3_tonsil12mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_tonsil12mNotDiscussed" name="p3_tonsil12mNotDiscussed"
						onclick="onCheck(this,'p3_tonsil12m')"
						<%= props.getProperty("p3_tonsil12mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_3.formTonsilSize"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_tonsil12mOk,p3_tonsil12mOkConcerns,p3_tonsil12mNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_hips12mOk" name="p3_hips12mOk"
						onclick="onCheck(this,'p3_hips12m')"
						<%= props.getProperty("p3_hips12mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_hips12mOkConcerns" name="p3_hips12mOkConcerns"
						onclick="onCheck(this,'p3_hips12m')"
						<%= props.getProperty("p3_hips12mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_hips12mNotDiscussed" name="p3_hips12mNotDiscussed"
						onclick="onCheck(this,'p3_hips12m')"
						<%= props.getProperty("p3_hips12mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_2.formHips"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_hips12mOk,p3_hips12mOkConcerns,p3_hips12mNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<textarea id="p3_physical15m"
						name="p3_physical15m" class="wide" rows="5" cols="25"><%= props.getProperty("p3_physical15m", "") %></textarea>
			<table cellpadding="0" cellspacing="0" width="100%">
                                 <tr>
					<td colspan="3">&nbsp;</td>
				</tr>
                                <tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_fontanelles15mOk" name="p3_fontanelles15mOk"
						onclick="onCheck(this,'p3_fontanelles15m')"
						<%= props.getProperty("p3_fontanelles15mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_fontanelles15mOkConcerns" name="p3_fontanelles15mOkConcerns"
						onclick="onCheck(this,'p3_fontanelles15m')"
						<%= props.getProperty("p3_fontanelles15mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_fontanelles15mNotDiscussed" name="p3_fontanelles15mNotDiscussed"
						onclick="onCheck(this,'p3_fontanelles15m')"
						<%= props.getProperty("p3_fontanelles15mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_fontanelles15mOk,p3_fontanelles15mOkConcerns,p3_fontanelles15mNotDiscussed');" /></td>
                   	<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_eyes15mOk" name="p3_eyes15mOk"
						onclick="onCheck(this,'p3_eyes15m')"
						<%= props.getProperty("p3_eyes15mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_eyes15mOkConcerns" name="p3_eyes15mOkConcerns"
						onclick="onCheck(this,'p3_eyes15m')"
						<%= props.getProperty("p3_eyes15mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_eyes15mNotDiscussed" name="p3_eyes15mNotDiscussed"
						onclick="onCheck(this,'p3_eyes15m')"
						<%= props.getProperty("p3_eyes15mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke1.formRedReflex"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_eyes15mOk,p3_eyes15mOkConcerns,p3_eyes15mNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_corneal15mOk" name="p3_corneal15mOk"
						onclick="onCheck(this,'p3_corneal15m')"
						<%= props.getProperty("p3_corneal15mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_corneal15mOkConcerns" name="p3_corneal15mOkConcerns"
						onclick="onCheck(this,'p3_corneal15m')"
						<%= props.getProperty("p3_corneal15mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_corneal15mNotDiscussed" name="p3_corneal15mNotDiscussed"
						onclick="onCheck(this,'p3_corneal15m')"
						<%= props.getProperty("p3_corneal15mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_3.formCornealReflex"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_corneal15mOk,p3_corneal15mOkConcerns,p3_corneal15mNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_hearing15mOk" name="p3_hearing15mOk"
						onclick="onCheck(this,'p3_hearing15m')"
						<%= props.getProperty("p3_hearing15mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_hearing15mOkConcerns" name="p3_hearing15mOkConcerns"
						onclick="onCheck(this,'p3_hearing15m')"
						<%= props.getProperty("p3_hearing15mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_hearing15mNotDiscussed" name="p3_hearing15mNotDiscussed"
						onclick="onCheck(this,'p3_hearing15m')"
						<%= props.getProperty("p3_hearing15mNotDiscussed", "") %>></td>
					<td><i><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_1.formHearingInquiry"/>*</a></i></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_hearing15mOk,p3_hearing15mOkConcerns,p3_hearing15mNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_tonsil15mOk" name="p3_tonsil15mOk"
						onclick="onCheck(this,'p3_tonsil15m')"
						<%= props.getProperty("p3_tonsil15mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_tonsil15mOkConcerns" name="p3_tonsil15mOkConcerns"
						onclick="onCheck(this,'p3_tonsil15m')"
						<%= props.getProperty("p3_tonsil15mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_tonsil15mNotDiscussed" name="p3_tonsil15mNotDiscussed"
						onclick="onCheck(this,'p3_tonsil15m')"
						<%= props.getProperty("p3_tonsil15mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_3.formTonsilSize"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_tonsil15mOk,p3_tonsil15mOkConcerns,p3_tonsil15mNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio"
						id="p3_hips15mOk" name="p3_hips15mOk"
						onclick="onCheck(this,'p3_hips15m')"
						<%= props.getProperty("p3_hips15mOk", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_hips15mOkConcerns" name="p3_hips15mOkConcerns"
						onclick="onCheck(this,'p3_hips15m')"
						<%= props.getProperty("p3_hips15mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio"
						id="p3_hips15mNotDiscussed" name="p3_hips15mNotDiscussed"
						onclick="onCheck(this,'p3_hips15m')"
						<%= props.getProperty("p3_hips15mNotDiscussed", "") %>></td>
					<td><a href="javascript:showNotes()"
                                               onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
                                               onMouseOut="hideLayer()"><bean:message key="oscarEncounter.formRourke2006_2.formHips"/>*</a></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_hips15mOk,p3_hips15mOkConcerns,p3_hips15mNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>				
			</table>
			</td>
		</tr>
		<tr>			
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgProblemsAndPlans" /></a>
				<br>
				<img height="15" width="20" src="graphics/Checkmark_Lwhite.gif">
                <bean:message key="oscarEncounter.formRourke2009.msgProblemsLegendp2" />
            </td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="4"><textarea id="p3_problems9m"
						name="p3_problems9m" rows="5" cols="25" class="wide"><%= props.getProperty("p3_problems9m", "") %></textarea></td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDone"/></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_antiHB9mOk"
						name="p3_antiHB9mOk" onclick="onCheck(this,'p3_antiHB9m')" <%= props.getProperty("p3_antiHB9mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_antiHB9mOkConcerns"
						name="p3_antiHB9mOkConcerns" onclick="onCheck(this,'p3_antiHB9m')" <%= props.getProperty("p3_antiHB9mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_antiHB9mNotDiscussed"
						name="p3_antiHB9mNotDiscussed" onclick="onCheck(this,'p3_antiHB9m')" <%= props.getProperty("p3_antiHB9mNotDiscussed", "") %>></td>
					<td><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formAntiHB" />*</a></b><br />
					<bean:message key="oscarEncounter.formRourke2006_3.formAntiHBcond" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_hemoglobin9mOk"
						name="p3_hemoglobin9mOk"  onclick="onCheck(this,'p3_hemoglobin9m')"
						<%= props.getProperty("p3_hemoglobin9mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_hemoglobin9mOkConcerns"
						name="p3_hemoglobin9mOkConcerns"  onclick="onCheck(this,'p3_hemoglobin9m')"
						<%= props.getProperty("p3_hemoglobin9mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_hemoglobin9mNotDiscussed"
						name="p3_hemoglobin9mNotDiscussed"  onclick="onCheck(this,'p3_hemoglobin9m')"
						<%= props.getProperty("p3_hemoglobin9mNotDiscussed", "") %>></td>
					<td><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formHemoglobin" />*</a></i></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_antiHB9mOk,p3_antiHB9mOkConcerns,p3_antiHB9mNotDiscussed,p3_hemoglobin9mOk,p3_hemoglobin9mOkConcerns,p3_hemoglobin9mNotDiscussed');" /></td>
					<td>&nbsp;</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="4"><textarea id="p3_problems12m"
						name="p3_problems12m" rows="5" cols="25" class="wide"><%= props.getProperty("p3_problems12m", "") %></textarea></td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDone"/></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p3_hemoglobin12mOk"
						name="p3_hemoglobin12mOk"  onclick="onCheck(this,'p3_hemoglobin12m')"
						<%= props.getProperty("p3_hemoglobin12mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p3_hemoglobin12mOkConcerns"
						name="p3_hemoglobin12mOkConcerns"  onclick="onCheck(this,'p3_hemoglobin12m')"
						<%= props.getProperty("p3_hemoglobin12mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p3_hemoglobin12mNotDiscussed"
						name="p3_hemoglobin12mNotDiscussed"  onclick="onCheck(this,'p3_hemoglobin12m')"
						<%= props.getProperty("p3_hemoglobin12mNotDiscussed", "") %>></td>
					<td><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_3.formHemoglobin" />*</a></i></td>
				</tr>
								<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p3_hemoglobin12mOk,p3_hemoglobin12mOkConcerns,p3_hemoglobin12mNotDiscussed');" /></td>
					<td>&nbsp;</td>
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
