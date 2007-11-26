<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>

<%@ page import="java.util.Set, java.util.List, java.util.Iterator"%>
<%@ page import="org.oscarehr.casemgmt.model.CaseManagementNote" %>
<%@ page import="org.oscarehr.casemgmt.model.CaseManagementIssue" %>
<%@ page import="org.oscarehr.PMmodule.model.Provider" %>
<%@ page import="oscar.util.DateUtils" %>

<% CaseManagementNote note = (CaseManagementNote)request.getAttribute("Note");
    pageContext.setAttribute("provName", note.getProviderName());    
        
    String dateFormat = "dd-MMM-yyyy H:mm";
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>

    <c:if test="${success}">    
        <img title="Minimize Display" id='quitImg<c:out value="${Note.id}"/>' onclick='minView(event)' style='float:right; margin-right:5px;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/triangle_up.gif'/>
        <pre><c:out value="${Note.note}"/></pre>
        <div id="sig<c:out value="${Note.id}"/>">
            <div class="sig" id="sumary<c:out value="${Note.id}"/>">
                <div id="observation<c:out value="${Note.id}"/>" style="float:right;margin-right:3px;"><i>Date:&nbsp;<span id="obs<c:out value="${Note.id}"/>"><%=DateUtils.getDate(note.getObservation_date(),dateFormat)%></span>&nbsp;rev<a href="#" onclick="return showHistory('<c:out value="${Note.id}"/>', event);"><%=note.getRevision()%></a></i></div>
                <div><span style="float:left;">Editors:</span>
                      <ul style="list-style: none inside none; margin:0px;">    
                          <%  
                          List editors = note.getEditors();
                          Iterator<Provider> it = editors.iterator(); 
                          int count = 0;
                          int MAXLINE = 2;
                          while( it.hasNext() ) {
                              Provider p = it.next();

                              if( count % MAXLINE == 0 ) {
                                  out.print("<li>" + p.getFormattedName() + "; ");
                              }
                              else {
                                  out.print(p.getFormattedName() + "</li>");
                              }
                              ++count;
                          }
                          if( count % MAXLINE == 0 )
                              out.print("</li>");
                          %>

                      </ul>
                  </div>

                <%                        
                Set issSet = note.getIssues();
                if( issSet.size() > 0 ) {
                %>                      
                Assigned Issues
                <ul style="list-style: circle inside none; margin:0px;">                             
                    <% 
                    Iterator i = issSet.iterator();
                    while( i.hasNext() ) {
                    CaseManagementIssue iss = (CaseManagementIssue)i.next();
                    %>
                    <li><%=iss.getIssue().getDescription()%></li>
                    <%
                    }
                    %>
                </ul>
                    <%
                    }
                    %>                 
            </div>
        </div>
        <script type="text/javascript">            
            Element.observe('<c:out value="n${Note.id}"/>', 'click', editNote);
        </script>
    </c:if>
    <c:if test="${ not success}"> 
        <img title="Minimize Display" id='quitImg<c:out value="${Note.id}"/>' alt="Minimize Display" onclick='resetView(true, true, event)' style='float:right; margin-right:5px;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/triangle_up.gif'/>        
        <pre><bean:message key="oscarEncounter.Index.msgLocked" /> <%=DateUtils.getDate(note.getUpdate_date(),dateFormat)%> <c:out value="${provName}" /></pre>
        <span id="passwdError"><pre style="color:red;">Incorrect password</pre></span>
        <p id='passwdPara' class="passwd">Password:&nbsp;<input onkeypress="return grabEnter('btnUnlock', event);" type='password' id='passwd' size='16'>&nbsp;
            <input id='btnUnlock' type='button' onclick="return unlock_ajax('<c:out value="n${Note.id}"/>');" value='<bean:message key="oscarEncounter.Index.btnUnLock"/>'>
        </p>
        <script type="text/javascript">      
            $('passwd').focus();
        </script>
    </c:if>
