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
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="oscar.oscarBilling.ca.bc.data.*,oscar.*"%>
<%@page	import="java.util.*,java.io.*,oscar.oscarBilling.ca.bc.MSP.*,oscar.oscarBilling.ca.bc.administration.*,java.sql.*"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.ClinicLocationDao" %>
<%@page import="org.oscarehr.common.model.ClinicLocation" %>
<%@ page import="org.oscarehr.common.model.DiagnosticCode" %>
<%@ page import="org.oscarehr.common.dao.DiagnosticCodeDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.billing.CA.model.BillingDetail" %>
<%@ page import="org.oscarehr.billing.CA.dao.BillingDetailDao" %>
<%@ page import="oscar.entities.Billingmaster" %>
<%@ page import="oscar.oscarBilling.ca.bc.data.BillingmasterDAO" %>
<%@ page import="org.oscarehr.billing.CA.BC.model.TeleplanC12" %>
<%@ page import="org.oscarehr.billing.CA.BC.dao.TeleplanC12Dao" %>
<%
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	DiagnosticCodeDao diagnosticCodeDao = SpringUtils.getBean(DiagnosticCodeDao.class);
	ClinicLocationDao clinicLocationDao = (ClinicLocationDao)SpringUtils.getBean("clinicLocationDao");
	BillingDetailDao billingDetailDao = SpringUtils.getBean(BillingDetailDao.class);
	BillingmasterDAO billingMasterDao = SpringUtils.getBean(BillingmasterDAO.class);
	TeleplanC12Dao teleplanC12Dao = SpringUtils.getBean(TeleplanC12Dao.class);
%>


<%
  List<Object[]> results = billingMasterDao.select_user_bill_report_wcb(Integer.parseInt(request.getParameter("billing_no")));
  TeleplanCorrectionFormWCB form = new TeleplanCorrectionFormWCB(results);
  Properties codes = new MspErrorCodes();
  BillingFormData billform = new BillingFormData();
  String billRegion = OscarProperties.getInstance().getProperty("billRegion","BC");
  BillingFormData.BillingVisit[] billvisit = billform.getVisitType(billRegion);
  request.setAttribute("billvisit",billvisit);
%>
<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<title>OSCAR oscarBilling - Correction</title>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<link rel="stylesheet" href="../../../share/css/oscar.css">
<link rel="stylesheet" type="text/css" media="all"
	href="../../../share/calendar/calendar.css" title="win2k-cold-1" />
<script src="../../../share/javascript/Oscar.js"></script>
<script src="../../../share/calendar/calendar.js"></script>
<script
	src="../../../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"
	type="text/javascript"></script>
<script src="../../../share/calendar/calendar-setup.js"
	type="text/javascript"></script>

<script type="text/javascript">
function popFeeItemList(form,field){

     var width = 575;

     var height = 400;

     var str = document.forms[form].elements[field].value;

     var url = "support/billingfeeitem.jsp?form=" +form+ "&field="+field+"&searchStr=" +str;

     var windowName = field;

     popup(height,width,url,windowName);

  }



  function popICD9List(form,field){

     var width = 575;

     var height = 400;

     var str = document.forms[form].elements[field].value;

     var url = "support/icd9.jsp?form=" +form+ "&field="+field+"&searchStr=" +str;

     var windowName = field;

     popup(height,width,url,windowName);

  }



  function popBodyPartList(form,field){

     var width = 650;

     var height = 400;

     var str = document.forms[form].elements[field].value;

     var url = "support/bodypart.jsp?form=" +form+ "&field="+field+"&searchStr=" +str;

     var windowName = field;

     popup(height,width,url,windowName);

  }



  function popNOIList(form,field){

     var width = 800;

     var height = 400;

     var str = document.forms[form].elements[field].value;

     var url = "support/natureinjury.jsp?form=" +form+ "&field="+field+"&searchStr=" +str;

     var windowName = field;

     popup(height,width,url,windowName);

  }



