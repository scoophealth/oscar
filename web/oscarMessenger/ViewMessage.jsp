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
if(session.getValue("user") == null)
    response.sendRedirect("../logout.jsp");
String providerview = request.getParameter("providerview")==null?"all":request.getParameter("providerview") ;
boolean bFirstDisp=true; //this is the first time to display the window
if (request.getParameter("bFirstDisp")!=null) bFirstDisp= (request.getParameter("bFirstDisp")).equals("true");
%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="encounterStyles.css">
<html:html locale="true">
<head>

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

<title>
<bean:message key="oscarMessenger.ViewMessage.title"/>
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

function popupViewAttach(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "oscarMVA", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}

function popup(demographicNo, msgId, providerNo) { //open a new popup window
  var vheight = 700;
  var vwidth = 980;  
  //alert("popup is called and the demographicNo is: " + demographicNo);
  if (demographicNo!=null){
      //alert("demographicNo is not null!");
      windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";    
      var page = '/oscar/oscarMessenger/WriteToEncounter.do?demographic_no='+demographicNo+'&msgId='+msgId+'&providerNo='+providerNo;
      var popUp=window.open(page, "<bean:message key="provider.appointmentProviderAdminDay.apptProvider"/>", windowprops);
      if (popUp != null) {
        if (popUp.opener == null) {
          popUp.opener = self; 
        }
        popUp.focus();
      }
  }
}
</script>

</head>

<body onload="popup(<%=request.getParameter("demographic_no")%>,<%=request.getAttribute("viewMessageId")%>,<%=request.getAttribute("providerNo")%>);" class="BodyStyle" vlink="#0000FF" >
<script type="text/javascript">
    //popup(<%=request.getParameter("demographic_no")%>,<%=request.getAttribute("viewMessageId")%>,<%=request.getAttribute("providerNo")%>);
