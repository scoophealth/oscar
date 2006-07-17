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
    	INTAKE C. Mental Health Team Data Collection Form -- Step4: Medical Status
    	&nbsp;&nbsp;&nbsp;
	  	<input type="button"  name="backToClientSearch"  value="   Back   " 
		     onclick="javascript:redirectToClientSearch(
		     '','','<html:rewrite page="/PMmodule/ClientSearchAction.admit"/>')">
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
	11. <a name="Num11">Primary Diagnosis (Check ONE only)</a>
	<div align="right">
		<a href="#Top"><span  class="style126">Top</span></a><br>
		<!-- case management -->
	 </div>
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioPrimaryDiagnosis" value="1" onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" />
</td>
<td colspan="3"   class="style76">
	 Adjustment Disorders
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioPrimaryDiagnosis" value="2" onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" />
</td>
<td colspan="3"   class="style76">
	 Anxiety Disorders 
</td>
</tr>

<tr>
<td>&nbsp;	 
</td>
<td width="5%"> 
	<html:checkbox  property="intake.cboxPTSd"   styleId="11_0_CHILD"      
	                onclick="checkAnxietyDisorder(this,document.intakeCForm)" />  
</td>
<td width="90%" colspan="2" class="style76">
	 Post-traumatic stress disorder (PTSD)
</td>
</tr>

<tr>
<td>&nbsp;
	 
</td>
<td width="5%">
	<html:checkbox  property="intake.cboxOCd"  styleId="11_1_CHILD"       
	                onclick="checkAnxietyDisorder(this,document.intakeCForm)" />  
</td>
<td colspan="2" class="style76">
	  	Obsessive compulsive disorder (OCD)
</td>
</tr>

<tr>
<td>&nbsp;
	 
</td>
<td width="5%">
	<html:checkbox  property="intake.cboxSubstanceAnxietyDisorder"  styleId="11_2_CHILD"      
	                onclick="checkAnxietyDisorder(this,document.intakeCForm)" />  
</td>
<td colspan="2" class="style76">
	  	Substance induced anxiety disorder
</td>
</tr>

<tr>
<td>&nbsp;
	 
</td>
<td width="5%">
	<html:checkbox  property="intake.cboxOtherAnxietyDisorder"   styleId="11_3_CHILD"       
	                onclick="checkAnxietyDisorder(this,document.intakeCForm)" />  
</td>
<td colspan="2" class="style76">
	  	Other anxiety disorder 
</td>
</tr>


<tr>
<td width="5%">
	<html:radio property="intake.radioPrimaryDiagnosis" value="3" onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" />
</td>
<td colspan="3"   class="style76">
	 Delirium, Dementia, Amnestic and Cognitive Disorders
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioPrimaryDiagnosis" value="4" onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" />
</td>
<td colspan="3"   class="style76">
	 Disorder of Childhood/Adolescence
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioPrimaryDiagnosis" value="5" onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" />
</td>
<td colspan="3"   class="style76">
	 Disassociative Disorders
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioPrimaryDiagnosis" value="6" onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" />
</td>
<td colspan="3"   class="style76">
	 Eating Disorders
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioPrimaryDiagnosis" value="7" onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" />
</td>
<td colspan="3"   class="style76">
	 Factitious Disorders
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioPrimaryDiagnosis" value="8" onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" />
</td>
<td colspan="3"   class="style76">
	 Impulse Control Disorders Not Elsewhere Classified
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioPrimaryDiagnosis" value="9" onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" />
</td>
<td colspan="3"   class="style76">
	 Mental Disorders due to General Medical Conditions
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioPrimaryDiagnosis" value="10" onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" />
</td>
<td colspan="3"   class="style76">
	 Mood Disorder
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioPrimaryDiagnosis" value="11" onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" />
</td>
<td colspan="3"   class="style76">
	 Personality Disorder
</td>
</tr>
<tr>
<td width="5%">
	<html:radio property="intake.radioPrimaryDiagnosis" value="12" onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" />
