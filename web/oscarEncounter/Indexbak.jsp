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

<!-- May 22, 2002,-->
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="oscar.oscarEncounter.data.*,java.net.*"%>

<%
    response.setHeader("Cache-Control","no-cache");
    //The oscarEncounter session manager, if the session bean is not in the context it looks for a
    //session cookie with the appropriate name and value, if the required cookie is not available
    //it dumps you out to an erros page.

    oscar.oscarSecurity.CookieSecurity cs = new oscar.oscarSecurity.CookieSecurity();
    response.addCookie(cs.GiveMeACookie());
    oscar.oscarSecurity.SessionBean bean = null;
    if((bean=(oscar.oscarSecurity.SessionBean)request.getSession().getAttribute("SessionBean"))==null)
    {response.sendRedirect("error.jsp");
    return;}
%>
<!-- JSP variables -->
<%
// You will need these variables for the forms
oscar.util.DateUtilities dateConvert = new oscar.util.DateUtilities();
String demoNo = bean.demographicNo;
String provNo = bean.providerNo;
FormData.Form[] forms = new FormData().getForms();
PatientData.Patient pd = new PatientData().getPatient(demoNo);
ProviderData.Provider prov = new ProviderData().getProvider(provNo);
String patientName = pd.getFirstName()+" "+pd.getSurname();
String patientAge = pd.getAge();
String patientSex = pd.getSex();
String providerName = bean.userName;
%>

<html:html locale="true">
<html>
<head>
<title>oscarEncounter</title>
<html:base/>
<!-- This is from OscarMessenger to get the top and left borders on -->
<link rel="stylesheet" type="text/css" href="encounterStyles.css">
<!--<script type="application/x-javascript" language="javascript" src="/javascript/sizing.js"></script>-->
<script type="text/javascript" language=javascript>
    var X       = 10;
    var small   = 60;
    var normal  = 184;
    var large   = 435;
    var full    = 650;
//tilde operator function variables
    var handlePressState = 0;
    var handlePressFocus;
    var handlePressFocus2 = "none";
    var keyPressed;
    var textValue1;
    var textValue2;
    var textValue3;
//Nick, height has to be given in pixels in order to dynamically change it
//as far as i could find, there is no way to use the dom to change row values
    function closeEncounterWindow()
    {
        var x = window.confirm("Are you sure your wish to exit oscarEncounter wothout saving?");
        if(x)
        {window.close();}
    }
    function saveAndCloseEncounterWindow()
    {
        var x = window.confirm("Are you sure your wish to exit oscarEncounter wothout saving?");
        if(x)
        {window.close();}
    }
    //get another encounter from the select list
    function popUpImmunizations(vheight,vwidth,varpage) {
        var page = varpage;
        windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
        var popup=window.open(varpage, "immunizations", windowprops);
    }

    function popupStart1(vheight,vwidth,varpage) {
        var page = varpage;
        windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
        var popup=window.open(varpage, "oscarEncounter", windowprops);
    }
    function getAnotherEncounter(newAppointmentNo){
            varpage = "/oscarEncounter/IncomingEncounter.do?appointmentList=true&appointmentNo="+newAppointmentNo;
            location = varpage;
    }
    function insertTemplate(templateNo){
        var x = window.confirm("Adding a template without saving your changes may results in lost data. Are you sure?");
        if(x)
        {            varpage = "/oscarEncounter/IncomingEncounter.do?insertTemplate=true&templateNo="+templateNo;
            location = varpage;
}
    }

    //Li's search function java script
    //Li had top=50, left=50
    function refresh() {
  var u = self.location.href;
  if(u.lastIndexOf("&status=B") > 0) {
    self.location.href = u.substring(0,u.lastIndexOf("&status=B")) + "&status=P"  ;
  } else if (u.lastIndexOf("&status=") > 0) {
    self.location.href = u.substring(0,u.lastIndexOf("&status=")) + "&status=B"  ;
  } else {
    history.go(0);
  }
     	self.opener.refresh();  
    }
