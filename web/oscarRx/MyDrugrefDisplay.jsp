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
 
 
 COMING  BACK FROM DRUGREF
 
 name AVANDIA 2MG java.lang.String
 updated_by 3 java.lang.Integer
 id 70 java.lang.Integer
 evidence G java.lang.String
 cost 0.0 java.lang.Double
 reference NEJM article 2007 java.lang.String
 atc A10BG02 java.lang.String
 created_by 3 java.lang.Integer
 body May increase cardiovascular morbidity in high risk patients  java.lang.String
 type Warning java.lang.String
 updated_at Thu Nov 15 10:18:22 EST 2007 java.util.Date
 significance 3 java.lang.String
//
trusted truejava.lang.Boolean ? i think
 
 TODO:
 -track id, updated_at.  Allow someone to hide warnings they no longer require but reshow them if the updated_at date changes
 -check for trusted key and hide if not trusted.
 
 
--%><%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="java.util.*,oscar.oscarRx.data.*,oscar.oscarRx.pageUtil.*,java.io.*,org.apache.xmlrpc.*, oscar.util.StringUtils" %>
<%
    Vector warnings = (Vector)request.getAttribute("warnings");
     //warnings = (Vector) warnings.get(0);
    Object[] warningsArray = warnings.toArray();
    System.out.println("numb warnings "+warnings.size());
    if ( warningsArray != null && warningsArray.length > 0){
        for (int i=0; i < warningsArray.length; i++){
            System.out.println("type "+warningsArray[i].getClass().getName());
            Hashtable ht = (Hashtable) warningsArray[i]; 
            
            
            System.out.println("\nDrug: "+ht.get("name")+"\nEvidence: "+ht.get("evidence")+"\nSignificance: "+ht.get("significance")+"\nATC: "+ht.get("atc")+"\nReference: "+ht.get("reference")+"\nWarning: "+ht.get("body"));%>
            
            <div style="background-color:<%=sigColor(""+ht.get("significance"))%>;margin-right:3px;margin-left:3px;margin-top:2px;padding-left:3px;padding-top:3px;padding-bottom:3px;">
                <span style="float:right;"><a href="javascript:void(0);" onclick="HideW('<%=ht.get("id")%>','<%=getTime(ht.get("updated_at"))%>')" >Hide</a></span><b><%=ht.get("name")%></b>  <br/>
                
            <%=ht.get("body")%><br/> <% String ref = (String)ht.get("reference");%>
                (<%=ht.get("evidence")%>) &nbsp;Reference: <a href="<%=ht.get("reference")%>"target="_blank"><%= StringUtils.maxLenString(ref, 51, 50, "...") %></a>
            </div>

      <%}
    }else if(warnings == null){ %>
        <div>MyDrug to MyDrug Warning Service not available</div>                          
<%  }   %>
<%!
        
       long getTime(Object o){
            Date d = (Date) o;
            return d.getTime();
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
       h.put("P","Poor");
       h.put("F","Fair");
       h.put("G","Good");
       h.put(" ","Unknown");
       
       String retval = (String) h.get(s);
        if (retval == null) {retval = "Unknown";}
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
    
%>