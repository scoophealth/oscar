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

<%@ page import="java.util.*, oscar.form.*, oscar.OscarProperties"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Laboratory Requisition</title>
<html:base />
<link rel="stylesheet" type="text/css" href="LabReqPrintStyle.css">
<link rel="stylesheet" type="text/css" media="print" href="print.css">
</head>

<%
    Properties props = new Properties();
    StringBuffer temp = new StringBuffer("");
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	    temp = new StringBuffer(e.nextElement().toString());
		props.setProperty(temp.toString(), request.getParameter(temp.toString()));
    }
/*
    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getPrintRecord(demoNo, formId);
*/
    OscarProperties oscarProps = OscarProperties.getInstance();
%>

<script type="text/javascript" language="Javascript">
    function onPrint() {
        window.print();
    }
    function onCancel() {
        window.close();
    }
</script>

<body>


<!-- class="TableWithBorder" -->
<table class="Head" class="hidePrint">
	<tr>
		<td align="left"><input type="button" value="Cancel"
			onclick="javascript:return onCancel();" /> <input type="button"
			value="Print" onclick="javascript:return onPrint();" /></td>
	</tr>
</table>
<td>
<table class="outerTable">
	<tr>
		<td>
		<table width="100%">
			<tr>
				<td class="title" colspan="3" nowrap="true">LABORATORY
				REQUISITION</td>
			</tr>
			<tr>
				<td colspan="3" nowrap="true">Requisitioning
				Physician/Practioner:<br>
				<input type="hidden" style="width: 100%" name="provName"
					value="<%=props.getProperty("provName", "")%>" /> <%=props.getProperty("reqProvName", "").compareTo("")==0?props.getProperty("provName", ""):props.getProperty("reqProvName", "")%>&nbsp;<br>
				<%-- Dr. Hunter wants the form to say "Physician" instead of "Family Physician".  This is a quick and dirty hack to make it work.  This
     should really be rewritten more elegantly at some later point in time. --%>
				<br><%=oscarProps.getProperty("clinic_no", "").startsWith("1022")?"Physician:":"Family Physician:"%>
				<br><%=props.getProperty("provName", "")==null?"":props.getProperty("provName", "")%>&nbsp;<br>
				<input type="hidden" style="width: 100%" name="clinicAddress"
					value="<%=props.getProperty("clinicAddress", "")%>" /> <%=props.getProperty("clinicAddress", "")%>&nbsp;<br>
				<input type="hidden" style="width: 100%" name="clinicCity"
					value="<%=props.getProperty("clinicCity", "")%>" /> <%=props.getProperty("clinicCity", "")%>&nbsp;<br>
				<input type="hidden" style="width: 100%" name="clinicPC"
					value="<%=props.getProperty("clinicPC", "")%>" /> <%=props.getProperty("clinicPC", "")%>&nbsp;<br>
				</td>
			</tr>
			<tr>
				<td>
				<table width="100%" border="1"
					style="border-right: 0; border-bottom: 0;" cellspacing="0">
					<tr>
						<td colspan="3">Physician/Practitioner Number<br>
						<input type="hidden" name="practitionerNo"
							value="<%=props.getProperty("practitionerNo", "")%>" /> <%=props.getProperty("practitionerNo", "")%>&nbsp;
						</td>
					</tr>
					<tr>
						<td>
						<table>
							<tr>
								<td nowrap="true" valign="top">Check one:</td>
								<td valign="top"><input type="checkbox" name="ohip"
									<%=props.getProperty("ohip", "").equals("")?"":"checked"%> /><br>
								<input type="checkbox" name="thirdParty"
									<%=props.getProperty("thirdParty", "").equals("")?"":"checked"%> /><br>
								<input type="checkbox" name="wcb"
									<%=props.getProperty("wcb", "").equals("")?"":"checked"%> /><br>
								</td>
								<td>OHIP/Insured<br>
								Third Party/ Uninsured<br>
								WCB</td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td colspan="3">Additional Clinical Information<br>
						<textarea name="aci" style="width: 100%; height: 59px;"><%=props.getProperty("aci", "")%></textarea>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
		<td width="100%">
		<table width="100%" cellspacing="0" border="1"
			style="border-bottom: 0;">
			<tr>
				<td class="lab" valign="top">Laboratory Number <br>
				<br>
				<br>
				<br>
				<br>
				</td>
				<td class="lab" colspan="2" rowspan="2" valign="top" width="65%">
				Laboratory Name and Address</td>
			</tr>
			<tr>
				<td class="lab" valign="top">Total Fee <br>
				<br>
				<br>
				<br>
				<br>
				</td>
			</tr>
			<tr>
				<td class="lab" valign="top">Laboratory Accounting Number<br>
				<br>
				</td>
				<td class="lab" valign="top">Service Date (yyyy/mm/dd)</td>
				<td class="lab" valign="top">Ref. Lab.</td>
			</tr>
			<tr>
				<td colspan="3">
				<table width="100%">
					<tr>
						<td nowrap="true"><input type="hidden" style="width: 90%"
							name="patientName"
							value="<%=props.getProperty("patientName", "")%>" /> <%=props.getProperty("patientName", "")%>&nbsp;
						</td>
						<td>Health Number:</td>
						<td><input type="hidden" name="healthNumber"
							value="<%=props.getProperty("healthNumber", "")%>" /> <%=props.getProperty("healthNumber", "")%>&nbsp;
						</td>
						<td>Province:</td>
						<td><input type="hidden" name="province"
							value="<%=props.getProperty("province", "")%>" /> <%=props.getProperty("province", "")%>&nbsp;
						</td>
					</tr>
					<tr>
						<td nowrap="true"><input type="hidden" style="width: 90%"
							name="patientAddress"
							value="<%=props.getProperty("patientAddress", "")%>" /> <%=props.getProperty("patientAddress", "")%>
						</td>
						<td>Version:</td>
						<td><input type="hidden" name="version"
							value="<%=props.getProperty("version", "")%>" /> <%=props.getProperty("version", "")%>
						</td>
						<td>Other Registration Number:</td>
						<td><input type="text" name="orn" size="12"
							value="<%=props.getProperty("orn", "")%>" /></td>
					</tr>
					<td><input type="hidden" style="width: 90%" name="patientCity"
						value="<%=props.getProperty("patientCity", "")%>" /> <%=props.getProperty("patientCity", "")%>
					</td>
					<td>Date of Birth:</td>
					<td><input type="hidden" name="birthDate"
						value="<%=props.getProperty("birthDate", "")%>" /> <%=props.getProperty("birthDate", "")%>
					</td>
					<td>Phone Number:</td>
					<td><input type="hidden" name="phoneNumber"
						value="<%=props.getProperty("phoneNumber", "")%>" /> <%=props.getProperty("phoneNumber", "")%>
					</td>
					</tr>
					<td><input type="hidden" style="width: 90%" name="patientPC"
						value="<%=props.getProperty("patientPC", "")%>" /> <%=props.getProperty("patientPC", "")%>
					</td>
					<td>Payment Program:</td>
					<td><input type="text" name="paymentProgram" size="10"
						value="<%=props.getProperty("paymentProgram", "")%>" /></td>
					<td>Sex:</td>
					<td><input type="hidden" name="sex"
						value="<%=props.getProperty("sex", "")%>" /> <%=props.getProperty("sex", "")%>
					</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td colspan="2">
		<table width="100%" border="1" cellspacing="0">
			<tr>
				<td width="33%">
				<table border="1"
					style="border-top: 0; border-left: 0; border-right: 0; border-bottom: 0;"
					cellspacing="0">
					<tr class="testType">
						<td style="border-right: 0;">&nbsp;</td>
						<td style="border-left: 0; width: 100%;"><a>Biochemistry</a>
						</td>
						<td style="font-size: 5pt;">LAB CODE</td>
						<td style="font-size: 5pt;">FEE CODE</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="b_glucose"
							<%=props.getProperty("b_glucose", "").equals("")?"":"checked"%> /></td>
						<td>Glucose</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="b_creatine"
							<%=props.getProperty("b_creatine", "").equals("")?"":"checked"%> /></td>
						<td>Creatinine</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="b_uricAcid"
							<%=props.getProperty("b_uricAcid", "").equals("")?"":"checked"%> /></td>
						<td>Uric Acid</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="b_sodium"
							<%=props.getProperty("b_sodium", "").equals("")?"":"checked"%> /></td>
						<td>Sodium</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="b_potassium"
							<%=props.getProperty("b_potassium", "").equals("")?"":"checked"%> /></td>
						<td>Potassium</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="b_chloride"
							<%=props.getProperty("b_chloride", "").equals("")?"":"checked"%> /></td>
						<td>Chloride</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="b_ast"
							<%=props.getProperty("b_ast", "").equals("")?"":"checked"%> /></td>
						<td>AST (SGOT)</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="b_alkPhosphate"
							<%=props.getProperty("b_alkPhosphate", "").equals("")?"":"checked"%> /></td>
						<td>Alk. Phosphate</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="b_bilirubin"
							<%=props.getProperty("b_bilirubin", "").equals("")?"":"checked"%> /></td>
						<td>Bilirubin</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="b_cholesterol"
							<%=props.getProperty("b_cholesterol", "").equals("")?"":"checked"%> /></td>
						<td>Cholesterol</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="b_triglyceride"
							<%=props.getProperty("b_triglyceride", "").equals("")?"":"checked"%> /></td>
						<td>Triglyceride</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="b_urinalysis"
							<%=props.getProperty("b_urinalysis", "").equals("")?"":"checked"%> /></td>
						<td>Urinalysis (chemical)</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td colspan="4">
						<table width="100%">
							<tr>
								<td colspan="2">Viral Hepatitis (check <u>one</u> only)</td>
							</tr>
							<tr>
								<td><input type="checkbox" name="v_acuteHepatitis"
									<%=props.getProperty("v_acuteHepatitis", "").equals("")?"":"checked"%> /></td>
								<td>Acute hepatitis</td>
							</tr>
							<tr>
								<td><input type="checkbox" name="v_chronicHepatitis"
									<%=props.getProperty("v_chronicHepatitis", "").equals("")?"":"checked"%> /></td>
								<td>Chronic hepatitis</td>
							</tr>
							<tr>
								<td><input type="checkbox" name="v_immune"
									<%=props.getProperty("v_immune", "").equals("")?"":"checked"%> /></td>
								<td>Immune status / prev. exposure</td>
							<tr>
								<td>Specify:</td>
								<td>Hepatitis A</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><%=props.getProperty("v_hepA", "")%>&nbsp;</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td>Hepatitis B</td>
							</tr>
							</tr>
							<td>&nbsp;</td>
							<td><%=props.getProperty("v_hepB", "")%>&nbsp;</td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td colspan="4">
						<table>
							<tr>
								<td colspan="2">"I certify the tests ordered are not for
								registered in or out patients of a hospital".<br>
								<br>
								</td>
							</tr>
							<tr>
								<td>Signature</td>
								<td>Date</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><%=props.getProperty("formDate", "")%>&nbsp;</td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
				<td width="33%">
				<table border="1"
					style="border-top: 0; border-left: 0; border-right: 0; border-bottom: 0;"
					cellspacing="0">
					<tr class="testType">
						<td style="border-right: 0;">&nbsp;</td>
						<td style="border-left: 0; width: 100%;"><a>Hematology</a></td>
						<td style="font-size: 5pt;">LAB CODE</td>
						<td style="font-size: 5pt;">FEE CODE</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="h_bloodFilmExam"
							<%=props.getProperty("h_bloodFilmExam", "").equals("")?"":"checked"%> /></td>
						<td nowrap="true">Blood Film Exam</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="h_hemoglobin"
							<%=props.getProperty("h_hemoglobin", "").equals("")?"":"checked"%> /></td>
						<td nowrap="true">Hemoglobin</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="h_wcbCount"
							<%=props.getProperty("h_wcbCount", "").equals("")?"":"checked"%> /></td>
						<td nowrap="true">W.C.B. count</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="h_hematocrit"
							<%=props.getProperty("h_hematocrit", "").equals("")?"":"checked"%> /></td>
						<td nowrap="true">Hematocrit</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="h_prothrombTime"
							<%=props.getProperty("h_prothrombTime", "").equals("")?"":"checked"%> /></td>
						<td nowrap="true">Prothromb. time</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="h_otherC"
							<%=props.getProperty("h_otherC", "").equals("")?"":"checked"%> /></td>
						<td style="padding-bottom: 1px"><%=props.getProperty("h_other", "")%>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr class="testType">
						<td style="border-right: 0;">&nbsp;</td>
						<td colspan="3" style="border-left: 0; width: 100%;"><a>Immunology</a>
						</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="i_pregnancyTest"
							<%=props.getProperty("i_pregnancyTest", "").equals("")?"":"checked"%> /></td>
						<td nowrap="true">Pregnancy Test</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="i_heterophile"
							<%=props.getProperty("i_heterophile", "").equals("")?"":"checked"%> /></td>
						<td>Heterophile antibodies screen</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="i_rubella"
							<%=props.getProperty("i_rubella", "").equals("")?"":"checked"%> /></td>
						<td nowrap="true">Rubella</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="i_prenatal"
							<%=props.getProperty("i_prenatal", "").equals("")?"":"checked"%> /></td>
						<td>Prenatal: <small>ABO, RhD, anitbody screen (titre
						and ident. if positive</small></td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="i_repeatPrenatal"
							<%=props.getProperty("i_repeatPrenatal", "").equals("")?"":"checked"%> /></td>
						<td>Repeat Prenatal antibodies</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="i_prenatalHepatitisB"
							<%=props.getProperty("i_prenatalHepatitisB", "").equals("")?"":"checked"%> /></td>
						<td nowrap="true">Prenatal Hepatitis B</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="i_vdrl"
							<%=props.getProperty("i_vdrl", "").equals("")?"":"checked"%> /></td>
						<td nowrap="true">VDRL</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="i_otherC"
							<%=props.getProperty("i_otherC", "").equals("")?"":"checked"%> /></td>
						<td style="padding-bottom: 1px"><%=props.getProperty("i_other", "")%>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr class="testType">
						<td style="border-right: 0;">&nbsp;</td>
						<td colspan="3"
							style="border-left: 0; width: 100%; padding-bottom: 1px; padding-top: 1px;">
						<a>Microbiology</a> Sensitivities if warranted</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="m_cervicalVaginal"
							<%=props.getProperty("m_cervicalVaginal", "").equals("")?"":"checked"%> /></td>
						<td nowrap="true">Cervical, vaginal</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="m_sputum"
							<%=props.getProperty("m_sputum", "").equals("")?"":"checked"%> /></td>
						<td nowrap="true">Sputum</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="m_throat"
							<%=props.getProperty("m_throat", "").equals("")?"":"checked"%> /></td>
						<td nowrap="true">Throat</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="m_urine"
							<%=props.getProperty("m_urine", "").equals("")?"":"checked"%> /></td>
						<td nowrap="true">Urine</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td><input type="checkbox" name="m_stoolCulture"
							<%=props.getProperty("m_stoolCulture", "").equals("")?"":"checked"%> /></td>
						<td nowrap="true">Stool culture</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td valign="top"><input type="checkbox" name="m_otherSwabs"
							<%=props.getProperty("m_otherSwabs", "").equals("")?"":"checked"%> /></td>
						<td nowrap="true" style="padding-bottom: 2px"><%=props.getProperty("m_other", "")%>&nbsp;
						</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
				</table>
				</td>
				<td width="33%">
				<table border="1"
					style="border-top: 0; border-left: 0; border-right: 0; border-bottom: 0;"
					cellspacing="0">
					<tr class="testType">
						<td><a>Other test, one per line</a> (please use terminology
						of the Schedule of Benefits)</td>
						<td style="font-size: 5pt;">LAB CODE</td>
						<td style="font-size: 5pt;">FEE CODE</td>
						<td style="font-size: 5pt;">NO OF SERV</td>
					</tr>
					<tr>
						<td rowspan="9"><textarea name="otherTest"
							style="width: 100%; height: 159px; overflow: auto;"><%=props.getProperty("otherTest", "")%></textarea>
						</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr class="testType">
						<td colspan="4"><a>Laboratory use only</a></td>
					</tr>
					<tr>
						<td>Documentation Fee</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>Gyn. Specimen (Pap Smear)</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
					</tr>
					<tr>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
					</tr>
					<tr>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
					</tr>
					<tr>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
					</tr>
					<tr>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
					</tr>
					<tr>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
					</tr>
					<tr>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
					</tr>
					<tr>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
					</tr>
					<tr>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
					</tr>
					<tr>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
					</tr>
					<tr>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
					</tr>
					<tr>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
					</tr>
					<tr>
						<td style="padding-bottom: 1px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
						<td style="padding-bottom: 2px; padding-top: 2px;">&nbsp;</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<table class="Head" class="hidePrint">
	<tr>
		<td align="left"><input type="button" value="Cancel"
			onclick="javascript:return onCancel();" /> <input type="button"
			value="Print" onclick="javascript:return onPrint();" /></td>
	</tr>
</table>
</body>
</html:html>
