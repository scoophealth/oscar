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


package org.oscarehr.billing.CA.ON.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.billing.CA.ON.model.BillingClaimHeader1;
import org.oscarehr.billing.CA.ON.model.BillingItem;
import org.oscarehr.billing.CA.ON.model.BillingPercLimit;
import org.oscarehr.billing.CA.dao.GstControlDao;
import org.oscarehr.billing.CA.model.GstControl;
import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.BillingService;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.springframework.stereotype.Repository;

import oscar.OscarProperties;
import oscar.oscarBilling.ca.on.data.BillingDataHlp;

/**
 *
 * @author rjonasz
 */
@Repository
public class BillingClaimDAO extends AbstractDao<BillingClaimHeader1> {

    private DemographicDao demoDAO;
    private ProviderDao provDAO;
    private BillingServiceDao billServiceDAO;
    private GstControlDao gstControlDao;

    public BillingClaimDAO() {
        super(BillingClaimHeader1.class);
    }

    public void setBillingServiceDao(BillingServiceDao billServiceDAO) {
        this.billServiceDAO = billServiceDAO;
    }

    public BillingServiceDao getBillingServiceDao() {
        return this.billServiceDAO;
    }

    public void setDemographicDao(DemographicDao demoDAO) {
        this.demoDAO = demoDAO;
    }

    public DemographicDao getDemographicDao() {
        return demoDAO;
    }

    public void setProviderDao(ProviderDao provDAO) {
        this.provDAO = provDAO;
    }

    public ProviderDao getProviderDao() {
        return provDAO;
    }

    public void createBills( List<BillingClaimHeader1>lBills) {
        for( BillingClaimHeader1 b : lBills) {
            this.persist(b);
        }
    }

    public void createBill( BillingClaimHeader1 b) {
        this.persist(b);
    }

    public String createBill(String provider, String demographic, String code, String clinic_ref_code, Date serviceDate, String curuser) {
        BillingClaimHeader1 header1 = null;
        Provider prov = provDAO.getProvider(provider);
        OscarProperties properties = OscarProperties.getInstance();
        ArrayList<String>codes = new ArrayList<String>();
        ArrayList<String>dxCodes = new ArrayList<String>();

        codes.add(code);
        String total = this.calcTotal(codes,serviceDate);

        header1 = this.assembleHeader1(prov, demographic, clinic_ref_code, serviceDate, total, curuser, properties);
        this.addItems(header1, codes, dxCodes, serviceDate);
        this.persist(header1);
        
        return total;
    }
    
    public String createBill(String provider, String demographic, String code, String dxCode, String clinic_ref_code, Date serviceDate, String curuser) {
        BillingClaimHeader1 header1 = null;
        Provider prov = provDAO.getProvider(provider);
        OscarProperties properties = OscarProperties.getInstance();
        ArrayList<String>codes = new ArrayList<String>();
        ArrayList<String>dxCodes = new ArrayList<String>();        

        codes.add(code);
        dxCodes.add(dxCode);
        
        String total = this.calcTotal(codes,serviceDate);

        header1 = this.assembleHeader1(prov, demographic, clinic_ref_code, serviceDate, total, curuser, properties);
        this.addItems(header1, codes, dxCodes, serviceDate);
        this.persist(header1);
        
        return total;
    }
    

    public String createBills(String provider, List<String>demographic_nos, List<String>codes, List<String>dxcodes, String clinic_ref_code, Date serviceDate, String curuser) {
        BillingClaimHeader1 header1 = null;
        Provider prov = provDAO.getProvider(provider);
        OscarProperties properties = OscarProperties.getInstance();

        String total = calcTotal(codes,serviceDate);
        for( String demographic : demographic_nos) {
            header1 = this.assembleHeader1(prov, demographic, clinic_ref_code, serviceDate, total, curuser, properties);
            this.addItems(header1, codes, dxcodes, serviceDate);
            this.persist(header1);
        }
        
        return total;
    }

    private BillingClaimHeader1 assembleHeader1(Provider prov, String demographic, String clinic_ref_code, Date serviceDate, String total, String cursuser, OscarProperties properties) {
        Demographic demo;
        BillingClaimHeader1 header1 = new BillingClaimHeader1();
        header1.setTransc_id(BillingDataHlp.CLAIMHEADER1_TRANSACTIONIDENTIFIER);
        header1.setRec_id(BillingDataHlp.CLAIMHEADER1_REORDIDENTIFICATION);
        header1.setHeader_id(0);

        demo = demoDAO.getDemographic(demographic);

        header1.setHin(demo.getHin());
        header1.setVer(demo.getVer());
        header1.setDob(demo.getDateOfBirth());
        String payProg = demo.getHcType().equals("ON") ? "HCP" : "RMB";
        header1.setPay_program(payProg);
        header1.setPayee(BillingDataHlp.CLAIMHEADER1_PAYEE);
        header1.setRef_num("");
        header1.setFacilty_num(clinic_ref_code);
        header1.setAdmission_date("");
        header1.setRef_lab_num("");
        header1.setMan_review("");
        header1.setLocation(properties.getProperty("clinic_no", ""));
        header1.setDemographic_no(new Integer(demographic));
        header1.setProvider_no(prov.getProviderNo());
        header1.setAppointment_no("0");
        header1.setDemographic_name(demo.getLastName() + "," + demo.getFirstName());
        header1.setSex(demo.getSex());
        header1.setProvince(demo.getHcType());
        header1.setBilling_date(serviceDate);
        header1.setBilling_time(serviceDate);
        header1.setPaid("");
        header1.setStatus("O");
        header1.setComment1("");
        header1.setVisittype("00");
        header1.setProvider_ohip_no(prov.getOhipNo());
        header1.setProvider_rma_no(prov.getRmaNo());
        header1.setApptProvider_no("");
        header1.setAsstProvider_no("");
        header1.setCreator(cursuser);
        header1.setTotal(total);

        return header1;
    }

