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
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">

<%
  String transactionType = new String("Add Specialist");
  if ( request.getAttribute("upd") != null){
      transactionType = new String("Update Specialist");
  }
%>

<head>
<title>
<%=transactionType%>
</title>
<html:base/>
<style type="text/css">

.ChooseRecipientsBox1{
	font-size: 80%;
	height: 340px;
	/*width: 800px;*/
	overflow: auto;
        margin-left: 4px;
	border: 1px solid #dcdcdc;
}
.currGroup{
        color: #6666ff;
        FONT-FAMILY: tahoma;
        font-size: 14pt;
}

</style>
</head>
<script language="javascript">
function BackToOscar() {
       window.close();
}
</script>

<link rel="stylesheet" type="text/css" href="../../styles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF">

<html:errors/>
<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber1" height="100%">
    <tr>
        <td width="100%" style="padding-left: 3; padding-right: 3; padding-top: 2; padding-bottom: 2" height="0%" colspan="2">
        <p class="HelpAboutLogout"><span class="FakeLink"><a href="Help.htm">Help</a></span> |
        <span class="FakeLink"><a href="About.htm">About</a></span> | <span class="FakeLink">
        <a href="Disclaimer.htm">Disclaimer</a></span></p>
        </td>
    </tr>
    <tr>
        <td width="10%" height="37" bgcolor="#000000">&nbsp;</td>
        <td width="100%" bgcolor="#000000" style="border-left: 2px solid #A9A9A9; padding-left: 5" height="0%">
        <p class="ScreenTitle"><%=transactionType%></p>
        </td>
    </tr>
    <tr>
        <td></td>
        <td width="100%" style="border-left: 2px solid #A9A9A9; " height="100%" valign="top">
            <table cellpadding="0" cellspacing="2" style="border-collapse: collapse" bordercolor="#111111" width="100%" height="100%">

            <!----Start new rows here-->
               <tr>
                  <td >
                  <%oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConTitlebar titlebar = new oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConTitlebar();
                  out.print(titlebar.estBar(request));
                  %>
                  </td>
               </tr>
               <%
                   String added = (String) request.getAttribute("Added");
                   if (added != null){  %>
                    <tr>
                       <td>
                            <font color="red"> Specialist <%=added%> has been added. </font>
                       </td>
                    </tr>
               <%}%>
               <tr>
                  <td>
                        <div class="DivContentSectionHead">
                           <%=transactionType%>
                        </div>
                  </td>
               </tr>
               <tr>
                  <td>

                     <table>
                        <html:form  action="/oscarEncounter/AddSpecialist">
                        <%
                           if (request.getAttribute("specId") != null ){
                           oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConAddSpecialistForm thisForm;
                           thisForm = (oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConAddSpecialistForm) request.getAttribute("EctConAddSpecialistForm");
                           thisForm.setFirstName( (String) request.getAttribute("fName"));
                           thisForm.setLastName( (String) request.getAttribute("lName"));
                           thisForm.setProLetters( (String) request.getAttribute("proLetters"));
                           thisForm.setAddress( (String) request.getAttribute("address"));
                           thisForm.setPhone( (String) request.getAttribute("phone"));
                           thisForm.setFax( (String) request.getAttribute("fax"));
                           thisForm.setWebsite( (String) request.getAttribute("website"));
                           thisForm.setEmail( (String) request.getAttribute("email"));
                           thisForm.setSpecType( (String) request.getAttribute("specType"));
                           thisForm.setSpecId( (String) request.getAttribute("specId"));

                           }
                        %>
                        <html:hidden name="EctConAddSpecialistForm" property="specId"/>
                        <tr>
                           <td>
                                 First Name:
                              </td>
                              <td>
                                 <html:text name="EctConAddSpecialistForm" property="firstName"/>
                           </td>
                           <td>
                                 Last Name:
                              </td>
                              <td>
                                 <html:text name="EctConAddSpecialistForm" property="lastName"/>
                           </td>
                           <td>
                                 Professional Letters:
                              </td>
                              <td>
                                 <html:text name="EctConAddSpecialistForm" property="proLetters"/>
                           </td>
                        </tr>
                        <tr>
                           <td>
                                 Address:
                              </td>
                              <td colspan="5">
                                 <html:textarea name="EctConAddSpecialistForm" property="address" cols="30" rows="3" />
                           </td>
                        </tr>
                        <tr>
                           <td>
                                 Phone:
                              </td>
                              <td>
                                 <html:text name="EctConAddSpecialistForm" property="phone"/>
                              </td>
                           <td>
                                 Fax:
                              </td>
                              <td colspan="4">
                                 <html:text name="EctConAddSpecialistForm" property="fax"/>
                           </td>
                        </tr>
                        <tr>
                           <td>
                                 Website:
                             </td>
                              <td>
                                 <html:text name="EctConAddSpecialistForm" property="website"/>
                           </td>
                           <td>
                                 Email:
                              </td>
                              <td colspan="4">
                                 <html:text name="EctConAddSpecialistForm" property="email"/>
                              </td>
                        </tr>
                        <tr>
                           <td>
                                 Specialist Type:
                              </td>
                              <td colspan="5">
                                 <html:text name="EctConAddSpecialistForm" property="specType"/>
                           </td>
                        </tr>
                        <tr>
                           <td colspan="6">
                                    <input type="submit" name="transType" value="<%=transactionType%>"/>
                           </td>
                        </tr>
                        </html:form>
                     </table>
                  </td>
               </tr>
            <!----End new rows here-->

		        <tr height="100%">
                    <td>
                    </td>
                </tr>
            </table>
        </td>
    </tr>

	<tr>
    	<td height="0%" style="border-bottom:2px solid #A9A9A9; border-top:2px solid #A9A9A9; "></td>
    	<td height="0%" style="border-bottom:2px solid #A9A9A9; border-top:2px solid #A9A9A9; "></td>
  	</tr>
  	<tr>
    	<td width="100%" height="0%" colspan="2">&nbsp;</td>
  	</tr>
  	<tr>
    	<td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC" colspan="2"></td>
  	</tr>
</table>
</body>
</html:html>

