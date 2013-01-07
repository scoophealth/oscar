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
package org.oscarehr.billing.CA.BC.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.billing.CA.BC.model.Hl7Link;
import org.oscarehr.common.NativeSql;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

import oscar.util.ConversionUtils;

@Repository
@SuppressWarnings("unchecked")
public class Hl7LinkDao extends AbstractDao<Hl7Link>{

	public Hl7LinkDao() {
		super(Hl7Link.class);
	}
	
    public List<Object[]> findLabs() {
		String sql = "FROM Hl7Pid pid, Hl7Link link, Hl7Obr obr, Demographic demo " +
				"WHERE link.demographicNo = demo.id " +
				"AND pid.id = obr.pidId " +
				"AND ( link.status = 'P' OR link.status IS NULL ) " +
				"AND link.id = pid.id";

		Query q = entityManager.createQuery(sql);
		return q.getResultList();
	}
    
    public List<Object[]> findMagicLinks() {
    	String sql = "FROM Demographic demo, Hl7Pid pid, Hl7Link link " +
    	"WHERE pid.id = link.id " +
    	"AND demo.Hin = pid.externalId " +
    	"AND link.id IS NULL";
    	Query q = entityManager.createQuery(sql);
		return q.getResultList();
    }
    
    public List<Object[]> findLinksAndRequestDates(Integer demoId) {
    	String sql = "SELECT DISTINCT link.id, obr.requestedDateTime, obr.diagnosticServiceSectId " +
				"FROM Hl7Link link, Hl7Obr obr " +
				"WHERE link.demographicNo = :demoId " + 
				"AND link.id = obr.id " +
				"AND (" +
				"	link.status = 'N' " +
				"	OR link.status = 'A' " +
				"	OR link.status = 'S'" +
				") " +
				"ORDER BY obr.requestedDateTime DESC";
		Query query = entityManager.createQuery(sql);
		query.setParameter("demoId", demoId);
		return query.getResultList();
	
    }
    
	public List<Object[]> findProvidersWithReports() {
		String sql = "SELECT DISTINCT provider.ProviderNo, provider.LastName, provider.FirstName FROM Hl7Link hl7_link, Demographic demographic, Provider provider " +
				"WHERE hl7_link.demographicNo = demographic.DemographicNo " +
				"AND demographic.ProviderNo = provider.ProviderNo " +
				"AND demographic.ProviderNo IS NOT NULL";
		Query query = entityManager.createQuery(sql);
		return query.getResultList();
	}

	public List<Object[]> findReportsByProvider(String providerNo) {
		String sql = "FROM Hl7Link hl7_link, Demographic demographic, Hl7Pid hl7_pid, Hl7Obr hl7_obr, Hl7Message hl7_message, Provider provider " +
				"WHERE demographic.ProviderNo = provider.ProviderNo " +
				"AND hl7_link.id = hl7_obr.pidId " +
				"AND hl7_link.id = hl7_pid.id " +
				"AND demographic.ProviderNo = :providerNo " +
				"AND hl7_message.id = hl7_pid.messageId " +
				"AND demographic.DemographicNo = hl7_link.demographicNo " +
				"AND hl7_link.status != 'P'";
		Query query = entityManager.createQuery(sql);
		query.setParameter("providerNo", providerNo);
		return query.getResultList();
	}

	@NativeSql({"hl7_pid", "hl7_link", "hl7_obr", "hl7_message"})
	public List<Object[]> findReports(Date start, Date end, String provider_no, String orderby, String command) {
		String select_reports_by_provider         = "SELECT DISTINCT hl7_pid.pid_id, hl7_pid.patient_name, hl7_obr.ordering_provider, hl7_obr.result_copies_to, hl7_link.status, hl7_link.signed_on, provider.last_name, provider.first_name, hl7_message.date_time  FROM hl7_link, demographic, hl7_pid, hl7_obr, hl7_message, provider WHERE demographic.provider_no = provider.provider_no AND hl7_link.pid_id=hl7_obr.pid_id AND hl7_link.pid_id=hl7_pid.pid_id AND demographic.provider_no='@provider_no' AND hl7_message.message_id=hl7_pid.message_id AND demographic.demographic_no=hl7_link.demographic_no AND hl7_link.status!='P'";
		String select_reports_linked_to_providers = "SELECT DISTINCT hl7_pid.pid_id, hl7_pid.patient_name, hl7_obr.ordering_provider, hl7_obr.result_copies_to, hl7_link.status, hl7_link.signed_on, provider.last_name, provider.first_name, hl7_message.date_time  FROM hl7_link, demographic, hl7_pid, hl7_obr, hl7_message, provider WHERE demographic.provider_no = provider.provider_no AND hl7_link.pid_id=hl7_obr.pid_id AND hl7_link.pid_id=hl7_pid.pid_id AND hl7_message.message_id=hl7_pid.message_id AND demographic.demographic_no=hl7_link.demographic_no AND hl7_link.status!='P'";
		String select_unlinked_labs = "SELECT DISTINCT hl7_pid.pid_id, hl7_pid.patient_name, hl7_obr.ordering_provider, hl7_obr.result_copies_to, hl7_link.status, hl7_link.signed_on, hl7_message.date_time, '' as `last_name`, '' as `first_name` FROM hl7_pid left join hl7_link on hl7_link.pid_id=hl7_pid.pid_id left join hl7_obr on hl7_pid.pid_id=hl7_obr.pid_id left join hl7_message on hl7_message.message_id=hl7_pid.message_id WHERE hl7_link.status='P' OR hl7_link.status is null";
		String sqlWhere = (start != null ? " AND hl7_message.date_time >= '" + ConversionUtils.toDateString(start) + " 00:00:00'" : "") + 
				(end != null ? " AND hl7_message.date_time <= '" + ConversionUtils.toTimeString(end) + " 23:59:59'" : "");
		String sqlOrderBy = " ORDER BY @orderby";

		String sql = null;
		if (command != null && !command.equals("")) {
			if ("-ULL".equals(provider_no)) {
				sql = select_unlinked_labs;
			} else if ("-APL".equals(provider_no)) {
				sql = select_reports_linked_to_providers;
			} else if ("-UAP".equals(provider_no)) {
				sql = select_reports_by_provider;
			} else {
				sql = select_reports_by_provider;
			}
			sql += sqlWhere + sqlOrderBy;
			sql = sql.replaceAll("@provider_no", provider_no.replaceAll("-UAP", "")).replaceAll("@orderby", orderby);
			
			Query query = entityManager.createNativeQuery(sql);
			return query.getResultList();
		}

		return new ArrayList<Object[]>();
	}

}
