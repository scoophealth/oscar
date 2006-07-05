<%
String questr=(String)session.getAttribute("infirmaryView_OscarQue"); 
//remove "infirmaryView_programId" from querystring
questr=oscar.caisi.CaisiUtil.removeAttr(questr,"infirmaryView_programId=");
String providerurlString="providercontrol.jsp?"+questr;
//remove "infirmaryView_isOscar" from querystring
session.setAttribute("infirmaryView_OscarQue",oscar.caisi.CaisiUtil.removeAttr(questr,"infirmaryView_isOscar="));
%>

<logic:notEqual name="infirmaryView_isOscar" value="true">
<script>
function submitProgram(ctrl) {
	document.location.href = "<%=providerurlString%>"+"&infirmaryView_programId="+ctrl.value;
}
</script>
  <b>Program:</b>
  <select name="bedprogram_no" onchange="submitProgram(this)">
  <%java.util.List programBean=(java.util.List)session.getAttribute("infirmaryView_programBeans");
  	String programId=(String)session.getAttribute("infirmaryView_programId");
  if (programBean.size()==0 || programId.equalsIgnoreCase("0")){%>
  <option value="0" selected>-No assigned program-</option>
  <%}else{ %> 
  <logic:iterate id="pb" name="infirmaryView_programBeans" type="org.apache.struts.util.LabelValueBean">
  <logic:equal name="infirmaryView_programId" value="<%=pb.getValue()%>">
  	<option value="<%=pb.getValue()%>" selected><%= pb.getLabel() %></option>
  </logic:equal>
  <logic:notEqual name="infirmaryView_programId" value="<%=pb.getValue()%>">
  	<option value="<%=pb.getValue()%>"><%= pb.getLabel() %></option>
  </logic:notEqual>
  </logic:iterate>
  <%} %>
  </select>
  <a href='providercontrol.jsp?infirmaryView_isOscar=true&<%=session.getAttribute("infirmaryView_OscarQue") %>'>| Oscar View</a>
</logic:notEqual>
  
<logic:equal name="infirmaryView_isOscar" value="true">
  <div align="right"><a href='providercontrol.jsp?infirmaryView_isOscar=false&<%=session.getAttribute("infirmaryView_OscarQue") %>'>| Case Management View</a></div>
</logic:equal>
</td></tr></table>