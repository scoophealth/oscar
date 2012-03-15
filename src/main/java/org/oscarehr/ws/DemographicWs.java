package org.oscarehr.ws;

import javax.jws.WebService;

import org.oscarehr.common.model.Demographic;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.ws.transfer_objects.DemographicTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
public class DemographicWs extends AbstractWs {
	@Autowired
	private DemographicManager demographicManager;
	
	public DemographicTransfer getDemographic(Integer demographicId)
	{
		Demographic demographic=demographicManager.getDemographic(demographicId);
		return(DemographicTransfer.toTransfer(demographic));
	}

	public DemographicTransfer getDemographicByMyOscarUserName(String myOscarUserName)
	{
		Demographic demographic=demographicManager.getDemographicByMyOscarUserName(myOscarUserName);
		return(DemographicTransfer.toTransfer(demographic));
	}
}
