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
    	INTAKE C. Mental Health Team Data Collection Form -- Step2: Presenting and Referral Information
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



<table width="95%" align="center"  border="1" cellpadding="1" cellspacing="1">
<tr>
    <td colspan="4" class="style51"> 
    14. <a name="Num14">Presenting Issues (Check ALL that apply)  </a>
	<div align="right">
		<a href="#Top"><span  class="style126">Top</span></a><br>
		<!-- case management -->
	 </div>
    </td>
</tr>

<tr>
<td width="5%"><html:checkbox  property="intake.cboxThreatIssue" /></td>
<td colspan="3"   class="style76">
	 Threat To Others/Attempted Suicide 
</td>
</tr>

<tr>
<td width="5%"><html:checkbox  property="intake.cboxMentalIssue" /></td>
<td colspan="3"   class="style76">
	Specific Symptom of Serious Mental Illness
</td>
</tr>

<tr>
<td width="5%"><html:checkbox  property="intake.cboxSexualAbuseIssue" /></td>
<td colspan="3"   class="style76">
	Physical/Sexual Abuse 
</td>
</tr>

<tr>
<td width="5%"><html:checkbox  property="intake.cboxEducationalIssue" /></td>
<td colspan="3"   class="style76">
	Educational
</td>
</tr>

<tr>
<td width="5%"><html:checkbox  property="intake.cboxEmploymentIssue" /></td>
<td colspan="3"   class="style76">
	Occupational/Employment/Vocational
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxHousingIssue" />  
</td>
<td colspan="3"   class="style76">
	 	Housing
</td>
</tr>

<tr>
<td width="5%"><html:checkbox  property="intake.cboxFinancialIssue" /></td>
<td colspan="3"   class="style76">
	 	Financial
</td>
</tr>

<tr>
<td width="5%"><html:checkbox  property="intake.cboxLegalIssue" /></td>
<td colspan="3"   class="style76">
	 	Legal 
</td>
</tr>

<tr>
<td width="5%"><html:checkbox  property="intake.cboxRelationalIssue" /></td>
<td colspan="3"   class="style76">
	 	Problems with Relationships
</td>
</tr>


<tr>
<td width="5%"><html:checkbox  property="intake.cboxAddictionIssue" /></td>
<td colspan="3"   class="style76">
	 	 	Problems with Substance Abuse/Addictions 
</td>
</tr>


<tr>
<td width="5%"><html:checkbox  property="intake.cboxDailyActivityIssue" /></td>
<td colspan="3"   class="style76">
	 	 	Activities of Daily Living
</td>
</tr>


<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxOtherIssue"  styleId="14_PARENT"  
		onclick="unCheckOtherIssues(document.intakeCForm);"  />
</td>
<td colspan="3"   class="style76">
	 Other 
</td>
</tr>

<tr>
<td>&nbsp;
	 
</td>
<td width="5%">
	<html:checkbox  property="intake.cboxHealthCareIssueOther"    styleId="14_0_CHILD"  
	                onclick="document.intakeCForm.elements['intake.cboxOtherIssue'].checked=true" />  
</td>
<td width="90%" colspan="2" class="style76">
	  	Access to Health Care
</td>
</tr>

<tr>
<td>&nbsp;
	 
</td>
<td width="5%">
	<html:checkbox  property="intake.cboxSocialServiceIssueOther"   styleId="14_1_CHILD"  
	                onclick="document.intakeCForm.elements['intake.cboxOtherIssue'].checked=true" />   
</td>
<td colspan="2" class="style76">
	   	Access to Social Services
</td>
</tr>

<tr>
<td>&nbsp;
	 
</td>
<td width="5%">
	<html:checkbox  property="intake.cboxBankingIssueOther"    styleId="14_2_CHILD"   
					onclick="document.intakeCForm.elements['intake.cboxOtherIssue'].checked=true"/>   
</td>
<td colspan="2" class="style76">
	   	Banking
</td>
</tr>

<tr>
<td>&nbsp;
	 
</td>
<td width="5%">
	<html:checkbox  property="intake.cboxIdentificationIssueOther"   styleId="14_3_CHILD" 
					onclick="document.intakeCForm.elements['intake.cboxOtherIssue'].checked=true" />  
</td>
<td colspan="2" class="style76">
	  	 	Identification
</td>
</tr>

<tr>
<td>&nbsp;
	 
