package org.oscarehr.common.hl7.v2.oscar_to_oscar;

/**
 * Data structure for addresses.
 * <br /><br />
 * province  : 2 digit province / state code as defined by postal service, in canada it's in upper case, i.e. BC, ON
 * <br /><br />
 * country : iso 3166, 3 digit version / hl70399 code, i.e. USA, CAN, AUS in upper case.
 */
public class StreetAddressDataHolder {
	public String streetAddress;
	public String city;
	public String province;
	public String country;
	public String postalCode;
}
