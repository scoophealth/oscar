<%@ page import="java.util.ArrayList"%>
<%@ page import="java.net.URLEncoder"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html-el" prefix="html-el" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@ page import="oscar.oscarDemographic.data.DemographicData"%>
<%@ page import="oscar.indivoMessenger.*"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <html:base />
        <link rel="stylesheet" type="text/css" href="../../oscarMessenger/encounterStyles.css">
        <title>
        <%-- bean:message key="indivoMessenger.DisplayMessages.title"/ --%>  myOSCAR
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

        <script type="text/javascript">
        function BackToOscar()
        {
               window.close();
        } 
        </script>
    </head>

    <body class="BodyStyle" vlink="#0000FF" onload="window.focus()">
        <!--  -->

    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                <bean:message key="oscarMessenger.DisplayMessages.msgMessenger"/>
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
                            <div class="DivContentTitle"><bean:message key="oscarMessenger.DisplayMessages.msgInbox"/></div>
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
            <td class="MainTableRightColumn">
                <table width="100%">
                    <tr>
                        <td>
                            <table  cellspacing=3 >
                                <tr>
                                    <td >
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA">
                                                    <html:link page="/phr/PhrMessage.do?method=viewMessages" styleClass="messengerButtons">
                                                        <bean:message key="oscarMessenger.DisplayMessages.btnRefresh"/>
                                                    </html:link>
                                        </td></tr></table>
                                    </td>
                                    <td >
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA">
                                                    <a href="javascript:window.close()" class="messengerButtons">Exit MyOscar</a>
                                        </td></tr></table>
                                    </td>
                                    <td >
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA">
                                             <html:link page="/indivoMessenger/Logout.jsp" styleClass="messengerButtons">
                                                  Logout
                                             </html:link>
                                        </td></tr></table>
                                    </td>
                                </tr>
                            </table><!--cell spacing=3-->
                        </td>
                    </tr>
                </table><!--table width="100%">-->
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn">
                &nbsp;
            </td>
            <td class="MainTableRightColumn">
                <table border="0" width="80%" cellspacing="1">
                    <tr>
                        <th bgcolor="#DDDDFF" width="75">
                            &nbsp;
                        </th>
                        <th align="left" bgcolor="#DDDDFF">
                            <html-el:link action="/phr/PhrMessage?orderby=0" >
                            
                                <bean:message key="oscarMessenger.DisplayMessages.msgStatus"/>
                            </html-el:link>
                        </th>
                        <th align="left" bgcolor="#DDDDFF">
                             <html-el:link action="/phr/PhrMessage?orderby=1" >
                                <bean:message key="oscarMessenger.DisplayMessages.msgFrom"/>
                            </html-el:link>
                        </th>
                        <th align="left" bgcolor="#DDDDFF">
                            <html-el:link action="/phr/PhrMessage?orderby=2" >
                                <bean:message key="oscarMessenger.DisplayMessages.msgSubject"/>
                            </html-el:link>
                        </th>
                        <th align="left" bgcolor="#DDDDFF">
                            <html-el:link action="/phr/PhrMessage?orderby=3" >
                                <bean:message key="oscarMessenger.DisplayMessages.msgDate"/>
                            </html-el:link>
                        </th>                                    
                    </tr>          
                    <c:forEach var="iMessage" items="${indivoMessages}">
                        <tr>
                            <td bgcolor="#EEEEFF" width="75">
                               
                            <c:if test="${iMessage.replied}" > --> </c:if>
                            
                            </td>
                            <td bgcolor="#EEEEFF" width="75">
                                <c:choose>
                                   <c:when test="${iMessage.read}">read</c:when>
                                   <c:otherwise>new</c:otherwise>
                                </c:choose>
                            </td>
                            <td bgcolor="#EEEEFF">
                                <c:out value="${iMessage.senderPhr}"/></td>
                            <td bgcolor="#EEEEFF">
                                
                                <html-el:link action="/phr/PhrMessage?&method=read&id=${iMessage.id}"  >
                                   <c:out value="${iMessage.docSubject}"/>
                                </html-el:link>
                                
                            </td>
                            <td bgcolor="#EEEEFF"> 
                                <fmt:formatDate value="${iMessage.dateSent}" type="DATE" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                        </tr>
                    </c:forEach> 
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
    </body>
</html>
