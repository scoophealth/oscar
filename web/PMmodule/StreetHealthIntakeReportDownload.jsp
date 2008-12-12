<%@ taglib uri="/WEB-INF/streethealth.tld" prefix="sh"%>
<%
	response.setContentType("text/csv");
	response.addHeader("Content-Disposition", "attachment;filename=StreetHealthReport.csv");
%>
<sh:report_element num="7" format="csv"
	question="Total Service Recipients"
	answerProps="total_service_recipient"></sh:report_element>
<sh:report_element num="8" format="csv" question="Gender"
	answerProps="gender"></sh:report_element>
<sh:report_element num="9" format="csv" question="Age" answerProps="age"></sh:report_element>
<sh:report_element num="10" format="csv"
	question="Service Recipient Location"
	answerProps="service_recipient_location"></sh:report_element>
<sh:report_element num="11" format="csv" question="Aboriginal Origin"
	answerProps="aboriginal"></sh:report_element>
<sh:report_element num="12" format="csv"
	question="Service Recipient Preferred Language" answerProps="language"></sh:report_element>
<sh:report_element num="13" format="csv"
	question="Baseline Legal Status" answerProps="blegal"></sh:report_element>
<sh:report_element num="14" format="csv" question="Current Legal Status"
	answerProps="blegal"></sh:report_element>
<sh:report_element num="15" format="csv"
	question="Community Treatment Orders" answerProps="cto"></sh:report_element>
<sh:report_element num="16" format="csv"
	question="Diagnostic Categories" answerProps="diag"></sh:report_element>
<sh:report_element num="16a" format="csv"
	question="Other Illness Information" answerProps="illness"></sh:report_element>
<sh:report_element num="17" format="csv" question="Presenting Issues"
	answerProps="presenting_issues"></sh:report_element>
<sh:report_element num="18" format="csv" question="Source of Referral"
	answerProps="referral"></sh:report_element>
<sh:report_element num="19" format="csv" question="Exit Disposition"
	answerProps="exit"></sh:report_element>
<sh:report_element num="20" format="csv"
	question="Baseline Psychiatric Hospitalizations"
	answerProps="psych_hospitalizations"></sh:report_element>
<sh:report_element num="21" format="csv"
	question="Current Psychiatric Hospitalizations"
	answerProps="cpsych_hospitalizations"></sh:report_element>
<sh:report_element num="22" format="csv"
	question="Baseline Living Arrangement" answerProps="living_arrangement"></sh:report_element>
<sh:report_element num="23" format="csv"
	question="Current Living Arrangement" answerProps="living_arrangement"></sh:report_element>
<sh:report_element num="24" format="csv"
	question="Baseline Residence Type" answerProps="residence_type"></sh:report_element>
<sh:report_element num="24a" format="csv"
	question="Baseline Residence Status" answerProps="residence_status"></sh:report_element>
<sh:report_element num="25" format="csv"
	question="Current Residence Type" answerProps="residence_type"></sh:report_element>
<sh:report_element num="25a" format="csv"
	question="Current Residence Status" answerProps="residence_status"></sh:report_element>
<sh:report_element num="26" format="csv"
	question="Baseline Employment Status" answerProps="employment_status"></sh:report_element>
<sh:report_element num="27" format="csv"
	question="Current Employment Status" answerProps="employment_status"></sh:report_element>
<sh:report_element num="28" format="csv"
	question="Baseline Educational Status" answerProps="educational_status"></sh:report_element>
<sh:report_element num="29" format="csv"
	question="Current Educational Status" answerProps="educational_status"></sh:report_element>
<sh:report_element num="29a" format="csv"
	question="Highest Level of Education" answerProps="level_education"></sh:report_element>
<sh:report_element num="30" format="csv"
	question="Baseline Primary Income Source" answerProps="income_source"></sh:report_element>
<sh:report_element num="31" format="csv"
	question="Current Primary Income Source" answerProps="income_source"></sh:report_element>
