package org.caisi.core.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.caisi.service.InfirmBedProgramManager;

public class InfirmAction extends BaseAction
{
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(InfirmAction.class);

	public ActionForward showProgram(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		logger.info("====> inside showProgram action.");

		HttpSession se = request.getSession();
		se.setAttribute("infirmaryView_initflag", "true");
		String providerNo=(String) se.getAttribute("user");
		
		//clear memory for programbean
		//List memob=(List) se.getAttribute("infirmaryView_demographicBeans");
		//if (memob!=null) memob.clear();
		
		InfirmBedProgramManager manager=getInfirmBedProgramManager();
		List programBean=manager.getProgramBeans(providerNo);
		se.setAttribute("infirmaryView_programBeans",programBean );
		//set default program
		int defaultprogramId=getInfirmBedProgramManager().getDefaultProgramId(providerNo);
		boolean defaultInList=false;
		for (int i=0;i<programBean.size();i++){
			int id=new Integer(((LabelValueBean) programBean.get(i)).getValue()).intValue();
			if ( defaultprogramId == id ) defaultInList=true;
		}
		if (!defaultInList) defaultprogramId=0;
		int OriprogramId=0;
		if (programBean.size()>0) OriprogramId=new Integer(((LabelValueBean) programBean.get(0)).getValue()).intValue();
		int programId=0;
		if (defaultprogramId!=0 && OriprogramId!=0) programId=defaultprogramId;
		else {
			if (OriprogramId==0) programId=0;
			if (defaultprogramId==0 && OriprogramId!=0) programId=OriprogramId;
		}
		if (se.getAttribute("infirmaryView_programId")!=null){
			programId=Integer.valueOf((String)se.getAttribute("infirmaryView_programId")).intValue();
		}
		if (programId!=defaultprogramId) getInfirmBedProgramManager().setDefaultProgramId(providerNo,programId);
		logger.info(String.valueOf(programId));
		se.setAttribute("infirmaryView_programId",String.valueOf(programId));
		Date dt;

		if (se.getAttribute("infirmaryView_date")!=null)
		{
			dt=(Date) se.getAttribute("infirmaryView_date") ; //new Date(year,month-1,day);
		}else{
			dt=new Date();
		}
		
		//release memory
		//List memo=(List) se.getAttribute("infirmaryView_demographicBeans");
		//if (memo!=null) memo.clear();
		
		se.setAttribute("infirmaryView_demographicBeans",getInfirmBedProgramManager().getDemographicByBedProgramIdBeans(programId,dt));
		
		/*java.util.Enumeration enu =  se.getAttributeNames();
		while (enu.hasMoreElements())
			logger.info(enu.nextElement()); 
		*/
//		response.sendRedirect(se.getAttribute("infirmaryView_OscarURL")+"?"+se.getAttribute("infirmaryView_OscarQue"));
		return null;
	}

	public ActionForward getSig(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		logger.info("====> inside getSig action.");
		String providerNo=request.getParameter("providerNo");
		if (providerNo==null) providerNo=(String) request.getSession().getAttribute("user");
		Boolean onsig=getInfirmBedProgramManager().getProviderSig(providerNo);
		request.getSession().setAttribute("signOnNote",onsig);
		return null;
	}
	
	public ActionForward toggleSig(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		logger.info("====> inside toggleSig action.");
		String providerNo=request.getParameter("providerNo");
		if (providerNo==null) providerNo=(String) request.getSession().getAttribute("user");
		Boolean onsig=getInfirmBedProgramManager().getProviderSig(providerNo);
		request.getSession().setAttribute("signOnNote",onsig);
		return null;
	}
	
}

