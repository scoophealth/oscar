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
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.crystaldecisions.report.web.viewer.CrPrintMode;
import com.crystaldecisions.report.web.viewer.CrystalReportViewer;
import com.crystaldecisions.reports.sdk.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.data.Fields;
import com.crystaldecisions.sdk.occa.report.data.ParameterField;
import com.crystaldecisions.sdk.occa.report.data.ParameterFieldDiscreteValue;
import com.crystaldecisions.sdk.occa.report.data.Values;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.crystaldecisions.sdk.occa.report.reportsource.IReportSource;
import com.quatro.model.DataViews;
import com.quatro.model.ReportFilterValue;
import com.quatro.model.ReportOptionValue;
import com.quatro.model.ReportTempCriValue;
import com.quatro.model.ReportTempValue;
import com.quatro.model.ReportValue;
import com.quatro.service.QuatroReportManager;
import com.quatro.util.Utility;

public class QuatroReportViewerAction extends Action {
	ReportValue _rptValue;
    ReportOptionValue _rptOption;
//	protected CrystalDecisions.CrystalReports.Engine.ReportDocument reportDocument1;

	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		QuatroReportRunnerForm myForm = (QuatroReportRunnerForm)form;
		Refresh(myForm, request, response);		
		ActionForward forward = mapping.findForward("donothing");
		return forward;
	}
	
	public void Refresh(QuatroReportRunnerForm myForm, HttpServletRequest request, HttpServletResponse response){
		
	    _rptValue = (ReportValue)request.getSession().getAttribute(DataViews.REPORT);
	    _rptOption = (ReportOptionValue)request.getSession().getAttribute(DataViews.REPORT_OPTION);
	    try{
			ReportTempValue rptTemp = _rptValue.getReportTemp();
	        int reportNo = rptTemp.getReportNo();
			String loginId = (String)request.getSession().getAttribute("user");
	        String datepartDis = "Date ";
	        if ("M".equals(_rptValue.getDatePart()))
	           datepartDis = "Month ";
	        else if ("Y".equals(_rptValue.getDatePart()))
	           datepartDis = "Year ";

			int templateNo = rptTemp.getTemplateNo();
			//prepare to run the report
 
            //1. get he report Value bean and date filter
            Date startDate = rptTemp.getStartDate();
            Date endDate = rptTemp.getEndDate();
            String dateRangeDis = "";
            if (!Utility.IsEmpty(_rptValue.getDateOption()))
                dateRangeDis = Utility.FormatDate(startDate);
            else
                dateRangeDis = Utility.FormatDate(startDate) + " - " + Utility.FormatDate(endDate);

            if (!"".equals(dateRangeDis)) dateRangeDis = datepartDis + dateRangeDis;

            String startPayPeriod = rptTemp.getStartPayPeriod();
			String endPayPeriod = rptTemp.getEndPayPeriod();
			String sPP = "";	

			if (!Utility.IsEmpty(startPayPeriod)  || !Utility.IsEmpty(endPayPeriod)){ 
				sPP = startPayPeriod + " - " + endPayPeriod;
			}else if (Utility.IsEmpty(startPayPeriod) && Utility.IsEmpty(endPayPeriod)){ 
				// do nothing
			}else if (Utility.IsEmpty(startPayPeriod)){
				sPP =  " - " + endPayPeriod;
			}else if (Utility.IsEmpty(endPayPeriod)){ 
				sPP = startPayPeriod + " - ";
			}
			
			if (!Utility.IsEmpty(sPP)) dateRangeDis = datepartDis + sPP;
			
			String optionId = String.valueOf(rptTemp.getReportOptionID());

			//2. get the org list (not include the users limitations)
	        String orgDis = "";
            String orgs = "";
            if (_rptValue.isOrgApplicable()) orgs = ""; //ConstructOrgStringCrysatal(out orgDis);
	                
            //3. Construct Criteria String
			String criteriaDis="";
			ArrayList lst=ConstructCriteriaStringCrystal(reportNo, criteriaDis);
			String criteria="";
			if(lst!=null){
			   criteria =(String)lst.get(0); 
			   criteriaDis=(String)lst.get(1);
			}
			
			QuatroReportManager qrManager = (QuatroReportManager)WebApplicationContextUtils.getWebApplicationContext(
	        		getServlet().getServletContext()).getBean("quatroReportManager");
			if ("D".equals(_rptValue.getDatePart())){
				qrManager.SetReportDate(request.getSession().getId(), startDate, endDate);
                if (!Utility.IsEmpty(_rptValue.getSptorun()))
                	qrManager.SetReportDateSp(_rptValue.getReportNo(), startDate, endDate, _rptValue.getSptorun());
				PaintReport(request, response, startDate, endDate, orgs, criteria, dateRangeDis, orgDis, criteriaDis);
            }else{
				qrManager.SetReportDate(request.getSession().getId(), startPayPeriod, endPayPeriod);
                if (!Utility.IsEmpty(_rptValue.getSptorun()))
                	qrManager.SetReportDateSp(_rptValue.getReportNo(), startDate, endDate, _rptValue.getSptorun());
               PaintReport(request, response, startPayPeriod, endPayPeriod, orgs, criteria, dateRangeDis, orgDis, criteriaDis);
            }
		}catch(Exception e){
			System.out.println("Validation Error Detected:<BR>" + e.toString());
		}

	}

    public ArrayList ConstructCriteriaStringCrystal(int reportNo,  String criteriaDis)
    {
        ReportTempValue rptTemp = _rptValue.getReportTemp();
        if(rptTemp==null) return null;
        ArrayList criterias = rptTemp.getTemplateCriteria();
        if (criterias.size() == 0) return null;

        String tableName = _rptValue.getTableName() + ".";
        String r_criteriaDis = "";
        
        ReportTempCriValue rptCri = (ReportTempCriValue)criterias.get(0);
		QuatroReportManager qrManager = (QuatroReportManager)WebApplicationContextUtils.getWebApplicationContext(
        		getServlet().getServletContext()).getBean("quatroReportManager");

        String criteriaSQL = "(";
        r_criteriaDis="(\n";

        String err = "";
        for (int i = 0; i < criterias.size(); i++)
        {
            rptCri = (ReportTempCriValue)criterias.get(i);
            String relation = rptCri.getRelation();
            int fieldNo = rptCri.getFieldNo();
            String op = rptCri.getOp();
            String val = rptCri.getVal();
            String valDesc = rptCri.getValDesc();
            if (Utility.IsEmpty(valDesc)) valDesc = val;

            if (!Utility.IsEmpty(relation))
            {
                if ("(".equals(criteriaSQL))
                {
                    if (relation.equals("("))
                    {
                        criteriaSQL += " " + relation + " ";
                    }
                    else
                    {
                        err += "Syntax Error detected, wrong relation at line " + String.valueOf(i + 1) + "<br>";
                    }
                }
                else
                {
                    criteriaSQL += " " + relation + " ";
                }
                r_criteriaDis += " " + relation + " ";
            }
            if (fieldNo > 0)
            {
                if (Utility.IsEmpty(relation) && !"(".equals(criteriaSQL))
                {
                    err += "Missing relations at line " + String.valueOf(i + 1) + "<br>";
                }
                if (Utility.IsEmpty(op))
                {
                    err += "Missing Operator at line " + String.valueOf(i + 1) + "<br>";
                }
                if (Utility.IsEmpty(val))
                {
                    err += "Missing Values at line " + String.valueOf(i + 1) + "<br>";
                }
                ReportFilterValue filter = qrManager.GetFilterField(reportNo, fieldNo);
                String FieldType = filter.getFieldType();
                if ("S".equals(FieldType)) {
                	criteriaSQL += "UpperCase({" + tableName + filter.getFieldSQL() + "})";
                }
                else
                {
                    criteriaSQL += "{" + tableName + filter.getFieldSQL() + "}";
                	
                }
                r_criteriaDis += filter.getFieldName();

                criteriaSQL += " " + op + " ";
                r_criteriaDis += " " + op + " ";

                if ("IN".equals(op))
                {
                    criteriaSQL += GetValueListCrystal(val, FieldType);
                    r_criteriaDis += "(" + val + ")";
                }
                else if ("LIKE".equals(op))
                {
                    criteriaSQL += "\"" + val + "\"";
                    r_criteriaDis +=  val;
                }
                else
                {
                    if ("D".equals(FieldType))
                    {
                        Date dateValue;
                        if ("TODAY".equals(val))
                        {
                            dateValue = new Date();
                        }
                        else
                        {
                        dateValue = Utility.GetSysDate(val);
                        }
                    	Calendar c1 = Calendar.getInstance();
                    	c1.setTime(dateValue);
                    	criteriaSQL += "CDATE(" + String.valueOf(c1.get(Calendar.YEAR)) + "," + String.valueOf(c1.get(Calendar.MONTH)+1) + "," + c1.get(Calendar.DAY_OF_MONTH) + ")";

                        r_criteriaDis += dateValue.toString();
                    }
                    else if ("S".equals(FieldType))
                    {
                        criteriaSQL += "\"" + val.toUpperCase() + "\"";
                        r_criteriaDis += "\"" + valDesc + "\"";
                    }
                    else
                    {
                        criteriaSQL += val;
                        r_criteriaDis += val;
                    }
                }
                r_criteriaDis += "\n";      // end of line
            }
        }

        criteriaSQL += ")";
        r_criteriaDis += ")";

        //are parenthes paired correctly?
        int nLeft = 0;
        for (int i = 0; i < criteriaSQL.length(); i++)
        {
            String c = criteriaSQL.substring(i, i+1);
            if ("(".equals(c)) nLeft++;
            if (")".equals(c)) nLeft--;
            if (nLeft < 0)
            {
                break;
            }
        }
        if (nLeft != 0)
        {
            err += "Syntax Error detected, right parenthes not match left parenthes";
        }

        if (!"".equals(err))
        {
//            throw new Exception(err);
        }

        if (r_criteriaDis.equals("()")) r_criteriaDis = "None";
        if (criteriaSQL.equals("()")) criteriaSQL = "";

        ArrayList lst= new ArrayList();
        lst.add(criteriaSQL);
        lst.add(r_criteriaDis);
        return lst;
    }
	
    public String GetValueListCrystal(String sValue, String sFieldType)
    {
        StringBuilder sResult = new StringBuilder();
        sResult.append("[");
        String[] sVals = sValue.split(",");
        for (int i = 0; i < sVals.length; i++)
        {
            if (i > 0) sResult.append(",");
            String s = sVals[i].trim();
            if ("D".equals(sFieldType))
            {
//                s = CRDate(Utility.GetSysDate(s), false);
            	Date dt=Utility.GetSysDate(s);
//                s = "CDATE(" + String.valueOf(dt.getYear()) + "," + String.valueOf(dt.getMonth()) + "," + String.valueOf(dt.getDay()) + ")";
            	Calendar c1 = Calendar.getInstance();
            	c1.setTime(dt);
            	s = "CDATE(" + String.valueOf(c1.get(Calendar.YEAR)) + "," + String.valueOf(c1.get(Calendar.MONTH)+1) + "," + c1.get(Calendar.DAY_OF_MONTH) + ")";
            }
            else if ("S".equals(sFieldType))
            {
                s = "\"" + s + "\"";
            }
            sResult.append(s);
        }
        sResult.append("]");
        return sResult.toString();
    }

    private String CRDate(Date dt,boolean isDateFieldString)
    {
        String sDt;
        if (isDateFieldString)
        {
            sDt = "'" + Utility.FormatDate(dt, "yyyy-mm-dd") + "'";
        }
        else
        {
        	Calendar c1 = Calendar.getInstance();
        	c1.setTime(dt);
        	sDt = "CDATE(" + String.valueOf(c1.get(Calendar.YEAR)) + "," + String.valueOf(c1.get(Calendar.MONTH)+1) + "," + c1.get(Calendar.DAY_OF_MONTH) + ")";
        }
        return sDt;
    }
    
    private void PaintReport(HttpServletRequest request, HttpServletResponse response, Date startDate, 
    		Date endDate, String orgs, String criteriaString, String dateRangeDis, String orgDis, String criteriaDis){

    	String loginId = (String)request.getSession().getAttribute("user");
        String userName = _rptValue.getAuthor();

        String sStartDate = Utility.FormatDate(startDate);
		String sEndDate = Utility.FormatDate(endDate);
        String sDateField = "{" + _rptValue.getTableName() + "." + _rptOption.getDateFieldName() + "}";
        String sDateFieldDesc = _rptOption.getDateFieldDesc();
        String sDateRange = "";
        String sDateSQL = "";
        
        boolean  isDateFieldString = "S".equals(_rptOption.getDateFieldType());

        String sDateOption=_rptValue.getDateOption();
        if(sDateOption.equals("A")){
           sDateRange = sDateFieldDesc + " As of : " + sStartDate;
           sDateSQL = sDateField + " = " + CRDate(startDate,isDateFieldString);
        }
        else if(sDateOption.equals("G")){  // greater than
           sDateRange = sDateFieldDesc + " Since : " + sStartDate;
           sDateSQL = sDateField + " >= " + CRDate(startDate,isDateFieldString);
        }
        else if(sDateOption.equals("L")){
           sDateRange = sDateFieldDesc + " Upto : " + sStartDate;
           sDateSQL = sDateField + " <= " + CRDate(startDate,isDateFieldString);
        }
        else if(sDateOption.equals("B")){
           if(startDate!=null && endDate!=null){
        	 sDateRange = sDateFieldDesc + " Range : " + sStartDate + " - " + sEndDate;
             sDateSQL = sDateField + " IN " + CRDate(startDate,isDateFieldString);
             sDateSQL = sDateSQL + " TO " + CRDate(endDate,isDateFieldString);
           }
        }
        
        if (!"".equals(sDateSQL)) {
           if (Utility.IsEmpty(criteriaString))
             criteriaString = sDateSQL;
           else
             criteriaString = sDateSQL + " AND " + criteriaString;
        }

        if (!Utility.IsEmpty(_rptOption.getDbsqlWhere())){
           if (Utility.IsEmpty(criteriaString))
             criteriaString = _rptOption.getDbsqlWhere();
           else
             criteriaString = "(" + _rptOption.getDbsqlWhere() + ")" + " AND " + criteriaString;
        }
        
        if (!Utility.IsEmpty(orgs)){
           if (Utility.IsEmpty(criteriaString))
             criteriaString = orgs;
           else
             criteriaString = "(" + orgs + ")" + " AND " + criteriaString;
        }

      ReportClientDocument reportDocument1 = new ReportClientDocument();
      String jspPath="";
      try{
         jspPath = getServlet().getServletContext().getResource("/").getPath();
      }catch(Exception ex){
    	  ;
      }
//      jspPath= OscarProperties.getInstance().getProperty("RPT_PATH");      
      String path=jspPath  + "PMmodule/reports/RptFiles/" + _rptOption.getRptFileName();
      if(path.substring(2, 3).equals(":")){  //for Windows System
      	path=path.substring(1);
      }
      
	  QuatroReportManager reportManager = (QuatroReportManager)WebApplicationContextUtils.getWebApplicationContext(
        		getServlet().getServletContext()).getBean("quatroReportManager");
      try{
	     reportManager.DownloadRptFile(path, _rptOption.getRptFileNo());
      }catch(Exception ex){
       	  ;
      }
      
      try{
          reportDocument1.open(path,0);
          if (!Utility.IsEmpty(criteriaString))  reportDocument1.setRecordSelectionFormula(criteriaString);
      }catch(ReportSDKException ex){
	      ReportSDKException ss=ex;
      }
      
      IReportSource reportSource = (IReportSource)reportDocument1.getReportSource();
  	  CrystalReportViewer crystalReportViewer = new CrystalReportViewer();
  	
      try{
  	      crystalReportViewer.setReportSource(reportSource);
  	
  	      Fields fields = crystalReportViewer.getParameterFields();
  	      Fields fields2 = new Fields();

     	  for (Iterator it = fields.iterator(); it.hasNext();){
     		ParameterField pfield = (ParameterField)it.next();
      	    String fieldName=pfield.getName().toLowerCase();
      	    String fieldName2=pfield.getName();
     		if(fieldName.equals("daterange")){
         	    ParameterFieldDiscreteValue pfieldDV1 = new ParameterFieldDiscreteValue();
        	    pfieldDV1.setValue(sDateRange);
         	    Values vals1 = new Values();
         	    vals1.add(pfieldDV1);
        	    ParameterField pfield1 = new ParameterField();
         	    pfield1.setName(fieldName2);
         	    pfield1.setCurrentValues(vals1);
         	    fields2.add(pfield1);
      	    }else if(fieldName.equals("organization")){
         	    ParameterFieldDiscreteValue pfieldDV2 = new ParameterFieldDiscreteValue();
        	    pfieldDV2.setValue(orgDis);
         	    Values vals2 = new Values();
         	    vals2.add(pfieldDV2);
        	    ParameterField pfield2 = new ParameterField();
         	    pfield2.setName(fieldName2);
         	    pfield2.setCurrentValues(vals2);
         	    fields2.add(pfield2);
      	    }else if(fieldName.equals("criteria")){
         	    ParameterFieldDiscreteValue pfieldDV3 = new ParameterFieldDiscreteValue();
        	    pfieldDV3.setValue(criteriaDis);
         	    Values vals3 = new Values();
         	    vals3.add(pfieldDV3);
        	    ParameterField pfield3 = new ParameterField();
         	    pfield3.setName(fieldName2);
         	    pfield3.setCurrentValues(vals3);
         	    fields2.add(pfield3);
      	    }else if(fieldName.equals("username")){
         	    ParameterFieldDiscreteValue pfieldDV4 = new ParameterFieldDiscreteValue();
        	    pfieldDV4.setValue(userName);
         	    Values vals4 = new Values();
         	    vals4.add(pfieldDV4);
        	    ParameterField pfield4 = new ParameterField();
         	    pfield4.setName(fieldName2);
         	    pfield4.setCurrentValues(vals4);
         	    fields2.add(pfield4);
      	    }else if(fieldName.equals("userid")){
         	    ParameterFieldDiscreteValue pfieldDV5 = new ParameterFieldDiscreteValue();
        	    pfieldDV5.setValue(loginId);
         	    Values vals5 = new Values();
         	    vals5.add(pfieldDV5);
        	    ParameterField pfield5 = new ParameterField();
         	    pfield5.setName(fieldName2);
         	    pfield5.setCurrentValues(vals5);
         	    fields2.add(pfield5);
      	    }else if(fieldName.equals("reporttitle")){
         	    ParameterFieldDiscreteValue pfieldDV6 = new ParameterFieldDiscreteValue();
        	    pfieldDV6.setValue(_rptValue.getTitle());
         	    Values vals6 = new Values();
         	    vals6.add(pfieldDV6);
        	    ParameterField pfield6 = new ParameterField();
         	    pfield6.setName(fieldName2);
         	    pfield6.setCurrentValues(vals6);
         	    fields2.add(pfield6);
      	    }else if(fieldName.equals("reporttitle2")){
         	    ParameterFieldDiscreteValue pfieldDV7 = new ParameterFieldDiscreteValue();
        	    pfieldDV7.setValue(_rptOption.getOptionTitle());
         	    Values vals7 = new Values();
         	    vals7.add(pfieldDV7);
        	    ParameterField pfield7 = new ParameterField();
         	    pfield7.setName(fieldName2);
         	    pfield7.setCurrentValues(vals7);
         	    fields2.add(pfield7);
      	    }else if(fieldName.equals("reporttitle3")){
      		    ParameterFieldDiscreteValue pfieldDV8 = new ParameterFieldDiscreteValue();
         	    if(_rptValue.getReportTemp()!=null){
        	       if(_rptValue.getReportTemp().getDesc()!=null)
         	    	 pfieldDV8.setValue(_rptValue.getReportTemp().getDesc());
        	       else
           	         pfieldDV8.setValue("");
         	    }else{
         	       pfieldDV8.setValue("");
         	    }
         	    Values vals8 = new Values();
         	    vals8.add(pfieldDV8);
        	    ParameterField pfield8 = new ParameterField();
         	    pfield8.setName(fieldName2);
         	    pfield8.setCurrentValues(vals8);
         	    fields2.add(pfield8);
      	    }else if(fieldName.equals("sessionid")){
         	    ParameterFieldDiscreteValue pfieldDV9 = new ParameterFieldDiscreteValue();
        	    pfieldDV9.setValue(request.getSession().getId());
         	    Values vals9 = new Values();
         	    vals9.add(pfieldDV9);
        	    ParameterField pfield9 = new ParameterField();
         	    pfield9.setName(fieldName2);
         	    pfield9.setCurrentValues(vals9);
         	    fields2.add(pfield9);
      	    }else if(fieldName.equals("p_userid")){
         	    ParameterFieldDiscreteValue pfieldDV10 = new ParameterFieldDiscreteValue();
        	    pfieldDV10.setValue(loginId);
         	    Values vals10 = new Values();
         	    vals10.add(pfieldDV10);
        	    ParameterField pfield10 = new ParameterField();
         	    pfield10.setName(fieldName2);
         	    pfield10.setCurrentValues(vals10);
         	    fields2.add(pfield10);
      	    }else if(fieldName.equals("p_orgs")){
         	    ParameterFieldDiscreteValue pfieldDV11 = new ParameterFieldDiscreteValue();
        	    pfieldDV11.setValue(orgs);
         	    Values vals11 = new Values();
         	    vals11.add(pfieldDV11);
        	    ParameterField pfield11 = new ParameterField();
         	    pfield11.setName(fieldName2);
         	    pfield11.setCurrentValues(vals11);
         	    fields2.add(pfield11);
      	    }else{
         	    if(fieldName2!=null){
      		       ParameterFieldDiscreteValue pfieldDV12 = new ParameterFieldDiscreteValue();
        	       pfieldDV12.setValue("");
         	       Values vals12 = new Values();
         	       vals12.add(pfieldDV12);
           	       ParameterField pfield12 = new ParameterField();
         	       pfield12.setName(fieldName2);
         	       pfield12.setCurrentValues(vals12);
         	       fields2.add(pfield12);
         	    }
      	    } 
     	  }
     	
          crystalReportViewer.setParameterFields(fields2);

   	      crystalReportViewer.setPrintMode(CrPrintMode.PDF);
  	      crystalReportViewer.setOwnPage(true);
  	      crystalReportViewer.setOwnForm(true);
  	      crystalReportViewer.setDisplayGroupTree(false);
  	      crystalReportViewer.setHasExportButton(false);
  	      crystalReportViewer.setHasSearchButton(false);
  	      crystalReportViewer.setHasPageBottomToolbar(false);
  	      crystalReportViewer.setHasRefreshButton(false);
  	      crystalReportViewer.setHasToggleGroupTreeButton(false);
  	      crystalReportViewer.setHasZoomFactorList(false);
  	      crystalReportViewer.setHasLogo(false);
  	      crystalReportViewer.setEnableDrillDown(false);

          switch (_rptValue.getExportFormatType()){
            case ReportExportFormat._PDF:
               crystalReportViewer.processHttpRequest(request, response, getServlet().getServletContext(), null);
               break;
            case ReportExportFormat._MSExcel:
          	   crystalReportViewer.processHttpRequest(request, response, getServlet().getServletContext(), null);
               break;
            case ReportExportFormat._MSWord:
          	   crystalReportViewer.processHttpRequest(request, response, getServlet().getServletContext(), null);
               break;
            case ReportExportFormat._text:
          	   crystalReportViewer.processHttpRequest(request, response, getServlet().getServletContext(), null);
               break;
            default:
          	   crystalReportViewer.processHttpRequest(request, response, getServlet().getServletContext(), null);
               break;
          }
          crystalReportViewer.dispose(); 

      }catch(Exception ex2) {
         System.out.println(ex2.toString());
      }    
        
   }
        
    private void PaintReport(HttpServletRequest request, HttpServletResponse response, String startPeriod, 
    		String endPeriod, String orgs, String criteriaString, String dateRangeDis, String orgDis, String criteriaDis){

    	String loginId = (String)request.getSession().getAttribute("user");
        String userName = _rptValue.getAuthor();

//        SetLogonInfo();
        
		String sDateField = "{" + _rptValue.getTableName() + "." + _rptOption.getDateFieldName() + "}";
        String sDateFieldDesc = _rptOption.getDateFieldDesc();
        if ("D".equals(_rptOption.getDateFieldType())){
          if ("M".equals(_rptValue.getDatePart()))
            sDateField = "CSTR(" + sDateField + ",\"yyyyMM\")";
          else if ("Y".equals(_rptValue.getDatePart()))
            sDateField = "CSTR(" + sDateField + ",\"yyyy\")";
        }
        String sDateRange = "";
        String sDateSQL = "";

        if(_rptValue.getDateOption().equals("A")){
           sDateRange = sDateFieldDesc + " As of : " + startPeriod;
           sDateSQL = sDateField + " = \"" + startPeriod + "\"";
        }else if(_rptValue.getDateOption().equals("G")){  // greater than
           sDateRange = sDateFieldDesc + " Since : " + startPeriod;
           sDateSQL = sDateField + " >= \"" + startPeriod + "\"";
        }else if(_rptValue.getDateOption().equals("L")){
           sDateRange = sDateFieldDesc + " Upto : " + startPeriod;
           sDateSQL = sDateField + " <= \"" + startPeriod + "\"";
        }else if(_rptValue.getDateOption().equals("B")){
           sDateRange = sDateFieldDesc + " Range : " + startPeriod + " - " + endPeriod;
           sDateSQL = sDateField + " IN \"" + startPeriod + "\"";
           sDateSQL = sDateSQL + " TO " + "\"" + endPeriod + "\"";
        }
        
        if(!"".equals(sDateSQL)){
          if(Utility.IsEmpty(criteriaString))
            criteriaString = sDateSQL;
          else
            criteriaString = sDateSQL + " AND " + criteriaString;
        }

        if(!Utility.IsEmpty(_rptOption.getDbsqlWhere())){
          if(Utility.IsEmpty(criteriaString))
            criteriaString = _rptOption.getDbsqlWhere();
         else
            criteriaString = "(" + _rptOption.getDbsqlWhere() + ")" + " AND " + criteriaString;
        }

        if(!Utility.IsEmpty(orgs)){
          if(Utility.IsEmpty(criteriaString))
            criteriaString = orgs;
          else
            criteriaString = "(" + orgs + ")" + " AND " + criteriaString;
        }

        ReportClientDocument reportDocument1 = new ReportClientDocument();
        String jspPath="";
        try{
           jspPath = getServlet().getServletContext().getResource("/").getPath();
        }catch(Exception ex){
      	  ;
        }
//        jspPath= OscarProperties.getInstance().getProperty("RPT_PATH");      
        String path=jspPath  + "PMmodule/reports/RptFiles/" + _rptOption.getRptFileName();
        if(path.substring(2, 3).equals(":")){  //for Windows System
        	path=path.substring(1);
        }
        
  	    QuatroReportManager reportManager = (QuatroReportManager)WebApplicationContextUtils.getWebApplicationContext(
      		getServlet().getServletContext()).getBean("quatroReportManager");
        try{
	        reportManager.DownloadRptFile(path, _rptOption.getRptFileNo());
        }catch(Exception ex){
     	  ;
        }
        
        try{
             reportDocument1.open(path,0);
             if (!Utility.IsEmpty(criteriaString)) reportDocument1.setRecordSelectionFormula(criteriaString);
        }catch(ReportSDKException ex){
	        ReportSDKException ss=ex;
        }
        
        IReportSource reportSource = (IReportSource)reportDocument1.getReportSource();
    	CrystalReportViewer crystalReportViewer = new CrystalReportViewer();
    	
        try{
    	    crystalReportViewer.setReportSource(reportSource);
    	    Fields fields = crystalReportViewer.getParameterFields();
    	    Fields fields2 = new Fields();

       	    for (Iterator it = fields.iterator(); it.hasNext();){
       		  ParameterField pfield = (ParameterField)it.next();
        	  String fieldName=pfield.getName().toLowerCase();
        	  String fieldName2=pfield.getName();
       		  if(fieldName.equals("daterange")){
           	     ParameterFieldDiscreteValue pfieldDV1 = new ParameterFieldDiscreteValue();
          	     pfieldDV1.setValue(sDateRange);
           	     Values vals1 = new Values();
           	     vals1.add(pfieldDV1);
          	     ParameterField pfield1 = new ParameterField();
           	     pfield1.setName(fieldName2);
           	     pfield1.setCurrentValues(vals1);
           	     fields2.add(pfield1);
        	  }else if(fieldName.equals("organization")){
           	     ParameterFieldDiscreteValue pfieldDV2 = new ParameterFieldDiscreteValue();
          	     pfieldDV2.setValue(orgDis);
           	     Values vals2 = new Values();
           	     vals2.add(pfieldDV2);
          	     ParameterField pfield2 = new ParameterField();
           	     pfield2.setName(fieldName2);
           	     pfield2.setCurrentValues(vals2);
           	     fields2.add(pfield2);
        	  }else if(fieldName.equals("criteria")){
           	     ParameterFieldDiscreteValue pfieldDV3 = new ParameterFieldDiscreteValue();
          	     pfieldDV3.setValue(criteriaDis);
           	     Values vals3 = new Values();
           	     vals3.add(pfieldDV3);
          	     ParameterField pfield3 = new ParameterField();
           	     pfield3.setName(fieldName2);
           	     pfield3.setCurrentValues(vals3);
           	     fields2.add(pfield3);
        	  }else if(fieldName.equals("username")){
           	     ParameterFieldDiscreteValue pfieldDV4 = new ParameterFieldDiscreteValue();
          	     pfieldDV4.setValue(userName);
           	     Values vals4 = new Values();
           	     vals4.add(pfieldDV4);
          	     ParameterField pfield4 = new ParameterField();
           	     pfield4.setName(fieldName2);
           	     pfield4.setCurrentValues(vals4);
           	     fields2.add(pfield4);
        	  }else if(fieldName.equals("userid")){
           	     ParameterFieldDiscreteValue pfieldDV5 = new ParameterFieldDiscreteValue();
          	     pfieldDV5.setValue(loginId);
           	     Values vals5 = new Values();
           	     vals5.add(pfieldDV5);
          	     ParameterField pfield5 = new ParameterField();
           	     pfield5.setName(fieldName2);
           	     pfield5.setCurrentValues(vals5);
           	     fields2.add(pfield5);
        	  }else if(fieldName.equals("reporttitle")){
           	     ParameterFieldDiscreteValue pfieldDV6 = new ParameterFieldDiscreteValue();
          	     pfieldDV6.setValue(_rptValue.getTitle());
           	     Values vals6 = new Values();
           	     vals6.add(pfieldDV6);
          	     ParameterField pfield6 = new ParameterField();
           	     pfield6.setName(fieldName2);
           	     pfield6.setCurrentValues(vals6);
           	     fields2.add(pfield6);
        	  }else if(fieldName.equals("reporttitle2")){
           	     ParameterFieldDiscreteValue pfieldDV7 = new ParameterFieldDiscreteValue();
          	     pfieldDV7.setValue(_rptOption.getOptionTitle());
           	     Values vals7 = new Values();
           	     vals7.add(pfieldDV7);
          	     ParameterField pfield7 = new ParameterField();
           	     pfield7.setName(fieldName2);
           	     pfield7.setCurrentValues(vals7);
           	     fields2.add(pfield7);
        	  }else if(fieldName.equals("reporttitle3")){
        		 ParameterFieldDiscreteValue pfieldDV8 = new ParameterFieldDiscreteValue();
          	     if(_rptValue.getReportTemp()!=null){
         	       if(_rptValue.getReportTemp().getDesc()!=null)
          	    	 pfieldDV8.setValue(_rptValue.getReportTemp().getDesc());
         	       else
            	     pfieldDV8.setValue("");
          	     }else{
          	       pfieldDV8.setValue("");
          	     }
           	     Values vals8 = new Values();
           	     vals8.add(pfieldDV8);
          	     ParameterField pfield8 = new ParameterField();
           	     pfield8.setName(fieldName2);
           	     pfield8.setCurrentValues(vals8);
           	     fields2.add(pfield8);
        	  }else if(fieldName.equals("sessionid")){
           	     ParameterFieldDiscreteValue pfieldDV9 = new ParameterFieldDiscreteValue();
          	     pfieldDV9.setValue(request.getSession().getId());
           	     Values vals9 = new Values();
           	     vals9.add(pfieldDV9);
          	     ParameterField pfield9 = new ParameterField();
           	     pfield9.setName(fieldName2);
           	     pfield9.setCurrentValues(vals9);
           	     fields2.add(pfield9);
        	  }else if(fieldName.equals("p_userid")){
           	     ParameterFieldDiscreteValue pfieldDV10 = new ParameterFieldDiscreteValue();
          	     pfieldDV10.setValue(loginId);
           	     Values vals10 = new Values();
           	     vals10.add(pfieldDV10);
          	     ParameterField pfield10 = new ParameterField();
           	     pfield10.setName(fieldName2);
           	     pfield10.setCurrentValues(vals10);
           	     fields2.add(pfield10);
        	  }else if(fieldName.equals("p_orgs")){
           	     ParameterFieldDiscreteValue pfieldDV11 = new ParameterFieldDiscreteValue();
          	     pfieldDV11.setValue(orgs);
           	     Values vals11 = new Values();
           	     vals11.add(pfieldDV11);
          	     ParameterField pfield11 = new ParameterField();
           	     pfield11.setName(fieldName2);
           	     pfield11.setCurrentValues(vals11);
           	     fields2.add(pfield11);
        	  }else{
           	     if(fieldName2!=null){
        		   ParameterFieldDiscreteValue pfieldDV12 = new ParameterFieldDiscreteValue();
          	       pfieldDV12.setValue("");
           	       Values vals12 = new Values();
           	       vals12.add(pfieldDV12);
             	   ParameterField pfield12 = new ParameterField();
           	       pfield12.setName(fieldName2);
           	       pfield12.setCurrentValues(vals12);
           	       fields2.add(pfield12);
           	     }
        	  } 
       	  }
       	
          crystalReportViewer.setParameterFields(fields2);
     	  crystalReportViewer.setPrintMode(CrPrintMode.PDF);
    	  crystalReportViewer.setOwnPage(true);
    	  crystalReportViewer.setOwnForm(true);
    	  crystalReportViewer.setDisplayGroupTree(false);
    	  crystalReportViewer.setHasExportButton(false);
    	  crystalReportViewer.setHasSearchButton(false);
    	  crystalReportViewer.setHasPageBottomToolbar(false);
    	  crystalReportViewer.setHasRefreshButton(false);
    	  crystalReportViewer.setHasToggleGroupTreeButton(false);
    	  crystalReportViewer.setHasZoomFactorList(false);
    	  crystalReportViewer.setHasLogo(false);
    	  crystalReportViewer.setEnableDrillDown(false);

/*
    	ByteArrayInputStream bais = (ByteArrayInputStream) reportDocument1.getPrintOutputController().export(ReportExportFormat.PDF);
    	byte[] bytes = new byte[bais.available()];
    	bais.read(bytes, 0, bais.available());
//    	FacesContext faces = FacesContext.getCurrentInstance();
//    	HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
    	response.setContentType("application/pdf");
    	response.setContentLength(bytes.length);
    	response.setHeader( "Content-disposition", "inline; filename=report.pdf");
    	response.getOutputStream().write(bytes);
//    	faces.responseComplete();
*/
    	
          switch (_rptValue.getExportFormatType()){
            case ReportExportFormat._PDF:
          	  crystalReportViewer.processHttpRequest(request, response, getServlet().getServletContext(), null);
              break;
            case ReportExportFormat._MSExcel:
           	  crystalReportViewer.processHttpRequest(request, response, getServlet().getServletContext(), null);
              break;
            case ReportExportFormat._MSWord:
           	  crystalReportViewer.processHttpRequest(request, response, getServlet().getServletContext(), null);
              break;
            case ReportExportFormat._text:
          	  crystalReportViewer.processHttpRequest(request, response, getServlet().getServletContext(), null);
              break;
            default:
           	  crystalReportViewer.processHttpRequest(request, response, getServlet().getServletContext(), null);
              break;
          }
          crystalReportViewer.dispose(); 

      }catch(Exception ex2) {
         System.out.println(ex2.toString());
      }    

   }
	
}
