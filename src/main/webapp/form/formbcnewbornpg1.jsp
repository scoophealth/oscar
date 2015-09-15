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
<% java.util.Properties oscarVariables = oscar.OscarProperties.getInstance(); %>

<%
	String formClass = "BCNewBorn";
	String formLink = "formbcnewbornpg1.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

    FrmData fd = new FrmData();
    String resource = fd.getResource();
    //resource = resource + "ob/riskinfo/";    props.setProperty("c_lastVisited", "pg1");

	//get project_home
	String project_home = request.getContextPath().substring(1);
	boolean bSync = false;
	if(!props.getProperty("c_surname_cur", "").equals("") && !(props.getProperty("c_surname_cur", "").equals(props.getProperty("c_surname", "")) 
	        && props.getProperty("c_givenName_cur", "").equals(props.getProperty("c_givenName", ""))
	        && props.getProperty("c_address_cur", "").equals(props.getProperty("c_address", ""))
	        && props.getProperty("c_city_cur", "").equals(props.getProperty("c_city", ""))
	        && props.getProperty("c_province_cur", "").equals(props.getProperty("c_province", ""))
	        && props.getProperty("c_postal_cur", "").equals(props.getProperty("c_postal", ""))
	        //&& props.getProperty("c_phn_cur", "").equals(props.getProperty("c_phn", ""))
	        && props.getProperty("c_phone_cur", "").trim().equals(props.getProperty("c_phone", "").trim())
	        )) {
	    bSync = true;
	}
%>
<%
  boolean bView = false;
  if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true;
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>New Born Record (Baby)</title>
<link rel="stylesheet" type="text/css"
	href="<%=bView?"bcArStyleView.css" : "bcArStyle.css"%>">
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
    function onPrint() {
        document.forms[0].submit.value="print"; //printAR1
        var ret = checkAllDates();
        if(ret==true)
        {
            document.forms[0].action = "../form/createpdf?__title=British+Columbia+Newborn+Record+Part+1&__cfgfile=bcnb1PrintCfgPg1&__template=bcnewborn1";
            document.forms[0].target="_blank";
        }
        return ret;
    }
    function onSave() {
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
        if(ret==true) {
			ret = checkAllTimes();
		}
        if(ret==true) {
            reset();
            ret = confirm("Are you sure you want to save this form?");
        }
        return ret;
    }
    
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        var ret = checkAllDates();
        if(ret==true) {
			ret = checkAllTimes();
		}
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


function showDef(str, field) { 
    if(document.getElementById)	{
        field.value = str;
    }
}
function syncDemo() { 
    document.forms[0].c_surname.value = "<%=props.getProperty("c_surname_cur", "")%>";
    document.forms[0].c_givenName.value = "<%=props.getProperty("c_givenName_cur", "")%>";
    document.forms[0].c_address.value = "<%=props.getProperty("c_address_cur", "")%>";
    document.forms[0].c_city.value = "<%=props.getProperty("c_city_cur", "")%>";
    document.forms[0].c_province.value = "<%=props.getProperty("c_province_cur", "")%>";
    document.forms[0].c_postal.value = "<%=props.getProperty("c_postal_cur", "")%>";
    //document.forms[0].c_phn.value = "<%=props.getProperty("c_phn_cur", "")%>";
    document.forms[0].c_phone.value = "<%=props.getProperty("c_phone_cur", "")%>";
}

/**
 * DHTML date validation script. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
 */
// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1900;
var maxYear=9900;

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

    function stripCharsInBag(s, bag){
        var i;
        var returnString = "";
        // Search through string's characters one by one.
        // If character is not in bag, append to returnString.
        for (i = 0; i < s.length; i++){
            var c = s.charAt(i);
            if (bag.indexOf(c) == -1) returnString += c;
        }
        return returnString;
    }

    function daysInFebruary (year){
        // February has 29 days in any year evenly divisible by four,
        // EXCEPT for centurial years which are not also divisible by 400.
        return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
    }
    function DaysArray(n) {
        for (var i = 1; i <= n; i++) {
            this[i] = 31
            if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
            if (i==2) {this[i] = 29}
       }
       return this
    }

    function isDate(dtStr){
        var daysInMonth = DaysArray(12)
        var pos1=dtStr.indexOf(dtCh)
        var pos2=dtStr.indexOf(dtCh,pos1+1)
        var strMonth=dtStr.substring(0,pos1)
        var strDay=dtStr.substring(pos1+1,pos2)
        var strYear=dtStr.substring(pos2+1)
        strYr=strYear
        if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
        if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
        for (var i = 1; i <= 3; i++) {
            if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
        }
        month=parseInt(strMonth)
        day=parseInt(strDay)
        year=parseInt(strYr)
        if (pos1==-1 || pos2==-1){
            return "format"
        }
        if (month<1 || month>12){
            return "month"
        }
        if (day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
            return "day"
        }
        if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
            return "year"
        }
        if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
            return "date"
        }
    return true
    }


    function checkTypeIn(obj) {
      if(!checkTypeNum(obj.value) ) {
          alert ("You must type in a number in the field.");
        }
    }

    function valDate(dateBox)  {
        try    {
            var dateString = dateBox.value;
            if(dateString == "") {
				//alert('dateString'+dateString);
                return true;
            }
            var dt = dateString.split('/');
            var y = dt[2];  var m = dt[1];  var d = dt[0];
            //var y = dt[0];  var m = dt[1];  var d = dt[2];
            var orderString = m + '/' + d + '/' + y;
            var pass = isDate(orderString);

            if(pass!=true) {
                alert('Invalid '+pass+' in field ' + dateBox.name);
                dateBox.focus();
                return false;
            }
        } catch (ex)  {
            alert('Catch Invalid Date in field ' + dateBox.name);
            dateBox.focus();
            return false;
        }
        return true;
    }

    function valTime(dateBox)   {
        try   {
            var dateString = dateBox.value;
            if(dateString == "") {
				//alert('dateString'+dateString);
                return true;
            }
            var dt = dateString.split(':');
            var m = dt[1];  var h = dt[0];
            var pass = false;
			if(h >= 0 && h <=23 && m >=0 && m <=59) { pass = true; }

            if(pass!=true) {
                alert('Invalid data in field ' + dateBox.name);
                dateBox.focus();
                return false;
            }
        }  catch (ex) {
            alert('Catch Invalid Date in field ' + dateBox.name);
            dateBox.focus();
            return false;
        }
        return true;
    }

    function checkAllDates() {
        var b = true;
        if(valDate(document.forms[0].pg1_eddByDate)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_formDate)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_delRomBirthDay)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_Date)==false){
            b = false;
        }

        return b;
    }

	function checkAllTimes() {
        var b = true;
        if(valTime(document.forms[0].pg1_delRomBirthTime)==false){
            b = false;
        } else if(valTime(document.forms[0].pg1_routProEyeTime)==false){
            b = false;
        } else if(valTime(document.forms[0].pg1_routProVitTime)==false){
            b = false;
        } else if(valTime(document.forms[0].pg1_Time)==false){
            b = false;
        }

        return b;
    }
</script>




<script type="text/javascript" language="Javascript">

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

function onCheckMaster(a, groupName) {
    if (!a.checked) {
		var s = groupName;
		unCheck(s);
		//a.checked = false;
    }
}
function onCheckSlave(a, masterName) {
    if (a.checked) {
		if (!isChecked(masterName) ) {
		  a.checked = false;
		} else {
		  a.checked = true;
		}
    }
}
</script>


