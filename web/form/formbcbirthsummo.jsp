
<%@ page language="java"%>
<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<%
	String formClass = "BCBrithSumMo";
	String formLink = "formbcbirthsummo.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(demoNo, formId);

    FrmData fd = new FrmData();
    String resource = fd.getResource();
    resource = resource + "ob/riskinfo/";
    //props.setProperty("c_lastVisited", "pg1");

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
    <title>Birth Summary (Mother:)</title>
    <link rel="stylesheet" type="text/css" href="bcArStyle.css" >
    <html:base/>
</head>

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
        if(valDate(document.forms[0].romDate)==false){
            b = false;
        } else if(valDate(document.forms[0].fstStqDate)==false){
            b = false;
        } else if(valDate(document.forms[0].secStqDate)==false){
            b = false;
        } else if(valDate(document.forms[0].deliDate)==false){
            b = false;
        } else if(valDate(document.forms[0].placentaDate)==false){
            b = false;
        } 

        return b;
    }
</script>

<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1"  onLoad="setfocus()">
<!--
@oscar.formDB Table="formBCBirthSumMo" 
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
            <input type="button" value="Exit" onclick="javascript:return onExit();"/>
            <input type="button" value="Print" onclick="javascript:window.print();"/>
        </td>
    </tr>
</table>

<table width="100%" border="0"  cellspacing="3" cellpadding="0">
<tr>
	<th><%=bView?"<font color='yellow'>VIEW PAGE: </font>" : ""%>
	BIRTH SUMMARY (Mother: <%= props.getProperty("c_surname", "") %>, <%= props.getProperty("c_givenName", "") %> )</th>
    <input type="hidden" name="c_surname" size="30" value="<%= props.getProperty("c_surname", "") %>" maxlength="30" @oscar.formDB /> 
    <input type="hidden" name="c_givenName" size="30" value="<%= props.getProperty("c_givenName", "") %>" maxlength="30" @oscar.formDB /> 
</tr>
</table>





