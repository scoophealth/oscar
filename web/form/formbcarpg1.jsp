
<%@ page language="java"%>
<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<%
	String formClass = "BCAR";
	String formLink = "formbcarpg1.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(demoNo, formId);

    FrmData fd = new FrmData();
    String resource = fd.getResource();
    resource = resource + "ob/riskinfo/";
    props.setProperty("c_lastVisited", "pg1");

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
    <title>Antenatal Record 1</title>
    <link rel="stylesheet" type="text/css" href="<%=bView?"bcArStyleView.css" : "bcArStyle.css"%>">
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

<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1">
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
<input type="hidden" name="c_lastVisited" value=<%=props.getProperty("c_lastVisited", "pg1")%> />
<input type="hidden" name="demographic_no" value="<%= props.getProperty("demographic_no", "0") %>" />
<input type="hidden" name="formCreated" value="<%= props.getProperty("formCreated", "") %>" />
<input type="hidden" name="form_class" value="<%=formClass%>" />
<input type="hidden" name="form_link" value="<%=formLink%>" />
<input type="hidden" name="formId" value="<%=formId%>" />
<input type="hidden" name="provider_no" value=<%=request.getParameter("provNo")%> />
<input type="hidden" name="provNo" value="<%= request.getParameter("provNo") %>" />
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
         <!--td>
           <a href="javascript: popPage('formlabreq.jsp?demographic_no=<%=demoNo%>&formId=0&provNo=<%=provNo%>&labType=AR','LabReq');">LAB</a>
        </td-->

        <td align="right"><b>View:</b> 
            <a href="javascript: popupPage('formbcarpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2 <font size=-2>(pg.1)</font></a> |
            <a href="javascript: popupPage('formbcarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2 <font size=-2>(pg.2)</font></a>
            &nbsp;
        </td>
        <td align="right"><b>Edit:</b>AR1 |
            <a href="formbcarpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2 <font size=-2>(pg.1)</font></a> |
            <a href="formbcarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2 <font size=-2>(pg.2)</font></a> |
            <!--a href="javascript: popupFixedPage(700,950,'../decision/antenatal/antenatalplanner.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">AR Planner</a-->
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
	  British Columbia Antenatal Record Part 1</th>
    </tr>
  </table>

  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <td width="30%"><b>1.</b> HOSPITAL<br>
      <input type="text" name="c_hospital" style="width:100%" size="30" maxlength="60" value="<%= props.getProperty("c_hospital", "") %>" @oscar.formDB />
      </td>
	  <td width="33%">PRIMARY CARE GIVER<br>
      <input type="text" name="pg1_priCare" style="width:100%" size="30" maxlength="60" value="<%= props.getProperty("pg1_priCare", "") %>" @oscar.formDB />
      </td><td colspan="2">FAMILY PHYSICIAN<br>
      <input type="text" name="pg1_famPhy" style="width:100%" size="30" maxlength="60" value="<%= props.getProperty("pg1_famPhy", "") %>" @oscar.formDB />
      </td>
    </tr><tr>
      <td colspan='2'>MOTHER'S NAME
      <input type="text" name="pg1_moName" style="width:100%" size="60" maxlength="60" value="<%= props.getProperty("pg1_moName", "") %>" @oscar.formDB />
      </td>
      <td width="20%">DATE OF BIRTH<br>dd/mm/yyyy
      <input type="text" name="pg1_dateOfBirth" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_dateOfBirth", "") %>" @oscar.formDB dbType="date" />
      </td>
	  <td>AGE AT EDD<br>
      <input type="text" name="pg1_ageAtEDD" style="width:100%" size="2" maxlength="2" value="<%= props.getProperty("pg1_ageAtEDD", "") %>" @oscar.formDB  />
	  </td>
    </tr>
  </table>
  
  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <td width="40%">MOTHER'S MAIDEN NAME<br>
      <input type="text" name="pg1_maidenName" style="width:100%" size="30" maxlength="60" value="<%= props.getProperty("pg1_maidenName", "") %>" @oscar.formDB  />
      </td>
	  <td width="33%">ETHNIC ORIGIN<br>
      <input type="text" name="pg1_ethOrig" style="width:100%" size="30" maxlength="60" value="<%= props.getProperty("pg1_ethOrig", "") %>" @oscar.formDB  />
	  </td>
	  <td><span class="small9">LANGUAGE PREFERRED</span><br>
      <input type="text" name="pg1_langPref" style="width:100%" size="30" maxlength="60" value="<%= props.getProperty("pg1_langPref", "") %>" @oscar.formDB  />
	  </td>
    </tr>
  </table>

  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
      <td width="50%">PARTNER'S NAME<br>
      <input type="text" name="pg1_partnerName" style="width:100%" size="30" maxlength="60" value="<%= props.getProperty("pg1_partnerName", "") %>" @oscar.formDB  />
      </td>
	  <td width="5%">AGE
      <input type="text" name="pg1_partnerAge" style="width:100%" size="2" maxlength="2" value="<%= props.getProperty("pg1_partnerAge", "") %>" @oscar.formDB  />
	  </td>
	  <td><span class="small9">ETHNIC ORIGIN OF NEWBORN’S FATHER</span>
      <input type="text" name="pg1_faEthOrig" style="width:100%" size="30" maxlength="50" value="<%= props.getProperty("pg1_faEthOrig", "") %>" @oscar.formDB  />
	  </td>
    </tr>
  </table>

  </td>
  <td>

  <table width="100%" border="0"  cellspacing="0" cellpadding="0">
    <tr>
      <td colspan="2">DATE<br>
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
      <td>PERSONAL HEALTH NUMBER<br>
      <input type="text" name="c_phn" style="width:100%" size="20" maxlength="20" value="<%= props.getProperty("c_phn", "") %>" @oscar.formDB />
	  </td>
	  <td><span class="small9">PHYSICIAN / MIDWIFE NAME</span><br>
      <input type="text" name="c_phyMid" style="width:100%" size="30" maxlength="60" value="<%= props.getProperty("c_phyMid", "") %>" @oscar.formDB />
	  </td>
    </tr>
  </table>
  
  </td>
  </tr>
</table>

<table class="shrinkMe" width="100%" border="0"  cellspacing="0" cellpadding="0">
<tr>
  <td colspan="3"><b>2. 
INFORMED CONSENT </b><span class="small8">(in compliance with the Freedom of Information and Protection of Privacy Act, Oct. 1993) . I understand that providing this information is necessary to
assist the physician/midwife in planning my care throughout pregnancy, childbirth and postpartum; my personal information will be kept private. I also understand this
information may be reviewed when necessary by other health professionals directly involved in my care. This information is collected in accordance with the provisions of the
Freedom of Information and the Protection of Privacy Act by the Perinatal Database Registry, an integral part of the Ministry of Health supported and funded British Columbia
Reproductive Care Program. I understand that I can ask my care provider if I have any questions regarding the collection and use of this information.</span>
  </td>
</tr><tr>
  <td width="45%"><i>Mother's Signature:</i>
  <input type="text" name="pg1_moSign" size="40" maxlength="60" value="<%= props.getProperty("pg1_moSign", "") %>" @oscar.formDB />
  </td>
  <td width="40%"><i>Witness:</i>
  <input type="text" name="pg1_witness" size="40" maxlength="60" value="<%= props.getProperty("pg1_witness", "") %>" @oscar.formDB />
  </td>
  <td><i>Date:</i>
  <input type="text" name="pg1_signDate" size="10" maxlength="10" value="<%= props.getProperty("pg1_signDate", "") %>" @oscar.formDB dbType="date"/>
  </td>
