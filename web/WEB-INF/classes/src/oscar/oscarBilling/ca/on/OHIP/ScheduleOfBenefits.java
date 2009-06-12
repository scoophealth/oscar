/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of ScheduleOfBenefits
 *
 *
 * ScheduleOfBenefits.java
 *
 * Created on September 21, 2005, 2:49 PM
 */

package oscar.oscarBilling.ca.on.OHIP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import oscar.oscarBilling.ca.on.data.BillingCodeData;

/**
 *
 * @author Jay Gallagher
 */
public class ScheduleOfBenefits {
   
   
   public ScheduleOfBenefits() {
   }
   
   
   public List processNewFeeSchedule(InputStream is, boolean addNewCodes , boolean addChangedCodes){
      ArrayList changes = new ArrayList();      
      BillingCodeData bc = new BillingCodeData();      
      StringBuffer codesThatHaveBothGPSpec = new StringBuffer();
      
      try {
         InputStreamReader isr = new InputStreamReader(is);
         BufferedReader in = new BufferedReader(isr);
         String str;
         int total = 0;
         int newfees = 0;
         int oldfees = 0;
         while ((str = in.readLine()) != null) {
            total++;
            Hashtable newPricingInfo = breakLine(str);
            Hashtable billingInfo = bc.searchBillingCode((String) newPricingInfo.get("feeCode")+"A");
            //System.out.println("pricing info for code "+newPricingInfo.get("feeCode"));
            
            BigDecimal gpBD   = getJBD((String) newPricingInfo.get("gpFees"));
            BigDecimal specBD = getJBD((String) newPricingInfo.get("specFee"));
            BigDecimal zeroBD = new BigDecimal("0.00");
            
            if ( gpBD.compareTo(zeroBD) != 0 && specBD.compareTo(zeroBD) != 0 && gpBD.compareTo(specBD) != 0){
               codesThatHaveBothGPSpec.append(( (String) newPricingInfo.get("feeCode"))+":"+gpBD+" "+specBD+"\n");
            }
            
            String moreprices = "(gp.:"+getJBD((String) newPricingInfo.get("gpFees"))+
                                ")  (asst.:"+getJBD((String) newPricingInfo.get("assistantCompFee"))+
                                ")  (spec.:"+getJBD((String) newPricingInfo.get("specFee"))+
                                ")  (anaes:"+getJBD((String) newPricingInfo.get("anaesthetistFee"))+
                                ")  (non-a:"+getJBD((String) newPricingInfo.get("nonAnaesthetistFee"))+")";
            
            String newPrice = (String) newPricingInfo.get("gpFees");                                             
            double newDoub = (Double.parseDouble(newPrice))/10000;                                             
            BigDecimal newPriceDec = new BigDecimal(newDoub).setScale(2, BigDecimal.ROUND_HALF_UP);
            
            if( newPriceDec.compareTo(zeroBD) == 0){
               newPriceDec = getJBD((String) newPricingInfo.get("specFee"));
               if( newPriceDec.compareTo(zeroBD) == 0){
                  newPriceDec = getJBD((String) newPricingInfo.get("assistantCompFee"));
               }
            }
            
            if(billingInfo == null){
               if (addNewCodes){
                  newfees++;
                  Hashtable change = new Hashtable();
                  change.put("oldprice", "--");
                                                      
                  change.put("newprice",""+newPriceDec);
                  change.put("feeCode",newPricingInfo.get("feeCode")+"A");
                  change.put("diff","");
                  change.put("prices",moreprices);
                  change.put("description","----");
                  change.put("effectiveDate",    newPricingInfo.get("effectiveDate"));
                  change.put("terminactionDate", newPricingInfo.get("terminactionDate"));
                  changes.add(change);
               }
            }else{
               if (addChangedCodes){
                  oldfees++;                  
                  String oldPrice = (String) billingInfo.get("value");                    
                  double oldDoub = 0.00;
                  try{
                      oldDoub = Double.parseDouble(oldPrice);
                  }   catch(Exception e){
                      oldDoub = 0.00;
                  }               
                  BigDecimal oldPriceDec = new BigDecimal(oldDoub).setScale(2, BigDecimal.ROUND_HALF_UP);                 
                  BigDecimal diffPriceDec = newPriceDec.subtract(oldPriceDec);

                  if ( oldPriceDec.compareTo(newPriceDec) != 0 ){
                     Hashtable change = new Hashtable();
                     change.put("oldprice", oldPriceDec);
                     change.put("newprice",newPriceDec);
                     change.put("diff",diffPriceDec);
                     change.put("feeCode",newPricingInfo.get("feeCode")+"A");
                     change.put("numCodes",""+bc.searchNumBillingCode((String)newPricingInfo.get("feeCode")));
                     change.put("description",billingInfo.get("description"));
                     change.put("prices",moreprices);
                     change.put("effectiveDate",    newPricingInfo.get("effectiveDate"));     
                     change.put("terminactionDate", newPricingInfo.get("terminactionDate"));  
                     change.put("prices",moreprices);
                     changes.add(change);
                  }                                                             
               }
            }            
        }
        in.close();
        //System.out.println("IN Total : "+total);
        //System.out.println("new fees "+newfees+ " current fees "+oldfees);
        //System.out.println("Dual fees \n"+codesThatHaveBothGPSpec.toString());
      } catch (IOException e) {
         e.printStackTrace();
      }
      return changes;     
   }
   
