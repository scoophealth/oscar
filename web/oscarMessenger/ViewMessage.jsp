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
<%@ page language="java" import="oscar.oscarDemographic.data.*, java.util.Enumeration" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html locale="true">
<head>

<link rel="stylesheet" type="text/css" href="encounterStyles.css" media="screen">
<link rel="stylesheet" type="text/css" href="printable.css" media="print">    
    
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
oscar.oscarMessenger.util.MsgDemoMap msgDemoMap = new oscar.oscarMessenger.util.MsgDemoMap();
java.util.Hashtable demoMap = msgDemoMap.getDemoMap((String) request.getAttribute("viewMessageId"));
String boxType = request.getParameter("boxType");
%>

<title>
<bean:message key="oscarMessenger.ViewMessage.title"/>
</title>


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

function popup(demographicNo, msgId, providerNo, action) { //open a new popup window
  var vheight = 700;
  var vwidth = 980;  
  
  if (demographicNo!=null &&  demographicNo!="" ){
      //alert("demographicNo is not null!");
      windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";    
      var page = "";
      if ( action == "writeToEncounter") {
          page = 'WriteToEncounter.do?demographic_no='+demographicNo+'&msgId='+msgId+'&providerNo='+providerNo;
          var popUp=window.open(page, "<bean:message key="provider.appointmentProviderAdminDay.apptProvider"/>", windowprops);
          if (popUp != null) {
            if (popUp.opener == null) {
              popUp.opener = self; 
            }
            popUp.focus();
          }

      }
      else if ( action == "linkToDemographic"){
          page = 'ViewMessage.do?linkMsgDemo=true&demographic_no='+demographicNo+'&messageID='+msgId+'&providerNo='+providerNo;
          window.location = page;
      }
  }
  
}



function popupSearchDemo(keyword){ // open a new popup window
    var vheight = 700;
    var vwidth = 980;  
    windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";    
    var page = 'msgSearchDemo.jsp?keyword=' +keyword +'&firstSearch='+true;
    var popUp=window.open(page, "msgSearchDemo", windowprops);
    if (popUp != null) {
        if (popUp.opener == null) {
          popUp.opener = self; 
        }
        popUp.focus();
    }
}
</script>

</head>

