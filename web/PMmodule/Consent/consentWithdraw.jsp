<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->

<%@ include file="/taglibs.jsp"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>

<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<title>Consent Form</title>
</head>

<script language="javaScript">

  //The following functions needs to implemented to enable the webpages with integrisign


  //The getHashdata function will be called by applet to get the hash data.
  
  //e.g form fields that nedds to be included in hash should be concatinated into a single string
  //Hidden fields meant to hold integrisign data should be omitted from hash as they doesn't carry any
  //value prior to the act of signing.

  function getHashData() {
  	//In this example uid,uname field values are sent to Hash
  	//Similar way any number of form field values can be sent to hash
  	var hashstr;
  	hashstr=window.document.integrisignfrm.uid.value+window.document.integrisignfrm.uname.value+window.document.integrisignfrm.crdno.value;
  	return hashstr;
  }

  // This signnow function responsible for the capture and paint the signature on web forms.

  function signNow() {

	var a,b,d;
	a = new String(window.document.integrisignfrm.uid.value); //user id
	b = new String(window.document.integrisignfrm.uname.value); // name
	//alert(window.document.integrisignfrm.a1.value);
	if (window.document.integrisignfrm.a1.value!="1"||window.document.integrisignfrm.a2.value!="1"||window.document.integrisignfrm.a3.value
!="1")
	{
	alert('You can not sign this form unless above three answers all are correct!');
	return;
	}
	//alert("here!!!")
	if(a == "" || b == "")
	{
		if(a == "" && b == "")
			alert("User ID & User Name must  be entered");
		else
		{
			 if(a == "")
			 	alert("User Id must be entered");
			 else
				alert("User Name must be entered");

		}
	}
	else
	{
		// UserId specfic to site and the users name must be passed to sign the forms

		//document.Sign.signNow(a,b);
		for(i=0;i<window.document.integrisignfrm.uid.value.length;i++)
		{
			if(window.document.integrisignfrm.uid.value.charAt(i)==" ")
			{
				alert("Id should not contain whitespace");
				return;
			}
			if(window.document.integrisignfrm.uid.value.charAt(i)=="'") {
				alert("Id should not contain single quote");
				return;

			}
		}

		if(window.document.integrisignfrm.uid.value.length>50) {
			alert("Id should not be more than 50 characters");
			return;
		}


		document.Sign.signNow(a,b,"demo","demo","demo","footer","comments",true,true,true);
		//document.Sign.signNow(a,b,true,true,true);

        d=document.Sign.getString();
		//alert (d);
		
	}
}

// trybytes function gets the raw signature and bmp data as strings this should be
// called before submit after signnow
// The returned strings from calls to applets are assigned to form hidden fields.

function tryBytes() {
		var y;
		var z;
		isSigned = document.Sign.isSigned();
		// Information can only be extracted if isSigned() returns true
		if(isSigned == true)
		{
			// Get the signature info and assign to one of form field
			document.integrisignfrm.signstr.value=document.Sign.getString();
			x = document.integrisignfrm.signstr.value;
			// Get the bmp info and assign to one of form field
			document.integrisignfrm.bmpstr.value=document.Sign.getBMPString(x,125,60);
			y =document.integrisignfrm.bmpstr.value;
			// Get the Gif data and assign it to one of the fields
			z=document.integrisignfrm.gifstr.value=document.Sign.getGifString(x,125,60);
			jpeg=document.integrisignfrm.jpegstr.value=document.Sign.getJpegString(x,125,60,100);

			if(x != "") {
				if(y !="") {
					if(z!="") {
						if(jpeg!="") {
								document.integrisignfrm.submit();
						} else
							alert("unable to get the JPEG Info");
					} else
						alert("unable to get the Gif Info");

				} else
					alert("unable to get the BMP info");

			 } else
				alert("Unable to get The Sign String");
		}
		else
			alert("Before Submit form must be signed");
 }
 
 
 function saveForm()
 {
 
 isSigned = true;
 signstr=document.integrisignfrm2.signstr.value;
 
 if (window.document.integrisignfrm.a1.value!="1"||window.document.integrisignfrm.a2.value!="1"||window.document.integrisignfrm.a3.value
!="1")
	{
	alert('You can not print and sign this form unless above three answers all are correct!');
	return;
	}
 
 if (signstr=="")
	{
	alert('please input the consent paper location and try again!');
	return;
	}
 
    if(isSigned == true)
		{
		
		 document.consentForm.clientSignture1.value=signstr;
		 //alert(document.Sign.getJpegString(signstr,125,60,100));
		jpegstr="jpeg string should be load here";
		document.consentForm.clientSignture2.value=jpegstr;
		 document.consentForm.submit();
		 opener.document.clientManagerForm.submit();
		}
     else
       {
       alert('The consent form is not signed!');
       }
 }
 
 function printForm()
 {
 if (window.document.integrisignfrm.a1.value!="1"||window.document.integrisignfrm.a2.value!="1"||window.document.integrisignfrm.a3.value
!="1")
	{
	alert('You can not print and sign this form unless above three answers all are correct!');
	return;
	}
 window.print();
 
 }
 
 
 
 </script>


