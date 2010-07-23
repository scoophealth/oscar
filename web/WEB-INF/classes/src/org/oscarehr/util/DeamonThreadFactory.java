package org.oscarehr.util;

import java.util.concurrent.ThreadFactory;

public class DeamonThreadFactory implements ThreadFactory{

	public Thread newThread(Runnable r) {
		Thread thread=new Thread(r);
		thread.setDaemon(true);
		return(thread);
    }
}