function onUnbilled(url) {
  if(confirm("You are about to delete the previous billing, are you sure?")) {
    popupPage(700,720, url);
  }
}
    function popupPage2(varpage) {
    var page = "" + varpage;
    windowprops = "height=600,width=700,location=no,"
    + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";
    window.open(page, "apptProviderSearch", windowprops);
    }
    function popupPage(vheight,vwidth,varpage) { //open a new popup window
      var page = "" + varpage;
      windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=600,screenY=200,top=0,left=0";
      var popup=window.open(page, "EncProvider", windowprops);
      if (popup != null) {
        if (popup.opener == null) {
          popup.opener = self;
          alert("hi this is a null for self!");
        }
      }
    }
    function urlencode(str) {
        var ns = (navigator.appName=="Netscape") ? 1 : 0;
        if (ns) { return escape(str); }
        var ms = "%25#23 20+2B?3F<3C>3E{7B}7D[5B]5D|7C^5E~7E`60";
        var msi = 0;
        var i,c,rs,ts ;
        while (msi < ms.length) {
            c = ms.charAt(msi);
            rs = ms.substring(++msi, msi +2);
            msi += 2;
            i = 0;
            while (true)	{
                i = str.indexOf(c, i);
                if (i == -1) break;
                ts = str.substring(0, i);
                str = ts + "%" + rs + str.substring(++i, str.length);
            }
        }
        return str;
    }
    function popupSearchPage(vheight,vwidth,varpage) { //open a new popup window
        alert(vheight +"<-height "+vwidth+"<-width " + varpage+"<-varpage");
        var page = "" + varpage;
        windowprop = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
        var popup=window.open(page, "searchpage", windowprop);
    }
    function popupStart(vheight,vwidth,varpage) {
      var page = varpage;
      windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
      var popup=window.open(varpage, "", windowprops);
      if (popup != null) {
        if (popup.opener == null) {
          popup.opener = self;
        }
      }
    }
    //This function governs the tilde operator functions
