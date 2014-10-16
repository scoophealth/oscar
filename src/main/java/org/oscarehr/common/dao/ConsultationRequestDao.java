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

package org.oscarehr.common.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.time.DateFormatUtils;
import org.oscarehr.common.model.ConsultationRequest;
import org.springframework.stereotype.Repository;

@Repository
public class ConsultationRequestDao extends AbstractDao<ConsultationRequest> {

	public ConsultationRequestDao() {
		super(ConsultationRequest.class);
	}

	public int getCountReferralsAfterCutOffDateAndNotCompleted(Date referralDateCutoff)
	{
		Query query = entityManager.createNativeQuery("select count(*) from consultationRequests where referalDate < ?1 and status != 4");
		query.setParameter(1, referralDateCutoff);

		return (((Number) query.getSingleResult()).intValue());
	}

	public int getCountReferralsAfterCutOffDateAndNotCompleted(Date referralDateCutoff,String sendto)
	{
		Query query = entityManager.createNativeQuery("select count(*) from consultationRequests where referalDate < ?1 and status != 4 and sendto = ?2");
		query.setParameter(1, referralDateCutoff);
		query.setParameter(2, sendto);

		return (((Number) query.getSingleResult()).intValue());
	}

        public List<ConsultationRequest> getConsults(String demoNo) {
            StringBuilder sql = new StringBuilder("select cr from ConsultationRequest cr, Demographic d, Provider p where d.DemographicNo = cr.demographicId and p.ProviderNo = cr.providerNo and cr.demographicId = ?1");
            Query query = entityManager.createQuery(sql.toString());
            query.setParameter(1, new Integer(demoNo));
            @SuppressWarnings("unchecked")
            List<ConsultationRequest> results = query.getResultList();
            return results;
        }

        public List getConsults(String team, boolean showCompleted, Date startDate, Date endDate, String orderby, String desc, String searchDate) {
            StringBuilder sql = new StringBuilder("select cr from ConsultationRequest cr left outer join cr.professionalSpecialist specialist, ConsultationServices service, Demographic d left outer join d.provider p where d.DemographicNo = cr.demographicId and service.id = cr.serviceId ");

            if( !showCompleted ) {
               sql.append("and cr.status != 4 ");
            }

            if( !team.equals("-1") ) {
                sql.append("and cr.sendTo = '" + team + "' ");
            }

            if(startDate != null){
                if (searchDate != null && searchDate.equals("1")){
                    sql.append("and cr.appointmentDate >= '" + DateFormatUtils.ISO_DATETIME_FORMAT.format(startDate)+ "' ");
                }else{
                    sql.append("and cr.referralDate >= '" + DateFormatUtils.ISO_DATETIME_FORMAT.format(startDate)+ "' ");
                }
            }

            if(endDate != null){
                if (searchDate != null && searchDate.equals("1")){
                    sql.append("and cr.appointmentDate <= '" + DateFormatUtils.ISO_DATETIME_FORMAT.format(endDate)+ "' ");
                }else{
                    sql.append("and cr.referralDate <= '" + DateFormatUtils.ISO_DATETIME_FORMAT.format(endDate)+ "' ");
                }
            }

            String orderDesc = desc != null && desc.equals("1") ? "DESC" : "";
            if (orderby == null){
                sql.append("order by cr.referralDate desc ");
            }else if(orderby.equals("1")){               //1 = msgStatus
                sql.append("order by cr.status " + orderDesc);
             }else if(orderby.equals("2")){               //2 = msgTeam
                sql.append("order by cr.sendTo " + orderDesc);
            }else if(orderby.equals("3")){               //3 = msgPatient
                sql.append("order by d.LastName " + orderDesc);
            }else if(orderby.equals("4")){               //4 = msgProvider
                sql.append("order by p.LastName " + orderDesc);
            }else if(orderby.equals("5")){               //5 = msgService Desc
                sql.append("order by service.serviceDesc " + orderDesc);
            }else if(orderby.equals("6")){               //6 = msgSpecialist Name
                sql.append("order by specialist.lastName " + orderDesc);
            }else if(orderby.equals("7")){               //7 = msgRefDate
                sql.append("order by cr.referralDate " + orderDesc);
            }else if(orderby.equals("8")){               //8 = Appointment Date
                sql.append("order by cr.appointmentDate " + orderDesc);
            }else if(orderby.equals("9")){               //9 = FollowUp Date
                sql.append("order by cr.followUpDate " + orderDesc);
            }else{
                sql.append("order by cr.referralDate desc");
            }

            Query query = entityManager.createQuery(sql.toString());

            return query.getResultList();
        }


        public List<ConsultationRequest> getConsultationsByStatus(String demographicNo, String status) {
        	Query query = entityManager.createQuery("SELECT c FROM ConsultationRequest c where c.demographicId = ? and c.status = ?");
        	query.setParameter(1,Integer.parseInt(demographicNo));
        	query.setParameter(2,status);

        	@SuppressWarnings("unchecked")
            List<ConsultationRequest> results = query.getResultList();
        	return results;
        }

        public ConsultationRequest getConsultation(Integer requestId) {
            return this.find(requestId);
        }
}
