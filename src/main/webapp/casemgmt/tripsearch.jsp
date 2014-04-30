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
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="org.apache.commons.httpclient.HttpClient"%>
<%@page import="org.apache.commons.httpclient.methods.GetMethod"%>
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
        List<HashMap <String,String>> list = searchTripDatabase(searchString);
        if (list != null && list.size() > 0){%>
        <table class="sample">
            <%for (HashMap<String,String> h : list) {%>

            <tr>
                <td>
                    <a href="<%=h.get("link")%>" ><%=h.get("title")%></a><br/>
                     <%=h.get("publication")%> -- <%=h.get("year")%>
                </td>

            </tr>

            <%}%>
        </table>

        <%} else {%>
        No Results
        <%}%>
    </body>
</html>

<%--

<total>169703</total>
<count>20</count>
<skip>0</skip>
 

"total" is the total number of results available, and "count" is the number returned in this request - we only send back the first 20 by default, but you can request the next 20 by appending a "skip" parameter thus:
http://www.tripdatabase.com/search/xml?key=XXXXXX&criteria=cancer&skip=20


FILTERS category and publication


As I understand it you 'simply' transform the URL given (I think that was included in the previous instructions).  So, the URL for a search for 'prostate cancer' and restricted to 'systematic reviews' is:
 
http://www.tripdatabase.com/search?criteria=prostate+cancer&sort=r&categoryid=11
 
Similarly, to restrict by publication it's:
 
http://www.tripdatabase.com/search?publicationid=14&criteria=prostate+cancer
the trick with the latter one is to carry out a search and under each result the publisher name is 'clickable'.  If you click on the name it restricts the results to just that publication.
 


 --%>

<%!
    public List<HashMap <String,String>> searchTripDatabase(String searchString) throws Exception{
       Logger logger = MiscUtils.getLogger();
       List<HashMap <String,String>> h  = null;
       GetMethod post = new GetMethod("http://www.tripdatabase.com/search/xml?key=MCM001&criteria="+URLEncoder.encode(searchString,"UTF-8"));
      
       HttpClient httpclient = new HttpClient();
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

     private List<HashMap <String,String>> parseReturn(InputStream is){
        Logger logger = MiscUtils.getLogger();
        List<HashMap <String,String>> list = new ArrayList<HashMap <String,String>>();
        try {

            SAXBuilder parser = new SAXBuilder();
            Document doc = parser.build(is);
            Element root = doc.getRootElement();
            logger.debug(root.getName());
            Iterator<Element> results = root.getDescendants(new ElementFilter("document"));
			
            while(results.hasNext()){
               Element ele = results.next();
               HashMap<String,String> h = new HashMap<String,String>();
               h.put("title",ele.getChild("title").getText());
               h.put("link",ele.getChild("link").getText());
               h.put("year", ele.getChild("pubDate").getText()); 
               h.put("publication", ele.getChild("publication").getText()); 
               list.add(h);
            }
        }catch(Exception e){
            logger.debug("parseReturn ",e);
        }
        return list;
    }
%>
