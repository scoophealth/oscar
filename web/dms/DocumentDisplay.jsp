<%@ page import="oscar.dms.*,java.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext,org.oscarehr.common.dao.*,org.oscarehr.common.model.*"%><%

            WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
            ProviderInboxRoutingDao providerInboxRoutingDao = (ProviderInboxRoutingDao) ctx.getBean("providerInboxRoutingDAO");


            String provNo = request.getParameter("provNo");
            String start = request.getParameter("start");
            String end = request.getParameter("end");
            
            String  provMatched = request.getParameter("prov");
            String  demoMatched = request.getParameter("demo");
            
            boolean unmatchedDemographics = true;
            
            if (demoMatched != null && demoMatched.equals("on")){
                unmatchedDemographics = false;
            }
            
            System.out.println("P "+provNo+" S "+ start + " E "+  end+ "PM "+provMatched+" DM "+demoMatched);
            
            String creator = (String) session.getAttribute("user");
            
            java.util.Date startDate = oscar.util.UtilDateUtilities.Today();
            java.util.Date endDate = oscar.util.UtilDateUtilities.Tomorrow();
            ArrayList doctypes = EDocUtil.getDoctypes("demographic");

            EDocUtil edocUtil = new EDocUtil();
            ArrayList<EDoc> docs = edocUtil.getUnmatchedDocuments(creator, startDate, endDate, unmatchedDemographics);
            for (EDoc curdoc : docs) {

                String docId = curdoc.getDocId();
                int tabindex = 0;
                int slash = 0;
                String contentType = "";
                if ((slash = curdoc.getContentType().indexOf('/')) != -1) {
                    contentType = curdoc.getContentType().substring(slash + 1);
                }
                String dStatus = "";
                if ((curdoc.getStatus() + "").compareTo("A") == 0) {
                    dStatus = "active";
                } else if ((curdoc.getStatus() + "").compareTo("H") == 0) {
                    dStatus = "html";
                }
                String url = "ManageDocument.do?method=view&doc_no=" + curdoc.getDocId();
                String url2 = "ManageDocument.do?method=display&doc_no=" + curdoc.getDocId();
%>
<div id="document<%=docId%>">
    <table class="docTable">
        <tr>
            
            
            <td colspan="8"><a href="<%=url2%>"><img src="<%=url%>" /></a></td>
            
            
            <td align="left" valign="top">
                <fieldset><legend>Document Uploaded :<%=curdoc.getDateTimeStamp()%> - Content Type: <%=contentType%></legend>
                    <form id="forms<%=docId%>" action="undocumentReport.jsp" onsubmit="return sendToServer('forms<%=docId%>');">
                        <input type="hidden" name="method" value="documentUpdate" /> 
                        <input type="hidden" name="documentId" value="<%=docId%>" />
                        <table>
                            <tr>
                                <td><bean:message key="dms.documentReport.msgDocType" />:</td>
                                <td>
                                    <select <%--tabindex="<%=tabindex++%>"--%> name="docType" id="docType">
                                        <option value=""><bean:message key="dms.addDocument.formSelect" /></option>
                                        <%for (int j = 0; j < doctypes.size(); j++) {
                    String doctype = (String) doctypes.get(j);%>
                                        <option value="<%= doctype%>" <%=(curdoc.getType().equals(doctype)) ? " selected" : ""%>><%= doctype%></option>
                                        <%}%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td><bean:message key="dms.documentReport.msgDocDesc" />:</td>
                                <td><input <%-- tabindex="<%=tabindex++%>" --%> type="text" name="documentDescription" value="<%=curdoc.getDescription()%>" /></td>
                            </tr>
                            <tr>
                                <td>Observation Date:</td>
                                <td>
                                    <input <%--tabindex="<%=tabindex++%>" --%> id="observationDate" name="observationDate" type="text" value="<%=curdoc.getObservationDate()%>">
                                    <a id="obsdate"><img title="Calendar" src="../images/cal.gif" alt="Calendar"border="0" /></a>
                                </td>
                            </tr>
                            <tr>
                                <td>Demographic: <% if (request.getParameter("demo_linked_Id") != null && request.getParameter("demo_linked_Id").equals(curdoc.getDocId())) {%>
                                    <%=request.getParameter("name")%> <% }%>
                                </td>
                                <td><input type="hidden" name="demog" id="demofind<%=curdoc.getDocId()%>" /> 
                                    <input <%-- tabindex="<%=tabindex++%>"--%> type="text" id="autocompletedemo<%=curdoc.getDocId()%>" name="demographicKeyword" />
                                    <div id="autocomplete_choices<%=curdoc.getDocId()%>"class="autocomplete"></div>
                                    
                                    <script type="text/javascript">       
                                        new Ajax.Autocompleter("autocompletedemo<%=curdoc.getDocId()%>", "autocomplete_choices<%=curdoc.getDocId()%>", "../demographic/SearchDemographic.do", {minChars: 1, afterUpdateElement: saveDemoId});

                                    </script>
                                    <input type="checkbox" name="demoLink" >Send to MRP</input> 
                                </td>
                            </tr>
                            
                            
                            <tr>
                                <td valign="top">Flag Provider: </td>
                                <td> 
                                    <input <%--  tabindex="<%=tabindex++%>" --%> type="text" id="autocompleteprov<%=curdoc.getDocId()%>" name="demographicKeyword"/>
                                    <div id="autocomplete_choicesprov<%=curdoc.getDocId()%>" class="autocomplete"></div>
                                    
                                    <script type="text/javascript">
                                        new Ajax.Autocompleter("autocompleteprov<%=curdoc.getDocId()%>", "autocomplete_choicesprov<%=curdoc.getDocId()%>", "testProvcomp.jsp", {minChars: 1, afterUpdateElement: saveProvId});
                                        //console.log("autocompleteprov<%=curdoc.getDocId()%>");
                                </script>
                                    <div id="providerList<%=curdoc.getDocId()%>"></div>
                                </td>
                            </tr>
                            
                            
                            
                            <tr>
                                <td><bean:message key="dms.documentReport.msgCreator"/>:</td>
                                <td><%=curdoc.getCreatorName()%></td>
                            </tr>
                            
                            <tr>
                                <td colspan="2" align="right"><input <%-- tabindex="<%=tabindex++%>"--%> type="submit" name="save" value="Save" /></td>
                            </tr> 
                            
                            <tr>
                                <td colspan="2">
                                    Linked Providers
                                    <%
                                    Properties p = (Properties) session.getAttribute("providerBean");
                                    List<ProviderInboxItem> routeList = providerInboxRoutingDao.getProvidersWithRoutingForDocument("DOC", curdoc.getDocId());
                                    %>
                                    <ul>
                                    <%for (ProviderInboxItem pItem : routeList){%>
                                    <li><%=p.getProperty(pItem.getProviderNo(),pItem.getProviderNo())%></li>
                                    <%}%>
                                    </ul>
                                </td>
                            </tr>
                        </table>
                        
                    </form>
                </fieldset>      
                
            </td>
        </tr>
    </table>
</div>
<%}%>