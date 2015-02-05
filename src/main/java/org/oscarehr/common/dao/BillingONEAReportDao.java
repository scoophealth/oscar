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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.BillingONEAReport;
import org.springframework.stereotype.Repository;

import oscar.oscarBilling.ca.on.data.BillingProviderData;
import oscar.util.ParamAppender;

@Repository
@SuppressWarnings("unchecked")
public class BillingONEAReportDao extends AbstractDao<BillingONEAReport> {
    
    public BillingONEAReportDao() {
        super(BillingONEAReport.class);	
    }
    
    public List<BillingONEAReport> findByProviderOhipNoAndGroupNoAndSpecialtyAndProcessDate(String providerOhipNo, String groupNo, String specialty, Date processDate) {
        String sql = "select b from BillingONEAReport b where b.providerOHIPNo=:providerOHIPNo and b.groupNo=:groupNo and b.specialty=:specialty and b.processDate = :processDate";
        Query query = entityManager.createQuery(sql);
        query.setParameter("providerOHIPNo", providerOhipNo);
        query.setParameter("groupNo", groupNo);
        query.setParameter("specialty", specialty);
        query.setParameter("processDate", processDate);

        List<BillingONEAReport> results = query.getResultList();
        return results;
    }


    public List<BillingONEAReport> findByProviderOhipNoAndGroupNoAndSpecialtyAndProcessDateAndBillingNo(String providerOhipNo, String groupNo, String specialty, Date processDate, Integer billingNo) {
    	String sql = "select b from BillingONEAReport b where b.providerOHIPNo=:providerOHIPNo and b.groupNo=:groupNo and b.specialty=:specialty and b.processDate = :processDate  and b.billingNo=:billingNo";
    	Query query = entityManager.createQuery(sql);
        query.setParameter("providerOHIPNo", providerOhipNo);
        query.setParameter("groupNo", groupNo);
        query.setParameter("specialty", specialty);
        query.setParameter("processDate", processDate);
  	query.setParameter("billingNo", billingNo);
        
        List<BillingONEAReport> results = query.getResultList();       
        return results;
    }
    
    
    public List<BillingONEAReport> findByBillingNo(Integer billingNo) {
    	String sql = "select b from BillingONEAReport b where b.billingNo=:billingNo order by b.processDate DESC";
    	Query query = entityManager.createQuery(sql);
        query.setParameter("billingNo", billingNo);

        
        List<BillingONEAReport> results = query.getResultList();
        
        return results;
    }
    
    public List<String> getBillingErrorList(Integer billingNo) {
        List<String> errors = new ArrayList<String>();
        
        Query query = entityManager.createQuery("select eaRpt from BillingONEAReport eaRpt where eaRpt.billingNo = (:billingNo) order by processDate desc");
        query.setParameter("billingNo", billingNo);

        
        List<BillingONEAReport> eaReports = query.getResultList();
        
        for (BillingONEAReport eaReport : eaReports) {
            String[] claimErrors = eaReport.getClaimError().split("\\s");
            for (String claimError : claimErrors) {
                if (!claimError.trim().isEmpty())
                    errors.add(claimError);
            }
            
            String[] codeErrors = eaReport.getCodeError().split("\\s");
            for (String codeError : codeErrors) {
                if (!codeError.trim().isEmpty())
                    errors.add(codeError);
            }                         
        }
		
        return errors;
    }

	public List<BillingONEAReport> findByMagic(String ohipNo, String billingGroupNo, String specialtyCode, Date fromDate, Date toDate, String reportName) {
		ParamAppender appender = getAppender("b");
		appender.and("b.providerOHIPNo = :ohipNo", "ohipNo", ohipNo);
		appender.and("b.groupNo = :billingGroupNo", "billingGroupNo", billingGroupNo);
		appender.and("b.specialty = :specialtyCode", "specialtyCode", specialtyCode);
		appender.and("b.codeDate >= :fromDate", "fromDate", fromDate);
		appender.and("b.codeDate <= :toDate", "toDate", toDate);
		
		if( reportName != null && !"".equals(reportName)) {
			appender.and("b.reportName = :reportName", "reportName", reportName);
		}
		
		appender.addOrder("b.codeDate");
		
		Query query = entityManager.createQuery(appender.toString());
		appender.setParams(query);
		return query.getResultList();
    }

	public List<BillingONEAReport> findByMagic(List<BillingProviderData> list, Date fromDate, Date toDate, String reportName) {
		ParamAppender appender = getAppender("b");
		
		boolean hasProviderData = !list.isEmpty();
		if (hasProviderData) {
			ParamAppender providerSubclauseAppender = new ParamAppender();
			for (int i = 0; i < list.size(); i++) {
				ParamAppender providerAppender = new ParamAppender();
				
				BillingProviderData d  = list.get(i);
				ParamAppender pa = new ParamAppender();
				pa.and("b.providerOHIPNo = :ohipNo" + i, "ohipNo" + i, d.getOhipNo());
				pa.and("b.groupNo = :billingGroupNo" + i, "billingGroupNo" + i, d.getBillingGroupNo());
				pa.and("b.specialty = :specialtyCode" + i, "specialtyCode" + i, d.getSpecialtyCode());
				
				providerSubclauseAppender.or(providerAppender);
			}
			appender.and(providerSubclauseAppender);
		}
		
		appender.and("b.codeDate >= :fromDate", "fromDate", fromDate);
		appender.and("b.codeDate <= :toDate", "toDate", toDate);
		if( !"".equals(reportName) ) {
			appender.and("b.reportName = :reportName", "reportName", reportName);
		}
		appender.addOrder("b.codeDate");

		Query query = entityManager.createQuery(appender.toString());
		appender.setParams(query);
		return query.getResultList();

    }
    
}
