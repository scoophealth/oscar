<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/quatro-tag.tld" prefix="quatro" %>

<bean:define id="lstExportFormat" name="quatroReportRunnerForm" property="exportFormatList"/>
<bean:define id="lstReportOption" name="quatroReportRunnerForm" property="reportOptionList"/>
<bean:define id="lstOrgSelection" name="quatroReportRunnerForm" property="orgSelectionList"/>
<bean:define id="arRelations" name="quatroReportRunnerForm" property="relations"/>
<bean:define id="filterFields" name="quatroReportRunnerForm" property="filterFields"/>
<bean:define id="operatorList" name="quatroReportRunnerForm" property="operatorList"/>

<bean:define id="reportTitle" name="quatroReportRunnerForm" property="reportTitle"/>
<bean:define id="lblDateRange" name="quatroReportRunnerForm" property="lblDateRange"/>
<bean:define id="lblStartDate" name="quatroReportRunnerForm" property="lblStartDate"/>
<bean:define id="lblEndDate" name="quatroReportRunnerForm" property="lblEndDate"/>
<bean:define id="startDateProperty" name="quatroReportRunnerForm" property="startDateProperty" type="com.quatro.util.HTMLPropertyBean" />
<bean:define id="endDateProperty" name="quatroReportRunnerForm" property="endDateProperty" type="com.quatro.util.HTMLPropertyBean" />
<bean:define id="startTxtProperty" name="quatroReportRunnerForm" property="startTxtProperty" type="com.quatro.util.HTMLPropertyBean" />
<bean:define id="endTxtProperty" name="quatroReportRunnerForm" property="endTxtProperty" type="com.quatro.util.HTMLPropertyBean" />
<bean:define id="orgSelectionProperty" name="quatroReportRunnerForm" property="orgSelectionProperty" type="com.quatro.util.HTMLPropertyBean" />

<link rel="stylesheet" href="../style/style.css" type="text/css" />
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<script type="text/javascript" src='<c:out value="${ctx}"/>/js/quatroReport.js'></script>
<script type="text/javascript" src='<c:out value="${ctx}"/>/js/quatroLookup.js'></script>

<logic:equal name="quatroReportRunnerForm" property="strClientJavascript" value="showReport">
<script language="JavaScript">
showReport('<c:out value="${ctx}"/>/QuatroReport/ReportViewer.do');
</script>
</logic:equal>
<script language="JavaScript">
function CriteriaChanged(obj){
  quatroReportRunnerForm.onCriteriaChange.value=obj.name;
  quatroReportRunnerForm.submit();
}

function showOrgLookup(tId) {
    var queryString = "?tId=" + tId;
    var lookupURL="<c:out value="${ctx}"/>/PMmodule/reports/Lookup.jsp" + queryString;
	top.childWin = window.open(lookupURL,"_blank","resizable=yes,scrollbars=yes,width=600,height=450,top=120, left=200");
	top.childWin.focus();
}
</script>

<html:form action="/QuatroReport/ReportRunner.do">
<table width="100%">
<tr><td style="color: white;font-family:Tahoma,Verdana,Arial;font-size: 14px;padding-left:8px;padding-right:8px;padding-top:4px;padding-bottom:4px; font-weight: bold" background="../images/TitleBar2.png" align="center">
<bean:write name="reportTitle"/>
</td></tr>

<tr><td style="font-family:Tahoma,Verdana,Arial;font-size: 14px;padding-left:8px;padding-right:8px;padding-top:4px;padding-bottom:4px;" background="../images/ButtonBar2.png"  align="left">
<img src="../images/Print16x16.gif"/>
<html:submit property="Run"
				style="margin-left: 0px; margin-right: 0px; background-color: transparent; margin-bottom: 0px; margin-top: 0px">Run Report</html:submit>&nbsp;|&nbsp;
<img src="../images/Back16.png"/>&nbsp;<html:link action="/QuatroReport/ReportList.do">QuatroShelter Reports</html:link>
</td></tr>

<tr><td valign="top">

