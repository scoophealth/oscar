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
package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.oscarehr.common.model.ConsultationResponse;
import org.oscarehr.consultations.ConsultationResponseSearchFilter;
import org.oscarehr.consultations.ConsultationResponseSearchFilter.SORTMODE;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ConsultResponseDao extends AbstractDao<ConsultationResponse> {
	private Logger logger = MiscUtils.getLogger();
	
	public ConsultResponseDao() {
		super(ConsultationResponse.class);
	}
	
	public int getConsultationCount(ConsultationResponseSearchFilter filter) {
		String sql = getSearchQuery(filter,true);
		logger.debug("sql="+sql);
		
		Query query = entityManager.createQuery(sql);
		Long count = this.getCountResult(query);
		
		
		return count.intValue();
	}

	@SuppressWarnings("unchecked")
    public List<Object[]> search(ConsultationResponseSearchFilter filter) {
		String sql = this.getSearchQuery(filter,false);
		logger.debug("sql="+sql);
		
		Query query = entityManager.createQuery(sql);
		query.setFirstResult(filter.getStartIndex());
		query.setMaxResults(filter.getNumToReturn());
		return query.getResultList();
	}
	
	
	
	private String getSearchQuery(ConsultationResponseSearchFilter filter, boolean selectCountOnly) {
		StringBuilder sql = new StringBuilder(
				"select "+ (selectCountOnly?"count(*)":"cr,sp,d,p") +
				" from ConsultationResponse cr , ProfessionalSpecialist sp, Demographic d left outer join d.provider p" +
				" where sp.id = cr.referringDocId and d.DemographicNo = cr.demographicNo ");
		
		if (filter.getAppointmentStartDate() != null) {
			sql.append("and cr.appointmentDate >=  '" + FastDateFormat.getInstance("yyyy-MM-dd").format(filter.getAppointmentStartDate()) + "' ");
		}
		if (filter.getAppointmentEndDate() != null) {
			sql.append("and cr.appointmentDate <=  '" +  DateFormatUtils.ISO_DATE_FORMAT.format(filter.getAppointmentEndDate()) + " 23:59:59' ");
		}
		if (filter.getReferralStartDate() != null) {
			sql.append("and cr.referralDate >=  '" + DateFormatUtils.ISO_DATE_FORMAT.format(filter.getReferralStartDate()) + "' ");
		}
		if (filter.getReferralEndDate() != null) {
			sql.append("and cr.referralDate <=  '" + DateFormatUtils.ISO_DATE_FORMAT.format(filter.getReferralEndDate()) + " 23:59:59' ");
		}
		if (filter.getResponseStartDate() != null) {
			sql.append("and cr.responseDate >=  '" + DateFormatUtils.ISO_DATE_FORMAT.format(filter.getResponseStartDate()) + "' ");
		}
		if (filter.getResponseEndDate() != null) {
			sql.append("and cr.responseDate <=  '" + DateFormatUtils.ISO_DATE_FORMAT.format(filter.getResponseEndDate()) + " 23:59:59' ");
		}
		if (filter.getStatus()!=null) {
			sql.append("and cr.status = '" + filter.getStatus() + "' ");
		} else {
			sql.append("and cr.status!=4 and cr.status!=5 ");
		}
		if (StringUtils.isNotBlank(filter.getTeam())) {
			sql.append("and cr.sendTo = '" + StringEscapeUtils.escapeSql(filter.getTeam()) + "' ");
		}
		if (StringUtils.isNotBlank(filter.getUrgency())) {
			sql.append("and cr.urgency = '" + StringEscapeUtils.escapeSql(filter.getUrgency()) + "' ");
		}
		if (filter.getDemographicNo() != null && filter.getDemographicNo()>0) {
			sql.append("and cr.demographicNo = " + filter.getDemographicNo() + " ");
		}
		if (filter.getMrpNo() != null && filter.getMrpNo()>0) {
			sql.append("and d.ProviderNo = '" + filter.getMrpNo() + "' ");
		}
		
		String orderBy = "cr.referralDate";
		String orderDir = "desc";
		
		if(filter.getSortDir() != null) {
			orderDir = filter.getSortDir().toString();
		}
		if(SORTMODE.AppointmentDate.equals(filter.getSortMode())) {
			orderBy = "cr.appointmentDate " + orderDir + ",cr.appointmentTime " + orderDir;
		} else if(SORTMODE.Demographic.equals(filter.getSortMode())) {
			orderBy = "d.LastName " + orderDir + ",d.FirstName " + orderDir;
		} else if(SORTMODE.ReferringDoctor.equals(filter.getSortMode())) {
			orderBy = "sp.lastName "+ orderDir + ",sp.firstName " + orderDir;
		} else if(SORTMODE.Team.equals(filter.getSortMode())) {
			orderBy = "cr.sendTo " + orderDir;
		} else if(SORTMODE.Status.equals(filter.getSortMode())) {
			orderBy = "cr.status " + orderDir;
		} else if(SORTMODE.Provider.equals(filter.getSortMode())) {
			orderBy = "p.LastName "+ orderDir+",p.FirstName " + orderDir;
		}  else if(SORTMODE.FollowUpDate.equals(filter.getSortMode())) {
			orderBy = "cr.followUpDate "+ orderDir;
		} else if(SORTMODE.ReferralDate.equals(filter.getSortMode())) {
			orderBy = "cr.referralDate "+ orderDir;
		} else if(SORTMODE.ResponseDate.equals(filter.getSortMode())) {
			orderBy =  "cr.responseDate "+ orderDir;
		} else if(SORTMODE.Urgency.equals(filter.getSortMode())) {
			orderBy = "cr.urgency "+ orderDir;
		}
		
		orderBy = " ORDER BY " + orderBy;
		sql.append(orderBy);
		
		return sql.toString();
	}
}
