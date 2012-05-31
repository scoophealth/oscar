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
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.RaDetail;
import org.oscarehr.common.model.RaHeader;
import org.springframework.stereotype.Repository;
import org.oscarehr.common.model.BillingONItem;
import org.oscarehr.util.MiscUtils;
import java.util.Date;

@Repository
public class RaDetailDao extends AbstractDao<RaDetail>{

	public RaDetailDao() {
		super(RaDetail.class);
	}
        
        public List<RaDetail> getProviderRaDetailByRaHeader(Provider p, RaHeader raHeader) {
            Query query = entityManager.createQuery("SELECT rad from RaDetail rad, BillingONCHeader1 ch1 WHERE rad.raheaderNo = ? and ch1.id = rad.billingNo and rad.ohipNo = ? order by rah.raHeaderNo");
            query.setParameter(1, raHeader.getId());
            query.setParameter(2, p.getOhipNo());
            
            @SuppressWarnings("unchecked")
            List<RaDetail> raDetails = query.getResultList();

            return raDetails;
	} 
        public List<RaDetail> getRaDetailByBillingONItemPayDate(BillingONItem b, Date start, Date end){
            Query query = entityManager.createQuery("SELECT rad from RaDetail rad, RaHeader rah WHERE rad.raHeaderNo = rah.raHeaderNo and rad.billingNo = ? and rad.serviceCode = ? and rah.paymentDate >= ? and rah.paymentDate < ?");
            query.setParameter(1, b.getCh1Id());
            query.setParameter(2, b.getServiceCode());
            query.setParameter(3, start);
            query.setParameter(4,end);
            
            @SuppressWarnings("unchecked")
            List<RaDetail> raDetails = query.getResultList();
            if (raDetails.size() > 1) {
                MiscUtils.getLogger().warn("More than one radetail with same billing number and service code as billing item");
            } 
            
            return raDetails;
        }
        
        public List<RaDetail> getRaDetailByClaimNo(String claimNo) {
            
            Query query = entityManager.createQuery("SELECT rad from RaDetail rad where rad.claimNo = ?");
            query.setParameter(1, claimNo);
            
            @SuppressWarnings("unchecked")
            List<RaDetail> raDetails = query.getResultList();
                      
            return raDetails;
        }
        
        public List<String> getBillingExplanatoryList(Integer billingNo) {
                        
            Query query = entityManager.createQuery("SELECT errorCode from RaDetail rad where rad.billingNo = (:billingNo) and rad.errorCode!='' and rad.raHeaderNo=(select max(rad2.raHeaderNo) from RaDetail rad2 where rad2.billingNo=(:billingNo))");
            query.setParameter("billingNo", billingNo);
            
            @SuppressWarnings("unchecked")
            List<String> errors = query.getResultList();
            
            return errors;
        }
}
