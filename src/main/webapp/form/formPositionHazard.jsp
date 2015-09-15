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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page language="java"%>
<%@ page
	import="oscar.form.*, oscar.OscarProperties, java.util.Date, oscar.util.UtilDateUtilities"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@page import="org.oscarehr.util.LoggedInInfo" %>

<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Position Hazard Communication Form</title>
<html:base />
<link rel="stylesheet" type="text/css" media="screen"
	href="positionHazardStyle.css">
<link rel="stylesheet" type="text/css" media="print" href="print.css">
<script src="../share/javascript/prototype.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<%
   String formClass = "PositionHazard";
   String formLink = "formPositionHazard.jsp";

   boolean readOnly = false;
   int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
   int formId = Integer.parseInt(request.getParameter("formId"));
   
   FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
   java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);
   
   if (request.getParameter("readOnly") != null){
      readOnly = true;
   }

   request.removeAttribute("submit");
%>


<script type="text/javascript" language="Javascript">
    var temp;
    temp = "";

    function onPrintPDF() {
                 
      temp=document.forms[0].action;
      document.forms[0].action = "<rewrite:reWrite jspPage="formname.do?__title=Position+Hazard&__cfgfile=PositionHazardPrint_pg1&__cfgfile=PositionHazardPrint_pg2&__template=PositionHazardForm"/>";
      document.forms[0].submit.value="printall";
      document.forms[0].target="_self";

        return true;
    }
    function onSave() {
        if (temp != "") { document.forms[0].action = temp; }
        document.forms[0].target="_self";
        document.forms[0].submit.value="save";
        return true;
    }

    function onSaveExit() {
        if (temp != "") { document.forms[0].action = temp; }
        document.forms[0].target="_self";
        document.forms[0].submit.value="exit";

        ret = confirm("Are you sure you wish to save and close this window?");
        return ret;
    }
    
</script>


<body style="page: doublepage; page-break-after: right">
<html:form action="/form/formname">
    <input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
    <input type="hidden" name="form_class" value="<%=formClass%>" />
    <input type="hidden" name="form_link" value="<%=formLink%>" />
    <input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
    <input type="hidden" name="ID"
		value="<%= props.getProperty("ID", "0") %>" />
    <input type="hidden" name="submit" value="exit" />

    <table class="Head" class="hidePrint">
        <tr>
                <td nowrap="true">
                <% if(!readOnly){ %> <input type="submit" value="Save"
                        onclick="javascript:return onSave();" /> <input type="submit"
                        value="Save and Exit" onclick="javascript:return onSaveExit();" /> <% } %>
                <input type="submit" value="Exit"
                        onclick="javascript:return onExit();" /> <input type="submit"
                        value="Print Pdf" onclick="javascript:return onPrintPDF();" /></td>
        </tr>
    </table>
    
    <table style="font-size:12px; width:700px">
        <tr>
            <td class="title" colspan="3" nowrap="true" style="text-align:center">POSITION HAZARD COMMUNICATION FORM - Academic and Administrative Staff<br/><hr/>
                <div style="font-family: sans-serif; text-align: center;  font-weight: bold; font-size: 8pt;width:680px">Workplace Health Services - Room 25, UCC Lower Level - London, ON, N6A 3K7 - Tel: 519-661-2047 - Fax: 519-661-2016</div><br/>
            </td>
        </tr>
        <tr>
            <td class="outerTable" colspan="3"><b><i>TO BE COMPLETED BY THE CHAIR/IMMEDIATE SUPERVISOR</i></b><br/><br/></td>
        </tr>
