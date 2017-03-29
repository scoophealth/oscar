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
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="oscar.oscarRx.data.RxDrugData,java.util.*" %>
<%@page import="java.util.Date" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Calendar" %>
<%@page import="oscar.oscarRx.data.*" %>
<%@page import="oscar.oscarRx.util.*" %>
<%@page import="oscar.OscarProperties"%>
<%@page import="org.oscarehr.common.dao.ProviderDataDao"%>
<%@page import="org.oscarehr.common.model.ProviderData"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.model.Drug" %>
<%@page import="org.oscarehr.casemgmt.service.CaseManagementManager" %>
<%@page import="oscar.OscarProperties" %>
<bean:define id="patient" type="oscar.oscarRx.data.RxPatientData.Patient" name="Patient" />
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
    int flag = 1;
    String methToday = "", subToday = methToday, methEnddate=methToday, subEnddate=subToday;
    String noCarryDays = null, carryDays = null, carryLevel = "0";
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_rx" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_rx");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

    <%

List<RxPrescriptionData.Prescription> listRxDrugs=(List)request.getAttribute("listRxDrugs");
String action = (String)request.getAttribute("action");
if (OscarProperties.getInstance().isPropertyActive("enable_rx_custom_methodone_suboxone") && action == null) {
	 action = "prescribe";
}
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
         boolean shortTerm		= rx.getShortTerm();
      //   boolean isCustomNote   =rx.isCustomNote();
         String outsideProvOhip = rx.getOutsideProviderOhip();
         String brandName       = rx.getBrandName();
         String ATC             = rx.getAtcCode();
         String ATCcode			= rx.getAtcCode();
         String genericName     = rx.getGenericName();
         String dosage = rx.getDosage();
 
         String pickupDate      = RxUtil.DateToString(rx.getPickupDate(), "yyyy-MM-dd");
         String pickupTime      = RxUtil.DateToString(rx.getPickupTime(), "hh:mm");
         String eTreatmentType  = rx.getETreatmentType()!=null ? rx.getETreatmentType() : "";
         String rxStatus        = rx.getRxStatus()!=null ? rx.getRxStatus() : "";
         String drugForm		= rx.getDrugForm();
         //remove from the rerx list
         int DrugReferenceId = rx.getDrugReferenceId();
         
         if( ATCcode == null || ATCcode.trim().length() == 0 ) {
             ATCcode = "";
         }
         
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
         boolean dispenseInternal = rx.getDispenseInternal();
         boolean startDateUnknown	= rx.getStartDateUnknown();
         boolean nonAuthoritative   = rx.isNonAuthoritative();
         String quantity            = rx.getQuantity();
         String dispensingUnits     = rx.getDispensingUnits();
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
                    String dispensingUnitsStr = rx.getDispensingUnits();
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