/*    function handlePress(evt){
            //alert("keyCode was " +window.event.keyCode+" handlePresssFocus is "+handlePressFocus+" and handlePressState is "+handlePressState);
            if(evt.which == null)
            {
                //Traps the first tilde
                if ( handlePressState ==0 && window.event.keyCode=="126")
                {
                    handlePressState  = 1;
                }
                //Catches all keypress sequences of ~d that happen in the same textarea and puts the date string in
                else if ((handlePressState ==1) && (window.event.keyCode=="100"))
                {
                    var date = new Date();
                    var year = date.getUTCFullYear()
                    var month = date.getMonth() +1;
                    if(month<10){month="0"+month;}
                    var day = date.getDate()
                    if(day <10){day="0"+day;}
                    handlePressState = 0;

                    textValue1 = document.encForm.shTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"["+year+"/"+month+"/"+day+"]");
                    document.encForm.shTextarea.value = textValue1;

                    textValue1 = document.encForm.fhTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"["+year+"/"+month+"/"+day+"]");
                    document.encForm.fhTextarea.value = textValue1;

                    textValue1 = document.encForm.mhTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"["+year+"/"+month+"/"+day+"]");
                    document.encForm.mhTextarea.value = textValue1;

                    textValue1 = document.encForm.ocTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"["+year+"/"+month+"/"+day+"]");
                    document.encForm.ocTextarea.value = textValue1;

                    textValue1 = document.encForm.enTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"["+year+"/"+month+"/"+day+"]");
                    document.encForm.enTextarea.value = textValue1;

                    window.event.keyCode = 0;
                }
                //Puts the reason for the appointment in
                else if ((handlePressState ==1) && (window.event.keyCode=="114"))
                {
                    handlePressState = 0;

                    textValue1 = document.encForm.shTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"[Reason for Visit:<%=bean.reason%>]");
                    document.encForm.shTextarea.value = textValue1;

                    textValue1 = document.encForm.fhTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"[Reason for Visit:<%=bean.reason%>]");
                    document.encForm.fhTextarea.value = textValue1;

                    textValue1 = document.encForm.mhTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"[Reason for Visit:<%=bean.reason%>]");
                    document.encForm.mhTextarea.value = textValue1;

                    textValue1 = document.encForm.ocTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"[Reason for Visit:<%=bean.reason%>]");
                    document.encForm.ocTextarea.value = textValue1;

                    textValue1 = document.encForm.enTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"[Reason for Visit:<%=bean.reason%>]");
                    document.encForm.enTextarea.value = textValue1;

                    window.event.keyCode = 0;
                }

                //Signs the doctor's name
                else if ((handlePressState ==1) && (window.event.keyCode=="115"))
                {
                    var date = new Date();
                    var year = date.getUTCFullYear()
                    var month = date.getMonth() +1;
                    var hours = date.getHours();
                    var minutes = date.getMinutes();
                    if(month<10){month="0"+month;}
                    var day = date.getDate()
                    if(day <10){day="0"+day;}
                    handlePressState = 0;

                    textValue1 = document.encForm.shTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"[Signed:"+year+"/"+month+"/"+day+":"+hours+":"+minutes+ " <%=bean.userName%>]" );
                    document.encForm.shTextarea.value = textValue1;

                    textValue1 = document.encForm.fhTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"[Signed:"+year+"/"+month+"/"+day+":"+hours+":"+minutes+ " <%=bean.userName%>]" );
                    document.encForm.fhTextarea.value = textValue1;

                    textValue1 = document.encForm.mhTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"[Signed:"+year+"/"+month+"/"+day+":"+hours+":"+minutes+ " <%=bean.userName%>]" );
                    document.encForm.mhTextarea.value = textValue1;

                    textValue1 = document.encForm.ocTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"[Signed:"+year+"/"+month+"/"+day+":"+hours+":"+minutes+ " <%=bean.userName%>]" );
                    document.encForm.ocTextarea.value = textValue1;

                    textValue1 = document.encForm.enTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"[Signed:"+year+"/"+month+"/"+day+":"+hours+":"+minutes+ " <%=bean.userName%>]" );
                    document.encForm.enTextarea.value = textValue1;

                    window.event.keyCode = 0;
                }

            //brings the user to view the first row
               else if ((handlePressState ==1) && (window.event.keyCode=="49"))
               {
                  //  var myBody=document.getElementsByTagName("body").item(0);
                  //  var myTableL1=myBody.getElementsByTagName("table").item(0);
                  //  var myTableL2=myTableL1.getElementsByTagName("table").item(0);
                    handlePressState = 0;
                    handlePressFocus = "none";
                    document.encForm.shInput.scrollIntoView(top);
                    window.event.keyCode = 0;

                    textValue1 = document.encForm.shTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"");
                    document.encForm.shTextarea.value = textValue1;

                    textValue1 = document.encForm.fhTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"");
                    document.encForm.fhTextarea.value = textValue1;

                    textValue1 = document.encForm.mhTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"");
                    document.encForm.mhTextarea.value = textValue1;

                    textValue1 = document.encForm.ocTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"");
                    document.encForm.ocTextarea.value = textValue1;

                    textValue1 = document.encForm.enTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"");
                    document.encForm.enTextarea.value = textValue1;

                }
            //brings the user to view the second row
               else if ((handlePressState ==1) && (window.event.keyCode=="50"))
                {
                    handlePressState = 0;
                    handlePressFocus = "none";
                    document.encForm.ocInput.scrollIntoView(top);
                    window.event.keyCode = 0;


                    textValue1 = document.encForm.shTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"");
                    document.encForm.shTextarea.value = textValue1;

                    textValue1 = document.encForm.fhTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"");
                    document.encForm.fhTextarea.value = textValue1;

                    textValue1 = document.encForm.mhTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"");
                    document.encForm.mhTextarea.value = textValue1;

                    textValue1 = document.encForm.ocTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"");
                    document.encForm.ocTextarea.value = textValue1;

                    textValue1 = document.encForm.enTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"");
                    document.encForm.enTextarea.value = textValue1;

                }
            //brings the user to view the last row
                else if ((handlePressState ==1) && (window.event.keyCode=="51"))
                {
                    handlePressState = 0;
                    handlePressFocus = "none";
                    document.encForm.enInput.scrollIntoView(top);

                    window.event.keyCode = 0;

                    textValue1 = document.encForm.shTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"");
                    document.encForm.shTextarea.value = textValue1;

                    textValue1 = document.encForm.fhTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"");
                    document.encForm.fhTextarea.value = textValue1;

                    textValue1 = document.encForm.mhTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"");
                    document.encForm.mhTextarea.value = textValue1;

                    textValue1 = document.encForm.ocTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"");
                    document.encForm.ocTextarea.value = textValue1;

                    textValue1 = document.encForm.enTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"");
                    document.encForm.enTextarea.value = textValue1;

                }
                //Catches all codes not recognized and resets
                else if (handlePressState ==1)
                {
                    handlePressState = 0;
                    textValue1 = document.encForm.shTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"");
                    document.encForm.shTextarea.value = textValue1;

                    textValue1 = document.encForm.fhTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"");
                    document.encForm.fhTextarea.value = textValue1;

                    textValue1 = document.encForm.mhTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"");
                    document.encForm.mhTextarea.value = textValue1;

                    textValue1 = document.encForm.ocTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"");
                    document.encForm.ocTextarea.value = textValue1;

                    textValue1 = document.encForm.enTextarea.value;
                    textValue1 = textValue1.replace(/~/g,"");
                    document.encForm.enTextarea.value = textValue1;

                    window.event.keyCode = 0;

                }
            }
            //The w3c parahraph
            else
            {
                keyPressed = evt.which;
                alert("w3c keyPressed == "+keyPressed);
            }

    }*/
