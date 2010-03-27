<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>


<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>

<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%response.sendRedirect("logout.jsp");%>
</security:oscarSec>

<html:html locale="true">
    <head>
        <meta http-equiv="Cache-Control" content="no-cache" />
        <title>Edit Facility</title>
        <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/tigris.css" />' />
        <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/displaytag.css" />' />

        <script type="text/javascript"
            src="<%=request.getContextPath()%>/js/validation.js">
        </script>
        <script type="text/javascript">
            function validateForm()
            {
                if (bCancel) return bCancel;

                var isOk = false;
                isOk = validateRequiredField('facilityName', 'Facility Name', 32);
                if (isOk) isOk = validateRequiredField('facilityDesc', 'Facility Description', 70);
//                if (isOk) isOk = validateUpdateInterval();
                if (isOk) isOk = validateRemoveDemoId();
                return isOk;
            }

            function validateUpdateInterval()
            {
                var ret = false;
                var interval = document.forms[0].updateInterval.value;
                if (!isInteger(interval)) {
                    alert("Integrator Update Interval must be an integer!");
                } else if (parseInt(interval)<1) {
                    alert("Integrator Update Interval must be > 0");
                } else {
                    ret = true;
                }
                return ret;
            }

            function validateRemoveDemoId() {
                var ret = true;
                var rid = document.forms[0].removeDemographicIdentity.checked;
                if (!rid) {
                    ret = confirm("Remove Demographic Identity NOT checked! Is it OK?");
                }
                return ret;
            }
        </script>
        <!-- don't close in 1 statement, will break IE7 -->
        
    </head>
    <body>
        <h1>Edit Facility</h1>
        <html:form action="/FacilityManager.do"
            onsubmit="return validateForm();">
            <input type="hidden" name="method" value="save" />
            <html:hidden property="facility.orgId" />
            <html:hidden property="facility.sectorId" />
            <html:hidden property="facility.ocanServiceOrgNumber" />
            <table width="100%" border="1" cellspacing="2" cellpadding="3">
                <tr class="b">
                    <td>Facility Id:</td>
                    <td><c:out value="${requestScope.id}" /></td>
                </tr>
                <tr class="b">
                    <td>Name: *</td>
                    <td><html:text property="facility.name" size="32"
                           maxlength="32" styleId="facilityName" /></td>
                </tr>
                <tr class="b">
                    <td>Description: *</td>
                    <td><html:text property="facility.description" size="60"
                           maxlength="70" styleId="facilityDesc" /></td>
                </tr>
                <tr class="b">
                    <td>Enable Integrator:</td>
                    <td><html:checkbox property="facility.integratorEnabled" /></td>
                </tr>
                <tr class="b">
                    <td>Integrator Url:</td>
                    <td><html:text property="facility.integratorUrl" size="40" /></td>
                </tr>
                <tr class="b">
                    <td>Integrator User:</td>
                    <td><html:text property="facility.integratorUser" /></td>
                </tr>
                <tr class="b">
                    <td>Integrator Password:</td>
                    <td><html:password property="facility.integratorPassword" /></td>
                </tr>
                <tr class="b">
                    <td>Remove Demographic Identity:</td>
                    <td>
                        <html:checkbox property="removeDemographicIdentity" />
                        (All patients' names, hin# & sin# will be removed in Integrator)
                        <br>
                    </td>
<!--Ronnie
                </tr>
                <tr class="b">
                    <td>Integrator Update Interval:</td>
                    <td>
                        <html:text property="updateInterval" size="3" />
                        Hour(s)
                        <br>
                    </td>
                </tr>
-->
                <tr>
                    <td colspan="2">* Mandatory fields</td>
                <tr>
                    <td colspan="2"><html:submit property="submit.save" onclick="bCancel=false;">Save</html:submit>
                        <html:cancel>Cancel</html:cancel></td>
                </tr>
            </table>
        </html:form>
    </body>
</html:html>