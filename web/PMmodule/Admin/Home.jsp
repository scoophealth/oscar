<!-- /PMmodule/Admin/Home.jsp -->

<%
String _appPath = request.getContextPath();
%>
<%@ include file="/taglibs.jsp"%>
<%@page import="com.quatro.common.KeyConstants;" %>

<table width="100%" height="100%" cellpadding="0px" cellspacing="0px" style="border-width: 1px; border-style: solid; bordercolor: black">
	<tr>
		<th class="pageTitle" align="center"><span
			id="_ctl0_phBody_lblTitle" align="left">System Administration</span></th>
	</tr>
<!--
	<tr>
		<td align="left" class="buttonBar"><html:link action="/Home.do"
			style="color:Navy;text-decoration:none">
			<img style="vertical-align: middle" border=0 src=<html:rewrite page="/images/home16.png"/> />&nbsp;Home&nbsp;&nbsp;|</html:link>
		</td>
	</tr>
-->
	<tr>
		<td align="left"></td>
	</tr>
	<tr>
		<td height="100%">
		<div
			style="color: Black; background-color: White; 
                    height: 100%; width: 100%; overflow: auto;" id="scrollBar">

		<table width="100%" cellpadding="0" cellspacing="0" style="vertical-align: middle">
			<tr>
				<td>&nbsp;
				</td>
			</tr>
			<tr>
				<td width="10%" style="vertical-align: middle">&nbsp;&nbsp;<img border="0" width="60px"
										height="60px" src="<%=_appPath %>/images/Admin-60.gif" alt="" />
				</td>
				<td width="70%" align="left" class="clsPageHeader" colspan="4" style="font-style: italic; vertical-align: middle">
									<h2 style="color:#1E90FF; vertical-align: middle">System Administration</h2>
				</td>
				<td width="10%" align="right" style="vertical-align: middle"><html:link action="/Home.do"
					style="color:Navy;text-decoration:none; font-size: 12px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<img align="right" style="vertical-align: middle" border=0 src=<html:rewrite page="/images/home16.png"/> />Home</html:link>
				</td>
				<td width="10%">&nbsp;</td>
			</tr>
			<tr>
				<td></td>
				<td colspan="5"><hr /></td>
				<td></td>
			</tr>
			
		</table>

		<table width="100%">
			<tr>
				<td width="10%"></td>
				<td width="10%"></td>
				<td width="27%"></td>
				<td width="5%"></td>
				<td width="10%"></td>
				<td width="28%"></td>
				<td width="10%"></td>
			</tr>


			<tr style="vertical-align: middle">
				<th></th>

				<th style="vertical-align: middle">
				<security:oscarSec objectName="<%=KeyConstants.FUN_ADMIN_USER %>" rights="<%=KeyConstants.ACCESS_READ%>">
				<html:link	action="/PMmodule/Admin/UserSearch.do">
					<img ID="lnkClient" src="<%=_appPath%>/images/Users-60.gif"
						Height="60" Width="60" border="0" />
				</html:link>
				</security:oscarSec>
				</th>
				<th align="left" style="vertical-align: middle">
				<table>
					<tr align="left">
						<th align="left" valign="top" class="clsHomePageLabels">
							<security:oscarSec objectName="<%=KeyConstants.FUN_ADMIN_USER %>" rights="<%=KeyConstants.ACCESS_READ%>">
								<html:link	action="/PMmodule/Admin/UserSearch.do">User Management</html:link>
							</security:oscarSec>
						</th>
					</tr>
				</table>
				</th>

				<th></th>

				<th style="vertical-align: middle">
					<security:oscarSec objectName="<%=KeyConstants.FUN_ADMIN_ROLE %>" rights="<%=KeyConstants.ACCESS_READ%>">
						<html:link	action="/PMmodule/Admin/RoleManager.do">
							<img ID="lnkCare1" src="<%=_appPath%>/images/Role-60.gif"
								Height="60" Width="60" border="0" style="vertical-align: middle" />
						</html:link>
					</security:oscarSec>
				</th>
				<th style="vertical-align: middle">
				<table align="left">
					<tr>
						<th align="left" valign="top" class="clsHomePageLabels" >
							<security:oscarSec objectName="<%=KeyConstants.FUN_ADMIN_ROLE %>" rights="<%=KeyConstants.ACCESS_READ%>">
								<html:link	action="/PMmodule/Admin/RoleManager.do">Role Management</html:link>
					    	</security:oscarSec>
					    </th>
					</tr>
				</table>
				</th>
				<th></th>
			</tr>

			<tr>
				<th></th>


				<th style="vertical-align: middle">
					<security:oscarSec objectName="<%=KeyConstants.FUN_ADMIN_LOOKUP %>" rights="<%=KeyConstants.ACCESS_READ%>">
						<a id="orgAdd"	href="<c:out value='${ctx}'/>/Lookup/LookupTableList.do"> <img
						ID="lnkCare1" src="<%=_appPath%>/images/Lookup-60.gif" Height="60"
						Width="60" border="0" style="vertical-align: middle"/></a>
					</security:oscarSec>	
				</th>
				<th style="vertical-align: middle">
				<table align="left">
					<tr>


						<th align="left" style="vertical-align: middle" class="clsHomePageLabels">
							<security:oscarSec objectName="<%=KeyConstants.FUN_ADMIN_LOOKUP %>" rights="<%=KeyConstants.ACCESS_READ%>">
								<a	id="orgAdd"	href="<c:out value='${ctx}'/>/Lookup/LookupTableList.do">
								Lookup Tables</a>
							</security:oscarSec>
						</th>
					</tr>
				</table>
				</th>
				<th></th>
				<th style="vertical-align: middle">
					<security:oscarSec objectName="<%=KeyConstants.FUN_ADMIN_ORG %>" rights="<%=KeyConstants.ACCESS_READ%>">
						<a id="orgAdd"	href="<c:out value='${ctx}'/>/PMmodule/Admin/ShowORGTree.do?tableId=ORG">
						<img ID="lnkClient" src="<%=_appPath%>/images/Organization2.png"				
							Height="60" Width="60" border="0" style="vertical-align: middle"/></a>
					</security:oscarSec>	
				</th>
				<th align="left" style="vertical-align: middle">
				<table>
					<tr align="left">
						<th align="left" style="vertical-align: middle" class="clsHomePageLabels">
							<security:oscarSec objectName="<%=KeyConstants.FUN_ADMIN_ORG %>" rights="<%=KeyConstants.ACCESS_READ%>">
								<a	id="orgAdd"	href="<c:out value='${ctx}'/>/PMmodule/Admin/ShowORGTree.do?tableId=ORG">
								SMIS Org Chart</a>
							</security:oscarSec>
						</th>
					</tr>
				</table>
				</th>
				<th></th>
			</tr>
			<tr>
				<th></th>
				<th style="vertical-align: middle">
					<security:oscarSec objectName="<%=KeyConstants.FUN_ADMIN_MERGECLIENT %>" rights="<%=KeyConstants.ACCESS_READ%>">
						<a id="lnkMergeClient"	href="<c:out value='${ctx}'/>/PMmodule/MergeClient.do"> 
						<img ID="lnkMereg1" src="<%=_appPath%>/images/ClientMerge.gif" Height="60"
						Width="60" border="0" style="vertical-align: middle"/></a>
					</security:oscarSec>		
				</th>
				<th style="vertical-align: middle">
				<table align="left">
					<tr>
						<th align="left" style="vertical-align: middle" class="clsHomePageLabels">
							<security:oscarSec objectName="<%=KeyConstants.FUN_ADMIN_MERGECLIENT %>" rights="<%=KeyConstants.ACCESS_READ%>">
							<a	id="lnkMerge2"	href="<c:out value='${ctx}'/>/PMmodule/MergeClient.do">
							Merge Client</a>
							</security:oscarSec>
						</th>
					</tr>
				</table>
				</th>
				<th></th>
				<th style="vertical-align: middle">
					<security:oscarSec objectName="<%=KeyConstants.FUN_ADMIN_SYSTEMMESSAGE %>" rights="<%=KeyConstants.ACCESS_READ%>">
					<a id="lnkSysMessage1"	href="<c:out value='${ctx}'/>/SystemMessage.do"> 
					<img ID="imgAddMessage" src="<%=_appPath%>/images/SystemMessages60.gif" Height="60"
					Width="60" border="0" style="vertical-align: middle"/></a>
					</security:oscarSec>
				</th>
				<th style="vertical-align: middle">
				<table align="left">
					<tr>
						<th align="left" style="vertical-align: middle" class="clsHomePageLabels">
						<security:oscarSec objectName="<%=KeyConstants.FUN_ADMIN_SYSTEMMESSAGE %>" rights="<%=KeyConstants.ACCESS_READ%>">
							<a	id="lnkAddMessage2"	href="<c:out value='${ctx}'/>/SystemMessage.do">
							System Message</a>
						</security:oscarSec>	
						</th>
					</tr>
				</table>
				</th>
			</tr>
			<tr>
				<th></th>
				<th style="vertical-align: middle">
					<security:oscarSec objectName="<%=KeyConstants.FUN_ADMIN_UNLOCKUSER %>" rights="<%=KeyConstants.ACCESS_READ%>">
						<a id="lnkUnlockAccount1"	href="<c:out value='${ctx}'/>/PMmodule/Admin/UnlockAccount.do"> 
						<img ID="imgUnlockAccount1" src="<%=_appPath%>/images/UnlockAccount.gif" Height="60"
						Width="60" border="0" style="vertical-align: middle"/></a>
					</security:oscarSec>		
				</th>
				<th style="vertical-align: middle">
				<table align="left">
					<tr>
						<th align="left" style="vertical-align: middle" class="clsHomePageLabels">
							<security:oscarSec objectName="<%=KeyConstants.FUN_ADMIN_UNLOCKUSER  %>" rights="<%=KeyConstants.ACCESS_READ%>">
							<a	id="lnkUnlockAccount2"	href="<c:out value='${ctx}'/>/PMmodule/Admin/UnlockAccount.do">
								Unlock User</a>
							</security:oscarSec>
						</th>
					</tr>
				</table>
				</th>
				<th></th>
				<th style="vertical-align: middle"></th>
				<th></th>
				</tr>
			
		</table>
		</div>
		</td>
	</tr>
</table>