    private void addItems(BillingClaimHeader1 h1, List<String>codes, List<String>dxcodes, Date serviceDate) {

        BillingService billingservice = null;
        BillingItem item = null;
        for( String code : codes) {
            item = new BillingItem();
            item.setTransc_id(BillingDataHlp.ITEM_TRANSACTIONIDENTIFIER);
            item.setRec_id(BillingDataHlp.ITEM_REORDIDENTIFICATION);
            item.setService_code(code);

            billingservice = billServiceDAO.searchBillingCode(code, "ON", serviceDate);
            item.setFee(billingservice.getValue());
            item.setSer_num("1");
            item.setService_date(serviceDate);
            item.setStatus("O");
            //item.setBillingClaimHeader1(h1);

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
        //return h1;
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
            billingservice = billServiceDAO.searchBillingCode(code, "ON", serviceDate);

            if( billingservice.getPercentage() != null && !billingservice.getPercentage().equalsIgnoreCase("")) {
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

    public int getDaysSinceBilled(String serviceCode, String demographic_no) {
        String sql = "select b from BillingClaimHeader1 h1, BillingItem b where b.ch1_id = h1.id and b.service_code = :code and" +
                " h1.demographic_no = :demo and h1.status != 'D' order by h1.billing_date desc limit 1";
        Query q = entityManager.createQuery(sql);
        q.setParameter("code", serviceCode);
        q.setParameter("demo", new Integer(demographic_no));
        List billingClaims = q.getResultList();
        int numdays = -1;

        if( billingClaims.size() > 0 ) {
            BillingItem i = (BillingItem)billingClaims.get(0);
            Calendar billdate = Calendar.getInstance();
            billdate.setTime(i.getService_date());

            long milliBilldate = billdate.getTimeInMillis();
            long milliToday = Calendar.getInstance().getTimeInMillis();
            long day = 1000*60*60*24;
            numdays = (int)((milliToday/day) - (milliBilldate/day));

        }

        return numdays;
    }
    
    public int getDaysSincePaid(String serviceCode, String demographic_no) {
        String sql = "select b from BillingClaimHeader1 h1, BillingItem b where b.ch1_id = h1.id and b.service_code = :code and" +
                " h1.demographic_no = :demo and h1.status = 'S' order by h1.billing_date desc limit 1";
        Query q = entityManager.createQuery(sql);
        q.setParameter("code", serviceCode);
        q.setParameter("demo", new Integer(demographic_no));
        List billingClaims = q.getResultList();
        int numdays = -1;

        if( billingClaims.size() > 0 ) {
            BillingItem i = (BillingItem)billingClaims.get(0);
            Calendar billdate = Calendar.getInstance();
            billdate.setTime(i.getService_date());

            long milliBilldate = billdate.getTimeInMillis();
            long milliToday = Calendar.getInstance().getTimeInMillis();
            long day = 1000*60*60*24;
            numdays = (int)((milliToday/day) - (milliBilldate/day));

        }

        return numdays;
    }    
    
    @SuppressWarnings("unchecked")
    public List<BillingClaimHeader1> getInvoices(String demographic_no, Integer limit) {
    	String sql = "select h1 from BillingClaimHeader1 h1 where " +
                " h1.demographic_no = :demo and h1.status != 'D' order by h1.billing_date desc";
        Query q = entityManager.createQuery(sql);
        
        q.setParameter("demo", new Integer(demographic_no));
        q.setMaxResults(limit);
        
        return q.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<BillingClaimHeader1> getInvoices(String demographic_no) {
    	String sql = "select h1 from BillingClaimHeader1 h1 where " +
                " h1.demographic_no = :demo and h1.status != 'D' order by h1.billing_date desc";
        Query q = entityManager.createQuery(sql);
        
        q.setParameter("demo", new Integer(demographic_no));
        
        return q.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<BillingClaimHeader1> getInvoicesByIds(List<Integer> ids) {
    	if(ids.size()==0) return new ArrayList<BillingClaimHeader1>();
    	
    	String sql = "select h1 from BillingClaimHeader1 h1 where h1.id in (:ids)";
        Query q = entityManager.createQuery(sql);
        
        q.setParameter("ids", ids);
        
        return q.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getInvoicesMeta(String demographic_no) {
    	String sql = "select new map(h1.id as id, h1.billing_date as billing_date, h1.billing_time as billing_time, h1.provider_no as provider_no) from BillingClaimHeader1 h1 where " +
                " h1.demographic_no = :demo and h1.status != 'D' order by h1.billing_date desc";
        Query q = entityManager.createQuery(sql);
        
        q.setParameter("demo", new Integer(demographic_no));
        
        return q.getResultList();
    }

    /**
     * @return the gstCtontrolDao
     */
    public GstControlDao getGstControlDao() {
        return gstControlDao;
    }

    /**
     * @param gstControlDao the gstControlDao to set
     */
    public void setGstControlDao(GstControlDao gstControlDao) {
        this.gstControlDao = gstControlDao;
    }
}
