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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="java.util.*,oscar.oscarBilling.ca.bc.data.BillingCodeData,oscar.oscarBilling.ca.bc.pageUtil.*" %>

<html:html locale="true">



<head>
<title>
Adjust Private Billing Codes
</title>
<link rel="stylesheet" type="text/css" href="../../../oscarEncounter/encounterStyles.css">
<script type="text/javascript">


	
	

</script>

<style type="text/css">
	table.outline{
	   margin-top:50px;
	   border-bottom: 1pt solid #888888;
	   border-left: 1pt solid #888888;
	   border-top: 1pt solid #888888;
	   border-right: 1pt solid #888888;
	}
	table.grid{
	   border-bottom: 1pt solid #888888;
	   border-left: 1pt solid #888888;
	   border-top: 1pt solid #888888;
	   border-right: 1pt solid #888888;
	}
	td.gridTitles{
		border-bottom: 2pt solid #888888;
		font-weight: bold;
		text-align: center;
	}
        td.gridTitlesWOBottom{
                font-weight: bold;
                text-align: center;
        }
	td.middleGrid{
	   border-left: 1pt solid #888888;	   
	   border-right: 1pt solid #888888;
           text-align: center;
	}	
</style>
</head>

<body class="BodyStyle" vlink="#0000FF" onLoad="setValues()" >
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                billing
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
						Adjust Private Billing Codes
                        </td>
                        <td  >&nbsp;
							
                        </td>
                        <td style="text-align:right">
                                <a href="javascript:popupStart(300,400,'Help.jsp')"  ><bean:message key="global.help" /></a> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about" /></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license" /></a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn" valign="top">&nbsp;
            <a href="billingAddPrivateCode.jsp?addNew=true">Add Code</td>
            <td class="MainTableRightColumn">
                <form action="billingPrivateCodeAdjust.jsp"  method="get">                
                <%if (request.getAttribute("returnMessage") != null){%>
                  
                <table>
                    <tr>
                    <td style="font-color: red;"><%=request.getAttribute("returnMessage")%></td>
                    </tr>
                </table>
                
                <%}%>
                
                <table>               
                    <tr>
                        <td>
                        </td>
                    </tr>
                </table>
                </form>
                <%BillingCodeData bcds = new BillingCodeData();
                  ArrayList list = (ArrayList) bcds.findBillingCodesByCode("P");
                  if (list != null){ %>
                  
                <table border=1>
                <tr>
                    
                    <td>code</td>
                    <td>desc</td>
                    <td>price</td>
                </tr>
                <%for(int i = 0; i < list.size(); i ++){ 
                    BillingCodeData bcd = (BillingCodeData) list.get(i);
                    
                %>
                
                  <tr>
                     <td><a href="billingEditCode.jsp?codeId=<%=bcd.getBillingserviceNo()%>&code=<%=bcd.getServiceCode()%>&desc=<%=bcd.getDescription()%>&value=<%=bcd.getValue()%>&whereTo=private">
                           <%=bcd.getServiceCode()%>
                         </a>
                     </td>                    
                     <td><%=bcd.getDescription()%></td>
                     <td><%=bcd.getValue()%></td>
                  </tr>
                <%}%>
                </table>
                
                 <%}%>
                
			   </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">

            </td>
            <td class="MainTableBottomRowRightColumn">

            </td>
        </tr>
    </table>
</body>
</html:html>
