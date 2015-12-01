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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.oscarehr.app.AppOAuth1Config;
import org.oscarehr.app.OAuth1Utils;
import org.oscarehr.common.dao.AppDefinitionDao;
import org.oscarehr.common.dao.AppUserDao;
import org.oscarehr.common.model.AppDefinition;
import org.oscarehr.common.model.AppUser;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.ws.rest.to.RSSResponse;
import org.oscarehr.ws.rest.to.model.RssItem;
import org.springframework.stereotype.Component;


@Path("/rssproxy")
@Component("rssFeedService")
/**
 * Only works with K2A right now..this should be changed up to read from some persistent, editable source.
 * @author marc
 *
 */
public class RSSFeedService extends AbstractServiceImpl {

	Logger logger = MiscUtils.getLogger();
	
	@GET
	@Path("/rss")
	@Produces("application/json")
	public org.oscarehr.ws.rest.to.RSSResponse getRSS(@QueryParam("key") String key,@QueryParam("startPoint") String startPoint, @QueryParam("numberOfRows") String numberOfRows, @Context HttpServletRequest request) {
		RSSResponse response = new RSSResponse();
		response.setTimestamp(new Date());
		try {
			if(key.equals("k2a")) {
				AppDefinitionDao appDefinitionDao = SpringUtils.getBean(AppDefinitionDao.class);
	    		AppUserDao appUserDao = SpringUtils.getBean(AppUserDao.class);
	    		
	    		AppDefinition k2aApp = appDefinitionDao.findByName("K2A");
			
	    		if(k2aApp != null) {
		    		AppUser k2aUser = appUserDao.findForProvider(k2aApp.getId(),getLoggedInInfo().getLoggedInProviderNo());
		    		
		    		if(k2aUser != null) {
		    			LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		    			String jsonString = OAuth1Utils.getOAuthGetResponse(loggedInInfo,k2aApp, k2aUser, "/ws/api/posts/userFriendsRecentPosts?startingPoint=" + startPoint + "&numberOfRows=" + numberOfRows, "/ws/api/posts/userFriendsRecentPosts");
			    		JSONArray jsonArray = new JSONArray();
			    		
			    		if(jsonString != null && !jsonString.isEmpty()) {
			    			jsonArray = new JSONArray(jsonString);
			    			
			    			for (int i = 0; i < jsonArray.length(); i++) {
			    	        	JSONObject post = jsonArray.getJSONObject(i);
			    	        	
			    	        	RssItem item = new RssItem();
			    	        	item.setId(Long.parseLong(post.getString("id")));
			    				item.setTitle(post.getString("name"));
			    				item.setAuthor(post.getString("author"));
			    				item.setType(post.getString("type"));
			    				
			    				AppOAuth1Config appAuthConfig = AppOAuth1Config.fromDocument(k2aApp.getConfig());
			    				item.setLink(appAuthConfig.getBaseURL() + "/#/ws/rs/posts/" + post.getString("id"));
			    				
			    				Date date = null;
			    				try {
				    				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		    	        			date = formatter.parse(post.getString("updatedAt"));
			    				} catch(ParseException e) {
			    					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		    	        			date = formatter.parse(post.getString("updatedAt"));
			    				}
			    				item.setPublishedDate(date);
			    				item.setBody(post.getString("body"));
			    				item.setAgreeCount(Long.parseLong(post.getString("agreeCount")));
			    				item.setDisagreeCount(Long.parseLong(post.getString("disagreeCount")));
			    				item.setCommentCount(Long.parseLong(post.getString("commentCount")));
			    				if(post.has("significance")) {
			    					item.setSignificance(post.getString("significance"));
			    				}
			    				if(post.has("agree")) {
			    					item.setAgree(post.getBoolean("agree"));
			    				}
			    				if(post.has("disagree")) {
			    					item.setDisagree(post.getBoolean("disagree"));
			    				}
			    				if(post.has("agreeId")) {
			    					item.setAgreeId(Long.parseLong(post.getString("agreeId")));
			    				}
			    				if(post.has("comments")) {
			    					List<RssItem> commentItems = new ArrayList<RssItem>();
			    					JSONArray comments =  post.getJSONArray("comments");
			    					
			    					for (int j = 0; j < comments.length(); j++) {
					    	        	JSONObject comment = comments.getJSONObject(j);
					    	        	
					    	        	RssItem commentItem = new RssItem();
					    	        	commentItem.setId(Long.parseLong(comment.getString("id")));
					    	        	commentItem.setAuthor(comment.getString("author"));
					    	        	commentItem.setType(comment.getString("type"));
					    				Date commentDate = null;
					    				if(comment.has("createdAt")) {
						    				try {
							    				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
					    	        			commentDate = formatter.parse(comment.getString("createdAt"));
						    				} catch(ParseException e) {
						    					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
					    	        			commentDate = formatter.parse(comment.getString("createdAt"));
						    				}
					    				}
					    				commentItem.setPublishedDate(commentDate);
					    				commentItem.setBody(comment.getString("body"));
					    				
					    				commentItems.add(commentItem);
			    					}
			    					item.setComments(commentItems);
			    				}
			    					
			    				response.getContent().add(item);
			    			}
			    			response.setTotal(response.getContent().size());
			    		}
		    		} else {
			    		RssItem item = new RssItem();
			    		item.setType("Utilize K2A Now!");
			    		item.setBody("Receive the latest evidence and information from your trusted network! Sign into K2A now, by visiting User Settings > Integration > K2A Login.");
			    		item.setId((long)k2aApp.getId());
			    		response.getContent().add(item);
			    	}
	    		}
			} else {
				return response;
			}
		}catch(Exception e) {
			logger.error("error",e);
			return null;
		}
		return response;
	}
}