function reset()
{
    document.encForm.shTextarea.style.overflow="auto";
    document.encForm.fhTextarea.style.overflow="auto";
    document.encForm.mhTextarea.style.overflow="auto";
    document.encForm.shTextarea.style.height=small;
    document.encForm.fhTextarea.style.height=small;
    document.encForm.mhTextarea.style.height=small;
    document.encForm.ocTextarea.style.overflow="auto";
    document.encForm.reTextarea.style.overflow="auto";
    document.encForm.ocTextarea.style.height=small;
    document.encForm.reTextarea.style.height=small;
    document.encForm.enTextarea.style.overflow="auto";
    document.encForm.enTextarea.style.height=large;


}
function rowOneX()
{
    document.encForm.shTextarea.style.overflow="auto";
    document.encForm.fhTextarea.style.overflow="auto";
    document.encForm.mhTextarea.style.overflow="auto";
    document.encForm.shTextarea.style.height=X;
    document.encForm.fhTextarea.style.height=X;
    document.encForm.mhTextarea.style.height=X;
}
function rowOneSmall()
{
    document.encForm.shTextarea.style.overflow="auto";
    document.encForm.fhTextarea.style.overflow="auto";
    document.encForm.mhTextarea.style.overflow="auto";
    document.encForm.shTextarea.style.height=small;
    document.encForm.fhTextarea.style.height=small;
    document.encForm.mhTextarea.style.height=small;
}
function rowOneNormal()
{
    document.encForm.shTextarea.style.overflow="auto";
    document.encForm.fhTextarea.style.overflow="auto";
    document.encForm.mhTextarea.style.overflow="auto";
    document.encForm.shTextarea.style.height=normal;
    document.encForm.fhTextarea.style.height=normal;
    document.encForm.mhTextarea.style.height=normal;
}
function rowOneLarge()
{
    document.encForm.shTextarea.style.overflow="auto";
    document.encForm.fhTextarea.style.overflow="auto";
    document.encForm.mhTextarea.style.overflow="auto";
    document.encForm.shTextarea.style.height=large;
    document.encForm.fhTextarea.style.height=large;
    document.encForm.mhTextarea.style.height=large;
}
function rowOneFull()
{
    document.encForm.shTextarea.style.overflow="auto";
    document.encForm.fhTextarea.style.overflow="auto";
    document.encForm.mhTextarea.style.overflow="auto";
    document.encForm.shTextarea.style.height=full;
    document.encForm.fhTextarea.style.height=full;
    document.encForm.mhTextarea.style.height=full;
    document.encForm.shInput.scrollIntoView(top);
}


function rowTwoX()
{
    document.encForm.ocTextarea.style.overflow="auto";
    document.encForm.reTextarea.style.overflow="auto";
    document.encForm.ocTextarea.style.height=X;
    document.encForm.reTextarea.style.height=X;
}
function rowTwoSmall()
{
    document.encForm.ocTextarea.style.overflow="auto";
    document.encForm.reTextarea.style.overflow="auto";
    document.encForm.ocTextarea.style.height=small;
    document.encForm.reTextarea.style.height=small;
}
function rowTwoNormal()
{
    document.encForm.ocTextarea.style.overflow="auto";
    document.encForm.reTextarea.style.overflow="auto";
    document.encForm.ocTextarea.style.height=normal;
    document.encForm.reTextarea.style.height=normal;
}
function rowTwoLarge()
{
    document.encForm.ocTextarea.style.overflow="auto";
    document.encForm.reTextarea.style.overflow="auto";
    document.encForm.ocTextarea.style.height=large;
    document.encForm.reTextarea.style.height=large;
}
function rowTwoFull()
{
    document.encForm.ocTextarea.style.overflow="auto";
    document.encForm.reTextarea.style.overflow="auto";
    document.encForm.ocTextarea.style.height=full;
    document.encForm.reTextarea.style.height=full;
    document.encForm.ocInput.scrollIntoView(top);
}

function rowThreeX()
{
    document.encForm.enTextarea.style.overflow="auto";
    document.encForm.enTextarea.style.height=X;
}
function rowThreeSmall()
{
    document.encForm.enTextarea.style.overflow="auto";
    document.encForm.enTextarea.style.height=small;
}
function rowThreeNormal()
{
    document.encForm.enTextarea.style.overflow="auto";
    document.encForm.enTextarea.style.height=normal;
}
function rowThreeLarge()
{
    document.encForm.enTextarea.style.overflow="auto";
    document.encForm.enTextarea.style.height=large;
}
function rowThreeFull()
{
    document.encForm.enTextarea.style.overflow="auto";
    document.encForm.enTextarea.style.height=full;
    document.encForm.enInput.scrollIntoView(top);
}



function popupSearchPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprop = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=0,left=0";
  var popup=window.open(page, "searchpage", windowprop);
}

if (!document.all) document.captureEvents(Event.MOUSEUP);
document.onmouseup = getActiveText;
function getActiveText(e) {
  //text = (document.all) ? document.selection.createRange().text : document.getSelection();
  //document.ksearch.key.value = text;
 if(document.all) {
    text = document.selection.createRange().text;
    if(text != "" && document.ksearch.key.value=="") {
      document.ksearch.key.value += text;
    }
    if(text != "" && document.ksearch.key.value!="") {
      document.ksearch.key.value = text;
    }
  } else {
    text = document.getSelection();
    document.ksearch.key.value = text;
  }
  return true;
}
function popupPageK(page) {
    windowprops = "height=700,width=960,location=no,"
    + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";
    window.open(page, "apptProviderSearch", windowprops);
}
function selectBox(name) {

    to = name.options[name.selectedIndex].value;
    name.selectedIndex =0;
//    alert(to);
    if(to!="null")
        popupPageK(to);
}
function popupOscarRx(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "oscarRx", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}
function popupOscarCon(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "oscarConsultation", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}
function goToSearch() { //open a new popup window

        var x = window.confirm("Entering search without first saving encounter to lost date, are you sure?");
        if(x)
        {  location = "/oscarEncounter/search/DemographicSearch.jsp";}



}
function sign(){
        document.encForm.enTextarea.value =document.encForm.enTextarea.value +"\n[Signed on <%=dateConvert.DateToString(bean.currentDate)%> by <%=bean.userName%>]";
}

