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

package org.oscarehr.casemgmt.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.caisi.model.CustomFilter;
import org.oscarehr.common.model.Provider;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation for the corresponding DAO interface 
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public class TicklerDAO extends HibernateDaoSupport {

    public List getTicklers(CustomFilter filter) {

        String tickler_date_order = filter.getSort_order();
        String query = "from Tickler t where t.service_date >= ? and t.service_date <= ? ";
        boolean includeProviderClause = true;
        boolean includeAssigneeClause = true;
        boolean includeStatusClause = true;
        boolean includeClientClause = true;
        boolean includeDemographicClause = true;

        if (filter.getStartDate() == null || filter.getStartDate().length() == 0) {
            filter.setStartDate("1900-01-01");
        }
        if (filter.getEndDate() == null || filter.getEndDate().length() == 0) {
            filter.setEndDate("8888-12-31");
        }

        if (filter.getProvider() == null || filter.getProvider().equals("All Providers")) {
            includeProviderClause = false;
        }
        if (filter.getAssignee() == null || filter.getAssignee().equals("All Providers")) {
            includeAssigneeClause = false;
        }
        if (filter.getClient() == null || filter.getClient().equals("All Clients")) {
            includeClientClause = false;
        }
        if (filter.getDemographic_no() == null || filter.getDemographic_no().equals("")) {
            includeDemographicClause = false;
        }

        if (filter.getStatus().equals("") || filter.getStatus().equals("Z")) {
            includeStatusClause = false;
        }

        List paramList = new ArrayList();
        paramList.add(filter.getStart_date());
        paramList.add(new java.util.Date(filter.getEnd_date().getTime() + DateUtils.MILLIS_PER_DAY));

        //TODO: IN clause
        if (includeProviderClause) {
            query = query + " and t.creator IN (";
            Set pset = filter.getProviders();
            Provider[] providers = (Provider[])pset.toArray(new Provider[pset.size()]);
            for (int x = 0; x < providers.length; x++) {
                if (x > 0) {
                    query += ",";
                }
                query += "?";
                paramList.add(providers[x].getProviderNo());
            }
            query += ")";
        }

        //TODO: IN clause
        if (includeAssigneeClause) {
            query = query + " and t.task_assigned_to IN (";
            Set pset = filter.getAssignees();
            Provider[] providers = (Provider[])pset.toArray(new Provider[pset.size()]);
            for (int x = 0; x < providers.length; x++) {
                if (x > 0) {
                    query += ",";
                }
                query += "?";
                paramList.add(providers[x].getProviderNo());
            }
            query += ")";
        }

        if (includeStatusClause) {
            query = query + " and t.status = ?";
            paramList.add(String.valueOf(filter.getStatus()));
        }
        if (includeClientClause) {
            query = query + "and t.demographic_no = ?";
            paramList.add(filter.getClient());
        }
        if (includeDemographicClause) {
            query = query + "and t.demographic_no = ?";
            paramList.add(filter.getDemographic_no());
        }
        Object params[] = paramList.toArray(new Object[paramList.size()]);

        return getHibernateTemplate().find(query + "order by t.service_date " + tickler_date_order, params);
    }

}
