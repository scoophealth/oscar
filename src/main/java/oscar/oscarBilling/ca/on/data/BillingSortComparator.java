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


package oscar.oscarBilling.ca.on.data;

import java.util.Comparator;
import java.util.Hashtable;

/**
 *
 * @author jay
 */
public class BillingSortComparator implements Comparator {
    
    /** Creates a new instance of BillingSortComparator */
    public BillingSortComparator() {
    }
    
     public int compare(Object obj1, Object obj2) {
         Hashtable h1 = (Hashtable) obj1;
         Hashtable h2 = (Hashtable) obj2;
         String billReferenceDate = (String)h1.get("billReferenceDate");
         
         JdbcBillingReviewImpl dbObj = new JdbcBillingReviewImpl();

	 String fee1 = dbObj.getCodeFee((String) h1.get("serviceCode"), billReferenceDate);
         String fee2 = dbObj.getCodeFee((String) h2.get("serviceCode"), billReferenceDate);

	if (fee1 == null && fee2 == null){
             return 0;
        }else if(fee1 == null && fee2 != null){
             return -1;    
        }else if(fee1 != null && fee2 == null){
             return 1;    
        }else if (fee1.equals(fee2)){
             return 0;  
        }else if (fee1.equals(".00") || fee1.equals("")){
             return 1;
        }else if (fee2.equals(".00") || fee2.equals("")){
             return -1;
        }
				
         
        return 0;
      }
    
}
