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
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.oscarehr.common.model.ScheduleDate"%>
<%@page import="org.oscarehr.common.dao.ScheduleDateDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.oscarehr.common.dao.UserPropertyDAO"%>
<%@page import="org.oscarehr.common.model.UserProperty"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%
UserPropertyDAO userPropertyDao = SpringUtils.getBean(UserPropertyDAO.class);

 String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
	LoggedInInfo loggedInInfo =LoggedInInfo.getLoggedInInfoFromSession(request);
	String providerNo=loggedInInfo.getLoggedInProviderNo();
   			

  String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%
	String targetProviderNo = request.getParameter("provider_no");
	Provider provider = null;
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	provider = providerDao.getProvider(targetProviderNo);
	
	List<Provider> providers = providerDao.getActiveProviders();

	String sessionDateStr = request.getParameter("date");	
	
	//series level attributes
	UserProperty seriesNameUp = userPropertyDao.getProp(targetProviderNo,"seriesName");
	UserProperty seriesSiteUp = userPropertyDao.getProp(targetProviderNo,"seriesSite");
	UserProperty seriesNoteUp = userPropertyDao.getProp(targetProviderNo,"seriesNote");
	UserProperty completedUp = userPropertyDao.getProp(targetProviderNo,"completed");
	
	//setup dates for this series
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	ScheduleDateDao scheduleDateDao = SpringUtils.getBean(ScheduleDateDao.class);
	List<ScheduleDate> sdList = scheduleDateDao.findActiveByProviderAndHour(targetProviderNo,"Group Series");
	List<String> dates = new ArrayList<String>();
	boolean dateFound=false;
	for(ScheduleDate sd:sdList) {
		String tmp = formatter.format(sd.getDate());
		if(tmp.equals(sessionDateStr)){
			dateFound=true;
		}
		dates.add(tmp);
	}
	
	if(!dateFound && dates.size()>0) {
		sessionDateStr = dates.get(0);
		System.out.println("resetting date");
	}
		
	
	//session level attriibutes
	UserProperty facilitatorUp = userPropertyDao.getProp(targetProviderNo, "session_" + sessionDateStr + "_facilitator");
	UserProperty facilitator2Up = userPropertyDao.getProp(targetProviderNo, "session_" + sessionDateStr + "_facilitator2");
	UserProperty sessionSiteUp = userPropertyDao.getProp(targetProviderNo, "session_" + sessionDateStr + "_site");
	
	List<UserProperty> topics = new ArrayList<UserProperty>();
	UserProperty numTopicsUp = userPropertyDao.getProp(targetProviderNo, "session_" + sessionDateStr + "_num_topics");
	if(numTopicsUp != null) {
		int numTopics = Integer.parseInt(numTopicsUp.getValue());
		for(int x=1;x<=numTopics;x++) {
			UserProperty tmp = userPropertyDao.getProp(targetProviderNo,"session_" + sessionDateStr + "_topic" + x);
			if(tmp != null) {
				topics.add(tmp);
			}
		}
	}
	
%>
<html:html locale="true">

