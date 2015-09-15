<%--                                                                            
                                                                                
    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.        
    This program is free software; you can redistribute it and/or               
    modify it under the terms of the GNU General Public License                 
    as published by the Free Software Foundation; either version 2              
    of the License, or (at your option) any later version.                      
                                                                                
    This program is distributed in the hope that it will be useful,             
    but WITHOUT ANY WARRANTY; without even the implied warranty of              
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                
    GNU General Public License for more details.                                
                                                                                
    You should have received a copy of the GNU General Public License           
    along with this program; if not, write to the Free Software                 
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.  
                                                                                
    This software was written for the                                           
    Department of Family Medicine                                               
    McMaster University                                                         
    Hamilton                                                                    
    Ontario, Canada                                                             
                                                                                
--%>                                                                            
        
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

<%@ page language="java"%>
<%@ page import="oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<jsp:useBean id="oscarVariables" class="java.util.Properties"
             scope="session" />

<%
String formClass = "BCNewBorn2008";
String formLink = "formBCNewBorn2008pg1.jsp";

int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
int formId = Integer.parseInt(request.getParameter("formId"));
int provNo = Integer.parseInt((String) session.getAttribute("user"));
FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

FrmData fd = new FrmData();
String resource = fd.getResource();

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
    <% response.setHeader("Cache-Control","no-cache");%>

    <head>

        <title>British Columbia New Born Record 2008 - Page 1</title>


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

        <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
        <html:base />
    </head>

    <script type="text/javascript" language="Javascript">

        function reset() {
            document.forms[0].target = "";
            document.forms[0].action = "/<%=project_home%>/form/formname.do" ;
        }
        function onPrint() {
            document.forms[0].submit.value="print"; //printAR1
           // var ret = checkAllDates();
            var ret = true;
            if(ret==true)
            {
                document.forms[0].action = "../form/createpdf?__title=British+Columbia+Newborn+Record+2008+Part+2&__cfgfile=bcNB2008PrintCfgPg1&__template=bcNewBorn2008pg1";

                document.forms[0].target="_blank";
            }
            return ret;
        }
        
        function onSave() {
        	document.forms[0].submit.value="save";
        	var ret = true;
        	//var ret = checkAllDates();
        	//if(ret==true) {
		   //	ret = checkAllTimes();
    		//}
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
        //if(valDate(document.forms[0].Section8DATE)==false){
         //   b = false;
        //}

        return b;
       }

       function checkAllTimes() {
        var b = true;
        //if(valTime(document.forms[0].ID GOES HERE)==false){
        //    b = false;
        //}

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

    <body onLoad="setfocus()">
    <%--
@oscar.formDB Table="formBCNewBorn2008"
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
                    %> 
                      <input type="submit" value="Save"
				                 onclick="javascript:return onSave();" /> <input type="submit"
				                                                        value="Save and Exit" onclick="javascript:return onSaveExit();" />
				                                                                                                                             
                                                                    <%
                    }
                    %> <input type="submit" value="Exit"
                              onclick="javascript:return onExit();" /> <input type="submit"
                                                                value="Print" onclick="javascript:return onPrint();" /></td>
                <%
                if (!bView) {
                %>

                <td align="right">
                <td align="right"><b>Edit:</b>Part1 | <a
                        href="formBCNewBorn2008pg2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Part2
                    <font size=-2>(pg.1)</font></a> | <a
                        href="formBCNewBorn2008pg3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Part2
                <font size=-2>(pg.2)</font></a> |</td>
                <%
                }
                %>
            </tr>
        </table>

<table cellpadding="0" cellspacing="0" border="0" >
<tr>
<td width="40%" align="left" valign="top">


        <br>

        <table cellpadding="0" cellspacing="0" border="1" align="left" width="100%" style="font-size: 10px;"  summary="Section 1.">
		  

		  

        <tr><td colspan="3" style="font-size: 14; "><b>Section 1.</b></td></tr> 
        <tr>
            <td align="left">Mother's Name<input name="MothersName" type="text" value="<%= props.getProperty("MothersName", "") %>" @oscar.formDB />
                                                  </td>
            <td align="center">Age<br><input size="3" name="MothersAge" type="text" value="<%= props.getProperty("MothersAge", "") %>" @oscar.formDB /></td>
            <td align="center">Mother's Hospital ID #<input name="MothersHospitalID" type="text" value="<%= props.getProperty("MothersHospitalID", "") %>" @oscar.formDB /></td>
        </tr>
        <tr>
            <td>Surname of newborn<br><input name="SurNewBorn" value="<%= props.getProperty("SurNewBorn", "") %>" @oscar.formDB /></td>
            <td>Partner's Name<br><input name="FathersName" value="<%= props.getProperty("FathersName", "") %>" @oscar.formDB /></td>
            <td>Age<br><input size="3" name="FathersAge" value="<%= props.getProperty("FathersAge", "") %>" @oscar.formDB /></td>
        </tr>
        <tr>
            <td>
                G<input size="1" name="G" value="<%= props.getProperty("G", "") %>" @oscar.formDB />
                        T<input size="1" name="T" value="<%= props.getProperty("T", "") %>" @oscar.formDB />
                        P<input size="1" name="P" value="<%= props.getProperty("P", "") %>" @oscar.formDB />
                        A<input size="1" name="A" value="<%= props.getProperty("A", "") %>" @oscar.formDB />
                    L<input size="1" name="L" value="<%= props.getProperty("L", "") %>" @oscar.formDB /></td>
            <td>EDD <input size="8" name="EDD" id="EDD" value="<%= props.getProperty("EDD", "") %>" @oscar.formDB /><img src="../images/cal.gif" id="EDD_cal"><br>dd/mm/yy </td>
            <td >by <input name="LMP" type="checkbox" <%= props.getProperty("LMP", "") %> @oscar.formDB dbType="tinyint(1)" />LMP <input name="US" type="checkbox" <%= props.getProperty("US", "") %> @oscar.formDB dbType="tinyint(1)" />US</td>
        </tr>
        <tr>

        <td colspan="3" nowrap="nowrap"> Blood Group:
            </font>

            <select name="BloodGroup">
                <%
                String[] optBG1 = {"", "O", "A", "B", "AB"};
                for (int i=0; i<optBG1.length; i++) {
                %>
                <option value="<%=optBG1[i]%>" <%=props.getProperty("BloodGroup", "").equals(optBG1[i])?"selected":""%> > <%=optBG1[i]%></option>
                <%}%>
            </select>

