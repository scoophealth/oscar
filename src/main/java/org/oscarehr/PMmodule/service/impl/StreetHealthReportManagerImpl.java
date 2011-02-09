package org.oscarehr.PMmodule.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ClientDao;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.service.GenericIntakeManager;
import org.oscarehr.PMmodule.service.StreetHealthReportManager;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.MiscUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class StreetHealthReportManagerImpl implements StreetHealthReportManager {

	private Logger log=MiscUtils.getLogger();

	private ClientDao clientDao;
	private GenericIntakeManager intakeMgr;
		
	public void setClientDao(ClientDao dao) {
		this.clientDao = dao;
	}
	
	public void setGenericIntakeManager(GenericIntakeManager mgr) {
		this.intakeMgr = mgr;
	}
	
	public List getCohort(Date beginDate, Date endDate, int facilityId) {
		List<Demographic> clients = clientDao.getClients();
//		//return streetHealthDao.getCohort(beginDate, endDate, clientDao.getClients());
	        if (beginDate == null && endDate == null) {
	            return new ArrayList();
	        }
	        
	        List results = new ArrayList();

	        if (log.isDebugEnabled()) {
	            log.debug("Getting Cohort: " + beginDate + " to " + endDate);
	        }

	        for (int x = 0; x < clients.size(); x++) {
	            Demographic client = (Demographic)clients.get(x);
	            if (client.getPatientStatus().equals("AC")) {
	                // get current intake
	                //Formintakec intake = this.getCurrentForm(client.getDemographicNo());
	            	
	            	Intake intake = this.intakeMgr.getMostRecentQuickIntake(client.getDemographicNo(),facilityId);	            	

	            	if(intake==null) {continue;}
	            	
	            	// parse date
	                Date admissionDate = null;
	                try {	                	
	                    //admissionDate = formatter.parse(intake.getAnswerKeyValues().get("Admission Date"));
	                	admissionDate = intake.getCreatedOn().getTime();
	                }
	                catch (Exception e) {
	                }
	                if (admissionDate == null) {
	                    log.warn("invalid admission date for client #" + client.getDemographicNo());
	                    continue;
	                }
	                // does it belong in this cohort?
	                if (beginDate != null && endDate != null) {
	                    if (admissionDate.after(beginDate) && admissionDate.before(endDate)) {
	                        log.debug("admissionDate=" + admissionDate);
	                        // ok, add this client
	                        Object[] ar = new Object[2];
	                        ar[0] = intake;
	                        ar[1] = client;
	                        results.add(ar);
	                    }
	                }
	                if (beginDate == null && admissionDate.before(endDate)) {
	                    log.debug("admissionDate=" + admissionDate);
	                    // ok, add this client
	                    Object[] ar = new Object[2];
	                    ar[0] = intake;
	                    ar[1] = client;
	                    results.add(ar);
	                }
	            }
	        }

	        log.info("getCohort: found " + results.size() + " results. (" + beginDate + " - " + endDate + ")");

	        return results;
	    }


}
