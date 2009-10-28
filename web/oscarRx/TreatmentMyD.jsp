<%--
 * Copyright (c) 2007. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 *  Jason Gallagher
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
--%><%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,oscar.oscarRx.data.*,oscar.oscarRx.pageUtil.*,java.io.*,org.apache.xmlrpc.*"%>
<%

RxSessionBean bean = (RxSessionBean) session.getAttribute("RxSessionBean");
if ( bean == null ){
    return;
}
    String treatment = request.getParameter("cond");
    if (treatment == null || treatment.trim().equals("")){
        %>
        <div style="background-color:white;margin:100px;padding:40px;border:2px solid grey">
            <a href="javascript: function myFunction() {return false; }" onclick="hidepic('treatmentsMyD');">NOTHING FOUND</a>
        </div>
        <%
           return ;
    }
    Vector prices = getTreatment(treatment);
    Object[] pricesArray = null;
    if (prices != null){
    System.out.println("CODES count "+prices.size());
    pricesArray = prices.toArray();
    }
    if ( pricesArray != null && pricesArray.length > 0){
        for (int i=0; i < pricesArray.length; i++){
            Hashtable ht = (Hashtable) pricesArray[i]; 
            Enumeration en = ht.keys();
            
            while(en.hasMoreElements()){
                Object s = en.nextElement();
                System.out.println(s+" -- "+ht.get(s));
            }
            
            System.out.println("\nName: "+ht.get("name")
            +"\nPrice: "+ht.get("cost")
            +"\nRetailer: "+ht.get("reference"));%>

<div style="background-color:<%=sigColor(""+ht.get("significance"))%>;margin-right:10px;margin-left:20px;margin-top:10px;padding-left:10px;padding-top:10px;padding-bottom:5px;border-bottom: 2px solid gray;border-right: 2px solid #999;border-top: 1px solid #CCC;border-left: 1px solid #CCC;">
<%=ht.get("name")%>&nbsp;&nbsp;&nbsp;&nbsp;: <a href="javascript: function myFunction() {return false; }" onclick="hidepic('treatmentsMyD');" style="text-decoration: none;">CLOSE</a>
<br/>
<%
if (ht.get("drugs") != null){
    Vector<Hashtable> drugs = (Vector) ht.get("drugs");
    for(Hashtable drug :drugs){%>
<%=DrugLing((String)drug.get("label"))%>  <a href="javascript:void(0);" onclick="$('searchString').value ='<%=drug.get("tc_atc")%>';hidepic('treatmentsMyD');$('searchString').focus();"><%=drug.get("tc_atc")%></a>  <%=drug.get("tc_atc_number")%> <br/>
  <%}
}%>




<%-- pre style="width:600px;" --%>
<%=((String) ht.get("body")).replaceAll("\n","<br>")%>
<%-- /pre --%>
</div>

<%}
    }else if(prices == null){ %>
<div style="background-color:green;margin-right:10px;margin-left:20px;margin-top:10px;padding-left:10px;padding-top:10px;padding-bottom:5px;border-bottom: 2px solid gray;border-right: 2px solid #999;border-top: 1px solid #CCC;border-left: 1px solid #CCC;">
MyDrug to MyDrug Price Service not available -- <a href="javascript: function myFunction() {return false; }" onclick="hidepic('treatmentsMyD');" style="text-decoration: none;">CLOSE</a></div>
<%  }else{   %>
 <div style="background-color:white;margin:100px;padding:40px;border:2px solid grey">
            <a href="javascript: function myFunction() {return false; }" onclick="hidepic('treatmentsMyD');">NOTHING FOUND</a>
        </div>


<%}%>









<%!


String DrugLing(String s){
       Hashtable h = new Hashtable();
       h.put("FLD","First Line");
       h.put("SLD","Second Line");
       h.put("TLD","Third Line");
       h.put(" ","unknown");

       String retval = (String) h.get(s);
        if (retval == null) {retval = "unknown";}
        return retval;
   }
    
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
        System.out.println("#CALLDRUGREF-"+procedureName);
        Object object = null;
        //String server_url = "http://dev2.mydrugref.org/backend/api";
        //String server_url = "http://130.113.106.88:3000/backend/api";
        String server_url = "http://mydrugref.org/backend/api";
        try{
            System.out.println("server_url :"+server_url);
            XmlRpcClientLite server = new XmlRpcClientLite(server_url);
            object = (Object) server.execute(procedureName, params);
        }catch (XmlRpcException exception) {
            
            System.err.println("JavaClient: XML-RPC Fault #" +
                    Integer.toString(exception.code) + ": " +
                    exception.toString());
            exception.printStackTrace();
            
            throw new Exception("JavaClient: XML-RPC Fault #" +
                    Integer.toString(exception.code) + ": " +
                    exception.toString());
            
        } catch (Exception exception) {
            System.err.println("JavaClient: " + exception.toString());
            exception.printStackTrace();
            throw new Exception("JavaClient: " + exception.toString());
        }
        return object;
    }
    
    
     public Vector getTreatment(String  treatment)throws Exception{
         System.out.println("TREATMENT IS "+treatment);
        if (treatment == null){
            return null;
        }

        Vector params = new Vector();
        params.addElement(treatment);
        Vector vec = new Vector();
        Object obj =  callWebserviceLite("GetTreatments",params);
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
                System.out.println(s+" "+((Hashtable) obj).get(s)+" "+((Hashtable) obj).get(s).getClass().getName());
            }
        }
        System.out.println("RETURN TREAT VEC +"+vec.size());
        return vec;
    }
     
     
    public static void removeNullFromVector(Vector v){
        while(v != null && v.contains(null)){
            v.remove(null);
        }
    }
    
    
%>
