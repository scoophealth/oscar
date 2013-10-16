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
package org.oscarehr.event;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

public class EventService implements ApplicationEventPublisherAware {
	Logger logger = MiscUtils.getLogger();
	protected ApplicationEventPublisher applicationEventPublisher;
	
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher arg0) {
		this.applicationEventPublisher = arg0;

	}
	
	
	/*
	 * Event is fired:	 		
 		src/main/webapp/appointment/appointmentupdatearecord.jsp -- edit appt screen
 		src/main/webapp/provider/provideraddstatus.jsp  -- in appt screen when user clicks on the icon to change the appt status
	 * 
	 */
	public void appointmentStatusChanged(Object source,String appointment_no, String provider_no,String status){
		logger.debug("appointmentStatusChanged thrown by "+source.getClass().getName()+" appt# "+appointment_no+" status "+status);
		
		applicationEventPublisher.publishEvent(new AppointmentStatusChangeEvent(source,appointment_no, provider_no,status));
	}

	/*
	 * Event is fired:
    	src/main/webapp/appointment/appointmentaddarecord.jsp			
 	 	src/main/webapp/appointment/appointmentaddrecordcard.jsp	
 		src/main/webapp/appointment/appointmentaddrecordprint.jsp		
 	*/	
	public void appointmentCreated(Object source,String appointment_no,String provider_no){
		applicationEventPublisher.publishEvent(new AppointmentCreatedEvent(source,appointment_no, provider_no));
	}
}
