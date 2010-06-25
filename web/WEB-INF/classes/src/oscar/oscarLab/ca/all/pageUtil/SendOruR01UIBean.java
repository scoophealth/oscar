package oscar.oscarLab.ca.all.pageUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.Hl7TextMessageDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.dao.PublicKeyDao;
import org.oscarehr.common.model.Hl7TextMessage;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.PublicKey;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public final class SendOruR01UIBean {
	
	private static Logger logger=MiscUtils.getLogger();
	private static ProfessionalSpecialistDao professionalSpecialistDao = (ProfessionalSpecialistDao) SpringUtils.getBean("professionalSpecialistDao");
	private static Hl7TextMessageDao hl7TextMessageDao = (Hl7TextMessageDao) SpringUtils.getBean("hl7TextMessageDao");
	private static PublicKeyDao publicKeyDao = (PublicKeyDao) SpringUtils.getBean("publicKeyDao");

	public static String getLoggedInProviderDisplayLine() {
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		Provider provider = loggedInInfo.loggedInProvider;

		StringBuilder sb = new StringBuilder();

		sb.append(provider.getLastName());
		sb.append(", ");
		sb.append(provider.getFirstName());

		if (provider.getPhone() != null) {
			sb.append(", ");
			sb.append(provider.getPhone());
		}

		if (provider.getEmail() != null) {
			sb.append(", ");
			sb.append(provider.getEmail());
		}

		if (provider.getAddress() != null) {
			sb.append(", ");
			sb.append(provider.getAddress());
		}

		return (StringEscapeUtils.escapeHtml(sb.toString()));
	}

	public static List<ProfessionalSpecialist> getRemoteCapableProfessionalSpecialists() {
		ArrayList<ProfessionalSpecialist> results=new ArrayList<ProfessionalSpecialist>();
		
		for (ProfessionalSpecialist professionalSpecialist : professionalSpecialistDao.findAll())
		{
			if (professionalSpecialist.geteDataUrl()!=null) results.add(professionalSpecialist);
		}
		
		return(results);
	}

	public static String getProfessionalSpecialistDisplayString(ProfessionalSpecialist professionalSpecialist) {
		StringBuilder sb = new StringBuilder();

		sb.append(professionalSpecialist.getLastName());
		sb.append(", ");
		sb.append(professionalSpecialist.getFirstName());

		if (professionalSpecialist.getPhoneNumber() != null) {
			sb.append(", ");
			sb.append(professionalSpecialist.getPhoneNumber());
		}

		if (professionalSpecialist.getEmailAddress() != null) {
			sb.append(", ");
			sb.append(professionalSpecialist.getEmailAddress());
		}

		if (professionalSpecialist.getStreetAddress() != null) {
			sb.append(", ");
			sb.append(professionalSpecialist.getStreetAddress());
		}

		return (StringEscapeUtils.escapeHtml(sb.toString()));
	}

	public static Integer getSelectedProfessionalSpecialistId(String hl7TextMessageId)
	{
		if (hl7TextMessageId==null) return(null);

		try
		{
			Hl7TextMessage hl7TextMessage=hl7TextMessageDao.find(Integer.parseInt(hl7TextMessageId));
			PublicKey publicKey=publicKeyDao.find(hl7TextMessage.getServiceName());
			return(publicKey.getMatchingProfessionalSpecialistId());
		}
		catch (Exception e)
		{
			logger.error("Unexpected error.", e);
		}
		
		return(null);
	}
}
