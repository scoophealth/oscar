
<%@ page language="java"%>
<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<%
	String formClass = "BCNewBorn";
	String formLink = "formbcnewbornpg1.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(demoNo, formId);

    FrmData fd = new FrmData();
    String resource = fd.getResource();
    //resource = resource + "ob/riskinfo/";    props.setProperty("c_lastVisited", "pg1");

	//get project_home
	String project_home = getServletContext().getRealPath("/") ;
	String sep = project_home.substring(project_home.length()-1);
	project_home = project_home.substring(0, project_home.length()-1) ;
	project_home = project_home.substring(project_home.lastIndexOf(sep)+1) ;
	//System.out.println(project_home);
%>
<%
  boolean bView = false;
  if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 
%>

<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>

<head>
    <title>New Born Record (Baby)</title>
    <link rel="stylesheet" type="text/css" href="<%=bView?"arStyleView.css" : "bcArStyle.css"%>">
    <html:base/>
</head>

<script type="text/javascript" language="Javascript">

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
            document.forms[0].action = "../form/createpdf?__title=British+Columbia+Antenatal+Record+Part+1&__cfgfile=bcar1PrintCfgPg1&__cfgfile=bcar1PrintCfgPg2&__template=bcar1";
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
    function onExit() {
        if(confirm("Are you sure you wish to exit without saving your changes?")==true)
        {
            window.close();
        }
        return(false);
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
        } else if(valDate(document.forms[0].pg1_obHistDate1)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_obHistDate2)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_obHistDate3)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_obHistDate4)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_obHistDate5)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_lmp)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_stopDate)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_foliAcidDate)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_quitDate)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_examination)==false){
            b = false;
        }

        return b;
    }
</script>




<script type="text/javascript" language="Javascript">
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


<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1"  onLoad="setfocus()">
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
<input type="hidden" name="c_lastVisited" value=<%=props.getProperty("c_lastVisited", "pg1")%> />
<input type="hidden" name="demographic_no" value="<%= props.getProperty("demographic_no", "0") %>" />
<input type="hidden" name="formCreated" value="<%= props.getProperty("formCreated", "") %>" />
<input type="hidden" name="form_class" value="<%=formClass%>" />
<input type="hidden" name="form_link" value="<%=formLink%>" />
<input type="hidden" name="formId" value="<%=formId%>" />
<!--input type="hidden" name="provider_no" value=<%=request.getParameter("provNo")%> /-->
<!--input type="hidden" name="provNo" value="<%= request.getParameter("provNo") %>" /-->
<input type="hidden" name="submit" value="exit"/>

<table class="Head" class="hidePrint">
    <tr>
        <td align="left">
<%
  if (!bView) {
%>
            <input type="submit" value="Save" onclick="javascript:return onSave();" />
            <input type="submit" value="Save and Exit" onclick="javascript:return onSaveExit();"/>
<%
  }
%>
            <input type="submit" value="Exit" onclick="javascript:return onExit();"/>
            <input type="submit" value="Print" onclick="javascript:return onPrint();"/>
        </td>
<%
  if (!bView) {
%>

        <td align="right"><b>View:</b> 
            <a href="javascript: popupPage('formbcnewbornpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">Part2 <font size=-2>(pg.1)</font></a> |
            <a href="javascript: popupPage('formbcnewbornpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">Part2 <font size=-2>(pg.2)</font></a>
            &nbsp;
        </td>
        <td align="right"><b>Edit:</b>Part1 |
            <a href="formbcnewbornpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Part2 <font size=-2>(pg.1)</font></a> |
            <a href="formbcnewbornpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Part2 <font size=-2>(pg.2)</font></a> |
        </td>
<%
  }
%>
    </tr>
</table>


