<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<logic:iterate id="comp" collection="<%= org.caisi.comp.FrameworkFactory.getFramework().getAllComponents() %>">
Component: <bean:write name="comp" property="description"/><BR>
Version: <bean:write name="comp" property="version"/><BR>
<HR>
</logic:iterate>