/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.Formintakec;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class IntakeCDao extends HibernateDaoSupport {

    private static Logger log = MiscUtils.getLogger();

    public Formintakec getCurrentForm(Integer demographicNo) {
        if (demographicNo == null || demographicNo.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        Formintakec result = null;

        @SuppressWarnings("unchecked")
        List<Formintakec> results = this.getHibernateTemplate().find("from Formintakec f where f.DemographicNo = ? order by FormEdited desc", demographicNo);
        if (!results.isEmpty()) {
            result = results.get(0);
        }

        if (log.isDebugEnabled()) {
            log.debug("getCurrentForm: demographicNo=" + demographicNo + ",found=" + (result != null));
        }

        return result;
    }

    public Formintakec getForm(Long id) {
        if (id == null || id.intValue() <= 0) {
            throw new IllegalArgumentException();
        }

        Formintakec result = this.getHibernateTemplate().get(Formintakec.class, id);

        if (log.isDebugEnabled()) {
            log.debug("getForm: id=" + id + ",found=" + (result != null));
        }

        return result;
    }

    public void saveForm(Formintakec form) {
        if (form == null) {
            throw new IllegalArgumentException();
        }

        getHibernateTemplate().saveOrUpdate(form);

        if (log.isDebugEnabled()) {
            log.debug("saveForm:" + form.getId());
        }
    }

    public List<Object[]> getCohort(Date EndDate, Date BeginDate, List clients) {
        if (BeginDate == null && EndDate == null) {
            return new ArrayList<Object[]>();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        List<Object[]> results = new ArrayList<Object[]>();

        if (log.isDebugEnabled()) {
            log.debug("Getting Cohort: " + BeginDate + " to " + EndDate);
        }

        for (int x = 0; x < clients.size(); x++) {
            Demographic client = (Demographic)clients.get(x);
            if (client.getPatientStatus().equals("AC")) {
                // get current intake
                Formintakec intake = this.getCurrentForm(client.getDemographicNo());
                // parse date
                Date admissionDate = null;
                try {
                    admissionDate = formatter.parse(intake.getAdmissionDate());
                }
                catch (Exception e) {
                	//empty
                }
                if (admissionDate == null) {
                    log.warn("invalid admission date for client #" + client.getDemographicNo());
                    continue;
                }
                // does it belong in this cohort?
                if (BeginDate != null && EndDate != null) {
                    if (admissionDate.after(BeginDate) && admissionDate.before(EndDate)) {
                        log.debug("admissionDate=" + admissionDate);
                        // ok, add this client
                        Object[] ar = new Object[2];
                        ar[0] = intake;
                        ar[1] = client;
                        results.add(ar);
                    }
                }
                if (BeginDate == null && admissionDate.before(EndDate)) {
                    log.debug("admissionDate=" + admissionDate);
                    // ok, add this client
                    Object[] ar = new Object[2];
                    ar[0] = intake;
                    ar[1] = client;
                    results.add(ar);
                }
            }
        }

        log.info("getCohort: found " + results.size() + " results. (" + BeginDate + " - " + EndDate + ")");

        return results;
    }
}