<table width="100%" border="1"  cellspacing="0" cellpadding="0">
<tr><td width="60%">

  <table width="100%" border="0"  cellspacing="0" cellpadding="0">
    <tr>
    <th><%=bView?"<font color='yellow'>VIEW PAGE: </font>" : ""%>
	  British Columbia Newborn Record Part 1</th>
    </tr>
  </table>

  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <td width="60%"><b>1.</b> MOTHER'S NAME<br>
      <input type="text" name="c_motherName" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("c_motherName", "") %>" @oscar.formDB />
      </td>
	  <td width="8%">AGE<br>
      <input type="text" name="c_motherAge" style="width:100%" size="2" maxlength="4" value="<%= props.getProperty("c_motherAge", "") %>" @oscar.formDB />
      </td><td nowrap>MOTHER'S HOSPITAL #<br>
      <input type="text" name="c_hospitalNo" style="width:100%" size="20" maxlength="30" value="<%= props.getProperty("c_hospitalNo", "") %>" @oscar.formDB />
      </td>
    </tr>
  </table>
  
  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <td width="40%">SURNAME OF NEWBORN<br>
      <input type="text" name="c_newBornSurname" style="width:100%" size="30" maxlength="60" value="<%= props.getProperty("c_newBornSurname", "") %>" @oscar.formDB />
      </td>
	  <td width="50%">PARTINER'S NAME<br>
      <input type="text" name="c_partnerName" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("c_partnerName", "") %>" @oscar.formDB />
      </td><td nowrap>AGE<br>
      <input type="text" name="c_partnerAge" style="width:100%" size="2" maxlength="4" value="<%= props.getProperty("c_partnerAge", "") %>" @oscar.formDB />
      </td>
    </tr>
  </table>
  
  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <th colspan="5">G
      <input type="text" name="c_g" style="width:100%" size="2" maxlength="4" value="<%= props.getProperty("c_g", "") %>" @oscar.formDB />
      </th><th colspan="5">T
      <input type="text" name="c_t" style="width:100%" size="2" maxlength="4" value="<%= props.getProperty("c_t", "") %>" @oscar.formDB />
      </th><th colspan="5">P
      <input type="text" name="c_p" style="width:100%" size="2" maxlength="4" value="<%= props.getProperty("c_p", "") %>" @oscar.formDB />
      </th><th colspan="5">A
      <input type="text" name="c_a" style="width:100%" size="2" maxlength="4" value="<%= props.getProperty("c_a", "") %>" @oscar.formDB />
      </th><th colspan="5">L
      <input type="text" name="c_l" style="width:100%" size="2" maxlength="4" value="<%= props.getProperty("c_l", "") %>" @oscar.formDB />
      </th>
      <td width="20%">EDD dd/mm/yyyy
      <input type="text" name="pg1_eddByDate" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_eddByDate", "") %>" @oscar.formDB dbType="date" />
      </td>
	  <td width="20%" nowrap>BLOOD GROUP/Rh<br>
      <input type="text" name="pg1_bloodGrpRh" style="width:100%" size="10" maxlength="20" value="<%= props.getProperty("pg1_bloodGrpRh", "") %>" @oscar.formDB  />
	  </td>
	  <td nowrap>RH ANTIB<br>
      <input type="text" name="pg1_RhAntib" style="width:100%" size="10" maxlength="20" value="<%= props.getProperty("pg1_RhAntib", "") %>" @oscar.formDB  />
	  </td>
	  <td nowrap>HBsAg<br>
      <input type="text" name="pg1_HBsAg" style="width:100%" size="10" maxlength="20" value="<%= props.getProperty("pg1_HBsAg", "") %>" @oscar.formDB  />
	  </td>
    </tr>
  </table>
  
  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <td>RISK FACTORS FOR INFANT (Refer to Antenatal Record, Part 2)<br>
      <textarea name="c_riskFactor" style="width:100%" cols="50" rows="2" @oscar.formDB dbType="varchar(255)" > <%= props.getProperty("c_riskFactor", "") %> </textarea>
      </td>
    </tr>
  </table>

  </td>
  <td>

  <table width="100%" border="0"  cellspacing="0" cellpadding="0">
    <tr>
      <td width="55%">HOSPITAL NAME<br>
      <input type="text" name="c_hospitalName" style="width:100%" size="30" maxlength="80" value="<%= props.getProperty("c_hospitalName", "") %>" @oscar.formDB dbType="date" />
      </td>
      <td>DATE<br>
      <input type="text" name="pg1_formDate" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_formDate", "") %>" @oscar.formDB dbType="date" />
      </td>
    </tr><tr>
      <td width="55%">SURNAME<br>
      <input type="text" name="c_surname" style="width:100%" size="30" maxlength="30" value="<%= props.getProperty("c_surname", "") %>" @oscar.formDB />
	  </td>
	  <td>GIVEN NAME<br>
      <input type="text" name="c_givenName" style="width:100%" size="30" maxlength="30" value="<%= props.getProperty("c_givenName", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
      <td>ADDRESS<br>
      <input type="text" name="c_address" style="width:100%" size="50" maxlength="60" value="<%= props.getProperty("c_address", "") %>" @oscar.formDB />
      <input type="text" name="c_city" style="width:100%" size="50" maxlength="60" value="<%= props.getProperty("c_city", "") %>" @oscar.formDB />
      <input type="text" name="c_province" size="18" maxlength="50" value="<%= props.getProperty("c_province", "") %>" @oscar.formDB />
      <input type="text" name="c_postal" size="7" maxlength="8" value="<%= props.getProperty("c_postal", "") %>" @oscar.formDB />
	  </td>
	  <td valign="top">PHONE NUMBER<br>
      <input type="text" name="c_phone" style="width:100%" size="60" maxlength="60" value="<%= props.getProperty("c_phone", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
	  <td colspan="2"><span class="small9">PHYSICIAN / MIDWIFE NAME</span><br>
      <input type="text" name="c_phyMid" style="width:100%" size="30" maxlength="60" value="<%= props.getProperty("c_phyMid", "") %>" @oscar.formDB />
	  </td>
    </tr>
  </table>
  
  </td>
  </tr>
