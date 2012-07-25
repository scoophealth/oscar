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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.utility.DateUtils;
import org.oscarehr.billing.CA.ON.model.BillingPercLimit;
import org.oscarehr.billing.CA.dao.GstControlDao;
import org.oscarehr.billing.CA.model.GstControl;
import org.oscarehr.common.model.*;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;
import oscar.OscarProperties;
import oscar.oscarBilling.ca.on.data.BillingDataHlp;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
/**
*
* @author Eugene Katyukhin
*/

@Repository
public class BillingONCHeader1Dao extends AbstractDao<BillingONCHeader1>{
    
    @Autowired
    private DemographicDao demographicDao;
    @Autowired
    private ProviderDao providerDao;
    @Autowired
    private BillingServiceDao billingServiceDao;
    @Autowired
    private GstControlDao gstControlDao;
    
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
    
    public void createBills( List<BillingONCHeader1>lBills) {
        for( BillingONCHeader1 b : lBills) {
            this.persist(b);
        }
    }
    
    public String createBill(String provider, String demographic, String code, String clinicRefCode, Date serviceDate, String curUser) {
        BillingONCHeader1 header1 = null;
        Provider prov = providerDao.getProvider(provider);
        OscarProperties properties = OscarProperties.getInstance();
        ArrayList<String>codes = new ArrayList<String>();
        ArrayList<String>dxCodes = new ArrayList<String>();

        codes.add(code);
        String total = this.calcTotal(codes,serviceDate);

        header1 = this.assembleHeader1(prov, demographic, clinicRefCode, serviceDate, total, curUser, properties);
        this.addItems(header1, codes, dxCodes, serviceDate);
        this.persist(header1);
        
        return total;
    }
    
    public String createBill(String provider, String demographic, String code, String dxCode, String clinicRefCode, Date serviceDate, String curUser) {
        BillingONCHeader1 header1 = null;
        Provider prov = providerDao.getProvider(provider);
        OscarProperties properties = OscarProperties.getInstance();
        ArrayList<String>codes = new ArrayList<String>();
        ArrayList<String>dxCodes = new ArrayList<String>();        

        codes.add(code);
        dxCodes.add(dxCode);
        
        String total = this.calcTotal(codes,serviceDate);
        
        header1 = this.assembleHeader1(prov, demographic, clinicRefCode, serviceDate, total, curUser, properties);
        this.addItems(header1, codes, dxCodes, serviceDate);
        this.persist(header1);
        
        return total;
    }
    

    public String createBills(String provider, List<String>demographic_nos, List<String>codes, List<String>dxcodes, String clinicRefCode, Date serviceDate, String curUser) {
        BillingONCHeader1 header1 = null;
        Provider prov = providerDao.getProvider(provider);
        OscarProperties properties = OscarProperties.getInstance();

        String total = calcTotal(codes,serviceDate);
        for( String demographic : demographic_nos) {
            header1 = this.assembleHeader1(prov, demographic, clinicRefCode, serviceDate, total, curUser, properties);
            this.addItems(header1, codes, dxcodes, serviceDate);
            this.persist(header1);
        }
        
        return total;
    }

    private BillingONCHeader1 assembleHeader1(Provider prov, String demographic, String clinicRefCode, Date serviceDate, String total, String curUser, OscarProperties properties) {
        
        BillingONCHeader1 header1 = new BillingONCHeader1();
        header1.setTranscId(BillingDataHlp.CLAIMHEADER1_TRANSACTIONIDENTIFIER);
        header1.setRecId(BillingDataHlp.CLAIMHEADER1_REORDIDENTIFICATION);
        header1.setHeaderId(0);

        Demographic demo = demographicDao.getDemographic(demographic);

        header1.setHin(demo.getHin());
        header1.setVer(demo.getVer());
        header1.setDob(demo.getDateOfBirth());
        String payProg = demo.getHcType().equals("ON") ? "HCP" : "RMB";
        header1.setPayProgram(payProg);
        header1.setPayee(BillingDataHlp.CLAIMHEADER1_PAYEE);
        header1.setRefNum("");
        header1.setFaciltyNum(clinicRefCode);
        header1.setAdmissionDate(null);
        header1.setRefLabNum("");
        header1.setManReview("");
        header1.setLocation(properties.getProperty("clinic_no", ""));
        header1.setDemographicNo(new Integer(demographic));
        header1.setProviderNo(prov.getProviderNo());
        header1.setAppointmentNo(0);
        header1.setDemographicName(demo.getLastName() + "," + demo.getFirstName());
        header1.setSex(demo.getSex());
        header1.setProvince(demo.getHcType());
        header1.setBillingDate(serviceDate);
        header1.setBillingTime(serviceDate);
        header1.setPaid(new Long(0));
        header1.setStatus("O");
        header1.setComment("");
        header1.setVisitType("00");
        header1.setProviderOhipNo(prov.getOhipNo());
        header1.setProviderRmaNo(prov.getRmaNo());
        header1.setApptProviderNo("");
        header1.setAsstProviderNo("");
        header1.setCreator(curUser);
        BigDecimal totalValue = new BigDecimal(total);
        totalValue = totalValue.movePointRight(2);       
        header1.setTotal(new Long(totalValue.longValue()));

        return header1;
    }

