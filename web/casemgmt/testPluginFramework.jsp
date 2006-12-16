<%org.springframework.web.context.WebApplicationContext ctx=
	org.springframework.web.context.support.WebApplicationContextUtils.
		getWebApplicationContext(config.getServletContext());

orgorg.oscarehr.PMmodule.service.ProgramManager programManager= 
	(org.oscarehr.PMmodule.service.ProgramManager)ctx.getBean("programManagerTest");
 

java.util.List list = programManager.getAllPrograms();%><%=list.size()%><%


new org.apache.struts.action.ActionForward("/PMmodule/Forms/SurveyExecute.do?method=survey&formId=aaa&clientId=1").toString();
%>ok<%
%>