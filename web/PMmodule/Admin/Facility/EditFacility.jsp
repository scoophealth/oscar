<%@ include file="/taglibs.jsp"%>

<script>
    function ConfirmDelete(name)
    {
        if(confirm("Are you sure you want to delete " + name + " ?")) {
            return true;
        }
        return false;
    }
</script>
<%@ include file="/common/messages.jsp"%>
<div class="tabs" id="tabs">
    <table cellpadding="3" cellspacing="0" border="0">
        <tr>
            <th title="Facility">Edit / save facility for agency "<c:out value="${agency.name}"/>"</th>
        </tr>
    </table>
</div>

<html:form action="/PMmodule/FacilityManager.do">
    <html:hidden property="agencyId"/>
    <input type="hidden" name="method" value="save" />
    <table width="100%" border="1" cellspacing="2" cellpadding="3">
        <tr class="b">
            <td width="20%">Facility Id:</td>
            <td><c:out value="${requestScope.id}" /></td>
        </tr>
        <tr class="b">
            <td width="20%">Name:</td>
            <td><html:text property="facility.name" size="32" maxlength="32"/></td>
        </tr>
        <tr class="b">
            <td width="20%">Description:</td>
            <td><html:text property="facility.description" size="70" maxlength="70"/></td>
        </tr>
        <tr>
            <td colspan="2">
                <html:submit property="submit.save">Save</html:submit>
                <html:cancel property="submit.cancel">Cancel</html:cancel>
            </td>
        </tr>
    </table>
</html:form>
<div>
    <p><a href="<html:rewrite action="/PMmodule/FacilityManager.do"/>?method=list&agencyId=<c:out value="${agency.id}"/>" />Return to facilities list</a></p>
</div>
