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

<%@ page import="java.util.*,oscar.oscarRx.data.*,oscar.oscarRx.pageUtil.*"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_rx" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_rx");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%
RxSessionBean bean = (RxSessionBean) session.getAttribute("RxSessionBean");
if ( bean == null ){
    return;
}
Vector<Hashtable> warnings = (Vector)request.getAttribute("warnings");
    if ( warnings != null && warnings.size() > 0){

        int untrustedRes = 0;
        int hiddenRes = 0;
        Random rnd=new Random();
        for (Hashtable ht: warnings){
            Vector<Hashtable> commentsVec = (Vector) ht.get("comments");

            boolean trustedResource = trusted(ht.get("trusted"));

            String interactStr=(String)ht.get("interactStr");
            if(interactStr!=null && interactStr.trim().length()>0){

            %>
<div
	style="background-color:<%=sigColor((String)ht.get("significance"))%>;margin-right:100px;margin-left:20px;margin-top:10px;padding-left:10px;padding-top:10px;padding-bottom:5px;border-bottom: 2px solid gray;border-right: 2px solid #999;border-top: 1px solid #CCC;border-left: 1px solid #CCC;">
<%=interactStr%>
&nbsp;&nbsp;&nbsp;&nbsp;SIGNIFICANCE = <%=significance((String)ht.get("significance"))%>
&nbsp;&nbsp;&nbsp;EVIDENCE = <%=evidence((String) ht.get("evidence"))%><br />
<%--=commentsVec--%></div>
<%              }else;
        }
    }else if(warnings == null && bean.getStashSize() > 1){ %>
<div>Drug to Drug Interaction Service not available</div>
<%  }   %>
<%!

   String significance(String s){
       Hashtable h = new Hashtable();
       h.put("1","minor");
       h.put("2","moderate");
       h.put("3","major");
       h.put(" ","unknown");

       String retval=null;
       if(s!=null){
         retval = (String) h.get(s);
         if (retval == null) {
            retval = "unknown";
         }
       }else retval = "unknown";
        return retval;
   }

   String evidence(String s){
       Hashtable h = new Hashtable();
       h.put("P","poor");
       h.put("F","fair");
       h.put("G","good");
       h.put(" ","unknown");

       String retval;
      if(s!=null){
           retval = (String) h.get(s);
        if (retval == null) {retval = "unknown";}
      }
       else retval="unkown";
        return retval;
   }


   String sigColor(String s){
       Hashtable h = new Hashtable();
       h.put("1","yellow");
       h.put("2","orange");
       h.put("3","red");
       h.put(" ","greenyellow");
       String retval;
       if(s!=null){
            retval = (String) h.get(s);
            if (retval == null) {retval = "greenyellow";}
       }else retval = "greenyellow";
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

   String displayKeys(Hashtable ht) {
       StringBuffer sb = new StringBuffer();
       if (ht != null){
            for (Object o :ht.keySet()){
                String s  = "key:"+o+" val "+ht.get(o)+"  class : "+ht.get(o).getClass().getName();
                sb.append(s);

                if ( o instanceof Vector){
                   Vector v =  (Vector) o;
                }
            }
       }

       return sb.toString();
   }

  boolean trusted(Object o){
           boolean b = false;
           if (o != null && o instanceof Boolean){
              Boolean c  = (Boolean) o;
              b = c.booleanValue();
           }
           return b;
       }
%>
