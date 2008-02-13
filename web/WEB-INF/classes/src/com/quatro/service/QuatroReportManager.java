package com.quatro.service;

import java.util.*;
import com.quatro.dao.QuatroReportDao;
import com.quatro.model.*;

public class QuatroReportManager {

    private QuatroReportDao quatroReportDao=null;

    public void setQuatroReportDao(QuatroReportDao quatroReportDao) {
        this.quatroReportDao = quatroReportDao;
    }

	public ReportValue GetReport(int rptNo, String loginId) {
//		UserManagementService user = new UserManagementService();
//		UserValue uv = user.GetUser(loginId);

		ReportValue reportValue = new ReportValue(); 
		reportValue =(ReportValue)quatroReportDao.GetReport(rptNo, loginId);
		
//		ArrayList options= (ArrayList)quatroReportDao.GetReportOptionList(rptNo);
//		reportValue.setOptions(options);
		reportValue.setRunTime(new Date());

		ArrayList filters= (ArrayList)quatroReportDao.GetFilterFieldList(rptNo);
		reportValue.setFilters(filters);
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
		for (int i=0; i<rgs.size(); i++)
		{
			ReportGroupValue rgv = (ReportGroupValue) rgs.get(i);
			rgv.setReports(quatroReportDao.GetReportList(providerNo, rgv.getReportGroupId()));
		}
		return rgs;
	}

}
