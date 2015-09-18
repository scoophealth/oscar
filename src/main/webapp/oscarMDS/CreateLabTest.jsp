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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_lab" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_lab");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%@ include file="/taglibs.jsp"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.oscarehr.common.model.DemographicContact"%>
<%
	String id = request.getParameter("id");
%>

<div id="test_<%=id%>">
					<input type="hidden" name="test_<%=id%>.id" id="test_<%=id%>.id" value="<%=id%>"/>
								
					<fieldset>            	
                <legend>Test Information</legend>

                <table border="0">
					<tr>
						<td><label>Date:</label><input type="text" name="test_<%=id%>.valDate" id="test_<%=id%>.valDate" size="14"/><img src="<%=request.getContextPath()%>/images/cal.gif" id="test_<%=id%>.valDate_cal" /></td>
						<td><label>Flag:</label>
                 		<select name="test_<%=id%>.flag" id="test_<%=id%>.flag">
                        	<option value="">None</option>
                            <option value="A">Abnormal</option>
                            <option value="N">Normal</option>
                        </select>
                     </td>
                     <td><label>Status:</label>
 						<select name="test_<%=id%>.stat" id="test_<%=id%>.stat">
                    		<option value="F">Final</option>
                   			<option value="P">Partial</option>
                        </select>
                    </td>                
                 		</tr>
                 
                	<tr>
                		
                		<td><label>Code Type:</label>
                			<select name="test_<%=id%>.codeType" id="test_<%=id%>.codeType">
                            	<option value="ST">ST</option>
                                <option value="FT">FT</option>
                            </select>
                        </td>
                  	<td><label>Code:</label><input type="text" name="test_<%=id%>.code" size="10" id="test_<%=id%>.code"/></td>
                  	<td><label>Name:</label><input type="text" name="test_<%=id%>.lab_test_name" size="15" id="test_<%=id%>.lab_test_name"/></td>
                 	<td colspan="2"><label>Description:</label><input type="text" name="test_<%=id%>.test_descr" size="40" id="test_<%=id%>.test_descr"/></td>
                 	
                  </tr>
                 
                 
                 <tr>
                 	<td><label>Value:</label><input type="text" name="test_<%=id%>.codeVal" size="10" id="test_<%=id%>.codeVal"/></td>
                 	<td><label>Unit:</label><input type="text" name="test_<%=id%>.codeUnit" size="10" id="test_<%=id%>.codeUnit"/></td>
                 	
                 	<td><label>refRange (low):</label><input type="text" name="test_<%=id%>.refRangeLow" id="test_<%=id%>.refRangeLow" size="5"/></td>
                 	<td><label>refRange (high):</label><input type="text" name="test_<%=id%>.refRangeHigh" id="test_<%=id%>.refRangeHigh" size="5"/></td>
                 	<td><label>refRange (text):</label><input type="text" name="test_<%=id%>.refRangeText" id="test_<%=id%>.refRangeText" size="5"/></td>
                 </tr>                
                 
 			     <tr><td><label>Lab Notes:</label></td><td colspan="4"><textarea name="test_<%=id%>.labnotes" id="test_<%=id%>.labnotes" rows="5" cols="30"></textarea></td></tr>
	                                                                                                
                                 
                </table>
                
                <a href="#" onclick="deleteTest(<%=id%>); return false;">[Delete]</a>
										
		       </fieldset>
		       <script>
			       Calendar.setup({ inputField : "test_<%=id%>.valDate", ifFormat : "%Y-%m-%d %H:%m", showsTime :true, button : "test_<%=id%>.valDate_cal" });

		       </script>
</div>
