/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.renal.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.velocity.VelocityContext;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.Provider;
import org.oscarehr.renal.CkdScreener;
import org.oscarehr.renal.ORNCkdScreeningReportThread;
import org.oscarehr.renal.ORNPreImplementationReportThread;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.OscarAuditLogger;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.VelocityUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import oscar.OscarProperties;
import oscar.form.FrmLabReq07Record;
import oscar.form.FrmLabReq10Record;

public class RenalAction extends DispatchAction {

	private DxresearchDAO dxResearchDao = (DxresearchDAO)SpringUtils.getBean("DxresearchDAO");
	private MeasurementDao measurementDao = SpringUtils.getBean(MeasurementDao.class);
	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	private ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	
	static String labReqVersion;
	static {
		labReqVersion = OscarProperties.getInstance().getProperty("orn_labreqver", "07");
		if(labReqVersion == "") {
			labReqVersion="10";
		}
	}
	
	public ActionForward checkForDx(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String demographicNo = request.getParameter("demographicNo");
		String codingSystem = request.getParameter("codingSystem");
		String code = request.getParameter("code");
		
		boolean exists = dxResearchDao.activeEntryExists(Integer.parseInt(demographicNo), codingSystem, code);
		
		String str = "{'result':"+exists+"}";  
		JSONObject jsonArray = (JSONObject) JSONSerializer.toJSON(str);
        response.setContentType("text/x-json");
        try {
        	jsonArray.write(response.getWriter());
        }catch(IOException e) {
        	MiscUtils.getLogger().error("Error",e);
        }

		return null;
	}
	
	public ActionForward addtoDx(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String demographicNo = request.getParameter("demographicNo");
		String codingSystem = request.getParameter("codingSystem");
		String code = request.getParameter("code");
		
		List<Dxresearch> items = dxResearchDao.find(Integer.parseInt(demographicNo), codingSystem, code);
		Integer id = null;
		for(Dxresearch item:items) {
			if(item.getStatus() != 'A') {
				item.setStatus('A');
				dxResearchDao.merge(item);
				id=item.getDxresearchNo();
				break;
			}
		}
		if(id == null) {
			Dxresearch item = new Dxresearch();
			item.setAssociation((byte)0);
			item.setCodingSystem(codingSystem);
			item.setDemographicNo(Integer.parseInt(demographicNo));
			item.setDxresearchCode(code);
			item.setStartDate(new Date());
			item.setStatus('A');
			item.setUpdateDate(new Date());
			item.setProviderNo(LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo());
			dxResearchDao.persist(item);
			id = item.getDxresearchNo();
		}
		
		String str = "{'result':"+id+"}";  
		JSONObject jsonArray = (JSONObject) JSONSerializer.toJSON(str);
        response.setContentType("text/x-json");
        try {
        	jsonArray.write(response.getWriter());
        }catch(IOException e) {
        	MiscUtils.getLogger().error("Error",e);
        }

		return null;
	}
	