</tr>
</table>  

<table width="100%" border="1"  cellspacing="0" cellpadding="0">
<tr>
  <td colspan="6"><b>3. OBSTETRICAL HISTORY INCLUDING ABORTIONS 
	G</b><span class="small8">ravida</span> <input type="text" name="pg1_gravida" size="4" maxlength="4" value="<%= props.getProperty("pg1_gravida", "") %>" @oscar.formDB />
	<b>T</b><span class="small8">erm</span> <input type="text" name="pg1_term" size="3" maxlength="4" value="<%= props.getProperty("pg1_term", "") %>" @oscar.formDB /> 
    <b>P</b><span class="small8">reterm</span> <input type="text" name="pg1_preterm" size="3" maxlength="4" value="<%= props.getProperty("pg1_preterm", "") %>" @oscar.formDB /> 
	<b>A</b><span class="small8">bortion</span> <input type="text" name="pg1_abortion" size="3" maxlength="3" value="<%= props.getProperty("pg1_abortion", "") %>" @oscar.formDB />  
	<b>L</b><span class="small8">iving</span> <input type="text" name="pg1_living" size="3" maxlength="3" value="<%= props.getProperty("pg1_living", "") %>" @oscar.formDB />  
	</td>
  <td colspan="3" align="center"><b>CHILDREN</b></td>
</tr><tr>
  <th width="8%">DATE</th>
  <th width="15%">HOSPITAL OF BIRTH<br>OR ABORTION</th>
  <th width="8%">WEEKS AT<br>DELIVERY</th>
  <th width="8%">HRS.IN<br>ACTIVE<br>LABOUR</th>
  <th width="8%">DELIVERY<br>TYPE</th>
  
  <th width="33%">PERINATAL COMPLICATIONS</th>
  <th width="3%">SEX</th>
  <th width="8%">BIRTH<br>WEIGHT</th>
  <th width="8%">PRESENT<br>HEALTH</th>
</tr><tr>
  <td>
  <input type="text" name="pg1_obHistDate1" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_obHistDate1", "") %>" @oscar.formDB dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg1_birthOrAbort1" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_birthOrAbort1", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_deliWeek1" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg1_deliWeek1", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_laboHr1" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg1_laboHr1", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_deliType1" style="width:100%" size="10" maxlength="15" value="<%= props.getProperty("pg1_deliType1", "") %>" @oscar.formDB />
  </td>
  
  <td>
  <input type="text" name="pg1_periComp1" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg1_periComp1", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_obHistSex1" style="width:100%" size="1" maxlength="1" value="<%= props.getProperty("pg1_obHistSex1", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_birthWeit1" style="width:100%" size="6" maxlength="8" value="<%= props.getProperty("pg1_birthWeit1", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_presHealth1" style="width:100%" size="6" maxlength="8" value="<%= props.getProperty("pg1_presHealth1", "") %>" @oscar.formDB />
  </td>
</tr><tr>
  <td>
  <input type="text" name="pg1_obHistDate2" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_obHistDate2", "") %>" @oscar.formDB dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg1_birthOrAbort2" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_birthOrAbort2", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_deliWeek2" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg1_deliWeek2", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_laboHr2" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg1_laboHr2", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_deliType2" style="width:100%" size="10" maxlength="15" value="<%= props.getProperty("pg1_deliType2", "") %>" @oscar.formDB />
  </td>
  
  <td>
  <input type="text" name="pg1_periComp2" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg1_periComp2", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_obHistSex2" style="width:100%" size="1" maxlength="1" value="<%= props.getProperty("pg1_obHistSex2", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_birthWeit2" style="width:100%" size="6" maxlength="8" value="<%= props.getProperty("pg1_birthWeit2", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_presHealth2" style="width:100%" size="6" maxlength="8" value="<%= props.getProperty("pg1_presHealth2", "") %>" @oscar.formDB />
  </td>
</tr><tr>
  <td>
  <input type="text" name="pg1_obHistDate3" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_obHistDate3", "") %>" @oscar.formDB dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg1_birthOrAbort3" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_birthOrAbort3", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_deliWeek3" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg1_deliWeek3", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_laboHr3" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg1_laboHr3", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_deliType3" style="width:100%" size="10" maxlength="15" value="<%= props.getProperty("pg1_deliType3", "") %>" @oscar.formDB />
  </td>
  
  <td>
  <input type="text" name="pg1_periComp3" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg1_periComp3", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_obHistSex3" style="width:100%" size="1" maxlength="1" value="<%= props.getProperty("pg1_obHistSex3", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_birthWeit3" style="width:100%" size="6" maxlength="8" value="<%= props.getProperty("pg1_birthWeit3", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_presHealth3" style="width:100%" size="6" maxlength="8" value="<%= props.getProperty("pg1_presHealth3", "") %>" @oscar.formDB />
  </td>
</tr><tr>
  <td>
  <input type="text" name="pg1_obHistDate4" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_obHistDate4", "") %>" @oscar.formDB dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg1_birthOrAbort4" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_birthOrAbort4", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_deliWeek4" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg1_deliWeek4", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_laboHr4" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg1_laboHr4", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_deliType4" style="width:100%" size="10" maxlength="15" value="<%= props.getProperty("pg1_deliType4", "") %>" @oscar.formDB />
  </td>
  
  <td>
  <input type="text" name="pg1_periComp4" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg1_periComp4", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_obHistSex4" style="width:100%" size="1" maxlength="1" value="<%= props.getProperty("pg1_obHistSex4", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_birthWeit4" style="width:100%" size="6" maxlength="8" value="<%= props.getProperty("pg1_birthWeit4", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_presHealth4" style="width:100%" size="6" maxlength="8" value="<%= props.getProperty("pg1_presHealth4", "") %>" @oscar.formDB />
  </td>
</tr><tr>
  <td>
  <input type="text" name="pg1_obHistDate5" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_obHistDate5", "") %>" @oscar.formDB dbType="date"/>
  </td>
  <td>
  <input type="text" name="pg1_birthOrAbort5" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_birthOrAbort5", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_deliWeek5" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg1_deliWeek5", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_laboHr5" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg1_laboHr5", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_deliType5" style="width:100%" size="10" maxlength="15" value="<%= props.getProperty("pg1_deliType5", "") %>" @oscar.formDB />
  </td>
  
  <td>
  <input type="text" name="pg1_periComp5" style="width:100%" size="50" maxlength="80" value="<%= props.getProperty("pg1_periComp5", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_obHistSex5" style="width:100%" size="1" maxlength="1" value="<%= props.getProperty("pg1_obHistSex5", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_birthWeit5" style="width:100%" size="6" maxlength="8" value="<%= props.getProperty("pg1_birthWeit5", "") %>" @oscar.formDB />
  </td>
  <td>
  <input type="text" name="pg1_presHealth5" style="width:100%" size="6" maxlength="8" value="<%= props.getProperty("pg1_presHealth5", "") %>" @oscar.formDB />
  </td>
</tr>
</table>

