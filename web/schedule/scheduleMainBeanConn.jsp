<%@ include file="../admin/dbconnection.jsp" %>
<%
  //operation available to the client -- dboperation
  String [][] dbQueries=new String[][] {
    {"search_provider", "select provider_no, first_name, last_name from provider where status='1' and provider_type=? order by last_name" }, 
//    {"search_provider", "select provider_no, first_name, last_name from provider where  provider_type='doctor' order by ?" }, 

    {"search_rschedule_current", "select * from rschedule where provider_no=? and available=? and sdate <=? order by sdate desc limit 1 " }, 
    {"search_rschedule_current1", "select * from rschedule where provider_no=? and sdate <=? order by sdate desc limit 1 " }, 
    {"search_rschedule_current2", "select * from rschedule where provider_no=? and sdate >=? order by sdate limit 1 " }, 
    {"search_rschedule_future", "select * from rschedule where provider_no=? and available=? and sdate >? order by sdate" }, 
    {"search_rschedule_future1", "select * from rschedule where provider_no=?  and sdate >? order by sdate" }, 
    {"add_rschedule", "insert into rschedule values(?,?,?,?,?, ?,?,?)" },
    {"delete_rschedule", "delete from rschedule where provider_no=? and available=? and sdate=?" },
    {"update_rschedule", "update rschedule set edate=?, day_of_week=?, day_of_month=?, day_of_year=?, creator=? where provider_no=? and available=? and sdate=?" },
 
    {"search_scheduledate_c", "select * from scheduledate where priority='c' and provider_no=?" }, 
    {"add_scheduledate", "insert into scheduledate values(?,?,?,?,?, ?,?)" }, 
    {"delete_scheduledate", "delete from scheduledate where sdate=? and provider_no=? " }, // and priority=? 
    {"delete_scheduledate_b", "delete from scheduledate where provider_no=? and priority='b' and sdate>=? and sdate<=?" }, 
    {"delete_scheduledate_all", "delete from scheduledate where provider_no=? and sdate>=? and sdate<=?" }, 
    {"update_scheduledate", "update scheduledate set available=?, reason=?, hour=?, creator=? where sdate=? and provider_no=? and priority=?" }, 

    {"search_scheduleholiday", "select * from scheduleholiday where sdate like ?" }, 
    {"add_scheduleholiday", "insert into scheduleholiday values(?,?)" }, 
    {"delete_scheduleholiday", "delete from scheduleholiday where sdate=?" }, 
    {"update_scheduleholiday", "update scheduleholiday set holiday_name=? where sdate=?" }, 
    
    {"add_scheduletemplatecode", "insert into scheduletemplatecode values(?,?,?,?)" }, 
    {"search_scheduletemplatecode", "select * from scheduletemplatecode order by code" }, 
    {"search_scheduletemplatecodesingle", "select * from scheduletemplatecode where code = ?" }, 
    {"delete_scheduletemplatecode", "delete from scheduletemplatecode where code = ?" }, 
    
    {"add_scheduletemplate", "insert into scheduletemplate values(?,?,?,?)" }, 
    //{"search_scheduletemplate", "select * from scheduletemplate where provider_no=? order by ?" }, 
    {"search_scheduletemplate", "select * from scheduletemplate where provider_no=? order by name" }, 
    {"search_scheduletemplatesingle", "select * from scheduletemplate where provider_no=? and name= ?" }, 
    {"delete_scheduletemplate", "delete from scheduletemplate where provider_no=? and name=?" }, 
  };
   
  //associate each operation with an output JSP file -- displaymode
  scheduleMainBean.doConfigure(dbParams,dbQueries);
%>