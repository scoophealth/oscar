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


package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import org.apache.struts.action.ActionForm;
import org.oscarehr.common.dao.ConsultationRequestDao;

public final class EctViewConsultationRequestsForm extends ActionForm {
   
   public String getSendTo() {

      return sendTo;
   }
   
   public void setSendTo(String str) {

      sendTo = str;
   }
   
   public String getCurrentTeam() {

      if(currentTeam == null)
         currentTeam = new String();
      return currentTeam;
   }
   
   public void setCurrentTeam(String str) {

      currentTeam = str;
   }
   
   /**
    * Getter for property startDate.
    * @return Value of property startDate.
    */
   public java.lang.String getStartDate() {
      return startDate;
   }
   
   /**
    * Setter for property startDate.
    * @param startDate New value of property startDate.
    */
   public void setStartDate(java.lang.String startDate) {
      this.startDate = startDate;
   }
   
   /**
    * Getter for property endDate.
    * @return Value of property endDate.
    */
   public java.lang.String getEndDate() {
      return endDate;
   }
   
   /**
    * Setter for property endDate.
    * @param endDate New value of property endDate.
    */
   public void setEndDate(java.lang.String endDate) {
      this.endDate = endDate;
   }
   
   /**
    * Getter for property includeCompleted.
    * @return Value of property includeCompleted.
    */
   public java.lang.String getIncludeCompleted() {
      return includeCompleted;
   }
   
   /**
    * Setter for property includeCompleted.
    * @param includeCompleted New value of property includeCompleted.
    */
   public void setIncludeCompleted(java.lang.String includeCompleted) {
      this.includeCompleted = includeCompleted;
   }
   
   /**
    * Getter for property orderby.
    * @return Value of property orderby.
    */
   public java.lang.String getOrderby() {
      return orderby;
   }
   
   /**
    * Setter for property orderby.
    * @param orderby New value of property orderby.
    */
   public void setOrderby(java.lang.String orderby) {
      this.orderby = orderby;
   }
   
   /**
    * Getter for property desc.
    * @return Value of property desc.
    */
   public java.lang.String getDesc() {
      return desc;
   }
   
   /**
    * Setter for property desc.
    * @param desc New value of property desc.
    */
   public void setDesc(java.lang.String desc) {
      this.desc = desc;
   }
   
   /**
    * Getter for property searchDate.
    * @return Value of property searchDate.
    */
   public java.lang.String getSearchDate() {
      if ( searchDate  == null) { searchDate ="0"; }
      return searchDate;
   }
   
   /**
    * Setter for property searchDate.
    * @param searchDate New value of property searchDate.
    */
   public void setSearchDate(java.lang.String searchDate) {
      this.searchDate = searchDate;
   }
   
	public Integer getOffset() {
		return offset;
	}
	
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	
	public Integer getLimit() {
		return limit;
	}
	
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

   String sendTo;
   String currentTeam;
   
   String startDate;
   String endDate;
   String includeCompleted;
   String orderby;
   String desc;
   String searchDate = null;
   Integer offset;
   Integer limit = ConsultationRequestDao.DEFAULT_CONSULT_REQUEST_RESULTS_LIMIT;
}
