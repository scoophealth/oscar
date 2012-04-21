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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html:html locale="true">



<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarEncounter.calculators.GeneralCalculators.title" /></title>
<link rel="stylesheet" type="text/css" href="../encounterStyles.css">

<SCRIPT LANGUAGE="JavaScript">

<!-- Generic Unit Conversion Program

// Author    : Jonathan Weesner (jweesner@cyberstation.net)  21 Nov 95

// Copyright : You want it? Take it! ... but leave the Author line intact please!

function convertform(form){

    var firstvalue = 0;

    for (var i = 1; i <= form.count; i++) {

       // Find first non-blank entry

       if (form.elements[i].value != null && form.elements[i].value.length != 0) {

	  if (i == 1 && form.elements[2].value != "") return false;

	  firstvalue = form.elements[i].value / form.elements[i].factor;

	  break;

       }

    }

    if (firstvalue == 0) {

       clearform(form);

       return false;

    }

    for (var i = 1; i <= form.count; i++)

       form.elements[i].value = formatvalue((firstvalue * form.elements[i].factor), form.rsize);

    return true;

}

function formatvalue(input, rsize) {

   var invalid = "**************************";

   var nines = "999999999999999999999999";

   var strin = "" + input;

   var fltin = parseFloat(strin);

   if (strin.length <= rsize) return strin;

   if (strin.indexOf("e") != -1 ||

       fltin > parseFloat(nines.substring(0,rsize)+".4"))

      return invalid.substring(0, rsize);

   var rounded = "" + (fltin + (fltin - parseFloat(strin.substring(0, rsize))));

   return rounded.substring(0, rsize);

}

function resetform(form) {

    clearform(form);

    form.elements[1].value = 1;

    convertform(form);

    return true;

}

function clearform(form) {

    for (var i = 1; i <= form.count; i++) form.elements[i].value = "";

    return true;

}

<!-- done hiding from old browsers -->

</SCRIPT>






