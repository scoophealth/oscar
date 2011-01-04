/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mymeds.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.caisi.service.InfirmBedProgramManager;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.UserProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author jackson
 */
public class MyMedsLoginAction  extends DispatchAction {

    Logger logger = Logger.getLogger(MyMedsLoginAction.class);
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
          return listPatient( mapping,  form,  request,  response);
       }
        public ApplicationContext getAppContext()
        {
            return WebApplicationContextUtils.getWebApplicationContext(getServlet().getServletContext());
        }

	public InfirmBedProgramManager getInfirmBedProgramManager() {
		InfirmBedProgramManager bpm = (InfirmBedProgramManager) getAppContext()
				.getBean("infirmBedProgramManager");
                logger.debug("bpm="+bpm);
		return bpm;
	}
        public ActionForward listPatient(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
            Integer programId=(Integer)request.getAttribute("programId");
            Date dt=(Date)request.getAttribute("myMedsDate");//not sure if ever set this in MyMeds
            String archiveView=request.getParameter("archiveView");//not sure if ever set this in MyMeds
            if(dt==null){
                dt=new Date();
            }
            if(archiveView==null){
                logger.debug("archiveView is null");
            }



            WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
            InfirmBedProgramManager bpm = (InfirmBedProgramManager)ctx.getBean("infirmBedProgramManager");
            if(programId==null)
                programId=10016;//NEED TO CHANGE
            logger.debug("bpm="+bpm+";programId="+programId+";dt="+dt+";archiveView="+archiveView);
            if(bpm==null)
                logger.debug("bpm is NULL");
            List demographicBeans = new ArrayList();
            demographicBeans=bpm.getDemographicByBedProgramIdBeans(programId,dt,archiveView);
            List<Demographic> ds=new ArrayList();
            HashMap<String,String> ps=new HashMap();
            DemographicDao demographicDao = (DemographicDao)ctx.getBean("demographicDao");
            ProviderDao providerDao=(ProviderDao)ctx.getBean("providerDao");
            for(int i=0;i<demographicBeans.size();i++){
                LabelValueBean lvb=(LabelValueBean)demographicBeans.get(i);
                int dn = new Integer(lvb.getValue());
                //String demographic_name=lvb.getLabel();
                
                Demographic d=demographicDao.getDemographicById(dn);
                if(d!=null){
                    ds.add(d);
                    String pn=d.getProviderNo();
                    String pname="";
                    if(pn!=null){
                        pname=providerDao.getProviderName(pn);
                    }
                    ps.put(pn, pname);

                }
            }
            request.setAttribute("demographics", ds);
            request.setAttribute("providerNames",ps);
            return mapping.findForward("providerLogin");
       }
       //setting preference to show mymeds
       /* public ActionForward setMyMedsPreference(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
            String preference=request.getParameter("preference");
            String providerNo=request.getParameter("providerNo");
            if(preference!=null){
                WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
                UserPropertyDAO userPropertyDAO = (UserPropertyDAO)ctx.getBean("UserPropertyDAO");
                UserProperty prop=userPropertyDAO.getProp(providerNo, UserProperty.USE_MYMEDS);
                if(prop==null){
                    prop=new UserProperty();
                    prop.setName(UserProperty.USE_MYMEDS);
                    prop.setProviderNo(providerNo);
                }
                if(preference.equalsIgnoreCase("yes")){
                    prop.setValue("yes");
                }else if(preference.equalsIgnoreCase("no")){
                    prop.setValue("no");
                }
                userPropertyDAO.saveProp(prop);
            }
            return null;
        }*/
}
