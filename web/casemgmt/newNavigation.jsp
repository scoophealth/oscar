<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>

   
<script type="text/javascript">            
       
    //This object stores the key -> cmd value passed to action class and the id of the created div
    // and the value -> URL of the action class
    <c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
    var URLs = { 
                  preventions:  "<c:out value="${ctx}"/>/oscarEncounter/displayPrevention.do?hC=009999",
                  tickler:      "<c:out value="${ctx}"/>/oscarEncounter/displayTickler.do?hC=FF6600",
                  Dx:           "<c:out value="${ctx}"/>/oscarEncounter/displayDisease.do?hC=5A5A5A",
                  forms:        "<c:out value="${ctx}"/>/oscarEncounter/displayForms.do?hC=917611",
                  eforms:       "<c:out value="${ctx}"/>/oscarEncounter/displayEForms.do?hC=11CC00",<%/*  88E900 */%> 
                  docs:         "<c:out value="${ctx}"/>/oscarEncounter/displayDocuments.do?hC=476BB3",
                  labs:         "<c:out value="${ctx}"/>/oscarEncounter/displayLabs.do?hC=A0509C", <%/* 550066   */%>                         
                  msgs:         "<c:out value="${ctx}"/>/oscarEncounter/displayMessages.do?hC=DDDD00", <% /* FF33CC */ %>
                  measurements: "<c:out value="${ctx}"/>/oscarEncounter/displayMeasurements.do?hC=344887",
                  consultation: "<c:out value="${ctx}"/>/oscarEncounter/displayConsultation.do"
              };
              
    function setup() {
        
        for( var idx in URLs ) {        
            var div = document.createElement("div");
            div.id = idx;
            div.className = "leftBox";
            $("leftNavbar").appendChild(div);            
            popColumn(URLs[idx],idx,idx);
        }
        
   }
   
   

var updateNeeded = false;

function updateDiv() {
    
    if( updateNeeded ) { 
        var div = $F("reloadDiv");
        popColumn(URLs[div], div, div);  
        updateNeeded = false;
    }
    
    setTimeout("updateDiv();", 1000);
}
    setup();
    setTimeout("updateDiv();", 1000);
</script>

<form name="dummyForm">
    <input type="hidden" id="reloadDiv" name="reloadDiv" value="none" onchange="updateDiv();">
</form>