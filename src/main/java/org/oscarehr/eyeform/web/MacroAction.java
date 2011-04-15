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

	MacroDao dao = (MacroDao)SpringUtils.getBean("MacroDAO");
	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	
	
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }
	
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
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
    	
    	request.setAttribute("providers",providerDao.getActiveProviders());
    	
    	if(request.getParameter("macro.id") != null) {
    		int macroId = Integer.parseInt(request.getParameter("macro.id"));
    		Macro macro = dao.find(macroId);
    		DynaValidatorForm f = (DynaValidatorForm)form;	
    		f.set("macro", macro);
    	}
    	
        return mapping.findForward("form");
    }
    
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	DynaValidatorForm f = (DynaValidatorForm)form;
    	
    	Macro macro = (Macro)f.get("macro");    	
    	
    	if(request.getParameter("macro.id") != null && request.getParameter("macro.id").length()>0) {    		
    		macro.setId(Integer.parseInt(request.getParameter("macro.id")));
    	}
    	
    	if(macro.getId() != null && macro.getId() == 0) {
    		macro.setId(null);
    	}
    	
    	if(macro.getId() == null) {
    		dao.persist(macro);
    	} else {
    		dao.merge(macro);
    	}
    	
    	request.setAttribute("parentAjaxId", "macro");
    	return mapping.findForward("success");
    }

    public ActionForward deleteMacro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	String[] ids = request.getParameterValues("selected_id");
    	for(int x=0;x<ids.length;x++) {
    		dao.remove(Integer.parseInt(ids[x]));
    	}
        return list(mapping,form,request,response);
    }
}
