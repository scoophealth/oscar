<!--  
/*
 * 
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
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%@ include file="../admin/dbconnection.jsp" %>
<%
  //operation available to the client -- dboperation
  String [][] dbQueries=new String[][] {
    {"search_provider", "select provider_no, first_name, last_name from provider where  provider_type=? order by ?" }, 
//    {"search_provider", "select provider_no, first_name, last_name from provider where  provider_type='doctor' order by ?" }, 

    {"search_rschedule_current", "select * from rschedule where provider_no=? and available=? and sdate <=? order by sdate desc limit 0, 1 " }, 
    {"search_rschedule_current1", "select * from rschedule where provider_no=? and sdate <=? order by sdate desc limit 0, 1 " }, 
    {"search_rschedule_future", "select * from rschedule where provider_no=? and available=? and sdate >? order by sdate" }, 
    {"search_rschedule_future1", "select * from rschedule where provider_no=?  and sdate >? order by sdate" }, 
    {"add_rschedule", "insert into rschedule values(?,?,?,?,?, ?,?,?)" },
    {"delete_rschedule", "delete from rschedule where provider_no=? and available=? and sdate=?" },
    {"update_rschedule", "update rschedule set edate=?, day_of_week=?, day_of_month=?, day_of_year=?, creator=? where provider_no=? and available=? and sdate=?" },
 
    {"search_scheduledate_c", "select * from scheduledate where priority='c' and provider_no=?" }, 
    {"add_scheduledate", "insert into scheduledate values(?,?,?,?,?, ?,?)" }, 
    {"delete_scheduledate", "delete from scheduledate where sdate=? and provider_no=? " }, // and priority=? 
    {"delete_scheduledate_b", "delete from scheduledate where provider_no=? and priority='b' and sdate>=? and sdate<=?" }, 
    {"update_scheduledate", "update scheduledate set available=?, reason=?, hour=?, creator=? where sdate=? and provider_no=? and priority=?" }, 

    {"search_scheduleholiday", "select * from scheduleholiday where sdate like ?" }, 
    {"add_scheduleholiday", "insert into scheduleholiday values(?,?)" }, 
    {"delete_scheduleholiday", "delete from scheduleholiday where sdate=?" }, 
    {"update_scheduleholiday", "update scheduleholiday set holiday_name=? where sdate=?" }, 
    
    {"add_scheduletemplatecode", "insert into scheduletemplatecode values(?,?,?,?)" }, 
    {"search_scheduletemplatecode", "select * from scheduletemplatecode order by ?" }, 
    {"search_scheduletemplatecodesingle", "select * from scheduletemplatecode where code like binary ?" }, 
    {"delete_scheduletemplatecode", "delete from scheduletemplatecode where code like binary ?" }, 
    
    {"add_scheduletemplate", "insert into scheduletemplate values(?,?,?,?)" }, 
    {"search_scheduletemplate", "select * from scheduletemplate where provider_no=? order by ?" }, 
    {"search_scheduletemplatesingle", "select * from scheduletemplate where provider_no=? and name= ?" }, 
    {"delete_scheduletemplate", "delete from scheduletemplate where provider_no=? and name=?" }, 
  };
   
  //associate each operation with an output JSP file -- displaymode
  String[][] responseTargets=new String[][] {  };
  scheduleMainBean.doConfigure(dbParams,dbQueries,responseTargets);
%>