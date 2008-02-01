package com.quatro.dao;

import java.util.ArrayList;
import java.util.List;

import com.quatro.model.*;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class QuatroReportDao extends HibernateDaoSupport {

	  public ReportValue GetReport(int rptNo, String providerNo)
	  {
/*
		  Hashtable<String, Object> param = new Hashtable<String, Object>(); 
	      param.put("ReportNo", rptNo);
	      param.put("UserId", userId);
	      return (ReportValue)queryForObject("GetReport", param);
*/	      
//		return (CaisiRole)this.getHibernateTemplate().find("from CaisiRole cr where cr.provider_no = ?",new Object[] {provider_no}).get(0);
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
		  String sSQL="FROM ReportFilterValue s WHERE s.reportNo=?";		
	      paramList.add(rptNo);
	      Object params[] = paramList.toArray(new Object[paramList.size()]);
	      return getHibernateTemplate().find(sSQL ,params);
	  }
	  
	  public ReportFilterValue GetFilterField(int rptNo, int fieldNo)
	  {
/*
		  Hashtable param = new Hashtable();
	      param.put("ReportNo",rptNo);
	      param.put("FieldNo",fieldNo);
	      return (ReportFilterValue)queryForObject("GetFilterField", param);
*/
          ArrayList paramList = new ArrayList();
		  String sSQL="FROM ReportFilterValue s where s.reportNo=? AND s.fieldNo = ?";		
	      paramList.add(rptNo);
	      paramList.add(fieldNo);
	      Object params[] = paramList.toArray(new Object[paramList.size()]);
	      return (ReportFilterValue)getHibernateTemplate().find(sSQL ,params).get(0);
	  }

}
