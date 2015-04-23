/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.oscarReport.ClinicalReports.PageUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.KeyValue;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarReport.ClinicalReports.ClinicalReportManager;
import oscar.oscarReport.ClinicalReports.Denominator;
import oscar.oscarReport.ClinicalReports.Numerator;
import oscar.oscarReport.ClinicalReports.ReportEvaluator;
public class RunClinicalReportAction extends Action {
    
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    /** Creates a new instance of RunClinicalReportAction */
    public RunClinicalReportAction() {
    }
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
        throws IOException, ServletException {
        
    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_report", "r", null)) {
  		  throw new SecurityException("missing required security object (_report)");
  	  }
    	
        String numeratorId = request.getParameter("numerator");
        String denominatorId = request.getParameter("denominator");
         
        MiscUtils.getLogger().debug("numerator "+numeratorId+" denominator "+denominatorId);    
        ClinicalReportManager reports = ClinicalReportManager.getInstance();
    
        Denominator d = reports.getDenominatorById(denominatorId);        
        if (d.hasReplaceableValues()){
            String[] denomReplaceKeys = d.getReplaceableKeys();
            Hashtable h = new Hashtable();
            String keyValue;
            if ( denomReplaceKeys != null){
                for (int i = 0; i < denomReplaceKeys.length; i++){
                    String[] keyValues = request.getParameterValues("denominator_"+denomReplaceKeys[i]);
                    if (keyValues == null) {
                        keyValue = "";                        
                    }
                    if( keyValues.length == 1 ) {
                        h.put(denomReplaceKeys[i],keyValues[0]);                       
                    }
                    else {
                        for( int idx = 0; idx < keyValues.length; ++idx ) {
                            h.put(denomReplaceKeys[i] + String.valueOf(idx), keyValues[idx]);
                        }
                    }
                }
            }
            MiscUtils.getLogger().debug("setting replaceable values with a size of "+h.size());
            d.setReplaceableValues(h);
        }
        
        
        
        
        
       
        Numerator   n = reports.getNumeratorById(numeratorId);
        MiscUtils.getLogger().debug("n"+n+" "+n.hasReplaceableValues());
        if (n.hasReplaceableValues()){
            String[] denomReplaceKeys = n.getReplaceableKeys();
            Hashtable h = new Hashtable();
            String keyValue;
            if ( denomReplaceKeys != null){
                for (int i = 0; i < denomReplaceKeys.length; i++){
                    MiscUtils.getLogger().debug("The sought after key would be "+request.getParameterValues("numerator_"+denomReplaceKeys[i]) );
                    String[] keyValues = request.getParameterValues("numerator_"+denomReplaceKeys[i]);
                    if (keyValues == null) {
                        keyValue = "";                        
                    }
                    if( keyValues.length == 1 ) {
                        h.put(denomReplaceKeys[i],keyValues[0]);   
                        request.setAttribute("numerator_"+denomReplaceKeys[i],request.getParameter("numerator_"+denomReplaceKeys[i]));
                    }
                    else {
                        for( int idx = 0; idx < keyValues.length; ++idx ) {
                            h.put(denomReplaceKeys[i] + String.valueOf(idx), keyValues[idx]);
                        }
                    }
                }
            }
            MiscUtils.getLogger().debug("setting replaceable values with a size of "+h.size());
            n.setReplaceableValues(h);
        }
        
        
        
        
        List<KeyValue> extraVal = null;
        for(Object params: request.getParameterMap().keySet()){
            String requestParam = (String) params;
            if(requestParam.startsWith("report_measurement") && !request.getParameter(requestParam).equals("-1") ){
                if(extraVal ==null){
                    extraVal = new LinkedList();
                }
                    KeyValue kv = new org.apache.commons.collections.keyvalue.DefaultKeyValue(requestParam, request.getParameter(requestParam));
                    extraVal.add(kv);
                    request.setAttribute(requestParam, request.getParameter(requestParam));
                 
            }
        }
        
        
        request.setAttribute("showfields",request.getParameterValues("showfields"));
       
        //Need to change the out put fields here On the JSP use an getDisplay MEthod that checks for instance of.
        
        
        
        ReportEvaluator re  = new ReportEvaluator();
        re.evaluate(LoggedInInfo.getLoggedInInfoFromSession(request), d,n,extraVal);
        
        int num = re.getNumeratorCount();
        int denom = re.getDenominatorCount();
        MiscUtils.getLogger().debug("num "+num+" denom "+denom);
        float percentage = re.getPercentage();
        
        
        ArrayList arrList =  (ArrayList)  request.getSession().getAttribute("ClinicalReports");
        if (arrList == null){
            arrList = new ArrayList();
        }
        arrList.add(re);
        request.getSession().setAttribute("ClinicalReports",arrList);
        
        request.setAttribute("extraValues",extraVal);
        request.setAttribute("name",re.getName());
        request.setAttribute("numerator",Integer.toString(num));
        request.setAttribute("denominator",Integer.toString(denom));
        request.setAttribute("numeratorId",numeratorId);
        request.setAttribute("denominatorId",denominatorId);
        request.setAttribute("percentage",Integer.toString(new Float(percentage).intValue()));
        request.setAttribute("csv",re.getCSV());
        request.setAttribute("list",re.getReportResultList());
        request.setAttribute("outputfields",n.getOutputFields());
        return mapping.findForward("success");
     }    
}
