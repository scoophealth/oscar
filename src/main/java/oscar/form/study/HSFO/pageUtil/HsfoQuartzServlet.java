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

import java.io.IOException;
import java.util.Date;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import oscar.OscarProperties;
import oscar.form.study.HSFO.RecommitDAO;
import oscar.form.study.HSFO.RecommitSchedule;
import oscar.form.study.HSFO.pageUtil.RecommitHSFOAction.ResubmitJob;

public class HsfoQuartzServlet implements Servlet
{
	public static final String RESUBMIT_JOB = "hsfoResubmitJob";

	private static Logger logger = Logger.getLogger(HsfoQuartzServlet.class);

	public static final String RESUBMIT_TRIGGER = "hsfoResubmitTrigger";

	public static void schedule() throws Exception
	{
		// Grab the Scheduler instance from the Factory
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		RecommitDAO rDao = new RecommitDAO();
		RecommitSchedule rs = rDao.getLastSchedule(false);

		if (rs == null || rs.getSchedule_time() == null)
			return;
		Date startTime = rs.getSchedule_time();
		if (startTime.before(new Date()))
		{
			logger.info("Trigger time has passed, do not start scheduler.");
			return;
		}

		scheduler.start();

		JobDetail job = scheduler.getJobDetail(RESUBMIT_JOB,
				Scheduler.DEFAULT_GROUP);
		if (job != null)
		{
			logger.info("Delete old job.");
			scheduler.deleteJob(RESUBMIT_JOB, Scheduler.DEFAULT_GROUP);
		}
		logger.info("new schedule job at " + startTime);
		Trigger trigger = new SimpleTrigger(RESUBMIT_TRIGGER,
				Scheduler.DEFAULT_GROUP, startTime);
		JobDetail jobDetail = new JobDetail(RESUBMIT_JOB,
				Scheduler.DEFAULT_GROUP, ResubmitJob.class);
		scheduler.scheduleJob(jobDetail, trigger);

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
		String id = props.getProperty("hsfo.loginSiteCode", "");
		if (id == null || "".equalsIgnoreCase(id))
			return;
		try
		{
			logger.info("============== start quartz scheduler ===========");
			schedule();
		} catch (Exception e)
		{
			MiscUtils.getLogger().error("Error", e);
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
