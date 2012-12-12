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

package org.oscarehr.casemgmt.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.oscarehr.casemgmt.model.ClientImage;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.QueueCache;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Anyone modifying get and set methods should take note of the dataCache and add/remove items as appropriate.
 */
public class ClientImageDAO extends HibernateDaoSupport {

	private static final Logger logger = MiscUtils.getLogger();

	/**
	 * This is a simple cache for image data because the images are excessively large (relatively speaking). The Integer key is the demographic_no.
	 */
	private static final QueueCache<Integer, ClientImage> dataCache=new QueueCache<Integer, ClientImage>(4, 40, DateUtils.MILLIS_PER_HOUR, null);
	
	public void saveClientImage(ClientImage clientImage) {
		ClientImage existing = getClientImage(clientImage.getDemographic_no());
		if (existing != null) {
			existing.setImage_data(clientImage.getImage_data());
			existing.setImage_type(clientImage.getImage_type());
			existing.setUpdate_date(new Date());
		}
		getHibernateTemplate().saveOrUpdate(clientImage);

		// update cache
		dataCache.remove(clientImage.getDemographic_no());
	}

	public ClientImage getClientImage(Integer clientId) {

		// check cache
		ClientImage clientImage = dataCache.get(clientId);
		if (clientImage == null) {
			logger.debug("dataCache miss : clientId=" + clientId);

			// get from database
			@SuppressWarnings("unchecked")
			List<ClientImage> results = getHibernateTemplate().find("from ClientImage i where i.demographic_no=? order by update_date desc", clientId);
			if (results.size() > 0) {
				clientImage = results.get(0);

				// add to cache if it's less than ... say 1 megs
				if (clientImage.getImage_data().length<1024000)
				{
					dataCache.put(clientId, clientImage);
					logger.debug("entry found in db, adding to dataCache : clientId=" + clientId);
				}
			}
		} else {
			logger.debug("dataCache hit : clientId=" + clientId);
		}

		return (clientImage);
	}
}
