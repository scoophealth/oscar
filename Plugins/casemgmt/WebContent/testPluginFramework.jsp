<%


org.springframework.web.context.WebApplicationContext ctx=
	org.springframework.web.context.support.WebApplicationContextUtils.
		getWebApplicationContext(config.getServletContext());

org.caisi.PMmodule.service.ProgramManager programManager= 
	(org.caisi.PMmodule.service.ProgramManager)ctx.getBean("programManagerTest");
 

java.util.List list = programManager.getAllPrograms();
%><%=list.size()%><%

%>