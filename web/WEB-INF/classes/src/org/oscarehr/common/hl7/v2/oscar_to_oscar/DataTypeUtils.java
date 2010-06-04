package org.oscarehr.common.hl7.v2.oscar_to_oscar;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.Gender;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.datatype.CX;
import ca.uhn.hl7v2.model.v25.datatype.DTM;
import ca.uhn.hl7v2.model.v25.datatype.PLN;
import ca.uhn.hl7v2.model.v25.datatype.TS;
import ca.uhn.hl7v2.model.v25.datatype.XAD;
import ca.uhn.hl7v2.model.v25.datatype.XPN;
import ca.uhn.hl7v2.model.v25.datatype.XTN;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import ca.uhn.hl7v2.model.v25.segment.NTE;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.model.v25.segment.PRD;
import ca.uhn.hl7v2.model.v25.segment.SFT;

public final class DataTypeUtils {
	private static final Logger logger = MiscUtils.getLogger();

	/**
	 * Don't access this formatter directly, use the getAsFormattedString method, it provides synchronisation
	 */
	private static final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyyMMddHHmmss");

	private DataTypeUtils() {
		// not meant to be instantiated by anyone, it's a util class
	}

	public static String getAsHl7FormattedString(Date date) {
		synchronized (dateTimeFormatter) {
			return (dateTimeFormatter.format(date));
		}
	}
	
	public static GregorianCalendar getCalendarFromDTM(DTM dtm) throws DataTypeException
	{
		GregorianCalendar cal=new GregorianCalendar();
		// zero out fields we don't use
		cal.setTimeInMillis(0);
		cal.set(GregorianCalendar.YEAR, dtm.getYear());
		cal.set(GregorianCalendar.MONTH, dtm.getMonth()-1);
		cal.set(GregorianCalendar.DAY_OF_MONTH, dtm.getDay());
		cal.set(GregorianCalendar.HOUR_OF_DAY, dtm.getHour());
		cal.set(GregorianCalendar.MINUTE, dtm.getMinute());
		cal.set(GregorianCalendar.SECOND, dtm.getSecond());
		
		// force materialisation of values
		cal.getTimeInMillis();
		
		return(cal);
	}

	/**
	 * @param xad
	 * @param streetAddress i.e. 123 My St. unit 554
	 * @param city
	 * @param province 2 digit province / state code as defined by postal service, in canada it's in upper case, i.e. BC, ON
	 * @param country iso 3166, 3 digit version / hl70399 code, i.e. USA, CAN, AUS in upper case.
	 * @param addressType hlt0190 code, i.e. O=office, H=Home
	 * @throws DataTypeException
	 */
	public static void fillXAD(XAD xad, StreetAddressDataHolder streetAddressDataHolder, String addressType) throws DataTypeException {
		xad.getStreetAddress().getStreetOrMailingAddress().setValue(StringUtils.trimToNull(streetAddressDataHolder.streetAddress));
		xad.getCity().setValue(StringUtils.trimToNull(streetAddressDataHolder.city));
		xad.getStateOrProvince().setValue(StringUtils.trimToNull(streetAddressDataHolder.province));
		xad.getCountry().setValue(StringUtils.trimToNull(streetAddressDataHolder.country));
		xad.getZipOrPostalCode().setValue(StringUtils.trimToNull(streetAddressDataHolder.postalCode));
		xad.getAddressType().setValue(addressType);
	}

	/**
	 * @param msh
	 * @param dateOfMessage
	 * @param facilityName facility.getName();
	 * @param messageCode i.e. "REF"
	 * @param triggerEvent i.e. "I12"
	 * @param messageStructure i.e. "REF_I12"
	 * @param hl7VersionId is the version of hl7 in use, i.e. "2.5"
	 */
	public static void fillMsh(MSH msh, Date dateOfMessage, String facilityName, String messageCode, String triggerEvent, String messageStructure, String hl7VersionId) throws DataTypeException {
		msh.getFieldSeparator().setValue("|");
		msh.getEncodingCharacters().setValue("^~\\&");
		msh.getVersionID().getVersionID().setValue(hl7VersionId);

		msh.getDateTimeOfMessage().getTime().setValue(getAsHl7FormattedString(dateOfMessage));

		msh.getSendingApplication().getNamespaceID().setValue("OSCAR");

		msh.getSendingFacility().getNamespaceID().setValue(facilityName);

		// message code "REF", event "I12", structure "REF I12"
		msh.getMessageType().getMessageCode().setValue(messageCode);
		msh.getMessageType().getTriggerEvent().setValue(triggerEvent);
		msh.getMessageType().getMessageStructure().setValue(messageStructure);
	}

	/**
	 * @param sft
	 * @param version major version if available
	 * @param build build date or build number if available
	 */
	public static void fillSft(SFT sft, String version, String build) throws DataTypeException {
		sft.getSoftwareVendorOrganization().getOrganizationName().setValue("OSCARMcMaster");
		sft.getSoftwareCertifiedVersionOrReleaseNumber().setValue(version);
		sft.getSoftwareProductName().setValue("OSCAR");
		sft.getSoftwareBinaryID().setValue(build);
	}

