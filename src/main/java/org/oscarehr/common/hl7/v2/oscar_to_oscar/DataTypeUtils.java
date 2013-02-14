/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package org.oscarehr.common.hl7.v2.oscar_to_oscar;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.Gender;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v26.datatype.CX;
import ca.uhn.hl7v2.model.v26.datatype.DT;
import ca.uhn.hl7v2.model.v26.datatype.DTM;
import ca.uhn.hl7v2.model.v26.datatype.FT;
import ca.uhn.hl7v2.model.v26.datatype.PLN;
import ca.uhn.hl7v2.model.v26.datatype.XAD;
import ca.uhn.hl7v2.model.v26.datatype.XCN;
import ca.uhn.hl7v2.model.v26.datatype.XPN;
import ca.uhn.hl7v2.model.v26.datatype.XTN;
import ca.uhn.hl7v2.model.v26.segment.MSH;
import ca.uhn.hl7v2.model.v26.segment.NTE;
import ca.uhn.hl7v2.model.v26.segment.PID;
import ca.uhn.hl7v2.model.v26.segment.PRD;
import ca.uhn.hl7v2.model.v26.segment.ROL;
import ca.uhn.hl7v2.model.v26.segment.SFT;

public final class DataTypeUtils {
	private static final Logger logger = MiscUtils.getLogger();
	public static final String HEALTH_NUMBER = "HEALTH_NUMBER";
	public static final String CHART_NUMBER = "CHART_NUMBER";
	public static final String HL7_VERSION_ID = "2.6";
	public static final int NTE_COMMENT_MAX_SIZE = 65536;
	public static final String ACTION_ROLE_SENDER = "SENDER";
	public static final String ACTION_ROLE_RECEIVER = "RECEIVER";

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

	private static GregorianCalendar getCalendarFromDT(DT dt) throws DataTypeException {

		// hl7/hapi returns 0 for no date
		if (dt.getYear() == 0 || dt.getMonth() == 0 || dt.getDay() == 0) return (null);

		GregorianCalendar cal = new GregorianCalendar();
		// zero out fields we don't use
		cal.setTimeInMillis(0);
		cal.set(GregorianCalendar.YEAR, dt.getYear());
		cal.set(GregorianCalendar.MONTH, dt.getMonth() - 1);
		cal.set(GregorianCalendar.DAY_OF_MONTH, dt.getDay());

		// force materialisation of values
		cal.getTimeInMillis();

		return (cal);
	}

	public static GregorianCalendar getCalendarFromDTM(DTM dtm) throws DataTypeException {

		// hl7/hapi returns 0 for no date
		if (dtm.getYear() == 0 || dtm.getMonth() == 0 || dtm.getDay() == 0) return (null);

		GregorianCalendar cal = new GregorianCalendar();
		// zero out fields we don't use
		cal.setTimeInMillis(0);
		cal.set(GregorianCalendar.YEAR, dtm.getYear());
		cal.set(GregorianCalendar.MONTH, dtm.getMonth() - 1);
		cal.set(GregorianCalendar.DAY_OF_MONTH, dtm.getDay());
		cal.set(GregorianCalendar.HOUR_OF_DAY, dtm.getHour());
		cal.set(GregorianCalendar.MINUTE, dtm.getMinute());
		cal.set(GregorianCalendar.SECOND, dtm.getSecond());

		// force materialisation of values
		cal.getTimeInMillis();

		return (cal);
	}

	/**
	 * @param xad
	 * @param clinic
	 * @param country iso 3166, 3 digit version / hl70399 code, i.e. USA, CAN, AUS in upper case.
	 * @param addressType hlt0190 code, i.e. O=office, H=Home
	 * @throws DataTypeException
	 */
	public static void fillXAD(XAD xad, Clinic clinic, String country, String addressType) throws DataTypeException {
		xad.getStreetAddress().getStreetOrMailingAddress().setValue(StringUtils.trimToNull(clinic.getClinicAddress()));
		xad.getCity().setValue(StringUtils.trimToNull(clinic.getClinicCity()));
		xad.getStateOrProvince().setValue(StringUtils.trimToNull(clinic.getClinicProvince()));
		if (country != null) xad.getCountry().setValue(StringUtils.trimToNull(country));
		xad.getZipOrPostalCode().setValue(StringUtils.trimToNull(clinic.getClinicPostal()));
		xad.getAddressType().setValue(addressType);
	}

