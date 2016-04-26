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


package oscar.oscarBilling.ca.bc.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.oscarehr.billing.CA.dao.BillActivityDao;
import org.oscarehr.billing.CA.model.BillActivity;
import org.oscarehr.util.SpringUtils;

import oscar.entities.Billactivity;
import oscar.util.ConversionUtils;

/**
 *
 *
 * @author jay
 */
public class BillActivityDAO {
	
	private BillActivityDao dao = SpringUtils.getBean(BillActivityDao.class);

    
    public BillActivityDAO() {     
    }
    
    public String getMonthCode(){
        return getMonthCode(new Date());
    }
    
    public String getMonthCode(Date d){
        GregorianCalendar now=new GregorianCalendar();
        now.setTime(d);
        int curMonth = (now.get(Calendar.MONTH)+1);
        String monthCode = "";
        if      (curMonth == 1)  monthCode = "A";
        else if (curMonth == 2)  monthCode = "B";
        else if (curMonth == 3)  monthCode = "C";
        else if (curMonth == 4)  monthCode = "D";
        else if (curMonth == 5)  monthCode = "E";
        else if (curMonth == 6)  monthCode = "F";
        else if (curMonth == 7)  monthCode = "G";
        else if (curMonth == 8)  monthCode = "H";
        else if (curMonth == 9)  monthCode = "I";
        else if (curMonth == 10) monthCode = "J";
        else if (curMonth == 11) monthCode = "K";
        else if (curMonth == 12) monthCode = "L";
        return monthCode;
    }
    
    public String getNextMonthlySequence(String billinggroup_no){
        return getNextMonthlySequence(new Date(),billinggroup_no);
    }
    
    public String getNextMonthlySequence(Date d,String billinggroup_no){
         String batchCount= "0";
         GregorianCalendar now=new GregorianCalendar();
         now.setTime(d);
         int curYear = now.get(Calendar.YEAR);
         
         Calendar beginningOfYear = Calendar.getInstance();
         beginningOfYear.set(Calendar.YEAR,curYear);
         //beginningOfYear.set(Calendar.MONTH,0);
         beginningOfYear.set(Calendar.DAY_OF_YEAR,1);
         
        List<BillActivity> bs =  dao.findCurrentByMonthCodeAndGroupNo(getMonthCode(d),billinggroup_no,beginningOfYear.getTime());
         for(BillActivity b:bs) {
        	 batchCount = String.valueOf(b.getBatchCount());
         }
         int fileCount = Integer.parseInt(batchCount) + 1;
         batchCount = String.valueOf(fileCount);    
         
        return batchCount;
    }
    

    public int saveBillactivity(String monthCode,String batchCount,String htmlFilename, String mspFilename, String providerNo, String htmlFile,String mspFile, Date date,int records, String fileTotal ){
    	BillActivity b = new BillActivity();
    	b.setMonthCode(monthCode);
    	b.setBatchCount(Integer.valueOf(batchCount));
    	b.setHtmlFilename(htmlFilename);
    	b.setOhipFilename(mspFilename);
    	b.setProviderOhipNo("");
    	b.setGroupNo("");
    	b.setCreator(providerNo);
    	b.setHtmlContext(htmlFile);
    	b.setOhipContext(mspFile);
    	b.setClaimRecord(""+records);
    	b.setUpdateDateTime(new Date());
    	b.setStatus("A");
    	b.setTotal(fileTotal);
    	
    	dao.persist(b);
    	
    	return b.getId();
    }
    
    public void setStatusToSent(Billactivity b){
    	BillActivity ba = dao.find(b.getId());
    	if(ba != null) {
    		ba.setStatus(Billactivity.SENT);
    		ba.setSentDate(new Date());
    		dao.merge(ba);
    	}
    }
    
    public List getBillactivityByYear(int year){
       String startDate = year+"-01-01";
       String endDate = year+"-12-31 23:59:59"; 
       
       List<BillActivity> bs = dao.findCurrentByDateRange(ConversionUtils.fromDateString(startDate), ConversionUtils.fromTimestampString(endDate));
       List<Billactivity> results = new ArrayList<Billactivity>();
       for(BillActivity b:bs) {
    	   Billactivity r = new Billactivity();
    	   r.setId(b.getId());
    	   r.setMonthCode(b.getMonthCode());
    	   r.setBatchcount(b.getBatchCount());
    	   r.setHtmlfilename(b.getHtmlFilename());
    	   r.setOhipfilename(b.getOhipFilename());
    	   r.setProviderohipno(b.getProviderOhipNo());
    	   r.setGroupno(b.getGroupNo());
    	   r.setCreator(b.getCreator());
    	   r.setHtmlcontext(b.getHtmlContext());
    	   r.setOhipcontext(b.getOhipContext());
    	   r.setClaimrecord(b.getClaimRecord());
    	   r.setUpdatedatetime(b.getUpdateDateTime());
    	   r.setStatus(b.getStatus());
    	   r.setTotal(b.getTotal());
    	   
    	   results.add(r);
       }
       
       return results;
    }

    public List getBillactivityByID(String id){
    	List<Billactivity> results = new ArrayList<Billactivity>();
    	
    	BillActivity b = dao.find(Integer.parseInt(id));
    	if(b != null) {
    	   	   Billactivity r = new Billactivity();
        	   r.setId(b.getId());
        	   r.setMonthCode(b.getMonthCode());
        	   r.setBatchcount(b.getBatchCount());
        	   r.setHtmlfilename(b.getHtmlFilename());
        	   r.setOhipfilename(b.getOhipFilename());
        	   r.setProviderohipno(b.getProviderOhipNo());
        	   r.setGroupno(b.getGroupNo());
        	   r.setCreator(b.getCreator());
        	   r.setHtmlcontext(b.getHtmlContext());
        	   r.setOhipcontext(b.getOhipContext());
        	   r.setClaimrecord(b.getClaimRecord());
        	   r.setUpdatedatetime(b.getUpdateDateTime());
        	   r.setStatus(b.getStatus());
        	   r.setTotal(b.getTotal());
        	   
        	   results.add(r);   		
    	}
    	
    	return results;
    }
    
}
