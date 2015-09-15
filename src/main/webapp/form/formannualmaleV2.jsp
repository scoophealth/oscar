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
<%@ page import="oscar.form.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Annual Health Review</title>
<link rel="stylesheet" type="text/css" href="annualStyle.css">
<link rel="stylesheet" type="text/css" media="print" href="print.css">
<html:base />
</style>
</head>

<script type="text/javascript" language="Javascript">
    function onPrint() {
        var ret = checkAllDates();
        if(ret==true)
        {
            window.print();
        }
        return ret;
    }
    function popupOscarCon(vheight,vwidth,varpage) {
       var page = varpage;
       windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes, screenX=0,screenY=0,top=0,left=0";
       var popup=window.open(varpage, "<bean:message key="oscarEncounter.Index.msgOscarConsultation"/>", windowprops);
       popup.focus();
    }
    function onSave() {
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
        if(ret==true)
        {
            ret = confirm("Are you sure you want to save this form?");
        }
        return ret;
    }
    
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        var ret = checkAllDates();
        if(ret == true)
        {
            ret = confirm("Are you sure you wish to save and close this window?");
        }
        return ret;
    }
/**
 * DHTML date validation script. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
 */
// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1900;
var maxYear=3100;

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
    //            alert('dateString'+dateString);
                return true;
            }
            var dt = dateString.split('/');
            var y = dt[0];
            var m = dt[1];
            var d = dt[2];
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

    function checkAllDates()
    {
        var b = true;
        if(valDate(document.forms[0].formDate)==false){
            b = false;
        }
        

        return b;

    }
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=5,left=5";//360,680
  var popup=window.open(page, "aplan", windowprops);
}


</script>


<%
	String formClass = "AnnualV2";
//        String formClass = "Annual";
	String formLink = "formannualmaleV2.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);
%>

<BODY bgproperties="fixed" onLoad="javascript:window.focus()"
	topmargin="0" leftmargin="0" rightmargin="0">
