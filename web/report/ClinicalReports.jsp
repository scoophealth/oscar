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
<%@page  import="oscar.oscarReport.data.DemographicSets, oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*,oscar.oscarProvider.data.*,oscar.util.*,oscar.oscarReport.ClinicalReports.*"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>

<%
    if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
    String provider = (String) session.getValue("user");

    String numeratorId = (String) request.getAttribute("numeratorId");
    String denominatorId = (String)  request.getAttribute("denominatorId");

    System.out.println("num "+numeratorId+" denom "+denominatorId);

    ClinicalReportManager reports = ClinicalReportManager.getInstance();

    List denominatorList = reports.getDenominatorList();
    List numeratorList   = reports.getNumeratorList();  
    
    Hashtable rep = new Hashtable();
    
    if ( request.getParameter("clear") != null && request.getParameter("clear").equals("yes")){
       session.removeAttribute("ClinicalReports"); 
    }

%>

                         
<html:html locale="true">

<head>
<title>
Clinical Reports 
</title>
<html:base/>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" /> 
     
<script type="text/javascript" src="../share/calendar/calendar.js" ></script>      
<script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>" ></script>      
<script type="text/javascript" src="../share/calendar/calendar-setup.js" ></script>      


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
</style>

<script type="text/javascript">
    var denominator_fields = new Array ();
    var denom_xtras;
    denominator_fields[denominator_fields.length] = "denominator_provider_no";
    denominator_fields[1] = "denominator_patientSet";
    
    
    function processExtraFields(t){
       var currentDenom = t.options[t.selectedIndex].value; 
       console.log(currentDenom);
       //Hide all extra denom fields
       for (  i = 0 ; i < denominator_fields.length; i++) {
          document.getElementById(denominator_fields[i]).style.display = 'none'; 
       }
       try{
           var fields_to_turn_on = denom_xtras[currentDenom];
           console.log("fields to turn on " + fields_to_turn_on[0]);
           //get list of extra 
           for (  i = 0 ; i < fields_to_turn_on.length; i++) {
              document.getElementById(fields_to_turn_on[i]).style.display = ''; 
           }
       }catch(e){
        e.printStackTrace();
       }
          
    
    }
</script>
	
</head>

