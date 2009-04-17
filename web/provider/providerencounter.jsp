<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%
  String apptProvider_no, user_no, username;
  user_no = (String) session.getAttribute("user");
  apptProvider_no = request.getParameter("curProvider_no");
  username =  request.getParameter("username").toUpperCase();
%>
<%@ page import="java.util.*, java.sql.*, oscar.*,java.net.*"
	errorPage="errorpage.jsp"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<%
   String demoname=null,dob=null,gender=null,hin=null,roster=null;
   int dob_year = 0, dob_month = 0, dob_date = 0;
   List<Map> resultList = oscarSuperManager.find("providerDao", request.getParameter("dboperation"), new Object[] {request.getParameter("demographic_no")});
   for (Map demo : resultList) { //dboperation=search_demograph
     demoname=demo.get("last_name")+", "+demo.get("first_name");
     dob_year = Integer.parseInt(String.valueOf(demo.get("year_of_birth")));
     dob_month = Integer.parseInt(String.valueOf(demo.get("month_of_birth")));
     dob_date = Integer.parseInt(String.valueOf(demo.get("date_of_birth")));
     dob=dob_year+"-"+dob_month+"-"+dob_date;
     gender=String.valueOf(demo.get("sex"));
     hin=String.valueOf(demo.get("hin"));
     roster=String.valueOf(demo.get("roster_status"));
   }
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>ENCOUNTER</title>
<link rel="stylesheet" href="../web.css">
<meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
<meta http-equiv="Pragma" content="no-cache">
<script language="JavaScript">
<!--



function popupSearchPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprop = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=50,left=0";
  var popup=window.open(page, "searchpage", windowprop);
}
function selectform(s) {
	var toformtemp = s.options[s.selectedIndex].value;
	popupPage(600,1000, toformtemp);
}
function selecttemplate(s) {
  var b = document.encounter.encounterattachment.value ;
  var c = document.encounter.attachmentdisplay.value ;
	var toformtemp = s.options[s.selectedIndex].value;
  var a=self.location.href.substring(0,self.location.href.lastIndexOf("template=")+9);//get the last template= , in case of addition repeatly
	self.location.href = a+toformtemp+"&encounterattachment=" +b+ "&attachmentdisplay="+c;
}
var attachGainedFocus=0;
function onAttachmentFocus() {
  document.encounter.elements["attachmentdisplay"].blur();
  if (attachGainedFocus==0) {
    window.alert("You don't need to add or change your attachments.");
    attachGainedFocus=1;
  }
}
function refresh() {
  //history.go(0);//location.reload();
}

function urlencode(str) {
	var ns = (navigator.appName=="Netscape") ? 1 : 0;
	if (ns) { return escape(str); }
	var ms = "%25#23 20+2B?3F<3C>3E{7B}7D[5B]5D|7C^5E~7E`60";
	var msi = 0;
	var i,c,rs,ts ;
	while (msi < ms.length) {
		c = ms.charAt(msi);
		rs = ms.substring(++msi, msi +2);
		msi += 2;
		i = 0;
		while (true)	{ 
			i = str.indexOf(c, i);
			if (i == -1) break;
			ts = str.substring(0, i);
			str = ts + "%" + rs + str.substring(++i, str.length);
		}
	}
	return str;
}
if (!document.all) document.captureEvents(Event.MOUSEUP);
document.onmouseup = getActiveText;
function getActiveText(e) { 
  //text = (document.all) ? document.selection.createRange().text : document.getSelection();
  //document.ksearch.key.value = text;
  if(document.all) {
    text = document.selection.createRange().text;
    if(text != "" && document.ksearch.key.value=="") {
      document.ksearch.key.value += text;
    }
    if(text != "" && document.ksearch.key.value!="") {
      document.ksearch.key.value = text;
    }
  } else {  
    text = document.getSelection();
    document.ksearch.key.value = text;
  }
  return true;
}
function checksubject() {
  if(document.encounter.xml_subject.value=="")
    document.encounter.xml_subject.value="No Reason";
}
function gotoAccs() {
  popupPage(400,700,'providerencountereditdemoacc.jsp?demographic_no=<%=request.getParameter("demographic_no")%>&demographic_name=<%=URLEncoder.encode(demoname)%>');
}
//-->
</script>
</head>
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">ENCOUNTER
		</font></th>
	</tr>
