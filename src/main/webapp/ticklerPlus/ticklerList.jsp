<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_tickler" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_tickler");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@ include file="/ticklerPlus/header.jsp"%>

<%@ page import="org.oscarehr.common.model.Tickler" %>

<%@ page import="java.util.Calendar"%>
<%
	Calendar now = Calendar.getInstance();
int curYear = now.get(Calendar.YEAR);
int curMonth = now.get(Calendar.MONTH) + 1;
%>

<script type="text/javascript" src="../share/javascript/prototype.js"></script>
<script type="text/javascript" src="../js/checkDate.js"></script>

<script>
	//filter for client - will work for other dropdowns as well
	function filter(term, _id, cellNr) {
		var suche = term.toLowerCase();
		
		//suche = trimAll(suche);
		
		var table = document.getElementById(_id);


		if (suche.length < 2) {
			for (var r = 1; r < table.rows.length; r++)
				table.rows[r].style.display = '';
			return;
		}

		var ele;
		for (var r = 1; r < table.rows.length - 1; r++){
			ele = table.rows[r].cells[cellNr].innerHTML.replace(/<[^>]+>/g,"");
			
			if (ele.toLowerCase().indexOf(suche)>=0 ) {
				//alert('found-'+ele);
				table.rows[r].style.display = '';
			}
			else { 
				//alert('not found-'+ele);
				table.rows[r].style.display = 'none';
			}
		}
		
	}

	function batch_operation(method) {
		var checked=false;

		var checkboxes=document.getElementsByName("checkbox");
		var x=0;
		for(x=0;x<checkboxes.length;x++) {
			if(checkboxes[x].checked==true) {
				checked=true;
			}
		}
		if(checked==false) {
			alert('You must choose at least 1 tickler');
			return false;
		}
		var form = document.ticklerForm;
		form.method.value=method;
		form.submit();
	}

	function checkTicklerDate() {
		//2007-10-05
		var startDate = document.ticklerForm.elements['filter.startDateWeb'].value;
		var endDate = document.ticklerForm.elements['filter.endDateWeb'].value;

		if(check_date('filter.startDateWeb') && check_date('filter.endDateWeb')) {

		  var sArray1=startDate.split("-");
		  var sArray2=endDate.split("-");
		  var bValid=true;

		  if(parseInt(sArray1[0])>parseInt(sArray2[0])) {
		    bValid=false;
		  }else if(parseInt(sArray1[0])==parseInt(sArray2[0])) {
		  	if(parseInt(sArray1[1])>parseInt(sArray2[1])) {
		    	bValid=false;
		  	}else if(parseInt(sArray1[1])==parseInt(sArray2[1])) {
		  		if(parseInt(sArray1[2])>parseInt(sArray2[2])) {
		    		bValid=false;
		 		}
		 	}
		 }

		  if(!bValid){
            alert("Begin Date can not be greater than End Date.");
            return false;
		  }else{
		    var form = document.ticklerForm;
		    form.method.value='filter';
		    form.submit();
		  }
		} else {
			return false;
		}
	}

        function createReport() {
                document.ticklerForm.method.value='filter';
                document.ticklerForm.submit();
        }

        function updTklrList() {
                clearInterval(check_demo_no);
                createReport();
        }

        function search_demographic() {
                var url = '<c:out value="${ctx}"/>/ticklerPlus/demographicSearch2.jsp?query=' + document.ticklerForm.elements['filter.demographic_webName'].value;
                var popup = window.open(url,'demographic_search');
                demo_no_orig = document.ticklerForm.elements['filter.demographicNo'].value;
                check_demo_no = setInterval("if (demo_no_orig != document.ticklerForm.elements['filter.demographicNo'].value) updTklrList()",100);

       			if (popup != null) {
    				if (popup.opener == null) {
      					popup.opener = self;
    				}
    				popup.focus();
  				}
        }

        function sortByDate()
        {
			document.ticklerForm.method.value='filter';
			document.ticklerForm.submit();
        }

        function clearClientFilter() {
            document.ticklerForm.elements['filter.demographicNo'].value = "";
            document.ticklerForm.elements['filter.demographic_webName'].value = "";
            showClearButton();
            createReport();
        }

        function showClearButton() {
            var cb = document.getElementById('clear_button');

            if (document.ticklerForm.elements['filter.demographic_webName'].value=="") {
                cb.style.display = 'none';
            } else {
                cb.style.display = '';
            }
        }

        function wrapUp() {
            try {
                if (window.opener && window.opener.callRefreshTabAlerts) {
                    window.opener.callRefreshTabAlerts("oscar_new_tickler");
                    setTimeout("window.close();",100);
                } else {
                    window.close();
                }
            } catch (err) {
                window.close();
            }
        }
</script>


