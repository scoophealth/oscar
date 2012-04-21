<%@ page
	import="java.util.*,oscar.oscarRx.data.*,oscar.oscarRx.pageUtil.*,java.io.*,org.apache.xmlrpc.*"%>
<%
RxSessionBean bean = (RxSessionBean) session.getAttribute("RxSessionBean");
if ( bean == null ){
    return;
}
    Vector codes = bean.getAtcCodes();
    Vector bulletins = getBulletins(codes);
    Object[] bulletinsArray = bulletins.toArray();
    if ( bulletinsArray != null && bulletinsArray.length > 0){
        for (int i=0; i < bulletinsArray.length; i++){
            Hashtable ht = (Hashtable) bulletinsArray[i]; 
%>


<%@page import="org.oscarehr.util.MiscUtils"%><div
	style="background-color:<%=sigColor(""+ht.get("significance"))%>;margin-right:3px;margin-left:3px;margin-top:2px;padding-left:3px;padding-top:3px;padding-bottom:3px;">
<b><%=ht.get("name")%></b>&nbsp;&nbsp;News Source: <%=ht.get("news_source")%><br />
<%=ht.get("body")%><br />
Date: <%=ht.get("news_date")%></div>

<%}
    }else if(bulletins == null){ %>
<div>MyDrug to MyDrug Bulletin Service not available</div>
<%  }   %>
<%!
    
	String significance(String s){
       Hashtable h = new Hashtable();
       h.put("1","minor");
       h.put("2","moderate");
       h.put("3","major");
       h.put(" ","unknown");
       
       String retval = (String) h.get(s);
        if (retval == null) {retval = "unknown";}
        return retval;
   }
   
   String evidence(String s){
       Hashtable h = new Hashtable();
       h.put("P","poor");
       h.put("F","fair");
       h.put("G","good");
       h.put(" ","unknown");
       
       String retval = (String) h.get(s);
        if (retval == null) {retval = "unknown";}
        return retval;
   }


   String sigColor(String s){
       Hashtable h = new Hashtable();
       h.put("1","yellow");
       h.put("2","orange");
       h.put("3","red");
       h.put(" ","greenyellow");
       
       String retval = (String) h.get(s);
        if (retval == null) {retval = "greenyellow";}
        return retval;
   }
    
   String severityOfReaction(String s){
       Hashtable h = new Hashtable();
       h.put("1","Mild");
       h.put("2","Moderate");
       h.put("3","Severe");
       
       String retval = (String) h.get(s);
       if (retval == null) {retval = "Unknown";}
       return retval;
   }
    
   String severityOfReactionColor(String s){
       Hashtable h = new Hashtable();
       h.put("1","yellow");
       h.put("2","orange");
       h.put("3","red");
       
       String retval = (String) h.get(s);
       if (retval == null) {retval = "red";}
       return retval;
   }
                        
   String onSetOfReaction(String s){
       Hashtable h = new Hashtable();
       h.put("1","Immediate");
       h.put("2","Gradual");
       h.put("3","Slow");
       
       String retval = (String) h.get(s);
       if (retval == null) {retval = "Unknown";}
       return retval;
   }
   
   private Object callWebserviceLite(String procedureName,Vector params) throws Exception{
        Object object = null;
        String server_url = "http://dev2.mydrugref.org/backend/api";
        try{
            XmlRpcClientLite server = new XmlRpcClientLite(server_url);
            object = (Object) server.execute(procedureName, params);
        }catch (XmlRpcException exception) {
            
        	MiscUtils.getLogger().error("JavaClient: XML-RPC Fault #" + exception.code, exception);
            
            throw new Exception("JavaClient: XML-RPC Fault #" +
                    Integer.toString(exception.code) + ": " +
                    exception.toString());
            
        } catch (Exception exception) {
			MiscUtils.getLogger().error("JavaClient: ", exception);
            throw new Exception("JavaClient: " + exception.toString());
        }
        return object;
    }
    
    
     public Vector getBulletins(Vector drugs)throws Exception{
        removeNullFromVector(drugs);
        Vector params = new Vector();
        params.addElement("bulletins_byATC");
        params.addElement(drugs);
        //Vector vec = (Vector) callWebserviceLite("get",params);
        Vector vec = new Vector();
        Object obj =  callWebserviceLite("Fetch",params);
        if (obj instanceof Vector){
            vec = (Vector) obj;
        }else if(obj instanceof Hashtable){
            Object holbrook = ((Hashtable) obj).get("Holbrook Drug Interactions");
            if (holbrook instanceof Vector){
                vec = (Vector) holbrook;
            }
            Enumeration e = ((Hashtable) obj).keys();
            while (e.hasMoreElements()){
                String s = (String) e.nextElement();
            }
        }
        
        return vec;
    }
     
     
    public static void removeNullFromVector(Vector v){
        while(v != null && v.contains(null)){
            v.remove(null);
        }
    }
    
    
%>