</table>
<table  style="font-size:12px; width:700px;">
        <tr>
            <td style="width:350px;padding:10px">
                <label for="staffName">Name:</label>
                <input type="text" style="width:300px; font-family:courier;" name="staffName" value="<%=props.getProperty("staffName", "")%>"/>
            </td>
        </tr>
        <tr>
            <td style="width:350px;padding:10px">
                <label for="staffPosition">Position:</label>
                <input type="text" style="width:300px; font-family:courier" name="staffPosition" value="<%=props.getProperty("staffPosition", "")%>"/>
            </td>
            <td  style="width:350px;padding:10px">
               <label for="staffDept">Department:</label>
               <input type="text" style="width:300px; font-family:courier" name="staffDept" value="<%=props.getProperty("staffDept", "")%>"/>
            </td>
        </tr>
        <tr>
            <td  style="width:350px;padding:10px">
               <label for="staffFaculty">Faculty/School:</label>
               <input type="text" style="width:300px;font-family:courier" name="staffFaculty" value="<%=props.getProperty("staffFaculty", "")%>"/>
            </td>
            <td  style="width:350px;padding:10px">
               <label for="staffJobSite">Job site/Location:</label>
               <input type="text" style="width:300px;font-family:courier" name="staffJobSite" value="<%=props.getProperty("staffJobSite", "")%>"/>
            </td>
        </tr>
        <tr>
            <td  style="width:350px;padding:10px">
                <label for="staffPhone">Phone:</label>
                <input type="text" style="width:300px; font-family:courier" name="staffPhone" value="<%=props.getProperty("staffPhone", "")%>"/>
            </td>
            <td  style="width:350px;padding:10px">
                <label for="staffEmail">Email:</label>
                <input type="text" style="width:300px; font-family:courier" name="staffEmail" value="<%=props.getProperty("staffEmail", "")%>"/>
            </td>
        </tr>
</table>
<br/>
<br/>
<table  style="font-size:12px; width:700px">
        <tr>
            <td colspan="3">
                <p><b><i>(Please update whenever hiring, reclassifying, or hazards change):</i></b><br/></p>
            </td>
        </tr>
        <tr>
            <td style="width:233px;padding:10px">
                <label for="NewHire">New Hire:</label>
                <input type="checkbox" name="NewHire" <%=props.getProperty("NewHire", "")%>/>
            </td>
            <td  style="width:233px;padding:10px">
                <label for="JobReclassify">Job Reclassification:</label>
                <input type="checkbox" name="JobReclassify" <%=props.getProperty("JobReclassify", "")%>/>
            </td>
            <td  style="width:233px;padding:10px">
                <label for="ProcedureChange">Change in procedures:</label>
                <input type="checkbox" name="ProcedureChange" <%=props.getProperty("ProcedureChange", "")%>/>
            </td>
        </tr>
</table>
<br/>
<br/>
<table style="font-size:12px; width:700px">
    
        <tr>
            <td  style="width:350px;padding:10px">
                <label for="supervisorName">Chair/Supervisor:</label><br />
                <input type="text" style="width:300px;font-family:courier" name="supervisorName" value="<%=props.getProperty("supervisorName", "")%>"/>
            </td>
 
            <td  style="width:350px;padding:10px">
                <label for="supervisorCampusAddress">Campus Address:</label><br />
                <input type="text" style="width:300px;font-family:courier" name="supervisorCampusAddress" value="<%=props.getProperty("supervisorCampusAddress", "")%>"/>
            </td>
        </tr>
        <tr>
            <td  style="width:350px;padding:10px">
                <label for="supervisorEmail">Email Address:</label><br />
                <input type="text" style="width:300px;font-family:courier" name="supervisorEmail" value="<%=props.getProperty("supervisorEmail", "")%>"/>
            </td>
           
            <td  style="width:350px;padding:10px">
                    <label for="supervisorPhone">Phone:</label><br />
                    <input type="text" style="width:300px;font-family:courier" name="supervisorPhone" value="<%=props.getProperty("supervisorPhone", "")%>"/>
            </td>
        </tr>
</table>

<br/>
<br/>

<table  style="border-style:solid;text-align:center; border-color:black; border-width:3px; font-size:12px; width:700px">
     
        <tr>
            <td colspan="3"><b>INDICATE FREQUENCY OF USE/EXPOSURE USING THE FOLLOWING CODES</b></td>
        </tr>
        <tr>
            <td>
                <br/>
            </td>
        </tr>

        <tr>
            <td colspan="3"><b>H</b>=Hourly  <b>D</b>=Daily  <b>W</b>=Weekly  <b>O</b>=Other(<input type="text" style="width: 200px; font-family:courier; font-size:12px" name="OtherCode" value="<%=props.getProperty("OtherCode", "")%>"/>)  <b>X</b>=Not Selected</td>
        </tr>
        <tr>
            <td>
                <br/>
            </td>
        </tr>
