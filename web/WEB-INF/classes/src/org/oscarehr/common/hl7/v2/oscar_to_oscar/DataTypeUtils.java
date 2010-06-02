package org.oscarehr.common.hl7.v2.oscar_to_oscar;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.datatype.PLN;
import ca.uhn.hl7v2.model.v25.datatype.XAD;
import ca.uhn.hl7v2.model.v25.datatype.XPN;
import ca.uhn.hl7v2.model.v25.datatype.XTN;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import ca.uhn.hl7v2.model.v25.segment.PRD;
import ca.uhn.hl7v2.model.v25.segment.SFT;

public final class DataTypeUtils {
	/**
	 * Don't access this formatter directly, use the getAsFormattedString method, it provides synchronisation
	 */
	private static final SimpleDateFormat dateTimeFormatter=new SimpleDateFormat("yyyyMMddHHmmss");
	
	private DataTypeUtils() {
		// not meant to be instantiated by anyone, it's a util class
	}

	public static String getAsFormattedString(Date date)
	{
		synchronized (dateTimeFormatter) {
	        return(dateTimeFormatter.format(date));
        }
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
	 * @param messageStructure i.e. "REF I12"
	 */
	public static void fillMsh(MSH msh, Date dateOfMessage, String facilityName, String messageCode, String triggerEvent, String messageStructure) throws DataTypeException
	{
		msh.getFieldSeparator().setValue("|");
		msh.getEncodingCharacters().setValue("^~\\&");
		
		msh.getDateTimeOfMessage().getTime().setValue(getAsFormattedString(dateOfMessage));
		
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
	public static void fillSft(SFT sft, String version, String build) throws DataTypeException
	{
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
	public static void fillPrd(PRD prd, Provider provider, String providerRoleId, String providerRoleDescription, StreetAddressDataHolder officeStreetAddressDataHolder) throws DataTypeException, HL7Exception
	{
		//Value Description
		//-----------------
		// RP   Referring Provider
		// PP   Primary Care Provider
		// CP   Consulting Provider
		// RT   Referred to Provider
		prd.getProviderRole(0).getIdentifier().setValue(providerRoleId);
		prd.getProviderRole(0).getText().setValue(providerRoleDescription);
		
		XPN xpn=prd.getProviderName(0);
		xpn.getFamilyName().getSurname().setValue(provider.getLastName());
		xpn.getGivenName().setValue(provider.getFirstName());
		xpn.getPrefixEgDR().setValue("DR");
		
		XAD xad=prd.getProviderAddress(0);
		fillXAD(xad, officeStreetAddressDataHolder, "O");
		
		XTN xtn=prd.getProviderCommunicationInformation(0);
		xtn.getUnformattedTelephoneNumber().setValue(provider.getWorkPhone());
		xtn.getEmailAddress().setValue(provider.getEmail());
		
		PLN pln=prd.getProviderIdentifiers(0);
		pln.getIDNumber().setValue(provider.getProviderNo());
	}

	/**
	 * @param prd
	 * @param provider
	 * @param providerRoleId Note that this is not the oscar provider role, look in the method to see valid values
	 * @param providerRoleDescription Note that this is not the oscar provider role, look in the method to see valid values
	 */
	public static void fillPrd(PRD prd, ProfessionalSpecialist professionalSpecialist, String providerRoleId, String providerRoleDescription, StreetAddressDataHolder officeStreetAddressDataHolder) throws DataTypeException, HL7Exception
	{
		//Value Description
		//-----------------
		// RP   Referring Provider
		// PP   Primary Care Provider
		// CP   Consulting Provider
		// RT   Referred to Provider
		prd.getProviderRole(0).getIdentifier().setValue(providerRoleId);
		prd.getProviderRole(0).getText().setValue(providerRoleDescription);
		
		XPN xpn=prd.getProviderName(0);
		xpn.getFamilyName().getSurname().setValue(professionalSpecialist.getLastName());
		xpn.getGivenName().setValue(professionalSpecialist.getFirstName());
		xpn.getPrefixEgDR().setValue(professionalSpecialist.getProfessionalLetters());
		
		XAD xad=prd.getProviderAddress(0);
		fillXAD(xad, officeStreetAddressDataHolder, "O");
		
		XTN xtn=prd.getProviderCommunicationInformation(0);
		xtn.getUnformattedTelephoneNumber().setValue(professionalSpecialist.getPhoneNumber());
		xtn.getEmailAddress().setValue(professionalSpecialist.getEmailAddress());
		
		PLN pln=prd.getProviderIdentifiers(0);
		pln.getIDNumber().setValue(professionalSpecialist.getId().toString());
	}
}