function saveEncounter(){
        location="SaveEncounter.do";
}

function saveSignEncounter(){
        location="SaveEncounter.do";

}


function loader(){
    window.focus();
//    document.encForm.enTextarea.focus();
    var tmp;

//failed attempt to get all the boxes to scroll to the end
//    document.encForm.shTextarea.focus();
//    tmp= document.encForm.shTextarea.style.value;
//    document.encForm.shTextarea.style.value="";
//    document.encForm.shTextarea.style.value=tmp;
//
//    document.encForm.fhTextarea.focus();
//    tmp= document.encForm.fhTextarea.style.value;
//    document.encForm.fhTextarea.style.value="";
//    document.encForm.fhTextarea.style.value=tmp;
//
//    document.encForm.mhTextarea.focus();
//    tmp= document.encForm.mhTextarea.style.value;
//    document.encForm.mhTextarea.style.value="";
//    document.encForm.mhTextarea.style.value=tmp;
//
//    document.encForm.ocTextarea.focus();
//    tmp= document.encForm.ocTextarea.style.value;
//    document.encForm.ocTextarea.style.value="";
//    document.encForm.ocTextarea.style.value=tmp;
//
//    document.encForm.reTextarea.focus();
//    tmp= document.encForm.reTextarea.style.value;
//    document.encForm.reTextarea.style.value="";
//    document.encForm.reTextarea.style.value=tmp;

    document.encForm.enTextarea.focus();
    tmp = document.encForm.enTextarea.value;
    document.encForm.enTextarea.value = tmp;

}


</script>

</head>

<body  onload="javascript:loader();"  topmargin="0" leftmargin="0" vlink="#0000FF">
<html:errors/>

<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse;width:100%;height:680" bordercolor="#111111" id="scrollNumber1" name="encounterTable">
    <tr>
        <td  bgcolor="#003399" style="border-left: 2px solid #A9A9A9;border-right: 2px solid #A9A9A9;height:34px;" >
            <div class="Title">
                oscarEncounter
            </div>
        </td>


        <td  bgcolor="#003399" style="text-align:right;border-left: 2px solid #A9A9A9;height:34px;padding-left:3px;" >
                <table name="tileTable" style="veritcal-align:middle;border-collapse:collapse;" >
                    <form name="appointmentListForm" action="/oscarEncounter/IncomingEncounter.do">
                    <tr>
                        <td class="Header" style="padding-left:2px;padding-right:2px;border-right:2px solid #003399;text-align:left;font-size:80%;font-weight:bold;width:100%;" NOWRAP >
                            <%=bean.patientLastName %>, <%=bean.patientFirstName%> <%=bean.patientSex%> <%=bean.patientAge%>
                        </td>
                        <td class="Header" style="text-align:right;font-size:80%;font-weight:bold" NOWRAP >
                            Today's Appointments:
                        </td>
                        <td class="Header" style="border-right:2px solid #003399">
                                            <select   style="font-size:80%;" name="selectList" onchange="javascript:getAnotherEncounter(document.appointmentListForm.selectList.options[document.appointmentListForm.selectList.selectedIndex].value)">
                                           <%for(int i=0; i<bean.appointmentsIdArray.size();i++){ %>
                                           <%if(bean.appointmentsIdArray.get(i).equals(bean.appointmentNo)){%>
                                           <option selected value="<%=bean.appointmentsIdArray.get(i) %>"><%=bean.appointmentsNamesArray.get(i)%></option>
                                           <%}else{%>
                                           <option value="<%=bean.appointmentsIdArray.get(i) %>"><%=bean.appointmentsNamesArray.get(i)%></option>
                                           <%}%>
                                           <%}
                                           %>
                                           </select>
                        </td>
                        <td class="Header" style="text-align:center;border-right: 3px solid #003399" NOWRAP>
                        <div class="FakeLink">
                                <a href="javascript:popupStart(300,400,'Help.jsp')"  >Help</a> | <a href="javascript:popupStart(300,400,'About.jsp')" >About</a> | <a href="javascript:popupStart(480,880,'License.jsp')" >License</a>
                        </div>
                        </td>
                    </tr>
                    </form>
                </table>

            </td>
    </tr>
    <tr style="height:100%">
        <td style="font-size:80%;border-top:2px solid #A9A9A9;border-bottom:2px solid #A9A9A9;vertical-align:top">
            <table class="LeftTable">
                <tr class="Header">
                    <td style="font-weight:bold">
                        Clinical Modules
                    </td>
                </tr>
                <tr>
                    <td>
                        <a href=# onClick="popupPage2('../oscar_sfhc/demographic/demographiccontrol.jsp?demographic_no=<%=bean.demographicNo%>&displaymode=edit&dboperation=search_detail');return false;"
                        title="Master file">master</a><br>
