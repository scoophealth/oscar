package org.oscarehr.common.dao;

import org.oscarehr.common.model.RemoteDataRetrievalLog;
import org.springframework.stereotype.Repository;

@Repository
public class RemoteDataRetrievalLogDao extends AbstractDao<RemoteDataRetrievalLog>
{
	public RemoteDataRetrievalLogDao()
	{
		super(RemoteDataRetrievalLog.class);
	}
}