Rh:
            <select name="Rh" @oscar.formDB>
                    <option value="" <%=props.getProperty("Rh", "").equals("")?"selected":""%>></option>
                <option value="pos" <%=props.getProperty("Rh", "").equals("pos")?"selected":""%>>pos</option>
                <option value="neg" <%=props.getProperty("Rh", "").equals("neg")?"selected":""%>>neg</option>
            </select>
Antibodies:
            <select name="Antibodies" @oscar.formDB>
                    <option value="" <%=props.getProperty("Antibodies", "").equals("")?"selected":""%>></option>
                <option value="None" <%=props.getProperty("Antibodies", "").equals("None")?"selected":""%>>None</option>
                <option value="+ve"  <%=props.getProperty("Antibodies", "").equals("+ve")?"selected":""%>>+ve</option>
                <option value="-ve"  <%=props.getProperty("Antibodies", "").equals("-ve")?"selected":""%>>-ve</option>
            </select>



        </td>
        </tr>
        <tr>
            <td colspan="3" nowrap="nowrap">
                Risk Factors for Infact (Refer to Antenatal Record, Part 2)<br>
                Exposure to Substances: <input name="ExposuretoSubstancesTobacco" type="checkbox" <%= props.getProperty("ExposuretoSubstancesTobacco", "") %> @oscar.formDB dbType="tinyint(1)" /> Tobacco <input name="ExposuretoSubstancesAlcohol" type="checkbox" <%= props.getProperty("ExposuretoSubstancesAlcohol", "") %> @oscar.formDB dbType="tinyint(1)" /> Alcohol <input name="ExposuretoSubstancesMedication" type="checkbox" <%= props.getProperty("ExposuretoSubstancesMedication", "") %> @oscar.formDB dbType="tinyint(1)" /> Medication <br>
                <input name="ExposuretoSubstancesOther" type="checkbox" <%= props.getProperty("ExposuretoSubstancesOther", "") %> @oscar.formDB dbType="tinyint(1)" />  Other <input name="ExposuretoSubstancesOtherText" type="text" value="<%= props.getProperty("ExposuretoSubstancesOtherText", "") %>" @oscar.formDB /><br>
                Other Risk: <input name="ExposuretoSubstancesOtherRisk" type="text" value="<%= props.getProperty("ExposuretoSubstancesOtherRisk", "") %>" @oscar.formDB />
               </td>
        </tr>
    </table>



</td>


<td width="50%">

    <table cellpadding="0" cellspacing="0" border="0" width="100%">
            <tr>
                <td >HOSPITAL NAME<br>
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
                       /></td>
            </tr>
            <tr>
                <td>SURNAME<br>
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
                <td colspan="2"><span class="small9"> 
                
<a href="javascript: function myFunction() {return false; }" onClick="popupFixedPage(600, 300, 'formbcarpg1namepopup.jsp?fieldname=c_phyMid'); return false;">               
                PHYSICIAN
                    / MIDWIFE NAME</a></span><br>
                    <input type="text" name="c_phyMid" style="width: 100%" size="30"
                           maxlength="60" value="<%= props.getProperty("c_phyMid", "") %>" @oscar.formDB /></td>
            </tr>
        </table>
</td>
</tr>
</table>
<table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td width="40%" align="left" valign="top">

        <table cellpadding="0" cellspacing="0" border="1" width="100%" style="font-size: 10px;"  summary="2. Apgar Score">
         <tr><td colspan="7" style="font-size: 14; "><b>2. Apgar Score</b></td></tr>
            <tr>
                    <td>&nbsp;</td>
                    <td align="center">0</td>
                    <td align="center">1</td>
                    <td align="center">2</td>
                    <td align="center">1 min</td>
                    <td align="center">5 min</td>
                    <td align="center">10 min</td>
                </tr>
                <tr>
                    <td><b>Heart<br>Rate</b></td>
                    <td>Absent</td>
                    <td>&lt; 100</td>
                    <td>&#8805; 100</td>
                    <td align="center"><input size="1" name="HeartRate1min" type="text"  value="<%= props.getProperty("HeartRate1min", "") %>" /></td>
                    <td align="center"><input size="1" name="HeartRate5min" type="text" value="<%= props.getProperty("HeartRate5min", "") %>" /></td>
                    <td align="center"><input size="1" name="HeartRate10min" type="text" value="<%= props.getProperty("HeartRate10min", "") %>" /></td>
                </tr>
                <tr>
                    <td><b>Resp.<br>Effect</b></td>
                    <td>Absent</td>
                    <td>Weak Cry<br>Hypo-<br>ventila-<br>tion</td>
                    <td>Good<br>Crying</td>
                    <td align="center"><input size="1" name="Resp1min" type="text" value="<%= props.getProperty("Resp1min", "") %>" @oscar.formDB /></td>
                    <td align="center"><input size="1" name="Resp5min" type="text" value="<%= props.getProperty("Resp5min", "") %>" @oscar.formDB /></td>
                    <td align="center"><input size="1" name="Resp10min" type="text" value="<%= props.getProperty("Resp10min", "") %>" @oscar.formDB ></td>
                </tr>
                <tr>
                    <td><b>Muscle<br>Tone</b></td>
                    <td>Limp</td>
                    <td>Some<br>Flexion</td>
                    <td>Active<br>Motion</td>
                    <td align="center"><input size="1" name="MuscleTone1min" type="text" value="<%= props.getProperty("MuscleTone1min", "") %>" @oscar.formDB /></td>
                    <td align="center"><input size="1" name="MuscleTone5min" type="text" value="<%= props.getProperty("MuscleTone5min", "") %>" @oscar.formDB /></td>
                    <td align="center"><input size="1" name="MuscleTone10min" type="text" value="<%= props.getProperty("MuscleTone10min", "") %>" @oscar.formDB /></td>
                </tr>
                <tr>
                    <td><b>Resp.<br>to<br>Stim</b></td>
                    <td>None</td>
                    <td>Grimace</td>
                    <td>Active<br>With-<br>drawal</td>
                    <td align="center"><input size="1" name="ResptoStim1min" type="text" value="<%= props.getProperty("ResptoStim1min", "") %>" @oscar.formDB /></td>
                    <td align="center"><input size="1" name="ResptoStim5min" type="text" value="<%= props.getProperty("ResptoStim5min", "") %>" @oscar.formDB  /></td>
                    <td align="center"><input size="1" name="ResptoStim10min" type="text" value="<%= props.getProperty("ResptoStim10min", "") %>" @oscar.formDB /></td>
                </tr>
                <tr>
                    <td><b>Colour</b></td>
                    <td>Blue<br>Pale</td>
                    <td>Acro-<br>cyanosis</td>
                    <td>All Pink</td>
                    <td align="center"><input size="1" name="Colour1min" type="text" value="<%= props.getProperty("Colour1min", "") %>" @oscar.formDB /></td>
                    <td align="center"><input size="1" name="Colour5min" type="text" value="<%= props.getProperty("Colour5min", "") %>" @oscar.formDB /></td>
                    <td align="center"><input size="1" name="Colour10min" type="text"value="<%= props.getProperty("Colour10min", "") %>" @oscar.formDB /></td>
                </tr>
                <tr>
                    <td colspan="4"><b>Apgar Total Score</b></td>
                    <td align="center"><input size="2" name="Total1min" type="text" value="<%= props.getProperty("Total1min", "") %>" @oscar.formDB /></td>
                    <td align="center"><input size="2" name="Total5min" type="text" value="<%= props.getProperty("Total5min", "") %>" @oscar.formDB  /></td>
                    <td align="center"><input size="2" name="Total10min" type="text" value="<%= props.getProperty("Total10min", "") %>" @oscar.formDB /></td>
                </tr>
        </table>