</table>
<br/><br/>

<table style="font-size:12px; width:700px">
        <tr>
            <td style="font-size:16px;padding:14px"><b><u>EXPOSURE:</u></b></td>
        </tr>

        <tr>
            <td style="font-size:14px;padding:14px"><b>Chemical</b></td>
        </tr>
        <tr>
            <table style="font-size:12px;text-align:right;width:700px" >
                    <tr>
                        <td style="width:140px;padding:5px">
                            <label for="Acrylonitrile">Acrylonitrile:</label>
                            <input type="text" maxlength="1"  name="Acrylonitrile" style="width:16px;font-family:courier;font-size:12px;" value="<%=props.getProperty("Acrylonitrile", "")%>"/>
                        </td>
                        <td style="width:140px;padding:5px">
                            <label for="Asbestos">Asbestos:</label>
                            <input type="text" maxlength="1" name="Asbestos" style="width:16px;font-family:courier;font-size:12px;" value="<%=props.getProperty("Asbestos", "")%>"/>
                        </td>
                        <td style="width:140px;padding:5px">
                            <label for="EthyleneOxide">Ethylene Oxide:</label>
                            <input type="text" maxlength="1" name="EthyleneOxide" style="width:16px;font-family:courier;font-size:12px;" value="<%=props.getProperty("EthyleneOxide", "")%>"/>
                        </td>
                        <td style="width:140px;padding:5px">
                            <label for="Lead">Lead:</label>
                            <input type="text" maxlength="1" name="Lead" style="width:16px;font-family:courier;font-size:12px;" value="<%=props.getProperty("Lead", "")%>"/>
                        </td>
                        <td style="width:140px;padding:5px">
                            <label for="Silica">Silica:</label>
                            <input type="text" maxlength="1" name="Silica" style="width:16px;font-family:courier;font-size:12px;" value="<%=props.getProperty("Silica", "")%>"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width:140px;padding:5px">
                            <label for="">Arsenic:</label>
                            <input type="text" maxlength="1" name="Arsenic" style="width:16px;font-family:courier;font-size:12px;" value="<%=props.getProperty("Arsenic", "")%>"/>
                        </td>
                        <td  style="width:140px;padding:5px">
                            <label for="Benzene">Benzene:</label>
                            <input type="text" maxlength="1" name="Benzene" style="width:16px;font-family:courier;font-size:12px;" value="<%=props.getProperty("Benzene", "")%>"/>
                        </td>
                        <td  style="width:140px;padding:5px">
                            <label for="Isocyanates">Isocyanates:</label>
                            <input type="text" maxlength="1" name="Isocyanates" style="width:16px;font-family:courier;font-size:12px;" value="<%=props.getProperty("Isocyanates", "")%>"/>
                        </td>
                        <td  style="width:140px;padding:5px">
                            <label for="Mercury">Mercury:</label>
                            <input type="text" maxlength="1" name="Mercury" style="width:16px;font-family:courier;font-size:12px;" value="<%=props.getProperty("Mercury", "")%>"/>
                        </td>
                        <td  style="width:140px;padding:5px">
                            <label for="VinylChloride">Vinyl Chloride:</label>
                            <input type="text" maxlength="1" name="VinylChloride" style="width:16px;font-family:courier;font-size:12px;" value="<%=props.getProperty("VinylChloride", "")%>"/>
                        </td>
                    </tr>
            </table>
        </tr>
      </table>
        <br/>
     <table style="font-size:12px; width:700px">
        <tr>
            <td colspan="3">
                <p style="font-weight:bold;font-size:12px">Specify the name of the procedure and the form the designated substance is in:</p>
	   </td>
        </tr>                
        <tr>
            <td  style="width:600px;padding:10px">
              <label for="ProcedureName">Procedure Name:</label>
              <input type="text" name="ProcedureName" style="font-family:courier;font-size:12px;width:600px" value="<%=props.getProperty("ProcedureName", "")%>"/>
            </td>
        </tr>
        <tr>
             <td  style="width:600px;padding:10px">
                <label for="SubstanceForm">Designated Form of Substance:</label>
                <input type="text" name="SubstanceForm" style="font-family:courier;font-size:12px;width:600px" value="<%=props.getProperty("SubstanceForm","")%>"/>
            </td>
        </tr>
     </table>
