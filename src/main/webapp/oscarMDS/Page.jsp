<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@page import="java.net.URLEncoder"%>
<%@ page language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="oscar.oscarMDS.data.*,oscar.oscarLab.ca.on.*,oscar.util.StringUtils,oscar.util.UtilDateUtilities" %>
<%@ page import="org.apache.commons.collections.MultiHashMap" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@page import="org.oscarehr.common.hl7.v2.oscar_to_oscar.OscarToOscarUtils"%>
<%@page import="org.oscarehr.util.MiscUtils,org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.apache.log4j.Logger,org.oscarehr.common.dao.OscarLogDao,org.oscarehr.util.SpringUtils"%>

<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_lab" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_lab");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
Logger logger=MiscUtils.getLogger();

String view = (String)request.getAttribute("view");
Integer pageSize=(Integer)request.getAttribute("pageSize");
Integer pageNum=(Integer)request.getAttribute("pageNum");
Integer pageCount=(Integer)request.getAttribute("pageCount");
Map<String,String> docType=(Map<String,String>)request.getAttribute("docType");
Map<String,List<String>> patientDocs=(Map<String,List<String>>)request.getAttribute("patientDocs");
String providerNo=(String)request.getAttribute("providerNo");
String searchProviderNo=(String)request.getAttribute("searchProviderNo");
Map<String,String> patientIdNames=(Map<String,String>)request.getAttribute("patientIdNames");
String patientIdNamesStr=(String)request.getAttribute("patientIdNamesStr");
Map<String,String> docStatus=(Map<String,String>)request.getAttribute("docStatus");
String patientIdStr =(String)request.getAttribute("patientIdStr");
Map<String,List<String>> typeDocLab =(Map<String,List<String>>)request.getAttribute("typeDocLab");
String demographicNo=(String)request.getAttribute("demographicNo");
String ackStatus = (String)request.getAttribute("ackStatus");
List labdocs=(List)request.getAttribute("labdocs");
Map<String,Integer> patientNumDoc=(Map<String,Integer>)request.getAttribute("patientNumDoc");
Integer totalDocs=(Integer) request.getAttribute("totalDocs");
Integer totalHL7=(Integer)request.getAttribute("totalHL7");
List<String> normals=(List<String>)request.getAttribute("normals");
List<String> abnormals=(List<String>)request.getAttribute("abnormals");
Integer totalNumDocs=(Integer)request.getAttribute("totalNumDocs");
String selectedCategory = request.getParameter("selectedCategory");
String selectedCategoryPatient = request.getParameter("selectedCategoryPatient");
String selectedCategoryType = request.getParameter("selectedCategoryType");
boolean isListView = Boolean.valueOf(request.getParameter("isListView"));

OscarLogDao oscarLogDao = (OscarLogDao) SpringUtils.getBean("oscarLogDao");
String curUser_no = (String) session.getAttribute("user");

%>

