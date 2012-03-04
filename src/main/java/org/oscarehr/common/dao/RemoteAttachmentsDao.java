package org.oscarehr.common.dao;

import org.oscarehr.common.model.RemoteAttachments;
import org.springframework.stereotype.Repository;

@Repository
public class RemoteAttachmentsDao extends AbstractDao<RemoteAttachments>{

	public RemoteAttachmentsDao() {
		super(RemoteAttachments.class);
	}
}