	public ActionForward getNextSteps(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String demographicNo = request.getParameter("demographicNo");
		
		CkdScreener screener = new CkdScreener();
		List<String> reasons = new ArrayList<String>();
		boolean match = screener.screenDemographic(Integer.parseInt(demographicNo), reasons, null);
		
		String nextSteps = "N/A";
		if(match)
			nextSteps  = "Screen patient for CKD<br/>using eGFR, ACR, and BP";
		
		//get tests
		List<Measurement> egfrs = measurementDao.findByType(Integer.parseInt(demographicNo), "EGFR");
		List<Measurement> acrs = measurementDao.findByType(Integer.parseInt(demographicNo), "ACR");
		Date latestEgfrDate = null;
		
		
		Double latestEgfr = null;
		Double aYearAgoEgfr = null;
		if(egfrs.size()>0) {
			latestEgfr = Double.valueOf(egfrs.get(0).getDataField());
			latestEgfrDate = egfrs.get(0).getDateObserved();
		}
		Double latestAcr = null;
		if(acrs.size()>0) {
			latestAcr = Double.valueOf(acrs.get(0).getDataField());
		}
		if(latestEgfrDate != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(latestEgfrDate);
			cal.add(Calendar.MONTH, -12);
			Date aYearBefore = cal.getTime();
			
			//do we have any egfrs from before this date?
			List<Measurement> tmp = measurementDao.findByTypeBefore(Integer.parseInt(demographicNo), "EGFR",aYearBefore);
			if(tmp.size()>0) {
				Measurement m = tmp.get(0);
				aYearAgoEgfr = Double.valueOf(m.getDataField());
			}
		}
		
		if((latestEgfr != null && latestEgfr < 30) || (latestAcr != null && latestAcr >= 60)) {
			nextSteps = "<a href=\"javascript:void();\" onclick=\"window.open('"+request.getContextPath()+"/oscarEncounter/oscarConsultationRequest/ConsultationFormRequest.jsp?de="+demographicNo+"&teamVar=','Consultation"+demographicNo+"','width=960,height=700');return false;\">Refer to Nephrology</a>";
		}
		
		if((latestAcr != null && latestAcr > 2.8 && latestAcr < 60) && (latestEgfr != null && latestEgfr > 30)) {
			nextSteps = "Check ACR/eGFR q6m for<br/> 2 years, and if stable, yearly";
		}
		
		if((latestAcr != null && latestAcr <= 2.8) && (latestEgfr != null && latestEgfr >= 30 && latestEgfr <= 59)) {
			nextSteps = "Check ACR/eGFR q6m for<br/> 2 years, and if stable, yearly";
		}
		
		if((latestAcr != null && latestAcr <= 2.8) && (latestEgfr != null && latestEgfr > 60)) {
			nextSteps = "Check ACR/eGFR yearly";
		}
		
		if(latestEgfr != null && aYearAgoEgfr != null) {
			if((aYearAgoEgfr.doubleValue() - latestEgfr.doubleValue()) > 20) {
				nextSteps = "Check ACR, and if drop pesistent, <a href=\"javascript:void();\" onclick=\"window.open('"+request.getContextPath()+"/oscarEncounter/oscarConsultationRequest/ConsultationFormRequest.jsp?de="+demographicNo+"&teamVar=','Consultation"+demographicNo+"','width=960,height=700');return false;\">Refer to Nephrology</a>";
			}
		}
		
		String str = "{'result':'"+StringEscapeUtils.escapeJavaScript(nextSteps)+"'}";
		JSONObject jsonArray = (JSONObject) JSONSerializer.toJSON(str);
        response.setContentType("text/x-json");
        try {
        	jsonArray.write(response.getWriter());
        }catch(IOException e) {
        	MiscUtils.getLogger().error("Error",e);
        }
        
		return null;
	}
	
	public ActionForward generatePatientLetter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

		String demographicNo = request.getParameter("demographic_no");
		response.setContentType("text/html");
		try {
			String documentDir = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR","");
			File f = new File(documentDir,"orn_patient_letter.txt");
	        String template=IOUtils.toString(new FileInputStream(f));
	        
	        Demographic d = demographicDao.getDemographic(demographicNo);
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	        VelocityContext velocityContext=VelocityUtils.createVelocityContextWithTools();            
	        velocityContext.put("patient", d);
	        velocityContext.put("currentDate", sdf.format(new Date()));
	        if(d.getProviderNo() != null && d.getProviderNo().length()>0) {
	        	velocityContext.put("mrp", providerDao.getProvider(d.getProviderNo()));
	        } else {
	        	velocityContext.put("mrp",OscarProperties.getInstance().getProperty("orn.default_mrp",""));
	        }
	       
	        
	       String letter=VelocityUtils.velocityEvaluate(velocityContext, template);
	       
	       PrintWriter out = response.getWriter();
	       out.println(letter);
		}catch(IOException e) {
			MiscUtils.getLogger().error("Error",e);
		} finally {
			OscarAuditLogger.getInstance().log(loggedInInfo, "create", "CkdPatientLetter", Integer.valueOf(demographicNo), "");
		}
        
