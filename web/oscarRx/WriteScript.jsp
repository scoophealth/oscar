<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*,oscar.oscarRx.data.*,oscar.oscarRx.pageUtil.*" %>
<% response.setHeader("Cache-Control","no-cache");%>


<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->


<html:html locale="true">
<head>
<title><bean:message key="WriteScript.title"/></title>

<link rel="stylesheet" type="text/css" href="styles.css">
<title>Write Prescription</title>

<html:base/>

<logic:notPresent name="RxSessionBean" scope="session">
    <logic:redirect href="error.html" />
</logic:notPresent>
<logic:present name="RxSessionBean" scope="session">
    <bean:define id="bean" type="oscar.oscarRx.pageUtil.RxSessionBean" name="RxSessionBean" scope="session" />
    <logic:equal name="bean" property="valid" value="false">
        <logic:redirect href="error.html" />
    </logic:equal>
</logic:present>

<logic:equal name="bean" property="stashIndex" value="-1">
    <logic:redirect href="SearchDrug.jsp" />
</logic:equal>

<%
oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean)pageContext.findAttribute("bean");
int specialStringLen = 0;
%>

<script language=javascript>

    var freqMin;
    var freqMax;
    var orig = null;
    var first = true;
    
    var calcQtyflag = true;

    function takeChg(){
        var frm = document.forms.RxWriteScriptForm;

        if(frm.take.value != 'Other'){
            frm.takeOther.style.display = 'none';
            frm.takeOther.value = frm.take.value;
        }else{
            frm.takeOther.style.display = '';
        }
        
        
        if (frm.take.value == '1/2'){
           frm.takeOther.value = 0.5;
        }
        
        if (frm.take.value == '1/4'){
           frm.takeOther.value = 0.25;
        }
        var sTake = frm.takeOther.value;
        var s = sTake.split('-');
        var sMin;
        var sMax;

        if(s.length==1){
            sMin = sTake;
            sMax = sTake;
        }else{
            sMin = s[0];
            sMax = s[1];
        }
        
        if(isNaN(parseFloat(sMin))){
            sMin = '';
        }
        if(isNaN(parseFloat(sMax))){
            sMax = sMin;
        }

        frm.takeMin.value = sMin;
        frm.takeMax.value = sMax;

        calcQty();
    }

    function brandChg(){
        submitForm('update');
    }

    function submitForm(action){
        
        var frm = document.forms.RxWriteScriptForm;

        if(frm.repeat.value.length < 1 || isNaN(parseInt(frm.repeat.value))){
            frm.repeat.value = 0;
        }

        if(frm.quantity.value == null || frm.quantity.value.length < 1){
            alert('Please enter a quantity.');
        }else{
            
            frm.action.value = action;

            frm.submit();
        }
    }

    function calcQty(){
        var frm = document.forms.RxWriteScriptForm;

        var takeMin = frm.takeMin.value;
        var takeMax = frm.takeMax.value;
        var freqId = frm.frequencyCode.selectedIndex;
        var dailyMin = freqMin[freqId];
        var dailyMax = freqMax[freqId];
        var dur = 1;

        if(frm.cmbDuration.value == 'Other'){
            frm.txtDuration.style.display = '';
        }else{
            frm.txtDuration.style.display = 'none';
            frm.txtDuration.value = frm.cmbDuration.value;
        }

        frm.duration.value = frm.txtDuration.value;

        // calculate duration units
        switch(frm.durationUnit.value){
            case 'D':{
                dur = frm.duration.value;
                break;
            }
            case 'W':{
                dur = (frm.duration.value * 7);
                break;
            }
            case 'M':{
                dur = (frm.duration.value * 30);
                break;
            }
        }

        // calculate quantity
        var minQty = (takeMin * dailyMin * dur);
        var maxQty = (takeMax * dailyMax * dur);

        if(isNaN(minQty) || isNaN(maxQty)){
            alert('The value entered is invalid.');
        }else{
            frm.sugQtyMin.value = minQty;
            frm.sugQtyMax.value = maxQty;

            setQuantity();
        }

        // calculate repeats
        if(frm.cmbRepeat.value == 'Other'){
            frm.txtRepeat.style.display = '';
        }else{
            frm.txtRepeat.style.display = 'none';
            frm.txtRepeat.value = frm.cmbRepeat.value;
        }

        frm.repeat.value = frm.txtRepeat.value;       
        if (calcQtyflag){
        writeScriptDisplay();
        calcQtyflag=true;
        }
    }

    function useQtyMax(){
        var frm = document.forms.RxWriteScriptForm;

        frm.quantity.value = frm.sugQtyMax.value;
        
        writeScriptDisplay();
    }

    function writeScriptDisplay(){  

        if (first == false){      
            
            var frm = document.forms.RxWriteScriptForm;
            var frm2 = document.forms.RxWriteScriptForm;

            var orig2 = frm.special.value;
            var preStr = "";

            if ( frm2.brandName){        
                preStr = frm2.brandName.value + "\n";        
            }else{        
                preStr = frm2.customName.value + "\n";
            }

            if(frm2.take.value != 'Other'){
                preStr = preStr+frm2.take.value;
            }else{
                preStr = preStr+frm2.takeOther.value;
            }
            preStr = preStr +" "+ frm2.frequencyCode.value+" ";

            if (frm2.prn.checked){
                preStr = preStr +"PRN";
            }

            /////////////////////
            preStr = preStr + " ";
            if(frm2.cmbDuration.value == 'Other'){
                preStr = preStr + frm2.txtDuration.value;            
            }else{            
                preStr = preStr + frm2.cmbDuration.value;
            }

            // calculate duration units
            switch(frm2.durationUnit.value){
                case 'D':{
                    preStr = preStr + " Days";
                    break;
                }
                case 'W':{
                    preStr = preStr + " Weeks";
                    break;
                }
                case 'M':{
                    preStr = preStr + " Months";
                    break;
                }
            }

            /////////////////////
            preStr = preStr + "\n";
            preStr = preStr + "Qty:"+frm2.quantity.value+" Repeats:"+frm2.txtRepeat.value;   
            if (frm2.nosubs.checked){
                preStr = preStr +" No Subs";
            }

            preStr = preStr +"\n";
            frm.special.value = preStr;
          }
          first = false;
        
    }
    