<% if(bean.status.indexOf('B')==-1) { %><a href=# onClick='popupPage(700,1000, "../oscar_sfhc/billing/billingOB.jsp?billForm=<%=URLEncoder.encode("MFP")%>&hotclick=<%=URLEncoder.encode("")%>&appointment_no=<%=bean.appointmentNo%>&demographic_name=<%=URLEncoder.encode(bean.patientLastName+","+bean.patientFirstName)%>&demographic_no=<%=bean.demographicNo%>&providerview=<%=bean.curProviderNo%>&user_no=<%=bean.providerNo%>&apptProvider_no=<%=bean.curProviderNo%>&appointment_date=<%=bean.appointmentDate%>&start_time=<%=bean.startTime%>&bNewForm=1");return false;' title="Billing">Billing</a>
<% } else {%>  
    <a href=# onClick='onUnbilled("../oscar_sfhc/billing/billingDeleteWithoutNo.jsp?appointment_no=<%=bean.appointmentNo%>");return false;' title="Unbil">-Billing</a>
<% } %>          
<br>
                        <a href=# onClick="popupOscarRx(700,960,'../oscarRx/choosePatient.do?providerNo=<%=bean.providerNo%>&demographicNo=<%=bean.demographicNo%>')">prescriptions</a>
                        <a href=# onClick="popupOscarCon(700,960,'oscarConsultationRequest/ConsultationFormRequest.jsp')">consultations</a><br>
                        <a href="javascript:popUpImmunizations(700,960,'immunization/initSchedule.do')">immunizations</a><br>
                    </td>
                </tr>
                <tr><td>&nbsp;</td></tr>
            </table>


            <table class="LeftTable">
            <form name="leftColumnForm">
                <tr class="Header">
                    <td style="font-weight:bold">
                        Forms
                    </td>
                </tr>
                <tr>
                    <td>
                        <select name="selectCurrentForms" onChange="javascript:selectBox(this)" class="ControlSelect" onMouseOver="javascript:window.status='View any of <%=patientName%>\'s current forms.'; return true;">
                            <option value="null" selected>-current forms-
                            <%
                            for(int j=0; j<forms.length; j++) {
                                FormData.Form frm = forms[j];
                                String table = frm.getFormTable();
                                FormData.PatientForm[] pforms = new FormData().getPatientForms(demoNo, table);
                                if(pforms.length>0)
                                {
                                    FormData.PatientForm pfrm = pforms[0];
                            %>
                            <option value="<%=frm.getFormPage()+demoNo+"&formId="+pfrm.getFormId()+"&provNo="+provNo%>"><%=frm.getFormName()%>&nbsp;Cr:<%=pfrm.getCreated()%>&nbsp;Ed:<%=pfrm.getEdited()%>
                            <%}}

                            %>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <select name="selectOldForms" onChange="javascript:selectBox(this)" class="ControlSelect" onMouseOver="javascript:window.status='View any of <%=patientName%>\'s past forms.'; return true;">
                            <option value="null" selected>-old forms-
                            <%
                            for(int j=0; j<forms.length; j++) {
                                FormData.Form frm = forms[j];
                                String table = frm.getFormTable();
                                FormData.PatientForm[] pforms = new FormData().getPatientForms(demoNo, table);
                                for(int i=1; i<pforms.length; i++) {
                                    FormData.PatientForm pfrm = pforms[i];
                            %>
                                    <option value="<%=frm.getFormPage()+demoNo+"&formId="+pfrm.getFormId()+"&provNo="+provNo%>"><%=frm.getFormName()%>&nbsp;Cr:<%=pfrm.getCreated()%>&nbsp;Ed:<%=pfrm.getEdited()%>
                            <%}}
                            %>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <select name="selectForm" onChange="javascript:selectBox(this)" class="ControlSelect" onMouseOver="javascript:window.status='Create a new form for <%=patientName%>.'; return true;">
                        <option value="null" selected>-add new form-
                        <%
                        //FormData.Form[] forms = new FormData().getForms();
                        for(int j=0; j<forms.length; j++) {
                            FormData.Form frm = forms[j];
                        %>
                        <option value="<%=frm.getFormPage()+demoNo+"&formId=0&provNo="+provNo%>"><%=frm.getFormName()%>
                        <%

                        }

                        %>
                        </select>

                    </td>
                </tr>

                <tr><td>&nbsp;</td></tr>
            </form>
            </table>


            <table class="LeftTable">
            <form name="insertTemplateForm">
                <tr class="Header">
                    <td style="font-weight:bold">
                        Encounter Templates
                    </td>
                </tr>
                <tr>
                    <td>
                        <select name="templateSelect" class="ControlSelect" onchange="javascript:insertTemplate(document.insertTemplateForm.templateSelect.options[document.insertTemplateForm.templateSelect.selectedIndex].value)">
                        <option value="null" selected>-insert template-
                         <%
                            String encounterTmp ="NONE";
                            for(int j=0; j<bean.templateNames.size(); j++) {
                            encounterTmp = (String)bean.templateNames.get(j);
                         %>
                            <option value="<%=j%>"><%=encounterTmp %>
                         <%}%>
                        </select>
                    </td>
                </tr>
                <tr><td>&nbsp;</td></tr>
            </form>
            </table>
            <table class="LeftTable">
                <tr class="Header">
                    <td style="font-weight:bold">
                            Clinical Resources
                    </td>
                </tr>
                <tr>
                    <td>
                        <a href="#" ONCLICK ="popupPage2('http://209.61.188.77:8888/oscarResource/');return false;" title="Resources" onmouseover="window.status='View Resources';return true">resource</a><br>
                        <a href="#" onClick="popupPage(500,600,'../oscar_sfhc/dms/documentReport.jsp?function=demographic&doctype=lab&functionid=<%=bean.demographicNo%>&curUser=<%=bean.curProviderNo%>');return false;">documents</a><br>
                        <a href="#" onClick="popupPage(500,600, '../oscar_sfhc/e_form/ShowMyForm.jsp?demographic_no=<%=bean.demographicNo%>');return false;">E-Forms</a>
                    </td>
                </tr>
                <tr><td>&nbsp;</td></tr>
            </table>
            <table>
                <tr class="Header">
                    <td style="font-weight:bold">
                            Internet Resources
                    </td>
                </tr>
                <tr class="LeftTable">
                    <td>
                        <form class="LeftTable" name="ksearch" onsubmit="popupSearchPage(600,800,this.channel.options[this.channel.selectedIndex].value+urlencode(this.key.value) ); return false;">
                                search for...
                                    <input class="ControlSelect" type="text" size="14" name="key" value="">
                                using...
                                    <select class="ControlSelect" name="channel" size="1">
                                        <option value="http://emr.skolar.com/gateway?tfUsername=dchan@mcmaster.ca&pwPassword=david&url=/emr/Search.jsp&query=">Skolar</option>
                                        <option value="https://209.61.188.77:8443/oscar_slp/Greeting.jsp?firstkeyword=">Self-Learning</option>
                                        <option value="http://www.google.com/search?q=">Google</option>
                                        <option value="http://www.ncbi.nlm.nih.gov/entrez/query/static/clinical.html">Pubmed</option>
                                    </select>
                                <input type="submit" class="ControlPushButton" value=" go " name="submit">
                        </form>
                    </td>
                </tr>

            </table>
        </td>
        <td style="border-left: 2px solid #A9A9A9;border-right: 2px solid #A9A9A9;border-bottom:2px solid #A9A9A9;" valign="top">
        <form name="encForm" action="/oscarEncounter/SaveEncounter.do" method="POST">
            <table  name="encounterTableRightCol" >
