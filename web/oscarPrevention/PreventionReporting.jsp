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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->
<%@page  import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*,oscar.oscarProvider.data.*,oscar.util.*,oscar.oscarReport.data.*,oscar.oscarPrevention.pageUtil.*,java.net.*,oscar.eform.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />

<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  //int demographic_no = Integer.parseInt(request.getParameter("demographic_no")); 
  String demographic_no = request.getParameter("demographic_no"); 
  
  DemographicSets  ds = new DemographicSets();
  ArrayList sets = ds.getDemographicSets();

  String preventionText = "";
  
  String eformSearch = (String) request.getAttribute("eformSearch");
  EfmData efData = new EfmData();
  
%>

<html:html locale="true">

<head>
<html:base/>
<title>
oscarPrevention
</title> <!--I18n-->
<script src="../share/javascript/Oscar.js"></script>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" /> 
     
<script type="text/javascript" src="../share/calendar/calendar.js" ></script>      
<script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>" ></script>      
<script type="text/javascript" src="../share/calendar/calendar-setup.js" ></script>      

<style type="text/css">
  div.ImmSet { background-color: #ffffff; }
  div.ImmSet h2 {  }
  div.ImmSet ul {  }
  div.ImmSet li {  }
  div.ImmSet li a { text-decoration:none; color:blue;}
  div.ImmSet li a:hover { text-decoration:none; color:red; }
  div.ImmSet li a:visited { text-decoration:none; color:blue;}  
</style>

<SCRIPT LANGUAGE="JavaScript">

function showHideItem(id){ 
    if(document.getElementById(id).style.display == 'none')
        document.getElementById(id).style.display = ''; 
    else
        document.getElementById(id).style.display = 'none'; 
}

function showItem(id){
        document.getElementById(id).style.display = ''; 
}

function hideItem(id){
        document.getElementById(id).style.display = 'none'; 
}

function showHideNextDate(id,nextDate,nexerWarn){
    if(document.getElementById(id).style.display == 'none'){
        showItem(id);
    }else{
        hideItem(id);
        document.getElementById(nextDate).value = "";
        document.getElementById(nexerWarn).checked = false ;
        
    }        
}

function disableifchecked(ele,nextDate){        
    if(ele.checked == true){       
       document.getElementById(nextDate).disabled = true;       
    }else{                      
       document.getElementById(nextDate).disabled = false;              
    }
}

</SCRIPT>




<style type="text/css">
	table.outline{
	   margin-top:50px;
	   border-bottom: 1pt solid #888888;
	   border-left: 1pt solid #888888;
	   border-top: 1pt solid #888888;
	   border-right: 1pt solid #888888;
	}
	table.grid{
	   border-bottom: 1pt solid #888888;
	   border-left: 1pt solid #888888;
	   border-top: 1pt solid #888888;
	   border-right: 1pt solid #888888;
	}
	td.gridTitles{
		border-bottom: 2pt solid #888888;
		font-weight: bold;
		text-align: center;
	}
        td.gridTitlesWOBottom{
                font-weight: bold;
                text-align: center;
        }
	td.middleGrid{
	   border-left: 1pt solid #888888;	   
	   border-right: 1pt solid #888888;
           text-align: center;
	}	
	
	
label{
float: left;
width: 120px;
font-weight: bold;
}

span.labelLook{
font-weight:bold;

}

input, textarea,select{

margin-bottom: 5px;
}

textarea{
width: 250px;
height: 150px;
}

.boxes{
width: 1em;
}

#submitbutton{
margin-left: 120px;
margin-top: 5px;
width: 90px;
}

br{
clear: left;
}

table.ele {
   
   border-collapse:collapse;
}

table.ele td{
    border:1px solid grey;
    padding:2px;
}
</style>

<style type="text/css" media="print">
.MainTable {
    display:none;
}
.hiddenInPrint{
    display:none;
}
.shownInPrint{
    display:block;
}

</style>