</td>
<td width="5%">
	<html:checkbox  property="intake.cboxImmigrationIssueOther"   styleId="14_4_CHILD"  
					onclick="document.intakeCForm.elements['intake.cboxOtherIssue'].checked=true" />   
</td>
<td colspan="2" class="style76">
	  	  	Immigration
</td>
</tr>

<tr>
<td>&nbsp;
	 
</td>
<td width="5%">
	<html:checkbox  property="intake.cboxPhysicalIssueOther"   styleId="14_5_CHILD" 
					onclick="document.intakeCForm.elements['intake.cboxOtherIssue'].checked=true" />   
</td>
<td colspan="2" class="style76">
	  	  	Physical Health Issues
</td>
</tr>

<tr>
<td>&nbsp;
	 
	 
</td>
<td width="5%">
	<html:checkbox  property="intake.cboxIsolationIssueOther"   styleId="14_6_CHILD" 
					onclick="document.intakeCForm.elements['intake.cboxOtherIssue'].checked=true" />   
</td>
<td colspan="2" class="style76">
	  	 	 	Social Isolation
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxNoneListedIssue" />  
</td>
<td colspan="3" class="style76">
	 None of the above
</td>
</tr>

</table>



<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->

<table width="95%" align="center"  border="1" cellpadding="1" cellspacing="1">
<tr>
    <td colspan="4" class="style51"> 
    19. <a name="Num19">Baseline Social Services Access </a>
	<div align="right">
		<a href="#Top"><span  class="style126">Top</span></a><br>
		<!-- case management -->
	 </div>
    </td>
</tr>

<tr>
<td   colspan="4" class="style76">
	Accesses Social Services? (e.g. drop-in centres, recreational programs, meal programs, food banks, 
	counseling/support groups, mental health organizations, ethno-cultural centres, 
	employment programs, harm reduction programs (e.g. needle exchange), 
	drug treatment programs, other social services)
</td>

</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioBaseSocialServiceAccess" value="1"/> 
</td>
<td width="95%" colspan="3"   class="style76">
	   	 	Yes
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioBaseSocialServiceAccess" value="2" /> 
</td>
<td colspan="3"   class="style76">
	   	 No
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioBaseSocialServiceAccess" value="3" /> 
</td>
<td colspan="3"   class="style76">
	   	Unknown or Service Recipient Declined
</td>
</tr>


<tr>
<td   colspan="4" class="style76">
	If yes, what social services does client access?
</td>

</tr>

<tr>
<td colspan="4"  class="style76">
	<html:textarea property="intake.baseSocialServiceClientAccesses" cols="70"></html:textarea>
</td>
</tr>

<tr>
<td colspan="4"  class="style76">
	Needs Social Services?
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioBaseNeedSocialServices" value="1" /> 
</td>
<td width="95%" colspan="3"   class="style76">
	   	  Yes
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioBaseNeedSocialServices" value="2"/> 
</td>
<td colspan="3"   class="style76">
	   No
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioBaseNeedSocialServices" value="3"/> 
</td>
<td colspan="3"   class="style76">
	   	  	Unknown or Service Recipient Declined
</td>
</tr>

<tr>
<td   colspan="4"  class="style76">
	If yes, what are client needs and why is client not accessing these services?
</td>

</tr>

<tr>
<td colspan="4"  class="style76">
	<html:textarea property="intake.baseWhyClientDoNotAccessSocialServices" cols="70"></html:textarea>
</td>
</tr>
</table>


<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->

<table width="95%" align="center"  border="1" cellpadding="1" cellspacing="1">
<tr>
    <td colspan="4" class="style51"> 
    20. <a name="Num20">Current Social Services Access </a>
	<div align="right">
		<a href="#Top"><span  class="style126">Top</span></a><br>
		<!-- case management -->
	 </div>
      </td>
</tr>

<tr>
<td   colspan="4"  class="style76">
	Accesses Social Services? (e.g. drop-in centres, recreational programs, meal programs, food banks, 
	counseling/support groups, mental health organizations, ethno-cultural centres, 
	employment programs, harm reduction programs (e.g. needle exchange), drug treatment programs, other social services)
</td>

</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioCurrSocialServiceAccess" value="1" /> 
</td>
<td width="95%" colspan="3"   class="style76">
	   	 	Yes
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioCurrSocialServiceAccess" value="2" /> 
</td>
<td colspan="3"   class="style76">
	   	 No
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioCurrSocialServiceAccess" value="3" /> 
</td>
<td colspan="3"   class="style76">
	   	Unknown or Service Recipient Declined
