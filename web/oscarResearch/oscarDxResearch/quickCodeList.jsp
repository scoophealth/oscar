<%@page pageEncoding="UTF-8"%>
<%@ page
	import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*, oscar.oscarResearch.oscarDxResearch.bean.*"%>
<%
   String demoNO = request.getParameter("demographicNo");
   
   dxResearchBeanHandler dxResearchBeanHand = new dxResearchBeanHandler(demoNO);
   Vector patientDx = dxResearchBeanHand.getDxResearchBeanVector();   
   
   String quickList = oscar.OscarProperties.getInstance().getProperty("DX_QUICK_LIST_DEFAULT");
   
   if (quickList != null){%>
<ul
	<% dxQuickListItemsHandler dxList = new dxQuickListItemsHandler(quickList);
      Collection list = dxList.getDxQuickListItemsVectorNotInPatientsList(patientDx);
      Iterator iter = list.iterator();
      while (iter.hasNext()){
         dxCodeSearchBean code = (dxCodeSearchBean) iter.next();
         %>>
	<li><input type="checkbox" name="xml_research"
		value="<%=code.getType()%>,<%=code.getDxSearchCode()%>" /> <%=code.getDescription()%>
	</li<%
      }%>
      >
</ul>
<%}%>
