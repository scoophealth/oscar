package org.caisi.tickler.prepared;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PreparedTicklerManager {

	static Log log = LogFactory.getLog(PreparedTicklerManager.class); 
	
	private List ticklers;
	
	public PreparedTicklerManager() {
		ticklers = new ArrayList();
	}
	
	/* loads up the runtime plugins */
	public void setPath(String path) {
		ticklers.clear();
		File f = new File(path + "/WEB-INF/classes/org/caisi/tickler/prepared/runtime");
		if(f.isDirectory()) {
			File[] files = f.listFiles();
			for(int x=0;x<files.length;x++) {
				String fileName = files[x].getName();
				fileName = fileName.substring(0,fileName.lastIndexOf('.'));
				PreparedTickler pt = null;
				if((pt = loadClass("org.caisi.tickler.prepared.runtime." + fileName)) != null) {
					ticklers.add(pt);
				}
			}
		}
		
	}
	
	public PreparedTickler loadClass(String className) {
		PreparedTickler pt = null;
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
		try {
			 pt = (PreparedTickler)cl.loadClass(className).newInstance();
		}catch(ClassNotFoundException e) {
			log.warn(e);
		}catch(InstantiationException e) {
			log.warn(e);
		}catch(IllegalAccessException e) {
			log.warn(e);
		}catch(ClassCastException e) {
			log.warn(e);
		}

		return pt;
	}

	public List getTicklers() {
		return ticklers;
	}

	public PreparedTickler getTickler(String name) {
		for(int x=0;x<ticklers.size();x++) {
			PreparedTickler tickler = (PreparedTickler)ticklers.get(x);
			if(tickler.getName().equals(name)) {
				return tickler;
			}
		}
		return null;
	}
}
