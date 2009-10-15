<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page import="oscar.oscarRx.data.RxDrugData,java.util.*" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Calendar" %>
<%@page import="oscar.oscarRx.data.RxPrescriptionData" %>
<%@page import="oscar.oscarRx.util.RxUtil" %>
<%
System.out.println("***### IN prescribe.jsp");

Enumeration em=request.getAttributeNames();
while(em.hasMoreElements()){
    System.out.println("in prescribe.jsp attr="+em.nextElement());
}


List<RxPrescriptionData.Prescription> listRxDrugs=(List)request.getAttribute("listRxDrugs");



System.out.println("listRxDrugs="+listRxDrugs);

for(RxPrescriptionData.Prescription rx : listRxDrugs ){
                 System.out.println("display prescribe"+rx);
            String rand="";
            String drugForm="";
            String startDate="";
            int patientCompliance=0;
            String instructions="";
            String frequency="";
            String route="";
            String durationUnit="";
            boolean prn=false;
            String repeats="";
            String writtenDate="";
            String amount="";
            boolean longTerm=false;
            String outsideProvOhip="";
            String drugName="";
            boolean pastMed=false;
            String lastRefillDate="";
            String quantity="";
            String duration="";
            String method="";
            String outsideProvName="";




             rand           =Long.toString(rx.getRandomId());
             instructions   = rx.getSpecial();
             drugForm       = rx.getDrugForm();
             startDate      = RxUtil.DateToString(rx.getRxDate(), "yyyy-MM-dd");
             patientCompliance      = rx.getPatientCompliance();
             frequency      = rx.getFrequencyCode();
             route          =rx.getRoute();
             durationUnit   =rx.getDurationUnit();
             prn            =rx.getPrn();
             repeats        =Integer.toString(rx.getRepeat());
             writtenDate    =RxUtil.DateToString(rx.getWrittenDate(), "yyyy-MM-dd");
             amount         =rx.getTakeMinString();
             longTerm       =rx.getLongTerm();
             outsideProvOhip    =rx.getOutsideProviderOhip();
             drugName       =rx.getGenericName();
             pastMed        =rx.getPastMed();
             lastRefillDate = RxUtil.DateToString(rx.getLastRefillDate(), "yyyy-MM-dd");
             quantity       = rx.getQuantity();
             duration       = rx.getDuration();
             method         = rx.getMethod();
             outsideProvName    = rx.getOutsideProviderName();
             System.out.println("instructions from repscbAllLongTerm="+instructions);
             System.out.println("rand="+rand);
             System.out.println("drugName="+drugName);
             System.out.println("startDate="+startDate);
             System.out.println("writtenDate="+writtenDate);

%>

<fieldset style="margin-top:2px;width:600px;" id="set_<%=rand%>"">
          <a href="javascript:void();" style="float:right;margin-left:5px;margin-top:0px;padding-top:0px;" onclick="$('set_<%=rand%>').remove();">X</a>

    <a href="javascript:void();" style="float:right;margin-top:0px;padding-top:0px;" onclick="$('rx_more_<%=rand%>').toggle();">more</a>

    <label style="float:left;width:80px;">Name:</label> <input type="text"  size="30" name="drugName_<%=rand%>" id="drugName_<%=rand%>"  value="<%=drugName%>"/><br>
    <%--    <label style="float:left;width:80px;">Instructions:</label> <input type="text" id="instructions_<%=rand%>" name="instructions_<%=rand%>" value="" onblur="parseIntr(this);" size="60"/> <br>
        <label style="float:left;width:80px;">Quantity:</label> <input type="text" id="quantity_<%=rand%>" value="" name="quantity_<%=rand%>"/>
        <label style="">Repeats:</label> <input type="text" id="repeats_<%=rand%>" name="repeats_<%=rand%>" /> --%>

   
        <label style="float:left;width:80px;">Instructions:</label> <input type="text" id="instructions_<%=rand%>" name="instructions_<%=rand%>" value="<%=instructions%>" onblur="parseIntr(this);" size="60"/> <br>
        <label style="float:left;width:80px;">Quantity:</label> <input type="text" id="quantity_<%=rand%>" value="<%=quantity%>" name="quantity_<%=rand%>"/>
        <label style="">Repeats:</label> <input type="text" id="repeats_<%=rand%>" name="repeats_<%=rand%>" value="<%=repeats%>" />

        <input type="hidden" id="calQuantity_<%=rand%>" name="calQuantity_<%=rand%>" value="" />
        <input type="checkbox" id="longTerm_<%=rand%>" name="longTerm_<%=rand%>" <%if(longTerm) {%> checked="true" <%}%> >Long Term Med </input>
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
            <b><label style="float:left;width:80px;">Name :</label></b> <input type="text" id="outsideProviderName_<%=rand%>" name="outsideProviderName_<%=rand%>"  value="<%=outsideProvName%>"/>
            <b><label style="width:80px;">OHIP No:</label></b> <input type="text" id="outsideProviderOhip_<%=rand%>" name="outsideProviderOhip_<%=rand%>"  value="<%=outsideProvOhip%>"/>
        </div><br/>

        <bean:message key="WriteScript.msgPastMedication"/><input  type="checkbox" name="pastMed_<%=rand%>" id="pastMed_<%=rand%>" <%if(pastMed) {%> checked="true" <%}%> />

	<bean:message key="WriteScript.msgPatientCompliance"/>:
        <bean:message key="WriteScript.msgYes"/><input type="checkbox"  name="patientComplianceY_<%=rand%>" id="patientComplianceY_<%=rand%>" <%if(patientCompliance==1) {%> checked="true" <%}%> />
        
        <bean:message key="WriteScript.msgNo"/><input type="checkbox"  name="patientComplianceN_<%=rand%>" id="patientComplianceN_<%=rand%>" <%if(patientCompliance==-1) {%> checked="true" <%}%> /><br/>



        <label style="float:left;width:80px;">Start Date:</label><input type="text" id="rxDate_<%=rand%>" name="rxDate_<%=rand%>" value="<%=startDate%>"/>

	<label style="">Last Refill Date:</label><input type="text" id="lastRefillDate_<%=rand%>"  name="lastRefillDate_<%=rand%>" value="<%=lastRefillDate%>" />
	<br/>
        <label style="float:left;width:80px;">Written Date:</label><input type="text" id="writtenDate_<%=rand%>"  name="writtenDate_<%=rand%>" value="<%=writtenDate%>" />

    </div>

</fieldset>
   
        <script type="text/javascript">

            $('instructions_<%=rand%>').focus();
        </script>
 <%}%>
 
