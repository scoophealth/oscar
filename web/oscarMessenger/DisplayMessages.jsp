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

<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%
int pageType = 0;
String boxType = request.getParameter("boxType");
if (boxType == null || boxType.equals("")){
    pageType = 0;
}else if (boxType.equals("1")){
    pageType = 1;
}else if (boxType.equals("2")){
    pageType = 2;
}else{
    pageType = 0;
}   //messageid
%>



<logic:notPresent name="msgSessionBean" scope="session">
    <logic:redirect href="index.jsp" />
</logic:notPresent>
<logic:present name="msgSessionBean" scope="session">
    <bean:define id="bean" type="oscar.oscarMessenger.pageUtil.MsgSessionBean" name="msgSessionBean" scope="session" />
    <logic:equal name="bean" property="valid" value="false">
        <logic:redirect href="index.jsp" />
    </logic:equal>
</logic:present>
<%
oscar.oscarMessenger.pageUtil.MsgSessionBean bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean)pageContext.findAttribute("bean");
%>
<jsp:useBean id="DisplayMessagesBeanId" scope="session" class="oscar.oscarMessenger.pageUtil.MsgDisplayMessagesBean" />
<% DisplayMessagesBeanId.setProviderNo(bean.getProviderNo());
bean.nullAttachment();%>
<jsp:setProperty name="DisplayMessagesBeanId" property="*" />
<jsp:useBean id="ViewMessageForm" scope="session" class="oscar.oscarMessenger.pageUtil.MsgViewMessageForm"/>





<html>
<head>
<html:base />
<link rel="stylesheet" type="text/css" href="encounterStyles.css">
<title>
DisplayMessages
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
                Messenger
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
                        <% String inbxStyle = "messengerButtonsA";
                           String sentStyle = "messengerButtonsA";
                           String delStyle  = "messengerButtonsA";
                        switch(pageType){
                            case 0: %>
     		                    <div class="DivContentTitle"><bean:message key="displayMessages.title"/></div>
                        <%      inbxStyle = "messengerButtonsD";
                            break;
                            case 1: %>
                                <div class="DivContentTitle"><bean:message key="displayMessages.sentTitle"/></div>
                        <%      sentStyle = "messengerButtonsD";
                            break;
                            case 2: %>
                                <div class="DivContentTitle"><bean:message key="displayMessages.deleteTitle"/></div>
                        <%      delStyle =  "messengerButtonsD";
                            break;
                        }%>
                        </td>
                        <td  >

                        </td>
                        <td style="text-align:right">
                                <a href="javascript:popupStart(300,400,'Help.jsp')"  >Help</a> | <a href="javascript:popupStart(300,400,'About.jsp')" >About</a> | <a href="javascript:popupStart(300,400,'License.jsp')" >License</a>
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
                                        <html:link page="/oscarMessenger/CreateMessage.jsp" styleClass="messengerButtons">
                                         <bean:message key="displayMessages.createHRef"/>
                                        </html:link>
                                        </td></tr></table>
                                    </td>
                                    <td >
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA">
                                        <html:link page="/oscarMessenger/DisplayMessages.jsp" styleClass="messengerButtons">
                                         <bean:message key="displayMessages.displayMessages"/>
                                        </html:link>
                                        </td></tr></table>
                                    </td>
                                    <td >
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA">
                                        <html:link page="/oscarMessenger/DisplayMessages.jsp?boxType=1" styleClass="messengerButtons">
                                         <bean:message key="displayMessages.displaySentMessages"/><!--sentMessage--link-->
                                        </html:link>
                                        </td></tr></table>
                                    </td>
                                    <td >
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA">
                                        <html:link page="/oscarMessenger/DisplayMessages.jsp?boxType=2" styleClass="messengerButtons">
                                         <bean:message key="displayMessages.displayDeletedMessages"/><!--deletedMessage--link-->
                                        </html:link>
                                        </td></tr></table>
                                    </td>
                                    <td >
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA">
                                        <a href="javascript:BackToOscar()" class="messengerButtons"><bean:message key="backToOscar.link"/></a>
                                        </td></tr></table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>



                    <tr>
                        <td>

                                <%String strutsAction = "/oscarMessenger/DisplayMessages";
                                    if (pageType == 2){
                                        strutsAction = "/oscarMessenger/ReDisplayMessages";
                                    }
                                %>

                         <html:form action="<%=strutsAction%>" >


                            <table border="0" width="80%" cellspacing="1">
                                <tr>
                                    <th bgcolor="#DDDDFF" width="75">
                                    &nbsp;
                                    </th>
                                    <th align="left" bgcolor="#DDDDFF">
                                    Status
                                    </th>
                                    <th align="left" bgcolor="#DDDDFF">
                                    From
                                    </th>
                                    <th align="left" bgcolor="#DDDDFF">
                                    Subject
                                    </th>
                                    <th align="left" bgcolor="#DDDDFF">
                                    Date
                                    </th>
                                </tr>
                                <% //java.util.Vector theMessages = new java.util.Vector() ;
                                   java.util.Vector theMessages2 = new java.util.Vector() ;
                                switch(pageType){
                                    case 0:
//                                        theMessages =  DisplayMessagesBeanId.getMessageid();
                                        theMessages2 = DisplayMessagesBeanId.estInbox();
                                        System.out.println("normal messages");
                                    break;
                                    case 1:
  //                                      theMessages  = DisplayMessagesBeanId.getSentMessageid();
                                        theMessages2 = DisplayMessagesBeanId.estSentItemsInbox();
                                        System.out.println("Sent messages");
                                    break;
                                    case 2:
    ///                                    theMessages  = DisplayMessagesBeanId.getDelMessageid();
                                        theMessages2 = DisplayMessagesBeanId.estDeletedInbox();
                                        System.out.println("deleted messages");
                                    break;
                                }   //messageid
                                %>

                            <%for (int i = 0; i < theMessages2.size() ; i++) {
                                        oscar.oscarMessenger.data.MsgDisplayMessage dm;
                                        dm = (oscar.oscarMessenger.data.MsgDisplayMessage) theMessages2.get(i);
                            %>
                                <tr>
                                    <td bgcolor="#EEEEFF" width="75">
                                    <%if (pageType != 1){%>
                                        <html:checkbox property="messageNo" value="<%=dm.messageId %>" />
                                        <% String atta = dm.attach;
                                            if (atta.equals("1")){
                                            %><img src="img/clip4.JPG"><%
                                            }
                                         %>
                                    <%}else{
                                            String atta = dm.attach;
                                            if (atta.equals("1")){
                                            %><img src="img/clip4.JPG"><%
                                            }


                                    %>
                                        &nbsp;
                                    <%}%>
                                    </td>
                                    <td bgcolor="#EEEEFF">
                                    <%= dm.status %>
                                    </td>
                                    <td bgcolor="#EEEEFF">
                                    <%= dm.sentby  %>
                                    </td>
                                    <td bgcolor="#EEEEFF">
                                    <a href="<%=request.getContextPath()%>/oscarMessenger/ViewMessage.do?messageID=<%=dm.messageId  %>">
                                        <%=dm.thesubject%>
                                    </a>

                                    </td>
                                    <td bgcolor="#EEEEFF">
                                    <%= dm.thedate  %>
                                    </td>
                                </tr>
                            <%}%>
                            </table>
                            <%if (pageType == 0){%>
                                        <input type="submit" value="delete">
                            <%}else if (pageType == 2){%>
                                        <input type="submit" value="undelete">
                            <%}%>
                         </html:form>
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
</body>
</html>