<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message
			key="oscarEncounter.calculators.GeneralCalculators.msgCalculators" />
		</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td><bean:message
					key="oscarEncounter.calculators.GeneralCalculators.msgTitle" /></td>
				<td>&nbsp;</td>
				<td style="text-align: right"><oscar:help keywords="calculator" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top"><a href="#distance"><bean:message
			key="oscarEncounter.calculators.GeneralCalculators.msgDistance" /></A> <a
			href="#weight"><bean:message
			key="oscarEncounter.calculators.GeneralCalculators.msgWeight" /></A> <a
			href="#volume"><bean:message
			key="oscarEncounter.calculators.GeneralCalculators.msgVolume" /></A> <a
			href="#temps"><bean:message
			key="oscarEncounter.calculators.GeneralCalculators.msgTemperatures" /></A>


		</td>
		<td class="MainTableRightColumn">
		<table>
			<tr>
				<td style="text-align: center">
				<FORM method="post">
				<TABLE BORDER=2 cellpadding=3 cellspacing=0>
					<TR class="Header">
						<TD COLSPAN=7 ALIGN=CENTER VALIGN=MIDDLE><A NAME="distance"><b><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgDistanceConversion" /></b></A>
						</TD>
					</TR>
					<TR>
						<TD ALIGN=CENTER><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgMeters" /></TD>
						<TD ALIGN=CENTER><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgInches" /></TD>
						<TD ALIGN=CENTER><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgFeet" /></TD>
						<TD ALIGN=CENTER><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgYards" /></TD>
						<TD ALIGN=CENTER><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgMiles" /></TD>
						<TD ALIGN=CENTER><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgNauticalMiles" /></TD>
						<TD><INPUT TYPE="button"
							VALUE="<bean:message key="oscarEncounter.calculators.GeneralCalculators.btnCalibrate"/>"
							onclick="resetform(this.form)"></TD>
					</TR>
					<TR>
						<TD><INPUT TYPE=TEXT NAME=val1 SIZE=7
							onfocus="clearform(this.form)"></TD>
						<TD><INPUT TYPE=TEXT NAME=val2 SIZE=7
							onfocus="clearform(this.form)"></TD>
						<TD><INPUT TYPE=TEXT NAME=val3 SIZE=7
							onfocus="clearform(this.form)"></TD>
						<TD><INPUT TYPE=TEXT NAME=val4 SIZE=7
							onfocus="clearform(this.form)"></TD>
						<TD><INPUT TYPE=TEXT NAME=val5 SIZE=7
							onfocus="clearform(this.form)"></TD>
						<TD><INPUT TYPE=TEXT NAME=val6 SIZE=7
							onfocus="clearform(this.form)"></TD>
						<TD><INPUT TYPE="button"
							VALUE="<bean:message key="oscarEncounter.calculators.GeneralCalculators.btnCalculate"/>"
							onclick="convertform(this.form)"></TD>
					</TR>
				</TABLE>
				</FORM>
				<FORM method="post">
				<TABLE BORDER=2 cellpadding=3 cellspacing=0>
					<TR class="Header">
						<TD COLSPAN=8 ALIGN=CENTER VALIGN=MIDDLE style="font-weight: bold">
						<A NAME="weight"><b><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgWightConversion" /></b></A>
						</TD>
					</TR>
					<TR>
						<TD ALIGN=CENTER><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgKilograms" /></TD>
						<TD ALIGN=CENTER><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgOunces" /></TD>
						<TD ALIGN=CENTER><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgPounds" /></TD>
						<TD ALIGN=CENTER><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgTroyPounds" /></TD>
						<TD ALIGN=CENTER><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgStones" /></TD>
						<TD ALIGN=CENTER><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgShortTons" /></TD>
						<TD ALIGN=CENTER><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgLongTons" /></TD>
						<TD><INPUT TYPE="button"
							VALUE="<bean:message key="oscarEncounter.calculators.GeneralCalculators.btnCalibrate"/>"
							onClick="resetform(this.form)"></TD>
					</TR>
					<TR>
						<TD><INPUT TYPE=TEXT NAME=val1 SIZE=6
							onFocus="clearform(this.form)"></TD>
						<TD><INPUT TYPE=TEXT NAME=val2 SIZE=6
							onFocus="clearform(this.form)"></TD>
						<TD><INPUT TYPE=TEXT NAME=val3 SIZE=6
							onFocus="clearform(this.form)"></TD>
						<TD><INPUT TYPE=TEXT NAME=val4 SIZE=6
							onFocus="clearform(this.form)"></TD>
						<TD><INPUT TYPE=TEXT NAME=val5 SIZE=6
							onFocus="clearform(this.form)"></TD>
						<TD><INPUT TYPE=TEXT NAME=val6 SIZE=6
							onFocus="clearform(this.form)"></TD>
						<TD><INPUT TYPE=TEXT NAME=val7 SIZE=6
							onFocus="clearform(this.form)"></TD>
						<TD><INPUT TYPE="button"
							VALUE="<bean:message key="oscarEncounter.calculators.GeneralCalculators.btnCalculate"/>"
							onclick="convertform(this.form)"></TD>
					</TR>
				</TABLE>
				</FORM>
				<FORM method="post">
				<TABLE border=2 cellpadding=3 cellspacing=0>
					<TR class="Header">
						<TD COLSPAN=7 ALIGN=CENTER VALIGN=MIDDLE><A NAME="volume"><b><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgVolumeConversion" /></b></A>
						</TD>
					</TR>
					<TR>
						<TD ALIGN=CENTER><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgLitres" /></TD>
						<TD ALIGN=CENTER><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgFluid" /></TD>
						<TD ALIGN=CENTER><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgQuarts" /></TD>
						<TD ALIGN=CENTER><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgGallons" /></TD>
						<TD ALIGN=CENTER><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgImperialGallons" /></TD>
						<TD><INPUT TYPE="button"
							VALUE="<bean:message key="oscarEncounter.calculators.GeneralCalculators.btnCalibrate"/>"
							onclick="resetform(this.form)"></TD>
					</TR>
					<TR>
						<TD><INPUT TYPE=TEXT NAME=val1 SIZE=6
							onfocus="clearform(this.form)"></TD>
						<TD><INPUT TYPE=TEXT NAME=val2 SIZE=6
							onfocus="clearform(this.form)"></TD>
						<TD><INPUT TYPE=TEXT NAME=val3 SIZE=6
							onfocus="clearform(this.form)"></TD>
						<TD><INPUT TYPE=TEXT NAME=val4 SIZE=6
							onfocus="clearform(this.form)"></TD>
						<TD><INPUT TYPE=TEXT NAME=val5 SIZE=6
							onfocus="clearform(this.form)"></TD>
						<TD><INPUT TYPE="button"
							VALUE="<bean:message key="oscarEncounter.calculators.GeneralCalculators.btnCalculate"/>"
							onclick="convertform(this.form)"></TD>
					</TR>
				</TABLE>
				</FORM>
				<TABLE border=2 cellpadding=3 cellspacing=0>
					<TR class="Header">
						<TD COLSPAN=3 ALIGN=CENTER VALIGN=MIDDLE><A NAME="temps"><b><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgTemperaturesConversion" /></b></A>
						</TD>
					</TR>
					<TR>
						<TD><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgInstructions1" /></TD>
						<TD><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgInstructions2" /></TD>
						<TD><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgInstructions3" /></TD>
					</TR>
				</table>

				<form method="post">
				<table border=2 cellpadding=3 cellspacing=0 width="100%"
					height="100%">
					<tr>
						<td width="50%" style="text-align: center;" nowrap><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgFahrenheit" />
						<input type="text" name="F" value="32"
							onChange="C.value = 100/(212-32) * (this.value - 32 )"></td>
						<td width="50%" style="text-align: center;" nowrap><bean:message
							key="oscarEncounter.calculators.GeneralCalculators.msgCelsius" />
						<input type="text" name="C" value="0"
							onChange="F.value = (212-32)/100 * this.value + 32"></td>
					</tr>
				</table>
				</form>

				</td>
			</tr>
			<tr>
				<td style="text-align: center">&nbsp;</td>
			</tr>
			<tr>
				<td style="text-align: center">&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
