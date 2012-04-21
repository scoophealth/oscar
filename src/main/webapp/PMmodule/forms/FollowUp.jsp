
<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>



<%@ include file="/taglibs.jsp"%>
<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>INTAKE B. COUNSELLOR ASSESSMENT</title>
<link rel="stylesheet" type="text/css"
	href="<html:rewrite page="/css/intakeB.css"/>" />
<script language="JavaScript"
	src="<html:rewrite page="/js/IntakeA.js" />"></script>
<html:base />
<script>
   function onPrint() {
        window.print();
    }
    function onSave() 
	{
        document.forms[0].submitForm.value="save";
        document.forms[0].method.value ='save';
        
//        document.forms[0].demographicNo.value="";
        
        var ret = checkAllDates();
		if(ret)
		{
			ret = checkAllIntegers();
		}
        if(ret)
        {
            ret = confirm("Are you sure you want to save this form?");
        }
        return ret;
		
    }
    function onUpdate(path) 
	{
        document.forms[0].submitForm.value="update";
        document.forms[0].method.value ='update';
        var ret = checkAllDates();
		if(ret)
		{
			ret = checkAllIntegers();
		}
        if(ret)
        {
            ret = confirm("Are you sure you want to update this form?");
            if(ret)
            {
            	document.forms[0].action = path+"?actionType=update";
            	
            	document.forms[0].submit();
            	
            }
        }
    }
</script>
</head>

