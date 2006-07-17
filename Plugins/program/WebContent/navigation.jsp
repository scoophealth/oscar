<%@ include file="/taglibs.jsp" %>
<script>
function createIntakeAReport1()
{
        var dateObj = new Date();
        var yearStr = dateObj.getYear() + 1900;
        var mthStr = (dateObj.getMonth() + 1) + "";
        var dayStr = "" + dateObj.getDate();

        if(mthStr.length == 1)
        {
                mthStr = "0" + mthStr;
        }

        if(dayStr.length == 1)
        {
                dayStr = "0" + dayStr;
        }

        var dateStr = yearStr + "-" + mthStr + "-" + dayStr;
        var startDate = prompt("Please enter the start date in this format (e.g. 2005-01-10)", "0001-01-01");
        var endDate = prompt("Please enter the end date in this format (e.g. 2005-11-10)", dateStr);

        while(startDate.length != 10  ||  startDate.substring(4,5) != "-"  ||  startDate.substring(7,8) != "-")
        {
                startDate = prompt("Please enter the start date in this format (e.g. 2005-01-10)", "0001-01-01");
        }

        while(endDate.length != 10  ||  endDate.substring(4,5) != "-"  ||  endDate.substring(7,8) != "-")
        {
                endDate = prompt("Please enter the end date in this format (e.g. 2005-11-10)", dateStr);
        }

		alert('creating report from ' + startDate + ' to ' + endDate);
		
		location.href='<html:rewrite action="/PMmodule/IntakeAReport1Action"/>?startDate=' + startDate + '&endDate=' + endDate;
}
</script>
<div id="navcolumn">

<table border="0" cellspacing="0" cellpadding="4" >
	<tr>
	  <td align="left">
		<table border="0" cellpadding="0" cellspacing="2">
			<tr>
			  <td nowrap width=120>
				<div align=center>
				  <img src="<html:rewrite page="/images/caisi_1.jpg" />"
					   alt="Caisi" id="caisilogo" border="0">
				</div>
			  </td>	
			</tr>
			<tr>
				<td nowrap width=120>
					<div align="center"><bean:message key="version"/></div>
				</td>
			</tr>
		</table>
	  </td>
	</tr>
</table>
</div>

      <div id="projecttools" class="toolgroup">
       <div class="label">
        <strong>Navigator</strong>
       </div>
       
       <div class="body">

		<div>
           <span><html:link action="/PMmodule/ProviderInfo.do">Home</html:link></span>
       	 </div>	
       	 
		<div>
           <span>Client Management</span>
		   <div>
		      <html:link action="/PMmodule/ClientSearch2.do">Client Search</html:link>
		      <br/>
			  <html:link action="/PMmodule/Intake.do">New Client</html:link>
		   </div>
       	 </div>	
       	 
		<div>
           <span>Reporting Tools</span>
		   <div>
		      <a href="javascript:void(0)" onclick="javascript:createIntakeAReport1();return false;">IntakeA</a>
		   </div>
       	 </div>	
       	 
		<%
			if(session.getAttribute("userrole") != null && 
					((String)session.getAttribute("userrole")).indexOf("admin") != -1) {
		%>       	 
        <div>
           <span>Agency Management</span>
		   <div>
		      <html:link action="/PMmodule/AgencyManager.do">Summary</html:link>
		   </div>
		   <div>
		      <html:link action="/PMmodule/AgencyManager.do?method=edit">Update Info</html:link>
		   </div>
       	 </div>	
       	 
       	 <div>
           <span>Staff Management</span>
		   <div>
		      <html:link action="/PMmodule/StaffManager.do">Staff List</html:link>
		   </div>
       	 </div>	

         <div>
           <span>Program Management</span>
		   <div>
		      <html:link action="/PMmodule/ProgramManager.do">Program List</html:link>
		   </div>
		   <div>
		      <html:link action="/PMmodule/ProgramManager.do?method=edit">Add Program</html:link>
		   </div>
       	 </div>	
		<div>
        	<span><caisi:CaisiRoleLink/></span>
    	</div>	
       	 <% } %>
       	 <div>
           <span><caisi:OscarTag/></span>
       	 </div>	
       	 
  		</div>
  		
  		