	/**
	 * @param prd
	 * @param provider
	 * @param providerRoleId Note that this is not the oscar provider role, look in the method to see valid values
	 * @param providerRoleDescription Note that this is not the oscar provider role, look in the method to see valid values
	 */
	public static void fillPrd(PRD prd, Provider provider, String providerRoleId, String providerRoleDescription, StreetAddressDataHolder officeStreetAddressDataHolder) throws DataTypeException, HL7Exception {
		// Value Description
		// -----------------
		// RP Referring Provider
		// PP Primary Care Provider
		// CP Consulting Provider
		// RT Referred to Provider
		prd.getProviderRole(0).getIdentifier().setValue(providerRoleId);
		prd.getProviderRole(0).getText().setValue(providerRoleDescription);

		XPN xpn = prd.getProviderName(0);
		xpn.getFamilyName().getSurname().setValue(provider.getLastName());
		xpn.getGivenName().setValue(provider.getFirstName());
		xpn.getPrefixEgDR().setValue(provider.getTitle());

		XAD xad = prd.getProviderAddress(0);
		fillXAD(xad, officeStreetAddressDataHolder, "O");

		XTN xtn = prd.getProviderCommunicationInformation(0);
		xtn.getUnformattedTelephoneNumber().setValue(provider.getWorkPhone());
		xtn.getEmailAddress().setValue(provider.getEmail());

		PLN pln = prd.getProviderIdentifiers(0);
		pln.getIDNumber().setValue(provider.getProviderNo());
	}

	/**
	 * @param prd
	 * @param provider
	 * @param providerRoleId Note that this is not the oscar provider role, look in the method to see valid values
	 * @param providerRoleDescription Note that this is not the oscar provider role, look in the method to see valid values
	 */
	public static void fillPrd(PRD prd, ProfessionalSpecialist professionalSpecialist, String providerRoleId, String providerRoleDescription, StreetAddressDataHolder officeStreetAddressDataHolder) throws DataTypeException, HL7Exception {
		// Value Description
		// -----------------
		// RP Referring Provider
		// PP Primary Care Provider
		// CP Consulting Provider
		// RT Referred to Provider
		prd.getProviderRole(0).getIdentifier().setValue(providerRoleId);
		prd.getProviderRole(0).getText().setValue(providerRoleDescription);

		XPN xpn = prd.getProviderName(0);
		xpn.getFamilyName().getSurname().setValue(professionalSpecialist.getLastName());
		xpn.getGivenName().setValue(professionalSpecialist.getFirstName());
		xpn.getPrefixEgDR().setValue(professionalSpecialist.getProfessionalLetters());

		XAD xad = prd.getProviderAddress(0);
		fillXAD(xad, officeStreetAddressDataHolder, "O");

		XTN xtn = prd.getProviderCommunicationInformation(0);
		xtn.getUnformattedTelephoneNumber().setValue(professionalSpecialist.getPhoneNumber());
		xtn.getEmailAddress().setValue(professionalSpecialist.getEmailAddress());

		PLN pln = prd.getProviderIdentifiers(0);
		pln.getIDNumber().setValue(professionalSpecialist.getId().toString());
	}

	/**
	 * @param pid
	 * @param pidNumber pass in the # of this pid for the sequence, i.e. normally it's 1 if you only have 1 pid entry. if this is a list of pids, then the first one is 1, second is 2 etc..
	 * @param demographic
	 * @throws HL7Exception
	 */
	public static void fillPid(PID pid, int pidNumber, Demographic demographic) throws HL7Exception {
		// defined as first pid=1 second pid=2 etc
		pid.getSetIDPID().setValue(String.valueOf(pidNumber));

		CX cx = pid.getPatientIdentifierList(0);
		// health card string, excluding version code
		cx.getIDNumber().setValue(demographic.getHin());
		// province
		cx.getAssigningJurisdiction().getIdentifier().setValue(demographic.getHcType());

		GregorianCalendar tempCalendar = new GregorianCalendar();
		tempCalendar.setTime(demographic.getEffDate());
		cx.getEffectiveDate().setYearMonthDayPrecision(tempCalendar.get(GregorianCalendar.YEAR), tempCalendar.get(GregorianCalendar.MONTH) + 1, tempCalendar.get(GregorianCalendar.DAY_OF_MONTH));

		tempCalendar.setTime(demographic.getHcRenewDate());
		cx.getExpirationDate().setYearMonthDayPrecision(tempCalendar.get(GregorianCalendar.YEAR), tempCalendar.get(GregorianCalendar.MONTH) + 1, tempCalendar.get(GregorianCalendar.DAY_OF_MONTH));

		// blank for everyone but ontario use version code
		cx.getCheckDigit().setValue(demographic.getVer());

		XPN xpn = pid.getPatientName(0);
		xpn.getFamilyName().getSurname().setValue(demographic.getLastName());
		xpn.getGivenName().setValue(demographic.getFirstName());
		// Value Description
		// -----------------
		// A Alias Name
		// B Name at Birth
		// C Adopted Name
		// D Display Name
		// I Licensing Name
		// L Legal Name
		// M Maiden Name
		// N Nickname /”Call me” Name/Street Name
		// P Name of Partner/Spouse - obsolete (DO NOT USE)
		// R Registered Name (animals only)
		// S Coded Pseudo-Name to ensure anonymity
		// T Indigenous/Tribal/Community Name
		// U Unspecified
		xpn.getNameTypeCode().setValue("L");

		TS bday = pid.getDateTimeOfBirth();
		tempCalendar = demographic.getBirthDay();
		bday.getTime().setDatePrecision(tempCalendar.get(GregorianCalendar.YEAR), tempCalendar.get(GregorianCalendar.MONTH) + 1, tempCalendar.get(GregorianCalendar.DAY_OF_MONTH));

		// Value Description
		// -----------------
		// F Female
		// M Male
		// O Other
		// U Unknown
		// A Ambiguous
		// N Not applicable
		pid.getAdministrativeSex().setValue(getHl7GenderFromOscarGender(demographic.getSex()));

		XAD address = pid.getPatientAddress(0);
		StreetAddressDataHolder streetAddressDataHolder=new StreetAddressDataHolder();
		streetAddressDataHolder.streetAddress=demographic.getAddress();
		streetAddressDataHolder.city=demographic.getCity();
		streetAddressDataHolder.province=demographic.getProvince();
		streetAddressDataHolder.postalCode=demographic.getPostal();
		fillXAD(address, streetAddressDataHolder, "H");

		XTN phone = pid.getPhoneNumberHome(0);
		phone.getUnformattedTelephoneNumber().setValue(demographic.getPhone());

		// ISO 639, hrmmm shall we say 639-1 (2 digit)?
		pid.getPrimaryLanguage().getIdentifier().setValue(demographic.getSpokenLanguage());

		// ISO table 3166.
		pid.getCitizenship(0).getIdentifier().setValue(demographic.getCitizenship());
	}