<table width="100%" border="1"  cellspacing="0" cellpadding="0">
<tr>
  <td width="33%">

  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
  <tr>
    <td width="30%"><b>4.</b> LMP<br> dd/mm/yyyy<br>
    <input type="text" name="pg1_lmp" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_lmp", "") %>" @oscar.formDB dbType="date"/>
	</td>
    <td width="30%">MENSES CYCLE<br>
    <input type="text" name="pg1_mensCycle" style="width:100%" size="8" maxlength="8" value="<%= props.getProperty("pg1_mensCycle", "") %>" @oscar.formDB />
	</td>
    <td>EDD BY DATES<br> dd/mm/yyyy<br>
    <input type="text" name="pg1_eddByDate" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_eddByDate", "") %>" @oscar.formDB dbType="date"/>
	</td>
  </tr>
  <tr>
    <td colspan="2">
      <table width="100%" border="0"  cellspacing="0" cellpadding="0">
      <tr><td width="30%"><span class="small8">CONTRACEPTION</font> <br>
	    METHOD:<br>
        <input type="text" name="pg1_contrMethod" style="width:100%" size="12" maxlength="15" value="<%= props.getProperty("pg1_contrMethod", "") %>" @oscar.formDB />
		</td><td width="30%"><span class="small8">WHEN STOPPED:</font><br>
		dd/mm/yyyy<br>
        <input type="text" name="pg1_stopDate" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_stopDate", "") %>" @oscar.formDB dbType="date"/>
		</td>
	  </tr>
	  </table>
	</td>
	<td>EDD BY US<br> dd/mm/yyyy<br>
    <input type="text" name="pg1_eddByUs" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_eddByUs", "") %>" @oscar.formDB dbType="date"/>
	</td>
  </tr>
  </table>

  </td>
  <td width="33%" valign="top">

  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
  <tr>
    <td><b>5. <font color="red">ALLERGIES</font></b>
    <input type="checkbox" name="pg1_allergyN" <%= props.getProperty("pg1_allergyN", "") %>  @oscar.formDB dbType="tinyint(1)"/> NONE KNOWN <br>
    <input type="checkbox" name="pg1_allergyY" <%= props.getProperty("pg1_allergyY", "") %>  @oscar.formDB dbType="tinyint(1)"/> YES (specify):
    <input type="text" name="pg1_allergy" size="25" maxlength="50" value="<%= props.getProperty("pg1_allergy", "") %>" @oscar.formDB />
	</td>
  </tr>
  <tr>
	<td><b>CURRENT MEDICATIONS</b>
    <textarea name="pg1_curMedic" style="width:100%" cols="30" rows="2" @oscar.formDB dbType="varchar(255)" > <%= props.getProperty("pg1_curMedic", "") %> </textarea>
	</td>
  </tr>
  </table>
  
  </td>
  <td valign="top">

  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
  <tr>
    <td><b>6.</b> BELIEFS & PRACTICES  <br>
    <input type="text" name="pg1_beliPract" style="width:100%" size="60" maxlength="60" value="<%= props.getProperty("pg1_beliPract", "") %>" @oscar.formDB />
	</td>
  </tr>
  <tr>
	<td>COMPLEMENTARY Rx's <br>
    <input type="text" name="pg1_compRx" style="width:100%" size="60" maxlength="60" value="<%= props.getProperty("pg1_compRx", "") %>" @oscar.formDB />
	</td>
  </tr>
  </table>

  </td>
</tr>
</table>

