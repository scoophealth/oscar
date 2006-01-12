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
 * McMaster Unviersity test2
 * Hamilton 
 * Ontario, Canada 
 */
-->
<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  //int demographic_no = Integer.parseInt(request.getParameter("demographic_no")); 
  String demographic_no = request.getParameter("demographic_no"); 
  String creatorDemo = request.getParameter("demo");
  if (creatorDemo == null){
      creatorDemo = request.getParameter("remarks");      
  }
  if (creatorDemo == null){
      creatorDemo = (String) request.getAttribute("demo");    
  }
%>  

<%@page  import="oscar.oscarDemographic.data.*,java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>


<html:html locale="true">

<head>
<title>
Demographic Extention 
</title><!--I18n-->
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">


<SCRIPT LANGUAGE="JavaScript">
<!--
//if (document.all || document.layers)  window.resizeTo(790,580);
function newWindow(file,window) {
  msgWindow=open(file,window,'scrollbars=yes,width=760,height=520,screenX=0,screenY=0,top=0,left=10');
  if (msgWindow.opener == null) msgWindow.opener = self;
} 
//-->
</SCRIPT>


<style type="text/css">
	
	div.tableListing table {
               margin-top:0px;
               border-width: 1px 1px 1px 1px;
	       border-spacing: 0px;
	       border-style: outset outset outset outset;
	       border-color: gray gray gray gray;
	       border-collapse: collapse;
            }
            
            div.tableListing table tr td{
               font-size: x-small;
               text-align: center;
               border-width: 1px 1px 1px 1px;
               padding: 1px 1px 1px 1px;
               border-style: inset inset inset inset;
               border-color: gray gray gray gray;
               background-color: white;
               -moz-border-radius: 0px 0px 0px 0px;
            }
            
            div.tableListing table tr th{
               font-size: small;
               border-width: 1px 1px 1px 1px;
               padding: 1px 1px 1px 1px;
               border-style: inset inset inset inset;
               border-color: gray gray gray gray;
               background-color: white;
               -moz-border-radius: 0px 0px 0px 0px;
            }
	
