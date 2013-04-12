/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.ws.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonBeanProcessor;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.rs.security.oauth.data.OAuthContext;
import org.apache.cxf.security.SecurityContext;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.ws.transfer_objects.ProviderTransfer;
import org.springframework.beans.factory.annotation.Autowired;


@Path("/providerService/")
@Produces("application/xml")
public class ProviderService {

	@Autowired
	ProviderDao providerDao;
	
	protected SecurityContext getSecurityContext() {
		Message m = PhaseInterceptorChain.getCurrentMessage();
    	org.apache.cxf.security.SecurityContext sc = m.getContent(org.apache.cxf.security.SecurityContext.class);
    	return sc;
	}
	
	protected OAuthContext getOAuthContext() {
		Message m = PhaseInterceptorChain.getCurrentMessage();
		OAuthContext sc = m.getContent(OAuthContext.class);
    	return sc;
	}
	
    public ProviderService() {
    }

    @GET
    @Path("/providers")
    public org.oscarehr.ws.rest.to.OscarSearchResponse<ProviderTransfer> getProviders() {
    	org.oscarehr.ws.rest.to.OscarSearchResponse<ProviderTransfer> lst = new 
    			org.oscarehr.ws.rest.to.OscarSearchResponse<ProviderTransfer>();
    	   	
    	for(Provider p: providerDao.getActiveProviders()) {
    		lst.getContent().add(ProviderTransfer.toTransfer(p));
    	} 
      
        return lst;
    }
 
    @GET
    @Path("/providers_json")
    public String getProvidersAsJSON() {
    	JsonConfig config = new JsonConfig();
    	config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());
    	
    	List<Provider> p = providerDao.getActiveProviders();
    	JSONArray arr = JSONArray.fromObject(p, config);
    	return arr.toString();
    }

    @GET
    @Path("/provider/{id}")
    public ProviderTransfer getProvider(@PathParam("id") String id) {
        return ProviderTransfer.toTransfer(providerDao.getProvider(id));
    }
    
    @GET
    @Path("/providerjson/{id}")
    public String getProviderAsJSON(@PathParam("id") String id) {
    	JsonConfig config = new JsonConfig();
    	config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());
        return JSONObject.fromObject(providerDao.getProvider(id),config).toString();
    }

    @GET
    @Path("/providers/bad")
    public Response getBadRequest() {
        return Response.status(Status.BAD_REQUEST).build();
    }
}
