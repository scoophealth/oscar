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
//    {"drop_reporttemp", "DROP TABLE IF EXISTS ?" }, 
    {"search_provider", "select provider_no, last_name, first_name from provider where provider_type='doctor' order by ?"}, 
    {"search_group", "select * from mygroup group by mygroup_no order by ?"}, 
 
    {"drop_reporttemp", "DROP TABLE ?" }, 
    {"create_reporttemp", "create table reporttemp ( edb date not null,last_name varchar(30) not null,first_name varchar(30) not null,family_doctor varchar(20) not null,address varchar(60),phone varchar(20))" }, 
    {"delete_reporttemp", "delete from reporttemp where demographic_no like ? " }, 
    {"add_reporttemp", "insert into reporttemp (demographic_no, edb,demo_name,provider_no,address) values(?,?,?,?,?)" },
    {"search_reporttemp", "select * from reporttemp where edb >= ? order by ? desc limit ?, ?" }, 

    {"search_form_aredb", "select * from form where demographic_no = ? and form_name like ? order by form_date desc, form_time desc limit 0, 1" }, 
    {"search_form_demo", "select demographic_no from form group by ?"},

    {"search_demo_active", "select * from demographic where end_date = \"0000-00-00\" order by ? limit ?, ?"},
  };
  
  //associate each operation with an output JSP file -- displaymode
  String[][] responseTargets=new String[][] {  };
  reportMainBean.doConfigure(dbParams,dbQueries,responseTargets);
%> 