</td>
<td colspan="3"   class="style76">
	 Schizophrenia and other Psychotic Disorders
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioPrimaryDiagnosis" value="13" onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" />
</td>
<td colspan="3"   class="style76">
	 Sexual and Gender Identity Disorders
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioPrimaryDiagnosis" value="14" onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" />
</td>
<td colspan="3"   class="style76">
	 Sleep Disorders
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioPrimaryDiagnosis" value="15" onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" />
5</td>
<td colspan="3"   class="style76">
	 Somatoform Disorders
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioPrimaryDiagnosis" value="16" onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" />
</td>
<td colspan="3"   class="style76">
	 Substance-Related Disorders
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioPrimaryDiagnosis" value="17" onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" />
</td>
<td colspan="3"   class="style76">
	  	Developmental Handicap
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioPrimaryDiagnosis" value="18" onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" />
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
    	12. <a name="Num12">Secondary Diagnoses (Check ALL that apply) </a>
	<div align="right">
		<a href="#Top"><span  class="style126">Top</span></a><br>
		<!-- case management -->
	 </div>
    </td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndAdjustDisorder" />  
</td>
<td colspan="3"   class="style76">
	 Adjustment Disorders
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndAnxietyDisorder"  styleId="12_PARENT"    
		onclick="uncheck2ndAnxietyDisorderChildren(this,document.intakeCForm)" />  
</td>
<td colspan="3"   class="style76">
	 Anxiety Disorders 
</td>
</tr>

<tr>
<td>&nbsp;
	 
</td>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndAnxietyDisorderPSd"  styleId="12_0_CHILD"      
	                onclick="check2ndAnxietyDisorder(this,document.intakeCForm)" />   
</td>
<td width="90%" colspan="2" class="style76">
	 Post-traumatic stress disorder (PTSD)
</td>
</tr>

<tr>
<td>&nbsp;
	 
</td>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndAnxietyDisorderOCd"  styleId="12_1_CHILD"       
	                onclick="check2ndAnxietyDisorder(this,document.intakeCForm)" />   
</td>
<td colspan="2" class="style76">
	  	Obsessive compulsive disorder (OCD)
</td>
</tr>

<tr>
<td>&nbsp;
	 
</td>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndAnxietyDisorderFromSubstance"  styleId="12_2_CHILD"       
	                onclick="check2ndAnxietyDisorder(this,document.intakeCForm)" />   
</td>
<td colspan="2" class="style76">
	  	Substance induced anxiety disorder
</td>
</tr>

<tr>
<td>&nbsp;
	 
</td>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndAnxietyDisorderOther"  styleId="12_3_CHILD"       
	                onclick="check2ndAnxietyDisorder(this,document.intakeCForm)" />   
</td>
<td colspan="2" class="style76">
	  	Other anxiety disorder 
</td>
</tr>


<tr>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndCognitiveDisorder" />  
</td>
<td colspan="3"   class="style76">
	 Delirium, Dementia, Amnestic and Cognitive Disorders
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndChildhoodDisorder" />  
</td>
<td colspan="3"   class="style76">
	 Disorder of Childhood/Adolescence
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndDisassociativeDisorder" />  
</td>
<td colspan="3"   class="style76">
	 Disassociative Disorders
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndEatingDisorder" />  
</td>
<td colspan="3"   class="style76">
	 Eating Disorders
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndFactitiousDisorder" />  
</td>
<td colspan="3"   class="style76">
	 Factitious Disorders
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndImpulsiveDisorder" />  
</td>
<td colspan="3"   class="style76">
	 Impulse Control Disorders Not Elsewhere Classified
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndMedicalMentalDisorder" />  
</td>
<td colspan="3"   class="style76">
	 Mental Disorders due to General Medical Conditions
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndMoodDisorder" />  
</td>
<td colspan="3"   class="style76">
	 Mood Disorder
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndPersonalityDisorder" />  
</td>
<td colspan="3"   class="style76">
	 Personality Disorder
