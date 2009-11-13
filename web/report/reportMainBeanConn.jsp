<%--  
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
 * Date         Implemented By  Company                 Comments
 * 29-09-2004   Ivy Chan        iConcept Technologies   added search_waiting_list query
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
--%>

<%@ include file="../admin/dbconnection.jsp"%>
<%
  String [][] dbQueries=new String[][] {
    {"search_provider", "select provider_no, last_name, first_name from provider where provider_type='doctor' and status='1' order by last_name"}, 
    {"search_group", "select mygroup_no from mygroup group by mygroup_no order by mygroup_no"}, 
    {"drop_reporttemp", "DROP TABLE ?" }, 
    {"create_reporttemp", "create table reporttemp ( edb date not null,last_name varchar(30) not null,first_name varchar(30) not null,family_doctor varchar(20) not null,address varchar(60),phone varchar(20))" }, 
    {"delete_reporttemp", "delete from reporttemp where demographic_no like ? " }, 
    {"add_reporttemp", "insert into reporttemp (demographic_no, edb,demo_name,provider_no,address) values(?,?,?,?,?)" },
    {"search_reporttemp", "select * from reporttemp where edb >= ? order by edb desc limit ? offset ?" }, 

	{"search_form_aredb", "select * from form where demographic_no = ? and form_name like ? order by form_date desc, form_time desc limit 1 offset 0" }, 
    {"search_form_demo", "select demographic_no from form group by demographic_no"},

    {"search_demo_active", "select * from demographic where end_date = '0001-01-01' order by last_name limit ? offset ?"},
    {"search_waiting_list", "select * from waitingListName where group_no='" + session.getAttribute("groupno") + "' and is_history='N' " },
  };

  reportMainBean.doConfigure(dbQueries);
%>
