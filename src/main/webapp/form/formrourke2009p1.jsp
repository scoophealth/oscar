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
    String formLink = "formrourke2009p1.jsp";

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

<title>Rourke2009 Record 1</title>
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
        frm.action = "Rourke2009_Notes.pdf";
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
                
        document.forms[0].action = "../form/createpdf?__title=Rourke+Baby+Report+Pg1&__cfgfile=rourke2009printCfgPg1&__template=rourke2009p1";
        document.forms[0].target="_blank";            
        
        return true;
    }
    function onPrintAll() {
        document.forms[0].submit.value="printAll"; 
                
        document.forms[0].action = "../form/formname.do?__title=Rourke+Baby+Report&__cfgfile=rourke2009printCfgPg1&__cfgfile=rourke2009printCfgPg2&__cfgfile=rourke2009printCfgPg3&__cfgfile=rourke2009printCfgPg4&__template=rourke2009";
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
			<td nowrap="true"><a><bean:message
				key="oscarEncounter.formRourke2006.Pg1" /></a>&nbsp;|&nbsp; <a
				href="formrourke2009p2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
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
				key="oscarEncounter.formRourke1.msgRourkeBabyRecord" /></th>
		</tr>
	</table>

	<table cellpadding="0" cellspacing="0" width="100%" border="0">
		<tr valign="top">
			<td align="center"><bean:message
				key="oscarEncounter.formRourke2009.formBirhtRemarks" /><br>
                                        <input type="radio" id="p1_birthRemarksr1"
						name="p1_birthRemarksr1" onclick="onCheck(this,'p1_birthRemarksr')"
						<%= props.getProperty("p1_birthRemarksr1", "") %>><bean:message key="oscarEncounter.formRourke2009.formPremature" />&nbsp;
					<input type="radio" id="p1_birthRemarksr2"
						name="p1_birthRemarksr2" onclick="onCheck(this,'p1_birthRemarksr')"
						<%= props.getProperty("p1_birthRemarksr2", "") %>><bean:message key="oscarEncounter.formRourke2009.formHighRisk" />&nbsp;<br>
                                        <input type="radio" id="p1_birthRemarksr3"
						name="p1_birthRemarksr3" onclick="onCheck(this,'p1_birthRemarksr')"
						<%= props.getProperty("p1_birthRemarksr3", "") %>><bean:message key="oscarEncounter.formRourke2009.formNoConcerns" />&nbsp;
					
			
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
                    %>(<bean:message
				key="oscarEncounter.formRourke1.msgMale" />) <input type="hidden"
				name="c_male" value="x"> <%
                }else
                {
                    %>(<bean:message
				key="oscarEncounter.formRourke1.msgFemale" />) <input type="hidden"
				name="c_female" value="x"> <%
                }
                %>
                &nbsp;&nbsp;<bean:message
				key="oscarEncounter.formRourke2009.formFSA" /> <input type="text"
                           name="c_fsa" size="3" maxlength="3" value="<%= props.getProperty("c_fsa", "") %>">
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
                <tr>
                    <td style="padding:10px;"align="center">
                        <textarea wrap="physical" id="c_birthRemarks" name="c_birthRemarks"
				rows="6" cols="17"><%= props.getProperty("c_birthRemarks", "") %></textarea>
                    </td>
                    <td style="padding:10px;"align="center">
                        <table cellpadding="0" cellspacing="0" width="100%" border="0">
                            <tr>
                                <td align="center">
                                    <bean:message
                                            key="oscarEncounter.formRourke2009.formRiksFactors" />

                                    <br>
                                    <input type="checkbox" class="chk" name="p1_2ndhandsmoke" <%= props.getProperty("p1_2ndhandsmoke", "") %>>
                                    <bean:message
                                            key="oscarEncounter.formRourke2009.form2ndHandSmoke" />
                                    <p>
                                    <bean:message
                                            key="oscarEncounter.formRourke2009.formSubstanceabuse" /><br>
                                    <input type="checkbox" class="chk" name="p1_alcohol" <%= props.getProperty("p1_alcohol", "") %>>
                                    <bean:message
                                            key="oscarEncounter.formRourke2009.formAlcohol" /><br>
                                    <input type="checkbox" class="chk" name="p1_drugs" <%= props.getProperty("p1_drugs", "") %>>
                                    <bean:message
                                            key="oscarEncounter.formRourke2009.formDrugs" />
                                    </p>
                                </td>
                                <td nowrap align="center">
                                    <bean:message key="oscarEncounter.formRourke2009.formAPGAR" /><br>
                                    <bean:message key="oscarEncounter.formRourke2009.form1min" /><select name="c_APGAR1min"><option <%= props.getProperty("c_APGAR1min","").equals("") || props.getProperty("c_APGAR1min","").equals("-1") ? "selected='selected'": ""%> value="-1"><bean:message key="oscarEncounter.formRourke2009.formNotSet"/></option><% for( Integer idx = 0; idx <= 10; ++idx) { %><option <%= props.getProperty("c_APGAR1min","").equals(idx.toString()) ? "selected='selected'": ""%> value="<%=idx%>"><%=idx%></option><%}%></select><br>
                                    <bean:message key="oscarEncounter.formRourke2009.form5min" /><select name="c_APGAR5min"><option <%= props.getProperty("c_APGAR5min","").equals("") || props.getProperty("c_APGAR5min","").equals("-1") ? "selected='selected'": ""%> value="-1"><bean:message key="oscarEncounter.formRourke2009.formNotSet"/></option><% for( Integer idx = 0; idx <= 10; ++idx) { %><option <%= props.getProperty("c_APGAR5min","").equals(idx.toString()) ? "selected='selected'": ""%> value="<%=idx%>"><%=idx%></option><%}%></select><br>

                                </td>
                                <td align="center">
                                    <bean:message
                                    key="oscarEncounter.formRourke2009.formFamHistory" /><br>
                                    <textarea id="c_riskFactors" name="c_riskFactors" rows="5" cols="17"><%= props.getProperty("c_riskFactors", "") %></textarea>
                                </td>
                            </tr>
                        </table>
                    </td>
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
			<bean:message key="oscarEncounter.formRourke2009_1.btnGrowthmsg" /></a></td>
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
				key="oscarEncounter.formRourke1.msgNutrition" />*:</a></td>

			<td colspan="3" valign="bottom">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
                                    <td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
                                    <td class="edcol" valign="top">X</td>
                                    <td class="edcol" valign="top"><bean:message
						key="oscarEncounter.formRourke2009.formNo" /></td>
                                    <td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>                                   
                                </tr>
                                <tr>
                 	<td valign="top"><input type="radio" id="p1_breastFeeding1wOk"
						name="p1_breastFeeding1wOk" onclick="onCheck(this,'p1_breastFeeding1w')"
						<%= props.getProperty("p1_breastFeeding1wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_breastFeeding1wOkConcerns"
						name="p1_breastFeeding1wOkConcerns" onclick="onCheck(this,'p1_breastFeeding1w')"
						<%= props.getProperty("p1_breastFeeding1wOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_breastFeeding1wNo"
						name="p1_breastFeeding1wNo" onclick="onCheck(this,'p1_breastFeeding1w')"
						<%= props.getProperty("p1_breastFeeding1wNo", "") %>></td>	
					<td valign="top"><input type="radio" id="p1_breastFeeding1wNotDiscussed"
						name="p1_breastFeeding1wNotDiscussed" onclick="onCheck(this,'p1_breastFeeding1w')"
						<%= props.getProperty("p1_breastFeeding1wNotDiscussed", "") %>></td>
					<td><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.btnBreastFeeding" /><br />
					</a><span
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_1.msgBreastFeedingDescr" /></span></b></td>
				</tr>
				<tr>
                    <td valign="top"><input type="radio" id="p1_formulaFeeding1wOk"
						name="p1_formulaFeeding1wOk" onclick="onCheck(this,'p1_formulaFeeding1w')"
						<%= props.getProperty("p1_formulaFeeding1wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_formulaFeeding1wOkConcerns"
						name="p1_formulaFeeding1wOkConcerns" onclick="onCheck(this,'p1_formulaFeeding1w')"
						<%= props.getProperty("p1_formulaFeeding1wOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_formulaFeeding1wNo"
						name="p1_formulaFeeding1wNo" onclick="onCheck(this,'p1_formulaFeeding1w')"
						<%= props.getProperty("p1_formulaFeeding1wNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_formulaFeeding1wNotDiscussed"
						name="p1_formulaFeeding1wNotDiscussed" onclick="onCheck(this,'p1_formulaFeeding1w')"
						<%= props.getProperty("p1_formulaFeeding1wNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_1.msgFormulaFeeding" /></td>
				</tr>
				<tr>
                    <td valign="top"><input type="radio" id="p1_stoolUrine1wOk"
						name="p1_stoolUrine1wOk" onclick="onCheck(this,'p1_stoolUrine1w')"
						<%= props.getProperty("p1_stoolUrine1wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_stoolUrine1wOkConcerns"
						name="p1_stoolUrine1wOkConcerns" onclick="onCheck(this,'p1_stoolUrine1w')"
						<%= props.getProperty("p1_stoolUrine1wOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p1_stoolUrine1wNotDiscussed"
						name="p1_stoolUrine1wNotDiscussed" onclick="onCheck(this,'p1_stoolUrine1w')"
						<%= props.getProperty("p1_stoolUrine1wNotDiscussed", "") %>></td>
					
					<td><bean:message
						key="oscarEncounter.formRourke1.formStoolPatern" /></td>
				</tr>
                <tr>
					<td class="edcol" colspan="5" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_breastFeeding1wOk,p1_breastFeeding1wOkConcerns,p1_breastFeeding1wNo,p1_breastFeeding1wNotDiscussed,p1_formulaFeeding1wOk,p1_formulaFeeding1wOkConcerns,p1_formulaFeeding1wNo,p1_formulaFeeding1wNotDiscussed,p1_stoolUrine1wOk,p1_stoolUrine1wOkConcerns,p1_stoolUrine1wNotDiscussed');" /></td>
                                </tr>
			</table>
			<textarea id="p1_pNutrution1w"
				name="p1_pNutrition1w" style="width: 100%" cols="10" rows="5"><%= props.getProperty("p1_pNutrition1w", "") %></textarea>
			</td>
			</td>
			<td colspan="3" valign="bottom">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
                                    <td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
                                    <td class="edcol" valign="top">X</td>
                                    <td class="edcol" valign="top"><bean:message
										key="oscarEncounter.formRourke2009.formNo" /></td>
                                    <td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td> 
                                </tr>
                                <tr>
                    <td valign="top"><input type="radio" id="p1_breastFeeding2wOk"
						name="p1_breastFeeding2wOk" onclick="onCheck(this,'p1_breastFeeding2w')"
						<%= props.getProperty("p1_breastFeeding2wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_breastFeeding2wOkConcerns"
						name="p1_breastFeeding2wOkConcerns" onclick="onCheck(this,'p1_breastFeeding2w')"
						<%= props.getProperty("p1_breastFeeding2wOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_breastFeeding2wNo"
						name="p1_breastFeeding2wNo" onclick="onCheck(this,'p1_breastFeeding2w')"
						<%= props.getProperty("p1_breastFeeding2wNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_breastFeeding2wNotDiscussed"
						name="p1_breastFeeding2wNotDiscussed" onclick="onCheck(this,'p1_breastFeeding2w')"
						<%= props.getProperty("p1_breastFeeding2wNotDiscussed", "") %>></td>
					<td><b><a
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()" href="javascript:showNotes()"><bean:message
						key="oscarEncounter.formRourke2006_1.btnBreastFeeding" /></a><br />
					<span
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_1.msgBreastFeedingDescr" /></span></b></td>
				</tr>
				<tr>
                	<td valign="top"><input type="radio" id="p1_formulaFeeding2wOk"
						name="p1_formulaFeeding2wOk" onclick="onCheck(this,'p1_formulaFeeding2w')"
						<%= props.getProperty("p1_formulaFeeding2wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_formulaFeeding2wOkConcerns"
						name="p1_formulaFeeding2wOkConcerns" onclick="onCheck(this,'p1_formulaFeeding2w')"
						<%= props.getProperty("p1_formulaFeeding2wOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_formulaFeeding2wNo"
						name="p1_formulaFeeding2wNo" onclick="onCheck(this,'p1_formulaFeeding2w')"
						<%= props.getProperty("p1_formulaFeeding2wNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_formulaFeeding2wNotDiscussed"
						name="p1_formulaFeeding2wNotDiscussed" onclick="onCheck(this,'p1_formulaFeeding2w')"
						<%= props.getProperty("p1_formulaFeeding2wNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_1.msgFormulaFeeding" /></td>
				</tr>
				<tr>
                    <td valign="top"><input type="radio" id="p1_stoolUrine2wOk"
						name="p1_stoolUrine2wOk" onclick="onCheck(this,'p1_stoolUrine2w')"
						<%= props.getProperty("p1_stoolUrine2wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_stoolUrine2wOkConcerns"
						name="p1_stoolUrine2wOkConcerns" onclick="onCheck(this,'p1_stoolUrine2w')"
						<%= props.getProperty("p1_stoolUrine2wOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p1_stoolUrine2wNotDiscussed"
						name="p1_stoolUrine2wNotDiscussed" onclick="onCheck(this,'p1_stoolUrine2w')"
						<%= props.getProperty("p1_stoolUrine2wNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formStoolPatern" /></td>
				</tr>
                                <tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_breastFeeding2wOk,p1_breastFeeding2wOkConcerns,p1_breastFeeding2wNo,p1_breastFeeding2wNotDiscussed,p1_formulaFeeding2wOkConcerns,p1_formulaFeeding2wNotDiscussed,p1_formulaFeeding2wOk,p1_formulaFeeding2wNo,p1_stoolUrine2wOk,p1_stoolUrine2wOkConcerns,p1_stoolUrine2wNotDiscussed');" /></td>
                                </tr>
			</table>
			
			<textarea id="p1_pNutrution2w"
				name="p1_pNutrition2w" style="width: 100%" cols="10" rows="5"><%= props.getProperty("p1_pNutrition2w", "") %></textarea>
			
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%" height="100%">
                                <tr>
                                    <td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
                                    <td class="edcol" valign="top">X</td>
                                    <td class="edcol" valign="top"><bean:message
										key="oscarEncounter.formRourke2009.formNo" /></td>
                                    <td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
                                </tr>
                                <tr>
                    <td valign="top"><input type="radio" id="p1_breastFeeding1mOk"
						name="p1_breastFeeding1mOk" onclick="onCheck(this,'p1_breastFeeding1m')"
						<%= props.getProperty("p1_breastFeeding1mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_breastFeeding1mOkConcerns"
						name="p1_breastFeeding1mOkConcerns" onclick="onCheck(this,'p1_breastFeeding1m')"
						<%= props.getProperty("p1_breastFeeding1mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_breastFeeding1mNo"
						name="p1_breastFeeding1mNo" onclick="onCheck(this,'p1_breastFeeding1m')"
						<%= props.getProperty("p1_breastFeeding1mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_breastFeeding1mNotDiscussed"
						name="p1_breastFeeding1mNotDiscussed" onclick="onCheck(this,'p1_breastFeeding1m')"
						<%= props.getProperty("p1_breastFeeding1mNotDiscussed", "") %>></td>
					<td><b><a
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()" href="javascript:showNotes()"><bean:message
						key="oscarEncounter.formRourke2006_1.btnBreastFeeding" /></a><br />
					<span
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_1.msgBreastFeedingDescr" /></span></b></td>
				</tr>
				<tr>
                    <td valign="top"><input type="radio" id="p1_formulaFeeding1mOk"
						name="p1_formulaFeeding1mOk" onclick="onCheck(this,'p1_formulaFeeding1m')"
						<%= props.getProperty("p1_formulaFeeding1mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_formulaFeeding1mOkConcerns"
						name="p1_formulaFeeding1mOkConcerns" onclick="onCheck(this,'p1_formulaFeeding1m')"
						<%= props.getProperty("p1_formulaFeeding1mOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_formulaFeeding1mNo"
						name="p1_formulaFeeding1mNo" onclick="onCheck(this,'p1_formulaFeeding1m')"
						<%= props.getProperty("p1_formulaFeeding1mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_formulaFeeding1mNotDiscussed"
						name="p1_formulaFeeding1mNotDiscussed" onclick="onCheck(this,'p1_formulaFeeding1m')"
						<%= props.getProperty("p1_formulaFeeding1mNotDiscussed", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2009_1.msgFormulaFeeding" /></td>
				</tr>
				<tr>
                  	<td valign="top"><input type="radio" id="p1_stoolUrine1mOk"
						name="p1_stoolUrine1mOk" onclick="onCheck(this,'p1_stoolUrine1m')"
						<%= props.getProperty("p1_stoolUrine1mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_stoolUrine1mOkConcerns"
						name="p1_stoolUrine1mOkConcerns" onclick="onCheck(this,'p1_stoolUrine1m')"
						<%= props.getProperty("p1_stoolUrine1mOkConcerns", "") %>></td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p1_stoolUrine1mNotDiscussed"
						name="p1_stoolUrine1mNotDiscussed" onclick="onCheck(this,'p1_stoolUrine1m')"
						<%= props.getProperty("p1_stoolUrine1mNotDiscussed", "") %>></td>					
					<td><bean:message
						key="oscarEncounter.formRourke1.formStoolPatern" /></td>
				</tr>
                                <tr>
					<td class="edcol" colspan="3" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_breastFeeding1mOk,p1_breastFeeding1mOkConcerns,p1_breastFeeding1mNo,p1_breastFeeding1mNotDiscussed,p1_formulaFeeding1mOk,p1_formulaFeeding1mOkConcerns,p1_formulaFeeding1mNo,p1_formulaFeeding1mNotDiscussed,p1_stoolUrine1mOk,p1_stoolUrine1mOkConcerns,p1_stoolUrine1mNotDiscussed');" /></td>
                                </tr>
			</table>
			<textarea id="p1_pNutrution1m"
				name="p1_pNutrition1m" style="width: 100%" cols="10" rows="5"><%= props.getProperty("p1_pNutrition1m", "") %></textarea>
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
					<td colspan="16">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="16"><bean:message
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
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_carSeatOk"
						name="p1_carSeatOk" onclick="onCheck(this,'p1_carSeat')"
						<%= props.getProperty("p1_carSeatOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_carSeatOkConcerns"
						name="p1_carSeatOkConcerns" onclick="onCheck(this,'p1_carSeat')"
						<%= props.getProperty("p1_carSeatOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_carSeatNotDiscussed"
						name="p1_carSeatNotDiscussed" onclick="onCheck(this,'p1_carSeat')"
						<%= props.getProperty("p1_carSeatNotDiscussed", "") %>></td>
					<td valign="top"><b><a
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()" href="javascript:showNotes()"><bean:message
						key="oscarEncounter.formRourke1.formCarSeat" /></a>*</b></td>
					<td valign="top"><input type="radio" id="p1_sleepPosOk"
						name="p1_sleepPosOk" onclick="onCheck(this,'p1_sleepPos')"
						<%= props.getProperty("p1_sleepPosOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_sleepPosOkConcerns"
						name="p1_sleepPosOkConcerns" onclick="onCheck(this,'p1_sleepPos')"
						<%= props.getProperty("p1_sleepPosOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_sleepPosNotDiscussed"
						name="p1_sleepPosNotDiscussed" onclick="onCheck(this,'p1_sleepPos')"
						<%= props.getProperty("p1_sleepPosNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_1.formSleepPos" /></a></b></td>
					<td valign="top"><input type="radio" id="p1_cribSafetyOk"
						name="p1_cribSafetyOk" onclick="onCheck(this,'p1_cribSafety')"
						<%= props.getProperty("p1_cribSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_cribSafetyOkConcerns"
						name="p1_cribSafetyOkConcerns" onclick="onCheck(this,'p1_cribSafety')"
						<%= props.getProperty("p1_cribSafetyOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_cribSafetyNotDiscussed"
						name="p1_cribSafetyNotDiscussed" onclick="onCheck(this,'p1_cribSafety')"
						<%= props.getProperty("p1_cribSafetyNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.formCribSafety" />*</a></b></td>
					<td valign="top"><input type="radio" id="p1_firearmSafetyOk"
						name="p1_firearmSafetyOk"
						onclick="onCheck(this,'p1_firearmSafety')"
						<%= props.getProperty("p1_firearmSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_firearmSafetyOkConcerns"
						name="p1_firearmSafetyOkConcerns"
						onclick="onCheck(this,'p1_firearmSafety')"
						<%= props.getProperty("p1_firearmSafetyOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_firearmSafetyNotDiscussed"
						name="p1_firearmSafetyNotDiscussed"
						onclick="onCheck(this,'p1_firearmSafety')"
						<%= props.getProperty("p1_firearmSafetyNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formFireArm" />*</a></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_smokeSafetyOk"
						name="p1_smokeSafetyOk" onclick="onCheck(this,'p1_smokeSafety')"
						<%= props.getProperty("p1_smokeSafetyOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_smokeSafetyOkConcerns"
						name="p1_smokeSafetyOkConcerns" onclick="onCheck(this,'p1_smokeSafety')"
						<%= props.getProperty("p1_smokeSafetyOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_smokeSafetyNotDiscussed"
						name="p1_smokeSafetyNotDiscussed" onclick="onCheck(this,'p1_smokeSafety')"
						<%= props.getProperty("p1_smokeSafetyNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSmokeSafety" />*</a></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td valign="top"><input type="radio" id="p1_hotWaterOk"
						name="p1_hotWaterOk" onclick="onCheck(this,'p1_hotWater')"
						<%= props.getProperty("p1_hotWaterOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_hotWaterOkConcerns"
						name="p1_hotWaterOkConcerns" onclick="onCheck(this,'p1_hotWater')"
						<%= props.getProperty("p1_hotWaterOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_hotWaterNotDiscussed"
						name="p1_hotWaterNotDiscussed" onclick="onCheck(this,'p1_hotWater')"
						<%= props.getProperty("p1_hotWaterNotDiscussed", "") %>></td>
					<td valign="top"><i><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formHotWater" />*</a></i></td>
					<td valign="top"><input type="radio" id="p1_safeToysOk"
						name="p1_safeToysOk" onclick="onCheck(this,'p1_safeToys')"
						<%= props.getProperty("p1_safeToysOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_safeToysOkConcerns"
						name="p1_safeToysOkConcerns" onclick="onCheck(this,'p1_safeToys')"
						<%= props.getProperty("p1_safeToysOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_safeToysNotDiscussed"
						name="p1_safeToysNotDiscussed" onclick="onCheck(this,'p1_safeToys')"
						<%= props.getProperty("p1_safeToysNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSafeToys" />*</a></td>
				</tr>

				<tr>
					<td class="edcol" colspan="4" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_carSeatOk,p1_carSeatOkConcerns,p1_carSeatNotDiscussed,p1_smokeSafetyOk,p1_smokeSafetyOkConcerns,p1_smokeSafetyNotDiscussed');" /></td>
					<td class="edcol" colspan="4" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_sleepPosOk,p1_sleepPosOkConcerns,p1_sleepPosNotDiscussed');" /></td>
					<td class="edcol" colspan="4" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_cribSafetyOk,p1_cribSafetyOkConcerns,p1_cribSafetyNotDiscussed,p1_hotWaterOk,p1_hotWaterOkConcerns,p1_hotWaterNotDiscussed');" /></td>
					<td class="edcol" colspan="4" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_firearmSafetyOk,p1_firearmSafetyOkConcerns,p1_firearmSafetyNotDiscussed,p1_safeToysOk,p1_safeToysOkConcerns,p1_safeToysNotDiscussed');" /></td>
				</tr>
				<!-- </table>
          <br/>
          <table style="font-size:9pt;" cellpadding="0" cellspacing="0" width="100%"  >                    
          -->
				<tr>
					<td colspan="16">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="16"><bean:message
						key="oscarEncounter.formRourke2006_1.formBehaviour" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_sleepCryOk"
						name="p1_sleepCryOk" onclick="onCheck(this,'p1_sleepCry')"
						<%= props.getProperty("p1_sleepCryOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_sleepCryOkConcerns"
						name="p1_sleepCryOkConcerns" onclick="onCheck(this,'p1_sleepCry')"
						<%= props.getProperty("p1_sleepCryOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_sleepCryNotDiscussed"
						name="p1_sleepCryNotDiscussed" onclick="onCheck(this,'p1_sleepCry')"
						<%= props.getProperty("p1_sleepCryNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formsleepCry" />**</a></td>
					<td valign="top"><input type="radio" id="p1_soothabilityOk"
						name="p1_soothabilityOk" onclick="onCheck(this,'p1_soothability')"
						<%= props.getProperty("p1_soothabilityOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_soothabilityOkConcerns"
						name="p1_soothabilityOkConcerns" onclick="onCheck(this,'p1_soothability')"
						<%= props.getProperty("p1_soothabilityOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_soothabilityNotDiscussed"
						name="p1_soothabilityNotDiscussed" onclick="onCheck(this,'p1_soothability')"
						<%= props.getProperty("p1_soothabilityNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formSoothability" /></td>
					<td valign="top"><input type="radio" id="p1_homeVisitOk"
						name="p1_homeVisitOk" onclick="onCheck(this,'p1_homeVisit')"
						<%= props.getProperty("p1_homeVisitOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_homeVisitOkConcerns"
						name="p1_homeVisitOkConcerns" onclick="onCheck(this,'p1_homeVisit')"
						<%= props.getProperty("p1_homeVisitOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_homeVisitNotDiscussed"
						name="p1_homeVisitNotDiscussed" onclick="onCheck(this,'p1_homeVisit')"
						<%= props.getProperty("p1_homeVisitNotDiscussed", "") %>></td>
					<td colspan="5" valign="top"><b><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formHomeVisit" />**</a></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_bondingOk"
						name="p1_bondingOk" onclick="onCheck(this,'p1_bonding')"
						<%= props.getProperty("p1_bondingOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_bondingOkConcerns"
						name="p1_bondingOkConcerns" onclick="onCheck(this,'p1_bonding')"
						<%= props.getProperty("p1_bondingOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_bondingNotDiscussed"
						name="p1_bondingNotDiscussed" onclick="onCheck(this,'p1_bonding')"
						<%= props.getProperty("p1_bondingNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formBonding" /></td>
					<td valign="top"><input type="radio" id="p1_pFatigueOk"
						name="p1_pFatigueOk" onclick="onCheck(this,'p1_pFatigue')"
						<%= props.getProperty("p1_pFatigueOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_pFatigueOkConcerns"
						name="p1_pFatigueOkConcerns" onclick="onCheck(this,'p1_pFatigue')"
						<%= props.getProperty("p1_pFatigueOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_pFatigueNotDiscussed"
						name="p1_pFatigueNotDiscussed" onclick="onCheck(this,'p1_pFatigue')"
						<%= props.getProperty("p1_pFatigueNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote2" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formParentFatigue" />**</a></td>
					<td valign="top"><input type="radio" id="p1_famConflictOk"
						name="p1_famConflictOk" onclick="onCheck(this,'p1_famConflict')"
						<%= props.getProperty("p1_famConflictOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_famConflictOkConcerns"
						name="p1_famConflictOkConcerns" onclick="onCheck(this,'p1_famConflict')"
						<%= props.getProperty("p1_famConflictOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_famConflictNotDiscussed"
						name="p1_famConflictNotDiscussed" onclick="onCheck(this,'p1_famConflict')"
						<%= props.getProperty("p1_famConflictNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formFamConflict" /></td>
					<td valign="top"><input type="radio" id="p1_siblingsOk"
						name="p1_siblingsOk" onclick="onCheck(this,'p1_siblings')"
						<%= props.getProperty("p1_siblingsOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_siblingsOkConcerns"
						name="p1_siblingsOkConcerns" onclick="onCheck(this,'p1_siblings')"
						<%= props.getProperty("p1_siblingsOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_siblingsNotDiscussed"
						name="p1_siblingsNotDiscussed" onclick="onCheck(this,'p1_siblings')"
						<%= props.getProperty("p1_siblingsNotDiscussed", "") %>></td>
					<td valign="top"><bean:message
						key="oscarEncounter.formRourke2006_1.formSiblings" /></td>
				</tr>
				<tr>
					<td colspan="4" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_sleepCryOk,p1_sleepCryOkConcerns,p1_sleepCryNotDiscussed,p1_bondingOk,p1_bondingOkConcerns,p1_bondingNotDiscussed');" /></td>
					<td colspan="4" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_soothabilityOk,p1_soothabilityOkConcerns,p1_soothabilityNotDiscussed,p1_pFatigueOk,p1_pFatigueOkConcerns,p1_pFatigueNotDiscussed');" /></td>
					<td colspan="4" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_homeVisitOk,p1_homeVisitOkConcerns,p1_homeVisitNotDiscussed,p1_famConflictOk,p1_famConflictOkConcerns,p1_famConflictNotDiscussed');" /></td>
					<td colspan="4" class="edcol" colspan="2" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_siblingsOk,p1_siblingsOkConcerns,p1_siblingsNotDiscussed');" /></td>
				</tr>
				<!-- </table>
          <br/>
          <table style="font-size:9pt;" cellpadding="0" cellspacing="0" width="100%"  >
             -->
				<tr>
					<td colspan="16">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top" colspan="16"><bean:message
						key="oscarEncounter.formRourke2006_1.formOtherIssues" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_2ndSmokeOk"
						name="p1_2ndSmokeOk" onclick="onCheck(this,'p1_2ndSmoke')"
						<%= props.getProperty("p1_2ndSmokeOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_2ndSmokeOkConcerns"
						name="p1_2ndSmokeOkConcerns" onclick="onCheck(this,'p1_2ndSmoke')"
						<%= props.getProperty("p1_2ndSmokeOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_2ndSmokeNotDiscussed"
						name="p1_2ndSmokeNotDiscussed" onclick="onCheck(this,'p1_2ndSmoke')"
						<%= props.getProperty("p1_2ndSmokeNotDiscussed", "") %>></td>
					<td valign="top"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.formSecondHandSmoke" />*</a></b></td>
					<td valign="top"><input type="radio" id="p1_altMedOk"
						name="p1_altMedOk" onclick="onCheck(this,'p1_altMed')"
						<%= props.getProperty("p1_altMedOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_altMedOkConcerns"
						name="p1_altMedOkConcerns" onclick="onCheck(this,'p1_altMed')"
						<%= props.getProperty("p1_altMedOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_altMedNotDiscussed"
						name="p1_altMedNotDiscussed" onclick="onCheck(this,'p1_altMed')"
						<%= props.getProperty("p1_altMedNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formAltMed" />*</a></td>
					<td valign="top"><input type="radio" id="p1_pacifierOk"
						name="p1_pacifierOk" onclick="onCheck(this,'p1_pacifier')"
						<%= props.getProperty("p1_pacifierOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_pacifierOkConcerns"
						name="p1_pacifierOkConcerns" onclick="onCheck(this,'p1_pacifier')"
						<%= props.getProperty("p1_pacifierOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_pacifierNotDiscussed"
						name="p1_pacifierNotDiscussed" onclick="onCheck(this,'p1_pacifier')"
						<%= props.getProperty("p1_pacifierNotDiscussed", "") %>></td>
					<td colspan="5" valign="top"><b><a
						href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formPacifierUse" />*</a></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_feverOk"
						name="p1_feverOk" onclick="onCheck(this,'p1_fever')"
						<%= props.getProperty("p1_feverOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_feverOkConcerns"
						name="p1_feverOkConcerns" onclick="onCheck(this,'p1_fever')"
						<%= props.getProperty("p1_feverOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_feverNotDiscussed"
						name="p1_feverNotDiscussed" onclick="onCheck(this,'p1_fever')"
						<%= props.getProperty("p1_feverNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formFever" />*</a></td>
					<td valign="top"><input type="radio" id="p1_tmpControlOk"
						name="p1_tmpControlOk" onclick="onCheck(this,'p1_tmpControl')"
						<%= props.getProperty("p1_tmpControlOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_tmpControlOkConcerns"
						name="p1_tmpControlOkConcerns" onclick="onCheck(this,'p1_tmpControl')"
						<%= props.getProperty("p1_tmpControlOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_tmpControlNotDiscussed"
						name="p1_tmpControlNotDiscussed" onclick="onCheck(this,'p1_tmpControl')"
						<%= props.getProperty("p1_tmpControlNotDiscussed", "") %>></td>
					<td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formTempCtrl" />*</a></td>
					<td valign="top"><input type="radio" id="p1_sunExposureOk"
						name="p1_sunExposureOk" onclick="onCheck(this,'p1_sunExposure')"
						<%= props.getProperty("p1_sunExposureOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_sunExposureOkConcerns"
						name="p1_sunExposureOkConcerns" onclick="onCheck(this,'p1_sunExposure')"
						<%= props.getProperty("p1_sunExposureOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_sunExposureNotDiscussed"
						name="p1_sunExposureNotDiscussed" onclick="onCheck(this,'p1_sunExposure')"
						<%= props.getProperty("p1_sunExposureNotDiscussed", "") %>></td>
					<td colspan="5" valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formSunExposure" />*</a></td>
				</tr>
                <tr>
                    <td valign="top"><input type="radio" id="p1_noCoughMedOk"
						name="p1_noCoughMedOk" onclick="onCheck(this,'p1_noCoughMed')"
						<%= props.getProperty("p1_noCoughMedOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_noCoughMedOkConcerns"
						name="p1_noCoughMedOkConcerns" onclick="onCheck(this,'p1_noCoughMed')"
						<%= props.getProperty("p1_noCoughMedOkConcerns", "") %>></td>
                  	<td valign="top"><input type="radio" id="p1_noCoughMedNotDiscussed"
						name="p1_noCoughMedNotDiscussed" onclick="onCheck(this,'p1_noCoughMed')"
						<%= props.getProperty("p1_noCoughMedNotDiscussed", "") %>></td>
                    <td valign="top" colspan="13"><b><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2009_1.formCough" />*</a></b></td>
               	</tr>
				<tr>
					<td class="edcol" colspan="4" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_2ndSmokeOk,p1_2ndSmokeOkConcerns,p1_2ndSmokeNotDiscussed,p1_feverOk,p1_feverOkConcerns,p1_feverNotDiscussed,p1_noCoughMedOk,p1_noCoughMedOkConcerns,p1_noCoughMedNotDiscussed');" /></td>
					<td class="edcol" colspan="4" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_altMedOk,p1_altMedOkConcerns,p1_altMedNotDiscussed,p1_tmpControlOk,p1_tmpControlOkConcerns,p1_tmpControlNotDiscussed');" /></td>
					<td class="edcol" colspan="8" valign="top"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_pacifierOk,p1_pacifierOkConcerns,p1_pacifierNotDiscussed,p1_sunExposureOk,p1_sunExposureOkConcerns,p1_sunExposureNotDiscussed');" /></td>
				</tr>
				<tr>
					<td colspan="16">&nbsp;</td>
				</tr>
			</table>
			<textarea id="p1_pEducation"
				name="p1_pEducation" style="width: 100%" rows="5"><%= props.getProperty("p1_pEducation", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke2009.msgDevelopment" />**</a><br>
			<bean:message
				key="oscarEncounter.formRourke2009_1.msgDevelopmentDesc" /><br />
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
					<td colspan="3"><textarea id="p1_development2w"
						name="p1_development2w" rows="10" cols="25" class="wide"><%= props.getProperty("p1_development2w", "") %></textarea></td>
				</tr>
                                <tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
				</tr>
                <tr>
					<td valign="top"><input type="radio" id="p1_sucks2wOk"
						name="p1_sucks2wOk" onclick="onCheck(this,'p1_sucks2w')"
						<%= props.getProperty("p1_sucks2wOk", "") %>></td>
					<td><input type="radio" id="p1_sucks2wNo" name="p1_sucks2wNo"
						onclick="onCheck(this,'p1_sucks2w')"
						<%= props.getProperty("p1_sucks2wNo", "")%>></td>
					<td><input type="radio" id="p1_sucks2wNotDiscussed" name="p1_sucks2wNotDiscussed"
						onclick="onCheck(this,'p1_sucks2w')"
						<%= props.getProperty("p1_sucks2wNotDiscussed", "") %>><bean:message
						key="oscarEncounter.formRourke1.formSucksWell" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_sucks2wOk,p1_sucks2wNo,p1_sucks2wNotDiscussed');" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio"
						id="p1_noParentsConcerns2wOk" name="p1_noParentsConcerns2wOk"
						onclick="onCheck(this,'p1_noParentsConcerns2w')"
						<%= props.getProperty("p1_noParentsConcerns2wOk", "") %>></td>
					<td><input type="radio" id="p1_noParentsConcerns2wNo"
						name="p1_noParentsConcerns2wNo"
						onclick="onCheck(this,'p1_noParentsConcerns2w')"
						<%= props.getProperty("p1_noParentsConcerns2wNo", "") %>></td>
					<td><input type="radio" id="p1_noParentsConcerns2wNotDiscussed"
						name="p1_noParentsConcerns2wNotDiscussed"
						onclick="onCheck(this,'p1_noParentsConcerns2w')"
						<%= props.getProperty("p1_noParentsConcerns2wNotDiscussed", "") %>><bean:message
						key="oscarEncounter.formRourke1.formNoparentConcerns" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_noParentsConcerns2wOk,p1_noParentsConcerns2wNo,p1_noParentsConcerns2wNotDiscussed');" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="3"><textarea id="p1_development1m"
						name="p1_development1m" rows="5" cols="25" class="wide"><%= props.getProperty("p1_development1m", "") %></textarea></td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
					<td class="edcol" valign="top"><bean:message key="oscarEncounter.formRourke2009.formNotDiscussed"/></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_focusGaze1mOk"
						name="p1_focusGaze1mOk" onclick="onCheck(this,'p1_focusGaze1m')"
						<%= props.getProperty("p1_focusGaze1mOk", "") %>></td>
					<td><input type="radio" id="p1_focusGaze1mNo"
						name="p1_focusGaze1mNo" onclick="onCheck(this,'p1_focusGaze1m')"
						<%= props.getProperty("p1_focusGaze1mNo", "") %>></td>
					<td><input type="radio" id="p1_focusGaze1mNotDiscussed"
						name="p1_focusGaze1mNotDiscussed" onclick="onCheck(this,'p1_focusGaze1m')"
						<%= props.getProperty("p1_focusGaze1mNotDiscussed", "") %>><bean:message
						key="oscarEncounter.formRourke1.formFocusesGaze" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_focusGaze1mOk,p1_focusGaze1mNo,p1_focusGaze1mNotDiscussed');" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_startles1mOk"
						name="p1_startles1mOk" onclick="onCheck(this,'p1_startles1m')"
						<%= props.getProperty("p1_startles1mOk", "") %>></td>
					<td><input type="radio" id="p1_startles1mNo"
						name="p1_startles1mNo" onclick="onCheck(this,'p1_startles1m')"
						<%= props.getProperty("p1_startles1mNo", "") %>></td>
					<td><input type="radio" id="p1_startles1mNotDiscussed"
						name="p1_startles1mNotDiscussed" onclick="onCheck(this,'p1_startles1m')"
						<%= props.getProperty("p1_startles1mNotDiscussed", "") %>><bean:message
						key="oscarEncounter.formRourke1.formSuddenNoise" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_startles1mOk,p1_startles1mNo,p1_startles1mNotDiscussed');" /></td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_calms1mOk"
						name="p1_calms1mOk" onclick="onCheck(this,'p1_calms1m')"
						<%= props.getProperty("p1_calms1mOk", "") %>></td>
					<td><input type="radio" id="p1_calms1mNo"
						name="p1_calms1mNo" onclick="onCheck(this,'p1_calms1m')"
						<%= props.getProperty("p1_calms1mNo", "") %>></td>
					<td><input type="radio" id="p1_calms1mNotDiscussed"
						name="p1_calms1mNotDiscussed" onclick="onCheck(this,'p1_calms1m')"
						<%= props.getProperty("p1_calms1mNotDiscussed", "") %>><bean:message
						key="oscarEncounter.formRourke2009_1.formCalmes" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_calms1mOk,p1_calms1mNo,p1_calms1mNotDiscussed');" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_sucks1mOk"
						name="p1_sucks1mOk" onclick="onCheck(this,'p1_sucks1m')"
						<%= props.getProperty("p1_sucks1mOk", "") %>></td>
					<td><input type="radio" id="p1_sucks1mNo" name="p1_sucks1mNo"
						onclick="onCheck(this,'p1_sucks1m')"
						<%= props.getProperty("p1_sucks1mNo", "") %>></td>
					<td><input type="radio" id="p1_sucks1mNotDiscussed" name="p1_sucks1mNotDiscussed"
						onclick="onCheck(this,'p1_sucks1m')"
						<%= props.getProperty("p1_sucks1mNotDiscussed", "") %>><bean:message
						key="oscarEncounter.formRourke1.formSucksWell" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_sucks1mOk,p1_sucks1mNo,p1_sucks1mNotDiscussed');" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio"
						id="p1_noParentsConcerns1mOk" name="p1_noParentsConcerns1mOk"
						onclick="onCheck(this,'p1_noParentsConcerns1m')"
						<%= props.getProperty("p1_noParentsConcerns1mOk", "") %>></td>
					<td><input type="radio" id="p1_noParentsConcerns1mNo"
						name="p1_noParentsConcerns1mNo"
						onclick="onCheck(this,'p1_noParentsConcerns1m')"
						<%= props.getProperty("p1_noParentsConcerns1mNo", "") %>></td>
					<td><input type="radio" id="p1_noParentsConcerns1mNotDiscussed"
						name="p1_noParentsConcerns1mNotDiscussed"
						onclick="onCheck(this,'p1_noParentsConcerns1m')"
						<%= props.getProperty("p1_noParentsConcerns1mNotDiscussed", "") %>><bean:message
						key="oscarEncounter.formRourke1.formNoparentConcerns" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_noParentsConcerns1mOk,p1_noParentsConcerns1mNo,p1_noParentsConcerns1mNotDiscussed');" /></td>
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
                        <bean:message
				key="oscarEncounter.formRourke2009.msgPhysicalExaminationLegend" />
			
			</td>
			<td colspan="3" valign="bottom">
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
					<td valign="top"><input type="radio" id="p1_skin1wOk"
						name="p1_skin1wOk" onclick="onCheck(this,'p1_skin1w')"
						<%= props.getProperty("p1_skin1wOk", "") %>></td>
                    <td valign="top"><input type="radio" id="p1_skin1wNo"
						name="p1_skin1wNo" onclick="onCheck(this,'p1_skin1w')"
						<%= props.getProperty("p1_skin1wNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_skin1wNotDiscussed"
						name="p1_skin1wNotDiscussed" onclick="onCheck(this,'p1_skin1w')"
						<%= props.getProperty("p1_skin1wNotDiscussed", "") %>></td>
                    <td valign="top"><bean:message
						key="oscarEncounter.formRourke1.formDrySkin" /></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_skin1wOk,p1_skin1wNo,p1_skin1wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_fontanelles1wOk"
						name="p1_fontanelles1wOk" onclick="onCheck(this,'p1_fontanelles1w')"
						<%= props.getProperty("p1_fontanelles1wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_fontanelles1wNo"
						name="p1_fontanelles1wNo" onclick="onCheck(this,'p1_fontanelles1w')"
						<%= props.getProperty("p1_fontanelles1wNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_fontanelles1wNotDiscussed"
						name="p1_fontanelles1wNotDiscussed" onclick="onCheck(this,'p1_fontanelles1w')"
						<%= props.getProperty("p1_fontanelles1wNotDiscussed", "") %>></td>
                                        <td valign="top"><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_fontanelles1wOk,p1_fontanelles1wNo,p1_fontanelles1wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_eyes1wOk"
						name="p1_eyes1wOk" onclick="onCheck(this,'p1_eyes1w')"
						<%= props.getProperty("p1_eyes1wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_eyes1wNo"
						name="p1_eyes1wNo" onclick="onCheck(this,'p1_eyes1w')"
						<%= props.getProperty("p1_eyes1wNo", "") %>></td>
						<td valign="top"><input type="radio" id="p1_eyes1wNotDiscussed"
						name="p1_eyes1wNotDiscussed" onclick="onCheck(this,'p1_eyes1w')"
						<%= props.getProperty("p1_eyes1wNotDiscussed", "") %>></td>
                    <td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.formRedReflex" />*</a></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_eyes1wOk,p1_eyes1wNo,p1_eyes1wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_ears1wOk"
						name="p1_ears1wOk" onclick="onCheck(this,'p1_ears1w')"
						<%= props.getProperty("p1_ears1wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_ears1wNo"
						name="p1_ears1wNo" onclick="onCheck(this,'p1_ears1w')"
                                                <%= props.getProperty("p1_ears1wNo", "") %>></td>
                    <td valign="top"><input type="radio" id="p1_ears1wNotDiscussed"
						name="p1_ears1wNotDiscussed" onclick="onCheck(this,'p1_ears1w')"
                                                <%= props.getProperty("p1_ears1wNotDiscussed", "") %>></td>
                    <td valign="top"><a href="javascript:showNotes()"
                                                onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><i><bean:message
						key="oscarEncounter.formRourke2006_1.formEarDrums" />*</i></a></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_ears1wOk,p1_ears1wNo,p1_ears1wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                <tr>
					<td valign="top"><input type="radio" id="p1_heartLungs1wOk"
						name="p1_heartLungs1wOk" onclick="onCheck(this,'p1_heartLungs1w')"
						<%= props.getProperty("p1_heartLungs1wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_heartLungs1wNo"
						name="p1_heartLungs1wNo" onclick="onCheck(this,'p1_heartLungs1w')"
						<%= props.getProperty("p1_heartLungs1wNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_heartLungs1wNotDiscussed"
						name="p1_heartLungs1wNotDiscussed" onclick="onCheck(this,'p1_heartLungs1w')"
						<%= props.getProperty("p1_heartLungs1wNotDiscussed", "") %>></td>
                                        <td valign="top"><bean:message
						key="oscarEncounter.formRourke1.formHeart" /></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_heartLungs1wOk,p1_heartLungs1wNo,p1_heartLungs1wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_umbilicus1wOk"
						name="p1_umbilicus1wOk" onclick="onCheck(this,'p1_umbilicus1w')"
						<%= props.getProperty("p1_umbilicus1wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_umbilicus1wNo"
						name="p1_umbilicus1wNo" onclick="onCheck(this,'p1_umbilicus1w')"
                                                <%= props.getProperty("p1_umbilicus1wNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_umbilicus1wNotDiscussed"
						name="p1_umbilicus1wNotDiscussed" onclick="onCheck(this,'p1_umbilicus1w')"
                                                <%= props.getProperty("p1_umbilicus1wNotDiscussed", "") %>></td>                                               
                                        <td valign="top"><bean:message
						key="oscarEncounter.formRourke1.formUmbilicus" /></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="2"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_umbilicus1wOk,p1_umbilicus1wNo,p1_umbilicus1wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_femoralPulses1wOk"
						name="p1_femoralPulses1wOk" onclick="onCheck(this,'p1_femoralPulses1w')"
						<%= props.getProperty("p1_femoralPulses1wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_femoralPulses1wNo"
						name="p1_femoralPulses1wNo" onclick="onCheck(this,'p1_femoralPulses1w')"
                                                <%= props.getProperty("p1_femoralPulses1wNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_femoralPulses1wNotDiscussed"
						name="p1_femoralPulses1wNotDiscussed" onclick="onCheck(this,'p1_femoralPulses1w')"
                                                <%= props.getProperty("p1_femoralPulses1wNotDiscussed", "") %>></td>                                               
                                        <td valign="top"><bean:message
						key="oscarEncounter.formRourke1.formFemoralPulses" /></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_femoralPulses1wOk,p1_femoralPulses1wNo,p1_femoralPulses1wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_hips1wOk"
						name="p1_hips1wOk" onclick="onCheck(this,'p1_hips1w')"
						<%= props.getProperty("p1_hips1wOk", "") %>></td>
                                        <td valign="top"><input type="radio" id="p1_hips1wNo"
						name="p1_hips1wNo" onclick="onCheck(this,'p1_hips1w')"
						<%= props.getProperty("p1_hips1wNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_hips1wNotDiscussed"
						name="p1_hips1wNotDiscussed" onclick="onCheck(this,'p1_hips1w')"
						<%= props.getProperty("p1_hips1wNotDiscussed", "") %>></td>
                                        <td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.formHips" />*</a></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_hips1wOk,p1_hips1wNo,p1_hips1wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_muscleTone1wOk"
						name="p1_muscleTone1wOk" onclick="onCheck(this,'p1_muscleTone1w')"
						<%= props.getProperty("p1_muscleTone1wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_muscleTone1wNo"
						name="p1_muscleTone1wNo" onclick="onCheck(this,'p1_muscleTone1w')"
                                                <%= props.getProperty("p1_muscleTone1wNo", "") %>></td>
                   	<td valign="top"><input type="radio" id="p1_muscleTone1wNotDiscussed"
						name="p1_muscleTone1wNotDiscussed" onclick="onCheck(this,'p1_muscleTone1w')"
                                                <%= props.getProperty("p1_muscleTone1wNotDiscussed", "") %>></td>
                                        <td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formMuscleTone" />*</a></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_muscleTone1wOk,p1_muscleTone1wNo,p1_muscleTone1wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_testicles1wOk"
						name="p1_testicles1wOk" onclick="onCheck(this,'p1_testicles1w')"
						<%= props.getProperty("p1_testicles1wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_testicles1wNo"
						name="p1_testicles1wNo" onclick="onCheck(this,'p1_testicles1w')"
						<%= props.getProperty("p1_testicles1wNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_testicles1wNotDiscussed"
						name="p1_testicles1wNotDiscussed" onclick="onCheck(this,'p1_testicles1w')"
						<%= props.getProperty("p1_testicles1wNotDiscussed", "") %>></td>
                                        <td valign="top"><bean:message
						key="oscarEncounter.formRourke1.formTescicles" /></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_testicles1wOk,p1_testicles1wNo,p1_testicles1wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_maleUrinary1wOk"
						name="p1_maleUrinary1wOk" onclick="onCheck(this,'p1_maleUrinary1w')"
						<%= props.getProperty("p1_maleUrinary1wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_maleUrinary1wNo"
						name="p1_maleUrinary1wNo" onclick="onCheck(this,'p1_maleUrinary1w')"
						<%= props.getProperty("p1_maleUrinary1wNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_maleUrinary1wNotDiscussed"
						name="p1_maleUrinary1wNotDiscussed" onclick="onCheck(this,'p1_maleUrinary1w')"
						<%= props.getProperty("p1_maleUrinary1wNotDiscussed", "") %>></td>
                                        <td valign="top"><bean:message
						key="oscarEncounter.formRourke1.formMaleUrinaryStream" /></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_maleUrinary1wOk,p1_maleUrinary1wNo,p1_maleUrinary1wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
				
			</table>
			<textarea id="p1_pPhysical1w"
				name="p1_pPhysical1w" style="width: 100%" cols="10" rows="5"><%= props.getProperty("p1_pPhysical1w", "") %></textarea>
			</td>
			<td colspan="3" valign="bottom">
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
					<td valign="top"><input type="radio" id="p1_skin2wOk"
						name="p1_skin2wOk" onclick="onCheck(this,'p1_skin2w')"
						<%= props.getProperty("p1_skin2wOk", "") %>></td>
                                        <td valign="top"><input type="radio" id="p1_skin2wNo"
						name="p1_skin2wNo" onclick="onCheck(this,'p1_skin2w')"
						<%= props.getProperty("p1_skin2wNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_skin2wNotDiscussed"
						name="p1_skin2wNotDiscussed" onclick="onCheck(this,'p1_skin2w')"
						<%= props.getProperty("p1_skin2wNotDiscussed", "") %>></td>
                                        <td valign="top"><bean:message
						key="oscarEncounter.formRourke1.formDrySkin" /></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_skin2wOk,p1_skin2wNo,p1_skin2wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_fontanelles2wOk"
						name="p1_fontanelles2wOk" onclick="onCheck(this,'p1_fontanelles2w')"
						<%= props.getProperty("p1_fontanelles2wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_fontanelles2wNo"
						name="p1_fontanelles2wNo" onclick="onCheck(this,'p1_fontanelles2w')"
						<%= props.getProperty("p1_fontanelles2wNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_fontanelles2wNotDiscussed"
						name="p1_fontanelles2wNotDiscussed" onclick="onCheck(this,'p1_fontanelles2w')"
						<%= props.getProperty("p1_fontanelles2wNotDiscussed", "") %>></td>
                                        <td valign="top"><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_fontanelles2wOk,p1_fontanelles2wNo,p1_fontanelles2wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_eyes2wOk"
						name="p1_eyes2wOk" onclick="onCheck(this,'p1_eyes2w')"
						<%= props.getProperty("p1_eyes2wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_eyes2wNo"
						name="p1_eyes2wNo" onclick="onCheck(this,'p1_eyes2w')"
						<%= props.getProperty("p1_eyes2wNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_eyes2wNotDiscussed"
						name="p1_eyes2wNotDiscussed" onclick="onCheck(this,'p1_eyes2w')"
						<%= props.getProperty("p1_eyes2wNotDiscussed", "") %>></td>
                                        <td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.formRedReflex" />*</a></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_eyes2wOk,p1_eyes2wNo,p1_eyes2wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_ears2wOk"
						name="p1_ears2wOk" onclick="onCheck(this,'p1_ears2w')"
						<%= props.getProperty("p1_ears2wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_ears2wNo"
						name="p1_ears2wNo" onclick="onCheck(this,'p1_ears2w')"
                                                <%= props.getProperty("p1_ears2wNo", "") %>></td>
                   	<td valign="top"><input type="radio" id="p1_ears2wNotDiscussed"
						name="p1_ears2wNotDiscussed" onclick="onCheck(this,'p1_ears2w')"
                                                <%= props.getProperty("p1_ears2wNotDiscussed", "") %>></td>
                                        <td valign="top"><a href="javascript:showNotes()"
                                                onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><i><bean:message
						key="oscarEncounter.formRourke2006_1.formEarDrums" />*</i></a></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_ears2wOk,p1_ears2wNo,p1_ears2wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_heartLungs2wOk"
						name="p1_heartLungs2wOk" onclick="onCheck(this,'p1_heartLungs2w')"
						<%= props.getProperty("p1_heartLungs2wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_heartLungs2wNo"
						name="p1_heartLungs2wNo" onclick="onCheck(this,'p1_heartLungs2w')"
						<%= props.getProperty("p1_heartLungs2wNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_heartLungs2wNotDiscussed"
						name="p1_heartLungs2wNotDiscussed" onclick="onCheck(this,'p1_heartLungs2w')"
						<%= props.getProperty("p1_heartLungs2wNotDiscussed", "") %>></td>
                                        <td valign="top"><bean:message
						key="oscarEncounter.formRourke1.formHeart" /></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_heartLungs2wOk,p1_heartLungs2wNo,p1_heartLungs2wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_umbilicus2wOk"
						name="p1_umbilicus2wOk" onclick="onCheck(this,'p1_umbilicus2w')"
						<%= props.getProperty("p1_umbilicus2wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_umbilicus2wNo"
						name="p1_umbilicus2wNo" onclick="onCheck(this,'p1_umbilicus2w')"
                                                <%= props.getProperty("p1_umbilicus2wNo", "") %>></td>
                    <td valign="top"><input type="radio" id="p1_umbilicus2wNotDiscussed"
						name="p1_umbilicus2wNotDiscussed" onclick="onCheck(this,'p1_umbilicus2w')"
                                                <%= props.getProperty("p1_umbilicus2wNotDiscussed", "") %>></td>
                                        <td valign="top"><bean:message
						key="oscarEncounter.formRourke1.formUmbilicus" /></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_umbilicus2wOk,p1_umbilicus2wNo,p1_umbilicus2wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_femoralPulses2wOk"
						name="p1_femoralPulses2wOk" onclick="onCheck(this,'p1_femoralPulses2w')"
						<%= props.getProperty("p1_femoralPulses2wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_femoralPulses2wNo"
						name="p1_femoralPulses2wNo" onclick="onCheck(this,'p1_femoralPulses2w')"
                                                <%= props.getProperty("p1_femoralPulses2wNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_femoralPulses2wNotDiscussed"
						name="p1_femoralPulses2wNotDiscussed" onclick="onCheck(this,'p1_femoralPulses2w')"
                                                <%= props.getProperty("p1_femoralPulses2wNotDiscussed", "") %>></td>                                                
                                        <td valign="top"><bean:message
						key="oscarEncounter.formRourke1.formFemoralPulses" /></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_femoralPulses2wOk,p1_femoralPulses2wNo,p1_femoralPulses2wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_hips2wOk"
						name="p1_hips2wOk" onclick="onCheck(this,'p1_hips2w')"
						<%= props.getProperty("p1_hips2wOk", "") %>></td>
                                        <td valign="top"><input type="radio" id="p1_hips2wNo"
						name="p1_hips2wNo" onclick="onCheck(this,'p1_hips2w')"
						<%= props.getProperty("p1_hips2wNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_hips2wNotDiscussed"
						name="p1_hips2wNotDiscussed" onclick="onCheck(this,'p1_hips2w')"
						<%= props.getProperty("p1_hips2wNotDiscussed", "") %>></td>
                                        <td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.formHips" />*</a></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_hips2wOk,p1_hips2wNo,p1_hips2wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_muscleTone2wOk"
						name="p1_muscleTone2wOk" onclick="onCheck(this,'p1_muscleTone2w')"
						<%= props.getProperty("p1_muscleTone2wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_muscleTone2wNo"
						name="p1_muscleTone2wNo" onclick="onCheck(this,'p1_muscleTone2w')"
                                                <%= props.getProperty("p1_muscleTone2wNo", "") %>></td>
                   	<td valign="top"><input type="radio" id="p1_muscleTone2wNotDiscussed"
						name="p1_muscleTone2wNotDiscussed" onclick="onCheck(this,'p1_muscleTone2w')"
                                                <%= props.getProperty("p1_muscleTone2wNotDiscussed", "") %>></td>
                                        <td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formMuscleTone" />*</a></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_muscleTone2wOk,p1_muscleTone2wNo,p1_muscleTone2wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_testicles2wOk"
						name="p1_testicles2wOk" onclick="onCheck(this,'p1_testicles2w')"
						<%= props.getProperty("p1_testicles2wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_testicles2wNo"
						name="p1_testicles2wNo" onclick="onCheck(this,'p1_testicles2w')"
						<%= props.getProperty("p1_testicles2wNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_testicles2wNotDiscussed"
						name="p1_testicles2wNotDiscussed" onclick="onCheck(this,'p1_testicles2w')"
						<%= props.getProperty("p1_testicles2wNotDiscussed", "") %>></td>
                                        <td valign="top"><bean:message
						key="oscarEncounter.formRourke1.formTescicles" /></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_testicles2wOk,p1_testicles2wNo,p1_testicles2wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_maleUrinary2wOk"
						name="p1_maleUrinary2wOk" onclick="onCheck(this,'p1_maleUrinary2w')"
						<%= props.getProperty("p1_maleUrinary2wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_maleUrinary2wNo"
						name="p1_maleUrinary2wNo" onclick="onCheck(this,'p1_maleUrinary2w')"
						<%= props.getProperty("p1_maleUrinary2wNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_maleUrinary2wNotDiscussed"
						name="p1_maleUrinary2wNotDiscussed" onclick="onCheck(this,'p1_maleUrinary2w')"
						<%= props.getProperty("p1_maleUrinary2wNotDiscussed", "") %>></td>
                                        <td valign="top"><bean:message
						key="oscarEncounter.formRourke1.formMaleUrinaryStream" /></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_maleUrinary2wOk,p1_maleUrinary2wNo,p1_maleUrinary2wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                        </table>
             	<textarea id="p1_pPhysical2w"
				name="p1_pPhysical2w" style="width: 100%" cols="10" rows="5"><%= props.getProperty("p1_pPhysical2w", "") %></textarea>
			</td>
			</td>
			<td colspan="3" valign="bottom">
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
					<td valign="top"><input type="radio" id="p1_skin1mOk"
						name="p1_skin1mOk" onclick="onCheck(this,'p1_skin1m')"
						<%= props.getProperty("p1_skin1mOk", "") %>></td>
                                        <td valign="top"><input type="radio" id="p1_skin1mNo"
						name="p1_skin1mNo" onclick="onCheck(this,'p1_skin1m')"
						<%= props.getProperty("p1_skin1mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_skin1mNotDiscussed"
						name="p1_skin1mNotDiscussed" onclick="onCheck(this,'p1_skin1m')"
						<%= props.getProperty("p1_skin1mNotDiscussed", "") %>></td>
                                        <td valign="top"><bean:message
						key="oscarEncounter.formRourke2009_1.formSkin" /></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_skin1mOk,p1_skin1mNo,p1_skin1mNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_fontanelles1mOk"
						name="p1_fontanelles1mOk" onclick="onCheck(this,'p1_fontanelles1m')"
						<%= props.getProperty("p1_fontanelles1mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_fontanelles1mNo"
						name="p1_fontanelles1mNo" onclick="onCheck(this,'p1_fontanelles1m')"
						<%= props.getProperty("p1_fontanelles1mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_fontanelles1mNotDiscussed"
						name="p1_fontanelles1mNotDiscussed" onclick="onCheck(this,'p1_fontanelles1m')"
						<%= props.getProperty("p1_fontanelles1mNotDiscussed", "") %>></td>
                                        <td valign="top"><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_fontanelles1mOk,p1_fontanelles1mNo,p1_fontanelles1mNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_eyes1mOk"
						name="p1_eyes1mOk" onclick="onCheck(this,'p1_eyes1m')"
						<%= props.getProperty("p1_eyes1mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_eyes1mNo"
						name="p1_eyes1mNo" onclick="onCheck(this,'p1_eyes1m')"
						<%= props.getProperty("p1_eyes1mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_eyes1mNotDiscussed"
						name="p1_eyes1mNotDiscussed" onclick="onCheck(this,'p1_eyes1m')"
						<%= props.getProperty("p1_eyes1mNotDiscussed", "") %>></td>
                                        <td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.formRedReflex" />*</a></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_eyes1mOk,p1_eyes1mNo,p1_eyes1mNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_corneal1mOk"
						name="p1_corneal1mOk" onclick="onCheck(this,'p1_corneal1m')"
						<%= props.getProperty("p1_corneal1mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_corneal1mNo"
						name="p1_corneal1mNo" onclick="onCheck(this,'p1_corneal1m')"
						<%= props.getProperty("p1_corneal1mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_corneal1mNotDiscussed"
						name="p1_corneal1mNotDiscussed" onclick="onCheck(this,'p1_corneal1m')"
						<%= props.getProperty("p1_corneal1mNotDiscussed", "") %>></td>
                                        <td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formCornealReflex" />*</a></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_corneal1mOk,p1_corneal1mNo,p1_corneal1mNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_hearing1mOk"
						name="p1_hearing1mOk" onclick="onCheck(this,'p1_hearing1m')"
						<%= props.getProperty("p1_hearing1mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_hearing1mNo"
						name="p1_hearing1mNo" onclick="onCheck(this,'p1_hearing1m')"
						<%= props.getProperty("p1_hearing1mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_hearing1mNotDiscussed"
						name="p1_hearing1mNotDiscussed" onclick="onCheck(this,'p1_hearing1m')"
						<%= props.getProperty("p1_hearing1mNotDiscussed", "") %>></td>
                                        <td valign="top"><a href="javascript:showNotes()"
                                                            onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><i><bean:message
						key="oscarEncounter.formRourke2006_1.formHearingInquiry" />*</i></a></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_hearing1mOk,p1_hearing1mNo,p1_hearing1mNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_heart1mOk"
						name="p1_heart1mOk" onclick="onCheck(this,'p1_heart1m')"
						<%= props.getProperty("p1_heart1mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_heart1mNo"
						name="p1_heart1mNo" onclick="onCheck(this,'p1_heart1m')"
						<%= props.getProperty("p1_heart1mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_heart1mNotDiscussed"
						name="p1_heart1mNotDiscussed" onclick="onCheck(this,'p1_heart1m')"
						<%= props.getProperty("p1_heart1mNotDiscussed", "") %>></td>
                                        <td valign="top"><bean:message
						key="oscarEncounter.formRourke1.formHeart1" /></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_heart1mOk,p1_heart1mNo,p1_heart1mNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>				
				<tr>
					<td valign="top"><input type="radio" id="p1_hips1mOk"
						name="p1_hips1mOk" onclick="onCheck(this,'p1_hips1m')"
						<%= props.getProperty("p1_hips1mOk", "") %>></td>
                                        <td valign="top"><input type="radio" id="p1_hips1mNo"
						name="p1_hips1mNo" onclick="onCheck(this,'p1_hips1m')"
						<%= props.getProperty("p1_hips1mNo", "") %>></td>
					<td valign="top"><input type="radio" id="p1_hips1mNotDiscussed"
						name="p1_hips1mNotDiscussed" onclick="onCheck(this,'p1_hips1m')"
						<%= props.getProperty("p1_hips1mNotDiscussed", "") %>></td>
                                        <td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke1.formHips" />*</a></td>
				</tr>
                                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_hips1mOk,p1_hips1mNo,p1_hips1mNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
                                <tr>
					<td valign="top"><input type="radio" id="p1_muscleTone1mOk"
						name="p1_muscleTone1mOk" onclick="onCheck(this,'p1_muscleTone1m')"
						<%= props.getProperty("p1_muscleTone1mOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_muscleTone1mNo"
						name="p1_muscleTone1mNo" onclick="onCheck(this,'p1_muscleTone1m')"
                                                <%= props.getProperty("p1_muscleTone1mNo", "") %>></td>
                    <td valign="top"><input type="radio" id="p1_muscleTone1mNotDiscussed"
						name="p1_muscleTone1mNotDiscussed" onclick="onCheck(this,'p1_muscleTone1m')"
                                                <%= props.getProperty("p1_muscleTone1mNotDiscussed", "") %>></td>
                                        <td valign="top"><a href="javascript:showNotes()"
						onMouseOver="popLayer('<bean:message key="oscarEncounter.formRourke2006.footnote1" />')"
						onMouseOut="hideLayer()"><bean:message
						key="oscarEncounter.formRourke2006_1.formMuscleTone" />*</a></td>
				</tr>
                <tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_muscleTone1mOk,p1_muscleTone1mNo,p1_muscleTone1mNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
				</tr>
			</table>
			<textarea id="p1_pPhysical1m"
				name="p1_pPhysical1m" style="width: 100%" cols="10" rows="5"><%= props.getProperty("p1_pPhysical1m", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgProblemsAndPlans" /></a>
				<br>
				<img height="15" width="20" src="graphics/Checkmark_Lwhite.gif">
                <bean:message key="oscarEncounter.formRourke2009.msgProblemsLegend" />
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="4"><textarea id="p1_problems1w"
						name="p1_problems1w" rows="5" cols="25" class="wide"><%= props.getProperty("p1_problems1w", "") %></textarea></td>
				</tr>
				 <tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>
                    <td class="edcol" valign="top" colspan="2"><bean:message key="oscarEncounter.formRourke2009.formNotDone"/></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_pkuThyroid1wOk" name="p1_pkuThyroid1wOk"
						onclick="onCheck(this,'p1_pkuThyroid1w')"
						<%= props.getProperty("p1_pkuThyroid1wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_pkuThyroid1wOkConcerns" name="p1_pkuThyroid1wOkConcerns"
						onclick="onCheck(this,'p1_pkuThyroid1w')"
						<%= props.getProperty("p1_pkuThyroid1wOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_pkuThyroid1wNotDiscussed" name="p1_pkuThyroid1wNotDiscussed"
						onclick="onCheck(this,'p1_pkuThyroid1w')"
						<%= props.getProperty("p1_pkuThyroid1wNotDiscussed", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke1.formThyroid" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="radio" id="p1_hemoScreen1wOk"
						name="p1_hemoScreen1wOk" onclick="onCheck(this,'p1_hemoScreen1w')"
						<%= props.getProperty("p1_hemoScreen1wOk", "") %>></td>
					<td valign="top"><input type="radio" id="p1_hemoScreen1wOkConcerns"
						name="p1_hemoScreen1wOkConcerns" onclick="onCheck(this,'p1_hemoScreen1w')"
						<%= props.getProperty("p1_hemoScreen1wOkConcerns", "") %>></td>
					<td valign="top"><input type="radio" id="p1_hemoScreen1wNotDiscussed"
						name="p1_hemoScreen1wNotDiscussed" onclick="onCheck(this,'p1_hemoScreen1w')"
						<%= props.getProperty("p1_hemoScreen1wNotDiscussed", "") %>></td>
					<td><b><a href="javascript:showNotes()"><bean:message
						key="oscarEncounter.formRourke1.formHemoglobinopathy" /></a> (if at
					risk)*</b></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_pkuThyroid1wOk,p1_pkuThyroid1wOkConcerns,p1_pkuThyroid1wNotDiscussed,p1_hemoScreen1wOk,p1_hemoScreen1wOkConcerns,p1_hemoScreen1wNotDiscussed');" /></td>
                                        <td>&nbsp;</td>
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
			<br>
				<img height="15" width="20" src="graphics/Checkmark_Lwhite.gif">
                <bean:message key="oscarEncounter.formRourke2009.msgProblemsLegend" />
			</td>
			<td colspan="3" valign="top">
				<textarea id="p1_immunization1w"
						name="p1_immunization1w" rows="5" cols="25" class="wide"><%= props.getProperty("p1_immunization1w", "") %></textarea>
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td style="text-align: center" colspan="3"><b><bean:message
						key="oscarEncounter.formRourke2006_1.msgImmunizationColTitle" /></b><br />
					<bean:message
						key="oscarEncounter.formRourke2006_1.msgImmunizationHepatitis" />
					</td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>	
					<td>&nbsp;</td>				
				</tr>
				<tr>
					<td><input type="radio" id="p1_hepatitisVaccine1wOk"
						name="p1_hepatitisVaccine1wOk" onclick="onCheck(this,'p1_hepatitisVaccine1w')"
						<%= props.getProperty("p1_hepatitisVaccine1wOk", "") %>></td>
					<td><input type="radio" id="p1_hepatitisVaccine1wNo"
						name="p1_hepatitisVaccine1wNo" onclick="onCheck(this,'p1_hepatitisVaccine1w')"
						<%= props.getProperty("p1_hepatitisVaccine1wNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_1.msgImmunizationHepatitisVaccine" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_hepatitisVaccine1wOk,p1_hepatitisVaccine1wNo');" /></td>                                        
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<textarea id="p1_immunization2w"
						name="p1_immunization2w" rows="5" cols="25" class="wide"><%= props.getProperty("p1_immunization2w", "") %></textarea>
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td style="text-align: center"><b><bean:message
						key="oscarEncounter.formRourke2006_1.msgImmunizationColTitle" /></b></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<textarea id="p1_immunization1m"
						name="p1_immunization1m" rows="5" cols="25" class="wide"><%= props.getProperty("p1_immunization1m", "") %></textarea>
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td style="text-align: center" colspan="3"><b><bean:message
						key="oscarEncounter.formRourke2006_1.msgImmunizationColTitle" /></b><br />
					<bean:message
						key="oscarEncounter.formRourke2006_1.msgImmunizationHepatitis" />
					</td>
				</tr>
				<tr>
					<td style="padding-right: 5pt" valign="top"><img height="15"
						width="20" src="graphics/Checkmark_L.gif"></td>
					<td class="edcol" valign="top">X</td>	
					<td>&nbsp;</td>				
				</tr>
				<tr>
					<td><input type="radio" id="p1_hepatitisVaccine1mOk"
						name="p1_hepatitisVaccine1mOk" onclick="onCheck(this,'p1_hepatitisVaccine1m')"
						<%= props.getProperty("p1_hepatitisVaccine1mOk", "") %>></td>
					<td><input type="radio" id="p1_hepatitisVaccine1mNo"
						name="p1_hepatitisVaccine1mNo" onclick="onCheck(this,'p1_hepatitisVaccine1mNo')"
						<%= props.getProperty("p1_hepatitisVaccine1mNo", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2006_1.msgImmunizationHepatitisVaccine" /></td>
				</tr>
				<tr>
					<td valign="top" class="edcol" colspan="3"><input
						class="delete" type="button" value="Del"
						onclick="del('p1_hepatitisVaccine1mOk,p1_hepatitisVaccine1mNo');" /></td>                                        
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
				onclick="javascript:return popPage('http://rourkebabyrecord.ca','About Rourke');" />
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
				href="formrourke2009p2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
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
    Calendar.setup({ inputField : "p1_date1w", ifFormat : "%d/%m/%Y", showsTime :false, button : "p1_date1w_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "p1_date2w", ifFormat : "%d/%m/%Y", showsTime :false, button : "p1_date2w_cal", singleClick : true, step : 1 });
    Calendar.setup({ inputField : "p1_date1m", ifFormat : "%d/%m/%Y", showsTime :false, button : "p1_date1m_cal", singleClick : true, step : 1 });
</script>
</html:html>