</td>
</tr>
<tr>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndSchizophrenia" />  
</td>
<td colspan="3"   class="style76">
	 Schizophrenia and other Psychotic Disorders
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndGenderIdentityDisorder" />  
</td>
<td colspan="3"   class="style76">
	 Sexual and Gender Identity Disorders
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndSleepDisorder" />  
</td>
<td colspan="3"   class="style76">
	 Sleep Disorders
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndSomatoformDisorder" />  
</td>
<td colspan="3"   class="style76">
	 Somatoform Disorders
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndSubstanceDisorder" />  
</td>
<td colspan="3"   class="style76">
	 Substance-Related Disorders
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndDevelopmentalDisorder" />  
</td>
<td colspan="3"   class="style76">
	  	Developmental Handicap
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cbox2ndUnknown" />  
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
    13. <a name="Num13">Other Illness Information (Check ALL that apply) </a>
	<div align="right">
		<a href="#Top"><span  class="style126">Top</span></a><br>
		<!-- case management -->
	 </div>
    </td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxConcurrentDisorder" />  
</td>
<td width="95%" colspan="3"   class="style76">
	 Concurrent Disorder (Substance Abuse & Mental Illness)
</td>
</tr>


<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxDualDisorder" />  
</td>
<td colspan="3"   class="style76">
	 Dual Diagnosis (Developmental Disability & Mental Illness)
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxMOHLTCDisorder" />  
</td>
<td colspan="3"   class="style76">
	 MOHLTC Initiatives (targeted illness such as Cancer, Diabetes and Cardiac)
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxOtherChronicIllness" />  
</td>
<td colspan="3"   class="style76">
	 Other Chronic Illnesses and/or Physical Disabilities
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxNa" />  
</td>
<td colspan="3"   class="style76">
	 Not Applicable
</td>
</tr>

</table>


<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center"  border="1" cellpadding="1" cellspacing="1">
<tr>
    <td colspan="4" class="style51"> 
	17. <a name="Num17">Baseline Health Care Access </a>
	<div align="right">
		<a href="#Top"><span  class="style126">Top</span></a><br>
		<!-- case management -->
	 </div>
	</td>
</tr>

<tr>
<td   colspan="4"  class="style76">
	Has someone to go to for health care when needed? 
</td>

</tr>


<tr>
<td width="5%">
	<html:radio property="intake.radioBaseHealthCareAccess" value="1"/> 
</td>
<td width="95%" colspan="3"   class="style76">
	   	 	Yes
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioBaseHealthCareAccess" value="2"/> 
</td>
<td colspan="3"   class="style76">
	   	 	Does not access health care
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioBaseHealthCareAccess" value="3"/> 
</td>
<td colspan="3"   class="style76">
	   	Unknown or Service Recipient Declined
</td>
</tr>



<tr>
<td   colspan="4"  class="style76">
	Where does client go for health care? (Check ALL that apply)
</td>

</tr>


<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxBaseHasRegularHealthProvider" />  
</td>
<td width="95%" colspan="3"   class="style76">
	   	  	Has regular health care provider (e.g. doctor, nurse practitioner, community health centre)
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxBaseUseShelterClinic" />  
</td>
<td colspan="3"   class="style76">
	   	 	 	Uses a clinic at a shelter, hostel or drop-in (e.g. nursing clinics)
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxBaseUseHealthBus" />  
</td>
<td colspan="3"   class="style76">
	   	 	Uses the Health Bus
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxBaseUseWalkinClinic" />  
</td>
<td colspan="3"   class="style76">
	    	Uses walk-in clinic
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxBaseUseHospitalEmergency" />  
</td>
<td colspan="3"   class="style76">
	   	  	Uses hospital emergency room
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxBaseDoNotAccessHealthCare" />  
</td>
<td colspan="3"   class="style76">
	   	  	Does not access health care
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxBaseAccessHealthCareUnknown" />  
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
	18. <a name="Num18">Current Health Care Access </a>
	<div align="right">
		<a href="#Top"><span  class="style126">Top</span></a><br>
		<!-- case management -->
	 </div>
	</td>