</td>

<td width="70%" align="left" valign="top">
        <table cellpadding="0" cellspacing="0" border="1" width="60%" style="font-size: 10px;"  summary="3. Transistion to One Hour of Age">
         <tr><td colspan="3" style="font-size: 14;"><b>3. Transition to One Hour of Age</b></td></tr>
            <tr>
                    <td colspan="3">
                        Positioned:
                        <input name="SkinToSkin" type="checkbox" <%= props.getProperty("SkinToSkin", "") %> @oscar.formDB dbType="tinyint(1)" /> Skin-to-Skin
                               <input name="RadiantWarmer" type="checkbox" <%= props.getProperty("RadiantWarmer", "") %> @oscar.formDB dbType="tinyint(1)" /> Radiant Warmer
                               <input name="PositionedOther" type="checkbox" <%= props.getProperty("PositionedOther", "") %> @oscar.formDB dbType="tinyint(1)" /> Other:
                               <input name="PositionedOtherText" type="text" value="<%= props.getProperty("PositionedOtherText", "") %>" @oscar.formDB />
                               <br>
                        Amniotic Fluid:
                        <input name="AmnioticFluidClear" type="checkbox" <%= props.getProperty("AmnioticFluidClear", "") %> @oscar.formDB dbType="tinyint(1)" /> Clear
                               <input name="AmnioticFluidMeconium" type="checkbox" <%= props.getProperty("AmnioticFluidMeconium", "") %> @oscar.formDB dbType="tinyint(1)" /> Meconium
                               <input name="AmnioticFluidBloody" type="checkbox" <%= props.getProperty("AmnioticFluidBloody", "") %> @oscar.formDB dbType="tinyint(1)" /> Bloody
                               <br>
                        Suction: <input name="SuctionOropharyngeal" type="checkbox" <%= props.getProperty("SuctionOropharyngeal", "") %> @oscar.formDB dbType="tinyint(1)" /> Oropharyngeal
                                        <input name="SuctionTrachea" type="checkbox" <%= props.getProperty("SuctionTrachea", "") %> @oscar.formDB dbType="tinyint(1)" /> Trachea
                               <input name="SuctionMecBelowCords" type="checkbox" <%= props.getProperty("SuctionMecBelowCords", "") %> @oscar.formDB dbType="tinyint(1)" /> Mec. Below Cords
                               <input name="SuctionStomachAspirated" type="checkbox"  <%= props.getProperty("SuctionStomachAspirated", "") %> @oscar.formDB dbType="tinyint(1)" /> Stomach Aspirated
                               <br>
                        Oxygen: <input name="OxygenNone" type="checkbox" <%= props.getProperty("OxygenNone", "") %> @oscar.formDB dbType="tinyint(1)" /> None
                                       <input name="OxygenFreeFlow" type="checkbox" <%= props.getProperty("OxygenFreeFlow", "") %> @oscar.formDB dbType="tinyint(1)" />Free Flow Start
                               <input name="OxygenStart" type="text" value="<%= props.getProperty("OxygenStart", "") %>" @oscar.formDB />min. Stop
                               <input name="OxygenStop" type="text" value="<%= props.getProperty("OxygenStop", "") %>" @oscar.formDB />min.
                               <br>
                        <input name="OxygenIPPV" type="checkbox" <%= props.getProperty("OxygenIPPV", "") %> @oscar.formDB dbType="tinyint(1)" /> IPPV per mask Start
                               <input name="OxygenIPPVStart" type="text" value="<%= props.getProperty("OxygenIPPVStart", "") %>" @oscar.formDB />min. Stop
                               <input name="OxygenIPPVStop" type="text" value="<%= props.getProperty("OxygenIPPVStop", "") %>" @oscar.formDB /> min.
                               <br>
                        <input name="OxygenSeeExpanded" type="checkbox" <%= props.getProperty("OxygenSeeExpanded", "") %> @oscar.formDB dbType="tinyint(1)" /> See Expanded Resuscitation Form
                               <br>
                        Cord Gases: <input name="OxygenCordGases" type="checkbox"  <%= props.getProperty("OxygenCordGases", "") %> @oscar.formDB dbType="tinyint(1)" /> Done (see lab results)
                                           <input name="OxygenNotDone" type="checkbox" <%= props.getProperty("OxygenNotDone", "") %> @oscar.formDB dbType="tinyint(1)" /> Not Done
                               <br>
                        Temperature: <input name="Section3Tempeature" type="text" value="<%= props.getProperty("Section3Tempeature", "") %>" @oscar.formDB />  &#8451;
                                           <br>
                        Pulse Oximetry: <input name="Section3PulseOximetryYes" type="checkbox" <%= props.getProperty("Section3PulseOximetryYes", "") %> @oscar.formDB dbType="tinyint(1)" /> Yes
                                               <input name="Section3PulseOximetryNo" type="checkbox" <%= props.getProperty("Section3PulseOximetryNo", "") %> @oscar.formDB dbType="tinyint(1)" /> No
                               <br>
                        Heart Rate: <input name="Section3HeartRate" type="text" value="<%= props.getProperty("Section3HeartRate", "") %>" @oscar.formDB />  Time to HR &#8805;100
                                           <input name="Section3HeartRateMin" type="text" value="<%= props.getProperty("Section3HeartRateMin", "") %>" @oscar.formDB /> min.
                               <input name="Section3HeartRateSec" type="text" value="<%= props.getProperty("Section3HeartRateSec", "") %>" @oscar.formDB /> sec.
                               <br>
                        Respirations: <input name="Section3Respirations" type="text" value="<%= props.getProperty("Section3Respirations", "") %>" @oscar.formDB /> Time to Spontaneous Breathing
                                             <input name="Section3MinTimetoSpontaneousBrathingMin" type="text" value="<%= props.getProperty("Section3MinTimetoSpontaneousBrathingMin", "") %>" @oscar.formDB /> min.
                               <input name="Section3MinTimetoSpontaneousBrathingSec" type="text" value="<%= props.getProperty("Section3MinTimetoSpontaneousBrathingSec", "") %>" @oscar.formDB /> sec.
                           </td>
                </tr>
                <tr>
                    <td>Signature<br><input name="Section3SigRMRN1" type="text"  value="<%= props.getProperty("Section3SigRMRN1", "") %>" @oscar.formDB /> <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;RM/RN</td>
                    <td>Signature<br><input name="Section3SigRMRN2" type="text" value="<%= props.getProperty("Section3SigRMRN2", "") %>" @oscar.formDB /> <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;RM/RN</td>
                    <td>Signature<br><input name="Section3SigMD" type="text" value="<%= props.getProperty("Section3SigMD", "") %>" @oscar.formDB /> <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MD</td>
                </tr>

      </table>

