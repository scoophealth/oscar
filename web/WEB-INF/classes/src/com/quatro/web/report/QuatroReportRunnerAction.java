/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU General Public License
 * which accompanies this distribution, and is available at
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 *******************************************************************************/
package com.quatro.web.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.MyDateFormat;

import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.quatro.model.DataViews;
import com.quatro.model.LookupCodeValue;
import com.quatro.model.ReportFilterValue;
import com.quatro.model.ReportOptionValue;
import com.quatro.model.ReportTempCriValue;
import com.quatro.model.ReportTempValue;
import com.quatro.model.ReportValue;
import com.quatro.service.QuatroReportManager;
import com.quatro.util.HTMLPropertyBean;
import com.quatro.util.KeyValueBean;
import com.quatro.util.Utility;

public class QuatroReportRunnerAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String param=(String)request.getParameter("id");
		String param2=(String)request.getParameter("templateNo");
		int rptNo;
        int templateNo = 0;
        if (param2!=null) templateNo=Integer.parseInt(param2); 
		
		QuatroReportRunnerForm myForm = (QuatroReportRunnerForm)form;
		String loginId = (String)request.getSession().getAttribute("user");

		if(param2!=null)
		  request.getSession().setAttribute(DataViews.REPORTTPL, param2);
		else
		  if(request.getSession().getAttribute(DataViews.REPORTTPL)==null) request.getSession().setAttribute(DataViews.REPORTTPL, "0");

        ArrayList lstExportFormat = myForm.getExportFormatList();

//        KeyValueBean obj1= new KeyValueBean(String.valueOf(ReportExportFormat._crystalReports),"Crystal Report");
//        lstExportFormat.add(obj1);
//        KeyValueBean obj2= new KeyValueBean(String.valueOf(ReportExportFormat._MSExcel),"Excel (.xls)");
//        lstExportFormat.add(obj2);
//        KeyValueBean obj3= new KeyValueBean(String.valueOf(ReportExportFormat._MSWord),"MS-Word (.doc)");
//        lstExportFormat.add(obj3);
        KeyValueBean obj4= new KeyValueBean(String.valueOf(ReportExportFormat._PDF),"Portable Document Format (.pdf)");
        lstExportFormat.add(obj4);
