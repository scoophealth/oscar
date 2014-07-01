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
package oscar.oscarBilling.ca.bc.pageUtil;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;

/**
 * 
 * @author Dennis Warren 
 * Company: Colcamex Resources
 * Copyright 2013
 * 
 * This class was created to override sequence's P100+ that comprise part two of a C02 Teleplan submission.
 * Adaptations are made two these sequence variables based on the Other Insurer code.
 *  
 * If a provider is Pay Patient Opted-Out this class will override the Oin Insurer Code and insert a PP.
 * 
 * Opted-Out providers cannot bill reciprocal claims therefore a province code will be overridden with PP if the 
 * provider is Opted-Out. 
 * 
 * For Work Safe BC claims the code WC covers both Opted-Out providers and Work Safe claims: if the provider
 * is Opted-Out AND is making a Work Safe claim this will be overridden with a WC.
 * 
 * To activate ensure the following settings are in Oscar_Mcmaster.properties:
 * 
 * # Assign each provider that is participating in PPOO by their provider number
 * # [provider number min] - [provider number max] Use a dash(-) between a range of provider numbers.
 * # [provider number], [provider number], [provider number]. Use a comma(,) between each specific provider in a list. 
 * # i.e. 1-5 assigns providers 1 2 3 4 and 5 as PPOO
 * # i.e. 1,5,10,9 assigns providers 1 5 10 and 9 as PPOO
 * # Commenting out or blank variables will cause the system to use a default list of provider numbers.
 * # The default numbers are: 777777 through 777782
 * # These list options are "either/or"
 * 
 * BC_MSP_OPTOUT_PROVIDER_NUMBER=701-750
 * 
 */
public class OverrideInsurerCode {
	
	private static final Logger logger=MiscUtils.getLogger();
	
	public static final int DEFAULT_OPTOUT_PROVIDER_NUMBER_MIN = 777777;
	public static final int DEFAULT_OPTOUT_PROVIDER_NUMBER_RANGE = 5;
	
	public static final String BC_INSTITUTIONAL_CLAIM = "IN";
	public static final String BC_OPTED_OUT = "PP"; // not including institutional
	public static final String WORK_SAFE_BC = "WC"; // includes opted out payments
	
	// valid province codes as stated by MSP. 
	public static enum VALID_PROVINCE_CODES {AB, SK, MB, ON, NB, NS, PE, NF, NT, YT, NU}
	
	public static String getOinInsurerCode(Integer providerNo, String billingType) {
		String provider_no = null;
		
		if(providerNo != null) {
			if(providerNo > 0) {
				provider_no = " "+providerNo;
			}
		}
		
		return getOinInsurerCode(provider_no, billingType);
	}
	
	
	public static String getOinInsurerCode(String providerNo, String billingType) {
		String oinInsurerCode = null;
		int providerNumber = 0;
		int providerOptedOut = 0;
		
		if (providerNo != null) {
			
			logger.info("Oin Insurer Code Override Provider number: "+providerNo);
			
			providerNumber = Integer.parseInt(providerNo);
			
			// always returns a default list.
			int[] optedOutProviders = getOptedOutProviders();
			
			for(int i = 0; i < optedOutProviders.length; i++) {
				if(providerNumber == optedOutProviders[i]) {
					providerOptedOut = 1;
				}
			}
				
			// check the provider
			if(providerOptedOut > 0) {				
				oinInsurerCode = BC_OPTED_OUT;
				logger.info("Provider is BC MSP Opted-out");				
			}
			
			if( billingType.equalsIgnoreCase(WORK_SAFE_BC) ) {				
				oinInsurerCode = WORK_SAFE_BC;
				logger.info("This is a WCB billing");
			}
				
		}
		
		return oinInsurerCode;
	}
	
	/**
	 * Retrieves a list of approved opted out provider numbers.
	 * @return
	 */
	private static int[] getOptedOutProviders() {
		
		OscarProperties properties = OscarProperties.getInstance();
		String optedOutProviderString = null;
		int[] optedOutProviders = null;
		int providerMin = 0;
		int providerMax = 0;
		String[] split = null;
				
		if(properties != null) {
			
			if(properties.containsKey("BC_MSP_OPTOUT_PROVIDER_NUMBER")) {	
				optedOutProviderString = properties.getProperty("BC_MSP_OPTOUT_PROVIDER_NUMBER");
				optedOutProviderString = optedOutProviderString.trim();
			} 

			if(optedOutProviderString != null){
				
				// for range of provider numbers separated by a "-"
				if(optedOutProviderString.contains("-")) {
					
					split = optedOutProviderString.split("-", 2);
					String min = split[0].trim();
					String max = split[1].trim();
					
					if( (min != null) && (max != null) && (min != "") && (max != "") ) {
						providerMin = Integer.parseInt(min);
						providerMax = Integer.parseInt(max);
					}
					
					if( (providerMin > 0) && (providerMax > 0) ) {
						int range = Math.abs(providerMax - providerMin);
						
						optedOutProviders = new int[range];
						optedOutProviders[0] = providerMin;
						for(int i = 1; i < range; i++) {
							optedOutProviders[i] = providerMin +1;
						}
					}
				
				// comma delimited list of provider numbers.
				} else if(optedOutProviderString.contains(",")) { 

					split = optedOutProviderString.split(",");
					int size = split.length;
					String number = null;
					
					if(size > 0) {
						optedOutProviders = new int[size];
						for(int i = 0; i < size; i++) {
							number = split[i].trim();
							if( (number == null) || (number == "") ) {
								optedOutProviders[i] = 0;
							} else {
								optedOutProviders[i] = Integer.parseInt(number);
							}
						}
					}
				
				// single provider number.
				} else {					
					optedOutProviders = new int[]{ Integer.parseInt(optedOutProviderString) };
				}
			}
			
		}
		
		if( (optedOutProviderString == null) || (optedOutProviders == null) )  {
			
			optedOutProviders = new int[DEFAULT_OPTOUT_PROVIDER_NUMBER_RANGE];
			optedOutProviders[0] = DEFAULT_OPTOUT_PROVIDER_NUMBER_MIN;
			for(int i = 1; i < DEFAULT_OPTOUT_PROVIDER_NUMBER_RANGE; i++) {
				optedOutProviders[i] = DEFAULT_OPTOUT_PROVIDER_NUMBER_MIN +1;
			}
			
		}
		
		return optedOutProviders;
	}

}
