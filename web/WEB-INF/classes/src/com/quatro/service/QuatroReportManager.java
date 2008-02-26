package com.quatro.service;

import java.util.ArrayList;
import java.util.List;
import com.quatro.dao.QuatroReportDao;
import com.quatro.model.*;

import java.util.Date;

public class QuatroReportManager {

    private QuatroReportDao quatroReportDao=null;

    public void setQuatroReportDao(QuatroReportDao quatroReportDao) {
        this.quatroReportDao = quatroReportDao;
    }

	public ReportValue GetReport(int rptNo, String loginId) {
		ReportValue reportValue = new ReportValue(); 
		reportValue =(ReportValue)quatroReportDao.GetReport(rptNo, loginId);
		
//		ArrayList options= (ArrayList)quatroReportDao.GetReportOptionList(rptNo);
//		reportValue.setOptions(options);
		reportValue.setRunTime(new java.util.Date());

		ArrayList filters= (ArrayList)quatroReportDao.GetFilterFieldList(rptNo);
		reportValue.setFilters(filters);

		return reportValue;
	}

	public ReportValue GetReport(int rptNo, int templateNo, String loginId) {
		ReportValue reportValue = new ReportValue(); 
		reportValue =(ReportValue)quatroReportDao.GetReport(rptNo, loginId);
		
		reportValue.setRunTime(new java.util.Date());

		ArrayList filters= (ArrayList)quatroReportDao.GetFilterFieldList(rptNo);
		reportValue.setFilters(filters);

		ReportTempValue rptTempValue=quatroReportDao.GetReportTemplate(templateNo, loginId);
		reportValue.setReportTemp(rptTempValue);
		
		return reportValue;
	}

	public List GetReportOptionList(int rptNo) {
		List options = null;
		options = quatroReportDao.GetReportOptionList(rptNo);
		return options;
	}
/*	
	public void DownloadRptFile(String rptFilePath, int fileNo) throws  Exception{
		if (fileNo <= 0) return;

    	   byte[] fileData = quatroReportDao.GetDocText(fileNo);
//			DocumentDataValue fileData= _rptDao.GetDocText(fileNo);

  		   if (fileData == null) return;

//		if (File.Exists(rptFilePath)) {
//			File.Delete(rptFilePath);
//		}
//		StreamWriter sw = new StreamWriter(rptFilePath);
//		Stream st = sw.BaseStream;
//		st.Write(fileData, 0, fileData.Length);
//		sw.Close();
		   File file=new File(rptFilePath);
		   if (file.exists()) file.delete();
		   FileOutputStream st = new FileOutputStream(rptFilePath);
           try{
		      for (int i = 0; i < fileData.length; i++) {
                 st.write(fileData[i]);
              }
              st.close();
           }catch(IOException ex){
        	  st.close();
           }
//		   _rptDao.UpdateReportOptVersion(fileNo);
           
	}
*/
	
    public List GetCriteriaFieldList(int rptNo)
    {
        List reportFilters = null;
        reportFilters = quatroReportDao.GetFilterFieldList(rptNo);
        return reportFilters;
    }

    public ReportFilterValue GetFilterField(int rptNo, int fieldNo)
    {
       ReportFilterValue rfv = quatroReportDao.GetFilterField(rptNo, fieldNo);
       return rfv;
    }
	public List GetReportList(String providerNo, int groupId) 
	{
		List lst=quatroReportDao.GetReportList(providerNo, groupId);
		return lst;
//		return quatroReportDao.GetReportList(providerNo);
	}

	public List GetReportGroupList(String providerNo) 
	{
		List rgs = quatroReportDao.GetReportGroupList(providerNo);
		for (int i=0; i<rgs.size(); i++){
			ReportGroupValue rgv = (ReportGroupValue) rgs.get(i);
//			rgv.setReports(quatroReportDao.GetReportList(providerNo, rgv.getReportGroupId()));
			List lst1=quatroReportDao.GetReportList(providerNo, rgv.getReportGroupId());
		    List lst3= new ArrayList();
			for (int j=0; j<lst1.size(); j++){
			  ReportValue obj1=(ReportValue)lst1.get(j);
			  ReportEXValue obj2= new ReportEXValue();
			  obj2.setReportNo(obj1.getReportNo());
			  obj2.setTitle(obj1.getTitle());
			  obj2.setDescription(obj1.getDescription());
			  obj2.setTemplateNo("");
			  List lst2 = quatroReportDao.GetReportTemplates(obj1.getReportNo(), providerNo);
			  obj2.setChildList(lst2);
		      lst3.add(obj2);
			}
			rgv.setReports(lst3);
		}
		return rgs;
	}

	public void SetReportDate(String sessionid,Date startDate, Date endDate){
		quatroReportDao.SetReportDate(sessionid, startDate, endDate);
	}

	public void SetReportDate(String sessionid, String startPayPeriod, String endPayPeriod){
		quatroReportDao.SetReportDate(sessionid, startPayPeriod, endPayPeriod);
	}
	
	public void SetReportDateSp(int reportNo, Date startDate, Date endDate, String spToRun){
		quatroReportDao.SetReportDateSp(reportNo, startDate, endDate, spToRun);
	}
	
	public List GetReportTemplates(int reportNo, String userId){
		return quatroReportDao.GetReportTemplates(reportNo, userId);
	}

	public void SaveReportTemplate(ReportTempValue rtv){
		quatroReportDao.SaveReportTemplate(rtv);
	}

	public void DeleteReportTemplates(String templateNo){
		if(templateNo==null) return;
		String[] sArray= templateNo.split(",");
		for(int i=0;i<sArray.length;i++){
		  quatroReportDao.DeleteReportTemplate(sArray[i]);
		}  
	}
}
