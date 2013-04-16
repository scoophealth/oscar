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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import net.oauth.OAuth;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.OAuthValidator;

import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.rs.security.oauth.data.Client;
import org.apache.cxf.rs.security.oauth.data.RequestToken;
import org.apache.cxf.rs.security.oauth.data.RequestTokenRegistration;
import org.apache.cxf.rs.security.oauth.provider.OAuthDataProvider;
import org.apache.cxf.rs.security.oauth.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth.utils.OAuthConstants;
import org.apache.cxf.rs.security.oauth.utils.OAuthUtils;

public class OscarRequestTokenHandler {

    private static final Logger LOG = LogUtils.getL7dLogger(OscarRequestTokenHandler.class);
    private static final String[] REQUIRED_PARAMETERS = 
        new String[] {
            OAuth.OAUTH_CONSUMER_KEY,
            OAuth.OAUTH_SIGNATURE_METHOD,
            OAuth.OAUTH_SIGNATURE,
            OAuth.OAUTH_TIMESTAMP,
            OAuth.OAUTH_NONCE,
            OAuth.OAUTH_CALLBACK
        };
    
    private long tokenLifetime = 3600L;
    private String defaultScope;
    
    public Response handle(MessageContext mc, 
                           OAuthDataProvider dataProvider,
                           OAuthValidator validator) {
        try {
            OAuthMessage oAuthMessage = 
                OAuthUtils.getOAuthMessage(mc, mc.getHttpServletRequest(), REQUIRED_PARAMETERS);

            Client client = dataProvider
                .getClient(oAuthMessage.getParameter(OAuth.OAUTH_CONSUMER_KEY));
            //client credentials not found
            if (client == null) {
                throw new OAuthProblemException(OAuth.Problems.CONSUMER_KEY_UNKNOWN);
            }

            OAuthUtils.validateMessage(oAuthMessage, client, null, 
                                       dataProvider, validator);

            String callback = oAuthMessage.getParameter(OAuth.OAUTH_CALLBACK);
            validateCallbackURL(client, callback);

            List<String> scopes = OAuthUtils.parseParamValue(
            		mc.getHttpServletRequest().getParameter(OAuthConstants.X_OAUTH_SCOPE), defaultScope);
            
            RequestTokenRegistration reg = new RequestTokenRegistration();
            reg.setClient(client);
            reg.setCallback(callback);
            reg.setState(oAuthMessage.getParameter("state"));
            reg.setScopes(scopes);
            reg.setLifetime(tokenLifetime);
            reg.setIssuedAt(System.currentTimeMillis() / 1000);
            
            RequestToken requestToken = dataProvider.createRequestToken(reg);

            if (LOG.isLoggable(Level.FINE)) {
                LOG.log(Level.FINE, "Preparing Temporary Credentials Endpoint correct response");
            }
            //create response
            Map<String, Object> responseParams = new HashMap<String, Object>();
            responseParams.put(OAuth.OAUTH_TOKEN, requestToken.getTokenKey());
            responseParams.put(OAuth.OAUTH_TOKEN_SECRET, requestToken.getTokenSecret());
            responseParams.put(OAuth.OAUTH_CALLBACK_CONFIRMED, Boolean.TRUE);

            String responseBody = OAuth.formEncode(responseParams.entrySet());

            return Response.ok(responseBody).build();
        } catch (OAuthProblemException e) {
            LOG.log(Level.WARNING, "An OAuth-related problem: {0}", new Object[] {e.fillInStackTrace()});
            int code = e.getHttpStatusCode();
            if (code == HttpServletResponse.SC_OK) {
                code = e.getProblem() == OAuth.Problems.CONSUMER_KEY_UNKNOWN
                    ? 401 : 400; 
            }
            return OAuthUtils.handleException(mc, e, code);
        } catch (OAuthServiceException e) {
            return OAuthUtils.handleException(mc, e, HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Unexpected internal server exception: {0}",
                new Object[] {e.fillInStackTrace()});
            return OAuthUtils.handleException(mc, e, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected void validateCallbackURL(Client client,
                                       String oauthCallback) throws OAuthProblemException {
        // the callback must not be empty or null, and it should either match
        // the pre-registered callback URI or have the common root with the
        // the pre-registered application URI
        if (!StringUtils.isEmpty(oauthCallback) 
            && (!StringUtils.isEmpty(client.getCallbackURI())
                && oauthCallback.equals(client.getCallbackURI())
                || !StringUtils.isEmpty(client.getApplicationURI())
                && oauthCallback.startsWith(client.getApplicationURI()))) {
            return;
        }
        OAuthProblemException problemEx = new OAuthProblemException(
            OAuth.Problems.PARAMETER_REJECTED + " - " + OAuth.OAUTH_CALLBACK);
        problemEx
            .setParameter(OAuthProblemException.HTTP_STATUS_CODE,
                HttpServletResponse.SC_BAD_REQUEST);
        throw problemEx;
    }

    public void setTokenLifetime(long tokenLifetime) {
        this.tokenLifetime = tokenLifetime;
    }

    public void setDefaultScope(String defaultScope) {
        this.defaultScope = defaultScope;
    }
            
}
