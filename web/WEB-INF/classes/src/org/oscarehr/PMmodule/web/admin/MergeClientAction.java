package org.oscarehr.PMmodule.web.admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.oscarehr.PMmodule.model.ClientMerge;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.model.Provider;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.MergeClientManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;

import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;
import org.oscarehr.PMmodule.web.utils.UserRoleUtils;

import com.quatro.common.KeyConstants;
import com.quatro.model.security.NoAccessException;
import com.quatro.service.LookupManager;
import com.quatro.util.Utility;

public class MergeClientAction extends BaseAdminAction {

	private ClientManager clientManager;

	private ProviderManager providerManager;

	private ProgramManager programManager;

	private LookupManager lookupManager;

	private MergeClientManager mergeClientManager;

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// return search(mapping, form, request, response);
		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_MERGECLIENT);
			setLookupLists(request);
			return mapping.findForward("view");
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}
	}

	public ActionForward mergedSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_MERGECLIENT);
			DynaActionForm searchForm = (DynaActionForm) form;
			ClientSearchFormBean formBean = (ClientSearchFormBean) searchForm
					.get("criteria");
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
			request.setAttribute("clients", mergeClientManager
					.searchMerged(formBean));
			request.setAttribute("method", "mergedSearch");
			setLookupLists(request);
	
			return mapping.findForward("view");
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}
		
	}

	public ActionForward unmerge(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_MERGECLIENT,KeyConstants.ACCESS_WRITE);
			setLookupLists(request);
	
			ActionMessages messages = new ActionMessages();
			request.setAttribute("mergeAction", KeyConstants.CLIENT_MODE_MERGE);
			if (request.getParameterValues("records") == null) {
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"message.merge.errors.select", request.getContextPath()));
				saveMessages(request, messages);
				return mergedSearch(mapping, form, request, response);
			}
			ArrayList records = new ArrayList(Arrays.asList(request
					.getParameterValues("records")));
	
			String providerNo = (String) request.getSession().getAttribute(
					KeyConstants.SESSION_KEY_PROVIDERNO);
			if (records.size() > 0) {
				for (int i = 0; i < records.size(); i++) {
					String demographic_no = (String) records.get(i);
					try {
						Integer clientId = Integer.valueOf(demographic_no);
						ClientMerge cmObj =  mergeClientManager.getClientMerge(clientId);
						if(cmObj == null) {
							messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									"error.unmerge.failed", request.getContextPath(),"The client " + demographic_no + " is not currently merged to anyone"));
						}
					} catch (Exception e) {
						messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"error.unmerge.failed", request.getContextPath(),e.getMessage()));
					}
					if (messages.size() > 0) {
						saveMessages(request, messages);
						return mergedSearch(mapping,form,request,response);
					}
				}
				if (records.size() > 0) {
					for (int i = 0; i < records.size(); i++) {
						String demographic_no = (String) records.get(i);
						try {
							Integer clientId = Integer.valueOf(demographic_no);
							ClientMerge cmObj =  mergeClientManager.getClientMerge(clientId);
							cmObj.setProviderNo(providerNo);
							cmObj.setLastUpdateDate(new GregorianCalendar());
							mergeClientManager.unMerge(cmObj);
						} catch (Exception e) {
							messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									"error.unmerge.failed", request.getContextPath(),e.getMessage()));
							saveMessages(request, messages);
							return mergedSearch(mapping,form,request,response);
						}
					}
				}
			}
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"message.merge.success", request.getContextPath()));
			saveMessages(request, messages);
			// return mapping.findForward("view");
			return search(mapping, form, request, response);
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}
			
	}

	public ActionForward merge(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_MERGECLIENT,KeyConstants.ACCESS_WRITE);
			setLookupLists(request);
	
			ActionMessages messages = new ActionMessages();
			String test = request.getParameter("records");
			if (request.getParameterValues("records") == null) {
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"message.merge.errors.select", request.getContextPath()));
				saveMessages(request, messages);
				return mapping.findForward("view");
			}
			boolean isSuccess = true;
			request.setAttribute("mergeAction", KeyConstants.CLIENT_MODE_MERGE);
			ArrayList records = new ArrayList(Arrays.asList(request
					.getParameterValues("records")));
			String head = request.getParameter("head");
			String action = request.getParameter("mergeAction");
			String providerNo = (String) request.getSession().getAttribute(
					KeyConstants.SESSION_KEY_PROVIDERNO);
			if (head != null && records.size() > 1 && records.contains(head)) {
				for (int i = 0; i < records.size(); i++) 
				{
					if (!((String) records.get(i)).equals(head)){					
						Integer mergeClientNo = Integer.valueOf((String) records.get(i));
						Demographic mClient =clientManager.getClientByDemographicNo((String) records.get(i));
						if(mClient.isActive()){
							messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									"message.merge.errors.active.client", request.getContextPath()));
							saveMessages(request, messages);
							return search(mapping,form,request,response);
						}
						ClientMerge	cmObj = mergeClientManager.getClientMerge(mergeClientNo);
						if( cmObj != null && !cmObj.isDeleted())
						{
							messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									"message.merge.errors.merged.client", request.getContextPath(),cmObj.getMergedToClientFormattedName() + "(" + cmObj.getMergedToClientId() +  ")"));
							saveMessages(request, messages);
							return search(mapping,form,request,response);
						}
						try {
							if(cmObj == null) cmObj = new ClientMerge();
							cmObj.setDeleted(false);
							cmObj.setClientId(mergeClientNo);
							cmObj.setMergedToClientId(Integer.valueOf(head));
							cmObj.setProviderNo(providerNo);
							cmObj.setLastUpdateDate(new GregorianCalendar());
							mergeClientManager.merge(cmObj);
						} catch (Exception e) {
							messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									"error.merge.failed", request.getContextPath(),e.getMessage()));
							saveMessages(request, messages);
							return search(mapping,form,request,response);
						}
					}
				}
	
			} else {
				isSuccess = false;
			}
			if (!isSuccess) {
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"message.merge.errors", request.getContextPath()));
				saveMessages(request, messages);
				return search(mapping,form,request,response);
			} else {
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"message.merge.success", request.getContextPath()));
				saveMessages(request, messages);
				return mapping.findForward("unmerge");
			}
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}
	}

	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			super.getAccess(request, KeyConstants.FUN_ADMIN_MERGECLIENT);
			DynaActionForm searchForm = (DynaActionForm) form;
		
			ClientSearchFormBean formBean = (ClientSearchFormBean) searchForm
					.get("criteria");
	
			request.setAttribute("mergeAction", KeyConstants.CLIENT_MODE_MERGE);
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
			List clients = clientManager.search(formBean, false, false);
			request.setAttribute("clients", clients);
			setLookupLists(request);
			return mapping.findForward("view");
		}
		catch(NoAccessException e)
		{
			return mapping.findForward("failure");
		}
	}

	private void setLookupLists(HttpServletRequest request) {
		Integer shelterId = (Integer) request.getSession().getAttribute(
				KeyConstants.SESSION_KEY_SHELTERID);
		String providerNo = (String) request.getSession().getAttribute(
				KeyConstants.SESSION_KEY_PROVIDERNO);
		List allBedPrograms = programManager.getBedPrograms(providerNo,
				shelterId);
		request.setAttribute("allBedPrograms", allBedPrograms);
		List allProviders = providerManager.getActiveProviders(providerNo,
				shelterId);
		request.setAttribute("allProviders", allProviders);
		request.setAttribute("genders", lookupManager.LoadCodeList("GEN", true,
				null, null));
		request.setAttribute("moduleName", " - Client Management");
	}

	public void setClientManager(ClientManager clientManager) {
		this.clientManager = clientManager;
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
