package oscar.oscarBilling.pageUtil;
import oscar.oscarDB.DBHandler;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

public class BillingCreateBillingAction extends Action {
    
    
    public ActionForward perform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
        
        oscar.oscarBilling.pageUtil.BillingSessionBean bean;
        bean = (oscar.oscarBilling.pageUtil.BillingSessionBean)request.getSession().getAttribute("billingSessionBean");
        String patientNo   = bean.getPatientNo();
        String patientName = bean.getPatientName();
        String apptNo     = bean.getApptNo();
        
        String[] service  = ((BillingCreateBillingForm)form).getService();
        String other_service1 = ((BillingCreateBillingForm)form).getXml_other1();
        String other_service2 = ((BillingCreateBillingForm)form).getXml_other2();
        String other_service3 = ((BillingCreateBillingForm)form).getXml_other3();
        String other_service1_unit = ((BillingCreateBillingForm)form).getXml_other1_unit();
        String other_service2_unit = ((BillingCreateBillingForm)form).getXml_other2_unit();
        String other_service3_unit = ((BillingCreateBillingForm)form).getXml_other3_unit();
        oscar.oscarBilling.pageUtil.BillingBillingManager bmanager;
        bmanager = new BillingBillingManager();
        ArrayList billItem = bmanager.getDups(service, other_service1, other_service2, other_service3,other_service1_unit, other_service2_unit, other_service3_unit);
        System.out.println("Calling getGrandTotal");
        
        bean.setGrandtotal(bmanager.getGrandTotal(billItem));
        System.out.println("GrandTotal" +bmanager.getGrandTotal(billItem));
        oscar.oscarDemographic.data.DemographicData demoData = new oscar.oscarDemographic.data.DemographicData();
        
        oscar.oscarDemographic.data.DemographicData.Demographic demo = demoData.getDemographic(bean.getPatientNo());
        bean.setPatientLastName(demo.getLastName());
        bean.setPatientFirstName(demo.getFirstName());
        bean.setPatientDoB(demo.getDob());
        bean.setPatientAddress1(demo.getAddress());
        bean.setPatientAddress2(demo.getCity());
        bean.setPatientPostal(demo.getPostal());
        bean.setPatientSex(demo.getSex());
        bean.setPatientPHN(demo.getHIN());
        bean.setPatientHCType(demo.getHCType());
        bean.setPatientAge(demo.getAge());
        bean.setBillingType(((BillingCreateBillingForm)form).getXml_billtype());
        bean.setVisitType(((BillingCreateBillingForm)form).getXml_visittype());
        bean.setVisitLocation(((BillingCreateBillingForm)form).getXml_location());
        bean.setServiceDate(((BillingCreateBillingForm)form).getXml_appointment_date());
        bean.setStartTime(((BillingCreateBillingForm)form).getXml_starttime());
        bean.setEndTime(((BillingCreateBillingForm)form).getXml_endtime());
        bean.setAdmissionDate(((BillingCreateBillingForm)form).getXml_vdate());
        bean.setBillingProvider(((BillingCreateBillingForm)form).getXml_provider());
        
        oscar.oscarBilling.data.BillingFormData billform = new oscar.oscarBilling.data.BillingFormData();
        
        bean.setBillingPracNo(billform.getPracNo(((BillingCreateBillingForm)form).getXml_provider()));
        bean.setBillingGroupNo(billform.getGroupNo(((BillingCreateBillingForm)form).getXml_provider()));
        
        
        
        bean.setDx1(((BillingCreateBillingForm)form).getXml_diagnostic_detail1());
        bean.setDx2(((BillingCreateBillingForm)form).getXml_diagnostic_detail2());
        bean.setDx3(((BillingCreateBillingForm)form).getXml_diagnostic_detail3());
        bean.setReferral1(((BillingCreateBillingForm)form).getXml_refer1());
        bean.setReferral2(((BillingCreateBillingForm)form).getXml_refer2());
        bean.setReferType1(((BillingCreateBillingForm)form).getRefertype1());
        bean.setReferType2(((BillingCreateBillingForm)form).getRefertype2());
        bean.setBillItem(billItem);
        
        //bean.setApptProviderNo(request.getParameter("apptProvider_no"));
        //bean.setPatientName(request.getParameter("demographic_name"));
        //bean.setProviderView(request.getParameter("providerview"));
        //bean.setBillRegion(request.getParameter("billRegion"));
        //bean.setBillForm(request.getParameter("billForm"));
        //bean.setCreator(request.getParameter("user_no"));
        //bean.setPatientNo(request.getParameter("demographic_no"));
        //bean.setApptNo(request.getParameter("appointment_no"));
        //bean.setApptDate(request.getParameter("appointment_date"));
        
        
        
        
        
        
        
        
        
        //      System.out.println("Service count : "+ billItem.size());
        
        return (mapping.findForward("success"));
    }
    
}
