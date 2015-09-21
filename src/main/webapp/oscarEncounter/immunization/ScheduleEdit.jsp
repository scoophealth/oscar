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
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_eChart");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page
	import="oscar.oscarEncounter.immunization.data.EctImmImmunizationData"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Record Immunization</title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<%
oscar.oscarEncounter.pageUtil.EctSessionBean bean = (oscar.oscarEncounter.pageUtil.EctSessionBean)request.getSession().getAttribute("EctSessionBean");
String node = request.getParameter("node").toString();
String immName = "";
if(request.getParameter("name")!=null)
	immName = request.getParameter("name");
%>
<script lanbuage="javascript">
    function changeStatus(status)
    {
        var frm = document.forms[0];
        switch(status)
        {
            case 1:
                chgGiven(true);
                chgRefused(false);
                setCurrent(frm.givenYear, frm.givenMonth, frm.givenDay);
                if(frm.provider.value < 1)
                {
                    frm.provider.value = <%= bean.providerNo %>;
                }

                frm.lot.focus();
                break;
            case 2:
                chgGiven(false);
                chgRefused(true);
                setCurrent(frm.refusedYear, frm.refusedMonth, frm.refusedDay);
                frm.refusedYear.focus();
                break;
            default:
                chgGiven(false);
                chgRefused(false);
        }

        function chgGiven(active)
        {
            var style = active ? "active" : "grey";
            frm.givenYear.className = style;
            frm.givenMonth.className = style;
            frm.givenDay.className = style;
            frm.lot.className = style;
            frm.provider.className = style;
            frm.givenYear.disabled = !active;
            frm.givenMonth.disabled = !active;
            frm.givenDay.disabled = !active;
            frm.lot.disabled = !active;
            frm.provider.disabled = !active;
        }

        function chgRefused(active)
        {
            var style = active ? "active" : "grey";
            frm.refusedYear.className = style;
            frm.refusedMonth.className = style;
            frm.refusedDay.className = style;
            frm.refusedYear.disabled = !active;
            frm.refusedMonth.disabled = !active;
            frm.refusedDay.disabled = !active;
        }

        function setCurrent(txtYear, cmbMonth, txtDay)
        {
            if(txtYear.value.length < 1 || txtDay.value.length < 1 || cmbMonth.selectedIndex < 1)
            {
                var dt = new Date();
                txtYear.value = dt.getFullYear();
                txtDay.value = dt.getDate();
                cmbMonth.selectedIndex = dt.getMonth();
            }
        }
    }

    function setHiddenDate(txtHidden, txtYear, cmbMonth, txtDay)
    {
        try
        {
            var y = txtYear.value;
            var m = cmbMonth.selectedIndex;
            var d = txtDay.value;
            var dt = new Date(y, m, d);
            var s = (dt.getFullYear() + '/' + (dt.getMonth() + 1) + '/' + dt.getDate());
            txtHidden.value = s;
            return true;
        }
        catch (ex)
        {
            alert('Invalid date. Please fix the value and resubmit.');
            return false;
        }
    }

/*
    function valDate(dateString)
    {
        var s = dateString.split('/');

        var y = s[0];
        var m = s[1];
        var d = s[2];

        var dt = new Date(y, m - 1, d);

        if(! isDate(dt))
        {
            alert('bad date');
        }
    }
*/

    function parseHiddenDate(txtHidden, txtYear, cmbMonth, txtDay)
    {
        try
        {
            var dt = new Date(txtHidden.value);
            var y = dt.getFullYear();
            var m = dt.getMonth();
            var d = dt.getDay();
            if(isNaN(y) || isNaN(m) || isNaN(d))
            {
                throw('');
            }
            else
            {
                txtYear.value = dt.getFullYear(); //y2k compliance is very important
                txtDay.value = dt.getDate();
                var m = dt.getMonth();
                cmbMonth.selectedIndex = m; //js dates are 0-based
            }
        }
        catch (ex)
        {
            txtHidden.value = '';
            txtYear.value = '';
            cmbMonth.selectedIndex = 0;
            txtDay.value = '';
        }
        return;
    }

    function loadPage()
    {
        var frm = window.opener.document.forms[0];
        var vGivenDate   = (frm.<%=node + "_givenDate"%>.value);
        var vLot         = (frm.<%=node + "_lot"%>.value);
        var vProvider    = (frm.<%=node + "_provider"%>.value);
        var vRefusedDate = (frm.<%=node + "_refusedDate"%>.value);
        var vComments    = (frm.<%=node + "_comments"%>.value);
        var editFrm = document.forms[0];
        
        if(vGivenDate.length>0)
        {
            editFrm.chkStatus[1].click();
            editFrm.givenDate.value = vGivenDate;
            editFrm.lot.value = vLot;
            editFrm.provider.value = vProvider;
            editFrm.refusedDate.value = '';
        }
        else if(vRefusedDate.length>0)
        {
            editFrm.chkStatus[2].click();
            editFrm.refusedDate.value = vRefusedDate;
            editFrm.givenDate.value = '';
            editFrm.lot.value = '';
            editFrm.provider.value = '';
        }
        else
        {
            editFrm.chkStatus[0].click();
            editFrm.givenDate.value = '';
            editFrm.lot.value = '';
            editFrm.provider.value = '';
            editFrm.refusedDate.value = '';
        }

        parseHiddenDate(editFrm.givenDate, editFrm.givenYear, editFrm.givenMonth, editFrm.givenDay);
        parseHiddenDate(editFrm.refusedDate, editFrm.refusedYear, editFrm.refusedMonth, editFrm.refusedDay);
        editFrm.comments.value = vComments;
    }

    function saveClose()
    {
        var node = '<%= node %>';
        var frm = document.forms[0];
        var vGivenDate = '';
        var vRefusedDate = '';
        var vLot = '';
        var vProvider = '';
        var vComments = '';
        vComments = frm.comments.value;

        if(frm.chkStatus[1].checked) // given
        {
            setHiddenDate(frm.givenDate, frm.givenYear,
                    frm.givenMonth, frm.givenDay);
            vGivenDate = frm.givenDate.value;
            vLot = frm.lot.value;
            vProvider = frm.provider.value;
        }
        else if(frm.chkStatus[2].checked) // refused
        {
            setHiddenDate(frm.refusedDate, frm.refusedYear,
                    frm.refusedMonth, frm.refusedDay);
            vRefusedDate = frm.refusedDate.value;
        }        
        // call method defined in Schedule.jsp
        window.opener.returnEdit(node, vGivenDate, vRefusedDate, vLot, vProvider, vComments);
        window.close();
    }

    function popupStart(vheight,vwidth,varpage) {
      var page = varpage;
      windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
      var popup=window.open(varpage, "", windowprops);
      if (popup != null) {
        if (popup.opener == null) {
          popup.opener = self;
        }
      }
    }
