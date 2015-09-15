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
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.util.Set, java.util.List,org.oscarehr.util.LocaleUtils, java.util.Calendar, java.util.GregorianCalendar"%>
<%@page import="org.oscarehr.common.dao.TicklerTextSuggestDao"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"  %>
<%@page import="org.oscarehr.common.model.Provider"  %>
<%@page import="org.oscarehr.common.model.Demographic"  %>
<%@page import="org.oscarehr.common.model.TicklerTextSuggest"  %>
<%@ page import="org.oscarehr.common.model.Tickler" %>
<%@ page import="org.oscarehr.common.model.TicklerComment" %>
<%@ page import="org.oscarehr.common.model.TicklerUpdate" %>
<%@ page import="org.oscarehr.common.model.TicklerLink" %>
<%@ page import="org.oscarehr.common.dao.TicklerLinkDao" %>
<%@ page import="oscar.util.UtilDateUtilities" %>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.managers.TicklerManager" %>

<%
	TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);
   	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_tickler" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_tickler");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%
	String ticklerNoStr = request.getParameter("tickler_no");    
    
    Integer ticklerNo = null;
    try {
        ticklerNo = Integer.valueOf(ticklerNoStr);
    }catch (NumberFormatException e) {}
    
    
    Tickler t = null;
    Demographic d = null;
    if (ticklerNo != null) {
        t = ticklerManager.getTickler(loggedInInfo, ticklerNo);
        d = t.getDemographic();
    }
    else {
        response.sendRedirect("../errorpage.jsp");
    }
             
    java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);

    String selected="";
    String stActive =  LocaleUtils.getMessage(request.getLocale(), "tickler.ticklerMain.stActive");
    String stComplete = LocaleUtils.getMessage(request.getLocale(), "tickler.ticklerMain.stComplete");
    String stDeleted = LocaleUtils.getMessage(request.getLocale(), "tickler.ticklerMain.stDeleted");
    
    String prHigh = LocaleUtils.getMessage(request.getLocale(), "tickler.ticklerMain.priority.high");
    String prNormal = LocaleUtils.getMessage(request.getLocale(), "tickler.ticklerMain.priority.normal");
    String prLow = LocaleUtils.getMessage(request.getLocale(), "tickler.ticklerMain.priority.low");
    
    GregorianCalendar now=new GregorianCalendar();
    int curYear = now.get(Calendar.YEAR);
    int curMonth = (now.get(Calendar.MONTH)+1);
%>

