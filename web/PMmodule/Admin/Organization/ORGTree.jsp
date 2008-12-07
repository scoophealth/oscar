<!-- Source:web/PMmodule/Admin/Organization/ProgramView/ORGTree.jsp -->
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ include file="../../../taglibs.jsp"%>
<%@page import="com.quatro.common.KeyConstants;"%>


<script type="text/javascript" src='<c:out value="${ctx}"/>/js/menuExpandable.js'></script>
<style type="text/css">
    @import "<html:rewrite page="/css/menuExpandable.css" />";
</style>



<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />
<bean:define id="tree" name="lookupTreeForm" property="tree"
	type="net.sf.navigator.menu.MenuRepository" />

<table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
	<tr>
		<th class="pageTitle" align="center"><span
			id="_ctl0_phBody_lblTitle" align="left">Organization Chart</span></th>
	</tr>
	<tr>
		<td align="left" class="buttonBar2"><html:link
			action="/PMmodule/Admin/SysAdmin.do"
			style="color:Navy;text-decoration:none;">
			<img border=0 src=<html:rewrite page="/images/close16.png"/> />&nbsp;Close&nbsp;&nbsp;|</html:link>
		</td>
	</tr>
	<tr>
		<td align="left"></td>
	</tr>
	<tr>
		<td height="100%">
		<div
			style="color: Black; background-color: White; border-width: 1px; border-style: Ridge;
                    height: 100%; width: 100%; overflow: auto;" id="scrollBar">

		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td align="left" class="clsHomePageHeader" colspan="5">
				<h2>Org Chart</h2>
				</td>
			</tr>

		</table>

		<table width="100%" border="0">
			<tr>
				<td colspan="2">
					<security:oscarSec objectName="<%=KeyConstants.FUN_ADMIN_ORG %>" rights="<%=KeyConstants.ACCESS_READ%>">
						<menu:useMenuDisplayer name="ListMenu"
							repository="tree">
							<c:forEach var="menu" items="${tree.topMenus}">
								<menu-el:displayMenu name="${menu.name}" />
							</c:forEach>
						</menu:useMenuDisplayer>
					</security:oscarSec>
				</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>
<script>
	initializeMenus();
</script>