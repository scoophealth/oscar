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
<%@ page import="java.util.*,oscar.oscarReport.data.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">
<%
if(session.getValue("user") == null)
    response.sendRedirect("../logout.htm");
  String curUser_no,userfirstname,userlastname;
  curUser_no = (String) session.getAttribute("user");
int count = 0;

String bgcolor = "#ddddff";
String sql = request.getParameter("textarea")==null?"":request.getParameter("textarea");
String pros = "";

oscar.oscarReport.data.RptByExampleData exampleData  = new oscar.oscarReport.data.RptByExampleData();
Properties proppies = (Properties)  request.getSession().getAttribute("oscarVariables");


String results = exampleData.exampleReportGenerate(sql, proppies)==null?"": exampleData.exampleReportGenerate(sql, proppies);
String resultText = exampleData.exampleTextGenerate(sql, proppies)==null?"": exampleData.exampleTextGenerate(sql, proppies);
%><html>
<script language="JavaScript" type="text/JavaScript">
<!--
function reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.pgW=innerWidth; document.pgH=innerHeight; onresize=reloadPage; }}
  else if (innerWidth!=document.pgW || innerHeight!=document.pgH) location.reload();
}
reloadPage(true);

function findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function showHideLayers() { //v6.0
  var i,p,v,obj,args=showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
}
//-->
</script>
<head>
<title>
Query By Examples
</title>
<style type="text/css">
   td.nameBox {
      border-bottom: 1pt solid #888888;
      font-family: tahoma, helvetica; ;
      font-size: 12pt;
   }
   td.sideLine {
      border-right: 1pt solid #888888;
   }
   td.fieldBox {
      font-family: tahoma, helvetica;
   }
   th.headerColor{
      font-family: tahoma, helvetica ;
      font-size:12pt;
      font-weight: bold;
      
      background-color: #C3D4E5
   }
   
   tr.rowColor1 {
         font-family: tahoma, helvetica ;
         font-size:10pt;
        
         background-color: #FFFFFF
         }
         tr.rowColor2 {
	          font-family: tahoma, helvetica ;
	          font-size:10pt;
	         
	          background-color: #EEEEFF
         }
</style>

<script type="text/javascript">
   var remote=null;

   function rs(n,u,w,h,x) {
      args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
      remote=window.open(u,n,args);
     // if (remote != null) {
     //    if (remote.opener == null)
     //        remote.opener = self;
     // }
     // if (x == 1) { return remote; }
   }

   function popupOscarFluConfig(vheight,vwidth,varpage) { //open a new popup window
     var page = varpage;
     windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
     var popup=window.open(varpage, "OscarFluConfig", windowprops);
     if (popup != null) {
       if (popup.opener == null) {
        popup.opener = self;
       }
    }
  }
</script>

</head>

<body vlink="#0000FF" class="BodyStyle" >
<div id="Layer1" style="position:absolute; left:5px; top:160px; width:800px; height:600px; z-index:1; visibility: hidden;"> 
  <table width="100%" border="1" cellpadding="0" cellspacing="0" bgcolor="#D6D5C5">
    <tr>
      <td>  <font size="2" face="Tahoma">
  <pre><%=resultText%></pre>
  </font></td>
    </tr>
  </table>
</div>
<table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                Report
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar" >
                 <form method="post" action="RptByExample.jsp">
                    <tr>
                        <td >Query By Examples</td>
                        <td>
                         <textarea name="textarea" cols="45" rows="4"></textarea>
                           <input type=submit value="Query"/>
                        </td>
                        <td style="text-align:right">
                                <a href="javascript:popupStart(300,400,'Help.jsp')"  >Help</a> | <a href="javascript:popupStart(300,400,'About.jsp')" >About</a> | <a href="javascript:popupStart(300,400,'License.jsp')" >License</a>
                        </td>
                    </tr>
                  </form>
                </table>
            </td>
        </tr>
        </table>
     <% if (results.compareTo("") != 0) {%>
	 <table width="800" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td><a href="#" onMouseOver="showHideLayers('Layer1','','show')">Show Text 
      Version</a></td>
    <td><a href="#" onMouseOver="showHideLayers('Layer1','','hide')">Hide</a></td>
  </tr>
</table>
<% } %>
     <%=results%> 
</body>
        </html>
