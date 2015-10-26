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
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_form" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%
  String user_no = (String) session.getAttribute("user");
  String form_name="ar2_99_08";
  String username = (String) session.getAttribute("userlastname")+","+ (String) session.getAttribute("userfirstname");
%>
<%@ page import="java.util.*, java.sql.*, java.net.*, oscar.*, oscar.util.UtilDateUtilities, oscar.form.graphic.*" errorPage="errorpage.jsp"%>

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.DemographicAccessoryDao" %>
<%@page import="org.oscarehr.common.model.DemographicAccessory" %>
<%@page import="org.oscarehr.common.dao.FormDao" %>
<%@page import="org.oscarehr.common.model.Form" %>
<%@page import="org.oscarehr.common.dao.DemographicDao" %>
<%@page import="org.oscarehr.common.model.Demographic" %>
<%
	DemographicAccessoryDao demographicAccessoryDao = (DemographicAccessoryDao)SpringUtils.getBean("demographicAccessoryDao");
	FormDao formDao = SpringUtils.getBean(FormDao.class);
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
%>

<jsp:useBean id="checklist" class="oscar.OBChecklist_99_12" scope="page" />
<jsp:useBean id="risks" class="oscar.OBRisks_99_12" scope="page" />
<% java.util.Properties oscarVariables = OscarProperties.getInstance(); %>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>ANTENATAL RECORD</title>
<link rel="stylesheet" href="../provider/antenatalrecord.css">
<script language="JavaScript">
<!--
var saveTemp=0;


function onAR1FieldsFocus(obj) {
  obj.blur();
    window.alert("Please edit Antenatal Record 1 for this field value.");
}
function onExit() {
  if(confirm("Did you SAVE?")) window.close();
}
function onSave() {
  saveTemp=1;
  onSubmitForm();
}
function onSaveExit() {
  saveTemp=0;
}
function onSaveEnc() {
  saveTemp=4;
}
function onPrint() {
  saveTemp=2;
}
function onSubmitForm() {
  if(document.serviceform.xml_soa.value=="" || document.serviceform.xml_soa.value==" " || document.serviceform.xml_soa.value=="null") {
    alert("Please type in a Signature at the bottom of the form!");
    return false;
  }
  if(document.serviceform.xml_fedb.value.length != 10 && document.serviceform.xml_fedb.value.length != 0 ) {
    alert("Please type in a 'Final EDB' date in a standard format, e.g. 2003/01/01. \nYour action is cancelled! ");
    return false;
  }
  if(saveTemp==0) {
    // document.serviceform.target="apptProvider";
    document.serviceform.cmd.value="Save & Exit";
    document.serviceform.submit();
  }
  if(saveTemp==1) {
    popupPage(30,200,'../provider/notice.htm');
    document.serviceform.target="printlocation";
    document.serviceform.cmd.value="Save";
    document.serviceform.submit();
    document.serviceform.target="apptProvider";
    saveTemp=0;
  }
  if(saveTemp==2) {
    document.serviceform.cmd.value="Print Preview";
    document.serviceform.submit();
  }
  if(saveTemp==4) {
    document.serviceform.cmd.value="Save & Enc";
    document.serviceform.submit();
  }
  return false;
}
//-->
</SCRIPT>
</head>
<body onLoad="setfocus()" bgproperties="fixed" topmargin="0"
	leftmargin="0" rightmargin="0" bgcolor="#c4e9f6">
<form name="serviceform" action="../provider/providercontrol.jsp"
	method="POST" onSubmit="return (onSubmitForm());">
<%
  //if bNewForm is false (0), then it should be able to display xml data.
  boolean bNew = true, bNewList = true; //bNew=if using the old form data, bNewList=if using dynamic list data
  String[] param2 =new String[2];
  String content="", demoname=null,address=null,dob=null,homephone=null,workphone=null,familydoc=null,allergies="",medications="";
  String birthAttendants="", newbornCare="",riskFactors="",finalEDB="",g="",t="",p="",a="",l="",prepregwt="";
  int pageno = 1;
  int age=0;
  if( request.getParameter("bNewForm")!=null && request.getParameter("bNewForm").compareTo("0")==0 ) bNew = false;

  if(!bNew ) { //not new form
    bNewList = false;
    Form f = formDao.find(Integer.parseInt(request.getParameter("form_no")));
    if(f != null) {
    	content = f.getContent();
    	%> <xml id="xml_list"><encounter><%=content%></encounter></xml> <%
    }
  } else {

	//get the data from the latest version of artenatal record 1 or 2
    Form f =  formDao.search_form_no(Integer.parseInt(request.getParameter("demographic_no")), "ar%");
    if (f != null) {
      content = f.getContent();
      birthAttendants = SxmlMisc.getXmlContent(content, "<xml_ba>","</xml_ba>");
	  birthAttendants = birthAttendants==null?"":birthAttendants;
      newbornCare = SxmlMisc.getXmlContent(content, "<xml_nc>","</xml_nc>");
	  newbornCare = newbornCare==null?"":newbornCare;
      riskFactors = SxmlMisc.getXmlContent(content, "<xml_rfi>","</xml_rfi>");
	  riskFactors = riskFactors==null?"":riskFactors;
      finalEDB = SxmlMisc.getXmlContent(content, "<xml_fedb>","</xml_fedb>");
	  finalEDB = finalEDB==null?"":finalEDB;
      g = SxmlMisc.getXmlContent(content, "<xml_gra>","</xml_gra>");
	  g = g==null?"":g;
      t = SxmlMisc.getXmlContent(content, "<xml_term>","</xml_term>");
	  t = t==null?"":t;
      p = SxmlMisc.getXmlContent(content, "<xml_prem>","</xml_prem>");
	  p = p==null?"":p;
      int aa = Integer.parseInt((SxmlMisc.getXmlContent(content, "<xml_ect>","</xml_ect>")==null?"0":SxmlMisc.getXmlContent(content, "<xml_ect>","</xml_ect>")).trim() )
	     +Integer.parseInt((SxmlMisc.getXmlContent(content, "<xml_tet>","</xml_tet>")==null?"0":SxmlMisc.getXmlContent(content, "<xml_tet>","</xml_tet>")).trim() )
	     +Integer.parseInt((SxmlMisc.getXmlContent(content, "<xml_spt>","</xml_spt>")==null?"0":SxmlMisc.getXmlContent(content, "<xml_spt>","</xml_spt>")).trim() )
	     +Integer.parseInt((SxmlMisc.getXmlContent(content, "<xml_stt>","</xml_stt>")==null?"0":SxmlMisc.getXmlContent(content, "<xml_stt>","</xml_stt>")).trim() ) ;
	  a = ""+aa;
	  l = SxmlMisc.getXmlContent(content, "<xml_liv>","</xml_liv>");
	  l = l==null?"":l;
	  prepregwt = SxmlMisc.getXmlContent(content, "<xml_ppw>","</xml_ppw>");
	  prepregwt = prepregwt==null?"":prepregwt;

	  //page no
	  pageno = Integer.parseInt(SxmlMisc.getXmlContent(content, "<xml_pageno>","</xml_pageno>")==null?"1":SxmlMisc.getXmlContent(content, "<xml_pageno>","</xml_pageno>"));
      if( request.getParameter("bNext")!=null && request.getParameter("bNext").compareTo("1")==0 ) pageno++;
	}

    f = formDao.search_form_no(Integer.parseInt(request.getParameter("demographic_no")), "ar2%");	
    
    if (f != null) {
      bNew = false;
      content = f.getContent();
%> <xml id="xml_list"> <encounter> <%=content%> </encounter> </xml> <%
    }

    Demographic d = demographicDao.getDemographic(request.getParameter("demographic_no"));
    
    if(d != null) {
      demoname=d.getFormattedName();
      address=d.getAddress() +",  "+d.getCity() +",  "+ d.getProvince() +"  "+ d.getPostal();
      dob=d.getYearOfBirth()+"/"+d.getMonthOfBirth()+"/"+d.getDateOfBirth();
      homephone=d.getPhone();
      workphone=d.getPhone2();   
      familydoc=d.getFamilyDoctor();
      age=MyDateFormat.getAge(Integer.parseInt(d.getYearOfBirth()),Integer.parseInt(d.getMonthOfBirth()),Integer.parseInt(d.getDateOfBirth()));
    }

    DemographicAccessory da = demographicAccessoryDao.find(Integer.parseInt(request.getParameter("demographic_no")));
    if(da != null) {
    	allergies=SxmlMisc.getXmlContent(da.getContent(),"<xml_Alert>","</xml_Alert>");
        medications=SxmlMisc.getXmlContent(da.getContent(),"<xml_Medication>","</xml_Medication>");
    }

  }
  //boolean bNewDemoAcc=true;
  if( request.getParameter("bNew")!=null && request.getParameter("bNew").compareTo("1")==0 ) bNew = true; //here for another ar2 new form, continue from finished previous ar2 form
  if( request.getParameter("bNewForm")!=null && request.getParameter("bNewForm").compareTo("1")==0 ) bNew = true; //here for a total new ar2 form
