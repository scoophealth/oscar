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
<%@ page language="java" import="oscar.oscarDemographic.data.*, java.util.Enumeration" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html-el" prefix="html-el" %>


<html:html locale="true">
<head>

<link rel="stylesheet" type="text/css" href="../oscarMessenger/encounterStyles.css" media="screen">
<link rel="stylesheet" type="text/css" href="../oscarMessenger/printable.css" media="print">    

<title>
<%-- bean:message key="indivoMessenger.ViewIndivoMessage.title"/--%>View Message
</title>


<script type="text/javascript">
function BackToOscar()
{
       window.close();
}
</script>

</head>

<body class="BodyStyle" vlink="#0000FF" >

<html:form action="/phr/PhrMessage">
   
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                <%-- bean:message key="indivoMessenger.ViewIndivoMessage.msgMessenger"/ --%>View Message
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
                            <bean:message key="oscarMessenger.ViewMessage.msgViewMessage"/>
                        </td>
                        <td  >
                        </td>
                        <td style="text-align:right">
                                <a href="javascript:popupStart(300,400,'Help.jsp')"  ><bean:message key="global.help"/></a> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about"/></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license"/></a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn">
                &nbsp;
            </td>
            <td class="MainTableRightColumn Printable">
                <table>
                    <tr>
                        <td>
                            <table  cellspacing=3>
                                <tr>
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3 ><tr><td class="messengerButtonsA">
                                            <a href="javascript:window.print()" class="messengerButtons"><bean:message key="oscarMessenger.ViewMessage.btnPrint"/></a>
                                        </td></tr></table>
                                    </td>   
                                  
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3 ><tr><td class="messengerButtonsA">
                                           <a href="PhrMessage.do?method=<c:out value="${comingfrom}"/>" class="messengerButtons">
                                             Back
                                           </a>
                                        </td></tr></table>
                                    </td>                                    
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3 ><tr><td class="messengerButtonsA">
                                            <a href="javascript:BackToOscar()" class="messengerButtons"><%-- bean:message key="indivoMessenger.ViewMessage.btnExit"/ --%>Exit</a>
                                        </td></tr></table>
                                    </td>                                    
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td class="Printable">
                            <table border="0" cellspacing="1" valign="top">
                                <tr>
                                    <td class="Printable" bgcolor="#DDDDFF" align="right">
                                    <bean:message key="oscarMessenger.ViewMessage.msgFrom"/>:
                                    </td>
                                    <td class="Printable" bgcolor="#CCCCFF">
                                       <c:out value="${message.senderPhr}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="Printable" bgcolor="#DDDDFF" align="right">
                                    <bean:message key="oscarMessenger.ViewMessage.msgTo"/>:
                                    </td>
                                    <td class="Printable" bgcolor="#BFBFFF">
                                       <c:out value="${message.receiverPhr}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="Printable" bgcolor="#DDDDFF" align="right">
                                        <bean:message key="oscarMessenger.ViewMessage.msgSubject"/>:
                                    </td>
                                    <td class="Printable" bgcolor="#BBBBFF">
                                        <c:out value="${message.docSubject}"/>
                                    </td>
                                </tr>
                                <tr>
                                  <td class="Printable" bgcolor="#DDDDFF" align="right">
                                      <bean:message key="oscarMessenger.ViewMessage.msgDate"/>:
                                  </td>
                                  <td  class="Printable" bgcolor="#B8B8FF">
                                      <c:out value="${message.dateSent}"/>
                                  </td>
                                </tr>
                                <tr>
                                    
                                    <td bgcolor="#EEEEFF" ></td>
                                    <td bgcolor="#EEEEFF" >
                                        <textarea name="msgBody" wrap="hard" readonly="true" rows="18" cols="60" ><c:out value="${message.body}"/></textarea><br>
                                        <logic:notPresent name="noreply">
                                            <html:submit styleClass="ControlPushButton" property="reply">
                                                <bean:message key="oscarMessenger.ViewMessage.btnReply"/>
                                            </html:submit>
                                        </logic:notPresent>
                                        <html-el:hidden property="msgTo"         value="${message.senderPhr}" />
                                        <html-el:hidden property="msgFrom"       value="${message.senderPhr}" />
                                        <html-el:hidden property="msgRe"         value="${message.docSubject}" />
                                        <html-el:hidden property="id"            value="${message.id}" />
                                        <html-el:hidden property="docIdx"        value="${message.phrIndex}" />
                                        <html-el:hidden property="demographicNo" value="${message.senderOscar}"/>
                                        <html-el:hidden property="comingfrom"    value="${comingfrom}"/>
                                        <input type="hidden" name="method" value="reply"/>
                                     </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#EEEEFF" ></td>
                                    <td bgcolor="#EEEEFF" >&nbsp;</td>
                                </tr>
                            </table>
                        </td>                        
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">

            </td>
            <td class="MainTableBottomRowRightColumn">

            </td>
        </tr>
    </table>
 </html:form>
</body>

</html:html>
