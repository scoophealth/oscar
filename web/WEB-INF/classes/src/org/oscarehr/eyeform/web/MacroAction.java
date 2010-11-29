package org.oscarehr.eyeform.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.eyeform.dao.MacroDao;
import org.oscarehr.eyeform.model.Macro;
import org.oscarehr.util.SpringUtils;

public class MacroAction extends DispatchAction {

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }
	
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		MacroDao dao = (MacroDao)SpringUtils.getBean("MacroDAO");
		List<Macro> macros = dao.getAll();
		request.setAttribute("macros", macros);
        return mapping.findForward("list");
    }
	
	public ActionForward addMacro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {		
        return form(mapping, form, request, response);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
    	MacroDao dao = (MacroDao)SpringUtils.getBean("MacroDAO");
		
    	request.setAttribute("providers",providerDao.getActiveProviders());
    	
    	DynaValidatorForm f = (DynaValidatorForm)form;
    	
    	Macro macro = (Macro)f.get("macro");
    	if(macro.getId() != null && macro.getId().intValue()>0) {
    		macro = dao.find(macro.getId()); 
    	}
    	
    	f.set("macro", macro);
    	
        return mapping.findForward("form");
    }
    
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	DynaValidatorForm f = (DynaValidatorForm)form;
    	
    	Macro macro = (Macro)f.get("macro");
    	
    	MacroDao dao = (MacroDao)SpringUtils.getBean("MacroDAO");
    	
    	dao.save(macro);
    	
    	return mapping.findForward("success");
    }

}
