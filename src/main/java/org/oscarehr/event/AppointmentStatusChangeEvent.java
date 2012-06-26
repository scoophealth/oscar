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
import org.springframework.context.ApplicationEvent;

public class AppointmentStatusChangeEvent extends ApplicationEvent{
	Logger logger = MiscUtils.getLogger();
	
	/**
     * The ID of the appointment whose status has changed.
     */
    private final String appointment_no;
    /**
     * The ID of the provider who has the appointment.
     */
    private final String provider_no;
    /**
     * The new appointment status.
     */
    private final String status;
		
	public AppointmentStatusChangeEvent(Object source,String appointment_no, String provider_no,String status) {
		super(source);
		this.appointment_no = appointment_no;
        this.provider_no = provider_no;
        this.status = status;
	    logger.debug("Object up "+source.getClass().getName());
    }
   
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        AppointmentStatusChangeEvent other = (AppointmentStatusChangeEvent) obj;
        if (this.appointment_no == null) {
            if (other.appointment_no != null) {
                return false;
            }
        } else if (!this.appointment_no.equals(other.appointment_no)) {
            return false;
        }
        if (this.provider_no == null) {
            if (other.provider_no != null) {
                return false;
            }
        } else if (!this.provider_no.equals(other.provider_no)) {
            return false;
        }
        if (this.status == null) {
            if (other.status != null) {
                return false;
            }
        } else if (!this.status.equals(other.status)) {
            return false;
        }
        return true;
    }

    /**
     * Returns the ID of the appointment whose status has changed.
     * 
     * @return The current ID of the appointment whose status has changed.
     */
    public final String getAppointment_no() {
        return this.appointment_no;
    }

    /**
     * Returns the ID of the provider who has the appointment.
     * 
     * @return The current ID of the provider who has the appointment.
     */
    public final String getProvider_no() {
        return this.provider_no;
    }

    /**
     * Returns the new appointment status.
     * 
     * @return The current value of the new appointment status.
     */
    public final String getStatus() {
        return this.status;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result)
                + ((this.appointment_no == null) ? 0 : this.appointment_no
                        .hashCode());
        result = (prime * result)
                + ((this.provider_no == null) ? 0 : this.provider_no.hashCode());
        result = (prime * result)
                + ((this.status == null) ? 0 : this.status.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AppointmentChangeEvent [appointment_no=" + this.appointment_no
                + ", provider_no=" + this.provider_no + ", status="
                + this.status + "]";
    }
	
	
	
	
	
	
}
