<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page import="oscar.oscarRx.data.RxDrugData,java.util.*" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Calendar" %>
<%
System.out.println("***### IN prescribe.jsp");
RxDrugData drugData = new RxDrugData();
RxDrugData.DrugSearch drugSearch = null;
Enumeration em=request.getParameterNames();
System.out.println("size of em");
while(em.hasMoreElements()){
    System.out.println("in prescribe.jsp attr="+em.nextElement());
}
String id = request.getParameter("id");
String text = request.getParameter("text");
String rand = request.getParameter("rand");
String notRePrescribe=request.getParameter("notRePrescribe");
String countPrescribe=request.getParameter("countPrescribe");
System.out.println("notRePrescribe in prescribe.jsp="+notRePrescribe);
System.out.println("countPrescribe in prescribe.jsp="+countPrescribe);
String today=null;
Calendar calendar = Calendar.getInstance();
SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            today = dateFormat.format(calendar.getTime()) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
System.out.println("id "+id+ " text "+text);
System.out.println("rand="+rand);

String drugName = "";

if(id != null && id.startsWith("b_")){
    String sId= id.replaceAll("b_", "");
    drugName= drugData.getGenericName(sId);

}else if(id != null && id.startsWith("g_")){
    String sId= id.replaceAll("g_", "");
    drugName = text;

}else if (id !=null){
    drugName=text;
}
System.out.println("drugName="+drugName);
System.out.println("id="+id);
%>

<fieldset style="margin-top:2px;width:600px;">
   
    <a href="javascript:void();" style="float:right;margin-top:0px;padding-top:0px;" onclick="$('rx_more_<%=rand%>').toggle();">more</a>
    <input type="hidden" id="whichPrescribe_<%=rand%>" value="<%=countPrescribe%>"/>

    <label style="float:left;width:80px;">Name:</label> <input type="text"  size="30" name="drugName_<%=rand%>" id="drugName_<%=rand%>"  <% if(notRePrescribe!=null && notRePrescribe.equals("true")){%> 
                                                               onblur="createNewRx(this);" value="<%=drugName%>" <%}else {%> value="<%=drugName%>"<%}%>/><br>
        <label style="float:left;width:80px;">Instructions:</label> <input type="text" id="instructions_<%=rand%>" name="instructions_<%=rand%>" value="" onblur="parseIntr(this);" size="60"/> <br>
        <label style="float:left;width:80px;">Quantity:</label> <input type="text" id="quantity_<%=rand%>" value="" name="quantity_<%=rand%>"/>
        <label style="">Repeats:</label> <input type="text" id="repeats_<%=rand%>" name="repeats_<%=rand%>" />
        <input type="hidden" id="calQuantity_<%=rand%>" name="calQuantity_<%=rand%>" value="" />
        <input type="checkbox" id="longTerm_<%=rand%>" name="longTerm_<%=rand%>">Long Term Med </input>
        <input type="button" id="update_<%=rand%>" name="update_<%=rand%>" onclick="updateDrug(this,$(whichPrescribe_<%=rand%>).value);return false;" value="Update"</input>
        <div id="rxString_<%=rand%>"> </div>
        <div id="quantityWarning_<%=rand%>"> </div>
        
     <%--   <div id="intr_parse_<%=rand%>" style="display:none;padding:2px;" >
                <input type="text" style="border:0;" size="60" id="rxString_<%=rand%>" name="rxString_<%=rand%>"  value="" />
            <bean:message key="WriteScript.method"/>:<input type="text" id="rxMethod_<%=rand%>" name="rxMethod_<%=rand%>" value="" />
            <bean:message key="WriteScript.route"/>:<input type="text" id="rxRoute_<%=rand%>" name="rxRoute_<%=rand%>" value=""/>
            <bean:message key="WriteScript.frequency"/>:<input type="text" id="rxFreq_<%=rand%>" name="rxFreq_<%=rand%>" value=""/>
            <bean:message key="WriteScript.drugForm"/>:<input type="text" id="rxDrugForm_<%=rand%>" name="rxDrugForm_<%=rand%>" value=""/>
            <bean:message key="WriteScript.duration"/>:<input type="text" id="rxDuration_<%=rand%>" name="rxDuration_<%=rand%>" value=""/>
            <bean:message key="WriteScript.durationUnit"/>:<input type="text" id="rxDurationUnit_<%=rand%>" name="rxDurationUnit_<%=rand%>" value=""/>
            <bean:message key="WriteScript.amount"/>:<input type="text" id="rxAmount_<%=rand%>" name="rxAmount_<%=rand%>" value=""/>
            <bean:message key="WriteScript.prn"/><%--:<html:checkbox property="pnr" onchange="javascript:writeScriptDisplay();" /> --%>
           <%-- <input type="checkbox" name="rxPRN_<%=rand%>" id="rxPRN_<%=rand%>" />--%>
            
        <!--/div-->
            <div id="rx_more_<%=rand%>" style="display:none;padding:2px;">

        <bean:message key="WriteScript.msgPrescribedByOutsideProvider"/>        
        <input type="checkbox" id="ocheck_<%=rand%>"  onclick="$('otext_<%=rand%>').toggle();" />       
        <div id="otext_<%=rand%>" style="display:none;padding:2px;" >
            <b><label style="float:left;width:80px;">Name :</label></b> <input type="text" id="outsideProviderName_<%=rand%>" name="outsideProviderName_<%=rand%>" />
            <b><label style="width:80px;">OHIP No:</label></b> <input type="text" id="outsideProviderOhip_<%=rand%>" name="outsideProviderOhip_<%=rand%>" />
        </div><br/>

        <bean:message key="WriteScript.msgPastMedication"/><input  type="checkbox" name="pastMed_<%=rand%>" id="pastMed_<%=rand%>"  />
	<bean:message key="WriteScript.msgPatientCompliance"/>:
        <bean:message key="WriteScript.msgYes"/><input type="checkbox"  name="patientComplianceY_<%=rand%>" id="patientComplianceY_<%=rand%>" />
        
        <bean:message key="WriteScript.msgNo"/><input type="checkbox"  name="patientComplianceN_<%=rand%>" id="patientComplianceN_<%=rand%>" /><br/>



        <label style="float:left;width:80px;">Start Date:</label><input type="text" id="rxDate_<%=rand%>" name="rxDate_<%=rand%>"
                <%if(id.startsWith("b_") ||id.startsWith("g_")){%>value="<%=today%>"<%} else {}%>/> 
	<label style="">Last Refill Date:</label><input type="text" id="lastRefillDate_<%=rand%>"  name="lastRefillDate_<%=rand%>" onfocus="javascript:lastRefillDate.value='';" />
	<br/>
        <label style="float:left;width:80px;">Written Date:</label><input type="text" id="writtenDate_<%=rand%>"  name="writtenDate_<%=rand%>"
                 <%if(id.startsWith("b_") ||id.startsWith("g_")){%>value="<%=today%>"<%} else {}%> />
    </div>

</fieldset>
        <script type="text/javascript">
            $('drugName_<%=rand%>').focus();
            $('instructions_<%=rand%>').focus();
        </script>
