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

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="org.w3c.dom.*"%>
<%@ page import="oscar.oscarMessenger.util.Msgxml"%>
<%@ page import="oscar.oscarDemographic.data.*"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_msg" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_msg");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


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
oscar.oscarMessenger.data.MsgAddressBook addressBook = new oscar.oscarMessenger.data.MsgAddressBook();
String myAddressBookXmlString = addressBook.myAddressBook();
java.util.Vector xmlVector    = addressBook.remoteAddressBooks();
String CurrentLocationName    = addressBook.CurrentLocationName;
String[] theProviders;
theProviders = new String[] {};
java.util.Vector locationVect = new java.util.Vector();
oscar.oscarMessenger.data.MsgReplyMessageData reData = new oscar.oscarMessenger.data.MsgReplyMessageData();
boolean bFirstDisp=true; //this is the first time to display the window
if (request.getParameter("bFirstDisp")!=null) bFirstDisp= (request.getParameter("bFirstDisp")).equals("true");

String demographic_no = (String) request.getAttribute("demographic_no");

String subjectText = request.getParameter("subject");
if(subjectText == null) {
	if (request.getAttribute("ReSubject") != null){
		bean.setSubject((String)request.getAttribute("ReSubject"));
	}
}
else if (subjectText != null) {
	bean.setSubject(subjectText);
}

String messageText = request.getParameter("message");
if(messageText == null) {
	if (request.getAttribute("ReText") != null){
		bean.setMessage((String)request.getAttribute("ReText"));
	}
}
else if (messageText != null) {
	bean.setMessage(messageText);
}
%>
<%@page import="org.oscarehr.util.MiscUtils"%><html:html locale="true">
<head>
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js"></script>
<script src="<%=request.getContextPath()%>/js/fg.menu.js"></script>

<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="oscarMessenger.CreateMessage.title" />
</title>

