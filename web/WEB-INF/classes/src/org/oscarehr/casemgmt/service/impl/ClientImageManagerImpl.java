package org.oscarehr.casemgmt.service.impl;

import org.oscarehr.casemgmt.dao.ClientImageDAO;
import org.oscarehr.casemgmt.model.ClientImage;

public class ClientImageManagerImpl implements
		org.oscarehr.casemgmt.service.ClientImageManager
{
	protected ClientImageDAO clientImageDAO;

	public void setClientImageDAO(ClientImageDAO dao) {
		this.clientImageDAO = dao;
	}
	
	public byte[] getClientImage(String id,String image_type)
	{
		//System.out.println("QQQQQQQQQQQQQQQQQQQQQ"+id +image_type);
		ClientImage image = clientImageDAO.getClientImage(id,image_type);
		if(image != null) {
			return image.getImage_data();
		}
		return null;
	}
	
	public void saveClientImage(String id,byte[] image_data,String image_type)
	{
		ClientImage clientImage=new ClientImage();
		clientImage.setDemographic_no(Long.valueOf(id).longValue());
		clientImage.setImage_data(image_data);
		clientImage.setImage_type(image_type);
		clientImageDAO.saveClientImage(clientImage);
	}
	
	public ClientImage getClientImage(String clientId) {
		return clientImageDAO.getClientImage(clientId);
	}
	
	public void saveClientImage(ClientImage img) {
		clientImageDAO.saveClientImage(img);
	}
	
}
