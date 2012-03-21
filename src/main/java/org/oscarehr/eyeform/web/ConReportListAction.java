package org.oscarehr.eyeform.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.eyeform.dao.ConsultationReportDao;
import org.oscarehr.eyeform.model.EyeformConsultationReport;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ConReportListAction extends DispatchAction {

	Logger logger = MiscUtils.getLogger();
	
	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	DemographicDao demographicDao= (DemographicDao)SpringUtils.getBean("demographicDao");
	ConsultationReportDao crDao = (ConsultationReportDao)SpringUtils.getBean("consultationReportDao");
	
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("unspecified");
		return list(mapping, form, request, response);
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		DynaValidatorForm testForm = (DynaValidatorForm) form;
		ConsultationReportFormBean crBean = (ConsultationReportFormBean)testForm.get("cr");
		
		EyeformConsultationReport cr = new EyeformConsultationReport();
		if(crBean.getStatus() != null && crBean.getStatus().length()>0) {
			cr.setStatus(crBean.getStatus());
		}
		if(crBean.getProviderNo() != null && crBean.getProviderNo().length()>0) {
			cr.setProviderNo(crBean.getProviderNo());
		}
		
		if(crBean.getDemographicNo() != null && crBean.getDemographicNo().length()>0) {
			cr.setDemographicNo(Integer.parseInt(crBean.getDemographicNo()));
		}
		
		Date startDate = null;
		Date endDate = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		if(crBean.getStartDate() != null && crBean.getStartDate().length()>0) {
			try {
				startDate = formatter.parse(crBean.getStartDate());
			}catch(ParseException e) {
				logger.error("Error",e);
			}
		}
		if(crBean.getEndDate() != null && crBean.getEndDate().length()>0) {
			try {
				endDate = formatter.parse(crBean.getEndDate());
			}catch(ParseException e) {
				logger.error("Error",e);
			}
		}
		if(startDate == null && endDate == null) {
			endDate=new Date();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR,-1);
			startDate = cal.getTime();
		}
		
		
		List<Provider> pl = providerDao.getActiveProviders();
		crBean.setProviderList(pl);

		List<EyeformConsultationReport> results = crDao.search(cr,startDate,endDate);
		for(EyeformConsultationReport crtmp:results) {
			crtmp.setDemographic(demographicDao.getClientByDemographicNo(crtmp.getDemographicNo()));
			crtmp.setProvider(providerDao.getProvider(crtmp.getProviderNo()));
		}
		request.setAttribute("conReportList",results);
				
		request.setAttribute("dmname", crBean.getDemographicName());
		
		return mapping.findForward("list");
	}
}
