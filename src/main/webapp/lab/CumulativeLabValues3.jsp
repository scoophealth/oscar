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
	import="oscar.oscarDemographic.data.*,java.util.*,java.sql.Connection,oscar.oscarPrevention.*,oscar.oscarLab.ca.on.*,oscar.util.*,oscar.oscarLab.*,oscar.oscarLab.ca.all.util.CumulativeLabValuesComparator,org.jdom.*,oscar.oscarDB.*,org.jdom.input.*,java.io.InputStream"%>
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

String demographic_no = request.getParameter("demographic_no");

LinkedHashMap nameMap = new LinkedHashMap();
ArrayList idList = new ArrayList();
HashMap measIdMap = new HashMap();
ArrayList dateList = new ArrayList();

try{
    InputStream is = application.getResource("/WEB-INF/measurements.xml").openStream();
    SAXBuilder parser = new SAXBuilder();
    Document doc = parser.build(is);
    is.close();
    
    Element root = doc.getRootElement();
    List items = root.getChildren();
    
/*  loop through the measurements specified in the measurements.xml file
*   nameMap: (loinc_code, name)
*   measIdMap: (loinc_code, IdMap)
*   IdMap: (idHash, h)
*   h: (lab_no, result, abn, date, type) -- retrieved in order of the date
*   dateList: (dateIdHash)
*   dateIdHash: (date, lab_no)
    */
    
    for (int i = 0; i < items.size(); i++){
        Element e = (Element) items.get(i);
        String loinc_code = e.getAttributeValue("loinc_code");
        String name = e.getAttributeValue("name");
        if (!loinc_code.equalsIgnoreCase("NULL")){
            LinkedHashMap IdMap = new LinkedHashMap();
            ArrayList labList = CommonLabTestValues.findValuesByLoinc(demographic_no, loinc_code);
            for (int j=0; j < labList.size(); j++){
                Hashtable h = (Hashtable) labList.get(j);
                String date = ( (String) h.get("date") );
                String id = (String) h.get("lab_no");
                IdMap.put(id, h);
                
                // check if this lab has already been added
                if (!idList.contains(id)){
                    idList.add(id);
                    Hashtable dateIdHash = new Hashtable();
                    dateIdHash.put("date", date);
                    dateIdHash.put("id", id);
                    dateList.add(dateIdHash);
                }
            }
            
            // add the test only if there are results for it
            if (labList.size() > 0){
                measIdMap.put(loinc_code, IdMap);
                nameMap.put(loinc_code, name);
            }
            
        // If the first element to be displayed is a header
        }else if(nameMap.size() == 0 && !name.equals("NULL")){
            nameMap.put("NULL"+i, name);
        // Do not allow the first element displayed to be a space
        }else if(nameMap.size() != 0){

            String[] nameMapKeys = new String[nameMap.size()];
            nameMap.keySet().toArray(nameMapKeys);
            String lastKey = nameMapKeys[nameMapKeys.length-1];
            
            // Do not allow more than one space or more than one header in a row
            // A space is allowed to be followed by a header
            if ( lastKey.startsWith("NULL") && (name.equalsIgnoreCase("NULL") || !((String) nameMap.get(lastKey)).equalsIgnoreCase("NULL"))){
                nameMap.remove(lastKey);
                
                if (nameMapKeys.length > 1){
                    lastKey = nameMapKeys[nameMapKeys.length-2];
                    // if a header has been removed by a space remove the space before the header too
                    if (((String) nameMap.get(lastKey)).equalsIgnoreCase("NULL") && name.equalsIgnoreCase("NULL"))
                        nameMap.remove(lastKey);
                }
            }
            nameMap.put("NULL"+i, name);
        }
        
    }

// if the last item in the name list is a space or header remove it
    String[] nameMapKeys = new String[nameMap.size()];
    nameMap.keySet().toArray(nameMapKeys);
    if ( nameMapKeys[nameMapKeys.length-1].startsWith("NULL") )
        nameMap.remove(nameMapKeys[nameMapKeys.length-1]);
    
}catch(Exception e){
	MiscUtils.getLogger().error("Error", e);
}%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">



<%@page import="org.oscarehr.util.MiscUtils"%><html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Cumulative Lab 3</title>
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


