<%@ page import="java.util.ArrayList"%>
<%@ page import="java.net.URLEncoder"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html-el" prefix="html-el" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-nested" prefix="nested" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/phr-tag.tld" prefix="phr" %>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite" %>

<%@ page import="oscar.oscarDemographic.data.DemographicData"%><%@ page import="org.oscarehr.phr.PHRAuthentication"%>
<%@ page import="org.oscarehr.phr.model.PHRAction"%>
<%@ page import="oscar.oscarProvider.data.ProviderData"%>
<%@ page import="org.oscarehr.phr.PHRConstants"%>
<%@ page import="org.oscarehr.phr.model.PHRMessage"%>
<%@ page import="org.oscarehr.phr.dao.PHRActionDAO, org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="oscar.util.StringUtils"%>
<%@ page import="org.oscarehr.phr.indivo.service.accesspolicies.IndivoAPService" %>

<%
int statusNotAuthorized = PHRAction.STATUS_NOT_AUTHORIZED;
String providerName = request.getSession().getAttribute("userfirstname") + " " + 
        request.getSession().getAttribute("userlastname");
String providerNo = (String) request.getSession().getAttribute("user");
ProviderData providerData = new ProviderData();
providerData.setProviderNo(providerNo);
String providerPhrId = providerData.getMyOscarId();
request.setAttribute("forwardto", request.getRequestURI());

//set the "sent" tab to red if there are authorization errors on send
PHRActionDAO phrActionDAO = (PHRActionDAO) WebApplicationContextUtils.getWebApplicationContext(
        		pageContext.getServletContext()).getBean("phrActionDAO");

//some phrAction static constants
pageContext.setAttribute("STATUS_OTHER_ERROR", PHRAction.STATUS_OTHER_ERROR);
pageContext.setAttribute("STATUS_NOT_AUTHORIZED", PHRAction.STATUS_NOT_AUTHORIZED);
pageContext.setAttribute("STATUS_SENT", PHRAction.STATUS_SENT);
pageContext.setAttribute("STATUS_SEND_PENDING", PHRAction.STATUS_SEND_PENDING);
pageContext.setAttribute("STATUS_ON_HOLD", PHRAction.STATUS_ON_HOLD);

pageContext.setAttribute("ACTION_ADD", PHRAction.ACTION_ADD);
pageContext.setAttribute("ACTION_UPDATE", PHRAction.ACTION_UPDATE);

pageContext.setAttribute("TYPE_BINARYDATA", PHRConstants.DOCTYPE_BINARYDATA());
pageContext.setAttribute("TYPE_MEDICATION", PHRConstants.DOCTYPE_MEDICATION());
pageContext.setAttribute("TYPE_ACCESSPOLICIES", PHRConstants.DOCTYPE_ACCESSPOLICIES());

String pageMethod = request.getParameter("method");
if (pageMethod.equals("delete") || pageMethod.equals("resend")) 
    pageMethod = "viewSentMessages";
if (pageMethod.equals("archive"))
    pageMethod = "viewMessages";
if (pageMethod.equals("unarchive"))
    pageMethod = "viewArchivedMessages";
    