</style>
</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn" width="175" >
      Add Relation
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
					<oscar:nameage demographicNo="<%=creatorDemo%>"/>
                        </td>
                        <td  >&nbsp;
							
                        </td>
                        <td style="text-align:right">
                                <a href="javascript:popupStart(300,400,'Help.jsp')"  ><bean:message key="global.help" /></a> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about" /></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license" /></a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn" valign="top">
            &nbsp;
            
            
            </td>
            <td valign="top" class="MainTableRightColumn">
               <form name="ADDAPPT" method="post" action="../appointment/appointmentcontrol.jsp">
                Name: <INPUT TYPE="TEXT" NAME="keyword" size="25" VALUE="">
                <input type="submit" name="Submit" value="Search">
                <INPUT TYPE="hidden" NAME="orderby" VALUE="last_name" >
				    <INPUT TYPE="hidden" NAME="search_mode" VALUE="search_name" >
				    <INPUT TYPE="hidden" NAME="originalpage" VALUE="../demographic/AddAlternateContact.jsp" >
				    <INPUT TYPE="hidden" NAME="limit1" VALUE="0" >
				    <INPUT TYPE="hidden" NAME="limit2" VALUE="5" >
                <INPUT TYPE="hidden" NAME="displaymode" VALUE="Search "> 
                
                <INPUT TYPE="hidden" NAME="appointment_date" VALUE="2002-10-01" WIDTH="25" HEIGHT="20" border="0" hspace="2">
                <INPUT TYPE="hidden" NAME="status" VALUE="t"  WIDTH="25" HEIGHT="20" border="0" hspace="2">
                <INPUT TYPE="hidden" NAME="start_time" VALUE="10:45" WIDTH="25" HEIGHT="20" border="0"  onChange="checkTimeTypeIn(this)">
                <INPUT TYPE="hidden" NAME="type" VALUE="" WIDTH="25" HEIGHT="20" border="0" hspace="2">
                <INPUT TYPE="hidden" NAME="duration" VALUE="15" WIDTH="25" HEIGHT="20" border="0" hspace="2" >
                <INPUT TYPE="hidden" NAME="end_time" VALUE="10:59" WIDTH="25" HEIGHT="20" border="0" hspace="2"  onChange="checkTimeTypeIn(this)" >       
                <input type="hidden" name="demographic_no"  readonly value="" width="25" height="20" border="0" hspace="2" >
                <input type="hidden" name="location"  tabindex="4" value="" width="25" height="20" border="0" hspace="2">
                <input type="hidden" name="resources"  tabindex="5" value="" width="25" height="20" border="0" hspace="2">
                <INPUT TYPE="hidden" NAME="user_id" readonly VALUE='oscardoc, doctor' WIDTH="25" HEIGHT="20" border="0" hspace="2">
     	          <INPUT TYPE="hidden" NAME="dboperation" VALUE="add_apptrecord">
                <INPUT TYPE="hidden" NAME="createdatetime" readonly VALUE="2002-10-1 17:53:50" WIDTH="25" HEIGHT="20" border="0" hspace="2">
                <INPUT TYPE="hidden" NAME="provider_no" VALUE="115">
                <INPUT TYPE="hidden" NAME="creator" VALUE="oscardoc, doctor">
                <INPUT TYPE="hidden" NAME="remarks" VALUE="<%=creatorDemo%>">
            </form>
            
               <%String demoNo = request.getParameter("demographic_no");
                 String name = request.getParameter("name");
                 String origDemo = request.getParameter("remarks");
               if ( demoNo != null ) {%>
               <html:form action="/demographic/AddRelation">
               <input type="hidden" name="origDemo" value="<%=origDemo%>"/>
               <input type="hidden" name="linkingDemo" value="<%=demoNo%>"/>
               
               <div class="prevention">                                                         
                   <fieldset >
                      <legend >Relation</legend>
                         <label for="name">Name:<%=name%> <br/>
                         
                         <label for="relation">Relationship:</label>
                                <select name="relation">              
                                  <option value="Mother">Mother</option>
                                  <option value="Father">Father</option>
                                  <option value="Father">Parent</option>
                                  <option value="Wife">Wife</option>
                                  <option value="Husband">Husband</option>
                                  <option value="Partner">Partner</option>
                                  
                                  <option value="Brother">Brother</option>
                                  <option value="Sister">Sister</option>
                                  <option value="Son">Son</option>
                                  <option value="Daughter">Daughter</option>
                                  <option value="Aunt">Aunt</option>
                                  <option value="Uncle">Uncle</option>
                                  <option value="GrandFather">GrandFather</option>
                                  <option value="GrandMother">GrandMother</option>
                                  <option value="Guardian">Guardian</option>
                                  <option value="Other">Other</option>
                               </select>
                               <input type="checkbox" name="sdm" value="yes"> Substitute Decision Maker</input>
                               <input type="checkbox" name="emergContact" value="yes"> Emergency Contact</input>
                               <br/>
                         <label for="notes">Notes:</label><br>
                                <textarea cols="20" rows="3" name="notes" ></textarea>
                               <input type="submit" value="Add Relationship"/>
                   </fieldset>                   
               </div>                                                                                               
               </html:form>            
               <%}%>
            
               <div class="tablelisting">
                  <table>
               <% DemographicRelationship demoRelation = new DemographicRelationship(); 
                  ArrayList list = demoRelation.getDemographicRelationships(creatorDemo);   
                  if (list.size() > 0){
               %>
                     <tr>
                        <th>Name</th>
                        <th>Relation</th>
                        <th>SDM</th>
                        <th>Notes</th>
                        <th>&nbsp;</th>
                     </tr>
                  
               <% }
                  for ( int i = 0; i < list.size(); i++ ){ 
                     Hashtable h = (Hashtable) list.get(i);
                     String relatedDemo = (String) h.get("demographic_no");              
                     DemographicData dd = new DemographicData();
                     DemographicData.Demographic demographic = dd.getDemographic(relatedDemo);    %>
                     <tr>
                        <td><%=demographic.getLastName() +", "+demographic.getFirstName()%></td>
                        <td><%=h.get("relation")%></td>
                        <td><%=returnYesIf1(h.get("sub_decision_maker"))%></td>
                        <td><%=h.get("notes")%></td>
                        <td><a href="DeleteRelation.do?id=<%=h.get("id")%>&amp;origDemo=<%=creatorDemo%>">del</a></td>
                     </tr>
                <%}%>
                  </table>
               </div>
            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">
            &nbsp;
            </td>
            <td class="MainTableBottomRowRightColumn" valign="top">
            &nbsp;
            </td>
        </tr>
    </table>
</body>
</html:html>
<%!
String returnYesIf1(Object o){
    String ret = "";
    if ( o instanceof String){
        String s = (String) o;
        if ( "1".equals(s)){
            ret = "yes";
        }
    }
    return ret;
}

%>