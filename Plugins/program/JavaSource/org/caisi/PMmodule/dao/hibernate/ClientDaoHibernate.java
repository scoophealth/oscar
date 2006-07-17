package org.caisi.PMmodule.dao.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.PMmodule.dao.ClientDao;
import org.caisi.PMmodule.dao.Database_Service;
import org.caisi.PMmodule.model.Demographic;
import org.caisi.PMmodule.model.Formintakea;
import org.caisi.PMmodule.model.ProgramProvider;
import org.caisi.PMmodule.utility.Utility;
import org.caisi.PMmodule.web.formbean.ClientSearchFormBean;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ClientDaoHibernate  extends HibernateDaoSupport 
implements ClientDao  
{
    private String dbTable1 = "  Demographic  d ";
    private String dbTable2 = " Formintakea  ra  ";
    private String DemographicPmm = " DemographicPmm  dPmm ";
    private String demographic = " demographic  ";
    private String formintakea = " formintakea ";
    private String formintakeb = " formintakeb ";

	private Log log = LogFactory.getLog(ClientDaoHibernate.class);
	 
//###############################################################################
	 
public Demographic getClient(String firstName, String lastName)
{
	if( firstName == null  ||  firstName.length() <= 0  ||
		lastName == null   ||  lastName.length() <= 0	)
	{
		return null;
	}

    String queryStr = " FROM " + dbTable1 +
    				  " WHERE  d.FirstName=? " + 
    				  " AND    d.LastName=? ";

	List rs = getHibernateTemplate().find(queryStr, new Object[]{firstName,lastName});
	
	if(rs != null  &&  rs.size() > 0)
	{
		return  (Demographic )rs.get(0);
	}
	return null;
}

//###############################################################################
public Demographic getClientByDemographicNo(String demographicNo)
{
	if(demographicNo == null  ||  demographicNo.length() <= 0)
	{
		return null;
	}
    String queryStr = " FROM " + dbTable1 +
    				  " WHERE  d.DemographicNo=? ";

	List rs = getHibernateTemplate().find(queryStr, new Object[]{demographicNo});
	
	if(rs != null  &&  rs.size() > 0)
	{
		return  (Demographic )rs.get(0);
	}
	return null;
}
//###############################################################################


//###############################################################################
//haven't tested this yet ???
public int updateDemographicPmmSharingValue(
		Database_Service databaseService, DataSource dataSource, 
		String demographicNo, String sharingValue)
{
	if( demographicNo == null  || demographicNo.equals("") ||
		sharingValue == null  ||  sharingValue.equals(""))
	{
		return -1;
	}
	
	if(!sharingValue.equalsIgnoreCase("Y"))
	{
		sharingValue = "N";
	}
	
	
	String queryStr = " update  demographic_pmm " +
    					" set sharing = '" + sharingValue + "' " +
    					" where  demographic_no = '" + demographicNo + "' ";
	boolean updateOK = false;
	try
	{
		
		updateOK = databaseService.executeUpdate(dataSource, queryStr, true);
		
		if(updateOK)
		{
			return  1;
		}

	}
    catch(Exception exAdd)
    {
        return -1;
    }
    finally
    {
    	//databaseService.closeConnection();
    	//databaseService.closeStatement();
    }

    return -1;
}
//###############################################################################

public List getAllSharingClients()
{
String queryStr = " FROM " + DemographicPmm +
				    " WHERE  dPmm.Sharing='Y' ";

	List rs = getHibernateTemplate().find(queryStr);
	
	if(rs != null  &&  rs.size() > 0)
	{
		return  rs;
	}
	return null;
}

//###############################################################################

public List getAllSharingOffClients()
{
String queryStr = " FROM " + DemographicPmm +
				    " WHERE  dPmm.Sharing='N' ";

	List rs = getHibernateTemplate().find(queryStr);
	
	if(rs != null  &&  rs.size() > 0)
	{
		return  rs;
	}
	return null;
}


//###############################################################################

public List getAllSharingClientsFromInnerJoinTables(
		Database_Service databaseService, DataSource dataSource)
{
	String queryStr = " select d.demographic_no, d.last_name, d.first_name, " +
					  " d.year_of_birth, d.month_of_birth, d.date_of_birth, " +
					  " d.hin, d.ver, d.patient_status, d.provider_no, d.family_doctor  " +
					  " from demographic d  " +
					  " inner join demographic_pmm dp " +
					  " on   d.demographic_no = dp.demographic_no  " +
					  " where  dp.sharing='Y' ";

	List allSharingClients = null;
	try
	{
		
		allSharingClients = databaseService.getTableQueryArrayList(dataSource, queryStr);
		
		if(allSharingClients != null  &&  allSharingClients.size() > 0)
		{
			return  allSharingClients;
		}

	}
    catch(Exception exAdd)
    {
        return null;
    }
    finally
    {
    	//databaseService.closeConnection();
    	//databaseService.closeStatement();
    }

	
	return null;
}

//###############################################################################

public List getAllSharingOffClientsFromInnerJoinTables(
		Database_Service databaseService, DataSource dataSource)
{
	String queryStr = " select d.demographic_no, d.last_name, d.first_name, " +
					  " d.year_of_birth, d.month_of_birth, d.date_of_birth, " +
					  " d.hin, d.ver, d.patient_status, d.provider_no, d.family_doctor  " +
					  " from demographic d  " +
					  " inner join demographic_pmm dp " +
					  " on   d.demographic_no = dp.demographic_no  " +
					  " where  dp.sharing='N' ";

	List allSharingClients = null;
	try
	{
		
		allSharingClients = databaseService.getTableQueryArrayList(dataSource, queryStr);
		
		if(allSharingClients != null  &&  allSharingClients.size() > 0)
		{
			return  allSharingClients;
		}

	}
    catch(Exception exAdd)
    {
        return null;
    }
    finally
    {
    	//databaseService.closeConnection();
    	//databaseService.closeStatement();
    }

	
	return null;
}

//###############################################################################
public Demographic getClient(String queryStr)
{
	if(queryStr == null  ||  queryStr.length() <= 0)
	{
		return null;
	}

	List rs = getHibernateTemplate().find(queryStr);
	
	if(rs != null  &&  rs.size() > 0)
	{
		return  (Demographic )rs.get(0);
	}
	return null;

}

//###############################################################################
public List getClients()
{
	String queryStr = " FROM " + dbTable1;
	List rs = getHibernateTemplate().find(queryStr);
	return rs;
}

//###############################################################################

public List getClients(String queryStr)
{
	if(queryStr != null  &&  queryStr.length() <= 0)
	{
		return null;
	}
	Query q = getSession().createQuery(queryStr);
	List rs = q.list();
	if(rs != null  &&  rs.size() > 0)
	{
		return rs;
	}
	return null;
}

//###############################################################################

public List getClients(Database_Service databaseService, 
		               DataSource dataSource, String queryStr)
{

      Demographic client = null;
      List clients = null;

      ResultSet rs = null;

    try
    {
        rs = databaseService.retrieveResultSet(dataSource,queryStr);

        if(rs == null)
        {
            return null;
        }
        else
        {
        	clients = new ArrayList();
        }

        while(rs.next())
        {
            client = new Demographic();
            client = setClientObj(client, rs);
            clients.add(client);
        }
    }
    catch(Exception ex1)
    {
        return null;
    }
    finally
    {
        if(rs != null)
        {
        	try
        	{
        		rs.close();
        	}
        	catch(Exception ex2)
        	{
        	}
        }
    	//databaseService.closeConnection();
    	//databaseService.closeStatement();
    }
    return clients;
}

//#################################################################################
public String getDemographicNo(String firstName, String lastName)
{
	Demographic client = getClient(firstName, lastName);
	if(client == null)
	{
		return "";
	}
	
	return  client.getDemographicNo().toString();
}

//#################################################################################
public List getDemographicNos(String firstName, String lastName)
{
	if( firstName == null  ||  firstName.length() <= 0  ||
			lastName == null   ||  lastName.length() <= 0	)
	{
		return null;
	}

	String queryStr = " FROM " + dbTable1 +
	   				  " WHERE  d.FirstName=? " + 
	   				  " AND    d.LastName=? ";

	List rs = getHibernateTemplate().find(queryStr, new Object[]{firstName,lastName});
		
	if(rs != null  &&  rs.size() > 0)
	{
		return  rs;
	}
	return null;

}

//#################################################################################
public boolean isClientExistAlready(String firstName, String lastName)
{
	Demographic client = getClient(firstName, lastName);
	if(client != null)
	{
		return true;
	}
	return false;
}
//#################################################################################
//haven't tested this method !!!
public boolean isSharingOn(String demographicNo)
{
	return false;
}

//#################################################################################
public int getTotalRecordCount(Database_Service databaseService, DataSource dataSource)
{
    String rowCountStr = "";
    String totalRowCountStr = "";

    try
    {
        rowCountStr = " SELECT COUNT(*) FROM " + demographic + 
		  			  " WHERE    patient_status='AC' ";

        ResultSet resultSetRowCount = databaseService.executeQuery(dataSource,rowCountStr);

        while(resultSetRowCount != null  &&  resultSetRowCount.next())
        {
            totalRowCountStr = resultSetRowCount.getString(1);
        }

        return Integer.parseInt(totalRowCountStr);
    }
    catch(Exception e)
    {
        return -1;
    }
    finally
    {
    }
}
//##############################################################################
public Demographic setClientObj(Demographic clientInfo, ResultSet rs)
{
	try
	{
		clientInfo.setDemographicNo(new Integer(rs.getInt("demographic_no"))); 
		clientInfo.setLastName(rs.getString("last_name")); 
		clientInfo.setFirstName(rs.getString("first_name")); 
		clientInfo.setYearOfBirth(rs.getString("year_of_birth"));
		clientInfo.setMonthOfBirth(rs.getString("month_of_birth"));
		clientInfo.setDateOfBirth(rs.getString("date_of_birth"));
		clientInfo.setProviderNo(rs.getString("provider_no")); 
		clientInfo.setHin(rs.getString("hin")); 
		clientInfo.setVer(rs.getString("ver"));
	}
	catch(SQLException sqlex)
	{
		return null;
	}
	return clientInfo;
	
}

//##############################################################################
public String getQuerySqlStr(String clientFirstName, String clientSurname,
        String clientDateOfBirth, String healthCardNum, String healthCardVer, 
        String retrieveAll)
{
	String sqlStr = "";
	String queryStr = "";
	String andStr = " where ";
	String requiredCondition = " patient_status='AC' ";

	if(!clientFirstName.equals(""))
	{
		sqlStr += andStr + " firstOfBirthame like '%" + clientFirstName + "%' ";
		andStr = " and ";
	}

	if(!clientSurname.equals(""))
	{
		sqlStr += andStr + " last_name like '%" + clientSurname + "%' ";
		andStr = " and ";
	}

	if(!clientDateOfBirth.equals(""))
	{
		sqlStr += andStr + " date_of_birth like '%" + clientDateOfBirth + "%' ";
		andStr = " and ";
	}

	if(!healthCardNum.equals(""))
	{
		sqlStr += andStr + " health_card like '%" + healthCardNum + "%' ";
	}

	if(!sqlStr.equals(""))
	{
		andStr = " and ";
		queryStr = " select * from  " + demographic + " " + sqlStr; // + andStr + requiredCondition;
	}

	if(retrieveAll.equalsIgnoreCase("Y"))
	{
		andStr = " where ";
		queryStr = " select * from  " + demographic + 
//				   andStr + requiredCondition + 	
		           " order by last_name, first_name; ";
	}

	return queryStr;

}
//################################################################################
/*
public String getQuerySqlStr(String clientFirstName, String clientSurname,
        String clientDateOfBirth, String healthCardNum, String healthCardVer, 
        String retrieveAll, int rowCountPass, int totalRowDisplay)
{
	String sqlStr = "";
	String queryStr = "";
	String andStr = " where ";
	String requiredCondition = " d.PatientStatus='AC' ";
	
	if(!clientFirstName.equals(""))
	{
		sqlStr += andStr + " d.FirstName like '%" + clientFirstName + "%' ";
		andStr = " and ";
	}

	if(!clientSurname.equals(""))
	{
		sqlStr += andStr + " d.LastName like '%" + clientSurname + "%' ";
		andStr = " and ";
	}
/*
	if(!clientDateOfBirth.equals(""))
	{
		sqlStr += andStr + " date_of_birth like '%" + clientDateOfBirth + "%' ";
		andStr = " and ";
	}

	if(!healthCardNum.equals(""))
	{
		sqlStr += andStr + " health_card like '%" + healthCardNum + "%' ";
	}
*/
/*
	if(!sqlStr.equals(""))
	{
		andStr = " and ";
		queryStr = " from " + dbTable1 + sqlStr;// + 
//		           andStr + requiredCondition +
//		           " limit " + rowCountPass + "," + totalRowDisplay + ";" ;
	}

	if(retrieveAll.equalsIgnoreCase("Y"))
	{
		andStr = " where ";
		
		queryStr = " from " + dbTable1 + 
//        andStr + requiredCondition +
		" order by d.LastName, d.FirstName " +
		" limit " + rowCountPass + "," + totalRowDisplay + ";";
	}
	
	return queryStr;

}
*/
//################################################################################

public String getQuerySqlStr(String clientFirstName, String clientSurname,
        String clientDateOfBirth, String healthCardNum, String healthCardVer, 
        String retrieveAll, int rowCountPass, int totalRowDisplay)
{
	String sqlStr = "";
	String queryStr = "";
	String andStr = " where ";
	String requiredCondition = " patient_status='AC' ";
	
	if(!clientFirstName.equals(""))
	{
		sqlStr += andStr + " first_name like '%" + clientFirstName + "%' ";
		andStr = " and ";
	}

	if(!clientSurname.equals(""))
	{
		sqlStr += andStr + " last_name like '%" + clientSurname + "%' ";
		andStr = " and ";
	}
	/*
	if(!clientDateOfBirth.equals(""))
	{
		sqlStr += andStr + " date_of_birth like '%" + clientDateOfBirth + "%' ";
		andStr = " and ";
	}

	if(!healthCardNum.equals(""))
	{
		sqlStr += andStr + " health_card like '%" + healthCardNum + "%' ";
	}
	*/

	if(!sqlStr.equals(""))
	{
		andStr = " and ";
		queryStr = " select * from  " + demographic + sqlStr + 
//		           andStr + requiredCondition +
		           " limit " + rowCountPass + "," + totalRowDisplay + ";" ;
	}

	if(retrieveAll.equalsIgnoreCase("Y"))
	{
		andStr = " where ";
		queryStr = " select * from  " + demographic + 
//        andStr + requiredCondition +
		" order by last_name, first_name " +
		" limit " + rowCountPass + "," + totalRowDisplay + ";";
	}
	
	return queryStr;

}


//################################################################################
//for SQL, not HQL 
public String getUpdateClientStatusSqlStr(String demographicNo)
{
	String updateStr = " update " + demographic +
	                   " set patient_status='AC' " +
	                   " where demographic_no=" + demographicNo;
	return updateStr;
}

//#################################################################################	

public boolean addClient(Demographic client)
{
    if(client == null)
    {
    	return false;
    }
    try
    {
    	getHibernateTemplate().save(client);
    }
    catch(Exception ex)
    {
    	return false;
    }
    return true;
}

//#################################################################################
public boolean addClientFromFormintakea(
		Database_Service databaseService, DataSource dataSource,
		Formintakea intakeA)
{
      try
      {
      	  if(databaseService == null  ||  intakeA == null)
      	  {
      		return false;
      	  }

      	  //insert  demographic_no  along with other same field data!!!
      	  
          String addStr = "insert into " + demographic + 
          " (demographic_no, last_name, first_name, year_of_birth, month_of_birth, date_of_birth , " + 
          " hin, ver ) " + 
          " values ( " +  
          Utility.escapeNull(intakeA.getDemographicNo()) + ",'" +
          Utility.escapeNull(intakeA.getClientSurname()) + "','" + 
          Utility.escapeNull(intakeA.getClientFirstName()) + "','" +
          Utility.convertToRelacementStrIfNull(intakeA.getYear(),"0001") + "','" +
          Utility.convertToRelacementStrIfNull(intakeA.getMonth(),"01") + "','" +
          Utility.convertToRelacementStrIfNull(intakeA.getDay(),"01") + "','" +
          Utility.escapeNull(intakeA.getHealthCardNum()) + "',' " +
          Utility.escapeNull(intakeA.getHealthCardVer()) + "' " +
          ")";

          if(databaseService.executeUpdate(dataSource, addStr, true))
          {
          	return true;
          }
      }
      catch(Exception exAdd)
      {
          return false;
      }
      finally
      {
      	//databaseService.closeConnection();
      	//databaseService.closeStatement();
      }
      return false;
}


//#################################################################################
public boolean addClientIntoFormintakea(
		Database_Service databaseService, DataSource dataSource,
		Demographic client)
{
      try
      {
      	if(databaseService == null  ||  client == null)
      	{
      		return false;
      	}

          String addStr = "insert into " + formintakea + 
          " (clientFirstName, clientSurname, month, day, year, " + 
          " healthCardNum, healthCardVer ) " + 
          " values ( '" +  
          Utility.escapeNull(client.getFirstName()) + "','" + 
          Utility.escapeNull(client.getLastName()) + "','" +
          client.getMonthOfBirth() + "','" +
          client.getDateOfBirth() + "','" +
          client.getYearOfBirth() + "','" +
          Utility.escapeNull(client.getHin()) + "',' " +
          Utility.escapeNull(client.getVer()) + "' " +
          ")";

          if(databaseService.executeUpdate(dataSource, addStr, true))
          {
          	return true;
          }
      }
      catch(Exception exAdd)
      {
          return false;
      }
      finally
      {
      	//databaseService.closeConnection();
      	//databaseService.closeStatement();
      }
      return false;
}
//#################################################################################	
public boolean updateClientDateOfBirth(
		Database_Service databaseService, DataSource dataSource, Demographic client) 
{
    String sqlUpdateStr = "";
	try
  	{
        sqlUpdateStr = " update " + demographic + " set " +
                    " year_of_birth='" + client.getYearOfBirth() + "', " +
                    " month_of_birth='" + client.getMonthOfBirth() + "', " +
                    " date_of_birth='" + client.getDateOfBirth() + "' " +

                    " where " +
                    " demographic_no=" + client.getDemographicNo();

        if(databaseService.executeUpdate(dataSource, sqlUpdateStr, true))
        {
        	return true;
        }
	}
   	catch (Exception e)
   	{
   		return false;
  	}
   	finally
   	{
   	}
   	return false;
}
//#################################################################################	
public boolean updateClientHealthCardNum(
		Database_Service databaseService, DataSource dataSource, 
		Demographic client, String healthCardNum, String healthCardVer) 
{
    String sqlUpdateStr = "";
	try
  	{
		if(healthCardNum == null  ||  healthCardNum.length() <= 0)
		{
			return false;
		}
        sqlUpdateStr = " update " + demographic + " set " +
                    " hin='" + healthCardNum + "', " +
                    " ver='" + healthCardVer + "' " +

                    " where " +
                    " demographic_no=" + client.getDemographicNo();

        if(databaseService.executeUpdate(dataSource, sqlUpdateStr, true))
        {
        	return true;
        }
	}
   	catch (Exception e)
   	{
   		return false;
  	}
   	finally
   	{
   	}
   	return false;
}
//#################################################################################	
public boolean updateClientDoctor(
		Database_Service databaseService, DataSource dataSource, Demographic client) 
{
    String sqlUpdateStr = "";
	try
  	{
        sqlUpdateStr = " update " + demographic + " set " +
                    " family_doctor='" + client.getFamilyDoctor() + "' " +

                    " where " +
                    " demographic_no=" + client.getDemographicNo();

        if(databaseService.executeUpdate(dataSource, sqlUpdateStr, true))
        {
        	return true;
        }
	}
   	catch (Exception e)
   	{
   		return false;
  	}
   	finally
   	{
   	}
   	return false;
}

//#################################################################################

public boolean updateClient(Demographic client)
{
    if(client == null)
    {
    	return false;
    }
    try
    {
    	getHibernateTemplate().update(client);
    }
    catch(Exception ex)
    {
    	return false;
    }
    return true;
}

//################################################################################

public boolean removeClient(String demographicNo)
{
    Object client = getHibernateTemplate().load(Demographic.class, Integer.valueOf(demographicNo));
    
    try
    {
    	getHibernateTemplate().delete(client);
    }
    catch(Exception ex)
    {
    	return false;
    }
    return true;

}

//###############################################################################

/*
 * use program_client table to do domain based search
 * 
 */
	public List search(ClientSearchFormBean bean) {
		Criteria criteria = getSession().createCriteria(Demographic.class);
		
		if (!bean.isSearchUsingSoundex()) {
			if(bean.getFirstName() != null && bean.getFirstName().length() > 0) {
				criteria.add(Expression.like("FirstName",bean.getFirstName() +"%"));
			}
			if(bean.getLastName() != null && bean.getLastName().length() > 0) {
				criteria.add(Expression.like("LastName",bean.getLastName() + "%"));
			}
		}
		else { // soundex variation
			String sql;
			if(bean.getFirstName() != null && bean.getFirstName().length() > 0) {
				sql = "LEFT(SOUNDEX(first_name),4) = LEFT(SOUNDEX('" + bean.getFirstName() + "'),4)";
				criteria.add(Restrictions.sqlRestriction(sql));
			}
			if(bean.getLastName() != null && bean.getLastName().length() > 0) {
				sql = "LEFT(SOUNDEX(last_name),4) = LEFT(SOUNDEX('" + bean.getLastName() + "'),4)";
				criteria.add(Restrictions.sqlRestriction(sql));
			}

		}
		if(bean.getDob() != null && bean.getDob().length() > 0) {
			criteria.add(Expression.eq("YearOfBirth",bean.getYearOfBirth()));
			criteria.add(Expression.eq("MonthOfBirth",bean.getMonthOfBirth()));
			criteria.add(Expression.eq("DateOfBirth",bean.getDayOfBirth()));
		}
		if(bean.getHealthCardNumber() != null && bean.getHealthCardNumber().length() > 0) {
			criteria.add(Expression.eq("HealthCardNum",bean.getHealthCardNumber()));
		}
		
		if(bean.getHealthCardVersion() != null && bean.getHealthCardVersion().length() > 0) {
			criteria.add(Expression.eq("HealthCardVersion",bean.getHealthCardVersion()));
		}
		if(bean.getProgramDomain() != null && !bean.getProgramDomain().isEmpty() && !bean.isSearchOutsideDomain()) {
			//program domain search
			StringBuffer programIds = new StringBuffer();
			
			for(int x=0;x<bean.getProgramDomain().size();x++) {
				ProgramProvider p = (ProgramProvider)bean.getProgramDomain().get(x);
				if(x>0) { programIds.append(",");}
				programIds.append(p.getProgramId());
			}
			String sql = "{alias}.demographic_no in (select client_id from admission where program_id in (" + programIds.toString() + "))";
			criteria.add(Restrictions.sqlRestriction(sql));
		}
		if(bean.getProgramDomain() != null && bean.getProgramDomain().isEmpty() && !bean.isSearchOutsideDomain()) {
			String sql = "{alias}.demographic_no = 0";
			criteria.add(Restrictions.sqlRestriction(sql));
		}
		
		return criteria.list();
	}

	public Date getMostRecentIntakeADate(Long demographicNo) {
		Query q = getSession().createQuery("select intake.FormEdited from Formintakea intake where intake.DemographicNo = ? order by intake.FormEdited DESC");
		q.setLong(0,demographicNo.longValue());
		List result = q.list();
		if(result.size() > 0) {
			return (Date)result.get(0);
		}
		return null;
	}
	
	public Date getMostRecentIntakeBDate(Long demographicNo) {
		Query q = getSession().createQuery("select intake.FormEdited from Formintakeb intake where intake.DemographicNo = ? order by intake.FormEdited DESC");
		q.setLong(0,demographicNo.longValue());
		List result = q.list();
		if(result.size() > 0) {
			return (Date)result.get(0);
		}
		return null;
	}
	
	public Date getMostRecentIntakeCDate(Long demographicNo) {
		Query q = getSession().createQuery("select intake.FormEdited from Formintakec intake where intake.DemographicNo = ? order by intake.FormEdited DESC");
		q.setLong(0,demographicNo.longValue());
		List result = q.list();
		if(result.size() > 0) {
			return (Date)result.get(0);
		}
		return null;
	}
	

	public void saveClient(Demographic client) {
		this.getHibernateTemplate().saveOrUpdate(client);
	}
}
