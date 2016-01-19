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

<%    
String providerview = request.getParameter("providerview")==null?"all":request.getParameter("providerview") ;
boolean bFirstDisp=true; //this is the first time to display the window
if (request.getParameter("bFirstDisp")!=null) bFirstDisp= (request.getParameter("bFirstDisp")).equals("true");
%>
<%@page import="java.util.Enumeration"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_msg" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_msg");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<link rel="stylesheet" type="text/css" href="encounterStyles.css"
	media="screen">
<link rel="stylesheet" type="text/css" href="printable.css"
	media="print">
	
<style>
.TopStatusBar{
width:100% !important;
height:100% !important;
}
</style>
	
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>

<logic:notPresent name="msgSessionBean" scope="session">
	<logic:redirect href="index.jsp" />
</logic:notPresent>
<logic:present name="msgSessionBean" scope="session">
	<bean:define id="bean"
		type="oscar.oscarMessenger.pageUtil.MsgSessionBean"
		name="msgSessionBean" scope="session" />
	<logic:equal name="bean" property="valid" value="false">
		<logic:redirect href="index.jsp" />
	</logic:equal>
</logic:present>
<%
oscar.oscarMessenger.pageUtil.MsgSessionBean bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean)pageContext.findAttribute("bean");

oscar.oscarMessenger.util.MsgDemoMap msgDemoMap = new oscar.oscarMessenger.util.MsgDemoMap();
java.util.Hashtable demoMap = msgDemoMap.getDemoMap((String) request.getAttribute("viewMessageId"));

String sPrevMsg = (String) request.getAttribute("viewMessagePosition");
String sNextMsg = (String) request.getAttribute("viewMessagePosition");


int iPrevMsg = sPrevMsg==null?0:Integer.parseInt(sPrevMsg) - 1;
int iNextMsg = sNextMsg==null?0:Integer.parseInt(sNextMsg) + 1;

String messageId = (String) request.getAttribute("viewMessageId");
String isLastMessage = (String) request.getAttribute("viewMessageIsLastMsg");
String orderBy = (String) request.getAttribute("orderBy");
String demographic_no = bean.getDemographic_no();
String provider_no = bean.getProviderNo();
String msgCount = request.getParameter("msgCount");
int iMsgCount =0;
if(msgCount!=null){
    iMsgCount = Integer.parseInt(msgCount);
}
%>

<title><bean:message key="oscarMessenger.ViewMessage.title" /></title>

<script type="text/javascript">
function BackToOscar()
{
    if (opener.callRefreshTabAlerts) {
	opener.callRefreshTabAlerts("oscar_new_msg");
        setTimeout("window.close()", 100);
    } else {
        window.close();
    }
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
      windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";    
      var page = 'WriteToEncounter.do?demographic_no='+demographicNo+'&msgId='+msgId+'&providerNo='+providerNo+'&encType=oscarMessenger';
      var popUp=window.open(page, "<bean:message key="provider.appointmentProviderAdminDay.apptProvider"/>", windowprops);
      if (popUp != null) {
        if (popUp.opener == null) {
          popUp.opener = self; 
        }
        popUp.focus();
      }
  }
   
}

//format and paste msg txt into open encounter
function paste2Encounter(demoNo) {
    
    var win = window.open("","apptProvider");
    var txt;
    var tmp;
    if( win.pasteToEncounterNote && win.demographicNo == demoNo ) {
        txt = "From: ";
        tmp = document.getElementById("sentBy").innerHTML;
        tmp = tmp.replace(/^\s+|\s+$/g,"");
        txt += tmp;
        txt += "\nTo: ";
        tmp = document.getElementById("sentTo").innerHTML;
        tmp = tmp.replace(/^\s+|\s+$/g,"");
        txt += tmp;
        txt += "\nDate: ";
        tmp = document.getElementById("sentDate").innerHTML;
        tmp = tmp.replace(/\s+|\n+/g,"");
        tmp = tmp.replace(/&nbsp;/g," ");
        txt += tmp;
        txt += "\nSubject: ";
        tmp = document.getElementById("msgSubject").innerHTML;
        tmp = tmp.replace(/^\s+|\s+$/g,"");
        txt += tmp;        
        txt += "\n";
        tmp = document.getElementById("msgBody").innerHTML;
        tmp = tmp.replace(/^\s+|\s+$/g,"");
        txt += tmp;
        
        win.pasteToEncounterNote(txt);
    }
    else {
        win.close();
        alert("Cannot find the Encounter window to paste into.\nSelect 'Write to Encounter' instead");
    }
    
    return false;
}

</script>

</head>

<body class="BodyStyle" vlink="#0000FF">