	public static void fillXAD(XAD xad, Demographic demographic, String country, String addressType) throws DataTypeException {
		xad.getStreetAddress().getStreetOrMailingAddress().setValue(StringUtils.trimToNull(demographic.getAddress()));
		xad.getCity().setValue(StringUtils.trimToNull(demographic.getCity()));
		xad.getStateOrProvince().setValue(StringUtils.trimToNull(demographic.getProvince()));
		if (country != null) xad.getCountry().setValue(StringUtils.trimToNull(country));
		xad.getZipOrPostalCode().setValue(StringUtils.trimToNull(demographic.getPostal()));
		xad.getAddressType().setValue(addressType);
	}

	/**
	 * @param msh
	 * @param dateOfMessage
	 * @param clinicName 
	 * @param messageCode i.e. "REF"
	 * @param triggerEvent i.e. "I12"
	 * @param messageStructure i.e. "REF_I12"
	 * @param hl7VersionId is the version of hl7 in use, i.e. "2.6"
	 */
	public static void fillMsh(MSH msh, Date dateOfMessage, String clinicName, String messageCode, String triggerEvent, String messageStructure, String hl7VersionId) throws DataTypeException {
		msh.getFieldSeparator().setValue("|");
		msh.getEncodingCharacters().setValue("^~\\&");
		msh.getVersionID().getVersionID().setValue(hl7VersionId);

		msh.getDateTimeOfMessage().setValue(getAsHl7FormattedString(dateOfMessage));

		msh.getSendingApplication().getNamespaceID().setValue("OSCAR");

		msh.getSendingFacility().getNamespaceID().setValue(clinicName);

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
	public static void fillPrd(PRD prd, Provider provider, String providerRoleId, String providerRoleDescription, Clinic clinic) throws DataTypeException, HL7Exception {
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
		fillXAD(xad, clinic, null, "O");

		XTN xtn = prd.getProviderCommunicationInformation(0);
		xtn.getUnformattedTelephoneNumber().setValue(provider.getWorkPhone());
		xtn.getCommunicationAddress().setValue(provider.getEmail());

		PLN pln = prd.getProviderIdentifiers(0);
		pln.getIDNumber().setValue(provider.getProviderNo());
	}

	/**
	 * @return a detached and non-persisted Provider model object with data filed in from the PRD.
	 * @throws HL7Exception
	 */
	public static Provider parsePrdAsProvider(PRD prd) throws HL7Exception {
		Provider provider = new Provider();

		XPN xpn = prd.getProviderName(0);
		provider.setLastName(StringUtils.trimToNull(xpn.getFamilyName().getSurname().getValue()));
		provider.setFirstName(StringUtils.trimToNull(xpn.getGivenName().getValue()));
		provider.setTitle(StringUtils.trimToNull(xpn.getPrefixEgDR().getValue()));

		XAD xad = prd.getProviderAddress(0);
		provider.setAddress(StringUtils.trimToNull(xad.getStreetAddress().getStreetOrMailingAddress().getValue()));

		XTN xtn = prd.getProviderCommunicationInformation(0);
		provider.setWorkPhone(StringUtils.trimToNull(xtn.getUnformattedTelephoneNumber().getValue()));
		provider.setEmail(StringUtils.trimToNull(xtn.getCommunicationAddress().getValue()));

		PLN pln = prd.getProviderIdentifiers(0);
		provider.setProviderNo(StringUtils.trimToNull(pln.getIDNumber().getValue()));

		return (provider);
	}

	/**
	 * @param prd
	 * @param professionalSpecialist
	 * @param providerRoleId Note that this is not the oscar provider role, look in the method to see valid values
	 * @param providerRoleDescription Note that this is not the oscar provider role, look in the method to see valid values
	 */
	public static void fillPrd(PRD prd, ProfessionalSpecialist professionalSpecialist, String providerRoleId, String providerRoleDescription) throws DataTypeException, HL7Exception {
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
		xad.getStreetAddress().getStreetOrMailingAddress().setValue(StringUtils.trimToNull(professionalSpecialist.getStreetAddress()));
		xad.getAddressType().setValue("O");

		XTN xtn = prd.getProviderCommunicationInformation(0);
		xtn.getUnformattedTelephoneNumber().setValue(professionalSpecialist.getPhoneNumber());
		xtn.getCommunicationAddress().setValue(professionalSpecialist.getEmailAddress());

		PLN pln = prd.getProviderIdentifiers(0);
		pln.getIDNumber().setValue(professionalSpecialist.getId().toString());
	}

	public static ProfessionalSpecialist parsePrdAsProfessionalSpecialist(PRD prd) throws HL7Exception {
		ProfessionalSpecialist professionalSpecialist = new ProfessionalSpecialist();

		XPN xpn = prd.getProviderName(0);
		professionalSpecialist.setLastName(StringUtils.trimToNull(xpn.getFamilyName().getSurname().getValue()));
		professionalSpecialist.setFirstName(StringUtils.trimToNull(xpn.getGivenName().getValue()));
		professionalSpecialist.setProfessionalLetters(StringUtils.trimToNull(xpn.getPrefixEgDR().getValue()));

		XAD xad = prd.getProviderAddress(0);
		professionalSpecialist.setStreetAddress(StringUtils.trimToNull(xad.getStreetAddress().getStreetOrMailingAddress().getValue()));

		XTN xtn = prd.getProviderCommunicationInformation(0);
		professionalSpecialist.setPhoneNumber(StringUtils.trimToNull(xtn.getUnformattedTelephoneNumber().getValue()));
		professionalSpecialist.setEmailAddress(StringUtils.trimToNull(xtn.getCommunicationAddress().getValue()));

		return (professionalSpecialist);
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
		cx.getIdentifierTypeCode().setValue(HEALTH_NUMBER);
		// blank for everyone but ontario use version code
		cx.getIdentifierCheckDigit().setValue(demographic.getVer());
		// province
		cx.getAssigningJurisdiction().getIdentifier().setValue(demographic.getHcType());

		GregorianCalendar tempCalendar = new GregorianCalendar();
		if (demographic.getEffDate() != null) {
			tempCalendar.setTime(demographic.getEffDate());
			cx.getEffectiveDate().setYearMonthDayPrecision(tempCalendar.get(GregorianCalendar.YEAR), tempCalendar.get(GregorianCalendar.MONTH) + 1, tempCalendar.get(GregorianCalendar.DAY_OF_MONTH));
		}

		if (demographic.getHcRenewDate() != null) {
			tempCalendar.setTime(demographic.getHcRenewDate());
			cx.getExpirationDate().setYearMonthDayPrecision(tempCalendar.get(GregorianCalendar.YEAR), tempCalendar.get(GregorianCalendar.MONTH) + 1, tempCalendar.get(GregorianCalendar.DAY_OF_MONTH));
		}

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
		// N Nickname /Call me Name/Street Name
		// P Name of Partner/Spouse - obsolete (DO NOT USE)
		// R Registered Name (animals only)
		// S Coded Pseudo-Name to ensure anonymity
		// T Indigenous/Tribal/Community Name
		// U Unspecified
		xpn.getNameTypeCode().setValue("L");

		if (demographic.getBirthDay() != null) {
			DTM bday = pid.getDateTimeOfBirth();
			tempCalendar = demographic.getBirthDay();
			bday.setDatePrecision(tempCalendar.get(GregorianCalendar.YEAR), tempCalendar.get(GregorianCalendar.MONTH) + 1, tempCalendar.get(GregorianCalendar.DAY_OF_MONTH));
		}

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
		fillXAD(address, demographic, null, "H");

		XTN phone = pid.getPhoneNumberHome(0);
		phone.getUnformattedTelephoneNumber().setValue(demographic.getPhone());

		// ISO 639, hrmmm shall we say 639-1 (2 digit)?
		pid.getPrimaryLanguage().getIdentifier().setValue(demographic.getSpokenLanguage());

		// ISO table 3166.
		pid.getCitizenship(0).getIdentifier().setValue(demographic.getCitizenship());
	}

	/**
	 * This method returns a non-persisted, detached demographic model object.
	 * 
	 * @throws HL7Exception
	 */
	public static Demographic parsePid(PID pid) throws HL7Exception {
		Demographic demographic = new Demographic();

		XAD xad = pid.getPatientAddress(0);
		demographic.setAddress(StringUtils.trimToNull(xad.getStreetAddress().getStreetOrMailingAddress().getValue()));
		demographic.setCity(StringUtils.trimToNull(xad.getCity().getValue()));
		demographic.setProvince(StringUtils.trimToNull(xad.getStateOrProvince().getValue()));
		demographic.setPostal(StringUtils.trimToNull(xad.getZipOrPostalCode().getValue()));

		GregorianCalendar birthDate = DataTypeUtils.getCalendarFromDTM(pid.getDateTimeOfBirth());
		demographic.setBirthDay(birthDate);

		CX cx = pid.getPatientIdentifierList(0);
		// health card string, excluding version code
		demographic.setHin(StringUtils.trimToNull(cx.getIDNumber().getValue()));
		// blank for everyone but ontario use version code
		demographic.setVer(StringUtils.trimToNull(cx.getIdentifierCheckDigit().getValue()));
		// province
		demographic.setHcType(StringUtils.trimToNull(cx.getAssigningJurisdiction().getIdentifier().getValue()));
		// valid
		GregorianCalendar tempCalendar = DataTypeUtils.getCalendarFromDT(cx.getEffectiveDate());
		if (tempCalendar != null) demographic.setEffDate(tempCalendar.getTime());
		// expire
		tempCalendar = DataTypeUtils.getCalendarFromDT(cx.getExpirationDate());
		if (tempCalendar != null) demographic.setHcRenewDate(tempCalendar.getTime());

		XPN xpn = pid.getPatientName(0);
		demographic.setLastName(StringUtils.trimToNull(xpn.getFamilyName().getSurname().getValue()));
		demographic.setFirstName(StringUtils.trimToNull(xpn.getGivenName().getValue()));

		XTN phone = pid.getPhoneNumberHome(0);
		demographic.setPhone(StringUtils.trimToNull(phone.getUnformattedTelephoneNumber().getValue()));

		Gender gender = getOscarGenderFromHl7Gender(pid.getAdministrativeSex().getValue());
		if (gender != null) demographic.setSex(gender.name());

		return (demographic);
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
	 * @param subject should be a short string denoting what's in the comment data, i.e. "REASON_FOR_REFERRAL" or "ALLERGIES", max length is 250
	 * @param fileName should be the file name if applicable, can be null if it didn't come from a file.
	 * @param data and binary data, a String must pass in bytes too as it needs to be base64 encoded for \n and \r's
	 * @throws HL7Exception
	 * @throws UnsupportedEncodingException
	 */
	public static void fillNte(NTE nte, String subject, String fileName, byte[] data) throws HL7Exception, UnsupportedEncodingException {

		nte.getCommentType().getText().setValue(subject);
		if (fileName != null) nte.getCommentType().getNameOfCodingSystem().setValue(fileName);

		String stringData = new String(Base64.encodeBase64(data), MiscUtils.DEFAULT_UTF8_ENCODING);
		int dataLength = stringData.length();
		int chunks = dataLength / DataTypeUtils.NTE_COMMENT_MAX_SIZE;
		if (dataLength % DataTypeUtils.NTE_COMMENT_MAX_SIZE != 0) chunks++;
		logger.debug("Breaking Observation Data (" + dataLength + ") into chunks:" + chunks);

		for (int i = 0; i < chunks; i++) {
			FT commentPortion = nte.getComment(i);

			int startIndex = i * DataTypeUtils.NTE_COMMENT_MAX_SIZE;
			int endIndex = Math.min(dataLength, startIndex + DataTypeUtils.NTE_COMMENT_MAX_SIZE);

			commentPortion.setValue(stringData.substring(startIndex, endIndex));
		}
	}

	public static byte[] getNteCommentsAsSingleDecodedByteArray(NTE nte) {
		FT[] fts = nte.getComment();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fts.length; i++)
			sb.append(fts[i].getValue());

		return (Base64.decodeBase64(sb.toString()));
	}

	/**
	 * @param rol
	 * @param professionalSpecialist
	 * @param actionRole the role of the given provider with regards to this communcations. There is HL7 table 0443. We will also add DataTypeUtils.ACTION_ROLE_* as roles so we can send and receive arbitrary data under arbitrary conditions.
	 * @throws HL7Exception
	 */
	public static void fillRol(ROL rol, ProfessionalSpecialist professionalSpecialist, String actionRole) throws HL7Exception {
		rol.getActionCode().setValue("unused");
		rol.getRoleROL().getIdentifier().setValue(actionRole);

		fillXcn(rol.getRolePerson(0), professionalSpecialist);

		XAD xad = rol.getOfficeHomeAddressBirthplace(0);
		xad.getStreetAddress().getStreetOrMailingAddress().setValue(StringUtils.trimToNull(professionalSpecialist.getStreetAddress()));
		xad.getAddressType().setValue("O");

		XTN xtn = rol.getPhone(0);
		xtn.getUnformattedTelephoneNumber().setValue(professionalSpecialist.getPhoneNumber());
		xtn.getCommunicationAddress().setValue(professionalSpecialist.getEmailAddress());
	}

	public static void fillRol(ROL rol, Provider provider, Clinic clinic, String actionRole) throws HL7Exception {
		rol.getActionCode().setValue("unused");
		rol.getRoleROL().getIdentifier().setValue(actionRole);

		fillXcn(rol.getRolePerson(0), provider);

		XAD xad = rol.getOfficeHomeAddressBirthplace(0);
		fillXAD(xad, clinic, null, "O");

		XTN xtn = rol.getPhone(0);
		xtn.getUnformattedTelephoneNumber().setValue(provider.getPhone());
		xtn.getCommunicationAddress().setValue(provider.getEmail());
	}

	public static void fillXcn(XCN xcn, Provider provider) throws DataTypeException {
		xcn.getIDNumber().setValue(provider.getProviderNo());
		xcn.getFamilyName().getSurname().setValue(provider.getLastName());
		xcn.getGivenName().setValue(provider.getFirstName());
		xcn.getPrefixEgDR().setValue(provider.getTitle());
	}

	public static void fillXcn(XCN xcn, ProfessionalSpecialist professionalSpecialist) throws DataTypeException {
		xcn.getFamilyName().getSurname().setValue(professionalSpecialist.getLastName());
		xcn.getGivenName().setValue(professionalSpecialist.getFirstName());
		xcn.getPrefixEgDR().setValue(professionalSpecialist.getProfessionalLetters());
	}

	/**
	 * The provider returned is just a detached/unmanaged Provider object which may not represent an entry in the db, it is used as a data structure only.
	 * 
	 * @throws HL7Exception
	 */
	public static Provider parseRolAsProvider(ROL rol) throws HL7Exception {
		Provider provider = new Provider();

		XCN xcn = rol.getRolePerson(0);
		provider.setProviderNo(StringUtils.trimToNull(xcn.getIDNumber().getValue()));
		provider.setLastName(StringUtils.trimToNull(xcn.getFamilyName().getSurname().getValue()));
		provider.setFirstName(StringUtils.trimToNull(xcn.getGivenName().getValue()));
		provider.setTitle(StringUtils.trimToNull(xcn.getPrefixEgDR().getValue()));

		XAD xad = rol.getOfficeHomeAddressBirthplace(0);
		provider.setAddress(StringUtils.trimToNull(xad.getStreetAddress().getStreetOrMailingAddress().getValue()));

		XTN xtn = rol.getPhone(0);
		provider.setPhone(StringUtils.trimToNull(xtn.getUnformattedTelephoneNumber().getValue()));
		provider.setEmail(StringUtils.trimToNull(xtn.getCommunicationAddress().getValue()));

		return (provider);
	}
}
