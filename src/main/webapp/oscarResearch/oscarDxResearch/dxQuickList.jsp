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
<%--
Liberated by Dennis Warren @ Colcamex 
Created by RJ
Required Parameters to plug-in: 

	disabled : returns true or false.
	quickList : default quick list name by parameter

 --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<table id="dxCodeQuicklist" style="width:100%;border-collapse:collapse;">
	<tr >
		<td class="heading">
			<bean:message key="oscarResearch.oscarDxResearch.quickList" />
		</td>
	</tr>
	<tr>
		<td>
			<table>
			<tr>
				<td>
					<html:select size="5" style="overflow:auto" property="quickList"
						onchange="javascript:changeList(this,'${ demographicNo }','${ providerNo }');"  >			
						<logic:iterate id="quickLists" name="allQuickLists" property="dxQuickListBeanVector" >				
							<option value="${ quickLists.quickListName }" ${ quickLists.quickListName eq param.quickList || quickLists.lastUsed eq 'Selected' ? 'selected' : '' } >			
								<bean:write	name="quickLists" property="quickListName" />
							</option>
						</logic:iterate>
					</html:select>
				</td>
				<td>
					<i>(Click the titles to switch lists)</i>
				</td>
			</tr>
			</table>
		</td>
	</tr>

	<logic:iterate id="item" name="allQuickListItems" property="dxQuickListItemsVector">	
		<tr>
			<td class="quickList">			
				<html:link href="#" title="${ item.dxSearchCode }"
					onclick="javascript:submitform( '${ item.dxSearchCode }', '${ item.type }' )" >
						<bean:write name="item" property="description" />
				</html:link>
			</td>
		</tr>		
	</logic:iterate>

</table>

<script type="text/javascript">
	function changeList(quickList, demographicNo, providerNo){       
	    location.href = 'setupDxResearch.do?demographicNo='
	    		+demographicNo
	    		+'&quickList='
	    		+quickList.value
	    		+'&providerNo='
	    		+providerNo;
	}
</script>
