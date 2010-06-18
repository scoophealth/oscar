package oscar.oscarLab.ca.all.pageUtil;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public final class SendOruR01UIBean {
	private static ProfessionalSpecialistDao professionalSpecialistDao = (ProfessionalSpecialistDao) SpringUtils.getBean("professionalSpecialistDao");

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

	public static List<ProfessionalSpecialist> getAllProfessionalSpecialists() {
		return (professionalSpecialistDao.findAll());
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
}