%>


<table border="0" cellspacing="0" cellpadding="0" width="100%"
	<%=bNew?"":"datasrc='#xml_list'"%>>
	<tr bgcolor="#486ebd">
		<th width="25%" nowrap><!--input type="hidden" name="xml_pageno" value="<%--=pageno--%>"-->
		<%--=bNewList?"<a href=# onClick='onSave()'><img src='../images/buttonsave.gif' align='top' width='75' height='25' ></a> ":""--%>
		<%=bNewList?"<input type='button' name='savetemp' value=' Save ' onClick='onSave()'> ":""%>
		<%--=bNewList&&!(request.getParameter("patientmaster")!=null)?"<input type='submit' name='saveexit' value='Save to Enc.& Exit' onClick='onSaveExit()'> ":""--%>
		<%=bNewList&&!(request.getParameter("patientmaster")!=null)?"<input type='submit' name='saveexit' value='Save & Exit' onClick='onSaveExit()'> ":""%></th>
		<th align=CENTER><font face="Helvetica" color="#FFFFFF">ANTENATAL
		RECORD 2</font> <font color="orange">page <input type="text"
			name="xml_pageno" size="1" maxlength="1"
			<%=bNewList?("value='"+pageno+"'"):"datafld='xml_pageno'"%>></font></th>
		<th width="25%" nowrap>
		<div align="right">
		<%
  //if(bNewList&&!(request.getParameter("patientmaster")!=null) ) {
  if(bNewList || (request.getParameter("patientmaster")!=null) ) {
%> <a href=#
			onClick="popupPage(600,900,'../provider/providercontrol.jsp?appointment_no=<%=request.getParameter("appointment_no")%>&demographic_no=<%=request.getParameter("demographic_no")%>&curProvider_no=&bNewForm=1&username=&reason=<%=URLEncoder.encode(request.getParameter("reason")==null?"":request.getParameter("reason"))%>&displaymode=ar1&dboperation=search_demograph');return false;"
			title="Antenatal Record 1"> <font color='yellow'>View AR1</font></a>
		| <a href=#
			onClick="popupPage(500,600,'../demographic/formhistory.jsp?demographic_no=<%=request.getParameter("demographic_no")%>')"
			title="Previous Antenatal Record 2"> <font color='yellow'>Prev.
		AR2</font></a> | <a
			href="../provider/providercontrol.jsp?appointment_no=<%=request.getParameter("appointment_no")%>&demographic_no=<%=request.getParameter("demographic_no")%>&curProvider_no=<%=request.getParameter("curProvider_no")%>&username=<%=request.getParameter("username")%>&reason=<%=request.getParameter("reason")%>&displaymode=ar2&dboperation=search_demograph&template=&bNext=1"
			title="Next AR2 Form"> <font color='yellow'> Next AR2 </font></a> <% } %>
		<%=bNewList?"<input type='button' name='Button' value=' Exit ' onClick='onExit();'>":"<input type='button' name='Button' value=' Exit ' onClick='window.close();'>" %>
		<input type="submit" name="print" value="Print Preview"
			onClick='onPrint()'> <a href=#
			onClick="popupPage(200,300,'formarprintsetting.jsp');return false;">.</a>
		  <font color='yellow'> New AR2 </font></a--> <input type="hidden"
			name="oox" value="0"> <input type="hidden" name="ooy"
			value="0"> <input type="hidden" name="cmd" value="">
		<%
String newFormURL = "../provider/providercontrol.jsp?";
if (request.getParameter("demographic_no") != null) newFormURL += "demographic_no=" + request.getParameter("demographic_no");
if (request.getParameter("appointment_no") != null) newFormURL += "&appointment_no=" + request.getParameter("appointment_no");
if (request.getParameter("reason") != null) newFormURL += "&reason=" + URLEncoder.encode(request.getParameter("reason"));
newFormURL += "&bNewForm=1&displaymode=ar2&dboperation=search_demograph&template=";

%> <a href="<%=newFormURL%>"><font color="yellow">New Form</font></a>&nbsp;
		</div>
		</th>
	</tr>
</table>

<table width="60%" border="1" cellspacing="0" cellpadding="0"
	<%=bNew?"":"datasrc='#xml_list'"%>>
	<tr>
		<td valign="top" colspan='2'>Name <input type="text"
			name="xml_name" style="width: 100%" size="30" maxlength="60"
			<%=bNewList?"value=\""+demoname+"\"":"datafld='xml_name'"%>>
		</td>
	</tr>
	<tr>
		<td valign="top" colspan='2'>Address <input type="text"
			name="xml_address" style="width: 100%" size="60" maxlength="80"
			<%=bNewList?"value=\""+address+"\"":"datafld='xml_address'"%>>
		</td>
	</tr>
	<tr>
		<td valign="top" width="50%">Birth attendants<br>
		<input type="text" name="xml_ba" size="15" style="width: 100%"
			maxlength="25"
			<%=bNewList?("value=\""+birthAttendants+"\""):"datafld='xml_ba'"%>>
		</td>
		<td>Newborn care<br>
		<input type="text" name="xml_nc" size="15" style="width: 100%"
			maxlength="25"
			<%=bNewList?("value=\""+newbornCare+"\""):"datafld='xml_nc'"%>>
		</td>
	</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td bgcolor="#CCCCCC">
		<div align="center"><b>Summary of Risk Factors, Allergies,
		and Medications</b></div>
		</td>
	</tr>
</table>
<table width="100%" border="1" cellspacing="0" cellpadding="0"
	<%=bNew?"":"datasrc='#xml_list'"%>>
	<tr align="center">
		<td width="50%">Risk Factors</td>
		<td width="25%">Allergies</td>
		<td>Medications</td>
	</tr>
	<tr align="center">
		<td><textarea name="xml_rfi" cols="20" rows="3"
			style="width: 100%" <%=bNewList?(""):"datafld='xml_rfi'"%>><%=bNewList?(riskFactors):""%></textarea></td>
		<td><textarea name="xml_Alert_demographicaccessory"
			style="width: 100%" cols="30" rows="3"
			<%=bNewList?"":"datafld='xml_Alert_demographicaccessory'"%>><%=bNewList?allergies:""%></textarea></td>
		<td><textarea name="xml_Medication_demographicaccessory"
			style="width: 100%" cols="30" rows="3"
			<%=bNewList?"":"datafld='xml_Medication_demographicaccessory'"%>><%=bNewList?medications:""%></textarea></td>
	</tr>
</table>

<script language="JavaScript">
<!--
	function calcWeek(source) {
<%
String fedb = null;
if (bNewList) fedb = finalEDB;
else fedb = SxmlMisc.getXmlContent(content, "xml_fedb");   //:"datafld='xml_fedb'";

String sDate = "";
if (fedb != null && fedb.length() == 10 ) {
	FrmGraphicAR arG = new FrmGraphicAR();
	java.util.Date edbDate = arG.getStartDate(fedb);
    sDate = UtilDateUtilities.DateToString(edbDate, "MMMMM dd, yyyy"); //"yy,MM,dd");
%>
	    var delta = 0;
        var str_date = getDateField(source.name);
        if (str_date.length < 10) return;
        var yyyy = str_date.substring(0, str_date.indexOf("/"));
        var mm = eval(str_date.substring(eval(str_date.indexOf("/")+1), str_date.lastIndexOf("/")) - 1);
        var dd = str_date.substring(eval(str_date.lastIndexOf("/")+1));
        var check_date=new Date(yyyy,mm,dd);
        var start=new Date("<%=sDate%>");

		if (check_date.getUTCHours() != start.getUTCHours()) {
			if (check_date.getUTCHours() > start.getUTCHours()) {
			    delta = -1 * 60 * 60 * 1000;
			} else {
			    delta = 1 * 60 * 60 * 1000;
			}
		}

		var day = eval((check_date.getTime() - start.getTime() + delta) / (24*60*60*1000));
        var week = Math.floor(day/7);
		var weekday = day%7;
        source.value = week + "w+" + weekday;
<% } %>
}

	function getDateField(name) {
		var temp = ""; //xml_sv1ga - xml_sv1da
		var n1 = name.substring(eval(name.indexOf("v")+1), name.indexOf("g"));

		if (n1>17) {
			name = "xml_sv" + n1 + "da";
		} else {
			name = "xml_sv" + n1 + "da";
		}

        for (var i =0; i <document.serviceform.elements.length; i++) {
            if (document.serviceform.elements[i].name == name) {
               return document.serviceform.elements[i].value;
    	    }
	    }
        return temp;
    }
//-->
</SCRIPT>