<body class="BodyStyle" vlink="#0000FF" >
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn" width="100" >
               Clinical Reports
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
                            <%=  request.getAttribute("name") != null ?request.getAttribute("name"):""%>                       
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
               <%  
                ArrayList arrList = (ArrayList) session.getAttribute("ClinicalReports"); 
                if (arrList != null){
               %>
               <a href="ClinicalReports.jsp?clear=yes">Clear</a>
               <ul style="list-style-type:square; margin-left:1px;padding-left:4px;padding-top:2px;margin-top:2px;">
               <%     for (int i = 0; i < arrList.size(); i++){
                        ReportEvaluator re = (ReportEvaluator) arrList.get(i);
               %>
                    <li title="<%=re.getName()%>"><%=re.getNumeratorCount()%> / <%=re.getDenominatorCount()%>&nbsp;
                       <a style="text-decoration:none;" target="_blank" href="reportExport.jsp?id=<%=i%>" >csv</a>&nbsp;
                       <a style="text-decoration:none;" href="RemoveClinicalReport.do?id=<%=i%>" >del</a>
                    </li>
               <%   } %>
               </ul>
               <a style="text-decoration:none;" target="_blank" href="reportExport.jsp" >csv</a>
               <%}%>  
               
            </td>
            <td valign="top" class="MainTableRightColumn">
                <div>
                    <fieldset>
                     <html:form action="RunClinicalReport">
                         <!--
                           <label for="asOfDate"  >As Of Date:</label><input type="text" name="asOfDate" id="asOfDate" value="<%=""%>" size="9" > <a id="date"><img title="Calendar" src="../images/cal.gif" alt="Calendar" border="0" /></a> <br>                        
                           -->
                         <fieldset>
                             <legend>Numerator</legend>
                       
                           <select name="numerator">
                              <%for (int i =0 ; i < numeratorList.size();i++){
                                 Numerator n = (Numerator) numeratorList.get(i); 
                               %>
                               <option value="<%=n.getId()%>"  <%=sel(numeratorId,n.getId())%> ><%=n.getNumeratorName()%></option>              
                               <%}%>
                           </select>
                           <br/>
                         </fieldset> 
                         <fieldset>
                             <legend>Denominator</legend>
                       
                               <select id="denominator" name="denominator"  onchange="javascript:processExtraFields(this)">
                                   <%for (int i =0 ; i < denominatorList.size();i++){
                                     Denominator d = (Denominator) denominatorList.get(i); 
                                     if(d.hasReplaceableValues()){
                                         rep.put(d.getId(),d.getReplaceableKeys());
                                     }
                                   %>
                                   <option value="<%=d.getId()%>" <%=sel(denominatorId,d.getId())%> ><%=d.getDenominatorName()%></option>              
                                   <%}%>
                               </select>

                           <select  id="denominator_provider_no" name="denominator_provider_no">                          
                              <%
                                ArrayList providers = ProviderData.getProviderList();
                                for (int i=0; i < providers.size(); i++) {
                                   Hashtable h = (Hashtable) providers.get(i);%>
                                <option value="<%= h.get("providerNo")%>" <%= ( h.get("providerNo").equals(provider) ? " selected" : "" ) %>><%= h.get("lastName") %> <%= h.get("firstName") %></option>
                              <%}%>                    
                           </select>
                            <div id="denominator_patientSet">
                                <%  
                                DemographicSets demoSets = new DemographicSets();
                                ArrayList demoSetList = demoSets.getDemographicSets();
                                for( int idx = 0; idx < demoSetList.size(); ++idx ) {
                                %>
                                <input type="checkbox" name="denominator_patientSet" value="<%=demoSetList.get(idx)%>"><%=demoSetList.get(idx)%><br>
                                <%
                                }
                                %>
                            </div>
                        </fieldset>
                           <br/>
                       <input type="submit" value="Evaluate"/>
                       </html:form>
                    </fieldset>
    
               </div>
               
               <% if(request.getAttribute("denominator") != null){%>
               <div>
                  <H3>Results</H3> 
                  <ul>
                    <li>numerator:   <%=request.getAttribute("numerator")%></li>
                    <li>denominator: <%=request.getAttribute("denominator")%></li>
                    <li>percentage:  <%=request.getAttribute("percentage")%> %</li>
                  </ul>
                  CSV:<input type="text" size="30" value="<%=request.getAttribute("csv")%>"/>
               </div>
                <%}%>
               
                
                <%
                   //String[] outputfields = new String[2];//{"_demographic_no","_report_result" };
                   //outputfields[0]= "_demographic_no";
                   //outputfields[1]= "_report_result";
                   
                   //String[] outputfields = (String[]) request.getAttribute("outputfields");
                   // outputfields = (String[]) request.getAttribute("outputfields");
                   String[] outputfields ={"_demographic_no","_report_result" };
                   
                   System.out.println(outputfields);
                   
                   if (request.getAttribute("list") != null){
                     DemographicNameAgeString deName = DemographicNameAgeString.getInstance();                       
                     DemographicData demoData= new DemographicData();
                         
                         
                            
                       
                %>
                  <style type="text/css">
                      
                      table.results{
                           margin-top: 3px;
                           margin-left: 3px;
                           /*border-bottom: 1pt solid #888888;
                           border-left: 1pt solid #888888;
                           border-top: 1pt solid #888888;
                           border-right: 1pt solid #888888;*/
                           border: 1pt solid #888888;
                           border-collapse:collapse;
                      }
                      
                      table.results th{
                          border:1px solid grey;
                          padding:2px;    
                          text-decoration: none;
                      }
                      table.results td{
                         border:1px solid lightgrey;
                         padding-left:2px;
                         padding-right:2px;
                      }
                      
                      tr.red td {
                      background-color: red;
                      padding-left:2px;
                         padding-right:2px;
                      }
                  </style>
                  <table class="sortable tabular_list results" id="results_table">
                      <thead>
                      <tr>
                         <th>Last Name</th>
                         <th>First Name</th>
                         <th>Sex</th>
                         <th>Phone #</th>
                         <th>Address</th>
                          
                      <%for( int i= 0; i < outputfields.length; i++){%>
                         <th><%=replaceHeading(outputfields[i])%></th>
                      <%}%>
                      </tr>
                      </thead>
                <%       ArrayList list = (ArrayList) request.getAttribute("list");
                       for (int j = 0; j < list.size(); j++){
                          Hashtable h = (Hashtable) list.get(j);
                          System.out.println("h:"+h.size());
                          Enumeration en = h.keys();
                          while (en.hasMoreElements()){
                              String ssss = (String) en.nextElement();
                             System.out.println(ssss+" "+h.get(ssss)); 
                          }      
                          Hashtable demoHash = deName.getNameAgeSexHashtable(""+h.get("_demographic_no"));
                          DemographicData.Demographic demoObj = demoData.getDemographic(""+h.get("_demographic_no")); 
                          
                          String colour = "";
                          if ( h.get("_report_result") != null && (""+h.get("_report_result")).equals("false")){
                              colour = "class=red";
                          }
                %>
                      <tr <%=colour%> >
                          
                          <td><%=demoHash.get("lastName")%></td>
                          <td><%=demoHash.get("firstName")%></td>
                          <td><%=demoHash.get("sex")%></td>
                          <td><%=demoObj.getPhone()%> </td>
                          <td><%=demoObj.getAddress()+" "+demoObj.getCity()+" "+demoObj.getProvince()+" "+demoObj.getPostal()%> </td>  
                      
                <%
                          for( int i= 0; i < outputfields.length; i++){%>
                          <td><%=h.get(outputfields[i])%></td>
                <%        }
                          %>
                      </tr>    
                <%
                       }
                %>
                  </table>
                <%
                   }
                %>
               
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
    <!-- div>
       ToDos
       <ul>
          <li>-Show values of values in question.  ie Date of last BP measurement. Value of last A1C</li>
          <li>-export PDF pretty version</li>
          
       </ul>
    </div  -->