</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn" width="100" >
               oscarPrevention
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
                            Prevention Reporting
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
               <html:form action="/oscarPrevention/PreventionReport" method="get">
               <div>
                  Patient Set:
                  <html:select property="patientSet">
                      <html:option value="-1" >--Select Set--</html:option>
                      <% for ( int i = 0 ; i < sets.size(); i++ ){  
                            String s = (String) sets.get(i);%>
                      <html:option value="<%=s%>"><%=s%></html:option>
                      <%}%>
                  </html:select>                  
               </div>
               <div>
                  Prevention Query:
                  <html:select property="prevention">
                      <html:option value="-1" >--Select Query--</html:option>
                      <html:option value="PAP" >PAP</html:option>
                      <html:option value="Mammogram" >Mammogram</html:option>
                      <html:option value="Flu" >Flu</html:option>
                      <html:option value="ChildImmunizations" >Child Immunizations</html:option>                                                             
                  </html:select>                  
               </div>
               <div>
                  As of:
                    <html:text property="asofDate" size="9" styleId="asofDate" /> <a id="date"><img title="Calendar" src="../images/cal.gif" alt="Calendar" border="0" /></a> <br>                        
                      
                    
                    
               </div>
               <input type="submit" />
               </html:form>
               
               
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
    
    <div>
                <%ArrayList overDueList = new ArrayList();
                  String type = (String) request.getAttribute("ReportType");
                  String ineligible = (String) request.getAttribute("inEligible");
                  String done = (String) request.getAttribute("up2date");
                  String percentage = (String) request.getAttribute("precent");
                  ArrayList list = (ArrayList) request.getAttribute("returnReport");
                  Date asDate = (Date) request.getAttribute("asDate");
                  if (asDate == null){ asDate = Calendar.getInstance().getTime(); }
                  if (list != null ){ %>
              <table class="ele" width="80%">
                       <tr>
                       <td colspan="2">Total patients: <%=list.size()%><br/>Ineligible:<%=ineligible%></td>
                       <td colspan="3">Up to date: <%=done%> = <%=percentage %> %</td>
                       <%if (type != null ){ %>
                       <td colspan="10">&nbsp;<%=request.getAttribute("patientSet")%> </td>
                       <%}else{%>
                       <td colspan="8">&nbsp;<%=request.getAttribute("patientSet")%> </td>
                       <%}%>
                       </tr>
                       <tr>
                          <td>DemoNo</td>
                          <td>Age as of <br/><%=UtilDateUtilities.DateToString(asDate)%></td>
                          <td>Sex</td>
                          <td>Lastname</td>
                          <td>Firstname</td>
                          <%if (type != null ){ %>
                          <td>Guardian</td>
                          <%}%>
                          <td>Phone</td>                          
                          <td>Address</td>
                          <td>Status</td>                          
                          <%if (type != null ){ %>
                          <td>Shot #</td>
                          <%}%>                          
                          <td>Bonus Stat</td>
                          <td>Since Last Procedure Date</td>
                          <td>Last Procedure Date</td>
                          <td>Last Contact Method</td>
                          <td>Roster Physician</td>
                       </tr>
                       <%DemographicNameAgeString deName = DemographicNameAgeString.getInstance();                       
                         DemographicData demoData= new DemographicData();
                         
                         
                         for (int i = 0; i < list.size(); i++){
                            PreventionReportDisplay dis = (PreventionReportDisplay) list.get(i);
                            Hashtable h = deName.getNameAgeSexHashtable(dis.demographicNo);
                            DemographicData.Demographic demo = demoData.getDemographic(dis.demographicNo);
                            Hashtable efHash = efData.getLastEformDate(eformSearch,dis.demographicNo);
                            if (efHash == null){ System.out.println("efhash was null"); }
                            if (efHash != null){ System.out.println("efhash wasn't null"); }
                            
                            if (dis.state != null && dis.state.equals("Overdue")){
                               overDueList.add(dis.demographicNo);
                            }
                            %>
                       <tr>
                          <td>
                              <a href="javascript: return false;" onClick="popup(724,964,'../demographic/demographiccontrol.jsp?demographic_no=<%=dis.demographicNo%>&displaymode=edit&dboperation=search_detail','MasterDemographic')"><%=dis.demographicNo%></a>                              
                          </td>
                          
                          <%if (type == null ){ %>
                          <td><%=demo.getAgeAsOf(asDate)%></td>
                          <td><%=h.get("sex")%></td>
                          <td><%=h.get("lastName")%></td>
                          <td><%=h.get("firstName")%></td>
                          <td><%=demo.getPhone()%> </td>
                          <td><%=demo.getAddress()+" "+demo.getCity()+" "+demo.getProvince()+" "+demo.getPostal()%> </td>                          
                          <td bgcolor="<%=dis.color%>"><%=dis.state%></td>
                          <td bgcolor="<%=dis.color%>"><%=dis.bonusStatus%></td>
                          <td bgcolor="<%=dis.color%>"><%=dis.numMonths%></td>
                          <td bgcolor="<%=dis.color%>"><%=dis.lastDate%></td>
                          
                          <% }else {
                              DemographicData.Demographic demoSDM = demoData.getSubstituteDecisionMaker(dis.demographicNo);%>                                                                 
                          <td><%=demo.getAgeAsOf(asDate)%></td>
                          <td><%=h.get("sex")%></td>
                          <td><%=h.get("lastName")%></td>
                          <td><%=h.get("firstName")%></td>                          
                          
                          <td><%=demoSDM==null?"":demoSDM.getLastName()%><%=demoSDM==null?"":","%> <%= demoSDM==null?"":demoSDM.getFirstName() %>&nbsp;</td>
                          <td><%=demoSDM==null?"":demoSDM.getPhone()%> &nbsp;</td>
                          <td><%=demoSDM==null?"":demoSDM.getAddress()+" "+demoSDM==null?"":demoSDM.getCity()+" "+demoSDM==null?"":demoSDM.getProvince()+" "+demoSDM==null?"":demoSDM.getPostal()%> &nbsp;</td>                          
                          <td bgcolor="<%=dis.color%>"><%=dis.state%></td>                          
                          <td bgcolor="<%=dis.color%>"><%=dis.numShots%></td>
                          <td bgcolor="<%=dis.color%>"><%=dis.bonusStatus%></td>
                          <td bgcolor="<%=dis.color%>"><%=dis.numMonths%></td>
                          <td bgcolor="<%=dis.color%>"><%=dis.lastDate%></td>
                          <%}%>                          
                          <td bgcolor="<%=dis.color%>">
                             <% if (efHash != null ){ %>
                                 <%=efHash.get("formName")%>
                                 <%=efHash.get("date")%>                                 
                             <% }else{ %>
                                ----
                             <% } %>                                 
                          </td>
                          <td bgcolor="<%=dis.color%>"><%=providerBean.getProperty(demo.getProviderNo()) %></td>
                          
                       </tr>                                                         
                      <%}%>
                    </table>   
                  
                  <%}%>
                  
                  <% if ( overDueList.size() > 0 ) { 
                        String queryStr = "";
                        for (int i = 0; i < overDueList.size(); i++){
                            String demo = (String) overDueList.get(i);
                            if (i == 0){
                              queryStr += "demo="+demo;
                            }else{
                              queryStr += "&demo="+demo;  
                            }
                        }
                        %>                        
                        <a target="_blank" href="../tickler/AddTickler.do?<%=queryStr%>&message=<%=java.net.URLEncoder.encode(request.getAttribute("prevType")+" is due","UTF-8")%>">Add Tickler for Overdue</a>
                  <%}%>
               </div>
    
<script type="text/javascript">
    Calendar.setup( { inputField : "asofDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "date", singleClick : true, step : 1 } );
</script>    

</body>
</html:html>
