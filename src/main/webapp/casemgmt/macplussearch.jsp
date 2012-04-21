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
<%@page import="java.util.Date"%>
<%@page import="javax.crypto.Cipher"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.security.*"%>
<%@page import="java.security.spec.*"%>
<%@page import="java.math.*"%>
<%@page import="java.text.*"%>
<%@page import="ca.mcmaster.plus.oscarws.Service"%>
<%@page import="ca.mcmaster.plus.oscarws.ServiceSoap"%>
<%@page import="ca.mcmaster.plus.oscarws.SearchPlusResponse"%>
<%@page import="org.w3c.dom.Element"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="java.net.URL"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
    	<script type="text/javascript" src="<c:out value="${ctx}/share/javascript/prototype.js"/>"></script>
    	<title>MacPlus Search Results</title>
        <style type="text/css">
            table.sample {
                border-width: 0.2px;
                border-spacing: 0px;
                border-style: solid;
                border-color: gray;
                border-collapse: collapse;
                background-color: white;
            }
            table.sample th {
                border-width: 0px;
                padding: 1px;
                border-style: solid;
                border-color: gray;
                background-color: white;
                -moz-border-radius: 0px 0px 0px 0px;
            }
            table.sample td {
                border-bottom-width: 1px;
                padding: 5px;
                border-style: solid;
                border-color: gray;
                background-color: white;
                -moz-border-radius: 0px 0px 0px 0px;
                font-size:12px;

            }

            table.sample td a{
                text-decoration:none;
                font-family:sans-serif,helvetica;
                font-size:14px;
            }
            span.source{
            	font-style:italic;
            	color:grey;
            }
            span.readmore{
            	float:right;
            	font-family:sans-serif,helvetica;
                font-size:8px;
            }

        </style>
        <script type="text/javascript">
			function callReplacementWebService(url,id,skey,sourceId){
              var ran_number=Math.round(Math.random()*1000000);
              var params = "skey="+skey+"&sourceId="+sourceId+"&rand="+ran_number;  //hack to get around ie caching the page
              var updater=new Ajax.Updater(id,url, {method:'get',parameters:params,evalScripts:true});
         	}
		</script>
    </head>
    <body>



        <%
    		Service service = new Service(new URL("http://plus.mcmaster.ca/oscarws/service.asmx?WSDL"));
    		ServiceSoap serviceSoap = service.getServiceSoap();
    		String sKey = getsKey();
    		String SearchTerm =  request.getParameter("searchterm");
    		String Disciplines = "-1";
    		String Category = "-1";
    		String Population = "-1";
    		int ArticleType = -1;
    		String OrderBy = "D";
    		SearchPlusResponse.SearchPlusResult spr =  serviceSoap.searchPlus(sKey,SearchTerm,Disciplines,Category,Population,ArticleType,OrderBy);
    		 		
    		org.w3c.dom.Element ele = (Element) spr.getAny();
    		   	
    		Node node = XmlUtils.getChildNode(ele,"NewDataSet");
    		
    		if(node == null){
    			out.write("Mac Plus Did not find anything");
    			return;
    		}
    		
    		ArrayList<Node> nodes = XmlUtils.getChildNodes(node, "SearchResults");
    		%>
    		<table class="sample">
    		<% 
    		int counter=0;
    		for (Node searchRes: nodes){
    		counter++;
    		String articleId = XmlUtils.getChildNodeTextContents(searchRes,"ArticleId");
    		String source=XmlUtils.getChildNodeTextContents(searchRes,"Source");
    		String pubMedId = XmlUtils.getChildNodeTextContents(searchRes,"PubMedId");
    		
    		String idSource=articleId+"||"+source; 		
    		String sEnc= getArticle(idSource);
    		boolean link = false;
    		try{
    			int pubId = Integer.parseInt(pubMedId);
    			if (pubId > 0){
    				link = true;
    			}
    		}catch(Exception e){/*Only checking to see that its a positive Id*/} 
    		
    		%>
    			<tr>
                	<td>
                	    <%if(link){ %>
                		<a href="http://plus.mcmaster.ca/MacPLUSFS/Oscar.aspx?x=<%=URLEncoder.encode(sKey, "UTF-8") %>&articleid=<%=XmlUtils.getChildNodeTextContents(searchRes,"ArticleId")%>">
                		<%}%>
                    	<%=XmlUtils.getChildNodeTextContents(searchRes,"Title") %>
                    	<%if(link){ %></a><%}%>
                    	<br>
                    	<span class="source"><%=XmlUtils.getChildNodeTextContents(searchRes,"Source") %></span>
                    	<span class="readmore"><a href="javascript:void(0);" onclick="callReplacementWebService('macplusdetail.jsp','id<%=counter%>','<%=URLEncoder.encode(sKey, "UTF-8") %>','<%=URLEncoder.encode(sEnc, "UTF-8") %>');">More</a></span>
                    	<div id="id<%=counter%>"></div>
                	</td>

            	</tr>
    	<% 	} %>
    		</table>
        
      

    </body>
</html>

<%!
	
	public String getArticle(String input) throws Exception{	
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
		RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(modules, exponent);
		PublicKey pubKey = factory.generatePublic(pubSpec);
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		// byte[] encrypted = cipher.doFinal(input.getBytes("UTF-8"));
		byte[] encrypted = cipher.doFinal(input.getBytes());
		String sEnc;
		sEnc=encode(encrypted);
		
		
		return sEnc;
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