<table width="100%">
  <tr>
    <td class="clsTdBackGround" width="50%">
      <html:hidden property="reportNo" />
      <table width="100%">
        <tr>
           <td class="clsNameLabels"  colspan="3" width="100%"><bean:write name="lblDateRange"/></td>
        </tr>

        <tr>
           <td width="10%">&nbsp;</td>
           <td class="clsNameLabels" width="20%"><bean:write name="lblStartDate"/></td>
           <td width="70%">
           <logic:equal name="quatroReportRunnerForm" property="startDateProperty.visible" value="visibility:visible;">
             <quatro:datePickerTag property="startDate" width="70%" style="<%=startDateProperty.getVisible()%>" openerForm="quatroReportRunnerForm"></quatro:datePickerTag>
           </logic:equal>
           <logic:equal name="quatroReportRunnerForm" property="startTxtProperty.visible" value="visibility:visible;">
             <html:text property="startTxt" style="<%=startTxtProperty.getVisible()%>"/></td>
           </logic:equal>
        </tr>
        <tr>
           <td width="10%">&nbsp;</td>
           <td class="clsNameLabels" width="20%"><bean:write name="lblEndDate"/></td>
           <td width="70%">
           <logic:equal name="quatroReportRunnerForm" property="endDateProperty.visible" value="visibility:visible;">
             <quatro:datePickerTag property="endDate" width="70%" style="<%=endDateProperty.getVisible()%>" openerForm="quatroReportRunnerForm"></quatro:datePickerTag>
           </logic:equal>
           <logic:equal name="quatroReportRunnerForm" property="endTxtProperty.visible" value="visibility:visible;">
             <html:text property="endTxt" style="<%=endTxtProperty.getVisible()%>"></html:text></td>
           </logic:equal>
        </tr>

      </table>
    </td>
    <td class="clsTdBackGround" width="50%">
      <table width="100%">
        <tr>
           <td class="clsNameLabels" width="100%">OUTPUT FORMAT</td>
        </tr>
        <tr>
           <td width="100%"><html:select property="exportFormat">
           <html:options collection="lstExportFormat" property="key" labelProperty="value"></html:options>
           </html:select></td>
        </tr>
      </table>
    </td>
  </tr>  
  <tr>
    <td width="50%">
      <table width="100%" >
        <tr>
           <td class="clsMenuLables" valign="top" width="50%">ORGANIZATION</td>
        </tr>
        <tr>
           <td><html:select property="lstOrg" multiple="true" size="5" style="width:90%;">
              <html:options collection="lstOrgSelection" property="key" labelProperty="value"></html:options>
           </html:select></td>
        </tr>
        <tr>
           <td class="clsButtonBarText">
               <div style="<%=orgSelectionProperty.getVisible()%>">
               &nbsp;&nbsp;<a id="orgAdd" href="javascript:showOrgLookup('ORG')">Add</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;
                    <a href="javascript:removeSel('lstOrg')">Remove</a>
                </div>
           </td>
        </tr>
      </table>
    </td>
    <td width="50%">
      <table width="100%" >
        <tr>
          <td class="clsMenuLables" valign="top">REPORT OPTIONS</td>
        </tr>
        <tr>
          <td>
             <html:select property="reportOption"  multiple="true" size="5" style="width:90%;">
               <html:options collection="lstReportOption" property="optionNo" labelProperty="optionDescription"></html:options>
             </html:select>
          </td>
        </tr>
        <tr>
          <td>&nbsp;</td>
        </tr>
      </table>
        </td>
    </tr> 
    <tr>
        <td colspan="2" width="100%">
           <table width="100%">
             <tr>
               <td class="clsNameLabels" width="100%">ADDITIONAL CRITERIA</td></tr>   	
             <tr>
		       <td class="clsDataGridList" width="100%">
	           </td>
	         </tr>
	         <tr>
                <td class="clsButtonBarText" width="100%">
                    &nbsp;&nbsp;<html:submit property="AddTplCri">Add</html:submit>&nbsp;&nbsp;&nbsp;|
                        &nbsp;&nbsp;<html:submit property="InsertTplCri">Insert</html:submit>&nbsp;&nbsp;&nbsp;|
                        &nbsp;&nbsp;<html:submit property="RemoveTplCri">Remove</html:submit>
	                </td>     
	            </tr>
