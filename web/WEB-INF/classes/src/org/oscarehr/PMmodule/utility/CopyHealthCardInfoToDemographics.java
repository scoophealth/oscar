package org.oscarehr.PMmodule.utility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Formintakea;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.IntakeAManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CopyHealthCardInfoToDemographics {
	protected final Log log = LogFactory.getLog(getClass());
	
    protected ApplicationContext ctx = null;
    protected DataSource ds;
    protected Connection conn;
    
    
    public CopyHealthCardInfoToDemographics() throws Exception {
    	int totalUpdated = 0;
    	int totalWarnings = 0;
    	
    	String[] paths = {"/WEB-INF/applicationContext.xml"};
    	ctx = new ClassPathXmlApplicationContext(paths);
    
    	IntakeAManager intakeMgr = (IntakeAManager)ctx.getBean("intakeAManager");
    	ClientManager clientMgr = (ClientManager)ctx.getBean("clientManager");
    	
    	ds = (DataSource)ctx.getBean("dataSource");
    	conn = ds.getConnection();
 
    	Statement stmt = conn.createStatement();
    	stmt.execute("SELECT demographic_no FROM demographic");
    	ResultSet rs = stmt.getResultSet();
    	while(rs.next()) {
    		int demographicNo = rs.getInt("demographic_no");
    		Formintakea intake = intakeMgr.getCurrIntakeAByDemographicNo(String.valueOf(demographicNo));
    		if(intake != null) {
    			Demographic client = clientMgr.getClientByDemographicNo(String.valueOf(demographicNo));
    			boolean saveRequired=false;
    			if(intake.getHealthCardNum() != null && intake.getHealthCardNum().length()>0){
    				client.setHin(intake.getHealthCardNum());
    				log.info("updating " + demographicNo + " with hin " + client.getHin());
    				saveRequired=true;
    			}
    			if(intake.getHealthCardVer() != null && intake.getHealthCardVer().length()>0){
    				client.setVer(intake.getHealthCardVer());
    				log.info("updating " + demographicNo + " with ver " + client.getVer());
    				saveRequired=true;
    			}
    			if(saveRequired) {
    				clientMgr.saveClient(client);
    				totalUpdated++;
    			}
    		} else {
    			log.warn("No intake found for " + demographicNo);
    			totalWarnings++;
    		}
    		
    	}
    	
    	log.info("total updates=" + totalUpdated);
    	log.info("total warnings=" + totalWarnings);
    
    }
    
    public static void main(String args[]) throws Exception {
    	new CopyHealthCardInfoToDemographics();
    }
}
