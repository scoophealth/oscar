<%--  
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
--%>

<% 
  if(session.getValue("user") == null || !( ((String) session.getValue("userprofession")).equalsIgnoreCase("doctor") ))
    response.sendRedirect("../logout.jsp");
  String user_no = (String) session.getAttribute("user");
%>

<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.*,java.sql.ResultSet, javax.xml.parsers.*, org.w3c.dom.*, oscar.util.*, oscar.oscarEncounter.data.EctARRecord, oscar.oscarEncounter.data.EctFormData" %>
<jsp:useBean id="myDBBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
{"search_msg_attach", "select attachment from messagetbl m, messagelisttbl l where m.messageid = l.message and m.messageid = ? and l.provider_no =" + user_no}, 
  };
  myDBBean.doConfigure(dbParams,dbQueries);
%>

<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");

    String resource = "";
	String messageid = request.getParameter("messageid");
    Properties props = new Properties();

    ResultSet rs = myDBBean.queryResults(messageid, "search_msg_attach");

    Document dc = null;
    while (rs.next()) {
		dc = UtilXML.parseXML(rs.getString(1));
	}
    //System.out.println( UtilXML.getNodeXML(dc, "risk", "name", "risk168") );
    //props = UtilXML.getPropText(dc, "fld","sql" , "value");
    
    props = UtilXML.getPropText(dc);
	myDBBean.closePstmtConn();


if (props.isEmpty()) System.out.println("kkkkkkk");
	for (Enumeration e = props.propertyNames() ; e.hasMoreElements() ;) {
		String t = (String) e.nextElement();
        // System.out.println("kkkkkkk    :" + t + " : " + props.getProperty(t) );
        // System.out.println("g-----------------------------------------------------------------------------------------------------");
    }

%>
<head>
    <title>Antenatal Record 1</title>
    <link rel="stylesheet" type="text/css" href="arStyle.css">
</head>

<script type="text/javascript" language="Javascript">

    function onPrint() {
        document.forms[0].submit.value="print"; //printAR1
        var ret = checkAllDates();
        if(ret==true)
        {
            //ret = confirm("Do you wish to save this form and view the print preview?");
            //popupFixedPage(650,850,'../provider/notice.htm');
            //document.forms[0].action = "formAR1Print.jsp";
            //document.forms[0].target="planner";
            //document.forms[0].submit();
            //document.forms[0].target="apptProviderSearch";
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
    function popupPage(varpage) {
        windowprops = "height=700,width=960"+
            ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=50,screenY=50,top=20,left=20";
        var popup = window.open(varpage, "ar2", windowprops);
        if (popup.opener == null) {
            popup.opener = self;
        }
    }
function popupFixedPage(vheight,vwidth,varpage) { 
  var page = "" + varpage;
  windowprop = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=10,screenY=0,top=0,left=0";
  var popup=window.open(page, "planner", windowprop);
}

</script>

<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1">

<form  name="EctARForm" method="POST" action="formAR1Print.jsp">

<table class="Head" class="hidePrint">
    <tr>
        <td align="left">
            <input type="submit" value="Exit" onclick="javascript:return onExit();"/>
            <input type="submit" value="Print" onclick="javascript:return onPrint();"/>
        </td>
    </tr>
</table>

<table class="title" border="0" cellspacing="0" cellpadding="0" width="100%" >
    <tr>
        <th>ANTENATAL RECORD 1</th>
    </tr>
</table>
<table width="60%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
        <td valign="top" colspan='4'>Name
            <input type="text" name="c_pName" style="width:100%" size="30" maxlength="60" value="<%= props.getProperty("formAR."+"c_pName", "") %>"/>
        </td>
    </tr>
    <tr>
        <td valign="top" colspan='4'>Address
            <input type="text" name="c_address" style="width:100%" size="60" maxlength="80" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"c_address", "")) %>"/>
        </td>
    </tr>
    <tr>
        <td valign="top" width="28%">Date of birth (yyyy/mm/dd)<br>
            <input type="text" name="pg1_dateOfBirth" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("formAR."+"pg1_dateOfBirth", "") %>" readonly="true"/>
        </td>
        <td width="8%">Age<br>
            <input type="text" name="pg1_age" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("formAR."+"pg1_age", "") %>"/>
        </td>
	    <td width="25%" nowrap>Marital status <br>
            <input type="checkbox" name="pg1_msMarried" <%= props.getProperty("formAR."+"pg1_msMarried", "").equals("1")?"checked":"" %>/>M
            <input type="checkbox" name="pg1_msCommonLaw" <%= props.getProperty("formAR."+"pg1_msCommonLaw", "").equals("1")?"checked":"" %>/>CL
            <input type="checkbox" name="pg1_msSingle" <%= props.getProperty("formAR."+"pg1_msSingle", "").equals("1")?"checked":"" %>/>S
        </td>
        <td>Education level <br>
            <input type="text" name="pg1_eduLevel" size="15" style="width:100%" maxlength="25" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_eduLevel", "")) %>"/>
        </td>
    </tr>
</table>
<table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
        <td>Occupation<br>
            <input type="text" name="pg1_occupation" size="15" style="width:100%" maxlength="25" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_occupation", "")) %>"/>
	    </td>
        <td>Language<br>
            <input type="text" name="pg1_language" size="15" style="width:100%" maxlength="25" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_language", "")) %>"/>
        </td>
        <td>Home phone<br>
            <input type="text" name="pg1_homePhone" size="15" style="width:100%" maxlength="20" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_homePhone", "")) %>"/>
        </td>
        <td>Work phone<br>
            <input type="text" name="pg1_workPhone" size="15" style="width:100%" maxlength="20" value="<%= props.getProperty("formAR."+"pg1_workPhone", "") %>"/>
        </td>
        <td>Name of partner<br>
            <input type="text" name="pg1_partnerName" size="15" style="width:100%" maxlength="50" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_partnerName", "")) %>"/>
        </td>
        <td>Age<br>
            <input type="text" name="pg1_partnerAge" size="2" style="width:100%" maxlength="2" value="<%= props.getProperty("formAR."+"pg1_partnerAge", "") %>"/>
        </td>
        <td valign="top">Occupation<br>
            <input type="text" name="pg1_partnerOccupation" size="15" style="width:100%" maxlength="25" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_partnerOccupation", "")) %>"/>
        </td>
    </tr>
