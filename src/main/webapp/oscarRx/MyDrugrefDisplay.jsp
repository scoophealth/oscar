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

<%--
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

 if we add the id+update_at timestamp to a table. we could use that as a way to see if that had been used already.

 -check for trusted key and hide if not trusted.


--%><%@ page import="java.util.*,oscar.oscarRx.data.*,java.text.DateFormatSymbols,oscar.oscarRx.pageUtil.*,java.io.*,org.apache.xmlrpc.*, oscar.util.StringUtils,java.util.Random"%>


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

<div id="showHideTotal"></div>
<%
    Vector<Hashtable> warnings = (Vector)request.getAttribute("warnings");
    Hashtable hiddenResources = (Hashtable) request.getSession().getAttribute("hideResources");
    if ( warnings != null && warnings.size() > 0){

        int untrustedRes = 0;
        int hiddenRes = 0;
        Random rnd=new Random();
        for (Hashtable ht: warnings){
            Vector<Hashtable> commentsVec = (Vector) ht.get("comments");
            displayKeys(ht);

            boolean trustedResource = trusted(ht.get("trusted"));
            boolean hideResource = false;

            String interactStr=(String)ht.get("interactStr");
            if(interactStr==null) interactStr="";
            if (hiddenResources != null ) {
                hideResource = hiddenResources.containsKey("mydrugref"+ht.get("id"));
            }

            String hidden ="";
            if (!trustedResource){
                untrustedRes++;
                hidden ="display:none;";
            }
            if (hideResource){
                hiddenRes++;
                hidden ="display:none;";
            }
            String showHidden="";
            if(hidden.equals("display:none;"));
            else
                showHidden="display:none;";
            String bodyStr=(String)ht.get("body");
            int rand=Math.abs(rnd.nextInt());
            String elementId=ht.get("id")+"."+getTime(ht.get("updated_at"));
          Date lastUpdateTime=(Date)ht.get("updated_at");
          String lastUpdateDate=getDateMonthYear(lastUpdateTime);
            %>

<div id="<%=elementId%>"
	<%=outputHtmlClass(trustedResource,hideResource)%>
	style="font-size: 9pt;<%=hidden%>background-color:<%=sigColor(""+ht.get("significance"))%>;margin-right:3px;margin-left:3px;margin-top:2px;padding-left:3px;padding-top:3px;padding-bottom:3px;">
<span style="float: right;"><a href="javascript:void(0);"
	onclick="HideW('<%=ht.get("id")%>.<%=getTime(ht.get("updated_at"))%>','<%=ht.get("id")%>','<%=getTime(ht.get("updated_at"))%>')">Hide</a></span>
        <%=interactStr%><br/>
<b><%=ht.get("name")%></b> From:<%=s(ht.get("author"))%> <br/>
Last Update:<%=s(lastUpdateDate)%><br/>

<%if(bodyStr.length() > 90){%>
<%=((String)ht.get("body")).substring(0,90)%><a id="addText_<%=rand%>" style="display:none;padding:2px;"><%=((String)ht.get("body")).substring(90)%>
</a>
<div><a href="javascript:void(0);" id="addTextWord_<%=rand%>" onclick="showAddText('<%=rand%>');"><span>more</span></a></div>

<%} else{%>
<%=ht.get("body")%>
<%}%>
     <br/><!--br/><%--=interactStr--%><br/-->
<% String ref = (String)ht.get("reference");%> <%if (commentsVec != null && commentsVec.size() > 0){ %>
<a style="float: right;" href="javascript:void(0);"
	onclick="$('comm.<%=ht.get("id")%>.<%=getTime(ht.get("updated_at"))%>').toggle();">comments</a>
<%}%> (<%=ht.get("evidence")%>) &nbsp;Reference: <a href="<%=ht.get("reference")%>" target="_blank"><%= StringUtils.maxLenString(ref, 51, 50, "...") %></a>



<%if (commentsVec != null && commentsVec.size() > 0){
     %> <!--a style="float:right;" href="javascript:void(0);" onclick="$('comm.<%=ht.get("id")%>.<%=getTime(ht.get("updated_at"))%>').toggle();">comments</a-->
<fieldset style="border: 1px solid white; display: none; padding: 2px;"	id="comm.<%=ht.get("id")%>.<%=getTime(ht.get("updated_at"))%>">
<legend>Comments</legend> <%for(Hashtable comment : commentsVec){ %>
<div><%= getCommentDisplay( comment ) %></div>
<%}%>
</fieldset>
<%}%>
</div>
<div id="show_<%=elementId%>" style="text-align: right; <%=showHidden%>font-size: 9pt; background-color:<%=sigColor(""+ht.get("significance"))%>;margin-right:3px;margin-left:3px;margin-top:2px;padding-left:3px;padding-top:3px;padding-bottom:3px;">
    <a  href="javascript:void(0);" onclick="ShowW('<%=elementId%>','<%=ht.get("id")%>','<%=getTime(ht.get("updated_at"))%>');">Show</a>
</div>
<%
        }
        if (untrustedRes > 0 ){ %>
<div><a href="javascript:void(0);" onclick="showUntrustedRes();"><span
	id="showUntrustedResWord">show</span> <%=untrustedRes%> untrusted
resources</a></div>
<%}

%>
    <%}else if(warnings == null){ %>
<div>MyDrug to MyDrug Warning Service not available</div>
<%  }   %>

<script type="text/javascript">
    new Ajax.Updater('showHideTotal','updateMyDrugrefResource.jsp',{method:'get',onSuccess:function(transport){
                oscarLog("updated showHideTotal ");
            }});
</script>



<%!

     String getCommentDisplay(Hashtable h){
         StringBuffer ret = new StringBuffer();
         //key:comments val [{created_at=Fri Apr 04 14:35:10 EDT 2008, name=Re: APO-WARFARIN 10MG, id=4053, post_id=133, author=Guest, goat=true, created_by=7, body=i disagree , updated_at=Fri Apr 04 14:35:10 EDT 2008}]  class : java.util.Vector
//key:type val Warning  class : java.lang.String
         if (h != null){
             Boolean thumbs = (Boolean) h.get("goat");
             boolean thumb = true;
             if(thumbs != null && thumbs.booleanValue()){
                 ret.append("+");
             }else{
                 ret.append("-");
             }

             ret.append("From: "+h.get("author")+", "+h.get("body"));
             //author=Guest, goat=true, created_by=7, body=i disagree
         }
         return ret.toString();
     }

      String s(Object o){
          String ret ="";

          if(o != null && o instanceof String){
              return (String) o;
          }

          return ret;
      }


       String outputHtmlClass(boolean trustedResource, boolean hideResource){
           if (!trustedResource && hideResource){
               return "class=\"untrustedResource hiddenResource\"";
           }else if (trustedResource && hideResource){
               return "class=\"hiddenResource\"";
           }else if (!trustedResource && !hideResource){
               return "class=\"untrustedResource\"";
           }
           return "";
       }


       boolean trusted(Object o){
           boolean b = false;
           if (o != null && o instanceof Boolean){
              Boolean c  = (Boolean) o;
              b = c.booleanValue();
           }
           return b;
       }

       long getTime(Object o){
            Date d = (Date) o;
            return d.getTime();
        }
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

   String displayKeys(Hashtable ht) {
       StringBuffer sb = new StringBuffer();
       if (ht != null){
            for (Object o :ht.keySet()){
                String s  = "key:"+o+" val "+ht.get(o)+"  class : "+ht.get(o).getClass().getName();
                sb.append(s);
            }
       }
       return sb.toString();
   }


%>
