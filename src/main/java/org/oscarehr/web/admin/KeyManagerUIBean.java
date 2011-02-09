package org.oscarehr.web.admin;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.oscarehr.common.dao.OscarKeyDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.dao.PublicKeyDao;
import org.oscarehr.common.model.OscarKey;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.PublicKey;
import org.oscarehr.util.SpringUtils;

public final class KeyManagerUIBean {
	
	private static final PublicKeyDao publicKeyDao=(PublicKeyDao) SpringUtils.getBean("publicKeyDao");
	private static final OscarKeyDao oscarKeyDao=(OscarKeyDao) SpringUtils.getBean("oscarKeyDao");
	private static final ProfessionalSpecialistDao professionalSpecialistDao=(ProfessionalSpecialistDao) SpringUtils.getBean("professionalSpecialistDao");
	
	public static List<PublicKey> getPublicKeys()
	{
		return(publicKeyDao.findAll());
	}

	public static PublicKey getPublicKey(String serviceName)
	{
		return(publicKeyDao.find(serviceName));
	}
	
	public static List<ProfessionalSpecialist> getProfessionalSpecialists()
	{
		return(professionalSpecialistDao.findAll());
	}
	
	public static String getSericeNameEscaped(PublicKey publicKey)
	{
		return(StringEscapeUtils.escapeHtml(publicKey.getId()));	
	}

	public static String getSericeDisplayString(PublicKey publicKey)
	{
		return(StringEscapeUtils.escapeHtml(publicKey.getId()+" ("+publicKey.getType()+')'));	
	}
	
	public static String getProfessionalSpecialistDisplayString(ProfessionalSpecialist professionalSpecialist)
	{
		return(StringEscapeUtils.escapeHtml(professionalSpecialist.getLastName()+", "+professionalSpecialist.getFirstName()+" ("+professionalSpecialist.getId()+')'));	
	}
	
	public static void updateMatchingProfessionalSpecialist(String serviceName, Integer matchingProfessionalSpecialistId)
	{
		PublicKey publicKey=publicKeyDao.find(serviceName);
		publicKey.setMatchingProfessionalSpecialistId(matchingProfessionalSpecialistId);
		publicKeyDao.merge(publicKey);
	}
	
	public static String getPublicOscarKeyEscaped()
	{
		OscarKey oscarKey=oscarKeyDao.find("oscar");
		
		if (oscarKey==null) return("");
		
		return(StringEscapeUtils.escapeHtml(oscarKey.getPublicKey()));
	}
}