</td>
</tr>


<tr>
<td   colspan="4"  class="style76">
	If yes, what social services does client access?
</td>

</tr>

<tr>
<td colspan="4"  class="style76">
	<html:textarea property="intake.currSocialServiceClientAccesses" cols="70"></html:textarea>
</td>
</tr>

<tr>
<td colspan="4"  class="style76">
	Needs Social Services?
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioCurrNeedSocialServices" value="1"/> 
</td>
<td width="95%" colspan="3"   class="style76">
	   	  Yes
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioCurrNeedSocialServices" value="2"/> 
</td>
<td colspan="3"   class="style76">
	   No
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioCurrNeedSocialServices" value="3"/> 
</td>
<td colspan="3"   class="style76">
	   	  	Unknown or Service Recipient Declined
</td>
</tr>

<tr>
<td   colspan="4"  class="style76">
	If yes, what are client needs and why is client not accessing these services?
</td>

</tr>

<tr>
<td colspan="4"  class="style76">
	<html:textarea property="intake.currWhyClientDoNotAccessSocialServices" cols="70"></html:textarea>
</td>
</tr>

</table>



<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->

<table width="95%" align="center"  border="1" cellpadding="1" cellspacing="1">
<tr>
    <td colspan="4" class="style51"> 
	24. <a name="Num24">Source of Referral </a>
	<div align="right">
		<a href="#Top"><span  class="style126">Top</span></a><br>
		<!-- case management -->
	 </div>
	</td>
</tr>


<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxReferralByHospital" />  
</td>
<td colspan="3"   class="style76">
	  	   	General Hospital
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxReferralByPsychiatricHospital" />  
</td>
<td colspan="3"   class="style76">
	  	  	Psychiatric Hospital
</td>
</tr>


<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxReferralByOtherInstitution" />  
</td>
<td colspan="3"   class="style76">
	  	   	Other Institution
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxReferralByMentalOrg" />  
</td>
<td colspan="3"   class="style76">
	     	Community Mental Health Organization
</td>
</tr>


<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxReferralByOtherAgency" 
	  styleId="24a_PARENT"   
	   onclick="unCheckReferralAgency(document.intakeCForm)"  />  
</td>
<td colspan="3"   class="style76">
	  	Other Community Agencies 
</td>
</tr>

<tr>
<td>&nbsp;
	 
</td>
<td width="4%">
	<html:checkbox  property="intake.cboxReferralByStreetNurseOther"   styleId="24a_0_CHILD"  
	                onclick="document.intakeCForm.elements['intake.cboxReferralByOtherAgency'].checked=true" />    
</td>
<td width="91%" colspan="2" class="style76">
	  	 	Street Health Nurse
</td>
</tr>


<tr>
<td>&nbsp;
</td>
<td width="4%">
	<html:checkbox  property="intake.cboxReferralByStreetIDWorkerOther"   styleId="24a_1_CHILD"   
	                onclick="document.intakeCForm.elements['intake.cboxReferralByOtherAgency'].checked=true" />    
</td>
<td colspan="2" class="style76">
	    	Street Health ID Worker
</td>
</tr>

<tr>
<td>&nbsp;
</td>
<td width="4%">
	<html:checkbox  property="intake.cboxReferralByStreetHealthReceptionOther"   styleId="24a_2_CHILD"  
	                onclick="document.intakeCForm.elements['intake.cboxReferralByOtherAgency'].checked=true" />    
</td>
<td colspan="2" class="style76">
	    	 	Street Health Reception
</td>
</tr>

<tr>
<td>&nbsp;
</td>
<td width="4%">
	<html:checkbox  property="intake.cboxReferralByFredVictorCentreOther"   styleId="24a_3_CHILD"   
	                onclick="document.intakeCForm.elements['intake.cboxReferralByOtherAgency'].checked=true" />    
</td>
<td colspan="2" class="style76">
	    	 	Fred Victor Centre
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxReferralByPhysician" />  
</td>
<td colspan="3"   class="style76">
	     	 	Family Physicians
</td>
</tr>


<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxReferralByPsychiatrists" />  
</td>
<td colspan="3"   class="style76">
	     	 	Psychiatrists
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxReferralByMentalHealthWorker" />  
</td>
<td colspan="3"   class="style76">
	      	Mental Health Worker
</td>
</tr>


