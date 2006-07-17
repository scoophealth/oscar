package org.caisi.PMmodule.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.PMmodule.dao.ProgramQueueDao;
import org.caisi.PMmodule.model.ProgramQueue;
import org.caisi.PMmodule.service.ProgramManager;
import org.caisi.PMmodule.utility.Utility;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProgramQueueDaoHibernate  extends HibernateDaoSupport 
implements ProgramQueueDao  
{
	/*
	 * (1)When maximum allowed admission is reached (e.g. 20)
	 * -- all Bed Program admission/discharge is processed through the 
	 *    program queuing module.
	 * -- all Service Program discharge is still allowed.   
	 * -- all Service Program admission however, are processed through the 
	 *    program queuing module. 
	 * 
	 * (2)Program Queuing Module
	 * -- clients are inserted into the 'program_queue' table in their 
	 *    respective order.
	 * -- when a client is discharged from the 'admission' table, the system 
	 *    immediately checks the 'program_queue' table for clients --> if
	 *    clients are present, admit the client with the lowest id number 
	 *    (auto-increment).
	 * -- ??? Keep a history of queuing clients in the same table or a 
	 *    separate table. 
	 * 
	 * 
	 */	
	 private String dbTable = " ProgramQueue  q ";

	 private Log log = LogFactory.getLog(ProgramQueueDaoHibernate.class);

//###############################################################################
//this is a unique resultset
	
	 
	 public String getQueueId(String programId, String demographicNo)
	 {
	    String queryStr = " FROM " + dbTable +
                          " WHERE  q.ProgramId=? " +
                          " AND    q.ClientId=? ";

		List rs = getHibernateTemplate().find(queryStr, new Object[]{programId,demographicNo} );
		if(rs != null  &&  rs.size() > 0)
		{
			return	((ProgramQueue)rs.get(0)).getId().toString();
		}
		 return "";
	}
	    
//	###############################################################################
	public ProgramQueue getProgramQueue(Long queueId)
	{
//		String queueIdStr = getQueueId(programId, demographicNo);
		ProgramQueue pq = (ProgramQueue)getHibernateTemplate().get(ProgramQueue.class, queueId);

	    return pq;
	}

//	###############################################################################

	public List getAllProgramQueues()
	{
		List rs = getHibernateTemplate().find(" FROM " + dbTable );
		return rs;
	}
//#################################################################################
	public List getBedProgramQueues(ProgramManager programMgr, String programId)
	{
	    if( programMgr == null  ||  programId == null  ||  programId.length() <= 0)
	    {
	    	return null;
	    }
	    
	    ProgramQueue programQueue = null;
	    List programQueues = new ArrayList();
	    List rs = new ArrayList();
    
	    String queryStr = " FROM " + dbTable +
	    				  " WHERE  q.ProgramId=? " + 	
	                      " ORDER BY  q.Id ";

	    rs = getHibernateTemplate().find( queryStr, new Object[]{programId} );
	    
	    if(rs == null  ||  rs.size() <= 0)
	    {
	    	return null;
	    }

	    ListIterator  listIterator = rs.listIterator();
	    
		while(listIterator.hasNext())
		{
			try
	        {
				programQueue = (ProgramQueue)listIterator.next();
		
				if(programMgr.isBedProgram(String.valueOf(programQueue.getProgramId())))
				{
					programQueues.add(programQueue);
				}
	        }
			catch(Exception ex)
			{
				return null;
			}
		}
		
		return programQueues;
	}
//#################################################################################
	public List getServiceProgramQueues(ProgramManager programMgr, String programId)
	{
	    if( programMgr == null  ||  programId == null  ||  programId.length() <= 0)
	    {
	    	return null;
	    }
	    
	    ProgramQueue programQueue = new ProgramQueue();
	    List programQueues = new ArrayList();
	    List rs = new ArrayList();
    
	    String queryStr = "FROM " + dbTable +
	    				  " WHERE  q.ProgramId=?" + 
	                      " ORDER BY  q.Id ";

		rs = getHibernateTemplate().find( queryStr, programId );
	    
	    if(rs == null  ||  rs.size() <= 0)
	    {
	    	return null;
	    }
	    ListIterator  listIterator = rs.listIterator();
	    
		while(listIterator.hasNext())
		{
			try
	        {
				programQueue = (ProgramQueue)listIterator.next();
		
				if(!programMgr.isBedProgram(String.valueOf(programQueue.getProgramId())))
				{
					programQueues.add(programQueue);
				}
	        }
			catch(Exception ex)
			{
				return null;
			}
		}
		
		return programQueues;
	}
//################################################################################
	public List getProgramQueuesByClient(String demographicNo)
	{
	    if( demographicNo == null  ||  demographicNo.length() <= 0)
		{
		    	return null;
		}

	    String queryStr = " FROM " + dbTable +
                          " WHERE   q.ClientId=? " +
                          " ORDER BY  q.ProgramId  ";
	    
		List rs = getHibernateTemplate().find(queryStr, new Object[]{demographicNo} );
		
		if(rs != null &&  rs.size() > 0)
		{
			return rs;
		}

		return null;
	}

//################################################################################
	public List getProgramQueuesByProgramId(String programId)
	{
	    if( programId == null  ||  programId.length() <= 0)
		{
		    	return null;
		}

	    String queryStr = " FROM " + dbTable +
                          " WHERE   q.ProgramId=? " +
                          " ORDER BY  q.Id  ";
	    
		return getHibernateTemplate().find(queryStr, programId );
	}
	
	public List getActiveProgramQueuesByProgramId(String programId) {
		return this.getHibernateTemplate().find("from ProgramQueue pq where pq.ProgramId = ? and pq.Status = 'active' order by pq.ReferralDate",programId);
	}
//################################################################################
	public List getClientIdsByProgramIds(List programIds)
	{
	    if( programIds == null  ||  programIds.size() <= 0)
		{
		    	return null;
		}

	    String queryStr = "";
	    List clientIdsReferredToProvider = new ArrayList();
	    List filteredClientIdsReferredToProvider = new ArrayList();
	    
	    for(int i=0; i < programIds.size(); i++)
	    {
	    	
	    	queryStr = " select  q.ClientId   FROM " + dbTable +
                       " WHERE   q.ProgramId=? ";
	    
	    	List rs = getHibernateTemplate().find(queryStr, new Object[]{programIds.get(i)} );
		
	    	if(rs != null  &&  rs.size() > 0)
	    	{
	    		
	    		for(int j=0; j < rs.size(); j++)
	    		{
	    			clientIdsReferredToProvider.add(rs.get(j).toString()); //clients may be repeated many times.
	    		}
	    	}
	    }
	    
//clientIdsReferredToProvider.add("201"); //testing only
//clientIdsReferredToProvider.add("202"); //testing only
	    
	    
if(clientIdsReferredToProvider != null)
{

}
else
{
}
	    //Filter out repeated clientIds
	    filteredClientIdsReferredToProvider = Utility.filterOutDuplicateStrTokens(clientIdsReferredToProvider);
	    

if(filteredClientIdsReferredToProvider != null)
{
}
else
{
}
	    if(filteredClientIdsReferredToProvider == null  ||  filteredClientIdsReferredToProvider.size() <= 0)
	    {
	    	return null;
	    }
	
	    return  filteredClientIdsReferredToProvider;
	
	}

//################################################################################
	public int getNumOfProgramQueuesByProgramId(String programId)
	{
	    if( programId == null  ||  programId.length() <= 0)
		{
		    	return 0;
		}

	    List rs = getProgramQueuesByProgramId( programId);		
	    
		if(rs != null &&  rs.size() > 0)
		{
			return rs.size();
		}

		return 0;
	}
	
//################################################################################
	public boolean isClientAlreadyReferredToProgram(String demographicNo, String programId)
	{
	    if( demographicNo == null  ||  
		    	programId == null  ||  programId.length() <= 0)
		{
		   	return false;
		}

	    String queryStr = " FROM " + dbTable +
        " WHERE   q.ProgramId=?  AND  q.ClientId=?";

	    
		List rs = getHibernateTemplate().find(queryStr, new Object[]{programId,demographicNo} );
		
		if(rs == null ||  rs.size() <= 0)
		{
			return false;
		}

		return true;
	}

//#################################################################################
	
	public boolean isClientReferredByProvider(String demographicNo, String providerNo)
	{
	    if( demographicNo == null  ||  demographicNo.length() <= 0  ||
	    	providerNo == null  ||  providerNo.length() <= 0 )
		{
		   	return false;
		}

	    String queryStr = " FROM " + dbTable +
		  				  " WHERE  q.ClientId=? " + 
		  				  " AND    q.ProviderNo=? ";

	    
	    List rs = getHibernateTemplate().find(queryStr,  new Object[]{demographicNo,providerNo});

	    if(rs != null  &&  rs.size() > 0)
	    {
	    	return true;
	    }

		return false;
	}
	
//###############################################################################

	public boolean isProgramIdInProgramQueue(String programId)
	{
	    if( programId == null  ||  programId.length() <= 0)
	    {
	       	return false;
	    }
		String queryStr = " SELECT  q.ProgramId  FROM " + dbTable +
						  " WHERE   q.ProgramId=? ";
		
		List rs = getHibernateTemplate().find(queryStr, new Object[]{programId} );

		if(rs != null  &&  rs.size() > 0)
		{
			return true;
		}
		
		return false;
	}
	
//#################################################################################	
	
	  public void saveProgramQueue(ProgramQueue programQueue)
	  {
		  
	      if(programQueue == null)
	      {
	    	  return;
	      }
	        	
	      getHibernateTemplate().saveOrUpdate(programQueue);
	      
	  }
//	#################################################################################	

	  public void updateProgramQueue(ProgramQueue programQueue)
	  {
	  	  
	      if(programQueue == null)
	      {
	    	  return;
	      }
	        	
	      getHibernateTemplate().update(programQueue);

	      if(log.isDebugEnabled())
	      {
	    	  log.debug("programQueueId set to: " + programQueue.getId());
	      }
        	
	  }

//	################################################################################

	  public void removeProgramQueue(Long queueId)
	  {
	      
	      Object programQueue = getHibernateTemplate().load(ProgramQueue.class, queueId);
	      
	      getHibernateTemplate().delete(programQueue);
	      
	  }

//	###############################################################################

	  public ProgramQueue getQueue(String programId, String clientId) {
		List results = this.getHibernateTemplate().find("from ProgramQueue pq where pq.ProgramId = ? and pq.ClientId = ?",
				new Object[] {Long.valueOf(programId),Long.valueOf(clientId)});
		if(results.size()>0) {
			return (ProgramQueue)results.get(0);
		}
		return null;
	  }
	  
	public ProgramQueue getActiveProgramQueue(Long programId, String demographicNo) {
		List results = this.getHibernateTemplate().find("from ProgramQueue pq where pq.ProgramId = ? and pq.ClientId = ? and pq.Status='active'",
				new Object[] {programId,Long.valueOf(demographicNo)});
		if(results.size()>0) {
			return (ProgramQueue)results.get(0);
		}
		return null;
	}
}