</script>
</head>
<body>
<html:form action="billing/CA/BC/billingTeleplanCorrectionWCB">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr bgcolor="#000000">
			<td height="40" width="10%"></td>
			<td width="90%" align="left">
			<p><font face="Verdana, Arial, Helvetica, sans-serif"
				color="#FFFFFF"> <b> <font
				face="Arial, Helvetica, sans-serif" size="4"> oscar <font
				size="3">Billing - Correction</font> </font> </b> </font></p>
			</td>
		</tr>
	</table>
    Form Needed
    <input type="checkbox" value="1" name="formNeeded"
		onClick="isformNeeded();" />
	<table width="100%">
		<tr>
			<td colspan="2" class="SectionHead"><a href=#
				onClick="popup(700,900,'../../../demographic/demographiccontrol.jsp?demographic_no=<%=form.getDemographicNumber()%>&displaymode=edit&dboperation=search_detail','<bean:message key="oscarEncounter.Index.popupPage2Window"/>');return false;"
				title="<bean:message key="provider.appointmentProviderAdminDay.msgMasterFile"/>">Patient
			Information</a> <html:hidden property="id" value="<%=form.getId()%>" /> <html:hidden
				property="demographicNumber"
				value="<%=form.getDemographicNumber()%>" /></td>
		</tr>
		<tr>
			<td width="50%" align="left" valign="top">
			<table width="100%">
				<tr>
					<td class="FormLabel">First Name:</td>
					<td><html:text property="firstName"
						value="<%=form.getFirstName()%>" /></td>
				</tr>
				<tr>
					<td class="FormLabel">Middle Name:</td>
					<td><html:text property="w_mname"
						value="<%=form.getW_mname()%>" /></td>
				</tr>
				<tr>
					<td class="FormLabel">Last Name:</td>
					<td><html:text property="lastName"
						value="<%=form.getLastName()%>" /></td>
				</tr>
				<tr>
					<td class="FormLabel">Date Of Birth:</td>
					<td><html:text property="yearOfBirth"
						value="<%=form.getYearOfBirth()%>" maxlength="4" size="4" /> <html:text
						property="monthOfBirth" value="<%=form.getMonthOfBirth()%>"
						maxlength="2" size="2" /> <html:text property="dayOfBirth"
						value="<%=form.getDayOfBirth()%>" maxlength="2" size="2" /> Age:(
					<%=form.getAge()%> )</td>
				</tr>
				<tr>
					<td class="FormLabel">HIN (PHN):</td>
					<td><html:text property="hin" value="<%=form.getHin()%>" /></td>
				</tr>
				<tr>
					<td class="FormLabel">Gender:</td>
					<td><html:text readonly="true" property="w_gender"
						value="<%=form.getW_gender()%>" /></td>
				</tr>
			</table>
			</td>
			<td width="50%" align="left" valign="top">
			<table width="100%">
				<tr>
					<td width="100" class="FormLabel">Area:</td>
					<td><html:text property="w_area" value="<%=form.getW_area()%>" />
					</td>
				</tr>
				<tr>
					<td width="100" class="FormLabel">Phone:</td>
					<td><html:text property="w_phone"
						value="<%=form.getW_phone()%>" /></td>
				</tr>
				<tr>
					<td width="100" class="FormLabel">Address:</td>
					<td><html:text property="address"
						value="<%=form.getAddress()%>" /></td>
				</tr>
				<tr>
					<td width="100" class="FormLabel">City:</td>
					<td><html:text property="city" value="<%=form.getCity()%>" />
					</td>
				</tr>
				<tr>
					<td class="FormLabel">Province:</td>
					<td><html:text property="province"
						value="<%=form.getProvince()%>" /></td>
				</tr>
				<tr>
					<td width="100" class="FormLabel">Postal:</td>
					<td><html:text property="postal" value="<%=form.getPostal()%>" />
					</td>
				</tr>
			</table>
			</td>
			<!-- -->
		</tr>
	</table>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr valign="top">
			<td width="50%">
			<table width="100%">
				<tr>
					<td colspan="2" class="SectionHead">Employer Information</td>
				</tr>
				<tr>
					<td width="50%" align="left" valign="top">
					<table width="100%">
						<tr>
							<td class="FormLabel">Name:</td>
							<td><html:text property="w_empname"
								value="<%=form.getW_empname()%>" /></td>
						</tr>
						<tr>
							<td class="FormLabel">Area:</td>
							<td><html:text property="w_emparea"
								value="<%=form.getW_emparea()%>" /></td>
						</tr>
						<tr>
							<td class="FormLabel">Phone:</td>
							<td><html:text property="w_empphone"
								value="<%=form.getW_empphone()%>" /></td>
						</tr>
					</table>
					</td>
					<td width="50%" align="left" valign="top">
					<table width="100%">
						<tr>
							<td width="175" class="FormLabel">Operating Address:</td>
							<td><html:text property="w_opaddress"
								value="<%=form.getW_opaddress()%>" /></td>
						</tr>
						<tr>
							<td width="175" class="FormLabel">Operating City:</td>
							<td><html:text property="w_opcity"
								value="<%=form.getW_opcity()%>" /></td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
			<td>
			<table width="100%">
				<tr>
					<td width="100%" colspan="4" class="SectionHead">Assessment</td>
				</tr>
				<tr>
					<td>Capability:</td>
					<td><html:select name="w_capability"
						value="<%=form.getW_capability()%>" property="w_capability">
						<html:option value="Y">Yes</html:option>
						<html:option value="N">No</html:option>
					</html:select></td>
					<td>Rehab:</td>
					<td><html:select name="w_rehab" value="<%=form.getW_rehab()%>"
						property="w_rehab">
						<html:option value="Y">Yes</html:option>
						<html:option value="N">No</html:option>
					</html:select></td>
				</tr>
				<tr>
					<td>Rehab Type:</td>
					<td><html:select name="w_rehabtype"
						value="<%=form.getW_rehabtype()%>" property="w_rehabtype">
						<html:option value="C">Work Conditioning</html:option>
						<html:option value="O">Other</html:option>
					</html:select></td>
					<td>To Follow:</td>
					<td><html:select name="w_tofollow"
						value="<%=form.getW_tofollow()%>" property="w_tofollow">
						<html:option value="Y">Yes</html:option>
						<html:option value="N">No</html:option>
					</html:select></td>
				</tr>


				</tr>
				<td>Advisor:</td>
				<td><html:select name="w_wcbadvisor"
					value="<%=form.getW_wcbadvisor()%>" property="w_wcbadvisor">
					<html:option value="Y">Yes</html:option>
					<html:option value="N">No</html:option>
				</html:select></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
	<table width="100%">
		<tr>
			<td colspan="2" class="SectionHead">Claim Information ( <%=form.getDataSequenceNo()%>
			)</td>
		</tr>
		<tr>
			<td width="50%" align="left" valign="top">
			<table width="100%">
				<tr>
					<td class="FormLabel">WCB Claim No:</td>
					<td><html:text property="w_wcbno"
						value="<%=form.getW_wcbno()%>" /></td>
				</tr>
				<tr>
					<td class="FormLabel">Billing Physician</td>
					<td><html:select style="font-size:80%;" property="providerNo"
						value="<%=form.getProviderNo()%>">
						<%
                  String proFirst = "", proLast = "", proOHIP = "", proNo = "";
					for(Provider p:providerDao.getActiveProviders()) {
                  		if(p.getOhipNo() != null && !p.getOhipNo().isEmpty()) {
                    proFirst =p.getFirstName();
                    proLast = p.getLastName();
                    proOHIP = p.getProviderNo();
                %>
						<html:option value="<%=proOHIP%>"><%=proOHIP%>                    |
<%=proLast%>                    ,
<%=proFirst%>
						</html:option>
						<%}    }            %>
					</html:select></td>
				</tr>
				<tr>
					<td class="FormLabel">Practioner Num:</td>
					<td><%=form.getW_pracno()%></td>
				</tr>
				<tr>
					<td class="FormLabel">Payee Num:</td>
					<td><%=form.getW_payeeno()%></td>
				</tr>
				<tr>
					<td class="FormLabel">Regular Physician:</td>
					<td><html:select name="w_rphysician"
						value="<%=form.getW_rphysician()%>" property="w_rphysician">
						<html:option value="Y">Yes</html:option>
						<html:option value="N">No</html:option>
					</html:select></td>
				</tr>
				<tr>
					<td class="FormLabel">Duration:</td>
					<td><html:select name="w_duration"
						value="<%=form.getW_duration()%>" property="w_duration">
						<html:option value="1">0-6 months</html:option>
						<html:option value="2">7-12 months</html:option>
						<html:option value="9">                    &gt;
                    12 months</html:option>
					</html:select></td>
				</tr>
				<tr>
					<td class="FormLabel">Billing Unit:</td>
					<td><html:text property="billingUnit"
						value="<%=form.getBillingUnit()%>" /></td>
				</tr>
				<!--<tr>

					<td class="FormLabel">Billing Code:</td>

					<td><html:text property="billingCode" value="<%=form.getBillingCode()%>" />

					<a onClick="popup('400', '600', 'support/billingcodes.jsp?form=TeleplanCorrectionFormWCB&field=billingCode', 'Code');">Service</a>

					</td>

				</tr>-->
				<tr>
					<td class="FormLabel">Bill Amount:</td>
					<td><html:text property="billingAmount"
						value="<%=form.getBillingAmount()%>" /></td>
				</tr>
				<tr>
					<td class="FormLabel" title="Internal Adjustment">Int. Adj.</td>
					<td><label> Amount: <input name="adjAmount"
						type="text" size="7" maxlength="7"> </label> <label> <input
						type="checkbox" name="adjType" value="1" /> debit </label></td>
				</tr>
				<tr>
					<td class="FormLabel">ICD 9:</td>
					<td><html:text property="w_icd9" value="<%=form.getW_icd9()%>" />
					<a onClick="popICD9List('TeleplanCorrectionFormWCB','w_icd9');">Codes</a>
					</td>
				</tr>
				<tr>
					<td class="FormLabel">Body Part:</td>
					<td><html:text property="w_bp" value="<%=form.getW_bp()%>" />
					<a onClick="popBodyPartList('TeleplanCorrectionFormWCB','w_bp');">Codes</a>
					</td>
				</tr>
				<tr>
					<td class="FormLabel">Side:</td>
					<td colspan="2"><html:select name="w_side"
						value="<%=form.getW_side()%>" property="w_side">
						<html:option value="B">Left and Right</html:option>
						<html:option value="L">Left</html:option>
						<html:option value="N">Not Applicable</html:option>
						<html:option value="R">Right</html:option>
					</html:select></td>
				</tr>
				<tr>
					<td class="FormLabel">Nature Of Injury:</td>
					<td><html:text property="w_noi" value="<%=form.getW_noi()%>" />
					<a onClick="popNOIList('TeleplanCorrectionFormWCB','w_noi');">Codes</a>
					</td>
				</tr>
				<tr>
					<td class="FormLabel">Fee Item:</td>
					<td><html:text property="w_feeitem"
						value="<%=form.getW_feeitem()%>" /> <a
						onClick="popFeeItemList('TeleplanCorrectionFormWCB','w_feeitem');">Codes</a>
					</td>
				</tr>
				<!--<tr>

					<td class="FormLabel">Fee Item:</td>

					<td><html:text property="w_extrafeeitem" value="<%=form.getW_extrafeeitem()%>" />

					<a onClick="popup('400', '600', 'support/billingfeeitem.jsp?info=all&form=TeleplanCorrectionFormWCB&field=w_extrafeeitem', 'eFeeItem');">Codes</a></td>

				</tr>-->
				<tr>
					<td class="FormLabel">Service Location:</td>
					<td><html:select property="serviceLocation"
						style="font-size:80%;" value="<%=form.getServiceLocation()%>">
						<html:options collection="billvisit" property="visitType"
							labelProperty="description" />
					</html:select></td>
				</tr>
				<tr>
					<td class="FormLabel">Report Type:</td>
					<td><html:text property="w_reporttype"
						value="<%=form.getW_reporttype()%>" /></td>
				</tr>
				<tr>


              <td colspan="2">
              <%String status = form.getStatus()!=null?form.getStatus():"";
              String statusCodes[] = {"O","P","N","B","X","T","C","D"};
              request.setAttribute("codes",statusCodes);

              %>
                 <!-- includes the Billing Type Drop Down List -->
                 <jsp:include flush="false" page="billType_frag.jsp">
                    <jsp:param name="BillType" value="<%=status%>"/>                 
                 </jsp:include>              
              </td>
            </tr>
          </table>
        </td>
        <td width="50%" align="left" valign="top">
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td>
                <table width="100%">
                  <tr>
                    <td class="FormLabel">Disabled from Work:</td>
                    <td>
                      <html:select name="w_work" value="<%=form.getW_work()%>" property="w_work">
                        <html:option value="Y">Yes</html:option>
                        <html:option value="N">No</html:option>
                      </html:select>
                    </td>
                  </tr>
                  <tr>
                    <td class="FormLabel">Date Of Injury:</td>
                    <td>
                      <html:text readonly="true" property="w_doi" value="<%=form.getW_doi()%>" styleId="w_doi"/>
                      <a id="hlIDate">Date</a>
                    </td>
                  </tr>
                  <tr>
                    <td class="FormLabel">Service Date:</td>
                    <td>
                      <html:text readonly="true" property="w_servicedate" value="<%=form.getW_servicedate()%>" styleId="w_servicedate"/>
                      <a id="hlSDate">Date</a>
                    </td>
                  </tr>
                  <tr>
                    <td class="FormLabel">Work Date:</td>
                    <td>
                      <html:text readonly="true" property="w_workdate" value="<%=form.getW_workdate()%>" styleId="w_workdate"/>
                      <a id="hlWDate">Date</a>
                    </td>
                  </tr>
                  <tr>
                    <td class="FormLabel">Estimate:</td>
                    <td>
                      <html:select name="w_estimate" value="<%=form.getW_estimate()%>" property="w_estimate">
                        <html:option value="0">At Work</html:option>
                        <html:option value="1">1-6 days</html:option>
                        <html:option value="2">7-13 days</html:option>
                        <html:option value="3">14-20 days</html:option>
                        <html:option value="9">                          &gt;
                          20 days
