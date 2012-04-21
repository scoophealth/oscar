
<%@ page
	import="oscar.util.*, oscar.form.*, oscar.form.data.*,java.util.*,oscar.oscarPrevention.*"%>

<% 
                String id = request.getParameter("id");
                String date = request.getParameter("date");
                
                if (id != null) { 
                ArrayList<Map<String,Object>> alist = PreventionData.getPreventionDataFromExt("workflowId", id);       
                
                for (int k = 0; k < alist.size(); k++){
               	Map<String,Object> hdata = alist.get(k);
                Map<String,String> hextended = PreventionData.getPreventionKeyValues(""+hdata.get("id"));
                String refused = (String) hdata.get("refused");
                %>
<fieldset><legend> Injection # <%=k+1%> &nbsp;
&nbsp; &nbsp; Date: <%=hdata.get("preventionDate")%> &nbsp; &nbsp;
&nbsp; Weeks: <%=UtilDateUtilities.calculateGestationAge( (Date) hdata.get("prevention_date_asDate") , UtilDateUtilities.StringToDate (date))%>
</legend> <%if ( refused.equals("1")){ %> Refused <a
	onclick="deleteInjection('<%=hdata.get("id")%>')"
	href="javascript: function myFunction() {return false; }"
	style="color: blue;"> Delete </a> <%}else{%> Given By: <%=PreventionData.getProviderName(hdata)%>
Location: <%=hextended.get("location")  %> Lot #: <%=hextended.get("lot")  %>
Product #: <%=hextended.get("product")  %> Dosage: <%=hextended.get("dosage")  %>
<a onclick="deleteInjection('<%=hdata.get("id")%>')"
	href="javascript: function myFunction() {return false; }"
	style="color: blue;"> Delete </a> </br>
Reason: <%=hextended.get("reason")  %> <%}%>
</fieldset>
<%  }
                 }%>