</script>
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                <bean:message key="oscarMessenger.ViewMessage.msgMessenger"/>
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
            <td class="MainTableRightColumn">
                <table>
                    <tr>
                        <td>
                            <table  cellspacing=3>
                                <tr>
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3 ><tr><td class="messengerButtonsA">
                                            <html:link page="/oscarMessenger/CreateMessage.jsp" styleClass="messengerButtons">
                                             <bean:message key="oscarMessenger.ViewMessage.btnCompose"/>
                                            </html:link>
                                        </td></tr></table>
                                    </td>
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3 ><tr><td class="messengerButtonsA">
                                            <html:link page="/oscarMessenger/DisplayMessages.jsp" styleClass="messengerButtons">
                                             <bean:message key="oscarMessenger.ViewMessage.btnInbox"/>
                                            </html:link>
                                        </td></tr></table>
                                    </td>                                    
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3 ><tr><td class="messengerButtonsA">
                                            <a href="javascript:BackToOscar()" class="messengerButtons"><bean:message key="oscarMessenger.ViewMessage.btnExit"/></a>
                                        </td></tr></table>
                                    </td>                                    
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <table  cellspacing="1" valign="top">
                                <tr>
                                    <td bgcolor="#DDDDFF">
                                    <bean:message key="oscarMessenger.ViewMessage.msgFrom"/>:
                                    </td>
                                    <td bgcolor="#CCCCFF">
                                    <%= request.getAttribute("viewMessageSentby") %>
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#DDDDFF">
                                    <bean:message key="oscarMessenger.ViewMessage.msgTo"/>:
                                    </td>
                                    <td bgcolor="#BFBFFF">
                                    <%= request.getAttribute("viewMessageSentto") %>
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#DDDDFF">
                                        <bean:message key="oscarMessenger.ViewMessage.msgSubject"/>:
                                    </td>
                                    <td bgcolor="#BBBBFF">
                                        <%= request.getAttribute("viewMessageSubject") %>
                                    </td>
                                </tr>

                                <tr>
                                  <td bgcolor="#DDDDFF">
                                      <bean:message key="oscarMessenger.ViewMessage.msgDate"/>:
                                  </td>
                                  <td bgcolor="#B8B8FF">
                                      <%= request.getAttribute("viewMessageDate") %>&nbsp;&nbsp;
                                      <%= request.getAttribute("viewMessageTime") %>
                                  </td>
                                </tr>
                                <%  String attach = (String) request.getAttribute("viewMessageAttach");
                                    String id = (String) request.getAttribute("viewMessageId");
                                    if ( attach != null && attach.equals("1") ){
                                    %>
                                    <tr>
                                        <td bgcolor="#DDDDFF">
                                            <bean:message key="oscarMessenger.ViewMessage.msgAttachments"/>:
                                        </td>
                                        <td bgcolor="#B8B8FF">
                                            <a href="javascript:popupViewAttach(700,960,'ViewAttach.do?attachId=<%=id%>')">
                                            <bean:message key="oscarMessenger.ViewMessage.btnAttach"/>
                                            </a>
                                        </td>
                                    </tr>
                                    <%
                                    }
                                %>
                                        
                                <tr>
                                    <html:form action="/oscarMessenger/HandleMessages" >
                                    <td bgcolor="#EEEEFF" ></td>
                                    <td bgcolor="#EEEEFF" >
                                        <textarea name="Message" wrap="hard" readonly="true" rows="18" cols="60"><%= request.getAttribute("viewMessageMessage") %></textarea><br>
                                        <html:submit styleClass="ControlPushButton" property="reply">
					<bean:message key="oscarMessenger.ViewMessage.btnReply"/>
					</html:submit>
                                        <html:submit styleClass="ControlPushButton" property="replyAll">
 					<bean:message key="oscarMessenger.ViewMessage.btnReplyAll"/>
					</html:submit>
                                        <html:submit styleClass="ControlPushButton" property="forward">
					<bean:message key="oscarMessenger.ViewMessage.btnForward"/>
					</html:submit>
                                        <html:submit styleClass="ControlPushButton" property="delete">
					<bean:message key="oscarMessenger.ViewMessage.btnDelete"/>
					</html:submit>
                                        <html:hidden property="messageNo" value="<%=(String)request.getAttribute(\"viewMessageNo\") %>"/>                                   
                                    </html:form>
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#EEEEFF" ></td>
                                    <td bgcolor="#EEEEFF" >&nbsp;</td>
                                </tr>
                                <tr>
                                    <td bgcolor="#B8B8FF" ></td>
                                    <td bgcolor="#B8B8FF" ><font style="font-weight:bold"><bean:message key="oscarMessenger.ViewMessage.msgWriteThisMessageToEncounter"/></font></td>
                                </tr>                                    
                                <tr>
                                    <td bgcolor="#EEEEFF" ></td>
                                    <td bgcolor="#EEEEFF" >
                                        <form name="ADDAPPT" method="post" action="../appointment/appointmentcontrol.jsp">
                                            <bean:message key="oscarMessenger.ViewMessage.msgDemographicName"/>:
                                            <input type="TEXT" name="keyword" size="15" value="<%=bFirstDisp?"":request.getParameter("name").equals("")?session.getAttribute("appointmentname"):request.getParameter("name")%>">
                                            <input class="ControlPushButton" type="submit" name="Submit" value="<bean:message key="oscarMessenger.ViewMessage.btnSearchAndWriteToEncounter"/>">
                                        </td>
                                        <table style="display:none">
                                            <tr><td>                                                
                                            </tr></td>
                                        </table>                                        
                                        <input type="hidden" name="orderby" value="last_name" >
                                        <input type="hidden" name="search_mode" value="search_name" >
                                        <input type="hidden" name="messageId" value="<%=request.getAttribute("viewMessageNo")%>" >
                                        <input type="hidden" name="originalpage" value="../oscarMessenger/ViewMessage.do" >
                                        <input type="hidden" name="limit1" value="0" >
                                        <input type="hidden" name="limit2" value="5" >
                                        <!--input type="hidden" name="displaymode" value="TicklerSearch" -->
                                        <input type="hidden" name="displaymode" value="Search "> 

                                        <input type="hidden" name="dboperation" value="add_apptrecord">                                
                                        <input type="hidden" name="provider_no" value="115">                                
                                        </form>                                        
                                    </td>
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
</body>
</html:html>
