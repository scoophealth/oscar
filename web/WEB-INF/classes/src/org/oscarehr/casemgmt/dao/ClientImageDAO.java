package org.oscarehr.casemgmt.dao;

import org.oscarehr.casemgmt.model.ClientImage;

public interface ClientImageDAO extends DAO {
	
	public ClientImage getClientImage(String id,String image_type);
	
	public void saveClientImage(ClientImage clientImage);
	
	public ClientImage getClientImage(String clientId);
}

