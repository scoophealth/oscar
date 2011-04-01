package org.oscarehr.phr.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.RemoteDataRetrievalLogDao;
import org.oscarehr.common.model.RemoteDataRetrievalLog;
import org.oscarehr.myoscar_server.ws.MessageTransfer;
import org.oscarehr.myoscar_server.ws.MessageWs;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class MyOscarMessageManager {
	private static final Logger logger=MiscUtils.getLogger();
	
	private static RemoteDataRetrievalLogDao remoteDataRetrievalLogDao=(RemoteDataRetrievalLogDao) SpringUtils.getBean("remoteDataRetrievalLogDao");
	
	public static ArrayList<MessageTransfer> getReceivedMessages(Long myOscarUserId, String myOscarPassword)
	{		
		MessageWs messageWs=MyOscarServerWebServicesManager.getMessageWs(myOscarUserId, myOscarPassword);
		
		int startIndex=0;
		int itemsToReturn=100;
		List<MessageTransfer> messageTransfers;
		ArrayList<MessageTransfer> allResults=new ArrayList<MessageTransfer>();
		do
		{
			messageTransfers=messageWs.getReceivedMessages(myOscarUserId, true, startIndex, itemsToReturn);
			logger.debug("Mesages Retrieved from MyOsar Server : "+messageTransfers.size());
			
			for (MessageTransfer messageTransfer : messageTransfers)
			{
				logMessageRetrieved(messageTransfer);
				allResults.add(messageTransfer);		
			}
			
			startIndex=startIndex+itemsToReturn;
		}
		while (messageTransfers.size()>=itemsToReturn && startIndex<=5000); // 5000 arbitrary runaway limit for now
		
		return(allResults);
	}

	private static void logMessageRetrieved(MessageTransfer messageTransfer) {
		RemoteDataRetrievalLog remoteDataRetrievalLog=new RemoteDataRetrievalLog();
		
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		remoteDataRetrievalLog.setProviderNo(loggedInInfo.loggedInProvider.getProviderNo());
		
		remoteDataRetrievalLog.setMyOscarRemoteDocumentId(MyOscarServerWebServicesManager.getMyOscarServerBaseUrl(), "MESSAGE", messageTransfer.getId());
		
		remoteDataRetrievalLog.setRemoteDocumentContents(ReflectionToStringBuilder.toString(messageTransfer));
		
		remoteDataRetrievalLogDao.persist(remoteDataRetrievalLog);
    }
}