<table width="100%" border="1" cellspacing="0" cellpadding="0"
	<%=bNew?"":"datasrc='#xml_list'"%>>
	<tr>
		<td nowrap width="13%"><b>Final EDB</b> (yyyy/mm/dd)<br>
		<input type="text" name="xml_fedb" style="width: 100%" size="10"
			maxlength="10"
			<%=bNewList?"value=\""+finalEDB+"\"":"datafld='xml_fedb'"%>>
		</td>
		<td align="center" bgcolor="#CCCCCC" width="3%">G</td>
		<td width="9%"><input type="text" name="xml_gra" size="5"
			style="width: 100%" maxlength="10"
			<%=bNewList?"value=\""+g+"\"":"datafld='xml_gra'"%>></td>
		<td align="center" bgcolor="#CCCCCC" width="3%">T</td>
		<td width="10%"><input type="text" name="xml_term" size="5"
			style="width: 100%" maxlength="10"
			<%=bNewList?"value=\""+t+"\"":"datafld='xml_term'"%>></td>
		<td align="center" bgcolor="#CCCCCC" width="3%">P</td>
		<td width="9%"><input type="text" name="xml_prem" size="5"
			style="width: 100%" maxlength="10"
			<%=bNewList?"value=\""+p+"\"":"datafld='xml_prem'"%>></td>
		<td align="center" bgcolor="#CCCCCC" width="3%">A</td>
		<td width="7%"><input type="text" name="xml_etss" size="5"
			style="width: 100%" maxlength="10"
			<%=bNewList?"value=\""+a+"\"":"datafld='xml_etss'"%> readonly
			onClick="onAR1FieldsFocus(this)"></td>
		<td align="center" bgcolor="#CCCCCC" width="3%">L</td>
		<td width="7%"><input type="text" name="xml_liv" size="5"
			style="width: 100%" maxlength="10"
			<%=bNewList?"value=\""+l+"\"":"datafld='xml_liv'"%>></td>
		<td width="8%">Hb<br>
		<input type="text" name="xml_hb" size="5" style="width: 100%"
			maxlength="10" <%=bNew?"":"datafld='xml_hb'"%>></td>
		<td width="8%">MCV<br>
		<input type="text" name="xml_mcv" size="5" style="width: 100%"
			maxlength="10" <%=bNew?"":"datafld='xml_mcv'"%>></td>
		<td>MSS<br>
		<input type="text" name="xml_mss" size="5" style="width: 100%"
			maxlength="10" <%=bNew?"":"datafld='xml_mss'"%>></td>
	</tr>
</table>
<table width="100%" border="1" cellspacing="0" cellpadding="0"
	<%=bNew?"":"datasrc='#xml_list'"%>>
	<tr>
		<td nowrap width="13%">Pre-preg. wt.<br>
		<input type="text" name="xml_ppw" size="5" style="width: 100%"
			maxlength="6"
			<%=bNewList?"value=\""+prepregwt+"\"":"datafld='xml_ppw'"%>>
		</td>
		<td align="center" bgcolor="#CCCCCC" width="6%">Rubella<br>
		immune</td>
		<td width="6%"><input type="text" name="xml_rub" size="5"
			style="width: 100%" maxlength="5" <%=bNew?"":"datafld='xml_rub'"%>>
		</td>
		<td align="center" bgcolor="#CCCCCC" width="6%" nowrap>HBs Ag</td>
		<td width="7%"><input type="text" name="xml_hbs" size="5"
			style="width: 100%" maxlength="6" <%=bNew?"":"datafld='xml_hbs'"%>>
		</td>
		<td align="center" bgcolor="#CCCCCC" width="5%">VDRL</td>
		<td width="6%"><input type="text" name="xml_vdr" size="5"
			style="width: 100%" maxlength="6" <%=bNew?"":"datafld='xml_vdr'"%>>
		</td>
		<td align="center" bgcolor="#CCCCCC" width="6%">Blood<br>
		group</td>
		<td width="5%"><input type="text" name="xml_blo" size="5"
			style="width: 100%" maxlength="6" <%=bNew?"":"datafld='xml_blo'"%>>
		</td>
		<td align="center" bgcolor="#CCCCCC" width="6%">Rh type<br>
		(+/-)</td>
		<td width="5%"><input type="text" name="xml_rhp" size="6"
			style="width: 100%" maxlength="6" <%=bNew?"":"datafld='xml_rhp'"%>>
		</td>
		<td width="7%" bgcolor="#CCCCCC">
		<div align="center">Antibodies</div>
		</td>
		<td width="12%"><input type="text" name="xml_anti" size="5"
			style="width: 100%" maxlength="6" <%=bNew?"":"datafld='xml_anti'"%>>
		</td>
		<td width="6%" nowrap bgcolor="#CCCCCC">
		<div align="center">Rh IG<br>
		Given</div>
		</td>
		<td><input type="text" name="xml_rhg" size="5"
			style="width: 100%" maxlength="6" <%=bNew?"":"datafld='xml_rhg'"%>>
		</td>
	</tr>
</table>
<table width="100%" border="0">
	<tr>
		<td valign="top" bgcolor="#CCCCCC" align="center"><b>Subsequent
		Visits</b></td>
	</tr>
</table>

<table width="100%" border="1" cellspacing="0" cellpadding="0"
	<%=bNew?"":"datasrc='#xml_list'"%>>
	<tr align="center">
		<td width="11%">Date<br>
		(yyyy/mm/dd)</td>
		<td width="7%">G-age<br>
		wk.</td>
		<td width="7%">S-F<br>
		Ht.</td>
		<td width="7%">Wt.<br>
		(lb/K)</td>
		<td width="7%">Presn<br>
		Posn.</td>
		<td width="7%">FHR/Fm</td>
		<td width="6%" colspan="2">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td colspan="2" align="center">Urine</td>
			</tr>
			<tr align="center">
				<td>Pr</td>
				<td>Gl</td>
			</tr>
		</table>
		</td>
		<td width="11%">B.P.</td>
		<td width="33%" nowrap>Comments</td>
		<td nowrap width="4%">Cig./<br>
		day</td>
	</tr>
	<%
  boolean bTemp = bNew;
  if( request.getParameter("bNext")!=null && request.getParameter("bNext").compareTo("1")==0 ) bNew = true; //here for next ar2 form, continue from finished previous ar2 form
