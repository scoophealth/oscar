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

<%@page import="org.oscarehr.common.model.EForm"%>
<%@page import="org.oscarehr.common.dao.EFormDao"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.model.EFormData"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="org.oscarehr.common.model.EFormGroup"%>
<%@page import="org.oscarehr.common.dao.EFormDataDao"%>
<%@page import="org.oscarehr.common.dao.EFormGroupDao"%>
<%@page import="oscar.OscarProperties"%>
<%@page import="org.oscarehr.common.dao.LabRequestReportLinkDao"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_lab" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_lab");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="oscar.util.ConversionUtils"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.forms.FormsDao"%>
<%@page	import="
		java.util.Date,
		java.util.Hashtable,
		java.util.Vector,
		oscar.oscarLab.LabRequestReportLink,
		oscar.util.UtilDateUtilities"%>
<%
	LabRequestReportLinkDao linkDao = SpringUtils.getBean(LabRequestReportLinkDao.class);
	EFormGroupDao eformGroupDao = SpringUtils.getBean(EFormGroupDao.class);
	EFormDataDao eformDataDao = SpringUtils.getBean(EFormDataDao.class);
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	EFormDao eformDao = SpringUtils.getBean(EFormDao.class);


    String table = request.getParameter("table"); //can be hl7TextMessage or labPatientPhysicianInfo (CML)
    String rptId = request.getParameter("rptid"); //this is the lab no.
    String reqId = request.getParameter("reqid"); //already has a link
    String linkReqId = request.getParameter("linkReqId"); //internal - make the link
    String demographicNo = request.getParameter("demographicNo");
    
    String sql = null;
    String reqDateLink = "";
    
    Vector<String> req_id      = new Vector<String>();
    Vector<String> formCreated = new Vector<String>();
    Vector<String> patientName = new Vector<String>();
    Vector<String> formDisplayName = new Vector<String>();
    Vector<String> formName = new Vector<String>(); //formname or eformname
    Vector<Integer> formType = new Vector<Integer>(); //1=form, 2=eform
    
    org.oscarehr.common.model.LabRequestReportLink existingLink = null;
    
    boolean close = false;
    
    FormsDao dao = SpringUtils.getBean(FormsDao.class);
    if (linkReqId == null || linkReqId.length()==0) {
		if(reqId != null && reqId.length()>0) {
			existingLink = linkDao.find(Integer.parseInt(reqId));
	    	reqDateLink = LabRequestReportLink.getRequestDate(reqId);
		}
	    for(Object[] f : dao.findIdFormCreatedAndPatientNameFromFormLabReq07(demographicNo)) {
	   	  	Integer id = (Integer) f[0];
		  	Date frmCreated = (Date) f[1];
		   	String name = (String) f[2];
	    	
			req_id.add("form:formLabReq07:" + id);
			patientName.add(name);
			formCreated.add(UtilDateUtilities.DateToString(frmCreated, "yyyy-MM-dd"));
			formDisplayName.add("Lab Req 2007");
			formName.add("formLabReq07");
			formType.add(1);
	    }		
	    
	    for(Object[] f : dao.findIdFormCreatedAndPatientNameFromFormLabReq10(demographicNo)) {
	   	  	Integer id = (Integer) f[0];
		  	Date frmCreated = (Date) f[1];
		   	String name = (String) f[2];
	    	
			req_id.add("form:formLabReq10:" + id);
			patientName.add(name);
			formCreated.add(UtilDateUtilities.DateToString(frmCreated, "yyyy-MM-dd"));
			formDisplayName.add("Lab Req 2010");
			formName.add("formLabReq10");
			formType.add(1);
	    }		
	    
	    String eformGroupName = OscarProperties.getInstance().getProperty("lab_req_eform_group","");
	    
	    if(!StringUtils.isEmpty(eformGroupName)) {
	    	for(EFormGroup eformGroupItem : eformGroupDao.getByGroupName(eformGroupName)) {
	    		int formId = eformGroupItem.getFormId();
	    		if(formId > 0) {
	    			EForm eform = eformDao.find(formId);
	    		
	    			
	    			if(demographicNo != null) {
		    			for(EFormData eformData :  eformDataDao.findByDemographicIdAndFormId(Integer.parseInt(demographicNo), formId)) {
		    				req_id.add("eform:"+formId+":" + eformData.getId());
		    				Demographic d = demographicDao.getDemographicById(eformData.getDemographicId());
		    				patientName.add(d.getFormattedName());
		    				formCreated.add(UtilDateUtilities.DateToString(eformData.getFormDate(), "yyyy-MM-dd"));
		    				formDisplayName.add(eform.getFormName());
		    				formName.add(String.valueOf(formId));
		    				formType.add(2);		    			
		    			}
	    			} else {
	    				for(Integer fdid :  eformDataDao.findAllFdidByFormId(formId) ) {
	    					EFormData eformData = eformDataDao.find(fdid);
		    				req_id.add("eform:"+formId+":" + eformData.getId());
		    				Demographic d = demographicDao.getDemographicById(eformData.getDemographicId());
		    				patientName.add(d.getFormattedName());
		    				formCreated.add(UtilDateUtilities.DateToString(eformData.getFormDate(), "yyyy-MM-dd"));
		    				formDisplayName.add(eform.getFormName());
		    				formName.add(String.valueOf(formId));
		    				formType.add(2);		    			
		    			}
	    			}
	    			
	    		}
	    	}
	    }
	    
	    
    } else { //Make the link
		
    	if(linkReqId.equals("-1")) {
    		LabRequestReportLink.delete(table,Long.valueOf(rptId));
    	} else {
    		String parts[] = linkReqId.split(":");
    		
    		if(parts[0].equals("form")) {
			    String req_date = "";
			    if(parts[0].equals("form") && parts[1].equals("formLabReq07")) {
				    for(Object o : dao.findFormCreatedFromFormLabReq07ById(ConversionUtils.fromIntString(parts[2]))) { 
				    	req_date = UtilDateUtilities.DateToString((Date) o,"yyyy-MM-dd");
				    }
			    } else if (parts[0].equals("form") && parts[1].equals("formLabReq10")) {
			    	for(Object o : dao.findFormCreatedFromFormLabReq10ById(ConversionUtils.fromIntString(parts[2]))) { 
				    	req_date = UtilDateUtilities.DateToString((Date) o,"yyyy-MM-dd");
				    }
			    }
			    Long id = LabRequestReportLink.getIdByReport(table, Long.valueOf(rptId));
			    if (id==null) { //new report
					LabRequestReportLink.save(parts[1],Long.valueOf(parts[2]),req_date,table,Long.valueOf(rptId));
			    } else {
					LabRequestReportLink.update(id,parts[1],Long.valueOf(parts[2]),req_date);
			    }
    		} else if(parts[0].equals("eform")) {
    			EFormData eformData = eformDataDao.find(Integer.parseInt(parts[2]));
    			String req_date = UtilDateUtilities.DateToString(eformData.getFormDate(),"yyyy-MM-dd");
    			Long id = LabRequestReportLink.getIdByReport(table, Long.valueOf(rptId));
    			 if (id==null) { //new report
 					LabRequestReportLink.save("eform_data",Long.valueOf(parts[2]),req_date,table,Long.valueOf(rptId));
 			    } else {
 					LabRequestReportLink.update(id,"eform_data",Long.valueOf(parts[2]),req_date);
 			    }
    		}
		    
    	}
    	close = true;
		//response.sendRedirect("../close.html");
    }
