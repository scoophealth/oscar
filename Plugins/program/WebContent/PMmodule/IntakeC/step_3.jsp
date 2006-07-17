<%@ include file="/taglibs.jsp" %>
<%@ page import="org.caisi.PMmodule.web.formbean.*" %>
<%@ page import="org.apache.struts.validator.DynaValidatorForm" %>

<%
	IntakeCFormBean formBean;
	DynaValidatorForm form = (DynaValidatorForm)session.getAttribute("intakeCForm");
	formBean = (IntakeCFormBean)form.get("view");
	pageContext.setAttribute("formBean",formBean);

%>

<table width="95%" align="center" border="1" cellpadding="1" cellspacing="1">

  <tr>
    <td colspan="4"  class="style63">
    	INTAKE C. Mental Health Team Data Collection Form -- Step3: Legal and ID Status
    	&nbsp;&nbsp;&nbsp;
	</td>
  </tr>
<tr>
<td height="10" colspan="4">&nbsp;
	
</td>
</tr>
<tr>
<td width="13%" class="style76">
Client Number: 
</td>
<td width="21%" class="style76">
	&nbsp;<c:out value="${requestScope.demographicNo}"/>
</td>
<td colspan="2"   class="style76">&nbsp;

</td>

</tr>

<tr>
<td class="style76">
Client First Name:  
</td>
<td width="21%" class="style76">
<html:text styleClass="style71" property="intake.clientFirstName" readonly="true" />
</td>
<td width="14%" class="style76">
Client Last Name:  
</td>
<td width="52%">
<html:text styleClass="style71" property="intake.clientSurname"  readonly="true"/>
</td>
</tr>
</table>


<!-- ################################################################################################### -->

<table width="95%" align="center"  border="1" cellpadding="1" cellspacing="1">
<tr>
<td colspan="4" class="style51">
	8.	<a name="Num8">Baseline Legal Status   </a>
	<div align="right">
		<a href="#Top"><span  class="style126">Top</span></a><br>
		<!-- case management -->
	 </div>
</td>
</tr>

<tr>
<td colspan="4"  class="style76">
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioBaseLegalStatus" value="1" onclick="unCheckBaseLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	No Criminal Legal Problems
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioBaseLegalStatus" value="2" onclick="unCheckBaseLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	 	Pre-Charge Diversion
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioBaseLegalStatus" value="3" onclick="unCheckBaseLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	  	Court Diversion Program
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioBaseLegalStatus" value="4" onclick="unCheckBaseLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	   	Conditional Discharge
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioBaseLegalStatus" value="5" onclick="unCheckBaseLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	   	Fitness Assessment
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioBaseLegalStatus" value="6" onclick="unCheckBaseLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	   Criminal Responsibility Assessment
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioBaseLegalStatus" value="7" onclick="unCheckBaseLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	    Awaiting Trial/Bail
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioBaseLegalStatus" value="8" onclick="unCheckBaseLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	   	Awaiting Sentencing
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioBaseLegalStatus" value="9" onclick="unCheckBaseLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	   	On Probation
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioBaseLegalStatus" value="10" onclick="unCheckBaseLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	   On Parole
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioBaseLegalStatus" value="11" onclick="unCheckBaseLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	   Incarcerated
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioBaseLegalStatus" value="12"/> 
</td>
    <td colspan="3"   class="style76"> Other Criminal/Legal Problems: </td>
</tr>

<tr>
<td width="4%">&nbsp;
	
</td>
<td width="4%">
	<html:checkbox  property="intake.cboxFamilyLawIssues1"   styleId="8_0_CHILD"     
	                onclick="checkBaseLegalStatusToOther(this,document.intakeCForm)" />    
</td>
<td width="92%" colspan="2" class="style76">
	  Family Law Issues
</td>
</tr>

<tr>
<td width="4%">&nbsp;
	
</td>
<td width="4%">
	<html:checkbox  property="intake.cboxProblemsWithPolice1"  styleId="8_1_CHILD"     
	               onclick="checkBaseLegalStatusToOther(this,document.intakeCForm)" />     
</td>
<td width="92%" colspan="2" class="style76">
	   	Problems with Police
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioBaseLegalStatus" value="13" onclick="unCheckBaseLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	  Unknown or Service Recipient Declined
</td>
</tr>
</table>



<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->

<table width="95%" align="center"  border="1" cellpadding="1" cellspacing="1">
<tr>
<td colspan="4" class="style51">
	9.	<a name="Num9">Current Legal Status</a>
	<div align="right">
		<a href="#Top"><span  class="style126">Top</span></a><br>
		<!--  case management -->
	 </div>
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioCurrLegalStatus" value="1" onclick="unCheckCurrentLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	 No Criminal Legal Problems
</td>
</tr>

<tr>
    <td width="4%"> 
	<html:radio property="intake.radioCurrLegalStatus" value="2" onclick="unCheckCurrentLegalStatus(document.intakeCForm)"/> 
	</td>
<td colspan="3"   class="style76">
	  	Pre-Charge Diversion
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioCurrLegalStatus" value="3" onclick="unCheckCurrentLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	   	Court Diversion Program
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioCurrLegalStatus" value="4" onclick="unCheckCurrentLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	   	 	Conditional Discharge
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioCurrLegalStatus" value="5" onclick="unCheckCurrentLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	   	  	Fitness Assessment
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioCurrLegalStatus" value="6" onclick="unCheckCurrentLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	   	   	Criminal Responsibility Assessment
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioCurrLegalStatus" value="7" onclick="unCheckCurrentLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	   	    	Awaiting Trial/Bail
</td>
</tr>

