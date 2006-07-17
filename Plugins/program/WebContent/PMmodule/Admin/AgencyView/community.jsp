<%@ include file="/taglibs.jsp" %>

	<br/>
	<%@ include file="/messages.jsp"%>
	<br/>

	<c:choose>
		<c:when test="${requestScope.integrator_registered == false}">
			<span style="color:red">&lt;This feature is only available to CAISI systems registered with an integrator&gt;</span>
		</c:when>
		<c:otherwise>
			
		<div class="tabs" id="tabs">
			<table cellpadding="3" cellspacing="0" border="0">
		
				<tr>
					<th title="Programs">Agencies</th>
		
				</tr>
			</table>
		</div>
		
		<display:table class="b" border="1" cellspacing="2" cellpadding="3" width="100%" id="agency" name="agencies" export="false" pagesize="0" requestURI="/PMmodule/AgencyManager.do">
		  <display:setProperty name="paging.banner.placement" value="bottom"/>
		  <display:setProperty name="paging.banner.item_name" value="agency"/>
		  <display:setProperty name="paging.banner.items_name" value="agencies"/>
		  <display:setProperty name="basic.msg.empty_list" value="No agencies found."/>
		  
		  <display:column property="id"  sortable="true" title="Id" />
		  <display:column property="name"  sortable="true" title="Name" />
		  <display:column property="descr"  sortable="true" title="Description" />
		  <display:column sortable="true" title="Contact Info">
		  	<c:out value="${agency.contactName}"/><br/>
		  	<c:out value="${agency.contactEmail }"/><br/>
		  	<c:out value="${agency.contactPhone }"/>
		  </display:column>
		</display:table>
		
		</c:otherwise>
	</c:choose>
