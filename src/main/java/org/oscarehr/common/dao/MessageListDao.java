package org.oscarehr.common.dao;

import org.oscarehr.common.model.MessageList;
import org.springframework.stereotype.Repository;

@Repository
public class MessageListDao extends AbstractDao<MessageList>{

	public MessageListDao() {
		super(MessageList.class);
	}
}
