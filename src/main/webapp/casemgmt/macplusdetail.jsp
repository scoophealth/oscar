<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.util.XmlUtils" %>
<%@page import="java.util.Map,java.util.HashMap" %>    
<%@page import="java.util.Date"%>
<%@page import="javax.crypto.Cipher"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.security.*"%>
<%@page import="java.security.spec.*"%>
<%@page import="java.math.*"%>
<%@page import="java.text.*"%>
<%@page import="ca.mcmaster.plus.oscarws.Service"%>
<%@page import="ca.mcmaster.plus.oscarws.ServiceSoap"%>
<%@page import="ca.mcmaster.plus.oscarws.GetFullArticleRecordResponse"%>
<%@page import="org.w3c.dom.Element"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="java.net.URL"%>

        <%      
    		Service service = new Service(new URL("http://plus.mcmaster.ca/oscarws/service.asmx?WSDL"));
    		ServiceSoap serviceSoap = service.getServiceSoap();
    		
    		String sKey     = request.getParameter("skey");
    		String sourceId = request.getParameter("sourceId");
    		sKey = URLEncoder.encode(sKey, "UTF-8");
    		sourceId = URLEncoder.encode(sourceId, "UTF-8");
    		
    		GetFullArticleRecordResponse.GetFullArticleRecordResult gfar = serviceSoap.getFullArticleRecord(sKey,sourceId);
    				
    		org.w3c.dom.Element ele = (Element) gfar.getAny();
    		Node node = XmlUtils.getChildNode(ele,"NewDataSet");
    		Node test = XmlUtils.getChildNode(node,"ArticleTbl");

    		ArrayList<Node> articleNodes = XmlUtils.getChildNodes(node, "ArticleTbl");
    		ArrayList<Node> discipNodes = XmlUtils.getChildNodes(node, "DisciplineAndRatingsTbl");
    		ArrayList<Node> commentNodes = XmlUtils.getChildNodes(node, "ArticleCommentsTbl");

    		for (Node article:articleNodes){ 
    		%>
    			<div>Abstract: <%=XmlUtils.getChildNodeTextContents(test,"Abstract")%></div>
    		<%}%>
    		<%--=XmlUtils.toString(ele,true) --%>
    	
    	    <table>
    	    	<tr>
	    	    	<th>Rated by doctors in</th>
	    	    	<th>Relevance</th>
	    	    	<th>Newsworthiness</th>
	    	    </tr>	
    	    	<% for (Node rating:discipNodes){ %>
    	    	<tr>
    				<td><%=getDis(XmlUtils.getChildNodeTextContents(rating,"DisciplineId"))%></td>
					<td><%=writeStars(XmlUtils.getChildNodeTextContents(rating,"RelevanceAvg"))%>    </td>
					<td><%=writeStars(XmlUtils.getChildNodeTextContents(rating,"Newsworthiness"))%>  </td>
				</tr>			  			
    			<%}%>
    	    </table>
    	    
    	    <%for (Node comments:commentNodes){ %>
    			<div><b>Comment from Doctor in: <%=getDis(XmlUtils.getChildNodeTextContents(comments,"DisciplineId"))%></b>
    			<br>
    			<%=XmlUtils.getChildNodeTextContents(comments,"Comments")%>
    			</div>
    			</br>
    		<%}%>
    	    
    	
    		
<%!
  public  String writeStars(String str){
	 StringBuilder sb = new StringBuilder();
	 int num = Integer.parseInt(str);
	 
	 for(int i =0;i < 7;i++){
		 if(i<num){
		 	sb.append("<span style=\"color:black\">&#9733;</span>");
		 }else{
			sb.append("<span style=\"color:lightgrey\">&#9733;</span>");
		 }
	 }
	 return sb.toString();
 }
 

   public String getDis(String str){
	 Map<String,String> h= new HashMap<String,String>(); 
	 h.put("3","Emergency Medicine");
	 h.put("1","General Practice(GP)/Family Practice(FP)");
	 h.put("2","GP/FP/Anesthesia");
	 h.put("4","GP/FP/Obstetrics");
	 h.put("5","GP/FP/Mental Health");
	 h.put("263","Occupational and Environmental Health");
	 h.put("264","Public Health");
	 h.put("6","General Internal Medicine-Primary Care(US)");
	 h.put("7","Hospital Doctor/Hospitalists");
	 h.put("8","Internal Medicine");
	 h.put("9","Allergy and Immunology");
	 h.put("10","Cardiology");
	 h.put("261","Dermatology");
	 h.put("11","Endocrine");
	 h.put("12","Gastroenterology");
	 h.put("13","Genetics");
	 h.put("14","Geriatrics");
	 h.put("15","Hematology/Thrombosis");
	 h.put("17","Infectious Disease");
	 h.put("18","Tropical and Travel Medicine");
	 h.put("19","Intensivist/Critical Care");
	 h.put("20","Nephrology");
	 h.put("21","Neurology");
	 h.put("290","Oncology - Genitourinary");
	 h.put("289","Oncology - Pediatric");
	 h.put("288","Oncology - Palliative and Supportive Care");
	 h.put("287","Oncology - Lung");
	 h.put("286","Oncology - Hematology");
	 h.put("285","Oncology - Gynecology");
	 h.put("284","Oncology - Gastrointestinal");
	 h.put("283","Oncology - Breast");
	 h.put("22","Oncology - General");
	 h.put("23","Physical Medicine and Rehabilitation");
	 h.put("24","Respirology/Pulmonology");
	 h.put("25","Rheumatology");
	 h.put("251","Gynecology");
	 h.put("255","Obstetrics");
	 h.put("253","Pediatrics (General)");
	 h.put("277","Pediatric Hospital Medicine");
	 h.put("279","Pediatric Neonatology");
	 h.put("280","Pediatric Emergency Medicine");
	 h.put("256","Psychiatry");
	 h.put("292","Anesthesiology");
	 h.put("254","Surgery - General");
	 h.put("275","Surgery - Cardiac");
	 h.put("265","Surgery - Colorectal");
	 h.put("276","Surgery - Ear Nose Throat");
	 h.put("266","Surgery - Gastrointestinal");
	 h.put("267","Surgery - Head and Neck");
	 h.put("268","Surgery - Neurosurgery");
	 h.put("269","Surgery - Oncology");
	 h.put("270","Surgery - Ophthalmology");
	 h.put("262","Surgery - Orthopaedics");
	 h.put("271","Surgery - Plastic");
	 h.put("272","Surgery - Thoracic");
	 h.put("274","Surgery - Urology");
	 h.put("273","Surgery - Vascular");
	 h.put("296","Special Interest - Pain -- Physician");
	 return h.get(str);
	   
	 
   }
%>