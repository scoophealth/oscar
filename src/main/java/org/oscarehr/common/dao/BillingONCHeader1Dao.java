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

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.model.BillingONCHeader1;
import org.springframework.stereotype.Repository;

/**
*
* @author Eugene Katyukhin
*/

@Repository
public class BillingONCHeader1Dao extends AbstractDao<BillingONCHeader1>{

    public BillingONCHeader1Dao() {
        super(BillingONCHeader1.class);
    }

    public int getNumberOfDemographicsWithInvoicesForProvider(String providerNo,Date startDate,Date endDate,boolean distinct ){
        String distinctStr = "distinct";
        if (distinct == false){
                distinctStr = StringUtils.EMPTY;
        }

        Query query = entityManager.createNativeQuery("select count("+distinctStr+" demographic_no) from billing_on_cheader1 ch where ch.provider_no = ? and billing_date >= ? and billing_date <= ?");
        query.setParameter(1, providerNo);
                query.setParameter(2,startDate);
                query.setParameter(3,endDate);
                BigInteger bint =  (BigInteger) query.getSingleResult();
                return bint.intValue();
    }

}
