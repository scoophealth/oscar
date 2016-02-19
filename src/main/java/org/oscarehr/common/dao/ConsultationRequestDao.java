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

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.time.DateFormatUtils;
import org.oscarehr.common.NativeSql;
import org.oscarehr.common.model.ConsultationRequest;
import org.oscarehr.util.MiscUtils;

@SuppressWarnings("unchecked")
public class ConsultationRequestDao extends AbstractDao<ConsultationRequest> {
	
	public static final int DEFAULT_CONSULT_REQUEST_RESULTS_LIMIT = 100;

	public ConsultationRequestDao() {
		super(ConsultationRequest.class);
	}

	public int getCountReferralsAfterCutOffDateAndNotCompleted(Date referralDateCutoff)
	{
		Query query = entityManager.createNativeQuery("select count(*) from consultationRequests where referalDate < ?1 and status != 4");
		query.setParameter(1, referralDateCutoff);

		return((BigInteger)query.getSingleResult()).intValue();
	}

	public int getCountReferralsAfterCutOffDateAndNotCompleted(Date referralDateCutoff,String sendto)
	{
		Query query = entityManager.createNativeQuery("select count(*) from consultationRequests where referalDate < ?1 and status != 4 and sendto = ?2");
		query.setParameter(1, referralDateCutoff);
		query.setParameter(2, sendto);

		return((BigInteger)query.getSingleResult()).intValue();
	}

        public List<ConsultationRequest> getConsults(Integer demoNo) {
            StringBuilder sql = new StringBuilder("select cr from ConsultationRequest cr, Demographic d, Provider p where d.DemographicNo = cr.demographicId and p.ProviderNo = cr.providerNo and cr.demographicId = ?1");
            Query query = entityManager.createQuery(sql.toString());
            query.setParameter(1, demoNo);
            
            List<ConsultationRequest> results = query.getResultList();
            return results;
        }

        
        public List<ConsultationRequest> getConsults(String team, boolean showCompleted, Date startDate, Date endDate, String orderby, String desc, String searchDate, Integer offset, Integer limit) {
        	MiscUtils.getLogger().error("getConsults(bunch)-----------------------------------------------"); 
        	
        	StringBuilder sql = new StringBuilder("select cr from ConsultationRequest cr left outer join cr.professionalSpecialist specialist, ConsultationServices service, Demographic d left outer join d.provider p where d.DemographicNo = cr.demographicId and service.id = cr.serviceId ");

            if( !showCompleted ) {
               sql.append("and cr.status != 4 ");
            }

            if( !team.isEmpty()) {
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
            String service = ", service.serviceDesc";
            if (orderby == null){
                sql.append("order by cr.referralDate desc ");
            }else if(orderby.equals("1")){               //1 = msgStatus
                sql.append("order by cr.status " + orderDesc + service);
             }else if(orderby.equals("2")){               //2 = msgTeam
                sql.append("order by cr.sendTo " + orderDesc + service);
            }else if(orderby.equals("3")){               //3 = msgPatient
                sql.append("order by d.LastName " + orderDesc + service);
            }else if(orderby.equals("4")){               //4 = msgProvider
                sql.append("order by p.LastName " + orderDesc + service);
            }else if(orderby.equals("5")){               //5 = msgService Desc
                sql.append("order by service.serviceDesc " + orderDesc);
            }else if(orderby.equals("6")){               //6 = msgSpecialist Name
                sql.append("order by specialist.lastName " + orderDesc + service);
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
            query.setFirstResult(offset!=null?offset:0);
            
            //need to never send more than MAX_LIST_RETURN_SIZE
            int myLimit = limit!=null?limit:DEFAULT_CONSULT_REQUEST_RESULTS_LIMIT;
            query.setMaxResults(Math.min(myLimit, MAX_LIST_RETURN_SIZE));
            
            return query.getResultList();
        }


        public List<ConsultationRequest> getConsultationsByStatus(Integer demographicNo, String status) {
        	Query query = entityManager.createQuery("SELECT c FROM ConsultationRequest c where c.demographicId = ? and c.status = ?");
        	query.setParameter(1,demographicNo);
        	query.setParameter(2,status);

        	
            List<ConsultationRequest> results = query.getResultList();
        	return results;
        }

        public ConsultationRequest getConsultation(Integer requestId) {
            return this.find(requestId);
        }

		
        public List<ConsultationRequest> getReferrals(String providerId, Date cutoffDate) {
			Query query = createQuery("cr", "cr.referralDate <= :cutoff AND cr.status = '1' and cr.providerNo = :providerNo");
			query.setParameter("cutoff", cutoffDate);
			query.setParameter("providerNo", providerId);
			return query.getResultList();
        }

		public List<Object[]> findRequests(Date timeLimit, String providerNo) {
			StringBuilder sql = new StringBuilder("SELECT DISTINCT d.LastName, c.demographicId FROM ConsultationRequest c, Demographic d " +
					"WHERE c.referralDate >= :timeLimit " +
					"AND c.demographicId = d.DemographicNo");
            if (providerNo != null){
               sql.append(" AND d.ProviderNo = :providerNo ");
            }
            sql.append(" ORDER BY d.LastName");
            
			Query query = entityManager.createQuery(sql.toString());
			query.setParameter("timeLimit", timeLimit);
			if (providerNo != null) {
				query.setParameter("providerNo", providerNo);
			}
			return query.getResultList();
        }

		public List<ConsultationRequest> findRequestsByDemoNo(Integer demoId, Date cutoffDate) {
	        Query query = createQuery("cr", "cr.referralDate <= :cutoff AND cr.demographicId = :demoId");
			query.setParameter("cutoff", cutoffDate);
			query.setParameter("demoId", demoId);
			return query.getResultList();
        }
		
		public List<ConsultationRequest> findByDemographicAndService(Integer demographicNo, String serviceName) {
			String sql = "SELECT cr FROM ConsultationRequest cr, ConsultationServices cs WHERE cr.serviceId = cs.serviceId and cr.demographicId = :demo and cs.serviceDesc = :serviceName";
			Query query = entityManager.createQuery(sql);
			query.setParameter("demo", demographicNo);
			query.setParameter("serviceName", serviceName);
			
			return query.getResultList();
		}
		
		public List<ConsultationRequest> findByDemographicAndServices(Integer demographicNo, List<String> serviceNameList) {
			String sql = "SELECT cr FROM ConsultationRequest cr, ConsultationServices cs WHERE cr.serviceId = cs.serviceId and cr.demographicId = :demo and cs.serviceDesc IN (:serviceName)";
			Query query = entityManager.createQuery(sql);
			query.setParameter("demo", demographicNo);
			query.setParameter("serviceName", serviceNameList);
			
			return query.getResultList();
		}
		
		@NativeSql("consultationRequests")
		public List<Integer> findNewConsultationsSinceDemoKey(String keyName) {
			
			String sql = "select distinct dr.demographicNo from consultationRequests dr,demographic d,demographicExt e where dr.demographicNo = d.demographic_no and d.demographic_no = e.demographic_no and e.key_val=? and dr.lastUpdateDate > e.value";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1,keyName);
			return query.getResultList();
		}
		
		
}
