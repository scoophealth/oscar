
<%-- Updated by Eugene Petruhin on 11 dec 2008 while fixing #2356548 & #2393547 --%>
<%-- Updated by Eugene Petruhin on 19 dec 2008 while fixing #2422864 & #2317933 & #2379840 --%>
<%-- Updated by Eugene Petruhin on 22 dec 2008 while fixing #2455143 --%>

<%@ include file="/taglibs.jsp" %>
<%@ include file="/ticklerPlus/header.jsp"%>

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
		//alert(suche.length + suche  + _id + cellNr);

		var table = document.getElementById(_id);

		if (suche.length < 2) {
			for (var r = 1; r < table.rows.length; r++)
				table.rows[r].style.display = '';
			return;
		}

		var ele;
		for (var r = 1; r < table.rows.length - 1; r++){
			//alert(table.rows.length +  table.rows[0].cells[2].innerHTML);
			ele = table.rows[r].cells[cellNr].innerHTML.replace(/<[^>]+>/g,"");
			
			if (ele.toLowerCase().indexOf(suche)>=0 )
				table.rows[r].style.display = '';
			else table.rows[r].style.display = 'none';
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
		var startDate = document.ticklerForm.elements['filter.startDate'].value;
		var endDate = document.ticklerForm.elements['filter.endDate'].value;
		
		if(check_date(startDate) && check_date(endDate)) {
		  
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
                var url = '<c:out value="${ctx}"/>/ticklerPlus/demographicSearch2.jsp?query=';// + document.ticklerForm.elements['filter.demographic_webName'].value;
                var popup = window.open(url,'demographic_search');
                demo_no_orig = document.ticklerForm.elements['filter.demographic_no'].value;
                check_demo_no = setInterval("if (demo_no_orig != document.ticklerForm.elements['filter.demographic_no'].value) updTklrList()",100);
       		
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
            document.ticklerForm.elements['filter.demographic_no'].value = "";
            document.ticklerForm.elements['filter.demographic_webName'].value = "";
            showClearButton();
            createReport();
        }
        
        function showClearButton() {
            var cb = $('clear_button');
            
            if (document.ticklerForm.elements['filter.demographic_webName'].value=="") {
                cb.hide();
            } else {
                cb.show();
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
	<input type="hidden" name="method" value="save" />
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
			onClick="openBrWindow('<c:out value="${ctx}"/>/ticklerPlus/calendar/oscarCalendarPopup.jsp?type=caisi&openerForm=ticklerForm&amp;openerElement=filter.startDate&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')">Begin&nbsp;Date:</span>
			<html:text property="filter.startDate" maxlength="10" /></td>
		
		<td class="blueText" width="30%"><span style="text-decoration:underline"
			onClick="openBrWindow('<c:out value="${ctx}"/>/ticklerPlus/calendar/oscarCalendarPopup.jsp?type=caisi&openerForm=ticklerForm&amp;openerElement=filter.endDate&amp;year=<%=curYear%>&amp;month=<%=curMonth %>','','width=300,height=300')">End&nbsp;Date:</span>
			<html:text property="filter.endDate" maxlength="10"/>
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
		    <html:select property="filter.demographic_no"
			onchange="return checkTicklerDate();">
			<option value="All Clients">All Clients</option>
			<html:options collection="demographics" property="demographicNo" labelProperty="formattedName" />
		    </html:select>
		</oscar:oscarPropertiesCheck>
		
		<oscar:oscarPropertiesCheck property="clientdropbox" value="off" defaultVal="true">
		    <html:hidden property="filter.demographic_no"/>
		    <html:text property="filter.demographic_webName" onkeyup="filter(this.value, 'ticklersTbl', 2)" size="15"/>
		    <span id="clear_button"><input type="button" value="Clear" onclick="clearClientFilter();" /></span>
		    <script language="JavaScript">showClearButton();</script>
		    <input type="button" value="Search" onclick="search_demographic();" />
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


	<br />
	<%@ include file="/ticklerPlus/messages.jsp"%>
	<br />
	<table width="100%" border="0" cellpadding="0" cellspacing="1"
		bgcolor="#C0C0C0" id="ticklersTbl" >
		<tr class="title">
			<th></th>
			<th></th>
			<th>Demographic Name</th>
			<th class=noprint>Provider Name</th>
		 
			<%
			String click_order = (String)session.getAttribute( "filter_order" );
	
			if(click_order=="DESC") {%>
				<input type="hidden" name="filter.sort_order" value="ASC" />
				<% 
				session.setAttribute( "filter_order", "ASC" );
			} else {%>
				<input type="hidden" name="filter.sort_order" value="DESC" />
				<% session.setAttribute( "filter_order", "DESC");
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
				<tr bgcolor="<%=bgcolor %>" align="center">
			<%
			String demographic_name = "";
			String provider_name = "";
			String assignee_name = "";
			String program_name = "";
			String status = "Active";
			String late_status = "b";			
			Tickler temp = (Tickler) pageContext.getAttribute("tickler");
			if (temp != null) {
				org.oscarehr.common.model.Demographic demographic = (org.oscarehr.common.model.Demographic) temp.getDemographic();
				if (demographic != null) {
					demographic_name = demographic.getLastName() + ","
							+ demographic.getFirstName();
				}
				org.oscarehr.common.model.Provider provider = (org.oscarehr.common.model.Provider) temp.getProvider();
				if (provider != null) {
					provider_name = provider.getLastName() + ","
							+ provider.getFirstName();
				}
				org.oscarehr.common.model.Provider assignee = (org.oscarehr.common.model.Provider) temp.getAssignee();
				if (assignee != null) {
					assignee_name = assignee.getLastName() + ","
							+ assignee.getFirstName();
				}
				
				Program program = (Program) temp.getProgram();
				if (program != null) {
					program_name = program.getName();
				}
				
				switch (temp.getStatus()) {
				case 'A':
					status = "Active";
					break;
				case 'D':
					status = "Deleted";
					break;
				case 'C':
					status = "Completed";
					break;
				}
				// add by PINE_SOFT
				// get system date
				Date sysdate = new java.util.Date();
				Date service_date = (Date) temp.getService_date();

				if (!sysdate.before(service_date)) {
					late_status = "a";
				}
			}

			%>
					<td ><input type="checkbox" name="checkbox"
						value="<c:out value="${tickler.tickler_no}"/>" /></td>
					<td><a href="../Tickler.do?method=view&id=<c:out value="${tickler.tickler_no}"/>"><img
						align="right" src="<c:out value="${ctx}"/>/ticklerPlus/images/<%=view_image %>" border="0" />
					</a></td>

			<%
			String style = "";
			style = "color:black;";
			if ("High".equals(temp.getPriority())) {
				style = "color:red;";
			}
			%>
					<td style="<%=style%>"><%=demographic_name%></td>
					<td style="<%=style%>" class=noprint><%=provider_name%></td>
					<td style="<%=style%>" class=noprint><fmt:formatDate pattern="MM/dd/yy : hh:mm a"
						value="${tickler.service_date}" /></td>
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
		<!-- <td><a href="../provider/providercontrol.jsp">Back to Schedule Page</a></td> -->
		<td><a href="javascript:void(0);" onclick="wrapUp();">Close Window</a></td>
	</tr>
</table>
<%}%>

</body>
</html>

