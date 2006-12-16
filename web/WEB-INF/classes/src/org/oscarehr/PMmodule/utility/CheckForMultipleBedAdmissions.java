package org.oscarehr.PMmodule.utility;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CheckForMultipleBedAdmissions {

	protected final Log log = LogFactory.getLog(getClass());
	
    protected ApplicationContext ctx = null;
    
    public CheckForMultipleBedAdmissions() {
    	String[] paths = {"/WEB-INF/applicationContext-test.xml"};
    	ctx = new ClassPathXmlApplicationContext(paths);
    }
    
    public void run() throws Exception {
    	//get a list of providers
    	ProviderManager providerManager = (ProviderManager)ctx.getBean("providerManager");
    	AdmissionManager admissionManager = (AdmissionManager)ctx.getBean("admissionManager");
    	ClientManager clientManager = (ClientManager)ctx.getBean("clientManager");
    	
    	List clients = clientManager.getClients();
    	for(Iterator iter=clients.iterator();iter.hasNext();) {
    		Demographic client = (Demographic)iter.next();
    		List admissions = admissionManager.getCurrentAdmissions(String.valueOf(client.getDemographicNo()));
    		doReport(client,admissions);   
    	}
    }

    private void doReport(Demographic client, List currentAdmissions) {
    	int numBedPrograms = 0;
 		for(Iterator iter2=currentAdmissions.iterator();iter2.hasNext();) {
			Admission admission = (Admission)iter2.next();
			if(admission.getProgramName() == null) {
				System.out.println("Error: can't find program " + admission.getProgramId());
				return;
			}
			if(admission.getProgramType().equals("Bed")) {
				numBedPrograms++;
			}
		}
		if(numBedPrograms == 0) {
			System.out.println(client.getDemographicNo() + " is not in a bed program");
		}
		if(numBedPrograms > 1) {
			System.out.println(client.getDemographicNo() + " is in " + numBedPrograms + " bed programs");        		
		}
		
    }
    public static void main(String args[]) throws Exception {
    	CheckForMultipleBedAdmissions prog = new CheckForMultipleBedAdmissions();
    	prog.run();
    }
    
    
}
