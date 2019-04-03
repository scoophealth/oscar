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

<%@ include file="/taglibs.jsp"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.oscarehr.common.model.DemographicContact"%>
<%
	String id = request.getParameter("id");
    StringUtils.trimToEmpty(id);
	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	request.setAttribute("providers",providerDao.getActiveProviders());
%>

<div id="contact_<%=id%>">
					<input type="hidden" name="contact_<%=id%>.id" id="contact_<%=id%>.id" value=""/>
					
					<a href="#" onclick="deleteContact(<%=id%>);">[Delete]</a>
					
		            <select name="contact_<%=id%>.role" id="contact_<%=id%>.role">
						<option value="Mother">Mother</option>
						<option value="Father">Father</option>
						<option value="Parent">Parent</option>
						<option value="Wife">Wife</option>
						<option value="Husband">Husband</option>
						<option value="Partner">Partner</option>
						<option value="Son">Son</option>
						<option value="Daughter">Daughter</option>
						<option value="Brother">Brother</option>
						<option value="Sister">Sister</option>
						<option value="Aunt">Aunt</option>
						<option value="Uncle">Uncle</option>
						<option value="GrandFather">GrandFather</option>
						<option value="GrandMother">GrandMother</option>
						<option value="Neighbour">Neighbour</option>
						<option value="Guardian">Guardian</option>
                                                <option value="Foster Parent">Foster Parent</option>
                                                <option value="Next of Kin">Next of Kin</option>
						<option value="Administrative Staff">Administrative Staff</option>
						<option value="Care Giver">Care Giver</option>
						<option value="Power of Attorney">Power of Attorney</option>
						<option value="Insurance">Insurance</option>
						<option value="Guarantor">Guarantor</option>
						<option value="Other">Other</option>		
						<option value="">Unknown</option>		            	
		            </select>
	            	
	            	&nbsp;
	            	
	            	<select name="contact_<%=id%>.consentToContact" id="procontact_<%=id%>.consentToContact" title="Consent to Contact">
	            		<option value="1">Consent</option>
						<option value="0">No Consent</option>
	            	</select>
	            	
	            	&nbsp;
	            	
	            	<select name="contact_<%=id%>.active" id="procontact_<%=id%>.active" title="Active">
	            		<option value="1">Active</option>
						<option value="0">Inactive</option>
	            	</select>
	            	
	            	&nbsp;
	            	<!--  they can be an internal (Demographic) or external (Contact) contact -->
	            		             
		            <select name="contact_<%=id%>.type" id="contact_<%=id%>.type">
		            	<option value="<%=DemographicContact.TYPE_DEMOGRAPHIC%>">Internal</option>
		            	<option value="<%=DemographicContact.TYPE_CONTACT%>">External</option>
					</select>
	            			           
	            	<input type="hidden" name="contact_<%=id%>.contactId" value="0"/>
		             <input type="text" name="contact_<%=id%>.contactName" id="contact_<%=id%>.contactName" size="20" readonly="readonly"/>		             
		             <a href="#" onclick="doPersonalSearch('<%=id%>');return false;">${param.search}</a>
		             
		             &nbsp;
		             SDM:<input type="checkbox" name="contact_<%=id%>.sdm"/>
		             EC:<input type="checkbox" name="contact_<%=id%>.ec"/>
		             <textarea name="contact_<%=id%>.note" rows="3" cols="25" title="Contact Note"></textarea>
</div>