<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1"
	onLoad="setfocus()">
<%--
@oscar.formDB Table="formBCNewBorn"
@oscar.formDB Field="ID" Type="int(10)" Null="NOT NULL" Key="PRI" Default="" Extra="auto_increment"
@oscar.formDB Field="demographic_no" Type="int(10)" Null="NOT NULL" Default="'0'"
@oscar.formDB Field="provider_no" Type="int(10)" Null="" Default="NULL"
@oscar.formDB Field="formCreated" Type="date" Null="" Default="NULL"
@oscar.formDB Field="formEdited" Type="timestamp"
@oscar.formDB Field="c_lastVisited" Type="char(3)"
--%>

<html:form action="/form/formname">
	<input type="hidden" name="c_lastVisited" value="pg1" />
	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="formId" value="<%=formId%>" />
	<input type="hidden" name="provider_no" value=<%=""+provNo%> />
	<!--input type="hidden" name="provNo" value="<%= request.getParameter("provNo") %>" /-->
	<input type="hidden" name="submit" value="exit" />

	<table class="Head" class="hidePrint">
		<tr>
			<td align="left">
			<%
  if (!bView) {
%> <input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <%
  }
%> <input type="submit" value="Exit"
				onclick="javascript:return onExit();" /> <input type="submit"
				value="Print" onclick="javascript:return onPrint();" /></td>
			<%
  if (!bView) {
%>

			<td align="right"><!--  b>View:</b>
            <a href="javascript: popupPage('formbcnewbornpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">Part2 <font size=-2>(pg.1)</font></a> |
            <a href="javascript: popupPage('formbcnewbornpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">Part2 <font size=-2>(pg.2)</font></a>
            &nbsp;--></td>
			<td align="right"><b>Edit:</b>Part1 | <a
				href="formbcnewbornpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Part2
			<font size=-2>(pg.1)</font></a> | <a
				href="formbcnewbornpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Part2
			<font size=-2>(pg.2)</font></a> |</td>
			<%
  }