<!----Start new rows here-->
                <tr>
                    <td>
                        <table bgcolor="#CCCCFF" id="rowOne" >
                            <tr>
                               <td>
                                    <div class="RowTop" >Social History:</div><input type="hidden" name="shInput"/>
                                </td>
                                <td>
                                    <div class="RowTop" >Family History:</div>
                                </td>
                                <td>
                                    <div class="RowTop" >Medical History:</div>
                                </td>
                                <td>
                                    <div style="font-size:8pt;text-align:right;vertical-align:bottom">
                                    <a onMouseOver="javascript:window.status='Minimize'; return true;" href="javascript:rowOneX();">
                                        X</a> |
                                    <a onMouseOver="javascript:window.status='Small Size'; return true;" href="javascript:rowOneSmall();">
                                        S</a> |
                                    <a onMouseOver="javascript:window.status='Medium Size'; return true;" href="javascript:rowOneNormal();" >
                                        N</a> |
                                    <a onMouseOver="javascript:window.status='Large Size'; return true;" href="javascript:rowOneLarge();" >
                                        L</a> |
                                    <a onMouseOver="javascript:window.status='Full Size'; return true;" href="javascript:rowOneFull();">
                                        F</a> |
                                    <a onMouseOver="javascript:window.status='Full Size'; return true;" href="javascript:reset();">
                                        R</a>
                                    </div>
                                </td>
                            </tr>
                            <tr width="100%">
                                <!----This is the Social History cell ...sh...-->
                                <td  valign="top">
                                    <!-- Creating the table tag within the script allows you to adjust all table sizes at once, by changing the value of leftCol -->
                                       <textarea name='shTextarea' wrap="hard"  cols= "31" style="height:60;overflow:auto"><%=bean.socialHistory%></textarea>
                                </td>
                                <!----This is the Family History cell ...fh...-->
                                <td  valign="top">
                                       <textarea name='fhTextarea' wrap="hard"  cols= "31" style="height:60;overflow:auto"><%=bean.familyHistory%></textarea>
                                </td>
                                <!----This is the Medical History cell ...mh...-->
                                <td  valign="top" colspan="2">
                                       <textarea name='mhTextarea' wrap="hard"  cols= "31" style="height:60;overflow:auto"><%=bean.medicalHistory%></textarea>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
    <!--social history row-->
                <tr>
                    <td>
                        <table bgcolor="#CCCCFF" name="rowTwo">
                            <tr>
                                <td>
                                    <div class="RowTop" >Ongoing Concerns:</div><input type="hidden" name="ocInput"/>
                                </td>

                                <td>
                                    <div class="RowTop" >Reminders:</div>
                                </td>
                                <td>
                                    <div style="font-size:8pt;text-align:right;vertical-align:bottom">
                                    <a onMouseOver="javascript:window.status='Minimize'; return true;" href="javascript:rowTwoX();">
                                        X</a> |
                                    <a onMouseOver="javascript:window.status='Small Size'; return true;" href="javascript:rowTwoSmall();">
                                        S</a> |
                                    <a onMouseOver="javascript:window.status='Medium Size'; return true;" href="javascript:rowTwoNormal();" >
                                        N</a> |
                                    <a onMouseOver="javascript:window.status='Large Size'; return true;" href="javascript:rowTwoLarge();" >
                                        L</a> |
                                    <a onMouseOver="javascript:window.status='Full Size'; return true;" href="javascript:rowTwoFull();">
                                        F</a> |
                                    <a onMouseOver="javascript:window.status='Full Size'; return true;" href="javascript:reset();">
                                        R</a>
                               </td>
                            </tr>

                            <tr width="100%">
                                <td valign="top" style="border-right:2px solid #ccccff">
                                       <textarea name='ocTextarea' wrap="hard" cols="48" style="height:60;overflow:auto"><%=bean.ongoingConcerns%></textarea>
                                </td>
                                <td colspan= "2" valign="top">
                                       <textarea name='reTextarea' wrap="hard" cols="48" style="height:60;overflow:auto"><%=bean.reminders%></textarea>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
    <!--encounter row-->
                <tr>
                    <td>
                        <table bgcolor="#CCCCFF" name="rowThree">
                           <tr>
                                <td>
                                    <div class="RowTop" >Encounter: <%=bean.reason%></div><input type="hidden" name="enInput"/>
                                </td>
                                <td>
                                    <div style="font-size:8pt;text-align:right;vertical-align:bottom">
                                    <a onMouseOver="javascript:window.status='Minimize'; return true;" href="javascript:rowThreeX();">
                                        X</a> |
                                    <a onMouseOver="javascript:window.status='Small Size'; return true;" href="javascript:rowThreeSmall();">
                                        S</a> |
                                    <a onMouseOver="javascript:window.status='Medium Size'; return true;" href="javascript:rowThreeNormal();" >
                                        N</a> |
                                    <a onMouseOver="javascript:window.status='Large Size'; return true;" href="javascript:rowThreeLarge();" >
                                        L</a> |
                                    <a onMouseOver="javascript:window.status='Full Size'; return true;" href="javascript:rowThreeFull();">
                                        F</a> |
                                    <a onMouseOver="javascript:window.status='Full Size'; return true;" href="javascript:reset();">
                                        R</a>
                                   </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" valign="top" style="text-align:right">
                                    <textarea name='enTextarea' wrap="hard" cols="99" style="border-right:6px solid #ccccff;border-right:6px solid #ccccff;height:435;overflow:auto"><%=bean.encounter%><%if(bean.eChartTimeStamp==null){%><%="\n["+dateConvert.DateToString(bean.currentDate)+" .: "+bean.reason+"]\n"%><%}
                                        else if(bean.currentDate.compareTo(bean.eChartTimeStamp)>0)
                                        {%><%="\n["+dateConvert.DateToString(bean.currentDate)+" .: "+bean.reason+"]\n"%><%}
                                        if(!bean.template.equals("")){%><%=bean.template%><%}%></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" style="text-align:right">
                                    <input type="submit" style="height:20px"  name="buttonPressed" value="Save" class="ControlPushButton">
                                    <input type="submit" style="height:20px"  name="buttonPressed" value="Sign,Save and Exit" class="ControlPushButton">
                                    <input type="button" style="height:20px" onclick="javascript:closeEncounterWindow()" name="buttonPressed" value="Exit" class="ControlPushButton">
                                    <input type="button" style="height:20px" onclick="javascript:goToSearch()" name="buttonPressed" value="Search New Patient" class="ControlPushButton">
                                </td>

                            </tr>
                        </table>
                    </td>
                </tr>
<!----End new rows here-->
            </table>
            </form>
        </td>
    </tr>
    <tr style="height:100%">
        <td>
        &nbsp;
        </td>
    </tr>
</table>

</body>
</html:html>