%>
	<tr align="center">
		<td width="11%"><input type="text" name="xml_sv1da" size="10"
			maxlength="10" style="width: 90%" <%=bNew?"":"datafld='xml_sv1da'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv1ga" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv1ga'"%>
			onDblClick="calcWeek(this)"></td>
		<td width="7%"><input type="text" name="xml_sv1sf" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv1sf'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv1wt" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv1wt'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv1pr" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv1pr'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv1fh" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv1fh'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv1upr" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv1upr'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv1ugl" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv1ugl'"%>>
		</td>
		<td width="11%"><input type="text" name="xml_sv1bp" size="7"
			maxlength="8" style="width: 100%" <%=bNew?"":"datafld='xml_sv1bp'"%>>
		</td>
		<td nowrap width="33%"><input type="text" name="xml_sv1co"
			size="20" maxlength="70" style="width: 100%"
			<%=bNew?"":"datafld='xml_sv1co'"%>></td>
		<td nowrap width="4%"><input type="text" name="xml_sv1ra"
			size="5" maxlength="5" style="width: 90%"
			<%=bNew?"":"datafld='xml_sv1ra'"%>></td>
	</tr>
	<tr align="center">
		<td width="11%"><input type="text" name="xml_sv2da" size="10"
			maxlength="10" style="width: 90%" <%=bNew?"":"datafld='xml_sv2da'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv2ga" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv2ga'"%>
			onDblClick="calcWeek(this)"></td>
		<td width="7%"><input type="text" name="xml_sv2sf" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv2sf'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv2wt" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv2wt'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv2pr" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv2pr'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv2fh" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv2fh'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv2upr" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv2upr'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv2ugl" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv2ugl'"%>>
		</td>
		<td width="11%"><input type="text" name="xml_sv2bp" size="7"
			maxlength="8" style="width: 100%" <%=bNew?"":"datafld='xml_sv2bp'"%>>
		</td>
		<td nowrap width="33%"><input type="text" name="xml_sv2co"
			size="20" maxlength="70" style="width: 100%"
			<%=bNew?"":"datafld='xml_sv2co'"%>></td>
		<td nowrap width="4%"><input type="text" name="xml_sv2ra"
			size="5" maxlength="5" style="width: 90%"
			<%=bNew?"":"datafld='xml_sv2ra'"%>></td>
	</tr>
	<tr align="center">
		<td width="11%"><input type="text" name="xml_sv3da" size="10"
			maxlength="10" style="width: 90%" <%=bNew?"":"datafld='xml_sv3da'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv3ga" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv3ga'"%>
			onDblClick="calcWeek(this)"></td>
		<td width="7%"><input type="text" name="xml_sv3sf" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv3sf'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv3wt" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv3wt'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv3pr" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv3pr'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv3fh" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv3fh'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv3upr" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv3upr'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv3ugl" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv3ugl'"%>>
		</td>
		<td width="11%"><input type="text" name="xml_sv3bp" size="7"
			maxlength="8" style="width: 100%" <%=bNew?"":"datafld='xml_sv3bp'"%>>
		</td>
		<td nowrap width="33%"><input type="text" name="xml_sv3co"
			size="20" maxlength="70" style="width: 100%"
			<%=bNew?"":"datafld='xml_sv3co'"%>></td>
		<td nowrap width="4%"><input type="text" name="xml_sv3ra"
			size="5" maxlength="5" style="width: 90%"
			<%=bNew?"":"datafld='xml_sv3ra'"%>></td>
	</tr>
	<tr align="center">
		<td width="11%"><input type="text" name="xml_sv4da" size="10"
			maxlength="10" style="width: 90%" <%=bNew?"":"datafld='xml_sv4da'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv4ga" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv4ga'"%>
			onDblClick="calcWeek(this)"></td>
		<td width="7%"><input type="text" name="xml_sv4sf" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv4sf'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv4wt" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv4wt'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv4pr" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv4pr'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv4fh" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv4fh'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv4upr" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv4upr'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv4ugl" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv4ugl'"%>>
		</td>
		<td width="11%"><input type="text" name="xml_sv4bp" size="7"
			maxlength="8" style="width: 100%" <%=bNew?"":"datafld='xml_sv4bp'"%>>
		</td>
		<td nowrap width="33%"><input type="text" name="xml_sv4co"
			size="20" maxlength="70" style="width: 100%"
			<%=bNew?"":"datafld='xml_sv4co'"%>></td>
		<td nowrap width="4%"><input type="text" name="xml_sv4ra"
			size="5" maxlength="5" style="width: 90%"
			<%=bNew?"":"datafld='xml_sv4ra'"%>></td>
	</tr>
	<tr align="center">
		<td width="11%"><input type="text" name="xml_sv5da" size="10"
			maxlength="10" style="width: 90%" <%=bNew?"":"datafld='xml_sv5da'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv5ga" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv5ga'"%>
			onDblClick="calcWeek(this)"></td>
		<td width="7%"><input type="text" name="xml_sv5sf" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv5sf'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv5wt" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv5wt'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv5pr" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv5pr'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv5fh" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv5fh'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv5upr" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv5upr'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv5ugl" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv5ugl'"%>>
		</td>
		<td width="11%"><input type="text" name="xml_sv5bp" size="7"
			maxlength="8" style="width: 100%" <%=bNew?"":"datafld='xml_sv5bp'"%>>
		</td>
		<td nowrap width="33%"><input type="text" name="xml_sv5co"
			size="20" maxlength="70" style="width: 100%"
			<%=bNew?"":"datafld='xml_sv5co'"%>></td>
		<td nowrap width="4%"><input type="text" name="xml_sv5ra"
			size="5" maxlength="5" style="width: 90%"
			<%=bNew?"":"datafld='xml_sv5ra'"%>></td>
	</tr>
	<tr align="center">
		<td width="11%"><input type="text" name="xml_sv6da" size="10"
			maxlength="10" style="width: 90%" <%=bNew?"":"datafld='xml_sv6da'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv6ga" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv6ga'"%>
			onDblClick="calcWeek(this)"></td>
		<td width="7%"><input type="text" name="xml_sv6sf" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv6sf'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv6wt" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv6wt'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv6pr" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv6pr'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv6fh" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv6fh'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv6upr" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv6upr'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv6ugl" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv6ugl'"%>>
		</td>
		<td width="11%"><input type="text" name="xml_sv6bp" size="7"
			maxlength="8" style="width: 100%" <%=bNew?"":"datafld='xml_sv6bp'"%>>
		</td>
		<td nowrap width="33%"><input type="text" name="xml_sv6co"
			size="20" maxlength="70" style="width: 100%"
			<%=bNew?"":"datafld='xml_sv6co'"%>></td>
		<td nowrap width="4%"><input type="text" name="xml_sv6ra"
			size="5" maxlength="5" style="width: 90%"
			<%=bNew?"":"datafld='xml_sv6ra'"%>></td>
	</tr>
	<tr align="center">
		<td width="11%"><input type="text" name="xml_sv7da" size="10"
			maxlength="10" style="width: 90%" <%=bNew?"":"datafld='xml_sv7da'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv7ga" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv7ga'"%>
			onDblClick="calcWeek(this)"></td>
		<td width="7%"><input type="text" name="xml_sv7sf" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv7sf'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv7wt" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv7wt'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv7pr" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv7pr'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv7fh" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv7fh'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv7upr" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv7upr'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv7ugl" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv7ugl'"%>>
		</td>
		<td width="11%"><input type="text" name="xml_sv7bp" size="7"
			maxlength="8" style="width: 100%" <%=bNew?"":"datafld='xml_sv7bp'"%>>
		</td>
		<td nowrap width="33%"><input type="text" name="xml_sv7co"
			size="20" maxlength="70" style="width: 100%"
			<%=bNew?"":"datafld='xml_sv7co'"%>></td>
		<td nowrap width="4%"><input type="text" name="xml_sv7ra"
			size="5" maxlength="5" style="width: 90%"
			<%=bNew?"":"datafld='xml_sv7ra'"%>></td>
	</tr>
	<tr align="center">
		<td width="11%"><input type="text" name="xml_sv8da" size="10"
			maxlength="10" style="width: 90%" <%=bNew?"":"datafld='xml_sv8da'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv8ga" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv8ga'"%>
			onDblClick="calcWeek(this)"></td>
		<td width="7%"><input type="text" name="xml_sv8sf" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv8sf'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv8wt" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv8wt'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv8pr" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv8pr'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv8fh" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv8fh'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv8upr" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv8upr'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv8ugl" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv8ugl'"%>>
		</td>
		<td width="11%"><input type="text" name="xml_sv8bp" size="7"
			maxlength="8" style="width: 100%" <%=bNew?"":"datafld='xml_sv8bp'"%>>
		</td>
		<td nowrap width="33%"><input type="text" name="xml_sv8co"
			size="20" maxlength="70" style="width: 100%"
			<%=bNew?"":"datafld='xml_sv8co'"%>></td>
		<td nowrap width="4%"><input type="text" name="xml_sv8ra"
			size="5" maxlength="5" style="width: 90%"
			<%=bNew?"":"datafld='xml_sv8ra'"%>></td>
	</tr>
	<tr align="center">
		<td width="11%"><input type="text" name="xml_sv9da" size="10"
			maxlength="10" style="width: 90%" <%=bNew?"":"datafld='xml_sv9da'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv9ga" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv9ga'"%>
			onDblClick="calcWeek(this)"></td>
		<td width="7%"><input type="text" name="xml_sv9sf" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv9sf'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv9wt" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv9wt'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv9pr" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv9pr'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv9fh" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv9fh'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv9upr" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv9upr'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv9ugl" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv9ugl'"%>>
		</td>
		<td width="11%"><input type="text" name="xml_sv9bp" size="7"
			maxlength="8" style="width: 100%" <%=bNew?"":"datafld='xml_sv9bp'"%>>
		</td>
		<td nowrap width="33%"><input type="text" name="xml_sv9co"
			size="20" maxlength="70" style="width: 100%"
			<%=bNew?"":"datafld='xml_sv9co'"%>></td>
		<td nowrap width="4%"><input type="text" name="xml_sv9ra"
			size="5" maxlength="5" style="width: 90%"
			<%=bNew?"":"datafld='xml_sv9ra'"%>></td>
	</tr>
	<tr align="center">
		<td width="11%"><input type="text" name="xml_sv10da" size="10"
			maxlength="10" style="width: 90%" <%=bNew?"":"datafld='xml_sv10da'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv10ga" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv10ga'"%>
			onDblClick="calcWeek(this)"></td>
		<td width="7%"><input type="text" name="xml_sv10sf" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv10sf'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv10wt" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv10wt'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv10pr" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv10pr'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv10fh" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv10fh'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv10upr" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv10upr'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv10ugl" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv10ugl'"%>>
		</td>
		<td width="11%"><input type="text" name="xml_sv10bp" size="7"
			maxlength="8" style="width: 100%" <%=bNew?"":"datafld='xml_sv10bp'"%>>
		</td>
		<td nowrap width="33%"><input type="text" name="xml_sv10co"
			size="20" maxlength="70" style="width: 100%"
			<%=bNew?"":"datafld='xml_sv10co'"%>></td>
		<td nowrap width="4%"><input type="text" name="xml_sv10ra"
			size="5" maxlength="5" style="width: 90%"
			<%=bNew?"":"datafld='xml_sv10ra'"%>></td>
	</tr>
	<tr align="center">
		<td width="11%"><input type="text" name="xml_sv11da" size="10"
			maxlength="10" style="width: 90%" <%=bNew?"":"datafld='xml_sv11da'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv11ga" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv11ga'"%>
			onDblClick="calcWeek(this)"></td>
		<td width="7%"><input type="text" name="xml_sv11sf" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv11sf'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv11wt" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv11wt'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv11pr" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv11pr'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv11fh" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv11fh'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv11upr" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv11upr'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv11ugl" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv11ugl'"%>>
		</td>
		<td width="11%"><input type="text" name="xml_sv11bp" size="7"
			maxlength="8" style="width: 100%" <%=bNew?"":"datafld='xml_sv11bp'"%>>
		</td>
		<td nowrap width="33%"><input type="text" name="xml_sv11co"
			size="20" maxlength="70" style="width: 100%"
			<%=bNew?"":"datafld='xml_sv11co'"%>></td>
		<td nowrap width="4%"><input type="text" name="xml_sv11ra"
			size="5" maxlength="5" style="width: 90%"
			<%=bNew?"":"datafld='xml_sv11ra'"%>></td>
	</tr>
	<tr align="center">
		<td width="11%"><input type="text" name="xml_sv12da" size="10"
			maxlength="10" style="width: 90%" <%=bNew?"":"datafld='xml_sv12da'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv12ga" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv12ga'"%>
			onDblClick="calcWeek(this)"></td>
		<td width="7%"><input type="text" name="xml_sv12sf" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv12sf'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv12wt" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv12wt'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv12pr" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv12pr'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv12fh" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv12fh'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv12upr" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv12upr'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv12ugl" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv12ugl'"%>>
		</td>
		<td width="11%"><input type="text" name="xml_sv12bp" size="7"
			maxlength="8" style="width: 100%" <%=bNew?"":"datafld='xml_sv12bp'"%>>
		</td>
		<td nowrap width="33%"><input type="text" name="xml_sv12co"
			size="20" maxlength="70" style="width: 100%"
			<%=bNew?"":"datafld='xml_sv12co'"%>></td>
		<td nowrap width="4%"><input type="text" name="xml_sv12ra"
			size="5" maxlength="5" style="width: 90%"
			<%=bNew?"":"datafld='xml_sv12ra'"%>></td>
	</tr>
	<tr align="center">
		<td width="11%"><input type="text" name="xml_sv13da" size="10"
			maxlength="10" style="width: 90%" <%=bNew?"":"datafld='xml_sv13da'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv13ga" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv13ga'"%>
			onDblClick="calcWeek(this)"></td>
		<td width="7%"><input type="text" name="xml_sv13sf" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv13sf'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv13wt" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv13wt'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv13pr" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv13pr'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv13fh" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv13fh'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv13upr" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv13upr'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv13ugl" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv13ugl'"%>>
		</td>
		<td width="11%"><input type="text" name="xml_sv13bp" size="7"
			maxlength="8" style="width: 100%" <%=bNew?"":"datafld='xml_sv13bp'"%>>
		</td>
		<td nowrap width="33%"><input type="text" name="xml_sv13co"
			size="20" maxlength="70" style="width: 100%"
			<%=bNew?"":"datafld='xml_sv13co'"%>></td>
		<td nowrap width="4%"><input type="text" name="xml_sv13ra"
			size="5" maxlength="5" style="width: 90%"
			<%=bNew?"":"datafld='xml_sv13ra'"%>></td>
	</tr>
	<tr align="center">
		<td width="11%"><input type="text" name="xml_sv14da" size="10"
			maxlength="10" style="width: 90%" <%=bNew?"":"datafld='xml_sv14da'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv14ga" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv14ga'"%>
			onDblClick="calcWeek(this)"></td>
		<td width="7%"><input type="text" name="xml_sv14sf" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv14sf'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv14wt" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv14wt'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv14pr" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv14pr'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv14fh" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv14fh'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv14upr" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv14upr'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv14ugl" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv14ugl'"%>>
		</td>
		<td width="11%"><input type="text" name="xml_sv14bp" size="7"
			maxlength="8" style="width: 100%" <%=bNew?"":"datafld='xml_sv14bp'"%>>
		</td>
		<td nowrap width="33%"><input type="text" name="xml_sv14co"
			size="20" maxlength="70" style="width: 100%"
			<%=bNew?"":"datafld='xml_sv14co'"%>></td>
		<td nowrap width="4%"><input type="text" name="xml_sv14ra"
			size="5" maxlength="5" style="width: 90%"
			<%=bNew?"":"datafld='xml_sv14ra'"%>></td>
	</tr>
	<tr align="center">
		<td width="11%"><input type="text" name="xml_sv15da" size="10"
			maxlength="10" style="width: 90%" <%=bNew?"":"datafld='xml_sv15da'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv15ga" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv15ga'"%>
			onDblClick="calcWeek(this)"></td>
		<td width="7%"><input type="text" name="xml_sv15sf" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv15sf'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv15wt" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv15wt'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv15pr" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv15pr'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv15fh" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv15fh'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv15upr" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv15upr'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv15ugl" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv15ugl'"%>>
		</td>
		<td width="11%"><input type="text" name="xml_sv15bp" size="7"
			maxlength="8" style="width: 100%" <%=bNew?"":"datafld='xml_sv15bp'"%>>
		</td>
		<td nowrap width="33%"><input type="text" name="xml_sv15co"
			size="20" maxlength="70" style="width: 100%"
			<%=bNew?"":"datafld='xml_sv15co'"%>></td>
		<td nowrap width="4%"><input type="text" name="xml_sv15ra"
			size="5" maxlength="5" style="width: 90%"
			<%=bNew?"":"datafld='xml_sv15ra'"%>></td>
	</tr>
	<tr align="center">
		<td width="11%"><input type="text" name="xml_sv16da" size="10"
			maxlength="10" style="width: 90%" <%=bNew?"":"datafld='xml_sv16da'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv16ga" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv16ga'"%>
			onDblClick="calcWeek(this)"></td>
		<td width="7%"><input type="text" name="xml_sv16sf" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv16sf'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv16wt" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv16wt'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv16pr" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv16pr'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv16fh" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv16fh'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv16upr" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv16upr'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv16ugl" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv16ugl'"%>>
		</td>
		<td width="11%"><input type="text" name="xml_sv16bp" size="7"
			maxlength="8" style="width: 100%" <%=bNew?"":"datafld='xml_sv16bp'"%>>
		</td>
		<td nowrap width="33%"><input type="text" name="xml_sv16co"
			size="20" maxlength="70" style="width: 100%"
			<%=bNew?"":"datafld='xml_sv16co'"%>></td>
		<td nowrap width="4%"><input type="text" name="xml_sv16ra"
			size="5" maxlength="5" style="width: 90%"
			<%=bNew?"":"datafld='xml_sv16ra'"%>></td>
	</tr>
	<tr align="center">
		<td width="11%"><input type="text" name="xml_sv17da" size="10"
			maxlength="10" style="width: 90%" <%=bNew?"":"datafld='xml_sv17da'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv17ga" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv17ga'"%>
			onDblClick="calcWeek(this)"></td>
		<td width="7%"><input type="text" name="xml_sv17sf" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv17sf'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv17wt" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv17wt'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv17pr" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv17pr'"%>>
		</td>
		<td width="7%"><input type="text" name="xml_sv17fh" size="6"
			maxlength="6" style="width: 90%" <%=bNew?"":"datafld='xml_sv17fh'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv17upr" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv17upr'"%>>
		</td>
		<td width="4%"><input type="text" name="xml_sv17ugl" size="2"
			maxlength="3" style="width: 90%" <%=bNew?"":"datafld='xml_sv17ugl'"%>>
		</td>
		<td width="11%"><input type="text" name="xml_sv17bp" size="7"
			maxlength="8" style="width: 100%" <%=bNew?"":"datafld='xml_sv17bp'"%>>
		</td>
		<td nowrap width="33%"><input type="text" name="xml_sv17co"
			size="20" maxlength="70" style="width: 100%"
			<%=bNew?"":"datafld='xml_sv17co'"%>></td>
		<td nowrap width="4%"><input type="text" name="xml_sv17ra"
			size="5" maxlength="5" style="width: 90%"
			<%=bNew?"":"datafld='xml_sv17ra'"%>></td>
	</tr>
	<%
  bNew = bTemp;
