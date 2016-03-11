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
package org.oscarehr.billing.CA.filters;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CodeFilterManager {
	private static Logger logger = MiscUtils.getLogger();
	
	
	@Autowired 
	@Qualifier("DxresearchDAO")
	private DxresearchDAO dxresearchDao;// = SpringUtils.getBean(DxresearchDAO.class);
	
	@Autowired(required=false)
	private CodeFilterDataSource codeFilterDataSource ;
	/*
	Filter takes in:
		- input list of codes eg (A007A, ....
		- where: office / home / LTC / Hosp / Phone
		- procedure (yes/no) 
	 	- time/date of day M-F 7-5 / M-F / M-F 5-midnight / Sat & Sun / Nights
	 	- Demographic Object 
	 		- sex male / female
	 	 	- age <6w / 6w -12mo / 12 - 24mo / 2-10y / 10-15y / 16-17y / 18-20y / 21-49y / 50-64y / 65-70y / 71-74y / 75>
	 	 	
	 	 	
	 	First approach is to try to find a reason why this code shouldn't be used and then fail fast and return FALSE
	 	ie. code is for 12-24mo and person is 65, just return false and don't look further 	
	 	
	 */
	public boolean isCodeValid(String billingCode, String location, boolean procedure, Date dateOfAppt,Demographic demographic  ){
		logger.debug("billingCode "+billingCode+"  location "+location+"  procedure "+ procedure+"  dateOfAppt "+ dateOfAppt +" Demographic "+ demographic  );
		if(codeFilterDataSource == null){
			return true;
		}
				
		Map<String,CodeFilter> map = codeFilterDataSource.getDataMap();
		
		if(map == null || billingCode == null || billingCode.length() < 5){
			return true;
		}
		
		CodeFilter cf = map.get(billingCode.substring(0, 4));
		
		if(cf == null){
			logger.debug("no data on "+billingCode);
			return true;
		}
		logger.debug("cf: "+cf);
		//Age Calculations
		int age = demographic.getAgeInYears();
		logger.debug("Age: "+age+" cf.a12to24m:" +cf.a12to24m+" cf.a2to10y:"+cf.a2to10y+" cf.a10to15y:"+cf.a10to15y+" cf.a16to17y:"+cf.a16to17y+" cf.a18to20y:"+cf.a18to20y+" cf.a21to49:"+cf.a21to49+" cf.a50to64:"+cf.a50to64+" cf.a65to70:"+cf.a65to70+" cf.a71to74:"+cf.a71to74+" cf.a75older:"+cf.a75older);
		if(age > 0){
			if(age == 1 && cf.a12to24m == false){
				return false;
			}else if(age >=2 && age <=10 && cf.a2to10y == false){
				return false;
			}else if(age >=10 && age <=15 && cf.a10to15y == false){
				return false;
			}else if(age >=16 && age <=17 && cf.a16to17y == false){
				return false;
			}else if(age >=18 && age <=20 && cf.a18to20y == false){
				return false;
			}else if(age >=21 && age <=49 && cf.a21to49 !=null && cf.a21to49 == false){
				return false;
			}else if(age >=50 && age <=64 && cf.a50to64 == false){
				return false;
			}else if(age >=65 && age <=70 && cf.a65to70 == false){
				return false;
			}else if(age >=71 && age <=74 && cf.a71to74 == false){
				return false;
			}else if(age >=75 && cf.a75older == false){
				return false;
			}
		}else{
			//more work if younger than 6w or 6w-12mo
		} 	
	
		//Sex -- Check to See if female flag is true and if so check the demographic
		if (cf.Female != null && cf.Female == true) {
			if(!"F".equals(demographic.getSex())){
				return false;
			}
		}
		
		//Process Date  
		//Check if atleast one of the flags has been set.  If not skip.
		if(dateOfAppt != null){
			if(cf.Mtof7to5 || cf.MtoF || cf.MtoF5tomidnight || cf.SandS || cf.Nights){
				Calendar cal = Calendar.getInstance();
				cal.setTime(dateOfAppt);
				//Need to check all 5 types and look for one that is true. If not returned false.
				boolean rightTime = false;
				
				if(cf.Mtof7to5){
					if((cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) && (cal.get(Calendar.HOUR_OF_DAY) >= 7 &&cal.get(Calendar.HOUR_OF_DAY) < 17)){ 
						rightTime = true;
					}
				}
				
				if(cf.MtoF){
					if((cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)){ 
						rightTime = true;
					}
				}
				
				if(cf.MtoF5tomidnight){
					if((cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) && (cal.get(Calendar.HOUR_OF_DAY) >= 17 &&cal.get(Calendar.HOUR_OF_DAY) <= 24) ){ 
						rightTime = true;
					}
				}
				
				if(cf.SandS){
					if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
						rightTime = true;
					}
				}
				
				if(cf.Nights){
					if(cal.get(Calendar.HOUR_OF_DAY) > 0 &&cal.get(Calendar.HOUR_OF_DAY) < 7){
						rightTime = true;
					}
				}
				
				if(!rightTime){
					return false;
				}
			}
			
			if(cf.Dx != null){
				/* Don't check if Dx hasn't been set. */
				boolean patientHasCodeInDxReg = false;
				List<Dxresearch> codeList = dxresearchDao.getDxResearchItemsByPatient(demographic.getDemographicNo()); 
				Arrays.sort(cf.Dx);
				if(codeList != null){
					for(Dxresearch dxresearch :codeList){
						if("ICD9".equals(dxresearch.getCodingSystem())){
							if(Arrays.binarySearch(cf.Dx,dxresearch.getDxresearchCode()) != -1){
								patientHasCodeInDxReg = true;
							}
						}
						if(patientHasCodeInDxReg){ 
							break; 
						}
					}
				}
				if(!patientHasCodeInDxReg){
					return false;
				}
			}//cf.Dx
		}
		
		return true;
	}
	
}