</table>
<%
	GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  int age=0;
  if(dob_year!=0) age=MyDateFormat.getAge(dob_year,dob_month,dob_date);

  boolean bNewDemoAcc=true;
  resultList = oscarSuperManager.find("providerDao", "search_demographicaccessory", new Object[] {request.getParameter("demographic_no")});
  for (Map acc : resultList) {
     String content=String.valueOf(acc.get("content"));
     bNewDemoAcc=false;
%>
<xml id="xml_list">
<encounteraccessory>
<%=content%>
</encounteraccessory>
</xml>
<%
   } 
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<!--for form use-->
	<!--form name="demoacce" method="post" action="providercontrol.jsp" target="encounterhist" onSubmit="popupPage(1,1,'providercontrol.jsp')"-->
	<form name="encounter" method="post" action="providercontrol.jsp">
	<tr>
		<td>

		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td align="left"><font color="blue"><%=Misc.toUpperLowerCase(demoname)%>
				<i><%=""+age%></i> <%=gender%> <i>RS: <%=roster==null?"NONE":roster%></i></font></td>
				<td align="center" bgcolor="#aabbcc">
				<table bgcolor="#eeeeee" border="0" cellpadding="3" cellspacing="0"
					width="98%">
					<tr>
						<td bgcolor="#ffffff" align="center">
						<table width="100%" border="0" bgcolor="#aabbcc">
							<tr>
								<td align="center"><a href=#
									onClick="popupPage(750,1000,'providerencountereditdemoacc.jsp?demographic_no=<%=request.getParameter("demographic_no")%>&demographic_name=<%=URLEncoder.encode(demoname)%>')">
								Edit </a></td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>

		<table border="0" cellpadding="2" cellspacing="0" bgcolor="#aabbcc"
			width=100%>
			<tr>
				<td>
				<table bgcolor="#eeeeee" border="0" cellpadding="2" cellspacing="0"
					width="100%">
					<tr>
						<td bgcolor="#ffffff" align="center">

						<table width="100%" border="0" cellpadding="2" cellspacing="0"
							bgcolor="#aabbcc" <%=bNewDemoAcc?"":"datasrc='#xml_list'"%>>
							<tr>
								<td width="50%" align="center">Problem List:<br>
								<textarea name="xml_Problem_List" style="width: 100%" cols="30"
									rows="6" readonly
									<%=bNewDemoAcc?"":"datafld='xml_Problem_List'"%>></textarea></td>
								<td width="50%" align="center">Medication:<br>
								<textarea name="xml_Medication" style="width: 100%" cols="30"
									rows="6" readonly
									<%=bNewDemoAcc?"":"datafld='xml_Medication'"%>></textarea></td>
							</tr>
							<tr>
								<td>
								<div align="center">Allergy/Alert:<br>
								<textarea name="xml_Alert" style="width: 100%" cols="30"
									rows="3" readonly <%=bNewDemoAcc?"":"datafld='xml_Alert'"%>></textarea>
								</div>
								</td>
								<td>
								<div align="center">Family Social History:<br>
								<textarea name="xml_Family_Social_History" style="width: 100%"
									cols="30" rows="3" readonly
									<%=bNewDemoAcc?"":"datafld='xml_Family_Social_History'"%>></textarea>
								</div>
								</td>
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
	<!--/form-->
</table>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td><font size="-1"><!--History--></font></td>
		<td width="100%">
