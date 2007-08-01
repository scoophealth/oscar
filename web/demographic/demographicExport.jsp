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
<%@page  import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*,oscar.oscarProvider.data.*,oscar.util.*,oscar.oscarReport.data.*,oscar.oscarPrevention.pageUtil.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>

<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  //int demographic_no = Integer.parseInt(request.getParameter("demographic_no")); 
  String demographic_no = request.getParameter("demographic_no"); 
  
  DemographicSets  ds = new DemographicSets();
  ArrayList sets = ds.getDemographicSets();

  
  
%>

<html:html locale="true">

<head>
<!--I18n-->
<title>
oscarPrevention 
</title>
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

function checkSelect(slct) {
    if (slct==-1) {
	alert("Please select a Patient Set");
	return false;
    }
    else return true;
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
</style>
</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn" width="100" >
               demographicExport
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
                            Demographic Export
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
               <html:form action="/demographic/DemographicExport" method="get" onsubmit="return checkSelect(patientSet.value);">
               <div>
                  Patient Set:
                  <html:select property="patientSet">
                      <html:option value="-1" >--Select Set--</html:option>
                      <% for ( int i = 0 ; i < sets.size(); i++ ){  
                            String s = (String) sets.get(i);%>
                      <html:option value="<%=s%>"><%=s%></html:option>
                      <%}%>
                  </html:select>                  
                  <input type="submit" value="Export" />
               </div>               
               
               </html:form>
               <html:form action="/demographic/DemographicExport2" method="get" onsubmit="patientSet.value = document.forms[0].patientSet.value;return checkSelect(patientSet.value);">
		   <html:hidden property="patientSet"/>
		   <input type="submit" value="Export (Spec 2.0)" />               
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
<script type="text/javascript">
    //Calendar.setup( { inputField : "asofDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "date", singleClick : true, step : 1 } );
</script>    

</body>
</html:html>
<%!

%>