</script>


<script language="javascript">

    function showpic(picture){
       if (document.getElementById){ // Netscape 6 and IE 5+

          var targetElement = document.getElementById(picture);                
          var bal = document.getElementById("Calcs");

          var offsetTrail = document.getElementById("Calcs");
          var offsetLeft = 0;
          var offsetTop = 0;
          while (offsetTrail) {
             offsetLeft += offsetTrail.offsetLeft;
             offsetTop += offsetTrail.offsetTop;
             offsetTrail = offsetTrail.offsetParent;
          }
          if (navigator.userAgent.indexOf("Mac") != -1 && 
             typeof document.body.leftMargin != "undefined") {
             offsetLeft += document.body.leftMargin;
             offsetTop += document.body.topMargin;
          }

          targetElement.style.left = offsetLeft +bal.offsetWidth;        
          targetElement.style.top = offsetTop;	
          targetElement.style.visibility = 'visible';        
       }
    }

    function hidepic(picture){
            if (document.getElementById){ // Netscape 6 and IE 5+
                    var targetElement = document.getElementById(picture);
            targetElement.style.visibility = 'hidden';
            }
    }
    function popperup(vheight,vwidth,varpage,pageName) { //open a new popup window
      hidepic('Layer1');
      var page = varpage;
      windowprops = "height="+vheight+",width="+vwidth+",status=yes,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=100,left=100";
      var popup=window.open(varpage, pageName, windowprops);
          popup.opener = self;
      popup.focus();
    }
</script>

<style type="text/css">
        td.menuLayer{
            background-color: #ccccff;
            font-size: 14px;
        }
        table.layerTable{
            border-top: 2px solid #cfcfcf;
            border-left: 2px solid #cfcfcf;
            border-bottom: 2px solid #333333;
            border-right: 2px solid #333333;
        }

        table.messButtonsA{
            border-top: 2px solid #cfcfcf;
            border-left: 2px solid #cfcfcf;
            border-bottom: 2px solid #333333;
            border-right: 2px solid #333333;
        }

        table.messButtonsD{
            border-top: 2px solid #333333;
            border-left: 2px solid #333333;
            border-bottom: 2px solid #cfcfcf;
            border-right: 2px solid #cfcfcf;
        }

</style>