<%
   int i=0;
   resultList = oscarSuperManager.find("providerDao", "search_encounter", new Object[] {request.getParameter("demographic_no")});
   for (Map enc : resultList) {
     i++;
     if(i>3) {
       out.println("<a href=# onClick=\"popupPage(400,600,'providercontrol.jsp?demographic_no=" +request.getParameter("demographic_no")+ "&dboperation=search_encounter&displaymode=encounterhistory');return false;\">... more</a>");
       break;
     }
%> &nbsp;<%=enc.get("encounter_date")%> <%=enc.get("encounter_time")%><font
			color="blue"> <%
     String historysubject = enc.get("subject")==null?"No Reason":String.valueOf(enc.get("subject")).equals("")?"No Reason":String.valueOf(enc.get("subject"));
     StringTokenizer st=new StringTokenizer(historysubject,":");
     //System.out.println(" history = " + historysubject);
     String strForm="", strTemplateURL="";
     while (st.hasMoreTokens()) {
       strForm = (new String(st.nextToken())).trim();
       break;
     }

     if(strForm.toLowerCase().compareTo("form")==0 && st.hasMoreTokens()) {
       strTemplateURL = "template" + (new String(st.nextToken())).trim().toLowerCase()+".jsp";
%> <a href=#
			onClick="popupPage(600,800,'providercontrol.jsp?encounter_no=<%=enc.get("encounter_no")%>&demographic_no=<%=request.getParameter("demographic_no")%>&dboperation=search_encountersingle&displaymodevariable=<%=strTemplateURL%>&displaymode=vary&bNewForm=0');return false;"><%=historysubject%>
		</a></font><br>
		<%
     } else if(strForm.compareTo("")!=0) {
%> <a href=#
			onClick="popupPage(400,600,'providercontrol.jsp?encounter_no=<%=enc.get("encounter_no")%>&demographic_no=<%=request.getParameter("demographic_no")%>&template=<%=strForm%>&dboperation=search_encountersingle&displaymode=encountersingle');return false;"><%=historysubject%>
		</a></font><br>
		<%
     }
   }     
%>
		</td>
	</tr>
</table>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<td><font color="#FFFFFF"><%=curYear+"-"+(curMonth)+"-"+curDay%>
		| <%=now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)%> <br>
		<%=Misc.toUpperLowerCase(username)%> </font></td>
		<td align="right"><a href=#
			onclick="window.open('../dms/adddocument.jsp?function=demographic&functionid=<%=request.getParameter("demographic_no")%>&creator=<%=user_no%>','', 'scrollbars=yes,resizable=yes,width=600,height=300')";>
		<font color="yellow">Add Document</font></a> <%
     if(request.getParameter("status").indexOf('B')==-1) {
   %> <!--a href=# onClick='popupPage(600,800, "billingobstetric.jsp?appointment_no=<%=request.getParameter("appointment_no")%>&demographic_name=<%=URLEncoder.encode(demoname)%>&demographic_no=<%=request.getParameter("demographic_no")%>&user_no=<%=user_no%>&apptProvider_no=<%=apptProvider_no%>&appointment_date=<%=request.getParameter("appointment_date")%>&start_time=<%=request.getParameter("start_time")%>&bNewForm=1" )'><font color="yellow">
    Billing</font></a><br--> <!--a href=# onClick='popupPage(700,720, "../billing/billingOB.jsp?hotclick=<%=URLEncoder.encode("")%>&appointment_no=<%=request.getParameter("appointment_no")%>&demographic_name=<%=URLEncoder.encode(demoname)%>&demographic_no=<%=request.getParameter("demographic_no")%>&user_no=<%=user_no%>&apptProvider_no=<%=apptProvider_no%>&appointment_date=<%=request.getParameter("appointment_date")%>&start_time=<%=request.getParameter("start_time")%>&bNewForm=1")'><font color="yellow">
    Billing</font></a><br--> <%       
     }
   %> <a href=# onClick='popupPage(500,700, "providercontrol.jsp?appointment_no=<%=request.getParameter("appointment_no")%>&demographic_name=<%=URLEncoder.encode(demoname)%>&demographic_no=<%=request.getParameter("demographic_no")%>&curProvider_no=<%=user_no%>&username=<%= username %>&apptProvider_no=<%=apptProvider_no%>&displaymode=prescribe&dboperation=search_demograph&template=" );return false;'><font color="yellow">
    Prescribe</font></a></td>
		<td align="right"><select name="formmenu" size="1"
			OnChange="selectform(this)">
			<option selected value="">Form</option>