</table>





<table width="100%" border="0"  cellspacing="3" cellpadding="0">
<tr>
	<th><%=bView?"<font color='yellow'>VIEW PAGE: </font>" : ""%>
	NEW BORN RECORD (Baby: <%= props.getProperty("c_surname", "") %>, <%= props.getProperty("c_givenName", "") %> )</th>
    <input type="hidden" name="c_surname" size="30" value="<%= props.getProperty("c_surname", "") %>" maxlength="30" @oscar.formDB /> 
    <input type="hidden" name="c_givenName" size="30" value="<%= props.getProperty("c_givenName", "") %>" maxlength="30" @oscar.formDB /> 
</tr>
</table>



<center>

<table width="80%" border="0"  cellspacing="6" cellpadding="0">
<tr><td width="14%" valign="top">

  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <th colspan="3" align="left"><b>Apgar Score</b> 
      </th>
	</tr><tr>
	  <td align="center">1 Min<br>
      <input type="text" name="apgar1" size="5" maxlength="5" value="<%= props.getProperty("apgar1", "") %>" @oscar.formDB /> 
      </td>
	  <td align="center">5 Min<br>
      <input type="text" name="apgar5" size="5" maxlength="5" value="<%= props.getProperty("apgar5", "") %>" @oscar.formDB /> 
      </td>
	  <td align="center">10 Min<br>
      <input type="text" name="apgar10" size="5" maxlength="5" value="<%= props.getProperty("apgar10", "") %>" @oscar.formDB /> 
      </td>
	</tr>
	</table>
	
  

  </td><td valign="top" width="20%">


  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <th colspan="2" align="left">Stillbirth 
      </th>
	</tr>
	
	<tr>
	  <td>
      <input type="radio" name="stillbPriOnLab" <%= props.getProperty("stillbPriOnLab", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "stillb")' />
      Prior to onset of labour
      </td>
	  <td>
      <input type="radio" name="stillbNA" <%= props.getProperty("stillbNA", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "stillb")' />
      N/A
      </td>
	</tr><tr>
	  <td colspan="2">
      <input type="radio" name="stillbAftOn1Lab" <%= props.getProperty("stillbAftOn1Lab", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "stillb")' />
      After onset of first stage of labour
      </td>
	</tr><tr>
	  <td colspan="2">
      <input type="radio" name="stillbUnknown" <%= props.getProperty("stillbUnknown", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "stillb")' />
      Unknown if prior to or during labour
      </td>
	</tr>
  </table>
  

  </td><td valign="top" width="20%">


  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <th colspan="2" align="left">Eval of Development 
      </th>
	</tr><tr>
	  <td align="center">Length<br>
      <input type="text" name="evalDevLength" size="5" maxlength="5" value="<%= props.getProperty("evalDevLength", "") %>" @oscar.formDB /> 
      </td>
	  <td align="center">Head Circ<br>
      <input type="text" name="evalDevHeadCirc" size="5" maxlength="5" value="<%= props.getProperty("evalDevHeadCirc", "") %>" @oscar.formDB /> 
      </td>
	</tr>
  </table>


  </td>
</tr>
</table>
  


