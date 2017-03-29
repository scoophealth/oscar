/**
 * Copyright (C) 2007  Heart & Stroke Foundation
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

package oscar.form.study.hsfo2.pageUtil;

import java.io.IOException;
import java.util.Calendar;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.Hsfo2RecommitScheduleDao;
import org.oscarehr.common.model.Hsfo2RecommitSchedule;
import org.oscarehr.util.SpringUtils;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import oscar.OscarProperties;

import oscar.form.study.hsfo2.pageUtil.RecommitHSFOAction.ResubmitJob;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.quartz.impl.StdSchedulerFactory;

public class HsfoQuartzServlet implements Servlet
{
	public static final String RESUBMIT_JOB = "hsfoResubmitJob";

	private static Logger logger = Logger.getLogger(HsfoQuartzServlet.class);

	public static final String RESUBMIT_TRIGGER = "hsfoResubmitTrigger";

	private static Hsfo2RecommitScheduleDao rDao = (Hsfo2RecommitScheduleDao) SpringUtils.getBean("hsfo2RecommitScheduleDao");
	
	public static void schedule() throws Exception
	{		
		// Grab the Scheduler instance from the Factory
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			
		//RecommitDAO rDao = new RecommitDAO();
		Hsfo2RecommitSchedule rs = rDao.getLastSchedule(false);

		if (rs == null || rs.getSchedule_time() == null)
			return;				
			
		scheduler.start();
		
		Calendar startTime = Calendar.getInstance();
		startTime.setTime(rs.getSchedule_time());
				
		// Run every day.
		String hour = String.valueOf(startTime.get(Calendar.HOUR_OF_DAY));
		String minute = String.valueOf(startTime.get(Calendar.MINUTE));		
				
		String cronExpression = "0 " + minute + " " + hour + " * * ?";
		//this is just for testing every 5 minutes: 
		//String cronExpression = "0 0,5,10,15,20,25,30,35,40,45,50,55 " + hour + "-23 * * ?";
		
		logger.info("quartz schedule cron expression:" + cronExpression);		
		
		CronTrigger trigger = new CronTrigger(RESUBMIT_TRIGGER, Scheduler.DEFAULT_GROUP, cronExpression);
		
		//trigger.setCronExpression("0 42 10 * * ?"); //Build a trigger that will fire daily at 10:42 am
				
		JobDetail jobDetail = new JobDetail(RESUBMIT_JOB, Scheduler.DEFAULT_GROUP, ResubmitJob.class);
				
		//Delete old job.		
		JobDetail job = scheduler.getJobDetail(RESUBMIT_JOB, Scheduler.DEFAULT_GROUP);
		if (job != null)
		{
			logger.info("Reschedule quartz cron trigger.......");
			//scheduler.rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
			scheduler.deleteJob(RESUBMIT_JOB, Scheduler.DEFAULT_GROUP);
			scheduler.scheduleJob(jobDetail, trigger);
		} else {		
			//Start new job.			
			scheduler.scheduleJob(jobDetail, trigger);
		}
		
	}

	public ServletConfig getServletConfig()
	{
		return null;
	}

	public String getServletInfo()
	{
		return null;
	}

	public void init(ServletConfig arg0) throws ServletException
	{
		OscarProperties props = OscarProperties.getInstance();
		String id = props.getProperty("hsfo2.loginSiteCode", "");
		if (id == null || "".equalsIgnoreCase(id))
			return;
		try
		{
			logger.info("============== start quartz scheduler ===========");
			schedule();
		} catch (Exception e)
		{
			logger.info( e.getMessage());
		}
	}

	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException
	{
	}

	public void destroy()
	{
	}
}
