
<%@ page language="java"%>
<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*,java.util.*,oscar.oscarPrevention.*" %>
<%@ page import="oscar.oscarProvider.data.*,oscar.oscarWorkflow.*,oscar.oscarEncounter.oscarMeasurements.bean.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />


<%--
 //TODO: Mother's Information Doesn't save
 //TODO: Reason for injection needs to save better
 //TODO: Injection Input Needs styling
 //TODO: Page shuts when saving an Injection

 This Form works in two modes  no form started mode and started
 
 Use case 1 No form started 
 
 User comes into this 
 
 
 
 What happens when users look at the form from the form history??
 
 
--%>
<%
    String formClass = "RhImmuneGlobulin";
    String formLink = "formRhImmuneGlobulin.jsp";
    
//insert into encounterForm values ('RH Form', '../form/formRhImmuneGlobulin.jsp?demographic_no=', 'formRhImmuneGlobulin',0);

    String demographicNo = request.getParameter("demographic_no");
    if (demographicNo == null){
        demographicNo = (String) request.getAttribute("demographic_no");
    }    
    System.out.println("demographic_no "+demographicNo);
    int demoNo = Integer.parseInt(demographicNo);
    
    
    String formIdStr = "0";
    if(request.getParameter("formId") != null ){   ////TEMPORARY
        formIdStr = request.getParameter("formId");     
    }
    
    int formId = Integer.parseInt(formIdStr);
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(demoNo, formId);

    String project_home = request.getContextPath().substring(1);
    boolean bView = false;
    if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true;
    
    ArrayList providers = ProviderData.getProviderList();
    String prevDate = UtilDateUtilities.getToday("yyyy-MM-dd");
    String providerName = "";
    String provider = (String) session.getAttribute("user");
    