<html:form action="/form/formname">


	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="ID"
		value="<%= props.getProperty("ID", "0") %>" />
	<input type="hidden" name="provider_no"
		value=<%=request.getParameter("provNo")%> />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="provNo"
		value="<%= request.getParameter("provNo") %>" />
	<input type="hidden" name="submit" value="exit" />

	<table class="Head">
		<!--class="hidePrint"-->
		<tr>
			<td align="left"><input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <input
				type="submit" value="Exit" onclick="javascript:return onExit();" />
			<input type="button" value="Print"
				onclick="javascript:return onPrint();" /> <input type="button"
				value="Consult"
				onclick="javascript:popupOscarCon(700,960,'../oscarEncounter/oscarConsultationRequest/ConsultationFormRequest.jsp?de=<%=demoNo%>');" />

			</td>
			<td align='right'><a
				href="javascript: popupPage(700,950,'../decision/annualreview/annualreviewplanner.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">Annual
			Review Planner</a></td>
		</tr>
	</table>

	<table>
		<tr>
			<td>
			<table cellspacing="3" cellpadding="0" width="100%">
				<tr>
					<td><big><i><b>ANNUAL MALE HEALTH REVIEW</b></i></big></td>
					<td><b>Name:</b> <input type="text" class="Input" name="pName"
						readonly="true" size="30"
						value="<%= props.getProperty("pName", "") %>" /></td>
					<td><b>Age:</b> <input type="text" class="Input"
						readonly="true" name="age" size="11"
						value="<%= props.getProperty("age", "") %>" /></td>
					<td><b>Date</b><small>(yyyy/mm/dd)</small>: <input type="text"
						class="Input" name="formDate" size="11"
						value="<%=props.getProperty("formDate", "") %>" /></td>
				</tr>
			</table>
			<table width="100%" class="FixedTableWithBorder">
				<tr>
					<td align="center" valign=top>
					<table width="100%">
						<tr>
							<td class="HeadingsReqOhip">PMHX/PSHX:*</td>
							<td class="HeadingNotOhip">Updated<input type="checkbox"
								name="pmhxPshxUpdated"
								<%=props.getProperty("pmhxPshxUpdated", "") %> /></td>
						</tr>
						<tr>
							<td class="HeadingsReqOhip">FamHx:*</td>
							<td class="HeadingNotOhip">Updated<input type="checkbox"
								name="famHxUpdated" <%=props.getProperty("famHxUpdated", "") %> /></td>
						</tr>
						<tr>
							<td class="HeadingsReqOhip">SocHx:*</td>
							<td class="HeadingNotOhip">Updated<input type="checkbox"
								name="socHxUpdated" <%=props.getProperty("socHxUpdated", "") %> /></td>
						</tr>
						<tr>
							<td class="HeadingNotOhip">Allergies:</td>
							<td class="HeadingNotOhip">Updated<input type="checkbox"
								name="allergiesUpdated"
								<%=props.getProperty("allergiesUpdated", "") %> /></td>
						</tr>
						<tr>
							<td class="HeadingNotOhip">Medications:</td>
							<td class="HeadingNotOhip">Updated<input type="checkbox"
								name="medicationsUpdated"
								<%=props.getProperty("medicationsUpdated", "") %> /></td>
						</tr>

					</table>
					</td>
					<td align="center">
					<table cellspacing=0>
						<tr>
							<td class="HeadingsReqOhip">Wt:*</td>
							<td><input type="text" name="weight"
								value="<%=props.getProperty("weight", "") %>" /></td>
						<tr>
						<tr>
							<td class="HeadingsReqOhip">Ht:*</td>
							<td><input type="text" name="height"
								value="<%=props.getProperty("height", "") %>" /></td>
						<tr>
						<tr>
							<td class="HeadingNotOhip">Waist (100cm):</td>
							<td><input type="text" name="waist"
								value="<%=props.getProperty("waist", "") %>" /></td>
						<tr>
						<tr>
							<td class="HeadingsReqOhip">BP:*</td>
							<td><input type="text" name="BP"
								value="<%=props.getProperty("BP", "") %>" /></td>
						<tr>
					</table>
					</td>
				</tr>
			</table>
			<br>
			<table class="FixedTableWithBorder">
				<tr>
					<td valign="top">
					<table>
						<tr>
							<td colspan="3" nowrap="true" align=center
								class="HeadingsReqOhip">Lifestyle Review:</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td class="Headings">No</td>
							<td class="Headings">Yes</td>
							<td>&nbsp;</td>
						</tr>
						<tr>

							<td class="listItemReqOhip">Smoking:*</td>
							<td><input type="checkbox" name="smokingNo"
								<%= props.getProperty("smokingNo", "") %> /></td>
							<td><input type="checkbox" name="smokingYes"
								<%= props.getProperty("smokingYes", "") %> /></td>
							<td align="right"><input type="text" name="smoking"
								class="LifestyleReview"
								value="<%= props.getProperty("smoking", "") %>" /></td>
						</tr>
						<tr>
							<td class="listItemReqOhip">ETOH:*</td>
							<td><input type="checkbox" name="etohNo"
								<%= props.getProperty("etohNo", "") %> /></td>
							<td><input type="checkbox" name="etohYes"
								<%= props.getProperty("etohYes", "") %> /></td>
							<td align="right"><input type="text" name="etoh"
								class="LifestyleReview"
								value="<%= props.getProperty("etoh", "") %>" /></td>
						</tr>
						<tr>
							<td class="listItem">Caffeine:</td>
							<td><input type="checkbox" name="caffineNo"
								<%= props.getProperty("caffineNo", "") %> /></td>
							<td><input type="checkbox" name="caffineYes"
								<%= props.getProperty("caffineYes", "") %> /></td>
							<td align="right"><input type="text" name="caffine"
								class="LifestyleReview"
								value="<%= props.getProperty("caffine", "") %>" /></td>
						</tr>

						<tr>
							<td class="listItemReqOhip">OTC/Illicit Drugs:*</td>
							<td><input type="checkbox" name="otcNo"
								<%= props.getProperty("otcNo", "") %> /></td>
							<td><input type="checkbox" name="otcYes"
								<%= props.getProperty("otcYes", "") %> /></td>
							<td align="right"><input type="text" name="otc"
								class="LifestyleReview"
								value="<%= props.getProperty("otc", "") %>" /></td>
						</tr>
						<tr>
							<td class="listItem">Exercise/Sports:</td>
							<td><input type="checkbox" name="exerciseNo"
								<%= props.getProperty("exerciseNo", "") %> /></td>
							<td><input type="checkbox" name="exerciseYes"
								<%= props.getProperty("exerciseYes", "") %> /></td>
							<td align="right"><input type="text" name="exercise"
								class="LifestyleReview"
								value="<%= props.getProperty("exercise", "") %>" /></td>
						</tr>
						<tr>
							<td class="listItemReqOhip">Nutrition:</td>
							<td><input type="checkbox" name="nutritionNo"
								<%= props.getProperty("nutritionNo", "") %> /></td>
							<td><input type="checkbox" name="nutritionYes"
								<%= props.getProperty("nutritionYes", "") %> /></td>
							<td align="right"><input type="text" name="nutrition"
								class="LifestyleReview"
								value="<%= props.getProperty("nutrition", "") %>" /></td>
						</tr>
						<tr>
							<td class="listItem">Dental Hygiene:</td>
							<td><input type="checkbox" name="dentalNo"
								<%= props.getProperty("dentalNo", "") %> /></td>
							<td><input type="checkbox" name="dentalYes"
								<%= props.getProperty("dentalYes", "") %> /></td>
							<td align="right"><input type="text" name="dental"
								class="LifestyleReview"
								value="<%= props.getProperty("dental", "") %>" /></td>
						</tr>
						<tr>
							<td nowrap="true" class="listItem">Occupational Risks:</td>
							<td><input type="checkbox" name="occupationalNo"
								<%= props.getProperty("occupationalNo", "") %> /></td>
							<td><input type="checkbox" name="occupationalYes"
								<%= props.getProperty("occupationalYes", "") %> /></td>
							<td align="right"><input type="text" name="occupational"
								class="LifestyleReview"
								value="<%= props.getProperty("occupational", "") %>" /></td>
						</tr>
						<tr>
							<td nowrap="true" class="listItem">Foreign Travel (in last
							yr.):</td>
							<td><input type="checkbox" name="travelNo"
								<%= props.getProperty("travelNo", "") %> /></td>
							<td><input type="checkbox" name="travelYes"
								<%= props.getProperty("travelYes", "") %> /></td>
							<td align="right"><input type="text" name="travel"
								class="LifestyleReview"
								value="<%= props.getProperty("travel", "") %>" /></td>
						</tr>
						<tr>
							<td nowrap="true" class="listItem">Sexual
							Health/Relationships:</td>
							<td><input type="checkbox" name="sexualityNo"
								<%= props.getProperty("sexualityNo", "") %> /></td>
							<td><input type="checkbox" name="sexualityYes"
								<%= props.getProperty("sexualityYes", "") %> /></td>
							<td align="right"><input type="text" name="sexuality"
								class="LifestyleReview"
								value="<%= props.getProperty("sexuality", 