<tr>
<td width="5%"> 
	<html:checkbox  property="intake.cboxReferralByCriminalJusticeSystem"
	   styleId="24b_PARENT"   
	     onclick="unCheckReferralCriminal(document.intakeCForm);"  />  
</td>
<td colspan="3"   class="style76">
	      	Criminal Justice System
</td>
</tr>

<tr>
<td>&nbsp;
</td>
<td width="4%"> 
	<html:checkbox  property="intake.cboxReferralByPolice"   styleId="24b_0_CHILD"    
	                onclick="document.intakeCForm.elements['intake.cboxReferralByCriminalJusticeSystem'].checked=true" />   
</td>
<td colspan="2" class="style76">
 	 	Police
</td>
</tr>


<tr>
<td>&nbsp;
</td>
<td width="4%"> 
	<html:checkbox  property="intake.cboxReferralByCourt"   styleId="24b_1_CHILD"    
	                onclick="document.intakeCForm.elements['intake.cboxReferralByCriminalJusticeSystem'].checked=true" />   
</td>

<td colspan="2" class="style76">
	Courts (includes Court Support & Diversion Programs)
</td>
</tr>

<tr>
<td>&nbsp;
</td>
<td width="4%"> 
	<html:checkbox  property="intake.cboxReferralByDetentionCenter"   styleId="24b_2_CHILD"     
	                onclick="document.intakeCForm.elements['intake.cboxReferralByCriminalJusticeSystem'].checked=true" />   
</td>
<td colspan="2" class="style76"> 
	Correctional Facilities (includes jails and detention centres)</td>
</tr>

<tr>
<td>&nbsp;
</td>
<td width="4%">
	<html:checkbox  property="intake.cboxReferralByProbation"    styleId="24b_3_CHILD"     
	                onclick="document.intakeCForm.elements['intake.cboxReferralByCriminalJusticeSystem'].checked=true" />   
</td>
<td colspan="2" class="style76">
	Probation/Parole Officers
</td>
</tr>


<tr>
<td>&nbsp;
</td>

<td width="4%"> 
	<html:checkbox  property="intake.cboxReferralBySafeBeds"   styleId="24b_4_CHILD"     
	                onclick="document.intakeCForm.elements['intake.cboxReferralByCriminalJusticeSystem'].checked=true" />   
</td>
<td colspan="2" class="style76">
	    	 	 	Safe Beds
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxReferralBySelf" />  
</td>
<td colspan="3" class="style76">
	      	 	Self, Family or Friend 
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxReferralByOtherPeople"   styleId="24c_PARENT"     onclick="checkChildCheckbox(this, '24c', 1)"  />  
</td>
<td colspan="3" class="style76">
	       	Other
</td>
</tr>

<tr>
<td>&nbsp;
</td>
<td width="4%">
	<html:checkbox  property="intake.cboxReferralByPublic"   styleId="24c_0_CHILD"     
	                onclick="document.forms[0].cboxReferralByOtherPeople.checked=true" />     
</td>
<td colspan="2" class="style76">
	    	  	General Public
</td>
</tr>


<tr>
<td   colspan="4" class="style76">
	Comments about referral:
</td>

</tr>

<tr>
<td colspan="4"  class="style76">
	<html:textarea property="intake.referralComment" cols="70"></html:textarea>
</td>
</tr>
</table>


<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->

<table width="95%" align="center"  border="1" cellpadding="1" cellspacing="1">
<tr>
    <td colspan="4" class="style51"> 
	25. <a name="Num25">Exit Disposition </a>
	<div align="right">
		<a href="#Top"><span  class="style126">Top</span></a><br>
		<!-- case management -->
	 </div>
	</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxCompleteWithoutReferral" />  
</td>
<td width="95%" colspan="3" class="style76">
	  	   	Completion without Referral
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxCompleteWithReferral" />  
</td>
<td colspan="3" class="style76">
	  	  	Completion with Referral
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxSuicideExit" />  
</td>
<td colspan="3" class="style76">
	   	 	Suicide
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxDeathExit" />  
</td>
<td colspan="3" class="style76">
	  	  	Death
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxRelocationExit" />  
</td>
<td colspan="3" class="style76">
	 	  	Relocation
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxWithdrawalExit" />  
</td>
<td colspan="3" class="style76">
	 	   	Withdrawal (No Contact and Involuntary Discharge included)
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxNAExit" />  
</td>
<td colspan="3" class="style76">
	 	  	 	Not Applicable
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