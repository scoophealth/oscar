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
<%@ page import="org.w3c.dom.*" %>
<%@ page import="oscar.oscarMessenger.util.Msgxml"%>

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
oscar.oscarMessenger.data.MsgAddressBook addressBook = new oscar.oscarMessenger.data.MsgAddressBook();
String myAddressBookXmlString = addressBook.myAddressBook();
java.util.Vector xmlVector    = addressBook.remoteAddressBooks();
String CurrentLocationName    = addressBook.CurrentLocationName;
String[] theProviders;
theProviders = new String[] {};
java.util.Vector locationVect = new java.util.Vector();
oscar.oscarMessenger.data.MsgReplyMessageData reData = new oscar.oscarMessenger.data.MsgReplyMessageData();
%>



<link rel="stylesheet" type="text/css" href="encounterStyles.css">
<html>
<head>
<title>
createMessage
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

<style type="text/css">
    BODY
    {
        font-family: Verdana, Tahoma, Arial, sans-serif;
        font-size: 10pt;
        text-decoration: none;
    }

    SPAN.treeNode
    {
        font-size: 10pt;
        font-weight: bold;
        cursor: hand;
    }

    IMG.treeNode
    {
        vertical-align: middle;
    }

    IMG.collapse
    {
        cursor: hand;
        margin-left: 10px;
    }


    TABLE.treeTable
    {
        margin-left: 15px;
    }

    TH.treeTable
    {
        font-weight: bold;
    }

    PRE
    {
        font-size: 9pt;
        font-weight: normal;
    }

    .content
    {
        margin-left: 15px;
        border-width: 1px;
        border-color: #A9A9A9;
        border-style: solid;
        //padding: 3px;
        background-color: #F5F5F5;
        font-size: 9pt;
    }

    .groupIndent
    {
        margin-left: 19px;
        /*border-width: 1px;
        border-color: #A9A9A9;
        border-style: solid;
        padding: 3px;
        background-color: #F5F5F5;*/
        font-size: 9pt;
    }

    .borderTop
    {
        border-top-width: 1px;
        border-top-color: #A9A9A9;
        border-top-style: solid;
    }
</style>

<script language="javascript">
    function checkGroup(tblName)
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
    function showTbl(tblName)
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
<!--
function validatefields(){
  if (document.forms[0].message.value.length == 0){
    alert("You have forgot to enter a message");
    return false;
  }
  val = validateCheckBoxes(document.forms[0]);
  if (val  == 0)
  {
    alert("No providers have been selected to send this message too");
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
       window.close();
}
//-->
</script>

</head>

<jsp:useBean id="docListBean" scope="session" class="oscar.oscarMessenger.pageUtil.MsgDocListForm" />
<% String lastName =null; int i = 0;%>




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
                            <bean:message key="createMessage.title"/>
                        </td>
                        <td  >
                            &nbsp;
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
                <table >

                    <tr>
                        <td>
                            <table cellspacing=3 >
                                <tr >
                                    <td >
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA">
                                            <html:link page="/oscarMessenger/DisplayMessages.jsp" styleClass="messengerButtons">
                                             <bean:message key="createMessage.displayMessages"/>
                                            </html:link>
                                        </td></tr></table>
                                    </td>
                                    <td >
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA">
                                            <html:link page="/oscarMessenger/ClearMessage.do" styleClass="messengerButtons">
                                             <bean:message key="createMessage.createHRef"/>
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
                            <html:form action="/oscarMessenger/CreateMessage" onsubmit="return validatefields()">
                                <table>
                                    <tr>
                                        <th bgcolor="#DDDDFF" width="75">
                                            Recipients
                                        </th>
                                        <th align="left" bgcolor="#DDDDFF">
                                            Message
                                        </th>
                                    </tr>
                                    <tr>
                                        <td bgcolor="#EEEEFF" valign=top>
                                            <table>
                                                <tr>
                                                    <td>
                                                        <input type="submit" class="ControlPushButton" value="Send Message" >
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
                                                            System.err.println("hey every one i have a "+reData);
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
                                                        <td> <!--list of the providers cell Start-->
                                                           <table>
                                                                <%if (xmlVector.size() > 0){%><!--the remotes-->
                                                                <tr>
                                                                    <td>
                                                                            <span class="treeNode" onclick="javascript:showTbl('tblREMO');">
                                                                                <img class="treeNode" src="img/plusblue.gif" border="0" />
                                                                                    RemoteLocations
                                                                            </span>

                                                                            <table class="treeTable" id="tblREMO" style="display:none" cellspacing=0 cellpadding=3>
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
                                                                                                    System.out.println(g+": pro = "+pData.providerNo+" remo ="+pData.locationId);
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
                                                                    <td>  <!-- the locals -->
                                                                    <%
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
                                        </td><!--list of the providers cell End-->
                                        <td bgcolor="#EEEEFF" valign=top>   <!--Message and Subject Cell-->
                                            Subject :
                                                <html:text name="msgCreateMessageForm" property="subject" size="67"/>
                                                <br><br>
                                                <html:textarea name="msgCreateMessageForm" property="message" cols="60" rows="18" />
                                                <%
                                                String att = bean.getAttachment();
                                                if (att != null){ %>
                                                    <br>Message has attachments!
                                                    <br>Only doctors will recieve your attachments!

                                                <% }

                                                %>
                                        </td>
                                    </tr>
                                </table>
                            </html:form>
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
</html>



<%!

 public void displayNodes(Node node,JspWriter out,int depth,String[] thePros,String CurrentLocationName ){
      depth++;

      Element element = (Element) node;
      System.out.println("desc = "+element.getAttribute("desc")+"<br>");
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
            out.print("<span class=\"treeNode\" onclick=\"javascript:showTbl('tblDFR"+(depth+1)+"');\">");

            if (depth < 2){
               out.print("<img class=\"treeNode\" src=\"img/minusblue.gif\" border=\"0\" />");
            }else{
               out.print("<img class=\"treeNode\" src=\"img/plusblue.gif\" border=\"0\" />");
            }
            out.print("</span>");
            if (depth == 1){
               out.print("<input type=\"checkbox\" name=tblDFR"+depth+" onclick=\"javascript:checkGroup('tblDFR"+(depth+1)+"');\"><font color=#0c7bd6><b>"+CurrentLocationName+"</b></font><br>");
            }else{
               out.print("<input type=\"checkbox\" name=tblDFR"+depth+" onclick=\"javascript:checkGroup('tblDFR"+(depth+1)+"');\"><font color=#0c7bd6><b>"+element.getAttribute("desc")+"</b></font><br>");
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
                  out.print("<img id=\"tblDFR"+depth+"\" class=\"collapse\"   onclick=\"javascript:showTbl('tblDFR"+(depth)+"');\" src=\"img/collapse.gif\" border=\"0\" />");
               else
                  out.print("<img id=\"tblDFR"+depth+"\" class=\"collapse\"  style=\"display:none\" onclick=\"javascript:showTbl('tblDFR"+(depth)+"');\" src=\"img/collapse.gif\" border=\"0\" />");

            }
         }

       }catch(Exception e){System.out.println("didn't work moron");}
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