<tr>
    <td width="4%">
	<html:radio property="intake.radioCurrLegalStatus" value="8" onclick="unCheckCurrentLegalStatus(document.intakeCForm)"/> 
	</td>
<td colspan="3"   class="style76">
	   	     	Awaiting Sentencing
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioCurrLegalStatus" value="9" onclick="unCheckCurrentLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	   	      	On Probation
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioCurrLegalStatus" value="10" onclick="unCheckCurrentLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	   	      On Parole
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioCurrLegalStatus" value="11" onclick="unCheckCurrentLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	   	     Incarcerated
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioCurrLegalStatus" value="12" /> 
</td>
<td colspan="3"   class="style76">
	   	     Other Criminal/Legal Problems: 
</td>
</tr>

<tr>
<td width="4%">&nbsp;
	
</td>
<td width="3%"> 
	<html:checkbox  property="intake.cboxFamilyLawIssues2"  styleId="9_0_CHILD"      
		onclick="checkCurrentLegalStatusToOther(this,document.intakeCForm)" />     
</td>
<td width="93%" colspan="2" class="style76">
	  Family Law Issues
</td>
</tr>

<tr>
<td width="4%">&nbsp;
	
</td>
<td width="4%">
	<html:checkbox  property="intake.cboxProblemsWithPolice2"  styleId="9_1_CHILD"      
		onclick="checkCurrentLegalStatusToOther(this,document.intakeCForm)" />     
</td>
<td width="92%" colspan="2" class="style76">
	   	Problems with Police
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioCurrLegalStatus" value="13" onclick="unCheckCurrentLegalStatus(document.intakeCForm)"/> 
</td>
<td colspan="3"   class="style76">
	  Unknown or Service Recipient Declined
</td>
</tr>

</table>


<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->

<table width="95%" align="center"  border="1" cellpadding="1" cellspacing="1">
<tr>
<td colspan="4" class="style51">
	10.	<a name="Num10">Community Treatment Orders</a>
	<div align="right">
		<a href="#Top"><span  class="style126">Top</span></a><br>
		<!-- case management -->
	 </div>
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioTreatmentOrders" value="1" /> 
</td>
<td width="96%" colspan="3"   class="style76">
	 Issued CTO
</td>
</tr>


<tr>
<td width="4%">
	<html:radio property="intake.radioTreatmentOrders" value="2" /> 
</td>
<td colspan="3"   class="style76">
	 No CTO
</td>
</tr>

<tr>
<td width="4%">
	<html:radio property="intake.radioTreatmentOrders" value="3" /> 
</td>
<td colspan="3"   class="style76">
	 Unknown or Service Recipient Declined
</td>
</tr>
</table>



<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->

<table width="95%" align="center"  border="1" cellpadding="1" cellspacing="1">
<tr>
    <td colspan="4" class="style51"> 
    15. <a name="Num15">Baseline Identification Status (Check ALL that apply) </a>
	<div align="right">
		<a href="#Top"><span  class="style126">Top</span></a><br>
		<!-- case management -->
	 </div>
	</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxBaseHasCertificate" />  
</td>
<td width="95%" colspan="3"   class="style76">
	  	Has Birth Certificate/Record of Landing/Citizenship Card
</td>
</tr>


<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxBaseHasSIn" />  
</td>
<td colspan="3"   class="style76">
	  	Has Social Insurance Number
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxBaseHasHealthCard" />  
</td>
<td colspan="3"   class="style76">
	  	Has Health Card
</td>
</tr>


<tr>
    <td width="5%"> 
	<html:checkbox  property="intake.cboxBaseHasNativeCard" />  
    </td>
<td colspan="3"   class="style76">
	  	Has Native Status Card
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxBaseHasNonStatus" />  
</td>
<td colspan="3"   class="style76">
	 	Has less than full immigration status (i.e. is non-status) 
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxBaseHasUnknown" />  
</td>
<td colspan="3"   class="style76">
	 	 	Unknown or Service Recipient Declined
</td>
</tr>
</table>


<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->


<table width="95%" align="center"  border="1" cellpadding="1" cellspacing="1">
<tr>
    <td colspan="4" class="style51"> 
    16. <a name="Num16">Current Identification Status (Check ALL that apply)  </a>
	<div align="right">
		<a href="#Top"><span  class="style126">Top</span></a><br>
		<!-- case management -->
	 </div>
     </td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxCurrHasCertificate" />  
</td>
<td width="95%" colspan="3"   class="style76">
	  	Has Birth Certificate/Record of Landing/Citizenship Card
</td>
</tr>


<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxCurrHasSIn" />  
</td>
<td colspan="3"   class="style76">
	  	Has Social Insurance Number
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxCurrHasHealthCard" />  
</td>
<td colspan="3"   class="style76">
	  	Has Health Card
</td>
</tr>

<tr>
    <td width="5%"> 
	<html:checkbox  property="intake.cboxCurrHasNativeCard" />  
   </td>
<td colspan="3"   class="style76">
	  	Has Native Status Card
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxCurrHasNonStatus" />  
</td>
<td colspan="3"   class="style76">
	 	Has less than full immigration status (i.e. is non-status) 
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxCurrHasUnknown" />  
</td>
<td colspan="3"   class="style76">
	 	 	Unknown or Service Recipient Declined
</td>
</tr>

</table>


<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->

<table width="95%" align="center"  border="1">
<tr>
<td align="center" class="style76">
<div align="center">
     <html:submit value="Save" onclick="this.form.method.value='save';" />
     <html:cancel onclick="this.form.method.value='cancel';"/>
     <input type="button" value="Print" onclick="javascript:return onPrint();"/>
</td>
</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->


<table>
<tr>
<td height="15">&nbsp;
	
</td>
</tr>
</table>