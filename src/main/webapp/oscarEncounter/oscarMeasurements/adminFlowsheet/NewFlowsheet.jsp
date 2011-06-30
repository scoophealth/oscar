<%@page contentType="text/html"%>
<%@page  import="java.util.*,oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*,oscar.oscarEncounter.oscarMeasurements.*,oscar.oscarEncounter.oscarMeasurements.bean.*,java.net.*"%>
<%@page import="org.jdom.Element,oscar.oscarEncounter.oscarMeasurements.data.*,org.jdom.output.Format,org.jdom.output.XMLOutputter,oscar.oscarEncounter.oscarMeasurements.util.*" %><% 
String newFlowsheet = null;
MeasurementFlowSheet m = null;
if ( request.getParameter("flowsheetDisplayName") != null){
	String trigger = request.getParameter("trigger"); 
      m = new MeasurementFlowSheet();
      m.parseDxTriggers(trigger);
      m.setDisplayName(request.getParameter("flowsheetDisplayName"));
      
      Hashtable h = new Hashtable();
      h.put("measurement_type","WT");
      h.put("display_name","WT");
      h.put("value_name","WT");
      
      FlowSheetItem fsi = new FlowSheetItem( h);
      m.addListItem(fsi);

      MeasurementTemplateFlowSheetConfig templateConfig = MeasurementTemplateFlowSheetConfig.getInstance();
      newFlowsheet =  templateConfig.addFlowsheet( m );
      m.loadRuleBase();
      
  }
      
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create a new Flowsheet</title>
    </head>
    <body>
        <h2>Create a new Flowsheet</h2>        
        <form action="NewFlowsheet.jsp" >
        <table border="0">
        <tr><td>Name: </td><td><input type="text" name="flowsheetDisplayName" /></td></tr>
        <tr><td>Trigger: </td><td><input type="text" name="trigger"/> (eg icd9:250)</td></tr>
        </table>
        <input type="submit" name="Submit" value="Create"/>
        </form>
        <br>
        <%if (newFlowsheet !=null){%>
        <a href="EditFlowsheet.jsp?flowsheet=<%=newFlowsheet%>"><%=m.getDisplayName()%></a>
        <%}%>
        
    </body>
</html>
