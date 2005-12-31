<%@page pageEncoding="UTF-8"%>
<%@ page import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*, oscar.oscarResearch.oscarDxResearch.bean.*"  %>
<% response.setHeader("Cache-Control","no-cache");%>
<ul<%
   String demoNO = request.getParameter("demographicNo");
   
   dxResearchBeanHandler dxResearchBeanHand = new dxResearchBeanHandler(demoNO);
   Vector patientDx = dxResearchBeanHand.getDxResearchBeanVector();
   
   //dxQuickListItemsHandler dxList = new dxQuickListItemsHandler("List1");
   //Collection list = dxList.getDxQuickListItemsVectorNotInPatientsList(patientDx);
   //Iterator iter = list.iterator();
   for ( int i = 0; i < patientDx.size(); i++){
      dxResearchBean code = (dxResearchBean)patientDx.get(i);  // code.getEnd_date() code.getStart_date()
   %>><li>- <%=code.getDescription()%>  </li<%
   }%>
></ul>     