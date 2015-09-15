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

<%@page import="org.oscarehr.common.model.TicklerTextSuggest, org.oscarehr.common.dao.TicklerTextSuggestDao"%>
<%@page import="org.springframework.web.context.WebApplicationContext, org.springframework.web.context.support.WebApplicationContextUtils"%>
<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_tickler" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_tickler");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%
    if(session.getAttribute("user") == null)
        response.sendRedirect("../logout.jsp");

    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    TicklerTextSuggestDao ticklerTextSuggestDao = (TicklerTextSuggestDao) ctx.getBean("ticklerTextSuggestDao");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><bean:message key="tickler.ticklerEdit.title"/></title>
        <style type="text/css">
            h1{
                width:100%;
                background-color: black;
                text-align:left; 
                font-weight: 900; 
                height:40px;
                font-size:large;
                font-family:arial,sans-serif;
                color:white;   
                line-height: 40px;
            }
            h2{
                font-size:larger;
                text-align: center;
                font-family:arial,sans-serif;
            }
            th{
                font-family:arial,sans-serif;
                font-size:  medium;
                font-weight: bolder;
                text-align: center;
                color: #336666;
            }
        </style>
        <script language="javascript">
            function setEmpty(selectbox) {
                var emptyTxt = "<bean:message key="oscarEncounter.oscarConsultationRequest.AttachDocPopup.empty"/>";
                var emptyVal = "0";
                var op = document.createElement("option");
                try {
                    selectbox.add(op);
                }catch(e) {
                    selectbox.add(op,null);
                }
                selectbox.options[0].text = emptyTxt;
                selectbox.options[0].value = emptyVal;
            }

            function swap(srcName,dstName) {
                var src = document.getElementsByName(srcName)[0];
                var dst = document.getElementsByName(dstName)[0];
                var opt;

                //if nothing or dummy is being transfered do nothing
                if( src.selectedIndex == -1 || src.options[0].value == "0" )
                    return;

                //if dst has dummy clobber it with new options
                if( dst.options[0].value == "0" )
                    dst.remove(0);

                for( var idx = src.options.length - 1; idx >= 0; --idx ) {

                    if( src.options[idx].selected ) {
                        opt = document.createElement("option");
                        try {  //ie method of adding option
                            dst.add(opt);
                            dst.options[dst.options.length-1].text = src.options[idx].text;
                            dst.options[dst.options.length-1].value = src.options[idx].value;
                            dst.options[dst.options.length-1].className = src.options[idx].className;
                            src.remove(idx);
                        }catch(e) { //firefox method of adding option
                            dst.add(src.options[idx],null);
                            dst.options[dst.options.length-1].selected = false;
                        }

                    }

                } //end for

                if( src.options.length == 0 )
                    setEmpty(src);

            }      
            function addToList(listName, srcId) {
                var dst = document.getElementsByName(listName)[0];
                var src = document.getElementById(srcId);
                //if dst has dummy clobber it with new options
                if( dst.options[0].value == "0" )
                    dst.remove(0);
                var opt = document.createElement("option");
                try {  //ie method of adding option
                        dst.add(opt);
                        dst.options[dst.options.length-1].text = src.value;
                        dst.options[dst.options.length-1].value = src.value;
                        dst.options[dst.options.length-1].className = dst.options[dst.options.length-2].className;                        
                }catch(e) { //firefox method of adding option
                        opt.text = src.value;
                        opt.value = src.value;
                        opt.className = dst.options[dst.options.length-2].className;
                        dst.add(opt,null);
                        dst.options[dst.options.length-1].selected = false;
                }    
                src.value="";
            }
            
            function doSelect(listName){
                var lst = document.getElementsByName(listName)[0];
                for( var idx = lst.options.length - 1; idx >= 0; --idx ) {
                    lst.options[idx].selected = true;
                }
            }
        </script>
    </head>
    <body style="font-family:arial, sans-serif;background-color: #ddddff;">
       <h1><bean:message key="tickler.ticklerEdit.title"/></h1>
       <h2><bean:message key="tickler.ticklerTextSuggest.textSuggestTitle"/></h2>
        <html:form action="/tickler/EditTicklerTextSuggest">
            <input type="hidden" name="method" value="updateTextSuggest">
            <table width="100%">

                <tr>
                    <th><bean:message key="tickler.ticklerTextSuggest.activeText"/></th>
                    <td></td>
                    <th><bean:message key="tickler.ticklerTextSuggest.inactiveText"/></th>
                </tr>
                <tr>
                    <td>                    
                        <html:select property="activeText" style="width: 300px;" multiple="true" size="10">        
                            <%  java.util.List<TicklerTextSuggest> activeTexts = ticklerTextSuggestDao.getActiveTicklerTextSuggests();
                                if (activeTexts.isEmpty()) {
                             %>
                             <html:option value=""></html:option>
                            <%  }else {

                                    for (TicklerTextSuggest tTextSuggestActive : activeTexts) { 
                             %>
                             <html:option value="<%=tTextSuggestActive.getId().toString()%>"><%=tTextSuggestActive.getSuggestedText()%></html:option>
                            <%      }                                 
                                }
                             %>       
                        </html:select>
                    </td>
                    <td>
                        <input type="button" name="movetoInactive" value=">>" onclick="swap('activeText','inactiveText')"/>    
                        <br/>
                        <input type="button" name="movetoActive" value="<<" onclick="swap('inactiveText','activeText')"/>
                    </td>
                    <td>                    
                        <html:select property="inactiveText" style="width: 300px;" multiple="true" size="10">        
                            <%
                                java.util.List<TicklerTextSuggest> inactiveTexts = ticklerTextSuggestDao.getInactiveTicklerTextSuggests();
                                if (inactiveTexts.isEmpty()) {
                            %>
                              <html:option value=""></html:option>
                            <%
                                }else {
                                    for (TicklerTextSuggest tTextSuggestInactive : inactiveTexts) { 
                            %>
                             <html:option value="<%=tTextSuggestInactive.getId().toString()%>"><%=tTextSuggestInactive.getSuggestedText()%></html:option>
                            <%      } 
                                }
                            %>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <label for="newTextSuggest" style="font-weight:bold;font-size:small"><bean:message key="tickler.ticklerTextSuggest.enterText"/>:</label>
                         <input  id="newTextSuggest" name="newTextSuggest" type="text" maxlength="100" style="width: 95%"/>
                    </td>
                    <td style="vertical-align:  bottom;text-align: left;">
                         <input type="button" name="addNewTextSuggest" value="<bean:message key="tickler.ticklerTextSuggest.addText"/>" onclick="addToList('activeText','newTextSuggest')"/>
                    </td>
                </tr>
                <tr>
                    <td style="text-align:right;height:3em;vertical-align: bottom" colspan="3">
                        <input type="button" name="saveTextChanges" value="<bean:message key="tickler.ticklerTextSuggest.save"/>" onclick="doSelect('activeText');doSelect('inactiveText');document.tsTicklerForm.submit();"/>
                        <input type="button" name="cancelTextChanges" value="<bean:message key="tickler.ticklerTextSuggest.cancel"/>" onclick="window.close()"/>
                    </td>
                </tr>
            </table>
        </html:form>                           
    </body>
</html>
