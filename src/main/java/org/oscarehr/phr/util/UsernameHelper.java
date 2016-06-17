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

package org.oscarehr.phr.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.oscarehr.myoscar.client.ws_manager.AccountManager;
import org.oscarehr.myoscar.client.ws_manager.MyOscarLoggedInInfoInterface;
import org.oscarehr.myoscar_server.ws.MinimalPersonTransfer2;

public class UsernameHelper
{

	public static List<String> suggestUsernames(MyOscarLoggedInInfoInterface credentials, String fn, String ln, String email)
	{
		List<String> list = new ArrayList<String>();
		
		if (email != null && !"".equals(email) && email.indexOf("@") != -1){
			
			String emailPredicate = email.substring(0, email.indexOf("@"));
			
			if(UsernameHelper.isUserNameValid(credentials, emailPredicate))
			{
				list.add(emailPredicate);
			}
		}
		
		String un1 = UsernameHelper.getDefaultUserName(fn, ln);
		if (UsernameHelper.isUserNameValid(credentials, un1))
		{
			list.add(un1);
		}

		String un2 = UsernameHelper.getConcatUsername(fn, ln);
		if (UsernameHelper.isUserNameValid(credentials, un2))
		{
			list.add(un2);
		}
		
		if(list.size() < 3){
			String un3 = UsernameHelper.getFirstInitialUsername(fn, ln);
			if (UsernameHelper.isUserNameValid(credentials, un3))
			{
				list.add(un3);
			}
		}

		if(list.size() < 3){
			String un4;
			if (fn.length() > 3) {
				un4 = getTruncatedUsername(fn, ln);
			} else {
				un4 = getNumberedUsername(fn,ln);
			}
			if (UsernameHelper.isUserNameValid(credentials, un4))
			{
				list.add(un4);
			}
		}
				
		return list;

	}

	public static String stripName(String name)
	{
		String rn = name.trim().replaceAll("\\s", "").toLowerCase();
		rn = rn.replaceAll("\\(.*", "");

		return rn;
	}
	
	private static boolean isUserNameValid(MyOscarLoggedInInfoInterface credentials, String username)
	{

		MinimalPersonTransfer2 requestedPerson = AccountManager.getMinimalPerson(credentials, username);
		if (requestedPerson == null) return true;

		return false;
	}

	private static String getDefaultUserName(String fn, String ln)
	{
		return(fn + '.' + ln);
	}

	private static String getConcatUsername(String fn, String ln)
	{
		return fn + ln;
	}

	private static String getFirstInitialUsername(String fn, String ln)
	{
		return fn.substring(1, 1) + ln;
	}

	private static String getTruncatedUsername(String fn, String ln)
	{
		return fn.substring(0, 3) + ln;
	}
	
	private static String getNumberedUsername(String fn, String ln)
	{
		Random r = new Random();
		return fn + ln + r.nextInt(100);
	}
		

}
