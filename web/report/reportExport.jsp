<%!
 /**
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
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of LabTag
 *
 *
 * reportExport.jsp
 *
 * Created on May 4, 2005, 11:15 AM
 */
 %><%@page contentType="application/octet-stream"%><%@page  import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*,oscar.oscarProvider.data.*,oscar.util.*,oscar.oscarReport.ClinicalReports.*"%><%
   String filename = "report.txt";

     response.addHeader("Content-Disposition", "attachment;filename=" + filename);  
   
     ArrayList arrList = (ArrayList) session.getAttribute("ClinicalReports"); 
     if (arrList != null){
        String id = request.getParameter("id");
        ReportEvaluator re = (ReportEvaluator) arrList.get(Integer.parseInt(id));
        out.write(re.getCSV());
     }else{
        out.write("ERROR:No report found.");
     }
     }
%>