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


package org.oscarehr.appointment.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.ScheduleDateDao;
import org.oscarehr.common.dao.ScheduleTemplateCodeDao;
import org.oscarehr.common.dao.ScheduleTemplateDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ScheduleDate;
import org.oscarehr.common.model.ScheduleTemplate;
import org.oscarehr.common.model.ScheduleTemplateCode;
import org.oscarehr.common.model.ScheduleTemplatePrimaryKey;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class NextAppointmentSearchHelper {
	static final int MAX_DAYS_TO_SEARCH = 180;

	static Logger logger = MiscUtils.getLogger();
	static ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");			 	
	static ScheduleDateDao scheduleDateDao = (ScheduleDateDao)SpringUtils.getBean("scheduleDateDao");
	static ScheduleTemplateDao scheduleTemplateDao = (ScheduleTemplateDao)SpringUtils.getBean("scheduleTemplateDao");
	static ScheduleTemplateCodeDao scheduleTemplateCodeDao = (ScheduleTemplateCodeDao)SpringUtils.getBean("scheduleTemplateCodeDao");
	static OscarAppointmentDao oscarAppointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
	
	/**
	 * Search against schedule for next appointment.
	 * 
	 * This implementation searches day by day until searchBean.numberOfResults is realized or MAX_DAYS_TO_SEARCH is reached
	 * 
	 * @param searchBean
	 */
	public static List<NextAppointmentSearchResult> search(NextAppointmentSearchBean searchBean) {
		List<NextAppointmentSearchResult> results = new ArrayList<NextAppointmentSearchResult>();
				
		Calendar c = Calendar.getInstance();
		int curHour = c.get(Calendar.HOUR_OF_DAY);
		
		int endTimeHour = Integer.parseInt(searchBean.getEndTimeOfDay());
		
		//can we search today?
		if((endTimeHour-curHour)>=1) {
			results.addAll(searchDay(c.getTime(),true,searchBean));
		} 
			
		//main loop..each from tomorrow onward
		int daysSearched=0;
		while(daysSearched < MAX_DAYS_TO_SEARCH) {
			c.add(Calendar.DAY_OF_MONTH, 1);
			results.addAll(searchDay(c.getTime(),false,searchBean));
			if(results.size()>=searchBean.getNumResults()) {
				break;
			}
			daysSearched++;
		}
		
		
		return results;
	}
	
	/**
	 * Break it up by provider..it doesn't really make sense to search all providers, but could be modified to do groups
	 * of providers.
	 * 
	 * @param day
	 * @param today
	 * @param searchBean
	 * @return
	 */
	private static List<NextAppointmentSearchResult> searchDay(Date day, boolean today, NextAppointmentSearchBean searchBean) {
		List<NextAppointmentSearchResult> results = new ArrayList<NextAppointmentSearchResult>();
		
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		if(searchBean.getDayOfWeek().length()>0) {
			if(searchBean.getDayOfWeek().equals("daily")) {
				if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					return results;				
				}
			} else {
				if(c.get(Calendar.DAY_OF_WEEK) != Integer.parseInt(searchBean.getDayOfWeek())) {
					return results;				
				}
			}
		}
		
		if(searchBean.getProviderNo().equals("")) {			
			List<Provider> providers = providerDao.getActiveProviders();
			for(Provider p:providers) {
				results.addAll(searchDayProvider(p.getProviderNo(),day,today,searchBean));
				if(results.size()>=searchBean.getNumResults()) {
					break;
				}
			}
		} else {
			results.addAll(searchDayProvider(searchBean.getProviderNo(),day,today,searchBean));
		}
		
		return results;
	}
	
	/**
	 * Search by provider and day.
	 * 
	 * 1) load up schedule
	 * 2) see what slots are available
	 * 3) for each slot, check to see if it's already booked
	 * 4) if not, add it to result set.
	 * 
	 * @param providerNo
	 * @param day
	 * @param today
	 * @param searchBean
	 * @return
	 */
	private static List<NextAppointmentSearchResult> searchDayProvider(String providerNo, Date day, boolean today, NextAppointmentSearchBean searchBean) {
		List<NextAppointmentSearchResult> results = new ArrayList<NextAppointmentSearchResult>();
		//load up the schedule
		ScheduleDate sd = scheduleDateDao.findByProviderNoAndDate(providerNo, day);
		if(sd == null) {
			logger.warn("no schedule found for provider " + providerNo + " on day " + day);
			return results;
		}
		//we have a schedule..lets check what template to use
		String templateName = sd.getHour();		
		ScheduleTemplate template = scheduleTemplateDao.find(new ScheduleTemplatePrimaryKey(providerNo,templateName));
		if(template == null) {
			logger.warn("no template found for provider " + providerNo + " and name=" + templateName);
			return results;
		}
		
		String timecode = template.getTimecode();   //length=96
		int slotsPerHour = (timecode.length()/24);  //4
		int slotSize = (60/(timecode.length()/24)); //15
		
		//check to see which slots are available between the start/end times - build a map.
		int startHour = Integer.parseInt(searchBean.getStartTimeOfDay());
		int startMin = 0;
		int endHour = Integer.parseInt(searchBean.getEndTimeOfDay());
		
		if(today) {
			Calendar c = Calendar.getInstance();
			int curHour = c.get(Calendar.HOUR_OF_DAY);
			int curMin= c.get(Calendar.MINUTE);
			if(curHour >= startHour) {
				startHour = curHour;
				startMin = curMin;
			}
		}
		
		//logger.info("startHour="+startHour + ",endHour="+endHour);
		
		for(int x=0;x<timecode.length();x++) {
			char slot = timecode.charAt(x);
			int hour = (int)Math.floor(x/slotsPerHour);
			int min = (x%slotsPerHour)*slotSize;
			if( (hour>=startHour) && (hour <=(endHour-1))) {
				if(hour==startHour && min<startMin) {
					continue;
				}
				//logger.info("currently at position " + x + " which is hour " + hour + " and min " + min);
				if(slot != '_') {
					//filter by code
					if(searchBean.getCode().length()>0) {
						if(slot != searchBean.getCode().charAt(0)) {
							logger.debug("skipping because code doesn't match, slot=" + slot + ",code=" + searchBean.getCode().charAt(0) + ".");
							continue;
						}
					}
					
					//TODO: is there a default appt length somewhere?					
					int duration = 15;
					if(searchBean.getCode().length()>0) {
						//load the template code
						ScheduleTemplateCode stc = scheduleTemplateCodeDao.getByCode(searchBean.getCode().charAt(0));
						if(stc == null) {
							logger.error("Error - ScheduleTemplateCode not found!!!");
							continue;
						}
						//check the duration						
						if(stc.getDuration() != null && stc.getDuration().length()>0 ) {
							duration = Integer.parseInt(stc.getDuration());
						}
					}
					
					//ready to check appointments
					//logger.info("schedule availability found at hour " + hour + ", min = " + min + " duration = " + duration);
					
					Calendar cal2 = Calendar.getInstance();
					cal2.setTime(day);
					cal2.set(Calendar.HOUR_OF_DAY, hour);
					cal2.set(Calendar.MINUTE, min);
					cal2.set(Calendar.SECOND,0);
					cal2.set(Calendar.MILLISECOND, 0);
					if(checkAvailability(cal2.getTime(), duration, providerNo)) {
						//logger.info("spot available at " + cal2.getTime() + " for " + duration + " mins with provider " + providerNo);
						NextAppointmentSearchResult result = new NextAppointmentSearchResult();
						result.setProviderNo(providerNo);
						result.setProvider(providerDao.getProvider(providerNo));
						result.setDate(cal2.getTime());
						result.setDuration(duration);
						results.add(result);
					} 
				}
			}						
		}
		return results;
	}
	
    private static boolean checkAvailability(Date date, int duration, String providerNo) {
    	List<Appointment> rs = oscarAppointmentDao.getByProviderAndDay(date,providerNo);
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	Date startTime = cal.getTime();
    	cal.add(Calendar.MINUTE, (duration-1));
    	Date endTime = cal.getTime();
    	
    	//MiscUtils.getLogger().info("checking availability - startTime:"+startTime + ",endTime="+endTime);
    	//startTime + duration = endTime
    	//we are wanting to make sure no appointments have overlapping time
    	boolean booked=false;
    	for(Appointment a:rs) {
    		Date apptStartDate = fixDate(date,a.getStartTime());
    		Date apptEndDate = fixDate(date,a.getEndTime());
    		//MiscUtils.getLogger().info("\tappt found @ startTime:"+apptStartDate + ",endTime="+apptEndDate);
        	
    		if(endTime.before(apptStartDate)) {
    			continue;
    		}
    		if(startTime.after(apptEndDate)) {
    			continue;
    		}
    		booked=true;
    	}
    	if(booked) {
    		return false;
    	}
    	
    	//available
    	return true;    	
    }
    
    private static Date fixDate(Date day, Date time) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(day);
    	Calendar acal = Calendar.getInstance();
    	acal.setTime(time);
    	cal.set(Calendar.HOUR_OF_DAY, acal.get(Calendar.HOUR_OF_DAY));
    	cal.set(Calendar.MINUTE, acal.get(Calendar.MINUTE));
    	cal.set(Calendar.SECOND,0);
    	cal.set(Calendar.MILLISECOND, 0);
    	return cal.getTime();
    }
}
