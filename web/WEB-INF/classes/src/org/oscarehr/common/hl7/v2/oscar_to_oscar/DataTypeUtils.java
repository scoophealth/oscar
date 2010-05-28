package org.oscarehr.common.hl7.v2.oscar_to_oscar;

import org.apache.commons.lang.StringUtils;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.datatype.XAD;

public final class DataTypeUtils {
	private DataTypeUtils() {
		// not meant to be instantiated by anyone, it's a util class
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
	public static void fillXAD(XAD xad, String streetAddress, String city, String province, String country, String postalCode, String addressType) throws DataTypeException {
		xad.getStreetAddress().getStreetOrMailingAddress().setValue(StringUtils.trimToNull(streetAddress));
		xad.getCity().setValue(StringUtils.trimToNull(city));
		xad.getStateOrProvince().setValue(StringUtils.trimToNull(province));
		xad.getCountry().setValue(StringUtils.trimToNull(country));
		xad.getZipOrPostalCode().setValue(StringUtils.trimToNull(postalCode));
		xad.getAddressType().setValue(addressType);
	}
}
