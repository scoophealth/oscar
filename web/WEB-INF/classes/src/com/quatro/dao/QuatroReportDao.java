package com.quatro.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.quatro.model.DocTextValue;
import com.quatro.model.LookupCodeValue;
import com.quatro.model.ReportDateSPValue;
import com.quatro.model.ReportDateValue;
import com.quatro.model.ReportFilterValue;
import com.quatro.model.ReportTempCriValue;
import com.quatro.model.ReportTempOrgValue;
import com.quatro.model.ReportTempValue;
import com.quatro.model.ReportValue;

public class QuatroReportDao extends HibernateDaoSupport {

	  public ReportValue GetReport(int rptNo, String providerNo)
	  {
          ArrayList paramList = new ArrayList();
		  String sSQL="from ReportValue s where s.reportNo=? AND s.providerNo=? order by s.accessType DESC";		
	      paramList.add(rptNo);
	      paramList.add(providerNo);
	      Object params[] = paramList.toArray(new Object[paramList.size()]);
	      return (ReportValue)getHibernateTemplate().find(sSQL ,params).get(0);
	  }

	  public List GetReportOptionList(int rptNo)
	  {
          ArrayList paramList = new ArrayList();
		  String sSQL="from ReportOptionValue s where s.active=true AND s.reportNo= ? order by s.shortDesc";		
	      paramList.add(rptNo);
	      Object params[] = paramList.toArray(new Object[paramList.size()]);
	      return getHibernateTemplate().find(sSQL ,params);
	  }

	  public List GetFilterFieldList(int rptNo)
	  {
          ArrayList paramList = new ArrayList();
		  String sSQL="from ReportFilterValue s WHERE s.reportNo=? order by s.fieldName";		
	      paramList.add(rptNo);
	      Object params[] = paramList.toArray(new Object[paramList.size()]);
	      return getHibernateTemplate().find(sSQL ,params);
	  }
	  
	  public ReportFilterValue GetFilterField(int rptNo, int fieldNo)
	  {
          ArrayList paramList = new ArrayList();
		  String sSQL="from ReportFilterValue s where s.reportNo=? AND s.fieldNo = ?";		
	      paramList.add(rptNo);
	      paramList.add(fieldNo);
	      Object params[] = paramList.toArray(new Object[paramList.size()]);
	      List lst=getHibernateTemplate().find(sSQL ,params);
	      if(lst.size()==0){
	    	 return null; 
	      }
	      else
	      {
	         return (ReportFilterValue)lst.get(0);
	      }
	  }

	  public List GetReportGroupList(String providerNo)
	  {
          ArrayList paramList = new ArrayList();
		  String sSQL="from ReportGroupValue s  order by s.reportGroupDesc";
	      return  getHibernateTemplate().find(sSQL);
	  }

	  public List GetReportList(String providerNo, int reportGroupId)
	  {
          ArrayList paramList = new ArrayList();
	      paramList.add(providerNo);
	      paramList.add(reportGroupId);
	      Object params[] = paramList.toArray(new Object[paramList.size()]);          
		  String sSQL="from ReportValue s WHERE s.providerNo=? AND s.reportGroupId=? order by s.reportGroupDesc";
	      return  getHibernateTemplate().find(sSQL,params);
	  }

      public void SetReportDate(String sessionId, Date startDate, Date endDate){
    	  ReportDateValue rdv= new ReportDateValue();
    	  rdv.setSessionId(sessionId);
    	  rdv.setStartDate(startDate);
    	  rdv.setEndDate(endDate);
          getHibernateTemplate().saveOrUpdate(rdv);
      }
      
      public void SetReportDate(String sessionId, String startPayPeriod, String endPayPeriod){
    	  ReportDateValue rdv= new ReportDateValue();
    	  rdv.setSessionId(sessionId);
    	  rdv.setStartDate_S(startPayPeriod);
    	  rdv.setEndDate_S(endPayPeriod);
          getHibernateTemplate().saveOrUpdate(rdv);
      }
      
  	  public void SetReportDateSp(int reportNo, Date startDate, Date endDate, String spToRun){

//        String sql="insert into ReportDateSPValue (reportNo, startDate, endDate, spToRun) VALUES (?, ?, ?,?)";
/*
          // this update record works
  		  String sql="update ReportDateSPValue rv set startDate=?,endDate=?,spToRun=? where reportNo=?";
          Query query = getSession().createQuery(sql);
		  query.setParameter(0, startDate, Hibernate.DATE);
		  query.setParameter(1, endDate, Hibernate.DATE);
		  query.setParameter(2, spToRun, Hibernate.STRING);
		  query.setParameter(3, reportNo);
		  int rowx = query.executeUpdate();
*/		  
    	  ReportDateSPValue rdsv= new ReportDateSPValue();
    	  rdsv.setReportNo(reportNo);
    	  rdsv.setStartDate(startDate);
    	  rdsv.setEndDate(endDate);
    	  rdsv.setSpToRun(spToRun);
          getHibernateTemplate().saveOrUpdate(rdsv);
  	  }

  	  public List GetReportTemplates(int reportNo, String loginId){
          ArrayList paramList = new ArrayList();
	      paramList.add(Integer.valueOf(reportNo));
	      paramList.add(loginId);
	      Object params[] = paramList.toArray(new Object[paramList.size()]);          
		  String sSQL="from ReportTempValue s WHERE s.reportNo=? AND (s.loginId=? or s.privateTemplate=true)";
	      return  getHibernateTemplate().find(sSQL,params);
  	  }

