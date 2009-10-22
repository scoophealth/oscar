<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page import="oscar.oscarRx.data.RxDrugData,java.util.*" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Calendar" %>
<%@page import="oscar.oscarRx.data.RxPrescriptionData" %>
<%@page import="oscar.oscarRx.util.RxUtil" %>
<%
System.out.println("***### IN prescribe.jsp");

List<RxPrescriptionData.Prescription> listRxDrugs=(List)request.getAttribute("listRxDrugs");

System.out.println("listRxDrugs="+listRxDrugs);

for(RxPrescriptionData.Prescription rx : listRxDrugs ){
         System.out.println("display prescribe"+rx);
         String rand            = Long.toString(rx.getRandomId());
         String instructions    = rx.getSpecial();
         String drugForm        = rx.getDrugForm();
         String startDate       = RxUtil.DateToString(rx.getRxDate(), "yyyy-MM-dd");
         String writtenDate     = RxUtil.DateToString(rx.getWrittenDate(), "yyyy-MM-dd");
         String lastRefillDate  = RxUtil.DateToString(rx.getLastRefillDate(), "yyyy-MM-dd");

         int patientCompliance  = rx.getPatientCompliance();
         String frequency       = rx.getFrequencyCode();
         String route           = rx.getRoute();
         String durationUnit    = rx.getDurationUnit();
         boolean prn            = rx.getPrn();
         String repeats         = Integer.toString(rx.getRepeat());
         String amount          = rx.getTakeMinString();
         boolean longTerm       = rx.getLongTerm();
         String outsideProvOhip = rx.getOutsideProviderOhip();
         String drugName        = rx.getBrandName();
         boolean pastMed        = rx.getPastMed();
         String quantity        = rx.getQuantity();
         String duration        = rx.getDuration();
         String method          = rx.getMethod();
         String outsideProvName = rx.getOutsideProviderName();
         
         System.out.println("instructions from repscbAllLongTerm="+instructions+ " rand="+rand+" drugName="+drugName+" startDate="+startDate+" writtenDate="+writtenDate);

%>

<fieldset style="margin-top:2px;width:600px;" id="set_<%=rand%>">
    <a href="javascript:void(0);" style="float:right;margin-left:5px;margin-top:0px;padding-top:0px;" onclick="$('set_<%=rand%>').remove();deletePrescribe('<%=rand%>');">X</a>
    <a href="javascript:void(0);" style="float:right;margin-top:0px;padding-top:0px;" onclick="$('rx_more_<%=rand%>').toggle();">more</a>

    <label style="float:left;width:80px;">Name:</label>
       <input type="text" id="drugName_<%=rand%>"     name="drugName_<%=rand%>"     value="<%=drugName%>"     size="30"/><br>
    <label style="float:left;width:80px;">Instructions:</label>
       <input type="text" id="instructions_<%=rand%>" name="instructions_<%=rand%>" value="<%=instructions%>" size="60" onblur="parseIntr(this);" /> <br>
    <label style="float:left;width:80px;">Quantity:</label>
       <input type="text" id="quantity_<%=rand%>"     name="quantity_<%=rand%>"     value="<%=quantity%>"/>
    <label style="">Repeats:</label>                            
       <input type="text" id="repeats_<%=rand%>"      name="repeats_<%=rand%>"      value="<%=repeats%>" />


       <input type="hidden" id="calQuantity_<%=rand%>" name="calQuantity_<%=rand%>" value="" />
       <input type="checkbox" id="longTerm_<%=rand%>"  name="longTerm_<%=rand%>" <%if(longTerm) {%> checked="true" <%}%> >Long Term Med </input>
       <div id="rxString_<%=rand%>"> </div>
       <div id="quantityWarning_<%=rand%>"> </div>
       <div id="rx_more_<%=rand%>" style="display:none;padding:2px;">
          <bean:message key="WriteScript.msgPrescribedByOutsideProvider"/>
          <input type="checkbox" id="ocheck_<%=rand%>"  onclick="$('otext_<%=rand%>').toggle();" />
          <div id="otext_<%=rand%>" style="display:none;padding:2px;" >
                <b><label style="float:left;width:80px;">Name :</label></b> <input type="text" id="outsideProviderName_<%=rand%>" name="outsideProviderName_<%=rand%>"  value="<%=outsideProvName%>"/>
                <b><label style="width:80px;">OHIP No:</label></b> <input type="text" id="outsideProviderOhip_<%=rand%>" name="outsideProviderOhip_<%=rand%>"  value="<%=outsideProvOhip%>"/>
          </div><br/>

        <bean:message key="WriteScript.msgPastMedication"/>
            <input  type="checkbox" name="pastMed_<%=rand%>" id="pastMed_<%=rand%>" <%if(pastMed) {%> checked="true" <%}%> />

	<bean:message key="WriteScript.msgPatientCompliance"/>:
          <bean:message key="WriteScript.msgYes"/>
            <input type="checkbox"  name="patientComplianceY_<%=rand%>" id="patientComplianceY_<%=rand%>" <%if(patientCompliance==1) {%> checked="true" <%}%> />
        
          <bean:message key="WriteScript.msgNo"/>
            <input type="checkbox"  name="patientComplianceN_<%=rand%>" id="patientComplianceN_<%=rand%>" <%if(patientCompliance==-1) {%> checked="true" <%}%> /><br/>



        <label style="float:left;width:80px;">Start Date:</label>
           <input type="text" id="rxDate_<%=rand%>" name="rxDate_<%=rand%>" value="<%=startDate%>"/>
	<label style="">Last Refill Date:</label>
           <input type="text" id="lastRefillDate_<%=rand%>"  name="lastRefillDate_<%=rand%>" value="<%=lastRefillDate%>" />
	<br/>
        <label style="float:left;width:80px;">Written Date:</label>
           <input type="text" id="writtenDate_<%=rand%>"  name="writtenDate_<%=rand%>" value="<%=writtenDate%>" />
           <a href="javascript:void(0);" style="float:right;margin-top:0px;padding-top:0px;" onclick="addFav('<%=rand%>','<%=drugName%>')">Add to Favorite</a>

    </div>

</fieldset>
   
        <script type="text/javascript">
            $('instructions_<%=rand%>').focus();
        </script>
 <%}%>
 