</head>
<body topmargin="0" leftmargin="0" vlink="#0000FF" onload="javascript:calcQty();">

<html:form action="/oscarRx/writeScript">
<html:hidden property="action" />
<%




// define current form
oscar.oscarRx.pageUtil.RxWriteScriptForm thisForm = (oscar.oscarRx.pageUtil.RxWriteScriptForm)request.getAttribute("RxWriteScriptForm");


if(bean.getStashIndex() > -1){ //new way
    oscar.oscarRx.data.RxPrescriptionData.Prescription rx = bean.getStashItem(bean.getStashIndex());
    oscar.oscarRx.data.RxDrugData drugData = new oscar.oscarRx.data.RxDrugData();
    thisForm.setDemographicNo(bean.getDemographicNo());
    thisForm.setRxDate(oscar.oscarRx.util.RxUtil.DateToString(rx.getRxDate()));
    thisForm.setEndDate(oscar.oscarRx.util.RxUtil.DateToString(rx.getEndDate()));

    if(! rx.isCustom()){
        thisForm.setGenericName(rx.getGenericName());        
        thisForm.setBrandName(rx.getBrandName() );
        thisForm.setGCN_SEQNO(rx.getGCN_SEQNO());
        thisForm.setCustomName("");
    }else{
        thisForm.setGenericName("");
        thisForm.setBrandName("");
        thisForm.setGCN_SEQNO(0);
        thisForm.setCustomName(rx.getCustomName());
    }

    thisForm.setTakeMin(rx.getTakeMinString());
    thisForm.setTakeMax(rx.getTakeMaxString());
    thisForm.setFrequencyCode(rx.getFrequencyCode());
    thisForm.setDuration(rx.getDuration());
    thisForm.setDurationUnit(rx.getDurationUnit());
    thisForm.setQuantity(rx.getQuantity());
    thisForm.setRepeat(rx.getRepeat());
    thisForm.setNosubs(rx.getNosubs());
    thisForm.setPrn(rx.getPrn());
    thisForm.setSpecial(rx.getSpecial());
    thisForm.setAtcCode(rx.getAtcCode());
    thisForm.setRegionalIdentifier(rx.getRegionalIdentifier());
}

boolean isCustom = thisForm.getGCN_SEQNO() == 0;
int drugId = thisForm.getGCN_SEQNO();

%>
<!--
DemographicNo:  <%= thisForm.getDemographicNo() %><br>
RxDate:         <%= thisForm.getRxDate() %><br>
EndDate:        <%= thisForm.getEndDate() %><br>
GenericName:    <%= thisForm.getGenericName() %><br>
BrandName:      <%= thisForm.getBrandName() %><br>
GCN_SEQNO:      <%= thisForm.getGCN_SEQNO() %><br>
CustomName:     <%= thisForm.getCustomName() %><br>
TakeMin:        <%= thisForm.getTakeMin() %><br>
TakeMax:        <%= thisForm.getTakeMax() %><br>
FrequencyCode:  <%= thisForm.getFrequencyCode() %><br>
Duration:       <%= thisForm.getDuration() %><br>
DurationUnit:   <%= thisForm.getDurationUnit() %><br>
Quantity:       <%= thisForm.getQuantity() %><br>
Repeat:         <%= thisForm.getRepeat() %><br>
Nosubs:         <%= String.valueOf(thisForm.getNosubs()) %><br>
Prn:            <%= String.valueOf(thisForm.getPrn()) %><br>

Special:        <%= thisForm.getSpecial() %><br>
ATC:            <%= thisForm.getAtcCode() %>
regional ident  <%= thisForm.getRegionalIdentifier() %>


-->
<%

// set patient info
oscar.oscarRx.data.RxPatientData.Patient patient = new oscar.oscarRx.data.RxPatientData().getPatient(thisForm.getDemographicNo());

oscar.oscarRx.data.RxDrugData drug = new oscar.oscarRx.data.RxDrugData();

java.util.ArrayList brands = null;
java.util.ArrayList forms  = null;
java.util.ArrayList routes = null;
java.util.Hashtable dosage = null; 
try{
    specialStringLen = thisForm.getSpecial().length();
}catch(Exception strLenEx){
    specialStringLen = 0;
}
String compString = null;