  	  public ReportTempValue GetReportTemplate(int templateNo, String loginId){
          ArrayList paramList = new ArrayList();
	      paramList.add(Integer.valueOf(templateNo));
	      paramList.add(loginId);
	      Object params[] = paramList.toArray(new Object[paramList.size()]);          
		  String sSQL="from ReportTempValue s WHERE s.templateNo=? AND (s.loginId=? or s.privateTemplate=true)";
	      ReportTempValue rptTempVal= (ReportTempValue)getHibernateTemplate().find(sSQL,params).get(0);
	      
	      paramList.clear();
	      paramList.add(Integer.valueOf(templateNo));
	      Object params2[] = paramList.toArray(new Object[paramList.size()]);          
		  sSQL="from ReportTempCriValue s WHERE s.templateNo=?";
		  ArrayList lst1 = (ArrayList)getHibernateTemplate().find(sSQL,params2);
		  for(int i=0;i<lst1.size();i++){
			ReportTempCriValue obj1 = (ReportTempCriValue)lst1.get(i);
		    paramList.clear();
		    paramList.add(Integer.valueOf(obj1.getFieldNo()));
		    Object params3[] = paramList.toArray(new Object[paramList.size()]);          
			sSQL="from ReportFilterValue s WHERE s.fieldNo=?";
		    ReportFilterValue obj2 = (ReportFilterValue)getHibernateTemplate().find(sSQL,params3).get(0);
		    obj1.setFilter(obj2);
		  }
		  rptTempVal.setTemplateCriteria(lst1);

//		  sSQL="from ReportTempOrgValue s WHERE s.templateNo=?";
//		  ArrayList lst2 = (ArrayList)getHibernateTemplate().find(sSQL,params2);
//		  rptTempVal.setOrgCodes(lst2);
		  
		  sSQL="select new com.quatro.util.KeyValueBean(b.code, b.description) from ReportTempOrgValue a, LookupCodeValue b WHERE a.templateNo=?" +
		      " and a.orgCd = b.code and b.prefix='ORG'";
		  ArrayList lst2 = (ArrayList)getHibernateTemplate().find(sSQL,params2);
		  rptTempVal.setOrgCodes(lst2);
		  
		  return rptTempVal;
  	  }

  	  public void SaveReportTemplate(ReportTempValue rtv){

  		 int templateNo = rtv.getTemplateNo();
	     Session sess= getSessionFactory().openSession();
  		 Transaction tx=sess.beginTransaction();
  		 try {
  		      if (templateNo > 0){
  	  		    Query query= sess.createQuery("delete ReportTempOrgValue s where s.templateNo = ?"); 
   	  	        query.setInteger(0, templateNo); 
  	  	        query.executeUpdate(); 

  	  		    query= sess.createQuery("delete ReportTempCriValue s where s.templateNo = ?"); 
  	  	        query.setInteger(0, templateNo); 
  	  	        query.executeUpdate();
              }
         
  		      sess.saveOrUpdate(rtv);

              ArrayList orgs = (ArrayList)rtv.getOrgCodes();
              if (orgs != null){
                for(int i=0;i<orgs.size();i++){
      	          LookupCodeValue orgCd = (LookupCodeValue)orgs.get(i);
                  if (orgCd != null){
                    ReportTempOrgValue rtlv = new ReportTempOrgValue();
                    rtlv.setTemplateNo(rtv.getTemplateNo());
                    rtlv.setOrgCd(orgCd.getCode());
                    sess.save(rtlv);
                  }
                }
              }
              
              ArrayList cris = rtv.getTemplateCriteria();
              if (cris != null){
                for(int i=0;i<cris.size();i++){
       	          ReportTempCriValue cri = (ReportTempCriValue)cris.get(i);
                  if (cri != null){
                    cri.setTemplateNo(rtv.getTemplateNo());
                    sess.save(cri);
                  }
                }
              }

              tx.commit();
  		 }catch(Exception e){
  			 tx.rollback();
  		 }
         sess.close();
  	  
  	  }

  	  public void DeleteReportTemplate(String templateNo){
         int iTempNo=Integer.valueOf(templateNo).intValue();
 	     
         Session sess= getSessionFactory().openSession();
   		 Transaction tx=sess.beginTransaction();
   		 try {
   			  Query query= sess.createQuery("delete ReportTempValue s where s.templateNo =?"); 
    	  	  query.setInteger(0, iTempNo); 
   	  	      query.executeUpdate(); 

	  		  query= sess.createQuery("delete ReportTempOrgValue s where s.templateNo = ?"); 
    	  	  query.setInteger(0, iTempNo); 
  	  	      query.executeUpdate(); 

  	  		  query= sess.createQuery("delete ReportTempCriValue s where s.templateNo = ?"); 
    	  	  query.setInteger(0, iTempNo); 
  	  	      query.executeUpdate();

  	  	      tx.commit();
   		 }catch(Exception e){
   			 tx.rollback();
   		 }
          sess.close();
   	  
   	  }
  	  
  	  public DocTextValue GetDocText(int fileNo){
          try{
  		  ArrayList paramList = new ArrayList();
		  String sSQL="from DocTextValue s where s.docId=?";		
	      paramList.add(fileNo);
	      Object params[] = paramList.toArray(new Object[paramList.size()]);
	      List lst = getHibernateTemplate().find(sSQL ,params);
	      return (DocTextValue)lst.get(0);
          }catch(Exception ex){
        	  return null;
          }
  	  }
}
