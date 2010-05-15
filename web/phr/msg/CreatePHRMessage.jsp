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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
--%>

<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.w3c.dom.*" %>
<%@ page import="oscar.oscarDemographic.data.*" %>
<%@ page import="javax.servlet.http.HttpServletRequest.*" %>
<%@ page import="java.util.Iterator.*" %>
<%@ page import="java.util.Enumeration.*" %>
<%@ page import="org.apache.commons.collections.iterators.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html-el" prefix="html-el" %>


<html:html locale="true">

<head>
    <html:base/>
    <link rel="stylesheet" type="text/css" href="../../oscarMessenger/encounterStyles.css">
<title>
<%--bean:message key="indivoMessenger.CreateIndivoMessage.title"/--%> Create Message
</title>

<style type="text/css">
    td.messengerButtonsA{
    /*background-color: #6666ff;*/
    /*background-color: #6699cc;*/
    background-color: #003399;
    }
    td.messengerButtonsD{
    /*background-color: #84c0f4;*/
    background-color: #555599;
    }
    a.messengerButtons{
    color: #ffffff;
    font-size: 9pt;
    text-decoration: none;
    }
    
    table.messButtonsA{
    border-top: 2px solid #cfcfcf;
    border-left: 2px solid #cfcfcf;
    border-bottom: 2px solid #333333;
    border-right: 2px solid #333333;
    }
    
    table.messButtonsD{
    border-top: 2px solid #333333;
    border-left: 2px solid #333333;
    border-bottom: 2px solid #cfcfcf;
    border-right: 2px solid #cfcfcf;
    }
</style>
<script language="javascript">

    var browserName=navigator.appName; 
    if (browserName=="Netscape"){ 

        if( document.implementation ){
            //this detects W3C DOM browsers (IE is not a W3C DOM Browser)
            if( Event.prototype && Event.prototype.__defineGetter__ ){
                //this detects Mozilla Based Browsers
                Event.prototype.__defineGetter__( "srcElement", function(){
                    var src = this.target;
                    if( src && src.nodeType == Node.TEXT_NODE )
                        src = src.parentNode;
                        return src;
                    }
                );
            }
        }
    }
</script>
</head>


<body class="BodyStyle" vlink="#0000FF" >

<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                <%-- bean:message key="indivoMessenger.CreateMessage.msgMessenger"/ --%>Create Message
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
                            <bean:message key="oscarMessenger.CreateMessage.msgCreate"/>
                        </td>
                        <td  >
                            &nbsp;
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
            <td class="MainTableRightColumn">
                <table >

                    <tr>
                        <td>
                            <table cellspacing=3 >
                                <tr >
                                    <td >
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA">
                                            <html:link action="/phr/PhrMessage.do?method=viewMessages" styleClass="messengerButtons">
                                             <bean:message key="oscarMessenger.ViewMessage.btnInbox"/>
                                            </html:link>
                                        </td></tr></table>
                                    </td>
                                    <td >
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA">
                                            <a href="javascript:document.forms[0].body.value=''; return false;" class="messengerButtons"><bean:message key="oscarMessenger.CreateMessage.btnClear"/></a>
                                        </td></tr></table>
                                    </td>
                                    <td >
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA">
                                            <a href="javascript:window.close()" class="messengerButtons"><bean:message key="oscarMessenger.ViewMessage.btnExit"/></a>
                                        </td></tr></table>
                                    </td>
                                 </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <table> 
                                 <%--
         request.setAttribute("toName",toName);
        request.setAttribute("toId",toId);
        request.setAttribute("toType",toType);
     
        request.setAttribute("fromName",fromName);
        request.setAttribute("fromId",fromId);
        request.setAttribute("fromType",fromType);
         
                                        --%>
                                <html:form action="/phr/PhrMessage">
                                    <tr>
                                        <th align="left" bgcolor="#DDDDFF">
                                            
                                            <bean:message key="oscarMessenger.CreateMessage.msgMessage"/>
                                        </th>
                                    </tr>
                                    <tr>
                                        <td bgcolor="#EEEEFF" valign=top>   <!--Message and Subject Cell--><c:out value=""/>
                                            <table>
                                                <tr>
                                                    <td align="right">To :</td>
                                                    <td><html-el:text name="to" property="to" size="30" value="${toName}"/>
                                                        <html-el:hidden property="recipientOscarId" value="${toId}"/>
                                                        <html-el:hidden property="recipientType" value="${toType}"/>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="right"><bean:message key="oscarMessenger.CreateMessage.formSubject"/> : </td>
                                                    <td><html-el:text name="subject" property="subject" size="67" value="${subject}"/></td>
                                                </tr>
                                                <c:if test="${!empty message}">
                                                    <tr>
                                                        <td >&nbsp;</td>
                                                        
                                                        <td >
                                                            <pre style="border: 1px solid black;  background-color: white;" ><c:out value="${message.body}" />
                                                            </pre>
                                                            <html-el:hidden property="priorMessageId" value="${message.id}"/>
                                                        </td>
                                                    </tr>
                                                    
                                                </c:if> 
                                                <tr>
                                                    <td>&nbsp;</td>
                                                    <td><html:textarea value="" name="body" styleId="message" property="body" cols="60" rows="18"/></td>
                                                        
                                                </tr>
                                            </table>
                                           
                                            <html-el:hidden property="demographic" value="${demographicNo}"/>
                                            <html-el:hidden property="method" value="send"/>
                                            <input type="submit" class="ControlPushButton" value="<bean:message key="oscarMessenger.CreateMessage.btnSendMessage"/>" >
                                        </td>
                                    </tr>
                                    </html:form>                                                                                                                                                                            
                                </table>                            
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <script language="JavaScript">
                            document.forms[0].message.focus();
                            </script>
                        </td>
                    </tr>
                    
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">
            &nbsp;
            </td>
            <td class="MainTableBottomRowRightColumn">
            &nbsp;
            </td>
        </tr>
    </table>
</body>
</html:html>