%>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0"
	<%=bNew?"":"datasrc='#xml_list'"%>>
	<tr>
		<td width="30%">&nbsp;</td>
		<td width="70%" valign="top">
		<table width="100%" border="1" cellspacing="0" cellpadding="0">
			<tr>
				<td colspan="4" align="center" bgcolor="#CCCCCC"><b>Ultrasound</b></td>
				<td rowspan="6" nowrap width="18%"><b> &nbsp; &nbsp;
				Referral Plan<br>
				</b> <input type="checkbox" name="xml_rpob" value="checked"
					<%=bNew?"":"datafld='xml_rpob'"%>> Obstetrician<br>
				<input type="checkbox" name="xml_rppp" value="checked"
					<%=bNew?"":"datafld='xml_rppp'"%>> Pediatrician<br>
				<input type="checkbox" name="xml_rpan" value="checked"
					<%=bNew?"":"datafld='xml_rpan'"%>> Anesthesiologist<br>
				<input type="checkbox" name="xml_rpsw" value="checked"
					<%=bNew?"":"datafld='xml_rpsw'"%>> Social worker<br>
				<input type="checkbox" name="xml_rpdi" value="checked"
					<%=bNew?"":"datafld='xml_rpdi'"%>> Dietitian<br>
				<input type="checkbox" name="xml_rpotc" value="checked"
					<%=bNew?"":"datafld='xml_rpotc'"%>> Other<br>
				<input type="text" name="xml_rpott" size="20" maxlength="35"
					<%=bNew?"":"datafld='xml_rpott'"%>></td>
				<td rowspan="18" nowrap valign="top">&nbsp; &nbsp; <b>Discussion
				Topics<br>
				</b> <input type="checkbox" name="xml_dtdu" value="checked"
					<%=bNew?"":"datafld='xml_dtdu'"%>> Drug use<br>
				<input type="checkbox" name="xml_dtsm" value="checked"
					<%=bNew?"":"datafld='xml_dtsm'"%>> Smoking<br>
				<input type="checkbox" name="xml_dtal" value="checked"
					<%=bNew?"":"datafld='xml_dtal'"%>> Alcohol<br>
				<input type="checkbox" name="xml_dtex" value="checked"
					<%=bNew?"":"datafld='xml_dtex'"%>> Exercise<br>
				<input type="checkbox" name="xml_dtwp" value="checked"
					<%=bNew?"":"datafld='xml_dtwp'"%>> Work plan<br>
				<input type="checkbox" name="xml_dtin" value="checked"
					<%=bNew?"":"datafld='xml_dtin'"%>> Intercourse<br>
				<input type="checkbox" name="xml_dtdc" value="checked"
					<%=bNew?"":"datafld='xml_dtdc'"%>> Dental care<br>
				<input type="checkbox" name="xml_dttr" value="checked"
					<%=bNew?"":"datafld='xml_dttr'"%>> Travel<br>
				<input type="checkbox" name="xml_dtpc" value="checked"
					<%=bNew?"":"datafld='xml_dtpc'"%>> Prenatal classes<br>
				<input type="checkbox" name="xml_dtbf" value="checked"
					<%=bNew?"":"datafld='xml_dtbf'"%>> Breast feeding<br>
				<input type="checkbox" name="xml_dtbp" value="checked"
					<%=bNew?"":"datafld='xml_dtbp'"%>> Birth plan<br>
				<input type="checkbox" name="xml_dtpl" value="checked"
					<%=bNew?"":"datafld='xml_dtpl'"%>> Preterm labour<br>
				<input type="checkbox" name="xml_dtpr" value="checked"
					<%=bNew?"":"datafld='xml_dtpr'"%>> PROM<br>
				<input type="checkbox" name="xml_dtfm" value="checked"
					<%=bNew?"":"datafld='xml_dtfm'"%>> Fetal movement<br>
				<input type="checkbox" name="xml_dtat" value="checked"
					<%=bNew?"":"datafld='xml_dtat'"%>> Admission timing<br>
				<input type="checkbox" name="xml_dtls" value="checked"
					<%=bNew?"":"datafld='xml_dtls'"%>> Labour support<br>
				<input type="checkbox" name="xml_dtpm" value="checked"
					<%=bNew?"":"datafld='xml_dtpm'"%>> Pain management<br>
				<input type="checkbox" name="xml_dtde" value="checked"
					<%=bNew?"":"datafld='xml_dtde'"%>> Depression<br>
				<input type="checkbox" name="xml_dtci" value="checked"
					<%=bNew?"":"datafld='xml_dtci'"%>> Circumcision<br>
				<input type="checkbox" name="xml_dtcs" value="checked"
					<%=bNew?"":"datafld='xml_dtcs'"%>> Car safety<br>
				<input type="checkbox" name="xml_dtco" value="checked"
					<%=bNew?"":"datafld='xml_dtco'"%>> Contraception<br>
				<input type="checkbox" name="xml_dtoc" value="checked"
					<%=bNew?"":"datafld='xml_dtoc'"%>> On call</td>
			</tr>
			<tr>
				<td align="center" width="11%">Date</td>
				<td align="center" width="11%">GA</td>
				<td colspan="2" width="34%" align="center">Result</td>
			</tr>
			<tr>
				<td><input type="text" name="xml_uda1" size="10" maxlength="10"
					<%=bNew?"":"datafld='xml_uda1'"%>></td>
				<td><input type="text" name="xml_uga1" size="10" maxlength="10"
					<%=bNew?"":"datafld='xml_uga1'"%>></td>
				<td colspan="2"><input type="text" name="xml_ure1" size="25"
					<%=bNew?"":"datafld='xml_ure1'"%>></td>
			</tr>
			<tr>
				<td width="34%"><input type="text" name="xml_uda2" size="10"
					maxlength="10" <%=bNew?"":"datafld='xml_uda2'"%>></td>
				<td><input type="text" name="xml_uga2" size="10" maxlength="10"
					<%=bNew?"":"datafld='xml_uga2'"%>></td>
				<td colspan="2"><input type="text" name="xml_ure2" size="25"
					<%=bNew?"":"datafld='xml_ure2'"%>></td>
			</tr>
			<tr>
				<td><input type="text" name="xml_uda3" size="10" maxlength="10"
					<%=bNew?"":"datafld='xml_uda3'"%>></td>
				<td><input type="text" name="xml_uga3" size="10" maxlength="10"
					<%=bNew?"":"datafld='xml_uga3'"%>></td>
				<td colspan="2"><input type="text" name="xml_ure3" size="25"
					<%=bNew?"":"datafld='xml_ure3'"%>></td>
			</tr>
			<tr>
				<td><input type="text" name="xml_uda4" size="10" maxlength="10"
					<%=bNew?"":"datafld='xml_uda4'"%>></td>
				<td><input type="text" name="xml_uga4" size="10" maxlength="10"
					<%=bNew?"":"datafld='xml_uga4'"%>></td>
				<td colspan="2"><input type="text" name="xml_ure4" size="25"
					<%=bNew?"":"datafld='xml_ure4'"%>></td>
			</tr>
			<tr>
				<td colspan="2" align="center" bgcolor="#CCCCCC"><b>Selected
				Tests</b></td>
				<td align="center" bgcolor="#CCCCCC"><b>Result</b></td>
				<td colspan="2" align="center" bgcolor="#CCCCCC"><b>Comments</b></td>
			</tr>
			<tr>
				<td colspan="2">1. Pap</td>
				<td width="13%"><input type="text" name="xml_re1" size="10"
					maxlength="20" <%=bNew?"":"datafld='xml_re1'"%>></td>
				<td colspan="2" rowspan="10" align="center"><textarea
					name="xml_com" style="width: 98%" rows="14" cols="20"
					<%=bNew?"":"datafld='xml_com'"%>></textarea></td>
			</tr>
			<tr>
				<td colspan="2">2. GC/Chlamydia</td>
				<td><input type="text" name="xml_re2" size="10"
					<%=bNew?"":"datafld='xml_re2'"%>></td>
			</tr>
			<tr>
				<td colspan="2">3. HIV</td>
				<td><input type="text" name="xml_re3" size="10"
					<%=bNew?"":"datafld='xml_re3'"%>></td>
			</tr>
			<tr>
				<td colspan="2">4. B. vaginosis</td>
				<td><input type="text" name="xml_re4" size="10"
					<%=bNew?"":"datafld='xml_re4'"%>></td>
			</tr>
			<tr>
				<td colspan="2">5. Group B strep</td>
				<td><input type="text" name="xml_re5" size="10"
					<%=bNew?"":"datafld='xml_re5'"%>></td>
			</tr>
			<tr>
				<td colspan="2">6. Urine culture</td>
				<td><input type="text" name="xml_re6" size="10"
					<%=bNew?"":"datafld='xml_re6'"%>></td>
			</tr>
			<tr>
				<td colspan="2">7. Sickle dex</td>
				<td><input type="text" name="xml_re7" size="10"
					<%=bNew?"":"datafld='xml_re7'"%>></td>
			</tr>
			<tr>
				<td colspan="2">8. Hb electro</td>
				<td><input type="text" name="xml_re8" size="10"
					<%=bNew?"":"datafld='xml_re8'"%>></td>
			</tr>
			<tr>
				<td colspan="2">9. Amnio/CVS</td>
				<td><input type="text" name="xml_re9" size="10"
					<%=bNew?"":"datafld='xml_re9'"%>></td>
			</tr>
			<tr>
				<td colspan="2">10. Glucose screen</td>
				<td><input type="text" name="xml_re10" size="10"
					<%=bNew?"":"datafld='xml_re10'"%>></td>
			</tr>
			<tr>
				<td colspan="2">11. Other<br>
				<input type="text" name="xml_st11" size="20"
					<%=bNew?"":"datafld='xml_st11'"%>></td>
				<td><input type="text" name="xml_re11" size="10"
					<%=bNew?"":"datafld='xml_re11'"%>></td>
				<td colspan="2">Psychosocial issues<br>
				<input type="text" name="xml_cpi" style="width: 100%" size="25"
					<%=bNew?"":"datafld='xml_cpi'"%>></td>
			</tr>
		</table>
		</td>
	</tr>
