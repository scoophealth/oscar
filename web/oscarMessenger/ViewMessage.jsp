<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="encounterStyles.css">
<html>
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
ViewMessage
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
</script>

</head>

<body class="BodyStyle" vlink="#0000FF" >
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
                            <bean:message key="displayMessages.title"/>
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
                <table>
                    <tr>
                        <td>
                            <table  cellspacing=3>
                                <tr>
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3 ><tr><td class="messengerButtonsA">
                                            <html:link page="/oscarMessenger/CreateMessage.jsp" styleClass="messengerButtons">
                                             <bean:message key="viewMessage.createHRef"/>
                                            </html:link>
                                        </td></tr></table>
                                    </td>
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3 ><tr><td class="messengerButtonsA">
                                            <html:link page="/oscarMessenger/DisplayMessages.jsp" styleClass="messengerButtons">
                                             <bean:message key="viewMessage.displayMessages"/>
                                            </html:link>
                                        </td></tr></table>
                                    </td>
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3 ><tr><td class="messengerButtonsA">
                                            <a href="javascript:BackToOscar()" class="messengerButtons"><bean:message key="backToOscar.link"/></a>
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
                                    From:
                                    </td>
                                    <td bgcolor="#CCCCFF">
                                    <%= request.getAttribute("viewMessageSentby") %>
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#DDDDFF">
                                    To:
                                    </td>
                                    <td bgcolor="#BFBFFF">
                                    <%= request.getAttribute("viewMessageSentto") %>
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#DDDDFF">
                                        Subject:
                                    </td>
                                    <td bgcolor="#BBBBFF">
                                        <%= request.getAttribute("viewMessageSubject") %>
                                    </td>
                                </tr>

                                <tr>
                                  <td bgcolor="#DDDDFF">
                                      Date:
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
                                            Attachments:
                                        </td>
                                        <td bgcolor="#B8B8FF">
                                            <a href="javascript:popupViewAttach(700,960,'ViewAttach.do?attachId=<%=id%>')">
                                            Click here to view attachment
                                            </a>
                                        </td>
                                    </tr>
                                    <%
                                    }
                                %>
                                        <html:form action="/oscarMessenger/HandleMessages" >
                                <tr>
                                    <td bgcolor="#EEEEFF" ></td>
                                    <td bgcolor="#EEEEFF" >
                                        <textarea name="Message" wrap="hard" readonly="true" rows="18" cols="60"><%= request.getAttribute("viewMessageMessage") %></textarea><br>
                                        <html:submit styleClass="ControlPushButton" property="reply" value="Reply" />
                                        <html:submit styleClass="ControlPushButton" property="replyAll" value="Reply All"/>
                                        <html:submit styleClass="ControlPushButton" property="forward" value="Forward"/>
                                        <html:submit styleClass="ControlPushButton" property="delete" value="Delete"/>
                                        <html:hidden property="messageNo" value="<%=(String)request.getAttribute(\"viewMessageNo\") %>"/>
                                    </td>
                                </tr>
                                    </html:form>
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
</html>