</tr>

<tr>
<td   colspan="4"  class="style76">
	Has someone to go to for health care when needed? 
</td>

</tr>


<tr>
<td width="5%">
	<html:radio property="intake.radioCurrHealthCareAccess" value="1"/> 
</td>
<td width="95%" colspan="3"   class="style76">
	   	 	Yes
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioCurrHealthCareAccess" value="2"/> 
</td>
<td colspan="3"   class="style76">
	   	 	Does not access health care
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioCurrHealthCareAccess" value="3"/> 
</td>
<td colspan="3"   class="style76">
	   	Unknown or Service Recipient Declined
</td>
</tr>



<tr>
<td   colspan="4"  class="style76">
	Where does client go for health care? (Check ALL that apply)
</td>

</tr>


<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxCurrHasRegularHealthProvider" />  
</td>
<td width="95%" colspan="3"   class="style76">
	   	  	Has regular health care provider (e.g. doctor, nurse practitioner, community health centre)
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxCurrUseShelterClinic" />  
</td>
<td colspan="3"   class="style76">
	   	 	 	Uses a clinic at a shelter, hostel or drop-in (e.g. nursing clinics)
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxCurrUseHealthBus" />  
</td>
<td colspan="3"   class="style76">
	   	 	Uses the Health Bus
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxCurrUseWalkinClinic" />  
</td>
<td colspan="3"   class="style76">
	    	Uses walk-in clinic
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxCurrUseHospitalEmergency" />  
</td>
<td colspan="3"   class="style76">
	   	  	Uses hospital emergency room
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxCurrDoNotAccessHealthCare" />  
</td>
<td colspan="3"   class="style76">
	   	  	Does not access health care
</td>
</tr>

<tr>
<td width="5%">
	<html:checkbox  property="intake.cboxCurrAccessHealthCareUnknown" />  
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
	23. <a name="Num23">Resistant to Treatment? </a>
	<div align="right">
		<a href="#Top"><span  class="style126">Top</span></a><br>
		<!-- case management -->
	 </div>
	</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioResistTreatment" value="1"/> 
</td>
<td width="95%" colspan="3"   class="style76">
	  	  Yes
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioResistTreatment" value="2"/> 
</td>
<td colspan="3"   class="style76">
	  	 No
</td>
</tr>

<tr>
<td width="5%">
	<html:radio property="intake.radioResistTreatment" value="3"/> 
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
    26. <a name="Num26">Current Hospitalizations </a> 
	<div align="right">
		<a href="#Top"><span  class="style126">Top</span></a><br>
		<!-- case management -->
	 </div>
    </td>
</tr>

<tr>
<td width="17%" class="style76">
	Date of hospitalization:  
</td>
<td width="15%">
	<html:text styleClass="style71" property="intake.dateOfHospitalization"/> 
</td>
<td width="68%"  colspan="2" class="style76">
(YYYY/MM/DD) 
</td>
</tr>


<tr>
<td width="17%" class="style76">
	Length of hospitalization:  
</td>
<td width="15%">
	<html:text styleClass="style71" property="intake.lengthOfHospitalization"/> 
</td>
<td width="68%"  colspan="2" class="style76">
days 
</td>
</tr>

<tr>
<td width="11%">
	<html:checkbox  property="intake.cboxPsychiatricHospitalization" />  
</td>
<td colspan="3" class="style76">
	  	   	Psychiatric Hospitalization 
</td>
</tr>

<tr>
<td width="11%">
	<html:checkbox  property="intake.cboxPhysicalHospitalization" />  
</td>
<td colspan="3" class="style76">
	  	    	Physical Health Hospitalization
</td>
</tr>

<tr>
<td width="11%">
	<html:checkbox  property="intake.cboxHospitalizationUnknown" />  
</td>
<td colspan="3" class="style76">
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