<body topmargin="20" leftmargin="10">

<%=request.getParameter("clientName")%>

<table border="2" width="700" cellspacing="0" cellpadding="0">
	<tr>
		<td width="100%"><b><font color="red" face="Century Gothic"
			size="4">Consent WithDraw Form</font></b>


		<p><b><font color="blue">Part 1: Consent information (to be read to client, hard copy
		will be offered to client; identical information to “Consent A”)</font></b></p>
		</td>
		</tr>
	<tr>
		<td width="100%"><i>
		<p>“CAISI is a community project.</p>
		<p>The purpose of this project is to help clients like yourself have
		control oveinstr your personal information at different agencies. This
		information will be stored on computers. With your permission and
		control, this information will be sent to other agencies when they
		care for you so they can provide you better care. These other agencies
		are:</p>
		<li>Hospitals.inst
		<li>Ambulance services.
		<li>Public health.
		<li>Outreach teams.
		<li>Shelters.
		<li>Mental health teams.
		<li>Related agencies that are CAISI partners.

		<p>In these agencies, your information will only be seen by those with
		the appropriate training. For example, only health care teams will be
		allowed to see detailed medical information.</p>
		<p>The purpose of this program is to send information to agencies when
		they care for you so they can provide better care for you. The program
		will also be used to improve community programs, to do research and to
		compile statistics.</p>
		<p>Your care at <u><%=session.getAttribute("userfirstname")%>,<%=session.getAttribute("userlastname")%> </u>will not change by allowing agencies to
		send information and work together through CAISI.</p>
		<p>You may withdraw permission to send information to other agencies
		at any time. To withdraw, contact <u><%=session.getAttribute("userfirstname")%>,<%=session.getAttribute("userlastname")%></u> or any other CAISI
		partner.</p>
		<p>Any questions can be directed to <u><%=session.getAttribute("userfirstname")%>,<%=session.getAttribute("userlastname")%></u> staff.”</p>
		</i> 
		
		
		<html:form action="/PMmodule/Consent.do">
			<input type="hidden" name="id"
				value="<c:out value="${requestScope.id}"/>">
			<input type="hidden" name="method" value="saveConsent" />



			<input type="hidden" name="clientSignture1">
			<input type="hidden" name="clientSignture2">
			<input type="hidden" name="demographicNo"
				value=<%=request.getParameter("demographicNo")%>>
			<input type="hidden" name="status" value="withdraw">

			</td>
	</tr>
	<tr>
		<td width="100%"><p><b><font color="blue">Part 2: Testing of comprehension (questions to be
		asked and scored by <u><%=session.getAttribute("userfirstname")%>,<%=session.getAttribute("userlastname")%></u> staff, correct answers to all 3
		questions required to provide a consent/refusal for part 3, if less
		than all 3 questions correct do not proceed to part 3) </font></b>
		</p>
		</td>
	</tr>
	<tr>
		<td width="100%">
		<p>1.<i>What is the purpose of the CAISI project? </i><br>
		<html:radio property="consent.answer1" value="1" onclick="document.integrisignfrm.a1.value=1;"/>Correct<html:radio
			property="consent.answer1" value="0" onclick="document.integrisignfrm.a1.value=0;"/>Incorrect</p>
		<p>[Correct includes any one of: to send information to agencies when
		they care for you; to provide better care for clients; to give clients
		control over their information or any combination of these]</p>
		<p>2.<i>When are you able to withdraw from CAISI?</i><html:radio
			property="consent.answer2" value="1" onclick="document.integrisignfrm.a2.value=1;"/>Correct <html:radio
			property="consent.answer2" value="0" onclick="document.integrisignfrm.a2.value=0;"/>Incorrect</p>
		<p>[Correct includes: any time]</p>
		<p>3.<i>Will your care at <u><%=session.getAttribute("userfirstname")%>,<%=session.getAttribute("userlastname")%></u> be affected by your participation
		in CAISI? </i> <html:radio property="consent.answer3" value="1" onclick="document.integrisignfrm.a3.value=1;"/>
		Correct <html:radio property="consent.answer3" value="0" onclick="document.integrisignfrm.a3.value=0;"/>Incorrect</p>
		<p>[Correct = no]</p>
		</td>
	</tr>

	<tr>
		<td width="100%">
		<p><b><font color="blue">Part 3a: Read the following prompt and record if the client chooses not to consent to CAISI. Nothing needs to 
		be recorded if the client agrees to consent. Do not read prompt if less than three correct answers were given for 
		the questions in Part 2 (above). </font></b></p>
		</td>
	</tr>
	<tr>
		<td>
		<p><br>
		&nbsp&nbsp&nbsp I, <b><U><%=request.getParameter("clientName")%></U></b>, <b>DO NOT </b>
		permit <b><U><%=session.getAttribute("userfirstname")%>,<%=session.getAttribute("userlastname")%></U></b>
		and any other CAISI partner agencies to record, send and use my
		personal information for the purposes above.</p>
		</td>
	</tr>

	</html:form>

	<!-- sign4IntakeForm should be complied as sign4consentForm, only naming issue, doesn't matter.  -->
		<form name="integrisignfrm" method="post"
			action="/OscarWAR/mod/caisiComp/sign4IntakeForm">
			<input	type="hidden" name="uid"
			value=<%=request.getParameter("demographicNo")%>> 
			<input
			type="hidden" name="uname"
			value=<%=request.getParameter("clientName")%>> 
			<input type="hidden"
			name="crdno" value=<%=request.getParameter("providerNo")%>>
	
	<input	type="hidden" name="a1"  > 
	<input	type="hidden" name="a3"  > 
	<input	type="hidden" name="a2"  > 

