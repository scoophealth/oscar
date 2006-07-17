package org.caisi.PMmodule.dao.hibernate;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.PMmodule.dao.Database_Service;
import org.caisi.PMmodule.dao.IntakeBDao;
import org.caisi.PMmodule.model.Formintakeb;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class IntakeBDaoHibernate  extends HibernateDaoSupport 
implements IntakeBDao  
{
	 private String dbTable1 = " Formintakeb  fb ";
	 private String dbTable2 = " Demographic  d ";

	 private Log log = LogFactory.getLog(IntakeBDaoHibernate.class);

//###############################################################################
	 
public Formintakeb getCurrIntakeB(String firstName, String lastName)
{
	if( firstName == null  ||  firstName.length() <= 0  ||
		lastName == null   ||  lastName.length() <= 0	)
	{
		return null;
	}

    String queryStr = " FROM " + dbTable1 +
    " WHERE  fb.ClientFirstName=? " + 
    " AND    fb.ClientSurname=? " + 
    " ORDER  BY  fb.FormEdited  DESC  LIMIT 0,1 ";

	List rs = getHibernateTemplate().find(queryStr, new Object[]{firstName,lastName});
	
	if(rs != null  &&  rs.size() > 0)
	{
		return  (Formintakeb )rs.get(0);
	}
	return null;
}

//###############################################################################
//used in ClientSearchAction.java, IntakeBAction.java
public Formintakeb getCurrIntakeBByDemographicNo(String demographicNo)
{
	if(demographicNo == null  ||  demographicNo.length() <= 0)
	{
		return null;
	}

    String queryStr = " FROM " + dbTable1 +
    " WHERE  fb.DemographicNo=? " + 
    " ORDER  BY  fb.FormEdited  DESC  LIMIT 0,1 ";

	List rs = getHibernateTemplate().find(queryStr, new Object[]{demographicNo});
	
	if(rs != null  &&  rs.size() > 0)
	{
		return  (Formintakeb )rs.get(0);
	}
	return null;
}
//###############################################################################
public Formintakeb getIntakeB(String queryStr)
{
	if(queryStr == null  ||  queryStr.length() <= 0)
	{
		return null;
	}

	List rs = getHibernateTemplate().find(queryStr);
	
	if(rs != null  &&  rs.size() > 0)
	{
		return  (Formintakeb )rs.get(0);
	}
	return null;

}

//###############################################################################
public List getIntakeBs()
{
	List rs = getHibernateTemplate().find(" FROM " + dbTable1 + " ORDER BY  fb.DemographicNo ");
	return rs;
}

//###############################################################################
public List getIntakeBs(String queryStr)  
{
	if(queryStr == null  ||  queryStr.length() <= 0)
	{
		return null;
	}
	List rs = getHibernateTemplate().find(queryStr);
	return rs;
}
//#################################################################################
//used in IntakeBCommitAction.java
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
//used in IntakeBCommitAction.java
public boolean insertNewClientInfoIntoDemographicTable(
		Database_Service databaseService, DataSource dataSource, 
		Formintakeb fb, String newDemographicNo) 
{
	String patientStatus = "AC";
	
	if(fb.getYear().equals(""))
	{
		fb.setYear("0001");
	}
	if(fb.getMonth().equals(""))
	{
		fb.setMonth("01");
	}
	if(fb.getDay().equals(""))
	{
		fb.setDay("01");
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
	" '" + fb.getClientSurname() + "'," +
	" '" + fb.getClientFirstName() + "'," +
	" '','','','','','','','', " + 
	" '" + fb.getYear() + "'," + 
	" '" + fb.getMonth() + "'," + 
	" '" + fb.getDay() + "'," + 
	" '" + "" + "'," + 
	" '" + "" + "'," + 
	" '', " + 
	" '" + patientStatus + "'," + 
	" '0001-01-01','', " + 
	" '" + fb.getProviderNo() + "'," + 
	" '','0001-01-01','0001-01-01','','','0001-01-01'," + 
	" '" + "" + "' " + 

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

public Formintakeb setNewIntakeBObj(Formintakeb fb) 
{
//    fb.setId(new Integer(rs.getInt("id")));
//    fb.setDemographicNo(new Integer(rs.getInt("demographic_no")));
//    fb.setProviderNo(new Integer(rs.getInt("provider_no")));

//    fb.setFormCreated(rs.getDate("formCreated"));
//    fb.setFormEdited(rs.getDate("formEdited"));

	fb.setCboxAssistwithhealthcard("");
	fb.setCommentsOnEducation("");
	fb.setTypeOfIncome("");
	fb.setDateEnteredSeaton("");
	fb.setCommentsOnStreetDrugs("");
	fb.setWhenMadeAppforOtherIncome("");
	fb.setCboxOdsp("");
	fb.setCboxFortyork("");
	fb.setCboxOneillhouse("");
	fb.setCboxOther("");
	fb.setCboxHaveother1("");
	fb.setRadioDrinkthese("");
	fb.setCboxOas("");
	fb.setWhySponsorshipBreakdown("");
	fb.setRadioEverbeenjailed("");
	fb.setDoctor2Phone("");
	fb.setRadioCaredfordepression("");
	fb.setRadioCaredforother("");
	fb.setCboxGetmoremedication("");
	fb.setRadioDruguse("");
	fb.setRadioHavementalproblem("");
	fb.setRadioRequirereferraltoesl("");
	fb.setRadioHavehealthcoverage("");
	fb.setDay("");
	fb.setCboxSpeakfrench("");
	fb.setCommentsOnLegalIssues("");
	fb.setRadioHowmuchdrink("");
	fb.setRadioNeedassistinlegal("");
	fb.setCommentsOnHousing("");
	fb.setCboxDownsviewdells("");
	fb.setCboxHaveuniversity("");
	fb.setPublicTrusteeInfo("");
	fb.setCboxLongtermprogram("");
	fb.setDateAssessment("");
	fb.setCboxAssistwithsincard("");
	fb.setDrinksPerDay("");
	fb.setSpeakOther("");
	fb.setCboxRemembertotakemedication("");
	fb.setDateLastContact3("");
	fb.setUsualOccupation("");
	fb.setRadioWantappmt("");
	fb.setRadioHasidinfile("");
	fb.setDrinksPerMonth("");
	fb.setSponsorName("");
	fb.setCboxPamphletissued("");
	fb.setDateLastContact1("");
	fb.setCboxOw("");
	fb.setAssessCompleteTime("");
	fb.setCboxBirchmountresidence("");
	fb.setContact4Name("");
	fb.setClientLastAddressPayRent("");
	fb.setDateLastContact2("");
	fb.setContact2Name("");
	fb.setRadioCurrentlyemployed("");
	fb.setCommentsOnFinance("");
	fb.setCboxHaveodsp("");
	fb.setAgency3Name("");
	fb.setAmtOwing("");
	fb.setCboxRotaryclub("");
	fb.setClientSurname("");
	fb.setCompletedBy1("");
	fb.setCommentsOnEmployment("");
	fb.setRadioEntitledtootherincome("");
	fb.setDateLastContact4("");
	fb.setWhereBeforeSeaton("");
	fb.setDoctor2NameAddr("");
	fb.setRadioSponsorshipbreakdown("");
	fb.setCboxAssistwithbirthcert("");
	fb.setHowHearAboutSeaton("");
	fb.setHowMuchYouReceive("");
	fb.setEverMadeAppforOtherIncome("");
	fb.setContact4Phone("");
	fb.setAssistProvided1("");
	fb.setCboxEmployment("");
	fb.setRadioEvermadeappforotherincome("");
	fb.setCboxHaveschizophrenia("");
	fb.setCommentsOnID("");
	fb.setCboxHaveother2("");
	fb.setRadioNeedassistwithmedication("");
	fb.setAssessStartTime("");
	fb.setCboxHavedepression("");
	fb.setCboxAssistwithimmigrant("");
	fb.setClientLastAddress("");
	fb.setMainSourceOfIncome("");
	fb.setCboxAssistwithnone("");
	fb.setDateExitedSeaton("");
	fb.setCboxHaveohip("");
	fb.setHousingInterested("");
	fb.setNeedHelpWithImmigration("");
	fb.setCboxSpeakenglish("");
	fb.setCboxAssistwithother("");
	fb.setContact2Phone("");
	fb.setCboxHavecollege("");
	fb.setDateLivedThere("");
	fb.setRadioInvolvedotheragencies("");
	fb.setAssistProvided2("");
	fb.setRadioWanthelpquitdrug("");
	fb.setContact1Phone("");
	fb.setRadioNeed60daysseatonservices("");
	fb.setCompletedBy2("");
	fb.setRadioInterestedintraining("");
	fb.setContact3Phone("");
	fb.setFollowupAppmts("");
	fb.setCboxHaveanxiety("");
	fb.setRadioInterestbacktoschool("");
	fb.setCboxAssistwithcitizencard("");
	fb.setCboxHavehighschool("");
	fb.setCboxHaveother3("");
	fb.setRadioCitizen("");
	fb.setClientFirstName("");
	fb.setMonth("");
	fb.setCboxCpp("");
	fb.setRadioSeendoctorregalcohol("");
	fb.setRadioWanthelpquit("");
	fb.setRadioOwedrent("");
	fb.setWhereOweRent("");
	fb.setHowLongUnemployed("");
	fb.setCboxHaveodb("");
	fb.setHowLongEmployed("");
	fb.setCboxAnnexharm("");
	fb.setDateLastDoctor2Contact("");
	fb.setYearsOfEducation("");
	fb.setRadioYourcanadianstatus("");
	fb.setRadioDrugusefrequency("");
	fb.setDoctor1Phone("");
	fb.setAgency1Name("");
	fb.setCboxHostel("");
	fb.setCommentsOnAlcohol("");
	fb.setRadioMentalillness("");
	fb.setCboxSpeakspanish("");
	fb.setDrinksPerWeek("");
	fb.setCommentsOnImmigration("");
	fb.setRadioBehaviorproblem("");
	fb.setAgency2Name("");
	fb.setCommentsOnNeedHelp("");
	fb.setRadioHavepublictrustee("");
	fb.setCboxSpeakother("");
	fb.setCboxTakeprescribedmedication("");
	fb.setNeedAssistInLegal("");
	fb.setRadioCaredforschizophrenia("");
	fb.setRadioDoyoudrink("");
	fb.setRadioDrinking("");
	fb.setRadioHealthproblem("");
	fb.setCboxUi("");
	fb.setLivedWithWhom("");
	fb.setAssistProvided3("");
	fb.setCboxHavemanic("");
	fb.setContact1Name("");
	fb.setYear("");
	fb.setRadioCaredformanic("");
	fb.setCboxAssistwithrefugee("");
	fb.setRadioLivedinsubsidized("");
	fb.setRadioUsedrugs("");
	fb.setCboxNeedhelpinother("");
	fb.setAssistProvided4("");
	fb.setDoctor1NameAddr("");
	fb.setContact3Name("");
	fb.setHaveOther("");
	fb.setCboxStoremedication("");
	fb.setAgency4Name("");
	fb.setRadioCaredforanxiety("");
	fb.setDateLastDoctor1Contact("");

	fb.setHistoryOfJail("");
	fb.setYourCanadianStatus("");
	fb.setAssistWithOther("");

	return fb;
}

//#################################################################################
public boolean isClientExistAlready(String firstName, String lastName)
{
	Formintakeb client = getCurrIntakeB(firstName, lastName);
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

public boolean addIntakeB(Formintakeb intakeB)
{
    if(intakeB == null)
    {
    	return false;
    }
    try
    {
    	getHibernateTemplate().save(intakeB);
    }
    catch(Exception ex)
    {
    	return false;
    }
    return true;
}

//#################################################################################
//used in IntakeBAction.java, IntakeBCommitAction.java
public boolean addNewClientToIntakeB(Formintakeb intakeB, String newDemographicNo, String actionType)
{
    if(intakeB == null  ||  newDemographicNo == null  ||  newDemographicNo.length() <= 0)
    {
    	return false;
    }
    
    intakeB.setDemographicNo(Long.valueOf(newDemographicNo));
    try
    {
    	getHibernateTemplate().save(intakeB);
    }
    catch(Exception ex)
    {
    	return false;
    }
    return true;
}

//#################################################################################	

public boolean updateIntakeB(Formintakeb intakeB)
{
    if(intakeB == null)
    {
    	return false;
    }
    try
    {
    	getHibernateTemplate().update(intakeB);
    }
    catch(Exception ex)
    {
    	return false;
    }
    return true;
}

//################################################################################

public boolean removeIntakeB(String intakeBId)
{
    Object intakeB = getHibernateTemplate().load(Formintakeb.class, Long.valueOf(intakeBId));
    
    try
    {
    	getHibernateTemplate().delete(intakeB);
    }
    catch(Exception ex)
    {
    	return false;
    }
    return true;

}

//###############################################################################

}
