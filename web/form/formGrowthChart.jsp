
<%@ page language="java"%>
<%@ page import="oscar.form.*, oscar.form.data.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<%
	String formClass = "GrowthChart";
	String formLink = "formGrowthChart.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(demoNo, formId);

    FrmData fd = new FrmData();

	//get project_home
	String project_home = request.getContextPath().substring(1);

	// boy/girl color
	String boyColor = "#99CCFF";
	String girlColor = "#FFccFF";
	boolean bGirl = props.getProperty("patientSex", "").equals("F")?true:false;
%>
<!--
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
-->
<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>

<head>
    <title>CDC US Growth Charts</title>
    <link rel="stylesheet" type="text/css" href="bcArStyle.css" >
  <!-- calendar stylesheet -->
  <link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />

  <!-- main calendar program -->
  <script type="text/javascript" src="../share/calendar/calendar.js"></script>

  <!-- language for the calendar -->
  <script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>

  <!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
  <script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
    <html:base/>
</head>

<script type="text/javascript" language="Javascript">
    function reset() {
        document.forms[0].target = "";
        document.forms[0].action = "/<%=project_home%>/form/formname.do" ;
	}
    function onPrintStatureWeight() {
        //document.forms[0].submit.value="print";
        var ret = checkAllDates();
        if(ret==true)
        {
            document.forms[0].action = "../form/createpdf?__title=GrowthCharts&__cfgfile=<%=bGirl?"growthChartGirlPrint":"growthChartBoyPrint"%>&__cfgGraphicFile=<%=bGirl?"growthChartGirlGraphic":"growthChartBoyGraphic"%>&__cfgGraphicFile=<%=bGirl?"growthChartGirlGraphic2":"growthChartBoyGraphic2"%>&__template=<%=bGirl?"growthChartGirlStatureWeight":"growthChartBoyStatureWeight"%>";
            document.forms[0].target="_blank";
        }
        return ret;
    }
    function onPrintBMI() {
        //document.forms[0].submit.value="print";
        var ret = checkAllDates();
        if(ret==true)
        {
            document.forms[0].action = "../form/createpdf?__title=GrowthCharts&__cfgfile=<%=bGirl?"growthChartGirlBMIPrint":"growthChartBoyBMIPrint"%>&__cfgGraphicFile=<%=bGirl?"growthChartGirlGraphicBMI":"growthChartBoyGraphicBMI"%>&__template=<%=bGirl?"growthChartGirlBMI":"growthChartBoyBMI"%>";
            document.forms[0].target="_blank";
        }
        return ret;
    }

