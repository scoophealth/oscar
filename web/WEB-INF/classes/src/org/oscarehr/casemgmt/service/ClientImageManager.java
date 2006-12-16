package org.oscarehr.casemgmt.service;

import org.oscarehr.casemgmt.model.ClientImage;

public interface ClientImageManager {
//	client image
	public ClientImage getClientImage(String clientId);
	public void saveClientImage(ClientImage img);
	
	public byte[] getClientImage(String id,String image_type);
	public void saveClientImage(String id,byte[] image_data,String image_type);
}
