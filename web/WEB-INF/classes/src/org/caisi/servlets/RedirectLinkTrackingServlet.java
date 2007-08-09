package org.caisi.servlets;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import oscar.OscarProperties;

/**
 */
public class RedirectLinkTrackingServlet extends javax.servlet.http.HttpServlet
{
	private static final Logger logger = LogManager.getLogger(RedirectLinkTrackingServlet.class);
	
	private static final Timer redirectLinkTrackingTimer = new Timer(RedirectLinkTrackingServlet.class.getName(), true);

	private static final long REDIRECT_CLEANING_PERIOD = DateUtils.MILLIS_PER_HOUR*6;
	private static long dataRetentionTimeMillis=-1;
	private static RedirectCleaningTimerTask redirectCleaningTimerTask = null;	
	
	
	public static class RedirectCleaningTimerTask extends TimerTask
	{
		public void run()
		{
			logger.debug("RedirectCleaningTimerTask timerTask started.");

			try
            {
				if (dataRetentionTimeMillis==-1) return;
				
// delete old redirect entries
// TODO here
            }
            catch (Exception e)
            {
	            logger.error("Unexpected error flushing html open queue.", e);
            }
			
			logger.debug("RedirectCleaningTimerTask timerTask completed.");
		}
	}
	
	public void init() throws ServletException
	{
		logger.info("REDIRECT_CLEANING_PERIOD=" + REDIRECT_CLEANING_PERIOD);

		String temp=StringUtils.trimToNull(OscarProperties.getInstance().getProperty("REDIRECT_TRACKING_DATA_RETENTION_MILLIS"));
		if (temp!=null)
		{
			dataRetentionTimeMillis=Long.parseLong(temp);
		}
		
		redirectCleaningTimerTask = new RedirectCleaningTimerTask();
		redirectLinkTrackingTimer.scheduleAtFixedRate(redirectCleaningTimerTask, REDIRECT_CLEANING_PERIOD, REDIRECT_CLEANING_PERIOD);		
	}

	public void destroy()
	{
		redirectCleaningTimerTask.cancel();
		redirectLinkTrackingTimer.cancel();
		
		super.destroy();
	}
	
	/**
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, javax.servlet.ServletException
	{
		try
		{
// lookup redirect and send them there, low throughput no caching required
// TODO here
		}
		catch (Exception e)
		{
			logger.error("Error processing request. " + request.getRequestURL() + ", " + e.getMessage());
			logger.debug(e);
			response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			return;
		}
	}
	
}
