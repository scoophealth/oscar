<%
if(session.getAttribute("user") == null) response.sendRedirect("../../logout.jsp");
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title><bean:message key="admin.admin.uploadEntryTxt"/></title>
        <link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">
    </head>
    <body>
        <table class="MainTable">
            <tr class="MainTableTopRow">
                <td class="MainTableTopRowLeftColumn" width="175">&nbsp;</td>
                <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                <tr>
                    <td><bean:message key="admin.admin.uploadEntryTxt"/></td>
                    <td>&nbsp;</td>
                    <td style="text-align: right"><a
                        href="javascript:popupStart(300,400,'Help.jsp')"><bean:message
                        key="global.help" /></a> | <a
                        href="javascript:popupStart(300,400,'About.jsp')"><bean:message
    					key="global.about" /></a> | <a
        				href="javascript:popupStart(300,400,'License.jsp')"><bean:message
            			key="global.license" /></a></td>
                </tr>
                </table>
                </td>
            </tr>
            <tr>
                <td class="MainTableLeftColumn" valign="top">&nbsp;</td>
                <td class="MainTableRightColumn">
                    <html:form action="/admin/uploadEntryText" method="POST" enctype="multipart/form-data">
                        <input type="file" name="importFile"><br>
                        <input type="submit" value="<bean:message key="admin.admin.uploadEntryTxt"/>">
                    </html:form>
                </td>
            </tr>
            <% 
            Boolean error = (Boolean)request.getAttribute("error");                                
            if( error != null ) {
            %>
            <tr>
                <td class="MainTableLeftColumn" valign="top">&nbsp;</td>
                <td class="MainTableRightColumn">
                    <% if( error == true ) { %>
                    <span style="color:red;"><bean:message key="admin.admin.ErrorUploadEntryTxt"/></span>
                    <%}else {%>
                     <span style="color:green;"><bean:message key="admin.admin.SuccessUploadEntryTxt"/></span>
                    <%}%>
                </td>
            </tr>
            <%}%>
            <tr>
                <td class="MainTableBottomRowLeftColumn">&nbsp;</td>
                <td class="MainTableBottomRowRightColumn" valign="top">&nbsp;</td>
            </tr>
        </table>
    </body>
</html>
