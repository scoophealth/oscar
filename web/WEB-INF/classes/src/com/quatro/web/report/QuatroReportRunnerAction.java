package com.quatro.web.report;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.quatro.model.*;
import com.quatro.service.*;
import com.quatro.util.*;

import java.util.ArrayList;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import java.util.*;

public class QuatroReportRunnerAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String param=(String)request.getParameter("id");
		QuatroReportRunnerForm myForm = (QuatroReportRunnerForm)form;
		int rptNo;
        String loginId = "999998";

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
	  	    String[] strTemp = param.split("_");
	        rptNo = Integer.parseInt(strTemp[0]);
            int templateNo = 0;
	        
            myForm.setReportNo(strTemp[0]);
	        if (strTemp.length > 1)
	        {
	            templateNo = Integer.parseInt(strTemp[1]);
	            Refresh(myForm, loginId, rptNo, templateNo, request);
	        }
	        else
	        {
	            Refresh(myForm, loginId, rptNo, request);
	        }
			ActionForward forward = mapping.findForward("success");
			return forward;
		}
		//postback form
        else
		{
			rptNo=Integer.parseInt(myForm.getReportNo());
            //
			if((String)request.getParameter("Run")!=null)
			{
				btnRun_Click(rptNo, myForm, request);
			}
			else if((String)request.getParameter("AddTplCri")!=null)
			{
				btnAddTplCri_Click(rptNo, myForm, request);
			}
			else if((String)request.getParameter("InsertTplCri")!=null)
			{
				btnInsertTplCri_Click(rptNo, myForm, request);
			}
			else if((String)request.getParameter("RemoveTplCri")!=null)
			{
				btnRemoveTplCri_Click(rptNo, myForm, request);
			}
			else if(!((String)request.getParameter("onCriteriaChange")).equals(""))
			{
				OnCriteriaTextChangedHandler(rptNo, myForm, request);
			}
		    Refresh(myForm, loginId, rptNo, request);
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
		
		ArrayList cris = (ArrayList) request.getSession().getAttribute(DataViews.REPORT_CRI);
		ReportTempCriValue rptCri = new ReportTempCriValue();

		ReportFilterValue rptFilterVal = null;
		ReportService rpt = null;

		Map map=request.getParameterMap();
        LookupService lk = new LookupService();
        try{
        LookupCodeValue lkv = lk.GetCode("REPT", "");
        }catch(Exception ex){}
		switch (col)
		{
			case 1:
                String[] arRelation=(String[])map.get("tplCriteria[" + row + "].relation");
                rptCri.setRelation(arRelation[0]);
				break;
			case 2:
                String[] arFieldNo=(String[])map.get("tplCriteria[" + row + "].fieldNo");
                int fieldNo= 0;
                if(arFieldNo!=null) fieldNo=Integer.parseInt(arFieldNo[0]);

				rptCri.setFieldNo(fieldNo);
				if (fieldNo == 0) 
				{
					return;
				}
				rpt = new ReportService(reportNo);
				rptFilterVal = rpt.GetFilterField(rptCri.getFieldNo());
				rptCri.setOperatorList(GetOperatorList(rptFilterVal.getOp()));

                if (!Utility.IsEmpty(rptFilterVal.getLookupTable()))
                {
                	rptCri.setLookupTable(rptFilterVal.getLookupTable());
                }
                
				break;
			case 3:  //operator
  			    String[] arOp=(String[])map.get("tplCriteria[" + row + "].op");
			    if(arOp!=null){
			    	rptCri.setOp(arOp[0]);
			    }
			    else
			    {
			    	rptCri.setOp("");
			    }
                break;
			case 4:
				break;
		}
		request.getSession().setAttribute(DataViews.REPORT_CRI, cris);
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
//		myForm.setFilterFields(filterFields);
		ChangeTplCriTable(2, myForm, request);
    }

	public void btnRun_Click(int reportNo, QuatroReportRunnerForm myForm, HttpServletRequest request)
	{
        ReportValue rptVal = (ReportValue)request.getSession().getAttribute(DataViews.REPORT);
        try
        {
        	ReportTempValue rptTempVal= BuildTemplate(myForm, request); 
            rptVal.setReportTemp(rptTempVal);
            
            rptVal.setExportFormatType(Integer.parseInt(myForm.getExportFormat()));
            rptVal.setPrint2Pdf(false); // this property is kept only for the customized prints 

            int optionIdx = Integer.parseInt(myForm.getReportOption());
            ReportOptionValue option = (ReportOptionValue)rptVal.getOptions().get(optionIdx-1);

            String path = DataViews.RptFiles;
            String rptFilePath = path + "/" + option.getRptFileName();

            ReportService reportManager = new ReportService(rptVal.getReportNo());

    		request.getSession().setAttribute(DataViews.REPORTTPL, rptTempVal);
    		request.getSession().setAttribute(DataViews.REPORT_OPTION, option);

   		    myForm.setStrClientJavascript("showReport();");
        }
        catch (Exception ex)
        {
           ;
        }
        
	}
	
	public void Refresh(QuatroReportRunnerForm myForm, String loginId,int reportNo, int templateNo, HttpServletRequest request)
	{
		
	}
	
	public void Refresh(QuatroReportRunnerForm myForm, String loginId,int reportNo, HttpServletRequest request)
	{
		if(request.getSession()!=null) request.getSession().removeAttribute(DataViews.REPORT_CRI);
		
		QuatroReportManager reportManager = (QuatroReportManager) WebApplicationContextUtils.getWebApplicationContext(
 	       		pageContext.getServletContext()).getBean("quatroReportManagerTarget");
		ReportValue rptVal = reportManager.GetReport(reportNo, loginId);

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
		}
		else
		{
			obj5.setVisible("visibility:hidden;height=1px;");
			ArrayList<KeyValueBean> lst= new ArrayList<KeyValueBean>();
			lst.add(new KeyValueBean("Not Applicable", "Not Applicable"));
			myForm.setOrgSelectionList(lst);
		}
		myForm.setOrgSelectionProperty(obj5);

		ArrayList rptOptions = rptVal.getOptions();
		myForm.setReportOptionList(rptOptions);
       	for (Iterator it = rptOptions.iterator(); it.hasNext();){
        	ReportOptionValue rv = (ReportOptionValue)it.next();
        	if(rv.isBdefault()){
    		  myForm.setReportOption(String.valueOf(rv.getOptionNo()));
        	  break;
        	} 
       	}

		RefreshCriteria(myForm, reportNo);
	}

	private void RefreshCriteria(QuatroReportRunnerForm myForm, int reportNo)
	{
		ReportService reportManager = new ReportService(reportNo);
		myForm.setFilterFields((ArrayList<ReportFilterValue>)reportManager.GetCriteriaFieldList());
	}
	
	private ReportTempValue BuildTemplate(QuatroReportRunnerForm myForm, HttpServletRequest request) throws Exception
    {
        ReportValue rptVal = (ReportValue)request.getSession().getAttribute(DataViews.REPORT);
        int reportNo = rptVal.getReportNo();

        ReportTempValue repTemp = new ReportTempValue();
        repTemp.setStartDate(Utility.GetSysDate(myForm.getStartDate()));
        repTemp.setEndDate(Utility.GetSysDate(myForm.getEndDate()));
        repTemp.setReportNo(reportNo);
        return repTemp;
    }

	private ArrayList GetOperatorList(String ops)
	{
		ArrayList<KeyValueBean> operators = new ArrayList<KeyValueBean>();
		if (Utility.IsEmpty(ops)) return operators;
		for(int i=0; i<ops.length(); i++)
		{
			String op = ops.substring(i,1);
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

	private String GetLookupTable(int fieldNo, ArrayList filterFields)
	{
		String lk = "";
		for (int i=0; i<filterFields.size(); i++) 
		{
			ReportFilterValue rptFilter = (ReportFilterValue) filterFields.get(i);
			if (rptFilter.getFieldNo() == fieldNo) 
			{					
				lk = rptFilter.getLookupTable();
                break;
            }
		}
		return lk;
	}
	
    public void ChangeTplCriTable(int operationType, QuatroReportRunnerForm myForm, HttpServletRequest request)
    {
    	ArrayList<ReportTempCriValue> obj= new ArrayList<ReportTempCriValue>();
		Map map=request.getParameterMap();
		String[] obj2= (String[])map.get("lineno");
		int lineno=0;
		if(obj2!=null) lineno=obj2.length;
		ReportService reportManager = new ReportService(Integer.parseInt(myForm.getReportNo()));

		switch(operationType)
		{
		  case 1:  //remove
		    for(int i=0;i<lineno;i++)
		    {
	          if(map.get("p" + i)!=null) continue;
		      ReportTempCriValue criNew = new ReportTempCriValue();
	          String[] arRelation=(String[])map.get("tplCriteria[" + i + "].relation");
	          String[] arFieldNo=(String[])map.get("tplCriteria[" + i + "].fieldNo");
	          String[] arOp=(String[])map.get("tplCriteria[" + i + "].op");
	          String[] arVal=(String[])map.get("tplCriteria[" + i + "].val");
	          String[] arLookupTable=(String[])map.get("tplCriteria[" + i + "].lookupTable");
		      if(arFieldNo!=null){
				int iFieldNo = Integer.parseInt(arFieldNo[0]);
				ReportFilterValue rptFilterVal = reportManager.GetFilterField(iFieldNo);
				if(rptFilterVal!=null) criNew.setOperatorList(GetOperatorList(rptFilterVal.getOp()));
				criNew.setFieldNo(iFieldNo);
			  }
		      if(arRelation!=null) criNew.setRelation(arRelation[0]);
		      if(arOp!=null) criNew.setOp(arOp[0]);
		      if(arVal!=null) criNew.setVal(arVal[0]);
		      if(arLookupTable!=null) criNew.setLookupTable(arLookupTable[0]);
		      obj.add(criNew);
		    }
		    break;
		  case 2:  //add
			for(int i=0;i<lineno;i++)
			{
			  ReportTempCriValue criNew = new ReportTempCriValue();
			  String[] arRelation=(String[])map.get("tplCriteria[" + i + "].relation");
			  String[] arFieldNo=(String[])map.get("tplCriteria[" + i + "].fieldNo");
			  String[] arOp=(String[])map.get("tplCriteria[" + i + "].op");
			  String[] arVal=(String[])map.get("tplCriteria[" + i + "].val");
	          String[] arLookupTable=(String[])map.get("tplCriteria[" + i + "].lookupTable");
		      if(arFieldNo!=null){
				int iFieldNo = Integer.parseInt(arFieldNo[0]);
				ReportFilterValue rptFilterVal = reportManager.GetFilterField(iFieldNo);
				if(rptFilterVal!=null) criNew.setOperatorList(GetOperatorList(rptFilterVal.getOp()));
				criNew.setFieldNo(iFieldNo);
			  }
			  if(arRelation!=null) criNew.setRelation(arRelation[0]);
			  if(arOp!=null) criNew.setOp(arOp[0]);
			  if(arVal!=null) criNew.setVal(arVal[0]);
		      if(arLookupTable!=null) criNew.setLookupTable(arLookupTable[0]);
			  obj.add(criNew);
			}
			ReportTempCriValue criNew2 = new ReportTempCriValue();
			obj.add(criNew2);
			break;
		  case 3:  //insert
			boolean isInserted=false;
			for(int i=0;i<lineno;i++)
			{
			  ReportTempCriValue criNew = new ReportTempCriValue();
			  String[] arRelation=(String[])map.get("tplCriteria[" + i + "].relation");
			  String[] arFieldNo=(String[])map.get("tplCriteria[" + i + "].fieldNo");
			  String[] arOp=(String[])map.get("tplCriteria[" + i + "].op");
			  String[] arVal=(String[])map.get("tplCriteria[" + i + "].val");
	          String[] arLookupTable=(String[])map.get("tplCriteria[" + i + "].lookupTable");
		      if(arFieldNo!=null){
				int iFieldNo = Integer.parseInt(arFieldNo[0]);
				ReportFilterValue rptFilterVal = reportManager.GetFilterField(iFieldNo);
				if(rptFilterVal!=null) criNew.setOperatorList(GetOperatorList(rptFilterVal.getOp()));
				criNew.setFieldNo(iFieldNo);
			  }
			  if(arRelation!=null) criNew.setRelation(arRelation[0]);
			  if(arOp!=null) criNew.setOp(arOp[0]);
			  if(arVal!=null) criNew.setVal(arVal[0]);
		      if(arLookupTable!=null) criNew.setLookupTable(arLookupTable[0]);
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
		  case 4:  //OnCriteriaTextChanged
			ArrayList<ReportFilterValue> filterFields=(ArrayList<ReportFilterValue>)reportManager.GetCriteriaFieldList();
			myForm.setFilterFields(filterFields);

			String sTemp1=(String)request.getParameter("onCriteriaChange");
			sTemp1 = sTemp1.replaceAll("tplCriteria", "");
			sTemp1 = sTemp1.replaceAll(".fieldNo", "");
			String sTemp2= sTemp1.substring(1, sTemp1.length()-1);
			int iCriChange= Integer.parseInt(sTemp2);
			for(int i=0;i<lineno;i++)
			{
			  ReportTempCriValue criNew = new ReportTempCriValue();
			  String[] arRelation=(String[])map.get("tplCriteria[" + i + "].relation");
			  String[] arFieldNo=(String[])map.get("tplCriteria[" + i + "].fieldNo");
			  String[] arOp=(String[])map.get("tplCriteria[" + i + "].op");
			  String[] arVal=(String[])map.get("tplCriteria[" + i + "].val");
		      if(arFieldNo!=null){
				int iFieldNo = Integer.parseInt(arFieldNo[0]);
			    ReportFilterValue rptFilterVal = reportManager.GetFilterField(iFieldNo);
				if(rptFilterVal!=null) criNew.setOperatorList(GetOperatorList(rptFilterVal.getOp()));
				criNew.setFieldNo(iFieldNo);
		      }
			  if(i==iCriChange){
		        if(arFieldNo!=null){
				  String arLookupTable=GetLookupTable(Integer.parseInt(arFieldNo[0]), filterFields);
			      if(arLookupTable!=null) criNew.setLookupTable(arLookupTable);
		        }
			  }
			  else
			  {
				String[] arLookupTable=(String[])map.get("tplCriteria[" + i + "].lookupTable");
		        if(arLookupTable!=null) criNew.setLookupTable(arLookupTable[0]);
			  }
			  
			  if(arRelation!=null) criNew.setRelation(arRelation[0]);
			  if(arOp!=null) criNew.setOp(arOp[0]);
			  if(arVal!=null) criNew.setVal(arVal[0]);
			  obj.add(criNew);
			}
			break;
		}
		myForm.setTemplateCriteriaList(obj);
    }
	
}
