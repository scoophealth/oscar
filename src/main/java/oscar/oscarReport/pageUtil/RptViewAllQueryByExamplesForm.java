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


package oscar.oscarReport.pageUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.struts.action.ActionForm;

import oscar.util.DateUtils;

public final class RptViewAllQueryByExamplesForm extends ActionForm {

    GregorianCalendar now=new GregorianCalendar(); 
    int curYear = now.get(Calendar.YEAR);
    int curMonth = (now.get(Calendar.MONTH)+1);
    int curDay = now.get(Calendar.DAY_OF_MONTH);
    
    String sql;
    String selectedRecentSearch;
    DateUtils dateUtils = new DateUtils();
    String startDate = dateUtils.NextDay(curDay, curMonth, curYear-1);
    String endDate = dateUtils.NextDay(curDay, curMonth, curYear);

    public String getSql(){
       return sql;
    }

    public void setSql(String sql){
       this.sql = sql;
    }
    
    public String getSelectedRecentSearch(){
       return selectedRecentSearch;
    }

    public void setSelectedRecentSearch(String selectedRecentSearch){
       this.selectedRecentSearch = selectedRecentSearch;
    }
    
    public String getStartDate(){
       return startDate;
    }

    public void setStartDate(String startDate){
       this.startDate = startDate;
    }
    
    public String getEndDate(){
       return endDate;
    }

    public void setEndDate(String endDate){
       this.endDate = endDate;
    }
}