<table width="100%" border="1"  cellspacing="0" cellpadding="0">
<tr>
  <td width="33%" valign="top" >

  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
  <tr>
    <td>

	  <table class="shrinkMe"  width="100%" border="0"  cellspacing="0" cellpadding="0">
	  <tr>
		<td colspan="3"><b>7. PRESENT PREGNANCY</b></td>
	  </tr><tr>
		<td colspan="2"><i>no</i></td><td align="center"><i>yes (specify)</i></td>
	  </tr><tr>
		<td width="1%">
		<input type="checkbox" name="pg1_bleeding" <%= props.getProperty("pg1_bleeding", "") %>  @oscar.formDB dbType="tinyint(1)"/>
		</td>
		<td width="20%"><span class="small9">BLEEDING</span></td>
		<td>
		<input type="text" name="pg1_bleedingSpec" style="width:100%" size="30" maxlength="30" value="<%= props.getProperty("pg1_bleedingSpec", "") %>" @oscar.formDB />
		</td>
	  </tr><tr>
		<td>
		<input type="checkbox" name="pg1_nausea" <%= props.getProperty("pg1_nausea", "") %>  @oscar.formDB dbType="tinyint(1)"/>
		</td>
		<td><span class="small9">NAUSEA</span></td>
		<td>
		<input type="text" name="pg1_nauseaSpec" style="width:100%" size="30" maxlength="30" value="<%= props.getProperty("pg1_nauseaSpec", "") %>" @oscar.formDB />
		</td>
	  </tr><tr>
		<td>
		<input type="checkbox" name="pg1_infect" <%= props.getProperty("pg1_infect", "") %>  @oscar.formDB dbType="tinyint(1)"/>
		</td>
		<td><span class="small9">INFECTIONS<br>OR FEVER</span></td>
		<td>
		<input type="text" name="pg1_infectSpec" style="width:100%" size="30" maxlength="30" value="<%= props.getProperty("pg1_infectSpec", "") %>" @oscar.formDB />
		</td>
	  </tr><tr>
		<td>
		<input type="checkbox" name="pg1_depres" <%= props.getProperty("pg1_depres", "") %>  @oscar.formDB dbType="tinyint(1)"/>
		</td>
		<td><span class="small9">DEPRESSION</span></td>
		<td>
		<input type="text" name="pg1_depresSpec" style="width:100%" size="30" maxlength="30" value="<%= props.getProperty("pg1_depresSpec", "") %>" @oscar.formDB />
		</td>
	  </tr><tr>
		<td>
		<input type="checkbox" name="pg1_prePregOther" <%= props.getProperty("pg1_prePregOther", "") %>  @oscar.formDB dbType="tinyint(1)"/>
		</td>
		<td><span class="small9">OTHER</span></td>
		<td>
		<input type="text" name="pg1_prePregOtherSpec" style="width:100%" size="30" maxlength="30" value="<%= props.getProperty("pg1_prePregOtherSpec", "") %>" @oscar.formDB />
		</td>
	  </tr>
      </table>

    </td>
  </tr><tr>
    <td>

	  <table width="100%" border="0"  cellspacing="0" cellpadding="0">
	  <tr>
		<td colspan="2" nowrap><b>10. FAMILY HISTORY</b></td>
		<td><span class="small8">MATERNAL  PATERNAL</span></td>
	  </tr><tr>
		<td colspan="2">&nbsp;<i>no</i></td><td align="center"><i>yes (specify)</i></td>
	  </tr><tr>
		<td width="1%">
		<input type="checkbox" name="pg1_heartDise" <%= props.getProperty("pg1_heartDise", "") %>  @oscar.formDB dbType="tinyint(1)"/>
		</td>
		<td width="20%"><span class="small8">HEART DISEASE</span></td>
		<td>
		<input type="text" name="pg1_heartDiseSpec" style="width:100%" size="30" maxlength="30" value="<%= props.getProperty("pg1_heartDiseSpec", "") %>" @oscar.formDB />
		</td>
	  </tr><tr>
		<td>
		<input type="checkbox" name="pg1_hyperts" <%= props.getProperty("pg1_hyperts", "") %>  @oscar.formDB dbType="tinyint(1)"/>
		</td>
		<td><span class="small8">HYPERTENSION</span></td>
		<td>
		<input type="text" name="pg1_hypertsSpec" style="width:100%" size="30" maxlength="30" value="<%= props.getProperty("pg1_hypertsSpec", "") %>" @oscar.formDB />
		</td>
	  </tr><tr>
		<td>
		<input type="checkbox" name="pg1_diabete" <%= props.getProperty("pg1_diabete", "") %>  @oscar.formDB dbType="tinyint(1)"/>
		</td>
		<td><span class="small8">DIABETES</span></td>
		<td>
		<input type="text" name="pg1_diabeteSpec" style="width:100%" size="30" maxlength="30" value="<%= props.getProperty("pg1_diabeteSpec", "") %>" @oscar.formDB />
		</td>
	  </tr><tr>
		<td>
		<input type="checkbox" name="pg1_deprPsychiat" <%= props.getProperty("pg1_deprPsychiat", "") %>  @oscar.formDB dbType="tinyint(1)"/>
		</td>
		<td><span class="small8">DEPRESSION OR<br>PSYCHIATRIC</span></td>
		<td>
		<input type="text" name="pg1_deprPsychiatSpec" style="width:100%" size="30" maxlength="30" value="<%= props.getProperty("pg1_deprPsychiatSpec", "") %>" @oscar.formDB />
		</td>
	  </tr><tr>
		<td>
		<input type="checkbox" name="pg1_alcohDrug" <%= props.getProperty("pg1_alcohDrug", "") %>  @oscar.formDB dbType="tinyint(1)"/>
		</td>
		<td><span class="small8">ALCOHOL/<br>DRUG USE</span></td>
		<td>
		<input type="text" name="pg1_alcohDrugSpec" style="width:100%" size="30" maxlength="30" value="<%= props.getProperty("pg1_alcohDrugSpec", "") %>" @oscar.formDB />
		</td>
	  </tr><tr>
		<td>
		<input type="checkbox" name="pg1_thromCoag" <%= props.getProperty("pg1_thromCoag", "") %>  @oscar.formDB dbType="tinyint(1)"/>
		</td>
		<td><span class="small8">THROMBOEMBOLIC / COAG.</span></td>
		<td>
		<input type="text" name="pg1_thromCoagSpec" style="width:100%" size="30" maxlength="30" value="<%= props.getProperty("pg1_thromCoagSpec", "") %>" @oscar.formDB />
		</td>
	  </tr><tr>
		<td>
		<input type="checkbox" name="pg1_inherDisease" <%= props.getProperty("pg1_inherDisease", "") %>  @oscar.formDB dbType="tinyint(1)"/>
		</td>
		<td><span class="small8">INHERITED<br>DISEASE/DEFECT</span></td>
		<td>
		<input type="text" name="pg1_inherDiseaseSpec" style="width:100%" size="30" maxlength="30" value="<%= props.getProperty("pg1_inherDiseaseSpec", "") %>" @oscar.formDB />
		</td>
	  </tr><tr>
		<td>
		<input type="checkbox" name="pg1_ethnic" <%= props.getProperty("pg1_ethnic", "") %>  @oscar.formDB dbType="tinyint(1)"/>
		</td>
		<td><span class="small8">ETHNIC ( e.g.<br>Taysachs, Sickle)</span></td>
		<td>
		<input type="text" name="pg1_ethnicSpec" style="width:100%" size="30" maxlength="30" value="<%= props.getProperty("pg1_ethnicSpec", "") %>" @oscar.formDB />
		</td>
	  </tr><tr>
		<td>
		<input type="checkbox" name="pg1_famHistOther" <%= props.getProperty("pg1_famHistOther", "") %>  @oscar.formDB dbType="tinyint(1)"/>
		</td>
		<td><span class="small8">OTHER</span></td>
		<td>
		<input type="text" name="pg1_famHistOtherSpec" style="width:100%" size="30" maxlength="30" value="<%= props.getProperty("pg1_famHistOtherSpec", "") %>" @oscar.formDB />
		</td>
	  </tr>
      </table>

    </td>
  </tr>
  </table>


  </td>
  <td width="33%" valign="top">

  <table width="100%" border="0"  cellspacing="0" cellpadding="0">
  <tr>
    <td colspan="3"><b>8. PAST ILLNESS</b></td>
  </tr><tr>
    <td colspan="2">&nbsp;<i>no</i></td><td align="center"><i>yes (specify)</i></td>
  </tr><tr>
	<td width="1%" valign="top">
    <input type="checkbox" name="pg1_operation" <%= props.getProperty("pg1_operation", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	<td width="30%" valign="top"><span class="small8">OPERATIONS</span></td>
	<td>
	<textarea name="pg1_operationSpec" style="width:100%" cols="60" rows="3" @oscar.formDB  dbType="varchar(255)" ><%= props.getProperty("pg1_operationSpec", "") %>
    </textarea>
	</td>
  </tr><tr>
	<td valign="top">
    <input type="checkbox" name="pg1_cvOrResp" <%= props.getProperty("pg1_cvOrResp", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	<td><span class="small8">CV OR<br>RESPIRATORY</span></td>
	<td>
	<textarea name="pg1_cvOrRespSpec" style="width:100%" cols="60" rows="2" @oscar.formDB  dbType="varchar(255)" ><%= props.getProperty("pg1_cvOrRespSpec", "") %>
    </textarea>
	</td>
  </tr><tr>
	<td valign="top">
    <input type="checkbox" name="pg1_anesthetic" <%= props.getProperty("pg1_anesthetic", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	<td><span class="small8">ANESTHETIC PROBLEMS</span></td>
	<td>
	<input type="text" name="pg1_anestheticSpec" style="width:100%" size="40" maxlength="40" value="<%= props.getProperty("pg1_anestheticSpec", "") %>" @oscar.formDB />
	</td>
  </tr><tr>
	<td valign="top">
    <input type="checkbox" name="pg1_rxBlood" <%= props.getProperty("pg1_rxBlood", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	<td><span class="small8">Rx BLOOD PRODUCTS</span></td>
	<td>
	<input type="text" name="pg1_rxBloodSpec" style="width:100%" size="40" maxlength="40" value="<%= props.getProperty("pg1_rxBloodSpec", "") %>" @oscar.formDB />
	</td>
  </tr><tr>
	<td valign="top">
    <input type="checkbox" name="pg1_infectStd" <%= props.getProperty("pg1_infectStd", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	<td><span class="small8">INFECTIONS, STDS etc.</span></td>
	<td>
	<input type="text" name="pg1_infectStdSpec" style="width:100%" size="40" maxlength="40" value="<%= props.getProperty("pg1_infectStdSpec", "") %>" @oscar.formDB />
	</td>
  </tr><tr>
	<td valign="top">
    <input type="checkbox" name="pg1_susChiPox" <%= props.getProperty("pg1_susChiPox", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	<td><span class="small8">SUSCEPTIBLE TO CHICKEN POX</span></td>
	<td>
	<input type="text" name="pg1_susChiPoxSpec" style="width:100%" size="40" maxlength="40" value="<%= props.getProperty("pg1_susChiPoxSpec", "") %>" @oscar.formDB />
	</td>
  </tr><tr>
	<td valign="top">
    <input type="checkbox" name="pg1_thrCoag" <%= props.getProperty("pg1_thrCoag", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	<td><span class="small8">THROMBOEMBOLIC / COAG.</span></td>
	<td>
	<input type="text" name="pg1_thrCoagSpec" style="width:100%" size="40" maxlength="40" value="<%= props.getProperty("pg1_thrCoagSpec", "") %>" @oscar.formDB />
	</td>
  </tr><tr>
	<td valign="top">
    <input type="checkbox" name="pg1_hyper" <%= props.getProperty("pg1_hyper", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	<td><span class="small8">HYPERTENSION</span></td>
	<td>
	<input type="text" name="pg1_hyperSpec" style="width:100%" size="40" maxlength="40" value="<%= props.getProperty("pg1_hyperSpec", "") %>" @oscar.formDB />
	</td>
  </tr><tr>
	<td valign="top">
    <input type="checkbox" name="pg1_piGi" <%= props.getProperty("pg1_piGi", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	<td><span class="small8">GI</span></td>
	<td>
	<input type="text" name="pg1_piGiSpec" style="width:100%" size="40" maxlength="40" value="<%= props.getProperty("pg1_piGiSpec", "") %>" @oscar.formDB />
	</td>
  </tr><tr>
	<td valign="top">
    <input type="checkbox" name="pg1_piUrin" <%= props.getProperty("pg1_piUrin", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	<td><span class="small8">URINARY</span></td>
	<td>
	<input type="text" name="pg1_piUrinSpec" style="width:100%" size="40" maxlength="40" value="<%= props.getProperty("pg1_piUrinSpec", "") %>" @oscar.formDB />
	</td>
  </tr><tr>
	<td valign="top">
    <input type="checkbox" name="pg1_dbEndoc" <%= props.getProperty("pg1_dbEndoc", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	<td><span class="small8">DIABETES OR<br>ENDOCRINE</span></td>
	<td>
	<input type="text" name="pg1_dbEndocSpec" style="width:100%" size="40" maxlength="40" value="<%= props.getProperty("pg1_dbEndocSpec", "") %>" @oscar.formDB />
	</td>
  </tr><tr>
	<td valign="top">
    <input type="checkbox" name="pg1_seizNeur" <%= props.getProperty("pg1_seizNeur", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	<td><span class="small8">SEIZURE OR<br>NEUROLOGIC</span></td>
	<td>
	<input type="text" name="pg1_seizNeurSpec" style="width:100%" size="40" maxlength="40" value="<%= props.getProperty("pg1_seizNeurSpec", "") %>" @oscar.formDB />
	</td>
  </tr><tr>
	<td valign="top">
    <input type="checkbox" name="pg1_deprPsy" <%= props.getProperty("pg1_deprPsy", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	<td><span class="small8">DEPRESSION OR<br>PSYCHIATRIC</span></td>
	<td>
	<input type="text" name="pg1_deprPsySpec" style="width:100%" size="40" maxlength="40" value="<%= props.getProperty("pg1_deprPsySpec", "") %>" @oscar.formDB />
	</td>
  </tr><tr>
	<td valign="top">
    <input type="checkbox" name="pg1_piOther" <%= props.getProperty("pg1_piOther", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	<td><span class="small8">OTHER</span></td>
	<td>
	<input type="text" name="pg1_piOtherSpec" style="width:100%" size="40" maxlength="40" value="<%= props.getProperty("pg1_piOtherSpec", "") %>" @oscar.formDB />
	</td>
  </tr><tr>
  </table>

  </td>
  <td valign="top">

  <table width="100%" border="1"  cellspacing="0" cellpadding="0">
  <tr>
    <td>

    <table width="100%" border="0"  cellspacing="0" cellpadding="0">
    <tr>
      <td colspan="2"><b>9. SOCIAL HISTORY</b></td>
    </tr><tr>
      <td colspan="2"><i><span class="small9">&nbsp; discussed &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;concerns (specify)</span></i></td>
    </tr><tr>
      <td width="1%"><input type="checkbox" name="pg1_nutrition" <%= props.getProperty("pg1_nutrition", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	  <td><span class="small9">NUTRITION</span>
      <input type="text" name="pg1_nutritionSpec" size="32" maxlength="40" value="<%= props.getProperty("pg1_nutritionSpec", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
      <td><input type="checkbox" name="pg1_speDiet" <%= props.getProperty("pg1_speDiet", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	  <td><span class="small9">SPECIAL DIET</span>
      <input type="text" name="pg1_speDietSpec" size="30" maxlength="40" value="<%= props.getProperty("pg1_speDietSpec", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
      <td><input type="checkbox" name="pg1_foliAcid" <%= props.getProperty("pg1_foliAcid", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	  <td><span class="small8">FOLIC ACID</span>
      <input type="text" name="pg1_foliAcidSpec" size="10" maxlength="15" value="<%= props.getProperty("pg1_foliAcidSpec", "") %>" @oscar.formDB />
	  <span class="small8">start date:</span>
      <input type="text" name="pg1_foliAcidDate" size="10" maxlength="15" value="<%= props.getProperty("pg1_foliAcidDate", "") %>" @oscar.formDB dbType="date" />
	  </td>
    </tr><tr>
      <td><input type="checkbox" name="pg1_alco" <%= props.getProperty("pg1_alco", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	  <td><span class="small8">ALCOHOL</span>
      <input type="text" name="pg1_alcoSpec" size="10" maxlength="10" value="<%= props.getProperty("pg1_alcoSpec", "") %>" @oscar.formDB />
	  <span class="small7" title="(see reverse)">T-ACE SCORE:</span>
      <input type="text" name="pg1_alcoTA" size="5" maxlength="5" value="<%= props.getProperty("pg1_alcoTA", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
      <td><input type="checkbox" name="pg1_drug" <%= props.getProperty("pg1_drug", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	  <td><span class="small8">DRUGS (OTC's, vitamins)</span>
      <input type="text" name="pg1_drugSpec" size="22" maxlength="30" value="<%= props.getProperty("pg1_drugSpec", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
      <td><input type="checkbox" name="pg1_subUse" <%= props.getProperty("pg1_subUse", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	  <td><span class="small8">SUBSTANCE USE</span>
      <input type="text" name="pg1_subUseSpec" size="28" maxlength="35" value="<%= props.getProperty("pg1_subUseSpec", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
      <td><input type="checkbox" name="pg1_ipv" <%= props.getProperty("pg1_ipv", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	  <td><span class="small9">IPV</span>
      <input type="text" name="pg1_ipvSpec" size="40" maxlength="40" value="<%= props.getProperty("pg1_ipvSpec", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
      <td><input type="checkbox" name="pg1_smokeBef" <%= props.getProperty("pg1_smokeBef", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	  <td><span class="small8">SMOKING (before pregnancy) Cigs./day</span>
      <input type="text" name="pg1_smokeBefSpec" size="5" maxlength="5" value="<%= props.getProperty("pg1_smokeBefSpec", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
      <td><input type="checkbox" name="pg1_smokeCur" <%= props.getProperty("pg1_smokeCur", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	  <td><span class="small8">SMOKING (currently) Cigs./day</span>
      <input type="text" name="pg1_smokeCurSpec" size="5" maxlength="5" value="<%= props.getProperty("pg1_smokeCurSpec", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
      <td><input type="checkbox" name="pg1_secSmoke" <%= props.getProperty("pg1_secSmoke", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	  <td><span class="small8">SECOND HAND SMOKE</span>
      <input type="text" name="pg1_secSmokeSpec" size="24" maxlength="30" value="<%= props.getProperty("pg1_secSmokeSpec", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
      <td><input type="checkbox" name="pg1_finaHouse" <%= props.getProperty("pg1_finaHouse", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	  <td><span class="small8">FINANCIAL/HOUSING</span>
      <input type="text" name="pg1_finaHouseSpec" size="26" maxlength="30" value="<%= props.getProperty("pg1_finaHouseSpec", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
      <td><input type="checkbox" name="pg1_supSys" <%= props.getProperty("pg1_supSys", "") %>  @oscar.formDB dbType="tinyint(1)"/></td>
	  <td><span class="small8">SUPPORT SYSTEMS</span>
      <input type="text" name="pg1_supSysSpec" size="26" maxlength="30" value="<%= props.getProperty("pg1_supSysSpec", "") %>" @oscar.formDB />
	  </td>
    </tr>
	</table>

	</td>
  </tr><tr>
    <td>

    <table width="100%" border="0"  cellspacing="0" cellpadding="0">
	<tr>
      <td colspan="3"><span class="small9">NUMBER OF SCHOOL YEARS COMPLETED:</span>
      <input type="text" name="pg1_schYear" size="3" maxlength="3" value="<%= props.getProperty("pg1_schYear", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
      <td colspan="3"><span class="small8">WORK (specify type):</span>
      <input type="text" name="pg1_work" size="20" maxlength="25" value="<%= props.getProperty("pg1_work", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
      <td colspan="3"><span class="small8">hours worked per day:</span>
      <input type="text" name="pg1_workHourDay" size="3" maxlength="5" value="<%= props.getProperty("pg1_workHourDay", "") %>" @oscar.formDB />
	  <span class="small8">quitting date:</span>
      <input type="text" name="pg1_quitDate" size="10" maxlength="10" value="<%= props.getProperty("pg1_quitDate", "") %>" @oscar.formDB  dbType="date"/></td>
    </tr><tr>
      <td colspan="3"><span class="small8">partner's work:</span>
      <input type="text" name="pg1_ptWork" size="30" maxlength="40" value="<%= props.getProperty("pg1_ptWork", "") %>" @oscar.formDB />
	  </td>
    </tr>
    </table>
	
    </td>
  </tr><tr>
    <td>

    <table width="100%" border="0"  cellspacing="0" cellpadding="0">
	<tr>
      <td width="1%" valign="top">
	  <input type="checkbox" name="pg1_earComm" <%= props.getProperty("pg1_earComm", "") %>  @oscar.formDB dbType="tinyint(1)"/>
	  </td>
      <td><span class="small8">EARLY COMMUNITY<br>SERVICES REFERRAL</span>
	  </td><td valign="top">
      <input type="text" name="pg1_earCommSpec" size="26" maxlength="30" value="<%= props.getProperty("pg1_earCommSpec", "") %>" @oscar.formDB />
	  </td>
    </tr><tr>
      <td>
	  <input type="checkbox" name="pg1_otherRef" <%= props.getProperty("pg1_otherRef", "") %>  @oscar.formDB dbType="tinyint(1)"/>
	  </td>
      <td><span class="small8">OTHER REFERRAL</span>
	  </td><td valign="top">
      <input type="text" name="pg1_otherRefSpec" size="26" maxlength="35" value="<%= props.getProperty("pg1_otherRefSpec", "") %>" @oscar.formDB />
	  </td>
    </tr>
    </table>

    </td>
  </tr>
  </table>

  </td>
</tr>
</table>

<table width="100%" border="1"  cellspacing="0" cellpadding="0">
<tr>
  <td width="60%" valign="top">

  <table class="shrinkMe" width="100%" border="1"  cellspacing="0" cellpadding="0">
  <tr>
    <td colspan="2"><b>11. EXAMINATION</b> dd/mm/yyyy<br>
      <input type="text" name="pg1_examination"  style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("pg1_examination", "") %>" @oscar.formDB  dbType="date"/>
	</td>
	<td colspan="2"><b>BP</b><br>
      <input type="text" name="pg1_bp"  style="width:100%" size="10" maxlength="12" value="<%= props.getProperty("pg1_bp", "") %>" @oscar.formDB />	
	</td>
  </tr><tr>
    <td width="10%"><span class="small9">HEAD &<br>NECK</span></td>
    <td width="40%">
      <input type="text" name="pg1_headNeck" size="35" maxlength="40" value="<%= props.getProperty("pg1_headNeck", "") %>" @oscar.formDB />
	</td>
    <td width="15%"><span class="small8">MUSCULOSKELETAL<br>&SPINE</span></td>
    <td width="35%">
      <input type="text" name="pg1_muscSpine" size="30" maxlength="40" value="<%= props.getProperty("pg1_muscSpine", "") %>" @oscar.formDB />
	</td>
  </tr><tr>
    <td><span class="small9">BREAST /<br>NIPPLES</span></td>
    <td>
      <input type="text" name="pg1_breaNipp" size="35" maxlength="40" value="<%= props.getProperty("pg1_breaNipp", "") %>" @oscar.formDB />
	</td>
    <td><span class="small9">VARICES &<br>SKIN</span></td>
    <td>
      <input type="text" name="pg1_variSkin" size="30" maxlength="40" value="<%= props.getProperty("pg1_variSkin", "") %>" @oscar.formDB />
	</td>
  </tr><tr>
    <td><span class="small9">HEART &<br>LUNGS</span></td>
    <td>
      <input type="text" name="pg1_heartLung" size="35" maxlength="40" value="<%= props.getProperty("pg1_heartLung", "") %>" @oscar.formDB />
	</td>
    <td><span class="small9">PELVIC EXAM</span></td>
    <td>
      <input type="text" name="pg1_pelvic" size="30" maxlength="40" value="<%= props.getProperty("pg1_pelvic", "") %>" @oscar.formDB />
	</td>
  </tr><tr>
    <td><span class="small9">ABDOMEN</span></td>
    <td>
      <input type="text" name="pg1_abdomen" size="35" maxlength="40" value="<%= props.getProperty("pg1_abdomen", "") %>" @oscar.formDB />
	</td>
    <td><span class="small8">SWABS /<br>CERVIX CYTOLOGY</span></td>
    <td>
      <input type="text" name="pg1_swabsCerv" size="30" maxlength="40" value="<%= props.getProperty("pg1_swabsCerv", "") %>" @oscar.formDB />
	</td>
  </tr>
  </table>

  </td>
  <td valign="top">

  <table width="100%" border="0"  cellspacing="0" cellpadding="0">
  <tr>
    <td colspan="6"><b>12. <font color="red">TOPICS FOR DISCUSSION</font></b></td>
  </tr><tr>
    <td width="1%">
	<input type="checkbox" name="pg1_disBestCh" <%= props.getProperty("pg1_disBestCh", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td width="38%"><span class="small8"><i>Baby's Best Chance</i></font></td>
    <td width="1%">
	<input type="checkbox" name="pg1_disRestPreLabour" <%= props.getProperty("pg1_disRestPreLabour", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td width="35%"><span class="small8">Rest / Preterm Labour</font></td>
    <td width="1%">
	<input type="checkbox" name="pg1_disCallSCh" <%= props.getProperty("pg1_disCallSCh", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td><span class="small8">Call Schedule</font></td>
  </tr><tr>
    <td>
	<input type="checkbox" name="pg1_disPreEdu" <%= props.getProperty("pg1_disPreEdu", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td><span class="small8">Prenatal Education</font></td>
    <td>
	<input type="checkbox" name="pg1_disSexu" <%= props.getProperty("pg1_disSexu", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td><span class="small8">Sexual Relations</font></td>
    <td>
	<input type="checkbox" name="pg1_disLabStg" <%= props.getProperty("pg1_disLabStg", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td><span class="small8">Labour Stages</font></td>
  </tr><tr>
    <td>
	<input type="checkbox" name="pg1_disBreFeed" <%= props.getProperty("pg1_disBreFeed", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td><span class="small8">Breastfeeding </font></td>
    <td>
	<input type="checkbox" name="pg1_disGBS" <%= props.getProperty("pg1_disGBS", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td><span class="small8">GBS Management</font></td>
    <td>
	<input type="checkbox" name="pg1_disCSect" <%= props.getProperty("pg1_disCSect", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td><span class="small8">C-Section</font></td>
  </tr><tr>
    <td colspan="2"><span class="small7">plans to BF<input type="checkbox" name="pg1_disBfY" <%= props.getProperty("pg1_disBfY", "") %> @oscar.formDB dbType="tinyint(1)"/>yes
	<input type="checkbox" name="pg1_disBfN" <%= props.getProperty("pg1_disBfN", "") %> @oscar.formDB dbType="tinyint(1)"/>no</font>
    </td>
	<td>
	<input type="checkbox" name="pg1_disVBAC" <%= props.getProperty("pg1_disVBAC", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td><span class="small8">VBAC</font></td>
    <td>
	<input type="checkbox" name="pg1_disBabyCare" <%= props.getProperty("pg1_disBabyCare", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td><span class="small8">Baby Care</font></td>
  </tr><tr>
    <td>
	<input type="checkbox" name="pg1_disBreNip" <%= props.getProperty("pg1_disBreNip", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td><span class="small8">Breast / Nipple Care </font></td>
    <td>
	<input type="checkbox" name="pg1_disHospAdm" <%= props.getProperty("pg1_disHospAdm", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td><span class="small8">Hospital Admission/</font></td>
    <td>
	<input type="checkbox" name="pg1_disSIDS" <%= props.getProperty("pg1_disSIDS", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td><span class="small8">SIDS Prevention</font></td>
  </tr><tr>
    <td>
	<input type="checkbox" name="pg1_disExer" <%= props.getProperty("pg1_disExer", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td><span class="small8">Exercises</font></td>
    <td colspan="2" align="center"><span class="small8">Procedures</font></td>
    <td>
	<input type="checkbox" name="pg1_disCircum" <%= props.getProperty("pg1_disCircum", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td><span class="small8">Circumcision</font></td>
  </tr><tr>
    <td>
	<input type="checkbox" name="pg1_disGenCouns" <%= props.getProperty("pg1_disGenCouns", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td><span class="small8">Genetic Counselling</font></td>
    <td>
	<input type="checkbox" name="pg1_disBirthPlan" <%= props.getProperty("pg1_disBirthPlan", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td><span class="small8">Birth Plan</font></td>
    <td>
	<input type="checkbox" name="pg1_disOther" <%= props.getProperty("pg1_disOther", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td rowspan="2"><span class="small8">
	<textarea name="pg1_disOtherSpec" style="width:100%" cols="10" rows="2" @oscar.formDB  dbType="varchar(30)" ><%= props.getProperty("pg1_disOtherSpec", "") %>
    </textarea>
    </font></td>
  </tr><tr>
    <td>
	<input type="checkbox" name="pg1_disHIV" <%= props.getProperty("pg1_disHIV", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td><span class="small8">HIV Testing</font></td>
    <td>
	<input type="checkbox" name="pg1_disPain" <%= props.getProperty("pg1_disPain", "") %> @oscar.formDB dbType="tinyint(1)"/>
	</td>
    <td><span class="small8">Pain Management</font></td>
    <td></td>
  </tr>
  </table>

  </td>
</tr>
</table>

<table width="100%" border="0"  cellspacing="0" cellpadding="0">
<tr>
  <td width="12%"  valign="top" nowrap><b>13. SUMMARY</b></td>
  <td width="1%" valign="top">
  <input type="checkbox" name="pg1_sumBloodTran" <%= props.getProperty("pg1_sumBloodTran", "") %>  @oscar.formDB dbType="tinyint(1)"/>
  </td>
  <td width="50%" valign="top">I have discussed the benefits and risks of planned or potential transfusion therapy
  of blood and/or blood products with the patient
  </td>
  <td width="1%" valign="top">
  <input type="checkbox" name="pg1_sumSerumScr" <%= props.getProperty("pg1_sumSerumScr", "") %>  @oscar.formDB dbType="tinyint(1)"/>
  </td>
  <td valign="top">Maternal serum screening offered</td>
</tr>
</table>

<br>
<br>
<table width="100%" border="0"  cellspacing="0" cellpadding="0">
<tr>
  <td width="60%"></td>
  <td>SIGNATURE: 
  <input type="text" name="pg1_signature" size="35" maxlength="40" value="<%= props.getProperty("pg1_signature", "") %>" @oscar.formDB/>   MD/RM</td>
</tr>
</table>


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
       <td>
           <!--a href="javascript: popPage('formlabreq.jsp?demographic_no=<%=demoNo%>&formId=0&provNo=<%=provNo%>&labType=AR','LabReq');">LAB</a-->
        </td>
        <td align="right"><b>View:</b> 
            <a href="javascript: popupPage('formbcarpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2 <font size=-2>(pg.1)</font></a> |
            <a href="javascript: popupPage('formbcarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>&view=1');">AR2 <font size=-2>(pg.2)</font></a>
            &nbsp;
        </td>
        <td align="right"><b>Edit:</b>AR1 |
            <a href="formbcarpg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2 <font size=-2>(pg.1)</font></a> |
            <a href="formbcarpg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">AR2 <font size=-2>(pg.2)</font></a> |
            <!--a href="javascript: popupFixedPage(700,950,'../decision/antenatal/antenatalplanner.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">AR Planner</a-->
        </td>
<%
  }
%>
    </tr>
</table>


<table width="100%" border="1"  cellspacing="0" cellpadding="0">
<tr>
  <th align="center">RISK IDENTIFICATION</th>
</tr><tr>
  <td>

  <table width="100%" border="0"  cellspacing="0" cellpadding="0">
  <tr>
    <td valign="top">
	
    <table width="100%" border="0"  cellspacing="0" cellpadding="0">
    <tr>
      <th colspan="2" align="left">PAST OBSTETRICAL HISTORY</th>
	</tr><tr>
      <th colspan="2" align="left"><span class="small9">RISK FACTORS</span></th>
	</tr><tr>
      <td width="1%">
	  <input type="checkbox" name="c_riskNeonDeath" <%= props.getProperty("c_riskNeonDeath", "") %> @oscar.formDB dbType="tinyint(1)" />
	  </td>
      <td>Neonatal death</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskStillbirth" <%= props.getProperty("c_riskStillbirth", "") %> @oscar.formDB dbType="tinyint(1)" />
	  </td>
      <td>Stillbirth</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskAbortion" <%= props.getProperty("c_riskAbortion", "") %> @oscar.formDB dbType="tinyint(1)" />
	  </td>
      <td>Abortion ( 12 - 20 weeks )</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskHabitAbort" <%= props.getProperty("c_riskHabitAbort", "") %> @oscar.formDB dbType="tinyint(1)" />
	  </td>
      <td>Habitual abortion ( 3+ )</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskPriPretBirth33" <%= props.getProperty("c_riskPriPretBirth33", "") %> @oscar.formDB dbType="tinyint(1)" />
	  </td>
      <td>Prior preterm birth ( 33 - 36 wks. )</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskPriPretBirth20" <%= props.getProperty("c_riskPriPretBirth20", "") %> @oscar.formDB dbType="tinyint(1)" />
	  </td>
      <td>Prior preterm birth ( 20 - 33 wks. )</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskPriCesBirth" <%= props.getProperty("c_riskPriCesBirth", "") %> @oscar.formDB dbType="tinyint(1)" />
	  </td>
      <td>Prior Cesarean birth ( uterine surgery )</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskPriIUGR" <%= props.getProperty("c_riskPriIUGR", "") %> @oscar.formDB dbType="tinyint(1)" />
	  </td>
      <td>Prior IUGR baby</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskPriMacr" <%= props.getProperty("c_riskPriMacr", "") %> @oscar.formDB dbType="tinyint(1)" />
	  </td>
      <td>Prior macrosomic baby</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskRhImmu" <%= props.getProperty("c_riskRhImmu", "") %> @oscar.formDB dbType="tinyint(1)" />
	  </td>
      <td>Rh Immunized ( antibodies present )</td>
	</tr><tr>
      <td valign="top">
	  <input type="checkbox" name="c_riskPriRhNB" <%= props.getProperty("c_riskPriRhNB", "") %> @oscar.formDB dbType="tinyint(1)" />
	  </td>
      <td>Prior Rh affected preg. with NB exchange<br>or prem.</td>
	</tr><tr>
      <td valign="top">
	  <input type="checkbox" name="c_riskMajCongAnom" <%= props.getProperty("c_riskMajCongAnom", "") %> @oscar.formDB dbType="tinyint(1)" />
	  </td>
      <td>Major congenital anomalies (eg. Cardiac,<br>CNS, Down's Syndrome.)</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskPPHemo" <%= props.getProperty("c_riskPPHemo", "") %> @oscar.formDB dbType="tinyint(1)" />
	  </td>
      <td>P.P. Hemorrhage</td>
	</tr>
	</table>
	
	</td><td valign="top">

    <table width="100%" border="0"  cellspacing="0" cellpadding="0">
    <tr>
      <th colspan="2" align="left">MEDICAL HISTORY RISK FACTORS</th>
	</tr><tr>
      <th colspan="2" align="left"><span class="small9">DIABETES</span></th>
	</tr><tr>
      <td width="1%">
	  <input type="checkbox" name="c_riskConDiet" <%= props.getProperty("c_riskConDiet", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Controlled by diet only</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskDietMacFetus" <%= props.getProperty("c_riskDietMacFetus", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Diet only macrosomic fetus</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskInsDepend" <%= props.getProperty("c_riskInsDepend", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Insulin dependent</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskRetDoc" <%= props.getProperty("c_riskRetDoc", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Retinopathy documented</td>
	</tr><tr>
      <th colspan="2" align="left"><span class="small9">HEART DISEASE</span></th>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskAsymt" <%= props.getProperty("c_riskAsymt", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Asymptomatic (no effect on daily living)</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskSymt" <%= props.getProperty("c_riskSymt", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Symptomatic ( affects daily living)</td>
	</tr><tr>
      <th colspan="2" align="left"><span class="small9">HYPERTENSION</span></th>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_risk14090" <%= props.getProperty("c_risk14090", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>140 / 90</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskHyperDrug" <%= props.getProperty("c_riskHyperDrug", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Hypertensive drugs</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskChroRenalDisease" <%= props.getProperty("c_riskChroRenalDisease", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Chronic renal disease documented</td>
	</tr><tr>
      <th colspan="2" align="left"><span class="small9">OTHER</span></th>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskUnder18" <%= props.getProperty("c_riskUnder18", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Age under 18 at delivery</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskOver35" <%= props.getProperty("c_riskOver35", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Age 35 or over at delivery</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskObesity" <%= props.getProperty("c_riskObesity", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Obesity (equal or more than 90kg. or 200 lbs.)</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskH157" <%= props.getProperty("c_riskH157", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Height (under 1.57 m 5 ft. 2 in.)</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskH152" <%= props.getProperty("c_riskH152", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Height (under 1.52 m 5 ft. 0 in.)</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskDepre" <%= props.getProperty("c_riskDepre", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Depression</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskAlcoDrug" <%= props.getProperty("c_riskAlcoDrug", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Alcohol and Drugs</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskSmoking" <%= props.getProperty("c_riskSmoking", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Smoking any time during pregnancy</td>
	</tr><tr>
      <td valign="top">
	  <input type="checkbox" name="c_riskOtherMedical" <%= props.getProperty("c_riskOtherMedical", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Other medical / surgical disorders<br>e.g. epilepsy, severe asthma, Lupus etc.</td>
	</tr>
	</table>



	</td><td valign="top">
	
    <table width="100%" border="0"  cellspacing="0" cellpadding="0">
    <tr>
      <th colspan="2" align="left">PROBLEMS IN CURRENT PREGNANCY</th>
	</tr><tr>
      <th colspan="2" align="left"><span class="small9">RISK FACTOR</span></th>
	</tr><tr>
      <td width="1%">
	  <input type="checkbox" name="c_riskDiagLarge" <%= props.getProperty("c_riskDiagLarge", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Diagnosis of large for dates</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskDiagSmall" <%= props.getProperty("c_riskDiagSmall", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Diagnosis of small for dates (IUGR)</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskPolyhyd" <%= props.getProperty("c_riskPolyhyd", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Polyhydramnios or oligohydramnios</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskMulPreg" <%= props.getProperty("c_riskMulPreg", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Multiple pregnancy</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskMalpres" <%= props.getProperty("c_riskMalpres", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Malpresentations</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskMemRupt37" <%= props.getProperty("c_riskMemRupt37", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Membrane rupture before 37 weeks</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskBleeding" <%= props.getProperty("c_riskBleeding", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Bleeding</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskPregIndHypert" <%= props.getProperty("c_riskPregIndHypert", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Pregnancy induced hypertension</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskProte1" <%= props.getProperty("c_riskProte1", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Proteinuria > 1+</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskGesDiabete" <%= props.getProperty("c_riskGesDiabete", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Gestational diabetes documented</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskBloodAnti" <%= props.getProperty("c_riskBloodAnti", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Blood antibodies (Rh, Anti C, Anti K, etc.) </td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskAnemia" <%= props.getProperty("c_riskAnemia", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Anemia ( < 100g per L ) </td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskAdmPreterm" <%= props.getProperty("c_riskAdmPreterm", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Admission in preterm labour</td>
	</tr><tr>
      <td>
	  <input type="checkbox" name="c_riskPreg42W" <%= props.getProperty("c_riskPreg42W", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Pregnancy >= 42 weeks</td>
	</tr><tr>
      <td valign="top">
	  <input type="checkbox" name="c_riskWtLoss" <%= props.getProperty("c_riskWtLoss", "") %> @oscar.formDB dbType="tinyint(1)" />
      </td>
      <td>Poor weight gain 26 - 36 weeks ( <.5 kg / wk )<br>or weight loss</td>
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
</html:html>