<%if (OscarProperties.getInstance().isPropertyActive("enable_rx_custom_methodone_suboxone")) { %>
<script type="text/javascript">
convertToDate = function(date) {
	return new Date(date.replace(/-/g, "\/"));
};

disabledMethadoneDays = function(rand) {
	var start = convertToDate(jQuery("#methadoneStartDate_"+rand).val());
	var startBk = new Date(start);
	var end = convertToDate(jQuery("#methdoneEndDate_"+rand).val());
	if (start > end) {
		alert("End date should be bigger than Start Date!");
		return;
	}
	var i;
	for (i=0; i<7; i++) {
		var day = start.getDay();
		switch(day) {
		case 1:
			if (start <= end) {
				jQuery("#drkMon_"+rand).removeAttr("disabled");
				jQuery("#homedoseMonMeth_"+rand).removeAttr("disabled");
			} else {
				jQuery("#drkMon_"+rand).attr("disabled","disabled");
				jQuery("#drkMon_"+rand).removeAttr("checked");
				jQuery("#homedoseMonMeth_"+rand).attr("disabled", "disabled");
				jQuery("#homedoseMonMeth_"+rand).removeAttr("checked");
			}
			break;
		case 2:
			if (start <= end) {
				jQuery("#drkTues_"+rand).removeAttr("disabled");
				jQuery("#homedoseTuesMeth_"+rand).removeAttr("disabled");
			} else {
				jQuery("#drkTues_"+rand).attr("disabled","disabled");
				jQuery("#drkTues_"+rand).removeAttr("checked");
				jQuery("#homedoseTuesMeth_"+rand).attr("disabled", "disabled");
				jQuery("#homedoseTuesMeth_"+rand).removeAttr("checked");
			}
			break;
		case 3:
			if (start <= end) {
				jQuery("#drkWed_"+rand).removeAttr("disabled");
				jQuery("#homedoseWedMeth_"+rand).removeAttr("disabled");
			} else {
				jQuery("#drkWed_"+rand).attr("disabled","disabled");
				jQuery("#drkWed_"+rand).removeAttr("checked");
				jQuery("#homedoseWedMeth_"+rand).attr("disabled", "disabled");
				jQuery("#homedoseWedMeth_"+rand).removeAttr("checked");
			}
			break;
		case 4:
			if (start <= end) {
				jQuery("#drkThurs_"+rand).removeAttr("disabled");
				jQuery("#homedoseThursMeth_"+rand).removeAttr("disabled");
			} else {
				jQuery("#drkThurs_"+rand).attr("disabled","disabled");
				jQuery("#drkThurs_"+rand).removeAttr("checked");
				jQuery("#homedoseThursMeth_"+rand).attr("disabled", "disabled");
				jQuery("#homedoseThursMeth_"+rand).removeAttr("checked");
			}
			break;
		case 5:
			if (start <= end) {
				jQuery("#drkFri_"+rand).removeAttr("disabled");
				jQuery("#homedoseFriMeth_"+rand).removeAttr("disabled");
			} else {
				jQuery("#drkFri_"+rand).attr("disabled","disabled");
				jQuery("#drkFri_"+rand).removeAttr("checked");
				jQuery("#homedoseFriMeth_"+rand).attr("disabled", "disabled");
				jQuery("#homedoseFriMeth_"+rand).removeAttr("checked");
			}
			break;
		case 6:
			if (start <= end) {
				jQuery("#drkSat_"+rand).removeAttr("disabled");
				jQuery("#homedoseSatMeth_"+rand).removeAttr("disabled");
			} else {
				jQuery("#drkSat_"+rand).attr("disabled","disabled");
				jQuery("#drkSat_"+rand).removeAttr("checked");
				jQuery("#homedoseSatMeth_"+rand).attr("disabled", "disabled");
				jQuery("#homedoseSatMeth_"+rand).removeAttr("checked");
			}
			break;
		case 0:
			if (start <= end) {
				jQuery("#drkSun_"+rand).removeAttr("disabled");
				jQuery("#homedoseSunMeth_"+rand).removeAttr("disabled");
			} else {
				jQuery("#drkSun_"+rand).attr("disabled","disabled");
				jQuery("#drkSun_"+rand).removeAttr("checked");
				jQuery("#homedoseSunMeth_"+rand).attr("disabled", "disabled");
				jQuery("#homedoseSunMeth_"+rand).removeAttr("checked");
			}
			break;
		}
		start.setDate(start.getDate() + 1);
	}
	
};
/**
 * noCarryDays and carryDays are not null indicating it's re-prescribe.
 */
defaultCheckMethadoneCarryDays = function(rand, noCarryDays, carryDays) { // noCarryDays: takes med in clinic  carryDays: takes meds at home
	if (!jQuery("#drkMon_"+rand).attr("disabled")) {
		if (noCarryDays == null || noCarryDays.contains("MON")) {
			jQuery("#drkMon_"+rand).attr("checked", "checked");
		}
	}
	if (!jQuery("#drkTues_"+rand).attr("disabled")) {
		if (noCarryDays == null || noCarryDays.contains("TUES")) {
			jQuery("#drkTues_"+rand).attr("checked", "checked");
		}
	}
	if (!jQuery("#drkWed_"+rand).attr("disabled")) {
		if (noCarryDays == null || noCarryDays.contains("WED")) {
			jQuery("#drkWed_"+rand).attr("checked", "checked");
		}
	}
	if (!jQuery("#drkThurs_"+rand).attr("disabled")) {
		if (noCarryDays == null || noCarryDays.contains("THURS")) {
			jQuery("#drkThurs_"+rand).attr("checked", "checked");
		}
	}
	if (!jQuery("#drkFri_"+rand).attr("disabled")) {
		if (noCarryDays == null || noCarryDays.contains("FRI")) {
			jQuery("#drkFri_"+rand).attr("checked", "checked");
		}
	}
	if (!jQuery("#drkSat_"+rand).attr("disabled")) {
		if (noCarryDays == null || noCarryDays.contains("SAT")) {
			jQuery("#drkSat_"+rand).attr("checked", "checked");
		}
	}
	if (!jQuery("#drkSun_"+rand).attr("disabled")) {
		if (noCarryDays == null || noCarryDays.contains("SUN")) {
			jQuery("#drkSun_"+rand).attr("checked", "checked");
		}
	}
	jQuery("#drkCarr_"+rand).attr("checked", "checked");
	
	if (!jQuery("#homedoseMonMeth_"+rand).attr("disabled")
			&& carryDays != null && carryDays.contains("MON")) {
		jQuery("#homedoseMonMeth_"+rand).attr("checked","checked");
	}
	if (!jQuery("#homedoseTuesMeth_"+rand).attr("disabled")
			&& carryDays != null && carryDays.contains("TUES")) {
		jQuery("#homedoseTuesMeth_"+rand).attr("checked","checked");
	}
	if (!jQuery("#homedoseWedMeth_"+rand).attr("disabled")
			&& carryDays != null && carryDays.contains("WED")) {
		jQuery("#homedoseWedMeth_"+rand).attr("checked","checked");
	}
	if (!jQuery("#homedoseThursMeth_"+rand).attr("disabled")
			&& carryDays != null && carryDays.contains("THURS")) {
		jQuery("#homedoseThursMeth_"+rand).attr("checked","checked");
	}
	if (!jQuery("#homedoseFriMeth_"+rand).attr("disabled")
			&& carryDays != null && carryDays.contains("FRI")) {
		jQuery("#homedoseFriMeth_"+rand).attr("checked","checked");
	}
	if (!jQuery("#homedoseSatMeth_"+rand).attr("disabled")
			&& carryDays != null && carryDays.contains("SAT")) {
		jQuery("#homedoseSatMeth_"+rand).attr("checked","checked");
	}
	if (!jQuery("#homedoseSunMeth_"+rand).attr("disabled")
			&& carryDays != null && carryDays.contains("SUN")) {
		jQuery("#homedoseSunMeth_"+rand).attr("checked","checked");
	}
	if (carryDays != null && carryDays != "NONE") {
		jQuery("#homedoseNoMeth_"+rand).attr("checked", "checked");
	}
}

defaultCheckSuboxoneCarryDays = function(rand, noCarryDays, carryDays) { // noCarryDays: takes med in clinic  carryDays: takes meds at home
	if (!jQuery("#doseMonSub_"+rand).attr("disabled")) {
		if (noCarryDays == null || noCarryDays.contains("MON")) {
			jQuery("#doseMonSub_"+rand).attr("checked", "checked");
		}
	}
	if (!jQuery("#doseTuesSub_"+rand).attr("disabled")) {
		if (noCarryDays == null || noCarryDays.contains("TUES")) {
			jQuery("#doseTuesSub_"+rand).attr("checked", "checked");
		}
	}
	if (!jQuery("#doseWedSub_"+rand).attr("disabled")) {
		if (noCarryDays == null || noCarryDays.contains("WED")) {
			jQuery("#doseWedSub_"+rand).attr("checked", "checked");
		}
	}
	if (!jQuery("#doseThursSub_"+rand).attr("disabled")) {
		if (noCarryDays == null || noCarryDays.contains("THURS")) {
			jQuery("#doseThursSub_"+rand).attr("checked", "checked");
		}
	}
	if (!jQuery("#doseFriSub_"+rand).attr("disabled")) {
		if (noCarryDays == null || noCarryDays.contains("FRI")) {
			jQuery("#doseFriSub_"+rand).attr("checked", "checked");
		}
	}
	if (!jQuery("#doseSatSub_"+rand).attr("disabled")) {
		if (noCarryDays == null || noCarryDays.contains("SAT")) {
			jQuery("#doseSatSub_"+rand).attr("checked", "checked");
		}
	}
	if (!jQuery("#doseSunSub_"+rand).attr("disabled")) {
		if (noCarryDays == null || noCarryDays.contains("SUN")) {
			jQuery("#doseSunSub_"+rand).attr("checked", "checked");
		}
	}
	jQuery("#doseCarrSub_"+rand).attr("checked", "checked");
	
	if (!jQuery("#homedoseMonSub_"+rand).attr("disabled")
			&& carryDays != null && carryDays.contains("MON")) {
		jQuery("#homedoseMonSub_"+rand).attr("checked", "checked");
	}
	if (!jQuery("#homedoseTuesSub_"+rand).attr("disabled")
			&& carryDays != null && carryDays.contains("TUES")) {
		jQuery("#homedoseTuesSub_"+rand).attr("checked", "checked");
	}
	if (!jQuery("#homedoseWedSub_"+rand).attr("disabled")
			&& carryDays != null && carryDays.contains("WED")) {
		jQuery("#homedoseWedSub_"+rand).attr("checked", "checked");
	}
	if (!jQuery("#homedoseThursSub_"+rand).attr("disabled")
			&& carryDays != null && carryDays.contains("THURS")) {
		jQuery("#homedoseThursSub_"+rand).attr("checked", "checked");
	}
	if (!jQuery("#homedoseFriSub_"+rand).attr("disabled")
			&& carryDays != null && carryDays.contains("FRI")) {
		jQuery("#homedoseFriSub_"+rand).attr("checked", "checked");
	}
	if (!jQuery("#homedoseSatSub_"+rand).attr("disabled")
			&& carryDays != null && carryDays.contains("SAT")) {
		jQuery("#homedoseSatSub_"+rand).attr("checked", "checked");
	}
	if (!jQuery("#homedoseSunSub_"+rand).attr("disabled")
			&& carryDays != null && carryDays.contains("SUN")) {
		jQuery("#homedoseSunSub_"+rand).attr("checked", "checked");
	}
	if (carryDays != null && carryDays != "NONE") {
		jQuery("#homedoseNoSub_"+rand).attr("checked", "checked");
	}
}

disabledSuboxoneDays = function(rand) {
	var start = convertToDate(jQuery("#suboxoneStartDate_"+rand).val());
	var end = convertToDate(jQuery("#suboxoneEndDate_"+rand).val());
	if (start > end) {
		alert("End date should be bigger than Start Date!");
		return;
	}
	var i;
	for (i=0; i<7; i++) {
		var day = start.getDay();
		switch(day) {
		case 1:
			if (start <= end) {
				jQuery("#doseMonSub_"+rand).removeAttr("disabled");
				jQuery("#homedoseMonSub_"+rand).removeAttr("disabled");
			} else {
				jQuery("#doseMonSub_"+rand).removeAttr("checked");
				jQuery("#doseMonSub_"+rand).attr("disabled", "disabled");
				jQuery("#homedoseMonSub_"+rand).removeAttr("checked");
				jQuery("#homedoseMonSub_"+rand).attr("disabled", "disabled");
			}
			break;
		case 2:
			if (start <= end) {
				jQuery("#doseTuesSub_"+rand).removeAttr("disabled");
				jQuery("#homedoseTuesSub_"+rand).removeAttr("disabled");
			} else {
				jQuery("#doseTuesSub_"+rand).removeAttr("checked");
				jQuery("#doseTuesSub_"+rand).attr("disabled", "disabled");
				jQuery("#homedoseTuesSub_"+rand).removeAttr("checked");
				jQuery("#homedoseTuesSub_"+rand).attr("disabled", "disabled");
			}
			break;
		case 3:
			if (start <= end) {
				jQuery("#doseWedSub_"+rand).removeAttr("disabled");
				jQuery("#homedoseWedSub_"+rand).removeAttr("disabled");
			} else {
				jQuery("#doseWedSub_"+rand).removeAttr("checked");
				jQuery("#doseWedSub_"+rand).attr("disabled", "disabled");
				jQuery("#homedoseWedSub_"+rand).removeAttr("checked");
				jQuery("#homedoseWedSub_"+rand).attr("disabled", "disabled");
			}
			break;
		case 4:
			if (start <= end) {
				jQuery("#doseThursSub_"+rand).removeAttr("disabled");
				jQuery("#homedoseThursSub_"+rand).removeAttr("disabled");
			} else {
				jQuery("#doseThursSub_"+rand).removeAttr("checked");
				jQuery("#doseThursSub_"+rand).attr("disabled", "disabled");
				jQuery("#homedoseThursSub_"+rand).removeAttr("checked");
				jQuery("#homedoseThursSub_"+rand).attr("disabled", "disabled");
			}
			break;
		case 5:
			if (start <= end) {
				jQuery("#doseFriSub_"+rand).removeAttr("disabled");
				jQuery("#homedoseFriSub_"+rand).removeAttr("disabled");
			} else {
				jQuery("#doseFriSub_"+rand).removeAttr("checked");
				jQuery("#doseFriSub_"+rand).attr("disabled", "disabled");
				jQuery("#homedoseFriSub_"+rand).removeAttr("checked");
				jQuery("#homedoseFriSub_"+rand).attr("disabled", "disabled");
			}
			break;
		case 6:
			if (start <= end) {
				jQuery("#doseSatSub_"+rand).removeAttr("disabled");
				jQuery("#homedoseSatSub_"+rand).removeAttr("disabled");
			} else {
				jQuery("#doseSatSub_"+rand).removeAttr("checked");
				jQuery("#doseSatSub_"+rand).attr("disabled", "disabled");
				jQuery("#homedoseSatSub_"+rand).removeAttr("checked");
				jQuery("#homedoseSatSub_"+rand).attr("disabled", "disabled");
			}
			break;
		case 0:
			if (start <= end) {
				jQuery("#doseSunSub_"+rand).removeAttr("disabled");
				jQuery("#homedoseSunSub_"+rand).removeAttr("disabled");
			} else {
				jQuery("#doseSunSub_"+rand).removeAttr("checked");
				jQuery("#doseSunSub_"+rand).attr("disabled", "disabled");
				jQuery("#homedoseSunSub_"+rand).removeAttr("checked");
				jQuery("#homedoseSunSub_"+rand).attr("disabled", "disabled");
			}
			break;
		}
		start.setDate(start.getDate() + 1);
	}
	
};

</script>
<%}%>
<fieldset style="margin-top:2px;width:640px;padding-right: 30px;" id="set_<%=rand%>">
    <a tabindex="-1" href="javascript:void(0);"  style="float:right;margin-left:5px;margin-top:0px;padding-top:0px;" onclick="$('set_<%=rand%>').remove();deletePrescribe('<%=rand%>');removeReRxDrugId('<%=DrugReferenceId%>')">X</a>
    <a tabindex="-1" href="javascript:void(0);"  style="float:right;;margin-left:5px;margin-top:0px;padding-top:0px;" title="Add to Favorites" onclick="addFav('<%=rand%>','<%=drugName%>');return false;">F</a>
    <a tabindex="-1" href="javascript:void(0);" style="float:right;margin-top:0px;padding-top:0px;" onclick="$('rx_more_<%=rand%>').toggle();">  <span id="moreLessWord_<%=rand%>" onclick="updateMoreLess(id)" >more</span> </a>

    <label style="float:left;width:80px;" title="<%=ATC%>" >Name:</label>
    <input type="hidden" name="atcCode" value="<%=ATCcode%>" />
    <input tabindex="-1" type="text" id="drugName_<%=rand%>"  name="drugName_<%=rand%>"  size="60" <%if(gcn==0){%> onkeyup="saveCustomName(this);" value="<%=drugName%>"<%} else{%> value='<%=drugName%>'  onchange="changeDrugName('<%=rand%>','<%=drugName%>');" <%}%>/><span id="alleg_<%=rand%>" style="color:red;"></span>&nbsp;&nbsp;<span id="inactive_<%=rand%>" style="color:red;"></span><br>
    <%if (OscarProperties.getInstance().isPropertyActive("enable_rx_custom_methodone_suboxone")) { %>
    <div id="instructionsDiv_<%=rand %>" style="display: inline;">
    <%} %>
    <a tabindex="-1" href="javascript:void(0);" onclick="showHideSpecInst('siAutoComplete_<%=rand%>')" style="float:left;width:80px;">Instructions:</a>
    <%if (!OscarProperties.getInstance().isPropertyActive("enable_rx_custom_methodone_suboxone")){%>
    <input type="text" id="instructions_<%=rand%>" name="instructions_<%=rand%>" onkeypress="handleEnter(this,event);" value="<%=instructions%>" size="60" onchange="parseIntr(this);" /><a href="javascript:void(0);" tabindex="-1" onclick="displayMedHistory('<%=rand%>');" style="color:red;font-size:13pt;vertical-align:super;text-decoration:none" ><b>*</b></a>  <a href="javascript:void(0);" tabindex="-1" onclick="displayInstructions('<%=rand%>');"><img src="<c:out value="${ctx}/images/icon_help_sml.gif"/>" border="0" TITLE="Instructions Field Reference"></a> 
    <%}else{%>
    <input type="text" id="instructions_<%=rand%>" name="instructions_<%=rand%>" onkeypress="handleEnter(this,event);" value="<%=instructions%>" size="60" onchange="parseIntr(this);" /> 
    <a href="javascript:void(0);" tabindex="-1" onclick="displayMedHistory('<%=rand%>');" style="color:red;font-size:13pt;vertical-align:super;text-decoration:none" ><b>*</b></a>  
    <a href="javascript:void(0);" tabindex="-1" onclick="displayInstructions('<%=rand%>');"><img src="<c:out value="${ctx}/images/icon_help_sml.gif"/>" border="0" TITLE="Instructions Field Reference"></a>
    </div>
    <%
    CaseManagementManager caseManagementManager = (CaseManagementManager) SpringUtils.getBean("caseManagementManager");
    Date lastEndDate = null;
    Drug lastActiveDrug = null;
    
    Date today = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    methToday = sdf.format(today);
    subToday = methToday;
    methEnddate=methToday;
    subEnddate=subToday;
    Calendar cal=Calendar.getInstance();  
    cal.setTime(today);
    cal.add(Calendar.DAY_OF_MONTH, 6);
    methEnddate = sdf.format(cal.getTime());
    subEnddate = methEnddate;
    
    if (drugName != null && drugName.toLowerCase().contains("methadone")) {
    	flag = 2;
    	lastEndDate = caseManagementManager.getLastEndDateForCustomRx(patient.getDemographicNo(), "methadone");
    	if (action.equals("represcribe")) {
    		lastActiveDrug = caseManagementManager.getLastRxForCustomRx(patient.getDemographicNo(), "methadone");
    	}
    	Calendar calendar=Calendar.getInstance();  
    	calendar.setTime((lastEndDate==null)?new Date():lastEndDate); 
    	calendar.add(Calendar.DAY_OF_MONTH, 1);
    	lastEndDate = calendar.getTime();
    	if (lastEndDate != null && lastEndDate.after(today)) {
    		methToday = sdf.format(lastEndDate);
    		calendar.add(Calendar.DAY_OF_MONTH, 6);
    		methEnddate = sdf.format(calendar.getTime());
    	} 
    	
    } else if (drugName != null && (drugName.toLowerCase().contains("suboxone") || drugName.toLowerCase().contains("buprenorphine"))) {
    	flag = 3;
    	lastEndDate = caseManagementManager.getLastEndDateForCustomRx(patient.getDemographicNo(), "suboxone");
    	if (action.equals("represcribe")) {
    		lastActiveDrug = caseManagementManager.getLastRxForCustomRx(patient.getDemographicNo(), "suboxone");
    	}
    	Calendar calendar=Calendar.getInstance();  
    	calendar.setTime((lastEndDate==null)?new Date():lastEndDate); 
    	calendar.add(Calendar.DAY_OF_MONTH, 1);
    	lastEndDate = calendar.getTime();
    	if (lastEndDate!=null && lastEndDate.after(today)) {
    		subToday = sdf.format(lastEndDate);
    		calendar.add(Calendar.DAY_OF_MONTH, 6);
    		subEnddate = sdf.format(calendar.getTime());
    	}
    }
    
    if (lastActiveDrug != null && lastActiveDrug.getComment() != null) {
    	String days[] = lastActiveDrug.getComment().split(";");
    	if (days.length == 3) {
    		noCarryDays = days[0];
    		carryDays = days[1];
    		carryLevel = days[2];
    	}
    }
    
    %>
    <div style="display: inline-block; float: right; margin-right: -30px;">
		<input type="radio" value="1" name="rxModules_<%=rand %>" 
		<%if (flag == 1) { %>checked="checked"<%} %>>REGULAR<br>
		<input type="radio" value="2" name="rxModules_<%=rand %>" <%if (flag == 2) { %>checked="checked"<%} %>>METHADONE<br>
		<input type="radio" value="3" name="rxModules_<%=rand %>" <%if (flag == 3) { %>checked="checked"<%} %>>SUBOXONE
	</div>
	<%}%>
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

		<%if (flag == 1) {%>
        <label id="labelQuantity_<%=rand%>"  style="float:left;width:80px;">Qty/Mitte:</label><input size="8"<%if(rx.isCustomNote()){%> disabled <%}%> type="text" id="quantity_<%=rand%>"    size="10" name="quantity_<%=rand%>"     value="<%=quantityText%>" onblur="updateQty(this);" />
        <label style="">Units:</label><input type="text" id="dispensingUnits_<%=rand%>"  <%if(rx.isCustomNote()){%> disabled <%}%>    name="dispensingUnits_<%=rand%>"   value="<%=repeats%>" />
        <label style="">Repeats:</label><input type="text" id="repeats_<%=rand%>" size="5"  <%if(rx.isCustomNote()){%> disabled <%}%>    name="repeats_<%=rand%>"   value="<%=repeats%>" />
        <%} else {%>
        <input type="hidden" id="repeats_<%=rand%>"  <%if(rx.isCustomNote()){%> disabled <%}%>    name="repeats_<%=rand%>"   value="0" />
        <input <%if(rx.isCustomNote()){%> disabled <%}%> type="hidden" id="quantity_<%=rand%>" name="quantity_<%=rand%>" value="<%=quantityText%>" onblur="updateQty(this);" value="0"/>
        <%} %>

        <span id="medTerm_<%=rand%>">
        <input type="checkbox" id="longTerm_<%=rand%>"  name="longTerm_<%=rand%>" class="med-term" <%if(longTerm) {%> checked="true" <%}%> />Long Term Med
        <input type="checkbox" id="shortTerm_<%=rand%>"  name="shortTerm_<%=rand%>" class="med-term" <%if(shortTerm) {%> checked="true" <%}%> />Short Term Med
		</span>
        
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
           <a tabindex="-1" >Units:</a><a tabindex="-1" id="dispensingUnitsStr_<%=rand%>"> <%=dispensingUnitsStr%></a>
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
       	  <%if(OscarProperties.getInstance().getProperty("rx.enable_internal_dispensing","false").equals("true")) {%>
			  <bean:message key="WriteScript.msgDispenseInternal"/>	
			  <input type="checkbox" name="dispenseInternal_<%=rand%>" id="dispenseInternal_<%=rand%>" <%if(dispenseInternal) {%> checked="true" <%}%> />
			 <br/>
			<% } %>
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
           <a href="javascript:void(0);" style="float:right;margin-top:0px;padding-top:0px;"  title="Add to Favorites" onclick="addFav('<%=rand%>','<%=drugName%>');return false;">Add to Favorite</a>
       </div>
       <%if (OscarProperties.getInstance().isPropertyActive("enable_rx_custom_methodone_suboxone")){%>
       <br/>
       <div id="rxRegularDiv_<%=rand %>" <%if (flag == 1) { %>style="display: inline"<%} else { %> style="display: none" <%}%> >
        <bean:message key="WriteScript.msgPickUpDate"/>: 
           <input type="text" id="pickupDate_<%=rand%>"  name="pickupDate_<%=rand%>" value="<%=pickupDate%>" onchange="if (!isValidDate(this.value)) {this.value=null}" />
           <bean:message key="WriteScript.msgPickUpTime"/>: 
           <input type="text" id="pickupTime_<%=rand%>"  name="pickupTime_<%=rand%>" value="<%=pickupTime%>" onchange="if (!isValidTime(this.value)) {this.value=null}" />
       <br/>
       <%}%>
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
	    <%if (OscarProperties.getInstance().isPropertyActive("enable_rx_custom_methodone_suboxone")){%>
            </div>
            <div id="rxMethadoneDiv_<%=rand %>" <%if (flag == 2) { %>style="display: inline"<%} else { %> style="display: none" <%}%>>
             Start Date:<input type="text" name="methadoneStartDate_<%=rand %>" id="methadoneStartDate_<%=rand %>" value="<%=methToday %>" readonly onchange="disabledMethadoneDays('<%=rand %>')">
             <img src="<%=request.getContextPath() %>/images/cal.gif" id="methadoneStartDate_cal_<%=rand %>">
             
             End Date:<input type="text" name="methdoneEndDate_<%=rand %>" id="methdoneEndDate_<%=rand %>" value="<%=methEnddate %>" readonly onchange="disabledMethadoneDays('<%=rand %>')">
             <img src="<%=request.getContextPath() %>/images/cal.gif" id="methdoneEndDate_cal_<%=rand %>">
             <script type="text/javascript">
             Calendar.setup({ inputField : "methadoneStartDate_<%=rand %>", ifFormat : "%Y-%m-%d", showsTime :false, button : "methadoneStartDate_cal_<%=rand %>", singleClick : true, step : 1 });
             Calendar.setup({ inputField : "methdoneEndDate_<%=rand %>", ifFormat : "%Y-%m-%d", showsTime :false, button : "methdoneEndDate_cal_<%=rand %>", singleClick : true, step : 1 });
             disabledMethadoneDays("<%=rand%>");
             <%if (noCarryDays != null && carryDays != null) {%>
             defaultCheckMethadoneCarryDays("<%=rand%>", "<%=noCarryDays%>", "<%=carryDays%>");
             <%} else {%>
             defaultCheckMethadoneCarryDays("<%=rand%>");
             <%}%>
             </script>
             <br>
             <b><u>Drink observed</u> in the pharmacy on days:<br></b>
             &nbsp;&nbsp;&nbsp;&nbsp;
             <input type="checkbox" name="drkMon_<%=rand %>" disabled="disabled" id="drkMon_<%=rand %>">MON
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="drkTues_<%=rand %>" id="drkTues_<%=rand %>" disabled="disabled">TUES
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="drkWed_<%=rand %>" id="drkWed_<%=rand %>" disabled="disabled">WED
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="drkThurs_<%=rand %>" id="drkThurs_<%=rand %>" disabled="disabled">THURS
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="drkFri_<%=rand %>" id="drkFri_<%=rand %>" disabled="disabled">FRI
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="drkSat_<%=rand %>" id="drkSat_<%=rand %>" disabled="disabled">SAT
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="drkSun_<%=rand %>" id="drkSun_<%=rand %>" disabled="disabled">SUN
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="drkCarr_<%=rand %>" id="drkCarr_<%=rand %>" onclick="carryMethdone(this);">NO CARRIES
             <br>
             <b>The following days are to be dispensed <u>as take home doses:</u></b><br>
             &nbsp;&nbsp;&nbsp;&nbsp;
             <input type="checkbox" name="homedoseMonMeth_<%=rand %>" id="homedoseMonMeth_<%=rand %>" disabled="disabled">MON
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="homedoseTuesMeth_<%=rand %>" id="homedoseTuesMeth_<%=rand %>" disabled="disabled">TUES
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="homedoseWedMeth_<%=rand %>" id="homedoseWedMeth_<%=rand %>" disabled="disabled">WED
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="homedoseThursMeth_<%=rand %>" id="homedoseThursMeth_<%=rand %>" disabled="disabled">THURS
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="homedoseFriMeth_<%=rand %>" id="homedoseFriMeth_<%=rand %>" disabled="disabled">FRI
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="homedoseSatMeth_<%=rand %>" id="homedoseSatMeth_<%=rand %>" disabled="disabled">SAT
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="homedoseSunMeth_<%=rand %>" id="homedoseSunMeth_<%=rand %>" disabled="disabled">SUN
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="homedoseNoMeth_<%=rand %>" id="homedoseNoMeth_<%=rand %>">CARRY
             <br>
             &nbsp;&nbsp;&nbsp;&nbsp;Hold prescription if 3 consecutive doses missed or <br>
             if dosage change exceeds <input type="text" name="excQtyMeth_<%=rand %>" id="excQtyMeth_<%=rand %>" size="2" value="15mg" readonly> from previous prescription.<br>
              &nbsp;&nbsp;&nbsp;&nbsp;Notify physician if dose is missed.
              <br>Carry Level:&nbsp;&nbsp; <input type="text" size="2" name="carryLevelMeth_<%=rand %>" id="carryLevelMeth_<%=rand %>" value="<%=carryLevel%>"> &nbsp;(Normally, it is 0-6)
		</div>
		<div id="rxSuboxoneDiv_<%=rand %>" <%if (flag == 3) { %>style="display: inline"<%} else { %> style="display: none" <%}%>>
			Start Date:<input type="text" name="suboxoneStartDate_<%=rand %>" id="suboxoneStartDate_<%=rand %>" value="<%=subToday %>" readonly onchange="disabledSuboxoneDays('<%=rand%>');">
			<img src="<%=request.getContextPath() %>/images/cal.gif" id="suboxoneStartDate_cal_<%=rand %>">
            End Date:<input type="text" name="suboxoneEndDate_<%=rand %>" id="suboxoneEndDate_<%=rand %>"  value="<%=subEnddate %>" readonly onchange="disabledSuboxoneDays('<%=rand%>');">
            <img src="<%=request.getContextPath() %>/images/cal.gif" id="suboxoneEndDate_cal_<%=rand %>">
             <script type="text/javascript">
             Calendar.setup({ inputField : "suboxoneStartDate_<%=rand %>", ifFormat : "%Y-%m-%d", showsTime :false, button : "suboxoneStartDate_cal_<%=rand %>", singleClick : true, step : 1 });
             Calendar.setup({ inputField : "suboxoneEndDate_<%=rand %>", ifFormat : "%Y-%m-%d", showsTime :false, button : "suboxoneEndDate_cal_<%=rand %>", singleClick : true, step : 1 });
             disabledSuboxoneDays("<%=rand%>");
             <%if (noCarryDays != null && carryDays != null) {%>
             defaultCheckSuboxoneCarryDays("<%=rand%>", "<%=noCarryDays%>", "<%=carryDays%>");
             <%} else {%>
             defaultCheckSuboxoneCarryDays("<%=rand%>");
             <%}%>
             </script>
             <br>
             <b><u>Dose observed</u> in the pharmacy on days:<br></b>
             &nbsp;&nbsp;&nbsp;&nbsp;
             <input type="checkbox" name="doseMonSub_<%=rand %>" id="doseMonSub_<%=rand %>" disabled="disabled">MON
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="doseTuesSub_<%=rand %>" id="doseTuesSub_<%=rand %>" disabled="disabled">TUES
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="doseWedSub_<%=rand %>" id="doseWedSub_<%=rand %>" disabled="disabled">WED
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="doseThursSub_<%=rand %>" id="doseThursSub_<%=rand %>" disabled="disabled">THURS
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="doseFriSub_<%=rand %>" id="doseFriSub_<%=rand %>" disabled="disabled">FRI
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="doseSatSub_<%=rand %>" id="doseSatSub_<%=rand %>" disabled="disabled">SAT
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="doseSunSub_<%=rand %>" id="doseSunSub_<%=rand %>" disabled="disabled">SUN
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="doseCarrSub_<%=rand %>" id="doseCarrSub_<%=rand %>" onclick="carrySuboxone(this);">NO CARRIES
             <br>
             <b>The following days are to be dispensed <u>as take home doses:</u></b><br>
             &nbsp;&nbsp;&nbsp;&nbsp;
             <input type="checkbox" name="homedoseMonSub_<%=rand %>" id="homedoseMonSub_<%=rand %>" disabled="disabled">MON
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="homedoseTuesSub_<%=rand %>" id="homedoseTuesSub_<%=rand %>" disabled="disabled">TUES
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="homedoseWedSub_<%=rand %>" id="homedoseWedSub_<%=rand %>" disabled="disabled">WED
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="homedoseThursSub_<%=rand %>" id="homedoseThursSub_<%=rand %>" disabled="disabled">THURS
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="homedoseFriSub_<%=rand %>" id="homedoseFriSub_<%=rand %>" disabled="disabled">FRI
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="homedoseSatSub_<%=rand %>" id="homedoseSatSub_<%=rand %>" disabled="disabled">SAT
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="homedoseSunSub_<%=rand %>" id="homedoseSunSub_<%=rand %>" disabled="disabled">SUN
             &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="homedoseNoSub_<%=rand %>" id="homedoseNoSub_<%=rand %>">CARRY
             <br>
             &nbsp;&nbsp;&nbsp;&nbsp;Hold prescription if 3 consecutive doses missed or <br>
             if dosage change exceeds <input type="text" name="excQtySub_<%=rand %>" id="excQtySub_<%=rand %>" size="2" value="4mg" readonly> from previous prescription.<br>
              &nbsp;&nbsp;&nbsp;&nbsp;Notify physician if dose is missed.
               <br>Carry Level:&nbsp;&nbsp; <input type="text" size="2" name="carryLevelSub_<%=rand %>" id="carryLevelSub_<%=rand %>" value="<%=carryLevel%>"> &nbsp;(Normally, it is 0-6)
		</div>
    	<%}%>