<head>
	<title>Manage Group Series</title>
	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/js/jquery_css/smoothness/jquery-ui-1.10.2.custom.min.css" type="text/css">
	<link rel="stylesheet" type="text/css" href='<html:rewrite page="/jsCalendar/skins/aqua/theme.css" />' />
	<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/share/calendar/calendar.css" title="win2k-cold-1" />
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar-setup.js"></script>

	<c:set var="ctx" value="${pageContext.request.contextPath}" />
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>

	<script>jQuery.noConflict();</script>

	<script>
	function addTopic() {
		var total = jQuery("#topics_num").val();
		total++;
		jQuery("#topics_num").val(total);
		jQuery.ajax({url:'group_topic.jsp?id='+total,async:false, success:function(data) {
			  jQuery("#topics_container").append(data);
		}});
	}
	
	function deleteTopic(id) {
		var topicId = jQuery("input[name='topic_"+id+".id']").val();
		jQuery("form[name='myForm']").append("<input type=\"hidden\" name=\"topic.delete\" value=\""+topicId+"\"/>");
		jQuery("#topic_"+id).remove();
	}
	
	function setSelect(id,type,name,val) {
		jQuery("select[name='"+type+"_"+id+"."+name+"']").each(function() {
			jQuery(this).val(val);
		});
	}
	
	function setInput(id,type,name,val) {
		jQuery("input[name='"+type+"_"+id+"."+name+"']").each(function() {
			jQuery(this).val(val);
		});
	}
	
	jQuery(document).ready(function() {
		<%
			for(UserProperty up : topics) {
				%>
					addTopic();
					var num = jQuery("#topics_num").val();
					setInput(num,'topic','text','<%=up.getValue()%>');
				<%
			}
		%>
	});
	
	jQuery(document).ready(function(){
	
	        var url = "<%= request.getContextPath() %>/demographic/SearchDemographic.do?jqueryJSON=true&activeOnly=true";
	
	        jQuery("#demo").autocomplete( {
	                source: url,
	                minLength: 2,
	
	                focus: function( event, ui ) {
	                		jQuery("#demo").val( ui.item.label );
	                        return false;
	                },
	                select: function( event, ui ) {
	                		jQuery("#demo").val( ui.item.label );
	                		jQuery("#demographic_no").val( ui.item.value );
	                		addDemographicToSession(jQuery("#demographic_no").val());
	                        return false;
	                }
	        });
	
	});
	
	function addDemographicToSession(demographicNo) {
		jQuery.post("<%=request.getContextPath()%>/groupAppointment.do" , {method: 'addParticipantToSeries',demographicNo:demographicNo,providerNo:'<%=targetProviderNo%>',date:'<%=sessionDateStr%>'},
	            function(xml)
	            {
					if(xml.error) {
						alert("ERROR:" + xml.error);
						jQuery("#demo").val('');
						jQuery("#demographic_no").val('');
					} else {
						jQuery("#demo").val('');
						jQuery("#demographic_no").val('');
						updateParticipants();
					}
	            }, "json"
	        );	
	}
	
	function setStatus(appointmentNo, status) {
		jQuery.post("<%=request.getContextPath()%>/groupAppointment.do" , {method: 'updateStatus',appointmentNo:appointmentNo,status:status},
	            function(xml)
	            {
					if(xml.error) {
						alert("ERROR:" + xml.error);
					}
					updateParticipants();
	            }, "json"
		);
	}
	
	function adjustValue(val,type) {
		var currentValue = parseInt(jQuery("#" + type).html());
		var newValue = currentValue + val;
		
		if(newValue >= 0) {
			jQuery("#" + type).html(newValue);
		}
	}
	
	function applyGroupNote() {
		
		var appts = "";
		jQuery("input[name='checked']:checked").each(function(index,value){
			if(appts.length>0)
				appts = appts + "," + value.value;
			else 
				appts = value.value;
		});
		
		if(appts.length == 0) {
			alert('No participants have been selected!');
			return;
		}
		
		var note = jQuery("#groupNote").val().trim();
		
		if(note.length == 0) {
			alert('Please enter a note!');
			return;
		}
		
		jQuery.post("<%=request.getContextPath()%>/groupAppointment.do" , {method: 'saveGroupNote',appointmentNos:appts,note:note},
	            function(xml)
	            {
					if(xml.error) {
						alert("ERROR:" + xml.error);
					} else {
						alert('Note saved');
						jQuery("#groupNote").val('');
					}
	            }, "json"
		);
		
	}
	
	function saveValues(appointmentNo) {
		
		var bus_tickets = jQuery("#bus_ticket_" + appointmentNo).html();
		var vouchers = jQuery("#voucher_" + appointmentNo).html();
		var guests = jQuery("#guests_" + appointmentNo).val();
		
		var providerNo = '<%=targetProviderNo%>';
		var d = '<%=sessionDateStr%>';
		
		
		jQuery.post("<%=request.getContextPath()%>/groupAppointment.do" , {method: 'saveParticipantAttributes',appointmentNo:appointmentNo,providerNo:providerNo,date:d,bus_tickets:bus_tickets,vouchers:vouchers,guests:guests},
	            function(xml)
	            {
					if(xml.error) {
						alert("ERROR:" + xml.error);
					} else {
						alert('success');
					}
	            }, "json"
		);
	}
	
	function removeFromSeries(appointmentNo) {
		var reason = prompt("Please enter reason:", "");
	    if (reason == null) {
	      return false;
	    }
	    
		jQuery.post("<%=request.getContextPath()%>/groupAppointment.do" , {method: 'removeFromSeries',appointmentNo:appointmentNo,reason:reason},
	            function(xml)
	            {
					if(xml.error) {
						alert("ERROR:" + xml.error);
					}
					updateParticipants();
	            }, "json"
		);
	}
	
	function updateParticipants() {
		jQuery.post("<%=request.getContextPath()%>/groupAppointment.do" , {method: 'getParticipantsForSession',providerNo:'<%=targetProviderNo%>',date:'<%=sessionDateStr%>'},
	            function(xml)
	            {
	                     //  console.log(JSON.stringify(xml));
	                       jQuery("#participantList tbody").empty();
	                       for(var x=0;x<xml.appointments.length;x++) {
	                    	   var a = xml.appointments[x];
	                    	   
	                    	   var arrivedButton =	 "<input type='button' value='Arrived' onClick=\"setStatus("+a.appointmentNo+",'H')\"/>";
	                    	   var noShowButton =	 "<input type='button' value='No Show' onClick=\"setStatus("+a.appointmentNo+",'N')\"/>";
	                    	   
	                    	   if(a.status == 'C') {
	                    		   continue;
	                    	   }
	                    	   if(a.status == 'N') { //NOSHOW
	                    		  noShowButton = "<input type='button' value='No Show' style='background-color:orange;font-weight:bold'/>";
	                    	   } 
	                    	   if(a.status == 'H') { //HERE 
	                    		  arrivedButton =  "<input type='button' value='Arrived' style='background-color:orange;font-weight:bold'/>";
	                    	   }
	                    	   
	                    	   var bus_tickets = '0';
	                    	   var vouchers = '0';
	                    	   var guests = '';
	                    	   
	                    	   if(a.bus_tickets != null) {
	                    		   bus_tickets = a.bus_tickets;
	                    	   }
	                    	   if(a.vouchers != null) {
	                    		   vouchers = a.vouchers;
	                    	   }
	                    	   if(a.guests != null) {
	                    		   guests = a.guests;
	                    	   }
	                    	   
	                    	   var newData = "<tr><td><input type='checkbox' id='checked_"+a.appointmentNo+"' name='checked' value='"+a.appointmentNo+"'/></td>" + 
	                    	   "<td style='width:20%'>" +a.name + "</td>"+
	                    	   "<td>"+arrivedButton+"&nbsp;"+noShowButton+"</td>"+
	                    	   "<td><input type='button' value='Remove from series' onClick='removeFromSeries("+a.appointmentNo+")'/></td>"+
	                    	   "<td><a href='javascript:void()' onClick='adjustValue(-1,\"bus_ticket_"+a.appointmentNo+"\")'><img src='../images/back_enabled.png'/></a><span id='bus_ticket_"+a.appointmentNo+"'>"+bus_tickets+"</span><a href='javascript:void()' onClick='adjustValue(1,\"bus_ticket_"+a.appointmentNo+"\")'><img src='../images/forward_enabled.png'/></a></td>"+
	                    	   "<td><a href='javascript:void()' onClick='adjustValue(-1,\"voucher_"+a.appointmentNo+"\")'><img src='../images/back_enabled.png'/></a><span id='voucher_"+a.appointmentNo+"'>"+vouchers+"</span><a href='javascript:void()' onClick='adjustValue(1,\"voucher_"+a.appointmentNo+"\")'><img src='../images/forward_enabled.png'/></a></td>"+
	                    	   "<td><textarea cols='3' style='width:100%' id='guests_"+a.appointmentNo+"'>"+guests+"</textarea></td>"+
	                    	   "<td style='text-align:center'><input type='Button' value='Save' style='width:100%' onClick='saveValues("+a.appointmentNo+")'/></td>" + 
	                    	   "</tr>";
	                    	   
	                    	   jQuery("#participantList tbody").append(newData);
	                       }
	            }, "json"
	        );	
	}
	
	function updateDay() {
		var newDay = jQuery("#sessionDate").val();
		window.location.href='<%=request.getContextPath()%>/appointment/groupProperties.jsp?provider_no=<%=targetProviderNo%>&date=' + newDay;
	}
	
	jQuery(document).ready(function(){
		updateParticipants();
	});
	
	
	function reschedule() {
		var dt = jQuery("#pdate").val();
		
		jQuery.post("<%=request.getContextPath()%>/groupAppointment.do" , {method: 'rescheduleSession',providerNo:'<%=targetProviderNo%>',date:dt,currentSession:'<%=sessionDateStr%>'},
	            function(xml)
	            {
					if(xml.error) {
						alert("ERROR:" + xml.error);
					} else {
						window.location.href='<%=request.getContextPath()%>/appointment/groupProperties.jsp?provider_no=<%=targetProviderNo%>&date=' + dt;
					}
	            }, "json"
		);
	}
	
	function addNewSession() {
		var dt = jQuery("#pdate2").val();
		
		jQuery.post("<%=request.getContextPath()%>/groupAppointment.do" , {method: 'addNewSession',providerNo:'<%=targetProviderNo%>',date:dt,currentSession:'<%=sessionDateStr%>'},
	            function(xml)
	            {
					if(xml.error) {
						alert("ERROR:" + xml.error);
					} else {
						window.location.href='<%=request.getContextPath()%>/appointment/groupProperties.jsp?provider_no=<%=targetProviderNo%>&date=' + dt;
					}
	            }, "json"
		);
	}
	</script>
