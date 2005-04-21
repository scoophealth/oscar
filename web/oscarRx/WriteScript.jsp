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
    var warningArray = new Array ();
    
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
    
    function changeDuration(){
       var frm = document.forms.RxWriteScriptForm;
       var freqId = frm.frequencyCode.selectedIndex;
       
       var dailyMin = freqMin[freqId];
       
       if (dailyMin < 1){
          var minDays = 1/dailyMin;
          var bySeven = minDays / 7;
          var byThirty = minDays / 30;
          var currentDur = frm.cmbDuration.value;
          
          if  (Math.floor(bySeven) == bySeven ){  //no remainder
             frm.durationUnit.value = 'W';  
             if (currentDur < bySeven){
                frm.cmbDuration.value = bySeven;
             }
             //alert (bySeven);
          }else {
             if (Math.floor(byThirty) == byThirty ){  //no remainder
                frm.durationUnit.value = 'M';  
                if (currentDur < byThirty){
                   frm.cmbDuration.value = byThirty;
                }
                //alert (byThirty);
             }
          }
          
       }
       return true;
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
            
            minQty = Math.ceil(minQty);
            maxQty = Math.ceil(maxQty);
            frm.sugQtyMin.value = minQty;
            frm.sugQtyMax.value = maxQty;
            frm.quantity.value = frm.sugQtyMax.value;
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
//alert ("q"+calcQtyflag);
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
    function regexDebug(regRet){
       alert(regRet.input+"\n"+regRet.index+" " +(regRet.index+regRet[0].length)+" "+regRet[0]);
    }
    
    function replaceScriptDisplay(){
       var frm = document.forms.RxWriteScriptForm;       
       var orig = frm.special.value;       
       var nameRegExp = /.*\n/i;
       var a = nameRegExp.exec(orig);
       
       clearWarning();
       if (a){  //Find the title and shorten the string
          //regexDebug(a);                    
          origMinusName = orig.substring(a.index+a[0].length);
          origMinusName = origMinusName.replace(/Q12H/,"OD");   //KLUDGE the 2 get picked up as the second digit
          origMinusName = origMinusName.replace(/Q1-2H/,"OD");   //KLUDGE the 2 get picked up as the second digit
          origMinusName = origMinusName.replace(/Q3-4H/,"OD");   //KLUDGE the 2 get picked up as the second digit
          origMinusName = origMinusName.replace(/Q4-6H/,"OD");   //KLUDGE the 2 get picked up as the second digit
          origMinusName = removePRNifNeeded(origMinusName);
          origMinusName = removeNoSubsifNeeded(origMinusName);
    
          drugName = orig.substring(0,a.index+a[0].length);       
          var beforeFirstDigit = "";
          
          //find first digit
          var firstDigitRegExp = /[0-9][0-9,\/,-]*/;
          var b = firstDigitRegExp.exec(origMinusName);
          if (b) {                        
             //regexDebug(b);                          
             if(b.index == 0){  //frequency is at the start of the line
                //TODO should I add method if it is not there??
                addWarning(frm.method.options[frm.method.selectedIndex].text+ " Not Added");
             }else{ // frequency is not at the start of the line words come before             
               var beforeFirstDigit = origMinusName.substring(0,b.index);
               var findMethodRegExp = /(Take|Apply|Rub well in)/;
               var c = findMethodRegExp.exec(beforeFirstDigit);
               if (c){                  
                  //regexDebug(c);
                  beforeFirstDigit = beforeFirstDigit.replace(/(Take|Apply|Rub well in)/,frm.method.options[frm.method.selectedIndex].text);  
               }else{                                   
                 addWarning("Could not replace word for "+frm.method.options[frm.method.selectedIndex].text);
               }               
             }             
             var firstDigitStringLen = b.index+b[0].length;
             
             if(origMinusName.substring(b.index+b[0].length,b.index+b[0].length+1) == '.'){   //Another Kludge, \. does not seem to work correctly t get values 0.5                
                firstDigitStringLen++;
                var afterDot = /[0-9][0-9]*/;
                var afterDotResult = afterDot.exec(origMinusName.substring(firstDigitStringLen));
                if (afterDotResult){
                  
                  //regexDebug(afterDotResult);
                  if(afterDotResult.index == 0){
                     firstDigitStringLen = firstDigitStringLen + afterDotResult.index+afterDotResult[0].length;
                  }
                }                
             }
             
             var afterFirstDigit = origMinusName.substring(firstDigitStringLen);
             
             //Find Next Digit
             var secondDigitRegExp = /[^Q,-,0-9][0-9][0-9,\/,-]*/;   //This will ignore digits starting with Q and - need for frequencies like Q3-4H
             var d = secondDigitRegExp.exec(afterFirstDigit);
             if(d){
                //regexDebug(d);
                var betweenFirstAndSecondDigit = afterFirstDigit.substring(0,d.index+1);                
                var afterSecondDigit = afterFirstDigit.substring(d.index+d[0].length);
                                
                //Replace units and frequency Unit  //TODO Pull this from Database
                
                var findUnitRegExp = /(Tabs|mL|Squirts|g|mg|µg|Drops)/;
                var findU = findUnitRegExp.exec(betweenFirstAndSecondDigit);
                if (findU){
                  //todo make it like !findU
                }else{                   
                   addWarning("Could not find place to put "+frm.unit.options[frm.unit.selectedIndex].text);
                }
                
                var findUnitRegExp = /(PO|SL|IM|SC|PATCH|TOP.|INH|SUPP)/;                
                var findU = findUnitRegExp.exec(betweenFirstAndSecondDigit);
                if (findU){
                  //todo make it like !findU
                }else{                   
                   addWarning("Could not find place to put "+frm.route.options[frm.route.selectedIndex].text);
                }
                                                
                var findFreqRegExp = /(OD|BID|TID|QID|Q1H|Q2H|Q1-2H|Q3-4H|Q4H|Q4-6H|Q6H|Q8H|Q12H|QAM|QPM|QHS|Q1Week|Q2Week|Q1Month|Q3Month)/;
                var findFreq = findFreqRegExp.exec(betweenFirstAndSecondDigit);
                if (findFreq){
                  //todo make it like !findFreq
                }else{                   
                   addWarning("Could not find place to put "+frm.frequencyCode.value);
                }
                
                betweenFirstAndSecondDigit = betweenFirstAndSecondDigit.replace(/(Tabs|mL|Squirts|g|mg|µg|Drops)/,frm.unit.options[frm.unit.selectedIndex].text);
                betweenFirstAndSecondDigit = betweenFirstAndSecondDigit.replace(/(PO|SL|IM|SC|PATCH|TOP.|INH|SUPP)/,frm.route.options[frm.route.selectedIndex].text);
                betweenFirstAndSecondDigit = betweenFirstAndSecondDigit.replace(/(OD|BID|TID|QID|Q1H|Q2H|Q1-2H|Q3-4H|Q4H|Q4-6H|Q6H|Q8H|Q12H|QAM|QPM|QHS|Q1Week|Q2Week|Q1Month|Q3Month)/,frm.frequencyCode.value);                                    
                                                                
                //Replace Days Weeks or Months
                
                afterSecondDigit = afterSecondDigit.replace(/(Days|Weeks|Months)/,getDurationUnit());
                                
                var findDurationRegExp = /(Days|Weeks|Months)/;
                var e = findDurationRegExp.exec(afterSecondDigit);
                if (e){
                  //todo make it like !e
                }else{                   
                   addWarning("Could not find place to put "+getDurationUnit());
                }
                                                                   
                //Replace Qty
                var f = firstDigitRegExp.exec(afterSecondDigit);

                if(f){
                   //regexDebug(f)
                   var betweenSecondDigitandQuantity = afterSecondDigit.substring(0,f.index);                   
                   var afterQuantity = afterSecondDigit.substring(f.index+f[0].length);                   

                   var g = firstDigitRegExp.exec(afterQuantity);
                   if(g){
                      //regexDebug(g);
                      var betweenQtyandRepeat = afterQuantity.substring(0,g.index);
                      var afterRepeat = afterQuantity.substring(g.index+g[0].length);
                      var b4final =drugName+beforeFirstDigit+getTakeValue()+betweenFirstAndSecondDigit+getDurationValue()+betweenSecondDigitandQuantity+frm.quantity.value+betweenQtyandRepeat+frm.txtRepeat.value+afterRepeat;
                      var newStr = drugName+beforeFirstDigit+getTakeValue()+betweenFirstAndSecondDigit+getPRN(b4final)+getDurationValue()+betweenSecondDigitandQuantity+frm.quantity.value+betweenQtyandRepeat+frm.txtRepeat.value+getNoSubs(b4final)+afterRepeat;
                      
                      frm.special.value = newStr;
                      //alert(newStr);
                   }else{
                      addWarning("Could not find value to replace Repeat Value");
                   }
                }else{
                   addWarning("Could not find value to replace Qty Value");
                }                                                   
             }else{ 
                addWarning("Could not find value to replace Duration Value");
             }             
          }else{
                addWarning("Could not find value frequency value");
          }       
       }else{
         addWarning("Name of Drug could not be found");
       }        
       fillWarnings();
    }
    
    function removePRNifNeeded(str){
       var frm = document.forms.RxWriteScriptForm;
       var retval = str;
       if (frm.prn.checked == false){
          retval = str.replace(/PRN/,"");
       }
       return retval;
    }   
    
    function removeNoSubsifNeeded(str){
       var frm = document.forms.RxWriteScriptForm;
       var retval = str;
       if (frm.nosubs.checked == false){
          retval = str.replace(/No Subs/,"");
       }
       return retval;
    }   
    function getPRN(str){
       var retval = "";
       var frm = document.forms.RxWriteScriptForm;
       if (frm.prn.checked){//is PRN CHECKED?
          var prnFindRegExp = /PRN/; 
          var p = prnFindRegExp.exec(str);
          if(p){
             //PRN found leave it
          }else{
             retval = "PRN ";
          }
       }
       return retval;
    }

    function getNoSubs(str){
       var retval = "";
       var frm = document.forms.RxWriteScriptForm;
       if (frm.nosubs.checked){//is PRN CHECKED?
          var noSubFindRegExp = /No Subs/; 
          var p = noSubFindRegExp.exec(str);
          if(p){
             //No Subs found leave it
          }else{
             retval = " No Subs ";
          }
       }
       return retval;
    }
    
    function getTakeValue(){
       var frm = document.forms.RxWriteScriptForm;
       var retval = "";
       if(frm.take.value != 'Other'){
          retval = frm.take.value;
       }else{
          retval = frm.takeOther.value;
       }
       return retval;
    }
    
    function getDurationValue(){
       var frm = document.forms.RxWriteScriptForm;
       var retval = "";
       if(frm.cmbDuration.value == 'Other'){
          retval = frm.txtDuration.value;            
       }else{            
          retval = frm.cmbDuration.value;
       }
       return retval;
    }
    
    function getDurationUnit(){
       var frm = document.forms.RxWriteScriptForm;
       var retval = "";
       switch(frm.durationUnit.value){
          case 'D':{
             retval = "Days";
             break;
          }
          case 'W':{
             retval = "Weeks";
             break;
          }
          case 'M':{
             retval = "Months";
             break;
          }
       }
       return retval;
    }
    
    function writeScriptDisplay(){  
  //      alert ("f"+first);
    
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

            preStr = preStr + frm2.method.options[frm2.method.selectedIndex].text +" ";            
            
            if(frm2.take.value != 'Other'){
                preStr = preStr+frm2.take.value;
            }else{
                preStr = preStr+frm2.takeOther.value;
            }
                                    
            preStr = preStr +" "+ frm2.unit.options[frm2.unit.selectedIndex].text;            
            
            preStr = preStr +" "+ frm2.route.options[frm2.route.selectedIndex].text;            
            
            preStr = preStr +" "+ frm2.frequencyCode.value+" ";
                        
            if (frm2.prn.checked){
                preStr = preStr +"PRN ";
            }

            /////////////////////
            preStr = preStr + "for ";
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
            
            if ( "" == frm2.quantity.value ){              
              frm2.quantity.value = "0";
            }
            /////////////////////
            preStr = preStr + "\n";
            preStr = preStr + "Qty:"+frm2.quantity.value+" Repeats:"+frm2.txtRepeat.value;   
            if (frm2.nosubs.checked){
                preStr = preStr +" No Subs";
            }

            preStr = preStr +"\n";
            frm.special.value = preStr;
            first = true;
          }else{
             replaceScriptDisplay();
          }
          //first = false;
        
    }
    
    function clearWarning(){
       warningArray = new Array(); 
    }    
    function addWarning(addit){
       warningArray[warningArray.length] = addit;
    }
    
    function fillWarnings(){
     //warningDiv
     // warningList
     
     var warningDiv  = document.getElementById("warningDiv");
     var warningList = document.getElementById("warningList");
     
     while (warningList.hasChildNodes()){
        warningList.removeChild(warningList.firstChild);
     }                          
     for(i=0;i<warningArray.length;i++){
			var newText = document.createTextNode(warningArray[i]);
			var newNode = document.createElement("li");
			newNode.appendChild(newText);
			warningList.appendChild(newNode);
		}
		
		if(warningArray.length == 0){
		   warningDiv.style.display = 'None';
		}else{
		   warningDiv.style.display = '';
		}
		
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
    
    thisForm.setRxDate(oscar.oscarRx.util.RxUtil.DateToString(rx.getRxDate(),"yyyy-MM-dd") );    
    
    thisForm.setEndDate(oscar.oscarRx.util.RxUtil.DateToString(rx.getEndDate()));
    
    try{
        System.out.println(oscar.oscarRx.util.RxUtil.DateToString(rx.getRxDate(),"yyyy-MM-dd") );
    }catch(Exception e){ e.printStackTrace(); }

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
    thisForm.setUnit(rx.getUnit());
    thisForm.setMethod(rx.getMethod());
    thisForm.setRoute(rx.getRoute());
    System.out.println("route "+rx.getRoute());
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
                                  Start Date:
                                </td>
                                <td colspan=2>
                                  <html:text property="rxDate" />
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

                            
                            <tr>
                                <td colspan=2>
                                   <html:select property="method" style="width:90px" onchange="calcQty();">                                   
                                        <html:option value="Take">Take</html:option>
                                        <html:option value="Apply">Apply</html:option>
                                        <html:option value="Rub">Rub well in</html:option>                                        
                                    </html:select>
                                </td>
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
                                    <html:select property="unit" style="width:80px" onchange="calcQty();">
                                        <html:option value="tab">Tabs</html:option>
                                        <html:option value="mL">mL</html:option>
                                        <html:option value="sqrt">Squirts</html:option>
                                        <html:option value="g" >gm</html:option>
                                        <html:option value="mg">mg</html:option>
                                        <html:option value="micg">µg</html:option>
                                        <html:option value="drop">Drops</html:option>                                        
                                    </html:select>
                                    
                                    <html:select property="route" style="width:80px" onchange="calcQty();">
                                        <html:option value="PO">PO</html:option>
                                        <html:option value="SL">SL</html:option>
                                        <html:option value="IM">IM</html:option>
                                        <html:option value="SC">SC</html:option>
                                        <html:option value="PATCH">PATCH</html:option>
                                        <html:option value="TOP">TOP.</html:option>
                                        <html:option value="INH">INH</html:option>                                        
                                        <html:option value="SUPP">SUPP</html:option>                                        
                                    </html:select>
                                    
                                    <html:select property="frequencyCode" style="width:80px" onchange="javascript:changeDuration();calcQty();">
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
                                    <html:select property="durationUnit" style="width:80px" onchange="javascript:calcQty();">
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
                                    <html:text property="quantity" size="8" onchange="javascript:writeScriptDisplay();"   />

                                    <input type=button value="<<" onclick="javascript:useQtyMax();" />
                                    (Calculated:&nbsp;<span id="lblSugQty" style="font-weight:bold"></span>&nbsp;)
                                    <input type=hidden name="sugQtyMin" />
                                    <input type=hidden name="sugQtyMax" />
                                    <script language="javascript">
                                        function setQuantity(){
                                            var sugQtyLbl = document.getElementById('lblSugQty');
                                            while (sugQtyLbl.hasChildNodes()){
                                                sugQtyLbl.removeChild(sugQtyLbl.firstChild);
                                            }                          
                                            var newSugQty = frm.sugQtyMin.value;
                                            if (frm.sugQtyMin.value != frm.sugQtyMax.value){                                                                                
                                                newSugQty = frm.sugQtyMin.value + ' - ' + frm.sugQtyMax.value;
                                            }             
                                            sugQtyLbl.appendChild(document.createTextNode(newSugQty));
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
                                                <input type=button value="RD" title="Redraw" onclick="javascript:first = false; writeScriptDisplay();"/>
                                                <div id="warningDiv" style="display: none;">
                                                   <ul id="warningList">
                                                   <li>warning</li>
                                                   </ul>
                                                </div>
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
                        <!-- input type=button class="ControlPushButton" style="width:200px" onclick="javascript:replaceScriptDisplay();" value="REPLACE" />
                         <input type=button class="ControlPushButton" style="width:200px" onclick="javascript:fillWarnings();" value="RunWarning" /
                         <input type=button class="ControlPushButton" style="width:200px" onclick="javascript:addWarning();" value="FillWarning" /--> 
                        <br>
                        <!-- peice Went Here -->
                        <%RxDrugData.Interaction[] interactions = (RxDrugData.Interaction[]) bean.getInteractions();                        
                          if (interactions != null && interactions.length > 0){ 
                              System.out.println("interactions.length "+interactions.length);
                             for (int i = 0 ; i < interactions.length; i++){  %>
                             <div style="background-color:<%=sigColor(interactions[i].significance)%>;margin-right:100px;margin-left:20px;margin-top:10px;padding-left:10px;padding-top:10px;padding-bottom:5px;border-bottom: 2px solid gray;border-right: 2px solid #999;border-top: 1px solid #CCC;border-left: 1px solid #CCC;">
                             <%=interactions[i].affectingdrug%> 	<%=effect(interactions[i].effect)%> <%=interactions[i].affecteddrug%> &nbsp;&nbsp;&nbsp;&nbsp;SIGNIFICANCE = <%=significance(interactions[i].significance)%> &nbsp;&nbsp;&nbsp;EVIDENCE = <%=evidence(interactions[i].evidence)%>
                             </div>
                        <%   }
                         }
                        %>
                        
                        <!--<div style="background-color:yellow;margin-right:100px;margin-left:20px;margin-top:10px;padding-left:10px;padding-top:10px;padding-bottom:5px;border-bottom: 2px solid gray;border-right: 2px solid #999;border-top: 1px solid #CCC;border-left: 1px solid #CCC;">
                        ACETAMINOPHEN	inhibits	BENZODIAZEPINE, long acting &nbsp;&nbsp;&nbsp;&nbsp;SIGNIFICANCE = MINOR &nbsp;&nbsp;&nbsp;EVIDENCE = POOR
                        </div>
                        <div style="background-color:red;margin-right:100px;margin-left:20px;margin-top:1px;padding-left:10px;padding-top:10px;padding-bottom:5px;border-bottom: 2px solid gray;border-right: 2px solid #999;border-top: 1px solid #CCC;border-left: 1px solid #CCC;">
                        ACETAMINOPHEN	inhibits	BENZODIAZEPINE, long acting &nbsp;&nbsp;&nbsp;&nbsp;SIGNIFICANCE = MINOR &nbsp;&nbsp;&nbsp;EVIDENCE = POOR
                        </div>-->
                        
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
                //out.write("calcQtyflag=false;"); 
          }%>

        
          writeScriptDisplay();
        </script>
        
        </td>
  	</tr>

</table>
</body>
</html:html>
<%!

    String effect(String s){
		  Hashtable h = new Hashtable();
        h.put("a","augments (no clinical effect)");
        h.put("A","augments");
        h.put("i","inhibts  (no clinical effect)");
        h.put("I","inhibits");
        h.put("n","has no effect on");
        h.put("N","has no effect on");
        h.put(" ","unknown effect on");
        
        String retval = (String) h.get(s);
        if (retval == null) {retval = "unknown effect on";}
        return retval;
   }
    
	String significance(String s){
       Hashtable h = new Hashtable();
       h.put("1","minor");
       h.put("2","moderate");
       h.put("3","major");
       h.put(" ","unknown");
       
       String retval = (String) h.get(s);
        if (retval == null) {retval = "unknown";}
        return retval;
   }
   
   String evidence(String s){
       Hashtable h = new Hashtable();
       h.put("P","poor");
       h.put("F","fair");
       h.put("G","good");
       h.put(" ","unknown");
       
       String retval = (String) h.get(s);
        if (retval == null) {retval = "unknown";}
        return retval;
   }


   String sigColor(String s){
       Hashtable h = new Hashtable();
       h.put("1","yellow");
       h.put("2","orange");
       h.put("3","red");
       h.put(" ","greenyellow");
       
       String retval = (String) h.get(s);
        if (retval == null) {retval = "greenyellow";}
        return retval;
   }
    

%>