<SCRIPT LANGUAGE="JavaScript">

<!-- Set conversion factors for each item in form. All

// factors must convert the first item to the current item.

// Be sure to use the correct form index. The first form is

// always index "0" and remaining forms are numbered in the

// order they appear in the document.

document.forms[0].count = 6;  // number of unit types

document.forms[0].rsize = 7;  // Rounding size, use same as SIZE

document.forms[0].val1.factor = 1;            // m to m.

document.forms[0].val2.factor = 39.37007874;  // m to in.

document.forms[0].val3.factor = 3.280839895;  // m to ft.

document.forms[0].val4.factor = 1.093613298;  // m to yards.

document.forms[0].val5.factor = 0.00062137119; // m to mi.

document.forms[0].val6.factor = 0.000547045; // m to nm.

<!-- done hiding from old browsers -->

</SCRIPT>

<SCRIPT LANGUAGE="JavaScript">

<!-- Set conversion factors for each item in form.

document.forms[1].count = 7;

document.forms[1].rsize = 6;

document.forms[1].val1.factor = 1;

document.forms[1].val2.factor = 35.273944;

document.forms[1].val3.factor = 2.2046215;

document.forms[1].val4.factor = 2.6792765;

document.forms[1].val5.factor = 0.1574731232747;

document.forms[1].val6.factor = 0.00110231075;

document.forms[1].val7.factor = 0.001;

<!-- done hiding from old browsers -->

</SCRIPT>



<SCRIPT LANGUAGE="JavaScript">

// Set conversion factors for each item in form.

document.forms[2].count = 5;

document.forms[2].rsize = 6;

document.forms[2].val1.factor = 1;

document.forms[2].val2.factor = 33.8239926;

document.forms[2].val3.factor = 1.056998;

document.forms[2].val4.factor = 0.2642499;

document.forms[2].val5.factor = 0.2200433;

</SCRIPT>


</html:html>
