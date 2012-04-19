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


package oscar.oscarBilling.ca.bc.MSP;

/**
+------------------+---------+------+-----+---------+----------------+
| Field            | Type    | Null | Key | Default | Extra          |
+------------------+---------+------+-----+---------+----------------+
| log_no           | int(10) |      | PRI | NULL    | auto_increment |
| claim            | blob    | YES  |     | NULL    |                |
| sequence_no      | int(10) | YES  |     | NULL    |                |
| billingmaster_no | int(10) | YES  |     | NULL    |                |
+------------------+---------+------+-----+---------+----------------+

 * @author jay
 */
public class TeleplanLog {
    private int logNo ;
    private String claim;
    private int sequenceNo;
    private int billingmasterNo;
    
    /** Creates a new instance of TeleplanLog */
    public TeleplanLog() {
    }
        
    public TeleplanLog(int sequenceNo,String claim,int billingmasterNo){
      this.setSequenceNo(sequenceNo);
      this.setClaim(claim);
      this.setBillingmasterNo(billingmasterNo);
    }

    public TeleplanLog(int sequenceNo,String value){
      this.setSequenceNo(sequenceNo);
      this.setClaim(value);
    }

    public TeleplanLog(String sequenceNo,String claim,String billingmasterNo){
      this.setSequenceNo(Integer.parseInt(sequenceNo));
      this.setClaim(claim);
      this.setBillingmasterNo(Integer.parseInt(billingmasterNo));
    }

    public TeleplanLog(String sequenceNo,String value){
      this.setSequenceNo(Integer.parseInt(sequenceNo));
      this.setClaim(value);
    }

    public int getLogNo() {
        return logNo;
    }

    public void setLogNo(int logNo) {
        this.logNo = logNo;
    }

    public String getClaim() {
        return claim;
    }

    public void setClaim(String claim) {
        this.claim = claim;
    }

    public int getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public int getBillingmasterNo() {
        return billingmasterNo;
    }

    public void setBillingmasterNo(int billingmasterNo) {
        this.billingmasterNo = billingmasterNo;
    }

}