"") %>" /></td>
						</tr>
					</table>
					</td>
					<td>
					<table width="100%">
						<tr>
							<td colspan="4" align="center" class="HeadingsReqOhip">Functional
							Inquiry*/Current Concerns:</td>

						</tr>
						<tr>
							<td>&nbsp;</td>
							<td class="Headings">N</td>
							<td class="Headings">AbN</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td align="left" nowrap="true"
								title="(sleep, energy, wt. loss, appetite, etc.)"
								class="listItemReqOhip">General*:</td>
							<td><input type="checkbox" name="generalN"
								<%= props.getProperty("generalN", "") %> /></td>
							<td><input type="checkbox" name="generalAbN"
								<%= props.getProperty("generalAbN", "") %> /></td>
							<td align="right"><input type="text" name="general"
								class="SystemsReview"
								value="<%= props.getProperty("general", "") %>" /></td>
						</tr>

						<tr>
							<td align="left" nowrap="true" class="listItemReqOhip">H/N:*</td>
							<td><input type="checkbox" name="headN"
								<%= props.getProperty("headN", "") %> /></td>
							<td><input type="checkbox" name="headAbN"
								<%= props.getProperty("headAbN", "") %> /></td>
							<td align="right"><input type="text" name="head"
								class="SystemsReview"
								value="<%= props.getProperty("head", "") %>" /></td>
						</tr>
						<tr>
							<td class="listItemReqOhip">Chest:*</td>
							<td><input type="checkbox" name="chestN"
								<%= props.getProperty("chestN", "") %> /></td>
							<td><input type="checkbox" name="chestAbN"
								<%= props.getProperty("chestAbN", "") %> /></td>
							<td align="right"><input type="text" name="chest"
								class="SystemsReview"
								value="<%= props.getProperty("chest", "") %>" /></td>
						</tr>
						<tr>
							<td class="listItemReqOhip">CVS:*</td>
							<td><input type="checkbox" name="cvsN"
								<%= props.getProperty("cvsN", "") %> /></td>
							<td><input type="checkbox" name="cvsAbN"
								<%= props.getProperty("cvsAbN", "") %> /></td>
							<td align="right"><input type="text" name="cvs"
								class="SystemsReview"
								value="<%= props.getProperty("cvs", "") %>" /></td>
						</tr>
						<tr>
							<td class="listItemReqOhip">G.I.:*</td>
							<td><input type="checkbox" name="giN"
								<%= props.getProperty("giN", "") %> /></td>
							<td><input type="checkbox" name="giAbN"
								<%= props.getProperty("giAbN", "") %> /></td>
							<td align="right"><input type="text" name="gi"
								class="SystemsReview" value="<%= props.getProperty("gi", "") %>" /></td>
						</tr>
						<tr>
							<td class="listItemReqOhip">G.U.:*</td>
							<td><input type="checkbox" name="guN"
								<%= props.getProperty("guN", "") %> /></td>
							<td><input type="checkbox" name="guAbN"
								<%= props.getProperty("guAbN", "") %> /></td>
							<td align="right"><input type="text" name="gu"
								class="SystemsReview" value="<%= props.getProperty("gu", "") %>" /></td>
						</tr>
						<tr>
							<td class="listItemReqOhip">CNS:*</td>
							<td><input type="checkbox" name="cnsN"
								<%= props.getProperty("cnsN", "") %> /></td>
							<td><input type="checkbox" name="cnsAbN"
								<%= props.getProperty("cnsAbN", "") %> /></td>
							<td align="right"><input type="text" name="cns"
								class="SystemsReview"
								value="<%= props.getProperty("cns", "") %>" /></td>
						</tr>
						<tr>
							<td class="listItemReqOhip">MSK:*</td>
							<td><input type="checkbox" name="mskN"
								<%= props.getProperty("mskN", "") %> /></td>
							<td><input type="checkbox" name="mskAbN"
								<%= props.getProperty("mskAbN", "") %> /></td>
							<td align="right"><input type="text" name="msk"
								class="SystemsReview"
								value="<%= props.getProperty("msk", "") %>" /></td>
						</tr>
						<tr>
							<td class="listItemReqOhip">Skin:*</td>
							<td><input type="checkbox" name="skinN"
								<%= props.getProperty("skinN", "") %> /></td>
							<td><input type="checkbox" name="skinAbN"
								<%= props.getProperty("skinAbN", "") %> /></td>
							<td align="right"><input type="text" name="skin"
								class="SystemsReview"
								value="<%= props.getProperty("skin", "") %>" /></td>
						</tr>
						<tr>
							<td class="listItemReqOhip">Mood:*</td>
							<td><input type="checkbox" name="moodN"
								<%= props.getProperty("moodN", "") %> /></td>
							<td><input type="checkbox" name="moodAbN"
								<%= props.getProperty("moodAbN", "") %> /></td>
							<td align="right"><input type="text" name="mood"
								class="SystemsReview"
								value="<%= props.getProperty("mood", "") %>" /></td>
						</tr>
						<tr>
							<td valign="top" class="listItem">Other:</td>
							<td valign="top"><input type="checkbox" name="otherN"
								<%= props.getProperty("otherN", "") %> /></td>
							<td valign="top"><input type="checkbox" name="otherAbN"
								<%= props.getProperty("otherAbN", "") %> /></td>
							<td align="right"><textarea name="other"
								class="SystemsReview" style="height: 50px;"><%= props.getProperty("other", "") %></textarea></td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			<br>
			<table class="FixedTableWithBorder">
				<tr>
					<td>
					<table>
						<tr>
							<td>
							<table>
								<tr>
									<td>
									<table>
										<tr>
											<td class="HeadingsReqOhip">H/N:*</td>
											<td class="Headings">N</td>
											<td class="Headings">AbN</td>
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td class="listItem">Eyes:</td>
											<td><input type="checkbox" name="eyesN"
												<%= props.getProperty("eyesN", "")   %> /></td>
											<td><input type="checkbox" name="eyesAbN"
												<%= props.getProperty("eyesAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="eyes"
												value="<%= props.getProperty("eyes", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItem">Ears:</td>
											<td><input type="checkbox" name="earsN"
												<%= props.getProperty("earsN", "") %> /></td>
											<td><input type="checkbox" name="earsAbN"
												<%= props.getProperty("earsAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="ears"
												value="<%= props.getProperty("ears", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItem">Oropharynx:</td>
											<td><input type="checkbox" name="oropharynxN"
												<%= props.getProperty("oropharynxN", "") %> /></td>
											<td><input type="checkbox" name="oropharynxAbN"
												<%= props.getProperty("oropharynxAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="oropharynx"
												value="<%= props.getProperty("oropharynx", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItem">Thyroid:</td>
											<td><input type="checkbox" name="thyroidN"
												<%= props.getProperty("thyroidN", "") %> /></td>
											<td><input type="checkbox" name="thyroidAbN"
												<%= props.getProperty("thyroidAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="thyroid"
												value="<%= props.getProperty("thyroid", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItem">L. nodes:</td>
											<td><input type="checkbox" name="lnodesN"
												<%= props.getProperty("lnodesN", "") %> /></td>
											<td><input type="checkbox" name="lnodesAbN"
												<%= props.getProperty("lnodesAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="lnodes"
												value="<%= props.getProperty("lnodes", "") %>" /></td>
										</tr>
										<tr>
											<td colspan=4>&nbsp;</td>
										</tr>
										<tr>
											<td class="HeadingsReqOhip">CHEST:*</td>
											<td class="Headings">N</td>
											<td class="Headings">AbN</td>
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td class="listItem">Clear:</td>
											<td><input type="checkbox" name="clearN"
												<%= props.getProperty("clearN", "") %> /></td>
											<td><input type="checkbox" name="clearAbN"
												<%= props.getProperty("clearAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="clear"
												value="<%= props.getProperty("clear", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItem">A/E = bilat:</td>
											<td><input type="checkbox" name="bilatN"
												<%= props.getProperty("bilatN", "") %> /></td>
											<td><input type="checkbox" name="bilatAbN"
												<%= props.getProperty("bilatAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="bilat"
												value="<%= props.getProperty("bilat", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItem">Wheezes:</td>
											<td><input type="checkbox" name="wheezesN"
												<%= props.getProperty("wheezesN", "") %> /></td>
											<td><input type="checkbox" name="wheezesAbN"
												<%= props.getProperty("wheezesAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="wheezes"
												value="<%= props.getProperty("wheezes", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItem">Crackles:</td>
											<td><input type="checkbox" name="cracklesN"
												<%= props.getProperty("cracklesN", "") %> /></td>
											<td><input type="checkbox" name="cracklesAbN"
												<%= props.getProperty("cracklesAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="crackles"
												value="<%= props.getProperty("crackles", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItem">Other:</td>
											<td colspan=2>&nbsp;</td>
											<td><input type="text" class="OnExam" name="chestOther"
												value="<%= props.getProperty("chestOther", "") %>" /></td>

										</tr>
										<tr>
											<td colspan=4>&nbsp;</td>
										</tr>
										<tr>
											<td class="HeadingsReqOhip">CVS:*</td>
											<td class="Headings">N</td>
											<td class="Headings">AbN</td>
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td class="listItemReqOhip">S1,S2:*</td>
											<td><input type="checkbox" name="s1s2N"
												<%= props.getProperty("s1s2N", "") %> /></td>
											<td><input type="checkbox" name="s1s2AbN"
												<%= props.getProperty("s1s2AbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="s1s2"
												value="<%= props.getProperty("s1s2", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItem">Murmur:</td>
											<td><input type="checkbox" name="murmurN"
												<%= props.getProperty("murmurN", "") %> /></td>
											<td><input type="checkbox" name="murmurAbN"
												<%= props.getProperty("murmurAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="murmur"
												value="<%= props.getProperty("murmur", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItem">Periph pulse:</td>
											<td><input type="checkbox" name="periphPulseN"
												<%= props.getProperty("periphPulseN", "") %> /></td>
											<td><input type="checkbox" name="periphPulseAbN"
												<%= props.getProperty("periphPulseAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="periphPulse"
												value="<%= props.getProperty("periphPulse", "") %>" / /></td>
										</tr>
										<tr>
											<td class="listItem">Edema:</td>
											<td><input type="checkbox" name="edemaN"
												<%= props.getProperty("edemaN", "") %> /></td>
											<td><input type="checkbox" name="edemaAbN"<%= props.getProperty("edemaAbN", "") %>/ ></td>
											<td><input type="text" class="OnExam" name="edema"
												value="<%= props.getProperty("edema", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItem">JVP:</td>
											<td><input type="checkbox" name="jvpN"
												<%= props.getProperty("jvpN", "") %> /></td>
											<td><input type="checkbox" name="jvpAbN" <%= props.getProperty("jvpAbN", "") %>//></td>
											<td><input type="text" class="OnExam" name="jvp"
												value="<%= props.getProperty("jvp", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItemReqOhip">HR/rhythm:*</td>
											<td><input type="checkbox" name="rhythmN"
												<%= props.getProperty("rhythmN", "") %> /></td>
											<td><input type="checkbox" name="rhythmAbN"
												<%= props.getProperty("rhythmAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="rhythm"
												value="<%= props.getProperty("rhythm", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItemReqOhip">BP:*</td>
											<td><input type="checkbox" name="chestbpN"
												<%= props.getProperty("chestbpN", "") %> /></td>
											<td><input type="checkbox" name="chestbpAbN"
												<%= props.getProperty("chestbpAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="chestbp"
												value="<%= props.getProperty("chestbp", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItem">Other:</td>
											<td colspan=2>&nbsp;</td>
											<td><input type="text" class="OnExam" name="cvsOther"
												value="<%= props.getProperty("cvsOther", "") %>" /></td>
										</tr>






									</table>
									</td>
								</tr>



							</table>
							</td>
						</tr>
					</table>
					</td>
					<td>
					<table>
						<tr>
							<td>
							<table>
								<tr>
									<td>
									<table>
										<tr>
											<td class="HeadingsReqOhip">BREASTS:*</td>
											<td class="Headings">N</td>
											<td class="Headings">AbN</td>
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td class="listItem">Left:</td>
											<td><input type="checkbox" name="breastLeftN"
												<%= props.getProperty("breastLeftN", "") %> /></td>
											<td><input type="checkbox" name="breastLeftAbN"
												<%= props.getProperty("breastLeftAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="breastLeft"
												value="<%= props.getProperty("breastLeft", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItem">Right:</td>
											<td><input type="checkbox" name="breastRightN"
												<%= props.getProperty("breastRightN", "") %> /></td>
											<td><input type="checkbox" name="breastRightAbN"
												<%= props.getProperty("breastRightAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="breastRight"
												value="<%= props.getProperty("breastRight", "") %>" /></td>
										</tr>
										<tr>
											<td colspan=4>&nbsp;</td>
										</tr>

										<tr>
											<td class="HeadingsReqOhip">ABD:*</td>
											<td class="Headings">N</td>
											<td class="Headings">AbN</td>
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td class="listItem">Soft:</td>
											<td><input type="checkbox" name="softN"
												<%= props.getProperty("softN", "") %> /></td>
											<td><input type="checkbox" name="softAbN"
												<%= props.getProperty("softAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="soft"
												value="<%= props.getProperty("soft", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItem">Tender:</td>
											<td><input type="checkbox" name="tenderN"
												<%= props.getProperty("tenderN", "") %> /></td>
											<td><input type="checkbox" name="tenderAbN"
												<%= props.getProperty("tenderAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="tender"
												value="<%= props.getProperty("tender", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItem">b.s.:</td>
											<td><input type="checkbox" name="bsN"
												<%= props.getProperty("bsN", "") %> /></td>
											<td><input type="checkbox" name="bsAbN"
												<%= props.getProperty("bsAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="bs"
												value="<%= props.getProperty("bs", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItem">Hepatomeg:</td>
											<td><input type="checkbox" name="hepatomegN"
												<%= props.getProperty("hepatomegN", "") %> /></td>
											<td><input type="checkbox" name="hepatomegAbN"
												<%= props.getProperty("hepatomegAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="hepatomeg"
												value="<%= props.getProperty("hepatomeg", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItem">Splenomeg:</td>
											<td><input type="checkbox" name="splenomegN"
												<%= props.getProperty("splenomegN", "") %> /></td>
											<td><input type="checkbox" name="splenomegAbN"
												<%= props.getProperty("splenomegAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="splenomeg"
												value="<%= props.getProperty("splenomeg", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItem">Masses:</td>
											<td><input type="checkbox" name="massesN"
												<%= props.getProperty("massesN", "") %> /></td>
											<td><input type="checkbox" name="massesAbN"
												<%= props.getProperty("massesAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="masses"
												value="<%= props.getProperty("masses", "") %>" /></td>
										</tr>
										<tr>
											<td class="listItem">Rectal:</td>
											<td><input type="checkbox" name="rectalN"
												<%= props.getProperty("rectalN", "") %> /></td>
											<td><input type="checkbox" name="rectalAbN"
												<%= props.getProperty("rectalAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="rectal"
												value="<%= props.getProperty("rectal", "") %>" /></td>
										</tr>
										<tr>
											<td colspan=4>&nbsp;</td>
										</tr>

										<tr>
											<td class="HeadingsReqOhip">&nbsp;</td>
											<td class="Headings">N</td>
											<td class="Headings">AbN</td>
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td class="HeadingsReqOhip">GENITALIA:*</td>
											<td><input type="checkbox" name="examGenitaliaN"
												<%= props.getProperty("examGenitaliaN", "") %> /></td>
											<td><input type="checkbox" name="examGenitaliaAbN"
												<%= props.getProperty("examGenitaliaAbN", "") %> /></td>
											<td><input type="text" class="OnExam"
												name="examGenitalia"
												value="<%= props.getProperty("examGenitalia", "") %>" /></td>
										</tr>
										<tr>
											<td colspan=4>&nbsp;</td>
										</tr>

										<tr>
											<td class="HeadingsReqOhip">MSK:*</td>
											<td><input type="checkbox" name="exammskN"
												<%= props.getProperty("exammskN", "") %> /></td>
											<td><input type="checkbox" name="exammskAbN"
												<%= props.getProperty("exammskAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="exammsk"
												value="<%= props.getProperty("exammsk", "") %>" /></td>
										</tr>
										<tr>
											<td colspan=4>&nbsp;</td>
										</tr>

										<tr>
											<td class="HeadingsReqOhip">Skin:*</td>
											<td><input type="checkbox" name="examskinN"
												<%= props.getProperty("examskinN", "") %> /></td>
											<td><input type="checkbox" name="examskinAbN"
												<%= props.getProperty("examskinAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="examskin"
												value="<%= props.getProperty("examskin", "") %>" /></td>
										</tr>
										<tr>
											<td colspan=4>&nbsp;</td>
										</tr>

										<tr>
											<td class="HeadingsReqOhip">CNS:*</td>
											<td><input type="checkbox" name="examcnsN"
												<%= props.getProperty("examcnsN", "") %> /></td>
											<td><input type="checkbox" name="examcnsAbN"
												<%= props.getProperty("examcnsAbN", "") %> /></td>
											<td><input type="text" class="OnExam" name="examcns"
												value="<%= props.getProperty("examcns", "") %>" /></td>
										</tr>
									</table>
									</td>
								</tr>

							</table>
							</td>
						</tr>
					</table>

					</td>
				</tr>
			</table>
			<br>
			<table class="FixedTableWithBorder">
				<tr>
					<td>
					<table width="100%">
						<tr>
							<td><b>IMPRESSION & PLAN</b></td>
						</tr>
						<tr>
							<td align="center"><textarea name="impressionPlan"
								class="ImpressionPlan"><%= props.getProperty("impressionPlan", "") %></textarea></td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			<br>





			<table class="FixedTableWithBorder">
				<tr>
					<td class="bottomBorder">
					<table border=0 width="100%" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan=3>
							<table cellspacing="0" cellpadding="0" width="100%" border=0>
								<tr>
									<td class="HeadingsReqOhip" width="200">1. Sexual Health:</td>
									<td class="HeadingsReqOhip">&nbsp;</td>
									<td class="HeadingsReqOhip">&nbsp;</td>
									<td class="HeadingsReqOhip">&nbsp;</td>
									<td class="HeadingsReqOhip">&nbsp;</td>
									<td class="HeadingsReqOhip">&nbsp;</td>
									<td class="HeadingsReqOhip">&nbsp;</td>
								</tr>
							</table>
							</td>

						</tr>
						<tr>
							<td colspan=3 class="listItem">Previous Hx Sti's:</td>
						</tr>
						<tr>
							<td colspan=3 class="listItem">Contraception:</td>
						</tr>
						<tr>
							<td colspan=3 class="listItem">Sexual Dysfunction:</td>
						</tr>
						<tr>
							<td colspan=3 class="listItem">Safe Sex:</td>
						</tr>
					</table>
					</td>
					<td class="bottomBorder"><textarea class="ToDos" rows=6
						name="toDoSexualHealth"><%= props.getProperty("toDoSexualHealth", "") %></textarea>
					</td>
				</tr>
				<tr>
					<td class="bottomBorder" valign="top">
					<table cellspacing=0 cellpadding=0 width="100%">
						<tr>
							<td class="HeadingsReqOhip">2. Obesity:</td>
							<td class="listItem">BMI(>27)</td>
							<td align=right class="listItem">Level B</td>
						</tr>
					</table>
					</td>
					<td class="bottomBorder"><textarea class="ToDos" rows=2
						name="toDoObesity"><%= props.getProperty("toDoObesity", "") %></textarea>
					</td>
				</tr>
				<tr>
					<td class="bottomBorder">
					<table width="100%" cellspacing=0 cellpadding=0>
						<tr>
							<td valign=top class="HeadingsReqOhip">3.Cholesterol:</td>
							<td>
							<table>
								<tr>
									<td colspan=2 class="listItem">F> 50 years or 2 +ve Risk
									Factors:</td>
								</tr>
								<tr>
									<td colspan=2 class="HeadingsReqOhip">Risk Factors</td>
								</tr>
								<tr>
									<td class="listItem">CAD</td>
									<td class="listItem">DM</td>
								</tr>
								<tr>
									<td class="listItem">PVD</td>
									<td class="listItem">Stig of inc lipids</td>
								</tr>
								<tr>
									<td class="listItem">CVD</td>
									<td class="listItem">Fam hx CAD</td>
								</tr>
								<tr>
									<td colspan=2 class="listItem">Carotid disease</td>
								</tr>
								<tr>
									<td colspan=2 class="listItem">Fam hx hypertension</td>
								</tr>
							</table>
							</td>
							<td align=right valign=top class="listItem">Level C</td>
						</tr>



					</table>
					</td>
					<td class="bottomBorder"><textarea class="ToDos" rows=8
						name="toDoCholesterol"><%= props.getProperty("toDoCholesterol", "") %></textarea>
					</td>
				</tr>
				<tr>
					<td class="bottomBorder">
					<table width="100%" cellspacing=0 cellpadding=0>
						<tr>
							<td class="HeadingsReqOhip">4. Osteoporosis:</td>
							<td class="listItem">BMD age > 65 yes or 1 major or 2 minor
							RF's</td>
							<td align="right" class="listItem">Level A</td>
						</tr>
					</table>
					<table>
						<tr>
							<td valign=top class="listItem">
							<table>
								<tr>
									<td class="HeadingsReqOhip">Major RF's</td>
								</tr>
								<tr>
									<td class="listItem">Compression #</td>
								</tr>
								<tr>
									<td class="listItem">Fragility # > 40 years</td>
								</tr>
								<tr>
									<td class="listItem">Fam Hx (hip#)</td>
								</tr>
								<tr>
									<td class="listItem">Glucocorticoids > 3 months</td>
								</tr>
								<tr>
									<td class="listItem">Malabsorption</td>
								</tr>
								<tr>
									<td class="listItem">Primary hyperthyroidism</td>
								</tr>
								<tr>
									<td class="listItem">Osteopenia on x-ray</td>
								</tr>
								<tr>
									<td class="listItem">Inc. falls risk</td>
								</tr>
								<tr>
									<td class="listItem">Hypogonadism</td>
								</tr>
							</table>
							</td>
							<td valign=top>
							<table>
								<tr>
									<td class="HeadingsReqOhip">Minor RF's</td>
								</tr>
								<tr>
									<td class="listItem">RA</td>
								</tr>
								<tr>
									<td class="listItem">Clinical hyperthyroidism</td>
								</tr>
								<tr>
									<td class="listItem">Anticonvulsants</td>
								</tr>
								<tr>
									<td class="listItem">Wt < 57kg</td>
								</tr>
								<tr>
									<td class="listItem">Smoking</td>
								</tr>
								<tr>
									<td class="listItem">ETOH</td>
								</tr>
								<tr>
									<td class="listItem">Excessive caffine</td>
								</tr>
								<tr>
									<td class="listItem">Dec. dietary Ca<sup>2+</sup></td>
								</tr>
							</table>

							</td>
						</tr>
					</table>
					<table>
						<tr>
							<td colspan=2 class="listItem">Screening Frequencies:</td>
						</tr>
						<td class="listItem">&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td class="listItem">Annually if at risk for rapid bone loss
						(steroids, immobility)<br>
						q2 to 3 years for TC that increases bone mineral density slowly
						(calcitonin)<br>
						Ca<sup>2+</sup> / Vit D req: Age > 50 yes Ca<sup>2+</sup> 1500mg /
						Vit D 800 IU OD</td>
					</table>
					</td>
					<td class="bottomBorder"><textarea class="ToDos" rows=19
						name="toDoOsteoporosis"><%= props.getProperty("toDoOsteoporosis", "") %></textarea>
					</td>
				</tr>
				<!--<tr>
			<td class="bottomBorder">
				<table width=100%>
				<tr>
					<td rowspan=3 valign=top class="HeadingsReqOhip">5. PAPs:</td>
					<td class="listItem"> annual after becoming sexually active</td>
					<td align=right class="listItem">Level B</td>
				</tr>
				<tr>
					<td colspan=2 class="listItem">if N X3 then q2 yr (B)</td>
				</tr>
				<tr>
					<td colspan=2 class="listItem">if 4 N in 10 yrs and no hx AbN, d/c screening age 70 </td>
				</tr>
				</table>
			</td>
			<td class="bottomBorder"><textarea class="ToDos" rows=3 name="toDoPAPs"><%= props.getProperty("toDoPAPs", "") %></textarea></td>
		</tr>-->
				<!--<tr>
			<td class="bottomBorder" valign=top width="100%">
				<table width="100%" valign="top">
				   <tr>
				      <td class="HeadingsReqOhip">6.Mammogram:</td>
				      <td class="listItem">age 50 - 69 yrs: q2 yrs</td>
				      <td align=right class="listItem">Level A</td>
				   </tr>
				</table>
			</td>
			<td class="bottomBorder"><textarea class="ToDos" rows=3 name="toDoMammogram" ><%= props.getProperty("toDoMammogram", "") %></textarea></td>

		</tr>-->
				<tr>
					<td class="bottomBorder">
					<table>
						<tr>
							<td class="HeadingsReqOhip">5. Colorectal CA:</td>
						</tr>
						<tr>
							<td valign=top>
							<table>
								<tr>
									<td class="listItem">annual FOB age 50 yrs (A)</td>
								</tr>
								<tr>
									<td class="listItem">sig or colonoscopy for high risk (B)</td>
								</tr>
								<tr>
									<td class="listItem">genetic screening (B)</td>
								</tr>
							</table>
							</td>
							<td>
							<table>
								<tr>
									<td class="HeadingsReqOhip">RF's</td>
								</tr>
								<tr>
									<td class="listItem">FPS</td>
								</tr>
								<tr>
									<td class="listItem">IBD</td>
								</tr>
								<tr>
									<td class="listItem">polyps</td>
								</tr>
								<tr>
									<td class="listItem">Fam Hx colon Ca
									<td>
								</tr>
								<tr>
									<td class="listItem">Endometrial/Breast/Ovarian Ca</td>
								</tr>
							</table>
							</td>
						</tr>
					</table>
					</td>
					<td class="bottomBorder"><textarea class="ToDos" rows=7
						name="toDoColorectal"><%= props.getProperty("toDoColorectal", "") %></textarea></td>

				</tr>
				<tr>
					<td class="bottomBorder">
					<table>
						<tr>
							<td class="HeadingsReqOhip">6. Prostate Cancer:</td>
						</tr>
						<tr>
							<td valign=top>
							<table border=0>
								<tr>
									<td colspan=2 class="listItem">DRE - age 50 annually
									(Level C)</td>
								</tr>
								<tr>
									<td colspan=2 class="listItem">PSA - age 50 anually (Level
									D). Need to discuss w pts</td>
								</tr>
								<tr>
									<td class="listItem">&nbsp;&nbsp;&nbsp;&nbsp;</td>
									<td class="listItem">Sen & Spec widely variable:</td>
								</tr>
								<tr>
									<td class="listItem">&nbsp;&nbsp;&nbsp;&nbsp;</td>
									<td class="listItem">Sen 43-79%</td>
								</tr>
								<tr>
									<td class="listItem">&nbsp;&nbsp;&nbsp;&nbsp;</td>
									<td class="listItem">Spec 50-90%</td>
								</tr>
								<tr>
									<td colspan=2 class="listItem">&nbsp;&nbsp;PPV 8-33% ( 67%
									unnecessary bx - conservative)</td>
								</tr>

							</table>
							</td>
						</tr>
					</table>
					</td>
					<td class="bottomBorder"><textarea class="ToDos" rows=8
						name="toDoProstateCancer"><%= props.getProperty("toDoProstateCancer", "") %></textarea></td>

				</tr>
				<tr>
					<td class="bottomBorder">
					<table width="100%">
						<tr>
							<td rowspan=4 class="HeadingsReqOhip" valign="top">7.
							Elderly:</td>
							<td class="listItem">Falls (A)</td>
							<td class="listItem">Home Safety (C)</td>
						</tr>
						<tr>
							<td class="listItem">Vision (B)</td>
							<td class="listItem">Driving</td>
						</tr>
						<tr>
							<td class="listItem">Hearing (B)</td>
							<td class="listItem">A-fib</td>
						</tr>
						<tr>
							<td class="listItem">Cognition (A)</td>
							<td class="listItem">Abuse (C)</td>
						</tr>
					</table>
					</td>
					<td class="bottomBorder"><textarea class="ToDos" rows=5
						name="toDoElderly"><%= props.getProperty("toDoElderly", "") %></textarea></td>
				</tr>
				<tr>
					<td class="bottomBorder">
					<table>
						<tr>
							<td width=200 class="HeadingsReqOhip">8.Immunization</td>
							<td class="listItem">Td<input type="checkbox"
								name="immunizationtd"
								<%= props.getProperty("immunizationtd", "") %> /></td>
							<td class="listItem">Pneumovax<input type="checkbox"
								name="immunizationPneumovax"
								<%= props.getProperty("immunizationPneumovax", "")%> /></td>
							<td class="listItem">Flu<input type="checkbox"
								name="immunizationFlu"
								<%= props.getProperty("immunizationFlu", "") %> /></td>
							<td class="listItem">Menjugate<input type="checkbox"
								name="immunizationMenjugate"
								<%= props.getProperty("immunizationMenjugate", "") %> /></td>
						</tr>
					</table>
					</td>
					<td class="bottomBorder"><textarea class="ToDos" rows=3
						name="toDoImmunization"><%= props.getProperty("toDoImmunization", "") %></textarea></td>
				</tr>

			</table>

			<br>
			<table class="FixedTableWithBorder">
				<tr>
					<td colspan="2" align="right">Signature: <input type="text"
						name="signature" size="30"
						value="<%= props.getProperty("signature", "") %>" /></td>
				</tr>
			</table>

			<table class="Head" class="hidePrint">
				<tr>
					<td align="left"><input type="submit" value="Save"
						onclick="javascript:return onSave();" /> <input type="submit"
						value="Save and Exit" onclick="javascript:return onSaveExit();" />
					<input type="submit" value="Exit"
						onclick="javascript:return onExit();" /> <input type="button"
						value="Print" onclick="javascript:return onPrint();" /></td>
					<td align='right'><a
						href="javascript: popupPage(700,950,'../decision/annualreview/annualreviewplanner.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">Annual
					Review Planner</a></td>
				</tr>
			</table>

			</html:form>
			
</body>
</html:html>