<html:form action="/Tickler">
	<input type="hidden" name="method" value="" />
	<input type="hidden" name="order_tcr" value="asc"/>

	<tr>
		<td class="searchTitle" colspan="4">Filter Tickler List</td>
	</tr>
	<tr>
		<td class="blueText" width="30%">Status: <html:select property="filter.status"
			onchange="return checkTicklerDate();">
			<html:option value="Z">All</html:option>
			<html:option value="A">Active</html:option>
			<html:option value="C">Completed</html:option>
			<html:option value="D">Deleted</html:option>
		</html:select></td>
		<td class="blueText" width="30%"><span style="text-decoration:underline"
			onClick="openBrWindow('<c:out value="${ctx}"/>/ticklerPlus/calendar/oscarCalendarPopup.jsp?type=caisi&openerForm=ticklerForm&amp;openerElement=filter.startDateWeb&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')">Begin&nbsp;Date:</span>
			<html:text property="filter.startDateWeb" maxlength="10" /></td>

		<td class="blueText" width="30%"><span style="text-decoration:underline"
			onClick="openBrWindow('<c:out value="${ctx}"/>/ticklerPlus/calendar/oscarCalendarPopup.jsp?type=caisi&openerForm=ticklerForm&amp;openerElement=filter.endDateWeb&amp;year=<%=curYear%>&amp;month=<%=curMonth %>','','width=300,height=300')">End&nbsp;Date:</span>
			<html:text property="filter.endDateWeb" maxlength="10"/>
		</td>

		<td width="10%">&nbsp;</td>
	</tr>
	<tr>
		<td class="blueText">Program: <html:select
			property="filter.programId"
			onchange="return checkTicklerDate();">
			<option value="All Programs">All Programs</option>
			<html:options collection="programs" property="id" labelProperty="name" />
		</html:select></td>

		<td class="blueText">Provider: <html:select property="filter.provider"
			onchange="return checkTicklerDate();">
			<option value="All Providers">All Providers</option>
			<html:options collection="providers" property="providerNo"
				labelProperty="formattedName" />
		</html:select></td>

		<td class="blueText" colspan="2">Task Assigned To: <html:select
			property="filter.assignee"
			onchange="return checkTicklerDate();">
			<option value="All Providers">All Providers</option>
			<html:options collection="providers" property="providerNo"
				labelProperty="formattedName" />
		</html:select></td>

	</tr>
	<tr>
		<td class="blueText" colspan="2">Client:

		<oscar:oscarPropertiesCheck property="clientdropbox" value="on">
		    <html:select property="filter.demographicNo"
			onchange="return checkTicklerDate();">
			<option value="All Clients">All Clients</option>
			<html:options collection="demographics" property="demographicNo" labelProperty="formattedName" />
		    </html:select>
		</oscar:oscarPropertiesCheck>

		<oscar:oscarPropertiesCheck property="clientdropbox" value="off" defaultVal="true">
		    <html:hidden property="filter.demographicNo"/>
		    <html:text property="filter.demographic_webName" onkeyup="filter(this.value, 'ticklersTbl', 2)" size="15"/>
		    <span id="clear_button"><input type="button" value="Clear" onclick="clearClientFilter();" /></span>
		    <script language="JavaScript">showClearButton();</script>
		    <!-- <input type="button" value="Search" onclick="search_demographic();" /> -->
		</oscar:oscarPropertiesCheck>

		</td>

		<td colspan="2" class="blueText"><html:link action="CustomFilter.do">Custom Filters:</html:link>
			<html:select property="filter.name"
			onchange="this.form.method.value='run_custom_filter';this.form.submit();">
			<option value=""></option>
			<html:options collection="customFilters" property="name" />
		</html:select></td>
	</tr>

	<tr>
		<td><input type="button" value="Create Report"
			onclick="return checkTicklerDate();" /></td>
		<td colspan="3"><a id="pre_print" href='<c:out value="${ctx}"/>/ticklerPlus/ticklerPrint.jsp' target="_pre_print">Print Preview</a> </td>
	</tr>
</table>

<%
	//colour codes - html
	 String[] ColourCodesArray=new String[3];
	 ColourCodesArray[1]="red"; //red - High
	 ColourCodesArray[2]="black"; //normal or low

	 //labels for colour codes
	 String[] lblCodesArray=new String[3];
	 lblCodesArray[1]="High"; //red - High
	 lblCodesArray[2]="Normal or Low"; //normal or low

	 //Title ie: Legend or Profile Legend
	 String legend_title="Priority Legend: ";

	 //creat empty builder string
	 String legend_builder=" ";


	 	for (int iLegend = 1; iLegend < 3; iLegend++){

	legend_builder +="<td> <table class='colour_codes' bgcolor='"+ColourCodesArray[iLegend]+"'><td> </td></table> </td> <td align='center'>"+lblCodesArray[iLegend]+"</td>";

		}

	 	String legend = "<table class='legend' cellspacing='0' align='right'><tr><td><b>"+legend_title+"</b></td>"+legend_builder+" </tr></table>";

		out.print(legend);
%>
<br />
<%@ include file="/ticklerPlus/messages.jsp"%>

