
<%@ page language="java"%>
<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<%
    String formClass = "SelfAdministered";
    String formLink = "formselfadministered.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(demoNo, formId);

    //FrmData fd = new FrmData();    String resource = fd.getResource(); resource = resource + "ob/riskinfo/";

    //get project_home
    String project_home = request.getContextPath().substring(1);	
%>
<%
  boolean bView = false;
  if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 
%>

<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>

<head>
    <title>Self Administered Questions Used in Self-Report Risk Index</title>
    <html:base/>
        <style type="text/css">
	.Head {
            background-color:#BBBBBB;
            padding-top:3px;
            padding-bottom:3px;
            width:739px;
            font-size:12pt;
        }

        .Head INPUT {
            width: 100px;
        }

        .Head A {
            font-size:12pt;
        }

        BODY {
            font-family: Arial, Verdana, Tahoma, Helvetica, sans-serif;             
            background-color: #c4e9f6;            
        }

        TABLE {
            font-family: Arial, Verdana, Tahoma, Helvetica, sans-serif;
        }
        
        TD{
            font-size:13.5pt;
        }

        TH{
            font-size:14pt;
        }

        INPUT.checkbox{
            height: 25px;
            width: 25px;            
        }
        .title {
            background-color: #486ebd;
            color: #FFFFFF;            
            font-weight: bold;
            text-align: centre;
        }
        

    </style>
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
            document.forms[0].action = "../form/createpdf?__title=&__cfgfile=&__cfgfile=&__template=";
            document.forms[0].target="planner";
            document.forms[0].submit();
            document.forms[0].target="apptProviderSearch";
        }
        return ret;
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
        if(confirm("Are you sure you wish to exit without saving your changes?")==true) {
            window.close();
        }
        return(false);
    }
    function onSaveExit() {
        document.forms[0].submit.value="exit";
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
        }  catch (ex)  {
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
        } 

        return b;
    }
</script>

<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0" onload="window.resizeTo(768,768)">
<!--
@oscar.formDB Table="formAdf" 
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
<!--input type="hidden" name="provider_no" value=<%=request.getParameter("provNo")%> />
<input type="hidden" name="provNo" value="<%= request.getParameter("provNo") %>" /-->
<input type="hidden" name="submit" value="exit"/>

<table border="0" cellspacing="0" cellpadding="0" width="100%" >
 <tr bgcolor="#486ebd">
      <th align='LEFT'  ><font face="Arial, Helvetica, sans-serif" color="#FFFFFF">Self Administered Questions Used in Self-Report Risk Index</font></th>
 </tr>
</table>