<body>
<html:form action="/PMmodule/Forms/FollowUp">
	<input type="hidden" name="method" value="save" />


	<html:hidden property="intake.demographicNo" />

	<table width="100%" border="0">
		<tr>
			<td width="5%"></td>

			<td>
			<table width="95%" border="0">
				<tr>
					<td colspan="3" class="style63">INTAKE B. COUNSELLOR
					ASSESSMENT &nbsp;&nbsp;&nbsp; <!--  cancel button --></td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td class="style76">Date of assessment (YYYY/MM/DD): <br>
					<html:text property="intake.dateAssessment" /></td>
					<td class="style76">Assessment start time: <br>
					<html:text property="intake.assessStartTime" /> am / pm</br>
					</td>
					<td class="style76">Date client entered Seaton House
					(YYYY/MM/DD): &nbsp; <html:text property="intake.dateEnteredSeaton" />
					<br>
					Date client exited Seaton House (YYYY/MM/DD):
					&nbsp;&nbsp;&nbsp;&nbsp; <html:text
						property="intake.dateExitedSeaton" /></td>
				</tr>
			</table>



			<table width="95%" border="1">
				<tr>
					<td colspan="3" align="center" class="style51">CLIENT
					INFORMATION</td>
				</tr>
				<tr>
					<td align="left" class="style76">Client's Surname <html:text
						property="intake.clientSurname" /></td>
					<td align="left" class="style76">Client's First Name <html:text
						property="intake.clientFirstName" /></td>

					<td align="left" class="style76">Date of Birth<br>
					(MM/DD/YYYY)<br>
					<html:select property="intake.month">
						<%for(int x=1;x<13;x++) { 
				String var = String.valueOf(x);
				if(var.length() == 1) { var = "0" + var;}
			%>
						<html:option value="<%=var%>"><%=var %></html:option>
						<% } %>
					</html:select> <html:select property="intake.day">
						<%for(int x=1;x<32;x++) { 
				String var = String.valueOf(x);
				if(var.length() == 1) { var = "0" + var;}
			%>
						<html:option value="<%=var%>"><%=var %></html:option>
						<% } %>
					</html:select> <html:text property="intake.year" size="7" maxlength="4" /></td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td align="center" class="style51">NOTES FOR COMPLETING THIS
					INTAKE FORM</td>
				</tr>
				<tr>
					<td align="left" class="style76">Counsellor:<br>
					1. Please review Assessment A, including 'Assistance Required'
					section to respond to client's request for assistance.<br>
					2. Each numbered section of this intake corresponds to a
					Case-Management page (attached). After completing each section,
					enter complete the relevant case management page, including a
					timeline for review of client goals.</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td align="center" class="style51">INTRODUCTION</td>
				</tr>
				<tr>
					<td align="left" class="style76">Counsellor:<br>
					1.Please introduce yourself to your client: give your name and some
					information about yourself (e.g., how long you have worked at
					Seaton House).<br>
					2. Please read the following statement to the client:"We are trying
					to ensure that you and others that stay at Seaton House have access
					to health care and other services if you need it. Please answer
					these questions so that we can better help you."</td>
				</tr>
				<tr>
					<td align="left" class="style76">Question 1. How did you hear
					about Seaton House? &nbsp;&nbsp;&nbsp; <html:text
						property="intake.howHearAboutSeaton" /></td>
				</tr>
				<tr>
					<td align="left" class="style76">Question 2. Where were you
					before coming to Seaton House? &nbsp;&nbsp;&nbsp; <html:text
						property="intake.whereBeforeSeaton" size="55" /></td>
				</tr>

			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="4" align="center" class="style51">1.
					IDENTIFICATION</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">NOTE TO
					COUNSELLORS:<br>
					ID is required for some internal referrals. If SIN, Health Card or
					Birth Certificate is missing, please begin process of securing a
					replacement</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">Is photocopy of
					ID included in this file? &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:radio
						property="intake.radioHasidinfile" value="yes" />Yes
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:radio
						property="intake.radioHasidinfile" value="no" />No</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">1. What
					identification do you need assistance with?</td>
				</tr>
				<tr>
				<tr>
					<td e colspan="1" align="left" class="style76">
					<table width="100%" border="0">
						<tr>
							<td colspan="1" align="left" class="style76"><html:checkbox
								property="intake.cboxAssistwithsincard" value="Y" />SIN Card:<br>
							<html:checkbox property="intake.cboxAssistwithimmigrant"
								value="Y" />Landed Immigrant<br>
							</td>
							<td colspan="1" align="left" class="style76"><html:checkbox
								property="intake.cboxAssistwithhealthcard" value="Y" />Ontario
							Health Card<br>
							<html:checkbox property="intake.cboxAssistwithrefugee" value="Y" />Convention
							Refugee</td>
							<td colspan="1" align="left" class="style76"><html:checkbox
								property="intake.cboxAssistwithbirthcert" value="Y" />Birth
							Certificate<br>
							<html:checkbox property="intake.cboxAssistwithnone" value="Y" />None
							</td>
							<td colspan="1" align="left" class="style76"><html:checkbox
								property="intake.cboxAssistwithcitizencard" value="Y" />Citizenship
							Card<br>
							<html:checkbox property="intake.cboxAssistwithother" value="Y" />Other
							&nbsp;&nbsp; <html:text property="intake.assistWithOther"
								size="35" /></td>
						</tr>
						<tr>
							<td colspan="4" align="left" valign="top" class="style76">
							Comments:<br>
							<html:textarea property="intake.commentsOnID" cols="75"></html:textarea>
							</td>
						</tr>

					</table>
					</td>
				</tr>
			</table>


			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">2. PHYSICAL AND
					MENTAL HEALTH</td>
				</tr>
				<tr>
					<td align="left" class="style76">
					<table width="100%" border="0">
						<tr>
							<td colspan="2" align="left" class="style76">1. Do you have
							any health care coverage?</td>
							<td colspan="1" align="left" class="style76"><html:radio
								property="intake.radioHavehealthcoverage" value="no" />No</td>
							<td colspan="1" align="left" class="style76"><html:radio
								property="intake.radioHavehealthcoverage" value="yes" />Yes</td>
							<td colspan="1" align="left" class="style76">If yes, what
							type:</td>
							<td colspan="1" align="left" class="style76"><html:checkbox
								property="intake.cboxHaveohip" value="Y" />OHIP</td>
						</tr>
						<tr>
							<td colspan="5" align="left" class="style76">&nbsp;</td>

							<td align="left" class="style76"><html:checkbox
								property="intake.cboxHaveodsp" value="Y" />ODSP</td>
						</tr>

						<tr>
							<td colspan="5" align="left" class="style76">&nbsp;</td>

							<td align="left" class="style76"><html:checkbox
								property="intake.cboxHaveodb" value="Y" />ODB Card</td>
						</tr>
						<tr>
							<td colspan="5" align="left" class="style76">&nbsp;</td>

							<td colspan="1" align="left" class="style76"><html:checkbox
								property="intake.cboxHaveother1" value="Y" />Other (specify) <html:text
								property="intake.haveOther" size="27" /></td>
						</tr>
					</table>

					<table width="100%" border="0">

						<tr>
							<td colspan="6" class="style76">2. Has a health professional
							ever diagnosed you with or do you believe you have a problem with
							mental health?</td>
						</tr>

						<tr>
							<td colspan="2" class="style76">&nbsp;</td>

							<td colspan="1" class="style76"><html:radio
								property="intake.radioHavementalproblem" value="yes" />Yes</td>
							<td colspan="1" class="style76"><html:radio
								property="intake.radioHavementalproblem" value="no" />No</td>
							<td colspan="1" class="style76"><html:radio
								property="intake.radioHavementalproblem" value="dontknow" />Don't
							know</td>
							<td colspan="1" class="style76"><html:radio
								property="intake.radioHavementalproblem" value="refuse" />Refuse
							to answer</td>
						</tr>

					</table>

					<table width="100%" border="1">
						<tr>
							<td colspan="6" class="style76">3. Do you have any of the
							following conditions? (read list and check all that apply)</td>
						</tr>

						<tr>
							<td colspan="2" class="style76"><html:checkbox
								property="intake.cboxHaveschizophrenia" value="Y" />
							Schizophrenia <br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							If yes, is this being cared for by a doctor?</td>
							<td colspan="1" valign="bottom" class="style76"><html:radio
								property="intake.radioCaredforschizophrenia" value="yes" />Yes
							--> Q4</td>
							<td colspan="1" valign="bottom" class="style76"><html:radio
								property="intake.radioCaredforschizophrenia" value="no" />No -->
							Q5</td>
							<td colspan="1" valign="bottom" class="style76"><html:radio
								property="intake.radioCaredforschizophrenia" value="dontknow" />Don't
							know</td>
							<td colspan="1" valign="bottom" class="style76"><html:radio
								property="intake.radioCaredforschizophrenia" value="refuse" />Refuse
							to answer</td>
						</tr>

						<tr>
							<td colspan="2" class="style76"><html:radio
								property="intake.cboxHavemanic" value="Y" />Manic depression /
							bipolar disorder <br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							If yes, is this being cared for by a doctor?</td>
							<td colspan="1" valign="bottom" class="style76"><html:radio
								property="intake.radioCaredformanic" value="yes" />Yes --> Q4</td>
							<td colspan="1" valign="bottom" class="style76"><html:radio
								property="intake.radioCaredformanic" value="no" />No --> Q5</td>
							<td colspan="1" valign="bottom" class="style76"><html:radio
								property="intake.radioCaredformanic" value="dontknow" />Don't
							know</td>
							<td colspan="1" valign="bottom" class="style76"><html:radio
								property="intake.radioCaredformanic" value="refuse" />Refuse to
							answer</td>
						</tr>
						<tr>
							<td colspan="2" class="style76"><html:checkbox
								property="intake.cboxHavedepression" value="Y" />Depression<br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							If yes, is this being cared for by a doctor?</td>
							<td colspan="1" valign="bottom" class="style76"><html:radio
								property="intake.radioCaredfordepression" value="yes" />Yes -->
							Q4</td>
							<td colspan="1" valign="bottom" class="style76"><html:radio
								property="intake.radioCaredfordepression" value="no" />No --> Q5
							</td>
							<td colspan="1" valign="bottom" class="style76"><html:radio
								property="intake.radioCaredfordepression" value="dontknow" />Don't
							Know</td>
							<td colspan="1" valign="bottom" class="style76"><html:radio
								property="intake.radioCaredfordepression" value="refuse" />Refuse
							to answer</td>
						</tr>
						<tr>
							<td colspan="2" class="style76"><html:checkbox
								property="intake.cboxHaveanxiety" value="Y" />Anxiety disorder
							(panic attacks, generalized anxiety) <br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							If yes, is this being cared for by a doctor?</td>
							<td colspan="1" valign="bottom" class="style76"><html:radio
								property="intake.radioCaredforanxiety" value="yes" />Yes --> Q4
							</td>
							<td colspan="1" valign="bottom" class="style76"><html:radio
								property="intake.radioCaredforanxiety" value="no" />No --> Q5</td>
							<td colspan="1" valign="bottom" class="style76"><html:radio
								property="intake.radioCaredforanxiety" value="dontknow" />Don't
							know</td>
							<td colspan="1" valign="bottom" class="style76"><html:radio
								property="intake.radioCaredforanxiety" value="refuse" />Refuse
							to answer</td>
						</tr>
						<tr>
							<td colspan="2" class="style76"><html:checkbox
								property="intake.cboxHaveother2" value="Y" />Other (specify)<br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							If yes, is this being cared for by a doctor?</td>
							<td colspan="1" valign="bottom" class="style76"><html:radio
								property="intake.radioCaredforother" value="yes" />Yes --> Q4</td>
							<td colspan="1" valign="bottom" class="style76"><html:radio
								property="intake.radioCaredforother" value="no" />No --> Q5</td>
							<td colspan="1" valign="bottom" class="style76"><html:radio
								property="intake.radioCaredforother" value="dontknow" />Don't
							know</td>
							<td colspan="1" valign="bottom" class="style76"><html:radio
								property="intake.radioCaredforother" value="refuse" />Refuse to
							answer</td>
						</tr>
					</table>

					<table width="100%" border="1">
						<tr>
							<td colspan="4" class="style76">4a. Doctor's Name & Address
							<br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:text
								property="intake.doctor1NameAddr" size="55" /></td>
							<td colspan="2" valign="bottom" class="style76">Phone #
							&nbsp;&nbsp;&nbsp; <html:text property="intake.doctor1Phone"
								size="25" /></td>
						</tr>
						<tr>
							<td colspan="6" class="style76">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date of last contact with
							Doctor (YYYY/MM/DD)&nbsp;&nbsp;&nbsp; <html:text
								property="intake.dateLastDoctor1Contact" size="55" /></td>
						</tr>
					</table>

					<table width="100%" border="1">
						<tr>
							<td colspan="4" class="style76">4b. Doctor's Name & Address
							<br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:text
								property="intake.doctor2NameAddr" size="55" /></td>
							<td colspan="2" valign="bottom" class="style76">Phone #
							&nbsp;&nbsp;&nbsp; <html:text property="intake.doctor2Phone"
								size="25" /></td>
						</tr>
						<tr>
							<td colspan="6" class="style76">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date of last contact with
							Doctor (YYYY/MM/DD)&nbsp;&nbsp;&nbsp; <html:text
								property="intake.dateLastDoctor2Contact" size="55" /></td>
						</tr>
					</table>

					<table width="100%" border="1">
						<tr>
							<td colspan="2" class="style76">5. Some clients need
							assistance with medications, such as safe storage, remembering to
							take them, etc. Do you require assistance with any medications?</td>
							<td colspan="2" valign="bottom" class="style76"><html:radio
								property="intake.radioNeedassistwithmedication" value="yes" />Yes
							--> Q6</td>
							<td colspan="2" valign="bottom" class="style76"><html:radio
								property="intake.radioNeedassistwithmedication" value="no" />No
							--> Section 3</td>
						</tr>
					</table>




					<table width="100%" border="1">
						<tr>
							<td colspan="6" class="style76">6. Do you need help with any
							of the following: (read list and check all that apply)</td>
						</tr>
						<tr>
							<td colspan="2" valign="top" class="style76"><html:checkbox
								property="intake.cboxRemembertotakemedication" value="Y" />remembering
							to take medication <br>
							<html:checkbox property="intake.cboxGetmoremedication" value="Y" />getting
							more medication</td>
							<td colspan="2" valign="top" class="style76"><html:checkbox
								property="intake.cboxStoremedication" value="Y" />safe storage
							of medication<br />
							<html:checkbox property="intake.cboxNeedhelpinother" value="Y" />Other
							(specify)</td>
							<td colspan="2" valign="top" class="style76"><html:checkbox
								property="intake.cboxTakeprescribedmedication" value="Y" />taking
							the medications as prescribed</td>
						</tr>

						<tr>
							<td colspan="6" align="left" valign="top" class="style76">
							Comments:<br>
							<html:textarea property="intake.commentsOnNeedHelp" cols="75"></html:textarea>

							</td>
						</tr>

					</table>

					</td>
				</tr>
			</table>


			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">3. ADDICTIONS</td>
				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">3.1 ALCOHOL USE</td>
				</tr>

				<tr>
					<td colspan="2" align="left" class="style76">1. Do you drink
					alcohol?</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioDoyoudrink" value="yes" />Yes</td>
					<td colspan="2" align="left" class="style76"><html:radio
						property="intake.radioDoyoudrink" value="no" />No --> Section 3.2</span>
					</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioDoyoudrink" value="refuse" />Refuse to
					answer</td>
				</tr>



				<tr>
					<td colspan="1" align="left" class="style76">2. How much and
					how often?</td>
					<td colspan="1" align="left" class="style76"><html:text
						property="intake.drinksPerDay" size="3" /> drinks/day</td>
					<td colspan="1" align="left" class="style76"><html:text
						property="intake.drinksPerWeek" size="3" /> drinks/week</td>
					<td colspan="1" align="left" class="style76"><html:text
						property="intake.drinksPerMonth" size="3" /> drinks/month</td>

					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioHowmuchdrink" value="uncertain" />Uncertain
					</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioHowmuchdrink" value="refuse" />Refuse to
					answer</td>
				</tr>

				<tr>
					<td colspan="2" align="left" class="style76">3. Do you ever
					drink any of the following:</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioDrinkthese" value="rubbingAlcohol" />Rubbing
					Alcohol</td>
					<td colspan="2" align="left" class="style76"><html:radio
						property="intake.radioDrinkthese" value="chineseCookingWine" />Chinese
					cooking wine</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioDrinkthese" value="mouthWash" />Mouthwash</td>
				</tr>

				<tr>
					<td colspan="2" class="style76">4. In the past year have you
					seen a physician about drug or alcohol addiction issues?</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioSeendoctorregalcohol" value="yes" />Yes</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioSeendoctorregalcohol" value="no" />No</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioSeendoctorregalcohol" value="uncertain" />Uncertain
					</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioSeendoctorregalcohol" value="refuse" />Refuse
					to answer</td>
				</tr>

				<tr>
					<td colspan="2" class="style76">5. Do you want help with
					quitting or reducing drinking?</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioWanthelpquit" value="yes" />Yes</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioWanthelpquit" value="no" />No</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioWanthelpquit" value="uncertain" />Uncertain
					</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioWanthelpquit" value="refuse" />Refuse to
					answer</td>
				</tr>

				<tr>
					<td colspan="6" align="left" valign="top" class="style76">
					Comments:<br>
					<html:textarea property="intake.commentsOnAlcohol" cols="75"></html:textarea>
					</td>
				</tr>

				<tr>
					<td colspan="6" align="left" valign="top" class="style76">3.2
					STREET DRUGS & SOLVENT USE</td>
				</tr>

				<tr>
					<td colspan="2" class="style76">1a. Do you use street drugs or
					solvents?</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioUsedrugs" value="yes" />Yes</td>
					<td colspan="2" valign="top" class="style76"><html:radio
						property="intake.radioUsedrugs" value="no" />No --> Section 4</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioUsedrugs" value="refuse" />Refuse to answer
					</td>
				</tr>

				<tr>
					<td colspan="4" class="style76">1b. What type? How often?</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioDrugusefrequency" value="uncertain" />Uncertain
					</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioDrugusefrequency" value="refuse" />Refuse to
					answer</td>
				</tr>

				<tr>
					<td colspan="2" class="style76">2. Do you want help with
					quitting or reducing drug or solvent use?</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioWanthelpquitdrug" value="yes" />Yes</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioWanthelpquitdrug" value="no" />No</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioWanthelpquitdrug" value="uncertain" />Uncertain
					</td>

					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioWanthelpquitdrug" value="refuse" />Refuse to
					answer</td>
				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">Comments:<br>
					<html:textarea property="intake.commentsOnStreetDrugs" cols="75"></html:textarea>

					</td>
				</tr>
			</table>



			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">4. HOUSING</td>
				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">1. What type of
					housing are you interested in? (e.g., shared, independent, rooming
					house) <br>
					<html:text property="intake.housingInterested" size="90" /></td>
				</tr>


				<tr>
					<td colspan="3" align="left" class="style76">2. Do you want an
					appointment with a housing counsellor?</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioWantappmt" value="yes" />Yes</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioWantappmt" value="no" />No</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioWantappmt" value="uncertain" />Uncertain</td>
				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">3. What was your
					last address? <br>
					<html:text property="intake.clientLastAddress" size="90" /></td>
				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">4. Where was the
					last place you paid rent? (give details if different from Q3) <br>
					<html:text property="intake.clientLastAddressPayRent" size="90" />
					</td>
				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">5. How long did
					you live there? Give approximate date
					(YYYY/MM/DD)&nbsp;&nbsp;&nbsp; <html:text
						property="intake.dateLivedThere" size="50" /></td>
				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">6. Who were you
					living with? (Roommate, family, etc.) &nbsp;&nbsp;&nbsp; <html:text
						property="intake.livedWithWhom" size="50" /></td>
				</tr>

				<tr>
					<td colspan="2" align="left" class="style76">7. Have you ever
					lived in subsidized housing (If yes, specify)?</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioLivedinsubsidized" value="yes" />Yes --> Q8
					</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioLivedinsubsidized" value="no" />No -->
					Section 5</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioLivedinsubsidized" value="uncertain" />Uncertain
					</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioLivedinsubsidized" value="refuse" />Refuse
					to answer</td>

				</tr>

				<tr>
					<td colspan="2" align="left" class="style76">8. Do you owe any
					money to a landlord of a subsidized unit?</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioOwedrent" value="yes" />Yes --> Q9</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioOwedrent" value="no" />No --> Section 5</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioOwedrent" value="uncertain" />Uncertain</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioOwedrent" value="refuse" />Refuse to answer
					</td>

				</tr>

				<tr>
					<td colspan="4" align="left" class="style76">9. Where do you
					owe money? &nbsp;&nbsp;&nbsp; <html:text
						property="intake.whereOweRent" size="50" /></td>
					<td colspan="2" align="left" class="style76">Amount $:
					&nbsp;&nbsp;&nbsp; <html:text property="intake.amtOwing" size="5" />
					</td>
				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">Comments:<br>
					<html:textarea property="intake.commentsOnHousing" cols="75"></html:textarea>
					</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">5. EDUCATION</td>
				</tr>
				<tr>
					<td colspan="5" align="left" class="style76">1. Excluding
					kindergarten, how many years of elementary and high school have you
					successfully completed?</td>
					<td width="30%" colspan="1" align="left" class="style76"><html:text
						property="intake.yearsOfEducation" size="5" /></td>
				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">

					<table width="100%" border="0">

						<tr>
							<td class="style76">Do you have any of the following:</td>
						</tr>
						<tr>
							<td colspan="1" align="left" class="style76"><html:checkbox
								property="intake.cboxHavehighschool" value="Y" />High School
							diploma</td>
							<td colspan="1" align="left" class="style76"><html:checkbox
								property="intake.cboxHavecollege" value="Y" />College diploma</td>
							<td colspan="2" align="left" class="style76"><html:checkbox
								property="intake.cboxHaveuniversity" value="Y" />University
							undergraduate degree (BA, BSc etc)</td>
							<td colspan="2" valign="top" class="style76"><html:checkbox
								property="intake.cboxHaveother3" value="Y" />Other</td>

						</tr>
					</table>
					</td>
				</tr>

				<tr>
					<td colspan="2" align="left" class="style76">2. Are you
					interested in going back to school?</td>
					<td width="9%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioInterestbacktoschool" value="yes" />Yes</td>
					<td width="9%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioInterestbacktoschool" value="no" />No</td>
					<td width="13%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioInterestbacktoschool" value="uncertain" />Uncertain
					</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioInterestbacktoschool" value="refuse" />Refuse
					to answer</td>

				</tr>

				<tr>
					<td colspan="2" align="left" class="style76">3. Do you require
					a referral for ESL classes?</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioRequirereferraltoesl" value="yes" />Yes</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioRequirereferraltoesl" value="no" />No</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioRequirereferraltoesl" value="uncertain" />Uncertain
					</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioRequirereferraltoesl" value="refuse" />Refuse
					to answer</td>

				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">Comments:<br>
					<html:textarea property="intake.commentsOnEducation" cols="75"></html:textarea>
					</td>
				</tr>

			</table>


			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">6. EMPLOYMENT</td>
				</tr>

				<tr>
					<td colspan="2" align="left" class="style76">1. Are you
					currently employed?</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioCurrentlyemployed" value="yes" />Yes</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioCurrentlyemployed" value="no" />No --> Skip
					to Q3</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioCurrentlyemployed" value="uncertain" />Uncertain
					</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioCurrentlyemployed" value="refuse" />Refuse
					to answer</td>

				</tr>


				<tr>
					<td colspan="3" align="left" class="style76">2. If yes, how
					long have you been employed? &nbsp;&nbsp;&nbsp; <html:text
						property="intake.howLongEmployed" size="5" /></td>
					<td colspan="3" align="left" class="style76">If no, how long
					have you been unemployed? &nbsp;&nbsp;&nbsp; <html:text
						property="intake.howLongUnemployed" size="5" /></td>

				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">3. What is your
					usual occupation? &nbsp;&nbsp;&nbsp; <html:text
						property="intake.usualOccupation" size="50" /></td>
				</tr>

				<tr>
					<td colspan="2" align="left" class="style76">4. Are you
					interested in Employment Counselling or Job Training?</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioInterestedintraining" value="yes" />Yes</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioInterestedintraining" value="no" />No</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioInterestedintraining" value="uncertain" />Uncertain
					</td>
					<td colspan="1" valign="top" class="style76"><html:radio
						property="intake.radioInterestedintraining" value="refuse" />Refuse
					to answer</td>

				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">Comments:<br>
					<html:textarea property="intake.commentsOnEmployment" cols="75"></html:textarea>

					</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="9" align="center" class="style51">7. FINANCIAL</td>
				</tr>
				<tr>
					<td colspan="9" align="left" class="style76">1. What has been
					your main source of income in the past 12 months (Please state
					source and monthly amount) <br>
					<html:text property="intake.mainSourceOfIncome" size="70" /></td>
				</tr>

				<tr>
					<td colspan="1" align="left" class="style76"><html:checkbox
						property="intake.cboxOw" value="Y" />OW:</td>
					<td colspan="1" align="left" class="style76"><html:checkbox
						property="intake.cboxOdsp" value="Y" />ODSP</td>
					<td colspan="2" align="left" class="style76"><html:checkbox
						property="intake.cboxEmployment" value="Y" />Employment --> Q2</td>
					<td colspan="1" align="left" class="style76"><html:checkbox
						property="intake.cboxUi" value="Y" />UI --> Q2</td>
					<td colspan="1" align="left" class="style76"><html:checkbox
						property="intake.cboxOas" value="Y" />OAS --> Q2</td>
					<td colspan="1" align="left" class="style76"><html:checkbox
						property="intake.cboxCpp" value="Y" />CPP --> Q2</td>
					<td colspan="2" align="left" class="style76"><html:checkbox
						property="intake.cboxOther" value="Y" />Other (specify) --> Q2</td>
				</tr>
				<tr>
					<td colspan="9" align="left" class="style76">2. Approximately
					how much do you receive: $ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:text
						property="intake.howMuchYouReceive" size="7" /></td>
				</tr>

				<tr>
					<td colspan="5" align="left" class="style76">3. Do you have a
					Public Trustee?</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioHavepublictrustee" value="yes" />Yes --> Q4
					</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioHavepublictrustee" value="no" />No</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioHavepublictrustee" value="uncertain" />Uncertain
					</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioHavepublictrustee" value="refuse" />Refuse
					to answer</td>
				</tr>

				<tr>
					<td colspan="9" align="left" class="style76">4. Name, address
					and phone # of Public Trustee <br>
					<html:text property="intake.publicTrusteeInfo" size="120" /></td>
				</tr>

				<tr>
					<td colspan="5" align="left" class="style76">5. Are you
					entitled to any other type(s) of income?</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioEntitledtootherincome" value="yes" />Yes -->
					</td>
					<td colspan="2" align="left" class="style76">Type: <html:text
						property="intake.typeOfIncome" size="35" /></td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioEntitledtootherincome" value="no" />No</td>
				</tr>
				<tr>
					<td colspan="9" class="style76">
					<table width="95%" border="0">
						<tr>
							<td colspan="9" class="style76">6. Have you ever made an
							application for any other type of income?</td>
						</tr>

						<tr>
							<td colspan="1" align="left" class="style76"><html:radio
								property="intake.radioEvermadeappforotherincome" value="no" />No
							</td>

							<td colspan="1" align="left" class="style76"><nobr> <html:radio
								property="intake.radioEvermadeappforotherincome" value="yes" />Yes
							--> </td>
							<td colspan="4" align="left" class="style76">Type: <html:text
								property="intake.everMadeAppforOtherIncome" /></td>
							<td colspan="3" align="left" class="style76"><nobr>When
							(YYYY/MM/DD): <html:text
								property="intake.whenMadeAppforOtherIncome" /> </nobr></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan="9" align="left" class="style76">Comments:<br>
					<html:textarea property="intake.commentsOnFinance" cols="75"></html:textarea>
					</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="9" align="center" class="style51">10. LEGAL
					ISSUES</td>
				</tr>

				<tr>
					<td colspan="4" align="left" class="style76">1. Have you ever
					been incarcerated?</td>
					<td colspan="2" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioEverbeenjailed" value="yes" />Yes
					--> Q2 </nobr></td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioEverbeenjailed" value="no" />No
					--> Q3 </nobr></td>

					<td colspan="2" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioEverbeenjailed" value="refuse" />Refuse
					to answer </nobr></td>
				</tr>

				<tr>
					<td colspan="9" align="left" class="style76">2. Give brief
					history of incarceration(s) (date, reason, location)<br>
					<html:textarea property="intake.historyOfJail" cols="75"></html:textarea>
					</td>
				</tr>

				<tr>
					<td colspan="9">
					<table width="100%" border="0">
						<tr>
							<td colspan="4" align="left" class="style76">3. Do you have
							any current outstanding legal issues that you need assistance
							with?</td>
							<td colspan="1" align="left" valign="top" class="style76"><nobr>
							<html:radio property="intake.radioNeedassistinlegal" value="yes" />Yes
							</nobr></td>
							<td colspan="2" align="left" valign="top" class="style76"><nobr>
							<html:radio property="intake.radioNeedassistinlegal" value="no" />No
							--> Section11 </nobr></td>

							<td colspan="2" align="left" valign="top" class="style76"><nobr>
							<html:radio property="intake.radioNeedassistinlegal"
								value="refuse" />Refuse to answer </nobr></td>
						</tr>
						<tr>
							<td colspan="9" class="style76">If yes, specify:
							&nbsp;&nbsp;&nbsp; <html:text property="intake.needAssistInLegal"
								size="55" /></td>
						</tr>
						<tr>
							<td colspan="9" align="left" class="style76">Comments:<br>
							<html:textarea property="intake.commentsOnLegalIssues" cols="75"></html:textarea>
							</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>


			<table width="95%" border="1">
				<tr>
					<td colspan="9" align="center" class="style51">11. IMMIGRATION
					ISSUES</td>
				</tr>
				<tr>
					<td colspan="3" align="left" valign="top" class="style76">1.
					Are you a Canadian Citizen?</td>
					<td colspan="2" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioCitizen" value="yes" />Yes -->
					Section 11 </nobr></td>
					<td colspan="1" align="left" valign="top" class="style76"><html:radio
						property="intake.radioCitizen" value="no" />No</td>
					<td colspan="1" align="left" valign="top" class="style76"><html:radio
						property="intake.radioCitizen" value="uncertain" />Uncertain</span></td>
					<td colspan="2" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioCitizen" value="refuse" />Refuse
					to answer </nobr></td>
				</tr>

				<tr>
					<td colspan="9">
					<table width="100%" border="0">
						<tr>
							<td colspan="9" align="left" class="style76">2. What is your
							status in Canada (read list and check one)</td>
						</tr>
						<tr>
							<td colspan="2" align="left" valign="top" class="style76"><nobr>
							<html:radio property="intake.radioYourcanadianstatus"
								value="immigrant" />Landed Immigrant --> Q4 </nobr></td>
							<td colspan="2" align="left" valign="top" class="style76"><nobr>
							<html:radio property="intake.radioYourcanadianstatus"
								value="conventionRefugee" />Convention Refugee --> Q4 </nobr></td>

							<td colspan="3" align="left" valign="top" class="style76"><nobr>
							<html:radio property="intake.radioYourcanadianstatus"
								value="conventionRefugeeApp" />Convention Refugee applicant -->
							Q4 </nobr></td>
							<td colspan="2" align="left" valign="top" class="style76"><nobr>
							<html:radio property="intake.radioYourcanadianstatus"
								value="sponsoredImmigrant" />Sponsored Immigrant --> Q3 </nobr></td>

						</tr>
						<tr>
							<td colspan="2" align="left" valign="top" class="style76"><nobr>
							<html:radio property="intake.radioYourcanadianstatus"
								value="visitor" />Visitor --> Q4 </nobr></td>
							<td colspan="2" align="left" valign="top" class="style76"><nobr>
							<html:radio property="intake.radioYourcanadianstatus"
								value="ministerPermit" />Minister's Permit --> Q4 </nobr></td>

							<td colspan="5" align="left" valign="top" class="style76"><nobr>
							<html:radio property="intake.radioYourcanadianstatus"
								value="other" />Other (specify) --> Q4 </nobr> &nbsp;&nbsp;&nbsp; <html:text
								property="intake.yourCanadianStatus" /></td>
						</tr>
					</table>
					</td>
				</tr>

				<tr>
					<td colspan="9">
					<table width="100%" border="0">
						<tr>
							<td colspan="7" align="left" class="style76">3. If sponsored
							immigrant - has there been a sponsorship breakdown?</td>
							<td colspan="1" align="left" valign="top" class="style76"><nobr>
							<html:radio property="intake.radioSponsorshipbreakdown"
								value="yes" />Yes </nobr></td>
							<td colspan="1" align="left" valign="top" class="style76"><nobr>
							<html:radio property="intake.radioSponsorshipbreakdown"
								value="no" />No </nobr></td>
						</tr>
						<tr>
							<td colspan="6" class="style76">*If yes, why?
							&nbsp;&nbsp;&nbsp; <html:text
								property="intake.whySponsorshipBreakdown" size="55" /></td>
							<td colspan="3" class="style76">Name of Sponsor: <html:text
								property="intake.sponsorName" size="35" /></td>

						</tr>

					</table>
					</td>
				</tr>

				<tr>
					<td colspan="9" class="style76">4. Do you need a referral to a
					community agency to help you with immigration issues? <html:text
						property="intake.needHelpWithImmigration" size="35" /></td>
				</tr>
				<tr>
					<td colspan="9" align="left" class="style76">
					CommentsOnImmigration:<br>
					<html:textarea property="intake.commentsOnImmigration" cols="75"></html:textarea>
					</td>
				</tr>
			</table>


			<table width="95%" border="1">
				<tr>
					<td colspan="9" align="center" class="style51">12. COMMUNITY
					SUPPORTS</td>
				</tr>

				<tr>
					<td colspan="9" align="left" class="style76">*Counsellor:
					include any information collected in Section 2, Questions 3 & 4</td>
				</tr>

				<tr>
					<td colspan="7" align="left" valign="top" class="style76">1.
					Are you currently involved with any other Community Agencies*?</td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioInvolvedotheragencies"
						value="yes" />Yes </nobr></td>
					<td colspan="1" align="left" valign="top" class="style76"><html:radio
						property="intake.radioInvolvedotheragencies" value="no" />No</td>
				</tr>


				<tr>
					<td colspan="9">
					<table width="100%" border="0">
						<tr>
							<td colspan="7" align="left" class="style76">2a. Name of
							Agency: <html:text property="intake.agency1Name" size="55" /></td>
						</tr>
						<tr>
							<td colspan="6" align="left" valign="top" class="style76"><nobr>
							Contact Person: <html:text property="intake.contact1Name"
								size="35" /> </nobr></td>
							<td colspan="3" align="left" valign="top" class="style76"><nobr>
							Phone # <html:text property="intake.contact1Phone" size="12" />
							</nobr></td>
						</tr>
						<tr>
							<td colspan="6" class="style76">Assistance provided:
							&nbsp;&nbsp;&nbsp; <html:text property="intake.assistProvided1"
								size="55" /></td>
							<td colspan="3" class="style76">Date of last contact with
							Agency (YYYY/MM/DD) <html:text property="intake.dateLastContact1"
								size="35" /></td>
						</tr>
					</table>
					</td>
				</tr>

				<tr>
					<td colspan="9">
					<table width="100%" border="0">
						<tr>
							<td colspan="7" align="left" class="style76">2b. Name of
							Agency: <html:text property="intake.agency2Name" size="55" /></td>
						</tr>
						<tr>
							<td colspan="6" align="left" valign="top" class="style76"><nobr>
							Contact Person: <html:text property="intake.contact2Name"
								size="35" /> </nobr></td>
							<td colspan="3" align="left" valign="top" class="style76"><nobr>
							Phone # <html:text property="intake.contact2Phone" size="12" />
							</nobr></td>
						</tr>
						<tr>
							<td colspan="6" class="style76">Assistance provided:
							&nbsp;&nbsp;&nbsp; <html:text property="intake.assistProvided2"
								size="55" /></td>
							<td colspan="3" class="style76">Date of last contact with
							Agency (YYYY/MM/DD) <html:text property="intake.dateLastContact2"
								size="35" /></td>

						</tr>
					</table>
					</td>
				</tr>

				<tr>
					<td colspan="9">
					<table width="100%" border="0">
						<tr>
							<td colspan="7" align="left" class="style76">2c. Name of
							Agency: <html:text property="intake.agency3Name" size="55" /></td>
						</tr>
						<tr>
							<td colspan="6" align="left" valign="top" class="style76"><nobr>
							Contact Person: <html:text property="intake.contact3Name"
								size="35" /> </nobr></td>
							<td colspan="3" align="left" valign="top" class="style76"><nobr>
							Phone # <html:text property="intake.contact3Phone" size="12" />
							</nobr></td>
						</tr>
						<tr>
							<td colspan="6" class="style76">Assistance provided:
							&nbsp;&nbsp;&nbsp; <html:text property="intake.assistProvided3"
								size="55" /></td>
							<td colspan="3" class="style76">Date of last contact with
							Agency (YYYY/MM/DD) <html:text property="intake.dateLastContact3"
								size="35" /></td>
						</tr>
					</table>
					</td>
				</tr>

				<tr>
					<td colspan="9">
					<table width="100%" border="0">
						<tr>
							<td colspan="7" align="left" class="style76">2d. Name of
							Agency: <html:text property="intake.agency4Name" size="55" /></td>
						</tr>
						<tr>
							<td colspan="6" align="left" valign="top" class="style76"><nobr>
							Contact Person: <html:text property="intake.contact4Name"
								size="35" /> </nobr></td>
							<td colspan="3" align="left" valign="top" class="style76"><nobr>
							Phone # <html:text property="intake.contact4Phone" size="12" />
							</nobr></td>
						</tr>
						<tr>
							<td colspan="6" class="style76">Assistance provided:
							&nbsp;&nbsp;&nbsp; <html:text property="intake.assistProvided4"
								size="55" /></td>
							<td colspan="3" class="style76">Date of last contact with
							Agency (YYYY/MM/DD) <html:text property="intake.dateLastContact4"
								size="35" /></td>
						</tr>
					</table>
					</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="left" class="style76">FOR STAFF USE
					ONLY</td>
				</tr>
			</table>


			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">STAFF RATINGS
					AND IDENTIFIED ISSUES</td>
				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">Based on my own
					observations and from information from other members on my team:</td>
				</tr>

				<tr>
					<td colspan="3" align="left" class="style76">I think this
					person currently has uncontrolled severe mental illness (like
					schizophrenia or manic depression)</td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioMentalillness" value="yes" />Yes
					</nobr></td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioMentalillness" value="uncertain" />Uncertain
					</nobr></td>

					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioMentalillness" value="no" />No </nobr></td>
				</tr>

				<tr>
					<td colspan="3" align="left" class="style76">I am concerned
					that this person has severe problems from uncontrolled drinking</td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioDrinking" value="yes" />Yes </nobr></td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioDrinking" value="uncertain" />Uncertain
					</nobr></td>

					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioDrinking" value="no" />No </nobr></td>
				</tr>

				<tr>
					<td colspan="3" align="left" class="style76">I am concerned
					that this person has severe problems from uncontrolled drug use</td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioDruguse" value="yes" />Yes </nobr></td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioDruguse" value="uncertain" />Uncertain
					</nobr></td>

					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioDruguse" value="no" />No </nobr></td>
				</tr>


				<tr>
					<td colspan="3" align="left" class="style76">I am concerned
					that this person has uncontrolled physical health problems</td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioHealthproblem" value="yes" />Yes
					</nobr></td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioHealthproblem" value="uncertain" />Uncertain
					</nobr></td>

					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioHealthproblem" value="no" />No </nobr></td>
				</tr>

				<tr>
					<td colspan="3" align="left" class="style76">I am concerned
					that this person is handicapped or disabled by severe behaviour
					problems</td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioBehaviorproblem" value="yes" />Yes
					</nobr></td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioBehaviorproblem"
						value="uncertain" />Uncertain </nobr></td>

					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioBehaviorproblem" value="no" />No
					</nobr></td>
				</tr>

				<tr>
					<td colspan="3" align="left" class="style76">I think this
					person will need our (Seaton House's) services for more than 60
					days</td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioNeed60daysseatonservices"
						value="yes" />Yes </nobr></td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioNeed60daysseatonservices"
						value="uncertain" />Uncertain </nobr></td>

					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<html:radio property="intake.radioNeed60daysseatonservices"
						value="no" />No </nobr></td>
				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">Completed by:
					(Sign and Print Name) &nbsp;&nbsp;&nbsp; <html:text
						property="intake.completedBy1" size="55" /></td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">COMPLETED BY</td>
				</tr>
				<tr>
					<td colspan="3" align="left" valign="top" class="style76">
					Completed by: &nbsp;&nbsp;&nbsp; <html:text
						property="intake.completedBy2" size="45" /></td>
					<td width="49%" colspan="3" align="left" valign="top"
						class="style76">Date Assessment Completed
					(YYYY/MM/DD):&nbsp;&nbsp;&nbsp; <html:text
						property="intake.assessCompleteTime" size="25" /> am/pm</td>
				</tr>
				<tr>
					<td colspan="3" align="left" valign="top" class="style76">
					Follow-up dates/appointments: &nbsp;&nbsp;&nbsp; <html:text
						property="intake.followupAppmts" size="25" /></td>
					<td width="49%" colspan="3" align="left" valign="top"
						class="style76">&nbsp;&nbsp;&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3" align="left" valign="top" class="style76">
					&nbsp;&nbsp;&nbsp;</td>
					<td width="49%" colspan="3" align="left" valign="top"
						class="style76">&nbsp;&nbsp;&nbsp;</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="3" align="center" class="style51">TRIAGE-
					REFERRAL TO A SEATON HOUSE PROGRAM</td>
				</tr>
				<tr>
					<td colspan="3" align="left" class="style76">Based on my own
					observations (and from information from other members on my team) <br>
					I believe this client may be appropriate for one of the following
					programs:</td>
				</tr>

				<tr>
					<td width="36%" colspan="1" align="center" class="style76">
					Admission Criteria</td>
					<td width="33%" colspan="1" align="center" class="style76">
					Exclusion Criteria:</td>
					<td width="31%" colspan="1" align="center" class="style76">
					Triage Contact #s:</td>
				</tr>
				<tr>
					<td colspan="1" align="left" valign="top" class="style76"><html:checkbox
						property="intake.cboxPamphletissued" value="Y" /> Emergency Hostel
					Program
					<ul>
						<li>Homeless man</li>
						<li>No other current resources</li>
						<li>No source of income</li>
						<li>Willing to follow case plan</li>
					</ul>
					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					<ul>
						<li>Violence or illicit drug use may result in client being
						barred from the Hostel</li>
					</ul>
					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Triage/Intake: <br>
					(416) 392-5529 <br>
					(416) 338-3196 <br>
					(416) 338-3197<br>
					<br>

					Steering Committee Member:<br>
					Maurice Jefferson (416) 392-5531</td>
				</tr>

				<tr>
					<td colspan="1" align="left" valign="top" class="style76"><html:checkbox
						property="intake.cboxHostel" value="Y" /> Hostel - Fusion of Care
					Team
					<ul>
						<li>Emergency Hostel Program client</li>
						<li>High mental or physical health needs</li>
					</ul>
					</td>
					<td colspan="1" align="left" class="style76">&nbsp;</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Triage/Intake: <br>
					(416) 338-3311<br>
					<br>

					Steering Committee Member:<br>
					Shawn Yoder (416) 392-6706</td>
				</tr>
				<tr>
					<td colspan="1" align="left" valign="top" class="style76"><html:checkbox
						property="intake.cboxHostel" value="Y" /> Rotary Club of Toronto
					Infirmary
					<ul>
						<li>Client is to be discharged from hospital and requires: <br>
						(i) Frequent physician or nursing follow-up and/or <br>
						(ii) Rehabilitation to return to previous level of functioning.</li>
					</ul>
					Client may be admitted to the Infirmary if he exhibits one or more
					of the following health conditions:
					<ul>
						<li>Requires follow-up care (post-operative care and wound
						care)</li>
						<li>Uncontrolled/poorly controlled chronic illness such as
						diabetes, cirrhosis, seizures, CHF and HIV</li>
						<li>Terminal illness that requires palliative care for
						comfort</li>
					</ul>

					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					<ul>
						<li>Client unable to transfer safely (move from bed to chair
						unassisted)</li>
						<li>Client is incontinent and using a wheelchair</li>
					</ul>
					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Triage/Intake: <br>
					(416) 392-5598<br>
					<br>

					Steering Committee Member:<br>
					Karen Smith (416) 392-5598</td>
				</tr>


				<tr>
					<td colspan="1" align="left" valign="top" class="style76"><html:checkbox
						property="intake.cboxAnnexharm" value="Y" /> Annex Harm Reduction
					Program
					<ul>
						<li>Chronic alcohol, substance use (including non-palatable
						substances)</li>
						<li>Severe uncontrolled physical illness</li>
						<li>Severe uncontrolled mental illness</li>
						<li>Severe uncontrolled behaviour problems</li>
						<li>Chronically homeless</li>
					</ul>

					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					<ul>
						<li>Client wishing to maintain abstinence may not do well in
						this environment</li>
					</ul>
					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Triage/Intake: <br>
					(416) 392-5519<br>
					<br>

					Steering Committee Member: <br>
					Ken Mendonca (416) 392-5522</td>
				</tr>

				<tr>
					<td colspan="1" align="left" valign="top" class="style76"><html:checkbox
						property="intake.cboxLongtermprogram" value="Y" /> Long Term
					Program
					<ul>
						<li>Age 65+ - refer direct from Reception</li>
					</ul>
					OR
					<ul>
						<li>Age 50+</li>
						<li>Willing and able to pay rent</li>
					</ul>
					OR Any age and one of the following criteria:
					<ul>
						<li>On pension</li>
						<li>Physically or socially vulnerable</li>
						<li>Mental health issues</li>
						<li>Physical disability</li>
						<li>Short-term disability (non-ambulatory)</li>
					</ul>

					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					<ul>
						<li>Client poses significant risk to residents of long term
						program</li>
						<li>History of aggressive or predatory behaviour</li>
					</ul>
					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Triage/Intake:<br>
					(416) 392-6049 <br>
					(416) 338-3175<br>
					<br>

					Steering Committee Member:<br>
					Mark Headley (416) 392-5543</td>
				</tr>
				<tr>
					<td colspan="1" align="left" valign="top" class="style76"><html:checkbox
						property="intake.cboxBirchmountresidence" value="Y" /> Birchmount
					Residence
					<ul>
						<li>Age 55+ (men aged 50-55 considered)</li>
						<li>Chronic health issues</li>
						<li>Able to carry out the activities of daily living without
						staff assistance</li>
						<li>Able to climb stairs</li>
						<li>Able to behave in a reliable, predictable and respectful
						manner in the community</li>
						<li>Stable mental health</li>
						<li>Able to act appropriately and responsibly if consuming
						alcohol and willing to cooperate with a harm reduction approach</li>
						<li>Willing to take medication as prescribed by the attending
						physician and cooperative with medical and Nursing plan and attend
						medical appointments and procedures as required.</li>
						<li>Willing to be financially responsible and pay rent</li>
						<li>Has legal status in Canada</li>
						<li>Willing to cooperate with plan for future housing or
						appropriate care facility</li>
					</ul>

					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					<ul>
						<li>Recent history of incarceration, criminal activity,
						violence</li>
						<li>Requires bedside nursing or personal care</li>
						<li>Requires oxygen, IV or intrusive medical procedures of
						equipment</li>
						<li>Incontinence issues</li>
						<li>Danger to self or others</li>
						<li>Abuse of medications or street drugs</li>
					</ul>
					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Triage/Intake: <br>
					(416) 392-6167<br>
					<br>

					Steering Committee Member:<br>
					Carla O'Brien (416) 392-5543<br>
					<br>
					Referrals to Birchmount are made by Long Term program.<br>
					<br>

					N.B. Client must be resident of Seaton House Long Term program for
					more than 30 days prior to admission (some exceptions considered).
					</td>
				</tr>

				<tr>
					<td colspan="1" align="left" valign="top" class="style76"><html:checkbox
						property="intake.cboxOneillhouse" value="Y" /> O'Neill House
					<ul>
						<li>Severe problems related to street drug use (Crack
						cocaine)</li>
						<li>Client does not want treatment</li>
						<li>Barred from Hostel due to drug use</li>
						<li>2 or more incident reports for drug use</li>
					</ul>

					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Triage/Intake <br>
					(416) 392-5407<br>
					<br>

					Steering Committee Member:<br>
					Tom Fulgosi (416) 392-5436</td>
				</tr>

				<tr>
					<td colspan="1" align="left" valign="top" class="style76"><html:checkbox
						property="intake.cboxFortyork" value="Y" /> Fort York Residence
					<ul>
						<li>Willing and able to work</li>
						<li>If employed: willing to save 60% of income</li>
						<li>Willing to do volunteer work</li>
						<li>Has ID (hard copy)</li>
						<li>Has up-to-date resume</li>
						<li>Willing and able to follow case plan</li>
					</ul>

					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Clients with substance use issues will be required to complete a
					Rehab program prior to admission to Fort York Residence.</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Triage/Intake:<br>
					Mark Horne: # TBA*<br>
					<br>

					Steering Committee Member#: TBA*</td>
				</tr>
				<tr>
					<td colspan="1" align="left" valign="top" class="style76"><html:checkbox
						property="intake.cboxDownsviewdells" value="Y" /> Downsview Dells
					<ul>
						<li>Chemically dependent now wishing abstinence</li>
						<li>Willing and able to complete 30-day treatment at Humber
						River Regional Hospital</li>
					</ul>

					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Disruptive behaviour</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Triage/Intake: <br>
					(416) 392-5452<br>
					<br>

					Steering Committee:<br>
					MemberDon Inglis (416) 392-5452</td>
				</tr>
			</table>

			<table width="95%" border="0">
				<tr>
					<td class="style76">* Phone numbers for Fort York Residence
					will be available shortly.</td>
				</tr>
			</table>

			<table width="95%" border="0">
				<tr>
					<td align="center" class="style76"><input type="submit"
						value="Save" onclick="javascript:return onSave();" /> <input
						type="button" value="Print" onclick="javascript:return onPrint();" />
					</td>
				</tr>
			</table>

			</html:form>
</body>
</head>
</html:html>
