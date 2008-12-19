<!-- 
/**
 * Copyright (C) 2007.
 * Centre for Research on Inner City Health, St. Michael's Hospital, Toronto, Ontario, Canada.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
 -->
  
<%@ page language="java"%>
<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*, org.oscarehr.util.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.springframework.context.*,org.springframework.web.context.support.*" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<%
	String formClass = "MentalHealthForm1";
	String formLink = "formMentalHealthForm1.jsp";
	String formLink_printPreview = "formMentalHealthForm1Print.jsp";
	int programNo = Integer.parseInt((String)request.getSession().getAttribute(SessionConstants.CURRENT_PROGRAM_ID));
    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(demoNo, formId);
	String project_home = request.getContextPath().substring(1);	

	boolean bView = false;
	if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 
%>

<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>

<head>
    <title>Mental Health Form1</title>
    
</head>

<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1">

<table width="100%" class="header">
	<tr width="100%">
		<td align="left"><input type="button" value="Exit"
			onclick="window.close();" /> <input type="button" value="Print"
			onclick="window.print()" /></td>
	</tr>
</table>

<table width="100%" height="63" border="0">
  <tr>
    <th width="131" height="59" scope="col"><p>Ministry<br />
      Of<br />
      Health
    </p></th>
    <th width="243" scope="col">Form1<br />
      Mental Health Act</th>
    <th width="322" scope="col">Application by Physician for<br />
	Psychiatric Assessment</th>
  </tr>
</table>
<hr />
<p>&nbsp;</p>
<table width="100%" border="0">
  <tr>
    <td width="315" scope="col">Name of physician</td>
    <td width="403" scope="col">
      <label>
              <%= props.getProperty("physicianName", "") %>
      </label>
    </td>
  </tr>
  <tr>
    <td height="26"><div align="left">Physician address</div></td>
    <td>
        <label>
          <%= props.getProperty("physicianAddress", "") %>
        </label>
    </td>
  </tr>
  <tr>
    <td><div align="left">Telephone number </div></td>
    <td>
      <label>
        <%= props.getProperty("telephoneNumber", "") %>
      </label>
    </td>
  </tr>
  <tr>
    <td><div align="left">Fax number </div></td>
    <td>
      <label>
      <%= props.getProperty("faxNumber", "") %>
      </label>
    </td>
  </tr>
  <tr>
    <td>On (date)</td>
    <td>
      <label>
        <%= props.getProperty("onDate", "") %>
      </label>
    </td>
  </tr>
  <tr>
    <td>I personally examined (print full name of person)</td>
    <td>
      <label>
        <%= props.getProperty("clientName", "") %>
      </label>
    </td>
  </tr>
  <tr>
    <td>whose address is (home address)</td>
    <td>
      <label>
        <%= props.getProperty("clientAddress", "") %>
      </label>
    </td>
  </tr>
</table>
<p>
  <!--ddd -->
</p>
<div>
  <p>You may only sign this <strong>Form 1</strong> if you have personally examined the person within the past seven days. In deciding if a Form 1 is appropriate, you must complete <strong>either </strong>Box A (serious harm test) <strong>or </strong>Box B (persons who are incapable of consenting to treatment and meet the specified criteria test) below.</p>
  <p>&nbsp;</p>
</div>
<table width="100%" border="1">
  <tr>
    <th scope="col"><p>Box A - Section 15(1) of the Mental Health Act <br />
    Serious Harm Test</p></th>
  </tr>
  <tr>
    <td><strong>The Past / Present Test</strong> (check one or more)</td>
  </tr>
  <tr>
    <td>I have reasonalbe cause to believe that the person:</td>
  </tr>
  <tr>
    <td>
       <label>
       		<input type="checkbox" name="threatened" id="threatened" <%=props.getProperty("threatened","")%> />
	   </label>
       has threatened or is threatening to cause bodily harm to himself or herself
	</td>
  </tr>
  
  
  
  <tr>
    <td>
      <label>
      <input type="checkbox" name="attempted" id="attempted" <%=props.getProperty("attempted","")%> />
			
      </label>
    has attempted or is attempting to cause bodily harm to himself or herself
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="behaved" id="behaved" <%=props.getProperty("behaved","")%>/>
      </label>
    has behaved or is behaving violently towards another person
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="caused" id="caused" <%=props.getProperty("caused","")%>/>
      </label>
    has caused or is causing another person to fear bodily harm from him or her; or
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="shown" id="shown" <%=props.getProperty("shown","")%>/>
      </label>
    has shown or is showing a lack of competence to care for himself or herself
    </td>
  </tr>
  <tr>
    <td>I base this belief on the following information (you may, as appropriate in the circumstances, rely on any combination of your own observations and information communicated to you by others.)</td>
  </tr>
  <tr>
    <td><p>My own observations:</p>
        <label>
          <%=props.getProperty("observation","")%>
        </label>
    <p>&nbsp;</p></td>
  </tr>
  <tr>
    <td><p>Facts communicated to me by others:</p>      
        <%=props.getProperty("facts","")%>
    <p>&nbsp;</p></td>
  </tr>
  <tr>
    <td><strong>The future Test</strong> (check one or more)</td>
  </tr>
  <tr>
    <td>I am of the opinion that the person is apparently suffering from mental disorder of a nature or quality that likely will result in:</td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="harmHimself" id="harmHimself" <%=props.getProperty("harmHimself","")%>/>
      </label>
    serious bodily harm to himself or herself,
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="harmOthers" id="harmOthers" <%=props.getProperty("harmOthers","")%>/>
      </label>
    serious bodily harm to another person,
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="impairment" id="impairment" <%=props.getProperty("impairment","")%> />
      </label>
    serious physical impairment of himself or herself
    </td>
  </tr>
  <tr>
    <td><p>I base this opnion on the following information (you may, as appropriate in the circumstances, rely on any combination of your own observations and information communicated to you by others.)</p></td>
  </tr>
  <tr>
    <td><p>My own observastions:</p>      
        <label>
          <%=props.getProperty("observation2","")%>
        </label>
    <p>&nbsp;</p></td>
  </tr>
  <tr>
    <td><p>Facts communicated by others:</p>
        <label>
          <%=props.getProperty("facts2","")%>
        </label>
    <p>&nbsp;</p></td>
  </tr>
</table>
<p>&nbsp;</p>
<table width="100%" border="1">
  <tr>
    <th scope="col"><p>Box B - Section 15(1.1) of the Mental Health Act<br />
      Patients who are Incapable of Consenting to Treatment and Meet the Specified Criteria</p>
      <p>Note: The patient must meet the criteria set out in each of the following conditions.</p></th>
  </tr>
  <tr>
    <td>I have reasonable cause to believe that the person:</td>
  </tr>
  <tr>
    <td>1. Has previously received treatment for mental disorder of an ongoing or recurring nature that, when not treated, is of a nature or quality that likely will result in one or more of the following: (please indicate one or more)</td>
  </tr>
  <tr>
    <td>
      <label>
        <input name="harmHimselfB" type="checkbox" id="harmHimselfB" <%=props.getProperty("harmHimselfB","")%> />
      </label>
    serious bodily harm to himself or herself,
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="harmOthersB" id="harmOthersB" <%=props.getProperty("harmOthersB","")%>/>
      </label>
    serious bodily harm to another person,
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="deteriorationB" id="deteriorationB" <%=props.getProperty("deteriorationB","")%>/>
      </label>
    substantial mental or physical deterioration of himself or herself, or
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="impairmentB" id="impairmentB" <%=props.getProperty("impairmentB","")%>/>
      </label>
    serious physical impairment of himself or herself;
    </td>
  </tr>
  <tr>
    <td>AND</td>
  </tr>
  <tr>
    <td>2. Has shown clinical improvement as a result of the treatment.</td>
  </tr>
  <tr>
    <td>AND</td>
  </tr>
  <tr>
    <td>I am of the opinion that the person,</td>
  </tr>
  <tr>
    <td>3. Is incapable, within the meaning of the <em>Health Care Consent Act, 1996</em>, of consenting to his or her treatment in a psychiatric facility and the consent of his or her substitute decision-maker has been obtained;</td>
  </tr>
  <tr>
    <td>AND</td>
  </tr>
  <tr>
    <td>4. Is apparently suffering from the same mental disorder as the one for which he or she previously received treatment or from a mental disorder that is similar to the previous one;</td>
  </tr>
  <tr>
    <td>AND</td>
  </tr>
  <tr>
    <td>5. Given the person's history of mental disorder and current mental or physical condition, is likely to: (choose one or more of the following)</td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="harmHimselfB2" id="harmHimselfB2" <%=props.getProperty("harmHimselfB2","")%>/>
      </label>
    cause serious bodily harm to himself or herself, or
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="harmOthersB2" id="harmOthersB2" <%=props.getProperty("harmOthersB2","")%>/>
      </label>
    cause serious bodily harm to another person, or
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="deteriorationB2" id="deteriorationB2" <%=props.getProperty("deteriorationB2","")%>/>
      </label>
    suffer substantial mental or physical deterioration, or
    </td>
  </tr>
  <tr>
    <td>
      <label>
        <input type="checkbox" name="impairmentB2" id="impairmentB2" <%=props.getProperty("impairmentB2","")%>/>
      </label>
    suffer serious physical impairment
    </td>
  </tr>
  <tr>
    <td height="44">I base this opinion on the following information (you may, as appropriate in the circumstances, rely on any combination of your own observations and information communicated to you by others.)</td>
  </tr>
  <tr>
    <td><p>My own observations:</p>      
        <label>
          <%=props.getProperty("observationB","")%>
        </label>
    <p>&nbsp;</p></td>
  </tr>
  <tr>
    <td><p>Facts communicated by others:</p>
        <label>
          <%=props.getProperty("factsB","")%>
        </label>
    <p>&nbsp;</p></td>
  </tr>
</table>
<p>&nbsp;</p>
<table width="100%" height="490" border="0">
  <tr>
    <td colspan="2" scope="col">I have made careful inquiry into all the facts necessary for me to form my opinion as to the nature and quality of the person's mental disorder. I hereby make application for a psychiatric assessment of the person named.</td>
  </tr>
  <tr>
    <td width="60%">Today's date<label>
          <%=props.getProperty("todayDate","")%>
        </label>
    </td>
    <td width="60%"><p>Today's time
        <%=props.getProperty("todayTime","")%>"
      </p></td>
  </tr>
  <tr>
    <td colspan="2">Examining physician's signature (signature of physician)<label>
          <%=props.getProperty("signature","")%>
        </label>
    </td>
  </tr>
  <tr>
    <td colspan="2">This form authorizes, for a period of 7 days including the date of signature, the apprehension of the person named and his or her detention in a psychiatirc facility for a maximum of 72 hours.</td>
  </tr>
  <tr>
    <td colspan="2"><strong>For Use at the Psychiatric Facility</strong></td>
  </tr>
  <tr>
    <td height="53" colspan="2">Once the period of detention at the psychiatric facility begins, the attending physician should note the date and time this occurs and must promptly give the person a Form 42.</td>
  </tr>
  <tr>
    <td height="37" colspan="2">Date and time detention commences<label>
          <%=props.getProperty("datetimeOfDetention","")%>
        </label>
    </td>
  </tr>
  <tr>
    <td height="42">Signature of physician<label>
          <%=props.getProperty("signature1","")%>
        </label>
    </td>
	<td>Signature of physician<label>
          <%=props.getProperty("signature2","")%>
        </label>
    </td>
  </tr>
  

</body>
</html:html>