<table>
    <tr>        
        <td>
            <table width="100%" border="0"  cellspacing="0" cellpadding="0" >
                <tr>
                    <td align="right">1. </td>
                    <td>Would you say, in general your health is</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="healthEx" <%= props.getProperty("healthEx", "") %>/>
                    </td>
                    <td>Excellent</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="healthVG" <%= props.getProperty("healthVG", "") %>/>
                    </td>
                    <td>Very Good</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="healthG" <%= props.getProperty("healthG", "") %>/>
                    </td>
                    <td>Good</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="healthF" <%= props.getProperty("healthF", "") %>/>
                    </td>
                    <td>Fair</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="healthP" <%= props.getProperty("healthP", "") %>/>
                    </td>
                    <td>Poor</td>
                </tr>    
                <tr>
                    <td align="right">2. </td>
                    <td>In the previous 12 months have you stayed overnight as a patient in a hospital?</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="stayInHospNo" <%= props.getProperty("stayInHospNo", "") %>/>
                    </td>
                    <td>Not at all</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="stayInHosp1" <%= props.getProperty("stayInHosp1", "") %>/>
                    </td>
                    <td>One time</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="stayInHosp2Or3" <%= props.getProperty("stayInHosp2Or3", "") %>/>
                    </td>
                    <td>Two or three times</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="stayInHospMore3" <%= props.getProperty("stayInHospMore3", "") %>/>
                    </td>
                    <td>More than three times</td>
                </tr>
                <tr>
                    <td align="right">3. </td>
                    <td>In the previous 12 months, how many times did you visit a physician or clinic?</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="visitPhyNo" <%= props.getProperty("visitPhyNo", "") %>/>
                    </td>
                    <td>Not at all</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="visitPhy1" <%= props.getProperty("visitPhy1", "") %>/>
                    </td>
                    <td>One time</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="visitPhy2Or3" <%= props.getProperty("visitPhy2Or3", "") %>/>
                    </td>
                    <td>Two or three times</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="visitPhyMore3" <%= props.getProperty("visitPhyMore3", "") %>/>
                    </td>
                    <td>More than three times</td>
                </tr>
                <tr>
                    <td align="right">4. </td>
                    <td>In the previous 12 months, did you have diabetes?</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="diabetesY" <%= props.getProperty("diabetesY", "") %>/>
                    </td>
                    <td>Yes</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="diabetesN" <%= props.getProperty("diabetesN", "") %>/>
                    </td>
                    <td>No</td>
                </tr>
                <tr>
                    <td align="right">5. </td>
                    <td>Have you ever had ...</td>
                </tr>
                <tr bgcolor="white">                    
                    <td colspan="2">
                        <table bgcolor="white">
                            <tr>
                                <td colspan="2">Coronary Heart Disease</td>
                                <td colspan="2">Angina Pectoris</td>
                                <td colspan="2">Myocardial infarction</td>
                                <td colspan="2">Any other heart attack</td>
                            </tr>
                            <tr>
                                <td width="3%" align="right">
                                    <input type="checkbox"  class="checkbox" name="heartDiseaseY" <%= props.getProperty("heartDiseaseY", "") %>/>                    
                                </td> 
                                <td>Yes</td>
                                <td width="3%" align="right">
                                    <input type="checkbox"  class="checkbox" name="anginaPectorisY" <%= props.getProperty("anginaPectorisY", "") %>/>                   
                                </td> 
                                <td>Yes</td>
                                <td width="3%" align="right">
                                    <input type="checkbox"  class="checkbox" name="myocardialInfarctionY" <%= props.getProperty("myocardialInfarctionY", "") %>/>          
                                </td> 
                                <td>Yes</td>
                                <td width="3%" align="right">
                                    <input type="checkbox"  class="checkbox" name="anyHeartAttackY" <%= props.getProperty("anyHeartAttackY", "") %>/>     
                                </td> 
                                <td>Yes</td>
                            </tr>
                            <tr>
                                <td width="3%" align="right">
                                    <input type="checkbox"  class="checkbox" name="heartDiseaseN" <%= props.getProperty("heartDiseaseN", "") %>/> 
                                </td>
                                <td>No</td>
                                <td width="3%" align="right">
                                    <input type="checkbox"  class="checkbox" name="anginaPectorisN" <%= props.getProperty("anginaPectorisN", "") %>/>
                                </td>
                                <td>No</td>
                                <td width="3%" align="right">
                                    <input type="checkbox"  class="checkbox" name="myocardialInfarctionN" <%= props.getProperty("myocardialInfarctionN", "") %>/>
                                </td>
                                <td>No</td>
                                <td width="3%" align="right">
                                    <input type="checkbox"  class="checkbox" name="anyHeartAttackN" <%= props.getProperty("anyHeartAttackN", "") %>/>
                                </td>
                                <td>No</td>
                            </tr>
                        </table>
                    </td>
                </tr>                
                <tr>
                    <td align="right" valign="top" >6. </td>
                    <td>Is there a friend, relative neighbour who would take care of you for a few days if neccessary?</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="relativeTakeCareY" <%= props.getProperty("relativeTakeCareY", "") %>/>
                    </td>
                    <td>Yes</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="relativeTakeCareN" <%= props.getProperty("relativeTakeCareN", "") %>/>
                    </td>
                    <td>No</td>
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
</html:form>
</body>
</html:html>