<tr>
<td>

<!--  
	<!THE FOLLOWING IS THE ACTIVEX OBJECT WHICH IS USED IN DETECTING THE MSJVM AND SUN PLUGIN INSTALLATION STATUS --?>
		<OBJECT id=Status1 classid=clsid:64D90B4C-C486-455D-9FDB-37CED5C9C19C
			width="0" height="0" codebase="jvmdetect.cab#Version=1,0,0,2">
			<param name="useslibrary" value="JVMDetect">
			<param name="useslibrarycodebase" value="jvmdetect.cab">
			<param name="useslibraryversion" value="6,21,0,0">
		</OBJECT> <?!SCRIPT TO CHECK THE PRESENCE OF MSJVM AND SUN PLUGIN --?> <SCRIPT
			LANGUAGE=JavaScript>
	var msjvmstatus;
	var sunpluginstatus;
	var jvmobj;
	//CHECK THE MSJVM INSTALLATION,ENABLEMENT STATUS
	msjvmstatus=window.document.Status1.GetMSJVMStatus();
	/*
		GetMSJVMStatus returns an integer value
		Description corresponding to the returned values are as follows
		0--  Microsoft JVM Succesfully Installed and Enabled for Internet Explorer.
		1--  Microsoft JVM not Installed.
		2--  Microsoft JVM installed but not enabled for Internet Explorer.
		3-- An internal error occured while reading the windows registry.
	*/
	//CHECK THE SUN PLUGIN  INSTALLATION,ENABLEMENT STATUS
	sunpluginstatus=window.document.Status1.GetSUNPluginStatus();

	/*
		GetSUNPluginStatus returns an integer value
		Description corresponding to the returned values are as follows
		0--  SUN Plug-in Succesfully Installed and Enabled for Internet Explorer.
		1--  SUN Plug-in not Installed.
		2--  SUN Plug-in installed but not enabled for Internet Explorer.
		3-- An internal error occured while reading the windows registry.
	*/
	//CHECK FOR THE RETURNED VALUES OF THE JVM STATUS
	if(msjvmstatus==0 && sunpluginstatus==0) {
		// If both the JVMs are installed then the default is set to Sun PlugIn
		writeSUNJVMTags();
		/*var msg;
		msg="Both MSJVM and SUNPlugin are installed and enabled on this machine. Disable any one of them.\n\n";
		msg=msg+"To disable the MS JVM Go To 'Tools--?>Internet Options--?>Advanced' and scroll down to 'Microsoft VM' section and uncheck the  'JIT compiler for virtual machine enabled' option\n\n";
		msg=msg+"To disable the Sun Plugin Go To  'Start--?>Settings--?>Control Panel--?>Java Plug-in' in the popped up dialog click on 'Browser' tab and uncheck the 'Microsoft Internet Explorer Option'";
		msg=msg+" or Alternately start IE and Go To 'Tools--?>Internet Options--?>Advanced' and scroll down to Java (Sun) section and uncheck the 'use java' option.";
		alert(msg);
		*/
	} else if(msjvmstatus==0) {
		writeMSJVMTags();
	} else if(msjvmstatus==2) {
		if(sunpluginstatus ==0) {
			writeSUNJVMTags();
		} else if(sunpluginstatus ==2) {
			var msg;
			msg="MS JVM and Sun Plugin both are  installed on this machine but not enabled.\n\n";
			msg=msg+"To enable the MS JVM Go To 'Tools--?>Internet Options--?>Advanced' and scroll down to 'Microsoft VM' section and check the  'JIT compiler for virtual machine enabled' option\n\n";
			msg=msg+"To enable the Sun Plugin Go To  'Start--?>Settings--?>Control Panel--?>Java Plug-in' in the popped up dialog click on 'Browser' tab and check the 'Microsoft Internet Explorer Option'";
			msg=msg+" or Alternately start IE and Go To 'Tools--?>Internet Options--?>Advanced' and scroll down to Java (Sun) section and check the 'use java' option.";
			alert(msg);
		} else {
			var msg;
			msg="MS JVM is  installed on this machine but not enabled \n\n";
			msg=msg+"To enable the MS JVM Go To 'Tools--?>Internet Options--?>Advanced' and scroll down to 'Microsoft VM' section and check the  'JIT compiler for virtual machine enabled' option";
			alert(msg);
		}
	} else if (msjvmstatus==1) {
		if(sunpluginstatus ==0) {
			writeSUNJVMTags();
		} else if (sunpluginstatus ==2) {
			var msg;
			msg="Sun PlugIn is installed on this machine but not enabled\n\n.";
			msg=msg+"To enable the Sun Plugin Go To  'Start--?>Settings--?>Control Panel--?>Java Plug-in' in the popped up dialog click on 'Browser' tab and check the 'Microsoft Internet Explorer Option";
			msg=msg+" or Alternately start IE and Go To 'Tools--?>Internet Options--?>Advanced' and scroll down to Java (Sun) section and check the 'use java' option.";
			alert(msg);
		} else if (sunpluginstatus ==1) {
			alert("Neither the MS JVM nor the SUN Plugin are installed on this machine. Please install any one of them");
		} else if(sunpluginstatus ==3) {
			alert("Internal error occured while reading the Windows registry for the Sun Plugin check");
		}
	} else if (msjvmstatus==3) {
		if(sunpluginstatus ==0) {
			writeSUNJVMTags();
		} else if (sunpluginstatus ==2) {
			var msg;
			msg="Internal error occured while reading the Windows registry for the MSJVM installation\n\n";
			msg=msg+"Sun PlugIn is installed on this machine but not enabled.\n\n";
			msg=msg+"To enable the Sun Plugin Go To  'Start--?>Settings--?>Control Panel--?>Java Plug-in' in the popped up dialog click on 'Browser' tab and check the 'Microsoft Internet Explorer Option";
			msg=msg+" or Alternately start IE and Go To 'Tools--?>Internet Options--?>Advanced' and scroll down to Java (Sun) section and check the 'use java' option.";
			alert(msg);
		} else if (sunpluginstatus ==1) {
			var msg;
			msg="Internal error occured while reading the Windows registry for the MSJVM installation\n\n";
			msg=msg+"SUN Plugin is not installed on this machine. Please install";
			alert(msg);
		} else if(sunpluginstatus ==3) {
			var msg;
			msg="Internal error occured while reading the Windows registry for the MSJVM installation\n";
			msg=msg+"Internal error occured while reading the Windows registry for the SUN Plugin installation.";
			alert(msg);
		}
	}
	//FUNCTION TO EMBED THE APPLET TAG WHICH IS COMPATIBLE WITH MICROSOFT JVM
	function writeMSJVMTags() {
		document.writeln('<OBJECT classid=clsid:E634B267-B8E7-406C-A308-988636B7D7E1 NAME=websignsup width=0 height=0 codebase=websignsup.cab#Version=6,21,0,0>');
		document.writeln('<param name=useslibrary value=websignsup>');
		document.writeln('<param name="useslibrarycodebase" value=websignsup.cab>');
		document.writeln('<param name="useslibraryversion" value=6,21,0,0>');

		document.writeln('</OBJECT>');
		document.writeln('<APPLET CODE=integrisign.webclient.WebSign.class NAME=Sign WIDTH=140 HEIGHT=60 ID=apptag MAYSCRIPT>');
		document.writeln('<PARAM NAME=useslibrary VALUE=IntegrisignSignatureApplet>');
		document.writeln('<PARAM NAME=useslibrarycodebase VALUE=websignmsjvm.cab>');
		document.writeln('<PARAM NAME=useslibraryversion VALUE=6,21,0,0>');
		document.writeln('<PARAM NAME=borderstyle VALUE=1>');
		document.writeln('</APPLET>');
	}
	//FUNCTION TO EMBED THE OBJECT TAG S WHICH ARE COMPATIBLE WITH SUN PLUGIN
	function  writeSUNJVMTags(){
			document.writeln('<OBJECT classid=clsid:E634B267-B8E7-406C-A308-988636B7D7E1 NAME=websignsup width=0 height=0 codebase=websignsup.cab#Version=6,21,0,0>');
			document.writeln('<param name=useslibrary value=websignsup>');
			document.writeln('<param name="useslibrarycodebase" value=websignsup.cab>');
			document.writeln('<param name="useslibraryversion" value=6,21,0,0>');
			document.writeln('</OBJECT>');
			document.writeln('<OBJECT classid=clsid:8AD9C840-044E-11D1-B3E9-00805F499D93 NAME=Sign width=125 height=65>');
			document.writeln('<PARAM NAME=code VALUE=integrisign.webclient.WebSign>');
			document.writeln('<PARAM NAME=archive VALUE=websignsunjvm.jar>');
			document.writeln('<PARAM NAME=scriptable VALUE=true>');
			document.writeln('<PARAM NAME=cache_option VALUE=Plugin>');
			document.writeln('<PARAM NAME=cache_archive VALUE=websignsunjvm.jar>');
			document.writeln('<PARAM NAME=cache_version VALUE=6.21.0.0>');
			document.writeln('<PARAM NAME=MAYSCRIPT VALUE=true>');
			document.writeln('<PARAM NAME=borderstyle VALUE=1>');
			document.writeln('Java Plugin not enabled');
			document.writeln('</OBJECT>');
	}
