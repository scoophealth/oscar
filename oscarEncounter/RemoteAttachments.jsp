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
<%@ page import="oscar.encounter.immunization.data.*, oscar.encounter.immunization.util.*" %>
<%@ page import="oscar.encounter.immunization.pageUtil.*, java.util.*, org.w3c.dom.*, sun.misc.BASE64Encoder" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%
    oscar.oscarSecurity.SessionBean bean = null;
    if((bean=(oscar.oscarSecurity.SessionBean)request.getSession().getAttribute("SessionBean"))==null)
    {response.sendRedirect("error.jsp");
    return;}
    oscar.oscarEncounter.data.RemoteAttachments remoAttach = new oscar.oscarEncounter.data.RemoteAttachments();
    remoAttach.estMessageIds(bean.getDemographicNo());
%>

<%!
public String encode64(String plainText)
    {
        BASE64Encoder enc = new BASE64Encoder();
        return enc.encode(plainText.getBytes());
    }

%>



<html>
<head>
<title>
oscarComm
</title>
<script type="text/javascript" language=javascript>
function popupViewAttach(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "oscarVA", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}

function popupSendAttach(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "oscarSA", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}
</script>
<link rel="stylesheet" type="text/css" href="encounterStyles.css">
</head>

<body class="BodyStyle" vlink="#0000FF" >
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                oscarComm
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
                        <%= bean.patientLastName %> , <%= bean.patientFirstName%>
                        </td>
                        <td  >
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        </td>
                        <td style="text-align:right">
                                <a href="javascript:popupStart(300,400,'Help.jsp')"  >Help</a> | <a href="javascript:popupStart(300,400,'About.jsp')" >About</a> | <a href="javascript:popupStart(300,400,'License.jsp')" >License</a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn">
            <%String prov = bean.getDemographicNo();
              String demog = bean.providerNo;
            %>
            <a href="javascript:popupSendAttach(700,960,'../../oscarMessenger/Transfer/SelectItems.jsp?val1=<%=demog%>&val2=<%=prov%>')">Send eDocs <br>for this Patient</a>
            </td>
            <td class="MainTableRightColumn">
                <h2>Demographic Attachments</h2>
                <table border="0" width="80%" cellspacing="1">
                    <tr>
                        <th bgcolor="#DDDDFF">Subject</th>
                        <th bgcolor="#DDDDFF">Sent From</th>
                        <th bgcolor="#DDDDFF">Saved By</th>
                        <th bgcolor="#DDDDFF">Date</th>

                    </tr>
                <%
                for(int i=0; i < remoAttach.messageIds.size(); i++){
                String mesId   = (String) remoAttach.messageIds.get(i);
                String theDate = (String) remoAttach.dates.get(i);
                String svBy    = (String) remoAttach.savedBys.get(i);

                java.util.ArrayList lis = remoAttach.getFromLocation(mesId);

                System.out.println("sys = "+lis.size());

                String fromLoco = (String) lis.get(0);
                String subject  = (String) lis.get(1);
                %>
                    <tr>
                        <td bgcolor="#EEEEFF"><a href="javascript:popupViewAttach(700,960,'/oscarEncounter/ViewAttachment.do?mesId=<%=mesId%>')"><%=subject%></a></td>
                        <td bgcolor="#EEEEFF"><%=fromLoco%></td>
                        <td bgcolor="#EEEEFF"><%=svBy%></td>
                        <td bgcolor="#EEEEFF"><%=theDate%></td>

                    </tr>

              <%}%>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">
                &nbsp;
            </td>
            <td class="MainTableBottomRowRightColumn">
                &nbsp;
            </td>
        </tr>
    </table>
</body>
</html>
