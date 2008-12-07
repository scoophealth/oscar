package com.quatro.web.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.navigator.menu.MenuComponent;
import net.sf.navigator.menu.MenuRepository;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.web.admin.BaseAdminAction;

import com.quatro.common.KeyConstants;
import com.quatro.model.LookupCodeValue;
import com.quatro.model.LookupTableDefValue;
import com.quatro.model.security.NoAccessException;
import com.quatro.service.ORGManager;

public class ShowORGTreeAction extends BaseAdminAction {
	private ORGManager orgManager = null;

	public ORGManager getOrgManager() {
		return orgManager;
	}

	public void setOrgManager(ORGManager orgManager) {
		this.orgManager = orgManager;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return tree(mapping, form, request, response);
	}

	private ActionForward tree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_ORG);
			String tableId = request.getParameter("tableId");
			
			LookupTableDefValue tableDef = orgManager.GetLookupTableDef(tableId);
	
			List lst = orgManager.LoadCodeList(tableId, true, null, null);
		
			MenuRepository repository = setMenu(lst, request);
	
			DynaActionForm qform = (DynaActionForm) form;
			qform.set("tableDef", tableDef);
			qform.set("tree", repository);
	
			return mapping.findForward("tree");
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}
	}

	private MenuRepository setMenu(List lst, HttpServletRequest request) {
		MenuRepository repository = new MenuRepository();
		MenuRepository defaultRepository = (MenuRepository) getServlet()
				.getServletContext().getAttribute(
						MenuRepository.MENU_REPOSITORY_KEY);
		repository.setDisplayers(defaultRepository.getDisplayers());

		String inactiveCodeTree = "";
		boolean hideInactive = false;
		for (int i = 0; i < lst.size(); i++) {
			MenuComponent mc = new MenuComponent();
			LookupCodeValue obj = (LookupCodeValue) lst.get(i);
			
			if(inactiveCodeTree.length()>0 && !obj.getCodeTree().startsWith(inactiveCodeTree)){
				hideInactive = false;
			}			
			
			if(hideInactive == false && obj.isActive() == false){
				inactiveCodeTree = obj.getCodeTree();
				hideInactive = true;
			}

			
			if(hideInactive == false){
				String name = obj.getCodeTree();
				mc.setName(name);
				String parent = null;
				if (name.length() > 8)
					parent = name.substring(0, name.length() - 8);
				if (parent != null) {
					MenuComponent parentMenu = repository.getMenu(parent);
					if (parentMenu == null) {
						parentMenu = new MenuComponent();
						parentMenu.setName(parent);
						repository.addMenu(parentMenu);
					}
	
					mc.setParent(parentMenu);
				}
				String title = obj.getDescription();
				mc.setTitle(title);
	
				String location = "javascript:void(0);";
	
				mc.setLocation(location);
	
				repository.addMenu(mc);
			}
		}

		return repository;
	}
}
