<%@ page
	import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*, oscar.oscarResearch.oscarDxResearch.bean.*"%>
<ul
	<%
   String demoNO = request.getParameter("demographicNo");
   String maxlen = request.getParameter("maxlen");
   int len = -1;
   
   try{
      len = Integer.parseInt(maxlen);         
   }catch(Exception e){}   
      
   dxResearchBeanHandler dxResearchBeanHand = new dxResearchBeanHandler(demoNO);
   Vector patientDx = dxResearchBeanHand.getDxResearchBeanVector();
   
   //dxQuickListItemsHandler dxList = new dxQuickListItemsHandler("List1");
   //Collection list = dxList.getDxQuickListItemsVectorNotInPatientsList(patientDx);
   //Iterator iter = list.iterator();
   for ( int i = 0; i < patientDx.size(); i++){
      dxResearchBean code = (dxResearchBean)patientDx.get(i);  // code.getEnd_date() code.getStart_date()
      String desc = code.getDescription();
      if (len != -1){
         desc = org.apache.commons.lang.StringUtils.abbreviate(desc,len) ;
      }
   %>>
	<li>- <%=desc%></li<%
   }%>
>
</ul>