</table>

<%
  GregorianCalendar now=new GregorianCalendar();
%>
<table width="100%" border="0" <%=bNew?"":"datasrc='#xml_list'"%>>
	<tr>
		<td>Signature of attendant<br>
		<input type="text" name="xml_soa" size="30" maxlength="50"
			style="width: 80%"
			<%--=bNewList?"value='"+request.getParameter("username")+"'":"datafld='xml_soa'"--%>
    <%=bNew?"value=''":"datafld='xml_soa'"%>>
		</td>
		<td>Date (yyyy/mm/dd)<br>
		<input type="text" name="xml_date" size="30" maxlength="50"
			style="width: 80%"
			<%=bNewList?"value='"+now.get(Calendar.YEAR)+"/"+(now.get(Calendar.MONTH)+1)+"/"+now.get(Calendar.DAY_OF_MONTH)+"'":"datafld='xml_date'"%>>
		</td>
	</tr>
	<tr bgcolor="#486ebd">
		<td align="center" colspan="2"><input type="hidden"
			name="xml_subject" value="form:AR2"> <input type="hidden"
			name="reason" value="<%=request.getParameter("reason")%>"> <input
			type="hidden" name="appointment_no"
			value="<%=request.getParameter("appointment_no")%>"> <input
			type="hidden" name="demographic_no"
			value="<%=request.getParameter("demographic_no")%>"> <input
			type="hidden" name="form_date"
			value='<%=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)%>'>
		<input type="hidden" name="form_time"
			value='<%=now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND)%>'>
		<input type="hidden" name="user_no" value='<%=user_no%>'> <input
			type="hidden" name="formtype" value='direct'> <input
			type="hidden" name="form_name" value='<%=form_name%>'> <input
			type="hidden" name="dboperation" value="save_form"> <input
			type="hidden" name="displaymode" value="saveform">
		<table width='100%' border=0>
			<tr>
				<td width='90%' align='center'><%=bNewList&&(request.getParameter("patientmaster")!=null)?"<input type='submit' name='savetemp' value=' Save ' onClick='onSave()'> ":""%>
				<%=bNewList&&!(request.getParameter("patientmaster")!=null)?"<input type='submit' name='saveexit' value='Save & Exit' onClick='onSaveExit()'> ":""%>
				<%--=bNewList&&!(request.getParameter("patientmaster")!=null)?"<input type='submit' name='saveexit' value='Save to Enc.& Exit' onClick='onSaveExit()'> ":""--%>
				</td>
				<td align='right'><%=bNewList?"<input type='button' name='Button' value=' Exit ' onClick='onExit();'>":"<input type='button' name='Button' value=' Exit ' onClick='window.close();'>" %>
				</td>
		</table>
		</td>
	</tr>