<br/>
<br/>

    <table style="font-size:12px; width:700px; text-align:right" >
                <tr>
                    <td style="width:200px;padding:5px">
                        <label for="CompressedGases">Compressed Gases:</label>
                        <input type="text" maxlength="1"  name="CompressedGases" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("CompressedGases", "")%>"/>
                    </td>
                    <td style="width:400px;padding:5px">
                        <label for="MaterialsSeriousEffects">Materials Causing Immediate and Serious Toxic Effects:</label>
                        <input type="text" maxlength="1" name="MaterialsSeriousEffects" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("MaterialsSeriousEffects", "")%>"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:200px;padding:5px">
                        <label for="ReactiveMaterials">Reactive Materials:</label>
                        <input type="text" maxlength="1" name="ReactiveMaterials" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("ReactiveMaterials", "")%>"/>
                    </td>
                    <td style="width:400px;padding:5px">
                        <label for="FlammableCombustible">Flammable and Combustible Materials:</label>
                        <input type="text" maxlength="1" name="FlammableCombustible" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("FlammableCombustible", "")%>"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:200px;padding:5px">
                        <label for="CorrosiveMaterials">Corrosive Material:</label>
                        <input type="text" maxlength="1" name="CorrosiveMaterials" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("CorrosiveMaterials", "")%>"/>
                    </td>
                    <td style="width:400px;padding:5px">
                        <label for="MaterialsOtherToxicEffects">Other Toxic Effects:</label>
                        <input type="text" maxlength="1" name="MaterialsOtherToxicEffects" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("MaterialsOtherToxicEffects", "")%>"/>
                    </td>
                </tr>
                <tr>
                    <td style="width:200px;padding:5px">
                        <label for="OxidizingMaterials">Oxidizing Material:</label>
                        <input type="text" maxlength="1" name="OxidizingMaterials" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("OxidizingMaterials", "")%>"/>
                    </td>
                    <td style="width:400px;padding:5px">
                        <label for="AgricultureChemicals">Agricultural Chemicals:</label>
                        <input type="text" maxlength="1" name="AgricultureChemicals" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("AgricultureChemicals", "")%>"/>
                    </td>
                </tr>
            </table>
        
        <br/>
        <table style="font-size:12px; width:700px;">
           <tr>
              <td style="width:600px;padding:10px">
                <label for="ChemicalNames">Name of chemicals:</label>
                <input type="text" name="ChemicalNames" style="font-family:courier;font-size:12px;width:600px"  value="<%=props.getProperty("ChemicalNames", "")%>"/>
              </td>
           </tr>
          <tr>
            <td  style="width:600px;padding:10px">
                <label for="ChemicalHowItsUsed">How chemical is used:</label>
                <input type="text" name="ChemicalHowItsUsed" style="font-family:courier;font-size:12px;width:600px"  value="<%=props.getProperty("ChemicalHowItsUsed", "")%>"/>
            </td>
          </tr>
      </table>

        <br/>
        <br/>
     <table  style="font-size:12px; width:700px;">
        <tr>
            <td style="font-size:14px;padding:14px"><b>Biological</b></td>
        </tr>
        
        <tr>
            <td style="width:650px"><b>Agents at Containment Level (check entries &amp; specify where appropriate)</b></td>
        </tr>
    </table>

    <table style="font-size:12px; width:700px;text-align:right">
        <tr>
            <td style="padding:10px;width:225px">
                <label for="ContainmentLevel1">Containment Level 1:</label>
                <input type="text" maxlength="1"  name="ContainmentLevel1" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("ContainmentLevel1", "")%>"/>
            </td>
            <td style="padding:10px;width:225px">
                <label for="ContainmentLevel2">Containment Level 2:</label>
                <input type="text" maxlength="1"  name="ContainmentLevel2" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("ContainmentLevel2", "")%>"/>
            </td>
            <td style="padding:10px;width:225px">
                <label for="ContainmentLevel3">Containment Level 3:</label>
                <input type="text" maxlength="1"  name="ContainmentLevel3" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("ContainmentLevel3", "")%>"/>
            </td>
        </tr>
    </table>