<table width="80%" border="0"  cellspacing="6" cellpadding="0">
<tr>
  <td valign="top" width="70%">



  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <th align="left">Resuscitation
      </th>
    </tr>
    <tr>
      <td valign="top">
	  
      <table width="100%" border="1"  cellspacing="0" cellpadding="0">
      <tr>
        <td align="left" valign="top">Meconium<br>
        <input type="checkbox" name="mecoThick" <%= props.getProperty("mecoThick", "") %> @oscar.formDB  dbType="tinyint(1)"/> Thick
        </td>
		<td>Drugs for Resus<br>
        <input type="radio" name="drugResYes" <%= props.getProperty("drugResYes", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "drugRes")' /> Yes
        <input type="radio" name="drugResNo" <%= props.getProperty("drugResNo", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "drugRes")' /> No
		<br>
        <input type="radio" name="drugResUnknown" <%= props.getProperty("drugResUnknown", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "drugRes")' /> Unknown
        </td>
		<td>Suction<br>
        <input type="checkbox" name="suctionPeri" <%= props.getProperty("suctionPeri", "") %> @oscar.formDB  dbType="tinyint(1)"/> At Perineum
        <input type="checkbox" name="suctionTrachea" <%= props.getProperty("suctionTrachea", "") %> @oscar.formDB  dbType="tinyint(1)"/> Trachea
		<br>
        <input type="checkbox" name="suctionOrop" <%= props.getProperty("suctionOrop", "") %> @oscar.formDB  dbType="tinyint(1)"/> Oropharynx
        <input type="checkbox" name="suctionUnspec" <%= props.getProperty("suctionUnspec", "") %> @oscar.formDB  dbType="tinyint(1)"/> Unspecified

		</td>
	  </tr>
	  </table>

      </td>
	</tr>
	<tr>
	  <td>

      <table width="100%" border="0"  cellspacing="0" cellpadding="0">
      <tr>
        <td align="right">[in min.]</td>
		<td>
        <input type="checkbox" name="resOxygen" <%= props.getProperty("resOxygen", "") %> @oscar.formDB  dbType="tinyint(1)"/> Oxygen
		</td>
		<td>
        <input type="checkbox" name="resIPPVMask" <%= props.getProperty("resIPPVMask", "") %> @oscar.formDB  dbType="tinyint(1)"/> IPPV Mask
		</td>
		<td>
        <input type="checkbox" name="resIPPVETT" <%= props.getProperty("resIPPVETT", "") %> @oscar.formDB  dbType="tinyint(1)"/> IPPV ETT
		</td>
		<td>
        <input type="checkbox" name="resCompress" <%= props.getProperty("resCompress", "") %> @oscar.formDB  dbType="tinyint(1)"/> Compress
		</td>
		<td>
	  </tr><tr>
	    <td align="right">Start Age</td>
		<td>
        <input type="text" name="resOxyStart" size="10" maxlength="10" value="<%= props.getProperty("resOxyStart", "") %>" @oscar.formDB />
		</td>
		<td>
        <input type="text" name="resIPPVMStart" size="10" maxlength="10" value="<%= props.getProperty("resIPPVMStart", "") %>" @oscar.formDB />
		</td>
		<td>
        <input type="text" name="resIPPVEStart" size="10" maxlength="10" value="<%= props.getProperty("resIPPVEStart", "") %>" @oscar.formDB />
		</td>
		<td>
        <input type="text" name="resCompStart" size="10" maxlength="10" value="<%= props.getProperty("resCompStart", "") %>" @oscar.formDB />
		</td>
	  </tr><tr>
	    <td align="right">Stop Age</td>
		<td>
        <input type="text" name="resOxyStop" size="10" maxlength="10" value="<%= props.getProperty("resOxyStop", "") %>" @oscar.formDB />
		</td>
		<td>
        <input type="text" name="resIPPVMStop" size="10" maxlength="10" value="<%= props.getProperty("resIPPVMStop", "") %>" @oscar.formDB />
		</td>
		<td>
        <input type="text" name="resIPPVEStop" size="10" maxlength="10" value="<%= props.getProperty("resIPPVEStop", "") %>" @oscar.formDB />
		</td>
		<td>
        <input type="text" name="resCompStop" size="10" maxlength="10" value="<%= props.getProperty("resCompStop", "") %>" @oscar.formDB />
		</td>
	  </tr>
	  </table>

	  </td>
	</tr>
  </table>



  </td><td valign="top">


  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <th align="left">Gestational Age
      </th>
    </tr><tr>
	  <td align="center">By Exam<br>
      <input type="text" name="gestByExam" size="10" maxlength="10" value="<%= props.getProperty("gestByExam", "") %>" @oscar.formDB />
      <br>
	  From Mat Record<br>
      <input type="text" name="gestFromMat" size="10" maxlength="10" value="<%= props.getProperty("gestFromMat", "") %>" @oscar.formDB />
	  </td>
	</tr>
  </table>


  </td>
</tr>
</table>