<link rel="stylesheet" type="text/css" href="encounterStyles.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<style>
.TopStatusBar{
width:100% !important;
height:100% !important;
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

    function checkGroup(tblName,event)
    {
        var chk = event.srcElement;
        var newValue = chk.checked;
        var td = chk.parentNode;
        checkTD(td);

        function checkTD(element){
           if(element.tagName=='INPUT' && element.getAttribute('type')=='checkbox'){
              if(element.checked!=newValue){
                  element.checked=newValue;
              }
           }
           else{
              for(var i=0; i<element.childNodes.length; i++){
                 checkTD(element.childNodes[i]);
              }
           }
        }
    }
////////////////////////////////////////////////////////////////////////////////
    function showTbl(tblName,event)
    {
        var i;
        var span;
        if(event.srcElement.tagName=='SPAN')
        {
            span = event.srcElement;
        }
        else
        {
            if(event.srcElement.parentNode.tagName=='SPAN')
            {
                span = event.srcElement.parentNode;
            }
            else
            {
                if(event.srcElement.tagName=='IMG')
                {
                    span = event.srcElement.parentNode.getElementsByTagName('SPAN').item(0);
                }
            }
        }

        if(span != 'undefined')
        {
            var imgs = span.getElementsByTagName('IMG');
            if(imgs.length>0)
            {
                var img = imgs.item(0);
                var s = img.src;
                if(s.search('plusblue.gif')>-1)
                {
                    img.src = s.replace('plusblue.gif', 'minusblue.gif');
                }
                else
                {
                    img.src = s.replace('minusblue.gif', 'plusblue.gif');
                }
            }

            var nods = span.parentNode.childNodes;


            for(i=0; i<nods.length; i++)
            {
                var nod = nods.item(i);

                if(nod.id == tblName)
                {
                    if(nod.style.display=="none")
                    {
                        nod.style.display = "";
                    }
                    else
                    {
                        nod.style.display = "none";
                    }
                }
            }
        }
    }

    function expandAll()
    {
        var i;
        var root = document.all('tblRoot');

        var col = root.getElementsByTagName('IMG');

        for(i=0; i<col.length; i++)
        {
            var nod = col.item(i);

            if(nod.src.search('plusblue.gif')>-1)
            {
                nod.click();
            }
        }
    }

    function collapseAll()
    {
        var i;
        var root = document.all('tblRoot');

        var col = root.getElementsByTagName('IMG');

        for(i=0; i<col.length; i++)
        {
            var nod = col.item(i);

            if(nod.src.search('minusblue.gif')>-1)
            {
                nod.click();
            }
        }
    }
    
</script>



<script language="JavaScript">
function validatefields(){
  if (document.forms[0].message.value.length == 0){
    alert("<bean:message key="oscarMessenger.CreateMessage.msgEmptyMessage"/>");
    return false;
  }
  val = validateCheckBoxes(document.forms[0]);
  if (val  == 0)
  {
    alert("<bean:message key="oscarMessenger.CreateMessage.msgNoProvider"/>");
    return false;
  }
  return true
}

function validateCheckBoxes(form)
{
  var retval = "0";
  for (var i =0; i < form.provider.length;i++)
    if  (form.provider[i].checked)
      retval = "1";
  return retval
}

function checkAll(form){
  for (var i =0; i < form.provider.length;i++)
    if (!form.provider[i].checked)
      form.provider[i].checked = true;
}

function BackToOscar()
{
    if (opener.callRefreshTabAlerts) {
	opener.callRefreshTabAlerts("oscar_new_msg");
        setTimeout("window.close()", 100);
    } else {
        window.close();
    }
}

function XMLHttpRequestSendnArch() {
	var oRequest = new XMLHttpRequest();
	var theLink=document.referrer;
	var theLinkComponents=theLink.split('?');
	var theQueryComponents=theLinkComponents[1].split('&');

	for (index = 0; index < theQueryComponents.length; ++index) {
    		var theKeyValue=theQueryComponents[index].split('=');
		if(theKeyValue[0]=='messageID'){
			var theArchiveLink=theLinkComponents[0].substring(0,theLinkComponents[0].lastIndexOf('/'))+'/DisplayMessages.do?btnDelete=archive&messageNo='+theKeyValue[1];
		}
	}

	oRequest.open('GET', theArchiveLink, false);
	oRequest.send();
	document.forms[0].submit();
}


</script>

<script type="text/javascript">

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

function popupAttachDemo(demographic){ // open a new popup window
    var subject = document.forms[0].subject.value;
    var message = document.forms[0].message.value;
    var formData = "subject=" + subject + "&message=" + message;

    $.ajax({
    	type: "post",
    	data : formData,
    	success: function(data){
    		console.log(data);
    	},
    	error: function (jqXHR, textStatus, errorThrown){
 			alert("Error: " + textStatus);
    	}
	});

    var vheight = 700;
    var vwidth = 900;  
    windowprops = "height="+vheight+",width="+vwidth+",location=0,scrollbars=1,menubar=0,toolbar=1,resizable=1,screenX=0,screenY=0,top=0,left=0";    
    var page = 'attachmentFrameset.jsp?demographic_no=' +demographic;
    var demo_no  = demographic;
    
   
    if ( demographic == "" || !demographic || demographic == null || demographic == "null") {
        alert("Please select a demographic.");
    }
    else { 
        var popUp=window.open(page, "msgAttachDemo", windowprops);
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

<jsp:useBean id="docListBean" scope="session"
	class="oscar.oscarMessenger.pageUtil.MsgDocListForm" />
<% String lastName =null; int i = 0;%>




<body class="BodyStyle" vlink="#0000FF">

<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">
		<bean:message key="oscarMessenger.CreateMessage.msgMessenger" />
		</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td><bean:message key="oscarMessenger.CreateMessage.msgCreate" />
				</td>
				<td>&nbsp;</td>
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
		<td class="MainTableRightColumn">
		<table>

			<tr>
				<td>
				<table cellspacing=3>
					<tr>
						<td>
						<table class=messButtonsA cellspacing=0 cellpadding=3>
							<tr>
								<td class="messengerButtonsA"><html:link
									page="/oscarMessenger/DisplayMessages.jsp"
									styleClass="messengerButtons">
									<bean:message key="oscarMessenger.ViewMessage.btnInbox" />
								</html:link></td>
							</tr>
						</table>
						</td>
						<td>
						<table class=messButtonsA cellspacing=0 cellpadding=3>
							<tr>
								<td class="messengerButtonsA"><html:link
									page="/oscarMessenger/ClearMessage.do"
									styleClass="messengerButtons">
									<bean:message key="oscarMessenger.CreateMessage.btnClear" />
								</html:link></td>
							</tr>
						</table>
						</td>
						<td>
						<table class=messButtonsA cellspacing=0 cellpadding=3>
							<tr>
								<td class="messengerButtonsA"><a
									href="javascript:BackToOscar()" class="messengerButtons"><bean:message
									key="oscarMessenger.CreateMessage.btnExit" /></a></td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>

			<tr>
				<td>
				<table>


					<html:form action="/oscarMessenger/CreateMessage"
						onsubmit="return validatefields()">

						<tr>
							<th bgcolor="#DDDDFF" width="75"><bean:message
								key="oscarMessenger.CreateMessage.msgRecipients" /></th>
							<th align="left" bgcolor="#DDDDFF"><bean:message
								key="oscarMessenger.CreateMessage.msgMessage" /></th>
						</tr>
						<tr>
						
							<td bgcolor="#EEEEFF" valign=top>
							<table>
								<tr>
									<td><input type="submit" class="ControlPushButton"
										value="<bean:message key="oscarMessenger.CreateMessage.btnSendMessage"/>">
									</td>
									<td><input type="button" class="ControlPushButton"
										value="<bean:message key="oscarMessenger.CreateMessage.btnSendnArchiveMessage"/>" onClick="XMLHttpRequestSendnArch()">
									</td>
								</tr>
							</table>
							<div class="ChooseRecipientsBox">
							<table>
								<%
                                                        if ( request.getAttribute("ReMessage") != null){
                                                            oscar.oscarMessenger.pageUtil.MsgCreateMessageForm thisForm ;
                                                            thisForm = (oscar.oscarMessenger.pageUtil.MsgCreateMessageForm)request.getAttribute("msgCreateMessageForm");
                                                            thisForm.setSubject((String) request.getAttribute("ReSubject"));
                                                            thisForm.setMessage( (String) request.getAttribute("ReText"));
                                                            theProviders = (String[]) request.getAttribute("ReMessage");
                                                            java.util.Arrays.sort(theProviders);
                                                            reData = (oscar.oscarMessenger.data.MsgReplyMessageData) request.getAttribute("ProvidersClassObject");
                                                        }else if ( request.getAttribute("ReText") != null){
                                                            oscar.oscarMessenger.pageUtil.MsgCreateMessageForm thisForm ;
                                                            thisForm = (oscar.oscarMessenger.pageUtil.MsgCreateMessageForm)request.getAttribute("msgCreateMessageForm");
                                                            thisForm.setSubject((String) request.getAttribute("ReSubject"));
                                                            thisForm.setMessage( (String) request.getAttribute("ReText"));
                                                        }

                                                        if ( request.getAttribute("Relocation") != null ){
                                                            locationVect = (java.util.Vector) request.getAttribute("Relocation");
                                                        }
                                                     %>

								<tr>
									<td><!--list of the providers cell Start-->
									<table>
										<%if (xmlVector.size() > 0){%><!--the remotes-->
										<tr>
											<td><span class="treeNode"
												onclick="javascript:showTbl('tblREMO', event);"> <img
												class="treeNode" src="img/plusblue.gif" border="0" /> <bean:message
												key="oscarMessenger.CreateMessage.msgRemoteLocations" /> </span>

											<table class="treeTable" id="tblREMO" style="display: none"
												cellspacing=0 cellpadding=3>
												<%for (int j = 0; j < xmlVector.size() ; j++){/*this is the remotes*/%>
												<tr>
													<td>
													<%
                                                                                            String[] tmpPros = new String[] {};
                                                                                            java.util.ArrayList listy = reData.remoList;
                                                                                            if (listy != null){
                                                                                                for (int g = 0; g < listy.size(); g++){
                                                                                                    oscar.oscarMessenger.data.MsgProviderData pData;
                                                                                                    pData = (oscar.oscarMessenger.data.MsgProviderData) listy.get(g);
                                                                                                }
                                                                                            }else{
                                                                                                listy = new java.util.ArrayList();
                                                                                            }

                                                                                            Document xmlDoc = Msgxml.parseXML((String) xmlVector.elementAt(j));
                                                                                            if ( xmlDoc != null  ){
                                                                                              Element remoteAddressBook = xmlDoc.getDocumentElement();
                                                                                              NodeList lst = remoteAddressBook.getChildNodes();
                                                                                              for (int ii = 0; ii < lst.getLength(); ii++){
                                                                                                 Node firstnode = lst.item(ii);
                                                                                                 addressBook.displayRemoteNodes2(firstnode,out,0,reData,j,locationVect);
                                                                                              }
                                                                                            }
                                                                                        %>
													</td>
												</tr>
												<%}%>
											</table>
											</td>
										</tr>
										<%}/*if(xmlVec...*/%>
										<tr>
											<td><!-- the locals --> <%
                                                                        Document xmlDoc = Msgxml.parseXML(myAddressBookXmlString);
                                                                        Element myAddressBook = xmlDoc.getDocumentElement();
                                                                        NodeList lst = myAddressBook.getChildNodes();

                                                                        for (int ii = 0; ii < lst.getLength(); ii++){
                                                                           Node firstnode = lst.item(ii);
                                                                           displayNodes(firstnode,out,0,theProviders,CurrentLocationName);
                                                                        }
                                                                    %>
											</td>
										</tr>
									</table>
									</td>
								</tr>

							</table>
							</div>
							</td>
							<!--list of the providers cell End-->
							<td bgcolor="#EEEEFF" valign=top><!--Message and Subject Cell-->
							<bean:message key="oscarMessenger.CreateMessage.formSubject" /> :
							<html:text name="msgCreateMessageForm" property="subject"
								size="67" value="${bean.subject}"/> <br>
							<br>
							<html:textarea name="msgCreateMessageForm" property="message"
								cols="60" rows="18" value="${bean.message}"/> <%
                                                String att = bean.getAttachment();
                                                String pdfAtt = bean.getPDFAttachment();
                                                if (att != null || pdfAtt != null){ %>
							<br>
							<bean:message key="oscarMessenger.CreateMessage.msgAttachments" />

							<% 
							bean.setSubject(null);
							bean.setMessage(null);
							}

                                                %>
							</td>
						</tr>

						<tr>
							<td bgcolor="#B8B8FF"></td>
							<td bgcolor="#B8B8FF"><font style="font-weight: bold"><bean:message key="oscarMessenger.CreateMessage.msgLinkThisMessage" /></font></td>
						</tr>
                                                        
						<tr>
							<td bgcolor="#EEEEFF"></td>
							<td bgcolor="#EEEEFF">
                                                            <input type="text" name="keyword" size="30" /> <input type="hidden" name="demographic_no" value="<%=demographic_no%>" /> 
                                                            <input type="button" class="ControlPushButton" name="searchDemo" value="<bean:message key="oscarMessenger.CreateMessage.msgSearchDemographic" />" onclick="popupSearchDemo(document.forms[0].keyword.value)" />
                                                        </td>

						</tr>
						<tr>
							<td bgcolor="#EEEEFF"></td>
							<td bgcolor="#EEEEFF"><font style="font-weight: bold"><bean:message key="oscarMessenger.CreateMessage.msgSelectedDemographic" /></font></td>
						</tr>

						<%

                                    
                                    DemographicData demoData = new  DemographicData();
                                    org.oscarehr.common.model.Demographic demo =  demoData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demographic_no);
                                    String demoName = "";
                                    if ( demo != null ) {
                                        demoName = demo.getLastName()+", "+demo.getFirstName();

                                    } %>
						<tr>
							<td bgcolor="#EEEEFF"></td>
							<td bgcolor="#EEEEFF"><input type="text"
								name="selectedDemo" size="20" readonly
								style="background: #EEEEFF; border: none" value="none" /> <script>
                                                if ( "<%=demoName%>" != "null" && "<%=demoName%>" != "") {
                                                    document.forms[0].selectedDemo.value = "<%=demoName%>";
                                                    document.forms[0].demographic_no.value = "<%=demographic_no%>";
                                                }
                                            </script> <input type="button"
								class="ControlPushButton" name="clearDemographic"
								value="<bean:message key="oscarMessenger.CreateMessage.msgClearSelectedDemographic" />"
								onclick='document.forms[0].demographic_no.value = ""; document.forms[0].selectedDemo.value = "none"' />
							<input type="button" class="ControlPushButton" name="attachDemo"
								value="<bean:message key="oscarMessenger.CreateMessage.msgAttachDemographic" />"
								onclick="popupAttachDemo(document.forms[0].demographic_no.value)"
								style="display: " /></td>

						</tr>
					</html:form>
				</table>
				</td>
			</tr>
			<tr>
				<td><script language="JavaScript">
                            document.forms[0].message.focus();
                            </script></td>
			</tr>

		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>
		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>
</body>
</html:html>



<%!

 public void displayNodes(Node node,JspWriter out,int depth,String[] thePros,String CurrentLocationName ){
      depth++;

      Element element = (Element) node;
      try{
         if (depth > 2){
            if ((element.getTagName()).equals("group")){
               out.print("<table id=\"tblDFR"+depth+"\" style=\"display:none\" class=\"groupIndent\" cellpadding=\"0\" border=0 >\n");
            }
            else{
               if (node.getPreviousSibling() == null){
                  out.print("<table id=\"tblDFR"+depth+"\" style=\"display:none\" class=\"groupIndent\" cellpadding=\"0\" cellspacing=0  >\n");
               }
            }
         }else{
            if (depth == 1){
                out.print("<table id=\"tblDFR"+depth+"\" cellpadding=\"0\" border=0>\n");
            }else{
                out.print("<table id=\"tblDFR"+depth+"\" class=\"groupIndent\" cellpadding=\"0\" border=0>\n");
            }
         }
         out.print("   <tr> \n");
         out.print("      <td> \n");

         if ((element.getTagName()).equals("group")){
            out.print("<span class=\"treeNode\" onclick=\"javascript:showTbl('tblDFR"+(depth+1)+"',event);\">");

            if (depth < 2){
               out.print("<img class=\"treeNode\" src=\"img/minusblue.gif\" border=\"0\" />");
            }else{
               out.print("<img class=\"treeNode\" src=\"img/plusblue.gif\" border=\"0\" />");
            }
            out.print("</span>");
            if (depth == 1){
               out.print("<input type=\"checkbox\" name=tblDFR"+depth+" onclick=\"javascript:checkGroup('tblDFR"+(depth+1)+"',event);\"><font color=#0c7bd6><b>"+CurrentLocationName+"</b></font><br>");
            }else{
               out.print("<input type=\"checkbox\" name=tblDFR"+depth+" onclick=\"javascript:checkGroup('tblDFR"+(depth+1)+"',event);\"><font color=#0c7bd6><b>"+element.getAttribute("desc")+"</b></font><br>");
            }

         }else{

               if ( java.util.Arrays.binarySearch(thePros,element.getAttribute("id")) < 0 ){
                  out.print("<input type=\"checkbox\" name=provider value="+element.getAttribute("id")+"  > <font color=#0e8ef7>"+personTitler(element.getAttribute("desc"))+"</font>\n");
               }else{
                  out.print("<input type=\"checkbox\" name=provider value="+element.getAttribute("id")+" checked > "+personTitler(element.getAttribute("desc"))+"\n");
               }
         }
         if (node.hasChildNodes()){
            NodeList nlst = node.getChildNodes();
            for (int i = 0; i < nlst.getLength(); i++){
               displayNodes(nlst.item(i), out,depth,thePros,CurrentLocationName);
            }
         }
         out.print("</td>\n");
         out.print("</tr>\n");
         if ((element.getTagName()).equals("group") && !node.hasChildNodes()){
            out.print("</table id="+depth+">\n");
         }else{
            if (node.getNextSibling() == null){
               out.print("</table id="+depth+">\n");
               if (depth == 2)
                  out.print("<img id=\"tblDFR"+depth+"\" class=\"collapse\"   onclick=\"javascript:showTbl('tblDFR"+(depth)+"',event);\" src=\"img/collapse.gif\" border=\"0\" />");
               else
                  out.print("<img id=\"tblDFR"+depth+"\" class=\"collapse\"  style=\"display:none\" onclick=\"javascript:showTbl('tblDFR"+(depth)+"',event);\" src=\"img/collapse.gif\" border=\"0\" />");

            }
         }

       }catch(Exception e){
    	   MiscUtils.getLogger().error("Exception in CreateMessage.jsp.displayNodes():", e);
       }
   }//display nodes
    
%>

<%!
    public String personTitler(String name){
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("<a TITLE=\""+name+"\">");
            if(name.length() > 20){
                name = name.substring(0,17)+"...";
            }
            stringBuffer.append(name+"</a>");
            return stringBuffer.toString();
    }
%>
