package org.oscarehr.ws;

import java.util.List;

import javax.jws.WebService;

import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.ProviderManager2;
import org.oscarehr.ws.transfer_objects.ProviderTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
public class ProviderWs extends AbstractWs {
	@Autowired
	private ProviderManager2 providerManager;

	public ProviderTransfer[] getProviders(boolean active) {
		List<Provider> tempResults = providerManager.getProviders(active);

		ProviderTransfer[] results = ProviderTransfer.toTransfers(tempResults);

		return (results);
	}

}
