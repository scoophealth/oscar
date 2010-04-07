<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@page import="oscar.oscarRx.data.RxDrugData,java.util.*" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Calendar" %>
<%@page import="oscar.oscarRx.data.*" %>
<%@page import="oscar.oscarRx.util.*" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
    <%
System.out.println("***### IN prescribe.jsp");

List<RxPrescriptionData.Prescription> listRxDrugs=(List)request.getAttribute("listRxDrugs");
oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean)request.getSession().getAttribute("RxSessionBean");

System.out.println("listRxDrugs="+listRxDrugs);
if(listRxDrugs!=null){
            String specStr=RxUtil.getSpecialInstructions();

  for(RxPrescriptionData.Prescription rx : listRxDrugs ){
         System.out.println("display prescribe :"+rx);
         String rand            = Long.toString(rx.getRandomId());
         String instructions    = rx.getSpecial();
         String specialInstruction=rx.getSpecialInstruction();
         String drugForm        = rx.getDrugForm();
         String startDate       = RxUtil.DateToString(rx.getRxDate(), "yyyy-MM-dd");
         String writtenDate     = RxUtil.DateToString(rx.getWrittenDate(), "yyyy-MM-dd");
         String lastRefillDate  = RxUtil.DateToString(rx.getLastRefillDate(), "yyyy-MM-dd");
         int gcn=rx.getGCN_SEQNO();//if gcn is 0, rx is customed drug.
         String customName      = rx.getCustomName();
         int patientCompliance  = rx.getPatientCompliance();
         String frequency       = rx.getFrequencyCode();
         String route           = rx.getRoute();
         String durationUnit    = rx.getDurationUnit();
         boolean prn            = rx.getPrn();
         String repeats         = Integer.toString(rx.getRepeat());
         String takeMin         = rx.getTakeMinString();
         String takeMax         = rx.getTakeMaxString();
         boolean longTerm       = rx.getLongTerm();
      //   boolean isCustomNote   =rx.isCustomNote();
         String outsideProvOhip = rx.getOutsideProviderOhip();
         String brandName       = rx.getBrandName();
         String ATC             = rx.getAtcCode();
         if(ATC.trim().length()>0)
             ATC="ATC: "+ATC;
         String drugName;
         boolean isSpecInstPresent=false;
         if(gcn==0){//it's a custom drug
            drugName=customName;
         }else{
            drugName=brandName;
         }
         if(specialInstruction!=null&&!specialInstruction.equalsIgnoreCase("null")&&specialInstruction.trim().length()>0){
            isSpecInstPresent=true;
         }
      //   System.out.println("customName="+customName);
      //   System.out.println("drugName="+drugName);
         //for display
         if(drugName==null || drugName.equalsIgnoreCase("null"))
             drugName="" ;

         boolean pastMed        = rx.getPastMed();
         String quantity        = rx.getQuantity();
         String quantityText="";
         //System.out.println("in prescribe.jsp, unitName="+rx.getUnitName());

         if(rx.getUnitName()==null || rx.getUnitName().equalsIgnoreCase("null")){
             quantityText=quantity;
         }
         else{
             quantityText=quantity+" "+rx.getUnitName();
         }
         String duration        = rx.getDuration();
         String method          = rx.getMethod();
         String outsideProvName = rx.getOutsideProviderName();
         boolean isDiscontinuedLatest = rx.isDiscontinuedLatest();
         String archivedDate="";
         String archivedReason="";
         boolean isOutsideProvider ;
         System.out.println("---"+outsideProvOhip+"--");System.out.println("---"+outsideProvName+"--");
         if(isDiscontinuedLatest){
                //System.out.println("isDiscontinuedLatest true");
                archivedReason=rx.getLastArchReason();
                archivedDate=rx.getLastArchDate();
                //System.out.println("---"+archivedDate +"--"+archivedReason);
         }
         else{
             //System.out.println("isDiscontinuedLatest false");
         }
          if((outsideProvOhip!=null && !outsideProvOhip.equals("")) || (outsideProvName!=null && !outsideProvName.equals(""))){
             isOutsideProvider=true;
               // System.out.println("isOutsideProvider true");
         }
         else{
             isOutsideProvider=false;
             //System.out.println("isOutsideProvider false");
         }
         System.out.println("instructions ="+instructions+ " rand="+rand+" drugName="+drugName+" startDate="+startDate+" writtenDate="+writtenDate);
         if(route==null || route.equalsIgnoreCase("null")) route="";
         String rxString="Method:"+method+"; Route:"+route+"; Frequency:"+frequency+"; Min:"+takeMin+"; Max:"
                    +takeMax+"; Duration:"+duration+"; DurationUnit:"+durationUnit+"; Quantity:"+quantityText;
                    String methodStr = method;
                    String routeStr = route;
                    String frequencyStr = frequency;
                    String minimumStr = takeMin;
                    String maximumStr = takeMax;
                    String durationStr = duration;
                    String durationUnitStr = durationUnit;
                    String quantityStr = quantityText;
                    String unitNameStr="";
                    if(rx.getUnitName()!=null && !rx.getUnitName().equalsIgnoreCase("null"))
                        unitNameStr=rx.getUnitName();
                    String prnStr="";
                    if(prn)
                        prnStr="prn";
       // System.out.println("is spec inst present ="+isSpecInstPresent+";spec="+specialInstruction);
                    //System.out.println("drugname in prescribe.jsp="+drugName);
%>

<fieldset style="margin-top:2px;width:580px;" id="set_<%=rand%>">
    <a href="javascript:void(0);"  style="float:right;margin-left:5px;margin-top:0px;padding-top:0px;" onclick="$('set_<%=rand%>').remove();deletePrescribe('<%=rand%>');">X</a>
    <a href="javascript:void(0);" style="float:right;margin-top:0px;padding-top:0px;" onclick="$('rx_more_<%=rand%>').toggle();">  <span id="moreLessWord_<%=rand%>" onclick="updateMoreLess(id)" >more</span> </a>

    <label style="float:left;width:80px;" title="<%=ATC%>" >Name:</label>
    <input type="text" id="drugName_<%=rand%>"  name="drugName_<%=rand%>"  size="30" <%if(gcn==0){%> onchange="saveCustomName(this);" value="<%=drugName%>"<%} else{%> value='<%=drugName%>'  onchange="changeDrugName('<%=rand%>','<%=drugName%>');" <%}%>/><span id="alleg_<%=rand%>" style="color:red;"></span>&nbsp;&nbsp;<span id="inactive_<%=rand%>" style="color:red;"></span><br>
    <a href="javascript:void(0);" onclick="showHideSpecInst('siAutoComplete_<%=rand%>')" style="float:left;width:80px;">Instructions:</a>
    <input type="text" id="instructions_<%=rand%>" name="instructions_<%=rand%>" onkeypress="handleEnter(this,event);" value="<%=instructions%>" size="60" onchange="parseIntr(this);" /><a href="javascript:void(0);" onclick="displayMedHistory('<%=rand%>');" style="color:red;font-size:13pt;vertical-align:super;text-decoration:none" ><b>*</b></a> <a id="major_<%=rand%>" style="display:none;background-color:red"></a>&nbsp;<a id="moderate_<%=rand%>" style="display:none;background-color:orange"></a>&nbsp;<a id='minor_<%=rand%>' style="display:none;background-color:yellow;"></a>&nbsp;<a id='unknown_<%=rand%>' style="display:none;background-color:#B1FB17"></a>
       <br>
       <label for="siInput_<%=rand%>" ></label>
       <div id="siAutoComplete_<%=rand%>" <%if(isSpecInstPresent){%> style="overflow:visible;"<%} else{%> style="overflow:visible;display:none;"<%}%> >
           <label style="float:left;width:80px;">&nbsp;&nbsp;</label><input id="siInput_<%=rand%>" type="text" size="60" <%if(!isSpecInstPresent) {%>style="color:gray; width:auto" value="Enter Special Instruction" <%} else {%> style="color:black; width:auto" value="<%=specialInstruction%>" <%}%> onblur="changeText('siInput_<%=rand%>');updateSpecialInstruction('siInput_<%=rand%>');" onfocus="changeText('siInput_<%=rand%>');" >
           <div id="siContainer_<%=rand%>" style="float:right" >
           </div>
                       <br><br>
        </div>

        <label id="labelQuantity_<%=rand%>"  style="float:left;width:80px;">Qty/Mitte:</label><input <%if(rx.isCustomNote()){%> disabled <%}%> type="text" id="quantity_<%=rand%>"     name="quantity_<%=rand%>"     value="<%=quantityText%>" onblur="updateQty(this);" />
        <label style="">Repeats:</label><input type="text" id="repeats_<%=rand%>"  <%if(rx.isCustomNote()){%> disabled <%}%>    name="repeats_<%=rand%>"      value="<%=repeats%>" />

    <input  type="checkbox" id="longTerm_<%=rand%>"  name="longTerm_<%=rand%>" <%if(longTerm) {%> checked="true" <%}%> >Long Term Med </input>

       <div class="rxStr" title="not what you mean?" >
           <a href="javascript:void(0);" onclick="focusTo('method_<%=rand%>')">Method:</a><a   id="method_<%=rand%>" onclick="focusTo(this.id)" onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"><%=methodStr%></a>
           <a href="javascript:void(0);" onclick="focusTo('route_<%=rand%>')">Route:</a><a id="route_<%=rand%>" onclick="focusTo(this.id)" onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"> <%=routeStr%></a>
           <a href="javascript:void(0);" onclick="focusTo('frequency_<%=rand%>')">Frequency:</a><a  id="frequency_<%=rand%>" onclick="focusTo(this.id) " onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"> <%=frequencyStr%></a>
           <a href="javascript:void(0);" onclick="focusTo('minimum_<%=rand%>')">Min:</a><a  id="minimum_<%=rand%>" onclick="focusTo(this.id) " onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"> <%=minimumStr%></a>
           <a href="javascript:void(0);" onclick="focusTo('maximum_<%=rand%>')">Max:</a><a id="maximum_<%=rand%>" onclick="focusTo(this.id) " onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"> <%=maximumStr%></a>
           <a href="javascript:void(0);" onclick="focusTo('duration_<%=rand%>')">Duration:</a><a  id="duration_<%=rand%>" onclick="focusTo(this.id) " onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"> <%=durationStr%></a>
           <a href="javascript:void(0);" onclick="focusTo('durationUnit_<%=rand%>')">DurationUnit:</a><a  id="durationUnit_<%=rand%>" onclick="focusTo(this.id) " onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"> <%=durationUnitStr%></a>
           <a>Qty/Mitte:</a><a id="quantityStr_<%=rand%>"> <%=quantityStr%></a>
           <a> </a><a id="unitName_<%=rand%>"> <%=unitNameStr%></a>
           <a> </a><a href="javascript:void(0);" id="prn_<%=rand%>" onclick="setPrn('<%=rand%>');updateProperty('prnVal_<%=rand%>');"><%=prnStr%></a>
           <input id="prnVal_<%=rand%>"  style="display:none" <%if(prnStr.trim().length()==0){%>value="false"<%} else{%>value="true" <%}%> />
       </div>
       <div id="rx_more_<%=rand%>" style="display:none;padding:2px;">
          <bean:message key="WriteScript.msgPrescribedByOutsideProvider"/>
          <input type="checkbox" id="ocheck_<%=rand%>" name="ocheck_<%=rand%>" onclick="$('otext_<%=rand%>').toggle();" <%if(isOutsideProvider){%> checked="true" <%}else{}%>/>
          <div id="otext_<%=rand%>" <%if(isOutsideProvider){%>style="display:table;padding:2px;"<%}else{%>style="display:none;padding:2px;"<%}%> >
                <b><label style="float:left;width:80px;">Name :</label></b> <input type="text" id="outsideProviderName_<%=rand%>" name="outsideProviderName_<%=rand%>" <%if(outsideProvName!=null){%> value="<%=outsideProvName%>"<%}else {%> value=""<%}%> />
                <b><label style="width:80px;">OHIP No:</label></b> <input type="text" id="outsideProviderOhip_<%=rand%>" name="outsideProviderOhip_<%=rand%>"  <%if(outsideProvOhip!=null){%>value="<%=outsideProvOhip%>"<%}else {%> value=""<%}%>/>
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

           <div id="renalDosing_<%=rand%>" ></div>

           <oscar:oscarPropertiesCheck property="billregion" value="ON" >
           <%
            ArrayList<LimitedUseCode> luList = LimitedUseLookup.getLUInfoForDin(rx.getRegionalIdentifier());
            if (luList != null){ System.out.println("inside luList size is "+luList.size()); %>
            <div style="margin-top:2px;">
            <table style="border-width: 1px; border-spacing: 2px; border-style: outset; border-color: black;">
                        <tr>
                                <th colspan="2" align="left">Limited Use Codes</th>
                        </tr>

                        <%for (LimitedUseCode limitedUseCode : luList){%>
                        <tr>
                            <td valign="top">
                                <a onclick="javascript:addLuCode('instructions_<%=rand%>','<%=limitedUseCode.getUseId()%>')" href="javascript: return void();"><%=limitedUseCode.getUseId()%></a>&nbsp;
                            </td>
                            <td><%=limitedUseCode.getTxt()%></td>
                        </tr>
                        <%}%>
            </table>
            </div>
            <%}%>
            </oscar:oscarPropertiesCheck>

</fieldset>

        <script type="text/javascript">
            $('drugName_'+'<%=rand%>').value=decodeURIComponent(encodeURIComponent('<%=drugName%>'));

            handleEnter=function handleEnter(inField, ev){
                var charCode;
                if(ev && ev.which)
                    charCode=ev.which;
                else if(window.event){
                    ev=window.event;
                    charCode=ev.keyCode;
                }
                var id=inField.id.split("_")[1];
                if(charCode==13)
                    showHideSpecInst('siAutoComplete_'+id);
            }
            showHideSpecInst=function showHideSpecInst(elementId){
                oscarLog("in show hide spec inst="+elementId);
              if($(elementId).getStyle('display')=='none'){
                  Effect.BlindDown(elementId);
              }else{
                  Effect.BlindUp(elementId);
              }
            }

            var specArr=new Array();
            var specStr='<%=specStr%>';
            specArr=specStr.split("*");// * is used as delimiter
            //oscarLog("specArr="+specArr);
            YAHOO.example.BasicLocal = function() {
                // Use a LocalDataSource
                var oDS = new YAHOO.util.LocalDataSource(specArr);
                // Optional to define fields for single-dimensional array
                oDS.responseSchema = {fields : ["state"]};

                // Instantiate the AutoComplete
                var oAC = new YAHOO.widget.AutoComplete("siInput_<%=rand%>", "siContainer_<%=rand%>", oDS);
                oAC.prehighlightClassName = "yui-ac-prehighlight";
                oAC.useShadow = true;

                return {
                    oDS: oDS,
                    oAC: oAC
                };
            }();



            checkAllergy('<%=rand%>','<%=rx.getAtcCode()%>');
            checkIfInactive('<%=rand%>','<%=rx.getRegionalIdentifier()%>');
            <oscar:oscarPropertiesCheck property="RENAL_DOSING_DS" value="yes">
            getRenalDosingInformation('renalDosing_<%=rand%>','<%=rx.getAtcCode()%>');
            </oscar:oscarPropertiesCheck>
            var isDiscontinuedLatest=<%=isDiscontinuedLatest%>;
            oscarLog("isDiscon "+isDiscontinuedLatest);
            //pause(1000);
            if(isDiscontinuedLatest){
               var archD='<%=archivedDate%>';
               var archR='<%=archivedReason%>';
               oscarLog("in js discon "+archR+"--"+archD);

                    if(confirm('This drug was discontinued on <%=archivedDate%> because of <%=archivedReason%> are you sure you want to continue it?')==true){
                        //do nothing
                    }
                    else{
                        $('set_<%=rand%>').remove();
                        //call java class to delete it from stash pool.
                        var randId='<%=rand%>';
                        deletePrescribe(randId);
                    }
            }
            var listRxDrugSize=<%=listRxDrugs.size()%>;
            oscarLog("listRxDrugsSize="+listRxDrugSize);
            counterRx++;
            oscarLog("counterRx="+counterRx);
           var gcn_val=<%=gcn%>;
           if(gcn_val==0){
               $('drugName_<%=rand%>').focus();
           } else if(counterRx==listRxDrugSize){
               oscarLog("counterRx="+counterRx+"--listRxDrugSize="+listRxDrugSize);
               $('instructions_<%=rand%>').focus();
           }

        </script>
                <%}%>
  <script type="text/javascript">
    counterRx=0;
</script>
<%}%>

