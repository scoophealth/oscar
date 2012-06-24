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

package org.oscarehr.PMmodule.dao;


import java.util.Iterator;
import java.util.List;

import org.oscarehr.PMmodule.model.ClientMerge;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class MergeClientDao extends HibernateDaoSupport {

	 public void merge(ClientMerge cmObj) {
	        if (cmObj == null) {
	            throw new IllegalArgumentException();
	        }
	        this.getHibernateTemplate().saveOrUpdate(cmObj);

	        String sql = "update ClientMerge a set a.mergedToClientId=? where a.mergedToClientId=?";
	        getHibernateTemplate().bulkUpdate(sql, new Object[] {cmObj.getMergedToClientId(),cmObj.getClientId()});

	        sql = "update Demographic a set a.merged=1 where a.DemographicNo=?";
	        getHibernateTemplate().bulkUpdate(sql, cmObj.getClientId());

	    }

	    public void unMerge(ClientMerge cmObj){
	    	String sql = "update ClientMerge c set c.deleted=1,c.providerNo=?, c.lastUpdateDate=?  where c.clientId=?";
	    	 getHibernateTemplate().bulkUpdate(sql, new Object[]{cmObj.getProviderNo(),cmObj.getLastUpdateDate(),cmObj.getClientId()} );
	    	 String sql1 = "update Demographic a set a.merged=0 where a.DemographicNo=?";
		     getHibernateTemplate().bulkUpdate(sql1, cmObj.getClientId());
	    }

	    public Integer getHead(Integer demographic_no) {
	        String queryStr = "FROM ClientMerge a WHERE a.deleted=0 and a.clientId =?";
	        ClientMerge cmObj= (ClientMerge)getHibernateTemplate().find(queryStr, new Object[] {demographic_no });
	        return cmObj.getClientId();
	    }

	    public List<ClientMerge> getTail(Integer demographic_no){
	        String sql = "from ClientMerge where mergedToClientId = ? and deleted = 0";
	        @SuppressWarnings("unchecked")
            List<ClientMerge>  lst= getHibernateTemplate().find(sql, new Object[] {demographic_no});
	        return lst;

	    }
	    public ClientMerge getClientMerge(Integer demographic_no)
	    {
	        String queryStr = "FROM ClientMerge a WHERE a.clientId =?";
	        List lst = getHibernateTemplate().find(queryStr, new Object[] {demographic_no });
	        if(lst.size() == 0) return null;
	        ClientMerge cmObj= (ClientMerge)lst.get(0);
	        return cmObj;
	    }
	    public String getMergedClientIds(Integer clientNo){
	    	String sql="select clientId from ClientMerge where mergedToClientId = ? and deleted = 0";
	    	List lst = getHibernateTemplate().find(sql, clientNo);
	    	String rtnStr=clientNo.toString();
	    	Iterator items =lst.iterator();
	    	while(items.hasNext()){
	    		Integer cId = (Integer)items.next();
	    		rtnStr+=","+cId.toString();
	    	}
	    	return "(" +rtnStr+")";
	    }
	}
