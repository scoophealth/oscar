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

<html:html>



<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarEncounter.calculators.SimpleCalculator.title" /></title>
<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<SCRIPT LANGUAGE="JavaScript">

var x = 0;
var y = 0;
var z = 0;
var clearflag = true;
var lastop = "";
var op = "";
var dirty = false;
var lastDigitEntered = "";


function AddDigit(form, digit){
	//alert(lastop);
  if (lastop != ""){
  	clearEquationView();
  }
  if (dirty){
	clearEquationView();
  }
  if(clearflag){ 
     form.display.value = "";	 
  }
  form.display.value += digit;
  addEquationView(digit);
  lastop = "";
  clearflag = false;
  lastDigitEntered = digit;
}

function Clear(form){
  x = 0;
  y = 0;
  op = "";
  form.display.value = "0";
  lastop = "";
  clearflag = true;
  clearEquationView();
}

function ClearEntry(form){
  if (lastop != ""){
  	clearEquationView();
  }else{
    document.getElementById('blah').innerHTML = findLastInstanceAndSwitch(lastDigitEntered,"");
  }
  form.display.value = "0";
  lastop = "";
  clearflag = true;
}

function Memory(form, func){

  if(func=="+"){
     z += parseFloat(form.display.value);
	 dirty = true;
  }
  if(func=="-"){
     z -= parseFloat(form.display.value);
	 dirty = true;
  }
  if(func=="S"){
     z = parseFloat(form.display.value);
	 dirty = true;
  }
  if(func=="C"){
     z = 0;
  }
  if(func=="R"){
//  	 alert ( op +"  "+lastop);
	 if ( op == "" ){
     	replaceEquationView(z);		
	 }else{
	 	addEquationView(z);		
	 }
	 lastDigitEntered = z;
     form.display.value = z;
  }
  lastop = "";
  clearflag = true;
}

function Op(form, func){

  if(op!=""){
     Eq(form);
  }
  addEquationView(func);

  x = parseFloat(form.display.value);

  if(func=="+"){
     op = "+";
  }
  if(func=="-"){
     op = "-";
  }
  if(func=="*"){
     op = "*";
  }
  if(func=="/"){
     op = "/";
  }
  if(func=="^"){
     op = "^";
  }
  lastop = "";
  clearflag = true;
  dirty = false;

}

function ReverseSign(form){
  
  var strFrom = form.display.value;
  form.display.value = -parseFloat(form.display.value);
  var strTo = form.display.value;
  document.getElementById('blah').innerHTML = findLastInstanceAndSwitch(strFrom,strTo);
  lastop = "";
  clearflag = true;
  dirty= true;
}

function Eq(form){
  addEquationView(" = ");	
 
  if(lastop==""){
     y = parseFloat(form.display.value);
  }else{
     x = parseFloat(form.display.value);
     op = lastop;
  }

  if(op=="+"){
     form.display.value = x+y ;
  }
  if(op=="-"){
     form.display.value = x-y;
  }
  if(op=="*"){
     form.display.value = x*y;
  }
  if(op=="/"){
     form.display.value = x/y;
  }
  if(op=="^"){
     form.display.value = Math.pow(x, y);
  }
  addEquationView(form.display.value);
  
  lastop = op;
  op = "";
  clearflag = true;

}


function addEquationView(str){
	var whatsInside = document.getElementById('blah').innerHTML;
	whatsInside = whatsInside + str;
	document.getElementById('blah').innerHTML = whatsInside; 
}

function findLastInstanceAndSwitch(strFrom,strTo){
	var whatsInside = new String(document.getElementById('blah').innerHTML);
	var indy = whatsInside.lastIndexOf(strFrom);
	return whatsInside.substring(0,indy) + strTo;
}

function replaceEquationView(str){
	document.getElementById('blah').innerHTML = str;
}

function clearEquationView(){
	document.getElementById('blah').innerHTML = ""; 
}

function popupStart(vheight,vwidth,varpage) {
      var page = varpage;
      windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
      var popup=window.open(varpage, "", windowprops);
      if (popup != null) {
        if (popup.opener == null) {
          popup.opener = self;
        }
      }
}
</SCRIPT>