%>
		</tr>
	</table>


	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td width="60%">

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<th><%=bView?"<font color='yellow'>VIEW PAGE: </font>" : ""%>
					British Columbia Newborn Record Part 1</th>
				</tr>
			</table>

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td width="60%"><b>1.</b> MOTHER'S NAME<br>
					<input type="text" name="c_motherName" style="width: 100%"
						size="50" maxlength="80"
						value="<%= props.getProperty("c_motherName", "") %>" @oscar.formDB />
					</td>
					<td width="8%">AGE<br>
					<input type="text" name="c_motherAge" style="width: 100%" size="2"
						maxlength="4" value="<%= props.getProperty("c_motherAge", "") %>"
						@oscar.formDB /></td>
					<td nowrap>MOTHER'S HOSPITAL #<br>
					<input type="text" name="c_hospitalNo" style="width: 100%"
						size="20" maxlength="30"
						value="<%= props.getProperty("c_hospitalNo", "") %>" @oscar.formDB />
					</td>
				</tr>
			</table>

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td width="40%">SURNAME OF NEWBORN<br>
					<input type="text" name="c_newBornSurname" style="width: 100%"
						size="30" maxlength="60"
						value="<%= props.getProperty("c_newBornSurname", "") %>"
						@oscar.formDB /></td>
					<td width="50%">PARTNER'S NAME<br>
					<input type="text" name="c_partnerName" style="width: 100%"
						size="50" maxlength="80"
						value="<%= props.getProperty("c_partnerName", "") %>"
						@oscar.formDB /></td>
					<td nowrap>AGE<br>
					<input type="text" name="c_partnerAge" style="width: 100%" size="2"
						maxlength="4" value="<%= props.getProperty("c_partnerAge", "") %>"
						@oscar.formDB /></td>
				</tr>
			</table>

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<th colspan="5">G <input type="text" name="c_g"
						style="width: 100%" size="2" maxlength="4"
						value="<%= props.getProperty("c_g", "") %>" @oscar.formDB /></th>
					<th colspan="5">T <input type="text" name="c_t"
						style="width: 100%" size="2" maxlength="4"
						value="<%= props.getProperty("c_t", "") %>" @oscar.formDB /></th>
					<th colspan="5">P <input type="text" name="c_p"
						style="width: 100%" size="2" maxlength="4"
						value="<%= props.getProperty("c_p", "") %>" @oscar.formDB /></th>
					<th colspan="5">A <input type="text" name="c_a"
						style="width: 100%" size="2" maxlength="4"
						value="<%= props.getProperty("c_a", "") %>" @oscar.formDB /></th>
					<th colspan="5">L <input type="text" name="c_l"
						style="width: 100%" size="2" maxlength="4"
						value="<%= props.getProperty("c_l", "") %>" @oscar.formDB /></th>
					<td width="20%">EDD dd/mm/yyyy <input type="text"
						name="pg1_eddByDate" id="pg1_eddByDate" size="8" maxlength="10"
						value="<%= props.getProperty("pg1_eddByDate", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_eddByDate_cal"></td>
					<td width="20%" nowrap align="center">BLOOD GROUP/Rh<br>
					<!--input type="text" name="pg1_bloodGrpRh" style="width:100%" size="10" maxlength="20" value="<%--= props.getProperty("pg1_bloodGrpRh", "") --%>" @oscar.formDB  /-->
					<font size="-2"><%=props.getProperty("pg1_bloodGrpRh", "")%></font>
					<select name="pg1_bloodGrpRh">
						<%
          String[] optBG1 = {"", "O", "A", "B", "AB"};
          for (int i=0; i<optBG1.length; i++) {
          %>
						<option value="<%=optBG1[i]%>"
							<%=props.getProperty("pg1_bloodGrpRh", "").equals(optBG1[i])?"selected":""%>><%=optBG1[i]%></option>
						<%}%>
					</select> <select name="pg1_bloodRh" @oscar.formDB>
						<option value=""
							<%=props.getProperty("pg1_bloodRh", "").equals("")?"selected":""%>></option>
						<option value="pos"
							<%=props.getProperty("pg1_bloodRh", "").equals("pos")?"selected":""%>>pos</option>
						<option value="neg"
							<%=props.getProperty("pg1_bloodRh", "").equals("neg")?"selected":""%>>neg</option>
					</select></td>
					<td nowrap>RH ANTIB<br>
					<!--input type="text" name="pg1_RhAntib" style="width:100%" size="10" maxlength="20" value="<%--= props.getProperty("pg1_RhAntib", "") --%>" @oscar.formDB  /-->
					<select name="pg1_RhAntib" @oscar.formDB>
						<option value=""
							<%=props.getProperty("pg1_RhAntib", "").equals("")?"selected":""%>></option>
						<option value="None"
							<%=props.getProperty("pg1_RhAntib", "").equals("None")?"selected":""%>>None</option>
						<option value="+ve"
							<%=props.getProperty("pg1_RhAntib", "").equals("+ve")?"selected":""%>>+ve</option>
						<option value="-ve"
							<%=props.getProperty("pg1_RhAntib", "").equals("-ve")?"selected":""%>>-ve</option>
					</select> <font size="-2"><%=props.getProperty("pg1_RhAntib", "")%></font></td>
					<td nowrap>HBsAg<br>
					<!-- input type="text" name="pg1_HBsAg" style="width:100%" size="10" maxlength="20" value="<%= props.getProperty("pg1_HBsAg", "") %>" @oscar.formDB  /-->
					<select name="pg1_HBsAg" @oscar.formDB>
						<option value=""
							<%=props.getProperty("pg1_HBsAg", "").equals("")?"selected":""%>></option>
						<option value="NR"
							<%=props.getProperty("pg1_HBsAg", "").equals("NR")?"selected":""%>>NR</option>
						<option value="R"
							<%=props.getProperty("pg1_HBsAg", "").equals("R")?"selected":""%>>R</option>
					</select> <font size="-2"><%=props.getProperty("pg1_HBsAg", "")%></font></td>
				</tr>
			</table>

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td>RISK FACTORS FOR INFANT (Refer to Antenatal Record, Part
					2)<br>
					<textarea name="c_riskFactor" style="width: 100%" cols="50"
						rows="2" @oscar.formDB dbType="varchar(255)"> <%= props.getProperty("c_riskFactor", "") %> </textarea>
					</td>
				</tr>
			</table>

			</td>
			<td>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="55%">HOSPITAL NAME<br>
					<input type="text" name="c_hospitalName"
						<%=oscarVariables.getProperty("BCAR_hospital")==null? " ": ("class=\"spe\" onDblClick='showDef(\""+oscarVariables.getProperty("BCAR_hospital")+"\", this);'") %>
						style="width: 100%" size="30" maxlength="80"
						value="<%= props.getProperty("c_hospitalName", "") %>"
						@oscar.formDB /></td>
					<td>DATE <img src="../images/cal.gif" id="pg1_formDate_cal">
					<%=bSync? ("<b><a href=# onClick='syncDemo(); return false;'><font color='red'>Synchronize</font></a></b>") :"" %>
					<br>
					<input type="text" name="pg1_formDate" id="pg1_formDate" size="10"
						maxlength="10"
						value="<%= props.getProperty("pg1_formDate", "") %>" @oscar.formDB
						dbType="date" /></td>
				</tr>
				<tr>
					<td width="55%">SURNAME<br>
					<input type="text" name="c_surname" style="width: 100%" size="30"
						maxlength="30" value="<%= props.getProperty("c_surname", "") %>"
						@oscar.formDB /></td>
					<td>GIVEN NAME<br>
					<input type="text" name="c_givenName" style="width: 100%" size="30"
						maxlength="30" value="<%= props.getProperty("c_givenName", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td>ADDRESS<br>
					<input type="text" name="c_address" style="width: 100%" size="50"
						maxlength="60" value="<%= props.getProperty("c_address", "") %>"
						@oscar.formDB /> <input type="text" name="c_city"
						style="width: 100%" size="50" maxlength="60"
						value="<%= props.getProperty("c_city", "") %>" @oscar.formDB /> <input
						type="text" name="c_province" size="18" maxlength="50"
						value="<%= props.getProperty("c_province", "") %>" @oscar.formDB />
					<input type="text" name="c_postal" size="7" maxlength="8"
						value="<%= props.getProperty("c_postal", "") %>" @oscar.formDB />
					</td>
					<td valign="top">PHONE NUMBER<br>
					<input type="text" name="c_phone" style="width: 100%" size="60"
						maxlength="60" value="<%= props.getProperty("c_phone", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td colspan="2"><span class="small9"> <a href=#
						onClick="popupFixedPage(600, 300, 'formbcarpg1namepopup.jsp?fieldname=c_phyMid'); return false;">PHYSICIAN
					/ MIDWIFE NAME</a></span><br>
					<input type="text" name="c_phyMid" style="width: 100%" size="30"
						maxlength="60" value="<%= props.getProperty("c_phyMid", "") %>"
						@oscar.formDB /></td>
				</tr>
			</table>

			</td>
		</tr>
	</table>



	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td width="40%" valign="top">

			<table class="small9" width="100%" border="1" cellspacing="0"
				cellpadding="0">
				<tr>
					<th colspan="7" align="left">2. APGAR SCORE</th>
				</tr>
				<tr align="center">
					<td width="14%"></td>
					<td width="14%">0</td>
					<td width="14%">1</td>
					<td width="14%">2</td>
					<td width="14%"><font color="red">1 MIN.</font></td>
					<td width="14%"><font color="red">5 MIN.</font></td>
					<td width="14%"><font color="red">10 MIN.</font></td>
				</tr>
				<tr align="center">
					<td><B>HEART RATE</B></td>
					<td>ABSENT</td>
					<td>BELOW 100</td>
					<td>ABOVE 100</td>
					<td><input type="text" name="pg1_apgarHeart1"
						style="width: 100%" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_apgarHeart1", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_apgarHeart5"
						style="width: 100%" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_apgarHeart5", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_apgarHeart10"
						style="width: 100%" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_apgarHeart10", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr align="center">
					<td><B>RESP. EFFORT</B></td>
					<td>ABSENT</td>
					<td>SLOW IRREG.</td>
					<td>GOOD CRYING</td>
					<td><input type="text" name="pg1_apgarResp1"
						style="width: 100%" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_apgarResp1", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_apgarResp5"
						style="width: 100%" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_apgarResp5", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_apgarResp10"
						style="width: 100%" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_apgarResp10", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr align="center">
					<td><B>MUSCLE TONE</B></td>
					<td>LIMP</td>
					<td>SOME FLEXION</td>
					<td>ACTIVE MOTION</td>
					<td><input type="text" name="pg1_apgarMuscle1"
						style="width: 100%" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_apgarMuscle1", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_apgarMuscle5"
						style="width: 100%" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_apgarMuscle5", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_apgarMuscle10"
						style="width: 100%" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_apgarMuscle10", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr align="center">
					<td><B>RESPONSE TO STIM.</B></td>
					<td>NONE</td>
					<td>GRIMACE</td>
					<td>COUGH OR SNEEZE</td>
					<td><input type="text" name="pg1_apgarStim1"
						style="width: 100%" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_apgarStim1", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_apgarStim5"
						style="width: 100%" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_apgarStim5", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_apgarStim10"
						style="width: 100%" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_apgarStim10", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr align="center">
					<td><B>COLOUR</B></td>
					<td>BLUE PALE</td>
					<td>BODY PINK BLUE EXTREM.</td>
					<td>ALL PINK</td>
					<td><input type="text" name="pg1_apgarColor1"
						style="width: 100%" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_apgarColor1", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_apgarColor5"
						style="width: 100%" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_apgarColor5", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_apgarColor10"
						style="width: 100%" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_apgarColor10", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr align="center">
					<td align="right" colspan="4"><b>APGAR TOTAL SCORE</b></td>
					<td><input type="text" name="pg1_apgarTotal1"
						style="width: 100%" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_apgarTotal1", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_apgarTotal5"
						style="width: 100%" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_apgarTotal5", "") %>"
						@oscar.formDB /></td>
					<td><input type="text" name="pg1_apgarTotal10"
						style="width: 100%" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_apgarTotal10", "") %>"
						@oscar.formDB /></td>
				</tr>
			</table>



			<table class="small9" width="100%" border="1" cellspacing="0"
				cellpadding="0">
				<tr>
					<th colspan="2" align="left">4. DELIVERY ROOM</th>
				</tr>
				<tr>
					<td colspan="2">BIRTHDATE <input type="text"
						name="pg1_delRomBirthDay" id="pg1_delRomBirthDay" size="10"
						maxlength="10"
						value="<%= props.getProperty("pg1_delRomBirthDay", "") %>"
						@oscar.formDB dbType="date" /> <img src="../images/cal.gif"
						id="pg1_delRomBirthDay_cal"> TIME <input type="text"
						name="pg1_delRomBirthTime" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_delRomBirthTime", "") %>"
						@oscar.formDB dbType="time" /></td>
				</tr>
				<tr>
					<td width="50%">DELIVERY TYPE<br>
					<!--input type="text" name="pg1_delRomDelType" style="width:100%" size="10" maxlength="30" value="<%--= props.getProperty("pg1_delRomDelType", "") --%>" @oscar.formDB/-->
					<select name="pg1_delRomDelType">
						<%
	String[] optBG = {"", "SVD", "C-section", "Vacuum", "Forceps", "Vacuum and Forceps", "Forceps Trial and C-section"};
	for (int i=0; i<optBG.length; i++) {
	%>
						<option value="<%=optBG[i]%>"
							<%=props.getProperty("pg1_delRomDelType", "").equals(optBG[i])?"selected":""%>><%=optBG[i]%></option>
						<%}%>
					</select></td>
					<td>NEWBORN HOSPITAL #<br>
					<input type="text" name="pg1_delRomHospNo" style="width: 100%"
						size="10" maxlength="20"
						value="<%= props.getProperty("pg1_delRomHospNo", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td colspan="2">IDENTIFIED AT BIRTH BY: <input type="text"
						name="pg1_delRomBirthBy" size="25" maxlength="60"
						value="<%= props.getProperty("pg1_delRomBirthBy", "") %>"
						@oscar.formDB /> <br>
					Signature: <input type="text" name="pg1_delRomBirthBySig" size="30"
						maxlength="60"
						value="<%= props.getProperty("pg1_delRomBirthBySig", "") %>"
						@oscar.formDB /> RN/RM <br>
					IDENTIFIED AT TRANSFER BY: (if appropriate) <input type="text"
						name="pg1_delRomTranBy" size="15" maxlength="60"
						value="<%= props.getProperty("pg1_delRomTranBy", "") %>"
						@oscar.formDB /> <br>
					Signature: <input type="text" name="pg1_delRomTranBySig" size="30"
						maxlength="60"
						value="<%= props.getProperty("pg1_delRomTranBySig", "") %>"
						@oscar.formDB /> RN/RM</td>
				</tr>
				<tr>
					<td>VOIDED<br>
					<input type="checkbox" name="pg1_delRomVoidN"
						<%= props.getProperty("pg1_delRomVoidN", "") %> @oscar.formDB
						dbType="tinyint(1)" /> No &nbsp;&nbsp;&nbsp; <input
						type="checkbox" name="pg1_delRomVoidY"
						<%= props.getProperty("pg1_delRomVoidY", "") %> @oscar.formDB
						dbType="tinyint(1)" /> Yes</td>
					<td>PASSED MECONIUM<br>
					<input type="checkbox" name="pg1_delRomPassN"
						<%= props.getProperty("pg1_delRomPassN", "") %> @oscar.formDB
						dbType="tinyint(1)" /> No &nbsp;&nbsp;&nbsp; <input
						type="checkbox" name="pg1_delRomPassY"
						<%= props.getProperty("pg1_delRomPassY", "") %> @oscar.formDB
						dbType="tinyint(1)" /> Yes</td>
				</tr>
				<tr>
					<td colspan="2">FEEDING PLAN <input type="checkbox"
						name="pg1_delRomBreast"
						<%= props.getProperty("pg1_delRomBreast", "") %> @oscar.formDB
						dbType="tinyint(1)" /> Breast &nbsp;&nbsp;&nbsp; <input
						type="checkbox" name="pg1_delRomFormula"
						<%= props.getProperty("pg1_delRomFormula", "") %> @oscar.formDB
						dbType="tinyint(1)" /> Formula <input type="checkbox"
						name="pg1_delRomDonorMilk"
						<%= props.getProperty("pg1_delRomDonorMilk", "") %> @oscar.formDB
						dbType="tinyint(1)" /> Donor Milk</td>
				</tr>
			</table>


			<table class="small9" width="100%" border="1" cellspacing="0"
				cellpadding="0">
				<tr>
					<td colspan="2" align="left"><B>5. ROUTINE PROCEDURES</B><br>
					CORD BLOOD &nbsp;&nbsp;&nbsp; <input type="checkbox"
						name="pg1_routProBldRh"
						<%= props.getProperty("pg1_routProBldRh", "") %> @oscar.formDB
						dbType="tinyint(1)" /> Rh &nbsp;&nbsp;&nbsp; <input
						type="checkbox" name="pg1_routProBldOther"
						<%= props.getProperty("pg1_routProBldOther", "") %> @oscar.formDB
						dbType="tinyint(1)" /> Other</td>
				</tr>
				<tr>
					<td>EYE PROPHYLAXIS<BR>
					<input type="checkbox" name="pg1_routProEyeEryth"
						<%= props.getProperty("pg1_routProEyeEryth", "") %> @oscar.formDB
						dbType="tinyint(1)" /> Erythromycin &nbsp;&nbsp;&nbsp; <input
						type="checkbox" name="pg1_routProEyeOther"
						<%= props.getProperty("pg1_routProEyeOther", "") %> @oscar.formDB
						dbType="tinyint(1)" /> Other: <input type="text"
						name="pg1_routProEyeOtherSpec" size="15" maxlength="20"
						value="<%= props.getProperty("pg1_routProEyeOtherSpec", "") %>"
						@oscar.formDB /> Time: <input type="text"
						name="pg1_routProEyeTime" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_routProEyeTime", "") %>"
						@oscar.formDB dbType="time" /> <BR>
					Signature <input type="text" name="pg1_routProEyeSig" size="30"
						maxlength="50"
						value="<%= props.getProperty("pg1_routProEyeSig", "") %>"
						@oscar.formDB /> RN/RM</td>
				</tr>
				<tr>
					<td>VITAMIN K<BR>
					<input type="checkbox" name="pg1_routProVitIM"
						<%= props.getProperty("pg1_routProVitIM", "") %> @oscar.formDB
						dbType="tinyint(1)" /> IM &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input
						type="checkbox" name="pg1_routProVitPO"
						<%= props.getProperty("pg1_routProVitPO", "") %> @oscar.formDB
						dbType="tinyint(1)" /> PO &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					Time: <input type="text" name="pg1_routProVitTime" size="5"
						maxlength="5"
						value="<%= props.getProperty("pg1_routProVitTime", "") %>"
						@oscar.formDB dbType="time" /> <BR>
					Signature <input type="text" name="pg1_routProVitSig" size="30"
						maxlength="50"
						value="<%= props.getProperty("pg1_routProVitSig", "") %>"
						@oscar.formDB /> RN/RM</td>
				</tr>
			</table>


			<table class="small9" width="100%" border="1" cellspacing="0"
				cellpadding="1">
				<tr>
					<td colspan="3"><b>6. EVALUATION OF DEVELOPMENT</b><br>
					<I>(growth chart and curve on reverse)</I></td>
				</tr>
				<tr>
					<td width="30%">BIRTHWEIGHT</td>
					<td width="35%" align="right"><input type="text"
						name="pg1_evaBirG" size="6" maxlength="10"
						value="<%= props.getProperty("pg1_evaBirG", "") %>" @oscar.formDB />
					&nbsp;&nbsp;g</td>
					<td align="right"><input type="text" name="pg1_evaBirP"
						size="6" maxlength="10"
						value="<%= props.getProperty("pg1_evaBirP", "") %>" @oscar.formDB />
					%</td>
				</tr>
				<tr>
					<td>LENGTH</td>
					<td align="right"><input type="text" name="pg1_evaLenCm"
						size="6" maxlength="10"
						value="<%= props.getProperty("pg1_evaLenCm", "") %>" @oscar.formDB />
					cm</td>
					<td align="right"><input type="text" name="pg1_evaLenP"
						size="6" maxlength="10"
						value="<%= props.getProperty("pg1_evaLenP", "") %>" @oscar.formDB />
					%</td>
				</tr>
				<tr>
					<td>HEAD CIRCUMFERENCE</td>
					<td align="right"><input type="text" name="pg1_evaHeadCm"
						size="6" maxlength="10"
						value="<%= props.getProperty("pg1_evaHeadCm", "") %>"
						@oscar.formDB /> cm</td>
					<td align="right"><input type="text" name="pg1_evaHeadP"
						size="6" maxlength="10"
						value="<%= props.getProperty("pg1_evaHeadP", "") %>" @oscar.formDB />
					%</td>
				</tr>
				<tr>
					<td colspan="3">

					<table class="small9" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td width="33%"><input type="checkbox" name="pg1_evaPret"
								<%= props.getProperty("pg1_evaPret", "") %> @oscar.formDB
								dbType="tinyint(1)" /> PRETERM</td>
							<td width="30%"><input type="checkbox" name="pg1_evaTerm"
								<%= props.getProperty("pg1_evaTerm", "") %> @oscar.formDB
								dbType="tinyint(1)" /> TERM</td>
							<td><input type="checkbox" name="pg1_evaPostTerm"
								<%= props.getProperty("pg1_evaPostTerm", "") %> @oscar.formDB
								dbType="tinyint(1)" /> POSTTERM</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_evaSga"
								<%= props.getProperty("pg1_evaSga", "") %> @oscar.formDB
								dbType="tinyint(1)" /> SGA</td>
							<td><input type="checkbox" name="pg1_evaAga"
								<%= props.getProperty("pg1_evaAga", "") %> @oscar.formDB
								dbType="tinyint(1)" /> AGA</td>
							<td><input type="checkbox" name="pg1_evaLga"
								<%= props.getProperty("pg1_evaLga", "") %> @oscar.formDB
								dbType="tinyint(1)" /> LGA</td>
						</tr>
					</table>

					</td>
				</tr>
			</table>


			<table class="small9" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td width="60%" colspan="2"><b>7. STILL BIRTH</b></td>
					<td width="10%">NO</td>
					<td>YES</td>
				</tr>
				<tr>
					<td width="3%"></td>
					<td>MACERATED</td>
					<td><input type="checkbox" name="pg1_stillMaceN"
						<%= props.getProperty("pg1_stillMaceN", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><input type="checkbox" name="pg1_stillMaceY"
						<%= props.getProperty("pg1_stillMaceY", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
				</tr>
				<tr>
					<td></td>
					<td>IUGR</td>
					<td><input type="checkbox" name="pg1_stillIugrN"
						<%= props.getProperty("pg1_stillIugrN", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><input type="checkbox" name="pg1_stillIugrY"
						<%= props.getProperty("pg1_stillIugrY", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
				</tr>
				<tr>
					<td></td>
					<td>RETROPLACENTAL CLOT</td>
					<td><input type="checkbox" name="pg1_stillRetroN"
						<%= props.getProperty("pg1_stillRetroN", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><input type="checkbox" name="pg1_stillRetroY"
						<%= props.getProperty("pg1_stillRetroY", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
				</tr>
				<tr>
					<td></td>
					<td>EVIDENCE OF ANEMIA</td>
					<td><input type="checkbox" name="pg1_stillEvidN"
						<%= props.getProperty("pg1_stillEvidN", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><input type="checkbox" name="pg1_stillEvidY"
						<%= props.getProperty("pg1_stillEvidY", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
				</tr>
				<tr>
					<td></td>
					<td>AUTOPSY CONSENT</td>
					<td><input type="checkbox" name="pg1_stillAutoN"
						<%= props.getProperty("pg1_stillAutoN", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><input type="checkbox" name="pg1_stillAutoY"
						<%= props.getProperty("pg1_stillAutoY", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
				</tr>
				<tr>
					<td></td>
					<td>OBVIOUS ANOMALY(descibe below):</td>
					<td><input type="checkbox" name="pg1_stillObviN"
						<%= props.getProperty("pg1_stillObviN", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><input type="checkbox" name="pg1_stillObviY"
						<%= props.getProperty("pg1_stillObviY", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
				</tr>
				<tr>
					<td></td>
					<td colspan="4" align="right">umblical cord length <input
						type="text" name="pg1_stillUmbCordLen" size="6" maxlength="10"
						value="<%= props.getProperty("pg1_stillUmbCordLen", "") %>"
						@oscar.formDB /> cm</td>
				</tr>
			</table>


			</td>
			<td valign="top">


			<table class="small8" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td colspan="4"><b><span class="small9">3.
					RESUSCITATION SUMMARY</span></b> (Use Hospital Progress Notes if insufficient
					space for narrative)</td>
				</tr>
				<tr>
					<td width="20%">AMNIOTIC FLUID:</td>
					<td width="30%"><input type="checkbox" name="pg1_ResAmnClear"
						<%= props.getProperty("pg1_ResAmnClear", "") %> @oscar.formDB
						dbType="tinyint(1)" /> CLEAR</td>
					<td width="30%"><input type="checkbox" name="pg1_ResAmnMeco"
						<%= props.getProperty("pg1_ResAmnMeco", "") %> @oscar.formDB
						dbType="tinyint(1)" /> MECONIUM STAINED</td>
					<td></td>
				</tr>
				<tr>
					<td>SUCTION:</td>
					<td><input type="checkbox" name="pg1_ResSucPeri"
						<%= props.getProperty("pg1_ResSucPeri", "") %> @oscar.formDB
						dbType="tinyint(1)" /> AT PERINEUM</td>
					<td><input type="checkbox" name="pg1_ResSucOrop"
						<%= props.getProperty("pg1_ResSucOrop", "") %> @oscar.formDB
						dbType="tinyint(1)" /> OROPHARYNGEAL</td>
					<td><input type="checkbox" name="pg1_ResSucTrac"
						<%= props.getProperty("pg1_ResSucTrac", "") %> @oscar.formDB
						dbType="tinyint(1)" /> TRACHEA</td>
				</tr>
				<tr>
					<td></td>
					<td><input type="checkbox" name="pg1_ResMegCord"
						<%= props.getProperty("pg1_ResMegCord", "") %> @oscar.formDB
						dbType="tinyint(1)" /> MEG. BELOW CORDS</td>
					<td><input type="checkbox" name="pg1_ResStoSuct"
						<%= props.getProperty("pg1_ResStoSuct", "") %> @oscar.formDB
						dbType="tinyint(1)" /> STOMACH SUCTIONED</td>
					<td></td>
				</tr>
			</table>

			<table class="small8" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td colspan="2"><input type="checkbox"
						name="pg1_ResDriedPosAsse"
						<%= props.getProperty("pg1_ResDriedPosAsse", "") %> @oscar.formDB
						dbType="tinyint(1)" /> DRIED, POSITIONED, ASSESSED</td>
				</tr>
				<tr>
					<td width="20%"><input type="checkbox" name="pg1_ResO2Free"
						<%= props.getProperty("pg1_ResO2Free", "") %> @oscar.formDB
						dbType="tinyint(1)" /> O2 FREE FLOW:</td>
					<td>START <input type="text" name="pg1_ResO2Start" size="6"
						maxlength="10"
						value="<%= props.getProperty("pg1_ResO2Start", "") %>"
						@oscar.formDB /> STOP <input type="text" name="pg1_ResO2Stop"
						size="6" maxlength="10"
						value="<%= props.getProperty("pg1_ResO2Stop", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td><input type="checkbox" name="pg1_ResIPPV"
						<%= props.getProperty("pg1_ResIPPV", "") %> @oscar.formDB
						dbType="tinyint(1)" /> IPPV:</td>
					<td>START <input type="text" name="pg1_ResIPPVStart" size="6"
						maxlength="10"
						value="<%= props.getProperty("pg1_ResIPPVStart", "") %>"
						@oscar.formDB /> STOP <input type="text" name="pg1_ResIPPVStop"
						size="6" maxlength="10"
						value="<%= props.getProperty("pg1_ResIPPVStop", "") %>"
						@oscar.formDB /> TIME TO SPONTANEOUS BREATHING <input type="text"
						name="pg1_ResIPPVTimeSpBrea" size="6" maxlength="10"
						value="<%= props.getProperty("pg1_ResIPPVTimeSpBrea", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td colspan="2" align="right">TIME TO HR &gt; 100 <input
						type="text" name="pg1_ResIPPVTimeHR" size="6" maxlength="10"
						value="<%= props.getProperty("pg1_ResIPPVTimeHR", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td colspan="2"><input type="checkbox" name="pg1_ResFormComp"
						<%= props.getProperty("pg1_ResFormComp", "") %> @oscar.formDB
						dbType="tinyint(1)" /> RESUSCITATION FORM COMPLETED<br>
					(document resuscitation requiring IPPV on separate Neonatal
					Resuscitation Record)</td>
				</tr>
			</table>

			<table class="small8" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td width="30%" nowrap>CORD GASES: <input type="checkbox"
						name="pg1_ResCordNotDone"
						<%= props.getProperty("pg1_ResCordNotDone", "") %> @oscar.formDB
						dbType="tinyint(1)" /> NOT DONE</td>
					<td><input type="checkbox" name="pg1_ResCordUA"
						<%= props.getProperty("pg1_ResCordUA", "") %> @oscar.formDB
						dbType="tinyint(1)" /> UA &nbsp;&nbsp;&nbsp; pH <input type="text"
						name="pg1_ResCordPh" size="6" maxlength="10"
						value="<%= props.getProperty("pg1_ResCordPh", "") %>"
						@oscar.formDB /> pCO2 <input type="text" name="pg1_ResCordPco2"
						size="6" maxlength="10"
						value="<%= props.getProperty("pg1_ResCordPco2", "") %>"
						@oscar.formDB /> pO2 <input type="text" name="pg1_ResCordPo2"
						size="6" maxlength="10"
						value="<%= props.getProperty("pg1_ResCordPo2", "") %>"
						@oscar.formDB /> B.E. <input type="text" name="pg1_ResCordBe"
						size="6" maxlength="10"
						value="<%= props.getProperty("pg1_ResCordBe", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td></td>
					<td><input type="checkbox" name="pg1_ResCordUV"
						<%= props.getProperty("pg1_ResCordUV", "") %> @oscar.formDB
						dbType="tinyint(1)" /> UV &nbsp;&nbsp;&nbsp; pH <input type="text"
						name="pg1_ResCordUVPh" size="6" maxlength="10"
						value="<%= props.getProperty("pg1_ResCordUVPh", "") %>"
						@oscar.formDB /> pCO2 <input type="text" name="pg1_ResCordUVPco2"
						size="6" maxlength="10"
						value="<%= props.getProperty("pg1_ResCordUVPco2", "") %>"
						@oscar.formDB /> pO2 <input type="text" name="pg1_ResCordUVPo2"
						size="6" maxlength="10"
						value="<%= props.getProperty("pg1_ResCordUVPo2", "") %>"
						@oscar.formDB /> B.E. <input type="text" name="pg1_ResCordUVBe"
						size="6" maxlength="10"
						value="<%= props.getProperty("pg1_ResCordUVBe", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td colspan="2">Comments: <textarea name="pg1_ResComment"
						style="width: 100%" cols="60" rows="2" @oscar.formDB
						dbType="varchar(255)"> <%= props.getProperty("pg1_ResComment", "") %> </textarea>
					</td>

				</tr>
			</table>

			<table class="small8" width="100%" border="1" cellspacing="0"
				cellpadding="0">
				<tr>
					<td>SIGNATURE <input type="text" name="pg1_ResSig1" size="20"
						maxlength="60" value="<%= props.getProperty("pg1_ResSig1", "") %>"
						@oscar.formDB /> RM/RN</td>
					<td>SIGNATURE <input type="text" name="pg1_ResSig2" size="20"
						maxlength="60" value="<%= props.getProperty("pg1_ResSig2", "") %>"
						@oscar.formDB /> RM/RN</td>
					<td>SIGNATURE <input type="text" name="pg1_ResSig3" size="20"
						maxlength="60" value="<%= props.getProperty("pg1_ResSig3", "") %>"
						@oscar.formDB /> MD</td>
				</tr>
			</table>

			<table class="small8" width="100%" border="1" cellspacing="0"
				cellpadding="0">
				<tr>
					<td colspan="2"><b><span class="small9">8. PHYSICAL
					EXAMINATION AT BIRTH </span>(INCLUDING STILLBIRTHS) </td>
					<td rowspan="2"><input type="checkbox" name="pg1_phyMale"
						<%= props.getProperty("pg1_phyMale", "") %> @oscar.formDB
						dbType="tinyint(1)" /> MALE <input type="checkbox"
						name="pg1_phyFemale"
						<%= props.getProperty("pg1_phyFemale", "") %> @oscar.formDB
						dbType="tinyint(1)" /> FEMALE<br>
					<input type="checkbox" name="pg1_phyAmbiguous"
						<%= props.getProperty("pg1_phyAmbiguous", "") %> @oscar.formDB
						dbType="tinyint(1)" /> AMBIGUOUS</td>
				</tr>
				<tr>
					<td>GESTATIONAL AGE FROM<br>
					ANTENATAL HISTORY <input type="text" name="pg1_phyHistWk" size="6"
						maxlength="6"
						value="<%= props.getProperty("pg1_phyHistWk", "") %>"
						@oscar.formDB /> wks.</td>
					<td>GESTATIONAL AGE FROM<br>
					BY EXAM <i>(see reverse)</i> <input type="text" name="pg1_phyAgeWk"
						size="6" maxlength="6"
						value="<%= props.getProperty("pg1_phyAgeWk", "") %>" @oscar.formDB />
					wks.</td>
				</tr>
			</table>

			<table class="small8" width="100%" border="1" cellspacing="0"
				cellpadding="0">
				<tr>
					<td width="80%">

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td width="19%">1. GENERAL APPEARANCE</td>
							<td width="12%">NORMAL<br>
							<input type="checkbox" name="pg1_phy1Normal"
								<%= props.getProperty("pg1_phy1Normal", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td width="20%">ABNORMAL<br>
							<input type="checkbox" name="pg1_phy1Abnormal"
								<%= props.getProperty("pg1_phy1Abnormal", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td align="center">COMMENTS<br>
							<textarea name="pg1_phy1Comm" style="width: 100%" cols="30"
								rows="2" @oscar.formDB dbType="varchar(255)"> <%= props.getProperty("pg1_phy1Comm", "") %> </textarea>
							</td>
						</tr>
					</table>

					</td>
					<td>TEMP. <input type="text" name="pg1_phy1Temp" size="5"
						maxlength="6" value="<%= props.getProperty("pg1_phy1Temp", "") %>"
						@oscar.formDB /> <br>
					RR.&nbsp;&nbsp; <input type="text" name="pg1_phy1RR" size="5"
						maxlength="6" value="<%= props.getProperty("pg1_phy1RR", "") %>"
						@oscar.formDB /> <br>
					HR.&nbsp;&nbsp; <input type="text" name="pg1_phy1HR" size="5"
						maxlength="6" value="<%= props.getProperty("pg1_phy1HR", "") %>"
						@oscar.formDB />
				</tr>
			</table>

			<table width="100%" border="1" cellspacing="0" cellpadding="2">
				<tr>
					<td>

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td width="15%">2. SKIN</td>
							<td width="10%"><input type="checkbox" name="pg1_phy2Normal"
								<%= props.getProperty("pg1_phy2Normal", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td width="20%"><input type="checkbox" name="pg1_phy2Pallor"
								<%= props.getProperty("pg1_phy2Pallor", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Pallor<br>
							<input type="checkbox" name="pg1_phy2Bruis"
								<%= props.getProperty("pg1_phy2Bruis", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Bruising<br>
							<input type="checkbox" name="pg1_phy2Petec"
								<%= props.getProperty("pg1_phy2Petec", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Petechiae</td>
							<td width="20%"><input type="checkbox" name="pg1_phy2Mec"
								<%= props.getProperty("pg1_phy2Mec", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Mec. Stain<br>
							<input type="checkbox" name="pg1_phy2Peel"
								<%= props.getProperty("pg1_phy2Peel", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Peeling<br>
							<input type="checkbox" name="pg1_phy2Jaun"
								<%= props.getProperty("pg1_phy2Jaun", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Jaundice</td>
							<td><textarea name="pg1_phy2Comm" style="width: 100%"
								cols="30" rows="2" @oscar.formDB dbType="varchar(255)"> <%= props.getProperty("pg1_phy2Comm", "") %> </textarea>
							</td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td>

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td width="15%">3. HEAD</td>
							<td width="10%"><input type="checkbox" name="pg1_phy3Normal"
								<%= props.getProperty("pg1_phy3Normal", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td width="20%"><input type="checkbox"
								name="pg1_phy3Abnormal"
								<%= props.getProperty("pg1_phy3Abnormal", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td colspan="2"><textarea name="pg1_phy3Comm"
								style="width: 100%" cols="30" rows="1" @oscar.formDB
								dbType="varchar(255)"> <%= props.getProperty("pg1_phy3Comm", "") %> </textarea>
							</td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td>

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td width="15%">4. EENT</td>
							<td width="10%"><input type="checkbox" name="pg1_phy4Eent"
								<%= props.getProperty("pg1_phy4Eent", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td width="20%"><input type="checkbox" name="pg1_phy4Cleft"
								<%= props.getProperty("pg1_phy4Cleft", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Cleft Lip/Palate<br>
							<input type="checkbox" name="pg1_phy4Micro"
								<%= props.getProperty("pg1_phy4Micro", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Micrognathia</td>
							<td width="20%"><input type="checkbox" name="pg1_phy4Susp"
								<%= props.getProperty("pg1_phy4Susp", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Suspected Choanal atresia</td>
							<td><textarea name="pg1_phy4Comm" style="width: 100%"
								cols="30" rows="2" @oscar.formDB dbType="varchar(255)"> <%= props.getProperty("pg1_phy4Comm", "") %> </textarea>
							</td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td>

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td width="15%">5. RESP.</td>
							<td width="10%"><input type="checkbox" name="pg1_phy5Normal"
								<%= props.getProperty("pg1_phy5Normal", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td width="20%"><input type="checkbox" name="pg1_phy5Grunt"
								<%= props.getProperty("pg1_phy5Grunt", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Grunting<br>
							<input type="checkbox" name="pg1_phy5Nasal"
								<%= props.getProperty("pg1_phy5Nasal", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Nasal Flaring<br>
							<input type="checkbox" name="pg1_phy5Retract"
								<%= props.getProperty("pg1_phy5Retract", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Retracting</td>
							<td width="22%"><input type="checkbox"
								name="pg1_phy5Shallow"
								<%= props.getProperty("pg1_phy5Shallow", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Shallow breathing<br>
							<input type="checkbox" name="pg1_phy5Tachy"
								<%= props.getProperty("pg1_phy5Tachy", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Tachypnea</td>
							<td><textarea name="pg1_phy5Comm" style="width: 100%"
								cols="30" rows="2" @oscar.formDB dbType="varchar(255)"> <%= props.getProperty("pg1_phy5Comm", "") %> </textarea>
							</td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td>

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td width="15%">6. CVS</td>
							<td width="10%"><input type="checkbox" name="pg1_phy6Normal"
								<%= props.getProperty("pg1_phy6Normal", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td width="20%"><input type="checkbox" name="pg1_phy6Murmur"
								<%= props.getProperty("pg1_phy6Murmur", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Murmur<br>
							<input type="checkbox" name="pg1_phy6Central"
								<%= props.getProperty("pg1_phy6Central", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Central Cyanosis</td>
							<td width="30%"><input type="checkbox" name="pg1_phy6Abn"
								<%= props.getProperty("pg1_phy6Abn", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Abn./ Delayed femoral pulses<br>
							<input type="checkbox" name="pg1_phy6AbnRate"
								<%= props.getProperty("pg1_phy6AbnRate", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Abnormal rate/rhythm</td>
							<td><textarea name="pg1_phy6Comm" style="width: 100%"
								cols="30" rows="2" @oscar.formDB dbType="varchar(255)"> <%= props.getProperty("pg1_phy6Comm", "") %> </textarea>
							</td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td>

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td width="15%">7. ABDOMEN</td>
							<td width="10%"><input type="checkbox" name="pg1_phy7Normal"
								<%= props.getProperty("pg1_phy7Normal", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td width="20%"><input type="checkbox" name="pg1_phy7Scaph"
								<%= props.getProperty("pg1_phy7Scaph", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Scaphoid<br>
							<input type="checkbox" name="pg1_phy7Distention"
								<%= props.getProperty("pg1_phy7Distention", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Distention<br>
							<input type="checkbox" name="pg1_phy7Hepato"
								<%= props.getProperty("pg1_phy7Hepato", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Hepatomegaly</td>
							<td width="20%"><input type="checkbox" name="pg1_phy7Spleno"
								<%= props.getProperty("pg1_phy7Spleno", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Splenomegaly<br>
							<input type="checkbox" name="pg1_phy7Abn"
								<%= props.getProperty("pg1_phy7Abn", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Abnormal mass</td>
							<td><textarea name="pg1_phy7Comm" style="width: 100%"
								cols="30" rows="2" @oscar.formDB dbType="varchar(255)"> <%= props.getProperty("pg1_phy7Comm", "") %> </textarea>
							</td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td>

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td width="15%">8. UMBILICAL CORD</td>
							<td width="10%"><input type="checkbox"
								name="pg1_phy8Umbilical"
								<%= props.getProperty("pg1_phy8Umbilical", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td width="20%"><input type="checkbox" name="pg1_phy8Mec"
								<%= props.getProperty("pg1_phy8Mec", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Mec. Stain<br>
							<input type="checkbox" name="pg1_phy8Vess"
								<%= props.getProperty("pg1_phy8Vess", "") %> @oscar.formDB
								dbType="tinyint(1)" /> 2 Vessels</td>
							<td width="20%"><input type="checkbox" name="pg1_phy8Thin"
								<%= props.getProperty("pg1_phy8Thin", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Thin</td>
							<td><textarea name="pg1_phy8Comm" style="width: 100%"
								cols="30" rows="2" @oscar.formDB dbType="varchar(255)"> <%= props.getProperty("pg1_phy8Comm", "") %> </textarea>
							</td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td>

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td width="15%">9. GENITO-<br>
							RECTAL</td>
							<td width="10%"><input type="checkbox" name="pg1_phy9Genito"
								<%= props.getProperty("pg1_phy9Genito", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td width="20%"><input type="checkbox" name="pg1_phy9Hypos"
								<%= props.getProperty("pg1_phy9Hypos", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Hypospadias<br>
							<input type="checkbox" name="pg1_phy9Imper"
								<%= props.getProperty("pg1_phy9Imper", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Imperforate anus</td>
							<td width="24%"><input type="checkbox"
								name="pg1_phy9UndesTest"
								<%= props.getProperty("pg1_phy9UndesTest", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Undescended testes</td>
							<td><textarea name="pg1_phy9Comm" style="width: 100%"
								cols="30" rows="2" @oscar.formDB dbType="varchar(255)"> <%= props.getProperty("pg1_phy9Comm", "") %> </textarea>
							</td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td>

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td width="15%">10. MUSCULO-<br>
							SKELETAL</td>
							<td width="10%"><input type="checkbox" name="pg1_phy10Muscu"
								<%= props.getProperty("pg1_phy10Muscu", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td width="20%"><input type="checkbox" name="pg1_phy10Spine"
								<%= props.getProperty("pg1_phy10Spine", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Spine<br>
							<input type="checkbox" name="pg1_phy10Hip"
								<%= props.getProperty("pg1_phy10Hip", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Hip abnormality</td>
							<td width="24%"><input type="checkbox" name="pg1_phy10Extre"
								<%= props.getProperty("pg1_phy10Extre", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Extremity abnormality</td>
							<td><textarea name="pg1_phy10Comm" style="width: 100%"
								cols="30" rows="2" @oscar.formDB dbType="varchar(255)"> <%= props.getProperty("pg1_phy10Comm", "") %> </textarea>
							</td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td>

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td width="15%">11. NEURO-<br>
							LOGICAL</td>
							<td width="10%"><input type="checkbox" name="pg1_phy11Neuro"
								<%= props.getProperty("pg1_phy11Neuro", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td width="20%"><input type="checkbox" name="pg1_phy11Hypo"
								<%= props.getProperty("pg1_phy11Hypo", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Hypotonia<br>
							<input type="checkbox" name="pg1_phy11Cry"
								<%= props.getProperty("pg1_phy11Cry", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Cry</td>
							<td width="20%"><input type="checkbox"
								name="pg1_phy11Jittery"
								<%= props.getProperty("pg1_phy11Jittery", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Jittery<br>
							<input type="checkbox" name="pg1_phy11Reflexes"
								<%= props.getProperty("pg1_phy11Reflexes", "") %> @oscar.formDB
								dbType="tinyint(1)" /> Reflexes</td>
							<td><textarea name="pg1_phy11Comm" style="width: 100%"
								cols="30" rows="2" @oscar.formDB dbType="varchar(255)"> <%= props.getProperty("pg1_phy11Comm", "") %> </textarea>
							</td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td>

					<table class="small8" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td width="15%">12. OTHER</td>
							<td colspan="4"><textarea name="pg1_phy12Other"
								style="width: 100%" cols="30" rows="2" @oscar.formDB
								dbType="varchar(255)"> <%= props.getProperty("pg1_phy12Other", "") %> </textarea>
							</td>
						</tr>
					</table>

					</td>

				</tr>
			</table>


			</td>
		</tr>
	</table>



	<table class="small8" width="100%" border="1" cellspacing="0"
		cellpadding="0">
		<tr>
			<td width="40%">HLTH 1583A Rev. 02/03</td>
			<td>

			<table class="small9" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td width="20%">DATE <img src="../images/cal.gif"
						id="pg1_Date_cal"> <br>
					<input type="text" name="pg1_Date" id="pg1_Date" size="10"
						maxlength="10" value="<%= props.getProperty("pg1_Date", "") %>"
						@oscar.formDB dbType="date" /></td>
					<td width="10%">TIME<br>
					<input type="text" name="pg1_Time" size="5" maxlength="5"
						value="<%= props.getProperty("pg1_Time", "") %>" @oscar.formDB
						dbType="time" /></td>
					<td>SIGNATURE<br>
					<input type="text" name="pg1_Signature" size="50" maxlength="80"
						value="<%= props.getProperty("pg1_Signature", "") %>"
						@oscar.formDB /> MD/RM</td>
				</tr>
			</table>

			</td>
		</tr>
	</table>



	<table class="Head" class="hidePrint">
		<tr>
			<td align="left">
			<%
  if (!bView) {
%> <input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <%
  }
%> <input type="submit" value="Exit"
				onclick="javascript:return onExit();" /> <input type="submit"
				value="Print" onclick="javascript:return onPrint();" /></td>
			<%
  if (!bView) {
%>

			<td align="right"><!--  b>View:</b>
            <a href="javascript: popupPage('formbcnewbornpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">Part2 <font size=-2>(pg.1)</font></a> |
            <a href="javascript: popupPage('formbcnewbornpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">Part2 <font size=-2>(pg.2)</font></a>
            &nbsp;--></td>
			<td align="right"><b>Edit:</b>Part1 | <a
				href="formbcnewbornpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Part2
			<font size=-2>(pg.1)</font></a> | <a
				href="formbcnewbornpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Part2
			<font size=-2>(pg.2)</font></a> |</td>
			<%
  }
%>
		</tr>
	</table>

	<br>
	<br>
	<br>
	<br>

</html:form>
<script type="text/javascript">
Calendar.setup({ inputField : "pg1_eddByDate", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_eddByDate_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_formDate", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_formDate_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_delRomBirthDay", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_delRomBirthDay_cal", singleClick : true, step : 1 });

Calendar.setup({ inputField : "pg1_Date", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_Date_cal", singleClick : true, step : 1 });
</script>
</body>
</html:html>