Vector comps = (Vector) request.getAttribute("components");
      if (comps != null){
        compString = new String();
        for (int c = 0; c < comps.size();c++){
            RxDrugData.DrugMonograph.DrugComponent dc = (RxDrugData.DrugMonograph.DrugComponent) comps.get(c); 
            compString = compString + dc.name+" "+dc.strength+ " "+dc.unit+"\n";              
        }          
      }

oscar.oscarRx.data.RxCodesData codesData = new oscar.oscarRx.data.RxCodesData();

// create freq list
oscar.oscarRx.data.RxCodesData.FrequencyCode[] freq = codesData.getFrequencyCodes();

// create special instructions list
String[] spec = codesData.getSpecialInstructions();

int i;

%>

<script language=javascript>
    freqMin = new Array(<%= freq.length%>);
    freqMax = new Array(<%= freq.length%>);

    <%for(i=0;i<freq.length;i++){%> 
        freqMin[<%=i%>] = <%= freq[i].getDailyMin()%>;
        freqMax[<%=i%>] = <%= freq[i].getDailyMax()%>;
    <%}%>
</script>

<html:hidden property="demographicNo" />
<html:hidden property="GCN_SEQNO" />
<html:hidden property="atcCode" />
<html:hidden property="regionalIdentifier" />



