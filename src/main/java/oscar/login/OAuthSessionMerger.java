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

/*
 * Written by Brandon Aubie
 * brandon@aubie.ca
 */


package oscar.login;

import org.oscarehr.common.dao.ServiceRequestTokenDao;
import org.oscarehr.common.model.ServiceRequestToken;
import javax.servlet.http.HttpServletRequest;
import org.oscarehr.util.SpringUtils;


public class OAuthSessionMerger {

    public static boolean mergeSession(HttpServletRequest request) {

    		String proNo = (String)request.getSession().getAttribute("user");
    		ServiceRequestTokenDao serviceRequestTokenDao = SpringUtils.getBean(ServiceRequestTokenDao.class);
    		ServiceRequestToken srt = serviceRequestTokenDao.findByTokenId(request.getParameter("oauth_token"));
    		if(srt != null) {
    			srt.setProviderNo(proNo);
    			serviceRequestTokenDao.merge(srt);
                return true;
    		}
            return false;
    }

}
