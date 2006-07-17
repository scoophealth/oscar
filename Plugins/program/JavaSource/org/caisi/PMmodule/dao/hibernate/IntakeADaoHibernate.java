package org.caisi.PMmodule.dao.hibernate;

import java.util.List;
import java.util.ListIterator;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.PMmodule.dao.Database_Service;
import org.caisi.PMmodule.dao.IntakeADao;
import org.caisi.PMmodule.model.Formintakea;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class IntakeADaoHibernate  extends HibernateDaoSupport 
implements IntakeADao  
{
	 private String dbTable1 = " Formintakea  fa ";
	 private String dbTable2 = " Demographic  d ";

	 private Log log = LogFactory.getLog(IntakeADaoHibernate.class);

//###############################################################################
	 
public Formintakea getCurrIntakeA(String firstName, String lastName)
{
	if( firstName == null  ||  firstName.length() <= 0  ||
		lastName == null   ||  lastName.length() <= 0	)
	{
		return null;
	}

    String queryStr = " FROM " + dbTable1 +
    " WHERE  fa.ClientFirstName=? " + 
    " AND    fa.ClientSurname=? " + 
    " ORDER  BY  fa.FormEdited  DESC  LIMIT 0,1 ";

	List rs = getHibernateTemplate().find(queryStr, new Object[]{firstName,lastName});
	
	if(rs != null  &&  rs.size() > 0)
	{
		return  (Formintakea )rs.get(0);
	}
	return null;
}

//###############################################################################
//used in ClientSearchAction.java, IntakeAAction.java
public Formintakea getCurrIntakeAByDemographicNo(String demographicNo)
{
	if(demographicNo == null  ||  demographicNo.length() <= 0)
	{
		return null;
	}

    String queryStr = " FROM " + dbTable1 +
    " WHERE  fa.DemographicNo=? " + 
    " ORDER  BY  fa.FormEdited  DESC  LIMIT 0,1 ";

	List rs = getHibernateTemplate().find(queryStr, new Object[]{demographicNo});
	
	if(rs != null  &&  rs.size() > 0)
	{
		return  (Formintakea )rs.get(0);
	}
	return null;
}

//###############################################################################
//used in  IntakeAReport1.jsp
public List getIntakeAByTimePeriod(String startDate, String endDate)
{
	if( startDate == null  ||  startDate.length() <= 0  ||
		endDate == null  ||  endDate.length() <= 0	)
	{
		return null;
	}

	String queryStr = " FROM " + dbTable1 +
	" WHERE  fa.FormEdited  BETWEEN ? " + 
	" AND ? " +
	" ORDER  BY  fa.DemographicNo  ASC, " + 
	" fa.FormEdited  DESC "; 
	

	List rs = getHibernateTemplate().find(queryStr, new Object[]{startDate, endDate});
	
	if(rs != null  &&  rs.size() > 0)
	{
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	
		int num =0;
		Formintakea intakeA = new Formintakea();
		ListIterator  listIterator = rs.listIterator();

		while(listIterator.hasNext())
		{
			intakeA = (Formintakea)listIterator.next();
			num++;
		}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%		
		
		return  rs;
	}
	return null;
}

//###############################################################################
public Formintakea getIntakeA(String queryStr)
{
	if(queryStr == null  ||  queryStr.length() <= 0)
	{
		return null;
	}

	List rs = getHibernateTemplate().find(queryStr);
	
	if(rs != null  &&  rs.size() > 0)
	{
		return  (Formintakea )rs.get(0);
	}
	return null;

}

//###############################################################################
public List getIntakeAs()
{
	List rs = getHibernateTemplate().find(" FROM " + dbTable1 + 
			                              " ORDER BY  fa.DemographicNo ");
	return rs;
}

//###############################################################################
public List getIntakeAs(String queryStr)  
{
	if(queryStr == null  ||  queryStr.length() <= 0)
	{
		return null;
	}
	List rs = getHibernateTemplate().find(queryStr);
	return rs;
}
//#################################################################################
//used in IntakeACommitAction.java
public int getNewDemographicNo(Database_Service databaseService, DataSource dataSource)
{
    String queryStr = " select max(demographic_no)  from demographic ";
    int demographicNo = -1;
 
    try
    {
        String[][] demographicNos = databaseService.getTableQueryFromArrList(dataSource,queryStr);

        if(demographicNos != null  &&  demographicNos.length > 0)
        {
            demographicNo = Integer.parseInt(demographicNos[0][0]) + 1;
        }
        else
        {
            return -1;
        }
    }
    catch(Exception ex)
    {
        return -1;
    }
    finally
    {
		//databaseService.closeConnection();
	    //databaseService.closeStatement();
    }
    return demographicNo;

}
//#################################################################################
//used in IntakeACommitAction.java
public boolean insertNewClientInfoIntoDemographicTable(
		Database_Service databaseService, DataSource dataSource, 
		Formintakea fa, String newDemographicNo) 
{
	String patientStatus = "AC";
	String hin = "";
	String ver = "";
	
	if(fa.getHealthCardNum() != null  &&  fa.getHealthCardNum().length() > 5)
	{
		hin = fa.getHealthCardNum();
		ver = fa.getHealthCardVer();
	}
	
	
	if(fa.getYear().equals(""))
	{
		fa.setYear("0001");
	}
	if(fa.getMonth().equals(""))
	{
		fa.setMonth("01");
	}
	if(fa.getDay().equals(""))
	{
		fa.setDay("01");
	}

	
	String addStr = 
	" INSERT INTO demographic ( demographic_no,  last_name,  first_name, " + 
	" address,  city,  province,  postal,  phone,  phone2,  email,  pin, " + 
	" year_of_birth,  month_of_birth,  date_of_birth,  hin,  ver, " + 
	" roster_status,  patient_status,  date_joined,  chart_no,  provider_no, " + 
	"  sex,  end_date,  eff_date,  pcn_indicator,  hc_type,  hc_renew_date, " + 
	" family_doctor ) " +

	" VALUES ( " +

	"  " + newDemographicNo + "," + 
	" '" + fa.getClientSurname() + "'," +
	" '" + fa.getClientFirstName() + "'," +
	" '','','','','','','','', " + 
	" '" + fa.getYear() + "'," + 
	" '" + fa.getMonth() + "'," + 
	" '" + fa.getDay() + "'," + 
	" '" + hin + "'," + 
	" '" + ver + "'," + 
	" '', " + 
	" '" + patientStatus + "'," + 
	" '0001-01-01','', " + 
	" '" + fa.getProviderNo() + "'," + 
	" '','0001-01-01','0001-01-01','','','0001-01-01'," + 
	" '" + fa.getDoctorName() + "' " + 

	" ); ";
	
	try
	{
        if(databaseService.executeUpdate(dataSource, addStr, true))
        {
        	return true;
        }
	}
	catch(Exception ex)
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
//used in IntakeACommitAction.java
public boolean insertNewClientInfoIntoDemographicPmmTable(
		Database_Service databaseService, DataSource dataSource, long agencyId, 
		Formintakea fa, String newDemographicNo) 
{
	if( fa == null  ||  newDemographicNo == null  ||  newDemographicNo.length() <= 0  ||
		agencyId < 0 )
	{
		return false;
	}
	
	if(!fa.getCboxSharing().equalsIgnoreCase("Y"))
	{
		fa.setCboxSharing("N");
	}
	
	String addStr = 
	" INSERT INTO demographic_pmm ( agency_id, demographic_no, sharing ) " +

	" VALUES ( " +

	"  " + agencyId + " ," +
	"  " + newDemographicNo + "," + 
	" '" + fa.getCboxSharing() + "' " + 
	" ); ";
	
	try
	{
      if(databaseService.executeUpdate(dataSource, addStr, true))
      {
      	return true;
      }
	}
	catch(Exception ex)
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

//################################################################################
//used in IntakeAAction.java
public Formintakea setNewIntakeAObj(Formintakea fa) 
{
//    fa.setId(new Integer(rs.getInt("id")));
//    fa.setDemographicNo(new Integer(rs.getInt("demographic_no")));
//    fa.setProviderNo(new Integer(rs.getInt("provider_no")));

//    fa.setFormCreated(rs.getDate("formCreated"));
//    fa.setFormEdited(rs.getDate("formEdited"));
    fa.setClientFirstName("");
    fa.setClientSurname("");
    fa.setNamesOfMedication("");
    fa.setRadioHasdoctor("");
    fa.setHealthIssueDetails("");
    fa.setCboxOdsp("");
    fa.setCboxFortyork("");
    fa.setCboxIdnone("");
    fa.setReactionToMedication("");
    fa.setSeatonNotToured("");
    fa.setRadioOtherhealthconcern("");
    fa.setCboxOneillhouse("");
    fa.setContactName("");
    fa.setRadioDidnotreceivehealthcare("");
    fa.setCboxTreatphysicalhealth("");
    fa.setCboxIdfiled("");
    fa.setCboxTreatinjury("");
    fa.setCboxOas("");
    fa.setCboxOtherincome("");
    fa.setCboxNewclient("");
    fa.setRadioSeedoctor("");
    fa.setRadioMentalhealthconcerns("");
    fa.setRadioObjecttoregulardoctorin4wks("");
    fa.setHelpObtainMedication("");
    fa.setSummary("");
    fa.setDay("");
    fa.setCboxSpeakfrench("");
    fa.setDoctorPhone("");
    fa.setRadioHasdrinkingproblem("");
    fa.setCboxDownsviewdells("");
    fa.setCboxLongtermprogram("");
    fa.setCboxGotoothers("");
    fa.setSpeakOther("");
    fa.setLastIssueDate("");
    fa.setRadioHasbehaviorproblem("");
    fa.setRadioNeedregulardoctor("");
    fa.setGoToOthers("");
    fa.setRadioHelpobtainmedication("");
    fa.setCboxAssistinhealth("");
    fa.setCboxGotowalkinclinic("");
    fa.setCboxNoid("");
    fa.setCboxPamphletissued("");
    fa.setCboxNorecord("");
    fa.setCboxOw("");
    fa.setAssessCompleteTime("");
    fa.setAssessDate("");
    fa.setCboxHearingimpair("");
    fa.setCboxVisualimpair("");
    fa.setCboxBirchmountresidence("");
    fa.setCboxEi("");
    fa.setRadioHasdrugproblem("");
    fa.setCboxVisithealthcenter("");
    fa.setCboxRotaryclub("");
    fa.setCboxHealthcard("");
    fa.setTreatOtherReasons("");
    fa.setMobilityImpair("");
    fa.setRadioSeatontour("");
    fa.setRadioHealthissue("");
    fa.setCboxAssistinhousing("");
    fa.setAmtReceived("");
    fa.setCboxVisithealthoffice("");
    fa.setCompletedBy("");
    fa.setContactPhone("");
    fa.setCboxImmigrant("");
    fa.setCboxDateofreadmission("");
    
    fa.setCboxIsstatementread("");

    fa.setCboxTreatotherreasons("");
    fa.setCommentsOnID("");
    fa.setCboxVisitothers("");
    fa.setAssessStartTime("");
    fa.setCboxInsulin("");
    fa.setRadioSeesamedoctor("");
    fa.setRadioSpeaktoresearcher("");
    fa.setCboxSpeakenglish("");
    fa.setCboxAssistinidentification("");
    fa.setContactRelationship("");
    fa.setCboxTreatmentalhealth("");
    fa.setDatesAtSeaton("");
    fa.setRadioRateoverallhealth("");
    fa.setHealthCardNum("");
    fa.setHealthCardVer("");

    fa.setOffice("");
    fa.setRadioNeedseatonservice("");
    fa.setCboxBleeding("");
    fa.setCboxRegularcheckup("");
    fa.setWorkerNum("");
    fa.setCboxHasdiabetes("");
    fa.setCboxVisitwalkinclinic("");
    fa.setMonth("");
    fa.setRadioAllergictomedication("");
    fa.setCboxCpp("");
    fa.setFrequencyOfSeeingDoctor("");
    fa.setCboxOtherid("");
    fa.setCboxAssistineducation("");
    fa.setCboxAnnexharm("");
    fa.setCboxVisitemergencyroom("");
    fa.setCboxMobilityimpair("");
    fa.setCboxHealthoffice("");
    fa.setCboxEpilepsy("");
    fa.setRadioPamphletissued("");
    fa.setCboxCitzenshipcard("");
    fa.setCboxAssistinaddictions("");
    fa.setCboxAssistinfinance("");
    fa.setRadioActive("");
    fa.setCboxEmployment("");
    fa.setMentalHealthConcerns("");
    fa.setRadioTakemedication("");
    fa.setCboxHostel("");
    fa.setDoctorAddress("");
    fa.setDoctorName("");
    fa.setCboxGotohealthcenter("");
    fa.setRadioAppmtseedoctorin3mths("");
    fa.setCboxAssistinlegal("");
    fa.setCboxSincard("");
    fa.setRadioHasmentalillness("");
    fa.setOtherIdentification("");
    fa.setCboxBirthcertificate("");
    fa.setCboxAssistinimmigration("");
    fa.setContactAddress("");
    fa.setCboxSpeakother("");
    fa.setPamphletNotIssued("");
    fa.setRadioHashealthproblem("");
    fa.setDateOfReadmission("");
    fa.setRadioOnlinecheck("");
    fa.setAllergicToMedicationName("");
    fa.setCboxGotoemergencyroom("");
    fa.setFrequencyOfSeeingEmergencyRoomDoctor("");
    fa.setCboxWsib("");
    fa.setCboxRefugee("");
    fa.setDoctorPhoneExt("");
    fa.setYear("");
    fa.setCboxAssistinemployment("");
    fa.setOtherHealthConerns("");
    fa.setEnterSeatonDate("");
    fa.setReasonToSeaton("");

    fa.setSinNum("");
    fa.setBirthCertificateNum("");
    fa.setCitzenshipCardNum("");
    fa.setImmigrantNum("");
    fa.setRefugeeNum("");
    fa.setCboxIsstatement6read("");
    
    fa.setCboxSharing("");
    return fa;
}

//#################################################################################
public boolean isClientExistAlready(String firstName, String lastName)
{
	Formintakea client = getCurrIntakeA(firstName, lastName);
	if(client != null)
	{
		return true;
	}
	return false;
}

//#################################################################################

//for SQL, not HQL 
public String getUpdateClientStatusSqlStr(String demographicNo)
{
	String updateStr = " update demographic " +
	                   " set patient_status='AC' " +
	                   " where demographic_no=" + demographicNo;
	return updateStr;
}

//#################################################################################	

public boolean addIntakeA(Formintakea intakeA)
{
    if(intakeA == null)
    {
    	return false;
    }
    try
    {
    	getHibernateTemplate().save(intakeA);
    }
    catch(Exception ex)
    {
    	return false;
    }
    return true;
}

//#################################################################################
//used in IntakeAAction.java, IntakeACommitAction.java
public boolean addNewClientToIntakeA(Formintakea intakeA, String newDemographicNo, String actionType)
{
    if( intakeA == null  ||  newDemographicNo == null  ||  newDemographicNo.length() <= 0  ||
       (intakeA.getClientFirstName().length() <= 0  &&  intakeA.getClientSurname().length() <= 0) )
    {
    	return false;
    }
    
    
    intakeA.setDemographicNo(Long.valueOf(newDemographicNo));
    try
    {
    	getHibernateTemplate().save(intakeA);
    }
    catch(Exception ex)
    {
    	return false;
    }
    return true;
}

//#################################################################################	

public boolean updateIntakeA(Formintakea intakeA)
{
    if(intakeA == null)
    {
    	return false;
    }
    try
    {
    	getHibernateTemplate().update(intakeA);
    }
    catch(Exception ex)
    {
    	return false;
    }
    return true;
}


//################################################################################

public boolean removeIntakeA(String intakeAId)
{
    Object intakeA = getHibernateTemplate().load(Formintakea.class, Long.valueOf(intakeAId));
    
    try
    {
    	getHibernateTemplate().delete(intakeA);
    }
    catch(Exception ex)
    {
    	return false;
    }
    return true;

}

//###############################################################################

}