		return null;
	}
	
	public ActionForward createLabReq(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  throws SQLException {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		Integer demographicNo = Integer.parseInt(request.getParameter("demographicNo"));
		
		if(labReqVersion.equals("07")) {
			FrmLabReq07Record lr = new FrmLabReq07Record();
			Properties p = lr.getFormRecord(loggedInInfo, demographicNo, 0);		
			p = lr.getFormCustRecord(loggedInInfo, loggedInInfo.getCurrentFacility(), p, loggedInInfo.getLoggedInProviderNo());
			p.setProperty("b_creatinine","checked=\"checked\"");	
			p.setProperty("b_acRatioUrine","checked=\"checked\"");	
			p.setProperty("b_urinalysis","checked=\"checked\"");	
			request.getSession().setAttribute("labReq07"+demographicNo,p);
		}else {
			FrmLabReq10Record lr = new FrmLabReq10Record();
			Properties p = lr.getFormRecord(loggedInInfo, demographicNo, 0);		
			p = lr.getFormCustRecord(p, loggedInInfo.getLoggedInProviderNo());
			p.setProperty("b_creatinine","checked=\"checked\"");	
			p.setProperty("b_acRatioUrine","checked=\"checked\"");	
			p.setProperty("b_urinalysis","checked=\"checked\"");	
			request.getSession().setAttribute("labReq10"+demographicNo,p);
		}
	
		OscarAuditLogger.getInstance().log(loggedInInfo, "create", "CkdLabReq", demographicNo, "");

		return null;
	}
	
	public ActionForward sendPatientLetterAsEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

		String demographicNo = request.getParameter("demographic_no");
		String error = "";
		boolean success=true;
		JSONObject json = new JSONObject();
		
		final Demographic d = demographicDao.getDemographic(demographicNo);
        
		if(d == null) {
			error = "Patient not found.";
			success=false;
		}
		if(d.getEmail() == null || d.getEmail().length() == 0 || d.getEmail().indexOf("@") == -1) {
			error = "No valid email address found for patient.";
			success=false;
		}
		
		if(success) {
		
			try {
				String documentDir = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR","");
				File f = new File(documentDir,"orn_patient_letter.txt");
		        String template=IOUtils.toString(new FileInputStream(f));
		        
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		        VelocityContext velocityContext=VelocityUtils.createVelocityContextWithTools();            
		        velocityContext.put("patient", d);
		        velocityContext.put("currentDate", sdf.format(new Date()));
		        Provider mrp = null;
		        if(d.getProviderNo() != null && d.getProviderNo().length()>0) {
		        	mrp = providerDao.getProvider(d.getProviderNo());	
		        } else {
		        	mrp = providerDao.getProvider(OscarProperties.getInstance().getProperty("orn.default_mrp",""));
		        }
		        velocityContext.put("mrp", mrp);
		       
		        final String mrp1 = mrp.getFullName();
		        
		       final String letter=VelocityUtils.velocityEvaluate(velocityContext, template);
		       
		       JavaMailSender mailSender = (JavaMailSender) SpringUtils.getBean("mailSender");
		       
		       MimeMessagePreparator preparator = new MimeMessagePreparator() {
		           public void prepare(MimeMessage mimeMessage) throws Exception {
		              MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
		              message.setTo(d.getEmail());
		              message.setSubject(OscarProperties.getInstance().getProperty("orn.email.subject", "Important Message from " +  mrp1));
		              message.setFrom(OscarProperties.getInstance().getProperty("orn.email.from","no-reply@oscarmcmaster.org"));         
		              message.setText(letter, true);
		           }
		        };
		        mailSender.send(preparator);
		       
			}catch(IOException e) {
				MiscUtils.getLogger().error("Error",e);
				success=false;
				error=e.getMessage();
			} finally {
				OscarAuditLogger.getInstance().log(loggedInInfo, "create", "CkdPatientLetter", Integer.valueOf(demographicNo), "");
				OscarAuditLogger.getInstance().log(loggedInInfo, "email", "CkdPatientLetter", Integer.valueOf(demographicNo), "");
			}
		}
		json.put("success", String.valueOf(success));
		json.put("error", error);
		try {
			json.write(response.getWriter());
		}catch(IOException e) {
			MiscUtils.getLogger().error("error",e);
		}
		return null;
	}
	
	public ActionForward submitPreimplementationReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		
		ORNPreImplementationReportThread t = new ORNPreImplementationReportThread();
		t.setProviderNo(providerNo);
		t.start();
		
		
		return null;
		
	}
	
	public ActionForward submitCkdScreeningReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		
		ORNCkdScreeningReportThread t = new ORNCkdScreeningReportThread();
		t.setProviderNo(providerNo);
		t.start();
		
		
		return null;
		
	}
}
