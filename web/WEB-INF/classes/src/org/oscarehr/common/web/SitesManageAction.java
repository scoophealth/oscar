package org.oscarehr.common.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.SiteDao;
import org.oscarehr.common.model.Site;

public class SitesManageAction extends DispatchAction {

    private SiteDao siteDao;

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return view(mapping, form, request, response);
    }

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Site> sites = siteDao.getAllSites();
        
        request.setAttribute("sites", sites);    	
        return mapping.findForward("list");
    }

    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	DynaBean lazyForm = (DynaBean) form;

    	Site s = new Site();
    	lazyForm.set("site", s);
        
        return mapping.findForward("details");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	DynaBean lazyForm = (DynaBean) form;
    	
    	Site s = (Site) lazyForm.get("site"); 
    	
    	// verify mandatories
    	if (StringUtils.isBlank(s.getName())||StringUtils.isBlank(s.getShortName())) {
   			ActionMessages errors = this.getErrors(request);
 			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.required", "Site name or short name"));
    		this.saveErrors(request, errors);
    	} 
    	if (StringUtils.isBlank(s.getBgColor())) {
   			ActionMessages errors = this.getErrors(request);
 			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.required", "Theme color"));
    		this.saveErrors(request, errors);
    	}
    	
    	if (this.getErrors(request).size()>0)
    		return mapping.findForward("details");    		

    	
    	siteDao.save(s);
        
        return view(mapping, form, request, response);
    }
    
    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	DynaBean lazyForm = (DynaBean) form;

    	String siteId = request.getParameter("siteId");
        Site s = siteDao.getById(new Integer(siteId));
    	
        lazyForm.set("site", s);
        return mapping.findForward("details");
    }

	public void setSiteDao(SiteDao siteDao) {
		this.siteDao = siteDao;
	}

   
}