<%
   resultList = oscarSuperManager.find("providerDao", "search_encounterform", new Object[] {"%"});
   for (Map form : resultList) {
	%>
			<option
				value="<%=form.get("form_value")+request.getParameter("demographic_no")%>"><%=form.get("form_name")%></option>
			<%
   }
%>
		</select> <select name="templatemenu" OnChange="selecttemplate(this)">
			<option selected value=""><%=request.getParameter("template").equals("")?"Template":request.getParameter("template")%></option>
<%
   resultList = oscarSuperManager.find("providerDao", "search_template", new Object[] {"%"});
   for (Map template : resultList) {
	   String url = String.valueOf(template.get("encountertemplate_name"));//encountertemplate_url");
%>
			<option
				value="<%=url.length()>20?url.substring(0,20):url%>"><%=((String)template.get("encountertemplate_name")).toLowerCase()%></option>
<%
   }
%>
		</select></td>
	</tr>
</table>

<table width="100%" border="1" bgcolor="#87CEEB">
	<!--form name="encounter" method="post" action="providercontrol.jsp" -->
	<tr>
		<td align="center">
		<%
  if(request.getParameter("template")!=null && !(request.getParameter("template").equals("")) ) {
    resultList = oscarSuperManager.find("providerDao", "search_template", new Object[] {request.getParameter("template")});
    for (Map template : resultList) {
%>
		<table>
			<tr>
				<td width='10%'>Reason:</td>
				<td><input type='text' name='xml_subject' style='width: 100%'
					value='<%=URLDecoder.decode(request.getParameter("reason"))%>'
					size='60' maxlength='60'></td>
			</tr>
			<tr>
				<td>Content:</td>
				<td><textarea name='xml_content' style='width: 100%' cols='60'
					rows='10'><%=template.get("encountertemplate_value")%>
 </textarea></td>
			</tr>
			<input type='hidden' name='xml_subjectprefix' value='SOAP'>
			</talbe>
<%
    }
  } else if(request.getParameter("editpreviousenc")!=null && request.getParameter("editpreviousenc").equals("1") ) {
     String [] param = new String [2];
     param[0] = request.getParameter("demographic_no");
     param[1] = user_no ;
     resultList = oscarSuperManager.find("providerDao", "search_previousenc", param);
%>
			<table width="100%">
				<tr>
					<td width="10%">Reason:</td>
					<td><input type='text' name='xml_subject' style="width: 100%"
						value='<%=URLDecoder.decode(request.getParameter("reason"))%>'
						size='60' maxlength='60'></td>
				</tr>
				<tr>
					<td>Content:</td>
					<td><textarea name='xml_content' style="width: 100%" cols='50'
						rows='10'>
<%  //String at=null, enc=null;
    if (resultList.size() > 0) {
      String at = SxmlMisc.getXmlContent((String)resultList.get(0).get("content"),"xml_content") ;
      out.println(at==null?"":at + "</textarea>");
      out.println("<input type=\"hidden\" name=\"del_encounter_no\" value=\"" + resultList.get(0).get("encounter_no") + "\">") ;
    } else {
      out.println("</textarea>");
    }
%>
<%--=rsdemo.getString("content")--%>
    </td>
				</tr>
			</table>
			<input type='hidden' name='xml_subjectprefix' value=".">
			<%  
  } else {
%>
			<table width="100%">
				<tr>
					<td width="10%">Reason:</td>
					<td><input type='text' name='xml_subject' style="width: 100%"
						value='<%=URLDecoder.decode(request.getParameter("reason"))%>'
						size='60' maxlength='60'></td>
				</tr>
				<tr>
					<td>Content:<br>
					<a
						href="providercontrol.jsp?editpreviousenc=1&appointment_no=<%=request.getParameter("appointment_no")%>&demographic_no=<%=request.getParameter("demographic_no")%>&curProvider_no=<%=request.getParameter("curProvider_no")%>&reason=<%=request.getParameter("reason")%>&username=<%=request.getParameter("username") %>&appointment_date=<%=request.getParameter("appointment_date")%>&start_time=<%=request.getParameter("start_time")%>&status=<%=request.getParameter("status")%>&displaymode=encounter&dboperation=search_demograph&template="
						title="Edit Last Encounter">(Edit)</a></td>
					<td><textarea name='xml_content' style="width: 100%" cols='50'
						rows='10'></textarea></td>
				</tr>
			</table>
			<input type='hidden' name='xml_subjectprefix' value=".">
			<%  
  }

  String slpusername="", slppassword="";
  resultList = oscarSuperManager.find("providerDao", "search_provider_slp", new Object[] {user_no});
  for (Map slp : resultList) {
	String comments = (String)slp.get("comments");
    if(comments!=null) {
      slpusername = SxmlMisc.getXmlContent(comments,"<xml_p_slpusername>","</xml_p_slpusername>");
      slppassword = SxmlMisc.getXmlContent(comments,"<xml_p_slppassword>","</xml_p_slppassword>");
    }
  }
