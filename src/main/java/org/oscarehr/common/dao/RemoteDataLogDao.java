package org.oscarehr.common.dao;

import org.oscarehr.common.model.RemoteDataLog;
import org.springframework.stereotype.Repository;

@Repository
public class RemoteDataLogDao extends AbstractDao<RemoteDataLog>
{
	public RemoteDataLogDao()
	{
		super(RemoteDataLog.class);
	}
}