<br/>
<table  style="font-size:12px; width:700px;text-align:right">
        <tr>
            <td style="width:700px;text-align:left;font-weight:bold"><p>May be required to enter:</p></td>
        </tr>
</table>
<br/>
<table  style="font-size:12px; width:700px;text-align:right">
        <tr>
            <td style="padding:0px;width:200px">
            	<label for="AnimalCareFacility">Animal Care Facility:</label>
                <input type="text" maxlength="1"  name="AnimalCareFacility" style="width:16px;font-family:courier;font-size:14px; " value="<%=props.getProperty("AnimalCareFacility", "")%>"/>
            </td>
            <td style="width:220px">
            	<label for="SheepContainmentUnit">Sheep Containment Unit:</label>
                <input type="text" maxlength="1"  name="SheepContainmentUnit" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("SheepContainmentUnit", "")%>"/>
            </td>
    
            <td style="width:230px">
                   <label for="NonHumanPrimate">Non-human Primate Unit:</label>
                   <input type="text" maxlength="1"  name="NonHumanPrimate" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("NonHumanPrimate", "")%>"/>
            </td>
            <td style="width:190px">
                   <label for="ContainmentLevel3Area">Containment Area 3:</label>
                   <input type="text" maxlength="1"  name="ContainmentLevel3Area" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("ContainmentLevel3Area", "")%>"/>
            </td>
         </tr>
  </table>
<br/>
<br/>
<table style="font-size:12px; width:700px;text-align:right">
	<tr>
        	<td style="padding:10px;width:300px"><b>Human</b><br/></td>
                <td style="padding:10px;width:300px"><b>Animal</b><br/>(live/unpreserved carcass)</td>
        </tr>
        <tr>
                    <td style="padding:10px;width:300px">
                        <label for="PrimaryCulture">Primary Culture:</label>
                        <input type="text" maxlength="1"  name="PrimaryCulture" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("PrimaryCulture", "")%>"/>
                    </td>
                    <td style="padding:10px;width:300px">
                        <label for="Primates">Primates:</label>
                        <input type="text" maxlength="1"  name="Primates" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("Primates", "")%>"/>
                    </td>
                </tr>
                <tr>
                    <td style="padding:10px;width:300px">
                        <label for="Cadavers">Cadavers (unfixed):</label>
                        <input type="text" maxlength="1"  name="Cadavers" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("Cadavers", "")%>"/>
                    </td>
                    <td style="padding:10px;width:300px">
                        <label for="DogsCats">Dogs or Cats:</label>
                        <input type="text" maxlength="1"  name="DogsCats" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("DogsCats", "")%>"/>
                    </td>
                </tr>
                <tr>
                    <td  style="padding:10px;width:300px">
                        <label for="BloodProducts">Blood or Blood Products:</label>
                        <input type="text" maxlength="1"  name="BloodProducts" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("BloodProducts", "")%>"/>
                    </td>
                    <td  style="padding:10px;width:300px">
                        <label for="Sheep">Sheep:</label>
                        <input type="text" maxlength="1"  name="Sheep" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("Sheep", "")%>"/>
                    </td>
                </tr>
                <tr>
                    <td style="padding:10px;width:300px">
                        <label for="PrimaryPatientCare">Primary Patient Care:</label>
                        <input type="text" maxlength="1"  name="PrimaryPatientCare" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("PrimaryPatientCare", "")%>"/>
                    </td>
                    <td style="padding:10px;width:300px">
                        <label for="WildMammals">Wild Mammals:</label>
                        <input type="text" maxlength="1"  name="WildMammals" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("WildMammals", "")%>"/>
                    </td>
                </tr>
                <tr>
                    <td  style="padding:10px;width:300px">
                        <label for="OtherHumanBiohazard">Other Human Biohazard:</label><br/>
                        <input type="text"  name="OtherHumanBiohazard" style="width:300px;font-family:courier;font-size:12px;" value="<%=props.getProperty("OtherHumanBiohazard", "")%>"/>
                    </td>
                    <td style="padding:10px;width:300px">
                        <label for="Rodents">Rodents:</label>
                        <input type="text" maxlength="1"  name="Rodents" style="width:16px;font-family:courier;font-size:14px;" value="<%=props.getProperty("Rodents", "")%>"/>
                    </td>
                </tr>
               
                <tr>
                    <td style="padding:10px;width:300px" ></td>
                    <td style="padding:10px;width:300px">
                        <label for="OtherAnimalBiohazard">Other Animal Biohazard:</label><br/>
                        <input type="text" name="OtherAnimalBiohazard" style="width:300px;font-family:courier;font-size:12px;" value="<%=props.getProperty("OtherAnimalBiohazard", "")%>"/>
                    </td>
                </tr>

            </table>
        
 <table  style="font-size:12px; width:700px;">
	<tr>
           <td  style="width:600px;padding:10px">
                <label for="OtherBiohazard">Other Biohazard:</label>
                <input type="text" name="OtherBiohazard" style="width:600px;font-family:courier;font-size:12px" value="<%=props.getProperty("OtherBiohazard", "")%>"/>
            </td>
        </tr>
        <tr>
             <td  style="width:600px;padding:10px">
                <label for="PathogenicParisites">Name of pathogenic parasites, bacteria or viruses:</label>
                <input type="text" name="PathogenicParasites" style="width:600px;font-family:courier;font-size:12px" value="<%=props.getProperty("PathogenicParasites", "")%>"/>
            </td>
        </tr>
