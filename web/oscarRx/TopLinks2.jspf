<%@ page import="oscar.oscarRx.data.*,oscar.oscarProvider.data.ProviderMyOscarIdData,oscar.oscarDemographic.data.DemographicData,oscar.OscarProperties,oscar.log.*"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%/*
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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
%>
<%/*
	oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean)pageContext.findAttribute("bean");

	RxPharmacyData pharmacyData = new RxPharmacyData();
	RxPharmacyData.Pharmacy pharmacy;
	pharmacy = pharmacyData.getPharmacyFromDemographic(Integer.toString(bean.getDemographicNo()));
	String prefPharmacy = "";
	if (pharmacy != null)
	{
		prefPharmacy = pharmacy.name;
	}

	String drugref_route = OscarProperties.getInstance().getProperty("drugref_route");
	if (drugref_route == null) drugref_route = "";
	String[] d_route = ("Oral," + drugref_route).split(",");

	String annotation_display = org.oscarehr.casemgmt.model.CaseManagementNoteLink.DISP_PRESCRIP;
*/
%>

<tr>
	<td width="10%" height="37" bgcolor="#000000">&nbsp;</td>
        <td width="100%" valign="bottom"  bgcolor="#000000" style="border-left: 2px solid #A9A9A9; padding-left: 5" height="0%" colspan="2">
            <table width="100%" border="0" height="100%">
                <tr>
                    <td valign="bottom" style="color:white;" >
	<span class="ScreenTitle" >oscarRx</span>
                   --<b><bean:message key="SearchDrug.nameText" /></b> <jsp:getProperty name="patient" property="firstName" /> <jsp:getProperty name="patient" property="surname" />
		     <b><bean:message key="SearchDrug.ageText" /></b> <jsp:getProperty name="patient" property="age" />
                     <b>
                         <a style="color:white; "href="SelectPharmacy2.jsp"><bean:message key="SearchDrug.PreferedPharmacy"/></a>:</b> <a  style="color:white;" href="javascript: function myFunction() {return false; }" onClick="showpic('Layer1');" id="Calcs" ><%=prefPharmacy%></a>
				<oscar:oscarPropertiesCheck property="MY_OSCAR" value="yes">
						<indivo:indivoRegistered demographic="<%=String.valueOf(bean.getDemographicNo())%>" provider="<%=bean.getProviderNo()%>">
							<a href="javascript: phrActionPopup('../oscarRx/SendToPhr.do?demoId=<%=Integer.toString(bean.getDemographicNo())%>', 'sendRxToPhr');">Send To myOscar</a>
						</indivo:indivoRegistered>
					</oscar:oscarPropertiesCheck>
                  
                    </td>
                    <td valign="top" align="right">
                        <span class="HelpAboutLogout" style="color:white;">
                            
                            <span class="FakeLink" style="color:white;"><oscar:help keywords="RX371" key="app.top1" style="color:white;"/></span> |
                            <span class="FakeLink" ><a style="color:white;" href="About.htm">About</a></span> |
                            <span class="FakeLink" style="color:white;"><a style="color:white;" href="Disclaimer.htm">Disclaimer</a></span>
                        </span>
                    </td>
                </tr>
            </table>
	</td>
</tr>