	/**
	 * Given an oscar gender, this will return an hl7 gender.
	 */
	public static String getHl7GenderFromOscarGender(String oscarGender) {
		Gender gender = null;

		try {
			gender = Gender.valueOf(oscarGender);
		} catch (NullPointerException e) {
			// this is okay, means there's no gender
		} catch (Exception e) {
			// this is not okay...
			logger.error("Missed gender or dirty data in database. demographic.sex=" + oscarGender);
		}

		return (getHl7GenderFromOscarGender(gender));
	}

	/**
	 * Given an oscar gender, this will return an hl7 gender.
	 */
	public static String getHl7GenderFromOscarGender(Gender oscarGender) {
		if (oscarGender == null) return ("U");

		if (Gender.M == oscarGender) return ("M");
		else if (Gender.F == oscarGender) return ("F");
		else if (Gender.O == oscarGender) return ("O");
		else if (Gender.T == oscarGender) return ("A");
		else if (Gender.U == oscarGender) return ("N");
		else {
			logger.error("Missed gender or dirty data in database. demographic.sex=" + oscarGender);
			return ("U");
		}
	}

	/**
	 * Given an hl7 gender, this will return an oscarGender
	 */
	public static Gender getOscarGenderFromHl7Gender(String hl7Gender) {
		if (hl7Gender == null) return (null);

		hl7Gender = hl7Gender.toUpperCase();

		if ("M".equals(hl7Gender)) return (Gender.M);
		else if ("F".equals(hl7Gender)) return (Gender.F);
		else if ("O".equals(hl7Gender)) return (Gender.O);
		else if ("A".equals(hl7Gender)) return (Gender.T);
		else if ("N".equals(hl7Gender)) return (Gender.U);
		else if ("U".equals(hl7Gender)) return (null);
		else throw (new IllegalArgumentException("Missed gender : " + hl7Gender));
	}

	/**
	 * @param nte
	 * @param type should be a short string denoting what's in the comment data, i.e. "REASON_FOR_REFERRAL" or "ALLERGIES", max length is 250
	 * @param data should be UTF-8 String, if you have bytes, use base64 encoding first. Max Length is 65535
	 * @throws HL7Exception 
	 */
	public static void fillNte(NTE nte, String type, String data) throws HL7Exception
	{
		if (type.length()>250) throw(new IllegalArgumentException("Type too long for NTE, type max length is 250, type.length()="+type.length()));
		if (data.length()>65535) throw(new IllegalArgumentException("Data too long for NTE, data max length is 65535, data.length()="+data.length()));
		
		nte.getCommentType().getText().setValue(type);
		nte.getComment(0).setValue(data);
	}

	/**
	 * @param nte
	 * @param type should be a short string denoting what's in the comment data, i.e. "REASON_FOR_REFERRAL" or "ALLERGIES"
	 * @param data will be base64 encoded, the encoded length must still be less than 65535
	 * @throws HL7Exception 
	 * @throws UnsupportedEncodingException 
	 */
	public static void fillNte(NTE nte, String type, byte[] data) throws HL7Exception, UnsupportedEncodingException
	{
		String dataString=OscarToOscarUtils.encodeBase64String(data); 
		fillNte(nte, type, dataString);
	}
}