<table width="100%" border="0" cellpadding="0" cellspacing="1"
	bgcolor="#C0C0C0" id="ticklersTbl" >
		<tr class="title">
			<th></th>
			<th></th>
			<th>Demographic Name</th>
			<th class=noprint>Provider Name</th>

			<%
				String click_order = (String)session.getAttribute( "filter_order" );

				if(click_order=="DESC") {
			%>
				<input type="hidden" name="filter.sort_order" value="ASC" />
				<%
					session.setAttribute( "filter_order", "ASC" );
					} else {
				%>
				<input type="hidden" name="filter.sort_order" value="DESC" />
				<%
					session.setAttribute( "filter_order", "DESC");
					}
				%>

			<th class=noprint><a href="javascript:sortByDate();" class=noprint>Date</a></th>
			<th class=noprint>Priority</th>
			<th class=noprint>Task Assigned To</th>
			<th class=noprint>Status</th>
			<th>Message</th>
			<th>Program</th>
		</tr>

			<%
				int index = 0;
				String bgcolor;
				String view_image;
			%>
			<c:forEach var="tickler" items="${ticklers}">
			<%
				if (index++ % 2 != 0) {
					bgcolor = "white";
					view_image = "details.gif";
				} else {
					bgcolor = "#EEEEFF";
					view_image = "details2.gif";
				}
			%>
				<tr bgcolor="<%=bgcolor%>" align="center">
			<%
				String demographic_name = "";
				String provider_name = "";
				String assignee_name = "";
				String program_name = "";
				String status = "Active";
				String late_status = "b";
				Tickler temp = (Tickler) pageContext.getAttribute("tickler");
				if (temp != null) {
					org.oscarehr.common.model.Demographic demographic = temp.getDemographic();
					if (demographic != null) {
						demographic_name = demographic.getLastName() + ","
								+ demographic.getFirstName();
					}
					org.oscarehr.common.model.Provider provider = temp.getProvider();
					if (provider != null) {
						provider_name = provider.getLastName() + ","
								+ provider.getFirstName();
					}
					org.oscarehr.common.model.Provider assignee = temp.getAssignee();
					if (assignee != null) {
						assignee_name = assignee.getLastName() + ","
								+ assignee.getFirstName();
					}

					Program program = temp.getProgram();
					if (program != null) {
						program_name = program.getName();
					}
					
					status = "Active";
					if(temp.getStatus().equals(Tickler.STATUS.C)) {
						status = "Completed";
					}
					if(temp.getStatus().equals(Tickler.STATUS.D)) {
						status = "Deleted";
					}

					
					// add by PINE_SOFT
					// get system date
					Date sysdate = new java.util.Date();
					Date service_date = temp.getServiceDate();

					if (!sysdate.before(service_date)) {
						late_status = "a";
					}
				}
			%>
					<td ><input type="checkbox" name="checkbox"
						value="<c:out value="${tickler.id}"/>" /></td>
					<td><a href="../Tickler.do?method=view&id=<c:out value="${tickler.id}"/>"><img
						align="right" src="<c:out value="${ctx}"/>/ticklerPlus/images/<%=view_image %>" border="0" />
					</a></td>

			<%
			String style = "";
			style = "color:black;";

			if ("High".equals(temp.getPriority().toString())) {
				style = "color:red;";
			}
			%>
					<td style="<%=style%>"><%=demographic_name%></td>
					<td style="<%=style%>" class=noprint><%=provider_name%></td>
					<td style="<%=style%>" class=noprint><fmt:formatDate pattern="MM/dd/yy : hh:mm a"
						value="${tickler.serviceDate}" /></td>
					<td style="<%=style%>" class=noprint><c:out value="${tickler.priority}" /></td>
					<td style="<%=style%>" class=noprint><%=assignee_name%></td>
					<td style="<%=style%>" class=noprint><%=status%></td>
					<td style="<%=style%>" align="left"><c:out escapeXml="false"
						value="${tickler.message}" /></td>
					<td style="<%=style%>" class=noprint><%=program_name%></td>
				</tr>
			</c:forEach>
		<tr>
			<td colspan="9"><%=((java.util.List) session.getAttribute("ticklers")).size()%> ticklers found.</td>
		</tr>
</table>

<table>
		<!--
		<tr>
			<td colspan="2"><a href="#" onclick="CheckAll(document.ticklerForm);return false;">Check All</a>&nbsp;<a href="#" onclick="ClearAll(document.ticklerForm);return false;">Clear All</a></td>
		</tr>
	-->
		<tr>
			<!--<td><input type="button" value="Create New Tickler" onclick="location.href='<html:rewrite action="/Tickler"/>?method=edit'"/></td>-->
			<td class=noprint><input type="button" value="Complete"
				onclick="batch_operation('complete');" /></td>
			<td class=noprint><input type="button" value="Delete"
				onclick="batch_operation('delete');" /></td>

		</tr>
</table>

</html:form>

<%if ((request.getParameter("from") == null) || (!request.getParameter("from").equals("CaseMgmt"))) { %>
<table width="100%">
	<tr>
		<td><a href="javascript:void(0);" onclick="wrapUp();">Close Window</a></td>
	</tr>
</table>
<%}%>

</body>
</html>