<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message
			key="oscarEncounter.calculators.SimpleCalculator.module" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td><bean:message
					key="oscarEncounter.calculators.SimpleCalculator.msgTitle" /></td>
				<td>&nbsp;</td>
				<td style="text-align: right"><oscar:help keywords="calculator" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'../About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'../License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn">&nbsp;</td>
		<td class="MainTableRightColumn">
		<table>
			<tr>
				<td style="text-align: center;">


				<table>
					<tr>
						<td id="blah"></td>
					</tr>
				</table>
				<bean:message
					key="oscarEncounter.calculators.SimpleCalculator.MsgUsage" />

				<FORM NAME="rcform"><INPUT TYPE="text" VALUE="0"
					NAME="display">
				<TABLE>
					<TR>
						<TD><INPUT TYPE=button VALUE="M+"
							onClick="Memory(this.form, '+')" class="calFunctionButton"
							title="Add to Memory"></td>
						<TD><INPUT TYPE=button VALUE="M-"
							onClick="Memory(this.form, '-')" class="calFunctionButton"
							title="Subtract from Memory"></td>
						<TD><INPUT TYPE=button VALUE="MS"
							onClick="Memory(this.form, 'S')" class="calFunctionButton"
							title="Save this Number to Memory"></td>
						<TD><INPUT TYPE=button VALUE="MC"
							onClick="Memory(this.form, 'C')" class="calFunctionButton"
							title="Clear Memory"></td>
						<TD><INPUT TYPE=button VALUE="MR"
							onClick="Memory(this.form, 'R')" class="calFunctionButton"
							title="Recall Memory"></td>
					</tr>
					<TR>
						<TD><INPUT TYPE=button VALUE="+/-"
							onClick="ReverseSign(this.form)" class="calFunctionButton"></td>
						<TD><INPUT TYPE=button VALUE="7"
							onClick="AddDigit(this.form, '7')" class="calDigitButton"></td>
						<TD><INPUT TYPE=button VALUE="8"
							onClick="AddDigit(this.form, '8')" class="calDigitButton"></td>
						<TD><INPUT TYPE=button VALUE="9"
							onClick="AddDigit(this.form, '9')" class="calDigitButton"></td>
						<TD><INPUT TYPE=button VALUE="/" onClick="Op(this.form, '/')"
							class="calFunctionButton"></td>
					</tr>
					<TR>
						<TD><INPUT TYPE=button VALUE="^" onClick="Op(this.form, '^')"
							class="calFunctionButton"></td>
						<TD><INPUT TYPE=button VALUE="4"
							onClick="AddDigit(this.form, '4')" class="calDigitButton"></td>
						<TD><INPUT TYPE=button VALUE="5"
							onClick="AddDigit(this.form, '5')" class="calDigitButton"></td>
						<TD><INPUT TYPE=button VALUE="6"
							onClick="AddDigit(this.form, '6')" class="calDigitButton"></td>
						<TD><INPUT TYPE=button VALUE="*" onClick="Op(this.form, '*')"
							class="calFunctionButton"></td>
					</tr>
					<TR>
						<TD><INPUT TYPE=button VALUE="C" onClick="Clear(this.form)"
							class="calFunctionButton"></td>
						<TD><INPUT TYPE=button VALUE="1"
							onClick="AddDigit(this.form, '1')" class="calDigitButton"></td>
						<TD><INPUT TYPE=button VALUE="2"
							onClick="AddDigit(this.form, '2')" class="calDigitButton"></td>
						<TD><INPUT TYPE=button VALUE="3"
							onClick="AddDigit(this.form, '3')" class="calDigitButton"></td>
						<TD><INPUT TYPE=button VALUE="-" onClick="Op(this.form, '-')"
							class="calFunctionButton"></td>
					</tr>
					<TR>
						<TD><INPUT TYPE=button VALUE="CE"
							onClick="ClearEntry(this.form)" class="calFunctionButton"></td>
						<TD><INPUT TYPE=button VALUE="0"
							onClick="AddDigit(this.form, '0')" class="calDigitButton"></td>
						<TD><INPUT TYPE=button VALUE="."
							onClick="AddDigit(this.form, '.')" class="calDigitButton"></td>
						<TD><INPUT TYPE=button VALUE="=" onClick="Eq(this.form)"
							class="calFunctionButton"></td>
						<TD><INPUT TYPE=button VALUE="+" onClick="Op(this.form, '+')"
							class="calFunctionButton"></td>
					</tr>

				</TABLE>
				</FORM>



				</td>
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
</html:html>
