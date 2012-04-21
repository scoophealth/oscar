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

<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.model.ProviderPreference"%>
<%@ page import="java.sql.*"%>
<%@ page errorPage="/common/error.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean id="scheduleMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="templateBean" class="oscar.ScheduleTemplateBean"
	scope="page" />
<jsp:useBean id="dateTimeCodeBean" class="java.util.Hashtable"
	scope="page" />

<%
	ProviderPreference providerPreference=(ProviderPreference)session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE);
	int startHour=providerPreference.getStartHour();
    int endHour=providerPreference.getEndHour();
    int depth=providerPreference.getEveryMin();
    
    String provider = request.getParameter("providerid");
    String[] param =new String[2];
    param[0]=provider;
    param[1]=request.getParameter("name");

    ResultSet rs = scheduleMainBean.queryResults(param, "search_scheduletemplatesingle");

   String bgcolordef = "#486ebd" ;

     //First search for template where provider_no == provider and name is set
     if(rs.next()) {
       dateTimeCodeBean.put(scheduleMainBean.getString(rs,"provider_no"), scheduleMainBean.getString(rs,"timecode"));
     }
     else {
       //no luck there, so we try for public template with same name
       rs.close();
       param[0] = "Public";
       rs = scheduleMainBean.queryResults(param, "search_scheduletemplatesingle");
       if(rs.next()) {
            provider = scheduleMainBean.getString(rs,"provider_no");
            dateTimeCodeBean.put(provider, scheduleMainBean.getString(rs,"timecode"));
       }
     }
   
     rs.close();
     Object[] rst = scheduleMainBean.queryResultsCaisi("search_scheduletemplatecode");
     rs = (ResultSet)rst[0];
	 
   while (rs.next()) {        
     dateTimeCodeBean.put("description"+scheduleMainBean.getString(rs,"code"), scheduleMainBean.getString(rs,"description"));
     dateTimeCodeBean.put("duration"+scheduleMainBean.getString(rs,"code"), scheduleMainBean.getString(rs,"duration"));
     dateTimeCodeBean.put("color"+scheduleMainBean.getString(rs,"code"), (scheduleMainBean.getString(rs,"color")==null || scheduleMainBean.getString(rs,"color").equals(""))?bgcolordef:scheduleMainBean.getString(rs,"color") );
     dateTimeCodeBean.put("confirm" + scheduleMainBean.getString(rs,"code"), scheduleMainBean.getString(rs,"confirm"));
   }
   ((Statement)rst[1]).close();
          
%>

<table border="1" bgcolor="#486ebd" width="100%">
	<%
            int hourCursor, minuteCursor;
            boolean bColorHour;
            StringBuffer hourmin, hourCode = new StringBuffer((String)dateTimeCodeBean.get(provider));
            for(int ih=startHour*60; ih<=(endHour*60+(60/depth-1)*depth); ih+=depth) { 
                hourCursor = ih/60;
                minuteCursor = ih%60;
                int nLen = 24*60 / ((String) dateTimeCodeBean.get(provider)).length();
                int ratio = (hourCursor*60+minuteCursor)/nLen;
                //hourmin = new StringBuffer((String)dateTimeCodeBean.get(((String) dateTimeCodeBean.get(provider)).substring(ratio,ratio+1)));
                hourmin = new StringBuffer(hourCode.substring(ratio,ratio+1));
                bColorHour=minuteCursor==0?true:false;
         %>
	<tr>
		<td style="color: white; font-size: xx-small" align="RIGHT"
			bgcolor="<%=bColorHour?"#3EA4E1":"#00A488"%>" width="5%" NOWRAP><b>
		<%=(hourCursor<10?"0":"") +hourCursor+ ":"%><%=(minuteCursor<10?"0":"")+minuteCursor%>&nbsp;</a></b></td>
		</td>
		<td style="font-size: xx-small" width='1%'
			<%=dateTimeCodeBean.get("color"+hourmin.toString())!=null?("bgcolor="+dateTimeCodeBean.get("color"+hourmin.toString()) ):""%>
			title='<%=dateTimeCodeBean.get("description"+hourmin.toString())%>'><font
			color='<%=(dateTimeCodeBean.get("color"+hourmin.toString())!=null && !dateTimeCodeBean.get("color"+hourmin.toString()).equals(bgcolordef) )?"black":"white" %>'><%=hourmin.toString() %></font>
	</tr>
	<%  }%>
	</body>
	</html>
