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
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%   
    if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");

    String user_no = (String) session.getAttribute("user");
    String color ="";
    int Count=0;    
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>


<html:html locale="true">
<head>
<title><bean:message key="oscarResearch.oscarDxResearch.dxResearch.title"/></title>
<script language="JavaScript">
<!--
function setfocus() {
	document.forms[0].xml_research1.focus();
	document.forms[0].xml_research1.select();
}

var remote=null;

function rs(n,u,w,h,x) {
	args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
	remote=window.open(u,n,args);
	if (remote != null) {
		if (remote.opener == null)
			remote.opener = self;
	}
	if (x == 1) { return remote; }
}

function popPage(url) {
	awnd=rs('',url ,400,200,1);
	awnd.focus();
}

var awnd=null;

function ResearchScriptAttach() {
	var t0 = escape(document.forms[0].xml_research1.value);
	var t1 = escape(document.forms[0].xml_research2.value);
	var t2 = escape(document.forms[0].xml_research3.value);
	var t3 = escape(document.forms[0].xml_research4.value);
	var t4 = escape(document.forms[0].xml_research5.value);
        var demographicNo = escape(document.forms[0].demographicNo.value);
	awnd=rs('att','dxResearchCodeSearch.do?xml_research1='+t0 + '&xml_research2=' + t1 + '&xml_research3=' + t2 + '&xml_research4=' + t3 + '&xml_research5=' + t4 +'&demographicNo=' + demographicNo,600,600,1);
	awnd.focus();
}

function submitform(target){	
        document.forms[0].forward.value = target;
	document.forms[0].submit()
}

function set(target) {
     document.forms[0].forward.value=target;
}

function changeList(){
    var quickList = document.forms[0].quickList.options[document.forms[0].quickList.selectedIndex].value;
    var demographicNo = document.forms[0].demographicNo.value;    
    var providerNo = document.forms[0].providerNo.value;    
    openNewPage(600,600,'/oscar/oscarResearch/oscarDxResearch/setupDxResearch.do?demographicNo='+demographicNo+'&quickList='+quickList+'&providerNo='+providerNo);    
}

function openNewPage(vheight,vwidth,varpage) { 
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=no,menubars=no,toolbars=no,resizable=no,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "<bean:message key="oscarEncounter.Index.msgOscarConsultation"/>", windowprops);
  popup.focus();
}

