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

<%@ page
	import="java.util.*,oscar.oscarRx.data.*,oscar.oscarRx.pageUtil.*"%>
<%
RxSessionBean bean = (RxSessionBean) session.getAttribute("RxSessionBean");
if ( bean == null ){
    return;
}

     RxDrugData.Interaction[] interactions = (RxDrugData.Interaction[]) bean.getInteractions();
     if (interactions != null && interactions.length > 0){
        for (int i = 0 ; i < interactions.length; i++){  %>
<div
	style="background-color:<%=sigColor(interactions[i].significance)%>;margin-right:100px;margin-left:20px;margin-top:10px;padding-left:10px;padding-top:10px;padding-bottom:5px;border-bottom: 2px solid gray;border-right: 2px solid #999;border-top: 1px solid #CCC;border-left: 1px solid #CCC;width:300px;">
<%=interactions[i].affectingdrug%> <%=effect(interactions[i].effect)%> <%=interactions[i].affecteddrug%>
&nbsp;&nbsp;&nbsp;&nbsp;SIGNIFICANCE = <%=significance(interactions[i].significance)%>
&nbsp;&nbsp;&nbsp;EVIDENCE = <%=evidence(interactions[i].evidence)%><br />
<%=interactions[i].comment%></div>
<%      }
    }else if(interactions == null && bean.getStashSize() > 1){ %>
<div>Drug to Drug Interaction Service not available</div>
<%  }   %>
<%!

    String effect(String s){
		  Hashtable h = new Hashtable();
        h.put("a","augments (no clinical effect)");
        h.put("A","augments");
        h.put("i","inhibits  (no clinical effect)");
        h.put("I","inhibits");
        h.put("n","has no effect on");
        h.put("N","has no effect on");
        h.put(" ","unknown effect on");

        String retval = (String) h.get(s);
        if (retval == null) {retval = "unknown effect on";}
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
%>
