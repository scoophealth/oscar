package org.oscarehr.casemgmt.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.RoleManager;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.ClientImage;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.casemgmt.service.ClientImageManager;
import org.oscarehr.casemgmt.service.TicklerManager;

public class BaseCaseManagementViewAction extends DispatchAction {
	
	protected CaseManagementManager caseManagementMgr;
	protected TicklerManager ticklerManager;
	protected ClientImageManager clientImageMgr;
	protected RoleManager roleMgr;
	protected ProgramManager programMgr;
	
	
	public void setCaseManagementManager(CaseManagementManager caseManagementMgr) {
		this.caseManagementMgr = caseManagementMgr;
	}
	
	public void setTicklerManager(TicklerManager mgr) {
		this.ticklerManager = mgr;
	}
	
	public void setClientImageManager(ClientImageManager mgr) {
		this.clientImageMgr = mgr;
	}
	
	public void setRoleManager(RoleManager mgr) {
		this.roleMgr = mgr;
	}
	
	public void setProgramManager(ProgramManager mgr) {
		this.programMgr = mgr;
	}
	
	public String getDemographicNo(HttpServletRequest request) {
		String demono= request.getParameter("demographicNo");
		if (demono==null || "".equals(demono)) 
			demono=(String)request.getSession().getAttribute("casemgmt_DemoNo");
		else
			request.getSession().setAttribute("casemgmt_DemoNo", demono);
		return demono;
	}
	
	public String getDemoName(String demoNo){
		if (demoNo==null) return "";
		return caseManagementMgr.getDemoName(demoNo);
	}
	
	public String getDemoAge(String demoNo){
		if (demoNo==null) return "";
		return caseManagementMgr.getDemoAge(demoNo);
	}
	
	public String getDemoDOB(String demoNo){
		if (demoNo==null) return "";
		return caseManagementMgr.getDemoDOB(demoNo);
	}
	
	public String getProviderNo(HttpServletRequest request){
		String providerNo=request.getParameter("providerNo");
		if (providerNo==null) 
			providerNo=(String)request.getSession().getAttribute("user");
		return providerNo;
	}
	
	public String getProviderName(HttpServletRequest request){
		String providerNo=getProviderNo(request);
		if (providerNo==null)
			return "";
		return caseManagementMgr.getProviderName(providerNo);
	}
	
	protected String getImageFilename(String demoNo, HttpServletRequest request) {
		ClientImage img = clientImageMgr.getClientImage(demoNo);
		
		if(img != null) {
			String path=request.getSession().getServletContext().getRealPath("/");
			int encodedValue = (int)(Math.random()*Integer.MAX_VALUE);
			String filename = "client" +encodedValue+"."+ img.getImage_type();
			try {
				java.io.FileOutputStream os= new java.io.FileOutputStream(path+"/images/"+filename);
				os.write(img.getImage_data());
				os.flush();
				os.close();
				return filename;
			}catch(Exception e) {
				log.warn(e);
			}
		}
		return null;
	}
	
	public List notesOrderByDate(List notes,String providerNo,String demoNo)
	{
		List rtNotes=new ArrayList();
		int noteSize=notes.size();
		for (int i=0; i<noteSize; i++){
			Iterator itr=notes.iterator();
			CaseManagementNote inote=(CaseManagementNote) itr.next();

			// check note access here.
			if(inote.getProgram_no() == null || inote.getProgram_no().length()==0) {
				//didn't save this data at this time - older note
				rtNotes.add(inote);		
			} else {
				if(caseManagementMgr.hasAccessRight(removeFirstSpace(caseManagementMgr.getCaisiRoleById(inote.getReporter_caisi_role()))+"notes","access",providerNo,demoNo,inote.getProgram_no())){				
					rtNotes.add(inote);					
				}
			}
			
			notes.remove(inote);
			
		}
		return rtNotes;
	}

	String removeFirstSpace(String withSpaces) {
        int spaceIndex = withSpaces.indexOf(' '); //use lastIndexOf to remove last space
        if (spaceIndex < 0) { //no spaces!
            return withSpaces;
        }
        return withSpaces.substring(0, spaceIndex)
            + withSpaces.substring(spaceIndex+1, withSpaces.length());
    }
	
	protected void addMessage(HttpServletRequest request, String key) {
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key));
		saveMessages(request, messages);
	}
	
}
