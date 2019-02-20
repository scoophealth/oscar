package org.oscarehr.ws.rest.conversion;

import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.rest.to.model.BillingDetailTo1;
import org.springframework.beans.BeanUtils;

public class BillingDetailConverter extends AbstractConverter<BillingONCHeader1, BillingDetailTo1> {
  @Override
  public BillingONCHeader1 getAsDomainObject(LoggedInInfo loggedInInfo, BillingDetailTo1 b) throws ConversionException {
    return null;
  }

  @Override
  public BillingDetailTo1 getAsTransferObject(LoggedInInfo loggedInInfo, BillingONCHeader1 billingONCHeader1) throws ConversionException {
    try {
      BillingDetailTo1 result = new BillingDetailTo1();
      result.setId(billingONCHeader1.getId());
      result.setHeaderId(billingONCHeader1.getHeaderId());
      result.setTranscId(billingONCHeader1.getTranscId());
      result.setRecId(billingONCHeader1.getRecId());
      result.setHin(billingONCHeader1.getHin());
      result.setVer(billingONCHeader1.getVer());
      result.setPayProgram(billingONCHeader1.getPayProgram());
      result.setPayee(billingONCHeader1.getPayee());
      result.setAdmissionDate(billingONCHeader1.getAdmissionDate());
      result.setDemographicNo(billingONCHeader1.getDemographicNo());
      result.setProviderNo(billingONCHeader1.getProviderNo());
      result.setAppointmentNo(billingONCHeader1.getAppointmentNo());
      result.setProvince(billingONCHeader1.getProvince());
      result.setBillingDate(billingONCHeader1.getBillingDate());
      result.setBillingTime(billingONCHeader1.getBillingTime());
      result.setTotal(billingONCHeader1.getTotal());
      result.setPaid(billingONCHeader1.getPaid());
      result.setStatus(billingONCHeader1.getStatus());
      result.setVisitType(billingONCHeader1.getVisitType());
      result.setProviderOhipNo(billingONCHeader1.getProviderOhipNo());
      result.setProviderRmaNo(billingONCHeader1.getProviderRmaNo());
      result.setApptProviderNo(billingONCHeader1.getApptProviderNo());
      result.setAsstProviderNo(billingONCHeader1.getAsstProviderNo());
      result.setCreator(billingONCHeader1.getCreator());
      result.setBillingItems(billingONCHeader1.getBillingItems());

      return result;

    } catch (Exception e) {
      throw new ConversionException(e);
    }
  }
}