</html:option>
							</html:select></td>
						</tr>
						<tr>
							<td class="FormLabel">Estimate Date:</td>
							<td><html:text readonly="true" property="w_estimatedate"
								value="<%=form.getW_estimatedate()%>" styleId="w_estimatedate" />
							<a id="hlEDate">Date</a></td>
						</tr>
						<tr>
							<td class="FormLabel">First Treatment:</td>
							<td><html:text property="w_ftreatment"
								value="<%=form.getW_ftreatment()%>" /></td>
						</tr>
						<tr>
							<td class="FormLabel">Problem:</td>
							<td><html:textarea style="width:100%" property="w_problem"
								value="<%=form.getW_problem()%>" /></td>
						</tr>
						<tr>
							<td class="FormLabel">Diagnosis:</td>
							<td><html:textarea style="width:100%" property="w_diagnosis"
								value="<%=form.getW_diagnosis()%>" /></td>
						</tr>
						<tr>
							<td class="FormLabel">Clinical Info:</td>
							<td><html:textarea style="width:100%"
								property="w_clinicinfo" value="<%=form.getW_clinicinfo()%>" /></td>
						</tr>
						<tr>
							<td class="FormLabel">Problem:</td>
							<td><html:textarea style="width:100%" property="w_capreason"
								value="<%=form.getW_capreason()%>" /></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr valign="top">
			<td width="50%">
			<table width="100%" border="0">
				<tr class="SectionHead">
					<td>Error Report</td>
				</tr>
				<tr>
					<td align="left" valign="top">
					<ul>
						<%
                  if ("" != request.getParameter("billing_no")) {
                   
                    String desc = null;
                    for(TeleplanC12 result : teleplanC12Dao.select_c12_record("O", request.getParameter("billing_no"))) {
                    
                      String seqNum = result.getDataSeq();
                      
                      if(result.getExp1() != null &&  codes.get(result.getExp1())!=null  && !( (String)codes.get(result.getExp1()) ).trim().equals("")) {
                    	  %><li><%=seqNum+" "+result.getExp1()+ " - " + codes.get(result.getExp1())%></li><%
                      }
                      if(result.getExp2() != null &&  codes.get(result.getExp2())!=null  && !( (String)codes.get(result.getExp2()) ).trim().equals("")) {
                    	  %><li><%=seqNum+" "+result.getExp2()+ " - " + codes.get(result.getExp2())%></li><%
                      }
                      if(result.getExp3() != null &&  codes.get(result.getExp3())!=null  && !( (String)codes.get(result.getExp3()) ).trim().equals("")) {
                    	  %><li><%=seqNum+" "+result.getExp3()+ " - " + codes.get(result.getExp3())%></li><%
                      }
                      if(result.getExp4() != null &&  codes.get(result.getExp4())!=null  && !( (String)codes.get(result.getExp4()) ).trim().equals("")) {
                    	  %><li><%=seqNum+" "+result.getExp4()+ " - " + codes.get(result.getExp4())%></li><%
                      }
                      if(result.getExp5() != null &&  codes.get(result.getExp5())!=null  && !( (String)codes.get(result.getExp5()) ).trim().equals("")) {
                    	  %><li><%=seqNum+" "+result.getExp5()+ " - " + codes.get(result.getExp5())%></li><%
                      }
                      if(result.getExp6() != null &&  codes.get(result.getExp6())!=null  && !( (String)codes.get(result.getExp6()) ).trim().equals("")) {
                    	  %><li><%=seqNum+" "+result.getExp6()+ " - " + codes.get(result.getExp6())%></li><%
                      }
                      if(result.getExp7() != null &&  codes.get(result.getExp7())!=null  && !( (String)codes.get(result.getExp7()) ).trim().equals("")) {
                    	  %><li><%=seqNum+" "+result.getExp7()+ " - " + codes.get(result.getExp7())%></li><%
                      }
                     
                      
                      
                  } //for results
                  } //if billingNo
                      
                %>
					</ul>
					</td>
				</tr>
			</table>
			</td>
			<td><jsp:include flush="false" page="billTransactions.jsp">
				<jsp:param name="billNo" value="<%=form.getBillingNo()%>" />
			</jsp:include></td>
		</tr>
	</table>
	<table width="100%">
	</table>
	<html:hidden property="billingNo" value="<%=form.getBillingNo()%>" />
	<html:hidden property="id" value="<%=form.getId()%>" />
	<table width="100%">
		<tr>
			<td colspan="2" align="center" class="SectionHead"><a
				href="billingTeleplanCorrectionWCB.jsp?billing_no=<%=form.getId()%>">Refresh</a>
			| <input type="button" name="Button" value="Close"
				onClick="window.close();"> | <input type="button"
				name="Button" value="Print" onClick="window.print();"> | <%if(!status.equals("S")){%>
			<html:submit /> <html:submit property="settle" value="Settle Bill" />
			<%}%>
			</td>
		</tr>
	</table>
	<script language='javascript'>

		   Calendar.setup({inputField:"w_servicedate",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlSDate",singleClick:true,step:1});

			//Calendar.setup({inputField:"w_servidedate",ifFormat:"y-mm-dd",button:"hlSDate",align:"Bl",singleClick:true});

			//Calendar.setup({inputField:"w_doi",ifFormat:"y-mm-dd",button:"hlIDate",align:"Bl",singleClick:true});

			Calendar.setup({inputField:"w_doi",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlIDate",singleClick:true,step:1});



			//Calendar.setup({inputField:"w_workdate",ifFormat:"y-mm-dd",button:"hlWDate",align:"Bl",singleClick:true});

			Calendar.setup({inputField:"w_workdate",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlWDate",singleClick:true,step:1});

			//Calendar.setup({inputField:"w_estimatedate",ifFormat:"y-mm-dd",button:"hlEDate",align:"Bl",singleClick:true});

			Calendar.setup({inputField:"w_estimatedate",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlEDate",singleClick:true,step:1});

		</script>
</html:form>
</body>
</html:html>
<%!
  String checked(String val, String str, boolean dfault) {
    String retval = "";
    if (str == null || str.equals("null")) {
      str = "";
    }
    if (str.equals("") && dfault) {
      retval = "CHECKED";
    }
    else if (str != null && str.equalsIgnoreCase(val)) {
      retval = "CHECKED";
    }
    return retval;
  }
%>