</td>
</tr>


<tr>
<td width="40%" align="left" valign="top">
        <table cellpadding="0" cellspacing="0" border="1" width="100%" style="font-size: 10px;"  summary="4. Delivery">
        <tr><td colspan="2" style="font-size: 14; "><b>4. Delivery</b></td></tr>
            <tr>
                    <td>
                        Birthdate <input  size="10" maxlength="10" name="Section4Birthdate" id="Section4Birthdate" type="text" value="<%= props.getProperty("Section4Birthdate", "") %>" @oscar.formDB /><img src="../images/cal.gif" id="Section4Birthdate_cal">
                    </td>
                    <td><input  size="8" maxlength="8"  name="Section4time" type="text" value="<%= props.getProperty("Section4time", "") %>" @oscar.formDB />time</td>
                </tr>
                <tr>
                    <td>Delivery Type
                        <select name="Section4DeliveryType">
                            <option value="" <%=props.getProperty("Section4DeliveryType", "").equals("")?"selected":""%>></option>
                            <option value="SVD" <%=props.getProperty("Section4DeliveryType", "").equals("SVD")?"selected":""%>>SVD</option>
                            <option value="C-section" <%=props.getProperty("Section4DeliveryType", "").equals("C-section")?"selected":""%>>C-section</option>
                            <option value="Vacuum" <%=props.getProperty("Section4DeliveryType", "").equals("Vacuum")?"selected":""%>>Vacuum</option>
                            <option value="Forceps" <%=props.getProperty("Section4DeliveryType", "").equals("Forceps")?"selected":""%>>Forceps</option>
                            <option value="Vacuum and Forceps" <%=props.getProperty("Section4DeliveryType", "Vacuum and Forceps").equals("")?"selected":""%>>Vacuum and Forceps</option>
                            <option value="Forceps Trial and C-section" <%=props.getProperty("Section4DeliveryType", "").equals("Forceps Trial and C-section")?"selected":""%>>Forceps Trial and C-section</option>
                        </select>
                    </td>
                    <td>Newborn Hospital # <input name="Section4NewBornHospital" type="text" value="<%= props.getProperty("Section4NewBornHospital", "") %>" @oscar.formDB /></td>
                </tr><tr>
                    <td colspan="2">
                        Identified at Birth by:<br>
                        SIGATURE: <input name="Section4Sig1" type="text" value="<%= props.getProperty("Section4Sig1", "") %>" @oscar.formDB />RN/RM<br>
                        Identified at Transfer by: (if applicable) <br>
                        SIGATURE: <input name="Section4Sig2" type="text" value="<%= props.getProperty("Section4Sig2", "") %>" @oscar.formDB />RN/RM<br>
                    </td>
                </tr>
                <tr>
                    <td colspan="1">Voided<br><input name="Section4VoidedYes" type="checkbox" <%= props.getProperty("Section4VoidedYes", "") %> @oscar.formDB dbType="tinyint(1)" />Yes
                                                     <input name="Section4VoidedNo" type="checkbox" <%= props.getProperty("Section4VoidedNo", "") %> @oscar.formDB dbType="tinyint(1)" />No</td>
                    <td colspan="1">Passed Meconium<br>
                        <input name="Section4MeconiumYes" type="checkbox" <%= props.getProperty("Section4MeconiumYes", "") %> @oscar.formDB dbType="tinyint(1)" />Yes
                           <input name="Section4MeconiumNo" type="checkbox" <%= props.getProperty("Section4MeconiumNo", "") %> @oscar.formDB dbType="tinyint(1)" />No</td>
                </tr>
                <tr>
                    <td colspan="2">
                        Breastfeeding Planned <input name="Section4BreastfeedingYes" type="checkbox" <%= props.getProperty("Section4BreastfeedingYes", "") %> @oscar.formDB dbType="tinyint(1)" />Yes
                                                     <input name="Section4BreastfeedingNo" type="checkbox" <%= props.getProperty("Section4BreastfeedingNo", "") %> @oscar.formDB dbType="tinyint(1)" />No
                           </td>
                </tr>
       </table>


</td>


