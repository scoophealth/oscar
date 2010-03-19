/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * DxReference.java
 *
 * Created on January 30, 2007, 2:59 AM
 *
 */

package oscar.oscarBilling.ca.bc.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.util.SpringUtils;
import oscar.entities.BillingDxCode;

/**
 *
 * @author jay
 */
public class DxReference {
    private static final Log _log = LogFactory.getLog(DxReference.class);
    BillingDxCodeDAO dxCode = (BillingDxCodeDAO) SpringUtils.getBean("BillingDxCodeDAO");

    /** Creates a new instance of DxReference */
    public DxReference() {
    }
    //select dx_code1, dx_code2, dx_code3,service_date from billingmaster order by service_date desc;
    /*
+----------+----------+----------+--------------+
| dx_code1 | dx_code2 | dx_code3 | service_date |
+----------+----------+----------+--------------+
| 250      |          |          | 20061114     |
+----------+----------+----------+--------------+
     */
    
    /*
     * method looks in a paitnest
     */
    public List getLatestDxCodes(String demo){     
       ArrayList list = new ArrayList(); 
       String nsql ="select dx_code1, dx_code2, dx_code3,service_date from billingmaster where demographic_no = ? and billingstatus != 'D' order by service_date desc";
       try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            PreparedStatement pstmt = DBHandler.getConnection().prepareStatement(nsql);
            pstmt.setString(1,demo);
            ResultSet rs = pstmt.executeQuery();
            Map m = new HashMap();
            while (rs.next()){
                String sDate = rs.getString("service_date");
                String[] dx = new String[3];
                dx[0] = rs.getString("dx_code1");
                dx[1] = rs.getString("dx_code2");
                dx[2] = rs.getString("dx_code3");
                Date sD = UtilDateUtilities.StringToDate(sDate,"yyyyMMdd") ;
                _log.debug("THIS IS THE DATE: "+sDate +" DATE PARSED "+sD);
                       
                for (int i = 0; i < dx.length; i++){
                    if (dx[i] != null && !dx[i].trim().equals("")){
                        DxCode code = new DxCode(sD,dx[i]);
                        if (!m.containsKey(dx[i])){
                            m.put(dx[i],dx[i]);
                            fillDxCodeDescrition(code);
                            list.add(code);
                        }
                    }
                }
            }
            pstmt.close();
       }catch (SQLException e) {
          e.printStackTrace();
       }
       Collections.sort(list);
       
       return list;
    }
    
    private void fillDxCodeDescrition(DxCode code){
         
         List<BillingDxCode> dxCodeList = dxCode.getByDxCode(code.getDx());
         BillingDxCode bdc = dxCodeList.get(0);
         code.setDesc(bdc.getDescription());
    }
    

    public class DxCode implements Comparable{
        
        public DxCode(Date d, String dx){
            this.setDx(dx);
            this.setDate(d);
        }
        
        private String dx = null;
        private Date date = null;
        private String desc = null;

        public String getDx() {
            return dx;
        }

        public void setDx(String dx) {
            this.dx = dx;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public void setDesc(String desc){
            this.desc = desc;
        }

        public String getDesc(){
            return this.desc;
        }
        
        
        public int getNumMonthSinceDate(){
            return getNumMonths(date,Calendar.getInstance().getTime());
        }
        public int getNumMonthsSinceDate(Date d){
            return getNumMonths(date,d); 
        }   
    
        private int getNumMonths(Date dStart, Date dEnd) {
            int i = 0;
            try{
                _log.debug("Getting the number of months between "+dStart.toString()+ " and "+dEnd.toString() );
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dStart);
                while (calendar.getTime().before(dEnd) || calendar.getTime().equals(dEnd)) {
                    calendar.add(Calendar.MONTH, 1);
                    i++;
                }
                i--;
                if (i < 0) { i = 0; }
            }catch (Exception e){
                _log.warn("Date was NULL in DxReference");
            }
            return i;
        }

        public int compareTo(Object o) {
            Date d = ((DxReference.DxCode) o).getDate();
            if (d == null && date == null) return 0;
            if (d == null && date != null) return -1;
            if (d != null && date == null) return 1;
            
            if (date.after(d)){
                return 1;
            }else if (date.before(d)){
                return -1;
            }
            return 0;
        }
    }
    
    
}