%>

			</td>
			</tr>
			<tr>
				<td nowrap align="center" colspan="2">
				<p><input type="hidden" name="demographic_no"
					value="<%=request.getParameter("demographic_no")%>"> <input
					type="hidden" name="encounter_date"
					value='<%=curYear+"-"+(curMonth)+"-"+curDay%>'> <input
					type="hidden" name="encounter_time"
					value='<%=now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND)%>'>
				<input type="hidden" name="user_no" value='<%=user_no%>'> <input
					type="hidden" name="xml_username" value='<%=username%>'> <input
					type="hidden" name="template"
					value='<%=request.getParameter("template")%>'> <input
					type="hidden" name="dboperation" value="add_encounter"> <input
					type="hidden" name="displaymode" value="saveencounter"> <input
					type="submit" name="submit" value="Save & Exit"
					onClick="checksubject();"><input type="submit"
					name="submit" value="Save & Print Preview"
					onClick="checksubject();"> <!--input type="button" name="submit" value="Signature" onClick="self.location.href='providerencountersignature.jsp?demographic_no=<%--=request.getParameter("demographic_no")--%>';"-->
				<input type="button" name="Button" value="Cancel"
					onClick="window.close();"> <!--a href=# onClick ="popupPage(400,600,'xmledit.jsp?')"><font color='navy'>Attachment</font></a-->
				</td>
			</tr>
			<tr>
				<td colspan="2"><font size="-2">Attachments:</font> <input
					type='text' size='70' name='attachmentdisplay'
					value="<%=request.getParameter("attachmentdisplay")==null?"":request.getParameter("attachmentdisplay")%>"
					readonly onFocus="onAttachmentFocus()"> <br>
				<input type='hidden' size='70' name='encounterattachment'
					value="<%=URLEncoder.encode(request.getParameter("encounterattachment")==null?"":request.getParameter("encounterattachment"))%>">
				</td>
			</tr>
			</form>
		</table>

		<table width="100%" border="1" bgcolor="ivory">
			<form name="ksearch"
				onsubmit="popupSearchPage(600,800,this.channel.options[this.channel.selectedIndex].value+urlencode(this.key.value) ); return false;">
			<tr>
				<td>Knowledge Search: <input type="text" name="key" value="">
				<select name="channel" size="1">
					<option
						value="http://emr.skolar.com/gateway?tfUsername=dchan@mcmaster.ca&pwPassword=david&url=/emr/Search.jsp&query=">Skolar</option>
					<option
						value="https://209.61.188.77:8443/oscar_slp/Greeting.jsp?firstkeyword=">Self-Learning</option>
					<!--option value="http://130.113.153.155:8080/slt/SimpleSearchOut.jsp?uid=<%--=slpusername%>&password=<%=slppassword--%>&firstkeyword="-->
					<option value="http://www.google.com/search?q=">Google</option>
				</select> <input type="submit" value=" Go " name="submit"></td>
			</tr>
			</form>
			<tr>
				<td><a href=#
					onClick='popupPage(600,800, "http://www.ncbi.nlm.nih.gov/entrez/query/static/clinical.html" );return false;'>
				Pubmed</a></td>
			</tr>
		</table>
</body>
</html>