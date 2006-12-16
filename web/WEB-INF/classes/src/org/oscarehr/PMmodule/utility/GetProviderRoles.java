package org.oscarehr.PMmodule.utility;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.model.Role;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GetProviderRoles {
	
		protected final Log log = LogFactory.getLog(getClass());
		
	    protected ApplicationContext ctx = null;
	    
	    public GetProviderRoles() {
	    	String[] paths = {"/WEB-INF/applicationContext-test.xml"};
	    	ctx = new ClassPathXmlApplicationContext(paths);
	    }
	    
	    public void run() throws Exception {
	    	//get a list of providers
	    	ProviderManager providerManager = (ProviderManager)ctx.getBean("providerManager");
	    	List providers = providerManager.getProviders();
	    	
	    	for(Iterator iter=providers.iterator();iter.hasNext();) {
	    		Provider provider = (Provider)iter.next();
	    		
	    		Set roles = getRoles(provider.getProviderNo());
	    		
	    		if(roles.size() > 1) {
	    			System.out.println(provider.getFormattedName());
	    			continue;
	    		}
	    		//System.out.print(provider.getProviderNo() + ",");
	    		int x=0;
	    		StringBuffer buf = new StringBuffer();
	    		for(Iterator iter2=roles.iterator();iter2.hasNext();) {
	    			String roleName= (String)iter2.next();
	    			if(x!=0) {
	    				buf.append(",");
	    			}
	    			buf.append(roleName);
	    			x++;
	    		}
	    		buf.append("\n");
	    		
	    		/*
	    		if(buf.toString().indexOf(',') != -1) {
	    			System.out.print(provider.getFormattedName() + "," + buf.toString());
		    		
	    		}
	    		
	    		if(buf.toString().equals("")) {
	    			System.out.print(provider.getFormattedName());
	    		}
	    		*/
	    	}
	    }
	    
	    public Set getRoles(String providerNo) {
	    	ProgramManager programManager = (ProgramManager) ctx.getBean("programManager");
	    	List ppList = programManager.getProgramProvidersByProvider(providerNo);
	    	Set roles = new HashSet();
	    	for(Iterator iter=ppList.iterator();iter.hasNext();) {
	    		ProgramProvider pp = (ProgramProvider)iter.next();
	    		Role role = pp.getRole();
	    		if(role != null) {
	    			roles.add(role.getName());
	    			//roles.add(String.valueOf(role.getId()));
	    		} else {
	    			//log.error("ROLE IS NULL" + pp.getId());
	    		}
	    	}
	    	return roles;
	    }

	    public static void main(String args[]) throws Exception {
	    	GetProviderRoles prog = new GetProviderRoles();
	    	prog.run();
	    }
	    
	}
