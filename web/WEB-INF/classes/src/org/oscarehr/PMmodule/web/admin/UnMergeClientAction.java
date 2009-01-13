package org.oscarehr.PMmodule.web.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.oscarehr.PMmodule.model.Program;

import org.oscarehr.PMmodule.service.MergeClientManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;

import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;



import com.quatro.common.KeyConstants;
import com.quatro.model.security.NoAccessException;
import com.quatro.service.LookupManager;

public class UnMergeClientAction extends BaseAdminAction {
	

	private ProviderManager providerManager;

	private ProgramManager programManager;	
	private LookupManager lookupManager;
	private MergeClientManager mergeClientManager;
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {	
		//return search(mapping, form, request, response);
		setLookupLists(request);
		return mergedSearch(mapping, form, request, response);
	}
	private ActionForward mergedSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_MERGECLIENT);
			DynaActionForm searchForm = (DynaActionForm) form;
			ClientSearchFormBean formBean = (ClientSearchFormBean) searchForm.get("criteria");
			/* do the search */
			request.setAttribute("mergeAction", KeyConstants.CLIENT_MODE_UNMERGE);
			if ("MyP".equals(formBean.getBedProgramId())) {
				Integer shelterId = (Integer) request.getSession().getAttribute(
						KeyConstants.SESSION_KEY_SHELTERID);
				String providerNo = (String) request.getSession().getAttribute(
						KeyConstants.SESSION_KEY_PROVIDERNO);
				//List allBedPrograms = programManager.getBedPrograms(providerNo, shelterId);
				List allPrograms = programManager.getPrograms(Program.PROGRAM_STATUS_ACTIVE,providerNo,shelterId);
				String prgId = "";
	//			for (Program prg : allBedPrograms) {
				for (int i=0;i<allPrograms.size();i++) {
					Program prg = (Program)allPrograms.get(i);
					prgId += prg.getId().toString() + ",";
				}
				if (!"".equals(prgId))
					prgId = prgId.substring(0, prgId.length() - 1);
				formBean.setBedProgramId(prgId);
			}
			request.setAttribute("clients", mergeClientManager.searchMerged(formBean));
			request.setAttribute("method", "mergedSearch");
			setLookupLists(request);
	
			return mapping.findForward("view");
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}
	}
	private void setLookupLists(HttpServletRequest request) {
		Integer shelterId = (Integer) request.getSession().getAttribute(KeyConstants.SESSION_KEY_SHELTERID);
		String providerNo = (String) request.getSession().getAttribute(KeyConstants.SESSION_KEY_PROVIDERNO);
		List allBedPrograms = programManager.getBedPrograms(providerNo, shelterId);

		request.setAttribute("allBedPrograms", allBedPrograms);

		request.setAttribute("allBedPrograms", allBedPrograms);
		List allProviders = providerManager.getActiveProviders(providerNo,shelterId);
		request.setAttribute("allProviders", allProviders);
		request.setAttribute("genders", lookupManager.LoadCodeList("GEN", true,	null, null));
		request.setAttribute("moduleName", " - Client Management");		
	}
	public void setLookupManager(LookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}
	public void setProgramManager(ProgramManager programManager) {
		this.programManager = programManager;
	}
	public void setProviderManager(ProviderManager providerManager) {
		this.providerManager = providerManager;
	}
	public void setMergeClientManager(MergeClientManager mergeClientManager) {
		this.mergeClientManager = mergeClientManager;
	}
}
