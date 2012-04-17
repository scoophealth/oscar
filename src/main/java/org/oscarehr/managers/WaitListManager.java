package org.oscarehr.managers;

import org.oscarehr.PMmodule.dao.ProgramDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WaitListManager
{
	@Autowired
	private ProgramDao programDao;

	public void sendImmediateNotification()
	{
		
	}
	
	public void checkAndSendDailyNotification()
	{
		
	}
}
