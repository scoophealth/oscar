package com.quatro.dao;

import java.util.*;

import com.quatro.model.*;

import org.caisi.model.CaisiEditor;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.hibernate.SessionFactory;
import org.hibernate.Hibernate;
import org.hibernate.Transaction;

public class QuatroReportDao extends HibernateDaoSupport {

	  public ReportValue GetReport(int rptNo, String providerNo)
	  {
          ArrayList paramList = new ArrayList();
		  String sSQL="FROM ReportValue s where s.reportNo=? AND s.providerNo=? ORDER BY s.accessType DESC";		
	      paramList.add(rptNo);
	      paramList.add(providerNo);
	      Object params[] = paramList.toArray(new Object[paramList.size()]);
	      return (ReportValue)getHibernateTemplate().find(sSQL ,params).get(0);
	  }

	  public List GetReportOptionList(int rptNo)
	  {
//	      return queryForList("GetReportOptionList",  rptNo);
          ArrayList paramList = new ArrayList();
		  String sSQL="FROM ReportOptionValue s where s.active=true AND s.reportNo= ? ORDER BY s.shortDesc";		
	      paramList.add(rptNo);
	      Object params[] = paramList.toArray(new Object[paramList.size()]);
	      return getHibernateTemplate().find(sSQL ,params);
	  }

	  public List GetFilterFieldList(int rptNo)
	  {
//	      return queryForList("GetFilterFieldList", rptNo);
          ArrayList paramList = new ArrayList();
		  String sSQL="FROM ReportFilterValue s WHERE s.reportNo=? order by s.fieldName";		
	      paramList.add(rptNo);
	      Object params[] = paramList.toArray(new Object[paramList.size()]);
	      return getHibernateTemplate().find(sSQL ,params);
	  }
	  
	  public ReportFilterValue GetFilterField(int rptNo, int fieldNo)
	  {
          ArrayList paramList = new ArrayList();
		  String sSQL="FROM ReportFilterValue s where s.reportNo=? AND s.fieldNo = ?";		
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
		  String sSQL="FROM ReportGroupValue s  ORDER BY s.reportGroupDesc";
	      return  getHibernateTemplate().find(sSQL);
	  }

	  public List GetReportList(String providerNo, int reportGroupId)
	  {
          ArrayList paramList = new ArrayList();
	      paramList.add(providerNo);
	      paramList.add(reportGroupId);
	      Object params[] = paramList.toArray(new Object[paramList.size()]);          
		  String sSQL="FROM ReportValue s WHERE s.providerNo=? AND s.reportGroupId=? ORDER BY s.reportGroupDesc";
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

  	  public List GetReportTemplates(int reportNo, String userId){
          ArrayList paramList = new ArrayList();
	      paramList.add(Integer.valueOf(reportNo));
	      paramList.add(userId);
	      Object params[] = paramList.toArray(new Object[paramList.size()]);          
		  String sSQL="FROM ReportTempValue s WHERE s.reportNo=? AND (s.loginId=? or s.privateTemplate=true)";
	      return  getHibernateTemplate().find(sSQL,params);
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
  	  
}