%>
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
 * GNU General Public License for more details. * 
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->
<html:html locale="true">
    <% response.setHeader("Cache-Control","no-cache");%>

    <head>
        <title>Rh Immune Globulin Injection Reporting Form</title>
        <html:base/>
        
        <link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" /> 
     
        <script type="text/javascript" src="../share/calendar/calendar.js" ></script>      
        <script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>" ></script>      
        <script type="text/javascript" src="../share/calendar/calendar-setup.js" ></script> 
        <script type="text/javascript" src="../share/prototype.js" ></script>
        <script type="text/javascript" src="../share/javascript/Oscar.js" ></script>

        <style type="text/css">
            a:link{
            text-decoration: none;
            color:#FFFFFF;
            }

            a:active{
            text-decoration: none;
            color:#FFFFFF;
            }

            a:visited{
            text-decoration: none;
            color:#FFFFFF;
            }

            a:hover{
            text-decoration: none;
            color:#FFFFFF;
            }

            .Head {
            background-color:#BBBBBB;
            padding-top:3px;
            padding-bottom:3px;
            width:740px;
            height: 30px;
            font-size:12pt;
            }

            .Head INPUT {
            width: 100px;
            }

            .Head A {
            font-size:12pt;
            }

            BODY {
            font-family: Arial, Verdana, Tahoma, Helvetica, sans-serif;             
            background-color: #FFF;            
            }

            TABLE {
            font-family: Arial, Verdana, Tahoma, Helvetica, sans-serif;
            }
        
            TD{
            font-size:13pt;
            }

            TH{
            font-size:14pt;
            font-weight: normal;            
            }

            .checkbox{
            height: 25px;
            width: 25px;     
            background-color: #FFFFFF;
            }

            .checkboxError{
            height: 25px;
            width: 25px;     
            background-color: red;
            }

            .subject {
            background-color: #000000;
            color: #FFFFFF;  
            font-size: 15pt;
            font-weight: bold;
            text-align: center;
            }

            .title {
            background-color: #486ebd;
            color: #FFFFFF;            
            font-weight: bold;
            text-align: left;
            }
            .subTitle {
            background-color: #F2F2F2;
            font-weight: bold;
            text-align: center;             
            }
            .question{
            text-align: left;
            }
        
            div.boxed1 {
            border-style: solid;
            margin: 10px;
            padding: 10px;
            }
        
            div.boxed2 {
            border-style: solid;
            margin: 10px;
            padding: 10px;
            }
        
            div.boxed2 label{
            float:left;
            width:160px;
            }
        
            div.boxed2 br{
            clear:left;
            }
          
            div.boxed2 input{
            float:left;
            margin-right: 3px;
           
            }
        
        
        
            label.smallmargin{
            float:left;
            width:70;
            }
        
        
        
            fieldset {
            margin-right: 10px;
            padding-right: 10px;
            margin-left: 10px;
            padding-left: 10px;
        
        
            }
        
            fieldset label{
            float:left;
            width:110px;
            }
            
            fieldset a {
              float:left;
            }
            
        
            fieldset br{
            clear:left;
            }
        
       
        
            fieldset input{
            float:left;
            margin-right: 3px;
           
            }
        
            fieldset select{
            float:left;
            }
        
            fieldset ul {
            list-style: none;
            }
        
        
        
            fieldset ul input {
            float:none;
            }
        
        
            fieldset.phys {
             margin-top: 10px;

            }
        
            fieldset.phys label{
            float:left;
            width:260px;
            }

        </style>
    </head>


    <script type="text/javascript" language="Javascript">
    
        var choiceFormat  = new Array(6,7,8,9,12,13);        
        var allNumericField = new Array(14,15);
        var allMatch = null;
        var action = "/<%=project_home%>/form/formname.do";        

    </script>
    
    <script type="text/javascript">
      function hideExtraName(ele){ 
       //alert(ele);
        if (ele.options[ele.selectedIndex].value != -1){
           hideItem('providerName');
           //alert('hidding');
        }else{                    
           showItem('providerName');
           document.getElementById('providerName').focus();
           //alert('showing');
        }                       
      }
      function showHideItem(id){ 
         if(document.getElementById(id).style.display == 'none')
                document.getElementById(id).style.display = ''; 
         else
                document.getElementById(id).style.display = 'none'; 
      }

        function showItem(id){
                document.getElementById(id).style.display = ''; 
        }

        function hideItem(id){
            document.getElementById(id).style.display = 'none'; 
        }

        function showHideNextDate(id,nextDate,nexerWarn){
            if(document.getElementById(id).style.display == 'none'){
                showItem(id);
            }else{
                hideItem(id);
                document.getElementById(nextDate).value = "";
                document.getElementById(nexerWarn).checked = false ;

            }        
        }

        function disableifchecked(ele,nextDate){        
            if(ele.checked == true){       
               document.getElementById(nextDate).disabled = true;       
            }else{                      
               document.getElementById(nextDate).disabled = false;              
            }
        }
    </script>     
    
    <script type="text/javascript" src="formScripts.js">
    
    </script>


    <body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0" onload="window.resizeTo(768,833)">
        <!--
        @oscar.formDB Table="formAdf" 
        @oscar.formDB Field="ID" Type="int(10)" Null="NOT NULL" Key="PRI" Default="" Extra="auto_increment"
        @oscar.formDB Field="demographic_no" Type="int(10)" Null="NOT NULL" Default="'0'" 
        @oscar.formDB Field="provider_no" Type="int(10)" Null="" Default="NULL" 
        @oscar.formDB Field="formCreated" Type="date" Null="" Default="NULL" 
        @oscar.formDB Field="formEdited" Type="timestamp"  
        -->
        
        
      
        
        <div class="title">Rh Immune Globulin Injection Reporting Form</div>
           
        
           <%   
             Hashtable h = null;
             String newFlowNeeded = (String) request.getAttribute("newWorkFlowNeeded"); 
             System.out.println("HERE 1");
           %>   
            <div>
                <fieldset>
                    <legend>Current Pregnancy </legend>
                <% 
                String workflowType = "RH";//request.getParameter("workflowType");
                    //WorkFlowState workFlow = new WorkFlowState();  
                    WorkFlowFactory flowFactory = new WorkFlowFactory();
                    WorkFlow flow = flowFactory.getWorkFlow(workflowType);
                    
                    ArrayList currentWorkFlows = flow.getActiveWorkFlowList(demographicNo);

                    if(currentWorkFlows != null && currentWorkFlows.size() > 0){
                        System.out.println("size of current workflows "+currentWorkFlows.size());
                        request.setAttribute("currentWorkFlow",currentWorkFlows.get(0));
                         h = (Hashtable) currentWorkFlows.get(0);
                    }
                    
                System.out.println("HERE 2");
                if(h != null){
                    String gestAge = "";
                    try{
                       gestAge = ""+UtilDateUtilities.calculateGestationAge( UtilDateUtilities.now(), (Date) h.get("completion_date"));
                    }catch(Exception gestAgeEx){}
                    %>
                    <span style="margin-right:20px;">EDD: <%=h.get("completion_date")%></span>
                    <!-- span style="margin-right:20px;">Start date: <%=h.get("create_date_time")%> </span -->
                    <span style="margin-right:20px;">Current State:<%=flow.getState(""+h.get("current_state"))%>  </span>
                    <span style="margin-right:20px;">Weeks: <%=gestAge%></span>
                <%}else{%>
                        <span style="margin-right:20px;">No Current Pregnancy</span>
                
                <%}%> 
                <br/>
                <html:form  action="/form/RHPrevention">
                    
                <%-- input type="hidden" name="demographic_no" value="<%= props.getProperty("demographic_no", "0") %>" / --%>
                <input type="hidden" name="formCreated" value="<%= props.getProperty("formCreated", "") %>" />
                <input type="hidden" name="form_class" value="<%=formClass%>" />
                <input type="hidden" name="form_link" value="<%=formLink%>" />
                <input type="hidden" name="formId" value="<%=formId%>" />
                <input type="hidden" name="submit" value="exit"/>    
                <input type="hidden" name="demographic_no" value="<%=demographicNo%>" />
                    
                <%if (h != null) { %>
                <input type="hidden" name="workflowId" value="<%=h.get("ID")%>"/>
              
                
                <label>Change State:</label>
                <select name="state">
                    <%ArrayList states = new ArrayList(flow.getStates());
                    for (int i = 0; i < states.size(); i++){
                      WFState state = (WFState) states.get(i);
                    %>
                    <option value="<%=state.getKey()%>" <%= ( state.getKey().equals(h.get("current_state")) ? " selected" : "" )%>><%=state.getName()%></option>
                    
                    <%}%>
                    
                </select>
                  <%} System.out.println("HERE 3");%>
                
               
                </fieldset>
                
                <fieldset>
                    <legend>Mother's Information</legend>
                    <label>Date of Referral:</label> 
                            <input type="text" name="dateOfReferral" id="dateOfReferral" size="9"  value="<%=props.getProperty("dateOfReferral","")%>" /> 
                            <a id="dateOfRefButton"><img title="Calendar" src="../images/cal.gif" alt="Calendar" border="0" /></a> 
                    <label>EDD:</label>   
                            <input type="text" name="edd" id="end_date" size="9" value="<%=props.getProperty("edd","")%>" > 
                            <a id="date"><img title="Calendar" src="../images/cal.gif" alt="Calendar" border="0" /></a> 
                    <br/>
                    
                    <label>Last Name:</label> <input type="text" name="motherSurname"  value="<%=props.getProperty("motherSurname","")%>"    /> 
                    <label>First Name:</label> <input type="text" name="motherFirstname" value="<%=props.getProperty("motherFirstname","")%>" /> 
                    <br/>
                    <label>Date of Birth:</label> <input type="text" name="dob" size="9" id="dob"   value="<%=props.getProperty("dob","")%>"/> 
                    <a id="dateOB"><img title="Calendar" src="../images/cal.gif" alt="Calendar" border="0" /></a> 
                    <br/>
                    
                    <label>Health Card #:</label> <input type="text" name="motherHIN"  value="<%=props.getProperty("motherHIN","")%>" /> 
                    <label>VC:</label> <input type="text" name="motherVC" size="3" value="<%=props.getProperty("motherVC","")%>" /> 
                    
                    <br/> 
                    <label>Address:</label> <input type="text" name="motherAddress"   value="<%=props.getProperty("motherAddress","")%>"/> 
                    <label>City:</label> <input type="text" name="motherCity"   value="<%=props.getProperty("motherCity","")%>"/> 
                    <br/>
                    <label>Province:</label> <input type="text" name="motherProvince"   value="<%=props.getProperty("motherProvince","")%>"/> 
                    <label>Postal Code:</label> <input type="text" name="motherPostalCode"   value="<%=props.getProperty("motherPostalCode","")%>"/> 
                    <br/>  
                    <label>ABO:</label> 
                            <select name="motherABO">
                                <option>Not Set</option> 
                                <option value="A" <%=props.getProperty("motherABO", "").equalsIgnoreCase("A")?"selected":""%>  >A</option>
                                <option value="B" <%=props.getProperty("motherABO", "").equalsIgnoreCase("B")?"selected":""%>  >B</option>
                                <option value="o" <%=props.getProperty("motherABO", "").equalsIgnoreCase("o")?"selected":""%>  >O</option>
                            </select>

                            <label class="smallmargin">Rh type:</label>  
                            <select name="motherRHtype">
                                <option >Not Set</option>
                                <option value="N" <%=props.getProperty("motherRHtype", "").equalsIgnoreCase("N")?"selected":""%> >Neg</option>
                                <option value="P" <%=props.getProperty("motherRHtype", "").equalsIgnoreCase("P")?"selected":""%> >Pos</option>
                            </select>
                    <br/> 
                    <label>Hospital for Delivery:</label> <input type="text" name="hospitalForDelivery" value="<%=props.getProperty("hospitalForDelivery","")%>"/> 
                </fieldset>
                
                
                <fieldset>
                    <legend>Physician / Midwife</legend>
                    
                    <label>Last Name:</label> <input type="text" name="refPhySurname"  value="<%=props.getProperty("refPhySurname","")%>"    /> 
                    <label>First Name:</label> <input type="text" name="refPhyFirstname" value="<%=props.getProperty("refPhyFirstname","")%>" /> 
                    <br/>
                    <label>Address:</label> <input type="text" name="refPhyAddress" size="20" value="<%=props.getProperty("refPhyAddress","")%>" /> 
                    <label>City:</label> <input type="text" name="refPhyCity"   value="<%=props.getProperty("refPhyCity","")%>"/> 
                    <br/>
                    <label>Province:</label> <input type="text" name="refPhyProvince"   value="<%=props.getProperty("refPhyProvince","")%>"/> 
                    <label>Postal Code:</label> <input type="text" name="refPhyPostalCode"   value="<%=props.getProperty("refPhyPostalCode","")%>"/> 
                    <br/>
                    <label>Telephone:</label> <input type="text" name="refPhyPhone"  value="<%=props.getProperty("refPhyPhone","")%>"    /> 
                    <label>Fax:</label> <input type="text" name="refPhyFax" value="<%=props.getProperty("refPhyFax","")%>" /> 
                    <br/>
                    
                    
                    
                </fieldset>
                <fieldset>
                    <legend>Comments</legend>
                    <textarea name="comments"style="width: 45em;"><%=props.getProperty("comments","")%></textarea>
                </fieldset>
                
                <input type="submit" value="Save"/> 
                <% System.out.println("HERE 4");%>
                
                <% 
                if ( h != null && h.get("ID") != null){ %>
                <input type="button" onClick="javascript: popup(700,600,'addRhInjection.jsp?demographic_no=<%=demographicNo%>&amp;workflowId=<%=h.get("ID")%>&amp;formId=<%=formId%>','addInjection');" value="Add Injection" />
                <%-- a style="color:blue; " href="javascript: function myFunction() {return false; }" onClick="popup(700,600,'addRhInjection.jsp?demographic_no=<%=demographicNo%>&amp;workflowId=<%=h.get("ID")%>&amp;formId=<%=formId%>','addInjection')">Add Injection</a --%>
                <%}%>
                <% System.out.println("HERE 5");%>
                </html:form>
                
                <% if (h != null) { 
                PreventionData pd = new PreventionData();
                ArrayList alist = pd.getPreventionDataFromExt("workflowId", ""+h.get("ID"));       
                
                for (int k = 0; k < alist.size(); k++){
                Hashtable hdata = (Hashtable) alist.get(k);
                Hashtable hextended = pd.getPreventionKeyValues(""+hdata.get("id"));
                %>    
                <fieldset>
                    <legend>
                        Injection # <%=k+1%>  
                        &nbsp; &nbsp; &nbsp; Date: <%=hdata.get("preventionDate")%>
                        &nbsp; &nbsp; &nbsp; Weeks: <%=UtilDateUtilities.calculateGestationAge( (Date) hdata.get("prevention_date_asDate") , (Date) h.get("completion_date"))%>
                    </legend>
                    Given By: <%=pd.getProviderName(hdata)%>
                    Location: <%=hextended.get("location")  %>
                    Lot #: <%=hextended.get("lot")  %>
                    Dosage: <%=hextended.get("dosage")  %>
                    </br>
                    Reason: <%=hextended.get("reason")  %>
                </fieldset>
               <%  }
                 }%>                           

            <script type="text/javascript">
            Calendar.setup( { inputField : "prevDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "date", singleClick : true, step : 1 } );
            Calendar.setup( { inputField : "end_date", ifFormat : "%Y-%m-%d", showsTime :false, button : "date", singleClick : true, step : 1 } );
            Calendar.setup( { inputField : "dob", ifFormat : "%Y-%m-%d", showsTime :false, button : "dateOB", singleClick : true, step : 1 } );
            Calendar.setup( { inputField : "dateOfReferral", ifFormat : "%Y-%m-%d", showsTime :false, button : "dateOfRefButton", singleClick : true, step : 1 } );
            hideExtraName(document.getElementById('providerDrop'));                                               
        </script>        
    </body>
</html:html>


