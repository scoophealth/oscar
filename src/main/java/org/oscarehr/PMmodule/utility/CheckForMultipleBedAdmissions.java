/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.utility;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.model.Admission;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.MiscUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CheckForMultipleBedAdmissions {

	protected final Logger log = MiscUtils.getLogger();
	
    protected ApplicationContext ctx = null;
    
    public CheckForMultipleBedAdmissions() {
    	String[] paths = {"/WEB-INF/applicationContext-test.xml"};
    	ctx = new ClassPathXmlApplicationContext(paths);
    }
    
    public void run() throws Exception {
    	//get a list of providers
    	
    	AdmissionManager admissionManager = (AdmissionManager)ctx.getBean("admissionManager");
    	ClientManager clientManager = (ClientManager)ctx.getBean("clientManager");
    	
    	List clients = clientManager.getClients();
    	for(Iterator iter=clients.iterator();iter.hasNext();) {
    		Demographic client = (Demographic)iter.next();
    		List admissions = admissionManager.getCurrentAdmissions(client.getDemographicNo());
    		doReport(client,admissions);   
    	}
    }

    private void doReport(Demographic client, List currentAdmissions) {
    	
 		for(Iterator iter2=currentAdmissions.iterator();iter2.hasNext();) {
			Admission admission = (Admission)iter2.next();
			if(admission.getProgramName() == null) {
				return;
			}
			
		}
    }
    public static void main(String args[]) throws Exception {
    	CheckForMultipleBedAdmissions prog = new CheckForMultipleBedAdmissions();
    	prog.run();
    }
    
    
}
