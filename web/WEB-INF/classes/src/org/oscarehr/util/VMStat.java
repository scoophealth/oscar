/*
 * Copyright (c) 2007-2008. MB Software Vancouver, Canada. All Rights Reserved.
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
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * This software was written for 
 * MB Software
 * Vancouver, B.C., Canada 
 */

package org.oscarehr.util;

import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * It is highly recommended that vmstat information is logged to 
 * it's own logger and using the simplesingleline formatter.
 * <br /><br />
 * Most use cases will be to either just call logAll() when needed 
 * or call startContinuousLogging().
 */
public class VMStat
{
	private static Logger logger=Logger.getLogger(VMStat.class.getName());
	
	private static Timer timer=null;
	
	public static void useSimpleLogger(String logDir) throws SecurityException, IOException
	{
		SimpleDateFormat isoDateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		
		FileHandler fileHandler=new FileHandler(logDir+"/vmstat_"+isoDateFormatter.format(new Date())+".log");
		fileHandler.setFormatter(new SimpleSingleLineFormatter());
		
		logger.setUseParentHandlers(false);
		logger.addHandler(fileHandler);
	}
	
	public static void startContinuousLogging(long logPeriodMilliseconds)
	{
		if (timer!=null) throw(new IllegalStateException("ContinuousLogging already started."));
		
		timer=new Timer(VMStat.class.getName(), true);
		
		TimerTask timerTask=new TimerTask()
		{
			public void run()
			{
				try
				{
					logAll();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		};
		
		timer.schedule(timerTask, 0, logPeriodMilliseconds);
	}
	
	public static String getMemoryFormat()
	{
		return("memoryPoolName,maxAllowed(bytes),currentUsage(bytes)");
	}
	
	public static String getMemoryInfo(MemoryPoolMXBean memoryPool)
	{
		StringBuilder sb=new StringBuilder();
		
		sb.append(memoryPool.getName());
		sb.append(',');
		
		MemoryUsage memoryUsage=memoryPool.getUsage();

		sb.append(memoryUsage.getMax());
		sb.append(',');

		sb.append(memoryUsage.getUsed());

		return(sb.toString());
	}
	
	public static void logMemoryInfo()
	{
		List<MemoryPoolMXBean> memoryPools=ManagementFactory.getMemoryPoolMXBeans();
		
		for (MemoryPoolMXBean memoryPool : memoryPools)
		{
			logger.info(getMemoryInfo(memoryPool));
		}		
	}
	
	public static String getThreadFormat()
	{
		return("ThreadInfoName,PeakThreadCount,ThreadCount(includes deamons),DaemonThreadCount");
	}
	
	public static void logThreadInfo()
	{
		logger.info(getThreadInfo());
	}

	public static String getThreadInfo()
	{
		StringBuilder sb=new StringBuilder();

		sb.append("ThreadInfo,");
		
		ThreadMXBean threadMXBean=ManagementFactory.getThreadMXBean();
		sb.append(threadMXBean.getPeakThreadCount());
		sb.append(',');
		sb.append(threadMXBean.getThreadCount());
		sb.append(',');
		sb.append(threadMXBean.getDaemonThreadCount());
		
		return(sb.toString());
	}
	
	public static String getGCFormat()
	{
		return("garbageCollectorName,CollectionTime(ms),CollectionCount");
	}
	
	public static String getGCInfo(GarbageCollectorMXBean garbageCollectorMXBean)
	{
		StringBuilder sb=new StringBuilder();

		sb.append(garbageCollectorMXBean.getName());
		sb.append(',');
		sb.append(garbageCollectorMXBean.getCollectionTime());
		sb.append(',');
		sb.append(garbageCollectorMXBean.getCollectionCount());
		
		return(sb.toString());
	}
	
	public static void logGCInfo()
	{
		List<GarbageCollectorMXBean> garbageCollectorMXBeans=ManagementFactory.getGarbageCollectorMXBeans();
		
		for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans)
		{
			logger.info(getGCInfo(garbageCollectorMXBean));
		}
	}
	
	public static void logAll()
	{
		logMemoryInfo();
		logGCInfo();		
		logThreadInfo();
	}
	
	public static void logAllFormat()
	{
		logger.info(getMemoryFormat());
		logger.info(getGCFormat());
		logger.info(getThreadFormat());		
	}
	
	public static void main(String... argv) throws Exception
	{
		useSimpleLogger("dist");
		
		startContinuousLogging(5000);
		
		Thread.sleep(60000);
	}
}