<% if (isListView && pageNum == 0) { %>
		<script type="text/javascript">
			function submitLabel(lblval){
		       	 document.forms['TDISLabelForm'].label.value = document.forms['acknowledgeForm'].label.value;
	       	}
		</script>
		
        <table  oldclass="MainTable" id="scrollNumber1" border="0" name="encounterTable" cellspacing="0" cellpadding="3" width="100%">
            <tr oldclass="MainTableTopRow">
                <td class="MainTableTopRowRightColumn" colspan="10" align="left">
                 <table width="100%">
                     <tr>
                           <td align="left" valign="center" > <%-- width="30%" --%>
                               <% if (labdocs.size() > 0) { %>
                                   <input id="topFBtn" type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="parent.checkSelected(document)">
                                   <% if (ackStatus.equals("N") || ackStatus.isEmpty()) {%>
                                       <input id="topFileBtn" type="button" class="smallButton" value="File" onclick="parent.submitFile(document)"/>
                                   <% }
                               }%>
                               <input type="hidden" id="currentNumberOfPages" value="0"/>
                           </td>
                     </tr>
                 </table>
                </td>
            </tr>
            <tr>
                <td style="margin:0px;padding:0px;">
                    <%--
                    <table width="100%" style="margin:0px;padding:0px;" cellpadding="0" cellspacing="0">
                        <tr>
                            <th align="left" valign="bottom" class="cell" nowrap>
                                <input type="checkbox" onclick="checkAll('lab_form');" name="checkA"/>
                                <bean:message key="oscarMDS.index.msgHealthNumber"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgPatientName"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgSex"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgResultStatus"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgDateTest"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgOrderPriority"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgRequestingClient"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgDiscipline"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgReportStatus"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                Ack #
                            </th>
                        </tr>
					</table>
					 --%>
					<div id="listViewDocs" style="height:536px; overflow:auto;" onscroll="handleScroll(this)">
					<style type="text/css">
						#summaryView td, #summaryView th {
							padding: 0px 5px;
						}
					</style>
					<table id="summaryView" width="100%" style="margin:0px;padding:0px;" cellpadding="0" cellspacing="0">
					<thead>
						<tr>
                            <th align="left" valign="bottom" class="cell" nowrap>
                                <input type="checkbox" onclick="checkAllLabs('lab_form');" name="checkA"/>
                                <bean:message key="oscarMDS.index.msgHealthNumber"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgPatientName"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgSex"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgResultStatus"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgDateTest"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgOrderPriority"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgRequestingClient"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgDiscipline"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgReportStatus"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                Ack #
                            </th>
                        </tr>
                          </thead>    
                          <tbody>
                                            <%
							} // End if(pageNum == 1)
                            List<String> doclabid_seq=new ArrayList<String>();
                            Integer number_of_rows_per_page=pageSize;
                            Integer totalNoPages=pageCount;
                            Integer total_row_index=labdocs.size()-1;
                            if (total_row_index < 0 || (totalNoPages != null && totalNoPages.intValue() == (pageNum+1))) {
                                	%> <input type="hidden" name="NoMoreItems" value="true" /> <%
                            		if (isListView) { %>
		                                <tr>
		                                    <tdcolspan="9" align="center">
		                                        <i>	<% if (pageNum == 1) { %>
		                                        	<bean:message key="oscarMDS.index.msgNoReports"/>
		                                        	<% } else { %>
		                                        	<bean:message key="oscarMDS.index.msgNoMoreReports"/>
		                                        	<% } %>
		                                        </i>

		                                    </td>
		                                </tr>
	                         	<%	}
                            		else {
                            		%>
                            			<center>
                            			<div>
                            			<% if (pageNum == 1) { %>
                                       	<bean:message key="oscarMDS.index.msgNoReports"/>
                                       	<% } else { %>
                                       	<bean:message key="oscarMDS.index.msgNoMoreReports"/>
                                       	<% } %>
                            			</div>
                            			</center>
                            		<%
                            		}

                                }
                            for (int i = 0; i < labdocs.size(); i++) {

                                LabResultData   result =  (LabResultData) labdocs.get(i);
                                //LabResultData result = (LabResultData) labMap.get(labNoArray.get(i));

                                String segmentID        =  result.getSegmentID();
                                String status           =  result.getAcknowledgedStatus();

                                String bgcolor = i % 2 == 0 ? "#e0e0ff" : "#ccccff" ;
                                if (!result.isMatchedToPatient()){
                                   bgcolor = "#FFCC00";
                                }

								String labRead = "";
								
                                if(result.isHRM() && !oscarLogDao.hasRead(curUser_no,"hrm",segmentID)){
                                	labRead = "*";
                                }
                                
                                if( !result.isHRM() && result.isDocument() && !oscarLogDao.hasRead(curUser_no,"document",segmentID) ) {                                    
                                    labRead = "*";
                                }
                                
                                if(!result.isHRM() && !result.isDocument() && !oscarLogDao.hasRead(curUser_no,"lab",segmentID)){
                                	labRead = "*";
                                }


                                String discipline=result.getDiscipline();
                                if(discipline==null || discipline.equalsIgnoreCase("null"))
                                    discipline="";
                                MiscUtils.getLogger().debug("result.isAbnormal()="+result.isAbnormal());
                                doclabid_seq.add(segmentID);
                                request.setAttribute("segmentID", segmentID);
                                String demoName = StringEscapeUtils.escapeJavaScript(result.getPatientName());

                                if (!isListView) {
                                	try {
                                		if (result.isDocument()) { %>
                                <!-- segment ID <%= segmentID %>  -->
                                <!-- demographic name <%=StringEscapeUtils.escapeJavaScript(result.getPatientName()) %>  -->
                                <form id="frmDocumentDisplay_<%=segmentID%>">
                                	<input type="hidden" name="segmentID" value="<%=segmentID%>"/>
									<input type="hidden" name="demoName" value="<%=demoName%>"/>
									<input type="hidden" name="providerNo" value="<%=providerNo%>"/>
									<input type="hidden" name="searchProviderNo" value="<%=searchProviderNo%>"/>
									<input type="hidden" name="status" value="<%=status%>"/>
								</form> 
                                <div id="document_<%=segmentID%>">                                 	                                		                            
                        			<jsp:include page="../dms/showDocument.jsp" flush="true">
                        				<jsp:param name="segmentID" value="<%=segmentID%>"/>
                        				<jsp:param name="demoName" value="<%=demoName%>"/>
                        				<jsp:param name="providerNo" value="<%=providerNo%>"/>
                        				<jsp:param name="searchProviderNo" value="<%=searchProviderNo%>"/>
                        				<jsp:param name="status" value="<%=status%>"/>
                        			</jsp:include>
								</div>
                        		<%
                                		}
                                		else if (result.isHRM()) {

                                			StringBuilder duplicateLabIds=new StringBuilder();
                                        	for (Integer duplicateLabId : result.getDuplicateLabIds())
                                        	{
                                        		if (duplicateLabIds.length()>0) duplicateLabIds.append(',');
                                        		duplicateLabIds.append(duplicateLabId);
                                        	}
                                		%>

                                	<jsp:include page="../hospitalReportManager/Display.do" flush="true">
                                		<jsp:param name="id" value="<%=segmentID %>" />
                                		<jsp:param name="segmentID" value="<%=segmentID %>" />
                                		<jsp:param name="providerNo" value="<%=providerNo %>" />
                                		<jsp:param name="searchProviderNo" value="<%=searchProviderNo %>" />
                                		<jsp:param name="status" value="<%=status %>" />
                                		<jsp:param name="demoName" value="<%=result.getPatientName() %>" />
                                		<jsp:param name="duplicateLabIds" value="<%=duplicateLabIds.toString() %>" />
                                	</jsp:include>
		                        		<% } else {

		                        		%>
		                        		<%--
		                        				<iframe src="../lab/CA/ALL/labDisplayAjax.jsp?segmentID=<%=segmentID %>" style="height:100%;width:100%;border:0;"></iframe>
		                        		--%>
		                        		<jsp:include page="../lab/CA/ALL/labDisplayAjax.jsp" flush="true">
		                        			<jsp:param name="segmentID" value="<%=segmentID%>"/>
		                        			<jsp:param name="demoName" value="<%=demoName%>"/>
				                        	<jsp:param name="providerNo" value="<%=providerNo%>"/>
		                        			<jsp:param name="searchProviderNo" value="<%=searchProviderNo%>"/>
		                        			<jsp:param name="status" value="<%=status%>"/>
		                        			<jsp:param name="showLatest" value="true" />
		                        		</jsp:include>

		                        		<%
		                        		}
                                	}
                                	catch (Exception e) { logger.error(e.toString()); }
                                }
                                else {
                        		%>
                                <tr id="labdoc_<%=segmentID%>" bgcolor="<%=bgcolor%>" <%if(result.isDocument()){%> name="scannedDoc" <%} else{%> name="HL7lab" <%}%> class="<%= (result.isAbnormal() ? "AbnormalRes" : "NormalRes" ) + " " + (result.isMatchedToPatient() ? "AssignedRes" : "UnassignedRes") %>">
                                <td nowrap>
                                    <input type="hidden" id="totalNumberRow" value="<%=total_row_index+1%>">
                                    <input type="checkbox" name="flaggedLabs" value="<%=segmentID%>">
                                    <input type="hidden" name="labType<%=segmentID+result.labType%>" value="<%=result.labType%>"/>
                                    <input type="hidden" name="ackStatus" value="<%= result.isMatchedToPatient() %>" />
                                    <input type="hidden" name="patientName" value="<%=StringEscapeUtils.escapeHtml(result.patientName) %>"/>
                                    <%=result.getHealthNumber() %>
                                </td>
                                <td nowrap>
                                    <% if ( result.isMDS() ){ %>
                                    <a href="javascript:parent.reportWindow('SegmentDisplay.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>')"><%=labRead%><%= StringEscapeUtils.escapeHtml(result.getPatientName())%></a>
                                    <% }else if (result.isCML()){ %>
                                    <a href="javascript:parent.reportWindow('<%=request.getContextPath()%>/lab/CA/ON/CMLDisplay.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>')"><%=labRead%><%=StringEscapeUtils.escapeHtml(result.getPatientName())%></a>
                                    <% }else if (result.isHL7TEXT())
                                   	{
                                    	String categoryType=result.getDiscipline();

                                    	if ("REF_I12".equals(categoryType))
                                    	{
	                                    	%>
                                      			<a href="javascript:parent.popupConsultation('<%=segmentID%>')"><%=labRead%><%=StringEscapeUtils.escapeHtml(result.getPatientName())%></a>
                                    		<%
                                    	}
                                    	else if (categoryType!=null && categoryType.startsWith("ORU_R01:"))
                                    	{
	                                    	%>
                                      			<a href="<%=request.getContextPath()%>/lab/CA/ALL/viewOruR01.jsp?segmentId=<%=segmentID%>"><%=labRead%><%=StringEscapeUtils.escapeHtml(result.getPatientName())%></a>
                                    		<%
                                    	}
                                    	else
                                    	{
	                                    	%>
	                                    		<a href="javascript:parent.reportWindow('<%=request.getContextPath()%>/lab/CA/ALL/labDisplay.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>&showLatest=true')"><%=labRead%><%=StringEscapeUtils.escapeHtml(result.getPatientName())%></a>
	                                    	<%
                                    	}
                                    }
                                    else if (result.isDocument()){ 
                                	String patientName = result.getPatientName();
                                    	StringBuilder url = new StringBuilder(request.getContextPath());                                    	
                                    	url.append("/dms/showDocument.jsp?inWindow=true&segmentID=");
                                    	url.append(segmentID);
                                    	url.append("&providerNo=");
                                    	url.append(providerNo);
                                    	url.append("&searchProviderNo=");
                                    	url.append(searchProviderNo);
                                    	url.append("&status=");
                                    	url.append(status);
                                    	url.append("&demoName=");
                                    	
                                    	//the browser html parser does not understand javascript so we need to account for the opening
                                    	//and closing quotes used in the onclick event handler                                    	
                                    	patientName = StringEscapeUtils.escapeHtml(patientName);
                                    	
                                    	//now that the html parser will pass the correct characters to the javascript engine we need to 
                                    	//escape chars for javascript that are not transformed by the html escape.
                                    	url.append(StringEscapeUtils.escapeJavaScript(patientName));                                    	                                    	                                    	
                                    %>                                    
                                    
                                    <a href="javascript:void(0);" onclick="reportWindow('<%=url.toString()%>',screen.availHeight, screen.availWidth); return false;" ><%=labRead + StringEscapeUtils.escapeHtml(result.getPatientName())%></a>
                                    
                                    <% }else if(result.isHRM()){
                                    	StringBuilder duplicateLabIds=new StringBuilder();
                                    	for (Integer duplicateLabId : result.getDuplicateLabIds())
                                    	{
                                    		if (duplicateLabIds.length()>0) duplicateLabIds.append(',');
                                    		duplicateLabIds.append(duplicateLabId);
                                    	}
                                    %>
                                    <a href="javascript:reportWindow('../hospitalReportManager/Display.do?id=<%=segmentID%>&segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>&demoName=<%=demoName%>&duplicateLabIds=<%=duplicateLabIds.toString()%> ',850,1020)"><%=labRead%><%=result.getPatientName()%></a>
                                    <% }else {%>
                                    <a href="javascript:parent.reportWindow('<%=request.getContextPath()%>/lab/CA/BC/labDisplay.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>')"><%=labRead%><%=StringEscapeUtils.escapeJavaScript(result.getPatientName())%></a>
                                    <% }%>
                                </td>
                                <td nowrap>
                                    <center><%=result.getSex() %></center>
                                </td>
                                <td nowrap>
                                    <%= (result.isAbnormal() ? "Abnormal" : "" ) %>
                                </td>
                                <td nowrap>
                                    <%=result.getDateTime() + (result.isDocument() ? " / " + result.lastUpdateDate : "")%>
                                </td>
                                <td nowrap>
                                    <%=result.getPriority()%>
                                </td>
                                <td nowrap>
                                    <%=result.getRequestingClient()%>
                                </td>
                                <td nowrap>
                                    <%=result.isDocument() ? result.description == null ? "" : result.description : result.getDisciplineDisplayString()%>
                                </td>
                                <td nowrap> <!--  -->
                                    <%= ((result.isReportCancelled())? "Cancelled" : result.isFinal() ? "Final" : "Partial")%>
                                </td>
                                <td nowrap>
                                    <% int multiLabCount = result.getMultipleAckCount(); %>
                                    <%= result.getAckCount() %>&#160<% if ( multiLabCount >= 0 ) { %>(<%= result.getMultipleAckCount() %>)<%}%>
                                </td>
                            </tr>
                         <% }


                            } // End else from if(isListView)
                            if (isListView && pageNum == 0) { %>
                            </tbody>
                       	</table>

                       	<table width="100%" style="margin:0px;padding:0px;" cellpadding="0" cellspacing="0">
                       		<tr><td bgcolor="E0E1FF">
                       			<div id='loader' style="display:none"><img src='<%=request.getContextPath()%>/images/DMSLoader.gif'> Loading reports...</div>
                       		</td></tr>
                       	</table>
                       	</div>
                       	<% if (labdocs.size() > 0) { %>
                       	<table width="100%" style="margin:0px;padding:0px;" cellpadding="0" cellspacing="0">
                            <tr class="MainTableBottomRow">
                                <td class="MainTableBottomRowRightColumn" bgcolor="#003399" colspan="10" align="left">
                                    <table width="100%">
                                        <tr>
                                            <td align="left" valign="middle" width="30%">

                                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="parent.checkSelected(document)">
                                                    <% if (ackStatus.equals("N")) {%>
                                                        <input type="button" class="smallButton" value="File" onclick="parent.submitFile(document)"/>
                                                    <% }  %>
                                            </td>
                                        <script type="text/javascript">
                                                var doclabid_seq='<%=doclabid_seq%>';
                                                doclabid_seq=doclabid_seq.replace('[','');
                                                doclabid_seq=doclabid_seq.replace(']','');
                                                var arr=doclabid_seq.split(',');
                                                var arr2=new Array();
                                                for(var i=0;i<arr.length;i++){
                                                    var ele=arr[i];
                                                    ele=ele.replace(' ','');
                                                    arr2.push(ele);
                                                }
                                                doclabid_seq=arr2;

                                                oldestLab = '<%=request.getAttribute("oldestLab") %>';

                                        </script>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                    </table>
                    <% } %>
                </td>
            </tr>
        </table>
    <% } // End if (pageNum == 1) %>

