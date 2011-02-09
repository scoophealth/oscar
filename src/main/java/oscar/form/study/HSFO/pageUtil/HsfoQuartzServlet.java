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

	protected static Logger logger = Logger.getLogger(HsfoQuartzServlet.class);

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
