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

<%@page contentType="text/html"%>
<%@page  import="java.util.*,oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*,oscar.oscarEncounter.oscarMeasurements.*,oscar.oscarEncounter.oscarMeasurements.bean.*,java.net.*"%>
<%@page import="org.jdom.Element,oscar.oscarEncounter.oscarMeasurements.data.*,org.jdom.output.Format,org.jdom.output.XMLOutputter,oscar.oscarEncounter.oscarMeasurements.util.*" %>
<%@page import="org.oscarehr.common.dao.FlowSheetUserCreatedDao,org.oscarehr.common.model.FlowSheetUserCreated,org.oscarehr.util.SpringUtils" %>
<%
FlowSheetUserCreatedDao flowSheetUserCreatedDao = (FlowSheetUserCreatedDao) SpringUtils.getBean("flowSheetUserCreatedDao");
List<FlowSheetUserCreated> flowsheets = flowSheetUserCreatedDao.getAllUserCreatedFlowSheets();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title>Create a new Flowsheet</title>
        <script type="text/javascript">
        	function checkForm(){
        		  var displayName = document.getElementById("displayName").value;
        	      var dxcodeTriggers = document.getElementById("dxcodeTriggers").value;
        	      var warningColour = document.getElementById("warningColour").value;
        	      var recommendationColour = document.getElementById("recommendationColour").value;
        	      
        	      if (displayName === ""){
        	    	  alert("Display Name can not be blank.");
        	    	  document.getElementById("displayName").focus();        	    	  
        	    	  return false;
        	      }        	      
        	      if (dxcodeTriggers === ""){
        	    	  alert("Dxcode Triggers can not be blank.");
        	    	  document.getElementById("dxcodeTriggers").focus();
        	    	  return false;
        	      }        	      
        	      if (warningColour === ""){
        	    	  alert("Warning colour can not be blank.");
        	    	  document.getElementById("warningColour").focus();
        	    	  return false;
        	      }        	             	      
        	      if (recommendationColour === ""){
        	    	  alert("Recommendation Colour can not be blank.");
        	    	  document.getElementById("recommendationColour").focus();
        	    	  return false;
        	      }
        	      
        	}
        </script>
    </head>
    <body>
        <h2>Create a new Flowsheet</h2>        
        <form action="FlowSheetCustomAction.do" onsubmit="return checkForm()">
            <input type="hidden" name="method" value="createNewFlowSheet"/>
        <table border="0">
        <tr><td>Name: </td><td><input type="text" name="displayName" id="displayName"/></td></tr>
        <tr><td>Trigger: </td><td><input type="text" name="dxcodeTriggers" id="dxcodeTriggers"/> (eg icd9:250)</td></tr>
        <tr><td>Warning Colour: </td><td><input type="text" name="warningColour" id="warningColour"/> (eg red or #E00000)</td></tr>
        <tr><td>Recommendation Colour: </td><td><input type="text" name="recommendationColour" id="recommendationColour"/> (eg yellow)</td></tr>
        
        </table>
        <input type="submit" name="Submit" value="Create"/>
        </form>
        <br>
        <%
        for(FlowSheetUserCreated flowsheet: flowsheets){%>
        <a href="EditFlowsheet.jsp?flowsheet=<%=flowsheet.getName()%>"><%=flowsheet.getDisplayName()%></a></br>
        <%}%>
        
    </body>
</html>