function addLabToProfile2(labType,testName){

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
   var params = "demographicNo=<%=demographic_no%>&rand="+ran_number+"&labType="+labType+"&testName="+testName;  //hack to get around ie caching the page
   ///alert(params);  //'d'+ran_number
   new Ajax.Updater(newNode,url, {method:'get',
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

function reportWindow(page) {
    windowprops="height=660, width=960, location=no, scrollbars=yes, menubars=no, toolbars=no, resizable=yes, top=0, left=0";
    var popup = window.open(page, "labreport", windowprops);
    popup.focus();
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
		<td class="MainTableLeftColumn" valign="top"></td>
		<td valign="top" class="MainTableRightColumn">
		<table class="cumlatable">
			<tr>
				<th>&nbsp;</th>
				<th>Latest Value</th>
				<th>Last Done</th>
				<!-- Dates start here. Need to have all the dates of the different labs -->
				<%
                            // use a custom comparator to compare the Hashtables in the array
                            CumulativeLabValuesComparator comp = new CumulativeLabValuesComparator();
                            Collections.sort(dateList, comp);
                            for (int i=0; i < dateList.size(); i++){
                                Hashtable dateIdHash = (Hashtable) dateList.get(i);
                                String dateString = (String) dateIdHash.get("date");
                                Date labDate= UtilDateUtilities.StringToDate(dateString, "yyyy-MM-dd HH:mm:ss");
                                //Hashtable idHash = (Hashtable) dateIdHash.get("idHash");
                                //String lab_no = (String) idHash.get("lab_no");
                                //String lab_type = (String) idHash.get("lab_type");
                                String lab_no = (String) dateIdHash.get("id");
                                
                                CommonLabResultData data = new CommonLabResultData();
                                String multiId = data.getMatchingLabs(lab_no, "HL7");
                            %>

				<th><a
					href="javascript:reportWindow('../lab/CA/ALL/labDisplay.jsp?segmentID=<%=lab_no%>&providerNo=<%= session.getValue("user") %>')"><%=UtilDateUtilities.DateToString( labDate , "dd-MMM yy")%></a>
				</th>
				<%}%>
			</tr>

			<%
                        Iterator iter = nameMap.keySet().iterator();
                        while (iter.hasNext()){
                            // Display the test name
                            String loinc_code = (String) iter.next();
                            String testName = (String) nameMap.get(loinc_code);
                            LinkedHashMap IdMap = (LinkedHashMap) measIdMap.get(loinc_code);
                            
                            //preserve spaces in the test names
                            testName = testName.replaceAll("\\s", "&#160;");
                            
                            // display the latest value for the test
                            if (!loinc_code.startsWith("NULL")){
                                String latestDate = "";
                                String latestVal = "";
                                String abn = "N";
                                if (IdMap.size() > 0){
                                    // the latest date will be the first one
                                    Hashtable ht = (Hashtable) IdMap.get(IdMap.keySet().iterator().next());
                                    latestVal = (String) ht.get("result");
                                    latestDate = (String) ht.get("date");
                                    abn = (String) ht.get("abn");
                                    // trim the date
                                    latestDate = latestDate.substring(0, 10);
                                }
                        %>
			<tr>
				<td><%=testName%></td>
				<td class="<%= abn %>"><%=StringUtils.maxLenString(latestVal, 9, 8, "...")%></td>
				<td><%=latestDate%></td>
				<%
                            // display all of values from all the labs for the given test
                            for (int i = 0; i < dateList.size(); i++){
                                    Hashtable dateIdHash = (Hashtable) dateList.get(i);
                                    String labVal = "";
                                    abn = "N";
                                    if (IdMap.size() > 0 ){
                                        Hashtable ht = (Hashtable) IdMap.get(dateIdHash.get("id"));
                                        if (ht != null){
                                            labVal = (String) ht.get("result");
                                            abn = (String) ht.get("abn");
                                        }
                                    }
                            %>
				<td class="<%= abn %>"><%=StringUtils.maxLenString(labVal, 9, 8, "...")%></td>
				<%}%>
			</tr>
			<%}else{
                        // if the loinc_code is null display the header name without any results or
                        // a blank space if the name is null as well
                        %>
			<tr>
				<td colspan="<%= 3+dateList.size() %>">
				<%
                                if (testName.equals("NULL")){
                                %><%="<br />"%>
				<%
                                }else{
                                %><%= testName %>
				<%
                                }
                                %>
				</td>
			</tr>
			<%}
                        }%>
		</table>
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
