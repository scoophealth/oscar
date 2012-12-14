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

<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite" %>
<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat,org.caisi.model.*,org.caisi.dao.*,oscar.util.*,org.oscarehr.common.model.*,org.oscarehr.common.dao.*"  %>


<%@page import="org.oscarehr.util.MiscUtils"%>

<%

//GregorianCalendar now=new GregorianCalendar();
//  int curYear = now.get(Calendar.YEAR);
//  int curMonth = (now.get(Calendar.MONTH)+1);
//  int curDay = now.get(Calendar.DAY_OF_MONTH);

 // String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);

String module="", module_id="", doctype="", docdesc="", docxml="", doccreator="", docdate="", docfilename="", docpriority="", docassigned="";
module_id = request.getParameter("demographic_no");
doccreator = request.getParameter("user_no");
docdate = request.getParameter("xml_appointment_date");
docfilename =request.getParameter("textarea");
docpriority =request.getParameter("priority");
docassigned =request.getParameter("task_assigned_to");

String docType =request.getParameter("docType");
String docId = request.getParameter("docId");



Tickler tickler = new Tickler();
    tickler.setDemographic_no(module_id);
    tickler.setStatus('A');
    tickler.setUpdate_date(new java.util.Date());
    tickler.setPriority(docpriority);
    tickler.setTask_assigned_to(docassigned);
    tickler.setCreator(doccreator);
    tickler.setMessage(docfilename);
    tickler.setService_date(UtilDateUtilities.StringToDate(docdate));


   WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
   TicklerDAO ticklerDAO = (TicklerDAO) ctx.getBean("ticklerDAOT");
   ticklerDAO.saveTickler(tickler);


   if (docType != null && docId != null && !docType.trim().equals("") && !docId.trim().equals("") && !docId.equalsIgnoreCase("null") ){

      long ticklerNo = tickler.getTickler_no();
      if (ticklerNo > 0){
          try{
             TicklerLink tLink = new TicklerLink();
             tLink.setTableId(Long.parseLong(docId));
             tLink.setTableName(docType);
             tLink.setTicklerNo(new Long(ticklerNo).intValue());
             TicklerLinkDao ticklerLinkDao = (TicklerLinkDao) ctx.getBean("ticklerLinkDao");
             ticklerLinkDao.save(tLink);
             }catch(Exception e){
            	 MiscUtils.getLogger().error("No link with this tickler", e);
             }
      }
   }

   boolean rowsAffected = true;

String parentAjaxId = request.getParameter("parentAjaxId");
String updateParent = request.getParameter("updateParent");

if (rowsAffected ){
%>
<script LANGUAGE="JavaScript">

      var parentId = "<%=parentAjaxId%>";
      var updateParent = <%=updateParent%>;
      var demo = "<%=module_id%>";
      var Url = window.opener.URLs;

      /*because the url for demomaintickler is truncated by the delete action, we need
        to reconstruct it if necessary
      */
      if( parentId != "" && updateParent == true && !window.opener.closed ) {
        var ref = window.opener.location.href;
        if( ref.indexOf("?") > -1 && ref.indexOf("updateParent") == -1 )
            ref = ref + "&updateParent=true";
        else if( ref.indexOf("?") == -1 )
            ref = ref + "?demoview=" + demo + "&parentAjaxId=" + parentId + "&updateParent=true";

        window.opener.location = ref;
      }
      else if( parentId != "" && !window.opener.closed ) {
        window.opener.document.forms['encForm'].elements['reloadDiv'].value=parentId;
        window.opener.updateNeeded = true;
      }
      else if( updateParent == true && !window.opener.closed )
        window.opener.location.reload();

      self.close();
</script>
<%}%>