<td rowspan="3" align="left" valign="top">

        <table cellpadding="0" cellspacing="0" border="1" align="left" valign="top" width="80%" style="font-size: 10; "  summary="Physical Examination at Birth">
   
	         <tr><td colspan="5" style="font-size: 14; "><b>8. Physical Examination at Birth</b> (Including Stillbirths)</td></tr>
            <tr>
                <td width="10" colspan="2">Gestational Age from<br>Antenatal History
                <input type="text" size="10" maxlength="10" name="Section8GestationalAgeAntenatalHistory" value="<%= props.getProperty("Section8GestationalAgeAntenatalHistory", "") %>" @oscar.formDB />wks
                </td>
                <td width="10"colspan="2">Gestational Age<br>by Exam (see reverse Part 2) <input type="text"  size="10" maxlength="10" name="Section8GestationalAgeByExam" value="<%= props.getProperty("Section8GestationalAgeByExam", "") %>" @oscar.formDB />wks</td>
                <td width="10"><input type="checkbox" name="Section8Male" <%= props.getProperty("Section8Male", "") %> @oscar.formDB dbType="tinyint(1)" />Male
                               <input type="checkbox" name="Section8Female" <%= props.getProperty("Section8Female", "") %> @oscar.formDB dbType="tinyint(1)" /> Female<br>
                <input type="checkbox" name="Section8Undifferentiated" <%= props.getProperty("Section8Undifferentiated", "") %> @oscar.formDB dbType="tinyint(1)" />Undifferentiated</td>
            </tr>
            <tr>
                <td>1. General<br>Appearance</td>
                <td width="10">Normal<br><input type="checkbox" name="Section8GeneralApperanceNormal" <%= props.getProperty("Section8GeneralApperanceNormal", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                <td width="10">Abnormal<br><input type="checkbox" name="Section8GeneralApperanceAbnormal" <%= props.getProperty("Section8GeneralApperanceAbnormal", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                <td width="10" colspan="2">Comments<br><input type="text" name="Section8GeneralApperanceComments" value="<%= props.getProperty("Section8GeneralApperanceComments", "") %>" @oscar.formDB /></td>
            </tr>

            <tr>
                <td width="10">2. Skin</td>
                <td width="10"><input type="checkbox" name="Section8SkinNormal" <%= props.getProperty("Section8SkinNormal", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                <td width="10"><input type="checkbox" name="Section8SkinPallor" <%= props.getProperty("Section8SkinPallor", "") %> @oscar.formDB dbType="tinyint(1)" />Pallor<br>
                    <input type="checkbox" name="Section8SkinBruising" <%= props.getProperty("Section8SkinBruising", "") %> @oscar.formDB dbType="tinyint(1)" />Bruising<br>
                <input type="checkbox" name="Section8SkinPetechiae" <%= props.getProperty("Section8SkinPetechiae", "") %> @oscar.formDB dbType="tinyint(1)" />Petechiae</td>
                <td><input type="checkbox" name="Section8SkinMecStaining" <%= props.getProperty("Section8SkinMecStaining", "") %> @oscar.formDB dbType="tinyint(1)" />Mec. Staining<br>
                    <input type="checkbox" name="Section8SkinPeeling" <%= props.getProperty("Section8SkinPeeling", "") %> @oscar.formDB dbType="tinyint(1)" />Peeling<br>
                <input type="checkbox" name="Section8SkinJaundice" <%= props.getProperty("Section8SkinJaundice", "") %> @oscar.formDB dbType="tinyint(1)" />Jaundice</td>
                <td><input type="text" name="Section8SkinText"  value="<%= props.getProperty("Section8SkinText", "") %>" @oscar.formDB /></td>
            </tr>
            <tr>
                <td width="10">3. Head</td>
                <td><input type="checkbox" name="Section8HeadNormal" <%= props.getProperty("Section8HeadNormal", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                <td><input type="checkbox" name="Section8HeadAbnormal" <%= props.getProperty("Section8HeadAbnormal", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                <td colspan="2"><input type="text" name="Section8HeadText" value="<%= props.getProperty("Section8HeadText", "") %>" @oscar.formDB /></td>
            </tr>

            <tr>
                <td width="10">4. EENT</td>
                <td><input type="checkbox" name="Section8EENTNormal" <%= props.getProperty("Section8EENTNormal", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                <td><input type="checkbox" name="Section8EENTCleftLipPalate" <%= props.getProperty("Section8EENTCleftLipPalate", "") %> @oscar.formDB dbType="tinyint(1)" />Cleft Lip/Palate<br>
                <input type="checkbox" name="Section8EENTMicrognathia" <%= props.getProperty("Section8EENTMicrognathia", "") %> @oscar.formDB dbType="tinyint(1)" />Micrognathia</td>
                <td><input type="checkbox" name="Section8EENTSuspectedChoanalAtresia" <%= props.getProperty("Section8EENTSuspectedChoanalAtresia", "") %> @oscar.formDB dbType="tinyint(1)" />Suspected Choanal Atresia</td>
                <td><input type="text" name="Section8EENTText" value="<%= props.getProperty("Section8EENTText", "") %>" @oscar.formDB /></td>
            </tr>


            <tr>
                <td width="10">5. Respiratory</td>
                <td><input type="checkbox" name="Section8RespiratoryNormal" <%= props.getProperty("Section8RespiratoryNormal", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                <td><input type="checkbox" name="Section8RespiratoryGrunting" <%= props.getProperty("Section8RespiratoryGrunting", "") %> @oscar.formDB dbType="tinyint(1)" />Grunting<br>
                    <input type="checkbox" name="Section8RespiratoryNasalFlaring" <%= props.getProperty("Section8RespiratoryNasalFlaring", "") %> @oscar.formDB dbType="tinyint(1)" />Nasal Flaring<br>
                <input type="checkbox" name="Section8RespiratoryRetracting" <%= props.getProperty("Section8RespiratoryRetracting", "") %> @oscar.formDB dbType="tinyint(1)" />Retracting</td>
                <td><input type="checkbox" name="Section8RespiratoryShallowBreathing" <%= props.getProperty("Section8RespiratoryShallowBreathing", "") %> @oscar.formDB dbType="tinyint(1)" />Shallow Breathing<br>
                <input type="checkbox" name="Section8RespiratoryTachypnea" <%= props.getProperty("Section8RespiratoryTachypnea", "") %> @oscar.formDB dbType="tinyint(1)" />Tachypnea</td>
                <td><input type="text" name="Section8RespiratoryText" value="<%= props.getProperty("Section8RespiratoryText", "") %>" @oscar.formDB /></td>
            </tr>

            <tr>
                <td width="10">6. CVS</td>
                <td><input type="checkbox" name="Section8CVSNormal" <%= props.getProperty("Section8CVSNormal", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                <td><input type="checkbox" name="Section8CVSMurmur" <%= props.getProperty("Section8CVSMurmur", "") %> @oscar.formDB dbType="tinyint(1)" />Murmur<br>
                <input type="checkbox" name="Section8CVSCentralCyanosis" <%= props.getProperty("Section8CVSCentralCyanosis", "") %> @oscar.formDB dbType="tinyint(1)" />Central Cyanosis</td>
                <td><input type="checkbox" name="Section8CVSAbnDelayedFemoralPulses" <%= props.getProperty("Section8CVSAbnDelayedFemoralPulses", "") %> @oscar.formDB dbType="tinyint(1)" />Abn./Delayed Femoral Pulses<br>
                <input type="checkbox" name="Section8CVSAbnormalRateRhythm" <%= props.getProperty("Section8CVSAbnormalRateRhythm", "") %> @oscar.formDB dbType="tinyint(1)" />Abnormal Rate/Rhythm</td>
                <td><input type="text" name="Section8CVSText" value="<%= props.getProperty("Section8CVSText", "") %>" @oscar.formDB /></td>
            </tr>

            <tr>
                <td width="10">7. Abdomen</td>
                <td><input type="checkbox" name="Section8AbdomenNormal" <%= props.getProperty("Section8AbdomenNormal", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                <td><input type="checkbox" name="Section8AbdomenScaphoid" <%= props.getProperty("Section8AbdomenScaphoid", "") %> @oscar.formDB dbType="tinyint(1)" />Scaphoid<br>
                    <input type="checkbox" name="Section8AbdomenDistended" <%= props.getProperty("Section8AbdomenDistended", "") %> @oscar.formDB dbType="tinyint(1)" />Distended<br>
                <input type="checkbox" name="Section8AbdomenHepatomegaly" <%= props.getProperty("Section8AbdomenHepatomegaly", "") %> @oscar.formDB dbType="tinyint(1)" />Hepatomegaly</td>
                <td><input type="checkbox" name="Section8AbdomenSpienomegaly" <%= props.getProperty("Section8AbdomenSpienomegaly", "") %> @oscar.formDB dbType="tinyint(1)" />Splenomegaly<br>
                <input type="checkbox" name="Section8AbdomenAbnormalMass" <%= props.getProperty("Section8AbdomenAbnormalMass", "") %> @oscar.formDB dbType="tinyint(1)" />Abnormal Mass</td>
                <td><input type="text" name="Section8AbdomenText" value="<%= props.getProperty("Section8AbdomenText", "") %>" @oscar.formDB /></td>
            </tr>

            <tr>
                <td width="10">8. Umbilical<br>Cord</td>
                <td><input type="checkbox" name="Section8UmbilicalCordNormal" <%= props.getProperty("Section8UmbilicalCordNormal", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                <td><input type="checkbox" name="Section8UmbilicalCordMecStained" <%= props.getProperty("Section8UmbilicalCordMecStained", "") %> @oscar.formDB dbType="tinyint(1)" />Mec. Stained<br>
                <input type="checkbox" name="Section8UmbilicalCord2Vessels" <%= props.getProperty("Section8UmbilicalCord2Vessels", "") %> @oscar.formDB dbType="tinyint(1)" />2 Vessels</td>
                <td><input type="checkbox" name="Section8UmbilicalCordThin" <%= props.getProperty("Section8UmbilicalCordThin", "") %> @oscar.formDB dbType="tinyint(1)" />Thin</td>
                <td><input type="text" name="Section8UmbilicalCordText"  value="<%= props.getProperty("Section8UmbilicalCordText", "") %>" @oscar.formDB /></td>
            </tr>

            <tr>
                <td width="10">9. Genito-<br>rectal</td>
                <td><input type="checkbox" name="Section8GenitorectalNormal" <%= props.getProperty("Section8GenitorectalNormal", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                <td><input type="checkbox" name="Section8GenitorectalHypospadias" <%= props.getProperty("Section8GenitorectalHypospadias", "") %> @oscar.formDB dbType="tinyint(1)" />Hypospadias<br>
                <input type="checkbox" name="Section8GenitorectalImperforateAnus" <%= props.getProperty("Section8GenitorectalImperforateAnus", "") %> @oscar.formDB dbType="tinyint(1)" />Imperforate Anus</td>
                <td><input type="checkbox" name="Section8GenitorectalUndescendedTestes" <%= props.getProperty("Section8GenitorectalUndescendedTestes", "") %> @oscar.formDB dbType="tinyint(1)" />Undescended Teste(s)</td>
                <td><input type="text" name="Section8GenitorectalText" value="<%= props.getProperty("Section8GenitorectalText", "") %>" @oscar.formDB /></td>
            </tr>

            <tr>
                <td width="10">10. Musculo-<br>skeletal</td>
                <td ><input type="checkbox" name="Section8MusculoSkeletalNormal" <%= props.getProperty("Section8MusculoSkeletalNormal", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                <td><input type="checkbox" name="Section8MusculoSkeletalSpine" <%= props.getProperty("Section8MusculoSkeletalSpine", "") %> @oscar.formDB dbType="tinyint(1)" />Spine<br>
                <input type="checkbox" name="Section8MusculoSkeletalHipAbnormality" <%= props.getProperty("Section8MusculoSkeletalHipAbnormality", "") %> @oscar.formDB dbType="tinyint(1)" />Hip Abnormality<br>
                <td><input type="checkbox" name="Section8MusculoSkeletalExtremityAbnormality" <%= props.getProperty("Section8MusculoSkeletalExtremityAbnormality", "") %> @oscar.formDB dbType="tinyint(1)" />Extremity Abnormality</td>
                <td><input type="text" name="Section8MusculoSkeletaltext" value="<%= props.getProperty("Section8MusculoSkeletaltext", "") %>" @oscar.formDB /></td>
            </tr>


            <tr>
                <td width="10">11. Neuro-<br>logical</td>
                <td><input type="checkbox" name="Section8NeurologicalNormal" <%= props.getProperty("Section8NeurologicalNormal", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                <td><input type="checkbox" name="Section8NeurologicalHypotonia" <%= props.getProperty("Section8NeurologicalHypotonia", "") %> @oscar.formDB dbType="tinyint(1)" />Hypotonia<br>
                <input type="checkbox" name="Section8NeurologicalCry" <%= props.getProperty("Section8NeurologicalCry", "") %> @oscar.formDB dbType="tinyint(1)" />Cry</td>
                <td><input type="checkbox" name="Section8NeurologicalJittery" <%= props.getProperty("Section8NeurologicalJittery", "") %> @oscar.formDB dbType="tinyint(1)" />Jittery<br>
                <input type="checkbox" name="Section8NeurologicalReflexes" <%= props.getProperty("Section8NeurologicalReflexes", "") %> @oscar.formDB dbType="tinyint(1)" />Reflexes<br></td>
                <td><input type="text" name="Section8NeurologicalText" value="<%= props.getProperty("Section8NeurologicalText", "") %>" @oscar.formDB /></td>
            </tr>

            <tr>
                <td width="10">12. Other</td>
                <td colspan="4"><input type="text" name="Section12Other" value="<%= props.getProperty("Section12Other", "") %>" @oscar.formDB /></td>
            </tr>

            
        </table>



</td>



</tr>

<tr>

<td align="left" valign="top">
 	
        <table cellpadding="0" cellspacing="0" border="1"  style="font-size: 10px;"  summary="5. Routine Procedures">
        


        <tr><td colspan="1" style="font-size: 14; "><b>5. Routine Procedures </b></td></tr>
            <tbody><tr>
                    <td>Cord blood <input name="Section5CordbloodYes" type="checkbox" <%= props.getProperty("Section5CordbloodYes", "") %> @oscar.formDB dbType="tinyint(1)" />Rh
                                          <input name="Section5CordbloodNo" type="checkbox" <%= props.getProperty("Section5CordbloodNo", "") %> @oscar.formDB dbType="tinyint(1)" />Other</td>
                </tr>
                <tr>
                    <td>
                        Eye Prophylaxis<br>
                        <input name="Section5EyeProphylaxisErthromycin" type="checkbox" <%= props.getProperty("Section5EyeProphylaxisErthromycin", "") %> @oscar.formDB dbType="tinyint(1)" />Erthromycin
                               <input name="Section5EyeProphylaxisOther" type="checkbox" <%= props.getProperty("Section5EyeProphylaxisOther", "") %> @oscar.formDB dbType="tinyint(1)" />
                               Other: <input name="Section5EyeProphylaxisOtherText" type="text" value="<%= props.getProperty("Section5EyeProphylaxisOtherText", "") %>" @oscar.formDB /><br>
                        <input name="Section5EyeProphylaxisInformedRefusal" type="checkbox" <%= props.getProperty("Section5EyeProphylaxisInformedRefusal", "") %> @oscar.formDB dbType="tinyint(1)" />Informed Refusal
                               Time: <input name="Section5EyeProphylaxisTime" type="text" value="<%= props.getProperty("Section5EyeProphylaxisTime", "") %>" @oscar.formDB />
                                     <br>
                        <input name="Section5sig1" type="text" value="<%= props.getProperty("Section5sig1", "") %>" @oscar.formDB /> RN/RM
                           </td>
                </tr>
                <tr>
                    <td>
                        Vitamin K<br>
                        <input name="Section5VitaminKPO" type="checkbox" <%= props.getProperty("Section5VitaminKPO", "") %> @oscar.formDB dbType="tinyint(1)" /> PO
                               <input name="Section5VitaminKIM" type="checkbox" <%= props.getProperty("Section5VitaminKIM", "") %> @oscar.formDB dbType="tinyint(1)" /> IM Dosage
                               <input name="Section5VitaminKDosage" type="text" value="<%= props.getProperty("Section5VitaminKDosage", "") %>" @oscar.formDB /> Site
                               <input name="Section5VitaminKSite" type="text" value="<%= props.getProperty("Section5VitaminKSite", "") %>" @oscar.formDB /><br>
                        <input name="Section5VitaminKInformedRefusal" type="checkbox" <%= props.getProperty("Section5VitaminKInformedRefusal", "") %> @oscar.formDB dbType="tinyint(1)" /> Informed Refusal
                               Time <input name="Section5VitaminKTime" type="text" value="<%= props.getProperty("Section5VitaminKTime", "") %>" @oscar.formDB /><br>

                        <input name="Section5VitaminKSig2" type="text" value="<%= props.getProperty("Section5VitaminKSig2", "") %>" @oscar.formDB /> RN/RM
                           </td>
                </tr>
        </table>

</td>



<tr>

<td align="left" valign="top">


        <table cellpadding="0" cellspacing="0" border="1" width="100%" style="font-size: 10px;"  summary="6. Evalution of Development">
                <tr><td colspan="3" style="font-size: 14; ">6. <b>Evalution of Development</b><br><div style="font-size: 10px;">(growth chart and curve on reverse)</div>  </td></tr>
            <tr>

                    <td>Birthweight</td>
                    <td><input name="Section6BirthweightG" type="text" size="6" maxlength="6" value="<%= props.getProperty("Section6BirthweightG", "") %>" @oscar.formDB />g</td>
                    <td><input name="Section6BirthweightPercent" type="text" size="6" maxlength="6"  value="<%= props.getProperty("Section6BirthweightPercent", "") %>" @oscar.formDB />%</td>
                </tr>
                <tr>
                    <td>Length</td>
                    <td><input name="Section6LengthCM" size="6" maxlength="6" type="text" value="<%= props.getProperty("Section6LengthCM", "") %>" @oscar.formDB />cm</td>
                    <td><input name="Section6LengthPercent" size="6" maxlength="6" type="text" value="<%= props.getProperty("Section6LengthPercent", "") %>" @oscar.formDB />%</td>
                </tr>
                <tr>
                    <td>Head<br>Circumference</td>
                    <td><input name="Section6HeadCircumferenceCM" size="6" maxlength="6" type="text" value="<%= props.getProperty("Section6HeadCircumferenceCM", "") %>" @oscar.formDB />cm</td>
                    <td><input name="Section6HeadCircumferencePercent" size="6" maxlength="6" type="text" value="<%= props.getProperty("Section6HeadCircumferencePercent", "") %>" @oscar.formDB />%</td>
                </tr>
                <tr>
                    <td colspan="3"><input name="Section6Preterm" type="checkbox" <%= props.getProperty("Section6Preterm", "") %> @oscar.formDB dbType="tinyint(1)" /> Preterm
                                               <input name="Section6Term" type="checkbox" <%= props.getProperty("Section6Term", "") %> @oscar.formDB dbType="tinyint(1)" /> Term
                               <input name="Section6Postterm" type="checkbox" <%= props.getProperty("Section6Postterm", "") %> @oscar.formDB dbType="tinyint(1)" /> Postterm<br>
                        <input name="Section6SGA" type="checkbox" <%= props.getProperty("Section6SGA", "") %> @oscar.formDB dbType="tinyint(1)" /> SGA
                               <input name="Section6AGA" type="checkbox" <%= props.getProperty("Section6AGA", "") %> @oscar.formDB dbType="tinyint(1)" /> AGA
                           <input name="Section6LGA" type="checkbox" <%= props.getProperty("Section6LGA", "") %> @oscar.formDB dbType="tinyint(1)" /> LGA</td>
                </tr>
        </table>
</td>

</tr>



<tr>

<td align="left" valign="top">
        <table cellpadding="0" cellspacing="0" border="1" align="left" valign="top" width="100%" style="font-size: 12px;"  summary="Stillbirth">
                            <tr><td colspan="3" style="font-size: 14; ">  7. <b>Stillbirth</b> </td></tr><tr>
                    <td colspan="2" align="right">Yes&nbsp;&nbsp;&nbsp;</td><td align="center">No</td>
                </tr>
                <tr>
                    <td>Macerated</td>
                    <td align="center"><input name="Section7MaceratedYes" type="checkbox" <%= props.getProperty("Section7MaceratedYes", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                    <td align="center"><input name="Section7MaceratedNo" type="checkbox" <%= props.getProperty("Section7MaceratedNo", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                </tr>
                <tr>
                    <td>IUGR</td>
                    <td align="center"><input name="Section7IUGRYes" type="checkbox" <%= props.getProperty("Section7IUGRYes", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                    <td align="center"><input name="Section7IUGRNo" type="checkbox" <%= props.getProperty("Section7IUGRNo", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                </tr>
                <tr>
                    <td>Retroplacental Clot</td>
                    <td align="center"><input name="Section7RetroplacentalClotYes" type="checkbox" <%= props.getProperty("Section7RetroplacentalClotYes", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                    <td align="center"><input name="Section7RetroplacentalClotNo" type="checkbox" <%= props.getProperty("Section7RetroplacentalClotNo", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                </tr>
                <tr>
                    <td>Evidence of Anemia</td>
                    <td align="center"><input name="Section7EvidenceofAnemiaYes" type="checkbox" <%= props.getProperty("Section7EvidenceofAnemiaYes", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                    <td align="center"><input name="Section7EvidenceofAnemiaNo" type="checkbox" <%= props.getProperty("Section7EvidenceofAnemiaNo", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                </tr>
                <tr>
                    <td>Autopsy Consented</td>
                    <td align="center"><input name="Section7AutopsyConsentedYes" type="checkbox" <%= props.getProperty("Section7AutopsyConsentedYes", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                    <td align="center"><input name="Section7AutopsyConsentedNo" type="checkbox" <%= props.getProperty("Section7AutopsyConsentedNo", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                </tr>
                <tr>
                    <td>Obvious Anomaly (describe below):</td>
                    <td align="center"><input name="Section7ObviousAnomalyYes" type="checkbox" <%= props.getProperty("Section7ObviousAnomalyYes", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                    <td align="center"><input name="Section7ObviousAnomalyNo" type="checkbox" <%= props.getProperty("Section7ObviousAnomalyNo", "") %> @oscar.formDB dbType="tinyint(1)" /></td>
                </tr>
                <tr>
                    <td colspan="3"><input name="Section7ObviousAnomalyText" type="text" value="<%= props.getProperty("Section7ObviousAnomalyText", "") %>" @oscar.formDB /> umbilical cord length
                                           <input size="3" name="Section7umbilicalCM" type="text" value="<%= props.getProperty("Section7umbilicalCM", "") %>" @oscar.formDB />cm</td>
                </tr>
        </tbody></table>

</td>

<td align="left" valign="top">

		<table cellpadding="0" cellspacing="0" border="0" width="80%">
			<tr>
                <td>DATE</td>
                <td>TIME</td>
                <td>SIGNATURE</td>
         </tr>
			<tr>
                <td><input type="text" name="Section8DATE" id="Section8DATE" value="<%= props.getProperty("Section8DATE", "") %>" @oscar.formDB /><img src="../images/cal.gif" id="Section8DATE_cal"></td>
                <td><input type="text" name="Section8TIME" value="<%= props.getProperty("Section8TIME", "") %>" @oscar.formDB /></td>
                <td><input type="text" name="Section8SIGNATURE" value="<%= props.getProperty("Section8SIGNATURE", "") %>" @oscar.formDB /></td>
			</tr>
		</table>

</td>

</tr>

</table>


    </html:form>
    <script type="text/javascript">
        Calendar.setup({ inputField : "EDD", ifFormat : "%d/%m/%Y", showsTime :false, button : "EDD_cal", singleClick : true, step : 1 });
        Calendar.setup({ inputField : "Section8DATE", ifFormat : "%d/%m/%Y", showsTime :false, button : "Section8DATE_cal", singleClick : true, step : 1 });
        Calendar.setup({ inputField : "Section4Birthdate", ifFormat : "%d/%m/%Y", showsTime :false, button : "Section4Birthdate_cal", singleClick : true, step : 1 });
        Calendar.setup({ inputField : "pg1_formDate", ifFormat : "%d/%m/%Y", showsTime :false, button : "pg1_formDate_cal", singleClick : true, step : 1 });

    </script>
    </body>
</html:html>
