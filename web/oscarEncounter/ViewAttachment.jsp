<%@ page language="java" %>
<%@ page import="oscar.oscarEncounter.immunization.data.*,oscar.util.UtilXML" %>
<%@ page import="oscar.oscarEncounter.immunization.pageUtil.*, java.util.*, org.w3c.dom.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%
    String remoteName = (String) request.getAttribute("remoteName");
    String themessage = (String) request.getAttribute("themessage");
    String theime     = (String) request.getAttribute("theime");
    String thedate    = (String) request.getAttribute("thedate");
    String attachment = (String) request.getAttribute("attachment");
    String thesubject = (String) request.getAttribute("thesubject");
    String sentBy     = (String) request.getAttribute("sentBy");
%>


<link rel="stylesheet" type="text/css" href="encounterStyles.css">
<html:html locale="true">
<head>
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
        margin-left: 15px;
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
        padding: 3px;
        background-color: #F5F5F5;
    }

    .borderTop
    {
        border-top-width: 1px;
        border-top-color: #A9A9A9;
        border-top-style: solid;
    }
</style>

<script language="javascript">
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

    xmlDoc = UtilXML.parseXML(attachment);

    Element root = xmlDoc.getDocumentElement();
    System.out.println(UtilXML.toXML(root));

%>
<%!
    String spanStartRoot = "<span class=\"treeNode\" onclick=\"javascript:showTbl('tblRoot');\">"
        + "<img class=\"treeNode\" src=\"graphics/minus.gif\" border=\"0\" />";

    String spanStart = "<span class=\"treeNode\" onclick=\"javascript:showTbl('tblNode');\">"
        + "<img class=\"treeNode\" src=\"graphics/plus.gif\" border=\"0\" />";
    String spanEnd = "</span>";

    String tblStartRoot = "<table class=\"treeTable\" id=\"tblRoot\" cellspacing=0 cellpadding=3>";
    String tblStart = "<table class=\"treeTable\" id=\"tblNode\" style=\"display:none\" cellspacing=0 cellpadding=3>";
    String tblStartContent = "<table class=\"content\" id=\"tblNode\" style=\"display:none\" cellspacing=0 cellpadding=3>";

    String tblRowStart = "<tr><td>";
    String tblRowEnd = "</td></tr>";

    String tblEnd = "</table>";

    void DrawDoc(Element root, JspWriter out)
            throws javax.servlet.jsp.JspException, java.io.IOException
    {
        out.print(spanStartRoot); %>
	Document Transfer
	<% out.print(spanEnd);
        out.print(tblStartRoot);

        NodeList lst = root.getChildNodes();
        for(int i=0; i<lst.getLength(); i++)
        {
            out.print(tblRowStart);
            DrawTable((Element)lst.item(i), out);
            out.print(tblRowEnd);
        }

        out.print(tblEnd);
    }

    void DrawTable(Element tbl, JspWriter out)
            throws javax.servlet.jsp.JspException, java.io.IOException
    {
        out.print(spanStart + tbl.getAttribute("name") + spanEnd);
        out.print(tblStart);

        NodeList lst = tbl.getChildNodes();
        for(int i=0; i<lst.getLength(); i++)
        {
            out.print(tblRowStart);
            DrawItem((Element)lst.item(i), out);
            out.print(tblRowEnd);
        }
        out.print(tblEnd);
    }

    void DrawItem(Element item, JspWriter out)
            throws javax.servlet.jsp.JspException, java.io.IOException
    {
        out.print(spanStart);
        if(! item.getAttribute("removable").equalsIgnoreCase("false"))
        {
            String sName = "item" + item.getAttribute("itemId");
            out.print("<input type=checkbox name='" + sName + "' onclick='javascript:chkClick();'/>");
        }
        out.print(item.getAttribute("name") + ": " + item.getAttribute("value") + spanEnd);
        out.print(tblStartContent);

        NodeList lst = item.getChildNodes();
        for(int i=0; i<lst.getLength(); i++)
        {
            if(lst.item(i).getNodeType()==Node.ELEMENT_NODE)
            {
                if(((Element)lst.item(i)).getTagName().equals("content"))
                {
                    DrawContent((Element)lst.item(i), out);
                }
            }
        }
        out.print(tblEnd);
    }

    void DrawContent(Element content, JspWriter out)
            throws javax.servlet.jsp.JspException, java.io.IOException
    {
        NodeList lst = content.getChildNodes();
        for(int i=0; i<lst.getLength(); i++)
        {
            if(lst.item(i).getNodeType()==Node.ELEMENT_NODE)
            {
                Element fld = (Element)lst.item(i);
                if(fld.getTagName().equals("fld"))
                {
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



<title>
<bean:message key="oscarEncounter.ViewAttachment.title"/>
</title>
</head>

<body class="BodyStyle" vlink="#0000FF" >
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="<bean:message key="oscarEncounter.ViewAttachment.msgEncounterTable"/>">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                <bean:message key="global.oscarComm"/>
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
                        <bean:message key="oscarEncounter.ViewAttachment.msgViewAtt"/>
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

                <table  cellspacing="1" valign="top">
                                <tr>
                                    <td bgcolor="#DDDDFF">
                                    <bean:message key="oscarEncounter.ViewAttachment.msgFrom"/>:
                                    </td>
                                    <td bgcolor="#CCCCFF">
                                    <%= sentBy%> <bean:message key="oscarEncounter.ViewAttachment.msgAt"/> <%=remoteName%>
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#DDDDFF">
                                        <bean:message key="oscarEncounter.ViewAttachment.msgSubject"/>:
                                    </td>
                                    <td bgcolor="#BBBBFF">
                                        <%= thesubject%>
                                    </td>
                                </tr>

                                <tr>
                                  <td bgcolor="#DDDDFF">
                                      <bean:message key="oscarEncounter.ViewAttachment.msgDate"/>:
                                  </td>
                                  <td bgcolor="#B8B8FF">
                                      <%= thedate %>&nbsp;&nbsp;
                                      <%= theime %>
                                  </td>
                                </tr>

                                <tr>
                                    <td bgcolor="#EEEEFF" ></td>
                                    <td bgcolor="#EEEEFF" >
                                        <textarea name="Message" wrap="hard" readonly="true" rows="18" cols="60"><%=themessage%></textarea>
                                    </td>
                                </tr>



                        </table>

                <hr style="color:#A9A9A9;">
                <div style="height:6px;"></div>
                <% DrawDoc(root, out); %>
                <div style="font-size: 8pt; margin-top: 15px;">
                    <a href="javascript:expandAll();"><bean:message key="oscarEncounter.ViewAttachment.msgExpandAll"/></a>
                    &nbsp;|&nbsp;
                    <a href="javascript:collapseAll();"><bean:message key="oscarEncounter.ViewAttachment.msgColapseAll"/></a>
                </div>

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
