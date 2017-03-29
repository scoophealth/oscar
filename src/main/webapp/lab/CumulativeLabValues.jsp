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

<%@page
	import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*,oscar.oscarLab.ca.on.*,oscar.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_lab" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_lab");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%
  
  //int demographic_no = Integer.parseInt(request.getParameter("demographic_no")); 
  String demographic_no = request.getParameter("demographic_no"); 
    
  ArrayList prevList = CommonLabTestValues.findUniqueLabsForPatient(demographic_no);
          
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">


<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Cumulative Lab</title>
<!--I18n-->
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css" />
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
<script type="text/javascript" src="../share/javascript/prototype.js"></script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<link rel="stylesheet" type="text/css"
	href="../share/css/niftyCorners.css" />
<link rel="stylesheet" type="text/css"
	href="../share/css/niftyPrint.css" media="print" />
<script type="text/javascript" src="../share/javascript/nifty.js"></script>
<script type="text/javascript">
window.onload=function(){
if(!NiftyCheck())
    return;

//Rounded("div.news","all","transparent","#FFF","small border #999");
Rounded("div.headPrevention","all","#CCF","#efeadc","small border blue");
Rounded("div.preventionProcedure","all","transparent","#F0F0E7","small border #999");

Rounded("div.leftBox","top","transparent","#CCCCFF","small border #ccccff");
Rounded("div.leftBox","bottom","transparent","#EEEEFF","small border #ccccff");

}
</script>




<script type="text/javascript">
<!--
//if (document.all || document.layers)  window.resizeTo(790,580);
function newWindow(file,window) {
  msgWindow=open(file,window,'scrollbars=yes,width=760,height=520,screenX=0,screenY=0,top=0,left=10');
  if (msgWindow.opener == null) msgWindow.opener = self;
} 
//-->
</script>




<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<script language="JavaScript">


function addLabToProfile(labType,testName){

   alert("calling addLabToProfile");
   var url = "../lab/DisplayLabValue.jsp";
   var ran_number=Math.round(Math.random()*1000000);
   var params = "demographicNo=<%=demographic_no%>&rand="+ran_number+"&labType="+labType+"&testName="+testName;  //hack to get around ie caching the page
   alert(params);
   new Ajax.Updater('dd',url, {method:'get',
                                          parameters:params,
                                          asynchronous:true,
                                          onComplete: addLabToList,
                                          evalScripts:true }); 
   //alert(origRequest.responseText);
}


function addLabToProfile2(labType,testName,identCode){

   ///alert("calling addLabToProfile2");
    
   var newNode = document.createElement('div');
   var img = document.createElement('img');
   img.setAttribute('src','../images/osx-pinwheel.gif');
   
   newNode.appendChild(img)
   var ran_number=Math.round(Math.random()*1000000);
   newNode.setAttribute('id','d'+ran_number);
   //var 
   //$('cumulativeLab').appendChild(req.responseText);
   $('cumulativeLab').appendChild(newNode);
   //alert(req.responseText);
   
   var url = "../lab/DisplayLabValue.jsp";
   var ran_number=Math.round(Math.random()*1000000);
   var params = "demographicNo=<%=demographic_no%>&rand="+ran_number+"&labType="+labType+"&testName="+testName+"&identCode="+identCode;  //hack to get around ie caching the page
   ///alert(params);  //'d'+ran_number
   new Ajax.Updater(newNode,url, {method:'post',
                                          parameters:params,
                                          asynchronous:true,
                                           //onComplete: reRound
                                          evalScripts:true}); 
   ///alert("sdf"+$('d'+ran_number));
   //alert(origRequest.responseText);
}

function reRound(){
   Rounded("div.headPrevention","all","#CCF","#efeadc","small border blue");
   Rounded("div.preventionProcedure","all","transparent","#F0F0E7","small border #999");
}


function addLabToList(req){
   var newText = document.createTextNode(req.responseText); 
   var newNode = document.createElement("div");
   var ran_number=Math.round(Math.random()*1000000);
   newNode.setAttribute("id","d"+ran_number);
   //$('cumulativeLab').appendChild(req.responseText);
   $('cumulativeLab').appendChild(newText);
   alert(req.responseText);
}

</script>


</head>

<body class="BodyStyle">
<!--  -->
<table class="MainTable" id="scrollNumber1">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">lab</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td><oscar:nameage demographicNo="<%=demographic_no%>" /></td>
				<td>&nbsp;</td>
				<td style="text-align: right"><oscar:help keywords="lab" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top">


		<div class="leftBox">
		<h3>&nbsp;Labs</h3>
		<div style="background-color: #EEEEFF;">
		<ul>
			<%for (int i = 0 ; i < prevList.size(); i++){ 
                      Hashtable h = (Hashtable) prevList.get(i);
                      String prevName = (String) h.get("testName");
                      String identCode = (String) h.get("identCode");
                      String identCodeEsc = "";
                      if (identCode != null)
                          identCodeEsc = identCode.replaceAll("&", "_amp_");
                      String prevNameEsc = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(prevName);
                      
                      if (prevName == null){prevName ="";} %>
			<li style="margin-top: 2px;"><%-- a title="fade=[on] header=[<%=prevName%>] body=[]"      href="javascript: function myFunction() {return false; }"  onclick="javascript:addLabToProfile2('<%=h.get("labType")%>','<%= java.net.URLEncoder.encode(prevName) %>');" --%>
			<a title="fade=[on] header=[<%=prevName%>] body=[]"
				href="javascript: function myFunction() {return false; }"
				onclick="javascript:addLabToProfile2('<%=h.get("labType")%>','<%=prevNameEsc%>','<%= identCodeEsc %>');">

			<%=StringUtils.maxLenString(prevName, 13, 8, "...")%> </a></li>
			<%}%>
		</ul>
		</div>
		</div>

		</td>
		<td valign="top" class="MainTableRightColumn">
		<div id="cumulativeLab"></div>

		<div style="display: none;">
		<div id="dd" /></div>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>
		<td class="MainTableBottomRowRightColumn" valign="top">&nbsp;</td>
	</tr>
</table>
<script type="text/javascript" src="../share/javascript/boxover.js"></script>
</body>
</html:html>
<%! 
String refused(Object re){ 
        String ret = "Given";
        if (re instanceof java.lang.String){
                
        if (re != null && re.equals("1")){
           ret = "Refused";
        }
        }
        return ret;
    }
String r(Object re){ 
        String ret = "";
        if (re instanceof java.lang.String){                
           if (re != null && re.equals("1")){
              ret = "style=\"background: #FFDDDD;\"";
           }else if(re !=null && re.equals("2")){
              ret = "style=\"background: #FFCC24;\""; 
           }
        }
        return ret;
    }
%>