<html:html locale="true">
    <head>
        <style type="text/css">
            td,th{
                font-family: verdana,arial,helvetica;
                font-size:10pt;
                text-align: left;
            }
            td.lilac {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #EEEEFF;}
            td.white {font-weight: normal; font-size: 8pt ; font-family: verdana,arial,helvetica; color: #000000; background-color: #FFFFFF;}
        </style>
        <title><bean:message key="tickler.ticklerEdit.title"/></title>
        <script language="javascript">
            //open a new popup window
            function popupPage(vheight,vwidth,varpage) { 
                var page = "" + varpage;
                windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
                var popup=window.open(page, "attachment", windowprops);
                if (popup != null) {
                    if (popup.opener == null) {
                      popup.opener = self;
                    }
                }
            }
            
            function pasteMessageText() {
                var selectedIdx = document.serviceform.suggestedText.selectedIndex;
                document.serviceform.newMessage.value = document.serviceform.suggestedText.options[selectedIdx].text;
            }
            
            function openBrWindow(theURL,winName,features) { 
                window.open(theURL,winName,features);
            }
            
            function DateAdd(startDate, numDays, numMonths, numYears){
                var returnDate = new Date(startDate.getTime());
                var yearsToAdd = numYears;

                var month = returnDate.getMonth() + numMonths;
                if (month > 11)
                {
                        yearsToAdd = Math.floor((month+1)/12);
                        month -= 12*yearsToAdd;
                        yearsToAdd += numYears;
                }
                returnDate.setMonth(month);
                returnDate.setFullYear(returnDate.getFullYear()	+ yearsToAdd);

                returnDate.setTime(returnDate.getTime()+60000*60*24*numDays);

                return returnDate;
            }
            
            function addMonth(no){      
                var gCurrentDate = new Date();
                var newDate = DateAdd(gCurrentDate, 0, no,0 );
                var newYear = newDate.getFullYear() 
                var newMonth = newDate.getMonth()+1;
                var newDay = newDate.getDate();
                var newD = newYear + "-" + newMonth + "-" + newDay;
                document.serviceform.xml_appointment_date.value = newD;
            }
        </script>
    </head>
    
    <body>
        <html:form action="/tickler/EditTickler">
            <input type="hidden" name="method" value="editTickler"/>
            <input type="hidden" name="ticklerNo" value="<%=ticklerNo%>"/>
        <table width="100%">                       
            <thead>
                <tr style="background-color: black">   
                    <td colspan="4" style="text-align:left; font-weight: 900; height:40px;font-size:large;font-family:arial,sans-serif;color:white"><bean:message key="tickler.ticklerEdit.title"/></td>
                </tr>
                
                <tr>
                    <th style="background-color: #EEEEFF"><bean:message key="tickler.ticklerEdit.demographicName"/></th>                           
                    <td><a href=# onClick="popupPage(600,800,'../demographic/demographiccontrol.jsp?demographic_no=<%=d.getDemographicNo()%>&displaymode=edit&dboperation=search_detail')"><%=d.getLastName()%>,<%=d.getFirstName()%></a></td>
                    <th style="background-color: #EEEEFF"><bean:message key="tickler.ticklerEdit.primaryPhone"/></th>                           
                    <td><%=d.getPhone()%></td> 
                </tr>
                <tr>
                    <th style="background-color: #EEEEFF"><bean:message key="tickler.ticklerEdit.chartNo"/></th>                           
                    <td><%=d.getChartNo()%></td>
                    <th style="background-color: #EEEEFF"><bean:message key="tickler.ticklerEdit.secondaryPhone"/></th>
                    <td><%=d.getPhone2()%></td>
                </tr>
                <tr>
                    <th style="background-color: #EEEEFF"><bean:message key="tickler.ticklerEdit.age"/></th>                           
                    <td><%=d.getAge()%>(<%=d.getFormattedDob()%>)</td> 
                    <th style="background-color: #EEEEFF"><bean:message key="tickler.ticklerEdit.email"/></th>
                    <td><%=d.getEmail()%></td>
                </tr>                
            </thead>
            <tbody style="width:100%;border-bottom-color:#4d8977;border-top-color:#4d8977;">
                <tr>
                    <td colspan="4" style="padding-bottom:2em;"></td>
                </tr>
                <tr>
                    <th colspan="2" style="background-color: #336666;color:white;"><bean:message key="tickler.ticklerEdit.messages"/></th>
                    <th style="background-color: #336666;color:white;"><bean:message key="tickler.ticklerEdit.addedBy"/>
                    <th style="background-color: #336666;color:white;"><bean:message key="tickler.ticklerEdit.updateDate"/>
                </tr>
                
                <%
                                	String cellColour = "lilac";
                                %>
                
                <tr>
                    <td colspan="2" class="<%=cellColour%>" style="font-weight: bold"><%=t.getMessage()%></td>
                    <td class="<%=cellColour%>" style="font-weight: bold"><%=t.getProvider().getLastName()%>,<%=t.getProvider().getFirstName()%></td>
                    <td class="<%=cellColour%>" style="font-weight: bold"><%=t.getUpdateDate()%></td>
                </tr>
                
                    <%
                                    	Set<TicklerComment> tComments = t.getComments(); 
                                                          for (TicklerComment tc : tComments) { 
                                                               if (cellColour.equals("lilac")) {
                                                                    cellColour = "white";
                                                                }else{
                                                                    cellColour = "lilac"; 
                                                                }
                                    %>
               <tr>
                    <td colspan="2" class="<%=cellColour%>"><%=tc.getMessage()%></td>
                    <td class="<%=cellColour%>"><%=tc.getProvider().getLastName()%>,<%=tc.getProvider().getFirstName()%></td>
                    <td class="<%=cellColour%>"><%=tc.getUpdateDateTime(vLocale)%></td>
                </tr>
                    <%}%>
               
            </tbody>
            <tfoot>
                <tr><td colspan="4" style="padding-top:2em;"></td></tr>
                <tr>
                    <th colspan="2" style="background-color: #666699;color:white;"><bean:message key="tickler.ticklerEdit.newMessage"/></th>
                    <th colspan="2" style="background-color: #666699;color:white;"><bean:message key="tickler.ticklerEdit.status"/></th>
                </tr>
                <tr>
                    <td><a href="#" onclick="openBrWindow('./ticklerSuggestedText.jsp','','width=680,height=400')" style="font-weight:bold"><bean:message key="tickler.ticklerEdit.suggestedText"/></a>:</td>
                    <td>
                        <select name="suggestedText">
                            <option value="">---</option>
                            <%   
                                TicklerTextSuggestDao ticklerTextSuggestDao = (TicklerTextSuggestDao) SpringUtils.getBean("ticklerTextSuggestDao");
                                for (TicklerTextSuggest tTextSuggest : ticklerTextSuggestDao. getActiveTicklerTextSuggests()) { %>
                                <option><%=tTextSuggest.getSuggestedText()%></option>
                            <% } %>
                        </select>
                    </td>
                    
                    <td colspan="2">
                        <select name="status"> 
                            <% if (t.getStatusDesc(vLocale).equals(stActive)){selected="selected";}else{selected="";}%>
                            <option <%=selected%> value="A"><bean:message key="tickler.ticklerMain.stActive"/></option>
                            <% if (t.getStatusDesc(vLocale).equals(stComplete)){selected="selected";}else{selected="";}%>
                            <option <%=selected%> value="C"><bean:message key="tickler.ticklerMain.stComplete"/></option>
                            <% if (t.getStatusDesc(vLocale).equals(stDeleted)){selected="selected";}else{selected="";}%>
                            <option <%=selected%> value="D"><bean:message key="tickler.ticklerMain.stDeleted"/></option>                           
                        <select>
                    </td>
                </tr>                
                <tr>
                    <td colspan="2"></td>                    
                    <th colspan="2" style="background-color: #666699;color:white;"><bean:message key="tickler.ticklerEdit.priority"/></th>                   
                </tr>
                
                <tr>   
                    <td><label for="newMessage" style="font-weight:bold"><bean:message key="tickler.ticklerEdit.messageText"/>:</label></td>
                    <td style="text-align:right"><input type="button" name="pasteMessage" onclick="pasteMessageText()" value="<bean:message key="tickler.ticklerEdit.pasteMessage"/>"/></td>
                    <td colspan="2">
                        <select name="priority">  
                            <% if (t.getPriority().equals(prHigh)){selected="selected";}else{selected="";}%>
                            <option <%=selected%> value="<bean:message key="tickler.ticklerMain.priority.high"/>"><bean:message key="tickler.ticklerMain.priority.high"/></option>
                            <% if (t.getPriority().equals(prNormal)){selected="selected";}else{selected="";}%>
                            <option <%=selected%> value="<bean:message key="tickler.ticklerMain.priority.normal"/>"><bean:message key="tickler.ticklerMain.priority.normal"/></option>
                            <% if (t.getPriority().equals(prLow)){selected="selected";}else{selected="";}%>
                            <option <%=selected%> value="<bean:message key="tickler.ticklerMain.priority.low"/>"><bean:message key="tickler.ticklerMain.priority.low"/></option>
                        </select>
                    </td>
                </tr>
                
                <tr>
                    <td colspan="2" rowspan="3" style="text-align:right"><textarea cols="50" name="newMessage"></textarea></td>
                    <th colspan="2" style="background-color: #666699;color:white;"><bean:message key="tickler.ticklerEdit.assignedTo"/></th>         
                </tr>
                <tr>
                       
                    <td colspan="2">
                        <select name="assignedToProviders">
                            <%
                                ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
                                List<Provider> providers = providerDao.getActiveProviders(); 
                                for (Provider p: providers) {
                                    
                                    if (p.equals(t.getAssignee())){
                                        selected = "selected";
                                    }
                                    else {
                                        selected = "";
                                    }
                            %>
                            <option <%=selected%> value="<%=p.getProviderNo()%>"><%=p.getLastName()%>,<%=p.getFirstName()%></option>
                            <%  } %>
                        </select>
                    </td>
                </tr>
                
                <tr>
                 
                    <th colspan="2" style="background-color: #666699;color:white;"><bean:message key="tickler.ticklerEdit.serviceDate"/></th>         
                </tr>
                <tr>
                    <td colspan="2"></td>      
                    <td><input name="xml_appointment_date" type="text" maxlength="10" value="<%=t.getServiceDate()%>"/></td>
                    <td style="text-align:right"><a href="#" onClick="openBrWindow('../billing/billingCalendarPopup.jsp?type=end&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')"><bean:message key="tickler.ticklerEdit.calendarLookup"/></a></td>
                </tr>
                <tr>
                    <td colspan="2"></td>
                    <td style="text-align:right"><a href="#" onClick="addMonth(6)"><bean:message key="tickler.ticklerEdit.add6month"/></a></td>
                    <td style="text-align:right"><a href="#" onClick="addMonth(12)"><bean:message key="tickler.ticklerEdit.add1year"/></a></td>
                </tr>                 
                <tr>
                    <td colspan="2"></td>
                    <td colspan="2" style="vertical-align: bottom;text-align:right"><br/>
                         <oscar:oscarPropertiesCheck property="tickler_email_enabled" value="true">
                            <html:checkbox property="emailDemographic"><bean:message key="tickler.ticklerEdit.emailDemographic"/></html:checkbox>
                         </oscar:oscarPropertiesCheck>
                       
                         <input type="submit" name="updateTickler" value="<bean:message key="tickler.ticklerEdit.update"/>"/>
                         <input type="button" name="cancelChangeTickler" value="<bean:message key="tickler.ticklerEdit.cancel"/>" onClick="window.close()"/>
                    </td>         
                </tr>               
            </tfoot>
        </table>
        </html:form>
    </body>
</html:html>