function setfocus() {
    this.focus();
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
        if (document.forms[0].elements[i].name.indexOf(s) != -1 && document.forms[0].elements[i].name.indexOf(s) < 1) {
            document.forms[0].elements[i].checked = false;
    	}
	}
}
function isChecked(s) {
    for (var i =0; i <document.forms[0].elements.length; i++) {
        if (document.forms[0].elements[i].name == s) {
            if (document.forms[0].elements[i].checked) {
				return true;
			} else {
				return false;
			}
    	}
	}
}

    function onSave() {
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
        if(ret==true) {
            reset();
            ret = confirm("Are you sure you want to save this form?");
        }
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
        //document.forms[0].submit.value="exit";
        var ret = checkAllDates();
        if(ret == true) {
            reset();
            ret = confirm("Are you sure you wish to save and close this window?");
        }
        return ret;
    }
    function popupPage(varpage) {
        windowprops = "height=700,width=960"+
            ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=50,screenY=50,top=20,left=20";
        var popup = window.open(varpage, "ar2", windowprops);
        if (popup.opener == null) {
            popup.opener = self;
        }
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
    function popupFixedPage(vheight,vwidth,varpage) {
       var page = "" + varpage;
       windowprop = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=10,screenY=0,top=0,left=0";
       var popup=window.open(page, "planner", windowprop);
    }


    function isInteger(s){
        var i;
        for (i = 0; i < s.length; i++){
            // Check that current character is number.
            var c = s.charAt(i);
            if (((c < "0") || (c > "9"))) return false;
        }
        // All characters are numbers.
        return true;
    }
    function checkTypeIn(obj) {
      if(!checkTypeNum(obj.value) ) {
          alert ("You must type in a number in the field.");
        }
    }
    function checkAllDates(){
        var b = true;

        for (var i =0; i <document.forms[0].elements.length; i++) {
            if (document.forms[0].elements[i].name.indexOf("age_")>=0 || document.forms[0].elements[i].name.indexOf("stature_")>=0 || document.forms[0].elements[i].name.indexOf("bmi_")>=0) {
               if(!isNumber(document.forms[0].elements[i]))
                 b=false;
    	    }
	    }
        return b;
    }
function wtEnglish2Metric(source) {
	if(isNumber(source) ) {
		weight = source.value;
		weightM = Math.round(weight * 10 * 0.4536) / 10 ;
		if(confirm("Are you sure you want to change " + weight + " pounds to " + weightM +"kg?") ) {
			source.value = weightM;
		}
	}
}
function htEnglish2Metric(source) {
	height = source.value;
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
				source.value = height;
			}
		//}
	} else if(height.length > 1 && height.indexOf("\"")>0 ) {
		inch = height.substring(0, height.indexOf("\"")) ;
		height = Math.round((0 * 30.48 + inch * 2.54) * 10) / 10 ;
		if(confirm("Are you sure you want to change " + inch + " inch(es) to " + height +"cm?") ) {
			source.value = height;
		}
	}
}
function calcBMIMetric(source) {
	//if(isNumber(document.forms[0].c_ppWt) && isNumber(document.forms[0].c_ppHt)) {
		weight = getDataField(source.name, "weight_");
		height = getDataField(source.name, "stature_") / 100;
		if(weight!="" && weight!="0" && height!="" && height!="0") {
			source.value = Math.round(weight * 10 / height / height) / 10;
		}
	//}
}
	function calcAge(source) {
	    var delta = 0;
        var str_date = getDataField(source.name, "date_");
        if (str_date.length < 10) return;
        var yyyy = str_date.substring(0, str_date.indexOf("/"));
        var mm = eval(str_date.substring(eval(str_date.indexOf("/")+1), str_date.lastIndexOf("/")) - 1);
        var dd = str_date.substring(eval(str_date.lastIndexOf("/")+1));
        //var dd = str_date.substring(0, str_date.indexOf("/"));
        //var mm = eval(str_date.substring(eval(str_date.indexOf("/")+1), str_date.lastIndexOf("/")) - 1);
        //var yyyy  = str_date.substring(eval(str_date.lastIndexOf("/")+1));
        var check_date=new Date(yyyy,mm,dd);
        str_date = "<%=props.getProperty("dateOfBirth")%>";
        var yyyy = str_date.substring(0, str_date.indexOf("/"));
        var mm = eval(str_date.substring(eval(str_date.indexOf("/")+1), str_date.lastIndexOf("/")) - 1);
        var dd = str_date.substring(eval(str_date.lastIndexOf("/")+1));
        var start=new Date(yyyy,mm,dd);

		if (check_date.getUTCHours() != start.getUTCHours()) {
			if (check_date.getUTCHours() > start.getUTCHours()) {
			    delta = -1 * 60 * 60 * 1000;
			} else {
			    delta = 1 * 60 * 60 * 1000;
			}
		}

		var day = eval((check_date.getTime() - start.getTime() + delta) / (24*60*60*1000));
		//alert(day);
        var year = Math.floor(day/365);
		var yearday = (Math.floor((day%365)/36.5));
        source.value = year + "." + yearday;
        //var week = Math.floor(day/7);
		//var weekday = day%7;
        //source.value = week + "w+" + weekday;
	}
	function getDataField(name, prefix) {
		var temp = ""; //age_1; date_1 - date_8
		var n1 = name.substring(eval(name.indexOf("_")+1));
		name = prefix + n1;
		//alert(name);

        for (var i =0; i <document.forms[0].elements.length; i++) {
            if (document.forms[0].elements[i].name == name) {
               return document.forms[0].elements[i].value;
    	    }
	    }
        return temp;
    }
	function isNumber(ss){
		var s = ss.value;
        var i;
        for (i = 0; i < s.length; i++){
            // Check that current character is number.
            var c = s.charAt(i);
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
</script>

<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1"  onLoad="setfocus()">
<!--
@oscar.formDB Table="formGrowthChart"
@oscar.formDB Field="ID" Type="int(10)" Null="NOT NULL" Key="PRI" Default="" Extra="auto_increment"
@oscar.formDB Field="demographic_no" Type="int(10)" Null="NOT NULL" Default="'0'"
@oscar.formDB Field="provider_no" Type="int(10)" Null="" Default="NULL"
@oscar.formDB Field="formCreated" Type="date" Null="" Default="NULL"
@oscar.formDB Field="formEdited" Type="timestamp"
-->
<html:form action="/form/formname">
<input type="hidden" name="demographic_no" value="<%= props.getProperty("demographic_no", "0") %>" />
<input type="hidden" name="formCreated" value="<%= props.getProperty("formCreated", "") %>" />
<input type="hidden" name="form_class" value="<%=formClass%>" />
<input type="hidden" name="form_link" value="<%=formLink%>" />
<input type="hidden" name="formId" value="<%=formId%>" />
<input type="hidden" name="provider_no" value=<%=request.getParameter("provNo")%> />
<input type="hidden" name="submit" value="exit"/>

<table class="Head" class="hidePrint">
    <tr>
        <td align="left">
            <input type="submit" value="Save" onclick="javascript:return onSave();" />
            <input type="submit" value="Save and Exit" onclick="javascript:return onSaveExit();"/>
            <input type="submit" value="Exit" onclick="javascript:return onExit();"/>
            <input type="submit" value="Print Growth" onclick="javascript:return onPrintStatureWeight();return false;"/>
            <input type="submit" value="Print BMI" onclick="javascript:return onPrintBMI();return false;"/>
        </td>
    </tr>
</table>

<center>
<table width="80%" border="0"  cellspacing="0" cellpadding="0" >
  <tr><td>


  <table width="100%" border="0"  cellspacing="0" cellpadding="0" bgcolor="<%= bGirl? girlColor:boyColor%>">
    <tr>
    <th align="left"><%= bGirl? "GIRLS:" : "BOYS:"%> 2 TO 20 YEARS<br>
    CDC US GROWTH CHARTS</th>
    </tr>
  </table>

  <table width="100%" border="0"  cellspacing="0" cellpadding="0">
    <tr>
    <td align="right">Name
      <input type="text" name="patientName" size="50" maxlength="80" value="<%= props.getProperty("patientName", "") %>" @oscar.formDB />
      Record #
      <input type="text" name="recordNo" size="10" maxlength="10" value="<%= props.getProperty("recordNo", "") %>" @oscar.formDB />
      </td>
    </tr>
  </table>

  <table border="0"  cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap>Mother's Stature</td><td>
      <input type="text" name="motherStature" size="50" maxlength="80" value="<%= props.getProperty("motherStature", "") %>" @oscar.formDB />
	</td>
  </tr><tr>
    <td nowrap>Father's Stature</td><td>
      <input type="text" name="fatherStature" size="50" maxlength="80" value="<%= props.getProperty("fatherStature", "") %>" @oscar.formDB />
	</td>
    </tr>
  </table>

	<table width="70%" border="1"  cellspacing="2" cellpadding="0"  bgcolor="<%= bGirl? girlColor:boyColor%>">
    <tr>
	  <th width="10%">Date</th>
	  <th width="10%">Age</th>
	  <th width="20%">Stature(cm)</th>
	  <th width="10%">Weight(kg)</th>
	  <th width="40%">Comment</th>
	  <th >BMI</th>
	</tr>
	  <td nowrap>
      <input type="text" name="date_1"  readonly size="10" maxlength="10" value="<%= props.getProperty("date_1", "") %>" @oscar.formDB  dbType="date"/>
		<img src="../images/cal.gif" id="date_1_cal">
	  </td>
	  <td>
      <input type="text" name="age_1"  onDblClick="calcAge(this)" size="5" maxlength="5" value="<%= props.getProperty("age_1", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="stature_1" onDblClick="htEnglish2Metric(this)" size="6" maxlength="6" value="<%= props.getProperty("stature_1", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="weight_1" onDblClick="wtEnglish2Metric(this)" size="5" maxlength="6" value="<%= props.getProperty("weight_1", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="comment_1" size="25" maxlength="25" value="<%= props.getProperty("comment_1", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="bmi_1" onDblClick="calcBMIMetric(this)" size="4" maxlength="5" value="<%= props.getProperty("bmi_1", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
	  <td nowrap>
      <input type="text" name="date_2"  readonly size="10" maxlength="10" value="<%= props.getProperty("date_2", "") %>" @oscar.formDB  dbType="date"/>
		<img src="../images/cal.gif" id="date_2_cal">
	  </td>
	  <td>
      <input type="text" name="age_2"  onDblClick="calcAge(this)" size="5" maxlength="5" value="<%= props.getProperty("age_2", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="stature_2" onDblClick="htEnglish2Metric(this)" size="6" maxlength="6" value="<%= props.getProperty("stature_2", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="weight_2" onDblClick="wtEnglish2Metric(this)" size="5" maxlength="6" value="<%= props.getProperty("weight_2", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="comment_2" size="25" maxlength="25" value="<%= props.getProperty("comment_2", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="bmi_2" onDblClick="calcBMIMetric(this)" size="4" maxlength="5" value="<%= props.getProperty("bmi_2", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
	  <td nowrap>
      <input type="text" name="date_3"  readonly size="10" maxlength="10" value="<%= props.getProperty("date_3", "") %>" @oscar.formDB  dbType="date"/>
		<img src="../images/cal.gif" id="date_3_cal">
	  </td>
	  <td>
      <input type="text" name="age_3"  onDblClick="calcAge(this)" size="5" maxlength="5" value="<%= props.getProperty("age_3", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="stature_3" onDblClick="htEnglish2Metric(this)" size="6" maxlength="6" value="<%= props.getProperty("stature_3", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="weight_3" onDblClick="wtEnglish2Metric(this)" size="5" maxlength="6" value="<%= props.getProperty("weight_3", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="comment_3" size="25" maxlength="25" value="<%= props.getProperty("comment_3", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="bmi_3" onDblClick="calcBMIMetric(this)" size="4" maxlength="5" value="<%= props.getProperty("bmi_3", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
	  <td>
      <input type="text" name="date_4"  readonly size="10" maxlength="10" value="<%= props.getProperty("date_4", "") %>" @oscar.formDB  dbType="date"/>
		<img src="../images/cal.gif" id="date_4_cal">
	  </td>
	  <td>
      <input type="text" name="age_4"  onDblClick="calcAge(this)" size="5" maxlength="5" value="<%= props.getProperty("age_4", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="stature_4" onDblClick="htEnglish2Metric(this)" size="6" maxlength="6" value="<%= props.getProperty("stature_4", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="weight_4" onDblClick="wtEnglish2Metric(this)" size="5" maxlength="6" value="<%= props.getProperty("weight_4", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="comment_4" size="25" maxlength="25" value="<%= props.getProperty("comment_4", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="bmi_4" onDblClick="calcBMIMetric(this)" size="4" maxlength="5" value="<%= props.getProperty("bmi_4", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
	  <td>
      <input type="text" name="date_5"  readonly size="10" maxlength="10" value="<%= props.getProperty("date_5", "") %>" @oscar.formDB  dbType="date"/>
		<img src="../images/cal.gif" id="date_5_cal">
	  </td>
	  <td>
      <input type="text" name="age_5"  onDblClick="calcAge(this)" size="5" maxlength="5" value="<%= props.getProperty("age_5", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="stature_5" onDblClick="htEnglish2Metric(this)" size="6" maxlength="6" value="<%= props.getProperty("stature_5", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="weight_5" onDblClick="wtEnglish2Metric(this)" size="5" maxlength="6" value="<%= props.getProperty("weight_5", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="comment_5" size="25" maxlength="25" value="<%= props.getProperty("comment_5", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="bmi_5" onDblClick="calcBMIMetric(this)" size="4" maxlength="5" value="<%= props.getProperty("bmi_5", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
	  <td>
      <input type="text" name="date_6"  readonly size="10" maxlength="10" value="<%= props.getProperty("date_6", "") %>" @oscar.formDB  dbType="date"/>
		<img src="../images/cal.gif" id="date_6_cal">
	  </td>
	  <td>
      <input type="text" name="age_6"  onDblClick="calcAge(this)" size="5" maxlength="5" value="<%= props.getProperty("age_6", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="stature_6" onDblClick="htEnglish2Metric(this)" size="6" maxlength="6" value="<%= props.getProperty("stature_6", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="weight_6" onDblClick="wtEnglish2Metric(this)" size="5" maxlength="6" value="<%= props.getProperty("weight_6", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="comment_6" size="25" maxlength="25" value="<%= props.getProperty("comment_6", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="bmi_6" onDblClick="calcBMIMetric(this)" size="4" maxlength="5" value="<%= props.getProperty("bmi_6", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
	  <td>
      <input type="text" name="date_7"  readonly size="10" maxlength="10" value="<%= props.getProperty("date_7", "") %>" @oscar.formDB  dbType="date"/>
		<img src="../images/cal.gif" id="date_7_cal">
	  </td>
	  <td>
      <input type="text" name="age_7"  onDblClick="calcAge(this)" size="5" maxlength="5" value="<%= props.getProperty("age_7", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="stature_7" onDblClick="htEnglish2Metric(this)" size="6" maxlength="6" value="<%= props.getProperty("stature_7", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="weight_7" onDblClick="wtEnglish2Metric(this)" size="5" maxlength="6" value="<%= props.getProperty("weight_7", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="comment_7" size="25" maxlength="25" value="<%= props.getProperty("comment_7", "") %>" @oscar.formDB />
	  </td>
	  <td>
      <input type="text" name="bmi_7" onDblClick="calcBMIMetric(this)" size="4" maxlength="5" value="<%= props.getProperty("bmi_7", "") %>" @oscar.formDB />
	  </td>
    </tr>
	</table>

</td></tr>
</table>
</center>

<table class="Head" class="hidePrint">
    <tr>
        <td align="left">
            <input type="submit" value="Save" onclick="javascript:return onSave();" />
            <input type="submit" value="Save and Exit" onclick="javascript:return onSaveExit();"/>
            <input type="submit" value="Exit" onclick="javascript:return onExit();"/>
            <input type="submit" value="Print Growth" onclick="javascript:return onPrintStatureWeight();return false;"/>
            <input type="submit" value="Print BMI" onclick="javascript:return onPrintBMI();return false;"/>
        </td>
    </tr>
</table>

</html:form>
<script type="text/javascript">
Calendar.setup({ inputField : "date_1", ifFormat : "%Y/%m/%d", showsTime :false, button : "date_1_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "date_2", ifFormat : "%Y/%m/%d", showsTime :false, button : "date_2_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "date_3", ifFormat : "%Y/%m/%d", showsTime :false, button : "date_3_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "date_4", ifFormat : "%Y/%m/%d", showsTime :false, button : "date_4_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "date_5", ifFormat : "%Y/%m/%d", showsTime :false, button : "date_5_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "date_6", ifFormat : "%Y/%m/%d", showsTime :false, button : "date_6_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "date_7", ifFormat : "%Y/%m/%d", showsTime :false, button : "date_7_cal", singleClick : true, step : 1 });
</script>
</body>
</html:html>
