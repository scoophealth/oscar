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
package oscar.login;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.cxf.rs.security.oauth.services.AbstractOAuthService;
import org.apache.cxf.rs.security.oauth.services.RequestTokenHandler;

/**
 * This resource issues a temporarily request token to the Client
 * which will be later authorised and exchanged for the access token 
 */
@Path("/initiate")
public class OscarRequestTokenService extends AbstractOAuthService {

    private RequestTokenHandler handler = new RequestTokenHandler();
    
    public void setRequestTokenHandler(RequestTokenHandler h) {
        this.handler = h;
    }
    
    @GET
    @Produces("application/x-www-form-urlencoded")
    public Response getRequestTokenWithGET() {
        return getRequestToken();
    }
    
    @POST
    @Produces("application/x-www-form-urlencoded")
    public Response getRequestToken() {
        return handler.handle(getMessageContext(), 
                              getDataProvider(),
                              getValidator());
    }
}