</head>


<body bgproperties="fixed"  topmargin="0"leftmargin="0" rightmargin="0" style="font-family:sans-serif">
	<FORM NAME = "myForm" METHOD="post" ACTION="groupPropertiesSave.jsp">
	<input type="hidden" id="topics_num" name="topics_num" value="0"/>
	<input type="hidden" id="provider_no" name="provider_no" value="<%=targetProviderNo%>"/>
	<input type="hidden" name="date" value="<%=sessionDateStr%>"/>
		<div style="background-color:<%=deepcolor%>;text-align:center;font-weight:bold;font-size:18pt">
			Manage Group Series
		</div>
		
		<div style="background-color:white;height:15px"></div>

		<div style="background-color:<%=deepcolor%>;text-align:left;font-weight:bold">
			Series-Level Attributes (<%=provider.getFormattedName() %>)
		</div>

		<table class="preferenceTable" style="width:100%;border-collapse:collapse;background-color:<%=weakcolor%>;">


			<tr>
				<td class="preferenceLabel" width="10%">
					Name:
				</td>
				<td class="preferenceValue">
					<input name="seriesName" type="text" style="width:80%" value="<%=(seriesNameUp != null)?seriesNameUp.getValue():""%>"/>
				</td>
			</tr>
			<tr>
				<td class="preferenceLabel" width="10%">
					Site/Facility:
				</td>
				<td class="preferenceValue">
					<input name="seriesSite" type="text" style="width:80%" value="<%=(seriesSiteUp != null)?seriesSiteUp.getValue():""%>" />
				</td>
			</tr>
			
			<tr>
				<td class="preferenceLabel" width="10%">
					Notes:
				</td>
				<td class="preferenceValue">
					<textarea name="seriesNote" rows="10" style="width:80%"><%=(seriesNoteUp != null)?seriesNoteUp.getValue():""%></textarea>
				</td>
			</tr>
			
			
			<tr>
				<td class="preferenceLabel" width="10%">
					Completed:
				</td>
				<td class="preferenceValue">
				<%
					String checked="";
					if(completedUp != null && "true".equals(completedUp.getValue())) {
						checked = " checked=\"checked\" ";
					}
				%>
					<input name="completed" type="checkbox" <%=checked %> />
				</td>
			</tr>
			
		
		</table>
		
		<div style="background-color:white;height:20px"></div>

		<p style="font-weight:bold">Select a Session Date: &nbsp;
		
		<select id="sessionDate" onChange="updateDay()">
			<%for(String d:dates) {%>
				<%
					String selected = "";
					if(d.equals(sessionDateStr)) {
						selected = " selected=\"selected\" ";
					}
				%>
				<option value="<%=d%>" <%=selected %> ><%=d %></option>
			<% } %>
		</select>
		
		</p>
		 
		<div style="background-color:<%=deepcolor%>;text-align:left;font-weight:bold">
			Session-Level Attributes (<%=sessionDateStr %>)
		</div>
		
		<table class="preferenceTable" style="width:100%;border-collapse:collapse;background-color:<%=weakcolor%>;">
			<tr>
				<td class="preferenceLabel" width="10%">
					Facilitator:
				</td>
				<td class="preferenceValue">
					<select name="facilitator">
						<option value="">Select Below</option>
						<%
							for(Provider p:providers) {
								
								String selected = "";
								if(facilitatorUp != null && p.getProviderNo().equals(facilitatorUp.getValue())) {
									selected=" selected=\"selected\" ";
								}
							
								%><option value="<%=p.getProviderNo()%>" <%=selected %>><%=p.getFormattedName() %></option><%
								
							}
						%>
					</select>
				</td>
			</tr>
			
			<tr>
				<td class="preferenceLabel" width="10%">
					Facilitator:
				</td>
				<td class="preferenceValue">
					<select name="facilitator2">
						<option value="">Select Below</option>
						<%
							for(Provider p:providers) {
								
								String selected = "";
								if(facilitator2Up != null && p.getProviderNo().equals(facilitator2Up.getValue())) {
									selected=" selected=\"selected\" ";
								}
							
								%><option value="<%=p.getProviderNo()%>" <%=selected %>><%=p.getFormattedName() %></option><%
								
							}
						%>
					</select>
				</td>
			</tr>
			
			
			<tr>
				
				<td class="preferenceLabel">
					Topics Discussed:
				</td>
				<td class="preferenceValue">
					<a href="#" onclick="addTopic();">[Add]</a>
				</td>
			</tr>
			
			<tr>
				<td></td>
				<td>
					<div id="topics_container"></div>
				</td>
			</tr>		
			
			<tr>
				<td class="preferenceLabel" width="10%">
					Site/Facility (Override):
				</td>
				<td class="preferenceValue">
					<input name="sessionSite" type="text" style="width:80%" value="<%=(sessionSiteUp != null)?sessionSiteUp.getValue():""%>" />
				</td>
			</tr>
			
			<tr>
				<td colspan="2" style="height:10px"></td>
			</tr>
			
		</table>
		<br/>
		<input type="submit" value="Save Changes"/>
		
		
		<div style="background-color:white;height:20px"></div>

		<div style="background-color:<%=deepcolor%>;text-align:left;font-weight:bold">
			Manage Participants
		</div>
		
		<table id="participantList" class="preferenceTable" style=width:100%;border:1border-collapse:collapse;background-color:<%=weakcolor%>;">
			<thead style="background-color:<%=deepcolor%>;text-align:left">
				<th width="1%"></th>
				<th>Name</th>
				<th>Attendance Status</th>
				<th>Group Status</th>
				<th>Bus Tickets</th>
				<th>Vouchers</th>
				<th>Guests</th>
				<th></th>
			</thead>
			<tbody></tbody>
		</table>
		<table class="preferenceTable" style="width:100%;border-collapse:collapse;background-color:<%=weakcolor%>;">
			<tr>
				<td colspan="2" style="height:15px"></td>
			</tr>
			<tr>
				<td colspan="2">Add new participant: <input type="hidden" name="demographic_no" id="demographic_no" value="" /><input type="text" name="demo" id="demo" size="35"/></td>
			</tr>
			
		</table>
		
		<div style="background-color:white;height:20px"></div>

		<div style="background-color:<%=deepcolor%>;text-align:left;font-weight:bold">
			Create group note (applies to all checked participants)
		</div>
		<div style="background-color:white;height:5px"></div>
		
		<textarea rows="20" style="width:95%;display:block;margin-left:auto;margin-right:auto" id="groupNote"></textarea>
		<input type="button" value="Apply Group Note to Selected Participants" onClick="applyGroupNote()"/>
		
		<br/><br/>
		
		
		<B>Reschedule Session: </B> <input type="text" name="pdate" size="10" id="pdate"/> <img src="<%=request.getContextPath()%>/images/cal.gif" id="pdate_cal">
		<input type="button" value="Reschedule" onClick="reschedule()"/>
		
		<br/><br/>
		
		<B>Add New Session: </B> <input type="text" name="pdate2" size="10" id="pdate2"/> <img src="<%=request.getContextPath()%>/images/cal.gif" id="pdate2_cal">
		<input type="button" value="Add New Session" onClick="addNewSession()"/>
		
		<br/><br/>
		
		
		<input name="close" type="button" value="Close Window" onClick="window.close()"/>
		
		<input name="generateAttendanceReport" type="button" value="Attendance Report"/>
		
	</FORM>


 <script type="text/javascript">
				Calendar.setup({ inputField : "pdate", ifFormat : "%Y-%m-%d", showsTime :false, button : "pdate_cal", singleClick : true, step : 1 });
				Calendar.setup({ inputField : "pdate2", ifFormat : "%Y-%m-%d", showsTime :false, button : "pdate2_cal", singleClick : true, step : 1 });
	   </script>	

</body>


</html:html>
