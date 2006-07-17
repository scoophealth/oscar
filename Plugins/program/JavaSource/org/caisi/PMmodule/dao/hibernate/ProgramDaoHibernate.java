package org.caisi.PMmodule.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.PMmodule.dao.ProgramDao;
import org.caisi.PMmodule.model.Program;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProgramDaoHibernate  extends HibernateDaoSupport 
implements ProgramDao  
{
	 private String dbTable = " Program  p ";

	 private Log log = LogFactory.getLog(ProgramDaoHibernate.class);

//###############################################################################
	 

public Program getProgram(Integer programId)
{
	if(programId == null  ||  programId.intValue() <= 0)
	{
		return null;
	}

	Program program = (Program)getHibernateTemplate().get(Program.class, programId);
		
	return program;
}

//###############################################################################
public String getProgramName(Integer programId)
{
	if(programId == null  ||  programId.intValue() <= 0)
	{
		return "";
	}
   
    Program program = (Program)getHibernateTemplate().get(Program.class, programId);
    
    return  program.getName();
}

//###############################################################################

public Program getProgramFromName(String name)
{
	if(name == null  ||  name.length() <= 0)
	{
		return null;
	}

    String queryStr = " FROM " + dbTable +
	                  " WHERE  p.Name=?";

	List rs = getHibernateTemplate().find(queryStr, name);
	
	if(rs != null  &&  rs.size() > 0)
	{
		return  (Program)rs.get(0);
	}

    return  null;

}
//###############################################################################
public List getAllPrograms()
{
	List rs = getHibernateTemplate().find("FROM " + dbTable + " ORDER BY  p.Name ");
	return rs;
}
//###############################################################################
public List getPrograms(String queryStr)
{
	List rs = getHibernateTemplate().find(queryStr);
	return rs;

	
}
//###############################################################################

public List getProgramsByAgencyId(String agencyId)
{
	if(agencyId == null  ||  agencyId.length() <= 0)
	{
		return null;
	}

    String queryStr = " FROM " + dbTable +
	                  " WHERE  p.AgencyId=?";

	List rs = getHibernateTemplate().find(queryStr, agencyId);
	
	if(rs != null  &&  rs.size() > 0)
	{
		return  rs;
	}

    return  null;

}

//###############################################################################

public List getBedPrograms()
{
	String queryStr = " FROM  " + dbTable + 
                      " WHERE  p.Type='Bed'  ORDER BY  p.Name ";
	
	List rs = getHibernateTemplate().find(queryStr);
	return rs;
}

//###############################################################################

public List getBedProgramsWithinProgramDomain(List bedProgramIds)
{
    if(bedProgramIds == null  ||  bedProgramIds.size() <= 0 )
    {
       	return null;
    }
    
    String queryStr = "";
    String conditionStr = "";
    
    for(int i=0;  i < bedProgramIds.size();  i++)
    {
    	if(bedProgramIds.get(i) != null  &&  i == 0)
    	{
    		conditionStr  +=  "  where  program_id = " +  ((Long)bedProgramIds.get(i)).longValue();
    	}
    	else if(bedProgramIds.get(i) != null  &&  i > 0)
    	{
    		conditionStr  +=  "  or  program_id = " +  ((Long)bedProgramIds.get(i)).longValue();
    	}
    }

   	queryStr = " FROM " + dbTable + conditionStr +
   	           " ORDER BY  p.Name ";


   	Query q = getSession().createQuery(queryStr);
   	List rs = q.list();
    	
    if(rs == null  ||  rs.size() <= 0)
    {
    	return null;
    }
	
	return rs;

}

//###############################################################################
public List getServicePrograms()
{
	String queryStr = " FROM  " + dbTable + 
                      " WHERE  p.Type='Service' ORDER BY  p.Name ";

	List rs = getHibernateTemplate().find(queryStr);
	return rs;

}

//###############################################################################

public List getServiceProgramsWithinProgramDomain(List serviceProgramIds)
{
    if(serviceProgramIds == null  ||  serviceProgramIds.size() <= 0 )
    {
       	return null;
    }
    
    String queryStr = "";
    String conditionStr = "";
    
    for(int i=0;  i < serviceProgramIds.size();  i++)
    {
    	if(serviceProgramIds.get(i) != null  &&  i == 0)
    	{
    		conditionStr  +=  "  where  program_id = " +  ((Long)serviceProgramIds.get(i)).longValue();
    	}
    	else if(serviceProgramIds.get(i) != null  &&  i > 0)
    	{
    		conditionStr  +=  "  or  program_id = " +  ((Long)serviceProgramIds.get(i)).longValue();
    	}
    }

   	queryStr = " FROM " + dbTable + conditionStr +
   	           " ORDER BY  p.Name ";


   	Query q = getSession().createQuery(queryStr);
   	List rs = q.list();
    	
    if(rs == null  ||  rs.size() <= 0)
    {
    	return null;
    }
	
	return rs;

}

//###############################################################################
public boolean isBedProgram(Integer programId)
{
	if(programId == null  ||  programId.intValue() <= 0)
	{
		return false;
	}
    String queryStr =  "FROM " + dbTable +
    				  " WHERE  p.Id=?" + 
                      " AND    p.Type='Bed' ";

	List rs = getHibernateTemplate().find(queryStr, programId);
	
	if(rs != null  &&  rs.size() > 0)
	{
		return true;
	}
	return  false;

}
//###############################################################################
public boolean isServiceProgram(Integer programId)
{
	if(programId == null  ||  programId.intValue() <= 0)
	{
		return false;
	}
    String queryStr = " FROM " + dbTable +
    				  " WHERE  p.Id=?" + 
                      " AND    p.Type='Service' ";  

	List rs = getHibernateTemplate().find(queryStr, programId);
	
	if(rs != null  &&  rs.size() > 0)
	{
		return true;
	}
	return  false;

}

//###############################################################################
public int getMaxAllowedNum(Integer programId)
{
	if(programId == null  ||  programId.intValue() <= 0)
	{
		return -1;
	}
    String queryStr = " FROM " + dbTable +
    				  " WHERE  p.Id=?";

	List rs = getHibernateTemplate().find(queryStr, programId);
	
	if(rs != null  &&  rs.size() > 0)
	{
		return  ((Program)rs.get(0)).getMaxAllowed().intValue();
	}
	return  -1;
}

//###############################################################################
public int getNumOfMembers(Integer programId)
{
	if(programId == null  ||  programId.intValue() <= 0)
	{
		return -1;
	}
    String queryStr = " FROM " + dbTable +
    				  " WHERE  p.Id=?";

	List rs = getHibernateTemplate().find(queryStr, programId);
	
	if(rs != null  &&  rs.size() > 0)
	{
		return  ((Program)rs.get(0)).getNumOfMembers().intValue();
	}
	return  -1;
}

//###############################################################################

public int getNumOfMembersFromAdmission(String programId)
{
	if( programId == null  ||  programId.length() <= 0 )
	{
		return 0;
	}
	
    String queryStr = " FROM  Admission  a " + 
	  				  " WHERE  a.ProgramId=? " +
	  				  " AND    a.AdmissionStatus='current' ";

   	List rs = getHibernateTemplate().find(queryStr, Long.valueOf(programId));
   	if(rs != null  &&  rs.size() > 0)
   	{
   		return  rs.size();
   	}
		
	return  0;
}


//###############################################################################

public boolean ensureNumOfMembersCorrect(String programId)
{
	if( programId == null  ||  programId.length() <= 0 )
	{
		return false;
	}
	int numAdmitted = getNumOfMembersFromAdmission(programId); 
	
  	
	Program program = getProgram(Integer.valueOf(programId));
		
    program.setNumOfMembers(new Integer(numAdmitted));
    	
   	updateProgram(program);
    	
	return  true;
}

//###############################################################################
public boolean isMaxAllowedMet(Integer programId)
{
	if(programId == null  ||  programId.intValue() <= 0)
	{
		return false;
	}
	int maxAllowable = 1;
	int numOfMembers = 0;

    String queryStr = " FROM " + dbTable +
    				  " WHERE  p.Id=?";

	List rs = getHibernateTemplate().find(queryStr, programId);
	
	if(rs != null  &&  rs.size() > 0)
	{
		Program program = (Program)rs.get(0);
		
		maxAllowable = program.getMaxAllowed().intValue();
		numOfMembers = program.getNumOfMembers().intValue();
		
        if( maxAllowable <= numOfMembers )
        {
        	return true;
        }
	}
    return false;

}

//###############################################################################

public boolean incrementNumOfMembers(Program program)
{
    int maxNum = program.getMaxAllowed().intValue();
    int numOfMembers = program.getNumOfMembers().intValue();
    if(maxNum < numOfMembers + 1)
    {
        return false;
    }
    
    numOfMembers++;
    
    program.setNumOfMembers( new Integer(numOfMembers) );  
    
    try
    {
    	getHibernateTemplate().update(program);
    }
    catch(Exception ex)
    {
    	return false;
    }

    return true;
}

//###############################################################################

public boolean decrementNumOfMembers(Program program)
{
    int numOfMembers = program.getNumOfMembers().intValue();

    if(numOfMembers - 1 < 0)
    {
        return false;
    }
    numOfMembers--;
    
    program.setNumOfMembers( new Integer(numOfMembers) );  

    try
    {
    	getHibernateTemplate().update(program);
    }
    catch(Exception ex)
    {
    	return false;
    }
    return true;
}

//###############################################################################

public boolean decrementNumOfMembers(Integer programId)
{
	Program program = getProgram(programId);
	
    int numOfMembers = program.getNumOfMembers().intValue();

    if(numOfMembers - 1 < 0)
    {
        return false;
    }
    numOfMembers--;
    
    program.setNumOfMembers( new Integer(numOfMembers) );  

    try
    {
    	getHibernateTemplate().update(program);
    }
    catch(Exception ex)
    {
    	return false;
    }

    return true;

}

//#################################################################################	

public void saveProgram(Program program)
{
	  
    if(program == null)
    {
  	  return;
    }
      	
    getHibernateTemplate().saveOrUpdate(program);

    if(log.isDebugEnabled())
    {
  	  log.debug("programId set to: " + program.getId());
    }
}

//#################################################################################	

public void updateProgram(Program program)
{
	  
    if(program == null)
    {
  	  return;
    }
      	
    getHibernateTemplate().update(program);

    if(log.isDebugEnabled())
    {
  	  log.debug("updateProgram programId: " + program.getId());
    }
	        	
    
}


//################################################################################

public void removeProgram(Integer programId)
{
   
    Object program = getHibernateTemplate().load(Program.class, programId);
    
    getHibernateTemplate().delete(program);
    
}

//###############################################################################

	public List search(Program program) {
		Criteria criteria = getSession().createCriteria(Program.class);
		if(program.getName() != null && program.getName().length() > 0) {
			criteria.add(Expression.like("Name",program.getName() +"%"));
		}
		if(program.getType() != null && program.getType().length() > 0) {
			criteria.add(Expression.eq("Type",program.getType()));
		}
		
		return criteria.list();
	}
	
	public void resetHoldingTank() {
		Query q = getSession().createQuery("update Program set HoldingTank=false");
		q.executeUpdate();
	}
	
	public Program getHoldingTankProgram() {
		List results = this.getHibernateTemplate().find("from Program p where p.HoldingTank = true");
		if(results.size()>0) {
			return (Program)results.get(0);
		}
		return null;
	}
}
