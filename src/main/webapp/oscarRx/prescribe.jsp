<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="oscar.oscarRx.data.RxDrugData,java.util.*" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Calendar" %>
<%@page import="oscar.oscarRx.data.*" %>
<%@page import="oscar.oscarRx.util.*" %>
<%@page import="oscar.OscarProperties"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
    <%

List<RxPrescriptionData.Prescription> listRxDrugs=(List)request.getAttribute("listRxDrugs");
String action = StringUtils.defaultString((String)request.getAttribute("action"), "prescribe");
oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean)request.getSession().getAttribute("RxSessionBean");

if(listRxDrugs!=null){
            String specStr=RxUtil.getSpecialInstructions();

  for(RxPrescriptionData.Prescription rx : listRxDrugs ){
         String rand            = Long.toString(rx.getRandomId());
         String instructions    = rx.getSpecial();
         String specialInstruction=rx.getSpecialInstruction();
         String startDate       = RxUtil.DateToString(rx.getRxDate(), "yyyy-MM-dd");
         String writtenDate     = RxUtil.DateToString(rx.getWrittenDate(), "yyyy-MM-dd");
         String lastRefillDate  = RxUtil.DateToString(rx.getLastRefillDate(), "yyyy-MM-dd");
         int gcn=rx.getGCN_SEQNO();//if gcn is 0, rx is customed drug.
         String customName      = rx.getCustomName();
         Boolean patientCompliance  = rx.getPatientCompliance();
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
         String genericName     = rx.getGenericName();
         String dosage = rx.getDosage();
 
         String pickupDate      = RxUtil.DateToString(rx.getPickupDate(), "yyyy-MM-dd");
         String pickupTime      = RxUtil.DateToString(rx.getPickupTime(), "hh:mm");
         String eTreatmentType  = rx.getETreatmentType()!=null ? rx.getETreatmentType() : "";
         String rxStatus        = rx.getRxStatus()!=null ? rx.getRxStatus() : "";
         String drugForm		= rx.getDrugForm();
         //remove from the rerx list
         int DrugReferenceId = rx.getDrugReferenceId();
         
         if(ATC != null && ATC.trim().length()>0)
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
         //for display
         if(drugName==null || drugName.equalsIgnoreCase("null"))
             drugName="" ;

         String comment  = rx.getComment();
         if(rx.getComment() == null) {
        	 comment = "";
         }
         boolean pastMed            = rx.getPastMed();
         boolean startDateUnknown	= rx.getStartDateUnknown();
         boolean nonAuthoritative   = rx.isNonAuthoritative();
         String quantity            = rx.getQuantity();
         String quantityText="";
         String unitName=rx.getUnitName();
         if(unitName==null || unitName.equalsIgnoreCase("null") || unitName.trim().length()==0){
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
         int refillQuantity=rx.getRefillQuantity();
         int refillDuration=rx.getRefillDuration();
         int dispenseInterval=rx.getDispenseInterval();
         if(isDiscontinuedLatest){
                archivedReason=rx.getLastArchReason();
                archivedDate=rx.getLastArchDate();
         }

          if((outsideProvOhip!=null && !outsideProvOhip.equals("")) || (outsideProvName!=null && !outsideProvName.equals(""))){
             isOutsideProvider=true;
         }
         else{
             isOutsideProvider=false;
         }
         if(route==null || route.equalsIgnoreCase("null")) route="";
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
                drugName=drugName.replace("'", "\\'");
                drugName=drugName.replace("\"","\\\"");
                byte[] drugNameBytes = drugName.getBytes("ISO-8859-1");
				drugName= new String(drugNameBytes, "UTF-8");
                
%>
<%if (OscarProperties.getInstance().getProperty("rx_enhance")!=null && OscarProperties.getInstance().getProperty("rx_enhance").equals("true")) { %>
<fieldset style="margin-top:2px;width:580px;" id="set_<%=rand%>">
    <a tabindex="-1" href="javascript:void(0);"  style="float:right;margin-left:5px;margin-top:0px;padding-top:0px;" onclick="$('set_<%=rand%>').remove();deletePrescribe('<%=rand%>');removeReRxDrugId('<%=DrugReferenceId%>');">X</a>
    <a tabindex="-1" href="javascript:void(0);" style="float:right;margin-top:0px;padding-top:0px;" onclick="$('rx_more_<%=rand%>').toggle();">  <span id="moreLessWord_<%=rand%>" onclick="updateMoreLess(id)" >more</span> </a>

    <label style="float:left;width:80px;" title="<%=ATC%>" >Name:</label>
    <input tabindex="-1" type="text" id="drugName_<%=rand%>"  name="drugName_<%=rand%>"  size="60" <%if(gcn==0){%> onkeyup="saveCustomName(this);" value="<%=drugName%>"<%} else{%> value='<%=drugName%>'  onkeyup="changeDrugName('<%=rand%>','<%=drugName%>');" <%}%>/><span id="alleg_<%=rand%>" style="color:red;"></span>&nbsp;&nbsp;<span id="inactive_<%=rand%>" style="color:red;"></span><br>
    <a tabindex="-1" href="javascript:void(0);" onclick="showHideSpecInst('siAutoComplete_<%=rand%>')" style="float:left;width:80px;">Instructions:</a>
    <input type="text" id="instructions_<%=rand%>" name="instructions_<%=rand%>" onkeypress="handleEnter(this,event);" value="<%=instructions%>" size="60" onchange="parseIntr(this);" /><a href="javascript:void(0);" tabindex="-1" onclick="displayMedHistory('<%=rand%>');" style="color:red;font-size:13pt;vertical-align:super;text-decoration:none" ><b>*</b></a>  <a href="javascript:void(0);" tabindex="-1" onclick="displayInstructions('<%=rand%>');"><img src="<c:out value="${ctx}/images/icon_help_sml.gif"/>" border="0" TITLE="Instructions Field Reference"></a> 
       <br>
       <span id="major_<%=rand%>" style="display:none;background-color:red"></span>&nbsp;<span id="moderate_<%=rand%>" style="display:none;background-color:orange"></span>&nbsp;<span id='minor_<%=rand%>' style="display:none;background-color:yellow;"></span>&nbsp;<span id='unknown_<%=rand%>' style="display:none;background-color:#B1FB17"></span>
       <br>
       <label for="siInput_<%=rand%>" ></label>
       <div id="siAutoComplete_<%=rand%>" <%if(isSpecInstPresent){%> style="overflow:visible;"<%} else{%> style="overflow:visible;display:none;"<%}%> >
           <label style="float:left;width:80px;">&nbsp;&nbsp;</label><input id="siInput_<%=rand%>"  type="text" size="60" <%if(!isSpecInstPresent) {%>style="color:gray; width:auto" value="Enter Special Instruction" <%} else {%> style="color:black; width:auto" value="<%=specialInstruction%>" <%}%> onblur="changeText('siInput_<%=rand%>');updateSpecialInstruction('siInput_<%=rand%>');" onfocus="changeText('siInput_<%=rand%>');" >
           <div id="siContainer_<%=rand%>" style="float:right" >
           </div>
                       <br><br>
        </div>

       <%--  <label id="labelQuantity_<%=rand%>"  style="float:left;width:80px;">Qty/Mitte:</label><input <%if(rx.isCustomNote()){%> disabled <%}%> type="text" id="quantity_<%=rand%>"     name="quantity_<%=rand%>"     value="<%=quantityText%>" onblur="updateQty(this);" /> --%>
        <label id="labelQuantity_<%=rand%>"  style="float:left;width:80px;">Qty/Mitte:</label><input <%if(rx.isCustomNote()){%> disabled <%}%> type="text" id="quantity_<%=rand%>" name="quantity_<%=rand%>" value="<%=quantityText%>" onblur="updateQty(this);" />
        <label style="">Repeats:</label><input type="text" id="repeats_<%=rand%>"  <%if(rx.isCustomNote()){%> disabled <%}%>    name="repeats_<%=rand%>"   value="<%=repeats%>" />

        <input  type="checkbox" id="longTerm_<%=rand%>"  name="longTerm_<%=rand%>" <%if(longTerm) {%> checked="true" <%}%> >Long Term Med </input>
        <%if(genericName!=null&&!genericName.equalsIgnoreCase("null")){%>
        <div><a>Ingredient:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=genericName%></a><a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Strength:&nbsp;&nbsp;<%=dosage%></a></div><%}%>
       <div class="rxStr" title="not what you mean?" >
           <a tabindex="-1" href="javascript:void(0);" onclick="focusTo('method_<%=rand%>')">Method:</a><a   id="method_<%=rand%>" onclick="focusTo(this.id)" onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"><%=methodStr%></a>
           <a tabindex="-1" href="javascript:void(0);" onclick="focusTo('route_<%=rand%>')">Route:</a><a id="route_<%=rand%>" onclick="focusTo(this.id)" onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"> <%=routeStr%></a>
           <a tabindex="-1" href="javascript:void(0);" onclick="focusTo('frequency_<%=rand%>')">Frequency:</a><a  id="frequency_<%=rand%>" onclick="focusTo(this.id) " onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"> <%=frequencyStr%></a>
           <a tabindex="-1" href="javascript:void(0);" onclick="focusTo('minimum_<%=rand%>')">Min:</a><a  id="minimum_<%=rand%>" onclick="focusTo(this.id) " onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"> <%=minimumStr%></a>
           <a tabindex="-1" href="javascript:void(0);" onclick="focusTo('maximum_<%=rand%>')">Max:</a><a id="maximum_<%=rand%>" onclick="focusTo(this.id) " onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"> <%=maximumStr%></a>
           <a tabindex="-1" href="javascript:void(0);" onclick="focusTo('duration_<%=rand%>')">Duration:</a><a  id="duration_<%=rand%>" onclick="focusTo(this.id) " onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"> <%=durationStr%></a>
           <a tabindex="-1" href="javascript:void(0);" onclick="focusTo('durationUnit_<%=rand%>')">DurationUnit:</a><a  id="durationUnit_<%=rand%>" onclick="focusTo(this.id) " onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"> <%=durationUnitStr%></a>
           <a tabindex="-1" >Qty/Mitte:</a><a tabindex="-1" id="quantityStr_<%=rand%>"> <%=quantityStr%></a>
           <a> </a><a tabindex="-1" id="unitName_<%=rand%>"> </a>
           <a> </a><a tabindex="-1" href="javascript:void(0);" id="prn_<%=rand%>" onclick="setPrn('<%=rand%>');updateProperty('prnVal_<%=rand%>');"><%=prnStr%></a>
           <input id="prnVal_<%=rand%>"  style="display:none" <%if(prnStr.trim().length()==0){%>value="false"<%} else{%>value="true" <%}%> />
       </div>
       <div id="rx_more_<%=rand%>" style="display:none;padding:2px;">
       <bean:message key="WriteScript.msgPrescribedRefill"/>:
       	  &nbsp;
       	  <bean:message key="WriteScript.msgPrescribedRefillDuration"/>
       	  <input type="text" size="6" id="refillDuration_<%=rand%>" name="refillDuration_<%=rand%>" value="<%=refillDuration%>"
       	   onchange="if(isNaN(this.value)||this.value<0){alert('Refill duration must be number (of days)');this.focus();return false;}return true;" /><bean:message key="WriteScript.msgPrescribedRefillDurationDays"/>
       	  &nbsp;       	  
       	  <bean:message key="WriteScript.msgPrescribedRefillQuantity"/>
       	  <input type="text" size="6" id="refillQuantity_<%=rand%>" name="refillQuantity_<%=rand%>" value="<%=refillQuantity%>" />
       	  <br/>       	 
       	  <bean:message key="WriteScript.msgPrescribedDispenseInterval"/>
       	  <input type="text" size="6" id="dispenseInterval_<%=rand%>" name="dispenseInterval_<%=rand%>" value="<%=dispenseInterval%>" />
       	  <br/>
       
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
            <input type="checkbox"  name="patientComplianceY_<%=rand%>" id="patientComplianceY_<%=rand%>" <%if(patientCompliance!=null && patientCompliance) {%> checked="true" <%}%> />

          <bean:message key="WriteScript.msgNo"/>
            <input type="checkbox"  name="patientComplianceN_<%=rand%>" id="patientComplianceN_<%=rand%>" <%if(patientCompliance!=null && !patientCompliance) {%> checked="true" <%}%> /><br/>

		<bean:message key="WriteScript.msgNonAuthoritative"/>
            <input type="checkbox" name="nonAuthoritativeN_<%=rand%>" id="nonAuthoritativeN_<%=rand%> " <%if(nonAuthoritative) {%> checked="true" <%}%> /><br/>
		

        <label style="float:left;width:80px;">Start Date:</label>
           <input type="text" id="rxDate_<%=rand%>" name="rxDate_<%=rand%>" value="<%=startDate%>"/>
	<label style="">Last Refill Date:</label>
           <input type="text" id="lastRefillDate_<%=rand%>"  name="lastRefillDate_<%=rand%>" value="<%=lastRefillDate%>" />
	<br/>
        <label style="float:left;width:80px;">Written Date:</label>
           <input type="text" id="writtenDate_<%=rand%>"  name="writtenDate_<%=rand%>" value="<%=writtenDate%>" />
           <a href="javascript:void(0);" style="float:right;margin-top:0px;padding-top:0px;" onclick="addFav('<%=rand%>','<%=drugName%>')">Add to Favorite</a>
       </div>
       
       <!--  g.d. added -->
        <br/>
        <bean:message key="WriteScript.msgPickUpDate"/>: 
           <input type="text" id="pickupDate_<%=rand%>"  name="pickupDate_<%=rand%>" value="<%=pickupDate%>" onchange="if (!isValidDate(this.value)) {this.value=null}" />
           <bean:message key="WriteScript.msgPickUpTime"/>: 
           <input type="text" id="pickupTime_<%=rand%>"  name="pickupTime_<%=rand%>" value="<%=pickupTime%>" onchange="if (!isValidTime(this.value)) {this.value=null}" />
       <br/>
       
       
       
       <bean:message key="WriteScript.msgComment"/>:
           <input type="text" id="comment_<%=rand%>" name="comment_<%=rand%>" value="<%=comment%>" size="60"/>
           <br/>  
           <bean:message key="WriteScript.msgETreatmentType"/>:     
           <select name="eTreatmentType_<%=rand%>">
           		<option>--</option>
                         <option value="CHRON" <%=eTreatmentType.equals("CHRON")?"selected":""%>><bean:message key="WriteScript.msgETreatment.Continuous"/></option>
 				<option value="ACU" <%=eTreatmentType.equals("ACU")?"selected":""%>><bean:message key="WriteScript.msgETreatment.Acute"/></option>
 				<option value="ONET" <%=eTreatmentType.equals("ONET")?"selected":""%>><bean:message key="WriteScript.msgETreatment.OneTime"/></option>
 				<option value="PRNL" <%=eTreatmentType.equals("PRNL")?"selected":""%>><bean:message key="WriteScript.msgETreatment.LongTermPRN"/></option>
 				<option value="PRNS" <%=eTreatmentType.equals("PRNS")?"selected":""%>><bean:message key="WriteScript.msgETreatment.ShortTermPRN"/></option>           </select>
           <select name="rxStatus_<%=rand%>">
           		<option>--</option>
                         <option value="New" <%=rxStatus.equals("New")?"selected":""%>><bean:message key="WriteScript.msgRxStatus.New"/></option>
                         <option value="Active" <%=rxStatus.equals("Active")?"selected":""%>><bean:message key="WriteScript.msgRxStatus.Active"/></option>
                         <option value="Suspended" <%=rxStatus.equals("Suspended")?"selected":""%>><bean:message key="WriteScript.msgRxStatus.Suspended"/></option>
                         <option value="Aborted" <%=rxStatus.equals("Aborted")?"selected":""%>><bean:message key="WriteScript.msgRxStatus.Aborted"/></option>
                         <option value="Completed" <%=rxStatus.equals("Completed")?"selected":""%>><bean:message key="WriteScript.msgRxStatus.Completed"/></option>
                         <option value="Obsolete" <%=rxStatus.equals("Obsolete")?"selected":""%>><bean:message key="WriteScript.msgRxStatus.Obsolete"/></option>
                         <option value="Nullified" <%=rxStatus.equals("Nullified")?"selected":""%>><bean:message key="WriteScript.msgRxStatus.Nullified"/></option>
           </select>
                <br/>                
                <bean:message key="WriteScript.msgDrugForm"/>: 
                <%if(rx.getDrugFormList()!=null && rx.getDrugFormList().indexOf(",")!=-1){ %>
                <select name="drugForm_<%=rand%>">
                	<%
                		String[] forms = rx.getDrugFormList().split(",");
                		for(String form:forms) {
                	%>
                		<option value="<%=form%>" <%=form.equals(drugForm)?"selected":"" %>><%=form%></option>
                	<% } %>
                </select>    
				<%} else { %>
					<%=drugForm%>
				<% } %>

           <div id="renalDosing_<%=rand%>" ></div>
           <div id="luc_<%=rand%>" style="margin-top:2px;"></div>
           <oscar:oscarPropertiesCheck property="RENAL_DOSING_DS" value="yes">
            <script type="text/javascript">getRenalDosingInformation('renalDosing_<%=rand%>','<%=rx.getAtcCode()%>');</script>
            </oscar:oscarPropertiesCheck>
           <oscar:oscarPropertiesCheck property="billregion" value="ON" >
               <script type="text/javascript">getLUC('luc_<%=rand%>','<%=rand%>','<%=rx.getRegionalIdentifier()%>');</script>
            </oscar:oscarPropertiesCheck>
</fieldset>
<%}else{%>
<fieldset style="margin-top:2px;width:620px;" id="set_<%=rand%>">
    <a tabindex="-1" href="javascript:void(0);"  style="float:right;margin-left:5px;margin-top:0px;padding-top:0px;" onclick="$('set_<%=rand%>').remove();deletePrescribe('<%=rand%>');removeReRxDrugId('<%=DrugReferenceId%>')"><img src='<c:out value="${ctx}/images/close.png"/>' border="0"></a>
    <a tabindex="-1" href="javascript:void(0);" style="float:right;margin-top:0px;padding-top:0px;" onclick="$('rx_more_<%=rand%>').toggle();">  <span id="moreLessWord_<%=rand%>" onclick="updateMoreLess(id)" >more</span> </a>

    <label style="float:left;width:80px;" title="<%=ATC%>" >Name:</label>
    <input tabindex="-1" type="text" id="drugName_<%=rand%>"  name="drugName_<%=rand%>"  size="30" <%if(gcn==0){%> onkeyup="saveCustomName(this);" value="<%=drugName%>"<%} else{%> value='<%=drugName%>'  onkeyup="changeDrugName('<%=rand%>','<%=drugName%>');" <%}%> TITLE="<%=drugName%>"/>&nbsp;<span id="inactive_<%=rand%>" style="color:red;"></span><br>

	<!-- Allergy Alert Table-->
	<table width="570px" border="1" bordercolor="#CCCCCC" cellspacing="0" cellpadding="0" style="border-collapse: collapse;display: none;" id="alleg_tbl_<%=rand%>">
		<tr>
			<td bgcolor="#CCCCCC" height="10px">
				<!--spacer cell-->
			</td>
		</tr>
	
		<tr>
			<td>    
	    		<span id="alleg_<%=rand%>" style="font-size:11px;"></span>
			</td>
		</tr>
	</table>     
    
    <a tabindex="-1" href="javascript:void(0);" onclick="showHideSpecInst('siAutoComplete_<%=rand%>')" style="float:left;width:80px;">Instructions:</a>
    <input type="text" id="instructions_<%=rand%>" name="instructions_<%=rand%>" onkeypress="handleEnter(this,event);" value="<%=instructions%>" size="60" onchange="parseIntr(this);" /><a href="javascript:void(0);" tabindex="-1" onclick="displayMedHistory('<%=rand%>');" style="color:red;font-size:13pt;vertical-align:super;text-decoration:none" TITLE="Instruction Examples"><b>*</b></a>  <a href="javascript:void(0);" tabindex="-1" onclick="displayInstructions('<%=rand%>');"><img src="<c:out value="${ctx}/images/icon_help_sml.gif"/>" border="0" TITLE="Instructions Field Reference"></a> <span id="major_<%=rand%>" style="display:none;background-color:red"></span>&nbsp;<span id="moderate_<%=rand%>" style="display:none;background-color:orange"></span>&nbsp;<span id='minor_<%=rand%>' style="display:none;background-color:yellow;"></span>&nbsp;<span id='unknown_<%=rand%>' style="display:none;background-color:#B1FB17"></span>
       <br>
       <label for="siInput_<%=rand%>" ></label>
       <div id="siAutoComplete_<%=rand%>" <%if(isSpecInstPresent){%> style="overflow:visible;"<%} else{%> style="overflow:visible;display:none;"<%}%> >
           <label style="float:left;width:80px;">&nbsp;&nbsp;</label><input id="siInput_<%=rand%>"  type="text" size="60" <%if(!isSpecInstPresent) {%>style="color:gray; width:auto" value="Enter Special Instruction" <%} else {%> style="color:black; width:auto" value="<%=specialInstruction%>" <%}%> onblur="changeText('siInput_<%=rand%>');updateSpecialInstruction('siInput_<%=rand%>');" onfocus="changeText('siInput_<%=rand%>');" >
           <div id="siContainer_<%=rand%>" style="float:right" >
           </div>
                       <br><br>
        </div>

        <label id="labelQuantity_<%=rand%>"  style="float:left;width:80px;">Qty/Mitte1:</label><input <%if(rx.isCustomNote()){%> disabled <%}%> type="text" id="quantity_<%=rand%>"     name="quantity_<%=rand%>"     value="<%=quantityText%>" onblur="updateQty(this);" />
        <label style="">Repeats:</label><input type="text" id="repeats_<%=rand%>"  <%if(rx.isCustomNote()){%> disabled <%}%>    name="repeats_<%=rand%>"   value="<%=repeats%>" />

        <input  type="checkbox" id="longTerm_<%=rand%>"  name="longTerm_<%=rand%>" <%if(longTerm) {%> checked="true" <%}%> />Long Term Med
        <%if(genericName!=null&&!genericName.equalsIgnoreCase("null")){%>
        <div><a>Ingredient:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=genericName%></a></div><%}%>
       <div class="rxStr" title="not what you mean?" >
           <a tabindex="-1" href="javascript:void(0);" onclick="focusTo('method_<%=rand%>')">Method:</a><a   id="method_<%=rand%>" onclick="focusTo(this.id)" onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"><%=methodStr%></a>
           <a tabindex="-1" href="javascript:void(0);" onclick="focusTo('route_<%=rand%>')">Route:</a><a id="route_<%=rand%>" onclick="focusTo(this.id)" onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"> <%=routeStr%></a>
           <a tabindex="-1" href="javascript:void(0);" onclick="focusTo('frequency_<%=rand%>')">Frequency:</a><a  id="frequency_<%=rand%>" onclick="focusTo(this.id) " onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"> <%=frequencyStr%></a>
           <a tabindex="-1" href="javascript:void(0);" onclick="focusTo('minimum_<%=rand%>')">Min:</a><a  id="minimum_<%=rand%>" onclick="focusTo(this.id) " onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"> <%=minimumStr%></a>
           <a tabindex="-1" href="javascript:void(0);" onclick="focusTo('maximum_<%=rand%>')">Max:</a><a id="maximum_<%=rand%>" onclick="focusTo(this.id) " onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"> <%=maximumStr%></a>
           <a tabindex="-1" href="javascript:void(0);" onclick="focusTo('duration_<%=rand%>')">Duration:</a><a  id="duration_<%=rand%>" onclick="focusTo(this.id) " onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"> <%=durationStr%></a>
           <a tabindex="-1" href="javascript:void(0);" onclick="focusTo('durationUnit_<%=rand%>')">DurationUnit:</a><a  id="durationUnit_<%=rand%>" onclick="focusTo(this.id) " onfocus="lookEdittable(this.id)" onblur="lookNonEdittable(this.id);updateProperty(this.id);"> <%=durationUnitStr%></a>
           <a tabindex="-1" >Qty/Mitte:</a><a tabindex="-1" id="quantityStr_<%=rand%>"> <%=quantityStr%></a>
           <a> </a><a tabindex="-1" id="unitName_<%=rand%>"> </a>
           <a> </a><a tabindex="-1" href="javascript:void(0);" id="prn_<%=rand%>" onclick="setPrn('<%=rand%>');updateProperty('prnVal_<%=rand%>');"><%=prnStr%></a>
           <input id="prnVal_<%=rand%>"  style="display:none" <%if(prnStr.trim().length()==0){%>value="false"<%} else{%>value="true" <%}%> />
           <input id="rx_save_updates_<%=rand%>" type="button" value="Save Changes" onclick="saveLinks('<%=rand%>')"/>
       </div>
       <div id="rx_more_<%=rand%>" style="display:none;padding:2px;">
       	  <bean:message key="WriteScript.msgPrescribedRefill"/>:
       	  &nbsp;
       	  <bean:message key="WriteScript.msgPrescribedRefillDuration"/>
       	  <input type="text" size="6" id="refillDuration_<%=rand%>" name="refillDuration_<%=rand%>" value="<%=refillDuration%>"
       	   onchange="if(isNaN(this.value)||this.value<0){alert('Refill duration must be number (of days)');this.focus();return false;}return true;" /><bean:message key="WriteScript.msgPrescribedRefillDurationDays"/>
       	  &nbsp;       	  
       	  <bean:message key="WriteScript.msgPrescribedRefillQuantity"/>
       	  <input type="text" size="6" id="refillQuantity_<%=rand%>" name="refillQuantity_<%=rand%>" value="<%=refillQuantity%>" />
       	  <br/>       	  
       	  <bean:message key="WriteScript.msgPrescribedDispenseInterval"/>
       	  <input type="text" size="6" id="dispenseInterval_<%=rand%>" name="dispenseInterval_<%=rand%>" value="<%=dispenseInterval%>" />
       	  <br/>
          <bean:message key="WriteScript.msgPrescribedByOutsideProvider"/>
          <input type="checkbox" id="ocheck_<%=rand%>" name="ocheck_<%=rand%>" onclick="$('otext_<%=rand%>').toggle();" <%if(isOutsideProvider){%> checked="true" <%}else{}%>/>
          <div id="otext_<%=rand%>" <%if(isOutsideProvider){%>style="display:table;padding:2px;"<%}else{%>style="display:none;padding:2px;"<%}%> >
                <b><label style="float:left;width:80px;">Name :</label></b> <input type="text" id="outsideProviderName_<%=rand%>" name="outsideProviderName_<%=rand%>" <%if(outsideProvName!=null){%> value="<%=outsideProvName%>"<%}else {%> value=""<%}%> />
                <b><label style="width:80px;">OHIP No:</label></b> <input type="text" id="outsideProviderOhip_<%=rand%>" name="outsideProviderOhip_<%=rand%>"  <%if(outsideProvOhip!=null){%>value="<%=outsideProvOhip%>"<%}else {%> value=""<%}%>/>
          </div><br/>

        <label title="Medications taken at home that were previously ordered."><bean:message key="WriteScript.msgPastMedication" />
            <input  type="checkbox" name="pastMed_<%=rand%>" id="pastMed_<%=rand%>" <%if(pastMed) {%> checked="true" <%}%> onclick="emptyWrittenDate('<%=rand%>');" /></label>
                  
	<Br/>
	
	<bean:message key="WriteScript.msgPatientCompliance"/>:
          <bean:message key="WriteScript.msgYes"/>
            <input type="checkbox"  name="patientComplianceY_<%=rand%>" id="patientComplianceY_<%=rand%>" <%if(patientCompliance!=null && patientCompliance) {%> checked="true" <%}%> />

          <bean:message key="WriteScript.msgNo"/>
            <input type="checkbox"  name="patientComplianceN_<%=rand%>" id="patientComplianceN_<%=rand%>" <%if(patientCompliance!=null && !patientCompliance) {%> checked="true" <%}%> />

          <bean:message key="WriteScript.msgNonAuthoritative"/>
            <input type="checkbox" name="nonAuthoritativeN_<%=rand%>" id="nonAuthoritativeN_<%=rand%> " <%if(nonAuthoritative) {%> checked="true" <%}%> /><br/>


        <label style="float:left;width:80px;">Start Date:</label>
           <input type="text" id="rxDate_<%=rand%>" name="rxDate_<%=rand%>" value="<%=startDate%>" <%if(startDateUnknown) {%> disabled="disabled" <%}%>/>
        <bean:message key="WriteScript.msgUnknown"/>
           <input  type="checkbox" name="startDateUnknown_<%=rand%>" id="startDateUnknown_<%=rand%>" <%if(startDateUnknown) {%> checked="true" <%}%> onclick="toggleStartDateUnknown('<%=rand%>');"/>
           
           <br/>
	<label style="">Last Refill Date:</label>
           <input type="text" id="lastRefillDate_<%=rand%>"  name="lastRefillDate_<%=rand%>" value="<%=lastRefillDate%>" />
	<br/>
        <label style="float:left;width:80px;">Written Date:</label>
           <input type="text" id="writtenDate_<%=rand%>"  name="writtenDate_<%=rand%>" value="<%=writtenDate%>" />
           <a href="javascript:void(0);" style="float:right;margin-top:0px;padding-top:0px;" onclick="addFav('<%=rand%>','<%=drugName%>')">Add to Favorite</a>
       
           <br />
           <bean:message key="WriteScript.msgPickUpDate"/>: 
           <input type="text" id="pickupDate_<%=rand%>"  name="pickupDate_<%=rand%>" value="<%=pickupDate%>" onchange="if (!isValidDate(this.value)) {this.value=null}" />
           <bean:message key="WriteScript.msgPickUpTime"/>: 
           <input type="text" id="pickupTime_<%=rand%>"  name="pickupTime_<%=rand%>" value="<%=pickupTime%>" onchange="if (!isValidTime(this.value)) {this.value=null}" />
           <br/>
           <bean:message key="WriteScript.msgComment"/>:
           <input type="text" id="comment_<%=rand%>" name="comment_<%=rand%>" value="<%=comment%>" size="60"/>
           <br/>  
           <bean:message key="WriteScript.msgETreatmentType"/>:     
           <select name="eTreatmentType_<%=rand%>">
           		<option>--</option>
                         <option value="CHRON" <%=eTreatmentType.equals("CHRON")?"selected":""%>><bean:message key="WriteScript.msgETreatment.Continuous"/></option>
 				<option value="ACU" <%=eTreatmentType.equals("ACU")?"selected":""%>><bean:message key="WriteScript.msgETreatment.Acute"/></option>
 				<option value="ONET" <%=eTreatmentType.equals("ONET")?"selected":""%>><bean:message key="WriteScript.msgETreatment.OneTime"/></option>
 				<option value="PRNL" <%=eTreatmentType.equals("PRNL")?"selected":""%>><bean:message key="WriteScript.msgETreatment.LongTermPRN"/></option>
 				<option value="PRNS" <%=eTreatmentType.equals("PRNS")?"selected":""%>><bean:message key="WriteScript.msgETreatment.ShortTermPRN"/></option>           </select>
           <select name="rxStatus_<%=rand%>">
           		<option>--</option>
                         <option value="New" <%=rxStatus.equals("New")?"selected":""%>><bean:message key="WriteScript.msgRxStatus.New"/></option>
                         <option value="Active" <%=rxStatus.equals("Active")?"selected":""%>><bean:message key="WriteScript.msgRxStatus.Active"/></option>
                         <option value="Suspended" <%=rxStatus.equals("Suspended")?"selected":""%>><bean:message key="WriteScript.msgRxStatus.Suspended"/></option>
                         <option value="Aborted" <%=rxStatus.equals("Aborted")?"selected":""%>><bean:message key="WriteScript.msgRxStatus.Aborted"/></option>
                         <option value="Completed" <%=rxStatus.equals("Completed")?"selected":""%>><bean:message key="WriteScript.msgRxStatus.Completed"/></option>
                         <option value="Obsolete" <%=rxStatus.equals("Obsolete")?"selected":""%>><bean:message key="WriteScript.msgRxStatus.Obsolete"/></option>
                         <option value="Nullified" <%=rxStatus.equals("Nullified")?"selected":""%>><bean:message key="WriteScript.msgRxStatus.Nullified"/></option>
           </select>
                <br/>                
                <bean:message key="WriteScript.msgDrugForm"/>: 
                <%if(rx.getDrugFormList()!=null && rx.getDrugFormList().indexOf(",")!=-1){ %>
                <select name="drugForm_<%=rand%>">
                	<%
                		String[] forms = rx.getDrugFormList().split(",");
                		for(String form:forms) {
                	%>
                		<option value="<%=form%>" <%=form.equals(drugForm)?"selected":"" %>><%=form%></option>
                	<% } %>
                </select>    
				<%} else { %>
					<%=drugForm%>
				<% } %>




       </div>
       
           <div id="renalDosing_<%=rand%>" ></div>
           <div id="luc_<%=rand%>" style="margin-top:2px;"/>
           <oscar:oscarPropertiesCheck property="RENAL_DOSING_DS" value="yes">
            <script type="text/javascript">getRenalDosingInformation('renalDosing_<%=rand%>','<%=rx.getAtcCode()%>');</script>
            </oscar:oscarPropertiesCheck>
           <oscar:oscarPropertiesCheck property="billregion" value="ON" >
               <script type="text/javascript">getLUC('luc_<%=rand%>','<%=rand%>','<%=rx.getRegionalIdentifier()%>');</script>
            </oscar:oscarPropertiesCheck>

</fieldset>
<%}%>
                <script type="text/javascript">
                        jQuery("document").ready(function() {
                                if ( jQuery.browser.msie ) {
                                        jQuery('#rx_save_updates_<%=rand%>').show();
                                } else {
                                        jQuery('#rx_save_updates_<%=rand%>').hide();
                                }
                        });
                </script>


        <script type="text/javascript">
            $('drugName_'+'<%=rand%>').value=decodeURIComponent(encodeURIComponent('<%=drugName%>'));
            calculateRxData('<%=rand%>');
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
              if($(elementId).getStyle('display')=='none'){
                  Effect.BlindDown(elementId);
              }else{
                  Effect.BlindUp(elementId);
              }
            }

            var specArr=new Array();
            var specStr='<%=org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(specStr)%>';
            
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

            var isDiscontinuedLatest=<%=isDiscontinuedLatest%>;
            //oscarLog("isDiscon "+isDiscontinuedLatest);
            //pause(1000);
            var archR='<%=archivedReason%>';
            if(isDiscontinuedLatest && archR!="represcribed"){
               var archD='<%=archivedDate%>';
               //oscarLog("in js discon "+archR+"--"+archD);

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
            //oscarLog("listRxDrugsSize="+listRxDrugSize);
            counterRx++;
            //oscarLog("counterRx="+counterRx);
           var gcn_val=<%=gcn%>;
           if(gcn_val==0){
               $('drugName_<%=rand%>').focus();
           } else if(counterRx==listRxDrugSize){
               //oscarLog("counterRx="+counterRx+"--listRxDrugSize="+listRxDrugSize);
               $('instructions_<%=rand%>').focus();
           }
        </script>
                <%}%>
  <script type="text/javascript">
    counterRx=0;
</script>
<%}%>