</fieldset>
<%}else{%>
<fieldset style="margin-top:2px;width:640px;" id="set_<%=rand%>">
    <a tabindex="-1" href="javascript:void(0);"  style="float:right;margin-left:5px;margin-top:0px;padding-top:0px;" onclick="$('set_<%=rand%>').remove();deletePrescribe('<%=rand%>');removeReRxDrugId('<%=DrugReferenceId%>')"><img src='<c:out value="${ctx}/images/close.png"/>' border="0"></a>
    <a tabindex="-1" href="javascript:void(0);"  style="float:right;;margin-left:5px;margin-top:0px;padding-top:0px;" title="Add to Favorites" onclick="addFav('<%=rand%>','<%=drugName%>')">F</a>
    <a tabindex="-1" href="javascript:void(0);" style="float:right;margin-top:0px;padding-top:0px;" onclick="$('rx_more_<%=rand%>').toggle();">  <span id="moreLessWord_<%=rand%>" onclick="updateMoreLess(id)" >more</span> </a>

    <label style="float:left;width:80px;" title="<%=ATC%>" >Name:</label>
    <input type="hidden" name="atcCode" value="<%=ATCcode%>" />
    <input tabindex="-1" type="text" id="drugName_<%=rand%>"  name="drugName_<%=rand%>"  size="30" <%if(gcn==0){%> onkeyup="saveCustomName(this);" value="<%=drugName%>"<%} else{%> value='<%=drugName%>'  onchange="changeDrugName('<%=rand%>','<%=drugName%>');" <%}%> TITLE="<%=drugName%>"/>&nbsp;<span id="inactive_<%=rand%>" style="color:red;"></span>

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
	    
    <%-- Splice in the Indication field --%>
<br />
	<label style="float:left;width:80px;" for="jsonDxSearch_<%=rand%>" >Indication</label>
		<select name="codingSystem_<%=rand%>" id="codingSystem_<%=rand%>" >
		<option value="icd9">icd9</option>
		<%-- option value="limitUse">Limited Use</option --%>
	</select>
	<input type="hidden" name="reasonCode_<%=rand%>" id="codeTxt_<%=rand%>" />
	<input type="text" class="codeTxt" name="jsonDxSearch_<%=rand%>" id="jsonDxSearch_<%=rand%>" placeholder="Search Dx" />
<br />
     <%-- Splice in the Indication field --%>
     
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

        <label id="labelQuantity_<%=rand%>"  style="float:left;width:80px;">Qty/Mitte:</label><input size="8" <%if(rx.isCustomNote()){%> disabled <%}%> type="text" id="quantity_<%=rand%>"     size="10" name="quantity_<%=rand%>"     value="<%=quantityText%>" onblur="updateQty(this);" />
        <label style="">Units:</label><input type="text" size="5" id="dispensingUnits_<%=rand%>"  <%if(rx.isCustomNote()){%> disabled <%}%>    name="dispensingUnits_<%=rand%>"   value="<%=repeats%>" />
        <label style="">Repeats:</label><input type="text" size="5" id="repeats_<%=rand%>"  <%if(rx.isCustomNote()){%> disabled <%}%>    name="repeats_<%=rand%>"   value="<%=repeats%>" />

		<span id="medTerm_<%=rand%>">
        <input  type="checkbox" id="longTerm_<%=rand%>"  name="longTerm_<%=rand%>" class="med-term" <%if(longTerm) {%> checked="true" <%}%> />Long Term Med
        <input  type="checkbox" id="shortTerm_<%=rand%>"  name="shortTerm_<%=rand%>" class="med-term" <%if(shortTerm) {%> checked="true" <%}%> />Short Term Med
		</span>
        
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
           <a tabindex="-1" >Units:</a><a tabindex="-1" id="dispensingUnitsStr_<%=rand%>"> <%=dispensingUnitsStr%></a>
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
       	  
	     <%if(OscarProperties.getInstance().getProperty("rx.enable_internal_dispensing","false").equals("true")) {%>  
	       	   <bean:message key="WriteScript.msgDispenseInternal"/>	
			  <input type="checkbox" name="dispenseInternal_<%=rand%>" id="dispenseInternal_<%=rand%>" <%if(dispenseInternal) {%> checked="true" <%}%> />
      	 <br/>
      	 <% } %>

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
           <a href="javascript:void(0);" style="float:right;margin-top:0px;padding-top:0px;" onclick="addFav('<%=rand%>','<%=drugName%>');return false;">Add to Favorite</a>
       
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
<style type="text/css" >


/*
 * jQuery UI Autocomplete 1.8.18
 *
 * Copyright 2011, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Autocomplete#theming
 */
.ui-autocomplete { position: absolute; cursor: default; }	

/* workarounds */
* html .ui-autocomplete { width:1px; } /* without this, the menu expands to 100% in IE6 */

/*
 * jQuery UI Menu 1.8.18
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Menu#theming
 */
.ui-menu {
	list-style:none;
	padding: 2px;
	margin: 0;
	display:block;
	float: left;
}
.ui-menu .ui-menu {
	margin-top: -3px;
}
.ui-menu .ui-menu-item {
	margin:0;
	padding: 0;
	zoom: 1;
	float: left;
	clear: left;
	width: 100%;
}
.ui-menu .ui-menu-item a {
	text-decoration:none;
	display:block;
	padding:.2em .4em;
	line-height:1.5;
	zoom:1;
}
.ui-menu .ui-menu-item a.ui-state-hover,
.ui-menu .ui-menu-item a.ui-state-active {
	font-weight: normal;
	margin: -1px;
}


	.ui-autocomplete-loading { 
		background: white url('../images/ui-anim_basic_16x16.gif') right center no-repeat; 
	} 
	.ui-autocomplete {
		max-height: 200px;
		overflow-y: auto;
		overflow-x: hidden;
		background-color: whitesmoke;
			border:#ccc thin solid;
	}

	.ui-menu .ui-menu {
	
		background-color: whitesmoke;
	}
	
	.ui-menu .ui-menu-item a {
		border-bottom:white thin solid;
	}
	.ui-menu .ui-menu-item a.ui-state-hover,
	.ui-menu .ui-menu-item a.ui-state-active {
		background-color: yellow;
	}

</style>

<script type="text/javascript">
<%if (OscarProperties.getInstance().isPropertyActive("enable_rx_custom_methodone_suboxone")){%>
getRand = function(which) {
	if (which == null) {
		return -1;
	}
	var start = which.id.indexOf("_");
	if (start == -1) {
		return -1;
	}
	return which.id.substring(start + 1);

};
carryMethdone = function(which) {
	var rand = getRand(which);
	if (rand == -1) {
		return;
	}
	if (which.checked) {
		// check all of options
		if (!jQuery("#drkMon_"+rand).attr("disabled")) {
			jQuery("#drkMon_"+rand).attr("checked", "checked");
		}
		if (!jQuery("#drkTues_"+rand).attr("disabled")) {
			jQuery("#drkTues_"+rand).attr("checked", "checked");
		}
		if (!jQuery("#drkWed_"+rand).attr("disabled")) {
			jQuery("#drkWed_"+rand).attr("checked", "checked");
		}
		if (!jQuery("#drkThurs_"+rand).attr("disabled")) {
			jQuery("#drkThurs_"+rand).attr("checked", "checked");
		}
		if (!jQuery("#drkFri_"+rand).attr("disabled")) {
			jQuery("#drkFri_"+rand).attr("checked", "checked");
		}
		if (!jQuery("#drkSat_"+rand).attr("disabled")) {
			jQuery("#drkSat_"+rand).attr("checked", "checked");
		}
		if (!jQuery("#drkSun_"+rand).attr("disabled")) {
			jQuery("#drkSun_"+rand).attr("checked", "checked");
		}
		jQuery("#homedoseMonMeth_"+rand).removeAttr("checked");
		jQuery("#homedoseTuesMeth_"+rand).removeAttr("checked");
		jQuery("#homedoseWedMeth_"+rand).removeAttr("checked");
		jQuery("#homedoseThursMeth_"+rand).removeAttr("checked");
		jQuery("#homedoseFriMeth_"+rand).removeAttr("checked");
		jQuery("#homedoseSatMeth_"+rand).removeAttr("checked");
		jQuery("#homedoseSunMeth_"+rand).removeAttr("checked");
		jQuery("#homedoseNoMeth_"+rand).removeAttr("checked");
	} else {
		// un-check all of options
		jQuery("#drkMon_"+rand).removeAttr("checked");
		jQuery("#drkTues_"+rand).removeAttr("checked");
		jQuery("#drkWed_"+rand).removeAttr("checked");
		jQuery("#drkThurs_"+rand).removeAttr("checked");
		jQuery("#drkFri_"+rand).removeAttr("checked");
		jQuery("#drkSat_"+rand).removeAttr("checked");
		jQuery("#drkSun_"+rand).removeAttr("checked");
	}
};

carrySuboxone = function(which) {
	var rand = getRand(which);
	if (rand == -1) {
		return;
	}
	if (which.checked) {
		// check all of options
		if (!jQuery("#doseMonSub_"+rand).attr("disabled")) {
			jQuery("#doseMonSub_"+rand).attr("checked", "checked");
		}
		if (!jQuery("#doseTuesSub_"+rand).attr("disabled")) {
			jQuery("#doseTuesSub_"+rand).attr("checked", "checked");
		}
		if (!jQuery("#doseWedSub_"+rand).attr("disabled")) {
			jQuery("#doseWedSub_"+rand).attr("checked", "checked");
		}
		if (!jQuery("#doseThursSub_"+rand).attr("disabled")) {
			jQuery("#doseThursSub_"+rand).attr("checked", "checked");
		}
		if (!jQuery("#doseFriSub_"+rand).attr("disabled")) {
			jQuery("#doseFriSub_"+rand).attr("checked", "checked");
		}
		if (!jQuery("#doseSatSub_"+rand).attr("disabled")) {
			jQuery("#doseSatSub_"+rand).attr("checked", "checked");
		}
		if (!jQuery("#doseSunSub_"+rand).attr("disabled")) {
			jQuery("#doseSunSub_"+rand).attr("checked", "checked");
		}
		jQuery("#homedoseMonSub_"+rand).removeAttr("checked");
		jQuery("#homedoseTuesSub_"+rand).removeAttr("checked");
		jQuery("#homedoseWedSub_"+rand).removeAttr("checked");
		jQuery("#homedoseThursSub_"+rand).removeAttr("checked");
		jQuery("#homedoseFriSub_"+rand).removeAttr("checked");
		jQuery("#homedoseSatSub_"+rand).removeAttr("checked");
		jQuery("#homedoseSunSub_"+rand).removeAttr("checked");
		jQuery("#homedoseNoSub_"+rand).removeAttr("checked");
	} else {
		// un-check all of options
		jQuery("#doseMonSub_"+rand).removeAttr("checked");
		jQuery("#doseTuesSub_"+rand).removeAttr("checked");
		jQuery("#doseWedSub_"+rand).removeAttr("checked");
		jQuery("#doseThursSub_"+rand).removeAttr("checked");
		jQuery("#doseFriSub_"+rand).removeAttr("checked");
		jQuery("#doseSatSub_"+rand).removeAttr("checked");
		jQuery("#doseSunSub_"+rand).removeAttr("checked");
	}
};
<%}%>
       jQuery("document").ready(function() {
    	   
               if ( jQuery.browser.msie ) {
                       jQuery('#rx_save_updates_<%=rand%>').show();
		       <%if (OscarProperties.getInstance().isPropertyActive("enable_rx_custom_methodone_suboxone")){%>
                       jQuery("input[name='rxModules']").on("click", function() {
                          	this.blur();
                          	this.focus();
                          });
			  <%}%>
               } else {
                       jQuery('#rx_save_updates_<%=rand%>').hide();
               }

				var idindex = "";
               jQuery( "input[id*='jsonDxSearch']" ).autocomplete({	
       			source: function(request, response) {
       				
       				var elementid = this.element[0].id;
	   				if( elementid.indexOf("_") > 0 ) {
	   					idindex = "_" + elementid.split("_")[1];
	   				}
       				       				
       				jQuery.ajax({
       				    url: ctx + "/dxCodeSearchJSON.do",
       				    type: 'POST',
       				    data: 'method=search' + ( jQuery( '#codingSystem' + idindex ).find(":selected").val() ).toUpperCase()
       				    				+ '&keyword=' 
       				    				+ jQuery( "#jsonDxSearch" + idindex ).val(),
       				  	dataType: "json",
       				    success: function(data) {
       						response(jQuery.map( data, function(item) { 
       							return {
       								label: item.description.trim() + ' (' + item.code + ')',
       								value: item.code,
       								id: item.id
       							};
       				    	}))
       				    }			    
       				})					  
       			},
       			delay: 100,
       			minLength: 2,
       			select: function( event, ui) {
       				event.preventDefault();
       				jQuery( "#jsonDxSearch" + idindex ).val(ui.item.label);
       				jQuery( '#codeTxt' + idindex ).val(ui.item.value);
       			},
       			focus: function(event, ui, idindex) {
       		        event.preventDefault();
       		        jQuery( "#jsonDxSearch" + idindex ).val(ui.item.label);
       		    },
       			open: function() {
       				jQuery( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
       			},
       			close: function() {
       				jQuery( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
       			}
       		})

            <%if (OscarProperties.getInstance().isPropertyActive("enable_rx_custom_methodone_suboxone")){%>
               jQuery("input[name^='rxModules']").change(function() {
                  	// get rand value
                  	var name = jQuery(this).attr("name").split("_");
                  	var idx = name[1];
                  	var checkIdx = jQuery("input[name='rxModules_"+idx+"']:checked").val();
                  	if (checkIdx == 1) {
                  		jQuery("#instructionsDiv_"+idx).css("display", "inline");
                  		jQuery("#rxRegularDiv_"+idx).css("display", "inline");
                  		jQuery("#rxMethadoneDiv_"+idx).css("display", "none");
                  		jQuery("#rxSuboxoneDiv_"+idx).css("display", "none");
                  	} else if (checkIdx == 2) {
                  		jQuery("#instructionsDiv_"+idx).css("display", "none");
                  		jQuery("#rxRegularDiv_"+idx).css("display", "none");
                  		jQuery("#rxMethadoneDiv_"+idx).css("display", "inline");
                  		jQuery("#rxSuboxoneDiv_"+idx).css("display", "none");
                  	} else if (checkIdx == 3) {
                  		jQuery("#instructionsDiv_"+idx).css("display", "none");
                  		jQuery("#rxRegularDiv_"+idx).css("display", "none");
                  		jQuery("#rxMethadoneDiv_"+idx).css("display", "none");
                  		jQuery("#rxSuboxoneDiv_"+idx).css("display", "inline");
                  	}
                  });
                  
                  // methodone
                  jQuery("input[id^='drkMon_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#homedoseMonMeth_"+rand).removeAttr("checked");
                  	} 
                  });
                  jQuery("input[id^='homedoseMonMeth_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#drkCarr_"+rand).removeAttr("checked");
                  		jQuery("#drkMon_"+rand).removeAttr("checked");
                  		jQuery("#homedoseNoMeth_"+rand).attr("checked","checked");
                  	}
                  });
                  
                  jQuery("input[id^='drkTues_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#homedoseTuesMeth_"+rand).removeAttr("checked");
                  	} 
                  });
                  jQuery("input[id^='homedoseTuesMeth_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#drkCarr_"+rand).removeAttr("checked");
                  		jQuery("#drkTues_"+rand).removeAttr("checked");
                  		jQuery("#homedoseNoMeth_"+rand).attr("checked","checked");
                  	}
                  });
                  
                  jQuery("input[id^='homedoseNoMeth_']").click(function() {
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#drkCarr_"+rand).removeAttr("checked");
                  	}
                  });
                  
                  jQuery("input[id^='drkWed_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#homedoseWedMeth_"+rand).removeAttr("checked");
                  	} 
                  });
                  jQuery("input[id^='homedoseWedMeth_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#drkCarr_"+rand).removeAttr("checked");
                  		jQuery("#drkWed_"+rand).removeAttr("checked");
                  		jQuery("#homedoseNoMeth_"+rand).attr("checked","checked");
                  	}
                  });
                  
                  jQuery("input[id^='drkThurs_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#homedoseThursMeth_"+rand).removeAttr("checked");
                  	} 
                  });
                  jQuery("input[id^='homedoseThursMeth_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#drkCarr_"+rand).removeAttr("checked");
                  		jQuery("#drkThurs_"+rand).removeAttr("checked");
                  		jQuery("#homedoseNoMeth_"+rand).attr("checked","checked");
                  	}
                  });
                  
                  jQuery("input[id^='drkFri_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#homedoseFriMeth_"+rand).removeAttr("checked");
                  	} 
                  });
                  jQuery("input[id^='homedoseFriMeth_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#drkCarr_"+rand).removeAttr("checked");
                  		jQuery("#drkFri_"+rand).removeAttr("checked");
                  		jQuery("#homedoseNoMeth_"+rand).attr("checked","checked");
                  	}
                  });
                  
                  jQuery("input[id^='drkSat_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#homedoseSatMeth_"+rand).removeAttr("checked");
                  	}
                  });
                  jQuery("input[id^='homedoseSatMeth_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#drkCarr_"+rand).removeAttr("checked");
                  		jQuery("#drkSat_"+rand).removeAttr("checked");
                  		jQuery("#homedoseNoMeth_"+rand).attr("checked","checked");
                  	}
                  });
                  
                  jQuery("input[id^='drkSun_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#homedoseSunMeth_"+rand).removeAttr("checked");
                  	} 
                  });
                  jQuery("input[id^='homedoseSunMeth_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#drkCarr_"+rand).removeAttr("checked");
                  		jQuery("#drkSun_"+rand).removeAttr("checked");
                  		jQuery("#homedoseNoMeth_"+rand).attr("checked","checked");
                  	}
                  });
                  
                  
                  // suboxone
                  jQuery("input[id^='doseMonSub_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#homedoseMonSub_"+rand).removeAttr("checked");
                  	} 
                  });
                  jQuery("input[id^='homedoseMonSub_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#doseCarrSub_"+rand).removeAttr("checked");
                  		jQuery("#doseMonSub_"+rand).removeAttr("checked");
                  		jQuery("#homedoseNoSub_"+rand).attr("checked","checked");
                  	}
                  });
                  
                  
                  jQuery("input[id^='doseTuesSub_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#homedoseTuesSub_"+rand).removeAttr("checked");
                  	} 
                  });
                  jQuery("input[id^='homedoseTuesSub_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#doseCarrSub_"+rand).removeAttr("checked");
                  		jQuery("#doseTuesSub_"+rand).removeAttr("checked");
                  		jQuery("#homedoseNoSub_"+rand).attr("checked","checked");
                  	}
                  });
                  
                  jQuery("input[id^='homedoseNoSub_']").click(function() {
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#doseCarrSub_"+rand).removeAttr("checked");
                  	}
                  });
                  
                  jQuery("input[id^='doseWedSub_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#homedoseWedSub_"+rand).removeAttr("checked");
                  	} 
                  });
                  jQuery("input[id^='homedoseWedSub_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#doseCarrSub_"+rand).removeAttr("checked");
                  		jQuery("#doseWedSub_"+rand).removeAttr("checked");
                  		jQuery("#homedoseNoSub_"+rand).attr("checked","checked");
                  	}
                  });
                  
                  jQuery("input[id^='doseThursSub_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#homedoseThursSub_"+rand).removeAttr("checked");
                  	} 
                  });
                  jQuery("input[id^='homedoseThursSub_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#doseCarrSub_"+rand).removeAttr("checked");
                  		jQuery("#doseThursSub_"+rand).removeAttr("checked");
                  		jQuery("#homedoseNoSub_"+rand).attr("checked","checked");
                  	}
                  });
                  
                  jQuery("input[id^='doseFriSub_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#homedoseFriSub_"+rand).removeAttr("checked");
                  	} 
                  });
                  jQuery("input[id^='homedoseFriSub_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#doseCarrSub_"+rand).removeAttr("checked");
                  		jQuery("#doseFriSub_"+rand).removeAttr("checked");
                  		jQuery("#homedoseNoSub_"+rand).attr("checked","checked");
                  	}
                  });
                  
                  jQuery("input[id^='doseSatSub_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#homedoseSatSub_"+rand).removeAttr("checked");
                  	}
                  });
                  jQuery("input[id^='homedoseSatSub_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#doseCarrSub_"+rand).removeAttr("checked");
                  		jQuery("#doseSatSub_"+rand).removeAttr("checked");
                  		jQuery("#homedoseNoSub_"+rand).attr("checked","checked");
                  	}
                  });
                  
                  jQuery("input[id^='doseSunSub_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#homedoseSunSub_"+rand).removeAttr("checked");
                  	} 
                  });
                  jQuery("input[id^='homedoseSunSub_']").click(function(){
                  	var rand = getRand(this);
                  	if (this.checked) {
                  		jQuery("#doseCarrSub_"+rand).removeAttr("checked");
                  		jQuery("#doseSunSub_"+rand).removeAttr("checked");
                  		jQuery("#homedoseNoSub_"+rand).attr("checked","checked");
                  	}
                  });
		  <%}%>
		
          jQuery("input[id^='repeats_']").keyup(function(){
            	var rand = <%=rand%>;
            	var repeatsVal = this.value;
            	if(repeatsVal>0){
            		jQuery("#longTerm_"+rand).attr("checked","checked");
            		jQuery(".med-term").trigger('change');
            	}
            });
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