    private void addItems(BillingONCHeader1 h1, List<String>codes, List<String>dxcodes, Date serviceDate) {

        BillingService billingService = null;
        BillingONItem item = null;
        for( String code : codes) {
            item = new BillingONItem();
            item.setTranscId(BillingDataHlp.ITEM_TRANSACTIONIDENTIFIER);
            item.setRecId(BillingDataHlp.ITEM_REORDIDENTIFICATION);
            item.setServiceCode(code);

            billingService = billingServiceDao.searchBillingCode(code, "ON", serviceDate);
            item.setFee(billingService.getValue());
            item.setServiceCount("1");
            item.setServiceDate(serviceDate);
            item.setStatus("O");

            if( dxcodes.size() == 1 ) {
                item.setDx(dxcodes.get(0));
                item.setDx1("");
                item.setDx2("");
            } else if( dxcodes.size() == 2 ) {
                item.setDx(dxcodes.get(0));
                item.setDx1(dxcodes.get(1));
                item.setDx2("");
            } else if( dxcodes.size() == 3 ) {
                item.setDx(dxcodes.get(0));
                item.setDx1(dxcodes.get(1));
                item.setDx2(dxcodes.get(2));
            }
            else {
                item.setDx("");
                item.setDx1("");
                item.setDx2("");
            }

            h1.getBillingItems().add(item);
        }
    }

    private String calcTotal(List<String>codes, Date serviceDate) {
        GstControl gstControl = gstControlDao.find(new Integer(1));
        BigDecimal gst;
        BigDecimal gstTotal;
        BigDecimal total = new BigDecimal(0.0);
        BigDecimal percent = new BigDecimal(0.0);
        BillingPercLimit billingPerc;
        ArrayList<BillingService> aPercentCodes = new ArrayList<BillingService>();
        BillingService billingservice = null;
        for( String code : codes ) {
            billingservice = billingServiceDao.searchBillingCode(code, "ON", serviceDate);

            if( !billingservice.getPercentage().equalsIgnoreCase("")) {
                //billingPerc = billingservice
                aPercentCodes.add(billingservice);

            }
            else {
                if( billingservice.getGstFlag() ) {
                    gst = gstControl.getGstPercent();
                    gst = gst.divide(new BigDecimal(100.0));
                    gstTotal = gst.multiply(new BigDecimal(Double.parseDouble(billingservice.getValue())));
                    total = total.add(gstTotal).setScale(2, BigDecimal.ROUND_HALF_UP);
                }

                total = total.add(new BigDecimal(billingservice.getValue()));
            }
        }

        BigDecimal percBase = total;
        BigDecimal percentCalc;
        for( BillingService percentcode : aPercentCodes ) {
            percent = new BigDecimal(Double.parseDouble(percentcode.getPercentage())).setScale(2, BigDecimal.ROUND_HALF_UP);
            percentCalc = percBase.multiply(percent).setScale(2, BigDecimal.ROUND_HALF_UP);
            billingPerc = percentcode.getBillingPercLimit();
            if( billingPerc != null ) {
                percentCalc = percentCalc.min(new BigDecimal(Double.parseDouble(billingPerc.getMax())));
                percentCalc = percentCalc.max(new BigDecimal(Double.parseDouble(billingPerc.getMin())));
            }

            total = total.add(percentCalc);
        }
        total.setScale(2, BigDecimal.ROUND_UP);
        return total.toString();
    }

    public int getDaysSinceBilled(String serviceCode, String demographicNo) {
        String sql = "select b from BillingONCHeader1 h1, BillingONItem b where b.ch1Id = h1.id and b.serviceCode = :code and" +
                " h1.demographicNo = :demo and h1.status != 'D' order by h1.billingDate desc limit 1";
        Query q = entityManager.createQuery(sql);
        q.setParameter("code", serviceCode);
        q.setParameter("demo", new Integer(demographicNo));
        List billingClaims = q.getResultList();
        int numDays = -1;

        if( billingClaims.size() > 0 ) {
            BillingONItem i = (BillingONItem)billingClaims.get(0);
            Calendar billdate = Calendar.getInstance();
            billdate.setTime(i.getServiceDate());

            long milliBilldate = billdate.getTimeInMillis();
            long milliToday = Calendar.getInstance().getTimeInMillis();
            
            numDays = DateUtils.getDifDays(milliToday, milliBilldate);
        }

        return numDays;
    }
    
    public int getDaysSincePaid(String serviceCode, String demographic_no) {
        String sql = "select b from BillingONCHeader1 h1, BillingONItem b where b.ch1Id = h1.id and b.serviceCode = :code and" +
                " h1.demographicNo = :demo and h1.status = 'S' order by h1.billingDate desc limit 1";
        Query q = entityManager.createQuery(sql);
        q.setParameter("code", serviceCode);
        q.setParameter("demo", new Integer(demographic_no));
        List billingClaims = q.getResultList();
        int numDays = -1;

        if( billingClaims.size() > 0 ) {
            BillingONItem i = (BillingONItem)billingClaims.get(0);
            Calendar billDate = Calendar.getInstance();
            billDate.setTime(i.getServiceDate());
          
            long milliBilldate = billDate.getTimeInMillis();
            long milliToday = Calendar.getInstance().getTimeInMillis();
            
            numDays = DateUtils.getDifDays(milliToday, milliBilldate);                
        }

        return numDays;
    }    
    
