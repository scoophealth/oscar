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
<%@page import="oscar.*,java.util.*"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.httpclient.HttpClient"%>
<%@page import="org.apache.commons.httpclient.methods.PostMethod"%>
<%@page import="org.apache.commons.httpclient.methods.RequestEntity"%>
<%@page import="org.apache.commons.httpclient.methods.StringRequestEntity"%>
<%@page import="org.jdom.Document"%>
<%@page import="org.jdom.Element"%>
<%@page import="org.jdom.filter.ElementFilter"%>
<%@page import="org.jdom.input.SAXBuilder"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.apache.log4j.Logger"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <style type="text/css">
            table.sample {
                border-width: 1px;
                border-spacing: 1px;
                border-style: solid;
                border-color: gray;
                border-collapse: collapse;
                background-color: white;
            }
            table.sample th {
                border-width: 1px;
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
                font-family:sans-serif;
                font-size:14px;
            }

        </style>
    </head>
    <body>



        <%
        String searchString = request.getParameter("searchterm");
        List<Hashtable> list = searchTripDatabase(searchString);
        if (list != null && list.size() > 0){%>
        <table class="sample">
            <%for (Hashtable h : list) {%>

            <tr>
                <td>
                    <a href="<%=h.get("tripurl")%>" ><%=h.get("title")%></a><br/>
                     <!-- publications name would go here --> <%=h.get("year")%>
                </td>

            </tr>

            <%}%>
        </table>

        <%} else {%>
        No Results
        <%}%>
    </body>
</html>

<%!

    public List searchTripDatabase(String searchString) throws Exception{
       Logger logger = MiscUtils.getLogger();
       List h  = null;
       PostMethod post = new PostMethod("http://tws.tripdatabase.com/en/trip.asmx");
       post.setRequestHeader("SOAPAction", "http://www.tripdatabase.com/webservices/GetSummaries");
      //post.setRequestHeader("SOAPAction", "http://www.tripdatabase.com/webservices/GetPublications");

       post.setRequestHeader("Content-Type", "text/xml; charset=utf-8");
       //String searchString = "cancer";
       String apiKey = "MCM001";                                                                                                                                                                                                                                                                                                                                                                                                                                     //68               80   &gt;    =&gt;                                              &lt;             &lt;               &lt;                &lt;                &lt;               &lt;              &lt;
       String soapMsg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><GetSummaries xmlns=\"http://www.tripdatabase.com/webservices/\"><sXMLIndividualSearch>&lt;search  showsources=\"0\" type=\"trip\" page=\"1\" pagesize=\"25\" &gt; &lt;criteria&gt;"+searchString+" &lt;/criteria&gt; &lt;categoryid>&lt;/categoryid&gt;&lt;publicationid&gt;&lt;/publicationid&gt;&lt;searchtype&gt;1&lt;/searchtype&gt;   &lt;/search &gt; </sXMLIndividualSearch><apiKey>MCM001</apiKey></GetSummaries></soap:Body></soap:Envelope>";
       
       RequestEntity re = new StringRequestEntity(soapMsg, "text/xml", "UTF-8");

       post.setRequestEntity(re);

       HttpClient httpclient = new HttpClient();
        // Execute request
       try{
                int result = httpclient.executeMethod(post);
                if (result != 200){
                    logger.debug("result "+result);
                }
                h =parseReturn(post.getResponseBodyAsStream());
       }catch(Exception e ){
            logger.debug("searchTripDB" ,e);
       } finally{
                // Release current connection to the connection pool
                post.releaseConnection();
       }
       return h;
    }

     private List<Hashtable> parseReturn(InputStream is){
        Logger logger = MiscUtils.getLogger();
        ArrayList list = new ArrayList();
        try {

            SAXBuilder parser = new SAXBuilder();
            Document doc = parser.build(is);
            Element root = doc.getRootElement();
            logger.debug(root.getName());


            Iterator results = root.getDescendants(new ElementFilter("resultspage"));

            if(results.hasNext()){
               Element ele = (Element) results.next();
               List<Element> resultItems = ele.getChildren("resultitem");
               for (Element e: resultItems){
                   List<Element> resultData = e.getChildren("resultdata");
                   Hashtable h = new Hashtable();
                   for (Element e2: resultData){
                       h.put(e2.getAttributeValue("name"), e2.getValue());
                   }
                   list.add(h);
               }
            }


        }catch(Exception e){
            logger.debug("parseReturn ",e);
        }
        return list;
    }




%>
