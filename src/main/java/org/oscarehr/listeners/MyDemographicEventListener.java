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
package org.oscarehr.listeners;

import org.apache.log4j.Logger;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.event.DemographicCreateEvent;
import org.oscarehr.event.DemographicUpdateEvent;
import org.oscarehr.util.MiscUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MyDemographicEventListener implements ApplicationListener {

	private Logger log = MiscUtils.getLogger();
	
	public void onApplicationEvent(ApplicationEvent event) {
		
		if(event instanceof DemographicCreateEvent) {
			log.info("Event Occurred : " + event);
			DemographicCreateEvent e = (DemographicCreateEvent)event;
			Integer demographicNo = e.getDemographicNo();
			Demographic demographic = (Demographic)e.getSource();
			log.info("id = " +  demographicNo + ",age="+demographic.getAge());
		}
		if(event instanceof DemographicUpdateEvent) {
			log.info("Event Occurred : " + event);
			DemographicUpdateEvent e = (DemographicUpdateEvent)event;
			Integer demographicNo = e.getDemographicNo();
			Demographic demographic = (Demographic)e.getSource();
			log.info("id = " +  demographicNo + ",age="+demographic.getAge());
		}
		
        
    }
}
