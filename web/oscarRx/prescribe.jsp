<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page import="oscar.oscarRx.data.RxDrugData,java.util.*" %>
<%
RxDrugData drugData = new RxDrugData();
RxDrugData.DrugSearch drugSearch = null;

String id = request.getParameter("id");
String text = request.getParameter("text");
String rand = request.getParameter("rand");

System.out.println("id "+id+ " text "+text);
String drugName = "";

if(id != null && id.startsWith("b_")){
    String sId= id.replaceAll("b_", "");
    drugName= drugData.getGenericName(sId);

}else if(id != null && id.startsWith("g_")){
    String sId= id.replaceAll("g_", "");
    drugName = text;

}


%>
<fieldset style="margin-top:2px;width:600px;">
   
    <a href="javascript:void();" style="float:right;margin-top:0px;padding-top:0px;" onclick="$('rx_more_<%=rand%>').toggle();">more</a>
    
    <label style="float:left;width:100px;">Name:</label> <input type="text"  size="30" name="drugName_<%=rand%>" value="<%=drugName%>"/><br>
    <label style="float:left;width:100px;">Instructions:</label> <input type="text" id="instructions_<%=rand%>" name="instructions_<%=rand%>" size="60"/> <br>
    <label style="float:left;width:100px;">Quantity:</label> <input type="text" value="" name="quantity_<%=rand%>"/>
    <label>Repeats:</label> <input type="text" name="repeats_<%=rand%>" />
    <input type="checkbox" name="longTerm_<%=rand%>">Long Term Med </input>

    <div id="rx_more_<%=rand%>" style="display:none;padding:2px;">
        <bean:message key="WriteScript.startDate"/>:<input type="text" name="rxDate" />
        <bean:message key="WriteScript.msgPrescribedByOutsideProvider"/>
        <input type="checkbox" id="ocheck" onclick="javascript:showHideOutsideProvider();" />
        <span id="otext">
            <b><bean:message key="WriteScript.msgName"/>:</b> <input type="text" name="outsideProviderName" />
            <b><bean:message key="WriteScript.msgOHIPNO"/>:</b> <input type="text" name="outsideProviderOhip" />
        </span>
        <bean:message key="WriteScript.msgLongTermMedication"/>:<html:checkbox property="longTerm" onchange="javascript:writeScriptDisplay();" />
        <bean:message key="WriteScript.msgPastMedication"/>:<html:checkbox property="pastMed" onchange="javascript:writeScriptDisplay();" />
	<bean:message key="WriteScript.msgPatientCompliance"/>:
        <bean:message key="WriteScript.msgYes"/><html:checkbox property="patientComplianceY" onchange="javascript:checkPatientCompliance('Y');" />
        <bean:message key="WriteScript.msgNo"/><html:checkbox property="patientComplianceN" onchange="javascript:checkPatientCompliance('N');" />
	<bean:message key="WriteScript.msgLastRefillDate"/>:<input type="text" name="lastRefillDate" onfocus="javascript:lastRefillDate.value='';" />
	<bean:message key="WriteScript.msgRxWrittenDate"/>: <input type="text" name="writtenDate" />
    </div>

</fieldset>
        <script type="text/javascript">
            $('instructions_<%=rand%>').focus();
            </script>