</table>
<table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
        <td nowrap>Birth attendants<br>
            <input type="checkbox" name="pg1_baObs" <%= props.getProperty("formAR."+"pg1_baObs", "").equals("1")?"checked":"" %>/>OBS
            <input type="checkbox" name="pg1_baFP"  <%= props.getProperty("formAR."+"pg1_baFP", "").equals("1")?"checked":"" %> />FP
            <input type="checkbox" name="pg1_baMidwife" <%= props.getProperty("formAR."+"pg1_baMidwife", "").equals("1")?"checked":"" %>/>Midwife<br>
            <input type="text" name="c_ba" size="15" style="width:100%" maxlength="25" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"c_ba", "")) %>"/>
	    </td>
        <td nowrap valign="top">Family physician<br>
            <div align="center"><textarea name="pg1_famPhys" style="width:100%" cols="30" rows="2"><%= props.getProperty("formAR."+"pg1_famPhys", "") %></textarea></div>
        </td>
        <td nowrap>Newborn care<br>
            <input type="checkbox" name="pg1_ncPed" <%= props.getProperty("formAR."+"pg1_ncPed", "").equals("1")?"checked":"" %>/>Ped.
            <input type="checkbox" name="pg1_ncFP"  <%= props.getProperty("formAR."+"pg1_ncFP", "").equals("1")?"checked":"" %>/>FP
            <input type="checkbox" name="pg1_ncMidwife" <%= props.getProperty("formAR."+"pg1_ncMidwife", "").equals("1")?"checked":"" %>/>Midwife<br>
            <input type="text" name="c_nc" size="15" style="width:100%" maxlength="25" value="<%= props.getProperty("formAR."+"c_nc", "") %>"/>
	    </td>
        <td nowrap valign="top">Ethnic background of mother/father<br>
            <div align="center"><textarea name="pg1_ethnicBg" style="width:100%" cols="30" rows="2"><%= props.getProperty("formAR."+"pg1_ethnicBg", "") %></textarea></div>
	    </td>
    </tr>
</table>
<table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
        <td width="12%">
            <input type="checkbox" name="pg1_vbac" <%= props.getProperty("formAR."+"pg1_vbac", "").equals("1")?"checked":"" %>/>VBAC<br>
            <input type="checkbox" name="pg1_repeatCS" <%= props.getProperty("formAR."+"pg1_repeatCS", "").equals("1")?"checked":"" %>/>Repeat CS<br>
        </td>
        <td width="44%">Allergies(list):<br>
            <div align="center"><textarea name="c_allergies" style="width:100%" cols="30" rows="2"><%= props.getProperty("formAR."+"c_allergies", "") %></textarea></div>
        </td>
        <td width="44%" >Medications (list)<br>
            <div align="center"><textarea name="c_meds" style="width:100%" cols="30" rows="2"><%= props.getProperty("formAR."+"c_meds", "") %></textarea></div>
        </td>
    </tr>
</table>
<table width="100%" border="0">
    <tr bgcolor="#99FF99">
        <td valign="top" bgcolor="#CCCCCC"><div align="center"><b>Pregnancy Summary</b></div></td>
    </tr>
</table>
<table width="100%" border="1"  cellspacing="0" cellpadding="0">
    <tr>
        <td valign="top" nowrap>Menstrual history (LMP):
            <input type="text" name="pg1_menLMP" size="10" maxlength="10" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_menLMP", "")) %>"/>&nbsp; &nbsp; &nbsp; Cycle
            <input type="text" name="pg1_menCycle" size="7" maxlength="7" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_menCycle", "")) %>"/>&nbsp; &nbsp; &nbsp;
            <input type="checkbox" name="pg1_menReg" <%= props.getProperty("formAR."+"pg1_menReg", "").equals("1")?"checked":"" %>/>Regular &nbsp; &nbsp; &nbsp; EDB
            <input type="text" name="pg1_menEDB" size="10" maxlength="10" value="<%= props.getProperty("formAR."+"pg1_menEDB", "") %>"/><br>
            Contraception:<br>
            <input type="checkbox" name="pg1_iud" <%= props.getProperty("formAR."+"pg1_iud", "").equals("1")?"checked":"" %>/>IUD
            <input type="checkbox" name="pg1_hormone" <%= props.getProperty("formAR."+"pg1_hormone", "").equals("1")?"checked":"" %>/>Hormonal(type)
            <input type="text" name = "hormoneType" size="15" maxlength="25" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_hormoneType", "")) %>"/>
            <input type="checkbox" name="pg1_otherAR1" <%= props.getProperty("formAR."+"pg1_otherAR1", "").equals("1")?"checked":"" %>/>Other
            <input type="text" name="pg1_otherAR1Name" size="15" maxlength="25" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_otherAR1Name", "")) %>"/>
            Last used<input type="text" name="pg1_lastUsed" size="10" maxlength="10" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_lastUsed", "")) %>"/>
        </td>
        <td valign="top" width="25%"> <font size="+1"><b>Final EDB</font></b></font>
            (yyyy/mm/dd) <br>
            <input type="text" name="c_finalEDB" style="width:100%" size="10" maxlength="10" value="<%= props.getProperty("formAR."+"c_finalEDB", "") %>"/>
        </td>
    </tr>
