package org.caisi.PMmodule.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.PMmodule.dao.AdmissionDao;
import org.caisi.PMmodule.dao.Database_Service;
import org.caisi.PMmodule.model.Admission;
import org.caisi.PMmodule.model.Program;
import org.caisi.PMmodule.service.ProgramManager;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class AdmissionDaoHibernate  extends HibernateDaoSupport 
implements AdmissionDao  
{
	 private String dbTable = " Admission  a ";
	  private Log log = LogFactory.getLog(AdmissionDaoHibernate.class);

//###############################################################################
	 
public Admission getAdmission(String programId, String demographicNo)
{
    if( programId == null  ||  programId.length() <= 0  ||
    	demographicNo == null  ||  demographicNo.length() <= 0)
    {
    	return null;
    }
    String queryStr = " FROM " + dbTable +
    				  " WHERE  a.ProgramId=? " +
    				  " AND    a.ClientId=? ";
    List rs = getHibernateTemplate().find(queryStr, new Object[]{programId,demographicNo} );
    if(rs != null  &&  rs.size() > 0)
    {
    	return	((Admission)rs.get(0));
    }
    
    return null;
}
//###############################################################################
		
public Admission getCurrentAdmission(String programId, String demographicNo)
{
    if( programId == null  ||  programId.length() <= 0  ||
        demographicNo == null  ||  demographicNo.length() <= 0)
    {
       	return null;
    }
	String queryStr = " FROM " + dbTable +
	                  " WHERE a.ProgramId=? " + 
      				  " AND    a.ClientId=? " + 
      				  " AND  a.AdmissionStatus='current' " + 
      				  " ORDER BY  a.AdmissionDate  DESC ";
	List rs = getHibernateTemplate().find(queryStr, new Object[]{programId,demographicNo} );
	if(rs != null  &&  rs.size() > 0)
	{
		return  ((Admission)rs.get(0));
	}
	    
	return null;

}
//###############################################################################
	
public List getAdmissions()
{
	  String queryStr = " FROM " + dbTable +
	  					" ORDER BY  a.AdmissionDate  DESC ";
	  List rs = getHibernateTemplate().find(queryStr);

	  return rs;
}
//###############################################################################

public List getAdmissions(String demographicNo)
{
    if( demographicNo == null  ||  demographicNo.length() <= 0)
    {
       	return null;
    }
	String queryStr = " FROM " + dbTable +
					  " WHERE   a.ClientId=? " + 
					  " ORDER BY  a.AdmissionDate  DESC ";
	List rs = getHibernateTemplate().find(queryStr, new Object[]{demographicNo} );

	return rs;
}
//###############################################################################
	
public List getBedProgramAdmissionHistory(ProgramManager programMgr, String demographicNo)
{
    if( programMgr == null  ||  demographicNo == null  ||  demographicNo.length() <= 0)
    {
       	return null;
    }
	String queryStr = " FROM " + dbTable +
					  " WHERE   a.ClientId=? " + 
					  " ORDER BY  a.AdmissionDate  DESC ";
	Admission admission = null;
	List admissions = new ArrayList();
	List rs = new ArrayList();
	  
    rs = getHibernateTemplate().find( queryStr, new Object[]{demographicNo} );
    
    if(rs == null  ||  rs.size() <= 0)
    {
    	return null;
    }
    ListIterator  listIterator = rs.listIterator();
	while(listIterator.hasNext())
	{
		try
        {
			admission = (Admission)listIterator.next();
	
			if(programMgr.isBedProgram(String.valueOf(admission.getProgramId())))
			{
				admissions.add(admission);
			}
        }
		catch(Exception ex)
		{
			return null;
		}
	}
	return admissions;
}
//###############################################################################

public List getServiceProgramAdmissionHistory(ProgramManager programMgr, String demographicNo) 
{
    if( programMgr == null  ||  demographicNo == null  ||  demographicNo.length() <= 0)
    {
       	return null;
    }
	String queryStr = " FROM " + dbTable +
					  " WHERE   a.ClientId=? " + 
					  " ORDER BY  a.AdmissionDate  DESC ";
	Admission admission = null;
	List admissions = new ArrayList();
	List rs = new ArrayList();
	  
    rs = getHibernateTemplate().find( queryStr, new Object[]{demographicNo} );
    
    if(rs == null  ||  rs.size() <= 0)
    {
    	return null;
    }
    ListIterator  listIterator = rs.listIterator();
	while(listIterator.hasNext())
	{
		try
        {
			admission = (Admission)listIterator.next();
	
			if(programMgr.isServiceProgram(String.valueOf(admission.getProgramId())))
			{
				admissions.add(admission);
			}
        }
		catch(Exception ex)
		{
			return null;
		}
	}
	return admissions;

}
//###############################################################################

public List getAdmissions(String demographicNo, int rowCountPass, int totalRowDisplay)
{
    if( demographicNo == null  ||  demographicNo.length() <= 0  ||
    	rowCountPass < 0  ||  totalRowDisplay < 0 )
    {
       	return null;
    }
	String queryStr = " FROM " + dbTable +
					  " WHERE   a.ClientId=? " + 
					  " ORDER BY  a.AdmissionDate  DESC " +
    				  " LIMIT  ?,? ";
    List rs = getHibernateTemplate().find( queryStr, 
    		  new Object[]{demographicNo, new Integer(rowCountPass), new Integer(totalRowDisplay)} );
    				  
    return rs;
}
//###############################################################################

public List getCurrentAdmissions(String demographicNo, int rowCountPass, int totalRowDisplay)
{
	
    if( demographicNo == null  ||  demographicNo.length() <= 0  ||
        	rowCountPass < 0  ||  totalRowDisplay < 0 )
    {
       	return null;
    }
    String queryStr = " FROM " + dbTable +
    				  " WHERE   a.ClientId=? " + 
    				  " AND  a.AdmissionStatus='current' " + 
    				  " ORDER BY  a.AdmissionDate  DESC " +
       				  " LIMIT  ?,? ";
    List rs = getHibernateTemplate().find( queryStr, 
       		  new Object[]{demographicNo, new Integer(rowCountPass), new Integer(totalRowDisplay)} );
        				  
    return rs;

}
//###############################################################################

public List getCurrentAdmissions(String demographicNo)
{
    if( demographicNo == null  ||  demographicNo.length() <= 0 )
    {
       	return null;
    }
    String queryStr = " FROM " + dbTable +
    				  " WHERE   a.ClientId=? " + 
    				  " AND  a.AdmissionStatus='current' " + 
    				  " ORDER BY  a.AdmissionDate  DESC ";
    List rs = getHibernateTemplate().find( queryStr, new Object[]{demographicNo} );
        				  
    return rs;

}
//###############################################################################

public Admission getCurrentBedProgramAdmission(ProgramManager programMgr, String demographicNo)
{
    if( programMgr == null  ||  demographicNo == null  ||  demographicNo.length() <= 0)
    {
       	return null;
    }
	String queryStr = " FROM " + dbTable +
	  " WHERE   a.ClientId=? " + 
	  " AND a.AdmissionStatus='current' " +
	  " ORDER BY  a.AdmissionDate  DESC ";
    
    Admission admission = null;
    List admissions = new ArrayList();
    List rs = new ArrayList();

    rs = getHibernateTemplate().find( queryStr, new Object[]{demographicNo} );

    if(rs == null  ||  rs.size() <= 0)
    {
    	return null;
    }
    ListIterator  listIterator = rs.listIterator();
    while(listIterator.hasNext())
    {
    	try
        {
    	    admission = (Admission)listIterator.next();
    	    if(programMgr.isBedProgram(String.valueOf(admission.getProgramId())))
    	    {
    	    	return admission;
    	    }
    	}
    	catch(Exception ex)
    	{
    		return null;
    	}
    }
    return null;
}
//###############################################################################

public List getCurrAdmissionRecordsOfABedProgram(ProgramManager programMgr, String bedProgramId)
{
    if( programMgr == null  ||  bedProgramId == null  ||  bedProgramId.length() <= 0)
    {
       	return null;
    }
	if(!programMgr.isBedProgram(bedProgramId))
	{
		return null;
	}
    String queryStr = " FROM " + dbTable +
	  				  " WHERE   a.ProgramId=? " + 
	  				  " AND  a.AdmissionStatus='current' " + 
	  				  " ORDER BY  a.AdmissionDate  DESC ";
    List rs = getHibernateTemplate().find( queryStr, new Object[]{bedProgramId} );

    return rs;
}
//###############################################################################

public List getCurrAdmissionRecordsOfAServiceProgram(ProgramManager programMgr, String serviceProgramId)
{
    if( programMgr == null  ||  serviceProgramId == null  ||  serviceProgramId.length() <= 0)
    {
       	return null;
    }
	if(!programMgr.isServiceProgram(serviceProgramId))
	{
		return null;
	}
    String queryStr = " FROM " + dbTable +
	  				  " WHERE   a.ProgramId=? " + 
	  				  " AND  a.AdmissionStatus='current' " + 
	  				  " ORDER BY  a.AdmissionDate  DESC ";
    List rs = getHibernateTemplate().find( queryStr, new Object[]{serviceProgramId} );

    return rs;

}
//###############################################################################
public List getCurrentServiceProgramAdmission(ProgramManager programMgr, String demographicNo)
{
    if( programMgr == null  ||  demographicNo == null  ||  demographicNo.length() <= 0)
    {
       	return null;
    }
	String queryStr = " FROM " + dbTable +
	  " WHERE   a.ClientId=? " + 
	  " AND     a.AdmissionStatus='current' " +
	  " ORDER BY  a.AdmissionDate  DESC ";
    
    Admission admission = null;
    List admissions = new ArrayList();
    List rs = new ArrayList();

    rs = getHibernateTemplate().find( queryStr, new Object[]{demographicNo} );

    if(rs == null  ||  rs.size() <= 0)
    {
    	return null;
    }
    ListIterator  listIterator = rs.listIterator();
    while(listIterator.hasNext())
    {
    	try
        {
    	    admission = (Admission)listIterator.next();
    	    if(programMgr.isServiceProgram(String.valueOf(admission.getProgramId())))
    	    {
    	    	admissions.add(admission);
    	    }
    	}
    	catch(Exception ex)
    	{
    		return null;
    	}
    }
    return admissions;
}
//###############################################################################
// haven't tested this method !!!
/*
 * Used for client based security view
 * 
 * 	use   programIds to filter out all the  clientIds  that are in them  from  the  admission  table.
 */
public List getClientIdsFromProgramIds(String providerNo, List selectedProgramIds)
{
    if( providerNo == null  ||  providerNo.length() <= 0 ||
    	selectedProgramIds == null ||  selectedProgramIds.size() <= 0     )
    {
       	return null;
    }

    String conditionStr = "";
    for(int i=0;  i < selectedProgramIds.size();  i++)
    {
    	if(selectedProgramIds.get(i) != null  &&  i == 0)
    	{
    		conditionStr  +=  "  and ( program_id = " +  ((Long)selectedProgramIds.get(i)).longValue();
    	}
    	else if(selectedProgramIds.get(i) != null  &&  i > 0)
    	{
    		conditionStr  +=  "  or  program_id = " +  ((Long)selectedProgramIds.get(i)).longValue();
    	}
    }

    String queryStr = "";
    List allProgramIds = new ArrayList();
    
   	queryStr = " SELECT distinct  a.ClientId  FROM " + dbTable + 
               " WHERE  a.ProviderNo=? " + conditionStr + " ) " ;

    Query q = getSession().createQuery(queryStr);
    q.setLong(0, Long.parseLong(providerNo) );//??? Long return ???
    List rs = q.list();
        	
    if(rs == null  ||  rs.size() <= 0)
    {
      	return null;
    }
   	
    return rs;
}

//###############################################################################
//haven't tested this method !!!
/*
* Used for client based security view
* 
* 	use   programIds to filter out all the  clientIds  that are not in them  from  the  admission  table.
*/
public List getClientIdsFromProgramIdsNotEqual(
		Database_Service databaseService, DataSource dataSource, 
		String providerNo, List selectedProgramIds)
{
	if( databaseService == null  ||  dataSource == null  ||
		providerNo == null  ||  providerNo.length() <= 0 ||
		selectedProgramIds == null ||  selectedProgramIds.size() <= 0     )
	{
    	return null;
	}

	String conditionStr = "";
	
	try
	{
		for(int i=0;  i < selectedProgramIds.size();  i++)
		{
			if(selectedProgramIds.get(i) != null  &&  i == 0)
			{
				conditionStr  +=  "  and ( program_id <> " +  ((Long)selectedProgramIds.get(i)).longValue();
			}
			else if(selectedProgramIds.get(i) != null  &&  i > 0)
			{
				conditionStr  +=  "  and  program_id <> " +  ((Long)selectedProgramIds.get(i)).longValue();
			}
		}

		String queryStr = "";
 
		queryStr =  " SELECT distinct  a.client_id  FROM  admission a" +  
					" INNER  JOIN  demographic_pmm d " +
					" ON  a.client_id=d.demographic_no " +
					" WHERE  d.sharing='Y' " +
					conditionStr + " ) " ;
		
		List rs = databaseService.getTableQueryArrayList(dataSource,queryStr);
		String clientIdTemp = "";
		if(rs != null  &&  rs.size() > 0)
		{
			for(int i=0; i < rs.size(); i++)
			{
				clientIdTemp = rs.get(i).toString();
				if(clientIdTemp.indexOf("[") >= 0)
				{
					rs.set(i, clientIdTemp.substring(1, clientIdTemp.length()-1));
				}
			}			
			return rs;
		}
	}
	catch(Exception e)
	{
		return null;
	}
	finally
	{
	}

	return null;
}

//###############################################################################

public boolean isProgramIdInAdmission(String programId)
{
    if( programId == null  ||  programId.length() <= 0)
    {
       	return false;
    }
	String queryStr = " SELECT  a.ProgramId  FROM " + dbTable +
					  " WHERE   a.ProgramId=? ";
	
	List rs = getHibernateTemplate().find(queryStr, new Object[]{programId} );

	if(rs != null  &&  rs.size() > 0)
	{
		return true;
	}
	
	return false;
}

//###############################################################################

public boolean addAdmission(Admission admission, ProgramManager programMgr, Program program)
{
	if(admission == null  ||  programMgr == null  ||  program == null )
    {
     	return false;
    }
/*
    if( !programMgr.incrementNumOfMembers(program) )
    {
        return false;
    }
*/    
    String notes = admission.getAdmissionNotes();

    if(notes == null  ||  notes.equals(""))
    {
        admission.setAdmissionNotes(".");
    }

    try
    {
    	getHibernateTemplate().save(admission);
        boolean updateOK = programMgr.ensureNumOfMembersCorrect(program.getId().toString());
    }
    catch(Exception ex)
    {
    	return false;
    }

    return true;
}
//###############################################################################

public boolean dischargeAdmission(ProgramManager programMgr, String programId, Admission admission, String amId)
{
	if( admission == null  ||  programMgr == null  ||  
		programId == null  ||  programId.length() <= 0  ||
		amId == null  ||  amId.length() <= 0 )
    {
     	return false;
    }
	
	String notes = admission.getAdmissionNotes();

	if(notes == null  ||  notes.equals(""))
	{
		admission.setAdmissionNotes(".");
	}

    try
    {
    	getHibernateTemplate().update(admission);
    }
    catch(Exception ex)
    {
    	return false;
    }
/*
	if(!programMgr.decrementNumOfMembers(programId))
	{
		return false;
	}
*/	
    boolean updateNumOK = programMgr.ensureNumOfMembersCorrect(programId);
	
	return true;

}
//###############################################################################

public boolean updateAdmission(Admission admission)
{
	if(admission == null)
	{
		return false;
	}

    try
    {
    	getHibernateTemplate().update(admission);
    }
    catch(Exception ex)
    {
    	return false;
    }

    return true;
}
//###############################################################################

public boolean removeAdmission(String amId)
{
	if(amId == null  ||  amId.length() <= 0)
	{
		return false;
	}

    Object admission = getHibernateTemplate().load(Admission.class, Long.valueOf(amId));
    
    try
    {
    	getHibernateTemplate().delete(admission);
    }
    catch(Exception ex)
    {
    	return false;
    }

    return true;
}
//###############################################################################
/*
public Admission setAdmissionObj(Admission admission, ResultSet rs)
{
	
}
*/
//###############################################################################

	public List getCurrentAdmissionsByProgramId(Long programId) {
		return this.getHibernateTemplate().find("from Admission a where a.ProgramId = ? and a.AdmissionStatus='current'",programId);
	}
	
	public List getAllAdmissionsByProgramId(Long programId) {
		return this.getHibernateTemplate().find("from Admission a where a.ProgramId = ?",programId);
	}
	public Admission getAdmission(Long id) {
		return (Admission)this.getHibernateTemplate().get(Admission.class,id);
	}
	
	public void saveAdmission(Admission admission) {
		this.getHibernateTemplate().saveOrUpdate(admission);
	}
	
	public List getAdmissionsInTeam(Long programId, Long teamId) {
		return this.getHibernateTemplate().find("from Admission a where a.ProgramId = ? and a.TeamId = ? and a.AdmissionStatus='current'",
				new Object[] {programId,teamId});
	}
}
