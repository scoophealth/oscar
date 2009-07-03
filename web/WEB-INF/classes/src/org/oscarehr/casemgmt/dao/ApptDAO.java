/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
//:TODO move to appt package
package org.oscarehr.casemgmt.dao;

import java.util.List;

import org.caisi.model.Appointment;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ApptDAO extends HibernateDaoSupport {
    public void updateAppointmentStatus(String apptId, String status, String type) {

        String sql = "from Appointment appt where appt.id = ?";
        List list = getHibernateTemplate().find(sql, new Long(apptId));
        Appointment appt;
        if (list.size() != 0) {
            appt = (Appointment)list.get(list.size() - 1);
            oscar.appt.ApptStatusData as = new oscar.appt.ApptStatusData();
            as.setApptStatus(status);
            if (type.equalsIgnoreCase("sign")) appt.setStatus(as.signStatus());
            if (type.equalsIgnoreCase("verify")) appt.setStatus(as.verifyStatus());

            getHibernateTemplate().update(appt);
        }
    }
    
    public Appointment getAppt(String apptId) {
       Appointment appt = (Appointment) getHibernateTemplate().get(Appointment.class,new Long(apptId));
       return appt;
    }
    
   
}