</table>
<table width="100%" border="1"  cellspacing="0" cellpadding="0">
	<tr>
        <td>Gravida<br>
            <input type="text" name="c_gravida" size="5" style="width:100%" maxlength="10" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"c_gravida", "")) %>"/>
	    </td>
        <td>Term<br>
            <input type="text" name="c_term" size="5" style="width:100%" maxlength="10" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"c_term", "")) %>"/>
	    </td>
        <td>Prem<br>
            <input type="text" name="c_prem" size="5" style="width:100%" maxlength="8" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"c_prem", "")) %>"/>
	    </td>
        <td valign="top" nowrap>
            No. of pregnancy loss(es)<br>&nbsp; &nbsp;
            <input type="checkbox" name="pg1_ectopic" <%= props.getProperty("formAR."+"pg1_ectopic", "").equals("1")?"checked":"" %>/>Ectopic
            <input type="text" name="pg1_ectopicBox" size="2" maxlength="2" value="<%= props.getProperty("formAR."+"pg1_ectopicBox", "") %>" />&nbsp; &nbsp;
            <input type="checkbox" name="pg1_termination" <%= props.getProperty("formAR."+"pg1_termination", "").equals("1")?"checked":"" %> />Termination
            <input type="text" name="pg1_terminationBox" size="2" maxlength="2" value="<%= props.getProperty("formAR."+"pg1_terminationBox", "") %>" />&nbsp; &nbsp;
            <input type="checkbox" name="pg1_spontaneous" <%= props.getProperty("formAR."+"pg1_spontaneous", "").equals("1")?"checked":"" %> />Spontaneous
            <input type="text" name="pg1_spontaneousBox" size="2" maxlength="2" value="<%= props.getProperty("formAR."+"pg1_spontaneousBox", "") %>" />&nbsp; &nbsp;
            <input type="checkbox" name="pg1_stillborn" <%= props.getProperty("formAR."+"pg1_stillborn", "").equals("1")?"checked":"" %> />Stillborn
            <input type="text" name="pg1_stillbornBox" size="2" maxlength="2" value="<%= props.getProperty("formAR."+"pg1_stillbornBox", "") %>" />
        </td>
        <td>Living<br>
            <input type="text" name="c_living" size="5" style="width:100%" maxlength="10" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"c_living", "")) %>"/>
	    </td>
        <td nowrap>Multipregnancy<br>
            No.<input type="text" name="pg1_multi" size="5" style="width:100%" maxlength="10" value="<%= props.getProperty("formAR."+"pg1_multi", "") %>"/>
	    </td>
    </tr>