//        KeyValueBean obj5= new KeyValueBean(String.valueOf(ReportExportFormat._text),"Text (.txt)");
//        lstExportFormat.add(obj5);
        myForm.setExportFormat(String.valueOf(ReportExportFormat._PDF));
        
        if(param!=null){
	        rptNo = Integer.parseInt(param);
            myForm.setReportNo(param);
	        if (param2!=null && templateNo > 0)
	            Refresh(myForm, loginId, rptNo, templateNo, request);
	        else
	            Refresh(myForm, loginId, rptNo, request, true);

	        ActionForward forward = mapping.findForward("success");
			return forward;
		}
		//postback form
        else
		{
			rptNo=Integer.parseInt(myForm.getReportNo());
            //
			String method = (String)request.getParameter("method");
			if("Run".equals(method))
			{
				btnRun_Click(rptNo, myForm, request);
			}
			else if("Save".equals(method))
			{
				btnSave_Click(rptNo, myForm, request);
			}
			else if("AddTplCri".equals(method))
			{
				btnAddTplCri_Click(rptNo, myForm, request);
			}
			else if("InsertTplCri".equals(method))
			{
				btnInsertTplCri_Click(rptNo, myForm, request);
			}
			else if("RemoveTplCri".equals(method))
			{
				btnRemoveTplCri_Click(rptNo, myForm, request);
			}
			else if("onCriteriaChange".equals(method))
			{
				OnCriteriaTextChangedHandler(rptNo, myForm, request);
			}
		    Refresh(myForm, loginId, rptNo, request, false);
            ActionForward forward = mapping.findForward("success");
     	    return forward;
		}

	}
    
	public void OnCriteriaTextChangedHandler(int reportNo, QuatroReportRunnerForm myForm, HttpServletRequest request)
	{
		ReportValue rptVal = (ReportValue)request.getSession().getAttribute(DataViews.REPORT);
        ReportTempValue rptTemp = rptVal.getReportTemp();
        if (rptTemp == null)
        {
            rptTemp = new ReportTempValue();
            rptTemp.setReportNo(reportNo);
        }
        // which cell is changing?
		String sTemp1=(String)request.getParameter("onCriteriaChange");
		String[] aTemp1 = sTemp1.split("].");
		String sTemp2 = aTemp1[0].replaceAll("tplCriteria", "");
		sTemp2= sTemp2.substring(1, sTemp2.length());
		int row= Integer.parseInt(sTemp2);

		int col=0;
		if(aTemp1[1].equals("relation")){
		  col=1;
		}
		else if(aTemp1[1].equals("fieldNo")){
		  col=2;
		}
		else if(aTemp1[1].equals("op")){
		  col=3;
		}
		else if(aTemp1[1].equals("val")){
		  col=4;
		}

		if(col!=2) return;
		
		ArrayList cris = (ArrayList) request.getSession().getAttribute(DataViews.REPORT_CRI);
		Map map=request.getParameterMap();
		QuatroReportManager rpt = (QuatroReportManager)WebApplicationContextUtils.getWebApplicationContext(
        		getServlet().getServletContext()).getBean("quatroReportManager");
	    for(int i=0;i<cris.size();i++)
	    {
          if(map.get("p" + i)!=null) continue;
	      ReportTempCriValue criNew = (ReportTempCriValue)cris.get(i);
          String[] arRelation=(String[])map.get("tplCriteria[" + i + "].relation");
          String[] arFieldNo=(String[])map.get("tplCriteria[" + i + "].fieldNo");
          String[] arOp=(String[])map.get("tplCriteria[" + i + "].op");
          String[] arVal=(String[])map.get("tplCriteria[" + i + "].val");
          String[] arValDesc=(String[])map.get("tplCriteria[" + i + "].valdesc");
          String[] arFieldType=(String[])map.get("tplCriteria[" + i + "].filter.fieldType");
          String[] arLookupTable=(String[])map.get("tplCriteria[" + i + "].filter.lookupTable");

	      if(arRelation!=null) criNew.setRelation(arRelation[0]);
	      if(arOp!=null) criNew.setOp(arOp[0]);
	      if(arVal!=null) criNew.setVal(arVal[0]);
	      if(arValDesc!=null) criNew.setValDesc(arValDesc[0]);
	      if(arFieldNo!=null){
		  	int iFieldNo = Integer.parseInt(arFieldNo[0]);
		  	criNew.setFieldNo(iFieldNo);
			ReportFilterValue filter = new ReportFilterValue();
		    filter.setFieldNo(iFieldNo);
		    if(arFieldType!=null) filter.setFieldType(arFieldType[0]);
		    if(arLookupTable!=null) filter.setLookupTable(arLookupTable[0]);
		    criNew.setFilter(filter);
		  }
	    }

		ReportTempCriValue rptCri = (ReportTempCriValue)cris.get(row);
		ReportFilterValue rptFilterVal = null;
        String[] arFieldNo=(String[])map.get("tplCriteria[" + row + "].fieldNo");
        int fieldNo= 0;
        if(arFieldNo!=null) fieldNo=Integer.parseInt(arFieldNo[0]);
		rptCri.setFieldNo(fieldNo);
		if (fieldNo == 0){
			rptCri.setFilter(rptFilterVal);
			return;
		}
		rptFilterVal = rpt.GetFilterField(reportNo, rptCri.getFieldNo());
		rptCri.setOperatorList(GetOperatorList(rptFilterVal.getOp()));
		rptCri.setFilter(rptFilterVal);

	    rptCri.setVal("");
		rptCri.setValDesc("");
		
		request.getSession().setAttribute(DataViews.REPORT_CRI, cris);
		if(rptVal.getReportTemp()!=null){
		  rptVal.getReportTemp().setTemplateCriteria(cris);
  		  request.getSession().setAttribute(DataViews.REPORT, rptVal);
		}
		myForm.setTemplateCriteriaList(cris);
	}

    public void btnInsertTplCri_Click(int reportNo, QuatroReportRunnerForm myForm, HttpServletRequest request)
	{
		ChangeTplCriTable(3, myForm, request);   // insert
	}

	public void btnRemoveTplCri_Click(int reportNo, QuatroReportRunnerForm myForm, HttpServletRequest request)
	{
		ChangeTplCriTable(1, myForm, request);
	}

	public void btnAddTplCri_Click(int reportNo, QuatroReportRunnerForm myForm, HttpServletRequest request)
	{
		ChangeTplCriTable(2, myForm, request);
    }

	public void btnSave_Click(int reportNo, QuatroReportRunnerForm myForm, HttpServletRequest request)
	{
        try
        {
        	BuildTemplate(myForm, request); 
        }catch (Exception ex){
            return;        
	    }
		myForm.setStrClientJavascript("saveTemplate");
	}
	
	public void btnRun_Click(int reportNo, QuatroReportRunnerForm myForm, HttpServletRequest request)
	{
        ReportTempValue rptTempVal=null;
        try
        {
        	BuildTemplate(myForm, request); 
        }catch (Exception ex){
            return;        
	    }
        
        try{
            ReportValue rptVal = (ReportValue)request.getSession().getAttribute(DataViews.REPORT);
            
            rptVal.setExportFormatType(Integer.parseInt(myForm.getExportFormat()));
            rptVal.setPrint2Pdf(false); // this property is kept only for the customized prints 

            ReportOptionValue option= new ReportOptionValue();
            if(myForm.getReportOption()==null){
               Iterator iObj=rptVal.getOptions().iterator();
               while(iObj.hasNext()){
                 option = (ReportOptionValue)iObj.next();
                 break;
               }
            }else{
              int optionNo = Integer.parseInt(myForm.getReportOption());
              Iterator iObj=rptVal.getOptions().iterator();
              while(iObj.hasNext()){
                option = (ReportOptionValue)iObj.next();
                if(optionNo==option.getOptionNo()) break;
              }
            }

    		request.getSession().setAttribute(DataViews.REPORT, rptVal);
    		request.getSession().setAttribute(DataViews.REPORT_OPTION, option);
   		    myForm.setStrClientJavascript("showReport");//('" + request.getContextPath() + "/PMmodule/Reports/quatroReportViewer.do');");
        }
        catch (Exception ex)
        {
           ;
        }
        
	}
	
	public void Refresh(QuatroReportRunnerForm myForm, String loginId,int reportNo, int templateNo, HttpServletRequest request)
	{
		ReportValue rptVal=null;
		
	    QuatroReportManager reportManager = (QuatroReportManager)WebApplicationContextUtils.getWebApplicationContext(
	        		getServlet().getServletContext()).getBean("quatroReportManager");

	    rptVal = reportManager.GetReport(reportNo,templateNo, loginId);
		request.getSession().setAttribute(DataViews.REPORT, rptVal);

		request.getSession().setAttribute(DataViews.REPORTTPL, String.valueOf(templateNo));

		Refresh(myForm, loginId, reportNo, request, false);
		
		ReportTempValue rptTempVal =rptVal.getReportTemp();
		if("D".equals(rptVal.getDatePart())){
		  myForm.setStartField(MyDateFormat.getSysDateString(rptTempVal.getStartDate()));
		  myForm.setEndField(MyDateFormat.getSysDateString(rptTempVal.getEndDate()));
		}else{
			myForm.setStartField(rptTempVal.getStartPayPeriod());
			myForm.setEndField(rptTempVal.getEndPayPeriod());
		}
		request.getSession().setAttribute(DataViews.REPORT_CRI, rptTempVal.getTemplateCriteria());
		
		myForm.setOrgSelectionList(rptTempVal.getOrgCodes());
		myForm.setReportOption(String.valueOf(rptTempVal.getReportOptionID()));
	}
	
	public void Refresh(QuatroReportRunnerForm myForm, String loginId,int reportNo, HttpServletRequest request, boolean refreshFromDB)
	{
		ReportValue rptVal=null;
		
	    QuatroReportManager reportManager = (QuatroReportManager)WebApplicationContextUtils.getWebApplicationContext(
	        		getServlet().getServletContext()).getBean("quatroReportManager");
		if(refreshFromDB==true){
		   rptVal = reportManager.GetReport(reportNo, loginId);
		   request.getSession().setAttribute(DataViews.REPORTTPL, "0");
		}else{
		   rptVal = (ReportValue)request.getSession().getAttribute(DataViews.REPORT);
		}
		
		request.getSession().setAttribute(DataViews.REPORT, rptVal);
        
        if (rptVal == null) return;
        
        myForm.setReportTitle(rptVal.getTitle());
        HTMLPropertyBean obj1= new HTMLPropertyBean();
        HTMLPropertyBean obj2= new HTMLPropertyBean();
        HTMLPropertyBean obj3= new HTMLPropertyBean();
        HTMLPropertyBean obj4= new HTMLPropertyBean();

        if ("D".equals(rptVal.getDatePart()))
        {
            obj1.setVisible("visibility:visible;");
            obj2.setVisible("visibility:visible;");
            obj3.setVisible("visibility:hidden;width=1px;");
            obj4.setVisible("visibility:hidden;width=1px;");
        }
        else
        {
            obj1.setVisible("visibility:hidden;width=1px;");
            obj2.setVisible("visibility:hidden;width=1px;");
            obj3.setVisible("visibility:visible;");
            obj4.setVisible("visibility:visible;");
        }

        switch ((rptVal.getDateOption()!=null)?(int)rptVal.getDateOption().charAt(0):-1)
        {
            case (int)'A':
                obj1.setEnable("true");
                obj2.setEnable("false");
                obj3.setEnable("true");
                obj4.setEnable("false");
                myForm.setLblDateRange("REPORT DATE");
                myForm.setLblStartDate("As of");
                myForm.setLblEndDate("(N/A)");
                break;
            case (int)'G':
                obj1.setEnable("true");
                obj2.setEnable("false");
                obj3.setEnable("true");
                obj4.setEnable("false");
                myForm.setLblDateRange("REPORT DATE");
                myForm.setLblStartDate("Since");
                myForm.setLblEndDate("(N/A)");
                break;
            case (int)'L':
                obj1.setEnable("true");
                obj2.setEnable("false");
                obj3.setEnable("true");
                obj4.setEnable("false");
                myForm.setLblDateRange("REPORT DATE");
                myForm.setLblStartDate("Upto");
                myForm.setLblEndDate("(N/A)");
                break;
            case (int)'B':
                obj1.setEnable("true");
                obj2.setEnable("true");
                obj3.setEnable("true");
                obj4.setEnable("true");
                myForm.setLblDateRange("REPORT DATE RANGE");
                myForm.setLblStartDate("START");
                myForm.setLblEndDate("END");
                break;
            default:
                obj1.setEnable("false");
                obj2.setEnable("false");
                obj3.setEnable("false");
                obj4.setEnable("false");
                myForm.setLblDateRange("Date Range:");
                myForm.setLblStartDate("");
                myForm.setLblEndDate("");
                break;
        }

        myForm.setStartDateProperty(obj1);
        myForm.setEndDateProperty(obj2);
        myForm.setStartTxtProperty(obj3);
        myForm.setEndTxtProperty(obj4);

		  
        String sYMD = "";
       	if ("M".equals(rptVal.getDatePart()))
		{
            sYMD = " MONTH";
		}
		else if ("Y".equals(rptVal.getDatePart()))
		{
            sYMD = " YEAR";
		}
        if (!"(N/A)".equals(myForm.getLblStartDate())) myForm.setLblStartDate(myForm.getLblStartDate() + sYMD);
        if (!"(N/A)".equals(myForm.getLblEndDate())) myForm.setLblEndDate(myForm.getLblEndDate() + sYMD);

        HTMLPropertyBean obj5= new HTMLPropertyBean();
		if (rptVal.isOrgApplicable()) 
		{
			obj5.setVisible("visibility:visible;");
			ArrayList<KeyValueBean> lst= new ArrayList<KeyValueBean>();
			String sOrgKey=myForm.getTxtOrgKey();
			String sOrgValue=myForm.getTxtOrgValue();
			if(sOrgKey!=null){
		      String[] sArray1=sOrgKey.split(":");
		      String[] sArray2=sOrgValue.split(":");
              for(int i=0;i<sArray1.length;i++){		      
			    lst.add(new KeyValueBean(sArray1[i], sArray2[i]));
              }  
			  myForm.setOrgSelectionList(lst);
			}  
		}
		else
		{
			obj5.setVisible("visibility:hidden;height=1px;");
			ArrayList<KeyValueBean> lst= new ArrayList<KeyValueBean>();
			lst.add(new KeyValueBean("Not Applicable", "Not Applicable"));
			myForm.setOrgSelectionList(lst);
		}
		myForm.setOrgSelectionProperty(obj5);

		ArrayList rptOptions = new ArrayList(); 
       	for (Iterator it = rptVal.getOptions().iterator(); it.hasNext();){
        	ReportOptionValue rv = (ReportOptionValue)it.next();
       		rptOptions.add(rv);
        	if(myForm.getReportOption()==null){
       		  if(rv.isDefault())  myForm.setReportOption(String.valueOf(rv.getOptionNo()));
        	}
       	}
		myForm.setReportOptionList(rptOptions);

		if(refreshFromDB==false){
		  if(rptVal.getReportTemp()!=null){	
              ArrayList lst=rptVal.getReportTemp().getTemplateCriteria();
              for(int i=0;i<lst.size();i++){
                ReportTempCriValue obj=(ReportTempCriValue)lst.get(i);
				ReportFilterValue rptFilterVal = reportManager.GetFilterField(reportNo, obj.getFieldNo());
				if(rptFilterVal!=null) obj.setOperatorList(GetOperatorList(rptFilterVal.getOp()));
              }
              myForm.setTemplateCriteriaList(lst);
		  }  
		}
		
		RefreshCriteria(myForm, request);
	}

	private void RefreshCriteria(QuatroReportRunnerForm myForm, HttpServletRequest request)
	{
        ReportValue rptVal = (ReportValue)request.getSession().getAttribute(DataViews.REPORT);
		myForm.setFilterFields((ArrayList)rptVal.getFilters());
	}
	
	private void BuildTemplate(QuatroReportRunnerForm myForm, HttpServletRequest request) throws Exception
    {
		String loginId = (String)request.getSession().getAttribute("user");

		ReportValue rptVal = (ReportValue)request.getSession().getAttribute(DataViews.REPORT);
		ReportTempValue repTemp = new ReportTempValue();
	    int reportNo = rptVal.getReportNo();
        ReportOptionValue option = null;
        repTemp.setReportNo(reportNo);
	    repTemp.setErrMsg("");
	    String errMsg = "";
		boolean noDateRange = false;
		boolean noOrgs = false;
		boolean noCriteria = false;

		if (rptVal.getReportTemp() != null){
		  repTemp.setTemplateNo(rptVal.getReportTemp().getTemplateNo());
		  repTemp.setDesc(rptVal.getReportTemp().getDesc());
		  repTemp.setPrivateTemplate(rptVal.getReportTemp().isPrivateTemplate());
		  errMsg = rptVal.getReportTemp().ErrMsg;
		}
		
		repTemp.setLoginId(loginId);
		boolean hasAnyCriteria = false;
		if ("D".equals(rptVal.getDatePart())){
		  if ("N".equals(rptVal.getDateOption())){
		    repTemp.setStartDate(MyDateFormat.getSysDate("1900-01-01"));
		    repTemp.setEndDate(MyDateFormat.getSysDate("2999-12-31"));
		  }else{
		    if (Utility.IsEmpty(myForm.getStartField()) && Utility.IsEmpty(myForm.getEndField())){
		      noDateRange = true;
		    }else if (Utility.IsEmpty(myForm.getStartField())){
		      repTemp.setStartDate(MyDateFormat.getCurrentDate());
			  repTemp.setEndDate(MyDateFormat.getSysDate(myForm.getEndField()));
		    }else if (Utility.IsEmpty(myForm.getEndField())){
	          repTemp.setStartDate(MyDateFormat.getSysDate(myForm.getStartField()));
		      repTemp.setEndDate(MyDateFormat.getCurrentDate());
		    }else{
		      repTemp.setStartDate(MyDateFormat.getSysDate(myForm.getStartField()));
			  repTemp.setEndDate(MyDateFormat.getSysDate(myForm.getEndField()));
		      if (repTemp.getStartDate().after(repTemp.getEndDate()))
		        errMsg = " Start Date Must Be Less or Equal to the End Date";
		    }
		  }
		}else{
		  if ("N".equals(rptVal.getDateOption())){
		    repTemp.setStartPayPeriod(null);
		    repTemp.setEndPayPeriod(null);
		  }else{
		    repTemp.setStartPayPeriod(myForm.getStartField().trim());
		    repTemp.setEndPayPeriod(myForm.getEndField().trim());
		    if ("".equals(repTemp.getStartPayPeriod()) && "".equals(repTemp.getEndPayPeriod())){
		      noDateRange = true;
		    }else if ("".equals(repTemp.getStartPayPeriod())){
		      repTemp.setStartPayPeriod(repTemp.getEndPayPeriod());
		    }else if ("".equals(repTemp.getEndPayPeriod())){
		      repTemp.setEndPayPeriod(repTemp.getStartPayPeriod());
		    }else{
		      if (repTemp.getStartPayPeriod().compareTo(repTemp.getEndPayPeriod()) > 0)
		         errMsg = " Start Date Must Be Less or Equal to the End Date";
		    }
		  }
		}

		if (rptVal.isOrgApplicable()){
		  ArrayList orgCodes = new ArrayList();
		  if(myForm.getTxtOrgKey()!=null){
		    String[] sOrgKey=myForm.getTxtOrgKey().split(":");
		    String[] sOrgValue=myForm.getTxtOrgValue().split(":");
		    for (int i = 0; i < sOrgKey.length; i++){
			  LookupCodeValue org = new LookupCodeValue();
			  org.setCode(sOrgKey[i]);
			  org.setDescription(sOrgValue[i]);
			  orgCodes.add(org);
		    }
		  }
		  repTemp.setOrgCodes(orgCodes);
		  noOrgs = (orgCodes.size() == 0);
		}else{
		  repTemp.setOrgCodes(null);
		}

        if(myForm.getReportOption()==null){
           Iterator iObj=rptVal.getOptions().iterator();
           while(iObj.hasNext()){
             option = (ReportOptionValue)iObj.next();
             break;
           }
        }else{
          int optionNo = Integer.parseInt(myForm.getReportOption());
          Iterator iObj=rptVal.getOptions().iterator();
          while(iObj.hasNext()){
            option = (ReportOptionValue)iObj.next();
            if(optionNo==option.getOptionNo()) break;
          }
        }
	    repTemp.setReportOptionID(option.getOptionNo());
		  
	    ArrayList<ReportTempCriValue> tempCris= new ArrayList<ReportTempCriValue>();
		Map map=request.getParameterMap();
	    String[] obj2= (String[])map.get("lineno");
	    int lineno=0;
	    if(obj2!=null) lineno=obj2.length;
        for(int i=0;i<lineno;i++){
	      ReportTempCriValue criNew = new ReportTempCriValue();
          String[] arRelation=(String[])map.get("tplCriteria[" + i + "].relation");
          String[] arFieldNo=(String[])map.get("tplCriteria[" + i + "].fieldNo");
          String[] arOp=(String[])map.get("tplCriteria[" + i + "].op");
          String[] arVal=(String[])map.get("tplCriteria[" + i + "].val");
          String[] arValDesc=(String[])map.get("tplCriteria[" + i + "].valdesc");
          String[] arFieldType=(String[])map.get("tplCriteria[" + i + "].filter.fieldType");
          String[] arLookupTable=(String[])map.get("tplCriteria[" + i + "].filter.lookupTable");
	      if(arRelation!=null) criNew.setRelation(arRelation[0]);
		  if(arOp!=null) criNew.setOp(arOp[0]);
		  if(arVal!=null) criNew.setVal(arVal[0]);
		  if(arValDesc!=null) criNew.setValDesc(arValDesc[0]);
	      if(arFieldNo!=null){
	  		int iFieldNo = Integer.parseInt(arFieldNo[0]);
	  		criNew.setFieldNo(iFieldNo);
			ReportFilterValue filter = new ReportFilterValue();
	        filter.setFieldNo(iFieldNo);
	        if(arFieldType!=null) filter.setFieldType(arFieldType[0]);
	        if(arLookupTable!=null) filter.setLookupTable(arLookupTable[0]);
	        criNew.setFilter(filter);
		  }
		  tempCris.add(criNew);
        }
        repTemp.setTemplateCriteria(tempCris);
        rptVal.setReportTemp(repTemp);

        request.getSession().setAttribute(DataViews.REPORT, rptVal);
	    
	    noCriteria = true;
	      
	      String sError="";
		  if (tempCris.size()>0){
		    noCriteria = (tempCris.size() == 0);
		    if (!noCriteria){
		      sError=ValidateCriteriaString(reportNo, tempCris);
		      if("".equals(sError)){
		    	myForm.setLblError("");
		      }else{
		    	myForm.setLblError("Error: " + sError);
		      }
		    }
		  }
		 
		  hasAnyCriteria = !(noDateRange && noOrgs && noCriteria);

		  if (!hasAnyCriteria || !("".equals(sError))) errMsg = "Please specify criteria for the report<br>" + sError;

		  if (!Utility.IsEmpty(errMsg))
		    throw new Exception(errMsg);
		  else
		    return;// repTemp;
    }
	
    public String ValidateCriteriaString(int reportNo, ArrayList criterias){
        String tableName = "";
        String criteriaDis = "";

        if (criterias == null || criterias.size() == 0) return "";

        ReportTempCriValue rptCri = (ReportTempCriValue)criterias.get(0);
	    QuatroReportManager reportManager = (QuatroReportManager)WebApplicationContextUtils.getWebApplicationContext(
        		getServlet().getServletContext()).getBean("quatroReportManager");

        String criteriaSQL = "(";
        criteriaDis = "(\n";

        String err = "";
        for (int i = 0; i < criterias.size(); i++){
          rptCri = (ReportTempCriValue)criterias.get(i);
          String relation = rptCri.getRelation();
          int fieldNo = rptCri.getFieldNo();
          String op = rptCri.getOp();
          String val = rptCri.getVal();
          String valDesc = rptCri.getValDesc();
          if (Utility.IsEmpty(valDesc)) valDesc = val;

          if (!Utility.IsEmpty(relation)){
            if ("(".equals(criteriaSQL)){
              if (relation.equals("(")){
                criteriaSQL += " " + relation + " ";
              }else{
                err += "Syntax Error detected, wrong relation at line " + String.valueOf(i + 1) + "<br>";
              }
            }else{
              criteriaSQL += " " + relation + " ";
            }
            criteriaDis += " " + relation + " ";
          }

          if (fieldNo > 0){
            if (Utility.IsEmpty(relation) && !"(".equals(criteriaSQL))
              err += "Missing relations at line " + String.valueOf(i + 1) + "<br>";
                
            if (Utility.IsEmpty(op)) err += "Missing Operator at line " + String.valueOf(i + 1) + "<br>";

            if (Utility.IsEmpty(val)) err += "Missing Values at line " + String.valueOf(i + 1) + "<br>";

            ReportFilterValue filter = reportManager.GetFilterField(reportNo, fieldNo);
            String FieldType = filter.getFieldType();
            criteriaSQL += "{" + tableName + filter.getFieldSQL() + "}";
            criteriaDis += filter.getFieldName();

            criteriaSQL += " " + op + " ";
            criteriaDis += " " + op + " ";

            if ("IN".equals(op)){
              criteriaSQL += val;
              criteriaDis += "(" + val + ")";
            }else if ("LIKE".equals(op)){
              criteriaSQL += "\"" + val + "\"";
              criteriaDis += val;
            }else{
              if ("D".equals(FieldType)){
                Date dateValue;
                if ("TODAY".equals(val)){
                  dateValue = MyDateFormat.getCurrentDate();
                }else{
                  dateValue = MyDateFormat.getSysDate(val);
                }
                criteriaSQL += dateValue.toString();
                criteriaDis += dateValue.toString();
              }else if ("S".equals(FieldType)){
                criteriaSQL += "\"" + val + "\"";
                criteriaDis += "\"" + valDesc + "\"";
              }else{
                criteriaSQL += val;
                criteriaDis += val;
              }
            }
            criteriaDis += "\n";      // end of line
          }
        }

        criteriaSQL += ")";
        criteriaDis += ")";

        //are parenthes paired correctly?
        int nLeft = 0;
        for (int i = 0; i < criteriaSQL.length(); i++){
          String c = criteriaSQL.substring(i, i+1);
          if ("(".equals(c)) nLeft++;
          if (")".equals(c)) nLeft--;
          if (nLeft < 0) break;
        }

        if (nLeft != 0) err += "Syntax Error detected, right parenthes not match left parenthes";

        if (criteriaDis.equals("()")) criteriaDis = "None";
        if (criteriaSQL.equals("()")) criteriaSQL = "";

        if (!"".equals(err)){
//        	throw new Exception(err);
          criteriaDis = "";
          criteriaSQL = "";
        }
        return err;
    }

	private ArrayList GetOperatorList(String ops)
	{
		ArrayList<KeyValueBean> operators = new ArrayList<KeyValueBean>();
		if (Utility.IsEmpty(ops)) return operators;
		for(int i=0; i<ops.length(); i++)
		{
			String op = ops.substring(i,i+1);
			if(op.equals("B"))
			{
			  operators.add(new KeyValueBean("BETWEEN", "Between"));
			}
			else if(op.equals("C"))
			{
			  operators.add(new KeyValueBean("<","<"));
			  operators.add(new KeyValueBean("<=","<="));
			  operators.add(new KeyValueBean("=","="));
			  operators.add(new KeyValueBean(">=",">="));
			  operators.add(new KeyValueBean(">",">"));
  			  operators.add(new KeyValueBean("<>","<>"));
			}
			else if(op.equals("I"))
		    {
			  operators.add(new KeyValueBean("Any of","IN"));
			}
			else if(op.equals("L"))
		    {
			  operators.add(new KeyValueBean("Like","LIKE"));
			}

		}
		return operators;
	}

    public void ChangeTplCriTable(int operationType, QuatroReportRunnerForm myForm, HttpServletRequest request)
    {
    	ArrayList<ReportTempCriValue> obj= new ArrayList<ReportTempCriValue>();
		ArrayList cris = new ArrayList();
		if(request.getSession().getAttribute(DataViews.REPORT_CRI)!=null)
			 cris = (ArrayList) request.getSession().getAttribute(DataViews.REPORT_CRI);

		Map map=request.getParameterMap();
		String[] obj2= (String[])map.get("lineno");
		int lineno=0;
		if(obj2!=null) lineno=obj2.length;
		
		int iReportNo = Integer.parseInt(myForm.getReportNo());

		QuatroReportManager reportManager = (QuatroReportManager)WebApplicationContextUtils.getWebApplicationContext(
        		getServlet().getServletContext()).getBean("quatroReportManager");

		switch(operationType)
		{
		  case 1:  //remove
		    for(int i=0;i<lineno;i++)
		    {
	          if(map.get("p" + i)!=null) continue;
	          ReportTempCriValue criNew=(ReportTempCriValue)cris.get(i);
	          String[] arRelation=(String[])map.get("tplCriteria[" + i + "].relation");
	          String[] arFieldNo=(String[])map.get("tplCriteria[" + i + "].fieldNo");
	          String[] arOp=(String[])map.get("tplCriteria[" + i + "].op");
	          String[] arVal=(String[])map.get("tplCriteria[" + i + "].val");
	          String[] arValDesc=(String[])map.get("tplCriteria[" + i + "].valdesc");
	          String[] arFieldType=(String[])map.get("tplCriteria[" + i + "].filter.fieldType");
	          String[] arLookupTable=(String[])map.get("tplCriteria[" + i + "].filter.lookupTable");
		      if(arRelation!=null) criNew.setRelation(arRelation[0]);
		      if(arOp!=null) criNew.setOp(arOp[0]);
		      if(arVal!=null) criNew.setVal(arVal[0]);
		      if(arValDesc!=null) criNew.setValDesc(arValDesc[0]);
	          if(arFieldNo!=null){
		  		int iFieldNo = Integer.parseInt(arFieldNo[0]);
				ReportFilterValue filter = new ReportFilterValue();
			    filter.setFieldNo(iFieldNo);
			    if(arFieldType!=null) filter.setFieldType(arFieldType[0]);
			    if(arLookupTable!=null) filter.setLookupTable(arLookupTable[0]);
		        criNew.setFilter(filter);
		      }  
	          obj.add(criNew);
	          
		    }
		    break;
		  case 2:  //add
			for(int i=0;i<lineno;i++)
			{
              ReportTempCriValue criNew=(ReportTempCriValue)cris.get(i);
		      String[] arRelation=(String[])map.get("tplCriteria[" + i + "].relation");
	          String[] arFieldNo=(String[])map.get("tplCriteria[" + i + "].fieldNo");
		      String[] arOp=(String[])map.get("tplCriteria[" + i + "].op");
		      String[] arVal=(String[])map.get("tplCriteria[" + i + "].val");
		      String[] arValDesc=(String[])map.get("tplCriteria[" + i + "].valdesc");
	          String[] arFieldType=(String[])map.get("tplCriteria[" + i + "].filter.fieldType");
	          String[] arLookupTable=(String[])map.get("tplCriteria[" + i + "].filter.lookupTable");
			  if(arRelation!=null) criNew.setRelation(arRelation[0]);
			  if(arOp!=null) criNew.setOp(arOp[0]);
			  if(arVal!=null) criNew.setVal(arVal[0]);
			  if(arValDesc!=null) criNew.setValDesc(arValDesc[0]);
	          if(arFieldNo!=null){
	  		    int iFieldNo = Integer.parseInt(arFieldNo[0]);
			    ReportFilterValue filter = new ReportFilterValue();
		        filter.setFieldNo(iFieldNo);
		        if(arFieldType!=null) filter.setFieldType(arFieldType[0]);
		        if(arLookupTable!=null) filter.setLookupTable(arLookupTable[0]);
		        criNew.setFilter(filter);
	          }  
		      obj.add(criNew);
			}
			ReportTempCriValue criNew2 = new ReportTempCriValue();
			obj.add(criNew2);
			break;
		  case 3:  //insert
			boolean isInserted=false;
			for(int i=0;i<lineno;i++)
			{
	          ReportTempCriValue criNew=(ReportTempCriValue)cris.get(i);
			  String[] arRelation=(String[])map.get("tplCriteria[" + i + "].relation");
	          String[] arFieldNo=(String[])map.get("tplCriteria[" + i + "].fieldNo");
			  String[] arOp=(String[])map.get("tplCriteria[" + i + "].op");
			  String[] arVal=(String[])map.get("tplCriteria[" + i + "].val");
			  String[] arValDesc=(String[])map.get("tplCriteria[" + i + "].valdesc");
	          String[] arFieldType=(String[])map.get("tplCriteria[" + i + "].filter.fieldType");
	          String[] arLookupTable=(String[])map.get("tplCriteria[" + i + "].filter.lookupTable");
			  if(arRelation!=null) criNew.setRelation(arRelation[0]);
			  if(arOp!=null) criNew.setOp(arOp[0]);
			  if(arVal!=null) criNew.setVal(arVal[0]);
			  if(arValDesc!=null) criNew.setValDesc(arValDesc[0]);
	          if(arFieldNo!=null){
		  		int iFieldNo = Integer.parseInt(arFieldNo[0]);
				ReportFilterValue filter = new ReportFilterValue();
			    filter.setFieldNo(iFieldNo);
			    if(arFieldType!=null) filter.setFieldType(arFieldType[0]);
			    if(arLookupTable!=null) filter.setLookupTable(arLookupTable[0]);
		        criNew.setFilter(filter);
		      }  
			  obj.add(criNew);
	          if(map.get("p" + i)!=null){
			    ReportTempCriValue criNew3 = new ReportTempCriValue();
				obj.add(criNew3);
				isInserted=true;
			  }
				
			}
			if(isInserted==false)
			{
		   	  ReportTempCriValue criNew4 = new ReportTempCriValue();
			  obj.add(criNew4);
			}
			break;
		}
		myForm.setTemplateCriteriaList(obj);
		request.getSession().setAttribute(DataViews.REPORT_CRI, obj);
		ReportValue rptVal = (ReportValue)request.getSession().getAttribute(DataViews.REPORT);
		if(rptVal.getReportTemp()!=null){
		  rptVal.getReportTemp().setTemplateCriteria(obj);
  		  request.getSession().setAttribute(DataViews.REPORT, rptVal);
		}
    }
	
}