    @SuppressWarnings("unchecked")
    public List<BillingONCHeader1> getInvoices(String demographicNo, Integer limit) {
    	String sql = "select h1 from BillingONCHeader1 h1 where " +
                " h1.demographicNo = :demo and h1.status != 'D' order by h1.billingDate desc";
        Query q = entityManager.createQuery(sql);
        
        q.setParameter("demo", new Integer(demographicNo));
        q.setMaxResults(limit);
        
        return q.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<BillingONCHeader1> getInvoices(String demographicNo) {
    	String sql = "select h1 from BillingONCHeader1 h1 where " +
                " h1.demographicNo = :demo and h1.status != 'D' order by h1.billingDate desc";
        Query q = entityManager.createQuery(sql);
        
        q.setParameter("demo", new Integer(demographicNo));
        
        return q.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<BillingONCHeader1> getInvoicesByIds(List<Integer> ids) {
    	if(ids.isEmpty()) 
            return new ArrayList<BillingONCHeader1>();
    	
    	String sql = "select h1 from BillingONCHeader1 h1 where h1.id in (:ids)";
        Query q = entityManager.createQuery(sql);
        
        q.setParameter("ids", ids);
        
        return q.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getInvoicesMeta(String demographicNo) {
    	String sql = "select new map(h1.id as id, h1.billingDate as billing_date, h1.billingTime as billing_time, h1.providerNo as provider_no) from BillingONCHeader1 h1 where " +
                " h1.demographicNo = :demo and h1.status != 'D' order by h1.billingDate desc";
        Query q = entityManager.createQuery(sql);
        
        q.setParameter("demo", new Integer(demographicNo));
        
        return q.getResultList();
    }

    public GstControlDao getGstControlDao() {
        return gstControlDao;
    }

    public void setGstControlDao(GstControlDao gstControlDao) {
        this.gstControlDao = gstControlDao;
    }
    
     public BillingONItem findBillingONItemByServiceCode(BillingONCHeader1 ch1, String serviceCode) {
        String sql = "select b1 from BillingONItem b1 where b1.ch1Id = :billId and b1.serviceCode = :code";
        
        Query q = entityManager.createQuery(sql);
        q.setParameter("billId", ch1.getId());
        q.setParameter("code", serviceCode);
        
       BillingONItem b = null;
        
        @SuppressWarnings("unchecked")
        List<BillingONItem> results = q.getResultList();
        if (!results.isEmpty()) {
            if (results.size() > 1) {
                 MiscUtils.getLogger().warn("Duplicate service codes on same invoice. Id:" + ch1.getId() + " Service Code:" + serviceCode);
            }
            b = results.get(0);
        }
        return b;
    }
     
     public List<BillingONCHeader1> get3rdPartyInvoiceByProvider(Provider p, Date start, Date end, Locale locale) {
         String sql = "select distinct bCh1 from BillingONPayment bPay, BillingONCHeader1 bCh1 where bPay.billingNo=bCh1.id and bCh1.providerNo=? and bPay.payDate >= ? and bPay.payDate < ? order by bCh1.id";
         Query query = entityManager.createQuery(sql);        
         query.setParameter(1, p.getProviderNo());  
         query.setParameter(2, start);
         query.setParameter(3, end);
         
        @SuppressWarnings("unchecked")
        List<BillingONCHeader1> results = query.getResultList();
        
        return results;
    }
     
     public List<BillingONCHeader1> get3rdPartyInvoiceByDate(Date start, Date end, Locale locale) {
         String sql = "select distinct bCh1 from BillingONPayment bPay, BillingONCHeader1 bCh1 where bPay.billingNo=bCh1.id and bPay.payDate >= ? and bPay.payDate < ? order by bCh1.id";
         Query query = entityManager.createQuery(sql);               
         query.setParameter(1, start);
         query.setParameter(2, end);
         
        @SuppressWarnings("unchecked")
        List<BillingONCHeader1> results = query.getResultList();
        
        return results;
    }

    public BillingONCHeader1 getLastOHIPBillingDateForServiceCode (String demographicNo, String serviceCode) {            
        String sql = "select b from BillingONItem i, BillingONCHeader1 b where i.ch1Id=b.id and i.status!='D' and i.serviceCode=? and b.demographicNo=?  and (b.payProgram='HCP' or b.payProgram='RMB' or b.payProgram='WCB') and (b.status='S' or b.status='O' or b.status='B') order by b.billingDate desc";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1,serviceCode);
        query.setParameter(2,Integer.parseInt(demographicNo));
        
        List<BillingONCHeader1> results = query.getResultList();
        BillingONCHeader1 result = null;
        if (results.size() > 0) {
            result = results.get(0);            
        }
        return result;
    }
}
