<%--
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
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
--%>
<%@page import="java.util.*,net.sf.json.*,java.io.*,org.apache.xmlrpc.*,oscar.oscarRx.util.*,oscar.oscarRx.data.*"  %><%
System.out.println("In getAllerfyData");
String atcCode =  request.getParameter("atcCode");
String id = request.getParameter("id");

oscar.oscarRx.pageUtil.RxSessionBean rxSessionBean = (oscar.oscarRx.pageUtil.RxSessionBean) session.getAttribute("RxSessionBean");
oscar.oscarRx.data.RxPatientData.Patient.Allergy[] allergies = new oscar.oscarRx.data.RxPatientData().getPatient(rxSessionBean.getDemographicNo()).getAllergies();

oscar.oscarRx.data.RxPatientData.Patient.Allergy[] allergyWarnings = null;
                RxDrugData drugData = new RxDrugData();
                allergyWarnings = drugData.getAllergyWarnings(atcCode, allergies);
                System.out.println("allg size"+allergyWarnings.length);
                

    Hashtable d = new Hashtable();

    d.put("id",id);
    for(oscar.oscarRx.data.RxPatientData.Patient.Allergy allg:allergyWarnings){
                    System.out.println(">>>>>>>>>>>> "+allg.getAllergy().getDESCRIPTION());
                     d.put("alleg",allg.getAllergy());
                 }
   
    response.setContentType("text/x-json");
    JSONObject jsonArray = (JSONObject) JSONSerializer.toJSON( d );
    jsonArray.write(out);
%>