   BigDecimal getBD(String s){
      double dgpFees = Double.parseDouble(s);
      BigDecimal bd = new BigDecimal(dgpFees);
      bd.setScale(2,BigDecimal.ROUND_UP);
      return bd;
   }
   
   BigDecimal getBD4digit(String s){
      double dgpFees = Double.parseDouble(s);     
      BigDecimal bd = new BigDecimal((dgpFees/10000));
      bd.setScale(2,BigDecimal.ROUND_HALF_UP);
      return bd;
   }
   
   
   BigDecimal getJBD(String s){      
      double newDoub = (Double.parseDouble(s))/10000;                                             
      return new BigDecimal(newDoub).setScale(2, BigDecimal.ROUND_HALF_UP);
   }
   
   Hashtable breakLine(String s){
      //System.out.println(s.length()+" "+s);
      Hashtable h = null;
      if ( s != null && s.length() == 75){
         String feeCode            = s.substring(0,4);
         String effectiveDate      = s.substring(4,12);
         String terminactionDate   = s.substring(12,20);
         String gpFees             = s.substring(20,31);
         String assistantCompFee   = s.substring(31,42);
         String specFee            = s.substring(42,53);
         String anaesthetistFee    = s.substring(53,64);
         String nonAnaesthetistFee = s.substring(64,75);
                           
         h = new Hashtable();
          h.put("feeCode", feeCode);            
          h.put("effectiveDate", effectiveDate);     
          h.put("terminactionDate", terminactionDate);  
          h.put("gpFees", gpFees);            
          h.put("assistantCompFee", assistantCompFee);  
          h.put("specFee",specFee);
          h.put("anaesthetistFee", anaesthetistFee);
          h.put("nonAnaesthetistFee",nonAnaesthetistFee);
         double dgpFees = Double.parseDouble(gpFees);
         BigDecimal bd = new BigDecimal(dgpFees);
         bd.setScale(2);
         System.out.println (feeCode+" "+effectiveDate+" "+terminactionDate+" "+gpFees+" "+assistantCompFee+" "+specFee+" "+anaesthetistFee+" "+nonAnaesthetistFee);
         System.out.println (feeCode+" "+effectiveDate+" "+terminactionDate+" "+getJBD(gpFees)+" "+getJBD(assistantCompFee)+" "+getJBD(specFee)+" "+getJBD(anaesthetistFee)+" "+getJBD(nonAnaesthetistFee));
         System.out.println(dgpFees+" "+(dgpFees/10000)+" "+bd.toString());
         
      }
      return h;
   }
}