</script>
</head>

<body class="BodyStyle" vlink="#0000FF" onload="loadPage();"
	bottommargin="0">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">record immunization</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBarShort">
			<tr>
				<td class="Header"
					style="padding-left: 2px; padding-right: 2px; border-right: 2px solid #003399; text-align: left; font-size: 80%; font-weight: bold; width: 100%;"
					NOWRAP><%=bean.patientLastName %>, <%=bean.patientFirstName%>
				<%=bean.patientSex%> <%=bean.patientAge%></td>
				<td></td>
				<td style="text-align: right" NOWRAP><oscar:help keywords="schedule" key="app.top1"/>> |</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn"></td>
		<td class="MainTableRightColumn">
		<form name="/oscarEncounter/scheduleEdit">
		<table>
			<tr>
				<td style="font-weight: bold">&nbsp;<%=immName%></td>
			</tr>
			<tr>
				<td style="font-weight: bold"><input type="radio"
					name="chkStatus" onclick="javascript:changeStatus(0);" value="0"
					checked="checked"></input> <span onclick="chkStatus[0].click()">Not
				Given</span></td>
			</tr>
			<tr>
				<td style="font-weight: bold"><input type="radio"
					name="chkStatus" onclick="javascript:changeStatus(1);" value="1"></input>
				<span onclick="chkStatus[1].click()">Immunization Given</span></td>
			</tr>
			<tr>
				<td>
				<table
					style="margin-left: 50px; border-width: 2px; border-style: solid;">
					<tr>
						<td align="right" width="120px">Given Date:</td>
						<td><input type="hidden" id="givenDate" /> <input
							type="text" id="givenYear" style="width: 50px" maxlength=4 /> <b>-</b>
						<select id="givenMonth">
							<option value="1">January</option>
							<option value="2">February</option>
							<option value="3">March</option>
							<option value="4">April</option>
							<option value="5">May</option>
							<option value="6">June</option>
							<option value="7">July</option>
							<option value="8">August</option>
							<option value="9">September</option>
							<option value="10">October</option>
							<option value="11">November</option>
							<option value="12">December</option>
						</select> <b>-</b> <input type=text id="givenDay" style="width: 50px"
							maxlength=2 /></td>
					</tr>
					<tr>
						<td align="right">Lot Number:</td>
						<td><input type="text" id="lot" /></td>
					</tr>
					<tr>
						<td align="right">Provider:</td>
						<td><select id="provider">
							<%= providerCombo() %>
						</select></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td style="font-weight: bold"><input type="radio"
					name="chkStatus" onclick="javascript:changeStatus(2);" value="2"></input>
				<span onclick="chkStatus[2].click()">Refused</span></td>
			</tr>
			<tr>
				<td>
				<table
					style="margin-left: 50px; border-width: 2px; border-style: solid;">
					<tr>
						<td align="right" width="120px">Refused Date:</td>
						<td><input type="hidden" id="refusedDate" /> <input
							type="text" id="refusedYear" style="width: 50px" maxlength=4 />
						<b>-</b> <select id="refusedMonth">
							<option value="1">January</option>
							<option value="2">February</option>
							<option value="3">March</option>
							<option value="4">April</option>
							<option value="5">May</option>
							<option value="6">June</option>
							<option value="7">July</option>
							<option value="8">August</option>
							<option value="9">September</option>
							<option value="10">October</option>
							<option value="11">November</option>
							<option value="12">December</option>
						</select> <b>-</b> <input type=text id="refusedDay" style="width: 50px"
							maxlength=2 /></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td><br />
				<div style="font-weight: bold">Comments</div>
				<textarea id="comments" style='width: 100%; height: 80px'></textarea>
				</td>
			</tr>
			<tr>
				<td align="right"><br />
				<input type="button" style="width: 120px"
					onclick="javascript:saveClose();" value="Save and Close" /> <input
					type="button" style="width: 120px"
					onclick="javascript:window.close();" value="Cancel" /></td>
			</tr>
		</table>
		</form>
		<%!
                String providerCombo()
                        throws java.sql.SQLException
                {
                    StringBuffer sb = new StringBuffer();

                    EctImmImmunizationData imm = new EctImmImmunizationData();
                    String[] prov = imm.getProviders();

                    for(int i=0; i<prov.length; i++)
                    {
                        String sVal = prov[i].split("/")[0];
                        String sTxt = prov[i].split("/")[1];

                        sb.append("<option value='" + sVal + "'>" + sTxt + "</option>");
                    }
                    return new String(sb);
                }
                %></td>
	</tr>
</table>
</body>
</html>
