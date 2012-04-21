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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@page import="oscar.oscarRx.data.RxDrugData,java.util.*" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Calendar" %>
<%@page import="oscar.oscarRx.data.RxPrescriptionData" %>
<%@page import="oscar.oscarRx.util.*" %>
<%
         Hashtable hiddenResources = (Hashtable) request.getSession().getAttribute("hideResources");
         if(hiddenResources!=null){
         int numHidden=hiddenResources.size();
         if(numHidden>0){
             %>  <script type="text/javascript">
                    if(showOrHide==1)$('showHideWord').update('hide');
                    else $('showHideWord').update('show');
             </script>
<div id="">
    <a href="javascript:void(0);" style="font-size: 9pt;" onclick="showOrHideRes('<%=hiddenResources%>');"> <span id="showHideWord">show</span> <span id="showHideNumber"><%=numHidden%></span> hidden resources</a>
</div>
            <%                      }
         }
         %>
