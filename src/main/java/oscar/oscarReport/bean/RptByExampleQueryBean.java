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


package oscar.oscarReport.bean;

import org.apache.commons.lang.StringEscapeUtils;
import org.oscarehr.util.MiscUtils;

public class RptByExampleQueryBean{

       int id;
       String providerNo;  
       String providerLastName;
       String providerFirstName;
       String query;
       String queryWithEscapeChar;
       String queryName;
       String date;       
       
       public RptByExampleQueryBean(){
       }

       public RptByExampleQueryBean(int id, String query, String queryName){   
            this.id = id;
            this.query = query;
            this.queryName = queryName;
            this.queryWithEscapeChar = StringEscapeUtils.escapeJavaScript(query);
            MiscUtils.getLogger().debug("query with javascript escape char: " + queryWithEscapeChar);
       }
      
       public RptByExampleQueryBean(String providerLastName, String providerFirstName, String query, String date){            
            this.providerLastName = providerLastName;
            this.providerFirstName = providerFirstName;
            this.query = query;
            this.date = date;
            this.queryWithEscapeChar = StringEscapeUtils.escapeJavaScript(query);
       }
       
       public int getId(){
           return id;
       }
       
       public void setId(int id){
           this.id = id;
       }
       
       public String getProviderNo(){
           return providerNo;
       }
       
       public void setProviderNo(String providerNo){
           this.providerNo = providerNo;
       }
       
       public String getProviderLastName(){
           return providerLastName;
       }
       
       public void setProviderLastName(String providerLastName){
           this.providerLastName = providerLastName;
       }
       
       public String getProviderFirstName(){
           return providerFirstName;
       }
       
       public void setProviderFirstName(String providerFirstName){
           this.providerFirstName = providerFirstName;
       }
       
       public String getQuery(){
           return query;
       }
       
       public void setQuery(String query){
           this.query = query;
       }
       
       public String getQueryName(){
           return queryName;
       }
       
       public void setQueryName(String queryName){
           this.queryName = queryName;
       }
       
       public String getQueryWithEscapeChar(){
           return queryWithEscapeChar;
       }
       
       public void setQueryWithEscapeChar(String queryWithEscapeChar){
           this.queryWithEscapeChar = queryWithEscapeChar;
       }
       
       public String getDate(){
           return date;
       }
       
       public void setDate(String date){
           this.date = date;
       }
       
}
