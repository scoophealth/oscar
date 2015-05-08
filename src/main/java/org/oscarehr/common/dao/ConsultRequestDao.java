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

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.oscarehr.common.PaginationQuery;
import org.oscarehr.common.model.ConsultationRequest;
import org.oscarehr.consultations.ConsultationQuery;
import org.oscarehr.consultations.ConsultationRequestSearchFilter;
import org.oscarehr.consultations.ConsultationRequestSearchFilter.SORTMODE;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ConsultRequestDao extends AbstractDao<ConsultationRequest> {

	public ConsultRequestDao() {
		super(ConsultationRequest.class);
	}

	public int getConsultationCount(PaginationQuery paginationQuery) {
		StringBuilder sql = this.generateQuery(paginationQuery,true);
		Query query = entityManager.createQuery(sql.toString());
		
		Long x = (Long)query.getSingleResult();
		return x.intValue();
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	public List<ConsultationRequest> listConsultationRequests(ConsultationQuery consultationQuery) {
		StringBuilder sql = this.generateQuery(consultationQuery,false);
		Query query = entityManager.createQuery(sql.toString());
		query.setFirstResult(consultationQuery.getStart());
		query.setMaxResults(consultationQuery.getLimit());
		return query.getResultList();
	}

	@Deprecated
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
	
	
	public int getConsultationCount2(ConsultationRequestSearchFilter filter) {
		String sql = getSearchQuery(filter,true);
		MiscUtils.getLogger().info("sql="+sql);
		Query query = entityManager.createQuery(sql);
		Long count = this.getCountResult(query);
		
		
		return count.intValue();
	}

	public List<Object[]> search(ConsultationRequestSearchFilter filter) {
		String sql = this.getSearchQuery(filter,false);
		MiscUtils.getLogger().info("sql="+sql);
		Query query = entityManager.createQuery(sql);
		query.setFirstResult(filter.getStartIndex());
		query.setMaxResults(filter.getNumToReturn());
		return query.getResultList();
	}
	
	private String getSearchQuery(ConsultationRequestSearchFilter filter, boolean selectCountOnly) {
		
		StringBuilder sql = new StringBuilder(
				"select "+ (selectCountOnly?"count(*)":"cr,specialist,cs,d,p") +
				" from ConsultationRequest cr left outer join cr.professionalSpecialist specialist, ConsultationServices cs, Demographic d" + 
				" left outer join d.provider p where d.DemographicNo = cr.demographicId and cs.id = cr.serviceId ");
		
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
		
		if (filter.getStatus()!=null) {
			sql.append("and cr.status = '" + filter.getStatus() + "' ");
		} else {
			sql.append("and cr.status!=4 and cr.status!=5 and cr.status!=7 ");
		}
		
		if (StringUtils.isNotBlank(filter.getTeam())) {
			sql.append("and cr.sendTo = '" + StringEscapeUtils.escapeSql(filter.getTeam()) + "' ");
		}
		
		if (StringUtils.isNotBlank(filter.getUrgency())) {
			sql.append("and cr.urgency = '" + StringEscapeUtils.escapeSql(filter.getUrgency()) + "' ");
		}
		
		if (filter.getDemographicNo() != null && filter.getDemographicNo()>0) {
			sql.append("and cr.demographicId = " + filter.getDemographicNo() + " ");
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
		  } else if(SORTMODE.Service.equals(filter.getSortMode())) {
			  orderBy = "cs.serviceDesc " + orderDir;
		  } else if(SORTMODE.Consultant.equals(filter.getSortMode())) {
			  orderBy = "specialist.lastName "+ orderDir + ",specialist.firstName " + orderDir;
		  } else if(SORTMODE.Team.equals(filter.getSortMode())) {
			  orderBy =  "cr.sendTo " + orderDir;
		  } else if(SORTMODE.Status.equals(filter.getSortMode())) {
			  orderBy =  "cr.status " + orderDir;
		  } else if(SORTMODE.MRP.equals(filter.getSortMode())) {
			  orderBy =  "p.LastName "+ orderDir+",p.FirstName " + orderDir;
		  }  else if(SORTMODE.FollowUpDate.equals(filter.getSortMode())) {
			  orderBy =  "cr.followUpDate "+ orderDir;
		  } else if(SORTMODE.ReferralDate.equals(filter.getSortMode())) {
			  orderBy =  "cr.referralDate "+ orderDir;
		  } else if(SORTMODE.Urgency.equals(filter.getSortMode())) {
			  orderBy =  "cr.urgency "+ orderDir;
		  }

		  orderBy = " ORDER BY " + orderBy;
		  
		
		  sql.append(orderBy);
		  
		return sql.toString();
	}
}
