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


public class dxCodeSearchBean implements java.io.Serializable{

       String description;       
       String dxSearchCode;              
       String type;
       String exactMatch;
       
       
       public dxCodeSearchBean(){
       }

       public dxCodeSearchBean(  String description,                               
                               String dxSearchCode){
            this.description = description;
            this.dxSearchCode = dxSearchCode;
       }
      
       public String getDescription(){
           return description;
       }       
       public void setDescription(String description){
           this.description = description;
       }
              
       
       public String getDxSearchCode(){
           return dxSearchCode;
       }       
       public void setDxSearchCode(String dxSearchCode){
           this.dxSearchCode = dxSearchCode;
       }
       
       public String getType(){
           return type;
       }       
       public void setType(String type){
           this.type = type;
       }
       
       public String getExactMatch(){
           return exactMatch;
       }       
       public void setExactMatch(String exactMatch){
           this.exactMatch = exactMatch;
       }
       
       public boolean equals( Object o ) {
           if( o instanceof dxResearchBean ) {
                dxResearchBean bean = (dxResearchBean)o;           
                return (dxSearchCode.equals(bean.getDxSearchCode()) && type.equals(bean.getType()));
           }
           else 
               return super.equals(o);
       }
       
}