</table>
<table width="100%" border="0">
    <tr bgcolor="#99FF99">
        <td align="center" colspan="2" bgcolor="#CCCCCC"><b>Obstetrical History</b></td>
    </tr>
    <tr>
        <td   valign="top">
            <table width="100%" border="1" cellspacing="0" cellpadding="0">
                <tr align="center">
                    <td width="40">No.</td>
                    <td width="50">Year</td>
                    <td width="40">Sex<br>M/F</td>
                    <td width="70">Gest. age<br>(weeks) </td>
                    <td width="70">Birth<br>weight </td>
                    <td width="70">Length<br>of labour</td>
                    <td width="120">Place<br>of birth</td>
                    <td width="100">Type of birth<br>SVB CS Ass'd</td>
                    <td nowrap>Comments regarding pregnancy and birth</td>
                </tr>
                <tr align="center">
                    <td>1</td>
                    <td><input type="text" name="pg1_year1" size="5" maxlength="8" style="width:90%" value="<%= props.getProperty("formAR."+"pg1_year1", "") %>"/></td>
                    <td><input type="text" name="pg1_sex1" size="1" maxlength="1" style="width:50%" value="<%= props.getProperty("formAR."+"pg1_sex1", "") %>"/></td>
                    <td><input type="text" name="pg1_oh_gest1" size="3" maxlength="5" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_oh_gest1", "")) %>"/></td>
                    <td><input type="text" name="pg1_weight1" size="5" maxlength="6" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_weight1", "")) %>"/></td>
                    <td><input type="text" name="pg1_length1" size="5" maxlength="6" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_length1", "")) %>"/></td>
                    <td><input type="text" name="pg1_place1" size="8" maxlength="20" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_place1", "")) %>"/></td>
                    <td>
                        <input type="checkbox" name="pg1_svb1" <%= props.getProperty("formAR."+"pg1_svb1", "").equals("1")?"checked":"" %>/>
                        <input type="checkbox" name="pg1_cs1" <%= props.getProperty("formAR."+"pg1_cs1", "").equals("1")?"checked":"" %>/>
                        <input type="checkbox" name="pg1_ass1" <%= props.getProperty("formAR."+"pg1_ass1", "").equals("1")?"checked":"" %>/>
                    </td>
                    <td align="left"><input type="text" name="pg1_oh_comments1" size="20" maxlength="80" style="width:100%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_oh_comments1", "")) %>"/></td>
                </tr>
                <tr align="center">
                    <td>2</td>
                    <td><input type="text" name="pg1_year2" size="5" maxlength="8" style="width:90%" value="<%= props.getProperty("formAR."+"pg1_year2", "") %>"/></td>
                    <td><input type="text" name="pg1_sex2" size="1" maxlength="1" style="width:50%" value="<%= props.getProperty("formAR."+"pg1_sex2", "") %>"/></td>
                    <td><input type="text" name="pg1_oh_gest2" size="3" maxlength="5" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_oh_gest2", "")) %>"/></td>
                    <td><input type="text" name="pg1_weight2" size="5" maxlength="6" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_weight2", "")) %>"/></td>
                    <td><input type="text" name="pg1_length2" size="5" maxlength="6" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_length2", "")) %>"/></td>
                    <td><input type="text" name="pg1_place2" size="8" maxlength="20" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_place2", "")) %>"/></td>
                    <td>
                        <input type="checkbox" name="pg1_svb2" <%= props.getProperty("formAR."+"pg1_svb2", "").equals("1")?"checked":"" %>/>
                        <input type="checkbox" name="pg1_cs2" <%= props.getProperty("formAR."+"pg1_cs2", "").equals("1")?"checked":"" %>/>
                        <input type="checkbox" name="pg1_ass2" <%= props.getProperty("formAR."+"pg1_ass2", "").equals("1")?"checked":"" %>/>
                    </td>
                    <td align="left"><input type="text" name="pg1_oh_comments2"size="20" maxlength="80" style="width:100%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_oh_comments2", "")) %>"/></td>
                </tr>
                <tr align="center">
                    <td>3</td>
                    <td><input type="text" name="pg1_year3" size="5" maxlength="8" style="width:90%" value="<%= props.getProperty("formAR."+"pg1_year3", "") %>"/></td>
                    <td><input type="text" name="pg1_sex3" size="1" maxlength="1" style="width:50%" value="<%= props.getProperty("formAR."+"pg1_sex3", "") %>"/></td>
                    <td><input type="text" name="pg1_oh_gest3" size="3" maxlength="5" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_oh_gest3", "")) %>"/></td>
                    <td><input type="text" name="pg1_weight3" size="5" maxlength="6" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_weight3", "")) %>"/></td>
                    <td><input type="text" name="pg1_length3" size="5" maxlength="6" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_length3", "")) %>"/></td>
                    <td><input type="text" name="pg1_place3" size="8" maxlength="20" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_place3", "")) %>"/></td>
                    <td>
                        <input type="checkbox" name="pg1_svb3" <%= props.getProperty("formAR."+"pg1_svb3", "").equals("1")?"checked":"" %> />
                        <input type="checkbox" name="pg1_cs3" <%= props.getProperty("formAR."+"pg1_cs3", "").equals("1")?"checked":"" %> />
                        <input type="checkbox" name="pg1_ass3" <%= props.getProperty("formAR."+"pg1_ass3", "").equals("1")?"checked":"" %> />
                    </td>
                    <td align="left"><input type="text" name="pg1_oh_comments3" size="20" maxlength="80" style="width:100%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_oh_comments3", "")) %>"/></td>
                </tr>
                <tr align="center">
                    <td>4</td>
                    <td><input type="text" name="pg1_year4" size="5" maxlength="8" style="width:90%" value="<%= props.getProperty("formAR."+"pg1_year4", "") %>"/></td>
                    <td><input type="text" name="pg1_sex4" size="1" maxlength="1" style="width:50%" value="<%= props.getProperty("formAR."+"pg1_sex4", "") %>"/></td>
                    <td><input type="text" name="pg1_oh_gest4" size="3" maxlength="5" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_oh_gest4", "")) %>"/></td>
                    <td><input type="text" name="pg1_weight4" size="5" maxlength="6" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_weight4", "")) %>"/></td>
                    <td><input type="text" name="pg1_length4" size="5" maxlength="6" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_length4", "")) %>"/></td>
                    <td><input type="text" name="pg1_place4" size="8" maxlength="20" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_place4", "")) %>"/></td>
                    <td>
                        <input type="checkbox" name="pg1_svb4" <%= props.getProperty("formAR."+"pg1_svb4", "").equals("1")?"checked":"" %> />
                        <input type="checkbox" name="pg1_cs4" <%= props.getProperty("formAR."+"pg1_cs4", "").equals("1")?"checked":"" %> />
                        <input type="checkbox" name="pg1_ass4" <%= props.getProperty("formAR."+"pg1_ass4", "").equals("1")?"checked":"" %> />
                    </td>
                    <td align="left"><input type="text" name="pg1_oh_comments4"size="20" maxlength="80" style="width:100%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_oh_comments4", "")) %>"/></td>
                </tr>
                <tr align="center">
                    <td>5</td>
                    <td><input type="text" name="pg1_year5" size="5" maxlength="8" style="width:90%" value="<%= props.getProperty("formAR."+"pg1_year5", "") %>"/></td>
                    <td><input type="text" name="pg1_sex5" size="1" maxlength="1" style="width:50%" value="<%= props.getProperty("formAR."+"pg1_sex5", "") %>"/></td>
                    <td><input type="text" name="pg1_oh_gest5" size="3" maxlength="5" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_oh_gest5", "")) %>"/></td>
                    <td><input type="text" name="pg1_weight5" size="5" maxlength="6" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_weight5", "")) %>"/></td>
                    <td><input type="text" name="pg1_length5" size="5" maxlength="6" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_length5", "")) %>"/></td>
                    <td><input type="text" name="pg1_place5" size="8" maxlength="20" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_place5", "")) %>"/></td>
                    <td>
                        <input type="checkbox" name="pg1_svb5" <%= props.getProperty("formAR."+"pg1_svb5", "").equals("1")?"checked":"" %> />
                        <input type="checkbox" name="pg1_cs5" <%= props.getProperty("formAR."+"pg1_cs5", "").equals("1")?"checked":"" %> />
                        <input type="checkbox" name="pg1_ass5" <%= props.getProperty("formAR."+"pg1_ass5", "").equals("1")?"checked":"" %> />
                    </td>
                    <td align="left"><input type="text" name="pg1_oh_comments5" size="20" maxlength="80" style="width:100%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_oh_comments5", "")) %>"/></td>
                </tr>
                <tr align="center">
                    <td>6</td>
                    <td><input type="text" name="pg1_year6" size="5" maxlength="8" style="width:90%" value="<%= props.getProperty("formAR."+"pg1_year6", "") %>"/></td>
                    <td><input type="text" name="pg1_sex6" size="1" maxlength="1" style="width:50%" value="<%= props.getProperty("formAR."+"pg1_sex6", "") %>"/></td>
                    <td><input type="text" name="pg1_oh_gest6" size="3" maxlength="5" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_oh_gest6", "")) %>"/></td>
                    <td><input type="text" name="pg1_weight6" size="5" maxlength="6" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_weight6", "")) %>"/></td>
                    <td><input type="text" name="pg1_length6" size="5" maxlength="6" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_length6", "")) %>"/></td>
                    <td><input type="text" name="pg1_place6" size="8" maxlength="20" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_place6", "")) %>"/></td>
                    <td>
                        <input type="checkbox" name="pg1_svb6" <%= props.getProperty("formAR."+"pg1_svb6", "").equals("1")?"checked":"" %> />
                        <input type="checkbox" name="pg1_cs6" <%= props.getProperty("formAR."+"pg1_cs6", "").equals("1")?"checked":"" %> />
                        <input type="checkbox" name="pg1_ass6" <%= props.getProperty("formAR."+"pg1_ass6", "").equals("1")?"checked":"" %> />
                    </td>
                    <td align="left"><input type="text" name="pg1_oh_comments6" size="20" maxlength="80" style="width:100%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_oh_comments6", "")) %>"/></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0" >
    <tr>
        <td align="center" bgcolor="#CCCCCC" ><b><font face="Verdana, Arial, Helvetica, sans-serif">
            Medical History and Physical Examination</font></b></td>
    </tr>
