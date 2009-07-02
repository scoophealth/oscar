package oscar.util.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class OscarDbPropertiesListener implements ServletContextListener {
	private static Log log = LogFactory.getLog(OscarDbPropertiesListener.class);

	public void contextInitialized(ServletContextEvent event) {
		String contextPath = event.getServletContext().getInitParameter("contextPath");
		if(contextPath != null) {
			log.info(" found contextPath override.");
		} else {
			String path = event.getServletContext().getRealPath("/");
			char sep = File.separatorChar;
			if (sep=='/'){
				//for linux
				String[] splitPath = path.split(String.valueOf(sep));
				contextPath = splitPath[splitPath.length-1];	
			} else {
				//for windows
				contextPath = path.substring(0,path.length()-1);
				int contextInt = contextPath.lastIndexOf(String.valueOf(sep));
				contextPath = contextPath.substring(contextInt+1);		
			}
			
		}
		
		log.info("using context path (" + contextPath + ")");
		
		
		Properties p = System.getProperties();
		String workingDir = (String)p.get("user.home");
		String propName = contextPath + ".properties";
		File f = new File(workingDir,propName);
		if(!f.exists()) {
			log.error("properties file \"" + propName + "\" does not exist in \"" + workingDir +"\"...cannot load db properties.");
			throw new RuntimeException(f.getAbsolutePath() + " not found");
		}
		Properties p2 = new Properties();
		try {
			p2.load(new FileInputStream(f));
		}catch(Exception e) {
			throw new RuntimeException("Unable to read properties file " + f.getAbsolutePath());
		}
		OscarProperties.setProperties(p2);
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

}
