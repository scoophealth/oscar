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

<%@ page import=" oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<% java.util.Properties oscarVariables = oscar.OscarProperties.getInstance(); %>

<%
	String formClass = "BCAR";
	String formLink = "formbcarpg1.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

    FrmData fd = new FrmData();
    String resource = fd.getResource();
    resource = resource + "ob/riskinfo/";
    props.setProperty("c_lastVisited", "pg1");

	//get project_home
	String project_home = request.getContextPath().substring(1);
	//sync
	boolean bSync = false;
	if(!props.getProperty("c_surname_cur", "").equals("") && !(props.getProperty("c_surname_cur", "").equals(props.getProperty("c_surname", "")) 
	        && props.getProperty("c_givenName_cur", "").equals(props.getProperty("c_givenName", ""))
	        && props.getProperty("c_address_cur", "").equals(props.getProperty("c_address", ""))
	        && props.getProperty("c_city_cur", "").equals(props.getProperty("c_city", ""))
	        && props.getProperty("c_province_cur", "").equals(props.getProperty("c_province", ""))
	        && props.getProperty("c_postal_cur", "").equals(props.getProperty("c_postal", ""))
	        && props.getProperty("c_phn_cur", "").equals(props.getProperty("c_phn", ""))
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
<title>Antenatal Record 1</title>
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
<style type="text/css">
<!--
.demo  {color:#000033; background-color:#cccccc; layer-background-color:#cccccc;
        position:absolute; top:150px; left:270px; width:120px; height:230px;
        z-index:99;  visibility:hidden;}
.demo1  {color:#000033; background-color:silver; layer-background-color:#cccccc;
        position:absolute; top:40px; left:370px; width:190px; height:80px;
        z-index:99;  visibility:hidden;}
.demo2  {color:#000033; background-color:silver; layer-background-color:#cccccc;
        position:absolute; top:40px; left:320px; width:150px; height:220px;
        z-index:99;  visibility:hidden;}
.demo3  {color:#000033; background-color:silver; layer-background-color:#cccccc;
        position:absolute; top:460px; left:430px; width:180px; height:160px;
        z-index:99;  visibility:hidden;}
-->
</style>
</head>
<script type="text/javascript">
<!--
function showHideBox(layerName, iState) { // 1 visible, 0 hidden
    if(document.layers)	   //NN4+
    {
       document.layers[layerName].visibility = iState ? "show" : "hide";
    } else if(document.getElementById)	  //gecko(NN6) + IE 5+
    {
        var obj = document.getElementById(layerName);
        obj.style.visibility = iState ? "visible" : "hidden";
    } else if(document.all)	// IE 4
    {
        document.all[layerName].style.visibility = iState ? "visible" : "hidden";
    }
}
function insertBox(str, field, layerName) { // 1 visible, 0 hidden
    if(document.getElementById)	{
        var obj = document.getElementById(field);
        obj.value = str;
    }
    showHideBox(layerName, 0);
}
function showDeliBox(layerName, iState, field, e) { // 1 visible, 0 hidden
    fieldObj = field;

    if(document.layers)	{   //NN4+
       document.layers[layerName].visibility = iState ? "show" : "hide";
    } else if(document.getElementById) {	  //gecko(NN6) + IE 5+
        var obj = document.getElementById(layerName);
        obj.style.visibility = iState ? "visible" : "hidden";
    } else if(document.all)	{// IE 4
        document.all[layerName].style.visibility = iState ? "visible" : "hidden";
    }
    fieldObj = field;
}
function insertBox1(str, layerName) { // 1 visible, 0 hidden
    if(document.getElementById)	{
        //var obj = document.getElementById(field);
        fieldObj.value = str;
    }
    showHideBox(layerName, 0);
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
    document.forms[0].c_phn.value = "<%=props.getProperty("c_phn_cur", "")%>";
    document.forms[0].c_phone.value = "<%=props.getProperty("c_phone_cur", "")%>";
}

// -->
</script>

<script type="text/javascript" language="Javascript">

    function reset() {
        document.forms[0].target = "";
        document.forms[0].action = "/<%=project_home%>/form/formname.do" ;
	}
    function onPrint() {
        document.forms[0].submit.value="print"; 
        var ret = checkAllDates();
        if(ret==true)
        {
            document.forms[0].action = "../form/createpdf?__title=British+Columbia+Antenatal+Record+Part+1&__cfgfile=bcar1PrintCfgPg1&__template=bcar1";
            document.forms[0].target="_blank";            
        }
        return ret;
    }
    function onPrintRisk() {
        document.forms[0].submit.value="print"; 
        var ret = checkAllDates();
        if(ret==true)
        {
            document.forms[0].action = "../form/createpdf?__title=British+Columbia+Antenatal+Record+Part+2&__cfgfile=bcar1PrintCfgPg2&__template=bcar22";
            document.forms[0].target="_blank";            
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
        if (month<0 || month>12){
            return "month"
        }
        if (day<0 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
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
            var y = dt[2];  var m = dt[1];  var d = dt[0];
            //var y = dt[0];  var m = dt[1];  var d = dt[2];
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
        if(valDate(document.forms[0].pg1_eddByDate)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_eddByUs)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_dateOfBirth)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_formDate)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_signDate)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_lmp)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_quitDate)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_examination)==false){
            b = false;
        }

        return b;
    }
    
function calByLMP() {
	if (document.forms[0].pg1_lmp.value!="" && valDate(document.forms[0].pg1_lmp)==true) {
		var str_date = document.forms[0].pg1_lmp.value;
        var dd = str_date.substring(0, str_date.indexOf("/"));
        var mm = eval(str_date.substring(eval(str_date.indexOf("/")+1), str_date.lastIndexOf("/")) - 1);
        var yyyy  = str_date.substring(eval(str_date.lastIndexOf("/")+1));
		var calDate=new Date();
		calDate.setFullYear(yyyy, mm, dd);
		calDate.setHours("8");
		var odate = new Date(calDate.getTime() + (280 * 86400000));
		document.forms[0].pg1_eddByDate.value = odate.getDate() + '/' + (odate.getMonth()+1) + '/' + odate.getFullYear();
	}
}
</script>

<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1">
<div ID="Langdiv" class="demo">
<table bgcolor='silver' width='100%'>
	<tr>
		<td align='right'><a
			href="javascript: function myFunction() {return false; }"
			onclick="showHideBox('Langdiv',0); return false;">X</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('English','pg1_langPref', 'Langdiv'); return false;">English</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('French','pg1_langPref', 'Langdiv'); return false;">French</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Mandarin','pg1_langPref', 'Langdiv'); return false;">Mandarin</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Cantonese','pg1_langPref', 'Langdiv'); return false;">Cantonese</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Hindu','pg1_langPref', 'Langdiv'); return false;">Hindu</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Punjabi','pg1_langPref', 'Langdiv'); return false;">Punjabi</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Pushtu','pg1_langPref', 'Langdiv'); return false;">Pushtu</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Spanish','pg1_langPref', 'Langdiv'); return false;">Spanish</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Tagalog','pg1_langPref', 'Langdiv'); return false;">Tagalog</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Urdu','pg1_langPref', 'Langdiv'); return false;">Urdu</a></td>
	</tr>
</table>
</div>
<div ID="Origdiv" class="demo2">
<table bgcolor='silver' width='100%'>
	<tr>
		<td align='right'><a
			href="javascript: function myFunction() {return false; }"
			onclick="showHideBox('Origdiv',0); return false;">X</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Caucasian','pg1_ethOrig', 'Origdiv'); return false;">Caucasian</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Black','pg1_ethOrig', 'Origdiv'); return false;">Black</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('South Asian','pg1_ethOrig', 'Origdiv'); return false;">South
		Asian</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Pakistani','pg1_ethOrig', 'Origdiv'); return false;">Pakistani</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Sri Lankan','pg1_ethOrig', 'Origdiv'); return false;">Sri
		Lankan</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Japanese','pg1_ethOrig', 'Origdiv'); return false;">Japanese</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Filipino','pg1_ethOrig', 'Origdiv'); return false;">Filipino</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Chinese','pg1_ethOrig', 'Origdiv'); return false;">Chinese</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Vietnamese','pg1_ethOrig', 'Origdiv'); return false;">Vietnamese</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Hispanic','pg1_ethOrig', 'Origdiv'); return false;">Hispanic</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('First Nations','pg1_ethOrig', 'Origdiv'); return false;">First
		Nations</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Jewish','pg1_ethOrig', 'Origdiv'); return false;">Jewish</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Other','pg1_ethOrig', 'Origdiv'); return false;">Other</a></td>
	</tr>
