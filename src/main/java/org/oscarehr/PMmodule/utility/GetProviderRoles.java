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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.common.model.Provider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.quatro.model.security.Secrole;

public class GetProviderRoles {
	
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
	    			continue;
	    		}
	    		int x=0;
	    		StringBuilder buf = new StringBuilder();
	    		for(Iterator iter2=roles.iterator();iter2.hasNext();) {
	    			String roleName= (String)iter2.next();
	    			if(x!=0) {
	    				buf.append(",");
	    			}
	    			buf.append(roleName);
	    			x++;
	    		}
	    		buf.append("\n");	    		
	    	}
	    }
	    
	    public Set getRoles(String providerNo) {
	    	ProgramManager programManager = (ProgramManager) ctx.getBean("programManager");
	    	List ppList = programManager.getProgramProvidersByProvider(providerNo);
	    	Set roles = new HashSet();
	    	for(Iterator iter=ppList.iterator();iter.hasNext();) {
	    		ProgramProvider pp = (ProgramProvider)iter.next();
	    		Secrole role = pp.getRole();
	    		if(role != null) {
	    			roles.add(role.getRoleName());
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
