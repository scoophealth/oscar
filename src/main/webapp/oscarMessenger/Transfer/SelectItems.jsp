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

<%@ page import=" java.util.*, org.w3c.dom.*"%>
<%@ page
	import="oscar.oscarMessenger.docxfer.send.*,oscar.oscarMessenger.docxfer.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_msg" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_msg");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>



<%
String demoNo   = request.getParameter("val2");
String prov     = request.getParameter("val1");

oscar.oscarMessenger.pageUtil.MsgSessionBean bean = null;
bean = new oscar.oscarMessenger.pageUtil.MsgSessionBean();

bean.setProviderNo(prov);



    bean.estUserName();
    request.getSession().setAttribute("msgSessionBean", bean);



%>




<link rel="stylesheet" type="text/css" href="../encounterStyles.css">

<%@page import="org.oscarehr.util.MiscUtils"%><html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

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
                if(s.search('plus.gif')>-1)
                {
                    img.src = s.replace('plus.gif', 'minus.gif');
                }
                else
                {
                    img.src = s.replace('minus.gif', 'plus.gif');
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

            if(nod.src.search('plus.gif')>-1)
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

            if(nod.src.search('minus.gif')>-1)
            {
                nod.click();
            }
        }
    }

    function chkClick()
    {
        event.cancelBubble = true;
    }
</script>
<%
    Document xmlDoc = null;

    try{
        int demographicNo = Integer.parseInt(demoNo);

        MsgGenerate gen = new MsgGenerate();

        xmlDoc = gen.getDocument(demographicNo);
    }catch (Exception ex){
    	MiscUtils.getLogger().error("Error", ex);
        response.sendRedirect("error.html");
    }

    if(xmlDoc == null){
        response.sendRedirect("error.html");
    }

    Element root = xmlDoc.getDocumentElement();

%>
<%!
    String spanStartRoot = "<span class=\"treeNode\" onclick=\"javascript:showTbl('tblRoot',event);\">"
        + "<img class=\"treeNode\" src=\"img/minus.gif\" border=\"0\" />";

    String spanStart = "<span class=\"treeNode\" onclick=\"javascript:showTbl('tblNode',event);\">"
        + "<img class=\"treeNode\" src=\"img/plus.gif\" border=\"0\" />";
    String spanEnd = "</span>";

    String tblStartRoot = "<table class=\"treeTable\" id=\"tblRoot\" cellspacing=0 cellpadding=3>";
    String tblStart = "<table class=\"treeTable\" id=\"tblNode\" style=\"display:none\" cellspacing=0 cellpadding=3>";
    String tblStartContent = "<table class=\"content\" id=\"tblNode\" style=\"display:none\" cellspacing=0 cellpadding=3>";

    String tblRowStart = "<tr><td>";
    String tblRowEnd = "</td></tr>";

    String tblEnd = "</table>";

    void DrawDoc(Element root, JspWriter out)
            throws javax.servlet.jsp.JspException, java.io.IOException{

        out.print(spanStartRoot + "Document Transfer" + spanEnd);
        out.print(tblStartRoot);

        NodeList lst = root.getChildNodes();
        for(int i=0; i<lst.getLength(); i++){
            out.print(tblRowStart);
            DrawTable((Element)lst.item(i), out);
            out.print(tblRowEnd);
        }

        out.print(tblEnd);
    }

    void DrawTable(Element tbl, JspWriter out)
            throws javax.servlet.jsp.JspException, java.io.IOException{
        NodeList lst = tbl.getChildNodes();

        out.print(spanStart + tbl.getAttribute("name") + spanEnd);
        out.print(tblStart);

        for(int i=0; i<lst.getLength(); i++){
            out.print(tblRowStart);
            DrawItem((Element)lst.item(i), out);
            out.print(tblRowEnd);
        }
        out.print(tblEnd);
    }

    void DrawItem(Element item, JspWriter out)
            throws javax.servlet.jsp.JspException, java.io.IOException{
        out.print(spanStart);
        if(! item.getAttribute("removable").equalsIgnoreCase("false")){
            String sName = "item" + item.getAttribute("itemId");
            out.print("<input type=checkbox name='" + sName + "' onclick='javascript:chkClick();'/>");
        }
        out.print(item.getAttribute("name") + ": " + item.getAttribute("value") + spanEnd);
        out.print(tblStartContent);

        NodeList lst = item.getChildNodes();
        for(int i=0; i<lst.getLength(); i++){
            if(lst.item(i).getNodeType()==Node.ELEMENT_NODE){
                if(((Element)lst.item(i)).getTagName().equals("content")){
                    DrawContent((Element)lst.item(i), out);
                }
            }
        }
        out.print(tblEnd);
    }

    void DrawContent(Element content, JspWriter out)
            throws javax.servlet.jsp.JspException, java.io.IOException{
        NodeList lst = content.getChildNodes();
        for(int i=0; i<lst.getLength(); i++){
            if(lst.item(i).getNodeType()==Node.ELEMENT_NODE){
                Element fld = (Element)lst.item(i);
                if(fld.getTagName().equals("fld")){
                    out.print("<tr><td style='font-weight:bold'>");
                    out.print(fld.getAttribute("name") + ": ");
                    out.print("</td><td>");
                    out.print(fld.getAttribute("value"));
                    out.print("</td></tr>");
                }
            }
        }
    }
%>
<title>Document Transfer</title>
</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">oscarComm</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Document Transfer</td>
				<td></td>
				<td style="text-align: right"><oscar:help keywords="message" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'About.jsp')">About</a> | <a
					href="javascript:popupStart(300,400,'License.jsp')">License</a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn">&nbsp;</td>
		<td class="MainTableRightColumn">
		<%
//                <table width="100%" cellspacing=0 cellpadding=0>
//                    <tr>
//                        <td>
//                            <div class="DivContentTitle">Document Transfer</div>
//                        </td>
//                        <td align=right>
//                            <a href="javascript:window.close();">Close</a>
//                        </td>
//                    </tr>
//                </table>
%>
		<hr style="color: #A9A9A9;">
		Please Select the eDocs you would like to transfer for this patient.
		Items without a check box will be sent by default.
		<div style="height: 6px;"></div>

		<form method="POST" action="PostItems.jsp"><input type=hidden
			name="xmlDoc"
			value="<%= MsgCommxml.encode64(MsgCommxml.toXML(root)) %>" /> <% DrawDoc(root, out); %>
		<br>
		<input type=submit value="Send These eDocs" /></form>

		<div style="font-size: 8pt; margin-top: 15px;"><a
			href="javascript:expandAll();">Expand All</a> &nbsp;|&nbsp; <a
			href="javascript:collapseAll();">Collapse All</a></div>

		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html>
