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
package org.oscarehr.consultations;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.oscarehr.common.PaginationQuery;
import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.common.model.ConsultationRequest;
import org.springframework.stereotype.Repository;

@Repository
public class ConsultationDao extends AbstractDao<ConsultationRequest> {

	public ConsultationDao() {
		super(ConsultationRequest.class);
	}

	public int getConsultationCount(PaginationQuery paginationQuery) {
		StringBuilder sql = this.generateQuery(paginationQuery,true);
		Query query = entityManager.createQuery(sql.toString());
		
		Long x = (Long)query.getSingleResult();
		return x.intValue();
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationRequest> listConsultationRequests(ConsultationQuery consultationQuery) {
		StringBuilder sql = this.generateQuery(consultationQuery,false);
		Query query = entityManager.createQuery(sql.toString());
		query.setFirstResult(consultationQuery.getStart());
		query.setMaxResults(consultationQuery.getLimit());
		return query.getResultList();
	}

	private StringBuilder generateQuery(PaginationQuery paginationQuery, boolean selectCountOnly) {
		ConsultationQuery consultationQuery = (ConsultationQuery) paginationQuery;
		StringBuilder sql = new StringBuilder(
				"select "+ (selectCountOnly?"count(*)":"cr") +
				" from ConsultationRequest cr left outer join cr.professionalSpecialist specialist, ConsultationServices cs, Demographic d" + 
				" left outer join d.provider p where d.DemographicNo = cr.demographicId and cs.id = cr.serviceId ");
		if (StringUtils.isNotBlank(consultationQuery.getProviderNo())) {
			sql.append("and cr.providerNo = '" + StringEscapeUtils.escapeSql(consultationQuery.getProviderNo()) + "' ");
		}
		if (!StringUtils.equals(consultationQuery.getComplete(), "true")) {
			sql.append("and cr.status != 4 ");
		}
		if (StringUtils.isNotBlank(consultationQuery.getStatus())) {
			sql.append("and cr.status = '" + StringEscapeUtils.escapeSql(consultationQuery.getStatus()) + "' ");
		}
		if (StringUtils.isNotBlank(consultationQuery.getTeam())) {
			sql.append("and cr.sendTo = '" + StringEscapeUtils.escapeSql(consultationQuery.getTeam()) + "' ");
		}
		if (StringUtils.isNotBlank(consultationQuery.getKeyword())) {
			String escapedKeyword = "%" + StringEscapeUtils.escapeSql(consultationQuery.getKeyword()) +"%";
			sql.append("and (");
			sql.append("d.LastName like '"+escapedKeyword+"'");
			sql.append("or d.FirstName like '"+escapedKeyword+"'");
			sql.append("or specialist.lastName like '"+escapedKeyword+"'");
			sql.append("or specialist.firstName like '"+escapedKeyword+"'");
			sql.append("or cs.serviceDesc like '"+escapedKeyword+"'");
			sql.append(") ");
		}
		if (consultationQuery != null) {
			if (StringUtils.equals("true", consultationQuery.getWithOption())) {
				String dateType = consultationQuery.getDateType();
				Date startDate = consultationQuery.getStartDate();
				Date endDate = consultationQuery.getEndDate();

				if (startDate != null) {
					if (StringUtils.equals("appointmentDate", dateType)) {
						sql.append("and cr.appointmentDate >= '" + DateFormatUtils.ISO_DATETIME_FORMAT.format(startDate) + "' ");
					} else {
						sql.append("and cr.referralDate >= '" + DateFormatUtils.ISO_DATETIME_FORMAT.format(startDate) + "' ");
					}
				}

				if (endDate != null) {
					if (StringUtils.equals("appointmentDate", dateType)) {
						sql.append("and cr.appointmentDate <= '" + DateFormatUtils.ISO_DATETIME_FORMAT.format(endDate) + "' ");
					} else {
						sql.append("and cr.referralDate <= '" + DateFormatUtils.ISO_DATETIME_FORMAT.format(endDate) + "' ");
					}
				}
			}
			String sort = StringEscapeUtils.escapeSql(consultationQuery.getSort());
			String orderby = StringEscapeUtils.escapeSql(consultationQuery.getOrderby());
			if (StringUtils.isBlank(orderby) || "null".equals(orderby)) {
				sql.append("order by cr.referralDate desc ");
			} else if (orderby.equals("serviceDesc")) {
				sql.append(" order by cs." + orderby + " " + sort);
			} else if (orderby.equals("patient")) {
				sql.append(" order by d.LastName " + sort);
			} else if (orderby.equals("providerName")) {
				sql.append(" order by p.LastName " + sort);
			} else if (orderby.equals("specialistName")) {
				sql.append(" order by specialist.lastName " + sort);
			} else if (orderby.equals("appointmentDate")) {
				sql.append(" order by cr.appointmentDate " + sort + ", cr.appointmentTime " + sort);
			}  else {
				sql.append(" order by cr." + orderby + " " + sort);
			}
		}
		return sql;
	}
}