</table>
</div>
<div ID="Origdiv1" class="demo2">
<table bgcolor='silver' width='100%'>
	<tr>
		<td align='right'><a
			href="javascript: function myFunction() {return false; }"
			onclick="showHideBox('Origdiv1',0); return false;">X</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Caucasian','pg1_faEthOrig', 'Origdiv1'); return false;">Caucasian</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Black','pg1_faEthOrig', 'Origdiv1'); return false;">Black</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('South Asian','pg1_faEthOrig', 'Origdiv1'); return false;">South
		Asian</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Pakistani','pg1_faEthOrig', 'Origdiv1'); return false;">Pakistani</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Sri Lankan','pg1_faEthOrig', 'Origdiv1'); return false;">Sri
		Lankan</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Japanese','pg1_faEthOrig', 'Origdiv1'); return false;">Japanese</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Filipino','pg1_faEthOrig', 'Origdiv1'); return false;">Filipino</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Chinese','pg1_faEthOrig', 'Origdiv1'); return false;">Chinese</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Vietnamese','pg1_faEthOrig', 'Origdiv1'); return false;">Vietnamese</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Hispanic','pg1_faEthOrig', 'Origdiv1'); return false;">Hispanic</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('First Nations','pg1_faEthOrig', 'Origdiv1'); return false;">First
		Nations</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Jewish','pg1_faEthOrig', 'Origdiv1'); return false;">Jewish</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox('Other','pg1_faEthOrig', 'Origdiv1'); return false;">Other</a></td>
	</tr>
</table>
</div>
<div ID="Instrdiv" class="demo1">
<center>
<table bgcolor='#007FFF' width='99%'>
	<tr>
		<th align='right'><a
			href="javascript: function myFunction() {return false; }"
			onclick="showHideBox('Instrdiv',0); return false;"><font
			color="red">X</font></a></th>
	</tr>
	<tr>
		<th><a href="javascript: function myFunction() {return false; }"
			onclick="showHideBox('Instrdiv',0); return false;"><font
			color="#66FF66">Double click shaded fields for drop down or
		calculation.</font><br>
		&nbsp;</a></th>
	</tr>
</table>
</center>
</div>
<div ID="Delidiv" class="demo3">
<center>
<table bgcolor='silver' width='99%'>
	<tr>
		<td align='right'><a
			href="javascript: function myFunction() {return false; }"
			onclick="showHideBox('Delidiv',0); return false;">X</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox1('SVD','Delidiv'); return false;">SVD</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox1('C-section','Delidiv'); return false;">C-section</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox1('Vacuum','Delidiv'); return false;">Vacuum</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox1('Forceps','Delidiv'); return false;">Forceps</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox1('Vacuum and Forceps','Delidiv'); return false;">Vacuum
		and Forceps</a></td>
	</tr>
	<tr>
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="insertBox1('Forceps Trial and C-section','Delidiv'); return false;">Forceps
		Trial and C-section</a></td>
	</tr>
</table>
</center>
</div>

