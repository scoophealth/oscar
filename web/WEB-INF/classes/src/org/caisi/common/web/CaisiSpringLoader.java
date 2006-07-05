package org.caisi.common.web;

import java.util.Properties;

import javax.servlet.ServletContextEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.common.OscarProperties;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

public class CaisiSpringLoader extends ContextLoaderListener{

	private static Log log = LogFactory.getLog(CaisiSpringLoader.class);
			
	private boolean getFlag() {
		Properties p2 = OscarProperties.getProperties();
		if (p2==null) {
			log.info("can't find oscar property file");
			return false;
		}
		String flag=p2.getProperty("caisi","");
		if (flag==null) return false;
		if ("on".equalsIgnoreCase(flag) ||"true".equalsIgnoreCase(flag)||"yes".equalsIgnoreCase(flag))
			return true;
		else return false;
	}

	protected ContextLoader createContextLoader() {
		if (!getFlag()) {
			log.info("create spring contextloader");
			return super.createContextLoader();
		}else {
			log.info("create caisi spring loader");
			ContextLoader caisiContextLoader=new CaisiContextLoader();
			return caisiContextLoader;
		}
	}
}