<table width="100%" border="0"  cellspacing="3" cellpadding="0">
<tr><td width="14%" valign="top">

  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <td><b>Baby Seq</b> 
      </td>
	</tr><tr>
	  <td>
      <input type="radio" name="babySeqSingleton" <%= props.getProperty("babySeqSingleton", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "babySeq")' />Singleton
      </td>
	</tr><tr>
	  <td>
      <input type="radio" name="babySeqTwinA" <%= props.getProperty("babySeqTwinA", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "babySeq")' />Twin A
      </td>
	</tr><tr>
	  <td>
      <input type="radio" name="babySeqTwinB" <%= props.getProperty("babySeqTwinB", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "babySeq")' />Twin B
      </td>
	</tr><tr>
	  <td>
      <input type="radio" name="babySeqTriA" <%= props.getProperty("babySeqTriA", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "babySeq")' />Triplet A
      </td>
	</tr><tr>
	  <td>
      <input type="radio" name="babySeqTriB" <%= props.getProperty("babySeqTriB", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "babySeq")' />Triplet B
      </td>
	</tr><tr>
	  <td>
      <input type="radio" name="babySeqTriC" <%= props.getProperty("babySeqTriC", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "babySeq")' />Triplet C
      </td>

	</tr><tr>
	  <td>
      <input type="radio" name="babySeqOther" <%= props.getProperty("babySeqOther", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "babySeq")' />Other
      </td>
	</tr><tr>
	  <td align="right" nowrap>
      <input type="text" name="babySeqOf1" size="2" maxlength="2" value="<%= props.getProperty("babySeqOf1", "") %>" @oscar.formDB /> 
	  of
      <input type="text" name="babySeqOf2" size="2" maxlength="2" value="<%= props.getProperty("babySeqOf2", "") %>" @oscar.formDB /> 
      </td>
    </tr>
  </table>
  

  </td><td valign="top" width="20%">


  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <td nowrap>*R.O.M. Date/Time<br>
      <input type="text" name="romDate" size="10" maxlength="10" value="<%= props.getProperty("romDate", "") %>" @oscar.formDB  dbType="date" />
      <input type="text" name="romTime" size="5" maxlength="5" value="<%= props.getProperty("romTime", "") %>" @oscar.formDB  />
      </td>
    </tr>
    <tr>
      <td>1st Stq Date/Time<br>
      <input type="text" name="fstStqDate" size="10" maxlength="10" value="<%= props.getProperty("fstStqDate", "") %>" @oscar.formDB  dbType="date" />
      <input type="text" name="fstStqTime" size="5" maxlength="5" value="<%= props.getProperty("fstStqTime", "") %>" @oscar.formDB  />
      </td>
    </tr>
    <tr>
      <td>2nd Stq Date/Time<br>
      <input type="text" name="secStqDate" size="10" maxlength="10" value="<%= props.getProperty("secStqDate", "") %>" @oscar.formDB  dbType="date" />
      <input type="text" name="secStqTime" size="5" maxlength="5" value="<%= props.getProperty("secStqTime", "") %>" @oscar.formDB  />
      </td>
    </tr>
    <tr>
      <td>Delivery Date/Time<br>
      <input type="text" name="deliDate" size="10" maxlength="10" value="<%= props.getProperty("deliDate", "") %>" @oscar.formDB  dbType="date" />
      <input type="text" name="deliTime" size="5" maxlength="5" value="<%= props.getProperty("deliTime", "") %>" @oscar.formDB  />
      </td>
    </tr>
    <tr>
      <td>Placenta Date/Time<br>
      <input type="text" name="placentaDate" size="10" maxlength="10" value="<%= props.getProperty("placentaDate", "") %>" @oscar.formDB  dbType="date" />
      <input type="text" name="placentaTime" size="5" maxlength="5" value="<%= props.getProperty("placentaTime", "") %>" @oscar.formDB  />
      </td>
    </tr>
  </table>
  

  </td><td valign="top" width="33%">



  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <td>Labour Position/Presentation<br>
      <input type="text" name="labourPosition" size="15" maxlength="15" value="<%= props.getProperty("labourPosition", "") %>" @oscar.formDB />
      <input type="text" name="labourPresentation" size="20" maxlength="20" value="<%= props.getProperty("labourPresentation", "") %>" @oscar.formDB />
      </td>
    </tr>
    <tr>
      <td>Delivery Position/Presentation<br>
      <input type="text" name="deliveryPosition" size="15" maxlength="15" value="<%= props.getProperty("deliveryPosition", "") %>" @oscar.formDB />
      <input type="text" name="deliveryPresentation" size="20" maxlength="20" value="<%= props.getProperty("deliveryPresentation", "") %>" @oscar.formDB />
      </td>
    </tr>
    <tr>
      <td>Delivered By<br>
      <input type="text" name="deliveredBy" size="40" maxlength="60" value="<%= props.getProperty("deliveredBy", "") %>" @oscar.formDB />
      </td>
    </tr>
    <tr>
      <td>Primary Indic. Operative Deliv<br>
      <input type="text" name="priIndicOpeDeliv" size="40" maxlength="60" value="<%= props.getProperty("priIndicOpeDeliv", "") %>" @oscar.formDB />
      </td>
    </tr>
  </table>

  </td><td valign="top">

  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <td>Cesarean Section Type<br>
      <input type="text" name="cesaSectType" style="width:100%" size="50" maxlength="60" value="<%= props.getProperty("cesaSectType", "") %>" @oscar.formDB />
      </td>
    </tr>
    <tr>
      <td>Cesarean Incision<br>
      <input type="text" name="cesaIncision" style="width:100%" size="50" maxlength="60" value="<%= props.getProperty("cesaIncision", "") %>" @oscar.formDB />
      </td>
    </tr>
    <tr><td>&nbsp;<br>&nbsp;</td></tr>
	<tr>
	  <td>

      <table width="100%" border="0"  cellspacing="0" cellpadding="0">
      <tr>
        <th colspan="3" align="left">VBAC Attempted</th>
      </tr><tr>
        <td>
        <input type="radio" name="vbacYes" <%= props.getProperty("vbacYes", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheck(this, "vbac")' />Yes
        </td>
        <td>
        <input type="radio" name="vbacNo" <%= props.getProperty("vbacNo", "") %> @oscar.formDB  dbType="tinyint(1)" onclick='onCheck(this, "vbac")'/>No
        </td>
        <td>
        <input type="radio" name="vbacNA" <%= props.getProperty("vbacNA", "") %> @oscar.formDB  dbType="tinyint(1)" onclick='onCheck(this, "vbac")'/>N/A
        </td>
      </tr><tr>
        <td colspan="3">
        <input type="radio" name="vbacUnknown" <%= props.getProperty("vbacUnknown", "") %> @oscar.formDB  dbType="tinyint(1)" onclick='onCheck(this, "vbac")'/>Unknown
        </td>
	  </tr>
	  </table>

      </td>	
	</tr>
  </table>
  
  </td>
  </tr>
</table>



<table width="100%" border="0"  cellspacing="3" cellpadding="0">
<tr>
  <td valign="top" width="34%">
  
  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <th colspan="2" align="left">Labour Initiation
      </th>
	</tr>
    <tr>
	  <td width="50%">
      <input type="checkbox" name="labInitNoLab" <%= props.getProperty("labInitNoLab", "") %> @oscar.formDB  dbType="tinyint(1)"/> No Labour
	  </td>
	  <td>
      <input type="checkbox" name="labInitUnknown" <%= props.getProperty("labInitUnknown", "") %> @oscar.formDB  dbType="tinyint(1)"/> Unknown
	  </td>
	</tr>
    <tr>
      <td colspan="2">
      <input type="checkbox" name="labInitSpontan" <%= props.getProperty("labInitSpontan", "") %> @oscar.formDB  dbType="tinyint(1)"/> Spontaneous
      </td>
    </tr>
    <tr>
	  <td>
      <input type="checkbox" name="labInitAugmented" <%= props.getProperty("labInitAugmented", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheckMaster(this, "labInitAugmented")' /> Augmented
	  </td>
	  <td>
      <input type="checkbox" name="labInitInduced" <%= props.getProperty("labInitInduced", "") %> @oscar.formDB  dbType="tinyint(1)" onclick='onCheckMaster(this, "labInitInduced")' /> Induced
	  </td>
	</tr>

    <tr>
	  <td>&nbsp;&nbsp;&nbsp;
      <input type="checkbox" name="labInitAugmentedARM" <%= props.getProperty("labInitAugmentedARM", "") %> @oscar.formDB  dbType="tinyint(1)" onclick='onCheckSlave(this, "labInitAugmented")' /> ARM
	  </td>
	  <td>&nbsp;&nbsp;&nbsp;
      <input type="checkbox" name="labInitInducedARM" <%= props.getProperty("labInitInducedARM", "") %> @oscar.formDB  dbType="tinyint(1)" onclick='onCheckSlave(this, "labInitInduced")' /> ARM
	  </td>
	</tr>
    <tr>
	  <td>&nbsp;&nbsp;&nbsp;
      <input type="checkbox" name="labInitAugmentedOxytocin" <%= props.getProperty("labInitAugmentedOxytocin", "") %> @oscar.formDB  dbType="tinyint(1)" onclick='onCheckSlave(this, "labInitAugmented")' /> Oxytocin
	  </td>
	  <td>&nbsp;&nbsp;&nbsp;
      <input type="checkbox" name="labInitInducedOxytocin" <%= props.getProperty("labInitInducedOxytocin", "") %> @oscar.formDB  dbType="tinyint(1)" onclick='onCheckSlave(this, "labInitInduced")' /> Oxytocin
	  </td>
	</tr>
    <tr>
	  <td>&nbsp;&nbsp;&nbsp;
      <input type="checkbox" name="labInitAugmentedProst" <%= props.getProperty("labInitAugmentedProst", "") %> @oscar.formDB  dbType="tinyint(1)" onclick='onCheckSlave(this, "labInitAugmented")' /> Prost.
	  </td>
	  <td>&nbsp;&nbsp;&nbsp;
      <input type="checkbox" name="labInitInducedProst" <%= props.getProperty("labInitInducedProst", "") %> @oscar.formDB  dbType="tinyint(1)" onclick='onCheckSlave(this, "labInitInduced")' /> Prost.
	  </td>
	</tr>
    <tr>
	  <td>&nbsp;&nbsp;&nbsp;
      <input type="checkbox" name="labInitAugmentedOther" <%= props.getProperty("labInitAugmentedOther", "") %> @oscar.formDB  dbType="tinyint(1)" onclick='onCheckSlave(this, "labInitAugmented")' /> Other
	  </td>
	  <td>&nbsp;&nbsp;&nbsp;
      <input type="checkbox" name="labInitInducedOther" <%= props.getProperty("labInitInducedOther", "") %> @oscar.formDB  dbType="tinyint(1)" onclick='onCheckSlave(this, "labInitInduced")' /> Other
	  </td>
	</tr>
    <tr>
	  <td colspan="2" align="right">
      Indication For Induction<br>
      <input type="text" name="labInitIndicInduc" size="30" maxlength="60" value="<%= props.getProperty("labInitIndicInduc", "") %>" @oscar.formDB />
	  </td>
	</tr>
  </table>


  </td><td valign="top" width="33%">


  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <th colspan="2" align="left">Perineum/Vagina/Cervix
      </th>
	</tr>
    <tr>
	  <td width="50%">
      <input type="checkbox" name="periVagCerIntact" <%= props.getProperty("periVagCerIntact", "") %> @oscar.formDB  dbType="tinyint(1)"/> Intact
	  </td>
	  <td>
      <input type="checkbox" name="periVagCerUnknown" <%= props.getProperty("periVagCerUnknown", "") %> @oscar.formDB  dbType="tinyint(1)"/> Unknown
	  </td>
	</tr>
    <tr>
	  <td>
      <input type="checkbox" name="periVagCerEpisiotomy" <%= props.getProperty("periVagCerEpisiotomy", "") %> @oscar.formDB  dbType="tinyint(1)" onclick='onCheckMaster(this, "pvcEpis")' /> Episiotomy
	  </td>
	  <td>
      <input type="checkbox" name="periVagCerLace" <%= props.getProperty("periVagCerLace", "") %> @oscar.formDB  dbType="tinyint(1)" onclick='onCheckMaster(this, "pvcLace")' /> Laceration
	  </td>
	</tr>

    <tr>
	  <td>&nbsp;&nbsp;&nbsp;
      <input type="radio" name="pvcEpisMedian" <%= props.getProperty("pvcEpisMedian", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheckSlave(this, "periVagCerEpisiotomy"); onCheck(this, "pvcEpis")' /> Median
	  </td>
	  <td>&nbsp;&nbsp;&nbsp;
      <input type="radio" name="pvcLace1st" <%= props.getProperty("pvcLace1st", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheckSlave(this, "periVagCerLace"); onCheck(this, "pvcLace")'/> 1st
      <input type="radio" name="pvcLace3rd" <%= props.getProperty("pvcLace3rd", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheckSlave(this, "periVagCerLace"); onCheck(this, "pvcLace")'/> 3rd
	  </td>
	</tr>
    <tr>
	  <td>&nbsp;&nbsp;&nbsp;
      <input type="radio" name="pvcEpisMediol" <%= props.getProperty("pvcEpisMediol", "") %> @oscar.formDB  dbType="tinyint(1)"  onclick='onCheckSlave(this, "periVagCerEpisiotomy"); onCheck(this, "pvcEpis")'/> Mediolateral
	  </td>
	  <td>&nbsp;&nbsp;&nbsp;
      <input type="radio" name="pvcLace2nd" <%= props.getProperty("pvcLace2nd", "") %> @oscar.formDB  dbType="tinyint(1)" onclick='onCheckSlave(this, "periVagCerLace"); onCheck(this, "pvcLace")'/> 2nd
      <input type="radio" name="pvcLace4th" <%= props.getProperty("pvcLace4th", "") %> @oscar.formDB  dbType="tinyint(1)" onclick='onCheckSlave(this, "periVagCerLace"); onCheck(this, "pvcLace")'/> 4th
	  </td>
	</tr>
    <tr>
	  <td colspan="2">
      <input type="checkbox" name="periVagCerCerv" <%= props.getProperty("periVagCerCerv", "") %> @oscar.formDB  dbType="tinyint(1)"/> 
	  Cervical tear
	  </td>
	</tr>
    <tr>
	  <td colspan="2">
      <input type="checkbox" name="periVagCerOther" <%= props.getProperty("periVagCerOther", "") %> @oscar.formDB  dbType="tinyint(1)"/> 
	  Other tear
	  </td>
	</tr>
  </table>

  </td><td valign="top">

  
  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <th colspan="2" align="left">Anesthetic / Analgesia
      </th>
	</tr>
    <tr>
	  <td width="50%">
      <input type="checkbox" name="aneAnaNone" <%= props.getProperty("aneAnaNone", "") %> @oscar.formDB  dbType="tinyint(1)"/> None
	  </td>
	  <td>
      <input type="checkbox" name="aneAnaSpinal" <%= props.getProperty("aneAnaSpinal", "") %> @oscar.formDB  dbType="tinyint(1)"/> Spinal
	  </td>
	</tr>
    <tr>
	  <td>
      <input type="checkbox" name="aneAnaEntonox" <%= props.getProperty("aneAnaEntonox", "") %> @oscar.formDB  dbType="tinyint(1)"/> Entonox
	  </td>
	  <td>
      <input type="checkbox" name="aneAnaGeneral" <%= props.getProperty("aneAnaGeneral", "") %> @oscar.formDB  dbType="tinyint(1)"/> General
	  </td>
	</tr>
    <tr>
	  <td>
      <input type="checkbox" name="aneAnaLocal" <%= props.getProperty("aneAnaLocal", "") %> @oscar.formDB  dbType="tinyint(1)"/> Local
	  </td>
	  <td>
      <input type="checkbox" name="aneAnaNarcotics" <%= props.getProperty("aneAnaNarcotics", "") %> @oscar.formDB  dbType="tinyint(1)"/> Narcotics
	  </td>
	</tr>
    <tr>
	  <td>
      <input type="checkbox" name="aneAnaPudendal" <%= props.getProperty("aneAnaPudendal", "") %> @oscar.formDB  dbType="tinyint(1)"/> Pudendal
	  </td>
	  <td>
      <input type="checkbox" name="aneAnaOther" <%= props.getProperty("aneAnaOther", "") %> @oscar.formDB  dbType="tinyint(1)"/> Other
	  </td>
	</tr>
    <tr>
	  <td>
      <input type="checkbox" name="aneAnaEpidural" <%= props.getProperty("aneAnaEpidural", "") %> @oscar.formDB  dbType="tinyint(1)"/> Epidural
	  </td>
	  <td>
      <input type="checkbox" name="aneAnaUnknown" <%= props.getProperty("aneAnaUnknown", "") %> @oscar.formDB  dbType="tinyint(1)"/> Unknown
	  </td>
	</tr>
  </table>

  

  </td>
</tr>
</table>  

* Date format:  <span class="small8">(dd/mm/yyyy)</span>


</html:form>
</body>
</html:html>
