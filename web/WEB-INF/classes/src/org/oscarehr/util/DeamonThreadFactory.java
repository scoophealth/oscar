package org.oscarehr.util;

import java.util.concurrent.ThreadFactory;

public final class DeamonThreadFactory implements ThreadFactory{

	private String threadName;
	
	public DeamonThreadFactory(String threadName)
	{
		this.threadName=threadName;	
	}
	
	public Thread newThread(Runnable r) {
		Thread thread=new Thread(r, threadName);
		thread.setDaemon(true);
		return(thread);
    }

}
