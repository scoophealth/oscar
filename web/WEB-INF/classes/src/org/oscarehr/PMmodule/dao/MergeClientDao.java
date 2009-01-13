package org.oscarehr.PMmodule.dao;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.oscarehr.PMmodule.model.ClientMerge;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.quatro.common.KeyConstants;

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
	    
	    public List getTail(Integer demographic_no){	        
	        String sql = "from ClientMerge where mergedToClientId = ? and deleted = 0";	              
	        List  lst= getHibernateTemplate().find(sql, new Object[] {demographic_no});
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