request.setAttribute("pageMethod",pageMethod);
   
    GregorianCalendar now=new GregorianCalendar();
    int curYear = now.get(Calendar.YEAR);
    int curMonth = (now.get(Calendar.MONTH)+1);
    int curDay = now.get(Calendar.DAY_OF_MONTH);
    String dateString = curYear+"-"+curMonth+"-"+curDay;    
    
    //get Actions Pending Approval
    List<PHRAction> actionsPendingApproval = (List<PHRAction>) request.getSession().getAttribute("actionsPendingApproval");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<html>
    <head>
        <html:base />
        <link rel="stylesheet" type="text/css" href="../../oscarMessenger/encounterStyles.css">
        <title>
        <%-- bean:message key="indivoMessenger.DisplayMessages.title"/ --%>  myOSCAR
        </title>
        <script type="text/javascript" src="../../share/javascript/prototype.js"></script>
        <script type="text/javascript" src="../../share/javascript/Oscar.js"></script>
        <script type="text/javascript" src="../../phr/phr.js"></script>
        <style type="text/css">
        td.messengerButtonsA{
            /*background-color: #6666ff;*/
            /*background-color: #6699cc;*/
            background-color: #003399;
        }
        td.messengerButtonsACurrent{
            /*background-color: #6666ff;*/
            /*background-color: #6699cc;*/
            background-color: #00ae99;
        }
        td.messengerButtonsAWarning{
            background-color: red;
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
        
        .myoscarLoginElementNoAuth {
            border: 0;
            padding-left: 3px;
            padding-right: 3px;
            background-color: #f3e9e9;
        }
        
        .myoscarLoginElementAuth {
            border: 0;
            padding-left: 3px;
            padding-right: 3px;
            background-color: #d9ecd8;
        }
        .moreInfoBoxoverBody{
            border: 1px solid #9fbbe8;
            padding: 1px;
            padding-left: 3px;
            padding-right: 3px;
            border-top: 0px;
            font-size: 10px;
            background-color: white;
        }
        .moreInfoBoxoverHeader{
            border: 1px solid #9fbbe8;
            background-color: #e8ecf3;
            padding: 2px;
            padding-left: 3px;
            padding-right: 3px;
            border-bottom: 0px;
            font-size: 10px;
            color: red;
        }
        table.messageTable {
        }
        
        tr.normal td {
            background-color: #EEEEFF;
        }
        tr.new td {
            
        }
        
        .notAuthorized {
            background-color: #ffcdb9;
        }
        .sendPending {
            background-color: #e1eddb;
        }
        .onHold {
            background-color: #edebdb;
        }
        .normal {
            background-color: #EEEEFF;
        }
        .new {
            background-color: #EEEEFF;
            font-weight: bold;
        }
        
        .statusDiv {
            background-color: #fb8781;
        }
        
        div.sharingAlert {
            background-color: #ffdf6f; /*#ffd649;*/
            width: 99%;
            font-size: 11px;
            margin-top: 1px;
            overflow: hidden;
            white-space: nowrap;
            
        }
        </style>

        <script type="text/javascript">
        function BackToOscar()
        {
               window.close();
        } 
        function setFocus() {
            if (document.getElementById('phrPassword'))
                document.getElementById('phrPassword').focus();
        }
        function reloadWindow() {
            window.location = "../../phr/PhrMessage.do?method=<%=request.getParameter("method")%>";
        }
        
        function setStatus(message) {
            if (message.length == 0) {
                $('statusDiv').hide();
            } else {
                $('statusDiv').show();
                $("statusDiv").innerHTML = message;
            }
        }
        
        function gotoEchart(demoNo, msgBody) {
            var url = '<c:out value="${ctx}"/>/oscarEncounter/IncomingEncounter.do?providerNo=<%=session.getAttribute("user")%>&appointmentNo=&demographicNo='+ demoNo + '&curProviderNo=&reason=<%=URLEncoder.encode("My Oscar Notes")%>&userName=<%=URLEncoder.encode(session.getAttribute("userfirstname")+" "+session.getAttribute("userlastname")) %>&curDate=<%=""+curYear%>-<%=""+curMonth%>-<%=""+curDay%>&encType=<%=URLEncoder.encode("MyOSCAR Note","UTF-8")%>&noteBody=';
            url += msgBody + '&appointmentDate=&startTime=&status=';
            popup(755,1048,url,'apptProvider');
        }
        </script>
    </head>

    <body class="BodyStyle" vlink="#0000FF" onload="window.focus(); setFocus();">
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
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA<%if (pageMethod.equals("viewMessages")) {%>Current<%}%>">
                                                    <html:link page="/phr/PhrMessage.do?method=viewMessages" styleClass="messengerButtons">
                                                        Inbox
                                                    </html:link>
                                        </td></tr></table>
                                    </td>
                                    <td >
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA<%if (phrActionDAO.ifActionsWithErrors(providerNo)) {%>Warning<%} else if (pageMethod.equals("viewSentMessages")) {%>Current<%}%>">
                                                    <html:link page="/phr/PhrMessage.do?method=viewSentMessages" styleClass="messengerButtons">
                                                        Sent
                                                    </html:link>
                                        </td></tr></table>
                                    </td>
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA<%if (pageMethod.equals("viewArchivedMessages")) {%>Current<%}%>">
                                                    <html:link page="/phr/PhrMessage.do?method=viewArchivedMessages" styleClass="messengerButtons">
                                                        Archived
                                                    </html:link>
                                        </td></tr></table>
                                    </td>
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA<%if (pageMethod.equals("viewArchivedMessages")) {%>Current<%}%>">
                                                    <a href="../../phrExchange.do?method=setExchangeTimeNow&forwardto=phr/msg/DisplayPHRMessages.jsp?method=<%=pageMethod%>" class="messengerButtons">
                                                        Send & Receive
                                                    </a>
                                        </td></tr></table>
                                    </td>
                                    <td >
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA">
                                                    <a href="javascript:window.close()" class="messengerButtons">Exit</a>
                                        </td></tr></table>
                                    </td>
                                    <%PHRAuthentication phrAuth = (PHRAuthentication) session.getAttribute(PHRAuthentication.SESSION_PHR_AUTH);%>
                                    <logic:present name="<%=PHRAuthentication.SESSION_PHR_AUTH%>">
                                        <td class="myoscarLoginElementAuth">
                                            <div>
                                                Status: <b>Logged in as <%=providerName%></b> (<%=phrAuth.getUserId()%>)
                                                <form action="../../phr/Logout.do" name="phrLogout" method="POST"  style="margin: 0px; padding: 0px;">
                                                    <input type="hidden" name="forwardto" value="<%=request.getServletPath()%>?method=<%=(String) request.getParameter("method")%>">
                                                    <center><a href="javascript: document.forms['phrLogout'].submit()">Logout</a><div class="statusDiv" id="statusDiv"></div></center>
                                                </form>
                                            </div>
                                        </td>
                                      <!--<p style="background-color: #E00000"  title="fade=[on] requireclick=[on] header=[Diabetes Med Changes] body=[<span style='color:red'>no DM Med changes have been recorded</span> </br>]">dsfsdfsdfsdfgsdgsdg</p>-->
                                    </logic:present>
                                    <logic:notPresent name="<%=PHRAuthentication.SESSION_PHR_AUTH%>">
                                        <td class="myoscarLoginElementNoAuth">
                                            <div>
                                                <form action="../../phr/Login.do" name="phrLogin" method="POST"  style="margin-bottom: 0px;">
                                                    <logic:present name="phrUserLoginErrorMsg">
                                                        <div class="phrLoginErrorMsg"><font color="red"><bean:write name="phrUserLoginErrorMsg"/>.</font>  
                                                        <logic:present name="phrTechLoginErrorMsg">
                                                            <a href="javascript:;" title="fade=[on] requireclick=[off] cssheader=[moreInfoBoxoverHeader] cssbody=[moreInfoBoxoverBody] singleclickstop=[on] header=[MyOSCAR Server Response:] body=[<bean:write name="phrTechLoginErrorMsg"/> </br>]">More Info</a></div>
                                                        </logic:present>
                                                    </logic:present>
                                                    Status: <b>Not logged in</b><br/>
                                                    <%=providerName%> password: <input type="password" id="phrPassword" name="phrPassword" style="font-size: 8px; width: 40px;"> <a href="javascript: document.forms['phrLogin'].submit()">Login</a>
                                                    <input type="hidden" name="forwardto" value="<%=request.getServletPath()%>?method=<%=(String) request.getParameter("method")%>">
                                                </form>
                                            </div>
                                        </td>
                                    </logic:notPresent>
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
                
                <%-- Sharing approval alerts -------------- --%>
                <%if (actionsPendingApproval != null) {
                    for (PHRAction actionPendingApproval: actionsPendingApproval) {
                        ProviderData senderProvider = new ProviderData(actionPendingApproval.getSenderOscar()); 
                        List idAndPermission = IndivoAPService.getProposalIdAndPermission(actionPendingApproval);
                        String demographicIndivoId = (String) idAndPermission.get(0);
                        String permission = (String) idAndPermission.get(1);
                        String permissionReadable = permission.substring(permission.lastIndexOf(':')+1, permission.length());
                                %>
                    <div class="sharingAlert">
                    <span style="float: left;">
                        Add sharing for <span style="font-weight: bold; color: #339a8a;"><%=demographicIndivoId%></span>.  
                        Share type: <span style="font-weight: bold; color: #d0722e;"><%=permissionReadable%></span> 
                        (Proposed by <span style="font-weight: bold; color: #339a8a;"><%= senderProvider.getFirst_name()%> <%=senderProvider.getLast_name()%></span>)
                    </span>
                        <span style="float: right;"><a href="../../phr/UserManagement.do?method=approveAction&actionId=<%=actionPendingApproval.getId()%>"><b>Approve</b></a> <a href="../../phr/UserManagement.do?method=denyAction&actionId=<%=actionPendingApproval.getId()%>"><b>Deny</b></a></span>
                    </div>
                    
                  <%} 
                }%>
                
                
                <table class="messageTable" border="0" width="99%" cellspacing="1">
                    <tr>
                        <th bgcolor="#DDDDFF" width="30">
                            &nbsp;
                        </th>
                        <th align="left" bgcolor="#DDDDFF">
                            <html-el:link action="/phr/PhrMessage?orderby=0&method=${pageMethod}" >
                                <bean:message key="oscarMessenger.DisplayMessages.msgStatus"/>
                            </html-el:link>
                        </th>
                        <th align="left" bgcolor="#DDDDFF">
                             <html-el:link action="/phr/PhrMessage?orderby=1&method=${pageMethod}" >
                                 <%if (pageMethod.equals("viewSentMessages")) {%>
                                         Recipient
                                 <%} else {%>
                                         <bean:message key="oscarMessenger.DisplayMessages.msgFrom"/>
                                 <%}%>
                            </html-el:link>
                        </th>
                        <th align="left" bgcolor="#DDDDFF">
                            <html-el:link action="/phr/PhrMessage?orderby=2&method=${pageMethod}" >
                                <bean:message key="oscarMessenger.DisplayMessages.msgSubject"/>
                            </html-el:link>
                        </th>
                        <th align="left" bgcolor="#DDDDFF">
                            <html-el:link action="/phr/PhrMessage?orderby=3&method=${pageMethod}" >
                                <bean:message key="oscarMessenger.DisplayMessages.msgDate"/>
                            </html-el:link>
                        </th>
                        <th align="center" style="width: 30px;" bgcolor="#DDDDFF">
                                &nbsp;
                        </th>
                        <%if (pageMethod.equals("viewSentMessages")) {%>
                            <th align="center" style="width: 30px;" bgcolor="#DDDDFF">
                                &nbsp;
                            </th>
                        <%}%>
                    </tr>
                    
     <%-- Sent items-----------------------------------------------------------------%>
 
                    <c:forEach var="otherAction" items="${indivoOtherActions}">
                        <tr 
                            <c:choose>
                                <c:when test="${otherAction.status == STATUS_ON_HOLD}">class="onHold"</c:when>
                                <c:when test="${otherAction.status == STATUS_SEND_PENDING}">class="sendPending"</c:when>
                                <c:when test="${otherAction.status == STATUS_NOT_AUTHORIZED}">class="notAuthorized"</c:when>
                                <c:when test="${otherAction.status == STATUS_OTHER_ERROR}">class="notAuthorized"</c:when>
                                <c:otherwise>class="normal"</c:otherwise>
                            </c:choose>
                        >
                            <td width="30">
                                &nbsp;
                            </td>
                            <td width="120"> 
                                <c:choose>
                                    <c:when test="${otherAction.status == STATUS_SENT}">Sent</c:when>
                                    <c:when test="${otherAction.status == STATUS_ON_HOLD}">Waiting to send...</c:when>
                                    <c:when test="${otherAction.status == STATUS_SEND_PENDING}">Waiting to send...</c:when>
                                    <c:when test="${otherAction.status == STATUS_NOT_AUTHORIZED}">Error: Not Authorized</c:when>
                                    <c:when test="${otherAction.status == STATUS_OTHER_ERROR}">Unknown Error</c:when>
                                    <c:otherwise>Unknown</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                            <c:choose>
                                 <c:when test="${otherAction.phrClassification == TYPE_ACCESSPOLICIES}">
                                    <%List idAndPermission = IndivoAPService.getProposalIdAndPermission((PHRAction)pageContext.getAttribute("otherAction"));
                                     String demographicIndivoId = (String) idAndPermission.get(0);
                                     String permission = (String) idAndPermission.get(1);
                                     String permissionReadable = permission.substring(permission.lastIndexOf(':')+1, permission.length());%>
                                    <%=demographicIndivoId%> (<%=permissionReadable%>)
                                 </c:when>
                                 <c:otherwise><c:out value="${otherAction.receiverPhr}"/></c:otherwise>
                            </c:choose>
                            </td>
                            <td>
                                  <c:choose>
                                    <c:when test="${otherAction.phrClassification == TYPE_BINARYDATA}">
                                            <c:choose>
                                                <c:when test="${otherAction.actionType == ACTION_ADD}"><-- Add Document></c:when>
                                                <c:when test="${otherAction.actionType == ACTION_UPDATE}"><-- Update Document></c:when>
                                                <c:otherwise><-- Document></c:otherwise>
                                            </c:choose>
                                    </c:when>
                                    <c:when test="${otherAction.phrClassification == TYPE_MEDICATION}">
                                            <c:choose>
                                                <c:when test="${otherAction.actionType == ACTION_ADD}"><-- Add Medication></c:when>
                                                <c:when test="${otherAction.actionType == ACTION_UPDATE}"><-- Update Medication></c:when>
                                                <c:otherwise><-- Medication></c:otherwise>
                                            </c:choose>
                                    </c:when>
                                    <c:when test="${otherAction.phrClassification == TYPE_ACCESSPOLICIES}"><-- Sharing Permission</c:when>
                                    <c:otherwise><-- Unknown></c:otherwise>
                                </c:choose>
                            </td>
                            <td> 
                                <fmt:formatDate value="${otherAction.dateQueued}" type="DATE" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${otherAction.status == STATUS_NOT_AUTHORIZED || otherAction.status == STATUS_OTHER_ERROR}"><html-el:link action="/phr/PhrMessage?&method=resend&id=${otherAction.id}">Resend</html-el:link></c:when>
                                    <c:otherwise>&nbsp;</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <html-el:link action="/phr/PhrMessage?&method=delete&id=${otherAction.id}">Remove</html-el:link>
                            </td>
                        </tr>
                    </c:forEach>
     
     
                    <c:forEach var="actionMessage" items="${indivoMessageActions}">
                        <tr 
                            <c:choose>
                                <c:when test="${actionMessage.status == STATUS_SEND_PENDING}">class="sendPending"</c:when>
                                <c:when test="${actionMessage.status == STATUS_NOT_AUTHORIZED}">class="notAuthorized"</c:when>
                                <c:when test="${actionMessage.status == STATUS_OTHER_ERROR}">class="notAuthorized"</c:when>
                                <c:otherwise>class="normal"</c:otherwise>
                            </c:choose>
                        >
                            <td width="30">
                                &nbsp;
                            </td>
                            <td width="120"> 
                                <c:choose>
                                    <c:when test="${actionMessage.status == STATUS_SEND_PENDING}">Waiting to send...</c:when>
                                    <c:when test="${actionMessage.status == STATUS_NOT_AUTHORIZED}">Error: Not Authorized</c:when>
                                    <c:when test="${actionMessage.status == STATUS_OTHER_ERROR}">Unknown Error</c:when>
                                    <c:otherwise>Unknown</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                            <c:out value="${actionMessage.phrMessage.receiverPhr}"/></td>
                            <td>
                                <html-el:link action="/phr/PhrMessage?method=read&source=actions&comingfrom=viewSentMessages&noreply=yes&id=${actionMessage.id}"  >
                                    <c:out value="${actionMessage.phrMessage.docSubject}"/>
                                </html-el:link>
                            </td>
                            <td> 
                                <fmt:formatDate value="${actionMessage.dateQueued}" type="DATE" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${actionMessage.status == STATUS_NOT_AUTHORIZED || actionMessage.status == STATUS_OTHER_ERROR}"><html-el:link action="/phr/PhrMessage?&method=resend&id=${actionMessage.id}">Resend</html-el:link></c:when>
                                    <c:otherwise>&nbsp;</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <html-el:link action="/phr/PhrMessage?&method=delete&id=${actionMessage.id}">Remove</html-el:link>
                            </td>
                        </tr>
                    </c:forEach>
                    
                    <c:forEach var="sMessage" items="${indivoSentMessages}">
                        <tr class="normal">
                            <td width="30">
                               
                            &nbsp;
                            
                            </td>
                            <td width="75">
                                Sent
                            </td>
                            <td>
                                <c:out value="${sMessage.receiverPhr}"/>
                                
                            </td>
                            <td>
                                
                                <html-el:link action="/phr/PhrMessage?method=read&noreply=yes&comingfrom=viewSentMessages&id=${sMessage.id}"  >
                                   <c:if test='${not empty sMessage.docSubject}'>
                                    <c:out value="${sMessage.docSubject}"/>
                                    </c:if>
                                    <c:if test='${empty sMessage.docSubject}'>
                                    No Subject
                                    </c:if>
                                </html-el:link>
                                
                            </td>
                            <td> 
                                <fmt:formatDate value="${sMessage.dateSent}" type="DATE" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td>
                                &nbsp;
                            </td>
                            <td>
                                &nbsp;
                            </td>
                        </tr>
                    </c:forEach> 
 <%-- Archived ---------------------------------------------------------- --%>
                    <c:forEach var="archivedMessage" items="${indivoArchivedMessages}">
                        <tr class="normal">
                            <td width="30">
                               
                            <c:if test="${archivedMessage.replied}" > --> </c:if>
                            
                            </td>
                            <td width="75">
                                <c:choose>
                                   <c:when test="${archivedMessage.read}">read</c:when>
                                   <c:otherwise>new</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:out value="${archivedMessage.senderPhr}"/></td>
                            <td>
                                
                                <html-el:link action="/phr/PhrMessage?&method=read&comingfrom=viewArchivedMessages&id=${archivedMessage.id}"  >
                                   <c:if test='${not empty archivedMessage.docSubject}'>
                                    <c:out value="${archivedMessage.docSubject}"/>
                                   </c:if>
                                   <c:if test='${empty archivedMessage.docSubject}'>
                                    No Subject
                                   </c:if>
                                </html-el:link>
                                
                            </td>
                            <td> 
                                <fmt:formatDate value="${archivedMessage.dateSent}" type="DATE" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td>
                                <html-el:link action="/phr/PhrMessage?&method=unarchive&id=${archivedMessage.id}"  >
                                   Unarchive
                                </html-el:link>
                            </td>
                        </tr>
                    </c:forEach>               
 <%-- Inbox-------------------------------------------------------------- --%>
                    
                <nested:notEmpty name="indivoMessages">
                    <% ArrayList<PHRMessage>phrMsgs = (ArrayList<PHRMessage>)session.getAttribute("indivoMessageBodies");%>
                    <nested:iterate indexId="msgIdx" id="iMessage" name="indivoMessages" type="org.oscarehr.phr.model.PHRDocument">                           
                        <tr <c:choose>
                                <c:when test="${iMessage.read}">class="normal"</c:when>
                                <c:otherwise>class="new"</c:otherwise>
                            </c:choose>>
                            <td bgcolor="#EEEEFF" width="30">
                               
                            <c:if test="${iMessage.replied}" > --> </c:if>
                            
                            </td>
                            <td bgcolor="#EEEEFF" width="75">
                                <c:choose>
                                   <c:when test="${iMessage.read}">read</c:when>
                                   <c:otherwise>new</c:otherwise>
                                </c:choose>
                            </td>
                            <td bgcolor="#EEEEFF">                                
                                
                                <a href="javascript: function myFunction() {return false; }" onClick="gotoEchart(<c:out value="${iMessage.senderDemographicNo}"/>, '<%=URLEncoder.encode(phrMsgs.get(msgIdx).getBody().replaceAll("\n","<br>"))%>');" >
                                    <c:out value="${iMessage.senderPhr}"/>
                                </a>
                            </td>
                            <td bgcolor="#EEEEFF">
                                
                                <html-el:link action="/phr/PhrMessage?&method=read&comingfrom=viewMessages&id=${iMessage.id}"  >
                                   <c:if test='${not empty iMessage.docSubject}'>
                                    <c:out value="${iMessage.docSubject}"/>
                                   </c:if>
                                   <c:if test='${empty iMessage.docSubject}'>
                                    No Subject
                                   </c:if>
                                </html-el:link>
                                
                            </td>
                            <td bgcolor="#EEEEFF"> 
                                <fmt:formatDate value="${iMessage.dateSent}" type="DATE" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td>
                                <html-el:link action="/phr/PhrMessage?&method=archive&id=${iMessage.id}"  >
                                   Archive
                                </html-el:link>
                            </td>
                        </tr>
                    </nested:iterate> 
                 </nested:notEmpty>
                </table>
            </td>
        </tr>
        <tr>
            <script type="text/javascript" src="../../share/javascript/boxover.js"></script>
            <td class="MainTableBottomRowLeftColumn">
            </td>
            <td class="MainTableBottomRowRightColumn">
            </td>
        </tr>
        
    </table>
    </body>
    <script type="text/javascript" language="JavaScript">
        <phr:IfTimeToExchange>
        //phrExchangeGo('../../phrExchange.do');
        setStatus("Loading messages...");
        new Ajax.Request('../../phrExchange.do',
          {
            method:'get',
            onSuccess: function(transport){
              var response = transport.responseText || "0";
              setStatus("");
              reloadWindow();
            },
            onFailure: function(){ 
              setStatus("");
              reloadWindow();
            }
          });
        </phr:IfTimeToExchange>
      </script>
</html>
