package org.oscarehr.casemgmt.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.oscarehr.casemgmt.dao.ClientImageDAO;
import org.oscarehr.casemgmt.model.ClientImage;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ClientImageDAOHibernate extends HibernateDaoSupport implements
		ClientImageDAO
{
	public ClientImage getClientImage(String id, String image_type) {
		List results = this.getHibernateTemplate().find("from ClientImage c where c.demographic_no = ? and c.image_type = ? order by c.update_date desc",new Object[] {id,image_type});
		if(results.size()>0) {
			return (ClientImage)results.get(0);
		}
		return null;
	}

	public void saveClientImage(ClientImage clientImage) {
		ClientImage existing = getClientImage(String.valueOf(clientImage.getDemographic_no()));
		if(existing != null) {
			existing.setImage_data(clientImage.getImage_data());
			existing.setImage_type(clientImage.getImage_type());
			existing.setUpdate_date(new Date());
		}
		this.getHibernateTemplate().saveOrUpdate(clientImage);
	}

	public ClientImage getClientImage(String clientId) {
		List results = this.getHibernateTemplate().find("from ClientImage i where i.demographic_no=?",clientId);
		if(results.size()>0) {
			return (ClientImage)results.get(0);
		}
		return null;
	}
}