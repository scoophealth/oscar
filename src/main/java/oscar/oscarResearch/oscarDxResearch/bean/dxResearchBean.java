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


package oscar.oscarResearch.oscarDxResearch.bean;

import org.oscarehr.common.dao.PartialDateDao;
import org.oscarehr.common.model.PartialDate;
import org.oscarehr.util.SpringUtils;


public class dxResearchBean{
		private static final PartialDateDao partialDateDao = (PartialDateDao) SpringUtils.getBean("partialDateDao");

       String description;
       String dxResearchNo;
       String dxSearchCode;       
       String end_date;
       String start_date;      
       String status;        
       String type;
       String providerNo;
       
       
       public dxResearchBean(){
       }

       public dxResearchBean(  String description,
                               String dxResearchNo,
                               String dxSearchCode,                               
                               String end_date,
                               String start_date,
                               String status,    
                               String type,
                               String providerNo){
            this.description = description;
            this.dxResearchNo = dxResearchNo;
            this.dxSearchCode = dxSearchCode;
            this.end_date = end_date;
            this.start_date = start_date;
            this.status = status;
            this.type = type;
            this.providerNo = providerNo;
       }
      
       public String getDescription(){
           return description;
       }       
       public void setDescription(String description){
           this.description = description;
       }
       
       public String getDxResearchNo(){
           return dxResearchNo;
       }       
       public void setDxResearchNo(String dxResearchNo){
           this.dxResearchNo = dxResearchNo;
       }
       
       public String getDxSearchCode(){
           return dxSearchCode;
       }       
       public void setDxSearchCode(String dxSearchCode){
           this.dxSearchCode = dxSearchCode;
       }
       
       public String getEnd_date(){
           return end_date;
       }       
       public void setEnd_date(String end_date){
           this.end_date = end_date;
       }
       
       public String getStart_date(){
           return partialDateDao.getDatePartial(start_date, PartialDate.DXRESEARCH, Integer.valueOf(dxResearchNo), PartialDate.DXRESEARCH_STARTDATE);
       }       
       public void setStart_date(String start_date){
           this.start_date = start_date;
       }
       
       public String getStatus(){
           return status;
       }       
       public void setStatus(String status){
           this.status = status;
       }
       
       public String getType(){
           return type;
       }       
       public void setType(String type){
           this.type = type;
       }
       
       public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public boolean equals( Object o ) {
           if( o instanceof dxCodeSearchBean ) {
                dxCodeSearchBean bean = (dxCodeSearchBean)o;                
                return (dxSearchCode.equals(bean.getDxSearchCode()) && type.equals(bean.getType()));
           }
           else
               return super.equals(o);
       }
}
