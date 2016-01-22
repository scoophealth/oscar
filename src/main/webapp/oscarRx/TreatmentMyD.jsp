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

<%@ page import="java.util.*,oscar.oscarRx.data.*,oscar.oscarRx.pageUtil.*,java.text.DateFormatSymbols,java.io.*,org.apache.xmlrpc.*"%>
<%

RxSessionBean bean = (RxSessionBean) session.getAttribute("RxSessionBean");
if ( bean == null ){
    return;
}
    String treatment = request.getParameter("cond");
    if (treatment == null || treatment.trim().equals("")){
        %>
        
<%@page import="org.oscarehr.util.MiscUtils"%><div style="background-color:white;margin:100px;padding:40px;border:2px solid grey">
            <a href="javascript: function myFunction() {return false; }" onclick="$('treatmentsMyD').toggle();">NOTHING FOUND</a>
        </div>
        <%
           return ;
    }
    Vector prices = getTreatment(treatment);
    Object[] pricesArray = null;
    if (prices != null){
    pricesArray = prices.toArray();
    }
    if ( pricesArray != null && pricesArray.length > 0){
        for (int i=0; i < pricesArray.length; i++){
            Hashtable ht = (Hashtable) pricesArray[i]; 

            String author=(String)ht.get("author");
            if(author==null) author="";
            Date updateTime=(Date)ht.get("updated_at");
            String updateStr=getDateMonthYear(updateTime);
            String content=((String) ht.get("body")).replaceAll("\n","<br>");
    %>

<div id="treatment_<%=i%>" style="background-color:<%=sigColor(""+ht.get("significance"))%>;margin-right:10px;margin-left:20px;margin-top:10px;padding-left:10px;padding-top:10px;padding-bottom:5px;border-bottom: 2px solid gray;border-right: 2px solid #999;border-top: 1px solid #CCC;border-left: 1px solid #CCC;">
    <table width="100%" cellspacing="0">
    <tr>
        <td align="left">
            <strong><%=ht.get("name")%>&nbsp;&nbsp;&nbsp;&nbsp;:</strong>
        </td>
        <td align="right">
            <a href="javascript: function myFunction() {return false; }" onclick="$('treatmentsMyD').toggle();" style="text-decoration: none;font-size:larger">X</a>
        </td>
    </tr>
    <tr>
        <td align="right" style="font-size:10px">
            <i>Reference: <a href="http://know2act.org" target="_blank"></i>know2act.org</a><br />
            <em>From:<%=author%></em>
        </td>
    </tr>
    <tr>
        <td align="right" style="font-size:10px">
            <em>Last Update:<%=updateStr%></em>
        </td>
    </tr>
</table>
<%
if (ht.get("drugs") != null){
    Vector<Hashtable> drugs = (Vector) ht.get("drugs");
    for(Hashtable drug :drugs){%>
<%=DrugLing((String)drug.get("label"))%>  <a href="javascript:void(0);" onclick="$('searchString').value ='<%=drug.get("tc_atc")%>';$('treatmentsMyD').toggle();$('searchString').focus();"><%=drug.get("tc_atc")%></a>  <%=drug.get("tc_atc_number")%> <br/>
  <%}
}          
if(content.length()<100){%>
            <%=content%>
<%}else{  %>
<%=content.substring(0,100)%><a id="addText_<%=i%>" style="display:none;padding:2px;"><%=content.substring(100)%></a>
<div><a href="javascript:void(0);" id="addTextWord_<%=i%>" onclick="showAddText('<%=i%>');" ><span>more</span></a></div>
<%}%>
</div>

<%}
    }else if(prices == null){ %>
<div style="background-color:green;margin-right:10px;margin-left:20px;margin-top:10px;padding-left:10px;padding-top:10px;padding-bottom:5px;border-bottom: 2px solid gray;border-right: 2px solid #999;border-top: 1px solid #CCC;border-left: 1px solid #CCC;">
MyDrug to MyDrug Price Service not available -- <a href="javascript: function myFunction() {return false; }" onclick="$('treatmentsMyD').toggle();" style="text-decoration: none;">CLOSE</a></div>
<%  }else{   %>
 <div style="background-color:white;margin:100px;padding:40px;border:2px solid grey">
            <a href="javascript: function myFunction() {return false; }" onclick="$('treatmentsMyD').toggle();">NOTHING FOUND</a>
        </div>


<%}%>









<%!

 String getDateMonthYear(Date d){
          if(d==null) return "";
          Calendar cal=Calendar.getInstance();
          cal.setTime(d);
          Integer date=cal.get(Calendar.DATE);
          Integer month=cal.get(Calendar.MONTH);
          Integer year=cal.get(Calendar.YEAR);
          String[] shortMonths = new DateFormatSymbols().getShortMonths();
          String shortMonth=shortMonths[month];
          String retStr=date+"-"+shortMonth+"-"+year;
          return retStr;
        }
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
        Object object = null;
        String server_url = "http://know2act.org/backend/api";
        try{
            if (!System.getProperty("http.proxyHost","").isEmpty()) {
                //The Lite client won't recgonize JAVA_OPTS as it uses a customized http
                XmlRpcClient server = new XmlRpcClient(server_url);
                object = (Object) server.execute(procedureName, params);
            } else {
                XmlRpcClientLite server = new XmlRpcClientLite(server_url);
                object = (Object) server.execute(procedureName, params);
            }
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
    
    
     public Vector getTreatment(String  treatment)throws Exception{
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
        }
        return vec;
    }
     
     
    public static void removeNullFromVector(Vector v){
        while(v != null && v.contains(null)){
            v.remove(null);
        }
    }
    
    
%>
