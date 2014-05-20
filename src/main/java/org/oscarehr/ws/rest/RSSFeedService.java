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

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.to.RSSResponse;
import org.oscarehr.ws.rest.to.model.RssItem;
import org.springframework.stereotype.Component;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


@Path("/rssproxy")
@Component("rssFeedService")
/**
 * Only works with K2A right now..this should be changed up to read from some persistent, editable source.
 * @author marc
 *
 */
public class RSSFeedService {

	Logger logger = MiscUtils.getLogger();
	
	@GET
	@Path("/rss")
	@Produces("application/json")
	public org.oscarehr.ws.rest.to.RSSResponse getRSS(@QueryParam("key") String key) {
		RSSResponse response = new RSSResponse();
		response.setTimestamp(new Date());
		
		
		try {
			String urlStr = null;
			
			if(key.equals("k2a")) {
				urlStr = "http://www.mydrugref.org/feed/rss";
			}
			
			if(urlStr == null) {
				return response;
			}
			
			URL url = new URL(urlStr);
			HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
			// Reading the feed
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(httpcon));
			List<SyndEntry> entries = feed.getEntries();
			for(SyndEntry entry:entries){
				RssItem item = new RssItem();
				item.setTitle(entry.getTitle());
				item.setAuthor(entry.getAuthor());
				item.setLink(entry.getLink());
				item.setPublishedDate(entry.getPublishedDate());
				item.setDescription(entry.getDescription().getValue());
				response.getContent().add(item);
			} 
			response.setTotal(response.getContent().size());
		}catch(Exception e) {
			logger.error("error",e);
			return null;
		}
		return response;
	}
}
