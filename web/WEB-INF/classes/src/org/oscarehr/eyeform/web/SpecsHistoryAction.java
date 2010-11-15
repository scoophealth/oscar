package org.oscarehr.eyeform.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.eyeform.dao.SpecsHistoryDao;
import org.oscarehr.eyeform.model.SpecsHistory;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class SpecsHistoryAction extends DispatchAction {
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
    	SpecsHistoryDao dao = (SpecsHistoryDao)SpringUtils.getBean("SpecsHistoryDAO");
    	
    	request.setAttribute("providers",providerDao.getActiveProviders());
    	
    	DynaValidatorForm f = (DynaValidatorForm)form;
    	SpecsHistory specs = (SpecsHistory)f.get("specs");
    	if(specs.getId() != null && specs.getId().intValue()>0) {
    		specs = dao.find(specs.getId()); 
    	}
    	
    	f.set("specs", specs);
    	
        return mapping.findForward("form");
    }
    
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	DynaValidatorForm f = (DynaValidatorForm)form;
    	SpecsHistory specs = (SpecsHistory)f.get("specs");
    	if(specs.getId()!=null && specs.getId()==0) {
    		specs.setId(null);
    	}
    	SpecsHistoryDao dao = (SpecsHistoryDao)SpringUtils.getBean("SpecsHistoryDAO");
    	specs.setProvider(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());	
    	dao.save(specs);
    	
    	return mapping.findForward("success");
    }

   
    public ActionForward copySpecs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String demographicNo = request.getParameter("demographicNo");
    	SpecsHistoryDao dao = (SpecsHistoryDao)SpringUtils.getBean("SpecsHistoryDAO");
    	List<SpecsHistory> specs = dao.getByDemographicNo(Integer.parseInt(demographicNo));
    	if(specs.size()>0) {
    		SpecsHistory latestSpecs = specs.get(0);
    		PrintWriter out = response.getWriter();
    		out.println("setfieldvalue(\"od_manifest_refraction_sph\",\""+latestSpecs.getOdSph()+"\");");
    		out.println("setfieldvalue(\"os_manifest_refraction_sph\",\""+latestSpecs.getOsSph()+"\");");
    		out.println("setfieldvalue(\"od_manifest_refraction_cyl\",\""+latestSpecs.getOdCyl()+"\");");
    		out.println("setfieldvalue(\"os_manifest_refraction_cyl\",\""+latestSpecs.getOsCyl()+"\");");
    		out.println("setfieldvalue(\"od_manifest_refraction_axis\",\""+latestSpecs.getOdAxis()+"\");");
    		out.println("setfieldvalue(\"os_manifest_refraction_axis\",\""+latestSpecs.getOsAxis()+"\");");    		
    	} else {
    		PrintWriter out = response.getWriter();
    		out.println("alert('No Specs Found.');");
    	}
    	return null;
    }
}
