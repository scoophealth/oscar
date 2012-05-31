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
import java.util.List;
import javax.persistence.Query;
import org.oscarehr.common.model.BillingONEAReport;
import org.springframework.stereotype.Repository;

/**
 *
 * @author mweston4
 */
@Repository
public class BillingONEAReportDao extends AbstractDao<BillingONEAReport> {
    
    public BillingONEAReportDao() {
        super(BillingONEAReport.class);	
    }
    
    public List<String> getBillingErrorList(Integer billingNo) {
        List<String> errors = new ArrayList<String>();
        
        Query query = entityManager.createQuery("select eaRpt from BillingONEAReport eaRpt where eaRpt.billingNo = (:billingNo) order by processDate desc");
        query.setParameter("billingNo", billingNo);

        @SuppressWarnings("unchecked")
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
    
}
