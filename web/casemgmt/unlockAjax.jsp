<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>

<%@page import="java.util.Set, java.util.Iterator"%>
<%@ page import="org.oscarehr.casemgmt.model.CaseManagementNote" %>
<%@page import="org.oscarehr.casemgmt.model.CaseManagementIssue" %>
<%@page import="oscar.util.DateUtils" %>

<% CaseManagementNote note = (CaseManagementNote)request.getAttribute("Note");
    pageContext.setAttribute("provName", note.getProviderName());
    String description;
    if( note.isSigned() )
        description = " signed by " + note.getSigning_provider_no();
    else
        description = " saved by " + note.getProviderName();
        
    String dateFormat = "dd-MMM-yyyy H:mm";
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>

    <c:if test="${success}">    
        <img title="Minimize Display" id='quitImg<c:out value="${Note.id}"/>' onclick='minView(event)' style='float:right; margin-right:5px;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/list-remove.png'/>
        <pre><c:out value="${Note.note}"/></pre>
        <span id="sig<c:out value="${Note.id}"/>">
            <p id="sumary<c:out value="${Note.id}"/>"><%=DateUtils.getDate(note.getUpdate_date(),dateFormat)  + description%><sup>rev <%=note.getRevision()%></sup>
                <%                        
                Set issSet = note.getIssues();
                if( issSet.size() > 0 ) {
                %>                      
                <br/>Assigned Issues
                <ul style="list-style: circle outside none; margin-top:0px;">                            
                    <% 
                    Iterator i = issSet.iterator();
                    while( i.hasNext() ) {
                    CaseManagementIssue iss = (CaseManagementIssue)i.next();
                    %>
                    <li><%=iss.getIssue().getDescription()%></li>
                    <%
                    }
                    }
                    %>
                </ul> 
            </p>
        </span>
        <script type="text/javascript">            
            Element.observe('<c:out value="n${Note.id}"/>', 'click', editNote);
        </script>
    </c:if>
    <c:if test="${ not success}">   
        <img title="Minimize Display" id='quitImg<c:out value="${Note.id}"/>' onclick='resetView(true, true, event)' style='float:right; margin-right:5px;' src='<c:out value="${ctx}"/>/oscarEncounter/graphics/list-remove.png'/>
        <pre><bean:message key="oscarEncounter.Index.msgLocked" /> <%=DateUtils.getDate(note.getUpdate_date(),dateFormat)%> <c:out value="${provName}" /></pre>
        <span id="passwdError"><pre>Incorrect password</pre></span>
        <p id='passwdPara'>Password:&nbsp;<input onkeypress="return passwordEnter('btnUnlock', event);" type='password' id='passwd' size='16'>&nbsp;
            <input id='btnUnlock' type='button' onclick="return unlock_ajax('<c:out value="${Note.id}"/>');" value='<bean:message key="oscarEncounter.Index.btnUnLock"/>'>
        </p>
        <script type="text/javascript">      
            $('passwd').focus();
        </script>
    </c:if>
