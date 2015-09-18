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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%
	String formClass = "ImmunAllergy";
	String formLink = "formimmunallergy.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

    FrmData fd = new FrmData();
    String resource = fd.getResource();
    resource = resource + "ob/riskinfo/";
    //props.setProperty("c_lastVisited", "pg1");

	//get project_home
	String project_home = getServletContext().getRealPath("/") ;
	String sep = project_home.substring(project_home.length()-1);
	project_home = project_home.substring(0, project_home.length()-1) ;
	project_home = project_home.substring(project_home.lastIndexOf(sep)+1) ;
%>
<%
  boolean bView = false;
  if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Immunizations/Allergies</title>
<link rel="stylesheet" type="text/css" href="arStyle.css">
<html:base />
</head>

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


    function reset() {
        document.forms[0].target = "apptProviderSearch";
        document.forms[0].action = "/<%=project_home%>/form/formname.do" ;
	}
    function onPrint() {
        document.forms[0].submit.value="print"; //printAR1
        var ret = checkAllDates();
        if(ret==true)
        {
            //ret = confirm("Do you wish to save this form and view the print preview?");
            popupFixedPage(650,850,'../provider/notice.htm');
            document.forms[0].action = "../form/formbcbirthsummoprint.jsp";
            document.forms[0].target="planner";
            document.forms[0].submit();
            document.forms[0].target="apptProviderSearch";
        }
        return ret;
    }
    function onSave() {
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
        if(ret==true)
        {
            reset();
            ret = confirm("Are you sure you want to save this form?");
        }
        return ret;
    }
    
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        var ret = checkAllDates();
        if(ret == true)
        {
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

    function valDate(dateBox)
    {
        try
        {
            var dateString = dateBox.value;
            if(dateString == "")
            {
				//alert('dateString'+dateString);
                return true;
            }
            var dt = dateString.split('/');
            //var y = dt[2];  var m = dt[1];  var d = dt[0];
            var y = dt[0];  var m = dt[1];  var d = dt[2];
            var orderString = m + '/' + d + '/' + y;
            var pass = isDate(orderString);

            if(pass!=true)
            {
                alert('Invalid '+pass+' in field ' + dateBox.name);
                dateBox.focus();
                return false;
            }
        }
        catch (ex)
        {
            alert('Catch Invalid Date in field ' + dateBox.name);
            dateBox.focus();
            return false;
        }
        return true;
    }

    function checkAllDates() {
        var b = true;
        if(valDate(document.forms[0].dateAdmin)==false){
            b = false;
        } else if(valDate(document.forms[0].expiDate)==false){
            b = false;
		}

        return b;
    }
</script>

<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1"
	onLoad="setfocus()">
<!--
@oscar.formDB Table="formImmunAllergy" 
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
	<!--input type="hidden" name="provider_no" value=<%=request.getParameter("provNo")%> /-->
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
				value="Print" onclick="javascript:window.print();" /></td>
		</tr>
	</table>

	<table width="100%" border="0" cellspacing="3" cellpadding="0">
		<tr>
			<th><%=bView?"<font color='yellow'>VIEW PAGE: </font>" : ""%>
			Immunizations / Allergies (Patient: <%= props.getProperty("c_surname", "") %>,
			<%= props.getProperty("c_givenName", "") %> )</th>
			<input type="hidden" name="c_surname" size="30"
				value="<%= props.getProperty("c_surname", "") %>" maxlength="30"
				@oscar.formDB />
			<input type="hidden" name="c_givenName" size="30"
				value="<%= props.getProperty("c_givenName", "") %>" maxlength="30"
				@oscar.formDB />
		</tr>
	</table>



	<center>


	<table width="80%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td align="right" width="50%">*Date of Administration :</td>
			<td><input type="text" name="dateAdmin" size="10" maxlength="10"
				value="<%= props.getProperty("dateAdmin", "") %>" @oscar.formDB
				dbType="date" /></td>
		</tr>
		<tr>
			<td align="right">Trade name :</td>
			<td><input type="text" name="tradeName" size="30" maxlength="50"
				value="<%= props.getProperty("tradeName", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td align="right">Manufacturer :</td>
			<td><input type="text" name="manufacturer" size="30"
				maxlength="50" value="<%= props.getProperty("manufacturer", "") %>"
				@oscar.formDB /></td>
		</tr>
		<tr>
			<td align="right">Lot # :</td>
			<td><input type="text" name="lot" size="30" maxlength="50"
				value="<%= props.getProperty("lot", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td align="right">Exip. date :</td>
			<td><input type="text" name="expiDate" size="10" maxlength="10"
				value="<%= props.getProperty("expiDate", "") %>" @oscar.formDB
				dbType="date" /></td>
		</tr>
	</table>

	<table width="80%" border="1" cellspacing="0" cellpadding="2">

		<tr>
			<td align="right" valign="top" width="50%">Dose /administration
			:</td>
			<td><input type="checkbox" name="doseAdminSC"
				<%= props.getProperty("doseAdminSC", "") %> @oscar.formDB
				dbType="tinyint(1)" /> SC<br>
			<input type="checkbox" name="doseAdminIM"
				<%= props.getProperty("doseAdminIM", "") %> @oscar.formDB
				dbType="tinyint(1)" /> IM<br>
			<input type="checkbox" name="doseAdminml"
				<%= props.getProperty("doseAdminml", "") %> @oscar.formDB
				dbType="tinyint(1)" /> ml <input type="text" name="doseAdminTxtml"
				size="10" maxlength="10"
				value="<%= props.getProperty("doseAdminTxtml", "") %>" @oscar.formDB />

			</td>
		</tr>
		<tr>
			<td align="right" valign="top">Location :</td>
			<td><input type="checkbox" name="locLtDel"
				<%= props.getProperty("locLtDel", "") %> @oscar.formDB
				dbType="tinyint(1)" /> Lt. deltoid<br>
			<input type="checkbox" name="locRtDel"
				<%= props.getProperty("locRtDel", "") %> @oscar.formDB
				dbType="tinyint(1)" /> Rt. deltoid<br>
			<input type="checkbox" name="locLtDelOUQ"
				<%= props.getProperty("locLtDelOUQ", "") %> @oscar.formDB
				dbType="tinyint(1)" /> Lt gluteal (OUQ)<br>
			<input type="checkbox" name="locRtDelOUQ"
				<%= props.getProperty("locRtDelOUQ", "") %> @oscar.formDB
				dbType="tinyint(1)" /> Rt.gluteal (OUQ)<br>
			<input type="checkbox" name="locOther"
				<%= props.getProperty("locOther", "") %> @oscar.formDB
				dbType="tinyint(1)" /> other ( anterolateral thigh)</td>
		</tr>
		<tr>
			<td align="right" valign="top">Instructions :</td>
			<td><input type="checkbox" name="InstrStay20"
				<%= props.getProperty("InstrStay20", "") %> @oscar.formDB
				dbType="tinyint(1)" /> stay in waiting area for 20 minutes<br>
			<input type="checkbox" name="InstrExpectLoc"
				<%= props.getProperty("InstrExpectLoc", "") %> @oscar.formDB
				dbType="tinyint(1)" /> expect local tenderness/small lump <br>
			<input type="checkbox" name="InstrFU"
				<%= props.getProperty("InstrFU", "") %> @oscar.formDB
				dbType="tinyint(1)" /> f/u if rash, fever, Sz, leth or new Sxs</td>
		</tr>
		<tr>
			<td align="right" valign="top">At discharge :</td>
			<td><input type="checkbox" name="disChNoComp"
				<%= props.getProperty("disChNoComp", "") %> @oscar.formDB
				dbType="tinyint(1)" /> no complications</td>
		</tr>
	</table>

	* Date format: yyyy/mm/dd</center>

</html:form>
</body>
</html:html>