<body class="BodyStyle" vlink="#0000FF" >
<html:form action="/oscarMessenger/HandleMessages" >

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
            <td class="MainTableRightColumn Printable">
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
                                            <a href="javascript:window.print()" class="messengerButtons"><bean:message key="oscarMessenger.ViewMessage.btnPrint"/></a>
                                        </td></tr></table>
                                    </td>
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3 ><tr><td class="messengerButtonsA">
                                            <html:link page="/oscarMessenger/DisplayMessages.jsp" styleClass="messengerButtons">
                                             <bean:message key="oscarMessenger.ViewMessage.btnInbox"/>
                                            </html:link>
                                        </td></tr></table>
                                    </td>  
                                    <%
                                        if( boxType.equals("1") ) {
                                        
                                    %>
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3 ><tr><td class="messengerButtonsA">
                                            <html:link page="/oscarMessenger/DisplayMessages.jsp?boxType=1" styleClass="messengerButtons">
                                             <bean:message key="oscarMessenger.ViewMessage.btnSent"/>
                                            </html:link>
                                        </td></tr></table>
                                    </td>  
                                    <%
                                        }
                                    %>                                                                                                          
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
                        <td class="Printable">
                            <table border="0" cellspacing="1" valign="top">
                                <tr>
                                    <td class="Printable" bgcolor="#DDDDFF">
                                    <bean:message key="oscarMessenger.ViewMessage.msgFrom"/>:
                                    </td>
                                    <td class="Printable" bgcolor="#CCCCFF">
                                    <%= request.getAttribute("viewMessageSentby") %>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="Printable" bgcolor="#DDDDFF">
                                    <bean:message key="oscarMessenger.ViewMessage.msgTo"/>:
                                    </td>
                                    <td class="Printable" bgcolor="#BFBFFF">
                                    <%= request.getAttribute("viewMessageSentto") %>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="Printable" bgcolor="#DDDDFF">
                                        <bean:message key="oscarMessenger.ViewMessage.msgSubject"/>:
                                    </td>
                                    <td class="Printable" bgcolor="#BBBBFF">
                                        <%= request.getAttribute("viewMessageSubject") %>
                                    </td>
                                </tr>

                                <tr>
                                  <td class="Printable" bgcolor="#DDDDFF">
                                      <bean:message key="oscarMessenger.ViewMessage.msgDate"/>:
                                  </td>
                                  <td  class="Printable" bgcolor="#B8B8FF">
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
                                <%  
                                    String pdfAttach = (String) request.getAttribute("viewMessagePDFAttach");
                                    if ( pdfAttach != null && pdfAttach.equals("1") ){
                                    %>
                                    <tr>
                                        <td bgcolor="#DDDDFF">
                                            <bean:message key="oscarMessenger.ViewMessage.msgAttachments"/>:
                                        </td>
                                        <td bgcolor="#B8B8FF">
                                            <a href="javascript:popupViewAttach(700,960,'ViewPDFAttach.do?attachId=<%=id%>')">
                                            <bean:message key="oscarMessenger.ViewMessage.btnAttach"/>
                                            </a>
                                        </td>
                                    </tr>
                                    <%
                                    }
                                %>
                                        
                                <tr>
                                    
                                    <td bgcolor="#EEEEFF" ></td>
                                    <td bgcolor="#EEEEFF" >
                                        <textarea name="Message" wrap="hard" readonly="true" rows="18" cols="60" ><%= request.getAttribute("viewMessageMessage") %></textarea><br>
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
                                    </td>
                                    
                                </tr>
                                <tr>
                                    <td bgcolor="#EEEEFF" ></td>
                                    <td bgcolor="#EEEEFF" >&nbsp;</td>
                                </tr>
                                  

                                <tr>
                                    <td bgcolor="#B8B8FF" ></td>
                                    <td bgcolor="#B8B8FF" ><font style="font-weight:bold">Link this message to ...</font></td>
                                </tr>
                                
                                <tr>
                                    <td bgcolor="#EEEEFF" ></td>             
                                    <td bgcolor="#EEEEFF" >
                                        <input type="text" name="keyword" size="30" />
                                        <input type="hidden" class="ControlPushButton" name="demographic_no" />
                                        <input type="button" class="ControlPushButton" name="searchDemo" value="Search Demographic" onclick="popupSearchDemo(document.forms[0].keyword.value)" />
                                        
                                    </td>
                                    
                                </tr>
                                <tr>
                                    <td bgcolor="#EEEEFF" ></td>
                                    <td bgcolor="#EEEEFF" ><font style="font-weight:bold">Selected Demographic</font></td>
                                </tr>

                               <%

                                String demographic_no = request.getParameter("demographic_no");
                                DemographicData demoData = new  DemographicData();
                                DemographicData.Demographic demo =  demoData.getDemographic(demographic_no);
                                String demoName = "";
                                if ( demo != null ) {
                                    demoName = demo.getLastName()+", "+demo.getFirstName();
                                                                       
                                } %> 
                                <tr>
                                    <td bgcolor="#EEEEFF" ></td>             
                                    <td bgcolor="#EEEEFF" >
                                        <input type="text" name="selectedDemo" size="30" readonly style="background:#EEEEFF;border:none" value="none"/>
                                        <script>
                                            if ( "<%=demoName%>" != "null" && "<%=demoName%>" != "") {
                                                document.forms[0].selectedDemo.value = "<%=demoName%>"
                                                document.forms[0].demographic_no.value = "<%=demographic_no%>"
                                            }
                                        </script>
                                           <input type="button" class="ControlPushButton" name="linkDemo" value="Link to demographic" onclick="popup(document.forms[0].demographic_no.value,'<%=request.getAttribute("viewMessageId")%>','<%=request.getAttribute("providerNo")%>','linkToDemographic')" />
                                        
                                           <input type="button" class="ControlPushButton" name="clearDemographic" value="Clear selected demographic" onclick='document.forms[0].demographic_no.value = ""; document.forms[0].selectedDemo.value = "none"'  />
                                    </td>
                                                                        
                                </tr> 
                                                      
                            
                                <tr>
                                    <td bgcolor="#EEEEFF">
                                    </td>
                                    <td bgcolor="#EEEEFF" >
                                        <font style="font-weight:bold">Demographic(s) linked to this message</font>
                                    </td>
                                </tr>
                                
   
                                <% if(demoMap !=null){ %>
                                             
                                        <% for (Enumeration e=demoMap.keys(); e.hasMoreElements(); ) { 
                                            String demoID = (String)e.nextElement(); %>
                                        <tr>
                                            <td bgcolor="#EEEEFF"></td>
                                            <td bgcolor="#EEEEFF">        
                                            <input type="text" size="30" readonly style="background:#EEEEFF;border:none" value="<%=(String)demoMap.get(demoID)%>"/>

<a href="../oscarEncounter/Index.jsp?demographic_no=<%=demoID%>&providerNo=<%=request.getAttribute("providerNo")%>">E</a>
<a href="../oscarRx/SearchDrug.jsp?demographic_no=<%=demoID%>&providerNo=<%=request.getAttribute("providerNo")%>">Rx</a>
<a href="../demographic/demographiccontrol.jsp?demographic_no=<%=demoID%>&displaymode=edit&dboperation=search_detail">M</a>

                                            <input type="button" class="ControlPushButton" name="writeEncounter" value="Write to encounter" onclick="popup( '<%=demoID%>','<%=request.getAttribute("viewMessageId")%>','<%=request.getAttribute("providerNo")%>','writeToEncounter')" />                                            
                                            </td>
                                        </tr>
                                        <%}
                                    }
                                else{%>
                                <tr>
                                    <td bgcolor="#EEEEFF"></td>
                                    <td bgcolor="#EEEEFF">
                                    No demographic is linked to this message
                                    </td>
                                </tr>
                                <%}%>                                
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
<%  String bodyTextAsHTML = (String) request.getAttribute("viewMessageMessage");
    bodyTextAsHTML = bodyTextAsHTML.replaceAll("\n|\r\n?","<br/>"); %>
<p class="NotDisplayable Printable"><%= bodyTextAsHTML %></p>
</body>

</html:html>