<table width="80%" border="0"  cellspacing="6" cellpadding="0"><tr><td>
<table width="100%" border="1"  cellspacing="0" cellpadding="0">
  <tr>
    <td width="30%" nowrap><B>Vitamin K</B><br>
    <input type="radio" name="vitKY" <%= props.getProperty("vitKY", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "vitK")' /> Y
    <input type="radio" name="vitKN" <%= props.getProperty("vitKN", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "vitK")' /> N
    <input type="radio" name="vitKUnknown" <%= props.getProperty("vitKUnknown", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "vitK")' /> Unknown
    </td>

    <td width="35%" nowrap><B>Breast Feed at Discharge</B><br>
    <input type="radio" name="breFedY" <%= props.getProperty("breFedY", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "breFed")' /> Y
    <input type="radio" name="breFedN" <%= props.getProperty("breFedN", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "breFed")' /> N
    <input type="radio" name="breFedUnknown" <%= props.getProperty("breFedUnknown", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "breFed")' /> Unknown
    </td>
	
    <td rowspan="2" valign="top">
	
    <table width="100%" border="0"  cellspacing="0" cellpadding="0">
      <tr>
        <td colspan="2" nowrap><B>Discharged To</B></td>
      </tr><tr>
	    <td width="50%">
        <input type="radio" name="dischHome" <%= props.getProperty("dischHome", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "disch")' /> Home
		</td><td>
        <input type="radio" name="dischOtherHosp" <%= props.getProperty("dischOtherHosp", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "disch")' /> Other Hospital
      </tr><tr>
	    <td width="50%">
        <input type="radio" name="dischAdoption" <%= props.getProperty("dischAdoption", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "disch")' /> Adoption
		</td><td>
        <input type="radio" name="dischDeath" <%= props.getProperty("dischDeath", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "disch")' /> Death / S.B.
      </tr><tr>
	    <td width="50%">
        <input type="radio" name="dischFoster" <%= props.getProperty("dischFoster", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "disch")' /> Foster Home
		</td><td>
        <input type="radio" name="dischUnknown" <%= props.getProperty("dischUnknown", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "disch")' /> Unknown
	  </tr>
	</table>

	</td>
  </tr><tr>
    <td nowrap><B>Eye Prophylaxis</B><br>
    <input type="radio" name="eyeProY" <%= props.getProperty("eyeProY", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "eyePro")' /> Y
    <input type="radio" name="eyeProN" <%= props.getProperty("eyeProN", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "eyePro")' /> N
    <input type="radio" name="eyeProUnknown" <%= props.getProperty("eyeProUnknown", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "eyePro")' /> Unknown
    </td>

	<td><B>Discharge Weight</B><br>
    <input type="text" name="discWeight" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("discWeight", "") %>" @oscar.formDB />
    </td>
  </tr>
</table>
</td></tr></table>




<table width="80%" border="0"  cellspacing="6" cellpadding="0"> <tr> <td>
<table width="100%" border="1"  cellspacing="0" cellpadding="0">
  <tr>
    <td width="50%">
	
    <table width="100%" border="0"  cellspacing="0" cellpadding="0">
      <tr align="center">
      <td width="33%">Ventilator Days</td>
      <td width="33%">TPN Days</td>
      <td >Oxygen Days</td>
	  </tr><tr align="center">
	  <td>
      <input type="text" name="ventilDay" size="10" maxlength="10" value="<%= props.getProperty("ventilDay", "") %>" @oscar.formDB />
	  </td><td>
      <input type="text" name="tpnDay" size="10" maxlength="10" value="<%= props.getProperty("tpnDay", "") %>" @oscar.formDB />
	  </td><td>
      <input type="text" name="oxygenDay" size="10" maxlength="10" value="<%= props.getProperty("oxygenDay", "") %>" @oscar.formDB />
	  </td><td>
	  </tr>
	</table>

	</td><td valign="top">
	
    <table width="100%" border="0"  cellspacing="0" cellpadding="0">
      <tr>
      <td colspan="2"><B>Cord Arterial Gases</B></td>
	  </tr><tr align="center">
	  <td width="50%">pH
      <input type="text" name="cordArtGasPh" size="10" maxlength="10" value="<%= props.getProperty("cordArtGasPh", "") %>" @oscar.formDB />
	  </td><td>Base E/D
      <input type="text" name="cordArtGasED" size="10" maxlength="10" value="<%= props.getProperty("cordArtGasED", "") %>" @oscar.formDB />
	  </td>
	  </tr>
	</table>
	
	</td>
  </tr>
</table>
</td></tr></table>

</center>

</html:form>
</body>
</html:html>
