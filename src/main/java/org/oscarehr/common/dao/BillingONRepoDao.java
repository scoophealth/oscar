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

import org.oscarehr.common.model.BillingONRepo;
import org.oscarehr.common.model.BillingONItem;
import org.springframework.stereotype.Repository;

import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.util.MiscUtils;
import oscar.util.DateUtils;
import java.util.Locale;
import java.util.Date;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

/**
 *
 * @author mweston4
 */
@Repository
public class BillingONRepoDao extends AbstractDao<BillingONRepo>{
    
    public BillingONRepoDao() {
        super(BillingONRepo.class);        
    }
    
    public void createBillingONItemEntry(BillingONItem bItem, Locale locale) {
        
        BillingONRepo billRepo = new BillingONRepo();
  
        StringBuilder content = new StringBuilder();
        content.append(bItem.getTranscId()).append("|")
               .append(bItem.getRecId()).append("|")
               .append(bItem.getServiceCode()).append("|")
               .append(bItem.getFee()).append("|")
               .append(bItem.getServiceCount()).append("|")
               .append(bItem.getServiceDate()).append("|")
               .append(bItem.getDx()).append("|")
               .append(bItem.getDx1()).append("|")
               .append(bItem.getDx2()).append("|")
               .append(bItem.getStatus()).append("|");
				        
        billRepo.sethId(bItem.getId());
        billRepo.setCategory("billing_on_item");
        billRepo.setContent(content.toString());
        billRepo.setCreateDateTime(bItem.getLastEditDT());
        
        this.persist(billRepo);

        MiscUtils.getLogger().info("createBillingONItemEntry(old value = " 
                                + billRepo.getId()
                                + "|" + bItem.getStatus() 
                                + "|" + bItem.getFee() 
                                + "|" + bItem.getServiceCount() 
                                + "|" + bItem.getServiceCode());
    }
    
    public void createBillingONCHeader1Entry(BillingONCHeader1 bCh1, Locale locale) {
        
        BillingONRepo billRepo = new BillingONRepo();
        
        String admissionDateStr = "Invalid Date";
        String billingDateStr = "Invalid Date";
        String billingTimeStr = "Invalid Time";
        try {
            admissionDateStr = DateUtils.formatDate(bCh1.getAdmissionDate(), locale);
            billingDateStr = DateUtils.formatDate(bCh1.getBillingDate(), locale);
            billingTimeStr = DateUtils.formatDate(bCh1.getBillingTime(), locale);
           
        } catch (java.text.ParseException e) {
            MiscUtils.getLogger().warn("Invalid Date or Time",e);
        }
        
        ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
        Provider provider = providerDao.getProvider(bCh1.getProviderNo());
                        
        StringBuilder content = new StringBuilder();
        content.append(bCh1.getTranscId()).append("|" )
               .append(bCh1.getRecId()).append("|" )
               .append(bCh1.getHin()).append("|" )
               .append(bCh1.getVer()).append("|" )
               .append(bCh1.getDob()).append("|" )
               .append(bCh1.getPayProgram()).append("|" )
               .append(bCh1.getPayee()).append("|" )
               .append(bCh1.getRefNum()).append("|" )
               .append(bCh1.getFaciltyNum()).append("|" )
               .append(admissionDateStr).append("|" )
               .append(bCh1.getRefLabNum()).append("|" )
               .append(bCh1.getManReview()).append("|" )
               .append(bCh1.getLocation()).append("|" )
               .append(bCh1.getDemographicNo()).append("|" )
               .append(bCh1.getProviderNo()).append("|" )
               .append(bCh1.getAppointmentNo()).append("|" )
               .append(bCh1.getDemographicName()).append("|" )
               .append(bCh1.getSex()).append("|" )
               .append(bCh1.getProvince()).append("|" )
               .append(billingDateStr).append("|" )
               .append(billingTimeStr).append("|" )
               .append(bCh1.getTotal()).append("|" )
               .append(bCh1.getPaid()).append("|" )
               .append(bCh1.getStatus()).append("|" )
               .append(bCh1.getComment()).append("|" )
               .append(bCh1.getVisitType()).append("|" )
               .append(provider.getOhipNo()).append("|" )
               .append(bCh1.getApptProviderNo()).append("|" )
               .append(bCh1.getAsstProviderNo()).append("|" ) 
               .append(bCh1.getCreator()).append("|" ) 
               .append(bCh1.getClinic());                     

        billRepo.sethId(bCh1.getId());
        billRepo.setCategory("billing_on_cheader1");
        billRepo.setContent(content.toString());
        billRepo.setCreateDateTime(new Date());

        this.persist(billRepo);

        MiscUtils.getLogger().info("createBillingONCHeader1Entry(old value = " 
                                + billRepo.getId()
                                + "|" + bCh1.getStatus() 
                                + "|" + bCh1.getRefNum() 
                                + "|" + admissionDateStr
                                + "|" + bCh1.getFaciltyNum() 
                                + "|" + bCh1.getManReview()                            
                                + "|" + billingDateStr
                                + "|" + bCh1.getProviderNo() 
                                + "|" + bCh1.getCreator());
    } 
}