<!--
@oscar.formDB Table="formBCAR" 
@oscar.formDB Field="ID" Type="int(10)" Null="NOT NULL" Key="PRI" Default="" Extra="auto_increment"
@oscar.formDB Field="demographic_no" Type="int(10)" Null="NOT NULL" Default="'0'" 
@oscar.formDB Field="provider_no" Type="int(10)" Null="" Default="NULL" 
@oscar.formDB Field="formCreated" Type="date" Null="" Default="NULL" 
@oscar.formDB Field="formEdited" Type="timestamp"  
@oscar.formDB Field="c_lastVisited" Type="char(3)" 
-->
<html:form action="/form/formname">
	<input type="hidden" name="commonField" value="ar2_" />
	<input type="hidden" name="c_lastVisited" value="pg1" />
	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="formId" value="<%=formId%>" />
	<input type="hidden" name="provider_no"
		value=<%=request.getParameter("provNo")%> />
	<input type="hidden" name="provNo"
		value="<%= request.getParameter("provNo") %>" />
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
				value="Print" onclick="javascript:return onPrint();" /> <input
				type="submit" value="Print Risk"
				onclick="javascript:return onPrintRisk();" /></td>
			<%
  if (!bView) {
%>
			<td><a href="javascript: function myFunction() {return false; }"
				title="Double click shaded fields for drop down or calculation"
				onClick="showHideBox('Instrdiv',1);return false;"><font
				color='red'>Instruction</font></a></td>

			<td align="right"><!-- font size=-2><b>View:</b> </font>
            <a href="javascript: popupPage('formbcarpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');"><font size=-2>AR2 (pg.1)</font></a> |
            <a href="javascript: popupPage('formbcarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');"><font size=-2>AR2 (pg.2)</font></a>
            &nbsp;</font> --></td>
			<td align="right"><b>Edit:</b>AR1 | <a
				href="formbcarpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.1)</font></a> | <a
				href="formbcarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.2)</font></a></td>
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
					British Columbia Antenatal Record Part 1 <font size="-2">HLTH-1582-1
					Rev.02/03</font></th>
				</tr>
			</table>

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td width="30%"><b>1.</b> HOSPITAL<br>
					<input type="text" name="c_hospital"
						<%=oscarVariables.getProperty("BCAR_hospital")==null? " ": ("class=\"spe\" onDblClick='showDef(\""+oscarVariables.getProperty("BCAR_hospital")+"\", this);'") %>
						style="width: 100%" size="30" maxlength="60"
						value="<%= props.getProperty("c_hospital", "") %>" @oscar.formDB />
					</td>
					<td width="33%"><a
						href="javascript: function myFunction() {return false; }"
						onClick="popupFixedPage(600, 300, 'formbcarpg1namepopup.jsp'); return false;">PRIMARY
					CARE GIVER</a><br>
					<input type="text" name="pg1_priCare" style="width: 100%" size="30"
						maxlength="60" value="<%= props.getProperty("pg1_priCare", "") %>"
						@oscar.formDB /></td>
					<td colspan="2">FAMILY PHYSICIAN<br>
					<input type="text" name="pg1_famPhy" style="width: 100%" size="30"
						maxlength="60" value="<%= props.getProperty("pg1_famPhy", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td colspan='2'>MOTHER'S NAME <input type="text"
						name="pg1_moName" style="width: 100%" size="60" maxlength="60"
						value="<%= props.getProperty("pg1_moName", (props.getProperty("c_surname", "")+", "+props.getProperty("c_givenName", "")) ) %>"
						@oscar.formDB /></td>
					<td width="20%">DATE OF BIRTH<br>
					dd/mm/yyyy <input type="text" name="pg1_dateOfBirth"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_dateOfBirth", "") %>"
						@oscar.formDB dbType="date" /></td>
					<td>AGE AT EDD<br>
					<input type="text" name="pg1_ageAtEDD" style="width: 100%" size="2"
						maxlength="2" value="<%= props.getProperty("pg1_ageAtEDD", "") %>"
						@oscar.formDB /></td>
				</tr>
			</table>

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td width="40%">MOTHER'S MAIDEN NAME<br>
					<input type="text" name="pg1_maidenName" style="width: 100%"
						size="30" maxlength="60"
						value="<%= props.getProperty("pg1_maidenName", "") %>"
						@oscar.formDB /></td>
					<td width="33%">ETHNIC ORIGIN<br>
					<input type="text" name="pg1_ethOrig" id="pg1_ethOrig" class="spe"
						onDblClick="showHideBox('Origdiv',1);" style="width: 100%"
						size="30" maxlength="60"
						value="<%= props.getProperty("pg1_ethOrig", "") %>" @oscar.formDB />
					</td>

					<td><span class="small9">LANGUAGE PREFERRED</span><br>
					<input type="text" name="pg1_langPref" id="pg1_langPref"
						class="spe" onDblClick="showHideBox('Langdiv',1);"
						style="width: 100%" size="30" maxlength="60"
						value="<%= props.getProperty("pg1_langPref", "") %>" @oscar.formDB />
					</td>
				</tr>
			</table>

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td width="50%">PARTNER'S NAME<br>
					<input type="text" name="pg1_partnerName" style="width: 100%"
						size="30" maxlength="60"
						value="<%= props.getProperty("pg1_partnerName", "") %>"
						@oscar.formDB /></td>
					<td width="5%">AGE <input type="text" name="pg1_partnerAge"
						style="width: 100%" size="2" maxlength="2"
						value="<%= props.getProperty("pg1_partnerAge", "") %>"
						@oscar.formDB /></td>
					<td><span class="small9">ETHNIC ORIGIN OF NEWBORN�S
					FATHER</span> <input type="text" name="pg1_faEthOrig" id="pg1_faEthOrig"
						class="spe" onDblClick="showHideBox('Origdiv1',1);"
						style="width: 100%" size="30" maxlength="50"
						value="<%= props.getProperty("pg1_faEthOrig", "") %>"
						@oscar.formDB /></td>
				</tr>
			</table>

			</td>
			<td>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>DATE<br>
					<input type="text" name="pg1_formDate" style="width: 100%"
						size="10" maxlength="10"
						value="<%= props.getProperty("pg1_formDate", "") %>" @oscar.formDB
						dbType="date" /></td>
					<td align="right"><%=bSync? ("<b><a href=\"javascript: function myFunction() {return false; }\" onClick='syncDemo(); return false;'><font size='+1' color='red'>Synchronize</font></a></b>") :"" %></td>
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
					<td>PERSONAL HEALTH NUMBER<br>
					<input type="text" name="c_phn" style="width: 100%" size="20"
						maxlength="20" value="<%= props.getProperty("c_phn", "") %>"
						@oscar.formDB /></td>
					<td><span class="small9"><a
						href="javascript: function myFunction() {return false; }"
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

	<table class="shrinkMe" width="100%" border="0" cellspacing="0"
		cellpadding="0">
		<tr>
			<td colspan="3"><b>2. INFORMED CONSENT </b><span class="small8">(in
			compliance with the Freedom of Information and Protection of Privacy
			Act, Oct. 1993) . I understand that providing this information is
			necessary to assist the physician/midwife in planning my care
			throughout pregnancy, childbirth and postpartum; my personal
			information will be kept private. I also understand this information
			may be reviewed when necessary by other health professionals directly
			involved in my care. This information is collected in accordance with
			the provisions of the Freedom of Information and the Protection of
			Privacy Act by the Perinatal Database Registry, an integral part of
			the Ministry of Health supported and funded British Columbia
			Reproductive Care Program. I understand that I can ask my care
			provider if I have any questions regarding the collection and use of
			this information.</span></td>
		</tr>
		<tr>
			<td width="45%"><i>Mother's Signature:</i> <input type="text"
				name="pg1_moSign" size="40" maxlength="60"
				value="<%= props.getProperty("pg1_moSign", "") %>" @oscar.formDB />
			</td>
			<td width="40%"><i>Witness:</i> <input type="text"
				name="pg1_witness" size="40" maxlength="60"
				value="<%= props.getProperty("pg1_witness", "") %>" @oscar.formDB />
			</td>
			<td><i>Date:</i> <input type="text" name="pg1_signDate"
				size="10" maxlength="10"
				value="<%= props.getProperty("pg1_signDate", "") %>" @oscar.formDB
				dbType="date" /></td>
		</tr>
	</table>

	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="6"><b>3. OBSTETRICAL HISTORY INCLUDING
			ABORTIONS G</b><span class="small8">ravida</span> <input type="text"
				name="pg1_gravida" style="width: 30px;" size="2" maxlength="4"
				value="<%= props.getProperty("pg1_gravida", "") %>" @oscar.formDB />
			<b>T</b><span class="small8">erm</span> <input type="text"
				name="pg1_term" style="width: 30px;" size="2" maxlength="4"
				value="<%= props.getProperty("pg1_term", "") %>" @oscar.formDB /> <b>P</b><span
				class="small8">reterm</span> <input type="text" name="pg1_preterm"
				style="width: 30px;" size="2" maxlength="4"
				value="<%= props.getProperty("pg1_preterm", "") %>" @oscar.formDB />
			<b>A</b><span class="small8">bortion</span> <input type="text"
				name="pg1_abortion" style="width: 30px;" size="2" maxlength="3"
				value="<%= props.getProperty("pg1_abortion", "") %>" @oscar.formDB />
			<b>L</b><span class="small8">iving</span> <input type="text"
				name="pg1_living" style="width: 30px;" size="2" maxlength="3"
				value="<%= props.getProperty("pg1_living", "") %>" @oscar.formDB />
			</td>
			<td colspan="3" align="center"><b>CHILDREN</b></td>
		</tr>
		<tr>
			<th width="10%">DATE</th>
			<th width="15%">HOSPITAL OF BIRTH<br>
			OR ABORTION</th>
			<th width="8%">WEEKS AT<br>
			DELIVERY</th>
			<th width="6%">HRS.IN<br>
			ACTIVE<br>
			LABOUR</th>
			<th width="8%">DELIVERY<br>
			TYPE</th>

			<th width="33%">PERINATAL COMPLICATIONS</th>
			<th width="3%">SEX</th>
			<th width="6%">BIRTH<br>
			WEIGHT</th>
			<th width="8%">PRESENT<br>
			HEALTH</th>
		</tr>
		<tr>
			<td nowrap><input type="text" name="pg1_obHistDate1"
				id="pg1_obHistDate1" size="6" maxlength="10"
				value="<%= props.getProperty("pg1_obHistDate1", "") %>"
				@oscar.formDB /> <img src="../images/cal.gif"
				id="pg1_obHistDate1_cal"></td>
			<td><input type="text" name="pg1_birthOrAbort1"
				style="width: 100%" size="20" maxlength="20"
				value="<%= props.getProperty("pg1_birthOrAbort1", "") %>"
				@oscar.formDB /></td>
			<td><input type="text" name="pg1_deliWeek1" style="width: 100%"
				size="8" maxlength="8"
				value="<%= props.getProperty("pg1_deliWeek1", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_laboHr1" style="width: 100%"
				size="8" maxlength="8"
				value="<%= props.getProperty("pg1_laboHr1", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_deliType1" class="spe"
				onDblClick="showDeliBox('Delidiv',1, this, event);"
				style="width: 100%" size="10" maxlength="15"
				value="<%= props.getProperty("pg1_deliType1", "") %>" @oscar.formDB />
			</td>

			<td><input type="text" name="pg1_periComp1" style="width: 100%"
				size="50" maxlength="80"
				value="<%= props.getProperty("pg1_periComp1", "") %>" @oscar.formDB />
			</td>
			<td><!-- input type="text" name="pg1_obHistSex1" style="width:100%" size="1" maxlength="1" value="<%= props.getProperty("pg1_obHistSex1", "") %>" @oscar.formDB / -->
			<select name="pg1_obHistSex1">
				<option value=""></option>
				<option value="M"
					<%=props.getProperty("pg1_obHistSex1", "").equalsIgnoreCase("M")?"selected":""%>>M</option>
				<option value="F"
					<%=props.getProperty("pg1_obHistSex1", "").equalsIgnoreCase("F")?"selected":""%>>F</option>
			</select></td>
			<td><input type="text" name="pg1_birthWeit1" style="width: 100%"
				size="6" maxlength="8"
				value="<%= props.getProperty("pg1_birthWeit1", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_presHealth1" class="spe"
				onDblClick='showDef("A&W", this);' style="width: 100%" size="6"
				maxlength="8"
				value="<%= props.getProperty("pg1_presHealth1", "") %>"
				@oscar.formDB /></td>
		</tr>
		<tr>
			<td><input type="text" name="pg1_obHistDate2"
				id="pg1_obHistDate2" size="6" maxlength="10"
				value="<%= props.getProperty("pg1_obHistDate2", "") %>"
				@oscar.formDB /> <img src="../images/cal.gif"
				id="pg1_obHistDate2_cal"></td>
			<td><input type="text" name="pg1_birthOrAbort2"
				style="width: 100%" size="20" maxlength="20"
				value="<%= props.getProperty("pg1_birthOrAbort2", "") %>"
				@oscar.formDB /></td>
			<td><input type="text" name="pg1_deliWeek2" style="width: 100%"
				size="8" maxlength="8"
				value="<%= props.getProperty("pg1_deliWeek2", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_laboHr2" style="width: 100%"
				size="8" maxlength="8"
				value="<%= props.getProperty("pg1_laboHr2", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_deliType2" class="spe"
				onDblClick="showDeliBox('Delidiv',1, this, event);"
				style="width: 100%" size="10" maxlength="15"
				value="<%= props.getProperty("pg1_deliType2", "") %>" @oscar.formDB />
			</td>

			<td><input type="text" name="pg1_periComp2" style="width: 100%"
				size="50" maxlength="80"
				value="<%= props.getProperty("pg1_periComp2", "") %>" @oscar.formDB />
			</td>
			<td><!-- input type="text" name="pg1_obHistSex2" style="width:100%" size="1" maxlength="1" value="<%= props.getProperty("pg1_obHistSex2", "") %>" @oscar.formDB /-->
			<select name="pg1_obHistSex2">
				<option value=""></option>
				<option value="M"
					<%=props.getProperty("pg1_obHistSex2", "").equalsIgnoreCase("M")?"selected":""%>>M</option>
				<option value="F"
					<%=props.getProperty("pg1_obHistSex2", "").equalsIgnoreCase("F")?"selected":""%>>F</option>
			</select></td>
			<td><input type="text" name="pg1_birthWeit2" style="width: 100%"
				size="6" maxlength="8"
				value="<%= props.getProperty("pg1_birthWeit2", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_presHealth2" class="spe"
				onDblClick='showDef("A&W", this);' style="width: 100%" size="6"
				maxlength="8"
				value="<%= props.getProperty("pg1_presHealth2", "") %>"
				@oscar.formDB /></td>
		</tr>
		<tr>
			<td><input type="text" name="pg1_obHistDate3"
				id="pg1_obHistDate3" size="6" maxlength="10"
				value="<%= props.getProperty("pg1_obHistDate3", "") %>"
				@oscar.formDB /> <img src="../images/cal.gif"
				id="pg1_obHistDate3_cal"></td>
			<td><input type="text" name="pg1_birthOrAbort3"
				style="width: 100%" size="20" maxlength="20"
				value="<%= props.getProperty("pg1_birthOrAbort3", "") %>"
				@oscar.formDB /></td>
			<td><input type="text" name="pg1_deliWeek3" style="width: 100%"
				size="8" maxlength="8"
				value="<%= props.getProperty("pg1_deliWeek3", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_laboHr3" style="width: 100%"
				size="8" maxlength="8"
				value="<%= props.getProperty("pg1_laboHr3", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_deliType3" class="spe"
				onDblClick="showDeliBox('Delidiv',1, this, event);"
				style="width: 100%" size="10" maxlength="15"
				value="<%= props.getProperty("pg1_deliType3", "") %>" @oscar.formDB />
			</td>

			<td><input type="text" name="pg1_periComp3" style="width: 100%"
				size="50" maxlength="80"
				value="<%= props.getProperty("pg1_periComp3", "") %>" @oscar.formDB />
			</td>
			<td><select name="pg1_obHistSex3">
				<option value=""></option>
				<option value="M"
					<%=props.getProperty("pg1_obHistSex3", "").equalsIgnoreCase("M")?"selected":""%>>M</option>
				<option value="F"
					<%=props.getProperty("pg1_obHistSex3", "").equalsIgnoreCase("F")?"selected":""%>>F</option>
			</select></td>
			<td><input type="text" name="pg1_birthWeit3" style="width: 100%"
				size="6" maxlength="8"
				value="<%= props.getProperty("pg1_birthWeit3", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_presHealth3" class="spe"
				onDblClick='showDef("A&W", this);' style="width: 100%" size="6"
				maxlength="8"
				value="<%= props.getProperty("pg1_presHealth3", "") %>"
				@oscar.formDB /></td>
		</tr>
		<tr>
			<td><input type="text" name="pg1_obHistDate4"
				id="pg1_obHistDate4" size="6" maxlength="10"
				value="<%= props.getProperty("pg1_obHistDate4", "") %>"
				@oscar.formDB /> <img src="../images/cal.gif"
				id="pg1_obHistDate4_cal"></td>
			<td><input type="text" name="pg1_birthOrAbort4"
				style="width: 100%" size="20" maxlength="20"
				value="<%= props.getProperty("pg1_birthOrAbort4", "") %>"
				@oscar.formDB /></td>
			<td><input type="text" name="pg1_deliWeek4" style="width: 100%"
				size="8" maxlength="8"
				value="<%= props.getProperty("pg1_deliWeek4", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_laboHr4" style="width: 100%"
				size="8" maxlength="8"
				value="<%= props.getProperty("pg1_laboHr4", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_deliType4" class="spe"
				onDblClick="showDeliBox('Delidiv',1, this, event);"
				style="width: 100%" size="10" maxlength="15"
				value="<%= props.getProperty("pg1_deliType4", "") %>" @oscar.formDB />
			</td>

			<td><input type="text" name="pg1_periComp4" style="width: 100%"
				size="50" maxlength="80"
				value="<%= props.getProperty("pg1_periComp4", "") %>" @oscar.formDB />
			</td>
			<td><select name="pg1_obHistSex4">
				<option value=""></option>
				<option value="M"
					<%=props.getProperty("pg1_obHistSex4", "").equalsIgnoreCase("M")?"selected":""%>>M</option>
				<option value="F"
					<%=props.getProperty("pg1_obHistSex4", "").equalsIgnoreCase("F")?"selected":""%>>F</option>
			</select></td>
			<td><input type="text" name="pg1_birthWeit4" style="width: 100%"
				size="6" maxlength="8"
				value="<%= props.getProperty("pg1_birthWeit4", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_presHealth4" class="spe"
				onDblClick='showDef("A&W", this);' style="width: 100%" size="6"
				maxlength="8"
				value="<%= props.getProperty("pg1_presHealth4", "") %>"
				@oscar.formDB /></td>
		</tr>
		<tr>
			<td><input type="text" name="pg1_obHistDate5"
				id="pg1_obHistDate5" size="6" maxlength="10"
				value="<%= props.getProperty("pg1_obHistDate5", "") %>"
				@oscar.formDB /> <img src="../images/cal.gif"
				id="pg1_obHistDate5_cal"></td>
			<td><input type="text" name="pg1_birthOrAbort5"
				style="width: 100%" size="20" maxlength="20"
				value="<%= props.getProperty("pg1_birthOrAbort5", "") %>"
				@oscar.formDB /></td>
			<td><input type="text" name="pg1_deliWeek5" style="width: 100%"
				size="8" maxlength="8"
				value="<%= props.getProperty("pg1_deliWeek5", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_laboHr5" style="width: 100%"
				size="8" maxlength="8"
				value="<%= props.getProperty("pg1_laboHr5", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_deliType5" class="spe"
				onDblClick="showDeliBox('Delidiv',1, this, event);"
				style="width: 100%" size="10" maxlength="15"
				value="<%= props.getProperty("pg1_deliType5", "") %>" @oscar.formDB />
			</td>

			<td><input type="text" name="pg1_periComp5" style="width: 100%"
				size="50" maxlength="80"
				value="<%= props.getProperty("pg1_periComp5", "") %>" @oscar.formDB />
			</td>
			<td><select name="pg1_obHistSex5">
				<option value=""></option>
				<option value="M"
					<%=props.getProperty("pg1_obHistSex5", "").equalsIgnoreCase("M")?"selected":""%>>M</option>
				<option value="F"
					<%=props.getProperty("pg1_obHistSex5", "").equalsIgnoreCase("F")?"selected":""%>>F</option>
			</select></td>
			<td><input type="text" name="pg1_birthWeit5" style="width: 100%"
				size="6" maxlength="8"
				value="<%= props.getProperty("pg1_birthWeit5", "") %>" @oscar.formDB />
			</td>
			<td><input type="text" name="pg1_presHealth5" class="spe"
				onDblClick='showDef("A&W", this);' style="width: 100%" size="6"
				maxlength="8"
				value="<%= props.getProperty("pg1_presHealth5", "") %>"
				@oscar.formDB /></td>
		</tr>
	</table>

	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td width="33%">

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td width="30%"><b>4.</b> LMP <img src="../images/cal.gif"
						id="pg1_lmp_cal"><br>
					dd/mm/yyyy<br>
					<input type="text" name="pg1_lmp" id="pg1_lmp" style="width: 100%"
						size="10" maxlength="10"
						value="<%= props.getProperty("pg1_lmp", "") %>" @oscar.formDB
						dbType="date" /></td>
					<td width="30%">MENSES CYCLE<br>
					<input type="text" name="pg1_mensCycle" style="width: 100%"
						size="8" maxlength="8"
						value="<%= props.getProperty("pg1_mensCycle", "") %>"
						@oscar.formDB /></td>
					<td>EDD BY DATES<br>
					dd/mm/yyyy<br>
					<input type="text" name="pg1_eddByDate" class="spe"
						style="width: 100%;" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_eddByDate", "") %>"
						onDblClick="calByLMP();" @oscar.formDB dbType="date" /></td>
				</tr>
				<tr>
					<td colspan="2">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="30%"><span class="small8">CONTRACEPTION</font> <br>
							METHOD:<br>
							<!--input type="text" name="pg1_contrMethod" style="width:100%" size="12" maxlength="15" value="<%--= props.getProperty("pg1_contrMethod", "") --%>" @oscar.formDB /-->
							<select name="pg1_contrMethod">
								<%
        String[] opt = {"nothing", "condom", "OCP", "diaphragm", "IUD"};
        for (int i=0; i<opt.length; i++) {
        %>
								<option value="<%=opt[i]%>"
									<%=props.getProperty("pg1_contrMethod", "").equals(opt[i])?"selected":""%>><%=opt[i]%></option>
								<%}%>
							</select></td>
							<td width="30%"><span class="small8">WHEN STOPPED:</font><br>
							dd/mm/yyyy <img src="../images/cal.gif" id="pg1_stopDate_cal"><br>
							<input type="text" name="pg1_stopDate" id="pg1_stopDate"
								style="width: 100%" size="10" maxlength="10"
								value="<%= props.getProperty("pg1_stopDate", "") %>"
								@oscar.formDB /></td>
						</tr>
					</table>
					</td>
					<td>EDD BY US <img src="../images/cal.gif"
						id="pg1_eddByUs_cal"><br>
					dd/mm/yyyy<br>
					<input type="text" name="pg1_eddByUs" id="pg1_eddByUs"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_eddByUs", "") %>" @oscar.formDB
						dbType="date" /></td>
				</tr>
			</table>

			</td>
			<td width="33%" valign="top">

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td><b>5. <font color="red">ALLERGIES</font></b> <input
						type="checkbox" name="pg1_allergyN"
						<%= props.getProperty("pg1_allergyN", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> NONE KNOWN <br>
					<input type="checkbox" name="pg1_allergyY"
						<%= props.getProperty("pg1_allergyY", "") %>  @oscar.formDB
						dbType="tinyint(1)" /> YES (specify): <input type="text"
						name="pg1_allergy" size="25" maxlength="50"
						value="<%= props.getProperty("pg1_allergy", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><b>CURRENT MEDICATIONS</b> <textarea name="pg1_curMedic"
						style="width: 100%" cols="30" rows="2" @oscar.formDB
						dbType="varchar(255)"> <%= props.getProperty("pg1_curMedic", "") %> </textarea>
					</td>
				</tr>
			</table>

			</td>
			<td valign="top">

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td><b>6.</b> BELIEFS & PRACTICES <br>
					<input type="text" name="pg1_beliPract" style="width: 100%"
						size="60" maxlength="60"
						value="<%= props.getProperty("pg1_beliPract", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td>COMPLEMENTARY Rx's <br>
					<input type="text" name="pg1_compRx" style="width: 100%" size="60"
						maxlength="60" value="<%= props.getProperty("pg1_compRx", "") %>"
						@oscar.formDB /></td>
				</tr>
			</table>

			</td>
		</tr>
	</table>

	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td width="33%" valign="top">

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td>

					<table class="shrinkMe" width="100%" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td colspan="3"><b>7. PRESENT PREGNANCY</b></td>
						</tr>
						<tr>
							<td colspan="2"><i>no</i></td>
							<td align="center"><i>yes (specify)</i></td>
						</tr>
						<tr>
							<td width="1%"><input type="checkbox" name="pg1_bleeding"
								<%= props.getProperty("pg1_bleeding", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td width="20%"><span class="small9">BLEEDING</span></td>
							<td><input type="text" name="pg1_bleedingSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_bleedingSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_nausea"
								<%= props.getProperty("pg1_nausea", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small9">NAUSEA</span></td>
							<td><input type="text" name="pg1_nauseaSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_nauseaSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_infect"
								<%= props.getProperty("pg1_infect", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small9">INFECTIONS<br>
							OR FEVER</span></td>
							<td><input type="text" name="pg1_infectSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_infectSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_depres"
								<%= props.getProperty("pg1_depres", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small9">DEPRESSION</span></td>
							<td><input type="text" name="pg1_depresSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_depresSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_prePregOther"
								<%= props.getProperty("pg1_prePregOther", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small9">OTHER</span></td>
							<td><input type="text" name="pg1_prePregOtherSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_prePregOtherSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td>

					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="2" nowrap><b>10. FAMILY HISTORY</b></td>
							<td><span class="small8">MATERNAL PATERNAL</span></td>
						</tr>
						<tr>
							<td colspan="2">&nbsp;<i>no</i></td>
							<td align="center"><i>yes (specify)</i></td>
						</tr>
						<tr>
							<td width="1%"><input type="checkbox" name="pg1_heartDise"
								<%= props.getProperty("pg1_heartDise", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td width="20%"><span class="small8">HEART DISEASE</span></td>
							<td><input type="text" name="pg1_heartDiseSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_heartDiseSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_hyperts"
								<%= props.getProperty("pg1_hyperts", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">HYPERTENSION</span></td>
							<td><input type="text" name="pg1_hypertsSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_hypertsSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_diabete"
								<%= props.getProperty("pg1_diabete", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">DIABETES</span></td>
							<td><input type="text" name="pg1_diabeteSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_diabeteSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_deprPsychiat"
								<%= props.getProperty("pg1_deprPsychiat", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">DEPRESSION OR<br>
							PSYCHIATRIC</span></td>
							<td><input type="text" name="pg1_deprPsychiatSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_deprPsychiatSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_alcohDrug"
								<%= props.getProperty("pg1_alcohDrug", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">ALCOHOL/<br>
							DRUG USE</span></td>
							<td><input type="text" name="pg1_alcohDrugSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_alcohDrugSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_thromCoag"
								<%= props.getProperty("pg1_thromCoag", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">THROMBOEMBOLIC / COAG.</span></td>
							<td><input type="text" name="pg1_thromCoagSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_thromCoagSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_inherDisease"
								<%= props.getProperty("pg1_inherDisease", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">INHERITED<br>
							DISEASE/DEFECT</span></td>
							<td><input type="text" name="pg1_inherDiseaseSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_inherDiseaseSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_ethnic"
								<%= props.getProperty("pg1_ethnic", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">ETHNIC ( e.g.<br>
							Taysachs, Sickle)</span></td>
							<td><input type="text" name="pg1_ethnicSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_ethnicSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_famHistOther"
								<%= props.getProperty("pg1_famHistOther", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">OTHER</span></td>
							<td><input type="text" name="pg1_famHistOtherSpec"
								style="width: 100%" size="30" maxlength="30"
								value="<%= props.getProperty("pg1_famHistOtherSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
					</table>

					</td>
				</tr>
			</table>


			</td>
			<td width="33%" valign="top">

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="3"><b>8. PAST ILLNESS</b></td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;<i>no</i></td>
					<td align="center"><i>yes (specify)</i></td>
				</tr>
				<tr>
					<td width="1%" valign="top"><input type="checkbox"
						name="pg1_operation"
						<%= props.getProperty("pg1_operation", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td width="30%" valign="top"><span class="small8">OPERATIONS</span></td>
					<td><textarea name="pg1_operationSpec" style="width: 100%"
						cols="60" rows="3" @oscar.formDB dbType="varchar(255)"><%= props.getProperty("pg1_operationSpec", "") %>
    </textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_cvOrResp"
						<%= props.getProperty("pg1_cvOrResp", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">CV OR<br>
					RESPIRATORY</span></td>
					<td><textarea name="pg1_cvOrRespSpec" style="width: 100%"
						cols="60" rows="2" @oscar.formDB dbType="varchar(255)"><%= props.getProperty("pg1_cvOrRespSpec", "") %>
    </textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_anesthetic"
						<%= props.getProperty("pg1_anesthetic", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">ANESTHETIC PROBLEMS</span></td>
					<td><input type="text" name="pg1_anestheticSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_anestheticSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_rxBlood"
						<%= props.getProperty("pg1_rxBlood", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Rx BLOOD PRODUCTS</span></td>
					<td><input type="text" name="pg1_rxBloodSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_rxBloodSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_infectStd"
						<%= props.getProperty("pg1_infectStd", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">INFECTIONS, STDS etc.</span></td>
					<td><input type="text" name="pg1_infectStdSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_infectStdSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_susChiPox"
						<%= props.getProperty("pg1_susChiPox", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">SUSCEPTIBLE TO CHICKEN POX</span></td>
					<td><input type="text" name="pg1_susChiPoxSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_susChiPoxSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_thrCoag"
						<%= props.getProperty("pg1_thrCoag", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">THROMBOEMBOLIC / COAG.</span></td>
					<td><input type="text" name="pg1_thrCoagSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_thrCoagSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_hyper"
						<%= props.getProperty("pg1_hyper", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">HYPERTENSION</span></td>
					<td><input type="text" name="pg1_hyperSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_hyperSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_piGi"
						<%= props.getProperty("pg1_piGi", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">GI</span></td>
					<td><input type="text" name="pg1_piGiSpec" style="width: 100%"
						size="40" maxlength="40"
						value="<%= props.getProperty("pg1_piGiSpec", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_piUrin"
						<%= props.getProperty("pg1_piUrin", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">URINARY</span></td>
					<td><input type="text" name="pg1_piUrinSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_piUrinSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_dbEndoc"
						<%= props.getProperty("pg1_dbEndoc", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">DIABETES OR<br>
					ENDOCRINE</span></td>
					<td><input type="text" name="pg1_dbEndocSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_dbEndocSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_seizNeur"
						<%= props.getProperty("pg1_seizNeur", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">SEIZURE OR<br>
					NEUROLOGIC</span></td>
					<td><input type="text" name="pg1_seizNeurSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_seizNeurSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_deprPsy"
						<%= props.getProperty("pg1_deprPsy", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">DEPRESSION OR<br>
					PSYCHIATRIC</span></td>
					<td><input type="text" name="pg1_deprPsySpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_deprPsySpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="pg1_piOther"
						<%= props.getProperty("pg1_piOther", "") %>  @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">OTHER</span></td>
					<td><input type="text" name="pg1_piOtherSpec"
						style="width: 100%" size="40" maxlength="40"
						value="<%= props.getProperty("pg1_piOtherSpec", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
			</table>

			</td>
			<td valign="top">

			<table width="100%" border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td>

					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="2"><b>9. SOCIAL HISTORY</b></td>
						</tr>
						<tr>
							<td colspan="2"><i><span class="small9">&nbsp;
							discussed &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;concerns (specify)</span></i></td>
						</tr>
						<tr>
							<td width="1%"><input type="checkbox" name="pg1_nutrition"
								<%= props.getProperty("pg1_nutrition", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small9">NUTRITION</span> <input type="text"
								name="pg1_nutritionSpec" size="32" maxlength="40"
								value="<%= props.getProperty("pg1_nutritionSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_speDiet"
								<%= props.getProperty("pg1_speDiet", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small9">SPECIAL DIET</span> <input
								type="text" name="pg1_speDietSpec" size="30" maxlength="40"
								value="<%= props.getProperty("pg1_speDietSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_foliAcid"
								<%= props.getProperty("pg1_foliAcid", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">FOLIC ACID</span> <input
								type="text" name="pg1_foliAcidSpec" size="10" maxlength="15"
								value="<%= props.getProperty("pg1_foliAcidSpec", "") %>"
								@oscar.formDB /> <span class="small8">start date:</span> <input
								type="text" name="pg1_foliAcidDate" size="10" maxlength="15"
								value="<%= props.getProperty("pg1_foliAcidDate", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_alco"
								<%= props.getProperty("pg1_alco", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">ALCOHOL</span> <input type="text"
								name="pg1_alcoSpec" size="10" maxlength="10"
								value="<%= props.getProperty("pg1_alcoSpec", "") %>"
								@oscar.formDB /> <span class="small7" title="(see reverse)">T-ACE
							SCORE:</span> <input type="text" name="pg1_alcoTA" size="5"
								maxlength="5" value="<%= props.getProperty("pg1_alcoTA", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_drug"
								<%= props.getProperty("pg1_drug", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">DRUGS (OTC's, vitamins)</span> <input
								type="text" name="pg1_drugSpec" size="22" maxlength="30"
								value="<%= props.getProperty("pg1_drugSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_subUse"
								<%= props.getProperty("pg1_subUse", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">SUBSTANCE USE</span> <input
								type="text" name="pg1_subUseSpec" size="28" maxlength="35"
								value="<%= props.getProperty("pg1_subUseSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_ipv"
								<%= props.getProperty("pg1_ipv", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small9">IPV</span> <input type="text"
								name="pg1_ipvSpec" size="40" maxlength="40"
								value="<%= props.getProperty("pg1_ipvSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_smokeBef"
								<%= props.getProperty("pg1_smokeBef", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">SMOKING (before pregnancy)
							Cigs./day</span> <input type="text" name="pg1_smokeBefSpec" size="5"
								maxlength="5"
								value="<%= props.getProperty("pg1_smokeBefSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_smokeCur"
								<%= props.getProperty("pg1_smokeCur", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">SMOKING (currently)
							Cigs./day</span> <input type="text" name="pg1_smokeCurSpec" size="5"
								maxlength="5"
								value="<%= props.getProperty("pg1_smokeCurSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_secSmoke"
								<%= props.getProperty("pg1_secSmoke", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">SECOND HAND SMOKE</span> <input
								type="text" name="pg1_secSmokeSpec" size="24" maxlength="30"
								value="<%= props.getProperty("pg1_secSmokeSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_finaHouse"
								<%= props.getProperty("pg1_finaHouse", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">FINANCIAL/HOUSING</span> <input
								type="text" name="pg1_finaHouseSpec" size="26" maxlength="30"
								value="<%= props.getProperty("pg1_finaHouseSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_supSys"
								<%= props.getProperty("pg1_supSys", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">SUPPORT SYSTEMS</span> <input
								type="text" name="pg1_supSysSpec" size="26" maxlength="30"
								value="<%= props.getProperty("pg1_supSysSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td>

					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="3"><span class="small9">NUMBER OF SCHOOL
							YEARS COMPLETED:</span> <input type="text" name="pg1_schYear" size="3"
								maxlength="3"
								value="<%= props.getProperty("pg1_schYear", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td colspan="3"><span class="small8">WORK (specify
							type):</span> <input type="text" name="pg1_work" size="20"
								maxlength="25" value="<%= props.getProperty("pg1_work", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td colspan="3"><span class="small8">hours worked per
							day:</span> <input type="text" name="pg1_workHourDay" size="3"
								maxlength="5"
								value="<%= props.getProperty("pg1_workHourDay", "") %>"
								@oscar.formDB /> <span class="small8">quitting date:</span> <input
								type="text" name="pg1_quitDate" size="10" maxlength="10"
								value="<%= props.getProperty("pg1_quitDate", "") %>"
								@oscar.formDB dbType="date" /></td>
						</tr>
						<tr>
							<td colspan="3"><span class="small8">partner's work:</span>
							<input type="text" name="pg1_ptWork" size="30" maxlength="40"
								value="<%= props.getProperty("pg1_ptWork", "") %>" @oscar.formDB />
							</td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td>

					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="1%" valign="top"><input type="checkbox"
								name="pg1_earComm"
								<%= props.getProperty("pg1_earComm", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">EARLY COMMUNITY<br>
							SERVICES REFERRAL</span></td>
							<td valign="top"><input type="text" name="pg1_earCommSpec"
								size="26" maxlength="30"
								value="<%= props.getProperty("pg1_earCommSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="pg1_otherRef"
								<%= props.getProperty("pg1_otherRef", "") %>  @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td><span class="small8">OTHER REFERRAL</span></td>
							<td valign="top"><input type="text" name="pg1_otherRefSpec"
								size="26" maxlength="35"
								value="<%= props.getProperty("pg1_otherRefSpec", "") %>"
								@oscar.formDB /></td>
						</tr>
					</table>

					</td>
				</tr>
			</table>

			</td>
		</tr>
	</table>

	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td width="60%" valign="top">

			<table class="shrinkMe" width="100%" border="1" cellspacing="0"
				cellpadding="0">
				<tr>
					<td colspan="2"><b>11. EXAMINATION</b> dd/mm/yyyy <img
						src="../images/cal.gif" id="pg1_examination_cal"><br>
					<input type="text" name="pg1_examination" id="pg1_examination"
						style="width: 100%" size="10" maxlength="10"
						value="<%= props.getProperty("pg1_examination", "") %>"
						@oscar.formDB dbType="date" /></td>
					<td colspan="2"><b>BP</b><br>
					<input type="text" name="pg1_bp" style="width: 100%" size="10"
						maxlength="12" value="<%= props.getProperty("pg1_bp", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td width="10%"><span class="small9">HEAD &<br>
					NECK</span></td>
					<td width="40%"><input type="text" name="pg1_headNeck"
						class="spe" onDblClick='showDef("NAD", this);' size="35"
						maxlength="40"
						value="<%= props.getProperty("pg1_headNeck", "") %>" @oscar.formDB />
					</td>
					<td width="15%"><span class="small8">MUSCULOSKELETAL<br>
					&SPINE</span></td>
					<td width="35%"><input type="text" name="pg1_muscSpine"
						class="spe" onDblClick='showDef("NAD", this);' size="30"
						maxlength="40"
						value="<%= props.getProperty("pg1_muscSpine", "") %>"
						@oscar.formDB /></td>
				</tr>
				<tr>
					<td><span class="small9">BREAST /<br>
					NIPPLES</span></td>
					<td><input type="text" name="pg1_breaNipp" class="spe"
						onDblClick='showDef("NAD", this);' size="35" maxlength="40"
						value="<%= props.getProperty("pg1_breaNipp", "") %>" @oscar.formDB />
					</td>
					<td><span class="small9">VARICES &<br>
					SKIN</span></td>
					<td><input type="text" name="pg1_variSkin" class="spe"
						onDblClick='showDef("NAD", this);' size="30" maxlength="40"
						value="<%= props.getProperty("pg1_variSkin", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><span class="small9">HEART &<br>
					LUNGS</span></td>
					<td><input type="text" name="pg1_heartLung" class="spe"
						onDblClick='showDef("NAD", this);' size="35" maxlength="40"
						value="<%= props.getProperty("pg1_heartLung", "") %>"
						@oscar.formDB /></td>
					<td><span class="small9">PELVIC EXAM</span></td>
					<td><input type="text" name="pg1_pelvic" class="spe"
						onDblClick='showDef("NAD", this);' size="30" maxlength="40"
						value="<%= props.getProperty("pg1_pelvic", "") %>" @oscar.formDB />
					</td>
				</tr>
				<tr>
					<td><span class="small9">ABDOMEN</span></td>
					<td><input type="text" name="pg1_abdomen" class="spe"
						onDblClick='showDef("NAD", this);' size="35" maxlength="40"
						value="<%= props.getProperty("pg1_abdomen", "") %>" @oscar.formDB />
					</td>
					<td><span class="small8">SWABS /<br>
					CERVIX CYTOLOGY</span></td>
					<td><input type="text" name="pg1_swabsCerv" class="spe"
						onDblClick='showDef("NAD", this);' size="30" maxlength="40"
						value="<%= props.getProperty("pg1_swabsCerv", "") %>"
						@oscar.formDB /></td>
				</tr>
			</table>

			</td>
			<td valign="top">

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="6"><b>12. <font color="red">TOPICS FOR
					DISCUSSION</font></b></td>
				</tr>
				<tr>
					<td width="1%"><input type="checkbox" name="pg1_disBestCh"
						<%= props.getProperty("pg1_disBestCh", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td width="38%"><span class="small8"><i>Baby's Best
					Chance</i></font></td>
					<td width="1%"><input type="checkbox"
						name="pg1_disRestPreLabour"
						<%= props.getProperty("pg1_disRestPreLabour", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td width="35%"><span class="small8">Rest / Preterm
					Labour</font></td>
					<td width="1%"><input type="checkbox" name="pg1_disCallSCh"
						<%= props.getProperty("pg1_disCallSCh", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Call Schedule</font></td>
				</tr>
				<tr>
					<td><input type="checkbox" name="pg1_disPreEdu"
						<%= props.getProperty("pg1_disPreEdu", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Prenatal Education</font></td>
					<td><input type="checkbox" name="pg1_disSexu"
						<%= props.getProperty("pg1_disSexu", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Sexual Relations</font></td>
					<td><input type="checkbox" name="pg1_disLabStg"
						<%= props.getProperty("pg1_disLabStg", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Labour Stages</font></td>
				</tr>
				<tr>
					<td><input type="checkbox" name="pg1_disBreFeed"
						<%= props.getProperty("pg1_disBreFeed", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Breastfeeding </font></td>
					<td><input type="checkbox" name="pg1_disGBS"
						<%= props.getProperty("pg1_disGBS", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">GBS Management</font></td>
					<td><input type="checkbox" name="pg1_disCSect"
						<%= props.getProperty("pg1_disCSect", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">C-Section</font></td>
				</tr>
				<tr>
					<td colspan="2"><span class="small7">plans to BF<input
						type="checkbox" name="pg1_disBfY"
						<%= props.getProperty("pg1_disBfY", "") %> @oscar.formDB
						dbType="tinyint(1)" />yes <input type="checkbox" name="pg1_disBfN"
						<%= props.getProperty("pg1_disBfN", "") %> @oscar.formDB
						dbType="tinyint(1)" />no</font></td>
					<td><input type="checkbox" name="pg1_disVBAC"
						<%= props.getProperty("pg1_disVBAC", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">VBAC</font></td>
					<td><input type="checkbox" name="pg1_disBabyCare"
						<%= props.getProperty("pg1_disBabyCare", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Baby Care</font></td>
				</tr>
				<tr>
					<td><input type="checkbox" name="pg1_disBreNip"
						<%= props.getProperty("pg1_disBreNip", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Breast / Nipple Care </font></td>
					<td><input type="checkbox" name="pg1_disHospAdm"
						<%= props.getProperty("pg1_disHospAdm", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Hospital Admission/</font></td>
					<td><input type="checkbox" name="pg1_disSIDS"
						<%= props.getProperty("pg1_disSIDS", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">SIDS Prevention</font></td>
				</tr>
				<tr>
					<td><input type="checkbox" name="pg1_disExer"
						<%= props.getProperty("pg1_disExer", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Exercises</font></td>
					<td colspan="2" align="center"><span class="small8">Procedures</font></td>
					<td><input type="checkbox" name="pg1_disCircum"
						<%= props.getProperty("pg1_disCircum", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Circumcision</font></td>
				</tr>
				<tr>
					<td><input type="checkbox" name="pg1_disGenCouns"
						<%= props.getProperty("pg1_disGenCouns", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Genetic Counselling</font></td>
					<td><input type="checkbox" name="pg1_disBirthPlan"
						<%= props.getProperty("pg1_disBirthPlan", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Birth Plan</font></td>
					<td><input type="checkbox" name="pg1_disOther"
						<%= props.getProperty("pg1_disOther", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td rowspan="2"><span class="small8"> <textarea
						name="pg1_disOtherSpec" style="width: 100%" cols="10" rows="2"
						@oscar.formDB dbType="varchar(30)"><%= props.getProperty("pg1_disOtherSpec", "") %>
    </textarea> </font></td>
				</tr>
				<tr>
					<td><input type="checkbox" name="pg1_disHIV"
						<%= props.getProperty("pg1_disHIV", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">HIV Testing</font></td>
					<td><input type="checkbox" name="pg1_disPain"
						<%= props.getProperty("pg1_disPain", "") %> @oscar.formDB
						dbType="tinyint(1)" /></td>
					<td><span class="small8">Pain Management</font></td>
					<td></td>
				</tr>
			</table>

			</td>
		</tr>
	</table>

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="12%" valign="top" nowrap><b>13. SUMMARY</b></td>
			<td width="1%" valign="top"><input type="checkbox"
				name="pg1_sumBloodTran"
				<%= props.getProperty("pg1_sumBloodTran", "") %>  @oscar.formDB
				dbType="tinyint(1)" /></td>
			<td width="50%" valign="top">I have discussed the benefits and
			risks of planned or potential transfusion therapy of blood and/or
			blood products with the patient</td>
			<td width="1%" valign="top"><input type="checkbox"
				name="pg1_sumSerumScr"
				<%= props.getProperty("pg1_sumSerumScr", "") %>  @oscar.formDB
				dbType="tinyint(1)" /></td>
			<td valign="top">Maternal serum screening offered</td>
		</tr>
	</table>

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><textarea name="pg1_summary" style="width: 100%" cols="100"
				rows="3" @oscar.formDB dbType="text"> <%= props.getProperty("pg1_summary", "") %> </textarea>
			</td>
		</tr>
	</table>

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="60%"></td>
			<td>SIGNATURE: <input type="text" name="pg1_signature" size="35"
				maxlength="40" value="<%= props.getProperty("pg1_signature", "") %>"
				@oscar.formDB /> MD/RM</td>
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
			<td><!--a href="javascript: popPage('formlabreq.jsp?demographic_no=<%=demoNo%>&formId=0&provNo=<%=provNo%>&labType=AR','LabReq');">LAB</a-->
			</td>
			<td align="right"><b>View:</b> <a
				href="javascript: popupPage('formbcarpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2
			<font size=-2>(pg.1)</font></a> | <a
				href="javascript: popupPage('formbcarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2
			<font size=-2>(pg.2)</font></a> &nbsp;</td>
			<td align="right"><b>Edit:</b>AR1 | <a
				href="formbcarpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.1)</font></a> | <a
				href="formbcarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2
			<font size=-2>(pg.2)</font></a> | <!--a href="javascript: popupFixedPage(700,950,'../decision/antenatal/antenatalplanner.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">AR Planner</a-->
			</td>
			<%
  }
%>
		</tr>
	</table>


	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<th align="center">RISK IDENTIFICATION</th>
		</tr>
		<tr>
			<td>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td valign="top">

					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<th colspan="2" align="left">PAST OBSTETRICAL HISTORY</th>
						</tr>
						<tr>
							<th colspan="2" align="left"><span class="small9">RISK
							FACTORS</span></th>
						</tr>
						<tr>
							<td width="1%"><input type="checkbox"
								name="ar2_riskNeonDeath"
								<%= props.getProperty("ar2_riskNeonDeath", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Neonatal death</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskStillbirth"
								<%= props.getProperty("ar2_riskStillbirth", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Stillbirth</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskAbortion"
								<%= props.getProperty("ar2_riskAbortion", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Abortion ( 12 - 20 weeks )</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskHabitAbort"
								<%= props.getProperty("ar2_riskHabitAbort", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Habitual abortion ( 3+ )</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskPriPretBirth33"
								<%= props.getProperty("ar2_riskPriPretBirth33", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Prior preterm birth ( 33 - 36 wks. )</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskPriPretBirth20"
								<%= props.getProperty("ar2_riskPriPretBirth20", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Prior preterm birth ( 20 - 33 wks. )</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskPriCesBirth"
								<%= props.getProperty("ar2_riskPriCesBirth", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Prior Cesarean birth ( uterine surgery )</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskPriIUGR"
								<%= props.getProperty("ar2_riskPriIUGR", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Prior IUGR baby</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskPriMacr"
								<%= props.getProperty("ar2_riskPriMacr", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Prior macrosomic baby</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskRhImmu"
								<%= props.getProperty("ar2_riskRhImmu", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Rh Immunized ( antibodies present )</td>
						</tr>
						<tr>
							<td valign="top"><input type="checkbox"
								name="ar2_riskPriRhNB"
								<%= props.getProperty("ar2_riskPriRhNB", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Prior Rh affected preg. with NB exchange<br>
							or prem.</td>
						</tr>
						<tr>
							<td valign="top"><input type="checkbox"
								name="ar2_riskMajCongAnom"
								<%= props.getProperty("ar2_riskMajCongAnom", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Major congenital anomalies (eg. Cardiac,<br>
							CNS, Down's Syndrome.)</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskPPHemo"
								<%= props.getProperty("ar2_riskPPHemo", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>P.P. Hemorrhage</td>
						</tr>
					</table>

					</td>
					<td valign="top">

					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<th colspan="2" align="left">MEDICAL HISTORY RISK FACTORS</th>
						</tr>
						<tr>
							<th colspan="2" align="left"><span class="small9">DIABETES</span></th>
						</tr>
						<tr>
							<td width="1%"><input type="checkbox" name="ar2_riskConDiet"
								<%= props.getProperty("ar2_riskConDiet", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Controlled by diet only</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskDietMacFetus"
								<%= props.getProperty("ar2_riskDietMacFetus", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Diet only macrosomic fetus</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskInsDepend"
								<%= props.getProperty("ar2_riskInsDepend", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Insulin dependent</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskRetDoc"
								<%= props.getProperty("ar2_riskRetDoc", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Retinopathy documented</td>
						</tr>
						<tr>
							<th colspan="2" align="left"><span class="small9">HEART
							DISEASE</span></th>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskAsymt"
								<%= props.getProperty("ar2_riskAsymt", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Asymptomatic (no effect on daily living)</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskSymt"
								<%= props.getProperty("ar2_riskSymt", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Symptomatic ( affects daily living)</td>
						</tr>
						<tr>
							<th colspan="2" align="left"><span class="small9">HYPERTENSION</span></th>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_risk14090"
								<%= props.getProperty("ar2_risk14090", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>140 / 90</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskHyperDrug"
								<%= props.getProperty("ar2_riskHyperDrug", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Hypertensive drugs</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskChroRenalDisease"
								<%= props.getProperty("ar2_riskChroRenalDisease", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Chronic renal disease documented</td>
						</tr>
						<tr>
							<th colspan="2" align="left"><span class="small9">OTHER</span></th>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskUnder18"
								<%= props.getProperty("ar2_riskUnder18", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Age under 18 at delivery</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskOver35"
								<%= props.getProperty("ar2_riskOver35", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Age 35 or over at delivery</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskObesity"
								<%= props.getProperty("ar2_riskObesity", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Obesity (equal or more than 90kg. or 200 lbs.)</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskH157"
								<%= props.getProperty("ar2_riskH157", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Height (under 1.57 m 5 ft. 2 in.)</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskH152"
								<%= props.getProperty("ar2_riskH152", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Height (under 1.52 m 5 ft. 0 in.)</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskDepre"
								<%= props.getProperty("ar2_riskDepre", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Depression</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskAlcoDrug"
								<%= props.getProperty("ar2_riskAlcoDrug", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Alcohol and Drugs</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskSmoking"
								<%= props.getProperty("ar2_riskSmoking", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Smoking any time during pregnancy</td>
						</tr>
						<tr>
							<td valign="top"><input type="checkbox"
								name="ar2_riskOtherMedical"
								<%= props.getProperty("ar2_riskOtherMedical", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Other medical / surgical disorders<br>
							e.g. epilepsy, severe asthma, Lupus etc.</td>
						</tr>
					</table>



					</td>
					<td valign="top">

					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<th colspan="2" align="left">PROBLEMS IN CURRENT PREGNANCY</th>
						</tr>
						<tr>
							<th colspan="2" align="left"><span class="small9">RISK
							FACTOR</span></th>
						</tr>
						<tr>
							<td width="1%"><input type="checkbox"
								name="ar2_riskDiagLarge"
								<%= props.getProperty("ar2_riskDiagLarge", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Diagnosis of large for dates</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskDiagSmall"
								<%= props.getProperty("ar2_riskDiagSmall", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Diagnosis of small for dates (IUGR)</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskPolyhyd"
								<%= props.getProperty("ar2_riskPolyhyd", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Polyhydramnios or oligohydramnios</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskMulPreg"
								<%= props.getProperty("ar2_riskMulPreg", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Multiple pregnancy</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskMalpres"
								<%= props.getProperty("ar2_riskMalpres", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Malpresentations</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskMemRupt37"
								<%= props.getProperty("ar2_riskMemRupt37", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Membrane rupture before 37 weeks</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskBleeding"
								<%= props.getProperty("ar2_riskBleeding", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Bleeding</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskPregIndHypert"
								<%= props.getProperty("ar2_riskPregIndHypert", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Pregnancy induced hypertension</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskProte1"
								<%= props.getProperty("ar2_riskProte1", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Proteinuria > 1+</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskGesDiabete"
								<%= props.getProperty("ar2_riskGesDiabete", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Gestational diabetes documented</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskBloodAnti"
								<%= props.getProperty("ar2_riskBloodAnti", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Blood antibodies (Rh, Anti C, Anti K, etc.)</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskAnemia"
								<%= props.getProperty("ar2_riskAnemia", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Anemia ( < 100g per L )</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskAdmPreterm"
								<%= props.getProperty("ar2_riskAdmPreterm", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Admission in preterm labour</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="ar2_riskPreg42W"
								<%= props.getProperty("ar2_riskPreg42W", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Pregnancy >= 42 weeks</td>
						</tr>
						<tr>
							<td valign="top"><input type="checkbox"
								name="ar2_riskWtLoss"
								<%= props.getProperty("ar2_riskWtLoss", "") %> @oscar.formDB
								dbType="tinyint(1)" /></td>
							<td>Poor weight gain 26 - 36 weeks ( <.5 kg / wk )<br>
							or weight loss</td>
						</tr>
					</table>


					</td>
				</tr>
			</table>


			</td>
		</tr>
	</table>



</html:form>
</body>
<script type="text/javascript">
Calendar.setup({ inputField : "pg1_lmp", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_lmp_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_stopDate", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_stopDate_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_eddByUs", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_eddByUs_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_examination", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_examination_cal", singleClick : true, step : 1 });

Calendar.setup({ inputField : "pg1_obHistDate1", ifFormat : "%b %Y", showsTime :false, button : "pg1_obHistDate1_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_obHistDate2", ifFormat : "%b %Y", showsTime :false, button : "pg1_obHistDate2_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_obHistDate3", ifFormat : "%b %Y", showsTime :false, button : "pg1_obHistDate3_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_obHistDate4", ifFormat : "%b %Y", showsTime :false, button : "pg1_obHistDate4_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "pg1_obHistDate5", ifFormat : "%b %Y", showsTime :false, button : "pg1_obHistDate5_cal", singleClick : true, step : 1 });
</script>

</html:html>