<script type="text/javascript">
//Calendar.setup( { inputField : "asOfDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "date", singleClick : true, step : 1 } );
denom_xtras = new Array();
<% Enumeration e = rep.keys();
   while(e.hasMoreElements()){
      String key = (String) e.nextElement();
      String[] repValues = (String[]) rep.get(key);      %>  
   var repVal<%=key%> = new Array();
<%   for (int i = 0; i < repValues.length; i++){ %>
     repVal<%=key%>[<%=i%>] = "denominator_<%=repValues[i]%>";
<%   }%>
   denom_xtras['<%=key%>']= repVal<%=key%>;
<% }%>

processExtraFields(document.getElementById('denominator'));


</script>  
<script language="javascript" src="../commons/scripts/sort_table/css.js">
<script language="javascript" src="../commons/scripts/sort_table/common.js">
<script language="javascript" src="../commons/scripts/sort_table/standardista-table-sorting.js">
</body>
</html:html>
<%!

String completed(boolean b){
    String ret ="";
    if(b){ret="checked";}
    return ret;
    }
     
String refused(boolean b){
    String ret ="";
    if(!b){ret="checked";}
    return ret;
    }
        
String str(String first,String second){
    String ret = "";
    if(first != null){
       ret = first;    
    }else if ( second != null){
       ret = second;    
    }
    return ret;
  }

String checked(String first,String second){
    String ret = "";
    if(first != null && second != null){
       if(first.equals(second)){
           ret = "checked";
       }
    }
    return ret;
  }


String sel(String s1,String s2){
     String ret = "";
     //System.out.println("s1 "+s1+" s2 "+s2);
     if (s1 != null && s2 != null && s1.equals(s2)){
        ret = "selected";    
     }
     return ret;  
  } 

String replaceHeading(String s){
    if ( s != null && s.equals("_demographic_no") ){
        return "Demographic #";    	 
    }else if (s.equals("_report_result")){
        return "Report Result";
    }
    return s;
}

%>