</table>
  <table  style="font-size:12px; width:700px;">
        <tr>
            <td style="font-size:14px;padding:14px"><b>Physical Agents</b></td>
        </tr>
 </table>
<table  style="font-size:12px; width:700px;text-align:right">
        <tr>
        	<td  style="width:225px;padding:5px">
                        <label for="UltravioletEmitter">Ultraviolet Emitter:</label>
                        <input type="text" maxlength="1"  name="UltravioletEmitter" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("UltravioletEmitter", "")%>"/>
                </td>
                <td  style="width:225px;padding:5px"> 
                        <label for="RadioactiveSubstance">Radioactive Substance:</label>
                        <input type="text" maxlength="1"  name="RadioactiveSubstance" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("RadioactiveSubstance", "")%>"/>
                </td>
                <td  style="width:225px;padding:5px">
                        <label for="Ultrasound">Ultrasound:</label>
                        <input type="text" maxlength="1"  name="Ultrasound" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("Ultrasound", "")%>"/>
                </td>
         </tr>
         <tr>
                 <td style="width:225px;padding:5px">
                        <label for="InfraredEmitter">Infrared Emitter:</label>
                        <input type="text" maxlength="1"  name="InfraredEmitter" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("InfraredEmitter", "")%>"/>
                 </td>
                 <td style="width:225px;padding:5px">
                        <label for="MicrowaveEmittingDevice">Microwave Emitting Devices:</label>
                        <input type="text" maxlength="1"  name="MicrowaveEmittingDevice" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("MicrowaveEmittingDevice", "")%>"/>
                 </td>
                 <td style="width:225px;padding:5px">
                        <label for="Infrasound">Infrasound:</label>
                        <input type="text" maxlength="1"  name="Infrasound" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("Infrasound", "")%>"/>
                    </td>
         </tr>
         <tr>
                 <td style="width:225px;padding:5px">
                        <label for="Irradiators">Irradiators:</label>
                        <input type="text" maxlength="1"  name="Irradiators" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("Irradiators", "")%>"/>
                 </td>
                 <td style="width:225px;padding:5px">
                        <label for="XrayEmittingDevice">X-ray Emitting Devices:</label>
                        <input type="text" maxlength="1"  name="XrayEmittingDevice" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("XrayEmittingDevice", "")%>"/>
                 </td>
                 <td style="width:225px;padding:5px">
                        <label for="Gamma">Gamma:</label>
                        <input type="text" maxlength="1"  name="Gamma" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("Gamma", "")%>"/>
                 </td>
          </tr>
          <tr>
                 <td style="width:225px;padding:5px">
                        <label for="Laser3B">Laser 3B:</label>
                        <input type="text" maxlength="1"  name="Laser3B" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("Laser3B", "")%>"/>
                 </td>
                 <td style="width:225px;padding:5px">
                        <label for="Laser4">Laser 4:</label>
                        <input type="text" maxlength="1"  name="Laser4" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("Laser4", "")%>"/>
                 </td>
                 <td  style="width:225px;padding:5px">
                        <label for="MagneticField">Magnetic Field:</label>
                        <input type="text" maxlength="1"  name="MagneticField" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("MagneticField", "")%>"/>
                 </td>
          </tr>
          <tr>
                 <td style="width:225px;padding:5px">
                        <label for="Vibration">Vibration:</label>
                        <input type="text" maxlength="1"  name="Vibration" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("Vibration", "")%>"/>
                 </td>
                 <td style="width:225px;padding:5px">
                        <label for="RadioFrequency">Radiofrequency:</label>
                        <input type="text" maxlength="1"  name="RadioFrequency" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("RadioFrequency", "")%>"/>
                 </td>
                 <td style="width:225px;padding:5px">
                        <label for="HighNoiseLevels">High Noise Levels (>85dB):</label>
                        <input type="text" maxlength="1"  name="HighNoiseLevels" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("HighNoiseLevels", "")%>"/>
                 </td>
          </tr>
          <tr>
                 <td style="width:225px;padding:5px">
                        <label for="ExtremeHeat">Extreme Heat:</label>
                        <input type="text" maxlength="1"  name="ExtremeHeat" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("ExtremeHeat", "")%>"/>
                 </td>
                 <td style="width:225px;padding:5px">
                        <label for="ExtremeCold">Extreme Cold:</label>
                        <input type="text" maxlength="1"  name="ExtremeCold" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("ExtremeCold", "")%>"/>
                 </td>
          </tr>

    </table>

    <table style="font-size:14px; width:700px;">
        <tr>
           <td style="padding:10px"><b>Nanotechnology:</b></td>
        </tr>
    </table>
    <table style="font-size:12px; width:700px;" > 
        <tr>
            <td style="width:600px; padding:10px">
                <label for="Nanotechnology">Type/materials used:</label><br/>
                <textarea style="width:600px;font-size:12px" name="Nanotechnology" cols="85" rows="3"><%=props.getProperty("Nanotechnology", "")%></textarea>
            </td>
        </tr>
    </table>
   <table style="font-size:16px; width:700px;">
        <tr>
            <td style="padding:10px"><b><u>SAFETY:</u></b></td>
        </tr>
   </table>
   <table  style="font-size:12px; width:700px;"> 
        <tr>
            <td style="padding:15px; width:100px">
            	<label for="Driving">Driving:</label>
                <input type="text" maxlength="1"  name="Driving" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("Driving", "")%>"/>
             </td>
                    <td style="padding:5px; width:210px">
                        <label for="HighVoltage">High Voltage Equipment:</label>
                        <input type="text" maxlength="1"  name="HighVoltage" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("HighVoltage", "")%>"/>
                    </td>
                    <td style="padding:5px; width:210px">
                        <label for="ConfinedSpaceEntry">Confined Space Entry:</label>
                        <input type="text" maxlength="1"  name="ConfinedSpaceEntry" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("ConfinedSpaceEntry", "")%>"/>
                    </td>
                    <td style="padding:5px; width:190px">
                        <label for="Heights">Heights:</label>
                        <input type="text" maxlength="1"  name="Heights" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("Heights", "")%>"/>
                    </td>
                </tr>
      </table>
     <table style="font-size:16px; width:700px;">
        <tr>
            <td style="padding:10px"><b><u>MUSCULOSKELETAL:</u></b></td>
        </tr>
     </table>
    <table style="font-size:12px; width:700px;text-align:right">
        <tr>
            <td style="width:100px; padding:10px">
            	<label for="ComputerWork">Computer Work:</label>
                <input type="text" maxlength="1"  name="ComputerWork" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("ComputerWork", "")%>"/>
            </td>
            <td style="width:100px; padding:10px">
            	<label for="RepetitiveWork">Repetitive Work:</label>
                <input type="text" maxlength="1"  name="RepetitiveWork" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("RepetitiveWork", "")%>"/>
            </td>
            <td style="width:100px; padding:10px">
            	<label for="AwkwardPositions">Awkward Positions:</label>
                <input type="text" maxlength="1"  name="AwkwardPositions" style="width:16px;font-family:courier;font-size:14px" value="<%=props.getProperty("AwkwardPositions", "")%>"/>
            </td>
        </tr>
    </table>
