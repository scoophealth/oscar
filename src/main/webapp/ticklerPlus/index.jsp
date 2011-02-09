<%@ page errorPage="/ticklerPlus/Error.jsp"%>

<jsp:forward page="/Tickler.do">
	<jsp:param name="method" value="filter" />
</jsp:forward>