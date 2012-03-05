package org.oscarehr.common.dao;

import org.oscarehr.common.model.AppointmentArchive;
import org.springframework.stereotype.Repository;

@Repository
public class AppointmentArchiveDao extends AbstractDao<AppointmentArchive> {

	public AppointmentArchiveDao() {
		super(AppointmentArchive.class);
	}
}
