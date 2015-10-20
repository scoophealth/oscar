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

/*
 * Written by Brandon Aubie <brandon@aubie.ca>
 */

package org.oscarehr.ws.oauth;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.common.model.Provider;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonBeanProcessor;


@Path("/oauth")
public class OAuthStatusService extends AbstractServiceImpl {
	
	@GET
	@Path("/info")
	@Produces("application/json")
	public String oauthInfo() {

        try {

            LoggedInInfo loggedInInfo = getLoggedInInfo();
            Provider provider = loggedInInfo.getLoggedInProvider();

    		JsonConfig config = new JsonConfig();
        	config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());

            String login = getOAuthContext().getSubject().getLogin();
            List<String> roles = getOAuthContext().getSubject().getRoles();

            JSONObject obj = new JSONObject();
            obj.put("provider", JSONObject.fromObject(provider,config));
            obj.put("login", login);
            obj.put("roles", roles);

            return obj.toString();

        } catch (Exception e) {
            return "Sorry, there was an error building your resposne: " + e.toString();
        }
	}
	
}