%>


<%@page import="org.oscarehr.util.MiscUtils"%><html>
    <head>
        <title>Link to Lab Requisition</title>
        <script>
        function closeItUp() {
        	window.opener.refresh();
        	window.close();
        }
        </script>
    </head>
    <body <%=(close) ? "onLoad=\"closeItUp()\" " : "" %>>

    <form action="LinkReq.jsp" method="post">
	<input type="hidden" name="table" value="<%=table%>" />
	<input type="hidden" name="rptid" value="<%=rptId%>" />
	<input type="hidden" name="reqid" value="<%=reqId%>" />
	
	<p>&nbsp;</p>
	Requisition Date: <%=reqDateLink%><p>
	Link to Lab Requisition:
	<select name="linkReqId">
	    <option value="-1">---</option>
<%
	String matchingId = null;
	if(existingLink != null) {
		if("eform_data".equals(existingLink.getRequestTable())) {
			EFormData eformData = eformDataDao.find(existingLink.getRequestId());
			matchingId = "eform:" + eformData.getFormId() + ":" + existingLink.getRequestId();
			
		} else {
			matchingId = "form:" + existingLink.getRequestTable() + ":" + existingLink.getRequestId();
		}
	}
	
    for (int i=0; i<req_id.size(); i++) {
%>
	    <option value="<%=req_id.get(i)%>" <%=req_id.get(i).equals(matchingId)?"selected":""%>><%=formDisplayName.get(i)%> : <%=formCreated.get(i)%> : <%=patientName.get(i)%></option>
<%  } %>
	</select><p>
	<input type="submit" value="Link" />
	<input type="button" value="Cancel" onclick="window.close();" />
    </form>
    
    
    </body>
</html>