</script>

	-->
	
	<!--  signstr hidden filed is for holding the signature string -->
	<input type="hidden" name="signstr">
	<!bmpstr hidden filed is for holding the bmpdata as string -->
	<input type="hidden" name="bmpstr">
	<!gifstr hidden filed is for holding the gif info as string -->
	<input type="hidden" name="gifstr">
	<!jpegstr hidden filed is for holding the JPEG info as string -->
	<input type="hidden" name="jpegstr">
	<!pngstr hidden filed is for holding the JPEG info as string -->
	<input type="hidden" name="pngstr">
	</form>

	<form name="integrisignfrm2" method="POST" action="">
	<div align="left">
	<! button which invokes the sign now call -->
	<!--  	<input type="button" value="Sign This Form" name="B1" onClick="signNow()">-->
	<!--  <INPUT type=radio name="clientAccept" CHECKED>The client has not accepted the terms.<br>
	<INPUT type=radio name="clientAccept" >The client has accepted the terms.-->
	
	<input type="button" value="Print to Sign" onClick="printForm()">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<br>Signature___________________________<br>
		     Date_________________________<br>
		
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<br>Signature___________________________<br>
		     Date_________________________</td>
		
		
	</tr>
	<tr><td>
	This form has been printed and  signed manually. It is kept in the location: <input type="text" name="signstr" value=""size=40>
	</td></tr>
	
	</div>
	</form>
	<tr>
	<td>
	<br>
	<input type="button" value="Save the form and Exit"
		onclick="saveForm()">
		<input type="button" value="Cancel"
		onclick="window.close()">
	</td>
	</tr>

	<tr>
		<td>
		<p align="center"><b>CAISI©&nbsp;&nbsp; <a href="http://www.caisi.ca">www.caisi.ca</a></b>
		</td>
	</tr>
</table>

</body>

</html>
