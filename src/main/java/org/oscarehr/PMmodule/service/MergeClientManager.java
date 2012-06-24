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

package org.oscarehr.PMmodule.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.oscarehr.PMmodule.dao.MergeClientDao;
import org.oscarehr.PMmodule.model.ClientMerge;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;

public class MergeClientManager {
	private MergeClientDao mergeClientDao;
	private DemographicDao	demographicDao;

	public void setMergeClientDao(MergeClientDao mergeClientDao) {
		this.mergeClientDao = mergeClientDao;
	}

	public void merge(ClientMerge cmObj) {
		mergeClientDao.merge(cmObj);
	}

	public void unMerge(ClientMerge cmObj) {
		mergeClientDao.unMerge(cmObj);
	}

	public Integer getHead(Integer demographic_no) {
		return mergeClientDao.getHead(demographic_no);
	}

	public List<ClientMerge> getTail(Integer demographic_no) {
		return mergeClientDao.getTail(demographic_no);
	}
	public ClientMerge getClientMerge(Integer demographic_no) {
		return mergeClientDao.getClientMerge(demographic_no);
	}
	public List<Demographic>  searchMerged(ClientSearchFormBean criteria){
		List<Demographic> lst=this.demographicDao.search(criteria, false,false);
		List<Demographic> result = new ArrayList<Demographic>();
		Iterator<Demographic> items =lst.iterator();
		while(items.hasNext()){
			Demographic client=items.next();
			if(!client.getSubRecord().isEmpty()) {
				Iterator<Integer> subs = client.getSubRecord().iterator();
				while(subs.hasNext()){
					Integer cId=subs.next();
					Demographic mergedClient= demographicDao.getClientByDemographicNo(cId);
					result.add(mergedClient);
					result.add(client);
				}
			}
		}
		return result;
	}

	public void setDemographicDao(DemographicDao demographicDao) {
		this.demographicDao = demographicDao;
	}
}