</table>


<%
if(bNewList) {

Properties savedar1risk = new Properties();
if(finalEDB==null || finalEDB=="") out.println("************No EDB, no check list!**************");
else {
   savedar1risk.setProperty("finalEDB", finalEDB);
   
    Form f = formDao.search_form_no(Integer.parseInt(request.getParameter("demographic_no")), "ar%");	
    String temp = "";
	StringBuffer tt;
    if (f != null) {
      temp = f.getContent();
      Properties savedar1risk1 = risks.getRiskName("../webapps/"+oscarVariables.getProperty("project_home")+"/provider/obarrisks_99_12.xml");
      for (Enumeration e = savedar1risk1.propertyNames() ; e.hasMoreElements() ;) {
        tt = new StringBuffer().append(e.nextElement());
        if(SxmlMisc.getXmlContent(temp, savedar1risk1.getProperty(tt.toString()))!= null) savedar1risk.setProperty(tt.toString(), savedar1risk1.getProperty(tt.toString()));
      }

      //calculate bmi
	  String wt = SxmlMisc.getXmlContent(temp, "<xml_pew>","</xml_pew>");
	  String ht = SxmlMisc.getXmlContent(temp, "<xml_peh>","</xml_peh>");
      if (wt!= null && ht!= null) {
	    boolean bNum = true;
	    for(int ii=0; ii<wt.length();ii++) {
          if (wt.charAt(ii) == '.') { ii++; continue; }
		  if ((wt.charAt(ii) < '0') || (wt.charAt(ii) > '9') ) {
			bNum = false;
			break;
		  }
		}
	    for(int ii=0; ii<ht.length();ii++) {
          if (ht.charAt(ii) == '.') { ii++; continue; }
		  if ((ht.charAt(ii) < '0') || (ht.charAt(ii) > '9') ) {
			bNum = false;
			break; }
		}
	    if(bNum) {
	    if(Float.parseFloat(wt)<=150 && Float.parseFloat(wt)>=40 && Float.parseFloat(ht)<=3 && Float.parseFloat(ht)>=1) {
          float bmi = Float.parseFloat(wt) / Float.parseFloat(ht) * Float.parseFloat(ht);
          if ((bmi > 0) && (bmi < 20)) { savedar1risk.setProperty("97", "xml_pew_peh"); }
          if ((bmi > 19) && (bmi < 26)) { savedar1risk.setProperty("98", "xml_pew_peh"); }
          if (bmi > 25) { savedar1risk.setProperty("99", "xml_pew_peh");  }
		}
		}
      }


      if(SxmlMisc.getXmlContent(temp, "<xml_cp1b>","</xml_cp1b>")!= null) savedar1risk.setProperty("1", "xml_cp1b");
      if(SxmlMisc.getXmlContent(temp, "<xml_cp2v>","</xml_cp2v>")!= null) savedar1risk.setProperty("2", "xml_cp2v");
      if(SxmlMisc.getXmlContent(temp, "<xml_cp3s>","</xml_cp3s>")!= null) savedar1risk.setProperty("3", "xml_cp3s");
      if(SxmlMisc.getXmlContent(temp, "<xml_cp4d>","</xml_cp4d>")!= null) savedar1risk.setProperty("4", "xml_cp4d");
      if(SxmlMisc.getXmlContent(temp, "<xml_cp5a>","</xml_cp5a>")!= null) savedar1risk.setProperty("5", "xml_cp5a");
      if(SxmlMisc.getXmlContent(temp, "<xml_cp6i>","</xml_cp6i>")!= null) savedar1risk.setProperty("6", "xml_cp6i");
      if(SxmlMisc.getXmlContent(temp, "<xml_cp7r>","</xml_cp7r>")!= null) savedar1risk.setProperty("7", "xml_cp7r");
      if(SxmlMisc.getXmlContent(temp, "<xml_cp8o>","</xml_cp8o>")!= null) savedar1risk.setProperty("8", "xml_cp8o");
      if(SxmlMisc.getXmlContent(temp, "<xml_m9hy>","</xml_m9hy>")!= null) savedar1risk.setProperty("9", "xml_m9hy");
      if(SxmlMisc.getXmlContent(temp, "<xml_m10ey>","</xml_m10ey>")!= null) savedar1risk.setProperty("10", "xml_m10ey");

      if(SxmlMisc.getXmlContent(temp, "<xml_m11hy>","</xml_m11hy>")!= null) savedar1risk.setProperty("11", "xml_m11hy");
      if(SxmlMisc.getXmlContent(temp, "<xml_m12ry>","</xml_m12ry>")!= null) savedar1risk.setProperty("12", "xml_m12ry");
      if(SxmlMisc.getXmlContent(temp, "<xml_m13ry>","</xml_m13ry>")!= null) savedar1risk.setProperty("13", "xml_m13ry");
      if(SxmlMisc.getXmlContent(temp, "<xml_m14ly>","</xml_m14ly>")!= null) savedar1risk.setProperty("14", "xml_m14ly");
      if(SxmlMisc.getXmlContent(temp, "<xml_m15ny>","</xml_m15ny>")!= null) savedar1risk.setProperty("15", "xml_m15ny");
      if(SxmlMisc.getXmlContent(temp, "<xml_m16ay>","</xml_m16ay>")!= null) savedar1risk.setProperty("16", "xml_m16ay");
      if(SxmlMisc.getXmlContent(temp, "<xml_m17by>","</xml_m17by>")!= null) savedar1risk.setProperty("17", "xml_m17by");
      if(SxmlMisc.getXmlContent(temp, "<xml_m18gy>","</xml_m18gy>")!= null) savedar1risk.setProperty("18", "xml_m18gy");
      if(SxmlMisc.getXmlContent(temp, "<xml_m19hy>","</xml_m19hy>")!= null) savedar1risk.setProperty("19", "xml_m19hy");
      if(SxmlMisc.getXmlContent(temp, "<xml_m20sy>","</xml_m20sy>")!= null) savedar1risk.setProperty("20", "xml_m20sy");

      if(SxmlMisc.getXmlContent(temp, "<xml_m21ay>","</xml_m21ay>")!= null) savedar1risk.setProperty("21", "xml_m21ay");
      if(SxmlMisc.getXmlContent(temp, "<xml_m22hy>","</xml_m22hy>")!= null) savedar1risk.setProperty("22", "xml_m22hy");
      if(SxmlMisc.getXmlContent(temp, "<xml_m23vy>","</xml_m23vy>")!= null) savedar1risk.setProperty("23", "xml_m23vy");
      if(SxmlMisc.getXmlContent(temp, "<xml_m24py>","</xml_m24py>")!= null) savedar1risk.setProperty("24", "xml_m24py");
      if(SxmlMisc.getXmlContent(temp, "<xml_m25oy>","</xml_m25oy>")!= null) savedar1risk.setProperty("25", "xml_m25oy");
      if(SxmlMisc.getXmlContent(temp, "<xml_g26ay>","</xml_g26ay>")!= null) savedar1risk.setProperty("26", "xml_g26ay");
      if(SxmlMisc.getXmlContent(temp, "<xml_g27ay>","</xml_g27ay>")!= null) savedar1risk.setProperty("27", "xml_g27ay");
      if(SxmlMisc.getXmlContent(temp, "<xml_g28ky>","</xml_g28ky>")!= null) savedar1risk.setProperty("28", "xml_g28ky");
      if(SxmlMisc.getXmlContent(temp, "<xml_g29py>","</xml_g29py>")!= null) savedar1risk.setProperty("29", "xml_g29py");
      if(SxmlMisc.getXmlContent(temp, "<xml_fh30ny>","</xml_fh30ny>")!= null) savedar1risk.setProperty("30", "xml_fh30ny");

      if(SxmlMisc.getXmlContent(temp, "<xml_fh31dy>","</xml_fh31dy>")!= null) savedar1risk.setProperty("31", "xml_fh31dy");
      if(SxmlMisc.getXmlContent(temp, "<xml_fh32cy>","</xml_fh32cy>")!= null) savedar1risk.setProperty("32", "xml_fh32cy");
      if(SxmlMisc.getXmlContent(temp, "<xml_fh33cy>","</xml_fh33cy>")!= null) savedar1risk.setProperty("33", "xml_fh33cy");
      if(SxmlMisc.getXmlContent(temp, "<xml_fh34cy>","</xml_fh34cy>")!= null) savedar1risk.setProperty("34", "xml_fh34cy");
      if(SxmlMisc.getXmlContent(temp, "<xml_fh35gy>","</xml_fh35gy>")!= null) savedar1risk.setProperty("35", "xml_fh35gy");
      if(SxmlMisc.getXmlContent(temp, "<xml_fh36fy>","</xml_fh36fy>")!= null) savedar1risk.setProperty("36", "xml_fh36fy");
      if(SxmlMisc.getXmlContent(temp, "<xml_fh37oy>","</xml_fh37oy>")!= null) savedar1risk.setProperty("37", "xml_fh37oy");
      if(SxmlMisc.getXmlContent(temp, "<xml_fh37ay>","</xml_fh37ay>")!= null) savedar1risk.setProperty("37", "xml_fh37ay");
      if(SxmlMisc.getXmlContent(temp, "<xml_idt38s>","</xml_idt38s>")!= null) savedar1risk.setProperty("38", "xml_idt38s");
      if(SxmlMisc.getXmlContent(temp, "<xml_idt39h>","</xml_idt39h>")!= null) savedar1risk.setProperty("39", "xml_idt39h");
      if(SxmlMisc.getXmlContent(temp, "<xml_idt40v>","</xml_idt40v>")!= null) savedar1risk.setProperty("40", "xml_idt40v");

      if(SxmlMisc.getXmlContent(temp, "<xml_idt41t>","</xml_idt41t>")!= null) savedar1risk.setProperty("41", "xml_idt41t");
      if(SxmlMisc.getXmlContent(temp, "<xml_idt42t>","</xml_idt42t>")!= null) savedar1risk.setProperty("42", "xml_idt42t");
      if(SxmlMisc.getXmlContent(temp, "<xml_pdt43s>","</xml_pdt43s>")!= null) savedar1risk.setProperty("43", "xml_pdt43s");
      if(SxmlMisc.getXmlContent(temp, "<xml_pdt44c>","</xml_pdt44c>")!= null) savedar1risk.setProperty("44", "xml_pdt44c");
      if(SxmlMisc.getXmlContent(temp, "<xml_pdt45e>","</xml_pdt45e>")!= null) savedar1risk.setProperty("45", "xml_pdt45e");
      if(SxmlMisc.getXmlContent(temp, "<xml_pdt46s>","</xml_pdt46s>")!= null) savedar1risk.setProperty("46", "xml_pdt46s");
      if(SxmlMisc.getXmlContent(temp, "<xml_pdt47f>","</xml_pdt47f>")!= null) savedar1risk.setProperty("47", "xml_pdt47f");
      if(SxmlMisc.getXmlContent(temp, "<xml_pdt48p>","</xml_pdt48p>")!= null) savedar1risk.setProperty("48", "xml_pdt48p");

      if(SxmlMisc.getXmlContent(temp, "<xml_cinhe>","</xml_cinhe>")!= null) savedar1risk.setProperty("51", "xml_cinhe");
      if(SxmlMisc.getXmlContent(temp, "<xml_cinth>","</xml_cinth>")!= null) savedar1risk.setProperty("52", "xml_cinth");
      if(SxmlMisc.getXmlContent(temp, "<xml_cinch>","</xml_cinch>")!= null) savedar1risk.setProperty("53", "xml_cinch");
      if(SxmlMisc.getXmlContent(temp, "<xml_cinbr>","</xml_cinbr>")!= null) savedar1risk.setProperty("54", "xml_cinbr");
      if(SxmlMisc.getXmlContent(temp, "<xml_cinca>","</xml_cinca>")!= null) savedar1risk.setProperty("55", "xml_cinca");
      if(SxmlMisc.getXmlContent(temp, "<xml_cinab>","</xml_cinab>")!= null) savedar1risk.setProperty("56", "xml_cinab");
      if(SxmlMisc.getXmlContent(temp, "<xml_cinva>","</xml_cinva>")!= null) savedar1risk.setProperty("57", "xml_cinva");
      if(SxmlMisc.getXmlContent(temp, "<xml_cinne>","</xml_cinne>")!= null) savedar1risk.setProperty("58", "xml_cinne");
      if(SxmlMisc.getXmlContent(temp, "<xml_cinpe>","</xml_cinpe>")!= null) savedar1risk.setProperty("59", "xml_cinpe");
      if(SxmlMisc.getXmlContent(temp, "<xml_cinex>","</xml_cinex>")!= null) savedar1risk.setProperty("60", "xml_cinex");
      if(SxmlMisc.getXmlContent(temp, "<xml_cince>","</xml_cince>")!= null) savedar1risk.setProperty("61", "xml_cince");
      if(SxmlMisc.getXmlContent(temp, "<xml_cinut>","</xml_cinut>")!= null) savedar1risk.setProperty("62", "xml_cinut");
      if(SxmlMisc.getXmlContent(temp, "<xml_cinad>","</xml_cinad>")!= null) savedar1risk.setProperty("63", "xml_cinad");

      if(SxmlMisc.getXmlContent(temp, "<xml_nafa>","</xml_nafa>")!= null) savedar1risk.setProperty("71", "xml_nafa");
      if(SxmlMisc.getXmlContent(temp, "<xml_namp>","</xml_namp>")!= null) savedar1risk.setProperty("72", "xml_namp");
      if(SxmlMisc.getXmlContent(temp, "<xml_nadb>","</xml_nadb>")!= null) savedar1risk.setProperty("73", "xml_nadb");
      if(SxmlMisc.getXmlContent(temp, "<xml_nadr>","</xml_nadr>")!= null) savedar1risk.setProperty("74", "xml_nadr");
      if(SxmlMisc.getXmlContent(temp, "<xml_nadref>","</xml_nadref>")!= null) savedar1risk.setProperty("75", "xml_nadref");
     }
	out.println(checklist.doStuff(new String("../webapps/"+oscarVariables.getProperty("project_home")+"/provider/obarchecklist_99_12.xml"), savedar1risk));
}
}

%>
</form>
</body>
</html>