</table>
<table class="shrinkMe" width="100%" border="1" cellspacing="0" cellpadding="0">
    <tr>
        <td align="center">
            <b>Current Pregnancy</b>
        </td>
        <td align="center">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td align="center" nowrap> <b>Medical</b> </td>
                    <td align="center" width="15%"><div align="right">Yes</div></td>
                    <td align="center" nowrap width="15%"><div align="right">No</div></td>
                </tr>
            </table>
        </td>
        <td>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td nowrap align="center"> <b>Genetic/Family</b> </td>
                    <td align="center" width="15%"><div align="right">Yes</div></td>
                    <td align="center" width="15%"><div align="right">No</div></td>
                </tr>
            </table>
        </td>
        <td align="center"><b>Infection Discussion Topics</b></td>
        <td align="center"><b>Physical examination</b></td>
    </tr>
    <tr>
        <td valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td colspan="3"><i>(check if positive)</i></td>
                </tr>
		        <tr>
                    <td>1.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c1p1");return false;'>Bleeding</a></td>
                    <td width="15%"><input type="checkbox" name="pg1_cp1" <%= props.getProperty("formAR."+"pg1_cp1", "").equals("1")?"checked":"" %> /></td>
                </tr>
                <tr>
                    <td>2.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c2p1");return false;'>Vomiting</a></td>
                    <td width="15%"><input type="checkbox" name="pg1_cp2" <%= props.getProperty("formAR."+"pg1_cp2", "").equals("1")?"checked":"" %>/></td>
                </tr>
                <tr>
                    <td valign="top">3.</td>
                    <td nowrap><a href=# onClick='popupPage("<%=resource%>c3p1");return false;'>Smoking</a><br>
                        <font size=1>cig/day
                        <input type="text" name="pg1_box3" size="2" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_box3", "")) %>"></font>
                    </td>
                    <td valign="bottom"><input type="checkbox" name="pg1_cp3" <%= props.getProperty("formAR."+"pg1_cp3", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>4.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c4p1");return false;'>Drugs</a></td>
                    <td width="15%"><input type="checkbox" name="pg1_cp4" <%= props.getProperty("formAR."+"pg1_cp4", "").equals("1")?"checked":"" %>/></td>
                </tr>
                <tr>
                    <td valign="top">5.</td>
                    <td nowrap><a href=# onClick='popupPage("<%=resource%>c5p1");return false;'>Alcohol</a><br>
                        <font size=1>drinks/day
                        <input type="text" name="pg1_box5" size="2" maxlength="3" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_box5", "")) %>"></font>
                    </td>
                    <td valign="bottom"><input type="checkbox" name="pg1_cp5" <%= props.getProperty("formAR."+"pg1_cp5", "").equals("1")?"checked":"" %>/></td>
                </tr>
                <tr>
                    <td>6.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c6p1");return false;'>Infertility</a></td>
                    <td width="15%"><input type="checkbox" name="pg1_cp6" <%= props.getProperty("formAR."+"pg1_cp6", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>7.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c7p1");return false;'>Radiation</a></td>
                    <td width="15%"><input type="checkbox" name="pg1_cp7" <%= props.getProperty("formAR."+"pg1_cp7", "").equals("1")?"checked":"" %>/></td>
                </tr>
                <tr>
                    <td valign="top">8.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c8p1");return false;'>Occup./Env.<br>hazards</a></td>
                    <td width="15%"><input type="checkbox" name="pg1_cp8" <%= props.getProperty("formAR."+"pg1_cp8", "").equals("1")?"checked":"" %>/></td>
                </tr>
                <tr>
                    <td colspan="3" nowrap>
                        <hr><b> Nutrition Assessment</b></td>
                </tr>
                <tr>
                    <td colspan="3"><i>(check if positive)</i></td>
                </tr>
                <tr>
                    <td colspan="2">Folic acid/vitamins</td>
                    <td><input type="checkbox" name="pg1_naFolic" <%= props.getProperty("formAR."+"pg1_naFolic", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td colspan="2">Milk products</td>
                    <td><input type="checkbox" name="pg1_naMilk" <%= props.getProperty("formAR."+"pg1_naMilk", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td colspan="2">Diet</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td colspan="2">&nbsp; &nbsp; &nbsp;Balanced</td>
                    <td><input type="checkbox" name="pg1_naDietBal" <%= props.getProperty("formAR."+"pg1_naDietBal", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td colspan="2">&nbsp; &nbsp; &nbsp;Restricted</td>
                    <td><input type="checkbox" name="pg1_naDietRes" <%= props.getProperty("formAR."+"pg1_naDietRes", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td colspan="2">Dietitian referral</td>
                    <td><input type="checkbox" name="pg1_naRef" <%= props.getProperty("formAR."+"pg1_naRef", "").equals("1")?"checked":"" %>></td>
                </tr>
            </table>
        </td>
        <td valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>9.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c9p1");return false;'>Hypertension</a></td>
                    <td><input type="checkbox" name="pg1_yes9" <%= props.getProperty("formAR."+"pg1_yes9", "").equals("1")?"checked":"" %>></td>
                    <td><input type="checkbox" name="pg1_no9" <%= props.getProperty("formAR."+"pg1_no9", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>10.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c10p1");return false;'>Endocrine/Diabetes</a></td>
                    <td><input type="checkbox" name="pg1_yes10" <%= props.getProperty("formAR."+"pg1_yes10", "").equals("1")?"checked":"" %>></td>
                    <td><input type="checkbox" name="pg1_no10" <%= props.getProperty("formAR."+"pg1_no10", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>11.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c11p1");return false;'>Heart</a></td>
                    <td><input type="checkbox" name="pg1_yes11" <%= props.getProperty("formAR."+"pg1_yes11", "").equals("1")?"checked":"" %>></td>
                    <td><input type="checkbox" name="pg1_no11" <%= props.getProperty("formAR."+"pg1_no11", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>12.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c12p1");return false;'>Renal/urinary tract</a></td>
                    <td><input type="checkbox" name="pg1_yes12" <%= props.getProperty("formAR."+"pg1_yes12", "").equals("1")?"checked":"" %>></td>
                    <td><input type="checkbox" name="pg1_no12" <%= props.getProperty("formAR."+"pg1_no12", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>13.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c13p1");return false;'>Respiratory</a></td>
                    <td><input type="checkbox" name="pg1_yes13" <%= props.getProperty("formAR."+"pg1_yes13", "").equals("1")?"checked":"" %>></td>
                    <td><input type="checkbox" name="pg1_no13" <%= props.getProperty("formAR."+"pg1_no13", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>14.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c14p1");return false;'>Liver/Hepatitis/GI</a></td>
                    <td><input type="checkbox" name="pg1_yes14" <%= props.getProperty("formAR."+"pg1_yes14", "").equals("1")?"checked":"" %>></td>
                    <td><input type="checkbox" name="pg1_no14" <%= props.getProperty("formAR."+"pg1_no14", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>15.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c15p1");return false;'>Neurological</a></td>
                    <td><input type="checkbox" name="pg1_yes15" <%= props.getProperty("formAR."+"pg1_yes15", "").equals("1")?"checked":"" %>></td>
                    <td><input type="checkbox" name="pg1_no15" <%= props.getProperty("formAR."+"pg1_no15", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>16.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c16p1");return false;'>Autoimmune</a></td>
                    <td><input type="checkbox" name="pg1_yes16" <%= props.getProperty("formAR."+"pg1_yes16", "").equals("1")?"checked":"" %>></td>
                    <td><input type="checkbox" name="pg1_no16" <%= props.getProperty("formAR."+"pg1_no16", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>17.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c17p1");return false;'>Breast</a></td>
                    <td><input type="checkbox" name="pg1_yes17" <%= props.getProperty("formAR."+"pg1_yes17", "").equals("1")?"checked":"" %>></td>
                    <td><input type="checkbox" name="pg1_no17" <%= props.getProperty("formAR."+"pg1_no17", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>18.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c18p1");return false;'>Gyn/PAP</a></td>
                    <td><input type="checkbox" name="pg1_yes18" <%= props.getProperty("formAR."+"pg1_yes18", "").equals("1")?"checked":"" %>></td>
                    <td><input type="checkbox" name="pg1_no18" <%= props.getProperty("formAR."+"pg1_no18", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>19.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c19p1");return false;'>Hospitalizations</a></td>
                    <td><input type="checkbox" name="pg1_yes19" <%= props.getProperty("formAR."+"pg1_yes19", "").equals("1")?"checked":"" %>></td>
                    <td><input type="checkbox" name="pg1_no19" <%= props.getProperty("formAR."+"pg1_no19", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>20.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c20p1");return false;'>Surgeries</a></td>
                    <td><input type="checkbox" name="pg1_yes20" <%= props.getProperty("formAR."+"pg1_yes20", "").equals("1")?"checked":"" %>></td>
                    <td><input type="checkbox" name="pg1_no20" <%= props.getProperty("formAR."+"pg1_no20", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>21.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c21p1");return false;'>Anesthetics</a></td>
                    <td><input type="checkbox" name="pg1_yes21" <%= props.getProperty("formAR."+"pg1_yes21", "").equals("1")?"checked":"" %>></td>
                    <td><input type="checkbox" name="pg1_no21" <%= props.getProperty("formAR."+"pg1_no21", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>22.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c22p1");return false;'>Hem./Transfusions</a></td>
                    <td><input type="checkbox" name="pg1_yes22" <%= props.getProperty("formAR."+"pg1_yes22", "").equals("1")?"checked":"" %>></td>
                    <td><input type="checkbox" name="pg1_no22" <%= props.getProperty("formAR."+"pg1_no22", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>23.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c23p1");return false;'>Varicosities/Phlebitis</a></td>
                    <td><input type="checkbox" name="pg1_yes23" <%= props.getProperty("formAR."+"pg1_yes23", "").equals("1")?"checked":"" %>></td>
                    <td><input type="checkbox" name="pg1_no23" <%= props.getProperty("formAR."+"pg1_no23", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>24.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c24p1");return false;'>Psychiatric illness</a></td>
                    <td><input type="checkbox" name="pg1_yes24" <%= props.getProperty("formAR."+"pg1_yes24", "").equals("1")?"checked":"" %>></td>
                    <td><input type="checkbox" name="pg1_no24" <%= props.getProperty("formAR."+"pg1_no24", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>25.</td>
                    <td>Other</td>
                    <td><input type="checkbox" name="pg1_yes25" <%= props.getProperty("formAR."+"pg1_yes25", "").equals("1")?"checked":"" %>></td>
                    <td><input type="checkbox" name="pg1_no25" <%= props.getProperty("formAR."+"pg1_no25", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><input type="text" name="pg1_box25" size="15" maxlength="25" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_box25", "")) %>"></td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
            </table>
        </td>
        <td valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>26.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c26p1");return false;'>Age&gt;=35 at EDB</a></td>
                    <td width="15%" align="center"><input type="checkbox" name="pg1_yes26" <%= props.getProperty("formAR."+"pg1_yes26", "").equals("1")?"checked":"" %>></td>
                    <td width="15%" align="center"><input type="checkbox" name="pg1_no26" <%= props.getProperty("formAR."+"pg1_no26", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td valign="top">27.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c27p1");return false;'>&quot;At risk&quot; population<br>
                        <span class="small">(Tay-Sach's, sicke cell,<br>thalassemia, etc.)</span></a></td>
                    <td align="center" valign="top"><input type="checkbox" name="pg1_yes27" <%= props.getProperty("formAR."+"pg1_yes27", "").equals("1")?"checked":"" %>></td>
                    <td align="center" valign="top"><input type="checkbox" name="pg1_no27" <%= props.getProperty("formAR."+"pg1_no27", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td valign="top">28.</td>
                    <td nowrap><a href=# onClick='popupPage("<%=resource%>c28p1");return false;'>Known teratogen exposure<br>
                        <span class="small">(includes maternal diabetes)</span></a></td>
                    <td align="center"><input type="checkbox" name="pg1_yes28" <%= props.getProperty("formAR."+"pg1_yes28", "").equals("1")?"checked":"" %>></td>
                    <td align="center"><input type="checkbox" name="pg1_no28" <%= props.getProperty("formAR."+"pg1_no28", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>29.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c29p1");return false;'>Previous birth defect</a></td>
                    <td align="center"><input type="checkbox" name="pg1_yes29" <%= props.getProperty("formAR."+"pg1_yes29", "").equals("1")?"checked":"" %>></td>
                    <td align="center"><input type="checkbox" name="pg1_no29" <%= props.getProperty("formAR."+"pg1_no29", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td colspan="4"><b>Family history of:</b></td>
                </tr>
                <tr>
                    <td>30.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c30p1");return false;'>Neural tube defects</a></td>
                    <td align="center"><input type="checkbox" name="pg1_yes30" <%= props.getProperty("formAR."+"pg1_yes30", "").equals("1")?"checked":"" %>></td>
                    <td align="center"><input type="checkbox" name="pg1_no30" <%= props.getProperty("formAR."+"pg1_no30", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>31.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c31p1");return false;'>Development delay</a></td>
                    <td align="center"><input type="checkbox" name="pg1_yes31" <%= props.getProperty("formAR."+"pg1_yes31", "").equals("1")?"checked":"" %>></td>
                    <td align="center"><input type="checkbox" name="pg1_no31" <%= props.getProperty("formAR."+"pg1_no31", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td valign="top">32.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c32p1");return false;'>Congenital physical<br>anomalies (includes<br>congenital heart disease)</a></td>
                    <td align="center"><input type="checkbox" name="pg1_yes32" <%= props.getProperty("formAR."+"pg1_yes32", "").equals("1")?"checked":"" %>></td>
                    <td align="center"><input type="checkbox" name="pg1_no32" <%= props.getProperty("formAR."+"pg1_no32", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>33.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c33p1");return false;'>Congenital hypotonias</a></td>
                    <td align="center"><input type="checkbox" name="pg1_yes33" <%= props.getProperty("formAR."+"pg1_yes33", "").equals("1")?"checked":"" %>></td>
                    <td align="center"><input type="checkbox" name="pg1_no33" <%= props.getProperty("formAR."+"pg1_no33", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td valign="top">34.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c34p1");return false;'>Chromosomal disease<br><span class="small">(Down's, Turner's, etc.) </span></a></td>
                    <td align="center"><input type="checkbox" name="pg1_yes34" <%= props.getProperty("formAR."+"pg1_yes34", "").equals("1")?"checked":"" %>></td>
                    <td align="center"><input type="checkbox" name="pg1_no34" <%= props.getProperty("formAR."+"pg1_no34", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td valign="top">35.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c35p1");return false;'>Genetic disease<br><span class="small">(cystic fibrosis, muscular<br>dystrophy, etc.)</span></a></td>
                    <td align="center" valign="top"><input type="checkbox" name="pg1_yes35" <%= props.getProperty("formAR."+"pg1_yes35", "").equals("1")?"checked":"" %>></td>
                    <td align="center" valign="top"><input type="checkbox" name="pg1_no35" <%= props.getProperty("formAR."+"pg1_no35", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>36.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c36p1");return false;'>Further investigations</a></td>
                    <td align="center"><input type="checkbox" name="pg1_yes36" <%= props.getProperty("formAR."+"pg1_yes36", "").equals("1")?"checked":"" %>></td>
                    <td align="center"><input type="checkbox" name="pg1_no36" <%= props.getProperty("formAR."+"pg1_no36", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>37.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c37p1");return false;'>MSS</a><br></td>
                    <td align="center">&nbsp;</td>
                    <td align="center">&nbsp;</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>Offered</td>
                    <td align="center"><input type="checkbox" name="pg1_yes37off" <%= props.getProperty("formAR."+"pg1_yes37off", "").equals("1")?"checked":"" %>></td>
                    <td align="center"><input type="checkbox" name="pg1_no37off" <%= props.getProperty("formAR."+"pg1_no37off", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>Accepted </td>
                    <td align="center"><input type="checkbox" name="pg1_yes37acc" <%= props.getProperty("formAR."+"pg1_yes37acc", "").equals("1")?"checked":"" %>></td>
                    <td align="center"><input type="checkbox" name="pg1_no37acc" <%= props.getProperty("formAR."+"pg1_no37acc", "").equals("1")?"checked":"" %>></td>
                </tr>
            </table>
        </td>
        <td valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>38.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c38p1");return false;'>STDs/Herpes</a></td>
                    <td><input type="checkbox" name="pg1_idt38" <%= props.getProperty("formAR."+"pg1_idt38", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>39.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c39p1");return false;'>HIV</a></td>
                    <td><input type="checkbox" name="pg1_idt39" <%= props.getProperty("formAR."+"pg1_idt39", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>40.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c40p1");return false;'>Varicella</a></td>
                    <td><input type="checkbox" name="pg1_idt40" <%= props.getProperty("formAR."+"pg1_idt40", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>41.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c41p1");return false;'>Toxo/CMV/Parvo</a></td>
                    <td><input type="checkbox" name="pg1_idt41" <%= props.getProperty("formAR."+"pg1_idt41", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>42.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c42p1");return false;'>TB/Other</a>
                        <input type="text" name="pg1_box42" size="10" maxlength="20" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_box42", "")) %>"></td>
                    <td><input type="checkbox" name="pg1_idt42" <%= props.getProperty("formAR."+"pg1_idt42", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td colspan="3"><hr><b>Psychosocial discussion topics</b></td>
                </tr>
                <tr>
                    <td>43.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c43p1");return false;'>Social support</a></td>
                    <td><input type="checkbox" name="pg1_pdt43" <%= props.getProperty("formAR."+"pg1_pdt43", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>44.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c44p1");return false;'>Couple's relationship</a></td>
                    <td><input type="checkbox" name="pg1_pdt44" <%= props.getProperty("formAR."+"pg1_pdt44", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>45.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c45p1");return false;'>Emotional/Depression</a></td>
                    <td><input type="checkbox" name="pg1_pdt45" <%= props.getProperty("formAR."+"pg1_pdt45", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>46.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c46p1");return false;'>Substance abuse</a></td>
                    <td><input type="checkbox" name="pg1_pdt46" <%= props.getProperty("formAR."+"pg1_pdt46", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>47.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c47p1");return false;'>Family violence</a></td>
                    <td><input type="checkbox" name="pg1_pdt47" <%= props.getProperty("formAR."+"pg1_pdt47", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>48.</td>
                    <td><a href=# onClick='popupPage("<%=resource%>c48p1");return false;'>Parenting concerns</a></td>
                    <td><input type="checkbox" name="pg1_pdt48" <%= props.getProperty("formAR."+"pg1_pdt48", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td colspan="3"><hr><b>Risk factors identified</b></td>
                </tr>
                <tr>
                    <td colspan="3"><textarea name="c_riskFactors" cols="20" rows="5" style="width:100%"><%= props.getProperty("formAR."+"c_riskFactors", "") %></textarea></td>
                </tr>
            </table>
        </td>
        <td valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td colspan="2">
                        Ht.<input type="text" name="pg1_ht" size="5" maxlength="6"  value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_ht", "")) %>" />
                        Wt.<input type="text" name="pg1_wt" size="5" maxlength="6"  value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_wt", "")) %>" />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        Pre-preg. wt.<input type="text" name="c_ppWt" size="6" maxlength="6" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"c_ppWt", "")) %>">
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        BP<input type="text" name="pg1_BP" size="10" maxlength="10" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_BP", "")) %>">
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <hr><b>Checkmark if normal:</b></td>
                </tr>
                <tr>
                    <td>Head, teeth, ENT</td>
                    <td align="right"><input type="checkbox" name="pg1_head" <%= props.getProperty("formAR."+"pg1_head", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>Thyroid</td>
                    <td align="right"><input type="checkbox" name="pg1_thyroid" <%= props.getProperty("formAR."+"pg1_thyroid", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>Chest</td>
                    <td align="right"><input type="checkbox" name="pg1_chest" <%= props.getProperty("formAR."+"pg1_chest", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>Breasts</td>
                    <td align="right"><input type="checkbox" name="pg1_breasts" <%= props.getProperty("formAR."+"pg1_breasts", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>Cardiovascular</td>
                    <td align="right"><input type="checkbox" name="pg1_cardio" <%= props.getProperty("formAR."+"pg1_cardio", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>Abdomen</td>
                    <td align="right"><input type="checkbox" name="pg1_abdomen" <%= props.getProperty("formAR."+"pg1_abdomen", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>Varicosities, extremities</td>
                    <td align="right"><input type="checkbox" name="pg1_vari" <%= props.getProperty("formAR."+"pg1_vari", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>Neurological</td>
                    <td align="right"><input type="checkbox" name="pg1_neuro" <%= props.getProperty("formAR."+"pg1_neuro", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>Pelvic architecture</td>
                    <td align="right"><input type="checkbox" name="pg1_pelvic" <%= props.getProperty("formAR."+"pg1_pelvic", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>Ext. genitalia</td>
                    <td align="right"><input type="checkbox" name="pg1_extGen" <%= props.getProperty("formAR."+"pg1_extGen", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>Cervix, vagina</td>
                    <td align="right"><input type="checkbox" name="pg1_cervix" <%= props.getProperty("formAR."+"pg1_cervix", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td nowrap>Uterus
                        <input type="text" name="pg1_uterusBox" size="3" maxlength="3" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_uterusBox", "")) %>">
                        <span class="small"> (no. of wks.)</span>
                    </td>
                    <td align="right"><input type="checkbox" name="pg1_uterus" <%= props.getProperty("formAR."+"pg1_uterus", "").equals("1")?"checked":"" %>></td>
                </tr>
                <tr>
                    <td>Adnexa</td>
                    <td align="right"><input type="checkbox" name="pg1_adnexa" <%= props.getProperty("formAR."+"pg1_adnexa", "").equals("1")?"checked":"" %>></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<table width="100%" border="0">
    <tr bgcolor="#CCCCCC">
        <td align="center" colspan="2">
            <b>Comments re Medical History and Physical
            Examination</b>
        </td>
    </tr>
	<tr>
        <td colspan="2">
            <textarea name="pg1_commentsAR1" style="width:100%" cols="80" rows="5" ><%= props.getProperty("formAR."+"pg1_commentsAR1", "") %></textarea>
        </td>
    </tr>
    <tr>
        <td>Signature of attendant<br>
            <input type="text" name="pg1_signature" size="30" maxlength="50" style="width:80%" value="<%= UtilMisc.htmlEscape(props.getProperty("formAR."+"pg1_signature", "")) %>">
	    </td>
        <td>Date (yyyy/mm/dd)<br>
            <input type="text" name="pg1_formDate" size="30" maxlength="50" style="width:80%" value="<%= props.getProperty("formAR."+"pg1_formDate", "") %>">
	    </td>
    </tr>

</table>

<table class="Head" class="hidePrint">
    <tr>
        <td align="left">
            <input type="submit" value="Exit" onclick="javascript:return onExit();"/>
            <input type="submit" value="Print" onclick="javascript:return onPrint();"/>
        </td>
    </tr>
</table>

<form>
</body>
</html:html>
