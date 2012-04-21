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
    		
    		String sKey     = getsKey();
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
   
   
   public String getsKey() throws Exception{	
		String sExponent="AQAB";
		String sModulus="lTpFGGIDT7eNpfTQqhjYS2IieM0vLHuoqglfTqa9aaQGsfCLgIepMdnpNEk9r9kuTyYNFLtuJJhJYtcfABIWXs+kwoUR+bhDxxOrWbBIzC4oC0ZS4VCItXD/v9dDOqcbW3b8ESkZAFsoFU9g+upImrZlPuoVoSN6ptU5PPMSrF0=";
		// String sD="";
		byte[] expBytes =decode(sExponent.getBytes());
		byte[] modBytes = decode(sModulus.getBytes());
		//  byte[] dBytes = decode(sD.getBytes());
		
		BigInteger modules = new BigInteger(1, modBytes);
		BigInteger exponent = new BigInteger(1, expBytes);
		//  BigInteger d = new BigInteger(1, dBytes); //needed only for generating the private key and decrypting with private key
		
		KeyFactory factory = KeyFactory.getInstance("RSA");
		
		Cipher cipher = Cipher.getInstance("RSA");
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
		Date date = new Date();
		
		String input = "DateTime=" + dateFormat.format(date);
		RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(modules, exponent);
		PublicKey pubKey = factory.generatePublic(pubSpec);
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		// byte[] encrypted = cipher.doFinal(input.getBytes("UTF-8"));
		byte[] encrypted = cipher.doFinal(input.getBytes());
		String sEnc;
		sEnc=encode(encrypted);
				//Comment: the sEnc has to be URL encoded before sending the user to MacPLUS FS
		
		return sEnc;
   }
   
   public static String encode(byte[] d)  {
	    if (d == null) return null;
	    byte data[] = new byte[d.length+2];
	    System.arraycopy(d, 0, data, 0, d.length);
	    byte dest[] = new byte[(data.length/3)*4];

	    // 3-byte to 4-byte conversion
	    for (int sidx = 0, didx=0; sidx < d.length; sidx += 3, didx += 4)
	    {
	      dest[didx]   = (byte) ((data[sidx] >>> 2) & 077);
	      dest[didx+1] = (byte) ((data[sidx+1] >>> 4) & 017 |
	                  (data[sidx] << 4) & 077);
	      dest[didx+2] = (byte) ((data[sidx+2] >>> 6) & 003 |
	                  (data[sidx+1] << 2) & 077);
	      dest[didx+3] = (byte) (data[sidx+2] & 077);
	    }

	    for (int idx = 0; idx <dest.length; idx++)
	    {
	      if (dest[idx] < 26)     dest[idx] = (byte)(dest[idx] + 'A');
	      else if (dest[idx] < 52)  dest[idx] = (byte)(dest[idx] + 'a' - 26);
	      else if (dest[idx] < 62)  dest[idx] = (byte)(dest[idx] + '0' - 52);
	      else if (dest[idx] < 63)  dest[idx] = (byte)'+';
	      else            dest[idx] = (byte)'/';
	    }

	    // add padding
	    for (int idx = dest.length-1; idx > (d.length*4)/3; idx--)
	    {
	      dest[idx] = (byte)'=';
	    }
	    return new String(dest);
	  }
	 
	 public static byte[] decode(byte[] data)  {
	    int tail = data.length;
	    while (data[tail-1] == '=')  tail--;
	    byte dest[] = new byte[tail - data.length/4];

	    for (int idx = 0; idx <data.length; idx++)
	    {
	      if (data[idx] == '=')    data[idx] = 0;
	      else if (data[idx] == '/') data[idx] = 63;
	      else if (data[idx] == '+') data[idx] = 62;
	      else if (data[idx] >= '0'  &&  data[idx] <= '9')
	        data[idx] = (byte)(data[idx] - ('0' - 52));
	      else if (data[idx] >= 'a'  &&  data[idx] <= 'z')
	        data[idx] = (byte)(data[idx] - ('a' - 26));
	      else if (data[idx] >= 'A'  &&  data[idx] <= 'Z')
	        data[idx] = (byte)(data[idx] - 'A');
	    }

	    // 4-byte to 3-byte conversion
	    int sidx, didx;
	    for (sidx = 0, didx=0; didx < dest.length-2; sidx += 4, didx += 3)
	    {
	      dest[didx]   = (byte) ( ((data[sidx] << 2) & 255) |
	              ((data[sidx+1] >>> 4) & 3) );
	      dest[didx+1] = (byte) ( ((data[sidx+1] << 4) & 255) |
	              ((data[sidx+2] >>> 2) & 017) );
	      dest[didx+2] = (byte) ( ((data[sidx+2] << 6) & 255) |
	              (data[sidx+3] & 077) );
	    }
	    if (didx < dest.length)
	    {
	      dest[didx]   = (byte) ( ((data[sidx] << 2) & 255) |
	              ((data[sidx+1] >>> 4) & 3) );
	    }
	    if (++didx < dest.length)
	    {
	      dest[didx]   = (byte) ( ((data[sidx+1] << 4) & 255) |
	              ((data[sidx+2] >>> 2) & 017) );
	    }
	    return dest;
	  }
		
   
   
%>