<br/>
<br/>
    <table style="font-size:12px; width:700px">
        <tr>
            <td style="width:600px;padding:10px">
                <label style="font-weight:bold" for="AdditionalNotes">Additional Notes or other hazards not included:</label><br/>
                <textarea name="AdditionalNotes" style="font-family:courier; font-size:12px;width:600px" cols="85" rows="3"><%=props.getProperty("AdditionalNotes", "")%></textarea>
            </td>
        </tr>
   </table>
  <table style="font-size:12px; width:700px">
        <tr>
            <td style="font-weight:bold;width:600px;padding:15px">Person requires:<br/></td>
        </tr>

</table>
<table style="text-align:right;font-size:12px; width:700px">
        <tr>
        	<td style="width:250px;padding:5px">
                	<label for="WHIMS">W.H.I.M.S:</label>
                        <input type="checkbox" name="WHIMS" <%=props.getProperty("WHIMS", "")%>/>
                </td>
                <td style="width:250px;padding:5px">
                        <label for="RadiationSafety">Radiation Safety Training:</label>
                         <input type="checkbox" name="RadiationSafety" <%=props.getProperty("RadiationSafety", "")%>/>
                </td>
                <td style="width:250px;padding:5px">
                        <label for="XrayTraining">X-ray Training:</label>
                         <input type="checkbox" name="XrayTraining" <%=props.getProperty("XrayTraining", "")%>/>
                </td>
        </tr>
        <tr>
                <td style="width:250px;padding:5px">
                        <label for="BiosafetyTraining">Biosafety Training:</label>
                        <input type="checkbox" name="BiosafetyTraining" <%=props.getProperty("BiosafetyTraining", "")%>/>
                </td>
                <td style="width:250px;padding:5px">
                        <label for="EmployeeSafety">Employee Safety Orientation:</label>
                         <input type="checkbox" name="EmployeeSafety" <%=props.getProperty("EmployeeSafety", "")%>/>
                </td>
                <td style="width:250px;padding:5px">
                        <label for="AnimalHandling">Animal Handling Workshop:</label>
                         <input type="checkbox" name="AnimalHandling" <%=props.getProperty("AnimalHandling", "")%>/>
                </td>
        </tr>
        <tr>
                <td colspan="3" style="padding:5px">
                      <label for="WasteManagement">Laboratory & Environment/Waste Management Safety Workshop:</label>
                      <input type="checkbox" name="WasteManagement" <%=props.getProperty("WasteManagement", "")%>/>
                </td>
        </tr>
      </table>
        
       
            <br/><br/>
<table  style="font-size:12px; width:700px">       
     <tr>
	<td style="width:600px;padding:10px">
     		<label style="font-weight:bold" for="FormCompleted">Form Completed By:</label>
            	<input type="text" name="FormCompletedBy" style="width:600px;font-family:courier;font-size:12px" value="<%=props.getProperty("FormCompletedBy", "")%>"/>
     	</td>      
     </tr>
</table>

    <table class="Head" class="hidePrint">
        <tr>
            <td nowrap="true">
                <% if(!readOnly){ %>
                      <input type="submit" value="Save" onclick="javascript:return onSave();" />
                      <input type="submit" value="Save and Exit" onclick="javascript:return onSaveExit();" />
                <% } %>
                      <input type="submit" value="Exit" onclick="javascript:return onExit();" />
                      <input type="submit" value="Print Pdf" onclick="javascript:return onPrintPDF();" />
            </td>
        </tr>
    </table>
</html:form>
</body>
</html:html>
