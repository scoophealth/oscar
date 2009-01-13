package com.quatro.web.lookup;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.web.BaseAction;
import org.oscarehr.PMmodule.web.admin.BaseAdminAction;

import oscar.MyDateFormat;

import com.quatro.common.KeyConstants;
import com.quatro.model.FieldDefValue;
import com.quatro.model.LookupTableDefValue;
import com.quatro.model.security.NoAccessException;
import com.quatro.service.LookupManager;
import com.quatro.service.security.SecurityManager;
import com.quatro.util.Utility;

public class LookupCodeEditAction extends BaseAdminAction {
    private LookupManager lookupManager=null;

	public LookupManager getLookupManager() {
		return lookupManager;
	}

	public void setLookupManager(LookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}
	
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    		return loadCode(mapping,form,request,response);
	}
	
	private ActionForward loadCode(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	try {
    		super.getAccess(request,KeyConstants.FUN_ADMIN_LOOKUP);
			String [] codeIds = request.getParameter("id").split(":");
	        String tableId = codeIds[0];
	        String code = "0";
	        boolean isNew = true;
	        if (codeIds.length > 1) {
	        	code = codeIds[1];
	        	isNew = false;
	        }
	
	        LookupTableDefValue tableDef = lookupManager.GetLookupTableDef(tableId);
	        
			List codeFields = lookupManager.GetCodeFieldValues(tableDef, code);
			boolean editable=false;
			for(int i=0;i<codeFields.size();i++){
				FieldDefValue fdv =(FieldDefValue)codeFields.get(i);
				if(isNew && fdv.getGenericIdx() == 1 && !fdv.isAuto()) fdv.setEditable(true);  // force a new code be added
				if(fdv.isEditable()){
					editable=true;
					break;
				}
			}
			LookupCodeEditForm qform = (LookupCodeEditForm) form;
			
			qform.setTableDef(tableDef);
			qform.setCodeFields(codeFields);
			qform.setNewCode(isNew);
			qform.setErrMsg("");
			boolean isReadOnly =false;		
			SecurityManager sec = (SecurityManager) request.getSession()
			.getAttribute(KeyConstants.SESSION_KEY_SECURITY_MANAGER);	
			if (sec.GetAccess(KeyConstants.FUN_ADMIN_LOOKUP, null).compareTo(KeyConstants.ACCESS_READ) <= 0) 
				isReadOnly=true;
			if(!editable) isReadOnly = true;
			if(isReadOnly) request.setAttribute("isReadOnly", Boolean.valueOf(isReadOnly));
			return mapping.findForward("edit");
    	}
    	catch(NoAccessException e)
    	{
    		return mapping.findForward("failure");
    	}
	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		com.quatro.web.lookup.LookupCodeEditForm qform = (com.quatro.web.lookup.LookupCodeEditForm) form;
		LookupTableDefValue tableDef = qform.getTableDef();
		List fieldDefList = qform.getCodeFields();
		boolean isNew = qform.isNewCode();
		
        ActionMessages messages = new ActionMessages();
		
		if(isNew)
		{
			super.getAccess(request,KeyConstants.FUN_ADMIN_LOOKUP,KeyConstants.ACCESS_WRITE);
		}
		else
		{
			super.getAccess(request,KeyConstants.FUN_ADMIN_LOOKUP,KeyConstants.ACCESS_UPDATE);
		}
		boolean isInActive = false;
		
		if(isNew)
			super.getAccess(request,KeyConstants.FUN_ADMIN_LOOKUP,KeyConstants.ACCESS_WRITE);
		else
			super.getAccess(request,KeyConstants.FUN_ADMIN_LOOKUP,KeyConstants.ACCESS_UPDATE);

		String  code = "";
		String providerNo = (String) request.getSession(true).getAttribute(KeyConstants.SESSION_KEY_PROVIDERNO);
		Map map=request.getParameterMap();
		for(int i=0; i<fieldDefList.size(); i++)
		{
			FieldDefValue fdv = (FieldDefValue) fieldDefList.get(i);
			if (fdv.getGenericIdx() == 8) {
				fdv.setVal(providerNo);
			}
			else if(fdv.getGenericIdx() == 9)
			{
				fdv.setVal(MyDateFormat.getStandardDateTime(Calendar.getInstance()));
			}
			else
			{	
				String [] val = (String[]) map.get("field[" + i + "].val");
				if (val != null) {
					fdv.setVal(val[0]);
					if(fdv.getGenericIdx() == 1) code = fdv.getVal();
					if(fdv.getGenericIdx() == 3) isInActive = "0".equals(fdv.getVal());
					if("D".equals(fdv.getFieldType())) {
						if(!Utility.IsDate(fdv.getVal())) {
							messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.lookup.date", request.getContextPath(),fdv.getFieldDesc()));
							//errMsg += fdv.getFieldDesc() + "should be a Date";
						}
					}
					else if("N".equals(fdv.getFieldType()))
					{
						if (!(fdv.isAuto() && isNew)) {
							if(!Utility.IsInt(fdv.getVal()))
							{
								messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.lookup.integer", request.getContextPath(),fdv.getFieldDesc()));
								//errMsg += fdv.getFieldDesc() + " should be an Integer number";
							}
							else if (Utility.IsIntLessThanZero(fdv.getVal())) 
							{
									messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.lookup.integer_eq0", request.getContextPath(),fdv.getFieldDesc()));
							}
							else if(!Utility.IsIntBiggerThanZero(fdv.getVal())){
								if(fdv.getGenericIdx() == 1)
									messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.lookup.integer_gt0", request.getContextPath(),fdv.getFieldDesc()));
//									errMsg += fdv.getFieldDesc() + " should be greater than 0";
							}
							
						}
					}
					else if("S".equals(fdv.getFieldType()))
					{
						if (Utility.IsEmpty(fdv.getVal()) && fdv.getGenericIdx() == 1) 
						{
								messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.lookup.empty", request.getContextPath(),fdv.getFieldDesc()));
//								errMsg += fdv.getFieldDesc() + " Should not be empty";
						}

					}	
				}
				else
				{
					if (fdv.getGenericIdx() == 1) 
					{
							messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.lookup.empty", request.getContextPath(),fdv.getFieldDesc()));
							//errMsg += fdv.getFieldDesc() + " Should not be empty";
					}
					fdv.setVal("");
				}
			}
		}
		if((!isNew) && isInActive) { 
			if("SHL,OGN".indexOf(tableDef.getTableId())>= 0) {
				int clientCount = lookupManager.getCountOfActiveClient(tableDef.getTableId().substring(0,1) + code);
				
				if(clientCount > 0)messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.lookup.client", request.getContextPath(),tableDef.getDescription())); 
					//errMsg += "Active Clients detected in the " + tableDef.getDescription();
			}
		}
		if(messages.size() > 0) 
		{
	        saveMessages(request, messages);
			return mapping.findForward("edit");
		}
		try {
			code = lookupManager.SaveCodeValue(isNew, tableDef, fieldDefList);
			fieldDefList = lookupManager.GetCodeFieldValues(tableDef, code);
			qform.setCodeFields(fieldDefList);
			qform.setNewCode(false);
			qform.setErrMsg("Saved Successfully");
	        messages.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.save.success", request.getContextPath()));
			saveMessages(request, messages);
	        return mapping.findForward("edit");
		}
		catch(SQLException e)
		{
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.lookup.duplicate",request.getContextPath(), e.getMessage()));
			saveMessages(request, messages);
			return mapping.findForward("edit");
		}
	}
}