//-->
</SCRIPT>
<style type="text/css">
    <!--
        A:link{
            font-size: 10pt ; 
            font-family: 
            verdana,arial,helvetica; 
            color: #336666;
            cursor: hand }
	A:visited{
            font-size: 10pt ; 
            font-family: verdana,arial,helvetica; 
            color: #336666; }
	A:hover{
            font-weight: bold  ; 
            font-size: 10pt; 
            font-family: verdana,arial,helvetica; 
            color: #000000; 
            background-color: #FFFFFF;}
	
	BODY{
            font-size: 10pt ; 
            font-family: verdana,arial,helvetica; 
            color: #000000; 
            background-color: #FFFFFF;
        }
	TD{
            font-size: 10pt ; 
            font-family: verdana,arial,helvetica;             
        }
	TD.black{
            font-weight: bold  ; 
            font-size: 11pt ; 
            font-family: verdana,arial,helvetica; 
            color: #FFFFFF; 
            background-color: #666699;
        }
	.heading{
            font-weight: bold  ; 
            font-size: 11pt ; 
            font-family: verdana,arial,helvetica; 
            color: #000000; 
            background-color: #6699cc;
            height: 20pt;
        }
        .notResolved{
            color: blue;
        }
        .subject{
            font-size: 18pt;
            font-weight: bold;
            color: #FFFFFF;
            height:40pt;
        }
	.sbttn {
            background: #EEEEFF;
            border-bottom: 1px solid #104A7B;
            border-right: 1px solid #104A7B;
            border-left: 1px solid #AFC4D5;
            border-top:1px solid #AFC4D5;
            color:#000066;
            height:19px;
            text-decoration:none;
            cursor: hand}          
	.mbttn{
            background: #D7DBF2;
            border-bottom: 1px solid #104A7B;
            border-right: 1px solid #104A7B;
            border-left: 1px solid #AFC4D5;
            border-top:1px solid #AFC4D5;
            color:#000066;height:19px;
            text-decoration:none;
            cursor: hand}

	-->
</style>  
</head>

<body bgcolor="#FFFFFF" text="#000000" rightmargin="0" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="setfocus()">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
    <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr bgcolor="#000000"> 
                <td class="subject" colspan="2">
                &nbsp;&nbsp;&nbsp;<bean:message key="oscarResearch.oscarDxResearch.dxResearch.msgDxResearch"/>
                </td>
        </tr>
        </table>
    </td>
</tr>
<tr>
    <td>
        <html:form action="/oscarResearch/oscarDxResearch/dxResearch.do">
        <table width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#EEEEFF" height="300">
        <tr> 
                <td width="25%" valign="top"> 

                <table width="100%" border="0" cellspacing="0" cellpadding="2" height="300" bgcolor="#FFFFFF">
                    <tr> 
                        <td class="heading">Coding System: <select name="codingSys" style="width:80px">
                                                                <option value="ICHPPC">ICHPPC</option>
                                                           </select>
                        </td>
                    </tr>
                    <tr>
                        <td><html:errors/></td>
                    </tr>
                    <tr> 
                        <td>
                            <input type="text" name="xml_research1" size="30">
                            <input type="hidden" name="demographicNo" value="<bean:write name="demographicNo"/>">
                            <input type="hidden" name="providerNo" value="<bean:write name="providerNo"/>">
                        </td>
                    </tr>
                    <tr> 
                        <td>
                            <input type="text" name="xml_research2" size="30">
                        </td>
                    </tr>
                    <tr> 
                        <td>
                            <input type="text" name="xml_research3" size="30">
                        </td>
                    </tr>
                    <tr> 
                        <td>
                            <input type="text" name="xml_research4" size="30">
                        </td>
                    </tr>
                    <tr> 
                        <td>
                            <input type="text" name="xml_research5" size="30">
                        </td>
                    </tr>
                    <tr> 
                        <td> 
                            <input type="hidden" name="forward" value="none"/>
                            <input type="button" name="button" class=mbttn value="Code Search" onClick="javascript: ResearchScriptAttach();")>
                            <input type="button" name="button" class=mbttn value="Add" onClick="javascript: submitform('');">
                        </td>
                    </tr>
                    <tr>
                        <td class="heading">
                            QuickList
                        </td>                
                    </tr>
                    <tr>
                        <td>
                            <html:select property="quickList" style="width:200px" onchange="javascript:changeList();">
                                <logic:iterate id="quickLists" name="allQuickLists" property="dxQuickListBeanVector">
                                    <option value="<bean:write name="quickLists" property="quickListName" />" <bean:write name="quickLists" property="lastUsed" />><bean:write name="quickLists" property="quickListName" /></option>
                                </logic:iterate>
                            </html:select>
                        </td>
                    </tr>
                    <logic:iterate id="item" name="allQuickListItems" property="dxQuickListItemVector">
                    <tr>
                        <td>
                            <a href="#" title='<bean:write name="item" property="dxSearchCode"/>' onclick="javascript:submitform('<bean:write name="item" property="dxSearchCode"/>');"><bean:write name="item" property="description"/></a>
                        </td>
                    </tr>
                    </logic:iterate>            
                    <tr>
                    </tr>
                </table>

                </td>
                <td width="75%" valign="top">

                <table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
                <tr class="heading"> 
                        <td width="48%"><b><bean:message key="oscarResearch.oscarDxResearch.dxResearch.msgDiagnosis"/></b></td>
                        <td width="15%"><b><bean:message key="oscarResearch.oscarDxResearch.dxResearch.msgFirstVisit"/></b></td>
                        <td width="15%"><b><bean:message key="oscarResearch.oscarDxResearch.dxResearch.msgLastVisit"/></b></td>
                        <td width="22%"><b><bean:message key="oscarResearch.oscarDxResearch.dxResearch.msgAction"/></b></td>
                </tr>        
                <logic:iterate id="diagnotics" name="allDiagnostics" property="dxResearchBeanVector" indexId = "ctr" >
                    <%  
                        if (Count == 0){
                            Count = 1;
                            color = "#FFFFFF";
                        } 
                        else {
                            Count = 0;
                            color="#EEEEFF";
                        }       
                    %> 
                    <logic:equal name="diagnotics" property="status" value="A">
                    <tr bgcolor="<%=color%>">
                        <td class="notResolved"><bean:write name="diagnotics" property="description" /></td>
                        <td class="notResolved"><bean:write name="diagnotics" property="start_date" /></td>                            
                        <td class="notResolved"><bean:write name="diagnotics" property="end_date" /></td>
                        <td class="notResolved">
                            <a href='dxResearchUpdate.do?status=C&did=<bean:write name="diagnotics" property="dxResearchNo" />&demographicNo=<bean:write name="demographicNo" />&providerNo=<bean:write name="providerNo" />'><bean:message key="oscarResearch.oscarDxResearch.dxResearch.btnResolve"/></a> | <a href='dxResearchUpdate.do?status=D&did=<bean:write name="diagnotics" property="dxResearchNo" />&demographicNo=<bean:write name="demographicNo" />&providerNo=<bean:write name="providerNo" />'><bean:message key="oscarResearch.oscarDxResearch.dxResearch.btnDelete"/></a>            
                        </td>
                    </tr>
                    </logic:equal>
                    <logic:equal name="diagnotics" property="status" value="C">
                    <tr bgcolor="<%=color%>">
                        <td><bean:write name="diagnotics" property="description" /></td>
                        <td><bean:write name="diagnotics" property="start_date" /></td>                            
                        <td><bean:write name="diagnotics" property="end_date" /></td>
                        <td>                
                           <bean:message key="oscarResearch.oscarDxResearch.dxResearch.btnResolve"/> | <a href='dxResearchUpdate.do?status=D&did=<bean:write name="diagnotics" property="dxResearchNo" />&demographicNo=<bean:write name="demographicNo" />&providerNo=<bean:write name="providerNo" />'><bean:message key="oscarResearch.oscarDxResearch.dxResearch.btnDelete"/></a>
                        </td>
                    </tr>
                    </logic:equal>
                </logic:iterate>
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
