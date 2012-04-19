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


public class dxQuickListBean{
       
       String quickListName;              
       String createdBy;
       String lastUsed = "";
       
       public dxQuickListBean(){
       }

       public dxQuickListBean( String quickListName, 
                               String createdBy){
            this.quickListName = quickListName;
            this.createdBy = createdBy;
       }       
       
       public dxQuickListBean( String quickListName){
            this.quickListName = quickListName;
       }     
       
       public String getQuickListName(){
           return quickListName;
       }       
       public void setQuickListName(String quickListName){
           this.quickListName = quickListName;
       }
       
       public String getCreatedBy(){
           return createdBy;
       }       
       public void setCreatedBy(String createdBy){
           this.createdBy = createdBy;
       }
       
       public String getLastUsed(){
           return lastUsed;
       }       
       public void setLastUsed(String lastUsed){
           this.lastUsed = lastUsed;
       }
       
}