<table border="0" cellpadding="0" cellspacing="0" <% /*style="border-collapse: collapse"*/%> bordercolor="#111111" width="100%" height="100%">
    <%@ include file="TopLinks.jsp" %><!-- Row One included here-->
    <tr>
        <%@ include file="SideLinksNoEditFavorites.jsp" %><!-- <td></td>Side Bar File --->
        <td width="100%" style="border-left: 2px solid #A9A9A9; " height="100%" valign="top">
            <table cellpadding="0" cellspacing="2" style="border-collapse: collapse" bordercolor="#111111" width="100%" height="100%">
                <tr>
                    <td width="0%" valign="top">
            	        <div class="DivCCBreadCrumbs">
              	        <a href="SearchDrug.jsp">   <bean:message key="SearchDrug.title"/></a> >
                        <bean:message key="ChooseDrug.title"/> >
                        <b><bean:message key="WriteScript.title"/></b>
                        </div>
                    </td>
                </tr>
            <!----Start new rows here-->

                <tr>
                    <td>
 		                <div class="DivContentTitle"><bean:message key="WriteScript.title"/></div>
                    </td>
		</tr>

                <tr>
                    <td>
                        <div class="DivContentSectionHead"><bean:message key="WriteScript.section2Title"/> for <%= patient.getFirstName() %> <%= patient.getSurname() %></div>
                    </td>
                </tr>


                <tr>
                    <td>
                        <table border=1 style="border: 1px solid #A9A9A9; ">

                        <% if (! isCustom) { %>
                            <tr>
                                <td colspan=2>
                                    Generic Name:
                                </td>
                                <td colspan=2>
                                    <html:hidden property="genericName"/>
                                    <b><%= thisForm.getGenericName() %></b>
                                    <%if ( compString != null ){%>
                                    <a href="javascript: function myFunction() {return false; }" title="<%=compString%>" >Components</a>
                                    <%}%>
                                    
                                                       
                                </td>
                                 <td valign=top rowspan=8>
                                                <select size=20 name="selSpecial" ondblclick="javascript:cmdSpecial_click();">
                                                    <%for(i=0; i<spec.length; i++){%>
                                                        <option value="<%= spec[i] %>">
                                                            <%= spec[i] %>
                                                        </option>
                                                    <%}%>
                                                </select>
                                            </td>
                            </tr>

                            <tr>
                                <td colspan=2>
                                    Brand Name:
                                </td>
                                <td colspan=2>                                                                        
                                            <html:hidden property="brandName" />
                                            <b><%= thisForm.getBrandName() %></b>                                                                     
                                </td>
                                <!--<td >
                                    &nbsp;
                                </td>-->
                            </tr>

                            <tr>
                                <td colspan=2>
                                  &nbsp;
                                </td>
                                <td colspan=2>
                                  &nbsp;                                   
                                </td>
                                <!--<td >
                                  &nbsp;
                                </td>-->
                            </tr>

                        <% } else { /* Custom Drug Entry */%>

                            <tr>
                                <td colspan=2 valign="top">
                                    Custom Drug:
                                </td>
                                <td colspan=2>
                                    <html:textarea property="customName" cols="50" rows="3" onchange="javascript:writeScriptDisplay();" />
                                </td>
                                <td valign=top rowspan=8>
                                            <div style="z-index: 0;">       
                                                <select size=20 name="selSpecial" ondblclick="javascript:cmdSpecial_click();" >
                                                    <%for(i=0; i<spec.length; i++){%>
                                                        <option value="<%= spec[i] %>">
                                                            <%= spec[i] %>
                                                        </option>
                                                    <%}%>
                                                </select>
                                            </div>
                                            </td>
                            </tr>

                        <% } /* Custom */ %>

                            <html:hidden property="rxDate" />
                            <tr>
                                <td colspan=2>Take:</td>
                                <td colspan=2>
                                    <select name="take" style="width:72px" onChange="javascript:takeChg();">
                                        <option value="1/4">1/4</option>
                                        <option value="1/2">1/2</option>
                                        <option value="1">1</option>
                                        <option value="1-2">1-2</option>
                                        <option value="1-3">1-3</option>
                                        <option value="2">2</option>
                                        <option value="2-3">2-3</option>
                                        <option value="3">3</option>
                                        <option value="3-4">3-4</option>
                                        <option value="4">4</option>
                                        <option value="5">5</option>
                                        <option value="6">6</option>
                                        <option value="7">7</option>
                                        <option value="8">8</option>
                                        <option value="9">9</option>
                                        <option value="Other">Other</option>
                                    </select>
                                    <input type=text name="takeOther" style="display:none" size="5" onChange="javascript:takeChg();" />
                                    <html:select property="frequencyCode" style="width:72px" onchange="javascript:calcQty();">
                                        <%for(i=0; i<freq.length; i++){%>
                                            <html:option value="<%= freq[i].getFreqCode() %>">
                                                <%= freq[i].getFreqCode() %>
                                            </html:option>
                                        <%}%>
                                    </html:select>


                                    <html:hidden property="takeMin" />
                                    <html:hidden property="takeMax" />
                                    <script language=javascript>
                                        var frm = document.forms.RxWriteScriptForm;

                                        
                                        if(frm.takeMin.value == frm.takeMax.value){
                                            frm.takeOther.value = frm.takeMin.value;
                                        }else{
                                            frm.takeOther.value = (frm.takeMin.value + '-' + frm.takeMax.value);
                                        }
                                        
                                        if(frm.takeOther.value == '0.5'){
                                           frm.takeOther.value = '1/2';
                                        }
                                        
                                        if(frm.takeOther.value == '0.25'){
                                           frm.takeOther.value == '1/4';                                        
                                        }                                        
                                        frm.take.value = frm.takeOther.value;                                        
                                        if(frm.take.value != frm.takeOther.value){
                                            frm.take.value = 'Other'; 
                                            frm.takeOther.style.display = '';
                                        }
                                    </script>
                                    PRN:<html:checkbox property="prn"  onchange="javascript:writeScriptDisplay();" />
                                </td>
                                <!--<td>
                                    &nbsp;
                                </td>-->
                            </tr>


                            <tr>
                                <td colspan=2>
                                    For:
                                </td>
                                <td colspan=2>
                                    <select name="cmbDuration" style="width:72px" onChange="javascript:calcQty();">
                                        <%for(i=1; i<15; i++){%>
                                            <option value="<%= i%>"><%= i%></option>
                                        <%}%>
                                        <option value="Other">Other</option>
                                    </select>
                                    <input type=text name="txtDuration" size="4" onchange="javascript:calcQty();" style="display:none" />
                                    <html:select property="durationUnit" style="width:72px" onchange="javascript:calcQty();">
                                        <html:option value="D">Days</html:option>
                                        <html:option value="W">Weeks</html:option>
                                        <html:option value="M">Months</html:option>
                                    </html:select>


                                    <html:hidden property="duration" />

                                    <script language=javascript>
                                        frm.txtDuration.value = frm.duration.value;

                                        frm.cmbDuration.value = frm.txtDuration.value;
                                        if(frm.cmbDuration.value != frm.txtDuration.value){
                                            frm.cmbDuration.value = 'Other';
                                            frm.txtDuration.style.display = '';
                                        }
                                    </script>
                                </td>
                                <!--<td>
                                &nbsp;
                                </td>-->
                            </tr>

                            <tr>
                                <td colspan=2>
                                    Quantity:
                                </td>
                                <td colspan=2 width=65%>
                                    <html:text property="quantity" size="8" onchange="javascript:writeScriptDisplay();"  />

                                    <input type=button value="<<" onclick="javascript:useQtyMax();" />
                                    (Calculated:&nbsp;<span id="lblSugQty" style="font-weight:bold"></span>&nbsp;)
                                    <input type=hidden name="sugQtyMin" />
                                    <input type=hidden name="sugQtyMax" />
                                    <script language="javascript">
                                        function setQuantity(){
                                            if (frm.sugQtyMin.value != frm.sugQtyMax.value){
                                                //document.all.namedItem()
                                                document.getElementById('lblSugQty').innerText
                                                    = frm.sugQtyMin.value + ' - ' + frm.sugQtyMax.value;
                                            }else{
                                                if(frm.sugQtyMin.value > 0){
                                                    document.getElementById('lblSugQty').innerText
                                                        = frm.sugQtyMin.value;
                                                }
                                            }
                                        }

                                        setQuantity();
                                    </script>
                                </td>
                                <!--<td>
                                    &nbsp;
                                </td>-->
                            </tr>

                            <tr>
                                <td colspan=2>
                                    Repeats:
                                </td>
                                <td colspan=2>
                                    <select name="cmbRepeat" style="width:72px" onChange="javascript:calcQty();">
                                        <%for(i=0; i<9; i++){%>
                                            <option value="<%= i%>"><%= i%></option>
                                        <%}%>
                                        <option value="Other">Other</option>
                                    </select>

                                    <input type=text name="txtRepeat" size="5" onchange="javascript:calcQty();" style="display:none" />

                                    <html:hidden property="repeat" />

                                    <script language=javascript>
                                        frm.txtRepeat.value = frm.repeat.value;

                                        frm.cmbRepeat.value = frm.txtRepeat.value;
                                        if(frm.cmbRepeat.value != frm.txtRepeat.value){
                                            frm.cmbRepeat.value = 'Other';
                                            frm.txtRepeat.style.display = '';
                                        }
                                    </script>
                                    No Subs:<html:checkbox property="nosubs" onchange="javascript:writeScriptDisplay();" />
                                </td>
                                                               
                            </tr>
                            <tr>
                                <td colspan=4>
                                    Special Instructions:
                                    <script language=javascript>
                                        function cmdSpecial_click(){
                                            var frm = document.forms.RxWriteScriptForm;
                                            if(frm.selSpecial.selectedIndex >-1){
                                                var s = frm.selSpecial.value;

                                                frm.special.value += s + '\n';
                                            }
                                        }
                                    </script>
                                    
                                    <table width=100% border=1 >
                                        <tr>
                                            <td valign=top>
                                                <html:textarea property="special" cols="50" rows="5" />
                                            </td>
                                            <td valign=center>
                                                <input type=button name="cmdSpecial" value="<<" onclick="javascript:cmdSpecial_click();" />
                                            </td>
                                            
                                        </tr>
                                    </table>
                                </td>
                               
                            </tr>
                        </table>
                    </td>
                </tr>

                <tr>
                    <td>
                        <!--3a-->
                        </html:form>
                        <input type=button class="ControlPushButton" style="width:55px"  onclick="javascript:submitForm('update');" value="Update" />
                        <input type=button class="ControlPushButton" style="width:200px" onclick="javascript:submitForm('updateAddAnother');" value="Update and Get New Drug" />
                        <input type=button class="ControlPushButton" style="width:200px" onclick="javascript:submitForm('updateAndPrint');" value="Update, Print and Save" />
                           

                        
                        <br>
                        <!-- peice Went Here 
                        <div style="background-color:yellow;margin-right:100px;margin-left:20px;margin-top:10px;padding-left:10px;padding-top:10px;padding-bottom:5px;border-bottom: 2px solid gray;border-right: 2px solid #999;border-top: 1px solid #CCC;border-left: 1px solid #CCC;">
                        ACETAMINOPHEN	inhibits	BENZODIAZEPINE, long acting &nbsp;&nbsp;&nbsp;&nbsp;SIGNIFICANCE = MINOR &nbsp;&nbsp;&nbsp;EVIDENCE = POOR
                        </div>
                        <div style="background-color:red;margin-right:100px;margin-left:20px;margin-top:1px;padding-left:10px;padding-top:10px;padding-bottom:5px;border-bottom: 2px solid gray;border-right: 2px solid #999;border-top: 1px solid #CCC;border-left: 1px solid #CCC;">
                        ACETAMINOPHEN	inhibits	BENZODIAZEPINE, long acting &nbsp;&nbsp;&nbsp;&nbsp;SIGNIFICANCE = MINOR &nbsp;&nbsp;&nbsp;EVIDENCE = POOR
                        </div>
                        -->
                        <script language=javascript>
                            function submitPending(stashId, action){
                                
                                var frm = document.forms.RxStashForm;
                                frm.stashId.value = stashId;
                                frm.action.value = action;
                                frm.submit();
                            }
                        </script>
                    <!--6a-->
                    <html:form action="/oscarRx/stash">
                        <input type="hidden" name="action" value="">
                        <input type="hidden" name="stashId" />
                    </html:form>

                    </td>
                </tr>

                <tr>
                    <td>
 <!--5a-->                       <div class="DivContentSectionHead"><bean:message key="WriteScript.section5Title"/></div>
                    </td>
                </tr>


                <tr>
                    <td>
                        <script language=javascript>
                            function ShowDrugInfo(GN){
                                window.open("drugInfo.do?GN=" + escape(GN), "_blank",
                                    "location=no, menubar=no, toolbar=no, scrollbars=yes, status=yes, resizable=yes");
                            }
                        </script>
                        <script language=javascript>
                            function addFavorite(stashId, brandName){
                                var favoriteName = window.prompt('Please enter a name for the Favorite:',
                                    brandName);

                                if (favoriteName.length > 0){
                                    window.location.href = 'addFavoriteWriteScript.do?stashId='
                                        + escape(stashId) + '&favoriteName=' + escape(favoriteName);
                                }
                            }
                        </script>

                        <table cellspacing=0 cellpadding=5>
                        <% int i=0; %>
                        <logic:iterate id="rx" name="bean" property="stash" length="stashSize">
                            <%
                            oscar.oscarRx.data.RxPrescriptionData.Prescription rx2
                                = ((oscar.oscarRx.data.RxPrescriptionData.Prescription)rx);

                            if(i==bean.getStashIndex()){ 
                                %> <tr class=tblRowSelected> <% 
                            }else{ 
                                %> <tr> <% 
                            }
                            %>
                                <td><a href="javascript:submitPending(<%= i%>, 'edit');">Edit</a></td>
                                <td><a href="javascript:submitPending(<%= i%>, 'delete');">Delete</a></td>
                                <td>
                                    <a href="javascript:submitPending(<%= i%>, 'edit');">
                                        <bean:write name="rx" property="rxDisplay" />
                                    </a>
                                </td>
                                <td>
                                    <a href="javascript:ShowDrugInfo('<%= rx2.getGenericName() %>');">Info</a>
                                </td>
                                <td>
                                    <a href="javascript:addFavorite(<%= String.valueOf(i) %>, '<%= rx2.isCustom() ? rx2.getCustomName() : rx2.getBrandName() %>');">Add to Favorites</a>
                                </td>
                            </tr>
                            <% i++; %>
                        </logic:iterate>
                        </table>
                    </td>
                </tr>


            <!----End new rows here-->
		        <tr height="100%">
                    <td>
                    </td>
                </tr>
            </table>
        </td>
    </tr>

	<tr>
    	<td height="0%" style="border-bottom:2px solid #A9A9A9; border-top:2px solid #A9A9A9; "></td>
    	<td height="0%" style="border-bottom:2px solid #A9A9A9; border-top:2px solid #A9A9A9; "></td>
  	</tr>

  	<tr>
    	<td width="100%" height="0%" colspan="2">&nbsp;</td>
  	</tr>

  	<tr>
    	<td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC" colspan="2">
        

        
        
        <script language=javascript>
        <%if ( specialStringLen == 0 ){
                out.write("first=false;"); 
          }else{  
                out.write("calcQtyflag=false;"); 
          }%>

        
          writeScriptDisplay();
        </script>
        
        </td>
  	</tr>

</table>
</body>
</html:html>
