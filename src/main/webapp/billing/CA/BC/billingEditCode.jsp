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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../../securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="java.util.*,oscar.oscarBilling.ca.bc.data.BillingCodeData,oscar.oscarBilling.ca.bc.pageUtil.*"%>

<html:html locale="true">

<%@ page import="org.oscarehr.common.dao.BillingServiceDao,org.oscarehr.util.SpringUtils,org.oscarehr.common.model.*" %>
<%BillingServiceDao billingServiceDao = (BillingServiceDao) SpringUtils.getBean("billingServiceDao"); %>

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/prototype.js"></script>
<title>Edit Billing Code</title>
<link rel="stylesheet" type="text/css"
	href="../../../oscarEncounter/encounterStyles.css">
<script type="text/javascript">
function setValues(){

}



</script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle" vlink="#0000FF" onLoad="setValues()">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">billing</td>
		<td class="MainTableTopRowRightColumn">
                   


		<table class="TopStatusBar">
			<tr>
				<td>Edit Billing Code <%=request.getParameter("code")%> -- <%=request.getParameter("desc")%> </td>
				<td>&nbsp;</td>
				<td style="text-align: right">
                                    <a href="javascript:popupStart(300,400,'Help.jsp')"><bean:message key="global.help" /></a> |
                                    <a href="javascript:popupStart(300,400,'About.jsp')"><bean:message key="global.about" /></a> |
                                    <a href="javascript:popupStart(300,400,'License.jsp')"><bean:message key="global.license" /></a>
                                </td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top">&nbsp; &nbsp;</td>
		<td class="MainTableRightColumn">
                    <table border="1" width="600px">
                        <tr><th colspan="5"><%=request.getParameter("code")%> -- <%=request.getParameter("desc")%></th></tr>
                        <tr>
                            <th>#</th>
                            <%-- th></th --%>
                            <th>Value</th>
                            <th>Billing Service Date</th>
                            <th>Termination Date</th>
                            <th>&nbsp;</th>
                        </tr>
                    <%
                    List<BillingService> bills = billingServiceDao.findBillingCodesByCode(request.getParameter("code"),"BC");
                    int i = 0;
                    for (BillingService bs :bills){
                        i++;
                    %>
                    <tr>
                        
                        <td><%=bs.getBillingserviceNo()%>
                            <input type="hidden" name="id<%=i%>" id="id<%=i%>" value="<%=bs.getBillingserviceNo()%>"/>
                        </td>
                        <%-- td><%=bs.getDescription()%></td --%>
                        <td><span id="val<%=i%>"><%=bs.getValue()%></span>
                            <input type="text" name="value" id="ival<%=i%>" value="<%=bs.getValue()%>" style="display:none;"/>
                        </td>
                        <td><span id="billservice<%=i%>"><%=bs.getBillingserviceDate()%></span>
                            <input type="text" name="billservice" id="ibillservice<%=i%>" value="<%=bs.getBillingserviceDate()%>" style="display:none;"/>
                        </td>
                        <td><span id="termdate<%=i%>"><%=bs.getTerminationDate()%></span>
                            <input type="text" name="termdate" id="itermdate<%=i%>" value="<%=bs.getTerminationDate()%>" style="display:none;"/>
                        </td>
                        <td><a id="edit<%=i%>"href="javascript: void(0);" onclick="editCode('<%=i%>','<%=bs.getBillingserviceNo()%>','<%=bs.getValue()%>','<%=bs.getBillingserviceDate()%>','<%=bs.getTerminationDate()%>');" >edit</a>
                            <a id="save<%=i%>"  style="display:none;" href="javascript: void(0);" onclick="saveCode('<%=i%>','<%=bs.getBillingserviceNo()%>','<%=bs.getValue()%>','<%=bs.getBillingserviceDate()%>','<%=bs.getTerminationDate()%>');" >save</a>
                            <span id="working<%=i%>" style="display:none;">...</span>
                        </td>
                    </tr>
                    <%}%>
                    </table>




                    <html:form action="/billing/CA/BC/billingEditCode">
                     <input type="hidden" name="whereTo" value="<%=request.getParameter("whereTo")%>"/>
                     <input type="hidden" name="method" value="returnToSearch"/>
                     <html:submit property="submitButton" value="Back" />
                    </html:form>


                    

                    <script type="text/javascript">
                        function editCode(id,billingserviceNo,Value,BillingserviceDate,TerminationDate){
                            $('val'+id).hide();
                            $('ival'+id).show();
                            $('billservice'+id).hide();
                            $('ibillservice'+id).show();
                            $('termdate'+id).hide();
                            $('itermdate'+id).show();
                            $('edit'+id).hide();
                            $('save'+id).show();

                        }


                        

                 


                        function saveCode(id,billingserviceNo,Value,BillingserviceDate,TerminationDate){
                            var url = ('billingEditCode.do?method=ajaxCodeUpdate');
                            $('save'+id).hide();
                            $('working'+id).show();

                            var ran_number=Math.round(Math.random()*1000000);
                            var params = "&codeId="+$('id'+id).value+"&id="+id+"&val="+$('ival'+id).value+"&billService="+$('ibillservice'+id).value+"&termDate="+$('itermdate'+id).value+"&rand="+ran_number;
                            new Ajax.Request(url, {method: 'get',parameters:params,evalJSON:true,onSuccess: function(transport) {
                                                                var data = transport.responseText.evalJSON();
                                                                var id = data.id;
                                                                $('val'+id).innerHTML=data.value;
                                                                var d = new Date(data.billingserviceDate.time);
                                                                $('billservice'+id).innerHTML=d.toLocaleFormat("%Y-%m-%d");
                                                                var t = new Date(data.terminationDate.time);
                                                                $('termdate'+id).innerHTML=t.toLocaleFormat("%Y-%m-%d");
                                                                $('val'+id).show();
                                                                $('ival'+id).hide();
                                                                $('billservice'+id).show();
                                                                $('ibillservice'+id).hide();
                                                                $('termdate'+id).show();
                                                                $('itermdate'+id).hide();
                                                                $('edit'+id).show();
                                                                $('working'+id).hide();
                                                          }
                                                        });

                            

                        }



                    </script>

                </td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
