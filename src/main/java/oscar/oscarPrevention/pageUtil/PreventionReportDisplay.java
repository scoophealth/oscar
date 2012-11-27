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


package oscar.oscarPrevention.pageUtil;

import java.util.Date;

/**
 *
 * @author Jay Gallagher
 */
public class PreventionReportDisplay implements Comparable{
   
   public Integer demographicNo = null;
   public String lastDate = null;
   public int rank = 0;
   public String state = null;
   public String numMonths = null;
   public String color = null;   
   public String numShots = null;
   public String bonusStatus= null;
   public String billStatus = null;
   
   //FollowUp Data
   public Date lastFollowup = null;
   public String lastFollupProcedure =null;
   public String nextSuggestedProcedure=null;
   
   public PreventionReportDisplay() {
   }
   
   public int compareTo(Object o) {
      
      int ret = 0;
      if (this.rank < ((PreventionReportDisplay )o).rank){
         ret = -1;
      }else if ( this.rank > ((PreventionReportDisplay )o).rank){
         ret = +1;
      }
      return ret;
      // If this < o, return a negative value
      // If this = o, return 0
      // If this > o, return a positive value
   }
   
}