<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message
			key="oscarMessenger.ViewMessage.msgMessenger" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td><bean:message
					key="oscarMessenger.ViewMessage.msgViewMessage" /></td>
				<td></td>
				<td style="text-align: right">
				  <oscar:help keywords="message" key="app.top1"/> | 
				  <a href="javascript:void(0)" onclick="javascript:popupPage(600,700,'../oscarEncounter/About.jsp')"><bean:message key="global.about" /></a>
			    </td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn">&nbsp;</td>
		<td class="MainTableRightColumn Printable">
		<table>
			<tr>
				<td>
				<table cellspacing=3>
					<tr>
						<td>
						<table class=messButtonsA cellspacing=0 cellpadding=3>
							<tr>
								<td class="messengerButtonsA"><a
									href="javascript:BackToOscar()" class="messengerButtons"><bean:message
									key="oscarMessenger.ViewMessage.btnExit" /></a></td>
							</tr>
						</table>
						</td>
						<td>
						<table class=messButtonsA cellspacing=0 cellpadding=3>
							<tr>
								<td class="messengerButtonsA"><a
									href="javascript:window.print()" class="messengerButtons"><bean:message
									key="oscarMessenger.ViewMessage.btnPrint" /></a></td>
							</tr>
						</table>
						</td>
						<td>
						<%if(iPrevMsg>=0){%>
						<table class=messButtonsA cellspacing=0 cellpadding=3>
							<tr>
								<td class="messengerButtonsA"><a class="messengerButtons"
									href="<%=request.getContextPath()%>/oscarMessenger/ViewMessageByPosition.do?from=encounter&msgCount=<%=msgCount%>&orderBy=<%=orderBy%>&demographic_no=<%=request.getParameter("demographic_no")%>&messagePosition=<%=Integer.toString(iPrevMsg)%>">
								<< Prev Msg </a></td>
							</tr>
						</table>
						<%}%>
						</td>
						<td>
						<%if(iNextMsg < iMsgCount){%>
						<table class=messButtonsA cellspacing=0 cellpadding=3>
							<tr>
								<td class="messengerButtonsA"><a class="messengerButtons"
									href="<%=request.getContextPath()%>/oscarMessenger/ViewMessageByPosition.do?from=encounter&msgCount=<%=msgCount%>&orderBy=<%=orderBy%>&demographic_no=<%=request.getParameter("demographic_no")%>&messagePosition=<%=Integer.toString(iNextMsg)%>">
								Next Msg >> </a></td>
							</tr>
						</table>
						<%}%>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td class="Printable">
				<table cellspacing="1" valign="top">
					<tr>
						<td class="Printable" bgcolor="#DDDDFF"><bean:message
							key="oscarMessenger.ViewMessage.msgFrom" />:</td>
						<td id="sentBy" class="Printable" bgcolor="#CCCCFF"><%= request.getAttribute("viewMessageSentby") %>
						</td>
					</tr>
					<tr>
						<td class="Printable" bgcolor="#DDDDFF"><bean:message
							key="oscarMessenger.ViewMessage.msgTo" />:</td>
						<td id="sentTo" class="Printable" bgcolor="#BFBFFF"><%= request.getAttribute("viewMessageSentto") %>
						</td>
					</tr>
					<tr>
						<td class="Printable" bgcolor="#DDDDFF"><bean:message
							key="oscarMessenger.ViewMessage.msgSubject" />:</td>
						<td id="msgSubject" class="Printable" bgcolor="#BBBBFF"><%= request.getAttribute("viewMessageSubject") %>
						</td>
					</tr>

					<tr>
						<td class="Printable" bgcolor="#DDDDFF"><bean:message
							key="oscarMessenger.ViewMessage.msgDate" />:</td>
						<td id="sentDate" class="Printable" bgcolor="#B8B8FF"><%= request.getAttribute("viewMessageDate") %>&nbsp;&nbsp;
						<%= request.getAttribute("viewMessageTime") %></td>
					</tr>
					<%  String attach = (String) request.getAttribute("viewMessageAttach");
                                    String id = (String) request.getAttribute("viewMessageId");
                                    if ( attach != null && attach.equals("1") ){
                                    %>
					<tr>
						<td bgcolor="#DDDDFF"><bean:message
							key="oscarMessenger.ViewMessage.msgAttachments" />:</td>
						<td bgcolor="#B8B8FF"><a
							href="javascript:popupViewAttach(700,960,'ViewAttach.do?attachId=<%=id%>')">
						<bean:message key="oscarMessenger.ViewMessage.btnAttach" /> </a></td>
					</tr>
					<%
                                    }
                                %>

					<tr>
						<html:form action="/oscarMessenger/HandleMessages">
							<td bgcolor="#EEEEFF"></td>
							<td bgcolor="#EEEEFF"><textarea id="msgBody" name="Message"
								wrap="hard" readonly="true" rows="18" cols="60"><%= request.getAttribute("viewMessageMessage") %></textarea><br>

							</td>
						</html:form>
					</tr>
					<tr>
						<td bgcolor="#EEEEFF"></td>
						<td bgcolor="#EEEEFF">&nbsp;</td>
					</tr>
					<tr>
						<td bgcolor="#EEEEFF"></td>
						<td bgcolor="#EEEEFF"><font style="font-weight: bold">Demographic(s)
						linked to this message</font></td>
					</tr>
					<% if(demoMap !=null){ %>

					<% for (Enumeration e=demoMap.keys(); e.hasMoreElements(); ) { 
                                            String demoID = (String)e.nextElement(); %>
					<tr>
						<td bgcolor="#EEEEFF"></td>
						<td bgcolor="#EEEEFF"><%=(String)demoMap.get(demoID)%> <% if ( demoID.equals(demographic_no ) ) { %>
						<input
							onclick="javascript:popup(<%=demographic_no%>, <%=messageId%>, <%=provider_no%>);"
							class="ControlPushButton" type="button" name="writeToEncounter"
							value="Write To Encounter"> <input
							onclick="return paste2Encounter(<%=demographic_no%>);"
							class="ControlPushButton" type="button" name="pasteToEncounter"
							value="Paste To Encounter"> <% } %>
						</td>
					</tr>
					<%}
                                    }
                                else{%>
					<tr>
						<td bgcolor="#EEEEFF"></td>
						<td bgcolor="#EEEEFF">No demographic is linked to this
						message</td>
					</tr>
					<%}%>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
<!-- I don't like this next part, but it is a working hack for printing long messages.  A prefered solution would
 move away from textboxes entirely, so that there is more programmatic formating control. -->
<%  String bodyTextAsHTML = (String) request.getAttribute("viewMessageMessage");
    bodyTextAsHTML = bodyTextAsHTML.replaceAll("\n|\r\n?","<br/>"); %>
<p class="NotDisplayable Printable"><%= bodyTextAsHTML %></p>
</body>
</html:html>
