/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.myoscar.utils;


import java.util.Arrays;

public class MessageTransferCacheKey
{
	private Long personId;
	private String sessionId;
	private byte[] messageId;
	
	public MessageTransferCacheKey(Long personId, String sessionId, byte[] messageId)
	{
		this.personId = personId;
		this.sessionId = sessionId;
		this.messageId = messageId;
	}
	
	@Override
	public boolean equals(Object o)
	{
		try
		{
			MessageTransferCacheKey key=(MessageTransferCacheKey)o;
			if (!personId.equals(key.personId)) return(false);
			if (!Arrays.equals(messageId, key.messageId)) return(false);
			if (!sessionId.equals(key.sessionId)) return(false);
			return(true);
		}
		catch (Exception e)
		{
			return(false);
		}
	}
	
	@Override
	public int hashCode()
	{
		return(sessionId.hashCode());
	}
}
