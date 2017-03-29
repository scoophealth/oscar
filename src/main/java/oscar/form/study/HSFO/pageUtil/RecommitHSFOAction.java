/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.form.study.HSFO.pageUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noNamespace.HsfoHbpsDataDocument;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.LazyValidatorForm;
import org.caisi.dao.ProviderDAO;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.SecurityDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.struts.DispatchActionSupport;

import oscar.OscarProperties;
import oscar.form.study.HSFO.RecommitDAO;
import oscar.form.study.HSFO.RecommitSchedule;

public class RecommitHSFOAction extends DispatchActionSupport {

    private static Logger logger = Logger.getLogger(RecommitHSFOAction.class);

    public ActionForward showSchedule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
        LazyValidatorForm sform = (LazyValidatorForm)form;
        boolean sflag = false;// whether have current schedule
        boolean cflag = true;// whether copy the demographic info to
        // hsfo_patient
        RecommitDAO rd = new RecommitDAO();
        String sdate = "";
        String shour = "03";
        String smin = "30";
        RecommitSchedule rsd = rd.getLastSchedule(true);
        boolean lastlog_flag = false;
        String lastlog_time = "";
        String lastlog = "";
        if (rsd != null) {
            lastlog_flag = true;
            SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            lastlog_time = sf1.format(rsd.getSchedule_time());
            lastlog = rsd.getMemo();
        }
        sform.set("lastlog_flag", lastlog_flag?"true":"false");
        sform.set("lastlog_time", lastlog_time);
        sform.set("lastlog", lastlog != null?lastlog:"");

        Integer sid = new Integer(0);
        if (rd.isLastActivExpire()) rd.deActiveLast();
        RecommitSchedule rs = rd.getLastSchedule(false);
        if (rs != null && !"D".equalsIgnoreCase(rs.getStatus())) {
            sflag = true;
            Date dd = rs.getSchedule_time();
            SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sf2 = new SimpleDateFormat("HH");
            SimpleDateFormat sf3 = new SimpleDateFormat("mm");
            sdate = sf1.format(dd);
            shour = sf2.format(dd);
            smin = sf3.format(dd);
            cflag = rs.isCheck_flag();
            sid = rs.getId();
        }
        sform.set("schedule_flag", sflag?"true":"false");// already have
        // a current
        // schedule
        sform.set("check_flag", cflag?"true":"false");
        sform.set("isCheck", cflag?"true":"false");
        sform.set("schedule_date", sdate);
        sform.set("schedule_shour", shour);
        sform.set("schedule_min", smin);
        sform.set("schedule_id", sid.toString());

        return mapping.findForward("schedulePage");
    }

    public ActionForward saveSchedule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RecommitDAO rd = new RecommitDAO();
        RecommitSchedule rs = new RecommitSchedule();
        LazyValidatorForm sform = (LazyValidatorForm)form;
        String user = (String)request.getSession().getAttribute("user");
        String sid = (String)sform.get("schedule_id");
        String sdate = (String)sform.get("schedule_date");
        String shour = (String)sform.get("schedule_shour");
        String smin = (String)sform.get("schedule_min");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date stime = sf.parse(sdate + " " + shour + ":" + smin);

        if (stime.before(new Date())) {
            request.setAttribute("schedule_err", "Invalid time,please schedule another time!");
            return showSchedule(mapping, form, request, response);
        }
        rs.setStatus("A");
        rs.setUser_no(user);
        String copyflag = (String)sform.get("isCheck");
        if (copyflag != null && "false".equalsIgnoreCase(copyflag)) rs.setCheck_flag(false);
        else rs.setCheck_flag(true);

        rs.setSchedule_time(stime);

        Integer id = new Integer(sid);
        if (id.intValue() != 0) {
            rs.setId(id);
            rd.updateLastSchedule(rs);
        }
        else rd.insertchedule(rs);
        request.setAttribute("schedule_message", "successfully update!");

        HsfoQuartzServlet.schedule();

        return mapping.findForward("schedulePage");
    }

    public static class ResubmitJob implements Job {

        public void execute(JobExecutionContext ctx) throws JobExecutionException {

        	
        	String providerNo = OscarProperties.getInstance().getProperty("hsfo_job_run_as_provider");
			if(providerNo == null) {
				return;
			}
			
			ProviderDAO providerDao = SpringUtils.getBean(ProviderDao.class);
			Provider provider = providerDao.getProvider(providerNo);
			
			if(provider == null) {
				return;
			}
			
			SecurityDao securityDao = SpringUtils.getBean(SecurityDao.class);
			List<Security> securityList = securityDao.findByProviderNo(providerNo);
			
			if(securityList.isEmpty()) {
				return;
			}
			
			LoggedInInfo x = new LoggedInInfo();
			x.setLoggedInProvider(provider);
			x.setLoggedInSecurity(securityList.get(0));
			
			
    		try {
                XMLTransferUtil tfutil = new XMLTransferUtil();
                RecommitDAO rDao = new RecommitDAO();
                RecommitSchedule rs = rDao.getLastSchedule(false);
                ArrayList<String> message = new ArrayList<String>();
                String retS = null;
                if (!"D".equalsIgnoreCase(rs.getStatus())) {
                    rs.setStatus("D");

                    if (rs.isCheck_flag()) retS = rDao.SynchronizeDemoInfo(x);
                    else retS = rDao.checkProvider(x);

                    if (retS != null) {
                        rs.setMemo("Upload failure. Missing internal doctor for patient " + retS + ".");
                        rDao.updateLastSchedule(rs);
                        return;
                    }
                    HsfoHbpsDataDocument doc = tfutil.generateXML(x, rs.getUser_no(), 0);

                    if (doc == null) {
                        message.add("");
                        message.add("Patient(s) data not found in the database.");
                    }
                    message = tfutil.validateDoc(doc);
                    if (message.size() != 0) {
                        rs.setMemo(message.get(0));
                        rDao.updateLastSchedule(rs);
                        return;
                    }
                    String rstr = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + doc.xmlText();
                    // send to hsfo web

                    try {
                        message = tfutil.soapHttpCall(tfutil.getSiteID().intValue(), tfutil.getUserId(), tfutil.getLoginPasswd(), rstr);
                    }
                    catch (Exception e) {
                        logger.error("Error", e);
                        throw e;
                    }
                    String msg = "Code: " + message.get(0) + " " + message.get(1);
                    rs.setMemo(msg);
                    rDao.updateLastSchedule(rs);
                }
            }
            catch (Exception e) {
                MiscUtils.getLogger().error("Error", e);
            }
            finally {
                DbConnectionFilter.releaseAllThreadDbResources();
            }
        }

    }

    public ActionForward test(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {

        return null;
    }
}