<tr><td>
<TABLE align="center" class="simple" width="100%"><!--  722px-->
<thead>
   	<TR><th class="sortable">Select</th>
    	<th class="sortable">Relation<input type="hidden" id="onCriteriaChange" name="onCriteriaChange" value=""  /></TH>
		<TH class="sortable">Field Name</TH>
      	<TH class="sortable">Operator</TH>
		<TH class="sortable">Value(s)</TH></TR></thead>

	<logic:iterate id="tplCriteria" name="quatroReportRunnerForm" property="templateCriteriaList" indexId="rIndex">
	<TR><TD align="center">
        <logic:equal name="tplCriteria" property="required" value="true">
     	  <input type="checkbox" disabled="disabled" name="p<%=String.valueOf(rIndex)%>" value="<%=String.valueOf(rIndex)%>" /> 
        </logic:equal>
        <logic:notEqual name="tplCriteria" property="required" value="true">
     	  <input type="checkbox" name="p<%=String.valueOf(rIndex)%>" value="<%=String.valueOf(rIndex)%>" /> 
        </logic:notEqual>
     	<input type="hidden" name="lineno" value="<%=String.valueOf(rIndex)%>" /> 
   	</TD>
   	<TD>
		<html:select name="tplCriteria" property="relation" indexed="true" onchange="CriteriaChanged(this);">
          <logic:iterate id="relation" name="arRelations" type="String">
            <html:option value="<%=relation%>"><%=relation%></html:option>
          </logic:iterate>
		</html:select>
	</TD>  
	<TD>
        <logic:equal name="tplCriteria" property="required" value="true">
		<html:select name="tplCriteria" disabled="true" property="fieldNo" indexed="true" onchange="CriteriaChanged(this);">
           <option value="0"></option>
           <html:options collection="filterFields" property="fieldNo" labelProperty="fieldName"></html:options>
		</html:select>
        </logic:equal>
        <logic:notEqual name="tplCriteria" property="required" value="true">
		<html:select name="tplCriteria" property="fieldNo" indexed="true" onchange="CriteriaChanged(this);">
           <option value="0"></option>
           <html:options collection="filterFields" property="fieldNo" labelProperty="fieldName"></html:options>
		</html:select>
        </logic:notEqual>
	</TD>  
	<TD>
        <logic:equal name="tplCriteria" property="required" value="true">
		<html:select name="tplCriteria" disabled="true" property="op" indexed="true" onchange="CriteriaChanged(this);">
           <option value=""></option>
           <logic:iterate id="ops" name="tplCriteria" property="operatorList" type="com.quatro.util.KeyValueBean">
              <html:option value="<%=(String)ops.getKey()%>"><bean:write name="ops" property="value" /></html:option>
           </logic:iterate>
		</html:select>
        </logic:equal>
        <logic:notEqual name="tplCriteria" property="required" value="true">
		<html:select name="tplCriteria" property="op" indexed="true" onchange="CriteriaChanged(this);">
           <option value=""></option>
           <logic:iterate id="ops" name="tplCriteria" property="operatorList" type="com.quatro.util.KeyValueBean">
              <html:option value="<%=(String)ops.getKey()%>"><bean:write name="ops" property="value" /></html:option>
           </logic:iterate>
		</html:select>
        </logic:notEqual>
	</TD>  
	<TD>
		<html:hidden name="tplCriteria" property="lookupTable" indexed="true" />
        <quatro:lookupTag name="tplCriteria" tableName="BED" indexed="true" formProperty="quatroReportRunnerForm" codeProperty ="val" bodyProperty="valdesc" codeValue="123456" bodyValue="asdfgh"></quatro:lookupTag>
        <logic:notEqual name="tplCriteria" property="lookupTable" value="">
		  <img src="microsoftsearch.gif" onclick="showLookupx(<%=String.valueOf(rIndex)%>);">
        </logic:notEqual>
	</TD>  
   	</TR>
   	</logic:iterate>
</TABLE>
</td></tr>

            </table>
        </td>
    </tr>	
</table>


</td></tr>
</table>
</html:form>
