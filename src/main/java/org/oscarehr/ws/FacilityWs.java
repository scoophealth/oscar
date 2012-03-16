package org.oscarehr.ws;

import java.util.List;

import javax.jws.WebService;

import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.model.Facility;
import org.oscarehr.ws.transfer_objects.FacilityTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
public class FacilityWs extends AbstractWs {
	@Autowired
	private FacilityDao facilityDao;

	public FacilityTransfer getDefaultFacilities() {
		List<Facility> results=facilityDao.findAll(true);
		if (results.size()==0)
			{
			return(null);
			}
		else
		{
			return(FacilityTransfer.toTransfer(results.get(0)));
		}
	}
}
