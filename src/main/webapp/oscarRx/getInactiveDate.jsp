<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="java.util.*,net.sf.json.*,java.io.*,org.apache.xmlrpc.*,oscar.oscarRx.util.*,oscar.oscarRx.data.*"  %><%
String din =  request.getParameter("din");
String id = request.getParameter("id");
Vector vec=new Vector();
try{
                RxDrugRef drugData = new RxDrugRef();
                 vec = drugData.getInactiveDate(din);
    
    }catch(Exception e){
    	MiscUtils.getLogger().error("Error", e);
    }

                

    Hashtable d = new Hashtable();

    d.put("id",id);
    d.put("vec",vec);
                 
   
    response.setContentType("text/x-json");
    JSONObject jsonArray = (JSONObject) JSONSerializer.toJSON( d );
    jsonArray.write(out);
%>
