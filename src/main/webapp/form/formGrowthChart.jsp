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

<%@ page import="oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%
	String formClass = "GrowthChart";
	String formLink = "formGrowthChart.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

    FrmData fd = new FrmData();

	//get project_home
	String project_home = request.getContextPath().substring(1);

	// boy/girl color
	String boyColor = "#99CCFF";
	String girlColor = "#FFccFF";
	boolean bGirl = props.getProperty("patientSex", "").equals("F")?true:false;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>WHO Growth Charts</title>
<link rel="stylesheet" type="text/css" href="bcArStyle.css">
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
<html:base />
</head>

<script type="text/javascript" language="Javascript">
    function reset() {
        document.forms[0].target = "";
        document.forms[0].action = "/<%=project_home%>/form/formname.do" ;
	}
    function onPrintStatureWeight(num) {
        //document.forms[0].submit.value="print";
        var ret = checkAllDates();
        if(ret==true) {
            document.forms[0].action = "../form/formGrowthChartPrint.jsp?print=" + num + "&__title=GrowthCharts&__cfgfile=<%=bGirl?"growthChartGirlPrint":"growthChartBoyPrint"%>&__cfgGraphicFile=<%=bGirl?"growthChartGirlGraphic":"growthChartBoyGraphic"%>&__cfgGraphicFile=<%=bGirl?"growthChartGirlGraphic2":"growthChartBoyGraphic2"%>&__template=<%=bGirl?"growthChartGirlStatureWeight":"growthChartBoyStatureWeight"%>";
            document.forms[0].target="_blank";
        }
        return ret;
    }
    function onPrintBMI(num) {
        //document.forms[0].submit.value="print";
        var ret = checkAllDates();
        if(ret==true) {
            document.forms[0].action = "../form/formGrowthChartPrint.jsp?print=" + num + "&__title=GrowthCharts&__cfgfile=<%=bGirl?"growthChartGirlBMIPrint":"growthChartBoyBMIPrint"%>&__cfgGraphicFile=<%=bGirl?"growthChartGirlGraphicBMI":"growthChartBoyGraphicBMI"%>&__template=<%=bGirl?"growthChartGirlBMI":"growthChartBoyBMI"%>";
            document.forms[0].target="_blank";
        }
        return ret;
    }
    function onPrintStatureWeight1() {
        var ret = checkAllDates();
        if(ret==true) {
            document.forms[0].action = "../form/formGrowthChartPrint.jsp?print=1&__title=GrowthCharts&__cfgfile=<%=bGirl?"growthChartGirlPrint":"growthChartBoyPrint"%>&__cfgGraphicFile=<%=bGirl?"growthChartGirlGraphic":"growthChartBoyGraphic"%>&__cfgGraphicFile=<%=bGirl?"growthChartGirlGraphic2":"growthChartBoyGraphic2"%>&__template=<%=bGirl?"growthChartGirlStatureWeight":"growthChartBoyStatureWeight"%>";
            document.forms[0].target="_blank";
        }
        return ret;
    }
    function onPrintStatureWeight2() {
        var ret = checkAllDates();
        if(ret==true) {
            document.forms[0].action = "../form/formGrowthChartPrint.jsp?print=2&__title=GrowthCharts&__cfgfile=<%=bGirl?"growthChartGirlPrint":"growthChartBoyPrint"%>&__cfgGraphicFile=<%=bGirl?"growthChartGirlGraphic":"growthChartBoyGraphic"%>&__cfgGraphicFile=<%=bGirl?"growthChartGirlGraphic2":"growthChartBoyGraphic2"%>&__template=<%=bGirl?"growthChartGirlStatureWeight":"growthChartBoyStatureWeight"%>";
            document.forms[0].target="_blank";
        }
        return ret;
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

<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1"
	onLoad="setfocus()">
<!--
@oscar.formDB Table="formGrowthChart"
@oscar.formDB Field="ID" Type="int(10)" Null="NOT NULL" Key="PRI" Default="" Extra="auto_increment"
@oscar.formDB Field="demographic_no" Type="int(10)" Null="NOT NULL" Default="'0'"
@oscar.formDB Field="provider_no" Type="int(10)" Null="" Default="NULL"
@oscar.formDB Field="formCreated" Type="date" Null="" Default="NULL"
@oscar.formDB Field="formEdited" Type="timestamp"
-->
<html:form action="/form/formname">
	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="formId" value="<%=formId%>" />
	<input type="hidden" name="provider_no"
		value=<%=request.getParameter("provNo")%> />
	<input type="hidden" name="submit" value="exit" />

	<table class="Head" class="hidePrint">
		<tr>
			<td align="left"><input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <input
				type="submit" value="Exit" onclick="javascript:return onExit();" />
			<!--input type="submit" value="Print Growth" onclick="javascript:return onPrintStatureWeight();return false;"/>
            <input type="submit" value="Print BMI" onclick="javascript:return onPrintBMI();return false;"/-->
			</td>
		</tr>
	</table>

	<center>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>


			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				bgcolor="<%= bGirl? girlColor:boyColor%>">
				<tr>
					<th align="left"><%= bGirl? "GIRLS:" : "BOYS:"%> 2 TO 20 YEARS<br>
					WHO GROWTH CHARTS</th>
				</tr>
			</table>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="right">Name <input type="text" name="patientName"
						size="50" maxlength="80"
						value="<%= props.getProperty("patientName", "") %>" @oscar.formDB />
					Record # <input type="text" name="recordNo" size="10"
						maxlength="10" value="<%= props.getProperty("recordNo", "") %>"
						@oscar.formDB /></td>
				</tr>
			</table>

			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td nowrap>Mother's Stature</td>
					<td><input type="text" name="motherStature" size="50"
						maxlength="80"
						value="<%= props.getProperty("motherStature", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td nowrap>Father's Stature</td>
					<td><input type="text" name="fatherStature" size="50"
						maxlength="80"
						value="<%= props.getProperty("fatherStature", "") %>"
						@oscar.formDB /></td>
				</tr>
			</table>

			</td>
		</tr>
	</table>



	<table width="100%" border="0" cellspacing="0" cellpadding="1">
		<tr>
			<td width="50%">

			<table width="100%" border="1" cellspacing="1" cellpadding="0"
				bgcolor="<%= bGirl? girlColor:boyColor%>">
				<tr>
					<th width="10%">Date</th>
					<th width="5%">Age</th>
					<th width="10%">Stature<br>
					(cm)</th>
					<th width="10%">Weight<br>
					(kg)</th>
					<th width="40%">Comment</th>
					<th>BMI</th>
				</tr>
				<%
	int n = 1;
	int nRowStart = 1;
	int nRowEnd = nRowStart + 6;
	for(int i=nRowStart; i<=nRowEnd; i++) { %>
				<tr align="center">
					<td nowrap><input type="text" name="date_<%=i%>"
						id="date_<%=i%>" readonly size="8" maxlength="10"
						value="<%= props.getProperty("date_"+i, "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="date_<%=i%>_cal">
					</td>
					<td><input type="text" name="age_<%=i%>"
						onDblClick="calcAge(this)" size="1" maxlength="5"
						value="<%= props.getProperty("age_"+i, "") %>" @oscar.formDB /></td>
					<td><input type="text" name="stature_<%=i%>"
						onDblClick="htEnglish2Metric(this)" size="3" maxlength="6"
						value="<%= props.getProperty("stature_"+i, "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="weight_<%=i%>"
						onDblClick="wtEnglish2Metric(this)" size="3" maxlength="6"
						value="<%= props.getProperty("weight_"+i, "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="comment_<%=i%>" size="20"
						maxlength="25" value="<%= props.getProperty("comment_"+i, "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="bmi_<%=i%>"
						onDblClick="calcBMIMetric(this)" size="1" maxlength="5"
						value="<%= props.getProperty("bmi_"+i, "") %>" @oscar.formDB /></td>
				</tr>
				<% } %>
				<tr class="Head">
					<td align="center" colspan="6"><input type="submit"
						value="Print Growth"
						onclick="javascript:return onPrintStatureWeight(1);return false;" />
					<input type="submit" value="Print BMI"
						onclick="javascript:return onPrintBMI(1);return false;" /></td>
				</tr>

			</table>

			</td>
			<td>


			<table width="100%" border="1" cellspacing="1" cellpadding="0"
				bgcolor="<%= bGirl? girlColor:boyColor%>">
				<tr>
					<th width="10%">Date</th>
					<th width="5%">Age</th>
					<th width="10%">Stature<br>
					(cm)</th>
					<th width="10%">Weight<br>
					(kg)</th>
					<th width="40%">Comment</th>
					<th>BMI</th>
				</tr>
				<%
	n = 2;
	nRowStart = (n-1)*7 + 1;
	nRowEnd = nRowStart + 6;
	for(int i=nRowStart; i<=nRowEnd; i++) { %>
				<tr align="center">
					<td nowrap><input type="text" name="date_<%=i%>"
						id="date_<%=i%>" readonly size="8" maxlength="10"
						value="<%= props.getProperty("date_"+i, "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="date_<%=i%>_cal">
					</td>
					<td><input type="text" name="age_<%=i%>"
						onDblClick="calcAge(this)" size="1" maxlength="5"
						value="<%= props.getProperty("age_"+i, "") %>" @oscar.formDB /></td>
					<td><input type="text" name="stature_<%=i%>"
						onDblClick="htEnglish2Metric(this)" size="3" maxlength="6"
						value="<%= props.getProperty("stature_"+i, "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="weight_<%=i%>"
						onDblClick="wtEnglish2Metric(this)" size="3" maxlength="6"
						value="<%= props.getProperty("weight_"+i, "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="comment_<%=i%>" size="20"
						maxlength="25" value="<%= props.getProperty("comment_"+i, "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="bmi_<%=i%>"
						onDblClick="calcBMIMetric(this)" size="1" maxlength="5"
						value="<%= props.getProperty("bmi_"+i, "") %>" @oscar.formDB /></td>
				</tr>
				<% } %>
				<tr class="Head">
					<td align="center" colspan="6"><input type="submit"
						value="Print Growth"
						onclick="javascript:return onPrintStatureWeight(2);return false;" />
					<input type="submit" value="Print BMI"
						onclick="javascript:return onPrintBMI(2);return false;" /></td>
				</tr>
			</table>


			</td>
		</tr>

		<tr>
			<td>

			<table width="100%" border="1" cellspacing="1" cellpadding="0"
				bgcolor="<%= bGirl? girlColor:boyColor%>">
				<tr>
					<th width="10%">Date</th>
					<th width="5%">Age</th>
					<th width="10%">Stature<br>
					(cm)</th>
					<th width="10%">Weight<br>
					(kg)</th>
					<th width="40%">Comment</th>
					<th>BMI</th>
				</tr>
				<%
	n = 3;
	nRowStart = (n-1)*7 + 1;
	nRowEnd = nRowStart + 6;
	for(int i=nRowStart; i<=nRowEnd; i++) { %>
				<tr align="center">
					<td nowrap><input type="text" name="date_<%=i%>"
						id="date_<%=i%>" readonly size="8" maxlength="10"
						value="<%= props.getProperty("date_"+i, "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="date_<%=i%>_cal">
					</td>
					<td><input type="text" name="age_<%=i%>"
						onDblClick="calcAge(this)" size="1" maxlength="5"
						value="<%= props.getProperty("age_"+i, "") %>" @oscar.formDB /></td>
					<td><input type="text" name="stature_<%=i%>"
						onDblClick="htEnglish2Metric(this)" size="3" maxlength="6"
						value="<%= props.getProperty("stature_"+i, "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="weight_<%=i%>"
						onDblClick="wtEnglish2Metric(this)" size="3" maxlength="6"
						value="<%= props.getProperty("weight_"+i, "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="comment_<%=i%>" size="20"
						maxlength="25" value="<%= props.getProperty("comment_"+i, "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="bmi_<%=i%>"
						onDblClick="calcBMIMetric(this)" size="1" maxlength="5"
						value="<%= props.getProperty("bmi_"+i, "") %>" @oscar.formDB /></td>
				</tr>
				<% } %>
				<tr class="Head">
					<td align="center" colspan="6"><input type="submit"
						value="Print Growth"
						onclick="javascript:return onPrintStatureWeight(3);return false;" />
					<input type="submit" value="Print BMI"
						onclick="javascript:return onPrintBMI(3);return false;" /></td>
				</tr>

			</table>

			</td>
			<td>


			<table width="100%" border="1" cellspacing="1" cellpadding="0"
				bgcolor="<%= bGirl? girlColor:boyColor%>">
				<tr>
					<th width="10%">Date</th>
					<th width="5%">Age</th>
					<th width="10%">Stature<br>
					(cm)</th>
					<th width="10%">Weight<br>
					(kg)</th>
					<th width="40%">Comment</th>
					<th>BMI</th>
				</tr>
				<%
	n = 4;
	nRowStart = (n-1)*7 + 1;
	nRowEnd = nRowStart + 6;
	for(int i=nRowStart; i<=nRowEnd; i++) { %>
				<tr align="center">
					<td nowrap><input type="text" name="date_<%=i%>"
						id="date_<%=i%>" readonly size="8" maxlength="10"
						value="<%= props.getProperty("date_"+i, "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="date_<%=i%>_cal">
					</td>
					<td><input type="text" name="age_<%=i%>"
						onDblClick="calcAge(this)" size="1" maxlength="5"
						value="<%= props.getProperty("age_"+i, "") %>" @oscar.formDB /></td>
					<td><input type="text" name="stature_<%=i%>"
						onDblClick="htEnglish2Metric(this)" size="3" maxlength="6"
						value="<%= props.getProperty("stature_"+i, "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="weight_<%=i%>"
						onDblClick="wtEnglish2Metric(this)" size="3" maxlength="6"
						value="<%= props.getProperty("weight_"+i, "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="comment_<%=i%>" size="20"
						maxlength="25" value="<%= props.getProperty("comment_"+i, "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="bmi_<%=i%>"
						onDblClick="calcBMIMetric(this)" size="1" maxlength="5"
						value="<%= props.getProperty("bmi_"+i, "") %>" @oscar.formDB /></td>
				</tr>
				<% } %>
				<tr class="Head">
					<td align="center" colspan="6"><input type="submit"
						value="Print Growth"
						onclick="javascript:return onPrintStatureWeight(4);return false;" />
					<input type="submit" value="Print BMI"
						onclick="javascript:return onPrintBMI(4);return false;" /></td>
				</tr>
			</table>


			</td>
		</tr>

		<tr>
			<td>

			<table width="100%" border="1" cellspacing="1" cellpadding="0"
				bgcolor="<%= bGirl? girlColor:boyColor%>">
				<tr>
					<th width="10%">Date</th>
					<th width="5%">Age</th>
					<th width="10%">Stature<br>
					(cm)</th>
					<th width="10%">Weight<br>
					(kg)</th>
					<th width="40%">Comment</th>
					<th>BMI</th>
				</tr>
				<%
	n = 5;
	nRowStart = (n-1)*7 + 1;
	nRowEnd = nRowStart + 6;
	for(int i=nRowStart; i<=nRowEnd; i++) { %>
				<tr align="center">
					<td nowrap><input type="text" name="date_<%=i%>"
						id="date_<%=i%>" readonly size="8" maxlength="10"
						value="<%= props.getProperty("date_"+i, "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="date_<%=i%>_cal">
					</td>
					<td><input type="text" name="age_<%=i%>"
						onDblClick="calcAge(this)" size="1" maxlength="5"
						value="<%= props.getProperty("age_"+i, "") %>" @oscar.formDB /></td>
					<td><input type="text" name="stature_<%=i%>"
						onDblClick="htEnglish2Metric(this)" size="3" maxlength="6"
						value="<%= props.getProperty("stature_"+i, "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="weight_<%=i%>"
						onDblClick="wtEnglish2Metric(this)" size="3" maxlength="6"
						value="<%= props.getProperty("weight_"+i, "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="comment_<%=i%>" size="20"
						maxlength="25" value="<%= props.getProperty("comment_"+i, "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="bmi_<%=i%>"
						onDblClick="calcBMIMetric(this)" size="1" maxlength="5"
						value="<%= props.getProperty("bmi_"+i, "") %>" @oscar.formDB /></td>
				</tr>
				<% } %>
				<tr class="Head">
					<td align="center" colspan="6"><input type="submit"
						value="Print Growth"
						onclick="javascript:return onPrintStatureWeight(5);return false;" />
					<input type="submit" value="Print BMI"
						onclick="javascript:return onPrintBMI(5);return false;" /></td>
				</tr>

			</table>

			</td>
			<td>


			<table width="100%" border="1" cellspacing="1" cellpadding="0"
				bgcolor="<%= bGirl? girlColor:boyColor%>">
				<tr>
					<th width="10%">Date</th>
					<th width="5%">Age</th>
					<th width="10%">Stature<br>
					(cm)</th>
					<th width="10%">Weight<br>
					(kg)</th>
					<th width="40%">Comment</th>
					<th>BMI</th>
				</tr>
				<%
	n = 6;
	nRowStart = (n-1)*7 + 1;
	nRowEnd = nRowStart + 6;
	for(int i=nRowStart; i<=nRowEnd; i++) { %>
				<tr align="center">
					<td nowrap><input type="text" name="date_<%=i%>"
						id="date_<%=i%>" readonly size="8" maxlength="10"
						value="<%= props.getProperty("date_"+i, "") %>" @oscar.formDB
						dbType="date" /> <img src="../images/cal.gif" id="date_<%=i%>_cal">
					</td>
					<td><input type="text" name="age_<%=i%>"
						onDblClick="calcAge(this)" size="1" maxlength="5"
						value="<%= props.getProperty("age_"+i, "") %>" @oscar.formDB /></td>
					<td><input type="text" name="stature_<%=i%>"
						onDblClick="htEnglish2Metric(this)" size="3" maxlength="6"
						value="<%= props.getProperty("stature_"+i, "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="weight_<%=i%>"
						onDblClick="wtEnglish2Metric(this)" size="3" maxlength="6"
						value="<%= props.getProperty("weight_"+i, "") %>" @oscar.formDB />
					</td>
					<td><input type="text" name="comment_<%=i%>" size="20"
						maxlength="25" value="<%= props.getProperty("comment_"+i, "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="bmi_<%=i%>"
						onDblClick="calcBMIMetric(this)" size="1" maxlength="5"
						value="<%= props.getProperty("bmi_"+i, "") %>" @oscar.formDB /></td>
				</tr>
				<% } %>
				<tr class="Head">
					<td align="center" colspan="6"><input type="submit"
						value="Print Growth"
						onclick="javascript:return onPrintStatureWeight(6);return false;" />
					<input type="submit" value="Print BMI"
						onclick="javascript:return onPrintBMI(6);return false;" /></td>
				</tr>
			</table>


			</td>
		</tr>

	</table>
	</center>

	<table class="Head" class="hidePrint">
		<tr>
			<td align="left"><input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <input
				type="submit" value="Exit" onclick="javascript:return onExit();" />
			<!--input type="submit" value="Print Growth" onclick="javascript:return onPrintStatureWeight();return false;"/>
            <input type="submit" value="Print BMI" onclick="javascript:return onPrintBMI();return false;"/-->
			</td>
		</tr>
	</table>

</html:form>
<script type="text/javascript">
	<%
	int nRStart = 1;
	int nREnd = 42;
	for(int i=nRStart; i<=nREnd; i++) { %>
Calendar.setup({ inputField : "date_<%=i%>", ifFormat : "%Y/%m/%d", showsTime :false, button : "date_<%=i%>_cal" });
	<% } %>
</script>
</body>
</html:html>
